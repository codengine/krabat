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

public class Fellowship extends MovableMainAnim {
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

    public Fellowship(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        druzina_walk = new GenericImage[6];

        initImages();
    }

    // Bilder vorbereiten
    private void initImages() {
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
    public synchronized boolean move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        // Animationen in x oder y Richtung
        boolean horizontal = tmpIsAnimHorizontal;
        walkTo = tmpWalkTo;
        directionX = tmpDirectionX;
        directionY = tmpDirectionY;

        if (!horizontal)
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            moveY();
            posX = tempPosX;
            posY = tempPosY;

            // Animationsphase weiterschalten
            animPos++;
            if (animPos == 6) {
                animPos = 2;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            moveY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkTo.y - (int) tempPosY) * directionY.getVal() <= 0) {
                setPos(walkTo);
                animPos = 0;
                return true;
            }
        }

        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void moveY() {
        moveYdefault(CVERT_DIST[animPos], SLOWY);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void moveTo(GenericPoint aim) {
        moveToDefault(aim);
        tmpIsAnimHorizontal = false;

        if (animPos < 2) {
            animPos = 2;       // Animationsimage bei Neubeginn initialis.
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

        if (animPos < 2) {
            animPos = Zwinker;
        }

        // hier wird er nur benoetigt fuer laufen oder rumstehen + zwinkern (also in MaleIhn)
        drawHim(offGraph);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int x, int y) {
        return calcLeftPosDefault(x, y, scaleFactor);
    }

    @Override
    protected int getUpPos(int y) {
        return calcUpPosDefault(y);
    }

    @Override
    protected int getScale(int y) {
        return calcScaleDefault(y);
    }

    private void drawHim(GenericDrawingContext g) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Koerperhoehe = (int) ((float) CHEIGHT - scale);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // Figur zeichnen
        g.drawImage(druzina_walk[animPos], left, up, Koerperbreite, Koerperhoehe);
    }
}