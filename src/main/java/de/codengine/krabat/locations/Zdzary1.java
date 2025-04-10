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
import de.codengine.krabat.anims.OldWoman;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Zdzary1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Zdzary1.class);
    private GenericImage background;
    private OldWoman alte;
    private final MultipleChoice Dialog;
    private final UserMultipleChoice Userdialog;

    private int whereIsAlte = 0;

    // Punkte in Location
    private static final GenericPoint Pdown = new GenericPoint(258, 479);
    private static final GenericPoint Puser = new GenericPoint(222, 474);
    private static final GenericPoint Pup = new GenericPoint(46, 288);
    private static final GenericPoint Pdurje = new GenericPoint(309, 431);

    // Konstanten - Rects deklarieren
    private static final BorderRect untererAusgang = new BorderRect(102, 454, 366, 479);
    private static final BorderRect obererAusgang = new BorderRect(34, 233, 61, 288);
    private static final BorderRect durjeRect = new BorderRect(277, 309, 315, 400);

    // Deklarationen fuer die Alte, da sie sonstwo sein kann
    private GenericPoint Palte;
    private GenericPoint altePoint;
    private BorderRect alteRect;
    private GenericPoint alteTalk;
    private int alteFacing;

    // Konstante ints
    private static final int fDurje = 12;
    private static final int fExitUp = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zdzary1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.enteringFromMap = false; // hier ohne Bedeutung
        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 428;
        mainFrame.krabat.zoomf = 1.73f;
        mainFrame.krabat.defScale = -30;

        alte = new OldWoman(mainFrame);
        DefineAlte();

        Dialog = new MultipleChoice(mainFrame);
        Userdialog = new UserMultipleChoice(mainFrame);

        InitLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(77, 609, 126, 609, 454, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(478, 432, 609, 453));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(25, 344, 25, 393, 433, 453));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(17, 36, 63, 188, 380, 432));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(17, 315, 19, 379));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(33, 50, 17, 19, 290, 314));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(6);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(0, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);
        mainFrame.pathFinder.PosVerbinden(4, 5);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 99:
                // von oben aus (gibts nicht)
                mainFrame.krabat.setPos(new GenericPoint(29, 299));
                mainFrame.krabat.SetFacing(6);
                break;
            case 8:
                // von Rapak aus
                mainFrame.krabat.setPos(new GenericPoint(270, 467));
                mainFrame.krabat.SetFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/zdzary/zdzary.png");

    }

    private void DefineAlte() {
        altePoint = new GenericPoint();
        alteTalk = new GenericPoint();

        int zuffi = (int) (Math.random() * 2.9);
        if (!mainFrame.actions[94]) {
            mainFrame.actions[94] = true;
            zuffi = 2;
        }
        switch (zuffi) {
            case 0:
                // hinteres Fenster
                Palte = new GenericPoint(152, 435);
                altePoint.x = 184;
                altePoint.y = 306;
                alteRect = new BorderRect(altePoint.x, altePoint.y, altePoint.x + OldWoman.Breite, altePoint.y + 35);
                alteFacing = 3;
                break;
            case 1:
                // vorderes Fenster
                Palte = new GenericPoint(584, 446);
                altePoint.x = 526;
                altePoint.y = 225;
                alteRect = new BorderRect(altePoint.x, altePoint.y, altePoint.x + OldWoman.Breite, altePoint.y + 35);
                alteFacing = 12;
                break;
            case 2:
                // vor der Tuer
                Palte = new GenericPoint(300, 452);
                altePoint.x = 295;
                altePoint.y = 323;
                alteRect = new BorderRect(altePoint.x, altePoint.y, altePoint.x + OldWoman.Breite, altePoint.y + OldWoman.Hoehe);
                alteFacing = 12;
                break;
        }

        whereIsAlte = zuffi;

        alteTalk.x = altePoint.x + OldWoman.Breite / 2;
        alteTalk.y = altePoint.y - 50;
    }

    @Override
    public void cleanup() {
        background = null;
        alte.cleanup();
        alte = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei UserMultiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        if (Userdialog.user && mainFrame.isClipSet) {
            Userdialog.paintMultiple(g);
            return;
        }

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
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Alte Schachtel zeichnen
        g.setClip(alteRect.lo_point.x, alteRect.lo_point.y, alteRect.ru_point.x - alteRect.lo_point.x, alteRect.ru_point.y - alteRect.lo_point.y);
        g.drawImage(background, 0, 0);
        alte.drawWudowa(g, TalkPerson, altePoint, whereIsAlte == 0);

        mainFrame.pathWalker.GeheWeg();

        // Krabat zeichnen

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

        // Usermultiple Choice ausfuehren
        if (Userdialog.user) {
            mainFrame.isClipSet = false;
            Userdialog.paintMultiple(g);
            return;
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
        // bei Usermultiple Choice extra Mouseroutine
        if (Userdialog.user) {
            Userdialog.evalMouseEvent(e);
            return;
        }

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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Wudowa
                if (alteRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 200;
                            break;
                        case 18: // bron
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 150;
                    }
                    pTxxx = Palte;
                }

                // Ausreden fuer Durje
                if (durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2) {
                    // Standard - Sinnloszeug
                    nextActionID = 155;
                    pTxxx = Pdurje;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Rapak gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTxxx = Pdown;
                    } else {
                        pTxxx = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach oben raus
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTxxx = Pup;
                    } else {
                        pTxxx = new GenericPoint(kt.x, Pup.y);
                    }

                    // Doppelclick ist hier unpassend
				/*if (mainFrame.dClick == true)
				  {
				  mainFrame.krabat.StopWalking();
				  mainFrame.repaint();
				  return;
				  } */
                }

                // Wudowa ansehen
                if (alteRect.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTxxx = Palte;
                }

                // Durje ansehen
                if (durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2) {
                    nextActionID = 2;
                    pTxxx = Pdurje;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Rapak Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // oberen Ausgang Anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Mit der Wudowa reden
                if (alteRect.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Palte);
                    mainFrame.repaint();
                    return;
                }

                // Durje mitnehmen
                if (durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pdurje);
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
        // bei Usermultiple Choice eigene Routine aufrufen
        if (Userdialog.user) {
            Userdialog.evalMouseMoveEvent(pTemp);
            return;
        }

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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || alteRect.IsPointInRect(pTemp) ||
                    durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2;

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
            if (alteRect.IsPointInRect(pTemp) ||
                    durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
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

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
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
        if (Userdialog.user) {
            Userdialog.evalMouseExitEvent();
        }
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Usermultiple Choice eigene Keyroutine
        if (Userdialog.user) {
            return;
        }

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
                // Wudowa anschauen
                if (whereIsAlte == 2) {
                    KrabatSagt("Zdzary1_1", alteFacing, 3, 0, 0);
                } else {
                    KrabatSagt("Zdzary1_2", alteFacing, 3, 0, 0);
                }
                break;

            case 2:
                // Durje anschauen
                KrabatSagt("Zdzary1_3", fDurje, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Alte benutzen)
                mainFrame.krabat.SetFacing(alteFacing);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 55:
                // Durje mitnehmen
                KrabatSagt("Zdzary1_4", fDurje, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Rapak
                NeuesBild(8, 19);
                break;

            case 101:
                // Gehe nach hinten
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                int zfz = (int) (Math.random() * 100);
                if (zfz > 50) {
                    mainFrame.soundPlayer.PlayFile("sfx/pos.wav");
                } else {
                    mainFrame.soundPlayer.PlayFile("sfx/pos2.wav");
                }
                nextActionID = 105;
                break;

            case 105:
                // Krabat hat Angst vor dem boesen Hund
                KrabatSagt("Zdzary1_5", fExitUp, 1, 2, 110);
                break;

            case 110:
                // wieder ende
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 150:
                // Alte - Ausreden
                WPersonAusrede(alteFacing);
                break;

            case 155:
                // Tuer - Ausreden
                DingAusrede(fDurje);
                break;

            case 200:
                // kij auf wudowa
                KrabatSagt("Zdzary1_6", alteFacing, 3, 0, 0);
                break;

            case 210:
                // bron auf wudowa
                KrabatSagt("Zdzary1_7", alteFacing, 3, 0, 0);
                break;


            // Krabat redet mit der Alten

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Zdzary1_74", 1000, 80, new int[]{80}, 610);
                Dialog.ExtendMC("Zdzary1_75", 80, 81, new int[]{81}, 620);
                Dialog.ExtendMC("Zdzary1_76", 81, 82, new int[]{82, 87, 92}, 630);

                // 2. Frage
                Dialog.ExtendMC("Zdzary1_77", 1000, 83, new int[]{83}, 710);
                Dialog.ExtendMC("Zdzary1_78", 83, 84, new int[]{84}, 720);
                Dialog.ExtendMC("Zdzary1_79", 84, 85, new int[]{85}, 730);
                Dialog.ExtendMC("Zdzary1_80", 85, 86, new int[]{86}, 740);

                // 4. Frage ( 3. = Ende )
                Dialog.ExtendMC("Zdzary1_81", 87, 88, new int[]{88}, 640);
                Dialog.ExtendMC("Zdzary1_82", 88, 89, new int[]{89}, 650);
                Dialog.ExtendMC("Zdzary1_83", 89, 90, new int[]{90}, 660);
                Dialog.ExtendMC("Zdzary1_84", 90, 91, new int[]{91}, 670);
                Dialog.ExtendMC("Zdzary1_85", 91, 1000, null, 680);

                // 5. Frage
                Dialog.ExtendMC("Zdzary1_86", 92, 93, new int[]{93}, 690);
                Dialog.ExtendMC("Zdzary1_87", 93, 1000, null, 700);

                // 3. Frage (Ende)
                Dialog.ExtendMC("Zdzary1_88", 1000, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
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
                // Reaktion Alte auf 1. Frage 1. Teil
                PersonSagt("Zdzary1_8", 0, 56, 2, 611, alteTalk);
                break;

            case 611:
                // Reaktion Alte auf 1. Teil 1. Frage
                PersonSagt("Zdzary1_9", 0, 57, 2, 612, alteTalk);
                break;

            case 612:
                // Reaktion Alte auf 1. Teil 1. Frage
                PersonSagt("Zdzary1_10", 0, 56, 2, 613, alteTalk);
                break;

            case 613:
                // Reaktion Alte auf 1. Teil 1. Frage
                PersonSagt("Zdzary1_11", 0, 57, 2, 600, alteTalk);
                break;

            case 620:
                // Reaktion Alte auf 2. Teil 1. Frage
                PersonSagt("Zdzary1_12", 0, 56, 2, 621, alteTalk);
                break;

            case 621:
                // Reaktion Alte auf 2. Teil 1. Frage
                PersonSagt("Zdzary1_13", 0, 57, 2, 600, alteTalk);
                break;

            case 630:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt("Zdzary1_14", 0, 57, 2, 631, alteTalk);
                break;

            case 631:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt("Zdzary1_15", 0, 56, 2, 632, alteTalk);
                break;

            case 632:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt("Zdzary1_16", 0, 57, 2, 633, alteTalk);
                break;

            case 633:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt("Zdzary1_17", 0, 56, 2, 600, alteTalk);
                break;

            case 640:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt("Zdzary1_18", 0, 57, 2, 641, alteTalk);
                break;

            case 641:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt("Zdzary1_19", 0, 56, 2, 642, alteTalk);
                break;

            case 642:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt("Zdzary1_20", 0, 57, 2, 643, alteTalk);
                break;

            case 643:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt("Zdzary1_21", 0, 56, 2, 644, alteTalk);
                break;

            case 644:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt("Zdzary1_22", 0, 57, 2, 645, alteTalk);
                break;

            case 645:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt("Zdzary1_23", 0, 56, 2, 600, alteTalk);
                break;

            case 650:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt("Zdzary1_24", 0, 56, 2, 651, alteTalk);
                break;

            case 651:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt("Zdzary1_25", 0, 57, 2, 652, alteTalk);
                break;

            case 652:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt("Zdzary1_26", 0, 56, 2, 653, alteTalk);
                break;

            case 653:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt("Zdzary1_27", 0, 57, 2, 654, alteTalk);
                break;

            case 654:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt("Zdzary1_28", 0, 56, 2, 600, alteTalk);
                break;

            case 660:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt("Zdzary1_29", 0, 57, 2, 661, alteTalk);
                break;

            case 661:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt("Zdzary1_30", 0, 56, 2, 662, alteTalk);
                break;

            case 662:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt("Zdzary1_31", 0, 57, 2, 663, alteTalk);
                break;

            case 663:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt("Zdzary1_32", 0, 56, 2, 600, alteTalk);
                break;

            case 670:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_33", 0, 56, 2, 671, alteTalk);
                break;

            case 671:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_34", 0, 57, 2, 672, alteTalk);
                break;

            case 672:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_35", 0, 56, 2, 673, alteTalk);
                break;

            case 673:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_36", 0, 57, 2, 674, alteTalk);
                break;

            case 674:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_37", 0, 56, 2, 675, alteTalk);
                break;

            case 675:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_38", 0, 57, 2, 676, alteTalk);
                break;

            case 676:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt("Zdzary1_39", 0, 56, 2, 600, alteTalk);
                break;

            case 680:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt("Zdzary1_40", 0, 56, 2, 681, alteTalk);
                break;

            case 681:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt("Zdzary1_41", 0, 57, 2, 682, alteTalk);
                break;

            case 682:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt("Zdzary1_42", 0, 56, 2, 683, alteTalk);
                break;

            case 683:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt("Zdzary1_43", 0, 57, 2, 684, alteTalk);
                break;

            case 684:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt("Zdzary1_44", 0, 56, 2, 685, alteTalk);
                break;

            case 685:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt("Zdzary1_45", 0, 57, 2, 600, alteTalk);
                break;

            case 690:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_46", 0, 56, 2, 691, alteTalk);
                break;

            case 691:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_47", 0, 57, 2, 692, alteTalk);
                break;

            case 692:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_48", 0, 56, 2, 693, alteTalk);
                break;

            case 693:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_49", 0, 57, 2, 694, alteTalk);
                break;

            case 694:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_50", 0, 56, 2, 695, alteTalk);
                break;

            case 695:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_51", 0, 57, 2, 696, alteTalk);
                break;

            case 696:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_52", 0, 56, 2, 697, alteTalk);
                break;

            case 697:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt("Zdzary1_53", 0, 57, 2, 600, alteTalk);
                break;

            case 700:
                // Reaktion Alte auf 2. Teil 5. Frage
                PersonSagt("Zdzary1_54", 0, 56, 2, 701, alteTalk);
                break;

            case 701:
                // Reaktion Alte auf 2. Teil 5. Frage
                PersonSagt("Zdzary1_55", 0, 57, 2, 702, alteTalk);
                break;

            case 702:
                // Reaktion Alte auf 2. Teil 5. Frage
                PersonSagt("Zdzary1_56", 0, 56, 2, 600, alteTalk);
                break;

            case 710:
                // Reaktion Alte auf 1. Teil 2. Frage
                PersonSagt("Zdzary1_57", 0, 57, 2, 711, alteTalk);
                break;

            case 711:
                // Reaktion Alte auf 1. Teil 2. Frage
                PersonSagt("Zdzary1_58", 0, 56, 2, 712, alteTalk);
                break;

            case 712:
                // Reaktion Alte auf 1. Teil 2. Frage
                PersonSagt("Zdzary1_59", 0, 57, 2, 713, alteTalk);
                break;

            case 713:
                // nach-Vorn-Laufen
                mainFrame.pathWalker.SetzeNeuenWeg(Puser);
                nextActionID = 714;
                break;

            case 714:
                // User anschauen
                outputText = mainFrame.imageFont.TeileTextKey("Zdzary1_89");
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                nextActionID = 715;
                TalkPerson = 3;
                mainFrame.krabat.SetFacing(6);
                break;

            case 715:
                // Multiple - Choice - Routine
                Userdialog.InitMC(100);
                // 1. Frage
                Userdialog.ExtendMC("Zdzary1_90", new GenericRectangle(30, 0, 500, 67), 1);

                // 2. Frage
                Userdialog.ExtendMC("Zdzary1_91", new GenericRectangle(30, 0, 500, 40), 2);
                Userdialog.user = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 716;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 716:
                // Ausgewaehltes Usermultiple-Choice-Ding wird ausgefuehrt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);

                // Aktionen dementsprechend ausfuehren
                switch (Userdialog.Ident[Userdialog.Antwort]) {
                    case 1:
                        // Weglaufen
                        nextActionID = 800;
                        break;
                    case 2:
                        // Hiergeblieben
                        nextActionID = 717;
                        break;
                }
                break;

            case 717:
                // wieder zur Alten hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(Palte);
                nextActionID = 718;
                break;

            case 718:
                // Alte anschauen und weiter im Text
                mainFrame.krabat.SetFacing(alteFacing);
                nextActionID = 600;
                break;

            case 720:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt("Zdzary1_60", 0, 56, 2, 721, alteTalk);
                break;

            case 721:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt("Zdzary1_61", 0, 57, 2, 722, alteTalk);
                break;

            case 722:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt("Zdzary1_62", 0, 56, 2, 723, alteTalk);
                break;

            case 723:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt("Zdzary1_63", 0, 57, 2, 600, alteTalk);
                break;

            case 730:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt("Zdzary1_64", 0, 56, 2, 731, alteTalk);
                break;

            case 731:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt("Zdzary1_65", 0, 57, 2, 732, alteTalk);
                break;

            case 732:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt("Zdzary1_66", 0, 56, 2, 733, alteTalk);
                break;

            case 733:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt("Zdzary1_67", 0, 56, 2, 600, alteTalk);
                break;

            case 740:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt("Zdzary1_68", 0, 56, 2, 741, alteTalk);
                break;

            case 741:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt("Zdzary1_69", 0, 57, 2, 742, alteTalk);
                break;

            case 742:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt("Zdzary1_70", 0, 56, 2, 743, alteTalk);
                break;

            case 743:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt("Zdzary1_71", 0, 57, 2, 744, alteTalk);
                break;

            case 744:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt("Zdzary1_72", 0, 56, 2, 745, alteTalk);
                break;

            case 745:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt("Zdzary1_73", 0, 57, 2, 600, alteTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}