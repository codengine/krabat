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

import de.codengine.krabat.Start;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class SavegameData {
    private static final Logger log = LoggerFactory.getLogger(SavegameData.class);
    public int Location;
    private GenericPoint Krabatpos;
    private int isHornjos;
    private int Day;
    private int Month;
    private int Year;
    private Vector<Integer> Inventar;
    private int Scrolling;
    private int Facing;
    private boolean[] Aktionen;
    private int[] Bild;
    public GenericImage Picture;
    public GenericImage DarkPicture;
    private final Start mainFrame;
    // private int Groesse = 44100;
    private static final byte W = -128;

    // Konstruktor fuer diese Klasse
    public SavegameData(Start caller) {
        mainFrame = caller;
    }

    public SavegameData(Start caller, int[] Data, int Tag, int Monat, int Jahr) {
        mainFrame = caller;
        Bild = new int[10593];
        System.arraycopy(Data, 0, Bild, 0, 10593);
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
        return Time;
    }

    public void GetSavedSpiel(int i) {
        // String File = "hry/krabat"+ (char) i +".hra";
        byte[] Feld;
        int Pos;
        Inventar = new Vector<>();
        Aktionen = new boolean[1002];
        Bild = new int[10593];
        int Checksumme = 0;
        int Where = 0;
        Feld = mainFrame.storageManager.loadFromFile(i);
        Pos = Feld.length;

        // System.out.println(Pos + "Datei wurde geladen");
        if (Pos > mainFrame.storageManager.getFileSize() - 3 && Pos < mainFrame.storageManager.getFileSize() + 3) {
            // wenn Datei geladen, dann Einlesen der Werte

            // Checksumme ueberpruefen
            for (int d = 0; d <= Pos; d++) {
                // if (d < 20)
                //{
                //System.out.print((int) Feld[d] + " ");
                //}
                if (Feld[d + 1] != W || d < 43381) {
                    Checksumme ^= (Feld[d] - W) % 256;
                } else {
                    Where = d;
                    break;
                }
            }
            // System.out.println("Checksumme : " + Checksumme + " Feld : " + (Feld[Where] - W) + " Pos : " + Where);
            if (Checksumme != Feld[Where] - W) {
                // System.out.println("Dieser Spielstand wurde manipuliert!");
                Feld[0] = W;
            }
            Feld[Where] = W;

            // Location einlesen
            Location = Feld[0] - W;

            // Krabats Position einlesen
            Krabatpos = new GenericPoint((Feld[1] - W) * 256 + Feld[2] - W,
                    (Feld[3] - W) * 256 + Feld[4] - W);

            // Sprache einlesen
            isHornjos = Feld[5] - W;

            // Datum einlesen
            Day = Feld[6] - W;
            Month = Feld[7] - W;
            Year = (Feld[8] - W) * 256 + Feld[9] - W;

            // Bild einlesen
            int pxx = 0;
            for (int x = 10; x <= 42370; x += 4) {
                Bild[pxx] = Feld[x] - W | Feld[x + 1] - W << 8 | Feld[x + 2] - W << 16 | Feld[x + 3] - W << 24;
                pxx++;
            }

            // Scrolling, Facing - Variablen
            Scrolling = (Feld[42374] - W) * 256 + Feld[42375] - W;
            Facing = Feld[42376] - W;

            // Actions - Boolean - array
            for (int l = 42380; l <= 43379; l++) {
                Aktionen[l - 42380] = Feld[l] - W != 0;
            }

            // Inventarvektor einlesen
            for (int r = 43381; r <= Pos; ++r) {
                if (Feld[r] == W) {
                    break;
                }
                Inventar.addElement(Feld[r] - W);
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
        System.arraycopy(Bild, 0, tempx, 0, 10592);
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
        mainFrame.freeze(true);
        // String File = "hry/krabat"+ (char) (Stand + 48) +".hra";
        byte[] Feld = new byte[mainFrame.storageManager.getFileSize()];

        // Location zuweisen
        Feld[0] = (byte) (mainFrame.currentLocationIdx + W);

        // Krabats Position zuweisen
        GenericPoint Tep = mainFrame.krabat.getPos();
        Feld[1] = (byte) (Tep.x / 256 + W);
        Feld[2] = (byte) (Tep.x % 256 + W);
        Feld[3] = (byte) (Tep.y / 256 + W);
        Feld[4] = (byte) (Tep.y % 256 + W);

        // Sprache zuweisen
        Feld[5] = (byte) (Start.language + W);

        // Datum zuweisen
        Feld[6] = (byte) (Day + W);
        Feld[7] = (byte) (Month + W);
        Feld[8] = (byte) (Year / 256 + W);
        Feld[9] = (byte) (Year % 256 + W);

        // Bild zerlegen und zuweisen
        int pxx = 0;
        for (int i = 0; i <= 42360; i += 4) {
            Feld[i + 10] = (byte) ((Bild[pxx] & 255) + W);
            Feld[i + 11] = (byte) ((Bild[pxx] >> 8 & 255) + W);
            Feld[i + 12] = (byte) ((Bild[pxx] >> 16 & 255) + W);
            Feld[i + 13] = (byte) ((Bild[pxx] >> 24 & 255) + W);
            pxx++;
        }

        // Scrolling - Variable zuweisen
        Feld[42374] = (byte) (mainFrame.scrollX / 256 + W);
        Feld[42375] = (byte) (mainFrame.scrollX % 256 + W);

        // Facing - Variable zuweisen
        Feld[42376] = (byte) (mainFrame.krabat.GetFacing() + W);

        // Boolean - Array Actions zuweisen
        for (int i = 42380; i <= 43379; i++) {
            if (!mainFrame.actions[i - 42380]) {
                Feld[i] = W;
            } else {
                Feld[i] = (byte) (1 + W);
            }
        }

        // Inventar zuweisen
        int nAnzahl = mainFrame.inventory.vInventory.size();
        for (int x = 0; x < nAnzahl; x++) {
            int iTemp = mainFrame.inventory.vInventory.elementAt(x);
            Feld[x + 43381] = (byte) (iTemp + W);
            Feld[x + 43382] = W;
        }

        // Checksumme erzeugen und hinzufuegen
        int Checksum = 0;
        int undwo = 0;
        for (int e = 0; e <= 44100; e++) {
            // if (e < 20)
            //{
            // System.out.print((int) Feld[e] + " ");
            //}
            if (Feld[e] != W || e < 43381) {
                Checksum ^= (Feld[e] + W) % 256;
            } else {
                undwo = e;
                break;
            }
        }
        Feld[undwo] = (byte) (Checksum + W);
        Feld[undwo + 1] = W;
        // System.out.println("Checksumme : " + Checksum + " Feld : " + (Feld[undwo] + W) + " Pos : " + (undwo));

        boolean success = mainFrame.storageManager.saveToFile(Feld, Stand);
        if (!success) {
            log.error("File save error!");
        }

        // System.out.println("Checksumme : " + Checksum + " Feld : " + (int) Feld[undwo] + " Pos : " + (undwo));
        mainFrame.freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    // Neuen Spielstand initialisieren
    public synchronized void Load() {
        mainFrame.freeze(true);

        // Sprache festlegen
        Start.language = isHornjos;

        // "illegale" Sprache verhindern
        // if (mainFrame.sprache > 2) mainFrame.sprache = 1;

        // Inventar setzen
        mainFrame.inventory.vInventory = Inventar;

        // Scrolling - Variable setzen
        mainFrame.scrollX = Scrolling;

        // Aktionen festlegen
        mainFrame.actions = Aktionen;

        // hier schon der Krabatinit-damit Loadberechnungen abh. von Krabatposition
        // ueberhaupt eine Chance haben
        // Krabats Position setzen
        mainFrame.krabat.setPos(Krabatpos);

        // Krabats Blickrichtung festlegen
        mainFrame.krabat.SetFacing(Facing);

        // alte Location zerstoeren, neue erzeugen
        mainFrame.destructLocation(mainFrame.currentLocationIdx);
        mainFrame.currentLocationIdx = 0;    // fuer Krabatpositionsinit darf keine alte Location erscheinen
        mainFrame.constructLocation(Location);

        mainFrame.freeze(false);
    }
}  