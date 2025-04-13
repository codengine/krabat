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

public class Hunter extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Hunter.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_left;
    private final GenericImage[] krabat_right;
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;

    private final GenericImage[] krabat_left_talk_head;
    private final GenericImage[] krabat_left_talk_body;
    private final GenericImage[] krabat_right_talk_head;
    private final GenericImage[] krabat_right_talk_body;
    private final GenericImage[] krabat_down_talk;

    private final GenericImage[] krabat_buecken;

    // Spritevariablen
    private static final int CWIDTH = 45;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 80;

    // Abstaende default
    private static final int[] CHORIZ_DIST = {3, 0, 7, 7, 13, 10, 7, 13};
    private static final int CVERT_DIST = 1;

    // Variablen fuer Animationen
    private static final int SLOWX = 14;  // Konstante, die angibt, wie sich die x - Abstaende
    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung                                      

    private int talkHead = 0;
    private int talkBody = 0;

    private int Verhinderhead;
    private int Verhinderbody;
    private static final int MAX_VERHINDERHEAD = 2;
    private static final int MAX_VERHINDERBODY = 8;

    private static final float KOPFHOEHE = 22;
    private static final float KOERPERHOEHE = 80;

    private int Bueckcounter;
    private static final int MAX_BUECKCOUNTER = 12;

    private int Verhinderwalk;
    private static final int MAX_VERHINDERWALK = 2;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Hunter(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_left = new GenericImage[8];
        krabat_right = new GenericImage[8];
        krabat_front = new GenericImage[4];
        krabat_back = new GenericImage[4];

        krabat_left_talk_head = new GenericImage[6];
        krabat_left_talk_body = new GenericImage[3];
        krabat_right_talk_head = new GenericImage[6];
        krabat_right_talk_body = new GenericImage[3];
        krabat_down_talk = new GenericImage[6];
        krabat_buecken = new GenericImage[2];

        initImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;
        Bueckcounter = MAX_BUECKCOUNTER;
        Verhinderwalk = MAX_VERHINDERWALK;
    }

    // Bilder vorbereiten
    private void initImages() {
        krabat_left[0] = getPicture("gfx/hojnt/ho-l.png");
        krabat_left[1] = getPicture("gfx/hojnt/ho-la.png");
        krabat_left[2] = getPicture("gfx/hojnt/ho-l0.png");
        krabat_left[3] = getPicture("gfx/hojnt/ho-l2.png");
        krabat_left[4] = getPicture("gfx/hojnt/ho-l3.png");
        krabat_left[5] = getPicture("gfx/hojnt/ho-l5.png");
        krabat_left[6] = getPicture("gfx/hojnt/ho-l7.png");
        krabat_left[7] = getPicture("gfx/hojnt/ho-l8.png");

        krabat_right[0] = getPicture("gfx/hojnt/ho-r.png");
        krabat_right[1] = getPicture("gfx/hojnt/ho-ra.png");
        krabat_right[2] = getPicture("gfx/hojnt/ho-r0.png");
        krabat_right[3] = getPicture("gfx/hojnt/ho-r2.png");
        krabat_right[4] = getPicture("gfx/hojnt/ho-r3.png");
        krabat_right[5] = getPicture("gfx/hojnt/ho-r5.png");
        krabat_right[6] = getPicture("gfx/hojnt/ho-r7.png");
        krabat_right[7] = getPicture("gfx/hojnt/ho-r8.png");

        krabat_front[0] = getPicture("gfx/hojnt/ho-u.png");
        krabat_front[1] = getPicture("gfx/hojnt/ho-u2.png");
        krabat_front[2] = getPicture("gfx/hojnt/ho-u.png");
        krabat_front[3] = getPicture("gfx/hojnt/ho-u3.png");

        krabat_back[0] = getPicture("gfx/hojnt/ho-o.png");
        krabat_back[1] = getPicture("gfx/hojnt/ho-o2.png");
        krabat_back[2] = getPicture("gfx/hojnt/ho-o.png");
        krabat_back[3] = getPicture("gfx/hojnt/ho-o3.png");

        krabat_left_talk_head[0] = getPicture("gfx/hojnt/ho-lh0.png");
        krabat_left_talk_head[1] = getPicture("gfx/hojnt/ho-lh1.png");
        krabat_left_talk_head[2] = getPicture("gfx/hojnt/ho-lh2.png");
        krabat_left_talk_head[3] = getPicture("gfx/hojnt/ho-lh3.png");
        krabat_left_talk_head[4] = getPicture("gfx/hojnt/ho-lh4.png");
        krabat_left_talk_head[5] = getPicture("gfx/hojnt/ho-lh5.png");

        krabat_left_talk_body[0] = getPicture("gfx/hojnt/ho-lb0.png");
        krabat_left_talk_body[1] = getPicture("gfx/hojnt/ho-lb1.png");
        krabat_left_talk_body[2] = getPicture("gfx/hojnt/ho-lb2.png");

        krabat_right_talk_head[0] = getPicture("gfx/hojnt/ho-rh0.png");
        krabat_right_talk_head[1] = getPicture("gfx/hojnt/ho-rh1.png");
        krabat_right_talk_head[2] = getPicture("gfx/hojnt/ho-rh2.png");
        krabat_right_talk_head[3] = getPicture("gfx/hojnt/ho-rh3.png");
        krabat_right_talk_head[4] = getPicture("gfx/hojnt/ho-rh4.png");
        krabat_right_talk_head[5] = getPicture("gfx/hojnt/ho-rh5.png");

        krabat_right_talk_body[0] = getPicture("gfx/hojnt/ho-rb0.png");
        krabat_right_talk_body[1] = getPicture("gfx/hojnt/ho-rb1.png");
        krabat_right_talk_body[2] = getPicture("gfx/hojnt/ho-rb2.png");

        krabat_down_talk[0] = getPicture("gfx/hojnt/ho-u.png");
        krabat_down_talk[1] = getPicture("gfx/hojnt/ho-ut1.png");
        krabat_down_talk[2] = getPicture("gfx/hojnt/ho-ut2.png");
        krabat_down_talk[3] = getPicture("gfx/hojnt/ho-ut3.png");
        krabat_down_talk[4] = getPicture("gfx/hojnt/ho-ut4.png");
        krabat_down_talk[5] = getPicture("gfx/hojnt/ho-ut5.png");

        krabat_buecken[0] = getPicture("gfx/hojnt/ho-rbuecken.png");
        krabat_buecken[1] = getPicture("gfx/hojnt/ho-rbuecken2.png");
    }

    @Override
    public void cleanup() {
        krabat_left[0] = null;
        krabat_left[1] = null;
        krabat_left[2] = null;
        krabat_left[3] = null;
        krabat_left[4] = null;
        krabat_left[5] = null;
        krabat_left[6] = null;
        krabat_left[7] = null;

        krabat_right[0] = null;
        krabat_right[1] = null;
        krabat_right[2] = null;
        krabat_right[3] = null;
        krabat_right[4] = null;
        krabat_right[5] = null;
        krabat_right[6] = null;
        krabat_right[7] = null;

        krabat_front[0] = null;
        krabat_front[1] = null;
        krabat_front[2] = null;
        krabat_front[3] = null;

        krabat_back[0] = null;
        krabat_back[1] = null;
        krabat_back[2] = null;
        krabat_back[3] = null;

        krabat_left_talk_head[0] = null;
        krabat_left_talk_head[1] = null;
        krabat_left_talk_head[2] = null;
        krabat_left_talk_head[3] = null;
        krabat_left_talk_head[4] = null;
        krabat_left_talk_head[5] = null;

        krabat_left_talk_body[0] = null;
        krabat_left_talk_body[1] = null;
        krabat_left_talk_body[2] = null;

        krabat_right_talk_head[0] = null;
        krabat_right_talk_head[1] = null;
        krabat_right_talk_head[2] = null;
        krabat_right_talk_head[3] = null;
        krabat_right_talk_head[4] = null;
        krabat_right_talk_head[5] = null;

        krabat_right_talk_body[0] = null;
        krabat_right_talk_body[1] = null;
        krabat_right_talk_body[2] = null;

        krabat_down_talk[0] = null;
        krabat_down_talk[1] = null;
        krabat_down_talk[2] = null;
        krabat_down_talk[3] = null;
        krabat_down_talk[4] = null;
        krabat_down_talk[5] = null;

        krabat_buecken[0] = null;
        krabat_buecken[1] = null;
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

        // Verzoegerung einbauen beim Laufen
        if (--Verhinderwalk > 0 && isAnimHorizontal) {
            return false;
        }

        Verhinderwalk = MAX_VERHINDERWALK;

        if (isAnimHorizontal)
        // Horizontal laufen
        {
            // neuen Punkt ermitteln und setzen
            moveX();
            posX = tempPosX;
            posY = tempPosY;

            // Animationsphase weiterschalten
            animPos++;
            if (animPos == 8) {
                animPos = 2;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            moveX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkTo.x - (int) tempPosX) * directionX.getVal() <= 0) {
                setPos(walkTo);
                if (resetAnimPos) {
                    animPos = 0;
                }
                return true;
            }
        } else
        // Vertikal laufen
        {
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
                if (resetAnimPos) {
                    animPos = 0;
                }
                return true;
            }
        }
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    private void moveX() {
        moveXdefault(CHORIZ_DIST[animPos], SLOWX);
    }

    // Vertikal - Positions - Verschieberoutine
    private void moveY() {
        moveYdefault(CVERT_DIST, SLOWY);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void moveTo(GenericPoint aim) {
        moveToDefault(aim);
        tmpIsAnimHorizontal = calcHorizontal(aim, 30);

        if (animPos == 0) {
            animPos = 1;       // Animationsimage bei Neubeginn initialis.
        } else {
            // bei fortgehendem Bewegen weiterschalten
            animPos++;
            if (tmpIsAnimHorizontal && animPos == 8) {
                animPos = 2;
            }
            if (!tmpIsAnimHorizontal && animPos == 4) {
                animPos = 0;
            }
        }
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawHojnt(GenericDrawingContext offGraph) {
        // je nach Richtung Sprite auswaehlen und zeichnen
        if (isAnimHorizontal) {
            // nach links laufen
            if (directionX == LEFT) {
                drawHim(offGraph, krabat_left[animPos]);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                drawHim(offGraph, krabat_right[animPos]);
            }
        } else {
            // Bei normaler Darstellung
            if (!upsideDown) {
                // nach oben laufen
                if (directionY == UP) {
                    drawHim(offGraph, krabat_back[animPos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    drawHim(offGraph, krabat_front[animPos]);
                }
            } else {
                // nach oben laufen
                if (directionY == UP) {
                    drawHim(offGraph, krabat_front[animPos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    drawHim(offGraph, krabat_back[animPos]);
                }
            }
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

    // Richtung, in die Krabat schaut, ermitteln (wieder nach Uhrzeit)
    // nur private, da nur in dieser Klasse benoetigt
    private int getFacing() {
        if (isAnimHorizontal) {
            return directionX == RIGHT ? 3 : 9;
        } else {
            return directionY == DOWN ? 6 : 12;
        }
    }

    // Zeichne Hojnt beim Sprechen mit anderen Personen
    public void talkHojnt(GenericDrawingContext offGraph) {
        // Kopf veraendern
        if (--Verhinderhead < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            talkHead = (int) (Math.random() * 5.9);
        }

        // Body switchen
        if (--Verhinderbody < 1) {
            Verhinderbody = MAX_VERHINDERBODY;
            talkBody = (int) (Math.random() * 2.9);
        }

        // reden nach unten ist nur 1 Image
        if (getFacing() == 6) {
            drawHim(offGraph, krabat_down_talk[talkHead]);
        } else {
            // reden nach rechts
            if (getFacing() == 3) {
                drawHim(offGraph, krabat_right_talk_head[talkHead], krabat_right_talk_body[talkBody]);
            }

            // reden nach links
            if (getFacing() == 9) {
                drawHim(offGraph, krabat_left_talk_head[talkHead], krabat_left_talk_body[talkBody]);
            }
        }
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

    private void drawHim(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // Hier beim Scaling ein echtes Verhaeltnis Hoehe/Breite einsetzen

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - (int) (scale * scaleFactor), CHEIGHT - scale);
    }

    private void drawHim(GenericDrawingContext g, GenericImage khead, GenericImage kbody) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // Scalings und Offset fuer gesplittetes Kopf/Koerperzeichnen
        float fScale = CHEIGHT - scale;
        float teilScaleHead = KOPFHOEHE / KOERPERHOEHE;
        int Kopfhoehe = (int) (fScale * teilScaleHead);
        int Koerperhoehe = CHEIGHT - scale - Kopfhoehe;

        // System.out.println ("Kopfhoehe : " + Kopfhoehe + " Bodyhoehe : " + Koerperhoehe);

        // Hier beim Scaling ein echtes Verhaeltnis Hoehe/Breite einsetzen

        // Figur zeichnen
        int bodyWidth = CWIDTH - (int) (scale * scaleFactor);
        g.drawImage(khead, left, up, bodyWidth, Kopfhoehe);
        g.drawImage(kbody, left, up + Kopfhoehe, bodyWidth, Koerperhoehe);
    }

    public boolean bueckeHojnt(GenericDrawingContext g) {
        // Clipping - Region setzen
        // Links - oben - Korrdinaten ermitteln
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd = 2 * ((int) posX - left) + 30;
        int yd = (int) posY - up;
        g.setClip(left, up, xd, yd);


        // Groesse und Position der Figur berechnen
        int scale = getScale((int) posY);

        // Hier beim Scaling ein echtes Verhaeltnis Hoehe/Breite einsetzen
        float fBreite = 63.0f;
        float fHoehe = 80.0f;
        float Verhaeltnis = fBreite / fHoehe;

        // Figur zeichnen
        g.drawImage(krabat_buecken[Bueckcounter > 8 || Bueckcounter < 4 ? 0 : 1], left, up, (int) fBreite - (int) (scale * Verhaeltnis), (int) fHoehe - scale);

        --Bueckcounter;
        if (Bueckcounter < 1) {
            Bueckcounter = MAX_BUECKCOUNTER;
            return false;
        }

        return true;
    }

}