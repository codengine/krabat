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

public class PredKorejtow extends Mainanim {
    private final GenericImage[] pred_wait_head;
    private final GenericImage[] pred_wait_body;
    private final GenericImage[] pred_call_head;
    private final GenericImage[] pred_call_body;
    private final GenericImage[] pred_talk_head;
    private final GenericImage[] pred_talk_body;

    public static final int Breite = 112;
    public static final int Hoehe = 194;

    private int WaitHead = 0;
    private int WaitBody = 0;
    private int CallHead = 0;
    private int CallBody = 0;
    private int TalkHead = 0;
    private int TalkBody = 0;

    private int Verhinderwaithead;
    private int Verhinderwaitbody;
    private int Verhindercallhead;
    private int Verhindercallbody;
    private int Verhindercallswitch;
    private int Verhindertalkhead;
    private int Verhindertalkbody;

    private static final int MAX_VERHINDERWAITHEAD = 50;
    private static final int MAX_VERHINDERWAITBODY = 30;
    private static final int MAX_VERHINDERCALLHEAD = 3;
    private static final int MAX_VERHINDERCALLBODY = 10;
    private static final int MAX_VERHINDERCALLSWITCH = 15;
    private static final int MAX_VERHINDERTALKHEAD = 2;
    private static final int MAX_VERHINDERTALKBODY = 10;

    private final GenericPoint posit;

    private static final int BODYOFFSET = 45;

    public PredKorejtow(Start caller, GenericPoint posit) {
        super(caller);

        this.posit = posit;

        pred_wait_head = new GenericImage[3];
        pred_wait_body = new GenericImage[2];
        pred_call_head = new GenericImage[8];
        pred_call_body = new GenericImage[3];
        pred_talk_head = new GenericImage[8];
        pred_talk_body = new GenericImage[3];

        InitImages();

        Verhinderwaithead = MAX_VERHINDERWAITHEAD;
        Verhinderwaitbody = MAX_VERHINDERWAITBODY;
        Verhindercallhead = MAX_VERHINDERCALLHEAD;
        Verhindercallbody = MAX_VERHINDERCALLBODY;
        Verhindercallswitch = MAX_VERHINDERCALLSWITCH;
        Verhindertalkhead = MAX_VERHINDERTALKHEAD;
        Verhindertalkbody = MAX_VERHINDERTALKBODY;
    }

    private void InitImages() {
        pred_wait_head[0] = getPicture("gfx-dd/starewiki/khaendler-h1.gif");
        pred_wait_head[1] = getPicture("gfx-dd/starewiki/khaendler-h1a.gif");
        pred_wait_head[2] = getPicture("gfx-dd/starewiki/khaendler-h2.gif");

        pred_wait_body[0] = getPicture("gfx-dd/starewiki/khaendler-b1.gif");
        pred_wait_body[1] = getPicture("gfx-dd/starewiki/khaendler-b4.gif");

        pred_call_head[0] = pred_wait_head[0];
        pred_call_head[1] = getPicture("gfx-dd/starewiki/khaendler-h5.gif");
        pred_call_head[2] = getPicture("gfx-dd/starewiki/khaendler-h8.gif");
        pred_call_head[3] = getPicture("gfx-dd/starewiki/khaendler-h9.gif");
        pred_call_head[4] = pred_wait_head[2];
        pred_call_head[5] = getPicture("gfx-dd/starewiki/khaendler-r1.gif");
        pred_call_head[6] = getPicture("gfx-dd/starewiki/khaendler-r2.gif");
        pred_call_head[7] = getPicture("gfx-dd/starewiki/khaendler-r3.gif");

        pred_call_body[0] = getPicture("gfx-dd/starewiki/khaendler-b2.gif");
        pred_call_body[1] = getPicture("gfx-dd/starewiki/khaendler-b3.gif");
        pred_call_body[2] = pred_wait_body[1];

        pred_talk_head[0] = pred_wait_head[0];
        pred_talk_head[1] = getPicture("gfx-dd/starewiki/khaendler-h3.gif");
        pred_talk_head[2] = getPicture("gfx-dd/starewiki/khaendler-h4.gif");
        pred_talk_head[3] = pred_call_head[1];
        pred_talk_head[4] = getPicture("gfx-dd/starewiki/khaendler-h6.gif");
        pred_talk_head[5] = getPicture("gfx-dd/starewiki/khaendler-h7.gif");
        pred_talk_head[6] = pred_call_head[2];
        pred_talk_head[7] = pred_call_head[3];

        pred_talk_body[0] = pred_call_body[0];
        pred_talk_body[1] = pred_wait_body[1];
        pred_talk_body[2] = getPicture("gfx-dd/starewiki/khaendler-b5.gif");
    }

    // Predawar beim rumstehen
    public void drawPredawar(GenericDrawingContext g) {
        // Head switchen, wenn es soweit ist
        if ((--Verhinderwaithead) < 1) {
            Verhinderwaithead = MAX_VERHINDERWAITHEAD;

            int zuffi = (int) (Math.random() * 50);
            if (zuffi < 25) {
                WaitHead = 0;
            } else {
                WaitHead = 2;
            }
        } else {
            // wenn er nach vorn schaut, dann darf er zwinkern
            if (WaitHead == 1) {
                WaitHead = 0;
            } else {
                int zf = (int) (Math.random() * 50);
                if (zf > 45) {
                    WaitHead = 1;
                }
            }
        }

        // Body switchen ohne Extrawurst
        if ((--Verhinderwaitbody) < 1) {
            Verhinderwaitbody = MAX_VERHINDERWAITBODY;
            WaitBody = (int) (Math.random() * 1.9);
        }

        // Predawar malen
        g.drawImage(pred_wait_head[WaitHead], posit.x, posit.y, null);
        g.drawImage(pred_wait_body[WaitBody], posit.x, posit.y + BODYOFFSET, null);
    }

    // Predawar beim Rufen
    public void callPredawar(GenericDrawingContext g) {
        // Sperre fuer Kopf wenden runterzaehlen
        if (Verhindercallswitch > 0) {
            --Verhindercallswitch;
        }

        // Head eval.
        if ((--Verhindercallhead) < 1) {
            Verhindercallhead = MAX_VERHINDERCALLHEAD;

            // schauen, ob auch Richtung gewechselt werden darf
            if (Verhindercallswitch < 1) {
                Verhindercallswitch = MAX_VERHINDERCALLSWITCH;
                CallHead = (int) (Math.random() * 7.9);
            } else {
                if (CallHead < 4) {
                    CallHead = (int) (Math.random() * 3.9);
                } else {
                    CallHead = (int) ((Math.random() * 3.9) + 4);
                }
            }
        }

        // Body eval.
        if ((--Verhindercallbody) < 1) {
            Verhindercallbody = MAX_VERHINDERCALLBODY;
            CallBody = (int) (Math.random() * 2.9);
        }

        // Predawar zeichnen
        g.drawImage(pred_call_head[CallHead], posit.x, posit.y, null);
        g.drawImage(pred_call_body[CallBody], posit.x, posit.y + BODYOFFSET, null);
    }

    // Predawar beim Reden
    public void talkPredawar(GenericDrawingContext g) {
        // Head eval.
        if ((--Verhindertalkhead) < 1) {
            Verhindertalkhead = MAX_VERHINDERTALKHEAD;
            TalkHead = (int) (Math.random() * 7.9);
        }

        // Body eval.
        if ((--Verhindertalkbody) < 1) {
            Verhindertalkbody = MAX_VERHINDERTALKBODY;
            TalkBody = (int) (Math.random() * 2.9);
        }

        // Predawar zeichnen
        g.drawImage(pred_talk_head[TalkHead], posit.x, posit.y, null);
        g.drawImage(pred_talk_body[TalkBody], posit.x, posit.y + BODYOFFSET, null);
    }
}    