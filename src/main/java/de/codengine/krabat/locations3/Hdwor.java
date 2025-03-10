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
import de.codengine.krabat.anims.Boote;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Hdwor extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Hdwor.class);
    private GenericImage background;
    private GenericImage stOben;
    private GenericImage stUnten;

    private final Boote boot;

    // Konstanten - Rects
    private static final Borderrect ausgangStraza
            = new Borderrect(283, 172, 317, 209);
    private static final Borderrect ausgangTreppe
            = new Borderrect(65, 282, 101, 343);
    private static final Borderrect stObenRect
            = new Borderrect(286, 238, 407, 293);
    private static final Borderrect stUntenRect
            = new Borderrect(293, 370, 442, 402);

    // Konstante Points
    private static final GenericPoint pExitTreppe = new GenericPoint(75, 343);
    private static final GenericPoint pExitStraza = new GenericPoint(298, 211);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Hdwor(Start caller, int oldLocation) {
        super(caller, 130);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 6.0f;
        mainFrame.krabat.defScale = 35;

        boot = new Boote(mainFrame, 2);

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(284, 343, 284, 343, 211, 222));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(247, 385, 247, 435, 223, 289));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(330, 362, 330, 362, 290, 319));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(253, 463, 253, 492, 320, 362));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 492, 35, 520, 363, 392));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(70, 113, 70, 132, 347, 362));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(50, 290, 62, 310, 393, 475));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(342, 390, 365, 435, 393, 475));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(445, 520, 500, 576, 393, 475));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 69, 20, 69, 353, 362));

        mainFrame.wegSucher.ClearMatrix(10);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(4, 6);
        mainFrame.wegSucher.PosVerbinden(4, 7);
        mainFrame.wegSucher.PosVerbinden(4, 8);
        mainFrame.wegSucher.PosVerbinden(5, 9);
        mainFrame.wegSucher.PosVerbinden(4, 9);


        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 128: // von Straza aus
                mainFrame.krabat.setPos(new GenericPoint(298, 217));
                mainFrame.krabat.SetFacing(6);
                break;
            case 131: // von Treppe aus
                mainFrame.krabat.setPos(new GenericPoint(75, 348));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/hdwor/hdwor.gif");
        stOben = getPicture("gfx-dd/hdwor/st-oben.gif");
        stUnten = getPicture("gfx-dd/hdwor/st-unten.gif");

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
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Boot-Routine
        // Hintergrund loeschen
        Borderrect temp = boot.evalBootRect();
        g.setClip(temp.lo_point.x, temp.lo_point.y,
                temp.ru_point.x - temp.lo_point.x, temp.ru_point.y - temp.lo_point.y);
        g.drawImage(background, 0, 0);
        // Boot zeichnen
        boot.drawBoot(g);

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
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinter den Staeben ? (nur Clipping - Region wird neugezeichnet)
        if (stObenRect.IsPointInRect(pKrTemp)) {
            g.drawImage(stOben, 296, 245);
        }
        if (stUntenRect.IsPointInRect(pKrTemp)) {
            g.drawImage(stUnten, 315, 383);
        }

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

                Borderrect tmp = mainFrame.krabat.getRect();

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
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Trjepjena gehen ?
                if (ausgangTreppe.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangTreppe.IsPointInRect(kt)) {
                        pTemp = pExitTreppe;
                    } else {
                        pTemp = new GenericPoint(pExitTreppe.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Straza gehen ?
                if (ausgangStraza.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangStraza.IsPointInRect(kt)) {
                        pTemp = pExitStraza;
                    } else {
                        pTemp = new GenericPoint(pExitStraza.x, kt.y);
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
                if (ausgangStraza.IsPointInRect(pTemp) ||
                        ausgangTreppe.IsPointInRect(pTemp)) {
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp);

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
            //if ((kerzen.IsPointInRect (pTemp) == true) ||
            //    (schwerter.IsPointInRect (pTemp) == true))
            //{
            //    if (Cursorform != 1)
            //    {
            //          mainFrame.setCursor (mainFrame.Kreuz);
            //          Cursorform = 1;
            //    }
            //    return;
            //}

            if (ausgangStraza.IsPointInRect(pTemp) ||
                    ausgangTreppe.IsPointInRect(pTemp)) {
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
            case 100:
                // Gehe zu Treppe
                NeuesBild(131, locationID);
                break;

            case 101:
                // Gehe zu Wache
                NeuesBild(128, locationID);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}