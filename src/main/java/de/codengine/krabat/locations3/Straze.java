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
import de.codengine.krabat.anims.Straza1;
import de.codengine.krabat.anims.Straza2;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Straze extends Mainloc {
    private GenericImage background;
    private final Straza1 straza1;
    private final Straza2 straza2;

    private final Borderrect reStraza1;
    private final Borderrect reStraza2;

    private final GenericPoint talkPoint;  // links
    private final GenericPoint talkPoint2; // rechts

    private final GenericPoint pStraza1; // links
    private final GenericPoint pStraza2; // rechts

    private boolean straza1VersperrtWeg = false;
    private boolean straza2VersperrtWeg = false;

    // Konstanten - Rects
    private static final Borderrect ausgangHdwor
            = new Borderrect(233, 330, 340, 405);
    private static final Borderrect ausgangTerassa
            = new Borderrect(150, 450, 420, 479);
    private static final Borderrect wappen
            = new Borderrect(250, 78, 331, 172);

    // Konstante Points
    private static final GenericPoint pExitHdwor = new GenericPoint(285, 410);
    private static final GenericPoint pExitTerassa = new GenericPoint(285, 479);
    private static final GenericPoint pStrazy = new GenericPoint(290, 475);
    private static final GenericPoint pWappen = new GenericPoint(293, 475);

    private static final GenericPoint straza1Feet = new GenericPoint(196, 467);
    private static final GenericPoint straza2Feet = new GenericPoint(370, 467);

    // Konstante ints
    private static final int fStraze = 12;
    private static final int fWapon = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Straze(Start caller, int oldLocation) {
        super(caller, 128);
        mainFrame.Freeze(true);

        // Schmied raushauen, wenn Hammer genommen
        if (mainFrame.Actions[953]) {
            mainFrame.Actions[701] = true;
        }

        // Zum testen - Rausnehmen !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // mainFrame.Actions[511] = true;

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 470;
        mainFrame.krabat.zoomf = 1f;
        mainFrame.krabat.defScale = -30;

        straza1 = new Straza1(mainFrame);
        straza2 = new Straza2(mainFrame);

        // Inits fuer Straza1
        pStraza1 = new GenericPoint(straza1Feet.x - Straza1.Breite / 2, straza1Feet.y - Straza1.Hoehe);

        talkPoint = new GenericPoint(straza1Feet.x, pStraza1.y - 50);

        reStraza1 = new Borderrect(pStraza1.x, pStraza1.y, pStraza1.x + Straza1.Breite, pStraza1.y + Straza1.Hoehe);

        // Inits fuer Straza2
        pStraza2 = new GenericPoint(straza2Feet.x - Straza2.Breite / 2, straza2Feet.y - Straza2.Hoehe);

        talkPoint2 = new GenericPoint(straza2Feet.x, pStraza2.y - 50);

        reStraza2 = new Borderrect(pStraza2.x, pStraza2.y, pStraza2.x + Straza2.Breite, pStraza2.y + Straza2.Hoehe);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();

        // diese Rechtecke nur mit Dienstkleidung passierbar
        if (mainFrame.Actions[511]) {
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(160, 410, 80, 430, 470, 479));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(242, 420, 309, 469));

            mainFrame.wegSucher.ClearMatrix(2);
            mainFrame.wegSucher.PosVerbinden(0, 1);
        } else {
            // sonst nur dieses
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(160, 410, 80, 430, 470, 479));

            mainFrame.wegSucher.ClearMatrix(1);
        }

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 127:
                // von Terassa aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(285, 475));
                mainFrame.krabat.SetFacing(12);
                break;
            case 130:
                // von Hdwor aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(279, 445));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/straze/straze.gif");

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
        Debug.DrawRect(g, mainFrame.wegGeher.vBorders);

        // Wache 1 + 2 zeichnen
        g.setClip(pStraza1.x, pStraza1.y, Straza1.Breite, Straza1.Hoehe);
        g.drawImage(background, 0, 0);
        straza1.drawStraza1(g, TalkPerson, pStraza1, straza1VersperrtWeg);

        // hier Wache2 (rechts)
        g.setClip(pStraza2.x, pStraza2.y, Straza2.Breite, Straza2.Hoehe);
        g.drawImage(background, 0, 0);
        straza2.drawStraza2(g, TalkPerson, pStraza2, straza2VersperrtWeg);

        // Wache 2 entscheidet selber, wann "Stop" zurueckgenommen
        straza2VersperrtWeg = false;

        // Krabat einen Schritt laufen lassen
        mainFrame.wegGeher.GeheWeg();

        // Krabat zeichnen

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
            TalkPerson = 0;
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

                // Ausreden fuer Wache 1 und 2
                if (reStraza1.IsPointInRect(pTemp) ||
                        reStraza2.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pStrazy;
                }

                // Ausreden fuer Wappen
                if (wappen.IsPointInRect(pTemp)) {
                    // hlebija
                    nextActionID = mainFrame.whatItem == 42 ? 200 : 160;
                    pTemp = pWappen;
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

                // zu Terassa gehen ?
                if (ausgangTerassa.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangTerassa.IsPointInRect(kt)) {
                        pTemp = pExitTerassa;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitTerassa.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Hdwor gehen ?
                // nur in Dienstkleidung moeglich, sonst Dialog mit Wachen
                if (ausgangHdwor.IsPointInRect(pTemp)) {
                    if (mainFrame.Actions[511]) {
                        nextActionID = 101;
                        GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!ausgangHdwor.IsPointInRect(kt)) {
                            pTemp = pExitHdwor;
                        } else {
                            // es wird nach unten verlassen
                            pTemp = new GenericPoint(kt.x, pExitHdwor.y);
                        }

                        if (mainFrame.dClick) {
                            mainFrame.krabat.StopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    } else {
                        nextActionID = 50;
                        pTemp = pStrazy;
                    }
                }

                // Wache 1 oder 2 ansehen
                if (reStraza1.IsPointInRect(pTemp) ||
                        reStraza2.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pStrazy;
                }

                // Wappen ansehen
                if (wappen.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pWappen;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit Wachen reden
                if (reStraza1.IsPointInRect(pTemp) ||
                        reStraza2.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pStrazy);
                    mainFrame.repaint();
                    return;
                }

                // Wappen mitnehmen
                if (wappen.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWappen);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangHdwor.IsPointInRect(pTemp) ||
                        ausgangTerassa.IsPointInRect(pTemp)) {
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || reStraza1.IsPointInRect(pTemp) ||
                    reStraza2.IsPointInRect(pTemp) || wappen.IsPointInRect(pTemp);

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
            if (ausgangTerassa.IsPointInRect(pTemp)) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 6;
                }
                return;
            }

            if (ausgangHdwor.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (reStraza1.IsPointInRect(pTemp) ||
                    reStraza2.IsPointInRect(pTemp) ||
                    wappen.IsPointInRect(pTemp)) {
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
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 541) {
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
                // Wachen anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00000"),
                        Start.stringManager.getTranslation("Loc3_Straze_00001"),
                        Start.stringManager.getTranslation("Loc3_Straze_00002"),
                        fStraze, 3, 0, 0);
                break;

            case 2:
                // Wappen anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00003"),
                        Start.stringManager.getTranslation("Loc3_Straze_00004"),
                        Start.stringManager.getTranslation("Loc3_Straze_00005"),
                        fWapon, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt Dialog mit Wachen
                mainFrame.krabat.SetFacing(fStraze);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                // Je nachdem, ob Dienstkleidung, passelnd quasseln
                if (mainFrame.Actions[511]) {
                    nextActionID = 620;
                } else {
                    if (mainFrame.Actions[510]) {
                        nextActionID = 610;
                    } else {
                        nextActionID = 600;
                    }
                }
                break;

            case 60:
                // Wappen mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00006"),
                        Start.stringManager.getTranslation("Loc3_Straze_00007"),
                        Start.stringManager.getTranslation("Loc3_Straze_00008"),
                        fWapon, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Terassa
                NeuesBild(127, locationID);
                break;

            case 101:
                // Gehe zu Hdwor
                NeuesBild(130, locationID);
                break;

            case 155:
                // Straze - Ausreden
                MPersonAusrede(fStraze);
                break;

            case 160:
                // Wappen - Ausreden
                DingAusrede(fWapon);
                break;

            case 200:
                // hlebija auf Wappen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00009"),
                        Start.stringManager.getTranslation("Loc3_Straze_00010"),
                        Start.stringManager.getTranslation("Loc3_Straze_00011"),
                        fWapon, 3, 0, 0);
                break;

            case 541:
                // Krabat mit Bedienstetenkleidung benutzen, hier Extrawurst !!!
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00012"),
                        Start.stringManager.getTranslation("Loc3_Straze_00013"),
                        Start.stringManager.getTranslation("Loc3_Straze_00014"),
                        0, 3, 0, 0);
                break;

            // Dialog mit Wachen

            case 600:
                // Reaktion Wache 1
                straza1VersperrtWeg = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00015"), Start.stringManager.getTranslation("Loc3_Straze_00016"), Start.stringManager.getTranslation("Loc3_Straze_00017"),
                        0, 43, 2, 601, talkPoint);
                break;

            case 601:
                // Reaktion Wache 2
                straza2VersperrtWeg = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00018"),
                        Start.stringManager.getTranslation("Loc3_Straze_00019"),
                        Start.stringManager.getTranslation("Loc3_Straze_00020"),
                        0, 44, 2, 602, talkPoint2);
                break;

            case 602:
                // Reaktion Wache 1
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00021"), Start.stringManager.getTranslation("Loc3_Straze_00022"), Start.stringManager.getTranslation("Loc3_Straze_00023"),
                        0, 43, 2, 603, talkPoint);
                break;

            case 603:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00024"),
                        Start.stringManager.getTranslation("Loc3_Straze_00025"),
                        Start.stringManager.getTranslation("Loc3_Straze_00026"),
                        0, 1, 2, 604);
                break;

            case 604:
                // Reaktion Wache 2
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00027"),
                        Start.stringManager.getTranslation("Loc3_Straze_00028"),
                        Start.stringManager.getTranslation("Loc3_Straze_00029"),
                        0, 44, 2, 605, talkPoint2);
                break;

            case 605:
                // Reaktion Wache 1
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00030"), Start.stringManager.getTranslation("Loc3_Straze_00031"), Start.stringManager.getTranslation("Loc3_Straze_00032"),
                        0, 43, 2, 606, talkPoint);
                break;

            case 606:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Straze_00033"),
                        Start.stringManager.getTranslation("Loc3_Straze_00034"),
                        Start.stringManager.getTranslation("Loc3_Straze_00035"),
                        0, 1, 2, 800);
                mainFrame.Actions[510] = true;    // Gesprach nicht wiederholen
                break;

            // Kurzer Dialog
            case 610:
                // Reaktion Wache 1
                straza1VersperrtWeg = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00036"),
                        Start.stringManager.getTranslation("Loc3_Straze_00037"),
                        Start.stringManager.getTranslation("Loc3_Straze_00038"),
                        0, 43, 2, 611, talkPoint);
                break;

            case 611:
                // Reaktion Wache 2
                straza2VersperrtWeg = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00039"),
                        Start.stringManager.getTranslation("Loc3_Straze_00040"),
                        Start.stringManager.getTranslation("Loc3_Straze_00041"),
                        0, 44, 2, 800, talkPoint2);
                break;

            case 620:
                // Reaktion Wache 2
                PersonSagt(Start.stringManager.getTranslation("Loc3_Straze_00042"),
                        Start.stringManager.getTranslation("Loc3_Straze_00043"),
                        Start.stringManager.getTranslation("Loc3_Straze_00044"),
                        0, 44, 2, 800, talkPoint2);
                break;


            case 800:
                // Dialog beenden, wenn zuende gelabert...
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