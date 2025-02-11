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

public class Info extends Mainanim {
    private GenericImage Bild;
    private GenericImage Jan;
    private GenericImage Jiri;
    private GenericImage Daniel;
    private GenericImage Eddie;
    private GenericImage Hanus;
    private GenericImage Mic;
    private final GenericImage Pfeill;
    private final GenericImage DPfeill;
    private final GenericImage Pfeilr; /* DPfeilr, */
            private GenericImage Abdecken;

    private final GenericPoint pLO;

    private final Borderrect brGesamt;
    private final Borderrect brPfeill;
    private final Borderrect brPfeilr;

    private int menuitem = 0;
    private int olditem = 0;

    private boolean Paintcall = false;

    private int PictureCounter;

    private final GenericImageObserver observer = null;

    // Hier die Texte
    private static final String[][][] BildText =
            {
                    {
                            {Start.stringManager.getTranslation("Main_Info_00000"), Start.stringManager.getTranslation("Main_Info_00001"), Start.stringManager.getTranslation("Main_Info_00002"), ""},
                            {Start.stringManager.getTranslation("Main_Info_00003"), Start.stringManager.getTranslation("Main_Info_00004"), Start.stringManager.getTranslation("Main_Info_00005"), ""},
                            {Start.stringManager.getTranslation("Main_Info_00006"), Start.stringManager.getTranslation("Main_Info_00007"), Start.stringManager.getTranslation("Main_Info_00008"), ""}
                    },
                    {
                            {Start.stringManager.getTranslation("Main_Info_00009"), Start.stringManager.getTranslation("Main_Info_00010"), Start.stringManager.getTranslation("Main_Info_00011"), ""},
                            {Start.stringManager.getTranslation("Main_Info_00012"), Start.stringManager.getTranslation("Main_Info_00013"), Start.stringManager.getTranslation("Main_Info_00014"), ""},
                            {Start.stringManager.getTranslation("Main_Info_00015"), Start.stringManager.getTranslation("Main_Info_00016"), Start.stringManager.getTranslation("Main_Info_00017"), ""}
                    },
                    {
                            {Start.stringManager.getTranslation("Main_Info_00018"), Start.stringManager.getTranslation("Main_Info_00019"), "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00020"), Start.stringManager.getTranslation("Main_Info_00021"), "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00022"), Start.stringManager.getTranslation("Main_Info_00023"), "", ""}
                    },
                    {
                            {Start.stringManager.getTranslation("Main_Info_00024"), Start.stringManager.getTranslation("Main_Info_00025"), "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00026"), Start.stringManager.getTranslation("Main_Info_00027"), "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00028"), Start.stringManager.getTranslation("Main_Info_00029"), "", ""}
                    },
                    {
                            {Start.stringManager.getTranslation("Main_Info_00030"), Start.stringManager.getTranslation("Main_Info_00031"), Start.stringManager.getTranslation("Main_Info_00032"), Start.stringManager.getTranslation("Main_Info_00033")},
                            {Start.stringManager.getTranslation("Main_Info_00034"), Start.stringManager.getTranslation("Main_Info_00035"), Start.stringManager.getTranslation("Main_Info_00036"), Start.stringManager.getTranslation("Main_Info_00037")},
                            {Start.stringManager.getTranslation("Main_Info_00038"), Start.stringManager.getTranslation("Main_Info_00039"), Start.stringManager.getTranslation("Main_Info_00040"), Start.stringManager.getTranslation("Main_Info_00041")}
                    },
                    {
                            {Start.stringManager.getTranslation("Main_Info_00042"), Start.stringManager.getTranslation("Main_Info_00043"), "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00044"), Start.stringManager.getTranslation("Main_Info_00045"), "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00046"), Start.stringManager.getTranslation("Main_Info_00047"), "", ""}
                    },
                    {
                            {Start.stringManager.getTranslation("Main_Info_00048"), "", "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00049"), "", "", ""},
                            {Start.stringManager.getTranslation("Main_Info_00050"), "", "", ""}
                    },
            };

    private static final GenericPoint[][] BildPunkte =
            {
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
        mainFrame.Freeze(true);

        InitImages();

        PictureCounter = 0;  // Reset der Bilder

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);

        brPfeill = mainFrame.inventory.brPfeill;
        brPfeilr = new Borderrect(pLO.x + 391, pLO.y + 319,
                pLO.x + 481, pLO.y + 359);

        Pfeill = mainFrame.inventory.Pfeill;
        DPfeill = mainFrame.inventory.DPfeill;
        Pfeilr = mainFrame.inventory.Pfeilr;
        // DPfeilr = mainFrame.inventory.DPfeilr;

        mainFrame.Freeze(false);
        mainFrame.setCursor(mainFrame.Normal);
    }

    // Bilder vorbereiten
    public void InitImages() {
        Bild = getPicture("gfx/mainmenu/info-7.gif");
        Jan = getPicture("gfx/mainmenu/jan.gif");
        Jiri = getPicture("gfx/mainmenu/jiri.gif");
        Daniel = getPicture("gfx/mainmenu/daniel.gif");
        Eddie = getPicture("gfx/mainmenu/eddie.gif");
        Hanus = getPicture("gfx/mainmenu/hanus.gif");
        Mic = getPicture("gfx/mainmenu/mic.gif");

        Abdecken = getPicture("gfx/mainmenu/dpfeil.gif");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintCredits(GenericDrawingContext g) {

        // Credits-Background zeichnen
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            g.setClip(0, 0, 1280, 480);

            g.drawImage(Bild, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
            switch (PictureCounter) {
                case 0:  // Jan und Joerg
                    g.drawImage(Jan, 186 + pLO.x + mainFrame.scrollx, 58 + pLO.y + mainFrame.scrolly, null);
                    g.drawImage(Jiri, 310 + pLO.x + mainFrame.scrollx, 43 + pLO.y + mainFrame.scrolly, null);
                    break;
                case 1: // Daniel und Eddie
                    g.drawImage(Daniel, 186 + pLO.x + mainFrame.scrollx, 87 + pLO.y + mainFrame.scrolly, null);
                    g.drawImage(Eddie, 338 + pLO.x + mainFrame.scrollx, 40 + pLO.y + mainFrame.scrolly, null);
                    break;
                case 2: // Hanus
                    g.drawImage(Hanus, 314 + pLO.x + mainFrame.scrollx, 31 + pLO.y + mainFrame.scrolly, null);
                    break;
                case 3: // Jan
                    g.drawImage(Jan, 186 + pLO.x + mainFrame.scrollx, 58 + pLO.y + mainFrame.scrolly, null);
                    break;
                case 4: // Mic
                    g.drawImage(Mic, 174 + pLO.x + mainFrame.scrollx, 43 + pLO.y + mainFrame.scrolly, null);
                    break;
                case 5: // Jiri
                    g.drawImage(Jiri, 310 + pLO.x + mainFrame.scrollx, 43 + pLO.y + mainFrame.scrolly, null);
                    break;
            }

            if (PictureCounter == 6) {
                g.drawImage(Abdecken, pLO.x + mainFrame.scrollx + 380, pLO.y + mainFrame.scrolly + 307, null);
            }

            g.setClip(90 + mainFrame.scrollx, 70 + mainFrame.scrolly, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);

            for (int i = 0; i <= 3; i++) {
                String outputText = BildText[PictureCounter][mainFrame.sprache - 1][i];
                GenericPoint outputTextPos = BildPunkte[PictureCounter][i];
                if (outputText != "") // leere Dinger ueberspringen
                {
                    mainFrame.ifont.drawString(g, outputText, pLO.x + outputTextPos.x + mainFrame.scrollx,
                            pLO.y + outputTextPos.y + mainFrame.scrolly, 0xffff0000);
                }
            }
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(DPfeill, 119 + mainFrame.scrollx, 349 + mainFrame.scrolly, observer);
                break;
            case 2:
                if (PictureCounter != 6) {
                    GenericRectangle txx = g.getClipBounds();
                    g.setClip(422 + mainFrame.scrollx, 348 + mainFrame.scrolly, 100, 50);
                    // BUG!!!
                    //		    g.setClip (422, 348, 100, 50);
                    g.drawImage(Bild, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, observer);
                    g.setClip(txx);
                }
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
                g.drawImage(Pfeill, 121 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                break;
            case 2:
                g.drawImage(Pfeilr, 423 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                break;
            default:
                System.out.println("Falsches Menu-Item fuers Highlight!!!");
        }

        if (menuitem != 0) {
            olditem = menuitem;
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
        menuitem = 0;
        mainFrame.repaint();
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

            // bei Click auf Pfeil links zurueck ins vorherige Bild oder verlassen
            if (brPfeill.IsPointInRect(pTemp)) {
                PictureCounter--;
                if (PictureCounter < 0) {
                    Deactivate();
                    return;
                } else {
                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                }
            }

            // bei Click auf Pfeil rechts ein Bild weiter
            if (brPfeilr.IsPointInRect(pTemp)) {
                if (PictureCounter < 6) {
                    PictureCounter++;
                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                }
            }

        } else {
            // rechte Maustaste

        }

    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        menuitem = 0;
        if (brPfeill.IsPointInRect(pTemp)) {
            menuitem = 1;
        }

        // wenn nicht im letzten Bild, dann auch den rechten Pfeil zeichnen
        if (brPfeilr.IsPointInRect(pTemp) && PictureCounter < 6) {
            menuitem = 2;
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

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
        }
    }


    // Deaktivieren //////////
    private void Deactivate() {
        menuitem = 0;
        mainFrame.Clipset = false;
        mainFrame.DestructLocation(104);
        if (mainFrame.mainmenu.MMactive) {
            mainFrame.whatScreen = 2;
        } else {
            mainFrame.whatScreen = 0;
        }
        mainFrame.repaint();
    }
}
