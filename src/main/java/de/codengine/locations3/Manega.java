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
import de.codengine.anims.Law;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Manega extends Mainloc {
    private GenericImage background, friedhelm;

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
    private static final String[] HLoewe = {Start.stringManager.getTranslation("Loc3_Manega_00000"), Start.stringManager.getTranslation("Loc3_Manega_00001"), Start.stringManager.getTranslation("Loc3_Manega_00002")};
    private static final String[] DLoewe = {Start.stringManager.getTranslation("Loc3_Manega_00003"), Start.stringManager.getTranslation("Loc3_Manega_00004"), Start.stringManager.getTranslation("Loc3_Manega_00005")};
    private static final String[] NLoewe = {Start.stringManager.getTranslation("Loc3_Manega_00006"), Start.stringManager.getTranslation("Loc3_Manega_00007"), Start.stringManager.getTranslation("Loc3_Manega_00008")};

    private static final String[] HVLoewe = {Start.stringManager.getTranslation("Loc3_Manega_00009"), Start.stringManager.getTranslation("Loc3_Manega_00010"), Start.stringManager.getTranslation("Loc3_Manega_00011")};
    private static final String[] DVLoewe = {Start.stringManager.getTranslation("Loc3_Manega_00012"), Start.stringManager.getTranslation("Loc3_Manega_00013"), Start.stringManager.getTranslation("Loc3_Manega_00014")};
    private static final String[] NVLoewe = {Start.stringManager.getTranslation("Loc3_Manega_00015"), Start.stringManager.getTranslation("Loc3_Manega_00016"), Start.stringManager.getTranslation("Loc3_Manega_00017")};

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

        loewePoint = new GenericPoint(loeweFeet.x - (Law.Breite / 2), loeweFeet.y - Law.Hoehe);
        loeweTalk = new GenericPoint(loeweFeet.x, loewePoint.y - 50);
        loeweRect = new Borderrect(loewePoint.x, loewePoint.y, loewePoint.x + Law.Breite, loewePoint.y + Law.Hoehe);

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(190, 367, 266, 415));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(267, 353, 287, 437));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(288, 408, 400, 442));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(401, 353, 479, 445));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(480, 368, 550, 434));

        mainFrame.wegSucher.ClearMatrix(5);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 170: // von Zastup
                mainFrame.krabat.SetKrabatPos(new GenericPoint(278, 367));
                mainFrame.krabat.SetFacing(6);
                break;
        }

        // beim Init festlegen, ob schon geschnarcht wird oder nicht
        loeweSchnarcht = mainFrame.Actions[613];
        if (loeweSchnarcht) {
            AnimTalkPerson = 68;
        }

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/manega/manega.gif");
        friedhelm = getPicture("gfx-dd/manega/friedhelm.gif");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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

        // Hintergrund, Zeug (falls noch da) und Krabat zeichnen
        g.drawImage(background, 0, 0, null);
        if (!mainFrame.Actions[611]) {
            g.drawImage(friedhelm, 365, 380, null);
        }

        // Loewen zeichnen
        g.setClip(loewePoint.x, loewePoint.y, Law.Breite, Law.Hoehe);
        g.drawImage(background, 0, 0, null);
        loewe.drawLaw(g, TalkPerson, loewePoint, AnimTalkPerson, hoertZu);

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
        if (AnimOutputText != "") {
            // Textausgabe
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
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

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Anims bedienen
        if (loeweSchnarcht) {
            DoAnims();
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
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

                // Zeugs an loewen geben
                if (loeweRect.IsPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTemp = pLoewe;
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
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Zastup gehen ?
                if (ausgangZastup.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangZastup.IsPointInRect(kt)) {
                        pTemp = pExitZastup;
                    } else {
                        pTemp = new GenericPoint(pExitZastup.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Helm ansehen
                if ((helm.IsPointInRect(pTemp)) &&
                        (!mainFrame.Actions[611])) {
                    nextActionID = 1;
                    pTemp = pHelm;
                }

                // Loewen anschauen
                if (loeweRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pLoewe;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Helm mitnehmen
                if ((helm.IsPointInRect(pTemp)) &&
                        (!mainFrame.Actions[611])) {
                    nextActionID = 4;
                    mainFrame.wegGeher.SetzeNeuenWeg(pHelm);
                    mainFrame.repaint();
                    return;
                }

                // mit Loewen reden
                if (loeweRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    mainFrame.wegGeher.SetzeNeuenWeg(pLoewe);
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
                mainFrame.Clipset = false;
                ResetAnims();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
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
            mainFrame.invHighCursor = (tmp.IsPointInRect(pTemp)) ||
                    (loeweRect.IsPointInRect(pTemp)) ||
                    ((helm.IsPointInRect(pTemp)) && (!mainFrame.Actions[611]));

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
            if (((helm.IsPointInRect(pTemp)) && (!mainFrame.Actions[611])) ||
                    (loeweRect.IsPointInRect(pTemp))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangZastup.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 9;
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
        ResetAnims();
    }

    private void evalSound(boolean schnarchen) {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        if (schnarchen) // wird hier nicht benutzt
        {
            // immer spielen, kommt eh nur selten
            mainFrame.wave.PlayFile("sfx-dd/lawspi.wav");
        } else {
            // eins von dreien als "Grrrr."
            int zf = (int) (Math.random() * 2.99);
            zf += 49;

            mainFrame.wave.PlayFile("sfx-dd/law" + (char) zf + ".wav");
        }
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
        if ((nextActionID > 499) && (nextActionID < 600) && (nextActionID != 501)) {
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
                // Helm anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00018"),
                        Start.stringManager.getTranslation("Loc3_Manega_00019"),
                        Start.stringManager.getTranslation("Loc3_Manega_00020"),
                        fHelm, 3, 0, 0);
                break;

            case 2:
                // Loewen anschauen, wenn er noch wach ist oder schon schlaeft
                if (!mainFrame.Actions[613]) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00021"), Start.stringManager.getTranslation("Loc3_Manega_00022"), Start.stringManager.getTranslation("Loc3_Manega_00023"),
                            fLoewe, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00024"), Start.stringManager.getTranslation("Loc3_Manega_00025"), Start.stringManager.getTranslation("Loc3_Manega_00026"),
                            fLoewe, 3, 0, 0);
                }
                break;

            case 3:
                // Versuch, mit Loewen zu reden
                // Hier Entscheidung, ob schon Buch gelesen
                if (!mainFrame.Actions[955]) {
                    // noch nicht gelesen
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00027"),
                            Start.stringManager.getTranslation("Loc3_Manega_00028"),
                            Start.stringManager.getTranslation("Loc3_Manega_00029"),
                            fLoewe, 3, 0, 0);
                } else {
                    // Animszene mit Loewe
                    if (!mainFrame.Actions[610]) {
                        mainFrame.fPlayAnim = true;
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        nextActionID = 200;
                    } else {
                        // Loewe ist sauer, oder wenn er schon schlaeft, will K ihn auch in Ruhe lassen
                        if (!mainFrame.Actions[613]) {
                            PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00030"),
                                    Start.stringManager.getTranslation("Loc3_Manega_00031"),
                                    Start.stringManager.getTranslation("Loc3_Manega_00032"),
                                    0, 68, 0, 0, loeweTalk);
                        } else {
                            KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00033"),
                                    Start.stringManager.getTranslation("Loc3_Manega_00034"),
                                    Start.stringManager.getTranslation("Loc3_Manega_00035"),
                                    fLoewe, 3, 0, 0);
                        }
                    }
                }
                break;

            case 4:
                // Helm mitnehmen (wenn noch da)
                if (mainFrame.Actions[613]) {
                    // darf mitnehmen
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    mainFrame.krabat.SetFacing(fHelm);
                    nextActionID = 10;
                    Counter = 5;
                    // Inventar hinzufuegen
                    mainFrame.inventory.vInventory.addElement(43);
                    mainFrame.krabat.nAnimation = 94;
                } else {
                    // Loewe wehrt sich noch
                    int zuffZahl = (int) (Math.random() * 2.9);
                    evalSound(false);
                    if (mainFrame.Actions[955]) {
                        // Krabat hat Buch gelesen, versteht Loewen also
                        PersonSagt(HLoewe[zuffZahl], DLoewe[zuffZahl], NLoewe[zuffZahl],
                                0, 68, 0, 0, loeweTalk);
                    } else {
                        // Krabat hat Buch noch nicht gelesen
                        PersonSagt(HVLoewe[zuffZahl], DVLoewe[zuffZahl], NVLoewe[zuffZahl],
                                0, 70, 0, 0, loeweTalk);
                    }
                }
                break;

            case 10:
                // Ende Helm nehmen
                if ((--Counter) == 1) {
                    mainFrame.Actions[611] = true;        // Flag setzen
                    mainFrame.Clipset = false;  // alles neu zeichnen
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00036"),
                        Start.stringManager.getTranslation("Loc3_Manega_00037"),
                        Start.stringManager.getTranslation("Loc3_Manega_00038"),
                        fLoewe, 1, 2, 210);
                break;

            case 210:
                // Antwort law
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00039"),
                        Start.stringManager.getTranslation("Loc3_Manega_00040"),
                        Start.stringManager.getTranslation("Loc3_Manega_00041"),
                        0, 68, 2, 220, loeweTalk);
                break;

            case 220:
                // Krabat
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00042"),
                        Start.stringManager.getTranslation("Loc3_Manega_00043"),
                        Start.stringManager.getTranslation("Loc3_Manega_00044"),
                        0, 1, 2, 230);
                break;

            case 230:
                // Loewe
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00045"),
                        Start.stringManager.getTranslation("Loc3_Manega_00046"),
                        Start.stringManager.getTranslation("Loc3_Manega_00047"),
                        0, 68, 2, 240, loeweTalk);
                break;

            case 240:
                // K
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00048"),
                        Start.stringManager.getTranslation("Loc3_Manega_00049"),
                        Start.stringManager.getTranslation("Loc3_Manega_00050"),
                        0, 1, 2, 250);
                break;

            case 250:
                // Loewe
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00051"),
                        Start.stringManager.getTranslation("Loc3_Manega_00052"),
                        Start.stringManager.getTranslation("Loc3_Manega_00053"),
                        0, 68, 2, 260, loeweTalk);
                break;

            case 260:
                // K
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00054"),
                        Start.stringManager.getTranslation("Loc3_Manega_00055"),
                        Start.stringManager.getTranslation("Loc3_Manega_00056"),
                        0, 1, 2, 270);
                break;

            case 270:
                // Loewe
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00057"),
                        Start.stringManager.getTranslation("Loc3_Manega_00058"),
                        Start.stringManager.getTranslation("Loc3_Manega_00059"),
                        0, 68, 2, 280, loeweTalk);
                break;

            case 280:
                // Loewe
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00060"),
                        Start.stringManager.getTranslation("Loc3_Manega_00061"),
                        Start.stringManager.getTranslation("Loc3_Manega_00062"),
                        0, 68, 2, 290, loeweTalk);
                break;

            case 290:
                // K
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00063"),
                        Start.stringManager.getTranslation("Loc3_Manega_00064"),
                        Start.stringManager.getTranslation("Loc3_Manega_00065"),
                        0, 3, 2, 300);
                break;

            case 300:
                // K
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Manega_00066"),
                        Start.stringManager.getTranslation("Loc3_Manega_00067"),
                        Start.stringManager.getTranslation("Loc3_Manega_00068"),
                        0, 1, 2, 310);
                break;

            case 310:
                // Loewe
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00069"),
                        Start.stringManager.getTranslation("Loc3_Manega_00070"),
                        Start.stringManager.getTranslation("Loc3_Manega_00071"),
                        0, 68, 2, 320, loeweTalk);
                break;

            case 320:
                // Ende Anim
                hoertZu = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.Actions[610] = true; // diese Anim nicht wiederholen
                mainFrame.repaint();
                break;

            case 501:
                // Floete, hier Extra, da mit Anim dahinter
                mainFrame.krabat.nAnimation = 2;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 600;
                break;

            case 600:
                // hier Loewenreaktion...
                // wenn noch nicht im Buch gelesen, dann passiert auch nichts...
                if (!mainFrame.Actions[955]) {
                    nextActionID = 615;
                } else {
                    // schon gelesen, also jetzt reagiert Loewe
                    if (mainFrame.krabat.nAnimation != 0) {
                        break;
                    }
                    if (!mainFrame.Actions[612]) {
                        // erstes Floetenspiel
                        mainFrame.Actions[612] = true;
                        nextActionID = 610;
                    } else {
                        if (!mainFrame.Actions[613]) {
                            // zweites Floetetnspiel
                            mainFrame.Actions[613] = true;
                            nextActionID = 620;
                        } else {
                            // weitere Floetenspiele
                            nextActionID = 0;
                            mainFrame.fPlayAnim = false;
                            evalMouseMoveEvent(mainFrame.Mousepoint);
                        }
                    }
                }
                break;

            case 610:
                // 1. Loewenreaktion
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00072"),
                        Start.stringManager.getTranslation("Loc3_Manega_00073"),
                        Start.stringManager.getTranslation("Loc3_Manega_00074"),
                        0, 68, 0, 615, loeweTalk);
                break;

            case 615:
                // Ende beider Anims
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                break;

            case 620:
                // 2. Loewenreaktion
                PersonSagt(Start.stringManager.getTranslation("Loc3_Manega_00075"),
                        Start.stringManager.getTranslation("Loc3_Manega_00076"),
                        Start.stringManager.getTranslation("Loc3_Manega_00077"),
                        0, 68, 0, 625, loeweTalk);
                break;

            case 625:
                // Backgroundanims einschalten
                loeweSchnarcht = true;
                nextActionID = 615;
                break;


            default:
                System.out.println("Falsche Action-ID !");
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
                if ((--AnimCounter) < 1) {
                    AnimID = 30;
                }
                break;

            case 30:
                // Text ueber Loewen ausgeben
                if (mainFrame.sprache == 1) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc3_Manega_00078");
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc3_Manega_00079");
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc3_Manega_00080");
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, loeweTalk);
                // evalSound (true); // schnarchen, hier nicht!!!
                AnimCounter = 50;
                AnimTalkPerson = 68;
                AnimID = 40;
                break;

            case 40:
                // warten, bis zu Ende geschnarcht
                if ((--AnimCounter) < 1) {
                    AnimID = 50;
                }
                break;

            case 50:
                // variable Pause dazwischen
                AnimOutputText = "";
                mainFrame.Clipset = false;
                AnimCounter = (int) ((Math.random() * 70) + 50);
                AnimID = 60;
                break;

            case 60:
                // Pause abwarten und von vorn...
                if ((--AnimCounter) < 1) {
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