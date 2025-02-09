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
import rapaki.krabat.main.Borderrect;
import rapaki.krabat.main.GenericPoint;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class Kuchar extends Mainanim
{
    private GenericImage kuchar_down_schlag[];
    private GenericImage kuchar_down_talk[];
    private GenericImage kuchar_up_talk[];
    private GenericImage maus[];
    
    private static final GenericPoint downPoint = new GenericPoint (327, 208);
    private static final GenericPoint upPoint = new GenericPoint (337, 163);
    
    private static final GenericPoint mauspos = new GenericPoint (344, 340);
    
    private int Schlag = 0;
    private int TalkDown = 0;
    private int TalkUp = 0;
    
    private int Verhinderschlag;
    private int Verhindertalkdown;
    private int Verhindertalkup;
    private int Verhinderstier;
    
    private static final int MAX_VERHINDERSCHLAG = 60;
    private static final int MAX_VERHINDERTALKDOWN = 3;
    private static final int MAX_VERHINDERTALKUP = 3;
    private static final int MAX_VERHINDERSTIER = 10;
    
    private int Maus = 0;
    
    private boolean isAufgestanden = false;
      
    public Kuchar (Start caller)
    {
      super (caller);
      
      kuchar_down_schlag = new GenericImage [4];
      kuchar_down_talk = new GenericImage [8];
      kuchar_up_talk = new GenericImage [6];
      maus = new GenericImage [3];
      
      InitImages();
      
      Verhinderschlag = MAX_VERHINDERSCHLAG;
      Verhindertalkdown = MAX_VERHINDERTALKDOWN;
      Verhindertalkup = MAX_VERHINDERTALKUP;
      Verhinderstier = MAX_VERHINDERSTIER;
    } 
	 
    private void InitImages()
    {
    	kuchar_down_schlag[0] = getPicture ("gfx-dd/kuchnja/koch2.gif");
    	kuchar_down_schlag[1] = getPicture ("gfx-dd/kuchnja/koch2a.gif");
    	kuchar_down_schlag[2] = getPicture ("gfx-dd/kuchnja/koch2b.gif");
    	kuchar_down_schlag[3] = getPicture ("gfx-dd/kuchnja/koch2c.gif");
    	
    	kuchar_down_talk[0] = getPicture ("gfx-dd/kuchnja/koch2-t1.gif");
    	kuchar_down_talk[1] = getPicture ("gfx-dd/kuchnja/koch2-t1a.gif");
    	kuchar_down_talk[2] = getPicture ("gfx-dd/kuchnja/koch2-t2.gif");
    	kuchar_down_talk[3] = getPicture ("gfx-dd/kuchnja/koch2-t3.gif");
    	kuchar_down_talk[4] = getPicture ("gfx-dd/kuchnja/koch2-t4.gif");
    	kuchar_down_talk[5] = getPicture ("gfx-dd/kuchnja/koch2-t5.gif");
    	kuchar_down_talk[6] = getPicture ("gfx-dd/kuchnja/koch2-t6.gif");
    	kuchar_down_talk[7] = getPicture ("gfx-dd/kuchnja/koch2-t7.gif");
    	
    	kuchar_up_talk[0] = getPicture ("gfx-dd/kuchnja/koch1.gif");    	
    	kuchar_up_talk[1] = getPicture ("gfx-dd/kuchnja/koch1-t1.gif");    	
    	kuchar_up_talk[2] = getPicture ("gfx-dd/kuchnja/koch1-t2.gif");    	
    	kuchar_up_talk[3] = getPicture ("gfx-dd/kuchnja/koch1-t3.gif");    	
    	kuchar_up_talk[4] = getPicture ("gfx-dd/kuchnja/koch1-t4.gif");    	
    	kuchar_up_talk[5] = getPicture ("gfx-dd/kuchnja/koch1-t5.gif");    	
    	
    	maus[0] = getPicture ("gfx-dd/kuchnja/mos.gif");
    	maus[1] = getPicture ("gfx-dd/kuchnja/mos2.gif");
    	maus[2] = getPicture ("gfx-dd/kuchnja/mos3.gif");
    }
  
    // gib Borderrect fuer "inside" - Evaluierung an
    public Borderrect KucharRect ()
    {
      if (isAufgestanden == false)
      {
        return (new Borderrect (327, 208, 327 + 140, 208 + 164));
      }
      else
      {
      	return (new Borderrect (337, 163, 337 + 101, 163 + 209));
      }
    }
    
    // gibt TalkPoint zurueck, je nach "Groesse" des Kochs
    public GenericPoint evalKucharTalkPoint ()
    {
      if (isAufgestanden == false)
      {
        return (new GenericPoint (327 + (140 / 2), 208 - 50));
      }
      else
      {
      	return (new GenericPoint (337 + (101 / 2), 163 - 50));
      }
    }
    
    // Zeichne Kuchar, wie er dasteht oder spricht
    public void drawKuchar (GenericDrawingContext offGraph, int TalkPerson, boolean isListening, 
                            boolean isAufgestanden, boolean noSound)
    {
    	// oberste Prio hat das Reden
    	if ((TalkPerson == 42) && (mainFrame.talkCount > 1))
    	{
    	  // mal schauen, ob er unten oder oben ist
    	  if (this.isAufgestanden == true)
    	  {
    	    if ((--Verhindertalkup) < 1)
    	    {
    	    	Verhindertalkup = MAX_VERHINDERTALKUP;
    	    	TalkUp = (int) ((Math.random () * 4.9) + 1);
    	    }
    	    
    	    evalMouse (offGraph);
    	    
    	    offGraph.drawImage (kuchar_up_talk[TalkUp], upPoint.x, upPoint.y, null);
    	  }
    	  else
    	  {
    	  	if ((--Verhindertalkdown) < 1)
    	  	{
    	  		Verhindertalkdown = MAX_VERHINDERTALKDOWN;
    	  		TalkDown = (int) (Math.random () * 7.9);
    	  		if (TalkDown == 1) TalkDown = 0;
    	  	}
    	  	
    	  	evalMouse (offGraph);
    	  	
    	  	offGraph.drawImage (kuchar_down_talk[TalkDown], downPoint.x, downPoint.y, null);
    	  }
      	// aufgesteht merken wg. Rect und TalkPos
      	this.isAufgestanden = isAufgestanden;
    	
    	  return;
    	}
    	
    	// beim Zuhoeren
    	if (isListening == true)
    	{
    		if (TalkDown > 0) TalkDown = 0;
    		else
    		{
    			int zf = (int) (Math.random () * 50);
    			if (zf > 45) TalkDown = 1;
    		}
    		
    		evalMouse (offGraph);
    		
    		offGraph.drawImage (kuchar_down_talk[TalkDown], downPoint.x, downPoint.y, null);
    		
      	// aufgesteht merken wg. Rect und TalkPos
      	this.isAufgestanden = isAufgestanden;
    	
    		return;
    	}
    	
    	// wenn aufgestanden, dann nur dastehen (wenn er fertig mit meckern ist)
    	if (this.isAufgestanden == true)
    	{
    		evalMouse (offGraph);
    		
    		offGraph.drawImage (kuchar_up_talk[0], upPoint.x, upPoint.y, null);

      	// aufgesteht merken wg. Rect und TalkPos
      	this.isAufgestanden = isAufgestanden;
    	
    	  return;
    	}
    	
    	// Maeuse erschlagen
    	int zuff = (int) (Math.random () * 50);
    	if (zuff < 30) --Verhinderschlag;
    	if (Verhinderschlag < 1)
    	{
    		// hier weiterswitchen, ob nur streng geschaut oder Schlag
    	  Schlag = 2;
    	  if ((--Verhinderstier) < 1)
    	  {
    	  	Verhinderstier = MAX_VERHINDERSTIER;
    	  	// int zf = (int) (Math.random () * 50);
    	  	// if (zf > 30) Schlag = 3;
    	  	Schlag = 3;
		if (noSound == false) evalSound ();
    	  	Verhinderschlag = MAX_VERHINDERSCHLAG;
    	  }
    	  
    	  offGraph.drawImage (maus[2], mauspos.x, mauspos.y, null);
    	  
    	  offGraph.drawImage (kuchar_down_schlag[Schlag], downPoint.x, downPoint.y, null);
    	}
    	else
    	{
    		// Zwinkern
    		if (Schlag > 0) Schlag = 0;
    		else
    		{
    			int zf = (int) (Math.random () * 50);
    			if (zf > 45) Schlag = 1;
    		}
    		
    		evalMouse (offGraph);
    		
    		offGraph.drawImage (kuchar_down_schlag[Schlag], downPoint.x, downPoint.y, null);
    	}

    	// aufgesteht merken wg. Rect und TalkPos
    	this.isAufgestanden = isAufgestanden;
    	
    	return;
    }
    
    // Maus zeichnen, wenn sie nicht rausguckt
    private void evalMouse (GenericDrawingContext g)
    {
    	// Zwinkern eval.
    	if (Maus > 0) Maus = 0;
    	else
    	{
    		int zf = (int) (Math.random () * 50);
    		if (zf > 45) Maus = 1;
    	}
    	
    	// Maus zeichnen
    	g.drawImage (maus[Maus], mauspos.x, mauspos.y, null);
    }

    private void evalSound ()
    {
	// zufaellig wavs fuer Geschnatter abspielen...
	mainFrame.wave.PlayFile ("sfx-dd/lzica.wav");
    }    
  
 
}    