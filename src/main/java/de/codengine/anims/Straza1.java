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

public class Straza1 extends Mainanim {
    private final GenericImage[] straza_stand;
    private final GenericImage[] straza_talk;

    public static final int Breite = 100;
    public static final int Hoehe = 135;

    private int Talk = 0;
    private int Stand = 0;

    private int Verhindertalk;

    private static final int MAX_VERHINDERTALK = 2;

    public Straza1(Start caller) {
        super(caller);

        straza_stand = new GenericImage[4];
        straza_talk = new GenericImage[3];

        InitImages();

        Verhindertalk = MAX_VERHINDERTALK;
    }

    private void InitImages() {
        straza_stand[0] = getPicture("gfx-dd/straze/straza1.gif");
        straza_stand[1] = getPicture("gfx-dd/straze/straza1a.gif");
        straza_stand[2] = getPicture("gfx-dd/straze/straza1b.gif");
        straza_stand[3] = getPicture("gfx-dd/straze/straza1c.gif");

        straza_talk[0] = getPicture("gfx-dd/straze/straza1-t1.gif");
        straza_talk[1] = getPicture("gfx-dd/straze/straza1-t2.gif");
        straza_talk[2] = getPicture("gfx-dd/straze/straza1-t3.gif");
    }

    // Zeichne Wache, wie er dasteht oder spricht
    public void drawStraza1(GenericDrawingContext offGraph, int TalkPerson, GenericPoint posit, boolean versperrtWeg) {
        // redende Straza
        if ((TalkPerson == 43) && (mainFrame.talkCount > 1)) {
            // evaluieren
            if ((--Verhindertalk) < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) (Math.random() * 2.9);
            }

            // Straza zeichnen
            offGraph.drawImage(straza_talk[Talk], posit.x, posit.y, null);
        }

        // rumstehende Straza
        else {
            // Zufallszahl fuer Zwinkern
            int zuffi = (int) (Math.random() * 50);

            // hat ausgestreckten Arm
            if (versperrtWeg) {
                // Reset
                if (Stand < 2) {
                    Stand = 2;
                }

                // Zwinkern eval.
                if (Stand == 3) {
                    Stand = 2;
                } else if (zuffi > 45) {
                    Stand = 3;
                }
            }
            // normal
            else {
                // Reset
                if (Stand > 1) {
                    Stand = 0;
                }

                // Zwinkern eval.
                if (Stand == 1) {
                    Stand = 0;
                } else if (zuffi > 45) {
                    Stand = 1;
                }
            }

            // zeichnen
            offGraph.drawImage(straza_stand[Stand], posit.x, posit.y, null);
        }
    }
}    