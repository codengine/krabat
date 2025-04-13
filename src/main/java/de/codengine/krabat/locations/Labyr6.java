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
import de.codengine.krabat.anims.Fire;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Labyr6 extends MainLabyrinth {
    private static final Logger log = LoggerFactory.getLogger(Labyr6.class);
    private GenericImage background;
    private GenericImage lab62;
    private GenericImage lab63;

    private Fire feuer;

    private boolean isBlinker = false;
    private int Ausgang = 0;
    private static final GenericPoint Aunten = new GenericPoint(490, 460);
    private static final GenericPoint Alinks = new GenericPoint(10, 210);
    private GenericPoint AusPoint = new GenericPoint(0, 0);

    // Konstanten - Rects
    private static final BorderRect untererAusgang = new BorderRect(283, 435, 498, 479);
    private static final BorderRect linkerAusgang = new BorderRect(0, 248, 28, 326);
    private static final BorderRect lab62Rect = new BorderRect(70, 193, 384, 336);
    private static final BorderRect lab63Rect = new BorderRect(378, 348, 457, 378);

    // Exitlocation - Array
    private static final int[] Exitdown = {52, 54, 55, 58, 60, 61};
    private static final int[] Exitleft = {51, 52, 53, 55, 57, 58, 59, 60, /*62*/};

    private static final GenericPoint Pleft = new GenericPoint(0, 282);
    private static final GenericPoint Pdown = new GenericPoint(385, 479);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Labyr6(Start caller, int Richtung) {
        super(caller);
        log.debug("Laby 6");

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 6.97f;
        mainFrame.krabat.defaultScale = 0;

        feuer = new Fire(mainFrame);

        initLocation(Richtung);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int Richtung) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(344, 362, 344, 414, 389, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(344, 356, 362, 388));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(312, 322, 312, 382, 305, 355));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(228, 305, 311, 319));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(198, 269, 227, 319));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 269, 197, 293));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(6);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
        mainFrame.pathFinder.connectPos(4, 5);

        initImages();
        switch (Richtung) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(10, true);
                Ausgang = calculateExit(false, true, true, false);
                break;
            case 3:
                // von links aus
                mainFrame.krabat.setPos(new GenericPoint(34, 284));
                mainFrame.krabat.setFacing(3);
                Ausgang = calculateExit(false, true, false, false);
                break;
            case 12:
                // von unten aus
                mainFrame.krabat.setPos(new GenericPoint(385, 452));
                mainFrame.krabat.setFacing(12);
                Ausgang = calculateExit(false, false, true, false);
                break;
        }
        // Hier richtigen Punkt fuers Geblinker eintragen
        if (Ausgang == 9) {
            AusPoint = Alinks;
        }
        if (Ausgang == 6) {
            AusPoint = Aunten;
        }

        if (mainFrame.actions[236]) {
            isBlinker = true;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/labyrinth/laby6.png");
        lab62 = getPicture("gfx/labyrinth/lab6-2.png");
        lab63 = getPicture("gfx/labyrinth/lab6-3.png");

    }

    @Override
    public void cleanup() {
        background = null;
        lab62 = null;
        lab63 = null;

        feuer.cleanup();
        feuer = null;
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

        // Blinkern zeichnen
        if (isBlinker) {
            g.setClip(AusPoint.x, AusPoint.y, Fire.Breite, Fire.Hoehe);
            g.drawImage(background, 0, 0);
            feuer.drawPlomja(g, AusPoint);
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
        if (lab62Rect.isPointInRect(pKrTemp)) {
            g.drawImage(lab62, 84, 162);
        }

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab63Rect.isPointInRect(pKrTemp)) {
            g.drawImage(lab63, 397, 357);
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

                // zu naechstem Laby gehen links
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.isPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu naechstem Laby gehen unten
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
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

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Naechstes Laby anschauen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Naechstes Laby anschauen
                if (untererAusgang.isPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp);

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

            if (untererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 5;
                }
                return;
            }

            if (linkerAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    cursorShape = 2;
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

    private void evalNewLocationLeft() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * Start.LABYRINTH_HELP) + 50;
            while (zuffi > 62) {
                zuffi--;
            }
            for (int j : Exitleft) {
                if (zuffi == j) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    private void evalNewLocationDown() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : Exitdown) {
                if (zuffi == j) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
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
            case 100:
                // Gehe zu naechstem Laby links
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                appear(Ausgang == 9);
                if (mainFrame.actions[235]) {
                    bludLocationLeft();
                } else {
                    evalNewLocationLeft();
                }
                mainFrame.constructLocation(newloc, 9);
                mainFrame.destructLocation(56);
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu naechstem Laby down
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                appear(Ausgang == 6);
                if (mainFrame.actions[235]) {
                    bludLocationDown();
                } else {
                    evalNewLocationDown();
                }
                mainFrame.constructLocation(newloc, 6);
                mainFrame.destructLocation(56);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}