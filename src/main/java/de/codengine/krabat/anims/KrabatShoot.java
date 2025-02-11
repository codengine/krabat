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

public class KrabatShoot extends Mainanim {
    private final GenericImage[] krabat_schiessen;
    private GenericImage stein;

    private int Counter = 0;

    private static final int CWIDTH = 50;
    private static final int CHEIGHT = 100;

    private final GenericPoint kamjenStart = new GenericPoint(275, 268);
    private final GenericPoint kamjenStop = new GenericPoint(428, 162);
    private float kamjenx;
    private float kamjeny;

    private static final float XOFFSET = 10;
    private final float Yoffset;

    public int maxx = 0;
    public float zoomf = 0.0f;
    public int defScale = 0;

    public KrabatShoot(Start caller) {
        super(caller);

        krabat_schiessen = new GenericImage[2];
        kamjenx = kamjenStart.x;
        kamjeny = kamjenStart.y;
        float tStopx = kamjenStop.x;
        float tStopy = kamjenStop.y;
        Yoffset = (tStopy - kamjeny) / (tStopx - kamjenx) * XOFFSET;

        InitImages();
    }

    private void InitImages() {
        krabat_schiessen[0] = getPicture("gfx/rapak/k-o-waffe1.gif");
        krabat_schiessen[1] = getPicture("gfx/rapak/k-o-waffe2.gif");

        stein = getPicture("gfx/rapak/kamjen.gif");
    }

    // Zeichne Krabat, wie sie dasteht oder spricht
    public boolean drawKrabat(GenericDrawingContext g, GenericPoint pos) {
        if (Counter == 0) {
            mainFrame.wave.PlayFile("sfx/spuck.wav");
        }  ///////////////////// Sound !!!!!!!!!!!!!!!!!!!!!!!!

        // Stein bewegen
        kamjenx += XOFFSET;
        kamjeny += Yoffset;

        // Stein zeichnen
        g.drawImage(stein, (int) kamjenx, (int) kamjeny);

        // Ermittlung der Hoehendifferenz beim Zooming
        float helper = (maxx - pos.y) / zoomf;
        if (helper < 0) {
            helper = 0;
        }
        helper += defScale;

        int tHelper = (int) helper;

        // Links-Oben-Pos berechnen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int x = pos.x - (CWIDTH - tHelper / 2) / 2;
        int y = pos.y - (CHEIGHT - tHelper);

        // Krabat beim Schiessen zeichnen
        if (Counter < 2 || Counter > 5) {
            g.drawImage(krabat_schiessen[0], x, y, CWIDTH - tHelper / 2, CHEIGHT - tHelper);
        } else {
            g.drawImage(krabat_schiessen[1], x, y, CWIDTH - tHelper / 2, CHEIGHT - tHelper);
        }

        // Counter eins hoch
        Counter++;

        // Testen ob Ende und alles zuruecksetzen
        boolean zurueck;
        if (kamjenx > kamjenStop.x || kamjeny < kamjenStop.y) {
            zurueck = false;
            kamjenx = kamjenStart.x;
            kamjeny = kamjenStart.y;
            Counter = 0;
            mainFrame.wave.PlayFile("sfx/rapak3.wav");
        } else {
            zurueck = true;
        }

        return zurueck;
    }
}    