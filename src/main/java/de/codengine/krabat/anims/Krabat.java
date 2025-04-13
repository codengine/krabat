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
    public void stopAnim() {
        nAnimation = 0;
        fAnimHelper = false;
        nAnimStep = 0;
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void setFacing(int direction) {
        switch (direction) {
            case 3:
                isAnimHorizontal = true;
                directionX = RIGHT;
                break;
            case 6:
                isAnimHorizontal = false;
                directionY = DOWN;
                break;
            case 9:
                isAnimHorizontal = true;
                directionX = LEFT;
                break;
            case 12:
                isAnimHorizontal = false;
                directionY = UP;
                break;
            default:
                log.debug("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Richtung, in die Krabat schaut, ermitteln (wieder nach Uhrzeit)
    public int getFacing() {
        if (isAnimHorizontal) {
            return directionX == RIGHT ? 3 : 9;
        } else {
            return directionY == DOWN ? 6 : 12;
        }
    }

    // Hoer auf hier rumzurennen, es gibt anderes zu tun!
    public void stopWalking() {
        stopAnim();
        isWandering = false;
        isWalking = false;
        animPos = 0;
    }

    // alle Methoden, die erst in der jeweiligen Klasse implementiert werden

    abstract public void move();

    abstract public void moveTo(GenericPoint aim);

    abstract public void talkKrabat(GenericDrawingContext g);

    abstract public void describeKrabat(GenericDrawingContext g);

    abstract public void drawKrabat(GenericDrawingContext g);

    abstract public void doAnimation(GenericDrawingContext g);

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
    }

    protected void moveXkrabat(float horizDist) {
        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = 0;
        if (horizDist != 0) {
            z = Math.abs(posX - walkTo.x) / horizDist;
        }

        // BUGFIX: kleine z nicht zulassen!!!
        if (z < 1) {
            z = 0;
        }

        tempPosY = posY;
        if (z != 0) {
            tempPosY += directionY.getVal() * (Math.abs(posY - walkTo.y) / z);
        }

        tempPosX = posX + directionX.getVal() * horizDist;
    }

    protected void moveYkrabat(float vertDist) {
        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(posY - walkTo.y) / vertDist;

        // BUGFIX: kleine z nicht zulassen!!!
        if (z < 1) {
            z = 0;
        }

        tempPosX = posX;
        if (z != 0) {
            tempPosX += directionX.getVal() * (Math.abs(posX - walkTo.x) / z);
        }

        tempPosY = posY + directionY.getVal() * vertDist;
    }

    protected synchronized void moveToKrabat(GenericPoint aim, int lohnx, int lohny, int minAnimPos, int calcHorizDeg) {
        // Variablen an Move uebergeben
        tmpWalkTo = aim;
        tmpIsAnimHorizontal = calcHorizontal(aim, calcHorizDeg);

        // Laufrichtung ermitteln
        tmpDirectionX = aim.x > (int) posX ? RIGHT : LEFT;
        tmpDirectionY = aim.y > (int) posY ? DOWN : UP;

        // Lohnt es sich zu laufen ?
        if (lohnx < 1) {
            lohnx = 1;
        }
        if (lohny < 1) {
            lohny = 1;
        }
        if (Math.abs(aim.x - (int) posX) < lohnx && Math.abs(aim.y - (int) posY) < lohny) {
            isWalking = false;
            log.debug("Nicht gerade lohnend !!");
            if (!isWandering && resetAnimPos) {
                animPos = 0;
            }
            return;
        }

        // von Oben
        if (animPos < minAnimPos) {
            animPos = minAnimPos;
        }

        isWalking = true;                             // Stiefel los !
    }
}	