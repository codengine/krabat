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

package de.codengine.krabat.locations4;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.PtackZaRapaka;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Extro extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Extro.class);
    private final GenericImage[] Extropics;
    private GenericImage ludzo_vor_buehne;
    private int PicIndex = 0;

    private final PtackZaRapaka rapak;

    private boolean setAnim = true;

    private boolean rapakVisible = true;
    private boolean isWotrowLocation = true;

    private final GenericPoint scrollPoint = new GenericPoint(320, 1300);

    private String scrollerOutputText;
    private boolean Scroller = false;

    private int FadeToBlack = 0;

    // Konstante Points
    private static final GenericPoint talkPoint = new GenericPoint(320, 350);
    private static final GenericPoint talkPointCopyleft = new GenericPoint(320, 425);

    // Konstante Rects
    private static final GenericRectangle kleinesRect = new GenericRectangle(60, 100, 580, 180);
    // private static final Rectangle kleinesRect = new Rectangle (60, 0, 580, 280);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz vom Extro erzeugen
    public Extro(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 519;
        mainFrame.krabat.zoomf = 4.5f;
        mainFrame.krabat.defScale = -10;

        Extropics = new GenericImage[3];

        // rapak = new ptack2 (caller, 460, 55, 40, -50);
        rapak = new PtackZaRapaka(caller, 560, 50, 30, -50);

        InitLocation();

        mainFrame.Freeze(false);

        // Rapak gleich mal am Anfang kreischen lassen
        mainFrame.wave.PlayFile("sfx/rapak1.wav");
    }

    private void InitLocation() {
        InitImages();
    }

    private void InitImages() {
        Extropics[0] = getPicture("gfx/wotrow/wotrow.gif");
        Extropics[1] = getPicture("gfx/wotrow/buehne2.gif");
        Extropics[2] = getPicture("gfx/wotrow/endbild.gif");

        ludzo_vor_buehne = getPicture("gfx/wotrow/bludzo.gif");
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
            if (setAnim) {
                mainFrame.fPlayAnim = true;
            }
        }

        // Beim Scroller Extrawurst
        if (Scroller) {
            g.setClip(kleinesRect);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(Extropics[PicIndex], 0, 0);

        // Raben zeichnen, solange da
        if (rapakVisible) {
            g.setClip(rapak.ptack2Rect());
            g.drawImage(Extropics[PicIndex], 0, 0);
            rapakVisible = rapak.Flieg(g);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // hier ist der Sound...
        evalSound();

        // Krabat einen Schritt gehen lassen
        mainFrame.wegGeher.GeheWeg();

        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "") || Scroller) {
            int textColor = FarbenArray[TalkPerson];
            if (Scroller) {
                textColor = FarbenArray[92];
            }

            // welchen Text ausgeben
            String tempText = outputText;
            if (Scroller) {
                tempText = scrollerOutputText;
            }

            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            if (!Scroller) {
                g.setClip(0, 0, 644, 484);
            }
            mainFrame.ifont.drawString(g, tempText, outputTextPos.x, outputTextPos.y, textColor);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        if (Scroller) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 176, 644, 176 + 154);
            g.drawImage(ludzo_vor_buehne, 0, 176);
            g.setClip(my);
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Anim starten
        if (setAnim) {
            setAnim = false;
            nextActionID = 10;
        }

        // Hier das FadeToBlack, wenn noetig
        if (FadeToBlack > 0) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);

            g.clearRect(0, 0, FadeToBlack, 479);
            g.clearRect(639 - FadeToBlack, 0, 639, 479);
            g.clearRect(0, 0, 639, FadeToBlack);
            g.clearRect(0, 479 - FadeToBlack, 639, 479);

            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // GenericPoint pTemp = e.getPoint ();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.Nix);
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Umgebungs-Sounds abspielen
    private void evalSound() {
        // Ambient-Sounds fuer Wotrow (1.Teil des Extros)
        if (isWotrowLocation) {
            int zfz = (int) (Math.random() * 100);

            if (zfz > 92) {
                mainFrame.wave.PlayFile("sfx/grillen.wav");
            }

            if (zfz > 98) {
                int zfz2 = (int) (Math.random() * 1.99f);

                if (zfz2 < 1) {
                    mainFrame.wave.PlayFile("sfx/uhu1.wav");
                } else {
                    mainFrame.wave.PlayFile("sfx/uhu2.wav");
                }
            }

            // Rapak-Gekreische (wenn er da ist)
            if (rapakVisible) {
                if (zfz > 97) {
                    mainFrame.wave.PlayFile("sfx/rapak1.wav");
                }
            }
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        switch (nextActionID) {

            case 10:
                // warten, bis Rabe rausgeflogen ist
                if (!rapakVisible) {
                    nextActionID = 1000;
                }
                break;

            case 1000:
                // ersten Text ausgeben
                BackgroundMusicPlayer.getInstance().playTrack(18, true);
                isWotrowLocation = false; // ab jetzt Background aus (stoert CD-Track nicht)
                PersonSagt("Extro_2", 0, 54, 2, 1010, talkPoint);
                break;

            case 1010:
                // zweiten Text
                PersonSagt("Extro_3",
                        0, 54, 2, 1020, talkPoint);
                break;

            case 1020:
                // dritten Text
                PersonSagt("Extro_4",
                        0, 54, 2, 8000, talkPoint);
                break;

            case 8000:
                // Bild umschalten
                mainFrame.Clipset = false;
                PicIndex++;
                nextActionID = 9000;
                mainFrame.wave.PlayFile("sfx/applaus.wav");
                break;

            case 9000:
                // Extro-Scroller (testweise)
                Scroller = true;
                scrollerOutputText = Start.stringManager.getTranslation("Extro_5");
                outputTextPos = mainFrame.ifont.CenterAnimText(scrollerOutputText, scrollPoint);
                TalkPerson = 54;
                nextActionID = 9010;
                break;

            case 9010:
                // scroller hochschieben
                outputText = Start.stringManager.getTranslation("Extro_5");
                mainFrame.Clipset = false;
                outputTextPos.y -= 2;
                TalkPerson = 54;
                if (outputTextPos.y < -1000) {
                    nextActionID = 9500;
                }
                log.debug("Point x : {} y : {}", outputTextPos.x, outputTextPos.y);
                break;

            case 9500:
                // End-Bild
                Scroller = false;
                mainFrame.Clipset = false;
                PicIndex++;
                nextActionID = 9510;
                break;

            case 9510:
                PersonSagt("Extro_1", 0, 54, 2, 9520, talkPointCopyleft);
                break;

            case 9520:
                // Ausfaden starten
                FadeToBlack = 1;
                nextActionID = 9530;
                break;

            case 9530:
                // bis zum Ende des Ausfadens warten
                FadeToBlack += 3;
                if (FadeToBlack >= 246) {
                    nextActionID = 10000;
                }
                break;

            case 10000:
                // Ende
                mainFrame.wave.PlayFile("sfx/wowca.wav");
                Scroller = false;
                // mainFrame.setVisible (false);
                BackgroundMusicPlayer.getInstance().stop();
                // mainFrame.dispose ();
                System.exit(0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}