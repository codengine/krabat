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

public class Fararhor extends Mainanim {
    private final GenericImage[] farar_talk;
    private GenericImage farar_open, foreground;

    public static final int Breite = 20;
    public static final int Hoehe = 20;

    private int Notalk;
    private static final int MAX_NOTALK = 3;

    private int Doorcount;
    private static final int MAX_DOORCOUNT = 5;

    private int nTemp = 1;

    public Fararhor(Start caller) {
        super(caller);

        farar_talk = new GenericImage[8];

        InitImages();

        Notalk = MAX_NOTALK;
        Doorcount = MAX_DOORCOUNT;

    }

    private void InitImages() {
        farar_talk[1] = getPicture("gfx/cyrkej/f-t-1.gif");
        farar_talk[2] = getPicture("gfx/cyrkej/f-t-2.gif");
        farar_talk[3] = getPicture("gfx/cyrkej/f-t-3.gif");
        farar_talk[4] = getPicture("gfx/cyrkej/f-t-4.gif");
        farar_talk[5] = getPicture("gfx/cyrkej/f-t-5.gif");
        farar_talk[6] = getPicture("gfx/cyrkej/f-t-6.gif");
        farar_talk[7] = getPicture("gfx/cyrkej/f-t-7.gif");

        farar_open = getPicture("gfx/cyrkej/f-t-1a.gif");
        foreground = getPicture("gfx/cyrkej/cdurje2.gif");
    }

    @Override
    public void cleanup() {
        farar_talk[1] = null;
        farar_talk[2] = null;
        farar_talk[3] = null;
        farar_talk[4] = null;
        farar_talk[5] = null;
        farar_talk[6] = null;
        farar_talk[7] = null;

        farar_open = null;
        foreground = null;
    }

    // Zeichne Mutter, wie sie dasteht oder spricht
    public void drawFarar(GenericDrawingContext offGraph, int TalkPerson, GenericPoint pos) {
        // Farar beim Reden zeichnen
        if ((TalkPerson == 37) && (mainFrame.talkCount > 1)) {
            if ((--Notalk) < 1) {
                Notalk = MAX_NOTALK;
                nTemp = (int) Math.round(Math.random() * 7);
                nTemp++;
                if (nTemp == 8) {
                    nTemp = 1;
                }
            }
            offGraph.drawImage(farar_talk[nTemp], pos.x, pos.y);
            offGraph.drawImage(foreground, 332, 300);
        }

        // Farar beim Rumstehen zeichnen
        else {
            nTemp = 1;
            offGraph.drawImage(farar_talk[nTemp], pos.x, pos.y);
            offGraph.drawImage(foreground, 332, 300);
        }
    }

    public boolean moveDoor(GenericDrawingContext offGraph) {
        offGraph.drawImage(farar_open, 322, 307, null);
        offGraph.drawImage(foreground, 332, 300, null);
        --Doorcount;
        if (Doorcount == 0) {
            Doorcount = MAX_DOORCOUNT;
            return false;
        }
        return true;
    }
}    