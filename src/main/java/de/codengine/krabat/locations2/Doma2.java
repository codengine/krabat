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

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Husa;
import de.codengine.krabat.anims.Mac;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Doma2 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Doma2.class);
    private GenericImage background1;
    private GenericImage background2;
    private GenericImage back;
    private GenericImage brunnen;
    private GenericImage blatt;

    private final GenericImage[] Rauchanim;
    private int Rauchcount = 1;

    private int FadeToBlack = 0;

    private boolean switchanim = true;
    private Mac mutter;
    private boolean setScroll = false;
    private int scrollwert;
    private boolean Animation;

    private Husa gans1;
    private Husa gans2;
    private Husa gans3;

    private final GenericPoint MacTalk;
    private final GenericPoint Pmac;
    // private borderrect brMac;
    private boolean istMutterZuSehen = true;

    // Texte
    private static final String[] KRABAT_TEXTS = {"Doma2_27", "Doma2_28", "Doma2_29", "Doma2_30", "Doma2_31"};
    private int Position = 0;

    // Walkto - Points fuer Dinge in Location
    private static final GenericPoint Pmutter = new GenericPoint(840, 400);
    private static final GenericPoint Ptuer = new GenericPoint(885, 355);
    private static final GenericPoint Pschild = new GenericPoint(338, 369);
    private static final GenericPoint Pbrunnen = new GenericPoint(1178, 370);
    private static final GenericPoint Pup = new GenericPoint(412, 250);
    private static final GenericPoint MutterFeet = new GenericPoint(879, 401);
    private static final GenericPoint Pgaense = new GenericPoint(254, 357);

    // Konstanten - Rects initialisieren
    private static final Borderrect brunnenRect = new Borderrect(1024, 300, 1279, 479);
    private static final Borderrect blattRect = new Borderrect(751, 379, 856, 464);
    private static final Borderrect brTuer = new Borderrect(900, 290, 920, 340);
    private static final Borderrect brBrunnen = new Borderrect(1130, 350, 1230, 410);
    private static final Borderrect obererAusgang = new Borderrect(386, 229, 425, 286);
    private static final Borderrect brSchild = new Borderrect(330, 267, 367, 283);

    // Konstante ints fuer Facing
    private static final int fBrunnen = 6;
    private static final int fTuer = 3;
    private static final int fStock = 9;
    private static final int fSchild = 12;
    // private static final int fMutter  = 3;
    private static final int fGaense = 12;

    public Doma2(Start caller, int oldLocation) {
        super(caller);

        mainFrame.freeze(true);
        mainFrame.checkKrabat();

        gans1 = new Husa(mainFrame, new Borderrect(150, 260, 197, 270));
        gans2 = new Husa(mainFrame, new Borderrect(238, 270, 285, 280));
        gans3 = new Husa(mainFrame, new Borderrect(182, 300, 276, 310));

        TalkPerson = 0;
        BackgroundMusicPlayer.getInstance().playTrack(4, true);

        mainFrame.krabat.maxx = 402;
        mainFrame.krabat.zoomf = 2.32f;
        mainFrame.krabat.defScale = 0;

        Rauchanim = new GenericImage[13];

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                // Mutter kann nicht da sein, weil das Anim waere
                istMutterZuSehen = false;
                break;

            case 74: // Aus Jitk kommend, also 1. Animation
                mainFrame.krabat.setPos(new GenericPoint(413, 275));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 88;
                setScroll = true;
                Animation = true;
                istMutterZuSehen = true;
                break;

            case 90: // spaeter aus der Muehle2 zurueck
                mainFrame.krabat.setPos(new GenericPoint(162, 356));
                mainFrame.krabat.SetFacing(3);
                scrollwert = 0;
                setScroll = true;
                Animation = true;
                istMutterZuSehen = false;
                break;

            case 87: // von Wjes aus (ueber Karte) oder Bote-Anim
                if (!mainFrame.actions[303]) {
                    mainFrame.krabat.setPos(new GenericPoint(413, 275));
                    mainFrame.krabat.SetFacing(6);
                    scrollwert = 88;
                    istMutterZuSehen = false;
                } else {
                    mainFrame.krabat.setPos(new GenericPoint(840, 400));
                    mainFrame.krabat.SetFacing(3);
                    scrollwert = 520;
                    Animation = true;
                    istMutterZuSehen = true;
                }
                setScroll = true;
                break;
        }

        mutter = new Mac(mainFrame, true);

        Pmac = new GenericPoint();
        Pmac.x = MutterFeet.x - Mac.Breite / 2;
        Pmac.y = MutterFeet.y - Mac.Hoehe;

        MacTalk = new GenericPoint();
        MacTalk.x = MutterFeet.x;
        MacTalk.y = Pmac.y - 50;

        // brMac = new borderrect (Pmac.x, Pmac.y, Pmac.x + mac.Breite, Pmac.y + mac.Hoehe);

        InitLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        // je nach dem, ob Mutter da ist oder nicht
        if (!istMutterZuSehen) {
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(411, 423, 396, 420, 281, 329));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(396, 420, 382, 470, 330, 364));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(135, 158, 187, 218, 351, 364));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(187, 496, 255, 496, 365, 410));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(497, 353, 639, 411));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(640, 370, 824, 408));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(825, 346, 866, 414));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(867, 373, 1015, 403));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(1016, 365, 1127, 416));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(1128, 366, 1200, 392));

            // Matrix loeschen
            mainFrame.pathFinder.ClearMatrix(10);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.pathFinder.PosVerbinden(0, 1);
            mainFrame.pathFinder.PosVerbinden(1, 3);
            mainFrame.pathFinder.PosVerbinden(2, 3);
            mainFrame.pathFinder.PosVerbinden(3, 4);
            mainFrame.pathFinder.PosVerbinden(4, 5);
            mainFrame.pathFinder.PosVerbinden(5, 6);
            mainFrame.pathFinder.PosVerbinden(6, 7);
            mainFrame.pathFinder.PosVerbinden(7, 8);
            mainFrame.pathFinder.PosVerbinden(8, 9);
        } else {
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(411, 423, 396, 420, 281, 329));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(396, 420, 382, 470, 330, 364));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(135, 158, 187, 218, 351, 364));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(187, 496, 255, 496, 365, 410));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(497, 353, 639, 411));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(640, 370, 824, 408));

            // Matrix loeschen
            mainFrame.pathFinder.ClearMatrix(6);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.pathFinder.PosVerbinden(0, 1);
            mainFrame.pathFinder.PosVerbinden(1, 3);
            mainFrame.pathFinder.PosVerbinden(2, 3);
            mainFrame.pathFinder.PosVerbinden(3, 4);
            mainFrame.pathFinder.PosVerbinden(4, 5);
        }

    }

    // Bilder vorbereiten
    private void InitImages() {
        background1 = getPicture("gfx/doma/dom-l.png");
        background2 = getPicture("gfx/doma/dom-r.png");
        back = getPicture("gfx/doma/domsky.png");
        brunnen = getPicture("gfx/doma/brunnen.png");
        blatt = getPicture("gfx/doma/blatt.png");

        Rauchanim[1] = getPicture("gfx/doma/ra1.png");
        Rauchanim[2] = getPicture("gfx/doma/ra2.png");
        Rauchanim[3] = getPicture("gfx/doma/ra3.png");
        Rauchanim[4] = getPicture("gfx/doma/ra4.png");
        Rauchanim[5] = getPicture("gfx/doma/ra5.png");
        Rauchanim[6] = getPicture("gfx/doma/ra6.png");
        Rauchanim[7] = getPicture("gfx/doma/ra7.png");
        Rauchanim[8] = getPicture("gfx/doma/ra8.png");
        Rauchanim[9] = getPicture("gfx/doma/ra9.png");
        Rauchanim[10] = getPicture("gfx/doma/ra10.png");
        Rauchanim[11] = getPicture("gfx/doma/ra11.png");
        Rauchanim[12] = getPicture("gfx/doma/ra12.png");

    }

    @Override
    public void cleanup() {
        background1 = null;
        background2 = null;
        back = null;
        brunnen = null;
        blatt = null;

        Rauchanim[1] = null;
        Rauchanim[2] = null;
        Rauchanim[3] = null;
        Rauchanim[4] = null;
        Rauchanim[5] = null;
        Rauchanim[6] = null;
        Rauchanim[7] = null;
        Rauchanim[8] = null;
        Rauchanim[9] = null;
        Rauchanim[10] = null;
        Rauchanim[11] = null;
        Rauchanim[12] = null;

        mutter.cleanup();
        mutter = null;
        gans1.cleanup();
        gans1 = null;
        gans2.cleanup();
        gans2 = null;
        gans3.cleanup();
        gans3 = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isBackgroundAnimRunning = true;
            if (Animation) {
                mainFrame.isAnimRunning = true;
            }
        }

        // Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
        g.drawImage(back, mainFrame.scrollX / 10, 0);
        g.drawImage(background1, 0, 0);
        g.drawImage(background2, 640, 0);

        // Parallax - Scrolling ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollX - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 285);
            g.drawImage(back, mainFrame.scrollX / 10, 0);
            g.drawImage(background1, 0, 0);
            g.drawImage(background2, 640, 0);
        }

        // Ab hier ist Retten des ClipRect sinnlos!!!
        // Rauch animieren
        if (mainFrame.isBackgroundAnimRunning && mainFrame.scrollX > 300) {
            switchanim = !switchanim;
            if (switchanim) {
                Rauchcount++;
                if (Rauchcount == 13) {
                    Rauchcount = 1;
                }
            }
            g.setClip(985, 15, 30, 120);
            g.drawImage(back, mainFrame.scrollX / 10, 0);
            g.drawImage(Rauchanim[Rauchcount], 985, 15);
            g.drawImage(background2, 640, 0);
        }

        // Gaense animieren
        if (mainFrame.isBackgroundAnimRunning && mainFrame.scrollX < 350) {
            g.setClip(120, 255, 230, 110);
            g.drawImage(back, mainFrame.scrollX / 10, 0);
            g.drawImage(background1, 0, 0);
            g.drawImage(background2, 640, 0);
            gans1.BewegeGans(g);
            gans2.BewegeGans(g);
            gans3.BewegeGans(g);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Krabats neue Position festlegen wenn noetig
        mainFrame.pathWalker.GeheWeg();

        // Mac zeichnen bei Reden und Herumstehen, vorher Hintergrund wiederherstellen
        if (mainFrame.scrollX > 130 && istMutterZuSehen) {
            g.setClip(Pmac.x, Pmac.y, Mac.Breite, Mac.Hoehe);
            g.drawImage(background2, 640, 0);
            mutter.drawMac(g, Pmac, TalkPerson);
        }

        // Krabat zeichnen

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

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

        // Ab hier muss Cliprect wieder gerettet werden
        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm Brunnen (nur Clipping - Region wird neugezeichnet)
        if (brunnenRect.IsPointInRect(pKrTemp)) {
            g.drawImage(brunnen, 1055, 251);
        }

        //hinterm Blatt
        if (blattRect.IsPointInRect(pKrTemp)) {
            g.drawImage(blatt, 764, 393);
        }

        // Hier das FadeToBlack, wenn noetig
        if (FadeToBlack > 0) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(mainFrame.scrollX, mainFrame.scrollY, 644, 484);

            g.clearRect(mainFrame.scrollX, mainFrame.scrollY,
                    FadeToBlack, 479);
            g.clearRect(mainFrame.scrollX + 639 - FadeToBlack, mainFrame.scrollY,
                    639, 479);
            g.clearRect(mainFrame.scrollX, mainFrame.scrollY,
                    639, FadeToBlack);
            g.clearRect(mainFrame.scrollX, mainFrame.scrollY + 479 - FadeToBlack,
                    639, 479);

            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Textausgabe, falls noetig
        if (!Objects.equals(outputText, "")) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 964);
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

        // Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!

        // Anims einschalten, wenn noetig
        if (Animation) {
            Animation = false;
            mainFrame.krabat.StopWalking();
            if (mainFrame.actions[300]) {
                mainFrame.actions[300] = false;
                nextActionID = 600;
            } else {
                if (mainFrame.actions[303]) {
                    nextActionID = 1000;
                } else {
                    nextActionID = 800;
                }
            }
        }

        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
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
            TalkPerson = 0;
        }

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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }


                // Hier kommt Routine hin, die Standard - Ausredeantworten auswaehlt, je nach Gegenstand
                // in Location, damit Textposition bekannt ist!!!

                // Ausreden fuer Mac
			/*if (brMac.IsPointInRect (pTemp) == true)
			  {
			  // hier Extra - Sinnlosantworten angeben
			  nextActionID = 150;
			  pTemp = Pmutter;
			  mainFrame.repaint(); 
			  }*/

                // Ausreden fuer Brunnen
                if (brBrunnen.IsPointInRect(pTemp) && !istMutterZuSehen) {
                    switch (mainFrame.whatItem) {
                        case 9: // wuda + hocka
                            nextActionID = 200;
                            break;
                        case 10: // Angel mit Wurm oder Holzfisch
                        case 11:
                            nextActionID = 210;
                            break;
                        case 14: // Fisch
                            nextActionID = 220;
                            break;
                        default: // alles andere
                            nextActionID = 160;
                            break;
                    }
                    pTemp = Pbrunnen;
                }

                // Ausreden fuer Tuer
                if (brTuer.IsPointInRect(pTemp) && !istMutterZuSehen) {
                    // nur Standard
                    nextActionID = 165;
                    pTemp = Ptuer;
                }

                // Ausreden fuer Schild
                if (brSchild.IsPointInRect(pTemp)) {
                    // nur Standard
                    nextActionID = 170;
                    pTemp = Pschild;
                }

                // Ausreden fuer Gaense
                if (gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                        gans3.GetHusaRect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                        case 18: // roh + kam
                            nextActionID = 230;
                            break;
                        case 6: // lajna
                            nextActionID = 240;
                            break;
                        case 8: // wacki
                        case 14: // ryba
                            nextActionID = 250;
                            break;
                        default:
                            nextActionID = 175;
                            break;
                    }
                    pTemp = Pgaense;
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

                // Brunnen ansehen
                if (brBrunnen.IsPointInRect(pTemp) && !istMutterZuSehen) {
                    nextActionID = 1;
                    pTemp = Pbrunnen;
                }

                // Tuer ansehen
                if (brTuer.IsPointInRect(pTemp) && !istMutterZuSehen) {
                    nextActionID = 2;
                    pTemp = Ptuer;
                }

                // Schild ansehen
                if (brSchild.IsPointInRect(pTemp)) {
                    pTemp = Pschild;
                    nextActionID = 4;
                }

                // nach Jitk gehen oder zurueckgepfiffen werden
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;

                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    // Bei Doppelklick sofort springen
                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Mutter ansehen
			/*if (brMac.IsPointInRect (pTemp) == true)
			  {
			  pTemp = Pmutter;
			  nextActionID = 5;
			  }*/

                // Gaense ansehen
                if (gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                        gans3.GetHusaRect().IsPointInRect(pTemp)) {
                    pTemp = Pgaense;
                    nextActionID = 6;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Brunnen benutzen ?
                if (brBrunnen.IsPointInRect(pTemp) && !istMutterZuSehen) {
                    nextActionID = 52;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pbrunnen);
                    mainFrame.repaint();
                    return;
                }

                // mit Mutter reden
			/*if (brMac.IsPointInRect (pTemp) == true)
			  {
			  nextActionID = 54;
			  mainFrame.wegGeher.SetzeNeuenWeg (Pmutter);
			  mainFrame.repaint();
			  return;
			  } */

                // Weg nach Jitk anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // ins Haus gehen
                if (brTuer.IsPointInRect(pTemp) && !istMutterZuSehen) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Ptuer);
                    mainFrame.repaint();
                    return;
                }

                // Schild mitnehmen
                if (brSchild.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Gaense mitnehmen
                if (gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                        gans3.GetHusaRect().IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pgaense);
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
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = brBrunnen.IsPointInRect(pTemp) && !istMutterZuSehen ||
                    brTuer.IsPointInRect(pTemp) && !istMutterZuSehen ||
                    brSchild.IsPointInRect(pTemp) || tmp.IsPointInRect(pTemp) ||
                    gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                    gans3.GetHusaRect().IsPointInRect(pTemp);

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
            if (brBrunnen.IsPointInRect(pTemp) && !istMutterZuSehen ||
                    brTuer.IsPointInRect(pTemp) && !istMutterZuSehen ||
                    brSchild.IsPointInRect(pTemp) ||
                    /*(brMac.IsPointInRect (pTemp) == true))*/
                    gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                    gans3.GetHusaRect().IsPointInRect(pTemp)) {
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
        TalkPerson = 0;
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.StopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {

        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering || mainFrame.krabat.isWalking) && nextActionID != 400) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.mousePoint);

            return;
        }

        // System.out.println("Nextaction " + nextActionID);

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {

            // Look - DoActions

            case 1:
                // Brunnen anschauen
                KrabatSagt("Doma2_1", fBrunnen, 3, 0, 0);
                break;

            case 2:
                // Haustuer anschauen
                KrabatSagt("Doma2_2", fTuer, 3, 0, 0);
                break;

            case 3:
                // Stock anschauen
                KrabatSagt("Doma2_3", fStock, 3, 0, 0);
                break;

            case 4:
                // Schild anschauen
                KrabatSagt("Doma2_4", fSchild, 3, 0, 0);
                break;

            case 6:
                // Gaense anschauen
                KrabatSagt("Doma2_5", fGaense, 3, 0, 0);
                break;

            // Use - DoActions

            case 50:
                // ins Haus gehen ??
                KrabatSagt("Doma2_6", fTuer, 3, 0, 0);
                break;

            case 51:
                // Schild mitnehmen
                KrabatSagt("Doma2_7", fSchild, 3, 0, 0);
                break;

            case 52:
                // Brunnen benutzen
                KrabatSagt("Doma2_8", fBrunnen, 3, 0, 0);
                break;

            case 70:
                // Gaense mitnehmen
                KrabatSagt("Doma2_9", fGaense, 3, 0, 0);
                break;

            // GoTo DoActions

            case 100:
                // Karte einblenden
                mainFrame.constructLocation(106);
                mainFrame.isBackgroundAnimRunning = false;
                mainFrame.whatScreen = ScreenType.MAP;
                nextActionID = 0;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            // Ausreden fuer Benutze Gegenstand mit Gegenstand

            case 160:
                // Brunnen - Ausreden
                DingAusrede(fBrunnen);
                break;

            case 165:
                // Tuer - Ausreden
                DingAusrede(fTuer);
                break;

            case 170:
                // Schild - Ausreden
                DingAusrede(fSchild);
                break;

            case 175:
                // Gaense - Ausreden
                APersonAusrede(fGaense);
                break;

            case 200:
                // Angel auf Brunnen
                KrabatSagt("Doma2_10", fBrunnen, 3, 0, 0);
                break;

            case 210:
                // Angel mit Wurm oder Holzfisch auf Brunnen
                KrabatSagt("Doma2_11", fBrunnen, 3, 0, 0);
                break;

            case 220:
                // Fisch auf Brunnen
                KrabatSagt("Doma2_12", fBrunnen, 3, 0, 0);
                break;

            case 230:
                // Kij auf Husy
                KrabatSagt("Doma2_13", fGaense, 3, 0, 0);
                break;

            case 240:
                // Lajna auf Husy
                KrabatSagt("Doma2_14", fGaense, 3, 0, 0);
                break;

            case 250:
                // Wacki oder Ryba auf Husy
                KrabatSagt("Doma2_15", fGaense, 3, 0, 0);
                break;

            // Multiple Choice

            case 600:
                // Intro: Krabat geht zur Mutter
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.SetzeNeuenWeg(Pmutter);
                mainFrame.repaint();
                nextActionID = 601;
                break;

            case 601:
                // Krabat steht vor Mutter
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.krabat.SetFacing(3);
                nextActionID = 610;
                break;

            case 610:
                // Mutter spricht
                PersonSagt("Doma2_16", 0, 20, 2, 620, MacTalk);
                break;

            case 620:
                // Mutter spricht
                PersonSagt("Doma2_17", 0, 20, 2, 630, MacTalk);
                break;

            case 630:
                // Krabat spricht
                KrabatSagt("Doma2_18", 0, 1, 2, 640);
                break;

            case 640:
                // Mutter spricht
                PersonSagt("Doma2_19", 0, 20, 2, 650, MacTalk);
                break;

            case 650:
                // Krabat spricht
                KrabatSagt("Doma2_20", 0, 1, 2, 660);
                break;

            case 660:
                // Text Erzaehler
                PersonSagt("Doma2_26", 0, 54, 2, 670, new GenericPoint(320, 200));
                break;

            case 670:
                // Text von Krabat
                KrabatSagt(KRABAT_TEXTS[Position], 0, 1, 2, 670);
                Position++;
                if (Position >= KRABAT_TEXTS.length) {
                    nextActionID = 680;
                }
                break;

            case 680:
                // Mutter spricht
                PersonSagt("Doma2_21", 0, 20, 2, 690, MacTalk);
                break;

            case 690:
                // Krabat spricht
                KrabatSagt("Doma2_22", 0, 1, 2, 700);
                break;

            case 700:
                // Ausfaden und Ruhe
                FadeToBlack = 1;
                nextActionID = 710;
                break;

            case 710:
                // bis zum Ende warten
                FadeToBlack += 3;
                if (FadeToBlack >= 246) {
                    nextActionID = 720;
                }
                break;		
      	
		/*	    case 700:
		// Krabat geht wieder weg
		mainFrame.wegGeher.SetzeNeuenWeg (Pup);
		mainFrame.repaint();
		nextActionID = 710;
		break;*/

            case 720:
                // Skip nach Muehle (spaeter)
                // if (mainFrame.isScrolling == true) break;
                NeuesBild(90, 71);
                break;

            case 800:
                // Text Erzaehler
                PersonSagt("Doma2_32", 0, 54, 2, 805, new GenericPoint(320, 200));
                break;

            case 805:
                // Text Erzaehler
                PersonSagt("Doma2_33", 0, 54, 2, 808, new GenericPoint(320, 250));
                break;

            case 808:
                // Text Erzaehler
                PersonSagt("Doma2_34", 0, 54, 2, 810, new GenericPoint(320, 250));
                break;

            case 810:
                mainFrame.isAnimRunning = false;
                mainFrame.isClipSet = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                break;

            case 1000:
                // Anim, nachdem Bote gesprochen hat
                // Mutter spricht
                PersonSagt("Doma2_23", 0, 20, 2, 1010, MacTalk);
                break;

            case 1010:
                // Text von Krabat
                KrabatSagt("Doma2_24", 0, 1, 2, 1020);
                break;

            case 1020:
                // Mutter spricht
                PersonSagt("Doma2_25", 0, 20, 2, 1030, MacTalk);
                break;

            case 1030:
                // Skip zu Most
                NeuesBild(89, 71);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}