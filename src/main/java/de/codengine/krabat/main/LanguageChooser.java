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
import de.codengine.krabat.platform.GenericImageObserver;

public class LanguageChooser extends Mainloc {
    private GenericImage background, pfeiloben, pfeilunten, dpfeiloben, dpfeilunten;

    private static final GenericPoint pPfeilOben = new GenericPoint(210, 80);  // allgemeine Pfeildeklarationen
    private static final GenericPoint pPfeilUnten = new GenericPoint(210, 277);

    private static final GenericPoint pOkUnten = new GenericPoint(400, 300);

    private static final int BREITE = 35; // dto.
    private static final int HOEHE = 53;

    private final Borderrect brPfeilOben;
    private final Borderrect brPfeilUnten;
    private final Borderrect brOk;

    private int Cursorform = 200;

    private int Index;

    private final Borderrect brGesamt;   // Gesamtrect fuer Auswahlleiste

    private int menuitem = 0;
    private int olditem = 0;

    private int currLang = 0;
    private int oldLang = 0;

    private boolean Paintcall = false;

    private static final int X_LEFT = 30;  // allgemeine Deklarationen fuer Woerterbuchanfang
    private static final int Y_UP = 80;

    private String[] languages;

    private String[] abbreviations;

    // Initialisierung ////////////////////////////////////////////////////////

    private final GenericImageObserver observer = null;

    private final GameProperties properties;

    // Instanz von dieser Location erzeugen
    public LanguageChooser(Start caller, GameProperties properties) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.NoPaint(true);

        this.properties = properties;

        brPfeilOben = new Borderrect(pPfeilOben.x, pPfeilOben.y, pPfeilOben.x + BREITE, pPfeilOben.y + HOEHE);
        brPfeilUnten = new Borderrect(pPfeilUnten.x, pPfeilUnten.y, pPfeilUnten.x + BREITE, pPfeilUnten.y + HOEHE);

        brOk = new Borderrect(pOkUnten.x, pOkUnten.y, pOkUnten.x + 40, pOkUnten.y + 20);

        brGesamt = new Borderrect(X_LEFT, Y_UP, pPfeilOben.x - 10, Y_UP + 10 * 25);

        InitImages();

        initLanguages();
        Index = 0;

        mainFrame.Clipset = false;

        System.out.println("LanguageChooser constructor called!");

        mainFrame.Freeze(false);
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mainmenu/background2.gif");
        pfeiloben = getPicture("gfx/mainmenu/pfeil-hoch.gif");
        dpfeiloben = getPicture("gfx/mainmenu/pfeil-hoch-leer.gif");
        pfeilunten = getPicture("gfx/mainmenu/pfeil-runter.gif");
        dpfeilunten = getPicture("gfx/mainmenu/pfeil-runter-leer.gif");
    }

    private void initLanguages() {
        int tmp = LanguageSupportMapper.getNumberSupportedLanguages();

        languages = new String[tmp];
        abbreviations = new String[tmp];

        for (int i = 0; i < tmp; i++) {
            languages[i] = LanguageSupportMapper.getLanguageName(i);
            abbreviations[i] = LanguageSupportMapper.getLanguageAbbreviation(i);
        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        System.out.println("LanguageChooser repaint with clipset=" + mainFrame.Clipset);

        // Credits-Background zeichnen
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            g.setClip(0, 0, 1280, 480);
            Cursorform = 200;
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);

            // alles loeschen und neuzeichnen - hier die texte, die sich nur bei "Clipset = false" aendern (Mouseclick)
            g.drawImage(background, mainFrame.scrollx, 0, null);
            GenericPoint ps = mainFrame.ifont.CenterAnimText("Select language", new GenericPoint(320, 35));
            mainFrame.ifont.drawString(g, "Select language", ps.x, ps.y, 0xffff0000);

            for (int i = Math.max(Index, 0); i < Math.min(Index + 10, languages.length); i++) {
                mainFrame.ifont.drawString(g, languages[i], X_LEFT + mainFrame.scrollx, mainFrame.scrolly + Y_UP + (i - Index) * 25, 0xff800000);
            }

            // Pfeile dazu-sind ja sonst geloescht !
            g.drawImage(dpfeiloben, pPfeilOben.x + mainFrame.scrollx, pPfeilOben.y + mainFrame.scrolly, observer);
            g.drawImage(dpfeilunten, pPfeilUnten.x + mainFrame.scrollx, pPfeilUnten.y + mainFrame.scrolly, observer);

            mainFrame.ifont.drawString(g, "OK", pOkUnten.x + mainFrame.scrollx, mainFrame.scrolly + pOkUnten.y, 0xff800000);
        }

        if (oldLang > 0) {
            if (oldLang <= languages.length) {
                mainFrame.ifont.drawString(g, languages[oldLang - 1], X_LEFT + mainFrame.scrollx, mainFrame.scrolly + Y_UP + (oldLang - Index - 1) * 25, 0xff800000);
            } else {
                System.out.println("Wrong language to deselect!");
            }
        }
        if (oldLang != 0) {
            oldLang = 0;
        }

        if (currLang > 0) {
            if (currLang <= languages.length) {
                mainFrame.ifont.drawString(g, languages[currLang - 1], X_LEFT + mainFrame.scrollx, mainFrame.scrolly + Y_UP + (currLang - Index - 1) * 25, 0xffff0000);
            } else {
                System.out.println("Wrong language to select!");
            }
        }
        if (currLang != 0) {
            oldLang = currLang;
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(dpfeiloben, pPfeilOben.x + mainFrame.scrollx, pPfeilOben.y + mainFrame.scrolly, observer);
                break;
            case 2:
                g.drawImage(dpfeilunten, pPfeilUnten.x + mainFrame.scrollx, pPfeilUnten.y + mainFrame.scrolly, observer);
                break;
            case 3:
                mainFrame.ifont.drawString(g, "OK", pOkUnten.x + mainFrame.scrollx, mainFrame.scrolly + pOkUnten.y, 0xff800000);
                break;
            default:
                System.out.println("Falsches Menu-Item zum abdunkeln!!!");
        }

        if (olditem != 0) {
            olditem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuitem) {
            case 0:
                break;
            case 1:
                g.drawImage(pfeiloben, pPfeilOben.x + mainFrame.scrollx, pPfeilOben.y + mainFrame.scrolly, observer);
                break;
            case 2:
                g.drawImage(pfeilunten, pPfeilUnten.x + mainFrame.scrollx, pPfeilUnten.y + mainFrame.scrolly, observer);
                break;
            case 3:
                mainFrame.ifont.drawString(g, "OK", pOkUnten.x + mainFrame.scrollx, mainFrame.scrolly + pOkUnten.y, 0xffff0000);
                break;
            default:
                System.out.println("Falsches Menu-Item!!!");
        }

        if (menuitem != 0) {
            olditem = menuitem;
        }
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();

        if (e.isRightClick()) {
            // linke Maustaste
            // Pfeil-Oben gedrueckt
            if (brPfeilOben.IsPointInRect(pTemp)) {
                Index -= 10;
                mainFrame.Clipset = false;
                if (Index < 0) {
                    Index = 0;
                }
                mainFrame.repaint();
            }

            // Pfeil-Unten gedrueckt
            if (brPfeilUnten.IsPointInRect(pTemp)) {
                Index += 10;
                mainFrame.Clipset = false;
                if (Index > (languages.length - 10)) {
                    Index = languages.length - 10;
                }
                mainFrame.repaint();
            }

            if ((brOk.IsPointInRect(pTemp)) && (currLang != 0)) {
                System.out.println("Selected language = " + currLang);
                if (abbreviations[currLang - 1].equalsIgnoreCase("hs")
                        || abbreviations[currLang - 1].equalsIgnoreCase("ds")
                        || abbreviations[currLang - 1].equalsIgnoreCase("de")) {
                    mainFrame.thirdGameLanguage = "de";
                } else {
                    mainFrame.thirdGameLanguage = abbreviations[currLang - 1];
                }
                if (abbreviations[currLang - 1].equalsIgnoreCase("hs")) {
                    mainFrame.sprache = 1;
                } else if (abbreviations[currLang - 1].equalsIgnoreCase("ds")) {
                    mainFrame.sprache = 2;
                } else {
                    mainFrame.sprache = 3;
                }
                Start.stringManager.defineThirdLanguage(
                        LanguageSupportMapper.getLanguageFilename(abbreviations[currLang - 1]),
                        LanguageSupportMapper.getFakeLanguage(abbreviations[currLang - 1]),
                        abbreviations[currLang - 1]);
                properties.setProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, Integer.toString(mainFrame.sprache));
                properties.setProperty(GameProperties.THIRD_GAME_LANGUAGE_SELECTION, mainFrame.thirdGameLanguage);
                NeuesBild(100, 109);
                mainFrame.repaint();
            }

            // Sprache wurde ausgewaehlt
            if (brGesamt.IsPointInRect(pTemp)) {
                for (int i = 0; i < languages.length; i++) {

                    // System.out.println("Curr Y: " + pTemp.y);
                    // System.out.println("Min y: " + (Y_UP + (i - Index) * 25));
                    // System.out.println("Max y: " + ((Y_UP + (i - Index) * 25) + 24));

                    if ((pTemp.y >= (Y_UP + (i - Index) * 25)) && (pTemp.y < ((Y_UP + (i - Index) * 25) + 24))) {
                        currLang = i + 1;
                        mainFrame.repaint();
                        System.out.println("Selected lang=" + abbreviations[i]);
                        break;
                    }
                }
            }
        } else {
            // rechte Maustaste, ignorieren
        }
    }

    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (Cursorform != 0) {
            Cursorform = 0;
            mainFrame.setCursor(mainFrame.Normal);
        }

        // Highlight im Menue festlegen
        menuitem = 0;
        if (brPfeilOben.IsPointInRect(pTemp)) {
            menuitem = 1;
        }
        if (brPfeilUnten.IsPointInRect(pTemp)) {
            menuitem = 2;
        }
        if ((brOk.IsPointInRect(pTemp)) && (currLang != 0)) {
            menuitem = 3;
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

    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
        menuitem = 0;
        mainFrame.repaint();
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // currently no keys
    }
}
