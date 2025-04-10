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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Saal extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Saal.class);
    private GenericImage background;

    // Konstanten - Rects
    private static final BorderRect linkerAusgang
            = new BorderRect(26, 326, 83, 433);
    private static final BorderRect rechterAusgang
            = new BorderRect(533, 332, 568, 438);
    private static final BorderRect bildLinks
            = new BorderRect(65, 175, 134, 281);
    private static final BorderRect bildRechts
            = new BorderRect(470, 182, 553, 288);
    private static final BorderRect saalEnde
            = new BorderRect(260, 340, 353, 388);

    // Konstante Points
    private static final GenericPoint pExitLeft = new GenericPoint(88, 458);
    private static final GenericPoint pExitRight = new GenericPoint(520, 449);
    private static final GenericPoint pBildLinks = new GenericPoint(180, 442);
    private static final GenericPoint pBildRechts = new GenericPoint(450, 442);

    // Konstante ints
    private static final int fBildLinks = 9;
    private static final int fBildRechts = 3;
    private static final int fSaalVorn = 6;
    private static final int fSaalHinten = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Saal(Start caller, int oldLocation) {
        super(caller, 140);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 452;
        mainFrame.krabat.zoomf = 1.04f;
        mainFrame.krabat.defScale = 0;

        InitLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(238, 375, 180, 440, 395, 418));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(180, 440, 30, 590, 419, 479));

        mainFrame.pathFinder.ClearMatrix(2);

        mainFrame.pathFinder.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 131: // von Trjepjena
                mainFrame.krabat.setPos(new GenericPoint(520, 449));
                mainFrame.krabat.SetFacing(9);
                break;
            case 141: // von Dingl aus
                mainFrame.krabat.setPos(new GenericPoint(88, 458));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/saal/saal.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        mainFrame.pathWalker.GeheWeg();

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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

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

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.isAnimRunning) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // linker Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden linkes bild
                if (bildLinks.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pBildLinks;
                }

                // Ausreden rechtes bild
                if (bildRechts.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pBildRechts;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Trjepjena gehen ?
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = pExitRight;
                    } else {
                        pTemp = new GenericPoint(pExitRight.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Dingl gehen ?
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = pExitLeft;
                    } else {
                        pTemp = new GenericPoint(pExitLeft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // BildLinks ansehen
                if (bildLinks.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pBildLinks;
                }

                // BildRechts ansehen
                if (bildRechts.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pBildRechts;
                }

                // Saalende ansehen
                if (saalEnde.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (linkerAusgang.IsPointInRect(pTemp) ||
                        rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // BildLinks mitnehmen
                if (bildLinks.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(pBildLinks);
                    mainFrame.repaint();
                    return;
                }

                // BildRechts ansehen
                if (bildRechts.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(pBildRechts);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || bildLinks.IsPointInRect(pTemp) ||
                    bildRechts.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.isInventoryHighlightCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (Cursorform != 11 && mainFrame.isInventoryHighlightCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (bildLinks.IsPointInRect(pTemp) ||
                    bildRechts.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                Cursorform = 0;
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.isInventoryCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.isAnimRunning) {
            return;
        }

        // Bei Krabat - Animation keine Keys
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();

        // Hauptmenue aktivieren
        if (Taste == GenericKeyEvent.VK_F1) {
            Keyclear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            Keyclear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            Keyclear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void Keyclear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.StopWalking();
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

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // BildLinks anschauen
                KrabatSagt("Saal_1", fBildLinks, 3, 0, 0);
                break;

            case 2:
                // BildRechts anschauen
                KrabatSagt("Saal_2", fBildRechts, 3, 0, 0);
                break;

            case 3:
                // endlosen Saal anschauen
                // zufaellige Antwort -> Zahl von 0 bis 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Saal_3", fSaalHinten, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Saal_4", fSaalVorn, 3, 0, 0);
                        break;
                }
                break;

            case 50:
                // BildLinks mitnehmen
                KrabatSagt("Saal_5", fBildLinks, 3, 0, 0);
                break;

            case 55:
                // BildRechts mitnehmen
                KrabatSagt("Saal_6", fBildRechts, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Trjepjena
                NeuesBild(131, locationID);
                break;

            case 101:
                // Gehe zu Dingl
                NeuesBild(141, locationID);
                break;

            case 150:
                // BildLinks-Ausreden
                DingAusrede(fBildLinks);
                break;

            case 155:
                // BildRechts-Ausreden
                DingAusrede(fBildRechts);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}