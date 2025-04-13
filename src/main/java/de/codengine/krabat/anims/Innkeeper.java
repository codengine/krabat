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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public class Innkeeper extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Innkeeper.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;
    private final GenericImage[] krabat_talk_head;
    private final GenericImage[] krabat_talk_body;

    // Spritevariablen
    private static final int CWIDTH = 76;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 146;

    // Abstaende default
    private static final int[] CVERT_OBEN = {0, 1, 4, 1, 4};
    private static final int[] CVERT_UNTEN = {0, 0, 4, 1, 4, 1};

    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung

    // Redevariablen
    private int Kopf = 0;
    private int Body = 0;

    private int Verhinderkopf;
    private int Verhinderbody;
    private static final int MAX_VERHINDERKOPF = 2;
    private static final int MAX_VERHINDERBODY = 8;

    private static final int HEADHEIGHT = 33;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Innkeeper(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_front = new GenericImage[6];
        krabat_back = new GenericImage[5];

        krabat_talk_head = new GenericImage[7];
        krabat_talk_body = new GenericImage[4];

        initImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    // Bilder vorbereiten
    private void initImages() {
        krabat_back[0] = getPicture("gfx/hoscenc/wirt-o.png");
        krabat_back[1] = getPicture("gfx/hoscenc/wirt-o1.png");
        krabat_back[2] = getPicture("gfx/hoscenc/wirt-o2.png");
        krabat_back[3] = getPicture("gfx/hoscenc/wirt-o3.png");
        krabat_back[4] = getPicture("gfx/hoscenc/wirt-o4.png");

        krabat_front[0] = getPicture("gfx/hoscenc/wirt-u.png");
        krabat_front[1] = getPicture("gfx/hoscenc/wirt-ua.png");
        krabat_front[2] = getPicture("gfx/hoscenc/wirt-u1.png");
        krabat_front[3] = getPicture("gfx/hoscenc/wirt-u2.png");
        krabat_front[4] = getPicture("gfx/hoscenc/wirt-u3.png");
        krabat_front[5] = getPicture("gfx/hoscenc/wirt-u4.png");

        krabat_talk_head[0] = getPicture("gfx/hoscenc/wirt-h0.png");
        krabat_talk_head[1] = getPicture("gfx/hoscenc/wirt-h1.png");
        krabat_talk_head[2] = getPicture("gfx/hoscenc/wirt-h2.png");
        krabat_talk_head[3] = getPicture("gfx/hoscenc/wirt-h3.png");
        krabat_talk_head[4] = getPicture("gfx/hoscenc/wirt-h4.png");
        krabat_talk_head[5] = getPicture("gfx/hoscenc/wirt-h5.png");
        krabat_talk_head[6] = getPicture("gfx/hoscenc/wirt-h6.png");

        krabat_talk_body[0] = getPicture("gfx/hoscenc/wirt-b0.png");
        krabat_talk_body[1] = getPicture("gfx/hoscenc/wirt-b1.png");
        krabat_talk_body[2] = getPicture("gfx/hoscenc/wirt-b2.png");
        krabat_talk_body[3] = getPicture("gfx/hoscenc/wirt-b3.png");
    }

    @Override
    public void cleanup() {
        krabat_back[0] = null;
        krabat_back[1] = null;
        krabat_back[2] = null;
        krabat_back[3] = null;
        krabat_back[4] = null;

        krabat_front[0] = null;
        krabat_front[1] = null;
        krabat_front[2] = null;
        krabat_front[3] = null;
        krabat_front[4] = null;
        krabat_front[5] = null;

        krabat_talk_head[0] = null;
        krabat_talk_head[1] = null;
        krabat_talk_head[2] = null;
        krabat_talk_head[3] = null;
        krabat_talk_head[4] = null;
        krabat_talk_head[5] = null;
        krabat_talk_head[6] = null;

        krabat_talk_body[0] = null;
        krabat_talk_body[1] = null;
        krabat_talk_body[2] = null;
        krabat_talk_body[3] = null;
    }


    // Laufen mit Plokarka ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        isAnimHorizontal = tmpIsAnimHorizontal;
        walkTo = tmpWalkTo;
        directionX = tmpDirectionX;
        directionY = tmpDirectionY;

        if (!isAnimHorizontal)
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            moveY();
            posX = tempPosX;
            posY = tempPosY;

            // Animationsphase weiterschalten
            animPos++;
            if (animPos == (directionY == DOWN ? 6 : 5)) {
                animPos = directionY == DOWN ? 2 : 1;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            moveY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkTo.y - (int) tempPosY) * directionY.getVal() <= 0) {
                setPos(walkTo);
                animPos = 0;
                return true;
            }
        }

        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void moveY() {
        // Skalierungsfaktor holen
        int scale = getScale((int) posY);

        float vertDist;
        // Zooming - Faktor beruecksichtigen in y-Richtung
        if (directionY == DOWN) {
            vertDist = CVERT_UNTEN[animPos] - (float) scale / SLOWY;
        } else {
            vertDist = CVERT_OBEN[animPos] - (float) scale / SLOWY;
        }

        if (vertDist < 1) {
            vertDist = 1;
        }

        moveY(vertDist);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void moveTo(GenericPoint aim) {
        // Laufrichtung ermitteln
        final DirectionY yricht = aim.y > (int) posY ? DOWN : UP;

        // Variablen an Move uebergeben
        tmpWalkTo = aim;
        tmpIsAnimHorizontal = false;
        tmpDirectionX = aim.x > (int) posX ? RIGHT : LEFT;
        tmpDirectionY = yricht;

        if (animPos == 0) {
            animPos = yricht == DOWN ? 2 : 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawKorcmar(GenericDrawingContext offGraph) {
        // nach oben laufen
        if (directionY == UP) {
            drawHim(offGraph, krabat_back[animPos]);
        }

        // nach unten laufen
        if (directionY == DOWN) {
            // Zwinkern evaluieren
            if (animPos == 1) {
                animPos = 0;
            } else {
                if (animPos == 0) {
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 45) {
                        animPos = 1;
                    }
                }
            }

            drawHim(offGraph, krabat_front[animPos]);
        }
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

    // Zeichne Plokarka normal Schimpfend
    public void talkKorcmar(GenericDrawingContext offGraph) {
        // Heads 0..6
        // Bodies 0..3

        // Heads evaluieren
        if (--Verhinderkopf < 1) {
            Verhinderkopf = MAX_VERHINDERKOPF;
            Kopf = (int) (Math.random() * 6.9);
        }

        // Bodies evaluieren
        if (--Verhinderbody < 1) {
            Verhinderbody = MAX_VERHINDERBODY;
            Body = (int) (Math.random() * 3.9);
        }

        talk(offGraph);
    }

    // Extraroutine fuers Reden
    private void talk(GenericDrawingContext g) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);

        // Offsets berechnen
        float fScale = getScale((int) posY);
        int scalewidth = (int) (fScale * scaleFactor);

        float fHeight = CHEIGHT;
        int Kopfhoehe = (int) ((fHeight - fScale) * ((float) HEADHEIGHT / fHeight));
        int Koerperhoehe = CHEIGHT - Kopfhoehe;

        // Figur zeichnen
        int bodyWidth = CWIDTH - scalewidth;
        g.drawImage(krabat_talk_head[Kopf], left, up, bodyWidth, Kopfhoehe);
        g.drawImage(krabat_talk_body[Body], left, up + Kopfhoehe, bodyWidth, Koerperhoehe);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int x, int y) {
        return calcLeftPosDefault(x, y);
    }

    @Override
    protected int getUpPos(int y) {
        return calcUpPosDefault(y);
    }

    @Override
    protected int getScale(int y) {
        return calcScaleDefault(y, defaultScale);
    }

    // Routine, die TalkPoint zurueckgibt...
    public GenericPoint evalTalkPoint() {
        int up = getUpPos((int) posY);
        return new GenericPoint((int) posX, up - 50);
    }

    private void drawHim(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // Offsets berechnen
        int scalewidth = (int) ((float) scale * scaleFactor);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scalewidth, CHEIGHT - scale);
    }
}