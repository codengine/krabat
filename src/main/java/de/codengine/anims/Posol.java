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

public class Posol extends Mainanim {
    private final GenericImage[] bote_head;
    private final GenericImage[] bote_body;

    public static final int Breite = 40;
    public static final int Hoehe = 95;

    private int Head = 0;
    private int Body = 0;

    private int Verhinderhead;
    private int Verhinderbody;

    private static final int MAX_VERHINDERHEAD = 2;
    private static final int MAX_VERHINDERBODY = 10;

    private static final int BODYOFFSET = 23;


    public Posol(Start caller) {
        super(caller);

        bote_head = new GenericImage[6];
        bote_body = new GenericImage[2];

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    private void InitImages() {
        bote_head[0] = getPicture("gfx/wjes/bote-h0.gif");
        bote_head[1] = getPicture("gfx/wjes/bote-h1.gif");
        bote_head[2] = getPicture("gfx/wjes/bote-h2.gif");
        bote_head[3] = getPicture("gfx/wjes/bote-h3.gif");
        bote_head[4] = getPicture("gfx/wjes/bote-h4.gif");
        bote_head[5] = getPicture("gfx/wjes/bote-h5.gif");

        bote_body[0] = getPicture("gfx/wjes/bote-b1.gif");
        bote_body[1] = getPicture("gfx/wjes/bote-b2.gif");
    }

    // Zeichne Mutter, wie sie dasteht oder spricht
    public void drawPosol(GenericDrawingContext offGraph, int TalkPerson, GenericPoint posit) {
        if ((TalkPerson == 38) && (mainFrame.talkCount > 1)) {
            // Bote beim Reden

            // Head eval.
            if ((--Verhinderhead) < 1) {
                Verhinderhead = MAX_VERHINDERHEAD;
                Head = (int) (Math.random() * 5.9);
            }

            // Body eval
            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;
                Body = (int) (Math.random() * 1.9);
            }
        } else {
            // Bote beim rumstehen
            Head = 0;
            Body = 0;
        }

        // Boten zeichnen
        offGraph.drawImage(bote_head[Head], posit.x, posit.y, null);
        offGraph.drawImage(bote_body[Body], posit.x, posit.y + BODYOFFSET, null);
    }
}    