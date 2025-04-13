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

public class Couch extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Couch.class);
    private GenericImage background;
    private GenericImage offeneTuer;
    private GenericImage vordertuer;
    private GenericImage fallen;

    private boolean istTuerOffen = false;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang
            = new BorderRect(490, 165, 590, 418);
    private static final BorderRect untererAusgang
            = new BorderRect(120, 445, 500, 479);
    private static final BorderRect durje
            = new BorderRect(257, 212, 347, 375);
    private static final BorderRect couch
            = new BorderRect(53, 115, 203, 386);

    // Konstante Points
    private static final GenericPoint pExitRight = new GenericPoint(510, 426);
    private static final GenericPoint pExitDown = new GenericPoint(315, 479);
    private static final GenericPoint pDurje = new GenericPoint(280, 383);
    private static final GenericPoint pCouch = new GenericPoint(145, 406);

    // Konstante ints
    private static final int fDurje = 12;
    private static final int fCouch = 9;

    private int SonderAnim = 0;

    private int Fallgeschwindigkeit = 1;

    private static final int MAX_FALLGESCHWINDIGKEIT = 18;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Couch(Start caller, int oldLocation) {
        super(caller, 144);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 479;
        mainFrame.krabat.zoomFactor = 4.8f;
        mainFrame.krabat.defaultScale = -60;

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(265, 370, 85, 370, 383, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(371, 505, 371, 500, 426, 479));

        mainFrame.pathFinder.clearMatrix(2);

        mainFrame.pathFinder.connectPos(0, 1);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 143: // von Casnik aus
                mainFrame.krabat.setPos(new GenericPoint(510, 426));
                mainFrame.krabat.setFacing(9);
                break;
            case 145: // von Zelen aus
                mainFrame.krabat.setPos(new GenericPoint(315, 465));
                mainFrame.krabat.setFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/couch/couch.png");
        offeneTuer = getPicture("gfx-dd/couch/couch3.png");

        vordertuer = getPicture("gfx-dd/couch/couch2.png");
        fallen = getPicture("gfx-dd/couch/s-o-fallen.png");

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

        // Tuer zeichnen, wenn geoeffnet
        if (istTuerOffen) {
            g.setClip(253, 211, 54, 171);
            g.drawImage(offeneTuer, 253, 211);
            g.drawImage(vordertuer, 219, 233);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        mainFrame.pathWalker.doWalk();

        if (SonderAnim != 0) {
            // hier erstmal alles berechnen, dann je nachdem die Bilder switchen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.getPos().x, mainFrame.krabat.getPos().y);

            // Groesse
            int scale = mainFrame.krabat.defaultScale;
            scale += (int) (((float) mainFrame.krabat.maxX - (float) hier.y) / mainFrame.krabat.zoomFactor);

            // System.out.println ("Scale ist " + scale + " gross.");

            // Hoehe: nur offset
            int hoch = 100 - scale;

            // Breite abhaengig von Hoehe...
            int weit = 50 - scale / 2;

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            g.drawImage(fallen, hier.x, hier.y, weit, hoch);

            Fallgeschwindigkeit++;
            if (Fallgeschwindigkeit == MAX_FALLGESCHWINDIGKEIT) {
                SonderAnim = 0;
            }

            GenericPoint tmp = mainFrame.krabat.getPos();
            tmp.y += Fallgeschwindigkeit * 2;
            mainFrame.krabat.setPos(tmp);
        } else {
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
        }

        if (istTuerOffen) {
            g.drawImage(vordertuer, 219, 233);
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

                // Ausreden fuer Tuer
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTemp = pDurje;
                }

                // Ausreden fuer couch
                if (couch.isPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pCouch;
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

                // zu Casnik gehen ?
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = pExitRight;
                    } else {
                        pTemp = new GenericPoint(pExitRight.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Zelen gehen ?
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTemp = pExitDown;
                    } else {
                        pTemp = new GenericPoint(pExitDown.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Tuer anschauen
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pDurje;
                }

                // Couch anschauen
                if (couch.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pCouch;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (untererAusgang.isPointInRect(pTemp) ||
                        rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // durje benutzen
                if (durje.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Couch benutzen
                if (couch.isPointInRect(pTemp)) {
                    nextActionID = 90;
                    mainFrame.pathWalker.setNewWay(pCouch);
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
                    durje.isPointInRect(pTemp) ||
                    couch.isPointInRect(pTemp);

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
            if (durje.isPointInRect(pTemp) ||
                    couch.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
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

            if (untererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 6) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 6;
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
                // durje ansehen
                if (!mainFrame.actions[670]) {
                    krabatSays("Couch_1", fDurje, 3, 0, 0);
                } else {
                    krabatSays("Couch_2", fDurje, 3, 0, 0);
                }
                break;

            case 2:
                // Couch ansehen
                krabatSays("Couch_3", fCouch, 3, 0, 0);
                break;

            case 50:
                // Durje oeffnen
                // keine 2 Mal durchfuehren
                if (mainFrame.actions[670]) {
                    krabatSays("Couch_4", fDurje, 3, 0, 0);
                } else {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    BackgroundMusicPlayer.getInstance().stop();
                    nextActionID = 55;
                    mainFrame.actions[670] = true;
                    istTuerOffen = true;
                    mainFrame.soundPlayer.playFile("sfx/kdurjeauf.wav");
                    mainFrame.krabat.setFacing(fDurje);
                }
                break;

            case 55:
                // Animszene Tuer ist offen
                krabatSays("Couch_5", 0, 3, 2, 60);
                break;

            case 60:
                // Animszene Tuer ist offen
                krabatSays("Couch_6", 0, 3, 2, 70);
                break;

            case 70:
                // runterfallen lassen
                SonderAnim = 1;
                krabatSays("Couch_7", 0, 3, 2, 75);
                break;

            case 75:
                // Gehe zu Murja draussen
                if (SonderAnim != 0) {
                    break;
                }
                Counter = 20;
                nextActionID = 76;
                break;

            case 76:
                // bisschen warten, bevor Sound
                if (--Counter > 0) {
                    break;
                }
                mainFrame.soundPlayer.playFile("sfx-dd/gebuesch.wav");
                Counter = 15;
                nextActionID = 77;
                break;

            case 77:
                // warten und Locationswitch
                if (--Counter > 1) {
                    break;
                }
                createNewLocation(126, locationID);
                break;

            case 90:
                // Couch mitnehmen
                krabatSays("Couch_8", fCouch, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Casnik
                createNewLocation(143, locationID);
                break;

            case 101:
                // Gehe zu Zelen
                createNewLocation(145, locationID);
                break;

            case 150:
                // durje-Ausreden
                thingExcuse(fDurje);
                break;

            case 155:
                // couch-Ausreden
                thingExcuse(fCouch);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}