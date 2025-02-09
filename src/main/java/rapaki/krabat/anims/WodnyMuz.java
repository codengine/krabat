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
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class WodnyMuz extends Mainanim
{
    private GenericImage[] wmuz_head;
    private GenericImage[] wmuz_body;
    private GenericImage[] wmuz_tauch;
    // private GenericImage woda;
	
    private int Head = 1;
    private int Body = 1;
    private int Norm = 1;
  
    private int Offset;

    public static final int Breite = 65;
    public static final int Hoehe  = 65;
    public static final int Tauchhoehe = 51;
  
    private static final int GRAFIK_HOEHE = 51;
	
    private int Wait;
    private static final int MAX_WAIT = 6;
  
    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;
  
    private boolean istOben;
    private boolean turn;
  
    private double angle = 0;
    private static final double step = 2 * Math.PI / 20;
  
    public WodnyMuz (Start caller, boolean istOben)
    {
	super (caller);
		
	wmuz_head  = new GenericImage [8];
	wmuz_body  = new GenericImage [6];
	wmuz_tauch = new GenericImage [3];
    	
	InitImages();
	  
	Wait = MAX_WAIT;
	Verhinderhead = MAX_VERHINDERHEAD;
	this.istOben = istOben;
	  
	if (istOben == true)
	    {
	  	Offset = 0;
	  	turn = true;
	    }	
	else 
	    {
	  	Offset = GRAFIK_HOEHE;
	  	turn = false;
	    }	
    } 
	 
    private void InitImages()
    {
	// woda          = getPicture ("gfx/mertens/woda.gif");
	         
	wmuz_head[1]  = getPicture ("gfx/mertens/wmk1.gif");
	wmuz_head[2]  = getPicture ("gfx/mertens/wmk1a.gif");
	wmuz_head[3]  = getPicture ("gfx/mertens/wmk2.gif");
	wmuz_head[4]  = getPicture ("gfx/mertens/wmk3.gif");
	wmuz_head[5]  = getPicture ("gfx/mertens/wmk4.gif");
	wmuz_head[6]  = getPicture ("gfx/mertens/wmk5.gif");
	wmuz_head[7]  = getPicture ("gfx/mertens/wmk6.gif");
 
	wmuz_body[1]  = getPicture ("gfx/mertens/wmb1.gif");
	wmuz_body[2]  = getPicture ("gfx/mertens/wmb2.gif");
	wmuz_body[3]  = getPicture ("gfx/mertens/wmb3.gif");
	wmuz_body[4]  = getPicture ("gfx/mertens/wmb4.gif");
	wmuz_body[5]  = getPicture ("gfx/mertens/wmb5.gif");

	wmuz_tauch[1] = getPicture ("gfx/mertens/wm.gif");
	wmuz_tauch[2] = getPicture ("gfx/mertens/wm2.gif");
    }
  
    // Zeichne Wassermann, wie er schwimmt oder spricht
    public void drawWmuz (GenericDrawingContext g, int TalkPerson, GenericPoint posit)
    {
	int zuffi;
    
	// Rede, Wassermann !!!
	if ((TalkPerson == 34) && (mainFrame.talkCount > 1))
	    {
		if ((--Wait) < 1)
		    {
			Wait = MAX_WAIT;	
			zuffi = (int) Math.round (Math.random() * 5);
			zuffi++;
			if (zuffi < 6) Body = zuffi;
		    }
      
		if ((--Verhinderhead) < 1)
		    {
			Verhinderhead = MAX_VERHINDERHEAD;
			zuffi = (int) Math.round (Math.random() * 7);
			zuffi++;
			if (zuffi == 2) zuffi = 3;
			if (zuffi < 8) Head = zuffi;
		    }
      
		g.drawImage (wmuz_head[Head], posit.x, posit.y + Offset     , null);
		g.drawImage (wmuz_body[Body], posit.x, posit.y + Offset + 34, null);
	    }  
    
	// Schwimme, Wassermann !!!
	else 
	    {
		if (Norm == 2) Norm = 1;
		else
		    {
			zuffi = (int) Math.round (Math.random() * 50);
			if (zuffi > 47) Norm = 2;
		    }
      
		g.drawImage (wmuz_head[Norm], posit.x, posit.y + Offset    , null);
		g.drawImage (wmuz_body[1]   , posit.x, posit.y + Offset + 34, null);		
	    }
    
	// Wippe im Wind wie ein Blatt vom Baum (Ok, ok, ich hoer schon auf...)
	angle += step;
	if (angle > (2 * Math.PI)) angle -= (2 * Math.PI);
	Offset = (int) Math.round ((Math.sin (angle) * 2.1) + 2); 
    }
  
    public boolean Tauche (GenericDrawingContext g, GenericPoint posit)
    {
  	// Die Angabe GenericPoint posit bezieht sich auf die Position im aufgetauchten Zustand...
  	// Methode gibt false zurueck, wenn sie nicht mehr aufgerufen werden soll (ich habe fertig)
  	
  	// Abtauchen hier
  	if (istOben == true)
	    {
		if (turn == true)
		    {
			turn = false;
			g.drawImage (wmuz_tauch[1], posit.x, posit.y + Offset, null);
			return true;
		    }
		else
		    {
			Offset++;
			if (Offset == 8) mainFrame.wave.PlayFile ("sfx/wmuz.wav");
			g.drawImage ((Offset > 30) ? wmuz_tauch[2] : wmuz_tauch[1], posit.x, posit.y + Offset, null);
			if (Offset < GRAFIK_HOEHE) return true;
			else
			    {
				istOben = !(istOben);	
				return false;
			    }	
		    }				
	    }
  	
  	// Hier ist Auftauchen
  	else
	    {
		if (Offset > 0)
		    {
			Offset--;
			if (Offset == (GRAFIK_HOEHE - 1)) mainFrame.wave.PlayFile ("sfx/wmuz.wav");
			g.drawImage ((Offset > 30) ? wmuz_tauch[2] : wmuz_tauch[1], posit.x, posit.y + Offset, null);	
			return true;
		    }
		else
		    {
			if (turn == false)
			    {
				turn = true;
				g.drawImage (wmuz_tauch[1], posit.x, posit.y + Offset, null);
				return true;
			    }
			else
			    {
				g.drawImage (wmuz_tauch[1], posit.x, posit.y + Offset, null);
				istOben = !(istOben);
				return false;
			    }
		    }		
	    }
    }						 
}    