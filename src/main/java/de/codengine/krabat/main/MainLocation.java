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

package de.codengine.krabat.main;

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class MainLocation {
    private static final Logger log = LoggerFactory.getLogger(MainLocation.class);
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
    private static final String[] EXCUSES_THINGS = {"Mainloc_49", "Mainloc_50", "Mainloc_51", "Mainloc_52", "Mainloc_53"};

    // Variablen fuer Standardausreden Personen

    // maennlich
    private static final String[] EXCUSES_MALE = {"Mainloc_54", "Mainloc_55", "Mainloc_56"};

    // weiblich
    private static final String[] EXCUSES_FEMALE = {"Mainloc_57", "Mainloc_58", "Mainloc_59"};

    // mehrere (groesser 2)
    private static final String[] EXCUSES_MULTI = {"Mainloc_60", "Mainloc_61", "Mainloc_62"};

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
    public MainLocation(Start caller) {
        // tracker = new MediaTracker (this);
        mainFrame = caller;

    }

    // Konstruktor mit Location-ID (SS)
    public MainLocation(Start caller, int idLocation) {
        // tracker = new MediaTracker (this);
        mainFrame = caller;
        locationID = idLocation;
    }

    // Methode zum Laden (registrieren) eines Bildes
    public GenericImage getPicture(String Filename) {
        return mainFrame.imageFetcher.fetchImage(Filename, false);
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
                KrabatSagt("Mainloc_1", 0, 3, 0, 0);
                break;

            case 503:
                // Krabat mit Deska benutzen
                KrabatSagt("Mainloc_2", 0, 3, 0, 0);
                break;

            case 504:
                // Krabat mit Honck benutzen
                KrabatSagt("Mainloc_3", 0, 3, 0, 0);
                break;

            case 505:
                // Krabat mit Hocka benutzen
                KrabatSagt("Mainloc_4", 0, 3, 0, 0);
                break;

            case 506:
                // Krabat mit Lajna benutzen
                KrabatSagt("Mainloc_5", 0, 3, 0, 0);
                break;

            case 507:
                // Krabat mit Wuda benutzen
                KrabatSagt("Mainloc_6", 0, 3, 0, 0);
                break;

            case 508:
                // Krabat mit Wacki benutzen
                KrabatSagt("Mainloc_7", 0, 3, 0, 0);
                break;

            case 509:
                // Krabat mit Wuda + Hocka benutzen
                KrabatSagt("Mainloc_8", 0, 3, 0, 0);
                break;

            case 510:
                // Krabat mit Wuda + Hocka + Wacka benutzen
                KrabatSagt("Mainloc_9", 0, 3, 0, 0);
                break;

            case 511:
                // Krabat mit Wuda + Hocka + Drjewjana Ryba benutzen
                KrabatSagt("Mainloc_10", 0, 3, 0, 0);
                break;

            case 512:
                // Krabat mit Wohnjowe Kamuski benutzen
                mainFrame.krabat.nAnimation = 154;
                nextActionID = 0;
                break;

            case 513:
                // Krabat mit Drjewjana Ryba benutzen
                KrabatSagt("Mainloc_11", 0, 3, 0, 0);
                break;

            case 514:
                // Krabat mit Ryba benutzen
                KrabatSagt("Mainloc_12", 0, 3, 0, 0);
                break;

            case 515:
                // Krabat mit Krosik benutzen
                KrabatSagt("Mainloc_13", 0, 3, 0, 0);
                break;

            case 516:
                // Krabat mit Honck z blotom benutzen
                KrabatSagt("Mainloc_14", 0, 3, 0, 0);
                break;

            case 517:
                // Krabat mit Rohodz benutzen
                // Floete
                mainFrame.krabat.nAnimation = 5;
                nextActionID = 0;
                break;

            case 518:
                // Krabat mit Rohodz + Kamusk benutzen
                KrabatSagt("Mainloc_15", 0, 3, 0, 0);
                break;

            case 519:
                // Krabat mit Pjero benutzen
                KrabatSagt("Mainloc_16", 0, 3, 0, 0);
                break;

            case 520:
                // Karte aktivieren
                mainFrame.ConstructLocation(106);
                mainFrame.isInventoryCursor = false;
                mainFrame.isBackgroundAnimRunning = false;
                mainFrame.whatScreen = ScreenType.MAP;
                nextActionID = 0;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            // Dresdner - Zeug (TEIL 3) - SS

            case 530:
                // List auf Krabat
                KrabatSagt("Mainloc_17", 0, 3, 0, 0);
                break;

            case 531:
                // Krabat mit Dowolnosc pur benutzen
                KrabatSagt("Mainloc_18", 0, 3, 0, 0);
                break;

            case 532:
                // Krabat mit Dowolnosc gesiegelt benutzen
                KrabatSagt("Mainloc_19", 0, 3, 0, 0);
                break;

            case 533:
                // Krabat mit Dowolnosc unterschrieben benutzen
                KrabatSagt("Mainloc_20", 0, 3, 0, 0);
                break;

            case 534:
                // Krabat mit Dowolnosc komplett benutzen
                KrabatSagt("Mainloc_21", 0, 3, 0, 0);
                break;

            case 535:
                // Krabat mit drjewo benutzen
                KrabatSagt("Mainloc_22", 0, 3, 0, 0);
                break;

            case 536:
                // Krabat mit Kozuch benutzen
                KrabatSagt("Mainloc_23", 0, 3, 0, 0);
                break;

            case 537:
                // Krabat mit kotwica benutzen
                KrabatSagt("Mainloc_24", 0, 3, 0, 0);
                break;

            case 538:
                // Krabat mit Lajna benutzen
                KrabatSagt("Mainloc_25", 0, 3, 0, 0);
                break;

            case 539:
                // Krabat mit kotwica + Lajna benutzen
                KrabatSagt("Mainloc_26", 0, 3, 0, 0);
                break;

            case 540:
                // Krabat mit 5 tolerow benutzen
                KrabatSagt("Mainloc_27", 0, 3, 0, 0);
                break;

            case 541:
                // Krabat mit Bedienstetenkleidung benutzen
                KrabatSagt("Mainloc_28", 0, 3, 0, 0);
                break;

            case 542:
                // Krabat mit Hlebija benutzen
                KrabatSagt("Mainloc_29", 0, 3, 0, 0);
                break;

            case 543:
                // Krabat mit Friedhelm benutzen
                KrabatSagt("Mainloc_30", 0, 3, 0, 0);
                break;

            case 544:
                // Krabat mit Wilhelm benutzen
                KrabatSagt("Mainloc_31", 0, 3, 0, 0);
                break;

            case 545:
                // Krabat mit Casnik benutzen
                KrabatSagt("Mainloc_32", 0, 3, 0, 0);
                break;

            case 546:
                // Krabat mit Hamor benutzen
                KrabatSagt("Mainloc_33", 0, 3, 0, 0);
                break;

            case 547:
                // Krabat mit Kluc benutzen
                KrabatSagt("Mainloc_34", 0, 3, 0, 0);
                break;

            case 548:
                // Krabat mit Metall benutzen
                KrabatSagt("Mainloc_35", 0, 3, 0, 0);
                break;

            case 549:
                // Krabat mit Prikaz benutzen
                KrabatSagt("Mainloc_36", 0, 3, 0, 0);
                break;

            case 550:
                // Krabat mit Skica benutzen
                mainFrame.ConstructLocation(108);
                mainFrame.isInventoryCursor = false;
                mainFrame.whatScreen = ScreenType.SKETCH;
                nextActionID = 0;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
//                 KrabatSagt ("Skicu trjeba Dinglinger!",
//                             "Skicu trjeba Dinglinger.",
//                             "Die Skizze braucht Dinglinger!",
//                             0, 3, 0, 0);
                break;

            case 551:
                // Krabat mit halbem Wosusk benutzen
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.nAnimation = 146;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.inventory.vInventory.removeElement(51);
                mainFrame.actions[680] = true;
                nextActionID = 590;
                break;

            case 552:
                // Krabat mit Wosusk benutzen
                KrabatSagt("Mainloc_37", 0, 3, 0, 0);
                break;

            case 553:
                // Krabat mit Drasta benutzen
                KrabatSagt("Mainloc_38", 0, 3, 0, 0);
                break;

            case 554:
                // Krabat mit Karta benutzen
                KrabatSagt("Mainloc_39", 0, 3, 0, 0);
                break;

            case 555:
                // Krabat liest Buch oder hat schon gelesen
                if (mainFrame.actions[955]) {
                    KrabatSagt("Mainloc_40", 0, 3, 0, 0);
                } else {
                    mainFrame.actions[955] = true;
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    KrabatSagt("Mainloc_41", 0, 3, 2, 580);
                }
                break;

            case 560:
                // Schuessel auf K
                KrabatSagt("Mainloc_42", 0, 3, 0, 0);
                break;

            case 561:
                // Koraktor auf K (entfaellt, wird in Rowy abgefangen)
                break;

            case 562:
                // grosser Stein auf K
                KrabatSagt("Mainloc_43", 0, 3, 0, 0);
                break;

            case 563:
                // Stroh auf K
                KrabatSagt("Mainloc_44", 0, 3, 0, 0);
                break;

            // Ab hier Extra-IDs, die nicht an InventarID gebunden sind (Extrawuerste)

            case 580:
                // Krabat liest im schweren Buch
                mainFrame.krabat.nAnimation = 155;
                KrabatSagt("Mainloc_45", 0, 3, 2, 581);
                break;

            case 581:
                // paar Sekunden warten
                Counter = 60;
                nextActionID = 582;
                break;

            case 582:
                // Erzaehlerspruch
                if (--Counter > 1) {
                    break;
                }
                PersonSagt("Mainloc_46", 0, 54, 2, 583, new GenericPoint(320, 200));
                break;

            case 583:
                // Krabat hat zuendegelesen
                mainFrame.krabat.StopAnim();
                KrabatSagt("Mainloc_47", 0, 3, 2, 584);
                break;

            case 584:
                // Ende dieser Anim 
                mainFrame.isInventoryCursor = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 590:
                // Kommentar nach Wosuskessen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                KrabatSagt("Mainloc_48", 6, 3, 0, 0);
                break;

            default:
                log.error("Es fehlt eine Ausrede !!!! {}", nextActionID);
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
    public void DingAusrede(int facing) {
        int random = (int) (Math.random() * (EXCUSES_THINGS.length - 1));
        KrabatSagt(EXCUSES_THINGS[random], facing, 3, 0, 0);
    }

    // fuer eine maennliche Person
    public void MPersonAusrede(int facing) {
        int random = (int) (Math.random() * (EXCUSES_MALE.length - 1));
        KrabatSagt(EXCUSES_MALE[random], facing, 3, 0, 0);
    }

    // fuer eine weibliche Person
    public void WPersonAusrede(int facing) {
        int random = (int) (Math.random() * (EXCUSES_FEMALE.length - 1));
        KrabatSagt(EXCUSES_FEMALE[random], facing, 3, 0, 0);
    }

    // fuer mehr als 2 Personen (A steht fuer All)
    public void APersonAusrede(int facing) {
        int random = (int) (Math.random() * (EXCUSES_MULTI.length - 1));
        KrabatSagt(EXCUSES_MULTI[random], facing, 3, 0, 0);
    }

    // Krabat spricht, entweder allein oder zu jemandem
    public void KrabatSagt(String langKey, int Facing, int Person, int Pause, int ActionID) {
        outputText = mainFrame.imageFont.TeileTextKey(langKey);
        outputTextPos = mainFrame.imageFont.KrabatText(outputText);
        if (Facing != 0) {
            mainFrame.krabat.SetFacing(Facing);
        }
        TalkPerson = Person;
        TalkPause = Pause;
        nextActionID = ActionID;
    }

    // Personen sprechen (keine Hintergrundanimpersonen !!)
    public void PersonSagt(String langKey, int Facing, int Person, int Pause, int ActionID, GenericPoint Position) {
        outputText = mainFrame.imageFont.TeileTextKey(langKey);
        outputTextPos = mainFrame.imageFont.CenterText(outputText, Position);
        if (Facing != 0) {
            mainFrame.krabat.SetFacing(Facing);
        }
        TalkPerson = Person;
        TalkPause = Pause;
        nextActionID = ActionID;
    }

    // Neue Location wird erzeugt
    public void NeuesBild(int NewLocation, int OldLocation) {
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.isAnimRunning = false;
        mainFrame.isClipSet = false;
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
                mainFrame.whatScreen = ScreenType.LOAD_GAME;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 121:
                // Save - Screen aktivieren
                nextActionID = 0;
                mainFrame.StoreImage();
                mainFrame.ConstructLocation(103);
                mainFrame.whatScreen = ScreenType.SAVE_GAME;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 122:
                // Mainmenu aktivieren
                mainFrame.StoreImage();
                mainFrame.whatScreen = ScreenType.MAIN_MENU;
                nextActionID = 0;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 123:
                // Inventar aktivieren
                mainFrame.StoreImage();
                mainFrame.krabat.StopWalking();
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                mainFrame.whatScreen = ScreenType.INVENTORY;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche ID in SwitchScreen! nextActionID = {}", nextActionID);
                break;
        }
    }

    // Hier die abstrakten Locations, die von jeder Location implementiert
    // werden muessen und so auch ueber das Mainloca-Objekt aufgerufen
    // werden koennen.

    abstract public void paintLocation(GenericDrawingContext g);

    abstract public void evalMouseEvent(GenericMouseEvent e);

    abstract public void evalMouseExitEvent();

    abstract public void evalMouseMoveEvent(GenericPoint mousePoint);

    abstract public void evalKeyEvent(GenericKeyEvent e);

    // overridden by subclasses if cleaup is useful to perform
    public void cleanup() {
    }
}      	