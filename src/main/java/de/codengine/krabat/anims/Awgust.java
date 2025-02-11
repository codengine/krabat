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

public class Awgust extends Mainanim {
    // Alle GenericImage - Objekte
    private final GenericImage[] kral_head;
    private final GenericImage[] kral_body;
    private final GenericImage[] kral_walk;

    // Grundlegende Variablen
    private float xps;
    private float yps;               // genaue Position der Fuesse fuer Offsetberechnung
    private float txps;
    private float typs;             // temporaere Variablen fuer genaue Position
    // public  boolean isWandering = false;  // gilt fuer ganze Route
    // public  boolean isWalking = false;    // gilt bis zum naechsten Rect.
    private int anim_pos = 0;             // Animationsbild
    // public  boolean clearanimpos = true;  // Bewirkt Standsprite nach Laufen

    private int Zwinker = 0;
    private static final int BODYOFFSET = 39;

    private int Verhinderkopf;
    private static final int MAX_VERHINDERKOPF = 2;

    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;

    private int Head = 0;

    // Variablen fuer Bewegung und Richtung
    private GenericPoint walkto = new GenericPoint(0, 0);                 // Zielpunkt fuer Move()
    private GenericPoint Twalkto = new GenericPoint(0, 0);                // Zielpunkt, der in MoveTo() gesetzt und von Move uebernommen wird
    // hier ist das Problem der Threadsynchronisierung !!!!!!!
    private int direction_x = 1;          // Laufrichtung x
    private int Tdirection_x = 1;

    private int direction_y = 1;          // Laufrichtung y
    private int Tdirection_y = 1;

    private boolean Thorizontal = true; // Animationen in x oder y Richtung

    public final boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden

    // Spritevariablen
    private static final int CWIDTH = 81;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 155;

    private final float scaleFactor = (float) CWIDTH / CHEIGHT;

    // Abstaende default
    private static final int[] CVERT_DIST = {6, 2, 6, 2, 6};

    // Variablen fuer Zooming
    public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)
    private static final int SLOWY = 10;  // dsgl. fuer y - Richtung aller wieviel Pix. die Schritte kleiner werden
    public int defScale;                  // definiert maximale Groesse von Krabat bei x > maxx
    public int minx;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Awgust(Start caller) {
        super(caller);

        kral_walk = new GenericImage[4];
        kral_head = new GenericImage[8];
        kral_body = new GenericImage[2];

        InitImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    // Bilder vorbereiten
    private void InitImages() {
        kral_walk[0] = getPicture("gfx-dd/zelen/kral-u-l2.gif");
        kral_walk[1] = getPicture("gfx-dd/zelen/kral-u-l4.gif");
        kral_walk[2] = getPicture("gfx-dd/zelen/kral-u-l6.gif");
        kral_walk[3] = getPicture("gfx-dd/zelen/kral-u-l8.gif");

        kral_head[0] = getPicture("gfx-dd/zelen/kral-uh1.gif");
        kral_head[1] = getPicture("gfx-dd/zelen/kral-uh1a.gif");
        kral_head[2] = getPicture("gfx-dd/zelen/kral-uh2.gif");
        kral_head[3] = getPicture("gfx-dd/zelen/kral-uh3.gif");
        kral_head[4] = getPicture("gfx-dd/zelen/kral-uh4.gif");
        kral_head[5] = getPicture("gfx-dd/zelen/kral-uh5.gif");
        kral_head[6] = getPicture("gfx-dd/zelen/kral-uh6.gif");
        kral_head[7] = getPicture("gfx-dd/zelen/kral-uh7.gif");

        kral_body[0] = getPicture("gfx-dd/zelen/kral-ub1.gif");
        kral_body[1] = getPicture("gfx-dd/zelen/kral-ub2.gif");
    }

    @Override
    public void cleanup() {
        kral_walk[0] = null;
        kral_walk[1] = null;
        kral_walk[2] = null;
        kral_walk[3] = null;

        kral_head[0] = null;
        kral_head[1] = null;
        kral_head[2] = null;
        kral_head[3] = null;
        kral_head[4] = null;
        kral_head[5] = null;
        kral_head[6] = null;
        kral_head[7] = null;

        kral_body[0] = null;
        kral_body[1] = null;
    }


    // Laufen mit Awgust ////////////////////////////////////////////////////////////////

    // Awgust um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        boolean horizontal = Thorizontal;
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
            if (anim_pos == 4) {
                anim_pos = 1;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * direction_y <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                SetAwgustPos(walkto);
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

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vert_dist = CVERT_DIST[anim_pos] - scale / SLOWY;
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

        horiz = false;

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
    public void SetAwgustPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    public GenericPoint GetAwgustPos() {
        //System.out.println(" Aktuelle Pos : "+pos_x+" "+pos_y);
        return new GenericPoint((int) xps, (int) yps);
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawAwgust(GenericDrawingContext offGraph) {
        // je nach Richtung Sprite auswaehlen und zeichnen (hier aber nur Stehen , ausser back = laufen)

        // Zufallscounter fuer Zwinkern
        int zuffZahl = (int) Math.round(Math.random() * 50);

        if (Zwinker == 1) {
            Zwinker = 0;
        } else {
            if (zuffZahl > 45) {
                Zwinker = 1;
            }
        }

        // hier wird er nur benoetigt fuer laufen oder rumstehen + zwinkern (also in MaleIhn)
        MaleIhn(offGraph, false, false); // Grafik, erhobeneHand, isTalking
    }

    // Zeichne Hojnt beim Sprechen mit anderen Personen
    public void talkAwgust(GenericDrawingContext offGraph, boolean erhobeneHand) {
        if (--Verhinderkopf < 1) {
            Verhinderkopf = MAX_VERHINDERKOPF;

            Head = (int) (Math.random() * 7.9);
            if (Head == 1) {
                Head = 0; // zwinkern rauslassen
            }
        }

        if (--Verhinderbody < 1) {
            Verhinderbody = MAX_VERHINDERBODY;
            // Body wird leider nicht geswitcht (ooohhh..)
        }

        MaleIhn(offGraph, erhobeneHand, true); // Grafik, erhobeneHand, isTalking
    }

    public GenericPoint evalAwgustTalkPoint() {
        // Hier Position des Textes berechnen
        Borderrect temp = AwgustRect();
        return new GenericPoint((temp.ru_point.x + temp.lo_point.x) / 2, temp.lo_point.y - 50);
    }

    // Zooming-Variablen berechnen
    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz

        float fScaleY = getScale(pox, poy) * scaleFactor;

        return pox - (CWIDTH - (int) fScaleY) / 2;
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
    public Borderrect AwgustRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) xps, (int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = (int) yps;
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return new Borderrect(x, y, xd, yd);
    }

    private void MaleIhn(GenericDrawingContext g, boolean hatHandErhoben, boolean redet) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // Figur zeichnen
        if (redet) {
            // beim Reden und ggf. mit erhobener Hand
            // System.out.println ("Head : " + Head + " Hand erhoben : " + hatHandErhoben);
            g.drawImage(kral_head[Head], left, up, Koerperbreite, Kopfhoehe, null);
            g.drawImage(kral_body[hatHandErhoben ? 1 : 0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
        } else {
            // beim Stehen oder Laufen
            if (anim_pos == 0) {
                // er steht, also getrennt Kopf und Body
                g.drawImage(kral_head[Zwinker], left, up, Koerperbreite, Kopfhoehe, null);
                g.drawImage(kral_body[0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
            } else {
                // er laeuft, das sind hier gemeinsame Images
                g.drawImage(kral_walk[anim_pos], left, up, Koerperbreite, Kopfhoehe + Koerperhoehe, null);
            }
        }
    }
}