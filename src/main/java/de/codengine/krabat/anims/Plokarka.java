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
        krabat_left[0] = getPicture("gfx/villa/wf3a.gif");
        krabat_left[1] = getPicture("gfx/villa/wf4a.gif");
        krabat_left[2] = getPicture("gfx/villa/wf5a.gif");
        krabat_left[3] = getPicture("gfx/villa/wf6a.gif");

        krabat_right[0] = getPicture("gfx/villa/wf3.gif");
        krabat_right[1] = getPicture("gfx/villa/wf4.gif");
        krabat_right[2] = getPicture("gfx/villa/wf5.gif");
        krabat_right[3] = getPicture("gfx/villa/wf6.gif");

        krabat_front[0] = getPicture("gfx/villa/wf1a.gif");
        krabat_front[1] = getPicture("gfx/villa/wf1b.gif");
        krabat_front[2] = getPicture("gfx/villa/wf1c.gif");

        krabat_back[0] = getPicture("gfx/villa/wf2.gif");
        krabat_back[1] = getPicture("gfx/villa/wf2g.gif");
        krabat_back[2] = getPicture("gfx/villa/wf2h.gif");
        krabat_back[3] = getPicture("gfx/villa/wf2g.gif");

        krabat_talk[0] = getPicture("gfx/villa/wf2.gif");
        krabat_talk[1] = getPicture("gfx/villa/wf2a.gif");
        krabat_talk[2] = getPicture("gfx/villa/wf2d.gif");
        krabat_talk[3] = getPicture("gfx/villa/wf2e.gif");
        krabat_talk[4] = getPicture("gfx/villa/wf2f.gif");

        krabat_haende[0] = getPicture("gfx/villa/wf2.gif");
        krabat_haende[1] = getPicture("gfx/villa/wf2b.gif");
        krabat_haende[2] = getPicture("gfx/villa/wf2c.gif");
        krabat_haende[3] = getPicture("gfx/villa/wf2e.gif");
        krabat_haende[4] = getPicture("gfx/villa/wf2f.gif");

        krabatw_left[0] = getPicture("gfx/villa/wf3a2.gif");
        krabatw_left[1] = getPicture("gfx/villa/wf4a2.gif");
        krabatw_left[2] = getPicture("gfx/villa/wf5a2.gif");
        krabatw_left[3] = getPicture("gfx/villa/wf6a2.gif");

        krabatw_front[0] = getPicture("gfx/villa/wf1a2.gif");
        krabatw_front[1] = getPicture("gfx/villa/wf1b2.gif");
        krabatw_front[2] = getPicture("gfx/villa/wf1c2.gif");

        krabatw_abnehm = getPicture("gfx/villa/wf2i.gif");
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
                SetPlokarkaPos(walkto);
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
                SetPlokarkaPos(walkto);
                anim_pos = 0;
                return true;
            }
        }
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    private void VerschiebeX() {
        // Skalierungsfaktor holen
        int scale = getScale((int) yps);

        // Zooming - Faktor beruecksichtigen in x - Richtung
        float horiz_dist = CHORIZ_DIST[anim_pos] - (float) scale / SLOWX;
        if (horiz_dist < 1) {
            horiz_dist = 1;
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(xps - walkto.x) / horiz_dist;

        typs = yps;
        if (z != 0) {
            typs += directionY.getVal() * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + directionX.getVal() * horiz_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        // Skalierungsfaktor holen
        int scale = getScale((int) yps);

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vert_dist = CVERT_DIST - (float) scale / SLOWY;
        if (vert_dist < 1) {
            vert_dist = 1;
            // hier kann noch eine Entscheidungsroutine hin, die je nach Animationsphase
            // und vert_distance ein Pixel erlaubt oder nicht
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vert_dist;

        txps = xps;
        if (z != 0) {
            txps += directionX.getVal() * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + directionY.getVal() * vert_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        boolean horiz = false;

        // Horizontal oder verikal laufen ?
        if (aim.x != (int) xps) {
            // Winkel berechnen, den Krabat laufen soll
            double yangle = Math.abs(aim.y - (int) yps);
            double xangle = Math.abs(aim.x - (int) xps);
            double angle = Math.atan(yangle / xangle);
            horiz = !(angle > 22 * Math.PI / 180);
        }

        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = horiz;

        // Laufrichtung ermitteln
        tDirectionX = aim.x > (int) xps ? RIGHT : LEFT;
        tDirectionY = aim.y > (int) yps ? DOWN : UP;

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetPlokarkaPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
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

    // fuer Debugging public - wird wieder private !!!
    protected int getScale(int poy) {

        // Hier kann override eingeschaltet werden (F7/F8)
        // return mainFrame.override;

        // Ermittlung der Hoehendifferenz beim Zooming
        if (!upsidedown) {
            // normale Berechnung
            float helper = (maxx - poy) / zoomf;
            if (helper < 0) {
                helper = 0;
            }
            helper += defScale;
            return (int) helper;
        } else {
            // Berechnung bei "upsidedown" - Berg/Tallauf
            float help2 = poy / zoomf;
            if (help2 < 0) {
                help2 = 0;
            }
            help2 += defScale;
            // System.out.println (minx + " + " + poy + " und " + zoomf + " ergeben " + help2);
            return (int) help2;
        }
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    private void KrabatClip(GenericDrawingContext g, int xx, int yy) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos(xx, yy);
        int y = getUpPos(yy);
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd = 2 * (xx - x);
        int yd = yy - y;
        g.setClip(x, y, xd, yd);

        // Fuer Debugging ClipRectangle zeichnen
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scale / 2, CHEIGHT - scale);
    }
}