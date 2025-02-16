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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public class WikowarkaRudy extends Mainanim {
    private static final Logger log = LoggerFactory.getLogger(WikowarkaRudy.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;
    private final GenericImage[] krabat_talk;
    private final GenericImage[] krabat_extra;
    private final GenericImage[] hrajer_extra;

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
    private DirectionX directionX = RIGHT;          // Laufrichtung x
    private DirectionX tDirectionX = RIGHT;

    private DirectionY directionY = DirectionY.DOWN;          // Laufrichtung y
    private DirectionY tDirectionY = DirectionY.DOWN;

    // private boolean horizontal = true;    // Animationen in x oder y Richtung
    // private boolean Thorizontal = true;

    // public  boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden

    // Spritevariablen
    private static final int CWIDTH = 33;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 35;

    // Abstaende default
    // private static final int[] CHORIZ_DIST = {6, 10, 11, 9, 10, 10, 10, 11, 11, 10};
    private static final int CVERT_DIST = 4;

    private int VerhinderTalk;
    private static final int MAX_VERHINDERTALK = 5;
    private int TalkPos = 0;

    // private boolean isListening = false;
    private boolean isMoving = false;

    private int Guck = 0;
    private int Verhinderguck;
    private static final int MAX_VERHINDERGUCK = 70;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public WikowarkaRudy(Start caller) {
        super(caller);

        krabat_front = new GenericImage[4];
        krabat_back = new GenericImage[4];
        krabat_talk = new GenericImage[8];
        krabat_extra = new GenericImage[2];
        hrajer_extra = new GenericImage[3];

        InitImages();

        VerhinderTalk = MAX_VERHINDERTALK;
        Verhinderguck = MAX_VERHINDERGUCK;
    }

    // Bilder vorbereiten
    private void InitImages() {
        krabat_front[0] = getPicture("gfx-dd/lodz/zona.gif");
        krabat_front[1] = getPicture("gfx-dd/lodz/zona5.gif");
        krabat_front[2] = getPicture("gfx-dd/lodz/zona.gif");
        krabat_front[3] = getPicture("gfx-dd/lodz/zona6.gif");

        krabat_back[0] = getPicture("gfx-dd/lodz/zona4.gif");
        krabat_back[1] = getPicture("gfx-dd/lodz/zona2.gif");
        krabat_back[2] = getPicture("gfx-dd/lodz/zona4.gif");
        krabat_back[3] = getPicture("gfx-dd/lodz/zona3.gif");

        krabat_talk[0] = getPicture("gfx-dd/lodz/zona.gif");
        krabat_talk[1] = getPicture("gfx-dd/lodz/zona9.gif");
        krabat_talk[2] = getPicture("gfx-dd/lodz/zona10.gif");
        krabat_talk[3] = getPicture("gfx-dd/lodz/zona11.gif");
        krabat_talk[4] = getPicture("gfx-dd/lodz/zona12.gif");
        krabat_talk[5] = getPicture("gfx-dd/lodz/zona13.gif");
        krabat_talk[6] = getPicture("gfx-dd/lodz/zona14.gif");
        krabat_talk[7] = getPicture("gfx-dd/lodz/zona15.gif");

        krabat_extra[0] = getPicture("gfx-dd/lodz/zona7.gif");
        krabat_extra[1] = getPicture("gfx-dd/lodz/zona8.gif");

        hrajer_extra[0] = getPicture("gfx-dd/lodz/kzona.gif");
        hrajer_extra[1] = getPicture("gfx-dd/lodz/kzona2.gif");
        hrajer_extra[2] = getPicture("gfx-dd/lodz/kzona3.gif");
    }


    // Laufen mit Plokarka ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean Move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        // horizontal = Thorizontal;
        walkto = Twalkto;
        directionX = tDirectionX;
        directionY = tDirectionY;

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
        if ((walkto.y - (int) typs) * directionY.getVal() <= 0) {
            // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
            SetZonaPos(walkto);
            anim_pos = 0;
            isMoving = true;
            return true;
        }
        /*  }  */
        isMoving = false;
        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        // Skalierungsfaktor holen
        // int scale = getScale(((int) xps), ((int) yps));

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vert_dist = CVERT_DIST;

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
        // Variablen an Move uebergeben
        Twalkto = aim;

        // Laufrichtung ermitteln
        tDirectionX = aim.x > (int) xps ? RIGHT : LEFT;
        tDirectionY = aim.y > (int) yps ? DOWN : UP;

        if (anim_pos == 0) {
            anim_pos = 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void SetZonaPos(GenericPoint aim) {
        // point bezeichnet Fusskoordinaten
        // pos_x = aim.x;
        // pos_y = aim.y;

        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;

        //System.out.println("Setkrabatpos allgemein "+pos_x+" "+pos_y);
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawZona(GenericDrawingContext offGraph, boolean isListening) {
        // hier evaluieren, ob geguckt werden darf oder nicht
        if (!isMoving && !isListening) {
            // Weiterschalten
            if (--Verhinderguck < 1) {
                Verhinderguck = MAX_VERHINDERGUCK;
                Guck = (int) (Math.random() * 1.9);
            }

            // Wenn Extrawurst, dann diese ausfuehren
            if (Guck == 1) {
                MaleIhn(offGraph, krabat_talk[1]);
                return;
            }
        }

        // nach oben laufen
        if (directionY == DOWN) {
            MaleIhn(offGraph, krabat_front[anim_pos]);
        }

        // nach unten laufen
        if (directionY == UP) {
            MaleIhn(offGraph, krabat_back[anim_pos]);
        }
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void SetFacing(int direction) {
        switch (direction) {
            case 6:
                // horizontal=false;
                directionY = DOWN;
                break;
            case 12:
                // horizontal=false;
                directionY = UP;
                break;
            default:
                log.debug("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Zeichne Hojnt beim Sprechen mit anderen Personen
    public void talkZona(GenericDrawingContext offGraph) {
        // isListening wird nur bei "draw" ausgewertet
        // d.h. aktiviert -> Gucken gesperrt
        // deaktiviert -> Guckt nach Ende Talk wieder

        // this.isListening = isListening;

        // schraeggucken deaktivieren
        Guck = 0;

        if (--VerhinderTalk < 1) {
            VerhinderTalk = MAX_VERHINDERTALK;
            TalkPos = (int) Math.round(Math.random() * 8);

            if (TalkPos == 8) {
                TalkPos = 7;
            }
        }

        MaleIhn(offGraph, krabat_talk[TalkPos]);
    }

    public void giveWosusk(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und die 2 Images fuer Give zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(krabat_extra[0], lo.x, lo.y);
        g.drawImage(hrajer_extra[0], lo.x, lo.y + CHEIGHT);
    }

    public void giveMetal(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und Images fuer Give zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(krabat_extra[1], lo.x, lo.y);
        g.drawImage(hrajer_extra[1], lo.x, lo.y + CHEIGHT);
    }

    public void Kiss(GenericDrawingContext g, GenericPoint lo) {
        // alles "overriden" und 1 Kiss-Image zeichnen
        // Achtung ! Nachher per Clipset Neuzeichnen erzwingen !!
        g.setClip(lo.x, lo.y, lo.x + CWIDTH * 2, lo.y + CHEIGHT * 2);
        g.drawImage(hrajer_extra[2], lo.x, lo.y);
    }

    // Zooming-Variablen berechnen

    private int getLeftPos(int pox) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        // int helper = getScale(pox, poy);
        return pox - CWIDTH / 2;
    }

    private int getUpPos(int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        // int helper = getScale(pox, poy);
        return poy - CHEIGHT / 2;
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    private void KrabatClip(GenericDrawingContext g, int xx, int yy) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos(xx);
        int y = getUpPos(yy);
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd = 2 * (xx - x);
        int yd = 2 * (yy - y);
        g.setClip(x, y, xd, yd);

        // Fuer Debugging ClipRectangle zeichnen
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    public Borderrect ZonaRect() {
        int x = getLeftPos((int) xps);
        int y = getUpPos((int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = 2 * ((int) yps - y) + y;
        // System.out.println(x + " " + y + " " + xd + " " + yd);
        return new Borderrect(x, y, xd, yd);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps);
        int up = getUpPos((int) yps);
        // int scale = getScale   ( ((int) xps), ((int) yps) );

        // Figur zeichnen
        g.drawImage(ktemp, left, up);
    }
}