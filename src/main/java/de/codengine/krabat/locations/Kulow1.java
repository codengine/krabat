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
import de.codengine.krabat.anims.MerchantFish;
import de.codengine.krabat.anims.MerchantGrain;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Kulow1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Kulow1.class);
    private GenericImage backleft;
    private GenericImage backright;
    private GenericImage himmel;
    private GenericImage saeule;
    private GenericImage kulow2;
    private GenericImage ryba;
    private boolean setScroll = false;
    private int scrollwert;

    private MerchantGrain haendler;
    private MerchantFish fischer;
    private final MultipleChoice Dialog;

    private final GenericPoint rybowarTalk;
    private boolean rybowarhoertzu = false;
    private boolean rybowargibtkrosik = false;
    private boolean rybowarsuchtzrawc = false;
    private boolean octopussyerscheint = false;
    private boolean rybowarnimmtfisch = false;

    private boolean wikowarhoertzu = false;
    private final BorderRect wikowarRect;
    private final GenericPoint wikowarTalk;

    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos;
    private int AnimTalkPerson = 0;
    private int AnimID = 0;
    private int AnimCounter = 0;
    private boolean AnimActive = false;
    private boolean AnimLocked = false;

    private int Counter = 0;

    // Konstanten - Rects
    private static final BorderRect untererAusgang = new BorderRect(0, 444, 330, 479);
    private static final BorderRect linkerAusgang = new BorderRect(343, 318, 395, 347);
    private static final BorderRect rechterAusgang = new BorderRect(1240, 407, 1279, 479);
    private static final BorderRect kulow2Rect = new BorderRect(343, 305, 558, 479);
    private static final BorderRect durjelRect = new BorderRect(194, 341, 215, 390);
    private static final BorderRect durjerRect = new BorderRect(865, 347, 898, 429);
    private static final BorderRect synoRect = new BorderRect(998, 407, 1043, 431);
    private static final BorderRect sudobjoRect = new BorderRect(1061, 411, 1090, 443);

    private static final BorderRect rybowarLookRect = new BorderRect(714, 343, 741, 384);
    private static final BorderRect drybaLookRect = new BorderRect(723, 344, 777, 366);

    // Konstante Points
    private static final GenericPoint Pwikowar = new GenericPoint(532, 467);
    private static final GenericPoint Prybowar = new GenericPoint(691, 453);
    private static final GenericPoint rybowarUp = new GenericPoint(701, 339);
    private static final GenericPoint Pleft = new GenericPoint(362, 371);
    private static final GenericPoint Pright = new GenericPoint(1279, 470);
    private static final GenericPoint Pdown = new GenericPoint(144, 479);
    private static final GenericPoint Pdryba = new GenericPoint(758, 458);
    private static final GenericPoint Pdurjel = new GenericPoint(204, 388);
    private static final GenericPoint Pdurjer = new GenericPoint(885, 421);
    private static final GenericPoint Psyno = new GenericPoint(1028, 443);
    private static final GenericPoint Psudobjo = new GenericPoint(1075, 442);
    private static final GenericPoint PwikZita = new GenericPoint(421, 366);
    private static final GenericPoint doorTalk = new GenericPoint(883, 200);

    // Konstante ints
    private static final int fWikowar = 9;
    private static final int fRybowar = 3;
    private static final int fDryba = 12;
    private static final int fGTuer = 12;
    private static final int fOTuer = 12;
    private static final int fSyno = 12;
    private static final int fSudobjo = 12;

    public Kulow1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.enteringFromMap = false;  // hier ohne Bedeutung, Kulow stimmt immer neu an
        BackgroundMusicPlayer.getInstance().playTrack(6, true);

        mainFrame.krabat.maxX = 470;
        mainFrame.krabat.zoomFactor = 1.67f;
        mainFrame.krabat.defaultScale = 0;

        haendler = new MerchantGrain(mainFrame);
        fischer = new MerchantFish(mainFrame);

        rybowarTalk = new GenericPoint();
        rybowarTalk.x = rybowarUp.x + MerchantFish.Breite / 2;
        rybowarTalk.y = rybowarUp.y - 100;

        wikowarTalk = new GenericPoint(PwikZita.x + MerchantGrain.Breite / 2, PwikZita.y - 50);

        wikowarRect = new BorderRect(PwikZita.x, PwikZita.y, PwikZita.x + MerchantGrain.Breite, PwikZita.y + MerchantGrain.Hoehe);

        Dialog = new MultipleChoice(mainFrame);

        initImages();
        cursorShape = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                break;
            case 22: // Aus Cyrkej kommend
                mainFrame.krabat.setPos(new GenericPoint(371, 371));
                mainFrame.krabat.setFacing(6);
                scrollwert = 51;
                setScroll = true;
                break;
            case 11: // Von Polo aus
                mainFrame.krabat.setPos(new GenericPoint(144, 452));
                mainFrame.krabat.setFacing(12);
                scrollwert = 0;
                setScroll = true;
                break;
            case 20: // Von Mertens aus
                mainFrame.krabat.setPos(new GenericPoint(1258, 445));
                mainFrame.krabat.setFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
        }

        initLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 455, 310, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(134, 355, 0, 310, 395, 454));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(370, 424, 318, 481, 371, 394));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(479, 623, 540, 623, 395, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(624, 655, 624, 761, 430, 461));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(624, 462, 813, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(814, 468, 959, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(881, 881, 840, 933, 443, 467));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(960, 468, 1279, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1267, 1279, 985, 1279, 434, 467));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1262, 1271, 1267, 1279, 421, 433));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(11);

        // Wege eintragen
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
        mainFrame.pathFinder.connectPos(4, 5);
        mainFrame.pathFinder.connectPos(3, 5);
        mainFrame.pathFinder.connectPos(5, 6);
        mainFrame.pathFinder.connectPos(6, 7);
        mainFrame.pathFinder.connectPos(6, 8);
        mainFrame.pathFinder.connectPos(8, 9);
        mainFrame.pathFinder.connectPos(9, 10);
    }

    // Bilder vorbereiten
    private void initImages() {
        backleft = getPicture("gfx/kulow/kulow-l.png");
        backright = getPicture("gfx/kulow/kulow-r.png");
        himmel = getPicture("gfx/kulow/kulsky.png");
        saeule = getPicture("gfx/kulow/postsaeule.png");
        kulow2 = getPicture("gfx/kulow/kulow2.png");
        ryba = getPicture("gfx/kulow/ddryba2a.png");

    }

    @Override
    public void cleanup() {
        backleft = null;
        backright = null;
        himmel = null;
        saeule = null;
        kulow2 = null;
        ryba = null;

        haendler.cleanup();
        haendler = null;
        fischer.cleanup();
        fischer = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping - Region initialisieren
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
            AnimLocked = false;
        }

        // Hintergrund zeichnen
        g.drawImage(himmel, mainFrame.scrollX / 6, 0);
        g.drawImage(backleft, 0, 0);
        g.drawImage(backright, 640, 0);

        // Parallax - Scrolling ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollX - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 285);
            g.drawImage(himmel, mainFrame.scrollX / 6, 0);
            g.drawImage(backleft, 0, 0);
            g.drawImage(backright, 640, 0);
        }

        // Parallaxer fuer Saeule, muss immer Hintergrund loeschen ?????
        float xtf = 1180 - (float) (mainFrame.scrollX * 3) / 8;
        int xt = (int) xtf;
        g.setClip(xt - 2, 208, xt + 90, 479);
        g.drawImage(backright, 640, 0);

        // Hintergrund fuer Wikowar loeschen -> sonst loescht der Krabat
        g.setClip(PwikZita.x, PwikZita.y, MerchantGrain.Breite, MerchantGrain.Hoehe);
        g.drawImage(backleft, 0, 0);

        // Ab hier ist Retten des ClipRect sinnlos!!!

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Andere Personen zeichnen

        // Rybowar
        g.setClip(rybowarUp.x, rybowarUp.y, MerchantFish.Breite + 50, MerchantFish.Hoehe);
        g.drawImage(backright, 640, 0);
        if (!AnimLocked) {
            fischer.drawRybowar(g, talkPerson, AnimTalkPerson, rybowarUp,
                    rybowarhoertzu, rybowargibtkrosik, rybowarsuchtzrawc,
                    octopussyerscheint, rybowarnimmtfisch);
        } else {
            fischer.drawRybowar(g, talkPerson, 0, rybowarUp,
                    rybowarhoertzu, rybowargibtkrosik, rybowarsuchtzrawc,
                    octopussyerscheint, rybowarnimmtfisch);
        }

        if (octopussyerscheint) {
            octopussyerscheint = false;
        }

        // Wenn Holzfisch noch da, dann auch zeichnen
        if (!mainFrame.actions[913]) {
            g.setClip(721, 338, 62, 34);
            g.drawImage(ryba, 721, 338);
        }

        // Krabats neue Position festlegen wenn noetig
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
        if (kulow2Rect.isPointInRect(pKrTemp)) {
            g.drawImage(kulow2, 338, 321);
        }

        // Ab hier muss Cliprect wieder gerettet werden

        // Wikowar
        GenericRectangle mx;
        mx = g.getClipBounds();
        g.setClip(PwikZita.x, PwikZita.y, MerchantGrain.Breite, MerchantGrain.Hoehe);
        haendler.drawWikowar(g, PwikZita, talkPerson, wikowarhoertzu);
        g.setClip(mx.getX(), mx.getY(), mx.getWidth(), mx.getHeight());

        // Postsaeule zeichnen, wenn im Bild
        if (mainFrame.scrollX > 320) // Diesen Wert bitte exakt !!
        {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(xt - 2, 208, xt + 90, 479);
            g.drawImage(saeule, xt, 208);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
        }

        // Ausgabe von AnimText, falls noetig
        if (!Objects.equals(AnimOutputText, "") && AnimActive && !AnimLocked) {
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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Die Anims muessen bedient werden
        if (AnimID != 0 && AnimActive && !AnimLocked) {
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

        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            talkPerson = 0;
        }
        outputText = "";

        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollX;

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
            // linke Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                BorderRect tmp = mainFrame.krabat.getBoundingBox();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Wikowar
                if (wikowarRect.isPointInRect(pTemp)) {
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
                    pTemp = Pwikowar;
                }

                // Ausreden fuer Rybowar
                if (rybowarLookRect.isPointInRect(pTemp) && !AnimActive)
                    /*((drybaRect.IsPointInRect (pTemp) == false) || (mainFrame.Actions[913] == true)))*/ {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 205;
                            break;
                        case 14: // ryba
                            nextActionID = 165;
                            break;
                        case 18: // bron
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Prybowar;
                }

                // Ausreden fuer Dryba
                if (drybaLookRect.isPointInRect(pTemp) && !mainFrame.actions[913] &&
                        (!rybowarLookRect.isPointInRect(pTemp) || AnimActive)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 220 : 160;
                    pTemp = Pdryba;
                }

                // Ausreden fuer tuer links
                if (durjelRect.isPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTemp = Pdurjel;
                }

                // Ausreden fuer tuer rechts
                if (durjerRect.isPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = Pdurjer;
                }

                // Ausreden fuer Stroh
                if (synoRect.isPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 230 : 180;
                    pTemp = Psyno;
                }

                // Ausreden fuer Gefaess
                if (sudobjoRect.isPointInRect(pTemp)) {
                    // ryba
                    nextActionID = mainFrame.whatItem == 14 ? 240 : 185;
                    pTemp = Psudobjo;
                }

                // wenn nix ausgewaehlt, dann einfach nur hinlaufen
                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // Gegenstand grundsaetzlich wieder ablegen
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

                // nach Polo gehen
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Mertens gehen
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

                // nach Cyrkej gehen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.isPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        // Hier "up"
                        pTemp = new GenericPoint(kt.x, Pleft.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Wikowar ansehen
                if (wikowarRect.isPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pwikowar;
                }

                // Rybowar ansehen
                if (rybowarLookRect.isPointInRect(pTemp) && !AnimActive)
                    /*((drybaRect.IsPointInRect (pTemp) == false) || (mainFrame.Actions[913] == true)))*/ {
                    nextActionID = 2;
                    pTemp = Prybowar;
                }

                // Dryba ansehen
                if (drybaLookRect.isPointInRect(pTemp) && !mainFrame.actions[913] &&
                        (!rybowarLookRect.isPointInRect(pTemp) || AnimActive)) {
                    nextActionID = 3;
                    pTemp = Pdryba;
                }

                // tuer links ansehen
                if (durjelRect.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Pdurjel;
                }

                // tuer rechts ansehen
                if (durjerRect.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pdurjer;
                }

                // Stroh ansehen
                if (synoRect.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Psyno;
                }

                // Gefaess ansehen
                if (sudobjoRect.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = Psudobjo;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weg nach Polo anschauen
                if (untererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Mertens anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Cyrkej anschauen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Wikowar reden, nicht bei Backgroundanim
                if (wikowarRect.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Pwikowar);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Rybowar reden, nicht bei Backgroundanim
                if (rybowarLookRect.isPointInRect(pTemp) && !AnimActive)
                    /* drybaRect.IsPointInRect (pTemp) == false) || (mainFrame.Actions[913] == true)))*/ {
                    nextActionID = 51;
                    mainFrame.pathWalker.setNewWay(Prybowar);
                    mainFrame.repaint();
                    return;
                }

                // Dryba nehmen
                if (drybaLookRect.isPointInRect(pTemp) && !mainFrame.actions[913] &&
                        (!rybowarLookRect.isPointInRect(pTemp) || AnimActive)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(Pdryba);
                    mainFrame.repaint();
                    return;
                }

                // Tuer links nehmen
                if (durjelRect.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(Pdurjel);
                    mainFrame.repaint();
                    return;
                }

                // Tuer rechts nehmen
                if (durjerRect.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(Pdurjer);
                    mainFrame.repaint();
                    return;
                }

                // Stroh nehmen
                if (synoRect.isPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.setNewWay(Psyno);
                    mainFrame.repaint();
                    return;
                }

                // Gefaess nehmen
                if (sudobjoRect.isPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.pathWalker.setNewWay(Psudobjo);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.isBackgroundAnimRunning = false;
                AnimLocked = true;
                mainFrame.isClipSet = false;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();

            }
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseMoveEvent(pTxxx);
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollX, pTxxx.y + mainFrame.scrollY);

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
                    wikowarRect.isPointInRect(pTemp) ||
                    rybowarLookRect.isPointInRect(pTemp) && !AnimActive ||
                    drybaLookRect.isPointInRect(pTemp) && !mainFrame.actions[913] ||
                    durjelRect.isPointInRect(pTemp) || durjerRect.isPointInRect(pTemp) ||
                    synoRect.isPointInRect(pTemp) || sudobjoRect.isPointInRect(pTemp);

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
            if (rybowarLookRect.isPointInRect(pTemp) && !AnimActive ||
                    wikowarRect.isPointInRect(pTemp) ||
                    drybaLookRect.isPointInRect(pTemp) && !mainFrame.actions[913] ||
                    durjelRect.isPointInRect(pTemp) || durjerRect.isPointInRect(pTemp) ||
                    synoRect.isPointInRect(pTemp) || sudobjoRect.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
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

            if (linkerAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 4;
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

            // sonst normal-Cursor
            if (cursorShape != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                cursorShape = 0;
            }
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
        AnimLocked = true;
        mainFrame.krabat.stopWalking();
    }

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {

        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // System.out.println("Nextaction " + nextActionID);

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
                // Wikowar anschauen
                krabatSays("Kulow1_1", fWikowar, 3, 0, 0);
                break;

            case 2:
                // Rybowar anschauen
                krabatSays("Kulow1_2", fRybowar, 3, 0, 0);
                break;

            case 3:
                // Dryba anschauen
                krabatSays("Kulow1_3", fDryba, 3, 0, 0);
                break;

            case 4:
                // geschlossene Tuer anschauen
                krabatSays("Kulow1_4", fGTuer, 3, 0, 0);
                break;

            case 5:
                // offene Tuer anschauen
                krabatSays("Kulow1_5", fOTuer, 3, 0, 0);
                break;

            case 6:
                // Stroh anschauen
                krabatSays("Kulow1_6", fSyno, 3, 0, 0);
                break;

            case 7:
                // Gefaess anschauen
                krabatSays("Kulow1_7", fSudobjo, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Wikowar benutzen)
                mainFrame.krabat.setFacing(fWikowar);
                if (mainFrame.isScrolling) {
                    return;
                }
                wikowarhoertzu = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 51:
                // Krabat beginnt MC (Rybowar benutzen)
                mainFrame.krabat.setFacing(fRybowar);
                if (mainFrame.isScrolling) {
                    return;
                }
                rybowarhoertzu = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 900;
                break;

            case 55:
                // Dryba benutzen
                if (AnimActive) {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    nextActionID = 56;
                    mainFrame.krabat.setFacing(fDryba);
                    mainFrame.krabat.nAnimation = 120;
                    Counter = 5;
                } else {
                    personSays("Kulow1_8", 0, 33, 0, 0, rybowarTalk);
                }
                break;

            case 56:
                // Ende nimmFischAttrappe
                if (--Counter == 1) {
                    // Auf Gleichzeitigkeit timen !!!
                    mainFrame.isClipSet = false;
                    mainFrame.actions[913] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.inventory.vInventory.addElement(13);
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                nextActionID = 0;
                break;

            case 60:
                // geschl. Tuer benutzen
                krabatSays("Kulow1_9", fGTuer, 3, 0, 0);
                break;

            case 65:
                // offene Tuer benutzen
                // Zuffzahl von 0 bis 2
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        personSays("Kulow1_10", fOTuer, 52, 0, 0, doorTalk);
                        break;

                    case 1:
                        personSays("Kulow1_11", fOTuer, 52, 0, 0, doorTalk);
                        break;

                    case 2:
                        personSays("Kulow1_12", fOTuer, 52, 0, 0, doorTalk);
                        break;
                }
                break;

            case 70:
                // Stroh benutzen
                krabatSays("Kulow1_13", fSyno, 3, 0, 0);
                break;

            case 75:
                // Gefaess benutzen
                int zuffZahl = (int) Math.round(Math.random() * 2);
                if (zuffZahl == 2) {
                    zuffZahl = 0;
                }
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Kulow1_14", fSudobjo, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Kulow1_15", fSudobjo, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // Gehe zu Polo
                createNewLocation(11, 21);
                break;

            case 101:
                // Gehe zu Mertens
                createNewLocation(20, 21);
                break;

            case 102:
                // Gehe zu Cyrkej
                createNewLocation(22, 21);
                break;

            case 150:
                // Wikowar - Ausreden
                maleExcuse(fWikowar);
                break;

            case 155:
                // Rybowar - Ausreden
                maleExcuse(fRybowar);
                break;

            case 160:
                // Dryba - Ausreden
                thingExcuse(fDryba);
                break;

            case 165:
                // Fisch geben
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(fRybowar);
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 148;
                nextActionID = 166;
                break;

            case 166:
                // Fisch wurde ueberreicht
                rybowarnimmtfisch = true;
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                rybowarnimmtfisch = false;
                personSays("Kulow1_16", fRybowar, 33, 2, 167, rybowarTalk);
                break;

            case 167:
                // Fisch wurde ueberreicht
                rybowargibtkrosik = true;
                personSays("Kulow1_17", fRybowar, 33, 0, 168, rybowarTalk);
                mainFrame.inventory.vInventory.removeElement(14);
                mainFrame.inventory.vInventory.addElement(15);
                // Zeichen fuer habe keinen Fisch, aber Groschen -> faengt noch nichts in Haty
                mainFrame.actions[914] = false;
                mainFrame.actions[915] = true;
                break;

            case 168:
                // Krabat nimmt Krosik
                mainFrame.krabat.nAnimation = 30;
                nextActionID = 169;
                break;

            case 169:
                // Ende Krosikueberreichen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                rybowargibtkrosik = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 170:
                // Durjeleft - Ausreden
                thingExcuse(fGTuer);
                break;

            case 175:
                // Durjeright - Ausreden
                thingExcuse(fOTuer);
                break;

            case 180:
                // Stroh - Ausreden
                thingExcuse(fSyno);
                break;

            case 185:
                // Gefaess - Ausreden
                thingExcuse(fSudobjo);
                break;

            case 200:
                // kij auf wikowar
                krabatSays("Kulow1_18", fWikowar, 3, 0, 0);
                break;

            case 205:
                // kij auf rybowar
                krabatSays("Kulow1_19", fRybowar, 3, 0, 0);
                break;

            case 210:
                // bron auf wikowar
                krabatSays("Kulow1_20", fWikowar, 3, 0, 0);
                break;

            case 215:
                // bron auf rybowar
                krabatSays("Kulow1_21", fRybowar, 3, 0, 0);
                break;

            case 220:
                // kamuski auf dryba
                krabatSays("Kulow1_22", fDryba, 3, 0, 0);
                break;

            case 230:
                // kamuski auf syno
                krabatSays("Kulow1_23", fSyno, 3, 0, 0);
                break;

            case 240:
                // ryba auf sudobjo
                krabatSays("Kulow1_24", fSudobjo, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine mit Wikowar
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("Kulow1_39", 1000, 120, new int[]{120}, 610);
                Dialog.extend("Kulow1_40", 120, 121, null, 620);

                // 2. Frage
                Dialog.extend("Kulow1_41", 1000, 122, new int[]{122}, 630);
                Dialog.extend("Kulow1_42", 122, 123, new int[]{123}, 640);
                Dialog.extend("Kulow1_43", 123, 1000, null, 650);

                // 3. Frage
                Dialog.extend("Kulow1_44", 1000, 125, null, 800);
                Dialog.extend("Kulow1_45", 125, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.actions[125] = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                break;

            case 610:
                // Reaktion Wikowar auf 1. Teil 1. Frage
                personSays("Kulow1_25", 0, 32, 2, 600, wikowarTalk);
                break;

            case 620:
                // Reaktion Wikowar auf 2. Teil 1. Frage
                personSays("Kulow1_26", 0, 32, 2, 600, wikowarTalk);
                break;

            case 630:
                // Reaktion Wikowar auf 1. Teil 2. Frage
                personSays("Kulow1_27", 0, 32, 2, 600, wikowarTalk);
                break;

            case 640:
                // Reaktion Wikowar auf 2. Teil 2. Frage
                personSays("Kulow1_28", 0, 32, 2, 600, wikowarTalk);
                break;

            case 650:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                personSays("Kulow1_29", 0, 32, 2, 651, wikowarTalk);
                break;

            case 651:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                personSays("Kulow1_30", 0, 32, 2, 652, wikowarTalk);
                break;

            case 652:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                personSays("Kulow1_31", 0, 32, 2, 653, wikowarTalk);
                break;

            case 653:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                personSays("Kulow1_32", 0, 32, 2, 600, wikowarTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[125] = false;
                mainFrame.isAnimRunning = false;
                wikowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 900:
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("Kulow1_46", 1000, 130, new int[]{130}, 910);
                if (!mainFrame.actions[150]) {
                    Dialog.extend("Kulow1_47", 130, 1000, null, 920);
                } else {
                    Dialog.extend("Kulow1_48", 130, 1000, null, 930);
                }

                // 2. Frage
                Dialog.extend("Kulow1_49", 1000, 132, new int[]{132, 133}, 940);

                // 4. Frage (wenn 2. weg)
                Dialog.extend("Kulow1_50", 133, 1000, null, 950);

                // 3. Frage
                Dialog.extend("Kulow1_51", 1000, 134, null, 1100);
                Dialog.extend("Kulow1_52", 134, 1000, null, 1100);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 901;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 901:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.actions[134] = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                break;

            case 910:
                // Reaktion Rybowar auf 1. Teil 1. Frage
                personSays("Kulow1_33", 0, 33, 2, 900, rybowarTalk);
                break;

            case 920:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K noch nicht in Haty)
                personSays("Kulow1_34", 0, 33, 2, 900, rybowarTalk);
                break;

            case 930:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K in Haty gewesen)
                personSays("Kulow1_35", 0, 33, 2, 931, rybowarTalk);
                break;

            case 931:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K in Haty gewesen)
                personSays("Kulow1_36", 0, 33, 2, 900, rybowarTalk);
                break;

            case 940:
                // Fischer quatscht erstes Mal beim Suchen (2. Frage)
                mainFrame.actions[134] = false;
                personSays("Kulow1_37", 0, 33, 1, 1110, rybowarTalk);
                break;

            case 950:
                // Fischer quatscht naechste Male beim Suchen (4. Frage)
                mainFrame.actions[134] = false;
                personSays("Kulow1_38", 0, 33, 1, 1120, rybowarTalk);
                break;

            case 1100:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[134] = false;
                mainFrame.isAnimRunning = false;
                rybowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 1110:
                // MC beenden und 1. Animation starten
                AnimActive = true;
                mainFrame.isAnimRunning = false;
                rybowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                AnimID = 1;
                mainFrame.repaint();
                break;

            case 1120:
                // MC beenden und 2. Animation starten
                AnimActive = true;
                mainFrame.isAnimRunning = false;
                rybowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                AnimID = 500;
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }

    private void doAnims() {
        switch (AnimID) {
            case 1:
                // Figur verschwinden lassen und bisschen warten
                rybowarsuchtzrawc = true;
                AnimCounter = 20;
                AnimID = 2;
                break;

            case 2:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 3;
                }
                break;

            case 3:
                // 1. Textausgabe
                AnimOutputText = Start.STRING_MANAGER.getTranslation("Kulow1_55");
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 10;
                AnimTalkPerson = 33;
                AnimID = 4;
                break;

            case 4:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 5;
                }
                break;

            case 5:
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimCounter = 10;
                AnimTalkPerson = 0;
                AnimID = 6;
                break;

            case 6:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 7;
                }
                break;

            case 7:
                // 2. Textausgabe
                AnimOutputText = Start.STRING_MANAGER.getTranslation("Kulow1_56");
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 10;
                AnimTalkPerson = 33;
                AnimID = 8;
                break;

            case 8:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 9;
                }
                break;

            case 9:
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimCounter = 10;
                AnimTalkPerson = 0;
                AnimID = 10;
                break;

            case 10:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 23;
                }
                break;

            case 23:
                // 6. Textausgabe (Person wieder aufrichten...
                rybowarsuchtzrawc = false;
                AnimOutputText = mainFrame.imageFont.splitTextKey("Kulow1_53");
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 20;
                AnimTalkPerson = 33;
                AnimID = 24;
                break;

            case 24:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 25;
                }
                break;

            case 25:
            case 508:
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimCounter = 0;
                AnimTalkPerson = 0;
                AnimID = 0;
                AnimActive = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 500:
                // Figur verschwinden lassen und bisschen warten
                AnimCounter = 90;
                rybowarsuchtzrawc = true;
                AnimID = 501;
                break;

            case 501:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 502;
                }
                break;

            case 502:
                // 1. Textausgabe
                int zuffi = 1;
                if (!mainFrame.actions[138]) {
                    mainFrame.actions[138] = true;
                    zuffi = 0;
                }
                switch (zuffi) {
                    case 0:
                        octopussyerscheint = true;
                        mainFrame.soundPlayer.playFile("sfx/oktopus.wav");
                        AnimOutputText = Start.STRING_MANAGER.getTranslation("Kulow1_57");
                        break;

                    case 1:
                        AnimOutputText = Start.STRING_MANAGER.getTranslation("Kulow1_58");
                        break;
                }
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 25;
                AnimTalkPerson = 33;
                AnimID = 503;
                break;

            case 503:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 504;
                }
                break;

            case 504:
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimCounter = 50;
                AnimTalkPerson = 0;
                AnimID = 505;
                break;

            case 505:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 506;
                }
                break;

            case 506:
                // 2. Textausgabe (Person wieder aufrichten...
                rybowarsuchtzrawc = false;
                AnimOutputText = mainFrame.imageFont.splitTextKey("Kulow1_54");
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 30;
                AnimTalkPerson = 33;
                AnimID = 507;
                break;

            case 507:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 508;
                }
                break;

        }
    }
}