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
import de.codengine.krabat.anims.Deer;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Most1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Most1.class);
    private GenericImage background;
    private GenericImage gelaend;
    private GenericImage wegstueck;
    private final GenericImage[] flussu;
    private boolean switchanim = false;
    private int flusscount = 1;
    private int oldActionID = 0;
    private GenericPoint Endpunkt;
    private GenericPoint Wendepunkt;
    private final GenericPoint Merkpunkt;
    private boolean Berglauf = false;
    private boolean isTal;

    private Deer deer;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang = new BorderRect(609, 353, 639, 415);
    private static final BorderRect obererAusgang = new BorderRect(412, 176, 496, 221);
    private static final BorderRect untererAusgang = new BorderRect(0, 436, 246, 479);
    private static final BorderRect ralbitzSchild = new BorderRect(254, 208, 301, 228);
    private static final BorderRect dresdenSchild = new BorderRect(255, 233, 296, 246);
    private static final BorderRect rekaRect = new BorderRect(427, 383, 513, 476);

    private static final BorderTrapezoid BergTrapez = new BorderTrapezoid(376, 396, 272, 342, 278, 349);
    private static final BorderTrapezoid TalTrapez = new BorderTrapezoid(457, 459, 407, 419, 225, 284);

    // Points in Location
    private static final GenericPoint Pschild = new GenericPoint(305, 333);
    private static final GenericPoint Pdown = new GenericPoint(95, 479);
    private static final GenericPoint Pup = new GenericPoint(458, 226);
    private static final GenericPoint Pright = new GenericPoint(639, 390);
    private static final GenericPoint Preka = new GenericPoint(476, 388);

    // Zooming - Variablen
    private static final int TAL_MAXX = 278;
    private static final int TAL_MINX = 278;
    private static final float TAL_ZOOMF = 5.2f;
    private static final int TAL_DEFSCALE = 70;

    private static final int BERG_MAXX = 422;
    private static final int BERG_MINX = 134;
    private static final float BERG_ZOOMF = 3.6f;
    private static final int BERG_DEFSCALE = -20;

    // Konstante ints
    private static final int fPokazRal = 9;
    private static final int fPokazDrj = 9;
    private static final int fReka = 6;
    private static final int fDrjezdzany = 6;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Most1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        Merkpunkt = new GenericPoint(0, 0);

        flussu = new GenericImage[8];

        deer = new Deer(mainFrame, false, new GenericRectangle(586, 247, 53, 25), 3);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        initImages();
        switch (oldLocation) {
            case 0: // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                GenericPoint tp = mainFrame.krabat.getPos();
                BorderRect TalRect = new BorderRect(400, 220, 460, 290);
                isTal = TalRect.isPointInRect(tp);
                break;
            case 1:
                // von Ralbicy aus
                mainFrame.krabat.setPos(new GenericPoint(624, 384));
                mainFrame.krabat.setFacing(9);
                isTal = false;
                break;
            case 7:
                // von Sunow aus
                mainFrame.krabat.setPos(new GenericPoint(458, 226));
                mainFrame.krabat.setFacing(6);
                isTal = true;
                break;
        }

        // Matrix - Init
        initMatrix();

    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/most/most2.png");
        gelaend = getPicture("gfx/most/most-2.png");
        wegstueck = getPicture("gfx/most/most-3.png");

        flussu[1] = getPicture("gfx/most/flu-1.png");
        flussu[2] = getPicture("gfx/most/flu-2.png");
        flussu[3] = getPicture("gfx/most/flu-3.png");
        flussu[4] = getPicture("gfx/most/flu-4.png");
        flussu[5] = getPicture("gfx/most/flu-5.png");
        flussu[6] = getPicture("gfx/most/flu-6.png");
        flussu[7] = getPicture("gfx/most/flu-7.png");

    }

    private void initMatrix() {
        mainFrame.pathWalker.vBorders.removeAllElements();

        if (isTal) {
            // Grenzen setzen im Tal
            // Taltrapez
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(457, 459, 407, 419, 225, 284));

            // Laufmatrix anpassen
            mainFrame.pathFinder.clearMatrix(1);

            // Zooming anpassen
            mainFrame.krabat.maxX = TAL_MAXX;
            mainFrame.krabat.minX = TAL_MINX;
            mainFrame.krabat.zoomFactor = TAL_ZOOMF;
            mainFrame.krabat.defaultScale = TAL_DEFSCALE;
        } else {
            // Grenzen setzen auf dem Berg
            // Bergtrapez
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(376, 396, 272, 342, 278, 349));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(272, 515, 218, 515, 350, 367));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(516, 358, 556, 373));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(557, 368, 609, 378));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(610, 368, 639, 402));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(218, 286, 43, 182, 368, 479));

            // Laufmatrix anpassen
            mainFrame.pathFinder.clearMatrix(6);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.pathFinder.connectPos(0, 1);
            mainFrame.pathFinder.connectPos(1, 2);
            mainFrame.pathFinder.connectPos(2, 3);
            mainFrame.pathFinder.connectPos(3, 4);
            mainFrame.pathFinder.connectPos(1, 5);

            // Zooming anpassen
            mainFrame.krabat.maxX = BERG_MAXX;
            mainFrame.krabat.minX = BERG_MINX;
            mainFrame.krabat.zoomFactor = BERG_ZOOMF;
            mainFrame.krabat.defaultScale = BERG_DEFSCALE;
        }
    }

    @Override
    public void cleanup() {
        background = null;
        gelaend = null;
        wegstueck = null;

        flussu[1] = null;
        flussu[2] = null;
        flussu[3] = null;
        flussu[4] = null;
        flussu[5] = null;
        flussu[6] = null;
        flussu[7] = null;

        deer.cleanup();
        deer = null;
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
            mainFrame.isBackgroundAnimRunning = true;
            g.setClip(0, 0, 644, 484);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Rehe Hintergrund loeschen
        g.setClip(586, 200, 100, 100);
        g.drawImage(background, 0, 0);

        // Rehe zeichnen
        deer.drawReh(g);

        // Animation abspielen
        switchanim = !switchanim;
        if (switchanim) {
            g.setClip(0, 0, 644, 484);
            g.drawImage(flussu[flusscount], 387, 380);
            flusscount++;
            if (flusscount == 8) {
                flusscount = 1;
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // hier ist der Sound...
        evalSound();

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
        if (Berglauf) {
            g.drawImage(wegstueck, 342, 270);
        }

        GenericPoint tem = mainFrame.krabat.getPos();

        if (BergTrapez.pointInside(tem)) {
            g.drawImage(gelaend, 379, 273);
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

                // Ausreden fuer Schild Ralbitz
                if (ralbitzSchild.isPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 16 ? 200 : 150;
                    pTemp = Pschild;
                }

                // Ausreden fuer Schild Dresden
                if (dresdenSchild.isPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 16 ? 200 : 155;
                    pTemp = Pschild;
                }

                // Ausreden fuer Reka
                if (rekaRect.isPointInRect(pTemp)) {
                    // wuda + wacki
                    nextActionID = mainFrame.whatItem == 10 ? 210 : 160;
                    pTemp = Preka;
                }

                boolean tp = tryWalk(pTemp, nextActionID);

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (!tp) {
                    mainFrame.pathWalker.setNewWay(pTemp);
                }
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

                // zu Dresden gehen ?
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                }

                // nach Sunow gehen?
                if (obererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.isPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // rechter Ausgang zu Ralbicy
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
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

                // Schild Ralbitz ansehen
                if (ralbitzSchild.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pschild;
                }

                // Schild Dresden ansehen
                if (dresdenSchild.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschild;
                }

                // Reka ansehen
                if (rekaRect.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Preka;
                }

                boolean tz = tryWalk(pTemp, nextActionID);

                log.debug("Lauftest ergab : {}", tz);

                if (!tz) {
                    mainFrame.pathWalker.setNewWay(pTemp);
                }
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                nextActionID = 0;

                // ??? Anschauen
                if (untererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Sunow anschauen
                if (obererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Ralbitz anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Schild Ralbitz mitnehmen
                if (ralbitzSchild.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    pTemp = Pschild;
                    boolean tu = tryWalk(pTemp, nextActionID);
                    if (!tu) {
                        mainFrame.pathWalker.setNewWay(pTemp);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Schild Ralbitz mitnehmen
                if (dresdenSchild.isPointInRect(pTemp)) {
                    nextActionID = 55;
                    pTemp = Pschild;
                    boolean tu = tryWalk(pTemp, nextActionID);
                    if (!tu) {
                        mainFrame.pathWalker.setNewWay(pTemp);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Reka mitnehmen
                if (rekaRect.isPointInRect(pTemp)) {
                    nextActionID = 70;
                    pTemp = Preka;
                    boolean tu = tryWalk(pTemp, nextActionID);
                    if (!tu) {
                        mainFrame.pathWalker.setNewWay(pTemp);
                    }
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
        // Wenn Animation, dann transparenter Cursor
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
            mainFrame.isInventoryHighlightCursor = ralbitzSchild.isPointInRect(pTemp) || tmp.isPointInRect(pTemp) ||
                    dresdenSchild.isPointInRect(pTemp) || rekaRect.isPointInRect(pTemp);

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
                    mainFrame.setCursor(mainFrame.cursorRight);
                    cursorShape = 3;
                }
                return;
            }

            if (ralbitzSchild.isPointInRect(pTemp) || dresdenSchild.isPointInRect(pTemp) ||
                    rekaRect.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (obererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 4;
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

    // Erkennungsroutine, ob Animationsmodus eingeschaltet werden muss
    private boolean tryWalk(GenericPoint pTxxx, int Action) {
        GenericPoint kpos = mainFrame.krabat.getPos();

        // Hier Punkt klonen, damit alter Punkt erhalten bleibt
        GenericPoint pTemp = new GenericPoint(pTxxx.x, pTxxx.y);

        log.debug("Es wird getestet.");

        // vom Tal auf den Berg???
        if (isTal && pTemp.y > 277) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 600;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor dem Verschwinden berechnen
            GenericPoint rand = TalTrapez.points(kpos.y);
            log.debug(" links aktuell rechts {} {} {}", rand.x, kpos.x, rand.y);
            pTemp.y = TalTrapez.y2;
            float t1 = kpos.x - rand.x;
            float t2 = rand.y - rand.x;
            float teil = t1 / t2;
            pTemp.x = TalTrapez.x3 + (int) ((TalTrapez.x4 - TalTrapez.x3) * teil);

            log.debug("Mittenfaktor {}", teil);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (BergTrapez.x1 + (BergTrapez.x2 - BergTrapez.x1) * teil), BergTrapez.y1);
            Wendepunkt = new GenericPoint((pTemp.x + Endpunkt.x) / 2, 380);

            mainFrame.pathWalker.setWayWithoutStanding(pTemp);

            log.debug(" Startpunkt {} {}", pTemp.x, pTemp.y);

            mainFrame.repaint();

            log.debug(" Wendepunkt Endpunkt {} {} {} {}", Wendepunkt.x, Wendepunkt.y, Endpunkt.x, Endpunkt.y);

            return true;
        }

        // vom Berg ins Tal ??
        if (!isTal && pTemp.y < 278) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 610;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor Verschwinden berechnen
            GenericPoint raud = BergTrapez.points(kpos.y);

            log.debug(" links aktuell rechts {} {} {}", raud.x, kpos.x, raud.y);

            pTemp.y = BergTrapez.y1;
            float t3 = kpos.x - raud.x;
            float t4 = raud.y - raud.x;
            float teal = t3 / t4;

            log.debug(" Mittenfaktor {}", teal);

            if (BergTrapez.pointInside(mainFrame.krabat.getPos())) {
                pTemp.x = BergTrapez.x1 + (int) ((BergTrapez.x2 - BergTrapez.x1) * teal);
            } else {
                // Default - Werte fuer Tallauf, wenn noch zu weit weg
                pTemp = new GenericPoint((BergTrapez.x1 + BergTrapez.x2) / 2, BergTrapez.y1);
                teal = 0.5f;
            }

            log.debug(" Mittenfaktor neu {}", teal);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (TalTrapez.x3 + (TalTrapez.x4 - TalTrapez.x3) * teal), TalTrapez.y2);
            Wendepunkt = new GenericPoint((pTemp.x + Endpunkt.x) / 2, 380);

            mainFrame.pathWalker.setWayWithoutStanding(pTemp);

            log.debug(" Startpunkt {} {}", pTemp.x, pTemp.y);

            mainFrame.repaint();

            log.debug(" Wendepunkt Endpunkt {} {} {} {}", Wendepunkt.x, Wendepunkt.y, Endpunkt.x, Endpunkt.y);

            return true;
        }
        return false;
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

    private void evalSound() {
        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 4.99);
            zwzfz += 49;

            mainFrame.soundPlayer.playFile("sfx/recka" + (char) zwzfz + ".wav");
        }
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
                // Schild Ralbitz anschauen
                krabatSays("Most1_1", fPokazRal, 3, 0, 0);
                break;

            case 2:
                // Schild Dresden anschauen
                krabatSays("Most1_2", fPokazDrj, 3, 0, 0);
                break;

            case 3:
                // Reka anschauen
                krabatSays("Most1_3", fReka, 3, 0, 0);
                break;

            case 50:
                // Schild Ralbitz mitnehmen
                krabatSays("Most1_4", fPokazRal, 3, 0, 0);
                break;

            case 55:
                // Schild Dresden mitnehmen
                krabatSays("Most1_5", fPokazDrj, 3, 0, 0);
                break;

            case 60:
                // Nach Dresden gehen
                krabatSays("Most1_6", fDrjezdzany, 3, 0, 0);
                break;

            case 70:
                // Reka mitnehmen
                // Zufallszahl 0 bis 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Most1_7", fReka, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Most1_8", fReka, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // Gehe zu Ralbicy
                createNewLocation(1, 2);
                break;

            case 102:
                // nach Sunow gehen
                createNewLocation(7, 2);
                break;

            case 150:
                // Schild - Ausreden Ralbitz
                thingExcuse(fPokazRal);
                break;

            case 155:
                // Schild - Ausreden Ralbitz
                thingExcuse(fPokazDrj);
                break;

            case 160:
                // Ausrede Wasser
                thingExcuse(fReka);
                break;

            case 200:
                // kamuski auf schild
                krabatSays("Most1_9", fPokazRal, 3, 0, 0);
                break;

            case 210:
                // Wuda + wacki auf Wasser
                krabatSays("Most1_10", fReka, 3, 0, 0);
                break;

            case 600:
                // vom Tal auf den Berg laufen
                Berglauf = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.setNewWayGuaranteed(Wendepunkt);
                nextActionID = 601;
                break;

            case 601:
                // beim Lauf Tal auf Berg wieder zum Vorschein kommen
                mainFrame.krabat.maxX = BERG_MAXX;
                mainFrame.krabat.minX = BERG_MINX;
                mainFrame.krabat.defaultScale = BERG_DEFSCALE;
                mainFrame.krabat.zoomFactor = BERG_ZOOMF;
                mainFrame.pathWalker.setWayGuaranteedWrong(Endpunkt);
                nextActionID = 620;
                break;

            case 610:
                // vom Berg ins Tal laufen invertiert
                Berglauf = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.setWayGuaranteedWrong(Wendepunkt);
                nextActionID = 611;
                break;

            case 611:
                // beim Lauf Berg ins Tal wieder zum Vorschein kommen
                mainFrame.pathWalker.setNewWayGuaranteed(Endpunkt);
                mainFrame.krabat.maxX = TAL_MAXX;
                mainFrame.krabat.defaultScale = TAL_DEFSCALE;
                mainFrame.krabat.zoomFactor = TAL_ZOOMF;
                mainFrame.krabat.minX = TAL_MINX;
                nextActionID = 620;
                break;

            case 620:
                // Laufen beenden und alles wieder auf Normal zuruecksetzen
                mainFrame.isAnimRunning = false;
                Berglauf = false;
                cursorShape = 200;
                evalMouseMoveEvent(mainFrame.mousePoint);
                isTal = !isTal;
                initMatrix();
                nextActionID = oldActionID;
                mainFrame.pathWalker.setNewWay(Merkpunkt);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}