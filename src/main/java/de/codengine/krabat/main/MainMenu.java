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

public class MainMenu extends Mainanim {
    private static final Logger log = LoggerFactory.getLogger(MainMenu.class);
    private GenericImage screen;
    private GenericImage newGame;
    private GenericImage loadGame;
    private GenericImage saveGame;
    private GenericImage info;
    private GenericImage continueGame;
    private GenericImage exit;
    private GenericImage langSwitch;
    private Borderrect brGesamt;
    private Borderrect brNewGame;
    private Borderrect brLoadGame;
    private Borderrect brSaveGame;
    private Borderrect brInfo;
    private Borderrect brContinueGame;
    private Borderrect brExit;
    private Borderrect brLangSwitch;

    private final GenericPoint pLO;

    private int menuitem = 0;
    private int olditem = 0;

    private boolean Paintcall = false;
    public boolean MMactive;
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
        InitRec();

    }

    // hier der Init je nach Sprache
    public void InitRec() {
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46, pLO.x + 513, pLO.y + 380);
        screen = getPicture("gfx/mainmenu/main-menu.png", true);
        int baseX1 = pLO.x + 89;
        int baseXRight = baseX1 + 396;
        brInfo = new Borderrect(baseX1, pLO.y + 314,pLO.x + 159, pLO.y + 336);

        langSwitch = getPicture("gfx/mainmenu/lang-switch.png", true);
        info = getPicture("gfx/mainmenu/info.png", true);

        continueGame = getPicture("gfx/mainmenu/continue.png", true);
        loadGame = getPicture("gfx/mainmenu/load.png", true);
        saveGame = getPicture("gfx/mainmenu/save.png", true);
        newGame = getPicture("gfx/mainmenu/new-game.png", true);
        exit = getPicture("gfx/mainmenu/exit.png", true);

        if (Start.language == 2) {
            brLangSwitch = new Borderrect(baseX1, pLO.y + 221,pLO.x + 227, pLO.y + 267);
            brContinueGame = new Borderrect(pLO.x + 297, pLO.y + 65, pLO.x + 486, pLO.y + 93);
            brLoadGame = new Borderrect(pLO.x + 352, pLO.y + 147, baseXRight, pLO.y + 175);
            brSaveGame = new Borderrect(pLO.x + 302, pLO.y + 186, baseXRight, pLO.y + 214);
            brNewGame = new Borderrect(pLO.x + 287, pLO.y + 275, baseXRight, pLO.y + 297);
            brExit = new Borderrect(pLO.x + 291, pLO.y + 308, baseXRight, pLO.y + 336);
        } else {
            brLangSwitch = new Borderrect(baseX1, pLO.y + 221, baseX1 + langSwitch.getWidth(), pLO.y + 221 + langSwitch.getHeight());
            brContinueGame = new Borderrect(baseXRight - continueGame.getWidth(), pLO.y + 65, baseXRight, pLO.y + 65 + continueGame.getHeight());
            brLoadGame = new Borderrect(baseXRight - loadGame.getWidth(), pLO.y + 147, baseXRight, pLO.y + 147 + loadGame.getHeight());
            brSaveGame = new Borderrect(baseXRight - saveGame.getWidth(), pLO.y + 186, baseXRight, pLO.y + 186 + saveGame.getHeight());
            brNewGame = new Borderrect(baseXRight - newGame.getWidth(), pLO.y + 275, baseXRight, pLO.y + 275 + newGame.getHeight());
            brExit = new Borderrect(baseXRight - exit.getWidth(), pLO.y + 308, baseXRight, pLO.y + 308 + exit.getHeight());
        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintMainmenu(GenericDrawingContext g) {
        // Mainmenu-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 964);
            g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1: // Nowostart
                g.setClip(brNewGame.lo_point.x + mainFrame.scrollX, brNewGame.lo_point.y + mainFrame.scrollY,
                        brNewGame.ru_point.x - brNewGame.lo_point.x + mainFrame.scrollX,
                        brNewGame.ru_point.y - brNewGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 2: // Wocinic
                g.setClip(brLoadGame.lo_point.x + mainFrame.scrollX, brLoadGame.lo_point.y + mainFrame.scrollY,
                        brLoadGame.ru_point.x - brLoadGame.lo_point.x + mainFrame.scrollX,
                        brLoadGame.ru_point.y - brLoadGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 3: // Skladzic
                g.setClip(brSaveGame.lo_point.x + mainFrame.scrollX, brSaveGame.lo_point.y + mainFrame.scrollY,
                        brSaveGame.ru_point.x - brSaveGame.lo_point.x + mainFrame.scrollX,
                        brSaveGame.ru_point.y - brSaveGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 4: // Rec
                g.setClip(brLangSwitch.lo_point.x + mainFrame.scrollX, brLangSwitch.lo_point.y + mainFrame.scrollY,
                        brLangSwitch.ru_point.x - brLangSwitch.lo_point.x + mainFrame.scrollX,
                        brLangSwitch.ru_point.y - brLangSwitch.lo_point.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 6: // Info
                g.setClip(brInfo.lo_point.x + mainFrame.scrollX, brInfo.lo_point.y + mainFrame.scrollY,
                        brInfo.ru_point.x - brInfo.lo_point.x + mainFrame.scrollX,
                        brInfo.ru_point.y - brInfo.lo_point.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 7: // Dalehrac
                g.setClip(brContinueGame.lo_point.x + mainFrame.scrollX, brContinueGame.lo_point.y + mainFrame.scrollY,
                        brContinueGame.ru_point.x - brContinueGame.lo_point.x + mainFrame.scrollX,
                        brContinueGame.ru_point.y - brContinueGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(screen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                break;
            case 8: // Konc
                g.setClip(brExit.lo_point.x + mainFrame.scrollX, brExit.lo_point.y + mainFrame.scrollY,
                        brExit.ru_point.x - brExit.lo_point.x + mainFrame.scrollX,
                        brExit.ru_point.y - brExit.lo_point.y + mainFrame.scrollY);
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
                g.setClip(brNewGame.lo_point.x + mainFrame.scrollX, brNewGame.lo_point.y + mainFrame.scrollY,
                        brNewGame.ru_point.x - brNewGame.lo_point.x + mainFrame.scrollX,
                        brNewGame.ru_point.y - brNewGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(newGame, brNewGame.lo_point.x + mainFrame.scrollX, brNewGame.lo_point.y + mainFrame.scrollY);
                break;
            case 2: // Wocinic
                g.setClip(brLoadGame.lo_point.x + mainFrame.scrollX, brLoadGame.lo_point.y + mainFrame.scrollY,
                        brLoadGame.ru_point.x - brLoadGame.lo_point.x + mainFrame.scrollX,
                        brLoadGame.ru_point.y - brLoadGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(loadGame, brLoadGame.lo_point.x + mainFrame.scrollX, brLoadGame.lo_point.y + mainFrame.scrollY);
                break;
            case 3: // Skladzic
                g.setClip(brSaveGame.lo_point.x + mainFrame.scrollX, brSaveGame.lo_point.y + mainFrame.scrollY,
                        brSaveGame.ru_point.x - brSaveGame.lo_point.x + mainFrame.scrollX,
                        brSaveGame.ru_point.y - brSaveGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(saveGame, brSaveGame.lo_point.x + mainFrame.scrollX, brSaveGame.lo_point.y + mainFrame.scrollY);
                break;
            case 4: // Rec
                g.setClip(brLangSwitch.lo_point.x + mainFrame.scrollX, brLangSwitch.lo_point.y + mainFrame.scrollY,
                        brLangSwitch.ru_point.x - brLangSwitch.lo_point.x + mainFrame.scrollX,
                        brLangSwitch.ru_point.y - brLangSwitch.lo_point.y + mainFrame.scrollY);
                g.drawImage(langSwitch, brLangSwitch.lo_point.x + mainFrame.scrollX, brLangSwitch.lo_point.y + mainFrame.scrollY);
                break;
            case 6: // Info
                g.setClip(brInfo.lo_point.x + mainFrame.scrollX, brInfo.lo_point.y + mainFrame.scrollY,
                        brInfo.ru_point.x - brInfo.lo_point.x + mainFrame.scrollX,
                        brInfo.ru_point.y - brInfo.lo_point.y + mainFrame.scrollY);
                g.drawImage(info, brInfo.lo_point.x + mainFrame.scrollX, brInfo.lo_point.y + mainFrame.scrollY);
                break;
            case 7: // Dalehrac
                g.setClip(brContinueGame.lo_point.x + mainFrame.scrollX, brContinueGame.lo_point.y + mainFrame.scrollY,
                        brContinueGame.ru_point.x - brContinueGame.lo_point.x + mainFrame.scrollX,
                        brContinueGame.ru_point.y - brContinueGame.lo_point.y + mainFrame.scrollY);
                g.drawImage(continueGame, brContinueGame.lo_point.x + mainFrame.scrollX, brContinueGame.lo_point.y + mainFrame.scrollY);
                break;
            case 8:
                g.setClip(brExit.lo_point.x + mainFrame.scrollX, brExit.lo_point.y + mainFrame.scrollY,
                        brExit.ru_point.x - brExit.lo_point.x + mainFrame.scrollX,
                        brExit.ru_point.y - brExit.lo_point.y + mainFrame.scrollY);
                g.drawImage(exit, brExit.lo_point.x + mainFrame.scrollX, brExit.lo_point.y + mainFrame.scrollY);
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
        if (!brGesamt.IsPointInRect(pTemp)) {
            Deactivate();
            mainFrame.whatScreen = 0;
            mainFrame.repaint();
            return;
        }

        // Dalehrac
        if (brContinueGame.IsPointInRect(pTemp)) {
            Deactivate();
            mainFrame.repaint();
        }

        // Konc hry
        if (brExit.IsPointInRect(pTemp)) {
            mainFrame.exitGame.Activate(1);
            return;
        }

        // Wocinic
        if (brLoadGame.IsPointInRect(pTemp)) {
            if (!mainFrame.storageManager.isLoadSaveSupported()) {
                return;
            }
            Deactivate();
            mainFrame.ConstructLocation(102);
            mainFrame.whatScreen = 3;
            MMactive = true;
            mainFrame.repaint();
            return;
        }

        // Skladzic
        if (brSaveGame.IsPointInRect(pTemp)) {
            // vom Intro aus darf nicht gespeichert werden
            if (introcall) {
                return;
            }
            if (!mainFrame.storageManager.isLoadSaveSupported()) {
                return;
            }
            Deactivate();
            mainFrame.ConstructLocation(103);
            mainFrame.whatScreen = 4;
            MMactive = true;
            mainFrame.repaint();
            return;
        }

        // Info
        if (brInfo.IsPointInRect(pTemp)) {
            Deactivate();
            mainFrame.ConstructLocation(104);
            mainFrame.whatScreen = 5;
            MMactive = true;
            mainFrame.repaint();
            return;
        }

        // Hornjoserbsce - Delnoserbsce
        if (brLangSwitch.IsPointInRect(pTemp)) {
            Start.language++;
            // erlaube umschalten auf deutsch
            if (Start.language == 4) { //TODO: This is weird
                Start.language = 1;
            }
            InitRec();

            switch (Start.language) {
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
        if (brNewGame.IsPointInRect(pTemp)) {
            mainFrame.exitGame.Activate(2);
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {

        // Highlight im Menue festlegen
        menuitem = 0;
        if (brNewGame.IsPointInRect(pTemp)) {
            menuitem = 1;
        }
        if (brLoadGame.IsPointInRect(pTemp) &&
                mainFrame.storageManager.isLoadSaveSupported()) {
            menuitem = 2;
        }

        // Speichern nicht im Introscreen!!
        if (brSaveGame.IsPointInRect(pTemp) && !introcall &&
                mainFrame.storageManager.isLoadSaveSupported()) {
            menuitem = 3;
        }
        if (brLangSwitch.IsPointInRect(pTemp)) {
            menuitem = 4;
        }
        // if (brNieders.IsPointInRect   (pTemp) == true) menuitem = 5;
        if (brInfo.IsPointInRect(pTemp)) {
            menuitem = 6;
        }
        if (brContinueGame.IsPointInRect(pTemp)) {
            menuitem = 7;
        }
        if (brExit.IsPointInRect(pTemp)) {
            menuitem = 8;
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
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
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
            mainFrame.repaint();
        }
    }


    // Deaktivieren
    private void Deactivate() {
        menuitem = 0;
        mainFrame.whatScreen = 0;
        mainFrame.isClipSet = false;
        MMactive = false;
    }
}