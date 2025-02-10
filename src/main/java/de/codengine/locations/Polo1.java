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

package de.codengine.locations;

import de.codengine.Start;
import de.codengine.anims.BurHanza;
import de.codengine.anims.BurMichal;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Polo1 extends Mainloc {
    private GenericImage background, polo2, polo3, polo4, polo5;

    private BurMichal michael;
    private BurHanza agnes;
    private Multiple2 Dialog;

    private Borderrect hanzaRect, michalRect;
    private GenericPoint hanzaTalk, michalTalk;

    private boolean hoertkrabatzu = false;

    private boolean stein1verdeckt = false;
    private boolean stein2verdeckt = false;
    private boolean stein3verdeckt = false;


    // Konstanten - Rects
    private static final Borderrect linkerAusgang = new Borderrect(0, 351, 40, 479);
    private static final Borderrect rechterAusgang = new Borderrect(595, 245, 639, 350);
    private static final Borderrect polo2Rect = new Borderrect(492, 244, 639, 470);
    private static final Borderrect kamuskiRect = new Borderrect(192, 340, 263, 377);

    // Konstante Punkte
    private static final GenericPoint michalPoint = new GenericPoint(375, 211);
    private static final GenericPoint hanzaPoint = new GenericPoint(356, 217);
    private static final GenericPoint Pburja = new GenericPoint(445, 323);
    private static final GenericPoint Pright = new GenericPoint(639, 282);
    private static final GenericPoint Pleft = new GenericPoint(0, 414);
    private static final GenericPoint Pkamuski = new GenericPoint(275, 356);
    private static final GenericPoint pStein1 = new GenericPoint(194, 374);
    private static final GenericPoint pStein2 = new GenericPoint(215, 374);
    private static final GenericPoint pStein3 = new GenericPoint(225, 364);

    private static final int fKamuski = 9;
    private static final int fBurja = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Polo1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 9.8f;
        mainFrame.krabat.defScale = 0;

        agnes = new BurHanza(mainFrame);
        michael = new BurMichal(mainFrame);
        Dialog = new Multiple2(mainFrame);

        evalPersons();

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Personen berechnen
    private void evalPersons() {
        michalRect = new Borderrect(michalPoint.x, michalPoint.y, michalPoint.x + BurMichal.Breite, michalPoint.y + BurMichal.Hoehe);

        michalTalk = new GenericPoint(michalPoint.x + (BurMichal.Breite / 2), michalPoint.y - 50);

        hanzaRect = new Borderrect(hanzaPoint.x, hanzaPoint.y, hanzaPoint.x + BurHanza.Breite, hanzaPoint.y + BurHanza.Hoehe);

        hanzaTalk = new GenericPoint(hanzaPoint.x + (BurHanza.Breite / 2), hanzaPoint.y - 50);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 100, 0, 30, 397, 427));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(172, 360, 20, 100, 353, 396));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(592, 639, 201, 360, 299, 352));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(3);

        // Wege eintragen
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 21:
                // von Kulow aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(536, 314));
                mainFrame.krabat.SetFacing(9);
                break;
            case 10:
                // von Weiden aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(34, 413));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/polo/polo.gif");
        polo2 = getPicture("gfx/polo/polo2.gif");
        polo3 = getPicture("gfx/polo/polo3.gif");
        polo4 = getPicture("gfx/polo/polo4.gif");
        polo5 = getPicture("gfx/polo/polo5.gif");

        loadPicture();
    }

    @Override
    public void cleanup() {
        background = null;
        polo2 = null;
        polo3 = null;
        polo4 = null;
        polo5 = null;

        michael.cleanup();
        michael = null;
        agnes.cleanup();
        agnes = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
	/*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
	  {
	  Dialog.paintMultiple (g);
	  return;
	  }*/

        // Kamuski wurden aufgehoben!!!!!!!!!
	/*if (mainFrame.krabat.fAnimHelper == true)
	    {
		mainFrame.inventory.vInventory.addElement (new Integer (12));
		mainFrame.Clipset = false; 
		mainFrame.krabat.fAnimHelper = false;
		mainFrame.Actions [912] = true;
		}*/

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

        // Wenn die Steine weg, dann anderes GenericImage drueber
        if (mainFrame.Actions[912] == true) {
            g.setClip(180, 339, 102, 35);
            g.drawImage(polo3, 180, 339, null);
            g.drawImage(polo4, 211, 339, null);
            g.drawImage(polo5, 231, 339, null);
        } else // teilweise Auflesen ermoeglichen
        {
            g.setClip(180, 339, 102, 35);
            if (stein1verdeckt == true) {
                g.drawImage(polo3, 180, 339, null);
            }
            if (stein2verdeckt == true) {
                g.drawImage(polo4, 211, 339, null);
            }
            if (stein3verdeckt == true) {
                g.drawImage(polo5, 231, 339, null);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Andere Personen zeichnen (zuerst Background loeschen)
        g.setClip(michalPoint.x, michalPoint.y, BurMichal.Breite, BurMichal.Hoehe);
        g.drawImage(background, 0, 0, null);
        g.setClip(hanzaPoint.x, hanzaPoint.y, BurHanza.Breite, BurHanza.Hoehe);
        g.drawImage(background, 0, 0, null);

        // Michal
        g.setClip(michalPoint.x, michalPoint.y, BurMichal.Breite, BurMichal.Hoehe);
        michael.drawMichal(g, TalkPerson, michalPoint, hoertkrabatzu);

        // Hanza
        g.setClip(hanzaPoint.x, hanzaPoint.y, BurHanza.Breite, BurHanza.Hoehe);
        agnes.drawHanza(g, TalkPerson, hanzaPoint);

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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // hinterm polo2 (nur Clipping - Region wird neugezeichnet)
        if (polo2Rect.IsPointInRect(pKrTemp) == true) {
            // System.out.println("drawing");
            g.drawImage(polo2, 510, 240, null); //orig 510, 336
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

    @Override
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

                // Ausreden fuer Hanza
                if ((hanzaRect.IsPointInRect(pTemp) == true) && (michalRect.IsPointInRect(pTemp) == false)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 200;
                            break;
                        case 18: // bron
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = Pburja;
                }

                // Ausreden fuer Michal
                if (michalRect.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 205;
                            break;
                        case 18: // bron
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Pburja;
                }

                // Ausreden fuer Kamuski
                if ((kamuskiRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[912] == false)) {
                    switch (mainFrame.whatItem) {
                        case 17: // rohodz
                            nextActionID = 220;
                            break;
                        default:
                            nextActionID = 160;
                            break;
                    }
                    pTemp = Pkamuski;
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

                // Kamuski ansehen
                if ((kamuskiRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[912] == false)) {
                    nextActionID = 3;
                    pTemp = Pkamuski;
                }

                // zu Kulow gehen
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (rechterAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Weiden gehen
                if (linkerAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (linkerAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Hanza ansehen
                if ((hanzaRect.IsPointInRect(pTemp) == true) || (michalRect.IsPointInRect(pTemp) == true)) {
                    nextActionID = 1;
                    pTemp = Pburja;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Kulow anschauen
                if (linkerAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Weiden anschauen
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Mit den Bauern reden
                if ((michalRect.IsPointInRect(pTemp) == true) || (hanzaRect.IsPointInRect(pTemp) == true)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pburja);
                    mainFrame.repaint();
                    return;
                }

                // Kamuski nehmen
                if ((kamuskiRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[912] == false)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pkamuski);
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
            if ((tmp.IsPointInRect(pTemp) == true) || (hanzaRect.IsPointInRect(pTemp) == true) ||
                    (michalRect.IsPointInRect(pTemp) == true) || ((kamuskiRect.IsPointInRect(pTemp) == true) &&
                    (mainFrame.Actions[912] == false))) {
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
            if ((hanzaRect.IsPointInRect(pTemp) == true) ||
                    (michalRect.IsPointInRect(pTemp) == true) ||
                    ((kamuskiRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[912] == false))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 2;
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
        if (mainFrame.isMultiple == true) {
            Dialog.evalMouseExitEvent(e);
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
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
                // Burja anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00000"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00001"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00002"),
                        fBurja, 3, 0, 0);
                break;

            case 3:
                // Kamuski anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00003"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00004"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00005"),
                        fKamuski, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Bauern benutzen)
                hoertkrabatzu = true;
                mainFrame.krabat.SetFacing(12);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 600;
                break;

            case 55:
                // Kamuski mitnehmen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00006"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00007"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00008"),
                        fKamuski, 3, 2, 60);
                break;

            case 60:
                // Kamuski benutzen
                mainFrame.krabat.SetFacing(9);
                mainFrame.inventory.vInventory.addElement(new Integer(12));
                mainFrame.wegGeher.SetzeNeuenWeg(pStein1);
                nextActionID = 62;
                break;

            case 62:
                // ersten Stein aufheben
                mainFrame.krabat.SetFacing(12);
                mainFrame.krabat.nAnimation = 122;
                nextActionID = 64;
                break;

            case 64:
                // zu zweitem Stein laufen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                stein1verdeckt = true;
                mainFrame.wegGeher.SetzeNeuenWeg(pStein2);
                nextActionID = 66;
                break;

            case 66:
                // zweiten Stein aufheben
                mainFrame.krabat.SetFacing(12);
                mainFrame.krabat.nAnimation = 122;
                nextActionID = 68;
                break;

            case 68:
                // zu drittem Stein laufen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                stein2verdeckt = true;
                mainFrame.wegGeher.SetzeNeuenWeg(pStein3);
                nextActionID = 70;
                break;

            case 70:
                // dritten Stein aufheben
                mainFrame.krabat.SetFacing(12);
                mainFrame.krabat.nAnimation = 122;
                nextActionID = 72;
                break;

            case 72:
                // alles zu Ende
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                stein3verdeckt = true;
                mainFrame.Actions[912] = true;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // gehe zu Weiden
                NeuesBild(10, 11);
                break;

            case 101:
                // Goto Kulow
                NeuesBild(21, 11);
                break;

            case 150:
                // Hanza - Ausreden
                WPersonAusrede(fBurja);
                break;

            case 155:
                // Michal - Ausreden
                MPersonAusrede(fBurja);
                break;

            case 160:
                // kamuski - Ausreden
                DingAusrede(fKamuski);
                break;

            case 200:
                // kij auf hanza
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00009"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00010"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00011"),
                        fBurja, 3, 0, 0);
                break;

            case 205:
                // kij auf michal
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00012"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00013"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00014"),
                        fBurja, 3, 0, 0);
                break;

            case 210:
                // bron auf hanza
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00015"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00016"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00017"),
                        fBurja, 3, 0, 0);
                break;

            case 215:
                // bron auf michal
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00018"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00019"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00020"),
                        fBurja, 3, 0, 0);
                break;

            case 220:
                // rohodz auf kamuski
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Polo1_00021"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00022"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00023"),
                        fKamuski, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00024"), 1000, 100, new int[]{100}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00025"), 100, 101, new int[]{101}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00026"), 101, 102, new int[]{102, 106, 109}, 630);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00027"), 1000, 103, new int[]{103}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00028"), 103, 104, new int[]{104}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00029"), 104, 1000, null, 660);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00030"), 106, 107, new int[]{107}, 670);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00031"), 107, 1000, null, 680);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00032"), 109, 110, new int[]{110}, 690);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00033"), 1000, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00034"), 1000, 100, new int[]{100}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00035"), 100, 101, new int[]{101}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00036"), 101, 102, new int[]{102, 106, 109}, 630);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00037"), 1000, 103, new int[]{103}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00038"), 103, 104, new int[]{104}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00039"), 104, 1000, null, 660);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00040"), 106, 107, new int[]{107}, 670);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00041"), 107, 1000, null, 680);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00042"), 109, 110, new int[]{110}, 690);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00043"), 1000, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00044"), 1000, 100, new int[]{100}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00045"), 100, 101, new int[]{101}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00046"), 101, 102, new int[]{102, 106, 109}, 630);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00047"), 1000, 103, new int[]{103}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00048"), 103, 104, new int[]{104}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00049"), 104, 1000, null, 660);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00050"), 106, 107, new int[]{107}, 670);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00051"), 107, 1000, null, 680);

                    // 5. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00052"), 109, 110, new int[]{110}, 690);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Polo1_00053"), 1000, 1000, null, 800);
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
                // Reaktion Michal auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00054"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00055"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00056"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 620:
                // Reaktion Michal auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00057"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00058"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00059"),
                        0, 28, 2, 621, michalTalk);
                break;

            case 621:
                // Reaktion Michal auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00060"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00061"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00062"),
                        0, 28, 2, 622, michalTalk);
                break;

            case 622:
                // Reaktion Michal auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00063"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00064"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00065"),
                        0, 28, 0, 623, michalTalk);
                break;

            case 623:
                // Reaktion Hanza auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00066"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00067"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00068"),
                        0, 29, 0, 624, hanzaTalk);
                break;

            case 624:
                // Reaktion Michal auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00069"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00070"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00071"),
                        0, 28, 2, 625, michalTalk);
                break;

            case 625:
                // Reaktion Michal auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00072"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00073"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00074"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 630:
                // Reaktion Michal auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00075"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00076"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00077"),
                        0, 28, 2, 631, michalTalk);
                break;

            case 631:
                // Reaktion Michal auf 3. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00078"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00079"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00080"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 640:
                // Reaktion Michal auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00081"), Start.stringManager.getTranslation("Loc1_Polo1_00082"), Start.stringManager.getTranslation("Loc1_Polo1_00083"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 650:
                // Reaktion Michal auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00084"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00085"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00086"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 660:
                // Reaktion Michal auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00087"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00088"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00089"),
                        0, 28, 2, 661, michalTalk);
                break;

            case 661:
                // Reaktion Michal auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00090"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00091"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00092"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 670:
                // Reaktion Michal auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00093"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00094"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00095"),
                        0, 28, 2, 671, michalTalk);
                break;

            case 671:
                // Reaktion Michal auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00096"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00097"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00098"),
                        0, 28, 2, 672, michalTalk);
                break;

            case 672:
                // Reaktion Michal auf 1. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00099"), Start.stringManager.getTranslation("Loc1_Polo1_00100"), Start.stringManager.getTranslation("Loc1_Polo1_00101"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 680:
                // Reaktion Michal auf 2. Teil 4. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00102"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00103"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00104"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 690:
                // Reaktion Michal auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00105"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00106"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00107"),
                        0, 28, 2, 691, michalTalk);
                break;

            case 691:
                // Reaktion Michal auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00108"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00109"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00110"),
                        0, 28, 2, 692, michalTalk);
                break;

            case 692:
                // Reaktion Michal auf 1. Teil 5. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Polo1_00111"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00112"),
                        Start.stringManager.getTranslation("Loc1_Polo1_00113"),
                        0, 28, 2, 600, michalTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                hoertkrabatzu = false;
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