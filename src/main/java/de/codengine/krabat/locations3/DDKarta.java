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

public class DDKarta extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(DDKarta.class);
    private GenericImage background;
    private GenericImage vor1;
    private GenericImage vor2;
    private GenericImage vor3;
    private GenericImage vor4;

    // Konstanten - Rects
    private static final BorderRect brPanorama
            = new BorderRect(312, 28, 394, 91);
    private static final BorderRect brHrod
            = new BorderRect(202, 246, 279, 290);
    private static final BorderRect brStarewiki
            = new BorderRect(295, 381, 392, 479);
    private static final BorderRect brZastup
            = new BorderRect(417, 10, 488, 85);

    // Konstante Points
    private static final GenericPoint pPanorama = new GenericPoint(355, 78);
    private static final GenericPoint pStarewiki = new GenericPoint(320, 427);
    private static final GenericPoint pZastup = new GenericPoint(479, 72);
    private static final GenericPoint pHrodmost = new GenericPoint(280, 226);
    // private static final GenericPoint pHrodwiki  = new GenericPoint (188, 250);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public DDKarta(Start caller, int oldLocation) {
        super(caller, 180);
        mainFrame.freeze(true);

        // Schmied raushauen, wenn Hammer genommen
        if (mainFrame.actions[953]) {
            mainFrame.actions[701] = true;
        }

        mainFrame.actions[851] = true;
        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 36f;
        mainFrame.krabat.defScale = 0;

        InitLocation(oldLocation);

        // Kr. hat Enterhaken bekommen -> Schiff kann wegfahren -> anderes kommt, sobald mit Dinglinger geredet
        if (mainFrame.actions[561] && mainFrame.actions[529]) {
            mainFrame.actions[568] = true;
        }

        // Kr. hat Metall bekommen -> Schiff kann wegfahren -> nun is leer
        if (mainFrame.actions[559]) {
            mainFrame.actions[569] = true;
        }

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(479, 480, 474, 475, 72, 76));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(474, 475, 456, 457, 77, 79));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(425, 80, 457, 81));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(425, 82, 426, 98));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(405, 99, 426, 100));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(365, 366, 405, 406, 91, 98));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(365, 78, 366, 90));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(352, 76, 366, 77));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(355, 356, 309, 310, 78, 164));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(309, 310, 279, 280, 165, 228));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(190, 229, 280, 230));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(190, 231, 191, 291));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(190, 191, 160, 161, 292, 336));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(160, 337, 161, 385));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(160, 386, 206, 387));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(295, 296, 205, 206, 381, 385));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(295, 379, 320, 380));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(319, 381, 320, 436));

        mainFrame.pathFinder.ClearMatrix(18);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);
        mainFrame.pathFinder.PosVerbinden(4, 5);
        mainFrame.pathFinder.PosVerbinden(5, 6);
        mainFrame.pathFinder.PosVerbinden(6, 7);
        mainFrame.pathFinder.PosVerbinden(7, 8);
        mainFrame.pathFinder.PosVerbinden(8, 9);
        mainFrame.pathFinder.PosVerbinden(9, 10);
        mainFrame.pathFinder.PosVerbinden(10, 11);
        mainFrame.pathFinder.PosVerbinden(11, 12);
        mainFrame.pathFinder.PosVerbinden(12, 13);
        mainFrame.pathFinder.PosVerbinden(13, 14);
        mainFrame.pathFinder.PosVerbinden(14, 15);
        mainFrame.pathFinder.PosVerbinden(15, 16);
        mainFrame.pathFinder.PosVerbinden(16, 17);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                break;
            case 160: // von Panorama
                mainFrame.krabat.setPos(pPanorama);
                mainFrame.krabat.SetFacing(6);
                break;
            case 170: // von Zastup
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(pZastup);
                mainFrame.krabat.SetFacing(6);
                break;
            case 127: // von Hrod (Terassa)
                mainFrame.krabat.setPos(pHrodmost);
                mainFrame.krabat.SetFacing(12);
                break;
            case 175: // von Starewiki
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(pStarewiki);
                mainFrame.krabat.SetFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/ddkarta/ddkarta.png");
        vor1 = getPicture("gfx-dd/ddkarta/kar1.png");
        vor2 = getPicture("gfx-dd/ddkarta/kar2.png");
        vor3 = getPicture("gfx-dd/ddkarta/kar3.png");
        vor4 = getPicture("gfx-dd/ddkarta/kar4.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // wegen geringen Aufwandes (kleine Images) zeichnen wir immer neu
        g.drawImage(vor1, 343, 79);
        g.drawImage(vor2, 307, 151);
        g.drawImage(vor3, 279, 215);
        g.drawImage(vor4, 231, 214);

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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
        GenericPoint pTemp = e.getPoint();
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

                // zu Panorama gehen ?
                // da hier durch die Exits durchgerannt werden kann, muss die Abfrage ein bisschen anders gestaltet werden
                if (brPanorama.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn im Ausgangrect, dann schon umschalten
                    if (!brPanorama.IsPointInRect(kt)) {
                        pTemp = pPanorama;
                    } else {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Zastup gehen ?
                // da hier durch die Exits durchgerannt werden kann, muss die Abfrage ein bisschen anders gestaltet werden
                if (brZastup.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn im Ausgangrect, dann schon umschalten
                    if (!brZastup.IsPointInRect(kt)) {
                        pTemp = pZastup;
                    } else {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Hrod gehen ?
                // da hier durch die Exits durchgerannt werden kann, muss die Abfrage ein bisschen anders gestaltet werden
                if (brHrod.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn im Ausgangrect, dann schon umschalten
                    if (!brHrod.IsPointInRect(kt)) {
                        pTemp = pHrodmost;
                    } else {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Starewiki gehen ?
                // da hier durch die Exits durchgerannt werden kann, muss die Abfrage ein bisschen anders gestaltet werden
                if (brStarewiki.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn im Ausgangrect, dann schon umschalten
                    if (!brStarewiki.IsPointInRect(kt)) {
                        pTemp = pStarewiki;
                    } else {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
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
                if (brPanorama.IsPointInRect(pTemp) ||
                        brZastup.IsPointInRect(pTemp) ||
                        brHrod.IsPointInRect(pTemp) ||
                        brStarewiki.IsPointInRect(pTemp)) {
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
    public void evalMouseMoveEvent(GenericPoint pTemp) {
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
            // bei Hrod und Starewiki nach unten
            if (brHrod.IsPointInRect(pTemp) || brStarewiki.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 3;
                }
                return;
            }

            // Zastup und Panorama nach Oben
            if (brZastup.IsPointInRect(pTemp) || brPanorama.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 2;
                }
                return;
            }

            // oben
      /*if (brPanorama.IsPointInRect (pTemp) == true)
      {
      	if (Cursorform != 12) 
      	{
	        mainFrame.setCursor (mainFrame.Cup);
	        Cursorform = 12;
	      }
	      return;
      } */

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
                // Gehe zu Panorama
                mainFrame.actions[851] = false;
                NeuesBild(160, locationID);
                break;

            case 101:
                // Gehe zu Zastup
                mainFrame.actions[851] = false;
                NeuesBild(170, locationID);
                break;

            case 102:
                // Gehe zu Hrod
                mainFrame.actions[851] = false;
                NeuesBild(127, locationID);
                break;

            case 103:
                // Gehe zu Starewiki
                mainFrame.actions[851] = false;
                NeuesBild(175, locationID);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}