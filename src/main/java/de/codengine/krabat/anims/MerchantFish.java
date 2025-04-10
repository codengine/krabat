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

public class MerchantFish extends MainAnim {
    private final GenericImage[] wik_head;
    private final GenericImage[] wik_body;
    private final GenericImage[] wik_kram;
    private final GenericImage[] octopussy;
    private GenericImage foreground;

    public static final int Breite = 50;
    public static final int Hoehe = 50;

    private int Head = 0;
    private int Body = 0;
    private int Stand = 0;
    private int Kram = 0;
    private int Octo = 0;

    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;

    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;

    private int Verhinderstand;
    private static final int MAX_VERHINDERSTAND = 50;

    private static final int BODYOFFSET = 27;

    private int Verhinderkram;
    private static final int MAX_VERHINDERKRAM = 7;

    private int Verhinderocto;
    private static final int MAX_VERHINDEROCTO = 4;

    private static final int AUGENZWINKERN = 1;

    private boolean isOctopussy = false;
    private boolean forward = true;

    public MerchantFish(Start caller) {
        super(caller);

        wik_head = new GenericImage[9];
        wik_body = new GenericImage[5];
        wik_kram = new GenericImage[3];
        octopussy = new GenericImage[3];

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;
        Verhinderstand = MAX_VERHINDERSTAND;
        Verhinderkram = MAX_VERHINDERKRAM;
        Verhinderocto = MAX_VERHINDEROCTO;
    }

    private void InitImages() {
        wik_head[0] = getPicture("gfx/kulow/fh-h.png");
        wik_head[1] = getPicture("gfx/kulow/fh-h1.png");
        wik_head[2] = getPicture("gfx/kulow/fh-h2.png");
        wik_head[3] = getPicture("gfx/kulow/fh-h3.png");
        wik_head[4] = getPicture("gfx/kulow/fh-h4.png");
        wik_head[5] = getPicture("gfx/kulow/fh-h5.png");
        wik_head[6] = getPicture("gfx/kulow/fh-hb.png");
        wik_head[7] = getPicture("gfx/kulow/fh-ha.png");
        wik_head[8] = getPicture("gfx/kulow/fh-hc.png");

        wik_body[0] = getPicture("gfx/kulow/fh-b.png");
        wik_body[1] = getPicture("gfx/kulow/fh-ba.png");
        wik_body[2] = getPicture("gfx/kulow/fh-bb.png");
        wik_body[3] = getPicture("gfx/kulow/fh-bc.png");
        wik_body[4] = getPicture("gfx/kulow/fh-bd.png");

        wik_kram[0] = getPicture("gfx/kulow/fhz.png");
        wik_kram[1] = getPicture("gfx/kulow/fhz2.png");
        wik_kram[2] = getPicture("gfx/kulow/fhz3.png");

        octopussy[0] = getPicture("gfx/kulow/oct1.png");
        octopussy[1] = getPicture("gfx/kulow/oct2.png");
        octopussy[2] = getPicture("gfx/kulow/oct3.png");

        foreground = getPicture("gfx/kulow/budka.png");
    }

    // Zeichne WikowarRybow, wie er dasteht oder spricht
    public void drawRybowar(GenericDrawingContext offGraph, int TalkPerson, int AnimTalkPerson,
                            GenericPoint Posit, boolean islistening, boolean givekrosik,
                            boolean isUmgedreht, boolean showOctopussy, boolean isTaking) {
        if (showOctopussy) {
            isOctopussy = true;
        }

        // Fischahendler steht normal da
        if (!isUmgedreht) {
            // beim Reden
            if (TalkPerson == 33 && mainFrame.talkCount > 1 || AnimTalkPerson == 33) {
                // neuen Kopf aussuchen
                if (--Verhinderhead < 1) {
                    Verhinderhead = MAX_VERHINDERHEAD;

                    Head = (int) Math.round(Math.random() * 7);
                    if (Head == 7) {
                        Head = 0;
                    }
                }

                // Neuen Body aussuchen
                if (--Verhinderbody < 1) {
                    Verhinderbody = MAX_VERHINDERBODY;

                    Body = (int) Math.round(Math.random() * 3);
                    if (Body == 3) {
                        Body = 0;
                    }
                }

                offGraph.drawImage(wik_head[Head], Posit.x, Posit.y);
                offGraph.drawImage(!givekrosik ? wik_body[Body] : wik_body[3], Posit.x, Posit.y + BODYOFFSET);
                offGraph.drawImage(foreground, 688, 327);
                return;
            }

            // wenn er nicht redet, dann kann er ja nur noch rumstehen

            // Kopf aussuchen
            if (--Verhinderstand < 1) {
                // Hat er gezwinkert ? Dann reset
                if (Stand == 6 || Stand == 8) {
                    // Zwinkern zuruecksetzen
                    if (Stand == 6) {
                        Stand = 0;
                    } else {
                        Stand = 7;
                    }

                } else {
                    // Hier auswaehlen, welcher Kopf gezeigt wird, entweder 0 oder 7
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi < 25) {
                        Stand = 0;
                    } else {
                        Stand = 7;
                    }

                }
                Verhinderstand = MAX_VERHINDERSTAND;
                if (islistening) {
                    Stand = 0;
                }
            }

            // Hier noch nachgucken, ob er nicht zwinkern sollte
            int zuffZahl = (int) Math.round(Math.random() * 50);
            if (zuffZahl > 48 && (Stand == 0 || Stand == 7)) {
                if (Stand == 0) {
                    Stand = 6;
                }
                if (Stand == 7) {
                    Stand = 8;
                }
                Verhinderstand = AUGENZWINKERN;
            }

            offGraph.drawImage(wik_head[Stand], Posit.x, Posit.y);

            // verschiedene Bodies eval.
            if (givekrosik) {
                offGraph.drawImage(wik_body[3], Posit.x, Posit.y + BODYOFFSET);
            } else {
                if (isTaking) {
                    offGraph.drawImage(wik_body[4], Posit.x, Posit.y + BODYOFFSET);
                } else {
                    offGraph.drawImage(wik_body[0], Posit.x, Posit.y + BODYOFFSET);
                }
            }

            offGraph.drawImage(foreground, 688, 327);
        } else {
            // er hat sich umgedreht und kramt nach irgendwas...
            if (!isOctopussy) {
                // nur normal hinten kramen
                if (--Verhinderkram < 1) {
                    Verhinderkram = MAX_VERHINDERKRAM;
                    Kram = (int) (Math.random() * 1.9);
                }
            } else {
                // der weisse Hai kommt...
                Kram = 2; // haendler steht solange nur so da

                if (--Verhinderocto < 1) {
                    Verhinderocto = MAX_VERHINDEROCTO;
                    if (forward) {
                        Octo++;
                        if (Octo == 3) {
                            Octo = 1;
                            forward = false;
                        }
                    } else {
                        Octo--;
                        if (Octo < 0) {
                            Octo = 0;
                            forward = true;
                            isOctopussy = false;
                        }
                    }
                }
                offGraph.drawImage(octopussy[Octo], 732, 343);
            }
            offGraph.drawImage(wik_kram[Kram], 697, 360);
            offGraph.drawImage(foreground, 688, 327);
        }
    }
}    