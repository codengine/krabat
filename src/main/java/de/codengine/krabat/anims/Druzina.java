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

public class Druzina extends MovableMainAnim {
    // Alle GenericImage - Objekte
    private final GenericImage[] druzina_walk;

    private int Zwinker = 0;

    // Spritevariablen
    private static final int CWIDTH = 65;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 154;

    // Abstaende default
    private static final int[] CVERT_DIST = {3, 3, 1, 3, 1, 3};

    private static final int SLOWY = 10;  // dsgl. fuer y - Richtung aller wieviel Pix. die Schritte kleiner werden

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Druzina(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        druzina_walk = new GenericImage[6];

        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        druzina_walk[0] = getPicture("gfx-dd/zelen/gefolge1.png");
        druzina_walk[1] = getPicture("gfx-dd/zelen/gefolge1a.png");
        druzina_walk[2] = getPicture("gfx-dd/zelen/gefolge1-l2.png");
        druzina_walk[3] = getPicture("gfx-dd/zelen/gefolge1-l4.png");
        druzina_walk[4] = getPicture("gfx-dd/zelen/gefolge1-l6.png");
        druzina_walk[5] = getPicture("gfx-dd/zelen/gefolge1-l8.png");
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
            if (anim_pos == 6) {
                anim_pos = 2;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * directionY.getVal() <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                setPos(walkto);
                anim_pos = 0;
                return true;
            }
        }

        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void VerschiebeY() {
        verschiebeYdefault(CVERT_DIST[anim_pos], SLOWY);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void MoveTo(GenericPoint aim) {
        moveToDefault(aim);
        Thorizontal = false;

        if (anim_pos < 2) {
            anim_pos = 2;       // Animationsimage bei Neubeginn initialis.
        }
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
    @Override
    protected int getLeftPos(int pox, int poy) {
        return calcLeftPosDefault(pox, poy, scaleFactor);
    }

    @Override
    protected int getUpPos(int poy) {
        return calcUpPosDefault(poy);
    }

    @Override
    protected int getScale(int poy) {
        return calcScaleDefault(poy);
    }

    private void MaleIhn(GenericDrawingContext g) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Koerperhoehe = (int) ((float) CHEIGHT - scale);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // Figur zeichnen
        g.drawImage(druzina_walk[anim_pos], left, up, Koerperbreite, Koerperhoehe);
    }
}