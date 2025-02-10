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
import rapaki.krabat.main.GenericPoint;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;

public class Dundak extends Mainanim {
    private GenericImage[] hosc_stand;
    private GenericImage[] hosc_head;
    private GenericImage[] hosc_body;

    public static final int Breite = 107;
    public static final int Hoehe = 110;

    private int Stand = 0;
    private int Verhinderstand;
    private static final int MAX_VERHINDERSTAND = 90;

    private int Head = 0;
    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;

    private int Body = 0;
    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;

    private static final int BODYOFFSET = 35;

    public Dundak(Start caller) {
        super(caller);

        hosc_stand = new GenericImage[4];
        hosc_head = new GenericImage[7];
        hosc_body = new GenericImage[4];

        Verhinderstand = MAX_VERHINDERSTAND;
        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;

        InitImages();
    }

    private void InitImages() {
        hosc_stand[0] = getPicture("gfx/hoscenc/gast2-1.gif");
        hosc_stand[1] = getPicture("gfx/hoscenc/gast2-1a.gif");
        hosc_stand[2] = getPicture("gfx/hoscenc/gast2-1b.gif");
        hosc_stand[3] = getPicture("gfx/hoscenc/gast2-2.gif");

        hosc_head[0] = getPicture("gfx/hoscenc/gast2-h1.gif");
        hosc_head[1] = getPicture("gfx/hoscenc/gast2-h2.gif");
        hosc_head[2] = getPicture("gfx/hoscenc/gast2-h3.gif");
        hosc_head[3] = getPicture("gfx/hoscenc/gast2-h4.gif");
        hosc_head[4] = getPicture("gfx/hoscenc/gast2-h5.gif");
        hosc_head[5] = getPicture("gfx/hoscenc/gast2-h6.gif");
        hosc_head[6] = getPicture("gfx/hoscenc/gast2-h7.gif");

        hosc_body[0] = getPicture("gfx/hoscenc/gast2-b1.gif");
        hosc_body[1] = getPicture("gfx/hoscenc/gast2-b2.gif");
        hosc_body[2] = getPicture("gfx/hoscenc/gast2-b3.gif");
        hosc_body[3] = getPicture("gfx/hoscenc/gast2-b4.gif");
    }

    public void cleanup() {
        hosc_stand[0] = null;
        hosc_stand[1] = null;
        hosc_stand[2] = null;
        hosc_stand[3] = null;

        hosc_head[0] = null;
        hosc_head[1] = null;
        hosc_head[2] = null;
        hosc_head[3] = null;
        hosc_head[4] = null;
        hosc_head[5] = null;
        hosc_head[6] = null;

        hosc_body[0] = null;
        hosc_body[1] = null;
        hosc_body[2] = null;
        hosc_body[3] = null;
    }

    // Zeichne Dundak, wie er dasteht oder spricht
    public void drawDundak(GenericDrawingContext g, int TalkPerson, GenericPoint Posit, int noSound) {
        // beim Reden
        if ((TalkPerson == 24) && (mainFrame.talkCount > 1)) {
            // Head evaluieren
            if ((--Verhinderhead) < 1) {
                Verhinderhead = MAX_VERHINDERHEAD;
                Head = (int) (Math.random() * 6.9);
            }

            // Body evaluieren
            if ((--Verhinderbody) < 1) {
                Verhinderbody = MAX_VERHINDERBODY;
                Body = (int) (Math.random() * 3.9);
            }

            // Rumsitz-anims ruecksetzen
            Stand = 0;

            g.drawImage(hosc_head[Head], Posit.x, Posit.y, null);
            g.drawImage(hosc_body[Body], Posit.x, Posit.y + BODYOFFSET, null);

        } else {
            // beim Rumstehen
            if ((--Verhinderstand) < 1) {
                Verhinderstand = MAX_VERHINDERSTAND;
                Stand++;
                if (Stand == 4) {
                    Stand = 0;
                    if (noSound < 1) {
                        evalSound();
                    }
                }

                // Extrawurst -> Zwinkern nicht so lange
                if (Stand == 1) {
                    Verhinderstand = 2;
                }
                if (Stand == 3) {
                    Verhinderstand = ((int) ((Math.random() * 30) + 10));
                }
            }

            g.drawImage(hosc_stand[Stand], Posit.x, Posit.y, null);
        }
    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        // wenn sonstige Blockaden noetig, dann mit wave.noBackground checken!!!

        if ((mainFrame.inventory.noBackgroundSound == true) && (mainFrame.invCursor == true)) {
            return; // bei Problemen mit dem Soundsystem zurueckspringen
        }

        mainFrame.wave.PlayFile("sfx/becher.wav");

    }

}    