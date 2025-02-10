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

package rapaki.krabat.platform;

public abstract class GenericImage {

    public static final int SCALE_DEFAULT = 1;

    public abstract int getHeight(GenericImageObserver object);

    public abstract int getWidth(GenericImageObserver object);

    public abstract GenericImageProducer getSource();

    public abstract GenericDrawingContext getGraphics();

    public abstract GenericImage getScaledInstance(int width, int height, int hints);
}
