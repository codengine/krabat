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
import de.codengine.krabat.main.GenericRectangle;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Rapak extends MainAnim {
    private final GenericImage[] Vogel;
    private int whichanim = 0;
    private int Animcount = 0;
    private int Fliegcount = 0;
    private int Fliegpos = 0;
    private static final int ANIMKONSTANTE = 100;

    private int Verhindertalk = 2;
    private static final int MAX_VERHINDERTALK = 2;

    private int Talk = 1;

    private final int flyx;
    private int flyy;
    // private int flycount = 1;
    private boolean fliegehoch = true;
    private boolean flyfront = true;

    private static final int[] FlyArray = {1, 6, 5, 6};
    private static final int[] PosArray = {0, 2, 4, 2};

    private static final int MAX_FLYX = 412;
    private static final int MAX_FLYY = 144;

    private final RapakiRaven sideptack;

    private boolean flyawaySound = false;

    public Rapak(Start caller) {
        super(caller);

        Vogel = new GenericImage[12];
        sideptack = new RapakiRaven(mainFrame, 412, 78, -50);

        flyx = MAX_FLYX;
        flyy = MAX_FLYY;

        InitImages();
    }

    private void InitImages() {
        Vogel[1] = getPicture("gfx/rapak/pk1.png");
        Vogel[2] = getPicture("gfx/rapak/pk2.png");
        Vogel[3] = getPicture("gfx/rapak/pk3.png");
        Vogel[4] = getPicture("gfx/rapak/pk4.png");
        Vogel[5] = getPicture("gfx/rapak/pk5.png");
        Vogel[6] = getPicture("gfx/rapak/pk5a.png");
        Vogel[7] = getPicture("gfx/rapak/pk6.png");
        Vogel[8] = getPicture("gfx/rapak/pk7.png");
        Vogel[9] = getPicture("gfx/rapak/pk8.png");
        Vogel[10] = getPicture("gfx/rapak/pk9.png");
        Vogel[11] = getPicture("gfx/rapak/pk10.png");
    }

    // gibt das Clipping - Rect zurueck, wo der Rabe drin ist
    public GenericRectangle rapakRect() {
        if (!flyfront) {
            return sideptack.ptack2Rect();
        }
        return new GenericRectangle(flyx - 10, flyy - 10, 35 + 20, 44 + 20);
    }

    // Zeichne Raben bei Animationen oder reden
    public void drawRapak(GenericDrawingContext offGraph, int TalkPerson) {
        if (TalkPerson == 41 && mainFrame.talkCount > 1) {
            // Raben beim Reden (Anims kommen noch)
            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;

                do {
                    Talk = (int) Math.round(Math.random() * 13);
                }
                while (Talk > 11 || Talk == 3 || Talk == 5 || Talk == 4 || Talk < 1 || Talk == 7);
            }

            offGraph.drawImage(Vogel[Talk], 412, 144);
        } else {
            // Raben beim Herumhaengen (normale Anims)
            if (whichanim == 0) {
                whichanim = (int) Math.round(Math.random() * ANIMKONSTANTE) + 1;
            }
            if (whichanim > 3) {
                if (whichanim < 10) {
                    whichanim = 1;
                } else {
                    whichanim = 0;
                }
            }
            switch (whichanim) {
                case 0:
                    // normalen Raben zeichnen
                    offGraph.drawImage(Vogel[1], 412, 144);
                    break;

                case 1:
                    // Zwinkern
                    if (Animcount == 0) {
                        offGraph.drawImage(Vogel[2], 412, 144);
                    }
                    if (Animcount == 1) {
                        offGraph.drawImage(Vogel[2], 412, 144);
                    }
                    Animcount++;
                    if (Animcount == 2) {
                        Animcount = 0;
                        whichanim = 0;
                    }
                    break;

                case 2:
                    // unter Fluegel kratzen
                    if (Animcount == 0) {
                        offGraph.drawImage(Vogel[3], 412, 144);
                    }
                    if (Animcount == 1) {
                        offGraph.drawImage(Vogel[3], 412, 144);
                    }
                    if (Animcount == 2) {
                        offGraph.drawImage(Vogel[1], 412, 144);
                    }
                    if (Animcount == 3) {
                        offGraph.drawImage(Vogel[1], 412, 144);
                    }
                    if (Animcount == 4) {
                        offGraph.drawImage(Vogel[3], 412, 144);
                    }
                    if (Animcount == 5) {
                        offGraph.drawImage(Vogel[3], 412, 144);
                    }
                    Animcount++;
                    if (Animcount == 6) {
                        Animcount = 0;
                        whichanim = 0;
                    }
                    break;

                case 3:
                    // unter Fluegel kratzen andere Seite
                    if (Animcount == 0) {
                        offGraph.drawImage(Vogel[4], 412, 144);
                    }
                    if (Animcount == 1) {
                        offGraph.drawImage(Vogel[4], 412, 144);
                    }
                    if (Animcount == 2) {
                        offGraph.drawImage(Vogel[1], 412, 144);
                    }
                    if (Animcount == 3) {
                        offGraph.drawImage(Vogel[1], 412, 144);
                    }
                    if (Animcount == 4) {
                        offGraph.drawImage(Vogel[4], 412, 144);
                    }
                    if (Animcount == 5) {
                        offGraph.drawImage(Vogel[4], 412, 144);
                    }
                    Animcount++;
                    if (Animcount == 6) {
                        Animcount = 0;
                        whichanim = 0;
                    }
                    break;
            }

        }
    }

    // Raben beim Aufschrecken zeichnen
    public boolean fliegRapak(GenericDrawingContext offGraph) {
        // Rueckgabevariable
        boolean rgabe = false;

        if (fliegehoch) {
            // Rabe erhebt sich
            Fliegpos++;
            if (Fliegpos == 4) {
                Fliegpos = 0;
            }

            Fliegcount++;
            if (Fliegcount == 15) {
                fliegehoch = false;
            }

            flyy -= PosArray[Fliegpos];

        } else {
            // Rabe schwebt nach unten
            Fliegpos = 2;

            flyy += 2;

            if (flyy >= MAX_FLYY) {
                Fliegcount = 0;
                Fliegpos = 0;
                fliegehoch = true;
                rgabe = true;
            }
        }

        offGraph.drawImage(Vogel[FlyArray[Fliegpos]], flyx, flyy);

        return rgabe;
    }

    public boolean flyAway(GenericDrawingContext g) {
        // Vogel erhebt sich und fliegt dann weg
        // Clipping - Region muss in Aufrufer richtig gesetzt werden !!!

        // hier von vorn aus hochfliegen
        if (flyfront) {
            // Rabe erhebt sich
            Fliegpos++;
            if (Fliegpos == 4) {
                Fliegpos = 0;
            }

            flyy -= PosArray[Fliegpos];

            if (flyy < 100) {
                flyfront = false;
            }


            // Vogel zeichnen
            g.drawImage(Vogel[FlyArray[Fliegpos]], flyx, flyy);

            return true;
        }

        // hier zur Seite wegfliegen
        else {
            if (!flyawaySound) {
                flyawaySound = true;
                mainFrame.soundPlayer.PlayFile("sfx/rapak1.wav");
            }

            return sideptack.Flieg(g);
        }
    }
}    