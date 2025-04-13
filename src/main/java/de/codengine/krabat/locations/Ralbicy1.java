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
import de.codengine.krabat.anims.FarmerRalbicy;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Ralbicy1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Ralbicy1.class);
    private GenericImage background;
    private GenericImage holz;
    private GenericImage kreuz;
    private FarmerRalbicy bauer;
    private final MultipleChoice Dialog;
    private boolean isListening = false;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang = new BorderRect(450, 423, 639, 479);
    private static final BorderRect linkerAusgang = new BorderRect(0, 290, 42, 361);
    private static final BorderRect brKirche = new BorderRect(175, 40, 639, 267);
    private static final BorderRect brBauer = new BorderRect(49, 221, 103, 250);

    // Punkte in Location
    private static final GenericPoint Pkirche = new GenericPoint(77, 316);
    private static final GenericPoint Pbauer = new GenericPoint(30, 303);
    private static final GenericPoint BurTalk = new GenericPoint(90, 200);
    private static final GenericPoint Pright = new GenericPoint(581, 479);
    private static final GenericPoint Pleft = new GenericPoint(0, 327);

    // Konstante ints
    private static final int fKirche = 3;
    private static final int fBauer = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Ralbicy1(Start caller, int oldLocation) {
        super(caller);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 543;
        mainFrame.krabat.zoomFactor = 4.92f;
        mainFrame.krabat.defaultScale = -20;

        bauer = new FarmerRalbicy(mainFrame);
        Dialog = new MultipleChoice(mainFrame);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 16, 0, 221, 296, 362));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 223, 97, 223, 363, 420));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(224, 372, 300, 435));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(301, 390, 408, 454));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(409, 390, 639, 479));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 3:
                // von Jitk aus
                if (mainFrame.enteringFromMap) {
                    mainFrame.enteringFromMap = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(581, 463));
                mainFrame.krabat.setFacing(9);
                break;
            case 2:
                // von Most aus
                if (mainFrame.enteringFromMap) {
                    mainFrame.enteringFromMap = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(20, 332));
                mainFrame.krabat.setFacing(3);
                break;
        }
        mainFrame.enteringFromMap = false;
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/ralbicy/ralbicy4.png");
        holz = getPicture("gfx/ralbicy/holz2.png");
        kreuz = getPicture("gfx/ralbicy/kreuz.png");

    }

    @Override
    public void cleanup() {
        background = null;
        holz = null;
        kreuz = null;

        bauer.cleanup();
        bauer = null;
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
        g.drawImage(holz, 0, 365);
        g.drawImage(kreuz, 118, 194);


        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Bauern zeichnen
        g.setClip(60, 223, 35, 32);
        g.drawImage(background, 0, 0);
        bauer.drawBur(g, talkPerson, isListening, false); // macht immer Geraeusche

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

        // muss noch mit Abfrage versehen werden (sonst unnuetz!)
        g.drawImage(holz, 0, 365);
        g.drawImage(kreuz, 118, 194);

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
            Dialog.paintMultiple(g);
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
            Dialog.evalMouseEvent(e);
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

                // Ausreden fuer Kirche
                if (brKirche.isPointInRect(pTemp)) {
                    // nur Standard
                    nextActionID = 150;
                    pTemp = Pkirche;
                }

                // Ausreden fuer Bauer
                if (brBauer.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                        case 9: // wuda
                            nextActionID = 200;
                            break;
                        case 15: // krosik
                            nextActionID = 220;
                            break;
                        case 18: // bron
                            nextActionID = 230;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Pbauer;
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

                // zu Jitk gehen ?
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, Pright.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Most gehen?
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
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

                // Kirche ansehen
                if (brKirche.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pkirche;
                }

                // Bauer ansehen
                if (brBauer.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pbauer;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Jitk anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Ausgang zu Most abfangen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Kirche mitnehmen
                if (brKirche.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Pkirche);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Bauern reden
                if (brBauer.isPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.pathWalker.setNewWay(Pbauer);
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
            Dialog.evalMouseMoveEvent(pTemp);
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
            mainFrame.isInventoryHighlightCursor = brKirche.isPointInRect(pTemp) ||
                    tmp.isPointInRect(pTemp) ||
                    brBauer.isPointInRect(pTemp);

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
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 3;
                }
                return;
            }

            if (brKirche.isPointInRect(pTemp) || brBauer.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
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
            Dialog.evalMouseExitEvent();
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
                // Kirche anschauen
                krabatSays("Ralbicy1_1", fKirche, 3, 0, 0);
                break;

            case 2:
                // Bauer anschauen
                krabatSays("Ralbicy1_2", fBauer, 3, 0, 0);
                break;

            case 50:
                // Kirche mitnehmen
                krabatSays("Ralbicy1_3", fKirche, 3, 0, 0);
                break;

            case 51:
                // Krabat beginnt MC (Bauer benutzen)
                mainFrame.krabat.setFacing(3);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                isListening = true;  // Bauer hoert auf zu arbeiten
                nextActionID = 600;
                break;

            case 100:
                // Gehe zu Jitk
                createNewLocation(3, 1);
                break;

            case 101:
                // nach Most gehen
                createNewLocation(2, 1);
                break;

            case 150:
                // Kirche - Ausreden
                thingExcuse(fKirche);
                break;

            case 155:
                // Bauer - Ausreden
                maleExcuse(fBauer);
                break;

            case 200:
                // Stock oder Angel auf Bauern
                krabatSays("Ralbicy1_4", fBauer, 3, 0, 0);
                break;

            case 220:
                // Krosik auf Bauern
                krabatSays("Ralbicy1_5", fBauer, 3, 0, 0);
                break;

            case 230:
                // Bron auf Bauern
                krabatSays("Ralbicy1_6", fBauer, 3, 0, 0);
                break;

            // Dialog mit Bauer

            case 600:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("Ralbicy1_20", 1000, 22, new int[]{22}, 640);
                Dialog.extend("Ralbicy1_21", 22, 21, new int[]{21}, 630);
                Dialog.extend("Ralbicy1_22", 21, 20, new int[]{20}, 620);
                Dialog.extend("Ralbicy1_23", 20, 1000, null, 610);

                // 2. Frage
                Dialog.extend("Ralbicy1_24", 1000, 23, new int[]{23}, 660);
                Dialog.extend("Ralbicy1_25", 23, 24, new int[]{24}, 650);

                // 3. Frage
                Dialog.extend("Ralbicy1_26", 1000, 1000, null, 670);

                // 4. Frage
                Dialog.extend("Ralbicy1_27", 25, 1000, null, 800);
                Dialog.extend("Ralbicy1_28", 1000, 25, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.actions[25] = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;
                nextActionID = Dialog.actionId;
                break;

            case 610:
                // Reaktion Bauer
                personSays("Ralbicy1_7", 0, 21, 2, 611, BurTalk);
                break;

            case 611:
                // Reaktion Bauer
                personSays("Ralbicy1_8", 0, 21, 2, 612, BurTalk);
                break;

            case 612:
                // Reaktion Bauer
                personSays("Ralbicy1_9", 0, 21, 2, 600, BurTalk);
                break;

            case 620:
                // Reaktion Bauer
                personSays("Ralbicy1_10", 0, 21, 2, 621, BurTalk);
                break;

            case 621:
                // Reaktion Bauer
                personSays("Ralbicy1_11", 0, 21, 2, 622, BurTalk);
                break;

            case 622:
                // Reaktion Bauer
                personSays("Ralbicy1_12", 0, 21, 2, 600, BurTalk);
                break;

            case 630:
                // Reaktion Bauer
                personSays("Ralbicy1_13", 0, 21, 2, 631, BurTalk);
                break;

            case 631:
                // Reaktion Bauer
                personSays("Ralbicy1_14", 0, 21, 2, 600, BurTalk);
                break;

            case 640:
                // Reaktion Bauer
                personSays("Ralbicy1_15", 0, 21, 2, 600, BurTalk);
                break;

            case 650:
                // Reaktion Bauer
                personSays("Ralbicy1_16", 0, 21, 2, 600, BurTalk);
                break;

            case 660:
                // Reaktion Bauer
                personSays("Ralbicy1_17", 0, 21, 2, 600, BurTalk);
                break;

            case 670:
                // Reaktion Bauer
                personSays("Ralbicy1_18", 0, 21, 2, 671, BurTalk);
                break;

            case 671:
                // Reaktion Bauer
                personSays("Ralbicy1_19", 0, 21, 2, 600, BurTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[25] = false; // wieder nico dale beim 1. MC
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                isListening = false; // Bauer hoert wieder auf zuzuhoeren
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}