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
import de.codengine.krabat.main.GenericRectangle;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Duck1 extends MainAnim {

    private final GenericImage[] kacka_left;
    private final GenericImage[] kacka_front;

    private int Schnatter = 0;

    private int Verhinderschnatter;

    private static final int MAX_VERHINDERSCHNATTER = 3;

    public final GenericPoint Posit;

    private static final GenericPoint Pstart = new GenericPoint(410, 210);
    private static final GenericPoint Pstop = new GenericPoint(300, 220);

    private static final float Xoffset = 1;
    private final float Yoffset;

    private static final int Breite = 40;
    private static final int Hoehe = 17;

    public Duck1(Start caller) {
        super(caller);

        kacka_left = new GenericImage[2];
        kacka_front = new GenericImage[2];

        InitImages();

        Verhinderschnatter = MAX_VERHINDERSCHNATTER;

        float Ydiff = (float) (Pstop.y - Pstart.y);
        float Xdiff = (float) (Pstop.x - Pstart.x);

        Yoffset = Ydiff / Xdiff * Xoffset;
        Posit = new GenericPoint(Pstart.x, Pstart.y);
    }

    private void InitImages() {
        kacka_left[0] = getPicture("gfx/mertens/kacka1.png");
        kacka_left[1] = getPicture("gfx/mertens/kacka1a.png");

        kacka_front[0] = getPicture("gfx/mertens/kacka1-t.png");
        kacka_front[1] = getPicture("gfx/mertens/kacka1-ta.png");
    }

    @Override
    public void cleanup() {
        kacka_left[0] = null;
        kacka_left[1] = null;

        kacka_front[0] = null;
        kacka_front[1] = null;
    }

    // evaluiere Rechteck zum Loeschen der Ente
    public GenericRectangle kackaRect() {
        int x = Posit.x - Breite / 2;
        int y = Posit.y - Hoehe;
        return new GenericRectangle(x, y, Breite, Hoehe);
    }

    // Zeichne Ente, wie sie dasteht oder spricht
    public void drawKacka(GenericDrawingContext offGraph, int TalkPerson, boolean isLeft, boolean moveAllowed) {
        // wenn noch erlaubt, dann Ente rumschwimmen lassen
        int zf = (int) (Math.random() * 100);
        // nur mit bestimmter Wahrscheinlichkeit erlauben
        if (zf > 80 && moveAllowed) {
            // nur, solange Endposition noch nicht erreicht ist
            if (Posit.x > Pstop.x) {
                Posit.x -= (int) Xoffset;
                Posit.y -= (int) Yoffset;
            }
        }

        // Redende Kacka
        if (TalkPerson == 71 && mainFrame.talkCount > 1) {
            if (--Verhinderschnatter < 1) {
                Verhinderschnatter = MAX_VERHINDERSCHNATTER;
                if (Schnatter == 0) {
                    Schnatter = 1;
                } else {
                    Schnatter = 0;
                }
            }

            // Kacka zeichnen
            if (isLeft) {
                offGraph.drawImage(kacka_left[Schnatter], Posit.x - Breite / 2, Posit.y - Hoehe);
            } else {
                offGraph.drawImage(kacka_front[Schnatter], Posit.x - Breite / 2, Posit.y - Hoehe);
            }
        }

        // rumschwimmende Kacka
        else {
            // Kacka zeichnen
            if (isLeft) {
                offGraph.drawImage(kacka_left[0], Posit.x - Breite / 2, Posit.y - Hoehe);
            } else {
                offGraph.drawImage(kacka_front[0], Posit.x - Breite / 2, Posit.y - Hoehe);
            }
        }
    }
}    