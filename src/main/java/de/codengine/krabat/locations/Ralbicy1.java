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
import de.codengine.krabat.anims.BurRalbicy;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

import java.util.Objects;

public class Ralbicy1 extends Mainloc {
    private GenericImage background;
    private GenericImage holz;
    private GenericImage kreuz;
    private BurRalbicy bauer;
    private final Multiple2 Dialog;
    private boolean isListening = false;

    // Konstanten - Rects
    private static final Borderrect rechterAusgang = new Borderrect(450, 423, 639, 479);
    private static final Borderrect linkerAusgang = new Borderrect(0, 290, 42, 361);
    private static final Borderrect brKirche = new Borderrect(175, 40, 639, 267);
    private static final Borderrect brBauer = new Borderrect(49, 221, 103, 250);

    // Punkte in Location
    private static final GenericPoint Pkirche = new GenericPoint(77, 316);
    private static final GenericPoint Pbauer = new GenericPoint(30, 303);
    private static final GenericPoint BurTalk = new GenericPoint(90, 200);
    private static final GenericPoint Pright = new GenericPoint(581, 479);
    private static final GenericPoint Pleft = new GenericPoint(0, 327);

    // Konstante ints
    private static final int fKirche = 3;
    private static final int fBauer = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Ralbicy1(Start caller, int oldLocation) {
        super(caller);

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 543;
        mainFrame.krabat.zoomf = 4.92f;
        mainFrame.krabat.defScale = -20;

        bauer = new BurRalbicy(mainFrame);
        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 16, 0, 221, 296, 362));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 223, 97, 223, 363, 420));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(224, 372, 300, 435));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(301, 390, 408, 454));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(409, 390, 639, 479));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 3:
                // von Jitk aus
                if (mainFrame.komme_von_karte) {
                    mainFrame.komme_von_karte = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.SetKrabatPos(new GenericPoint(581, 463));
                mainFrame.krabat.SetFacing(9);
                break;
            case 2:
                // von Most aus
                if (mainFrame.komme_von_karte) {
                    mainFrame.komme_von_karte = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.SetKrabatPos(new GenericPoint(20, 332));
                mainFrame.krabat.SetFacing(3);
                break;
        }
        mainFrame.komme_von_karte = false;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/ralbicy/ralbicy4.gif");
        holz = getPicture("gfx/ralbicy/holz2.gif");
        kreuz = getPicture("gfx/ralbicy/kreuz.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        holz = null;
        kreuz = null;

        bauer.cleanup();
        bauer = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
	/*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
	  {
	  Dialog.paintMultiple (g);
	  return;
	  } */

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
        g.drawImage(holz, 0, 365);
        g.drawImage(kreuz, 118, 194);


        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Bauern zeichnen
        g.setClip(60, 223, 35, 32);
        g.drawImage(background, 0, 0);
        bauer.drawBur(g, TalkPerson, isListening, false); // macht immer Geraeusche

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

        // muss noch mit Abfrage versehen werden (sonst unnuetz!)
        g.drawImage(holz, 0, 365);
        g.drawImage(kreuz, 118, 194);

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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // bei Multiple Choice extra Mouseroutine
        if (mainFrame.isMultiple) {
            Dialog.evalMouseEvent(e);
            return;
        }

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

                // Ausreden fuer Kirche
                if (brKirche.IsPointInRect(pTemp)) {
                    // nur Standard
                    nextActionID = 150;
                    pTemp = Pkirche;
                }

                // Ausreden fuer Bauer
                if (brBauer.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                        case 9: // wuda
                            nextActionID = 200;
                            break;
                        case 15: // krosik
                            nextActionID = 220;
                            break;
                        case 18: // bron
                            nextActionID = 230;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Pbauer;
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

                // zu Jitk gehen ?
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, Pright.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Most gehen?
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

                // Kirche ansehen
                if (brKirche.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pkirche;
                }

                // Bauer ansehen
                if (brBauer.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pbauer;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Jitk anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Ausgang zu Most abfangen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Kirche mitnehmen
                if (brKirche.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pkirche);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Bauern reden
                if (brBauer.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pbauer);
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
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

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
            mainFrame.invHighCursor = brKirche.IsPointInRect(pTemp) ||
                    tmp.IsPointInRect(pTemp) ||
                    brBauer.IsPointInRect(pTemp);

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
            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 3;
                }
                return;
            }

            if (brKirche.IsPointInRect(pTemp) || brBauer.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 2;
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
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultiple) {
            return;
        }

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
                // Kirche anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00000"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00001"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00002"),
                        fKirche, 3, 0, 0);
                break;

            case 2:
                // Bauer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00003"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00004"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00005"),
                        fBauer, 3, 0, 0);
                break;

            case 50:
                // Kirche mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00006"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00007"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00008"),
                        fKirche, 3, 0, 0);
                break;

            case 51:
                // Krabat beginnt MC (Bauer benutzen)
                mainFrame.krabat.SetFacing(3);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                isListening = true;  // Bauer hoert auf zu arbeiten
                nextActionID = 600;
                break;

            case 100:
                // Gehe zu Jitk
                NeuesBild(3, 1);
                break;

            case 101:
                // nach Most gehen
                NeuesBild(2, 1);
                break;

            case 150:
                // Kirche - Ausreden
                DingAusrede(fKirche);
                break;

            case 155:
                // Bauer - Ausreden
                MPersonAusrede(fBauer);
                break;

            case 200:
                // Stock oder Angel auf Bauern
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00009"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00010"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00011"),
                        fBauer, 3, 0, 0);
                break;

            case 220:
                // Krosik auf Bauern
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00012"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00013"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00014"),
                        fBauer, 3, 0, 0);
                break;

            case 230:
                // Bron auf Bauern
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00015"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00016"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00017"),
                        fBauer, 3, 0, 0);
                break;

            // Dialog mit Bauer

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00018"), 1000, 22, new int[]{22}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00019"), 22, 21, new int[]{21}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00020"), 21, 20, new int[]{20}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00021"), 20, 1000, null, 610);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00022"), 1000, 23, new int[]{23}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00023"), 23, 24, new int[]{24}, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00024"), 1000, 1000, null, 670);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00025"), 25, 1000, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00026"), 1000, 25, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00027"), 1000, 22, new int[]{22}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00028"), 22, 21, new int[]{21}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00029"), 21, 20, new int[]{20}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00030"), 20, 1000, null, 610);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00031"), 1000, 23, new int[]{23}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00032"), 23, 24, new int[]{24}, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00033"), 1000, 1000, null, 670);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00034"), 25, 1000, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00035"), 1000, 25, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00036"), 1000, 22, new int[]{22}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00037"), 22, 21, new int[]{21}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00038"), 21, 20, new int[]{20}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00039"), 20, 1000, null, 610);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00040"), 1000, 23, new int[]{23}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00041"), 23, 24, new int[]{24}, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00042"), 1000, 1000, null, 670);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00043"), 25, 1000, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Ralbicy1_00044"), 1000, 25, null, 800);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[25] = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;
                nextActionID = Dialog.ActionID;
                break;

            case 610:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00045"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00046"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00047"),
                        0, 21, 2, 611, BurTalk);
                break;

            case 611:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00048"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00049"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00050"),
                        0, 21, 2, 612, BurTalk);
                break;

            case 612:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00051"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00052"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00053"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 620:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00054"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00055"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00056"),
                        0, 21, 2, 621, BurTalk);
                break;

            case 621:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00057"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00058"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00059"),
                        0, 21, 2, 622, BurTalk);
                break;

            case 622:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00060"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00061"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00062"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 630:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00063"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00064"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00065"),
                        0, 21, 2, 631, BurTalk);
                break;

            case 631:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00066"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00067"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00068"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 640:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00069"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00070"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00071"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 650:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00072"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00073"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00074"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 660:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00075"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00076"), Start.stringManager.getTranslation("Loc1_Ralbicy1_00077"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 670:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00078"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00079"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00080"),
                        0, 21, 2, 671, BurTalk);
                break;

            case 671:
                // Reaktion Bauer
                PersonSagt(Start.stringManager.getTranslation("Loc1_Ralbicy1_00081"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00082"),
                        Start.stringManager.getTranslation("Loc1_Ralbicy1_00083"),
                        0, 21, 2, 600, BurTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.Actions[25] = false; // wieder nico dale beim 1. MC
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                isListening = false; // Bauer hoert wieder auf zuzuhoeren
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}