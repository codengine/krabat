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
import de.codengine.krabat.anims.StrazaPoklad;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Trepjena extends Mainloc {
    private GenericImage background, trVorn;
    private final StrazaPoklad strazaPoklad;
    // private Dinglinger dinglinger;

    private final GenericPoint talkPoint;
    private final GenericPoint strazaPoint;
    private final Borderrect rectStraza;

    private boolean weistzurueck = false;

    // Konstanten - Rects
    private static final Borderrect ausgangOben
            = new Borderrect(140, 5, 170, 95);
    private static final Borderrect ausgangMitte
            = new Borderrect(145, 177, 172, 280);
    private static final Borderrect ausgangUnten
            = new Borderrect(102, 325, 135, 457);
    private static final Borderrect ausgangHof
            = new Borderrect(460, 337, 526, 463);
    private static final Borderrect rectSchild
            = new Borderrect(73, 347, 98, 392);
    private static final Borderrect rectBildOben
            = new Borderrect(339, 163, 390, 225);
    private static final Borderrect rectBildUnten
            = new Borderrect(333, 346, 385, 410);
    private static final Borderrect durje
            = new Borderrect(105, 344, 118, 457);
    private static final Borderrect wokno1
            = new Borderrect(248, 13, 278, 60);
    private static final Borderrect wokno2
            = new Borderrect(323, 12, 354, 58);
    private static final Borderrect wokno3
            = new Borderrect(385, 12, 414, 58);
    private static final Borderrect wokno4
            = new Borderrect(493, 15, 525, 59);

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
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 8f;
        mainFrame.krabat.defScale = 20;

        strazaPoklad = new StrazaPoklad(mainFrame);

        strazaPoint = new GenericPoint();
        strazaPoint.x = strazaFeet.x - StrazaPoklad.Breite / 2;
        strazaPoint.y = strazaFeet.y - StrazaPoklad.Hoehe;

        talkPoint = new GenericPoint();
        talkPoint.x = strazaFeet.x;
        talkPoint.y = strazaPoint.y - 50;

        rectStraza = new Borderrect(strazaPoint.x, strazaPoint.y, strazaPoint.x + StrazaPoklad.Breite, strazaPoint.y + StrazaPoklad.Hoehe);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(296, 520, 296, 555, 466, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(140, 295, 90, 295, 461, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(215, 220, 215, 220, 414, 460));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(205, 330, 205, 330, 412, 413));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(325, 330, 325, 330, 360, 410));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(515, 520, 325, 330, 290, 359));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(235, 520, 235, 520, 288, 289));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(245, 250, 245, 250, 252, 287));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(240, 325, 240, 325, 250, 251));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(320, 325, 320, 325, 175, 249));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(513, 518, 320, 325, 105, 174));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(175, 518, 175, 518, 103, 104));

        mainFrame.wegSucher.ClearMatrix(12);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(5, 6);
        mainFrame.wegSucher.PosVerbinden(6, 7);
        mainFrame.wegSucher.PosVerbinden(7, 8);
        mainFrame.wegSucher.PosVerbinden(8, 9);
        mainFrame.wegSucher.PosVerbinden(9, 10);
        mainFrame.wegSucher.PosVerbinden(10, 11);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 130: // von Hdwor aus
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(507, 470));
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
                mainFrame.krabat.SetKrabatPos(new GenericPoint(176, 103));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/trepj/trepj.gif");
        trVorn = getPicture("gfx-dd/trepj/trepj-vorn.gif");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Straza zeichnen
        g.setClip(strazaPoint.x, strazaPoint.y, StrazaPoklad.Breite, StrazaPoklad.Hoehe);
        g.drawImage(background, 0, 0, null);
        strazaPoklad.drawStraza(g, TalkPerson, strazaPoint, weistzurueck);
        g.drawImage(trVorn, 147, 0, null);

        mainFrame.wegGeher.GeheWeg();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // Vordergrund zeichnen (nur Clipping - Region wird neugezeichnet)
        if (pKrTemp.y <= 415) {
            g.drawImage(trVorn, 147, 0, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

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
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Hdwor gehen ?
                if (ausgangHof.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangHof.IsPointInRect(kt)) {
                        pTemp = pExitHof;
                    } else {
                        pTemp = new GenericPoint(pExitHof.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Saal (oben) gehen ?
                if (ausgangOben.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangOben.IsPointInRect(kt)) {
                        pTemp = pExitOben;
                    } else {
                        pTemp = new GenericPoint(pExitOben.x, kt.y);
                    }

                    if (mainFrame.dClick) {
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

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit Straza reden
                if (rectStraza.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    mainFrame.wegGeher.SetzeNeuenWeg(pStraza);
                    mainFrame.repaint();
                    return;
                }

                // Schild lesen
                if (rectSchild.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSchild);
                    mainFrame.repaint();
                    return;
                }

                // BildUnten mitnehmen
                if (rectBildUnten.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBildUnten);
                    mainFrame.repaint();
                    return;
                }

                // BildOben mitnehmen
                if (rectBildOben.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBildOben);
                    mainFrame.repaint();
                    return;
                }

                // Tuer mitnehmen
                if (durje.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Wokno1 mitnehmen
                if (wokno1.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWokno1);
                    mainFrame.repaint();
                    return;
                }

                // Wokno2 mitnehmen
                if (wokno2.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWokno2);
                    mainFrame.repaint();
                    return;
                }

                // Wokno3 mitnehmen
                if (wokno3.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWokno3);
                    mainFrame.repaint();
                    return;
                }

                // Wokno4 mitnehmen
                if (wokno4.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWokno4);
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
        if (mainFrame.fPlayAnim || mainFrame.krabat.nAnimation != 0) {
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || rectStraza.IsPointInRect(pTemp) ||
                    durje.IsPointInRect(pTemp) || wokno1.IsPointInRect(pTemp) ||
                    wokno2.IsPointInRect(pTemp) || wokno3.IsPointInRect(pTemp) ||
                    wokno4.IsPointInRect(pTemp) || rectSchild.IsPointInRect(pTemp) ||
                    rectBildUnten.IsPointInRect(pTemp) || rectBildOben.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.invHighCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if (Cursorform != 11 && mainFrame.invHighCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
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
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangHof.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangOben.IsPointInRect(pTemp)) {
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
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();
            // manche Ausreden erfordern neuen Cursor !!!
            evalMouseMoveEvent(mainFrame.Mousepoint);
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
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00000"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00001"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00002"),
                        fStraza, 3, 0, 0);
                break;

            case 2:
                // Mit Wache reden
                mainFrame.krabat.SetFacing(fStraza);
                mainFrame.fPlayAnim = true;
                nextActionID = 300;
                mainFrame.repaint();
                break;

            case 3:
                // An Wache vorbei zur Tuer gehen (versuchen)
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.SetFacing(fPoklad);
                nextActionID = 301;
                mainFrame.repaint();
                break;

            case 4:
                // Schild ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00003"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00004"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00005"),
                        fTafla, 3, 0, 0);
                break;

            case 5:
                // Schild lesen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00006"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00007"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00008"),
                        fTafla, 3, 0, 0);
                break;

            case 6:
                // Zur Kapelle (unten) gehen -> verschlossen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00009"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00010"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00011"),
                        fKapala, 3, 0, 0);
                break;

            case 7:
                // BildUnten ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00012"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00013"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00014"),
                        fBildUnten, 3, 0, 0);
                break;

            case 8:
                // BildOben ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00015"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00016"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00017"),
                        fBildOben, 3, 0, 0);
                break;

            case 20:
                // wokna ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00018"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00019"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00020"),
                        fWokna, 3, 0, 0);
                break;

            case 50:
                // BildUnten mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00021"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00022"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00023"),
                        fBildUnten, 3, 0, 0);
                break;

            case 55:
                // BildOben mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00024"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00025"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00026"),
                        fBildOben, 3, 0, 0);
                break;

            case 60:
                // Tuer mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00027"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00028"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00029"),
                        fKapala, 3, 0, 0);
                break;

            case 65:
                // Fenster mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00030"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00031"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00032"),
                        fWokna, 3, 0, 0);
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
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00033"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00034"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00035"),
                        fKapala, 3, 0, 0);
                break;

            case 210:
                // schwere ggst auf tafla.
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00036"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00037"),
                        Start.stringManager.getTranslation("Loc3_Trepjena_00038"),
                        fTafla, 3, 0, 0);
                break;

            // Versuch, mit Stra#za zu reden
            case 300:
                // zufaellige Antwort -> Zahl von 0 bis 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00039"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00040"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00041"),
                                fStraza, 46, 2, 800, talkPoint);
                        break;

                    case 1:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00042"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00043"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00044"),
                                fStraza, 46, 2, 800, talkPoint);
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
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00045"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00046"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00047"),
                                fStraza, 46, 2, 800, talkPoint);
                        break;

                    case 1:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Trepjena_00048"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00049"),
                                Start.stringManager.getTranslation("Loc3_Trepjena_00050"),
                                fStraza, 46, 2, 800, talkPoint);
                        break;
                }
                break;

            case 800:
                // Dialog beenden, wenn zuende gelabert...
                weistzurueck = false;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}