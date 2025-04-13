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

public class Jitk1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Jitk1.class);
    private GenericImage background /* , strauch */;

    // Punkte in Location
    private static final GenericPoint Pwagen = new GenericPoint(242, 455);
    private static final GenericPoint Peingang = new GenericPoint(366, 339);
    private static final GenericPoint Pleft = new GenericPoint(87, 138);
    private static final GenericPoint Pright = new GenericPoint(639, 360);
    private static final GenericPoint Pdown = new GenericPoint(261, 479);

    // Konstanten - Rects deklarieren
    private static final BorderRect rechterAusgang = new BorderRect(598, 327, 639, 412);
    private static final BorderRect linkerAusgang = new BorderRect(0, 67, 131, 151);
    private static final BorderRect untererAusgang = new BorderRect(172, 435, 364, 479);
    private static final BorderRect brEingang = new BorderRect(353, 250, 397, 334);
    private static final BorderRect brWagen = new BorderRect(0, 338, 142, 479);

    // Konstante ints
    private static final int fWagen = 9;
    private static final int fEingang = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jitk1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 374;
        mainFrame.krabat.zoomFactor = 3.38f;
        mainFrame.krabat.defaultScale = 0;

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(276, 346, 213, 296, 399, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(276, 639, 276, 528, 375, 398));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(633, 639, 573, 639, 352, 374));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(271, 357, 368, 374));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(360, 374, 339, 367, 345, 356));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(218, 241, 271, 309, 328, 356));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(154, 176, 218, 241, 260, 327));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(91, 96, 154, 176, 220, 259));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(83, 90, 91, 96, 137, 219));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(9);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(1, 3);
        mainFrame.pathFinder.connectPos(3, 4);
        mainFrame.pathFinder.connectPos(3, 5);
        mainFrame.pathFinder.connectPos(5, 6);
        mainFrame.pathFinder.connectPos(6, 7);
        mainFrame.pathFinder.connectPos(7, 8);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 5:
                // von Les1 aus
                if (mainFrame.enteringFromMap) {
                    mainFrame.enteringFromMap = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(616, 371));
                mainFrame.krabat.setFacing(9);
                break;
            case 6:
                // von Doma aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(284, 453));
                mainFrame.krabat.setFacing(12);
                break;
            case 1:
                // von Ralbicy aus
                if (mainFrame.enteringFromMap) {
                    mainFrame.enteringFromMap = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(90, 154));
                mainFrame.krabat.setFacing(6);
                break;
        }
        mainFrame.enteringFromMap = false;
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/jitk/jitk.png");

    }

    @Override
    public void cleanup() {
        background = null;
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

        // Krabat einen Schritt gehen lassen
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

                // Ausreden fuer Eingang
                if (brEingang.isPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
                    pTemp = Peingang;
                }

                // Ausreden fuer Wagen
                if (brWagen.isPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 155;
                    pTemp = Pwagen;
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

                // zu Doma gehen ?
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Ralbicy gehen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.isPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        // Wegen nach oben gehen hier diese Variante
                        pTemp = new GenericPoint(kt.x, Pleft.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Les1 gehen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Tuer ansehen
                if (brEingang.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Peingang;
                }

                // Wagen ansehen
                if (brWagen.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pwagen;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Doma Anschauen
                if (untererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Ralbitz anschauen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Les1 anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Tuer mitnehmen
                if (brEingang.isPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.pathWalker.setNewWay(Peingang);
                    mainFrame.repaint();
                    return;
                }

                // Wagen mitnehmen
                if (brWagen.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Pwagen);
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
            mainFrame.isInventoryHighlightCursor = brEingang.isPointInRect(pTemp) || brWagen.isPointInRect(pTemp) ||
                    tmp.isPointInRect(pTemp);

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
            if (linkerAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 2) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 2;
                }
                return;
            }

            if (brEingang.isPointInRect(pTemp) ||
                    brWagen.isPointInRect(pTemp)) {
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
                if (cursorShape != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 5;
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
            case 1:
                // Eingang anschauen
                krabatSays("Jitk1_1", fEingang, 3, 0, 0);
                break;

            case 2:
                // Wagen anschauen
                krabatSays("Jitk1_2", fWagen, 3, 0, 0);
                break;

            case 50:
                // Wagen mitnehmen
                krabatSays("Jitk1_3", fWagen, 3, 0, 0);
                break;

            case 51:
                // Tuer mitnehmen
                krabatSays("Jitk1_4", fEingang, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Doma
                createNewLocation(6, 3);
                break;

            case 101:
                // gehe zu Les1
                createNewLocation(5, 3);
                break;

            case 102:
                // nach Ralbitz gehen
                createNewLocation(1, 3);
                break;

            case 150:
                // Eingang - Ausreden
                thingExcuse(fEingang);
                break;

            case 155:
                // Wagen - Ausreden
                thingExcuse(fWagen);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}