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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CornyCholmc1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(CornyCholmc1.class);
    private GenericImage background;
    private GenericImage himmel;
    private GenericImage vorder;
    private final MultipleChoice Dialog;

    // Konstanten - Rects deklarieren
    private static final BorderRect obererAusgang = new BorderRect(123, 228, 187, 276);
    private static final BorderRect untererAusgang = new BorderRect(376, 451, 590, 479);
    private static final BorderRect waldRect = new BorderRect(0, 44, 424, 164);
    private static final BorderRect kolmcRect = new BorderRect(511, 125, 639, 241);

    // Konstante Points
    private static final GenericPoint Pup = new GenericPoint(161, 268);
    private static final GenericPoint Pdown = new GenericPoint(463, 479);
    private static final GenericPoint Pwald = new GenericPoint(174, 318);
    private static final GenericPoint waldTalk = new GenericPoint(320, 150);
    private static final GenericPoint Pkolmc = new GenericPoint(234, 372);

    // Konstante ints
    private static final int fWald = 12;
    private static final int fWjes = 3;

    // konstantes Rectangle fuer den Waldvordergrund
    private static final GenericRectangle vorderWaldRect = new GenericRectangle(0, 190, 44, 209);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public CornyCholmc1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 4.2f;
        mainFrame.krabat.defScale = 20;

        Dialog = new MultipleChoice(mainFrame);

        InitLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(437, 490, 472, 497, 466, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(211, 263, 437, 490, 389, 465));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(196, 222, 211, 263, 367, 388));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(169, 184, 196, 222, 325, 366));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(153, 163, 169, 184, 276, 324));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);

        InitImages();
        if (oldLocation > 50 && oldLocation < 62) {
            // von Bludnickis teleportiert
            BackgroundMusicPlayer.getInstance().playTrack(26, true);
            mainFrame.krabat.setPos(new GenericPoint(161, 276));
            mainFrame.krabat.SetFacing(6);
        } else {
            switch (oldLocation) {
                case 0:
                    // Einsprung fuer Load
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                    break;
                case 16:
                    // von Villa aus
                    if (mainFrame.enteringFromMap) {
                        mainFrame.enteringFromMap = false;
                        BackgroundMusicPlayer.getInstance().playTrack(26, true);
                    }
                    mainFrame.krabat.setPos(new GenericPoint(463, 468));
                    mainFrame.krabat.SetFacing(12);
                    break;
                case 26:
                    // von Mlynkkolmc aus, Position nicht veraendern...
			/*mainFrame.krabat.SetKrabatPos (new GenericPoint (213, 376));
			  mainFrame.krabat.SetFacing (12);*/
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                    break;
                case 62:
                    // von Labyrinth aus
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                    mainFrame.krabat.setPos(new GenericPoint(161, 286));
                    mainFrame.krabat.SetFacing(6);
                    break;
            }
        }
        mainFrame.enteringFromMap = false;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/kolmc/kolmc2.png");
        himmel = getPicture("gfx/kolmc/kcsky1.png");
        vorder = getPicture("gfx/kolmc/kwald.png");

    }

    @Override
    public void cleanup() {
        background = null;
        himmel = null;
        vorder = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        if (mainFrame.isMultipleChoiceActive && mainFrame.isClipSet) {
            Dialog.paintMultiple(g);
            return;
        }

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
        g.drawImage(himmel, 0, 0);
        g.drawImage(background, 0, 0);
        g.setClip(vorderWaldRect);
        g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
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
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseEvent(e);
            return;
        }

        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
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

                // Ausreden fuer Wald
                if (waldRect.IsPointInRect(pTemp)) {
                    // Kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 200 : 150;
                    pTemp = Pwald;
                }

                // Ausreden fuer Kolmc
                if (kolmcRect.IsPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 155;
                    pTemp = Pkolmc;
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

                // zu Villa gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Labyrinth gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Wald ansehen
                if (waldRect.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pwald;
                }

                // Kolmc ansehen
                if (kolmcRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pkolmc;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Hojnt Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Villa anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Wald reden
                if (waldRect.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwald);
                    mainFrame.repaint();
                    return;
                }

                // Kolmc mitnehmen
                if (kolmcRect.IsPointInRect(pTemp)) {
                    nextActionID = 85;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pkolmc);
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
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || waldRect.IsPointInRect(pTemp) ||
                    kolmcRect.IsPointInRect(pTemp);

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
            if (waldRect.IsPointInRect(pTemp) || kolmcRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 5;
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

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseExitEvent();
        }
    }


    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultipleChoiceActive) {
            return;
        }

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

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
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
                // Wald anschauen
                KrabatSagt("CornyCholmc1_1", fWald, 3, 0, 0);
                break;

            case 2:
                // Kolmc anschauen
                if (!mainFrame.actions[225]) {
                    KrabatSagt("CornyCholmc1_2", fWjes, 3, 0, 0);
                } else {
                    KrabatSagt("CornyCholmc1_3", fWjes, 3, 0, 0);
                }
                break;

            case 50:
                // Wald benutzen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.SetFacing(fWald);
                // 3 Moeglichkeiten: 1. Echoanim
                //                   2. Sprueche rufen ohne den richtigen
                //                   3. Sprueche rufen mit dem richtigen
                // 1. Variante: Sequenz
                if (!mainFrame.actions[225]) {
                    mainFrame.actions[225] = true;
                    nextActionID = 60;
                } else {
                    if (!mainFrame.actions[215]) {
                        // Dialog, aber ohne den richtigen Spruch
                        nextActionID = 600;
                    } else {
                        // Dialog mit dem richtigen Spruch
                        nextActionID = 1000;
                    }
                }
                break;

            case 60:
                // Wald Anim
                KrabatSagt("CornyCholmc1_4", fWald, 1, 2, 65);
                break;

            case 65:
                // Wald Anim
                PersonSagt("CornyCholmc1_5", 0, 51, 2, 70, waldTalk);
                break;

            case 70:
                // Wald Anim
                KrabatSagt("CornyCholmc1_6", 0, 3, 2, 75);
                break;

            case 75:
                // Wald Anim
                KrabatSagt("CornyCholmc1_7", 0, 3, 2, 80);
                break;

            case 80:
                // Ende Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 85:
                // Kolmc mitnehmen
                KrabatSagt("CornyCholmc1_8", fWjes, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Villa
                NeuesBild(16, 17);
                break;

            case 101:
                // gehe zu Labyrinth
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                mainFrame.constructLocation(62, 12);
                mainFrame.destructLocation(17);
                mainFrame.repaint();
                break;

            case 150:
                // Wald - Ausreden
                DingAusrede(fWald);
                break;

            case 155:
                // Kolmc - Ausreden
                DingAusrede(fWjes);
                break;

            case 600:
                // Multiple - Choice - Routine, Mueller kommt aber nie
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("CornyCholmc1_13", 1000, 1000, null, 610);

                // 2. Frage
                Dialog.ExtendMC("CornyCholmc1_14", 1000, 1000, null, 620);

                // 3. Frage
                Dialog.ExtendMC("CornyCholmc1_15", 1000, 1000, null, 630);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
            case 1001:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 610:
                // Wald Antwort
                PersonSagt("CornyCholmc1_9", 0, 51, 2, 700, waldTalk);
                break;

            case 620:
                // Wald Antwort
                PersonSagt("CornyCholmc1_10", 0, 51, 2, 700, waldTalk);
                break;

            case 630:
                // Wald Antwort
                PersonSagt("CornyCholmc1_11", 0, 51, 2, 700, waldTalk);
                break;

            case 700:
                mainFrame.isAnimRunning = false;
                mainFrame.isClipSet = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 1000:
                // Multiple - Choice - Routine, Mueller kommt bei letztem Spruch
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("CornyCholmc1_16", 1000, 1000, null, 610);

                // 2. Frage
                Dialog.ExtendMC("CornyCholmc1_17", 1000, 1000, null, 620);

                // 3. Frage
                Dialog.ExtendMC("CornyCholmc1_18", 1000, 1000, null, 1010);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 1001;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 1010:
                // Wald Antwort
                PersonSagt("CornyCholmc1_12", 0, 51, 2, 0, waldTalk);
                // Test, ob Mueller auch kommen darf
                if (!mainFrame.actions[226] || mainFrame.actions[919]) {
                    nextActionID = 1020;
                } else {
                    nextActionID = 700;
                }
                break;

            case 1020:
                // Goto Mlynkkolmc
                NeuesBild(26, 17);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}