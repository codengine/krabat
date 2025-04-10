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

public class DinglingerWalk extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(DinglingerWalk.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_left_talk_head;
    private final GenericImage[] krabat_right_talk_head;

    private final GenericImage[] krabat_left_talk_body;
    private final GenericImage[] krabat_right_talk_body;

    private final GenericImage[] krabat_left_walk;
    private final GenericImage[] krabat_right_walk;

    private final GenericImage[] krabat_skla;

    // Spritevariablen
    private static final int CWIDTH = 88;  // Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 210;

    private static final int CEXTRAWIDTH = 97; // Hoehe/Breite fuer Images von vorn/hinten
    // private static final int CEXTRAHEIGHT = 211;

    // Abstaende default
    private static final int[] CHORIZ_DIST = {15, 24, 27, 24, 27};
    // private static final int[] YCORRECT    = { 0,  2,  1,  2,  1};

    // Variablen fuer Laufberechnung
    // private static final int CLOHNENX = 6;  // Werte fuer Entscheidung, ob sich
    // private static final int CLOHNENY = 6;  // Laufen ueberhaupt lohnt (halber Schritt)

    // Variablen fuer Animationen
    // public  int nAnimation = 0;           // ID der ggw. Animation
    // public  boolean fAnimHelper = false;  // Hilfsflag bei Animation
    // private int nAnimStep = 0;            // ggw. Pos in Animation

    // den Hintergrund geht (bildabhaengig)
    private static final int SLOWX = 8;  // Konstante, die angibt, wie sich die x - Abstaende

    private boolean isStanding = true;

    private int Head = 0;
    private int Body = 0;

    private int Verhinderhead;

    private static final int MAX_VERHINDERHEAD = 2;

    private static final int BODYOFFSET = 47;

    private int Verhinderwalk;

    private static final int MAX_VERHINDERWALK = 2;

    private int AnimCounter = 0;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public DinglingerWalk(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_left_talk_head = new GenericImage[10];
        krabat_right_talk_head = new GenericImage[9];

        krabat_left_talk_body = new GenericImage[6];
        krabat_right_talk_body = new GenericImage[1];

        krabat_left_walk = new GenericImage[5];
        krabat_right_walk = new GenericImage[5];

        krabat_skla = new GenericImage[3];

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderwalk = MAX_VERHINDERWALK;
    }

    // Bilder vorbereiten
    private void InitImages() {
        krabat_left_talk_head[0] = getPicture("gfx-dd/dingl/dingl-t1.png");
        krabat_left_talk_head[1] = getPicture("gfx-dd/dingl/dingl-t1a.png");
        krabat_left_talk_head[2] = getPicture("gfx-dd/dingl/dingl-t2.png");
        krabat_left_talk_head[3] = getPicture("gfx-dd/dingl/dingl-t3.png");
        krabat_left_talk_head[4] = getPicture("gfx-dd/dingl/dingl-t4.png");
        krabat_left_talk_head[5] = getPicture("gfx-dd/dingl/dingl-t5.png");
        krabat_left_talk_head[6] = getPicture("gfx-dd/dingl/dingl-t6.png");
        krabat_left_talk_head[7] = getPicture("gfx-dd/dingl/dingl-t7.png");
        krabat_left_talk_head[8] = getPicture("gfx-dd/dingl/dingl-t8.png");
        krabat_left_talk_head[9] = getPicture("gfx-dd/dingl/dingl-t9.png");

        krabat_right_talk_head[0] = getPicture("gfx-dd/dingl/dingl-tr1.png");
        krabat_right_talk_head[1] = getPicture("gfx-dd/dingl/dingl-tr2.png");
        krabat_right_talk_head[2] = getPicture("gfx-dd/dingl/dingl-tr3.png");
        krabat_right_talk_head[3] = getPicture("gfx-dd/dingl/dingl-tr4.png");
        krabat_right_talk_head[4] = getPicture("gfx-dd/dingl/dingl-tr5.png");
        krabat_right_talk_head[5] = getPicture("gfx-dd/dingl/dingl-tr6.png");
        krabat_right_talk_head[6] = getPicture("gfx-dd/dingl/dingl-tr7.png");
        krabat_right_talk_head[7] = getPicture("gfx-dd/dingl/dingl-tr8.png");
        krabat_right_talk_head[8] = getPicture("gfx-dd/dingl/dingl-tr9.png");

        krabat_left_talk_body[0] = getPicture("gfx-dd/dingl/dingl-b1.png");
        krabat_left_talk_body[1] = getPicture("gfx-dd/dingl/dingl-berlaubnis.png");
        krabat_left_talk_body[2] = getPicture("gfx-dd/dingl/dingl-bskla.png");
        krabat_left_talk_body[3] = getPicture("gfx-dd/dingl/dingl-l0a.png");
        krabat_left_talk_body[4] = getPicture("gfx-dd/dingl/dingl-l0b.png");
        krabat_left_talk_body[5] = getPicture("gfx-dd/dingl/dingl-l0c.png");

        krabat_right_talk_body[0] = getPicture("gfx-dd/dingl/dingl-b2.png");

        krabat_left_walk[0] = getPicture("gfx-dd/dingl/dingl-l0.png");
        krabat_left_walk[1] = getPicture("gfx-dd/dingl/dingl-r2.png");
        krabat_left_walk[2] = getPicture("gfx-dd/dingl/dingl-r4.png");
        krabat_left_walk[3] = getPicture("gfx-dd/dingl/dingl-r7.png");
        krabat_left_walk[4] = getPicture("gfx-dd/dingl/dingl-r9.png");

        krabat_right_walk[0] = getPicture("gfx-dd/dingl/dingl-r0.png");
        krabat_right_walk[1] = getPicture("gfx-dd/dingl/dingl-l2.png");
        krabat_right_walk[2] = getPicture("gfx-dd/dingl/dingl-l4.png");
        krabat_right_walk[3] = getPicture("gfx-dd/dingl/dingl-l7.png");
        krabat_right_walk[4] = getPicture("gfx-dd/dingl/dingl-l9.png");

        krabat_skla[0] = getPicture("gfx-dd/dingl/dingl-skla1.png");
        krabat_skla[1] = getPicture("gfx-dd/dingl/dingl-skla2.png");
        krabat_skla[2] = getPicture("gfx-dd/dingl/dingl-skla3.png");
    }

    @Override
    public void cleanup() {
        krabat_left_talk_head[0] = null;
        krabat_left_talk_head[1] = null;
        krabat_left_talk_head[2] = null;
        krabat_left_talk_head[3] = null;
        krabat_left_talk_head[4] = null;
        krabat_left_talk_head[5] = null;
        krabat_left_talk_head[6] = null;
        krabat_left_talk_head[7] = null;
        krabat_left_talk_head[8] = null;
        krabat_left_talk_head[9] = null;

        krabat_right_talk_head[0] = null;
        krabat_right_talk_head[1] = null;
        krabat_right_talk_head[2] = null;
        krabat_right_talk_head[3] = null;
        krabat_right_talk_head[4] = null;
        krabat_right_talk_head[5] = null;
        krabat_right_talk_head[6] = null;
        krabat_right_talk_head[7] = null;
        krabat_right_talk_head[8] = null;

        krabat_left_talk_body[0] = null;
        krabat_left_talk_body[1] = null;
        krabat_left_talk_body[2] = null;
        krabat_left_talk_body[3] = null;
        krabat_left_talk_body[4] = null;
        krabat_left_talk_body[5] = null;

        krabat_right_talk_body[0] = null;

        krabat_left_walk[0] = null;
        krabat_left_walk[1] = null;
        krabat_left_walk[2] = null;
        krabat_left_walk[3] = null;
        krabat_left_walk[4] = null;

        krabat_right_walk[0] = null;
        krabat_right_walk[1] = null;
        krabat_right_walk[2] = null;
        krabat_right_walk[3] = null;
        krabat_right_walk[4] = null;

        krabat_skla[0] = null;
        krabat_skla[1] = null;
        krabat_skla[2] = null;
    }


    // Laufen mit Dinglinger ////////////////////////////////////////////////////////////////

    // Dinglinger um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        // horizontal = Thorizontal;
        walkto = Twalkto;
        directionX = tDirectionX;
        directionY = tDirectionY;

        if (--Verhinderwalk < 1)
        // Horizontal laufen
        {
            Verhinderwalk = MAX_VERHINDERWALK;

            // neuen Punkt ermitteln und setzen
            VerschiebeX();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == 5) {
                anim_pos = 1;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkto.x - (int) txps) * directionX.getVal() <= 0) {
                // System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                setPos(walkto);
                anim_pos = 0;
                isStanding = true;
                return true;
            }
        }

        // er ist noch nicht fertig mit laufen
        isStanding = false;
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    private void VerschiebeX() {
        verschiebeXdefault(CHORIZ_DIST[anim_pos], SLOWX);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        moveToDefault(aim);

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawDinglinger(GenericDrawingContext offGraph, int AnimID) {
        // AnimIDs
        // 1 : zeige Erlaubnis
        // 4 : Schuessel geben

        // je nach Richtung Sprite auswaehlen und zeichnen
        if (!isStanding) {
            // nach links laufen
            if (directionX == LEFT) {
                MaleIhn(offGraph, krabat_left_walk[anim_pos], false);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                MaleIhn(offGraph, krabat_right_walk[anim_pos], false);
            }
        } else {

            // Body je nach AnimID evaluieren
            switch (AnimID) {
                case 1:
                    Body = 1; // Erlaubnis zeigen
                    break;
                case 4:
                    Body = 2; // Schuessel geben
                    break;
                default:
                    Body = 0; // normaler Body
                    break;
            }

            // Zwinkern eval. wenn links
            if (directionX == LEFT) {
                if (Head > 0) {
                    Head = 0;
                } else {
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 45) {
                        Head = 1;
                    }
                }

                MaleIhn(offGraph);
            }

            // zeichnen, wenn rechts steht
            if (directionX == RIGHT) {
                Head = 0;

                MaleIhn(offGraph);
            }
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

    // Zeichne dinglinger normal redend
    public void talkDinglinger(GenericDrawingContext offGraph, int AnimID) {
        // AnimIDs
        // 1 : zeige Erlaubnis
        // 4 : Schuessel geben

        // Heads evaluieren
        if (--Verhinderhead < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            Head = (int) (Math.random() * (directionX == LEFT ? 9.9 : 8.9));
        }

        // Body je nach AnimID evaluieren
        switch (AnimID) {
            case 1:
                Body = 1; // Erlaubnis zeigen
                break;
            case 4:
                Body = 2; // Schuessel geben
                break;
            default:
                Body = 0; // normaler Body
                break;
        }

        MaleIhn(offGraph);
    }

    public int DoAnimation(GenericDrawingContext g, int AnimID) {
        // AnimID-Bedeutungen
        // 2 = umschauen links
        // 3 = Schuessel nehmen

        // Reset des AnimCounters
        if (AnimCounter > 99) {
            AnimCounter = 0;
        }

        // schaue Dich um
        if (AnimID == 2) {
            if (AnimCounter < 15) {
                MaleIhn(g, krabat_left_talk_body[AnimCounter / 5 + 3], false);
            } else {
                AnimCounter = 99;
                MaleIhn(g, krabat_left_talk_body[5], false);
            }

            AnimCounter++;
            return AnimCounter;
        }

        // nimm Schuessel
        if (AnimID == 3) {
            if (AnimCounter < 9) {
                MaleIhn(g, krabat_skla[AnimCounter / 3], true);
            } else {
                AnimCounter = 99;
                MaleIhn(g, krabat_skla[0], true);
            }

            AnimCounter++;
            return AnimCounter;
        }

        // Compilerberuhigung
        return 0;
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        float fScaleY = getScale(poy) * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        return pox - Koerperbreite / 2;
    }

    // Left-Pos bei Schuessel
    private int getLeftPosSkla(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int helper = getScale(poy);
        return pox - (CEXTRAWIDTH - helper / 2) / 2;
    }

    @Override
    protected int getUpPos(int poy) {
        return calcUpPosDefault(poy);
    }

    @Override
    protected int getScale(int poy) {
        return calcScaleDefault(poy, defScale);
    }

    // gib TalkPoint von Dinglinger zurueck
    public GenericPoint evalTalkPoint() {
        BorderRect temp = getRect();
        return new GenericPoint((temp.lo_point.x + temp.ru_point.x) / 2, temp.lo_point.y - 50);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp, boolean isSkla) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) xps, (int) yps);

        int left;

        // Groesse und Position der Figur berechnen
        if (!isSkla) {
            left = getLeftPos((int) xps, (int) yps);
        } else {
            left = getLeftPosSkla((int) xps, (int) yps);
        }
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);
    }

    private void MaleIhn(GenericDrawingContext g) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        if (directionX == LEFT) {
            // nach links zeichnen
            g.drawImage(krabat_left_talk_head[Head], left, up, Koerperbreite, Kopfhoehe);
            g.drawImage(krabat_left_talk_body[Body], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
        } else {
            // nach rechts zeichnen
            g.drawImage(krabat_right_talk_head[Head], left, up, Koerperbreite, Kopfhoehe);
            g.drawImage(krabat_right_talk_body[Body], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
        }
    }
}