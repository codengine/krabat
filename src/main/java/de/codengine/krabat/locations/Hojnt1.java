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

public class Hojnt1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Hojnt1.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage skyl;
    private GenericImage skyr;
    private GenericImage hojnt2;
    private GenericImage hojnt4;
    private GenericImage seil;
    private GenericImage baum;
    private GenericImage hoelzerback;
    private GenericImage offeneTuer;
    private GenericImage leftschatten;
    private GenericImage rightschatten;
    private GenericImage vorder;
    private GenericImage vorder2;
    private GenericImage fallevor;
    private boolean setScroll = false;
    private int scrollwert;

    private final GenericImage[] hoelzer;
    private int HolzCount = 0;
    private boolean klingeln = false;
    private int Mehrmals;
    private static final int MAX_MEHRMALS = 1;

    private boolean setAnim = false;
    private int AnimFlag = 0;
    private Hunter jaeger;
    private boolean showHojnt = false;
    private boolean walkReady = true;
    private boolean isDoorOpen = false;

    private boolean baumActive = false;
    private int Zaehl;
    private static final int MAX_ZAEHL = 200;

    private boolean isLeft;
    private int xpos;
    private static final int MIN_SCHATTENX = 340;
    private static final int MAX_SCHATTENX = 620;
    private static final int MOVE = 4;
    private int Verhinderwandern;
    private static final int MAX_VERHINDERWANDERN = 5;

    private int WhichItem = 0;

    private boolean istJaegerGebueckt = false;

    private static final BorderRect obererAusgang = new BorderRect(199, 277, 271, 379);
    private static final BorderRect rechterAusgang = new BorderRect(1206, 409, 1279, 479);
    private static final BorderRect strauchRect = new BorderRect(160, 249, 305, 394);
    private static final BorderRect hakenRect = new BorderRect(426, 258, 506, 277);
    private static final BorderRect jamaRect = new BorderRect(1081, 378, 1188, 424);
    private static final BorderRect hoelzerRect = new BorderRect(613, 250, 640, 290);
    private static final BorderRect leineRect = new BorderRect(1100, 245, 1155, 362);
    private static final BorderRect brMega = new BorderRect(741, 0, 1279, 479);
    private static final BorderRect kurotwy1Rect = new BorderRect(438, 278, 454, 311);
    private static final BorderRect kurotwy2Rect = new BorderRect(480, 278, 507, 311);
    private static final BorderRect wokno1Rect = new BorderRect(378, 267, 420, 307);
    private static final BorderRect wokno2Rect = new BorderRect(523, 267, 565, 309);
    private static final BorderRect sekeraRect = new BorderRect(145, 188, 237, 220);
    private static final BorderRect durjeRect = new BorderRect(273, 266, 303, 339);
    private static final BorderRect drjewoRect = new BorderRect(0, 150, 84, 428);

    // Konstante Punkte
    private static final GenericPoint Pup = new GenericPoint(246, 350);
    private static final GenericPoint Pright = new GenericPoint(1279, 452);
    private static final GenericPoint Phaken = new GenericPoint(413, 359);
    private static final GenericPoint Pjama = new GenericPoint(1119, 421);
    private static final GenericPoint Phoelzer = new GenericPoint(587, 357);
    private static final GenericPoint Pbaum = new GenericPoint(737, 337);
    private static final GenericPoint PvorBaum = new GenericPoint(557, 367);
    private static final GenericPoint Pmega = new GenericPoint(740, 400);
    private static final GenericPoint Pkurotwy1 = new GenericPoint(447, 351);
    private static final GenericPoint Pkurotwy2 = new GenericPoint(494, 343);
    private static final GenericPoint Pwokno1 = new GenericPoint(399, 350);
    private static final GenericPoint Pwokno2 = new GenericPoint(543, 347);
    private static final GenericPoint Psekera = new GenericPoint(412, 396);
    private static final GenericPoint Pdurje = new GenericPoint(277, 351);
    private static final GenericPoint Pdrjewo = new GenericPoint(284, 365);
    private static final GenericPoint PinJama = new GenericPoint(1134, 406);

    // Konstante Ints
    private static final int fHaken = 12;
    private static final int fJama = 12;
    private static final int fLeine = 12;
    private static final int fHoelzer = 3;
    private static final int fKurotwy = 12;
    private static final int fWokno = 12;
    private static final int fSekera = 9;
    private static final int fDurje = 3;
    private static final int fDrjewo = 9;

    public Hojnt1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 379;
        mainFrame.krabat.zoomf = 0.93f;
        mainFrame.krabat.defScale = -15;

        jaeger = new Hunter(mainFrame);
        jaeger.maxx = mainFrame.krabat.maxx;
        jaeger.zoomf = mainFrame.krabat.zoomf;
        jaeger.defScale = mainFrame.krabat.defScale;

        hoelzer = new GenericImage[11];
        Mehrmals = MAX_MEHRMALS;
        Zaehl = MAX_ZAEHL;
        xpos = (int) (Math.random() * (MAX_SCHATTENX - MIN_SCHATTENX - 10) + MIN_SCHATTENX + 5);
        isLeft = (int) (Math.random() * 50) > 25;
        Verhinderwandern = MAX_VERHINDERWANDERN;

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                if (mainFrame.actions[231]) {
                    Zaehl = 50;
                    // isDoorOpen = true;
                }
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;

            case 13: // Aus Wjes kommend
                mainFrame.krabat.setPos(new GenericPoint(1243, 458));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;

            case 15: // Von Njedz aus
                mainFrame.krabat.setPos(new GenericPoint(256, 354));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;

            case 27: // aus Jama raus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(1134, 440));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setAnim = true;
                AnimFlag = 10;
                jaeger.setPos(new GenericPoint(1036, 428));
                jaeger.SetFacing(3);
                showHojnt = true;
                setScroll = true;
                break;

            case 29: // aus der Animszene Hojnth
                mainFrame.krabat.setPos(Pbaum);
                mainFrame.krabat.SetFacing(9);
                baumActive = true;
                scrollwert = 417;
                setAnim = true;
                setScroll = true;
                if (mainFrame.actions[216]) {
                    AnimFlag = 30;
                } else {
                    AnimFlag = 20;
                }
                break;
        }

        InitLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(229, 265, 267, 340, 350, 364));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(285, 606, 361, 642, 365, 383));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(605, 663, 715, 927, 384, 410));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(716, 996, 857, 1138, 411, 441));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(985, 1279, 1117, 1279, 442, 463));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx/hojnt/hojnt-l.png");
        backr = getPicture("gfx/hojnt/hojnt-r.png");
        skyl = getPicture("gfx/hojnt/hsky-l.png");
        skyr = getPicture("gfx/hojnt/hsky-r.png");
        hojnt2 = getPicture("gfx/hojnt/hojnt2.png");
        hojnt4 = getPicture("gfx/hojnt/hojnt-r3.png");
        baum = getPicture("gfx/hojnt/hojnt-r2.png");
        seil = getPicture("gfx/hojnt/seil.png");

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

        leftschatten = getPicture("gfx/hojnt/ho-lschatten.png");
        rightschatten = getPicture("gfx/hojnt/ho-rschatten.png");
        vorder = getPicture("gfx/hojnt/jwokna.png");
        vorder2 = getPicture("gfx/hojnt/jwokna2.png");

        fallevor = getPicture("gfx/hojnt/hojnt-r4.png");

    }

    @Override
    public void cleanup() {
        backl = null;
        backr = null;
        skyl = null;
        skyr = null;
        hojnt2 = null;
        hojnt4 = null;
        baum = null;
        seil = null;

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

        leftschatten = null;
        rightschatten = null;
        vorder = null;
        vorder2 = null;

        fallevor = null;

        jaeger.cleanup();
        jaeger = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Haken wurde aufgehoben!!!!!!!!!
        if (mainFrame.krabat.fAnimHelper) {
            if (WhichItem == 5) {
                mainFrame.inventory.vInventory.addElement(5);
                mainFrame.isClipSet = false;
                mainFrame.krabat.fAnimHelper = false;
                mainFrame.actions[905] = true;
                WhichItem = 0;
            }
        }

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }
            Cursorform = 200;
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Counter fuer Take Haken
        if (mainFrame.actions[231]) {
            Zaehl--;
            if (Zaehl < 1) {
                Zaehl = MAX_ZAEHL;
                setAnim = true;
                mainFrame.isAnimRunning = true;
                AnimFlag = 40;
                mainFrame.actions[231] = false;
                mainFrame.actions[232] = true;
            }
        }

        // Hintergrund zeichnen
        g.drawImage(skyl, mainFrame.scrollX / 10, 0);
        g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Jaeger Hintergrund loeschen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = jaeger.getRect();

            // normales Cliprectloeschen
            if (!istJaegerGebueckt) {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 20,
                        temp.ru_point.y - temp.lo_point.y + 20);
            }

            // groesseres Cliprect weg wegen Buecken
            else {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 50,
                        temp.ru_point.y - temp.lo_point.y + 50);
            }

            // Zeichne Hintergrund neu
            g.drawImage(skyl, mainFrame.scrollX / 10, 0);
            g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // Parallaxer ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollX - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 325);
            g.drawImage(skyl, mainFrame.scrollX / 10, 0);
            g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // wenn Jaeger in Huette, dann Schatten wandern lassen
        if (!mainFrame.actions[231] && !mainFrame.actions[232]) {
            if (--Verhinderwandern < 1) {
                Verhinderwandern = MAX_VERHINDERWANDERN;
                if (!isLeft) {
                    xpos += MOVE;
                    if (xpos > MAX_SCHATTENX) {
                        isLeft = true;
                    }
                } else {
                    xpos -= MOVE;
                    if (xpos < MIN_SCHATTENX) {
                        isLeft = false;
                    }
                }
            }
            g.setClip(340, 246, 273, 83);
            g.drawImage(backl, 0, 0);
            if (isLeft) {
                g.drawImage(leftschatten, xpos, 280);
            } else {
                g.drawImage(rightschatten, xpos, 280);
            }
            g.drawImage(!mainFrame.actions[905] ? vorder2 : vorder, 340, 246);
        }

        // wenn noetig, dann offene Tuer zeichnen
        if (isDoorOpen) {
            g.setClip(272, 265, 81, 80);
            g.drawImage(offeneTuer, 272, 265);
        }

        // kaputte Grube zeichnen, wenn noetig, sonst Strick einfuegen
        if (mainFrame.actions[230]) {
            g.setClip(1092, 189, 71, 227);
            g.drawImage(hojnt4, 1092, 189);
        } else {
            g.setClip(1122, 191, 6, 106);
            g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
            g.drawImage(backr, 640, 0);
            g.drawImage(seil, 1122, 191);
        }

        // leeren Haken zeichnen, wenn noetig
        if (mainFrame.actions[905]) {
            g.setClip(425, 257, 25, 25);
            g.drawImage(vorder, 340, 246);
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
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Jaeger bewegen
        if (showHojnt && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = jaeger.Move();
        }

        // Jaeger zeichnen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = jaeger.getRect();

            // normales Cliprectloeschen
            if (!istJaegerGebueckt) {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 20,
                        temp.ru_point.y - temp.lo_point.y + 20);
            }

            // groesseres Cliprect weg wegen Buecken
            else {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 50,
                        temp.ru_point.y - temp.lo_point.y + 50);
            }

            // Zeichne sie jetzt

            // Redet sie etwa gerade ??
            if (TalkPerson == 26 && mainFrame.talkCount > 0) {
                jaeger.talkHojnt(g);
            }

            // nur rumstehen oder laufen
            else {
                // normal zeichnen
                if (!istJaegerGebueckt) {
                    jaeger.drawHojnt(g);
                }

                // Bueckphase zeichnen (schaltet sich selbst ab)
                else {
                    istJaegerGebueckt = jaeger.bueckeHojnt(g);
                }
            }
        }

        // Krabats neue Position festlegen wenn noetig
        mainFrame.pathWalker.GeheWeg();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Extrawurst - Vordergrund vor in die Grube reinfallen
            if (mainFrame.krabat.nAnimation == 145) {
                g.drawImage(fallevor, 1098, 401);
            }

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.mousePoint);
            }
        } else {
            if (mainFrame.talkCount > 0 && TalkPerson != 0) {
                // beim Reden
                switch (TalkPerson) {
                    case 1:
                        // Krabat spricht gestikulierend
                        mainFrame.krabat.talkKrabat(g);
                        break;
                    case 3:
                        // Krabat spricht im Monolog
                        mainFrame.krabat.describeKrabat(g);
                        break;
                    default:
                        // Jaeger spricht, also steht Krabat nur da
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

        // hinterm Brunnen (nur Clipping - Region wird neugezeichnet)
        if (strauchRect.IsPointInRect(pKrTemp)) {
            g.drawImage(hojnt2, 143, 262);
        }

        // hinter Baum, wenn Verstecken aktiv
        if (baumActive) {
            g.drawImage(baum, 652, 186);
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            if (AnimFlag == 10) {
                nextActionID = 200;
            }
            if (AnimFlag == 20) {
                nextActionID = 300;
            }
            if (AnimFlag == 30) {
                nextActionID = 400;
            }
            if (AnimFlag == 40) {
                nextActionID = 350;
            }
        }

        // Gibt es was zu tun ? Achtung! Scrolling - Verriegelung in DoActions extra !!!
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
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
            TalkPerson = 0;
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

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Hier kommt Routine hin, die Standard - Ausredeantworten auswaehlt, je nach Gegenstand
                // in Location, damit Textposition bekannt ist!!!

                // Ausreden fuer Haken
                if (hakenRect.IsPointInRect(pTemp) && !mainFrame.actions[905]) {
                    // kij
                    nextActionID = mainFrame.whatItem == 2 ? 650 : 150;
                    pTemp = Phaken;
                }

                // Ausreden fuer Grube
                if (jamaRect.IsPointInRect(pTemp)) {
                    // beim 1. Mal reinfallen
                    if (!mainFrame.actions[908]) {
                        nextActionID = 700;
                    } else {
                        // nur Ausreden
                        nextActionID = 155;
                    }
                    pTemp = Pjama;
                }

                // an Leine ziehen mit Stock oder sowas
                if (leineRect.IsPointInRect(pTemp)) {
                    // beim 1. Mal immer reinfallen
                    if (!mainFrame.actions[908]) {
                        nextActionID = 700;
                    } else {
                        switch (mainFrame.whatItem) {
                            case 2: // kij
                            case 7: // wuda
                            case 9: // wuda+hocka
                            case 10: // wuda + wacka
                            case 11: // wuda + dryba
                                if (!mainFrame.actions[905]) {
                                    mainFrame.actions[216] = false;
                                    mainFrame.actions[217] = true;
                                    mainFrame.actions[218] = false;
                                    nextActionID = 180;
                                } else {
                                    nextActionID = 660;
                                }
                                break;
                            default:
                                nextActionID = 165;
                                break;
                        }
                    }
                    pTemp = Pjama;
                }

                // an Hoelzern mit Gegenstand, immer Ausrede
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    nextActionID = 166;
                    pTemp = Phoelzer;
                }

                // Ausrede fuer Rebhuehner1
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 420;
                    pTemp = Pkurotwy1;
                }

                // Ausrede fuer Rebhuehner2
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 420;
                    pTemp = Pkurotwy2;
                }

                // Ausrede fuer Schatten (Fenster1)
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                        case 18: // bron
                            nextActionID = 670;
                            break;
                        default:
                            nextActionID = 430;
                            break;
                    }
                    pTemp = Pwokno1;
                }

                // Ausrede fuer Schatten (Fenster2)
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                        case 18: // bron
                            nextActionID = 670;
                            break;
                        default:
                            nextActionID = 430;
                            break;
                    }
                    pTemp = Pwokno2;
                }

                // Ausrede fuer Axt
                if (sekeraRect.IsPointInRect(pTemp)) {
                    nextActionID = 440;
                    pTemp = Psekera;
                }

                // Ausrede fuer Tuer
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 450;
                    pTemp = Pdurje;
                }

                // Ausrede fuer Drjewo
                if (drjewoRect.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 680 : 460;
                    pTemp = Pdrjewo;
                }

                // hier K abfangen, wenn bei Anim und will zum Jaeger gehen
                // muss als letztes in der Liste stehen !!!!
                if (mainFrame.actions[231] && brMega.IsPointInRect(pTemp)) {
                    nextActionID = 320;
                    pTemp = Pmega;
                }

                // wenn nix ausgewaehlt, dann einfach nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // Gegenstand grundsaetzlich wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // nach Njedz gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Wjes gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Haken ansehen
                if (hakenRect.IsPointInRect(pTemp) && !mainFrame.actions[905]) {
                    nextActionID = 1;
                    pTemp = Phaken;
                }

                // Jama ansehen
                if (jamaRect.IsPointInRect(pTemp)) {
                    if (!mainFrame.actions[908]) {
                        nextActionID = 700;
                    } else {
                        nextActionID = 2;
                    }
                    pTemp = Pjama;
                }

                // Leine ansehen
                if (leineRect.IsPointInRect(pTemp)) {
                    if (!mainFrame.actions[908]) {
                        nextActionID = 690;
                    } else {
                        nextActionID = 3;
                    }
                    pTemp = Pjama;
                }

                // Hoelzer ansehen
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Phoelzer;
                }

                // Rebhuehner1 ansehen
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pkurotwy1;
                }

                // Rebhuehner2 ansehen
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pkurotwy2;
                }

                // Schatten (Fenster1) ansehen
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Pwokno1;
                }

                // Schatten (Fenster2) ansehen
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Pwokno2;
                }

                // Axt ansehen
                if (sekeraRect.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = Psekera;
                }

                // Tuer ansehen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTemp = Pdurje;
                }

                // Drjewo ansehen
                if (drjewoRect.IsPointInRect(pTemp)) {
                    nextActionID = 9;
                    pTemp = Pdrjewo;
                }

                // hier K abfangen, wenn bei Anim und will zum Jaeger gehen
                // muss als letztes in der Liste stehen !!!
                if (mainFrame.actions[231] && brMega.IsPointInRect(pTemp)) {
                    nextActionID = 320;
                    pTemp = Pmega;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weg nach Wjes anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Njedz anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Haken mitnehmen ?
                if (hakenRect.IsPointInRect(pTemp) &&
                        !mainFrame.actions[905]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Phaken);
                    mainFrame.repaint();
                    return;
                }

                // Jama mitnehmen ?
                if (jamaRect.IsPointInRect(pTemp)) {
                    if (!mainFrame.actions[908]) {
                        nextActionID = 700;
                    } else {
                        nextActionID = 55;
                    }
                    mainFrame.pathWalker.SetzeNeuenWeg(Pjama);
                    mainFrame.repaint();
                    return;
                }

                // Leine benutzen geht nicht !
                if (leineRect.IsPointInRect(pTemp)) {
                    // beim 1. Mal reinfallen
                    if (!mainFrame.actions[908]) {
                        nextActionID = 690;
                    } else {
                        nextActionID = 53;
                    }
                    mainFrame.pathWalker.SetzeNeuenWeg(Pjama);
                    mainFrame.repaint();
                    return;
                }

                // Hoelzer benutzen endet in Ausrede
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    // wenn in Animszene, dann kommt Jaeger zurueck
                    if (mainFrame.actions[231]) {
                        nextActionID = 320;
                    } else {
                        nextActionID = 190;
                    }
                    mainFrame.pathWalker.SetzeNeuenWeg(Phoelzer);
                    mainFrame.repaint();
                    return;
                }

                // Kurotwy1 mitnehmen
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 600;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pkurotwy1);
                    mainFrame.repaint();
                    return;
                }

                // Kurotwy2 mitnehmen
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 600;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pkurotwy2);
                    mainFrame.repaint();
                    return;
                }

                // Wokno1 mitnehmen
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 610;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwokno1);
                    mainFrame.repaint();
                    return;
                }

                // Wokno2 mitnehmen
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 610;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwokno2);
                    mainFrame.repaint();
                    return;
                }

                // Sekera mitnehmen
                if (sekeraRect.IsPointInRect(pTemp)) {
                    // wenn in Animszene, dann kommt Jaeger zurueck
                    if (mainFrame.actions[231]) {
                        nextActionID = 320;
                    } else {
                        nextActionID = 620;
                    }
                    mainFrame.pathWalker.SetzeNeuenWeg(Psekera);
                    mainFrame.repaint();
                    return;
                }

                // Durje mitnehmen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 630;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pdurje);
                    mainFrame.repaint();
                    return;
                }

                // Drjewo mitnehmen
                if (drjewoRect.IsPointInRect(pTemp)) {
                    nextActionID = 640;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pdrjewo);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();

            }
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // Wenn Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollX, pTxxx.y + mainFrame.scrollY);

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    hakenRect.IsPointInRect(pTemp) && !mainFrame.actions[905] ||
                    jamaRect.IsPointInRect(pTemp) || leineRect.IsPointInRect(pTemp) ||
                    hoelzerRect.IsPointInRect(pTemp) ||
                    kurotwy1Rect.IsPointInRect(pTemp) || kurotwy2Rect.IsPointInRect(pTemp) ||
                    wokno1Rect.IsPointInRect(pTemp) || wokno2Rect.IsPointInRect(pTemp) ||
                    sekeraRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp) ||
                    drjewoRect.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.isInventoryHighlightCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (Cursorform != 11 && mainFrame.isInventoryHighlightCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
                }
                return;
            }

            if (hakenRect.IsPointInRect(pTemp) && !mainFrame.actions[905] ||
                    jamaRect.IsPointInRect(pTemp) || leineRect.IsPointInRect(pTemp) ||
                    hoelzerRect.IsPointInRect(pTemp) ||
                    kurotwy1Rect.IsPointInRect(pTemp) || kurotwy2Rect.IsPointInRect(pTemp) ||
                    wokno1Rect.IsPointInRect(pTemp) || wokno2Rect.IsPointInRect(pTemp) ||
                    sekeraRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp) ||
                    drjewoRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                Cursorform = 0;
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
            Keyclear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            Keyclear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            Keyclear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void Keyclear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.StopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {

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
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Haken anschauen
                KrabatSagt("Hojnt1_1", fHaken, 3, 0, 0);
                break;

            case 2:
                // Jama anschauen
                KrabatSagt("Hojnt1_2", fJama, 3, 0, 0);
                break;

            case 3:
                // Leine anschauen
                KrabatSagt("Hojnt1_3", fLeine, 3, 0, 0);
                break;

            case 4:
                // Hoelzer anschauen
                KrabatSagt("Hojnt1_4", fHoelzer, 3, 0, 0);
                break;

            case 5:
                // Kurotwy anschauen
                KrabatSagt("Hojnt1_5", fKurotwy, 3, 0, 0);
                break;

            case 6:
                // Wokno anschauen
                if (!mainFrame.actions[231]) {
                    KrabatSagt("Hojnt1_6", fWokno, 3, 0, 0);
                } else {
                    KrabatSagt("Hojnt1_7", fWokno, 3, 0, 0);
                }
                break;

            case 7:
                // Sekera anschauen
                KrabatSagt("Hojnt1_8", fSekera, 3, 0, 0);
                break;

            case 8:
                // Durje anschauen
                KrabatSagt("Hojnt1_9", fDurje, 3, 0, 0);
                break;

            case 9:
                // Drjewo anschauen
                KrabatSagt("Hojnt1_10", fDrjewo, 3, 0, 0);
                break;

            case 50:
                // Haken mitnehmen
                if (mainFrame.actions[231]) {
                    mainFrame.krabat.SetFacing(fHaken);
                    mainFrame.krabat.nAnimation = 120;
                    WhichItem = 5;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    nextActionID = 0;
                } else {
                    KrabatSagt("Hojnt1_11", fHaken, 3, 0, 0);
                }
                break;

            case 53:
                // Leine mitnehmen
                KrabatSagt("Hojnt1_12", fLeine, 3, 0, 0);
                break;

            case 55:
                // Jama mitnehmen
                KrabatSagt("Hojnt1_13", fJama, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Njedz
                mainFrame.actions[231] = false;
                mainFrame.actions[232] = false;
                NeuesBild(15, 14);
                break;

            case 101:
                // Gehe zu Wjes
                mainFrame.actions[231] = false;
                mainFrame.actions[232] = false;
                NeuesBild(13, 14);
                break;

            case 150:
                // Haken - Ausreden
                DingAusrede(fHaken);
                break;

            case 155:
                // Jama - Ausreden
                DingAusrede(fJama);
                break;

            // Reinfallen in Grube und Skip zum Jaeger
            case 160:
                // hier erstmal hinlaufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(PinJama);
                nextActionID = 161;
                break;

            case 161:
                // falle in Grube rein (Anim)
                mainFrame.soundPlayer.PlayFile("sfx/pasle1.wav");
                mainFrame.krabat.SetFacing(fJama);
                WhichItem = 0;
                mainFrame.krabat.nAnimation = 145;
                mainFrame.isInventoryCursor = false;
                mainFrame.actions[230] = true;  // Grube ist kaputt, wenn K reinfaellt
                mainFrame.isClipSet = false; // Neuzeichnen, weil jetzt Grube offen...
                nextActionID = 162;
                break;

            case 162:
                // warten Ende anim
                if (mainFrame.krabat.fAnimHelper) {
                    nextActionID = 163;
                }
                break;

            case 163:
                // Falle in Jama rein
                mainFrame.krabat.fAnimHelper = false;
                mainFrame.krabat.StopAnim();
                mainFrame.actions[216] = false;
                mainFrame.actions[217] = false;
                mainFrame.actions[218] = true;
                NeuesBild(29, 14);
                break;

            case 165:
                // Leine - Ausreden
                DingAusrede(fLeine);
                break;

            case 166:
                // Hoelzer - Ausreden
                DingAusrede(fHoelzer);
                break;

            case 170:
                // Klingelanim der Hoelzer
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                klingeln = true;
                nextActionID = 171;
                break;

            case 171:
                // Warten auf Ende Klingeln
                if (!klingeln) {
                    nextActionID = 175;
                }
                break;

            case 175:
                // laufe hinter Baum
                baumActive = true;
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Pbaum);
                nextActionID = 180;
                break;

            // Hier Actions fuer Beginn LeineZiehAnim
            case 180:
                // Anim "Ziehe mit Stock an Leine"
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 6;
                mainFrame.krabat.SetFacing(fLeine);
                nextActionID = 182;
                break;

            case 182:
                // warten auf Ende Anim
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 185;
                }
                break;

            case 185:
                // umschalten auf Hojnth - Animlocation
                NeuesBild(29, 14);
                break;

            case 190:
                // Ausrede, wenn vor dem Jaegerhaus die Hoelzer benutzt werden sollen
                KrabatSagt("Hojnt1_14", fHoelzer, 3, 0, 0);
                break;

            // Einsprung, wenn gerade aus Grube geholt
            case 200:
                // Sich bei Jaeger bedanken
                KrabatSagt("Hojnt1_15", 9, 1, 2, 210);
                break;

            case 210:
                // Jaeger sagt Spruch
                // Hier Position des Textes berechnen
                BorderRect tmp = jaeger.getRect();
                GenericPoint tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.lo_point.y - 50);
                PersonSagt("Hojnt1_16", 0, 26, 2, 215, tTlk);
                break;

            case 215:
                // Laufe zur Falle
                jaeger.MoveTo(new GenericPoint(1079, 413));
                walkReady = false;
                nextActionID = 220;
                break;

            case 220:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 225;
                }
                break;

            case 225:
                // Jaeger zur Falle buecken lassen
                istJaegerGebueckt = true;
                nextActionID = 227;
                break;

            case 227:
                // Falle wieder in Ordnung bringen
                if (istJaegerGebueckt) {
                    break;
                }
                mainFrame.soundPlayer.PlayFile("sfx/pasle2.wav");
                mainFrame.actions[230] = false;
                mainFrame.isClipSet = false;
                nextActionID = 230;
                break;

            case 230:
                // Laufe wieder weg
                jaeger.clearanimpos = false;
                jaeger.MoveTo(new GenericPoint(738, 405));
                walkReady = false;
                nextActionID = 235;
                break;

            case 235:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 240;
                }
                break;

            case 240:
                // Aus Bild rauslaufen
                jaeger.clearanimpos = true;
                jaeger.MoveTo(new GenericPoint(590, 367));
                walkReady = false;
                nextActionID = 245;
                break;

            case 245:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 250;
                }
                break;

            case 250:
                // Jaeger wieder verschwinden lassen
                showHojnt = false;
                mainFrame.isClipSet = false;
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            // Animfolge: Jaeger ist vorbei, jetzt Zeit zum TakeHaken
            case 300:
                // Krabat erscheint wieder und kann jetzt 20 Sek. lang Haken nehmen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(PvorBaum);
                isDoorOpen = true;
                nextActionID = 310;
                mainFrame.actions[231] = true;
                break;

            case 310:
                // alles deaktivieren und Zeit laeuft !
                baumActive = false;
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 320:
                // bei Klick zu weit nach rechts zaehler loeschen, damit Anim losgeht
                Zaehl = 1;
                break;

            // Animfolge: Jaeger kommt zurueck
            case 350:
                // Achtung - Spruch aufsagen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                KrabatSagt("Hojnt1_17", 6, 3, 0, 355);
                break;

            case 355:
                // ok, jaeger kommt zurueck
                mainFrame.pathWalker.SetzeNeuenWeg(Phoelzer);
                nextActionID = 360;
                break;

            case 360:
                // wieder verstecken
                baumActive = true;
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Pbaum);
                nextActionID = 370;
                mainFrame.actions[216] = true;
                break;

            case 370:
                // Skip zur Anim
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.actions[216] = true;
                mainFrame.actions[217] = false;
                mainFrame.actions[218] = false;
                mainFrame.actions[232] = false;
                NeuesBild(29, 14);
                break;

            // Animfolge: Jaeger ist zurueckgekommen, Ende
            case 400:
                // Krabat erscheint wieder und aus der Traum
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(PvorBaum);
                nextActionID = 410;
                break;

            case 410:
                // Alles beenden
                baumActive = false;
                mainFrame.actions[231] = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 420:
                // Rebhuehner - Ausreden
                DingAusrede(fKurotwy);
                break;

            case 430:
                // Wokno - Ausreden
                DingAusrede(fWokno);
                break;

            case 440:
                // Sekera - Ausreden
                DingAusrede(fSekera);
                break;

            case 450:
                // Durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 460:
                // Drjewo - Ausreden
                DingAusrede(fDrjewo);
                break;

            case 600:
                // Kurotwy mitnehmen
                if (!mainFrame.actions[231]) {
                    KrabatSagt("Hojnt1_18", fKurotwy, 3, 0, 0);
                } else {
                    KrabatSagt("Hojnt1_19", fKurotwy, 3, 0, 0);
                }
                break;

            case 610:
                // Wokno mitnehmen
                if (!mainFrame.actions[231]) {
                    KrabatSagt("Hojnt1_20", fWokno, 3, 0, 0);
                } else {
                    KrabatSagt("Hojnt1_21", fWokno, 3, 0, 0);
                }
                break;

            case 620:
                // Sekera mitnehmen
                KrabatSagt("Hojnt1_22", fSekera, 3, 0, 0);
                break;

            case 630:
                // Durje mitnehmen
                if (!mainFrame.actions[231]) {
                    KrabatSagt("Hojnt1_23", fDurje, 3, 0, 0);
                } else {
                    KrabatSagt("Hojnt1_24", fDurje, 3, 0, 0);
                }
                break;

            case 640:
                // Drjewo mitnehmen
                KrabatSagt("Hojnt1_25", fDrjewo, 3, 0, 0);
                break;

            case 650:
                // kij auf hocka
                KrabatSagt("Hojnt1_26", fHaken, 3, 0, 0);
                break;

            case 660:
                // kij auf lajna, wenn haken schon genommen
                KrabatSagt("Hojnt1_27", fLeine, 3, 0, 0);
                break;

            case 670:
                // kamuski und bron auf wokno
                KrabatSagt("Hojnt1_28", fWokno, 3, 0, 0);
                break;

            case 680:
                // kamuski auf drjewo
                KrabatSagt("Hojnt1_29", fDrjewo, 3, 0, 0);
                break;

            case 690:
                // leine 1. Mal anschauen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                KrabatSagt("Hojnt1_30", fLeine, 3, 0, 160);
                break;

            case 700:
                // Grube 1. Mal anschauen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                KrabatSagt("Hojnt1_31", fLeine, 3, 0, 160);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}