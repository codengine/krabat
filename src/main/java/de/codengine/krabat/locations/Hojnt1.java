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
import de.codengine.krabat.anims.Hojnt;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Hojnt1 extends Mainloc {
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
    private Hojnt jaeger;
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

    private static final Borderrect obererAusgang = new Borderrect(199, 277, 271, 379);
    private static final Borderrect rechterAusgang = new Borderrect(1206, 409, 1279, 479);
    private static final Borderrect strauchRect = new Borderrect(160, 249, 305, 394);
    private static final Borderrect hakenRect = new Borderrect(426, 258, 506, 277);
    private static final Borderrect jamaRect = new Borderrect(1081, 378, 1188, 424);
    private static final Borderrect hoelzerRect = new Borderrect(613, 250, 640, 290);
    private static final Borderrect leineRect = new Borderrect(1100, 245, 1155, 362);
    private static final Borderrect brMega = new Borderrect(741, 0, 1279, 479);
    private static final Borderrect kurotwy1Rect = new Borderrect(438, 278, 454, 311);
    private static final Borderrect kurotwy2Rect = new Borderrect(480, 278, 507, 311);
    private static final Borderrect wokno1Rect = new Borderrect(378, 267, 420, 307);
    private static final Borderrect wokno2Rect = new Borderrect(523, 267, 565, 309);
    private static final Borderrect sekeraRect = new Borderrect(145, 188, 237, 220);
    private static final Borderrect durjeRect = new Borderrect(273, 266, 303, 339);
    private static final Borderrect drjewoRect = new Borderrect(0, 150, 84, 428);

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
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 379;
        mainFrame.krabat.zoomf = 0.93f;
        mainFrame.krabat.defScale = -15;

        jaeger = new Hojnt(mainFrame);
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
                if (mainFrame.Actions[231]) {
                    Zaehl = 50;
                    // isDoorOpen = true;
                }
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;

            case 13: // Aus Wjes kommend
                mainFrame.krabat.SetKrabatPos(new GenericPoint(1243, 458));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;

            case 15: // Von Njedz aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(256, 354));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;

            case 27: // aus Jama raus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(1134, 440));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setAnim = true;
                AnimFlag = 10;
                jaeger.SetHojntPos(new GenericPoint(1036, 428));
                jaeger.SetFacing(3);
                showHojnt = true;
                setScroll = true;
                break;

            case 29: // aus der Animszene Hojnth
                mainFrame.krabat.SetKrabatPos(Pbaum);
                mainFrame.krabat.SetFacing(9);
                baumActive = true;
                scrollwert = 417;
                setAnim = true;
                setScroll = true;
                if (mainFrame.Actions[216]) {
                    AnimFlag = 30;
                } else {
                    AnimFlag = 20;
                }
                break;
        }

        InitLocation();

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.wegGeher.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(229, 265, 267, 340, 350, 364));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(285, 606, 361, 642, 365, 383));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(605, 663, 715, 927, 384, 410));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(716, 996, 857, 1138, 411, 441));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(985, 1279, 1117, 1279, 442, 463));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx/hojnt/hojnt-l.gif");
        backr = getPicture("gfx/hojnt/hojnt-r.gif");
        skyl = getPicture("gfx/hojnt/hsky-l.gif");
        skyr = getPicture("gfx/hojnt/hsky-r.gif");
        hojnt2 = getPicture("gfx/hojnt/hojnt2.gif");
        hojnt4 = getPicture("gfx/hojnt/hojnt-r3.gif");
        baum = getPicture("gfx/hojnt/hojnt-r2.gif");
        seil = getPicture("gfx/hojnt/seil.gif");

        offeneTuer = getPicture("gfx/hojnt/jdurje.gif");

        hoelzer[0] = getPicture("gfx/hojnt/k0.gif");
        hoelzer[1] = getPicture("gfx/hojnt/k1.gif");
        hoelzer[2] = getPicture("gfx/hojnt/k2.gif");
        hoelzer[3] = getPicture("gfx/hojnt/k3.gif");
        hoelzer[4] = getPicture("gfx/hojnt/k4.gif");
        hoelzer[5] = getPicture("gfx/hojnt/k5.gif");
        hoelzer[6] = getPicture("gfx/hojnt/k6.gif");
        hoelzer[7] = getPicture("gfx/hojnt/k7.gif");
        hoelzer[8] = getPicture("gfx/hojnt/k8.gif");
        hoelzer[9] = getPicture("gfx/hojnt/k9.gif");
        hoelzer[10] = getPicture("gfx/hojnt/k10.gif");

        hoelzerback = getPicture("gfx/hojnt/kb.gif");

        leftschatten = getPicture("gfx/hojnt/ho-lschatten.gif");
        rightschatten = getPicture("gfx/hojnt/ho-rschatten.gif");
        vorder = getPicture("gfx/hojnt/jwokna.gif");
        vorder2 = getPicture("gfx/hojnt/jwokna2.gif");

        fallevor = getPicture("gfx/hojnt/hojnt-r4.gif");

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
                mainFrame.Clipset = false;
                mainFrame.krabat.fAnimHelper = false;
                mainFrame.Actions[905] = true;
                WhichItem = 0;
            }
        }

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollx = scrollwert;
            }
            Cursorform = 200;
            if (setAnim) {
                mainFrame.fPlayAnim = true;
            }
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isAnim = true;
        }

        // Counter fuer Take Haken
        if (mainFrame.Actions[231]) {
            Zaehl--;
            if (Zaehl < 1) {
                Zaehl = MAX_ZAEHL;
                setAnim = true;
                mainFrame.fPlayAnim = true;
                AnimFlag = 40;
                mainFrame.Actions[231] = false;
                mainFrame.Actions[232] = true;
            }
        }

        // Hintergrund zeichnen
        g.drawImage(skyl, mainFrame.scrollx / 10, 0, null);
        g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0, null);
        g.drawImage(backl, 0, 0, null);
        g.drawImage(backr, 640, 0, null);

        // Jaeger Hintergrund loeschen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = jaeger.HojntRect();

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
            g.drawImage(skyl, mainFrame.scrollx / 10, 0, null);
            g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0, null);
            g.drawImage(backl, 0, 0, null);
            g.drawImage(backr, 640, 0, null);
        }

        // Parallaxer ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 325);
            g.drawImage(skyl, mainFrame.scrollx / 10, 0, null);
            g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0, null);
            g.drawImage(backl, 0, 0, null);
            g.drawImage(backr, 640, 0, null);
        }

        // wenn Jaeger in Huette, dann Schatten wandern lassen
        if (!mainFrame.Actions[231] && !mainFrame.Actions[232]) {
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
            g.drawImage(backl, 0, 0, null);
            if (isLeft) {
                g.drawImage(leftschatten, xpos, 280, null);
            } else {
                g.drawImage(rightschatten, xpos, 280, null);
            }
            g.drawImage(!mainFrame.Actions[905] ? vorder2 : vorder, 340, 246, null);
        }

        // wenn noetig, dann offene Tuer zeichnen
        if (isDoorOpen) {
            g.setClip(272, 265, 81, 80);
            g.drawImage(offeneTuer, 272, 265, null);
        }

        // kaputte Grube zeichnen, wenn noetig, sonst Strick einfuegen
        if (mainFrame.Actions[230]) {
            g.setClip(1092, 189, 71, 227);
            g.drawImage(hojnt4, 1092, 189, null);
        } else {
            g.setClip(1122, 191, 6, 106);
            g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0, null);
            g.drawImage(backr, 640, 0, null);
            g.drawImage(seil, 1122, 191, null);
        }

        // leeren Haken zeichnen, wenn noetig
        if (mainFrame.Actions[905]) {
            g.setClip(425, 257, 25, 25);
            g.drawImage(vorder, 340, 246, null);
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
            g.drawImage(hoelzerback, 613, 263, null);
            g.drawImage(hoelzer[HolzCount], 608, 239, null);
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Jaeger bewegen
        if (showHojnt && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = jaeger.Move();
        }

        // Jaeger zeichnen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = jaeger.HojntRect();

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
        mainFrame.wegGeher.GeheWeg();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Extrawurst - Vordergrund vor in die Grube reinfallen
            if (mainFrame.krabat.nAnimation == 145) {
                g.drawImage(fallevor, 1098, 401, null);
            }

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // hinterm Brunnen (nur Clipping - Region wird neugezeichnet)
        if (strauchRect.IsPointInRect(pKrTemp)) {
            g.drawImage(hojnt2, 143, 262, null);
        }

        // hinter Baum, wenn Verstecken aktiv
        if (baumActive) {
            g.drawImage(baum, 652, 186, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
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
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollx;

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // linke Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Hier kommt Routine hin, die Standard - Ausredeantworten auswaehlt, je nach Gegenstand
                // in Location, damit Textposition bekannt ist!!!

                // Ausreden fuer Haken
                if (hakenRect.IsPointInRect(pTemp) && !mainFrame.Actions[905]) {
                    // kij
                    nextActionID = mainFrame.whatItem == 2 ? 650 : 150;
                    pTemp = Phaken;
                }

                // Ausreden fuer Grube
                if (jamaRect.IsPointInRect(pTemp)) {
                    // beim 1. Mal reinfallen
                    if (!mainFrame.Actions[908]) {
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
                    if (!mainFrame.Actions[908]) {
                        nextActionID = 700;
                    } else {
                        switch (mainFrame.whatItem) {
                            case 2: // kij
                            case 7: // wuda
                            case 9: // wuda+hocka
                            case 10: // wuda + wacka
                            case 11: // wuda + dryba
                                if (!mainFrame.Actions[905]) {
                                    mainFrame.Actions[216] = false;
                                    mainFrame.Actions[217] = true;
                                    mainFrame.Actions[218] = false;
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
                if (mainFrame.Actions[231] && brMega.IsPointInRect(pTemp)) {
                    nextActionID = 320;
                    pTemp = Pmega;
                }

                // wenn nix ausgewaehlt, dann einfach nur hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // Gegenstand grundsaetzlich wieder ablegen
                mainFrame.invCursor = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Wjes gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Haken ansehen
                if (hakenRect.IsPointInRect(pTemp) && !mainFrame.Actions[905]) {
                    nextActionID = 1;
                    pTemp = Phaken;
                }

                // Jama ansehen
                if (jamaRect.IsPointInRect(pTemp)) {
                    if (!mainFrame.Actions[908]) {
                        nextActionID = 700;
                    } else {
                        nextActionID = 2;
                    }
                    pTemp = Pjama;
                }

                // Leine ansehen
                if (leineRect.IsPointInRect(pTemp)) {
                    if (!mainFrame.Actions[908]) {
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
                if (mainFrame.Actions[231] && brMega.IsPointInRect(pTemp)) {
                    nextActionID = 320;
                    pTemp = Pmega;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
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
                        !mainFrame.Actions[905]) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Phaken);
                    mainFrame.repaint();
                    return;
                }

                // Jama mitnehmen ?
                if (jamaRect.IsPointInRect(pTemp)) {
                    if (!mainFrame.Actions[908]) {
                        nextActionID = 700;
                    } else {
                        nextActionID = 55;
                    }
                    mainFrame.wegGeher.SetzeNeuenWeg(Pjama);
                    mainFrame.repaint();
                    return;
                }

                // Leine benutzen geht nicht !
                if (leineRect.IsPointInRect(pTemp)) {
                    // beim 1. Mal reinfallen
                    if (!mainFrame.Actions[908]) {
                        nextActionID = 690;
                    } else {
                        nextActionID = 53;
                    }
                    mainFrame.wegGeher.SetzeNeuenWeg(Pjama);
                    mainFrame.repaint();
                    return;
                }

                // Hoelzer benutzen endet in Ausrede
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    // wenn in Animszene, dann kommt Jaeger zurueck
                    if (mainFrame.Actions[231]) {
                        nextActionID = 320;
                    } else {
                        nextActionID = 190;
                    }
                    mainFrame.wegGeher.SetzeNeuenWeg(Phoelzer);
                    mainFrame.repaint();
                    return;
                }

                // Kurotwy1 mitnehmen
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 600;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pkurotwy1);
                    mainFrame.repaint();
                    return;
                }

                // Kurotwy2 mitnehmen
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 600;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pkurotwy2);
                    mainFrame.repaint();
                    return;
                }

                // Wokno1 mitnehmen
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 610;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwokno1);
                    mainFrame.repaint();
                    return;
                }

                // Wokno2 mitnehmen
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 610;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwokno2);
                    mainFrame.repaint();
                    return;
                }

                // Sekera mitnehmen
                if (sekeraRect.IsPointInRect(pTemp)) {
                    // wenn in Animszene, dann kommt Jaeger zurueck
                    if (mainFrame.Actions[231]) {
                        nextActionID = 320;
                    } else {
                        nextActionID = 620;
                    }
                    mainFrame.wegGeher.SetzeNeuenWeg(Psekera);
                    mainFrame.repaint();
                    return;
                }

                // Durje mitnehmen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 630;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurje);
                    mainFrame.repaint();
                    return;
                }

                // Drjewo mitnehmen
                if (drjewoRect.IsPointInRect(pTemp)) {
                    nextActionID = 640;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdrjewo);
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
        if (mainFrame.fPlayAnim || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    hakenRect.IsPointInRect(pTemp) && !mainFrame.Actions[905] ||
                    jamaRect.IsPointInRect(pTemp) || leineRect.IsPointInRect(pTemp) ||
                    hoelzerRect.IsPointInRect(pTemp) ||
                    kurotwy1Rect.IsPointInRect(pTemp) || kurotwy2Rect.IsPointInRect(pTemp) ||
                    wokno1Rect.IsPointInRect(pTemp) || wokno2Rect.IsPointInRect(pTemp) ||
                    sekeraRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp) ||
                    drjewoRect.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.invHighCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if (Cursorform != 11 && mainFrame.invHighCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if (hakenRect.IsPointInRect(pTemp) && !mainFrame.Actions[905] ||
                    jamaRect.IsPointInRect(pTemp) || leineRect.IsPointInRect(pTemp) ||
                    hoelzerRect.IsPointInRect(pTemp) ||
                    kurotwy1Rect.IsPointInRect(pTemp) || kurotwy2Rect.IsPointInRect(pTemp) ||
                    wokno1Rect.IsPointInRect(pTemp) || wokno2Rect.IsPointInRect(pTemp) ||
                    sekeraRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp) ||
                    drjewoRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim) {
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
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
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

            evalMouseMoveEvent(mainFrame.Mousepoint);

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
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00000"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00001"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00002"),
                        fHaken, 3, 0, 0);
                break;

            case 2:
                // Jama anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00003"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00004"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00005"),
                        fJama, 3, 0, 0);
                break;

            case 3:
                // Leine anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00006"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00007"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00008"),
                        fLeine, 3, 0, 0);
                break;

            case 4:
                // Hoelzer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00009"), Start.stringManager.getTranslation("Loc1_Hojnt1_00010"), Start.stringManager.getTranslation("Loc1_Hojnt1_00011"),
                        fHoelzer, 3, 0, 0);
                break;

            case 5:
                // Kurotwy anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00012"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00013"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00014"),
                        fKurotwy, 3, 0, 0);
                break;

            case 6:
                // Wokno anschauen
                if (!mainFrame.Actions[231]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00015"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00016"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00017"),
                            fWokno, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00018"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00019"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00020"),
                            fWokno, 3, 0, 0);
                }
                break;

            case 7:
                // Sekera anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00021"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00022"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00023"),
                        fSekera, 3, 0, 0);
                break;

            case 8:
                // Durje anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00024"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00025"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00026"),
                        fDurje, 3, 0, 0);
                break;

            case 9:
                // Drjewo anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00027"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00028"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00029"),
                        fDrjewo, 3, 0, 0);
                break;

            case 50:
                // Haken mitnehmen
                if (mainFrame.Actions[231]) {
                    mainFrame.krabat.SetFacing(fHaken);
                    mainFrame.krabat.nAnimation = 120;
                    WhichItem = 5;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    nextActionID = 0;
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00030"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00031"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00032"),
                            fHaken, 3, 0, 0);
                }
                break;

            case 53:
                // Leine mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00033"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00034"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00035"),
                        fLeine, 3, 0, 0);
                break;

            case 55:
                // Jama mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00036"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00037"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00038"),
                        fJama, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Njedz
                mainFrame.Actions[231] = false;
                mainFrame.Actions[232] = false;
                NeuesBild(15, 14);
                break;

            case 101:
                // Gehe zu Wjes
                mainFrame.Actions[231] = false;
                mainFrame.Actions[232] = false;
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
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(PinJama);
                nextActionID = 161;
                break;

            case 161:
                // falle in Grube rein (Anim)
                mainFrame.wave.PlayFile("sfx/pasle1.wav");
                mainFrame.krabat.SetFacing(fJama);
                WhichItem = 0;
                mainFrame.krabat.nAnimation = 145;
                mainFrame.invCursor = false;
                mainFrame.Actions[230] = true;  // Grube ist kaputt, wenn K reinfaellt
                mainFrame.Clipset = false; // Neuzeichnen, weil jetzt Grube offen...
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
                mainFrame.Actions[216] = false;
                mainFrame.Actions[217] = false;
                mainFrame.Actions[218] = true;
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
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(Pbaum);
                nextActionID = 180;
                break;

            // Hier Actions fuer Beginn LeineZiehAnim
            case 180:
                // Anim "Ziehe mit Stock an Leine"
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00039"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00040"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00041"),
                        fHoelzer, 3, 0, 0);
                break;

            // Einsprung, wenn gerade aus Grube geholt
            case 200:
                // Sich bei Jaeger bedanken
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00042"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00043"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00044"),
                        9, 1, 2, 210);
                break;

            case 210:
                // Jaeger sagt Spruch
                // Hier Position des Textes berechnen
                Borderrect tmp = jaeger.HojntRect();
                GenericPoint tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.lo_point.y - 50);
                PersonSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00045"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00046"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00047"),
                        0, 26, 2, 215, tTlk);
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
                mainFrame.wave.PlayFile("sfx/pasle2.wav");
                mainFrame.Actions[230] = false;
                mainFrame.Clipset = false;
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
                mainFrame.Clipset = false;
                nextActionID = 0;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            // Animfolge: Jaeger ist vorbei, jetzt Zeit zum TakeHaken
            case 300:
                // Krabat erscheint wieder und kann jetzt 20 Sek. lang Haken nehmen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(PvorBaum);
                isDoorOpen = true;
                nextActionID = 310;
                mainFrame.Actions[231] = true;
                break;

            case 310:
                // alles deaktivieren und Zeit laeuft !
                baumActive = false;
                nextActionID = 0;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 320:
                // bei Klick zu weit nach rechts zaehler loeschen, damit Anim losgeht
                Zaehl = 1;
                break;

            // Animfolge: Jaeger kommt zurueck
            case 350:
                // Achtung - Spruch aufsagen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00048"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00049"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00050"),
                        6, 3, 0, 355);
                break;

            case 355:
                // ok, jaeger kommt zurueck
                mainFrame.wegGeher.SetzeNeuenWeg(Phoelzer);
                nextActionID = 360;
                break;

            case 360:
                // wieder verstecken
                baumActive = true;
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(Pbaum);
                nextActionID = 370;
                mainFrame.Actions[216] = true;
                break;

            case 370:
                // Skip zur Anim
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.Actions[216] = true;
                mainFrame.Actions[217] = false;
                mainFrame.Actions[218] = false;
                mainFrame.Actions[232] = false;
                NeuesBild(29, 14);
                break;

            // Animfolge: Jaeger ist zurueckgekommen, Ende
            case 400:
                // Krabat erscheint wieder und aus der Traum
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(PvorBaum);
                nextActionID = 410;
                break;

            case 410:
                // Alles beenden
                baumActive = false;
                mainFrame.Actions[231] = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                if (!mainFrame.Actions[231]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00051"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00052"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00053"),
                            fKurotwy, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00054"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00055"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00056"),
                            fKurotwy, 3, 0, 0);
                }
                break;

            case 610:
                // Wokno mitnehmen
                if (!mainFrame.Actions[231]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00057"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00058"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00059"),
                            fWokno, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00060"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00061"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00062"),
                            fWokno, 3, 0, 0);
                }
                break;

            case 620:
                // Sekera mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00063"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00064"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00065"),
                        fSekera, 3, 0, 0);
                break;

            case 630:
                // Durje mitnehmen
                if (!mainFrame.Actions[231]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00066"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00067"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00068"),
                            fDurje, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00069"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00070"),
                            Start.stringManager.getTranslation("Loc1_Hojnt1_00071"),
                            fDurje, 3, 0, 0);
                }
                break;

            case 640:
                // Drjewo mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00072"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00073"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00074"),
                        fDrjewo, 3, 0, 0);
                break;

            case 650:
                // kij auf hocka
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00075"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00076"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00077"),
                        fHaken, 3, 0, 0);
                break;

            case 660:
                // kij auf lajna, wenn haken schon genommen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00078"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00079"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00080"),
                        fLeine, 3, 0, 0);
                break;

            case 670:
                // kamuski und bron auf wokno
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00081"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00082"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00083"),
                        fWokno, 3, 0, 0);
                break;

            case 680:
                // kamuski auf drjewo
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00084"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00085"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00086"),
                        fDrjewo, 3, 0, 0);
                break;

            case 690:
                // leine 1. Mal anschauen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00087"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00088"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00089"),
                        fLeine, 3, 0, 160);
                break;

            case 700:
                // Grube 1. Mal anschauen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Hojnt1_00090"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00091"),
                        Start.stringManager.getTranslation("Loc1_Hojnt1_00092"),
                        fLeine, 3, 0, 160);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }
}