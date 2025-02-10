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

public class Swinjo3 extends Mainanim {
    private final GenericImage[] swinjo_look;
    private final GenericImage[] swinjo_dance;

    private int Look = 0;
    private int Dance = 1;

    private int Verhinderlook;
    private int Verhinderdance;

    private static final int MAX_VERHINDERLOOK = 12;
    private static final int MAX_VERHINDERDANCE = 3;

    private static final int untenBreite = 64;
    private static final int untenHoehe = 56;

    private static final int obenBreite = 64;
    private static final int obenHoehe = 65;

    private boolean oldDance = false;

    private final GenericPoint Posit;

    public Swinjo3(Start caller, GenericPoint Posit) {
        super(caller);

        swinjo_look = new GenericImage[5];
        swinjo_dance = new GenericImage[4];

        InitImages();

        Verhinderlook = MAX_VERHINDERLOOK;
        Verhinderdance = MAX_VERHINDERDANCE;

        this.Posit = Posit;
    }

    private void InitImages() {
        swinjo_look[0] = getPicture("gfx/most/swino3.gif");
        swinjo_look[1] = getPicture("gfx/most/swino3a.gif");
        swinjo_look[2] = getPicture("gfx/most/swino3b.gif");
        swinjo_look[3] = getPicture("gfx/most/swino3c.gif");
        swinjo_look[4] = getPicture("gfx/most/swino3d.gif");

        swinjo_dance[0] = getPicture("gfx/most/swino3-d0.gif");
        swinjo_dance[1] = getPicture("gfx/most/swino3-d1.gif");
        swinjo_dance[2] = getPicture("gfx/most/swino3-d2.gif");
        swinjo_dance[3] = getPicture("gfx/most/swino3-d3.gif");
    }

    // gibt Rectangle zurueck, wo sich Schwein drin befindet
    public GenericRectangle swinoRect() {
        return new GenericRectangle(Posit.x - untenBreite / 2, Posit.y - untenHoehe, untenBreite, untenHoehe);
    }

    // Zeichne Schwein, wie es dasteht oder tanzt
    public void drawSwino(GenericDrawingContext offGraph, boolean isDancing, boolean noSound) {
        // 3 Zustaende: Guck, Tanz, Uebergang
        if (!isDancing && !oldDance) {
            // nur Umschauen
            switch (Look) {
                case 0:
                    // verschiedene Varianten eval.
                    if (--Verhinderlook < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        if ((int) (Math.random() * 50) > 25) {
                            Look = 2;
                        } else {
                            Look = 4;
                        }
                    } else if ((int) (Math.random() * 50) > 45) {
                        Look = 1;
                    }
                    break;
                case 1:
                    // Zwinkern rueckgaengig
                    Look = 0;
                    break;
                case 2:
                    // entweder Schleife 3-2-3-2... oder 0
                    if (--Verhinderlook < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        if ((int) (Math.random() * 50) > 25) {
                            Look = 3;
                        } else {
                            Look = 0;
                        }
                    }
                    break;
                case 3:
                    // Schleife mit 2
                    if (--Verhinderlook < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        Look = 2;
                    }
                    break;
                case 4:
                    // zurueck zu 0
                    if (--Verhinderlook < 1) {
                        Verhinderlook = MAX_VERHINDERLOOK;
                        Look = 0;
                    }
                    break;
            }

            // hier darf der Sound rein
            if (!noSound) {
                evalSound();
            }

            // zeichnen
            offGraph.setClip(swinoRect());
            offGraph.drawImage(swinjo_look[Look], Posit.x - untenBreite / 2, Posit.y - untenHoehe, null);
        }

        if (isDancing && oldDance) {
            // rumhuepfen
            if (--Verhinderdance < 1) {
                Verhinderdance = MAX_VERHINDERDANCE;
                Dance++;
                if (Dance == 5) {
                    Dance = 1;
                }
            }

            // umrechnen auf richtige Bildvariable
            int tempDance = Dance;
            if (tempDance == 4) {
                tempDance = 2;
            }

            // zeichnen
            offGraph.setClip(swinoRect());
            offGraph.drawImage(swinjo_dance[tempDance], Posit.x - obenBreite / 2, Posit.y - obenHoehe, null);
        }

        if (isDancing != oldDance) {
            // Uebergangszustand, gleich zeichnen
            offGraph.setClip(swinoRect());
            offGraph.drawImage(swinjo_dance[0], Posit.x - obenBreite / 2, Posit.y - obenHoehe, null);
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