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
import de.codengine.krabat.anims.Fararhor;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Cyrkej2 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Cyrkej2.class);
    private GenericImage background;
    private GenericImage durje;
    private Fararhor pfarrer;
    private final Multiple2 Dialog;
    // private borderrect pfarrerRect;

    private final GenericPoint pfPoint;
    private final GenericPoint pfarrerTalk;

    private int WaitCount;

    private boolean showPfarrer = false;
    private boolean doorOpen = false;
    private boolean openDoorAnim = false;

    // Konstanten - Rects
    private static final Borderrect linkerAusgang = new Borderrect(0, 426, 38, 479);
    private static final Borderrect rechterAusgang = new Borderrect(590, 300, 639, 400);
    private static final Borderrect brTuer = new Borderrect(312, 297, 354, 327);

    // Punkte in Location
    private static final GenericPoint Ptuer = new GenericPoint(334, 330);
    private static final GenericPoint Pleft = new GenericPoint(0, 462);
    private static final GenericPoint Pright = new GenericPoint(639, 350);
    private static final GenericPoint pfarrerPoint = new GenericPoint(318, 310);

    // Konstante ints
    private static final int fTuer = 12;
    private static final int fPfarrer = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Cyrkej2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.actions[851] = true;
        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(7, true);

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 21f;
        mainFrame.krabat.defScale = 0;

        pfarrer = new Fararhor(mainFrame);
        pfPoint = new GenericPoint();
        // pfPoint.x = pfarrerPoint.x - (pfarrer.Breite / 2);
        // pfPoint.y = pfarrerPoint.y - (pfarrer.Hoehe  / 2);
        pfPoint.x = pfarrerPoint.x;
        pfPoint.y = pfarrerPoint.y;
        // pfarrerRect = new borderrect (pfPoint.x, pfPoint.y, pfPoint.x + pfarrer.Breite, pfPoint.y + pfarrer.Hoehe);

        pfarrerTalk = new GenericPoint();
        pfarrerTalk.x = pfarrerPoint.x;
        pfarrerTalk.y = pfPoint.y - 50;

        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(330, 600, 40, 600, 384, 462));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(334, 335, 337, 374, 330, 383));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(605, 639, 380, 639, 311, 383));
        mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(600, 290, 610, 310));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(4);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(0, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 76:
                // von Kulow aus
                mainFrame.krabat.setPos(new GenericPoint(604, 370));
                mainFrame.krabat.SetFacing(9);
                break;
            case 72:
                // von Dubring aus
                mainFrame.krabat.setPos(new GenericPoint(78, 456));
                mainFrame.krabat.SetFacing(3);
                break;
            case 81:
                // von Pinca aus
                mainFrame.krabat.setPos(Ptuer);
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/cyrkej/cyrkej2.png");
        durje = getPicture("gfx/cyrkej/cdurje.png");

    }

    @Override
    public void cleanup() {
        background = null;
        durje = null;

        pfarrer.cleanup();
        pfarrer = null;
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
        g.drawImage(background, 0, 0);

        // Tuer zeichnen, wenn offen
        if (doorOpen) {
            g.setClip(314, 299, 25, 34);
            g.drawImage(durje, 314, 299);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Pfarrer zeichnen
        if (showPfarrer) {
            if (openDoorAnim) {
                g.setClip(320, 300, 32, 29);
                openDoorAnim = pfarrer.moveDoor(g);
            } else {
                g.setClip(pfPoint.x, pfPoint.y, Fararhor.Breite, Fararhor.Hoehe);
                g.drawImage(background, 0, 0);
                pfarrer.drawFarar(g, TalkPerson, pfPoint);
            }
        }

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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                if (brTuer.IsPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTemp = Ptuer;
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

                // zu Kulow gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;

                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Karte einblenden
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
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

                // Tuer anschauen
                if (brTuer.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Ptuer;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Kulow anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Dubring anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // an die Tuer Klopfen
                if (brTuer.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Ptuer);
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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || brTuer.IsPointInRect(pTemp);

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
            if (brTuer.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
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

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 2;
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

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
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
                // Tuer anschauen
                KrabatSagt("Cyrkej2_1", fTuer, 3, 0, 0);
                break;

            case 50:
                // An Tuer der Kirche klopfen
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.SetFacing(fTuer);
                mainFrame.krabat.nAnimation = 4;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.soundPlayer.PlayFile("sfx/klopfklopf.wav");
                nextActionID = 55;
                WaitCount = 50;
                break;

            case 55:
                // bisschen warten
                if (--WaitCount < 1) {
                    nextActionID = 57;
                }
                break;

            case 57:
                // Leider hoert keiner, aber beim 1. Mal doch !!
                if (mainFrame.actions[302]) {
                    KrabatSagt("Cyrkej2_2", fTuer, 3, 0, 0);
                    mainFrame.isAnimRunning = false;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                } else {
                    nextActionID = 58;
                }
                break;

            case 58:
                // Krabat tritt zurueck, weil Pfarrer ja Platz braucht
                mainFrame.pathWalker.SetzeNeuenWeg(new GenericPoint(334, 340));
                nextActionID = 59;
                break;

            case 59:
                mainFrame.krabat.SetFacing(fPfarrer);
                nextActionID = 60;
                break;

            case 60:
                // Pfarrer erscheinen lassen
                doorOpen = true;
                mainFrame.soundPlayer.PlayFile("sfx/cdurjeauf.wav");
                showPfarrer = true;
                openDoorAnim = true;
                nextActionID = 65;
                break;

            case 65:
                // Skip zu MC
                if (!openDoorAnim) {
                    nextActionID = 600;
                }
                break;

            case 100:
                // Karte einblenden
                mainFrame.ConstructLocation(106);
                mainFrame.isBackgroundAnimRunning = false;
                mainFrame.whatScreen = 6;
                nextActionID = 0;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 101:
                // Goto Kulow
                mainFrame.actions[851] = false;
                NeuesBild(76, 70);
                break;

            case 102:
                // Goto Pinca
                mainFrame.actions[851] = false;
                NeuesBild(81, 70);
                break;

            case 150:
                // Standardausreden fuer Tuer
                DingAusrede(fTuer);
                break;

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Cyrkej2_19", 1000, 260, new int[]{260}, 610);
                Dialog.ExtendMC("Cyrkej2_20", 260, 261, new int[]{261}, 620);
                Dialog.ExtendMC("Cyrkej2_21", 261, 262, new int[]{262}, 630);


                // 2. Frage
                Dialog.ExtendMC("Cyrkej2_22", 1000, 264, new int[]{264}, 660);
                Dialog.ExtendMC("Cyrkej2_23", 264, 265, new int[]{265}, 670);
                Dialog.ExtendMC("Cyrkej2_24", 265, 1000, null, 680);


                // 4. Frage (3. = Ende)
                Dialog.ExtendMC("Cyrkej2_25", 267, 268, new int[]{268}, 690);

                // 3. Frage
                Dialog.ExtendMC("Cyrkej2_26", 1000, 270, null, 800);
                Dialog.ExtendMC("Cyrkej2_27", 270, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.actions[270] = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 610:
                // Reaktion Farar auf 1. Teil 1. Frage
                PersonSagt("Cyrkej2_3", 0, 37, 2, 600, pfarrerTalk);
                break;

            case 620:
                // Reaktion Farar auf 2. Teil 1. Frage
                PersonSagt("Cyrkej2_4", 0, 37, 2, 625, pfarrerTalk);
                break;

            case 625:
                // Reaktion Farar auf 2. Teil 1. Frage
                PersonSagt("Cyrkej2_5", 0, 37, 2, 600, pfarrerTalk);
                break;

            case 630:
                // Reaktion Farar auf 3. Teil 1. Frage
                PersonSagt("Cyrkej2_6", 0, 37, 2, 632, pfarrerTalk);
                break;


            case 632:
                // Reaktion Farar auf 3. Teil 1. Frage
                PersonSagt("Cyrkej2_7", 0, 37, 2, 634, pfarrerTalk);
                break;

            case 634:
                // Reaktion Farar auf 3. Teil 1. Frage
                PersonSagt("Cyrkej2_8", 0, 37, 2, 636, pfarrerTalk);
                break;

            case 636:
                // Krabat sequenzt hier 
                KrabatSagt("Cyrkej2_9", 0, 1, 2, 640);
                break;

            case 640:
                // Reaktion Farar auf 4. Teil 1. Frage
                PersonSagt("Cyrkej2_10", 0, 37, 2, 645, pfarrerTalk);
                break;

            case 645:
                // Krabat sagt nen Spruch  
                KrabatSagt("Cyrkej2_11", 0, 1, 2, 650);
                break;

            case 650:
                // Reaktion Farar auf 5. Teil 1. Frage
                PersonSagt("Cyrkej2_12", 0, 37, 2, 600, pfarrerTalk);
                break;

            case 660:
                // Reaktion Farar auf 1. Teil 2. Frage
                PersonSagt("Cyrkej2_13", 0, 37, 2, 600, pfarrerTalk);
                break;

            case 670:
                // Reaktion Farar auf 2. Teil 2. Frage
                PersonSagt("Cyrkej2_14", 0, 37, 2, 600, pfarrerTalk);
                break;

            case 680:
                // Reaktion Farar auf 3. Teil 2. Frage
                PersonSagt("Cyrkej2_15", 0, 37, 2, 800, pfarrerTalk);
                break;

            case 690:
                // Reaktion Farar auf 1. Teil 4. Frage
                PersonSagt("Cyrkej2_16", 0, 37, 2, 695, pfarrerTalk);
                break;

            case 695:
                // Krabat sequenzt
                KrabatSagt("Cyrkej2_17", 0, 1, 2, 700);
                break;


            case 700:
                // Reaktion Farar auf 2. Teil 4. Frage
                PersonSagt("Cyrkej2_18", 0, 37, 2, 710, pfarrerTalk);
                break;

            case 710:
                // Umschalten auf Pinca (Anim)
                mainFrame.actions[851] = false;
                NeuesBild(81, 70);
                break;

            case 800:
                // den Pfarrer wieder verschwinden lassen
                mainFrame.actions[270] = false;
                openDoorAnim = true;
                nextActionID = 805;
                break;

            case 805:
                // warten, bis Ende...
                if (!openDoorAnim) {
                    showPfarrer = false;
                    doorOpen = false;
                    mainFrame.soundPlayer.PlayFile("sfx/cdurjezu.wav");
                    nextActionID = 810;
                }
                break;

            case 810:
                // MC beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                mainFrame.isClipSet = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;


            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}