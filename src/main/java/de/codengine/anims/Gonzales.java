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

public class Gonzales extends Mainanim {
    private GenericImage[] pirat_head;
    private GenericImage[] pirat_body;
    private GenericImage pirat_blinker;
    private GenericImage pirat_give;

    private int Head = 1;
    private int Body = 1;
    private int Stand = 1;

    private int VerhinderHead;
    private int VerhinderBody;
    private int VerhinderStand;

    private static final int MAX_VERHINDERHEAD = 2;
    private static final int MAX_VERHINDERBODY = 8;
    private static final int MAX_VERHINDERSTAND = 30;

    public static final int Breite = 22;
    public static final int Hoehe = 45;

    public Gonzales(Start caller) {
        super(caller);

        pirat_head = new GenericImage[5];
        pirat_body = new GenericImage[4];

        InitImages();

        VerhinderHead = MAX_VERHINDERHEAD;
        VerhinderBody = MAX_VERHINDERBODY;
        VerhinderStand = MAX_VERHINDERSTAND;
    }

    private void InitImages() {
        pirat_head[0] = getPicture("gfx-dd/habor/ph1.gif");
        pirat_head[1] = getPicture("gfx-dd/habor/ph3.gif");
        pirat_head[2] = getPicture("gfx-dd/habor/ph4.gif");
        pirat_head[3] = getPicture("gfx-dd/habor/ph5.gif");
        pirat_head[4] = getPicture("gfx-dd/habor/ph6.gif");

        pirat_body[0] = getPicture("gfx-dd/habor/pb1.gif");
        pirat_body[1] = getPicture("gfx-dd/habor/pb3.gif");
        pirat_body[2] = getPicture("gfx-dd/habor/pb4.gif");
        pirat_body[3] = getPicture("gfx-dd/habor/pb5.gif");

        pirat_blinker = getPicture("gfx-dd/habor/ph2.gif");
        pirat_give = getPicture("gfx-dd/habor/pb2.gif");
    }

    @Override
    public void cleanup() {
        pirat_head[0] = null;
        pirat_head[1] = null;
        pirat_head[2] = null;
        pirat_head[3] = null;
        pirat_head[4] = null;

        pirat_body[0] = null;
        pirat_body[1] = null;
        pirat_body[2] = null;
        pirat_body[3] = null;

        pirat_blinker = null;
        pirat_give = null;
    }

    // Zeichne Hauptwachter, wie er dasteht oder spricht
    public void drawGonzales(GenericDrawingContext offGraph, int TalkPerson, GenericPoint Posit, boolean isGiving, boolean isListening) {
        // Pirat beim Reden
        if ((TalkPerson == 61) && (mainFrame.talkCount > 1)) {
            // int nTemp;

            if ((--VerhinderHead) < 1) {
                VerhinderHead = MAX_VERHINDERHEAD;
                Head = (int) Math.round(Math.random() * 3.9);
            }

            if ((--VerhinderBody) < 1) {
                VerhinderBody = MAX_VERHINDERBODY;
                Body = (int) Math.round(Math.random() * 2.9);
            }

            // Steh - Variable overriden
            Stand = 1;
        }

        // Pirat beim Rumstehen
        else {
            Body = 0;

            if ((--VerhinderStand) < 1) {
                VerhinderStand = MAX_VERHINDERSTAND;
                Head = 0;
                int ttpp = (int) Math.round(Math.random() * 50);
                if (ttpp > 25) {
                    Stand = 2;
                } else {
                    Stand = 1;
                }
            }

            // Wenn er bei MC zuhoert, darf er sich nicht wegdrehen
            if (isListening == true) {
                Stand = 1;
            }
        }

        offGraph.drawImage(((Stand == 2) ? pirat_blinker : pirat_head[Head]), Posit.x, Posit.y, null);
        offGraph.drawImage(((isGiving == true) ? pirat_give : pirat_body[Body]), Posit.x, Posit.y + 14, null);
    }
}    