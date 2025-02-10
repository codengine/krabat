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

package de.codengine.anims;

import de.codengine.Start;
import de.codengine.main.Borderrect;
import de.codengine.main.GenericPoint;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Hojnt extends Mainanim {
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

    // Grundlegende Variablen
    private float xps, yps;               // genaue Position der Fuesse fuer Offsetberechnung
    private float txps, typs;             // temporaere Variablen fuer genaue Position
    // public  boolean isWandering = false;  // gilt fuer ganze Route
    // public  boolean isWalking = false;    // gilt bis zum naechsten Rect.
    private int anim_pos = 0;             // Animationsbild
    public boolean clearanimpos = true;  // Bewirkt Standsprite nach Laufen

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
    private static final int CWIDTH = 45;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 80;

    // Abstaende default
    private static final int[] CHORIZ_DIST = {3, 0, 7, 7, 13, 10, 7, 13};
    private static final int CVERT_DIST = 1;

    // Variablen fuer Laufberechnung
    // private static final int CLOHNENX = 28;  // Werte fuer Entscheidung, ob sich
    // private static final int CLOHNENY = 2;  // Laufen ueberhaupt lohnt (halber Schritt)

    // Variablen fuer Animationen
    // public  int nAnimation = 0;           // ID der ggw. Animation
    // public  boolean fAnimHelper = false;  // Hilfsflag bei Animation
    // private int nAnimStep = 0;            // ggw. Pos in Animation

    // Variablen fuer Zooming
    public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)
    private static final int SLOWX = 14;  // Konstante, die angibt, wie sich die x - Abstaende
    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung                                      
    public int defScale;                  // definiert maximale Groesse von Krabat bei x > maxx
    public int minx;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...

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

    public Hojnt(Start caller) {
        super(caller);

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

        InitImages();

        Verhinderhead = MAX_VERHINDERHEAD;
        Verhinderbody = MAX_VERHINDERBODY;
        Bueckcounter = MAX_BUECKCOUNTER;
        Verhinderwalk = MAX_VERHINDERWALK;
    }

    // Bilder vorbereiten
    private void InitImages() {
        krabat_left[0] = getPicture("gfx/hojnt/ho-l.gif");
        krabat_left[1] = getPicture("gfx/hojnt/ho-la.gif");
        krabat_left[2] = getPicture("gfx/hojnt/ho-l0.gif");
        krabat_left[3] = getPicture("gfx/hojnt/ho-l2.gif");
        krabat_left[4] = getPicture("gfx/hojnt/ho-l3.gif");
        krabat_left[5] = getPicture("gfx/hojnt/ho-l5.gif");
        krabat_left[6] = getPicture("gfx/hojnt/ho-l7.gif");
        krabat_left[7] = getPicture("gfx/hojnt/ho-l8.gif");

        krabat_right[0] = getPicture("gfx/hojnt/ho-r.gif");
        krabat_right[1] = getPicture("gfx/hojnt/ho-ra.gif");
        krabat_right[2] = getPicture("gfx/hojnt/ho-r0.gif");
        krabat_right[3] = getPicture("gfx/hojnt/ho-r2.gif");
        krabat_right[4] = getPicture("gfx/hojnt/ho-r3.gif");
        krabat_right[5] = getPicture("gfx/hojnt/ho-r5.gif");
        krabat_right[6] = getPicture("gfx/hojnt/ho-r7.gif");
        krabat_right[7] = getPicture("gfx/hojnt/ho-r8.gif");

        krabat_front[0] = getPicture("gfx/hojnt/ho-u.gif");
        krabat_front[1] = getPicture("gfx/hojnt/ho-u2.gif");
        krabat_front[2] = getPicture("gfx/hojnt/ho-u.gif");
        krabat_front[3] = getPicture("gfx/hojnt/ho-u3.gif");

        krabat_back[0] = getPicture("gfx/hojnt/ho-o.gif");
        krabat_back[1] = getPicture("gfx/hojnt/ho-o2.gif");
        krabat_back[2] = getPicture("gfx/hojnt/ho-o.gif");
        krabat_back[3] = getPicture("gfx/hojnt/ho-o3.gif");

        krabat_left_talk_head[0] = getPicture("gfx/hojnt/ho-lh0.gif");
        krabat_left_talk_head[1] = getPicture("gfx/hojnt/ho-lh1.gif");
        krabat_left_talk_head[2] = getPicture("gfx/hojnt/ho-lh2.gif");
        krabat_left_talk_head[3] = getPicture("gfx/hojnt/ho-lh3.gif");
        krabat_left_talk_head[4] = getPicture("gfx/hojnt/ho-lh4.gif");
        krabat_left_talk_head[5] = getPicture("gfx/hojnt/ho-lh5.gif");

        krabat_left_talk_body[0] = getPicture("gfx/hojnt/ho-lb0.gif");
        krabat_left_talk_body[1] = getPicture("gfx/hojnt/ho-lb1.gif");
        krabat_left_talk_body[2] = getPicture("gfx/hojnt/ho-lb2.gif");

        krabat_right_talk_head[0] = getPicture("gfx/hojnt/ho-rh0.gif");
        krabat_right_talk_head[1] = getPicture("gfx/hojnt/ho-rh1.gif");
        krabat_right_talk_head[2] = getPicture("gfx/hojnt/ho-rh2.gif");
        krabat_right_talk_head[3] = getPicture("gfx/hojnt/ho-rh3.gif");
        krabat_right_talk_head[4] = getPicture("gfx/hojnt/ho-rh4.gif");
        krabat_right_talk_head[5] = getPicture("gfx/hojnt/ho-rh5.gif");

        krabat_right_talk_body[0] = getPicture("gfx/hojnt/ho-rb0.gif");
        krabat_right_talk_body[1] = getPicture("gfx/hojnt/ho-rb1.gif");
        krabat_right_talk_body[2] = getPicture("gfx/hojnt/ho-rb2.gif");

        krabat_down_talk[0] = getPicture("gfx/hojnt/ho-u.gif");
        krabat_down_talk[1] = getPicture("gfx/hojnt/ho-ut1.gif");
        krabat_down_talk[2] = getPicture("gfx/hojnt/ho-ut2.gif");
        krabat_down_talk[3] = getPicture("gfx/hojnt/ho-ut3.gif");
        krabat_down_talk[4] = getPicture("gfx/hojnt/ho-ut4.gif");
        krabat_down_talk[5] = getPicture("gfx/hojnt/ho-ut5.gif");

        krabat_buecken[0] = getPicture("gfx/hojnt/ho-rbuecken.gif");
        krabat_buecken[1] = getPicture("gfx/hojnt/ho-rbuecken2.gif");
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
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        horizontal = Thorizontal;
        walkto = Twalkto;
        direction_x = Tdirection_x;
        direction_y = Tdirection_y;

        // Verzoegerung einbauen beim Laufen
        if (((--Verhinderwalk) > 0) && (horizontal)) {
            return false;
        }

        Verhinderwalk = MAX_VERHINDERWALK;

        if (horizontal)
        // Horizontal laufen
        {
            // neuen Punkt ermitteln und setzen
            VerschiebeX();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == 8) {
                anim_pos = 2;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeX();

            // Ueberschreitung feststellen in X - Richtung
            if (((walkto.x - (int) txps) * direction_x) <= 0) {
                // System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                SetHojntPos(walkto);
                if (clearanimpos) {
                    anim_pos = 0;
                }
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
            if (anim_pos == 4) {
                anim_pos = 0;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if (((walkto.y - (int) typs) * direction_y) <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                SetHojntPos(walkto);
                if (clearanimpos) {
                    anim_pos = 0;
                }
                return true;
            }
        }
        return false;
    }

    // Horizontal - Positions - Verschieberoutine
    private void VerschiebeX() {
        // Skalierungsfaktor holen
        int scale = getScale(((int) xps), ((int) yps));

        // Zooming - Faktor beruecksichtigen in x - Richtung
        float horiz_dist = CHORIZ_DIST[anim_pos] - (scale / SLOWX);
        if (horiz_dist < 1) {
            horiz_dist = 1;
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = 0;
        if (horiz_dist != 0) {
            z = Math.abs(xps - walkto.x) / horiz_dist;
        }

        typs = yps;
        if (z != 0) {
            typs += direction_y * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + (direction_x * horiz_dist);
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        // Skalierungsfaktor holen
        int scale = getScale(((int) xps), ((int) yps));

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vert_dist = CVERT_DIST - (scale / SLOWY);
        if (vert_dist < 1) {
            vert_dist = 1;
            // hier kann noch eine Entscheidungsroutine hin, die je nach Animationsphase
            // und vert_distance ein Pixel erlaubt oder nicht
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vert_dist;

        txps = xps;
        if (z != 0) {
            txps += direction_x * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + (direction_y * vert_dist);
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        int xricht, yricht;
        boolean horiz;

        // Lohnt es sich zu laufen ?
	/*int scale = getScale ((int) xps, (int) yps);
	  int lohnenx = CLOHNENX - (scale / 2);
	  int lohneny = CLOHNENY - (scale / 4);
	  if (lohnenx < 1) lohnenx = 1;
	  if (lohneny < 1) lohneny = 1;
	  if ((Math.abs (aim.x - ((int) xps)) < lohnenx) && 
	  (Math.abs (aim.y - ((int) yps)) < lohneny))
	  {
	  System.out.println("Lohnt sich nicht !");
	  anim_pos = 0;
	  return;
	  }*/

        // Laufrichtung ermitteln
        if (aim.x > ((int) xps)) {
            xricht = 1;
        } else {
            xricht = -1;
        }
        if (aim.y > ((int) yps)) {
            yricht = 1;
        } else {
            yricht = -1;
        }

        // Horizontal oder verikal laufen ?
        if (aim.x == ((int) xps)) {
            horiz = false;
        } else {
            // Winkel berechnen, den Krabat laufen soll
            double yangle = Math.abs(aim.y - ((int) yps));
            double xangle = Math.abs(aim.x - ((int) xps));
            double angle = Math.atan(yangle / xangle);
            // System.out.println ((angle * 180 / Math.PI) + " Grad");
            horiz = !(angle > (30 * Math.PI / 180));
        }

        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = horiz;
        Tdirection_x = xricht;
        Tdirection_y = yricht;

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        } else {
            // bei fortgehendem Bewegen weiterschalten
            anim_pos++;
            if ((horiz) && (anim_pos == 8)) {
                anim_pos = 2;
            }
            if ((!horiz) && (anim_pos == 4)) {
                anim_pos = 0;
            }
        }
        // System.out.println("Animpos ist . " + anim_pos);
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetHojntPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    public GenericPoint GetHojntPos() {
        //System.out.println(" Aktuelle Pos : "+pos_x+" "+pos_y);
        return (new GenericPoint(((int) xps), ((int) yps)));
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawHojnt(GenericDrawingContext offGraph) {
        // je nach Richtung Sprite auswaehlen und zeichnen
        if (horizontal) {
            // nach links laufen
            if (direction_x == -1) {
                MaleIhn(offGraph, krabat_left[anim_pos]);
            }

            // nach rechts laufen
            if (direction_x == 1) {
                MaleIhn(offGraph, krabat_right[anim_pos]);
            }
        } else {
            // Bei normaler Darstellung
            if (!upsidedown) {
                // nach oben laufen
                if (direction_y == -1) {
                    MaleIhn(offGraph, krabat_back[anim_pos]);
                }

                // nach unten laufen
                if (direction_y == 1) {
                    MaleIhn(offGraph, krabat_front[anim_pos]);
                }
            } else {
                // nach oben laufen
                if (direction_y == -1) {
                    MaleIhn(offGraph, krabat_front[anim_pos]);
                }

                // nach unten laufen
                if (direction_y == 1) {
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

    // Richtung, in die Krabat schaut, ermitteln (wieder nach Uhrzeit)
    // nur private, da nur in dieser Klasse benoetigt
    private int GetFacing() {
        int rgabe = 0;
        if (horizontal) {
            if (direction_x == 1) {
                rgabe = 3;
            } else {
                rgabe = 9;
            }
        } else {
            if (direction_y == 1) {
                rgabe = 6;
            } else {
                rgabe = 12;
            }
        }
        if (rgabe == 0) {
            System.out.println("Fehler bei GetFacing !!!");
        }
        return rgabe;
    }


    // Zeichne Hojnt beim Sprechen mit anderen Personen
    public void talkHojnt(GenericDrawingContext offGraph) {
        // Kopf veraendern
        if ((--Verhinderhead) < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            talkHead = (int) (Math.random() * 5.9);
        }

        // Body switchen
        if ((--Verhinderbody) < 1) {
            Verhinderbody = MAX_VERHINDERBODY;
            talkBody = (int) (Math.random() * 2.9);
        }

        // reden nach unten ist nur 1 Image
        if (GetFacing() == 6) {
            MaleIhn(offGraph, krabat_down_talk[talkHead]);
        } else {
            // reden nach rechts
            if (GetFacing() == 3) {
                MaleIhn(offGraph, krabat_right_talk_head[talkHead], krabat_right_talk_body[talkBody]);
            }

            // reden nach links
            if (GetFacing() == 9) {
                MaleIhn(offGraph, krabat_left_talk_head[talkHead], krabat_left_talk_body[talkBody]);
            }
        }
    }

    // Hojnt beim Monolog (ohne Gestikulieren)
    public void describeHojnt(GenericDrawingContext offGraph) {
        // Kopf veraendern
        if ((--Verhinderhead) < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            talkHead = (int) (Math.random() * 5.9);
        }

        // reden nach unten ist nur 1 Image, keine Wahl beim mit/ohne Haendefuchteln
        if (GetFacing() == 6) {
            MaleIhn(offGraph, krabat_down_talk[talkHead]);
        } else {
            // reden nach rechts
            if (GetFacing() == 3) {
                MaleIhn(offGraph, krabat_right_talk_head[talkHead], krabat_right_talk_body[0]);
            }

            // reden nach links
            if (GetFacing() == 9) {
                MaleIhn(offGraph, krabat_left_talk_head[talkHead], krabat_left_talk_body[0]);
            }
        }
    }

    // Zooming-Variablen berechnen

    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int helper = getScale(pox, poy);
        return (pox - ((CWIDTH - (helper / 2)) / 2));
    }

    private int getUpPos(int pox, int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        int helper = getScale(pox, poy);
        return (poy - CHEIGHT + helper);
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
            return ((int) helper);
        } else {
            // Berechnung bei "upsidedown" - Berg/Tallauf
            float help2 = (poy - minx) / zoomf;
            if (help2 < 0) {
                help2 = 0;
            }
            help2 += defScale;
            // System.out.println (minx + " + " + poy + " und " + zoomf + " ergeben " + help2);
            return ((int) help2);
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
    public Borderrect HojntRect() {
        int x = getLeftPos(((int) xps), ((int) yps));
        int y = getUpPos(((int) xps), ((int) yps));
        int xd = (2 * (((int) xps) - x)) + x;
        int yd = ((int) yps);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return (new Borderrect(x, y, xd, yd));
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClip(g, ((int) xps), ((int) yps));

        // Groesse und Position der Figur berechnen
        int left = getLeftPos(((int) xps), ((int) yps));
        int up = getUpPos(((int) xps), ((int) yps));
        int scale = getScale(((int) xps), ((int) yps));

        // Hier beim Scaling ein echtes Verhaeltnis Hoehe/Breite einsetzen
        float fBreite = CWIDTH;
        float fHoehe = CHEIGHT;
        float Verhaeltnis = fBreite / fHoehe;

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - ((int) (scale * Verhaeltnis)), CHEIGHT - scale);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage khead, GenericImage kbody) {
        // Clipping - Region setzen
        KrabatClip(g, ((int) xps), ((int) yps));

        // Groesse und Position der Figur berechnen
        int left = getLeftPos(((int) xps), ((int) yps));
        int up = getUpPos(((int) xps), ((int) yps));
        int scale = getScale(((int) xps), ((int) yps));

        // Scalings und Offset fuer gesplittetes Kopf/Koerperzeichnen
        float fScale = (CHEIGHT - scale);
        float teilScaleHead = KOPFHOEHE / KOERPERHOEHE;
        int Kopfhoehe = (int) (fScale * teilScaleHead);
        int Koerperhoehe = CHEIGHT - scale - Kopfhoehe;

        // System.out.println ("Kopfhoehe : " + Kopfhoehe + " Bodyhoehe : " + Koerperhoehe);

        // Hier beim Scaling ein echtes Verhaeltnis Hoehe/Breite einsetzen
        float fBreite = CWIDTH;
        float fHoehe = CHEIGHT;
        float Verhaeltnis = fBreite / fHoehe;

        // Figur zeichnen
        g.drawImage(khead, left, up, CWIDTH - ((int) (scale * Verhaeltnis)), Kopfhoehe);
        g.drawImage(kbody, left, up + Kopfhoehe, CWIDTH - ((int) (scale * Verhaeltnis)), Koerperhoehe);
    }

    public boolean bueckeHojnt(GenericDrawingContext g) {
        // Clipping - Region setzen
        // Links - oben - Korrdinaten ermitteln
        int left = getLeftPos(((int) xps), ((int) yps));
        int up = getUpPos(((int) xps), ((int) yps));
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd = (2 * (((int) xps) - left)) + 30;
        int yd = ((int) yps) - up;
        g.setClip(left, up, xd, yd);


        // Groesse und Position der Figur berechnen
        int scale = getScale(((int) xps), ((int) yps));

        // Hier beim Scaling ein echtes Verhaeltnis Hoehe/Breite einsetzen
        float fBreite = 63.0f;
        float fHoehe = 80.0f;
        float Verhaeltnis = fBreite / fHoehe;

        // Figur zeichnen
        g.drawImage(krabat_buecken[((Bueckcounter > 8) || (Bueckcounter < 4)) ? 0 : 1], left, up, ((int) fBreite) - ((int) (scale * Verhaeltnis)), ((int) fHoehe) - scale);

        --Bueckcounter;
        if (Bueckcounter < 1) {
            Bueckcounter = MAX_BUECKCOUNTER;
            return (false);
        }

        return (true);
    }

}