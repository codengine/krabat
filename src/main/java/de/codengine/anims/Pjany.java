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

package de.codengine.anims;

import de.codengine.Start;
import de.codengine.main.GenericPoint;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Pjany extends Mainanim {
    private GenericImage[] hosc;

    public static final int Breite = 97;
    public static final int Hoehe = 120;

    private int Position = 0;

    private int Verhinderposition;
    private static final int MAX_VERHINDERPOSITION = 10;

    public Pjany(Start caller) {
        super(caller);

        hosc = new GenericImage[2];

        Verhinderposition = MAX_VERHINDERPOSITION;

        InitImages();
    }

    private void InitImages() {
        hosc[0] = getPicture("gfx/hoscenc/gast1-1.gif");
        hosc[1] = getPicture("gfx/hoscenc/gast1-2.gif");
    }

    // Zeichne Saeufer, wie er dasteht oder spricht
    public void drawPjany(GenericDrawingContext g, int TalkPerson, GenericPoint Posit, int AnimTalkPerson) {
        // reden ist egal, er bewegt sich nur ab und zu
        if ((--Verhinderposition) < 1) {
            Verhinderposition = MAX_VERHINDERPOSITION;
            Position = (int) (Math.random() * 1.9);
        }

        g.drawImage(hosc[Position], Posit.x, Posit.y, null);
    }
}    