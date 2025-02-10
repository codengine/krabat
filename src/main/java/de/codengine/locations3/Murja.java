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

package de.codengine.locations3;

import de.codengine.Start;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Murja extends Mainloc {
    private GenericImage backl, backr, himmel, laterne, openDoor, closedDoor;
    private GenericImage busch;
    private boolean setScroll = false;
    private int scrollwert;

    private boolean setAnim = false;
    private boolean isDoorOpen = false;

    private boolean isBuschVisible = false;

    // Konstanten - Rects
    private static final Borderrect ausgangHaska
            = new Borderrect(1240, 355, 1279, 460);
    private static final Borderrect ausgangTerassa
            = new Borderrect(0, 380, 40, 479);

    // Konstante Points
    private static final GenericPoint pExitHaska = new GenericPoint(1260, 435);
    private static final GenericPoint pExitTerassa = new GenericPoint(10, 473);

    // Konstante ints
    private static final int scrollLaterne = 7;
    private static final int scrollTurm = 7;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Murja(Start caller, int oldLocation) {
        super(caller, 126);
        mainFrame.Freeze(true);

        // Schmied raushauen, wenn Hammer genommen
        if (mainFrame.Actions[953]) {
            mainFrame.Actions[701] = true;
        }

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 470;
        mainFrame.krabat.zoomf = 1.13f;
        mainFrame.krabat.defScale = 40;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(10, 473, 1259, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(1260, 1270, 1260, 1270, 435, 479));

        mainFrame.wegSucher.ClearMatrix(2);

        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 121: // von Haska aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(1247, 475));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
            case 127: // von Terassa aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(40, 475));
                mainFrame.krabat.SetFacing(3);
                scrollwert = 0;
                setScroll = true;
                break;
            case 129: // von Mlynkmurja aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(310, 474));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 0;
                setScroll = true;
                break;
            case 144: // von Couch aus (Gag)
                mainFrame.krabat.SetKrabatPos(new GenericPoint(80, 460));
                mainFrame.krabat.SetFacing(3);
                isBuschVisible = true;
                scrollwert = 0;
                setScroll = true;
                setAnim = true;
                isDoorOpen = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx-dd/murja/murja-l.gif");
        backr = getPicture("gfx-dd/murja/murja-r.gif");
        himmel = getPicture("gfx-dd/murja/mur-sky.gif");
        laterne = getPicture("gfx-dd/murja/laterna.gif");
        openDoor = getPicture("gfx-dd/murja/mdurje2.gif");
        closedDoor = getPicture("gfx-dd/murja/mdurje.gif");
        busch = getPicture("gfx-dd/murja/mbusch.gif");

        loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollx = scrollwert;
            }
            Cursorform = 200;
            if (setAnim) {
                mainFrame.fPlayAnim = true;
            }
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen  ?????????????????????
        g.drawImage(himmel, 65 + (mainFrame.scrollx / scrollTurm), 0, null);
        g.drawImage(backl, 0, 0, null);
        g.drawImage(backr, 640, 0, null);

        // Parallax - Scrolling ausfuehren  ??????????????????????
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 500, 105);
            g.drawImage(himmel, 65 + (mainFrame.scrollx / scrollTurm), 0, null);
            g.drawImage(backl, 0, 0, null);
            g.drawImage(backr, 640, 0, null);
        }

        // Tuer offen oder geschlossen zeichnen
        g.setClip(103, 184, 26, 56);
        g.drawImage(((isDoorOpen) ? openDoor : closedDoor), 103, 184, null);

        // Parallaxer fuer Laterne, muss immer Hintergrund loeschen ?????
        float xtf = mainFrame.scrollx;
        xtf = 900 - (((mainFrame.scrollx - 110) * 4) / scrollLaterne);
        int laterneAdd = (int) xtf;
        g.setClip(laterneAdd - 10, 0, 190 + 20, 479);
        g.drawImage(backl, 0, 0, null);
        g.drawImage(backr, 640, 0, null);

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        mainFrame.wegGeher.GeheWeg();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }
        } else {
            if ((mainFrame.talkCount > 0) && (TalkPerson != 0)) {
                // beim Reden
                switch (TalkPerson) {
                    case 1:
                        // Krabat spricht gestikulierend
                        mainFrame.krabat.talkKrabat(g);
                        break;
                    case 3:
                        // Krabat spricht im Monolog
                        mainFrame.krabat.describeKrabat(g);
                        break;
                    default:
                        // steht Krabat nur da
                        mainFrame.krabat.drawKrabat(g);
                        break;
                }
            }
            // Rumstehen oder Laufen
            else {
                mainFrame.krabat.drawKrabat(g);
            }
        }

        // Laterne zeichnen, wenn im Bild (Clipping nur temporaer !!!)
        if (mainFrame.scrollx > 110) // Diesen Wert bitte exakt !!
        {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(laterneAdd - 10, 0, 200, 479);
            g.drawImage(laterne, laterneAdd, 0, null);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
	/*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
	  {
	  g.drawImage (weiden2, 84, 221, null);
	  }*/

        if (isBuschVisible) {
            g.drawImage(busch, 51, 393, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 600;
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollx;
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // linker Maustaste
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.invCursor = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Haska gehen ?
                if (ausgangHaska.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangHaska.IsPointInRect(kt)) {
                        pTemp = pExitHaska;
                    } else {
                        pTemp = new GenericPoint(pExitHaska.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Terassa gehen
                if (ausgangTerassa.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangTerassa.IsPointInRect(kt)) {
                        pTemp = pExitTerassa;
                    } else {
                        pTemp = new GenericPoint(pExitTerassa.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((ausgangHaska.IsPointInRect(pTemp)) ||
                        (ausgangTerassa.IsPointInRect(pTemp))) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // neuen Punkt erzeugen wg. Scrolling
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if ((mainFrame.fPlayAnim) || (mainFrame.krabat.nAnimation != 0)) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp);

            if ((Cursorform != 10) && (!mainFrame.invHighCursor)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            // if ((zweiteTuer.IsPointInRect (pTemp) == true) ||
            //   (rechterSpiegel.IsPointInRect (pTemp) == true))
            // {
            //   if (Cursorform != 1)
            //   {
            //          mainFrame.setCursor (mainFrame.Kreuz);
            //          Cursorform = 1;
            //   }
            //   return;
            // }

            if (ausgangHaska.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangTerassa.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 9;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim) {
            return;
        }

        // Bei Krabat - Animation keine Keys
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();

        // Hauptmenue aktivieren
        if (Taste == GenericKeyEvent.VK_F1) {
            Keyclear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            Keyclear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            Keyclear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void Keyclear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
        mainFrame.krabat.StopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering) ||
                (mainFrame.krabat.isWalking)) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)
        if ((nextActionID > 499) && (nextActionID < 600)) {
            setKrabatAusrede();
            // manche Ausreden erfordern neuen Cursor !!!
            evalMouseMoveEvent(mainFrame.Mousepoint);
            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if ((nextActionID > 119) && (nextActionID < 129)) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 100:
                // Gehe zu Haska
                NeuesBild(121, locationID);
                break;

            case 101:
                // Gehe zu Terassa
                NeuesBild(127, locationID);
                break;

            case 600:
                // hinter Busch hervorkommen
                mainFrame.wegGeher.SetzeNeuenWeg(new GenericPoint(140, 477));
                nextActionID = 605;
                break;

            case 605:
                // Anim runtergefallt
                isBuschVisible = false;
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Murja_00000"),
                        Start.stringManager.getTranslation("Loc3_Murja_00001"),
                        Start.stringManager.getTranslation("Loc3_Murja_00002"),
                        6, 3, 2, 610);
                break;

            case 610:
                // nun sagen, dass man sich ja umziehen moechte
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Murja_00003"),
                        Start.stringManager.getTranslation("Loc3_Murja_00004"),
                        Start.stringManager.getTranslation("Loc3_Murja_00005"),
                        0, 3, 2, 620);
                break;

            case 620:
                // aus dem Bild laufen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(new GenericPoint(-80, 478));
                nextActionID = 630;
                break;

            case 630:
                // umziehen
                mainFrame.Actions[511] = false;
                mainFrame.Actions[850] = false;
                mainFrame.CheckKrabat();
                mainFrame.inventory.vInventory.addElement(Integer.valueOf(41));
                mainFrame.inventory.vInventory.removeElement(Integer.valueOf(53));
                nextActionID = 640;
                break;

            case 640:
                // wieder herkommen
                mainFrame.wegGeher.SetzeNeuenWeg(new GenericPoint(119, 474));
                nextActionID = 650;
                break;

            case 650:
                // Ende Anim
                mainFrame.Clipset = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}