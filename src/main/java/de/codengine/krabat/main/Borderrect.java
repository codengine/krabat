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

package de.codengine.krabat.main;


public class Borderrect {
    public final GenericPoint lo_point;         // Punkt links oben
    public final GenericPoint ru_point;         // Punkt rechts unten

    // Grenz-Rechteck anlegen
    public Borderrect(int x1, int y1, int x2, int y2) {
        lo_point = new GenericPoint(x1, y1);
        ru_point = new GenericPoint(x2, y2);
    }

    // Ermittelt den naehesten Punkt im Rechteck, wenn Zielpunkt ausserhalb des
    // Rechtecks
  /* public GenericPoint GetBestPoint (GenericPoint pnt)
  {
    GenericPoint best_point = new GenericPoint ();

    // Beste X-Position ermitteln
    if (pnt.x < lo_point.x) best_point.x = lo_point.x;
    else {
      if (ru_point.x < pnt.x) best_point.x = ru_point.x;
      else best_point.x = pnt.x;
    }

    // Beste Y-Position ermitteln
    if (pnt.y < lo_point.y) best_point.y = lo_point.y;
    else {
      if (ru_point.y < pnt.y) best_point.y = ru_point.y;
      else best_point.y = pnt.y;
    }
    
    return best_point;
  } */

    // liefert den Kuerzesten Abstand eines Punktes vom Rechteck
    // Rueckgabe = 0, falls Punkt im Rechteck enthalten
  /* public float  MinDistanceToPoint (GenericPoint pt)
  {
    float distx = 0;
    float disty = 0;
    
    // X-Abstand berechnen
    if (pt.x < lo_point.x)           // Punkt links davon
      distx = lo_point.x - pt.x;
    if (pt.x > ru_point.x)           // rechts davon
      distx = pt.x - ru_point.x;

    // Y-Abstand berechnen
    if (pt.y < lo_point.y)           // drueber
      disty = lo_point.y - pt.y;
    if (pt.y > ru_point.y)           // drunter
      disty = pt.y - ru_point.y;

    float distance = (float) Math.sqrt ((distx * distx) + (disty * disty));
    return distance;
  } */

    // Befindet sich der Punkt in diesem BorderRect
    public boolean IsPointInRect(GenericPoint pTemp) {
        // System.out.println(" GenericPoint "+pTemp.x+" "+pTemp.y+" is not in lo "+lo_point.x+" "+
        // lo_point.y+" ru "+ru_point.x+" "+ru_point.y);
        return lo_point.x <= pTemp.x && pTemp.x <= ru_point.x &&
                lo_point.y <= pTemp.y && pTemp.y <= ru_point.y;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Borderrect) {
            Borderrect inst = (Borderrect) o;
            return inst.lo_point.x == lo_point.x &&
                    inst.lo_point.y == lo_point.y &&
                    inst.ru_point.x == ru_point.x &&
                    inst.ru_point.y == ru_point.y;
        }

        return false;
    }


    // Rueckgabe einzelner Koordinaten eines BorderRects
 /* public int gtx()
  {
    return lo_point.x;
  }

  public int gty()
  {
    return lo_point.y;
  }

  public int gtw()
  {
    return (ru_point.x - lo_point.x);
  }

  public int gth()
  {
    return (ru_point.y - lo_point.y);
  } */ 

  /* public int CenterDistance (GenericPoint feet)
  {
    int Mittex = Math.abs (feet.x - (((ru_point.x - lo_point.x) / 2) + lo_point.x));
    int Mittey = Math.abs (feet.y - (((ru_point.y - lo_point.y) / 2) + lo_point.y));
    int Abstand = (int) Math.sqrt ((Mittex * Mittex) + (Mittey * Mittey));
    return Abstand;
  }	*/
}