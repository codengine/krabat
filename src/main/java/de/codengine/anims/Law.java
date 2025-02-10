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

public class Law extends Mainanim {
    private final GenericImage[] law;
    private final GenericImage[] law_talk;
    private final GenericImage[] law_sleep;
    private GenericImage law_grrr;

    public static final int Breite = 60;
    public static final int Hoehe = 37;

    private int Talk = 0;
    private int Sleep = 0;
    private int Zwinker = 0;

    private int Verhindertalk;
    private int Verhindersleep;

    private static final int MAX_VERHINDERTALK = 2;
    private static final int MAX_VERHINDERSLEEP = 20;

    public Law(Start caller) {
        super(caller);

        law = new GenericImage[2];
        law_talk = new GenericImage[7];
        law_sleep = new GenericImage[2];

        InitImages();

        Verhindertalk = MAX_VERHINDERTALK;
        Verhindersleep = MAX_VERHINDERSLEEP;
    }

    private void InitImages() {
        law[0] = getPicture("gfx-dd/manega/law.gif");
        law[1] = getPicture("gfx-dd/manega/lawa.gif");

        law_talk[0] = getPicture("gfx-dd/manega/law-t.gif");
        law_talk[1] = getPicture("gfx-dd/manega/law-ta.gif");
        law_talk[2] = getPicture("gfx-dd/manega/law-t1.gif");
        law_talk[3] = getPicture("gfx-dd/manega/law-t2.gif");
        law_talk[4] = getPicture("gfx-dd/manega/law-t3.gif");
        law_talk[5] = getPicture("gfx-dd/manega/law-t4.gif");
        law_talk[6] = getPicture("gfx-dd/manega/law-t5.gif");

        law_sleep[0] = getPicture("gfx-dd/manega/law-sleep1.gif");
        law_sleep[1] = getPicture("gfx-dd/manega/law-sleep2.gif");

        law_grrr = getPicture("gfx-dd/manega/law-grrr.gif");
    }

    // Zeichne Loewen, wie er dasteht oder spricht
    public void drawLaw(GenericDrawingContext offGraph, int TalkPerson, GenericPoint Posit, int AnimTalkPerson, boolean isListening) {
        // Talkpersonsverteilung: 68 normal sprechen
        //                        70 grrrrr

        // oberste Prio hat das Reden, zuerst normal
        if ((TalkPerson == 68) && (mainFrame.talkCount > 1)) {
            // weiterschalten
            if ((--Verhindertalk) < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) ((Math.random() * 4.9) + 2);
            }

            // Zeichnen
            offGraph.drawImage(law_talk[Talk], Posit.x, Posit.y, null);

            return;
        }

        // hier das Reden grrr...
        if ((TalkPerson == 70) && (mainFrame.talkCount > 1)) {
            // nur ein statisches Image
            offGraph.drawImage(law_grrr, Posit.x, Posit.y, null);

            return;
        }

        // nun das Reden in der AnimTalkPerson ( == schlafen)
        if (AnimTalkPerson == 68) {
            // Weiterschalten
            if ((--Verhindersleep) < 1) {
                Verhindersleep = MAX_VERHINDERSLEEP;
                if (Sleep == 0) {
                    Sleep = 1;
                } else {
                    Sleep = 0;
                    evalSound();
                }
            }

            // Zeichnen
            offGraph.drawImage(law_sleep[Sleep], Posit.x, Posit.y, null);

            return;
        }

        // zuhoeren, wenn Flag gesetzt
        if (isListening) {
            // Zwinkern eval.
            if (Talk > 0) {
                Talk = 0;
            } else {
                int zuff = (int) (Math.random() * 50);
                if (zuff > 45) {
                    Talk = 1;
                }
            }

            // Zeichnen
            offGraph.drawImage(law_talk[Talk], Posit.x, Posit.y, null);

            return;
        }

        // alles andere ist weg, also haengt er hier nur normal rum
        if (Zwinker > 0) {
            Zwinker = 0;
        } else {
            int zf = (int) (Math.random() * 50);
            if (zf > 45) {
                Zwinker = 1;
            }
        }

        // Zeichnen und ende
        offGraph.drawImage(law[Zwinker], Posit.x, Posit.y, null);

    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        // immer spielen, kommt eh nur selten
        mainFrame.wave.PlayFile("sfx-dd/lawspi.wav");
    }


}    