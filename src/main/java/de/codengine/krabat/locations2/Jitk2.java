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

package de.codengine.krabat.locations2;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Jitk2 extends Mainloc2 {
    private static final Logger log = LoggerFactory.getLogger(Jitk2.class);
    private GenericImage background/* , strauch */;

    // Punkte in Location
    private static final GenericPoint Pwagen = new GenericPoint(242, 455);
    private static final GenericPoint Peingang = new GenericPoint(366, 339);
    private static final GenericPoint Pleft = new GenericPoint(87, 138);
    private static final GenericPoint Pright = new GenericPoint(639, 360);
    private static final GenericPoint Pdown = new GenericPoint(261, 479);

    // Konstanten - Rects deklarieren
    private static final Borderrect rechterAusgang = new Borderrect(598, 327, 639, 412);
    private static final Borderrect linkerAusgang = new Borderrect(0, 67, 131, 151);
    private static final Borderrect untererAusgang = new Borderrect(172, 435, 364, 479);
    private static final Borderrect brEingang = new Borderrect(353, 250, 397, 334);
    private static final Borderrect brWagen = new Borderrect(0, 338, 142, 479);
    // private static final borderrect buschRect      = new borderrect (0, 0, 0, 0);

    // Konstante ints
    private static final int fWagen = 9;
    private static final int fEingang = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jitk2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 374;
        mainFrame.krabat.zoomf = 3.38f;
        mainFrame.krabat.defScale = 0;

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(276, 346, 213, 296, 399, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(276, 639, 276, 528, 375, 398));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(633, 639, 573, 639, 352, 374));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(271, 357, 368, 374));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(360, 374, 339, 367, 345, 356));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(218, 241, 271, 309, 328, 356));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(154, 176, 218, 241, 260, 327));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(91, 96, 154, 176, 220, 259));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(83, 90, 91, 96, 137, 219));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(9);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(1, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(3, 5);
        mainFrame.wegSucher.PosVerbinden(5, 6);
        mainFrame.wegSucher.PosVerbinden(6, 7);
        mainFrame.wegSucher.PosVerbinden(7, 8);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                if (mainFrame.Actions[300]) {
                    BackgroundMusicPlayer.getInstance().playTrack(20, true);
                } else {
                    BackgroundMusicPlayer.getInstance().stop();
                }
                break;
            case 71:
                // von Doma aus
                BackgroundMusicPlayer.getInstance().stop();
                mainFrame.krabat.SetKrabatPos(new GenericPoint(284, 453));
                mainFrame.krabat.SetFacing(12);
                break;
            case 82:
                // von Ralbicy aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(90, 154));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/jitk/jitk.gif");
        // strauch    = getPicture ("gfx/jitk/strauch.gif");

    }

    @Override
    public void cleanup() {
        background = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Krabat einen Schritt gehen lassen
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

        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // Krabat hinter Strauch ??
    /*if (buschRect.IsPointInRect (pKrTemp) == true)
    {	
      g.drawImage(strauch, 104, 266, null);
    }*/

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
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

                // Ausreden fuer Eingang
                if (brEingang.IsPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
                    pTemp = Peingang;
                    mainFrame.repaint();
                }

                // Ausreden fuer Wagen
                if (brWagen.IsPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 155;
                    pTemp = Pwagen;
                    mainFrame.repaint();
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

                // zu Doma gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Ralbicy gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        // Wegen nach oben gehen hier diese Variante
                        pTemp = new GenericPoint(kt.x, Pleft.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Les1 gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Tuer ansehen
                if (brEingang.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Peingang;
                }

                // Wagen ansehen
                if (brWagen.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pwagen;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Doma Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Ralbitz anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Les1 anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Tuer mitnehmen
                if (brEingang.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(Peingang);
                    mainFrame.repaint();
                    return;
                }

                // Wagen mitnehmen
                if (brWagen.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwagen);
                    mainFrame.repaint();
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
            mainFrame.invHighCursor = brEingang.IsPointInRect(pTemp) || brWagen.IsPointInRect(pTemp) ||
                    tmp.IsPointInRect(pTemp);

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
            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 2;
                }
                return;
            }

            if (brEingang.IsPointInRect(pTemp) ||
                    brWagen.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 5;
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
    public void evalMouseExitEvent() {
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

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
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
                // Eingang anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Jitk2_00000"),
                        Start.stringManager.getTranslation("Loc2_Jitk2_00001"),
                        Start.stringManager.getTranslation("Loc2_Jitk2_00002"),
                        fEingang, 3, 0, 0);
                break;

            case 2:
                // Wagen anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Jitk2_00003"), Start.stringManager.getTranslation("Loc2_Jitk2_00004"), Start.stringManager.getTranslation("Loc2_Jitk2_00005"),
                        fWagen, 3, 0, 0);
                break;

            case 50:
                // Wagen mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Jitk2_00006"),
                        Start.stringManager.getTranslation("Loc2_Jitk2_00007"),
                        Start.stringManager.getTranslation("Loc2_Jitk2_00008"),
                        fWagen, 3, 0, 0);
                break;

            case 51:
                // Tuer mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Jitk2_00009"), Start.stringManager.getTranslation("Loc2_Jitk2_00010"), Start.stringManager.getTranslation("Loc2_Jitk2_00011"),
                        fEingang, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Doma
                NeuesBild(71, 74);
                break;

            case 101:
                // gehe zu Les1
                NeuesBild(78, 74);
                break;

            case 102:
                // nach Ralbitz gehen
                NeuesBild(82, 74);
                break;

            case 150:
                // Eingang - Ausreden
                DingAusrede(fEingang);
                break;

            case 155:
                // Wagen - Ausreden
                DingAusrede(fWagen);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}