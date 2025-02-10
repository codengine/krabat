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
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class BurRalbicy extends Mainanim {
    private final GenericImage[] bur_head;
    private final GenericImage[] bur_body;
    private final GenericImage[] bur_work;

    private int Head = 1;
    private int Body = 1;
    private int Work = 1;

    private int Pausenlaenge;
    private static final int MAX_PAUSENLAENGE = 10;

    private int Arbeitspause;
    private static final int MAX_ARBEITSPAUSE = 12;

    private int Zwinkerpause;
    private static final int MAX_ZWINKERPAUSE = 4;

    private int Kopfkratzpause;
    private static final int MAX_KOPFKRATZPAUSE = 5;

    private int Halbzupause;
    private static final int MAX_HALBZUPAUSE = 3;

    private int Redepause;
    private static final int MAX_REDEPAUSE = 2;

    private int Bodypause;
    private static final int MAX_BODYPAUSE = 6;

    private int Verhinderarbeit;
    private static final int MAX_VERHINDERARBEIT = 5;

    public BurRalbicy(Start caller) {
        super(caller);

        bur_head = new GenericImage[8];
        bur_body = new GenericImage[7];
        bur_work = new GenericImage[9];

        InitImages();

        Pausenlaenge = MAX_PAUSENLAENGE;
        Arbeitspause = MAX_ARBEITSPAUSE;
        Zwinkerpause = MAX_ZWINKERPAUSE;
        Kopfkratzpause = MAX_KOPFKRATZPAUSE;
        Halbzupause = MAX_HALBZUPAUSE;
        Redepause = MAX_REDEPAUSE;
        Bodypause = MAX_BODYPAUSE;
        Verhinderarbeit = MAX_VERHINDERARBEIT;

    }

    private void InitImages() {

        bur_head[1] = getPicture("gfx/ralbicy/b-k1.gif");
        bur_head[2] = getPicture("gfx/ralbicy/b-k2.gif");
        bur_head[3] = getPicture("gfx/ralbicy/b-k3.gif");
        bur_head[4] = getPicture("gfx/ralbicy/b-k4.gif");
        bur_head[5] = getPicture("gfx/ralbicy/b-k5.gif");
        bur_head[6] = getPicture("gfx/ralbicy/b-k6.gif");
        bur_head[7] = getPicture("gfx/ralbicy/b-k7.gif");

        bur_body[1] = getPicture("gfx/ralbicy/b-b1.gif");
        bur_body[2] = getPicture("gfx/ralbicy/b-b2.gif");
        bur_body[3] = getPicture("gfx/ralbicy/b-b3.gif");
        bur_body[4] = getPicture("gfx/ralbicy/b-b4.gif");
        bur_body[5] = getPicture("gfx/ralbicy/b-b5.gif");
        bur_body[6] = getPicture("gfx/ralbicy/b-b6.gif");

        bur_work[1] = getPicture("gfx/ralbicy/b-1.gif");
        bur_work[2] = getPicture("gfx/ralbicy/b-2.gif");
        bur_work[3] = getPicture("gfx/ralbicy/b-3.gif");
        bur_work[4] = getPicture("gfx/ralbicy/b-4.gif");
        bur_work[5] = getPicture("gfx/ralbicy/b-5.gif");
        bur_work[6] = getPicture("gfx/ralbicy/b-6.gif");
        bur_work[7] = getPicture("gfx/ralbicy/b-7.gif");
        bur_work[8] = getPicture("gfx/ralbicy/b-8.gif");
    }

    @Override
    public void cleanup() {
        bur_head[1] = null;
        bur_head[2] = null;
        bur_head[3] = null;
        bur_head[4] = null;
        bur_head[5] = null;
        bur_head[6] = null;
        bur_head[7] = null;

        bur_body[1] = null;
        bur_body[2] = null;
        bur_body[3] = null;
        bur_body[4] = null;
        bur_body[5] = null;
        bur_body[6] = null;

        bur_work[1] = null;
        bur_work[2] = null;
        bur_work[3] = null;
        bur_work[4] = null;
        bur_work[5] = null;
        bur_work[6] = null;
        bur_work[7] = null;
        bur_work[8] = null;
    }

    // Zeichne Bauern, wie er dasteht oder spricht
    public void drawBur(GenericDrawingContext g, int TalkPerson, boolean Listenflag, boolean noSoundFromBauer) {
        int zuffi;

        // Reden - Animationen
        if ((TalkPerson == 21) && (mainFrame.talkCount > 1)) {
            if ((--Bodypause) < 1) {
                Bodypause = MAX_BODYPAUSE;
                // Neuen Body auswaehlen
                zuffi = (int) Math.round(Math.random() * 60);
                if ((zuffi > 9) && (zuffi < 16)) {
                    Body = zuffi - 9;
                }
            }

            if ((--Redepause) < 1) {
                Redepause = MAX_REDEPAUSE;
                // Neuen Kopf auswaehlen
                zuffi = (int) Math.round(Math.random() * 7);
                zuffi++;
                if (zuffi < 8) {
                    Head = zuffi;
                }
            }

            // Beim Reden wird anderer Bur gezeichnet als beim Zuhoeren und Arbeiten
            g.drawImage(bur_head[Head], 60, 227, null);
            g.drawImage(bur_body[Body], 60, 241, null);
        } else {
            // Arbeiten oder zuhoeren

            switch (Work) {
                case 1: // Arbeitsphase
                    if ((--Arbeitspause) < 1) {
                        Work = 2;
                        Arbeitspause = MAX_ARBEITSPAUSE;
                    }
                    break;

                case 2: // Arbeitsphase
                    if ((--Arbeitspause) < 1) {
                        if (!noSoundFromBauer) {
                            evalSound(2);
                        }
                        Work = 3;
                        Arbeitspause = MAX_ARBEITSPAUSE;
                    }
                    break;

                case 3: // Arbeitsphase, Entscheidung Weiterarbeiten oder Pause
                    if (((--Arbeitspause) < 1) && ((--Verhinderarbeit) < 1)) {
                        zuffi = (int) Math.round(Math.random() * 20);
                        if ((zuffi < 10) && (!Listenflag)) {
                            Work = 1;
                            if (!noSoundFromBauer) {
                                evalSound(1);
                            }
                        } else {
                            Work = 8;
                        }
                        Arbeitspause = MAX_ARBEITSPAUSE;
                        Verhinderarbeit = MAX_VERHINDERARBEIT;
                    }
                    break;

                case 4: // Irgendeine Pausenanim(1)
                    if ((--Zwinkerpause) < 1) {
                        Work = 7;
                        Zwinkerpause = MAX_ZWINKERPAUSE;
                    }
                    break;

                case 5: // noch eine Pausenanim(2)
                    if ((--Kopfkratzpause) < 1) {
                        Work = 8;
                        Kopfkratzpause = MAX_KOPFKRATZPAUSE;
                    }
                    break;

                case 6: // Pausenanim(3), Entscheidung, wo weiter
                    if ((--Halbzupause) < 1) {
                        zuffi = (int) Math.round(Math.random() * 20);
                        if (zuffi < 10) {
                            // Animfolge 4 - 7 einleiten
                            Work = 4;
                        } else {
                            // Back to the 8
                            Work = 8;
                        }
                        Halbzupause = MAX_HALBZUPAUSE;
                    }
                    break;

                case 7: // Pausenanim(1)
                    if ((--Zwinkerpause) < 1) {
                        Work = 8;
                        Zwinkerpause = MAX_ZWINKERPAUSE;
                    }
                    break;

                case 8: // Pausenanim (3), hier grosse Entscheidungen
                    if ((--Pausenlaenge) < 1) {
                        zuffi = (int) Math.round(Math.random() * 100);
                        if ((zuffi < 10) && (!Listenflag)) {
                            Work = 1;
                            if (!noSoundFromBauer) {
                                evalSound(1);
                            }
                        }
                        if ((zuffi > 9) && (zuffi < 20) && (!Listenflag)) {
                            Work = 5;
                        }
                        if ((zuffi > 19) && (zuffi < 30)) {
                            Work = 6;
                        }
                        Pausenlaenge = 1;
                    }
                    if (Work != 8) {
                        Pausenlaenge = MAX_PAUSENLAENGE;
                    }
                    // System.out.println ("Pause = " + Pausenlaenge);
                    break;
            }
            // System.out.println ("Phase " + Work);

            g.drawImage(bur_work[Work], (Work == 2) ? 62 : 60, (Work == 2) ? 223 : 227, null);
        }
    }

    private void evalSound(int welcher) {
        // zufaellig wavs fuer Geschnatter abspielen...
        mainFrame.wave.PlayFile("sfx/stroh" + (char) (welcher + 48) + ".wav");
    }
}    