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

// ok

public class GenericRectangle {

    private final Borderrect rectangleImpl;

    public GenericRectangle(int x, int y, int width, int height) {
        rectangleImpl = new Borderrect(x, y, x + width, y + height);
    }

    public int getX() {
        return rectangleImpl.lo_point.x;
    }

    public int getY() {
        return rectangleImpl.lo_point.y;
    }

    public int getWidth() {
        return (rectangleImpl.ru_point.x - rectangleImpl.lo_point.x);
    }

    public int getHeight() {
        return (rectangleImpl.ru_point.y - rectangleImpl.lo_point.y);
    }

    public boolean contains(GenericPoint pTemp) {
        return rectangleImpl.IsPointInRect(pTemp);
    }

}
