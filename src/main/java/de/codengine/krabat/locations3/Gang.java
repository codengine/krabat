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

public class Gang extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Gang.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage stamm;
    private GenericImage buch;
    private GenericImage /* kette1 , */ kette2;
    private GenericImage tuer;
    private GenericImage tuervorder;
    private boolean setScroll = false;
    private int scrollwert;
    private boolean ziehtKrabatAnKette = false;

    // Konstanten - Rects
    private static final BorderRect ausgangZachod
            = new BorderRect(71, 93, 139, 127);
    private static final BorderRect ausgangKapala
            = new BorderRect(1103, 60, 1183, 98);
    private static final BorderRect tuerUnten
            = new BorderRect(1104, 253, 1182, 355);
    private static final BorderRect ring
            = new BorderRect(391, 283, 417, 337);
    private static final BorderRect knochen
            = new BorderRect(356, 351, 388, 369);
    private static final BorderRect rectStamm
            = new BorderRect(940, 190, 1060, 275);
    private static final BorderRect buchRect
            = new BorderRect(343, 331, 487, 344);
    private static final BorderRect rectVorderTuer
            = new BorderRect(1164, 223, 1255, 374);

    // Konstante Points
    private static final GenericPoint pExitZachod = new GenericPoint(85, 326);
    private static final GenericPoint pExitKapala = new GenericPoint(1134, 197);
    private static final GenericPoint pKnochen = new GenericPoint(420, 354);
    private static final GenericPoint pTuerUnten = new GenericPoint(1143, 370);
    private static final GenericPoint pRing = new GenericPoint(441, 350);
    private static final GenericPoint pRingZieh = new GenericPoint(441, 350);
    private static final GenericPoint pBuch = new GenericPoint(441, 350);
    private static final GenericPoint pTuerRein = new GenericPoint(1187, 348);
    private static final GenericPoint pTuerDrin = new GenericPoint(1212, 348);

    // Konstante ints
    private static final int fRing = 9;
    private static final int fKnochen = 9;
    private static final int fTuer = 12;
    private static final int fBuch = 9;
    private static final int fRingZieh = 9;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Gang(Start caller, int oldLocation) {
        super(caller, 152);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(20, true);

        mainFrame.krabat.maxX = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomFactor = 1f;
        mainFrame.krabat.defaultScale = 0;

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(40, 120, 67, 120, 326, 358));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(121, 430, 121, 430, 348, 358));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(431, 470, 431, 470, 338, 370));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(471, 790, 471, 790, 360, 365));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(791, 885, 791, 885, 363, 370));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(886, 1185, 886, 1170, 368, 380));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(538, 543, 465, 470, 324, 337));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(670, 675, 538, 543, 308, 323));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(728, 733, 670, 675, 298, 307));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(880, 885, 728, 733, 268, 297));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(1031, 1036, 880, 885, 225, 267));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(1178, 1183, 1031, 1036, 188, 224));

        mainFrame.pathFinder.clearMatrix(12);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
        mainFrame.pathFinder.connectPos(4, 5);
        mainFrame.pathFinder.connectPos(2, 6);
        mainFrame.pathFinder.connectPos(6, 7);
        mainFrame.pathFinder.connectPos(7, 8);
        mainFrame.pathFinder.connectPos(8, 9);
        mainFrame.pathFinder.connectPos(9, 10);
        mainFrame.pathFinder.connectPos(10, 11);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 151: // von Spaniska aus
                mainFrame.krabat.setPos(new GenericPoint(85, 330));
                mainFrame.krabat.setFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;
            case 153: // von Komedij aus
                mainFrame.krabat.setPos(new GenericPoint(1134, 197));
                mainFrame.krabat.setFacing(6);
                scrollwert = 640;
                setScroll = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        backl = getPicture("gfx-dd/gang/gang-l.png");
        backr = getPicture("gfx-dd/gang/gang-r.png");
        stamm = getPicture("gfx-dd/gang/stamm.png");
        buch = getPicture("gfx-dd/gang/gkniha.png");

        kette2 = getPicture("gfx-dd/gang/rjecaz.png");

        tuer = getPicture("gfx-dd/gang/gdurje.png");
        tuervorder = getPicture("gfx-dd/gang/gfground.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }
            cursorShape = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Buch zeichnen, wenn es auf der Kette liegt
        if (mainFrame.actions[690]) {
            g.setClip(390, 280, 38, 76);
            // vorher die untere Kette zeichnen
            g.drawImage(kette2, 390, 280);
            g.drawImage(buch, 390, 280);

            // wenn Tuer offen, dann diese offen zeichnen, und das muss sie ja sein ;-)
            g.setClip(1103, 246, 85, 109);
            g.drawImage(tuer, 1103, 246);
        }

        // hier die Kette unten zeichnen, wenn K dran zieht
        if (ziehtKrabatAnKette) {
            g.setClip(390, 280, 38, 76);
            g.drawImage(kette2, 390, 280);

            --Counter;
            if (Counter < 1) {
                ziehtKrabatAnKette = false;
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

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

        // hinter baumstamm (nur Clipping - Region wird neugezeichnet)
        if (rectStamm.isPointInRect(pKrTemp)) {
            g.drawImage(stamm, 977, 67);
        }

        // hier dureberzeichnen, wenn er den Speer holen soll
        if (mainFrame.actions[690] && rectVorderTuer.isPointInRect(pKrTemp)) {
            g.drawImage(tuervorder, 1175, 249);
        }


        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);  // Sobe hat 964 als y-wert ??????????
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
            // unser ach so beliebter Ueberschneidungsbug...
            GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

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

                // Ausreden fuer kette
                if (ring.isPointInRect(pTemp)) {
                    // schweres Buch
                    nextActionID = mainFrame.whatItem == 55 ? 230 : 150;
                    pTxxx = pRing;
                }

                // Ausreden fuer Knochen
                if (knochen.isPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTxxx = pKnochen;
                }

                // Ausreden fuer Tuer
                if (tuerUnten.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 42: // Hlebija
                            nextActionID = 200;
                            break;
                        case 47: // Kluc
                            nextActionID = 210;
                            break;
                        case 46: // Hammer
                            nextActionID = 220;
                            break;
                        default:
                            nextActionID = 160;
                            break;
                    }
                    pTxxx = pTuerUnten;
                }

                // Ausreden fuer Buch
                if (buchRect.isPointInRect(pTemp) && mainFrame.actions[690]) {
                    nextActionID = 165;
                    pTxxx = pBuch;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.setNewWay(pTxxx);
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

                // und nochmal Ueberschneidungsbug
                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Zachod gehen ?
                if (ausgangZachod.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangZachod.isPointInRect(kt)) {
                        pTxxx = pExitZachod;
                    } else {
                        pTxxx = new GenericPoint(pExitZachod.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Kapala gehen ?
                if (ausgangKapala.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKapala.isPointInRect(kt)) {
                        pTxxx = pExitKapala;
                    } else {
                        pTxxx = new GenericPoint(pExitKapala.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Ring ansehen
                if (ring.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTxxx = pRing;
                }

                // Knochen ansehen
                if (knochen.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTxxx = pKnochen;
                }

                // Tuer ansehen
                if (tuerUnten.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTxxx = pTuerUnten;
                }

                // Buch ansehen
                if (buchRect.isPointInRect(pTemp) && mainFrame.actions[690]) {
                    nextActionID = 7;
                    pTxxx = pBuch;
                }

                mainFrame.pathWalker.setNewWay(pTxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Buch mitnehmen
                if (buchRect.isPointInRect(pTemp) && mainFrame.actions[690]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pBuch);
                    mainFrame.repaint();
                    return;
                }

                // Ring benutzen
                if (ring.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    mainFrame.pathWalker.setNewWay(pRingZieh);
                    mainFrame.repaint();
                    return;
                }

                // knochen benutzen
                if (knochen.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.pathWalker.setNewWay(pKnochen);
                    mainFrame.repaint();
                    return;
                }

                // Tuer benutzen
                if (tuerUnten.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    mainFrame.pathWalker.setNewWay(pTuerUnten);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangZachod.isPointInRect(pTemp) || ausgangKapala.isPointInRect(pTemp)) {
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
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // neuen Punkt erzeugen wg. Scrolling
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollX, pTxxx.y + mainFrame.scrollY);

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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) || ring.isPointInRect(pTemp) ||
                    knochen.isPointInRect(pTemp) || tuerUnten.isPointInRect(pTemp) ||
                    buchRect.isPointInRect(pTemp) && mainFrame.actions[690];

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
            if (tuerUnten.isPointInRect(pTemp) ||
                    ring.isPointInRect(pTemp) ||
                    knochen.isPointInRect(pTemp) ||
                    buchRect.isPointInRect(pTemp) && mainFrame.actions[690]) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (ausgangZachod.isPointInRect(pTemp) ||
                    ausgangKapala.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
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
                // Ring ansehen
                krabatSays("Gang_1", fRing, 3, 0, 0);
                break;

            case 2:
                // Knochen ansehen
                krabatSays("Gang_2", fKnochen, 3, 0, 0);
                break;

            case 3:
                // Tuer ansehen
                if (!mainFrame.actions[690]) {
                    // Tuer ist zu
                    krabatSays("Gang_3", fTuer, 3, 0, 0);
                } else {
                    // Tuer ist auf...
                    mainFrame.krabat.setFacing(fTuer);
                    if (!mainFrame.actions[954]) {
                        // wenn noch kein Speer, dann diesen finden
                        // Inventar hinzufuegen
                        mainFrame.inventory.vInventory.addElement(42);
                        mainFrame.actions[954] = true;        // Flag setzen
                        mainFrame.isAnimRunning = true;
                        evalMouseMoveEvent(mainFrame.mousePoint);
                        nextActionID = 20;
                    } else {
                        // habe schon Speer
                        krabatSays("Gang_4", 0, 3, 0, 0);
                    }
                }
                break;

            case 4:
                // Ring benutzen
                if (!mainFrame.actions[690]) // Buch liegt nicht drauf
                {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    mainFrame.krabat.setFacing(fRingZieh);
                    mainFrame.krabat.nAnimation = 94;
                    ziehtKrabatAnKette = true;
                    Counter = 40;
                    nextActionID = 60;
                    mainFrame.soundPlayer.playFile("sfx-dd/gdurjeauf.wav");
                } else {  // Buch liegt drauf
                    krabatSays("Gang_5", fRingZieh, 3, 0, 0);
                }
                break;

            case 5:
                // Knochen benutzen
                krabatSays("Gang_6", fKnochen, 3, 0, 0);
                break;

            case 6:
                // Tuer benutzen
                if (!mainFrame.actions[690]) {
                    // Tuer ist zu
                    krabatSays("Gang_7", fTuer, 3, 0, 0);
                } else {
                    // Tuer ist auf...
                    mainFrame.krabat.setFacing(fTuer);
                    if (!mainFrame.actions[954]) {
                        // wenn noch kein Speer, dann diesen finden
                        // Inventar hinzufuegen
                        mainFrame.isAnimRunning = true;
                        evalMouseMoveEvent(mainFrame.mousePoint);
                        mainFrame.inventory.vInventory.addElement(42);
                        mainFrame.actions[954] = true;        // Flag setzen
                        nextActionID = 20;
                    } else {
                        // habe schon Speer
                        krabatSays("Gang_8", 0, 3, 0, 0);
                    }
                }
                break;

            case 7:
                // Buch anschauen
                krabatSays("Gang_9", fBuch, 3, 0, 0);
                break;

            case 20:
                // vorgehen (noch sichtbar)
                mainFrame.pathWalker.setNewWayGuaranteed(pTuerRein);
                nextActionID = 23;
                break;

            case 23:
                // Tueranim durchfuehren
                krabatSays("Gang_10", 0, 3, 2, 25);
                break;

            case 25:
                // jetzt aus dem Bild verschwinden
                mainFrame.pathWalker.setNewWayGuaranteed(pTuerDrin);
                nextActionID = 27;
                break;

            case 27:
                // Tueranim
                krabatSays("Gang_11", 0, 3, 2, 30);
                break;

            case 30:
                // Tueranim
                krabatSays("Gang_12", 0, 3, 2, 31);
                break;

            case 31:
                // zuruecklaufen
                mainFrame.pathWalker.setNewWayGuaranteed(pTuerRein);
                nextActionID = 32;
                break;

            case 32:
                // und wieder ins Rect
                mainFrame.pathWalker.setNewWay(pTuerUnten);
                nextActionID = 35;
                break;

            case 35:
                // Ende Tueranim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 50:
                // Buch mitnehmen
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.setFacing(fBuch);
                mainFrame.krabat.nAnimation = 94;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 53;
                Counter = 5;
                break;

            case 53:
                // Ende Buch mitnehmen
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(55);
                    mainFrame.actions[690] = false; // Kein Buch mehr da zum aufheben
                    mainFrame.isClipSet = false;
                    mainFrame.soundPlayer.playFile("sfx-dd/gdurjezu.wav");
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 60:
                // auf Ende Buecken warten, dann Spruch sagen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                krabatSays("Gang_13", fRingZieh, 3, 2, 65);
                break;

            case 65:
                // Ende reden und Ende Kette abwarten
                if (Counter > 1) {
                    break;
                }
                ziehtKrabatAnKette = false;
                mainFrame.isClipSet = false;
                mainFrame.soundPlayer.playFile("sfx-dd/gdurjezu.wav");
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Zachod
                createNewLocation(151, locationID);
                break;

            case 101:
                // Gehe zu Kapala
                createNewLocation(153, locationID);
                break;

            case 150:
                // Ausreden fuer kette
                thingExcuse(fRing);
                break;

            case 155:
                // Ausreden fuer knochen
                thingExcuse(fKnochen);
                break;

            case 160:
                // Ausreden fuer tuer
                thingExcuse(fTuer);
                break;

            case 165:
                // Ausreden fuer buch
                thingExcuse(fBuch);
                break;

            case 200:
                // hlebija auf tuer
                // Zufallszahl 0 bis 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Gang_14", fTuer, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Gang_15", fTuer, 3, 0, 0);
                        break;
                }
                break;

            case 210:
                // kluc auf tuer
                krabatSays("Gang_16", fTuer, 3, 0, 0);
                break;

            case 220:
                // hammer auf tuer
                krabatSays("Gang_17", fTuer, 3, 0, 0);
                break;

            case 230:
                // Buch hinlegen
                mainFrame.krabat.setFacing(fBuch);
                mainFrame.krabat.nAnimation = 94;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 0;
                // Buch raus aus Inventar
                mainFrame.inventory.vInventory.removeElement(55);
                mainFrame.actions[690] = true; // Buch da zum aufheben
                mainFrame.soundPlayer.playFile("sfx-dd/gdurjeauf.wav");
                mainFrame.isClipSet = false;
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}