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

public class BurHanza extends Mainanim {
    private final GenericImage[] bur_work;
    private final GenericImage[] bur_talk;

    public static final int Breite = 53;
    public static final int Hoehe = 54;

    private int Talk = 0;
    private int Work = 0;

    private int Verhindertalk;
    private int Verhinderwork;
    private static final int MAX_VERHINDERTALK = 2;
    private static final int MAX_VERHINDERWORK = 5;

    public BurHanza(Start caller) {
        super(caller);

        bur_work = new GenericImage[4];
        bur_talk = new GenericImage[4];

        InitImages();

        Verhindertalk = MAX_VERHINDERTALK;
        Verhinderwork = MAX_VERHINDERWORK;
    }

    private void InitImages() {
        bur_work[0] = getPicture("gfx/polo/dzona1.gif");
        bur_work[1] = getPicture("gfx/polo/dzona2.gif");
        bur_work[2] = getPicture("gfx/polo/dzona3.gif");
        bur_work[3] = getPicture("gfx/polo/dzona4.gif");

        bur_talk[0] = getPicture("gfx/polo/dzona-r1.gif");
        bur_talk[1] = getPicture("gfx/polo/dzona-r2.gif");
        bur_talk[2] = getPicture("gfx/polo/dzona-r3.gif");
        bur_talk[3] = getPicture("gfx/polo/dzona-r4.gif");
    }

    @Override
    public void cleanup() {
        bur_work[0] = null;
        bur_work[1] = null;
        bur_work[2] = null;
        bur_work[3] = null;

        bur_talk[0] = null;
        bur_talk[1] = null;
        bur_talk[2] = null;
        bur_talk[3] = null;
    }

    // Zeichne Hanza, wie er dasteht oder spricht
    public void drawHanza(GenericDrawingContext offGraph, int TalkPerson, GenericPoint pos) {
        // Zona beim Reden
        if (TalkPerson == 29 && mainFrame.talkCount > 1) {
            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) (Math.random() * 3.9);
            }

            offGraph.drawImage(bur_talk[Talk], pos.x, pos.y);
        } else {
            if (--Verhinderwork < 1) {
                Verhinderwork = MAX_VERHINDERWORK;

                Work++;
                if (Work == 4) {
                    Work = 0;
                }

                if (Work == 3) {
                    int zf = (int) (Math.random() * 50);
                    if (zf < 40) {
                        Work = 0;
                    } else if (!mainFrame.inventory.noBackgroundSound || !mainFrame.invCursor) {
                        mainFrame.wave.PlayFile("sfx/nepl.wav");
                    }
                }
            }

            offGraph.drawImage(bur_work[Work], pos.x, pos.y);
        }
    }
}    