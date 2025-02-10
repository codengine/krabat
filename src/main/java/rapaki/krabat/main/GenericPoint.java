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

// ok

public class GenericPoint {

    public int x;

    public int y;

    public GenericPoint() {
        x = 0;
        y = 0;
    }

    public GenericPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public GenericPoint(GenericPoint pt) {
        this.x = pt.x;
        this.y = pt.y;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof GenericPoint) {
            GenericPoint inst = (GenericPoint) o;
            if ((inst.x == x) && (inst.y == y)) {
                return true;
            }
        }

        return false;
    }
}
