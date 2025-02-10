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

package de.codengine.anims;

import de.codengine.Start;
import de.codengine.main.GenericPoint;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Kocka extends Mainanim {
    private final GenericImage[] kocka_look;
    private final GenericImage[] kocka_talk;

    public static final int Breite = 63;
    public static final int Hoehe = 47;

    private int Look = 0;
    private int Talk = 0;

    private int Lecken;

    private int Verhinderlook;
    private int Verhindertalk;
    private int Verhinderschwanzwedel;
    private int Verhinderpfote;

    private static final int MAX_VERHINDERTALK = 2;
    private static final int MAX_VERHINDERLOOK = 100;
    private static final int MAX_VERHINDERSCHWANZWEDEL = 3;
    private static final int MAX_VERHINDERPFOTE = 3;

    private static final int MAX_LECKEN = 6;

    private boolean forward = true;

    public Kocka(Start caller) {
        super(caller);

        kocka_look = new GenericImage[6];
        kocka_talk = new GenericImage[6];

        InitImages();

        Verhinderlook = MAX_VERHINDERLOOK;
        Verhindertalk = MAX_VERHINDERTALK;
        Verhinderschwanzwedel = MAX_VERHINDERSCHWANZWEDEL;
        Verhinderpfote = MAX_VERHINDERPFOTE;

        Lecken = MAX_LECKEN;
    }

    private void InitImages() {
        kocka_look[0] = getPicture("gfx/mlyn/kocka2.gif");
        kocka_look[1] = getPicture("gfx/mlyn/kocka2a.gif");
        kocka_look[2] = getPicture("gfx/mlyn/kocka1.gif");
        kocka_look[3] = getPicture("gfx/mlyn/kocka1a.gif");
        kocka_look[4] = getPicture("gfx/mlyn/kocka3.gif");
        kocka_look[5] = getPicture("gfx/mlyn/kocka4.gif");

        kocka_talk[0] = getPicture("gfx/mlyn/kocka-t1.gif");
        kocka_talk[1] = getPicture("gfx/mlyn/kocka-t2.gif");
        kocka_talk[2] = getPicture("gfx/mlyn/kocka-t3.gif");
        kocka_talk[3] = getPicture("gfx/mlyn/kocka-t4.gif");
        kocka_talk[4] = getPicture("gfx/mlyn/kocka-t5.gif");
        kocka_talk[5] = getPicture("gfx/mlyn/kocka-t6.gif");
    }

    @Override
    public void cleanup() {
        kocka_look[0] = null;
        kocka_look[1] = null;
        kocka_look[2] = null;
        kocka_look[3] = null;
        kocka_look[4] = null;
        kocka_look[5] = null;

        kocka_talk[0] = null;
        kocka_talk[1] = null;
        kocka_talk[2] = null;
        kocka_talk[3] = null;
        kocka_talk[4] = null;
        kocka_talk[5] = null;
    }

    // Zeichne Mutter, wie sie dasteht oder spricht
    public void drawKocka(GenericDrawingContext offGraph, int TalkPerson, GenericPoint pos) {
        if ((TalkPerson == 59) && (mainFrame.talkCount > 1)) {
            // Katze beim Reden, einfach nur Switchen
            // evaluieren
            if ((--Verhindertalk) < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                // Extrawurst, so dass die gehobene Pfote laenger oben bleibt
                if ((Talk == 1) || (Talk == 3)) {
                    // Testetn, ob die Pfote lange genug oben war
                    if ((--Verhinderpfote) < 1) {
                        Verhinderpfote = MAX_VERHINDERPFOTE;
                        Talk = (int) (Math.random() * 5.9);
                    } else {
                        // nur die beiden Pfotenimages eval.
                        if (Talk == 1) {
                            Talk = 3;
                        } else {
                            Talk = 1;
                        }
                    }
                } else {
                    Talk = (int) (Math.random() * 5.9);
                }
            }

            // Zeichnen und gut
            offGraph.drawImage(kocka_talk[Talk], pos.x, pos.y, null);
        } else {
            // Katze haengt rum, also Zwinkern, Pfote lecken und Schwanz wedeln berechnen

            if (Look < 2) {
                // normal, also schauen, ob eine Anim dran waere
                if ((--Verhinderlook) < 1) {
                    Verhinderlook = MAX_VERHINDERLOOK;

                    // Jetzt eine Anim aussuchen
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 25) {
                        Look = 2; // lecken
                    } else {
                        Look = 4;            // wedeln
                    }
                } else {
                    // keine Anim, also das Zwinkern berechnen
                    if (Look == 1) {
                        Look = 0;
                    } else {
                        int zuff2 = (int) (Math.random() * 50);
                        if (zuff2 > 45) {
                            Look = 1;
                        }
                    }
                }
            } else {
                // es laeuft gerade eine Anim
                // Leckanim testen
                if ((Look == 2) || (Look == 3)) {
                    // solange tun tun, bis Counter beendet
                    if ((--Lecken) < 1) {
                        // reset und zurueck auf normal
                        Lecken = MAX_LECKEN;
                        Look = 0;
                    } else {
                        // switchen
                        if (Look == 2) {
                            Look = 3;
                        } else {
                            Look = 2;
                        }
                    }
                } else {
                    // diese Anim zeitverzoegert
                    if ((--Verhinderschwanzwedel) < 1) {
                        Verhinderschwanzwedel = MAX_VERHINDERSCHWANZWEDEL;

                        // Schwanzwackelanim
                        if (Look == 4) {
                            // schauen, ob vor oder Ende
                            if (forward) {
                                Look = 5;
                            } else {
                                Look = 0;
                            }
                            forward = !(forward);
                        } else if (Look == 5) {
                            Look = 4;
                        }
                    }
                }
            }

            // Katze nun auch zeichnen
            offGraph.drawImage(kocka_look[Look], pos.x, pos.y, null);
        }
    }
}    