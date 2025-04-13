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
import de.codengine.krabat.anims.Bludnicki;
import de.codengine.krabat.anims.Fire;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Labyr7 extends MainLabyrinth {
    private static final Logger log = LoggerFactory.getLogger(Labyr7.class);
    private GenericImage background;
    private GenericImage lab72;

    private Bludnicki irrlicht;
    private Fire feuer;

    private boolean isBlinker = false;
    private boolean bludVisible = false;
    private int Ausgang = 0;
    private static final GenericPoint Alinks = new GenericPoint(10, 331);
    private static final GenericPoint Arechts = new GenericPoint(610, 318);
    private GenericPoint AusPoint = new GenericPoint(0, 0);

    // Konstanten - Rects
    private static final BorderRect linkerAusgang = new BorderRect(0, 324, 39, 433);
    private static final BorderRect rechterAusgang = new BorderRect(593, 342, 639, 454);
    private static final BorderRect lab72Rect = new BorderRect(92, 276, 372, 438);

    // Exitlocation - Array
    private static final int[] Exitright = {/*51,*/ 52, 53, 54, 56, 58, 59, 61};
    private static final int[] Exitleft = {/*51,*/ 52, 53, 55, 58, 59, 60, 62};

    // Konstante Points
    private static final GenericPoint Pleft = new GenericPoint(0, 380);
    private static final GenericPoint Pright = new GenericPoint(639, 400);

    // Nach diesem Fusspunkt richtet sich die komplette Location !!!
    private static final GenericPoint bludFeet = new GenericPoint(515, 360);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Labyr7(Start caller, int Richtung) {
        super(caller);
        log.debug("Laby 7");

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 7.25f;
        mainFrame.krabat.defaultScale = -10;

        irrlicht = new Bludnicki(mainFrame);
        feuer = new Fire(mainFrame);

        // Definitionen fuer Hauptklasse
        int th = bludFeet.x - irrlicht.breite / 2;
        bludRect = new BorderRect(th, bludFeet.y - irrlicht.hoehe, th + irrlicht.breite, bludFeet.y);
        bludTalk = new GenericPoint(bludFeet.x, bludFeet.y - irrlicht.hoehe - 50);

        bludPoint = new GenericPoint(479, 380); // Krabats Punkt zum Anschauen/Benutzen des Blud
        bludFacing = 3;
        locIndex = 57;

        initLocation(Richtung);

        // hier Entscheidung, ob Blud da ist oder nicht
        if (mainFrame.actions[235]) {
            bludVisible = true;
        }

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int Richtung) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 351, 166, 405));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(167, 366, 390, 386));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(391, 371, 639, 405));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(3);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);

        initImages();
        switch (Richtung) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(10, true);
                Ausgang = calculateExit(false, false, true, true);
                break;
            case 9:
                // von rechts aus
                mainFrame.krabat.setPos(new GenericPoint(617, 400));
                mainFrame.krabat.setFacing(9);
                Ausgang = calculateExit(false, false, true, false);
                break;
            case 3:
                // von links aus
                mainFrame.krabat.setPos(new GenericPoint(24, 380));
                mainFrame.krabat.setFacing(3);
                Ausgang = calculateExit(false, false, false, true);
                break;
        }
        // Hier richtigen Punkt fuers Geblinker eintragen
        if (Ausgang == 3) {
            AusPoint = Arechts;
        }
        if (Ausgang == 9) {
            AusPoint = Alinks;
        }

        if (mainFrame.actions[236]) {
            isBlinker = true;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/labyrinth/laby7.png");
        lab72 = getPicture("gfx/labyrinth/lab7-2.png");

    }

    @Override
    public void cleanup() {
        background = null;
        lab72 = null;

        irrlicht.cleanup();
        irrlicht = null;
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

        // Irrlichter zeichnen
        if (bludVisible) {
            g.setClip(bludRect.topLeftPoint.x, bludRect.topLeftPoint.y, irrlicht.breite, irrlicht.hoehe);
            g.drawImage(background, 0, 0);
            irrlicht.drawBludnicki(g, talkPerson, bludRect.topLeftPoint, doesHeListen, bludNimmt);
        }

        // Blinkern zeichnen
        if (isBlinker) {
            g.setClip(AusPoint.x, AusPoint.y, Fire.Breite, Fire.Hoehe);
            g.drawImage(background, 0, 0);
            feuer.drawPlomja(g, AusPoint);
        }

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
        if (lab72Rect.isPointInRect(pKrTemp)) {
            g.drawImage(lab72, 126, 199);
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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            dialog.paintMultiple(g);
            return;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // bei Multiple Choice extra Mouseroutine
        if (mainFrame.isMultipleChoiceActive) {
            dialog.evalMouseEvent(e);
            return;
        }

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

                // Ausreden fuer Irrlichter
                if (bludRect.isPointInRect(pTemp) && bludVisible) {
                    if (mainFrame.whatItem == 15) {
                        nextActionID = 155;
                    } else {
                        nextActionID = 150;
                    }
                    pTemp = bludPoint;
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

                // zu naechstem Laby gehen rechts
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

                // Irrlichter ansehen
                if (bludRect.isPointInRect(pTemp) && bludVisible) {
                    nextActionID = 1;
                    pTemp = bludPoint;
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
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Irrlicht reden
                if (bludRect.isPointInRect(pTemp) && bludVisible) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(bludPoint);
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
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultipleChoiceActive) {
            dialog.evalMouseMoveEvent(pTemp);
            return;
        }

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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) || bludRect.isPointInRect(pTemp) && bludVisible;

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
            if (bludRect.isPointInRect(pTemp) && bludVisible) {
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

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultipleChoiceActive) {
            dialog.evalMouseExitEvent();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultipleChoiceActive) {
            return;
        }

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

    private void evalNewLocationRight() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : Exitright) {
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

        // Hier Verzweigung zu Blud - Nextactions, wenn noetig
        if (nextActionID < 100 || nextActionID > 130) {
            bludAction(nextActionID);

            // Manchmal neuer Cursor wegen MC
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
                mainFrame.destructLocation(57);
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu naechstem Laby rechts
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                appear(Ausgang == 3);
                if (mainFrame.actions[235]) {
                    bludLocationRight();
                } else {
                    evalNewLocationRight();
                }
                mainFrame.constructLocation(newloc, 3);
                mainFrame.destructLocation(57);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}