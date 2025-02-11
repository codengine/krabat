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

public class Wudowa extends Mainanim {
    private final GenericImage[] alte_head;
    private final GenericImage[] alte_body;
    private final GenericImage[] alte_lach;

    private GenericImage wokno1;
    private GenericImage wokno2;

    public static final int Breite = 55;
    public static final int Hoehe = 110;

    private int Body = 1;
    private int Lach = 1;
    private int Steh = 1;

    private int Verbody;
    private static final int MAX_VERBODY = 9;

    private static final int zoomHinten = 10;

    private static final int BODYOFFSET = 33;

    public Wudowa(Start caller) {
        super(caller);

        alte_head = new GenericImage[8];
        alte_body = new GenericImage[5];
        alte_lach = new GenericImage[3];

        InitImages();

        Verbody = MAX_VERBODY;
    }

    private void InitImages() {
        alte_head[1] = getPicture("gfx/zdzary/st-k.gif");
        alte_head[2] = getPicture("gfx/zdzary/st-k1.gif");
        alte_head[3] = getPicture("gfx/zdzary/st-k2.gif");
        alte_head[4] = getPicture("gfx/zdzary/st-k3.gif");
        alte_head[5] = getPicture("gfx/zdzary/st-k4.gif");
        alte_head[6] = getPicture("gfx/zdzary/st-k5.gif");
        alte_head[7] = getPicture("gfx/zdzary/st-k6.gif");

        alte_body[1] = getPicture("gfx/zdzary/st-b.gif");
        alte_body[2] = getPicture("gfx/zdzary/st-b2.gif");
        alte_body[3] = getPicture("gfx/zdzary/st-b3.gif");
        alte_body[4] = getPicture("gfx/zdzary/st-b4.gif");

        alte_lach[1] = getPicture("gfx/zdzary/st-l.gif");
        alte_lach[2] = getPicture("gfx/zdzary/st-l2.gif");

        wokno1 = getPicture("gfx/zdzary/wokno1.gif");
        wokno2 = getPicture("gfx/zdzary/wokno2.gif");
    }

    // Zeichne Mutter, wie sie dasteht oder spricht
    public void drawWudowa(GenericDrawingContext g, int TalkPerson, GenericPoint drawPoint, boolean isZoomed) {
        // Zooming-Variablen berechnen, wir wissen ja hier (besser) nicht, ob gezoomt wird (Hallo Compiler!)
        float xGroesse = Breite - (float) zoomHinten / 2;
        float yGroesse = Breite - zoomHinten;

        float fBodyOffset = BODYOFFSET;

        int scaleBODYOFFSET = (int) (fBodyOffset - fBodyOffset / yGroesse * zoomHinten);
        int KoerperGroesse = (int) (yGroesse - scaleBODYOFFSET);

        // Normales Reden wie immer
        if (TalkPerson == 56 && mainFrame.talkCount > 1) {
            int head = (int) Math.round(Math.random() * 6);
            head += 2;
            if (head == 8) {
                head = 2;
            }

            if (--Verbody < 1) {
                Verbody = MAX_VERBODY;
                Body = (int) Math.round(Math.random() * 4);
                Body++;
                if (Body == 5) {
                    Body = 1;
                }
            }

            if (!isZoomed)  // ungezoomte Variante
            {
                g.drawImage(alte_head[head], drawPoint.x, drawPoint.y);
                g.drawImage(alte_body[Body], drawPoint.x, drawPoint.y + BODYOFFSET);
            } else {
                g.drawImage(alte_head[head], drawPoint.x, drawPoint.y, (int) xGroesse, scaleBODYOFFSET);
                g.drawImage(alte_body[Body], drawPoint.x, drawPoint.y + scaleBODYOFFSET, (int) xGroesse, KoerperGroesse);
            }
            drawVorder(g);
        } else {
            // Lachen der Alten
            if (TalkPerson == 57 && mainFrame.talkCount > 1) {
                Lach++;
                if (Lach == 3) {
                    Lach = 1;
                }

                if (--Verbody < 1) {
                    Verbody = MAX_VERBODY;
                    Body = (int) Math.round(Math.random() * 4);
                    Body++;
                    if (Body == 5) {
                        Body = 1;
                    }
                }

                if (!isZoomed) {
                    g.drawImage(alte_lach[Lach], drawPoint.x, drawPoint.y);
                    g.drawImage(alte_body[Body], drawPoint.x, drawPoint.y + BODYOFFSET);
                } else {
                    g.drawImage(alte_lach[Lach], drawPoint.x, drawPoint.y, (int) xGroesse, scaleBODYOFFSET);
                    g.drawImage(alte_body[Body], drawPoint.x, drawPoint.y + scaleBODYOFFSET, (int) xGroesse, KoerperGroesse);
                }
                drawVorder(g);
            }

            // Normales Dastehen mit Zwinkern ab und zu
            else {
                if (Steh == 2) {
                    Steh = 1;
                } else {
                    int zuffi = (int) Math.round(Math.random() * 50);
                    if (zuffi > 45) {
                        Steh = 2;
                    }
                }

                if (!isZoomed) {
                    g.drawImage(alte_head[Steh], drawPoint.x, drawPoint.y);
                    g.drawImage(alte_body[1], drawPoint.x, drawPoint.y + BODYOFFSET);
                } else {
                    g.drawImage(alte_head[Steh], drawPoint.x, drawPoint.y, (int) xGroesse, scaleBODYOFFSET);
                    g.drawImage(alte_body[1], drawPoint.x, drawPoint.y + scaleBODYOFFSET, (int) xGroesse, KoerperGroesse);
                }
                drawVorder(g);
            }
        }
    }

    private void drawVorder(GenericDrawingContext g) {
        g.drawImage(wokno1, 176, 295);
        g.drawImage(wokno2, 531, 218);
    }
}    