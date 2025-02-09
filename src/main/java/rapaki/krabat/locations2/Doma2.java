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

package rapaki.krabat.locations2;

import rapaki.krabat.Start;
import rapaki.krabat.anims.Husa;
import rapaki.krabat.anims.Mac;
import rapaki.krabat.main.*;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.sound.BackgroundMusicPlayer;

public class Doma2 extends Mainloc
{
    private GenericImage background1, background2, back, brunnen, blatt; 
 
    private GenericImage[] Rauchanim;
    private int Rauchcount = 1;
 
    private int FadeToBlack = 0;

    private boolean switchanim = true;
    private Mac mutter;
    private boolean setScroll = false;
    private int scrollwert;
    private boolean Animation;
  
    private Husa gans1, gans2, gans3;

    private GenericPoint MacTalk;
    private GenericPoint Pmac;
    // private borderrect brMac;
    private boolean istMutterZuSehen = true;
  
    // Texte
    private static final String H1Text = Start.stringManager.getTranslation("Loc2_Doma2_00000");
    private static final String D1Text = Start.stringManager.getTranslation("Loc2_Doma2_00001");
    private static final String N1Text = Start.stringManager.getTranslation("Loc2_Doma2_00002");
  
    private static final String[] HKText = {Start.stringManager.getTranslation("Loc2_Doma2_00003"), Start.stringManager.getTranslation("Loc2_Doma2_00004"), Start.stringManager.getTranslation("Loc2_Doma2_00005"), Start.stringManager.getTranslation("Loc2_Doma2_00006"), Start.stringManager.getTranslation("Loc2_Doma2_00007")};
    private static final String[] DKText = {Start.stringManager.getTranslation("Loc2_Doma2_00008"), Start.stringManager.getTranslation("Loc2_Doma2_00009"), Start.stringManager.getTranslation("Loc2_Doma2_00010"), Start.stringManager.getTranslation("Loc2_Doma2_00011"), Start.stringManager.getTranslation("Loc2_Doma2_00012")};
    private static final String[] NKText = {Start.stringManager.getTranslation("Loc2_Doma2_00013"), Start.stringManager.getTranslation("Loc2_Doma2_00014"), Start.stringManager.getTranslation("Loc2_Doma2_00015"), Start.stringManager.getTranslation("Loc2_Doma2_00016"), Start.stringManager.getTranslation("Loc2_Doma2_00017")};
    private static final int KONSTANTE = 4;  
    private int Position = 0;

    private static final String H2Text = Start.stringManager.getTranslation("Loc2_Doma2_00018");
    private static final String D2Text = Start.stringManager.getTranslation("Loc2_Doma2_00019");
    private static final String N2Text = Start.stringManager.getTranslation("Loc2_Doma2_00020");

    private static final String H3Text = Start.stringManager.getTranslation("Loc2_Doma2_00021");
    private static final String D3Text = Start.stringManager.getTranslation("Loc2_Doma2_00022");
    private static final String N3Text = Start.stringManager.getTranslation("Loc2_Doma2_00023");
    
    private static final String H4Text = Start.stringManager.getTranslation("Loc2_Doma2_00024");
    private static final String D4Text = Start.stringManager.getTranslation("Loc2_Doma2_00025");
    private static final String N4Text = Start.stringManager.getTranslation("Loc2_Doma2_00026");
    
    // Walkto - Points fuer Dinge in Location
    private static final GenericPoint Pmutter    = new GenericPoint ( 840, 400);
    private static final GenericPoint Ptuer      = new GenericPoint ( 885, 355);
    private static final GenericPoint Pschild    = new GenericPoint ( 338, 369);
    private static final GenericPoint Pbrunnen   = new GenericPoint (1178, 370);
    private static final GenericPoint Pup        = new GenericPoint ( 412, 250); 
    private static final GenericPoint MutterFeet = new GenericPoint ( 879, 401);
    private static final GenericPoint Pgaense    = new GenericPoint ( 254, 357);
  
    // Konstanten - Rects initialisieren
    private static final Borderrect brunnenRect   = new Borderrect (1024, 300, 1279, 479); 
    private static final Borderrect blattRect     = new Borderrect ( 751, 379,  856, 464);
    private static final Borderrect brTuer        = new Borderrect ( 900, 290,  920, 340);
    private static final Borderrect brBrunnen     = new Borderrect (1130, 350, 1230, 410);
    private static final Borderrect obererAusgang = new Borderrect ( 386, 229,  425, 286);
    private static final Borderrect brSchild      = new Borderrect ( 330, 267,  367, 283);
  
    // Konstante ints fuer Facing
    private static final int fBrunnen = 6;
    private static final int fTuer    = 3;
    private static final int fStock   = 9;
    private static final int fSchild  = 12;
    // private static final int fMutter  = 3;
    private static final int fGaense  = 12;
  
    public Doma2 (Start caller, int oldLocation)
    {
	super (caller);

	mainFrame.Freeze (true);
	mainFrame.CheckKrabat ();

	gans1 = new Husa (mainFrame, new Borderrect (150, 260, 197, 270));
	gans2 = new Husa (mainFrame, new Borderrect (238, 270, 285, 280));
	gans3 = new Husa (mainFrame, new Borderrect (182, 300, 276, 310));

	TalkPerson = 0;
        BackgroundMusicPlayer.getInstance ().playTrack (4, true);
        
	mainFrame.krabat.maxx = 402;
	mainFrame.krabat.zoomf = 2.32f;
	mainFrame.krabat.defScale = 0;
  	
	Rauchanim = new GenericImage [13]; 

	InitImages();
	Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird
    
	switch (oldLocation)
	    {
	    case 0: // Einsprung von Load
		// Mutter kann nicht da sein, weil das Anim waere
		istMutterZuSehen = false;
		break;
      	
	    case 74: // Aus Jitk kommend, also 1. Animation
		mainFrame.krabat.SetKrabatPos (new GenericPoint (413, 275));
		mainFrame.krabat.SetFacing (6);
		scrollwert = 88;
		setScroll = true;
		Animation = true;
		istMutterZuSehen = true;
		break;
        
	    case 90: // spaeter aus der Muehle2 zurueck
		mainFrame.krabat.SetKrabatPos (new GenericPoint (162, 356));
    		mainFrame.krabat.SetFacing (3);
    		scrollwert = 0;
    		setScroll = true;
		Animation = true;
		istMutterZuSehen = false;
		break;
        
	    case 87: // von Wjes aus (ueber Karte) oder Bote-Anim
		if (mainFrame.Actions[303] == false)
		    {
			mainFrame.krabat.SetKrabatPos (new GenericPoint (413, 275));
			mainFrame.krabat.SetFacing (6);
			scrollwert = 88;
			istMutterZuSehen = false;
		    }
		else
		    {
			mainFrame.krabat.SetKrabatPos (new GenericPoint (840, 400));
			mainFrame.krabat.SetFacing (3);
			scrollwert = 520;
			Animation = true;
			istMutterZuSehen = true;
		    }
		setScroll = true;
		break;  
	    }    

	mutter = new Mac (mainFrame, true);

	Pmac = new GenericPoint ();
	Pmac.x = MutterFeet.x - (Mac.Breite / 2);
	Pmac.y = MutterFeet.y - Mac.Hoehe;
    
	MacTalk = new GenericPoint();
	MacTalk.x = MutterFeet.x;
	MacTalk.y = Pmac.y - 50;
    
	// brMac = new borderrect (Pmac.x, Pmac.y, Pmac.x + mac.Breite, Pmac.y + mac.Hoehe);

	InitLocation ();

	mainFrame.Freeze (false);
    } 
    
    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation ()
    {

	mainFrame.wegGeher.vBorders.removeAllElements ();

	// Grenzen setzen
	// je nach dem, ob Mutter da ist oder nicht
	if (istMutterZuSehen == false)
	    {	
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (411, 423, 396, 420, 281, 329));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (396, 420, 382, 470, 330, 364));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (135, 158, 187, 218, 351, 364)); 
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (187, 496, 255, 496, 365, 410));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (497, 353, 639, 411));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (640, 370, 824, 408));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (825, 346, 866, 414));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (867, 373,1015, 403));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (1016,365,1127, 416));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (1128,366,1200, 392));

		// Matrix loeschen
		mainFrame.wegSucher.ClearMatrix (10);
	
		// moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
		mainFrame.wegSucher.PosVerbinden (0, 1); 
		mainFrame.wegSucher.PosVerbinden (1, 3); 
		mainFrame.wegSucher.PosVerbinden (2, 3); 
		mainFrame.wegSucher.PosVerbinden (3, 4);
		mainFrame.wegSucher.PosVerbinden (4, 5); 
		mainFrame.wegSucher.PosVerbinden (5, 6);
		mainFrame.wegSucher.PosVerbinden (6, 7); 
		mainFrame.wegSucher.PosVerbinden (7, 8);
		mainFrame.wegSucher.PosVerbinden (8, 9); 
	    }
	else
	    {
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (411, 423, 396, 420, 281, 329));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (396, 420, 382, 470, 330, 364));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (135, 158, 187, 218, 351, 364)); 
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (187, 496, 255, 496, 365, 410));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (497, 353, 639, 411));
		mainFrame.wegGeher.vBorders.addElement (new Bordertrapez (640, 370, 824, 408));
    
		// Matrix loeschen
		mainFrame.wegSucher.ClearMatrix (6);
	
		// moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
		mainFrame.wegSucher.PosVerbinden (0, 1); 
		mainFrame.wegSucher.PosVerbinden (1, 3); 
		mainFrame.wegSucher.PosVerbinden (2, 3); 
		mainFrame.wegSucher.PosVerbinden (3, 4);
		mainFrame.wegSucher.PosVerbinden (4, 5); 
	    }
    
    }
  
    // Bilder vorbereiten
    private void InitImages() 
    {
	background1   = getPicture ("gfx/doma/dom-l.gif");
	background2   = getPicture ("gfx/doma/dom-r.gif");
	back          = getPicture ("gfx/doma/domsky.gif");
	brunnen       = getPicture ("gfx/doma/brunnen.gif");
	blatt         = getPicture ("gfx/doma/blatt.gif");
    
	Rauchanim[1]  = getPicture ("gfx/doma/ra1.gif");
	Rauchanim[2]  = getPicture ("gfx/doma/ra2.gif");
	Rauchanim[3]  = getPicture ("gfx/doma/ra3.gif");
	Rauchanim[4]  = getPicture ("gfx/doma/ra4.gif");
	Rauchanim[5]  = getPicture ("gfx/doma/ra5.gif");
	Rauchanim[6]  = getPicture ("gfx/doma/ra6.gif");
	Rauchanim[7]  = getPicture ("gfx/doma/ra7.gif");
	Rauchanim[8]  = getPicture ("gfx/doma/ra8.gif");
	Rauchanim[9]  = getPicture ("gfx/doma/ra9.gif");
	Rauchanim[10] = getPicture ("gfx/doma/ra10.gif");
	Rauchanim[11] = getPicture ("gfx/doma/ra11.gif");
	Rauchanim[12] = getPicture ("gfx/doma/ra12.gif");

	loadPicture();
    }

    public void cleanup() 
    {
	background1   = null;
	background2   = null;
	back          = null;
	brunnen       = null;
	blatt         = null;
    
	Rauchanim[1]  = null;
	Rauchanim[2]  = null;
	Rauchanim[3]  = null;
	Rauchanim[4]  = null;
	Rauchanim[5]  = null;
	Rauchanim[6]  = null;
	Rauchanim[7]  = null;
	Rauchanim[8]  = null;
	Rauchanim[9]  = null;
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

    public void paintLocation (GenericDrawingContext g)
    {
	// Clipping - Region initialisieren und Rauchthread aktivieren
	if (mainFrame.Clipset == false)
	    {
		mainFrame.Clipset = true;
		if (setScroll == true) 
		    {
			setScroll = false;
			mainFrame.scrollx = scrollwert;
		    }	
		Cursorform = 200;
		evalMouseMoveEvent (mainFrame.Mousepoint);
		g.setClip(0, 0, 1284, 964);
		mainFrame.isAnim = true;
		if (Animation == true) mainFrame.fPlayAnim = true;
	    }
    
	// Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
	g.drawImage (back, (mainFrame.scrollx / 10), 0, null);
	g.drawImage (background1,   0, 0, null);
	g.drawImage (background2, 640, 0, null);
    
	// Parallax - Scrolling ausfuehren
	if (mainFrame.isScrolling == true)
	    {
		int xtemp = mainFrame.scrollx - 5;
		if (xtemp < 0) xtemp = 0;
		g.setClip   (xtemp, 0, 650, 285);
		g.drawImage (back, (mainFrame.scrollx / 10), 0, null);
		g.drawImage (background1,   0, 0, null);
		g.drawImage (background2, 640, 0, null);
	    }  
        
	// Ab hier ist Retten des ClipRect sinnlos!!!
	// Rauch animieren
	if ((mainFrame.isAnim == true) && (mainFrame.scrollx > 300))
	    {
		switchanim = !(switchanim);
		if (switchanim == true)
		    {
			Rauchcount++;
			if (Rauchcount == 13) Rauchcount = 1;
		    }  
		g.setClip (985, 15, 30, 120);
		g.drawImage (back, (mainFrame.scrollx / 10), 0, null);
		g.drawImage (Rauchanim[Rauchcount], 985, 15, null);
		g.drawImage (background2, 640, 0, null);
	    }
    
	// Gaense animieren
	if ((mainFrame.isAnim == true) && (mainFrame.scrollx < 350))
	    {
		g.setClip (120, 255, 230, 110);
		g.drawImage (back, (mainFrame.scrollx / 10), 0, null);
		g.drawImage (background1,   0, 0, null);
		g.drawImage (background2, 640, 0, null);
		gans1.BewegeGans (g);
		gans2.BewegeGans (g);
		gans3.BewegeGans (g);
	    }  

	// Debugging - Zeichnen der Laufrechtecke
	// mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

	// Krabats neue Position festlegen wenn noetig
	mainFrame.wegGeher.GeheWeg();
    
	// Mac zeichnen bei Reden und Herumstehen, vorher Hintergrund wiederherstellen
	if ((mainFrame.scrollx > 130)  && (istMutterZuSehen == true))
	    {
		g.setClip (Pmac.x, Pmac.y, Mac.Breite, Mac.Hoehe);
		g.drawImage (background2, 640, 0, null);
		mutter.drawMac (g, Pmac, TalkPerson);
	    }	

	// Krabat zeichnen

	// Animation??
	if (mainFrame.krabat.nAnimation != 0)
	    { 
		mainFrame.krabat.DoAnimation (g);
      
		// Cursorruecksetzung nach Animationsende
		if (mainFrame.krabat.nAnimation == 0) evalMouseMoveEvent (mainFrame.Mousepoint);
	    }  
	else
	    {
		if ((mainFrame.talkCount > 0) && (TalkPerson != 0))
		    {
			// beim Reden
			switch (TalkPerson)
			    {
			    case 1:
				// Krabat spricht gestikulierend
				mainFrame.krabat.talkKrabat (g);
				break;
			    case 3:
				// Krabat spricht im Monolog
				mainFrame.krabat.describeKrabat (g);
				break;
			    default:
				// steht Krabat nur da
				mainFrame.krabat.drawKrabat (g);
				break;
			    }    
		    }
		// Rumstehen oder Laufen
		else mainFrame.krabat.drawKrabat (g);
	    }  
    
	// Ab hier muss Cliprect wieder gerettet werden
	// Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
	GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

	// hinterm Brunnen (nur Clipping - Region wird neugezeichnet)
	if (brunnenRect.IsPointInRect (pKrTemp) == true)
	    {
		g.drawImage (brunnen, 1055, 251, null);
	    }

	//hinterm Blatt
	if (blattRect.IsPointInRect (pKrTemp) == true)
	    {
		g.drawImage (blatt, 764, 393, null);
	    }
      
        // Hier das FadeToBlack, wenn noetig
        if (FadeToBlack > 0)
            {
                GenericRectangle my;
                my = g.getClipBounds();
                g.setClip (mainFrame.scrollx, mainFrame.scrolly, 644, 484);
      
                g.clearRect (mainFrame.scrollx                    , mainFrame.scrolly                    ,
			     FadeToBlack, 479);
                g.clearRect (mainFrame.scrollx + 639 - FadeToBlack, mainFrame.scrolly                    ,
			     639, 479);
                g.clearRect (mainFrame.scrollx                    , mainFrame.scrolly                    ,
			     639, FadeToBlack);
                g.clearRect (mainFrame.scrollx                    , mainFrame.scrolly + 479 - FadeToBlack,
			     639, 479);
      
                g.setClip( (int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight()); 
            }

	// Textausgabe, falls noetig
	if (outputText != "")
	    {
		GenericRectangle my;
		my = g.getClipBounds();
		g.setClip (0, 0, 1284, 964);
		mainFrame.ifont.drawString (g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
		g.setClip( (int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight()); 
	    }

	// Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
	if (mainFrame.talkCount > 0)
	    {
		-- mainFrame.talkCount;
		if (mainFrame.talkCount <= 1)
		    {
			mainFrame.Clipset = false;
			outputText = "";
			TalkPerson = 0;
		    }
	    }  
    
	if ((TalkPause > 0) && (mainFrame.talkCount < 1)) TalkPause--;

	// Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!
    
	// Anims einschalten, wenn noetig
	if (Animation == true)
	    {
		Animation = false;
		mainFrame.krabat.StopWalking ();
		if (mainFrame.Actions[300] == true)
		    {
			mainFrame.Actions[300] = false;
			nextActionID = 600;
		    }
		else
		    {
			if (mainFrame.Actions[303] == true)
			    {
				nextActionID = 1000;
			    }
			else
			    {
				nextActionID = 800;
			    }
		    }
	    }			
    
	if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) DoAction ();
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent (GenericMouseEvent e)
    {
	// Auszugebenden Text abbrechen
	outputText="";
	if (mainFrame.talkCount != 0) mainFrame.Clipset = false;
	if (mainFrame.talkCount > 1) 
	    {
		mainFrame.talkCount = 1;
		TalkPerson = 0;
	    }  
    
	// Cursorpunkt mit Scrolloffset berechnen 
	GenericPoint pTemp = e.getPoint ();
	pTemp.x += mainFrame.scrollx;

	// Wenn in Animation, dann normales Gameplay aussetzen
	if (mainFrame.fPlayAnim == true)
	    {
		return;
	    }
    
	// Wenn Krabat - Animation, dann normales Gameplay aussetzen
	if (mainFrame.krabat.nAnimation != 0)
	    {
		return;
	    }    

	// wenn InventarCursor, dann anders reagieren
	if (mainFrame.invCursor == true)
	    {
		// linke Maustaste
		if (e.getModifiers () != GenericInputEvent.BUTTON3_MASK)
		    {
			nextActionID = 0;

			Borderrect tmp = mainFrame.krabat.KrabatRect();

			// Aktion, wenn Krabat angeclickt wurde
			if (tmp.IsPointInRect (pTemp) == true)
			    {
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
			if ((brBrunnen.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false))
			    {
				switch (mainFrame.whatItem)
				    {
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
			if ((brTuer.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false))
			    {
				// nur Standard
				nextActionID = 165;
				pTemp = Ptuer;
			    }				        
        
			// Ausreden fuer Schild
			if (brSchild.IsPointInRect (pTemp) == true)
			    {
				// nur Standard
				nextActionID = 170;
				pTemp = Pschild;
			    }				        
        
			// Ausreden fuer Gaense
			if ((gans1.GetHusaRect().IsPointInRect (pTemp) == true) || (gans2.GetHusaRect().IsPointInRect (pTemp) == true) ||
			    (gans3.GetHusaRect().IsPointInRect (pTemp) == true))
			    {
				switch (mainFrame.whatItem)
				    {
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
			mainFrame.wegGeher.SetzeNeuenWeg (pTemp);
			mainFrame.repaint();
		    }
      
		// rechte Maustaste
		else
		    {
			// Gegenstand grundsaetzlich wieder ablegen
			mainFrame.invCursor = false;
			evalMouseMoveEvent (mainFrame.Mousepoint);
			nextActionID = 0;
			mainFrame.krabat.StopWalking();
			mainFrame.repaint();
			return;
		    }  
	    }

	// normaler Cursor, normale Reaktion
	else
	    {
		if (e.getModifiers () != GenericInputEvent.BUTTON3_MASK)
		    {   
			// linke Maustaste
			nextActionID = 0;

			// Brunnen ansehen
			if ((brBrunnen.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false))
			    {
				nextActionID = 1;
				pTemp = Pbrunnen;
			    }  

			// Tuer ansehen
			if ((brTuer.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false))
			    {
				nextActionID = 2;
				pTemp = Ptuer;
			    }

			// Schild ansehen
			if (brSchild.IsPointInRect (pTemp) == true)
			    {
				pTemp = Pschild;
				nextActionID = 4;
			    }

			// nach Jitk gehen oder zurueckgepfiffen werden
			if (obererAusgang.IsPointInRect (pTemp) == true)
			    {
				nextActionID = 100;
            
				GenericPoint kt = mainFrame.krabat.GetKrabatPos();
          
				// Wenn nahe am Ausgang, dann "gerade" verlassen
				if (obererAusgang.IsPointInRect (kt) == false)
				    {
					pTemp = Pup;
				    }
				else
				    {
					pTemp = new GenericPoint (kt.x, Pup.y);
				    }
            
				// Bei Doppelklick sofort springen			
				if (mainFrame.dClick == true)
				    {
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
			if ((gans1.GetHusaRect().IsPointInRect (pTemp) == true) || (gans2.GetHusaRect().IsPointInRect (pTemp) == true) ||
			    (gans3.GetHusaRect().IsPointInRect (pTemp) == true))
			    {
				pTemp = Pgaense;
				nextActionID = 6;
			    }

			mainFrame.wegGeher.SetzeNeuenWeg (pTemp);
			mainFrame.repaint();
		    }

		else
		    {
			// rechte Maustaste

			// Brunnen benutzen ?
			if ((brBrunnen.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false))
			    {
				nextActionID = 52;
				mainFrame.wegGeher.SetzeNeuenWeg (Pbrunnen);
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
			if (obererAusgang.IsPointInRect (pTemp) == true)
			    {
				return;
			    }

			// ins Haus gehen
			if ((brTuer.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false))
			    {
				nextActionID = 50;
				mainFrame.wegGeher.SetzeNeuenWeg (Ptuer);
				mainFrame.repaint();
				return;
			    }

			// Schild mitnehmen
			if (brSchild.IsPointInRect (pTemp) == true)
			    {
				nextActionID = 51;
				mainFrame.wegGeher.SetzeNeuenWeg (Pschild);
				mainFrame.repaint();
				return;
			    }  

			// Gaense mitnehmen
			if ((gans1.GetHusaRect().IsPointInRect (pTemp) == true) || (gans2.GetHusaRect().IsPointInRect (pTemp) == true) ||
			    (gans3.GetHusaRect().IsPointInRect (pTemp) == true))
			    {
				nextActionID = 70;
				mainFrame.wegGeher.SetzeNeuenWeg (Pgaense);
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


    public void evalMouseMoveEvent (GenericPoint pTxxx)
    {
	// Wenn Animation oder Krabat - Animation, dann transparenter Cursor
	if ((mainFrame.fPlayAnim == true) || (mainFrame.krabat.nAnimation != 0))
	    {
		if (Cursorform != 20)
		    {
			Cursorform = 20;
			mainFrame.setCursor (mainFrame.Nix);
		    }
		return;		
	    }
 	
  	// neuen Punkt erzeugen wg. Referenzgleichheit
	GenericPoint pTemp = new GenericPoint (pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

	// wenn InventarCursor, dann anders reagieren
	if (mainFrame.invCursor == true)
	    {
		// hier kommt Routine hin, die Highlight berechnet
		Borderrect tmp = mainFrame.krabat.KrabatRect();
		if (((brBrunnen.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false)) ||
		    ((brTuer.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false)) ||
		    (brSchild.IsPointInRect (pTemp) == true) || (tmp.IsPointInRect (pTemp) == true) ||
		    (gans1.GetHusaRect().IsPointInRect (pTemp) == true) || (gans2.GetHusaRect().IsPointInRect (pTemp) == true) ||
		    (gans3.GetHusaRect().IsPointInRect (pTemp) == true))
		    {
			mainFrame.invHighCursor = true;
		    }
		else mainFrame.invHighCursor = false;
    	
		if ((Cursorform != 10) && (mainFrame.invHighCursor == false))
		    {
			Cursorform = 10;
			mainFrame.setCursor (mainFrame.Cinventar);
		    }
    	
		if ((Cursorform != 11) && (mainFrame.invHighCursor == true))
		    {
			Cursorform = 11;
			mainFrame.setCursor (mainFrame.CHinventar);
		    }	
	    }
    
	// normaler Cursor, normale Reaktion
	else
	    {
		if (((brBrunnen.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false)) ||
		    ((brTuer.IsPointInRect (pTemp) == true) && (istMutterZuSehen == false)) ||
		    (brSchild.IsPointInRect (pTemp) == true) ||
		    /*(brMac.IsPointInRect (pTemp) == true))*/
		    (gans1.GetHusaRect().IsPointInRect (pTemp) == true) || (gans2.GetHusaRect().IsPointInRect (pTemp) == true) ||
		    (gans3.GetHusaRect().IsPointInRect (pTemp) == true))
		    {
			if (Cursorform != 1)
			    {
				mainFrame.setCursor (mainFrame.Kreuz);
				Cursorform = 1;
			    }
			return;
		    }

		if (obererAusgang.IsPointInRect (pTemp) == true)
		    {
			if (Cursorform != 4)
			    {
				mainFrame.setCursor (mainFrame.Cup);
				Cursorform = 4;
			    }
			return;
		    }  
    
		// sonst normal-Cursor
		if (Cursorform != 0)
		    {
			mainFrame.setCursor (mainFrame.Normal);
			Cursorform = 0;
		    }
	    }
    }
  
    public void evalMouseExitEvent (GenericMouseEvent e)
    {
    }		

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent (GenericKeyEvent e)
    {
	// Wenn Inventarcursor, dann keine Keys
	if (mainFrame.invCursor == true) return;

	// Bei Animationen keine Keys
	if (mainFrame.fPlayAnim == true) return;
    
	// Bei Krabat - Animation keine Keys
	if (mainFrame.krabat.nAnimation != 0) return;

	// Nur auf Funktionstasten reagieren
	int Taste = e.getKeyCode();

	// Hauptmenue aktivieren
	if (Taste == GenericKeyEvent.VK_F1)
	    {
		Keyclear();
		nextActionID = 122;
		mainFrame.repaint();
		return;
	    }    

	// Save - Screen aktivieren
	if (Taste == GenericKeyEvent.VK_F2)
	    {
		Keyclear();
		nextActionID = 121;
		mainFrame.repaint();
		return;
	    }

	// Load - Screen aktivieren
	if (Taste == GenericKeyEvent.VK_F3)
	    {
		Keyclear();
		nextActionID = 120;
		mainFrame.repaint();
		return;
	    } 
    }  

    // Vor Key - Events alles deaktivieren
    private void Keyclear()
    {
	outputText="";
	if (mainFrame.talkCount > 1) mainFrame.talkCount = 1;
	TalkPerson = 0;
	mainFrame.Clipset = false;
	mainFrame.isAnim = false;
	mainFrame.krabat.StopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction ()
    {
        
	// nichts zu tun, oder Krabat laeuft noch
	if (((mainFrame.krabat.isWandering == true) || (mainFrame.krabat.isWalking == true)) && (nextActionID != 400))
	    return;

	// hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
	if ((nextActionID > 499) && (nextActionID < 600))
	    {
		setKrabatAusrede();
    	
		// manche Ausreden erfordern neuen Cursor !!!
    	
		evalMouseMoveEvent (mainFrame.Mousepoint);
    	
		return;
	    }		

	// System.out.println("Nextaction " + nextActionID);
  	
  	// Hier Evaluation der Screenaufrufe, in Superklasse
  	if ((nextActionID > 119) && (nextActionID < 129))
	    {
  		SwitchScreen ();
  		return;
	    }		
  	
  	// Was soll Krabat machen ?
	switch (nextActionID)
	    {
      
		// Look - DoActions
      
	    case 1:
		// Brunnen anschauen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00027"), Start.stringManager.getTranslation("Loc2_Doma2_00028"), Start.stringManager.getTranslation("Loc2_Doma2_00029"),
			    fBrunnen, 3, 0, 0);
		break;

	    case 2:
		// Haustuer anschauen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00030"), Start.stringManager.getTranslation("Loc2_Doma2_00031"), Start.stringManager.getTranslation("Loc2_Doma2_00032"),
			    fTuer, 3, 0, 0);
		break;

	    case 3:
		// Stock anschauen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00033"), Start.stringManager.getTranslation("Loc2_Doma2_00034"), Start.stringManager.getTranslation("Loc2_Doma2_00035"),
			    fStock, 3, 0, 0);
		break;

	    case 4:
		// Schild anschauen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00036"), Start.stringManager.getTranslation("Loc2_Doma2_00037"), Start.stringManager.getTranslation("Loc2_Doma2_00038"),
			    fSchild, 3, 0, 0);
		break;

		/*      case 5:
			// Mutter anschauen
			KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00039"), Start.stringManager.getTranslation("Loc2_Doma2_00040"), Start.stringManager.getTranslation("Loc2_Doma2_00041"),
			fMutter, 3, 0, 0);
			break;*/

	    case 6:
		// Gaense anschauen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00042"), Start.stringManager.getTranslation("Loc2_Doma2_00043"), Start.stringManager.getTranslation("Loc2_Doma2_00044"),
			    fGaense, 3, 0, 0);
		break;

		// Use - DoActions
      
	    case 50:
		// ins Haus gehen ??
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00045"), Start.stringManager.getTranslation("Loc2_Doma2_00046"), Start.stringManager.getTranslation("Loc2_Doma2_00047"),
			    fTuer, 3, 0, 0);
		break;

	    case 51:
		// Schild mitnehmen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00048"), Start.stringManager.getTranslation("Loc2_Doma2_00049"), Start.stringManager.getTranslation("Loc2_Doma2_00050"),
			    fSchild, 3, 0, 0);
		break;

	    case 52:
		// Brunnen benutzen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00051"), 
			    Start.stringManager.getTranslation("Loc2_Doma2_00052"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00053"),
			    fBrunnen, 3, 0, 0);
		break;

	    case 70:
		// Gaense mitnehmen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00054"), Start.stringManager.getTranslation("Loc2_Doma2_00055"), Start.stringManager.getTranslation("Loc2_Doma2_00056"), 
			    fGaense, 3, 0, 0);
		break;

		// GoTo DoActions
      
	    case 100:
		// Karte einblenden
		mainFrame.ConstructLocation (106);
		mainFrame.isAnim = false;
		mainFrame.whatScreen = 6;
		nextActionID = 0;
		mainFrame.Clipset = false;
		mainFrame.repaint();
		break;
  
		// Ausreden fuer Benutze Gegenstand mit Gegenstand

	    case 160:
		// Brunnen - Ausreden
		DingAusrede (fBrunnen);
		break;
      
	    case 165:
		// Tuer - Ausreden
		DingAusrede (fTuer);
		break;
        
	    case 170:
		// Schild - Ausreden
		DingAusrede (fSchild);
		break;
      
	    case 175:
		// Gaense - Ausreden
		APersonAusrede (fGaense);
		break;
      
	    case 200:
		// Angel auf Brunnen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00057"), Start.stringManager.getTranslation("Loc2_Doma2_00058"), Start.stringManager.getTranslation("Loc2_Doma2_00059"),
			    fBrunnen, 3, 0, 0);
		break;

	    case 210:
		// Angel mit Wurm oder Holzfisch auf Brunnen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00060"), Start.stringManager.getTranslation("Loc2_Doma2_00061"), Start.stringManager.getTranslation("Loc2_Doma2_00062"),
			    fBrunnen, 3, 0, 0);
		break;

	    case 220:
		// Fisch auf Brunnen
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00063"), Start.stringManager.getTranslation("Loc2_Doma2_00064"), Start.stringManager.getTranslation("Loc2_Doma2_00065"),
			    fBrunnen, 3, 0, 0);
		break;

	    case 230:
		// Kij auf Husy
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00066"), Start.stringManager.getTranslation("Loc2_Doma2_00067"), Start.stringManager.getTranslation("Loc2_Doma2_00068"),
			    fGaense, 3, 0, 0);
		break;

	    case 240:
		// Lajna auf Husy
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00069"), Start.stringManager.getTranslation("Loc2_Doma2_00070"), Start.stringManager.getTranslation("Loc2_Doma2_00071"),
			    fGaense, 3, 0, 0);
		break;

	    case 250:
		// Wacki oder Ryba auf Husy
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00072"), Start.stringManager.getTranslation("Loc2_Doma2_00073"), Start.stringManager.getTranslation("Loc2_Doma2_00074"),
			    fGaense, 3, 0, 0);
		break;

		// Multiple Choice      

	    case 600:
		// Intro: Krabat geht zur Mutter
		mainFrame.fPlayAnim = true;
		evalMouseMoveEvent (mainFrame.Mousepoint);
		mainFrame.wegGeher.SetzeNeuenWeg (Pmutter);
		mainFrame.repaint();
		nextActionID = 601;
		break;
        
	    case 601:
		// Krabat steht vor Mutter
		if (mainFrame.isScrolling == true) break;
		mainFrame.krabat.SetFacing(3);
		nextActionID = 610;
		break;

	    case 610:
		// Mutter spricht
		PersonSagt (Start.stringManager.getTranslation("Loc2_Doma2_00075"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00076"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00077"),
			    0, 20, 2, 620, MacTalk);
		break;

	    case 620:
		// Mutter spricht
		PersonSagt (Start.stringManager.getTranslation("Loc2_Doma2_00078"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00079"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00080"),
			    0, 20, 2, 630, MacTalk);
		break;

	    case 630:
		// Krabat spricht
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00081"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00082"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00083"),
			    0, 1, 2, 640);
		break;
        
	    case 640:
		// Mutter spricht
		PersonSagt (Start.stringManager.getTranslation("Loc2_Doma2_00084"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00085"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00086"),
			    0, 20, 2, 650, MacTalk);
		break;

	    case 650:
		// Krabat spricht
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00087"), Start.stringManager.getTranslation("Loc2_Doma2_00088"), Start.stringManager.getTranslation("Loc2_Doma2_00089"),
			    0, 1, 2, 660);
		break;
        
	    case 660:
		// Text Erzaehler
		PersonSagt (H1Text, D1Text, N1Text, 0, 54, 2, 670, new GenericPoint (320, 200));
		break;
        
	    case 670:
		// Text von Krabat
		KrabatSagt (HKText[Position], DKText[Position], NKText[Position],
			    0, 1, 2, 670);
		Position++;
		if (Position > KONSTANTE) nextActionID = 680;
		break;
        
	    case 680:
		// Mutter spricht
		PersonSagt (Start.stringManager.getTranslation("Loc2_Doma2_00090"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00091"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00092"),
			    0, 20, 2, 690, MacTalk);
		break;

	    case 690:
		// Krabat spricht
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00093"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00094"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00095"),
			    0, 1, 2, 700);
		break;
        
	    case 700:
		// Ausfaden und Ruhe
		FadeToBlack = 1;
		nextActionID = 710;
		break;

            case 710:
                // bis zum Ende warten
                FadeToBlack += 3;
                if (FadeToBlack >= 246) nextActionID = 720;
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
		NeuesBild (90, 71);
		break;

	    case 800:
		// Text Erzaehler
		PersonSagt (H2Text, D2Text, N2Text, 0, 54, 2, 805, new GenericPoint (320, 200));
		break;
        
	    case 805:
		// Text Erzaehler
		PersonSagt (H3Text, D3Text, N3Text, 0, 54, 2, 808, new GenericPoint (320, 250));
		break;
        
	    case 808:
		// Text Erzaehler
		PersonSagt (H4Text, D4Text, N4Text, 0, 54, 2, 810, new GenericPoint (320, 250));
		break;
        
	    case 810:
		mainFrame.fPlayAnim = false;
		mainFrame.Clipset = false;
		evalMouseMoveEvent (mainFrame.Mousepoint);
		nextActionID = 0;
		break;  
      	
	    case 1000:
		// Anim, nachdem Bote gesprochen hat
		// Mutter spricht
		PersonSagt (Start.stringManager.getTranslation("Loc2_Doma2_00096"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00097"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00098"),
			    0, 20, 2, 1010, MacTalk);
		break;

	    case 1010:
		// Text von Krabat
		KrabatSagt (Start.stringManager.getTranslation("Loc2_Doma2_00099"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00100"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00101"),
			    0, 1, 2, 1020);
		break;
        
	    case 1020:
		// Mutter spricht
		PersonSagt (Start.stringManager.getTranslation("Loc2_Doma2_00102"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00103"),
			    Start.stringManager.getTranslation("Loc2_Doma2_00104"),
			    0, 20, 2, 1030, MacTalk);
		break;
        
	    case 1030:
		// Skip zu Most
		NeuesBild (89, 71);
		break;

	    default:  
		System.out.println ("Falsche Action-ID !");
	    }
    }
}