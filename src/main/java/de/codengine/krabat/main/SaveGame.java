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
import de.codengine.krabat.anims.Mainanim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class SaveGame extends Mainanim {
    private static final Logger log = LoggerFactory.getLogger(SaveGame.class);
    private boolean Paintcall = false;

    private final GenericPoint pLO;
    private GenericImage saveScreen;
    private final GenericImage arrow;
    private final GenericImage downArrow;
    private GenericImage saveButton;
    private GenericImage empty;
    private final Borderrect brGesamt;
    private final Borderrect brPfeil;
    private Borderrect brSaveButton;
    private final GenericColor inactive = new GenericColor(156, 132, 107);

    private int menuItem = 0;
    private int oldItem = 0;
    private int nFieldActive = -1;
    private int oFieldActive = -1;
    private int selected = -1;
    private int unselected = -1;

    private final Spielstand[] Dir;
    private Spielstand currentSavegame;
    private GenericImage actualImage;

    public boolean saveIsValid = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public SaveGame(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        pLO = new GenericPoint(31, 31);

        InitRec();

        // Rechtecke im Inventar-Fenster festlegen
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);
        brPfeil = mainFrame.inventory.brPfeill;
        arrow = mainFrame.inventory.Pfeill;
        downArrow = mainFrame.inventory.DPfeill;

        // Spielstaende laden
        Dir = new Spielstand[7];
        for (int i = 49; i <= 54; ++i) {
            Dir[i - 48] = new Spielstand(mainFrame);
            Dir[i - 48].GetSavedSpiel(i - 48);
        }

        // aktuellen Spielstand (nicht komplett!!) erzeugen
        GetActualSpielstand();

        mainFrame.freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    // je nach Sprache init vornehmen
    private void InitRec() {
        // Bilder rein
        saveScreen = getPicture("gfx/mainmenu/save-menu.png", true);
        saveButton = getPicture("gfx/mainmenu/save-button.png", true);

        // Rects festlegen
        int xOffset = 310 - ((saveButton.getWidth() - 179) / 2);
        int baseX = pLO.x + xOffset;
        int baseY = pLO.y + 327;
        brSaveButton = new Borderrect(baseX, baseY,baseX + saveButton.getWidth(), baseY + saveButton.getHeight());
        empty = getPicture("gfx/mainmenu/leerzelle.png");
        actualImage = mainFrame.saveImage;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintSpeichern(GenericDrawingContext g) {

        // Speichern - Background zeichnen
        String outputText;
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 484);
            g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            // Datum und GenericImage jedes Spielstandes anzeigen
            for (int i = 1; i <= 6; ++i) {
                GenericPoint outputTextPos = GetCurrentXY(i - 1);
                if (Dir[i].Location != 0) {
                    outputText = Dir[i].ConvertTime();
                    g.drawImage(Dir[i].DarkPicture, outputTextPos.x + mainFrame.scrollX + 1,
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
            currentSavegame.Save(selected + 1);
            Deactivate();
            return;
        }

        // Ist ein Feld weg vom Cursor ? Dann roten Rahmen weg
        if (oFieldActive >= 0) {
            g.setColor(inactive);
            GenericPoint pTemp = GetCurrentXY(oFieldActive);
            g.drawRect(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY, 119, 89);
            oFieldActive = -1;
        }

        // Ist ein Feld unter Cursor ? Dann roten Rahmen drum
        if (nFieldActive >= 0) {
            g.setColor(GenericColor.red);
            GenericPoint pTemp = GetCurrentXY(nFieldActive);
            g.drawRect(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY, 119, 89);
            oFieldActive = nFieldActive;
        }

        // Demarkiertes Feld mit richtigem Geisterimage �berpinseln und Datum Korrigieren!
        if (unselected != -1) {
            GenericPoint pTemp = GetCurrentXY(unselected);
            if (Dir[unselected + 1].Location != 0) {
                g.drawImage(Dir[unselected + 1].DarkPicture,
                        pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            } else {
                g.drawImage(empty, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            }
            pTemp.y += 87;
            g.setClip(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY + 4, 110, 20);
            g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            if (Dir[unselected + 1].Location != 0) {
                outputText = Dir[unselected + 1].ConvertTime();
                mainFrame.imageFont.drawString(g, outputText, pTemp.x + mainFrame.scrollX,
                        pTemp.y + mainFrame.scrollY, 0xffff0000);
            }
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            unselected = -1;
        }

        // Markiertes Feld mit richtigem GenericImage �berpinseln und neues Datum hinzufuegen!
        if (selected != -1) {
            GenericPoint pTemp = GetCurrentXY(selected);
            g.drawImage(currentSavegame.Picture, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            pTemp.y += 87;
            g.setClip(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY + 4, 110, 20);
            g.drawImage(saveScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            outputText = currentSavegame.ConvertTime();
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
                g.setClip(brSaveButton.lo_point.x + mainFrame.scrollX, brSaveButton.lo_point.y + mainFrame.scrollY,
                        brSaveButton.ru_point.x - brSaveButton.lo_point.x + mainFrame.scrollX,
                        brSaveButton.ru_point.y - brSaveButton.lo_point.y + mainFrame.scrollY);
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
                g.setClip(brSaveButton.lo_point.x + mainFrame.scrollX, brSaveButton.lo_point.y + mainFrame.scrollY,
                        brSaveButton.ru_point.x - brSaveButton.lo_point.x + mainFrame.scrollX,
                        brSaveButton.ru_point.y - brSaveButton.lo_point.y + mainFrame.scrollY);
                g.drawImage(saveButton, brSaveButton.lo_point.x + mainFrame.scrollX, brSaveButton.lo_point.y + mainFrame.scrollY);
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
        if (!brGesamt.IsPointInRect(pTemp)) {
            Deactivate();
            mainFrame.whatScreen = ScreenType.NONE;
            return;
        }

        // bei Pfeil links verlassen
        if (brPfeil.IsPointInRect(pTemp)) {
            Deactivate();
            return;
        }

        // Bei Speichern und erlaubt speichern
        if (brSaveButton.IsPointInRect(pTemp) && selected != -1) {
            if (Dir[selected + 1].Location != 0) {
                // Sicherheitsabfrage aktivieren
                mainFrame.exitGame.Activate(3);
                return;
            }
            currentSavegame.Save(selected + 1);
            Deactivate();
            return;
        }

        // bei Klick auf Spielstand aktuellen Spielstand darueberzeichnen
        for (int i = 0; i <= 5; ++i) {
            if (GetCurrentRect(i).IsPointInRect(pTemp)) {
                if (selected != i) {
                    selected = i;
                }
                if (mainFrame.isDoubleClick) {

                    // Bei Doppelklick sofort speichern
                    if (Dir[selected + 1].Location != 0) {
                        // Sicherheitsabfrage aktivieren
                        mainFrame.exitGame.Activate(3);
                        return;
                    }
                    currentSavegame.Save(selected + 1);
                    Deactivate();
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
            if (GetCurrentRect(i).IsPointInRect(pTemp)) {
                nFieldActive = i;
            }
        }

        // Menueitems fuer Highlight festlegen
        menuItem = 0;
        if (brPfeil.IsPointInRect(pTemp)) {
            menuItem = 1;
        }
        if (brSaveButton.IsPointInRect(pTemp) && selected != -1) {
            menuItem = 2;
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
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
        int Taste = e.getKeyCode();

        // Bei ESCAPE verlassen
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
        }
    }


    // Deaktivieren /////////
    private void Deactivate() {
        menuItem = 0;
        nFieldActive = -1;
        mainFrame.isClipSet = false;
        mainFrame.destructLocation(103);
        if (mainFrame.mainMenu.MMactive) {
            mainFrame.whatScreen = ScreenType.MAIN_MENU;
        } else {
            mainFrame.whatScreen = ScreenType.NONE;
        }
        mainFrame.repaint();
    }

    // Berechnungsroutine Spielstandsfensternummer - X/Y-Koordinaten//////////////
    private Borderrect GetCurrentRect(int Number) {
        GenericPoint Pleftup = new GenericPoint(GetCurrentXY(Number));
        return new Borderrect(Pleftup.x, Pleftup.y, Pleftup.x + 120, Pleftup.y + 90);
    }

    private GenericPoint GetCurrentXY(int Number) {
        GenericPoint Pleftup = new GenericPoint();
        Pleftup.x = 117 + Number % 3 * 142;
        Pleftup.y = 89 + Number / 3 * 112;
        return Pleftup;
    }

    private void GetActualSpielstand() {
        // Auslesen des Datums vorbereiten
        GregorianCalendar Kal = new GregorianCalendar();
        Kal.setTimeZone(TimeZone.getTimeZone("ECT"));

        // erzeugt das Icon fuer den im Spiel aktuellen Spielstand
        int[] tempp = new int[10593];
        GenericToolkit.getDefaultToolkit().grabPixelsFromImage(actualImage, 0, 0, 118, 88, tempp, 0, 118);

        // erzeugt den aktuellen Spielstand (nicht komplett!!)
        currentSavegame = new Spielstand(mainFrame, tempp, Kal.get(Calendar.DAY_OF_MONTH), Kal.get(Calendar.MONTH) + 1,
                Kal.get(Calendar.YEAR));
    }
}