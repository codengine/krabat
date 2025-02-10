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

public class Oldmlynk extends Mainanim {
    private GenericImage[] mlynk_head;
    private GenericImage[] mlynk_body;

    public static final int Breite = 70;
    public static final int Hoehe = 100;

    private int Verhinderkopf;
    private static final int MAX_VERHINDERKOPF = 2;

    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;

    private static final int BODYOFFSET = 30;

    private int Head = 0;
    private int Body = 0;
    // private int Lach = 0;

    private int Scalex;
    private int Scaleyhead;
    private int Scaleybody;

    private boolean muellerHatSchonNachVornGeschaut = false; // hier festlegen, ob der Kpf nicht mehr nach rechts gedreht wird,
    // wenn der Mueller gerade Sendepause hat

    // OldMlynk in Rowy (Teil4) steht nur in linke richtung und blinzelt
    private GenericImage[] mlynk_left;
    private boolean fOnlyStandAndLookLeft = false;
    private int mlnkLeftFrameCounter = 0;


    // Standard-Konstruktor
    public Oldmlynk(Start caller, int scale) {
        super(caller);
        constructMueller(scale);
    }

    // Konstruktor fuer OldMlynk in Rowy (Teil4)
    public Oldmlynk(Start caller, int scale, boolean fLookLeft) {
        super(caller);
        fOnlyStandAndLookLeft = fLookLeft;
        constructMueller(scale);
    }

    // Init-Stuff wurde wegen doppelten Konstruktor ausgelagert (SS).
    public void constructMueller(int scale) {
        mlynk_head = new GenericImage[13];
        mlynk_body = new GenericImage[4];
        mlynk_left = new GenericImage[2];

        InitImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;

        float fWidth = Breite;
        float fHeight = Hoehe;
        float fBodyoffset = BODYOFFSET;
        float scaleHelper = fWidth / fHeight;
        float fScale = scale;
        float xScale = fScale * scaleHelper;
        float scaleHeadAnteil = fScale * (fBodyoffset / fHeight);

        Scalex = (int) (Breite - xScale);
        Scaleyhead = (int) (fBodyoffset - scaleHeadAnteil);
        Scaleybody = (int) (fHeight - fScale - Scaleyhead);
    }

    // uebermittelt Muellerzeichenpunkt an Mlynkmurja
    public GenericPoint getMuellerPoint(GenericPoint feet) {
        return (new GenericPoint(feet.x - (Scalex / 2),
                feet.y - Scaleyhead - Scaleybody));
    }

    // uebermittelt TalkPoint an Mlynkmurja
    public GenericPoint getTalkPoint(GenericPoint feet) {
        return (new GenericPoint(feet.x, feet.y - Scaleyhead - Scaleybody - 50));
    }

    private void InitImages() {
        // Wenn OldMlynk fuer Teil 4 -> nur Grafiken fuer Links-Stehen laden
        if (!fOnlyStandAndLookLeft) {
            mlynk_body[0] = getPicture("gfx-dd/murja/dml-b.gif");
            mlynk_body[1] = getPicture("gfx-dd/murja/dml-b1.gif");
            mlynk_body[2] = getPicture("gfx-dd/murja/dml-b2.gif");
            mlynk_body[3] = getPicture("gfx-dd/murja/dml-b3.gif");

            mlynk_head[0] = getPicture("gfx-dd/murja/dml2-h.gif");
            mlynk_head[1] = getPicture("gfx-dd/murja/dml2-ha.gif");
            mlynk_head[2] = getPicture("gfx-dd/murja/dml2-h1.gif");
            mlynk_head[6] = getPicture("gfx-dd/murja/dml2-h1.gif"); // Achtung, hier falsches Image, wegen Fehlermoeglichkeit -> richtiges GenericImage wird nicht verwendet!!
            mlynk_head[3] = getPicture("gfx-dd/murja/dml2-h3.gif");
            mlynk_head[4] = getPicture("gfx-dd/murja/dml2-h4.gif");
            mlynk_head[5] = getPicture("gfx-dd/murja/dml2-h5.gif");
            mlynk_head[7] = getPicture("gfx-dd/murja/dml2-h6.gif");
            mlynk_head[8] = getPicture("gfx-dd/murja/dml2-h7.gif");
            mlynk_head[9] = getPicture("gfx-dd/murja/dml2-h8.gif");
            mlynk_head[10] = getPicture("gfx-dd/murja/dml2-h9.gif");
            mlynk_head[11] = getPicture("gfx-dd/murja/dml2-h10.gif");
            mlynk_head[12] = getPicture("gfx-dd/murja/dml2-h11.gif");
        } else {
            mlynk_left[0] = getPicture("gfx/wotrow/rdml.gif");
            mlynk_left[1] = getPicture("gfx/wotrow/rdml2.gif");
        }
    }

    // Zeichne Mlynk, wie er dasteht oder spricht
    public void drawOldmlynk(GenericDrawingContext offGraph, int TalkPerson, GenericPoint pos) {
        // folgendes ist nur fuer Teil 4
        // OldMlynk guckt links und blinzelt ab und zu
        if (fOnlyStandAndLookLeft) {
            if ((mlnkLeftFrameCounter++) % 12 != 0) {
                offGraph.drawImage(mlynk_left[0], pos.x, pos.y, Scalex,
                        Scaleyhead + Scaleybody, null);
            } else {
                offGraph.drawImage(mlynk_left[1], pos.x, pos.y, Scalex,
                        Scaleyhead + Scaleybody, null);
            }
            return;
        }

        // ab hier normale Zeichenroutine

        // Mueller redet normal (nach rechts gucken) Talkperson = 36
        if ((TalkPerson == 36) && (mainFrame.talkCount > 1)) {
            // Kopf aussuchen
            if ((--Verhinderkopf) < 1) {
                Verhinderkopf = MAX_VERHINDERKOPF;
                Head = (int) Math.round(Math.random() * 4);
                if (Head == 4) {
                    Head = 0;
                }

                Head += 2;
            }

            // Body aussuchen
            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;
                Body = (int) Math.round(Math.random() * 4);
                if (Body == 4) {
                    Body = 0;
                }
            }
            offGraph.drawImage(mlynk_head[Head], pos.x, pos.y, Scalex,
                    Scaleyhead, null);
            offGraph.drawImage(mlynk_body[Body], pos.x,
                    pos.y + Scaleyhead, Scalex, Scaleybody, null);
            return;
        }

        // Mueller redet nach vorn hin , Talkperson = 66
        if ((TalkPerson == 66) && (mainFrame.talkCount > 1)) {
            // Override ermoeglichen
            muellerHatSchonNachVornGeschaut = true;

            // Kopf aussuchen
            if ((--Verhinderkopf) < 1) {
                Verhinderkopf = MAX_VERHINDERKOPF;
                Head = (int) Math.round(Math.random() * 4);
                if (Head == 4) {
                    Head = 0;
                }

                Head += 8;
            }

            // Body aussuchen
            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;
                Body = (int) Math.round(Math.random() * 4);
                if (Body == 4) {
                    Body = 0;
                }
            }
            offGraph.drawImage(mlynk_head[Head], pos.x, pos.y, Scalex,
                    Scaleyhead, null);
            offGraph.drawImage(mlynk_body[Body], pos.x,
                    pos.y + Scaleyhead, Scalex, Scaleybody, null);
            return;
        }

        // Mueller lacht nach vorn hin , Talkperson = 67
        if ((TalkPerson == 67) && (mainFrame.talkCount > 1)) {
            // Override ermoeglichen
            muellerHatSchonNachVornGeschaut = true;

            // Kopf aussuchen
            Head++;
            if (Head < 7) {
                Head = 7;
            }
            if (Head > 8) {
                Head = 7;
            }

            Body = 0;

            offGraph.drawImage(mlynk_head[Head], pos.x, pos.y, Scalex,
                    Scaleyhead, null);
            offGraph.drawImage(mlynk_body[Body], pos.x,
                    pos.y + Scaleyhead, Scalex, Scaleybody, null);
            return;
        }

        // Wir nehmen mal an, dass er ab hier nicht mehr reden soll (alle anderen Varianten oben abgefangen)
        if (Head > 0) {
            Head = 0;
        } else {
            int zuffi = (int) Math.round(Math.random() * 50);
            if (zuffi > 45) {
                Head = 1;
            }
        }

        if (muellerHatSchonNachVornGeschaut) {
            Head = 7; // hier override, wenn er sich schon nach vorn gedreht hat
        }

        Body = 0;
        offGraph.drawImage(mlynk_head[Head], pos.x, pos.y, Scalex,
                Scaleyhead, null);
        offGraph.drawImage(mlynk_body[Body], pos.x,
                pos.y + Scaleyhead, Scalex, Scaleybody, null);
    }
}