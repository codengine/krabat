/*
    The Krabat Adventure
    Copyright (C) 2001  Rapaki 
    http://www.rapaki.de

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package rapaki.krabat.platform.java;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rapaki.krabat.sound.AbstractPlayer;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class OGGPlayer extends AbstractPlayer {
    private static final Logger log = LoggerFactory.getLogger(OGGPlayer.class);
    public static final String MUSIC_DIR = "music/ogg/";
    public static final String MUSIC_SUFFIX = ".ogg";

    public OGGPlayer(Path workingDir) {
        super(workingDir);
    }

    private volatile PlayThread pThread;
    private final Object playerLock = new Object();

    public void play(final String filename, final boolean repeat) {
        synchronized (playerLock) {
            stopPlaying();

            pThread = new PlayThread(workingDir.resolve(MUSIC_DIR).resolve(filename), repeat);
            pThread.setDaemon(true);
            pThread.start();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void stop() {
        synchronized (playerLock) {
            stopPlaying();
        }
    }

    public String getMusicSuffix() {
        return MUSIC_SUFFIX;
    }

    private void stopPlaying() {
        if (pThread != null) {
            synchronized (playerLock) {
                if(pThread != null) {
                    pThread.terminate();

                    // waiting for termination might be optional...
                    try {
                        pThread.join();
                    } catch (InterruptedException e) {
                        log.warn("Playback thread interrupted", e);
                    }
                }
            }
        }
    }

    /**
     * OGG decoding is stolen from "DecodeExample"
     *
     * @author daniel
     */
    class PlayThread extends Thread {

        private final Path filepath;

        private final boolean repeat;

        private boolean isRunning;

        private int convsize = 4096 * 2;

        private final byte[] convbuffer = new byte[convsize]; // take 8k out of the data segment, not the stack

        public PlayThread(Path filepath, boolean repeat) {
            this.filepath = filepath;
            this.repeat = repeat;
        }

        public void run() {

            isRunning = true;

            System.out.println("Playing " + filepath + " starts.");

            try {
                outer_endless:
                do {
                    BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(filepath.toFile()));

                    SyncState oy = new SyncState(); // sync and verify incoming physical bitstream
                    StreamState os = new StreamState(); // take physical pages, weld into a logical stream of packets
                    Page og = new Page(); // one Ogg bitstream page.  Vorbis packets are inside
                    Packet op = new Packet(); // one raw packet of data for decode

                    Info vi = new Info(); // struct that stores all the static vorbis bitstream settings
                    Comment vc = new Comment(); // struct that stores all the bitstream user comments
                    DspState vd = new DspState(); // central working state for the packet->PCM decoder
                    Block vb = new Block(vd); // local working space for packet->PCM decode

                    byte[] buffer;
                    int bytes;

                    oy.init(); // Now we can read pages

                    while (isRunning) { // we repeat if the bitstream is chained
                        boolean eos = false;

                        // submit a 4k block to libvorbis' Ogg layer
                        int index = oy.buffer(4096);
                        buffer = oy.data;
                        bytes = inStream.read(buffer, index, 4096);
                        oy.wrote(bytes);

                        // Get the first page.
                        if (oy.pageout(og) != 1) {
                            // have we simply run out of data?  If so, we're done.
                            if (bytes < 4096) {
                                break;
                            }

                            System.err.println("Input does not appear to be an Ogg bitstream.");
                            isRunning = false;
                            break;
                        }

                        os.init(og.serialno());

                        vi.init();
                        vc.init();
                        if (os.pagein(og) < 0) {
                            // error; stream version mismatch perhaps
                            System.err.println("Error reading first page of Ogg bitstream data.");
                            isRunning = false;
                            break;
                        }

                        if (os.packetout(op) != 1) {
                            // no page? must not be vorbis
                            System.err.println("Error reading initial header packet.");
                            isRunning = false;
                            break;
                        }

                        if (vi.synthesis_headerin(vc, op) < 0) {
                            // error case; not a vorbis header
                            System.err
                                    .println("This Ogg bitstream does not contain Vorbis audio data.");
                            isRunning = false;
                            break;
                        }

                        int i = 0;
                        while (i < 2) {
                            while (i < 2) {

                                int result = oy.pageout(og);
                                if (result == 0) {
                                    break; // Need more data
                                }

                                if (result == 1) {
                                    os.pagein(og); // we can ignore any errors here
                                    // as they'll also become apparent
                                    // at packetout
                                    while (i < 2) {
                                        result = os.packetout(op);
                                        if (result == 0) {
                                            break;
                                        }
                                        if (result == -1) {
                                            // Uh oh; data at some point was corrupted or missing!
                                            // We can't tolerate that in a header.  Die.
                                            System.err.println("Corrupt secondary header.  Exiting.");
                                            isRunning = false;
                                            break outer_endless;
                                        }
                                        vi.synthesis_headerin(vc, op);
                                        i++;
                                    }
                                }
                            }
                            // no harm in not checking before adding more
                            index = oy.buffer(4096);
                            buffer = oy.data;
                            bytes = inStream.read(buffer, index, 4096);

                            if (bytes == 0 && i < 2) {
                                System.err.println("End of file before finding all Vorbis headers!");
                                isRunning = false;
                                break outer_endless;
                            }
                            oy.wrote(bytes);
                        }

                        // Throw the comments plus a few lines about the bitstream we're
                        // decoding
                        {
                            byte[][] ptr = vc.user_comments;
                            for (int j = 0; j < ptr.length; j++) {
                                if (ptr[j] == null) {
                                    break;
                                }
                                System.err.println(new String(ptr[j], 0, ptr[j].length - 1));
                            }
                            System.err.println("\nBitstream is " + vi.channels + " channel, " + vi.rate
                                    + "Hz");
                            System.err.println("Encoded by: "
                                    + new String(vc.vendor, 0, vc.vendor.length - 1) + "\n");
                        }

                        convsize = 4096 / vi.channels;

                        // OK, got and parsed all three headers. Initialize the Vorbis
                        //  packet->PCM decoder.
                        vd.synthesis_init(vi); // central decode state
                        vb.init(vd); // local state for most of the decode
                        // so multiple block decodes can
                        // proceed in parallel.  We could init
                        // multiple vorbis_block structures
                        // for vd here

                        DataLine.Info info = new DataLine.Info(SourceDataLine.class,
                                new AudioFormat(vi.rate, 16, vi.channels, true, false));
                        SourceDataLine outLine = (SourceDataLine) AudioSystem.getLine(info);
                        outLine.open();
                        outLine.start();

                        float[][][] _pcm = new float[1][][];
                        int[] _index = new int[vi.channels];
                        // The rest is just a straight decode loop until end of stream
                        while (!eos && isRunning) {
                            while (!eos && isRunning) {

                                int result = oy.pageout(og);
                                if (result == 0) {
                                    break; // need more data
                                }
                                if (result == -1) { // missing or corrupt data at this page position
                                    System.err
                                            .println("Corrupt or missing data in bitstream; continuing...");
                                } else {
                                    os.pagein(og); // can safely ignore errors at
                                    // this point
                                    while (true) {
                                        result = os.packetout(op);

                                        if (result == 0) {
                                            break; // need more data
                                        }

                                        if (result != -1) {
                                            // we have a packet.  Decode it
                                            int samples;
                                            if (vb.synthesis(op) == 0) { // test for success!
                                                vd.synthesis_blockin(vb);
                                            }

                                            // **pcm is a multichannel float vector.  In stereo, for
                                            // example, pcm[0] is left, and pcm[1] is right.  samples is
                                            // the size of each channel.  Convert the float values
                                            // (-1.<=range<=1.) to whatever PCM format and write it out

                                            while ((samples = vd.synthesis_pcmout(_pcm, _index)) > 0) {
                                                float[][] pcm = _pcm[0];
                                                int bout = Math.min(samples, convsize);

                                                // convert floats to 16 bit signed ints (host order) and
                                                // interleave
                                                for (i = 0; i < vi.channels; i++) {
                                                    int ptr = i * 2;
                                                    //int ptr=i;
                                                    int mono = _index[i];
                                                    for (int j = 0; j < bout; j++) {
                                                        int val = (int) (pcm[i][mono + j] * 32767.);
                                                        //              short val=(short)(pcm[i][mono+j]*32767.);
                                                        //              int val=(int)Math.round(pcm[i][mono+j]*32767.);
                                                        // might as well guard against clipping
                                                        if (val > 32767) {
                                                            val = 32767;
                                                        }
                                                        if (val < -32768) {
                                                            val = -32768;
                                                        }
                                                        if (val < 0) {
                                                            val = val | 0x8000;
                                                        }
                                                        convbuffer[ptr] = (byte) val;
                                                        convbuffer[ptr + 1] = (byte) (val >>> 8);
                                                        ptr += 2 * vi.channels;
                                                    }
                                                }

                                                // TODO write to soundcard
                                                outLine.write(convbuffer, 0, 2 * vi.channels * bout);

                                                // tell libvorbis how
                                                // many samples we
                                                // actually consumed
                                                vd.synthesis_read(bout);
                                            }
                                        }
                                    }
                                    if (og.eos() != 0) {
                                        eos = true;
                                    }
                                }
                            }
                            if (!eos) {
                                index = oy.buffer(4096);
                                buffer = oy.data;
                                bytes = inStream.read(buffer, index, 4096);
                                oy.wrote(bytes);
                                if (bytes == 0) {
                                    eos = true;
                                }
                            }
                        }

                        outLine.stop();
                        outLine.close();

                        // clean up this logical bitstream; before exit we see if we're
                        // followed by another [chained]

                        os.clear();

                        // ogg_page and ogg_packet structs always point to storage in
                        // libvorbis.  They're never freed or manipulated directly

                        vb.clear();
                        vd.clear();
                        vi.clear(); // must be called last
                    }

                    // OK, clean up the framer
                    oy.clear();
                } while (isRunning && repeat);
            } catch (FileNotFoundException e) {
                log.error("File not found.", e);
            } catch (IOException e) {
                log.error("I/O error.", e);
            } catch (LineUnavailableException e) {
                log.error("Line unavailable.", e);
            }

            System.out.println("Playing " + filepath + " stops.");
        }

        public void terminate() {
            isRunning = false;
        }
    }
}
