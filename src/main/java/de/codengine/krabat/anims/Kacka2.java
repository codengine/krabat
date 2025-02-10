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
import de.codengine.krabat.main.GenericRectangle;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Kacka2 extends Mainanim {

    private final GenericImage[] kacka_flieg;
    private final GenericImage[] kacka_lande;
    private final GenericImage[] kacka_rede;

    private int Schnatter = 0;
    private int Flieg = 0;
    private int Land = 0;

    private int Verhinderschnatter;
    private int Verhinderflieg;
    private int Verhinderland;

    private static final int MAX_VERHINDERSCHNATTER = 3;
    private static final int MAX_VERHINDERFLIEG = 2;
    private static final int MAX_VERHINDERLAND = 5;

    private boolean forward = true;

    private float Positx;
    private float Posity;

    private static final GenericPoint Pstart = new GenericPoint(-40, 190);
    private GenericPoint Pstop;

    private static final int Flugbreite = 43;
    private static final int Flughoehe = 46;

    private static final int Redebreite = 40;
    private static final int Redehoehe = 17;

    private boolean isFlying = true;
    private boolean isGleiting = false;
    private boolean isLanding = false;

    private static final float Xoffset = 4f;
    private float Yoffset;

    private static final int FLUGOFFSET = 19;

    private boolean landeSoundPlayed = false;
    private boolean flugSoundPlayed = false;

    public Kacka2(Start caller) {
        super(caller);

        kacka_flieg = new GenericImage[3];
        kacka_lande = new GenericImage[3];
        kacka_rede = new GenericImage[2];

        InitImages();

        Verhinderschnatter = MAX_VERHINDERSCHNATTER;
        Verhinderflieg = MAX_VERHINDERFLIEG;
        Verhinderland = MAX_VERHINDERLAND;
    }

    private void InitImages() {
        kacka_flieg[0] = getPicture("gfx/mertens/kacka2-f1.gif");
        kacka_flieg[1] = getPicture("gfx/mertens/kacka2-f3.gif");
        kacka_flieg[2] = getPicture("gfx/mertens/kacka2-f2.gif");

        kacka_lande[0] = getPicture("gfx/mertens/kacka2-f3a.gif");
        kacka_lande[1] = getPicture("gfx/mertens/kacka2-f3b.gif");
        kacka_lande[2] = getPicture("gfx/mertens/kacka2-f3c.gif");

        kacka_rede[0] = getPicture("gfx/mertens/kacka2.gif");
        kacka_rede[1] = getPicture("gfx/mertens/kacka2a.gif");
    }

    @Override
    public void cleanup() {
        kacka_flieg[0] = null;
        kacka_flieg[1] = null;
        kacka_flieg[2] = null;

        kacka_lande[0] = null;
        kacka_lande[1] = null;
        kacka_lande[2] = null;

        kacka_rede[0] = null;
        kacka_rede[1] = null;
    }

    // Startpos festlegen, sobald bekannt
    public void initPos(GenericPoint andereEnte) {
        Pstop = new GenericPoint(andereEnte.x - 40, andereEnte.y);
        Positx = Pstart.x;
        Posity = Pstart.y;

        float Ydiff = (float) (Pstop.y - Pstart.y);
        float Xdiff = (float) (Pstop.x - Pstart.x - 40);

        Yoffset = Ydiff / Xdiff * Xoffset;

        System.out.println("Yoffset = " + Yoffset);
    }

    // evaluiere Rechteck zum Loeschen der Ente
    public GenericRectangle kackaRect() {
        if (isFlying) {
            // Berechnung beim Fliegen
            int x = (int) (Positx - Flugbreite / 2);
            int y = (int) (Posity - Flughoehe + FLUGOFFSET);
            return new GenericRectangle(x, y, Flugbreite, Flughoehe);
        } else {
            // Berechnung beim Reden
            int x = (int) (Positx - Redebreite / 2);
            int y = (int) (Posity - Redehoehe);
            // System.out.println ("Rectangle Ente 2 : " + x + " " + y + " " + Redebreite + " " + Redehoehe);
            return new GenericRectangle(x, y, Redebreite, Redehoehe);
        }
    }

    // Zeichne Ente, wie sie dasteht oder spricht: Flag gibt an, dass sie zuendegeflogen ist
    public boolean drawKacka(GenericDrawingContext offGraph, int TalkPerson) {
        // Redende Kacka
        if (TalkPerson == 72 && mainFrame.talkCount > 1) {
            if (--Verhinderschnatter < 1) {
                Verhinderschnatter = MAX_VERHINDERSCHNATTER;
                if (Schnatter == 0) {
                    Schnatter = 1;
                } else {
                    Schnatter = 0;
                }
            }

            // Kacka zeichnen
            offGraph.drawImage(kacka_rede[Schnatter], (int) (Positx - Redebreite / 2), (int) (Posity - Redehoehe), null);
        } else {
            if (!isFlying) {
                // wenn sie nicht mehr fliegt, dann nur noch so zeichnen
                offGraph.drawImage(kacka_rede[Schnatter], (int) (Positx - Redebreite / 2), (int) (Posity - Redehoehe), null);
            } else {
                // gleitet schon ?
                if (isGleiting) {
                    // landet schon
                    if (isLanding) {
                        if (!landeSoundPlayed)  // hier Sound 1x abspielen
                        {
                            landeSoundPlayed = true;
                            mainFrame.wave.PlayFile("sfx/woda3.wav");
                        }

                        if (--Verhinderland < 1) {
                            Verhinderland = MAX_VERHINDERLAND;
                            if (Land < 2) {
                                Land++;
                            }
                        }

                        offGraph.drawImage(kacka_lande[Land], (int) (Positx - Flugbreite / 2), (int) (Posity - Flughoehe + FLUGOFFSET), null);
                    }
                    // gleitet noch
                    else {
                        offGraph.drawImage(kacka_flieg[1], (int) (Positx - Flugbreite / 2), (int) (Posity - Flughoehe + FLUGOFFSET), null);
                    }
                } else {
                    // fliegt noch
                    if (--Verhinderflieg < 1) {
                        Verhinderflieg = MAX_VERHINDERFLIEG;
                        if (forward) {
                            Flieg++;
                            if (Flieg == 3) {
                                Flieg = 1;
                                forward = false;
                            }
                        } else {
                            Flieg--;
                            if (Flieg == -1) {
                                Flieg = 1;
                                forward = true;
                            }
                        }
                    }
                    offGraph.drawImage(kacka_flieg[Flieg], (int) (Positx - Flugbreite / 2), (int) (Posity - Flughoehe + FLUGOFFSET), null);
                }

                // die Position muss noch weitergeschaltet werden
                Positx += Xoffset;

                // Ypos nur solange hochzaehlen, bis Maximum erreicht
                if ((int) Posity < Pstop.y) {
                    Posity += Yoffset;
                }
            }
        }
        // hier das Umschalten fliegen -> gleiten -> landen -> ich habe fertig
        if (Pstop.x - Positx < 180) {
            if (!flugSoundPlayed) {
                flugSoundPlayed = true;
                mainFrame.wave.PlayFile("sfx/quack.wav");
            }
        }

        if (Pstop.x - Positx < 150) {
            isGleiting = true;
        }
        if (Pstop.x - Positx < 40) {
            isLanding = true;
        }
        if (Pstop.x - Positx < 2) {
            isFlying = false;
        }

        return isFlying; // wenn gelandet, dann false
    }
}    