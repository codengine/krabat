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
import de.codengine.krabat.anims.GuardTreasure;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Trepjena extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Trepjena.class);
    private GenericImage background;
    private GenericImage trVorn;
    private final GuardTreasure guardTreasure;
    // private Dinglinger dinglinger;

    private final GenericPoint talkPoint;
    private final GenericPoint strazaPoint;
    private final BorderRect rectStraza;

    private boolean weistzurueck = false;

    // Konstanten - Rects
    private static final BorderRect ausgangOben
            = new BorderRect(140, 5, 170, 95);
    private static final BorderRect ausgangMitte
            = new BorderRect(145, 177, 172, 280);
    private static final BorderRect ausgangUnten
            = new BorderRect(102, 325, 135, 457);
    private static final BorderRect ausgangHof
            = new BorderRect(460, 337, 526, 463);
    private static final BorderRect rectSchild
            = new BorderRect(73, 347, 98, 392);
    private static final BorderRect rectBildOben
            = new BorderRect(339, 163, 390, 225);
    private static final BorderRect rectBildUnten
            = new BorderRect(333, 346, 385, 410);
    private static final BorderRect durje
            = new BorderRect(105, 344, 118, 457);
    private static final BorderRect wokno1
            = new BorderRect(248, 13, 278, 60);
    private static final BorderRect wokno2
            = new BorderRect(323, 12, 354, 58);
    private static final BorderRect wokno3
            = new BorderRect(385, 12, 414, 58);
    private static final BorderRect wokno4
            = new BorderRect(493, 15, 525, 59);

    // Konstante Points
    private static final GenericPoint pExitOben = new GenericPoint(172, 103);
    private static final GenericPoint pExitMitte = new GenericPoint(235, 288);
    // private static final GenericPoint pExitUnten = new GenericPoint (124, 461);
    private static final GenericPoint pExitHof = new GenericPoint(507, 466);
    private static final GenericPoint pStraza = new GenericPoint(235, 288);
    private static final GenericPoint pBildOben = new GenericPoint(365, 475);
    private static final GenericPoint pBildUnten = new GenericPoint(360, 465);
    private static final GenericPoint strazaFeet = new GenericPoint(176, 287);
    private static final GenericPoint pDurje = new GenericPoint(110, 457);
    private static final GenericPoint pWokno1 = new GenericPoint(263, 93);
    private static final GenericPoint pWokno2 = new GenericPoint(338, 93);
    private static final GenericPoint pWokno3 = new GenericPoint(401, 92);
    private static final GenericPoint pWokno4 = new GenericPoint(509, 93);
    private static final GenericPoint pSchild = new GenericPoint(107, 467);

    // Konstante ints
    private static final int fBildUnten = 12;
    private static final int fBildOben = 12;
    private static final int fWokna = 12;
    private static final int fKapala = 9;
    private static final int fTafla = 9;
    private static final int fPoklad = 9;
    private static final int fStraza = 9;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Trepjena(Start caller, int oldLocation) {
        super(caller, 131);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 8f;
        mainFrame.krabat.defScale = 20;

        guardTreasure = new GuardTreasure(mainFrame);

        strazaPoint = new GenericPoint();
        strazaPoint.x = strazaFeet.x - GuardTreasure.Breite / 2;
        strazaPoint.y = strazaFeet.y - GuardTreasure.Hoehe;

        talkPoint = new GenericPoint();
        talkPoint.x = strazaFeet.x;
        talkPoint.y = strazaPoint.y - 50;

        rectStraza = new BorderRect(strazaPoint.x, strazaPoint.y, strazaPoint.x + GuardTreasure.Breite, strazaPoint.y + GuardTreasure.Hoehe);

        InitLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(296, 520, 296, 555, 466, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(140, 295, 90, 295, 461, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(215, 220, 215, 220, 414, 460));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(205, 330, 205, 330, 412, 413));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(325, 330, 325, 330, 360, 410));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(515, 520, 325, 330, 290, 359));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(235, 520, 235, 520, 288, 289));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(245, 250, 245, 250, 252, 287));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(240, 325, 240, 325, 250, 251));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(320, 325, 320, 325, 175, 249));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(513, 518, 320, 325, 105, 174));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(175, 518, 175, 518, 103, 104));

        mainFrame.pathFinder.ClearMatrix(12);

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

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 130: // von Hdwor aus
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                mainFrame.krabat.setPos(new GenericPoint(507, 470));
                mainFrame.krabat.SetFacing(6);
                break;
/*      case 132: // von Poklad aus
      	mainFrame.krabat.SetKrabatPos (new GenericPoint (128, 463));
      	mainFrame.krabat.SetFacing (3);
      	break;*/
/*      case 133: // von Kapala aus
      	mainFrame.krabat.SetKrabatPos (new GenericPoint (178, 288));
      	mainFrame.krabat.SetFacing (3);
      	break;*/
            case 140: // von Saal aus
                mainFrame.krabat.setPos(new GenericPoint(176, 103));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/trepj/trepj.png");
        trVorn = getPicture("gfx-dd/trepj/trepj-vorn.png");

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

        // Straza zeichnen
        g.setClip(strazaPoint.x, strazaPoint.y, GuardTreasure.Breite, GuardTreasure.Hoehe);
        g.drawImage(background, 0, 0);
        guardTreasure.drawStraza(g, TalkPerson, strazaPoint, weistzurueck);
        g.drawImage(trVorn, 147, 0);

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
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Vordergrund zeichnen (nur Clipping - Region wird neugezeichnet)
        if (pKrTemp.y <= 415) {
            g.drawImage(trVorn, 147, 0);
        }

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

                // Ausreden Tuer
                if (durje.IsPointInRect(pTemp)) {
                    // kluc
                    nextActionID = mainFrame.whatItem == 47 ? 200 : 150;
                    pTemp = pDurje;
                }

                // Ausreden wokno1
                if (wokno1.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno1;
                }

                // Ausreden wokno2
                if (wokno2.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno2;
                }

                // Ausreden wokno3
                if (wokno3.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno3;
                }

                // Ausreden wokno4
                if (wokno4.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pWokno4;
                }

                // Ausreden Straza
                if (rectStraza.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pStraza;
                }

                // Ausreden Tafla
                if (rectSchild.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 46: // hammer
                        case 42: // Hlebija
                        case 12: // kamuski
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 165;
                            break;
                    }
                    pTemp = pSchild;
                }

                // Ausreden BildOben
                if (rectBildOben.IsPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTemp = pBildOben;
                }

                // Ausreden BildUnten
                if (rectBildUnten.IsPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = pBildUnten;
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

                // zu Hdwor gehen ?
                if (ausgangHof.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangHof.IsPointInRect(kt)) {
                        pTemp = pExitHof;
                    } else {
                        pTemp = new GenericPoint(pExitHof.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Saal (oben) gehen ?
                if (ausgangOben.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangOben.IsPointInRect(kt)) {
                        pTemp = pExitOben;
                    } else {
                        pTemp = new GenericPoint(pExitOben.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Straza ansehen
                if (rectStraza.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pStraza;
                }

                // zu Poklad (mitte) gehen versuchen
                if (ausgangMitte.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pExitMitte;
                }

                // zu Kapala (unten) gehen ? -> verschlossen
                if (durje.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pDurje;
                }

                // Schild ansehen
                if (rectSchild.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pSchild;
                }

                // BildUnten ansehen
                if (rectBildUnten.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pBildUnten;
                }

                // BildOben ansehen
                if (rectBildOben.IsPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTemp = pBildOben;
                }

                // Ansehen wokno1
                if (wokno1.IsPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno1;
                }

                // Ansehen wokno2
                if (wokno2.IsPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno2;
                }

                // Ansehen wokno3
                if (wokno3.IsPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno3;
                }

                // Ansehen wokno4
                if (wokno4.IsPointInRect(pTemp)) {
                    nextActionID = 20;
                    pTemp = pWokno4;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit Straza reden
                if (rectStraza.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    mainFrame.pathWalker.SetzeNeuenWeg(pStraza);
                    mainFrame.repaint();
                    return;
                }

                // Schild lesen
                if (rectSchild.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSchild);
                    mainFrame.repaint();
                    return;
                }

                // BildUnten mitnehmen
                if (rectBildUnten.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(pBildUnten);
                    mainFrame.repaint();
                    return;
                }

                // BildOben mitnehmen
                if (rectBildOben.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(pBildOben);
                    mainFrame.repaint();
                    return;
                }

                // Tuer mitnehmen
                if (durje.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.SetzeNeuenWeg(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Wokno1 mitnehmen
                if (wokno1.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(pWokno1);
                    mainFrame.repaint();
                    return;
                }

                // Wokno2 mitnehmen
                if (wokno2.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(pWokno2);
                    mainFrame.repaint();
                    return;
                }

                // Wokno3 mitnehmen
                if (wokno3.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(pWokno3);
                    mainFrame.repaint();
                    return;
                }

                // Wokno4 mitnehmen
                if (wokno4.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(pWokno4);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangHof.IsPointInRect(pTemp) ||
                        ausgangUnten.IsPointInRect(pTemp) ||
                        ausgangMitte.IsPointInRect(pTemp) ||
                        ausgangOben.IsPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || rectStraza.IsPointInRect(pTemp) ||
                    durje.IsPointInRect(pTemp) || wokno1.IsPointInRect(pTemp) ||
                    wokno2.IsPointInRect(pTemp) || wokno3.IsPointInRect(pTemp) ||
                    wokno4.IsPointInRect(pTemp) || rectSchild.IsPointInRect(pTemp) ||
                    rectBildUnten.IsPointInRect(pTemp) || rectBildOben.IsPointInRect(pTemp);

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
            if (rectStraza.IsPointInRect(pTemp) ||
                    durje.IsPointInRect(pTemp) || wokno1.IsPointInRect(pTemp) ||
                    wokno2.IsPointInRect(pTemp) || wokno3.IsPointInRect(pTemp) ||
                    wokno4.IsPointInRect(pTemp) || rectSchild.IsPointInRect(pTemp) ||
                    rectBildUnten.IsPointInRect(pTemp) || rectBildOben.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangHof.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangOben.IsPointInRect(pTemp)) {
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
            case 1:
                // Straza anschauen
                KrabatSagt("Trepjena_1", fStraza, 3, 0, 0);
                break;

            case 2:
                // Mit Wache reden
                mainFrame.krabat.SetFacing(fStraza);
                mainFrame.isAnimRunning = true;
                nextActionID = 300;
                mainFrame.repaint();
                break;

            case 3:
                // An Wache vorbei zur Tuer gehen (versuchen)
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.SetFacing(fPoklad);
                nextActionID = 301;
                mainFrame.repaint();
                break;

            case 4:
                // Schild ansehen
                KrabatSagt("Trepjena_2", fTafla, 3, 0, 0);
                break;

            case 5:
                // Schild lesen
                KrabatSagt("Trepjena_3", fTafla, 3, 0, 0);
                break;

            case 6:
                // Zur Kapelle (unten) gehen -> verschlossen
                KrabatSagt("Trepjena_4", fKapala, 3, 0, 0);
                break;

            case 7:
                // BildUnten ansehen
                KrabatSagt("Trepjena_5", fBildUnten, 3, 0, 0);
                break;

            case 8:
                // BildOben ansehen
                KrabatSagt("Trepjena_6", fBildOben, 3, 0, 0);
                break;

            case 20:
                // wokna ansehen
                KrabatSagt("Trepjena_7", fWokna, 3, 0, 0);
                break;

            case 50:
                // BildUnten mitnehmen
                KrabatSagt("Trepjena_8", fBildUnten, 3, 0, 0);
                break;

            case 55:
                // BildOben mitnehmen
                KrabatSagt("Trepjena_9", fBildOben, 3, 0, 0);
                break;

            case 60:
                // Tuer mitnehmen
                KrabatSagt("Trepjena_10", fKapala, 3, 0, 0);
                break;

            case 65:
                // Fenster mitnehmen
                KrabatSagt("Trepjena_11", fWokna, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Hdwor
                NeuesBild(130, locationID);
                break;

            case 101:
                // Gehe zu Poklad
                NeuesBild(132, locationID);
                break;

            case 102:
                // Gehe zu Kapala
                NeuesBild(133, locationID);
                break;

            case 103:
                // Gehe zu Saal
                NeuesBild(140, locationID);
                break;

            case 150:
                // durje-ausreden
                DingAusrede(fKapala);
                break;

            case 155:
                // wokno-ausreden
                DingAusrede(fWokna);
                break;

            case 160:
                // straza-ausreden
                MPersonAusrede(fStraza);
                break;

            case 165:
                // tafla-ausreden
                DingAusrede(fTafla);
                break;

            case 170:
                // wobrazoben-ausreden
                DingAusrede(fBildOben);
                break;

            case 175:
                // wobrazunten-ausreden
                DingAusrede(fBildUnten);
                break;

            case 200:
                // kluc auf durje
                KrabatSagt("Trepjena_12", fKapala, 3, 0, 0);
                break;

            case 210:
                // schwere ggst auf tafla.
                KrabatSagt("Trepjena_13", fTafla, 3, 0, 0);
                break;

            // Versuch, mit Stra#za zu reden
            case 300:
                // zufaellige Antwort -> Zahl von 0 bis 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        PersonSagt("Trepjena_14", fStraza, 46, 2, 800, talkPoint);
                        break;

                    case 1:
                        PersonSagt("Trepjena_15", fStraza, 46, 2, 800, talkPoint);
                        break;
                }
                break;

            // Versuch, an Wache vorbeizugehen
            case 301:
                // zufaellige Antwort -> Zahl von 0 bis 1 generieren
                int zuffZahl2 = (int) (Math.random() * 1.9);
                weistzurueck = true;
                switch (zuffZahl2) {
                    case 0:
                        PersonSagt("Trepjena_16", fStraza, 46, 2, 800, talkPoint);
                        break;

                    case 1:
                        PersonSagt("Trepjena_17", fStraza, 46, 2, 800, talkPoint);
                        break;
                }
                break;

            case 800:
                // Dialog beenden, wenn zuende gelabert...
                weistzurueck = false;
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}