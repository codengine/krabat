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

public class Druzina extends Mainanim {
    // Alle GenericImage - Objekte
    private final GenericImage[] druzina_walk;

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

    // Variablen fuer Bewegung und Richtung
    private GenericPoint walkto = new GenericPoint(0, 0);                 // Zielpunkt fuer Move()
    private GenericPoint Twalkto = new GenericPoint(0, 0);                // Zielpunkt, der in MoveTo() gesetzt und von Move uebernommen wird
    // hier ist das Problem der Threadsynchronisierung !!!!!!!
    private int direction_x = 1;          // Laufrichtung x
    private int Tdirection_x = 1;

    private int direction_y = 1;          // Laufrichtung y
    private int Tdirection_y = 1;

    private boolean Thorizontal = true;

    public final boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden

    // Spritevariablen
    private static final int CWIDTH = 65;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 154;

    private final float scaleFactorNormal = (float) CWIDTH / CHEIGHT;

    // Abstaende default
    private static final int[] CVERT_DIST = {3, 3, 1, 3, 1, 3};

    // Variablen fuer Zooming
    public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)
    private static final int SLOWY = 10;  // dsgl. fuer y - Richtung aller wieviel Pix. die Schritte kleiner werden

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Druzina(Start caller) {
        super(caller);

        druzina_walk = new GenericImage[6];

        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        druzina_walk[0] = getPicture("gfx-dd/zelen/gefolge1.gif");
        druzina_walk[1] = getPicture("gfx-dd/zelen/gefolge1a.gif");
        druzina_walk[2] = getPicture("gfx-dd/zelen/gefolge1-l2.gif");
        druzina_walk[3] = getPicture("gfx-dd/zelen/gefolge1-l4.gif");
        druzina_walk[4] = getPicture("gfx-dd/zelen/gefolge1-l6.gif");
        druzina_walk[5] = getPicture("gfx-dd/zelen/gefolge1-l8.gif");
    }

    @Override
    public void cleanup() {
        druzina_walk[0] = null;
        druzina_walk[1] = null;
        druzina_walk[2] = null;
        druzina_walk[3] = null;
        druzina_walk[4] = null;
        druzina_walk[5] = null;
    }


    // Laufen mit Druzina ////////////////////////////////////////////////////////////////

    // Druzina um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        // Animationen in x oder y Richtung
        boolean horizontal = Thorizontal;
        walkto = Twalkto;
        direction_x = Tdirection_x;
        direction_y = Tdirection_y;

        if (!horizontal)
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Animationsphase weiterschalten
            anim_pos++;
            if (anim_pos == 6) {
                anim_pos = 2;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * direction_y <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                SetDruzinaPos(walkto);
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

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vert_dist = CVERT_DIST[anim_pos] - (float) scale / SLOWY;
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

        // Laufrichtung ermitteln
        final int xricht = aim.x > (int) xps ? 1 : -1;
        final int yricht = aim.y > (int) yps ? 1 : -1;

        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = false;
        Tdirection_x = xricht;
        Tdirection_y = yricht;

        if (anim_pos < 2) {
            anim_pos = 2;       // Animationsimage bei Neubeginn initialis.
        }
        // System.out.println("Animpos ist . " + anim_pos);
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetDruzinaPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawDruzina(GenericDrawingContext offGraph) {
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

        if (anim_pos < 2) {
            anim_pos = Zwinker;
        }

        // hier wird er nur benoetigt fuer laufen oder rumstehen + zwinkern (also in MaleIhn)
        MaleIhn(offGraph);
    }

    // Zooming-Variablen berechnen
    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz

        float fHelper = getScale(poy) * scaleFactorNormal;

        return pox - (CWIDTH - (int) fHelper) / 2;
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
            return (int) helper;
        } else {
            // Berechnung bei "upsidedown" - Berg/Tallauf
            float help2 = poy / zoomf;
            if (help2 < 0) {
                help2 = 0;
            }
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
    public Borderrect DruzinaRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = (int) yps;
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return new Borderrect(x, y, xd, yd);
    }

    private void MaleIhn(GenericDrawingContext g) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen

        float fScaleY = (float) scale * scaleFactorNormal;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Koerperhoehe = (int) ((float) CHEIGHT - scale);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // Figur zeichnen
        g.drawImage(druzina_walk[anim_pos], left, up, Koerperbreite, Koerperhoehe);
    }
}