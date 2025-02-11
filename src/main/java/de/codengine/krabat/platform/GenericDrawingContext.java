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

package de.codengine.krabat.platform;

import de.codengine.krabat.main.GenericColor;
import de.codengine.krabat.main.GenericRectangle;

public abstract class GenericDrawingContext {

    public abstract void setClip(int x, int y, int xd, int yd);

    public abstract GenericRectangle getClipBounds();

    public abstract void clearRect(int k, int j, int i, int k2);

    public abstract void setClip(GenericRectangle txx);

    public abstract void setColor(GenericColor inakt);

    public abstract void drawRect(int i, int j, int k, int l);

    public abstract void drawLine(int i, int j, int k, int l);

    public abstract void drawImage(GenericImage genericImage, int x, int y);

    public abstract void drawImage(GenericImage ktemp, int left, int up,
                                   int koerperbreite, int i);

    public abstract GenericDrawingContext2D get2DContext();

}
