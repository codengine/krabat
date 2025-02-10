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

public class Dinglinger extends Mainanim {
    private GenericImage[] dingl_sit_body;
    private GenericImage[] dingl_sit_waithead;
    private GenericImage[] dingl_sit_talkhead;

    private int Wait = 0;
    private int Talkhead = 0;
    private int Talkbody = 0;

    private int Verhindertalkhead;
    private int Verhindertalkbody;
    private int Verhinderwait;

    private static final int MAX_VERHINDERTALKHEAD = 2;
    private static final int MAX_VERHINDERTALKBODY = 8;
    private static final int MAX_VERHINDERWAIT = 40;

    private static final GenericPoint Posit = new GenericPoint(41, 264);

    private static final int BODYOFFSET = 47;

    private int AnimCounter = 0;

    public static final int Breite = 130;
    public static final int Hoehe = 134;

    private boolean hatAugenNachUnten = true;

    private int Verhinderaugennachunten;
    private int Verhinderaugennachoben;

    private static final int MAX_VERHINDERAUGENNACHUNTEN = 50;
    private static final int MAX_VERHINDERAUGENNACHOBEN = 30;

    public Dinglinger(Start caller) {
        super(caller);

        dingl_sit_body = new GenericImage[8];
        dingl_sit_waithead = new GenericImage[2];
        dingl_sit_talkhead = new GenericImage[10];

        InitImages();

        Verhindertalkhead = MAX_VERHINDERTALKHEAD;
        Verhindertalkbody = MAX_VERHINDERTALKBODY;
        Verhinderwait = MAX_VERHINDERWAIT;

        Verhinderaugennachunten = MAX_VERHINDERAUGENNACHUNTEN;
        Verhinderaugennachoben = MAX_VERHINDERAUGENNACHOBEN;
    }

    private void InitImages() {
        dingl_sit_body[0] = getPicture("gfx-dd/dingl/dingl-sb1.gif");
        dingl_sit_body[1] = getPicture("gfx-dd/dingl/dingl-sb2.gif");
        dingl_sit_body[2] = getPicture("gfx-dd/dingl/dingl-sb3.gif");
        dingl_sit_body[3] = getPicture("gfx-dd/dingl/dingl-sb4.gif");
        dingl_sit_body[4] = getPicture("gfx-dd/dingl/dingl-sb5.gif");
        dingl_sit_body[5] = getPicture("gfx-dd/dingl/dingl-sberlaubnis.gif");
        dingl_sit_body[6] = getPicture("gfx-dd/dingl/dingl-sblist.gif");
        dingl_sit_body[7] = getPicture("gfx-dd/dingl/dingl-sbtake.gif");

        dingl_sit_waithead[0] = getPicture("gfx-dd/dingl/dingl-sh1.gif");
        dingl_sit_waithead[1] = getPicture("gfx-dd/dingl/dingl-sh2.gif");

        dingl_sit_talkhead[0] = getPicture("gfx-dd/dingl/dingl-st1.gif");
        dingl_sit_talkhead[1] = getPicture("gfx-dd/dingl/dingl-st1a.gif");
        dingl_sit_talkhead[2] = getPicture("gfx-dd/dingl/dingl-st2.gif");
        dingl_sit_talkhead[3] = getPicture("gfx-dd/dingl/dingl-st3.gif");
        dingl_sit_talkhead[4] = getPicture("gfx-dd/dingl/dingl-st4.gif");
        dingl_sit_talkhead[5] = getPicture("gfx-dd/dingl/dingl-st5.gif");
        dingl_sit_talkhead[6] = getPicture("gfx-dd/dingl/dingl-st6.gif");
        dingl_sit_talkhead[7] = getPicture("gfx-dd/dingl/dingl-st7.gif");
        dingl_sit_talkhead[8] = getPicture("gfx-dd/dingl/dingl-st8.gif");
        dingl_sit_talkhead[9] = getPicture("gfx-dd/dingl/dingl-st9.gif");
    }

    public void cleanup() {
        dingl_sit_body[0] = null;
        dingl_sit_body[1] = null;
        dingl_sit_body[2] = null;
        dingl_sit_body[3] = null;
        dingl_sit_body[4] = null;
        dingl_sit_body[5] = null;
        dingl_sit_body[6] = null;
        dingl_sit_body[7] = null;

        dingl_sit_waithead[0] = null;
        dingl_sit_waithead[1] = null;

        dingl_sit_talkhead[0] = null;
        dingl_sit_talkhead[1] = null;
        dingl_sit_talkhead[2] = null;
        dingl_sit_talkhead[3] = null;
        dingl_sit_talkhead[4] = null;
        dingl_sit_talkhead[5] = null;
        dingl_sit_talkhead[6] = null;
        dingl_sit_talkhead[7] = null;
        dingl_sit_talkhead[8] = null;
        dingl_sit_talkhead[9] = null;
    }

    // Zeichne Dinglinger, wie er dasteht oder spricht
    public int drawDinglinger(GenericDrawingContext offGraph, int TalkPerson, int AnimID) {
        // Liste der AnimIDs
        // 1 = hoert zu (Kopf nach rechts)
        // 2 = nimmt allgemein Sachen
        // 3 = nimmt List (Extrawurst beim Nehmen)
        // 4 = lies den Brief
        // 5 = gib Erlaubnis an Krabat

        // AnimCounter zuruecksetzen, wenn eine Anim zu Ende war
        if (AnimCounter > 99) {
            AnimCounter = 0;
        }

        // Dinglinger redet, nur Head/Body switchen und gut
        if ((TalkPerson == 47) && (mainFrame.talkCount > 1)) {
            // Head eval.
            if ((--Verhindertalkhead) < 1) {
                Verhindertalkhead = MAX_VERHINDERTALKHEAD;
                Talkhead = (int) (Math.random() * 9.9);
            }

            // Body eval., dabei einen gueltigen einsetzen, wenn anderes in Variable steht
            if (((--Verhindertalkbody) < 1) || (Talkbody > 1)) {
                Verhindertalkbody = MAX_VERHINDERTALKBODY;
                Talkbody = (int) (Math.random() * 1.9);
            }

            // hier zeichnen
            offGraph.drawImage(dingl_sit_talkhead[Talkhead], Posit.x, Posit.y, null);
            offGraph.drawImage(dingl_sit_body[Talkbody], Posit.x, Posit.y + BODYOFFSET, null);

            return (0); // keine Verlaufsanzeige zurueckgeben
        }

        // AnimID 1 : Dinglinger hoert zu
        if (AnimID == 1) {
            // Zwinkernden Head eval.
            if (Talkhead > 0) {
                Talkhead = 0;
            } else {
                int zuf = (int) (Math.random() * 50);
                if (zuf > 45) {
                    Talkhead = 1;
                }
            }

            // Body ist hier immer 0
            Talkbody = 0;

            // hier zeichnen
            offGraph.drawImage(dingl_sit_talkhead[Talkhead], Posit.x, Posit.y, null);
            offGraph.drawImage(dingl_sit_body[Talkbody], Posit.x, Posit.y + BODYOFFSET, null);

            return (0); // keine Verlaufsanzeige zurueckgeben
        }

        // 2 und 3 : Dinglinger nimmt allgemeine Sachen
        if ((AnimID == 2) || (AnimID == 3)) {
            // je nach AnimCounter einzelne Phasen zeichnen
            if (AnimCounter < 2) {
                // erste Takephase
                Talkbody = 0;
                Talkhead = 0;
            } else {
                if (AnimCounter < 4) {
                    // zweite Takephase
                    Talkbody = 7;
                } else {
                    if (AnimID == 2) {
                        // nur, wenn es nicht der Brief ist...
                        if (AnimCounter < 6) {
                            // dritte Takephase
                            Talkbody = 2;
                        } else {
                            AnimCounter = 99; // Ende der Anim signalisieren
                        }
                    } else {
                        // es ist der Brief, also jetzt schon Ende
                        AnimCounter = 99;
                    }
                }
            }

            // nun noch alles zeichnen
            offGraph.drawImage(dingl_sit_talkhead[Talkhead], Posit.x, Posit.y, null);
            offGraph.drawImage(dingl_sit_body[Talkbody], Posit.x, Posit.y + BODYOFFSET, null);

            // damit die Anim auch vorwaertsgeht...
            AnimCounter++;

            return (AnimCounter); // Verlaufsanzeige zurueckgeben, 100 = Ende
        }

        // ID = 4 : lies den Brief
        if (AnimID == 4) {
            if (AnimCounter < 40) {
                // eine Weile lesen
                Wait = 1;
                Talkbody = 6;
            } else {
                if (AnimCounter < 44) {
                    // wegstecken
                    Talkbody = 2;
                } else {
                    // AnimEnde
                    AnimCounter = 99;
                }
            }

            // hier zeichnen
            offGraph.drawImage(dingl_sit_waithead[Wait], Posit.x, Posit.y, null);
            offGraph.drawImage(dingl_sit_body[Talkbody], Posit.x, Posit.y + BODYOFFSET, null);

            // damit die Anim auch vorwaertsgeht...
            AnimCounter++;

            return (AnimCounter); // Verlaufsanzeige zurueckgeben, 100 = Ende
        }

        // ID 5 : gib Dowolnosc an Krabat
        if (AnimID == 5) {
            if (AnimCounter < 3) {
                // ertse Gebephase
                Talkhead = 0;
                Talkbody = 2;
            } else {
                if (AnimCounter < 8) {
                    // zweite Gebephase
                    Talkhead = 0;
                    Talkbody = 5;
                } else {
                    AnimCounter = 99; // Ende
                }
            }

            // nun noch alles zeichnen
            offGraph.drawImage(dingl_sit_talkhead[Talkhead], Posit.x, Posit.y, null);
            offGraph.drawImage(dingl_sit_body[Talkbody], Posit.x, Posit.y + BODYOFFSET, null);

            AnimCounter++;

            return (AnimCounter); // Verlaufsanzeige zurueckgeben, 100 = Ende
        }

        // bis jetzt keine Anims, also sitzt er nur da und bewegt sich ab und zu

        // entweder schaut er nach unten oder er schaut hoch und zwinkert
        if (hatAugenNachUnten == true) {
            // schaut immer runter
            Wait = 1;
        } else {
            // Zwinkern (Heads)
            if (Wait > 0) {
                Wait = 0;
            } else {
                int zf = (int) (Math.random() * 50);
                if (zf > 45) {
                    Wait = 1;
                }
            }
        }

        // hier umschalten, wie schnell er wieder nach unten bzw. nach oben schaut
        if (hatAugenNachUnten == true) {
            if ((--Verhinderaugennachunten) < 1) {
                Verhinderaugennachunten = MAX_VERHINDERAUGENNACHUNTEN;
                int zz = (int) (Math.random() * 50);
                if (zz > 25) {
                    hatAugenNachUnten = false;
                }
            }
        } else {
            if ((--Verhinderaugennachoben) < 1) {
                Verhinderaugennachoben = MAX_VERHINDERAUGENNACHOBEN;
                hatAugenNachUnten = true;
            }
        }

        // Bodies
        if (((--Verhinderwait) < 1) || ((Talkbody != 3) && (Talkbody != 4))) {
            Verhinderwait = MAX_VERHINDERWAIT;
            Talkbody = (int) ((Math.random() * 1.9) + 3);
        }

        // hier zeichnen
        offGraph.drawImage(dingl_sit_waithead[Wait], Posit.x, Posit.y, null);
        offGraph.drawImage(dingl_sit_body[Talkbody], Posit.x, Posit.y + BODYOFFSET, null);

        return (0); // keine Verlaufsanzeige
    }
}    