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

public class Plokarka extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Plokarka.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_left;
    private final GenericImage[] krabat_right;
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;
    private final GenericImage[] krabat_talk;
    private final GenericImage[] krabat_haende;

    private final GenericImage[] krabatw_front;
    private final GenericImage[] krabatw_left;
    private GenericImage krabatw_abnehm;

    public boolean hasWaesche = false;

    // Spritevariablen
    private static final int CWIDTH = 45;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 77;

    // Abstaende default
    private static final int[] CHORIZ_DIST = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    private static final int CVERT_DIST = 2;

    // Variablen fuer Laufberechnung
    // private static final int CLOHNENX = 6;  // Werte fuer Entscheidung, ob sich
    // private static final int CLOHNENY = 6;  // Laufen ueberhaupt lohnt (halber Schritt)

    // Variablen fuer Animationen
    // public  int nAnimation = 0;           // ID der ggw. Animation
    // public  boolean fAnimHelper = false;  // Hilfsflag bei Animation
    // private int nAnimStep = 0;            // ggw. Pos in Animation

    // den Hintergrund geht (bildabhaengig)
    private static final int SLOWX = 14;  // Konstante, die angibt, wie sich die x - Abstaende
    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung                                      

    // Redevariablen
    private int TalkPic = 0;
    private int Notalk = 2;
    private static final int MAX_NOTALK = 2;

    private int Abnehmcount = 3;
    private static final int MAX_ABNEHMCOUNT = 3;

    private int Nohaendefuchtel = 8;
    private static final int MAX_NOHAENDEFUCHTEL = 8;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Plokarka(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_left = new GenericImage[4];
        krabat_right = new GenericImage[4];
        krabat_front = new GenericImage[3];
        krabat_back = new GenericImage[4];
        krabat_talk = new GenericImage[5];
        krabat_haende = new GenericImage[5];

        krabatw_front = new GenericImage[3];
        krabatw_left = new GenericImage[4];

        InitImages();

    }

    // Bilder vorbereiten
    private void InitImages() {
        krabat_left[0] = getPicture("gfx/villa/wf3a.png");
        krabat_left[1] = getPicture("gfx/villa/wf4a.png");
        krabat_left[2] = getPicture("gfx/villa/wf5a.png");
        krabat_left[3] = getPicture("gfx/villa/wf6a.png");

        krabat_right[0] = getPicture("gfx/villa/wf3.png");
        krabat_right[1] = getPicture("gfx/villa/wf4.png");
        krabat_right[2] = getPicture("gfx/villa/wf5.png");
        krabat_right[3] = getPicture("gfx/villa/wf6.png");

        krabat_front[0] = getPicture("gfx/villa/wf1a.png");
        krabat_front[1] = getPicture("gfx/villa/wf1b.png");
        krabat_front[2] = getPicture("gfx/villa/wf1c.png");

        krabat_back[0] = getPicture("gfx/villa/wf2.png");
        krabat_back[1] = getPicture("gfx/villa/wf2g.png");
        krabat_back[2] = getPicture("gfx/villa/wf2h.png");
        krabat_back[3] = getPicture("gfx/villa/wf2g.png");

        krabat_talk[0] = getPicture("gfx/villa/wf2.png");
        krabat_talk[1] = getPicture("gfx/villa/wf2a.png");
        krabat_talk[2] = getPicture("gfx/villa/wf2d.png");
        krabat_talk[3] = getPicture("gfx/villa/wf2e.png");
        krabat_talk[4] = getPicture("gfx/villa/wf2f.png");

        krabat_haende[0] = getPicture("gfx/villa/wf2.png");
        krabat_haende[1] = getPicture("gfx/villa/wf2b.png");
        krabat_haende[2] = getPicture("gfx/villa/wf2c.png");
        krabat_haende[3] = getPicture("gfx/villa/wf2e.png");
        krabat_haende[4] = getPicture("gfx/villa/wf2f.png");

        krabatw_left[0] = getPicture("gfx/villa/wf3a2.png");
        krabatw_left[1] = getPicture("gfx/villa/wf4a2.png");
        krabatw_left[2] = getPicture("gfx/villa/wf5a2.png");
        krabatw_left[3] = getPicture("gfx/villa/wf6a2.png");

        krabatw_front[0] = getPicture("gfx/villa/wf1a2.png");
        krabatw_front[1] = getPicture("gfx/villa/wf1b2.png");
        krabatw_front[2] = getPicture("gfx/villa/wf1c2.png");

        krabatw_abnehm = getPicture("gfx/villa/wf2i.png");
    }


    // Laufen mit Plokarka ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        horizontal = Thorizontal;
        walkto = Twalkto;
        directionX = tDirectionX;
        directionY = tDirectionY;

        if (horizontal)
        // Horizontal laufen
        {
            // neuen Punkt ermitteln und setzen
            VerschiebeX();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == 4) {
                anim_pos = 0;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkto.x - (int) txps) * directionX.getVal() <= 0) {
                // System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                setPos(walkto);
                anim_pos = 0;
                return true;
            }
        } else
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == (directionY == DOWN ? 3 : 4)) {
                anim_pos = 0;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * directionY.getVal() <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                setPos(walkto);
                anim_pos = 0;
                return true;
            }
        }
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    private void VerschiebeX() {
        verschiebeXdefault(CHORIZ_DIST[anim_pos], SLOWX);
    }

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        verschiebeYdefault(CVERT_DIST, SLOWY);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        moveToDefault(aim);
        Thorizontal = calcHorizontal(aim, 22);

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawPlokarka(GenericDrawingContext offGraph) {
        // je nach Richtung Sprite auswaehlen und zeichnen
        if (horizontal) {
            // nach links laufen
            if (directionX == LEFT) {
                MaleIhn(offGraph, hasWaesche ? krabatw_left[anim_pos] : krabat_left[anim_pos]);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                MaleIhn(offGraph, krabat_right[anim_pos]);
            }
        } else {
            // Bei normaler Darstellung
            if (!upsidedown) {
                // nach oben laufen
                if (directionY == UP) {
                    MaleIhn(offGraph, krabat_back[anim_pos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    MaleIhn(offGraph, hasWaesche ? krabatw_front[anim_pos] : krabat_front[anim_pos]);
                }
            } else {
                // nach oben laufen
                if (directionY == UP) {
                    MaleIhn(offGraph, krabat_front[anim_pos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    MaleIhn(offGraph, krabat_back[anim_pos]);
                }
            }
        }
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

    // Lasse Plokarka Waeschestuecke abnehmen
    public boolean nimmWaescheAb(GenericDrawingContext g) {
        MaleIhn(g, krabatw_abnehm);
        if (Abnehmcount-- < 1) {
            Abnehmcount = MAX_ABNEHMCOUNT;
            return false;
        }
        return true;
    }

    // Zeichne Plokarka normal Schimpfend
    public void talkPlokarka(GenericDrawingContext offGraph) {
        if (--Notalk < 1) {
            Notalk = MAX_NOTALK;
            TalkPic = (int) Math.round(Math.random() * 5);
            if (TalkPic == 5) {
                TalkPic = 4;
            }
        }
        MaleIhn(offGraph, krabat_talk[TalkPic]);
    }

    // Zeichne Plokarka mit Haende hoch
    public void haendePlokarka(GenericDrawingContext offGraph) {
        if (--Notalk < 1) {
            Notalk = MAX_NOTALK;

            if (--Nohaendefuchtel < 1) {
                Nohaendefuchtel = MAX_NOHAENDEFUCHTEL;

                TalkPic = (int) Math.round(Math.random() * 5);
                if (TalkPic == 5) {
                    TalkPic = 4;
                }
            }

            // sind Haende schon oben ? dann nur switchen
            if (TalkPic == 1 || TalkPic == 2) {
                TalkPic = (int) Math.round(Math.random() * 2);
                TalkPic++;
                if (TalkPic == 3) {
                    TalkPic = 1;
                }
            }
            // Haende sind unten, also dort lassen
            else {
                do {
                    TalkPic = (int) Math.round(Math.random() * 5);
                }
                while (TalkPic != 0 && TalkPic != 3 && TalkPic != 4);
            }
        }
        MaleIhn(offGraph, krabat_haende[TalkPic]);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int pox, int poy) {
        return calcLeftPosDefault(pox, poy);
    }

    @Override
    protected int getUpPos(int poy) {
        return calcUpPosDefault(poy);
    }

    @Override
    protected int getScale(int poy) {
        return calcScaleDefault(poy, defScale);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scale / 2, CHEIGHT - scale);
    }
}