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

import rapaki.krabat.platform.GenericSoundEffectPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import java.net.URL;

public class JavaSoundEffectPlayer extends GenericSoundEffectPlayer {

	public JavaSoundEffectPlayer(String urlBase) {
        super(urlBase);
    }
    
    public void PlayFile (String filename) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream (new URL (urlBase + filename));
            DataLine.Info info = new DataLine.Info (Clip.class, stream.getFormat ());
            Clip clip = (Clip) AudioSystem.getLine (info);
            clip.open (stream);
            clip.start ();
        } 
        catch (Exception e) {
            System.err.println ("Fehler bei Wiedergabe der Datei '" + filename + "'!");
            e.printStackTrace ();
        }
    }    
}
