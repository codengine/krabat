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
import de.codengine.krabat.main.BorderRect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Awgust extends MovableMainAnim {
    // Alle GenericImage - Objekte
    private final GenericImage[] kral_head;
    private final GenericImage[] kral_body;
    private final GenericImage[] kral_walk;

    private int Zwinker = 0;
    private static final int BODYOFFSET = 39;

    private int Verhinderkopf;
    private static final int MAX_VERHINDERKOPF = 2;

    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;

    private int Head = 0;

    // Spritevariablen
    private static final int CWIDTH = 81;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 155;

    // Abstaende default
    private static final int[] CVERT_DIST = {6, 2, 6, 2, 6};

    private static final int SLOWY = 10;  // dsgl. fuer y - Richtung aller wieviel Pix. die Schritte kleiner werden

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Awgust(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        kral_walk = new GenericImage[4];
        kral_head = new GenericImage[8];
        kral_body = new GenericImage[2];

        initImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    // Bilder vorbereiten
    private void initImages() {
        kral_walk[0] = getPicture("gfx-dd/zelen/kral-u-l2.png");
        kral_walk[1] = getPicture("gfx-dd/zelen/kral-u-l4.png");
        kral_walk[2] = getPicture("gfx-dd/zelen/kral-u-l6.png");
        kral_walk[3] = getPicture("gfx-dd/zelen/kral-u-l8.png");

        kral_head[0] = getPicture("gfx-dd/zelen/kral-uh1.png");
        kral_head[1] = getPicture("gfx-dd/zelen/kral-uh1a.png");
        kral_head[2] = getPicture("gfx-dd/zelen/kral-uh2.png");
        kral_head[3] = getPicture("gfx-dd/zelen/kral-uh3.png");
        kral_head[4] = getPicture("gfx-dd/zelen/kral-uh4.png");
        kral_head[5] = getPicture("gfx-dd/zelen/kral-uh5.png");
        kral_head[6] = getPicture("gfx-dd/zelen/kral-uh6.png");
        kral_head[7] = getPicture("gfx-dd/zelen/kral-uh7.png");

        kral_body[0] = getPicture("gfx-dd/zelen/kral-ub1.png");
        kral_body[1] = getPicture("gfx-dd/zelen/kral-ub2.png");
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
    public synchronized boolean move() {
        // Variablen uebernehmen (Threadsynchronisierung)
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
            if (animPos == 4) {
                animPos = 1;
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

        if (animPos == 0) {
            animPos = 1;       // Animationsimage bei Neubeginn initialis.
        }
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
        drawHim(offGraph, false, false); // Grafik, erhobeneHand, isTalking
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

        drawHim(offGraph, erhobeneHand, true); // Grafik, erhobeneHand, isTalking
    }

    public GenericPoint evalAwgustTalkPoint() {
        // Hier Position des Textes berechnen
        BorderRect temp = getBoundingBox();
        return new GenericPoint((temp.bottomRightPoint.x + temp.topLeftPoint.x) / 2, temp.topLeftPoint.y - 50);
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
        return calcScaleDefault(y, defaultScale);
    }

    private void drawHim(GenericDrawingContext g, boolean handRaised, boolean isTalking) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // Figur zeichnen
        if (isTalking) {
            // beim Reden und ggf. mit erhobener Hand
            g.drawImage(kral_head[Head], left, up, Koerperbreite, Kopfhoehe);
            g.drawImage(kral_body[handRaised ? 1 : 0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
        } else {
            // beim Stehen oder Laufen
            if (animPos == 0) {
                // er steht, also getrennt Kopf und Body
                g.drawImage(kral_head[Zwinker], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(kral_body[0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
            } else {
                // er laeuft, das sind hier gemeinsame Images
                g.drawImage(kral_walk[animPos], left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);
            }
        }
    }
}