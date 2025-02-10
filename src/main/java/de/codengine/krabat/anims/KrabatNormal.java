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

public class KrabatNormal extends Krabat {
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
    private final GenericImage[] krabat_up_take_oben2;
    private final GenericImage[] krabat_left_take_mitte;
    private final GenericImage[] krabat_left_take_mitte_verdeckt;
    private final GenericImage[] krabat_left_take_unten;
    private final GenericImage[] krabat_left_take_unten2; // fuer Teil 4 !!!

    private final GenericImage[] krabat_up_take_oben;
    private final GenericImage[] krabat_up_take_mitte;
    private final GenericImage[] krabat_up_take_unten;

    private final GenericImage[] krabat_right_take_oben;
    private final GenericImage[] krabat_right_take_mitte;
    private final GenericImage[] krabat_right_take_unten;
    private final GenericImage[] krabat_right_take_unten2;

    private final GenericImage[] krabat_down_take_mitte;
    private final GenericImage[] krabat_down_take_unten;

    private GenericImage krabat_legeschild;
    private GenericImage krabat_gibkrosik;
    private GenericImage krabat_gibcasnik;
    private GenericImage krabat_gibtiger;
    private GenericImage krabat_gibtolery;
    private GenericImage krabat_gibprikaz;
    private GenericImage krabat_gibwop;
    private final GenericImage[] krabat_takeblotooben;
    private final GenericImage[] krabat_takeblotounten;
    private GenericImage krabat_fallen;
    private GenericImage krabat_gibryba;
    private GenericImage krabat_wirfholz;
    private GenericImage krabat_wino;

    private final GenericImage[] krabat_stock;
    private final GenericImage[] krabat_wosusk;
    private final GenericImage[] krabat_hammer;
    private final GenericImage[] krabat_fangraben;
    private final GenericImage[] krabat_bretter;
    private final GenericImage[] krabat_feuer;

    private final GenericImage[] krabat_left_liesbuch_head;
    private final GenericImage[] krabat_right_liesbuch_head;
    private GenericImage krabat_left_liesbuch_body;
    private GenericImage krabat_right_liesbuch_body;

    private final GenericImage[] krabat_lies_koraktor;


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

    public KrabatNormal(Start caller) {
        super(caller);

        krabat_left = new GenericImage[12];
        krabat_right = new GenericImage[12];
        krabat_front = new GenericImage[10];
        krabat_back = new GenericImage[10];

        krabat_floete = new GenericImage[4];
        krabat_rohodz = new GenericImage[4];

        krabat_left_talk_head = new GenericImage[8];
        krabat_left_talk_body = new GenericImage[4];
        krabat_right_talk_head = new GenericImage[8];
        krabat_right_talk_body = new GenericImage[4];
        krabat_up_talk_head = new GenericImage[2];
        krabat_up_talk_body = new GenericImage[3];
        krabat_down_talk_head = new GenericImage[6];
        krabat_down_talk_body = new GenericImage[3];

        krabat_left_take_oben = new GenericImage[1];
        krabat_left_take_mitte = new GenericImage[1];
        krabat_left_take_mitte_verdeckt = new GenericImage[1];
        krabat_left_take_unten = new GenericImage[1];
        krabat_left_take_unten2 = new GenericImage[1];

        krabat_up_take_oben = new GenericImage[1];
        krabat_up_take_oben2 = new GenericImage[1];
        krabat_up_take_mitte = new GenericImage[1];
        krabat_up_take_unten = new GenericImage[1];

        krabat_right_take_oben = new GenericImage[1];
        krabat_right_take_mitte = new GenericImage[1];
        krabat_right_take_unten = new GenericImage[1];
        krabat_right_take_unten2 = new GenericImage[1];

        krabat_down_take_mitte = new GenericImage[1];
        krabat_down_take_unten = new GenericImage[1];

        krabat_stock = new GenericImage[2];
        krabat_wosusk = new GenericImage[2];
        krabat_hammer = new GenericImage[2];
        krabat_fangraben = new GenericImage[2];
        krabat_bretter = new GenericImage[2];
        krabat_feuer = new GenericImage[3];

        krabat_left_liesbuch_head = new GenericImage[5];
        krabat_right_liesbuch_head = new GenericImage[5];

        krabat_lies_koraktor = new GenericImage[2];

        krabat_takeblotooben = new GenericImage[2];
        krabat_takeblotounten = new GenericImage[2];

        InitImages();

        // mal anders: Anstieg berechnen fuer Zoomingfunktion
        yFaktor = -(1.0f / 100.0f);
        xFaktor = -(1.0f / 100.0f);

        Verhinderlieshead = MAX_VERHINDERLIESHEAD;
    }

    // Bilder vorbereiten
    private void InitImages() {
        // Normal - Images, default
        krabat_left[0] = getPicture("gfx/anims/k-l-10.gif");  // Stehen
        krabat_left[1] = getPicture("gfx/anims/k-l-10a.gif"); // Zwinkern
        krabat_left[2] = getPicture("gfx/anims/k-l-5.gif");   // Laufen
        krabat_left[3] = getPicture("gfx/anims/k-l-6.gif");
        krabat_left[4] = getPicture("gfx/anims/k-l-7.gif");
        krabat_left[5] = getPicture("gfx/anims/k-l-8.gif");
        krabat_left[6] = getPicture("gfx/anims/k-l-9.gif");
        krabat_left[7] = getPicture("gfx/anims/k-l-0.gif");
        krabat_left[8] = getPicture("gfx/anims/k-l-1.gif");
        krabat_left[9] = getPicture("gfx/anims/k-l-2.gif");
        krabat_left[10] = getPicture("gfx/anims/k-l-3.gif");
        krabat_left[11] = getPicture("gfx/anims/k-l-4.gif");

        krabat_right[0] = getPicture("gfx/anims/k-r-10.gif");   // Stehen
        krabat_right[1] = getPicture("gfx/anims/k-r-10a.gif");  // Zwinkern
        krabat_right[2] = getPicture("gfx/anims/k-r-0.gif");    // Laufen
        krabat_right[3] = getPicture("gfx/anims/k-r-1.gif");
        krabat_right[4] = getPicture("gfx/anims/k-r-2.gif");
        krabat_right[5] = getPicture("gfx/anims/k-r-3.gif");
        krabat_right[6] = getPicture("gfx/anims/k-r-4.gif");
        krabat_right[7] = getPicture("gfx/anims/k-r-5.gif");
        krabat_right[8] = getPicture("gfx/anims/k-r-6.gif");
        krabat_right[9] = getPicture("gfx/anims/k-r-7.gif");
        krabat_right[10] = getPicture("gfx/anims/k-r-8.gif");
        krabat_right[11] = getPicture("gfx/anims/k-r-9.gif");

        krabat_front[0] = getPicture("gfx/anims/k-u-10.gif");   // Stehen
        krabat_front[1] = getPicture("gfx/anims/k-u-10a.gif");  // Zwinkern
        krabat_front[2] = getPicture("gfx/anims/k-u-8.gif");    // Laufen
        krabat_front[3] = getPicture("gfx/anims/k-u-1.gif");
        krabat_front[4] = getPicture("gfx/anims/k-u-2.gif");
        krabat_front[5] = getPicture("gfx/anims/k-u-3.gif");
        krabat_front[6] = getPicture("gfx/anims/k-u-4.gif");
        krabat_front[7] = getPicture("gfx/anims/k-u-5.gif");
        krabat_front[8] = getPicture("gfx/anims/k-u-6.gif");
        krabat_front[9] = getPicture("gfx/anims/k-u-7.gif");

        krabat_back[0] = getPicture("gfx/anims/k-o-10.gif");  // Stehen
        krabat_back[1] = krabat_back[0];                       // Zwinkern (gibts hier nicht, aber egal)
        krabat_back[2] = getPicture("gfx/anims/k-o-4.gif");   // Laufen
        krabat_back[3] = getPicture("gfx/anims/k-o-5.gif");
        krabat_back[4] = getPicture("gfx/anims/k-o-6.gif");
        krabat_back[5] = getPicture("gfx/anims/k-o-7.gif");
        krabat_back[6] = getPicture("gfx/anims/k-o-8.gif");
        krabat_back[7] = getPicture("gfx/anims/k-o-1.gif");
        krabat_back[8] = getPicture("gfx/anims/k-o-2.gif");
        krabat_back[9] = getPicture("gfx/anims/k-o-3.gif");

        krabat_floete[0] = getPicture("gfx/anims/k-u-f1.gif");
        krabat_floete[1] = getPicture("gfx/anims/k-u-f2.gif");
        krabat_floete[2] = getPicture("gfx/anims/k-u-f3.gif");
        krabat_floete[3] = getPicture("gfx/anims/k-u-f4.gif");

        krabat_rohodz[0] = getPicture("gfx/anims/k-u-fr1.gif");
        krabat_rohodz[1] = getPicture("gfx/anims/k-u-fr2.gif");
        krabat_rohodz[2] = getPicture("gfx/anims/k-u-fr3.gif");
        krabat_rohodz[3] = getPicture("gfx/anims/k-u-fr4.gif");

        krabat_left_talk_head[0] = getPicture("gfx/anims/k-l-h0.gif");
        krabat_left_talk_head[1] = getPicture("gfx/anims/k-l-h1.gif");
        krabat_left_talk_head[2] = getPicture("gfx/anims/k-l-h2.gif");
        krabat_left_talk_head[3] = getPicture("gfx/anims/k-l-h3.gif");
        krabat_left_talk_head[4] = getPicture("gfx/anims/k-l-h4.gif");
        krabat_left_talk_head[5] = getPicture("gfx/anims/k-l-h5.gif");
        krabat_left_talk_head[6] = getPicture("gfx/anims/k-l-h6.gif");
        krabat_left_talk_head[7] = getPicture("gfx/anims/k-l-h7.gif");

        krabat_left_talk_body[0] = getPicture("gfx/anims/k-l-b0.gif");
        krabat_left_talk_body[1] = getPicture("gfx/anims/k-l-b1.gif");
        krabat_left_talk_body[2] = getPicture("gfx/anims/k-l-b2.gif");
        krabat_left_talk_body[3] = getPicture("gfx/anims/k-l-b3.gif");

        krabat_right_talk_head[0] = getPicture("gfx/anims/k-r-h0.gif");
        krabat_right_talk_head[1] = getPicture("gfx/anims/k-r-h1.gif");
        krabat_right_talk_head[2] = getPicture("gfx/anims/k-r-h2.gif");
        krabat_right_talk_head[3] = getPicture("gfx/anims/k-r-h3.gif");
        krabat_right_talk_head[4] = getPicture("gfx/anims/k-r-h4.gif");
        krabat_right_talk_head[5] = getPicture("gfx/anims/k-r-h5.gif");
        krabat_right_talk_head[6] = getPicture("gfx/anims/k-r-h6.gif");
        krabat_right_talk_head[7] = getPicture("gfx/anims/k-r-h7.gif");

        krabat_right_talk_body[0] = getPicture("gfx/anims/k-r-b0.gif");
        krabat_right_talk_body[1] = getPicture("gfx/anims/k-r-b1.gif");
        krabat_right_talk_body[2] = getPicture("gfx/anims/k-r-b2.gif");
        krabat_right_talk_body[3] = getPicture("gfx/anims/k-r-b3.gif");

        krabat_up_talk_head[0] = getPicture("gfx/anims/k-o-h0.gif");
        krabat_up_talk_head[1] = getPicture("gfx/anims/k-o-h1.gif");

        krabat_up_talk_body[0] = getPicture("gfx/anims/k-o-b0.gif");
        krabat_up_talk_body[1] = getPicture("gfx/anims/k-o-b1.gif");
        krabat_up_talk_body[2] = getPicture("gfx/anims/k-o-b2.gif");

        krabat_down_talk_head[0] = getPicture("gfx/anims/k-u-h0.gif");
        krabat_down_talk_head[1] = getPicture("gfx/anims/k-u-h1.gif");
        krabat_down_talk_head[2] = getPicture("gfx/anims/k-u-h2.gif");
        krabat_down_talk_head[3] = getPicture("gfx/anims/k-u-h3.gif");
        krabat_down_talk_head[4] = getPicture("gfx/anims/k-u-h4.gif");
        krabat_down_talk_head[5] = getPicture("gfx/anims/k-u-h5.gif");

        krabat_down_talk_body[0] = getPicture("gfx/anims/k-u-b0.gif");
        krabat_down_talk_body[1] = getPicture("gfx/anims/k-u-b1.gif");
        krabat_down_talk_body[2] = getPicture("gfx/anims/k-u-b2.gif");

        krabat_left_take_oben[0] = getPicture("gfx/anims/k-l-takeoben.gif");
        krabat_left_take_mitte[0] = getPicture("gfx/anims/k-l-takemitte.gif");
        krabat_left_take_mitte_verdeckt[0] = getPicture("gfx/anims/k-l-takemitte2.gif");
        krabat_left_take_unten[0] = getPicture("gfx/anims/k-l-takeunten.gif");
        krabat_left_take_unten2[0] = getPicture("gfx/anims/k-l-takeunten2.gif");

        krabat_up_take_oben[0] = getPicture("gfx/anims/k-o-takeoben.gif");
        krabat_up_take_oben2[0] = getPicture("gfx/anims/k-o-takeoben4.gif");
        krabat_up_take_mitte[0] = getPicture("gfx/anims/k-o-takemitte.gif");
        krabat_up_take_unten[0] = getPicture("gfx/anims/k-o-takeunten.gif");

        krabat_right_take_oben[0] = getPicture("gfx/anims/k-r-takeoben.gif");
        krabat_right_take_mitte[0] = getPicture("gfx/anims/k-r-takemitte.gif");
        krabat_right_take_unten[0] = getPicture("gfx/anims/k-r-takeunten.gif");
        krabat_right_take_unten2[0] = getPicture("gfx/anims/k-r-takeunten2.gif");

        krabat_down_take_mitte[0] = getPicture("gfx/anims/k-u-takemitte.gif");
        krabat_down_take_unten[0] = getPicture("gfx/anims/k-u-takeunten.gif");

        krabat_legeschild = getPicture("gfx/anims/k-l-legeschild.gif");
        krabat_gibkrosik = getPicture("gfx/anims/k-r-gibkrosik.gif");
        krabat_gibcasnik = getPicture("gfx/anims/k-l-gibcasnik.gif");
        krabat_gibprikaz = getPicture("gfx/anims/k-l-gibprikaz.gif");
        krabat_gibtiger = getPicture("gfx/anims/k-l-gibtiger.gif");
        krabat_gibtolery = getPicture("gfx/anims/k-l-gibtolery.gif");
        krabat_gibwop = getPicture("gfx/anims/k-l-gibwop.gif");

        krabat_takeblotooben[0] = getPicture("gfx/anims/k-o-takebloto.gif");
        krabat_takeblotooben[1] = getPicture("gfx/anims/k-o-takebloto2.gif");
        krabat_takeblotounten[0] = getPicture("gfx/anims/k-u-takebloto.gif");
        krabat_takeblotounten[1] = getPicture("gfx/anims/k-u-takebloto2.gif");

        krabat_fallen = getPicture("gfx/anims/k-o-fallen.gif");
        krabat_gibryba = getPicture("gfx/anims/k-r-gibryba.gif");
        krabat_wirfholz = getPicture("gfx/anims/k-u-drjewo.gif");
        krabat_wino = getPicture("gfx/anims/k-o-wino.gif");

        krabat_stock[0] = getPicture("gfx/anims/k-o-stock1.gif");
        krabat_stock[1] = getPicture("gfx/anims/k-o-stock2.gif");

        krabat_wosusk[0] = getPicture("gfx/anims/k-u-wosusk1.gif");
        krabat_wosusk[1] = getPicture("gfx/anims/k-u-wosusk2.gif");

        krabat_hammer[0] = getPicture("gfx/anims/k-o-hammer1.gif");
        krabat_hammer[1] = getPicture("gfx/anims/k-o-hammer2.gif");

        krabat_fangraben[0] = getPicture("gfx/anims/k-o-takeoben2.gif");
        krabat_fangraben[1] = getPicture("gfx/anims/k-o-takeoben3.gif");

        krabat_bretter[0] = getPicture("gfx/anims/k-u-hammer1.gif");
        krabat_bretter[1] = getPicture("gfx/anims/k-u-hammer2.gif");

        krabat_feuer[0] = getPicture("gfx/anims/k-u-kamj1.gif");
        krabat_feuer[1] = getPicture("gfx/anims/k-u-kamj2.gif");
        krabat_feuer[2] = getPicture("gfx/anims/k-u-kamj3.gif");

        krabat_left_liesbuch_head[0] = getPicture("gfx/anims/k-l-c1.gif");
        krabat_left_liesbuch_head[1] = getPicture("gfx/anims/k-l-c2.gif");
        krabat_left_liesbuch_head[2] = getPicture("gfx/anims/k-l-c3.gif");
        krabat_left_liesbuch_head[3] = getPicture("gfx/anims/k-l-c4.gif");
        krabat_left_liesbuch_head[4] = getPicture("gfx/anims/k-l-c5.gif");

        krabat_right_liesbuch_head[0] = getPicture("gfx/anims/k-r-c1.gif");
        krabat_right_liesbuch_head[1] = getPicture("gfx/anims/k-r-c2.gif");
        krabat_right_liesbuch_head[2] = getPicture("gfx/anims/k-r-c3.gif");
        krabat_right_liesbuch_head[3] = getPicture("gfx/anims/k-r-c4.gif");
        krabat_right_liesbuch_head[4] = getPicture("gfx/anims/k-r-c5.gif");

        krabat_left_liesbuch_body = getPicture("gfx/anims/k-l-bc.gif");
        krabat_right_liesbuch_body = getPicture("gfx/anims/k-r-bc.gif");

        krabat_lies_koraktor[0] = getPicture("gfx/anims/k-r-kc.gif");
        krabat_lies_koraktor[1] = getPicture("gfx/anims/k-r-kc2.gif");
    }


    // Laufen mit Krabat ////////////////////////////////////////////////////////////////

    // Krabat um einen Schritt weitersetzen, Achtung ! Diese Routine wird sowohl im "MousePressed" - Event
    // als auch im "Paint" - Event angesprungen...

    // Diese Routine bleibt unveraendert, unabhaengig davon, wie Krabat gerade aussieht
    @Override
    public synchronized void Move() {
        // System.out.println ("K-Feetpos vorher : " + xps + " " + yps);

        // Wenn kein Laufen gewuenscht, dann auch nicht laufen!
        if (!isWalking && !isWandering) {
            return;
        }

        // Variablen uebernehmen (Threadsynchronisierung)
        horizontal = Thorizontal;
        walkto = new GenericPoint(Twalkto.x, Twalkto.y);
        direction_x = Tdirection_x;
        direction_y = Tdirection_y;

        if (horizontal)
        // Horizontal laufen, es gelten die Images left und right, gehen von 2 bis 11, von oben 1 bis 9
        {
            // Animationsphase weiterschalten,
            anim_pos++;

            if (anim_pos > 11) {
                anim_pos = 2;
            }

            // neuen Punkt ermitteln und setzen
            VerschiebeX();
            xps = txps;
            yps = typs;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkto.x - (int) txps) * direction_x <= 0) {
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
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * direction_y <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                isWalking = false;
                if (!isWandering && clearanimpos) {
                    anim_pos = 0;
                }
            }
        }

        if (!isWalking && !isWandering) {
            // System.out.println("Krabatpos korrigiert!");
            SetKrabatPos(walkto);
            if (clearanimpos) {
                anim_pos = 0;
            }
        }
        // if (anim_pos == 0) 
        //{
        //System.out.println("Animpos reset!");
        //}
        // pos_x = (int) xps;
        // pos_y = (int) yps;

        // System.out.println ("K-Feetpos nachher : " + xps + " " + yps);
    }

    // Horizontal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void VerschiebeX() {
        // System.out.println ("X-Verschiebung !");

        // Verschieberoutine, die fuer normalen Krabat und Krabat in sl. drasta verwendet wird
        // Skalierungsfaktor holen
        float scale = getScale((int) xps, (int) yps);

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
            typs += direction_y * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + direction_x * horiz_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    private float getHorizDist(float scale) {
        float helper;
        if (direction_x == 1) {
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
        // System.out.println ("Y-Verschiebung !");

        // Verschieberoutine, die fuer normalen Krabat und Krabat in sl. drasta verwendet wird
        // Skalierungsfaktor holen
        float scale = getScale((int) xps, (int) yps);

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
            txps += direction_x * (Math.abs(xps - walkto.x) / z);
        }

        // System.out.println ("X-Verschiebeoffset ist : " + (direction_x * (Math.abs (xps - walkto.x) / z)) +
        //                     " bei walkto : " + walkto.x + " " + walkto.y);

        typs = yps + direction_y * vert_dist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    private float getVertDist(float scale) {
        float helper;
        if (direction_y == 1) {
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
        int xricht, yricht;
        boolean horiz = true;

        // System.out.println("Quell : " + pos_x + " " + pos_y + "Ziel : " + walkto.x + " " + walkto.y);

        // System.out.println("X - Lohnen : " + lohnenx + "Y - Lohnen : " + lohneny);

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

        // Hier Default - Routine
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

        // Hier Ueberpruefen, ob es sich auch lohnt, zuerst Scaling holen
        int scale = getScale((int) xps, (int) yps);

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

        // System.out.println ("Lohnen - Variablen x : " + lohnenx + " y : " + lohneny + " bei Scaling : " + scale);

        if (Math.abs(aim.x - (int) xps) < lohnenx &&
                Math.abs(aim.y - (int) yps) < lohneny) {
            isWalking = false;
            System.out.println("Lohnt sich nicht !");
            if (!isWandering && clearanimpos) {
                // System.out.println("Animpos wurde zurueckgesetzt !");
                anim_pos = 0;
            }
            return;
        }

        // AnimationsImage initialisieren, wenn nicht schon in Bewegung

        // normales Laufen
        if (anim_pos < 2) {
            anim_pos = 2;
        }

        // System.out.println("Animpos ist . " + anim_pos);
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


    // Abspielen einer Animation
    @Override
    public void DoAnimation(GenericDrawingContext g) {
        switch (nAnimation) {
            case 2: // Floete spielen
                SpieleFloete(g, nAnimStep++);
                break;

            case 5: // Rohodz spielen
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

            case 6: // Stock an Leine
                if (nAnimStep < 12) {
                    ZieheLeine(g, nAnimStep++);
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

            case 130: // Schild rein in Kupa, dasgleiche wie case 92 !
                if (nAnimStep < 4) {
                    MaleLinksGross(g, krabat_legeschild);
                    if (nAnimStep == 3) {
                        fAnimHelper = true;
                    }
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 143: // schoepfe bloto nach oben
                if (nAnimStep < 9) {
                    switch (nAnimStep++) {
                        case 0:  // erste Animphase
                        case 1:
                        case 2:
                        case 6:
                        case 7:
                        case 8:
                            ZeigeBild(g, krabat_takeblotooben[0], 0);
                            break;
                        case 3:  // zweite Animphase
                        case 4:
                        case 5:
                            ZeigeBild(g, krabat_takeblotooben[1], 0);
                            break;
                    }
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 144: // schoepfe bloto nach unten
                if (nAnimStep < 9) {
                    switch (nAnimStep++) {
                        case 0:  // erste Animphase
                        case 1:
                        case 2:
                        case 6:
                        case 7:
                        case 8:
                            ZeigeBild(g, krabat_takeblotounten[0], 0);
                            break;
                        case 3:  // zweite Animphase
                        case 4:
                        case 5:
                            ZeigeBild(g, krabat_takeblotounten[1], 0);
                            break;
                    }
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 145: // Fallen hat spezielle Eigenschaften
                if (nAnimStep < 10) {
                    nAnimStep++;
                    yps += nAnimStep * 2;
                    if (nAnimStep == 9) {
                        // Hier Ende des Fallens An Location uebermitteln...
                        fAnimHelper = true;
                    }
                    ZeigeBild(g, krabat_fallen, 0);
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
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

            case 149: // Wirf Holz ins Feuer in Kuchnja, extra, weil nachher auf andere Anim umgeschaltet wird
                if (nAnimStep < 2) {
                    ZeigeBild(g, krabat_wirfholz, 0);
                    nAnimStep++;
                } else {
                    StopAnim();
                    nAnimation = 61;
                    ZeigeBild(g, krabat_down_take_mitte[0], 0);
                }
                break;

            case 150: // Fange Raben in Rapak
                if (nAnimStep < 8) {
                    ZeigeBild(g, krabat_fangraben[nAnimStep % 8 < 4 ? 0 : 1], 0);
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 152: // fuelle Friedhelm zu Wilhelm
                if (nAnimStep < 50) {
                    ZeigeBild(g, krabat_wino, 0);
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 153: // entnagele Bretter in Zachod
                if (nAnimStep == 0) {
                    mainFrame.wave.PlayFile("sfx-dd/nagel.wav");
                } /////////////// Sound !!!!!!!!!!!!!!!!
                if (nAnimStep < 8) {
                    ZeigeBild(g, krabat_bretter[nAnimStep % 4 / 2], 0);
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
                LiesBuch(g, 0);
                break;

            case 156: // lies aus Koraktor
                LiesKoraktor(g, nAnimStep++);
                break;

            case 157: // Floete spielen in Most (extralang, ohne Musik im Hintergrund)
                SpieleLeiseFloete(g, nAnimStep++);
                /*else
                    {
                        StopAnim ();
                        drawKrabat (g);
			}*/
                break;

            case 158: // lies aus Koraktor
                LiesKoraktorOhneReden(g, nAnimStep++);
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

                        case 93: // links mitte aufheben, dabei andere hand verdecken
                            zeigen = krabat_left_take_mitte_verdeckt[0];
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

                        case 123: // oben in der mitte (nach hinten sehen) aufheben
                            zeigen = krabat_up_take_oben2[0];
                            break;

                        case 131: // Krosik an Bludnickis
                            zeigen = krabat_gibkrosik;
                            break;

                        case 132: // Casnik an PredMalickow
                            zeigen = krabat_gibcasnik;
                            break;

                        case 133: // Prikaz an PredMalickow
                            zeigen = krabat_gibprikaz;
                            break;

                        case 134: // Tiger an Mato
                            zeigen = krabat_gibtiger;
                            break;

                        case 135: // Tolery an PredWosuskow
                            zeigen = krabat_gibtolery;
                            break;

                        case 136: // Wopismo an PredMalickow
                            zeigen = krabat_gibwop;
                            break;

                        case 148: // Gib Fisch an Haendler in Kulow
                            zeigen = krabat_gibryba;
                            break;

                        default:
                            System.out.println("Falsche Do-Animation verlangt !");
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

    // allgemeine Anzeigeroutine
    private void ZeigeBild(GenericDrawingContext g, GenericImage welches, int tCount) {
        if (tCount == 2) {
            fAnimHelper = true;
        }
        MaleIhn(g, welches);
    }		

    /*
    
    // Schild in Kupa reinlegen
    private void LegeSchild (GenericDrawingContext g, int tCount)
    {
  	if (tCount == 2) fAnimHelper = true;
  	MaleIhn (g, krabat_legeschild);
    }	

    // Krosik an Blud
    private void GibKrosik (GenericDrawingContext g, int tCount)
    {
  	if (tCount == 2) fAnimHelper = true;
  	MaleIhn (g, krabat_gibkrosik);
    }
    
    	*/

    // mit Stock an Leine ziehen
    private void ZieheLeine(GenericDrawingContext g, int tCount) {
        MaleIhn(g, krabat_stock[tCount % 4 / 2]);
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

    // Krabat spielt Floete incl Zoominginformationen hier nur in Most!
    private void SpieleLeiseFloete(GenericDrawingContext g, int tCount) {
        /*if (tCount == 199) 
            {
                // als letztes normal nach vorn sehend hinstellen
                SetFacing (6);
		}*/

        int nFrame = tCount % 8 / 2;
        MaleIhn(g, krabat_floete[nFrame]);
    }

    // Krabat spielt Rohodz incl Zoominginformationen
    private void SpieleRohodz(GenericDrawingContext g, int tCount) {
        // Sound abspielen
        if (tCount == 19) {
            // als letztes normal nach vorn sehend hinstellen
            SetFacing(6);
        }

        int nFrame = tCount % 8 / 2;
        MaleIhn(g, krabat_rohodz[nFrame]);
    }

    // Krabat liest aus dem Buch
    private void LiesBuch(GenericDrawingContext offGraph, int tCount) {
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
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        float fHeight = CHEIGHT;
        float fOffset = BODYOFFSET;
        float fScale = scale;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - fScale) * (fOffset / fHeight));
        int Koerperhoehe = (int) (fHeight - fScale - Kopfhoehe);

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
                offGraph.drawImage(krabat_right_liesbuch_head[LiesHead], left, up, Koerperbreite, Kopfhoehe, null);
                offGraph.drawImage(krabat_right_liesbuch_body, left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
                break;
            case 9: // von links
                offGraph.drawImage(krabat_left_liesbuch_head[LiesHead], left, up, Koerperbreite, Kopfhoehe, null);
                offGraph.drawImage(krabat_left_liesbuch_body, left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
                break;
        }

    }

    // Krabat liest aus dem Buch
    private void LiesKoraktor(GenericDrawingContext offGraph, int tCount) {
        // Groesse und Position der Figur berechnen
        // Update SS: 10 Pixel Offset, da Krabat im Bild weiter links steht
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        float fHeight = CHEIGHT;
        float fOffset = BODYOFFSET;
        float fScale = scale;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - fScale) * (fOffset / fHeight));
        int Koerperhoehe = (int) (fHeight - fScale - Kopfhoehe);

        // Links-Oben-Koordinate verschiebt sich ungezoomt um 10 Pix.
        int Offset = Koerperbreite / 5;
        if (GetFacing() == 9) {
            left -= Offset;
        }
        if (GetFacing() == 3) {
            left += Offset;
        }

        // Clipping-Region setzen
        offGraph.setClip(left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);

        // neues Head-Image berechnen
        if (--Verhinderlieshead < 1) {
            Verhinderlieshead = MAX_VERHINDERLIESHEAD;
            LiesHead = (int) (Math.random() * 4.9);
        }

        int unten = tCount > 10 ? 0 : 1;

        offGraph.drawImage(krabat_right_liesbuch_head[LiesHead], left, up, Koerperbreite, Kopfhoehe, null);
        offGraph.drawImage(krabat_lies_koraktor[unten], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
    }

    // Krabat liest aus dem Buch
    private void LiesKoraktorOhneReden(GenericDrawingContext offGraph, int tCount) {
        // Groesse und Position der Figur berechnen
        // Update SS: 10 Pixel Offset, da Krabat im Bild weiter links steht
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        float fHeight = CHEIGHT;
        float fOffset = BODYOFFSET;
        float fScale = scale;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - fScale) * (fOffset / fHeight));
        int Koerperhoehe = (int) (fHeight - fScale - Kopfhoehe);

        // Links-Oben-Koordinate verschiebt sich ungezoomt um 10 Pix.
        int Offset = Koerperbreite / 5;
        if (GetFacing() == 9) {
            left -= Offset;
        }
        if (GetFacing() == 3) {
            left += Offset;
        }

        // Clipping-Region setzen
        offGraph.setClip(left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);

        offGraph.drawImage(krabat_right_liesbuch_head[0], left, up, Koerperbreite, Kopfhoehe, null);
        offGraph.drawImage(krabat_lies_koraktor[0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
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
        TalkBodyUp = 0;
        TalkBodyDown = 0;

        Rede(offGraph);
    }

    private void Rede(GenericDrawingContext offGraph) {
        // Clipping - Region setzen
        KrabatClip(offGraph, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        float fHeight = CHEIGHT;
        float fOffset = BODYOFFSET;
        float fScale = scale;

        int Koerperbreite = CWIDTH - scale / 2;
        int Kopfhoehe = (int) ((fHeight - fScale) * (fOffset / fHeight));
        int Koerperhoehe = (int) (fHeight - fScale - Kopfhoehe);

        // je nach Richtung malen
        switch (GetFacing()) {
            case 3: // von rechts
                offGraph.drawImage(krabat_right_talk_head[TalkHead], left, up, Koerperbreite, Kopfhoehe, null);
                offGraph.drawImage(krabat_right_talk_body[TalkBody], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
                break;
            case 6: // von vorn
                offGraph.drawImage(krabat_down_talk_head[TalkHeadDown], left, up, Koerperbreite, Kopfhoehe, null);
                offGraph.drawImage(krabat_down_talk_body[TalkBodyDown], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
                break;
            case 9: // von links
                offGraph.drawImage(krabat_left_talk_head[TalkHead], left, up, Koerperbreite, Kopfhoehe, null);
                offGraph.drawImage(krabat_left_talk_body[TalkBody], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
                break;
            case 12: // von hinten
                offGraph.drawImage(krabat_up_talk_head[TalkHeadUp], left, up, Koerperbreite, Kopfhoehe, null);
                offGraph.drawImage(krabat_up_talk_body[TalkBodyUp], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe, null);
                break;
        }
    }

    // Zooming-Variablen berechnen

    private int getLeftPos(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int helper = getScale(pox, poy);
        return pox - (CWIDTH - helper / 2) / 2;
    }

    private int getUpPos(int pox, int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        int helper = getScale(pox, poy);
        return poy - CHEIGHT + helper;
    }

    // fuer Debugging public - wird wieder private !!!
    // wird nur bei Default angesprungen
    @Override
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

    // Clipping - Region vor Zeichnen von Krabat setzen
    private void KrabatClipExtra(GenericDrawingContext g, int xx, int yy, boolean isLeft) {
        // Links - oben - Korrdinaten ermitteln, wie sie eigentlich waeren
        int x = getLeftPos(xx, yy);
        int y = getUpPos(xx, yy);
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd, yd;

        if (isLeft) {
            // nach links Cliprectangle auch nach links vergroessern !!!
            // GenericImage ist in Beide Richtungen gleichgross !!
            yd = yy - y;
            xd = yd;

            // x-Position muss verringert werden !
            x -= xd / 2;
        } else {
            // nach rechts nur Cliprectangle vergroessern
            yd = yy - y;
            xd = yd;
        }

        g.setClip(x, y, xd, yd);

        // Fuer Debugging ClipRectangle zeichnen 
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    @Override
    public Borderrect KrabatRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) xps, (int) yps);
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
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTH - scale / 2, CHEIGHT - scale);
    }

    private void MaleLinksGross(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClipExtra(g, (int) xps, (int) yps, true);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        // hier muss GenericImage noch verschoben werden, da es breiter ist und K ganz rechts steht
        left -= (CHEIGHT - scale) / 2;

        // Figur zeichnen, in x - und y-Richtung gleichgross
        g.drawImage(ktemp, left, up, CHEIGHT - scale, CHEIGHT - scale);
    }

    private void MaleRechtsGross(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        KrabatClipExtra(g, (int) xps, (int) yps, false);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps, (int) yps);
        int up = getUpPos((int) xps, (int) yps);
        int scale = getScale((int) xps, (int) yps);

        // Figur zeichnen, in x - und y-Richtung gleichgross
        g.drawImage(ktemp, left, up, CHEIGHT - scale, CHEIGHT - scale);
    }
}