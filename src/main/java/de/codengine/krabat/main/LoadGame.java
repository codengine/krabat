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

public class LoadGame extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(LoadGame.class);
    private boolean paintCall = false;

    private GenericImage loadScreen;
    private final GenericImage arrowUp;
    private final GenericImage arrowDown;
    private GenericImage woci;
    private GenericImage empty;
    private final GenericPoint pLO;
    private final BorderRect brGesamt;
    private final BorderRect brPfeil;
    private BorderRect brWoci;
    private final GenericColor inactive = new GenericColor(156, 132, 107);

    private int menuItem = 0;
    private int oldItem = 0;
    private int nFieldActive = -1;
    private int oFieldActive = -1;
    private int selected = -1;
    private int unselected = -1;

    private final SavegameData[] dir;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public LoadGame(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        pLO = new GenericPoint(31, 31);

        // Images und Borderrects je nach Sprache
        initRec();

        // Rechtecke im Inventar-Fenster festlegen
        brGesamt = new BorderRect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);
        brPfeil = mainFrame.inventory.brArrowLeft;
        arrowUp = mainFrame.inventory.arrowLeft;
        arrowDown = mainFrame.inventory.arrowLeftDisabled;

        // Spielstaende einlesen
        dir = new SavegameData[7];
        for (int i = 49; i <= 54; ++i) {
            dir[i - 48] = new SavegameData(mainFrame);
            dir[i - 48].getSavedGame(i - 48);
        }

        mainFrame.freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    private void initRec() {
        //TODO: Add graphics for third language
        switch (Start.LANGUAGE) {
            case 1: // Hornjos
            case 3: // temporaer Deutsch bekommt Hornjos
                // Bilder rein
                loadScreen = getPicture("gfx/mainmenu/load-b.png");
                woci = getPicture("gfx/mainmenu/m-woci.png");
                empty = getPicture("gfx/mainmenu/leerzelle.png");

                // Rects festlegen
                brWoci = new BorderRect(pLO.x + 369, pLO.y + 327,
                        pLO.x + 489, pLO.y + 355);
                break;

            case 2: // Delnjos
                // Bilder rein
                loadScreen = getPicture("gfx/mainmenu/load-db.png");
                woci = getPicture("gfx/mainmenu/d-woci.png");
                empty = getPicture("gfx/mainmenu/leerzelle.png");

                // Rects festlegen
                brWoci = new BorderRect(pLO.x + 356, pLO.y + 327,
                        pLO.x + 489, pLO.y + 355);
                break;

        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLoadGame(GenericDrawingContext g) {

        log.trace("********** PaintLaden!");

        // Laden-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 964);
            g.drawImage(loadScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            // Datum und GenericImage jedes Spielstandes anzeigen
            for (int i = 1; i <= 6; ++i) {
                GenericPoint outputTextPos = getCurrentXY(i - 1);
                if (dir[i].location != 0) {
                    String outputText = dir[i].convertTime();
                    g.drawImage(dir[i].darkImage, outputTextPos.x + mainFrame.scrollX + 1,
                            outputTextPos.y + mainFrame.scrollY + 1);
                    outputTextPos.y += 87;
                    mainFrame.imageFont.drawString(g, outputText, outputTextPos.x + mainFrame.scrollX,
                            outputTextPos.y + mainFrame.scrollY, 0xffff0000);
                } else {
                    g.drawImage(empty, outputTextPos.x + mainFrame.scrollX + 1,
                            outputTextPos.y + mainFrame.scrollY + 1);
                }
            }
        }

        // Ist ein Feld weg vom Cursor ? Dann roten Rahmen loeschen
        if (oFieldActive >= 0) {
            g.setColor(inactive);
            GenericPoint pTemp = getCurrentXY(oFieldActive);
            g.drawRect(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY, 119, 89);
            oFieldActive = -1;
        }

        // Ist ein Feld unter Cursor ? Dann roten Rahmen drum
        if (nFieldActive >= 0) {
            g.setColor(GenericColor.RED);
            GenericPoint pTemp = getCurrentXY(nFieldActive);
            g.drawRect(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY, 119, 89);
            oFieldActive = nFieldActive;
        }

        // Demarkiertes Feld mit richtigem Geisterimage ueberpinseln
        if (unselected != -1) {
            GenericPoint pTemp = getCurrentXY(unselected);
            g.drawImage(dir[unselected + 1].darkImage, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            unselected = -1;
        }

        // Markiertes Feld mit richtigem GenericImage ueberpinseln
        if (selected != -1) {
            GenericPoint pTemp = getCurrentXY(selected);
            g.drawImage(dir[selected + 1].image, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            unselected = selected;
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (oldItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowDown, 119 + mainFrame.scrollX, 349 + mainFrame.scrollY);
                break;
            case 2:
                GenericRectangle tep = g.getClipBounds();
                g.setClip(brWoci.topLeftPoint.x + mainFrame.scrollX, brWoci.topLeftPoint.y + mainFrame.scrollY,
                        brWoci.bottomRightPoint.x - brWoci.topLeftPoint.x + mainFrame.scrollX,
                        brWoci.bottomRightPoint.y - brWoci.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(loadScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                g.setClip(tep);
                break;
            default:
                log.error("Falsches Menu-Item zum loeschen!!! olditem = {}", oldItem);
        }
        if (oldItem != 0) {
            oldItem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrowUp, 121 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            case 2:
                GenericRectangle tepm = g.getClipBounds();
                g.setClip(brWoci.topLeftPoint.x + mainFrame.scrollX, brWoci.topLeftPoint.y + mainFrame.scrollY,
                        brWoci.bottomRightPoint.x - brWoci.topLeftPoint.x + mainFrame.scrollX,
                        brWoci.bottomRightPoint.y - brWoci.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(woci, brWoci.topLeftPoint.x + mainFrame.scrollX, brWoci.topLeftPoint.y + mainFrame.scrollY);
                g.setClip(tepm);
                break;
            default:
                log.error("Falsches Menu-Item!!! menuitem = {}", menuItem);
        }
        if (menuItem != 0) {
            oldItem = menuItem;
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
            return;
        }

        // Verlassen, wenn auf Pfeil links gedrueckt
        if (brPfeil.isPointInRect(pTemp)) {
            deactivate();
            return;
        }

        // Laden, wenn auf wocinic gedrueckt und erlaubt
        if (brWoci.isPointInRect(pTemp) && selected != -1) {
            dir[selected + 1].load();
            mainFrame.mainMenu.mmActive = false;

            // Introcall - Variable zuruecksetzen
            mainFrame.mainMenu.introcall = false;

            // hier das Hauptmenue auf moegliche neue Sprache zuruecksetzen
            mainFrame.mainMenu.initRec();

            deactivate();
            return;
        }

        // GenericImage erhellen, wenn draufgeklickt
        for (int i = 0; i <= 5; ++i) {
            if (getCurrentRect(i).isPointInRect(pTemp) && dir[i + 1].location != 0) {
                if (selected != i) {
                    selected = i;
                }
                if (mainFrame.isDoubleClick) {

                    // bei Doppelklick sofort Laden
                    dir[selected + 1].load();
                    mainFrame.mainMenu.mmActive = false;

                    // Introcall - Variable zuruecksetzen
                    mainFrame.mainMenu.introcall = false;

                    // moegliche Sprachenumschaltung im Hauptmenue aktivieren
                    mainFrame.mainMenu.initRec();

                    deactivate();
                    return;
                }
                mainFrame.repaint();
                return;
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Feld feststellen, wo roter Rahmen drumgemalt werden muss
        nFieldActive = -1;
        for (int i = 0; i < 6; i++) {
            if (getCurrentRect(i).isPointInRect(pTemp) && dir[i + 1].location != 0) {
                nFieldActive = i;
            }
        }

        //Menueitem zum Highlighten festlegen
        menuItem = 0;
        if (brPfeil.isPointInRect(pTemp)) {
            menuItem = 1;
        }
        if (brWoci.isPointInRect(pTemp) && selected != -1) {
            menuItem = 2;
        }

        // wenn noetig , dann Neuzeichnen!
        if (paintCall) {
            paintCall = false;
            mainFrame.setCursor(mainFrame.cursorNormal);
            return;
        }
        if (menuItem != oldItem || oFieldActive != nFieldActive) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent() {
        menuItem = 0;
        nFieldActive = -1;
        mainFrame.repaint();
    }


    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();

        // Bei Escape Laden verlassen
        if (key == GenericKeyEvent.VK_ESCAPE) {
            deactivate();
        }
    }


    // Deaktivieren ////////
    private void deactivate() {
        menuItem = 0;
        nFieldActive = -1;
        selected = -1;
        mainFrame.isClipSet = false;
        mainFrame.destructLocation(102);
        if (mainFrame.mainMenu.mmActive) {
            mainFrame.whatScreen = ScreenType.MAIN_MENU;
        } else {
            mainFrame.whatScreen = ScreenType.NONE;
        }
        mainFrame.repaint();
    }

    // Berechnungsroutine Spielstandsfensternummer - X/Y-Koordinaten//////////////
    private BorderRect getCurrentRect(int number) {
        GenericPoint topLeft = new GenericPoint(getCurrentXY(number));
        return new BorderRect(topLeft.x, topLeft.y, topLeft.x + 120, topLeft.y + 90);
    }

    private GenericPoint getCurrentXY(int number) {
        GenericPoint topLeft = new GenericPoint();
        topLeft.x = 117 + number % 3 * 142;
        topLeft.y = 89 + number / 3 * 112;
        return topLeft;
    }
}