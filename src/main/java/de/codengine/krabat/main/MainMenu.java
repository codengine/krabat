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

public class MainMenu extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(MainMenu.class);
    private GenericImage screen;
    private GenericImage newGame;
    private GenericImage loadGame;
    private GenericImage saveGame;
    private GenericImage info;
    private GenericImage continueGame;
    private GenericImage exit;
    private GenericImage langSwitch;
    private BorderRect brGesamt;
    private BorderRect brNewGame;
    private BorderRect brLoadGame;
    private BorderRect brSaveGame;
    private BorderRect brInfo;
    private BorderRect brContinueGame;
    private BorderRect brExit;
    private BorderRect brLangSwitch;

    private final GenericPoint pLO;

    private int menuitem = 0;
    private int olditem = 0;

    private boolean paintCall = false;
    public boolean mmActive;
    public boolean introcall = false;

    private final GameProperties gameProperties;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public MainMenu(Start caller, GameProperties gameProperties) {
        super(caller);
        this.gameProperties = gameProperties;

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);

        // Sprachenspezifische Initialisierung der Images und Rechtecke
        initRec();

    }

    // hier der Init je nach Sprache
    public void initRec() {
        brGesamt = new BorderRect(pLO.x + 65, pLO.y + 46, pLO.x + 513, pLO.y + 380);
        screen = getPicture("gfx/mainmenu/main-menu.png", true);
        int baseX1 = pLO.x + 89;
        int baseXRight = baseX1 + 396;
        brInfo = new BorderRect(baseX1, pLO.y + 314, pLO.x + 159, pLO.y + 336);

        langSwitch = getPicture("gfx/mainmenu/lang-switch.png", true);
        info = getPicture("gfx/mainmenu/info.png", true);

        continueGame = getPicture("gfx/mainmenu/continue.png", true);
        loadGame = getPicture("gfx/mainmenu/load.png", true);
        saveGame = getPicture("gfx/mainmenu/save.png", true);
        newGame = getPicture("gfx/mainmenu/new-game.png", true);
        exit = getPicture("gfx/mainmenu/exit.png", true);

        if (Start.LANGUAGE == 2) {
            brLangSwitch = new BorderRect(baseX1, pLO.y + 221, pLO.x + 227, pLO.y + 267);
            brContinueGame = new BorderRect(pLO.x + 297, pLO.y + 65, pLO.x + 486, pLO.y + 93);
            brLoadGame = new BorderRect(pLO.x + 352, pLO.y + 147, baseXRight, pLO.y + 175);
            brSaveGame = new BorderRect(pLO.x + 302, pLO.y + 186, baseXRight, pLO.y + 214);
            brNewGame = new BorderRect(pLO.x + 287, pLO.y + 275, baseXRight, pLO.y + 297);
            brExit = new BorderRect(pLO.x + 291, pLO.y + 308, baseXRight, pLO.y + 336);
        } else {
            brLangSwitch = new BorderRect(baseX1, pLO.y + 221, baseX1 + langSwitch.getWidth(), pLO.y + 221 + langSwitch.getHeight());
            brContinueGame = new BorderRect(baseXRight - continueGame.getWidth(), pLO.y + 65, baseXRight, pLO.y + 65 + continueGame.getHeight());
            brLoadGame = new BorderRect(baseXRight - loadGame.getWidth(), pLO.y + 147, baseXRight, pLO.y + 147 + loadGame.getHeight());
            brSaveGame = new BorderRect(baseXRight - saveGame.getWidth(), pLO.y + 186, baseXRight, pLO.y + 186 + saveGame.getHeight());
            brNewGame = new BorderRect(baseXRight - newGame.getWidth(), pLO.y + 275, baseXRight, pLO.y + 275 + newGame.getHeight());
            brExit = new BorderRect(baseXRight - exit.getWidth(), pLO.y + 308, baseXRight, pLO.y + 308 + exit.getHeight());
        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintMainMenu(GenericDrawingContext g) {
        // Mainmenu-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 964);
            g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1: // Nowostart
                g.setClip(brNewGame.topLeftPoint.x + mainFrame.scrollX, brNewGame.topLeftPoint.y + mainFrame.scrollY,
                        brNewGame.bottomRightPoint.x - brNewGame.topLeftPoint.x + mainFrame.scrollX,
                        brNewGame.bottomRightPoint.y - brNewGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 2: // Wocinic
                g.setClip(brLoadGame.topLeftPoint.x + mainFrame.scrollX, brLoadGame.topLeftPoint.y + mainFrame.scrollY,
                        brLoadGame.bottomRightPoint.x - brLoadGame.topLeftPoint.x + mainFrame.scrollX,
                        brLoadGame.bottomRightPoint.y - brLoadGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 3: // Skladzic
                g.setClip(brSaveGame.topLeftPoint.x + mainFrame.scrollX, brSaveGame.topLeftPoint.y + mainFrame.scrollY,
                        brSaveGame.bottomRightPoint.x - brSaveGame.topLeftPoint.x + mainFrame.scrollX,
                        brSaveGame.bottomRightPoint.y - brSaveGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 4: // Rec
                g.setClip(brLangSwitch.topLeftPoint.x + mainFrame.scrollX, brLangSwitch.topLeftPoint.y + mainFrame.scrollY,
                        brLangSwitch.bottomRightPoint.x - brLangSwitch.topLeftPoint.x + mainFrame.scrollX,
                        brLangSwitch.bottomRightPoint.y - brLangSwitch.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 6: // Info
                g.setClip(brInfo.topLeftPoint.x + mainFrame.scrollX, brInfo.topLeftPoint.y + mainFrame.scrollY,
                        brInfo.bottomRightPoint.x - brInfo.topLeftPoint.x + mainFrame.scrollX,
                        brInfo.bottomRightPoint.y - brInfo.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 7: // Dalehrac
                g.setClip(brContinueGame.topLeftPoint.x + mainFrame.scrollX, brContinueGame.topLeftPoint.y + mainFrame.scrollY,
                        brContinueGame.bottomRightPoint.x - brContinueGame.topLeftPoint.x + mainFrame.scrollX,
                        brContinueGame.bottomRightPoint.y - brContinueGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 8: // Konc
                g.setClip(brExit.topLeftPoint.x + mainFrame.scrollX, brExit.topLeftPoint.y + mainFrame.scrollY,
                        brExit.bottomRightPoint.x - brExit.topLeftPoint.x + mainFrame.scrollX,
                        brExit.bottomRightPoint.y - brExit.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            default:
                log.error("Falsches Menu-Item zum abdunkeln!!! olditem = {}", olditem);
        }

        if (olditem != 0) {
            olditem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuitem) {
            case 0:
                break;
            case 1: // Nowostart
                g.setClip(brNewGame.topLeftPoint.x + mainFrame.scrollX, brNewGame.topLeftPoint.y + mainFrame.scrollY,
                        brNewGame.bottomRightPoint.x - brNewGame.topLeftPoint.x + mainFrame.scrollX,
                        brNewGame.bottomRightPoint.y - brNewGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(newGame, brNewGame.topLeftPoint.x + mainFrame.scrollX, brNewGame.topLeftPoint.y + mainFrame.scrollY);
                break;
            case 2: // Wocinic
                g.setClip(brLoadGame.topLeftPoint.x + mainFrame.scrollX, brLoadGame.topLeftPoint.y + mainFrame.scrollY,
                        brLoadGame.bottomRightPoint.x - brLoadGame.topLeftPoint.x + mainFrame.scrollX,
                        brLoadGame.bottomRightPoint.y - brLoadGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(loadGame, brLoadGame.topLeftPoint.x + mainFrame.scrollX, brLoadGame.topLeftPoint.y + mainFrame.scrollY);
                break;
            case 3: // Skladzic
                g.setClip(brSaveGame.topLeftPoint.x + mainFrame.scrollX, brSaveGame.topLeftPoint.y + mainFrame.scrollY,
                        brSaveGame.bottomRightPoint.x - brSaveGame.topLeftPoint.x + mainFrame.scrollX,
                        brSaveGame.bottomRightPoint.y - brSaveGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(saveGame, brSaveGame.topLeftPoint.x + mainFrame.scrollX, brSaveGame.topLeftPoint.y + mainFrame.scrollY);
                break;
            case 4: // Rec
                g.setClip(brLangSwitch.topLeftPoint.x + mainFrame.scrollX, brLangSwitch.topLeftPoint.y + mainFrame.scrollY,
                        brLangSwitch.bottomRightPoint.x - brLangSwitch.topLeftPoint.x + mainFrame.scrollX,
                        brLangSwitch.bottomRightPoint.y - brLangSwitch.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(langSwitch, brLangSwitch.topLeftPoint.x + mainFrame.scrollX, brLangSwitch.topLeftPoint.y + mainFrame.scrollY);
                break;
            case 6: // Info
                g.setClip(brInfo.topLeftPoint.x + mainFrame.scrollX, brInfo.topLeftPoint.y + mainFrame.scrollY,
                        brInfo.bottomRightPoint.x - brInfo.topLeftPoint.x + mainFrame.scrollX,
                        brInfo.bottomRightPoint.y - brInfo.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(info, brInfo.topLeftPoint.x + mainFrame.scrollX, brInfo.topLeftPoint.y + mainFrame.scrollY);
                break;
            case 7: // Dalehrac
                g.setClip(brContinueGame.topLeftPoint.x + mainFrame.scrollX, brContinueGame.topLeftPoint.y + mainFrame.scrollY,
                        brContinueGame.bottomRightPoint.x - brContinueGame.topLeftPoint.x + mainFrame.scrollX,
                        brContinueGame.bottomRightPoint.y - brContinueGame.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(continueGame, brContinueGame.topLeftPoint.x + mainFrame.scrollX, brContinueGame.topLeftPoint.y + mainFrame.scrollY);
                break;
            case 8:
                g.setClip(brExit.topLeftPoint.x + mainFrame.scrollX, brExit.topLeftPoint.y + mainFrame.scrollY,
                        brExit.bottomRightPoint.x - brExit.topLeftPoint.x + mainFrame.scrollX,
                        brExit.bottomRightPoint.y - brExit.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(exit, brExit.topLeftPoint.x + mainFrame.scrollX, brExit.topLeftPoint.y + mainFrame.scrollY);
                break;
            default:
                log.error("Falsches Menu-Item!!! menuitem = {}", menuitem);
        }

        if (menuitem != 0) {
            olditem = menuitem;
        }
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        if (!e.isLeftClick()) {
            return;
        }

        GenericPoint pTemp = e.getPoint();

        // bei Click Ausserhalb zurueck ins Spiel
        if (!brGesamt.isPointInRect(pTemp)) {
            deactivate();
            mainFrame.whatScreen = ScreenType.NONE;
            mainFrame.repaint();
            return;
        }

        // Dalehrac
        if (brContinueGame.isPointInRect(pTemp)) {
            deactivate();
            mainFrame.repaint();
        }

        // Konc hry
        if (brExit.isPointInRect(pTemp)) {
            mainFrame.exitGame.activate(1);
            return;
        }

        // Wocinic
        if (brLoadGame.isPointInRect(pTemp)) {
            if (!mainFrame.storageManager.isLoadSaveSupported()) {
                return;
            }
            deactivate();
            mainFrame.constructLocation(102);
            mainFrame.whatScreen = ScreenType.LOAD_GAME;
            mmActive = true;
            mainFrame.repaint();
            return;
        }

        // Skladzic
        if (brSaveGame.isPointInRect(pTemp)) {
            // vom Intro aus darf nicht gespeichert werden
            if (introcall) {
                return;
            }
            if (!mainFrame.storageManager.isLoadSaveSupported()) {
                return;
            }
            deactivate();
            mainFrame.constructLocation(103);
            mainFrame.whatScreen = ScreenType.SAVE_GAME;
            mmActive = true;
            mainFrame.repaint();
            return;
        }

        // Info
        if (brInfo.isPointInRect(pTemp)) {
            deactivate();
            mainFrame.constructLocation(104);
            mainFrame.whatScreen = ScreenType.CREDITS;
            mmActive = true;
            mainFrame.repaint();
            return;
        }

        // Hornjoserbsce - Delnoserbsce
        if (brLangSwitch.isPointInRect(pTemp)) {
            Start.LANGUAGE++;
            // erlaube umschalten auf deutsch
            if (Start.LANGUAGE == 4) { //TODO: This is weird
                Start.LANGUAGE = 1;
            }
            initRec();

            switch (Start.LANGUAGE) {
                case 1:
                    gameProperties.setProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, "1");
                    break;
                case 2:
                    gameProperties.setProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, "2");
                    break;
                case 3:
                    gameProperties.setProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, "3");
                    break;
            }

            mainFrame.isClipSet = false;
            mainFrame.repaint();
        }

        // Nowostart
        if (brNewGame.isPointInRect(pTemp)) {
            mainFrame.exitGame.activate(2);
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {

        // Highlight im Menue festlegen
        menuitem = 0;
        if (brNewGame.isPointInRect(pTemp)) {
            menuitem = 1;
        }
        if (brLoadGame.isPointInRect(pTemp) &&
                mainFrame.storageManager.isLoadSaveSupported()) {
            menuitem = 2;
        }

        // Speichern nicht im Introscreen!!
        if (brSaveGame.isPointInRect(pTemp) && !introcall &&
                mainFrame.storageManager.isLoadSaveSupported()) {
            menuitem = 3;
        }
        if (brLangSwitch.isPointInRect(pTemp)) {
            menuitem = 4;
        }
        if (brInfo.isPointInRect(pTemp)) {
            menuitem = 6;
        }
        if (brContinueGame.isPointInRect(pTemp)) {
            menuitem = 7;
        }
        if (brExit.isPointInRect(pTemp)) {
            menuitem = 8;
        }

        // wenn noetig , dann Neuzeichnen!
        if (paintCall) {
            paintCall = false;
            mainFrame.setCursor(mainFrame.cursorNormal);
            return;
        }
        if (menuitem != olditem) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent() {
        menuitem = 0;
        mainFrame.repaint();
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();
        if (key == GenericKeyEvent.VK_ESCAPE) {
            deactivate();
            mainFrame.repaint();
        }
    }


    // Deaktivieren
    private void deactivate() {
        menuitem = 0;
        mainFrame.whatScreen = ScreenType.NONE;
        mainFrame.isClipSet = false;
        mmActive = false;
    }
}