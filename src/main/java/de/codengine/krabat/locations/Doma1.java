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
import de.codengine.krabat.anims.Husa;
import de.codengine.krabat.anims.Mac;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Doma1 extends Mainloc {
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

    private Multiple2 Dialog;
    private boolean switchanim = true;

    private boolean setScroll = false;
    private int scrollwert;

    private boolean showLogo = false;
    private Husa gans1;
    private Husa gans2;
    private Husa gans3;
    private boolean setAnim = false;

    private final GenericPoint MacTalk;
    private final GenericPoint Pmac;
    private final Borderrect brMac;
    private boolean macIsVisible;
    private Mac mutter;

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
    private static final Borderrect brunnenRect = new Borderrect(1024, 300, 1279, 479); // Vordergrund
    private static final Borderrect blattRect = new Borderrect(751, 379, 856, 464); // Vordergrund
    private static final Borderrect brTuer = new Borderrect(900, 290, 920, 340);
    private static final Borderrect brBrunnen = new Borderrect(1130, 350, 1230, 410);
    private static final Borderrect obererAusgang = new Borderrect(386, 229, 425, 286);
    private static final Borderrect brStock = new Borderrect(89, 300, 130, 365);
    private static final Borderrect brSchild = new Borderrect(330, 267, 367, 283);

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
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        gans1 = new Husa(mainFrame, new Borderrect(150, 260, 197, 270));
        gans2 = new Husa(mainFrame, new Borderrect(238, 270, 285, 280));
        gans3 = new Husa(mainFrame, new Borderrect(182, 300, 276, 310));

        // Sachen initialisieren, die immer gleich sind
        Init(oldLocation);

        // Mutter laden
        mutter = new Mac(mainFrame, true);

        Pmac = new GenericPoint();
        Pmac.x = MutterFeet.x - Mac.Breite / 2;
        Pmac.y = MutterFeet.y - Mac.Hoehe;

        MacTalk = new GenericPoint();
        MacTalk.x = MutterFeet.x;
        MacTalk.y = Pmac.y - 50;

        brMac = new Borderrect(Pmac.x, Pmac.y, Pmac.x + Mac.Breite, Pmac.y + Mac.Hoehe);

        // ausrechnen, ob Mutter noch da ist
        if (mainFrame.Actions[0]) {
            // nur hier darf CD den Looptrack spielen
            BackgroundMusicPlayer.getInstance().playTrack(4, true);

            macIsVisible = false;
            showRauch = true;
        } else {
            // solange Mutter da, nix CD
            BackgroundMusicPlayer.getInstance().stop();

            macIsVisible = true;
            showRauch = false;
            Dialog = new Multiple2(mainFrame);
        }
        InitLocation();

        mainFrame.Freeze(false);
    }

    // Einsprung vom Intro aus
    public Doma1(Start caller, int oldLocation, Husa gans1, Husa gans2, Husa gans3) {
        super(caller);
        mainFrame.Freeze(true);

        BackgroundMusicPlayer.getInstance().playTrack(24, false);

        mainFrame.CheckKrabat();

        this.gans1 = gans1;
        this.gans2 = gans2;
        this.gans3 = gans3;

        // Sachen initialisieren, die immer gleich sind
        Init(oldLocation);

        // Krabat definieren
        mainFrame.krabat.SetKrabatPos(new GenericPoint(128, 352));
        mainFrame.krabat.SetFacing(3);
        scrollwert = 0;
        setScroll = true;

        // Mutter laden
        mutter = new Mac(mainFrame, true);

        Pmac = new GenericPoint();
        Pmac.x = MutterFeet.x - Mac.Breite / 2;
        Pmac.y = MutterFeet.y - Mac.Hoehe;

        MacTalk = new GenericPoint();
        MacTalk.x = MutterFeet.x;
        MacTalk.y = Pmac.y - 50;

        brMac = new Borderrect(Pmac.x, Pmac.y, Pmac.x + Mac.Breite, Pmac.y + Mac.Hoehe);

        // Mutter ist immer da und Anim erscheint
        macIsVisible = true;
        setAnim = true;
        Dialog = new Multiple2(mainFrame);
        InitLocation();

        mainFrame.Freeze(false);
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
                mainFrame.krabat.SetKrabatPos(new GenericPoint(410, 289));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 88;
                setScroll = true;
                break;
        }
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.wegGeher.vBorders.removeAllElements();

        // Grenzen setzen
        // je nach dem, ob Mutter da ist oder nicht
        if (!macIsVisible) {
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(411, 423, 396, 420, 281, 329));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(396, 420, 382, 470, 330, 364));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(135, 158, 187, 218, 351, 364));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(187, 496, 255, 496, 365, 410));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(497, 353, 639, 411));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(640, 370, 824, 408));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(825, 346, 866, 414));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(867, 373, 1015, 403));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(1016, 365, 1127, 416));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(1128, 366, 1200, 392));

            // Matrix loeschen
            mainFrame.wegSucher.ClearMatrix(10);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 3);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(4, 5);
            mainFrame.wegSucher.PosVerbinden(5, 6);
            mainFrame.wegSucher.PosVerbinden(6, 7);
            mainFrame.wegSucher.PosVerbinden(7, 8);
            mainFrame.wegSucher.PosVerbinden(8, 9);
        } else {
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(411, 423, 396, 420, 281, 329));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(396, 420, 382, 470, 330, 364));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(135, 158, 187, 218, 351, 364));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(187, 496, 255, 496, 365, 410));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(497, 353, 639, 411));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(640, 370, 824, 408));

            // Matrix loeschen
            mainFrame.wegSucher.ClearMatrix(6);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 3);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(4, 5);
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background1 = getPicture("gfx/doma/dom-l.gif");
        background2 = getPicture("gfx/doma/dom-r.gif");
// 	background1   = getPicture ("gfx/doma/dom-l.png");
// 	background2   = getPicture ("gfx/doma/dom-r.png");
        back = getPicture("gfx/doma/domsky.gif");
        stock = getPicture("gfx/doma/doma2.gif");
        brunnen = getPicture("gfx/doma/brunnen.gif");
        blatt = getPicture("gfx/doma/blatt.gif");

        Rauchanim[1] = getPicture("gfx/doma/ra1.gif");
        Rauchanim[2] = getPicture("gfx/doma/ra2.gif");
        Rauchanim[3] = getPicture("gfx/doma/ra3.gif");
        Rauchanim[4] = getPicture("gfx/doma/ra4.gif");
        Rauchanim[5] = getPicture("gfx/doma/ra5.gif");
        Rauchanim[6] = getPicture("gfx/doma/ra6.gif");
        Rauchanim[7] = getPicture("gfx/doma/ra7.gif");
        Rauchanim[8] = getPicture("gfx/doma/ra8.gif");
        Rauchanim[9] = getPicture("gfx/doma/ra9.gif");
        Rauchanim[10] = getPicture("gfx/doma/ra10.gif");
        Rauchanim[11] = getPicture("gfx/doma/ra11.gif");
        Rauchanim[12] = getPicture("gfx/doma/ra12.gif");

        logo = getPicture("gfx/intro/titul.gif");

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
            mainFrame.Clipset = false;
            mainFrame.krabat.fAnimHelper = false;
            mainFrame.Actions[900] = true;
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

        // Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
        g.drawImage(back, mainFrame.scrollx / 10, 0);
        g.drawImage(background1, 0, 0);
        g.drawImage(background2, 640, 0);

        // Parallax - Scrolling ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 285);
            g.drawImage(back, mainFrame.scrollx / 10, 0);
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
            g.setClip(195 + mainFrame.scrollx, 150, 250, 100);
            g.drawImage(logo, 205 + mainFrame.scrollx, 150);
        }

        // Ab hier ist Retten des ClipRect sinnlos!!!
        // Rauch animieren
        if (showRauch && mainFrame.scrollx > 300) {
            switchanim = !switchanim;
            if (switchanim) {
                Rauchcount++;
                if (Rauchcount == 13) {
                    Rauchcount = 1;
                }
            }
            g.setClip(985, 15, 30, 120);
            g.drawImage(back, mainFrame.scrollx / 10, 0);
            g.drawImage(Rauchanim[Rauchcount], 985, 15);
            g.drawImage(background2, 640, 0);
        }

        // Gaense animieren
        if (mainFrame.isAnim && mainFrame.scrollx < 350) {
            g.setClip(120, 255, 230, 110);
            g.drawImage(back, mainFrame.scrollx / 10, 0);
            g.drawImage(background1, 0, 0);
            g.drawImage(background2, 640, 0);
            gans1.BewegeGans(g);
            gans2.BewegeGans(g);
            gans3.BewegeGans(g);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Krabats neue Position festlegen wenn noetig
        mainFrame.wegGeher.GeheWeg();

        // Mac zeichnen bei Reden und Herumstehen, vorher Hintergrund wiederherstellen
        if (macIsVisible && mainFrame.scrollx > 130) {
            g.setClip(Pmac.x, Pmac.y, Mac.Breite, Mac.Hoehe);
            g.drawImage(background2, 640, 0);
            mutter.drawMac(g, Pmac, TalkPerson);
        }

        //hinterm Stock
        if (!mainFrame.Actions[900]) {
            g.setClip(80, 325, 29, 38);
            g.drawImage(stock, 80, 325);
        }

        // Krabat zeichnen

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

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
        if (outputText != "") {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 964);
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

        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
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
        if (mainFrame.isMultiple) {
            Dialog.evalMouseEvent(e);
            return;
        }

        // Auszugebenden Text abbrechen
        outputText = "";
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }

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

                // Ausreden fuer Mac
                if (brMac.IsPointInRect(pTemp) && macIsVisible) {
                    // Mac bekommt nur Standardausreden
                    nextActionID = 150;
                    pTemp = Pmutter;
                }

                // Ausreden fuer Stock
                if (brStock.IsPointInRect(pTemp) && !mainFrame.Actions[900]) {
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
                        !mainFrame.Actions[900]) {
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
                    if (mainFrame.Actions[0]) {
                        nextActionID = 100;

                        GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!obererAusgang.IsPointInRect(kt)) {
                            pTemp = Pup;
                        } else {
                            pTemp = new GenericPoint(kt.x, Pup.y);
                        }

                        // Bei Doppelklick sofort springen
                        if (mainFrame.dClick) {
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

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Brunnen benutzen ?
                if (brBrunnen.IsPointInRect(pTemp) && !macIsVisible) {
                    nextActionID = 52;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pbrunnen);
                    mainFrame.repaint();
                    return;
                }

                // Stock mitnehmen ?
                if (brStock.IsPointInRect(pTemp) &&
                        !mainFrame.Actions[900]) {
                    nextActionID = 53;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pstock);
                    mainFrame.repaint();
                    return;
                }

                // mit Mutter reden
                if (brMac.IsPointInRect(pTemp) && macIsVisible) {
                    nextActionID = 54;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pmutter);
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
                    mainFrame.wegGeher.SetzeNeuenWeg(Ptuer);
                    mainFrame.repaint();
                    return;
                }

                // Schild mitnehmen
                if (brSchild.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Gaense mitnehmen
                if (gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                        gans3.GetHusaRect().IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pgaense);
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
        if (mainFrame.isMultiple) {
            Dialog.evalMouseMoveEvent(pTxxx);
            return;
        }

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
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
            mainFrame.invHighCursor = brBrunnen.IsPointInRect(pTemp) && !macIsVisible ||
                    brTuer.IsPointInRect(pTemp) && !macIsVisible ||
                    brSchild.IsPointInRect(pTemp) || tmp.IsPointInRect(pTemp) ||
                    brStock.IsPointInRect(pTemp) && !mainFrame.Actions[900] ||
                    gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                    gans3.GetHusaRect().IsPointInRect(pTemp);

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
            if (brBrunnen.IsPointInRect(pTemp) && !macIsVisible ||
                    brTuer.IsPointInRect(pTemp) && !macIsVisible ||
                    brSchild.IsPointInRect(pTemp) ||
                    brMac.IsPointInRect(pTemp) && macIsVisible ||
                    brStock.IsPointInRect(pTemp) && !mainFrame.Actions[900] ||
                    gans1.GetHusaRect().IsPointInRect(pTemp) || gans2.GetHusaRect().IsPointInRect(pTemp) ||
                    gans3.GetHusaRect().IsPointInRect(pTemp)) {
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

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultiple) {
            return;
        }

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
        TalkPerson = 0;
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
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

            // Look - DoActions

            case 1:
                // Brunnen anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00000"), Start.stringManager.getTranslation("Loc1_Doma1_00001"), Start.stringManager.getTranslation("Loc1_Doma1_00002"),
                        fBrunnen, 3, 0, 0);
                break;

            case 2:
                // Haustuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00003"), Start.stringManager.getTranslation("Loc1_Doma1_00004"), Start.stringManager.getTranslation("Loc1_Doma1_00005"),
                        fTuer, 3, 0, 0);
                break;

            case 3:
                // Stock anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00006"), Start.stringManager.getTranslation("Loc1_Doma1_00007"), Start.stringManager.getTranslation("Loc1_Doma1_00008"),
                        fStock, 3, 0, 0);
                break;

            case 4:
                // Schild anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00009"), Start.stringManager.getTranslation("Loc1_Doma1_00010"), Start.stringManager.getTranslation("Loc1_Doma1_00011"),
                        fSchild, 3, 0, 0);
                break;

            case 5:
                // Mutter anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00012"), Start.stringManager.getTranslation("Loc1_Doma1_00013"), Start.stringManager.getTranslation("Loc1_Doma1_00014"),
                        fMutter, 3, 0, 0);
                break;

            case 6:
                // Gaense anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00015"), Start.stringManager.getTranslation("Loc1_Doma1_00016"), Start.stringManager.getTranslation("Loc1_Doma1_00017"),
                        fGaense, 3, 0, 0);
                break;

            // Use - DoActions

            case 50:
                // ins Haus gehen ??
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00018"), Start.stringManager.getTranslation("Loc1_Doma1_00019"), Start.stringManager.getTranslation("Loc1_Doma1_00020"),
                        fTuer, 3, 0, 0);
                break;

            case 51:
                // Schild mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00021"), Start.stringManager.getTranslation("Loc1_Doma1_00022"), Start.stringManager.getTranslation("Loc1_Doma1_00023"),
                        fSchild, 3, 0, 0);
                break;

            case 52:
                // Brunnen benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00024"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00025"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00026"),
                        fBrunnen, 3, 0, 0);
                break;

            case 53:
                // Stock mitnehmen
                // zuerstmal richtig hinlaufen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pVorStock);
                nextActionID = 80;
                break;

            case 54:
                // Krabat beginnt MC (Mutter benutzen)
                mainFrame.krabat.SetFacing(fMutter);
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 630;
                break;
  
		/*      case 60:
			// Brunnen - Animation (Hallo Echo!)
			KrabatSagt (Start.stringManager.getTranslation("Loc1_Doma1_00027"), Start.stringManager.getTranslation("Loc1_Doma1_00028"), Start.stringManager.getTranslation("Loc1_Doma1_00029"),
			fBrunnen, 3, 5, 61);
			break;
        
			case 61:
			// Brunnen - Animation Ende
			KrabatSagt (Start.stringManager.getTranslation("Loc1_Doma1_00030"), Start.stringManager.getTranslation("Loc1_Doma1_00031"), Start.stringManager.getTranslation("Loc1_Doma1_00032"),
			fBrunnen, 3, 0, 0);
			mainFrame.fPlayAnim = false;
			evalMouseMoveEvent (mainFrame.Mousepoint);
			break;*/

            case 70:
                // Gaense mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00033"), Start.stringManager.getTranslation("Loc1_Doma1_00034"), Start.stringManager.getTranslation("Loc1_Doma1_00035"),
                        fGaense, 3, 0, 0);
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
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(Pstock);
                nextActionID = 95;
                break;

            case 95:
                // Ende Stockanim
                mainFrame.fPlayAnim = false;
                mainFrame.krabat.SetFacing(3); // nach rechts schauen, nachdeem der Stock genommen wurde
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                if (mainFrame.sprache == 1) {
                    outputText = Start.stringManager.getTranslation("Loc1_Doma1_00036");
                }
                if (mainFrame.sprache == 2) {
                    outputText = Start.stringManager.getTranslation("Loc1_Doma1_00037");
                }
                if (mainFrame.sprache == 3) {
                    outputText = Start.stringManager.getTranslation("Loc1_Doma1_00038");
                }
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
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00039"), Start.stringManager.getTranslation("Loc1_Doma1_00040"), Start.stringManager.getTranslation("Loc1_Doma1_00041"),
                        fBrunnen, 3, 0, 0);
                break;

            case 210:
                // Angel mit Wurm oder Holzfisch auf Brunnen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00042"), Start.stringManager.getTranslation("Loc1_Doma1_00043"), Start.stringManager.getTranslation("Loc1_Doma1_00044"),
                        fBrunnen, 3, 0, 0);
                break;

            case 220:
                // Fisch auf Brunnen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00045"), Start.stringManager.getTranslation("Loc1_Doma1_00046"), Start.stringManager.getTranslation("Loc1_Doma1_00047"),
                        fBrunnen, 3, 0, 0);
                break;

            case 230:
                // Kij auf Husy
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00048"), Start.stringManager.getTranslation("Loc1_Doma1_00049"), Start.stringManager.getTranslation("Loc1_Doma1_00050"),
                        fGaense, 3, 0, 0);
                break;

            case 240:
                // Lajna auf Husy
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00051"), Start.stringManager.getTranslation("Loc1_Doma1_00052"), Start.stringManager.getTranslation("Loc1_Doma1_00053"),
                        fGaense, 3, 0, 0);
                break;

            case 250:
                // Wacki oder Ryba auf Husy
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00054"), Start.stringManager.getTranslation("Loc1_Doma1_00055"), Start.stringManager.getTranslation("Loc1_Doma1_00056"),
                        fGaense, 3, 0, 0);
                break;

            // Multiple Choice

            case 600:
                // Intro: Krabat geht zur Mutter
                mainFrame.fPlayAnim = true;
                showLogo = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeNeuenWeg(Pmutter);
                mainFrame.repaint();
                nextActionID = 601;
                break;

            case 601:
                // Krabat steht vor Mutter
                if (mainFrame.isScrolling) {
                    break;
                }
                showLogo = false;
                mainFrame.Clipset = false;
                mainFrame.krabat.SetFacing(fMutter);
                nextActionID = 602;
                break;

            case 602:
                // 1. Argument Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Doma1_00057"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00058"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00059"),
                        fMutter, 1, 2, 603);
                break;

            case 603:
                // 1. Argument Mutter
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00060"), Start.stringManager.getTranslation("Loc1_Doma1_00061"), Start.stringManager.getTranslation("Loc1_Doma1_00062"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 610:
                // Krabat geht zur Mutter bei Versuch gehe nach Jitk
                mainFrame.wegGeher.SetzeNeuenWeg(Pmutter);
                mainFrame.repaint();
                nextActionID = 611;
                break;

            case 611:
                // Krabat wird von Mutter gefragt
                mainFrame.krabat.SetFacing(fMutter);
                if (mainFrame.isScrolling) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00063"), Start.stringManager.getTranslation("Loc1_Doma1_00064"), Start.stringManager.getTranslation("Loc1_Doma1_00065"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 630:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00066"), 1000, 1, new int[]{1}, 640);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00067"), 1000, 3, new int[]{3}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00068"), 3, 4, new int[]{4}, 670);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00069"), 1000, 2, new int[]{2}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00070"), 2, 5, new int[]{5}, 680);

                    // 4. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00071"), 6, 7, null, 690);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00072"), 7, 1000, null, 690);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00073"), 1000, 1, new int[]{1}, 640);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00074"), 1000, 3, new int[]{3}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00075"), 3, 4, new int[]{4}, 670);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00076"), 1000, 2, new int[]{2}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00077"), 2, 5, new int[]{5}, 680);

                    // 4. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00078"), 6, 7, null, 690);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00079"), 7, 1000, null, 690);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00080"), 1000, 1, new int[]{1}, 640);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00081"), 1000, 3, new int[]{3}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00082"), 3, 4, new int[]{4}, 670);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00083"), 1000, 2, new int[]{2}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00084"), 2, 5, new int[]{5}, 680);

                    // 4. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00085"), 6, 7, null, 690);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Doma1_00086"), 7, 1000, null, 690);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 631;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 631:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                // Exit bereitstellen
                mainFrame.Actions[6] = true;
                mainFrame.Actions[7] = true;

                nextActionID = Dialog.ActionID;

                // Wenn alles abgefragt, dann beenden
                if (mainFrame.Actions[1] && mainFrame.Actions[2] && mainFrame.Actions[3] &&
                        mainFrame.Actions[4] && mainFrame.Actions[5]) {
                    nextActionID = 700;
                }
                break;

            case 640:
                // Reaktion Mutter 1. Fr/ 1. T
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00087"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00088"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00089"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 650:
                // Reaktion Mutter 1. Fr/2. T
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00090"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00091"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00092"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 660:
                // Reaktion Mutter 2. Fr/1. T
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00093"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00094"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00095"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 670:
                // Reaktion Mutter 2. Fr/ 2. T
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00096"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00097"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00098"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 680:
                // Reaktion Mutter 2. Fr/ 3. T
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00099"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00100"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00101"),
                        0, 20, 2, 630, MacTalk);
                break;

            case 690:
                // Vorzeitiger Abbruch des Intros
                // System.out.println("Vorzeitig verlassen !");
                mainFrame.Actions[7] = false; // nico dale statt derje tak beim naechsten mal
                mainFrame.fPlayAnim = false;
                mainFrame.Clipset = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 700:
                // Mutter gibt auf - Erfolg
                PersonSagt(Start.stringManager.getTranslation("Loc1_Doma1_00102"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00103"),
                        Start.stringManager.getTranslation("Loc1_Doma1_00104"),
                        0, 20, 2, 710, MacTalk);
                break;

            case 710:
                // Krabat laeuft zurueck
                mainFrame.wegGeher.SetzeNeuenWeg(new GenericPoint(380, 400));
                mainFrame.repaint();
                nextActionID = 711;
                break;

            case 711:
                // Back to normal Gameplay (Mutter weg)
                macIsVisible = false;
                InitLocation();
                mainFrame.Actions[0] = true;
                showRauch = true;
                mainFrame.Clipset = false;
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
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }
}