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
import de.codengine.krabat.anims.Handrij;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Zahrodnik extends Mainloc {
    private GenericImage background;
    private GenericImage hrod;
    private final Handrij handrij;
    private final Multiple2 Dialog;

    private boolean handrijHoertZu = false;
    private boolean handrijSchreibt = false;
    private boolean handrijGibt = false;

    private final Borderrect reZahrodnik;
    private final GenericPoint talkPoint;

    private int tCounter = 0;

    // Konstanten - Rects
    private static final Borderrect ausgangUnten
            = new Borderrect(596, 377, 639, 479);
    private static final Borderrect skizze
            = new Borderrect(525, 162, 600, 224);
    private static final Borderrect papier
            = new Borderrect(347, 322, 390, 355);
    private static final Borderrect rectHrod
            = new Borderrect(0, 352, 280, 479);
    private static final Borderrect budka
            = new Borderrect(388, 168, 485, 325);
    private static final Borderrect pjerjo
            = new Borderrect(222, 208, 233, 227);
    private static final Borderrect dokumenty
            = new Borderrect(246, 229, 266, 246);
    private static final Borderrect lookHrod
            = new Borderrect(36, 369, 160, 479);

    // Konstante Points
    private static final GenericPoint pExitUnten = new GenericPoint(639, 431);
    private static final GenericPoint pZahrodnik = new GenericPoint(315, 352);
    private static final GenericPoint pZahrUnten = new GenericPoint(325, 375);
    private static final GenericPoint pSkizze = new GenericPoint(555, 324);
    private static final GenericPoint pPapier = new GenericPoint(333, 331);
    private static final GenericPoint pBudka = new GenericPoint(435, 316);
    private static final GenericPoint pPjerjo = new GenericPoint(283, 302);
    private static final GenericPoint pDokumenty = new GenericPoint(283, 302);
    private static final GenericPoint pHrod = new GenericPoint(158, 428);
    private static final GenericPoint handrijFeet = new GenericPoint(242, 339);

    // Konstante ints
    private static final int fDokumente = 9;
    private static final int fZahrod = 9;
    private static final int fZahrodLook = 12;
    private static final int fHrod = 6;
    private static final int fPapier = 3;
    private static final int fBudka = 12;
    private static final int fSkizze = 12;
    private static final int fFeder = 9;

    private boolean schnauzeZahrod = false;

    private int initCounter = 15;
    private boolean initSound = true;  // defaultmaessig nicht abspielen, nur wenn kein "load", dann aktivieren


    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zahrodnik(Start caller, int oldLocation) {
        super(caller, 162);

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 4.6f;
        mainFrame.krabat.defScale = -100;

        handrij = new Handrij(mainFrame);
        Dialog = new Multiple2(mainFrame);

        talkPoint = new GenericPoint(handrijFeet.x, handrijFeet.y - Handrij.Hoehe - 50);
        reZahrodnik = new Borderrect(handrijFeet.x - Handrij.Breite / 2, handrijFeet.y - Handrij.Hoehe,
                handrijFeet.x + Handrij.Breite / 2, handrijFeet.y);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(327, 371, 639, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(505, 333, 600, 370));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(80, 326, 20, 326, 375, 450));

        mainFrame.wegSucher.ClearMatrix(3);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(0, 2);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 160:
                // von Panorama aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(557, 431));
                mainFrame.krabat.SetFacing(9);
                initSound = false;  // nur hier Sound abspielen
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/zahrod/zahrod.gif");
        hrod = getPicture("gfx-dd/zahrod/hrod.gif");
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Tuereintrittssound abspielen
        if (!initSound) {
            initSound = true;
            mainFrame.wave.PlayFile("sfx/vdurjezu.wav");
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
        g.drawImage(background, 0, 0, null);

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Handrij zeichnen
        g.setClip(reZahrodnik.lo_point.x, reZahrodnik.lo_point.y, Handrij.Breite, Handrij.Hoehe);
        g.drawImage(background, 0, 0, null);
        handrij.drawHandrij(g, TalkPerson, handrijHoertZu, handrijSchreibt, handrijGibt);

        // Krabat einen Schritt laufen lassen
        mainFrame.wegGeher.GeheWeg();

        // Sound abspielen
        evalSound();

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

        // hinter Schloss ? (nur Clipping - Region wird neugezeichnet)
        if (rectHrod.IsPointInRect(pKrTemp)) {
            g.drawImage(hrod, 0, 354, null);
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

                // Ausreden fuer Zahrodnik
                if (reZahrodnik.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pZahrUnten;
                }

                // Ausreden fuer hrod
                if (lookHrod.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTemp = pHrod;
                }

                // Ausreden fuer blatt
                if (papier.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = pPapier;
                }

                // Ausreden fuer budka
                if (budka.IsPointInRect(pTemp) && !reZahrodnik.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 170;
                    pTemp = pBudka;
                }

                // Ausreden fuer skizze
                if (skizze.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 175;
                    pTemp = pSkizze;
                }

                // Ausreden fuer pjerjo
                if (pjerjo.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 180;
                    pTemp = pPjerjo;
                }

                // Ausreden fuer dokumenty
                if (dokumenty.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 185;
                    pTemp = pDokumenty;
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

                // zu Panorama gehen ?
                if (ausgangUnten.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen, Achtung: ist jetzt nach rechts!!!
                    if (!ausgangUnten.IsPointInRect(kt)) {
                        pTemp = pExitUnten;
                    } else {
                        // es wird nach rechts! verlassen
                        pTemp = new GenericPoint(pExitUnten.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Kirche ansehen
                // if (brKirche.IsPointInRect (pTemp) == true)
                // {
                //   nextActionID = 1;
                //  pTemp = Pkirche;
                // }

                // Handrij ansehen
                if (reZahrodnik.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pZahrUnten;
                }

                // hrod ansehen
                if (lookHrod.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pHrod;
                }

                // blatt ansehen
                if (papier.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pPapier;
                }

                // budka ansehen
                if (budka.IsPointInRect(pTemp) && !reZahrodnik.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pBudka;
                }

                // skizze ansehen
                if (skizze.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pSkizze;
                }

                // pjerjo ansehen
                if (pjerjo.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pPjerjo;
                }

                // dokumenty ansehen
                if (dokumenty.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pDokumenty;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Zahrodnik reden
                if (reZahrodnik.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pZahrUnten);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangUnten.IsPointInRect(pTemp)) {
                    return;
                }

                // hrod mitnehmen
                if (lookHrod.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pHrod);
                    mainFrame.repaint();
                    return;
                }

                // blatt mitnehmen
                if (papier.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(pPapier);
                    mainFrame.repaint();
                    return;
                }

                // budka ansehen
                if (budka.IsPointInRect(pTemp) && !reZahrodnik.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBudka);
                    mainFrame.repaint();
                    return;
                }

                // skizze ansehen
                if (skizze.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSkizze);
                    mainFrame.repaint();
                    return;
                }

                // pjerjo ansehen
                if (pjerjo.IsPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.wegGeher.SetzeNeuenWeg(pPjerjo);
                    mainFrame.repaint();
                    return;
                }

                // dokumenty ansehen
                if (dokumenty.IsPointInRect(pTemp)) {
                    nextActionID = 80;
                    mainFrame.wegGeher.SetzeNeuenWeg(pDokumenty);
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || skizze.IsPointInRect(pTemp) ||
                    papier.IsPointInRect(pTemp) || reZahrodnik.IsPointInRect(pTemp) ||
                    budka.IsPointInRect(pTemp) || pjerjo.IsPointInRect(pTemp) ||
                    dokumenty.IsPointInRect(pTemp) || lookHrod.IsPointInRect(pTemp);

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
            if (ausgangUnten.IsPointInRect(pTemp)) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.Cright);  // Ausgang jetzt rechts!!!
                    Cursorform = 6;
                }
                return;
            }

            if (skizze.IsPointInRect(pTemp) ||
                    papier.IsPointInRect(pTemp) || reZahrodnik.IsPointInRect(pTemp) ||
                    budka.IsPointInRect(pTemp) || pjerjo.IsPointInRect(pTemp) ||
                    dokumenty.IsPointInRect(pTemp) || lookHrod.IsPointInRect(pTemp)) {
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
    public void evalMouseExitEvent(GenericMouseEvent e) {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent(e);
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
                // Zahrodnik anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00000"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00001"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00002"),
                        fZahrodLook, 3, 0, 0);
                break;

            case 2:
                // Hrod anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00003"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00004"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00005"),
                        fHrod, 3, 0, 0);
                break;

            case 3:
                // Papier anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00006"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00007"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00008"),
                        fPapier, 3, 0, 0);
                break;

            case 4:
                // budka anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00009"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00010"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00011"),
                        fBudka, 3, 0, 0);
                break;

            case 5:
                // skizze anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00012"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00013"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00014"),
                        fSkizze, 3, 0, 0);
                break;

            case 6:
                // feder anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00015"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00016"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00017"),
                        fFeder, 3, 0, 0);
                break;

            case 7:
                // dokuments anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00018"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00019"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00020"),
                        fDokumente, 3, 0, 0);
                break;

            case 50:
                // zuerstmal richtig zu Zahrodnik hinlaufen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pZahrodnik);
                nextActionID = 53;
                break;

            case 53:
                // Krabat beginnt MC (Zahrodnik benutzen)
                schnauzeZahrod = true;  // Sound schon hier abschalten
                mainFrame.krabat.SetFacing(fZahrod);
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.Actions[530]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 55:
                // Hrod mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00021"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00022"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00023"),
                        fHrod, 3, 0, 0);
                break;

            case 60:
                // Papier mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00024"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00025"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00026"),
                        fPapier, 3, 0, 0);
                break;

            case 65:
                // budka mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00027"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00028"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00029"),
                        fBudka, 3, 0, 0);
                break;

            case 70:
                // skizze mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00030"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00031"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00032"),
                        fSkizze, 3, 0, 0);
                break;

            case 75:
                // feder mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00033"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00034"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00035"),
                        fFeder, 3, 0, 0);
                break;

            case 80:
                // dokuments mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00036"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00037"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00038"),
                        fDokumente, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Panorama
                NeuesBild(160, locationID);
                break;

            case 155:
                // Zahrodnik - Ausreden
                MPersonAusrede(fZahrodLook);
                break;

            case 160:
                // hrod - Ausreden
                DingAusrede(fHrod);
                break;

            case 165:
                // papier - Ausreden
                DingAusrede(fPapier);
                break;

            case 170:
                // budka - Ausreden
                DingAusrede(fBudka);
                break;

            case 175:
                // skizze - Ausreden
                DingAusrede(fSkizze);
                break;

            case 180:
                // feder - Ausreden
                DingAusrede(fFeder);
                break;

            case 185:
                // dokumente - Ausreden
                DingAusrede(fDokumente);
                break;

            // Dialog mit Kuchar

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                handrijHoertZu = true;
                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    // 1. Frage nur, wenn noch kein Liscik
                    if (!mainFrame.Actions[542]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00039"), 1000, 537, new int[]{537}, 610);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00040"), 537, 536, new int[]{536}, 611);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00041"), 536, 535, new int[]{535}, 612);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00042"), 535, 534, new int[]{534}, 613);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00043"), 534, 533, new int[]{533}, 614);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00044"), 533, 532, new int[]{532}, 615);
                        if (!mainFrame.Actions[520]) {
                            Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00045"), 532, 1000, null, 618);
                        } else {
                            Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00046"), 532, 1000, null, 620);
                        }
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00047"), 1000, 541, new int[]{541}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00048"), 541, 540, new int[]{540}, 631);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00049"), 540, 1000, null, 632);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00050"), 1000, 1000, null, 800);
                }

                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    // 1. Frage nur, wenn noch kein Liscik
                    if (!mainFrame.Actions[542]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00051"), 1000, 537, new int[]{537}, 610);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00052"), 537, 536, new int[]{536}, 611);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00053"), 536, 535, new int[]{535}, 612);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00054"), 535, 534, new int[]{534}, 613);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00055"), 534, 533, new int[]{533}, 614);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00056"), 533, 532, new int[]{532}, 615);
                        if (!mainFrame.Actions[520]) {
                            Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00057"), 532, 1000, null, 618);
                        } else {
                            Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00058"), 532, 1000, null, 620);
                        }
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00059"), 1000, 541, new int[]{541}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00060"), 541, 540, new int[]{540}, 631);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00061"), 540, 1000, null, 632);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00062"), 1000, 1000, null, 800);
                }

                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    // 1. Frage nur, wenn noch kein Liscik
                    if (!mainFrame.Actions[542]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00063"), 1000, 537, new int[]{537}, 610);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00064"), 537, 536, new int[]{536}, 611);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00065"), 536, 535, new int[]{535}, 612);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00066"), 535, 534, new int[]{534}, 613);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00067"), 534, 533, new int[]{533}, 614);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00068"), 533, 532, new int[]{532}, 615);
                        if (!mainFrame.Actions[520]) {
                            Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00069"), 532, 1000, null, 618);
                        } else {
                            Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00070"), 532, 1000, null, 620);
                        }
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00071"), 1000, 541, new int[]{541}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00072"), 541, 540, new int[]{540}, 631);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00073"), 540, 1000, null, 632);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zahrodnik_00074"), 1000, 1000, null, 800);
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

            // Sequenz vor MC (erstes Anreden)
            case 608:
                // Reaktion Krabat
                // hier erst Handrij aufmerksam machen
                handrijHoertZu = true;
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00075"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00076"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00077"),
                        0, 1, 2, 609);
                break;

            case 609:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00078"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00079"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00080"),
                        0, 50, 2, 600, talkPoint);
                mainFrame.Actions[530] = true; // Flag setzen
                break;

            // Antworten zu Frage 1 ////////////////////////////

            case 610:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00081"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00082"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00083"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 611:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00084"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00085"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00086"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 612:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00087"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00088"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00089"),
                        0, 50, 2, 600, talkPoint);
                break;


            case 613:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00090"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00091"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00092"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 614:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00093"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00094"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00095"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 615:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00096"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00097"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00098"),
                        0, 50, 2, 616, talkPoint);
                break;

            case 616:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00099"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00100"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00101"),
                        0, 50, 2, 617, talkPoint);
                break;

            case 617:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00102"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00103"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00104"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 618:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00105"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00106"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00107"),
                        0, 50, 2, 619, talkPoint);
                break;

            case 619:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00108"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00109"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00110"),
                        0, 50, 2, 622, talkPoint);
                break;

            case 620:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00111"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00112"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00113"),
                        0, 50, 2, 621, talkPoint);
                break;

            case 621:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00114"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00115"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00116"),
                        0, 50, 2, 622, talkPoint);
                break;

            case 622:
                // Zahrodnik schreibt eine Weile
                tCounter = 58;
                handrijSchreibt = true;
                nextActionID = 623;
                break;

            case 623:
                // warten
                if (tCounter == 48) {
                    mainFrame.wave.PlayFile("sfx-dd/pisac.wav");
                }
                if (--tCounter > 1) {
                    break;
                }
                handrijSchreibt = false;
                handrijGibt = true;
                tCounter = 10;
                nextActionID = 626;
                break;

            case 626:
                // warten beim Geben, Krabat streckt auch bald Hand aus
                if (tCounter == 5) {
                    mainFrame.krabat.nAnimation = 93;
                }
                if (--tCounter > 1) {
                    break;
                }
                nextActionID = 628;
                handrijGibt = false;
                break;

            case 628:
                // End-Reaktion Krabat
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00117"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00118"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00119"),
                        0, 1, 2, 800);
                // Empfehlungsschreiben zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(30);
                // Flag - setzen ! Krabat bekommt Empfehl.schreiben
                mainFrame.Actions[542] = true;
                break;


            // Antworten zu Frage 2 ////////////////////////////
            case 630:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00120"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00121"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00122"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 631:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00123"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00124"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00125"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 632:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00126"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00127"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00128"),
                        0, 50, 2, 633, talkPoint);
                break;

            case 633:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00129"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00130"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00131"),
                        0, 50, 2, 634, talkPoint);
                break;

            case 634:
                // Reaktion Zahrodnik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zahrodnik_00132"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00133"),
                        Start.stringManager.getTranslation("Loc3_Zahrodnik_00134"),
                        0, 50, 2, 600, talkPoint);
                break;

            case 800:
                // wieder weg von Zahrodnik laufen
                mainFrame.wegGeher.SetzeNeuenWeg(pZahrUnten);
                nextActionID = 810;
                break;

            case 810:
                // MC beenden, wenn zuende gelabert...
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                handrijHoertZu = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        // wenn anderes wichtiger ist, dann hier verhindern
        if (schnauzeZahrod) {
            return;
        }

        // zu Anfang noch nicht abspielen
        if (--initCounter > 0) {
            return;
        }

        int zf = (int) (Math.random() * 100);

        if (zf > 96) {
            int zwzf = (int) (Math.random() * 4.99);
            zwzf += 49;

            mainFrame.wave.PlayFile("sfx-dd/zahrod" + (char) zwzf + ".wav");
        }

    }

}