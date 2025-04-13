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
import de.codengine.krabat.anims.Merchant;
import de.codengine.krabat.anims.PredMalickow;
import de.codengine.krabat.anims.PredWosuskow;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class StareWiki extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(StareWiki.class);
    private GenericImage background;
    private GenericImage koleso;
    private final Merchant merchant;
    private final PredWosuskow predWosuskow;
    private final PredMalickow predMalickow;
    private final MultipleChoice Dialog;

    private final GenericPoint korejtowPoint;
    private final GenericPoint talkPointKorejty;
    private final BorderRect rePredKorejtow;

    private final GenericPoint wosuskowPoint;
    private final GenericPoint talkPointWosuski;
    private final BorderRect rePredWosuskow;
    private boolean wosIsLooking = false;
    private boolean wosIsGiving = false;

    private final GenericPoint malickowPoint;
    private final GenericPoint talkPointMalicki;
    private final BorderRect rePredMalickow;
    private boolean malIsWalking = false;
    private boolean malIsGiving = false;
    private boolean malIsInvisible = false;
    private boolean malIsListening = false;

    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos;
    private int AnimTalkPerson = 0;
    private int AnimID = 5;
    private int AnimCounter = 0;
    private boolean AnimMCLocked = false;

    // Konstanten - Rects
    private static final BorderRect ausgangKarta
            = new BorderRect(0, 440, 310, 479);
    private static final BorderRect rectKoleso
            = new BorderRect(305, 395, 450, 415);

    // Konstante Points
    private static final GenericPoint pExitKarta = new GenericPoint(175, 479);
    private static final GenericPoint pPredKorejtow = new GenericPoint(412, 441);
    private static final GenericPoint pPredWosuskow = new GenericPoint(139, 407);
    private static final GenericPoint pPredMalickow = new GenericPoint(212, 400);
    private static final GenericPoint korejtowFeet = new GenericPoint(476, 444);
    private static final GenericPoint wosuskowFeet = new GenericPoint(67, 348);
    private static final GenericPoint malickowFeet = new GenericPoint(180, 369);

    // Konstante ints
    private static final int fKorj = 3;
    private static final int fWosu = 9;
    private static final int fMali = 9;

    private int Counter = 0;

    private static final int FAELSCHER_WARTEZEIT = 40;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public StareWiki(Start caller, int oldLocation) {
        super(caller, 175);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(6, true);

        mainFrame.krabat.maxX = 418;
        mainFrame.krabat.zoomFactor = 0.15f;
        mainFrame.krabat.defaultScale = -90;

        merchant = new Merchant(mainFrame, new GenericPoint(420, 250));
        predWosuskow = new PredWosuskow(mainFrame);
        predMalickow = new PredMalickow(mainFrame);
        Dialog = new MultipleChoice(mainFrame);

        // Inits fuer predKorejtow
        korejtowPoint = new GenericPoint();
        korejtowPoint.x = korejtowFeet.x - Merchant.Breite / 2;
        korejtowPoint.y = korejtowFeet.y - Merchant.Hoehe;

        talkPointKorejty = new GenericPoint();
        talkPointKorejty.x = korejtowFeet.x;
        talkPointKorejty.y = korejtowPoint.y - 100;

        rePredKorejtow = new BorderRect(korejtowPoint.x, korejtowPoint.y, korejtowPoint.x + Merchant.Breite, korejtowPoint.y + Merchant.Hoehe);

        // Inits fuer predWosuskow
        wosuskowPoint = new GenericPoint();
        wosuskowPoint.x = wosuskowFeet.x - PredWosuskow.Breite / 2;
        wosuskowPoint.y = wosuskowFeet.y - PredWosuskow.Hoehe;

        talkPointWosuski = new GenericPoint();
        talkPointWosuski.x = wosuskowFeet.x;
        talkPointWosuski.y = wosuskowPoint.y - 130;

        rePredWosuskow = new BorderRect(wosuskowPoint.x, wosuskowPoint.y, wosuskowPoint.x + PredWosuskow.Breite, wosuskowPoint.y + PredWosuskow.Hoehe);

        // Inits fuer predMalickow
        malickowPoint = new GenericPoint();
        malickowPoint.x = malickowFeet.x - PredMalickow.Breite / 2;
        malickowPoint.y = malickowFeet.y - PredMalickow.Hoehe;

        talkPointMalicki = new GenericPoint();
        talkPointMalicki.x = malickowFeet.x;
        talkPointMalicki.y = malickowPoint.y - 50;

        rePredMalickow = new BorderRect(malickowPoint.x, malickowPoint.y, malickowPoint.x + PredMalickow.Breite, malickowPoint.y + PredMalickow.Hoehe);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 430, 412, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 290, 20, 290, 418, 429));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(244, 370, 244, 370, 400, 417));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(210, 243, 210, 243, 400, 402));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(140, 407, 180, 409));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(181, 407, 184, 417));

        mainFrame.pathFinder.clearMatrix(6);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(1, 5);
        mainFrame.pathFinder.connectPos(4, 5);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 180:
                // von Karta aus
                mainFrame.krabat.setPos(new GenericPoint(175, 470));
                mainFrame.krabat.setFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/starewiki/starewiki.png");
        koleso = getPicture("gfx-dd/starewiki/koleso.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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

        // PredWos und PredMal Hintergrund loeschen (sind uebereinanader)
        g.setClip(wosuskowPoint.x, wosuskowPoint.y, PredWosuskow.Breite, PredWosuskow.Hoehe);
        g.drawImage(background, 0, 0);
        g.setClip(malickowPoint.x - 5, malickowPoint.y - 3, PredMalickow.Breite + 10, PredMalickow.Hoehe + 6);
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Predawar Korejtow zeichnen
        g.setClip(korejtowPoint.x, korejtowPoint.y, Merchant.Breite, Merchant.Hoehe);
        g.drawImage(background, 0, 0);
        if (AnimTalkPerson == 62 && !AnimMCLocked) {
            // bei einer Anim rumschreien lassen
            merchant.callPredawar(g);
        } else {
            if (talkPerson == 62) {
                // beim Reden
                merchant.talkPredawar(g);
            } else {
                // beim Rumstehen
                merchant.drawPredawar(g);
            }
        }

        // Predawar Wosuskow zeichnen
        g.setClip(wosuskowPoint.x, wosuskowPoint.y, PredWosuskow.Breite, PredWosuskow.Hoehe);
        if (AnimTalkPerson == 63 && !AnimMCLocked) {
            // bei einer Anim rumschreien lassen
            predWosuskow.callPredawar(g);
        } else {
            if (talkPerson == 63) {
                // beim Reden
                predWosuskow.talkPredawar(g);
            } else {
                if (wosIsGiving || wosIsLooking) {
                    // bei irgendwelchen Anims
                    if (wosIsGiving) {
                        wosIsGiving = predWosuskow.animPredawar(g, 2);
                    }
                    if (wosIsLooking) {
                        wosIsLooking = predWosuskow.animPredawar(g, 1);
                    }
                } else {
                    // beim Rumstehen
                    predWosuskow.drawPredawar(g);
                }
            }
        }

        // Predawar Malickoscow zeichnen
        g.setClip(malickowPoint.x - 5, malickowPoint.y - 3, PredMalickow.Breite + 10, PredMalickow.Hoehe + 6);
        if (AnimTalkPerson == 64 && !AnimMCLocked) {
            // bei einer Anim rumschreien lassen
            if (!malIsInvisible) {
                predMalickow.callPredawar(g);
            }
        } else {
            if (talkPerson == 64) {
                // beim Reden
                if (!malIsInvisible) {
                    predMalickow.talkPredawar(g);
                }
            } else {
                if (malIsWalking || malIsGiving) {
                    // bei irgendwelchen Anims
                    if (malIsWalking) {
                        malIsWalking = predMalickow.animPredawar(g, 1);
                    }
                    if (malIsGiving && !malIsInvisible) {
                        malIsGiving = predMalickow.animPredawar(g, 2);
                    }
                } else {
                    // beim Rumstehen
                    if (!malIsInvisible) {
                        predMalickow.drawPredawar(g, malIsListening);
                    }
                }
            }
        }

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

        // hinter Rad ? (nur Clipping - Region wird neugezeichnet)
        if (rectKoleso.isPointInRect(pKrTemp)) {
            g.drawImage(koleso, 337, 271);
        }

        // Ausgabe von AnimText, falls noetig
        if (!Objects.equals(AnimOutputText, "") && !AnimMCLocked) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.imageFont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, COLORS[AnimTalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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

        // Die Anims muessen bedient werden
        if (AnimID != 0 && !AnimMCLocked) {
            doAnims();
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
                // if (brKirche.IsPointInRect (pTemp) == true)
                // {
                // 	// Standard - Sinnloszeug
                // 	nextActionID = 150;
                //	pTemp = Pkirche;
                // }

                // Ausreden fuer predWosuskow oder Stollen kaufen
                if (rePredWosuskow.isPointInRect(pTemp)) {
                    if (mainFrame.whatItem == 40) {
                        nextActionID = 160;
                    } else {
                        nextActionID = 200;
                    }
                    pTemp = pPredWosuskow;
                }

                // Ausreden fuer PredKorejtow
                if (rePredKorejtow.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pPredKorejtow;
                }

                // Ausreden fuer PredMalickow bzw. Sachen zum Faelschen geben
                // Hier darf kein Inventory geaendert oder Actions gesetzt werden, da K noch laeuft (Abbruch mgl.)
                if (rePredMalickow.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 31: // Dowol ohne alles
                            if (!mainFrame.actions[647] && !mainFrame.actions[648]) {
                                nextActionID = 1000;
                            }
                            if (!mainFrame.actions[647] && mainFrame.actions[648]) {
                                nextActionID = 1070;
                            }
                            if (mainFrame.actions[647] && !mainFrame.actions[648]) {
                                nextActionID = 1040;
                            }
                            if (mainFrame.actions[647] && mainFrame.actions[648]) {
                                nextActionID = 1090;
                            }
                            mainFrame.actions[649] = false;
                            break;
                        case 32: // Dowol zygl.
                            if (!mainFrame.actions[647] && !mainFrame.actions[648]) {
                                nextActionID = 1000;
                            }
                            if (!mainFrame.actions[647] && mainFrame.actions[648]) {
                                nextActionID = 1070;
                            }
                            if (mainFrame.actions[647] && !mainFrame.actions[648]) {
                                nextActionID = 1040;
                            }
                            if (mainFrame.actions[647] && mainFrame.actions[648]) {
                                nextActionID = 1090;
                            }
                            mainFrame.actions[649] = true;
                            break;
                        case 45: // Casnik
                            if (!mainFrame.actions[645] && !mainFrame.actions[646] && !mainFrame.actions[647]) {
                                nextActionID = 1020;
                            }
                            if (!mainFrame.actions[645] && !mainFrame.actions[646] && mainFrame.actions[647]) {
                                nextActionID = 1060;
                            }
                            if ((mainFrame.actions[645] || mainFrame.actions[646]) && !mainFrame.actions[647]) {
                                nextActionID = 1050;
                            }
                            if ((mainFrame.actions[645] || mainFrame.actions[646]) && mainFrame.actions[647]) {
                                nextActionID = 1110;
                            }
                            break;
                        case 49: // Prikaz
                            if (!mainFrame.actions[645] && !mainFrame.actions[646] && !mainFrame.actions[648]) {
                                nextActionID = 1010;
                            }
                            if (!mainFrame.actions[645] && !mainFrame.actions[646] && mainFrame.actions[648]) {
                                nextActionID = 1080;
                            }
                            if ((mainFrame.actions[645] || mainFrame.actions[646]) && !mainFrame.actions[648]) {
                                nextActionID = 1030;
                            }
                            if ((mainFrame.actions[645] || mainFrame.actions[646]) && mainFrame.actions[648]) {
                                nextActionID = 1100;
                            }
                            break;
                        default:
                            nextActionID = 210;
                            break;
                    }
                    pTemp = pPredMalickow;
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

                // zu Karta gehen ?
                if (ausgangKarta.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKarta.isPointInRect(kt)) {
                        pTemp = pExitKarta;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitKarta.y);
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

                // PredKorejtow ansehen
                if (rePredKorejtow.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pPredKorejtow;
                }

                // PredWosuskow ansehen
                if (rePredWosuskow.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pPredWosuskow;
                }

                // PredMalickow ansehen
                if (rePredMalickow.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pPredMalickow;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem PredKorejtow reden
                if (rePredKorejtow.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pPredKorejtow);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem PredWosuskow reden
                if (rePredWosuskow.isPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.pathWalker.setNewWay(pPredWosuskow);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem PredMalickow reden
                if (rePredMalickow.isPointInRect(pTemp)) {
                    nextActionID = 52;
                    mainFrame.pathWalker.setNewWay(pPredMalickow);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangKarta.isPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.isBackgroundAnimRunning = false;
                resetAnims();
                mainFrame.isClipSet = false;
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) ||
                    rePredKorejtow.isPointInRect(pTemp) ||
                    rePredMalickow.isPointInRect(pTemp) ||
                    rePredWosuskow.isPointInRect(pTemp);

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
            if (ausgangKarta.isPointInRect(pTemp)) {
                if (cursorShape != 6) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 6;
                }
                return;
            }

            if (rePredKorejtow.isPointInRect(pTemp) ||
                    rePredWosuskow.isPointInRect(pTemp) ||
                    rePredMalickow.isPointInRect(pTemp)) {
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
        resetAnims();
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
                // PredKorejtow anschauen
                krabatSays("StareWiki_1", fKorj, 3, 0, 0);
                break;

            case 2:
                // PredWosuskow anschauen
                krabatSays("StareWiki_2", fWosu, 3, 0, 0);
                break;

            case 3:
                // PredMalickow anschauen
                krabatSays("StareWiki_3", fMali, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC mit PredKorejtow (benutzen)
                mainFrame.isClipSet = false;
                mainFrame.krabat.setFacing(fKorj);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                AnimMCLocked = true;
                resetAnims();
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.actions[580]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 51:
                // Krabat beginnt MC mit PredWosuskow (benutzen)
                mainFrame.isClipSet = false;
                mainFrame.krabat.setFacing(fWosu);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                AnimMCLocked = true;
                resetAnims();
                nextActionID = 700;
                break;

            case 52:
                // Krabat beginnt MC mit PredMalickow (benutzen)
                mainFrame.isClipSet = false;
                mainFrame.krabat.setFacing(fMali);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                AnimMCLocked = true;
                resetAnims();
                nextActionID = 800;
                break;

            case 100:
                // Gehe zu Karta
                createNewLocation(180, locationID);
                break;

            case 155:
                // predKorejtow - Ausreden
                maleExcuse(fKorj);
                break;

            case 160: // Taler uebergeben
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                mainFrame.isClipSet = false;
                AnimMCLocked = true;
                mainFrame.krabat.setFacing(fWosu);
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 135;
                nextActionID = 161;
                break;

            case 161: // warten Auf Ende Anim
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 162;
                }
                break;

            case 162:
                // PredWosuskow sucht Stollen
                wosIsLooking = true;
                nextActionID = 164;
                break;

            case 164:
                // Auf Ende Suchen warten
                if (!wosIsLooking) {
                    nextActionID = 165;
                }
                break;

            case 165:
                // Stollen kaufen
                personSays("StareWiki_4", fWosu, 63, 0, 167, talkPointWosuski);
                // Taler aus Inventar raus und Stollen rein
                mainFrame.inventory.vInventory.removeElement(40);
                mainFrame.inventory.vInventory.addElement(52);
                mainFrame.actions[956] = true;
                break;

            case 167:
                // PredWosuskow gibt Stollen
                wosIsGiving = true;
                nextActionID = 168;
                break;

            case 168:
                // Warten auf Ende Geben
                if (!wosIsGiving) {
                    nextActionID = 170;
                }
                break;

            case 170:
                // Takeanim Krabat
                mainFrame.krabat.nAnimation = 93;
                nextActionID = 175;
                break;

            case 175:
                // Krabat bedankt sich fuer Stollen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                krabatSays("StareWiki_5", 0, 1, 2, 180);
                break;

            case 180:
                // Animationsmodus aus
                nextActionID = 0;
                AnimMCLocked = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 200:
                // predWosuskow - Ausreden
                maleExcuse(fWosu);
                break;

            case 210:
                // predMali - Ausreden
                maleExcuse(fMali);
                break;

            // Marktschreier /////////////////////////////////////////////////

            // Dialog mit PredKorejtow ////////////////////////////////////////////

            case 600:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("StareWiki_55", 1000, 581, new int[]{581}, 610);
                Dialog.extend("StareWiki_56", 581, 1000, null, 611);

                // 2. Frage
                if (!mainFrame.actions[604]) {
                    Dialog.extend("StareWiki_57", 581, 1000, null, 620);
                } else {
                    Dialog.extend("StareWiki_58", 581, 1000, null, 620);
                }

                // 3. Frage
                Dialog.extend("StareWiki_59", 581, 583, new int[]{583}, 630);
                Dialog.extend("StareWiki_60", 583, 582, new int[]{582}, 631);
                Dialog.extend("StareWiki_61", 582, 1000, null, 634);

                // 4. Frage
                Dialog.extend("StareWiki_62", 1000, 1000, null, 910);

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
                // Reaktion PredKorejtow
                personSays("StareWiki_6", 0, 62, 2, 600, talkPointKorejty);
                mainFrame.actions[580] = true; // Flag setzen
                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion PredKorejtow
                personSays("StareWiki_7", 0, 62, 2, 600, talkPointKorejty);
                break;

            case 611:
                // Reaktion PredKorejtow
                personSays("StareWiki_8", 0, 62, 2, 600, talkPointKorejty);
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion PredKorejtow
                personSays("StareWiki_9", 0, 62, 2, 621, talkPointKorejty);
                break;

            case 621:
                // Reaktion PredKorejtow
                personSays("StareWiki_10", 0, 62, 2, 622, talkPointKorejty);
                break;

            case 622:
                // Reaktion PredKorejtow
                personSays("StareWiki_11", 0, 62, 2, 600, talkPointKorejty);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion PredKorejtow
                personSays("StareWiki_12", 0, 62, 2, 600, talkPointKorejty);
                break;

            case 631:
                // Reaktion PredKorejtow
                personSays("StareWiki_13", 0, 62, 2, 632, talkPointKorejty);
                break;

            case 632:
                // Reaktion PredKorejtow
                personSays("StareWiki_14", 0, 62, 2, 633, talkPointKorejty);
                break;

            case 633:
                // Reaktion PredKorejtow
                personSays("StareWiki_15", 0, 62, 2, 600, talkPointKorejty);
                break;

            case 634:
                // Reaktion PredKorejtow
                personSays("StareWiki_16", 0, 62, 2, 635, talkPointKorejty);
                break;

            case 635:
                // Reaktion PredKorejtow
                personSays("StareWiki_17", 0, 62, 2, 636, talkPointKorejty);
                break;

            case 636:
                // Reaktion PredKorejtow
                personSays("StareWiki_18", 0, 62, 2, 637, talkPointKorejty);
                break;

            case 637:
                // Reaktion Krabat
                krabatSays("StareWiki_19", 12, 3, 2, 638);
                break;

            case 638:
                // Reaktion Krabat
                krabatSays("StareWiki_20", 12, 3, 2, 910);
                break;


            // Dialog mit PredWosuskow ////////////////////////////////////////////

            case 700:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("StareWiki_63", 1000, 587, new int[]{587}, 710);
                Dialog.extend("StareWiki_64", 587, 586, new int[]{586}, 711);
                Dialog.extend("StareWiki_65", 586, 585, new int[]{585}, 713);
                Dialog.extend("StareWiki_66", 585, 1000, null, 711);

                // 2. Frage
                Dialog.extend("StareWiki_67", 587, 1000, null, 720);

                // 3. Frage
                Dialog.extend("StareWiki_68", 1000, 1000, null, 910);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 701;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 701:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                // eine Extrawurst: Dialog 1. Frage zurueckschalten, wenn gefragt
                if (mainFrame.actions[585] && nextActionID == 711) {
                    mainFrame.actions[585] = false;
                }

                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 710:
                // Reaktion PredWosuskow
                personSays("StareWiki_21", 0, 63, 2, 700, talkPointWosuski);
                break;

            case 711:
                // Reaktion PredWosuskow
                personSays("StareWiki_22", 0, 63, 2, 712, talkPointWosuski);
                break;

            case 712:
                // Reaktion PredWosuskow
                personSays("StareWiki_23", 0, 63, 2, 700, talkPointWosuski);
                break;

            case 713:
                // Reaktion PredWosuskow
                personSays("StareWiki_24", 0, 63, 2, 714, talkPointWosuski);
                break;

            case 714:
                // Reaktion PredWosuskow
                personSays("StareWiki_25", 0, 63, 2, 700, talkPointWosuski);
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 720:
                // Reaktion PredWosuskow
                personSays("StareWiki_26", 0, 63, 2, 721, talkPointWosuski);
                break;

            case 721:
                // Reaktion Krabat
                krabatSays("StareWiki_27", 0, 1, 2, 910);
                break;

            // Dialog mit PredMalickoscow  ///////////////////////////////////////

            case 800:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("StareWiki_69", 1000, 591, new int[]{591}, 810);
                Dialog.extend("StareWiki_70", 591, 590, new int[]{590}, 811);
                Dialog.extend("StareWiki_71", 590, 1000, null, 812);

                // 2. Frage
                Dialog.extend("StareWiki_72", 1000, 596, new int[]{596}, 820);
                Dialog.extend("StareWiki_73", 596, 595, new int[]{595}, 821);
                Dialog.extend("StareWiki_74", 595, 594, new int[]{594}, 822);
                Dialog.extend("StareWiki_75", 594, 593, new int[]{593}, 823);
                Dialog.extend("StareWiki_76", 593, 592, new int[]{592}, 824);
                Dialog.extend("StareWiki_77", 592, 1000, null, 825);

                // 3. Frage
                Dialog.extend("StareWiki_78", 1000, 1000, null, 830);

                // 4. Frage
                Dialog.extend("StareWiki_79", 1000, 1000, null, 910);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 801;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 801:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                // eine Extrawurst: Dialog 1. Frage zurueckschalten, wenn gefragt
                if (mainFrame.actions[590] && nextActionID == 812) {
                    mainFrame.actions[590] = false;
                }

                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 810:
                // Reaktion PredMalickow
                personSays("StareWiki_28", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 811:
                // Reaktion PredMalickow
                personSays("StareWiki_29", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 812:
                // Reaktion PredMalickow
                personSays("StareWiki_30", 0, 64, 2, 800, talkPointMalicki);
                break;

            // Antworten zu Frage 2 ////////////////////////////

            case 820:
                // Reaktion PredMalickow
                personSays("StareWiki_31", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 821:
                // Reaktion PredMalickow
                personSays("StareWiki_32", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 822:
                // Reaktion PredMalickow
                personSays("StareWiki_33", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 823:
                // Reaktion PredMalickow
                personSays("StareWiki_34", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 824:
                // Reaktion PredMalickow
                personSays("StareWiki_35", 0, 64, 2, 800, talkPointMalicki);
                break;

            case 825:
                // Reaktion PredMalickow
                personSays("StareWiki_36", 0, 64, 2, 800, talkPointMalicki);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 830:
                // Reaktion PredMalickow
                personSays("StareWiki_37", 0, 64, 2, 800, talkPointMalicki);
                break;


            // ENDE ALLER DIALOGE  //////////////////////////////////////

            case 900:
                // Andere Anims beenden, wenn zuende gelabert...
                AnimMCLocked = false;
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 910:
                // MC beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                AnimMCLocked = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            ///// Ab hier die Gebe-Szenen mit dem Faelscher /////////////////////////////////////////////

            case 1000:
                // Dowolnosc zuerst
                if (!mainFrame.actions[592]) // noch kein Dialog gelaufen
                {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    resetAnims();
                    AnimMCLocked = true;
                    personSays("StareWiki_38", 0, 64, 0, 900, talkPointMalicki);
                } else {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    resetAnims();
                    AnimMCLocked = true;
                    mainFrame.isClipSet = false;
                    mainFrame.isInventoryCursor = false;
                    mainFrame.krabat.nAnimation = 136;
                    nextActionID = 1002;
                }
                break;

            case 1002:
                // fertig geben 
                malIsGiving = true;
                nextActionID = 1005;
                break;

            case 1005:
                // Dowolnosc zuerst
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_39", 0, 64, 0, 900, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                break;

            case 1010:
                // Prikaz zuerst
                if (!mainFrame.actions[592]) // noch kein Dialog gelaufen
                {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    resetAnims();
                    AnimMCLocked = true;
                    personSays("StareWiki_40", 0, 64, 0, 900, talkPointMalicki);
                } else {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    resetAnims();
                    AnimMCLocked = true;
                    mainFrame.isClipSet = false;
                    mainFrame.isInventoryCursor = false;
                    mainFrame.krabat.nAnimation = 133;
                    nextActionID = 1012;
                }
                break;

            case 1012:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1015;
                break;

            case 1015:
                // Prikaz zuerst
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_41", 0, 64, 0, 900, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                break;

            case 1020:
                // Casnik zuerst
                if (!mainFrame.actions[592]) // noch kein Dialog gelaufen
                {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    resetAnims();
                    AnimMCLocked = true;
                    personSays("StareWiki_42", 0, 64, 0, 900, talkPointMalicki);
                } else {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    resetAnims();
                    AnimMCLocked = true;
                    mainFrame.isClipSet = false;
                    mainFrame.isInventoryCursor = false;
                    mainFrame.krabat.nAnimation = 132;
                    nextActionID = 1022;
                }
                break;

            case 1022:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1025;
                break;

            case 1025:
                // Casnik zuerst
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_43", 0, 64, 0, 900, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                break;

            case 1030:
                // Dowolnosc schon gegeben und Prikaz drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 133;
                nextActionID = 1032;
                break;

            case 1032:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1035;
                break;

            case 1035:
                // Dowolnosc schon gegeben und Prikaz drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_44", 0, 64, 0, 900, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                break;

            case 1040:
                // Prikaz schon gegeben und Dowolnosc drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 136;
                nextActionID = 1042;
                break;

            case 1042:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1045;
                break;

            case 1045:
                // Prikaz schon gegeben und Dowolnosc drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_45", 0, 64, 0, 900, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                break;

            case 1050:
                // Dowolnosc schon gegeben und Casnik drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 132;
                nextActionID = 1052;
                break;

            case 1052:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1055;
                break;

            case 1055:
                // Dowolnosc schon gegeben und Casnik drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_46", 0, 64, 0, 900, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                break;

            case 1060:
                // Prikaz schon gegeben und Casnik drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 132;
                nextActionID = 1062;
                break;

            case 1062:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1065;
                break;

            case 1065:
                // Prikaz schon gegeben und Casnik drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_47", 0, 64, 0, 900, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                break;

            case 1070:
                // Casnik schon da und Dowolnosc drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 136;
                nextActionID = 1072;
                break;

            case 1072:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1075;
                break;

            case 1075:
                // Casnik schon da und Dowolnosc drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_48", 0, 64, 0, 900, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                break;

            case 1080:
                // Casnik schon da und Prikaz drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 133;
                nextActionID = 1082;
                break;

            case 1082:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1085;
                break;

            case 1085:
                // Casnik schon da und Prikaz drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_49", 0, 64, 0, 900, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                break;

            // Ab hier ist alles komplett und die Unterschrift kann gefaelscht werden //////////////////////////////////////

            case 1090:
                // Casnik Prikaz da, Dowolnosc drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 136;
                nextActionID = 1092;
                malIsListening = true;
                break;

            case 1092:
                // PredMali nimmt  
                malIsGiving = true;
                nextActionID = 1093;
                break;

            case 1093:
                // Ende geben/nehmen
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                Counter = FAELSCHER_WARTEZEIT;
                nextActionID = 1094;
                break;

            case 1094:
                // Weile warten, dann gibt PredMal zurueck
                if (--Counter > 1) {
                    break;
                }
                malIsGiving = true;
                nextActionID = 1095;
                break;

            case 1095:
                // Casnik Prikaz da, Dowolnosc drauf
                if (malIsGiving) {
                    break;
                }
                personSays("StareWiki_50", 0, 64, 0, 1096, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                // nun die unterschriebene Dowolnosc zurueck ins Inventar
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(33);
                } else {
                    // gesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(34);
                }
                break;

            case 1096:
                // Krabat nimmts
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 1097;
                break;

            case 1097:
                // Ende nehmen
                malIsListening = false;
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 900;
                break;

            case 1100:
                // Casnik Dowolnosc da, Prikaz drauf
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 133;
                nextActionID = 1102;
                malIsListening = true;
                break;

            case 1102:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1103;
                break;

            case 1103:
                // waren auf Ende fertig geben, dann Warteschleife
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                Counter = FAELSCHER_WARTEZEIT;
                nextActionID = 1104;
                break;

            case 1104:
                if (--Counter > 1) {
                    break;
                }
                malIsGiving = true;
                nextActionID = 1105;
                break;

            case 1105:
                // Casnik Dowolnosc da, Prikaz drauf
                if (malIsGiving) {
                    break;
                }
                personSays("StareWiki_51", 0, 64, 0, 1106, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                // nun die unterschriebene Dowolnosc zurueck ins Inventar
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(33);
                } else {
                    // gesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(34);
                }
                break;

            case 1106:
                // Krabat nimmts
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 1107;
                break;

            case 1107:
            case 1122:
                // Ende nehmen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 900;
                malIsListening = false;
                break;

            case 1110:
                // Prikaz Dowolnosc da, Casnik drauf, kurze Anim
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                resetAnims();
                AnimMCLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 132;
                malIsListening = true;
                nextActionID = 1111;
                break;

            case 1111:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1112;
                break;

            case 1112:
                // Prikaz Dowolnosc da, Casnik drauf, kurze Anim
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                personSays("StareWiki_52", 0, 64, 2, 1113, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                // nun die unterschriebene Dowolnosc zurueck ins Inventar
                if (!mainFrame.actions[649]) {
                    // ungesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(33);
                } else {
                    // gesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(34);
                }
                break;

            case 1113:
                // PredMal haut ab
                malIsWalking = true;
                nextActionID = 1114;
                break;

            case 1114:
                // predMal nach hinten laufen lassen
                if (!malIsWalking) {
                    nextActionID = 1115;
                    malIsInvisible = true;
                }
                break;

            case 1115:
                // Krabat redet
                krabatSays("StareWiki_53", 0, 1, 2, 1116);
                break;

            case 1116:
                // PredMal kommt wieder
                malIsWalking = true;
                nextActionID = 1117;
                break;

            case 1117:
                if (!malIsWalking) {
                    nextActionID = 1118;
                    malIsInvisible = false;
                    Counter = FAELSCHER_WARTEZEIT;
                }
                break;

            case 1118:
                // PredMal gibt
                if (--Counter > 1) {
                    break;
                }
                malIsGiving = true;
                nextActionID = 1119;
                break;

            case 1119:
                if (!malIsGiving) {
                    nextActionID = 1120;
                }
                break;

            case 1120:
                // Antwort PredMal... 
                personSays("StareWiki_54", 0, 64, 0, 1121, talkPointMalicki);
                break;

            case 1121:
                // Krabat nimmts
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 1122;
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    // Anims: alle schreien rum und versuchen ihren schotter loszuwerden...
    private void doAnims() {
        switch (AnimID) {
            case 5:
                // bisschen warten, damit Mainmenu ohne Probleme
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 10;
                }
                break;

            case 10:
                // Hier einen der gluecklichen Redner auswaehlen
                int zuffi = (int) (Math.random() * 3.9);
                switch (zuffi) {
                    case 0:
                        // es ist mal bisschen Ruhe hier...
                        AnimCounter = 50;
                        break;

                    case 1:
                        // der Pred korejtow ist dran
                        int zuffZahl = (int) (Math.random() * 3.9);
                        switch (zuffZahl) {
                            case 0:
                                AnimOutputText = Start.STRING_MANAGER.getTranslation("StareWiki_80");
                                AnimCounter = 50;
                                break;

                            case 1:
                                AnimOutputText = mainFrame.imageFont.splitTextKey("StareWiki_81");
                                AnimCounter = 70;
                                break;

                            case 2:
                                AnimOutputText = mainFrame.imageFont.splitTextKey("StareWiki_82");
                                AnimCounter = 100;
                                break;

                            case 3:
                                AnimOutputText = mainFrame.imageFont.splitTextKey("StareWiki_83");
                                AnimCounter = 120;
                                break;
                        }
                        AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, talkPointKorejty);
                        AnimTalkPerson = 62;
                        break;

                    case 2:
                        // der pred Wosuskow ist dran
                        int zuffZahl2 = (int) (Math.random() * 2.9);
                        switch (zuffZahl2) {
                            case 0:
                                AnimOutputText = Start.STRING_MANAGER.getTranslation("StareWiki_84");
                                AnimCounter = 70;
                                break;

                            case 1:
                                AnimOutputText = mainFrame.imageFont.splitTextKey("StareWiki_85");
                                AnimCounter = 100;
                                break;

                            case 2:
                                AnimOutputText = Start.STRING_MANAGER.getTranslation("StareWiki_86");
                                AnimCounter = 50;
                                break;
                        }
                        AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, talkPointWosuski);
                        AnimTalkPerson = 63;
                        break;

                    case 3:
                        // der pred Malickow ist dran
                        int zuffZahl3 = (int) (Math.random() * 4.9);
                        switch (zuffZahl3) {
                            case 0:
                                AnimOutputText = mainFrame.imageFont.splitTextKey("StareWiki_87");
                                AnimCounter = 60;
                                break;

                            case 1:
                                AnimOutputText = Start.STRING_MANAGER.getTranslation("StareWiki_88");
                                AnimCounter = 50;
                                break;

                            case 2:
                                AnimOutputText = Start.STRING_MANAGER.getTranslation("StareWiki_89");
                                AnimCounter = 60;
                                break;

                            case 3:
                                AnimOutputText = Start.STRING_MANAGER.getTranslation("StareWiki_90");
                                AnimCounter = 60;
                                break;

                            case 4:
                                AnimOutputText = mainFrame.imageFont.splitTextKey("StareWiki_91");
                                AnimCounter = 70;
                                break;
                        }
                        AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, talkPointMalicki);
                        AnimTalkPerson = 64;
                        break;
                }
                AnimID = 20;
                break;

            case 20:
                // Warten, bis zu Ende geschrien
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 30;
                }
                break;

            case 30:
                mainFrame.isClipSet = false;
                AnimTalkPerson = 0;
                AnimOutputText = "";
                AnimCounter = 10;
                AnimID = 5;
                break;

        }
    }

    private void resetAnims() {
        AnimTalkPerson = 0;
        AnimCounter = 10;
        AnimOutputText = "";
        AnimID = 5;
    }
}


/* Texte 

// Pred. Malickoscow ///////////////////////////////////////////////////////

Aktionen (Krabat gibt entsprech. Gegenst#ande):

To je njepodpisana dowolnos#c. N#etko trjebam hi#s#ce originalne podpismo k p#rirunanju. P#oj zaso sem, hdy#s je ma#s. Dokument sebi tak do#lho zd#der#zu.

Podpismo Awgusta ! Ty sy mi kadli#kka ! Wokomik pro#su - a #sto mi za to da#s ?

D#ostanje#s podpisanu dowolnos#c jeno#z, hdy#z mi n#e#sto wosebite da#s.

Podpismo Awgusta pod jednym p#rikazom. Dobra ideja ! Hd#de dyrbju n#etko podpismo napodobni#c ?

Naru#kny s#l#on#kny #kasnik ! Mamamija, to je droho#cinka. Tysac raz d#dak ! #Sto m#o#zu za tebje #kini#c ? Praj, #sto ma#s na wutrobje ?

Tu ma#s - originalne podpismo krala sej zd#der#zu - snano podpisam raz awtogramowe kartki Awgusta za turistow.

Naru#kny s#l#on#kny #kasnik ! To je narunanja dos#c. Dyrbju j#on hnydom Ha#n#zi pokaza#c.

A m#oj dokument ?

(Kommt zurueck) Wodaj, to sym w hektice cyle zaby#l. Tu ma#s - originalny rukopis krala sej zd#der#zu - snano podpisam raz awtogramowe kartki Awgusta za turistow.

Naru#kny s#l#on#kny #kasnik ! To je narunanja dos#c. N#etko trjebam jeno#z hi#s#ce pobrachowacy d#d#el k napodobnjenju.

Tu ma#s potwjerd#densku. Originalny rukopis krala sej zd#der#zu - snano podpisam raz awtogramowe kartki Awgusta za turistow.


// Michal /////////////////////////////////////////////////////////

Aktion (Krabat gibt Michal Geld):

Pro#su jara - tw#oj wosu#sk. Njech #ci zes#lod#di !
D#dakuju !

*/