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
import de.codengine.krabat.platform.GenericImageObserver;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Skladzic extends Mainanim {
    private boolean Paintcall = false;

    private final GenericPoint pLO;
    private GenericImage LScreen;
    private final GenericImage Pfeil;
    private final GenericImage DPfeil;
    private GenericImage Sklad;
    private GenericImage Empty;
    private final Borderrect brGesamt;
    private final Borderrect brPfeil;
    private Borderrect brSklad;
    private final GenericColor inakt = new GenericColor(156, 132, 107);

    private int menuitem = 0;
    private int olditem = 0;
    private int nFeldAktiv = -1;
    private int oFeldAktiv = -1;
    private int selected = -1;
    private int unselected = -1;

    private final Spielstand[] Dir;
    private Spielstand Aktuell;
    private GenericImage actualImage;

    public boolean saveIsValid = false;

    private final GenericImageObserver observer = null;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Skladzic(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        pLO = new GenericPoint(31, 31);

        InitRec();

        // Rechtecke im Inventar-Fenster festlegen
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);
        brPfeil = mainFrame.inventory.brPfeill;
        Pfeil = mainFrame.inventory.Pfeill;
        DPfeil = mainFrame.inventory.DPfeill;

        // Spielstaende laden
        Dir = new Spielstand[7];
        for (int i = 49; i <= 54; ++i) {
            Dir[i - 48] = new Spielstand(mainFrame);
            Dir[i - 48].GetSavedSpiel(i - 48);
        }

        // aktuellen Spielstand (nicht komplett!!) erzeugen
        GetActualSpielstand();

        mainFrame.Freeze(false);
        mainFrame.setCursor(mainFrame.Normal);
    }

    // je nach Sprache init vornehmen
    private void InitRec() {
        switch (mainFrame.sprache) {
            case 1: // Hornjos
            case 3: // temporaer Deutsch bekommt Hornjos
                // Bilder rein
                LScreen = getPicture("gfx/mainmenu/save-b.gif");
                Sklad = getPicture("gfx/mainmenu/m-sklad.gif");
                Empty = getPicture("gfx/mainmenu/leerzelle.gif");

                // Rects festlegen
                brSklad = new Borderrect(pLO.x + 310, pLO.y + 327,
                        pLO.x + 489, pLO.y + 355);
                break;

            case 2: // Delnjos
                // Bilder rein
                LScreen = getPicture("gfx/mainmenu/save-db.gif");
                Sklad = getPicture("gfx/mainmenu/d-sklad.gif");
                Empty = getPicture("gfx/mainmenu/leerzelle.gif");

                // Rects festlegen
                brSklad = new Borderrect(pLO.x + 308, pLO.y + 327,
                        pLO.x + 491, pLO.y + 355);
                break;

        }
        actualImage = mainFrame.saveImage;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintSpeichern(GenericDrawingContext g) {

        // Speichern - Background zeichnen
        String outputText = "";
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            g.setClip(0, 0, 1284, 484);
            g.drawImage(LScreen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
            g.setClip(90 + mainFrame.scrollx, 70 + mainFrame.scrolly, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);

            // Datum und GenericImage jedes Spielstandes anzeigen
            for (int i = 1; i <= 6; ++i) {
                GenericPoint outputTextPos = GetCurrentXY(i - 1);
                if (Dir[i].Location != 0) {
                    outputText = Dir[i].ConvertTime();
                    g.drawImage(Dir[i].DarkPicture, outputTextPos.x + mainFrame.scrollx + 1,
                            outputTextPos.y + mainFrame.scrolly + 1, null);
                    outputTextPos.y += 87;
                    mainFrame.ifont.drawString(g, outputText, outputTextPos.x + mainFrame.scrollx,
                            outputTextPos.y + mainFrame.scrolly, 0xffff0000);
                } else {
                    g.drawImage(Empty, outputTextPos.x + mainFrame.scrollx + 1,
                            outputTextPos.y + mainFrame.scrolly + 1, null);
                }
            }
        }

        // Testen, ob nach Save over Existing gespeichert werden darf
        if (saveIsValid) {
            saveIsValid = false;
            Aktuell.Save(selected + 1);
            Deactivate();
            return;
        }

        // Ist ein Feld weg vom Cursor ? Dann roten Rahmen weg
        if (oFeldAktiv >= 0) {
            g.setColor(inakt);
            GenericPoint pTemp = GetCurrentXY(oFeldAktiv);
            g.drawRect(pTemp.x + mainFrame.scrollx, pTemp.y + mainFrame.scrolly, 119, 89);
            oFeldAktiv = -1;
        }

        // Ist ein Feld unter Cursor ? Dann roten Rahmen drum
        if (nFeldAktiv >= 0) {
            g.setColor(GenericColor.red);
            GenericPoint pTemp = GetCurrentXY(nFeldAktiv);
            g.drawRect(pTemp.x + mainFrame.scrollx, pTemp.y + mainFrame.scrolly, 119, 89);
            oFeldAktiv = nFeldAktiv;
        }

        // Demarkiertes Feld mit richtigem Geisterimage �berpinseln und Datum Korrigieren!
        if (unselected != -1) {
            GenericPoint pTemp = GetCurrentXY(unselected);
            if (Dir[unselected + 1].Location != 0) {
                g.drawImage(Dir[unselected + 1].DarkPicture,
                        pTemp.x + mainFrame.scrollx + 1, pTemp.y + mainFrame.scrolly + 1, null);
            } else {
                g.drawImage(Empty, pTemp.x + mainFrame.scrollx + 1, pTemp.y + mainFrame.scrolly + 1, null);
            }
            pTemp.y += 87;
            g.setClip(pTemp.x + mainFrame.scrollx, pTemp.y + mainFrame.scrolly + 4, 110, 20);
            g.drawImage(LScreen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
            if (Dir[unselected + 1].Location != 0) {
                outputText = Dir[unselected + 1].ConvertTime();
                mainFrame.ifont.drawString(g, outputText, pTemp.x + mainFrame.scrollx,
                        pTemp.y + mainFrame.scrolly, 0xffff0000);
            }
            g.setClip(90 + mainFrame.scrollx, 70 + mainFrame.scrolly, 550, 390);
            unselected = -1;
        }

        // Markiertes Feld mit richtigem GenericImage �berpinseln und neues Datum hinzufuegen!
        if (selected != -1) {
            GenericPoint pTemp = GetCurrentXY(selected);
            g.drawImage(Aktuell.Picture, pTemp.x + mainFrame.scrollx + 1, pTemp.y + mainFrame.scrolly + 1, null);
            pTemp.y += 87;
            g.setClip(pTemp.x + mainFrame.scrollx, pTemp.y + mainFrame.scrolly + 4, 110, 20);
            g.drawImage(LScreen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
            outputText = Aktuell.ConvertTime();
            mainFrame.ifont.drawString(g, outputText, pTemp.x + mainFrame.scrollx,
                    pTemp.y + mainFrame.scrolly, 0xffff0000);
            g.setClip(90 + mainFrame.scrollx, 70 + mainFrame.scrolly, 550, 390);
            unselected = selected;
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(DPfeil, 119 + mainFrame.scrollx, 349 + mainFrame.scrolly, observer);
                break;
            case 2:
                GenericRectangle tep = g.getClipBounds();
                g.setClip(brSklad.lo_point.x + mainFrame.scrollx, brSklad.lo_point.y + mainFrame.scrolly,
                        brSklad.ru_point.x - brSklad.lo_point.x + mainFrame.scrollx,
                        brSklad.ru_point.y - brSklad.lo_point.y + mainFrame.scrolly);
                g.drawImage(LScreen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, observer);
                g.setClip(tep);
                break;
            default:
                System.out.println("Falsches Menu-Item!!!");
        }
        if (olditem != 0) {
            olditem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuitem) {
            case 0:
                break;
            case 1:
                g.drawImage(Pfeil, 121 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                break;
            case 2:
                GenericRectangle tepm = g.getClipBounds();
                g.setClip(brSklad.lo_point.x + mainFrame.scrollx, brSklad.lo_point.y + mainFrame.scrolly,
                        brSklad.ru_point.x - brSklad.lo_point.x + mainFrame.scrollx,
                        brSklad.ru_point.y - brSklad.lo_point.y + mainFrame.scrolly);
                g.drawImage(Sklad, brSklad.lo_point.x + mainFrame.scrollx, brSklad.lo_point.y + mainFrame.scrolly, null);
                g.setClip(tepm);
                break;
            default:
                System.out.println("Falsches Menu-Item!!!");
        }
        if (menuitem != 0) {
            olditem = menuitem;
        }

    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();

        if (e.isLeftClick()) {
            // linke Maustaste
            // bei Click Ausserhalb zurueck ins Spiel
            if (!brGesamt.IsPointInRect(pTemp)) {
                Deactivate();
                mainFrame.whatScreen = 0;
                return;
            }

            // bei Pfeil links verlassen
            if (brPfeil.IsPointInRect(pTemp)) {
                Deactivate();
                return;
            }

            // Bei Speichern und erlaubt speichern
            if ((brSklad.IsPointInRect(pTemp)) && (selected != -1)) {
                if (Dir[selected + 1].Location != 0) {
                    // Sicherheitsabfrage aktivieren
                    mainFrame.exit.Activate(3);
                    return;
                }
                Aktuell.Save(selected + 1);
                Deactivate();
                return;
            }

            // bei Klick auf Spielstand aktuellen Spielstand darueberzeichnen
            for (int i = 0; i <= 5; ++i) {
                if (GetCurrentRect(i).IsPointInRect(pTemp)) {
                    if (selected != i) {
                        selected = i;
                    }
                    if (mainFrame.dClick) {

                        // Bei Doppelklick sofort speichern
                        if (Dir[selected + 1].Location != 0) {
                            // Sicherheitsabfrage aktivieren
                            mainFrame.exit.Activate(3);
                            return;
                        }
                        Aktuell.Save(selected + 1);
                        Deactivate();
                        return;
                    }
                    mainFrame.repaint();
                    return;
                }
            }
        } else {
            // rechte Maustaste
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // roten Rahmen zum Umranden festlegen
        nFeldAktiv = -1;
        for (int i = 0; i < 6; i++) {
            if (GetCurrentRect(i).IsPointInRect(pTemp)) {
                nFeldAktiv = i;
            }
        }

        // Menueitems fuer Highlight festlegen
        menuitem = 0;
        if (brPfeil.IsPointInRect(pTemp)) {
            menuitem = 1;
        }
        if ((brSklad.IsPointInRect(pTemp)) && (selected != -1)) {
            menuitem = 2;
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
            mainFrame.setCursor(mainFrame.Normal);
            return;
        }
        if ((menuitem != olditem) || (oFeldAktiv != nFeldAktiv)) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
        menuitem = 0;
        nFeldAktiv = -1;
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
        menuitem = 0;
        nFeldAktiv = -1;
        mainFrame.Clipset = false;
        mainFrame.DestructLocation(103);
        if (mainFrame.mainmenu.MMactive) {
            mainFrame.whatScreen = 2;
        } else {
            mainFrame.whatScreen = 0;
        }
        mainFrame.repaint();
    }

    // Berechnungsroutine Spielstandsfensternummer - X/Y-Koordinaten//////////////
    private Borderrect GetCurrentRect(int Number) {
        GenericPoint Pleftup = new GenericPoint(GetCurrentXY(Number));
        return (new Borderrect(Pleftup.x, Pleftup.y, Pleftup.x + 120, Pleftup.y + 90));
    }

    private GenericPoint GetCurrentXY(int Number) {
        GenericPoint Pleftup = new GenericPoint();
        Pleftup.x = 117 + ((Number % 3) * 142);
        Pleftup.y = 89 + ((Number / 3) * 112);
        return (Pleftup);
    }

    private void GetActualSpielstand() {
        // Auslesen des Datums vorbereiten
        GregorianCalendar Kal = new GregorianCalendar();
        Kal.setTimeZone(TimeZone.getTimeZone("ECT"));

        // erzeugt das Icon fuer den im Spiel aktuellen Spielstand
        int[] tempp = new int[10593];
        GenericToolkit.getDefaultToolkit().grabPixelsFromImage(actualImage, 0, 0, 118, 88, tempp, 0, 118);

        // erzeugt den aktuellen Spielstand (nicht komplett!!)
        Aktuell = new Spielstand(mainFrame, tempp, Kal.get(Calendar.DAY_OF_MONTH), (Kal.get(Calendar.MONTH) + 1),
                Kal.get(Calendar.YEAR));
    }
}