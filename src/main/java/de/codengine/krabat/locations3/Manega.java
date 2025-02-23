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
import de.codengine.krabat.anims.Law;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Manega extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Manega.class);
    private GenericImage background;
    private GenericImage friedhelm;

    private final Law loewe;

    private final GenericPoint loewePoint;
    private final GenericPoint loeweTalk;
    private final Borderrect loeweRect;

    private boolean loeweSchnarcht = false;
    private boolean hoertZu = false;

    private int AnimID = 10;
    private int AnimCounter = 0;
    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos = new GenericPoint(0, 0);
    private int AnimTalkPerson = 0;

    // Konstanten - Rects
    private static final Borderrect ausgangZastup
            = new Borderrect(198, 270, 256, 350);
    private static final Borderrect helm
            = new Borderrect(365, 380, 381, 390);

    // Konstante Points
    private static final GenericPoint pExitZastup = new GenericPoint(267, 345);
    private static final GenericPoint pHelm = new GenericPoint(403, 392);
    private static final GenericPoint loeweFeet = new GenericPoint(334, 389);
    private static final GenericPoint pLoewe = new GenericPoint(403, 392);

    // Konstante ints
    private static final int fHelm = 9;
    private static final int fLoewe = 9;

    // Konstante Strings
    private static final String[] LION_TEXTS = {"Manega_21", "Manega_22", "Manega_23"};
    private static final String[] VLION_TEXTS = {"Manega_24", "Manega_25", "Manega_26"};

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Manega(Start caller, int oldLocation) {
        super(caller, 171);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 465;
        mainFrame.krabat.zoomf = 12.5f;
        mainFrame.krabat.defScale = 20;

        loewe = new Law(mainFrame);

        loewePoint = new GenericPoint(loeweFeet.x - Law.Breite / 2, loeweFeet.y - Law.Hoehe);
        loeweTalk = new GenericPoint(loeweFeet.x, loewePoint.y - 50);
        loeweRect = new Borderrect(loewePoint.x, loewePoint.y, loewePoint.x + Law.Breite, loewePoint.y + Law.Hoehe);

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(190, 367, 266, 415));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(267, 353, 287, 437));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(288, 408, 400, 442));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(401, 353, 479, 445));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(480, 368, 550, 434));

        mainFrame.pathFinder.ClearMatrix(5);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 170: // von Zastup
                mainFrame.krabat.setPos(new GenericPoint(278, 367));
                mainFrame.krabat.SetFacing(6);
                break;
        }

        // beim Init festlegen, ob schon geschnarcht wird oder nicht
        loeweSchnarcht = mainFrame.actions[613];
        if (loeweSchnarcht) {
            AnimTalkPerson = 68;
        }

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/manega/manega.png");
        friedhelm = getPicture("gfx-dd/manega/friedhelm.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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

        // Hintergrund, Zeug (falls noch da) und Krabat zeichnen
        g.drawImage(background, 0, 0);
        if (!mainFrame.actions[611]) {
            g.drawImage(friedhelm, 365, 380);
        }

        // Loewen zeichnen
        g.setClip(loewePoint.x, loewePoint.y, Law.Breite, Law.Hoehe);
        g.drawImage(background, 0, 0);
        loewe.drawLaw(g, TalkPerson, loewePoint, AnimTalkPerson, hoertZu);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
        /*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
          {
          g.drawImage (weiden2, 84, 221, null);
          }*/

        // Ausgabe von Animoutputtext
        if (!Objects.equals(AnimOutputText, "")) {
            // Textausgabe
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
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

        // Anims bedienen
        if (loeweSchnarcht) {
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
        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
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

                // Zeugs an loewen geben
                if (loeweRect.IsPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTemp = pLoewe;
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

                // zu Zastup gehen ?
                if (ausgangZastup.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangZastup.IsPointInRect(kt)) {
                        pTemp = pExitZastup;
                    } else {
                        pTemp = new GenericPoint(pExitZastup.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Helm ansehen
                if (helm.IsPointInRect(pTemp) &&
                        !mainFrame.actions[611]) {
                    nextActionID = 1;
                    pTemp = pHelm;
                }

                // Loewen anschauen
                if (loeweRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pLoewe;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Helm mitnehmen
                if (helm.IsPointInRect(pTemp) &&
                        !mainFrame.actions[611]) {
                    nextActionID = 4;
                    mainFrame.pathWalker.SetzeNeuenWeg(pHelm);
                    mainFrame.repaint();
                    return;
                }

                // mit Loewen reden
                if (loeweRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    mainFrame.pathWalker.SetzeNeuenWeg(pLoewe);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangZastup.IsPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.isClipSet = false;
                ResetAnims();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
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
                    loeweRect.IsPointInRect(pTemp) ||
                    helm.IsPointInRect(pTemp) && !mainFrame.actions[611];

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
            if (helm.IsPointInRect(pTemp) && !mainFrame.actions[611] ||
                    loeweRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangZastup.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
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
        ResetAnims();
    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        // eins von dreien als "Grrrr."
        int zf = (int) (Math.random() * 2.99);
        zf += 49;

        mainFrame.soundPlayer.PlayFile("sfx-dd/law" + (char) zf + ".wav");
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, 
        // wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 501) {
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
                // Helm anschauen
                KrabatSagt("Manega_1", fHelm, 3, 0, 0);
                break;

            case 2:
                // Loewen anschauen, wenn er noch wach ist oder schon schlaeft
                if (!mainFrame.actions[613]) {
                    KrabatSagt("Manega_2", fLoewe, 3, 0, 0);
                } else {
                    KrabatSagt("Manega_3", fLoewe, 3, 0, 0);
                }
                break;

            case 3:
                // Versuch, mit Loewen zu reden
                // Hier Entscheidung, ob schon Buch gelesen
                if (!mainFrame.actions[955]) {
                    // noch nicht gelesen
                    KrabatSagt("Manega_4", fLoewe, 3, 0, 0);
                } else {
                    // Animszene mit Loewe
                    if (!mainFrame.actions[610]) {
                        mainFrame.isAnimRunning = true;
                        evalMouseMoveEvent(mainFrame.mousePoint);
                        nextActionID = 200;
                    } else {
                        // Loewe ist sauer, oder wenn er schon schlaeft, will K ihn auch in Ruhe lassen
                        if (!mainFrame.actions[613]) {
                            PersonSagt("Manega_5", 0, 68, 0, 0, loeweTalk);
                        } else {
                            KrabatSagt("Manega_6", fLoewe, 3, 0, 0);
                        }
                    }
                }
                break;

            case 4:
                // Helm mitnehmen (wenn noch da)
                if (mainFrame.actions[613]) {
                    // darf mitnehmen
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    mainFrame.krabat.SetFacing(fHelm);
                    nextActionID = 10;
                    Counter = 5;
                    // Inventar hinzufuegen
                    mainFrame.inventory.vInventory.addElement(43);
                    mainFrame.krabat.nAnimation = 94;
                } else {
                    // Loewe wehrt sich noch
                    int zuffZahl = (int) (Math.random() * 2.9);
                    evalSound();
                    if (mainFrame.actions[955]) {
                        // Krabat hat Buch gelesen, versteht Loewen also
                        PersonSagt(LION_TEXTS[zuffZahl], 0, 68, 0, 0, loeweTalk);
                    } else {
                        // Krabat hat Buch noch nicht gelesen
                        PersonSagt(VLION_TEXTS[zuffZahl], 0, 70, 0, 0, loeweTalk);
                    }
                }
                break;

            case 10:
                // Ende Helm nehmen
                if (--Counter == 1) {
                    mainFrame.actions[611] = true;        // Flag setzen
                    mainFrame.isClipSet = false;  // alles neu zeichnen
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Zastup
                NeuesBild(170, locationID);
                break;

            case 150:
                // Dinge an Loewen geben
                MPersonAusrede(fLoewe);
                break;

            case 200:
                // Animszene mit Loewe
                hoertZu = true;
                KrabatSagt("Manega_7", fLoewe, 1, 2, 210);
                break;

            case 210:
                // Antwort law
                PersonSagt("Manega_8", 0, 68, 2, 220, loeweTalk);
                break;

            case 220:
                // Krabat
                KrabatSagt("Manega_9", 0, 1, 2, 230);
                break;

            case 230:
                // Loewe
                PersonSagt("Manega_10", 0, 68, 2, 240, loeweTalk);
                break;

            case 240:
                // K
                KrabatSagt("Manega_11", 0, 1, 2, 250);
                break;

            case 250:
                // Loewe
                PersonSagt("Manega_12", 0, 68, 2, 260, loeweTalk);
                break;

            case 260:
                // K
                KrabatSagt("Manega_13", 0, 1, 2, 270);
                break;

            case 270:
                // Loewe
                PersonSagt("Manega_14", 0, 68, 2, 280, loeweTalk);
                break;

            case 280:
                // Loewe
                PersonSagt("Manega_15", 0, 68, 2, 290, loeweTalk);
                break;

            case 290:
                // K
                KrabatSagt("Manega_16", 0, 3, 2, 300);
                break;

            case 300:
                // K
                KrabatSagt("Manega_17", 0, 1, 2, 310);
                break;

            case 310:
                // Loewe
                PersonSagt("Manega_18", 0, 68, 2, 320, loeweTalk);
                break;

            case 320:
                // Ende Anim
                hoertZu = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.actions[610] = true; // diese Anim nicht wiederholen
                mainFrame.repaint();
                break;

            case 501:
                // Floete, hier Extra, da mit Anim dahinter
                mainFrame.krabat.nAnimation = 2;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 600:
                // hier Loewenreaktion...
                // wenn noch nicht im Buch gelesen, dann passiert auch nichts...
                if (!mainFrame.actions[955]) {
                    nextActionID = 615;
                } else {
                    // schon gelesen, also jetzt reagiert Loewe
                    if (mainFrame.krabat.nAnimation != 0) {
                        break;
                    }
                    if (!mainFrame.actions[612]) {
                        // erstes Floetenspiel
                        mainFrame.actions[612] = true;
                        nextActionID = 610;
                    } else {
                        if (!mainFrame.actions[613]) {
                            // zweites Floetetnspiel
                            mainFrame.actions[613] = true;
                            nextActionID = 620;
                        } else {
                            // weitere Floetenspiele
                            nextActionID = 0;
                            mainFrame.isAnimRunning = false;
                            evalMouseMoveEvent(mainFrame.mousePoint);
                        }
                    }
                }
                break;

            case 610:
                // 1. Loewenreaktion
                PersonSagt("Manega_19", 0, 68, 0, 615, loeweTalk);
                break;

            case 615:
                // Ende beider Anims
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                break;

            case 620:
                // 2. Loewenreaktion
                PersonSagt("Manega_20", 0, 68, 0, 625, loeweTalk);
                break;

            case 625:
                // Backgroundanims einschalten
                loeweSchnarcht = true;
                nextActionID = 615;
                break;


            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    // Schnarchanim des Loewen ausfuehren
    private void DoAnims() {
        switch (AnimID) {

            case 10:
                // Warteschleife, damit Menu problemlos...
                AnimCounter = 10;
                AnimID = 20;
                break;

            case 20:
                // warten...
                if (--AnimCounter < 1) {
                    AnimID = 30;
                }
                break;

            case 30:
                // Text ueber Loewen ausgeben
                AnimOutputText = Start.stringManager.getTranslation("Manega_27");
                AnimOutputTextPos = mainFrame.imageFont.CenterAnimText(AnimOutputText, loeweTalk);
                // evalSound (true); // schnarchen, hier nicht!!!
                AnimCounter = 50;
                AnimTalkPerson = 68;
                AnimID = 40;
                break;

            case 40:
                // warten, bis zu Ende geschnarcht
                if (--AnimCounter < 1) {
                    AnimID = 50;
                }
                break;

            case 50:
                // variable Pause dazwischen
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimCounter = (int) (Math.random() * 70 + 50);
                AnimID = 60;
                break;

            case 60:
                // Pause abwarten und von vorn...
                if (--AnimCounter < 1) {
                    AnimID = 10;
                }
                break;
        }
    }

    // Anims zuruecksetzen, damit leerer Screen bei Menu usw...
    private void ResetAnims() {
        AnimOutputText = "";
        AnimCounter = 10;
        AnimID = 10;
    }
}