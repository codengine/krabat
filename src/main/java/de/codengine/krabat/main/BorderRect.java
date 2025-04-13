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


public class BorderRect {
    public final GenericPoint topLeftPoint;         // Punkt links oben
    public final GenericPoint bottomRightPoint;         // Punkt rechts unten

    // Grenz-Rechteck anlegen
    public BorderRect(int x1, int y1, int x2, int y2) {
        topLeftPoint = new GenericPoint(x1, y1);
        bottomRightPoint = new GenericPoint(x2, y2);
    }

    // Befindet sich der Punkt in diesem BorderRect
    public boolean isPointInRect(GenericPoint pTemp) {
        return topLeftPoint.x <= pTemp.x && pTemp.x <= bottomRightPoint.x &&
                topLeftPoint.y <= pTemp.y && pTemp.y <= bottomRightPoint.y;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof BorderRect) {
            BorderRect inst = (BorderRect) o;
            return inst.topLeftPoint.x == topLeftPoint.x &&
                    inst.topLeftPoint.y == topLeftPoint.y &&
                    inst.bottomRightPoint.x == bottomRightPoint.x &&
                    inst.bottomRightPoint.y == bottomRightPoint.y;
        }

        return false;
    }
}