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
import de.codengine.krabat.anims.Dinglingerwalk;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

import java.util.Objects;

public class Dingl extends Mainloc {
    private GenericImage background;
    private GenericImage blido;
    private GenericImage tworba;
    private GenericImage kombinacija;
    private final Dinglinger dinglinger;
    private final Dinglingerwalk dinglingerwalk;
    private final Multiple2 Dialog;

    private final GenericPoint talkPoint;
    private final Borderrect reDinglinger;

    private boolean setAnim = false;

    private boolean sitzenderDinglinger = true;

    private int welcheAnim = 0;
    private int animRueckgabe = 0;

    private boolean walkReady = true;

    private int Counter = 0;

    private boolean zeigeGrossesBild = false;

    // Konstanten - Rects
    private static final Borderrect obererAusgang
            = new Borderrect(17, 122, 92, 248);
    private static final Borderrect rechterAusgang
            = new Borderrect(426, 123, 526, 327);
    private static final Borderrect kunstwerk
            = new Borderrect(465, 365, 639, 479);
    private static final Borderrect blidoRect
            = new Borderrect(176, 328, 235, 390);

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

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 350;
        mainFrame.krabat.zoomf = 1.84f;
        mainFrame.krabat.defScale = -60;

        dinglinger = new Dinglinger(mainFrame);

        talkPoint = new GenericPoint();
        talkPoint.x = dinglLO.x + Dinglinger.Breite / 2;
        talkPoint.y = dinglLO.y - 50;

        reDinglinger = new Borderrect(dinglLO.x, dinglLO.y, dinglLO.x + Dinglinger.Breite, dinglLO.y + Dinglinger.Hoehe);

        dinglingerwalk = new Dinglingerwalk(mainFrame);

        dinglingerwalk.maxx = 0;
        dinglingerwalk.zoomf = 1f;
        dinglingerwalk.defScale = 0;

        dinglingerwalk.SetDinglingerPos(dinglFeet);
        dinglingerwalk.SetFacing(9);

        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);

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
                mainFrame.Actions[i] = false;
            }
        }

        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 75, 20, 350, 255, 336));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 70, 20, 70, 337, 420));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(176, 480, 195, 469, 337, 413));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(244, 468, 306, 460, 414, 432));

        mainFrame.wegSucher.ClearMatrix(4);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(0, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);

        InitImages();

        sitzenderDinglinger = true;

        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 140: // von Saal aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(443, 344));
                mainFrame.krabat.SetFacing(9);
                break;
            case 142: // von Chodba aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(50, 265));
                mainFrame.krabat.SetFacing(6);
                break;
            case 181: // von Poklad - Location aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(320, 360));
                mainFrame.krabat.SetFacing(3);
                sitzenderDinglinger = false;
                setAnim = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/dingl/dingl.gif");
        blido = getPicture("gfx-dd/dingl/blido.gif");
        tworba = getPicture("gfx-dd/dingl/tworba.gif");

        kombinacija = getPicture("gfx-dd/dingl/kombinacija.gif");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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
            if (setAnim) {
                mainFrame.fPlayAnim = true;
            }
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

        // Dinglinger Hintergrund loeschen
        if (sitzenderDinglinger) {
            // sitzender Dinglinger
            g.setClip(dinglLO.x, dinglLO.y, Dinglinger.Breite, Dinglinger.Hoehe);
            g.drawImage(background, 0, 0);
        } else {
            // stehender Dinglinger

            // Hintergrund loeschen
            Borderrect temp = dinglingerwalk.DinglingerRect();
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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

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
                mainFrame.wegGeher.SetzeNeuenWeg(pTxxxx);
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

                // neuen Punkt wg. Ueberschneidung
                GenericPoint pTxxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Chodba gehen ?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTxxxx = pExitUp;
                    } else {
                        // es wird nach unten verlassen
                        pTxxxx = new GenericPoint(kt.x, pExitUp.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Saal gehen ?
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTxxxx = pExitRight;
                    } else {
                        // es wird nach unten verlassen
                        pTxxxx = new GenericPoint(kt.x, pExitRight.y);
                    }

                    if (mainFrame.dClick) {
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

                mainFrame.wegGeher.SetzeNeuenWeg(pTxxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Dinglinger reden
                if (reDinglinger.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pDinglinger);
                    mainFrame.repaint();
                    return;
                }

                // Kelch und Krug benutzen
                if (blidoRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBlido);
                    mainFrame.repaint();
                    return;
                }

                // Kunstwerk benutzen
                if (kunstwerk.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKunstwerk);
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    reDinglinger.IsPointInRect(pTemp) ||
                    kunstwerk.IsPointInRect(pTemp) ||
                    blidoRect.IsPointInRect(pTemp);

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
            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if (reDinglinger.IsPointInRect(pTemp) ||
                    kunstwerk.IsPointInRect(pTemp) ||
                    blidoRect.IsPointInRect(pTemp)) {
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
                // Dinglinger anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00000"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00001"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00002"),
                        fDingl, 3, 0, 0);
                break;

            case 2:
                // Kanne anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00003"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00004"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00005"),
                        fKanne, 3, 0, 0);
                break;

            case 3:
                // Kanne benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00006"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00007"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00008"),
                        fKanne, 3, 0, 0);
                break;

            case 4:
                // Kunstwerk mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00009"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00010"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00011"),
                        fKunstwerk, 3, 0, 0);
                break;

            case 5:
                // Dingl. Werk anschauen
                // zufallsZahl zwischen null und 1 
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00012"),
                                Start.stringManager.getTranslation("Loc3_Dingl_00013"),
                                Start.stringManager.getTranslation("Loc3_Dingl_00014"),
                                fKunstwerk, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00015"),
                                Start.stringManager.getTranslation("Loc3_Dingl_00016"),
                                Start.stringManager.getTranslation("Loc3_Dingl_00017"),
                                fKunstwerk, 3, 0, 0);
                        break;
                }
                break;

            case 50:
                // Krabat beginnt MC (Dinglinger benutzen)
                mainFrame.krabat.SetFacing(9);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);

                // hier zuhoerenden Dinglinger einschalten
                welcheAnim = 1;

                // Hat Krabat schon Dialog 3 mit Dinglinger angefangen (letzter)
                if (!mainFrame.Actions[528]) {
                    // Hat Krabat schon Breif von Zahrd. gegeben ? (MC1 oder MC2)
                    if (!mainFrame.Actions[527]) {
                        // faengt Dinglinger (erstes Mal) an zu reden ?
                        if (mainFrame.Actions[520]) {
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
                if (mainFrame.Actions[527]) {
                    // darf zur Chodba gehen (hat von Dingl. Prikaz bekommen)
                    NeuesBild(142, locationID);
                } else {
                    // 3 mal darf Kr. versuchen rauszugehen
                    if (!mainFrame.Actions[650] || !mainFrame.Actions[651] || !mainFrame.Actions[652]) {
                        mainFrame.fPlayAnim = true;
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        nextActionID = 300;
                        if (mainFrame.Actions[651]) {
                            mainFrame.Actions[652] = true;
                        }
                        if (mainFrame.Actions[650]) {
                            mainFrame.Actions[651] = true;
                        }
                        if (!mainFrame.Actions[650]) {
                            mainFrame.Actions[650] = true;
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
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 138;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                if (mainFrame.Actions[520]) {
                    // hat schon um Wein gebettelt
                    PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00018"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00019"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00020"),
                            0, 47, 5, 163, talkPoint);
                } else {
                    // hat noch nichts vom Wein gesagt
                    PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00021"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00022"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00023"),
                            0, 47, 5, 163, talkPoint);
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
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 139;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00024"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00025"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00026"),
                        0, 47, 0, 195, talkPoint);
                mainFrame.Actions[635] = true;
                mainFrame.inventory.vInventory.removeElement(48);
                break;

            case 170: // Gib Skizze an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 140;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00027"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00028"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00029"),
                        0, 47, 0, 195, talkPoint);
                mainFrame.Actions[636] = true;
                mainFrame.inventory.vInventory.removeElement(50);
                break;

            case 175: // Gib Dowolnosc an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 141;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00030"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00031"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00032"),
                        0, 47, 0, 195, talkPoint);
                mainFrame.Actions[637] = true;
                mainFrame.inventory.vInventory.removeElement(34);
                break;

            case 180: // Gib Kluc an Dingl
                mainFrame.krabat.SetFacing(fDingl);
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 137;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00033"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00034"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00035"),
                        0, 47, 0, 195, talkPoint);
                mainFrame.invCursor = false;
                mainFrame.Actions[638] = true;
                mainFrame.inventory.vInventory.removeElement(47);
                break;

            case 185:
                // Reaktion Dinglinger auf Give Dowol njepodpis.
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00036"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00037"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00038"),
                        fDingl, 47, 0, 900, talkPoint);
                break;

            case 190:
                // Reaktion Dinglinger auf Give Dowol njezyg.
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00039"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00040"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00041"),
                        fDingl, 47, 0, 900, talkPoint);
                break;

            case 195:
                // kontrollieren, ob alles zusammen
                if (mainFrame.Actions[635] && mainFrame.Actions[636] && mainFrame.Actions[637] && mainFrame.Actions[638]) {
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00042"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00043"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00044"),
                        0, 47, 2, 900, talkPoint);
                break;

            case 303:
                // Reaktion Dinglinger, wen was gegeben
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00045"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00046"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00047"),
                        0, 47, 2, 304, talkPoint);
                break;

            case 304:
                // Reaktion Dinglinger, wen was gegeben
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00048"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00049"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00050"),
                        0, 47, 2, 700, talkPoint);
                // Krabat darf dann zur Chodba raus
                mainFrame.Actions[527] = true;
                break;

            // Dialog mit Dinglinger

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00051"), 1000, 522, new int[]{522}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00052"), 522, 521, new int[]{521}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00053"), 521, 1000, null, 622);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00054"), 1000, 524, new int[]{524}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00055"), 524, 523, new int[]{523}, 632);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00056"), 523, 1000, null, 633);

                    // 3. Frage
                    if (!mainFrame.Actions[530]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00057"), 1000, 1000, null, 640);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00058"), 1000, 526, new int[]{526}, 641);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00059"), 526, 525, new int[]{525}, 642);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00060"), 525, 1000, null, 643);
                    }

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00061"), 1000, 1000, null, 645);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00062"), 1000, 1000, null, 900);
                }
                // Niedersorbische Fragen
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00063"), 1000, 522, new int[]{522}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00064"), 522, 521, new int[]{521}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00065"), 521, 1000, null, 622);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00066"), 1000, 524, new int[]{524}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00067"), 524, 523, new int[]{523}, 632);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00068"), 523, 1000, null, 633);

                    // 3. Frage
                    if (!mainFrame.Actions[530]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00069"), 1000, 1000, null, 640);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00070"), 1000, 526, new int[]{526}, 641);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00071"), 526, 525, new int[]{525}, 642);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00072"), 525, 1000, null, 643);
                    }

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00073"), 1000, 1000, null, 645);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00074"), 1000, 1000, null, 900);
                }
                // Deutsche Fragen
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00075"), 1000, 522, new int[]{522}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00076"), 522, 521, new int[]{521}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00077"), 521, 1000, null, 622);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00078"), 1000, 524, new int[]{524}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00079"), 524, 523, new int[]{523}, 632);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00080"), 523, 1000, null, 633);

                    // 3. Frage
                    if (!mainFrame.Actions[530]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00081"), 1000, 1000, null, 640);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00082"), 1000, 526, new int[]{526}, 641);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00083"), 526, 525, new int[]{525}, 642);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00084"), 525, 1000, null, 643);
                    }

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00085"), 1000, 1000, null, 645);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00086"), 1000, 1000, null, 900);
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
                // Dinglinger faengt Dialog an
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00087"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00088"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00089"),
                        0, 47, 2, 611, talkPoint);
                break;

            case 611:
                // Dinglinger faengt Dialog an (Teil 2)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00090"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00091"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00092"),
                        0, 47, 2, 600, talkPoint);
                mainFrame.Actions[520] = true; // Diesen Teil nicht wiederholen
                break;

            case 620:
                // Reaktion Dinglinger (1. Frage)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00093"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00094"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00095"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 621:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00096"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00097"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00098"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 622:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00099"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00100"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00101"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 630:
                // Reaktion Dinglinger (2. Frage)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00102"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00103"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00104"),
                        0, 47, 2, 631, talkPoint);
                break;

            case 631:
                // Reaktion Dinglinger (2. Frage)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00105"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00106"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00107"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 632:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00108"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00109"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00110"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 633:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00111"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00112"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00113"),
                        0, 47, 2, 600, talkPoint);
                break;

            // Antworten Frage 3 /////////////////////////////////////////
            case 640:
                // Reaktion Dinglinger (Frage 3)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00114"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00115"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00116"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 641:
                // Reaktion Dinglinger 
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00117"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00118"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00119"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 642:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00120"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00121"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00122"),
                        0, 47, 2, 600, talkPoint);
                break;

            case 643:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00123"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00124"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00125"),
                        0, 47, 2, 600, talkPoint);
                break;

            // Antworten Frage 4 /////////////////////////////////////////
            case 645:
                // Reaktion Dinglinger (Frage 4)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00126"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00127"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00128"),
                        0, 47, 2, 646, talkPoint);
                break;

            case 646:
                // Reaktion Dinglinger (Frage 4)
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00129"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00130"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00131"),
                        0, 47, 2, 600, talkPoint);
                break;


            // Dialog 2 - nach Uebergabe Breif von Zahrodnik //////////////////

            case 700:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00132"), 1000, 1000, null, 710);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00133"), 1000, 1000, new int[]{528}, 720);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00134"), 1000, 1000, null, 900);
                }
                // Niedersorbische Fragen
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00135"), 1000, 1000, null, 710);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00136"), 1000, 1000, new int[]{528}, 720);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00137"), 1000, 1000, null, 900);
                }
                // Deutsche Fragen
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00138"), 1000, 1000, null, 710);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00139"), 1000, 1000, new int[]{528}, 720);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00140"), 1000, 1000, null, 900);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 701;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 701:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            // Antworten Frage 1 ////////////
            case 710:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00141"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00142"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00143"),
                        0, 47, 2, 711, talkPoint);
                break;

            case 711:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00144"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00145"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00146"),
                        0, 47, 2, 700, talkPoint);
                break;

            // Antworten Frage 2 //////////// (selbstaend. Sequenz)
            case 720:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00147"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00148"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00149"),
                        0, 47, 2, 721, talkPoint);
                break;

            case 721:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00150"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00151"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00152"),
                        0, 1, 2, 722);
                break;

            case 722:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00153"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00154"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00155"),
                        0, 47, 2, 723, talkPoint);
                break;

            case 723:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00156"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00157"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00158"),
                        0, 47, 2, 724, talkPoint);
                break;

            case 724:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00159"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00160"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00161"),
                        0, 1, 2, 725);
                break;

            case 725:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00162"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00163"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00164"),
                        0, 47, 2, 726, talkPoint);
                break;

            case 726:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00165"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00166"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00167"),
                        0, 1, 2, 727);

                break;

            case 727:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00168"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00169"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00170"),
                        0, 47, 2, 728, talkPoint);
                break;

            case 728:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00171"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00172"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00173"),
                        0, 47, 2, 729, talkPoint);
                break;

            case 729:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00174"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00175"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00176"),
                        0, 1, 2, 730);
                break;

            case 730:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00177"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00178"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00179"),
                        0, 1, 2, 731);
                break;

            case 731:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00180"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00181"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00182"),
                        0, 47, 2, 732, talkPoint);
                break;

            case 732:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00183"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00184"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00185"),
                        0, 47, 2, 733, talkPoint);
                break;

            case 733:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00186"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00187"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00188"),
                        0, 1, 2, 734);
                break;

            case 734:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00189"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00190"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00191"),
                        0, 47, 2, 800, talkPoint);
                break;


            // Dialog 3 - nach Frage 2 des Dialogs 2 ///////

            case 800:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00192"), 1000, 1000, null, 810);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00193"), 1000, 1000, null, 820);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00194"), 1000, 631, null, 830);

                    // 4. Frage
                    if (!mainFrame.Actions[602] && !mainFrame.Actions[951]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00195"), 1000, 1000, null, 840);
                    }

                    // 5. Frage
                    if (!mainFrame.Actions[640] && !mainFrame.Actions[641]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00196"), 1000, 1000, null, 850);
                    }

                    // 6. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00197"), 1000, 529, new int[]{529}, 860);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00198"), 529, 1000, null, 900);
                }

                // Niedersorbische Fragen
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00199"), 1000, 1000, null, 810);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00200"), 1000, 1000, null, 820);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00201"), 1000, 631, null, 830);

                    // 4. Frage
                    if (!mainFrame.Actions[602] && !mainFrame.Actions[951]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00202"), 1000, 1000, null, 840);
                    }

                    // 5. Frage
                    if (!mainFrame.Actions[640] && !mainFrame.Actions[641]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00203"), 1000, 1000, null, 850);
                    }

                    // 6. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00204"), 1000, 529, new int[]{529}, 860);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00205"), 529, 1000, null, 900);
                }

                // Deutsche Fragen
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00206"), 1000, 1000, null, 810);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00207"), 1000, 1000, null, 820);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00208"), 1000, 631, null, 830);

                    // 4. Frage
                    if (!mainFrame.Actions[602] && !mainFrame.Actions[951]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00209"), 1000, 1000, null, 840);
                    }

                    // 5. Frage
                    if (!mainFrame.Actions[640] && !mainFrame.Actions[641]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00210"), 1000, 1000, null, 850);
                    }

                    // 6. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00211"), 1000, 529, new int[]{529}, 860);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Dingl_00212"), 529, 1000, null, 900);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 801;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 801:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            // Antworten Frage 1 ////////////
            case 810:
            case 811:
            case 812:
            case 813:
                // Reaktion Dinglinger
                if (nextActionID == 810 && !mainFrame.Actions[635]) {
                    PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00213"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00214"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00215"),
                            0, 47, 2, 811, talkPoint);
                    break;
                }
                if (nextActionID == 810) {
                    nextActionID = 811;
                }
                if (nextActionID == 811 && !mainFrame.Actions[636]) {
                    PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00216"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00217"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00218"),
                            0, 47, 2, 812, talkPoint);
                    break;
                }
                if (nextActionID == 811) {
                    nextActionID = 812;
                }
                if (nextActionID == 812 && !mainFrame.Actions[637]) {
                    PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00219"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00220"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00221"),
                            0, 47, 2, 813, talkPoint);
                    break;
                }
                if (nextActionID == 812) {
                    nextActionID = 813;
                }
                if (!mainFrame.Actions[638]) {
                    PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00222"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00223"),
                            Start.stringManager.getTranslation("Loc3_Dingl_00224"),
                            0, 47, 2, 800, talkPoint);
                    break;
                }
                nextActionID = 800;
                break;

            // Antworten Frage 2 ////////////
            case 820:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00225"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00226"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00227"),
                        0, 47, 2, 821, talkPoint);
                break;

            case 821:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00228"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00229"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00230"),
                        0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 3 ////////////
            case 830:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00231"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00232"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00233"),
                        0, 47, 2, 831, talkPoint);
                break;

            case 831:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00234"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00235"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00236"),
                        0, 47, 2, 832, talkPoint);
                break;

            case 832:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00237"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00238"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00239"),
                        0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 4 ////////////
            case 840:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00240"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00241"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00242"),
                        0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 5 ////////////
            case 850:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00243"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00244"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00245"),
                        0, 47, 2, 800, talkPoint);
                break;

            // Antworten Frage 6 ////////////
            case 860:
                // Reaktion Dinglinger
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00246"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00247"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00248"),
                        0, 47, 2, 861, talkPoint);
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00249"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00250"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00251"),
                        0, 47, 2, 900, talkPoint);
                // Erlaubnisschreiben zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(31);
                break;


            // ENDE aller Dialoge !!!!!! /////////////////////////////////
            case 900:
                // MC beenden, wenn zuende gelabert...
                // zuhoerenden Dinglinger wieder ausschalten
                welcheAnim = 0;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 1000:
                // Dinglinger "ich habe fertig"
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00252"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00253"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00254"),
                        0, 47, 2, 1010, talkPoint);
                break;

            case 1010:
                // ab vor die Schatzkammer
                NeuesBild(181, locationID);
                break;

            case 1100:
                // Dinglinger Endsequenz
                welcheAnim = 4;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00255"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00256"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00257"),
                        0, 47, 2, 1105, dinglingerwalk.evalTalkPoint());
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
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00258"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00259"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00260"),
                        0, 1, 2, 1120);
                break;

            case 1120:
                // Dinglinger spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00261"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00262"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00263"),
                        0, 47, 2, 1130, dinglingerwalk.evalTalkPoint());
                break;

            case 1130:
                // Erzaehler spricht
                zeigeGrossesBild = true;
                mainFrame.wave.PlayFile("sfx-dd/modrja.wav");
                BackgroundMusicPlayer.getInstance().stop();
                PersonSagt(Start.stringManager.getTranslation("Loc3_Dingl_00264"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00265"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00266"),
                        0, 54, 2, 1140, new GenericPoint(320, 200));
                break;

            case 1140:
                // Krabat spricht
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Dingl_00267"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00268"),
                        Start.stringManager.getTranslation("Loc3_Dingl_00269"),
                        0, 3, 2, 1150);
                break;

            case 1150:
                // Gehe zu Doma Teil 4 (aendert sich sicher noch)
                // Kleidung wieder zurueckwechseln
                zeigeGrossesBild = false;
                mainFrame.Actions[511] = false;
                mainFrame.Actions[850] = false;
                NeuesBild(203, locationID);
                break;

            default:
                System.out.println("Falsche Action-ID !");
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