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

package de.codengine.krabat.anims;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Plomja extends Mainanim {

    private int Counter = 1;
    private boolean isDrawing = false;
    private static final int KONSTANTE = 70;

    public static final int Breite = 23;
    public static final int Hoehe = 11;

    private final GenericImage[] Feuer;

    public Plomja(Start caller) {
        super(caller);

        Feuer = new GenericImage[8];
        InitImages();
    }

    private void InitImages() {
        Feuer[1] = getPicture("gfx/labyrinth/blink1.gif");
        Feuer[2] = getPicture("gfx/labyrinth/blink2.gif");
        Feuer[3] = getPicture("gfx/labyrinth/blink3.gif");
        Feuer[4] = getPicture("gfx/labyrinth/blink4.gif");
        Feuer[5] = getPicture("gfx/labyrinth/blink5.gif");
        Feuer[6] = getPicture("gfx/labyrinth/blink6.gif");
        Feuer[7] = getPicture("gfx/labyrinth/blink7.gif");

        // Bilder sind 23 x 11 Pixel gross
    }

    public void drawPlomja(GenericDrawingContext g, GenericPoint posit) {
        int zuffi = (int) Math.round(Math.random() * KONSTANTE);
        if (zuffi < 5 || isDrawing) {
            g.drawImage(Feuer[Counter], posit.x, posit.y);
            Counter++;
            if (Counter == 8) {
                Counter = 1;
                isDrawing = false;
            } else {
                isDrawing = true;
            }
        }
    }
}	    	
	  	
	  		