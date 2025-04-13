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
import de.codengine.krabat.main.BorderRect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public class MerchantIronOre extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(MerchantIronOre.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;
    private final GenericImage[] krabat_talk;
    private final GenericImage[] krabat_extra;
    private final GenericImage[] hrajer_extra;

    // Spritevariablen
    private static final int CWIDTH = 33;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 35;

    // Abstaende default
    private static final int CVERT_DIST = 4;

    private int VerhinderTalk;
    private static final int MAX_VERHINDERTALK = 5;
    private int TalkPos = 0;

    private boolean isMoving = false;

    private int Guck = 0;
    private int Verhinderguck;
    private static final int MAX_VERHINDERGUCK = 70;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public MerchantIronOre(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_front = new GenericImage[4];
        krabat_back = new GenericImage[4];
        krabat_talk = new GenericImage[8];
        krabat_extra = new GenericImage[2];
        hrajer_extra = new GenericImage[3];

        initImages();

        VerhinderTalk = MAX_VERHINDERTALK;
        Verhinderguck = MAX_VERHINDERGUCK;
    }

    // Bilder vorbereiten
    private void initImages() {
        krabat_front[0] = getPicture("gfx-dd/lodz/zona.png");
        krabat_front[1] = getPicture("gfx-dd/lodz/zona5.png");
        krabat_front[2] = getPicture("gfx-dd/lodz/zona.png");
        krabat_front[3] = getPicture("gfx-dd/lodz/zona6.png");

        krabat_back[0] = getPicture("gfx-dd/lodz/zona4.png");
        krabat_back[1] = getPicture("gfx-dd/lodz/zona2.png");
        krabat_back[2] = getPicture("gfx-dd/lodz/zona4.png");
        krabat_back[3] = getPicture("gfx-dd/lodz/zona3.png");

        krabat_talk[0] = getPicture("gfx-dd/lodz/zona.png");
        krabat_talk[1] = getPicture("gfx-dd/lodz/zona9.png");
        krabat_talk[2] = getPicture("gfx-dd/lodz/zona10.png");
        krabat_talk[3] = getPicture("gfx-dd/lodz/zona11.png");
        krabat_talk[4] = getPicture("gfx-dd/lodz/zona12.png");
        krabat_talk[5] = getPicture("gfx-dd/lodz/zona13.png");
        krabat_talk[6] = getPicture("gfx-dd/lodz/zona14.png");
        krabat_talk[7] = getPicture("gfx-dd/lodz/zona15.png");

        krabat_extra[0] = getPicture("gfx-dd/lodz/zona7.png");
        krabat_extra[1] = getPicture("gfx-dd/lodz/zona8.png");

        hrajer_extra[0] = getPicture("gfx-dd/lodz/kzona.png");
        hrajer_extra[1] = getPicture("gfx-dd/lodz/kzona2.png");
        hrajer_extra[2] = getPicture("gfx-dd/lodz/kzona3.png");
    }


    // Laufen mit Plokarka ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        walkTo = tmpWalkTo;
        directionX = tmpDirectionX;
        directionY = tmpDirectionY;

        // neuen Punkt ermitteln und setzen
        moveY();
        posX = tempPosX;
        posY = tempPosY;

        // Animationsphase weiterschalten
        animPos++;
        if (animPos == 4) {
            animPos = 0;
        }

        // Naechsten Schritt auf Gueltigkeit ueberpruefen
        moveY();

        // Ueberschreitung feststellen in Y - Richtung
        if ((walkTo.y - (int) tempPosY) * directionY.getVal() <= 0) {
            setPos(walkTo);
            animPos = 0;
            isMoving = true;
            return true;
        }
        /*  }  */
        isMoving = false;
        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void moveY() {
        moveY(CVERT_DIST);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void moveTo(GenericPoint aim) {
        moveToDefault(aim);

        if (animPos == 0) {
            animPos = 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawZona(GenericDrawingContext offGraph, boolean isListening) {
        // hier evaluieren, ob geguckt werden darf oder nicht
        if (!isMoving && !isListening) {
            // Weiterschalten
            if (--Verhinderguck < 1) {
                Verhinderguck = MAX_VERHINDERGUCK;
                Guck = (int) (Math.random() * 1.9);
            }

            // Wenn Extrawurst, dann diese ausfuehren
            if (Guck == 1) {
                drawHim(offGraph, krabat_talk[1]);
                return;
            }
        }

        // nach oben laufen
        if (directionY == DOWN) {
            drawHim(offGraph, krabat_front[animPos]);
        }

        // nach unten laufen
        if (directionY == UP) {
            drawHim(offGraph, krabat_back[animPos]);
        }
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void setFacing(int direction) {
        switch (direction) {
            case 6:
                directionY = DOWN;
                break;
            case 12:
                directionY = UP;
                break;
            default:
                log.debug("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Zeichne Hojnt beim Sprechen mit anderen Personen
    public void talkZona(GenericDrawingContext offGraph) {
        // isListening wird nur bei "draw" ausgewertet
        // d.h. aktiviert -> Gucken gesperrt
        // deaktiviert -> Guckt nach Ende Talk wieder

        // this.isListening = isListening;

        // schraeggucken deaktivieren
        Guck = 0;

        if (--VerhinderTalk < 1) {
            VerhinderTalk = MAX_VERHINDERTALK;
            TalkPos = (int) Math.round(Math.random() * 8);

            if (TalkPos == 8) {
                TalkPos = 7;
            }
        }

        drawHim(offGraph, krabat_talk[TalkPos]);
    }

    public void giveWosusk(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und die 2 Images fuer Give zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(krabat_extra[0], lo.x, lo.y);
        g.drawImage(hrajer_extra[0], lo.x, lo.y + CHEIGHT);
    }

    public void giveMetal(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und Images fuer Give zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(krabat_extra[1], lo.x, lo.y);
        g.drawImage(hrajer_extra[1], lo.x, lo.y + CHEIGHT);
    }

    public void kiss(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und 1 Kiss-Image zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(hrajer_extra[2], lo.x, lo.y);
    }

    // Zooming-Variablen berechnen
    private int getLeftPos(int pox) {
        return getLeftPos(pox, 1);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int x, int ignored) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        return x - CWIDTH / 2;
    }

    @Override
    protected int getUpPos(int y) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        return y - CHEIGHT / 2;
    }

    @Override
    protected int getScale(int y) {
        return calcScaleDefault(y);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    @Override
    public BorderRect getBoundingBox() {
        int x = getLeftPos((int) posX);
        int y = getUpPos((int) posY);
        int xd = 2 * ((int) posX - x) + x;
        int yd = 2 * ((int) posY - y) + y;
        return new BorderRect(x, y, xd, yd);
    }

    private void krabatClip(GenericDrawingContext g, int xps, int yps) {
        krabatClipDefault(g, xps, yps, 2);
    }

    private void drawHim(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClip(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX);
        int up = getUpPos((int) posY);
        // int scale = getScale   ( ((int) xps), ((int) yps) );

        // Figur zeichnen
        g.drawImage(ktemp, left, up);
    }
}