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
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Jama1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Jama1.class);
    private GenericImage background;
    private GenericImage foreground;
    private GenericImage vorder;
    private int Animcount = 1;
    private static final int MAXCOUNT = 5;
    private int Counter = 0;
    private final GenericImage[] Wuermer;

    private Hunter jaeger;
    private boolean showHojnt = false;
    private boolean walkReady = true;

    private static final GenericPoint AnfangsPunkt = new GenericPoint(50, 179);
    private static final GenericPoint EndPunkt = new GenericPoint(236, 179);

    // Konstanten - Rects
    private static final BorderRect wackiRect = new BorderRect(253, 350, 253 + 14, 350 + 13);

    // Konstante ints
    private static final int fWacki = 9;

    private int TakeCounter = 0;

    private boolean istJaegerGebueckt = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jama1(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxX = 492;
        mainFrame.krabat.zoomFactor = 2.1f;
        mainFrame.krabat.defaultScale = -90;

        jaeger = new Hunter(mainFrame);
        jaeger.maxX = 0;
        jaeger.zoomFactor = 1f;
        jaeger.defaultScale = -30;

        jaeger.setPos(AnfangsPunkt);
        jaeger.setFacing(3);

        Wuermer = new GenericImage[8];

        initLocation();
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation() {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(310, 390, 330, 396));

        mainFrame.pathFinder.clearMatrix(1);

        initImages();

        mainFrame.krabat.setPos(new GenericPoint(317, 393));
        mainFrame.krabat.setFacing(12);
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/jama/dzera2.png");
        foreground = getPicture("gfx/jama/black.png");

        Wuermer[1] = getPicture("gfx/jama/wr1.png");
        Wuermer[2] = getPicture("gfx/jama/wr2.png");
        Wuermer[3] = getPicture("gfx/jama/wr3.png");
        Wuermer[4] = getPicture("gfx/jama/wr4.png");
        Wuermer[5] = getPicture("gfx/jama/wr5.png");
        Wuermer[6] = getPicture("gfx/jama/wr6.png");
        Wuermer[7] = getPicture("gfx/jama/wr7.png");

        vorder = getPicture("gfx/jama/jtrawa.png");

    }

    @Override
    public void cleanup() {
        background = null;
        foreground = null;

        Wuermer[1] = null;
        Wuermer[2] = null;
        Wuermer[3] = null;
        Wuermer[4] = null;
        Wuermer[5] = null;
        Wuermer[6] = null;
        Wuermer[7] = null;

        vorder = null;

        jaeger.cleanup();
        jaeger = null;
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


        // Jaeger Hintergrund loeschen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = jaeger.getBoundingBox();

            if (!istJaegerGebueckt) {
                g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10,
                        temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                        temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);
            }

            // groesseres Cliprect weg wegen Buecken
            else {
                g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10,
                        temp.bottomRightPoint.x - temp.topLeftPoint.x + 50,
                        temp.bottomRightPoint.y - temp.topLeftPoint.y + 50);
            }

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
        }

        // Wacki zeichnen, solange noch da
        if (!mainFrame.actions[908]) {
            Counter--;
            if (Counter < 1) {
                Counter = MAXCOUNT;
                Animcount++;
                if (Animcount == 8) {
                    Animcount = 1;
                }
            }
            g.setClip(wackiRect.topLeftPoint.x, wackiRect.topLeftPoint.y, 15, 14);
            g.drawImage(background, 0, 0);
            g.drawImage(Wuermer[Animcount], wackiRect.topLeftPoint.x, wackiRect.topLeftPoint.y);
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

            // normales Cliprectloeschen
            if (!istJaegerGebueckt) {
                g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10,
                        temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                        temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);
            }

            // groesseres Cliprect weg wegen Buecken
            else {
                g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10,
                        temp.bottomRightPoint.x - temp.topLeftPoint.x + 50,
                        temp.bottomRightPoint.y - temp.topLeftPoint.y + 50);
            }

            // Zeichne sie jetzt

            // Redet sie etwa gerade ??
            if (talkPerson == 26 && mainFrame.talkCount > 0) {
                jaeger.talkHojnt(g);
            }

            // nur rumstehen oder laufen
            else {
                // normal zeichnen
                if (!istJaegerGebueckt) {
                    jaeger.drawHojnt(g);
                }

                // Bueckphase zeichnen (schaltet sich selbst ab) (ist hier wurst)
                else {
                    istJaegerGebueckt = jaeger.bueckeHojnt(g);
                }
            }

            g.drawImage(foreground, 0, 71);
            g.drawImage(vorder, 151, 158);
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

                // Ausreden fuer Wacki
                if (wackiRect.isPointInRect(pTemp) && !mainFrame.actions[908]) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
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

                // Wacki ansehen
                if (wackiRect.isPointInRect(pTemp) && !mainFrame.actions[908]) {
                    nextActionID = 1;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wacki mitnehmen ?
                if (wackiRect.isPointInRect(pTemp) &&
                        !mainFrame.actions[908]) {
                    nextActionID = 50;
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
                    wackiRect.isPointInRect(pTemp) && !mainFrame.actions[908];

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
            if (wackiRect.isPointInRect(pTemp) &&
                    !mainFrame.actions[908]) {
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
        BorderRect tmp;
        GenericPoint tTlk;

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
                // Wuermer anschauen
                krabatSays("Jama1_1", fWacki, 3, 0, 0);
                break;

            case 50:
                // Wuermer mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                krabatSays("Jama1_2", fWacki, 3, 0, 52);
                break;

            case 52:
                // Wuermer mitnehmen
                // Wuermer dem Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(8);
                mainFrame.krabat.nAnimation = 92;
                nextActionID = 53;
                TakeCounter = 5;
                break;

            case 53:
                // Laufe zur Falle
                if (--TakeCounter < 2) {
                    mainFrame.actions[908] = true;
                    mainFrame.isClipSet = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || TakeCounter > 0) {
                    break;
                }
                showHojnt = true;
                jaeger.moveTo(EndPunkt);
                walkReady = false;
                nextActionID = 54;
                break;

            case 54:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 55;
                }
                break;

            case 55:
                // Jaeger kommt zur Hilfe
                // Hier Position des Textes berechnen
                tmp = jaeger.getBoundingBox();
                tTlk = new GenericPoint((tmp.bottomRightPoint.x + tmp.topLeftPoint.x) / 2, tmp.bottomRightPoint.y + 30);
                personSays("Jama1_3", 0, 26, 2, 60, tTlk);
                break;


            case 60:
                // K spricht
                krabatSays("Jama1_4", 0, 1, 2, 65);
                break;

            case 65:
                // Jaeger kommt zur Hilfe
                // Hier Position des Textes berechnen
                tmp = jaeger.getBoundingBox();
                tTlk = new GenericPoint((tmp.bottomRightPoint.x + tmp.topLeftPoint.x) / 2, tmp.bottomRightPoint.y + 30);
                personSays("Jama1_5", 0, 26, 2, 70, tTlk);
                break;


            case 70:
                // K spricht
                krabatSays("Jama1_6", 0, 1, 2, 75);
                break;

            case 75:
                // Jaeger kommt zur Hilfe
                // Hier Position des Textes berechnen
                tmp = jaeger.getBoundingBox();
                tTlk = new GenericPoint((tmp.bottomRightPoint.x + tmp.topLeftPoint.x) / 2, tmp.bottomRightPoint.y + 30);
                personSays("Jama1_7", 0, 26, 2, 80, tTlk);
                break;

            case 80:
                // Jaeger buecken lassen und Krabat reicht Hand, das ca. 5 frames, dann umschalten
                istJaegerGebueckt = true;
                mainFrame.krabat.nAnimation = 90;
                TakeCounter = 5;
                nextActionID = 100;
                break;

            case 100:
                // Gehe zurueck zu Hojnt
                if (--TakeCounter > 1) {
                    break;
                }
                createNewLocation(14, 27);
                break;

            case 150:
                // Wacki - Ausreden
                thingExcuse(fWacki);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}