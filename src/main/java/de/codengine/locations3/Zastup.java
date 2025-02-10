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
import de.codengine.anims.Mato;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Zastup extends Mainloc {
    private GenericImage background, grasLinks, grasRechts;
    private GenericImage[] Schranke;
    private GenericImage Loch;
    private Mato mato;
    private Multiple2 Dialog;

    private int SchrankCount;

    private GenericPoint talkPoint;
    private Borderrect reMato;

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

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 440;
        mainFrame.krabat.zoomf = 0.67f;
        mainFrame.krabat.defScale = -10;

        // Mato noch da
        mato = new Mato(mainFrame);
        Dialog = new Multiple2(mainFrame);

        talkPoint = new GenericPoint();
        talkPoint.x = matoPoint.x + (Mato.Breite / 2);
        talkPoint.y = matoPoint.y - 50;

        reMato = new Borderrect(matoPoint.x, matoPoint.y, matoPoint.x + Mato.Breite, matoPoint.y + Mato.Hoehe);

        Schranke = new GenericImage[12];

        InitBorders();
        InitLocation(oldLocation);

        Verhinderschranke = MAX_VERHINDERSCHRANKE;

        mainFrame.Freeze(false);
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
                mainFrame.krabat.SetKrabatPos(new GenericPoint(53, 431));
                mainFrame.krabat.SetFacing(3);
                break;
            case 171:
                // von Manega aus
                // mainFrame.player.Play ("21", 151500);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(503, 400));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Grenzen intialisieren 
    private void InitBorders() {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(15, 285, 15, 285, 440, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(15, 22, 15, 143, 406, 439));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(286, 625, 286, 625, 447, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(453, 530, 395, 625, 423, 446));

        // Schranke offen ?
        if (mainFrame.Actions[575] == true) {
            mainFrame.wegGeher.vBorders.addElement
                    (new Bordertrapez(510, 513, 480, 510, 394, 422));
        }

        // Trapez-Beziehungen (Schranke offen oder nicht)
        if (mainFrame.Actions[575] == false) {
            mainFrame.wegSucher.ClearMatrix(4);

            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(0, 2);
            mainFrame.wegSucher.PosVerbinden(2, 3);
        } else {
            mainFrame.wegSucher.ClearMatrix(5);

            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(0, 2);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
        }

        // SchrankenImage festlegen
        if (mainFrame.Actions[575] == true) {
            SchrankCount = 11;
        } else {
            SchrankCount = 1;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/zastup/zastup.gif");
        grasLinks = getPicture("gfx-dd/zastup/gras-links.gif");
        grasRechts = getPicture("gfx-dd/zastup/gras-rechts.gif");

        Loch = getPicture("gfx-dd/zastup/lloch.gif");

        Schranke[1] = getPicture("gfx-dd/zastup/sr1.gif");
        Schranke[2] = getPicture("gfx-dd/zastup/sr2.gif");
        Schranke[3] = getPicture("gfx-dd/zastup/sr3.gif");
        Schranke[4] = getPicture("gfx-dd/zastup/sr4.gif");
        Schranke[5] = getPicture("gfx-dd/zastup/sr5.gif");
        Schranke[6] = getPicture("gfx-dd/zastup/sr6.gif");
        Schranke[7] = getPicture("gfx-dd/zastup/sr7.gif");
        Schranke[8] = getPicture("gfx-dd/zastup/sr8.gif");
        Schranke[9] = getPicture("gfx-dd/zastup/sr9.gif");
        Schranke[10] = getPicture("gfx-dd/zastup/sr10.gif");
        Schranke[11] = getPicture("gfx-dd/zastup/sr11.gif");

        loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
//         if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
//             {
//                 Dialog.paintMultiple (g);
//                 return;
//             }  

        // Clipping -Region initialisieren
        if (mainFrame.Clipset == false) {
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

        // Schranke zeichnen
        g.setClip(433, 264, 120, 121);
        g.drawImage(background, 0, 0, null);
        g.drawImage(Schranke[SchrankCount], 438, 264, null);
        g.drawImage(Loch, 433, 352, null);

        // Mato zeichnen, falls noch da
        if (mainFrame.Actions[576] == false) {
            g.setClip(matoPoint.x, matoPoint.y, Mato.Breite, Mato.Hoehe);
            g.drawImage(background, 0, 0, null);
            mato.drawMato(g, TalkPerson, matoPoint, isOpening, isTaking, isListening);
        }

        if (mainFrame.Actions[575] == false) {
            evalSound(); // Hier zufaellige Soundausgabe
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

        // hinter Gras ? (nur Clipping - Region wird neugezeichnet)
        if (rectGrasLinks.IsPointInRect(pKrTemp) == true) {
            g.drawImage(grasLinks, 0, 434, null);
        }
        if (rectGrasRechts.IsPointInRect(pKrTemp) == true) {
            g.drawImage(grasRechts, 436, 456, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple == true) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        // bei Multiple Choice extra Mouseroutine
        if (mainFrame.isMultiple == true) {
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
        if (mainFrame.fPlayAnim == true) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // linker Maustaste
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp) == true) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Pelz oder Papier geben, oder Ausreden, falls Mato noch da
                if ((reMato.IsPointInRect(pTemp) == true) && (mainFrame.Actions[576] == false)) {
                    if ((mainFrame.whatItem == 40) && (mainFrame.Actions[575] == false)) {
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
                if (tor.IsPointInRect(pTemp) == true) {
                    nextActionID = 160;
                    pTemp = pTor;
                }

                // Schranke Ausreden
                if ((schranke.IsPointInRect(pTemp) == true) && (mainFrame.Actions[575] == false)) {
                    nextActionID = 165;
                    pTemp = pSchranke;
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
                return;
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Karta gehen ?
                if (ausgangLinks.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (ausgangLinks.IsPointInRect(kt) == false) {
                        pTemp = pExitLinks;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitLinks.y);
                    }

                    if (mainFrame.dClick == true) {
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
                if (ausgangManega.IsPointInRect(pTemp) == true) {
                    if (mainFrame.Actions[575] == true) {
                        nextActionID = 101;
                        GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (ausgangManega.IsPointInRect(kt) == false) {
                            pTemp = pExitManega;
                        } else {
                            // es wird nach unten verlassen
                            pTemp = new GenericPoint(kt.x, pExitManega.y);
                        }

                        if (mainFrame.dClick == true) {
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
                if ((reMato.IsPointInRect(pTemp) == true) && (mainFrame.Actions[576] == false)) {
                    nextActionID = 1;
                    pTemp = pMato;
                }

                // Tor ansehen
                if (tor.IsPointInRect(pTemp) == true) {
                    nextActionID = 2;
                    pTemp = pTor;
                }

                // Schranke ansehen
                if ((schranke.IsPointInRect(pTemp) == true) && (mainFrame.Actions[575] == false)) {
                    nextActionID = 4;
                    pTemp = pSchranke;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Mato reden
                if ((reMato.IsPointInRect(pTemp) == true) &&
                        (mainFrame.Actions[576] == false)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pMato);
                    mainFrame.repaint();
                    return;
                }

                // Tor oeffnen
                if (tor.IsPointInRect(pTemp) == true) {
                    nextActionID = 3;
                    mainFrame.wegGeher.SetzeNeuenWeg(pTor);
                    mainFrame.repaint();
                    return;
                }

                // Schranke oeffnen
                if ((schranke.IsPointInRect(pTemp) == true) &&
                        (mainFrame.Actions[575] == false)) {
                    nextActionID = 5;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSchranke);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((ausgangLinks.IsPointInRect(pTemp) == true) ||
                        //  (ausgangRechts.IsPointInRect (pTemp) == true) ||
                        (ausgangManega.IsPointInRect(pTemp) == true)) {
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
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple == true) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if ((mainFrame.fPlayAnim == true) || (mainFrame.krabat.nAnimation != 0)) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            if ((tmp.IsPointInRect(pTemp) == true) ||
                    ((reMato.IsPointInRect(pTemp) == true) && (mainFrame.Actions[576] == false)) ||
                    (tor.IsPointInRect(pTemp) == true) ||
                    ((schranke.IsPointInRect(pTemp) == true) && (mainFrame.Actions[575] == false))) {
                mainFrame.invHighCursor = true;
            } else {
                mainFrame.invHighCursor = false;
            }

            if ((Cursorform != 10) && (mainFrame.invHighCursor == false)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor == true)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (ausgangLinks.IsPointInRect(pTemp) == true) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.Cleft);
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

            if (ausgangManega.IsPointInRect(pTemp) == true) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if ((tor.IsPointInRect(pTemp) == true) ||
                    ((reMato.IsPointInRect(pTemp) == true) &&
                            (mainFrame.Actions[576] == false)) ||
                    ((schranke.IsPointInRect(pTemp) == true) &&
                            (mainFrame.Actions[575] == false))) {
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

    public void evalMouseExitEvent(GenericMouseEvent e) {
        if (mainFrame.isMultiple == true) {
            Dialog.evalMouseExitEvent(e);
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultiple == true) {
            Dialog.evalKeyEvent(e);
            return;
        }

        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor == true) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim == true) {
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
            return;
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

    private void evalSound() {
        if (schnauzeLoewe == true) {
            return;
        }

        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 2.99);
            zwzfz += 49;

            mainFrame.wave.PlayFile("sfx-dd/law" + (char) zwzfz + "-2.wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering == true) ||
                (mainFrame.krabat.isWalking == true)) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
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
                // Mato anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zastup_00000"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00001"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00002"),
                        fMato, 3, 0, 0);
                break;

            case 2:
                // Tor anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zastup_00003"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00004"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00005"),
                        fTor, 3, 0, 0);
                break;

            case 3:
                // Tor oeffnen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zastup_00006"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00007"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00008"),
                        fTor, 3, 0, 0);
                break;

            case 4:
                // Schranke ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zastup_00009"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00010"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00011"),
                        fSchranke, 3, 0, 0);
                break;

            case 5:
                // Schranke oeffnen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zastup_00012"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00013"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00014"),
                        fSchranke, 3, 0, 0);
                break;

            case 6:
                //  ueber Schranke klettern
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zastup_00015"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00016"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00017"),
                        fSchranke, 3, 0, 0);
                break;

            case 10:
                // Kr. gibt Mato 5 Taler
                mainFrame.krabat.SetFacing(fMato);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                nextActionID = 300;
                break;

            case 11:
                // Kr. gibt Mato Tigerpelz
                mainFrame.krabat.SetFacing(fMato);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
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
                if ((--TakeCount) < 2) {
                    isTaking = false;
                    mainFrame.Clipset = false;
                    nextActionID = 301;
                }
                break;

            case 50:
                // Krabat beginnt MC (Mato benutzen)
                mainFrame.krabat.SetFacing(fMato);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                isListening = true;
                nextActionID = 600;
                break;

            case 100:
                // Gehe zu Karta
                NeuesBild(180, locationID);
                // Flag setzen, dass Kr. raus war -> Mato in Zastup weg, aber nur, wenn Pelz gegeben
                if (mainFrame.Actions[575] == true) {
                    mainFrame.Actions[576] = true;
                }
                break;

            case 101:
                // Gehe zu Manega
                NeuesBild(171, locationID);
                // Flag setzen, dass Kr. raus war -> Mato in Zastup weg, Pelz muss er hier gegeben haben !
                mainFrame.Actions[576] = true;
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
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00018"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00019"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00020"),
                        0, 49, 0, 800, talkPoint);
                break;

            case 301:
                //  Mato Pelz geben -> Schranke auf
                schnauzeLoewe = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00021"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00022"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00023"),
                        0, 49, 2, 302, talkPoint);
                break;

            case 302:
                //  Mato Pelz geben -> Schranke auf
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00024"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00025"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00026"),
                        0, 49, 2, 310, talkPoint);
                break;

            case 310:
                isOpening = true;
                if ((--Verhinderschranke) < 1) {
                    Verhinderschranke = MAX_VERHINDERSCHRANKE;
                    mainFrame.wave.PlayFile("sfx-dd/tack.wav");
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
                mainFrame.Actions[575] = true;
                // Pelz aus Inventory entfernen
                mainFrame.inventory.vInventory.removeElement(new Integer(36));
                // Bewegungsgrenzen neu setzen
                InitBorders();
                break;


            // Dialog mit Mato
            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    if (mainFrame.Actions[575] == false) {
                        // 1. Frage, nur wenn noch kein Pelz
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00027"), 1000, 571, new int[]{571}, 610);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00028"), 571, 570, new int[]{570}, 611);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00029"), 570, 1000, null, 612);
                    }

                    // 2. Frage kommt immer
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00030"), 1000, 1000, null, 620);

                    if (mainFrame.Actions[575] == false) {
                        // 3. Frage, nur wenn noch kein Pelz
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00031"), 1000, 572, new int[]{572}, 630);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00032"), 572, 1000, null, 631);
                    }

                    // 4. Frage, kommt immer
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00033"), 1000, 573, new int[]{573}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00034"), 573, 1000, null, 641);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00035"), 1000, 1000, null, 800);
                }

                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    if (mainFrame.Actions[575] == false) {
                        // 1. Frage
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00036"), 1000, 571, new int[]{571}, 610);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00037"), 571, 570, new int[]{570}, 611);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00038"), 570, 1000, null, 612);
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00039"), 1000, 1000, null, 620);

                    if (mainFrame.Actions[575] == false) {
                        // 3. Frage
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00040"), 1000, 572, new int[]{572}, 630);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00041"), 572, 1000, null, 631);
                    }

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00042"), 1000, 573, new int[]{573}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00043"), 573, 1000, null, 641);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00044"), 1000, 1000, null, 800);
                }

                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    if (mainFrame.Actions[575] == false) {
                        // 1. Frage
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00045"), 1000, 571, new int[]{571}, 610);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00046"), 571, 570, new int[]{570}, 611);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00047"), 570, 1000, null, 612);
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00048"), 1000, 1000, null, 620);

                    if (mainFrame.Actions[575] == false) {
                        // 3. Frage
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00049"), 1000, 572, new int[]{572}, 630);
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00050"), 572, 1000, null, 631);
                    }

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00051"), 1000, 573, new int[]{573}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00052"), 573, 1000, null, 641);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Zastup_00053"), 1000, 1000, null, 800);
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

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00054"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00055"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00056"),
                        0, 49, 2, 600, talkPoint);
                break;

            case 611:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00057"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00058"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00059"),
                        0, 49, 2, 600, talkPoint);
                break;

            case 612:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00060"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00061"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00062"),
                        0, 49, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00063"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00064"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00065"),
                        0, 49, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00066"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00067"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00068"),
                        0, 49, 2, 600, talkPoint);
                break;

            case 631:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00069"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00070"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00071"),
                        0, 49, 2, 600, talkPoint);
                break;

            // Antworten zu Frage 4 ////////////////////////////
            case 640:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00072"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00073"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00074"),
                        0, 49, 2, 600, talkPoint);
                break;

            case 641:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00075"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00076"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00077"),
                        0, 49, 2, 642, talkPoint);
                break;

            case 642:
                // Reaktion Mato
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zastup_00078"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00079"),
                        Start.stringManager.getTranslation("Loc3_Zastup_00080"),
                        0, 49, 2, 600, talkPoint);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                isListening = false;
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


/* Texte 

   Ow, tajki rjany a wosebje mjechki ko#zuch. To je prawa pod#loha za m#oj st#olc. Wutrobny d#dak, h#ol#ke. Za to m#o#ze#s skoku do h#o#ntwerskeho dwora pohlada#c. Zaw#eru #ci wo#cinju a potom p#o#ndu po lunk kofeja.

*/