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

public class Hala extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Hala.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage door;
    private boolean setScroll = false;
    private int scrollwert;

    // Konstanten - Rects
    private static final BorderRect linkerAusgang
            = new BorderRect(135, 120, 273, 399);
    private static final BorderRect zweiteTuer
            = new BorderRect(464, 184, 577, 399);
    private static final BorderRect dritteTuer
            = new BorderRect(820, 113, 963, 403);
    private static final BorderRect rechterAusgang
            = new BorderRect(1240, 111, 1279, 479);
    private static final BorderRect wobraz
            = new BorderRect(468, 13, 585, 150);

    // Konstante Points
    private static final GenericPoint pExitLinks = new GenericPoint(180, 403);
    private static final GenericPoint pExitKomedij = new GenericPoint(920, 407);
    private static final GenericPoint pExitRechts = new GenericPoint(1230, 474);
    private static final GenericPoint pZweiteTuer = new GenericPoint(523, 408);
    private static final GenericPoint pWobraz = new GenericPoint(520, 397);


    // Konstante ints
    private static final int fTueren = 12;
    private static final int fBild = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Hala(Start caller, int oldLocation) {
        super(caller, 123);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomFactor = 1f;
        mainFrame.krabat.defaultScale = -100;

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(140, 810, 25, 810, 407, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(811, 1115, 811, 1235, 407, 479));

        mainFrame.pathFinder.clearMatrix(2);

        mainFrame.pathFinder.connectPos(0, 1);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(16, true);
                break;
            case 122: // von Spaniska aus
                mainFrame.krabat.setPos(new GenericPoint(180, 415));
                mainFrame.krabat.setFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;
            case 124: // von Komedij aus
                mainFrame.krabat.setPos(new GenericPoint(920, 420));
                mainFrame.krabat.setFacing(6);
                scrollwert = 600;
                setScroll = true;
                break;
            case 125: // von Jewisco aus
                mainFrame.krabat.setPos(new GenericPoint(1220, 470));
                mainFrame.krabat.setFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        backl = getPicture("gfx-dd/hala/hala-l.png");
        backr = getPicture("gfx-dd/hala/hala-r.png");
        door = getPicture("gfx-dd/hala/hala-r2.png");

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

        // offene Tuer zeichnen, sobald da
        if (mainFrame.actions[675]) {
            g.setClip(883, 112, 84, 292);
            g.drawImage(door, 883, 112);
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

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
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

                // Ausreden fuer Tuer, wenn noch anwaehlbar
                if (dritteTuer.isPointInRect(pTemp) && !mainFrame.actions[675]) {
                    nextActionID = 150;
                    pTemp = pExitKomedij;
                }

                // Ausreden fuer 2. Tuer
                if (zweiteTuer.isPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pZweiteTuer;
                }

                // Ausreden fuer Bild
                if (wobraz.isPointInRect(pTemp)) {
                    // hlebija
                    nextActionID = mainFrame.whatItem == 42 ? 200 : 155;
                    pTemp = pWobraz;
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

                // zu Spaniska gehen ?
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.isPointInRect(kt)) {
                        pTemp = pExitLinks;
                    } else {
                        pTemp = new GenericPoint(pExitLinks.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Komedij gehen , wenn schon geoeffnet
                if (mainFrame.actions[675]) {
                    if (dritteTuer.isPointInRect(pTemp)) {
                        nextActionID = 101;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!dritteTuer.isPointInRect(kt)) {
                            pTemp = pExitKomedij;
                        } else {
                            pTemp = new GenericPoint(pExitKomedij.x, kt.y);
                        }

                        if (mainFrame.isDoubleClick) {
                            mainFrame.krabat.stopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    }
                } else {
                    if (dritteTuer.isPointInRect(pTemp)) {
                        // Tuer ist noch nicht geoeffnet
                        nextActionID = 5;
                        pTemp = pExitKomedij;
                    }
                }

                // zu Jewisco gehen ?
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = pExitRechts;
                    } else {
                        pTemp = new GenericPoint(pExitRechts.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // 2. Tuer ansehen
                if (zweiteTuer.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pZweiteTuer;
                }

                // Bild ansehen
                if (wobraz.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pWobraz;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (linkerAusgang.isPointInRect(pTemp) ||
                        rechterAusgang.isPointInRect(pTemp) ||
                        dritteTuer.isPointInRect(pTemp) && mainFrame.actions[675]) {
                    return;
                }

                // verschlossene Tuer oeffnen (erfolglos)
                if (zweiteTuer.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    mainFrame.pathWalker.setNewWay(pZweiteTuer);
                    mainFrame.repaint();
                    return;
                }

                // offene Tuer oeffnen
                if (dritteTuer.isPointInRect(pTemp) && !mainFrame.actions[675]) {
                    nextActionID = 10;
                    mainFrame.pathWalker.setNewWay(pExitKomedij);
                    mainFrame.repaint();
                    return;
                }

                // Bild mitnehmen
                if (wobraz.isPointInRect(pTemp)) {
                    nextActionID = 15;
                    mainFrame.pathWalker.setNewWay(pWobraz);
                    mainFrame.repaint();
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) ||
                    wobraz.isPointInRect(pTemp) ||
                    dritteTuer.isPointInRect(pTemp) && !mainFrame.actions[675] ||
                    zweiteTuer.isPointInRect(pTemp);

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
            if (wobraz.isPointInRect(pTemp) ||
                    dritteTuer.isPointInRect(pTemp) && !mainFrame.actions[675] ||
                    zweiteTuer.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (linkerAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (dritteTuer.isPointInRect(pTemp) && mainFrame.actions[675]) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (rechterAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    cursorShape = 3;
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
                // 2. Tuer ansehen
                krabatSays("Hala_1", fTueren, 3, 0, 0);
                break;

            case 2:
                // Bild ansehen
                krabatSays("Hala_2", fBild, 3, 0, 0);
                break;

            case 5:
                // 3. Tuer  ansehen
                krabatSays("Hala_3", fTueren, 3, 0, 0);
                break;

            case 10:
                // Tuer aufmachen
                mainFrame.krabat.setFacing(fTueren);
                mainFrame.actions[675] = true;
                nextActionID = 0;
                mainFrame.soundPlayer.playFile("sfx/kdurjeauf.wav");
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 15:
                // Bild mitnehmen
                krabatSays("Hala_4", fBild, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Spaniska
                createNewLocation(122, locationID);
                break;

            case 101:
                // Gehe zu Komedij
                createNewLocation(124, locationID);
                break;

            case 102:
                // Gehe zu Jewisco
                createNewLocation(125, locationID);
                break;

            case 150:
                // durje-Ausreden
                thingExcuse(fTueren);
                break;

            case 155:
                // wobraz-Ausreden
                thingExcuse(fBild);
                break;

            case 160:
                // durje2-Ausreden
                thingExcuse(fTueren);
                break;

            case 200:
                // Hlebija auf bild
                krabatSays("Hala_5", fBild, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}