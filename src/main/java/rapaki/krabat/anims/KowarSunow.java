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

package rapaki.krabat.anims;

import rapaki.krabat.Start;
import rapaki.krabat.main.Borderrect;
import rapaki.krabat.main.GenericPoint;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class KowarSunow extends Mainanim {
    private GenericImage[] kowar_head;
    private GenericImage[] kowar_body;
    private GenericImage[] kowar_work;
    private GenericImage[] kowar_walkl;
    private GenericImage[] kowar_walkr;
    private GenericImage Vorder, kowar_zwinker;

    private int Head = 1;
    private int Body = 1;
    private int Work = 1;
    private int Stand = 1;

    private int WalklCount = 1;
    private int WalkrCount = 1;

    private boolean Verhinderstrampeln = false;

    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 6;

    private int Verhinderpause;
    private static final int MAX_VERHINDERPAUSE = 7;

    private int Verhinderarbeit;
    private static final int MAX_VERHINDERARBEIT = 10;

    private int Verhindertalk;
    private static final int MAX_VERHINDERTALK = 2;

    private static final GenericPoint TalkPoint = new GenericPoint(1040, 189);
    private static final GenericPoint WorkPoint = new GenericPoint(1101, 179);

    public static final int Breite = 45;
    public static final int Hoehe = 90;

    private int TurnAnim = 1;
    private float Walkx;
    private float Walky;

    public KowarSunow(Start caller) {
        super(caller);

        kowar_head = new GenericImage[7];
        kowar_body = new GenericImage[6];
        kowar_work = new GenericImage[4];
        kowar_walkl = new GenericImage[4];
        kowar_walkr = new GenericImage[4];

        InitImages();

        Verhinderbody = MAX_VERHINDERBODY;
        Verhinderpause = MAX_VERHINDERPAUSE;
        Verhinderarbeit = MAX_VERHINDERARBEIT;
        Verhindertalk = MAX_VERHINDERTALK;

        Walkx = WorkPoint.x;
        Walky = WorkPoint.y;
    }

    private void InitImages() {
        kowar_head[1] = getPicture("gfx/wjes/kw-h1.gif");
        kowar_head[2] = getPicture("gfx/wjes/kw-h2.gif");
        kowar_head[3] = getPicture("gfx/wjes/kw-h3.gif");
        kowar_head[4] = getPicture("gfx/wjes/kw-h4.gif");
        kowar_head[5] = getPicture("gfx/wjes/kw-h5.gif");
        kowar_head[6] = getPicture("gfx/wjes/kw-h6.gif");

        kowar_body[1] = getPicture("gfx/wjes/kw-b1.gif");
        kowar_body[2] = getPicture("gfx/wjes/kw-b2.gif");
        kowar_body[3] = getPicture("gfx/wjes/kw-b3.gif");
        kowar_body[4] = getPicture("gfx/wjes/kw-b4.gif");
        kowar_body[5] = getPicture("gfx/wjes/kw-b5.gif");

        kowar_work[1] = getPicture("gfx/wjes/kw2.gif");
        kowar_work[2] = getPicture("gfx/wjes/kw3.gif");
        kowar_work[3] = getPicture("gfx/wjes/kw4.gif");

        kowar_walkl[1] = getPicture("gfx/wjes/kw5.gif");
        kowar_walkl[2] = getPicture("gfx/wjes/kw.gif");
        kowar_walkl[3] = getPicture("gfx/wjes/kw6.gif");

        kowar_walkr[1] = getPicture("gfx/wjes/kw5a.gif");
        kowar_walkr[2] = getPicture("gfx/wjes/kw1a.gif");
        kowar_walkr[3] = getPicture("gfx/wjes/kw6a.gif");

        kowar_zwinker = getPicture("gfx/wjes/kw1.gif");

        Vorder = getPicture("gfx/wjes/kowarnja.gif");
    }

    public void cleanup() {
        kowar_head[1] = null;
        kowar_head[2] = null;
        kowar_head[3] = null;
        kowar_head[4] = null;
        kowar_head[5] = null;
        kowar_head[6] = null;

        kowar_body[1] = null;
        kowar_body[2] = null;
        kowar_body[3] = null;
        kowar_body[4] = null;
        kowar_body[5] = null;

        kowar_work[1] = null;
        kowar_work[2] = null;
        kowar_work[3] = null;

        kowar_walkl[1] = null;
        kowar_walkl[2] = null;
        kowar_walkl[3] = null;

        kowar_walkr[1] = null;
        kowar_walkr[2] = null;
        kowar_walkr[3] = null;

        kowar_zwinker = null;

        Vorder = null;
    }

    // Zeichne Kolar, wie er dasteht oder spricht
    public void drawKowar(GenericDrawingContext g, int TalkPerson, boolean Listenflag, boolean noTalkSchmied) {
        // Schmied beim Reden
        if ((TalkPerson == 22) && (mainFrame.talkCount > 1) && (TurnAnim > 49)) {
            if ((--Verhindertalk) < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Head = (int) Math.round(Math.random() * 6);
                Head++;
                if (Head == 7) {
                    Head = 1;
                }
            }

            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;
                Body = (int) Math.round(Math.random() * 5);
                Body++;
                if (Body == 6) {
                    Body = 1;
                }
            }
            g.drawImage(kowar_head[Head], (int) Walkx, (int) Walky, null);
            g.drawImage(kowar_body[Body], (int) Walkx, (int) (Walky + 37), null);
            drawVorder(g);
        } else {
            if ((Listenflag == true) || (TurnAnim > 1)) {
                // zur Person hindrehen und rumstehen
                if (TurnAnim > 49) {
                    // nur Rumstehen und Zwinkern
                    if (Stand == 2) {
                        Stand = 1;
                    } else {
                        int zuffi = (int) Math.round(Math.random() * 50);
                        if (zuffi > 45) {
                            Stand = 2;
                        }
                    }
                    if (Stand == 1) {
                        g.drawImage(kowar_walkl[2], (int) Walkx, (int) Walky, null);
                    }
                    if (Stand == 2) {
                        g.drawImage(kowar_zwinker, (int) Walkx, (int) Walky, null);
                    }
                    drawVorder(g);

                    // Hier zurueckschalten auf Zuruecklaufen
                    if (Listenflag == false) {
                        TurnAnim = 10;
                    }
                } else {
                    // aufhoeren zu arbeiten und Umdrehen (wie auch immer)
                    switch (TurnAnim) {
                        case 1: // Pausenbild zeichnen
                            TurnAnim++;
                            g.drawImage(kowar_work[1], (int) Walkx, (int) Walky, null);
                            drawVorder(g);
                            WalklCount = 1;
                            Walkx -= 12;
                            Verhinderstrampeln = false;
                            break;

                        case 2: // Umdrehen und zum Zielpunkt laufen

                            // Punkt bewegen
                            Verhinderstrampeln = !(Verhinderstrampeln);
                            if (Verhinderstrampeln == false) {
                                float Verhaeltnis = Math.abs(Walky - TalkPoint.y) / Math.abs(Walkx - TalkPoint.x);

                                // System.out.println ("Verhaeltnis = " + Verhaeltnis);

                                if ((WalklCount == 1) || (WalklCount == 3)) {
                                    // Grosser Schritt
                                    Walkx -= 8;
                                    Walky += 8.0f * Verhaeltnis;
                                } else {
                                    // kleiner Schritt
                                    Walkx -= 2;
                                    Walky += 2.0f * Verhaeltnis;
                                }

                                WalklCount++;
                                if (WalklCount == 5) {
                                    WalklCount = 1;
                                }
                            }

                            // Auf "ich habe fertig" kontrollieren
                            if ((Walkx <= TalkPoint.x) || (Walky >= TalkPoint.y)) {
                                TurnAnim++;
                            }
                            // if ((Math.abs (Walkx - TalkPoint.x) < 5) || (Math.abs (Walky - TalkPoint.y) < ((int) (5.0f * Verhaeltnis)))) TurnAnim++;

                            // System.out.println ("Animphase : " + WalklCount + " Xkoord " + Walkx + " Ykoord " + Walky);

                            // Kolar malen
                            g.drawImage(kowar_walkl[((WalklCount == 4) ? 2 : WalklCount)], (int) Walkx, (int) Walky, null);
                            drawVorder(g);
                            break;

                        case 3: // als Umgedreht markieren und Skip ermoeglichen
                            TurnAnim = 50;
                            g.drawImage(kowar_walkl[2], (int) Walkx, (int) Walky, null);
                            drawVorder(g);
                            break;

                        case 10: // Zurueck umdrehen
                            TurnAnim++;
                            // Korrektur des gespiegelten Images
                            g.drawImage(kowar_walkl[2], (int) Walkx, (int) Walky, null);
                            drawVorder(g);
                            Walkx += 12;
                            WalkrCount = 1;
                            Verhinderstrampeln = false;
                            break;

                        case 11: // Umdrehen und zum Zielpunkt laufen

                            // Punkt bewegen
                            Verhinderstrampeln = !(Verhinderstrampeln);
                            if (Verhinderstrampeln == false) {
                                float Verhaeltnis = Math.abs(Walky - WorkPoint.y) / Math.abs(Walkx - WorkPoint.x);

                                // System.out.println ("Verhaeltnis = " + Verhaeltnis);

                                if ((WalkrCount == 1) || (WalkrCount == 3)) {
                                    // Grosser Schritt
                                    Walkx += 8;
                                    Walky -= 8.0f * Verhaeltnis;
                                } else {
                                    // kleiner Schritt
                                    Walkx += 2;
                                    Walky -= 2.0f * Verhaeltnis;
                                }

                                WalkrCount++;
                                if (WalkrCount == 5) {
                                    WalkrCount = 1;
                                }
                            }

                            // Auf "ich habe fertig" kontrollieren
                            if ((Walkx >= WorkPoint.x) || (Walky <= WorkPoint.y)) {
                                TurnAnim++;
                            }
                            // if ((Math.abs (Walkx - WorkPoint.x) < 5) || (Math.abs (Walky - WorkPoint.y) < ((int) (5.0f * Verhaeltnis)))) TurnAnim++;

                            // System.out.println ("Animphase : " + WalkrCount + " Xkoord " + Walkx + " Ykoord " + Walky);

                            // Kolar malen
                            g.drawImage(kowar_walkr[((WalkrCount == 4) ? 2 : WalkrCount)], (int) Walkx, (int) Walky, null);
                            drawVorder(g);
                            break;

                        case 12: // als Umgedreht markieren und Skip ermoeglichen
                            TurnAnim = 1;
                            g.drawImage(kowar_work[1], (int) Walkx, (int) Walky, null);
                            drawVorder(g);
                            break;
                    }
                }
            }

            // arbeiten mit Pause ab und zu
            else {
                switch (Work) {
                    case 1: // Pause
                        if ((--Verhinderpause) < 1) {
                            Verhinderpause = MAX_VERHINDERPAUSE;
                            Work = 2;
                        }
                        break;

                    case 2: // 1. Arbeitsschritt
                        if ((--Verhinderarbeit) < 1) {
                            if (noTalkSchmied == false) {
                                evalSound();
                            }
                            Verhinderarbeit = MAX_VERHINDERARBEIT / 2;
                            Work = 3;
                        }
                        break;

                    case 3: // 2. Arbeitsschritt, Entscheidung weiter oder Pause
                        if ((--Verhinderarbeit) < 1) {

                            Verhinderarbeit = MAX_VERHINDERARBEIT;
                            int zf = (int) Math.round(Math.random() * 50);
                            if (zf > 25)  // hier die Wahrscheinlichkeit Pause oder Arbeit
                            {
                                Work = 1;
                            } else {
                                Work = 2;
                            }
                        }
                        break;
                }
                g.drawImage(kowar_work[Work], (int) Walkx, (int) Walky, null);
                drawVorder(g);
            }
        }
    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        mainFrame.wave.PlayFile("sfx/amboss.wav");

    }

    private void drawVorder(GenericDrawingContext g) {
        g.drawImage(Vorder, 1083, 193, null);
    }

    public Borderrect schmiedRect() {
        return (new Borderrect((int) Walkx, (int) Walky, ((int) Walkx) + Breite, ((int) Walky + Hoehe)));
    }
}    