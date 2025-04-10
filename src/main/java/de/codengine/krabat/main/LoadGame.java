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

public class LoadGame extends Mainanim {
    private static final Logger log = LoggerFactory.getLogger(LoadGame.class);
    private boolean Paintcall = false;

    private GenericImage LScreen;
    private final GenericImage Pfeil;
    private final GenericImage DPfeil;
    private GenericImage Woci;
    private GenericImage Empty;
    private final GenericPoint pLO;
    private final Borderrect brGesamt;
    private final Borderrect brPfeil;
    private Borderrect brWoci;
    private final GenericColor inakt = new GenericColor(156, 132, 107);

    private int menuitem = 0;
    private int olditem = 0;
    private int nFeldAktiv = -1;
    private int oFeldAktiv = -1;
    private int selected = -1;
    private int unselected = -1;

    private final Spielstand[] Dir;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public LoadGame(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        pLO = new GenericPoint(31, 31);

        // Images und Borderrects je nach Sprache
        InitRec();

        // Rechtecke im Inventar-Fenster festlegen
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);
        brPfeil = mainFrame.inventory.brPfeill;
        Pfeil = mainFrame.inventory.Pfeill;
        DPfeil = mainFrame.inventory.DPfeill;

        // Spielstaende einlesen
        Dir = new Spielstand[7];
        for (int i = 49; i <= 54; ++i) {
            Dir[i - 48] = new Spielstand(mainFrame);
            Dir[i - 48].GetSavedSpiel(i - 48);
        }

        mainFrame.Freeze(false);
        mainFrame.setCursor(mainFrame.cursorNormal);
    }

    private void InitRec() {
        //TODO: Add graphics for third language
        switch (Start.language) {
            case 1: // Hornjos
            case 3: // temporaer Deutsch bekommt Hornjos
                // Bilder rein
                LScreen = getPicture("gfx/mainmenu/load-b.png");
                Woci = getPicture("gfx/mainmenu/m-woci.png");
                Empty = getPicture("gfx/mainmenu/leerzelle.png");

                // Rects festlegen
                brWoci = new Borderrect(pLO.x + 369, pLO.y + 327,
                        pLO.x + 489, pLO.y + 355);
                break;

            case 2: // Delnjos
                // Bilder rein
                LScreen = getPicture("gfx/mainmenu/load-db.png");
                Woci = getPicture("gfx/mainmenu/d-woci.png");
                Empty = getPicture("gfx/mainmenu/leerzelle.png");

                // Rects festlegen
                brWoci = new Borderrect(pLO.x + 356, pLO.y + 327,
                        pLO.x + 489, pLO.y + 355);
                break;

        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLaden(GenericDrawingContext g) {

        log.trace("********** PaintLaden!");

        // Laden-Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 964);
            g.drawImage(LScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
            g.setClip(90 + mainFrame.scrollX, 70 + mainFrame.scrollY, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            // Datum und GenericImage jedes Spielstandes anzeigen
            for (int i = 1; i <= 6; ++i) {
                GenericPoint outputTextPos = GetCurrentXY(i - 1);
                if (Dir[i].Location != 0) {
                    String outputText = Dir[i].ConvertTime();
                    g.drawImage(Dir[i].DarkPicture, outputTextPos.x + mainFrame.scrollX + 1,
                            outputTextPos.y + mainFrame.scrollY + 1);
                    outputTextPos.y += 87;
                    mainFrame.imageFont.drawString(g, outputText, outputTextPos.x + mainFrame.scrollX,
                            outputTextPos.y + mainFrame.scrollY, 0xffff0000);
                } else {
                    g.drawImage(Empty, outputTextPos.x + mainFrame.scrollX + 1,
                            outputTextPos.y + mainFrame.scrollY + 1);
                }
            }
        }

        // Ist ein Feld weg vom Cursor ? Dann roten Rahmen loeschen
        if (oFeldAktiv >= 0) {
            g.setColor(inakt);
            GenericPoint pTemp = GetCurrentXY(oFeldAktiv);
            g.drawRect(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY, 119, 89);
            oFeldAktiv = -1;
        }

        // Ist ein Feld unter Cursor ? Dann roten Rahmen drum
        if (nFeldAktiv >= 0) {
            g.setColor(GenericColor.red);
            GenericPoint pTemp = GetCurrentXY(nFeldAktiv);
            g.drawRect(pTemp.x + mainFrame.scrollX, pTemp.y + mainFrame.scrollY, 119, 89);
            oFeldAktiv = nFeldAktiv;
        }

        // Demarkiertes Feld mit richtigem Geisterimage ueberpinseln
        if (unselected != -1) {
            GenericPoint pTemp = GetCurrentXY(unselected);
            g.drawImage(Dir[unselected + 1].DarkPicture, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            unselected = -1;
        }

        // Markiertes Feld mit richtigem GenericImage ueberpinseln
        if (selected != -1) {
            GenericPoint pTemp = GetCurrentXY(selected);
            g.drawImage(Dir[selected + 1].Picture, pTemp.x + mainFrame.scrollX + 1, pTemp.y + mainFrame.scrollY + 1);
            unselected = selected;
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(DPfeil, 119 + mainFrame.scrollX, 349 + mainFrame.scrollY);
                break;
            case 2:
                GenericRectangle tep = g.getClipBounds();
                g.setClip(brWoci.lo_point.x + mainFrame.scrollX, brWoci.lo_point.y + mainFrame.scrollY,
                        brWoci.ru_point.x - brWoci.lo_point.x + mainFrame.scrollX,
                        brWoci.ru_point.y - brWoci.lo_point.y + mainFrame.scrollY);
                g.drawImage(LScreen, pLO.x + mainFrame.scrollX, pLO.y + mainFrame.scrollY);
                g.setClip(tep);
                break;
            default:
                log.error("Falsches Menu-Item zum loeschen!!! olditem = {}", olditem);
        }
        if (olditem != 0) {
            olditem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuitem) {
            case 0:
                break;
            case 1:
                g.drawImage(Pfeil, 121 + mainFrame.scrollX, 350 + mainFrame.scrollY);
                break;
            case 2:
                GenericRectangle tepm = g.getClipBounds();
                g.setClip(brWoci.lo_point.x + mainFrame.scrollX, brWoci.lo_point.y + mainFrame.scrollY,
                        brWoci.ru_point.x - brWoci.lo_point.x + mainFrame.scrollX,
                        brWoci.ru_point.y - brWoci.lo_point.y + mainFrame.scrollY);
                g.drawImage(Woci, brWoci.lo_point.x + mainFrame.scrollX, brWoci.lo_point.y + mainFrame.scrollY);
                g.setClip(tepm);
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
            mainFrame.whatScreen = ScreenType.NONE;
            return;
        }

        // Verlassen, wenn auf Pfeil links gedrueckt
        if (brPfeil.IsPointInRect(pTemp)) {
            Deactivate();
            return;
        }

        // Laden, wenn auf wocinic gedrueckt und erlaubt
        if (brWoci.IsPointInRect(pTemp) && selected != -1) {
            Dir[selected + 1].Load();
            mainFrame.mainMenu.MMactive = false;

            // Introcall - Variable zuruecksetzen
            mainFrame.mainMenu.introcall = false;

            // hier das Hauptmenue auf moegliche neue Sprache zuruecksetzen
            mainFrame.mainMenu.InitRec();

            Deactivate();
            return;
        }

        // GenericImage erhellen, wenn draufgeklickt
        for (int i = 0; i <= 5; ++i) {
            if (GetCurrentRect(i).IsPointInRect(pTemp) && Dir[i + 1].Location != 0) {
                if (selected != i) {
                    selected = i;
                }
                if (mainFrame.isDoubleClick) {

                    // bei Doppelklick sofort Laden
                    Dir[selected + 1].Load();
                    mainFrame.mainMenu.MMactive = false;

                    // Introcall - Variable zuruecksetzen
                    mainFrame.mainMenu.introcall = false;

                    // moegliche Sprachenumschaltung im Hauptmenue aktivieren
                    mainFrame.mainMenu.InitRec();

                    Deactivate();
                    return;
                }
                mainFrame.repaint();
                return;
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Feld feststellen, wo roter Rahmen drumgemalt werden muss
        nFeldAktiv = -1;
        for (int i = 0; i < 6; i++) {
            if (GetCurrentRect(i).IsPointInRect(pTemp) && Dir[i + 1].Location != 0) {
                nFeldAktiv = i;
            }
        }

        //Menueitem zum Highlighten festlegen
        menuitem = 0;
        if (brPfeil.IsPointInRect(pTemp)) {
            menuitem = 1;
        }
        if (brWoci.IsPointInRect(pTemp) && selected != -1) {
            menuitem = 2;
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
            mainFrame.setCursor(mainFrame.cursorNormal);
            return;
        }
        if (menuitem != olditem || oFeldAktiv != nFeldAktiv) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent() {
        menuitem = 0;
        nFeldAktiv = -1;
        mainFrame.repaint();
    }


    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();

        // Bei Escape Laden verlassen
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
        }
    }


    // Deaktivieren ////////
    private void Deactivate() {
        menuitem = 0;
        nFeldAktiv = -1;
        selected = -1;
        mainFrame.isClipSet = false;
        mainFrame.DestructLocation(102);
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
}