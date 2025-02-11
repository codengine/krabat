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
import de.codengine.krabat.anims.Gonzales;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Habor extends Mainloc {
    private GenericImage background;
    private GenericImage murja;
    private GenericImage steg;
    private GenericImage boota;
    private GenericImage bootb;
    private final Multiple2 Dialog;
    private final Gonzales gonzales;

    private final Borderrect gonzalesRect;
    private final GenericPoint gonzalesPoint;
    private final GenericPoint talkPoint;

    private int whichShip = 0;

    private boolean giveHaken = false;

    private int Hakencounter;

    private boolean isListening = false;

    // Konstanten - Rects
    private static final Borderrect ausgangPanorama
            = new Borderrect(600, 370, 639, 479);
    private static final Borderrect ausgangLodz
            = new Borderrect(220, 270, 280, 335);
    private static final Borderrect rectMurja
            = new Borderrect(360, 370, 485, 430);
    private static final Borderrect sudobjaRect
            = new Borderrect(357, 328, 402, 359);

    // Konstante Points
    private static final GenericPoint pExitPanorama = new GenericPoint(639, 427);
    private static final GenericPoint pExitLodz = new GenericPoint(265, 339);
    private static final GenericPoint pGonzales = new GenericPoint(346, 364);
    private static final GenericPoint gonzalesFeet = new GenericPoint(331, 366);
    private static final GenericPoint pSudobja = new GenericPoint(405, 370);

    // Konstante ints
    private static final int fGonzales = 9;
    private static final int fSudobja = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Habor(Start caller, int oldLocation) {
        super(caller, 163);

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 50;      // no zooming
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = 57;

        // Gonzales nur, wenn Kotwica noch nicht gegeben - init erfolgt jedoch immer
        gonzales = new Gonzales(mainFrame);
        Dialog = new Multiple2(mainFrame);

        // Berechnung der Position usw.
        gonzalesPoint = new GenericPoint();
        gonzalesPoint.x = gonzalesFeet.x - Gonzales.Breite / 2;
        gonzalesPoint.y = gonzalesFeet.y - Gonzales.Hoehe;

        talkPoint = new GenericPoint();
        talkPoint.x = gonzalesFeet.x;
        talkPoint.y = gonzalesPoint.y - 50;

        gonzalesRect = new Borderrect(gonzalesPoint.x, gonzalesPoint.y, gonzalesPoint.x + Gonzales.Breite, gonzalesPoint.y + Gonzales.Hoehe);

        // Hier rauskriegen, welches Schiff gezeichnet werden muss
        if (!mainFrame.Actions[568]) {
            whichShip = 1;
        } else {
            if (!mainFrame.Actions[569]) {
                whichShip = 2;
            } else {
                whichShip = 0;
            }
        }

        // Hier das Flag "ich weiss vom Stollen" loeschen
        mainFrame.Actions[710] = false;

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();

        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(370, 397, 578, 630, 377, 428));
        // Hier unterscheiden, ob Gonzales dasteht, damit man nicht durchrennt
        if (!mainFrame.Actions[568]) {
            // Gonzales ist da
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(345, 347, 390, 395, 364, 376));

        } else {
            // Gonzales ist weg
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(285, 290, 390, 395, 347, 376));
        }
        mainFrame.wegSucher.ClearMatrix(2);
        mainFrame.wegSucher.PosVerbinden(0, 1);
    		
        /*    mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (578, 630, 578, 630, 423, 428));
              mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (520, 580, 520, 580, 414, 422));
              mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (420, 522, 420, 522, 402, 413));
              mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (398, 460, 398, 460, 389, 401));
              mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (370, 397, 380, 397, 377, 401));
              mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (261, 266, 390, 395, 339, 376));
              mainFrame.wegGeher.vBorders.addElement 
              (new bordertrapez (280, 285, 261, 266, 331, 338));*/

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(14, true);
                break;
            case 160:
                // von Panorama aus
                BackgroundMusicPlayer.getInstance().playTrack(14, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(611, 426));
                mainFrame.krabat.SetFacing(9);
                break;
            case 164:
                // von Lodz aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(301, 352));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/habor/habor.gif");
        murja = getPicture("gfx-dd/habor/murja.gif");
        steg = getPicture("gfx-dd/habor/steg.gif");
        boota = getPicture("gfx-dd/habor/boot-a.gif");
        bootb = getPicture("gfx-dd/habor/boot-b.gif");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
//         if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
//             {
//                 Dialog.paintMultiple (g);
//                 return;
//             }  

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

        // Hier entscheiden, welches Schiff erscheint
        if (whichShip == 1) {
            g.drawImage(bootb, 166, 251);
        }
        if (whichShip == 2) {
            g.drawImage(boota, 175, 230);
        }
        if (whichShip == 1 || whichShip == 2) {
            g.drawImage(steg, 146, 255);
        }

        // Debugging - Zeichnen der Laufrechtecke
        Debug.DrawRect(g, mainFrame.wegGeher.vBorders);

        // Gonzales nur, wenn noch nicht Kotwica gegeben
        if (!mainFrame.Actions[568]) {
            // Gonzales zeichnen
            g.setClip(gonzalesPoint.x, gonzalesPoint.y, Gonzales.Breite, Gonzales.Hoehe);
            g.drawImage(background, 0, 0);
            gonzales.drawGonzales(g, TalkPerson, gonzalesPoint, giveHaken, isListening);
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

        // hinter Mauer ? (nur Clipping - Region wird neugezeichnet)
        if (rectMurja.IsPointInRect(pKrTemp)) {
            g.drawImage(murja, 380, 387);
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

                // Ausreden fuer Kirche
                // if (brKirche.IsPointInRect (pTemp) == true)
                // {
                // 	// Standard - Sinnloszeug
                // 	nextActionID = 150;
                //	pTemp = Pkirche;
                // }

                // Ausreden fuer Gonzales, wenn Kotwica noch nicht da
                if (gonzalesRect.IsPointInRect(pTemp) && !mainFrame.Actions[568]) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pGonzales;
                }

                // Sudobja ausreden
                if (sudobjaRect.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pSudobja;
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
                if (ausgangPanorama.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangPanorama.IsPointInRect(kt)) {
                        pTemp = pExitPanorama;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitPanorama.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Lodz gehen ?, falls noch da (wenn Gonzales weg ist)
                if (ausgangLodz.IsPointInRect(pTemp) && mainFrame.Actions[568] && !mainFrame.Actions[569]) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangLodz.IsPointInRect(kt)) {
                        pTemp = pExitLodz;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitLodz.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Gonzales ansehen, falls da
                if (gonzalesRect.IsPointInRect(pTemp) && !mainFrame.Actions[568]) {
                    nextActionID = 1;
                    pTemp = pGonzales;
                }

                // Subobja ansehen
                if (sudobjaRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pSudobja;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit Gonzales reden
                if (gonzalesRect.IsPointInRect(pTemp) && !mainFrame.Actions[568]) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pGonzales);
                    mainFrame.repaint();
                    return;
                }

                // Sudobja mitnehmen
                if (sudobjaRect.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSudobja);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangPanorama.IsPointInRect(pTemp) ||
                        ausgangLodz.IsPointInRect(pTemp) && mainFrame.Actions[568] && !mainFrame.Actions[569]) {
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
                    gonzalesRect.IsPointInRect(pTemp) && !mainFrame.Actions[568] ||
                    sudobjaRect.IsPointInRect(pTemp);

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
            if (ausgangPanorama.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if (ausgangLodz.IsPointInRect(pTemp) && mainFrame.Actions[568] && !mainFrame.Actions[569]) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 12;
                }
                return;
            }

            if (gonzalesRect.IsPointInRect(pTemp) && !mainFrame.Actions[568] ||
                    sudobjaRect.IsPointInRect(pTemp)) {
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
                // Gonzales anschauen
                // zufaell Zahl zw. 0 und 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Habor_00000"),
                                Start.stringManager.getTranslation("Loc3_Habor_00001"),
                                Start.stringManager.getTranslation("Loc3_Habor_00002"),
                                fGonzales, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Habor_00003"),
                                Start.stringManager.getTranslation("Loc3_Habor_00004"),
                                Start.stringManager.getTranslation("Loc3_Habor_00005"),
                                fGonzales, 3, 0, 0);
                        break;
                }
                break;

            case 2:
                // Sudobja anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Habor_00006"),
                        Start.stringManager.getTranslation("Loc3_Habor_00007"),
                        Start.stringManager.getTranslation("Loc3_Habor_00008"),
                        fSudobja, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Gonzales benutzen)
                mainFrame.krabat.SetFacing(fGonzales);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                isListening = true;
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.Actions[560]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 55:
                // Sudobja mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Habor_00009"),
                        Start.stringManager.getTranslation("Loc3_Habor_00010"),
                        Start.stringManager.getTranslation("Loc3_Habor_00011"),
                        fSudobja, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Panorama
                NeuesBild(160, locationID);
                break;

            case 101:
                // Gehe zu Lodz
                NeuesBild(164, locationID);
                break;

            case 155:
                // Gonzales - Ausreden
                MPersonAusrede(fGonzales);
                break;

            case 160:
                // Sudobja - Ausreden
                DingAusrede(fSudobja);
                break;

            // Dialog mit Gonzales

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00012"), 1000, 564, new int[]{564}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00013"), 564, 563, new int[]{563}, 611);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00014"), 563, 562, new int[]{562}, 612);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00015"), 562, 561, new int[]{561}, 615);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00016"), 561, 1000, null, 614);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00017"), 1000, 567, new int[]{567}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00018"), 567, 566, new int[]{566}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00019"), 566, 565, new int[]{565}, 622);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00020"), 565, 1000, null, 623);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00021"), 565, 1000, null, 630);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00022"), 1000, 1000, null, 800);
                }

                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00023"), 1000, 564, new int[]{564}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00024"), 564, 563, new int[]{563}, 611);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00025"), 563, 562, new int[]{562}, 612);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00026"), 562, 561, new int[]{561}, 615);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00027"), 561, 1000, null, 614);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00028"), 1000, 567, new int[]{567}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00029"), 567, 566, new int[]{566}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00030"), 566, 565, new int[]{565}, 622);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00031"), 565, 1000, null, 623);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00032"), 565, 1000, null, 630);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00033"), 1000, 1000, null, 800);
                }

                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00034"), 1000, 564, new int[]{564}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00035"), 564, 563, new int[]{563}, 611);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00036"), 563, 562, new int[]{562}, 612);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00037"), 562, 561, new int[]{561}, 615);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00038"), 561, 1000, null, 614);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00039"), 1000, 567, new int[]{567}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00040"), 567, 566, new int[]{566}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00041"), 566, 565, new int[]{565}, 622);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00042"), 565, 1000, null, 623);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00043"), 565, 1000, null, 630);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Habor_00044"), 1000, 1000, null, 800);
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
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Habor_00045"),
                        Start.stringManager.getTranslation("Loc3_Habor_00046"),
                        Start.stringManager.getTranslation("Loc3_Habor_00047"),
                        0, 1, 2, 609);
                break;

            case 609:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00048"),
                        Start.stringManager.getTranslation("Loc3_Habor_00049"),
                        Start.stringManager.getTranslation("Loc3_Habor_00050"),
                        0, 61, 2, 600, talkPoint);
                mainFrame.Actions[560] = true; // Flag setzen, denn es gibt nur einen Anfang !!!
                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00051"),
                        Start.stringManager.getTranslation("Loc3_Habor_00052"),
                        Start.stringManager.getTranslation("Loc3_Habor_00053"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 611:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00054"),
                        Start.stringManager.getTranslation("Loc3_Habor_00055"),
                        Start.stringManager.getTranslation("Loc3_Habor_00056"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 612:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00057"),
                        Start.stringManager.getTranslation("Loc3_Habor_00058"),
                        Start.stringManager.getTranslation("Loc3_Habor_00059"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 614:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00060"),
                        Start.stringManager.getTranslation("Loc3_Habor_00061"),
                        Start.stringManager.getTranslation("Loc3_Habor_00062"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 615:
                // Reaktion Gonzales, noch Ohne Give Haken
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00063"),
                        Start.stringManager.getTranslation("Loc3_Habor_00064"),
                        Start.stringManager.getTranslation("Loc3_Habor_00065"),
                        0, 61, 2, 616, talkPoint);
                break;

            case 616:
                // Reaktion Gonzales
                giveHaken = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00066"),
                        Start.stringManager.getTranslation("Loc3_Habor_00067"),
                        Start.stringManager.getTranslation("Loc3_Habor_00068"),
                        0, 61, 2, 617, talkPoint);
                // Enterhaken zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(37);
                Hakencounter = 10;
                break;

            case 617:
                // Give eval.
                if (Hakencounter == 5) {
                    mainFrame.krabat.nAnimation = 91;
                }
                if (--Hakencounter > 1) {
                    break;
                }
                giveHaken = false;
                nextActionID = 618;
                break;

            case 618:
                // Give wieder aufheben
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 600;
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00069"),
                        Start.stringManager.getTranslation("Loc3_Habor_00070"),
                        Start.stringManager.getTranslation("Loc3_Habor_00071"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 621:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00072"),
                        Start.stringManager.getTranslation("Loc3_Habor_00073"),
                        Start.stringManager.getTranslation("Loc3_Habor_00074"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 622:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00075"),
                        Start.stringManager.getTranslation("Loc3_Habor_00076"),
                        Start.stringManager.getTranslation("Loc3_Habor_00077"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 623:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00078"),
                        Start.stringManager.getTranslation("Loc3_Habor_00079"),
                        Start.stringManager.getTranslation("Loc3_Habor_00080"),
                        0, 61, 2, 624, talkPoint);
                break;

            case 624:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00081"),
                        Start.stringManager.getTranslation("Loc3_Habor_00082"),
                        Start.stringManager.getTranslation("Loc3_Habor_00083"),
                        0, 61, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00084"),
                        Start.stringManager.getTranslation("Loc3_Habor_00085"),
                        Start.stringManager.getTranslation("Loc3_Habor_00086"),
                        0, 61, 2, 631, talkPoint);
                break;

            case 631:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00087"),
                        Start.stringManager.getTranslation("Loc3_Habor_00088"),
                        Start.stringManager.getTranslation("Loc3_Habor_00089"),
                        0, 61, 2, 632, talkPoint);
                break;

            case 632:
                // Reaktion Gonzales
                PersonSagt(Start.stringManager.getTranslation("Loc3_Habor_00090"),
                        Start.stringManager.getTranslation("Loc3_Habor_00091"),
                        Start.stringManager.getTranslation("Loc3_Habor_00092"),
                        0, 61, 2, 600, talkPoint);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                isListening = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}