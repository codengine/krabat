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

public class Bludnicki extends MainAnim {
    private final GenericImage[][] Head;
    private final GenericImage[] Body;
    private final GenericImage[] Krat;
    private final GenericImage[] Stan;

    private GenericImage take;

    private Fire feuer;

    private int Kopf1 = 1;
    private int Kopf2 = 1;
    private int Rumpf = 1;

    private int Kratz = 1;
    private int Stand = 1;

    private int VerhinderKopf = 0;
    private int VerhinderRumpf = 0;

    private int VerhinderUmdreh = 0;
    private static final int MAX_VERHINDERUMDREH = 25;

    private int VerhinderKratz = 0;
    private static final int MAX_VERHINDERKRATZ = 40;

    private int VerhinderAufhoer = 0;
    private static final int MAX_VERHINDERAUFHOER = 5;

    private int VerhinderTalk = 0;
    private static final int MAX_VERHINDERTALK = 2;

    public final int breite = 34;
    public final int hoehe = 80; // 10 Pix mehr testweise wegen Blinkern...
    private static final int Offset = 10; // Hier Angabe der Hoehenvergroesserung...

    private static final int FEUERKONSTANTE = 5;

    public Bludnicki(Start caller) {
        super(caller);

        Head = new GenericImage[4][7];
        Body = new GenericImage[6];
        Krat = new GenericImage[3];
        Stan = new GenericImage[5];

        feuer = new Fire(mainFrame);

        InitImages();
    }

    private void InitImages() {
        Head[1][1] = getPicture("gfx/labyrinth/bha1.png");
        Head[1][2] = getPicture("gfx/labyrinth/bha2.png");
        Head[1][3] = getPicture("gfx/labyrinth/bha3.png");
        Head[1][4] = getPicture("gfx/labyrinth/bha4.png");
        Head[1][5] = getPicture("gfx/labyrinth/bha5.png");
        Head[1][6] = getPicture("gfx/labyrinth/bha6.png");
        Head[2][1] = getPicture("gfx/labyrinth/bhb1.png");
        Head[2][2] = getPicture("gfx/labyrinth/bhb2.png");
        Head[2][3] = getPicture("gfx/labyrinth/bhb3.png");
        Head[2][4] = getPicture("gfx/labyrinth/bhb4.png");
        Head[2][5] = getPicture("gfx/labyrinth/bhb5.png");
        Head[2][6] = getPicture("gfx/labyrinth/bhb6.png");
        Head[3][1] = getPicture("gfx/labyrinth/bhc1.png");
        Head[3][2] = getPicture("gfx/labyrinth/bhc2.png");
        Head[3][3] = getPicture("gfx/labyrinth/bhc3.png");
        Head[3][4] = getPicture("gfx/labyrinth/bhc4.png");
        Head[3][5] = getPicture("gfx/labyrinth/bhc5.png");
        Head[3][6] = getPicture("gfx/labyrinth/bhc6.png");

        Body[1] = getPicture("gfx/labyrinth/bb1.png");
        Body[2] = getPicture("gfx/labyrinth/bb2.png");
        Body[3] = getPicture("gfx/labyrinth/bb3.png");
        Body[4] = getPicture("gfx/labyrinth/bb4.png");
        Body[5] = getPicture("gfx/labyrinth/bb5.png");

        Krat[1] = getPicture("gfx/labyrinth/bl5.png");
        Krat[2] = getPicture("gfx/labyrinth/bl6.png");

        Stan[1] = getPicture("gfx/labyrinth/bl1.png");
        Stan[2] = getPicture("gfx/labyrinth/bl2.png");
        Stan[3] = getPicture("gfx/labyrinth/bl3.png");
        Stan[4] = getPicture("gfx/labyrinth/bl4.png");

        take = getPicture("gfx/labyrinth/bb-take.png");
    }

    @Override
    public void cleanup() {
        Head[1][1] = null;
        Head[1][2] = null;
        Head[1][3] = null;
        Head[1][4] = null;
        Head[1][5] = null;
        Head[1][6] = null;
        Head[2][1] = null;
        Head[2][2] = null;
        Head[2][3] = null;
        Head[2][4] = null;
        Head[2][5] = null;
        Head[2][6] = null;
        Head[3][1] = null;
        Head[3][2] = null;
        Head[3][3] = null;
        Head[3][4] = null;
        Head[3][5] = null;
        Head[3][6] = null;

        Body[1] = null;
        Body[2] = null;
        Body[3] = null;
        Body[4] = null;
        Body[5] = null;

        Krat[1] = null;
        Krat[2] = null;

        Stan[1] = null;
        Stan[2] = null;
        Stan[3] = null;
        Stan[4] = null;

        take = null;

        feuer.cleanup();
        feuer = null;
    }

    // Zeichne Irrlicht beim Rumstehen oder Sprechen
    public void drawBludnicki(GenericDrawingContext offGraph, int TalkPerson, GenericPoint posit, boolean islistening, boolean isTaking) {
        int zuffi;

        // redender Blud
        if (TalkPerson == 35 && mainFrame.talkCount > 1) {
            VerhinderKopf++;
            VerhinderRumpf++;

            if (VerhinderTalk++ >= MAX_VERHINDERTALK) {
                VerhinderTalk = 0;
                // Kopfzufallsphasen reden (Headshake)
                zuffi = (int) Math.round(Math.random() * 7);
                if (zuffi < 7 && zuffi > 0) {
                    Kopf2 = zuffi;
                }
            }

            // Kopf nach oben und unten schieben, aber kein uebelstes Strecken
            zuffi = (int) Math.round(Math.random() * 10);
            if (zuffi < 4 && VerhinderKopf > 6) {
                if (Kopf1 == 1) {
                    Kopf1 = 2;
                } else {
                    if (Kopf1 == 3) {
                        Kopf1 = 2;
                    } else {
                        if (zuffi < 2) {
                            Kopf1 = 1;
                        }
                        if (zuffi > 1) {
                            Kopf1 = 3;
                        }
                    }
                }
                VerhinderKopf = 0;
            }

            // Rumpf bewegen
            zuffi = (int) Math.round(Math.random() * 20);
            if (zuffi < 6 && zuffi > 0 && VerhinderRumpf > 6) {
                Rumpf = zuffi;
                VerhinderRumpf = 0;
            }
            feuer.drawPlomja(offGraph, new GenericPoint(posit.x + FEUERKONSTANTE, posit.y));
            offGraph.drawImage(Head[Kopf1][Kopf2], posit.x, posit.y + Offset);
            if (!isTaking) {
                offGraph.drawImage(Body[Rumpf], posit.x, posit.y + Offset);
            } else {
                offGraph.drawImage(take, posit.x + 6, posit.y + Offset);
            }
        }

        // Blud beim Rumstehen
        else {
            VerhinderKopf = 0;
            VerhinderRumpf = 0;

            // Umdrehen nach bestimmter Zeit
            VerhinderUmdreh++;
            if (VerhinderUmdreh == MAX_VERHINDERUMDREH) {
                VerhinderUmdreh = 0;
                Stand = (int) Math.round(Math.random() * 4);
                Stand++;
                if (Stand == 5) {
                    Stand = 4;
                }

            }

            // wenn er zuhoert, dann besser nicht umdrehen !!
            if (islistening) {
                if (Stand > 2) {
                    Stand -= 2;
                }
            }

            // nach vorn schauend zwinkern
            if (Stand == 2) {
                Stand = 1;
            } else {
                if (Stand == 1) {
                    zuffi = (int) Math.round(Math.random() * 50);
                    if (zuffi > 45) {
                        Stand = 2;
                    }
                }
            }

            // nach hinten schauend zwinkern
            if (Stand == 4) {
                Stand = 3;
            } else {
                if (Stand == 3) {
                    zuffi = (int) Math.round(Math.random() * 50);
                    if (zuffi > 45) {
                        Stand = 4;
                    }
                }
            }

            // ab und zu kratzen
            if (Kratz != 1 || VerhinderKratz++ >= MAX_VERHINDERKRATZ) {
                VerhinderKratz = 0;
                Kratz = (int) Math.round(Math.random() * 2);
                Kratz += 2;
                if (Kratz == 4) {
                    Kratz = 3;
                }

                // Kratzen irgendwann wieder aussschalten
                if (VerhinderAufhoer++ >= MAX_VERHINDERAUFHOER) {
                    zuffi = (int) Math.round(Math.random() * 20);
                    if (zuffi > 17) {
                        Kratz = 1;
                        VerhinderAufhoer = 0;
                    }
                }
            } else {
                //noinspection DataFlowIssue
                Kratz = 1;
            }

            feuer.drawPlomja(offGraph, new GenericPoint(posit.x + FEUERKONSTANTE, posit.y));
            offGraph.drawImage(Stan[Stand], posit.x, posit.y + Offset);
            if (!isTaking) {
                offGraph.drawImage(Kratz == 1 ? Body[1] : Krat[Kratz - 1], posit.x, posit.y + Offset);
            } else {
                offGraph.drawImage(take, posit.x - 6, posit.y + Offset);  // hier ist das take
            }
        }
    }
}    