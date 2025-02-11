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

public class KrabatAngeln extends Mainanim {
    // Szene Haty: Angeln, Fisch rausholen und sprechen
    private final GenericImage[] angle_rechts;
    private final GenericImage[] angle_links;
    private final GenericImage[] rede_rechts;
    private final GenericImage[] schaue_links;

    private int angeln;
    private int fangen;
    private int Verhinderangeln;
    private static final int MAX_VERHINDERANGELN = 2;
    private static final int MAXANGELN = 23;
    private static final int MAXFANGEN = 47;

    private int Zwinkern = 0;

    private int Head = 0;
    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;

    private static final int OFFSETLEFTX = 46;
    private static final int OFFSETLEFTY = 74;
    private static final int OFFSETRIGHTX = 7;
    private static final int OFFSETRIGHTY = 74;

    private static final int OFFSETLEFTHEADX = 21;
    private static final int OFFSETRIGHTHEADX = 17;
    private static final int OFFSETHEADY = 25;

    // Die Images sind so, dass sich die Fusspositionen folgendermassen ergeben:
    // nach links: Fuesse = leftUp + 46x + 74y
    // nach rechts: Fuesse = leftUp + 7x + 74y

    public KrabatAngeln(Start caller) {
        super(caller);

        angle_rechts = new GenericImage[6];
        angle_links = new GenericImage[6];
        rede_rechts = new GenericImage[9];
        schaue_links = new GenericImage[2];

        InitImages();

        angeln = MAXANGELN;
        fangen = MAXFANGEN;
        Verhinderangeln = MAX_VERHINDERANGELN;
        Verhinderhead = MAX_VERHINDERHEAD;
    }

    // alle Bilder reinladen
    private void InitImages() {
        angle_rechts[0] = getPicture("gfx/haty/k-r-wuda1.gif");
        angle_rechts[1] = getPicture("gfx/haty/k-r-wuda2.gif");
        angle_rechts[2] = getPicture("gfx/haty/k-r-wuda3a.gif");
        angle_rechts[3] = getPicture("gfx/haty/k-r-wuda3b.gif");
        angle_rechts[4] = getPicture("gfx/haty/k-r-wuda3c.gif");
        angle_rechts[5] = getPicture("gfx/haty/k-r-wuda3.gif");

        angle_links[0] = getPicture("gfx/haty/k-l-wuda1.gif");
        angle_links[1] = getPicture("gfx/haty/k-l-wuda2.gif");
        angle_links[2] = getPicture("gfx/haty/k-l-wuda3a.gif");
        angle_links[3] = getPicture("gfx/haty/k-l-wuda3b.gif");
        angle_links[4] = getPicture("gfx/haty/k-l-wuda3c.gif");
        angle_links[5] = getPicture("gfx/haty/k-l-wuda3.gif");

        rede_rechts[0] = getPicture("gfx/anims/k-r-h0.gif");
        rede_rechts[1] = getPicture("gfx/anims/k-r-h0a.gif");
        rede_rechts[2] = getPicture("gfx/anims/k-r-h1.gif");
        rede_rechts[3] = getPicture("gfx/anims/k-r-h2.gif");
        rede_rechts[4] = getPicture("gfx/anims/k-r-h3.gif");
        rede_rechts[5] = getPicture("gfx/anims/k-r-h4.gif");
        rede_rechts[6] = getPicture("gfx/anims/k-r-h5.gif");
        rede_rechts[7] = getPicture("gfx/anims/k-r-h6.gif");
        rede_rechts[8] = getPicture("gfx/anims/k-r-h7.gif");

        schaue_links[0] = getPicture("gfx/anims/k-l-h0.gif");
        schaue_links[1] = getPicture("gfx/anims/k-l-h0a.gif");
    }

    @Override
    public void cleanup() {
        angle_rechts[0] = null;
        angle_rechts[1] = null;
        angle_rechts[2] = null;
        angle_rechts[3] = null;
        angle_rechts[4] = null;
        angle_rechts[5] = null;

        angle_links[0] = null;
        angle_links[1] = null;
        angle_links[2] = null;
        angle_links[3] = null;
        angle_links[4] = null;
        angle_links[5] = null;

        rede_rechts[0] = null;
        rede_rechts[1] = null;
        rede_rechts[2] = null;
        rede_rechts[3] = null;
        rede_rechts[4] = null;
        rede_rechts[5] = null;
        rede_rechts[6] = null;
        rede_rechts[7] = null;
        rede_rechts[8] = null;

        schaue_links[0] = null;
        schaue_links[1] = null;
    }

    // rechts angeln, ohne je einen Fisch zu fangen
    public boolean AngleRechts(GenericDrawingContext g, GenericPoint feetPos) {
        boolean rueckgabe = true;

        if (--Verhinderangeln < 1) {
            Verhinderangeln = MAX_VERHINDERANGELN;

            // Head evaluieren (nach links schauend und zwinkern...)
            if (Zwinkern == 1) {
                Zwinkern = 0;
            } else {
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Zwinkern = 1;
                }
            }

            // auf Verlassen pruefen
            angeln--;
            if (angeln == 0) {
                angeln = MAXANGELN;
                Verhinderangeln = MAX_VERHINDERANGELN;
                rueckgabe = false;
            } else {
                rueckgabe = true;
            }
        }

        GenericPoint leftUp = new GenericPoint(feetPos.x - OFFSETRIGHTX, feetPos.y - OFFSETRIGHTY);

        // alles Zeichnen
        g.drawImage(rede_rechts[Zwinkern], leftUp.x - OFFSETRIGHTHEADX, leftUp.y - OFFSETHEADY, null);

        // Body evaluieren
        if (angeln == 18 || angeln == 17 || angeln == 12 || angeln == 11 || angeln == 6 || angeln == 5) {
            g.drawImage(angle_rechts[1], leftUp.x, leftUp.y, null);
        } else {
            g.drawImage(angle_rechts[0], leftUp.x, leftUp.y, null);
        }

        return rueckgabe;
    }

    // links angeln, ohne je einen Fisch zu fangen (Holzfisch nicht dran)
    public boolean AngleLinks(GenericDrawingContext g, GenericPoint feetPos) {
        boolean rueckgabe = true;

        if (--Verhinderangeln < 1) {
            Verhinderangeln = MAX_VERHINDERANGELN;

            // Head evaluieren (nach links schauend und zwinkern...)
            if (Zwinkern == 1) {
                Zwinkern = 0;
            } else {
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Zwinkern = 1;
                }
            }

            // auf Verlassen pruefen
            angeln--;
            if (angeln == 0) {
                angeln = MAXANGELN;
                Verhinderangeln = MAX_VERHINDERANGELN;
                rueckgabe = false;
            } else {
                rueckgabe = true;
            }
        }

        GenericPoint leftUp = new GenericPoint(feetPos.x - OFFSETLEFTX, feetPos.y - OFFSETLEFTY);

        // alles Zeichnen
        g.drawImage(schaue_links[Zwinkern], leftUp.x + OFFSETLEFTHEADX, leftUp.y - OFFSETHEADY, null);

        // Body evaluieren
        if (angeln == 18 || angeln == 17 || angeln == 12 || angeln == 11 || angeln == 6 || angeln == 5) {
            g.drawImage(angle_links[1], leftUp.x, leftUp.y, null);
        } else {
            g.drawImage(angle_links[0], leftUp.x, leftUp.y, null);
        }

        return rueckgabe;
    }

    // rechts angeln und rightigen Fisch dran
    public boolean FangeRechts(GenericDrawingContext g, GenericPoint feetPos) {
        boolean rueckgabe = true;

        if (fangen == 10 && Verhinderangeln == MAX_VERHINDERANGELN) {
            mainFrame.wave.PlayFile("sfx/woda2.wav");
        }      ////////////////////////////////// Sound !!!!!!!!!!!!!!!!!!

        if (--Verhinderangeln < 1) {
            Verhinderangeln = MAX_VERHINDERANGELN;

            // Head evaluieren (nach links schauend und zwinkern...)
            if (Zwinkern == 1) {
                Zwinkern = 0;
            } else {
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Zwinkern = 1;
                }
            }

            // auf Verlassen pruefen
            fangen--;
            if (fangen == 0) {
                fangen = MAXFANGEN;
                Verhinderangeln = MAX_VERHINDERANGELN;
                rueckgabe = false;
            } else {
                rueckgabe = true;
            }
        }

        GenericPoint leftUp = new GenericPoint(feetPos.x - OFFSETRIGHTX, feetPos.y - OFFSETRIGHTY);

        // alles Zeichnen
        g.drawImage(rede_rechts[Zwinkern], leftUp.x - OFFSETRIGHTHEADX, leftUp.y - OFFSETHEADY, null);

        // Body evaluieren
        if (fangen > 10 && rueckgabe) // nur am Anfang und wenn KEIN Verlassen
        {
            // hier noch rumangeln
            if (fangen == 42 || fangen == 41 || fangen == 36 || fangen == 35 || fangen == 30 || fangen == 29 ||
                    fangen == 24 || fangen == 23 || fangen == 18 || fangen == 17 || fangen == 12 || fangen == 11) {
                g.drawImage(angle_rechts[1], leftUp.x, leftUp.y, null);
            } else {
                g.drawImage(angle_rechts[0], leftUp.x, leftUp.y, null);
            }
        } else {
            // gefangen
            switch (fangen) {
                case 0: // fisch hochhalten
                case 1:
                case 2:
                case 3:
                    g.drawImage(angle_rechts[5], leftUp.x, leftUp.y, null);
                    break;

                case 4: // letzte Zappelphase
                case 5:
                case 6:
                    g.drawImage(angle_rechts[4], leftUp.x, leftUp.y, null);
                    break;

                case 7: // mittlere Zappelphase
                case 8:
                    g.drawImage(angle_rechts[3], leftUp.x, leftUp.y, null);
                    break;

                case 9: // erste Zappelphase
                case 10:
                    g.drawImage(angle_rechts[2], leftUp.x, leftUp.y, null);
                    break;
                default: // fuers Reset (letzte Phase) rein
                    g.drawImage(angle_rechts[5], leftUp.x, leftUp.y, null);
                    break;

            }
        }

        return rueckgabe;
    }

    // links angeln und Holzfisch dran
    public boolean FangeLinks(GenericDrawingContext g, GenericPoint feetPos) {
        boolean rueckgabe = true;

        if (fangen == MAXFANGEN && Verhinderangeln == MAX_VERHINDERANGELN) {
            mainFrame.wave.PlayFile("sfx/woda1.wav");
        }      ////////////////////////////////// Sound !!!!!!!!!!!!!!!!!!
        if (fangen == 10 && Verhinderangeln == MAX_VERHINDERANGELN) {
            mainFrame.wave.PlayFile("sfx/woda2.wav");
        }      ////////////////////////////////// Sound !!!!!!!!!!!!!!!!!!


        if (--Verhinderangeln < 1) {
            Verhinderangeln = MAX_VERHINDERANGELN;

            // Head evaluieren (nach links schauend und zwinkern...)
            if (Zwinkern == 1) {
                Zwinkern = 0;
            } else {
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Zwinkern = 1;
                }
            }

            // auf Verlassen pruefen
            fangen--;
            if (fangen == 0) {
                fangen = MAXFANGEN;
                Verhinderangeln = MAX_VERHINDERANGELN;
                rueckgabe = false;
            } else {
                rueckgabe = true;
            }
        }

        GenericPoint leftUp = new GenericPoint(feetPos.x - OFFSETLEFTX, feetPos.y - OFFSETLEFTY);

        // alles Zeichnen
        g.drawImage(schaue_links[Zwinkern], leftUp.x + OFFSETLEFTHEADX, leftUp.y - OFFSETHEADY, null);

        // Body evaluieren
        if (fangen > 10 && rueckgabe) {
            // hier noch rumangeln
            if (fangen == 42 || fangen == 41 || fangen == 36 || fangen == 35 || fangen == 30 || fangen == 29 ||
                    fangen == 24 || fangen == 23 || fangen == 18 || fangen == 17 || fangen == 12 || fangen == 11) {
                g.drawImage(angle_links[1], leftUp.x, leftUp.y, null);
            } else {
                g.drawImage(angle_links[0], leftUp.x, leftUp.y, null);
            }
        } else {
            // gefangen
            switch (fangen) {
                case 4: // letzte Zappelphase
                case 5:
                case 6:
                    g.drawImage(angle_links[4], leftUp.x, leftUp.y, null);
                    break;

                case 7: // mittlere Zappelphase
                case 8:
                    g.drawImage(angle_links[3], leftUp.x, leftUp.y, null);
                    break;

                case 9: // erste Zappelphase
                case 10:
                    g.drawImage(angle_links[2], leftUp.x, leftUp.y, null);
                    break;

                default: // fisch hochhalten
                    g.drawImage(angle_links[5], leftUp.x, leftUp.y, null);
                    break;
            }
        }

        return rueckgabe;
    }

    // reden mit Fisch an der Angel
    public void RedeLinks(GenericDrawingContext g, GenericPoint feetPos, int TalkPerson) {
        if (--Verhinderhead < 1) {
            // Head 0, 2...8
            Verhinderhead = MAX_VERHINDERHEAD;
            Head = (int) (Math.random() * 7.9);
            Head++;
            if (Head == 1) {
                Head = 0;
            }
        }

        // wenn K nicht redet, dann auch kein Mundwackeln
        if (TalkPerson != 1 && TalkPerson != 3) {
            Head = 0;
        }

        GenericPoint leftUp = new GenericPoint(feetPos.x - OFFSETLEFTX, feetPos.y - OFFSETLEFTY);

        g.drawImage(rede_rechts[Head], leftUp.x + OFFSETLEFTHEADX, leftUp.y - OFFSETHEADY, null);
        g.drawImage(angle_links[5], leftUp.x, leftUp.y, null);
    }
}    