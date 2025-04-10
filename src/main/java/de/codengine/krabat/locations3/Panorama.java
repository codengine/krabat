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
import de.codengine.krabat.anims.Boats;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Panorama extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Panorama.class);
    private GenericImage background;
    private GenericImage cychi;

    private final Boats boot;

    // Konstanten - Rects
    private static final BorderRect ausgangKartaOben
            = new BorderRect(295, 110, 400, 297);
    // private static final borderrect ausgangKartaRechts
    //     = new borderrect (590, 310, 639, 440);
    private static final BorderRect ausgangZahrodnik
            = new BorderRect(498, 343, 524, 377);
    private static final BorderRect ausgangStwa
            = new BorderRect(254, 354, 278, 380);
    private static final BorderRect ausgangWobjo
            = new BorderRect(17, 315, 62, 372);

    // Konstante Points
    private static final GenericPoint pExitKartaOben = new GenericPoint(327, 310);
    // private static final GenericPoint pExitKartaRechts = new GenericPoint (625, 385);
    private static final GenericPoint pExitZahrodnik = new GenericPoint(511, 378);
    private static final GenericPoint pExitStwa = new GenericPoint(266, 385);
    private static final GenericPoint pExitWobjo = new GenericPoint(37, 373);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Panorama(Start caller, int oldLocation) {
        super(caller, 160);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 377;
        mainFrame.krabat.zoomf = 10f;
        mainFrame.krabat.defScale = 73;

        boot = new Boats(mainFrame, 3);

        InitLocation(oldLocation);
        mainFrame.freeze(false);

    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(326, 330, 308, 313, 310, 377));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(300, 625, 300, 625, 378, 405));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(230, 299, 230, 299, 381, 405));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(38, 229, 38, 229, 388, 410));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(36, 37, 36, 37, 376, 400));

        mainFrame.pathFinder.ClearMatrix(5);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                break;
            case 180: // von Karta (Oben)
                mainFrame.krabat.setPos(new GenericPoint(327, 310));
                mainFrame.krabat.SetFacing(6);
                break;
            // case 170: // von KartaRechts   ???????????????? (Zastup)
            //	mainFrame.krabat.SetKrabatPos (new GenericPoint (620, 385));
            //	mainFrame.krabat.SetFacing (9);
            //	break;
            case 161: // von Stwa
                mainFrame.krabat.setPos(new GenericPoint(266, 385));
                mainFrame.krabat.SetFacing(6);
                break;
            case 162: // von Zahrodnik
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(new GenericPoint(511, 382));
                mainFrame.krabat.SetFacing(6);
                break;
            case 163: // von Wobjo (Habor)
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(new GenericPoint(37, 376));
                mainFrame.krabat.SetFacing(6);
                break;
        }

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/panorama/panorama.png");
        cychi = getPicture("gfx-dd/panorama/cychi.png");

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

        // Boot-Routine
        // Hintergrund loeschen
        BorderRect temp = boot.evalBootRect();
        g.setClip(temp.lo_point.x, temp.lo_point.y,
                temp.ru_point.x - temp.lo_point.x, temp.ru_point.y - temp.lo_point.y);
        g.drawImage(background, 0, 0);
        // Boot zeichnen
        boot.drawBoot(g);

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
        g.drawImage(cychi, 0, 329);

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
    /*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
    {
      g.drawImage (weiden2, 84, 221, null);
    }*/

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

                // zu KarteOben gehen ?
                if (ausgangKartaOben.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKartaOben.IsPointInRect(kt)) {
                        pTemp = pExitKartaOben;
                    } else {
                        pTemp = new GenericPoint(pExitKartaOben.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu KartaRechts gehen ?
        /*if (ausgangKartaRechts.IsPointInRect (pTemp) == true)
        { 
          nextActionID = 100;
          GenericPoint kt = mainFrame.krabat.GetKrabatPos();
          
          // Wenn nahe am Ausgang, dann "gerade" verlassen
          if (ausgangKartaRechts.IsPointInRect (kt) == false)
          {
          	pTemp = pExitKartaRechts;
          }
          else
          {
          	pTemp = new GenericPoint (pExitKartaRechts.x, kt.y);
          }
            
          if (mainFrame.dClick == true)
          {
            mainFrame.krabat.StopWalking();
            mainFrame.repaint();
            return;
          }  
        }*/

                // zu Zahrodnik gehen ?
                if (ausgangZahrodnik.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangZahrodnik.IsPointInRect(kt)) {
                        pTemp = pExitZahrodnik;
                    } else {
                        pTemp = new GenericPoint(pExitZahrodnik.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Stwa gehen ?
                if (ausgangStwa.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangStwa.IsPointInRect(kt)) {
                        pTemp = pExitStwa;
                    } else {
                        pTemp = new GenericPoint(pExitStwa.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Habor gehen ?
                if (ausgangWobjo.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangWobjo.IsPointInRect(kt)) {
                        pTemp = pExitWobjo;
                    } else {
                        pTemp = new GenericPoint(pExitWobjo.x, kt.y);
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
                if (ausgangKartaOben.IsPointInRect(pTemp) ||
                        // (ausgangKartaRechts.IsPointInRect (pTemp)) ||
                        ausgangZahrodnik.IsPointInRect(pTemp) ||
                        ausgangStwa.IsPointInRect(pTemp) ||
                        ausgangWobjo.IsPointInRect(pTemp)) {
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
            // if ((kellerFenster.IsPointInRect (pTemp) == true) ||
            //   (wino.IsPointInRect (pTemp) == true))
            // {
            //   if (Cursorform != 1)
            //   {
            //      mainFrame.setCursor (mainFrame.Kreuz);
            //      Cursorform = 1;
            //   }
            //  return;
            // }
   
 /*     if (ausgangKartaRechts.IsPointInRect (pTemp) == true) {
	  if (Cursorform != 3) {
	      mainFrame.setCursor (mainFrame.Cright);
	      Cursorform = 3;
	  }
	  return;
      }*/

            if (ausgangKartaOben.IsPointInRect(pTemp) ||
                    ausgangZahrodnik.IsPointInRect(pTemp) ||
                    ausgangStwa.IsPointInRect(pTemp) ||
                    ausgangWobjo.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
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
                // Gehe zu Karta
                NeuesBild(180, locationID);
                break;

            case 101:
                // Gehe zu Stwa
                NeuesBild(161, locationID);
                break;

            case 102:
                // Gehe zu Zahrodnik
                NeuesBild(162, locationID);
                break;

            case 103:
                // Gehe zu Habor
                NeuesBild(163, locationID);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}