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

package de.codengine.krabat.locations3;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Murja extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Murja.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage himmel;
    private GenericImage laterne;
    private GenericImage openDoor;
    private GenericImage closedDoor;
    private GenericImage busch;
    private boolean setScroll = false;
    private int scrollwert;

    private boolean setAnim = false;
    private boolean isDoorOpen = false;

    private boolean isBuschVisible = false;

    // Konstanten - Rects
    private static final BorderRect ausgangHaska
            = new BorderRect(1240, 355, 1279, 460);
    private static final BorderRect ausgangTerassa
            = new BorderRect(0, 380, 40, 479);

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
        mainFrame.freeze(true);

        // Schmied raushauen, wenn Hammer genommen
        if (mainFrame.actions[953]) {
            mainFrame.actions[701] = true;
        }

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 470;
        mainFrame.krabat.zoomf = 1.13f;
        mainFrame.krabat.defScale = 40;

        InitLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(10, 473, 1259, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(1260, 1270, 1260, 1270, 435, 479));

        mainFrame.pathFinder.ClearMatrix(2);

        mainFrame.pathFinder.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 121: // von Haska aus
                mainFrame.krabat.setPos(new GenericPoint(1247, 475));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
            case 127: // von Terassa aus
                mainFrame.krabat.setPos(new GenericPoint(40, 475));
                mainFrame.krabat.SetFacing(3);
                scrollwert = 0;
                setScroll = true;
                break;
            case 129: // von Mlynkmurja aus
                mainFrame.krabat.setPos(new GenericPoint(310, 474));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 0;
                setScroll = true;
                break;
            case 144: // von Couch aus (Gag)
                mainFrame.krabat.setPos(new GenericPoint(80, 460));
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
        backl = getPicture("gfx-dd/murja/murja-l.png");
        backr = getPicture("gfx-dd/murja/murja-r.png");
        himmel = getPicture("gfx-dd/murja/mur-sky.png");
        laterne = getPicture("gfx-dd/murja/laterna.png");
        openDoor = getPicture("gfx-dd/murja/mdurje2.png");
        closedDoor = getPicture("gfx-dd/murja/mdurje.png");
        busch = getPicture("gfx-dd/murja/mbusch.png");
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }
            Cursorform = 200;
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen  ?????????????????????
        g.drawImage(himmel, 65 + mainFrame.scrollX / scrollTurm, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Parallax - Scrolling ausfuehren  ??????????????????????
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollX - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 500, 105);
            g.drawImage(himmel, 65 + mainFrame.scrollX / scrollTurm, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // Tuer offen oder geschlossen zeichnen
        g.setClip(103, 184, 26, 56);
        g.drawImage(isDoorOpen ? openDoor : closedDoor, 103, 184);

        // Parallaxer fuer Laterne, muss immer Hintergrund loeschen ?????
        float xtf = 900 - (float) ((mainFrame.scrollX - 110) * 4) / scrollLaterne;
        int laterneAdd = (int) xtf;
        g.setClip(laterneAdd - 10, 0, 190 + 20, 479);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        mainFrame.pathWalker.GeheWeg();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.mousePoint);
            }
        } else {
            if (mainFrame.talkCount > 0 && TalkPerson != 0) {
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
        if (mainFrame.scrollX > 110) // Diesen Wert bitte exakt !!
        {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(laterneAdd - 10, 0, 200, 479);
            g.drawImage(laterne, laterneAdd, 0);
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
            g.drawImage(busch, 51, 393);
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
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
                mainFrame.isClipSet = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollX;
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.isAnimRunning) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // linker Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Haska gehen ?
                if (ausgangHaska.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangHaska.IsPointInRect(kt)) {
                        pTemp = pExitHaska;
                    } else {
                        pTemp = new GenericPoint(pExitHaska.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Terassa gehen
                if (ausgangTerassa.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangTerassa.IsPointInRect(kt)) {
                        pTemp = pExitTerassa;
                    } else {
                        pTemp = new GenericPoint(pExitTerassa.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangHaska.IsPointInRect(pTemp) ||
                        ausgangTerassa.IsPointInRect(pTemp)) {
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
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollX, pTxxx.y + mainFrame.scrollY);

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.isInventoryHighlightCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (Cursorform != 11 && mainFrame.isInventoryHighlightCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
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
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangTerassa.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                Cursorform = 0;
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.isInventoryCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.isAnimRunning) {
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
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.StopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();
            // manche Ausreden erfordern neuen Cursor !!!
            evalMouseMoveEvent(mainFrame.mousePoint);
            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
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
                mainFrame.pathWalker.SetzeNeuenWeg(new GenericPoint(140, 477));
                nextActionID = 605;
                break;

            case 605:
                // Anim runtergefallt
                isBuschVisible = false;
                KrabatSagt("Murja_1", 6, 3, 2, 610);
                break;

            case 610:
                // nun sagen, dass man sich ja umziehen moechte
                KrabatSagt("Murja_2", 0, 3, 2, 620);
                break;

            case 620:
                // aus dem Bild laufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(new GenericPoint(-80, 478));
                nextActionID = 630;
                break;

            case 630:
                // umziehen
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.checkKrabat();
                mainFrame.inventory.vInventory.addElement(41);
                mainFrame.inventory.vInventory.removeElement(53);
                nextActionID = 640;
                break;

            case 640:
                // wieder herkommen
                mainFrame.pathWalker.SetzeNeuenWeg(new GenericPoint(119, 474));
                nextActionID = 650;
                break;

            case 650:
                // Ende Anim
                mainFrame.isClipSet = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}