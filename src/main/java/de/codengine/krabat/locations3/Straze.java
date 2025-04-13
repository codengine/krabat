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
import de.codengine.krabat.anims.Guard1;
import de.codengine.krabat.anims.Guard2;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Straze extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Straze.class);
    private GenericImage background;
    private final Guard1 guard1;
    private final Guard2 guard2;

    private final BorderRect reStraza1;
    private final BorderRect reStraza2;

    private final GenericPoint talkPoint;  // links
    private final GenericPoint talkPoint2; // rechts

    private final GenericPoint pStraza1; // links
    private final GenericPoint pStraza2; // rechts

    private boolean straza1VersperrtWeg = false;
    private boolean straza2VersperrtWeg = false;

    // Konstanten - Rects
    private static final BorderRect ausgangHdwor
            = new BorderRect(233, 330, 340, 405);
    private static final BorderRect ausgangTerassa
            = new BorderRect(150, 450, 420, 479);
    private static final BorderRect wappen
            = new BorderRect(250, 78, 331, 172);

    // Konstante Points
    private static final GenericPoint pExitHdwor = new GenericPoint(285, 410);
    private static final GenericPoint pExitTerassa = new GenericPoint(285, 479);
    private static final GenericPoint pStrazy = new GenericPoint(290, 475);
    private static final GenericPoint pWappen = new GenericPoint(293, 475);

    private static final GenericPoint straza1Feet = new GenericPoint(196, 467);
    private static final GenericPoint straza2Feet = new GenericPoint(370, 467);

    // Konstante ints
    private static final int fStraze = 12;
    private static final int fWapon = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Straze(Start caller, int oldLocation) {
        super(caller, 128);
        mainFrame.freeze(true);

        // Schmied raushauen, wenn Hammer genommen
        if (mainFrame.actions[953]) {
            mainFrame.actions[701] = true;
        }

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxX = 470;
        mainFrame.krabat.zoomFactor = 1f;
        mainFrame.krabat.defaultScale = -30;

        guard1 = new Guard1(mainFrame);
        guard2 = new Guard2(mainFrame);

        // Inits fuer Straza1
        pStraza1 = new GenericPoint(straza1Feet.x - Guard1.Breite / 2, straza1Feet.y - Guard1.Hoehe);

        talkPoint = new GenericPoint(straza1Feet.x, pStraza1.y - 50);

        reStraza1 = new BorderRect(pStraza1.x, pStraza1.y, pStraza1.x + Guard1.Breite, pStraza1.y + Guard1.Hoehe);

        // Inits fuer Straza2
        pStraza2 = new GenericPoint(straza2Feet.x - Guard2.Breite / 2, straza2Feet.y - Guard2.Hoehe);

        talkPoint2 = new GenericPoint(straza2Feet.x, pStraza2.y - 50);

        reStraza2 = new BorderRect(pStraza2.x, pStraza2.y, pStraza2.x + Guard2.Breite, pStraza2.y + Guard2.Hoehe);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();

        // diese Rechtecke nur mit Dienstkleidung passierbar
        if (mainFrame.actions[511]) {
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(160, 410, 80, 430, 470, 479));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(242, 420, 309, 469));

            mainFrame.pathFinder.clearMatrix(2);
            mainFrame.pathFinder.connectPos(0, 1);
        } else {
            // sonst nur dieses
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(160, 410, 80, 430, 470, 479));

            mainFrame.pathFinder.clearMatrix(1);
        }

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 127:
                // von Terassa aus
                mainFrame.krabat.setPos(new GenericPoint(285, 475));
                mainFrame.krabat.setFacing(12);
                break;
            case 130:
                // von Hdwor aus
                mainFrame.krabat.setPos(new GenericPoint(279, 445));
                mainFrame.krabat.setFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/straze/straze.png");

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

        // Wache 1 + 2 zeichnen
        g.setClip(pStraza1.x, pStraza1.y, Guard1.Breite, Guard1.Hoehe);
        g.drawImage(background, 0, 0);
        guard1.drawStraza1(g, talkPerson, pStraza1, straza1VersperrtWeg);

        // hier Wache2 (rechts)
        g.setClip(pStraza2.x, pStraza2.y, Guard2.Breite, Guard2.Hoehe);
        g.drawImage(background, 0, 0);
        guard2.drawStraza2(g, talkPerson, pStraza2, straza2VersperrtWeg);

        // Wache 2 entscheidet selber, wann "Stop" zurueckgenommen
        straza2VersperrtWeg = false;

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.doWalk();

        // Krabat zeichnen

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
            talkPerson = 0;
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

                // Ausreden fuer Wache 1 und 2
                if (reStraza1.isPointInRect(pTemp) ||
                        reStraza2.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pStrazy;
                }

                // Ausreden fuer Wappen
                if (wappen.isPointInRect(pTemp)) {
                    // hlebija
                    nextActionID = mainFrame.whatItem == 42 ? 200 : 160;
                    pTemp = pWappen;
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

                // zu Terassa gehen ?
                if (ausgangTerassa.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangTerassa.isPointInRect(kt)) {
                        pTemp = pExitTerassa;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitTerassa.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Hdwor gehen ?
                // nur in Dienstkleidung moeglich, sonst Dialog mit Wachen
                if (ausgangHdwor.isPointInRect(pTemp)) {
                    if (mainFrame.actions[511]) {
                        nextActionID = 101;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!ausgangHdwor.isPointInRect(kt)) {
                            pTemp = pExitHdwor;
                        } else {
                            // es wird nach unten verlassen
                            pTemp = new GenericPoint(kt.x, pExitHdwor.y);
                        }

                        if (mainFrame.isDoubleClick) {
                            mainFrame.krabat.stopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    } else {
                        nextActionID = 50;
                        pTemp = pStrazy;
                    }
                }

                // Wache 1 oder 2 ansehen
                if (reStraza1.isPointInRect(pTemp) ||
                        reStraza2.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pStrazy;
                }

                // Wappen ansehen
                if (wappen.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pWappen;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit Wachen reden
                if (reStraza1.isPointInRect(pTemp) ||
                        reStraza2.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pStrazy);
                    mainFrame.repaint();
                    return;
                }

                // Wappen mitnehmen
                if (wappen.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(pWappen);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangHdwor.isPointInRect(pTemp) ||
                        ausgangTerassa.isPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) || reStraza1.isPointInRect(pTemp) ||
                    reStraza2.isPointInRect(pTemp) || wappen.isPointInRect(pTemp);

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
            if (ausgangTerassa.isPointInRect(pTemp)) {
                if (cursorShape != 6) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 6;
                }
                return;
            }

            if (ausgangHdwor.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (reStraza1.isPointInRect(pTemp) ||
                    reStraza2.isPointInRect(pTemp) ||
                    wappen.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
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

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 541) {
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
                // Wachen anschauen
                krabatSays("Straze_1", fStraze, 3, 0, 0);
                break;

            case 2:
                // Wappen anschauen
                krabatSays("Straze_2", fWapon, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt Dialog mit Wachen
                mainFrame.krabat.setFacing(fStraze);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // Je nachdem, ob Dienstkleidung, passelnd quasseln
                if (mainFrame.actions[511]) {
                    nextActionID = 620;
                } else {
                    if (mainFrame.actions[510]) {
                        nextActionID = 610;
                    } else {
                        nextActionID = 600;
                    }
                }
                break;

            case 60:
                // Wappen mitnehmen
                krabatSays("Straze_3", fWapon, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Terassa
                createNewLocation(127, locationID);
                break;

            case 101:
                // Gehe zu Hdwor
                createNewLocation(130, locationID);
                break;

            case 155:
                // Straze - Ausreden
                maleExcuse(fStraze);
                break;

            case 160:
                // Wappen - Ausreden
                thingExcuse(fWapon);
                break;

            case 200:
                // hlebija auf Wappen
                krabatSays("Straze_4", fWapon, 3, 0, 0);
                break;

            case 541:
                // Krabat mit Bedienstetenkleidung benutzen, hier Extrawurst !!!
                krabatSays("Straze_5", 0, 3, 0, 0);
                break;

            // Dialog mit Wachen

            case 600:
                // Reaktion Wache 1
                straza1VersperrtWeg = true;
                personSays("Straze_6", 0, 43, 2, 601, talkPoint);
                break;

            case 601:
                // Reaktion Wache 2
                straza2VersperrtWeg = true;
                personSays("Straze_7", 0, 44, 2, 602, talkPoint2);
                break;

            case 602:
                // Reaktion Wache 1
                personSays("Straze_8", 0, 43, 2, 603, talkPoint);
                break;

            case 603:
                // Reaktion Krabat
                krabatSays("Straze_9", 0, 1, 2, 604);
                break;

            case 604:
                // Reaktion Wache 2
                personSays("Straze_10", 0, 44, 2, 605, talkPoint2);
                break;

            case 605:
                // Reaktion Wache 1
                personSays("Straze_11", 0, 43, 2, 606, talkPoint);
                break;

            case 606:
                // Reaktion Krabat
                krabatSays("Straze_12", 0, 1, 2, 800);
                mainFrame.actions[510] = true;    // Gesprach nicht wiederholen
                break;

            // Kurzer Dialog
            case 610:
                // Reaktion Wache 1
                straza1VersperrtWeg = true;
                personSays("Straze_13", 0, 43, 2, 611, talkPoint);
                break;

            case 611:
                // Reaktion Wache 2
                straza2VersperrtWeg = true;
                personSays("Straze_14", 0, 44, 2, 800, talkPoint2);
                break;

            case 620:
                // Reaktion Wache 2
                personSays("Straze_15", 0, 44, 2, 800, talkPoint2);
                break;


            case 800:
                // Dialog beenden, wenn zuende gelabert...
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