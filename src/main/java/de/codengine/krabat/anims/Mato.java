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

public class Mato extends MainAnim {
    private final GenericImage[] mato_look;
    private final GenericImage[] mato_talk;
    private final GenericImage[] mato_open;
    private GenericImage mato_take;
    private GenericImage vorder;

    public static final int Breite = 54;
    public static final int Hoehe = 46;

    private int Wait = 0;
    private int Talk = 0;
    private int Open = 0;

    private int Verhinderwait;
    private int Verhindertalk;
    private int Verhinderopen;

    private static final int MAX_VERHINDERWAIT = 70;
    private static final int MAX_VERHINDERTALK = 2;
    private static final int MAX_VERHINDEROPEN = 7;

    public Mato(Start caller) {
        super(caller);

        mato_look = new GenericImage[4];
        mato_talk = new GenericImage[7];
        mato_open = new GenericImage[2];

        InitImages();

        Verhinderwait = MAX_VERHINDERWAIT;
        Verhindertalk = MAX_VERHINDERTALK;
        Verhinderopen = MAX_VERHINDEROPEN;
    }

    private void InitImages() {
        mato_look[0] = getPicture("gfx-dd/zastup/schrankier.png");
        mato_look[1] = getPicture("gfx-dd/zastup/schrankier-a.png");
        mato_look[2] = getPicture("gfx-dd/zastup/schrankier2.png");
        mato_look[3] = getPicture("gfx-dd/zastup/schrankier2-a.png");

        mato_talk[0] = mato_look[0];
        mato_talk[1] = getPicture("gfx-dd/zastup/schrankier-r1.png");
        mato_talk[2] = getPicture("gfx-dd/zastup/schrankier-r2.png");
        mato_talk[3] = getPicture("gfx-dd/zastup/schrankier-r3.png");
        mato_talk[4] = getPicture("gfx-dd/zastup/schrankier-r4.png");
        mato_talk[5] = getPicture("gfx-dd/zastup/schrankier-r5.png");
        mato_talk[6] = getPicture("gfx-dd/zastup/schrankier-r6.png");

        mato_open[0] = getPicture("gfx-dd/zastup/schrankier-s1.png");
        mato_open[1] = getPicture("gfx-dd/zastup/schrankier-s2.png");

        mato_take = getPicture("gfx-dd/zastup/schrankier-take.png");

        vorder = getPicture("gfx-dd/zastup/zwokno.png");
    }

    // Zeichne Mato, wie er dasteht oder spricht
    public void drawMato(GenericDrawingContext offGraph, int TalkPerson, GenericPoint Posit, boolean isOpening, boolean isGiving, boolean isListening) {
        // irgendwas entgegennehmen
        if (isGiving) {
            offGraph.drawImage(mato_look[0], Posit.x, Posit.y);
            offGraph.drawImage(vorder, 353, 343);
            offGraph.drawImage(mato_take, Posit.x, Posit.y);
            return;
        }

        // Schranke aufmachen
        if (isOpening) {
            // GenericImage evaluieren
            if (--Verhinderopen < 1) {
                Verhinderopen = MAX_VERHINDEROPEN;
                if (Open == 0) {
                    Open = 1;
                } else {
                    Open = 0;
                }
            }

            offGraph.drawImage(mato_open[Open], Posit.x, Posit.y);
            offGraph.drawImage(vorder, 353, 343);
            return;
        }


        // Mato redet
        if (TalkPerson == 49 && mainFrame.talkCount > 1) {
            // GenericImage evaluieren
            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) (Math.random() * 6.9);
            }

            offGraph.drawImage(mato_talk[Talk], Posit.x, Posit.y);
            offGraph.drawImage(vorder, 353, 343);
        }
        // Mato guckt in der Weltgeschichte rum
        else {
            switch (Wait) {
                case 0: // Zwinkern oder (selten) umschalten
                    if (--Verhinderwait < 1) {
                        // Umschalten
                        Verhinderwait = MAX_VERHINDERWAIT;
                        Wait = 2;
                    } else {
                        // Zwinkern
                        int zuffi = (int) (Math.random() * 50);
                        if (zuffi > 45) {
                            Wait = 1;
                        }
                    }
                    break;
                case 1: // Zwinkern wieder weg
                    Wait = 0;
                    break;
                case 2: // Zwinkern oder (selten) umschalten
                    if (--Verhinderwait < 1) {
                        // Umschalten
                        Verhinderwait = MAX_VERHINDERWAIT;
                        Wait = 0;
                    } else {
                        // Zwinkern
                        int zuffi = (int) (Math.random() * 50);
                        if (zuffi > 45) {
                            Wait = 3;
                        }
                    }
                    break;
                case 3: // Zwinkern wieder weg
                    Wait = 2;
                    break;
            }

            // wenn mit ihm geredet wird, dann nicht wegschauen
            if (isListening && Wait > 1) {
                Wait = 0;
            }

            offGraph.drawImage(mato_look[Wait], Posit.x, Posit.y);
            offGraph.drawImage(vorder, 353, 343);
        }
    }
}    