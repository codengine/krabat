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
import de.codengine.krabat.anims.MainAnim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dictionary extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(Dictionary.class);
    private GenericImage background;
    private GenericImage arrowUp;
    private GenericImage arrowDown;
    private GenericImage arrowUpDisabled;
    private GenericImage arrowDownDisabled;

    private static final GenericPoint ARROW_UP_POINT = new GenericPoint(210, 80);  // allgemeine Pfeildeklarationen
    private static final GenericPoint ARROW_DOWN_POINT = new GenericPoint(210, 277);

    private static final int WIDTH = 35; // dto.
    private static final int HEIGHT = 53;

    private final BorderRect brArrowUp;
    private final BorderRect brArrowDown;

    private int cursorShape = 200;

    private final String[][] entry;

    private int index = 0;

    private static final String[] D_SELECT = {"B", "C", "D", "E", "F", "G", "H", "Ch", "J", "K", "#L", "L", "M", "N",
            "O", "Pa", "Po", "P#y", "R", "S", "#S", "T", "Wa", "Wo", "Wu", "Z", "#Z", "#D"};
    private static final int[] D_JUMP = {0, 25, 37, 80, 81, 83, 114, 118, 122, 126, 180, 188, 197, 218, 260,
            262, 284, 324, 360, 378, 408, 419, 430, 448, 491, 530, 576, 586};

    private static final String[] H_SELECT = {"B", "#K", "D", "D#d", "H", "Ch", "J", "K", "L", "M", "N", "O",
            "Pa", "Po", "P#r", "R", "S", "#S", "T", "#C", "W#e", "Wo", "Wu", "Z", "#Z"};
    private static final int[] H_JUMP = {0, 6, 11, 21, 23, 30, 33, 35, 52, 56, 61, 79, 80, 89, 106,
            117, 126, 149, 155, 162, 163, 173, 194, 207, 226};

    private int skip = -1;
    private static final GenericPoint TOP_LEFT = new GenericPoint(30, 400);  // fuer Beginn Auswahlleiste
    private static final int XDIFF = 40;    // allegemeine festlegungen fuer Auswahlleiste
    private static final int YDIFF = 30;
    private static final int DXANZA = 14;
    private static final int HXANZA = 13;
    private static final int YANZA = 2;

    private final BorderRect brGesamt;   // Gesamtrect fuer Auswahlleiste

    private int newItem = 0;
    private int oldItem = 0;

    private int number = 0;

    private boolean paintCall = false;

    private static final int X_SORBIAN = 30;  // allgemeine Deklarationen fuer Woerterbuchanfang
    private static final int X_GERMAN = 260;
    private static final int Y_SORBIAN = 80;
    private static final int Y_GERMAN = 80;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Dictionary(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.stopPaint(true);

        entry = new String[600][2];

        brArrowUp = new BorderRect(ARROW_UP_POINT.x, ARROW_UP_POINT.y, ARROW_UP_POINT.x + WIDTH, ARROW_UP_POINT.y + HEIGHT);
        brArrowDown = new BorderRect(ARROW_DOWN_POINT.x, ARROW_DOWN_POINT.y, ARROW_DOWN_POINT.x + WIDTH, ARROW_DOWN_POINT.y + HEIGHT);

        //TODO: Lang3 not considered here?
        brGesamt = new BorderRect(TOP_LEFT.x, TOP_LEFT.y, TOP_LEFT.x + (Start.LANGUAGE == 2 ? DXANZA : HXANZA) * XDIFF - 1, TOP_LEFT.y + YANZA * YDIFF - 1);

        initImages();

        //TODO: Translations for Slownik
        loadDictionary();

        mainFrame.freeze(false);
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/mainmenu/background2.png");
        arrowUp = getPicture("gfx/mainmenu/pfeil-hoch.png");
        arrowUpDisabled = getPicture("gfx/mainmenu/pfeil-hoch-leer.png");
        arrowDown = getPicture("gfx/mainmenu/pfeil-runter.png");
        arrowDownDisabled = getPicture("gfx/mainmenu/pfeil-runter-leer.png");
    }

    // Woerterbuch laden
    private void loadDictionary() {
        String file = Start.LANGUAGE == 2 ? "slowds.kra" : "slowhs.kra";

        byte[] field = new byte[]{};

        if (mainFrame.storageManager.isDictionarySupported()) {
            field = mainFrame.storageManager.loadDictionary(file);
        }

        int pos = field.length;


        // auf Strings konvertieren und Eintraege generieren
        int i = 0;
        number = 0;

        if (pos == 0) {
            return;
        }

        do {
            // solange String reinkopieren, bis $07 erreicht ( = Trennzeichen Sorbisch -> Deutsch)
            entry[number][0] = "";
            for (int j = i; field[j] != 13; j++) {
                entry[number][0] += (char) field[j];
                i++;
            }

            i += 2;

            entry[number][1] = "";
            for (int j = i; field[j] != 13; j++) {
                entry[number][1] += (char) field[j];
                i++;
            }

            i += 2;
            number++;

        }
        while (i < pos);

        // alle Eintraege sortieren
        // Sort (Nummer);
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintDictionary(GenericDrawingContext g) {

        // Credits-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1280, 480);
            cursorShape = 200;
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            // alles loeschen und neuzeichnen - hier die texte, die sich nur bei "Clipset = false" aendern (Mouseclick)
            g.drawImage(background, mainFrame.scrollX, 0);
            GenericPoint ps = mainFrame.imageFont.centerAnimText("S#lownik", new GenericPoint(320, 35));
            mainFrame.imageFont.drawString(g, "S#lownik", ps.x, ps.y, 0xffff0000);

            for (int i = index; i < index + 10; i++) {
                mainFrame.imageFont.drawString(g, entry[i][0], X_SORBIAN + mainFrame.scrollX, mainFrame.scrollY + Y_SORBIAN + (i - index) * 25, 0xffff0000);
                mainFrame.imageFont.drawString(g, entry[i][1], X_GERMAN + mainFrame.scrollX, mainFrame.scrollY + Y_GERMAN + (i - index) * 25, 0xffff0000);
            }

            // Pfeile dazu-sind ja sonst geloescht !
            g.drawImage(arrowUpDisabled, ARROW_UP_POINT.x + mainFrame.scrollX, ARROW_UP_POINT.y + mainFrame.scrollY);
            g.drawImage(arrowDownDisabled, ARROW_DOWN_POINT.x + mainFrame.scrollX, ARROW_DOWN_POINT.y + mainFrame.scrollY);
        }

        // Hier Unterscheidung HS-DS
        int tempXANZA = Start.LANGUAGE == 2 ? DXANZA : HXANZA;

        // Auswahl anzeigen bzw. highlighten (wenn ihr dabeiseid, dann gibts 'n Highlight)
        for (int i = 0; i < tempXANZA; i++) {
            GenericPoint px = mainFrame.imageFont.centerAnimText(Start.LANGUAGE == 2 ? D_SELECT[i] : H_SELECT[i], new GenericPoint(mainFrame.scrollX + TOP_LEFT.x + i * XDIFF + XDIFF / 2, mainFrame.scrollY + TOP_LEFT.y));
            mainFrame.imageFont.drawString(g, Start.LANGUAGE == 2 ? D_SELECT[i] : H_SELECT[i], px.x, px.y, i == skip ? 0xffff0000 : 0xff800000);
        }

        for (int i = tempXANZA; i < tempXANZA * 2 - (Start.LANGUAGE == 2 ? 0 : 1); i++)  // HS hat ungerade Anzahl
        {
            GenericPoint py = mainFrame.imageFont.centerAnimText(Start.LANGUAGE == 2 ? D_SELECT[i] : H_SELECT[i], new GenericPoint(mainFrame.scrollX + TOP_LEFT.x + (i - tempXANZA) * XDIFF + XDIFF / 2, mainFrame.scrollY + TOP_LEFT.y + YDIFF));
            mainFrame.imageFont.drawString(g, Start.LANGUAGE == 2 ? D_SELECT[i] : H_SELECT[i], py.x, py.y, i == skip ? 0xffff0000 : 0xff800000);
        }


        // Wenn noetig, dann highlight aufheben!!!
        switch (oldItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowUpDisabled, ARROW_UP_POINT.x + mainFrame.scrollX, ARROW_UP_POINT.y + mainFrame.scrollY);
                break;
            case 2:
                g.drawImage(arrowDownDisabled, ARROW_DOWN_POINT.x + mainFrame.scrollX, ARROW_DOWN_POINT.y + mainFrame.scrollY);
                break;
            default:
                log.error("Falsches Menu-Item zum abdunkeln!!! olditem = {}", oldItem);
        }

        if (oldItem != 0) {
            oldItem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (newItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowUp, ARROW_UP_POINT.x + mainFrame.scrollX, ARROW_UP_POINT.y + mainFrame.scrollY);
                break;
            case 2:
                g.drawImage(arrowDown, ARROW_DOWN_POINT.x + mainFrame.scrollX, ARROW_DOWN_POINT.y + mainFrame.scrollY);
                break;
            default:
                log.error("Falsches Menu-Item!!! menuitem = {}", newItem);
        }

        if (newItem != 0) {
            oldItem = newItem;
        }

    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();

        if (e.isLeftClick()) {
            // linke Maustaste
            // Pfeil-Oben gedrueckt
            if (brArrowUp.isPointInRect(pTemp)) {
                index -= 10;
                mainFrame.isClipSet = false;
                if (index < 0) {
                    index = 0;
                }
                mainFrame.repaint();
            }

            // Pfeil-Unten gedrueckt
            if (brArrowDown.isPointInRect(pTemp)) {
                index += 10;
                mainFrame.isClipSet = false;
                if (index > number - 10) {
                    index = number - 10;
                }
                mainFrame.repaint();
            }

            // Buckstabe wurde ausgewaehlt
            if (brGesamt.isPointInRect(pTemp)) {
                int adresse = 0;

                // Y-Offset berechnen
                adresse += (pTemp.y - TOP_LEFT.y) / YDIFF * (Start.LANGUAGE == 2 ? DXANZA : HXANZA);

                // X-Offset dazu
                adresse += (pTemp.x - TOP_LEFT.x) / XDIFF;

                // Index neu festlegen, nicht, wenn auf Leerfeld in HS gedrueckt
                if (Start.LANGUAGE != 1 || adresse < 25) {
                    index = Start.LANGUAGE == 2 ? D_JUMP[adresse] : H_JUMP[adresse];
                    mainFrame.isClipSet = false;
                    mainFrame.repaint();
                }
            }
        } else {
            // rechte Maustaste, sofort raus (immer)
            deactivate();
        }

    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {

        // if ((NachOben.IsPointInRect (pTemp) == false) && (NachUnten.IsPointInRect (pTemp) == false))
        // {
        if (cursorShape != 0) {
            cursorShape = 0;
            mainFrame.setCursor(mainFrame.cursorNormal);
        }
        // }

        // Highlight im Menue festlegen
        newItem = 0;
        if (brArrowUp.isPointInRect(pTemp)) {
            newItem = 1;
        }
        if (brArrowDown.isPointInRect(pTemp)) {
            newItem = 2;
        }

        int oldskip = skip;
        if (!brGesamt.isPointInRect(pTemp)) {
            skip = -1;
        } else {
            skip = 0;

            // Y-Offset berechnen
            skip += (pTemp.y - TOP_LEFT.y) / YDIFF * (Start.LANGUAGE == 2 ? DXANZA : HXANZA);

            // X-Offset dazu
            skip += (pTemp.x - TOP_LEFT.x) / XDIFF;
        }

        // wenn noetig , dann Neuzeichnen!
        if (paintCall) {
            paintCall = false;
            return;
        }
        if (newItem != oldItem || skip != oldskip) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent() {
        newItem = 0;
        skip = -1;
        mainFrame.repaint();
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();
        if (key == GenericKeyEvent.VK_ESCAPE) {
            deactivate();
        }
    }


    // Deaktivieren //////////
    private void deactivate() {
        mainFrame.isClipSet = false;
        mainFrame.destructLocation(107);
        mainFrame.whatScreen = ScreenType.NONE;

        mainFrame.stopPaint(false);

        mainFrame.repaint();
    }
}
