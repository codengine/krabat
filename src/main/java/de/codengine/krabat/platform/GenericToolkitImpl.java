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

import de.codengine.krabat.main.GenericFilteredImageSource;
import de.codengine.krabat.main.GenericMemoryImageSource;
import de.codengine.krabat.main.GenericPoint;

public abstract class GenericToolkitImpl {

    public static final int ALLBITS = 32;

    public abstract GenericImage createImage(
            GenericMemoryImageSource genericMemoryImageSource);

    public abstract GenericCursor createCustomCursor(GenericImage getCursorImage,
                                                     GenericPoint hotSpot, String string);

    public abstract void prepareImage(GenericImage genericImage);

    public abstract int checkImage(GenericImage genericImage);

    public abstract GenericImage createImage(
            GenericFilteredImageSource genericFilteredImageSource);

    public abstract GenericImage createImage(int i, int j);

    public abstract void grabPixelsFromImage(GenericImage actualImage,
                                             int x, int y, int w, int h, int[] pix, int off, int scansize);

}
