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

public class KrabatAppearance extends Krabat {
    private static final Logger log = LoggerFactory.getLogger(KrabatAppearance.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_left;
    private final GenericImage[] krabat_right;
    private final GenericImage[] krabat_front;
    private final GenericImage[] krabat_back;

    private final GenericImage[] krabat_floete;
    private final GenericImage[] krabat_rohodz;

    private final GenericImage[] krabat_left_talk_head;
    private final GenericImage[] krabat_left_talk_body;

    private final GenericImage[] krabat_right_talk_head;
    private final GenericImage[] krabat_right_talk_body;

    private final GenericImage[] krabat_up_talk_head;
    private final GenericImage[] krabat_up_talk_body;

    private final GenericImage[] krabat_down_talk_head;
    private final GenericImage[] krabat_down_talk_body;

    private final GenericImage[] krabat_left_take_oben;
    private final GenericImage[] krabat_left_take_mitte;
    private final GenericImage[] krabat_left_take_unten;
    private final GenericImage[] krabat_left_take_unten2;

    private final GenericImage[] krabat_up_take_oben;
    private final GenericImage[] krabat_up_take_mitte;
    private final GenericImage[] krabat_up_take_unten;

    private final GenericImage[] krabat_right_take_oben;
    private final GenericImage[] krabat_right_take_mitte;
    private final GenericImage[] krabat_right_take_unten;
    private final GenericImage[] krabat_right_take_unten2;

    private final GenericImage[] krabat_down_take_mitte;
    private final GenericImage[] krabat_down_take_unten;

    private GenericImage krabat_gibkluc;
    private GenericImage krabat_giblist;
    private GenericImage krabat_gibmetall;
    private GenericImage krabat_gibskizze;
    private GenericImage krabat_gibwino;
    private GenericImage krabat_gibwop;

    private final GenericImage[] krabat_wosusk;
    private final GenericImage[] krabat_hammer;
    private final GenericImage[] krabat_feuer;

    private final GenericImage[] krabat_left_liesbuch_head;
    private final GenericImage[] krabat_right_liesbuch_head;
    private GenericImage krabat_left_liesbuch_body;
    private GenericImage krabat_right_liesbuch_body;

    // Spritevariablen
    private static final int CWIDTH = 50;  // Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 100;

    // Abstaende default von der Seite
    private static final int[] CLEFT_DIST = {4, 4, 7, 7, 6, 6, 7, 4, 8, 9, 6, 7};
    private static final int[] CRIGHT_DIST = {4, 4, 4, 8, 9, 6, 7, 7, 7, 6, 6, 7};

    private static final int[] CUP_DIST = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};
    private static final int[] CDOWN_DIST = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

    // Variablen fuer Laufberechnung
    private static final int CLOHNENX = 10;  // Werte fuer Entscheidung, ob sich
    private static final int CLOHNENY = 7;  // Laufen ueberhaupt lohnt (halber Schritt)

    private final float xFaktor; // geben an, wie gross die Basis der Exponentialfunktion ist
    private final float yFaktor; // mit dem die Zoomingmultiplikatoren berechnet werden

    private int TalkHead = 0;
    private int TalkBody = 0;

    private int TalkHeadUp = 0;
    private int TalkBodyUp = 0;

    private int TalkHeadDown = 0;
    private int TalkBodyDown = 0;

    private int Verhinderhead = 2;
    private static final int MAX_VERHINDERHEAD = 2;

    private int Verhinderkoerper;
    private static final int MAX_VERHINDERKOERPER = 8;

    private int Zwinker = 0;

    private static final int BODYOFFSET = 25;

    private int LiesHead = 0;

    private int Verhinderlieshead;

    private static final int MAX_VERHINDERLIESHEAD = 2;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public KrabatAppearance(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_left = new GenericImage[12];
        krabat_right = new GenericImage[12];
        krabat_front = new GenericImage[10];
        krabat_back = new GenericImage[10];
        krabat_floete = new GenericImage[4];
        krabat_rohodz = new GenericImage[4];

        krabat_left_take_oben = new GenericImage[1];
        krabat_left_take_mitte = new GenericImage[1];
        krabat_left_take_unten = new GenericImage[1];
        krabat_left_take_unten2 = new GenericImage[1];

        krabat_up_take_oben = new GenericImage[1];
        krabat_up_take_mitte = new GenericImage[1];
        krabat_up_take_unten = new GenericImage[1];

        krabat_right_take_oben = new GenericImage[1];
        krabat_right_take_mitte = new GenericImage[1];
        krabat_right_take_unten = new GenericImage[1];
        krabat_right_take_unten2 = new GenericImage[1];

        krabat_down_take_mitte = new GenericImage[1];
        krabat_down_take_unten = new GenericImage[1];

        krabat_left_talk_head = new GenericImage[8];
        krabat_left_talk_body = new GenericImage[4];

        krabat_right_talk_head = new GenericImage[8];
        krabat_right_talk_body = new GenericImage[4];

        krabat_up_talk_head = new GenericImage[2];
        krabat_up_talk_body = new GenericImage[3];

        krabat_down_talk_head = new GenericImage[6];
        krabat_down_talk_body = new GenericImage[3];

        krabat_wosusk = new GenericImage[2];
        krabat_hammer = new GenericImage[2];
        krabat_feuer = new GenericImage[3];

        krabat_left_liesbuch_head = new GenericImage[5];
        krabat_right_liesbuch_head = new GenericImage[5];

        initImages();

        // mal anders: Anstieg berechnen fuer Zoomingfunktion
        yFaktor = -(1.0f / 100.0f);
        xFaktor = -(1.0f / 100.0f);

        Verhinderlieshead = MAX_VERHINDERLIESHEAD;
    }

    // Bilder vorbereiten
    private void initImages() {
        // Normal - Images, default
        krabat_left[0] = getPicture("gfx/anims/s-l-10.png");  // Stehen
        krabat_left[1] = getPicture("gfx/anims/s-l-10a.png"); // Zwinkern
        krabat_left[2] = getPicture("gfx/anims/s-l-5.png");   // Laufen
        krabat_left[3] = getPicture("gfx/anims/s-l-6.png");
        krabat_left[4] = getPicture("gfx/anims/s-l-7.png");
        krabat_left[5] = getPicture("gfx/anims/s-l-8.png");
        krabat_left[6] = getPicture("gfx/anims/s-l-9.png");
        krabat_left[7] = getPicture("gfx/anims/s-l-0.png");
        krabat_left[8] = getPicture("gfx/anims/s-l-1.png");
        krabat_left[9] = getPicture("gfx/anims/s-l-2.png");
        krabat_left[10] = getPicture("gfx/anims/s-l-3.png");
        krabat_left[11] = getPicture("gfx/anims/s-l-4.png");

        krabat_right[0] = getPicture("gfx/anims/s-r-10.png");   // Stehen
        krabat_right[1] = getPicture("gfx/anims/s-r-10a.png");  // Zwinkern
        krabat_right[2] = getPicture("gfx/anims/s-r-0.png");    // Laufen
        krabat_right[3] = getPicture("gfx/anims/s-r-1.png");
        krabat_right[4] = getPicture("gfx/anims/s-r-2.png");
        krabat_right[5] = getPicture("gfx/anims/s-r-3.png");
        krabat_right[6] = getPicture("gfx/anims/s-r-4.png");
        krabat_right[7] = getPicture("gfx/anims/s-r-5.png");
        krabat_right[8] = getPicture("gfx/anims/s-r-6.png");
        krabat_right[9] = getPicture("gfx/anims/s-r-7.png");
        krabat_right[10] = getPicture("gfx/anims/s-r-8.png");
        krabat_right[11] = getPicture("gfx/anims/s-r-9.png");

        krabat_front[0] = getPicture("gfx/anims/s-u-10.png");   // Stehen
        krabat_front[1] = getPicture("gfx/anims/s-u-10a.png");  // Zwinkern
        krabat_front[2] = getPicture("gfx/anims/s-u-8.png");    // Laufen
        krabat_front[3] = getPicture("gfx/anims/s-u-1.png");
        krabat_front[4] = getPicture("gfx/anims/s-u-2.png");
        krabat_front[5] = getPicture("gfx/anims/s-u-3.png");
        krabat_front[6] = getPicture("gfx/anims/s-u-4.png");
        krabat_front[7] = getPicture("gfx/anims/s-u-5.png");
        krabat_front[8] = getPicture("gfx/anims/s-u-6.png");
        krabat_front[9] = getPicture("gfx/anims/s-u-7.png");

        krabat_back[0] = getPicture("gfx/anims/s-o-10.png");  // Stehen
        krabat_back[1] = getPicture("gfx/anims/s-o-10.png");  // Zwinkern (gibts hier nicht, aber egal)
        krabat_back[2] = getPicture("gfx/anims/s-o-4.png");   // Laufen
        krabat_back[3] = getPicture("gfx/anims/s-o-5.png");
        krabat_back[4] = getPicture("gfx/anims/s-o-6.png");
        krabat_back[5] = getPicture("gfx/anims/s-o-7.png");
        krabat_back[6] = getPicture("gfx/anims/s-o-8.png");
        krabat_back[7] = getPicture("gfx/anims/s-o-1.png");
        krabat_back[8] = getPicture("gfx/anims/s-o-2.png");
        krabat_back[9] = getPicture("gfx/anims/s-o-3.png");

        krabat_floete[0] = getPicture("gfx/anims/s-u-f1.png");
        krabat_floete[1] = getPicture("gfx/anims/s-u-f2.png");
        krabat_floete[2] = getPicture("gfx/anims/s-u-f3.png");
        krabat_floete[3] = getPicture("gfx/anims/s-u-f4.png");

        krabat_rohodz[0] = getPicture("gfx/anims/s-u-fr1.png");
        krabat_rohodz[1] = getPicture("gfx/anims/s-u-fr2.png");
        krabat_rohodz[2] = getPicture("gfx/anims/s-u-fr3.png");
        krabat_rohodz[3] = getPicture("gfx/anims/s-u-fr4.png");

        krabat_left_talk_head[0] = getPicture("gfx/anims/s-l-h0.png");
        krabat_left_talk_head[1] = getPicture("gfx/anims/s-l-h1.png");
        krabat_left_talk_head[2] = getPicture("gfx/anims/s-l-h2.png");
        krabat_left_talk_head[3] = getPicture("gfx/anims/s-l-h3.png");
        krabat_left_talk_head[4] = getPicture("gfx/anims/s-l-h4.png");
        krabat_left_talk_head[5] = getPicture("gfx/anims/s-l-h5.png");
        krabat_left_talk_head[6] = getPicture("gfx/anims/s-l-h6.png");
        krabat_left_talk_head[7] = getPicture("gfx/anims/s-l-h7.png");

        krabat_left_talk_body[0] = getPicture("gfx/anims/s-l-b0.png");
        krabat_left_talk_body[1] = getPicture("gfx/anims/s-l-b1.png");
        krabat_left_talk_body[2] = getPicture("gfx/anims/s-l-b2.png");
        krabat_left_talk_body[3] = getPicture("gfx/anims/s-l-b3.png");

        krabat_right_talk_head[0] = getPicture("gfx/anims/s-r-h0.png");
        krabat_right_talk_head[1] = getPicture("gfx/anims/s-r-h1.png");
        krabat_right_talk_head[2] = getPicture("gfx/anims/s-r-h2.png");
        krabat_right_talk_head[3] = getPicture("gfx/anims/s-r-h3.png");
        krabat_right_talk_head[4] = getPicture("gfx/anims/s-r-h4.png");
        krabat_right_talk_head[5] = getPicture("gfx/anims/s-r-h5.png");
        krabat_right_talk_head[6] = getPicture("gfx/anims/s-r-h6.png");
        krabat_right_talk_head[7] = getPicture("gfx/anims/s-r-h7.png");

        krabat_right_talk_body[0] = getPicture("gfx/anims/s-r-b0.png");
        krabat_right_talk_body[1] = getPicture("gfx/anims/s-r-b1.png");
        krabat_right_talk_body[2] = getPicture("gfx/anims/s-r-b2.png");
        krabat_right_talk_body[3] = getPicture("gfx/anims/s-r-b3.png");

        krabat_up_talk_head[0] = getPicture("gfx/anims/s-o-h0.png");
        krabat_up_talk_head[1] = getPicture("gfx/anims/s-o-h1.png");

        krabat_up_talk_body[0] = getPicture("gfx/anims/s-o-b0.png");
        krabat_up_talk_body[1] = getPicture("gfx/anims/s-o-b1.png");
        krabat_up_talk_body[2] = getPicture("gfx/anims/s-o-b2.png");

        krabat_down_talk_head[0] = getPicture("gfx/anims/s-u-h0.png");
        krabat_down_talk_head[1] = getPicture("gfx/anims/s-u-h1.png");
        krabat_down_talk_head[2] = getPicture("gfx/anims/s-u-h2.png");
        krabat_down_talk_head[3] = getPicture("gfx/anims/s-u-h3.png");
        krabat_down_talk_head[4] = getPicture("gfx/anims/s-u-h4.png");
        krabat_down_talk_head[5] = getPicture("gfx/anims/s-u-h5.png");

        krabat_down_talk_body[0] = getPicture("gfx/anims/s-u-b0.png");
        krabat_down_talk_body[1] = getPicture("gfx/anims/s-u-b1.png");
        krabat_down_talk_body[2] = getPicture("gfx/anims/s-u-b2.png");

        krabat_left_take_oben[0] = getPicture("gfx/anims/s-l-takeoben.png");
        krabat_left_take_mitte[0] = getPicture("gfx/anims/s-l-takemitte.png");
        krabat_left_take_unten[0] = getPicture("gfx/anims/s-l-takeunten.png");
        krabat_left_take_unten2[0] = getPicture("gfx/anims/s-l-takeunten2.png");

        krabat_up_take_oben[0] = getPicture("gfx/anims/s-o-takeoben.png");
        krabat_up_take_mitte[0] = getPicture("gfx/anims/s-o-takemitte.png");
        krabat_up_take_unten[0] = getPicture("gfx/anims/s-o-takeunten.png");

        krabat_right_take_oben[0] = getPicture("gfx/anims/s-r-takeoben.png");
        krabat_right_take_mitte[0] = getPicture("gfx/anims/s-r-takemitte.png");
        krabat_right_take_unten[0] = getPicture("gfx/anims/s-r-takeunten.png");
        krabat_right_take_unten2[0] = getPicture("gfx/anims/s-r-takeunten2.png");

        krabat_down_take_mitte[0] = getPicture("gfx/anims/s-u-takemitte.png");
        krabat_down_take_unten[0] = getPicture("gfx/anims/s-u-takeunten.png");

        krabat_gibkluc = getPicture("gfx/anims/s-l-gibkluc.png");
        krabat_giblist = getPicture("gfx/anims/s-l-giblist.png");
        krabat_gibmetall = getPicture("gfx/anims/s-l-gibmetall.png");
        krabat_gibskizze = getPicture("gfx/anims/s-l-gibskizze.png");
        krabat_gibwino = getPicture("gfx/anims/s-l-gibwino.png");
        krabat_gibwop = getPicture("gfx/anims/s-l-gibwop.png");

        krabat_wosusk[0] = getPicture("gfx/anims/s-u-wosusk1.png");
        krabat_wosusk[1] = getPicture("gfx/anims/s-u-wosusk2.png");

        krabat_hammer[0] = getPicture("gfx/anims/s-o-hammer1.png");
        krabat_hammer[1] = getPicture("gfx/anims/s-o-hammer2.png");

        krabat_feuer[0] = getPicture("gfx/anims/s-u-kamj1.png");
        krabat_feuer[1] = getPicture("gfx/anims/s-u-kamj2.png");
        krabat_feuer[2] = getPicture("gfx/anims/s-u-kamj3.png");

        krabat_left_liesbuch_head[0] = getPicture("gfx/anims/s-l-c1.png");
        krabat_left_liesbuch_head[1] = getPicture("gfx/anims/s-l-c2.png");
        krabat_left_liesbuch_head[2] = getPicture("gfx/anims/s-l-c3.png");
        krabat_left_liesbuch_head[3] = getPicture("gfx/anims/s-l-c4.png");
        krabat_left_liesbuch_head[4] = getPicture("gfx/anims/s-l-c5.png");

        krabat_right_liesbuch_head[0] = getPicture("gfx/anims/s-r-c1.png");
        krabat_right_liesbuch_head[1] = getPicture("gfx/anims/s-r-c2.png");
        krabat_right_liesbuch_head[2] = getPicture("gfx/anims/s-r-c3.png");
        krabat_right_liesbuch_head[3] = getPicture("gfx/anims/s-r-c4.png");
        krabat_right_liesbuch_head[4] = getPicture("gfx/anims/s-r-c5.png");

        krabat_left_liesbuch_body = getPicture("gfx/anims/s-l-bc.png");
        krabat_right_liesbuch_body = getPicture("gfx/anims/s-r-bc.png");
    }


    // Laufen mit Krabat ////////////////////////////////////////////////////////////////

    // Krabat um einen Schritt weitersetzen, Achtung ! Diese Routine wird sowohl im "MousePressed" - Event
    // als auch im "Paint" - Event angesprungen...

    // Diese Routine bleibt unveraendert, unabhaengig davon, wie Krabat gerade aussieht
    @Override
    public synchronized void move() {
        // Wenn kein Laufen gewuenscht, dann auch nicht laufen!
        if (!isWalking && !isWandering) {
            return;
        }

        // Variablen uebernehmen (Threadsynchronisierung)
        isAnimHorizontal = tmpIsAnimHorizontal;
        walkTo = new GenericPoint(tmpWalkTo.x, tmpWalkTo.y);
        directionX = tmpDirectionX;
        directionY = tmpDirectionY;

        if (isAnimHorizontal)
        // Horizontal laufen, es gelten die Images left und right, gehen von 2 bis 11, von oben 1 bis 9
        {
            // Animationsphase weiterschalten,
            animPos++;

            if (animPos > 11) {
                animPos = 2;
            }

            // neuen Punkt ermitteln und setzen
            moveX();
            posX = tempPosX;
            posY = tempPosY;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            moveX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkTo.x - (int) tempPosX) * directionX.getVal() <= 0) {
                isWalking = false;
                if (!isWandering && resetAnimPos) {
                    animPos = 0;
                }
            }
        } else
        // Vertikal laufen, Images front und back von 2 bis 9, von oben 1 bis 9
        {
            // Animationsphase weiterschalten
            animPos++;

            // Ueberschreitungen feststellen normale Ansicht
            if (animPos > 9) {
                animPos = 2;
            }

            // neuen Punkt ermitteln und setzen
            moveY();
            posX = tempPosX;
            posY = tempPosY;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            moveY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkTo.y - (int) tempPosY) * directionY.getVal() <= 0) {
                isWalking = false;
                if (!isWandering && resetAnimPos) {
                    animPos = 0;
                }
            }
        }

        if (!isWalking && !isWandering) {
            setPos(walkTo);
            if (resetAnimPos) {
                animPos = 0;
            }
        }
    }

    // Horizontal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void moveX() {
        // Verschieberoutine, die fuer normalen Krabat und Krabat in sl. drasta verwendet wird
        // Skalierungsfaktor holen
        float scale = getScale((int) posY);

        // Zooming - Faktor beruecksichtigen in x - Richtung
        // muss nach Richtung getrennt vorgenommen werden
        float horizDist = getHorizDist(scale);

        moveXkrabat(horizDist);
    }

    private float getHorizDist(float scale) {
        float helper;
        if (directionX == RIGHT) {
            // nach rechts laufen
            helper = CRIGHT_DIST[animPos];
        } else {
            // nach links laufen
            helper = CLEFT_DIST[animPos];
        }
        float horiz_dist = helper * (scale * xFaktor + 1.0f);

        // System.out.println ("Horizontaler Abstand " + horiz_dist + " Pixel.");

        // nicht kleiner 1
        return horiz_dist < 1 ? 1 : horiz_dist;
    }

    // Vertikal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void moveY() {
        // Verschieberoutine, die fuer normalen Krabat und Krabat in sl. drasta verwendet wird
        // Skalierungsfaktor holen
        float scale = getScale((int) posY);

        // Zooming - Faktor beruecksichtigen in y - Richtung
        // muss nach Richtung getrennt vorgenommen werden
        float vertDist = getVertDist(scale);

        moveYkrabat(vertDist);
    }

    private float getVertDist(float scale) {
        float helper;
        if (directionY == DOWN) {
            // nach unten laufen
            helper = CDOWN_DIST[animPos];
        } else {
            // nach oben laufen
            helper = CUP_DIST[animPos];
        }
        float vert_dist = helper * (yFaktor * scale + 1.0f);

        // System.out.println ("Vertikaler Abstand " + vert_dist + " Pixel.");

        // ist mal besser nicht kleiner als 1
        return vert_dist < 1 ? 1 : vert_dist;
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    @Override
    public synchronized void moveTo(GenericPoint aim) {
        // Hier Ueberpruefen, ob es sich auch lohnt, zuerst Scaling holen
        int scale = getScale((int) posY);
        // Lohnen-Variablen dem Scaling anpassen
        int lohnenx = (int) ((float) CLOHNENX * (xFaktor * scale + 1.0f));
        int lohneny = (int) ((float) CLOHNENY * (yFaktor * scale + 1.0f));

        moveToKrabat(aim, lohnenx, lohneny, 2, 22);
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    @Override
    public void drawKrabat(GenericDrawingContext offGraph) {
        // Default - Routine

        // Berechnung des Zwinkerns, wenn Krabat steht
        if (!isWalking && !isWandering) {
            int zuffi = (int) Math.round(Math.random() * 50);
            if (Zwinker == 1) {
                Zwinker = 0;
            } else {
                if (zuffi > 48) {
                    Zwinker = 1;
                }
            }
            animPos = Zwinker;  // hoffentlich geht das gut...
        }

        // je nach Richtung Sprite auswaehlen und zeichnen
        if (isAnimHorizontal) {
            // nach links laufen
            if (directionX == LEFT) {
                drawHim(offGraph, krabat_left[animPos]);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                drawHim(offGraph, krabat_right[animPos]);
            }
        } else {
            // Bei normaler Darstellung
            if (!upsideDown) {
                // nach oben laufen
                if (directionY == UP) {
                    drawHim(offGraph, krabat_back[animPos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    drawHim(offGraph, krabat_front[animPos]);
                }
            } else {
                // nach oben laufen
                if (directionY == UP) {
                    drawHim(offGraph, krabat_front[animPos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    drawHim(offGraph, krabat_back[animPos]);
                }
            }
        }
    }


    // Abspielen einer Animation
    @Override
    public void doAnimation(GenericDrawingContext g) {
        switch (nAnimation) {
            case 2:    // Floete spielen
                playFlute(g, nAnimStep++);
                break;

            case 5:  // Rohodz spielen
                if (nAnimStep == 0) {
                    mainFrame.soundPlayer.playFile("sfx/frohodz.wav");
                } ///////////////// Sound !!!!!!!!!!!!!!
                if (nAnimStep < rohodzWartezeit) {
                    playRohodz(g, nAnimStep++);
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 32: // rechts unten aufheben ist Extra, weil GenericImage doppelt so gross
                if (nAnimStep < 4) {
                    drawRightBig(g, krabat_right_take_unten[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 34: // rechts unten aufhebenII ist Extra, weil GenericImage doppelt so gross
                if (nAnimStep < 4) {
                    drawRightBig(g, krabat_right_take_unten2[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 92: // links unten aufheben ist Extra, weil doppelte Groesse UND verschoben
                if (nAnimStep < 4) {
                    drawLeftBig(g, krabat_left_take_unten[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 94: // links unten aufheben II ist Extra, weil doppelte Groesse UND verschoben
                // wird nur im Teil 4 fuer Aufheben von Stein und Stroh genutzt (SS)
                if (nAnimStep < 4) {
                    drawLeftBig(g, krabat_left_take_unten2[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 142: // Wino an Hlowny Straznik
                giveWine(g);
                break;

            case 146: // Wosusk essen (speziell)
                if (nAnimStep < 20) {
                    if (nAnimStep == 0) {
                        mainFrame.soundPlayer.playFile("sfx-dd/wosusk.wav");
                    } ////////////Sound!!!!!!!!!!!!!!!!!!!!!!
                    showImage(g, krabat_wosusk[nAnimStep % 4 < 2 ? 0 : 1], 0);
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 147: // Haemmern in Terassa
                if (nAnimStep < 24) {
                    showImage(g, krabat_hammer[nAnimStep % 12 < 6 ? 0 : 1], 0);
                    if (nAnimStep == 6 || nAnimStep == 18) {
                        mainFrame.soundPlayer.playFile("sfx-dd/schlag.wav");
                    }
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 154: // mache Feuer mit Feuersteinen
                if (nAnimStep < 12) {
                    if (nAnimStep == 0) {
                        mainFrame.soundPlayer.playFile("sfx/kamjeny.wav");
                    } /////////////////// Sound !!!!!!!!!!!
                    showImage(g, krabat_feuer[nAnimStep % 6 / 2], 0);
                    nAnimStep++;
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

            case 155: // lies aus Buch
                readBook(g);
                break;

            // Hier kommt eine grosse Kollektion von statisch angezeigten Bildern
            // deshalb immer dieselbe Anzeigeroutine
            default:
                if (nAnimStep < 4) {
                    GenericImage zeigen = null;

                    switch (nAnimation) {
                        case 30: // rechts oben aufheben
                            zeigen = krabat_right_take_oben[0];
                            break;

                        case 31: // rechts mitte aufheben
                            zeigen = krabat_right_take_mitte[0];
                            break;

                        case 61: // vorn mitte aufheben
                            zeigen = krabat_down_take_mitte[0];
                            break;

                        case 62: // vorn unten aufheben
                            zeigen = krabat_down_take_unten[0];
                            break;

                        case 90: // links oben aufheben
                            zeigen = krabat_left_take_oben[0];
                            break;

                        case 91: // links mitte aufheben
                            zeigen = krabat_left_take_mitte[0];
                            break;

                        case 120: // hinten oben aufheben
                            zeigen = krabat_up_take_oben[0];
                            break;

                        case 121: // hinten mitte aufheben
                            zeigen = krabat_up_take_mitte[0];
                            break;

                        case 122: // hinten unten aufheben
                            zeigen = krabat_up_take_unten[0];
                            break;

                        case 137: // Kluc an Dinglinger
                            zeigen = krabat_gibkluc;
                            break;

                        case 138: // List an Dinglinger
                            zeigen = krabat_giblist;
                            break;

                        case 139: // Metall an Dinglinger
                            zeigen = krabat_gibmetall;
                            break;

                        case 140: // Skizze an Dinglinger
                            zeigen = krabat_gibskizze;
                            break;

                        case 141: // Wopismo an Dinglinger
                            zeigen = krabat_gibwop;
                            break;

                        default:
                            log.error("Falsche Do-Animation verlangt! nAnimation = {}", nAnimation);
                            break;
                    }

                    showImage(g, zeigen, nAnimStep++);
                } else {
                    stopAnim();
                    drawKrabat(g);
                }
                break;

        }
    }

    private void showImage(GenericDrawingContext g, GenericImage image, int count) {
        if (count == 2) {
            fAnimHelper = true;
        }
        drawHim(g, image);
    }

    // Krabat spielt Floete incl Zoominginformationen
    private void playFlute(GenericDrawingContext g, int tCount) {
        // Sound abspielen
        if (tCount == 0) {
            int zuffi = (int) (Math.random() * 4.99);
            mainFrame.soundPlayer.playFile("sfx/flejta" + (char) (zuffi + 49) + ".wav");
            Floetenwartezeit = Floetenwartezeitarray[zuffi];
        }

        if (--Floetenwartezeit < 1) {
            // als letztes normal nach vorn sehend hinstellen
            stopAnim();
            setFacing(6);
            drawKrabat(g);
            return;
        }

        int nFrame = tCount % 8 / 2;
        drawHim(g, krabat_floete[nFrame]);
    }

    // Krabat spielt Rohodz incl Zoominginformationen
    private void playRohodz(GenericDrawingContext g, int tCount) {
        if (tCount == 19) {
            // als letztes normal nach vorn sehend hinstellen
            setFacing(6);
        }

        int nFrame = tCount % 8 / 2;
        drawHim(g, krabat_rohodz[nFrame]);
    }

    // Krabat liest aus dem Buch
    private void readBook(GenericDrawingContext offGraph) {
        // sorum hinstellen, dass er zur Seite schaut
        if (getFacing() == 12) {
            mainFrame.krabat.setFacing(9);
        }
        if (getFacing() == 6) {
            mainFrame.krabat.setFacing(3);
        }

        // Clipping - Region setzen
        // Links - oben - Korrdinaten ermitteln
        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        float fHeight = CHEIGHT;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - (float) scale) * ((float) BODYOFFSET / fHeight));
        int Koerperhoehe = (int) (fHeight - (float) scale - Kopfhoehe);

        // Links-Oben-Koordinate verschiebt sich ungezoomt um 10 Pix.
        int Offset = Koerperbreite / 5;
        if (getFacing() == 9) {
            left -= Offset;
        }
        if (getFacing() == 3) {
            left += Offset;
        }

        // Breite und Hoehe ermitteln
        offGraph.setClip(left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);

        // Fuer Debugging ClipRectangle zeichnen 
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);


        // neues Head-Image berechnen
        if (--Verhinderlieshead < 1) {
            Verhinderlieshead = MAX_VERHINDERLIESHEAD;
            LiesHead = (int) (Math.random() * 4.9);
        }

        // je nach Richtung malen
        switch (getFacing()) {
            case 3: // von rechts
                offGraph.drawImage(krabat_right_liesbuch_head[LiesHead], left, up, Koerperbreite, Kopfhoehe);
                offGraph.drawImage(krabat_right_liesbuch_body, left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;
            case 9: // von links
                offGraph.drawImage(krabat_left_liesbuch_head[LiesHead], left, up, Koerperbreite, Kopfhoehe);
                offGraph.drawImage(krabat_left_liesbuch_body, left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;
        }

    }

    // Krabat liest aus dem Buch
    private void giveWine(GenericDrawingContext offGraph) {
        // Clipping - Region setzen
        // Links - oben - Korrdinaten ermitteln
        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        float fHeight = CHEIGHT;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - (float) scale) * ((float) BODYOFFSET / fHeight));
        int Koerperhoehe = (int) (fHeight - (float) scale - Kopfhoehe);

        // Links-Oben-Koordinate verschiebt sich ungezoomt um 5 Pix.
        int Offset = Koerperbreite / 10;
        left -= Offset;

        // Breite und Hoehe ermitteln
        offGraph.setClip(left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);

        // Fuer Debugging ClipRectangle zeichnen 
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);

        // zeichnen und gut
        offGraph.drawImage(krabat_gibwino, left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);
    }

    // Zeichne Krabat beim Sprechen mit anderen Personen
    @Override
    public void talkKrabat(GenericDrawingContext offGraph) {
        // Default - Reden

        // Verschiedene Images fuer Head und Body berechnen
        if (--Verhinderhead < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            TalkHead = (int) (Math.random() * 7.9);
            TalkHeadUp = (int) (Math.random() * 1.9);
            TalkHeadDown = (int) (Math.random() * 5.9);
        }

        if (--Verhinderkoerper < 1) {
            Verhinderkoerper = MAX_VERHINDERKOERPER;
            TalkBody = (int) (Math.random() * 3.9);
            TalkBodyUp = (int) (Math.random() * 2.9);
            TalkBodyDown = (int) (Math.random() * 2.9);
        }

        talk(offGraph);
    }

    // Krabat beim Monolog (ohne Gestikulieren)
    @Override
    public void describeKrabat(GenericDrawingContext offGraph) {
        // Default - Reden

        // Verschiedene Images fuer Head und Body berechnen
        if (--Verhinderhead < 1) {
            Verhinderhead = MAX_VERHINDERHEAD;
            TalkHead = (int) (Math.random() * 7.9);
            TalkHeadUp = (int) (Math.random() * 1.9);
            TalkHeadDown = (int) (Math.random() * 5.9);
        }

        TalkBody = 0;

        talk(offGraph);
    }

    private void talk(GenericDrawingContext offGraph) {
        // Clipping - Region setzen
        krabatClipDefault(offGraph, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        float fHeight = CHEIGHT;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - (float) scale) * ((float) BODYOFFSET / fHeight));
        int Koerperhoehe = (int) (fHeight - (float) scale - Kopfhoehe);

        // je nach Richtung malen
        switch (getFacing()) {
            case 3: // von rechts
                offGraph.drawImage(krabat_right_talk_head[TalkHead], left, up, Koerperbreite, Kopfhoehe);
                offGraph.drawImage(krabat_right_talk_body[TalkBody], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;
            case 6: // von vorn
                offGraph.drawImage(krabat_down_talk_head[TalkHeadDown], left, up, Koerperbreite, Kopfhoehe);
                offGraph.drawImage(krabat_down_talk_body[TalkBodyDown], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;
            case 9: // von links
                offGraph.drawImage(krabat_left_talk_head[TalkHead], left, up, Koerperbreite, Kopfhoehe);
                offGraph.drawImage(krabat_left_talk_body[TalkBody], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;
            case 12: // von hinten
                offGraph.drawImage(krabat_up_talk_head[TalkHeadUp], left, up, Koerperbreite, Kopfhoehe);
                offGraph.drawImage(krabat_up_talk_body[TalkBodyUp], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;
        }
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int x, int y) {
        return calcLeftPosDefault(x, y);
    }

    @Override
    protected int getUpPos(int y) {
        return calcUpPosDefault(y);
    }

    // wird nur bei Default angesprungen
    @Override
    protected int getScale(int y) {
        return calcScaleDefault(y, defaultScale);
    }

    private void drawHim(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scale / 2, CHEIGHT - scale);
    }

    private void drawLeftBig(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipExtraDefault(g, (int) posX, (int) posY, true);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // hier muss GenericImage noch verschoben werden, da es breiter ist und K ganz rechts steht
        left -= (CHEIGHT - scale) / 2;

        // Figur zeichnen, in x - und y-Richtung gleichgross
        g.drawImage(ktemp, left, up, CHEIGHT - scale, CHEIGHT - scale);
    }

    private void drawRightBig(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipExtraDefault(g, (int) posX, (int) posY, false);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // Figur zeichnen, in x - und y-Richtung gleichgross
        g.drawImage(ktemp, left, up, CHEIGHT - scale, CHEIGHT - scale);
    }
}