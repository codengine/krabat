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
import de.codengine.platform.GenericImageObserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Install extends Mainloc {
    private GenericImage background, Pfeil, DPfeil;
    private final GenericPoint pLO;
    private final Borderrect brPfeil;

    private int menuitem = 0;
    private int olditem = 0;
    private boolean Paintcall = false;

    private byte[] Feld;

    // private int Position;

    private boolean drawMe = false;

    private int selected = 0;
    private int RecChosen = 0;
    private int CdChosen = 0;
    private int SoundChosen = 0;
    private int LicenseChosen = 0;

    private boolean licenseAccepted = false;

    /*
    private static final int MINX = 93;
    private static final int MAXX = 547;
    private static final int MINY = 72;
    private static final int MAXY = 346;
	*/

    private static final int OffsetY = 30;

    private final Borderrect[] Rects;
    private final String[] Texty;

    private final Borderrect[] LizenzRects;
    private final String[] LizenzTexty;

    private static final String Datei = "Krabat.lax";

    private static final String Text1 = "console\r";
    private String Text2;

    private static final String Rec1 = " hs";
    private static final String Rec2 = " ds";

    private static final String CD1 = " krabat.exe";
    private static final String CD2 = " kcdlinux.sh";
    private static final String CD3 = " none";

    private static final String Sound1 = " win auto";   // hintendran noch die Grafik-Variable setzen
    private static final String Sound2 = " java auto";

    private static final String Such1 = "lax.stdout.redirect=";
    private static final String Such2 = "lax.stderr.redirect=";
    private static final String Such3 = "lax.command.line.args=";

    private static final String Such10 = "lax.stdout.redirect=console";
    private static final String Such11 = "lax.stderr.redirect=console";

    private final File name;

    private final GenericImageObserver observer = null;

    // "richtiger" Konstruktor nachher
    public Install(Start caller) {
        super(caller);

        mainFrame.Freeze(true);

        name = new File(Datei);

        nextActionID = 0;

        InitImages();

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);
        brPfeil = new Borderrect(pLO.x + 90, pLO.y + 319, pLO.x + 180, pLO.y + 358);

        Rects = new Borderrect[8];
        Texty = new String[10];

        Texty[1] = "Hornjoserbsce";
        Texty[2] = "Dolnoserbski";
        Texty[3] = "WINDOWS";
        Texty[4] = "UNIX";
        Texty[5] = "#Zadyn";
        Texty[6] = "WINDOWS";
        Texty[7] = "Hina#si";
        Texty[8] = "#Zeden";
        Texty[9] = "Hynak#sy";

        Rects[1] = new Borderrect(93, 130, 93 + mainFrame.ifont.LineLength(Texty[1]), 130 + OffsetY);
        Rects[2] = new Borderrect(320, 130, 320 + mainFrame.ifont.LineLength(Texty[2]), 130 + OffsetY);
        Rects[3] = new Borderrect(93, 200, 93 + mainFrame.ifont.LineLength(Texty[3]), 200 + OffsetY);
        Rects[4] = new Borderrect(250, 200, 250 + mainFrame.ifont.LineLength(Texty[4]), 200 + OffsetY);
        Rects[5] = new Borderrect(350, 200, 350 + mainFrame.ifont.LineLength(Texty[5]), 200 + OffsetY);
        Rects[6] = new Borderrect(93, 270, 93 + mainFrame.ifont.LineLength(Texty[6]), 270 + OffsetY);
        Rects[7] = new Borderrect(250, 270, 250 + mainFrame.ifont.LineLength(Texty[7]), 270 + OffsetY);

        LizenzTexty = new String[5];
        LizenzRects = new Borderrect[5];

        LizenzTexty[1] = "Haj";
        LizenzTexty[2] = "Jo";
        LizenzTexty[3] = "N#e";
        LizenzTexty[4] = "N#e";

        LizenzRects[1] = new Borderrect(93, 160, 93 + mainFrame.ifont.LineLength(LizenzTexty[1]), 160 + OffsetY);
        LizenzRects[2] = new Borderrect(93, 260, 93 + mainFrame.ifont.LineLength(LizenzTexty[2]), 260 + OffsetY);
        LizenzRects[3] = new Borderrect(200, 160, 200 + mainFrame.ifont.LineLength(LizenzTexty[3]), 160 + OffsetY);
        LizenzRects[4] = new Borderrect(200, 260, 200 + mainFrame.ifont.LineLength(LizenzTexty[4]), 260 + OffsetY);

        if (TestInstallFile()) {
            nextActionID = 100;
        } else {
            drawMe = true;
        }

        mainFrame.Freeze(false);
    }


    // Bilder vorbereiten
    public void InitImages() {
        background = getPicture("gfx/mainmenu/info2.gif");
        DPfeil = getPicture("gfx/inventar/d-p-l-i.gif");
        Pfeil = getPicture("gfx/inventar/r-p-l.gif");

    }

    @Override
    public void paintLocation(GenericDrawingContext g) {

        if (!drawMe) {
            DoAction();
            return;
        }

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
        }

        g.drawImage(background, pLO.x, pLO.y, null);

        // Wenn noetig dann Pfeil zeichnen
        if (SoundChosen != 0) {
            // altes Highlight aufheben
            switch (olditem) {
                case 0:
                    break;
                case 1:
                    g.drawImage(DPfeil, 121 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                    break;
                default:
                    System.out.println("Falsches Menu-Item!!!");
            }

            if (olditem != 0) {
                olditem = 0;
            }

            // Wenn noetig, dann highlighten!!!
            switch (menuitem) {
                case 0:
                    break;
                case 1:
                    g.drawImage(Pfeil, 121 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                    break;
                default:
                    System.out.println("Falsches Menu-Item!!!");
            }

            if (menuitem != 0) {
                olditem = menuitem;
            }
        }

        // Ueberschrift anzeigen
        String tempString = "Krabat - Instalacija";
        GenericPoint tempPoint = mainFrame.ifont.CenterAnimText(tempString, new GenericPoint(320, 60));
        mainFrame.ifont.drawString(g, tempString, tempPoint.x, tempPoint.y, FarbenArray[13]);

        // hier die Unterscheidung, ob Lizenz angenommen wurde oder nicht
        int col;
        if (licenseAccepted) {
            // Sprachenchooser immer anzeigen
            mainFrame.ifont.drawString(g, "Pro#su r#e#k wuzwoli#c", 93, 100, FarbenArray[13]);

            mainFrame.ifont.drawString(g, "P#sosym r#ec wuzwoli#y", 320, 100, FarbenArray[13]);

            // Hornjos
            col = FarbenArray[13];
            if ((selected != 1) && (RecChosen != 1)) {
                col = FarbenArray[14];
            }
            mainFrame.ifont.drawString(g, Texty[1], Rects[1].lo_point.x, Rects[1].lo_point.y, col);

            // Delnjos
            col = FarbenArray[13];
            if ((selected != 2) && (RecChosen != 2)) {
                col = FarbenArray[14];
            }
            mainFrame.ifont.drawString(g, Texty[2], Rects[2].lo_point.x, Rects[2].lo_point.y, col);

            // Rest nur zeichnen, wenn fuer eine Sprache entschieden
            if (RecChosen != 0) {
                if (RecChosen == 1) {
                    mainFrame.ifont.drawString(g, "Wuzwol#ce sej CD-player", 93, 170, FarbenArray[13]);
                } else {
                    mainFrame.ifont.drawString(g, "Wuzwol#yo se CD-player", 93, 170, FarbenArray[13]);
                }

                // Win
                col = FarbenArray[13];
                if ((selected != 3) && (CdChosen != 3)) {
                    col = FarbenArray[14];
                }
                mainFrame.ifont.drawString(g, Texty[3], Rects[3].lo_point.x, Rects[3].lo_point.y, col);

                // Lin
                col = FarbenArray[13];
                if ((selected != 4) && (CdChosen != 4)) {
                    col = FarbenArray[14];
                }
                mainFrame.ifont.drawString(g, Texty[4], Rects[4].lo_point.x, Rects[4].lo_point.y, col);

                // Keiner
                col = FarbenArray[13];
                if ((selected != 5) && (CdChosen != 5)) {
                    col = FarbenArray[14];
                }
                mainFrame.ifont.drawString(g, Texty[(RecChosen == 1) ? 5 : 8], Rects[5].lo_point.x, Rects[5].lo_point.y, col);

                // Sound nur zeichnen, wenn CD ausgewaehlt
                if (CdChosen != 0) {
                    if (RecChosen == 1) {
                        mainFrame.ifont.drawString(g, "Wuzwol#ce sej sound-system", 93, 240, FarbenArray[13]);
                    } else {
                        mainFrame.ifont.drawString(g, "Wuzwol#yo se sound-system", 93, 240, FarbenArray[13]);
                    }

                    // Win
                    col = FarbenArray[13];
                    if ((selected != 6) && (SoundChosen != 6)) {
                        col = FarbenArray[14];
                    }
                    mainFrame.ifont.drawString(g, Texty[6], Rects[6].lo_point.x, Rects[6].lo_point.y, col);

                    // Java
                    col = FarbenArray[13];
                    if ((selected != 7) && (SoundChosen != 7)) {
                        col = FarbenArray[14];
                    }
                    mainFrame.ifont.drawString(g, Texty[(RecChosen == 1) ? 7 : 9], Rects[7].lo_point.x, Rects[7].lo_point.y, col);

                    // "Done" nur zeichnen, wenn alles andere da
                    if (SoundChosen != 0) {
                        if (RecChosen == 1) {
                            mainFrame.ifont.drawString(g, "Krabat znowa startowa#c", 93, 310, FarbenArray[13]);
                        } else {
                            mainFrame.ifont.drawString(g, "Krabata wotnowotki startowa#y", 93, 310, FarbenArray[13]);
                        }

                    }
                }
            }
        } else {
            // LizenzabfrageText zeichnen (in 2 Sprachen)
            mainFrame.ifont.drawString(g, "Akceptuje#ce w#sitke dypki licencneho zr#e#kenja,$kotre#z namaka#ce w p#ri#loze CDje?", 93, 100, FarbenArray[13]);
            mainFrame.ifont.drawString(g, "Akceptujo#yo w#sykne dypki licencnego$dogrona, kotare#z namakajo#yo w p#yi#loze CDje?", 73, 200, FarbenArray[13]);

            col = FarbenArray[13];
            if (LicenseChosen != 1) {
                col = FarbenArray[14];
            }
            mainFrame.ifont.drawString(g, LizenzTexty[1], LizenzRects[1].lo_point.x, LizenzRects[1].lo_point.y, col);
            mainFrame.ifont.drawString(g, LizenzTexty[2], LizenzRects[2].lo_point.x, LizenzRects[2].lo_point.y, col);

            col = FarbenArray[13];
            if (LicenseChosen != 2) {
                col = FarbenArray[14];
            }
            mainFrame.ifont.drawString(g, LizenzTexty[3], LizenzRects[3].lo_point.x, LizenzRects[3].lo_point.y, col);
            mainFrame.ifont.drawString(g, LizenzTexty[4], LizenzRects[4].lo_point.x, LizenzRects[4].lo_point.y, col);
        }


        // Gibt es was zu tun ?
        if (nextActionID != 0) {
            DoAction();
        }
    }

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();

        if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
            if (licenseAccepted) {
                // linke Maustaste waehlt aus, hier im richtigen Menue
                if (Rects[1].IsPointInRect(pTemp)) {
                    RecChosen = 1;
                }
                if (Rects[2].IsPointInRect(pTemp)) {
                    RecChosen = 2;
                }
                if ((Rects[3].IsPointInRect(pTemp)) && (RecChosen != 0)) {
                    CdChosen = 3;
                }
                if ((Rects[4].IsPointInRect(pTemp)) && (RecChosen != 0)) {
                    CdChosen = 4;
                }
                if ((Rects[5].IsPointInRect(pTemp)) && (RecChosen != 0)) {
                    CdChosen = 5;
                }
                if ((Rects[6].IsPointInRect(pTemp)) && (CdChosen != 0)) {
                    SoundChosen = 6;
                }
                if ((Rects[7].IsPointInRect(pTemp)) && (CdChosen != 0)) {
                    SoundChosen = 7;
                }
                if ((brPfeil.IsPointInRect(pTemp)) && (SoundChosen != 0)) {
                    Text2 = "";
                    if (RecChosen == 1) {
                        Text2 += Rec1;
                    }
                    if (RecChosen == 2) {
                        Text2 += Rec2;
                    }

                    if (CdChosen == 3) {
                        Text2 += CD1;
                    }
                    if (CdChosen == 4) {
                        Text2 += CD2;
                    }
                    if (CdChosen == 5) {
                        Text2 += CD3;
                    }

                    if (SoundChosen == 6) {
                        Text2 += Sound1;
                    }
                    if (SoundChosen == 7) {
                        Text2 += Sound2;
                    }

                    Text2 += "\r";

                    ModifyInstallFile();
                    System.exit(0);
                }
            } else {
                // hier das	Lizenzabfragemenu
                if ((LizenzRects[1].IsPointInRect(pTemp)) || (LizenzRects[2].IsPointInRect(pTemp))) {
                    licenseAccepted = true;
                }

                if ((LizenzRects[3].IsPointInRect(pTemp)) || (LizenzRects[4].IsPointInRect(pTemp))) {
                    System.exit(0);
                }
            }

            mainFrame.Clipset = false;
            mainFrame.repaint();
        } else {
            // rechte Maustaste tut nix
        }
    }

    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // sonst normal-Cursor
        if (Cursorform != 0) {
            mainFrame.setCursor(mainFrame.Normal);
            Cursorform = 0;
        }

        int oldsel = selected;
        selected = 0;

        // Move Highlighted

        for (int i = 1; i < Rects.length; i++) {
            if (Rects[i].IsPointInRect(pTemp)) {
                selected = i;
            }
        }

        int OldLicense = LicenseChosen;
        LicenseChosen = 0;

        for (int i = 1; i < LizenzRects.length; i++) {
            if (LizenzRects[i].IsPointInRect(pTemp)) {
                LicenseChosen = i;
            }
        }
        if (LicenseChosen == 2) {
            LicenseChosen = 1;
        }
        if (LicenseChosen > 2) {
            LicenseChosen = 2;
        }

        menuitem = 0;
        if (brPfeil.IsPointInRect(pTemp)) {
            menuitem = 1;
        }
        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
            return;
        }
        if ((menuitem != olditem) || (selected != oldsel) || (LicenseChosen != OldLicense)) {
            mainFrame.repaint();
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
        selected = 0;
        mainFrame.repaint();
    }

    // keine Keys im Installer
    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    private void DoAction() {
        switch (nextActionID) {
            case 100:
                // Installer ist schon gelaufen, wir koennen Krabat starten
                mainFrame.Clipset = false;
                nextActionID = 0;
                mainFrame.ConstructLocation(100);
                mainFrame.DestructLocation(105);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Wrong ID in Installer !!");
                break;
        }
    }

    // Methode, die testet, ob Installer ueberhaupt noch noetig ist
    private boolean TestInstallFile() {
        // keine Datei gefunden, also Fehler !
        if (!(LoadFile())) {
            return false;
        }

        if (SearchStringInByte(Such10) == 0) {
            return false;
        }
        return SearchStringInByte(Such11) != 0;

        // beides gefunden, also Installer schon Mal gelaufen...
    }

    // Methode, die das komplette Aendern des Files uebernimmt
    private boolean ModifyInstallFile() {
        // zuerst Datei reinladen
        if (!(LoadFile())) {
            return false;
        }

        // die beiden "console" - Ausdruecke einfuegen
        int erster = SearchStringInByte(Such1);
        int wieweit = SucheEnter(erster);
        System.out.println("Es steht was da von " + erster + " " + wieweit);
        Loesche(erster, (wieweit - erster + 1));
        Einfuege(erster, Text1.length());
        Einsetze(Text1, erster);

        // die beiden "console" - Ausdruecke einfuegen
        int zweiter = SearchStringInByte(Such2);
        int wiefar = SucheEnter(zweiter);
        Loesche(zweiter, (wiefar - zweiter + 1));
        Einfuege(zweiter, Text1.length());
        Einsetze(Text1, zweiter);

        // hier die Kommandozeilenargumente einfuegen
        int dritter = SearchStringInByte(Such3);
        int wiedale = SucheEnter(dritter);
        Loesche(dritter, (wiedale - dritter + 1));
        Einfuege(dritter, Text2.length());
        Einsetze(Text2, dritter);

        return SaveFile();
    }

    // File wird gesucht und geladen
    private boolean LoadFile() {
        if (!name.exists()) {
            System.out.println("Fehlerhafte Installation - Krabat.lax fehlt !");
            return false;
        }

        if (!name.canWrite()) {
            System.out.println("Kann Krabat.lax nicht veraendern !");
            return false;
        }

        // Array initialisieren je nach Laenge von Krabat.Iax
        long Laenge = name.length();
        Feld = new byte[(int) (Laenge + 100)]; // bisschen Reserve fuer das, was noch kommt...

        for (int i = 0; i < Feld.length; i++) {
            Feld[i] = 32;
        }

        try {
            FileInputStream Data = new FileInputStream(name);
            // Position = Data.read (Feld);
            Data.close();
        } catch (IOException e) {
            System.out.println("File read error " + e);
            // Data.close();
            return false;
        }

        return true;
    }

    // File wird zurueckgeschrieben
    private boolean SaveFile() {
        // File speichern
        try {
            FileOutputStream Data = new FileOutputStream(name);
            Data.write(Feld);
            Data.close();
        } catch (IOException e) {
            System.out.println("File write error " + e);
            // Data.close();
            return false;
        }
        return true;
    }

    // sucht die Stelle, wo String vorkommt (1. Treffer wird gezaehlt) 
    // und gibt den DARAUFFOLGENDEN INDEX im Byte - Feld an
    private int SearchStringInByte(String which) {
        int j = 0;
        boolean exit = false;

        // komplettes Feld absuchen
        for (int i = 0; i < Feld.length; i++) {
            // String testen
            j = 0;
            exit = false;

            // Schleife, die Test durchfuehrt
            do {
                if (which.charAt(j) != Feld[i + j]) {
                    exit = true;
                }
                j++;
            }
            while ((!exit) && (j < which.length()));

            if (!exit) {
                System.out.println("String wurde gefunden !");
                return (i + j);
            }
        }

        System.out.println("String nicht gefunden !");
        return (0);
    }

    // Kopiert Array "nach hinten", damit Buchstaben eingefuegt werden koennen  
    private void Einfuege(int Position, int Wieviel) {
        for (int i = Feld.length - Wieviel - 1; i >= Position; i--) {
            Feld[i + Wieviel] = Feld[i];
        }
    }

    // Kopiert Array "nach Vorn", um Text zu loeschen		
    private void Loesche(int Position, int Wieviel) {
        for (int i = Position; i < (Feld.length - Wieviel - 1); i++) {
            // System.out.println ("Index " + i + "Maximal " + Feld.length + "Offset " + Wieviel);
            Feld[i] = Feld[i + Wieviel];
        }
    }

    // setzt String in Byte - Array ein
    private void Einsetze(String Text, int Position) {
        for (int i = 0; i < Text.length(); i++) {
            Feld[Position + i] = (byte) Text.charAt(i);
        }
    }

    // suche naechstes "Enter", entweder "0d" fuer win oder "0a" fuer andere
    private int SucheEnter(int Position) {
        for (int i = Position; i < Feld.length; i++) {
            if ((Feld[i] == 13) || (Feld[i] == 10)) {
                return i;
            }
        }
        return (0);
    }
}	