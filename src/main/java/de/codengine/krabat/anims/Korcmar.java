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
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Korcmar extends Mainanim {
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;
    private final GenericImage[] krabat_talk_head;
    private final GenericImage[] krabat_talk_body;

    // Grundlegende Variablen
    private float xps, yps;               // genaue Position der Fuesse fuer Offsetberechnung
    private float txps, typs;             // temporaere Variablen fuer genaue Position
    // public  boolean isWandering = false;  // gilt fuer ganze Route
    // public  boolean isWalking = false;    // gilt bis zum naechsten Rect.
    private int anim_pos = 0;             // Animationsbild
    // public  boolean clearanimpos = true;  // Bewirkt Standsprite nach Laufen 

    // Variablen fuer Bewegung und Richtung
    private GenericPoint walkto = new GenericPoint(0, 0);                 // Zielpunkt fuer Move()
    private GenericPoint Twalkto = new GenericPoint(0, 0);                // Zielpunkt, der in MoveTo() gesetzt und von Move uebernommen wird
    // hier ist das Problem der Threadsynchronisierung !!!!!!!
    private int direction_x = 1;          // Laufrichtung x
    private int Tdirection_x = 1;

    private int direction_y = 1;          // Laufrichtung y
    private int Tdirection_y = 1;

    private boolean horizontal = true;    // Animationen in x oder y Richtung
    private boolean Thorizontal = true;

    public boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden

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

    // Variablen fuer Zooming
    public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)
    // private static final int SLOWX = 14;  // Konstante, die angibt, wie sich die x - Abstaende
    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung                                      
    public int defScale;                  // definiert maximale Groesse von Krabat bei x > maxx
    public int minx;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...

    // Redevariablen
    private int Kopf = 0;
    private int Body = 0;

    private int Verhinderkopf;
    private int Verhinderbody;
    private static final int MAX_VERHINDERKOPF = 2;
    private static final int MAX_VERHINDERBODY = 8;

    // private static final int BODYOFFSET = 34;
    private static final int HEADHEIGHT = 33;

    private final float scalefaktor;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Korcmar(Start caller) {
        super(caller);

        krabat_front = new GenericImage[6];
        krabat_back = new GenericImage[5];

        krabat_talk_head = new GenericImage[7];
        krabat_talk_body = new GenericImage[4];

        InitImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;

        float fWidth = CWIDTH;
        float fHeight = CHEIGHT;
        scalefaktor = fWidth / fHeight;
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
        direction_x = Tdirection_x;
        direction_y = Tdirection_y;

        if (horizontal)
        // Horizontal laufen
        {
        } else
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == (direction_y == 1 ? 6 : 5)) {
                anim_pos = direction_y == 1 ? 2 : 1;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * direction_y <= 0) {
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
        int scale = getScale((int) xps, (int) yps);

        float vert_dist = 1;
        // Zooming - Faktor beruecksichtigen in y-Richtung
        if (direction_y == 1) {
            vert_dist = CVERT_UNTEN[anim_pos] - scale / SLOWY;
            if (vert_dist < 1) {
                vert_dist = 1;
            }
        } else {
            vert_dist = CVERT_OBEN[anim_pos] - scale / SLOWY;
            if (vert_dist < 1) {
                vert_dist = 1;
            }
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vert_dist;

        txps = xps;
        if (z != 0) {
            txps += direction_x * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + direction_y * vert_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        int xricht, yricht;
        boolean horiz;

        // Laufrichtung ermitteln
        if (aim.x > (int) xps) {
            xricht = 1;
        } else {
            xricht = -1;
        }
        if (aim.y > (int) yps) {
            yricht = 1;
        } else {
            yricht = -1;
        }

        // Horizontal oder verikal laufen ?
        horiz = false;

        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = horiz;
        Tdirection_x = xricht;
        Tdirection_y = yricht;

        if (anim_pos == 0) {
            anim_pos = yricht == 1 ? 2 : 1;       // Animationsimage bei Neubeginn initialis.
        }
        // System.out.println("Animpos ist . " + anim_pos);
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
        if (direction_y == -1) {
            MaleIhn(offGraph, krabat_back[anim_pos]);
        }

        // nach unten laufen
        if (direction_y == 1) {
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
                direction_x = 1;
                break;
            case 6:
                horizontal = false;
                direction_y = 1;
                break;
            case 9:
                horizontal = true;
                direction_x = -1;
                break;
            case 12:
                horizontal = false;
                direction_y = -1;
                break;
            default:
                System.out.println("Falsche Uhrzeit zum Witzereissen!");
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
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        // Offsets berechnen
        float fScale = scale;
        int scalewidth = (int) (fScale * scalefaktor);

        float fHeight = CHEIGHT;
        float fBodyoffset = HEADHEIGHT;
        int Kopfhoehe = (int) ((fHeight - fScale) * (fBodyoffset / fHeight));
        int Koerperhoehe = CHEIGHT - Kopfhoehe;

        // Figur zeichnen
        g.drawImage(krabat_talk_head[Kopf], left, up, CWIDTH - scalewidth, Kopfhoehe);
        g.drawImage(krabat_talk_body[Body], left, up + Kopfhoehe, CWIDTH - scalewidth, Koerperhoehe);
    }

    // Zooming-Variablen berechnen
    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int helper = getScale(pox, poy);
        return pox - (CWIDTH - helper / 2) / 2;
    }

    private int getUpPos(int pox, int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        int helper = getScale(pox, poy);
        return poy - CHEIGHT + helper;
    }

    // fuer Debugging public - wird wieder private !!!
    public int getScale(int pox, int poy) {

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
            float help2 = (poy - minx) / zoomf;
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
        int y = getUpPos(xx, yy);
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

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    public Borderrect KorcmarRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) xps, (int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = (int) yps;
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return new Borderrect(x, y, xd, yd);
    }

    // Routine, die TalkPoint zurueckgibt...
    public GenericPoint evalTalkPoint() {
        int up = getUpPos((int) xps, (int) yps);
        return new GenericPoint((int) xps, up - 50);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        // Offsets berechnen
        float fScale = scale;
        int scalewidth = (int) (fScale * scalefaktor);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scalewidth, CHEIGHT - scale);
    }
}