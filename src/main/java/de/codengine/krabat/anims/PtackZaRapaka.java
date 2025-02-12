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
import de.codengine.krabat.main.GenericRectangle;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class PtackZaRapaka extends Mainanim {
    private final GenericImage[] vogel;
    private int x;
    private int y;
    private int animpos = 1;
    private boolean Gleiten = false;
    private boolean oben = true;
    // private boolean up = false;
    private int gleitcount;
    private boolean schalt = false;
    private final int XEnde;
    private static final int MAXGLEIT = 10;

    private final int Zoomfaktor;

    private boolean isNeverGleiting = false;

    // GenericImage ist 50 Breit und 50 Hoch

    // Erzeugen
    public PtackZaRapaka(Start caller, int x, int y, int XEnde) {
        super(caller);
        this.x = x;
        this.y = y;
        this.XEnde = XEnde;
        Zoomfaktor = 0;
        gleitcount = MAXGLEIT;

        vogel = new GenericImage[5];

        InitImages();
    }

    // gezoomten Ptack laden
    public PtackZaRapaka(Start caller, int x, int y, int Zoomfaktor, int XEnde) {
        super(caller);
        this.x = x;
        this.y = y;
        this.Zoomfaktor = Zoomfaktor;
        this.XEnde = XEnde;
        gleitcount = MAXGLEIT;

        vogel = new GenericImage[5];

        InitImages();
    }

    // gezoomten Ptack laden, der nur nach oben fliegt
    public PtackZaRapaka(Start caller, int x, int y, int Zoomfaktor, int XEnde, boolean isNeverGleiting) {
        super(caller);
        this.x = x;
        this.y = y;
        this.Zoomfaktor = Zoomfaktor;
        this.XEnde = XEnde;
        this.isNeverGleiting = isNeverGleiting;
        gleitcount = MAXGLEIT;

        vogel = new GenericImage[5];

        InitImages();
    }

    // Bilder laden
    private void InitImages() {
        vogel[1] = getPicture("gfx/doma/vogel1.gif");
        vogel[2] = getPicture("gfx/doma/vogel2.gif");
        vogel[3] = getPicture("gfx/doma/vogel3.gif");
        vogel[4] = getPicture("gfx/doma/vogel4.gif");
    }

    // Gib mir das Rectangle zurueck, wo der Vogel drin ist (aktuell)
    public GenericRectangle ptack2Rect() {
        return new GenericRectangle(x - 15, y - 15, 51 + 30, 51 + 30);
    }

    // rumfliegen
    public boolean Flieg(GenericDrawingContext g) {
        // wenn Fluegel in Mitte, dann schauen, ob weiterfliegen oder gleiten
        if (animpos == 1 || animpos == 4) {
            int glei = (int) Math.round(Math.random() * 30);
            if (glei < 29 && !Gleiten || isNeverGleiting) {
                // weiterfliegen
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
                // gleiten, dabei manchmal Schnabel auf
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
            // Fluegel in Extremposition, also wieder auf Mittelstellung setzen
            schalt = !schalt;
            if (schalt) {
                animpos = 1;
            }
        }

        // X- Offset fuer Fliegen ist 10 (ungezoomt)
        if (x > XEnde) {
            x = x - 10 + Zoomfaktor / 10;
        }

        // Beim Gleiten y nach unten, beim Fliegen nach oben, ab bestimmem Zoomfaktor nicht mehr...
        int versch = (int) Math.round(Math.random() * 20);
        if (versch > 10 && Zoomfaktor < 10/* || (isNeverGleiting == true)*/) {
            if (Gleiten) {
                y += 1;
            } else {
                y -= 1;
            }
        }
    
	/*int xx = x;
	  if (xx < 0) xx = 0;
	  int yy = y;
	  if (yy < 0) yy = 0; */

        // g.setClip (xx, yy, xx + 50, yy + 50);
        g.drawImage(vogel[animpos], x, y, 50 - Zoomfaktor, 50 - Zoomfaktor);
        return x > XEnde; // wenn aus dem Bild, dann das der Routine sagen !
    }
}    						