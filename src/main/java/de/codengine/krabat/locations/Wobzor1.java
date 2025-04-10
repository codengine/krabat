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

package de.codengine.krabat.locations;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Wobzor1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Wobzor1.class);
    private GenericImage background;
    private GenericImage horiz3;
    private GenericImage horiz4;
    private final GenericImage[] Wasser;
    private boolean switchanim = false;
    private int wassercount = 1;
    private boolean forward = true;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang = new BorderRect(594, 150, 639, 231);
    private static final BorderRect linkerAusgang = new BorderRect(0, 164, 140, 250);
    private static final BorderRect horiz3Rect = new BorderRect(179, 175, 270, 248);
    private static final BorderRect horiz4Rect = new BorderRect(528, 177, 639, 219);
    private static final BorderRect wodaRect = new BorderRect(329, 373, 639, 479);

    // Konstanten - Points
    private static final GenericPoint Pleft = new GenericPoint(146, 236);
    private static final GenericPoint Pright = new GenericPoint(577, 204);
    private static final GenericPoint Pwoda = new GenericPoint(449, 221);

    // Y - Positions - Array fuer Laufen
    private static final int[] Carray = {235, 235, 235, 235, 235, 235, 235, 235, 234, 234,
            234, 234, 234, 234, 234, 234, 234, 234, 233, 233,
            233, 233, 233, 233, 233, 233, 233, 233, 233, 233,
            233, 233, 233, 233, 233, 233, 233, 233, 233, 233,
            233, 233, 233, 233, 233, 233, 233, 233, 233, 233,
            233, 233, 233, 232, 232, 232, 232, 232, 232, 232,
            232, 232, 232, 232, 232, 232, 232, 231, 231, 231,
            231, 231, 231, 231, 231, 231, 231, 231, 231, 231,
            231, 231, 231, 231, 231, 231, 231, 231, 231, 230,
            230, 230, 230, 230, 230, 230, 230, 230, 230, 230,

            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 229, 229, 229,
            229, 229, 229, 229, 229, 229, 229, 230, 230, 230,
            230, 230, 230, 230, 230, 230, 230, 230, 230, 229,

            229, 229, 229, 229, 229, 229, 228, 228, 228, 228,
            228, 228, 228, 228, 228, 228, 228, 228, 227, 227,
            227, 227, 226, 226, 226, 226, 226, 226, 226, 225,
            225, 225, 225, 225, 225, 224, 224, 224, 224, 224,
            224, 224, 224, 224, 224, 223, 223, 223, 223, 223,
            223, 223, 223, 223, 223, 223, 223, 222, 222, 222,
            222, 222, 222, 222, 222, 222, 222, 222, 222, 223,
            223, 223, 223, 223, 223, 223, 223, 224, 224, 224,
            224, 224, 223, 223, 223, 223, 223, 223, 223, 223,
            223, 223, 223, 223, 223, 223, 223, 222, 222, 222,

            221, 221, 221, 221, 221, 221, 221, 221, 221, 221,
            221, 221, 221, 221, 221, 221, 221, 221, 221, 221,
            221, 221, 221, 220, 220, 220, 220, 220, 220, 220,
            220, 220, 220, 220, 220, 220, 220, 220, 220, 220,
            220, 220, 220, 220, 220, 220, 220, 220, 220, 220,
            220, 220, 220, 220, 220, 220, 220, 220, 220, 220,
            220, 220, 220, 220, 220, 220, 220, 220, 220, 220,
            220, 220, 220, 220, 220, 220, 220, 220, 220, 220,
            219, 219, 219, 219, 218, 218, 218, 218, 218, 218,
            218, 217, 217, 217, 217, 217, 217, 217, 217, 217,

            217, 216, 216, 216, 216, 216, 216, 216, 216, 216,
            215, 214, 214, 213, 213, 213, 213, 213, 213, 213,
            213, 213, 212, 212, 212, 212, 212, 212, 212, 212,
            212, 212, 212, 212, 212, 212, 212, 212, 211, 211,
            211, 211, 211, 211, 211, 210, 210, 209, 209, 209,
            209, 209, 209, 209, 209, 208, 208, 208, 208, 208,
            208, 208, 208, 208, 208, 208, 208, 208, 208, 208,
            208, 208, 208, 207, 207, 207, 207, 207, 207, 207,
            206, 206, 206, 206, 206, 206, 206, 206, 206, 206,
            206, 206, 206, 206, 206, 206, 206, 206, 206, 206,

            206, 206, 206, 206, 206, 206, 206, 205, 205, 205,
            205, 205, 205, 205, 205, 205, 205, 205, 205, 205,
            205, 205, 205, 205, 205, 205, 205, 205, 205, 205,
            205, 205, 205, 205, 205, 205, 205, 205, 205, 205,
            205, 205, 205, 205, 205};

    // Konstante ints
    private static final int fWoda = 6;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wobzor1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 4f;
        mainFrame.krabat.defScale = 80;

        Wasser = new GenericImage[7];

        InitLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(145, 192, 577, 260));

        mainFrame.pathFinder.ClearMatrix(1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 13:
                // von Wjes aus
                mainFrame.krabat.setPos(CorrectY(new GenericPoint(160, 200)));
                mainFrame.krabat.SetFacing(3);
                break;
            case 8:
                // von Rapak aus
                mainFrame.krabat.setPos(CorrectY(new GenericPoint(570, 211)));
                mainFrame.krabat.SetFacing(9);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/horiz/horiz2.png");
        horiz3 = getPicture("gfx/horiz/horiz3.png");
        horiz4 = getPicture("gfx/horiz/horiz4.png");

        Wasser[1] = getPicture("gfx/horiz/hww5.png");
        Wasser[2] = getPicture("gfx/horiz/hww3.png");
        Wasser[3] = getPicture("gfx/horiz/hww2.png");
        Wasser[4] = getPicture("gfx/horiz/hw3.png");
        Wasser[5] = getPicture("gfx/horiz/hw5.png");
        Wasser[6] = getPicture("gfx/horiz/hw6.png");

    }

    @Override
    public void cleanup() {
        background = null;
        horiz3 = null;
        horiz4 = null;

        Wasser[1] = null;
        Wasser[2] = null;
        Wasser[3] = null;
        Wasser[4] = null;
        Wasser[5] = null;
        Wasser[6] = null;
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

        // Wellenimages schunkeln...
        if (mainFrame.isBackgroundAnimRunning) {
            switchanim = !switchanim;
            if (switchanim) {
                if (forward) {
                    wassercount++;
                    if (wassercount == 7) {
                        wassercount = 5;
                        forward = false;
                    }
                } else {
                    wassercount--;
                    if (wassercount == 0) {
                        wassercount = 2;
                        forward = true;
                    }
                }
            }
            g.setClip(303, 356, 338, 125);
            g.drawImage(Wasser[wassercount], 303, 356);
        }

        // hier ist der Sound...
        evalSound();

        mainFrame.pathWalker.GeheWeg();

        mainFrame.krabat.setPos(CorrectY(mainFrame.krabat.getPos()));

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
                        // Krabat steht nur da
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
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (horiz3Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(horiz3, 197, 186);
        }

        // hinterm horiz4 (nur Clipping - Region wird neugezeichnet)
        if (horiz4Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(horiz4, 543, 186);
        }

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

                // Ausreden fuer Woda
                if (wodaRect.IsPointInRect(pTemp)) {
                    // wuda + wacka
                    nextActionID = mainFrame.whatItem == 10 ? 155 : 150;
                    pTemp = CorrectY(Pwoda);
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                SetzeNeuenWeg(pTemp);
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

                // zu Wjes gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = CorrectY(Pleft);
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Rapak gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = CorrectY(Pright);
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Wacki ansehen
                if (wodaRect.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = CorrectY(Pwoda);
                }

                SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wjes anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Rapak anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Woda mitnehmen ?
                if (wodaRect.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    mainFrame.pathWalker.SetzeNeuenWeg(CorrectY(Pwoda));
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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || wodaRect.IsPointInRect(pTemp);

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
            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 2;
                }
                return;
            } else if (wodaRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
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

    // Routinen fuer veraendertes Laufen auf definierter Linie

    private void SetzeNeuenWeg(GenericPoint dest) {
        GenericPoint right = CorrectY(dest);
        mainFrame.pathWalker.SetzeNeuenWeg(right);
    }

    private GenericPoint CorrectY(GenericPoint dst) {
        if (dst.x > 620) {
            return new GenericPoint(dst.x, 205);
        }
        if (dst.x < 75) {
            return new GenericPoint(dst.x, 235);
        } else {
            return new GenericPoint(dst.x, Carray[dst.x - 75] + 1);
        }
    }

    private void evalSound() {
        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 4.99);
            zwzfz += 49;

            mainFrame.soundPlayer.PlayFile("sfx/recka" + (char) zwzfz + ".wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
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
                // Wasser anschauen
                KrabatSagt("Wobzor1_1", fWoda, 3, 0, 0);
                break;

            case 100:
                // gehe zu Rapak
                NeuesBild(8, 9);
                break;

            case 101:
                // nach Wjes gehen
                NeuesBild(13, 9);
                break;

            case 150:
                // Ausreden fuer Wasser Standard
                KrabatSagt("Wobzor1_2", fWoda, 3, 0, 0);
                break;

            case 155:
                // Wasser Extraausrede (mit Angel + Wurm)
                KrabatSagt("Wobzor1_3", fWoda, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}