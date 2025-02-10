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

public class Bow extends Mainanim {
    private final GenericImage[] bow;

    private int Bowcount = 1;

    private int Verhinderanim;
    private static final int MAX_VERHINDERANIM = 70;

    private int Verhindercount;
    private static final int MAX_VERHINDERCOUNT = 2;

    public static final int Breite = 10;
    public static final int Hoehe = 20;

    public Bow(Start caller) {
        super(caller);

        bow = new GenericImage[8];

        InitImages();

        Verhinderanim = MAX_VERHINDERANIM;
        Verhindercount = MAX_VERHINDERCOUNT;
    }

    private void InitImages() {
        bow[1] = getPicture("gfx/haty/bow1.gif");
        bow[2] = getPicture("gfx/haty/bow2.gif");
        bow[3] = getPicture("gfx/haty/bow3.gif");
        bow[4] = getPicture("gfx/haty/bow4.gif");
        bow[5] = getPicture("gfx/haty/bow5.gif");
        bow[6] = getPicture("gfx/haty/bow6.gif");
        bow[7] = getPicture("gfx/haty/bow7.gif");
    }

    @Override
    public void cleanup() {
        bow[1] = null;
        bow[2] = null;
        bow[3] = null;
        bow[4] = null;
        bow[5] = null;
        bow[6] = null;
        bow[7] = null;
    }

    // Zeichne Hauptwachter, wie er dasteht oder spricht
    public void drawBow(GenericDrawingContext offGraph, GenericPoint Posit) {
        switch (Bowcount) {
            case 1:
                // evaluieren, ob eine Anim erfolgen darf
                if (--Verhinderanim < 1) {
                    Verhinderanim = MAX_VERHINDERANIM;

                    // Wahrscheinlichkeit von 75 %
                    int zuffi = (int) Math.round(Math.random() * 100);
                    if (zuffi < 75) {
                        if (zuffi < 37) {
                            Bowcount = 2;
                        } else {
                            Bowcount = 5;
                        }
                    }
                }
                break;

            case 2:
            case 3:
            case 4:     // Anim weiterzaehlen
                if (--Verhindercount < 1) {
                    Verhindercount = MAX_VERHINDERCOUNT;
                    Bowcount++;
                    if (Bowcount == 5) {
                        Bowcount = 1;
                    }
                }
                break;

            case 5:
            case 6:
            case 7: // Anim weiterzaehlen
                if (--Verhindercount < 1) {
                    Verhindercount = MAX_VERHINDERCOUNT;
                    Bowcount++;
                    if (Bowcount == 8) {
                        Bowcount = 1;
                    }
                }
                break;

            default:
                System.out.println("Falsche Eimerphase aufgetreten !!");
                break;
        }

        offGraph.drawImage(bow[Bowcount], Posit.x, Posit.y, null);

    }
}    