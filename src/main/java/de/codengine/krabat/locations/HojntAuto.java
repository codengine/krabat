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

public class HojntAuto extends Mainloc {
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

    // private boolean setAnim = false;
    private Hojnt jaeger;
    private boolean showHojnt = false;
    private boolean walkReady = true;

    private boolean isDoorOpen = false;

    // private boolean baumActive = false;

    private static final int SCROLLWERT = 417;

    private int scrollPosition = 0;
    private boolean setScroll = false;

    private boolean isScrollAnim = true;

    // private static final borderrect obererAusgang  = new borderrect ( 199, 277,  271, 379);
    // private static final borderrect rechterAusgang = new borderrect (1206, 409, 1279, 479);
    // private static final borderrect strauchRect    = new borderrect ( 160, 249,  305, 394);
    // private static final borderrect hakenRect      = new borderrect ( 426, 258,  506, 277);
    // private static final borderrect jamaRect       = new borderrect (1081, 378, 1188, 424);
    // private static final borderrect hoelzerRect    = new borderrect ( 613, 250,  640, 290);
    // private static final borderrect leineRect      = new borderrect (1100, 245, 1155, 362);

    // Konstante Punkte
    private static final GenericPoint Pleft = new GenericPoint(279, 358);
    private static final GenericPoint Pmitte = new GenericPoint(622, 376);
    private static final GenericPoint Pright = new GenericPoint(1076, 448);
    private static final GenericPoint Pin = new GenericPoint(286, 344);

    private int Counter = 0;

    public HojntAuto(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 420;
        mainFrame.krabat.zoomf = 4.4f;
        mainFrame.krabat.defScale = 50;

        jaeger = new Hojnt(mainFrame);
        jaeger.maxx = 0;
        jaeger.zoomf = mainFrame.krabat.zoomf;
        jaeger.defScale = 0;

        hoelzer = new GenericImage[11];
        Mehrmals = MAX_MEHRMALS;

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        // Unterscheidung nach DoActions, was zu tun ist

        // An Hoelzern gezogen Hinlauf
        if (!mainFrame.Actions[216] && !mainFrame.Actions[217] && !mainFrame.Actions[218]) {
            System.out.println("An den Hoelzern gezogen - Hinlauf !");
            jaeger.SetHojntPos(Pin);
            jaeger.SetFacing(3);
            scrollPosition = 0;
            setScroll = true;
            nextActionID = 150;
        }

        // An Strick gezogen Hinlauf
        if (!mainFrame.Actions[216] && mainFrame.Actions[217] && !mainFrame.Actions[218]) {
            System.out.println("Am Strick gezogen Hinlauf !");
            jaeger.SetHojntPos(Pin);
            jaeger.SetFacing(3);
            scrollPosition = 0;
            setScroll = true;
            nextActionID = 100;
        }

        // Ruecklauf
        if (mainFrame.Actions[216] && !mainFrame.Actions[218]) {
            System.out.println("Ruecklauf aus beiden Szenen !");
            isDoorOpen = true;
            jaeger.SetHojntPos(Pright);
            jaeger.SetFacing(9);
            showHojnt = true;
            scrollPosition = SCROLLWERT;
            setScroll = true;
            nextActionID = 500;
        }

        // Szene, wenn Krabat in die Grube faellt
        if (mainFrame.Actions[218]) {
            System.out.println("Szene : K in Grube.");
            jaeger.SetHojntPos(Pin);
            jaeger.SetFacing(3);
            scrollPosition = 210;
            isScrollAnim = false;
            setScroll = true;
            nextActionID = 600;
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
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(985, 1279, 1024, 1279, 442, 463));

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
        // hojnt2 =   getPicture ("gfx/hojnt/hojnt2.gif");
        hojnt3 = getPicture("gfx/hojnt/hojnt3.gif");
        // hojnt4 =   getPicture ("gfx/hojnt/hojnt-r3.gif");
        // baum   =   getPicture ("gfx/hojnt/hojnt-r2.gif");
        // seil   =   getPicture ("gfx/hojnt/seil.gif");

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
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            mainFrame.scrolly = 0;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollx = scrollPosition;
            }
            Cursorform = 200;
            mainFrame.fPlayAnim = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund zeichnen
        g.drawImage(skyl, mainFrame.scrollx / 10, 0);
        g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Parallaxer ausfuehren
        int xtemp = mainFrame.scrollx - 5;
        if (xtemp < 0) {
            xtemp = 0;
        }
        g.setClip(xtemp, 0, 650, 325);
        g.drawImage(skyl, mainFrame.scrollx / 10, 0);
        g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Jaeger Hintergrund loeschen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = jaeger.HojntRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(skyl, mainFrame.scrollx / 10, 0);
            g.drawImage(skyr, mainFrame.scrollx / 10 + 540, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // wenn noetig, dann offene Tuer zeichnen
        if (isDoorOpen) {
            g.setClip(272, 265, 81, 80);
            g.drawImage(offeneTuer, 272, 265);
        }

        // leeren Haken zeichnen, wenn noetig
        if (mainFrame.Actions[905]) {
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
        Debug.DrawRect(g, mainFrame.wegGeher.vBorders);

        // Jaeger bewegen
        if (showHojnt && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = jaeger.Move();
        }

        // Jaeger zeichnen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = jaeger.HojntRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

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
            Borderrect temprect = jaeger.HojntRect();
            int temp = (temprect.lo_point.x + temprect.ru_point.x) / 2;
            temp -= mainFrame.scrollx;

            if (temp < 315) {
                mainFrame.scrollx -= 6;
            }
            if (temp > 325) {
                mainFrame.scrollx += 6;
            }
            if (mainFrame.scrollx < 0) {
                mainFrame.scrollx = 0;
            }
            if (mainFrame.scrollx > SCROLLWERT) {
                mainFrame.scrollx = SCROLLWERT;
            }
        }

        // Cursor neu berechnen, weil Hintergrund verschoben wurde
        evalMouseMoveEvent(mainFrame.Mousepoint);

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
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.Nix);
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

    private void DoAction() {

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 100:
                // Klingelanim der Hoelzer
                klingeln = true;
                mainFrame.wave.PlayFile("sfx/klingel.wav");
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
                mainFrame.Actions[231] = true;
                jaeger.MoveTo(Pleft);
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
                if (mainFrame.sprache == 1) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00000"));
                }
                if (mainFrame.sprache == 2) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00001"));
                }
                if (mainFrame.sprache == 3) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00002"));
                }
                // Hier Position des Textes berechnen
                Borderrect tmp = jaeger.HojntRect();
                GenericPoint tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.lo_point.y - 50);
                outputTextPos = mainFrame.ifont.CenterText(outputText, tTlk);
                TalkPerson = 26;
                nextActionID = 160;
                break;

            case 160:
                // Laufe nach rechts
                jaeger.clearanimpos = false;
                jaeger.MoveTo(Pmitte);
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
                jaeger.clearanimpos = true;
                jaeger.MoveTo(Pright);
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
                NeuesBild(14, 29);
                break;

            case 500:
                // Jaeger laeuft wieder nach links zurueck
                jaeger.clearanimpos = false;
                jaeger.MoveTo(Pmitte);
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
                jaeger.clearanimpos = true;
                jaeger.MoveTo(Pleft);
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
                if (mainFrame.sprache == 1) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00003"));
                }
                if (mainFrame.sprache == 2) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00004"));
                }
                if (mainFrame.sprache == 3) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00005"));
                }
                // Hier Position des Textes berechnen
                Borderrect temp = jaeger.HojntRect();
                GenericPoint tTalk = new GenericPoint((temp.ru_point.x + temp.lo_point.x) / 2, temp.lo_point.y - 50);
                outputTextPos = mainFrame.ifont.CenterText(outputText, tTalk);
                TalkPerson = 26;
                nextActionID = 530;
                break;

            case 530:
                // Jaeger reingehen lassen
                jaeger.MoveTo(Pin);
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
                NeuesBild(14, 29);
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
                mainFrame.wave.PlayFile("sfx/klingel.wav");
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
                jaeger.MoveTo(Pleft);
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
                if (mainFrame.sprache == 1) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00006"));
                }
                if (mainFrame.sprache == 2) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00007"));
                }
                if (mainFrame.sprache == 3) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_HojntAuto_00008"));
                }
                // Hier Position des Textes berechnen
                Borderrect teemp = jaeger.HojntRect();
                GenericPoint tTaalk = new GenericPoint((teemp.ru_point.x + teemp.lo_point.x) / 2, teemp.lo_point.y - 50);
                outputTextPos = mainFrame.ifont.CenterText(outputText, tTaalk);
                TalkPerson = 26;
                nextActionID = 655;
                break;

            case 655:
                // 1 Repaint einfuegen
                nextActionID = 660;
                break;

            case 660:
                // auf Jama umschalten
                NeuesBild(27, 29);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }
}