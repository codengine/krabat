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
import de.codengine.krabat.anims.FarmerHanza;
import de.codengine.krabat.anims.FarmerMichal;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Polo1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Polo1.class);
    private GenericImage background;
    private GenericImage polo2;
    private GenericImage polo3;
    private GenericImage polo4;
    private GenericImage polo5;

    private FarmerMichal michael;
    private FarmerHanza agnes;
    private final MultipleChoice Dialog;

    private BorderRect hanzaRect;
    private BorderRect michalRect;
    private GenericPoint hanzaTalk;
    private GenericPoint michalTalk;

    private boolean hoertkrabatzu = false;

    private boolean stein1verdeckt = false;
    private boolean stein2verdeckt = false;
    private boolean stein3verdeckt = false;


    // Konstanten - Rects
    private static final BorderRect linkerAusgang = new BorderRect(0, 351, 40, 479);
    private static final BorderRect rechterAusgang = new BorderRect(595, 245, 639, 350);
    private static final BorderRect polo2Rect = new BorderRect(492, 244, 639, 470);
    private static final BorderRect kamuskiRect = new BorderRect(192, 340, 263, 377);

    // Konstante Punkte
    private static final GenericPoint michalPoint = new GenericPoint(375, 211);
    private static final GenericPoint hanzaPoint = new GenericPoint(356, 217);
    private static final GenericPoint Pburja = new GenericPoint(445, 323);
    private static final GenericPoint Pright = new GenericPoint(639, 282);
    private static final GenericPoint Pleft = new GenericPoint(0, 414);
    private static final GenericPoint Pkamuski = new GenericPoint(275, 356);
    private static final GenericPoint pStein1 = new GenericPoint(194, 374);
    private static final GenericPoint pStein2 = new GenericPoint(215, 374);
    private static final GenericPoint pStein3 = new GenericPoint(225, 364);

    private static final int fKamuski = 9;
    private static final int fBurja = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Polo1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 9.8f;
        mainFrame.krabat.defaultScale = 0;

        agnes = new FarmerHanza(mainFrame);
        michael = new FarmerMichal(mainFrame);
        Dialog = new MultipleChoice(mainFrame);

        evalPersons();

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Personen berechnen
    private void evalPersons() {
        michalRect = new BorderRect(michalPoint.x, michalPoint.y, michalPoint.x + FarmerMichal.Breite, michalPoint.y + FarmerMichal.Hoehe);

        michalTalk = new GenericPoint(michalPoint.x + FarmerMichal.Breite / 2, michalPoint.y - 50);

        hanzaRect = new BorderRect(hanzaPoint.x, hanzaPoint.y, hanzaPoint.x + FarmerHanza.Breite, hanzaPoint.y + FarmerHanza.Hoehe);

        hanzaTalk = new GenericPoint(hanzaPoint.x + FarmerHanza.Breite / 2, hanzaPoint.y - 50);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 100, 0, 30, 397, 427));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(172, 360, 20, 100, 353, 396));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(592, 639, 201, 360, 299, 352));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(3);

        // Wege eintragen
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 21:
                // von Kulow aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(536, 314));
                mainFrame.krabat.setFacing(9);
                break;
            case 10:
                // von Weiden aus
                mainFrame.krabat.setPos(new GenericPoint(34, 413));
                mainFrame.krabat.setFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/polo/polo.png");
        polo2 = getPicture("gfx/polo/polo2.png");
        polo3 = getPicture("gfx/polo/polo3.png");
        polo4 = getPicture("gfx/polo/polo4.png");
        polo5 = getPicture("gfx/polo/polo5.png");

    }

    @Override
    public void cleanup() {
        background = null;
        polo2 = null;
        polo3 = null;
        polo4 = null;
        polo5 = null;

        michael.cleanup();
        michael = null;
        agnes.cleanup();
        agnes = null;
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

        // Wenn die Steine weg, dann anderes GenericImage drueber
        if (mainFrame.actions[912]) {
            g.setClip(180, 339, 102, 35);
            g.drawImage(polo3, 180, 339);
            g.drawImage(polo4, 211, 339);
            g.drawImage(polo5, 231, 339);
        } else // teilweise Auflesen ermoeglichen
        {
            g.setClip(180, 339, 102, 35);
            if (stein1verdeckt) {
                g.drawImage(polo3, 180, 339);
            }
            if (stein2verdeckt) {
                g.drawImage(polo4, 211, 339);
            }
            if (stein3verdeckt) {
                g.drawImage(polo5, 231, 339);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Andere Personen zeichnen (zuerst Background loeschen)
        g.setClip(michalPoint.x, michalPoint.y, FarmerMichal.Breite, FarmerMichal.Hoehe);
        g.drawImage(background, 0, 0);
        g.setClip(hanzaPoint.x, hanzaPoint.y, FarmerHanza.Breite, FarmerHanza.Hoehe);
        g.drawImage(background, 0, 0);

        // Michal
        g.setClip(michalPoint.x, michalPoint.y, FarmerMichal.Breite, FarmerMichal.Hoehe);
        michael.drawMichal(g, talkPerson, michalPoint, hoertkrabatzu);

        // Hanza
        g.setClip(hanzaPoint.x, hanzaPoint.y, FarmerHanza.Breite, FarmerHanza.Hoehe);
        agnes.drawHanza(g, talkPerson, hanzaPoint);

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

        // hinterm polo2 (nur Clipping - Region wird neugezeichnet)
        if (polo2Rect.isPointInRect(pKrTemp)) {
            g.drawImage(polo2, 510, 240); //orig 510, 336
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

                // Ausreden fuer Hanza
                if (hanzaRect.isPointInRect(pTemp) && !michalRect.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 200;
                            break;
                        case 18: // bron
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = Pburja;
                }

                // Ausreden fuer Michal
                if (michalRect.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 205;
                            break;
                        case 18: // bron
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Pburja;
                }

                // Ausreden fuer Kamuski
                if (kamuskiRect.isPointInRect(pTemp) && !mainFrame.actions[912]) {
                    // rohodz
                    nextActionID = mainFrame.whatItem == 17 ? 220 : 160;
                    pTemp = Pkamuski;
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

                // Kamuski ansehen
                if (kamuskiRect.isPointInRect(pTemp) && !mainFrame.actions[912]) {
                    nextActionID = 3;
                    pTemp = Pkamuski;
                }

                // zu Kulow gehen
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

                // zu Weiden gehen
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

                // Hanza ansehen
                if (hanzaRect.isPointInRect(pTemp) || michalRect.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pburja;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Kulow anschauen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Weiden anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Mit den Bauern reden
                if (michalRect.isPointInRect(pTemp) || hanzaRect.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Pburja);
                    mainFrame.repaint();
                    return;
                }

                // Kamuski nehmen
                if (kamuskiRect.isPointInRect(pTemp) && !mainFrame.actions[912]) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(Pkamuski);
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) || hanzaRect.isPointInRect(pTemp) ||
                    michalRect.isPointInRect(pTemp) || kamuskiRect.isPointInRect(pTemp) &&
                    !mainFrame.actions[912];

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
            if (hanzaRect.isPointInRect(pTemp) ||
                    michalRect.isPointInRect(pTemp) ||
                    kamuskiRect.isPointInRect(pTemp) && !mainFrame.actions[912]) {
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
                // Burja anschauen
                krabatSays("Polo1_1", fBurja, 3, 0, 0);
                break;

            case 3:
                // Kamuski anschauen
                krabatSays("Polo1_2", fKamuski, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Bauern benutzen)
                hoertkrabatzu = true;
                mainFrame.krabat.setFacing(12);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 55:
                // Kamuski mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                krabatSays("Polo1_3", fKamuski, 3, 2, 60);
                break;

            case 60:
                // Kamuski benutzen
                mainFrame.krabat.setFacing(9);
                mainFrame.inventory.vInventory.addElement(12);
                mainFrame.pathWalker.setNewWay(pStein1);
                nextActionID = 62;
                break;

            case 62:
                // ersten Stein aufheben
                mainFrame.krabat.setFacing(12);
                mainFrame.krabat.nAnimation = 122;
                nextActionID = 64;
                break;

            case 64:
                // zu zweitem Stein laufen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                stein1verdeckt = true;
                mainFrame.pathWalker.setNewWay(pStein2);
                nextActionID = 66;
                break;

            case 66:
                // zweiten Stein aufheben
                mainFrame.krabat.setFacing(12);
                mainFrame.krabat.nAnimation = 122;
                nextActionID = 68;
                break;

            case 68:
                // zu drittem Stein laufen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                stein2verdeckt = true;
                mainFrame.pathWalker.setNewWay(pStein3);
                nextActionID = 70;
                break;

            case 70:
                // dritten Stein aufheben
                mainFrame.krabat.setFacing(12);
                mainFrame.krabat.nAnimation = 122;
                nextActionID = 72;
                break;

            case 72:
                // alles zu Ende
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                stein3verdeckt = true;
                mainFrame.actions[912] = true;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // gehe zu Weiden
                createNewLocation(10, 11);
                break;

            case 101:
                // Goto Kulow
                createNewLocation(21, 11);
                break;

            case 150:
                // Hanza - Ausreden
                femaleExcuse(fBurja);
                break;

            case 155:
                // Michal - Ausreden
                maleExcuse(fBurja);
                break;

            case 160:
                // kamuski - Ausreden
                thingExcuse(fKamuski);
                break;

            case 200:
                // kij auf hanza
                krabatSays("Polo1_4", fBurja, 3, 0, 0);
                break;

            case 205:
                // kij auf michal
                krabatSays("Polo1_5", fBurja, 3, 0, 0);
                break;

            case 210:
                // bron auf hanza
                krabatSays("Polo1_6", fBurja, 3, 0, 0);
                break;

            case 215:
                // bron auf michal
                krabatSays("Polo1_7", fBurja, 3, 0, 0);
                break;

            case 220:
                // rohodz auf kamuski
                krabatSays("Polo1_8", fKamuski, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("Polo1_29", 1000, 100, new int[]{100}, 610);
                Dialog.extend("Polo1_30", 100, 101, new int[]{101}, 620);
                Dialog.extend("Polo1_31", 101, 102, new int[]{102, 106, 109}, 630);

                // 2. Frage
                Dialog.extend("Polo1_32", 1000, 103, new int[]{103}, 640);
                Dialog.extend("Polo1_33", 103, 104, new int[]{104}, 650);
                Dialog.extend("Polo1_34", 104, 1000, null, 660);

                // 4. Frage
                Dialog.extend("Polo1_35", 106, 107, new int[]{107}, 670);
                Dialog.extend("Polo1_36", 107, 1000, null, 680);

                // 5. Frage
                Dialog.extend("Polo1_37", 109, 110, new int[]{110}, 690);

                // 3. Frage
                Dialog.extend("Polo1_38", 1000, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                break;

            case 610:
                // Reaktion Michal auf 1. Teil 1. Frage
                personSays("Polo1_9", 0, 28, 2, 600, michalTalk);
                break;

            case 620:
                // Reaktion Michal auf 2. Teil 1. Frage
                personSays("Polo1_10", 0, 28, 2, 621, michalTalk);
                break;

            case 621:
                // Reaktion Michal auf 2. Teil 1. Frage
                personSays("Polo1_11", 0, 28, 2, 622, michalTalk);
                break;

            case 622:
                // Reaktion Michal auf 2. Teil 1. Frage
                personSays("Polo1_12", 0, 28, 0, 623, michalTalk);
                break;

            case 623:
                // Reaktion Hanza auf 2. Teil 1. Frage
                personSays("Polo1_13", 0, 29, 0, 624, hanzaTalk);
                break;

            case 624:
                // Reaktion Michal auf 2. Teil 1. Frage
                personSays("Polo1_14", 0, 28, 2, 625, michalTalk);
                break;

            case 625:
                // Reaktion Michal auf 2. Teil 1. Frage
                personSays("Polo1_15", 0, 28, 2, 600, michalTalk);
                break;

            case 630:
                // Reaktion Michal auf 3. Teil 1. Frage
                personSays("Polo1_16", 0, 28, 2, 631, michalTalk);
                break;

            case 631:
                // Reaktion Michal auf 3. Teil 1. Frage
                personSays("Polo1_17", 0, 28, 2, 600, michalTalk);
                break;

            case 640:
                // Reaktion Michal auf 1. Teil 2. Frage
                personSays("Polo1_18", 0, 28, 2, 600, michalTalk);
                break;

            case 650:
                // Reaktion Michal auf 2. Teil 2. Frage
                personSays("Polo1_19", 0, 28, 2, 600, michalTalk);
                break;

            case 660:
                // Reaktion Michal auf 3. Teil 2. Frage
                personSays("Polo1_20", 0, 28, 2, 661, michalTalk);
                break;

            case 661:
                // Reaktion Michal auf 3. Teil 2. Frage
                personSays("Polo1_21", 0, 28, 2, 600, michalTalk);
                break;

            case 670:
                // Reaktion Michal auf 1. Teil 4. Frage
                personSays("Polo1_22", 0, 28, 2, 671, michalTalk);
                break;

            case 671:
                // Reaktion Michal auf 1. Teil 4. Frage
                personSays("Polo1_23", 0, 28, 2, 672, michalTalk);
                break;

            case 672:
                // Reaktion Michal auf 1. Teil 4. Frage
                personSays("Polo1_24", 0, 28, 2, 600, michalTalk);
                break;

            case 680:
                // Reaktion Michal auf 2. Teil 4. Frage
                personSays("Polo1_25", 0, 28, 2, 600, michalTalk);
                break;

            case 690:
                // Reaktion Michal auf 1. Teil 5. Frage
                personSays("Polo1_26", 0, 28, 2, 691, michalTalk);
                break;

            case 691:
                // Reaktion Michal auf 1. Teil 5. Frage
                personSays("Polo1_27", 0, 28, 2, 692, michalTalk);
                break;

            case 692:
                // Reaktion Michal auf 1. Teil 5. Frage
                personSays("Polo1_28", 0, 28, 2, 600, michalTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                hoertkrabatzu = false;
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