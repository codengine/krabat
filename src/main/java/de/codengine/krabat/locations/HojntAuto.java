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
import de.codengine.krabat.anims.Hunter;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class HojntAuto extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(HojntAuto.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage skyl;
    private GenericImage skyr; /*hojnt2, */
    private GenericImage hojnt3; /* hojnt4, seil, baum, */
    private GenericImage hoelzerback;
    private GenericImage offeneTuer;

    private final GenericImage[] hoelzer;
    private int HolzCount = 0;
    private boolean klingeln = false;
    private int Mehrmals;
    private static final int MAX_MEHRMALS = 3;

    private Hunter jaeger;
    private boolean showHojnt = false;
    private boolean walkReady = true;

    private boolean isDoorOpen = false;

    private static final int SCROLLWERT = 417;

    private int scrollPosition = 0;
    private boolean setScroll = false;

    private boolean isScrollAnim = true;

    // Konstante Punkte
    private static final GenericPoint Pleft = new GenericPoint(279, 358);
    private static final GenericPoint Pmitte = new GenericPoint(622, 376);
    private static final GenericPoint Pright = new GenericPoint(1076, 448);
    private static final GenericPoint Pin = new GenericPoint(286, 344);

    private int Counter = 0;

    public HojntAuto(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 420;
        mainFrame.krabat.zoomFactor = 4.4f;
        mainFrame.krabat.defaultScale = 50;

        jaeger = new Hunter(mainFrame);
        jaeger.maxX = 0;
        jaeger.zoomFactor = mainFrame.krabat.zoomFactor;
        jaeger.defaultScale = 0;

        hoelzer = new GenericImage[11];
        Mehrmals = MAX_MEHRMALS;

        initImages();
        cursorShape = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        // Unterscheidung nach DoActions, was zu tun ist

        // An Hoelzern gezogen Hinlauf
        if (!mainFrame.actions[216] && !mainFrame.actions[217] && !mainFrame.actions[218]) {
            log.debug("An den Hoelzern gezogen - Hinlauf !");
            jaeger.setPos(Pin);
            jaeger.setFacing(3);
            scrollPosition = 0;
            setScroll = true;
            nextActionID = 150;
        }

        // An Strick gezogen Hinlauf
        if (!mainFrame.actions[216] && mainFrame.actions[217] && !mainFrame.actions[218]) {
            log.debug("Am Strick gezogen Hinlauf !");
            jaeger.setPos(Pin);
            jaeger.setFacing(3);
            scrollPosition = 0;
            setScroll = true;
            nextActionID = 100;
        }

        // Ruecklauf
        if (mainFrame.actions[216] && !mainFrame.actions[218]) {
            log.debug("Ruecklauf aus beiden Szenen !");
            isDoorOpen = true;
            jaeger.setPos(Pright);
            jaeger.setFacing(9);
            showHojnt = true;
            scrollPosition = SCROLLWERT;
            setScroll = true;
            nextActionID = 500;
        }

        // Szene, wenn Krabat in die Grube faellt
        if (mainFrame.actions[218]) {
            log.debug("Szene : K in Grube.");
            jaeger.setPos(Pin);
            jaeger.setFacing(3);
            scrollPosition = 210;
            isScrollAnim = false;
            setScroll = true;
            nextActionID = 600;
        }

        initLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(229, 265, 267, 340, 350, 364));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(285, 606, 361, 642, 365, 383));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(605, 663, 715, 927, 384, 410));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(716, 996, 857, 1138, 411, 441));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(985, 1279, 1024, 1279, 442, 463));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
    }

    // Bilder vorbereiten
    private void initImages() {
        backl = getPicture("gfx/hojnt/hojnt-l.png");
        backr = getPicture("gfx/hojnt/hojnt-r.png");
        skyl = getPicture("gfx/hojnt/hsky-l.png");
        skyr = getPicture("gfx/hojnt/hsky-r.png");
        hojnt3 = getPicture("gfx/hojnt/hojnt3.png");

        offeneTuer = getPicture("gfx/hojnt/jdurje.png");

        hoelzer[0] = getPicture("gfx/hojnt/k0.png");
        hoelzer[1] = getPicture("gfx/hojnt/k1.png");
        hoelzer[2] = getPicture("gfx/hojnt/k2.png");
        hoelzer[3] = getPicture("gfx/hojnt/k3.png");
        hoelzer[4] = getPicture("gfx/hojnt/k4.png");
        hoelzer[5] = getPicture("gfx/hojnt/k5.png");
        hoelzer[6] = getPicture("gfx/hojnt/k6.png");
        hoelzer[7] = getPicture("gfx/hojnt/k7.png");
        hoelzer[8] = getPicture("gfx/hojnt/k8.png");
        hoelzer[9] = getPicture("gfx/hojnt/k9.png");
        hoelzer[10] = getPicture("gfx/hojnt/k10.png");

        hoelzerback = getPicture("gfx/hojnt/kb.png");

    }

    @Override
    public void cleanup() {
        backl = null;
        backr = null;
        skyl = null;
        skyr = null;
        hojnt3 = null;

        offeneTuer = null;

        hoelzer[0] = null;
        hoelzer[1] = null;
        hoelzer[2] = null;
        hoelzer[3] = null;
        hoelzer[4] = null;
        hoelzer[5] = null;
        hoelzer[6] = null;
        hoelzer[7] = null;
        hoelzer[8] = null;
        hoelzer[9] = null;
        hoelzer[10] = null;

        hoelzerback = null;

        jaeger.cleanup();
        jaeger = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            mainFrame.scrollY = 0;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollPosition;
            }
            cursorShape = 200;
            mainFrame.isAnimRunning = true;
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund zeichnen
        g.drawImage(skyl, mainFrame.scrollX / 10, 0);
        g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Parallaxer ausfuehren
        int xtemp = mainFrame.scrollX - 5;
        if (xtemp < 0) {
            xtemp = 0;
        }
        g.setClip(xtemp, 0, 650, 325);
        g.drawImage(skyl, mainFrame.scrollX / 10, 0);
        g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Jaeger Hintergrund loeschen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = jaeger.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(skyl, mainFrame.scrollX / 10, 0);
            g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // wenn noetig, dann offene Tuer zeichnen
        if (isDoorOpen) {
            g.setClip(272, 265, 81, 80);
            g.drawImage(offeneTuer, 272, 265);
        }

        // leeren Haken zeichnen, wenn noetig
        if (mainFrame.actions[905]) {
            g.setClip(426, 258, 20, 19);
            g.drawImage(hojnt3, 426, 258);
        }

        // Klingelanim der Hoelzer wenn noetig
        if (klingeln) {
            HolzCount++;
            if (HolzCount == 11) {
                HolzCount = 1;
                Mehrmals--;
                if (Mehrmals < 1) {
                    klingeln = false;
                    HolzCount = 0;
                    Mehrmals = MAX_MEHRMALS;
                }
            }

            g.setClip(608, 239, 25, 48);
            g.drawImage(hoelzerback, 613, 263);
            g.drawImage(hoelzer[HolzCount], 608, 239);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Jaeger bewegen
        if (showHojnt && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = jaeger.move();
        }

        // Jaeger zeichnen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = jaeger.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne sie jetzt

            // Redet sie etwa gerade ??
            if (mainFrame.talkCount > 0) {
                jaeger.talkHojnt(g);
            }

            // nur rumstehen oder laufen
            else {
                jaeger.drawHojnt(g);
            }
        }

        // Scroller mitschieben, wenn Hojnt bewegt wird
        // Hier allgemeine Scrollingroutine, nur wenn man darf...
        if (isScrollAnim) {
            BorderRect temprect = jaeger.getBoundingBox();
            int temp = (temprect.topLeftPoint.x + temprect.bottomRightPoint.x) / 2;
            temp -= mainFrame.scrollX;

            if (temp < 315) {
                mainFrame.scrollX -= 6;
            }
            if (temp > 325) {
                mainFrame.scrollX += 6;
            }
            if (mainFrame.scrollX < 0) {
                mainFrame.scrollX = 0;
            }
            if (mainFrame.scrollX > SCROLLWERT) {
                mainFrame.scrollX = SCROLLWERT;
            }
        }

        // Cursor neu berechnen, weil Hintergrund verschoben wurde
        evalMouseMoveEvent(mainFrame.mousePoint);

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

        // Gibt es was zu tun ? Achtung! Scrolling - Verriegelung in DoActions extra !!!
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Auszugebenden Text abbrechen
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            talkPerson = 0;
        }
        outputText = "";
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        if (cursorShape != 20) {
            cursorShape = 20;
            mainFrame.setCursor(mainFrame.cursorNone);
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 100:
                // Klingelanim der Hoelzer
                klingeln = true;
                mainFrame.soundPlayer.playFile("sfx/klingel.wav");
                nextActionID = 110;
                break;

            case 110:
                // Warten auf Ende Klingeln
                if (!klingeln) {
                    nextActionID = 150;
                }
                break;

            case 150:
                // Tuer oeffnen, Jaeger zeigen und vor die Tuer laufen lassen
                showHojnt = true;
                isDoorOpen = true;
                mainFrame.actions[231] = true;
                jaeger.moveTo(Pleft);
                walkReady = false;
                nextActionID = 155;
                break;

            case 155:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 159;
                }
                break;

            case 159:
                // Jaeger sagt Spruch vor dem Loslaufen
                outputText = mainFrame.imageFont.splitTextKey("HojntAuto_1");
                // Hier Position des Textes berechnen
                BorderRect tmp = jaeger.getBoundingBox();
                GenericPoint tTlk = new GenericPoint((tmp.bottomRightPoint.x + tmp.topLeftPoint.x) / 2, tmp.topLeftPoint.y - 50);
                outputTextPos = mainFrame.imageFont.centerText(outputText, tTlk);
                talkPerson = 26;
                nextActionID = 160;
                break;

            case 160:
                // Laufe nach rechts
                jaeger.resetAnimPos = false;
                jaeger.moveTo(Pmitte);
                walkReady = false;
                nextActionID = 170;
                break;

            case 170:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 173;
                }
                break;

            case 173:
                // Laufe nach rechts
                jaeger.resetAnimPos = true;
                jaeger.moveTo(Pright);
                walkReady = false;
                nextActionID = 176;
                break;

            case 176:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 180;
                }
                break;

            case 180:
                // Wieder auf normales Hojnt zurueckschalten
                createNewLocation(14, 29);
                break;

            case 500:
                // Jaeger laeuft wieder nach links zurueck
                jaeger.resetAnimPos = false;
                jaeger.moveTo(Pmitte);
                walkReady = false;
                nextActionID = 510;
                break;

            case 510:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 513;
                }
                break;

            case 513:
                // Jaeger laeuft wieder nach links zurueck
                jaeger.resetAnimPos = true;
                jaeger.moveTo(Pleft);
                walkReady = false;
                nextActionID = 516;
                break;

            case 516:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 520;
                }
                break;

            case 520:
                // Jaeger sagt Spruch vor dem Ende
                outputText = mainFrame.imageFont.splitTextKey("HojntAuto_2");
                // Hier Position des Textes berechnen
                BorderRect temp = jaeger.getBoundingBox();
                GenericPoint tTalk = new GenericPoint((temp.bottomRightPoint.x + temp.topLeftPoint.x) / 2, temp.topLeftPoint.y - 50);
                outputTextPos = mainFrame.imageFont.centerText(outputText, tTalk);
                talkPerson = 26;
                nextActionID = 530;
                break;

            case 530:
                // Jaeger reingehen lassen
                jaeger.moveTo(Pin);
                walkReady = false;
                nextActionID = 533;
                break;

            case 533:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 536;
                }
                break;

            case 536:
                // jaeger ist drin
                isDoorOpen = false;
                showHojnt = false;
                nextActionID = 540;
                break;

            case 540:
                // wieder auf NormalHojnt zurueckschalten
                createNewLocation(14, 29);
                break;

            case 600:
                // bisschen warten wg. Sound-Abfolge
                Counter = 15;
                nextActionID = 605;
                break;

            case 605:
                // Klingelanim der Hoelzer
                if (--Counter > 0) {
                    break;
                }
                klingeln = true;
                mainFrame.soundPlayer.playFile("sfx/klingel.wav");
                nextActionID = 610;
                break;

            case 610:
                // Warten auf Ende Klingeln
                if (!klingeln) {
                    nextActionID = 620;
                }
                break;

            case 620:
                // Tuer oeffnen, Jaeger zeigen und vor die Tuer laufen lassen
                showHojnt = true;
                isDoorOpen = true;
                jaeger.moveTo(Pleft);
                walkReady = false;
                nextActionID = 625;
                break;

            case 625:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 650;
                }
                break;

            case 650:
                // Jaeger sagt Spruch, wenn Krabat reingefallen ist
                outputText = mainFrame.imageFont.splitTextKey("HojntAuto_3");
                // Hier Position des Textes berechnen
                BorderRect teemp = jaeger.getBoundingBox();
                GenericPoint tTaalk = new GenericPoint((teemp.bottomRightPoint.x + teemp.topLeftPoint.x) / 2, teemp.topLeftPoint.y - 50);
                outputTextPos = mainFrame.imageFont.centerText(outputText, tTaalk);
                talkPerson = 26;
                nextActionID = 655;
                break;

            case 655:
                // 1 Repaint einfuegen
                nextActionID = 660;
                break;

            case 660:
                // auf Jama umschalten
                createNewLocation(27, 29);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}