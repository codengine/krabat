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
import de.codengine.krabat.main.BorderRect;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Geese extends MainAnim {

    private static final Logger log = LoggerFactory.getLogger(Geese.class);
    private final GenericImage[][] Gaense;
    private final BorderRect Grenze;
    private float Positx;
    private float Posity;

    private int Richtung;
    private int Aktion;
    private int ResetSchnatter = 0;
    private int ResetRichtung = 0;
    private int ResetAktion = 0;

    private static final int KONSTANTE1 = 100;
    private static final int KONSTANTE2 = 100;
    private static final int KONSTANTE3 = 100;

    public final boolean lautloseGaense = false;  // Schalter, ob sie auch wirklich Schnattern sollen (Intro)

    public Geese(Start caller, BorderRect gr) {
        super(caller);

        Gaense = new GenericImage[4][9];
        Grenze = gr;
        Positx = (float) (Grenze.ru_point.x + Grenze.lo_point.x) / 2;
        Posity = (float) (Grenze.ru_point.y + Grenze.lo_point.y) / 2;

        // System.out.println (Position);

        Richtung = (int) Math.round(Math.random() * 3) + 1;
        if (Richtung == 4) {
            Richtung = 3;
        }
        Aktion = 3;
        InitImages();
    }

    // 2. Konstruktor fuer vordefinierte Positionen erforderlich !! (???)

    private void InitImages() {
        Gaense[1][1] = getPicture("gfx/doma/g1a.png");
        Gaense[1][2] = getPicture("gfx/doma/g1a2.png");
        Gaense[1][3] = getPicture("gfx/doma/g1b.png");
        Gaense[1][4] = getPicture("gfx/doma/g1b2.png");
        Gaense[1][5] = getPicture("gfx/doma/g1c.png");
        Gaense[1][6] = getPicture("gfx/doma/g1c2.png");
        Gaense[1][7] = getPicture("gfx/doma/g1d.png");
        Gaense[1][8] = getPicture("gfx/doma/g1d2.png");
        Gaense[2][1] = getPicture("gfx/doma/g2a.png");
        Gaense[2][2] = getPicture("gfx/doma/g2a2.png");
        Gaense[2][3] = getPicture("gfx/doma/g2b.png");
        Gaense[2][4] = getPicture("gfx/doma/g2b2.png");
        Gaense[2][5] = getPicture("gfx/doma/g2c.png");
        Gaense[2][6] = getPicture("gfx/doma/g2c2.png");
        Gaense[3][1] = getPicture("gfx/doma/g3a.png");
        Gaense[3][2] = getPicture("gfx/doma/g3a2.png");
        Gaense[3][3] = getPicture("gfx/doma/g3b.png");
        Gaense[3][4] = getPicture("gfx/doma/g3b2.png");
        Gaense[3][5] = getPicture("gfx/doma/g3c.png");
        Gaense[3][6] = getPicture("gfx/doma/g3c2.png");
        Gaense[3][7] = getPicture("gfx/doma/g3d.png");
        Gaense[3][8] = getPicture("gfx/doma/g3d2.png");
    }

    @Override
    public void cleanup() {
        Gaense[1][1] = null;
        Gaense[1][2] = null;
        Gaense[1][3] = null;
        Gaense[1][4] = null;
        Gaense[1][5] = null;
        Gaense[1][6] = null;
        Gaense[1][7] = null;
        Gaense[1][8] = null;
        Gaense[2][1] = null;
        Gaense[2][2] = null;
        Gaense[2][3] = null;
        Gaense[2][4] = null;
        Gaense[2][5] = null;
        Gaense[2][6] = null;
        Gaense[3][1] = null;
        Gaense[3][2] = null;
        Gaense[3][3] = null;
        Gaense[3][4] = null;
        Gaense[3][5] = null;
        Gaense[3][6] = null;
        Gaense[3][7] = null;
        Gaense[3][8] = null;
    }

    public void BewegeGans(GenericDrawingContext offGraph) {
        ResetRichtung++;

        // Entscheidung, ob die Animphase oder die Richtung gewechselt werden soll
        int zuffi = (int) Math.round(Math.random() * KONSTANTE1);

        if ((Aktion & 1) == 0) {
            ResetSchnatter++;
        }

        // die kleineren Anims, die oefter erfolgen duerfen
        if (zuffi < 20 || ResetSchnatter == 4) {
            ResetSchnatter = 0;
            if ((Aktion & 1) == 0) {
                // es ist eine Schnatteraktion, also zuruecksetzen
                Aktion--;
            } else {
                // das Schnattern darf beginnen
                Aktion++;
            }
        } else if (zuffi > 80 && ResetRichtung > 15) {
            ResetRichtung = 0;

            // die groesseren Bewegungen, die nicht so oft erscheinen sollen

            // Test, ob die Richtung gewechselt werden darf, das darf nur bei Aktion = 3 erfolgen
            if (Aktion == 3) {
                int zu = (int) Math.round(Math.random() * KONSTANTE2);
                if (zu > 70) {
                    // Richtungswechsel je nach bereits vorhandener Richtung
                    switch (Richtung) {
                        case 1:
                            // kann nur groesser werden
                            Richtung++;
                            break;
                        case 2:
                            // kann groesser oder kleiner werden
                            if ((int) Math.round(Math.random() * 10) > 5) {
                                Richtung--;
                            } else {
                                Richtung++;
                            }
                            break;
                        case 3:
                            Richtung--;
                            break;
                        default:
                            log.error("Falsche Richtungsvariable aufgetreten! Richtung = {}", Richtung);
                    }
                }
            }
        }

        ResetAktion++;
        EvalNewAction();

        if (Aktion == 7 || Aktion == 8) {
            if (Richtung == 1) {
                // System.out.println ("x + 1");
                if (Positx - Grenze.lo_point.x > 1) {
                    Positx -= 0.5F;
                } else {
                    while (Aktion == 7 || Aktion == 8) {
                        GetNewAction();
                    }
                }
            } else {
                // System.out.println ("x - 1");
                if (Grenze.ru_point.x - Positx > 1) {
                    Positx += 0.5F;
                } else {
                    while (Aktion == 7 || Aktion == 8) {
                        GetNewAction();
                    }
                }
            }

            int zi = (int) Math.round(Math.random() * 100);

            if (zi < 15 && Posity - Grenze.lo_point.y > 1) {
                Posity -= 0.5F;
            }
            if (zi > 85 && Grenze.ru_point.y - Posity > 1) {
                Posity += 0.5F;
            }
        }

        offGraph.drawImage(Gaense[Richtung][Aktion], (int) Positx, (int) Posity);

        EvalSound();
    }

    private void EvalNewAction() {
        // Hier nur zufaellig die Phasen einer Richtung berechnen
        int zuf = (int) Math.round(Math.random() * KONSTANTE3);
        if (zuf > 70 && (Aktion & 1) != 0 && ResetAktion > 10) {
            ResetAktion = 0;
            GetNewAction();
        }
    }

    private void GetNewAction() {
        // Hier wird immer neue Aktion berechnet
        int zf;

        do {
            zf = (int) Math.round(Math.random() * 90);
            zf = zf / 10;
        }
        while ((zf & 1) == 0 || zf > (Richtung == 2 ? 5 : 7) || zf == Aktion);

        // System.out.println ("Neue Aktion " + zf);

        Aktion = zf;
    }

    private void EvalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        if (lautloseGaense) {
            return; // kein Sound, wenn vom Programm aus abgeschaltet
        }

        int zufallsZahl = (int) (Math.random() * 100);

        if (zufallsZahl > 98) // nur selten was ausgeben
        {
            int zweiterZufall = (int) (Math.random() * 3.9);
            zweiterZufall += 49;
            mainFrame.soundPlayer.PlayFile("sfx/husa" + (char) zweiterZufall + ".wav");
        }
    }

    public BorderRect GetHusaRect() {
        // gibt Borderrect zurueck, in dem sich die Gans befindet
        int a, b, c, d;

        a = (int) Positx;
        if (Richtung == 2) {
            a += 3;  // bei Brieteren Images bisschen einruecken
        }
        b = (int) Posity;
        c = a + 35;
        d = b + 40;

        return new BorderRect(a, b, c, d);
    }
}    		