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

public class RavenFreedom extends MainAnim {
    private final GenericImage[] Vogel;
    private int whichanim = 0;
    private int Animcount = 0;

    private int krlinks;
    private int krrechts;

    private static final int ANIMKONSTANTE = 100;

    public static final int Breite = 34;
    public static final int Hoehe = 43;

    public RavenFreedom(Start caller) {
        super(caller);

        Vogel = new GenericImage[5];

        initImages();

        krlinks = (int) Math.round(Math.random() * 3);
        krrechts = (int) Math.round(Math.random() * 3);
    }

    private void initImages() {
        Vogel[1] = getPicture("gfx/rapak/pk1.png");  // 34x43 Size
        Vogel[2] = getPicture("gfx/rapak/pk2.png");
        Vogel[3] = getPicture("gfx/rapak/pk3.png");
        Vogel[4] = getPicture("gfx/rapak/pk4.png");
    }

    // Zeichne Raben bei Animationen oder reden
    public void drawRapak(GenericDrawingContext g, GenericPoint Pos) {
        // Raben beim Herumhaengen (normale Anims)
        if (whichanim == 0) {
            whichanim = (int) Math.round(Math.random() * ANIMKONSTANTE) + 1;
        }
        if (whichanim > 3) {
            if (whichanim < 10) {
                whichanim = 1;
            } else {
                whichanim = 0;
            }
        }

        switch (whichanim) {
            case 0:
                // normalen Raben zeichnen
                drawIt(g, 1, Pos);
                break;

            case 1:
                // Zwinkern
                if (Animcount == 0) {
                    drawIt(g, 2, Pos);
                }
                if (Animcount == 1) {
                    drawIt(g, 2, Pos);
                }
                Animcount++;
                if (Animcount >= 2) {
                    Animcount = 0;
                    whichanim = 0;
                }
                break;

            case 2:
                // unter Fluegel kratzen
                if (Animcount == 0) {
                    drawIt(g, 3, Pos);
                }
                if (Animcount == 1) {
                    drawIt(g, 3, Pos);
                }
                if (Animcount == 2) {
                    drawIt(g, 1, Pos);
                }
                if (Animcount == 3) {
                    drawIt(g, 1, Pos);
                }
                if (Animcount == 4) {
                    drawIt(g, 3, Pos);
                }
                if (Animcount == 5) {
                    drawIt(g, 3, Pos);
                }
                Animcount++;
                if (Animcount >= 6) {
                    Animcount = 0;
                    whichanim = 0;
                }
                break;

            case 3:
                // unter Fluegel kratzen andere Seite
                if (Animcount == 0) {
                    drawIt(g, 4, Pos);
                }
                if (Animcount == 1) {
                    drawIt(g, 4, Pos);
                }
                if (Animcount == 2) {
                    drawIt(g, 1, Pos);
                }
                if (Animcount == 3) {
                    drawIt(g, 1, Pos);
                }
                if (Animcount == 4) {
                    drawIt(g, 4, Pos);
                }
                if (Animcount == 5) {
                    drawIt(g, 4, Pos);
                }
                Animcount++;
                if (Animcount >= 6) {
                    Animcount = 0;
                    whichanim = 0;
                }
                break;
        }
    }

    public void scratchLeft(GenericDrawingContext g, GenericPoint position) {
        // unter linkem Fluegel kratzen
        if (krlinks == 0) {
            drawIt(g, 3, position);
        }
        if (krlinks == 1) {
            drawIt(g, 3, position);
        }
        if (krlinks == 2) {
            drawIt(g, 1, position);
        }
        if (krlinks == 3) {
            drawIt(g, 1, position);
        }

        krlinks++;
        if (krlinks >= 3) {
            krlinks = 0;
        }
    }

    public void scratchRight(GenericDrawingContext g, GenericPoint position) {
        // unter rechtem Fluegel kratzen
        if (krrechts == 0) {
            drawIt(g, 4, position);
        }
        if (krrechts == 1) {
            drawIt(g, 4, position);
        }
        if (krrechts == 2) {
            drawIt(g, 1, position);
        }
        if (krrechts == 3) {
            drawIt(g, 1, position);
        }

        krrechts++;
        if (krrechts >= 3) {
            krrechts = 0;
        }
    }

    private void drawIt(GenericDrawingContext offGraph, int which, GenericPoint ps) {
        offGraph.drawImage(Vogel[which], ps.x, ps.y);
    }
}    