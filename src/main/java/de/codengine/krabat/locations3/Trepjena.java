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
import de.codengine.krabat.anims.GuardTreasure;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Trepjena extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Trepjena.class);
    private GenericImage background;
    private GenericImage trVorn;
    private final GuardTreasure guardTreasure;

    private final GenericPoint talkPoint;
    private final GenericPoint strazaPoint;
    private final BorderRect rectStraza;

    private boolean weistzurueck = false;

    // Konstanten - Rects
    private static final BorderRect ausgangOben
            = new BorderRect(140, 5, 170, 95);
    private static final BorderRect ausgangMitte
            = new BorderRect(145, 177, 172, 280);
    private static final BorderRect ausgangUnten
            = new BorderRect(102, 325, 135, 457);
    private static final BorderRect ausgangHof
            = new BorderRect(460, 337, 526, 463);
    private static final BorderRect rectSchild
            = new BorderRect(73, 347, 98, 392);
    private static final BorderRect rectBildOben
            = new BorderRect(339, 163, 390, 225);
    private static final BorderRect rectBildUnten
            = new BorderRect(333, 346, 385, 410);
    private static final BorderRect durje
            = new BorderRect(105, 344, 118, 457);
    private static final BorderRect wokno1
            = new BorderRect(248, 13, 278, 60);
    private static final BorderRect wokno2
            = new BorderRect(323, 12, 354, 58);
    private static final BorderRect wokno3
            = new BorderRect(385, 12, 414, 58);
    private static final BorderRect wokno4
            = new BorderRect(493, 15, 525, 59);

    // Konstante Points
    private static final GenericPoint pExitOben = new GenericPoint(172, 103);
    private static final GenericPoint pExitMitte = new GenericPoint(235, 288);
    private static final GenericPoint pExitHof = new GenericPoint(507, 466);
    private static final GenericPoint pStraza = new GenericPoint(235, 288);
    private static final GenericPoint pBildOben = new GenericPoint(365, 475);
    private static final GenericPoint pBildUnten = new GenericPoint(360, 465);
    private static final GenericPoint strazaFeet = new GenericPoint(176, 287);
    private static final GenericPoint pDurje = new GenericPoint(110, 457);
    private static final GenericPoint pWokno1 = new GenericPoint(263, 93);
    private static final GenericPoint pWokno2 = new GenericPoint(338, 93);
    private static final GenericPoint pWokno3 = new GenericPoint(401, 92);
    private static final GenericPoint pWokno4 = new GenericPoint(509, 93);
    private static final GenericPoint pSchild = new GenericPoint(107, 467);

    // Konstante ints
    private static final int fBildUnten = 12;
    private static final int fBildOben = 12;
    private static final int fWokna = 12;
    private static final int fKapala = 9;
    private static final int fTafla = 9;
    private static final int fPoklad = 9;
    private static final int fStraza = 9;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Trepjena(Start caller, int oldLocation) {
        super(caller, 131);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 8f;
        mainFrame.krabat.defaultScale = 20;

        guardTreasure = new GuardTreasure(mainFrame);

        strazaPoint = new GenericPoint();
        strazaPoint.x = strazaFeet.x - GuardTreasure.Breite / 2;
        strazaPoint.y = strazaFeet.y - GuardTreasure.Hoehe;

        talkPoint = new GenericPoint();
        talkPoint.x = strazaFeet.x;
        talkPoint.y = strazaPoint.y - 50;

        rectStraza = new BorderRect(strazaPoint.x, strazaPoint.y, strazaPoint.x + GuardTreasure.Breite, strazaPoint.y + GuardTreasure.Hoehe);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(296, 520, 296, 555, 466, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(140, 295, 90, 295, 461, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(215, 220, 215, 220, 414, 460));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(205, 330, 205, 330, 412, 413));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(325, 330, 325, 330, 360, 410));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(515, 520, 325, 330, 290, 359));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(235, 520, 235, 520, 288, 289));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(245, 250, 245, 250, 252, 287));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(240, 325, 240, 325, 250, 251));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(320, 325, 320, 325, 175, 249));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(513, 518, 320, 325, 105, 174));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(175, 518, 175, 518, 103, 104));

        mainFrame.pathFinder.clearMatrix(12);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
        mainFrame.pathFinder.connectPos(4, 5);
        mainFrame.pathFinder.connectPos(5, 6);
        mainFrame.pathFinder.connectPos(6, 7);
        mainFrame.pathFinder.connectPos(7, 8);
        mainFrame.pathFinder.connectPos(8, 9);
        mainFrame.pathFinder.connectPos(9, 10);
        mainFrame.pathFinder.connectPos(10, 11);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 130: // von Hdwor aus
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                mainFrame.krabat.setPos(new GenericPoint(507, 470));
                mainFrame.krabat.setFacing(6);
                break;
            case 140: // von Saal aus
                mainFrame.krabat.setPos(new GenericPoint(176, 103));
                mainFrame.krabat.setFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/trepj/trepj.png");
        trVorn = getPicture("gfx-dd/trepj/trepj-vorn.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            cursorShape = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Straza zeichnen
        g.setClip(strazaPoint.x, strazaPoint.y, GuardTreasure.Breite, GuardTreasure.Hoehe);
        g.drawImage(background, 0, 0);
        guardTreasure.drawStraza(g, talkPerson, strazaPoint, weistzurueck);
        g.drawImage(trVorn, 147, 0);

        mainFrame.pathWalker.doWalk();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.doAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.mousePoint);
            }
        } else {
            if (mainFrame.talkCount > 0 && talkPerson != 0) {
                // beim Reden
                switch (talkPerson) {
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
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Vordergrund zeichnen (nur Clipping - Region wird neugezeichnet)
        if (pKrTemp.y <= 415) {
            g.drawImage(trVorn, 147, 0);
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, COLORS[talkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                talkPerson = 0;
            }
        }

        if (talkPause > 0 && mainFrame.talkCount < 1) {
            talkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
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

                BorderRect tmp = mainFrame.krabat.getBoundingBox();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden Tuer
                if (durje.isPointInRect(pTemp)) {
                    // kluc
                    nextActionID = mainFrame.whatItem == 47 ? 200 : 150;
                    pTemp = pDurje;
                }

                // Ausreden wokno1
                if (wokno1.isPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno1;
                }

                // Ausreden wokno2
                if (wokno2.isPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno2;
                }

                // Ausreden wokno3
                if (wokno3.isPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno3;
                }

                // Ausreden wokno4
                if (wokno4.isPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno4;
                }

                // Ausreden Straza
                if (rectStraza.isPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pStraza;
                }

                // Ausreden Tafla
                if (rectSchild.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 46: // hammer
                        case 42: // Hlebija
                        case 12: // kamuski
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 165;
                            break;
                    }
                    pTemp = pSchild;
                }

                // Ausreden BildOben
                if (rectBildOben.isPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTemp = pBildOben;
                }

                // Ausreden BildUnten
                if (rectBildUnten.isPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = pBildUnten;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Hdwor gehen ?
                if (ausgangHof.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangHof.isPointInRect(kt)) {
                        pTemp = pExitHof;
                    } else {
                        pTemp = new GenericPoint(pExitHof.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Saal (oben) gehen ?
                if (ausgangOben.isPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangOben.isPointInRect(kt)) {
                        pTemp = pExitOben;
                    } else {
                        pTemp = new GenericPoint(pExitOben.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Straza ansehen
                if (rectStraza.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pStraza;
                }

                // zu Poklad (mitte) gehen versuchen
                if (ausgangMitte.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pExitMitte;
                }

                // zu Kapala (unten) gehen ? -> verschlossen
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pDurje;
                }

                // Schild ansehen
                if (rectSchild.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pSchild;
                }

                // BildUnten ansehen
                if (rectBildUnten.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pBildUnten;
                }

                // BildOben ansehen
                if (rectBildOben.isPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTemp = pBildOben;
                }

                // Ansehen wokno1
                if (wokno1.isPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno1;
                }

                // Ansehen wokno2
                if (wokno2.isPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno2;
                }

                // Ansehen wokno3
                if (wokno3.isPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno3;
                }

                // Ansehen wokno4
                if (wokno4.isPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno4;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit Straza reden
                if (rectStraza.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    mainFrame.pathWalker.setNewWay(pStraza);
                    mainFrame.repaint();
                    return;
                }

                // Schild lesen
                if (rectSchild.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.pathWalker.setNewWay(pSchild);
                    mainFrame.repaint();
                    return;
                }

                // BildUnten mitnehmen
                if (rectBildUnten.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pBildUnten);
                    mainFrame.repaint();
                    return;
                }

                // BildOben mitnehmen
                if (rectBildOben.isPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(pBildOben);
                    mainFrame.repaint();
                    return;
                }

                // Tuer mitnehmen
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Wokno1 mitnehmen
                if (wokno1.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(pWokno1);
                    mainFrame.repaint();
                    return;
                }

                // Wokno2 mitnehmen
                if (wokno2.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(pWokno2);
                    mainFrame.repaint();
                    return;
                }

                // Wokno3 mitnehmen
                if (wokno3.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(pWokno3);
                    mainFrame.repaint();
                    return;
                }

                // Wokno4 mitnehmen
                if (wokno4.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(pWokno4);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangHof.isPointInRect(pTemp) ||
                        ausgangUnten.isPointInRect(pTemp) ||
                        ausgangMitte.isPointInRect(pTemp) ||
                        ausgangOben.isPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
            if (cursorShape != 20) {
                cursorShape = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getBoundingBox();
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) || rectStraza.isPointInRect(pTemp) ||
                    durje.isPointInRect(pTemp) || wokno1.isPointInRect(pTemp) ||
                    wokno2.isPointInRect(pTemp) || wokno3.isPointInRect(pTemp) ||
                    wokno4.isPointInRect(pTemp) || rectSchild.isPointInRect(pTemp) ||
                    rectBildUnten.isPointInRect(pTemp) || rectBildOben.isPointInRect(pTemp);

            if (cursorShape != 10 && !mainFrame.isInventoryHighlightCursor) {
                cursorShape = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (cursorShape != 11 && mainFrame.isInventoryHighlightCursor) {
                cursorShape = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (rectStraza.isPointInRect(pTemp) ||
                    durje.isPointInRect(pTemp) || wokno1.isPointInRect(pTemp) ||
                    wokno2.isPointInRect(pTemp) || wokno3.isPointInRect(pTemp) ||
                    wokno4.isPointInRect(pTemp) || rectSchild.isPointInRect(pTemp) ||
                    rectBildUnten.isPointInRect(pTemp) || rectBildOben.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (ausgangHof.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (ausgangOben.isPointInRect(pTemp)) {
                if (cursorShape != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    cursorShape = 9;
                }
                return;
            }

            // sonst normal-Cursor
            if (cursorShape != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                cursorShape = 0;
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
            keyClear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            keyClear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            keyClear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void keyClear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.stopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {
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
            switchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Straza anschauen
                krabatSays("Trepjena_1", fStraza, 3, 0, 0);
                break;

            case 2:
                // Mit Wache reden
                mainFrame.krabat.setFacing(fStraza);
                mainFrame.isAnimRunning = true;
                nextActionID = 300;
                mainFrame.repaint();
                break;

            case 3:
                // An Wache vorbei zur Tuer gehen (versuchen)
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.setFacing(fPoklad);
                nextActionID = 301;
                mainFrame.repaint();
                break;

            case 4:
                // Schild ansehen
                krabatSays("Trepjena_2", fTafla, 3, 0, 0);
                break;

            case 5:
                // Schild lesen
                krabatSays("Trepjena_3", fTafla, 3, 0, 0);
                break;

            case 6:
                // Zur Kapelle (unten) gehen -> verschlossen
                krabatSays("Trepjena_4", fKapala, 3, 0, 0);
                break;

            case 7:
                // BildUnten ansehen
                krabatSays("Trepjena_5", fBildUnten, 3, 0, 0);
                break;

            case 8:
                // BildOben ansehen
                krabatSays("Trepjena_6", fBildOben, 3, 0, 0);
                break;

            case 20:
                // wokna ansehen
                krabatSays("Trepjena_7", fWokna, 3, 0, 0);
                break;

            case 50:
                // BildUnten mitnehmen
                krabatSays("Trepjena_8", fBildUnten, 3, 0, 0);
                break;

            case 55:
                // BildOben mitnehmen
                krabatSays("Trepjena_9", fBildOben, 3, 0, 0);
                break;

            case 60:
                // Tuer mitnehmen
                krabatSays("Trepjena_10", fKapala, 3, 0, 0);
                break;

            case 65:
                // Fenster mitnehmen
                krabatSays("Trepjena_11", fWokna, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Hdwor
                createNewLocation(130, locationID);
                break;

            case 101:
                // Gehe zu Poklad
                createNewLocation(132, locationID);
                break;

            case 102:
                // Gehe zu Kapala
                createNewLocation(133, locationID);
                break;

            case 103:
                // Gehe zu Saal
                createNewLocation(140, locationID);
                break;

            case 150:
                // durje-ausreden
                thingExcuse(fKapala);
                break;

            case 155:
                // wokno-ausreden
                thingExcuse(fWokna);
                break;

            case 160:
                // straza-ausreden
                maleExcuse(fStraza);
                break;

            case 165:
                // tafla-ausreden
                thingExcuse(fTafla);
                break;

            case 170:
                // wobrazoben-ausreden
                thingExcuse(fBildOben);
                break;

            case 175:
                // wobrazunten-ausreden
                thingExcuse(fBildUnten);
                break;

            case 200:
                // kluc auf durje
                krabatSays("Trepjena_12", fKapala, 3, 0, 0);
                break;

            case 210:
                // schwere ggst auf tafla.
                krabatSays("Trepjena_13", fTafla, 3, 0, 0);
                break;

            // Versuch, mit Stra#za zu reden
            case 300:
                // zufaellige Antwort -> Zahl von 0 bis 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        personSays("Trepjena_14", fStraza, 46, 2, 800, talkPoint);
                        break;

                    case 1:
                        personSays("Trepjena_15", fStraza, 46, 2, 800, talkPoint);
                        break;
                }
                break;

            // Versuch, an Wache vorbeizugehen
            case 301:
                // zufaellige Antwort -> Zahl von 0 bis 1 generieren
                int zuffZahl2 = (int) (Math.random() * 1.9);
                weistzurueck = true;
                switch (zuffZahl2) {
                    case 0:
                        personSays("Trepjena_16", fStraza, 46, 2, 800, talkPoint);
                        break;

                    case 1:
                        personSays("Trepjena_17", fStraza, 46, 2, 800, talkPoint);
                        break;
                }
                break;

            case 800:
                // Dialog beenden, wenn zuende gelabert...
                weistzurueck = false;
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}