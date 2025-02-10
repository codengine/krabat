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

public class GenericMemoryImageSource {

    private final int w;

    private final int h;

    private final int[] pix;

    private final int off;

    private final int scan;

    public GenericMemoryImageSource(int w, int h, int[] pix, int off, int scan) {
        this.w = w;
        this.h = h;
        this.pix = pix;
        this.off = off;
        this.scan = scan;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int[] getPix() {
        return pix;
    }

    public int getOff() {
        return off;
    }

    public int getScan() {
        return scan;
    }
}
