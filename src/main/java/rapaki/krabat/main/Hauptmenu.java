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

package rapaki.krabat.main;

import rapaki.krabat.Start;
import rapaki.krabat.anims.Mainanim;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.platform.GenericImageObserver;

public class Hauptmenu extends Mainanim {
    private GenericImage Screen, Nowostart, Wocinic, Skladzic,
            Info, Dalehrac, Konc, Rec;
    private Borderrect brGesamt, brNowostart, brWocinic, brSkladzic, brInfo,
            brDalehrac, brKonc, brRec;

    private GenericPoint pLO;

    private int menuitem = 0;
    private int olditem = 0;

    private boolean Paintcall = false;
    public boolean MMactive;
    public boolean introcall = false;

    private GenericImageObserver observer = null;

    private final GameProperties gameProperties;

    private static final String[] helpTextRight = new String[]{
            "Main_Hauptmenu_00001",
            "Main_Hauptmenu_00002",
            "Main_Hauptmenu_00003",
            "Main_Hauptmenu_00004",
            "Main_Hauptmenu_00005"
    };

    private static final GenericPoint[] helpTextPosRight = new GenericPoint[]{
            new GenericPoint(486, 98),
            new GenericPoint(486, 123),
            new GenericPoint(486, 220),
            new GenericPoint(486, 248),
            new GenericPoint(486, 338)
    };

    private static final String[] helpTextLeft = new String[]{
            "Main_Hauptmenu_00006",
            "Main_Hauptmenu_00007"
    };

    private static final GenericPoint[] helpTextPosLeft = new GenericPoint[]{
            new GenericPoint(91, 268),
            new GenericPoint(91, 291)
    };

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Hauptmenu(Start caller, GameProperties gameProperties) {
        super(caller);
        this.gameProperties = gameProperties;

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);

        // Sprachenspezifische Initialisierung der Images und Rechtecke
        InitRec();

    }

    // hier der Init je nach Sprache
    public void InitRec() {
        switch (mainFrame.sprache) {
            case 2: // Delnjos
                brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                        pLO.x + 513, pLO.y + 380);
                brNowostart = new Borderrect(pLO.x + 287, pLO.y + 275,
                        pLO.x + 485, pLO.y + 297);
                brWocinic = new Borderrect(pLO.x + 352, pLO.y + 147,
                        pLO.x + 485, pLO.y + 175);
                brSkladzic = new Borderrect(pLO.x + 302, pLO.y + 186,
                        pLO.x + 485, pLO.y + 214);
                brInfo = new Borderrect(pLO.x + 89, pLO.y + 314,
                        pLO.x + 159, pLO.y + 336);
                brDalehrac = new Borderrect(pLO.x + 297, pLO.y + 65,
                        pLO.x + 486, pLO.y + 93);
                brKonc = new Borderrect(pLO.x + 291, pLO.y + 308,
                        pLO.x + 485, pLO.y + 336);
                brRec = new Borderrect(pLO.x + 89, pLO.y + 221,
                        pLO.x + 227, pLO.y + 267);
                // Bilder rein
                Screen = getPicture("gfx/mainmenu/d-screen.gif");
                Nowostart = getPicture("gfx/mainmenu/m-nowo.gif");
                Wocinic = getPicture("gfx/mainmenu/d-woci.gif");
                Skladzic = getPicture("gfx/mainmenu/d-sklad.gif");
                Info = getPicture("gfx/mainmenu/m-info.gif");
                Dalehrac = getPicture("gfx/mainmenu/d-dale.gif");
                Konc = getPicture("gfx/mainmenu/d-konc.gif");
                Rec = getPicture("gfx/mainmenu/d-hs.gif");
                break;

            default: // Hornjos
                // hier temporaer die deutsche Version -> bekommt Hornjos
                // alle Rects definieren
                brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                        pLO.x + 513, pLO.y + 380);
                brNowostart = new Borderrect(pLO.x + 287, pLO.y + 275,
                        pLO.x + 485, pLO.y + 297);
                brWocinic = new Borderrect(pLO.x + 365, pLO.y + 147,
                        pLO.x + 485, pLO.y + 175);
                brSkladzic = new Borderrect(pLO.x + 306, pLO.y + 186,
                        pLO.x + 485, pLO.y + 214);
                brInfo = new Borderrect(pLO.x + 89, pLO.y + 314,
                        pLO.x + 159, pLO.y + 336);
                brDalehrac = new Borderrect(pLO.x + 320, pLO.y + 65,
                        pLO.x + 485, pLO.y + 93);
                brKonc = new Borderrect(pLO.x + 334, pLO.y + 308,
                        pLO.x + 485, pLO.y + 336);
                brRec = new Borderrect(pLO.x + 89, pLO.y + 222,
                        pLO.x + 220, pLO.y + 267);
                // Bilder rein
                Screen = getPicture("gfx/mainmenu/m-screen.gif");
                Nowostart = getPicture("gfx/mainmenu/m-nowo.gif");
                Wocinic = getPicture("gfx/mainmenu/m-woci.gif");
                Skladzic = getPicture("gfx/mainmenu/m-sklad.gif");
                Info = getPicture("gfx/mainmenu/m-info.gif");
                Dalehrac = getPicture("gfx/mainmenu/m-dale.gif");
                Konc = getPicture("gfx/mainmenu/m-konc.gif");
                Rec = getPicture("gfx/mainmenu/m-hs.gif");
                break;

        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintMainmenu(GenericDrawingContext g) {
        // Mainmenu-Background zeichnen
        if (mainFrame.Clipset == false) {
            mainFrame.Clipset = true;
            g.setClip(0, 0, 1284, 964);
            g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
            g.setClip(90 + mainFrame.scrollx, 70 + mainFrame.scrolly, 550, 390);
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);

            // übersetungen als Hilfe in 3. sprache mit reinschreiben


            if (mainFrame.sprache == 3) {
                String tmp;
                for (int i = 0; i < helpTextRight.length; i++) {
                    tmp = Start.stringManager.getTranslation(helpTextRight[i]);
                    mainFrame.ifont.drawString(g,
                            tmp,
                            pLO.x + helpTextPosRight[i].x + mainFrame.scrollx - mainFrame.ifont.LineLength(tmp),
                            pLO.y + helpTextPosRight[i].y + mainFrame.scrolly,
                            0xffff0000);
                }
            }

            if (mainFrame.sprache == 3) {
                for (int i = 0; i < helpTextLeft.length; i++) {
                    mainFrame.ifont.drawString(g,
                            Start.stringManager.getTranslation(helpTextLeft[i]),
                            pLO.x + helpTextPosLeft[i].x + mainFrame.scrollx,
                            pLO.y + helpTextPosLeft[i].y + mainFrame.scrolly,
                            0xffff0000);
                }
            }
        }

        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1: // Nowostart
                g.setClip(brNowostart.lo_point.x + mainFrame.scrollx, brNowostart.lo_point.y + mainFrame.scrolly,
                        brNowostart.ru_point.x - brNowostart.lo_point.x + mainFrame.scrollx,
                        brNowostart.ru_point.y - brNowostart.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
                break;
            case 2: // Wocinic
                g.setClip(brWocinic.lo_point.x + mainFrame.scrollx, brWocinic.lo_point.y + mainFrame.scrolly,
                        brWocinic.ru_point.x - brWocinic.lo_point.x + mainFrame.scrollx,
                        brWocinic.ru_point.y - brWocinic.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
                break;
            case 3: // Skladzic
                g.setClip(brSkladzic.lo_point.x + mainFrame.scrollx, brSkladzic.lo_point.y + mainFrame.scrolly,
                        brSkladzic.ru_point.x - brSkladzic.lo_point.x + mainFrame.scrollx,
                        brSkladzic.ru_point.y - brSkladzic.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
                break;
            case 4: // Rec
                g.setClip(brRec.lo_point.x + mainFrame.scrollx, brRec.lo_point.y + mainFrame.scrolly,
                        brRec.ru_point.x - brRec.lo_point.x + mainFrame.scrollx,
                        brRec.ru_point.y - brRec.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
                break;
            case 6: // Info
                g.setClip(brInfo.lo_point.x + mainFrame.scrollx, brInfo.lo_point.y + mainFrame.scrolly,
                        brInfo.ru_point.x - brInfo.lo_point.x + mainFrame.scrollx,
                        brInfo.ru_point.y - brInfo.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
                break;
            case 7: // Dalehrac
                g.setClip(brDalehrac.lo_point.x + mainFrame.scrollx, brDalehrac.lo_point.y + mainFrame.scrolly,
                        brDalehrac.ru_point.x - brDalehrac.lo_point.x + mainFrame.scrollx,
                        brDalehrac.ru_point.y - brDalehrac.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
                break;
            case 8: // Konc
                g.setClip(brKonc.lo_point.x + mainFrame.scrollx, brKonc.lo_point.y + mainFrame.scrolly,
                        brKonc.ru_point.x - brKonc.lo_point.x + mainFrame.scrollx,
                        brKonc.ru_point.y - brKonc.lo_point.y + mainFrame.scrolly);
                g.drawImage(Screen, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
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
            case 1: // Nowostart
                g.setClip(brNowostart.lo_point.x + mainFrame.scrollx, brNowostart.lo_point.y + mainFrame.scrolly,
                        brNowostart.ru_point.x - brNowostart.lo_point.x + mainFrame.scrollx,
                        brNowostart.ru_point.y - brNowostart.lo_point.y + mainFrame.scrolly);
                g.drawImage(Nowostart, brNowostart.lo_point.x + mainFrame.scrollx, brNowostart.lo_point.y + mainFrame.scrolly, observer);
                break;
            case 2: // Wocinic
                g.setClip(brWocinic.lo_point.x + mainFrame.scrollx, brWocinic.lo_point.y + mainFrame.scrolly,
                        brWocinic.ru_point.x - brWocinic.lo_point.x + mainFrame.scrollx,
                        brWocinic.ru_point.y - brWocinic.lo_point.y + mainFrame.scrolly);
                g.drawImage(Wocinic, brWocinic.lo_point.x + mainFrame.scrollx, brWocinic.lo_point.y + mainFrame.scrolly, observer);
                break;
            case 3: // Skladzic
                g.setClip(brSkladzic.lo_point.x + mainFrame.scrollx, brSkladzic.lo_point.y + mainFrame.scrolly,
                        brSkladzic.ru_point.x - brSkladzic.lo_point.x + mainFrame.scrollx,
                        brSkladzic.ru_point.y - brSkladzic.lo_point.y + mainFrame.scrolly);
                g.drawImage(Skladzic, brSkladzic.lo_point.x + mainFrame.scrollx, brSkladzic.lo_point.y + mainFrame.scrolly, observer);
                break;
            case 4: // Rec
                g.setClip(brRec.lo_point.x + mainFrame.scrollx, brRec.lo_point.y + mainFrame.scrolly,
                        brRec.ru_point.x - brRec.lo_point.x + mainFrame.scrollx,
                        brRec.ru_point.y - brRec.lo_point.y + mainFrame.scrolly);
                g.drawImage(Rec, brRec.lo_point.x + mainFrame.scrollx, brRec.lo_point.y + mainFrame.scrolly, observer);
                break;
            case 6: // Info
                g.setClip(brInfo.lo_point.x + mainFrame.scrollx, brInfo.lo_point.y + mainFrame.scrolly,
                        brInfo.ru_point.x - brInfo.lo_point.x + mainFrame.scrollx,
                        brInfo.ru_point.y - brInfo.lo_point.y + mainFrame.scrolly);
                g.drawImage(Info, brInfo.lo_point.x + mainFrame.scrollx, brInfo.lo_point.y + mainFrame.scrolly, observer);
                break;
            case 7: // Dalehrac
                g.setClip(brDalehrac.lo_point.x + mainFrame.scrollx, brDalehrac.lo_point.y + mainFrame.scrolly,
                        brDalehrac.ru_point.x - brDalehrac.lo_point.x + mainFrame.scrollx,
                        brDalehrac.ru_point.y - brDalehrac.lo_point.y + mainFrame.scrolly);
                g.drawImage(Dalehrac, brDalehrac.lo_point.x + mainFrame.scrollx, brDalehrac.lo_point.y + mainFrame.scrolly, observer);
                break;
            case 8:
                g.setClip(brKonc.lo_point.x + mainFrame.scrollx, brKonc.lo_point.y + mainFrame.scrolly,
                        brKonc.ru_point.x - brKonc.lo_point.x + mainFrame.scrollx,
                        brKonc.ru_point.y - brKonc.lo_point.y + mainFrame.scrolly);
                g.drawImage(Konc, brKonc.lo_point.x + mainFrame.scrollx, brKonc.lo_point.y + mainFrame.scrolly, observer);
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

        if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
            // linke Maustaste

            // bei Click Ausserhalb zurueck ins Spiel
            if (brGesamt.IsPointInRect(pTemp) == false) {
                Deactivate();
                mainFrame.whatScreen = 0;
                mainFrame.repaint();
                return;
            }

            // Dalehrac
            if (brDalehrac.IsPointInRect(pTemp) == true) {
                Deactivate();
                mainFrame.repaint();
            }

            // Konc hry
            if (brKonc.IsPointInRect(pTemp) == true) {
                mainFrame.exit.Activate(1);
                return;
            }

            // Wocinic
            if (brWocinic.IsPointInRect(pTemp) == true) {
                if (mainFrame.storageManager.isLoadSaveSupported() == false) {
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
            if (brSkladzic.IsPointInRect(pTemp) == true) {
                // vom Intro aus darf nicht gespeichert werden
                if (introcall == true) {
                    return;
                }
                if (mainFrame.storageManager.isLoadSaveSupported() == false) {
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
            if (brInfo.IsPointInRect(pTemp) == true) {
                Deactivate();
                mainFrame.ConstructLocation(104);
                mainFrame.whatScreen = 5;
                MMactive = true;
                mainFrame.repaint();
                return;
            }

            // Hornjoserbsce - Delnoserbsce
            if (brRec.IsPointInRect(pTemp) == true) {
                mainFrame.sprache++;
                // erlaube umschalten auf deutsch
                if (mainFrame.sprache == 4) {
                    mainFrame.sprache = 1;
                }
                InitRec();

                switch (mainFrame.sprache) {
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

                mainFrame.Clipset = false;
                mainFrame.repaint();
            }

            // Nowostart
            if (brNowostart.IsPointInRect(pTemp) == true) {
                mainFrame.exit.Activate(2);
                return;
            }
        } else {
            // rechte Maustaste
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {

        // Highlight im Menue festlegen
        menuitem = 0;
        if (brNowostart.IsPointInRect(pTemp) == true) {
            menuitem = 1;
        }
        if ((brWocinic.IsPointInRect(pTemp) == true) &&
                (mainFrame.storageManager.isLoadSaveSupported() == true)) {
            menuitem = 2;
        }

        // Speichern nicht im Introscreen!!
        if ((brSkladzic.IsPointInRect(pTemp) == true) && (introcall == false) &&
                (mainFrame.storageManager.isLoadSaveSupported() == true)) {
            menuitem = 3;
        }
        if (brRec.IsPointInRect(pTemp) == true) {
            menuitem = 4;
        }
        // if (brNieders.IsPointInRect   (pTemp) == true) menuitem = 5;
        if (brInfo.IsPointInRect(pTemp) == true) {
            menuitem = 6;
        }
        if (brDalehrac.IsPointInRect(pTemp) == true) {
            menuitem = 7;
        }
        if (brKonc.IsPointInRect(pTemp) == true) {
            menuitem = 8;
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall == true) {
            Paintcall = false;
            mainFrame.setCursor(mainFrame.Normal);
            return;
        }
        if (menuitem != olditem) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
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
        mainFrame.Clipset = false;
        MMactive = false;
    }
}