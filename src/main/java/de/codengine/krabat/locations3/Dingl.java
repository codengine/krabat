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
import de.codengine.krabat.anims.Dinglinger;
import de.codengine.krabat.anims.DinglingerWalk;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Dingl extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Dingl.class);
    private GenericImage background;
    private GenericImage blido;
    private GenericImage tworba;
    private GenericImage kombinacija;
    private final Dinglinger dinglinger;
    private final DinglingerWalk dinglingerwalk;
    private final MultipleChoice Dialog;

    private final GenericPoint talkPoint;
    private final BorderRect reDinglinger;

    private boolean setAnim = false;

    private boolean sitzenderDinglinger = true;

    private int welcheAnim = 0;
    private int animRueckgabe = 0;

    private boolean walkReady = true;

    private int Counter = 0;

    private boolean zeigeGrossesBild = false;

    // Konstanten - Rects
    private static final BorderRect obererAusgang
            = new BorderRect(17, 122, 92, 248);
    private static final BorderRect rechterAusgang
            = new BorderRect(426, 123, 526, 327);
    private static final BorderRect kunstwerk
            = new BorderRect(465, 365, 639, 479);
    private static final BorderRect blidoRect
            = new BorderRect(176, 328, 235, 390);

    // Konstante Points
    private static final GenericPoint pExitUp = new GenericPoint(50, 255);
    private static final GenericPoint pExitRight = new GenericPoint(452, 337);
    private static final GenericPoint pDinglinger = new GenericPoint(195, 413);
    private static final GenericPoint dinglLO = new GenericPoint(41, 264);
    private static final GenericPoint pKunstwerk = new GenericPoint(429, 432);
    private static final GenericPoint pBlido = new GenericPoint(310, 392);
    private static final GenericPoint dinglFeet = new GenericPoint(394, 395);

    // Konstante ints
    private static final int fDingl = 9;
    private static final int fKanne = 9;
    private static final int fKunstwerk = 6;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Dingl(Start caller, int oldLocation) {
        super(caller, 141);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 350;
        mainFrame.krabat.zoomf = 1.84f;
        mainFrame.krabat.defScale = -60;

        dinglinger = new Dinglinger(mainFrame);

        talkPoint = new GenericPoint();
        talkPoint.x = dinglLO.x + Dinglinger.Breite / 2;
        talkPoint.y = dinglLO.y - 50;

        reDinglinger = new BorderRect(dinglLO.x, dinglLO.y, dinglLO.x + Dinglinger.Breite, dinglLO.y + Dinglinger.Hoehe);

        dinglingerwalk = new DinglingerWalk(mainFrame);

        dinglingerwalk.maxx = 0;
        dinglingerwalk.zoomf = 1f;
        dinglingerwalk.defScale = 0;

        dinglingerwalk.setPos(dinglFeet);
        dinglingerwalk.SetFacing(9);

        Dialog = new MultipleChoice(mainFrame);

        InitLocation(oldLocation);

        mainFrame.freeze(false);

        // Zum Testen - rausnehmen !!!!!!!!!!!!!!!!
        // mainFrame.Actions[527] = true; // darf zum Gang raus
        // mainFrame.Actions[530] = true; // bereits bei Zahr. gewesen
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // wenn kein Load, dann alle Versuche zuruecksetzen
        if (oldLocation != 0) {
            // Actions fuer Versuche zuruecksetzen
            for (int i = 650; i <= 652; i++) {
                mainFrame.actions[i] = false;
            }
        }

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 75, 20, 350, 255, 336));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 70, 20, 70, 337, 420));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(176, 480, 195, 469, 337, 413));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(244, 468, 306, 460, 414, 432));

        mainFrame.pathFinder.ClearMatrix(4);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(0, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);

        InitImages();

        sitzenderDinglinger = true;

        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 140: // von Saal aus
                mainFrame.krabat.setPos(new GenericPoint(443, 344));
                mainFrame.krabat.SetFacing(9);
                break;
            case 142: // von Chodba aus
                mainFrame.krabat.setPos(new GenericPoint(50, 265));
                mainFrame.krabat.SetFacing(6);
                break;
            case 181: // von Poklad - Location aus
                mainFrame.krabat.setPos(new GenericPoint(320, 360));
                mainFrame.krabat.SetFacing(3);
                sitzenderDinglinger = false;
                setAnim = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/dingl/dingl.png");
        blido = getPicture("gfx-dd/dingl/blido.png");
        tworba = getPicture("gfx-dd/dingl/tworba.png");

        kombinacija = getPicture("gfx-dd/dingl/kombinacija.png");

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
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
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

        // Dinglinger Hintergrund loeschen
        if (sitzenderDinglinger) {
            // sitzender Dinglinger
            g.setClip(dinglLO.x, dinglLO.y, Dinglinger.Breite, Dinglinger.Hoehe);
            g.drawImage(background, 0, 0);
        } else {
            // stehender Dinglinger

            // Hintergrund loeschen
            BorderRect temp = dinglingerwalk.getRect();
            g.setClip(temp.lo_point.x, temp.lo_point.y,
                    temp.ru_point.x - temp.lo_point.x, temp.ru_point.y - temp.lo_point.y);
            g.drawImage(background, 0, 0);
        }

        // Dinglinger zeichnen
        if (!sitzenderDinglinger) // nur der stehende, sitzender ist unten
        {
            // stehender Dinglinger
            // Dinglinger weiterbewegen, wenn noetig
            if (!walkReady) {
                walkReady = dinglingerwalk.Move();
            }

            // Dinglinger zeichnen
            if (TalkPerson == 47 && mainFrame.talkCount > 1) {
                dinglingerwalk.talkDinglinger(g, welcheAnim);
            } else {
                dinglingerwalk.drawDinglinger(g, welcheAnim);
            }
        }

        // Krabat einen Schritt laufen lassen
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

        // hinter blido (nur Clipping - Region wird neugezeichnet)
        if (pKrTemp.x < 370) {
            g.drawImage(blido, 0, 126);
        }
        // hinter tworba (nur Clipping - Region wird neugezeichnet)
        if (pKrTemp.x > 330) {
            g.drawImage(tworba, 381, 336);
        }

        // sitzenden Dinglinger hier zeichnen, da er im Vordergrund ist
        if (sitzenderDinglinger) {
            // sitzender Dinglinger
            GenericRectangle mx;
            mx = g.getClipBounds();
            g.setClip(dinglLO.x, dinglLO.y, Dinglinger.Breite, Dinglinger.Hoehe);
            animRueckgabe = dinglinger.drawDinglinger(g, TalkPerson, welcheAnim);
            g.setClip(mx);
        }

        // hier alles overriden und Bild zeigen, wenn noetig
        if (zeigeGrossesBild) {
            GenericRectangle mx;
            mx = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            g.drawImage(kombinacija, 0, 0);
            g.setClip(mx);
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
            nextActionID = 1100;
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

                GenericPoint pTxxxx = new GenericPoint(pTemp.x, pTemp.y);

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Sachen geben oder Ausreden fuer Dinglinger
                if (reDinglinger.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 30: // brief Zahrodnik
                            nextActionID = 160;
                            break;
                        case 32: // Dowol njepod.
                            nextActionID = 185;
                            break;
                        case 33: // Dowl njezyg.
                            nextActionID = 190;
                            break;
                        case 34: // Dowol. Komplett
                            nextActionID = 175;
                            break;
                        case 47: // Kluc
                            nextActionID = 180;
                            break;
                        case 48: // Metall
                            nextActionID = 165;
                            break;
                        case 50: // Skizze
                            nextActionID = 170;
                            break;
                        default: // Ausreden
                            nextActionID = 155;
                            break;
                    }
                    pTxxxx = pDinglinger;
                }

                // Ausreden Blido
                if (blidoRect.IsPointInRect(pTemp)) {
                    nextActionID = 200;
                    pTxxxx = pBlido;
                }

                // Ausreden Kunstwerk
                if (kunstwerk.IsPointInRect(pTemp)) {
                    nextActionID = 210;
                    pTxxxx = pKunstwerk;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTxxxx);
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

                // neuen Punkt wg. Ueberschneidung
                GenericPoint pTxxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Chodba gehen ?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTxxxx = pExitUp;
                    } else {
                        // es wird nach unten verlassen
                        pTxxxx = new GenericPoint(kt.x, pExitUp.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Saal gehen ?
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTxxxx = pExitRight;
                    } else {
                        // es wird nach unten verlassen
                        pTxxxx = new GenericPoint(kt.x, pExitRight.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // blido ansehen
                if (blidoRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTxxxx = pBlido;
                }

                // Dinglingers Werk ansehen
                if (kunstwerk.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                }

                // Dinglinger ansehen
                if (reDinglinger.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTxxxx = pDinglinger;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTxxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Dinglinger reden
                if (reDinglinger.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(pDinglinger);
                    mainFrame.repaint();
                    return;
                }

                // Kelch und Krug benutzen
                if (blidoRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    mainFrame.pathWalker.SetzeNeuenWeg(pBlido);
                    mainFrame.repaint();
                    return;
                }

                // Kunstwerk benutzen
                if (kunstwerk.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    mainFrame.pathWalker.SetzeNeuenWeg(pKunstwerk);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (rechterAusgang.IsPointInRect(pTemp) || obererAusgang.IsPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    reDinglinger.IsPointInRect(pTemp) ||
                    kunstwerk.IsPointInRect(pTemp) ||
                    blidoRect.IsPointInRect(pTemp);

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
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
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

            if (reDinglinger.IsPointInRect(pTemp) ||
                    kunstwerk.IsPointInRect(pTemp) ||
                    blidoRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
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
                // Dinglinger anschauen
                KrabatSagt("Dingl_1", fDingl, 3, 0, 0);
                break;

            case 2:
                // Kanne anschauen
                KrabatSagt("Dingl_2", fKanne, 3, 0, 0);
                break;

            case 3:
                // Kanne benutzen
                KrabatSagt("Dingl_3", fKanne, 3, 0, 0);
                break;

            case 4:
                // Kunstwerk mitnehmen
                KrabatSagt("Dingl_4", fKunstwerk, 3, 0, 0);
                break;

            case 5:
                // Dingl. Werk anschauen
                // zufallsZahl zwischen null und 1 
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Dingl_5", fKunstwerk, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Dingl_6", fKunstwerk, 3, 0, 0);
                        break;
                }
                break;

            case 50:
                // Krabat beginnt MC (Dinglinger benutzen)
                mainFrame.krabat.SetFacing(9);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);

                // hier zuhoerenden Dinglinger einschalten
                welcheAnim = 1;

                // Hat Krabat schon Dialog 3 mit Dinglinger angefangen (letzter)
                if (!mainFrame.actions[528]) {
                    // Hat Krabat schon Breif von Zahrd. gegeben ? (MC1 oder MC2)
                    if (!mainFrame.actions[527]) {
                        // faengt Dinglinger (erstes Mal) an zu reden ?
                        if (mainFrame.actions[520]) {
                            nextActionID = 600;
                        } else {
                            nextActionID = 610;
                        }
                    } else {
                        nextActionID = 700;
                    }
                } else {
                    // Dialog 3 (letzter) mit Dinglinger anfangen
                    nextActionID = 800;
                }
                break;

            case 100:
                // Gehe zu Chodba
                if (mainFrame.actions[527]) {
                    // darf zur Chodba gehen (hat von Dingl. Prikaz bekommen)
                    NeuesBild(142, locationID);
                } else {
                    // 3 mal darf Kr. versuchen rauszugehen
                    if (!mainFrame.actions[650] || !mainFrame.actions[651] || !mainFrame.actions[652]) {
                        mainFrame.isAnimRunning = true;
                        evalMouseMoveEvent(mainFrame.mousePoint);
                        nextActionID = 300;
                        if (mainFrame.actions[651]) {
                            mainFrame.actions[652] = true;
                        }
                        if (mainFrame.actions[650]) {
                            mainFrame.actions[651] = true;
                        }
                        if (!mainFrame.actions[650]) {
                            mainFrame.actions[650] = true;
                        }
                    } else {
                        // zur Strafe in die Kueche zurueck
                        NeuesBild(120, locationID);
                    }
                }
                break;

            case 101:
                // Gehe zu Saal
                NeuesBild(140, locationID);
                break;

            case 155:
                // Dinglinger - Ausreden
                MPersonAusrede(fDingl);
                break;

            case 160:
                // Dinglinger Brief von Zahrodnik geben -> darauf Dialog MC2
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.nAnimation = 138;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                welcheAnim = 1;
                nextActionID = 161;
                break;

            case 161:
                // Dingl nimmt Brief
                welcheAnim = 3;
                nextActionID = 162;
                break;

            case 162:
                // warten auf Ende nimm brief, dann lesen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 1;
                if (mainFrame.actions[520]) {
                    // hat schon um Wein gebettelt
                    PersonSagt("Dingl_7", 0, 47, 5, 163, talkPoint);
                } else {
                    // hat noch nichts vom Wein gesagt
                    PersonSagt("Dingl_8", 0, 47, 5, 163, talkPoint);
                }
                // Brief aus Inventory entfernen
                mainFrame.inventory.vInventory.removeElement(30);
                break;

            case 163:
                // Reaktion Dinglinger, wen Brief von Zahrod. gegeben
                welcheAnim = 4;
                nextActionID = 164;
                break;

            case 164:
                // Ende lesen
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 1;
                nextActionID = 303;
                break;

            case 165: // Gib Metall an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.nAnimation = 139;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 166;
                break;

            case 166: // Warten auf Ende geben
                welcheAnim = 2;
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 167;
                }
                break;

            case 167:
                // Reaktion Dinglinger auf Give Metall
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 1;
                PersonSagt("Dingl_9", 0, 47, 0, 195, talkPoint);
                mainFrame.actions[635] = true;
                mainFrame.inventory.vInventory.removeElement(48);
                break;

            case 170: // Gib Skizze an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.nAnimation = 140;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 171;
                break;

            case 171: // Warten auf Ende geben
                welcheAnim = 2;
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 172;
                }
                break;

            case 172:
                // Reaktion Dinglinger auf Give Skizze
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 1;
                PersonSagt("Dingl_10", 0, 47, 0, 195, talkPoint);
                mainFrame.actions[636] = true;
                mainFrame.inventory.vInventory.removeElement(50);
                break;

            case 175: // Gib Dowolnosc an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.nAnimation = 141;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 176;
                break;

            case 176: // Warten auf Ende geben
                welcheAnim = 2;
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 177;
                }
                break;

            case 177:
                // Reaktion Dinglinger auf Give Dowolnosc komplett
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 1;
                PersonSagt("Dingl_11", 0, 47, 0, 195, talkPoint);
                mainFrame.actions[637] = true;
                mainFrame.inventory.vInventory.removeElement(34);
                break;

            case 180: // Gib Kluc an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.nAnimation = 137;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 181;
                break;

            case 181: // Warten auf Ende geben
                welcheAnim = 2;
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 182;
                }
                break;

            case 182:
                // Reaktion Dinglinger auf Give Kluc
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 1;
                PersonSagt("Dingl_12", 0, 47, 0, 195, talkPoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.actions[638] = true;
                mainFrame.inventory.vInventory.removeElement(47);
                break;

            case 185:
                // Reaktion Dinglinger auf Give Dowol njepodpis.
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                PersonSagt("Dingl_13", fDingl, 47, 0, 900, talkPoint);
                break;

            case 190:
                // Reaktion Dinglinger auf Give Dowol njezyg.
                PersonSagt("Dingl_14", fDingl, 47, 0, 900, talkPoint);
                break;

            case 195:
                // kontrollieren, ob alles zusammen
                if (mainFrame.actions[635] && mainFrame.actions[636] && mainFrame.actions[637] && mainFrame.actions[638]) {
                    nextActionID = 1000;
                } else {
                    nextActionID = 900;
                }
                break;

            case 200:
                // Ausreden blido
                DingAusrede(fKanne);
                break;

            case 210:
                // Ausreden kunstwerk
                DingAusrede(fKunstwerk);
                break;

            // Sequenzen mit Dinglinger  /////////////////////////////////

            case 300:
                // Reaktion Dinglinger, wenn unerlaubt zum Gang
                PersonSagt("Dingl_15", 0, 47, 2, 900, talkPoint);
                break;

            case 303:
                // Reaktion Dinglinger, wen was gegeben
                PersonSagt("Dingl_16", 0, 47, 2, 304, talkPoint);
                break;

            case 304:
                // Reaktion Dinglinger, wen was gegeben
                PersonSagt("Dingl_17", 0, 47, 2, 700, talkPoint);
                // Krabat darf dann zur Chodba raus
                mainFrame.actions[527] = true;
                break;

            // Dialog mit Dinglinger

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Dingl_69", 1000, 522, new int[]{522}, 620);
                Dialog.ExtendMC("Dingl_70", 522, 521, new int[]{521}, 621);
                Dialog.ExtendMC("Dingl_71", 521, 1000, null, 622);

                // 2. Frage
                Dialog.ExtendMC("Dingl_72", 1000, 524, new int[]{524}, 630);
                Dialog.ExtendMC("Dingl_73", 524, 523, new int[]{523}, 632);
                Dialog.ExtendMC("Dingl_74", 523, 1000, null, 633);

                // 3. Frage
                if (!mainFrame.actions[530]) {
                    Dialog.ExtendMC("Dingl_75", 1000, 1000, null, 640);
                } else {
                    Dialog.ExtendMC("Dingl_76", 1000, 526, new int[]{526}, 641);
                    Dialog.ExtendMC("Dingl_77", 526, 525, new int[]{525}, 642);
                    Dialog.ExtendMC("Dingl_78", 525, 1000, null, 643);
                }

                // 4. Frage
                Dialog.ExtendMC("Dingl_79", 1000, 1000, null, 645);

                // 5. Frage
                Dialog.ExtendMC("Dingl_80", 1000, 1000, null, 900);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
            case 701:
            case 801:
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
                // Dinglinger faengt Dialog an
                PersonSagt("Dingl_18", 0, 47, 2, 611, talkPoint);
                break;

            case 611:
                // Dinglinger faengt Dialog an (Teil 2)
                PersonSagt("Dingl_19", 0, 47, 2, 600, talkPoint);
                mainFrame.actions[520] = true; // Diesen Teil nicht wiederholen
                break;

            case 620:
                // Reaktion Dinglinger (1. Frage)
                PersonSagt("Dingl_20", 0, 47, 2, 600, talkPoint);
                break;

            case 621:
                // Reaktion Dinglinger
                PersonSagt("Dingl_21", 0, 47, 2, 600, talkPoint);
                break;

            case 622:
                // Reaktion Dinglinger
                PersonSagt("Dingl_22", 0, 47, 2, 600, talkPoint);
                break;

            case 630:
                // Reaktion Dinglinger (2. Frage)
                PersonSagt("Dingl_23", 0, 47, 2, 631, talkPoint);
                break;

            case 631:
                // Reaktion Dinglinger (2. Frage)
                PersonSagt("Dingl_24", 0, 47, 2, 600, talkPoint);
                break;

            case 632:
                // Reaktion Dinglinger
                PersonSagt("Dingl_25", 0, 47, 2, 600, talkPoint);
                break;

            case 633:
                // Reaktion Dinglinger
                PersonSagt("Dingl_26", 0, 47, 2, 600, talkPoint);
                break;

            // Antworten Frage 3 /////////////////////////////////////////
            case 640:
                // Reaktion Dinglinger (Frage 3)
                PersonSagt("Dingl_27", 0, 47, 2, 600, talkPoint);
                break;

            case 641:
                // Reaktion Dinglinger 
                PersonSagt("Dingl_28", 0, 47, 2, 600, talkPoint);
                break;

            case 642:
                // Reaktion Dinglinger
                PersonSagt("Dingl_29", 0, 47, 2, 600, talkPoint);
                break;

            case 643:
                // Reaktion Dinglinger
                PersonSagt("Dingl_30", 0, 47, 2, 600, talkPoint);
                break;

            // Antworten Frage 4 /////////////////////////////////////////
            case 645:
                // Reaktion Dinglinger (Frage 4)
                PersonSagt("Dingl_31", 0, 47, 2, 646, talkPoint);
                break;

            case 646:
                // Reaktion Dinglinger (Frage 4)
                PersonSagt("Dingl_32", 0, 47, 2, 600, talkPoint);
                break;


            // Dialog 2 - nach Uebergabe Breif von Zahrodnik //////////////////

            case 700:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // 1. Frage
                Dialog.ExtendMC("Dingl_81", 1000, 1000, null, 710);

                // 2. Frage
                Dialog.ExtendMC("Dingl_82", 1000, 1000, new int[]{528}, 720);

                // 3. Frage
                Dialog.ExtendMC("Dingl_83", 1000, 1000, null, 900);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 701;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            // Antworten Frage 1 ////////////
            case 710:
                // Reaktion Dinglinger
                PersonSagt("Dingl_33", 0, 47, 2, 711, talkPoint);
                break;

            case 711:
                // Reaktion Dinglinger
                PersonSagt("Dingl_34", 0, 47, 2, 700, talkPoint);
                break;

            // Antworten Frage 2 //////////// (selbstaend. Sequenz)
            case 720:
                // Reaktion Dinglinger
                PersonSagt("Dingl_35", 0, 47, 2, 721, talkPoint);
                break;

            case 721:
                // Reaktion Krabat
                KrabatSagt("Dingl_36", 0, 1, 2, 722);
                break;

            case 722:
                // Reaktion Dinglinger
                PersonSagt("Dingl_37", 0, 47, 2, 723, talkPoint);
                break;

            case 723:
                // Reaktion Dinglinger
                PersonSagt("Dingl_38", 0, 47, 2, 724, talkPoint);
                break;

            case 724:
                // Reaktion Krabat
                KrabatSagt("Dingl_39", 0, 1, 2, 725);
                break;

            case 725:
                // Reaktion Dinglinger
                PersonSagt("Dingl_40", 0, 47, 2, 726, talkPoint);
                break;

            case 726:
                // Reaktion Krabat
                KrabatSagt("Dingl_41", 0, 1, 2, 727);

                break;

            case 727:
                // Reaktion Dinglinger
                PersonSagt("Dingl_42", 0, 47, 2, 728, talkPoint);
                break;

            case 728:
                // Reaktion Dinglinger
                PersonSagt("Dingl_43", 0, 47, 2, 729, talkPoint);
                break;

            case 729:
                // Reaktion Krabat
                KrabatSagt("Dingl_44", 0, 1, 2, 730);
                break;

            case 730:
                // Reaktion Krabat
                KrabatSagt("Dingl_45", 0, 1, 2, 731);
                break;

            case 731:
                // Reaktion Dinglinger
                PersonSagt("Dingl_46", 0, 47, 2, 732, talkPoint);
                break;

            case 732:
                // Reaktion Dinglinger
                PersonSagt("Dingl_47", 0, 47, 2, 733, talkPoint);
                break;

            case 733:
                // Reaktion Krabat
                KrabatSagt("Dingl_48", 0, 1, 2, 734);
                break;

            case 734:
                // Reaktion Dinglinger
                PersonSagt("Dingl_49", 0, 47, 2, 800, talkPoint);
                break;


            // Dialog 3 - nach Frage 2 des Dialogs 2 ///////

            case 800:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Dingl_84", 1000, 1000, null, 810);

                // 2. Frage
                Dialog.ExtendMC("Dingl_85", 1000, 1000, null, 820);

                // 3. Frage
                Dialog.ExtendMC("Dingl_86", 1000, 631, null, 830);

                // 4. Frage
                if (!mainFrame.actions[602] && !mainFrame.actions[951]) {
                    Dialog.ExtendMC("Dingl_87", 1000, 1000, null, 840);
                }

                // 5. Frage
                if (!mainFrame.actions[640] && !mainFrame.actions[641]) {
                    Dialog.ExtendMC("Dingl_88", 1000, 1000, null, 850);
                }

                // 6. Frage
                Dialog.ExtendMC("Dingl_89", 1000, 529, new int[]{529}, 860);
                Dialog.ExtendMC("Dingl_90", 529, 1000, null, 900);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 801;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            // Antworten Frage 1 ////////////
            case 810:
            case 811:
            case 812:
            case 813:
                // Reaktion Dinglinger
                if (nextActionID == 810 && !mainFrame.actions[635]) {
                    PersonSagt("Dingl_50", 0, 47, 2, 811, talkPoint);
                    break;
                }
                if (nextActionID == 810) {
                    nextActionID = 811;
                }
                if (nextActionID == 811 && !mainFrame.actions[636]) {
                    PersonSagt("Dingl_51", 0, 47, 2, 812, talkPoint);
                    break;
                }
                if (nextActionID == 811) {
                    nextActionID = 812;
                }
                if (nextActionID == 812 && !mainFrame.actions[637]) {
                    PersonSagt("Dingl_52", 0, 47, 2, 813, talkPoint);
                    break;
                }
                if (nextActionID == 812) {
                    nextActionID = 813;
                }
                if (!mainFrame.actions[638]) {
                    PersonSagt("Dingl_53", 0, 47, 2, 800, talkPoint);
                    break;
                }
                nextActionID = 800;
                break;

            // Antworten Frage 2 ////////////
            case 820:
                // Reaktion Dinglinger
                PersonSagt("Dingl_54", 0, 47, 2, 821, talkPoint);
                break;

            case 821:
                // Reaktion Dinglinger
                PersonSagt("Dingl_55", 0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 3 ////////////
            case 830:
                // Reaktion Dinglinger
                PersonSagt("Dingl_56", 0, 47, 2, 831, talkPoint);
                break;

            case 831:
                // Reaktion Dinglinger
                PersonSagt("Dingl_57", 0, 47, 2, 832, talkPoint);
                break;

            case 832:
                // Reaktion Dinglinger
                PersonSagt("Dingl_58", 0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 4 ////////////
            case 840:
                // Reaktion Dinglinger
                PersonSagt("Dingl_59", 0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 5 ////////////
            case 850:
                // Reaktion Dinglinger
                PersonSagt("Dingl_60", 0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 6 ////////////
            case 860:
                // Reaktion Dinglinger
                PersonSagt("Dingl_61", 0, 47, 2, 861, talkPoint);
                break;

            case 861:
                // Dinglinger gibt Erlaubnis
                welcheAnim = 5;
                nextActionID = 862;
                break;

            case 862:
                // Krabat setzt ein
                if (animRueckgabe == 4) {
                    mainFrame.krabat.nAnimation = 91;
                    nextActionID = 863;
                }
                break;

            case 863:
                // warten, bis beide fertig sind
                if (animRueckgabe == 100) {
                    welcheAnim = 1;
                }
                if (mainFrame.krabat.nAnimation != 0 || welcheAnim != 1) {
                    break;
                }
                nextActionID = 865;
                break;

            case 865:
                // Reaktion Dinglinger
                PersonSagt("Dingl_62", 0, 47, 2, 900, talkPoint);
                // Erlaubnisschreiben zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(31);
                break;


            // ENDE aller Dialoge !!!!!! /////////////////////////////////
            case 900:
                // MC beenden, wenn zuende gelabert...
                // zuhoerenden Dinglinger wieder ausschalten
                welcheAnim = 0;
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 1000:
                // Dinglinger "ich habe fertig"
                PersonSagt("Dingl_63", 0, 47, 2, 1010, talkPoint);
                break;

            case 1010:
                // ab vor die Schatzkammer
                NeuesBild(181, locationID);
                break;

            case 1100:
                // Dinglinger Endsequenz
                welcheAnim = 4;
                PersonSagt("Dingl_64", 0, 47, 2, 1105, dinglingerwalk.evalTalkPoint());
                // altes unbenoetigtes Inevntar loeschen
                mainFrame.inventory.vInventory.removeAllElements();
                // Schuessel auch wirklich geben
                mainFrame.inventory.vInventory.addElement(60);
                // Inventar neu anlegen, brauch nur Floete, Feuersteine und Schuessel
                mainFrame.inventory.vInventory.addElement(1);
                mainFrame.inventory.vInventory.addElement(12);
                break;

            case 1105:
                // Krabat nimmt schuessel
                mainFrame.krabat.nAnimation = 31;
                nextActionID = 1110;
                Counter = 4;
                break;

            case 1110:
                // Krabat spricht
                if (--Counter < 1) {
                    welcheAnim = 0;
                }
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                KrabatSagt("Dingl_65", 0, 1, 2, 1120);
                break;

            case 1120:
                // Dinglinger spricht
                PersonSagt("Dingl_66", 0, 47, 2, 1130, dinglingerwalk.evalTalkPoint());
                break;

            case 1130:
                // Erzaehler spricht
                zeigeGrossesBild = true;
                mainFrame.soundPlayer.PlayFile("sfx-dd/modrja.wav");
                BackgroundMusicPlayer.getInstance().stop();
                PersonSagt("Dingl_67", 0, 54, 2, 1140, new GenericPoint(320, 200));
                break;

            case 1140:
                // Krabat spricht
                KrabatSagt("Dingl_68", 0, 3, 2, 1150);
                break;

            case 1150:
                // Gehe zu Doma Teil 4 (aendert sich sicher noch)
                // Kleidung wieder zurueckwechseln
                zeigeGrossesBild = false;
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                NeuesBild(203, locationID);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}


/* TEXTE

   /// AKTIONEN beim zeug geben ///////////////////////////////////

   Derje, n#etko mam p#lony kusk #zeleza, z kotreho#z kopiju zhotowju.
   Tole su m#ery #skl#e - te te#z trjebam. Prawje tak.
   Podpisana a zyglowana dowolnos#c Awgusta. Pilny kadla.
   To je jeno#z zyglowana dowolnos#c. N#etko trjebam tu hi#s#ce podpismo.
   To je jeno#z podpisana dowolnos#c. N#etko trjebam hi#s#ce kralowski pje#kat na dowolnos#c.
   Aha, klu#k za komoru pok#ladow sy te#z hi#s#ce wobstara#l.

*/