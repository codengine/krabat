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

package de.codengine.krabat.locations3;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.OldMiller;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Mlynkmurja extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Mlynkmurja.class);
    private GenericImage backr;
    private GenericImage laterne;

    private final OldMiller mueller;
    private final GenericPoint muellerPoint;
    private final GenericPoint muellerTalk;

    private boolean krabatVisible = true;

    private final int laterneAdd;

    private boolean initplay = true;

    // Konstante Points
    private static final GenericPoint muellerFeet = new GenericPoint(128, 466);
    private static final GenericPoint Pmueller = new GenericPoint(210, 475);
    private static final GenericPoint Pexit = new GenericPoint(0, 475);

    // Konstante ints
    private static final int Muellerzooming = 45;  // scaling analog Krabat

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mlynkmurja(Start caller) {
        super(caller, 129);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();
        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 469;
        mainFrame.krabat.zoomf = 1f;
        mainFrame.krabat.defScale = 35;

        InitLocation();

        // Mueller initialisieren
        mueller = new OldMiller(mainFrame, Muellerzooming);

        muellerPoint = mueller.getMuellerPoint(muellerFeet);
        muellerTalk = mueller.getTalkPoint(muellerFeet);

        nextActionID = 10;

        float xtf = 900 - (float) ((640 - 110) * 4) / 7;
        laterneAdd = (int) (xtf - 640);

        // System.out.println ("Laterne wird auf Pos. " + laterneAdd + " gezeichnet.");

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(0, 90, 0, 90, 473, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(91, 619, 91, 619, 470, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(620, 630, 620, 630, 435, 479));

        mainFrame.pathFinder.ClearMatrix(3);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);

        InitImages();

        mainFrame.krabat.setPos(new GenericPoint(607, 475));
        mainFrame.krabat.SetFacing(9);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backr = getPicture("gfx-dd/murja/murja-r.png");
        laterne = getPicture("gfx-dd/murja/laterna.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        if (initplay) {
            initplay = false;
            mainFrame.soundPlayer.PlayFile("sfx-dd/mlynk-dd.wav");
        }

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
            mainFrame.isAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(backr, 0, 0);
        g.drawImage(laterne, laterneAdd, 0);

        // Mueller zeichnen
        g.setClip(muellerPoint.x, muellerPoint.y, OldMiller.Breite, OldMiller.Hoehe);
        g.drawImage(backr, 0, 0);
        mueller.drawOldmlynk(g, TalkPerson, muellerPoint);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        mainFrame.pathWalker.GeheWeg();

        // Krabat zeichnen

        if (krabatVisible) {
            // Animation??
            if (mainFrame.krabat.nAnimation != 0) {
                mainFrame.krabat.DoAnimation(g);

                // Cursorruecksetzung nach Animationsende
                if (mainFrame.krabat.nAnimation == 0) {
                    evalMouseMoveEvent(mainFrame.mousePoint);
                }
            } else {
                if (mainFrame.talkCount > 0 && TalkPerson != 0) {
                    // beim Reden
                    switch (TalkPerson) {
                        case 1:
                            // Krabat spricht gestikulierend
                            mainFrame.krabat.talkKrabat(g);
                            break;
                        case 3:
                            // Krabat spricht im Monolog
                            mainFrame.krabat.describeKrabat(g);
                            break;
                        default:
                            // steht Krabat nur da
                            mainFrame.krabat.drawKrabat(g);
                            break;
                    }
                }
                // Rumstehen oder Laufen
                else {
                    mainFrame.krabat.drawKrabat(g);
                }
            }
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // Laterne zeichnen, wenn im Bild
        g.drawImage(laterne, laterneAdd, 0);

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
	/*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
	  {
	  g.drawImage (weiden2, 84, 221, null);
	  }*/

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        if (TalkPause > 0 && mainFrame.talkCount == 0) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause == 0 && mainFrame.talkCount == 0) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollX;
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.cursorNone);
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

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();
            // manche Ausreden erfordern neuen Cursor !!!
            evalMouseMoveEvent(mainFrame.mousePoint);
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 10:
                // Krabat laeuft zum Mueller
                mainFrame.pathWalker.SetzeNeuenWeg(Pmueller);
                nextActionID = 20;
                break;

            case 20:
                // richtigrum hinstellen
                mainFrame.krabat.SetFacing(9);
                nextActionID = 30;
                break;

            case 30:
                // Mueller spricht
                PersonSagt("Mlynkmurja_1", 0, 36, 2, 40, muellerTalk);
                break;

            case 40:
                // Krabat spricht
                KrabatSagt("Mlynkmurja_2", 0, 1, 2, 50);
                break;

            case 50:
                // Mueller spricht
                PersonSagt("Mlynkmurja_3", 0, 36, 2, 60, muellerTalk);
                break;

            case 60:
                // Krabat spricht
                KrabatSagt("Mlynkmurja_4", 0, 1, 2, 70);
                break;

            case 70:
                // Mueller spricht
                PersonSagt("Mlynkmurja_5", 0, 36, 2, 80, muellerTalk);
                break;

            case 80:
                // Krabat spricht
                KrabatSagt("Mlynkmurja_6", 0, 1, 2, 90);
                break;

            case 90:
                // Krabat laeuft aus dem Bild
                mainFrame.pathWalker.SetzeNeuenWeg(Pexit);
                nextActionID = 95;
                break;

            case 95:
                // bisschen warten, nachdem K weg
                krabatVisible = false;
                Counter = 15;
                nextActionID = 100;
                break;

            case 100:
                // Mueller spricht
                if (--Counter > 0) {
                    break;
                }
                PersonSagt("Mlynkmurja_7", 0, 66, 2, 110, muellerTalk);
                break;

            case 110:
                // Mueller spricht
                PersonSagt("Mlynkmurja_8", 0, 67, 2, 120, muellerTalk);
                break;

            case 120:
                // Gehe zu Murja, aber diesmal wieder die alte...
                NeuesBild(126, locationID);
                mainFrame.actions[656] = true; // hier verhindern, dass Szene ein zweites Mal passiert
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}