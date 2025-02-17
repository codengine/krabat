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

public class KrabatDrasta extends Krabat {
    private static final Logger log = LoggerFactory.getLogger(KrabatDrasta.class);
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
    // private static final int[] CUP_DIST    = {2, 2, 2, 2, 4, 2, 2, 2, 4, 2};
    // private static final int[] CDOWN_DIST  = {2, 2, 2, 4, 2, 2, 2, 4, 2, 2};

    // private static final int[] CUP_DIST    = {1, 1, 1, 1, 2, 1, 1, 1, 2, 1};
    // private static final int[] CDOWN_DIST  = {1, 1, 1, 2, 1, 1, 1, 2, 1, 1};

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

    public KrabatDrasta(Start caller) {
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

        InitImages();

        // mal anders: Anstieg berechnen fuer Zoomingfunktion
        yFaktor = -(1.0f / 100.0f);
        xFaktor = -(1.0f / 100.0f);

        Verhinderlieshead = MAX_VERHINDERLIESHEAD;
    }

    // Bilder vorbereiten
    private void InitImages() {
        // Normal - Images, default
        krabat_left[0] = getPicture("gfx/anims/s-l-10.gif");  // Stehen
        krabat_left[1] = getPicture("gfx/anims/s-l-10a.gif"); // Zwinkern
        krabat_left[2] = getPicture("gfx/anims/s-l-5.gif");   // Laufen
        krabat_left[3] = getPicture("gfx/anims/s-l-6.gif");
        krabat_left[4] = getPicture("gfx/anims/s-l-7.gif");
        krabat_left[5] = getPicture("gfx/anims/s-l-8.gif");
        krabat_left[6] = getPicture("gfx/anims/s-l-9.gif");
        krabat_left[7] = getPicture("gfx/anims/s-l-0.gif");
        krabat_left[8] = getPicture("gfx/anims/s-l-1.gif");
        krabat_left[9] = getPicture("gfx/anims/s-l-2.gif");
        krabat_left[10] = getPicture("gfx/anims/s-l-3.gif");
        krabat_left[11] = getPicture("gfx/anims/s-l-4.gif");

        krabat_right[0] = getPicture("gfx/anims/s-r-10.gif");   // Stehen
        krabat_right[1] = getPicture("gfx/anims/s-r-10a.gif");  // Zwinkern
        krabat_right[2] = getPicture("gfx/anims/s-r-0.gif");    // Laufen
        krabat_right[3] = getPicture("gfx/anims/s-r-1.gif");
        krabat_right[4] = getPicture("gfx/anims/s-r-2.gif");
        krabat_right[5] = getPicture("gfx/anims/s-r-3.gif");
        krabat_right[6] = getPicture("gfx/anims/s-r-4.gif");
        krabat_right[7] = getPicture("gfx/anims/s-r-5.gif");
        krabat_right[8] = getPicture("gfx/anims/s-r-6.gif");
        krabat_right[9] = getPicture("gfx/anims/s-r-7.gif");
        krabat_right[10] = getPicture("gfx/anims/s-r-8.gif");
        krabat_right[11] = getPicture("gfx/anims/s-r-9.gif");

        krabat_front[0] = getPicture("gfx/anims/s-u-10.gif");   // Stehen
        krabat_front[1] = getPicture("gfx/anims/s-u-10a.gif");  // Zwinkern
        krabat_front[2] = getPicture("gfx/anims/s-u-8.gif");    // Laufen
        krabat_front[3] = getPicture("gfx/anims/s-u-1.gif");
        krabat_front[4] = getPicture("gfx/anims/s-u-2.gif");
        krabat_front[5] = getPicture("gfx/anims/s-u-3.gif");
        krabat_front[6] = getPicture("gfx/anims/s-u-4.gif");
        krabat_front[7] = getPicture("gfx/anims/s-u-5.gif");
        krabat_front[8] = getPicture("gfx/anims/s-u-6.gif");
        krabat_front[9] = getPicture("gfx/anims/s-u-7.gif");

        krabat_back[0] = getPicture("gfx/anims/s-o-10.gif");  // Stehen
        krabat_back[1] = getPicture("gfx/anims/s-o-10.gif");  // Zwinkern (gibts hier nicht, aber egal)
        krabat_back[2] = getPicture("gfx/anims/s-o-4.gif");   // Laufen
        krabat_back[3] = getPicture("gfx/anims/s-o-5.gif");
        krabat_back[4] = getPicture("gfx/anims/s-o-6.gif");
        krabat_back[5] = getPicture("gfx/anims/s-o-7.gif");
        krabat_back[6] = getPicture("gfx/anims/s-o-8.gif");
        krabat_back[7] = getPicture("gfx/anims/s-o-1.gif");
        krabat_back[8] = getPicture("gfx/anims/s-o-2.gif");
        krabat_back[9] = getPicture("gfx/anims/s-o-3.gif");

        krabat_floete[0] = getPicture("gfx/anims/s-u-f1.gif");
        krabat_floete[1] = getPicture("gfx/anims/s-u-f2.gif");
        krabat_floete[2] = getPicture("gfx/anims/s-u-f3.gif");
        krabat_floete[3] = getPicture("gfx/anims/s-u-f4.gif");

        krabat_rohodz[0] = getPicture("gfx/anims/s-u-fr1.gif");
        krabat_rohodz[1] = getPicture("gfx/anims/s-u-fr2.gif");
        krabat_rohodz[2] = getPicture("gfx/anims/s-u-fr3.gif");
        krabat_rohodz[3] = getPicture("gfx/anims/s-u-fr4.gif");

        krabat_left_talk_head[0] = getPicture("gfx/anims/s-l-h0.gif");
        krabat_left_talk_head[1] = getPicture("gfx/anims/s-l-h1.gif");
        krabat_left_talk_head[2] = getPicture("gfx/anims/s-l-h2.gif");
        krabat_left_talk_head[3] = getPicture("gfx/anims/s-l-h3.gif");
        krabat_left_talk_head[4] = getPicture("gfx/anims/s-l-h4.gif");
        krabat_left_talk_head[5] = getPicture("gfx/anims/s-l-h5.gif");
        krabat_left_talk_head[6] = getPicture("gfx/anims/s-l-h6.gif");
        krabat_left_talk_head[7] = getPicture("gfx/anims/s-l-h7.gif");

        krabat_left_talk_body[0] = getPicture("gfx/anims/s-l-b0.gif");
        krabat_left_talk_body[1] = getPicture("gfx/anims/s-l-b1.gif");
        krabat_left_talk_body[2] = getPicture("gfx/anims/s-l-b2.gif");
        krabat_left_talk_body[3] = getPicture("gfx/anims/s-l-b3.gif");

        krabat_right_talk_head[0] = getPicture("gfx/anims/s-r-h0.gif");
        krabat_right_talk_head[1] = getPicture("gfx/anims/s-r-h1.gif");
        krabat_right_talk_head[2] = getPicture("gfx/anims/s-r-h2.gif");
        krabat_right_talk_head[3] = getPicture("gfx/anims/s-r-h3.gif");
        krabat_right_talk_head[4] = getPicture("gfx/anims/s-r-h4.gif");
        krabat_right_talk_head[5] = getPicture("gfx/anims/s-r-h5.gif");
        krabat_right_talk_head[6] = getPicture("gfx/anims/s-r-h6.gif");
        krabat_right_talk_head[7] = getPicture("gfx/anims/s-r-h7.gif");

        krabat_right_talk_body[0] = getPicture("gfx/anims/s-r-b0.gif");
        krabat_right_talk_body[1] = getPicture("gfx/anims/s-r-b1.gif");
        krabat_right_talk_body[2] = getPicture("gfx/anims/s-r-b2.gif");
        krabat_right_talk_body[3] = getPicture("gfx/anims/s-r-b3.gif");

        krabat_up_talk_head[0] = getPicture("gfx/anims/s-o-h0.gif");
        krabat_up_talk_head[1] = getPicture("gfx/anims/s-o-h1.gif");

        krabat_up_talk_body[0] = getPicture("gfx/anims/s-o-b0.gif");
        krabat_up_talk_body[1] = getPicture("gfx/anims/s-o-b1.gif");
        krabat_up_talk_body[2] = getPicture("gfx/anims/s-o-b2.gif");

        krabat_down_talk_head[0] = getPicture("gfx/anims/s-u-h0.gif");
        krabat_down_talk_head[1] = getPicture("gfx/anims/s-u-h1.gif");
        krabat_down_talk_head[2] = getPicture("gfx/anims/s-u-h2.gif");
        krabat_down_talk_head[3] = getPicture("gfx/anims/s-u-h3.gif");
        krabat_down_talk_head[4] = getPicture("gfx/anims/s-u-h4.gif");
        krabat_down_talk_head[5] = getPicture("gfx/anims/s-u-h5.gif");

        krabat_down_talk_body[0] = getPicture("gfx/anims/s-u-b0.gif");
        krabat_down_talk_body[1] = getPicture("gfx/anims/s-u-b1.gif");
        krabat_down_talk_body[2] = getPicture("gfx/anims/s-u-b2.gif");

        krabat_left_take_oben[0] = getPicture("gfx/anims/s-l-takeoben.gif");
        krabat_left_take_mitte[0] = getPicture("gfx/anims/s-l-takemitte.gif");
        krabat_left_take_unten[0] = getPicture("gfx/anims/s-l-takeunten.gif");
        krabat_left_take_unten2[0] = getPicture("gfx/anims/s-l-takeunten2.gif");

        krabat_up_take_oben[0] = getPicture("gfx/anims/s-o-takeoben.gif");
        krabat_up_take_mitte[0] = getPicture("gfx/anims/s-o-takemitte.gif");
        krabat_up_take_unten[0] = getPicture("gfx/anims/s-o-takeunten.gif");

        krabat_right_take_oben[0] = getPicture("gfx/anims/s-r-takeoben.gif");
        krabat_right_take_mitte[0] = getPicture("gfx/anims/s-r-takemitte.gif");
        krabat_right_take_unten[0] = getPicture("gfx/anims/s-r-takeunten.gif");
        krabat_right_take_unten2[0] = getPicture("gfx/anims/s-r-takeunten2.gif");

        krabat_down_take_mitte[0] = getPicture("gfx/anims/s-u-takemitte.gif");
        krabat_down_take_unten[0] = getPicture("gfx/anims/s-u-takeunten.gif");

        krabat_gibkluc = getPicture("gfx/anims/s-l-gibkluc.gif");
        krabat_giblist = getPicture("gfx/anims/s-l-giblist.gif");
        krabat_gibmetall = getPicture("gfx/anims/s-l-gibmetall.gif");
        krabat_gibskizze = getPicture("gfx/anims/s-l-gibskizze.gif");
        krabat_gibwino = getPicture("gfx/anims/s-l-gibwino.gif");
        krabat_gibwop = getPicture("gfx/anims/s-l-gibwop.gif");

        krabat_wosusk[0] = getPicture("gfx/anims/s-u-wosusk1.gif");
        krabat_wosusk[1] = getPicture("gfx/anims/s-u-wosusk2.gif");

        krabat_hammer[0] = getPicture("gfx/anims/s-o-hammer1.gif");
        krabat_hammer[1] = getPicture("gfx/anims/s-o-hammer2.gif");

        krabat_feuer[0] = getPicture("gfx/anims/s-u-kamj1.gif");
        krabat_feuer[1] = getPicture("gfx/anims/s-u-kamj2.gif");
        krabat_feuer[2] = getPicture("gfx/anims/s-u-kamj3.gif");

        krabat_left_liesbuch_head[0] = getPicture("gfx/anims/s-l-c1.gif");
        krabat_left_liesbuch_head[1] = getPicture("gfx/anims/s-l-c2.gif");
        krabat_left_liesbuch_head[2] = getPicture("gfx/anims/s-l-c3.gif");
        krabat_left_liesbuch_head[3] = getPicture("gfx/anims/s-l-c4.gif");
        krabat_left_liesbuch_head[4] = getPicture("gfx/anims/s-l-c5.gif");

        krabat_right_liesbuch_head[0] = getPicture("gfx/anims/s-r-c1.gif");
        krabat_right_liesbuch_head[1] = getPicture("gfx/anims/s-r-c2.gif");
        krabat_right_liesbuch_head[2] = getPicture("gfx/anims/s-r-c3.gif");
        krabat_right_liesbuch_head[3] = getPicture("gfx/anims/s-r-c4.gif");
        krabat_right_liesbuch_head[4] = getPicture("gfx/anims/s-r-c5.gif");

        krabat_left_liesbuch_body = getPicture("gfx/anims/s-l-bc.gif");
        krabat_right_liesbuch_body = getPicture("gfx/anims/s-r-bc.gif");
    }


    // Laufen mit Krabat ////////////////////////////////////////////////////////////////

    // Krabat um einen Schritt weitersetzen, Achtung ! Diese Routine wird sowohl im "MousePressed" - Event
    // als auch im "Paint" - Event angesprungen...

    // Diese Routine bleibt unveraendert, unabhaengig davon, wie Krabat gerade aussieht
    @Override
    public synchronized void Move() {
        // Wenn kein Laufen gewuenscht, dann auch nicht laufen!
        if (!isWalking && !isWandering) {
            return;
        }

        // Variablen uebernehmen (Threadsynchronisierung)
        horizontal = Thorizontal;
        walkto = new GenericPoint(Twalkto.x, Twalkto.y);
        directionX = tDirectionX;
        directionY = tDirectionY;

        // System.out.println ("Move: Vorher " + (int) xps + " " + (int) yps + " nach " + walkto.x + " " + walkto.y);

        if (horizontal)
        // Horizontal laufen, es gelten die Images left und right, gehen von 2 bis 11, von oben 1 bis 9
        {
            // Animationsphase weiterschalten,
            anim_pos++;

            if (anim_pos > 11) {
                anim_pos = 2;
            }

            // neuen Punkt ermitteln und setzen
            // System.out.println ("Xverschiebung!");
            VerschiebeX();
            xps = txps;
            yps = typs;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkto.x - (int) txps) * directionX.getVal() <= 0) {
                // System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                isWalking = false;
                if (!isWandering && clearanimpos) {
                    anim_pos = 0;
                }
            }
        } else
        // Vertikal laufen, Images front und back von 2 bis 9, von oben 1 bis 9
        {
            // Animationsphase weiterschalten
            anim_pos++;

            // Ueberschreitungen feststellen normale Ansicht
            if (anim_pos > 9) {
                anim_pos = 2;
            }

            // neuen Punkt ermitteln und setzen
            // System.out.println ("Yverschiebung!");
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * directionY.getVal() <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                isWalking = false;
                if (!isWandering && clearanimpos) {
                    anim_pos = 0;
                }
            }
        }

        if (!isWalking && !isWandering) {
            // System.out.println("Krabatpos korrigiert auf " + walkto.x + " " + walkto.y);
            SetKrabatPos(walkto);
            if (clearanimpos) {
                anim_pos = 0;
            }
        }

        // System.out.println ("Move: Nachher " + (int) xps + " " + (int) yps + " nach " + walkto.x + " " + walkto.y);

        // if (anim_pos == 0)
        //{
        // System.out.println("Animpos reset!");
        //}
        // pos_x = (int) xps;
        // pos_y = (int) yps;
    }

    // Horizontal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void VerschiebeX() {
        // Verschieberoutine, die fuer normalen Krabat und Krabat in sl. drasta verwendet wird
        // Skalierungsfaktor holen
        float scale = getScale((int) yps);

        // Zooming - Faktor beruecksichtigen in x - Richtung
        // muss nach Richtung getrennt vorgenommen werden
        float horiz_dist = getHorizDist(scale);

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = 0;
        if (horiz_dist != 0) {
            z = Math.abs(xps - walkto.x) / horiz_dist;
        }

        // BUGFIX: kleine z nicht zulassen!!!
        if (z < 1) {
            z = 0;
        }

        typs = yps;
        if (z != 0) {
            typs += directionY.getVal() * (Math.abs(yps - walkto.y) / z);
        }

        // System.out.println ("XOffset = " + z + " Yoffset = " + Math.abs (yps - walkto.y));

        txps = xps + directionX.getVal() * horiz_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    private float getHorizDist(float scale) {
        float helper;
        if (directionX == RIGHT) {
            // nach rechts laufen
            helper = CRIGHT_DIST[anim_pos];
            // horiz_dist = helper * (float) Math.pow (xFaktor, scale);
        } else {
            // nach links laufen
            helper = CLEFT_DIST[anim_pos];
            // horiz_dist = helper * (float) Math.pow (xFaktor, scale);
        }
        float horiz_dist = helper * (scale * xFaktor + 1.0f);

        // System.out.println ("Horizontaler Abstand " + horiz_dist + " Pixel.");

        // nicht kleiner 1
        return horiz_dist < 1 ? 1 : horiz_dist;
    }

    // Vertikal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void VerschiebeY() {
        // Verschieberoutine, die fuer normalen Krabat und Krabat in sl. drasta verwendet wird
        // Skalierungsfaktor holen
        float scale = getScale((int) yps);

        // Zooming - Faktor beruecksichtigen in y - Richtung
        // muss nach Richtung getrennt vorgenommen werden
        float vert_dist = getVertDist(scale);

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vert_dist;

        // BUGFIX: kleine z nicht zulassen!!!
        if (z < 1) {
            z = 0;
        }

        txps = xps;
        if (z != 0) {
            txps += directionX.getVal() * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + directionY.getVal() * vert_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    private float getVertDist(float scale) {
        float helper;
        if (directionY == DOWN) {
            // nach unten laufen
            helper = CDOWN_DIST[anim_pos];
            // vert_dist = helper * (float) Math.pow (yFaktor, scale);
        } else {
            // nach oben laufen
            helper = CUP_DIST[anim_pos];
            // vert_dist = helper * (float) Math.pow (yFaktor, scale);
        }
        float vert_dist = helper * (yFaktor * scale + 1.0f);

        // System.out.println ("Vertikaler Abstand " + vert_dist + " Pixel.");

        // ist mal besser nicht kleiner als 1
        return vert_dist < 1 ? 1 : vert_dist;
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    @Override
    public synchronized void MoveTo(GenericPoint aim) {
        // Variablen an Move uebergeben
        Twalkto = aim;
        Thorizontal = calcHorizontal(aim, 22);

        // Laufrichtung ermitteln
        tDirectionX = aim.x > (int) xps ? RIGHT : LEFT;
        tDirectionY = aim.y > (int) yps ? DOWN : UP;

        // Hier Ueberpruefen, ob es sich auch lohnt, zuerst Scaling holen
        int scale = getScale((int) yps);

        // Lohnen-Variablen dem Scaling anpassen
        int lohnenx = (int) ((float) CLOHNENX * (xFaktor * scale + 1.0f));
        int lohneny = (int) ((float) CLOHNENY * (yFaktor * scale + 1.0f));

        // Abfangen, wenn zu klein
        if (lohnenx < 1) {
            lohnenx = 1;
        }
        if (lohneny < 1) {
            lohneny = 1;
        }

        if (Math.abs(aim.x - (int) xps) < lohnenx &&
                Math.abs(aim.y - (int) yps) < lohneny) {
            isWalking = false;
            log.debug("Lohnt sich nicht !");
            if (!isWandering && clearanimpos) {
                anim_pos = 0;
            }
            return;
        }

        // normales Laufen
        if (anim_pos < 2) {
            anim_pos = 2;
        }

        isWalking = true;                             // Stiefel los !
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
            anim_pos = Zwinker;  // hoffentlich geht das gut...
        }

        // je nach Richtung Sprite auswaehlen und zeichnen
        if (horizontal) {
            // nach links laufen
            if (directionX == LEFT) {
                MaleIhn(offGraph, krabat_left[anim_pos]);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                MaleIhn(offGraph, krabat_right[anim_pos]);
            }
        } else {
            // Bei normaler Darstellung
            if (!upsidedown) {
                // nach oben laufen
                if (directionY == UP) {
                    MaleIhn(offGraph, krabat_back[anim_pos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    MaleIhn(offGraph, krabat_front[anim_pos]);
                }
            } else {
                // nach oben laufen
                if (directionY == UP) {
                    MaleIhn(offGraph, krabat_front[anim_pos]);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    MaleIhn(offGraph, krabat_back[anim_pos]);
                }
            }
        }
    }


    // Abspielen einer Animation
    @Override
    public void DoAnimation(GenericDrawingContext g) {
        switch (nAnimation) {
            case 2:    // Floete spielen
                SpieleFloete(g, nAnimStep++);
                break;

            case 5:  // Rohodz spielen
                if (nAnimStep == 0) {
                    mainFrame.wave.PlayFile("sfx/frohodz.wav");
                } ///////////////// Sound !!!!!!!!!!!!!!
                if (nAnimStep < rohodzWartezeit) {
                    SpieleRohodz(g, nAnimStep++);
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 32: // rechts unten aufheben ist Extra, weil GenericImage doppelt so gross
                if (nAnimStep < 4) {
                    MaleRechtsGross(g, krabat_right_take_unten[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 34: // rechts unten aufhebenII ist Extra, weil GenericImage doppelt so gross
                if (nAnimStep < 4) {
                    MaleRechtsGross(g, krabat_right_take_unten2[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 92: // links unten aufheben ist Extra, weil doppelte Groesse UND verschoben
                if (nAnimStep < 4) {
                    MaleLinksGross(g, krabat_left_take_unten[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 94: // links unten aufheben II ist Extra, weil doppelte Groesse UND verschoben
                // wird nur im Teil 4 fuer Aufheben von Stein und Stroh genutzt (SS)
                if (nAnimStep < 4) {
                    MaleLinksGross(g, krabat_left_take_unten2[0]);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 142: // Wino an Hlowny Straznik
                GibWein(g);
                break;

            case 146: // Wosusk essen (speziell)
                if (nAnimStep < 20) {
                    if (nAnimStep == 0) {
                        mainFrame.wave.PlayFile("sfx-dd/wosusk.wav");
                    } ////////////Sound!!!!!!!!!!!!!!!!!!!!!!
                    ZeigeBild(g, krabat_wosusk[nAnimStep % 4 < 2 ? 0 : 1], 0);
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 147: // Haemmern in Terassa
                if (nAnimStep < 24) {
                    ZeigeBild(g, krabat_hammer[nAnimStep % 12 < 6 ? 0 : 1], 0);
                    if (nAnimStep == 6 || nAnimStep == 18) {
                        mainFrame.wave.PlayFile("sfx-dd/schlag.wav");
                    }
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 154: // mache Feuer mit Feuersteinen
                if (nAnimStep < 12) {
                    if (nAnimStep == 0) {
                        mainFrame.wave.PlayFile("sfx/kamjeny.wav");
                    } /////////////////// Sound !!!!!!!!!!!
                    ZeigeBild(g, krabat_feuer[nAnimStep % 6 / 2], 0);
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 155: // lies aus Buch
                LiesBuch(g);
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

                    ZeigeBild(g, zeigen, nAnimStep++);
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

        }
    }

    private void ZeigeBild(GenericDrawingContext g, GenericImage welches, int tCount) {
        if (tCount == 2) {
            fAnimHelper = true;
        }
        MaleIhn(g, welches);
    }

    // Krabat spielt Floete incl Zoominginformationen
    private void SpieleFloete(GenericDrawingContext g, int tCount) {
        // Sound abspielen
        if (tCount == 0) {
            int zuffi = (int) (Math.random() * 4.99);
            mainFrame.wave.PlayFile("sfx/flejta" + (char) (zuffi + 49) + ".wav");
            Floetenwartezeit = Floetenwartezeitarray[zuffi];
        }

        if (--Floetenwartezeit < 1) {
            // als letztes normal nach vorn sehend hinstellen
            StopAnim();
            SetFacing(6);
            drawKrabat(g);
            return;
        }

        int nFrame = tCount % 8 / 2;
        MaleIhn(g, krabat_floete[nFrame]);
    }

    // Krabat spielt Rohodz incl Zoominginformationen
    private void SpieleRohodz(GenericDrawingContext g, int tCount) {
        if (tCount == 19) {
            // als letztes normal nach vorn sehend hinstellen
            SetFacing(6);
        }

        int nFrame = tCount % 8 / 2;
        MaleIhn(g, krabat_rohodz[nFrame]);
    }

    // Krabat liest aus dem Buch
    private void LiesBuch(GenericDrawingContext offGraph) {
        // sorum hinstellen, dass er zur Seite schaut
        if (GetFacing() == 12) {
            mainFrame.krabat.SetFacing(9);
        }
        if (GetFacing() == 6) {
            mainFrame.krabat.SetFacing(3);
        }

        // Clipping - Region setzen
        // Links - oben - Korrdinaten ermitteln
        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        float fHeight = CHEIGHT;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - (float) scale) * ((float) BODYOFFSET / fHeight));
        int Koerperhoehe = (int) (fHeight - (float) scale - Kopfhoehe);

        // Links-Oben-Koordinate verschiebt sich ungezoomt um 10 Pix.
        int Offset = Koerperbreite / 5;
        if (GetFacing() == 9) {
            left -= Offset;
        }
        if (GetFacing() == 3) {
            left += Offset;
        }

        // Breite und Hoehe ermitteln
        // int xd = 2 * ( ((int) xps) - left);
        // int yd = ((int) yps) - up;     
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
        switch (GetFacing()) {
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
    private void GibWein(GenericDrawingContext offGraph) {
        // Clipping - Region setzen
        // Links - oben - Korrdinaten ermitteln
        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        float fHeight = CHEIGHT;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - (float) scale) * ((float) BODYOFFSET / fHeight));
        int Koerperhoehe = (int) (fHeight - (float) scale - Kopfhoehe);

        // Links-Oben-Koordinate verschiebt sich ungezoomt um 5 Pix.
        int Offset = Koerperbreite / 10;
        left -= Offset;

        // Breite und Hoehe ermitteln
        // int xd = 2 * ( ((int) xps) - left);
        // int yd = ((int) yps) - up;     
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

        Rede(offGraph);
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

        Rede(offGraph);
    }

    private void Rede(GenericDrawingContext offGraph) {
        // Clipping - Region setzen
        krabatClipDefault(offGraph, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        float fHeight = CHEIGHT;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - (float) scale) * ((float) BODYOFFSET / fHeight));
        int Koerperhoehe = (int) (fHeight - (float) scale - Kopfhoehe);

        // je nach Richtung malen
        switch (GetFacing()) {
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
    protected int getLeftPos(int pox, int poy) {
        return calcLeftPosDefault(pox, poy);
    }

    @Override
    protected int getUpPos(int poy) {
        return calcUpPosDefault(poy);
    }

    // wird nur bei Default angesprungen
    @Override
    protected int getScale(int poy) {
        return calcScaleDefault(poy, defScale);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scale / 2, CHEIGHT - scale);
    }

    private void MaleLinksGross(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipExtraDefault(g, (int) xps, (int) yps, true);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // hier muss GenericImage noch verschoben werden, da es breiter ist und K ganz rechts steht
        left -= (CHEIGHT - scale) / 2;

        // Figur zeichnen, in x - und y-Richtung gleichgross
        g.drawImage(ktemp, left, up, CHEIGHT - scale, CHEIGHT - scale);
    }

    private void MaleRechtsGross(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClipExtraDefault(g, (int) xps, (int) yps, false);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) yps);
        int scale = getScale((int) yps);

        // Figur zeichnen, in x - und y-Richtung gleichgross
        g.drawImage(ktemp, left, up, CHEIGHT - scale, CHEIGHT - scale);
    }
}