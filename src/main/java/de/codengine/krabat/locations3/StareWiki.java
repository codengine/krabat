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
import de.codengine.krabat.anims.PredKorejtow;
import de.codengine.krabat.anims.PredMalickow;
import de.codengine.krabat.anims.PredWosuskow;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class StareWiki extends Mainloc {
    private GenericImage background;
    private GenericImage koleso;
    private final PredKorejtow predKorejtow;
    private final PredWosuskow predWosuskow;
    private final PredMalickow predMalickow;
    private final Multiple2 Dialog;

    private final GenericPoint korejtowPoint;
    private final GenericPoint talkPointKorejty;
    private final Borderrect rePredKorejtow;

    private final GenericPoint wosuskowPoint;
    private final GenericPoint talkPointWosuski;
    private final Borderrect rePredWosuskow;
    private boolean wosIsLooking = false;
    private boolean wosIsGiving = false;

    private final GenericPoint malickowPoint;
    private final GenericPoint talkPointMalicki;
    private final Borderrect rePredMalickow;
    private boolean malIsWalking = false;
    private boolean malIsGiving = false;
    private boolean malIsInvisible = false;
    private boolean malIsListening = false;

    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos;
    private int AnimTalkPerson = 0;
    private int AnimID = 5;
    private int AnimCounter = 0;
    private boolean AnimMCLocked = false;

    // Konstanten - Rects
    private static final Borderrect ausgangKarta
            = new Borderrect(0, 440, 310, 479);
    private static final Borderrect rectKoleso
            = new Borderrect(305, 395, 450, 415);

    // Konstante Points
    private static final GenericPoint pExitKarta = new GenericPoint(175, 479);
    private static final GenericPoint pPredKorejtow = new GenericPoint(412, 441);
    private static final GenericPoint pPredWosuskow = new GenericPoint(139, 407);
    private static final GenericPoint pPredMalickow = new GenericPoint(212, 400);
    private static final GenericPoint korejtowFeet = new GenericPoint(476, 444);
    private static final GenericPoint wosuskowFeet = new GenericPoint(67, 348);
    private static final GenericPoint malickowFeet = new GenericPoint(180, 369);

    // Konstante ints
    private static final int fKorj = 3;
    private static final int fWosu = 9;
    private static final int fMali = 9;

    private int Counter = 0;

    private static final int FAELSCHER_WARTEZEIT = 40;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public StareWiki(Start caller, int oldLocation) {
        super(caller, 175);

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(6, true);

        mainFrame.krabat.maxx = 418;
        mainFrame.krabat.zoomf = 0.15f;
        mainFrame.krabat.defScale = -90;

        predKorejtow = new PredKorejtow(mainFrame, new GenericPoint(420, 250));
        predWosuskow = new PredWosuskow(mainFrame);
        predMalickow = new PredMalickow(mainFrame);
        Dialog = new Multiple2(mainFrame);

        // Inits fuer predKorejtow
        korejtowPoint = new GenericPoint();
        korejtowPoint.x = korejtowFeet.x - PredKorejtow.Breite / 2;
        korejtowPoint.y = korejtowFeet.y - PredKorejtow.Hoehe;

        talkPointKorejty = new GenericPoint();
        talkPointKorejty.x = korejtowFeet.x;
        talkPointKorejty.y = korejtowPoint.y - 100;

        rePredKorejtow = new Borderrect(korejtowPoint.x, korejtowPoint.y, korejtowPoint.x + PredKorejtow.Breite, korejtowPoint.y + PredKorejtow.Hoehe);

        // Inits fuer predWosuskow
        wosuskowPoint = new GenericPoint();
        wosuskowPoint.x = wosuskowFeet.x - PredWosuskow.Breite / 2;
        wosuskowPoint.y = wosuskowFeet.y - PredWosuskow.Hoehe;

        talkPointWosuski = new GenericPoint();
        talkPointWosuski.x = wosuskowFeet.x;
        talkPointWosuski.y = wosuskowPoint.y - 130;

        rePredWosuskow = new Borderrect(wosuskowPoint.x, wosuskowPoint.y, wosuskowPoint.x + PredWosuskow.Breite, wosuskowPoint.y + PredWosuskow.Hoehe);

        // Inits fuer predMalickow
        malickowPoint = new GenericPoint();
        malickowPoint.x = malickowFeet.x - PredMalickow.Breite / 2;
        malickowPoint.y = malickowFeet.y - PredMalickow.Hoehe;

        talkPointMalicki = new GenericPoint();
        talkPointMalicki.x = malickowFeet.x;
        talkPointMalicki.y = malickowPoint.y - 50;

        rePredMalickow = new Borderrect(malickowPoint.x, malickowPoint.y, malickowPoint.x + PredMalickow.Breite, malickowPoint.y + PredMalickow.Hoehe);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 430, 412, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 290, 20, 290, 418, 429));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(244, 370, 244, 370, 400, 417));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(210, 243, 210, 243, 400, 402));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(140, 407, 180, 409));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(181, 407, 184, 417));

        mainFrame.wegSucher.ClearMatrix(6);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(1, 5);
        mainFrame.wegSucher.PosVerbinden(4, 5);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 180:
                // von Karta aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(175, 470));
                mainFrame.krabat.SetFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/starewiki/starewiki.gif");
        koleso = getPicture("gfx-dd/starewiki/koleso.gif");

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
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // PredWos und PredMal Hintergrund loeschen (sind uebereinanader)
        g.setClip(wosuskowPoint.x, wosuskowPoint.y, PredWosuskow.Breite, PredWosuskow.Hoehe);
        g.drawImage(background, 0, 0, null);
        g.setClip(malickowPoint.x - 5, malickowPoint.y - 3, PredMalickow.Breite + 10, PredMalickow.Hoehe + 6);
        g.drawImage(background, 0, 0, null);

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Predawar Korejtow zeichnen
        g.setClip(korejtowPoint.x, korejtowPoint.y, PredKorejtow.Breite, PredKorejtow.Hoehe);
        g.drawImage(background, 0, 0, null);
        if (AnimTalkPerson == 62 && !AnimMCLocked) {
            // bei einer Anim rumschreien lassen
            predKorejtow.callPredawar(g);
        } else {
            if (TalkPerson == 62) {
                // beim Reden
                predKorejtow.talkPredawar(g);
            } else {
                // beim Rumstehen
                predKorejtow.drawPredawar(g);
            }
        }

        // Predawar Wosuskow zeichnen
        g.setClip(wosuskowPoint.x, wosuskowPoint.y, PredWosuskow.Breite, PredWosuskow.Hoehe);
        if (AnimTalkPerson == 63 && !AnimMCLocked) {
            // bei einer Anim rumschreien lassen
            predWosuskow.callPredawar(g);
        } else {
            if (TalkPerson == 63) {
                // beim Reden
                predWosuskow.talkPredawar(g);
            } else {
                if (wosIsGiving || wosIsLooking) {
                    // bei irgendwelchen Anims
                    if (wosIsGiving) {
                        wosIsGiving = predWosuskow.animPredawar(g, 2);
                    }
                    if (wosIsLooking) {
                        wosIsLooking = predWosuskow.animPredawar(g, 1);
                    }
                } else {
                    // beim Rumstehen
                    predWosuskow.drawPredawar(g);
                }
            }
        }

        // Predawar Malickoscow zeichnen
        g.setClip(malickowPoint.x - 5, malickowPoint.y - 3, PredMalickow.Breite + 10, PredMalickow.Hoehe + 6);
        if (AnimTalkPerson == 64 && !AnimMCLocked) {
            // bei einer Anim rumschreien lassen
            if (!malIsInvisible) {
                predMalickow.callPredawar(g);
            }
        } else {
            if (TalkPerson == 64) {
                // beim Reden
                if (!malIsInvisible) {
                    predMalickow.talkPredawar(g);
                }
            } else {
                if (malIsWalking || malIsGiving) {
                    // bei irgendwelchen Anims
                    if (malIsWalking) {
                        malIsWalking = predMalickow.animPredawar(g, 1);
                    }
                    if (malIsGiving && !malIsInvisible) {
                        malIsGiving = predMalickow.animPredawar(g, 2);
                    }
                } else {
                    // beim Rumstehen
                    if (!malIsInvisible) {
                        predMalickow.drawPredawar(g, malIsListening);
                    }
                }
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

        // hinter Rad ? (nur Clipping - Region wird neugezeichnet)
        if (rectKoleso.IsPointInRect(pKrTemp)) {
            g.drawImage(koleso, 337, 271, null);
        }

        // Ausgabe von AnimText, falls noetig
        if (AnimOutputText != "" && !AnimMCLocked) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.ifont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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

        // Die Anims muessen bedient werden
        if (AnimID != 0 && !AnimMCLocked) {
            DoAnims();
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

                // Ausreden fuer predWosuskow oder Stollen kaufen
                if (rePredWosuskow.IsPointInRect(pTemp)) {
                    if (mainFrame.whatItem == 40) {
                        nextActionID = 160;
                    } else {
                        nextActionID = 200;
                    }
                    pTemp = pPredWosuskow;
                }

                // Ausreden fuer PredKorejtow
                if (rePredKorejtow.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pPredKorejtow;
                }

                // Ausreden fuer PredMalickow bzw. Sachen zum Faelschen geben
                // Hier darf kein Inventory geaendert oder Actions gesetzt werden, da K noch laeuft (Abbruch mgl.)
                if (rePredMalickow.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 31: // Dowol ohne alles
                            if (!mainFrame.Actions[647] && !mainFrame.Actions[648]) {
                                nextActionID = 1000;
                            }
                            if (!mainFrame.Actions[647] && mainFrame.Actions[648]) {
                                nextActionID = 1070;
                            }
                            if (mainFrame.Actions[647] && !mainFrame.Actions[648]) {
                                nextActionID = 1040;
                            }
                            if (mainFrame.Actions[647] && mainFrame.Actions[648]) {
                                nextActionID = 1090;
                            }
                            mainFrame.Actions[649] = false;
                            break;
                        case 32: // Dowol zygl.
                            if (!mainFrame.Actions[647] && !mainFrame.Actions[648]) {
                                nextActionID = 1000;
                            }
                            if (!mainFrame.Actions[647] && mainFrame.Actions[648]) {
                                nextActionID = 1070;
                            }
                            if (mainFrame.Actions[647] && !mainFrame.Actions[648]) {
                                nextActionID = 1040;
                            }
                            if (mainFrame.Actions[647] && mainFrame.Actions[648]) {
                                nextActionID = 1090;
                            }
                            mainFrame.Actions[649] = true;
                            break;
                        case 45: // Casnik
                            if (!mainFrame.Actions[645] && !mainFrame.Actions[646] && !mainFrame.Actions[647]) {
                                nextActionID = 1020;
                            }
                            if (!mainFrame.Actions[645] && !mainFrame.Actions[646] && mainFrame.Actions[647]) {
                                nextActionID = 1060;
                            }
                            if ((mainFrame.Actions[645] || mainFrame.Actions[646]) && !mainFrame.Actions[647]) {
                                nextActionID = 1050;
                            }
                            if ((mainFrame.Actions[645] || mainFrame.Actions[646]) && mainFrame.Actions[647]) {
                                nextActionID = 1110;
                            }
                            break;
                        case 49: // Prikaz
                            if (!mainFrame.Actions[645] && !mainFrame.Actions[646] && !mainFrame.Actions[648]) {
                                nextActionID = 1010;
                            }
                            if (!mainFrame.Actions[645] && !mainFrame.Actions[646] && mainFrame.Actions[648]) {
                                nextActionID = 1080;
                            }
                            if ((mainFrame.Actions[645] || mainFrame.Actions[646]) && !mainFrame.Actions[648]) {
                                nextActionID = 1030;
                            }
                            if ((mainFrame.Actions[645] || mainFrame.Actions[646]) && mainFrame.Actions[648]) {
                                nextActionID = 1100;
                            }
                            break;
                        default:
                            nextActionID = 210;
                            break;
                    }
                    pTemp = pPredMalickow;
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

                // zu Karta gehen ?
                if (ausgangKarta.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKarta.IsPointInRect(kt)) {
                        pTemp = pExitKarta;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitKarta.y);
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

                // PredKorejtow ansehen
                if (rePredKorejtow.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pPredKorejtow;
                }

                // PredWosuskow ansehen
                if (rePredWosuskow.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pPredWosuskow;
                }

                // PredMalickow ansehen
                if (rePredMalickow.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pPredMalickow;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem PredKorejtow reden
                if (rePredKorejtow.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pPredKorejtow);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem PredWosuskow reden
                if (rePredWosuskow.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(pPredWosuskow);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem PredMalickow reden
                if (rePredMalickow.IsPointInRect(pTemp)) {
                    nextActionID = 52;
                    mainFrame.wegGeher.SetzeNeuenWeg(pPredMalickow);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangKarta.IsPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.isAnim = false;
                ResetAnims();
                mainFrame.Clipset = false;
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
                    rePredKorejtow.IsPointInRect(pTemp) ||
                    rePredMalickow.IsPointInRect(pTemp) ||
                    rePredWosuskow.IsPointInRect(pTemp);

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
            if (ausgangKarta.IsPointInRect(pTemp)) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 6;
                }
                return;
            }

            if (rePredKorejtow.IsPointInRect(pTemp) ||
                    rePredWosuskow.IsPointInRect(pTemp) ||
                    rePredMalickow.IsPointInRect(pTemp)) {
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
        ResetAnims();
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
                // PredKorejtow anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00000"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00001"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00002"),
                        fKorj, 3, 0, 0);
                break;

            case 2:
                // PredWosuskow anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00003"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00004"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00005"),
                        fWosu, 3, 0, 0);
                break;

            case 3:
                // PredMalickow anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00006"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00007"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00008"),
                        fMali, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC mit PredKorejtow (benutzen)
                mainFrame.Clipset = false;
                mainFrame.krabat.SetFacing(fKorj);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                AnimMCLocked = true;
                ResetAnims();
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.Actions[580]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 51:
                // Krabat beginnt MC mit PredWosuskow (benutzen)
                mainFrame.Clipset = false;
                mainFrame.krabat.SetFacing(fWosu);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                AnimMCLocked = true;
                ResetAnims();
                nextActionID = 700;
                break;

            case 52:
                // Krabat beginnt MC mit PredMalickow (benutzen)
                mainFrame.Clipset = false;
                mainFrame.krabat.SetFacing(fMali);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                AnimMCLocked = true;
                ResetAnims();
                nextActionID = 800;
                break;

            case 100:
                // Gehe zu Karta
                NeuesBild(180, locationID);
                break;

            case 155:
                // predKorejtow - Ausreden
                MPersonAusrede(fKorj);
                break;

            case 160: // Taler uebergeben
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                mainFrame.Clipset = false;
                AnimMCLocked = true;
                mainFrame.krabat.SetFacing(fWosu);
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 135;
                nextActionID = 161;
                break;

            case 161: // warten Auf Ende Anim
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 162;
                }
                break;

            case 162:
                // PredWosuskow sucht Stollen
                wosIsLooking = true;
                nextActionID = 164;
                break;

            case 164:
                // Auf Ende Suchen warten
                if (!wosIsLooking) {
                    nextActionID = 165;
                }
                break;

            case 165:
                // Stollen kaufen
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00009"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00010"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00011"),
                        fWosu, 63, 0, 167, talkPointWosuski);
                // Taler aus Inventar raus und Stollen rein
                mainFrame.inventory.vInventory.removeElement(40);
                mainFrame.inventory.vInventory.addElement(52);
                mainFrame.Actions[956] = true;
                break;

            case 167:
                // PredWosuskow gibt Stollen
                wosIsGiving = true;
                nextActionID = 168;
                break;

            case 168:
                // Warten auf Ende Geben
                if (!wosIsGiving) {
                    nextActionID = 170;
                }
                break;

            case 170:
                // Takeanim Krabat
                mainFrame.krabat.nAnimation = 93;
                nextActionID = 175;
                break;

            case 175:
                // Krabat bedankt sich fuer Stollen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00012"), Start.stringManager.getTranslation("Loc3_StareWiki_00013"), Start.stringManager.getTranslation("Loc3_StareWiki_00014"), 0, 1, 2, 180);
                break;

            case 180:
                // Animationsmodus aus
                nextActionID = 0;
                AnimMCLocked = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 200:
                // predWosuskow - Ausreden
                MPersonAusrede(fWosu);
                break;

            case 210:
                // predMali - Ausreden
                MPersonAusrede(fMali);
                break;

            // Marktschreier /////////////////////////////////////////////////

            // Dialog mit PredKorejtow ////////////////////////////////////////////

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00015"), 1000, 581, new int[]{581}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00016"), 581, 1000, null, 611);

                    // 2. Frage
                    if (!mainFrame.Actions[604]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00017"), 581, 1000, null, 620);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00018"), 581, 1000, null, 620);
                    }

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00019"), 581, 583, new int[]{583}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00020"), 583, 582, new int[]{582}, 631);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00021"), 582, 1000, null, 634);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00022"), 1000, 1000, null, 910);
                }

                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00023"), 1000, 581, new int[]{581}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00024"), 581, 1000, null, 611);

                    // 2. Frage
                    if (!mainFrame.Actions[604]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00025"), 581, 1000, null, 620);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00026"), 581, 1000, null, 620);
                    }

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00027"), 581, 583, new int[]{583}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00028"), 583, 582, new int[]{582}, 631);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00029"), 582, 1000, null, 634);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00030"), 1000, 1000, null, 910);
                }

                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00031"), 1000, 581, new int[]{581}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00032"), 581, 1000, null, 611);

                    // 2. Frage
                    if (!mainFrame.Actions[604]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00033"), 581, 1000, null, 620);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00034"), 581, 1000, null, 620);
                    }

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00035"), 581, 583, new int[]{583}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00036"), 583, 582, new int[]{582}, 631);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00037"), 582, 1000, null, 634);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00038"), 1000, 1000, null, 910);
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
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00039"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00040"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00041"),
                        0, 62, 2, 600, talkPointKorejty);
                mainFrame.Actions[580] = true; // Flag setzen
                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00042"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00043"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00044"),
                        0, 62, 2, 600, talkPointKorejty);
                break;

            case 611:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00045"), Start.stringManager.getTranslation("Loc3_StareWiki_00046"), Start.stringManager.getTranslation("Loc3_StareWiki_00047"), 0, 62, 2, 600, talkPointKorejty);
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00048"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00049"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00050"),
                        0, 62, 2, 621, talkPointKorejty);
                break;

            case 621:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00051"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00052"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00053"),
                        0, 62, 2, 622, talkPointKorejty);
                break;

            case 622:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00054"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00055"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00056"),
                        0, 62, 2, 600, talkPointKorejty);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00057"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00058"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00059"),
                        0, 62, 2, 600, talkPointKorejty);
                break;

            case 631:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00060"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00061"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00062"),
                        0, 62, 2, 632, talkPointKorejty);
                break;

            case 632:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00063"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00064"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00065"),
                        0, 62, 2, 633, talkPointKorejty);
                break;

            case 633:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00066"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00067"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00068"),
                        0, 62, 2, 600, talkPointKorejty);
                break;

            case 634:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00069"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00070"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00071"),
                        0, 62, 2, 635, talkPointKorejty);
                break;

            case 635:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00072"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00073"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00074"),
                        0, 62, 2, 636, talkPointKorejty);
                break;

            case 636:
                // Reaktion PredKorejtow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00075"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00076"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00077"),
                        0, 62, 2, 637, talkPointKorejty);
                break;

            case 637:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00078"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00079"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00080"),
                        12, 3, 2, 638);
                break;

            case 638:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00081"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00082"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00083"),
                        12, 3, 2, 910);
                break;


            // Dialog mit PredWosuskow ////////////////////////////////////////////

            case 700:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00084"), 1000, 587, new int[]{587}, 710);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00085"), 587, 586, new int[]{586}, 711);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00086"), 586, 585, new int[]{585}, 713);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00087"), 585, 1000, null, 711);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00088"), 587, 1000, null, 720);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00089"), 1000, 1000, null, 910);
                }

                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00090"), 1000, 587, new int[]{587}, 710);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00091"), 587, 586, new int[]{586}, 711);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00092"), 586, 585, new int[]{585}, 713);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00093"), 585, 1000, null, 711);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00094"), 587, 1000, null, 720);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00095"), 1000, 1000, null, 910);
                }

                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00096"), 1000, 587, new int[]{587}, 710);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00097"), 587, 586, new int[]{586}, 711);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00098"), 586, 585, new int[]{585}, 713);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00099"), 585, 1000, null, 711);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00100"), 587, 1000, null, 720);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00101"), 1000, 1000, null, 910);
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

                // eine Extrawurst: Dialog 1. Frage zurueckschalten, wenn gefragt
                if (mainFrame.Actions[585] && nextActionID == 711) {
                    mainFrame.Actions[585] = false;
                }

                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 710:
                // Reaktion PredWosuskow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00102"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00103"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00104"),
                        0, 63, 2, 700, talkPointWosuski);
                break;

            case 711:
                // Reaktion PredWosuskow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00105"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00106"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00107"),
                        0, 63, 2, 712, talkPointWosuski);
                break;

            case 712:
                // Reaktion PredWosuskow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00108"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00109"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00110"),
                        0, 63, 2, 700, talkPointWosuski);
                break;

            case 713:
                // Reaktion PredWosuskow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00111"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00112"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00113"),
                        0, 63, 2, 714, talkPointWosuski);
                break;

            case 714:
                // Reaktion PredWosuskow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00114"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00115"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00116"),
                        0, 63, 2, 700, talkPointWosuski);
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 720:
                // Reaktion PredWosuskow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00117"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00118"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00119"),
                        0, 63, 2, 721, talkPointWosuski);
                break;

            case 721:
                // Reaktion Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00120"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00121"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00122"),
                        0, 1, 2, 910);
                break;

            // Dialog mit PredMalickoscow  ///////////////////////////////////////

            case 800:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00123"), 1000, 591, new int[]{591}, 810);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00124"), 591, 590, new int[]{590}, 811);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00125"), 590, 1000, null, 812);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00126"), 1000, 596, new int[]{596}, 820);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00127"), 596, 595, new int[]{595}, 821);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00128"), 595, 594, new int[]{594}, 822);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00129"), 594, 593, new int[]{593}, 823);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00130"), 593, 592, new int[]{592}, 824);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00131"), 592, 1000, null, 825);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00132"), 1000, 1000, null, 830);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00133"), 1000, 1000, null, 910);
                }

                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00134"), 1000, 591, new int[]{591}, 810);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00135"), 591, 590, new int[]{590}, 811);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00136"), 590, 1000, null, 812);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00137"), 1000, 596, new int[]{596}, 820);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00138"), 596, 595, new int[]{595}, 821);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00139"), 595, 594, new int[]{594}, 822);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00140"), 594, 593, new int[]{593}, 823);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00141"), 593, 592, new int[]{592}, 824);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00142"), 592, 1000, null, 825);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00143"), 1000, 1000, null, 830);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00144"), 1000, 1000, null, 910);
                }

                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00145"), 1000, 591, new int[]{591}, 810);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00146"), 591, 590, new int[]{590}, 811);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00147"), 590, 1000, null, 812);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00148"), 1000, 596, new int[]{596}, 820);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00149"), 596, 595, new int[]{595}, 821);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00150"), 595, 594, new int[]{594}, 822);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00151"), 594, 593, new int[]{593}, 823);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00152"), 593, 592, new int[]{592}, 824);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00153"), 592, 1000, null, 825);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00154"), 1000, 1000, null, 830);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_StareWiki_00155"), 1000, 1000, null, 910);
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

                // eine Extrawurst: Dialog 1. Frage zurueckschalten, wenn gefragt
                if (mainFrame.Actions[590] && nextActionID == 812) {
                    mainFrame.Actions[590] = false;
                }

                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 810:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00156"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00157"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00158"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 811:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00159"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00160"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00161"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 812:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00162"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00163"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00164"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            // Antworten zu Frage 2 ////////////////////////////

            case 820:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00165"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00166"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00167"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 821:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00168"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00169"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00170"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 822:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00171"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00172"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00173"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 823:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00174"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00175"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00176"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 824:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00177"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00178"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00179"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            case 825:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00180"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00181"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00182"),
                        0, 64, 2, 800, talkPointMalicki);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 830:
                // Reaktion PredMalickow
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00183"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00184"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00185"),
                        0, 64, 2, 800, talkPointMalicki);
                break;


            // ENDE ALLER DIALOGE  //////////////////////////////////////

            case 900:
                // Andere Anims beenden, wenn zuende gelabert...
                AnimMCLocked = false;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 910:
                // MC beenden, wenn zuende gelabert...
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                AnimMCLocked = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            ///// Ab hier die Gebe-Szenen mit dem Faelscher /////////////////////////////////////////////

            case 1000:
                // Dowolnosc zuerst
                if (!mainFrame.Actions[592]) // noch kein Dialog gelaufen
                {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    ResetAnims();
                    AnimMCLocked = true;
                    PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00186"),
                            Start.stringManager.getTranslation("Loc3_StareWiki_00187"),
                            Start.stringManager.getTranslation("Loc3_StareWiki_00188"),
                            0, 64, 0, 900, talkPointMalicki);
                } else {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    ResetAnims();
                    AnimMCLocked = true;
                    mainFrame.Clipset = false;
                    mainFrame.invCursor = false;
                    mainFrame.krabat.nAnimation = 136;
                    nextActionID = 1002;
                }
                break;

            case 1002:
                // fertig geben 
                malIsGiving = true;
                nextActionID = 1005;
                break;

            case 1005:
                // Dowolnosc zuerst
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00189"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00190"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00191"),
                        0, 64, 0, 900, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.Actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.Actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                break;

            case 1010:
                // Prikaz zuerst
                if (!mainFrame.Actions[592]) // noch kein Dialog gelaufen
                {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    ResetAnims();
                    AnimMCLocked = true;
                    PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00192"),
                            Start.stringManager.getTranslation("Loc3_StareWiki_00193"),
                            Start.stringManager.getTranslation("Loc3_StareWiki_00194"),
                            0, 64, 0, 900, talkPointMalicki);
                } else {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    ResetAnims();
                    AnimMCLocked = true;
                    mainFrame.Clipset = false;
                    mainFrame.invCursor = false;
                    mainFrame.krabat.nAnimation = 133;
                    nextActionID = 1012;
                }
                break;

            case 1012:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1015;
                break;

            case 1015:
                // Prikaz zuerst
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00195"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00196"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00197"),
                        0, 64, 0, 900, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.Actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                break;

            case 1020:
                // Casnik zuerst
                if (!mainFrame.Actions[592]) // noch kein Dialog gelaufen
                {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    ResetAnims();
                    AnimMCLocked = true;
                    PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00198"),
                            Start.stringManager.getTranslation("Loc3_StareWiki_00199"),
                            Start.stringManager.getTranslation("Loc3_StareWiki_00200"),
                            0, 64, 0, 900, talkPointMalicki);
                } else {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    ResetAnims();
                    AnimMCLocked = true;
                    mainFrame.Clipset = false;
                    mainFrame.invCursor = false;
                    mainFrame.krabat.nAnimation = 132;
                    nextActionID = 1022;
                }
                break;

            case 1022:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1025;
                break;

            case 1025:
                // Casnik zuerst
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00201"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00202"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00203"),
                        0, 64, 0, 900, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.Actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                break;

            case 1030:
                // Dowolnosc schon gegeben und Prikaz drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 133;
                nextActionID = 1032;
                break;

            case 1032:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1035;
                break;

            case 1035:
                // Dowolnosc schon gegeben und Prikaz drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00204"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00205"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00206"),
                        0, 64, 0, 900, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.Actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                break;

            case 1040:
                // Prikaz schon gegeben und Dowolnosc drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 136;
                nextActionID = 1042;
                break;

            case 1042:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1045;
                break;

            case 1045:
                // Prikaz schon gegeben und Dowolnosc drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00207"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00208"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00209"),
                        0, 64, 0, 900, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.Actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.Actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                break;

            case 1050:
                // Dowolnosc schon gegeben und Casnik drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 132;
                nextActionID = 1052;
                break;

            case 1052:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1055;
                break;

            case 1055:
                // Dowolnosc schon gegeben und Casnik drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00210"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00211"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00212"),
                        0, 64, 0, 900, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.Actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                break;

            case 1060:
                // Prikaz schon gegeben und Casnik drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 132;
                nextActionID = 1062;
                break;

            case 1062:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1065;
                break;

            case 1065:
                // Prikaz schon gegeben und Casnik drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00213"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00214"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00215"),
                        0, 64, 0, 900, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.Actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                break;

            case 1070:
                // Casnik schon da und Dowolnosc drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 136;
                nextActionID = 1072;
                break;

            case 1072:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1075;
                break;

            case 1075:
                // Casnik schon da und Dowolnosc drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00216"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00217"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00218"),
                        0, 64, 0, 900, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.Actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.Actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                break;

            case 1080:
                // Casnik schon da und Prikaz drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 133;
                nextActionID = 1082;
                break;

            case 1082:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1085;
                break;

            case 1085:
                // Casnik schon da und Prikaz drauf
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00219"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00220"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00221"),
                        0, 64, 0, 900, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.Actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                break;

            // Ab hier ist alles komplett und die Unterschrift kann gefaelscht werden //////////////////////////////////////

            case 1090:
                // Casnik Prikaz da, Dowolnosc drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 136;
                nextActionID = 1092;
                malIsListening = true;
                break;

            case 1092:
                // PredMali nimmt  
                malIsGiving = true;
                nextActionID = 1093;
                break;

            case 1093:
                // Ende geben/nehmen
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                Counter = FAELSCHER_WARTEZEIT;
                nextActionID = 1094;
                break;

            case 1094:
                // Weile warten, dann gibt PredMal zurueck
                if (--Counter > 1) {
                    break;
                }
                malIsGiving = true;
                nextActionID = 1095;
                break;

            case 1095:
                // Casnik Prikaz da, Dowolnosc drauf
                if (malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00222"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00223"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00224"),
                        0, 64, 0, 1096, talkPointMalicki);
                // Actionvariable setzen und Inventarstueck entfernen
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc
                    mainFrame.Actions[645] = true;
                    mainFrame.inventory.vInventory.removeElement(31);
                } else {
                    // gesiegelte Dowolnosc
                    mainFrame.Actions[646] = true;
                    mainFrame.inventory.vInventory.removeElement(32);
                }
                // nun die unterschriebene Dowolnosc zurueck ins Inventar
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(33);
                } else {
                    // gesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(34);
                }
                break;

            case 1096:
                // Krabat nimmts
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 1097;
                break;

            case 1097:
                // Ende nehmen
                malIsListening = false;
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 900;
                break;

            case 1100:
                // Casnik Dowolnosc da, Prikaz drauf
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 133;
                nextActionID = 1102;
                malIsListening = true;
                break;

            case 1102:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1103;
                break;

            case 1103:
                // waren auf Ende fertig geben, dann Warteschleife
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                Counter = FAELSCHER_WARTEZEIT;
                nextActionID = 1104;
                break;

            case 1104:
                if (--Counter > 1) {
                    break;
                }
                malIsGiving = true;
                nextActionID = 1105;
                break;

            case 1105:
                // Casnik Dowolnosc da, Prikaz drauf
                if (malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00225"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00226"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00227"),
                        0, 64, 0, 1106, talkPointMalicki);
                // Prikaz entfernen und Actionvariable setzen
                mainFrame.Actions[647] = true;
                mainFrame.inventory.vInventory.removeElement(49);
                // nun die unterschriebene Dowolnosc zurueck ins Inventar
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(33);
                } else {
                    // gesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(34);
                }
                break;

            case 1106:
                // Krabat nimmts
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 1107;
                break;

            case 1107:
                // Ende nehmen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 900;
                malIsListening = false;
                break;

            case 1110:
                // Prikaz Dowolnosc da, Casnik drauf, kurze Anim
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                ResetAnims();
                AnimMCLocked = true;
                mainFrame.Clipset = false;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 132;
                malIsListening = true;
                nextActionID = 1111;
                break;

            case 1111:
                // fertig geben  
                malIsGiving = true;
                nextActionID = 1112;
                break;

            case 1112:
                // Prikaz Dowolnosc da, Casnik drauf, kurze Anim
                if (mainFrame.krabat.nAnimation != 0 || malIsGiving) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00228"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00229"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00230"),
                        0, 64, 2, 1113, talkPointMalicki);
                // Casnik entfernen und Actionvariable setzen
                mainFrame.Actions[648] = true;
                mainFrame.inventory.vInventory.removeElement(45);
                // nun die unterschriebene Dowolnosc zurueck ins Inventar
                if (!mainFrame.Actions[649]) {
                    // ungesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(33);
                } else {
                    // gesiegelte Dowolnosc hinzufuegen
                    mainFrame.inventory.vInventory.addElement(34);
                }
                break;

            case 1113:
                // PredMal haut ab
                malIsWalking = true;
                nextActionID = 1114;
                break;

            case 1114:
                // predMal nach hinten laufen lassen
                if (!malIsWalking) {
                    nextActionID = 1115;
                    malIsInvisible = true;
                }
                break;

            case 1115:
                // Krabat redet
                KrabatSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00231"), Start.stringManager.getTranslation("Loc3_StareWiki_00232"), Start.stringManager.getTranslation("Loc3_StareWiki_00233"),
                        0, 1, 2, 1116);
                break;

            case 1116:
                // PredMal kommt wieder
                malIsWalking = true;
                nextActionID = 1117;
                break;

            case 1117:
                if (!malIsWalking) {
                    nextActionID = 1118;
                    malIsInvisible = false;
                    Counter = FAELSCHER_WARTEZEIT;
                }
                break;

            case 1118:
                // PredMal gibt
                if (--Counter > 1) {
                    break;
                }
                malIsGiving = true;
                nextActionID = 1119;
                break;

            case 1119:
                if (!malIsGiving) {
                    nextActionID = 1120;
                }
                break;

            case 1120:
                // Antwort PredMal... 
                PersonSagt(Start.stringManager.getTranslation("Loc3_StareWiki_00234"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00235"),
                        Start.stringManager.getTranslation("Loc3_StareWiki_00236"),
                        0, 64, 0, 1121, talkPointMalicki);
                break;

            case 1121:
                // Krabat nimmts
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 1122;
                break;

            case 1122:
                // Ende nehmen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 900;
                malIsListening = false;
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }

    // Anims: alle schreien rum und versuchen ihren schotter loszuwerden...
    private void DoAnims() {
        switch (AnimID) {
            case 5:
                // bisschen warten, damit Mainmenu ohne Probleme
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 10;
                }
                break;

            case 10:
                // Hier einen der gluecklichen Redner auswaehlen
                int zuffi = (int) (Math.random() * 3.9);
                switch (zuffi) {
                    case 0:
                        // es ist mal bisschen Ruhe hier...
                        AnimCounter = 50;
                        break;

                    case 1:
                        // der Pred korejtow ist dran
                        int zuffZahl = (int) (Math.random() * 3.9);
                        switch (zuffZahl) {
                            case 0:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00237");
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00238");
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00239");
                                }
                                AnimCounter = 50;
                                break;

                            case 1:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00240"));
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00241"));
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00242"));
                                }
                                AnimCounter = 70;
                                break;

                            case 2:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00243"));
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00244"));
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00245"));
                                }
                                AnimCounter = 100;
                                break;

                            case 3:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00246"));
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00247"));
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00248"));
                                }
                                AnimCounter = 120;
                                break;
                        }
                        AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, talkPointKorejty);
                        AnimTalkPerson = 62;
                        break;

                    case 2:
                        // der pred Wosuskow ist dran
                        int zuffZahl2 = (int) (Math.random() * 2.9);
                        switch (zuffZahl2) {
                            case 0:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00249");
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00250");
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00251");
                                }
                                AnimCounter = 70;
                                break;

                            case 1:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00252"));
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00253"));
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00254"));
                                }
                                AnimCounter = 100;
                                break;

                            case 2:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00255");
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00256");
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00257");
                                }
                                AnimCounter = 50;
                                break;
                        }
                        AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, talkPointWosuski);
                        AnimTalkPerson = 63;
                        break;

                    case 3:
                        // der pred Malickow ist dran
                        int zuffZahl3 = (int) (Math.random() * 4.9);
                        switch (zuffZahl3) {
                            case 0:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00258"));
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00259"));
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00260"));
                                }
                                AnimCounter = 60;
                                break;

                            case 1:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00261");
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00262");
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00263");
                                }
                                AnimCounter = 50;
                                break;

                            case 2:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00264");
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00265");
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00266");
                                }
                                AnimCounter = 60;
                                break;

                            case 3:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00267");
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00268");
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = Start.stringManager.getTranslation("Loc3_StareWiki_00269");
                                }
                                AnimCounter = 60;
                                break;

                            case 4:
                                if (mainFrame.sprache == 1) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00270"));
                                }
                                if (mainFrame.sprache == 2) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00271"));
                                }
                                if (mainFrame.sprache == 3) {
                                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc3_StareWiki_00272"));
                                }
                                AnimCounter = 70;
                                break;
                        }
                        AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, talkPointMalicki);
                        AnimTalkPerson = 64;
                        break;
                }
                AnimID = 20;
                break;

            case 20:
                // Warten, bis zu Ende geschrien
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 30;
                }
                break;

            case 30:
                mainFrame.Clipset = false;
                AnimTalkPerson = 0;
                AnimOutputText = "";
                AnimCounter = 10;
                AnimID = 5;
                break;

        }
    }

    private void ResetAnims() {
        AnimTalkPerson = 0;
        AnimCounter = 10;
        AnimOutputText = "";
        AnimID = 5;
    }
}


/* Texte 

// Pred. Malickoscow ///////////////////////////////////////////////////////

Aktionen (Krabat gibt entsprech. Gegenst#ande):

To je njepodpisana dowolnos#c. N#etko trjebam hi#s#ce originalne podpismo k p#rirunanju. P#oj zaso sem, hdy#s je ma#s. Dokument sebi tak do#lho zd#der#zu.

Podpismo Awgusta ! Ty sy mi kadli#kka ! Wokomik pro#su - a #sto mi za to da#s ?

D#ostanje#s podpisanu dowolnos#c jeno#z, hdy#z mi n#e#sto wosebite da#s.

Podpismo Awgusta pod jednym p#rikazom. Dobra ideja ! Hd#de dyrbju n#etko podpismo napodobni#c ?

Naru#kny s#l#on#kny #kasnik ! Mamamija, to je droho#cinka. Tysac raz d#dak ! #Sto m#o#zu za tebje #kini#c ? Praj, #sto ma#s na wutrobje ?

Tu ma#s - originalne podpismo krala sej zd#der#zu - snano podpisam raz awtogramowe kartki Awgusta za turistow.

Naru#kny s#l#on#kny #kasnik ! To je narunanja dos#c. Dyrbju j#on hnydom Ha#n#zi pokaza#c.

A m#oj dokument ?

(Kommt zurueck) Wodaj, to sym w hektice cyle zaby#l. Tu ma#s - originalny rukopis krala sej zd#der#zu - snano podpisam raz awtogramowe kartki Awgusta za turistow.

Naru#kny s#l#on#kny #kasnik ! To je narunanja dos#c. N#etko trjebam jeno#z hi#s#ce pobrachowacy d#d#el k napodobnjenju.

Tu ma#s potwjerd#densku. Originalny rukopis krala sej zd#der#zu - snano podpisam raz awtogramowe kartki Awgusta za turistow.


// Michal /////////////////////////////////////////////////////////

Aktion (Krabat gibt Michal Geld):

Pro#su jara - tw#oj wosu#sk. Njech #ci zes#lod#di !
D#dakuju !

*/