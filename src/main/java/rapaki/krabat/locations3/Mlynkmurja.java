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

package rapaki.krabat.locations3;

import rapaki.krabat.Start;
import rapaki.krabat.anims.Oldmlynk;
import rapaki.krabat.main.*;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.sound.BackgroundMusicPlayer;

public class Mlynkmurja extends Mainloc
{
    private GenericImage backr, laterne; 
  
    private Oldmlynk mueller;
    private GenericPoint muellerPoint;
    private GenericPoint muellerTalk;
  
    private boolean krabatVisible = true;
  
    private int laterneAdd;

    private boolean initplay = true;

    // Konstante Points
    private static final GenericPoint muellerFeet = new GenericPoint (128, 466);
    private static final GenericPoint Pmueller    = new GenericPoint (210, 475);
    private static final GenericPoint Pexit       = new GenericPoint (  0, 475);
  
    // Konstante ints
    private static final int Muellerzooming = 45;  // scaling analog Krabat

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mlynkmurja (Start caller,int oldLocation) 
    {
	super (caller, 129);
	mainFrame.Freeze (true);
    
	mainFrame.CheckKrabat ();
        BackgroundMusicPlayer.getInstance ().stop (); 
    
	mainFrame.krabat.maxx = 469;  
	mainFrame.krabat.zoomf = 1f;
	mainFrame.krabat.defScale = 35;
  	
	InitLocation (oldLocation);
    
	// Mueller initialisieren
	mueller = new Oldmlynk (mainFrame, Muellerzooming);
    
	muellerPoint = mueller.getMuellerPoint (muellerFeet);
	muellerTalk  = mueller.getTalkPoint (muellerFeet);
    
	nextActionID = 10;

	float xtf = 900 - (((640 - 110) * 4) / 7);
	laterneAdd = (int) (xtf - 640);
    
	// System.out.println ("Laterne wird auf Pos. " + laterneAdd + " gezeichnet.");

	mainFrame.Freeze (false);
    }
  
    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation (int oldLocation)
    {
	// Grenzen setzen
	mainFrame.wegGeher.vBorders.removeAllElements ();
	mainFrame.wegGeher.vBorders.addElement 
	    (new Bordertrapez (   0,   90,    0,   90, 473, 479));
	mainFrame.wegGeher.vBorders.addElement 
	    (new Bordertrapez (  91,  619,   91,  619, 470, 479));
	mainFrame.wegGeher.vBorders.addElement 
	    (new Bordertrapez ( 620,  630,  620,  630, 435, 479));
       
	mainFrame.wegSucher.ClearMatrix (3);
    
	mainFrame.wegSucher.PosVerbinden (0, 1); 
	mainFrame.wegSucher.PosVerbinden (1, 2);

	InitImages();

	mainFrame.krabat.SetKrabatPos (new GenericPoint (607, 475));
	mainFrame.krabat.SetFacing (9);
    }

    // Bilder vorbereiten
    private void InitImages() 
    {
	backr   = getPicture ("gfx-dd/murja/murja-r.gif");
	laterne = getPicture ("gfx-dd/murja/laterna.gif");
    
	loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation (GenericDrawingContext g)
    {
	if (initplay == true)
	    {
		initplay = false;
		mainFrame.wave.PlayFile ("sfx-dd/mlynk-dd.wav");
	    }

	// Clipping -Region initialisieren
	if (mainFrame.Clipset == false)
	    {
		mainFrame.Clipset = true;
		Cursorform = 200;
		evalMouseMoveEvent (mainFrame.Mousepoint);
		g.setClip(0, 0, 644, 484);
		mainFrame.isAnim = true;
		if (mainFrame.fPlayAnim = true) mainFrame.fPlayAnim = true;	
	    }

	// Hintergrund und Krabat zeichnen  
	g.drawImage (backr, 0, 0, null);
  	g.drawImage (laterne, laterneAdd, 0, null);
    
	// Mueller zeichnen
	g.setClip (muellerPoint.x, muellerPoint.y, Oldmlynk.Breite, Oldmlynk.Hoehe);
	g.drawImage (backr, 0, 0, null);
	mueller.drawOldmlynk (g, TalkPerson, muellerPoint);
    
	// Debugging - Zeichnen der Laufrechtecke
	// mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);
  	
	mainFrame.wegGeher.GeheWeg ();
    
	// Krabat zeichnen

	if (krabatVisible == true)
	    {
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

	// Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
	// GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

	// Laterne zeichnen, wenn im Bild 
  	g.drawImage (laterne, laterneAdd, 0, null);
    
	// hinter weiden2 (nur Clipping - Region wird neugezeichnet)
	/*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
	  {
	  g.drawImage (weiden2, 84, 221, null);
	  }*/

	// sonst noch was zu tun ?
	if  (outputText != "")
	    {
		// Textausgabe
		GenericRectangle my;
		my = g.getClipBounds();
		g.setClip (0, 0, 644, 484);
		mainFrame.ifont.drawString (g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
		g.setClip( (int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight()); 
	    }

	if ((TalkPause > 0) && (mainFrame.talkCount == 0)) TalkPause--;

	// Gibt es was zu tun ?
	if ((nextActionID != 0) && (TalkPause == 0) && (mainFrame.talkCount == 0)) DoAction ();
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent (GenericMouseEvent e)
    {
	// Cursorpunkt mit Scrolloffset berechnen 
	GenericPoint pTemp = e.getPoint ();
	pTemp.x += mainFrame.scrollx;
	if (mainFrame.talkCount != 0) mainFrame.Clipset = false;
	if (mainFrame.talkCount > 1) mainFrame.talkCount = 1;
	outputText="";
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    public void evalMouseMoveEvent (GenericPoint pTxxx)
    {
	// Wenn Animation oder Krabat - Animation, dann transparenter Cursor
	if (Cursorform != 20)
	    {
		Cursorform = 20;
		mainFrame.setCursor (mainFrame.Nix);
	    }
	return;
    }

    // dieses Event nicht beachten
    public void evalMouseExitEvent (GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent (GenericKeyEvent e)
    {
    }  

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction ()
    {
	// nichts zu tun, oder Krabat laeuft noch
	if ((mainFrame.krabat.isWandering == true) ||
	    (mainFrame.krabat.isWalking == true))
	    return;

	// hier wird zu den Standardausreden von Krabat verzweigt, 
	// wenn noetig (in Superklasse)
	if ((nextActionID > 499) && (nextActionID < 600))
	    {
		setKrabatAusrede();
		// manche Ausreden erfordern neuen Cursor !!!
		evalMouseMoveEvent (mainFrame.Mousepoint);
		return;
	    }		

	// Was soll Krabat machen ?
	switch (nextActionID)
	    {
	    case 10:
		// Krabat laeuft zum Mueller
		mainFrame.wegGeher.SetzeNeuenWeg (Pmueller);
		nextActionID = 20;
		break;
      
	    case 20:
		// richtigrum hinstellen
		mainFrame.krabat.SetFacing (9);	
		nextActionID = 30;
		break;
      	
	    case 30:
		// Mueller spricht
		PersonSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00000"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00001"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00002"),
			    0, 36, 2, 40, muellerTalk);
		break;
        
	    case 40:
		// Krabat spricht
		KrabatSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00003"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00004"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00005"),
			    0, 1, 2, 50);
		break;
      
	    case 50:
		// Mueller spricht
		PersonSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00006"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00007"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00008"),
			    0, 36, 2, 60, muellerTalk);
		break;
        
	    case 60:
		// Krabat spricht
		KrabatSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00009"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00010"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00011"),
			    0, 1, 2, 70);
		break;

	    case 70:
		// Mueller spricht
		PersonSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00012"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00013"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00014"),
			    0, 36, 2, 80, muellerTalk);
		break;
        
	    case 80:
		// Krabat spricht
		KrabatSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00015"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00016"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00017"),
			    0, 1, 2, 90);
		break;

	    case 90:
		// Krabat laeuft aus dem Bild
		mainFrame.wegGeher.SetzeNeuenWeg (Pexit);
		nextActionID = 95;
		break;

	    case 95:
		// bisschen warten, nachdem K weg
		krabatVisible = false;
		Counter = 15;
		nextActionID = 100;
		break;
      
	    case 100:
		// Mueller spricht
		if ((--Counter) > 0) break;
		PersonSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00018"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00019"),
			    Start.stringManager.getTranslation("Loc3_Mlynkmurja_00020"),
			    0, 66, 2, 110, muellerTalk);
		break;
        
	    case 110:
		// Mueller spricht
		PersonSagt (Start.stringManager.getTranslation("Loc3_Mlynkmurja_00021"), Start.stringManager.getTranslation("Loc3_Mlynkmurja_00022"), Start.stringManager.getTranslation("Loc3_Mlynkmurja_00023"), 0, 67, 2, 120, muellerTalk);
		break;
        
	    case 120:
		// Gehe zu Murja, aber diesmal wieder die alte...
		NeuesBild (126, locationID);
		mainFrame.Actions[656] = true; // hier verhindern, dass Szene ein zweites Mal passiert
		break;

	    default:
		System.out.println ("Falsche Action-ID !");
	    }

    }
}