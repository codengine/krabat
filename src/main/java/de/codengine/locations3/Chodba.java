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

public class Chodba extends Mainloc {
    private GenericImage background;

    // Konstanten - Rects
    private static final Borderrect rechterAusgang
            = new Borderrect(545, 320, 639, 479);
    private static final Borderrect obererAusgang
            = new Borderrect(215, 245, 270, 345);
    private static final Borderrect bilder
            = new Borderrect(463, 250, 500, 339);

    // Konstante Points
    private static final GenericPoint pExitUp = new GenericPoint(255, 350);
    private static final GenericPoint pExitRight = new GenericPoint(595, 467);
    private static final GenericPoint pBilder = new GenericPoint(464, 412);

    // Konstante ints
    private static final int fBilder = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Chodba(Start caller, int oldLocation) {
        super(caller, 142);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 1.34f;
        mainFrame.krabat.defScale = -90;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(70, 595, 60, 595, 435, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(210, 331, 165, 430, 382, 434));

        mainFrame.wegSucher.ClearMatrix(2);

        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 141:  // von Dingl
                mainFrame.krabat.SetKrabatPos(new GenericPoint(595, 467));
                mainFrame.krabat.SetFacing(9);
                break;
            case 146:  // von Wonka aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(270, 395));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/chodba/chodba.gif");
        loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (mainFrame.Clipset == false) {
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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
    /*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
    {
      g.drawImage (weiden2, 84, 221, null);
    }*/

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
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
        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim == true) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // linker Maustaste
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp) == true) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Bilder
                if (bilder.IsPointInRect(pTemp) == true) {
                    nextActionID = 150;
                    pTemp = pBilder;
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
                return;
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Dingl gehen ?
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (rechterAusgang.IsPointInRect(kt) == false) {
                        pTemp = pExitRight;
                    } else {
                        pTemp = new GenericPoint(pExitRight.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Wonka gehen ?
                if (obererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (obererAusgang.IsPointInRect(kt) == false) {
                        pTemp = pExitUp;
                    } else {
                        pTemp = new GenericPoint(pExitUp.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Ansehen Bilder
                if (bilder.IsPointInRect(pTemp) == true) {
                    nextActionID = 1;
                    pTemp = pBilder;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Bilder mitnehmen
                if (bilder.IsPointInRect(pTemp) == true) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBilder);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((rechterAusgang.IsPointInRect(pTemp) == true) ||
                        (obererAusgang.IsPointInRect(pTemp) == true)) {
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
        if ((mainFrame.fPlayAnim == true) || (mainFrame.krabat.nAnimation != 0)) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            if ((tmp.IsPointInRect(pTemp) == true) || (bilder.IsPointInRect(pTemp) == true)) {
                mainFrame.invHighCursor = true;
            } else {
                mainFrame.invHighCursor = false;
            }

            if ((Cursorform != 10) && (mainFrame.invHighCursor == false)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor == true)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }


        // normaler Cursor, normale Reaktion
        else {
            if (bilder.IsPointInRect(pTemp) == true) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
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
        if (mainFrame.invCursor == true) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim == true) {
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
            return;
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
        if ((mainFrame.krabat.isWandering == true) ||
                (mainFrame.krabat.isWalking == true)) {
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
            case 1:
                // Bilder anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Chodba_00000"),
                        Start.stringManager.getTranslation("Loc3_Chodba_00001"),
                        Start.stringManager.getTranslation("Loc3_Chodba_00002"),
                        fBilder, 3, 0, 0);
                break;

            case 50:
                // Bilder mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Chodba_00003"),
                        Start.stringManager.getTranslation("Loc3_Chodba_00004"),
                        Start.stringManager.getTranslation("Loc3_Chodba_00005"),
                        fBilder, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Dingl
                NeuesBild(141, locationID);
                break;

            case 101:
                // Gehe zu Wonka
                NeuesBild(146, locationID);
                break;

            case 150:
                // Bilder-Ausreden
                DingAusrede(fBilder);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}