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

package de.codengine.krabat.locations;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.*;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

import java.util.Objects;

public class Haty1 extends Mainloc {
    private GenericImage background;
    private boolean switchanim = false;

    private final GenericImage[] Boot;
    private int Bootzaehl = 1;
    private boolean vorw = true;
    private int Bootcount = 4;
    private static final int[] ZEIT = {4, 4, 4, 2, 2, 2, 2, 4, 4};

    private Wudzerneu1 angler1;
    private Wudzerneu2 angler2;
    private Bow eimer;
    private Ryby fische;
    private KrabatAngeln kfischer;
    private final Multiple2 Dialog;

    private Reh reh;
    private final boolean clipIstLinks;

    private final GenericImage[] muell;
    private int whichMuell = -1;

    // Laufvariablen fuer die beiden Angler
    private boolean walkWudzer1 = true;
    private boolean walkWudzer2 = true;
    private int Verzoegerung;

    // Antworten des 1. Anglers bei MC
    private static final String[] HWAnt = {Start.stringManager.getTranslation("Loc1_Haty1_00000"), Start.stringManager.getTranslation("Loc1_Haty1_00001"), Start.stringManager.getTranslation("Loc1_Haty1_00002"), Start.stringManager.getTranslation("Loc1_Haty1_00003"), Start.stringManager.getTranslation("Loc1_Haty1_00004")};
    private static final String[] DWAnt = {Start.stringManager.getTranslation("Loc1_Haty1_00005"), Start.stringManager.getTranslation("Loc1_Haty1_00006"), Start.stringManager.getTranslation("Loc1_Haty1_00007"), Start.stringManager.getTranslation("Loc1_Haty1_00008"), Start.stringManager.getTranslation("Loc1_Haty1_00009")};
    private static final String[] NWAnt = {Start.stringManager.getTranslation("Loc1_Haty1_00010"), Start.stringManager.getTranslation("Loc1_Haty1_00011"), Start.stringManager.getTranslation("Loc1_Haty1_00012"), Start.stringManager.getTranslation("Loc1_Haty1_00013"), Start.stringManager.getTranslation("Loc1_Haty1_00014")};

    // Points fuer Dinge in Location
    private static final GenericPoint Prohodzl = new GenericPoint(73, 324);
    private static final GenericPoint Prohodzr = new GenericPoint(534, 341);
    private static final GenericPoint PwudzerLeft = new GenericPoint(83, 335);
    private static final GenericPoint PwudzerRight = new GenericPoint(469, 356);
    private static final GenericPoint Pcolmik = new GenericPoint(474, 356);
    private static final GenericPoint Pup = new GenericPoint(315, 115);
    private static final GenericPoint Pdown = new GenericPoint(230, 480);
    private static final GenericPoint PwodaLeft = new GenericPoint(96, 353);
    private static final GenericPoint PwodaRight = new GenericPoint(543, 370);
    private static final GenericPoint Pmuell = new GenericPoint(170, 349);

    // Points fuer Angler, das hier ist der stehende Angler
    private static final GenericPoint wudzer2RightFeet = new GenericPoint(538, 369); // wo steht der Angler rechts ?
    private static final GenericPoint wudzer2LeftFeet = new GenericPoint(29, 346); // wo steht der Angler links
    private static final GenericPoint wudzer2RightWalk = new GenericPoint(538, 369); // von wo laeuft der Angler los ?
    private static final GenericPoint wudzer2LeftWalk = new GenericPoint(29, 346); // wo laeuft der Angler hin ?

    // Wudzer1 ist der mit Eimer, der sitzt !!!
    private static final GenericPoint wudzer1RightFeet = new GenericPoint(553, 404); // sitzen rechts
    private static final GenericPoint wudzer1LeftFeet = new GenericPoint(7, 383); // sitzen links
    private static final GenericPoint wudzer1RightWalk = new GenericPoint(529, 377); // Ziel des Laufens
    private static final GenericPoint wudzer1LeftWalk = new GenericPoint(28, 350); // Beginn des Laufens
    private static final GenericPoint wudzer1Eimernimm = new GenericPoint(514, 377); // wo wird der Eimer aufgenommen ?
    private static final GenericPoint wudzer1Eimergib = new GenericPoint(79, 356); // wo wird der Eimer wieder hingestellt ?

    // Points fuer Eimer
    private static final GenericPoint eimerLinks = new GenericPoint(60, 331);
    private static final GenericPoint eimerRechts = new GenericPoint(495, 350);
    private GenericPoint eimerPos;
    private Borderrect eimerRect;

    // Konstanten - Rects deklarieren
    private static final Borderrect untererAusgang = new Borderrect(176, 426, 286, 479);
    private static final Borderrect obererAusgang = new Borderrect(294, 90, 340, 125);
    private static final Borderrect rohodzLeft = new Borderrect(35, 222, 144, 265);
    private static final Borderrect rohodzRight = new Borderrect(561, 203, 639, 260);
    private static final Borderrect colmik = new Borderrect(410, 384, 546, 410);
    private static final Borderrect muellRect = new Borderrect(77, 322, 147, 357);

    // Konstante Trapeze
    // Wasser rechts
    private static final Bordertrapez wodaRight1 = new Bordertrapez(364, 261, 639, 327);
    private static final Bordertrapez wodaRight2 = new Bordertrapez(550, 639, 592, 639, 328, 416);
    private static final Bordertrapez wodaRight3 = new Bordertrapez(366, 639, 400, 639, 417, 479);

    // Wasser links
    private static final Bordertrapez wodaLeft1 = new Bordertrapez(0, 267, 189, 310);
    private static final Bordertrapez wodaLeft2 = new Bordertrapez(0, 125, 0, 1, 384, 479);

    // Konstante ints
    private static final int fRohodzLeft = 12;
    private static final int fRohodzRight = 12;
    private static final int fBowLeft = 9;
    private static final int fBowRight = 3;
    private static final int fBoot = 6;
    private static final int fWodaLeft = 9;
    private static final int fWodaRight = 3;
    private static final int fWudzerjoLeft = 9;
    private static final int fWudzerjoRight = 3;
    private static final int fMuell = 9;

    // fuers Blinkern
    private static final Bordertrapez[] Blink =
            {new Bordertrapez(25, 26, 0, 26, 205, 229),
                    new Bordertrapez(0, 230, 38, 268),
                    new Bordertrapez(39, 41, 39, 153, 259, 268),
                    new Bordertrapez(157, 167, 146, 191, 239, 268),
                    new Bordertrapez(0, 269, 190, 311),
                    new Bordertrapez(59, 189, 181, 182, 312, 323),
                    new Bordertrapez(0, 28, 0, 1, 312, 333),
                    new Bordertrapez(0, 125, 0, 3, 399, 479),
                    new Bordertrapez(0, 378, 126, 398),
                    new Bordertrapez(0, 352, 14, 377),
                    new Bordertrapez(28, 362, 133, 377),
                    new Bordertrapez(30, 31, 28, 133, 353, 361),
                    new Bordertrapez(365, 398, 639, 443),
                    new Bordertrapez(363, 639, 406, 639, 444, 479),
                    new Bordertrapez(373, 373, 402, 397),
                    new Bordertrapez(417, 379, 535, 397),
                    new Bordertrapez(541, 639, 579, 639, 326, 397),
                    new Bordertrapez(350, 639, 369, 639, 270, 325),
                    new Bordertrapez(370, 549, 350, 639, 242, 269),
                    new Bordertrapez(474, 552, 370, 549, 214, 241)};

    private int[][][] MerkArray;
    private static final int HAEUFIGKEITSKONSTANTE = 1000;
    private static final int ANIMZUSTAENDE = 4;

    // Variablen fuer Extra-Anims
    private int Sonderstatus = 0;
    private boolean schautAnglerInDerGegendRum = false;
    private boolean stehenderAnglerSchautSichUm = false;

    private boolean hatAnglerDenEimer = false; // Anzeige, ob Eimer extra gezeichnet wird
    private boolean anglerNimmtEimer = false;  // Flag fuer die Anglerklasse
    private boolean merkeEimer = false;        // Flag fuer die Anglerklasse zur Uebernahme

    private boolean schnauzeFische = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Haty1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 325;
        mainFrame.krabat.zoomf = 2.99f;
        mainFrame.krabat.defScale = 0;  // genau zum Angeln muss es 0 sein -> testen

        Boot = new GenericImage[9];
        muell = new GenericImage[4];

        angler1 = new Wudzerneu1(mainFrame);
        angler2 = new Wudzerneu2(mainFrame);
        eimer = new Bow(mainFrame);
        fische = new Ryby(mainFrame);
        Dialog = new Multiple2(mainFrame);
        kfischer = new KrabatAngeln(mainFrame);

        int zf = (int) Math.round(Math.random() * 50);
        if (zf > 25) {
            reh = new Reh(mainFrame, false, new GenericRectangle(215, 128, 33, 25), 103);
            clipIstLinks = true;
        } else {
            reh = new Reh(mainFrame, false, new GenericRectangle(460, 120, 40, 25), 103);
            clipIstLinks = false;
        }

        InitLocation(oldLocation);

        // fuer Blinkern rein
        InitBlinker();

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();

        // wenn kein Load, dann die Moeglichkeit, dass mehr Muell da ist, berechnen
        if (oldLocation != 0 && mainFrame.Actions[220]) {
            // angler muessen auch links sein
            // Actions von 152 bis 155

            // Zufallszahl erzeugen, die unseren Bedingungen nuetzlich ist
            int zz = (int) (Math.random() * 100);

            if (!mainFrame.Actions[152])  // hier wird die Muellgroesse evaluiert
            {
                // Stufe 1
                if (zz > 70) {
                    mainFrame.Actions[152] = true;
                    whichMuell = 0;
                }
            } else {
                whichMuell = 0;
                if (!mainFrame.Actions[153]) {
                    // Stufe 2
                    if (zz > 80) {
                        mainFrame.Actions[153] = true;
                        whichMuell = 1;
                    }

                } else {
                    whichMuell = 1;
                    if (!mainFrame.Actions[154]) {
                        // Stufe 3
                        if (zz > 90) {
                            mainFrame.Actions[154] = true;
                            whichMuell = 2;
                        }
                    } else {
                        whichMuell = 2;
                        if (!mainFrame.Actions[155]) {
                            // Stufe 4
                            if (zz > 93) {
                                mainFrame.Actions[155] = true;
                                whichMuell = 3;
                            }
                        } else {
                            whichMuell = 3;
                        }
                    }
                }
            }
        }


        InitAngler();
        InitEimer();

        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 5:
                // Von Les1 aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(232, 463));
                mainFrame.krabat.SetFacing(12);
                break;
            case 8:
                // von Rapak aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(318, 120));
                mainFrame.krabat.SetFacing(6);
                break;
        }

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/haty/haty2.gif");

        Boot[1] = getPicture("gfx/haty/bt3.gif");
        Boot[2] = getPicture("gfx/haty/bt4.gif");
        Boot[3] = getPicture("gfx/haty/bt5.gif");
        Boot[4] = getPicture("gfx/haty/bt6.gif");
        Boot[5] = getPicture("gfx/haty/bt7.gif");
        Boot[6] = getPicture("gfx/haty/bt8.gif");
        Boot[7] = getPicture("gfx/haty/bt9.gif");
        Boot[8] = getPicture("gfx/haty/bt10.gif");

        muell[0] = getPicture("gfx/haty/muell1.gif");
        muell[1] = getPicture("gfx/haty/muell2.gif");
        muell[2] = getPicture("gfx/haty/muell3.gif");
        muell[3] = getPicture("gfx/haty/muell4.gif");

    }

    // Anglerpositionen definieren
    private void InitAngler() {
        // Hier berechnen, auf welcher Seite die netten Angler stehen...
        if (!mainFrame.Actions[220]) {
            // stehen rechts
            angler1.SetWudzer1Pos(wudzer1RightFeet);
            angler1.SetFacing(3);

            angler2.SetWudzer2Pos(wudzer2RightFeet);
            angler2.SetFacing(3);

            // Grenzen setzen
            mainFrame.wegGeher.vBorders.removeAllElements();
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(310, 318, 294, 313, 115, 222));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(294, 313, 238, 271, 223, 336));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(38, 93, 16, 93, 326, 340));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(94, 332, 168, 350));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(169, 337, 216, 360));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(217, 337, 264, 407));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(265, 337, 361, 362));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(362, 342, 458, 363));
            // mainFrame.wegGeher.vBorders.addElement (new bordertrapez (459, 525, 459, 542, 345, 364));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(208, 408, 256, 479));

            // Matrix loeschen
            mainFrame.wegSucher.ClearMatrix(9);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 5);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(4, 5);
            mainFrame.wegSucher.PosVerbinden(5, 6);
            mainFrame.wegSucher.PosVerbinden(6, 7);
            mainFrame.wegSucher.PosVerbinden(5, 8);

        } else {
            // stehen links
            angler1.SetWudzer1Pos(wudzer1LeftFeet);
            angler1.SetFacing(9);

            angler2.SetWudzer2Pos(wudzer2LeftFeet);
            angler2.SetFacing(9);

            // Grenzen setzen
            mainFrame.wegGeher.vBorders.removeAllElements();
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(310, 318, 294, 313, 115, 222));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(294, 313, 238, 271, 223, 336));
            // mainFrame.wegGeher.vBorders.addElement (new bordertrapez ( 38,  93,  16,  93, 326, 340));

            // dieses nur solange, wie kein Muell zu sehen ist
            if (!mainFrame.Actions[152]) {
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(94, 332, 168, 350));
            }
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(169, 337, 216, 360));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(217, 337, 264, 407));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(265, 337, 361, 362));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(362, 342, 458, 363));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(459, 525, 459, 542, 345, 364));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(208, 408, 256, 479));

            // Matrix loeschen, je nachdem, wie gross noch ist
            mainFrame.wegSucher.ClearMatrix(mainFrame.Actions[152] ? 8 : 9);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            if (!mainFrame.Actions[152]) {
                mainFrame.wegSucher.PosVerbinden(0, 1);
                mainFrame.wegSucher.PosVerbinden(1, 4);
                mainFrame.wegSucher.PosVerbinden(2, 3);
                mainFrame.wegSucher.PosVerbinden(3, 4);
                mainFrame.wegSucher.PosVerbinden(4, 5);
                mainFrame.wegSucher.PosVerbinden(5, 6);
                mainFrame.wegSucher.PosVerbinden(6, 7);
                mainFrame.wegSucher.PosVerbinden(4, 8);
            } else {
                mainFrame.wegSucher.PosVerbinden(0, 1);
                mainFrame.wegSucher.PosVerbinden(1, 3);
                mainFrame.wegSucher.PosVerbinden(2, 3);
                mainFrame.wegSucher.PosVerbinden(3, 4);
                mainFrame.wegSucher.PosVerbinden(4, 5);
                mainFrame.wegSucher.PosVerbinden(5, 6);
                mainFrame.wegSucher.PosVerbinden(3, 7);
            }
        }
    }

    private void InitEimer() {
        // Hier berechnen, auf welcher Seite der Eimer steht...
        eimerPos = !mainFrame.Actions[220] ? eimerRechts : eimerLinks;
        eimerRect = new Borderrect(eimerPos.x, eimerPos.y, eimerPos.x + Bow.Breite, eimerPos.y + Bow.Hoehe);
    }

    private void InitBlinker() {
        // hier wird das Blinkern festgelegt, indem das Array initialisiert wird, wo der
        // Blinkstatus gespeichert wird

        // So viele Borderrects
        int AnzahlRects = Blink.length;

        // So viele Sachen sind zu merken
        int AnzahlMerksachen = 3;

        // So viele Striche sollen in den borderrects erscheinen
        int AnzahlStriche = 1;

        for (Bordertrapez bordertrapez : Blink) {
            if (bordertrapez.Flaeche() / HAEUFIGKEITSKONSTANTE > AnzahlStriche) {
                AnzahlStriche = bordertrapez.Flaeche() / HAEUFIGKEITSKONSTANTE;
            }
        }

        // Groesse des Arrays steht fest, also Init
        MerkArray = new int[AnzahlRects][AnzahlStriche][AnzahlMerksachen];

        // Jetzt das Array initialisieren und beschraenken, da nicht alle borderrects so viele Striche haben wie das Groesste
        for (int i = 0; i < MerkArray.length; i++) {
            for (int j = 0; j < MerkArray[i].length; j++) {
                // mit -1 kennzeichnen, das dieser Eintrag nicht beachtet werden soll
                if (Blink[i].Flaeche() / HAEUFIGKEITSKONSTANTE < j && j > 0) {
                    MerkArray[i][j][2] = -1;
                } else {
                    // gewisse Anfangszufaelligkeit zuweisen, damit nicht alle im selben Status
                    int zuffZahl = 0;

                    do {
                        zuffZahl = (int) Math.round(Math.random() * 7);
                    }
                    while (zuffZahl > ANIMZUSTAENDE);

                    MerkArray[i][j][2] = zuffZahl;
                }

                // x - und y - Koordinate = 0 -> Platz ist frei
                MerkArray[i][j][0] = 0;
                MerkArray[i][j][1] = 0;
            }
        }
    }

    @Override
    public void cleanup() {
        background = null;

        Boot[1] = null;
        Boot[2] = null;
        Boot[3] = null;
        Boot[4] = null;
        Boot[5] = null;
        Boot[6] = null;
        Boot[7] = null;
        Boot[8] = null;

        muell[0] = null;
        muell[1] = null;
        muell[2] = null;
        muell[3] = null;

        angler1.cleanup();
        angler1 = null;
        angler2.cleanup();
        angler2 = null;
        eimer.cleanup();
        eimer = null;
        fische.cleanup();
        fische = null;
        kfischer.cleanup();
        kfischer = null;
        reh.cleanup();
        reh = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        if (mainFrame.isMultiple && mainFrame.Clipset) {
            Dialog.paintMultiple(g);
            return;
        }

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.isAnim = true;
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Rehe Hintergrund loeschen (Extrawurst ggue. allen anderen Anims hier)
        if (clipIstLinks) {
            g.setClip(200, 80, 100, 100);
        } else {
            g.setClip(460, 80, 100, 100);
        }
        g.drawImage(background, 0, 0);

        // Rehe zeichnen
        reh.drawReh(g);

        // Blinkern ermoeglichen, dieses ist am Hintergruendigsten
        g.setClip(0, 181, 639, 298);  // dieses Clipping muss die kompletten Anims loeschen, alles inclusive !!!
        g.drawImage(background, 0, 0);
        Blink(g);

        // jetzt ist ALLES !!! Hintergrundanimmaessiges geloescht, also keine extra Clipsets und loeschen

        // hier testweise den Muell rein
        if (whichMuell > -1) {
            g.drawImage(muell[whichMuell], 77, 287);
        }

        // Boot ist im Hintergrund, aber vor Blinkern
        if (mainFrame.isAnim) {
            // Wellenbild berechnen und anzeigen
            switchanim = !switchanim;
            if (!switchanim && --Bootcount < 1) {
                if (vorw) {
                    Bootzaehl++;
                    if (Bootzaehl == 9) {
                        Bootzaehl = 7;
                        vorw = false;
                    }
                } else {
                    Bootzaehl--;
                    if (Bootzaehl == 0) {
                        Bootzaehl = 2;
                        vorw = true;
                    }
                }
                Bootcount = ZEIT[Bootzaehl];
            }
            g.drawImage(Boot[Bootzaehl], 376, 367);
        }

        // Fische im Hintergrund, aber vor boot
        fische.drawRyby(g, schnauzeFische);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        if (TalkPerson == 31) {
            // angler 2 redet
            angler2.talkWudzer2(g);
        } else {
            angler2.drawWudzer2(g, stehenderAnglerSchautSichUm);
        }

        // Andere Personen zeichnen
        if (!walkWudzer2) {
            walkWudzer2 = angler2.Move();
        }

        if (anglerNimmtEimer) {
            angler1.takeBow(g, merkeEimer);
        } else {
            if (TalkPerson == 30 || schautAnglerInDerGegendRum) {
                // angler 1 redet
                angler1.talkWudzer1(g, schautAnglerInDerGegendRum);
            } else {
                angler1.drawWudzer1(g);
            }
        }

        // Angler 1 ist vor Angler 2
        if (!walkWudzer1) {
            walkWudzer1 = angler1.Move();
        }

        // Krabat bewegen
        mainFrame.wegGeher.GeheWeg();

        // Krabat zeichnen

        if (Sonderstatus != 0) {
            boolean nochaktiv = true;

            GenericPoint tPoint = mainFrame.krabat.GetKrabatPos();

            switch (Sonderstatus) {
                case 1: // links angeln ohne Erfolg
                    g.setClip(0, 230, 200, 200);
                    nochaktiv = kfischer.AngleLinks(g, tPoint);
                    break;

                case 2: // rechts angeln ohne Erfolg
                    g.setClip(500, 250, 140, 200);
                    nochaktiv = kfischer.AngleRechts(g, tPoint);
                    break;

                case 3: // links angeln mit Holzfisch
                    g.setClip(0, 230, 200, 200);
                    nochaktiv = kfischer.FangeLinks(g, tPoint);
                    break;

                case 4: // rechts Angeln mit Erfolg
                    g.setClip(500, 250, 140, 200);
                    nochaktiv = kfischer.FangeRechts(g, tPoint);
                    break;

                case 5: // links reden mit Holzfisch
                    g.setClip(0, 230, 200, 200);
                    kfischer.RedeLinks(g, tPoint, TalkPerson);
                    break;

                default: // Fehler !!
                    System.out.println("Falsche Sonderanim !!!");
                    nochaktiv = false;
                    break;
            }

            // Abschalten, wenn fertig
            if (!nochaktiv) {
                Sonderstatus = 0;
            }
        } else {
            // Animation??
            if (mainFrame.krabat.nAnimation != 0) {
                mainFrame.krabat.DoAnimation(g);

                // Cursorruecksetzung nach Animationsende
                if (mainFrame.krabat.nAnimation == 0) {
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                }
            } else {
                if (mainFrame.talkCount > 0 && TalkPerson != 0) {
                    // beim Reden
                    switch (TalkPerson) {
                        case 1:
                            // Krabat spricht gestikulierend
                            mainFrame.krabat.talkKrabat(g);
                            break;
                        case 3:
                            // Krabat spricht im Monolog
                            mainFrame.krabat.describeKrabat(g);
                            break;
                        default:
                            // steht Krabat nur da
                            mainFrame.krabat.drawKrabat(g);
                            break;
                    }
                }
                // Rumstehen oder Laufen
                else {
                    mainFrame.krabat.drawKrabat(g);
                }
            }
        }

        // Eimer steht immer vor Krabat, deshalb hier zeichnen, wenn er denn da ist
        if (!hatAnglerDenEimer) {
            GenericRectangle myi;
            myi = g.getClipBounds();
            g.setClip(eimerPos.x, eimerPos.y, Bow.Breite, Bow.Hoehe);
            eimer.drawBow(g, eimerPos);
            g.setClip(myi.getX(), myi.getY(), myi.getWidth(), myi.getHeight());
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // bei Multiple Choice extra Mouseroutine
        if (mainFrame.isMultiple) {
            Dialog.evalMouseEvent(e);
            return;
        }

        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // linker Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Rohodz left
                if (rohodzLeft.IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp) &&
                        !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                        case 7: // kij + lajna
                        case 9: // wuda + hocka
                        case 10: // wuda + wacki
                            nextActionID = 300;
                            break;
                        case 6: // Lajna
                            nextActionID = 310;
                            break;
                        case 17: // rohodz
                            nextActionID = 320;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = Prohodzl;
                }

                // Ausreden fuer Rohodz right
                if (rohodzRight.IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp) &&
                        !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                        case 7: // kij + lajna
                        case 9: // wuda + hocka
                        case 10: // wuda + wacki
                            nextActionID = 305;
                            break;
                        case 6: // Lajna
                            nextActionID = 315;
                            break;
                        case 17: // rohodz
                            nextActionID = 325;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Prohodzr;
                }

                // Ausreden fuer Eimer
                if (eimerRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 8: // wacki
                            nextActionID = 330;
                            break;
                        case 13: // dryba
                            nextActionID = 340;
                            break;
                        case 14: // ryba
                            nextActionID = 350;
                            break;
                        default:
                            nextActionID = 160;
                            break;
                    }
                    pTemp = eimerPos;
                }

                // Ausreden fuer Muell
                if (muellRect.IsPointInRect(pTemp) && mainFrame.Actions[152]) {
                    nextActionID = 185;
                    pTemp = Pmuell;
                }

                // Ausreden fuer colmik
                if (colmik.IsPointInRect(pTemp)) {
                    // lajna
                    nextActionID = mainFrame.whatItem == 6 ? 360 : 165;
                    pTemp = Pcolmik;
                }

                // Ausreden fuer Wasser links
                if ((wodaLeft1.PointInside(pTemp) || wodaLeft2.PointInside(pTemp)) &&
                        !angler1.Wudzer1Rect().IsPointInRect(pTemp) && !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 7: // kij + lajna
                            nextActionID = 370;
                            break;
                        case 9: // wuda + hocka
                            nextActionID = 380;
                            break;
                        case 10: // wuda + wacka
                            if (!mainFrame.Actions[220]) {
                                nextActionID = 1240;
                            } else {
                                nextActionID = 1200;
                            }
                            break;
                        case 11: // wuda + dryba
                            if (!mainFrame.Actions[220]) {
                                nextActionID = 200;
                            } else {
                                nextActionID = 1200;
                            }
                            break;
                        case 14: // ryba
                            nextActionID = 390;
                            break;
                        default:
                            nextActionID = 170;
                            break;
                    }
                    pTemp = PwodaLeft;
                }

                // Ausreden fuer Wasser rechts
                if ((wodaRight1.PointInside(pTemp) || wodaRight2.PointInside(pTemp) || wodaRight3.PointInside(pTemp)) &&
                        !angler1.Wudzer1Rect().IsPointInRect(pTemp) && !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 7: // kij + lajna
                            nextActionID = 375;
                            break;
                        case 9: // wuda + hocka
                            nextActionID = 385;
                            break;
                        case 10: // wuda + wacka
                            if (!mainFrame.Actions[220]) {
                                nextActionID = 1210;
                            } else {
                                if (!mainFrame.Actions[914] && !mainFrame.Actions[915]) {
                                    nextActionID = 1220;
                                } else {
                                    nextActionID = 1230;
                                }
                            }
                            break;
                        case 11: // wuda + dryba
                            if (!mainFrame.Actions[220]) {
                                nextActionID = 1210;
                            } else {
                                nextActionID = 1250;
                            }
                            break;
                        case 14: // ryba
                            nextActionID = 395;
                            break;
                        default:
                            nextActionID = 175;
                            break;
                    }
                    pTemp = PwodaRight;
                }

                // Ausreden fuer Angler
                if (angler1.Wudzer1Rect().IsPointInRect(pTemp) || angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                        case 7: // kij + lajna
                            nextActionID = 400;
                            break;
                        case 5: // hocka
                            nextActionID = 410;
                            break;
                        case 8: // wacki
                            nextActionID = 420;
                            break;
                        case 9: // wuda + hocka
                        case 10: // wuda + wacki
                            nextActionID = 430;
                            break;
                        case 11: // wuda + dryba
                            nextActionID = 440;
                            break;
                        case 13: // dryba
                            nextActionID = 450;
                            break;
                        case 14: // ryba
                            nextActionID = 470;
                            break;
                        case 15: // krosik
                            nextActionID = 480;
                            break;
                        case 18: // bron
                            nextActionID = 490;
                            break;
                        default:
                            nextActionID = 180;
                            break;
                    }
                    if (!mainFrame.Actions[220]) {
                        pTemp = PwudzerRight;
                    } else {
                        pTemp = PwudzerLeft;
                    }
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.invCursor = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Les1 gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Rapak gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Rohodz left anschauen
                if (rohodzLeft.IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp) &&
                        !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Prohodzl;
                }

                // Rohodz right anschauen
                if (rohodzRight.IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp) &&
                        !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Prohodzr;
                }

                // Eimer anschauen
                if (eimerRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = eimerPos;
                }

                if (muellRect.IsPointInRect(pTemp) && mainFrame.Actions[152]) {
                    nextActionID = 10;
                    pTemp = Pmuell;
                }

                // colmik anschauen
                if (colmik.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Pcolmik;
                }

                // Wasser links anschauen
                if ((wodaLeft1.PointInside(pTemp) || wodaLeft2.PointInside(pTemp)) &&
                        !angler1.Wudzer1Rect().IsPointInRect(pTemp) && !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = PwodaLeft;
                }

                // Wasser rechts anschauen
                if ((wodaRight1.PointInside(pTemp) || wodaRight2.PointInside(pTemp) || wodaRight3.PointInside(pTemp)) &&
                        !angler1.Wudzer1Rect().IsPointInRect(pTemp) && !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = PwodaRight;
                }

                // Angler anschauen
                if (angler1.Wudzer1Rect().IsPointInRect(pTemp) || angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    if (!mainFrame.Actions[220]) {
                        nextActionID = 8;
                        pTemp = PwudzerRight;
                    } else {
                        pTemp = PwudzerLeft;
                        nextActionID = 7;
                    }
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Les1 Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Rapak anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Rohodz left mitnehmen
                if (rohodzLeft.IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp) &&
                        !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Prohodzl);
                    mainFrame.repaint();
                    return;
                }

                // Rohodz right mitnehmen
                if (rohodzRight.IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp) &&
                        !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Prohodzr);
                    mainFrame.repaint();
                    return;
                }

                // Eimer mitnehmen
                if (eimerRect.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(eimerPos);
                    mainFrame.repaint();
                    return;
                }

                // Muell mitnehmen
                if (muellRect.IsPointInRect(pTemp) && mainFrame.Actions[152]) {
                    nextActionID = 90;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pmuell);
                    mainFrame.repaint();
                    return;
                }

                // colmik use
                if (colmik.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pcolmik);
                    mainFrame.repaint();
                    return;
                }

                // Wasser links use
                if ((wodaLeft1.PointInside(pTemp) || wodaLeft2.PointInside(pTemp)) &&
                        !angler1.Wudzer1Rect().IsPointInRect(pTemp) && !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(PwodaLeft);
                    mainFrame.repaint();
                    return;
                }

                // Wasser rechts use
                if ((wodaRight1.PointInside(pTemp) || wodaRight2.PointInside(pTemp) || wodaRight3.PointInside(pTemp)) &&
                        !angler1.Wudzer1Rect().IsPointInRect(pTemp) && !angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.wegGeher.SetzeNeuenWeg(PwodaRight);
                    mainFrame.repaint();
                    return;
                }

                // Angler talk
                if (angler1.Wudzer1Rect().IsPointInRect(pTemp) || angler2.Wudzer2Rect().IsPointInRect(pTemp)) {
                    if (mainFrame.Actions[220]) {
                        nextActionID = 80;
                        mainFrame.wegGeher.SetzeNeuenWeg(PwudzerLeft);
                    } else {
                        if (angler2.Wudzer2Rect().IsPointInRect(pTemp) && !angler1.Wudzer1Rect().IsPointInRect(pTemp)) {
                            nextActionID = 85;
                        } else {
                            nextActionID = 87;
                        }
                        mainFrame.wegGeher.SetzeNeuenWeg(PwudzerRight);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

        // Krabat - Animation = transparenter Cursor
        if (mainFrame.fPlayAnim || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    wodaRight1.PointInside(pTemp) || wodaRight2.PointInside(pTemp) ||
                    wodaRight3.PointInside(pTemp) || wodaLeft1.PointInside(pTemp) ||
                    wodaLeft2.PointInside(pTemp) || rohodzLeft.IsPointInRect(pTemp) ||
                    rohodzRight.IsPointInRect(pTemp) || angler1.Wudzer1Rect().IsPointInRect(pTemp) ||
                    angler2.Wudzer2Rect().IsPointInRect(pTemp) || colmik.IsPointInRect(pTemp) ||
                    eimerRect.IsPointInRect(pTemp) ||
                    muellRect.IsPointInRect(pTemp) && mainFrame.Actions[152];

            if (Cursorform != 10 && !mainFrame.invHighCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if (Cursorform != 11 && mainFrame.invHighCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }


        // normaler Cursor, normale Reaktion
        else {
            if (wodaRight1.PointInside(pTemp) || wodaRight2.PointInside(pTemp) ||
                    wodaRight3.PointInside(pTemp) || wodaLeft1.PointInside(pTemp) ||
                    wodaLeft2.PointInside(pTemp) || rohodzLeft.IsPointInRect(pTemp) ||
                    rohodzRight.IsPointInRect(pTemp) || angler1.Wudzer1Rect().IsPointInRect(pTemp) ||
                    angler2.Wudzer2Rect().IsPointInRect(pTemp) || colmik.IsPointInRect(pTemp) ||
                    eimerRect.IsPointInRect(pTemp) ||
                    muellRect.IsPointInRect(pTemp) && mainFrame.Actions[152]) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 5;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultiple) {
            return;
        }

        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim) {
            return;
        }

        // Bei Krabat - Animation keine Keys
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();

        // Hauptmenue aktivieren
        if (Taste == GenericKeyEvent.VK_F1) {
            Keyclear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            Keyclear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            Keyclear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void Keyclear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
        mainFrame.krabat.StopWalking();
    }

    private void Blink(GenericDrawingContext g) {
        g.setColor(GenericColor.white);

        // Das Array Stueck fuer Stueck abarbeiten
        for (int i = 0; i < MerkArray.length; i++) {
            for (int j = 0; j < MerkArray[i].length; j++) {
                // nur bearbeiten, falls der Eintrag nicht gesperrt ist
                if (MerkArray[i][j][2] > -1) {
                    // ein leeres Feld bekommt einen neuen Eintrag zugewiesen
                    if (MerkArray[i][j][0] == 0 && MerkArray[i][j][1] == 0) {
                        // gewisse Haeufigkeit fuer Neubelegung festlegen
                        int zuffZahl = (int) Math.round(Math.random() * 50);
                        if (zuffZahl > 25) {
                            // Werte fuer Zufallsgenerator berechnen
                            int xlaenge = (Blink[i].x2 > Blink[i].x4 ? Blink[i].x2 : Blink[i].x4)
                                    - (Blink[i].x1 < Blink[i].x3 ? Blink[i].x1 : Blink[i].x3);
                            int ylaenge = Blink[i].y2 - Blink[i].y1;
                            int xoffset = Blink[i].x1 < Blink[i].x3 ? Blink[i].x1 : Blink[i].x3;

                            // ungueltige Werte nicht beachten
                            do {
                                MerkArray[i][j][0] = (int) Math.round(Math.random() * xlaenge) + xoffset;
                                MerkArray[i][j][1] = (int) Math.round(Math.random() * ylaenge) + Blink[i].y1;
                            }
                            while (!Blink[i].PointInside(new GenericPoint(MerkArray[i][j][0], MerkArray[i][j][1])));
                        }
                    }

                    // wenn der Eintrag gueltig ist, dann auch zeichnen
                    if (MerkArray[i][j][0] != 0 && MerkArray[i][j][1] != 0) {
                        // den Eintrag zeichnen
                        switch (MerkArray[i][j][2]) {
                            case 0: // Ein Punkt
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0], MerkArray[i][j][1]);
                                break;

                            case 1: // Zwei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 1, MerkArray[i][j][1]);
                                break;

                            case 2: // Drei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 2, MerkArray[i][j][1]);
                                break;

                            case 3: // Zwei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 1, MerkArray[i][j][1]);
                                break;

                            case 4: // Ein Punkt
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0], MerkArray[i][j][1]);
                                break;

                            default:
                                System.out.println("Fehler in Blinkerroutine !!!");

                        }

                        // Status weiterzaehlen
                        MerkArray[i][j][2]++;

                        // wenn zuende, dann loeschen
                        if (MerkArray[i][j][2] > ANIMZUSTAENDE) {
                            MerkArray[i][j][0] = 0;
                            MerkArray[i][j][1] = 0;
                            MerkArray[i][j][2] = 0;
                        }
                    }
                }
            }
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.Mousepoint);

            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Schilf links ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00015"), Start.stringManager.getTranslation("Loc1_Haty1_00016"), Start.stringManager.getTranslation("Loc1_Haty1_00017"),
                        fRohodzLeft, 3, 0, 0);
                break;

            case 2:
                // Schilf rechts ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00018"), Start.stringManager.getTranslation("Loc1_Haty1_00019"), Start.stringManager.getTranslation("Loc1_Haty1_00020"),
                        fRohodzRight, 3, 0, 0);
                break;

            case 3:
                // Bow z rybami ansehen
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00021"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00022"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00023"),
                            fBowRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00024"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00025"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00026"),
                            fBowLeft, 3, 0, 0);
                }
                break;

            case 4:
                // Boot ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00027"), Start.stringManager.getTranslation("Loc1_Haty1_00028"), Start.stringManager.getTranslation("Loc1_Haty1_00029"),
                        fBoot, 3, 0, 0);
                break;

            case 5:
                // Wasser links ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00030"), Start.stringManager.getTranslation("Loc1_Haty1_00031"), Start.stringManager.getTranslation("Loc1_Haty1_00032"),
                        fWodaLeft, 3, 0, 0);
                break;

            case 6:
                // Wasser rechts ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00033"), Start.stringManager.getTranslation("Loc1_Haty1_00034"), Start.stringManager.getTranslation("Loc1_Haty1_00035"),
                        fWodaRight, 3, 0, 0);
                break;

            case 7:
                // Angler ansehen, wenn links und fangen nix
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00036"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00037"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00038"),
                        fWudzerjoLeft, 3, 0, 0);
                break;

            case 8:
                // Angler ansehen, wenn rechts und erfolgreich...
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00039"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00040"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00041"),
                        fWudzerjoRight, 3, 0, 0);
                break;

            case 10:
                // Muell anschauen, sobald er da ist
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00042"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00043"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00044"),
                        fMuell, 3, 0, 0);
                break;

            case 50:
                // Schilf links mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00045"), Start.stringManager.getTranslation("Loc1_Haty1_00046"), Start.stringManager.getTranslation("Loc1_Haty1_00047"),
                        fRohodzLeft, 3, 0, 0);
                break;

            case 55:
                // Schilf rechts mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00048"), Start.stringManager.getTranslation("Loc1_Haty1_00049"), Start.stringManager.getTranslation("Loc1_Haty1_00050"),
                        fRohodzRight, 3, 0, 0);
                break;

            case 60:
                // Bow mitnehmen
                // Zufallszahl 0...2
                int zuffZahl = (int) (Math.random() * 2.9);
                switch (zuffZahl) {
                    case 0:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00051"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00052"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00053"));
                        }
                        break;

                    case 1:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00054"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00055"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00056"));
                        }
                        break;

                    case 2:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00057"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00058"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00059"));
                        }
                        break;
                }
                outputTextPos = mainFrame.ifont.CenterText(outputText, angler2.Wudzer2TalkPoint());
                if (!mainFrame.Actions[220]) {
                    mainFrame.krabat.SetFacing(3);
                } else {
                    mainFrame.krabat.SetFacing(9);
                }
                TalkPerson = 31;
                nextActionID = 0;
                break;

            case 65:
                // Colmik mitnehmen
                // Zufallszahl 0...1, erstes Mal immer 0
                int zuffZahl2 = (int) (Math.random() * 1.9);
                if (!mainFrame.Actions[151]) {
                    mainFrame.Actions[151] = true;
                    zuffZahl2 = 0;
                }
                switch (zuffZahl2) {
                    case 0:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00060"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00061"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00062"));
                        }
                        break;

                    case 1:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00063"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00064"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00065"));
                        }
                        break;
                }
                outputTextPos = mainFrame.ifont.CenterText(outputText, angler2.Wudzer2TalkPoint());
                if (!mainFrame.Actions[220]) {
                    mainFrame.krabat.SetFacing(3);
                } else {
                    mainFrame.krabat.SetFacing(9);
                }
                TalkPerson = 31;
                nextActionID = 0;
                break;

            case 70:
                // Woda Left mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00066"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00067"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00068"),
                        fWodaLeft, 3, 0, 0);
                break;

            case 75:
                // Woda Right mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00069"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00070"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00071"),
                        fWodaRight, 3, 0, 0);
                break;

            case 80:
                // mit Anglern reden, wenn rechts
                // Zufallszahl 0...1, erstes Mal immer 0
                int zuffZahl3 = (int) (Math.random() * 1.9);
                switch (zuffZahl3) {
                    case 0:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00072"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00073"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00074"));
                        }
                        break;

                    case 1:
                        if (mainFrame.sprache == 1) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00075"));
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00076"));
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Haty1_00077"));
                        }
                        break;
                }
                outputTextPos = mainFrame.ifont.CenterText(outputText, angler2.Wudzer2TalkPoint());
                mainFrame.krabat.SetFacing(9);
                TalkPerson = 31;
                nextActionID = 0;
                break;

            case 85:
                // Krabat beginnt MC (Wudzer1 benutzen)
                mainFrame.krabat.SetFacing(fWudzerjoRight);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                stehenderAnglerSchautSichUm = true;
                if (!mainFrame.Actions[156]) {
                    nextActionID = 600;  // erstes Ansprechen
                } else {
                    nextActionID = 610;
                }
                break;

            case 87:
                // Krabat beginnt MC (Wudzer2 benutzen)
                mainFrame.krabat.SetFacing(fWudzerjoRight);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 900;
                break;

            case 90:
                // Muell mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00078"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00079"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00080"),
                        fMuell, 3, 0, 0);
                break;

            case 100:
                // gehe zu Les1 
                NeuesBild(5, 4);
                break;

            case 101:
                // gehe zu Rapak 
                NeuesBild(8, 4);
                break;

            case 150:
                // Rohodz1 - Ausreden
                DingAusrede(fRohodzLeft);
                break;

            case 155:
                // Rohodz2 - Ausreden
                DingAusrede(fRohodzRight);
                break;

            case 160:
                // Bow - Ausreden
                if (!mainFrame.Actions[220]) {
                    DingAusrede(fBowRight);
                } else {
                    DingAusrede(fBowLeft);
                }
                break;

            case 165:
                // Colmik - Ausreden
                DingAusrede(fBoot);
                break;

            case 170:
                // Wodaleft - Ausreden
                DingAusrede(fWodaLeft);
                break;

            case 175:
                // Wodaright - Ausreden
                DingAusrede(fWodaRight);
                break;

            case 180:
                // Angler - Ausreden
                if (!mainFrame.Actions[220]) {
                    MPersonAusrede(fWudzerjoRight);
                } else {
                    MPersonAusrede(fWudzerjoLeft);
                }
                break;

            case 185:
                // Ausreden fuer Muell
                DingAusrede(fMuell);
                break;

            // Sonderreaktionen - Anim "Locke die Assis vom Teich weg" /////////////////


            case 200:
                // Anim Holzfisch rausholen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                Sonderstatus = 3;
                nextActionID = 201;
                break;

            case 201:
                // warten auf Ende angeln
                if (Sonderstatus != 0) {
                    break;
                }
                Sonderstatus = 5;
                // Holzfisch in Wasser tauchen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00081"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00082"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00083"),
                        0, 3, 5, 205);
                break;

            case 205:
                // Reaktion Angler 1
                schautAnglerInDerGegendRum = true;
                stehenderAnglerSchautSichUm = true;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00084"), Start.stringManager.getTranslation("Loc1_Haty1_00085"), Start.stringManager.getTranslation("Loc1_Haty1_00086"),
                        0, 31, 2, 210, angler2.Wudzer2TalkPoint());
                break;

            case 210:
                // Reaktion Angler 2
                schautAnglerInDerGegendRum = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00087"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00088"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00089"),
                        0, 30, 2, 215, angler1.Wudzer1TalkPoint());
                break;

            case 215:
                // Reaktion Angler 1
                stehenderAnglerSchautSichUm = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00090"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00091"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00092"),
                        0, 31, 2, 218, angler2.Wudzer2TalkPoint());
                break;

            case 218:
                // Angler laufen los
                Sonderstatus = 0;
                mainFrame.Clipset = false;
                angler2.SetWudzer2Pos(wudzer2RightWalk);
                angler2.MoveTo(wudzer2LeftWalk);
                walkWudzer2 = false;
                Verzoegerung = 5;
                nextActionID = 220;
                break;

            case 220:
                // angler 1 spaeter loslaufen lassen
                if (--Verzoegerung > 0) {
                    break;
                }
                angler1.SetWudzer1Pos(wudzer1RightWalk);
                angler1.MoveTo(wudzer1Eimernimm, true);
                walkWudzer1 = false;
                nextActionID = 221;
                break;

            case 221:
                // warten, bis 1. Angler am Eimer ist
                if (!walkWudzer1) {
                    break;
                }
                anglerNimmtEimer = true;
                merkeEimer = true;
                Verzoegerung = 5;
                nextActionID = 222;
                break;

            case 222:
                // warten auf Ende Eimernehmen
                if (--Verzoegerung > 0) {
                    break;
                }
                hatAnglerDenEimer = true;
                anglerNimmtEimer = false;
                angler1.MoveTo(wudzer1Eimergib, false);
                walkWudzer1 = false;
                nextActionID = 223;
                break;

            case 223:
                // wenn hingelaufen, dann Eimer wieder hinstellen
                if (!walkWudzer1) {
                    break;
                }
                anglerNimmtEimer = true;
                hatAnglerDenEimer = false;
                merkeEimer = false;
                mainFrame.Actions[220] = true;
                InitEimer();
                Verzoegerung = 5;
                nextActionID = 224;
                break;

            case 224:
                // warten auf Ende
                if (--Verzoegerung > 0) {
                    break;
                }
                anglerNimmtEimer = false;
                angler1.SetFacing(9);
                angler1.MoveTo(wudzer1LeftWalk, false);
                walkWudzer1 = false;
                nextActionID = 228;
                break;

            case 228:
                // warten auf Ende Anglerlaufen
                if (!walkWudzer1 || !walkWudzer2) {
                    break;
                }
                InitAngler();
                mainFrame.Clipset = false;
                TalkPause = 5;
                nextActionID = 229;
                break;

            case 229:
                // Reaktion Angler 1
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00093"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00094"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00095"),
                        0, 31, 2, 230, angler2.Wudzer2TalkPoint());
                break;

            case 230:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00096"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00097"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00098"),
                        0, 1, 2, 235);
                break;

            case 235:
                // Reaktion Angler 1
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00099"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00100"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00101"),
                        0, 31, 2, 240, angler2.Wudzer2TalkPoint());
                break;

            case 240:
                // Reaktion Angler 1
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00102"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00103"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00104"),
                        0, 31, 2, 245, angler2.Wudzer2TalkPoint());
                break;

            case 245:
                // Reaktion Angler 2
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00105"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00106"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00107"),
                        0, 30, 2, 250, angler1.Wudzer1TalkPoint());
                break;

            case 250:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00108"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00109"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00110"),
                        0, 1, 2, 255);
                break;

            case 255:
                // Reaktion Angler 2
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00111"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00112"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00113"),
                        0, 30, 2, 260, angler1.Wudzer1TalkPoint());
                break;

            case 260:
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            // Hier die Sonderausreden ///////////////////////////

            case 300:
                // Schilf links mit was erreichen versuchen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00114"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00115"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00116"),
                        fRohodzLeft, 3, 0, 0);
                break;

            case 305:
                // Schilf rechts mit was erreichen versuchen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00117"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00118"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00119"),
                        fRohodzRight, 3, 0, 0);
                break;

            case 310:
                // Schilf links mit Leine
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00120"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00121"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00122"),
                        fRohodzLeft, 3, 0, 0);
                break;

            case 315:
                // Schilf rechts mit Leine
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00123"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00124"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00125"),
                        fRohodzRight, 3, 0, 0);
                break;

            case 320:
                // Schilf links mit Schilf
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00126"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00127"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00128"),
                        fRohodzLeft, 3, 0, 0);
                break;

            case 325:
                // Schilf rechts mit Schilf
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00129"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00130"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00131"),
                        fRohodzRight, 3, 0, 0);
                break;

            case 330:
                // Eimer mit Wuermern
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00132"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00133"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00134"),
                            fBowRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00135"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00136"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00137"),
                            fBowLeft, 3, 0, 0);
                }
                break;

            case 340:
                // Eimer mit dryba
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00138"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00139"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00140"),
                            fBowRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00141"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00142"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00143"),
                            fBowLeft, 3, 0, 0);
                }
                break;

            case 350:
                // Eimer mit ryba
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00144"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00145"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00146"),
                            fBowRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00147"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00148"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00149"),
                            fBowLeft, 3, 0, 0);
                }
                break;

            case 360:
                // Colmik mit Lajna
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00150"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00151"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00152"),
                        fBoot, 3, 0, 0);
                break;

            case 370:
                // Woda left mit wuda
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00153"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00154"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00155"),
                        fWodaLeft, 3, 0, 0);
                break;

            case 375:
                // Woda right mit wuda
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00156"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00157"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00158"),
                        fWodaRight, 3, 0, 0);
                break;

            case 380:
                // Woda left mit wuda + hocka
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00159"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00160"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00161"),
                        fWodaLeft, 3, 0, 0);
                break;

            case 385:
                // Woda right mit wuda + hocka
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00162"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00163"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00164"),
                        fWodaRight, 3, 0, 0);
                break;

            case 390:
                // Woda left mit ryba
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00165"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00166"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00167"),
                        fWodaLeft, 3, 0, 0);
                break;

            case 395:
                // Woda right mit ryba
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00168"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00169"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00170"),
                        fWodaRight, 3, 0, 0);
                break;

            case 400:
                // Wudzerjo verpruegeln
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00171"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00172"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00173"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00174"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00175"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00176"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 410:
                // Wudzerjo Haken geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00177"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00178"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00179"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00180"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00181"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00182"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 420:
                // Wudzerjo wacki geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00183"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00184"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00185"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00186"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00187"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00188"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 430:
                // Wudzerjo angel geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00189"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00190"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00191"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00192"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00193"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00194"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 440:
                // Wudzerjo wuda + dryba geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00195"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00196"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00197"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00198"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00199"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00200"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 450:
                // Wudzerjo dryba geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00201"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00202"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00203"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00204"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00205"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00206"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 470:
                // Wudzerjo ryba geben
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00207"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00208"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00209"),
                            fWudzerjoRight, 1, 2, 473);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00210"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00211"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00212"),
                            fWudzerjoLeft, 1, 2, 473);
                }
                break;

            case 473:
                // Reaktion Angler 1
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00213"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00214"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00215"),
                        0, 31, 2, 476, angler2.Wudzer2TalkPoint());
                break;

            case 476:
                // Ende Anim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 480:
                // Wudzerjo krosik geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00216"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00217"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00218"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00219"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00220"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00221"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            case 490:
                // Wudzerjo bron geben
                if (!mainFrame.Actions[220]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00222"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00223"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00224"),
                            fWudzerjoRight, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00225"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00226"),
                            Start.stringManager.getTranslation("Loc1_Haty1_00227"),
                            fWudzerjoLeft, 3, 0, 0);
                }
                break;

            // Multiple Choice

                /*case 600:
                  // MC mit Angler 1 beginnen
                  if (mainFrame.sprache == 1) outputText = Start.stringManager.getTranslation("Loc1_Haty1_00228");
                  if (mainFrame.sprache == 2) outputText = Start.stringManager.getTranslation("Loc1_Haty1_00229");      
                  if (mainFrame.sprache == 3) outputText = Start.stringManager.getTranslation("Loc1_Haty1_00230");
                  outputTextPos = mainFrame.ifont.KrabatText (outputText);
                  mainFrame.krabat.SetFacing (9);
                  TalkPerson = 1;
                  TalkPause = 0;
                  nextActionID = 605;
                  break;*/

            case 600:
                // Reaktion Angler 1
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00231"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00232"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00233"),
                        fWudzerjoRight, 31, 2, 610, angler2.Wudzer2TalkPoint());
                mainFrame.Actions[156] = true; // keine 2x dummen Sprueche
                break;

            case 610:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00234"), 1000, 140, new int[]{140}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00235"), 140, 141, new int[]{141}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00236"), 141, 142, new int[]{142}, 640);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00237"), 1000, 143, new int[]{143}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00238"), 143, 144, new int[]{144}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00239"), 144, 145, null, 670);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00240"), 1000, 146, new int[]{146}, 680);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00241"), 146, 147, new int[]{148, 150}, 690);

                    // 5. Frage (4. = Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00242"), 148, 149, null, 700);

                    // 4. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00243"), 1000, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00244"), 1000, 140, new int[]{140}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00245"), 140, 141, new int[]{141}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00246"), 141, 142, new int[]{142}, 640);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00247"), 1000, 143, new int[]{143}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00248"), 143, 144, new int[]{144}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00249"), 144, 145, null, 670);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00250"), 1000, 146, new int[]{146}, 680);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00251"), 146, 147, new int[]{148, 150}, 690);

                    // 5. Frage (4. = Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00252"), 148, 149, null, 700);

                    // 4. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00253"), 1000, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00254"), 1000, 140, new int[]{140}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00255"), 140, 141, new int[]{141}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00256"), 141, 142, new int[]{142}, 640);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00257"), 1000, 143, new int[]{143}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00258"), 143, 144, new int[]{144}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00259"), 144, 145, null, 670);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00260"), 1000, 146, new int[]{146}, 680);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00261"), 146, 147, new int[]{148, 150}, 690);

                    // 5. Frage (4. = Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00262"), 148, 149, null, 700);

                    // 4. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00263"), 1000, 1000, null, 800);

                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 611;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 611:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;
                nextActionID = Dialog.ActionID;
                break;

            case 620:
                // Reaktion Angler 1 auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00264"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00265"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00266"),
                        0, 31, 2, 621, angler2.Wudzer2TalkPoint());
                break;

            case 621:
                // Reaktion Angler 1 auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00267"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00268"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00269"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 630:
                // Reaktion Angler 1 auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00270"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00271"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00272"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 640:
                // Reaktion Angler 1 auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00273"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00274"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00275"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 650:
                // Reaktion Angler 1 auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00276"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00277"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00278"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 660:
                // Reaktion Angler 1 auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00279"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00280"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00281"),
                        0, 31, 2, 661, angler2.Wudzer2TalkPoint());
                break;

            case 661:
                // Reaktion Angler 1 auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00282"), Start.stringManager.getTranslation("Loc1_Haty1_00283"), Start.stringManager.getTranslation("Loc1_Haty1_00284"),
                        0, 31, 2, 662, angler2.Wudzer2TalkPoint());
                break;

            case 662:
                // Reaktion Angler 1 auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00285"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00286"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00287"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 670:
                // Reaktion Angler 1 auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00288"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00289"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00290"),
                        0, 31, 2, 671, angler2.Wudzer2TalkPoint());
                break;

            case 671:
                // Reaktion Angler 1 auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00291"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00292"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00293"),
                        0, 31, 2, 672, angler2.Wudzer2TalkPoint());
                break;

            case 672:
                // Reaktion Angler 1 auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00294"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00295"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00296"),
                        0, 31, 2, 673, angler2.Wudzer2TalkPoint());
                break;

            case 673:
                // Reaktion Angler 1 auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00297"), Start.stringManager.getTranslation("Loc1_Haty1_00298"), Start.stringManager.getTranslation("Loc1_Haty1_00299"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 680:
                // Reaktion Angler 1 auf 1. Teil 3. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00300"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00301"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00302"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 690:
                // Reaktion Angler 1 auf 2. Teil 3. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00303"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00304"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00305"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 700:
                // Reaktion Angler 1 auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00306"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00307"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00308"),
                        0, 31, 2, 702, angler2.Wudzer2TalkPoint());
                break;
     
            /*case 701:
                // Reaktion Angler 1 auf 1. Teil 5. Frage
                PersonSagt (Start.stringManager.getTranslation("Loc1_Haty1_00309"), Start.stringManager.getTranslation("Loc1_Haty1_00310"), Start.stringManager.getTranslation("Loc1_Haty1_00311"),
                            0, 31, 2, 702, angler2.Wudzer2TalkPoint () );
                break;*/

            case 702:
                // Reaktion Angler 1 auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00312"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00313"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00314"),
                        0, 31, 2, 703, angler2.Wudzer2TalkPoint());
                break;

            case 703:
                // Reaktion Angler 1 auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00315"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00316"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00317"),
                        0, 31, 2, 610, angler2.Wudzer2TalkPoint());
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.fPlayAnim = false;
                stehenderAnglerSchautSichUm = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 900:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00318"), 1000, 1000, null, 910);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00319"), 1000, 1000, null, 910);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00320"), 1000, 1000, null, 1000);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00321"), 1000, 1000, null, 910);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00322"), 1000, 1000, null, 910);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00323"), 1000, 1000, null, 1000);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00324"), 1000, 1000, null, 910);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00325"), 1000, 1000, null, 910);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Haty1_00326"), 1000, 1000, null, 1000);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 901;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 901:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 910:
                // Angler 2  - Antworten
                int zg = (int) Math.round(Math.random() * 4);
                PersonSagt(HWAnt[zg], DWAnt[zg], NWAnt[zg], 0, 30, 2, 900, angler1.Wudzer1TalkPoint());
                break;

            case 1000:
                // MC beenden, wenn zuende gelabert...
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 1200:
                // Reaktion Angler 1, wenn links und K irgendwie angeln will (sinnvolle Angeln)
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00327"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00328"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00329"),
                        0, 31, 2, 0, angler2.Wudzer2TalkPoint());
                break;

            case 1210:
                // Reaktion Angler 1, wenn rechts und K irgendwie angeln
                PersonSagt(Start.stringManager.getTranslation("Loc1_Haty1_00330"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00331"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00332"),
                        0, 31, 2, 0, angler2.Wudzer2TalkPoint());
                break;

            case 1220:
                // Angeln und Fisch dran
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeFische = true;
                mainFrame.invCursor = false;
                Sonderstatus = 4;
                mainFrame.inventory.vInventory.addElement(14);
                mainFrame.Actions[914] = true;
                nextActionID = 1222;
                break;

            case 1222:
                // warten bis Ende Anim
                if (Sonderstatus == 0) {
                    nextActionID = 1224;
                }
                break;

            case 1224:
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeFische = false;
                nextActionID = 0;
                break;

            case 1230:
                // Angeln wenn schon Fisch oder noch krosik
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeFische = true;
                mainFrame.invCursor = false;
                Sonderstatus = 2;
                nextActionID = 1232;
                break;

            case 1232:
                // warten auf Ende Anim
                if (Sonderstatus == 0) {
                    nextActionID = 1238;
                }
                break;

            case 1238:
                // Habe doch schon Fisch oder krosik
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00333"), Start.stringManager.getTranslation("Loc1_Haty1_00334"), Start.stringManager.getTranslation("Loc1_Haty1_00335"),
                        3, 3, 0, 0);
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeFische = false;
                break;

            case 1240:
                // Angeln im falschen Teich, es passiert nichts
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeFische = true;
                mainFrame.invCursor = false;
                Sonderstatus = 1;
                nextActionID = 1242;
                break;

            case 1242:
                // warten auf Ende Anim
                if (Sonderstatus == 0) {
                    nextActionID = 1248;
                }
                break;

            case 1248:
                // Fange nichts
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00336"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00337"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00338"),
                        9, 3, 0, 0);
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeFische = false;
                break;

            case 1250:
                // Den Holzfischtrick nicht rechts versuchen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Haty1_00339"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00340"),
                        Start.stringManager.getTranslation("Loc1_Haty1_00341"),
                        fWodaRight, 3, 0, 0);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}