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
import de.codengine.krabat.anims.Geese;
import de.codengine.krabat.anims.Mother;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Doma1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Doma1.class);
    private GenericImage background1;
    private GenericImage background2;
    private GenericImage back;
    private GenericImage brunnen;
    private GenericImage blatt;
    private GenericImage stock;
    private GenericImage logo;
    private GenericImage[] Rauchanim;
    private int Rauchcount = 1;
    private boolean showRauch = false;

    private MultipleChoice Dialog;
    private boolean switchanim = true;

    private boolean setScroll = false;
    private int scrollwert;

    private boolean showLogo = false;
    private Geese gans1;
    private Geese gans2;
    private Geese gans3;
    private boolean setAnim = false;

    private final GenericPoint MacTalk;
    private final GenericPoint Pmac;
    private final BorderRect brMac;
    private boolean macIsVisible;
    private Mother mutter;

    // Walkto - Points fuer Dinge in Location
    private static final GenericPoint Pmutter = new GenericPoint(840, 400);
    private static final GenericPoint Ptuer = new GenericPoint(866, 346);
    private static final GenericPoint Pschild = new GenericPoint(338, 369);
    private static final GenericPoint Pstock = new GenericPoint(150, 350);
    private static final GenericPoint pVorStock = new GenericPoint(127, 362);
    private static final GenericPoint Pbrunnen = new GenericPoint(1202, 410);
    private static final GenericPoint Pgaense = new GenericPoint(254, 357);
    private static final GenericPoint Pup = new GenericPoint(412, 250);
    private static final GenericPoint MutterFeet = new GenericPoint(879, 401);

    // Konstanten - Rects initialisieren
    private static final BorderRect brunnenRect = new BorderRect(1024, 300, 1279, 479); // Vordergrund
    private static final BorderRect blattRect = new BorderRect(751, 379, 856, 464); // Vordergrund
    private static final BorderRect brTuer = new BorderRect(900, 290, 920, 340);
    private static final BorderRect brBrunnen = new BorderRect(1130, 350, 1230, 410);
    private static final BorderRect obererAusgang = new BorderRect(386, 229, 425, 286);
    private static final BorderRect brStock = new BorderRect(89, 300, 130, 365);
    private static final BorderRect brSchild = new BorderRect(330, 267, 367, 283);

    // Konstante ints fuer Facing
    private static final int fBrunnen = 6;
    private static final int fTuer = 3;
    private static final int fStock = 9;
    private static final int fSchild = 12;
    private static final int fMutter = 3;
    private static final int fGaense = 12;

    // private boolean playIntroMusic = false;

    private int Counter = 0;

    // normaler Einsprung waehrend Spiel oder Load
    public Doma1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        gans1 = new Geese(mainFrame, new BorderRect(150, 260, 197, 270));
        gans2 = new Geese(mainFrame, new BorderRect(238, 270, 285, 280));
        gans3 = new Geese(mainFrame, new BorderRect(182, 300, 276, 310));

        // Sachen initialisieren, die immer gleich sind
        Init(oldLocation);

        // Mutter laden
        mutter = new Mother(mainFrame, true);

        Pmac = new GenericPoint();
        Pmac.x = MutterFeet.x - Mother.Breite / 2;
        Pmac.y = MutterFeet.y - Mother.Hoehe;

        MacTalk = new GenericPoint();
        MacTalk.x = MutterFeet.x;
        MacTalk.y = Pmac.y - 50;

        brMac = new BorderRect(Pmac.x, Pmac.y, Pmac.x + Mother.Breite, Pmac.y + Mother.Hoehe);

        // ausrechnen, ob Mutter noch da ist
        if (mainFrame.actions[0]) {
            // nur hier darf CD den Looptrack spielen
            BackgroundMusicPlayer.getInstance().playTrack(4, true);

            macIsVisible = false;
            showRauch = true;
        } else {
            // solange Mutter da, nix CD
            BackgroundMusicPlayer.getInstance().stop();

            macIsVisible = true;
            showRauch = false;
            Dialog = new MultipleChoice(mainFrame);
        }
        InitLocation();

        mainFrame.freeze(false);
    }

    // Einsprung vom Intro aus
    public Doma1(Start caller, int oldLocation, Geese gans1, Geese gans2, Geese gans3) {
        super(caller);
        mainFrame.freeze(true);

        BackgroundMusicPlayer.getInstance().playTrack(24, false);

        mainFrame.checkKrabat();

        this.gans1 = gans1;
        this.gans2 = gans2;
        this.gans3 = gans3;

        // Sachen initialisieren, die immer gleich sind
        Init(oldLocation);

        // Krabat definieren
        mainFrame.krabat.setPos(new GenericPoint(128, 352));
        mainFrame.krabat.SetFacing(3);
        scrollwert = 0;
        setScroll = true;

        // Mutter laden
        mutter = new Mother(mainFrame, true);

        Pmac = new GenericPoint();
        Pmac.x = MutterFeet.x - Mother.Breite / 2;
        Pmac.y = MutterFeet.y - Mother.Hoehe;

        MacTalk = new GenericPoint();
        MacTalk.x = MutterFeet.x;
        MacTalk.y = Pmac.y - 50;

        brMac = new BorderRect(Pmac.x, Pmac.y, Pmac.x + Mother.Breite, Pmac.y + Mother.Hoehe);

        // Mutter ist immer da und Anim erscheint
        macIsVisible = true;
        setAnim = true;
        Dialog = new MultipleChoice(mainFrame);
        InitLocation();

        mainFrame.freeze(false);
    }

    private void Init(int oldLocation) {
        TalkPerson = 0;

        mainFrame.krabat.maxx = 402;
        mainFrame.krabat.zoomf = 2.32f;
        mainFrame.krabat.defScale = 0;

        Rauchanim = new GenericImage[13];

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird
        switch (oldLocation) {
            case 0: // Einsprung von Load
                break;
            case 3: // Aus Jitk kommend
                mainFrame.krabat.setPos(new GenericPoint(410, 289));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 88;
                setScroll = true;
                break;
        }
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        // je nach dem, ob Mutter da ist oder nicht
        if (!macIsVisible) {
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(411, 423, 396, 420, 281, 329));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(396, 420, 382, 470, 330, 364));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(135, 158, 187, 218, 351, 364));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(187, 496, 255, 496, 365, 410));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(497, 353, 639, 411));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(640, 370, 824, 408));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(825, 346, 866, 414));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(867, 373, 1015, 403));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1016, 365, 1127, 416));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1128, 366, 1200, 392));

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
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(411, 423, 396, 420, 281, 329));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(396, 420, 382, 470, 330, 364));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(135, 158, 187, 218, 351, 364));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(187, 496, 255, 496, 365, 410));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(497, 353, 639, 411));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(640, 370, 824, 408));

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
// 	background1   = getPicture ("gfx/doma/dom-l.png");
// 	background2   = getPicture ("gfx/doma/dom-r.png");
        back = getPicture("gfx/doma/domsky.png");
        stock = getPicture("gfx/doma/doma2.png");
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

        logo = getPicture("gfx/intro/titul.png");

    }

    @Override
    public void cleanup() {
        background1 = null;
        background2 = null;
        back = null;
        stock = null;
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

        logo = null;

        gans1.cleanup();
        gans1 = null;
        gans2.cleanup();
        gans2 = null;
        gans3.cleanup();
        gans3 = null;
        mutter.cleanup();
        mutter = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // System.out.print("g");

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
	/*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
	    {
		// System.out.println("Hier bin ich!");
		Dialog.paintMultiple (g);
		return;
		}  */

        // Stock wurde aufgehoben!!!!!!!!!
        if (mainFrame.krabat.fAnimHelper) {
            mainFrame.inventory.vInventory.addElement(2);
            mainFrame.isClipSet = false;
            mainFrame.krabat.fAnimHelper = false;
            mainFrame.actions[900] = true;
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

        // Logo im Intro zeigen
        if (showLogo) {
// 		if (playIntroMusic == false)
// 		    {
// 			playIntroMusic = true;
// 			mainFrame.player.Play ("24", -17700);
// 		    }
            g.setClip(195 + mainFrame.scrollX, 150, 250, 100);
            g.drawImage(logo, 205 + mainFrame.scrollX, 150);
        }

        // Ab hier ist Retten des ClipRect sinnlos!!!
        // Rauch animieren
        if (showRauch && mainFrame.scrollX > 300) {
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
        if (macIsVisible && mainFrame.scrollX > 130) {
            g.setClip(Pmac.x, Pmac.y, Mother.Breite, Mother.Hoehe);
            g.drawImage(background2, 640, 0);
            mutter.drawMac(g, Pmac, TalkPerson);
        }

        //hinterm Stock
        if (!mainFrame.actions[900]) {
            g.setClip(80, 325, 29, 38);
            g.drawImage(stock, 80, 325);
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

        // Stock wurde testweise mal hinter Krabat gelegt
        //hinterm Stock
	/*if (mainFrame.Actions[900] == false)
	  {    
	  GenericRectangle my;
	  my = g.getClipBounds();
	  g.setClip (80, 325, 29, 38);
	  g.drawImage (stock, 80, 325, null);
	  g.setClip( (int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight()); 
	  } */

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

        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            Dialog.paintMultiple(g);
            return;
        }

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 600;
        }

        // Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!

        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
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

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Mac
                if (brMac.IsPointInRect(pTemp) && macIsVisible) {
                    // Mac bekommt nur Standardausreden
                    nextActionID = 150;
                    pTemp = Pmutter;
                }

                // Ausreden fuer Stock
                if (brStock.IsPointInRect(pTemp) && !mainFrame.actions[900]) {
                    // Stock bekommt nur Standardausreden
                    nextActionID = 155;
                    pTemp = Pstock;
                }

                // Ausreden fuer Brunnen
                if (brBrunnen.IsPointInRect(pTemp) && !macIsVisible) {
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
                if (brTuer.IsPointInRect(pTemp) && !macIsVisible) {
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
                if (brBrunnen.IsPointInRect(pTemp) && !macIsVisible) {
                    nextActionID = 1;
                    pTemp = Pbrunnen;
                }

                // Tuer ansehen
                if (brTuer.IsPointInRect(pTemp) && !macIsVisible) {
                    nextActionID = 2;
                    pTemp = Ptuer;
                }

                // Stock ansehen ?
                if (brStock.IsPointInRect(pTemp) &&
                        !mainFrame.actions[900]) {
                    nextActionID = 3;
                    pTemp = Pstock;
                }

                // Schild ansehen
                if (brSchild.IsPointInRect(pTemp)) {
                    pTemp = Pschild;
                    nextActionID = 4;
                }

                // nach Jitk gehen oder zurueckgepfiffen werden
                if (obererAusgang.IsPointInRect(pTemp)) {
                    if (mainFrame.actions[0]) {
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
                    } else {
                        nextActionID = 101;
                    }
                }

                // Mutter ansehen
                if (brMac.IsPointInRect(pTemp) && macIsVisible) {
                    pTemp = Pmutter;
                    nextActionID = 5;
                }

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
                if (brBrunnen.IsPointInRect(pTemp) && !macIsVisible) {
                    nextActionID = 52;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pbrunnen);
                    mainFrame.repaint();
                    return;
                }

                // Stock mitnehmen ?
                if (brStock.IsPointInRect(pTemp) &&
                        !mainFrame.actions[900]) {
                    nextActionID = 53;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pstock);
                    mainFrame.repaint();
                    return;
                }

                // mit Mutter reden
                if (brMac.IsPointInRect(pTemp) && macIsVisible) {
                    nextActionID = 54;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pmutter);
                    mainFrame.repaint();
                    return;
                }

                // Weg nach Jitk anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // ins Haus gehen
                if (brTuer.IsPointInRect(pTemp) && !macIsVisible) {
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

        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseMoveEvent(pTxxx);
            return;
        }

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
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = brBrunnen.IsPointInRect(pTemp) && !macIsVisible ||
                    brTuer.IsPointInRect(pTemp) && !macIsVisible ||
                    brSchild.IsPointInRect(pTemp) || tmp.IsPointInRect(pTemp) ||
                    brStock.IsPointInRect(pTemp) && !mainFrame.actions[900] ||
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
            if (brBrunnen.IsPointInRect(pTemp) && !macIsVisible ||
                    brTuer.IsPointInRect(pTemp) && !macIsVisible ||
                    brSchild.IsPointInRect(pTemp) ||
                    brMac.IsPointInRect(pTemp) && macIsVisible ||
                    brStock.IsPointInRect(pTemp) && !mainFrame.actions[900] ||
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

            // Look - DoActions

            case 1:
                // Brunnen anschauen
                KrabatSagt("Doma1_1", fBrunnen, 3, 0, 0);
                break;

            case 2:
                // Haustuer anschauen
                KrabatSagt("Doma1_2", fTuer, 3, 0, 0);
                break;

            case 3:
                // Stock anschauen
                KrabatSagt("Doma1_3", fStock, 3, 0, 0);
                break;

            case 4:
                // Schild anschauen
                KrabatSagt("Doma1_4", fSchild, 3, 0, 0);
                break;

            case 5:
                // Mutter anschauen
                KrabatSagt("Doma1_5", fMutter, 3, 0, 0);
                break;

            case 6:
                // Gaense anschauen
                KrabatSagt("Doma1_6", fGaense, 3, 0, 0);
                break;

            // Use - DoActions

            case 50:
                // ins Haus gehen ??
                KrabatSagt("Doma1_7", fTuer, 3, 0, 0);
                break;

            case 51:
                // Schild mitnehmen
                KrabatSagt("Doma1_8", fSchild, 3, 0, 0);
                break;

            case 52:
                // Brunnen benutzen
                KrabatSagt("Doma1_9", fBrunnen, 3, 0, 0);
                break;

            case 53:
                // Stock mitnehmen
                // zuerstmal richtig hinlaufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(pVorStock);
                nextActionID = 80;
                break;

            case 54:
                // Krabat beginnt MC (Mutter benutzen)
                mainFrame.krabat.SetFacing(fMutter);
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 630;
                break;

            case 70:
                // Gaense mitnehmen
                KrabatSagt("Doma1_10", fGaense, 3, 0, 0);
                break;

            case 80:
                // Stock mitnehmen
                mainFrame.krabat.SetFacing(fStock);
                mainFrame.krabat.nAnimation = 92;
                nextActionID = 85;
                break;

            case 85:
                // auf Ende Anim warten
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 90;
                }
                break;

            case 90:
                // zuruecklaufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Pstock);
                nextActionID = 95;
                break;

            case 95:
                // Ende Stockanim
                mainFrame.isAnimRunning = false;
                mainFrame.krabat.SetFacing(3); // nach rechts schauen, nachdeem der Stock genommen wurde
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            // GoTo DoActions

            case 100:
                // Nach Jitk gehen
                NeuesBild(3, 6);
                break;

            case 101:
                // von Mutter zurueckgepfiffen werden
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Start.stringManager.getTranslation("Doma1_33");
                TalkPerson = 20;
                mainFrame.talkCount = 30;
                TalkPause = 0;
                outputTextPos = new GenericPoint(400, 100);
                nextActionID = 610;
                break;

            // Ausreden fuer Benutze Gegenstand mit Gegenstand

            case 150:
                // Mutter - Ausreden
                WPersonAusrede(fMutter);
                break;

            case 155:
                // Stock - Ausreden
                DingAusrede(fStock);
                break;

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

            // Extra - Ausreden fuer ggst-Benutzen

            case 200:
                // Angel auf Brunnen
                KrabatSagt("Doma1_11", fBrunnen, 3, 0, 0);
                break;

            case 210:
                // Angel mit Wurm oder Holzfisch auf Brunnen
                KrabatSagt("Doma1_12", fBrunnen, 3, 0, 0);
                break;

            case 220:
                // Fisch auf Brunnen
                KrabatSagt("Doma1_13", fBrunnen, 3, 0, 0);
                break;

            case 230:
                // Kij auf Husy
                KrabatSagt("Doma1_14", fGaense, 3, 0, 0);
                break;

            case 240:
                // Lajna auf Husy
                KrabatSagt("Doma1_15", fGaense, 3, 0, 0);
                break;

            case 250:
                // Wacki oder Ryba auf Husy
                KrabatSagt("Doma1_16", fGaense, 3, 0, 0);
                break;

            // Multiple Choice

            case 600:
                // Intro: Krabat geht zur Mutter
                mainFrame.isAnimRunning = true;
                showLogo = true;
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
                showLogo = false;
                mainFrame.isClipSet = false;
                mainFrame.krabat.SetFacing(fMutter);
                nextActionID = 602;
                break;

            case 602:
                // 1. Argument Krabat
                KrabatSagt("Doma1_17", fMutter, 1, 2, 603);
                break;

            case 603:
                // 1. Argument Mutter
                PersonSagt("Doma1_18", 0, 20, 2, 630, MacTalk);
                break;

            case 610:
                // Krabat geht zur Mutter bei Versuch gehe nach Jitk
                mainFrame.pathWalker.SetzeNeuenWeg(Pmutter);
                mainFrame.repaint();
                nextActionID = 611;
                break;

            case 611:
                // Krabat wird von Mutter gefragt
                mainFrame.krabat.SetFacing(fMutter);
                if (mainFrame.isScrolling) {
                    break;
                }
                PersonSagt("Doma1_19", 0, 20, 2, 630, MacTalk);
                break;

            case 630:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Doma1_26", 1000, 1, new int[]{1}, 640);

                // 2. Frage
                Dialog.ExtendMC("Doma1_27", 1000, 3, new int[]{3}, 660);
                Dialog.ExtendMC("Doma1_28", 3, 4, new int[]{4}, 670);

                // 3. Frage
                Dialog.ExtendMC("Doma1_29", 1000, 2, new int[]{2}, 650);
                Dialog.ExtendMC("Doma1_30", 2, 5, new int[]{5}, 680);

                // 4. Frage (Ende)
                Dialog.ExtendMC("Doma1_31", 6, 7, null, 690);
                Dialog.ExtendMC("Doma1_32", 7, 1000, null, 690);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 631;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 631:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                // Exit bereitstellen
                mainFrame.actions[6] = true;
                mainFrame.actions[7] = true;

                nextActionID = Dialog.ActionID;

                // Wenn alles abgefragt, dann beenden
                if (mainFrame.actions[1] && mainFrame.actions[2] && mainFrame.actions[3] &&
                        mainFrame.actions[4] && mainFrame.actions[5]) {
                    nextActionID = 700;
                }
                break;

            case 640:
                // Reaktion Mutter 1. Fr/ 1. T
                PersonSagt("Doma1_20", 0, 20, 2, 630, MacTalk);
                break;

            case 650:
                // Reaktion Mutter 1. Fr/2. T
                PersonSagt("Doma1_21", 0, 20, 2, 630, MacTalk);
                break;

            case 660:
                // Reaktion Mutter 2. Fr/1. T
                PersonSagt("Doma1_22", 0, 20, 2, 630, MacTalk);
                break;

            case 670:
                // Reaktion Mutter 2. Fr/ 2. T
                PersonSagt("Doma1_23", 0, 20, 2, 630, MacTalk);
                break;

            case 680:
                // Reaktion Mutter 2. Fr/ 3. T
                PersonSagt("Doma1_24", 0, 20, 2, 630, MacTalk);
                break;

            case 690:
                // Vorzeitiger Abbruch des Intros
                // System.out.println("Vorzeitig verlassen !");
                mainFrame.actions[7] = false; // nico dale statt derje tak beim naechsten mal
                mainFrame.isAnimRunning = false;
                mainFrame.isClipSet = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 700:
                // Mutter gibt auf - Erfolg
                PersonSagt("Doma1_25", 0, 20, 2, 710, MacTalk);
                break;

            case 710:
                // Krabat laeuft zurueck
                mainFrame.pathWalker.SetzeNeuenWeg(new GenericPoint(380, 400));
                mainFrame.repaint();
                nextActionID = 711;
                break;

            case 711:
                // Back to normal Gameplay (Mutter weg)
                macIsVisible = false;
                InitLocation();
                mainFrame.actions[0] = true;
                showRauch = true;
                mainFrame.isClipSet = false;
                nextActionID = 712;
                Counter = 5;
                break;

            case 712:
                // CD-Player an und weg
                if (--Counter > 1) {
                    break;
                }
                BackgroundMusicPlayer.getInstance().playTrack(4, true);
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}