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

public class Plokarka extends Mainanim {
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

    // Grundlegende Variablen
    private float xps;
    private float yps;               // genaue Position der Fuesse fuer Offsetberechnung
    private float txps;
    private float typs;             // temporaere Variablen fuer genaue Position
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

    public final boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden

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

    // Variablen fuer Zooming
    public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)
    private static final int SLOWX = 14;  // Konstante, die angibt, wie sich die x - Abstaende
    // beim Zoomen veraendern
    private static final int SLOWY = 22;  // dsgl. fuer y - Richtung                                      
    public int defScale;                  // definiert maximale Groesse von Krabat bei x > maxx

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
        super(caller);

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
        direction_x = Tdirection_x;
        direction_y = Tdirection_y;

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
            if ((walkto.x - (int) txps) * direction_x <= 0) {
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
            if (anim_pos == (direction_y == 1 ? 3 : 4)) {
                anim_pos = 0;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * direction_y <= 0) {
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
            typs += direction_y * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + direction_x * horiz_dist;
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
        if (aim.x == (int) xps) {
            horiz = false;
        } else {
            // Winkel berechnen, den Krabat laufen soll
            double yangle = Math.abs(aim.y - (int) yps);
            double xangle = Math.abs(aim.x - (int) xps);
            double angle = Math.atan(yangle / xangle);
            // System.out.println ((angle * 180 / Math.PI) + " Grad");
            horiz = !(angle > 22 * Math.PI / 180);
        }

        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = horiz;
        Tdirection_x = xricht;
        Tdirection_y = yricht;

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        }
        // System.out.println("Animpos ist . " + anim_pos);
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
            if (direction_x == -1) {
                MaleIhn(offGraph, hasWaesche ? krabatw_left[anim_pos] : krabat_left[anim_pos]);
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
                    MaleIhn(offGraph, hasWaesche ? krabatw_front[anim_pos] : krabat_front[anim_pos]);
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
    /*public int GetFacing()
      {
      int rgabe = 0;
      if (horizontal == true)
      {
      if (direction_x == 1) rgabe = 3;
      else rgabe = 9;
      }
      else
      {
      if (direction_y == 1) rgabe = 6;
      else rgabe = 12;
      }
      if (rgabe == 0)
      {
      System.out.println("Fehler bei GetFacing !!!");
      }
      return rgabe;
      }*/


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

    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int helper = getScale(poy);
        return pox - (CWIDTH - helper / 2) / 2;
    }

    private int getUpPos(int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        int helper = getScale(poy);
        return poy - CHEIGHT + helper;
    }

    // fuer Debugging public - wird wieder private !!!
    public int getScale(int poy) {

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

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    public Borderrect PlokarkaRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = (int) yps;
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return new Borderrect(x, y, xd, yd);
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