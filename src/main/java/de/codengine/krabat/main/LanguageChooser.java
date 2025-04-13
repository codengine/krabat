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
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LanguageChooser extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(LanguageChooser.class);
    private GenericImage background;
    private GenericImage arrowUp;
    private GenericImage arrowDown;
    private GenericImage arrowUpDisabled;
    private GenericImage arrowDownDisabled;

    private static final GenericPoint ARROW_UP = new GenericPoint(210, 80);  // allgemeine Pfeildeklarationen
    private static final GenericPoint ARROW_DOWN = new GenericPoint(210, 277);

    private static final GenericPoint OK_BOTTOM = new GenericPoint(400, 300);

    private static final int WIDTH = 35; // dto.
    private static final int HEIGHT = 53;

    private final BorderRect brArrowUp;
    private final BorderRect brArrowDown;
    private final BorderRect brOk;

    private int cursorShape = 200;

    private int index;

    private final BorderRect brGesamt;   // Gesamtrect fuer Auswahlleiste

    private int menuItem = 0;
    private int oldItem = 0;

    private int currLang = 0;
    private int oldLang = 0;

    private boolean paintCall = false;

    private static final int X_LEFT = 30;  // allgemeine Deklarationen fuer Woerterbuchanfang
    private static final int Y_UP = 80;

    private String[] languages;

    private String[] abbreviations;

    // Initialisierung ////////////////////////////////////////////////////////

    private final GameProperties properties;

    // Instanz von dieser Location erzeugen
    public LanguageChooser(Start caller, GameProperties properties) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.stopPaint(true);

        this.properties = properties;

        brArrowUp = new BorderRect(ARROW_UP.x, ARROW_UP.y, ARROW_UP.x + WIDTH, ARROW_UP.y + HEIGHT);
        brArrowDown = new BorderRect(ARROW_DOWN.x, ARROW_DOWN.y, ARROW_DOWN.x + WIDTH, ARROW_DOWN.y + HEIGHT);

        brOk = new BorderRect(OK_BOTTOM.x, OK_BOTTOM.y, OK_BOTTOM.x + 40, OK_BOTTOM.y + 20);

        brGesamt = new BorderRect(X_LEFT, Y_UP, ARROW_UP.x - 10, Y_UP + 10 * 25);

        initImages();

        initLanguages();
        index = 0;

        mainFrame.isClipSet = false;

        log.debug("LanguageChooser constructor called!");

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

    private void initLanguages() {
        Map<String, String> existingTranslations = mainFrame.storageManager.getExistingTranslations();
        int tmp = existingTranslations.size();

        languages = new String[tmp];
        abbreviations = new String[tmp];

        int i = 0;

        for (Map.Entry<String, String> langEntry : existingTranslations.entrySet()) {
            abbreviations[i] = langEntry.getKey();
            languages[i] = langEntry.getValue();
            i++;
        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        log.debug("LanguageChooser repaint with clipset={}", mainFrame.isClipSet);

        // Credits-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1280, 480);
            cursorShape = 200;
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            // alles loeschen und neuzeichnen - hier die texte, die sich nur bei "Clipset = false" aendern (Mouseclick)
            g.drawImage(background, mainFrame.scrollX, 0);
            GenericPoint ps = mainFrame.imageFont.centerAnimText("Select language", new GenericPoint(320, 35));
            mainFrame.imageFont.drawString(g, "Select language", ps.x, ps.y, 0xffff0000);

            for (int i = Math.max(index, 0); i < Math.min(index + 10, languages.length); i++) {
                mainFrame.imageFont.drawString(g, languages[i], X_LEFT + mainFrame.scrollX, mainFrame.scrollY + Y_UP + (i - index) * 25, 0xff800000);
            }

            // Pfeile dazu-sind ja sonst geloescht !
            g.drawImage(arrowUpDisabled, ARROW_UP.x + mainFrame.scrollX, ARROW_UP.y + mainFrame.scrollY);
            g.drawImage(arrowDownDisabled, ARROW_DOWN.x + mainFrame.scrollX, ARROW_DOWN.y + mainFrame.scrollY);

            mainFrame.imageFont.drawString(g, "OK", OK_BOTTOM.x + mainFrame.scrollX, mainFrame.scrollY + OK_BOTTOM.y, 0xff800000);
        }

        if (oldLang > 0) {
            if (oldLang <= languages.length) {
                mainFrame.imageFont.drawString(g, languages[oldLang - 1], X_LEFT + mainFrame.scrollX, mainFrame.scrollY + Y_UP + (oldLang - index - 1) * 25, 0xff800000);
            } else {
                log.warn("Wrong language to deselect! oldLang: {}, languages.length: {}", oldLang, languages.length);
            }
        }
        if (oldLang != 0) {
            oldLang = 0;
        }

        if (currLang > 0) {
            if (currLang <= languages.length) {
                mainFrame.imageFont.drawString(g, languages[currLang - 1], X_LEFT + mainFrame.scrollX, mainFrame.scrollY + Y_UP + (currLang - index - 1) * 25, 0xffff0000);
            } else {
                log.warn("Wrong language to select! currLang: {}, languages.length: {}", currLang, languages.length);
            }
        }
        if (currLang != 0) {
            oldLang = currLang;
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (oldItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowUpDisabled, ARROW_UP.x + mainFrame.scrollX, ARROW_UP.y + mainFrame.scrollY);
                break;
            case 2:
                g.drawImage(arrowDownDisabled, ARROW_DOWN.x + mainFrame.scrollX, ARROW_DOWN.y + mainFrame.scrollY);
                break;
            case 3:
                mainFrame.imageFont.drawString(g, "OK", OK_BOTTOM.x + mainFrame.scrollX, mainFrame.scrollY + OK_BOTTOM.y, 0xff800000);
                break;
            default:
                log.error("Falsches Menu-Item zum abdunkeln!!! olditem = {}", oldItem);
        }

        if (oldItem != 0) {
            oldItem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowUp, ARROW_UP.x + mainFrame.scrollX, ARROW_UP.y + mainFrame.scrollY);
                break;
            case 2:
                g.drawImage(arrowDown, ARROW_DOWN.x + mainFrame.scrollX, ARROW_DOWN.y + mainFrame.scrollY);
                break;
            case 3:
                mainFrame.imageFont.drawString(g, "OK", OK_BOTTOM.x + mainFrame.scrollX, mainFrame.scrollY + OK_BOTTOM.y, 0xffff0000);
                break;
            default:
                log.error("Falsches Menu-Item!!! menuitem = {}", menuItem);
        }

        if (menuItem != 0) {
            oldItem = menuItem;
        }
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        if (!e.isLeftClick()) {
            return;
        }

        GenericPoint pTemp = e.getPoint();

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
            if (index > languages.length - 10) {
                index = languages.length - 10;
            }
            mainFrame.repaint();
        }

        if (brOk.isPointInRect(pTemp) && currLang != 0) {
            log.debug("Selected language = {}", currLang);
            if (abbreviations[currLang - 1].equalsIgnoreCase("hs")
                    || abbreviations[currLang - 1].equalsIgnoreCase("ds")
                    || abbreviations[currLang - 1].equalsIgnoreCase("de")) {
                Start.THIRD_GAME_LANGUAGE = "de";
            } else {
                Start.THIRD_GAME_LANGUAGE = abbreviations[currLang - 1];
            }
            if (abbreviations[currLang - 1].equalsIgnoreCase("hs")) {
                Start.LANGUAGE = 1;
            } else if (abbreviations[currLang - 1].equalsIgnoreCase("ds")) {
                Start.LANGUAGE = 2;
            } else {
                Start.LANGUAGE = 3;
            }
            Start.STRING_MANAGER.defineThirdLanguage(abbreviations[currLang - 1]);
            properties.setProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, Integer.toString(Start.LANGUAGE));
            properties.setProperty(GameProperties.THIRD_GAME_LANGUAGE_SELECTION, Start.THIRD_GAME_LANGUAGE);
            createNewLocation(100, 109);
            mainFrame.repaint();
        }

        // Sprache wurde ausgewaehlt
        if (brGesamt.isPointInRect(pTemp)) {
            for (int i = 0; i < languages.length; i++) {

                if (pTemp.y >= Y_UP + (i - index) * 25 && pTemp.y < Y_UP + (i - index) * 25 + 24) {
                    currLang = i + 1;
                    mainFrame.repaint();
                    log.debug("Selected lang={}", abbreviations[i]);
                    break;
                }
            }
        }
    }

    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (cursorShape != 0) {
            cursorShape = 0;
            mainFrame.setCursor(mainFrame.cursorNormal);
        }

        // Highlight im Menue festlegen
        menuItem = 0;
        if (brArrowUp.isPointInRect(pTemp)) {
            menuItem = 1;
        }
        if (brArrowDown.isPointInRect(pTemp)) {
            menuItem = 2;
        }
        if (brOk.isPointInRect(pTemp) && currLang != 0) {
            menuItem = 3;
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

    @Override
    public void evalMouseExitEvent() {
        menuItem = 0;
        mainFrame.repaint();
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // currently no keys
    }
}
