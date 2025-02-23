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
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class HlownyStraznik extends Mainanim {
    private final GenericImage[] straz_stand;
    private final GenericImage[] straz_talk;
    private final GenericImage[] straz_drink;
    private final GenericImage[] straz_sit;

    private GenericImage vorder;

    // generelle Flags
    private final boolean istCasnik;             // Flag, ob in Casnik oder in Kuchnja
    private boolean isSleeping;            // Flag, ob er schon schlaeft

    private boolean drink = false;
    private boolean sit = false;

    private static final GenericPoint casnikPoint1 = new GenericPoint(105, 229);
    private static final GenericPoint casnikPoint2 = new GenericPoint(97, 262);

    private int Drinkcount = 0;
    private int Sitcount = 0;

    private int Stand = 0;
    private int Sit = 0;
    private int Talk = 0;
    private int Drink = 0;

    private int Verhinderstand;
    private int Verhindertalk;
    // private int Verhinderdrink;

    private static final int MAX_VERHINDERSTAND = 50;
    private static final int MAX_VERHINDERTALK = 2;
    // private static final int MAX_VERHINDERDRINK = 5;

    public HlownyStraznik(Start caller, boolean istCasnik, boolean isSleeping) {
        super(caller);

        straz_stand = new GenericImage[4];
        straz_talk = new GenericImage[7];
        straz_drink = new GenericImage[3];
        straz_sit = new GenericImage[4];

        this.istCasnik = istCasnik;
        this.isSleeping = isSleeping;

        InitImages();

        Verhinderstand = MAX_VERHINDERSTAND;
        Verhindertalk = MAX_VERHINDERTALK;
        // Verhinderdrink = MAX_VERHINDERDRINK;
    }

    private void InitImages() {
        if (istCasnik) {
            straz_stand[0] = getPicture("gfx-dd/casnik/straznik1.png");
            straz_stand[1] = getPicture("gfx-dd/casnik/straznik1z.png");
            straz_stand[2] = getPicture("gfx-dd/casnik/straznik1a.png");
            straz_stand[3] = getPicture("gfx-dd/casnik/straznik1b.png");

            straz_talk[0] = straz_stand[0];
            straz_talk[1] = getPicture("gfx-dd/casnik/straznik1-t1.png");
            straz_talk[2] = getPicture("gfx-dd/casnik/straznik1-t2.png");
            straz_talk[3] = getPicture("gfx-dd/casnik/straznik1-t3.png");
            straz_talk[4] = getPicture("gfx-dd/casnik/straznik1-t4.png");
            straz_talk[5] = getPicture("gfx-dd/casnik/straznik1-t5.png");
            straz_talk[6] = getPicture("gfx-dd/casnik/straznik1-t6.png");

            straz_drink[0] = getPicture("gfx-dd/casnik/straznik1-w.png");
            straz_drink[1] = getPicture("gfx-dd/casnik/straznik1-w1.png");
            straz_drink[2] = getPicture("gfx-dd/casnik/straznik1-w2.png");

            straz_sit[0] = getPicture("gfx-dd/casnik/straznik1-s.png");
            straz_sit[1] = getPicture("gfx-dd/casnik/straznik2a.png");
            straz_sit[2] = getPicture("gfx-dd/casnik/straznik2b.png");
            straz_sit[3] = getPicture("gfx-dd/casnik/straznik2.png");

            vorder = getPicture("gfx-dd/casnik/cblido.png");
        }
    }

    @Override
    public void cleanup() {
        straz_stand[0] = null;
        straz_stand[1] = null;
        straz_stand[2] = null;
        straz_stand[3] = null;

        straz_talk[0] = null;
        straz_talk[1] = null;
        straz_talk[2] = null;
        straz_talk[3] = null;
        straz_talk[4] = null;
        straz_talk[5] = null;
        straz_talk[6] = null;

        straz_drink[0] = null;
        straz_drink[1] = null;
        straz_drink[2] = null;

        straz_sit[0] = null;
        straz_sit[1] = null;
        straz_sit[2] = null;
        straz_sit[3] = null;

        vorder = null;
    }

    // aktuelles (!!!) Rectangle zurueckgeben
    public Borderrect straznikRect(int TalkPerson) {
        // Hier unterscheiden, was gerade los ist
        if (TalkPerson == 48 && mainFrame.talkCount > 1) {
            // redet gerade, also RedeImages bearbeiten
            return new Borderrect(casnikPoint1.x, casnikPoint1.y,
                    casnikPoint1.x + straz_talk[Talk].getWidth(),
                    casnikPoint1.y + straz_talk[Talk].getHeight());
        }

        // trinkt
        if (drink) {
            // Trinkimages sind aktuell
            return new Borderrect(casnikPoint1.x, casnikPoint1.y,
                    casnikPoint1.x + straz_drink[Drink].getWidth(),
                    casnikPoint1.y + straz_drink[Drink].getHeight());
        }

        // setzt sich hin oder schlaeft
        if (sit || isSleeping) {
            // Hinsetzimages
            if (Sit == 0) {
                return new Borderrect(casnikPoint1.x, casnikPoint1.y,
                        casnikPoint1.x + straz_sit[Sit].getWidth(),
                        casnikPoint1.y + straz_sit[Sit].getHeight());
            } else {
                return new Borderrect(casnikPoint2.x, casnikPoint2.y,
                        casnikPoint2.x + straz_sit[Sit].getWidth(),
                        casnikPoint2.y + straz_sit[Sit].getHeight());
            }
        }

        // nix, also nur rumstehen
        return new Borderrect(casnikPoint1.x, casnikPoint1.y,
                casnikPoint1.x + straz_stand[Stand].getWidth(),
                casnikPoint1.y + straz_stand[Stand].getHeight());
    }

    // Figur weiterschalten, je nachdem, was gerade los ist
    public boolean evalStraznik(int TalkPerson, boolean trink, boolean schlafein, boolean hasClock) {
        // Variablen setzen
        if (trink) {
            drink = true;
        }
        if (schlafein) {
            sit = true;
        }
        boolean isDoing = true;

        // Reden
        if (TalkPerson == 48 && mainFrame.talkCount > 1) {
            // Reden weiterschalten
            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) (Math.random() * 6.9);
            }
            return false;
        }

        // trinken
        if (drink) {
            if (Drinkcount < 7) // hier wird angegeben, wielange Straznik Wein nimmt
            {
                // am Anfang Wein nehmen
                Drink = 0;
            } else {
                // Hier immer nur Nippen
                int welches = (Drinkcount - 10) % 6;
                if (welches > 3) {
                    Drink = 2;
                } else {
                    Drink = 1;
                }
            }

            // Anim weiterschalten und auf Beenden pruefen
            Drinkcount++;
            if (Drinkcount > 40) {
                Drinkcount = 0;
                drink = false;
                isDoing = false;
            }
            return isDoing;
        }

        // sitzen oder schlafen
        if (sit || isSleeping) {
            // wenn er schon schlaeft, dann nur so zeichnen
            if (isSleeping) {
                if (!hasClock) {
                    Sit = 2;
                } else {
                    Sit = 3;
                }
                return true;
            }

            // Hier die Hinsetzszene
            if (Sitcount < 3) {
                Sit = 0;
            } else {
                Sit = 1;
            }

            // Anim weiterschalten und auf Verlassen pruefen
            Sitcount++;
            if (Sitcount > 6) {
                Sitcount = 0;
                sit = false;
                isSleeping = true;
                isDoing = false;
            }
            return isDoing;
        }

        // nur rumstehen
        switch (Stand) {
            case 0:
                if (--Verhinderstand < 1) {
                    // Schwanken
                    Verhinderstand = MAX_VERHINDERSTAND;
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 25) {
                        Stand = 2;
                    } else {
                        Stand = 3;
                    }
                } else {
                    // Zwinkern
                    int zuff2 = (int) (Math.random() * 50);
                    if (zuff2 > 45) {
                        Stand = 1;
                    }
                }
                break;

            case 1:
                Stand = 0;
                break;

            case 2:
            case 3:
                if (--Verhinderstand < 1) {
                    // Schwanken
                    Verhinderstand = MAX_VERHINDERSTAND;
                    Stand = 0;
                }
                break;
        }

        return false;
    }

    // Zeichne Hauptwachter, wie er dasteht oder spricht
    public void drawStraznik(GenericDrawingContext offGraph, int TalkPerson) {
        // Unterschied Kuchnja/Casnik
        if (istCasnik) {
            // reden extra
            if (TalkPerson == 48 && mainFrame.talkCount > 1) {
                offGraph.drawImage(straz_talk[Talk], casnikPoint1.x, casnikPoint1.y);
                offGraph.drawImage(vorder, 121, 308);
                return;
            }

            // trinken
            if (drink) {
                offGraph.drawImage(straz_drink[Drink], casnikPoint1.x, casnikPoint1.y);
                offGraph.drawImage(vorder, 121, 308);
                return;
            }

            // hinsetzen
            if (sit || isSleeping) {
                if (Sit == 0) {
                    offGraph.drawImage(straz_sit[Sit], casnikPoint1.x, casnikPoint1.y);
                } else {
                    offGraph.drawImage(straz_sit[Sit], casnikPoint2.x, casnikPoint2.y);
                }
                offGraph.drawImage(vorder, 121, 308);
                return;
            }

            // nix von alledem, also steht er nur rum
            offGraph.drawImage(straz_stand[Stand], casnikPoint1.x, casnikPoint1.y);
            offGraph.drawImage(vorder, 121, 308);
        }
    }

    // Talkpoint zurueckgeben
    public GenericPoint evalTalkPoint() {
        if (sit && Sit == 0 || isSleeping) // hier schlaeft er
        {
            return new GenericPoint(casnikPoint2.x + 38, casnikPoint2.y - 120);
        } else {
            return new GenericPoint(casnikPoint1.x + 21, casnikPoint2.y - 50); // hier steht er
        }
    }
}    