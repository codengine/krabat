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
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Ryby extends Mainanim {
    private final GenericImage[] ryby_left;
    private final GenericImage[] ryby_right;

    private static final Borderrect fisch1Rect = new Borderrect(364, 266, 639, 300);
    // private static final borderrect fisch2Rect = new borderrect (559, 300, 639, 399);
    private static final Borderrect fisch3Rect = new Borderrect(546, 451, 639, 479);

    private int Rybycount = 0;

    private boolean isFischJumping = false;
    private boolean isLeftFish = false;

    private final GenericPoint Position;

    private int Verhinderanim;
    private static final int MAX_VERHINDERANIM = 30;

    private int Verhindercount;
    private static final int MAX_VERHINDERCOUNT = 2;

    public Ryby(Start caller) {
        super(caller);

        ryby_left = new GenericImage[5];
        ryby_right = new GenericImage[5];

        InitImages();

        Position = new GenericPoint();

        Verhinderanim = MAX_VERHINDERANIM;
        Verhindercount = MAX_VERHINDERCOUNT;
    }

    private void InitImages() {
        ryby_left[0] = getPicture("gfx/haty/fisch1.gif");
        ryby_left[1] = getPicture("gfx/haty/fisch2.gif");
        ryby_left[2] = getPicture("gfx/haty/fisch3.gif");
        ryby_left[3] = getPicture("gfx/haty/fisch4.gif");
        ryby_left[4] = getPicture("gfx/haty/fisch5.gif");

        ryby_right[0] = getPicture("gfx/haty/rfisch1.gif");
        ryby_right[1] = getPicture("gfx/haty/rfisch2.gif");
        ryby_right[2] = getPicture("gfx/haty/rfisch3.gif");
        ryby_right[3] = getPicture("gfx/haty/rfisch4.gif");
        ryby_right[4] = getPicture("gfx/haty/rfisch5.gif");
    }

    // Zeichne Hauptwachter, wie er dasteht oder spricht
    public void drawRyby(GenericDrawingContext offGraph, boolean noSound) {
        // zuerst Unterscheidung, ob Fisch schon da ist oder nicht...
        if (isFischJumping) {
            // Fisch weiterschalten, wenn noetig
            if (--Verhindercount < 1) {
                if (Rybycount == 3 && !noSound) {
                    mainFrame.wave.PlayFile("sfx/woda1.wav");
                }

                Verhindercount = MAX_VERHINDERCOUNT;
                Rybycount++;
                if (Rybycount < 5) {
                    // Fisch zeichnen, je nach Richtung
                    if (isLeftFish) {
                        offGraph.drawImage(ryby_left[Rybycount], Position.x, Position.y, null);
                    } else {
                        offGraph.drawImage(ryby_right[Rybycount], Position.x, Position.y, null);
                    }
                } else {
                    // Fisch wieder ausschalten
                    isFischJumping = false;
                }
            } else {
                // wenn warten, dann aktuellen Fisch zeichnen
                // Fisch zeichnen, je nach Richtung
                if (isLeftFish) {
                    offGraph.drawImage(ryby_left[Rybycount], Position.x, Position.y, null);
                } else {
                    offGraph.drawImage(ryby_right[Rybycount], Position.x, Position.y, null);
                }
            }
        } else {
            // berechnen, ob ein Fisch wieder kommen darf
            if (--Verhinderanim < 1) {
                Verhinderanim = MAX_VERHINDERANIM;

                // wahrscheinlichkeit von 75 %
                int zuffi = (int) Math.round(Math.random() * 100);
                if (zuffi < 75) {
                    // entscheiden, ob Fisch nach links oder rechts
                    isLeftFish = zuffi < 37;

                    // zufaelligen Punkt innerhalb der 3 rects ermitteln
                    do {
                        Position.x = (int) (Math.random() * 293.9 + 346);
                        Position.y = (int) (Math.random() * 285.9 + 194);
                    }
                    while (!fisch1Rect.IsPointInRect(Position) && /*(fisch2Rect.IsPointInRect (Position) == false) && */
                            !fisch3Rect.IsPointInRect(Position));

                    Rybycount = 0;
                    isFischJumping = true;
                }
            }
        }
    }
}    