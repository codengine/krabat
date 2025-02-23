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
import de.codengine.krabat.anims.Mainanim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Info extends Mainanim {
    private static final Logger log = LoggerFactory.getLogger(Info.class);
    private GenericImage Bild;
    private GenericImage Jan;
    private GenericImage Jiri;
    private GenericImage Daniel;
    private GenericImage Eddie;
    private GenericImage Hanus;
    private GenericImage Mic;
    private final GenericImage Pfeill;
    private final GenericImage DPfeill;
    private final GenericImage Pfeilr; /* DPfeilr, */
    private GenericImage Abdecken;

    private final GenericPoint pLO;

    private final Borderrect brGesamt;
    private final Borderrect brPfeill;
    private final Borderrect brPfeilr;

    private int menuitem = 0;
    private int olditem = 0;

    private boolean Paintcall = false;

    private int PictureCounter;

    // Hier die Texte
    private static final String[][] IMAGE_TEXTS = {
            {"Info_1", "Info_2", "Info_3", ""},
            {"Info_4", "Info_5", "Info_6", ""},
            {"Info_7", "Info_8", "", ""},
            {"Info_9", "Info_10", "", ""},
            {"Info_11", "Info_12", "Info_13", "Info_14"},
            {"Info_15", "Info_16", "", ""},
            {"Info_17", "", "", ""}
    };

    private static final GenericPoint[][] BildPunkte = {
            {new GenericPoint(110, 55), new GenericPoint(113, 238), new GenericPoint(413, 170), null},
            {new GenericPoint(94, 52), new GenericPoint(90, 215), new GenericPoint(440, 80), null},
            {new GenericPoint(142, 64), new GenericPoint(257, 223), null, null},
            {new GenericPoint(329, 48), new GenericPoint(113, 238), null, null},
            {new GenericPoint(91, 76), new GenericPoint(250, 245), new GenericPoint(358, 149), new GenericPoint(406, 183)},
            {new GenericPoint(135, 73), new GenericPoint(413, 170), null, null},
            {new GenericPoint(90, 45), null, null, null}
    };


    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Info(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        InitImages();

        PictureCounter = 0;  // Reset der Bilder

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);

        brPfeill = mainFrame.inventory.brPfeill;
        brPfeilr = new Borderrect(pLO.x + 391, pLO.y + 319,
                pLO.x + 481, pLO.y + 359);

        Pfeill = mainFrame.inventory.Pfeill;
        DPfeill = mainFrame.inventory.DPfeill;
        Pfeilr = mainFrame.inventory.Pfeilr;
        // DPfeilr = mainFrame.inventory.DPfeilr;

        mainFrame.Freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    // Bilder vorbereiten
    public void InitImages() {
        Bild = getPicture("gfx/mainmenu/info-7.png");
        Jan = getPicture("gfx/mainmenu/jan.png");
        Jiri = getPicture("gfx/mainmenu/jiri.png");
        Daniel = getPicture("gfx/mainmenu/daniel.png");
        Eddie = getPicture("gfx/mainmenu/eddie.png");
        Hanus = getPicture("gfx/mainmenu/hanus.png");
        Mic = getPicture("gfx/mainmenu/mic.png");

        Abdecken = getPicture("gfx/mainmenu/dpfeil.png");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintCredits(GenericDrawingContext g) {

        // Credits-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1280, 480);

            g.drawImage(Bild, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            switch (PictureCounter) {
                case 0:  // Jan und Joerg
                    g.drawImage(Jan, 186 + pLO.x + mainFrame.scrollX, 58 + pLO.y + mainFrame.scrollY);
                    g.drawImage(Jiri, 310 + pLO.x + mainFrame.scrollX, 43 + pLO.y + mainFrame.scrollY);
                    break;
                case 1: // Daniel und Eddie
                    g.drawImage(Daniel, 186 + pLO.x + mainFrame.scrollX, 87 + pLO.y + mainFrame.scrollY);
                    g.drawImage(Eddie, 338 + pLO.x + mainFrame.scrollX, 40 + pLO.y + mainFrame.scrollY);
                    break;
                case 2: // Hanus
                    g.drawImage(Hanus, 314 + pLO.x + mainFrame.scrollX, 31 + pLO.y + mainFrame.scrollY);
                    break;
                case 3: // Jan
                    g.drawImage(Jan, 186 + pLO.x + mainFrame.scrollX, 58 + pLO.y + mainFrame.scrollY);
                    break;
                case 4: // Mic
                    g.drawImage(Mic, 174 + pLO.x + mainFrame.scrollX, 43 + pLO.y + mainFrame.scrollY);
                    break;
                case 5: // Jiri
                    g.drawImage(Jiri, 310 + pLO.x + mainFrame.scrollX, 43 + pLO.y + mainFrame.scrollY);
                    break;
            }

            if (PictureCounter == 6) {
                g.drawImage(Abdecken, pLO.x + mainFrame.scrollX + 380, pLO.y + mainFrame.scrollY + 307);
            }

            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            for (int i = 0; i <= 3; i++) {
                String textKey = IMAGE_TEXTS[PictureCounter][i];
                String outputText = Objects.equals(textKey, "") ? "" : Start.stringManager.getTranslation(textKey);
                GenericPoint outputTextPos = BildPunkte[PictureCounter][i];
                if (!Objects.equals(outputText, "")) // leere Dinger ueberspringen
                {
                    mainFrame.imageFont.drawString(g, outputText, pLO.x + outputTextPos.x + mainFrame.scrollX,
                            pLO.y + outputTextPos.y + mainFrame.scrollY, 0xffff0000);
                }
            }
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(DPfeill, 119 + mainFrame.scrollX, 349 + mainFrame.scrollY);
                break;
            case 2:
                if (PictureCounter != 6) {
                    GenericRectangle txx = g.getClipBounds();
                    g.setClip(422 + mainFrame.scrollX, 348 + mainFrame.scrollY, 100, 50);
                    // BUG!!!
                    //		    g.setClip (422, 348, 100, 50);
                    g.drawImage(Bild, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                    g.setClip(txx);
                }
                break;
            default:
                log.error("Falsches Menu-Item!!! oldItem = {}", olditem);
        }

        if (olditem != 0) {
            olditem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuitem) {
            case 0:
                break;
            case 1:
                g.drawImage(Pfeill, 121 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            case 2:
                g.drawImage(Pfeilr, 423 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            default:
                log.error("Falsches Menu-Item fuers Highlight!!! menuitem = {}", menuitem);
        }

        if (menuitem != 0) {
            olditem = menuitem;
        }
    }

    public void evalMouseExitEvent() {
        menuitem = 0;
        mainFrame.repaint();
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        if (!e.isLeftClick()) {
            return;
        }

        GenericPoint pTemp = e.getPoint();

        // bei Click Ausserhalb zurueck ins Spiel
        if (!brGesamt.IsPointInRect(pTemp)) {
            Deactivate();
            mainFrame.whatScreen = 0;
            return;
        }

        // bei Click auf Pfeil links zurueck ins vorherige Bild oder verlassen
        if (brPfeill.IsPointInRect(pTemp)) {
            PictureCounter--;
            if (PictureCounter < 0) {
                Deactivate();
                return;
            } else {
                mainFrame.isClipSet = false;
                mainFrame.repaint();
            }
        }

        // bei Click auf Pfeil rechts ein Bild weiter
        if (brPfeilr.IsPointInRect(pTemp)) {
            if (PictureCounter < 6) {
                PictureCounter++;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
            }
        }

    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        menuitem = 0;
        if (brPfeill.IsPointInRect(pTemp)) {
            menuitem = 1;
        }

        // wenn nicht im letzten Bild, dann auch den rechten Pfeil zeichnen
        if (brPfeilr.IsPointInRect(pTemp) && PictureCounter < 6) {
            menuitem = 2;
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
            return;
        }

        if (menuitem != olditem) {
            mainFrame.repaint();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
        }
    }


    // Deaktivieren //////////
    private void Deactivate() {
        menuitem = 0;
        mainFrame.isClipSet = false;
        mainFrame.DestructLocation(104);
        if (mainFrame.mainMenu.MMactive) {
            mainFrame.whatScreen = 2;
        } else {
            mainFrame.whatScreen = 0;
        }
        mainFrame.repaint();
    }
}
