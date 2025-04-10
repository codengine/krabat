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

public class Dziwadzelnica extends MainAnim {
    private final GenericImage[] dziw_talk;
    private final GenericImage[] dziw_beat;

    public static final int Breite = 110;
    public static final int Hoehe = 238;

    private int Scream = 0;
    private int Beat = 0;

    private int Verhinderscream;
    private int Verhinderbeat;

    private static final int MAX_VERHINDERSCREAM = 2;
    private static final int MAX_VERHINDERBEAT = 2;

    public Dziwadzelnica(Start caller) {
        super(caller);

        dziw_talk = new GenericImage[9];
        dziw_beat = new GenericImage[3];

        InitImages();

        Verhinderscream = MAX_VERHINDERSCREAM;
        Verhinderbeat = MAX_VERHINDERBEAT;
    }

    private void InitImages() {
        dziw_talk[0] = getPicture("gfx-dd/spaniska/dziwa2.png");
        dziw_talk[1] = getPicture("gfx-dd/spaniska/dziwa2a.png");
        dziw_talk[2] = getPicture("gfx-dd/spaniska/dziwa2b.png");
        dziw_talk[3] = getPicture("gfx-dd/spaniska/dziwa2c.png");
        dziw_talk[4] = getPicture("gfx-dd/spaniska/dziwa2d.png");
        dziw_talk[5] = getPicture("gfx-dd/spaniska/dziwa2e.png");
        dziw_talk[6] = getPicture("gfx-dd/spaniska/dziwa2f.png");
        dziw_talk[7] = getPicture("gfx-dd/spaniska/dziwa2g.png");
        dziw_talk[8] = getPicture("gfx-dd/spaniska/dziwa2h.png");

        dziw_beat[0] = getPicture("gfx-dd/spaniska/dziwa1.png");
        dziw_beat[1] = getPicture("gfx-dd/spaniska/dziwa1a.png");
        dziw_beat[2] = getPicture("gfx-dd/spaniska/dziwa1b.png");
    }

    @Override
    public void cleanup() {
        dziw_talk[0] = null;
        dziw_talk[1] = null;
        dziw_talk[2] = null;
        dziw_talk[3] = null;
        dziw_talk[4] = null;
        dziw_talk[5] = null;
        dziw_talk[6] = null;
        dziw_talk[7] = null;
        dziw_talk[8] = null;

        dziw_beat[0] = null;
        dziw_beat[1] = null;
        dziw_beat[2] = null;
    }

    // Zeichne Schauspielerin, wie er dasteht oder spricht
    public boolean drawDziwadzelnica(GenericDrawingContext offGraph, int TalkPerson, boolean isBeating, int AnimTalkPerson, GenericPoint pos) {
        // Dziw beim Schlagen -> Extra
        if (isBeating) {
            // Weiterschalten
            if (--Verhinderbeat < 1) {
                Verhinderbeat = MAX_VERHINDERBEAT;
                Beat++;
                if (Beat == 2) {
                    mainFrame.soundPlayer.PlayFile("sfx-dd/dyr.wav");
                }
            }

            // Zeichnen
            offGraph.drawImage(dziw_beat[Beat > 2 ? 0 : Beat], pos.x, pos.y);

            // rueckgabe = false, wenn sie fertig gehauen hat
            return Beat <= 2;
        } else {
            // Dziw beim Sprechen
            if (TalkPerson == 58 && mainFrame.talkCount > 1 || AnimTalkPerson == 58) {
                // Weiterschalten
                if (--Verhinderscream < 1) {
                    Verhinderscream = MAX_VERHINDERSCREAM;
                    Scream = (int) (Math.random() * 8.9);
                }
            } else {
                // steht nur rum
                if (Scream > 0) {
                    Scream = 0;
                } else {
                    int zf = (int) (Math.random() * 50);
                    if (zf > 45) {
                        Scream = 1;
                    }
                }
            }

            // Zeichnen
            offGraph.drawImage(dziw_talk[Scream], pos.x, pos.y);

            return true; // hier bedeutungslos
        }
    }
}    