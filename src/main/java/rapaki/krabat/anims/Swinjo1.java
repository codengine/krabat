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

package rapaki.krabat.anims;

import rapaki.krabat.Start;
import rapaki.krabat.main.GenericPoint;
import rapaki.krabat.main.GenericRectangle;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class Swinjo1 extends Mainanim {
    private GenericImage[] swinjo_look;
    private GenericImage[] swinjo_dance;

    private int Look = 0;
    private int Dance = 1;

    private int Verhinderlook;
    private int Verhinderdance;

    private static final int MAX_VERHINDERLOOK = 10;
    private static final int MAX_VERHINDERDANCE = 3;

    private static final int Breite = 58;
    private static final int Hoehe = 51;

    private boolean oldDance = false;

    private GenericPoint Posit;

    public Swinjo1(Start caller, GenericPoint Posit) {
        super(caller);

        swinjo_look = new GenericImage[5];
        swinjo_dance = new GenericImage[4];

        InitImages();

        Verhinderlook = MAX_VERHINDERLOOK;
        Verhinderdance = MAX_VERHINDERDANCE;

        this.Posit = Posit;
    }

    private void InitImages() {
        swinjo_look[0] = getPicture("gfx/most/swino1.gif");
        swinjo_look[1] = getPicture("gfx/most/swino1a.gif");
        swinjo_look[2] = getPicture("gfx/most/swino1b.gif");
        swinjo_look[3] = getPicture("gfx/most/swino1c.gif");
        swinjo_look[4] = getPicture("gfx/most/swino1d.gif");

        swinjo_dance[0] = getPicture("gfx/most/swino1-d0.gif");
        swinjo_dance[1] = getPicture("gfx/most/swino1-d1.gif");
        swinjo_dance[2] = getPicture("gfx/most/swino1-d2.gif");
        swinjo_dance[3] = getPicture("gfx/most/swino1-d3.gif");
    }

    // gibt Rectangle zurueck, wo sich Schein drin befindet
    public GenericRectangle swinoRect() {
        return (new GenericRectangle(Posit.x - (Breite / 2), Posit.y - Hoehe, Breite, Hoehe));
    }

    // Zeichne Schwein, wie es dasteht oder tanzt
    public void drawSwino(GenericDrawingContext offGraph, boolean isDancing, boolean noSound) {
        // 3 Zustaende: Guck, Tanz, Uebergang
        if ((isDancing == false) && (oldDance == false)) {
            // nur Umschauen
            switch (Look) {
                case 0:
                    // verschiedene Varianten eval.
                    if ((--Verhinderlook) < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        if (((int) (Math.random() * 50)) > 25) {
                            Look = 2;
                        } else {
                            Look = 4;
                        }
                    } else if (((int) (Math.random() * 50)) > 45) {
                        Look = 1;
                    }
                    break;
                case 1:
                    // Zwinkern rueckgaengig
                    Look = 0;
                    break;
                case 2:
                    // entweder Schleife 3-2-3-2... oder 0
                    if ((--Verhinderlook) < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        if (((int) (Math.random() * 50)) > 25) {
                            Look = 3;
                        } else {
                            Look = 0;
                        }
                    }
                    break;
                case 3:
                    // Schleife mit 2
                    if ((--Verhinderlook) < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        Look = 2;
                    }
                    break;
                case 4:
                    // zurueck zu 0
                    if ((--Verhinderlook) < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        Look = 0;
                    }
                    break;
            }

            // hier darf der Sound rein
            if (noSound == false) {
                evalSound();
            }

            // zeichnen
            offGraph.setClip(swinoRect());
            offGraph.drawImage(swinjo_look[Look], Posit.x - (Breite / 2), Posit.y - Hoehe, null);
        }

        if ((isDancing == true) && (oldDance == true)) {
            // rumhuepfen
            if ((--Verhinderdance) < 1) {
                Verhinderdance = MAX_VERHINDERDANCE;
                Dance++;
                if (Dance == 5) {
                    Dance = 1;
                }
            }

            // umrechnen auf richtige Bildvariable
            int tempDance = Dance;
            if (tempDance == 3) {
                tempDance = 1;
            }
            if (tempDance == 4) {
                tempDance = 3;
            }

            // zeichnen
            offGraph.setClip(swinoRect());
            offGraph.drawImage(swinjo_dance[tempDance], Posit.x - (Breite / 2), Posit.y - Hoehe, null);
        }

        if (isDancing != oldDance) {
            // Uebergangszustand, gleich zeichnen
            offGraph.setClip(swinoRect());
            offGraph.drawImage(swinjo_dance[0], Posit.x - (Breite / 2), Posit.y - Hoehe, null);
        }

        // merken, womit aufgerufen wurde
        oldDance = isDancing;
    }

    private void evalSound() {
        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 4.99);
            zwzfz += 49;

            mainFrame.wave.PlayFile("sfx/swino" + (char) zwzfz + ".wav");
        }
    }
}    