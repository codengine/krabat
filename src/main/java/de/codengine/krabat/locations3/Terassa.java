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
import de.codengine.krabat.anims.DDBlacksmith;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Terassa extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Terassa.class);
    private GenericImage background;
    private GenericImage gelaender;
    private GenericImage busch;
    private GenericImage hammer;
    private GenericImage gelaender2;
    private GenericImage delle;
    private final DDBlacksmith schmied;

    private final BorderRect schmiedClickRect;
    private final GenericPoint schmiedPoint;
    private final GenericPoint schmiedTalk;
    private final boolean schmiedVisible;
    private boolean schmiedhoertzu = false;
    private boolean schnauzeSchmied = false;
    // Schmied ist da, wenn 529 = true und solange 701 = false, aber nur
    // bei Neueintritt in Location testen

    private boolean isVordergrund;

    // Eingrenzung, ob K nach vorn will, wenn er hinten steht
    private static final BorderTrapezoid[] vorWennHinten =
            {new BorderTrapezoid(0, 1, 0, 330, 408, 479),
                    new BorderTrapezoid(43, 334, 132, 430)};

    // Eingrenzung, ob K vorn bleiben will, wenn er vorn steht
    private static final BorderTrapezoid[] vorWennVor =
            {new BorderTrapezoid(0, 408, 41, 479),
                    new BorderTrapezoid(42, 129, 42, 639, 335, 446),
                    new BorderTrapezoid(42, 447, 639, 479)};

    // Merkvariablen fuer Zielpunkt und ZielActionID, wenn Ebene wechselt
    private int MerkActionID;
    private GenericPoint MerkPunkt;

    // Konstanten - Rects
    private static final BorderRect ausgangMurja
            = new BorderRect(491, 252, 560, 330);
    private static final BorderRect ausgangStraza
            = new BorderRect(171, 313, 215, 360);
    private static final BorderRect ausgangCychi
            = new BorderRect(0, 300, 40, 380);
    private static final BorderRect ausgangKarta
            = new BorderRect(600, 385, 639, 460);
    private static final BorderRect gelaenderRect  // fuer Vordergrund
            = new BorderRect(-400, 326, 639, 800);
    private static final BorderRect buschRect      // fuer Vordergrund
            = new BorderRect(498, 283, 639, 374);
    private static final BorderRect hammerRect
            = new BorderRect(221, 463, 248, 479);

    // Konstante Points
    private static final GenericPoint pExitMurja = new GenericPoint(515, 337);
    private static final GenericPoint pExitStraza = new GenericPoint(193, 311);  // damit K von vorn immer nach hinten geht, bevor exit
    private static final GenericPoint pExitCychi = new GenericPoint(10, 375);
    private static final GenericPoint pExitKarta = new GenericPoint(630, 440);
    private static final GenericPoint pSchmied = new GenericPoint(199, 479);
    private static final GenericPoint schmiedFeet = new GenericPoint(340, 491);
    private static final GenericPoint pHammer = new GenericPoint(188, 479);
    private static final GenericPoint pToHammer = new GenericPoint(199, 479);

    private static final GenericPoint vorUmziehPoint = new GenericPoint(549, 339);
    private static final GenericPoint UmziehPoint = new GenericPoint(620, 339);
    private static final GenericPoint nachUmziehPoint = new GenericPoint(500, 339);

    private static final GenericPoint walktoUnten = new GenericPoint(20, 393);
    private static final GenericPoint walktoTreppe = new GenericPoint(-30, 430);
    private static final GenericPoint walktoTreppeOben = new GenericPoint(-60, 393);
    private static final GenericPoint walktoOben = new GenericPoint(27, 450);

    private static final int UNTEN_MAXX = 440;
    private static final int UNTEN_MINX = 440;
    private static final int UNTEN_DEFSCALE = 50;
    private static final float UNTEN_ZOOMF = 3.0f;

    private static final int OBEN_MAXX = 0;
    private static final int OBEN_MINX = 0;
    private static final int OBEN_DEFSCALE = -90;
    private static final float OBEN_ZOOMF = 6.0f;

    // Konstante ints
    private static final int fKowar = 3;
    private static final int fHammer = 3;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Terassa(Start caller, int oldLocation) {
        super(caller, 127);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        schmied = new DDBlacksmith(mainFrame);

        schmiedPoint = new GenericPoint();
        schmiedPoint.x = schmiedFeet.x - DDBlacksmith.Breite / 2;
        schmiedPoint.y = schmiedFeet.y - DDBlacksmith.Hoehe;

        schmiedTalk = new GenericPoint();
        schmiedTalk.x = schmiedFeet.x;
        schmiedTalk.y = schmiedPoint.y - 50;

        schmiedClickRect = new BorderRect(schmiedPoint.x + 7, schmiedPoint.y, schmiedPoint.x + DDBlacksmith.Breite - 14, schmiedPoint.y + DDBlacksmith.Hoehe);

        // hier evaluieren, ob Schmied ueberhaupt da ist
        schmiedVisible = mainFrame.actions[529] && !mainFrame.actions[701];

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // wenn im Vordergrundrect, welches fuer "ist hinten" gilt, dann ist er vorn
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                isVordergrund = vorWennHinten[0].pointInside(mainFrame.krabat.getPos());
                break;
            case 126: // von Murja aus
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(new GenericPoint(500, 339));
                mainFrame.krabat.setFacing(6);
                isVordergrund = false;
                break;
            case 128: // von Straza aus
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(new GenericPoint(193, 368));
                mainFrame.krabat.setFacing(6);
                isVordergrund = false;
                break;
            case 150: // von Cychi aus
                mainFrame.krabat.setPos(new GenericPoint(22, 375));
                mainFrame.krabat.setFacing(3);
                isVordergrund = false;
                break;
            case 180: // von Karta aus
                mainFrame.krabat.setPos(new GenericPoint(597, 420));
                mainFrame.krabat.setFacing(9);
                isVordergrund = false;
                break;
        }

        // es ist bekannt, ob er vorn oder hinten steht, also init
        initBorders();
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/terassa/terassa.png");
        gelaender = getPicture("gfx-dd/terassa/gelaender.png");
        busch = getPicture("gfx-dd/terassa/busch.png");
        hammer = getPicture("gfx-dd/terassa/thammer.png");
        gelaender2 = getPicture("gfx-dd/terassa/gelaender2.png");
        delle = getPicture("gfx-dd/terassa/delle.png");

    }

    private void initBorders() {
        // Grenzen loeschen
        mainFrame.pathWalker.vBorders.removeAllElements();

        // Testen, welche Grenzen gesetzt werden muessen
        if (!isVordergrund) {
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(515, 550, 300, 500, 337, 362));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(10, 450, 10, 560, 363, 389));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(10, 560, 250, 630, 390, 440));

            mainFrame.pathFinder.clearMatrix(3);

            mainFrame.pathFinder.connectPos(0, 1);
            mainFrame.pathFinder.connectPos(1, 2);

            mainFrame.krabat.maxX = UNTEN_MAXX;
            mainFrame.krabat.minX = UNTEN_MINX;
            mainFrame.krabat.defaultScale = UNTEN_DEFSCALE;
            mainFrame.krabat.zoomFactor = UNTEN_ZOOMF;
        } else {
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(15, 446, 34, 479));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(35, 73, 35, 191, 446, 479));

            mainFrame.pathFinder.clearMatrix(2);

            mainFrame.pathFinder.connectPos(0, 1);

            mainFrame.krabat.maxX = OBEN_MAXX;
            mainFrame.krabat.minX = OBEN_MINX;
            mainFrame.krabat.defaultScale = OBEN_DEFSCALE;
            mainFrame.krabat.zoomFactor = OBEN_ZOOMF;
        }
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

        // Kowar Hintergrund loeschen (sonst vielleicht K geloescht)
        if (schmiedVisible) {
            g.setClip(schmiedPoint.x, schmiedPoint.y, DDBlacksmith.Breite, DDBlacksmith.Hoehe);
            g.drawImage(background, 0, 0);
            g.drawImage(gelaender, 0, 284);

            evalSound(); // Schmied macht Geraeusche
        }

        // Hammer zeichnen, solange noch da
        if (!mainFrame.actions[953] && schmiedVisible) {
            g.setClip(221, 463, 27, 20);
            g.drawImage(hammer, 221, 463);
        }

        // Hier die Delle reinzeichnen, sobald sie drin ist (muss dann immer gezeichnet werden !!!)
        if (mainFrame.actions[700]) {
            g.setClip(203, 358, 15, 10);
            g.drawImage(delle, 203, 358);
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
                if (mainFrame.isInventoryCursor) {
                    evalMouseMoveEvent(mainFrame.mousePoint);  // wenn Krabat unter den Umzieh-Cursor laeuft, muss das Highlight automatisch kommen
                }
            }
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Wenn Krabat im Vordergrund steht, dann braucht nicht gecheckt zu werden
        if (!isVordergrund) {
            // steht hinter Gelander
            if (gelaenderRect.isPointInRect(pKrTemp)) {
                g.drawImage(!mainFrame.actions[700] ? gelaender : gelaender2, 0, 284);
            }

            // steht hinter Busch
            if (buschRect.isPointInRect(pKrTemp)) {
                g.drawImage(busch, 512, 284);
            }
        }

        // Kowar zeichnen
        if (schmiedVisible) {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(schmiedPoint.x, schmiedPoint.y, DDBlacksmith.Breite, DDBlacksmith.Hoehe);
            schmied.drawDDkowar(g, talkPerson, schmiedPoint, schmiedhoertzu);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden Schmied
                if (schmiedClickRect.isPointInRect(pTemp) && schmiedVisible) {
                    nextActionID = 150;
                    pTxxx = pSchmied;
                }

                // Ausreden Hammer
                if (hammerRect.isPointInRect(pTemp) && !mainFrame.actions[953] &&
                        schmiedVisible) {
                    nextActionID = 155;
                    pTxxx = pHammer;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (canKrabatStayOnLayer(pTxxx)) {
                    mainFrame.pathWalker.setNewWay(pTxxx);
                    mainFrame.repaint();
                }
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Murja gehen ?
                if (ausgangMurja.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangMurja.isPointInRect(kt)) {
                        pTxxx = pExitMurja;
                    } else {
                        pTxxx = new GenericPoint(pExitMurja.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Straza gehen ?
                if (ausgangStraza.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangStraza.isPointInRect(kt)) {
                        pTxxx = pExitStraza;
                    } else {
                        pTxxx = new GenericPoint(pExitStraza.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Cychi gehen ?
                if (ausgangCychi.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangCychi.isPointInRect(kt)) {
                        pTxxx = pExitCychi;
                    } else {
                        pTxxx = new GenericPoint(pExitCychi.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Karta gehen ?
                if (ausgangKarta.isPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKarta.isPointInRect(kt)) {
                        pTxxx = pExitKarta;
                    } else {
                        pTxxx = new GenericPoint(pExitKarta.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schmied ansehen
                if (schmiedClickRect.isPointInRect(pTemp) && schmiedVisible) {
                    nextActionID = 1;
                    pTxxx = pSchmied;
                }

                // Hammer ansehen
                if (hammerRect.isPointInRect(pTemp) && !mainFrame.actions[953] &&
                        schmiedVisible) {
                    nextActionID = 2;
                    pTxxx = pHammer;
                }

                if (canKrabatStayOnLayer(pTxxx)) {
                    mainFrame.pathWalker.setNewWay(pTxxx);
                    mainFrame.repaint();
                }
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangMurja.isPointInRect(pTemp) ||
                        ausgangStraza.isPointInRect(pTemp) ||
                        ausgangCychi.isPointInRect(pTemp) ||
                        ausgangKarta.isPointInRect(pTemp)) {
                    return;
                }

                // Schmied anreden
                if (schmiedClickRect.isPointInRect(pTemp) && schmiedVisible) {
                    nextActionID = 50;
                    if (canKrabatStayOnLayer(pSchmied)) {
                        mainFrame.pathWalker.setNewWay(pSchmied);
                        mainFrame.repaint();
                    }
                    return;
                }

                // Hammer nehmen
                if (hammerRect.isPointInRect(pTemp) && !mainFrame.actions[953] &&
                        schmiedVisible) {
                    nextActionID = 55;
                    if (canKrabatStayOnLayer(pHammer)) {
                        mainFrame.pathWalker.setNewWay(pHammer);
                        mainFrame.repaint();
                    }
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
                    schmiedClickRect.isPointInRect(pTemp) && schmiedVisible ||
                    hammerRect.isPointInRect(pTemp) && !mainFrame.actions[953] &&
                            schmiedVisible;

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
            if (schmiedClickRect.isPointInRect(pTemp) && schmiedVisible ||
                    hammerRect.isPointInRect(pTemp) && !mainFrame.actions[953] &&
                            schmiedVisible) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (ausgangMurja.isPointInRect(pTemp) ||
                    ausgangStraza.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (ausgangCychi.isPointInRect(pTemp)) {
                if (cursorShape != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    cursorShape = 9;
                }
                return;
            }

            if (ausgangKarta.isPointInRect(pTemp)) {
                if (cursorShape != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    cursorShape = 3;
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

    // hier wird festgestellt, ob Krabat den Vorder/Hintergrundwechsel durchfuehren muss
    private boolean canKrabatStayOnLayer(GenericPoint Zielpunkt) {
        // rects sind verschieden, je nach dem, ob er oben ist
        if (isVordergrund) {
            if (vorWennVor[0].pointInside(Zielpunkt) ||
                    vorWennVor[1].pointInside(Zielpunkt) ||
                    vorWennVor[2].pointInside(Zielpunkt)) {
                // der WegGeher kann normal wie immer angesprochen werden, nix wird veraendert
                return true;
            } else {
                // Laufpunkt wird extra gesetzt und Anim bei Erreichen eingeschaltet

                // DoAction und ZielPunkt merken
                MerkPunkt = Zielpunkt;
                MerkActionID = nextActionID;

                mainFrame.pathWalker.setWayWithoutStanding(walktoOben);
                mainFrame.repaint();
                nextActionID = 800;
                return false;
            }
        } else {
            if (!vorWennHinten[0].pointInside(Zielpunkt) &&
                    !vorWennHinten[1].pointInside(Zielpunkt)) {
                // der WegGeher kann normal wie immer angesprochen werden, nix wird veraendert
                return true;
            } else {
                // Laufpunkt wird extra gesetzt und Anim bei Erreichen eingeschaltet

                // DoAction und ZielPunkt merken
                MerkPunkt = Zielpunkt;
                MerkActionID = nextActionID;

                mainFrame.pathWalker.setWayWithoutStanding(walktoUnten);
                mainFrame.repaint();
                nextActionID = 900;
                return false;
            }
        }
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

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        if (schnauzeSchmied) {
            return;
        }

        int zf = (int) (Math.random() * 100);
        if (zf > 96) {
            int zwzf = (int) (Math.random() * 2.99);
            zwzf += 49;

            mainFrame.soundPlayer.playFile("sfx-dd/schmied" + (char) zwzf + ".wav");
        }
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

        // Achtung, hier sind 2 Ausnahmen, weil diese Sachen in dieser Location behandelt werden muessen
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 541 && nextActionID != 553) {
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
                // Schmied anschauen
                krabatSays("Terassa_1", fKowar, 3, 0, 0);
                break;

            case 2:
                // Hammer anschauen
                krabatSays("Terassa_2", fHammer, 3, 0, 0);
                break;

            case 50:
                // Schmied anreden hat wenig Erfolg
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = true;
                Counter = 10;
                nextActionID = 52;
                break;

            case 52:
                // nach bisschen Warten die Antwort bringen (Sound stoert hoffentlich nicht mehr)
                if (--Counter > 1) {
                    break;
                }
                int zuffZahl = (int) (Math.random() * 3.9);
                switch (zuffZahl) {
                    case 0:
                        personSays("Terassa_3", fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 1:
                        personSays("Terassa_4", fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 2:
                        personSays("Terassa_5", fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 3:
                        personSays("Terassa_6", fKowar, 45, 0, 53, schmiedTalk);
                        break;
                }
                break;

            case 53:
                // Ende Talk Schmied
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = false;
                nextActionID = 0;
                break;

            case 55:
                // Hammer mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = true;
                mainFrame.krabat.setFacing(fHammer);
                nextActionID = 60;
                mainFrame.krabat.nAnimation = 34;
                Counter = 5;
                break;

            case 60:
                // Krabat sagt Spruch
                if (--Counter == 1) {
                    mainFrame.actions[953] = true;        // Flag setzen
                    mainFrame.isClipSet = false;  // alles neu zeichnen
                    // Inventar hinzufuegen
                    mainFrame.inventory.vInventory.addElement(46);
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                krabatSays("Terassa_7", 0, 3, 2, 63);
                break;

            case 63:
                // zum haemmern hinlaufen
                mainFrame.pathWalker.setNewWay(pToHammer);
                nextActionID = 65;
                break;

            case 65:
                // Krabat schlaegt zu
                mainFrame.krabat.setFacing(12);
                mainFrame.krabat.nAnimation = 147;
                nextActionID = 70;
                Counter = 10;
                break;

            case 70:
                // Schmied regt sich auf
                if (--Counter == 1) {
                    mainFrame.actions[700] = true; // ab jetzt die Delle drin
                    mainFrame.isClipSet = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                personSays("Terassa_8", fKowar, 45, 2, 75, schmiedTalk);
                schmiedhoertzu = true;
                break;

            case 75:
                // Krabat antwortet
                krabatSays("Terassa_9", fKowar, 1, 2, 80);
                break;

            case 80:
                // Schmied sagt letzten Spruch
                personSays("Terassa_10", 0, 45, 2, 85, schmiedTalk);
                schmiedhoertzu = false;
                break;

            case 85:
                // Ende dieser Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = false;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Murja
                // Hat Krabat noch Dienstkleidung an -> zur Murja darf er so gehen
                if (mainFrame.actions[511]) {
                    krabatUmziehen();
                    break;
                }
                createNewLocation(126, locationID);
                break;

            case 101:
                // Gehe zu Straza
                createNewLocation(128, locationID);
                break;

            case 102:
                // Gehe zu Cychi
                // Hat Krabat noch Dienstkleidung an -> erst umziehen, aber nicht automatisch !!!
                if (mainFrame.actions[511]) {
                    krabatUmziehen();
                    break;
                }
                createNewLocation(150, locationID);
                break;

            case 103:
                // Gehe zu Karta
                // Hat Krabat noch Dienstkleidung an -> erst umziehen, aber nicht automatisch
                if (mainFrame.actions[511]) {
                    krabatUmziehen();
                    break;
                }
                createNewLocation(180, locationID);
                break;

            case 150:
                // Kowar - Ausreden
                maleExcuse(fKowar);
                break;

            case 155:
                // Hammer - Ausreden
                thingExcuse(fHammer);
                break;

            case 541:
                // Krabat zieht sich Bedienstetenkleidung an
                nextActionID = 700;
                if (canKrabatStayOnLayer(vorUmziehPoint)) {
                    mainFrame.pathWalker.setNewWay(vorUmziehPoint);
                }
                break;

            case 553:
                // Krabat zieht sich wieder normale Klamotten an
                nextActionID = 750;
                if (canKrabatStayOnLayer(vorUmziehPoint)) {
                    mainFrame.pathWalker.setNewWay(vorUmziehPoint);
                }
                break;

            // Anim "Sluz. Drasta anziehen"

            case 700:
                // Krabat geht hintern Busch
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.pathWalker.setNewWayGuaranteed(UmziehPoint);
                nextActionID = 710;
                break;

            case 710:
                // Kleidung wechseln, sluz Drasta anziehen
                mainFrame.actions[511] = true;
                mainFrame.actions[850] = true;
                mainFrame.checkKrabat();
                mainFrame.inventory.vInventory.addElement(53);
                mainFrame.inventory.vInventory.removeElement(41);
                nextActionID = 720;
                break;

            case 720:
                // wieder erscheinen
                mainFrame.pathWalker.setNewWayGuaranteed(nachUmziehPoint);
                if (!mainFrame.actions[702]) {
                    nextActionID = 730;  // Spruch nur 1x reissen
                } else {
                    nextActionID = 740;
                }
                break;

            case 730:
                // Kommentar
                krabatSays("Terassa_11", 0, 3, 0, 740);
                break;

            case 740:
            case 780:
                // Ende
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            // Anim "Normale Kleidung anziehen"

            case 750:
                // Krabat geht hintern Busch
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.pathWalker.setNewWayGuaranteed(UmziehPoint);
                nextActionID = 760;
                break;

            case 760:
                // Kleidung wechseln, wieder normale Kleidung anziehen
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.checkKrabat();
                mainFrame.inventory.vInventory.addElement(41);
                mainFrame.inventory.vInventory.removeElement(53);
                nextActionID = 770;
                break;

            case 770:
                // wieder erscheinen
                mainFrame.pathWalker.setNewWayGuaranteed(nachUmziehPoint);
                if (!mainFrame.actions[702]) {
                    nextActionID = 775;
                } else {
                    nextActionID = 780;
                }
                break;

            case 775:
                // Spruch reissen, dass wieder Normalkleidung
                mainFrame.actions[702] = true;
                krabatSays("Terassa_12", 0, 3, 0, 780);
                break;

            // Hier Routinen fuer "die Treppe runter"

            case 800:
                // von vorn nach hinten laufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // auf die Treppe zu laufen
                mainFrame.pathWalker.setNewWayGuaranteed(walktoTreppeOben);
                Counter = 20;
                nextActionID = 810;
                break;

            case 810:
                // die Treppe runterlaufen, nur runterbeamen
                if (--Counter > 1) {
                    break;
                }
                mainFrame.krabat.setPos(walktoTreppe);
                // Borders neu initialisieren
                isVordergrund = false;
                initBorders();
                nextActionID = 820;
                break;

            case 820:
                // wieder in den richtigen Bereich laufen
                mainFrame.pathWalker.setNewWayGuaranteed(walktoUnten);
                nextActionID = 830;
                break;

            case 830:
            case 930:
                // wir sind da, nur noch die alten Werte restaurieren
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = MerkActionID;
                mainFrame.pathWalker.setNewWay(MerkPunkt);
                mainFrame.repaint();
                break;

            // Hier Routinen fuer "die Treppe rauf" (Hallo Kelly Bundy !!!)

            case 900:
                // von hinten nach vorn laufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // auf die Treppe zu laufen
                mainFrame.pathWalker.setNewWayGuaranteed(walktoTreppe);
                Counter = 20;
                nextActionID = 910;
                break;

            case 910:
                // die Treppe hochlaufen, nur beamen
                if (--Counter > 1) {
                    break;
                }
                mainFrame.krabat.setPos(walktoTreppeOben);
                nextActionID = 920;
                break;

            case 920:
                // wieder in den richtigen Bereich laufen
                mainFrame.pathWalker.setNewWayGuaranteed(walktoOben);
                // Borders neu initialisieren
                isVordergrund = true;
                initBorders();
                nextActionID = 930;
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }

    private void krabatUmziehen() {
        // Krabat zieht sich Dienstkleidung aus
        krabatSays("Terassa_13", 0, 3, 0, 0);
    }
}