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

public class FarmerMichal extends MainAnim {
    private final GenericImage[] bur_work;
    private final GenericImage[] bur_talk;
    private GenericImage bur_look;

    public static final int Breite = 100;
    public static final int Hoehe = 60;

    private int Talk = 0;
    private int Verhindertalk;
    private static final int MAX_VERHINDERTALK = 2;

    private int Work = 0;
    private int Verhinderwork;
    private static final int MAX_VERHINDERWORK = 5;

    private int Listen = 0;

    public FarmerMichal(Start caller) {
        super(caller);

        bur_work = new GenericImage[6];
        bur_talk = new GenericImage[7];

        InitImages();

        Verhindertalk = MAX_VERHINDERTALK;
        Verhinderwork = MAX_VERHINDERWORK;
    }

    private void InitImages() {
        bur_work[0] = getPicture("gfx/polo/dmuz.png");
        bur_work[1] = getPicture("gfx/polo/dmuz-a.png");
        bur_work[2] = getPicture("gfx/polo/dmuz1.png");
        bur_work[3] = getPicture("gfx/polo/dmuz2.png");
        bur_work[4] = getPicture("gfx/polo/dmuz3.png");
        bur_work[5] = getPicture("gfx/polo/dmuz4.png");

        bur_talk[0] = getPicture("gfx/polo/dmuz-r1.png");
        bur_talk[1] = getPicture("gfx/polo/dmuz-r2.png");
        bur_talk[2] = getPicture("gfx/polo/dmuz-r3.png");
        bur_talk[3] = getPicture("gfx/polo/dmuz-r4.png");
        bur_talk[4] = getPicture("gfx/polo/dmuz-r5.png");
        bur_talk[5] = getPicture("gfx/polo/dmuz-r6.png");
        bur_talk[6] = getPicture("gfx/polo/dmuz-r7.png");

        bur_look = getPicture("gfx/polo/dmuz-b.png");
    }

    @Override
    public void cleanup() {
        bur_work[0] = null;
        bur_work[1] = null;
        bur_work[2] = null;
        bur_work[3] = null;
        bur_work[4] = null;
        bur_work[5] = null;

        bur_talk[0] = null;
        bur_talk[1] = null;
        bur_talk[2] = null;
        bur_talk[3] = null;
        bur_talk[4] = null;
        bur_talk[5] = null;
        bur_talk[6] = null;

        bur_look = null;
    }

    // Zeichne Michal, wie er dasteht oder spricht
    public void drawMichal(GenericDrawingContext offGraph, int TalkPerson, GenericPoint pos, boolean isListening) {
        // wenn Hanza spricht, schaut Michal auf sie
        if (TalkPerson == 29 && mainFrame.talkCount > 1) {
            offGraph.drawImage(bur_look, pos.x, pos.y);
            return;
        }

        // Michal spricht
        if (TalkPerson == 28 && mainFrame.talkCount > 1) {
            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                Talk = (int) (Math.random() * 6.9);
            }

            offGraph.drawImage(bur_talk[Talk], pos.x, pos.y);
            return;
        }

        // Michal hoert zu
        if (isListening) {
            if (Listen == 1) {
                Listen = 0;
            } else {
                int zuf = (int) (Math.random() * 50);
                if (zuf > 45) {
                    Listen = 1;
                }
            }

            offGraph.drawImage(Listen == 0 ? bur_talk[0] : bur_work[1], pos.x, pos.y);
            return;
        }

        // Michal arbeitet
        if (Work == 1) {
            // Zwinkern ausschalten
            Work = 0;
        } else {
            // Weiterarbeiten
            if (--Verhinderwork < 1) {
                Verhinderwork = MAX_VERHINDERWORK;

                switch (Work) {
                    case 0:
                        // auf 1. Arbeitsschritt
                        Work = 2;
                        break;
                    case 2: // 2. Arbeitsschritt
                        Work = 3;
                        break;
                    case 3: // Entscheidung, ob Weiterarbeiten oder von vorn
                        int zuffi = (int) (Math.random() * 50);
                        if (zuffi > 40) {
                            Work = 4;
                        } else {
                            Work = 0;
                        }
                        break;
                    case 4: // letzter Arbeitsschritt
                        if (!mainFrame.inventory.noBackgroundSound || !mainFrame.isInventoryCursor) {
                            mainFrame.soundPlayer.PlayFile("sfx/nepl.wav");
                        }
                        Work = 5;
                        break;
                    case 5: // Back to the Beginning
                        Work = 0;
                        break;
                }
            } else {
                // wenn erlaubt, dann Zwinkern evaluieren
                int zuff = (int) (Math.random() * 50);
                if (zuff > 45 && Work == 0) {
                    Work = 1;
                }
            }
        }

        offGraph.drawImage(bur_work[Work], pos.x, pos.y);

    }
}    