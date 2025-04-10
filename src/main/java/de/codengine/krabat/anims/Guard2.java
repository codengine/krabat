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

public class Guard2 extends MainAnim {
    private final GenericImage[] straza_head;
    private final GenericImage[] straza_body;

    public static final int Breite = 73;
    public static final int Hoehe = 135;

    private int Head = 0;
    // private int Body = 0;

    private int Verhinderhead;
    private int Verhinderversperr;

    private static final int MAX_VERHINDERHEAD = 2;
    private static final int MAX_VERHINDERVERSPERR = 10;

    private boolean versperrtWeg;

    private static final int BODYOFFSET = 34;

    public Guard2(Start caller) {
        super(caller);

        straza_head = new GenericImage[9];
        straza_body = new GenericImage[2];

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderversperr = MAX_VERHINDERVERSPERR;
    }

    private void InitImages() {
        straza_head[0] = getPicture("gfx-dd/straze/straza2-h.png");
        straza_head[1] = getPicture("gfx-dd/straze/straza2-ha.png");
        straza_head[2] = getPicture("gfx-dd/straze/straza2-t1.png");
        straza_head[3] = getPicture("gfx-dd/straze/straza2-t2.png");
        straza_head[4] = getPicture("gfx-dd/straze/straza2-t3.png");
        straza_head[5] = getPicture("gfx-dd/straze/straza2-t4.png");
        straza_head[6] = getPicture("gfx-dd/straze/straza2-t5.png");
        straza_head[7] = getPicture("gfx-dd/straze/straza2-t6.png");
        straza_head[8] = getPicture("gfx-dd/straze/straza2-t7.png");

        straza_body[0] = getPicture("gfx-dd/straze/straza2-b1.png");
        straza_body[1] = getPicture("gfx-dd/straze/straza2-b2.png");
    }

    // Zeichne Wache, wie er dasteht oder spricht
    public void drawStraza2(GenericDrawingContext offGraph, int TalkPerson, GenericPoint posit, boolean sperrt) {
        // Weg versperren uebernehmen
        if (sperrt) {
            versperrtWeg = true;
        }

        // redende Straza
        if (TalkPerson == 44 && mainFrame.talkCount > 1) {
            // Head eval.
            if (--Verhinderhead < 1 || Head < 2) {
                Verhinderhead = MAX_VERHINDERHEAD;
                Head = (int) (Math.random() * 6.9 + 2);
            }
        }
        // stehende Straza
        else {
            // Reset
            if (Head > 1) {
                Head = 0;
            }

            // Zwinkern eval.
            if (Head == 1) {
                Head = 0;
            } else {
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Head = 1;
                }
            }
        }

        // Body eval., abh. von versperrtWeg
        if (versperrtWeg) {
            if (--Verhinderversperr < 1) {
                Verhinderversperr = MAX_VERHINDERVERSPERR;
                versperrtWeg = false;
            }
        }

        offGraph.drawImage(straza_head[Head], posit.x, posit.y);
        offGraph.drawImage(straza_body[versperrtWeg ? 1 : 0], posit.x, posit.y + BODYOFFSET);

    }
}    