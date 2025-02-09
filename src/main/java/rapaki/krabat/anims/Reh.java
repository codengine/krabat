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

package rapaki.krabat.anims;

import rapaki.krabat.Start;
import rapaki.krabat.main.GenericPoint;
import rapaki.krabat.main.GenericRectangle;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class Reh extends Mainanim
{
    private GenericImage[] reh;
    
    private boolean isHirsch[];
    
    private boolean istErNochDa;
    
    private boolean wegRennRichtung; // true = nach links
    private boolean rennenAlleWeg = false;
    private boolean rennenNieWeg = false;
    private GenericRectangle Aufhaltebereich;
    
    private int Verhinderwegrenn;
    private static final int MAX_VERHINDERWEGRENN = 500;
    
    private int Verhindergrasen[];
    private static final int MAX_VERHINDERGRASEN = 40;
    
    private int Verhinderlauf[];
    private static final int MAX_VERHINDERLAUF = 1;
    
    private int Animpos[];
    
    private int Wieviele;
    
    private GenericPoint[] Positionen;
    
    private static final int HOEHE = 15;

    private static final int AUFTAUCHWAHRSCHEINLICHKEIT = 90;

    // normale Angaben = Rehe rennen weg
    // Wieviele > 100 ... bleiben stehen
    // Rectangle grenzt Punkt links unten ein (Fuss, aber ganz links sozusagen)
    public Reh (Start caller, boolean wegRennRichtung, GenericRectangle Aufhaltebereich, int Wieviele)
    {
	super (caller);
      
	reh = new GenericImage [16];
      
	// hier die Abfrage auf nichtwegrennende Rehe
	if (Wieviele > 100)
	    { 
		Wieviele -= 100;
		rennenNieWeg = true;
	    }
      
	// this.Wieviele = (int) (Math.random () * Wieviele);
	int zuffiZ = (int) (Math.random () * 100);      
	if (zuffiZ > AUFTAUCHWAHRSCHEINLICHKEIT) this.Wieviele = 1;
	else this.Wieviele = 0;
      
	int MaximaleRehe = (int) ((Aufhaltebereich.getHeight() / 7) + 1);
	if (this.Wieviele > MaximaleRehe) this.Wieviele = MaximaleRehe;
      
	this.wegRennRichtung = wegRennRichtung;
	this.Aufhaltebereich = Aufhaltebereich;
      
	Verhinderwegrenn = MAX_VERHINDERWEGRENN;
	istErNochDa = true;
      
	isHirsch    = new boolean [this.Wieviele];
      
	Verhindergrasen  = new int [this.Wieviele];
	Verhinderlauf    = new int [this.Wieviele];
	Animpos          = new int [this.Wieviele];
      
	Positionen = new GenericPoint [this.Wieviele];
      
	// System.out.println ("Es werden " + this.Wieviele + " Rehe im Hintergrund angezeigt.");
      
	for (int i = 0; i < this.Wieviele; i++)
	    {
		// Berechnen, ob Hirsch oder Reh erscheint
		int zuffi = (int) (Math.random () * 50);
		if (zuffi > 25) isHirsch[i] = true;
		else            isHirsch[i] = false;
      
		// Berechnen, ob nach links oder rechts grasend
		zuffi = (int) (Math.random () * 50);
		if (zuffi > 25)
		    {
			if (isHirsch[i] == true) Animpos[i] = 0;
			else                  Animpos[i] = 8;
		    }
		else
		    {
			if (isHirsch[i] == true) Animpos[i] = 2;
			else                  Animpos[i] = 10;
		    }
        
		// Verhindergrasen zufaellig init. ,damit nichts so vorherberechnet aussieht
		Verhindergrasen[i] = (int) (Math.random() * MAX_VERHINDERGRASEN);
        
		// dto. fuer Verhinderlauf
		Verhinderlauf[i] = (int) (Math.random() * MAX_VERHINDERLAUF);
      
		// Hier die Position innerhalb des Rects berechnen
		int xpos = (int) ((Math.random() * ((int) Aufhaltebereich.getWidth())) + Aufhaltebereich.getX());
		int ypos;
        
		boolean PositionIstGut;
        
		do
		    {
			ypos = (int) ((Math.random() * ((int) Aufhaltebereich.getHeight())) + Aufhaltebereich.getY());
          
			PositionIstGut = true;
          
			if (i != 0) 
			    {
				for (int j = 0; j < i; j++)
				    {
					if (Math.abs (ypos - Positionen[j].y) < 3) PositionIstGut = false;
				    }
			    }
		    }
		while (PositionIstGut == false);
        
		Positionen[i] = new GenericPoint (xpos, ypos);
	    }
      
	InitImages();
    } 
	 
    private void InitImages()
    {
    	reh[0]  = getPicture ("gfx/anims/reh1.gif");  // stehen in eine Richtung
	reh[1]  = getPicture ("gfx/anims/reh1a.gif"); // grasen
	reh[2]  = getPicture ("gfx/anims/reh2.gif");  // stehen in andere Richtung
	reh[3]  = getPicture ("gfx/anims/reh2a.gif"); // grasen
     	reh[4]  = getPicture ("gfx/anims/reh5a.gif"); // Rennen in 1 Richtung
	reh[5]  = getPicture ("gfx/anims/reh6a.gif");
	reh[6]  = getPicture ("gfx/anims/reh5.gif");  // Rennen in 2. Richtung
	reh[7]  = getPicture ("gfx/anims/reh6.gif");
     	reh[8]  = getPicture ("gfx/anims/reh3.gif");  // dto.
     	reh[9]  = getPicture ("gfx/anims/reh3a.gif");
	reh[10] = getPicture ("gfx/anims/reh4.gif");
	reh[11] = getPicture ("gfx/anims/reh4a.gif");
	reh[12] = getPicture ("gfx/anims/reh7a.gif");
     	reh[13] = getPicture ("gfx/anims/reh8a.gif");
	reh[14] = getPicture ("gfx/anims/reh7.gif");
     	reh[15] = getPicture ("gfx/anims/reh8.gif");
    }
  
    // Reh zeichnen, wenn noch da (Routine entscheidet alles selber)
    public void drawReh (GenericDrawingContext g)
    {
    	// wenn weggerannt, dann nix mehr tun
    	if (istErNochDa == false) return;
    	
    	// wenn keines da, dann auch nix zeichnen
    	if (Wieviele < 1) return;
    	
    	// hier schauen, ob alle nicht besser wegrennen sollten
    	if (((--Verhinderwegrenn) < 1) && (rennenNieWeg == false))
	    {
    		int zuffi = (int) (Math.random () * 100);
    		if (zuffi > 90) rennenAlleWeg = true;
	    }	
    	
    	// hier nur weiterschalten, nix zeichnen
	if (rennenAlleWeg == false)
	    {
		// hier malen, wenn sie nur grasen
		for (int i = 0; i < Wieviele; i++)
		    {
			if ((--Verhindergrasen[i]) < 1)
			    {
				Verhindergrasen[i] = MAX_VERHINDERGRASEN;
      			
				// mit bestimmter Wahrscheinlichkeit Anims wechseln, sonst zu mechanisch
				int zf = (int) (Math.random () * 50);
				if (zf > 30)
				    {	
					switch (Animpos[i])
					    {
					    case 0: // stehen -> grasen
						Animpos[i] = 1;
						Positionen[i].x -= 6;
						break;
					    case 2:
	      					Animpos[i] = 3;
	      					break;
					    case 8:
	      					Animpos[i] = 9;
	      					Positionen[i].x -= 3;
	      					break;
					    case 10:
	      					Animpos[i] = 11;
	      					break;
					    case 1: // grasen -> stehen
	      					Animpos[i] = 0;
						Positionen[i].x += 6;
	      					break;
					    case 3:
	      					Animpos[i] = 2;
	      					break;
					    case 9:
	      					Animpos[i] = 8;
	      					Positionen[i].x += 3;
	      					break;
					    case 11:
	      					Animpos[i] = 10;
	      					break;
					    }
				    }
			    }
		    }
	    }
	else
	    {
		// hier alle wegrennen lassen
		istErNochDa = false;
      	
		// testen, welche Richtung
		if (wegRennRichtung == true)
		    {
			// nach links, bis alles raus
			for (int i = 0; i < Wieviele; i++)
			    {
				if ((--Verhinderlauf[i]) < 1)
				    {
					Verhinderlauf[i] = MAX_VERHINDERLAUF;	
	      		
					Positionen[i].x -= 4;
					if (Positionen[i].x > -30) istErNochDa = true;
	      			
					switch (Animpos[i])
					    {
					    case 0:
					    case 1:
					    case 2:
					    case 3:
	      					Animpos[i] = 4;
	      					break;
					    case 4:
	      					Animpos[i] = 5;
	      					break;
					    case 5:
	      					Animpos[i] = 4;
	      					break;
					    case 8:
					    case 9:
					    case 10:
					    case 11:
	      					Animpos[i] = 12;
	      					break;
					    case 12:
	      					Animpos[i] = 13;
	      					break;
					    case 13:
	      					Animpos[i] = 12;
	      					break;
					    }
				    }
				else istErNochDa = true;
			    }		
		    }
		else
		    {
			// nach rechts, bis alle weg
			for (int i = 0; i < Wieviele; i++)
			    {
				if ((--Verhinderlauf[i]) < 1)
				    {
					Verhinderlauf[i] = MAX_VERHINDERLAUF;	
	      		
					Positionen[i].x += 4;
					if (Positionen[i].x < 670) istErNochDa = true;
	      			
					switch (Animpos[i])
					    {
					    case 0:
					    case 1:
					    case 2:
					    case 3:
	      					Animpos[i] = 6;
	      					break;
					    case 6:
	      					Animpos[i] = 7;
	      					break;
					    case 7:
	      					Animpos[i] = 6;
	      					break;
					    case 8:
					    case 9:
					    case 10:
					    case 11:
	      					Animpos[i] = 14;
	      					break;
					    case 14:
	      					Animpos[i] = 15;
	      					break;
					    case 15:
	      					Animpos[i] = 14;
	      					break;
					    }
				    }
				else istErNochDa = true;
			    }		
		    }		
	    }
      
	// hier alle malen, die vorderste Figur zuletzt
	int Anfang = (int) (Aufhaltebereich.getY ());
	int Ende = (int) (Anfang + Aufhaltebereich.getHeight());
      
	for (int i = Anfang; i <= Ende; i++)
	    {
		// hier kann der Reihenfolge nach gezeichnet werden
		for (int j = 0; j < Wieviele; j++)
		    {
			if (Positionen[j].y == i)
			    {
				// dieses Reh zeichnen
				g.drawImage (reh[Animpos[j]], Positionen[j].x, Positionen[j].y - HOEHE, null);
          
				// System.out.println ("Zeichne Reh!");
			    }
		    }
	    }	
    }	
}    