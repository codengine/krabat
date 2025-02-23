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
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class IntroDomaPtack extends Mainanim {
    private final GenericImage[] vogel;
    private int x;
    private int y;
    private int animpos = 1;
    private boolean Gleiten = false;
    private boolean oben = true;
    // private boolean up = false;
    private int gleitcount;
    private boolean schalt = false;
    private boolean start = true;
    private static final int MAXGLEIT = 10;

    public IntroDomaPtack(Start caller, int x, int y) {
        super(caller);
        this.x = x;
        this.y = y;
        gleitcount = MAXGLEIT;

        vogel = new GenericImage[5];

        InitImages();

    }

    private void InitImages() {
        vogel[1] = getPicture("gfx/doma/vogel1.png");
        vogel[2] = getPicture("gfx/doma/vogel2.png");
        vogel[3] = getPicture("gfx/doma/vogel3.png");
        vogel[4] = getPicture("gfx/doma/vogel4.png");
    }

    public boolean Flieg(GenericDrawingContext g) {
        if (animpos == 1 || animpos == 4) {
            int glei = (int) Math.round(Math.random() * 30);
            if (glei < 29 && !Gleiten && !start) {
                schalt = !schalt;
                if (schalt) {
                    oben = !oben;
                    if (!oben) {
                        animpos = 3;
                    } else {
                        animpos = 2;
                    }
                }
            } else {
                int schnab = (int) Math.round(Math.random() * 10);
                if (schnab < 4) {
                    if (animpos != 4) {
                        animpos = 4;
                    } else {
                        animpos = 1;
                    }
                }
                if (gleitcount == MAXGLEIT) {
                    Gleiten = true;
                }
                gleitcount--;
                if (gleitcount == 0) {
                    gleitcount = MAXGLEIT;
                    Gleiten = false;
                }
            }
        } else {
            schalt = !schalt;
            if (schalt) {
                animpos = 1;
            }
        }

        x -= 10;

        int versch = (int) Math.round(Math.random() * 20);
        if (versch > 10 && !start) {
            if (Gleiten) {
                y += 1;
            } else {
                y -= 1;
            }
            if (y < 40) {
                y = 40;
                Gleiten = true;
            }
            if (y > 200) {
                y = 200;
                Gleiten = false;
                gleitcount = MAXGLEIT;
            }
        } else {
            if (start) {
                y += 1;
            }
            if (y >= 60) {
                start = false;
            }
        }

        g.drawImage(vogel[animpos], x, y);
        return x >= -50;
    }
}    						
    						