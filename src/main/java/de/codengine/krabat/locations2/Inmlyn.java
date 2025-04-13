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

package de.codengine.krabat.locations2;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.RapakiRaven;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Inmlyn extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Inmlyn.class);
    private GenericImage background1;
    private GenericImage background2;
    private GenericImage fenster;
    private GenericImage rabeVorder;
    private final GenericImage[] krabatKopf;

    private boolean setScroll = false;
    private int scrollwert;
    private boolean setAnim = false;
    private boolean showRapak = false;
    private RapakiRaven rabe;

    private boolean krabatVisible = true;

    private int Zwinker = 0;
    private int scrollCounter;

    private GenericPoint tempPoint;

    private int Counter;

    public Inmlyn(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 4.93f;
        mainFrame.krabat.defaultScale = -120;

        krabatKopf = new GenericImage[2];

        initLocation();
        initImages();
        cursorShape = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird
        switch (oldLocation) {
            case 25: // 1. Mal aus Mlyn
                mainFrame.krabat.setPos(new GenericPoint(0, 0));
                mainFrame.krabat.setFacing(12);
                scrollwert = 0;
                setScroll = true;
                break;

            case 28: // von Dzera aus
                mainFrame.krabat.setPos(new GenericPoint(833, 429));
                mainFrame.krabat.setFacing(12);
                scrollwert = 513;
                setScroll = true;
                break;
        }

        setAnim = true;

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation() {
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(423, 450, 403, 450, 407, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(451, 446, 613, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(614, 470, 817, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(818, 423, 856, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(857, 466, 1199, 479));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(5);

        // Wege eintragen
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
    }

    // Bilder vorbereiten
    private void initImages() {
        background1 = getPicture("gfx/mlyn/mlynn-l.png");
        background2 = getPicture("gfx/mlyn/mlynn-r.png");
        fenster = getPicture("gfx/mlyn/wokno.png");

        krabatKopf[0] = getPicture("gfx/mlyn/k-u-wokno1.png");
        krabatKopf[1] = getPicture("gfx/mlyn/k-u-wokno1a.png");

        rabeVorder = getPicture("gfx/mlyn/mwokno.png");

    }

    @Override
    public void cleanup() {
        background1 = null;
        background2 = null;
        fenster = null;

        krabatKopf[0] = null;
        krabatKopf[1] = null;

        rabeVorder = null;

        if (rabe != null) {
            rabe.cleanup();
        }
        rabe = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // System.out.print("g");

        // Clipping - Region initialisieren und Rauchthread aktivieren
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
            mainFrame.isAnimRunning = true;
        }

        // Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
        if (mainFrame.actions[310]) {
            g.drawImage(background1, 0, 0);
            g.drawImage(background2, 640, 0);
        } else {
            g.drawImage(fenster, 0, 0);

            // hier Krabatkopf draufzeichnen
            if (Zwinker == 1) {
                Zwinker = 0;
            } else {
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Zwinker = 1;
                }
            }

            g.drawImage(krabatKopf[Zwinker], 55, 53);
        }

        // hier am Anfang das Buecken einschalten, wenn aus Dzera zurueckkommend
        if (setAnim && mainFrame.actions[310]) {
            mainFrame.krabat.nAnimation = 122;
        }

        // Hier Raben zeichnen, solange noetig
        if (showRapak) {
            g.setClip(0, 200, 500, 280);
            g.drawImage(background1, 0, 0);
            showRapak = rabe.doFly(g);
            g.drawImage(rabeVorder, 0, 242);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Krabats neue Position festlegen wenn noetig
        mainFrame.pathWalker.doWalk();

        // Krabat zeichnen

        // Animation??
        if (mainFrame.actions[310] && krabatVisible) {
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

        // Textausgabe, falls noetig
        if (!Objects.equals(outputText, "")) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 964);
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

        // Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!

        if (setAnim) {
            setAnim = false;
            if (!mainFrame.actions[310]) {
                nextActionID = 100;
            } else {
                nextActionID = 1000;
            }
        }

        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Auszugebenden Text abbrechen
        outputText = "";
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            talkPerson = 0;
        }

    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning) {
            if (cursorShape != 20) {
                cursorShape = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
        }

    }

    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // System.out.println ("NextActionID = " + nextActionID);

        // Was soll Krabat machen ?
        switch (nextActionID) {

            case 100:
                // Textausgabe auf Bild Wokno
                mainFrame.soundPlayer.playFile("sound.wav");
                personSays("Inmlyn_2", 0, 54, 2, 110, new GenericPoint(320, 400));
                break;

            case 110:
                // Umschalten
                mainFrame.actions[310] = true;
                mainFrame.krabat.setPos(new GenericPoint(437, 412));
                mainFrame.krabat.setFacing(12);
                scrollwert = 117;
                setScroll = true;
                mainFrame.isClipSet = false;
                nextActionID = 120;
                break;

            case 120:
                // Rumlaufen
                talkPause = 20;
                mainFrame.pathWalker.setNewWay(new GenericPoint(833, 429));
                nextActionID = 130;
                break;

            case 130:
                // Auf Ende warten
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.krabat.setFacing(12);
                nextActionID = 140;
                break;

            case 140:
                // Naechsten Text einblenden
                personSays("Inmlyn_3", 0, 54, 2, 150, new GenericPoint(320, 200));
                break;

            case 150:
                // krabat bueckt sich kurz vor der Umblende
                mainFrame.krabat.nAnimation = 122;
                Counter = 5;
                nextActionID = 160;
                break;

            case 160:
                // Umschalten auf Dzera
                if (--Counter > 1) {
                    break;
                }
                mainFrame.krabat.stopAnim();
                createNewLocation(28, 91);
                break;

            //------------------------Hier der Einsprung fuer zurueck---------------------------

            case 1000:
                // Krabat spricht
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                krabatSays("Inmlyn_1", 0, 3, 2, 1020);
                break;

            case 1020:
                // Sound playen und 2 Sek. warten...
                mainFrame.soundPlayer.playFile("sfx/rapak1.wav");
                Counter = 20;
                nextActionID = 1030;
                break;

            case 1030:
                // Raben schon zeigen, bevor Scroller umgeschaltet wird
                if (--Counter > 1) {
                    break;
                }
                rabe = new RapakiRaven(mainFrame, 300, 270, -50);
                showRapak = true;
                nextActionID = 1040;
                break;

            case 1040:
                // Raben zeigen und fliegen lassen
                krabatVisible = false;
                tempPoint = mainFrame.krabat.getPos();
                mainFrame.krabat.setPos(new GenericPoint(0, 0));
                scrollCounter = 40;
                mainFrame.scrollX = 0;
                nextActionID = 1045;
                break;

            case 1045:
                // noch bestimmte Zeit den Scroller anhalten, dann normal weiter
                if (--scrollCounter > 1) {
                    break;
                }
                mainFrame.krabat.setPos(tempPoint);
                mainFrame.krabat.setFacing(9);  // in Richtung des Raben schauen lassen
                krabatVisible = true;
                nextActionID = 1050;
                break;

            case 1050:
                // Abwarten, bis Scroller zurueck
                if (showRapak || mainFrame.isScrolling) {
                    break;
                }
                nextActionID = 1060;
                break;

            case 1060:
                // ErzaehlerText
                personSays("Inmlyn_4", 0, 54, 2, 1070, new GenericPoint(320, 200));
                break;

            case 1070:
                // Umschalten auf Mlyn 2. Teil
                createNewLocation(90, 91);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}