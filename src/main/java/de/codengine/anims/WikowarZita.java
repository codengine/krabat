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

public class WikowarZita extends Mainanim {
    private GenericImage[] wik_head;
    private GenericImage[] wik_body;
    private GenericImage[] wik_wait;
    private GenericImage vorder;

    public static final int Breite = 49;
    public static final int Hoehe = 103;

    private int Head = 0;
    private int Body = 0;
    private int Wait = 0;
    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;
    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;
    private int Verhinderdreh;
    private static final int MAX_VERHINDERDREHWEG = 100;
    private static final int MAX_VERHINDERDREHHIN = 8;

    private static final GenericPoint Pvorder = new GenericPoint(394, 354);

    private static final int BODYOFFSET = 32;

    public WikowarZita(Start caller) {
        super(caller);

        wik_head = new GenericImage[7];
        wik_body = new GenericImage[2];
        wik_wait = new GenericImage[3];

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    private void InitImages() {
        wik_head[0] = getPicture("gfx/kulow/ah-h1.gif");
        wik_head[1] = getPicture("gfx/kulow/ah-h2.gif");
        wik_head[2] = getPicture("gfx/kulow/ah-h3.gif");
        wik_head[3] = getPicture("gfx/kulow/ah-h4.gif");
        wik_head[4] = getPicture("gfx/kulow/ah-h5.gif");
        wik_head[5] = getPicture("gfx/kulow/ah-h6.gif");
        wik_head[6] = getPicture("gfx/kulow/ah-h7.gif");

        wik_body[0] = getPicture("gfx/kulow/ah-b1.gif");
        wik_body[1] = getPicture("gfx/kulow/ah-b2.gif");

        wik_wait[0] = getPicture("gfx/kulow/ah1.gif");
        wik_wait[1] = getPicture("gfx/kulow/ah1a.gif");
        wik_wait[2] = getPicture("gfx/kulow/ah1b.gif");

        vorder = getPicture("gfx/kulow/budka2.gif");
    }

    // Zeichne Mutter, wie sie dasteht oder spricht
    public void drawWikowar(GenericDrawingContext g, GenericPoint pos, int TalkPerson, boolean isListening) {
        // beim Reden
        if ((TalkPerson == 32) && (mainFrame.talkCount > 1)) {
            // Kopf evaluieren
            if ((--Verhinderhead) < 1) {
                Verhinderhead = MAX_VERHINDERHEAD;

                Head = (int) (Math.random() * 6.9);
            }

            // Body evaluieren
            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;

                Body = (int) (Math.random() * 1.9);
            }

            g.drawImage(wik_head[Head], pos.x, pos.y, null);
            g.drawImage(wik_body[Body], pos.x, pos.y + BODYOFFSET, null);
            g.drawImage(vorder, Pvorder.x, Pvorder.y, null);
        } else {
            if (isListening == true) {
                // kein Wegdrehen beim Zuhoeren
                if (Wait != 0) {
                    Wait = 0;
                } else {
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 45) {
                        Wait = 1;
                    }
                }
            } else {
                // darf sich auch wegdrehen
                if ((--Verhinderdreh) < 1) {
                    // darf sich wegdrehen oder ist weggedreht
                    if (Wait == 2) {
                        Wait = 0;
                        Verhinderdreh = MAX_VERHINDERDREHWEG;
                    } else {
                        if ((Wait == 0) || (Wait == 1)) {
                            Wait = 2;
                            Verhinderdreh = MAX_VERHINDERDREHHIN;
                        }
                    }
                } else {
                    if (Wait == 1) {
                        Wait = 0;
                    } else {
                        int zuffi = (int) (Math.random() * 50);
                        if ((zuffi > 45) && (Wait != 2)) {
                            Wait = 1;
                        }
                    }
                }
            }

            g.drawImage(wik_wait[Wait], pos.x, pos.y, null);
            g.drawImage(vorder, Pvorder.x, Pvorder.y, null);
        }
    }
}    