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
import de.codengine.platform.GenericImage;

import java.util.Vector;

public class Spielstand {
    public int Location;
    private GenericPoint Krabatpos;
    private int isHornjos;
    private int Day, Month, Year;
    private Vector<Integer> Inventar;
    private int Scrolling;
    private int Facing;
    private boolean[] Aktionen;
    private int[] Bild;
    public GenericImage Picture, DarkPicture;
    private Start mainFrame;
    // private int Groesse = 44100;
    private static final byte W = -128;

    // Konstruktor fuer diese Klasse
    public Spielstand(Start caller) {
        mainFrame = caller;
    }

    public Spielstand(Start caller, int[] Data, int Tag, int Monat, int Jahr) {
        mainFrame = caller;
        Bild = new int[10593];
        for (int i = 0; i <= 10592; i++) {
            Bild[i] = Data[i];
        }
        Day = Tag;
        Month = Monat;
        Year = Jahr;
        DoImages();
    }

    public String ConvertTime() {
        String Time = "";
        if (Day < 10) {
            Time += "0";
        }
        Time += Integer.toString(Day);
        Time += ".";
        if (Month < 10) {
            Time += "0";
        }
        Time += Integer.toString(Month);
        Time += ".";
        Time += Integer.toString(Year);
        return (Time);
    }

    public void GetSavedSpiel(int i) {
        // String File = "hry/krabat"+ (char) i +".hra";
        byte[] Feld;
        int Pos = 0;
        Inventar = new Vector<Integer>();
        Aktionen = new boolean[1002];
        Bild = new int[10593];
        int Checksumme = 0;
        int Where = 0;
        Feld = mainFrame.storageManager.loadFromFile(i);
        Pos = Feld.length;

        // System.out.println(Pos + "Datei wurde geladen");
        if ((Pos > (mainFrame.storageManager.getFileSize() - 3)) && (Pos < (mainFrame.storageManager.getFileSize() + 3))) {
            // wenn Datei geladen, dann Einlesen der Werte

            // Checksumme ueberpruefen
            for (int d = 0; d <= Pos; d++) {
                // if (d < 20)
                //{
                //System.out.print((int) Feld[d] + " ");
                //}
                if ((Feld[d + 1] != W) || (d < 43381)) {
                    Checksumme ^= (Feld[d] - W) % 256;
                } else {
                    Where = d;
                    break;
                }
            }
            // System.out.println("Checksumme : " + Checksumme + " Feld : " + (Feld[Where] - W) + " Pos : " + Where);
            if (Checksumme == (Feld[Where] - W)) {
                // System.out.println("Verified !");
            } else {
                // System.out.println("Dieser Spielstand wurde manipuliert!");
                Feld[0] = W;
            }
            Feld[Where] = W;

            // Location einlesen
            Location = Feld[0] - W;

            // Krabats Position einlesen
            Krabatpos = new GenericPoint(((Feld[1] - W) * 256) + (Feld[2] - W),
                    ((Feld[3] - W) * 256) + (Feld[4] - W));

            // Sprache einlesen
            isHornjos = Feld[5] - W;

            // Datum einlesen
            Day = Feld[6] - W;
            Month = Feld[7] - W;
            Year = ((Feld[8] - W) * 256) + (Feld[9] - W);

            // Bild einlesen
            int pxx = 0;
            for (int x = 10; x <= 42370; x += 4) {
                Bild[pxx] = (Feld[x] - W) | ((Feld[x + 1] - W) << 8) | ((Feld[x + 2] - W) << 16) | ((Feld[x + 3] - W) << 24);
                pxx++;
            }

            // Scrolling, Facing - Variablen
            Scrolling = ((Feld[42374] - W) * 256) + (Feld[42375] - W);
            Facing = (Feld[42376] - W);

            // Actions - Boolean - array
            for (int l = 42380; l <= 43379; l++) {
                if ((Feld[l] - W) == 0) {
                    Aktionen[l - 42380] = false;
                } else {
                    Aktionen[l - 42380] = true;
                }
            }

            // Inventarvektor einlesen
            for (int r = 43381; r <= Pos; ++r) {
                if (Feld[r] == W) {
                    break;
                }
                Inventar.addElement(new Integer((Feld[r] - W)));
            }

            // verkleinerte Bilder fuer Screen erzeugen
            DoImages();
        }

        // ansonsten Spielstand als ungueltig markieren
        else {
            Location = 0;
        }
    }

    private void DoImages() {
        // normales GenericImage erzeugen
        int[] tempx = new int[10592];
        for (int f = 0; f <= 10591; ++f) {
            tempx[f] = Bild[f];
        }
        Picture = GenericToolkit.getDefaultToolkit().createImage(new GenericMemoryImageSource
                (118, 89, tempx, 0, 118));

        // Geisterimage erzeugen
        int[] tempy = new int[10592];
        int zaehl = 0;
        for (int f = 0; f <= 10588; f += 2) {
            tempy[f] = -16777215;
            tempy[f + 1] = tempx[f + 1];
            zaehl++;
            if (zaehl == 60) {
                f--;
            }
            if (zaehl == 118) {
                tempy[f + 2] = -16777215;
                f++;
                zaehl = 0;
            }
        }
        DarkPicture = GenericToolkit.getDefaultToolkit().createImage(new GenericMemoryImageSource
                (118, 89, tempy, 0, 118));
    }

    public synchronized void Save(int Stand) {
        mainFrame.Freeze(true);
        // String File = "hry/krabat"+ (char) (Stand + 48) +".hra";
        byte[] Feld = new byte[mainFrame.storageManager.getFileSize()];

        // Location zuweisen
        Feld[0] = (byte) (mainFrame.currLocation + W);

        // Krabats Position zuweisen
        GenericPoint Tep = mainFrame.krabat.GetKrabatPos();
        Feld[1] = (byte) ((Tep.x / 256) + W);
        Feld[2] = (byte) ((Tep.x % 256) + W);
        Feld[3] = (byte) ((Tep.y / 256) + W);
        Feld[4] = (byte) ((Tep.y % 256) + W);

        // Sprache zuweisen
        Feld[5] = (byte) (mainFrame.sprache + W);

        // Datum zuweisen
        Feld[6] = (byte) (Day + W);
        Feld[7] = (byte) (Month + W);
        Feld[8] = (byte) ((Year / 256) + W);
        Feld[9] = (byte) ((Year % 256) + W);

        // Bild zerlegen und zuweisen
        int pxx = 0;
        for (int i = 0; i <= 42360; i += 4) {
            Feld[i + 10] = (byte) ((Bild[pxx] & 255) + W);
            Feld[i + 11] = (byte) (((Bild[pxx] >> 8) & 255) + W);
            Feld[i + 12] = (byte) (((Bild[pxx] >> 16) & 255) + W);
            Feld[i + 13] = (byte) (((Bild[pxx] >> 24) & 255) + W);
            pxx++;
        }

        // Scrolling - Variable zuweisen
        Feld[42374] = (byte) ((mainFrame.scrollx / 256) + W);
        Feld[42375] = (byte) ((mainFrame.scrollx % 256) + W);

        // Facing - Variable zuweisen
        Feld[42376] = (byte) ((mainFrame.krabat.GetFacing()) + W);

        // Boolean - Array Actions zuweisen
        for (int i = 42380; i <= 43379; i++) {
            if (mainFrame.Actions[i - 42380] == false) {
                Feld[i] = (byte) W;
            } else {
                Feld[i] = (byte) (1 + W);
            }
        }

        // Inventar zuweisen
        int nAnzahl = mainFrame.inventory.vInventory.size();
        for (int x = 0; x < nAnzahl; x++) {
            int iTemp = ((Integer) mainFrame.inventory.vInventory.elementAt(x)).intValue();
            Feld[x + 43381] = (byte) (iTemp + W);
            Feld[x + 43382] = (byte) W;
        }

        // Checksumme erzeugen und hinzufuegen
        int Checksum = 0;
        int undwo = 0;
        for (int e = 0; e <= 44100; e++) {
            // if (e < 20)
            //{
            // System.out.print((int) Feld[e] + " ");
            //}
            if ((Feld[e] != W) || (e < 43381)) {
                Checksum ^= (Feld[e] + W) % 256;
            } else {
                undwo = e;
                break;
            }
        }
        Feld[undwo] = (byte) (Checksum + W);
        Feld[undwo + 1] = (byte) W;
        // System.out.println("Checksumme : " + Checksum + " Feld : " + (Feld[undwo] + W) + " Pos : " + (undwo));

        boolean success = mainFrame.storageManager.saveToFile(Feld, Stand);
        if (success == false) {
            System.out.println("File save error!");
        }

        // System.out.println("Checksumme : " + Checksum + " Feld : " + (int) Feld[undwo] + " Pos : " + (undwo));
        mainFrame.Freeze(false);
        mainFrame.setCursor(mainFrame.Normal);
    }

    // Neuen Spielstand initialisieren
    public synchronized void Load() {
        mainFrame.Freeze(true);

        // Sprache festlegen
        mainFrame.sprache = isHornjos;

        // "illegale" Sprache verhindern
        // if (mainFrame.sprache > 2) mainFrame.sprache = 1;

        // Inventar setzen
        mainFrame.inventory.vInventory = Inventar;

        // Scrolling - Variable setzen
        mainFrame.scrollx = Scrolling;

        // Aktionen festlegen
        mainFrame.Actions = Aktionen;

        // hier schon der Krabatinit-damit Loadberechnungen abh. von Krabatposition
        // ueberhaupt eine Chance haben
        // Krabats Position setzen
        mainFrame.krabat.SetKrabatPos(Krabatpos);

        // Krabats Blickrichtung festlegen
        mainFrame.krabat.SetFacing(Facing);

        // alte Location zerstoeren, neue erzeugen
        mainFrame.DestructLocation(mainFrame.currLocation);
        mainFrame.currLocation = 0;    // fuer Krabatpositionsinit darf keine alte Location erscheinen
        mainFrame.ConstructLocation(Location);

        mainFrame.Freeze(false);
    }
}  