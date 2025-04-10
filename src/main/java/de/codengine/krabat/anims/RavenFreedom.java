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

        InitImages();

        krlinks = (int) Math.round(Math.random() * 3);
        krrechts = (int) Math.round(Math.random() * 3);
    }

    private void InitImages() {
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
                DrawIt(g, 1, Pos);
                break;

            case 1:
                // Zwinkern
                if (Animcount == 0) {
                    DrawIt(g, 2, Pos);
                }
                if (Animcount == 1) {
                    DrawIt(g, 2, Pos);
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
                    DrawIt(g, 3, Pos);
                }
                if (Animcount == 1) {
                    DrawIt(g, 3, Pos);
                }
                if (Animcount == 2) {
                    DrawIt(g, 1, Pos);
                }
                if (Animcount == 3) {
                    DrawIt(g, 1, Pos);
                }
                if (Animcount == 4) {
                    DrawIt(g, 3, Pos);
                }
                if (Animcount == 5) {
                    DrawIt(g, 3, Pos);
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
                    DrawIt(g, 4, Pos);
                }
                if (Animcount == 1) {
                    DrawIt(g, 4, Pos);
                }
                if (Animcount == 2) {
                    DrawIt(g, 1, Pos);
                }
                if (Animcount == 3) {
                    DrawIt(g, 1, Pos);
                }
                if (Animcount == 4) {
                    DrawIt(g, 4, Pos);
                }
                if (Animcount == 5) {
                    DrawIt(g, 4, Pos);
                }
                Animcount++;
                if (Animcount >= 6) {
                    Animcount = 0;
                    whichanim = 0;
                }
                break;
        }
    }

    public void KratzeLinks(GenericDrawingContext g, GenericPoint Pos) {
        // unter linkem Fluegel kratzen
        if (krlinks == 0) {
            DrawIt(g, 3, Pos);
        }
        if (krlinks == 1) {
            DrawIt(g, 3, Pos);
        }
        if (krlinks == 2) {
            DrawIt(g, 1, Pos);
        }
        if (krlinks == 3) {
            DrawIt(g, 1, Pos);
        }

        krlinks++;
        if (krlinks >= 3) {
            krlinks = 0;
        }
    }

    public void KratzeRechts(GenericDrawingContext g, GenericPoint Pos) {
        // unter rechtem Fluegel kratzen
        if (krrechts == 0) {
            DrawIt(g, 4, Pos);
        }
        if (krrechts == 1) {
            DrawIt(g, 4, Pos);
        }
        if (krrechts == 2) {
            DrawIt(g, 1, Pos);
        }
        if (krrechts == 3) {
            DrawIt(g, 1, Pos);
        }

        krrechts++;
        if (krrechts >= 3) {
            krrechts = 0;
        }
    }

    private void DrawIt(GenericDrawingContext offGraph, int which, GenericPoint ps) {
        offGraph.drawImage(Vogel[which], ps.x, ps.y);
    }
}    