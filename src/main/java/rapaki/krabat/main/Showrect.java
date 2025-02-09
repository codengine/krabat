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

package rapaki.krabat.main;

import rapaki.krabat.Start;
import rapaki.krabat.platform.GenericDrawingContext;

import java.util.Vector;

public class Showrect
{
  
  // private Start mainFrame;
  
  private boolean istDeaktiviert = false;

  public Showrect (Start caller)
  {
    // mainFrame = caller;
  }  

  public void Zeichne(GenericDrawingContext g, Vector<Bordertrapez> Rechtecke)
  {
    if (istDeaktiviert == true) return;
    
    g.setColor (GenericColor.white);
    GenericRectangle my;
    my = g.getClipBounds();
    g.setClip(0 , 0, 1280, 480);
    int laenge = Rechtecke.size();
    for (int i = 0; i < laenge; i++)
    {
      Bordertrapez di = (Bordertrapez) Rechtecke.elementAt (i);
      g.drawLine (di.x1, di.y1, di.x3, di.y2);
      g.drawLine (di.x3, di.y2, di.x4, di.y2);
      g.drawLine (di.x4, di.y2, di.x2, di.y1);
      g.drawLine (di.x2, di.y1, di.x1, di.y1);
    }
    g.setClip( (int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
  }
}  