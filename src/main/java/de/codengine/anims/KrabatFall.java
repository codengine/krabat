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

public class KrabatFall extends Mainanim {
    private GenericImage[] krabat_fallen;

    private int FallCount = 0;
    private int zoom;

    private int Verhinderfallen;

    private static final int MAX_VERHINDERFALLEN = 4;

    private boolean fallSound = false;

    public KrabatFall(Start caller, int zoom) {
        super(caller);

        this.zoom = zoom;

        krabat_fallen = new GenericImage[3];

        Verhinderfallen = MAX_VERHINDERFALLEN;

        InitImages();
    }

    private void InitImages() {
        krabat_fallen[0] = getPicture("gfx-dd/spaniska/k-l-zucken.gif");
        krabat_fallen[1] = getPicture("gfx-dd/spaniska/k-l-zucken2.gif");
        krabat_fallen[2] = getPicture("gfx-dd/spaniska/k-l-zucken3.gif");
    }

    // Zeichne Krabat, wie sie dasteht oder spricht
    public boolean drawKrabat(GenericDrawingContext g, GenericPoint pos) {
        boolean rueckgabe = true; // true solange, wie noch aktiv, nachher false...

        // Feetposoffset fuer diese Images reinrechnen
        pos.x += 10;

        GenericPoint LeftUp = new GenericPoint();

        // GenericImage weiterschalten und Ende eval.
        if ((--Verhinderfallen) < 1) {
            Verhinderfallen = MAX_VERHINDERFALLEN;
            if (FallCount < 2) {
                FallCount++;
            } else {
                rueckgabe = false;
            }

            if (FallCount == 2) {

                if (fallSound == false) {

                    fallSound = true;
                    mainFrame.wave.PlayFile("sfx-dd/fallen.wav");
                }
            }
        }

        // Hoehe und Breite berechnen
        int Hoehe = 100 - zoom;
        int Breite = 0;
        if (FallCount == 0) {
            Breite = 50 - (zoom / 2);
        } else {
            Breite = 100 - zoom;
        }

        // Y-Pos berechnen
        LeftUp.y = pos.y - Hoehe;

        // X-Pos berechnen
        LeftUp.x = pos.x - (Breite / 2);

        // Cliprect setzen
        g.setClip(LeftUp.x, LeftUp.y, Breite + 1, Hoehe + 1);

        // Hier Animphase zeichnen
        g.drawImage(krabat_fallen[FallCount], LeftUp.x, LeftUp.y, Breite, Hoehe, null);

        return (rueckgabe);

    }
}    