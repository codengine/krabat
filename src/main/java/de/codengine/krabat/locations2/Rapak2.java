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

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Rapak;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Rapak2 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Rapak2.class);
    private GenericImage background;
    private GenericImage blumen1;
    private GenericImage blumen2/* , schild */;

    /*
    private boolean animit = false;
    private boolean segeln = false;
    private int ykoord = 186;
    private int Zaehl;
    private boolean forward = true;
    */
    private boolean setAnim = false;

    private Rapak rabe;
    private final Multiple2 Dialog;

    private boolean wegFliegen = false;

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(162, 150, 240, 193);
    private static final Borderrect linkerAusgang = new Borderrect(0, 308, 27, 425);
    private static final Borderrect untererAusgang = new Borderrect(26, 430, 286, 479);
    private static final Borderrect rechterAusgang = new Borderrect(594, 319, 639, 447);
    private static final Borderrect blRect1 = new Borderrect(0, 367, 100, 479);
    private static final Borderrect blRect2 = new Borderrect(200, 414, 485, 479);
    private static final Borderrect rapakRect = new Borderrect(396, 147, 465, 179);
    private static final Borderrect brSchildOben = new Borderrect(393, 204, 466, 228);

    // Punkte in Location
    private static final GenericPoint Pdown = new GenericPoint(144, 479);
    private static final GenericPoint Pleft = new GenericPoint(15, 369);
    private static final GenericPoint Pup = new GenericPoint(200, 187);
    private static final GenericPoint Pright = new GenericPoint(621, 384);
    private static final GenericPoint Prapak = new GenericPoint(188, 402);
    private static final GenericPoint rapakTalk = new GenericPoint(420, 100);
    private static final GenericPoint Pschild = new GenericPoint(430, 370);

    // Konstante ints
    private static final int fRapak = 12;
    private static final int fSchildOben = 12;
    private static final int fExitRight = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Rapak2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(26, true);

        mainFrame.krabat.maxx = 415;
        mainFrame.krabat.zoomf = 1.9f;
        mainFrame.krabat.defScale = -60;

        rabe = new Rapak(mainFrame);
        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(196, 205, 172, 194, 185, 229));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(172, 230, 194, 261));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(172, 194, 190, 237, 262, 332));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(190, 237, 152, 251, 333, 365));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(0, 369, 85, 416));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(86, 366, 277, 441));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(86, 442, 233, 479));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(278, 376, 459, 441));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(460, 374, 639, 428));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(9);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 5);
        mainFrame.pathFinder.PosVerbinden(4, 5);
        mainFrame.pathFinder.PosVerbinden(5, 6);
        mainFrame.pathFinder.PosVerbinden(5, 7);
        mainFrame.pathFinder.PosVerbinden(7, 8);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
                /*   	case 4:
                        // von Haty aus
                        mainFrame.krabat.SetKrabatPos ( new GenericPoint (147, 470));
                        mainFrame.krabat.SetFacing (12);
                        break;*/
            case 93:
                // von Zdzary aus
                mainFrame.krabat.setPos(new GenericPoint(195, 200));
                mainFrame.krabat.SetFacing(6);
                if (!mainFrame.actions[250]) {
                    setAnim = true;
                }
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/rapak/rapak.png");
        blumen1 = getPicture("gfx/rapak/rap1.png");
        blumen2 = getPicture("gfx/rapak/rap3.png");
        // schild     = getPicture ("gfx/rapak/rap2.png");

    }

    @Override
    public void cleanup() {
        background = null;
        blumen1 = null;
        blumen2 = null;

        rabe.cleanup();
        rabe = null;
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
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // wenn Rabe noch da, dann auch zeichnen
        if (!mainFrame.actions[256]) {
            // Raben zeichnen
            g.setClip(rabe.rapakRect());
            g.drawImage(background, 0, 0);

            // normale Backgroundanims
            if (wegFliegen) {
                wegFliegen = rabe.flyAway(g);
            } else {
                rabe.drawRapak(g, TalkPerson);
            }
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

        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Krabat hinter Gestruepp
        if (blRect1.IsPointInRect(pKrTemp)) {
            g.drawImage(blumen1, 0, 374);
        }

        if (blRect2.IsPointInRect(pKrTemp)) {
            g.drawImage(blumen2, 241, 417);
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

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 600;
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Raben
                if (rapakRect.IsPointInRect(pTemp) && !mainFrame.actions[256]) {
                    nextActionID = 155;
                    pTemp = Prapak;
                }

                // Ausreden fuer Schild oben
                if (brSchildOben.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 200 : 170;
                    pTemp = Pschild;
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

                // zu Haty gehen ?
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

                // zu Horiz gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // oberer Ausgang zu Zdzary
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
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

                // rechter Ausgang
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    // Dopppelclick waere nix gutt
                                /*if (mainFrame.dClick == true)
                                  {
                                  mainFrame.krabat.StopWalking();
                                  mainFrame.repaint();
                                  return;
                                  } */
                }

                // Rapak ansehen
                if (rapakRect.IsPointInRect(pTemp) && !mainFrame.actions[256]) {
                    nextActionID = 2;
                    pTemp = Prapak;
                }

                // Schild oben ansehen
                if (brSchildOben.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Pschild;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Haty Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Horiz anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Zdzary anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // rechten Ausgang anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Schild oben mitnehmen
                if (brSchildOben.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // mit Raben reden
                if (rapakRect.IsPointInRect(pTemp) && !mainFrame.actions[256]) {
                    nextActionID = 615;
                    mainFrame.pathWalker.SetzeNeuenWeg(Prapak);
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    rapakRect.IsPointInRect(pTemp) && !mainFrame.actions[256] ||
                    brSchildOben.IsPointInRect(pTemp);

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
            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
                }
                return;
            }

            if (rapakRect.IsPointInRect(pTemp) && !mainFrame.actions[256] ||
                    brSchildOben.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }


            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 2;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
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

            case 2:
                // Raben anschauen, Zufallszahl zwischen 0 und 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Rapak2_1", fRapak, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Rapak2_2", fRapak, 3, 0, 0);
                        break;
                }
                break;

            case 4:
                // Schild oben anschauen
                KrabatSagt("Rapak2_3", fSchildOben, 3, 0, 0);
                break;

            case 65:
                // Schild oben mitnehmen
                KrabatSagt("Rapak2_4", fSchildOben, 3, 0, 0);
                break;

            case 100:
            case 101:
                // Karte einblenden
                mainFrame.constructLocation(106);
                mainFrame.isBackgroundAnimRunning = false;
                mainFrame.whatScreen = ScreenType.MAP;
                nextActionID = 0;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 102:
                // Gehe zu Zdzary
                NeuesBild(93, 83);
                break;

            case 103:
                // nach rechts gehen will ich nicht !
                KrabatSagt("Rapak2_5", fExitRight, 3, 0, 0);
                break;

            case 155:
                // Rapak - Ausreden
                DingAusrede(fRapak);
                break;

            case 200:
                // Kamuski auf schildern
                KrabatSagt("Rapak2_6", fSchildOben, 3, 0, 0);
                break;

            case 600:
                // Rabe spricht
                // Es kann nur einen automatischen Dialoganfang geben !!!!
                mainFrame.actions[250] = true;
                PersonSagt("Rapak2_7", 0, 41, 2, 605, rapakTalk);
                break;

            case 605:
                // Krabat spricht
                KrabatSagt("Rapak2_8", 6, 1, 2, 610);
                break;

            case 610:
                // Rabe spricht
                PersonSagt("Rapak2_9", 0, 41, 2, 615, rapakTalk);
                break;

            case 615:
                // zum Raben hingehen
                mainFrame.pathWalker.SetzeNeuenWeg(Prapak);
                nextActionID = 618;
                break;

            case 618:
                // richtigrum hinstellen
                mainFrame.krabat.SetFacing(3);
                nextActionID = 620;
                break;

            case 620:
                // Multiple - Choice - Routine mit dem Raben
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Rapak2_18", 1000, 251, new int[]{251}, 630);
                Dialog.ExtendMC("Rapak2_19", 251, 252, new int[]{252}, 640);
                Dialog.ExtendMC("Rapak2_20", 252, 1000, null, 650);

                // 2. Frage
                Dialog.ExtendMC("Rapak2_21", 1000, 253, new int[]{253}, 660);
                Dialog.ExtendMC("Rapak2_22", 253, 254, new int[]{254}, 670);

                // 3. Frage (Ende)
                Dialog.ExtendMC("Rapak2_23", 1000, 255, null, 800);
                Dialog.ExtendMC("Rapak2_24", 255, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 625;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 625:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.actions[255] = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 630:
                // Reaktion Rapak auf 1. Teil 1. Frage
                PersonSagt("Rapak2_10", 0, 41, 2, 620, rapakTalk);
                break;

            case 640:
                // Reaktion Rapak auf 1. Teil 1. Frage
                PersonSagt("Rapak2_11", 0, 41, 2, 620, rapakTalk);
                break;

            case 650:
                // Reaktion Rapak auf 1. Teil 1. Frage
                PersonSagt("Rapak2_12", 0, 41, 2, 655, rapakTalk);
                break;

            case 655:
                // Reaktion Rapak auf 1. Teil 1. Frage
                PersonSagt("Rapak2_13", 0, 41, 2, 620, rapakTalk);
                break;

            case 660:
                // Reaktion Rapak auf 1. Teil 2. Frage
                PersonSagt("Rapak2_14", 0, 41, 2, 620, rapakTalk);
                break;

            case 670:
                // Reaktion Rapak auf 1. Teil 2. Frage
                PersonSagt("Rapak2_15", 0, 41, 2, 673, rapakTalk);
                break;

            case 673:
                // Reaktion Rapak auf 1. Teil 2. Frage
                PersonSagt("Rapak2_16", 0, 41, 2, 676, rapakTalk);
                break;

            case 676:
                // Reaktion Rapak auf 1. Teil 2. Frage
                PersonSagt("Rapak2_17", 0, 41, 2, 680, rapakTalk);
                break;

            case 680:
                // Rabe fliegt weg
                wegFliegen = true;
                nextActionID = 690;
                break;

            case 690:
                // warten auf Ende
                if (!wegFliegen) {
                    nextActionID = 700;
                }
                break;

            case 700:
                // raben deaktivieren
                mainFrame.actions[256] = true;
                nextActionID = 800;
                mainFrame.isClipSet = false;
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[255] = false;
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