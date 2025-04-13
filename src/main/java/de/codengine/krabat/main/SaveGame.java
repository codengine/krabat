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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SaveGame extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(SaveGame.class);
    private boolean paintCall = false;

    private final GenericPoint pLO;
    private GenericImage saveScreen;
    private final GenericImage arrow;
    private final GenericImage downArrow;
    private GenericImage saveButton;
    private GenericImage empty;
    private final BorderRect brGesamt;
    private final BorderRect brPfeil;
    private BorderRect brSaveButton;
    private final GenericColor inactive = new GenericColor(156, 132, 107);

    private int menuItem = 0;
    private int oldItem = 0;
    private int nFieldActive = -1;
    private int oFieldActive = -1;
    private int selected = -1;
    private int unselected = -1;

    private final SavegameData[] dir;
    private SavegameData currentSavegame;
    private GenericImage actualImage;

    public boolean saveIsValid = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public SaveGame(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        pLO = new GenericPoint(31, 31);

        initRec();

        // Rechtecke im Inventar-Fenster festlegen
        brGesamt = new BorderRect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);
        brPfeil = mainFrame.inventory.brArrowLeft;
        arrow = mainFrame.inventory.arrowLeft;
        downArrow = mainFrame.inventory.arrowLeftDisabled;

        // Spielstaende laden
        dir = new SavegameData[7];
        for (int i = 49; i <= 54; ++i) {
            dir[i - 48] = new SavegameData(mainFrame);
            dir[i - 48].getSavedGame(i - 48);
        }

        // aktuellen Spielstand (nicht komplett!!) erzeugen
        getActualSavegame();

        mainFrame.freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    // je nach Sprache init vornehmen
    private void initRec() {
        // Bilder rein
        saveScreen = getPicture("gfx/mainmenu/save-menu.png", true);
        saveButton = getPicture("gfx/mainmenu/save-button.png", true);

        // Rects festlegen
        int xOffset = 310 - ((saveButton.getWidth() - 179) / 2);
        int baseX = pLO.x + xOffset;
        int baseY = pLO.y + 327;
        brSaveButton = new BorderRect(baseX, baseY, baseX + saveButton.getWidth(), baseY + saveButton.getHeight());
        empty = getPicture("gfx/mainmenu/leerzelle.png");
        actualImage = mainFrame.saveImage;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintSaveGame(GenericDrawingContext g) {

        // Speichern - Background zeichnen
        String outputText;
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 484);
            g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            // Datum und GenericImage jedes Spielstandes anzeigen
            for (int i = 1; i <= 6; ++i) {
                GenericPoint outputTextPos = getCurrentXY(i - 1);
                if (dir[i].location != 0) {
                    outputText = dir[i].convertTime();
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

        // Testen, ob nach Save over Existing gespeichert werden darf
        if (saveIsValid) {
            saveIsValid = false;
            currentSavegame.save(selected + 1);
            deactivate();
            return;
        }

        // Ist ein Feld weg vom Cursor ? Dann roten Rahmen weg
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

        // Demarkiertes Feld mit richtigem Geisterimage �berpinseln und Datum Korrigieren!
        if (unselected != -1) {
            GenericPoint pTemp = getCurrentXY(unselected);
            if (dir[unselected + 1].location != 0) {
                g.drawImage(dir[unselected + 1].darkImage,
                        pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            } else {
                g.drawImage(empty, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            }
            pTemp.y += 87;
            g.setClip(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY + 4, 110, 20);
            g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            if (dir[unselected + 1].location != 0) {
                outputText = dir[unselected + 1].convertTime();
                mainFrame.imageFont.drawString(g, outputText, pTemp.x + mainFrame.scrollX,
                        pTemp.y + mainFrame.scrollY, 0xffff0000);
            }
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            unselected = -1;
        }

        // Markiertes Feld mit richtigem GenericImage �berpinseln und neues Datum hinzufuegen!
        if (selected != -1) {
            GenericPoint pTemp = getCurrentXY(selected);
            g.drawImage(currentSavegame.image, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            pTemp.y += 87;
            g.setClip(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY + 4, 110, 20);
            g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            outputText = currentSavegame.convertTime();
            mainFrame.imageFont.drawString(g, outputText, pTemp.x + mainFrame.scrollX,
                    pTemp.y + mainFrame.scrollY, 0xffff0000);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            unselected = selected;
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (oldItem) {
            case 0:
                break;
            case 1:
                g.drawImage(downArrow, 119 + mainFrame.scrollX, 349 + mainFrame.scrollY);
                break;
            case 2:
                GenericRectangle tep = g.getClipBounds();
                g.setClip(brSaveButton.topLeftPoint.x + mainFrame.scrollX, brSaveButton.topLeftPoint.y + mainFrame.scrollY,
                        brSaveButton.bottomRightPoint.x - brSaveButton.topLeftPoint.x + mainFrame.scrollX,
                        brSaveButton.bottomRightPoint.y - brSaveButton.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                g.setClip(tep);
                break;
            default:
                log.error("Falsches Menu-Item!!! olditem = {}", oldItem);
        }
        if (oldItem != 0) {
            oldItem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuItem) {
            case 0:
                break;
            case 1:
                g.drawImage(arrow, 121 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            case 2:
                GenericRectangle tepm = g.getClipBounds();
                g.setClip(brSaveButton.topLeftPoint.x + mainFrame.scrollX, brSaveButton.topLeftPoint.y + mainFrame.scrollY,
                        brSaveButton.bottomRightPoint.x - brSaveButton.topLeftPoint.x + mainFrame.scrollX,
                        brSaveButton.bottomRightPoint.y - brSaveButton.topLeftPoint.y + mainFrame.scrollY);
                g.drawImage(saveButton, brSaveButton.topLeftPoint.x + mainFrame.scrollX, brSaveButton.topLeftPoint.y + mainFrame.scrollY);
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

        // bei Pfeil links verlassen
        if (brPfeil.isPointInRect(pTemp)) {
            deactivate();
            return;
        }

        // Bei Speichern und erlaubt speichern
        if (brSaveButton.isPointInRect(pTemp) && selected != -1) {
            if (dir[selected + 1].location != 0) {
                // Sicherheitsabfrage aktivieren
                mainFrame.exitGame.activate(3);
                return;
            }
            currentSavegame.save(selected + 1);
            deactivate();
            return;
        }

        // bei Klick auf Spielstand aktuellen Spielstand darueberzeichnen
        for (int i = 0; i <= 5; ++i) {
            if (getCurrentRect(i).isPointInRect(pTemp)) {
                if (selected != i) {
                    selected = i;
                }
                if (mainFrame.isDoubleClick) {

                    // Bei Doppelklick sofort speichern
                    if (dir[selected + 1].location != 0) {
                        // Sicherheitsabfrage aktivieren
                        mainFrame.exitGame.activate(3);
                        return;
                    }
                    currentSavegame.save(selected + 1);
                    deactivate();
                    return;
                }
                mainFrame.repaint();
                return;
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // roten Rahmen zum Umranden festlegen
        nFieldActive = -1;
        for (int i = 0; i < 6; i++) {
            if (getCurrentRect(i).isPointInRect(pTemp)) {
                nFieldActive = i;
            }
        }

        // Menueitems fuer Highlight festlegen
        menuItem = 0;
        if (brPfeil.isPointInRect(pTemp)) {
            menuItem = 1;
        }
        if (brSaveButton.isPointInRect(pTemp) && selected != -1) {
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

        // Bei ESCAPE verlassen
        if (key == GenericKeyEvent.VK_ESCAPE) {
            deactivate();
        }
    }


    // Deaktivieren /////////
    private void deactivate() {
        menuItem = 0;
        nFieldActive = -1;
        mainFrame.isClipSet = false;
        mainFrame.destructLocation(103);
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

    private void getActualSavegame() {
        // Auslesen des Datums vorbereiten
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("ECT"));

        // erzeugt das Icon fuer den im Spiel aktuellen Spielstand
        int[] tempp = new int[10593];
        GenericToolkit.getDefaultToolkit().grabPixelsFromImage(actualImage, 0, 0, 118, 88, tempp, 0, 118);

        // erzeugt den aktuellen Spielstand (nicht komplett!!)
        currentSavegame = new SavegameData(mainFrame, tempp, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));
    }
}