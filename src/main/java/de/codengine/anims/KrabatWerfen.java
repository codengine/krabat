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

public class KrabatWerfen extends Mainanim {
    private final GenericImage[] krabat_werfen;

    private static final int CWIDTH = 100;
    private static final int CHEIGHT = 100;

    public int maxx = 0;
    public float zoomf = 0.0f;
    public int defScale = 0;

    private int BildIndex = 0;
    private int Umlaeufe = 3;

    private int Verhinderwerfen;

    private static final int MAX_VERHINDERWERFEN = 2;

    public KrabatWerfen(Start caller) {
        super(caller);

        krabat_werfen = new GenericImage[4];

        InitImages();

        Verhinderwerfen = MAX_VERHINDERWERFEN;
    }

    private void InitImages() {
        krabat_werfen[0] = getPicture("gfx-dd/haska/k-l-kw1.gif");
        krabat_werfen[1] = getPicture("gfx-dd/haska/k-l-kw2.gif");
        krabat_werfen[2] = getPicture("gfx-dd/haska/k-l-kw3.gif");
        krabat_werfen[3] = getPicture("gfx-dd/haska/k-l-kw4.gif");
    }

    // Zeichne Krabat, wie er dasteht oder spricht
    public boolean drawKrabat(GenericDrawingContext g, GenericPoint pos) {
        // Ermittlung der Hoehendifferenz beim Zooming
        float helper = (maxx - pos.y) / zoomf;
        if (helper < 0) {
            helper = 0;
        }
        helper += defScale;

        int tHelper = (int) helper;

        // Links-Oben-Pos berechnen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int x = pos.x - ((CWIDTH - tHelper) / 2);
        int y = pos.y - (CHEIGHT - tHelper);

        // Krabat beim Schiessen zeichnen
        g.setClip(x, y, CWIDTH - tHelper, CHEIGHT - tHelper);
        g.drawImage(krabat_werfen[BildIndex], x, y, CWIDTH - tHelper, CHEIGHT - tHelper, null);

        boolean zurueck = true;

        // Warteschleife fuer das Weiterschalten des Images
        if ((--Verhinderwerfen) < 1) {
            Verhinderwerfen = MAX_VERHINDERWERFEN;
            BildIndex++;

            if (BildIndex == 1) {
                mainFrame.wave.PlayFile("sfx-dd/wusch2.wav");
            }

            if (BildIndex > 3) {
                // hier einen Umaluf weiterschalten
                --Umlaeufe;
                BildIndex = 0;
                        /*if (Umlaeufe < 1)
                            {
                               // Ende, wenn fertig!
                                zurueck = false;
                            }*/
            }
            if ((Umlaeufe == 0) && (BildIndex == 3)) {
                zurueck = false;
            }
        }

        // Bei true = weitermachen / false = fertig
        return (zurueck);
    }
}    