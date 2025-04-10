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
import de.codengine.krabat.anims.Mato;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Zastup extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Zastup.class);
    private GenericImage background;
    private GenericImage grasLinks;
    private GenericImage grasRechts;
    private final GenericImage[] Schranke;
    private GenericImage Loch;
    private final Mato mato;
    private final Multiple2 Dialog;

    private int SchrankCount;

    private final GenericPoint talkPoint;
    private final Borderrect reMato;

    private boolean isOpening = false;
    private boolean isTaking = false;
    private boolean isListening = false;

    private int TakeCount;

    private int Verhinderschranke;
    private static final int MAX_VERHINDERSCHRANKE = 15;

    // Konstanten - Rects
    private static final Borderrect ausgangLinks
            = new Borderrect(0, 310, 40, 479);
    //private static final borderrect ausgangRechts
    //    = new borderrect (600, 380, 639, 479);
    private static final Borderrect ausgangManega
            = new Borderrect(455, 275, 575, 409);
    private static final Borderrect tor
            = new Borderrect(258, 327, 328, 431);
    private static final Borderrect schranke
            = new Borderrect(454, 367, 553, 390);
    private static final Borderrect rectGrasLinks
            = new Borderrect(0, 430, 175, 479);
    private static final Borderrect rectGrasRechts
            = new Borderrect(400, 430, 640, 480);

    // Konstante Points
    private static final GenericPoint pExitLinks = new GenericPoint(16, 416);
    //private static final GenericPoint pExitRechts = new GenericPoint (625, 460);
    private static final GenericPoint pExitManega = new GenericPoint(503, 394);
    private static final GenericPoint pMato = new GenericPoint(424, 450);
    private static final GenericPoint pTor = new GenericPoint(286, 436);
    private static final GenericPoint pSchranke = new GenericPoint(497, 423);
    private static final GenericPoint matoPoint = new GenericPoint(360, 357);

    // Konstante ints
    private static final int fSchranke = 12;
    private static final int fTor = 12;
    private static final int fMato = 9;

    private boolean schnauzeLoewe = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zastup(Start caller, int oldLocation) {
        super(caller, 170);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 440;
        mainFrame.krabat.zoomf = 0.67f;
        mainFrame.krabat.defScale = -10;

        // Mato noch da
        mato = new Mato(mainFrame);
        Dialog = new Multiple2(mainFrame);

        talkPoint = new GenericPoint();
        talkPoint.x = matoPoint.x + Mato.Breite / 2;
        talkPoint.y = matoPoint.y - 50;

        reMato = new Borderrect(matoPoint.x, matoPoint.y, matoPoint.x + Mato.Breite, matoPoint.y + Mato.Hoehe);

        Schranke = new GenericImage[12];

        InitBorders();
        InitLocation(oldLocation);

        Verhinderschranke = MAX_VERHINDERSCHRANKE;

        mainFrame.freeze(false);
    }

    // Gegend intialisieren 
    private void InitLocation(int oldLocation) {
        BackgroundMusicPlayer.getInstance().stop();
        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // mainFrame.player.Play ("21", 151500);
                break;
            case 180:
                // von Karta aus
                mainFrame.krabat.setPos(new GenericPoint(53, 431));
                mainFrame.krabat.SetFacing(3);
                break;
            case 171:
                // von Manega aus
                // mainFrame.player.Play ("21", 151500);
                mainFrame.krabat.setPos(new GenericPoint(503, 400));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Grenzen intialisieren 
    private void InitBorders() {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(15, 285, 15, 285, 440, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(15, 22, 15, 143, 406, 439));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(286, 625, 286, 625, 447, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(453, 530, 395, 625, 423, 446));

        // Schranke offen ?
        if (mainFrame.actions[575]) {
            mainFrame.pathWalker.vBorders.addElement
                    (new Bordertrapez(510, 513, 480, 510, 394, 422));
        }

        // Trapez-Beziehungen (Schranke offen oder nicht)
        if (!mainFrame.actions[575]) {
            mainFrame.pathFinder.ClearMatrix(4);

            mainFrame.pathFinder.PosVerbinden(0, 1);
            mainFrame.pathFinder.PosVerbinden(0, 2);
            mainFrame.pathFinder.PosVerbinden(2, 3);
        } else {
            mainFrame.pathFinder.ClearMatrix(5);

            mainFrame.pathFinder.PosVerbinden(0, 1);
            mainFrame.pathFinder.PosVerbinden(0, 2);
            mainFrame.pathFinder.PosVerbinden(2, 3);
            mainFrame.pathFinder.PosVerbinden(3, 4);
        }

        // SchrankenImage festlegen
        if (mainFrame.actions[575]) {
            SchrankCount = 11;
        } else {
            SchrankCount = 1;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/zastup/zastup.png");
        grasLinks = getPicture("gfx-dd/zastup/gras-links.png");
        grasRechts = getPicture("gfx-dd/zastup/gras-rechts.png");

        Loch = getPicture("gfx-dd/zastup/lloch.png");

        Schranke[1] = getPicture("gfx-dd/zastup/sr1.png");
        Schranke[2] = getPicture("gfx-dd/zastup/sr2.png");
        Schranke[3] = getPicture("gfx-dd/zastup/sr3.png");
        Schranke[4] = getPicture("gfx-dd/zastup/sr4.png");
        Schranke[5] = getPicture("gfx-dd/zastup/sr5.png");
        Schranke[6] = getPicture("gfx-dd/zastup/sr6.png");
        Schranke[7] = getPicture("gfx-dd/zastup/sr7.png");
        Schranke[8] = getPicture("gfx-dd/zastup/sr8.png");
        Schranke[9] = getPicture("gfx-dd/zastup/sr9.png");
        Schranke[10] = getPicture("gfx-dd/zastup/sr10.png");
        Schranke[11] = getPicture("gfx-dd/zastup/sr11.png");

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

        // Schranke zeichnen
        g.setClip(433, 264, 120, 121);
        g.drawImage(background, 0, 0);
        g.drawImage(Schranke[SchrankCount], 438, 264);
        g.drawImage(Loch, 433, 352);

        // Mato zeichnen, falls noch da
        if (!mainFrame.actions[576]) {
            g.setClip(matoPoint.x, matoPoint.y, Mato.Breite, Mato.Hoehe);
            g.drawImage(background, 0, 0);
            mato.drawMato(g, TalkPerson, matoPoint, isOpening, isTaking, isListening);
        }

        if (!mainFrame.actions[575]) {
            evalSound(); // Hier zufaellige Soundausgabe
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

        // hinter Gras ? (nur Clipping - Region wird neugezeichnet)
        if (rectGrasLinks.IsPointInRect(pKrTemp)) {
            g.drawImage(grasLinks, 0, 434);
        }
        if (rectGrasRechts.IsPointInRect(pKrTemp)) {
            g.drawImage(grasRechts, 436, 456);
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

                // Pelz oder Papier geben, oder Ausreden, falls Mato noch da
                if (reMato.IsPointInRect(pTemp) && !mainFrame.actions[576]) {
                    if (mainFrame.whatItem == 40 && !mainFrame.actions[575]) {
                        // Taler nur dann, wenn Schranke noch zu
                        nextActionID = 10;  // 5 Taler
                    } else {
                        if (mainFrame.whatItem == 36) {
                            nextActionID = 11;  // Tigerpelz
                        } else {
                            // Extra - Sinnloszeug
                            nextActionID = 155;
                        }
                    }
                    pTemp = pMato;
                }

                // Tor Ausreden
                if (tor.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pTor;
                }

                // Schranke Ausreden
                if (schranke.IsPointInRect(pTemp) && !mainFrame.actions[575]) {
                    nextActionID = 165;
                    pTemp = pSchranke;
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

                // zu Karta gehen ?
                if (ausgangLinks.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangLinks.IsPointInRect(kt)) {
                        pTemp = pExitLinks;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitLinks.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Karta (Rechts) gehen ?
                        /*if (ausgangRechts.IsPointInRect (pTemp) == true)
                          { 
                          nextActionID = 100;
                          GenericPoint kt = mainFrame.krabat.GetKrabatPos();
          
                          // Wenn nahe am Ausgang, dann "gerade" verlassen
                          if (ausgangRechts.IsPointInRect (kt) == false)
                          {
                          pTemp = pExitRechts;
                          }
                          else
                          {
                          // es wird nach unten verlassen
                          pTemp = new GenericPoint (kt.x, pExitRechts.y);
                          }
            
                          if (mainFrame.dClick == true)
                          {
                          mainFrame.krabat.StopWalking();
                          mainFrame.repaint();
                          return;
                          }  
                          }*/

                // zu Manega gehen ? - Schranke offen ?
                if (ausgangManega.IsPointInRect(pTemp)) {
                    if (mainFrame.actions[575]) {
                        nextActionID = 101;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!ausgangManega.IsPointInRect(kt)) {
                            pTemp = pExitManega;
                        } else {
                            // es wird nach unten verlassen
                            pTemp = new GenericPoint(kt.x, pExitManega.y);
                        }

                        if (mainFrame.isDoubleClick) {
                            mainFrame.krabat.StopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    } else {
                        // die Schranke ist noch zu
                        nextActionID = 6;
                        pTemp = pSchranke;
                    }
                }

                // Mato ansehen
                if (reMato.IsPointInRect(pTemp) && !mainFrame.actions[576]) {
                    nextActionID = 1;
                    pTemp = pMato;
                }

                // Tor ansehen
                if (tor.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pTor;
                }

                // Schranke ansehen
                if (schranke.IsPointInRect(pTemp) && !mainFrame.actions[575]) {
                    nextActionID = 4;
                    pTemp = pSchranke;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Mato reden
                if (reMato.IsPointInRect(pTemp) &&
                        !mainFrame.actions[576]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(pMato);
                    mainFrame.repaint();
                    return;
                }

                // Tor oeffnen
                if (tor.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    mainFrame.pathWalker.SetzeNeuenWeg(pTor);
                    mainFrame.repaint();
                    return;
                }

                // Schranke oeffnen
                if (schranke.IsPointInRect(pTemp) &&
                        !mainFrame.actions[575]) {
                    nextActionID = 5;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSchranke);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangLinks.IsPointInRect(pTemp) ||
                        //  (ausgangRechts.IsPointInRect (pTemp) == true) ||
                        ausgangManega.IsPointInRect(pTemp)) {
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
                    reMato.IsPointInRect(pTemp) && !mainFrame.actions[576] ||
                    tor.IsPointInRect(pTemp) ||
                    schranke.IsPointInRect(pTemp) && !mainFrame.actions[575];

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
            if (ausgangLinks.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
                }
                return;
            }

                /* if (ausgangRechts.IsPointInRect (pTemp) == true) {
                   if (Cursorform != 3) {
                   mainFrame.setCursor(mainFrame.Cright);
                   Cursorform = 3;
                   }
                   return;
                   }*/

            if (ausgangManega.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
                }
                return;
            }

            if (tor.IsPointInRect(pTemp) ||
                    reMato.IsPointInRect(pTemp) &&
                            !mainFrame.actions[576] ||
                    schranke.IsPointInRect(pTemp) &&
                            !mainFrame.actions[575]) {
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

    private void evalSound() {
        if (schnauzeLoewe) {
            return;
        }

        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 2.99);
            zwzfz += 49;

            mainFrame.soundPlayer.PlayFile("sfx-dd/law" + (char) zwzfz + "-2.wav");
        }
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
                // Mato anschauen
                KrabatSagt("Zastup_1", fMato, 3, 0, 0);
                break;

            case 2:
                // Tor anschauen
                KrabatSagt("Zastup_2", fTor, 3, 0, 0);
                break;

            case 3:
                // Tor oeffnen
                KrabatSagt("Zastup_3", fTor, 3, 0, 0);
                break;

            case 4:
                // Schranke ansehen
                KrabatSagt("Zastup_4", fSchranke, 3, 0, 0);
                break;

            case 5:
                // Schranke oeffnen
                KrabatSagt("Zastup_5", fSchranke, 3, 0, 0);
                break;

            case 6:
                //  ueber Schranke klettern
                KrabatSagt("Zastup_6", fSchranke, 3, 0, 0);
                break;

            case 10:
                // Kr. gibt Mato 5 Taler
                mainFrame.krabat.SetFacing(fMato);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 300;
                break;

            case 11:
                // Kr. gibt Mato Tigerpelz
                mainFrame.krabat.SetFacing(fMato);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 134;
                TakeCount = 4;
                nextActionID = 12;
                break;

            case 12:
                // Mato nimmt
                isTaking = true;
                nextActionID = 13;
                break;

            case 13:
                // Ende nehmen timen
                if (--TakeCount < 2) {
                    isTaking = false;
                    mainFrame.isClipSet = false;
                    nextActionID = 301;
                }
                break;

            case 50:
                // Krabat beginnt MC (Mato benutzen)
                mainFrame.krabat.SetFacing(fMato);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                isListening = true;
                nextActionID = 600;
                break;

            case 100:
                // Gehe zu Karta
                NeuesBild(180, locationID);
                // Flag setzen, dass Kr. raus war -> Mato in Zastup weg, aber nur, wenn Pelz gegeben
                if (mainFrame.actions[575]) {
                    mainFrame.actions[576] = true;
                }
                break;

            case 101:
                // Gehe zu Manega
                NeuesBild(171, locationID);
                // Flag setzen, dass Kr. raus war -> Mato in Zastup weg, Pelz muss er hier gegeben haben !
                mainFrame.actions[576] = true;
                break;

            case 155:
                // Mato - Ausreden
                MPersonAusrede(fMato);
                break;

            case 160:
                // Tor - Ausreden
                DingAusrede(fTor);
                break;

            case 165:
                // Schranke - Ausreden
                DingAusrede(fSchranke);
                break;

            // Sequenzen mit Mato  /////////////////////////////////

            case 300:
                //  Mato 5 Taler geben -> zuwenig
                PersonSagt("Zastup_7", 0, 49, 0, 800, talkPoint);
                break;

            case 301:
                //  Mato Pelz geben -> Schranke auf
                schnauzeLoewe = true;
                PersonSagt("Zastup_8", 0, 49, 2, 302, talkPoint);
                break;

            case 302:
                //  Mato Pelz geben -> Schranke auf
                PersonSagt("Zastup_9", 0, 49, 2, 310, talkPoint);
                break;

            case 310:
                isOpening = true;
                if (--Verhinderschranke < 1) {
                    Verhinderschranke = MAX_VERHINDERSCHRANKE;
                    mainFrame.soundPlayer.PlayFile("sfx-dd/tack.wav");
                    SchrankCount++;
                }
                if (SchrankCount == 11) {
                    nextActionID = 350;
                }
                break;

            case 350:
                isOpening = false;
                // Anim zu Ende, Schranke auf
                nextActionID = 800;
                // Krabat darf dann zur Manega raus
                mainFrame.actions[575] = true;
                // Pelz aus Inventory entfernen
                mainFrame.inventory.vInventory.removeElement(36);
                // Bewegungsgrenzen neu setzen
                InitBorders();
                break;


            // Dialog mit Mato
            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (!mainFrame.actions[575]) {
                    // 1. Frage, nur wenn noch kein Pelz
                    Dialog.ExtendMC("Zastup_19", 1000, 571, new int[]{571}, 610);
                    Dialog.ExtendMC("Zastup_20", 571, 570, new int[]{570}, 611);
                    Dialog.ExtendMC("Zastup_21", 570, 1000, null, 612);
                }

                // 2. Frage kommt immer
                Dialog.ExtendMC("Zastup_22", 1000, 1000, null, 620);

                if (!mainFrame.actions[575]) {
                    // 3. Frage, nur wenn noch kein Pelz
                    Dialog.ExtendMC("Zastup_23", 1000, 572, new int[]{572}, 630);
                    Dialog.ExtendMC("Zastup_24", 572, 1000, null, 631);
                }

                // 4. Frage, kommt immer
                Dialog.ExtendMC("Zastup_25", 1000, 573, new int[]{573}, 640);
                Dialog.ExtendMC("Zastup_26", 573, 1000, null, 641);

                // 5. Frage
                Dialog.ExtendMC("Zastup_27", 1000, 1000, null, 800);

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

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Mato
                PersonSagt("Zastup_10", 0, 49, 2, 600, talkPoint);
                break;

            case 611:
                // Reaktion Mato
                PersonSagt("Zastup_11", 0, 49, 2, 600, talkPoint);
                break;

            case 612:
                // Reaktion Mato
                PersonSagt("Zastup_12", 0, 49, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Mato
                PersonSagt("Zastup_13", 0, 49, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion Mato
                PersonSagt("Zastup_14", 0, 49, 2, 600, talkPoint);
                break;

            case 631:
                // Reaktion Mato
                PersonSagt("Zastup_15", 0, 49, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 4 ////////////////////////////
            case 640:
                // Reaktion Mato
                PersonSagt("Zastup_16", 0, 49, 2, 600, talkPoint);
                break;

            case 641:
                // Reaktion Mato
                PersonSagt("Zastup_17", 0, 49, 2, 642, talkPoint);
                break;

            case 642:
                // Reaktion Mato
                PersonSagt("Zastup_18", 0, 49, 2, 600, talkPoint);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                isListening = false;
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


/* Texte 

   Ow, tajki rjany a wosebje mjechki ko#zuch. To je prawa pod#loha za m#oj st#olc. Wutrobny d#dak, h#ol#ke. Za to m#o#ze#s skoku do h#o#ntwerskeho dwora pohlada#c. Zaw#eru #ci wo#cinju a potom p#o#ndu po lunk kofeja.

*/