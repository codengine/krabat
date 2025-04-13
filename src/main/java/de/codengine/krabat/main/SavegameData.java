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
    public int location;
    private GenericPoint krabatPos;
    private int isHornjos;
    private int day;
    private int month;
    private int year;
    private Vector<Integer> inventory;
    private int scrolling;
    private int facing;
    private boolean[] actions;
    private int[] imageData;
    public GenericImage image;
    public GenericImage darkImage;
    private final Start mainFrame;
    private static final byte WIDTH = -128;

    // Konstruktor fuer diese Klasse
    public SavegameData(Start caller) {
        mainFrame = caller;
    }

    public SavegameData(Start caller, int[] data, int day, int month, int year) {
        mainFrame = caller;
        imageData = new int[10593];
        System.arraycopy(data, 0, imageData, 0, 10593);
        this.day = day;
        this.month = month;
        this.year = year;
        doImages();
    }

    public String convertTime() {
        String time = "";
        if (day < 10) {
            time += "0";
        }
        time += Integer.toString(day);
        time += ".";
        if (month < 10) {
            time += "0";
        }
        time += Integer.toString(month);
        time += ".";
        time += Integer.toString(year);
        return time;
    }

    public void getSavedGame(int i) {
        byte[] field;
        int pos;
        inventory = new Vector<>();
        actions = new boolean[1002];
        imageData = new int[10593];
        int checksum = 0;
        int where = 0;
        field = mainFrame.storageManager.loadFromFile(i);
        pos = field.length;

        if (pos > mainFrame.storageManager.getFileSize() - 3 && pos < mainFrame.storageManager.getFileSize() + 3) {
            // wenn Datei geladen, dann Einlesen der Werte

            // Checksumme ueberpruefen
            for (int d = 0; d <= pos; d++) {
                if (field[d + 1] != WIDTH || d < 43381) {
                    checksum ^= (field[d] - WIDTH) % 256;
                } else {
                    where = d;
                    break;
                }
            }
            if (checksum != field[where] - WIDTH) {
                field[0] = WIDTH;
            }
            field[where] = WIDTH;

            // Location einlesen
            location = field[0] - WIDTH;

            // Krabats Position einlesen
            krabatPos = new GenericPoint((field[1] - WIDTH) * 256 + field[2] - WIDTH,
                    (field[3] - WIDTH) * 256 + field[4] - WIDTH);

            // Sprache einlesen
            isHornjos = field[5] - WIDTH;

            // Datum einlesen
            day = field[6] - WIDTH;
            month = field[7] - WIDTH;
            year = (field[8] - WIDTH) * 256 + field[9] - WIDTH;

            // Bild einlesen
            int pxx = 0;
            for (int x = 10; x <= 42370; x += 4) {
                imageData[pxx] = field[x] - WIDTH | field[x + 1] - WIDTH << 8 | field[x + 2] - WIDTH << 16 | field[x + 3] - WIDTH << 24;
                pxx++;
            }

            // Scrolling, Facing - Variablen
            scrolling = (field[42374] - WIDTH) * 256 + field[42375] - WIDTH;
            facing = field[42376] - WIDTH;

            // Actions - Boolean - array
            for (int l = 42380; l <= 43379; l++) {
                actions[l - 42380] = field[l] - WIDTH != 0;
            }

            // Inventarvektor einlesen
            for (int r = 43381; r <= pos; ++r) {
                if (field[r] == WIDTH) {
                    break;
                }
                inventory.addElement(field[r] - WIDTH);
            }

            // verkleinerte Bilder fuer Screen erzeugen
            doImages();
        }

        // ansonsten Spielstand als ungueltig markieren
        else {
            location = 0;
        }
    }

    private void doImages() {
        // normales GenericImage erzeugen
        int[] tempx = new int[10592];
        System.arraycopy(imageData, 0, tempx, 0, 10592);
        image = GenericToolkit.getDefaultToolkit().createImage(new GenericMemoryImageSource
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
        darkImage = GenericToolkit.getDefaultToolkit().createImage(new GenericMemoryImageSource
                (118, 89, tempy, 0, 118));
    }

    public synchronized void save(int slot) {
        mainFrame.freeze(true);
        byte[] field = new byte[mainFrame.storageManager.getFileSize()];

        // Location zuweisen
        field[0] = (byte) (mainFrame.currentLocationIdx + WIDTH);

        // Krabats Position zuweisen
        GenericPoint krabatPos = mainFrame.krabat.getPos();
        field[1] = (byte) (krabatPos.x / 256 + WIDTH);
        field[2] = (byte) (krabatPos.x % 256 + WIDTH);
        field[3] = (byte) (krabatPos.y / 256 + WIDTH);
        field[4] = (byte) (krabatPos.y % 256 + WIDTH);

        // Sprache zuweisen
        field[5] = (byte) (Start.LANGUAGE + WIDTH);

        // Datum zuweisen
        field[6] = (byte) (day + WIDTH);
        field[7] = (byte) (month + WIDTH);
        field[8] = (byte) (year / 256 + WIDTH);
        field[9] = (byte) (year % 256 + WIDTH);

        // Bild zerlegen und zuweisen
        int pxx = 0;
        for (int i = 0; i <= 42360; i += 4) {
            field[i + 10] = (byte) ((imageData[pxx] & 255) + WIDTH);
            field[i + 11] = (byte) ((imageData[pxx] >> 8 & 255) + WIDTH);
            field[i + 12] = (byte) ((imageData[pxx] >> 16 & 255) + WIDTH);
            field[i + 13] = (byte) ((imageData[pxx] >> 24 & 255) + WIDTH);
            pxx++;
        }

        // Scrolling - Variable zuweisen
        field[42374] = (byte) (mainFrame.scrollX / 256 + WIDTH);
        field[42375] = (byte) (mainFrame.scrollX % 256 + WIDTH);

        // Facing - Variable zuweisen
        field[42376] = (byte) (mainFrame.krabat.getFacing() + WIDTH);

        // Boolean - Array Actions zuweisen
        for (int i = 42380; i <= 43379; i++) {
            if (!mainFrame.actions[i - 42380]) {
                field[i] = WIDTH;
            } else {
                field[i] = (byte) (1 + WIDTH);
            }
        }

        // Inventar zuweisen
        int nAnzahl = mainFrame.inventory.vInventory.size();
        for (int x = 0; x < nAnzahl; x++) {
            int iTemp = mainFrame.inventory.vInventory.elementAt(x);
            field[x + 43381] = (byte) (iTemp + WIDTH);
            field[x + 43382] = WIDTH;
        }

        // Checksumme erzeugen und hinzufuegen
        int checksum = 0;
        int undwo = 0;
        for (int e = 0; e <= 44100; e++) {
            if (field[e] != WIDTH || e < 43381) {
                checksum ^= (field[e] + WIDTH) % 256;
            } else {
                undwo = e;
                break;
            }
        }
        field[undwo] = (byte) (checksum + WIDTH);
        field[undwo + 1] = WIDTH;

        boolean success = mainFrame.storageManager.saveToFile(field, slot);
        if (!success) {
            log.error("File save error!");
        }

        mainFrame.freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    // Neuen Spielstand initialisieren
    public synchronized void load() {
        mainFrame.freeze(true);

        // Sprache festlegen
        Start.LANGUAGE = isHornjos;

        // "illegale" Sprache verhindern
        // if (mainFrame.sprache > 2) mainFrame.sprache = 1;

        // Inventar setzen
        mainFrame.inventory.vInventory = inventory;

        // Scrolling - Variable setzen
        mainFrame.scrollX = scrolling;

        // Aktionen festlegen
        mainFrame.actions = actions;

        // hier schon der Krabatinit-damit Loadberechnungen abh. von Krabatposition
        // ueberhaupt eine Chance haben
        // Krabats Position setzen
        mainFrame.krabat.setPos(krabatPos);

        // Krabats Blickrichtung festlegen
        mainFrame.krabat.setFacing(facing);

        // alte Location zerstoeren, neue erzeugen
        mainFrame.destructLocation(mainFrame.currentLocationIdx);
        mainFrame.currentLocationIdx = 0;    // fuer Krabatpositionsinit darf keine alte Location erscheinen
        mainFrame.constructLocation(location);

        mainFrame.freeze(false);
    }
}  