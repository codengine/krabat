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
import de.codengine.krabat.anims.Wudowa;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

import java.util.Objects;

public class Zdzary1 extends Mainloc {
    private GenericImage background;
    private Wudowa alte;
    private final Multiple2 Dialog;
    private final Usermultiple Userdialog;

    private int whereIsAlte = 0;

    // Punkte in Location
    private static final GenericPoint Pdown = new GenericPoint(258, 479);
    private static final GenericPoint Puser = new GenericPoint(222, 474);
    private static final GenericPoint Pup = new GenericPoint(46, 288);
    private static final GenericPoint Pdurje = new GenericPoint(309, 431);

    // Konstanten - Rects deklarieren
    private static final Borderrect untererAusgang = new Borderrect(102, 454, 366, 479);
    private static final Borderrect obererAusgang = new Borderrect(34, 233, 61, 288);
    private static final Borderrect durjeRect = new Borderrect(277, 309, 315, 400);

    // Deklarationen fuer die Alte, da sie sonstwo sein kann
    private GenericPoint Palte;
    private GenericPoint altePoint;
    private Borderrect alteRect;
    private GenericPoint alteTalk;
    private int alteFacing;

    // Konstante ints
    private static final int fDurje = 12;
    private static final int fExitUp = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zdzary1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.komme_von_karte = false; // hier ohne Bedeutung
        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 428;
        mainFrame.krabat.zoomf = 1.73f;
        mainFrame.krabat.defScale = -30;

        alte = new Wudowa(mainFrame);
        DefineAlte();

        Dialog = new Multiple2(mainFrame);
        Userdialog = new Usermultiple(mainFrame);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(77, 609, 126, 609, 454, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(478, 432, 609, 453));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(25, 344, 25, 393, 433, 453));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(17, 36, 63, 188, 380, 432));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(17, 315, 19, 379));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(33, 50, 17, 19, 290, 314));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(6);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(0, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 99:
                // von oben aus (gibts nicht)
                mainFrame.krabat.SetKrabatPos(new GenericPoint(29, 299));
                mainFrame.krabat.SetFacing(6);
                break;
            case 8:
                // von Rapak aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(270, 467));
                mainFrame.krabat.SetFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/zdzary/zdzary.gif");

    }

    private void DefineAlte() {
        altePoint = new GenericPoint();
        alteTalk = new GenericPoint();

        int zuffi = (int) (Math.random() * 2.9);
        if (!mainFrame.Actions[94]) {
            mainFrame.Actions[94] = true;
            zuffi = 2;
        }
        switch (zuffi) {
            case 0:
                // hinteres Fenster
                Palte = new GenericPoint(152, 435);
                altePoint.x = 184;
                altePoint.y = 306;
                alteRect = new Borderrect(altePoint.x, altePoint.y, altePoint.x + Wudowa.Breite, altePoint.y + 35);
                alteFacing = 3;
                break;
            case 1:
                // vorderes Fenster
                Palte = new GenericPoint(584, 446);
                altePoint.x = 526;
                altePoint.y = 225;
                alteRect = new Borderrect(altePoint.x, altePoint.y, altePoint.x + Wudowa.Breite, altePoint.y + 35);
                alteFacing = 12;
                break;
            case 2:
                // vor der Tuer
                Palte = new GenericPoint(300, 452);
                altePoint.x = 295;
                altePoint.y = 323;
                alteRect = new Borderrect(altePoint.x, altePoint.y, altePoint.x + Wudowa.Breite, altePoint.y + Wudowa.Hoehe);
                alteFacing = 12;
                break;
        }

        whereIsAlte = zuffi;

        alteTalk.x = altePoint.x + Wudowa.Breite / 2;
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
        if (Userdialog.user && mainFrame.Clipset) {
            Userdialog.paintMultiple(g);
            return;
        }

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        if (mainFrame.isMultiple && mainFrame.Clipset) {
            Dialog.paintMultiple(g);
            return;
        }

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

        // Alte Schachtel zeichnen
        g.setClip(alteRect.lo_point.x, alteRect.lo_point.y, alteRect.ru_point.x - alteRect.lo_point.x, alteRect.ru_point.y - alteRect.lo_point.y);
        g.drawImage(background, 0, 0);
        alte.drawWudowa(g, TalkPerson, altePoint, whereIsAlte == 0);

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

        // Usermultiple Choice ausfuehren
        if (Userdialog.user) {
            mainFrame.Clipset = false;
            Userdialog.paintMultiple(g);
            return;
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
        // bei Usermultiple Choice extra Mouseroutine
        if (Userdialog.user) {
            Userdialog.evalMouseEvent(e);
            return;
        }

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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                Borderrect tmp = mainFrame.krabat.KrabatRect();

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
                mainFrame.wegGeher.SetzeNeuenWeg(pTxxx);
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Rapak gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTxxx = Pdown;
                    } else {
                        pTxxx = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach oben raus
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

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

                mainFrame.wegGeher.SetzeNeuenWeg(pTxxx);
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
                    mainFrame.wegGeher.SetzeNeuenWeg(Palte);
                    mainFrame.repaint();
                    return;
                }

                // Durje mitnehmen
                if (durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurje);
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || alteRect.IsPointInRect(pTemp) ||
                    durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2;

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
            if (alteRect.IsPointInRect(pTemp) ||
                    durjeRect.IsPointInRect(pTemp) && whereIsAlte != 2) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
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

    @Override
    public void evalMouseExitEvent() {
        if (Userdialog.user) {
            Userdialog.evalMouseExitEvent();
        }
        if (mainFrame.isMultiple) {
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
                // Wudowa anschauen
                if (whereIsAlte == 2) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00000"),
                            Start.stringManager.getTranslation("Loc1_Zdzary1_00001"),
                            Start.stringManager.getTranslation("Loc1_Zdzary1_00002"),
                            alteFacing, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00003"),
                            Start.stringManager.getTranslation("Loc1_Zdzary1_00004"),
                            Start.stringManager.getTranslation("Loc1_Zdzary1_00005"),
                            alteFacing, 3, 0, 0);
                }
                break;

            case 2:
                // Durje anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00006"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00007"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00008"),
                        fDurje, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Alte benutzen)
                mainFrame.krabat.SetFacing(alteFacing);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 600;
                break;

            case 55:
                // Durje mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00009"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00010"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00011"),
                        fDurje, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Rapak
                NeuesBild(8, 19);
                break;

            case 101:
                // Gehe nach hinten
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                int zfz = (int) (Math.random() * 100);
                if (zfz > 50) {
                    mainFrame.wave.PlayFile("sfx/pos.wav");
                } else {
                    mainFrame.wave.PlayFile("sfx/pos2.wav");
                }
                nextActionID = 105;
                break;

            case 105:
                // Krabat hat Angst vor dem boesen Hund
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00012"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00013"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00014"),
                        fExitUp, 1, 2, 110);
                break;

            case 110:
                // wieder ende
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00015"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00016"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00017"),
                        alteFacing, 3, 0, 0);
                break;

            case 210:
                // bron auf wudowa
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00018"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00019"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00020"),
                        alteFacing, 3, 0, 0);
                break;


            // Krabat redet mit der Alten

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00021"), 1000, 80, new int[]{80}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00022"), 80, 81, new int[]{81}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00023"), 81, 82, new int[]{82, 87, 92}, 630);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00024"), 1000, 83, new int[]{83}, 710);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00025"), 83, 84, new int[]{84}, 720);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00026"), 84, 85, new int[]{85}, 730);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00027"), 85, 86, new int[]{86}, 740);

                    // 4. Frage ( 3. = Ende )
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00028"), 87, 88, new int[]{88}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00029"), 88, 89, new int[]{89}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00030"), 89, 90, new int[]{90}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00031"), 90, 91, new int[]{91}, 670);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00032"), 91, 1000, null, 680);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00033"), 92, 93, new int[]{93}, 690);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00034"), 93, 1000, null, 700);

                    // 3. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00035"), 1000, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00036"), 1000, 80, new int[]{80}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00037"), 80, 81, new int[]{81}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00038"), 81, 82, new int[]{82, 87, 92}, 630);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00039"), 1000, 83, new int[]{83}, 710);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00040"), 83, 84, new int[]{84}, 720);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00041"), 84, 85, new int[]{85}, 730);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00042"), 85, 86, new int[]{86}, 740);

                    // 4. Frage ( 3. = Ende )
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00043"), 87, 88, new int[]{88}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00044"), 88, 89, new int[]{89}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00045"), 89, 90, new int[]{90}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00046"), 90, 91, new int[]{91}, 670);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00047"), 91, 1000, null, 680);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00048"), 92, 93, new int[]{93}, 690);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00049"), 93, 1000, null, 700);

                    // 3. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00050"), 1000, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00051"), 1000, 80, new int[]{80}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00052"), 80, 81, new int[]{81}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00053"), 81, 82, new int[]{82, 87, 92}, 630);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00054"), 1000, 83, new int[]{83}, 710);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00055"), 83, 84, new int[]{84}, 720);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00056"), 84, 85, new int[]{85}, 730);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00057"), 85, 86, new int[]{86}, 740);

                    // 4. Frage ( 3. = Ende )
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00058"), 87, 88, new int[]{88}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00059"), 88, 89, new int[]{89}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00060"), 89, 90, new int[]{90}, 660);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00061"), 90, 91, new int[]{91}, 670);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00062"), 91, 1000, null, 680);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00063"), 92, 93, new int[]{93}, 690);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00064"), 93, 1000, null, 700);

                    // 3. Frage (Ende)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00065"), 1000, 1000, null, 800);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 610:
                // Reaktion Alte auf 1. Frage 1. Teil
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00066"), Start.stringManager.getTranslation("Loc1_Zdzary1_00067"), Start.stringManager.getTranslation("Loc1_Zdzary1_00068"),
                        0, 56, 2, 611, alteTalk);
                break;

            case 611:
                // Reaktion Alte auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00069"), Start.stringManager.getTranslation("Loc1_Zdzary1_00070"), Start.stringManager.getTranslation("Loc1_Zdzary1_00071"),
                        0, 57, 2, 612, alteTalk);
                break;

            case 612:
                // Reaktion Alte auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00072"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00073"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00074"),
                        0, 56, 2, 613, alteTalk);
                break;

            case 613:
                // Reaktion Alte auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00075"), Start.stringManager.getTranslation("Loc1_Zdzary1_00076"), Start.stringManager.getTranslation("Loc1_Zdzary1_00077"),
                        0, 57, 2, 600, alteTalk);
                break;

            case 620:
                // Reaktion Alte auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00078"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00079"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00080"),
                        0, 56, 2, 621, alteTalk);
                break;

            case 621:
                // Reaktion Alte auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00081"), Start.stringManager.getTranslation("Loc1_Zdzary1_00082"), Start.stringManager.getTranslation("Loc1_Zdzary1_00083"),
                        0, 57, 2, 600, alteTalk);
                break;

            case 630:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00084"), Start.stringManager.getTranslation("Loc1_Zdzary1_00085"), Start.stringManager.getTranslation("Loc1_Zdzary1_00086"),
                        0, 57, 2, 631, alteTalk);
                break;

            case 631:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00087"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00088"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00089"),
                        0, 56, 2, 632, alteTalk);
                break;

            case 632:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00090"), Start.stringManager.getTranslation("Loc1_Zdzary1_00091"), Start.stringManager.getTranslation("Loc1_Zdzary1_00092"),
                        0, 57, 2, 633, alteTalk);
                break;

            case 633:
                // Reaktion Alte auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00093"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00094"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00095"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 640:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00096"), Start.stringManager.getTranslation("Loc1_Zdzary1_00097"), Start.stringManager.getTranslation("Loc1_Zdzary1_00098"),
                        0, 57, 2, 641, alteTalk);
                break;

            case 641:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00099"), Start.stringManager.getTranslation("Loc1_Zdzary1_00100"), Start.stringManager.getTranslation("Loc1_Zdzary1_00101"),
                        0, 56, 2, 642, alteTalk);
                break;

            case 642:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00102"), Start.stringManager.getTranslation("Loc1_Zdzary1_00103"), Start.stringManager.getTranslation("Loc1_Zdzary1_00104"),
                        0, 57, 2, 643, alteTalk);
                break;

            case 643:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00105"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00106"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00107"),
                        0, 56, 2, 644, alteTalk);
                break;

            case 644:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00108"), Start.stringManager.getTranslation("Loc1_Zdzary1_00109"), Start.stringManager.getTranslation("Loc1_Zdzary1_00110"),
                        0, 57, 2, 645, alteTalk);
                break;

            case 645:
                // Reaktion Alte auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00111"), Start.stringManager.getTranslation("Loc1_Zdzary1_00112"), Start.stringManager.getTranslation("Loc1_Zdzary1_00113"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 650:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00114"), Start.stringManager.getTranslation("Loc1_Zdzary1_00115"), Start.stringManager.getTranslation("Loc1_Zdzary1_00116"),
                        0, 56, 2, 651, alteTalk);
                break;

            case 651:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00117"), Start.stringManager.getTranslation("Loc1_Zdzary1_00118"), Start.stringManager.getTranslation("Loc1_Zdzary1_00119"),
                        0, 57, 2, 652, alteTalk);
                break;

            case 652:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00120"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00121"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00122"),
                        0, 56, 2, 653, alteTalk);
                break;

            case 653:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00123"), Start.stringManager.getTranslation("Loc1_Zdzary1_00124"), Start.stringManager.getTranslation("Loc1_Zdzary1_00125"),
                        0, 57, 2, 654, alteTalk);
                break;

            case 654:
                // Reaktion Alte auf 2. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00126"), Start.stringManager.getTranslation("Loc1_Zdzary1_00127"), Start.stringManager.getTranslation("Loc1_Zdzary1_00128"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 660:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00129"), Start.stringManager.getTranslation("Loc1_Zdzary1_00130"), Start.stringManager.getTranslation("Loc1_Zdzary1_00131"),
                        0, 57, 2, 661, alteTalk);
                break;

            case 661:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00132"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00133"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00134"),
                        0, 56, 2, 662, alteTalk);
                break;

            case 662:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00135"), Start.stringManager.getTranslation("Loc1_Zdzary1_00136"), Start.stringManager.getTranslation("Loc1_Zdzary1_00137"),
                        0, 57, 2, 663, alteTalk);
                break;

            case 663:
                // Reaktion Alte auf 3. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00138"), Start.stringManager.getTranslation("Loc1_Zdzary1_00139"), Start.stringManager.getTranslation("Loc1_Zdzary1_00140"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 670:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00141"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00142"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00143"),
                        0, 56, 2, 671, alteTalk);
                break;

            case 671:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00144"), Start.stringManager.getTranslation("Loc1_Zdzary1_00145"), Start.stringManager.getTranslation("Loc1_Zdzary1_00146"),
                        0, 57, 2, 672, alteTalk);
                break;

            case 672:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00147"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00148"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00149"),
                        0, 56, 2, 673, alteTalk);
                break;

            case 673:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00150"), Start.stringManager.getTranslation("Loc1_Zdzary1_00151"), Start.stringManager.getTranslation("Loc1_Zdzary1_00152"),
                        0, 57, 2, 674, alteTalk);
                break;

            case 674:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00153"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00154"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00155"),
                        0, 56, 2, 675, alteTalk);
                break;

            case 675:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00156"), Start.stringManager.getTranslation("Loc1_Zdzary1_00157"), Start.stringManager.getTranslation("Loc1_Zdzary1_00158"),
                        0, 57, 2, 676, alteTalk);
                break;

            case 676:
                // Reaktion Alte auf 4. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00159"), Start.stringManager.getTranslation("Loc1_Zdzary1_00160"), Start.stringManager.getTranslation("Loc1_Zdzary1_00161"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 680:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00162"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00163"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00164"),
                        0, 56, 2, 681, alteTalk);
                break;

            case 681:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00165"), Start.stringManager.getTranslation("Loc1_Zdzary1_00166"), Start.stringManager.getTranslation("Loc1_Zdzary1_00167"),
                        0, 57, 2, 682, alteTalk);
                break;

            case 682:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00168"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00169"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00170"),
                        0, 56, 2, 683, alteTalk);
                break;

            case 683:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00171"), Start.stringManager.getTranslation("Loc1_Zdzary1_00172"), Start.stringManager.getTranslation("Loc1_Zdzary1_00173"),
                        0, 57, 2, 684, alteTalk);
                break;

            case 684:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00174"), Start.stringManager.getTranslation("Loc1_Zdzary1_00175"), Start.stringManager.getTranslation("Loc1_Zdzary1_00176"),
                        0, 56, 2, 685, alteTalk);
                break;

            case 685:
                // Reaktion Alte auf 5. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00177"), Start.stringManager.getTranslation("Loc1_Zdzary1_00178"), Start.stringManager.getTranslation("Loc1_Zdzary1_00179"),
                        0, 57, 2, 600, alteTalk);
                break;

            case 690:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00180"), Start.stringManager.getTranslation("Loc1_Zdzary1_00181"), Start.stringManager.getTranslation("Loc1_Zdzary1_00182"),
                        0, 56, 2, 691, alteTalk);
                break;

            case 691:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00183"), Start.stringManager.getTranslation("Loc1_Zdzary1_00184"), Start.stringManager.getTranslation("Loc1_Zdzary1_00185"),
                        0, 57, 2, 692, alteTalk);
                break;

            case 692:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00186"), Start.stringManager.getTranslation("Loc1_Zdzary1_00187"), Start.stringManager.getTranslation("Loc1_Zdzary1_00188"),
                        0, 56, 2, 693, alteTalk);
                break;

            case 693:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00189"), Start.stringManager.getTranslation("Loc1_Zdzary1_00190"), Start.stringManager.getTranslation("Loc1_Zdzary1_00191"),
                        0, 57, 2, 694, alteTalk);
                break;

            case 694:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00192"), Start.stringManager.getTranslation("Loc1_Zdzary1_00193"), Start.stringManager.getTranslation("Loc1_Zdzary1_00194"),
                        0, 56, 2, 695, alteTalk);
                break;

            case 695:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00195"), Start.stringManager.getTranslation("Loc1_Zdzary1_00196"), Start.stringManager.getTranslation("Loc1_Zdzary1_00197"),
                        0, 57, 2, 696, alteTalk);
                break;

            case 696:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00198"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00199"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00200"),
                        0, 56, 2, 697, alteTalk);
                break;

            case 697:
                // Reaktion Alte auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00201"), Start.stringManager.getTranslation("Loc1_Zdzary1_00202"), Start.stringManager.getTranslation("Loc1_Zdzary1_00203"),
                        0, 57, 2, 600, alteTalk);
                break;

            case 700:
                // Reaktion Alte auf 2. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00204"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00205"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00206"),
                        0, 56, 2, 701, alteTalk);
                break;

            case 701:
                // Reaktion Alte auf 2. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00207"), Start.stringManager.getTranslation("Loc1_Zdzary1_00208"), Start.stringManager.getTranslation("Loc1_Zdzary1_00209"),
                        0, 57, 2, 702, alteTalk);
                break;

            case 702:
                // Reaktion Alte auf 2. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00210"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00211"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00212"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 710:
                // Reaktion Alte auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00213"), Start.stringManager.getTranslation("Loc1_Zdzary1_00214"), Start.stringManager.getTranslation("Loc1_Zdzary1_00215"),
                        0, 57, 2, 711, alteTalk);
                break;

            case 711:
                // Reaktion Alte auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00216"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00217"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00218"),
                        0, 56, 2, 712, alteTalk);
                break;

            case 712:
                // Reaktion Alte auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00219"), Start.stringManager.getTranslation("Loc1_Zdzary1_00220"), Start.stringManager.getTranslation("Loc1_Zdzary1_00221"),
                        0, 57, 2, 713, alteTalk);
                break;

            case 713:
                // nach-Vorn-Laufen
                mainFrame.wegGeher.SetzeNeuenWeg(Puser);
                nextActionID = 714;
                break;

            case 714:
                // User anschauen
                if (mainFrame.sprache == 1) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Zdzary1_00222"));
                }
                if (mainFrame.sprache == 2) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Zdzary1_00223"));
                }
                if (mainFrame.sprache == 3) {
                    outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Zdzary1_00224"));
                }
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                nextActionID = 715;
                TalkPerson = 3;
                mainFrame.krabat.SetFacing(6);
                break;

            case 715:
                // Multiple - Choice - Routine
                Userdialog.InitMC(100);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Userdialog.ExtendMC(mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Zdzary1_00225"))
                            , new GenericRectangle(30, 0, 500, 67), 1);

                    // 2. Frage
                    Userdialog.ExtendMC(mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Zdzary1_00226"))
                            , new GenericRectangle(30, 0, 500, 40), 2);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Userdialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00227")
                            , new GenericRectangle(30, 0, 500, 67), 1);

                    // 2. Frage
                    Userdialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00228")
                            , new GenericRectangle(30, 0, 500, 40), 2);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Userdialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00229")
                            , new GenericRectangle(30, 0, 500, 67), 1);

                    // 2. Frage
                    Userdialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Zdzary1_00230")
                            , new GenericRectangle(30, 0, 500, 40), 2);
                }
                Userdialog.user = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 716;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 716:
                // Ausgewaehltes Usermultiple-Choice-Ding wird ausgefuehrt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);

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
                mainFrame.wegGeher.SetzeNeuenWeg(Palte);
                nextActionID = 718;
                break;

            case 718:
                // Alte anschauen und weiter im Text
                mainFrame.krabat.SetFacing(alteFacing);
                nextActionID = 600;
                break;

            case 720:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00231"), Start.stringManager.getTranslation("Loc1_Zdzary1_00232"), Start.stringManager.getTranslation("Loc1_Zdzary1_00233"),
                        0, 56, 2, 721, alteTalk);
                break;

            case 721:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00234"), Start.stringManager.getTranslation("Loc1_Zdzary1_00235"), Start.stringManager.getTranslation("Loc1_Zdzary1_00236"),
                        0, 57, 2, 722, alteTalk);
                break;

            case 722:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00237"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00238"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00239"),
                        0, 56, 2, 723, alteTalk);
                break;

            case 723:
                // Reaktion Alte auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00240"), Start.stringManager.getTranslation("Loc1_Zdzary1_00241"), Start.stringManager.getTranslation("Loc1_Zdzary1_00242"),
                        0, 57, 2, 600, alteTalk);
                break;

            case 730:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00243"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00244"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00245"),
                        0, 56, 2, 731, alteTalk);
                break;

            case 731:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00246"), Start.stringManager.getTranslation("Loc1_Zdzary1_00247"), Start.stringManager.getTranslation("Loc1_Zdzary1_00248"),
                        0, 57, 2, 732, alteTalk);
                break;

            case 732:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00249"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00250"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00251"),
                        0, 56, 2, 733, alteTalk);
                break;

            case 733:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00252"), Start.stringManager.getTranslation("Loc1_Zdzary1_00253"), Start.stringManager.getTranslation("Loc1_Zdzary1_00254"),
                        0, 56, 2, 600, alteTalk);
                break;

            case 740:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00255"), Start.stringManager.getTranslation("Loc1_Zdzary1_00256"), Start.stringManager.getTranslation("Loc1_Zdzary1_00257"),
                        0, 56, 2, 741, alteTalk);
                break;

            case 741:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00258"), Start.stringManager.getTranslation("Loc1_Zdzary1_00259"), Start.stringManager.getTranslation("Loc1_Zdzary1_00260"),
                        0, 57, 2, 742, alteTalk);
                break;

            case 742:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00261"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00262"),
                        Start.stringManager.getTranslation("Loc1_Zdzary1_00263"),
                        0, 56, 2, 743, alteTalk);
                break;

            case 743:
                // Reaktion Alte auf 4. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00264"), Start.stringManager.getTranslation("Loc1_Zdzary1_00265"), Start.stringManager.getTranslation("Loc1_Zdzary1_00266"),
                        0, 57, 2, 744, alteTalk);
                break;

            case 744:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00267"), Start.stringManager.getTranslation("Loc1_Zdzary1_00268"), Start.stringManager.getTranslation("Loc1_Zdzary1_00269"),
                        0, 56, 2, 745, alteTalk);
                break;

            case 745:
                // Reaktion Alte auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Zdzary1_00270"), Start.stringManager.getTranslation("Loc1_Zdzary1_00271"), Start.stringManager.getTranslation("Loc1_Zdzary1_00272"),
                        0, 57, 2, 600, alteTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
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