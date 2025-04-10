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

public class Coach extends MainAnim {
    private GenericImage kutsche;
    private GenericImage kutschentuer/*, pferdohr1, pferdohr2, hinterohr */;
    private final GenericImage[] pferdschwanz;
    private final GenericImage[] kleineWolke;
    private final GenericImage[] grosseWolke;
    private GenericImage vorder;
    private GenericImage pohonc;

    private static final GenericPoint kutscheLO = new GenericPoint(0, 252);

    // private int Auge = 0;
    // private int Ohr = 0;
    private int Schwanz = 0;

    private int Verhinderschwanz;

    private static final int MAX_VERHINDERSCHWANZ = 80;
    private static final int MAX_VERHINDERSCHWANZWACKEL = 3;

    private int klWolke;
    private int grWolke;

    private int Verhinderklwolke;
    private int Verhindergrwolke;

    private static final int MAX_VERHINDERKLWOLKE = 1;
    private static final int MAX_VERHINDERGRWOLKE = 1;

    private int Kutschenzustand = 0;

    private static final GenericPoint[] Anfangspunkte =
            {new GenericPoint(460, 230),
                    new GenericPoint(390, 350),
                    new GenericPoint(105, 479),
                    new GenericPoint(105, 479)};

    private static final GenericPoint[] Endpunkte =
            {new GenericPoint(390, 333),
                    new GenericPoint(220, 500),
                    new GenericPoint(105, 479),
                    new GenericPoint(0, 522)};

    private static final float[] Offsets =
            {0.7f, 3f, 0f, 3f};

    private static final float[] Scalings =
            {10f, 0.6f, 0f, 0f};

    private float Xpos;
    private float Ypos;

    private float Xoffset;
    private float Yoffset;

    private float Xende;

    // private float Xanfang;
    private float Yanfang;

    private float scale;

    private static final int kBreite = 44;
    private static final int kHoehe = 38;

    private static final int gBreite = 200;
    private static final int gHoehe = 200;

    private final float scaleVerhaeltnisKlein;
    private final float scaleVerhaeltnisGross;

    private int Rauch = 0;

    private int Verhinderrauch;

    private static final int MAX_VERHINDERRAUCH = 5;

    private boolean isKutscheRauching = true;

    private static final GenericPoint pohoncPoint = new GenericPoint(80, 267);

    private boolean hintenMusik = true;  // fuer einmaliges Musikabspielen
    private boolean vornMusik = true;

    public Coach(Start caller) {
        super(caller);

        pferdschwanz = new GenericImage[3];
        kleineWolke = new GenericImage[6];
        grosseWolke = new GenericImage[6];

        InitImages();

        Verhinderschwanz = MAX_VERHINDERSCHWANZ;

        Verhinderklwolke = MAX_VERHINDERKLWOLKE;
        Verhindergrwolke = MAX_VERHINDERGRWOLKE;

        float breit = kBreite;
        float hoch = kHoehe;
        scaleVerhaeltnisKlein = breit / hoch;

        breit = gBreite;
        hoch = gHoehe;
        scaleVerhaeltnisGross = breit / hoch;

        Verhinderrauch = MAX_VERHINDERRAUCH;
    }

    // Initialisiert die Kutsche in den Zustaenden
    public void InitKutsche(int Kutschenzustand) {
        // 0 = Hinten zu sehen
        // 1 = Vorn heranfahrend
        // 2 = Vorn stehend
        // 3 = wieder abfahrend (bis Ende)

        this.Kutschenzustand = Kutschenzustand;

        if (Kutschenzustand > 1) {
            return; // Abbruch bei stehend, da dort die Daten
        }
        // aus dem Zustand davor benoetigt werden

        klWolke = 0;
        grWolke = 0;

        Xpos = Anfangspunkte[Kutschenzustand].x;
        Ypos = Anfangspunkte[Kutschenzustand].y;

        // Xanfang = Xpos;
        Yanfang = Ypos;

        Xende = Endpunkte[Kutschenzustand].x;
        float yende = Endpunkte[Kutschenzustand].y;

        scale = Scalings[Kutschenzustand];

        Xoffset = Offsets[Kutschenzustand];
        if (Xende != Xpos) {
            Yoffset = (yende - Ypos) / (Xende - Xpos) * Xoffset;
        } else {
            Yoffset = 0f;
        }
    }

    private void InitImages() {
        kutsche = getPicture("gfx/most/kutse.png");
        kutschentuer = getPicture("gfx/most/kudurje.png");

        // pferdohr1    = getPicture ("gfx/most/kohr.png");
        // pferdohr2    = getPicture ("gfx/most/kohr2.png");
        // hinterohr    = getPicture ("gfx/most/kohr3.png");

        pferdschwanz[0] = getPicture("gfx/most/ps1.png");
        pferdschwanz[1] = getPicture("gfx/most/ps2.png");
        pferdschwanz[2] = getPicture("gfx/most/ps3.png");

        kleineWolke[0] = getPicture("gfx/most/wo1.png");
        kleineWolke[1] = getPicture("gfx/most/wo2.png");
        kleineWolke[2] = getPicture("gfx/most/wo3.png");
        kleineWolke[3] = getPicture("gfx/most/wo4.png");
        kleineWolke[4] = getPicture("gfx/most/wo5.png");
        kleineWolke[5] = getPicture("gfx/most/wo6.png");

        grosseWolke[0] = getPicture("gfx/most/gwo1.png");
        grosseWolke[1] = getPicture("gfx/most/gwo2.png");
        grosseWolke[2] = getPicture("gfx/most/gwo3.png");
        grosseWolke[3] = getPicture("gfx/most/gwo4.png");
        grosseWolke[4] = getPicture("gfx/most/gwo5.png");
        grosseWolke[5] = getPicture("gfx/most/gwo6.png");

        vorder = getPicture("gfx/most/most-3.png");

        pohonc = getPicture("gfx/most/kutscher.png");
    }

    // gibt Rectangle an Most zurueck, wo sich Kutsche befindet
    public GenericRectangle kutscheRect() {
        switch (Kutschenzustand) {
            case 0:
                // hinten fahrend
                int xVeraenderung = kBreite + (int) ((Ypos - Yanfang) / scale * scaleVerhaeltnisKlein);
                int yVeraenderung = kHoehe + (int) ((Ypos - Yanfang) / scale);
                return new GenericRectangle((int) (Xpos - (float) xVeraenderung / 2), (int) (Ypos - yVeraenderung), xVeraenderung, yVeraenderung);
            case 1:
            case 3:
                // vorn fahrend
                int xxVeraenderung = gBreite + (int) ((Ypos - Yanfang) / scale * scaleVerhaeltnisGross);
                int yyVeraenderung = gHoehe + (int) ((Ypos - Yanfang) / scale);
                return new GenericRectangle((int) (Xpos - (float) xxVeraenderung / 2), (int) (Ypos - yyVeraenderung), xxVeraenderung, yyVeraenderung);
            case 2:
                // vorn stehend
                if (!isKutscheRauching)  // nur Kutsche an sich
                {
                    return new GenericRectangle(kutscheLO.x, kutscheLO.y, kutscheLO.x + 267, kutscheLO.y + 228);
                } else {
                    // Kutsche + Rauch
                    int xxxVeraenderung = gBreite + (int) ((Ypos - Yanfang) / scale * scaleVerhaeltnisGross);
                    int yyyVeraenderung = gHoehe + (int) ((Ypos - Yanfang) / scale);
                    return new GenericRectangle((int) (Xpos - (float) xxxVeraenderung / 2), (int) (Ypos - yyyVeraenderung), xxxVeraenderung, yyyVeraenderung);
                }
        }

        // Compilerberuhigung...
        return new GenericRectangle(0, 0, 0, 0);
    }

    // Zeichne Kutsche mit allen Anims
    public int drawKutsche(GenericDrawingContext g, boolean doorOpen) {
        // 0 = fertig; != 0 -> arbeitet noch, wird auch anderweitig benutzt

        switch (Kutschenzustand) {
            case 0:
                // Kleine Wolke hinten (Vordergrund immer mitmalen)

                // Sound eval.
                if (hintenMusik) {
                    hintenMusik = false;
                    // mainFrame.wave.PlayFile ("sfx/fanfara.wav");
                }

                // GenericImage weiterschalten
                if (--Verhinderklwolke < 1) {
                    Verhinderklwolke = MAX_VERHINDERKLWOLKE;
                    klWolke++;
                    if (klWolke == 6) {
                        klWolke = 3;
                    }
                }

                // Wolke bewegen
                Xpos -= Xoffset;
                Ypos -= Yoffset;

                g.setClip(kutscheRect());

                // Veraenderung der Groesse berechnen
                int xVeraenderung = kBreite + (int) ((Ypos - Yanfang) / scale * scaleVerhaeltnisKlein);
                int yVeraenderung = kHoehe + (int) ((Ypos - Yanfang) / scale);

                // hier entweder 1 oder 2 Images malen
                if (klWolke > 2) {
                    g.drawImage(kleineWolke[klWolke - 3], (int) (Xpos - (float) xVeraenderung / 2), (int) (Ypos - yVeraenderung), xVeraenderung, yVeraenderung);
                    g.drawImage(kleineWolke[klWolke], (int) (Xpos - (float) xVeraenderung / 2), (int) (Ypos - yVeraenderung), xVeraenderung, yVeraenderung);
                } else {
                    g.drawImage(kleineWolke[klWolke], (int) (Xpos - (float) xVeraenderung / 2), (int) (Ypos - yVeraenderung), xVeraenderung, yVeraenderung);
                }
                // Vordergrund zeichnen (hier immer)
                g.drawImage(vorder, 342, 270);

                // testen auf "ich habe fertig"
                if (Xpos <= Xende) {
                    return 0;
                } else {
                    return 1;
                }

            case 1:
                // grosse Wolke vorn

                // Sound eval.
                if (vornMusik) {
                    vornMusik = false;
                    mainFrame.soundPlayer.PlayFile("sfx/kutsa.wav");
                }

                // GenericImage weiterschalten
                if (--Verhindergrwolke < 1) {
                    Verhindergrwolke = MAX_VERHINDERGRWOLKE;
                    grWolke++;
                    if (grWolke == 6) {
                        grWolke = 3;
                    }
                }

                // Wolke bewegen
                Xpos -= Xoffset;
                Ypos -= Yoffset;

                g.setClip(kutscheRect());

                // Groessenveraenderung berechnen
                int xxVeraenderung = gBreite + (int) ((Ypos - Yanfang) / scale * scaleVerhaeltnisGross);
                int yyVeraenderung = gHoehe + (int) ((Ypos - Yanfang) / scale);

                // hier entweder 1 oder 2 Images malen
                if (grWolke > 2) {
                    g.drawImage(grosseWolke[grWolke - 3], (int) (Xpos - (float) xxVeraenderung / 2), (int) (Ypos - yyVeraenderung), xxVeraenderung, yyVeraenderung);
                    g.drawImage(grosseWolke[grWolke], (int) (Xpos - (float) xxVeraenderung / 2), (int) (Ypos - yyVeraenderung), xxVeraenderung, yyVeraenderung);
                } else {
                    g.drawImage(grosseWolke[grWolke], (int) (Xpos - (float) xxVeraenderung / 2), (int) (Ypos - yyVeraenderung), xxVeraenderung, yyVeraenderung);
                }

                // testen auf "ich habe fertig"

                if (Xpos <= Xende) {
                    return 0; // wir sind da, Rauch muss sich noch lichten
                } else {
                    if (Math.abs(Xpos - Xende) <= 20) {
                        return 10; // Signal fuer erstes Talking
                    } else {
                        return 1;   // noch voll im Gange
                    }
                }

            case 2:
                // Rumstehende Kutsche mit allerlei Nebenbeianims

                // zuerstmal die Kutsche zeichnen, das ist immer dran
                g.drawImage(kutsche, kutscheLO.x, kutscheLO.y);

                // solange es noch nicht von Aussen erledigt wird, hier den Kutscher zeichnen
                if (isKutscheRauching) {
                    g.drawImage(pohonc, pohoncPoint.x, pohoncPoint.y);
                }

                // hier die Tuer, wenn sie offen ist
                if (doorOpen) {
                    g.drawImage(kutschentuer, kutscheLO.x + 68, kutscheLO.y + 20);
                }

                // eval. ob das pferd mit den Augen zwinkert
                // int zf = (int) (Math.random () * 50);
		/*  if (Auge == 1) 
		  {
		  Auge = 0;
		  g.drawImage (pferdauge, kutscheLO.x + 17, kutscheLO.y + 119, null);
		  }
		  else
		  {
		  if (zf > 45) Auge = 1;
		  }*/

                // eval, ob es mit den Ohren wackelt
		/*if (Ohr == 1)
		    {
			Ohr = 0;
			g.drawImage (hinterohr, 41, 347, null);
			g.drawImage (pferdohr1, kutscheLO.x + 39, kutscheLO.y + 99, null);
		    }
		else
		    {
			if (zf > 48) Ohr = 1;
			g.drawImage (hinterohr, 41, 347, null);
                        g.drawImage (pferdohr2, kutscheLO.x + 39, kutscheLO.y + 99, null);
			}*/

                // eval. ob es mit dem Schwanz wackelt
                if (--Verhinderschwanz < 1) {
                    if (Schwanz < 2) {
                        Verhinderschwanz = MAX_VERHINDERSCHWANZWACKEL;
                    } else {
                        Verhinderschwanz = MAX_VERHINDERSCHWANZ;
                    }

                    Schwanz++;
                    if (Schwanz == 3) {
                        Schwanz = 0;
                    }
                }
                g.drawImage(pferdschwanz[Schwanz], kutscheLO.x + 126, kutscheLO.y + 187);

                // wenns noch aktuell ist, dann die Kutsche "aus dem Rauch" aufsteigen lassen
                if (isKutscheRauching) {
                    // GenericImage weiterschalten
                    if (--Verhinderrauch < 1) {
                        Verhinderrauch = MAX_VERHINDERRAUCH;
                        Rauch++;
                        if (Rauch == 3) {
                            isKutscheRauching = false;
                        }
                    }


                    // Groessenveraenderung berechnen
                    int xxxxVeraenderung = gBreite + (int) ((Ypos - Yanfang) / scale * scaleVerhaeltnisGross);
                    int yyyyVeraenderung = gHoehe + (int) ((Ypos - Yanfang) / scale);

                    // dazugehoeriges GenericImage malen
                    if (Rauch < 3) {
                        g.drawImage(grosseWolke[Rauch + 3], (int) (Xpos - (float) xxxxVeraenderung / 2), (int) (Ypos - yyyyVeraenderung), xxxxVeraenderung, yyyyVeraenderung);
                    }
                    if (!isKutscheRauching) {
                        return 0; // es wurde schon auf normal umgeschaltet
                    } else {
                        return 1; // Signal, dass noch rauch drum ist
                    }
                }

                return 0;  // alles wie gehabt, nur die Kutsche zu sehen..

            case 3:
                // grosse Wolke, die das Bild verlaesst -> momentan unbenutzt!!!!!!!!!!!!!!! also egal ;-)

                // GenericImage weiterschalten
                if (--Verhindergrwolke < 1) {
                    Verhindergrwolke = MAX_VERHINDERGRWOLKE;
                    grWolke++;
                    if (grWolke == 6) {
                        grWolke = 3;
                    }
                }

                // Wolke bewegen
                Xpos -= Xoffset;
                Ypos -= Yoffset;

                g.setClip(kutscheRect());

                // hier entweder 1 oder 2 Images malen
                if (grWolke > 2) {
                    g.drawImage(grosseWolke[grWolke - 3], (int) (Xpos - (float) gBreite / 2), (int) (Ypos - gHoehe));
                    g.drawImage(grosseWolke[grWolke], (int) (Xpos - (float) gBreite / 2), (int) (Ypos - gHoehe));
                } else {
                    g.drawImage(grosseWolke[grWolke], (int) (Xpos - (float) gBreite / 2), (int) (Ypos - gHoehe));
                }

                // testen auf "ich habe fertig"
                if (Xpos <= Xende) {
                    return 0;
                } else {
                    return 1;
                }

        }

        // is ja gut... brav sein ;-)
        return 1;
    }
}    