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

public class StrazaPoklad extends Mainanim {
    private final GenericImage[] straza_head;
    private final GenericImage[] straza_body;

    public static final int Breite = 34;
    public static final int Hoehe = 89;

    private static final int BODYOFFSET = 22;

    private int Head = 0;

    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;

    public StrazaPoklad(Start caller) {
        super(caller);

        straza_head = new GenericImage[8];
        straza_body = new GenericImage[2];

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
    }

    private void InitImages() {
        straza_head[0] = getPicture("gfx-dd/trepj/pstraza-h1.gif");
        straza_head[1] = getPicture("gfx-dd/trepj/pstraza-h1a.gif");
        straza_head[2] = getPicture("gfx-dd/trepj/pstraza-h2.gif");
        straza_head[3] = getPicture("gfx-dd/trepj/pstraza-h3.gif");
        straza_head[4] = getPicture("gfx-dd/trepj/pstraza-h4.gif");
        straza_head[5] = getPicture("gfx-dd/trepj/pstraza-h5.gif");
        straza_head[6] = getPicture("gfx-dd/trepj/pstraza-h6.gif");
        straza_head[7] = getPicture("gfx-dd/trepj/pstraza-h7.gif");

        straza_body[0] = getPicture("gfx-dd/trepj/pstraza-b1.gif");
        straza_body[1] = getPicture("gfx-dd/trepj/pstraza-b2.gif");
    }

    // Zeichne Hauptwachter, wie er dasteht oder spricht
    public void drawStraza(GenericDrawingContext offGraph, int TalkPerson, GenericPoint Posit, boolean weistzurueck) {
        // Hauptwaechter redet
        if ((TalkPerson == 46) && (mainFrame.talkCount > 1)) {
            // Head evaluieren
            if ((--Verhinderhead) < 1) {
                Verhinderhead = MAX_VERHINDERHEAD;
                Head = (int) (Math.random() * 7.9);
            }

            offGraph.drawImage(straza_head[Head], Posit.x, Posit.y, null);
            offGraph.drawImage((weistzurueck) ? straza_body[1] : straza_body[0], Posit.x, Posit.y + BODYOFFSET, null);
        }
        // Hauptwaechter steht rum
        else {
            // wenn Zwinkern oder irgendein redekopf gewesen, dann weg
            if (Head != 0) {
                Head = 0;
            } else {
                // Zwinkern evaluieren
                int zuffi = (int) (Math.random() * 50);
                if (zuffi > 45) {
                    Head = 1;
                }
            }

            offGraph.drawImage(straza_head[Head], Posit.x, Posit.y, null);
            offGraph.drawImage((weistzurueck) ? straza_body[1] : straza_body[0], Posit.x, Posit.y + BODYOFFSET, null);
        }
    }
}    