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

public class Kuchnjaopen extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Kuchnjaopen.class);
    private GenericImage background;
    private GenericImage herd;
    private GenericImage schwein/* , herd2, herd3 */;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang
            = new BorderRect(467, 120, 505, 320);
    private static final BorderRect glocke
            = new BorderRect(104, 230, 125, 252);
    private static final BorderRect drjewo
            = new BorderRect(237, 342, 305, 392);
    private static final BorderRect durje
            = new BorderRect(9, 87, 100, 380);
    private static final BorderRect wokno
            = new BorderRect(560, 0, 640, 118);
    private static final BorderRect swinjo
            = new BorderRect(454, 387, 639, 479);
    private static final BorderRect herdRect     // zum drueberzeichnen
            = new BorderRect(0, 386, 347, 479);
    private static final BorderRect schweinRect  // zum drueberzeichnen
            = new BorderRect(388, 344, 639, 479);

    // Konstante Points
    private static final GenericPoint pRight = new GenericPoint(449, 320);
    private static final GenericPoint pSwinjo = new GenericPoint(490, 430);
    private static final GenericPoint pDrjewo = new GenericPoint(260, 400);
    private static final GenericPoint pWokno = new GenericPoint(575, 355);
    private static final GenericPoint pGlocke = new GenericPoint(121, 381);
    private static final GenericPoint pDurje = new GenericPoint(79, 398);

    // Konstante ints
    private static final int fSwinjo = 6;
    private static final int fDrjewo = 12;
    private static final int fDurje = 9;
    private static final int fGlocke = 9;
    private static final int fWokno = 12;
    private static final int fHerd = 6;
    private static final int fHornc = 6;

    // private static final int fStraza = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Kuchnjaopen(Start caller, int oldLocation) {
        super(caller, 132);

        mainFrame.freeze(true);

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 2f;
        mainFrame.krabat.defaultScale = -80;

        // hlStraznik  = new HlownyStraznik (mainFrame, false, mainFrame.Actions[705]);
        //                                           nicht Casnik !!!!
        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {

        // Feuer-Variablen immer auf niedrigste Groesse setzen
        mainFrame.actions[625] = false;
        mainFrame.actions[626] = false;
        mainFrame.actions[627] = false;

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(400, 311, 449, 354));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(370, 575, 360, 610, 355, 399));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(125, 610, 75, 620, 400, 430));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(75, 380, 75, 380, 431, 479));

        mainFrame.pathFinder.clearMatrix(4);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 121:
                // von Hintergasse aus
                mainFrame.krabat.setPos(new GenericPoint(422, 320));
                mainFrame.krabat.setFacing(9);
                break;
            case 145:
                // von August reingesteckt worden - STRAFE !
                // Dienstkleidung zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(41);
                // Eigene Kleidung entfernen
                mainFrame.inventory.vInventory.removeElement(53);
                // Flag setzen -> normale Kleidung
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.krabat.setPos(new GenericPoint(370, 410));
                mainFrame.krabat.setFacing(3);
                break;
        }

        mainFrame.checkKrabat();

    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/kuchnja/kuchnja.png");
        herd = getPicture("gfx-dd/kuchnja/herd.png");
        schwein = getPicture("gfx-dd/kuchnja/schwein.png");

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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinter Herd oder Schwein ? (nur Clipping - Region wird neugezeichnet)
        if (herdRect.isPointInRect(pKrTemp)) {
            g.drawImage(herd, 25, 387);
        }
        if (schweinRect.isPointInRect(pKrTemp)) {
            g.drawImage(schwein, 427, 337);
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

                // Schwein Ausreden
                if (swinjo.isPointInRect(pTemp)) {
                    // kotwica
                    nextActionID = mainFrame.whatItem == 37 ? 195 : 160;
                    pTemp = pSwinjo;
                }

                // Holz Ausreden
                if (drjewo.isPointInRect(pTemp)) {
                    nextActionID = 165;
                    pTemp = pDrjewo;
                }

                // Tuer Ausreden
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTemp = pDurje;
                }

                // Glocke Ausreden
                if (glocke.isPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = pGlocke;
                }

                // Fenster Ausreden
                if (wokno.isPointInRect(pTemp)) {
                    nextActionID = 180;
                    pTemp = pWokno;
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

                // zu Hintergasse gehen ? geht hier...
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = pRight;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pRight.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schwein ansehen
                if (swinjo.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pSwinjo;
                }
                // Holz ansehen
                if (drjewo.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pDrjewo;
                }
                // Tuer ansehen
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pDurje;
                }
                // Glocke ansehen
                if (glocke.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pGlocke;
                }
                // Fenster ansehen
                if (wokno.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pWokno;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Holz mitnehmen
                if (drjewo.isPointInRect(pTemp)) {
                    nextActionID = 40;
                    mainFrame.pathWalker.setNewWay(pDrjewo);
                    mainFrame.repaint();
                    return;
                }

                // Schwein mitnehmen
                if (swinjo.isPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(pSwinjo);
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

                // Glocke mitnehmen
                if (glocke.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(pGlocke);
                    mainFrame.repaint();
                    return;
                }

                // Fenster mitnehmen
                if (wokno.isPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.setNewWay(pWokno);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (rechterAusgang.isPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) ||
                    glocke.isPointInRect(pTemp) ||
                    drjewo.isPointInRect(pTemp) ||
                    durje.isPointInRect(pTemp) ||
                    wokno.isPointInRect(pTemp) ||
                    swinjo.isPointInRect(pTemp);

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
            if (rechterAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    cursorShape = 3;
                }
                return;
            }

            if (glocke.isPointInRect(pTemp) ||
                    drjewo.isPointInRect(pTemp) ||
                    durje.isPointInRect(pTemp) ||
                    wokno.isPointInRect(pTemp) ||
                    swinjo.isPointInRect(pTemp)) {
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
            case 2:
                // Schwein anschauen
                int zuffZahl = (int) (Math.random() * 2.9);
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Kuchnjaopen_1", fSwinjo, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Kuchnjaopen_2", fSwinjo, 3, 0, 0);
                        break;

                    case 2:
                        krabatSays("Kuchnjaopen_3", fSwinjo, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Holz anschauen
                krabatSays("Kuchnjaopen_4", fDrjewo, 3, 0, 0);
                break;

            case 4:
                // Tuer anschauen
                krabatSays("Kuchnjaopen_5", fDurje, 3, 0, 0);
                break;

            case 5:
                // Glocke anschauen
                krabatSays("Kuchnjaopen_6", fGlocke, 3, 0, 0);
                break;

            case 6:
                // Fenster anschauen
                krabatSays("Kuchnjaopen_7", fWokno, 3, 0, 0);
                break;

            case 7:
                // Herd anschauen
                if (!mainFrame.actions[625] && !mainFrame.actions[626] && !mainFrame.actions[627]) {
                    krabatSays("Kuchnjaopen_8", fHerd, 3, 0, 0);
                } else {
                    krabatSays("Kuchnjaopen_9", fHerd, 3, 0, 0);
                }
                break;

            case 8:
                // Kochtopf anschauen
                if (!mainFrame.actions[625] && !mainFrame.actions[626] && !mainFrame.actions[627]) {
                    krabatSays("Kuchnjaopen_10", fHornc, 3, 0, 0);
                } else {
                    krabatSays("Kuchnjaopen_11", fHornc, 3, 0, 0);
                }
                break;


            case 40:
                // Holzscheitel mitnehmen ist hier nicht moeglich
                krabatSays("Kuchnjaopen_12", fDrjewo, 3, 0, 0);
                break;

            case 55:
                // Schwein mitnehmen
                krabatSays("Kuchnjaopen_13", fSwinjo, 3, 0, 0);
                break;

            case 60:
                // Tuer mitnehmen
                krabatSays("Kuchnjaopen_14", fDurje, 3, 0, 0);
                break;

            case 65:
                // Glocke mitnehmen
                krabatSays("Kuchnjaopen_15", fGlocke, 3, 0, 0);
                break;

            case 70:
                // Fenster mitnehmen
                krabatSays("Kuchnjaopen_16", fWokno, 3, 0, 0);
                break;

            case 75:
                // Kochtopf mitnehmen
                krabatSays("Kuchnjaopen_17", fHornc, 3, 0, 0);
                break;

            case 80:
                // Herd mitnehmen
                krabatSays("Kuchnjaopen_18", fHornc, 3, 0, 0);
                break;

            case 100:
                // Animationssequenz beenden
                createNewLocation(121, locationID);
                break;

            case 155:
                // Dinge ins Feuer schmeissen
                krabatSays("Kuchnjaopen_19", fHerd, 3, 0, 0);
                break;

            case 160:
                // Dinge dem Schwein geben
                krabatSays("Kuchnjaopen_20", fSwinjo, 3, 0, 0);
                break;

            case 165:
                // Drjewo - Ausreden
                thingExcuse(fDrjewo);
                break;

            case 170:
                // durje - Ausreden
                thingExcuse(fDurje);
                break;

            case 175:
                // klinkac  Ausreden
                thingExcuse(fGlocke);
                break;

            case 180:
                // wokno - Ausreden
                thingExcuse(fWokno);
                break;

            case 185:
                // Dinge in den Topf werfen
                krabatSays("Kuchnjaopen_21", fHornc, 3, 0, 0);
                break;

            case 195:
                // kotwica auf swino
                krabatSays("Kuchnjaopen_22", fSwinjo, 3, 0, 0);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
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