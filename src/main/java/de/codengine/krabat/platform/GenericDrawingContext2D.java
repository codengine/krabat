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

import de.codengine.krabat.main.GenericAlphaComposite;
import de.codengine.krabat.main.GenericColor;

public abstract class GenericDrawingContext2D {

    public abstract void setComposite(GenericAlphaComposite ad);

    public abstract void setColor(GenericColor color);

    public abstract void drawImage(GenericImage genericImage, int x, int y);

    public abstract void fillRect(int x1, int y1, int width, int height);

}
