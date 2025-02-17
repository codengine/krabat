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

public class Korcmar extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Korcmar.class);
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

    // Variablen fuer Laufberechnung
    // private static final int CLOHNENX = 6;  // Werte fuer Entscheidung, ob sich
    // private static final int CLOHNENY = 6;  // Laufen ueberhaupt lohnt (halber Schritt)

    // Variablen fuer Animationen
    // public  int nAnimation = 0;           // ID der ggw. Animation
    // public  boolean fAnimHelper = false;  // Hilfsflag bei Animation
    // private int nAnimStep = 0;            // ggw. Pos in Animation

    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung

    // Redevariablen
    private int Kopf = 0;
    private int Body = 0;

    private int Verhinderkopf;
    private int Verhinderbody;
    private static final int MAX_VERHINDERKOPF = 2;
    private static final int MAX_VERHINDERBODY = 8;

    // private static final int BODYOFFSET = 34;
    private static final int HEADHEIGHT = 33;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Korcmar(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_front = new GenericImage[6];
        krabat_back = new GenericImage[5];

        krabat_talk_head = new GenericImage[7];
        krabat_talk_body = new GenericImage[4];

        InitImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    // Bilder vorbereiten
    private void InitImages() {
        krabat_back[0] = getPicture("gfx/hoscenc/wirt-o.gif");
        krabat_back[1] = getPicture("gfx/hoscenc/wirt-o1.gif");
        krabat_back[2] = getPicture("gfx/hoscenc/wirt-o2.gif");
        krabat_back[3] = getPicture("gfx/hoscenc/wirt-o3.gif");
        krabat_back[4] = getPicture("gfx/hoscenc/wirt-o4.gif");

        krabat_front[0] = getPicture("gfx/hoscenc/wirt-u.gif");
        krabat_front[1] = getPicture("gfx/hoscenc/wirt-ua.gif");
        krabat_front[2] = getPicture("gfx/hoscenc/wirt-u1.gif");
        krabat_front[3] = getPicture("gfx/hoscenc/wirt-u2.gif");
        krabat_front[4] = getPicture("gfx/hoscenc/wirt-u3.gif");
        krabat_front[5] = getPicture("gfx/hoscenc/wirt-u4.gif");

        krabat_talk_head[0] = getPicture("gfx/hoscenc/wirt-h0.gif");
        krabat_talk_head[1] = getPicture("gfx/hoscenc/wirt-h1.gif");
        krabat_talk_head[2] = getPicture("gfx/hoscenc/wirt-h2.gif");
        krabat_talk_head[3] = getPicture("gfx/hoscenc/wirt-h3.gif");
        krabat_talk_head[4] = getPicture("gfx/hoscenc/wirt-h4.gif");
        krabat_talk_head[5] = getPicture("gfx/hoscenc/wirt-h5.gif");
        krabat_talk_head[6] = getPicture("gfx/hoscenc/wirt-h6.gif");

        krabat_talk_body[0] = getPicture("gfx/hoscenc/wirt-b0.gif");
        krabat_talk_body[1] = getPicture("gfx/hoscenc/wirt-b1.gif");
        krabat_talk_body[2] = getPicture("gfx/hoscenc/wirt-b2.gif");
        krabat_talk_body[3] = getPicture("gfx/hoscenc/wirt-b3.gif");
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
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        horizontal = Thorizontal;
        walkto = Twalkto;
        directionX = tDirectionX;
        directionY = tDirectionY;

        if (!horizontal)
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == (directionY == DOWN ? 6 : 5)) {
                anim_pos = directionY == DOWN ? 2 : 1;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * directionY.getVal() <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                SetKorcmarPos(walkto);
                anim_pos = 0;
                return true;
            }
        }

        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        // Skalierungsfaktor holen
        int scale = getScale((int) yps);

        float vert_dist;
        // Zooming - Faktor beruecksichtigen in y-Richtung
        if (directionY == DOWN) {
            vert_dist = CVERT_UNTEN[anim_pos] - (float) scale / SLOWY;
        } else {
            vert_dist = CVERT_OBEN[anim_pos] - (float) scale / SLOWY;
        }

        if (vert_dist < 1) {
            vert_dist = 1;
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
        // Laufrichtung ermitteln
        final DirectionX xricht = aim.x > (int) xps ? RIGHT : LEFT;
        final DirectionY yricht = aim.y > (int) yps ? DOWN : UP;

        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = false;
        tDirectionX = xricht;
        tDirectionY = yricht;

        if (anim_pos == 0) {
            anim_pos = yricht == DOWN ? 2 : 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetKorcmarPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    /*public GenericPoint GetPlokarkaPos ()
      {
      //System.out.println(" Aktuelle Pos : "+pos_x+" "+pos_y);
      return (new GenericPoint (((int) xps), ((int) yps)));
      }*/

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawKorcmar(GenericDrawingContext offGraph) {
        // nach oben laufen
        if (directionY == UP) {
            MaleIhn(offGraph, krabat_back[anim_pos]);
        }

        // nach unten laufen
        if (directionY == DOWN) {
            // Zwinkern evaluieren
            if (anim_pos == 1) {
                anim_pos = 0;
            } else {
                if (anim_pos == 0) {
                    int zuffi = (int) (Math.random() * 50);
                    if (zuffi > 45) {
                        anim_pos = 1;
                    }
                }
            }

            MaleIhn(offGraph, krabat_front[anim_pos]);
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

        Rede(offGraph);
    }

    // Extraroutine fuers Reden
    private void Rede(GenericDrawingContext g) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);

        // Offsets berechnen
        float fScale = getScale((int) yps);
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
    protected int getLeftPos(int pox, int poy) {
        return calcLeftPosDefault(pox, poy);
    }

    @Override
    protected int getUpPos(int poy) {
        return calcUpPosDefault(poy);
    }

    // fuer Debugging public - wird wieder private !!!
    @Override
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

    // Routine, die TalkPoint zurueckgibt...
    public GenericPoint evalTalkPoint() {
        int up = getUpPos((int) yps);
        return new GenericPoint((int) xps, up - 50);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // Offsets berechnen
        int scalewidth = (int) ((float) scale * scaleFactor);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scalewidth, CHEIGHT - scale);
    }
}