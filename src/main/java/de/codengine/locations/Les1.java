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

package de.codengine.locations;

import de.codengine.Start;
import de.codengine.anims.Reh;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Les1 extends Mainloc {
    private GenericImage background, strauch;

    private Reh reh;

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(453, 111, 535, 215);
    private static final Borderrect linkerAusgang = new Borderrect(0, 306, 39, 417);
    private static final Borderrect strauchRect = new Borderrect(0, 380, 255, 419);

    // Konstante Points
    private static final GenericPoint Pup = new GenericPoint(500, 180);
    private static final GenericPoint Pleft = new GenericPoint(0, 370);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Les1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 340;
        mainFrame.krabat.zoomf = 2.45f;
        mainFrame.krabat.defScale = 0;

        reh = new Reh(mainFrame, true, new GenericRectangle(22, 159, 50, 19), 3);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(484, 490, 449, 485, 205, 233));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(449, 485, 231, 339, 234, 338));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 339, 167, 413));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(168, 339, 168, 175, 339, 400));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(4);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 3);
        mainFrame.wegSucher.PosVerbinden(3, 2);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung von Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 3:
                // von Jitk aus kommend
                mainFrame.krabat.SetKrabatPos(new GenericPoint(21, 370));
                mainFrame.krabat.SetFacing(3);
                break;
            case 4:
                // von Haty aus kommend
                mainFrame.krabat.SetKrabatPos(new GenericPoint(487, 212));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/les1/wald.gif");
        strauch = getPicture("gfx/les1/grass2.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        strauch = null;

        reh.cleanup();
        reh = null;
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

        // Hintergrund fuer Rehe loeschen
        g.setClip(0, 135, 100, 45);
        g.drawImage(background, 0, 0, null);

        // Rehe zeichnen
        reh.drawReh(g);

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
                        // Krabat steht nur da
                        mainFrame.krabat.drawKrabat(g);
                        break;
                }
            }
            // Rumstehen oder Laufen
            else {
                mainFrame.krabat.drawKrabat(g);
            }
        }

        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // Krabat hinterm Gras ??
        if (strauchRect.IsPointInRect(pKrTemp)) {
            g.drawImage(strauch, 0, 381, null);
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
            if (e.isRightClick()) {
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
            if (e.isRightClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Haty gehen ?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Jitk gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
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

                // Haty Anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Jitk anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
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
            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 2;
                }
                return;
            }

      /*
      if (false)
      {
        if (Cursorform != 1)
        {
          mainFrame.setCursor (mainFrame.Kreuz);
          Cursorform = 1;
        }
        return;
      }
      */

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
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

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
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
                // Gehe zu Haty
                NeuesBild(4, 5);
                break;

            case 101:
                // Gehe zu Jitk
                NeuesBild(3, 5);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}