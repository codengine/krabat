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
import rapaki.krabat.main.GenericRectangle;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class Mlynkptack extends Mainanim
{
    private GenericImage[] vogel;
    private int x,y;
    private int animpos = 1;
    private boolean Gleiten	= false;
    private boolean oben = true;
    // private boolean up = false;
    private int gleitcount;
    private boolean schalt = false;
    private int XEnde;
    private boolean isLeft;
    private static final int MAXGLEIT = 10;
  
    private int Zoomfaktor;
	
    // GenericImage ist 50 Breit und 50 Hoch
	
    // gezoomten Ptack laden
    public Mlynkptack (Start caller, int x, int y, int Zoomfaktor, int XEnde, boolean isLeft)
    {
  	super (caller);
  	this.x = x;
  	this.y = y;
  	this.Zoomfaktor = Zoomfaktor;
  	this.XEnde = XEnde;
  	this.isLeft = isLeft;
  	animpos = 1;
  	gleitcount = MAXGLEIT;
  	  	  	
  	vogel = new GenericImage[4];
  	
  	InitImages();
    }		
  
    // Bilder laden
    private void InitImages()
    {
	if (isLeft == true)
	    {
		vogel[1] = getPicture ("gfx/kolmc/iv1a.gif");
		vogel[2] = getPicture ("gfx/kolmc/iv2a.gif");
		vogel[3] = getPicture ("gfx/kolmc/iv3a.gif");
	    }
	else
	    {
		vogel[1] = getPicture ("gfx/kolmc/iv1a-gespiegelt.gif");
		vogel[2] = getPicture ("gfx/kolmc/iv2a-gespiegelt.gif");
		vogel[3] = getPicture ("gfx/kolmc/iv3a-gespiegelt.gif");
	    }
    }
  
    // gib mir das Rectangle !!!
    public GenericRectangle mlynkPtackRect ()
    {
	return (new GenericRectangle (x - 15, y - 15, 50 + 30, 50 + 30));
    }		
  
    // rumfliegen
    public boolean Flieg (GenericDrawingContext g)		
    {
	// wenn Fluegel in Mitte, dann schauen, ob weiterfliegen oder gleiten
	if ((animpos == 1) || (animpos == 4))
	    {
		int glei = (int) Math.round (Math.random() * 30);
		if ((glei < 29) && (Gleiten == false))
		    {
			// weiterfliegen
			schalt = ! (schalt);
			if (schalt == true)
			    {
				oben = ! (oben);
				if (oben == false) animpos = 3;
				else animpos = 2;
			    }  
		    }
		else
		    {
			// gleiten
			animpos = 1;

			if (gleitcount == MAXGLEIT) Gleiten = true; 
			gleitcount--;
			if (gleitcount == 0) 
			    {
				gleitcount = MAXGLEIT;
				Gleiten = false;
			    }
		    }			
	    }
	else 
	    {
		// Fluegel in Extremposition, also wieder auf Mittelstellung setzen
		schalt = ! (schalt);
		if (schalt == true) animpos = 1;
	    }	
    
	// X- Offset fuer Fliegen ist 10 (ungezoomt)
	if (isLeft == true) x = x - 10 + (Zoomfaktor / 10);
	else x = x + 10 - (Zoomfaktor / 10);

	// keine Ueberschreitung zulassen
	if ((isLeft == true)  && (x < XEnde)) x = XEnde;
	if ((isLeft == false) && (x > XEnde)) x = XEnde;
    
	// Beim Gleiten y nach unten, beim Fliegen nach oben, ab bestimmem Zoomfaktor nicht mehr...
	int versch = (int) Math.round (Math.random() * 20);
	if ((versch > 10) && (Zoomfaktor < 10))
	    {
		if (Gleiten == true)
		    {
			y += 1;
		    }
		else
		    {
			y -= 1;
		    }
	    }
    
	/*int xx = x;
	  if (xx < 0) xx = 0;
	  int yy = y;
	  if (yy < 0) yy = 0; */
    
	// g.setClip (xx, yy, xx + 50, yy + 50);
	g.drawImage (vogel[animpos], x, y, 50 - Zoomfaktor, 50  - Zoomfaktor, null);
	if (isLeft == true)
	    {
		if (x <= XEnde) return false; // wenn aus dem Bild, dann das der Routine sagen !
		else return true;
	    }
	else
	    {
		if (x >= XEnde) return false;
		else return true;
	    }		  
    }
}    						