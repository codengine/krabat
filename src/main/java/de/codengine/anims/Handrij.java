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

public class Handrij extends Mainanim {
    private final GenericImage[] hand_work;
    private final GenericImage[] hand_down;
    private final GenericImage[] hand_head;
    private final GenericImage[] hand_body;

    public static final int Breite = 97;
    public static final int Hoehe = 165;

    private int Work = 0;
    private int Down = 0;
    private int Head = 0;
    private int Body = 0;

    private int Verhinderwork;
    private int Verhinderhead;
    private int Verhinderbody;

    private static final int MAX_VERHINDERWORK = 7;
    private static final int MAX_VERHINDERHEAD = 2;
    private static final int MAX_VERHINDERBODY = 10;

    private static final int BODYOFFSET = 48;

    private static final GenericPoint workpos = new GenericPoint(195, 175);
    private static final GenericPoint downpos = new GenericPoint(194, 174);
    private static final GenericPoint talkpos = new GenericPoint(208, 174);

    private boolean hatSchonGeredet = false; // Flag, ob er nach vorn oder nach hinten schauen soll

    public Handrij(Start caller) {
        super(caller);

        hand_work = new GenericImage[7];
        hand_down = new GenericImage[2];
        hand_head = new GenericImage[8];
        hand_body = new GenericImage[5];

        InitImages();

        Verhinderwork = MAX_VERHINDERWORK;
        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    private void InitImages() {
        hand_work[0] = getPicture("gfx-dd/zahrod/zahrod-o.gif");
        hand_work[1] = getPicture("gfx-dd/zahrod/zahrod-o1.gif");
        hand_work[2] = getPicture("gfx-dd/zahrod/zahrod-o2.gif");
        hand_work[3] = getPicture("gfx-dd/zahrod/zahrod-o3.gif");
        hand_work[4] = getPicture("gfx-dd/zahrod/zahrod-o4.gif");
        hand_work[5] = getPicture("gfx-dd/zahrod/zahrod-o5.gif");
        hand_work[6] = getPicture("gfx-dd/zahrod/zahrod-o6.gif");

        hand_down[0] = getPicture("gfx-dd/zahrod/zahrod-u.gif");
        hand_down[1] = getPicture("gfx-dd/zahrod/zahrod-ua.gif");

        hand_head[0] = getPicture("gfx-dd/zahrod/zahrod-rh1.gif");
        hand_head[1] = getPicture("gfx-dd/zahrod/zahrod-rh1a.gif");
        hand_head[2] = getPicture("gfx-dd/zahrod/zahrod-rh2.gif");
        hand_head[3] = getPicture("gfx-dd/zahrod/zahrod-rh3.gif");
        hand_head[4] = getPicture("gfx-dd/zahrod/zahrod-rh4.gif");
        hand_head[5] = getPicture("gfx-dd/zahrod/zahrod-rh5.gif");
        hand_head[6] = getPicture("gfx-dd/zahrod/zahrod-rh6.gif");
        hand_head[7] = getPicture("gfx-dd/zahrod/zahrod-rh7.gif");

        hand_body[0] = getPicture("gfx-dd/zahrod/zahrod-rb1.gif");
        hand_body[1] = getPicture("gfx-dd/zahrod/zahrod-rb2.gif");
        hand_body[2] = getPicture("gfx-dd/zahrod/zahrod-rb3.gif");
        hand_body[3] = getPicture("gfx-dd/zahrod/zahrod-rb4.gif");
        hand_body[4] = getPicture("gfx-dd/zahrod/zahrod-rb5.gif");
    }

    @Override
    public void cleanup() {
        hand_work[0] = null;
        hand_work[1] = null;
        hand_work[2] = null;
        hand_work[3] = null;
        hand_work[4] = null;
        hand_work[5] = null;
        hand_work[6] = null;

        hand_down[0] = null;
        hand_down[1] = null;

        hand_head[0] = null;
        hand_head[1] = null;
        hand_head[2] = null;
        hand_head[3] = null;
        hand_head[4] = null;
        hand_head[5] = null;
        hand_head[6] = null;
        hand_head[7] = null;

        hand_body[0] = null;
        hand_body[1] = null;
        hand_body[2] = null;
        hand_body[3] = null;
        hand_body[4] = null;
    }

    // Zeichne Zahrodnik, wie er dasteht oder spricht
    public void drawHandrij(GenericDrawingContext offGraph, int TalkPerson, boolean isListening, boolean stelltErlaubnisAus,
                            boolean gibtErlaubnis) {
        // Zahrodnik redet
        if ((TalkPerson == 50) && (mainFrame.talkCount > 1)) {
            // Flag fuer nachher setzen
            if (!hatSchonGeredet) {
                hatSchonGeredet = true;
            }

            // Head eval.
            if ((--Verhinderhead) < 1) {
                Verhinderhead = MAX_VERHINDERHEAD;
                Head = (int) (Math.random() * 7.9);
            }

            // Body eval.
            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;
                Body = (int) (Math.random() * 3.9);
            }

            offGraph.drawImage(hand_head[Head], talkpos.x, talkpos.y, null);
            offGraph.drawImage(hand_body[Body], talkpos.x, talkpos.y + BODYOFFSET, null);

            return;
        }

        // schreibt gerade ein Stueck Papier
        if (stelltErlaubnisAus) {
            // wie Arbeiten, nur nicht alle Images
            if ((--Verhinderwork) < 1) {
                Verhinderwork = MAX_VERHINDERWORK;
                Work = (int) ((Math.random() * 3.9) + 1);
            }

            offGraph.drawImage(hand_work[Work], workpos.x, workpos.y, null);

            return;
        }

        // Gibt Erlaubnis an Krabat
        if (gibtErlaubnis) {
            offGraph.drawImage(hand_head[0], talkpos.x, talkpos.y, null);
            offGraph.drawImage(hand_body[4], talkpos.x, talkpos.y + BODYOFFSET, null);
            return;
        }

        // Zahrod hoert zu, ist also nach links gedreht
        if (isListening) {
            // Flag fuer nachher setzen
            if (!hatSchonGeredet) {
                hatSchonGeredet = true;
            }

            if (Head > 0) {
                Head = 0;
            } else {
                int zuf = (int) (Math.random() * 50);
                if (zuf > 45) {
                    Head = 1;
                }
            }

            offGraph.drawImage(hand_head[Head], talkpos.x, talkpos.y, null);
            offGraph.drawImage(hand_body[0], talkpos.x, talkpos.y + BODYOFFSET, null);

            return;
        }

        // nix von alledem, also entweder arbeiten oder nach vorn schauen
        if (hatSchonGeredet) {
            // schaut nach vorn
            if (Down == 1) {
                Down = 0;
            } else {
                int zf = (int) (Math.random() * 50);
                if (zf > 45) {
                    Down = 1;
                }
            }

            offGraph.drawImage(hand_down[Down], downpos.x, downpos.y, null);
        } else {
            // arbeitet nach hinten
            if ((--Verhinderwork) < 1) {
                Verhinderwork = MAX_VERHINDERWORK;
                Work = (int) (Math.random() * 6.9);
            }

            offGraph.drawImage(hand_work[Work], workpos.x, workpos.y, null);
        }
    }
}    