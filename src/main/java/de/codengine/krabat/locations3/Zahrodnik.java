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
import de.codengine.krabat.anims.Handrij;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Zahrodnik extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Zahrodnik.class);
    private GenericImage background;
    private GenericImage hrod;
    private final Handrij handrij;
    private final MultipleChoice Dialog;

    private boolean handrijHoertZu = false;
    private boolean handrijSchreibt = false;
    private boolean handrijGibt = false;

    private final BorderRect reZahrodnik;
    private final GenericPoint talkPoint;

    private int tCounter = 0;

    // Konstanten - Rects
    private static final BorderRect ausgangUnten
            = new BorderRect(596, 377, 639, 479);
    private static final BorderRect skizze
            = new BorderRect(525, 162, 600, 224);
    private static final BorderRect papier
            = new BorderRect(347, 322, 390, 355);
    private static final BorderRect rectHrod
            = new BorderRect(0, 352, 280, 479);
    private static final BorderRect budka
            = new BorderRect(388, 168, 485, 325);
    private static final BorderRect pjerjo
            = new BorderRect(222, 208, 233, 227);
    private static final BorderRect dokumenty
            = new BorderRect(246, 229, 266, 246);
    private static final BorderRect lookHrod
            = new BorderRect(36, 369, 160, 479);

    // Konstante Points
    private static final GenericPoint pExitUnten = new GenericPoint(639, 431);
    private static final GenericPoint pZahrodnik = new GenericPoint(315, 352);
    private static final GenericPoint pZahrUnten = new GenericPoint(325, 375);
    private static final GenericPoint pSkizze = new GenericPoint(555, 324);
    private static final GenericPoint pPapier = new GenericPoint(333, 331);
    private static final GenericPoint pBudka = new GenericPoint(435, 316);
    private static final GenericPoint pPjerjo = new GenericPoint(283, 302);
    private static final GenericPoint pDokumenty = new GenericPoint(283, 302);
    private static final GenericPoint pHrod = new GenericPoint(158, 428);
    private static final GenericPoint handrijFeet = new GenericPoint(242, 339);

    // Konstante ints
    private static final int fDokumente = 9;
    private static final int fZahrod = 9;
    private static final int fZahrodLook = 12;
    private static final int fHrod = 6;
    private static final int fPapier = 3;
    private static final int fBudka = 12;
    private static final int fSkizze = 12;
    private static final int fFeder = 9;

    private boolean schnauzeZahrod = false;

    private int initCounter = 15;
    private boolean initSound = true;  // defaultmaessig nicht abspielen, nur wenn kein "load", dann aktivieren


    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zahrodnik(Start caller, int oldLocation) {
        super(caller, 162);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxX = 479;
        mainFrame.krabat.zoomFactor = 4.6f;
        mainFrame.krabat.defaultScale = -100;

        handrij = new Handrij(mainFrame);
        Dialog = new MultipleChoice(mainFrame);

        talkPoint = new GenericPoint(handrijFeet.x, handrijFeet.y - Handrij.Hoehe - 50);
        reZahrodnik = new BorderRect(handrijFeet.x - Handrij.Breite / 2, handrijFeet.y - Handrij.Hoehe,
                handrijFeet.x + Handrij.Breite / 2, handrijFeet.y);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(327, 371, 639, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(505, 333, 600, 370));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(80, 326, 20, 326, 375, 450));

        mainFrame.pathFinder.clearMatrix(3);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(0, 2);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 160:
                // von Panorama aus
                mainFrame.krabat.setPos(new GenericPoint(557, 431));
                mainFrame.krabat.setFacing(9);
                initSound = false;  // nur hier Sound abspielen
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/zahrod/zahrod.png");
        hrod = getPicture("gfx-dd/zahrod/hrod.png");
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Tuereintrittssound abspielen
        if (!initSound) {
            initSound = true;
            mainFrame.soundPlayer.playFile("sfx/vdurjezu.wav");
        }

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        if (mainFrame.isMultipleChoiceActive && mainFrame.isClipSet) {
            Dialog.paintMultiple(g);
            return;
        }

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

        // Handrij zeichnen
        g.setClip(reZahrodnik.topLeftPoint.x, reZahrodnik.topLeftPoint.y, Handrij.Breite, Handrij.Hoehe);
        g.drawImage(background, 0, 0);
        handrij.drawHandrij(g, talkPerson, handrijHoertZu, handrijSchreibt, handrijGibt);

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.doWalk();

        // Sound abspielen
        evalSound();

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

        // hinter Schloss ? (nur Clipping - Region wird neugezeichnet)
        if (rectHrod.isPointInRect(pKrTemp)) {
            g.drawImage(hrod, 0, 354);
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

                // Ausreden fuer Zahrodnik
                if (reZahrodnik.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pZahrUnten;
                }

                // Ausreden fuer hrod
                if (lookHrod.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTemp = pHrod;
                }

                // Ausreden fuer blatt
                if (papier.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = pPapier;
                }

                // Ausreden fuer budka
                if (budka.isPointInRect(pTemp) && !reZahrodnik.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 170;
                    pTemp = pBudka;
                }

                // Ausreden fuer skizze
                if (skizze.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 175;
                    pTemp = pSkizze;
                }

                // Ausreden fuer pjerjo
                if (pjerjo.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 180;
                    pTemp = pPjerjo;
                }

                // Ausreden fuer dokumenty
                if (dokumenty.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 185;
                    pTemp = pDokumenty;
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

                // zu Panorama gehen ?
                if (ausgangUnten.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen, Achtung: ist jetzt nach rechts!!!
                    if (!ausgangUnten.isPointInRect(kt)) {
                        pTemp = pExitUnten;
                    } else {
                        // es wird nach rechts! verlassen
                        pTemp = new GenericPoint(pExitUnten.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Kirche ansehen
                // if (brKirche.IsPointInRect (pTemp) == true)
                // {
                //   nextActionID = 1;
                //  pTemp = Pkirche;
                // }

                // Handrij ansehen
                if (reZahrodnik.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pZahrUnten;
                }

                // hrod ansehen
                if (lookHrod.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pHrod;
                }

                // blatt ansehen
                if (papier.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pPapier;
                }

                // budka ansehen
                if (budka.isPointInRect(pTemp) && !reZahrodnik.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pBudka;
                }

                // skizze ansehen
                if (skizze.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pSkizze;
                }

                // pjerjo ansehen
                if (pjerjo.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pPjerjo;
                }

                // dokumenty ansehen
                if (dokumenty.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pDokumenty;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Zahrodnik reden
                if (reZahrodnik.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pZahrUnten);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangUnten.isPointInRect(pTemp)) {
                    return;
                }

                // hrod mitnehmen
                if (lookHrod.isPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(pHrod);
                    mainFrame.repaint();
                    return;
                }

                // blatt mitnehmen
                if (papier.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(pPapier);
                    mainFrame.repaint();
                    return;
                }

                // budka ansehen
                if (budka.isPointInRect(pTemp) && !reZahrodnik.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(pBudka);
                    mainFrame.repaint();
                    return;
                }

                // skizze ansehen
                if (skizze.isPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.setNewWay(pSkizze);
                    mainFrame.repaint();
                    return;
                }

                // pjerjo ansehen
                if (pjerjo.isPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.pathWalker.setNewWay(pPjerjo);
                    mainFrame.repaint();
                    return;
                }

                // dokumenty ansehen
                if (dokumenty.isPointInRect(pTemp)) {
                    nextActionID = 80;
                    mainFrame.pathWalker.setNewWay(pDokumenty);
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) || skizze.isPointInRect(pTemp) ||
                    papier.isPointInRect(pTemp) || reZahrodnik.isPointInRect(pTemp) ||
                    budka.isPointInRect(pTemp) || pjerjo.isPointInRect(pTemp) ||
                    dokumenty.isPointInRect(pTemp) || lookHrod.isPointInRect(pTemp);

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
            if (ausgangUnten.isPointInRect(pTemp)) {
                if (cursorShape != 6) {
                    mainFrame.setCursor(mainFrame.cursorRight);  // Ausgang jetzt rechts!!!
                    cursorShape = 6;
                }
                return;
            }

            if (skizze.isPointInRect(pTemp) ||
                    papier.isPointInRect(pTemp) || reZahrodnik.isPointInRect(pTemp) ||
                    budka.isPointInRect(pTemp) || pjerjo.isPointInRect(pTemp) ||
                    dokumenty.isPointInRect(pTemp) || lookHrod.isPointInRect(pTemp)) {
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
                // Zahrodnik anschauen
                krabatSays("Zahrodnik_1", fZahrodLook, 3, 0, 0);
                break;

            case 2:
                // Hrod anschauen
                krabatSays("Zahrodnik_2", fHrod, 3, 0, 0);
                break;

            case 3:
                // Papier anschauen
                krabatSays("Zahrodnik_3", fPapier, 3, 0, 0);
                break;

            case 4:
                // budka anschauen
                krabatSays("Zahrodnik_4", fBudka, 3, 0, 0);
                break;

            case 5:
                // skizze anschauen
                krabatSays("Zahrodnik_5", fSkizze, 3, 0, 0);
                break;

            case 6:
                // feder anschauen
                krabatSays("Zahrodnik_6", fFeder, 3, 0, 0);
                break;

            case 7:
                // dokuments anschauen
                krabatSays("Zahrodnik_7", fDokumente, 3, 0, 0);
                break;

            case 50:
                // zuerstmal richtig zu Zahrodnik hinlaufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.setNewWayGuaranteed(pZahrodnik);
                nextActionID = 53;
                break;

            case 53:
                // Krabat beginnt MC (Zahrodnik benutzen)
                schnauzeZahrod = true;  // Sound schon hier abschalten
                mainFrame.krabat.setFacing(fZahrod);
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.actions[530]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 55:
                // Hrod mitnehmen
                krabatSays("Zahrodnik_8", fHrod, 3, 0, 0);
                break;

            case 60:
                // Papier mitnehmen
                krabatSays("Zahrodnik_9", fPapier, 3, 0, 0);
                break;

            case 65:
                // budka mitnehmen
                krabatSays("Zahrodnik_10", fBudka, 3, 0, 0);
                break;

            case 70:
                // skizze mitnehmen
                krabatSays("Zahrodnik_11", fSkizze, 3, 0, 0);
                break;

            case 75:
                // feder mitnehmen
                krabatSays("Zahrodnik_12", fFeder, 3, 0, 0);
                break;

            case 80:
                // dokuments mitnehmen
                krabatSays("Zahrodnik_13", fDokumente, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Panorama
                createNewLocation(160, locationID);
                break;

            case 155:
                // Zahrodnik - Ausreden
                maleExcuse(fZahrodLook);
                break;

            case 160:
                // hrod - Ausreden
                thingExcuse(fHrod);
                break;

            case 165:
                // papier - Ausreden
                thingExcuse(fPapier);
                break;

            case 170:
                // budka - Ausreden
                thingExcuse(fBudka);
                break;

            case 175:
                // skizze - Ausreden
                thingExcuse(fSkizze);
                break;

            case 180:
                // feder - Ausreden
                thingExcuse(fFeder);
                break;

            case 185:
                // dokumente - Ausreden
                thingExcuse(fDokumente);
                break;

            // Dialog mit Kuchar

            case 600:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                handrijHoertZu = true;
                // 1. Frage nur, wenn noch kein Liscik
                if (!mainFrame.actions[542]) {
                    Dialog.extend("Zahrodnik_34", 1000, 537, new int[]{537}, 610);
                    Dialog.extend("Zahrodnik_35", 537, 536, new int[]{536}, 611);
                    Dialog.extend("Zahrodnik_36", 536, 535, new int[]{535}, 612);
                    Dialog.extend("Zahrodnik_37", 535, 534, new int[]{534}, 613);
                    Dialog.extend("Zahrodnik_38", 534, 533, new int[]{533}, 614);
                    Dialog.extend("Zahrodnik_39", 533, 532, new int[]{532}, 615);
                    if (!mainFrame.actions[520]) {
                        Dialog.extend("Zahrodnik_40", 532, 1000, null, 618);
                    } else {
                        Dialog.extend("Zahrodnik_41", 532, 1000, null, 620);
                    }
                }

                // 2. Frage
                Dialog.extend("Zahrodnik_42", 1000, 541, new int[]{541}, 630);
                Dialog.extend("Zahrodnik_43", 541, 540, new int[]{540}, 631);
                Dialog.extend("Zahrodnik_44", 540, 1000, null, 632);

                // 3. Frage
                Dialog.extend("Zahrodnik_45", 1000, 1000, null, 800);

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

            // Sequenz vor MC (erstes Anreden)
            case 608:
                // Reaktion Krabat
                // hier erst Handrij aufmerksam machen
                handrijHoertZu = true;
                krabatSays("Zahrodnik_14", 0, 1, 2, 609);
                break;

            case 609:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_15", 0, 50, 2, 600, talkPoint);
                mainFrame.actions[530] = true; // Flag setzen
                break;

            // Antworten zu Frage 1 ////////////////////////////

            case 610:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_16", 0, 50, 2, 600, talkPoint);
                break;

            case 611:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_17", 0, 50, 2, 600, talkPoint);
                break;

            case 612:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_18", 0, 50, 2, 600, talkPoint);
                break;


            case 613:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_19", 0, 50, 2, 600, talkPoint);
                break;

            case 614:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_20", 0, 50, 2, 600, talkPoint);
                break;

            case 615:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_21", 0, 50, 2, 616, talkPoint);
                break;

            case 616:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_22", 0, 50, 2, 617, talkPoint);
                break;

            case 617:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_23", 0, 50, 2, 600, talkPoint);
                break;

            case 618:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_24", 0, 50, 2, 619, talkPoint);
                break;

            case 619:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_25", 0, 50, 2, 622, talkPoint);
                break;

            case 620:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_26", 0, 50, 2, 621, talkPoint);
                break;

            case 621:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_27", 0, 50, 2, 622, talkPoint);
                break;

            case 622:
                // Zahrodnik schreibt eine Weile
                tCounter = 58;
                handrijSchreibt = true;
                nextActionID = 623;
                break;

            case 623:
                // warten
                if (tCounter == 48) {
                    mainFrame.soundPlayer.playFile("sfx-dd/pisac.wav");
                }
                if (--tCounter > 1) {
                    break;
                }
                handrijSchreibt = false;
                handrijGibt = true;
                tCounter = 10;
                nextActionID = 626;
                break;

            case 626:
                // warten beim Geben, Krabat streckt auch bald Hand aus
                if (tCounter == 5) {
                    mainFrame.krabat.nAnimation = 93;
                }
                if (--tCounter > 1) {
                    break;
                }
                nextActionID = 628;
                handrijGibt = false;
                break;

            case 628:
                // End-Reaktion Krabat
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                krabatSays("Zahrodnik_28", 0, 1, 2, 800);
                // Empfehlungsschreiben zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(30);
                // Flag - setzen ! Krabat bekommt Empfehl.schreiben
                mainFrame.actions[542] = true;
                break;


            // Antworten zu Frage 2 ////////////////////////////
            case 630:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_29", 0, 50, 2, 600, talkPoint);
                break;

            case 631:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_30", 0, 50, 2, 600, talkPoint);
                break;

            case 632:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_31", 0, 50, 2, 633, talkPoint);
                break;

            case 633:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_32", 0, 50, 2, 634, talkPoint);
                break;

            case 634:
                // Reaktion Zahrodnik
                personSays("Zahrodnik_33", 0, 50, 2, 600, talkPoint);
                break;

            case 800:
                // wieder weg von Zahrodnik laufen
                mainFrame.pathWalker.setNewWay(pZahrUnten);
                nextActionID = 810;
                break;

            case 810:
                // MC beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                handrijHoertZu = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        // wenn anderes wichtiger ist, dann hier verhindern
        if (schnauzeZahrod) {
            return;
        }

        // zu Anfang noch nicht abspielen
        if (--initCounter > 0) {
            return;
        }

        int zf = (int) (Math.random() * 100);

        if (zf > 96) {
            int zwzf = (int) (Math.random() * 4.99);
            zwzf += 49;

            mainFrame.soundPlayer.playFile("sfx-dd/zahrod" + (char) zwzf + ".wav");
        }

    }

}