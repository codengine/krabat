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

package de.codengine.locations3;

import de.codengine.Start;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Gang extends Mainloc {
    private GenericImage backl, backr, stamm, buch;
    private GenericImage /* kette1 , */ kette2, tuer, tuervorder;
    private boolean setScroll = false;
    private int scrollwert;
    private boolean ziehtKrabatAnKette = false;

    // Konstanten - Rects
    private static final Borderrect ausgangZachod
            = new Borderrect(71, 93, 139, 127);
    private static final Borderrect ausgangKapala
            = new Borderrect(1103, 60, 1183, 98);
    private static final Borderrect tuerUnten
            = new Borderrect(1104, 253, 1182, 355);
    private static final Borderrect ring
            = new Borderrect(391, 283, 417, 337);
    private static final Borderrect knochen
            = new Borderrect(356, 351, 388, 369);
    private static final Borderrect rectStamm
            = new Borderrect(940, 190, 1060, 275);
    private static final Borderrect buchRect
            = new Borderrect(343, 331, 487, 344);
    private static final Borderrect rectVorderTuer
            = new Borderrect(1164, 223, 1255, 374);

    // Konstante Points
    private static final GenericPoint pExitZachod = new GenericPoint(85, 326);
    private static final GenericPoint pExitKapala = new GenericPoint(1134, 197);
    private static final GenericPoint pKnochen = new GenericPoint(420, 354);
    private static final GenericPoint pTuerUnten = new GenericPoint(1143, 370);
    private static final GenericPoint pRing = new GenericPoint(441, 350);
    private static final GenericPoint pRingZieh = new GenericPoint(441, 350);
    private static final GenericPoint pBuch = new GenericPoint(441, 350);
    private static final GenericPoint pTuerRein = new GenericPoint(1187, 348);
    private static final GenericPoint pTuerDrin = new GenericPoint(1212, 348);

    // Konstante ints
    private static final int fRing = 9;
    private static final int fKnochen = 9;
    private static final int fTuer = 12;
    private static final int fBuch = 9;
    private static final int fRingZieh = 9;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Gang(Start caller, int oldLocation) {
        super(caller, 152);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(20, true);

        mainFrame.krabat.maxx = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomf = 1f;
        mainFrame.krabat.defScale = 0;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(40, 120, 67, 120, 326, 358));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(121, 430, 121, 430, 348, 358));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(431, 470, 431, 470, 338, 370));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(471, 790, 471, 790, 360, 365));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(791, 885, 791, 885, 363, 370));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(886, 1185, 886, 1170, 368, 380));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(538, 543, 465, 470, 324, 337));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(670, 675, 538, 543, 308, 323));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(728, 733, 670, 675, 298, 307));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(880, 885, 728, 733, 268, 297));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(1031, 1036, 880, 885, 225, 267));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(1178, 1183, 1031, 1036, 188, 224));

        mainFrame.wegSucher.ClearMatrix(12);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(2, 6);
        mainFrame.wegSucher.PosVerbinden(6, 7);
        mainFrame.wegSucher.PosVerbinden(7, 8);
        mainFrame.wegSucher.PosVerbinden(8, 9);
        mainFrame.wegSucher.PosVerbinden(9, 10);
        mainFrame.wegSucher.PosVerbinden(10, 11);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 151: // von Spaniska aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(85, 330));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;
            case 153: // von Komedij aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(1134, 197));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 640;
                setScroll = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx-dd/gang/gang-l.gif");
        backr = getPicture("gfx-dd/gang/gang-r.gif");
        stamm = getPicture("gfx-dd/gang/stamm.gif");
        buch = getPicture("gfx-dd/gang/gkniha.gif");

        // kette1 = getPicture ("gfx-dd/gang/rjecaz2.gif");
        kette2 = getPicture("gfx-dd/gang/rjecaz.gif");

        tuer = getPicture("gfx-dd/gang/gdurje.gif");
        tuervorder = getPicture("gfx-dd/gang/gfground.gif");

        loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollx = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(backl, 0, 0, null);
        g.drawImage(backr, 640, 0, null);

        // Buch zeichnen, wenn es auf der Kette liegt
        if (mainFrame.Actions[690]) {
            g.setClip(390, 280, 38, 76);
            // vorher die untere Kette zeichnen
            g.drawImage(kette2, 390, 280, null);
            g.drawImage(buch, 390, 280, null);

            // wenn Tuer offen, dann diese offen zeichnen, und das muss sie ja sein ;-)
            g.setClip(1103, 246, 85, 109);
            g.drawImage(tuer, 1103, 246, null);
        }

        // hier die Kette unten zeichnen, wenn K dran zieht
        if (ziehtKrabatAnKette) {
            g.setClip(390, 280, 38, 76);
            g.drawImage(kette2, 390, 280, null);

            --Counter;
            if (Counter < 1) {
                ziehtKrabatAnKette = false;
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        mainFrame.wegGeher.GeheWeg();

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }
        } else {
            if ((mainFrame.talkCount > 0) && (TalkPerson != 0)) {
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

        // hinter baumstamm (nur Clipping - Region wird neugezeichnet)
        if (rectStamm.IsPointInRect(pKrTemp)) {
            g.drawImage(stamm, 977, 67, null);
        }

        // hier dureberzeichnen, wenn er den Speer holen soll
        if ((mainFrame.Actions[690]) && (rectVorderTuer.IsPointInRect(pKrTemp))) {
            g.drawImage(tuervorder, 1175, 249, null);
        }


        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);  // Sobe hat 964 als y-wert ??????????
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

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Cursorpunkt mit Scrolloffset berechnen 
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollx;
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
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
            // unser ach so beliebter Ueberschneidungsbug...
            GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

            // linker Maustaste
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer kette
                if (ring.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 55: // schweres Buch
                            nextActionID = 230;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTxxx = pRing;
                }

                // Ausreden fuer Knochen
                if (knochen.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTxxx = pKnochen;
                }

                // Ausreden fuer Tuer
                if (tuerUnten.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 42: // Hlebija
                            nextActionID = 200;
                            break;
                        case 47: // Kluc
                            nextActionID = 210;
                            break;
                        case 46: // Hammer
                            nextActionID = 220;
                            break;
                        default:
                            nextActionID = 160;
                            break;
                    }
                    pTxxx = pTuerUnten;
                }

                // Ausreden fuer Buch
                if ((buchRect.IsPointInRect(pTemp)) && (mainFrame.Actions[690])) {
                    nextActionID = 165;
                    pTxxx = pBuch;
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
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // und nochmal Ueberschneidungsbug
                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Zachod gehen ?
                if (ausgangZachod.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangZachod.IsPointInRect(kt)) {
                        pTxxx = pExitZachod;
                    } else {
                        pTxxx = new GenericPoint(pExitZachod.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Kapala gehen ?
                if (ausgangKapala.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKapala.IsPointInRect(kt)) {
                        pTxxx = pExitKapala;
                    } else {
                        pTxxx = new GenericPoint(pExitKapala.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Ring ansehen
                if (ring.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTxxx = pRing;
                }

                // Knochen ansehen
                if (knochen.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTxxx = pKnochen;
                }

                // Tuer ansehen
                if (tuerUnten.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTxxx = pTuerUnten;
                }

                // Buch ansehen
                if ((buchRect.IsPointInRect(pTemp)) && (mainFrame.Actions[690])) {
                    nextActionID = 7;
                    pTxxx = pBuch;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Buch mitnehmen
                if ((buchRect.IsPointInRect(pTemp)) && (mainFrame.Actions[690])) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBuch);
                    mainFrame.repaint();
                    return;
                }

                // Ring benutzen
                if (ring.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    mainFrame.wegGeher.SetzeNeuenWeg(pRingZieh);
                    mainFrame.repaint();
                    return;
                }

                // knochen benutzen
                if (knochen.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKnochen);
                    mainFrame.repaint();
                    return;
                }

                // Tuer benutzen
                if (tuerUnten.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    mainFrame.wegGeher.SetzeNeuenWeg(pTuerUnten);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((ausgangZachod.IsPointInRect(pTemp)) || (ausgangKapala.IsPointInRect(pTemp))) {
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
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // neuen Punkt erzeugen wg. Scrolling
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if ((mainFrame.fPlayAnim) || (mainFrame.krabat.nAnimation != 0)) {
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
            mainFrame.invHighCursor = (tmp.IsPointInRect(pTemp)) || (ring.IsPointInRect(pTemp)) ||
                    (knochen.IsPointInRect(pTemp)) || (tuerUnten.IsPointInRect(pTemp)) ||
                    ((buchRect.IsPointInRect(pTemp)) && (mainFrame.Actions[690]));

            if ((Cursorform != 10) && (!mainFrame.invHighCursor)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if ((tuerUnten.IsPointInRect(pTemp)) ||
                    (ring.IsPointInRect(pTemp)) ||
                    (knochen.IsPointInRect(pTemp)) ||
                    ((buchRect.IsPointInRect(pTemp)) && (mainFrame.Actions[690]))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if ((ausgangZachod.IsPointInRect(pTemp)) ||
                    (ausgangKapala.IsPointInRect(pTemp))) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
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

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
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
        if ((mainFrame.krabat.isWandering) ||
                (mainFrame.krabat.isWalking)) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, 
        // wenn noetig (in Superklasse)
        if ((nextActionID > 499) && (nextActionID < 600)) {
            setKrabatAusrede();
            // manche Ausreden erfordern neuen Cursor !!!
            evalMouseMoveEvent(mainFrame.Mousepoint);
            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if ((nextActionID > 119) && (nextActionID < 129)) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Ring ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00000"),
                        Start.stringManager.getTranslation("Loc3_Gang_00001"),
                        Start.stringManager.getTranslation("Loc3_Gang_00002"),
                        fRing, 3, 0, 0);
                break;

            case 2:
                // Knochen ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00003"),
                        Start.stringManager.getTranslation("Loc3_Gang_00004"),
                        Start.stringManager.getTranslation("Loc3_Gang_00005"),
                        fKnochen, 3, 0, 0);
                break;

            case 3:
                // Tuer ansehen
                if (!mainFrame.Actions[690]) {
                    // Tuer ist zu
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00006"),
                            Start.stringManager.getTranslation("Loc3_Gang_00007"),
                            Start.stringManager.getTranslation("Loc3_Gang_00008"),
                            fTuer, 3, 0, 0);
                } else {
                    // Tuer ist auf...
                    mainFrame.krabat.SetFacing(fTuer);
                    if (!mainFrame.Actions[954]) {
                        // wenn noch kein Speer, dann diesen finden
                        // Inventar hinzufuegen
                        mainFrame.inventory.vInventory.addElement(42);
                        mainFrame.Actions[954] = true;        // Flag setzen
                        mainFrame.fPlayAnim = true;
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        nextActionID = 20;
                    } else {
                        // habe schon Speer
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00009"), Start.stringManager.getTranslation("Loc3_Gang_00010"), Start.stringManager.getTranslation("Loc3_Gang_00011"),
                                0, 3, 0, 0);
                    }
                }
                break;

            case 4:
                // Ring benutzen
                if (!mainFrame.Actions[690]) // Buch liegt nicht drauf
                {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    mainFrame.krabat.SetFacing(fRingZieh);
                    mainFrame.krabat.nAnimation = 94;
                    ziehtKrabatAnKette = true;
                    Counter = 40;
                    nextActionID = 60;
                    mainFrame.wave.PlayFile("sfx-dd/gdurjeauf.wav");
                } else {  // Buch liegt drauf
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00012"),
                            Start.stringManager.getTranslation("Loc3_Gang_00013"),
                            Start.stringManager.getTranslation("Loc3_Gang_00014"),
                            fRingZieh, 3, 0, 0);
                }
                break;

            case 5:
                // Knochen benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00015"),
                        Start.stringManager.getTranslation("Loc3_Gang_00016"),
                        Start.stringManager.getTranslation("Loc3_Gang_00017"),
                        fKnochen, 3, 0, 0);
                break;

            case 6:
                // Tuer benutzen
                if (!mainFrame.Actions[690]) {
                    // Tuer ist zu
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00018"),
                            Start.stringManager.getTranslation("Loc3_Gang_00019"),
                            Start.stringManager.getTranslation("Loc3_Gang_00020"),
                            fTuer, 3, 0, 0);
                } else {
                    // Tuer ist auf...
                    mainFrame.krabat.SetFacing(fTuer);
                    if (!mainFrame.Actions[954]) {
                        // wenn noch kein Speer, dann diesen finden
                        // Inventar hinzufuegen
                        mainFrame.fPlayAnim = true;
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        mainFrame.inventory.vInventory.addElement(42);
                        mainFrame.Actions[954] = true;        // Flag setzen
                        nextActionID = 20;
                    } else {
                        // habe schon Speer
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00021"), Start.stringManager.getTranslation("Loc3_Gang_00022"), Start.stringManager.getTranslation("Loc3_Gang_00023"),
                                0, 3, 0, 0);
                    }
                }
                break;

            case 7:
                // Buch anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00024"),
                        Start.stringManager.getTranslation("Loc3_Gang_00025"),
                        Start.stringManager.getTranslation("Loc3_Gang_00026"),
                        fBuch, 3, 0, 0);
                break;

            case 20:
                // vorgehen (noch sichtbar)
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pTuerRein);
                nextActionID = 23;
                break;

            case 23:
                // Tueranim durchfuehren
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00027"), Start.stringManager.getTranslation("Loc3_Gang_00028"), Start.stringManager.getTranslation("Loc3_Gang_00029"),
                        0, 3, 2, 25);
                break;

            case 25:
                // jetzt aus dem Bild verschwinden
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pTuerDrin);
                nextActionID = 27;
                break;

            case 27:
                // Tueranim
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00030"), Start.stringManager.getTranslation("Loc3_Gang_00031"), Start.stringManager.getTranslation("Loc3_Gang_00032"),
                        0, 3, 2, 30);
                break;

            case 30:
                // Tueranim
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00033"), Start.stringManager.getTranslation("Loc3_Gang_00034"), Start.stringManager.getTranslation("Loc3_Gang_00035"),
                        0, 3, 2, 31);
                break;

            case 31:
                // zuruecklaufen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pTuerRein);
                nextActionID = 32;
                break;

            case 32:
                // und wieder ins Rect
                mainFrame.wegGeher.SetzeNeuenWeg(pTuerUnten);
                nextActionID = 35;
                break;

            case 35:
                // Ende Tueranim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 50:
                // Buch mitnehmen
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.SetFacing(fBuch);
                mainFrame.krabat.nAnimation = 94;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 53;
                Counter = 5;
                break;

            case 53:
                // Ende Buch mitnehmen
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(55);
                    mainFrame.Actions[690] = false; // Kein Buch mehr da zum aufheben
                    mainFrame.Clipset = false;
                    mainFrame.wave.PlayFile("sfx-dd/gdurjezu.wav");
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 60:
                // auf Ende Buecken warten, dann Spruch sagen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00036"),
                        Start.stringManager.getTranslation("Loc3_Gang_00037"),
                        Start.stringManager.getTranslation("Loc3_Gang_00038"),
                        fRingZieh, 3, 2, 65);
                break;

            case 65:
                // Ende reden und Ende Kette abwarten
                if (Counter > 1) {
                    break;
                }
                ziehtKrabatAnKette = false;
                mainFrame.Clipset = false;
                mainFrame.wave.PlayFile("sfx-dd/gdurjezu.wav");
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Zachod
                NeuesBild(151, locationID);
                break;

            case 101:
                // Gehe zu Kapala
                NeuesBild(153, locationID);
                break;

            case 150:
                // Ausreden fuer kette
                DingAusrede(fRing);
                break;

            case 155:
                // Ausreden fuer knochen
                DingAusrede(fKnochen);
                break;

            case 160:
                // Ausreden fuer tuer
                DingAusrede(fTuer);
                break;

            case 165:
                // Ausreden fuer buch
                DingAusrede(fBuch);
                break;

            case 200:
                // hlebija auf tuer
                // Zufallszahl 0 bis 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00039"),
                                Start.stringManager.getTranslation("Loc3_Gang_00040"),
                                Start.stringManager.getTranslation("Loc3_Gang_00041"),
                                fTuer, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00042"),
                                Start.stringManager.getTranslation("Loc3_Gang_00043"),
                                Start.stringManager.getTranslation("Loc3_Gang_00044"),
                                fTuer, 3, 0, 0);
                        break;
                }
                break;

            case 210:
                // kluc auf tuer
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00045"),
                        Start.stringManager.getTranslation("Loc3_Gang_00046"),
                        Start.stringManager.getTranslation("Loc3_Gang_00047"),
                        fTuer, 3, 0, 0);
                break;

            case 220:
                // hammer auf tuer
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Gang_00048"),
                        Start.stringManager.getTranslation("Loc3_Gang_00049"),
                        Start.stringManager.getTranslation("Loc3_Gang_00050"),
                        fTuer, 3, 0, 0);
                break;

            case 230:
                // Buch hinlegen
                mainFrame.krabat.SetFacing(fBuch);
                mainFrame.krabat.nAnimation = 94;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                nextActionID = 0;
                // Buch raus aus Inventar
                mainFrame.inventory.vInventory.removeElement(55);
                mainFrame.Actions[690] = true; // Buch da zum aufheben
                mainFrame.wave.PlayFile("sfx-dd/gdurjeauf.wav");
                mainFrame.Clipset = false;
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}