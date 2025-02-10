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
import de.codengine.main.Borderrect;
import de.codengine.main.Bordertrapez;
import de.codengine.main.GenericPoint;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Boote extends Mainanim {
    private GenericImage[] boots;

    public int Breite = 0;
    public int Hoehe = 0;

    private GenericPoint bootPoint;

    private Bordertrapez bootRect;

    // private boolean forward = false;

    private GenericImage bootBild;

    private static final int[][] groesse = {{43, 41}, /*{43, 41},*/ {13, 12}, {41, 80}, {41, 80}, {69, 118}, {99, 78}};

    private static final Bordertrapez[] grenze =
            {new Bordertrapez(456, 639, 604, 787, 222, 315),
                    /*new bordertrapez (456, 639, 604, 787, 222, 315),*/

                    new Bordertrapez(563, 565, 623, 625, 64, 77),

                    new Bordertrapez(-50, 235, 280, 244),
                    new Bordertrapez(402, 245, 700, 272),
                    new Bordertrapez(75, 255, 265, 260),
                    new Bordertrapez(435, 243, 700, 272)};

    // private static final int XMIND = 70;
    // private static final int XDIFF = 70;
    // private static final int YMIND = 120;
    // private static final int YDIFF = 60;

    // private int Verhinderx;
    // private int Verhindery;

    // private int locationID;

    public Boote(Start caller, int locationID) {
        super(caller);

        // locationIDs:
        // 1 : cychi
        // 2 : hdwor
        // 3 : panorama
        // this.locationID = locationID;  // merken, koennte wichtig sein

        boots = new GenericImage[7];

        InitImages();

        evalBoot(locationID);

        InitPosition();

        // Verhinderx = XMIND;
        // Verhindery = YMIND;
    }

    private void InitImages() {
        boots[0] = getPicture("gfx-dd/cychi/boot2.gif");
        //       boots[1] = getPicture ("gfx-dd/cychi/boot2a.gif");

        boots[1] = getPicture("gfx-dd/hdwor/boot5.gif");

        boots[2] = getPicture("gfx-dd/panorama/boot1.gif");
        boots[3] = getPicture("gfx-dd/panorama/boot1a.gif");
        boots[4] = getPicture("gfx-dd/panorama/boot3.gif");
        boots[5] = getPicture("gfx-dd/panorama/boot4.gif");
    }

    public void cleanup() {
        boots[0] = null;
        boots[1] = null;
        boots[2] = null;
        boots[3] = null;
        boots[4] = null;
        boots[5] = null;
    }

    private void evalBoot(int locID) {
        int helper = 0;

        // je nach Location Boot zufaellig bestimmen
        switch (locID) {
            case 1: // cychi
                helper = 0;
                break;
            case 2: // hdwor
                helper = 1;
                break;
            case 3: // panorama
                helper = (int) ((Math.random() * 3.9) + 2);
                break;
        }

        // ermitteltes Boot einsetzen
        bootBild = boots[helper];
        bootRect = grenze[helper];

        Breite = groesse[helper][0];
        Hoehe = groesse[helper][1];

    }

    // zufaellige Position innerhalb des bordertrapez bestimmen und Richtung
    private void InitPosition() {
        // innerhalb des umschliessenden Rects Zufallsschuss und auf innerhalb pruefen
        // erstmal die Grenzen
        int xmin = (bootRect.x1 < bootRect.x3) ? bootRect.x1 : bootRect.x3;
        int xmax = (bootRect.x2 > bootRect.x4) ? bootRect.x2 : bootRect.x4;
        int ymin = bootRect.y1;
        int ymax = bootRect.y2;

        int xt, yt;

        // Punkt erzeugen und auf Gueltigkeit ueberpruefen
        do {
            xt = (int) ((Math.random() * (xmax - xmin)) + xmin);
            yt = (int) ((Math.random() * (ymax - ymin)) + ymin);
        }
        while (bootRect.PointInside(new GenericPoint(xt, yt)) == false);

        // tu es mal auch wirklich einsetzen tun tun tun
        bootPoint = new GenericPoint(xt, yt);

        // Richtung des Bootes zufaellig bestimmen
    	/*
    	int zf = (int) (Math.random () * 50);
    	if (zf > 25) forward = true;
    	else forward = false;
    	*/

    }

    // Zeichne Boot
    public void drawBoot(GenericDrawingContext g) {
        int x = bootPoint.x - (Breite / 2);
        int y = bootPoint.y - Hoehe;

        g.drawImage(bootBild, x, y, null);

        // moveBoot ();
    }

    // gibt das aktuelle Borderrect fuer das Boot an fuer Cliprect und Loeschen
    public Borderrect evalBootRect() {
        int x = bootPoint.x - (Breite / 2);
        int y = bootPoint.y - Hoehe;
        return (new Borderrect(x - 2, y - 2, x + Breite + 4, y + Hoehe + 4));
    }

}    