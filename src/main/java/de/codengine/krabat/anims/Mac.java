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

public class Mac extends Mainanim {
    private final GenericImage[] mac_head_left;
    private final GenericImage[] mac_head_right;
    private final GenericImage[] mac_body_left;
    private final GenericImage[] mac_body_right;
    private final GenericImage[] mac;
    private final GenericImage[] mac_r;


    private final int links;

    public static final int Breite = 46;
    public static final int Hoehe = 85;
    public int Breites;
    public int Hoehes;

    private static final int KOPFY = 30;
    private static final int BODYY = 55;

    private int Kopf = 1;
    private int Body = 1;
    private int Steh = 1;

    private static final int SCALEY = 253;
    private final int Scalex;
    private final int Scaleyh;
    private final int Scaleyb;

    private int Bodycount;
    private static final int MAX_BODYCOUNT = 8;

    private int Verhinderkopf;
    private static final int MAX_VERHINDERKOPF = 2;

    public Mac(Start caller, boolean isLeft) {
        super(caller);

        mac_head_left = new GenericImage[6];
        mac_body_left = new GenericImage[7];
        mac = new GenericImage[3];
        mac_head_right = new GenericImage[6];
        mac_body_right = new GenericImage[3];
        mac_r = new GenericImage[3];

        InitImages();

        if (isLeft) {
            links = 1;
        } else {
            links = 2;
        }
        Bodycount = MAX_BODYCOUNT;

        float sc = SCALEY;
        float ho = Hoehe;

        Scalex = (int) (sc / ho * (float) Breite);
        // Scalex = (SCALEY / Hoehe) * Breite;

        float hy = KOPFY;
        Scaleyh = (int) (sc / ho * hy);

        hy = BODYY;
        Scaleyb = (int) (sc / ho * hy);

        Breites = Scalex;
        Hoehes = SCALEY;
    }

    private void InitImages() {
        mac_head_left[1] = getPicture("gfx/anims/ma-k1.gif");
        mac_head_left[2] = getPicture("gfx/anims/ma-k2.gif");
        mac_head_left[3] = getPicture("gfx/anims/ma-k3.gif");
        mac_head_left[4] = getPicture("gfx/anims/ma-k4.gif");
        mac_head_left[5] = getPicture("gfx/anims/ma-k5.gif");

        mac_head_right[1] = getPicture("gfx/anims/ma-ks1.gif");
        mac_head_right[2] = getPicture("gfx/anims/ma-ks2.gif");
        mac_head_right[3] = getPicture("gfx/anims/ma-ks3.gif");
        mac_head_right[4] = getPicture("gfx/anims/ma-ks4.gif");
        mac_head_right[5] = getPicture("gfx/anims/ma-ks5.gif");

        mac_body_left[1] = getPicture("gfx/anims/ma-b1.gif");
        mac_body_left[2] = getPicture("gfx/anims/ma-b2.gif");
        mac_body_left[3] = getPicture("gfx/anims/ma-b3.gif");
        mac_body_left[4] = getPicture("gfx/anims/ma-b4.gif");
        mac_body_left[5] = getPicture("gfx/anims/ma-b5.gif");
        mac_body_left[6] = getPicture("gfx/anims/ma-b6.gif");

        mac_body_right[1] = getPicture("gfx/anims/macpokaza.gif");
        mac_body_right[2] = getPicture("gfx/anims/ma-3bb.gif");

        mac[1] = getPicture("gfx/anims/ma-1.gif");
        mac[2] = getPicture("gfx/anims/ma-1a.gif");

        mac_r[1] = getPicture("gfx/anims/ma-2.gif");
        mac_r[2] = getPicture("gfx/anims/ma-2a.gif");
    }

    // Zeichne Mutter, wie sie dasteht oder spricht
    public void drawMac(GenericDrawingContext g, GenericPoint Pmac, int TalkPerson) {
        int zuffi;

        // Rede, Mutter !!!!!
        if ((TalkPerson == 20 || TalkPerson == 60) && mainFrame.talkCount > 1) {
            // Kopf evaluieren
            if (--Verhinderkopf < 1) {
                Verhinderkopf = MAX_VERHINDERKOPF;
                zuffi = (int) Math.round(Math.random() * 5);
                zuffi++;
                if (zuffi >= 6) {
                    zuffi = 1;
                }
                Kopf = zuffi;
            }

            // Body evaluieren
            if (--Bodycount < 1) {
                Bodycount = MAX_BODYCOUNT;
                zuffi = (int) Math.round(Math.random() * 6);
                zuffi++;
                if (zuffi < 7) {
                    Body = zuffi;
                }
            }

            // Mutter redet nach links , nur normal
            if (links == 1) {
                g.drawImage(mac_head_left[Kopf], Pmac.x, Pmac.y, null);
                g.drawImage(mac_body_left[Body], Pmac.x, Pmac.y + KOPFY, null);
            }
            // Mutter redet nach rechts oder zeigt dazu (Talkperson = 60)
            else {
                g.drawImage(mac_head_right[Kopf], Pmac.x, Pmac.y, Scalex, Scaleyh, null);
                if (TalkPerson == 60) {
                    g.drawImage(mac_body_right[1], Pmac.x, Pmac.y, Scalex, SCALEY, null);
                } else {
                    g.drawImage(mac_body_right[2], Pmac.x, Pmac.y + Scaleyh, Scalex, Scaleyb, null);
                }
            }
        }
        // Sei still, Mutter !!!!!!!
        else {
            // Stehen und Zwinkern nach links oder rechts
            if (Steh == 2) {
                Steh = 1;
            } else {
                zuffi = (int) Math.round(Math.random() * 50);
                if (zuffi > 45) {
                    Steh = 2;
                }
            }
            if (links == 1) {
                g.drawImage(mac[Steh], Pmac.x, Pmac.y, null);
            } else {
                g.drawImage(mac_r[Steh], Pmac.x, Pmac.y, Scalex, SCALEY, null);
            }
        }
    }
}    