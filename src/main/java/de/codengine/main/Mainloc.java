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

package de.codengine.main;

import de.codengine.Start;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

abstract public class Mainloc {
    // ID der jeweiligen Location (SS)
    public int locationID;

    // Variablen fuer Textausgabe
    public String outputText = "";
    public GenericPoint outputTextPos;

    // Variablen fuer Textausgabe + Zeit
    // Achtung : Diese Variablen werden nicht in jeder Klasse benutzt, sind aber fuer Krabat -
    // Ausreden unbedingt erforderlich (Multiple - Choice - Klasse)
    public int TalkPerson = 0;
    public int TalkPause = 0;

    private int Counter; // universeller Zaehler fuer alles Moegliche

    // Variablen fuer Krabat - Ausreden
    public int nextActionID = 0;

    // Variablen fuer Cursor, da stets vorhanden, hierher nehmen
    public int Cursorform = 200; // Wert, damit default = neuen Cursor setzen (siehe evalMouseMoveEvent jeder Klasse)

    // Objekt auf "start" - sollte das hier rein (Geschwindigkeit ???)
    public Start mainFrame;

    // Variablen fuer Standardausreden
    private static final String[] HAus = {Start.stringManager.getTranslation("Main_Mainloc_00000"), Start.stringManager.getTranslation("Main_Mainloc_00001"), Start.stringManager.getTranslation("Main_Mainloc_00002"), Start.stringManager.getTranslation("Main_Mainloc_00003"), Start.stringManager.getTranslation("Main_Mainloc_00004")};
    private static final String[] DAus = {Start.stringManager.getTranslation("Main_Mainloc_00005"), Start.stringManager.getTranslation("Main_Mainloc_00006"), Start.stringManager.getTranslation("Main_Mainloc_00007"), Start.stringManager.getTranslation("Main_Mainloc_00008"), Start.stringManager.getTranslation("Main_Mainloc_00009")};
    private static final String[] NAus = {Start.stringManager.getTranslation("Main_Mainloc_00010"), Start.stringManager.getTranslation("Main_Mainloc_00011"), Start.stringManager.getTranslation("Main_Mainloc_00012"), Start.stringManager.getTranslation("Main_Mainloc_00013"), Start.stringManager.getTranslation("Main_Mainloc_00014")};
    private static final int AUS_KONSTANTE = 4;

    // Variablen fuer Standardausreden Personen

    // maennlich
    private static final String[] HMAus = {Start.stringManager.getTranslation("Main_Mainloc_00015"), Start.stringManager.getTranslation("Main_Mainloc_00016"), Start.stringManager.getTranslation("Main_Mainloc_00017")};
    private static final String[] DMAus = {Start.stringManager.getTranslation("Main_Mainloc_00018"), Start.stringManager.getTranslation("Main_Mainloc_00019"), Start.stringManager.getTranslation("Main_Mainloc_00020")};
    private static final String[] NMAus = {Start.stringManager.getTranslation("Main_Mainloc_00021"), Start.stringManager.getTranslation("Main_Mainloc_00022"), Start.stringManager.getTranslation("Main_Mainloc_00023")};
    private static final int MAUS_KONSTANTE = 2;

    // weiblich
    private static final String[] HWAus = {Start.stringManager.getTranslation("Main_Mainloc_00024"), Start.stringManager.getTranslation("Main_Mainloc_00025"), Start.stringManager.getTranslation("Main_Mainloc_00026")};
    private static final String[] DWAus = {Start.stringManager.getTranslation("Main_Mainloc_00027"), Start.stringManager.getTranslation("Main_Mainloc_00028"), Start.stringManager.getTranslation("Main_Mainloc_00029")};
    private static final String[] NWAus = {Start.stringManager.getTranslation("Main_Mainloc_00030"), Start.stringManager.getTranslation("Main_Mainloc_00031"), Start.stringManager.getTranslation("Main_Mainloc_00032")};
    private static final int WAUS_KONSTANTE = 2;

    // mehrere (groesser 2)
    private static final String[] HAAus = {Start.stringManager.getTranslation("Main_Mainloc_00033"), Start.stringManager.getTranslation("Main_Mainloc_00034"), Start.stringManager.getTranslation("Main_Mainloc_00035")};
    private static final String[] DAAus = {Start.stringManager.getTranslation("Main_Mainloc_00036"), Start.stringManager.getTranslation("Main_Mainloc_00037"), Start.stringManager.getTranslation("Main_Mainloc_00038")};
    private static final String[] NAAus = {Start.stringManager.getTranslation("Main_Mainloc_00039"), Start.stringManager.getTranslation("Main_Mainloc_00040"), Start.stringManager.getTranslation("Main_Mainloc_00041")};
    private static final int AAUS_KONSTANTE = 2;

    // 2 mit maennlicher Dominanz
    private static final String[] HMMAus = {Start.stringManager.getTranslation("Main_Mainloc_00042"), Start.stringManager.getTranslation("Main_Mainloc_00043"), Start.stringManager.getTranslation("Main_Mainloc_00044")};
    private static final String[] DMMAus = {Start.stringManager.getTranslation("Main_Mainloc_00045"), Start.stringManager.getTranslation("Main_Mainloc_00046"), Start.stringManager.getTranslation("Main_Mainloc_00047")};
    private static final String[] NMMAus = {Start.stringManager.getTranslation("Main_Mainloc_00048"), Start.stringManager.getTranslation("Main_Mainloc_00049"), Start.stringManager.getTranslation("Main_Mainloc_00050")};
    private static final int MMAUS_KONSTANTE = 2;

    // 2 weibliche
    private static final String[] HWWAus = {Start.stringManager.getTranslation("Main_Mainloc_00051"), Start.stringManager.getTranslation("Main_Mainloc_00052"), Start.stringManager.getTranslation("Main_Mainloc_00053")};
    private static final String[] DWWAus = {Start.stringManager.getTranslation("Main_Mainloc_00054"), Start.stringManager.getTranslation("Main_Mainloc_00055"), Start.stringManager.getTranslation("Main_Mainloc_00056")};
    private static final String[] NWWAus = {Start.stringManager.getTranslation("Main_Mainloc_00057"), Start.stringManager.getTranslation("Main_Mainloc_00058"), Start.stringManager.getTranslation("Main_Mainloc_00059")};
    private static final int WWAUS_KONSTANTE = 2;

    // Hier das Farbenarray nach Talkpersons geordnet
    public static final int[] FarbenArray = {0x00000000, 0x00000001, 0x00000000, 0x00000001, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0x00000000, 0x00000000, 0x00000000, 0xffff0000, 0xffb00000,
            0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000,
            0xff948c00,  // 20 (Mutter)
            0xffb594de,  // 21 (Bur Ralbicy) 0xffa58787 alt
            0xffc0c0c0,  // 22 (Kowar Sunow)
            0xffb5b684,  // 23 (Pjany)
            0xffd64624,  // 24 (Dundak)
            0xff89adad,  // 25 (Korcmar)
            0xffe8632a,  // 26 (Hojnt)
            0xff45c2eb,  // 27 (Plokarka)
            0xffecd422,  // 28 (Michal in Polo) 0xff828200 alt
            0xffb39292,  // 29 (Hanza in Polo)
            0xffc2d6af,  // 30 (Wudzer1)
            0xffd6e7ff,  // 31 (Wudzer2) 0xff7c6452 alt
            0xffffff00,  // 32 (WikowarZita) 0xffc1a15b alt
            0xffccd8fa,  // 33 (WikowarRybow)
            0xfff87c54,  // 34 (Wmuz)
            0xfff7ff63,  // 35 (Bludnickis)
            0xffe5b814,  // 36 (Mlynk)
            0xffabb0cd,  // 37 (Farar)
            0xffbfc2f2,  // 38 (Posol)
            0xfff3dd41,  // 39 (Pohonc)
            0xffea9e26,  // 40 (Awgust)
            0xfff0f0f0,  // 41 (Rapak)
            0xfffd9488,  // 42 (Kuchar) alt 0xfffd9488,
            0xfffc4835,  // 43 (Straza 1)  alt 0xffabb0cd
            0xfff4f4f4,  // 44 (Straza 2)
            0xffa8e34b,  // 45 (KowarDD) alt 0xffe99986 0xfff8261a
            0xff73a786,  // 46 (Straza Poklad)
            0xfff2f1cd,  // 47 (Dinglinger)
            0xffcdfbab,  // 48 (Hl. Straznik)
            0xfff2ecb5,  // 49 (Mato)
            0xfffae8af,  // 50 (Zahrodnik)
            0xffe8cc98,  // 51 (Wald in Kolmc)
            0xffffdead,  // 52 (StimmeInKulow)
            0xffff4242,  // 53 (WmuzFrau) 0xff7f90b1 alt
            0xffff0000,  // 54 (Erzaehler)
            0xff45c2eb,  // 55 (Plokarka mit erhobenen Haenden)
            0xffe4ce95,  // 56 (Wudowa)
            0xffe4ce95,  // 57 (lachende Wudowa)
            0xffef958c,  // 58 (Dziwadzelnica)
            0xffd2e4da,  // 59 (Kocka)
            0xff948c00,  // 60 (Mac zeigt mit Finger auf Raben)
            0xffebe696,  // 61 (Gonzales)
            0xffffad52,  // 62 (PredKorjejtow)
            0xffde2d14,  // 63 (PredWosuskow)
            0xffb5d6de,  // 64 (PredMalickow)
            0xffdcc002,  // 65 (WikowarkaRudy)
            0xffe5b814,  // 66 (Mlynk in DD)
            0xffe5b814,  // 67 (Mlynk lacht in DD)
            0xfffa4933,  // 68 (Loewe)
            0xff8a97cc,  // 69 (Stimme in Stwa)
            0xfffa4933,  // 70 (Loewe grrr)
            0xffe5b814,  // 71 (Mueller als Ente)
            0xfff111ff,  // 72 (Ente im Dialog mit Mueller)
            0xffffffff,
            0xffffffff,
            0xffffffff,
            0xffffffff,
            0xffffffff,
            0xffffffff,
            0xfff111ff, // Violett
            0xff5f9ea0, // cadet blue
            0xffb5ea24, // Gruengelb
            0xff1dbdff, // Hellblau
            0xffff590c, // Orange
            0xff94ad79, // Graugelb
            0xff18faa8, // Blaugruen
            0xff8470ff, // light slate blue
            0xffa020f0, // purple
            0xffbc8f8f, // rosy brown
            0xffa52a2a, // Braun
            0xff191970, // midnight blue
            0xffe0ff11, // Gelb
            0xffffffff, // Weiss
            0xff8d4607, // Braun
            0xff808080, // Grau mittel
            0xffd8bfd8, // thistle
            0xffff7f24, // chocolate
            0xffffffff,
            0xffffffff};

    // Methodendefinitionen

    // Konstruktor
    public Mainloc(Start caller) {
        // tracker = new MediaTracker (this);
        mainFrame = caller;

    }

    // Konstruktor mit Location-ID (SS)
    public Mainloc(Start caller, int idLocation) {
        // tracker = new MediaTracker (this);
        mainFrame = caller;
        locationID = idLocation;
    }

    // Methode zum Laden (registrieren) eines Bildes
    public GenericImage getPicture(String Filename) {
        return mainFrame.imageFetcher.fetchImage(Filename);
    }

    public void loadPicture() {
    }

    // Hier werden die Ausreden "Benutze Krabat mit Gegenstand" initialisiert
    // Blickrichtung hier immer "nach aussen" Richtung User

    public void setKrabatAusrede() {
        switch (nextActionID) {
            case 501:
                // Floete
                mainFrame.krabat.nAnimation = 2;
                nextActionID = 0;
                break;

            case 502:
                // Krabat mit Stock benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00060"),
                        Start.stringManager.getTranslation("Main_Mainloc_00061"),
                        Start.stringManager.getTranslation("Main_Mainloc_00062"),
                        0, 3, 0, 0);
                break;

            case 503:
                // Krabat mit Deska benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00063"),
                        Start.stringManager.getTranslation("Main_Mainloc_00064"),
                        Start.stringManager.getTranslation("Main_Mainloc_00065"),
                        0, 3, 0, 0);
                break;

            case 504:
                // Krabat mit Honck benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00066"),
                        Start.stringManager.getTranslation("Main_Mainloc_00067"),
                        Start.stringManager.getTranslation("Main_Mainloc_00068"),
                        0, 3, 0, 0);
                break;

            case 505:
                // Krabat mit Hocka benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00069"),
                        Start.stringManager.getTranslation("Main_Mainloc_00070"),
                        Start.stringManager.getTranslation("Main_Mainloc_00071"),
                        0, 3, 0, 0);
                break;

            case 506:
                // Krabat mit Lajna benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00072"),
                        Start.stringManager.getTranslation("Main_Mainloc_00073"),
                        Start.stringManager.getTranslation("Main_Mainloc_00074"),
                        0, 3, 0, 0);
                break;

            case 507:
                // Krabat mit Wuda benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00075"),
                        Start.stringManager.getTranslation("Main_Mainloc_00076"),
                        Start.stringManager.getTranslation("Main_Mainloc_00077"),
                        0, 3, 0, 0);
                break;

            case 508:
                // Krabat mit Wacki benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00078"),
                        Start.stringManager.getTranslation("Main_Mainloc_00079"),
                        Start.stringManager.getTranslation("Main_Mainloc_00080"),
                        0, 3, 0, 0);
                break;

            case 509:
                // Krabat mit Wuda + Hocka benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00081"),
                        Start.stringManager.getTranslation("Main_Mainloc_00082"),
                        Start.stringManager.getTranslation("Main_Mainloc_00083"),
                        0, 3, 0, 0);
                break;

            case 510:
                // Krabat mit Wuda + Hocka + Wacka benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00084"),
                        Start.stringManager.getTranslation("Main_Mainloc_00085"),
                        Start.stringManager.getTranslation("Main_Mainloc_00086"),
                        0, 3, 0, 0);
                break;

            case 511:
                // Krabat mit Wuda + Hocka + Drjewjana Ryba benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00087"),
                        Start.stringManager.getTranslation("Main_Mainloc_00088"),
                        Start.stringManager.getTranslation("Main_Mainloc_00089"),
                        0, 3, 0, 0);
                break;

            case 512:
                // Krabat mit Wohnjowe Kamuski benutzen
                mainFrame.krabat.nAnimation = 154;
                nextActionID = 0;
                break;

            case 513:
                // Krabat mit Drjewjana Ryba benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00090"),
                        Start.stringManager.getTranslation("Main_Mainloc_00091"),
                        Start.stringManager.getTranslation("Main_Mainloc_00092"),
                        0, 3, 0, 0);
                break;

            case 514:
                // Krabat mit Ryba benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00093"),
                        Start.stringManager.getTranslation("Main_Mainloc_00094"),
                        Start.stringManager.getTranslation("Main_Mainloc_00095"),
                        0, 3, 0, 0);
                break;

            case 515:
                // Krabat mit Krosik benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00096"),
                        Start.stringManager.getTranslation("Main_Mainloc_00097"),
                        Start.stringManager.getTranslation("Main_Mainloc_00098"),
                        0, 3, 0, 0);
                break;

            case 516:
                // Krabat mit Honck z blotom benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00099"),
                        Start.stringManager.getTranslation("Main_Mainloc_00100"),
                        Start.stringManager.getTranslation("Main_Mainloc_00101"),
                        0, 3, 0, 0);
                break;

            case 517:
                // Krabat mit Rohodz benutzen
                // Floete
                mainFrame.krabat.nAnimation = 5;
                nextActionID = 0;
                break;

            case 518:
                // Krabat mit Rohodz + Kamusk benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00102"),
                        Start.stringManager.getTranslation("Main_Mainloc_00103"),
                        Start.stringManager.getTranslation("Main_Mainloc_00104"),
                        0, 3, 0, 0);
                break;

            case 519:
                // Krabat mit Pjero benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00105"),
                        Start.stringManager.getTranslation("Main_Mainloc_00106"),
                        Start.stringManager.getTranslation("Main_Mainloc_00107"),
                        0, 3, 0, 0);
                break;

            case 520:
                // Karte aktivieren
                mainFrame.ConstructLocation(106);
                mainFrame.invCursor = false;
                mainFrame.isAnim = false;
                mainFrame.whatScreen = 6;
                nextActionID = 0;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            // Dresdner - Zeug (TEIL 3) - SS

            case 530:
                // List auf Krabat
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00108"),
                        Start.stringManager.getTranslation("Main_Mainloc_00109"),
                        Start.stringManager.getTranslation("Main_Mainloc_00110"),
                        0, 3, 0, 0);
                break;

            case 531:
                // Krabat mit Dowolnosc pur benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00111"),
                        Start.stringManager.getTranslation("Main_Mainloc_00112"),
                        Start.stringManager.getTranslation("Main_Mainloc_00113"),
                        0, 3, 0, 0);
                break;

            case 532:
                // Krabat mit Dowolnosc gesiegelt benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00114"),
                        Start.stringManager.getTranslation("Main_Mainloc_00115"),
                        Start.stringManager.getTranslation("Main_Mainloc_00116"),
                        0, 3, 0, 0);
                break;

            case 533:
                // Krabat mit Dowolnosc unterschrieben benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00117"),
                        Start.stringManager.getTranslation("Main_Mainloc_00118"),
                        Start.stringManager.getTranslation("Main_Mainloc_00119"),
                        0, 3, 0, 0);
                break;

            case 534:
                // Krabat mit Dowolnosc komplett benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00120"),
                        Start.stringManager.getTranslation("Main_Mainloc_00121"),
                        Start.stringManager.getTranslation("Main_Mainloc_00122"),
                        0, 3, 0, 0);
                break;

            case 535:
                // Krabat mit drjewo benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00123"),
                        Start.stringManager.getTranslation("Main_Mainloc_00124"),
                        Start.stringManager.getTranslation("Main_Mainloc_00125"),
                        0, 3, 0, 0);
                break;

            case 536:
                // Krabat mit Kozuch benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00126"),
                        Start.stringManager.getTranslation("Main_Mainloc_00127"),
                        Start.stringManager.getTranslation("Main_Mainloc_00128"),
                        0, 3, 0, 0);
                break;

            case 537:
                // Krabat mit kotwica benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00129"),
                        Start.stringManager.getTranslation("Main_Mainloc_00130"),
                        Start.stringManager.getTranslation("Main_Mainloc_00131"),
                        0, 3, 0, 0);
                break;

            case 538:
                // Krabat mit Lajna benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00132"),
                        Start.stringManager.getTranslation("Main_Mainloc_00133"),
                        Start.stringManager.getTranslation("Main_Mainloc_00134"),
                        0, 3, 0, 0);
                break;

            case 539:
                // Krabat mit kotwica + Lajna benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00135"),
                        Start.stringManager.getTranslation("Main_Mainloc_00136"),
                        Start.stringManager.getTranslation("Main_Mainloc_00137"),
                        0, 3, 0, 0);
                break;

            case 540:
                // Krabat mit 5 tolerow benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00138"),
                        Start.stringManager.getTranslation("Main_Mainloc_00139"),
                        Start.stringManager.getTranslation("Main_Mainloc_00140"),
                        0, 3, 0, 0);
                break;

            case 541:
                // Krabat mit Bedienstetenkleidung benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00141"),
                        Start.stringManager.getTranslation("Main_Mainloc_00142"),
                        Start.stringManager.getTranslation("Main_Mainloc_00143"),
                        0, 3, 0, 0);
                break;

            case 542:
                // Krabat mit Hlebija benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00144"),
                        Start.stringManager.getTranslation("Main_Mainloc_00145"),
                        Start.stringManager.getTranslation("Main_Mainloc_00146"),
                        0, 3, 0, 0);
                break;

            case 543:
                // Krabat mit Friedhelm benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00147"),
                        Start.stringManager.getTranslation("Main_Mainloc_00148"),
                        Start.stringManager.getTranslation("Main_Mainloc_00149"),
                        0, 3, 0, 0);
                break;

            case 544:
                // Krabat mit Wilhelm benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00150"),
                        Start.stringManager.getTranslation("Main_Mainloc_00151"),
                        Start.stringManager.getTranslation("Main_Mainloc_00152"),
                        0, 3, 0, 0);
                break;

            case 545:
                // Krabat mit Casnik benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00153"),
                        Start.stringManager.getTranslation("Main_Mainloc_00154"),
                        Start.stringManager.getTranslation("Main_Mainloc_00155"),
                        0, 3, 0, 0);
                break;

            case 546:
                // Krabat mit Hamor benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00156"),
                        Start.stringManager.getTranslation("Main_Mainloc_00157"),
                        Start.stringManager.getTranslation("Main_Mainloc_00158"),
                        0, 3, 0, 0);
                break;

            case 547:
                // Krabat mit Kluc benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00159"),
                        Start.stringManager.getTranslation("Main_Mainloc_00160"),
                        Start.stringManager.getTranslation("Main_Mainloc_00161"),
                        0, 3, 0, 0);
                break;

            case 548:
                // Krabat mit Metall benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00162"),
                        Start.stringManager.getTranslation("Main_Mainloc_00163"),
                        Start.stringManager.getTranslation("Main_Mainloc_00164"),
                        0, 3, 0, 0);
                break;

            case 549:
                // Krabat mit Prikaz benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00165"),
                        Start.stringManager.getTranslation("Main_Mainloc_00166"),
                        Start.stringManager.getTranslation("Main_Mainloc_00167"),
                        0, 3, 0, 0);
                break;

            case 550:
                // Krabat mit Skica benutzen
                mainFrame.ConstructLocation(108);
                mainFrame.invCursor = false;
                mainFrame.whatScreen = 8;
                nextActionID = 0;
                mainFrame.Clipset = false;
                mainFrame.repaint();
//                 KrabatSagt ("Skicu trjeba Dinglinger!",
//                             "Skicu trjeba Dinglinger.",
//                             "Die Skizze braucht Dinglinger!",
//                             0, 3, 0, 0);
                break;

            case 551:
                // Krabat mit halbem Wosusk benutzen
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 146;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                mainFrame.inventory.vInventory.removeElement(new Integer(51));
                mainFrame.Actions[680] = true;
                nextActionID = 590;
                break;

            case 552:
                // Krabat mit Wosusk benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00168"),
                        Start.stringManager.getTranslation("Main_Mainloc_00169"),
                        Start.stringManager.getTranslation("Main_Mainloc_00170"),
                        0, 3, 0, 0);
                break;

            case 553:
                // Krabat mit Drasta benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00171"),
                        Start.stringManager.getTranslation("Main_Mainloc_00172"),
                        Start.stringManager.getTranslation("Main_Mainloc_00173"),
                        0, 3, 0, 0);
                break;

            case 554:
                // Krabat mit Karta benutzen
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00174"),
                        Start.stringManager.getTranslation("Main_Mainloc_00175"),
                        Start.stringManager.getTranslation("Main_Mainloc_00176"),
                        0, 3, 0, 0);
                break;

            case 555:
                // Krabat liest Buch oder hat schon gelesen
                if (mainFrame.Actions[955] == true) {
                    KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00177"),
                            Start.stringManager.getTranslation("Main_Mainloc_00178"),
                            Start.stringManager.getTranslation("Main_Mainloc_00179"),
                            0, 3, 0, 0);
                } else {
                    mainFrame.Actions[955] = true;
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00180"),
                            Start.stringManager.getTranslation("Main_Mainloc_00181"),
                            Start.stringManager.getTranslation("Main_Mainloc_00182"),
                            0, 3, 2, 580);
                }
                break;

            case 560:
                // Schuessel auf K
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00183"),
                        Start.stringManager.getTranslation("Main_Mainloc_00184"),
                        Start.stringManager.getTranslation("Main_Mainloc_00185"),
                        0, 3, 0, 0);
                break;

            case 561:
                // Koraktor auf K (entfaellt, wird in Rowy abgefangen)
                break;

            case 562:
                // grosser Stein auf K
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00186"),
                        Start.stringManager.getTranslation("Main_Mainloc_00187"),
                        Start.stringManager.getTranslation("Main_Mainloc_00188"),
                        0, 3, 0, 0);
                break;

            case 563:
                // Stroh auf K
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00189"),
                        Start.stringManager.getTranslation("Main_Mainloc_00190"),
                        Start.stringManager.getTranslation("Main_Mainloc_00191"),
                        0, 3, 0, 0);
                break;

            // Ab hier Extra-IDs, die nicht an InventarID gebunden sind (Extrawuerste)

            case 580:
                // Krabat liest im schweren Buch
                mainFrame.krabat.nAnimation = 155;
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00192"),
                        Start.stringManager.getTranslation("Main_Mainloc_00193"),
                        Start.stringManager.getTranslation("Main_Mainloc_00194"),
                        0, 3, 2, 581);
                break;

            case 581:
                // paar Sekunden warten
                Counter = 60;
                nextActionID = 582;
                break;

            case 582:
                // Erzaehlerspruch
                if ((--Counter) > 1) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Main_Mainloc_00195"),
                        Start.stringManager.getTranslation("Main_Mainloc_00196"),
                        Start.stringManager.getTranslation("Main_Mainloc_00197"),
                        0, 54, 2, 583, new GenericPoint(320, 200));
                break;

            case 583:
                // Krabat hat zuendegelesen
                mainFrame.krabat.StopAnim();
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00198"),
                        Start.stringManager.getTranslation("Main_Mainloc_00199"),
                        Start.stringManager.getTranslation("Main_Mainloc_00200"),
                        0, 3, 2, 584);
                break;

            case 584:
                // Ende dieser Anim 
                mainFrame.invCursor = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 590:
                // Kommentar nach Wosuskessen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Main_Mainloc_00201"),
                        Start.stringManager.getTranslation("Main_Mainloc_00202"),
                        Start.stringManager.getTranslation("Main_Mainloc_00203"),
                        6, 3, 0, 0);
                break;

            default:
                System.out.println("Es fehlt eine Ausrede !!!! " + nextActionID);
                break;

        }
    }

    // Fuer die einzelnen Personen/Dinge - Ausreden wird das alles einzeln geregelt, da in der Location
    // oefter auch mal eine positive Reaktion kommt bzw. sich eine Anim anschliesst
    // Deshalb einzelne Methoden, auch wegen Blickrichtung

    // Achtung ! Innerhalb der Location muss jedoch das SetFacing in anims.hrajer (= Krabat)
    // ausgefuehrt werden, da man hier allgemein keine Blickrichtung festlegen kann

    // Neue Ausreden - Routinen, die Facing mitbehandeln und KrabatSagt benutzen

    // fuer Dinge in Location
    public void DingAusrede(int Facing) {
        int zuffAusrede = (int) (Math.random() * AUS_KONSTANTE);
        KrabatSagt(HAus[zuffAusrede], DAus[zuffAusrede], NAus[zuffAusrede], Facing, 3, 0, 0);
    }

    // fuer eine maennliche Person
    public void MPersonAusrede(int Facing) {
        int zuffAusrede = (int) (Math.random() * MAUS_KONSTANTE);
        KrabatSagt(HMAus[zuffAusrede], DMAus[zuffAusrede], NMAus[zuffAusrede], Facing, 3, 0, 0);
    }

    // fuer eine weibliche Person
    public void WPersonAusrede(int Facing) {
        int zuffAusrede = (int) (Math.random() * WAUS_KONSTANTE);
        KrabatSagt(HWAus[zuffAusrede], DWAus[zuffAusrede], NWAus[zuffAusrede], Facing, 3, 0, 0);
    }

    // fuer mehr als 2 Personen (A steht fuer All)
    public void APersonAusrede(int Facing) {
        int zuffAusrede = (int) (Math.random() * AAUS_KONSTANTE);
        KrabatSagt(HAAus[zuffAusrede], DAAus[zuffAusrede], NAAus[zuffAusrede], Facing, 3, 0, 0);
    }

    // Dual fuer mindestens 1 maennliche Person
    public void MMPersonAusrede(int Facing) {
        int zuffAusrede = (int) (Math.random() * MMAUS_KONSTANTE);
        KrabatSagt(HMMAus[zuffAusrede], DMMAus[zuffAusrede], NMMAus[zuffAusrede], Facing, 3, 0, 0);
    }

    // Dual fuer 2 weibliche Personen  				
    public void WWPersonAusrede(int Facing) {
        int zuffAusrede = (int) (Math.random() * WWAUS_KONSTANTE);
        KrabatSagt(HWWAus[zuffAusrede], DWWAus[zuffAusrede], NWWAus[zuffAusrede], Facing, 3, 0, 0);
    }

    // allgemeine Methoden, die den Quelltext deutlich reduzieren und Fehlern vorbeugen

    // Krabat spricht, entweder allein oder zu jemandem
    public void KrabatSagt(String HText, String DText, String NText, int Facing, int Person, int Pause, int ActionID) {
        switch (mainFrame.sprache) {
            case 1: // Obersorbisch
                outputText = mainFrame.ifont.TeileText(HText);
                break;

            case 2: // Niedersorbisch
                outputText = mainFrame.ifont.TeileText(DText);
                break;

            case 3: // Deutsch
                outputText = mainFrame.ifont.TeileText(NText);
                break;
        }
        outputTextPos = mainFrame.ifont.KrabatText(outputText);
        if (Facing != 0) {
            mainFrame.krabat.SetFacing(Facing);
        }
        TalkPerson = Person;
        TalkPause = Pause;
        nextActionID = ActionID;
    }

    // Personen sprechen (keine Hintergrundanimpersonen !!)
    public void PersonSagt(String HText, String DText, String NText, int Facing, int Person, int Pause, int ActionID, GenericPoint Position) {
        switch (mainFrame.sprache) {
            case 1: // Obersorbisch
                outputText = mainFrame.ifont.TeileText(HText);
                break;

            case 2: // Niedersorbisch
                outputText = mainFrame.ifont.TeileText(DText);
                break;

            case 3: // Deutsch
                outputText = mainFrame.ifont.TeileText(NText);
                break;
        }
        outputTextPos = mainFrame.ifont.CenterText(outputText, Position);
        if (Facing != 0) {
            mainFrame.krabat.SetFacing(Facing);
        }
        TalkPerson = Person;
        TalkPause = Pause;
        nextActionID = ActionID;
    }

    // Neue Location wird erzeugt
    public void NeuesBild(int NewLocation, int OldLocation) {
        mainFrame.isAnim = false;
        mainFrame.fPlayAnim = false;
        mainFrame.Clipset = false;
        mainFrame.ConstructLocation(NewLocation);
        mainFrame.DestructLocation(OldLocation);
        nextActionID = 0;
        mainFrame.repaint();
    }

    // Hier die stets gleichen Aufrufe von Load/Save/Mainmenu/Inventar
    public void SwitchScreen() {
        switch (nextActionID) {
            // Load / Save / Mainmenu / Inventory

            case 120:
                // Load - Screen aktivieren
                nextActionID = 0;
                mainFrame.ConstructLocation(102);
                mainFrame.whatScreen = 3;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 121:
                // Save - Screen aktivieren
                nextActionID = 0;
                mainFrame.StoreImage();
                mainFrame.ConstructLocation(103);
                mainFrame.whatScreen = 4;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 122:
                // Mainmenu aktivieren
                mainFrame.StoreImage();
                mainFrame.whatScreen = 2;
                nextActionID = 0;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 123:
                // Inventar aktivieren
                mainFrame.StoreImage();
                mainFrame.krabat.StopWalking();
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                mainFrame.whatScreen = 1;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche ID in SwitchScreen !");
                break;
        }
    }

    // Hier die abstrakten Locations, die von jeder Location implementiert
    // werden muessen und so auch ueber das Mainloca-Objekt aufgerufen
    // werden koennen.

    abstract public void paintLocation(GenericDrawingContext g);

    abstract public void evalMouseEvent(GenericMouseEvent e);

    abstract public void evalMouseExitEvent(GenericMouseEvent e);

    abstract public void evalMouseMoveEvent(GenericPoint mousePoint);

    abstract public void evalKeyEvent(GenericKeyEvent e);

    // overridden by subclasses if cleaup is useful to perform
    public void cleanup() {
    }
}      	