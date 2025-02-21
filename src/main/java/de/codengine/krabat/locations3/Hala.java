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

public class Hala extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Hala.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage door;
    private boolean setScroll = false;
    private int scrollwert;

    // Konstanten - Rects
    private static final Borderrect linkerAusgang
            = new Borderrect(135, 120, 273, 399);
    private static final Borderrect zweiteTuer
            = new Borderrect(464, 184, 577, 399);
    private static final Borderrect dritteTuer
            = new Borderrect(820, 113, 963, 403);
    private static final Borderrect rechterAusgang
            = new Borderrect(1240, 111, 1279, 479);
    private static final Borderrect wobraz
            = new Borderrect(468, 13, 585, 150);

    // Konstante Points
    private static final GenericPoint pExitLinks = new GenericPoint(180, 403);
    private static final GenericPoint pExitKomedij = new GenericPoint(920, 407);
    private static final GenericPoint pExitRechts = new GenericPoint(1230, 474);
    private static final GenericPoint pZweiteTuer = new GenericPoint(523, 408);
    private static final GenericPoint pWobraz = new GenericPoint(520, 397);


    // Konstante ints
    private static final int fTueren = 12;
    private static final int fBild = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Hala(Start caller, int oldLocation) {
        super(caller, 123);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomf = 1f;
        mainFrame.krabat.defScale = -100;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(140, 810, 25, 810, 407, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(811, 1115, 811, 1235, 407, 479));

        mainFrame.wegSucher.ClearMatrix(2);

        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(16, true);
                break;
            case 122: // von Spaniska aus
                mainFrame.krabat.setPos(new GenericPoint(180, 415));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;
            case 124: // von Komedij aus
                mainFrame.krabat.setPos(new GenericPoint(920, 420));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 600;
                setScroll = true;
                break;
            case 125: // von Jewisco aus
                mainFrame.krabat.setPos(new GenericPoint(1220, 470));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx-dd/hala/hala-l.gif");
        backr = getPicture("gfx-dd/hala/hala-r.gif");
        door = getPicture("gfx-dd/hala/hala-r2.gif");

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
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // offene Tuer zeichnen, sobald da
        if (mainFrame.Actions[675]) {
            g.setClip(883, 112, 84, 292);
            g.drawImage(door, 883, 112);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

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
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

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
            g.setClip(0, 0, 1284, 484);
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
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Tuer, wenn noch anwaehlbar
                if (dritteTuer.IsPointInRect(pTemp) && !mainFrame.Actions[675]) {
                    nextActionID = 150;
                    pTemp = pExitKomedij;
                }

                // Ausreden fuer 2. Tuer
                if (zweiteTuer.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pZweiteTuer;
                }

                // Ausreden fuer Bild
                if (wobraz.IsPointInRect(pTemp)) {
                    // hlebija
                    nextActionID = mainFrame.whatItem == 42 ? 200 : 155;
                    pTemp = pWobraz;
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

                // zu Spaniska gehen ?
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = pExitLinks;
                    } else {
                        pTemp = new GenericPoint(pExitLinks.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Komedij gehen , wenn schon geoeffnet
                if (mainFrame.Actions[675]) {
                    if (dritteTuer.IsPointInRect(pTemp)) {
                        nextActionID = 101;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!dritteTuer.IsPointInRect(kt)) {
                            pTemp = pExitKomedij;
                        } else {
                            pTemp = new GenericPoint(pExitKomedij.x, kt.y);
                        }

                        if (mainFrame.dClick) {
                            mainFrame.krabat.StopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    }
                } else {
                    if (dritteTuer.IsPointInRect(pTemp)) {
                        // Tuer ist noch nicht geoeffnet
                        nextActionID = 5;
                        pTemp = pExitKomedij;
                    }
                }

                // zu Jewisco gehen ?
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = pExitRechts;
                    } else {
                        pTemp = new GenericPoint(pExitRechts.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // 2. Tuer ansehen
                if (zweiteTuer.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pZweiteTuer;
                }

                // Bild ansehen
                if (wobraz.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pWobraz;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (linkerAusgang.IsPointInRect(pTemp) ||
                        rechterAusgang.IsPointInRect(pTemp) ||
                        dritteTuer.IsPointInRect(pTemp) && mainFrame.Actions[675]) {
                    return;
                }

                // verschlossene Tuer oeffnen (erfolglos)
                if (zweiteTuer.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    mainFrame.wegGeher.SetzeNeuenWeg(pZweiteTuer);
                    mainFrame.repaint();
                    return;
                }

                // offene Tuer oeffnen
                if (dritteTuer.IsPointInRect(pTemp) && !mainFrame.Actions[675]) {
                    nextActionID = 10;
                    mainFrame.wegGeher.SetzeNeuenWeg(pExitKomedij);
                    mainFrame.repaint();
                    return;
                }

                // Bild mitnehmen
                if (wobraz.IsPointInRect(pTemp)) {
                    nextActionID = 15;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWobraz);
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
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // neuen Punkt erzeugen wg. Scrolling
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    wobraz.IsPointInRect(pTemp) ||
                    dritteTuer.IsPointInRect(pTemp) && !mainFrame.Actions[675] ||
                    zweiteTuer.IsPointInRect(pTemp);

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
            if (wobraz.IsPointInRect(pTemp) ||
                    dritteTuer.IsPointInRect(pTemp) && !mainFrame.Actions[675] ||
                    zweiteTuer.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (dritteTuer.IsPointInRect(pTemp) && mainFrame.Actions[675]) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
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
                // 2. Tuer ansehen
                KrabatSagt("Hala_1", fTueren, 3, 0, 0);
                break;

            case 2:
                // Bild ansehen
                KrabatSagt("Hala_2", fBild, 3, 0, 0);
                break;

            case 5:
                // 3. Tuer  ansehen
                KrabatSagt("Hala_3", fTueren, 3, 0, 0);
                break;

            case 10:
                // Tuer aufmachen
                mainFrame.krabat.SetFacing(fTueren);
                mainFrame.Actions[675] = true;
                nextActionID = 0;
                mainFrame.wave.PlayFile("sfx/kdurjeauf.wav");
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 15:
                // Bild mitnehmen
                KrabatSagt("Hala_4", fBild, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Spaniska
                NeuesBild(122, locationID);
                break;

            case 101:
                // Gehe zu Komedij
                NeuesBild(124, locationID);
                break;

            case 102:
                // Gehe zu Jewisco
                NeuesBild(125, locationID);
                break;

            case 150:
                // durje-Ausreden
                DingAusrede(fTueren);
                break;

            case 155:
                // wobraz-Ausreden
                DingAusrede(fBild);
                break;

            case 160:
                // durje2-Ausreden
                DingAusrede(fTueren);
                break;

            case 200:
                // Hlebija auf bild
                KrabatSagt("Hala_5", fBild, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}