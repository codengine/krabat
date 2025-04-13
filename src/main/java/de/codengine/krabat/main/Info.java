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

import java.util.Objects;

public class Info extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(Info.class);
    private GenericImage image;
    private GenericImage jan;
    private GenericImage jiri;
    private GenericImage daniel;
    private GenericImage eddie;
    private GenericImage hanus;
    private GenericImage mic;
    private final GenericImage arrowLeft;
    private final GenericImage arrowLeftDisabled;
    private final GenericImage arrowRight;
    private GenericImage cover;

    private final GenericPoint pLO;

    private final BorderRect brTotal;
    private final BorderRect brArrowLeft;
    private final BorderRect brArrowRight;

    private int menuItem = 0;
    private int oldItem = 0;

    private boolean paintCall = false;

    private int pictureCounter;

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

    private static final GenericPoint[][] IMAGE_POINTS = {
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
        mainFrame.freeze(true);

        initImages();

        pictureCounter = 0;  // Reset der Bilder

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);
        brTotal = new BorderRect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);

        brArrowLeft = mainFrame.inventory.brArrowLeft;
        brArrowRight = new BorderRect(pLO.x + 391, pLO.y + 319,
                pLO.x + 481, pLO.y + 359);

        arrowLeft = mainFrame.inventory.arrowLeft;
        arrowLeftDisabled = mainFrame.inventory.arrowLeftDisabled;
        arrowRight = mainFrame.inventory.arrowRight;

        mainFrame.freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    // Bilder vorbereiten
    public void initImages() {
        image = getPicture("gfx/mainmenu/info-7.png");
        jan = getPicture("gfx/mainmenu/jan.png");
        jiri = getPicture("gfx/mainmenu/jiri.png");
        daniel = getPicture("gfx/mainmenu/daniel.png");
        eddie = getPicture("gfx/mainmenu/eddie.png");
        hanus = getPicture("gfx/mainmenu/hanus.png");
        mic = getPicture("gfx/mainmenu/mic.png");

        cover = getPicture("gfx/mainmenu/dpfeil.png");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintCredits(GenericDrawingContext g) {

        // Credits-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1280, 480);

            g.drawImage(image, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            switch (pictureCounter) {
                case 0:  // Jan und Joerg
                    g.drawImage(jan, 186 + pLO.x + mainFrame.scrollX, 58 + pLO.y + mainFrame.scrollY);
                    g.drawImage(jiri, 310 + pLO.x + mainFrame.scrollX, 43 + pLO.y + mainFrame.scrollY);
                    break;
                case 1: // Daniel und Eddie
                    g.drawImage(daniel, 186 + pLO.x + mainFrame.scrollX, 87 + pLO.y + mainFrame.scrollY);
                    g.drawImage(eddie, 338 + pLO.x + mainFrame.scrollX, 40 + pLO.y + mainFrame.scrollY);
                    break;
                case 2: // Hanus
                    g.drawImage(hanus, 314 + pLO.x + mainFrame.scrollX, 31 + pLO.y + mainFrame.scrollY);
                    break;
                case 3: // Jan
                    g.drawImage(jan, 186 + pLO.x + mainFrame.scrollX, 58 + pLO.y + mainFrame.scrollY);
                    break;
                case 4: // Mic
                    g.drawImage(mic, 174 + pLO.x + mainFrame.scrollX, 43 + pLO.y + mainFrame.scrollY);
                    break;
                case 5: // Jiri
                    g.drawImage(jiri, 310 + pLO.x + mainFrame.scrollX, 43 + pLO.y + mainFrame.scrollY);
                    break;
            }

            if (pictureCounter == 6) {
                g.drawImage(cover, pLO.x + mainFrame.scrollX + 380, pLO.y + mainFrame.scrollY + 307);
            }

            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            for (int i = 0; i <= 3; i++) {
                String textKey = IMAGE_TEXTS[pictureCounter][i];
                String outputText = Objects.equals(textKey, "") ? "" : Start.STRING_MANAGER.getTranslation(textKey);
                GenericPoint outputTextPos = IMAGE_POINTS[pictureCounter][i];
                if (!Objects.equals(outputText, "")) // leere Dinger ueberspringen
                {
                    mainFrame.imageFont.drawString(g, outputText, pLO.x + outputTextPos.x + mainFrame.scrollX,
                            pLO.y + outputTextPos.y + mainFrame.scrollY, 0xffff0000);
                }
            }
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (oldItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowLeftDisabled, 119 + mainFrame.scrollX, 349 + mainFrame.scrollY);
                break;
            case 2:
                if (pictureCounter != 6) {
                    GenericRectangle txx = g.getClipBounds();
                    g.setClip(422 + mainFrame.scrollX, 348 + mainFrame.scrollY, 100, 50);
                    g.drawImage(image, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                    g.setClip(txx);
                }
                break;
            default:
                log.error("Falsches Menu-Item!!! oldItem = {}", oldItem);
        }

        if (oldItem != 0) {
            oldItem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowLeft, 121 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            case 2:
                g.drawImage(arrowRight, 423 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            default:
                log.error("Falsches Menu-Item fuers Highlight!!! menuitem = {}", menuItem);
        }

        if (menuItem != 0) {
            oldItem = menuItem;
        }
    }

    public void evalMouseExitEvent() {
        menuItem = 0;
        mainFrame.repaint();
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        if (!e.isLeftClick()) {
            return;
        }

        GenericPoint pTemp = e.getPoint();

        // bei Click Ausserhalb zurueck ins Spiel
        if (!brTotal.isPointInRect(pTemp)) {
            deactivate();
            mainFrame.whatScreen = ScreenType.NONE;
            return;
        }

        // bei Click auf Pfeil links zurueck ins vorherige Bild oder verlassen
        if (brArrowLeft.isPointInRect(pTemp)) {
            pictureCounter--;
            if (pictureCounter < 0) {
                deactivate();
                return;
            } else {
                mainFrame.isClipSet = false;
                mainFrame.repaint();
            }
        }

        // bei Click auf Pfeil rechts ein Bild weiter
        if (brArrowRight.isPointInRect(pTemp)) {
            if (pictureCounter < 6) {
                pictureCounter++;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
            }
        }

    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        menuItem = 0;
        if (brArrowLeft.isPointInRect(pTemp)) {
            menuItem = 1;
        }

        // wenn nicht im letzten Bild, dann auch den rechten Pfeil zeichnen
        if (brArrowRight.isPointInRect(pTemp) && pictureCounter < 6) {
            menuItem = 2;
        }

        // wenn noetig , dann Neuzeichnen!
        if (paintCall) {
            paintCall = false;
            return;
        }

        if (menuItem != oldItem) {
            mainFrame.repaint();
        }
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
        menuItem = 0;
        mainFrame.isClipSet = false;
        mainFrame.destructLocation(104);
        if (mainFrame.mainMenu.mmActive) {
            mainFrame.whatScreen = ScreenType.MAIN_MENU;
        } else {
            mainFrame.whatScreen = ScreenType.NONE;
        }
        mainFrame.repaint();
    }
}
