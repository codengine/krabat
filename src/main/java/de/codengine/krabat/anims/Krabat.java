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

    // Clipping - Region vor Zeichnen von Krabat setzen
    protected void krabatClipExtraDefault(GenericDrawingContext g, int xx, int yy, boolean isLeft) {
        // Links - oben - Korrdinaten ermitteln, wie sie eigentlich waeren
        int x = getLeftPos(xx, yy);
        int y = getUpPos(yy);

        // Breite und Hoehe ermitteln
        int xd, yd;

        if (isLeft) {
            // nach links Cliprectangle auch nach links vergroessern !!!
            // GenericImage ist in Beide Richtungen gleichgross !!
            yd = yy - y;
            xd = yd;

            // x-Position muss verringert werden !
            x -= xd / 2;
        } else {
            // nach rechts nur Cliprectangle vergroessern
            yd = yy - y;
            xd = yd;
        }

        g.setClip(x, y, xd, yd);

        // Fuer Debugging ClipRectangle zeichnen
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
    }

    protected void verschiebeXkrabat(float horizDist) {
        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = 0;
        if (horizDist != 0) {
            z = Math.abs(xps - walkto.x) / horizDist;
        }

        // BUGFIX: kleine z nicht zulassen!!!
        if (z < 1) {
            z = 0;
        }

        typs = yps;
        if (z != 0) {
            typs += directionY.getVal() * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + directionX.getVal() * horizDist;
    }

    protected void verschiebeYkrabat(float vertDist) {
        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vertDist;

        // BUGFIX: kleine z nicht zulassen!!!
        if (z < 1) {
            z = 0;
        }

        txps = xps;
        if (z != 0) {
            txps += directionX.getVal() * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + directionY.getVal() * vertDist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    protected synchronized void moveToKrabat(GenericPoint aim, int lohnx, int lohny, int minAnimPos, int calcHorizDeg) {
        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = calcHorizontal(aim, calcHorizDeg);

        // Laufrichtung ermitteln
        tDirectionX = aim.x > (int) xps ? RIGHT : LEFT;
        tDirectionY = aim.y > (int) yps ? DOWN : UP;

        // Lohnt es sich zu laufen ?
        if (lohnx < 1) {
            lohnx = 1;
        }
        if (lohny < 1) {
            lohny = 1;
        }
        if (Math.abs(aim.x - (int) xps) < lohnx && Math.abs(aim.y - (int) yps) < lohny) {
            isWalking = false;
            log.debug("Nicht gerade lohnend !!");
            if (!isWandering && clearanimpos) {
                anim_pos = 0;
            }
            return;
        }

        // von Oben
        if (anim_pos < minAnimPos) {
            anim_pos = minAnimPos;
        }

        isWalking = true;                             // Stiefel los !
    }
}	