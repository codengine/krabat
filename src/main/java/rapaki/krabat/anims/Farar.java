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
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class Farar extends Mainanim {
    private GenericImage[] farar_head;
    private GenericImage[] farar_body;

    public static final int Breite = 94;
    public static final int Hoehe = 207;

    private int Head = 1;
    private int Body = 1;

    private int Nobody;
    private static final int MAX_NOBODY = 8;

    private int Nohead;
    private static final int MAX_NOHEAD = 2;

    public Farar(Start caller) {
        super(caller);

        farar_head = new GenericImage[9];
        farar_body = new GenericImage[5];

        InitImages();

        Nobody = MAX_NOBODY;
        Nohead = MAX_NOHEAD;
    }

    private void InitImages() {
        farar_head[1] = getPicture("gfx/pinca/f-h-1.gif");
        farar_head[2] = getPicture("gfx/pinca/f-h-2.gif");
        farar_head[3] = getPicture("gfx/pinca/f-h-3.gif");
        farar_head[4] = getPicture("gfx/pinca/f-h-4.gif");
        farar_head[5] = getPicture("gfx/pinca/f-h-5.gif");
        farar_head[6] = getPicture("gfx/pinca/f-h-6.gif");
        farar_head[7] = getPicture("gfx/pinca/f-h-7.gif");
        farar_head[8] = getPicture("gfx/pinca/f-h-8.gif");

        farar_body[1] = getPicture("gfx/pinca/f-b-1.gif");
        farar_body[2] = getPicture("gfx/pinca/f-b-2.gif");
        farar_body[3] = getPicture("gfx/pinca/f-b-3.gif");
        farar_body[4] = getPicture("gfx/pinca/f-b-4.gif");
    }

    public void cleanup() {
        farar_head[1] = null;
        farar_head[2] = null;
        farar_head[3] = null;
        farar_head[4] = null;
        farar_head[5] = null;
        farar_head[6] = null;
        farar_head[7] = null;
        farar_head[8] = null;

        farar_body[1] = null;
        farar_body[2] = null;
        farar_body[3] = null;
        farar_body[4] = null;
    }

    // Zeichne Pfarrer, wie er dasteht oder spricht
    public void drawFarar(GenericDrawingContext offGraph, int TalkPerson, GenericPoint pos) {
        // Redender Pfarrer
        if ((TalkPerson == 37) && (mainFrame.talkCount > 1)) {
            if ((--Nohead) < 1) {
                Nohead = MAX_NOHEAD;
                Head = (int) Math.round(Math.random() * 8);
                Head++;
                if (Head == 9) {
                    Head = 1;
                }
            }

            if ((--Nobody) < 1) {
                Nobody = MAX_NOBODY;
                Body = (int) Math.round(Math.random() * 4);
                Body++;
                if (Body == 5) {
                    Body = 1;
                }
            }
        }

        // rumstehender Pfarrer (ab und zu Zwinkern)
        else {
            if (Head == 2) {
                Head = 1;
            } else {
                int zuffi = (int) Math.round(Math.random() * 50);
                if (zuffi > 45) {
                    Head = 2;
                }
            }
            Body = 1;
        }

        offGraph.drawImage(farar_head[Head], pos.x, pos.y, null);
        offGraph.drawImage(farar_body[Body], pos.x, pos.y + 50, null);
    }
}    