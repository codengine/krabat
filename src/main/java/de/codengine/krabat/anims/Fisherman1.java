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

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public class Fisherman1 extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Fisherman1.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] angler_left_stand;
    private final GenericImage[] angler_right_stand;
    private final GenericImage[] angler_walk;
    private final GenericImage[] angler_walk_eimer;
    private final GenericImage[] angler_left_talk_head;
    private final GenericImage[] angler_left_talk_body;
    private final GenericImage[] angler_right_talk_head;
    private final GenericImage[] angler_right_talk_body;

    // Spritevariablen
    private static final int CWIDTH = 100;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 100;

    // Abstaende default
    private static final int[] CHORIZ_DIST = {0, 17, 12, 14, 12, 14};

    private int Stand = 0;

    private int Verhinderstand;
    private static final int MAX_VERHINDERSTAND = 50;

    private boolean laeuftNicht = true;

    private int Head = 0;
    private int Verhinderhead;
    private static final int MAX_VERHINDERHEAD = 2;

    private int Verhinderwalk;
    private static final int MAX_VERHINDERWALK = 2;

    private boolean hasEimer = false;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Fisherman1(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        angler_left_stand = new GenericImage[6];
        angler_right_stand = new GenericImage[6];
        angler_walk = new GenericImage[6];
        angler_walk_eimer = new GenericImage[6];
        angler_left_talk_head = new GenericImage[7];
        angler_left_talk_body = new GenericImage[3];
        angler_right_talk_head = new GenericImage[7];
        angler_right_talk_body = new GenericImage[3];

        InitImages();

        Verhinderstand = MAX_VERHINDERSTAND;
        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderwalk = MAX_VERHINDERWALK;
    }

    // Bilder vorbereiten
    private void InitImages() {
        angler_left_stand[0] = getPicture("gfx/haty/ang1-1.png");
        angler_left_stand[1] = getPicture("gfx/haty/ang1-1a.png");
        angler_left_stand[2] = getPicture("gfx/haty/ang1-2.png");
        angler_left_stand[3] = getPicture("gfx/haty/ang1-2a.png");
        angler_left_stand[4] = getPicture("gfx/haty/ang1-3.png");
        angler_left_stand[5] = getPicture("gfx/haty/ang1-3a.png");

        angler_right_stand[0] = getPicture("gfx/haty/rang1-1.png");
        angler_right_stand[1] = getPicture("gfx/haty/rang1-1a.png");
        angler_right_stand[2] = getPicture("gfx/haty/rang1-2.png");
        angler_right_stand[3] = getPicture("gfx/haty/rang1-2a.png");
        angler_right_stand[4] = getPicture("gfx/haty/rang1-3.png");
        angler_right_stand[5] = getPicture("gfx/haty/rang1-3a.png");

        angler_walk[0] = getPicture("gfx/haty/rang1-w0.png");
        angler_walk[1] = getPicture("gfx/haty/ang1-w0.png");
        angler_walk[2] = getPicture("gfx/haty/ang1-w7.png");
        angler_walk[3] = getPicture("gfx/haty/ang1-w9.png");
        angler_walk[4] = getPicture("gfx/haty/ang1-w2.png");
        angler_walk[5] = getPicture("gfx/haty/ang1-w4.png");

        angler_walk_eimer[0] = getPicture("gfx/haty/ang1-we.png");
        angler_walk_eimer[1] = getPicture("gfx/haty/ang1-w0e.png");
        angler_walk_eimer[2] = getPicture("gfx/haty/ang1-w7e.png");
        angler_walk_eimer[3] = getPicture("gfx/haty/ang1-w9e.png");
        angler_walk_eimer[4] = getPicture("gfx/haty/ang1-w2e.png");
        angler_walk_eimer[5] = getPicture("gfx/haty/ang1-w4e.png");

        angler_left_talk_head[0] = getPicture("gfx/haty/ang1-h1.png");
        angler_left_talk_head[1] = getPicture("gfx/haty/ang1-h2.png");
        angler_left_talk_head[2] = getPicture("gfx/haty/ang1-h3.png");
        angler_left_talk_head[3] = getPicture("gfx/haty/ang1-h4.png");
        angler_left_talk_head[4] = getPicture("gfx/haty/ang1-h5.png");
        angler_left_talk_head[5] = getPicture("gfx/haty/ang1-h6.png");
        angler_left_talk_head[6] = getPicture("gfx/haty/ang1-h7.png");

        angler_left_talk_body[0] = getPicture("gfx/haty/ang1-b1.png");
        angler_left_talk_body[1] = getPicture("gfx/haty/ang1-b2.png");
        angler_left_talk_body[2] = getPicture("gfx/haty/ang1-b3.png");

        angler_right_talk_head[0] = getPicture("gfx/haty/rang1-h1.png");
        angler_right_talk_head[1] = getPicture("gfx/haty/rang1-h2.png");
        angler_right_talk_head[2] = getPicture("gfx/haty/rang1-h3.png");
        angler_right_talk_head[3] = getPicture("gfx/haty/rang1-h4.png");
        angler_right_talk_head[4] = getPicture("gfx/haty/rang1-h5.png");
        angler_right_talk_head[5] = getPicture("gfx/haty/rang1-h6.png");
        angler_right_talk_head[6] = getPicture("gfx/haty/rang1-h7.png");

        angler_right_talk_body[0] = getPicture("gfx/haty/rang1-b1.png");
        angler_right_talk_body[1] = getPicture("gfx/haty/rang1-b2.png");
        angler_right_talk_body[2] = getPicture("gfx/haty/rang1-b3.png");
    }


    // Laufen mit Plokarka ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        walkto = Twalkto;
        directionX = tDirectionX;
        directionY = tDirectionY;

        if (--Verhinderwalk > 0) {
            laeuftNicht = false;
            return false;
        }

        Verhinderwalk = MAX_VERHINDERWALK;

        if (anim_pos < 1) {
            // null-Position nur zeigen
            anim_pos++;
            laeuftNicht = false;
            return false;
        }

        // neuen Punkt ermitteln und setzen
        VerschiebeX();
        xps = txps;
        yps = typs;

        // Animationsphase weiterschalten
        anim_pos++;
        if (anim_pos == 6) {
            anim_pos = 2;
        }

        // Naechsten Schritt auf Gueltigkeit ueberpruefen
        VerschiebeX();

        // Ueberschreitung feststellen in X - Richtung
        if ((walkto.x - (int) txps) * directionX.getVal() <= 0) {
            // System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
            setPos(walkto);
            anim_pos = 1;
            laeuftNicht = true;
            return true;
        }

        laeuftNicht = false;
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    private void VerschiebeX() {
        verschiebeXdefault(CHORIZ_DIST[anim_pos]);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim, boolean clearAnimPos) {
        laeuftNicht = false;
        moveToDefault(aim);
        anim_pos = clearAnimPos ? 0 : 1;       // Animationsimage bei Neubeginn initialis.
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawWudzer1(GenericDrawingContext offGraph) {
        if (laeuftNicht) {
            // beim Sitzen aufrufen
            // Positionen umschalten bzw. Zwinkern berechnen
            if (--Verhinderstand < 1) {
                Verhinderstand = MAX_VERHINDERSTAND;
                Stand = (int) (Math.random() * 5.9);
            }

            // Zwinkern evaluieren
            if (Stand % 2 != 0) {
                Stand--;
            } else {
                int zuffi = (int) Math.round(Math.random() * 50);
                if (zuffi > 45) {
                    Stand++;
                }
            }

            // links sitzen
            if (directionX == LEFT) {
                MaleIhn(offGraph, angler_left_stand[Stand]);
            }

            // rechts sitzen
            if (directionX == RIGHT) {
                MaleIhn(offGraph, angler_right_stand[Stand]);
            }
        } else {
            // hier wird gelaufen
            MaleIhn(offGraph, !hasEimer ? angler_walk[anim_pos] : angler_walk_eimer[anim_pos]);
        }
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void SetFacing(int direction) {
        switch (direction) {
            case 3:
                // horizontal=true;
                directionX = RIGHT;
                break;
            case 6:
                // horizontal=false;
                directionY = DOWN;
                break;
            case 9:
                // horizontal=true;
                directionX = LEFT;
                break;
            case 12:
                // horizontal=false;
                directionY = UP;
                break;
            default:
                log.debug("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Zeichne Wudzer1 beim Sprechen mit anderen Personen
    public void talkWudzer1(GenericDrawingContext g, boolean isLookingAround) {
        // beim Rumsitzen, selbe Bodyevaluation wie beim rumstehen/sitzen
        // Positionen umschalten
        if (--Verhinderstand < 1) {
            Verhinderstand = MAX_VERHINDERSTAND;
            Stand = (int) (Math.random() * 5.9);
        }

        // Head evaluieren
        if (--Verhinderhead < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            Head = (int) (Math.random() * 5.9);
        }

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps);
        int up = getUpPos((int) yps);

        // Figur zeichnen
        if (directionX == RIGHT) {
            // nach rechts angeln
            g.drawImage(angler_right_talk_body[Stand / 2], left, up);
            g.drawImage(angler_right_talk_head[isLookingAround ? 6 : Head], left + 22, up + 18);
        } else {
            // nach links angeln
            g.drawImage(angler_left_talk_body[Stand / 2], left, up);
            g.drawImage(angler_left_talk_head[isLookingAround ? 6 : Head], left + 65, up + 18);
        }
    }

    public void takeBow(GenericDrawingContext g, boolean hatJetztEimer) {
        hasEimer = hatJetztEimer;
        MaleIhnGebueckt(g, angler_walk_eimer[0]);
    }

    // Zooming-Variablen berechnen
    private int getLeftPos(int pox) {
        return getLeftPos(pox, 1);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int pox, int ignored) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        return pox - CWIDTH / (laeuftNicht ? 2 : 4);
    }

    @Override
    protected int getUpPos(int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        return poy - CHEIGHT;
    }

    @Override
    protected int getScale(int poy) {
        return calcScaleDefault(poy);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    @Override
    public BorderRect getRect() {
        int x = getLeftPos((int) xps);
        int y = getUpPos((int) yps);

        // hier Unterscheidung nach Richtung, da die Angler viel breitere Images haben
        if (directionX == RIGHT) {
            // schauen nach rechts
            return new BorderRect(x, y + 15, x + 49, y + 60);
        } else {
            // schauen nach links
            return new BorderRect(x + 52, y + 15, x + 100, y + 60);
        }
    }

    public GenericPoint Wudzer1TalkPoint() {
        return new GenericPoint((int) xps, (int) yps - CHEIGHT - 50);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen, wir nehmen an, dass Haty die richtige Region bereitstellt...
        // KrabatClip(g, ((int) xps), ((int) yps));

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps);
        int up = getUpPos((int) yps);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - (!laeuftNicht ? 50 : 0), CHEIGHT);
    }

    private void MaleIhnGebueckt(GenericDrawingContext g, GenericImage ktemp) {
        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps);
        int up = getUpPos((int) yps);

        // Figur zeichnen
        g.drawImage(ktemp, left + 10, up, CWIDTH - 30, CHEIGHT);
    }
}