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

public class DDKowar extends Mainanim {
    private final GenericImage[] kowar_work_head;
    private final GenericImage[] kowar_work_body;

    private final GenericImage[] kowar_talk_head;
    private final GenericImage[] kowar_talk_body;

    public static final int Breite = 94;
    public static final int Hoehe = 175;

    private int Verhinderworkhead;
    private int Verhinderworkbody;
    private int Verhindertalkhead;
    private int Verhindertalkbody;

    private static final int MAX_VERHINDERWORKHEAD = 15;
    private static final int MAX_VERHINDERWORKBODY = 13;
    private static final int MAX_VERHINDERTALKHEAD = 2;
    private static final int MAX_VERHINDERTALKBODY = 10;

    private int Workhead = 0;
    private int Workbody = 0;
    private int Talkhead = 0;
    private int Talkbody = 0;
    private int Zwinker = 0;

    private static final int WORKBODYOFFSET = 39;
    private static final int TALKBODYOFFSET = 47;

    private static final int XTALKVERSCHIEB = -13;
    private static final int YTALKVERSCHIEB = 0;

    // Konstruktor
    public DDKowar(Start caller) {
        super(caller);

        kowar_work_head = new GenericImage[3];
        kowar_work_body = new GenericImage[5];

        kowar_talk_head = new GenericImage[8];
        kowar_talk_body = new GenericImage[3];

        InitImages();

        Verhinderworkhead = MAX_VERHINDERWORKHEAD;
        Verhinderworkbody = MAX_VERHINDERWORKBODY;
        Verhindertalkhead = MAX_VERHINDERTALKHEAD;
        Verhindertalkbody = MAX_VERHINDERTALKBODY;
    }

    // Bilder laden
    private void InitImages() {
        kowar_work_head[0] = getPicture("gfx-dd/terassa/schmied-wh1.gif");
        kowar_work_head[1] = getPicture("gfx-dd/terassa/schmied-wh2.gif");
        kowar_work_head[2] = getPicture("gfx-dd/terassa/schmied-wh3.gif");

        kowar_work_body[0] = getPicture("gfx-dd/terassa/schmied-wb1.gif");
        kowar_work_body[1] = getPicture("gfx-dd/terassa/schmied-wb2.gif");
        kowar_work_body[2] = getPicture("gfx-dd/terassa/schmied-wb3.gif");
        kowar_work_body[3] = getPicture("gfx-dd/terassa/schmied-wb4.gif");
        kowar_work_body[4] = getPicture("gfx-dd/terassa/schmied-wb5.gif");

        kowar_talk_head[0] = getPicture("gfx-dd/terassa/schmied-th1.gif");
        kowar_talk_head[1] = getPicture("gfx-dd/terassa/schmied-th1a.gif");
        kowar_talk_head[2] = getPicture("gfx-dd/terassa/schmied-th2.gif");
        kowar_talk_head[3] = getPicture("gfx-dd/terassa/schmied-th3.gif");
        kowar_talk_head[4] = getPicture("gfx-dd/terassa/schmied-th4.gif");
        kowar_talk_head[5] = getPicture("gfx-dd/terassa/schmied-th5.gif");
        kowar_talk_head[6] = getPicture("gfx-dd/terassa/schmied-th6.gif");
        kowar_talk_head[7] = getPicture("gfx-dd/terassa/schmied-th7.gif");

        kowar_talk_body[0] = getPicture("gfx-dd/terassa/schmied-tb1.gif");
        kowar_talk_body[1] = getPicture("gfx-dd/terassa/schmied-tb2.gif");
        kowar_talk_body[2] = getPicture("gfx-dd/terassa/schmied-tb3.gif");
    }

    @Override
    public void cleanup() {
        kowar_work_head[0] = null;
        kowar_work_head[1] = null;
        kowar_work_head[2] = null;

        kowar_work_body[0] = null;
        kowar_work_body[1] = null;
        kowar_work_body[2] = null;
        kowar_work_body[3] = null;
        kowar_work_body[4] = null;

        kowar_talk_head[0] = null;
        kowar_talk_head[1] = null;
        kowar_talk_head[2] = null;
        kowar_talk_head[3] = null;
        kowar_talk_head[4] = null;
        kowar_talk_head[5] = null;
        kowar_talk_head[6] = null;
        kowar_talk_head[7] = null;

        kowar_talk_body[0] = null;
        kowar_talk_body[1] = null;
        kowar_talk_body[2] = null;
    }

    // Zeichne Schmied, wie er dasteht oder spricht
    public void drawDDkowar(GenericDrawingContext g, int TalkPerson, GenericPoint Posit, boolean isListening) {
        // Schmied beim Reden
        if (((TalkPerson == 45) && (mainFrame.talkCount > 1)) || (isListening)) {
            // Head evaluieren
            if ((--Verhindertalkhead) < 1) {
                Verhindertalkhead = MAX_VERHINDERTALKHEAD;
                Talkhead = (int) (Math.random() * 7.9);
            }

            // Body evaluieren
            if ((--Verhindertalkbody) < 1) {
                Verhindertalkbody = MAX_VERHINDERTALKBODY;
                Talkbody = (int) (Math.random() * 2.9);
            }

            // alles falsch, wenn er nur zuhoeren soll
            if ((isListening) && (TalkPerson != 45)) {
                Talkbody = 0;
                Talkhead = Zwinker;

                // Zwinkern auch evaluieren
                if (Zwinker == 1) {
                    Zwinker = 0;
                } else {
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 45) {
                        Zwinker = 1;
                    }
                }
            }

            // Kowar zeichnen
            g.drawImage(kowar_talk_head[Talkhead], Posit.x + XTALKVERSCHIEB,
                    Posit.y + YTALKVERSCHIEB, null);
            g.drawImage(kowar_talk_body[Talkbody], Posit.x + XTALKVERSCHIEB,
                    Posit.y + TALKBODYOFFSET + YTALKVERSCHIEB, null);
        }

        // Schmied beim Arbeiten
        else {
            // Head evaluieren
            if ((--Verhinderworkhead) < 1) {
                Verhinderworkhead = MAX_VERHINDERWORKHEAD;
                Workhead = (int) (Math.random() * 2.9);
            }

            // Body evaluieren
            if ((--Verhinderworkbody) < 1) {
                Verhinderworkbody = MAX_VERHINDERWORKBODY;
                Workbody = (int) (Math.random() * 4.9);
            }

            // Kowar zeichnen
            g.drawImage(kowar_work_head[Workhead], Posit.x, Posit.y, null);
            g.drawImage(kowar_work_body[Workbody], Posit.x, Posit.y + WORKBODYOFFSET, null);
        }
    }
}    