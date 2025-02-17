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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

abstract public class Krabat extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Krabat.class);
    // alle Variablen, die nach aussen sichtbar sein sollen

    public boolean isWandering = false;  // gilt fuer ganze Route
    public boolean isWalking = false;    // gilt bis zum naechsten Rect.

    // Variablen fuer Animationen
    public int nAnimation = 0;           // ID der ggw. Animation
    public boolean fAnimHelper = false;  // Hilfsflag bei Animation

    public int minx;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...

    // Variablen, die nur innerhalb Krabat sichtbar sein sollen

    int nAnimStep = 0;                    // ggw. Pos in Animation

    int Floetenwartezeit;
    static final int[] Floetenwartezeitarray = new int[]{78, 78, 95, 112, 54};
    static final int rohodzWartezeit = 28;


    // Konstruktor
    public Krabat(Start caller, int width, int height) {
        super(caller, width, height);
    }

    // Alle Methoden, die immer Gueltigkeit haben, egal, welche untergelagerte Krabatklasse gerade aktiv ist

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetKrabatPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        // System.out.println ("K-Feetpos : " + xps + " " + yps);

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    public GenericPoint GetKrabatPos() {
        //System.out.println(" Aktuelle Pos : "+pos_x+" "+pos_y);
        return new GenericPoint((int) xps, (int) yps);
    }

    // Animation anhalten und Daten zuruecksetzen
    public void StopAnim() {
        nAnimation = 0;
        fAnimHelper = false;
        nAnimStep = 0;
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void SetFacing(int direction) {
        switch (direction) {
            case 3:
                horizontal = true;
                directionX = RIGHT;
                break;
            case 6:
                horizontal = false;
                directionY = DOWN;
                break;
            case 9:
                horizontal = true;
                directionX = LEFT;
                break;
            case 12:
                horizontal = false;
                directionY = UP;
                break;
            default:
                log.debug("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Richtung, in die Krabat schaut, ermitteln (wieder nach Uhrzeit)
    public int GetFacing() {
        if (horizontal) {
            return directionX == RIGHT ? 3 : 9;
        } else {
            return directionY == DOWN ? 6 : 12;
        }
    }

    // Hoer auf hier rumzurennen, es gibt anderes zu tun!
    public void StopWalking() {
        StopAnim();
        isWandering = false;
        isWalking = false;
        anim_pos = 0;
    }

    // alle Methoden, die erst in der jeweiligen Klasse implementiert werden

    abstract public void Move();

    abstract public void MoveTo(GenericPoint aim);

    abstract public void talkKrabat(GenericDrawingContext g);

    abstract public void describeKrabat(GenericDrawingContext g);

    abstract public void drawKrabat(GenericDrawingContext g);

    abstract public void DoAnimation(GenericDrawingContext g);

}	