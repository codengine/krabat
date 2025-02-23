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

public class PredWosuskow extends Mainanim {
    private final GenericImage[] pred_stand;
    private final GenericImage[] pred_call;
    private final GenericImage[] pred_talk;
    private GenericImage pred_stollen;
    private GenericImage pred_take;
    private GenericImage vorder;

    public static final int Breite = 29;
    public static final int Hoehe = 44;

    private int Stand = 0;
    private int Call = 0;
    private int Talk = 0;

    private int Verhinderstand;
    private int Verhindercall;
    private int Verhindertalk;

    private static final int MAX_VERHINDERSTAND = 40;
    private static final int MAX_VERHINDERCALL = 2;
    private static final int MAX_VERHINDERTALK = 2;

    private int Givecounter = 0;
    private int Stollencounter = 0;

    private static final GenericPoint punkt = new GenericPoint(53, 314);
    private static final GenericPoint vorderpunkt = new GenericPoint(47, 303);

    // Konstruktor
    public PredWosuskow(Start caller) {
        super(caller);

        pred_stand = new GenericImage[4];
        pred_call = new GenericImage[5];
        pred_talk = new GenericImage[7];

        InitImages();

        Verhinderstand = MAX_VERHINDERSTAND;
        Verhindercall = MAX_VERHINDERCALL;
        Verhindertalk = MAX_VERHINDERTALK;
    }

    private void InitImages() {
        pred_stand[0] = getPicture("gfx-dd/starewiki/whaendler1.png");
        pred_stand[1] = getPicture("gfx-dd/starewiki/whaendler1a.png");
        pred_stand[2] = getPicture("gfx-dd/starewiki/whaendler1b.png");
        pred_stand[3] = getPicture("gfx-dd/starewiki/whaendler1c.png");

        pred_call[0] = getPicture("gfx-dd/starewiki/whaendler-r1.png");
        pred_call[1] = getPicture("gfx-dd/starewiki/whaendler-r2.png");
        pred_call[2] = getPicture("gfx-dd/starewiki/whaendler-r3.png");
        pred_call[3] = getPicture("gfx-dd/starewiki/whaendler-r4.png");
        pred_call[4] = getPicture("gfx-dd/starewiki/whaendler-r5.png");

        pred_talk[0] = pred_stand[0];
        pred_talk[1] = getPicture("gfx-dd/starewiki/whaendler-t1.png");
        pred_talk[2] = getPicture("gfx-dd/starewiki/whaendler-t2.png");
        pred_talk[3] = getPicture("gfx-dd/starewiki/whaendler-t3.png");
        pred_talk[4] = getPicture("gfx-dd/starewiki/whaendler-t4.png");
        pred_talk[5] = getPicture("gfx-dd/starewiki/whaendler-t5.png");
        pred_talk[6] = getPicture("gfx-dd/starewiki/whaendler-t6.png");

        pred_stollen = getPicture("gfx-dd/starewiki/whaendler2.png");
        pred_take = getPicture("gfx-dd/starewiki/whaendler3.png");

        vorder = getPicture("gfx-dd/starewiki/sbudka.png");
    }

    // PredWosuskow beim Rumstehen
    public void drawPredawar(GenericDrawingContext g) {
        // Wenn der Zeit ist reif, dann mal Gesicht wechseln
        if (--Verhinderstand < 1) {
            Verhinderstand = MAX_VERHINDERSTAND;
            if (Stand > 1) {
                Stand = 0;
            } else {
                Stand = 2;
            }
        } else {
            // Zwinkern eval.
            int zuffi = (int) (Math.random() * 50);

            switch (Stand) {
                case 0:
                    if (zuffi > 45) {
                        Stand = 1;
                    }
                    break;
                case 1:
                    Stand = 0;
                    break;
                case 2:
                    if (zuffi > 45) {
                        Stand = 3;
                    }
                    break;
                case 3:
                    Stand = 2;
                    break;
            }
        }

        // Predawar auch malen
        g.drawImage(pred_stand[Stand], punkt.x, punkt.y);
        g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y);
    }

    // PredWosuskow beim Rumschreien
    public void callPredawar(GenericDrawingContext g) {
        if (--Verhindercall < 1) {
            Verhindercall = MAX_VERHINDERCALL;
            Call = (int) (Math.random() * 4.9);
        }

        g.drawImage(pred_call[Call], punkt.x, punkt.y);
        g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y);
    }

    // PredWosuskow beim Reden
    public void talkPredawar(GenericDrawingContext g) {
        if (--Verhindertalk < 1) {
            Verhindertalk = MAX_VERHINDERTALK;
            Talk = (int) (Math.random() * 6.9);
        }

        g.drawImage(pred_talk[Talk], punkt.x, punkt.y);
        g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y);
    }

    // PredWosuskow bei Anims (selbstkillend)
    public boolean animPredawar(GenericDrawingContext g, int anim) {
        switch (anim) {
            case 1:
                // Stollennimmanim
                Stollencounter++;
                g.drawImage(pred_stollen, punkt.x, punkt.y);
                g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y);
                if (Stollencounter > 5) {
                    Stollencounter = 0;
                    return false;
                } else {
                    return true;
                }

            case 2:
                // Gebeanim
                Givecounter++;
                g.drawImage(pred_take, punkt.x, punkt.y);
                g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y);
                if (Givecounter > 5) {
                    Givecounter = 0;
                    return false;
                } else {
                    return true;
                }
        }

        // Compilerberuhigung
        return false;
    }
}    