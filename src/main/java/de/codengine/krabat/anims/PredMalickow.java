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

public class PredMalickow extends Mainanim {
    private final GenericImage[] pred_stand;
    private final GenericImage[] pred_call;
    private final GenericImage[] pred_talk;
    private GenericImage pred_walkback;
    private GenericImage pred_take;
    private GenericImage vorder;

    public static final int Breite = 15; // um 2 verringert!
    public static final int Hoehe = 18; // um 2 verringert!

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
    private int Walkcounter = 0;

    private static final GenericPoint punkt = new GenericPoint(173, 350); // x+1, y+1 wegen verringerter Breite/Hoehe
    private static final GenericPoint vorderpunkt = new GenericPoint(136, 341);
    private final GenericPoint walkpunkt;
    private boolean back = true;

    public PredMalickow(Start caller) {
        super(caller);

        pred_stand = new GenericImage[4];
        pred_call = new GenericImage[5];
        pred_talk = new GenericImage[7];

        InitImages();

        Verhinderstand = MAX_VERHINDERSTAND;
        Verhindercall = MAX_VERHINDERCALL;
        Verhindertalk = MAX_VERHINDERTALK;

        walkpunkt = new GenericPoint(punkt.x, punkt.y);
    }

    private void InitImages() {
        pred_stand[0] = getPicture("gfx-dd/starewiki/faelscher1.gif");
        pred_stand[1] = getPicture("gfx-dd/starewiki/faelscher1a.gif");
        pred_stand[2] = getPicture("gfx-dd/starewiki/faelscher1b.gif");
        pred_stand[3] = getPicture("gfx-dd/starewiki/faelscher1c.gif");

        pred_call[0] = getPicture("gfx-dd/starewiki/faelscher1-r1.gif");
        pred_call[1] = getPicture("gfx-dd/starewiki/faelscher1-r2.gif");
        pred_call[2] = getPicture("gfx-dd/starewiki/faelscher1-r3.gif");
        pred_call[3] = getPicture("gfx-dd/starewiki/faelscher1-r4.gif");
        pred_call[4] = getPicture("gfx-dd/starewiki/faelscher1-r5.gif");

        pred_talk[0] = pred_stand[0];
        pred_talk[1] = getPicture("gfx-dd/starewiki/faelscher1-t1.gif");
        pred_talk[2] = getPicture("gfx-dd/starewiki/faelscher1-t2.gif");
        pred_talk[3] = getPicture("gfx-dd/starewiki/faelscher1-t3.gif");
        pred_talk[4] = getPicture("gfx-dd/starewiki/faelscher1-t4.gif");
        pred_talk[5] = getPicture("gfx-dd/starewiki/faelscher1-t5.gif");
        pred_talk[6] = getPicture("gfx-dd/starewiki/faelscher1-t6.gif");

        pred_walkback = getPicture("gfx-dd/starewiki/faelscher2.gif");
        pred_take = getPicture("gfx-dd/starewiki/faelscher1g.gif");

        vorder = getPicture("gfx-dd/starewiki/sbudka2.gif");
    }

    // PredMalickow beim Rumstehen
    public void drawPredawar(GenericDrawingContext g, boolean isListening) {
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

        // bei "isListening" darf er nur nach vorn schauen
        if (isListening && Stand > 1) {
            Stand = 0;
        }

        // Predawar auch malen
        g.drawImage(pred_stand[Stand], punkt.x, punkt.y, Breite, Hoehe, null);
        g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y, null);
    }

    // PredMalickow beim Rumschreien
    public void callPredawar(GenericDrawingContext g) {
        if (--Verhindercall < 1) {
            Verhindercall = MAX_VERHINDERCALL;
            Call = (int) (Math.random() * 4.9);
        }

        g.drawImage(pred_call[Call], punkt.x, punkt.y, Breite, Hoehe, null);
        g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y, null);
    }

    // Predmalickow beim Reden
    public void talkPredawar(GenericDrawingContext g) {
        if (--Verhindertalk < 1) {
            Verhindertalk = MAX_VERHINDERTALK;
            Talk = (int) (Math.random() * 6.9);
        }

        g.drawImage(pred_talk[Talk], punkt.x, punkt.y, Breite, Hoehe, null);
        g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y, null);
    }

    // PredMalickow bei Anims (selbstkillend)
    public boolean animPredawar(GenericDrawingContext g, int anim) {
        switch (anim) {
            case 1:
                // Laufanim
                // wohin laufen
                //noinspection IfStatementWithIdenticalBranches
                if (back) {
                    // PredMalickow bewegen
                    Walkcounter++;
                    walkpunkt.x -= 2;
                    if (Walkcounter % 2 == 1) {
                        walkpunkt.y -= 1;
                    } else {
                        walkpunkt.y += 1;
                    }

                    // PredMalickow zeichnen
                    g.drawImage(pred_walkback, walkpunkt.x, walkpunkt.y, Breite, Hoehe, null);
                    g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y, null);

                    // Auf Ende der Anim testen
                    if (Walkcounter == 20) {
                        Walkcounter = 0;
                        back = false;
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    // PredMalickow bewegen
                    Walkcounter++;
                    walkpunkt.x += 2;
                    if (Walkcounter % 2 == 1) {
                        walkpunkt.y -= 1;
                    } else {
                        walkpunkt.y += 1;
                    }

                    // PredMalickow zeichnen
                    g.drawImage(pred_stand[0], walkpunkt.x, walkpunkt.y, Breite, Hoehe, null);
                    g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y, null);

                    // Auf Ende der Anim testen
                    if (Walkcounter == 20) {
                        Walkcounter = 0;
                        back = false;
                        return false;
                    } else {
                        return true;
                    }
                }

            case 2:
                // Gebeanim
                Givecounter++;
                g.drawImage(pred_take, punkt.x, punkt.y, Breite, Hoehe, null);
                g.drawImage(vorder, vorderpunkt.x, vorderpunkt.y, null);
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