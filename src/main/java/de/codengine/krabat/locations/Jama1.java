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

package de.codengine.krabat.locations;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Hojnt;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Jama1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Jama1.class);
    private GenericImage background;
    private GenericImage foreground;
    private GenericImage vorder;
    private int Animcount = 1;
    private static final int MAXCOUNT = 5;
    private int Counter = 0;
    private final GenericImage[] Wuermer;

    private Hojnt jaeger;
    private boolean showHojnt = false;
    private boolean walkReady = true;

    private static final GenericPoint AnfangsPunkt = new GenericPoint(50, 179);
    private static final GenericPoint EndPunkt = new GenericPoint(236, 179);

    // Konstanten - Rects
    private static final Borderrect wackiRect = new Borderrect(253, 350, 253 + 14, 350 + 13);

    // Konstante ints
    private static final int fWacki = 9;

    private int TakeCounter = 0;

    private boolean istJaegerGebueckt = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jama1(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 492;
        mainFrame.krabat.zoomf = 2.1f;
        mainFrame.krabat.defScale = -90;

        jaeger = new Hojnt(mainFrame);
        jaeger.maxx = 0;
        jaeger.zoomf = 1f;
        jaeger.defScale = -30;

        jaeger.setPos(AnfangsPunkt);
        jaeger.SetFacing(3);

        Wuermer = new GenericImage[8];

        InitLocation();
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(310, 390, 330, 396));

        mainFrame.wegSucher.ClearMatrix(1);

        InitImages();

        mainFrame.krabat.setPos(new GenericPoint(317, 393));
        mainFrame.krabat.SetFacing(12);
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/jama/dzera2.gif");
        foreground = getPicture("gfx/jama/black.gif");

        Wuermer[1] = getPicture("gfx/jama/wr1.gif");
        Wuermer[2] = getPicture("gfx/jama/wr2.gif");
        Wuermer[3] = getPicture("gfx/jama/wr3.gif");
        Wuermer[4] = getPicture("gfx/jama/wr4.gif");
        Wuermer[5] = getPicture("gfx/jama/wr5.gif");
        Wuermer[6] = getPicture("gfx/jama/wr6.gif");
        Wuermer[7] = getPicture("gfx/jama/wr7.gif");

        vorder = getPicture("gfx/jama/jtrawa.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        foreground = null;

        Wuermer[1] = null;
        Wuermer[2] = null;
        Wuermer[3] = null;
        Wuermer[4] = null;
        Wuermer[5] = null;
        Wuermer[6] = null;
        Wuermer[7] = null;

        vorder = null;

        jaeger.cleanup();
        jaeger = null;
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


        // Jaeger Hintergrund loeschen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = jaeger.getRect();

            if (!istJaegerGebueckt) {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 20,
                        temp.ru_point.y - temp.lo_point.y + 20);
            }

            // groesseres Cliprect weg wegen Buecken
            else {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 50,
                        temp.ru_point.y - temp.lo_point.y + 50);
            }

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
        }

        // Wacki zeichnen, solange noch da
        if (!mainFrame.Actions[908]) {
            Counter--;
            if (Counter < 1) {
                Counter = MAXCOUNT;
                Animcount++;
                if (Animcount == 8) {
                    Animcount = 1;
                }
            }
            g.setClip(wackiRect.lo_point.x, wackiRect.lo_point.y, 15, 14);
            g.drawImage(background, 0, 0);
            g.drawImage(Wuermer[Animcount], wackiRect.lo_point.x, wackiRect.lo_point.y);
        }

        // Jaeger bewegen
        if (showHojnt && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = jaeger.Move();
        }

        // Jaeger zeichnen
        if (showHojnt) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = jaeger.getRect();

            // normales Cliprectloeschen
            if (!istJaegerGebueckt) {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 20,
                        temp.ru_point.y - temp.lo_point.y + 20);
            }

            // groesseres Cliprect weg wegen Buecken
            else {
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 50,
                        temp.ru_point.y - temp.lo_point.y + 50);
            }

            // Zeichne sie jetzt

            // Redet sie etwa gerade ??
            if (TalkPerson == 26 && mainFrame.talkCount > 0) {
                jaeger.talkHojnt(g);
            }

            // nur rumstehen oder laufen
            else {
                // normal zeichnen
                if (!istJaegerGebueckt) {
                    jaeger.drawHojnt(g);
                }

                // Bueckphase zeichnen (schaltet sich selbst ab) (ist hier wurst)
                else {
                    istJaegerGebueckt = jaeger.bueckeHojnt(g);
                }
            }

            g.drawImage(foreground, 0, 71);
            g.drawImage(vorder, 151, 158);
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

                // Ausreden fuer Wacki
                if (wackiRect.IsPointInRect(pTemp) && !mainFrame.Actions[908]) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
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

                // Wacki ansehen
                if (wackiRect.IsPointInRect(pTemp) && !mainFrame.Actions[908]) {
                    nextActionID = 1;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wacki mitnehmen ?
                if (wackiRect.IsPointInRect(pTemp) &&
                        !mainFrame.Actions[908]) {
                    nextActionID = 50;
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    wackiRect.IsPointInRect(pTemp) && !mainFrame.Actions[908];

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
            if (wackiRect.IsPointInRect(pTemp) &&
                    !mainFrame.Actions[908]) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
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
        Borderrect tmp;
        GenericPoint tTlk;

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
                // Wuermer anschauen
                KrabatSagt("Jama1_1", fWacki, 3, 0, 0);
                break;

            case 50:
                // Wuermer mitnehmen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt("Jama1_2", fWacki, 3, 0, 52);
                break;

            case 52:
                // Wuermer mitnehmen
                // Wuermer dem Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(8);
                mainFrame.krabat.nAnimation = 92;
                nextActionID = 53;
                TakeCounter = 5;
                break;

            case 53:
                // Laufe zur Falle
                if (--TakeCounter < 2) {
                    mainFrame.Actions[908] = true;
                    mainFrame.Clipset = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || TakeCounter > 0) {
                    break;
                }
                showHojnt = true;
                jaeger.MoveTo(EndPunkt);
                walkReady = false;
                nextActionID = 54;
                break;

            case 54:
                // Warten, bis er ausgelaufen ist
                if (walkReady) {
                    nextActionID = 55;
                }
                break;

            case 55:
                // Jaeger kommt zur Hilfe
                // Hier Position des Textes berechnen
                tmp = jaeger.getRect();
                tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.ru_point.y + 30);
                PersonSagt("Jama1_3", 0, 26, 2, 60, tTlk);
                break;


            case 60:
                // K spricht
                KrabatSagt("Jama1_4", 0, 1, 2, 65);
                break;

            case 65:
                // Jaeger kommt zur Hilfe
                // Hier Position des Textes berechnen
                tmp = jaeger.getRect();
                tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.ru_point.y + 30);
                PersonSagt("Jama1_5", 0, 26, 2, 70, tTlk);
                break;


            case 70:
                // K spricht
                KrabatSagt("Jama1_6", 0, 1, 2, 75);
                break;

            case 75:
                // Jaeger kommt zur Hilfe
                // Hier Position des Textes berechnen
                tmp = jaeger.getRect();
                tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.ru_point.y + 30);
                PersonSagt("Jama1_7", 0, 26, 2, 80, tTlk);
                break;

            case 80:
                // Jaeger buecken lassen und Krabat reicht Hand, das ca. 5 frames, dann umschalten
                istJaegerGebueckt = true;
                mainFrame.krabat.nAnimation = 90;
                TakeCounter = 5;
                nextActionID = 100;
                break;

            case 100:
                // Gehe zurueck zu Hojnt
                if (--TakeCounter > 1) {
                    break;
                }
                NeuesBild(14, 27);
                break;

            case 150:
                // Wacki - Ausreden
                DingAusrede(fWacki);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}