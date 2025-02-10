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

public class LogoPtack extends Mainanim {
    private final GenericImage[] vogel;
    private int x, y;
    private int animpos = 1;
    private boolean last2 = false;
    private boolean animlock = true;

    public LogoPtack(Start caller, int x, int y) {
        super(caller);

        this.x = x;
        this.y = y;
        animpos = 1;

        vogel = new GenericImage[4];

        InitImages();

    }

    private void InitImages() {
        vogel[1] = getPicture("gfx/intro/iv1.gif");
        vogel[2] = getPicture("gfx/intro/iv2.gif");
        vogel[3] = getPicture("gfx/intro/iv3.gif");
    }

    @Override
    public void cleanup() {
        vogel[1] = null;
        vogel[2] = null;
        vogel[3] = null;
    }

    public boolean Flieg(GenericDrawingContext g) {
        switch (animpos) {
            case 1:
                // Gleitphase
                if ((Math.round(Math.random() * 30) < 20) && (y > 200)) {
                    if (last2) {
                        last2 = false;
                        animpos = 3;
                        animlock = true;
                    } else {
                        last2 = true;
                        animpos = 2;
                        animlock = true;
                    }
                }
                break;
            case 2:
                // Fluegelschlag 1. Richtung
                if (!animlock) {
                    animpos = 1;
                } else {
                    animlock = false;
                }
                break;
            case 3:
                // Fluegelschlag 2. Richtung
                if (!animlock) {
                    animpos = 1;
                } else {
                    animlock = false;
                }
                break;
        }

        x -= 8;
        if (animpos == 1) {
            y += 1;
        } else {
            y -= 1;
        }

        // g.setClip (xx, yy, xx + 50, yy + 50);
        g.drawImage(vogel[animpos], x, y, null);
        return x >= -50;
    }
}    						
    						