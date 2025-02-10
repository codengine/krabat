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
import de.codengine.main.GenericRectangle;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Bumm extends Mainanim {
    private GenericImage bumm[];

    private int Bummcount;

    private int Verhinderbumm;

    private static final int MAX_VERHINDERBUMM = 2;

    private static final float Breite = 184f;
    private static final float Hoehe = 68f;

    private static final float Yanfang = 15f;
    private static final float Yende = 55f;

    private static final float Xmitte = 95f;

    private float xScale;

    private int Groesse;
    private GenericPoint Posit;

    private boolean playBumm;

    public Bumm(Start caller) {
        super(caller);

        bumm = new GenericImage[8];

        InitImages();

        xScale = Breite / Hoehe / 3.8f;  // soll nicht so in die Breite gehen
    }

    private void InitImages() {
        bumm[0] = getPicture("gfx/anims/mo1.gif");
        bumm[1] = getPicture("gfx/anims/mo2.gif");
        bumm[2] = getPicture("gfx/anims/mo3.gif");
        bumm[3] = getPicture("gfx/anims/mo4.gif");
        bumm[4] = getPicture("gfx/anims/mo5.gif");
        bumm[5] = getPicture("gfx/anims/mo6.gif");
        bumm[6] = getPicture("gfx/anims/mo7.gif");
        bumm[7] = getPicture("gfx/anims/mo8.gif");
    }

    public void cleanup() {
        bumm[0] = null;
        bumm[1] = null;
        bumm[2] = null;
        bumm[3] = null;
        bumm[4] = null;
        bumm[5] = null;
        bumm[6] = null;
        bumm[7] = null;
    }

    // initialisiert die Anim
    public void Init(GenericPoint Posit, int Groesse)  // Fusspos. des Objektes, Hoehe in Pixeln
    {
        this.Groesse = Groesse;
        this.Posit = Posit;
        Bummcount = 0;
        Verhinderbumm = MAX_VERHINDERBUMM;
        playBumm = true;

        if (this.Groesse < 0) // wenn gewuenscht, dann kein Bumm abspielen (Groesse < 0)
        {
            this.Groesse = -this.Groesse;
            playBumm = false;
        }
    }

    // gibt Rectangle zurueck, wie gross der Spass wird
    public GenericRectangle bummRect() {
        // Groesse des Images bestimmen
        float fGroesse = Groesse;  // so gross soll es von y=15 bis y=55 sein
        float Scale = fGroesse / (Yende - Yanfang);  // Faktor, um wieviel sich das GenericImage vergroessert

        float yGroesse = Hoehe * Scale;
        float xGroesse = Breite * Scale * xScale;

        // Position der LinksOben-Koordinaten bestimmen aus Fussposition
        float Yabstand = Yende * Scale;
        float Xabstand = Xmitte * Scale * xScale;
        // float Xabstand = xGroesse / 2;

        // aus Fussposition folgt x/y-Position
        return (new GenericRectangle((int) (Posit.x - Xabstand), (int) (Posit.y - Yabstand), (int) xGroesse, (int) yGroesse));
    }

    // Zeichne Anim
    public int drawBumm(GenericDrawingContext offGraph) {
        // GenericPoint Posit bezeichnet Fussposition des jeweiligen Dings / Mensches
        // Groesse gibt reale Groesse des TeilImages an, welches den Hintergrund verdeckt

        if (playBumm == true) {
            playBumm = false;
            mainFrame.wave.PlayFile("sfx/morph.wav");
        }

        if ((--Verhinderbumm) < 1) {
            Verhinderbumm = MAX_VERHINDERBUMM;
            Bummcount++;
        }

        // Berechnung der x,y - Koordinaten und der Groesse
        GenericRectangle tp = bummRect();

        // Zeichnen
        offGraph.drawImage(bumm[(Bummcount > 7) ? 7 : Bummcount], (int) tp.getX(), (int) tp.getY(), (int) tp.getWidth(), (int) tp.getHeight(), null);

        // Rueckgabe des aktuellen Zaehlers
        return (Bummcount);

        // Aufrufer sollte ab Bummcount = 8 diese Routine abschalten !!!
    }
}    