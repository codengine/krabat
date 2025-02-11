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

public class Kutser extends Mainanim {
    private final GenericImage[] kutser_look;
    private final GenericImage[] kutser_listen;
    private final GenericImage[] kutser_talk;

    public static final int Breite = 65;
    public static final int Hoehe = 81;

    private int Talk = 0;
    private int Listen = 0;
    private int Look = 0;

    private int Verhindertalk;

    private static final int MAX_VERHINDERTALK = 2;

    public Kutser(Start caller) {
        super(caller);

        kutser_look = new GenericImage[2];
        kutser_listen = new GenericImage[2];
        kutser_talk = new GenericImage[7];

        InitImages();

        Verhindertalk = MAX_VERHINDERTALK;
    }

    private void InitImages() {
        kutser_look[0] = getPicture("gfx/most/kutscher.gif");
        kutser_look[1] = getPicture("gfx/most/kutscher-a.gif");

        kutser_listen[0] = getPicture("gfx/most/kutscher2.gif");
        kutser_listen[1] = getPicture("gfx/most/kutscher2a.gif");

        kutser_talk[0] = getPicture("gfx/most/kutscher2t1.gif");
        kutser_talk[1] = getPicture("gfx/most/kutscher2t2.gif");
        kutser_talk[2] = getPicture("gfx/most/kutscher2t3.gif");
        kutser_talk[3] = getPicture("gfx/most/kutscher2t4.gif");
        kutser_talk[4] = getPicture("gfx/most/kutscher2t5.gif");
        kutser_talk[5] = getPicture("gfx/most/kutscher2t6.gif");
        kutser_talk[6] = getPicture("gfx/most/kutscher2t7.gif");
    }

    // Zeichne Dundak, wie er dasteht oder spricht
    public void drawKutser(GenericDrawingContext g, int TalkPerson, GenericPoint Posit, boolean isListening) {
        // beim Reden
        if (TalkPerson == 39 && mainFrame.talkCount > 1) {
            // Head evaluieren
            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) (Math.random() * 6.9);
            }

            g.drawImage(kutser_talk[Talk], Posit.x, Posit.y);
        } else {
            // hier unterscheiden, ob er zuhoert oder nicht
            int zf = (int) (Math.random() * 50);

            if (isListening) {
                // zuhoeren
                if (Listen == 1) {
                    Listen = 0;
                } else if (zf > 45) {
                    Listen = 1;
                }

                g.drawImage(kutser_listen[Listen], Posit.x, Posit.y);
            } else {
                // rumschauen
                if (Look == 1) {
                    Look = 0;
                } else if (zf > 45) {
                    Look = 1;
                }

                g.drawImage(kutser_look[Look], Posit.x, Posit.y);
            }
        }
    }
}    