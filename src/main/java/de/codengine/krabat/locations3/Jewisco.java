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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Jewisco extends Mainloc {
    private GenericImage backl, backr, buch;
    private boolean setScroll = false;
    private int scrollwert;

    // Konstanten - Rects
    private static final Borderrect linkerAusgang
            = new Borderrect(0, 90, 75, 450);
    /*
    private static final borderrect kerzen
	= new borderrect (158, 95, 215, 183);
    private static final borderrect stuehle
	= new borderrect (135, 288, 252, 415);
	*/
    private static final Borderrect brBuch
            = new Borderrect(213, 345, 257, 362);
    private static final Borderrect zuschauer
            = new Borderrect(883, 326, 993, 358);

    // Konstante Points
    private static final GenericPoint pExitLinks = new GenericPoint(126, 427);
    private static final GenericPoint pBuch = new GenericPoint(228, 437);
    private static final GenericPoint pZuschauer = new GenericPoint(935, 363);

    // Konstante ints
    private static final int fBuch = 12;
    private static final int fZuschauer = 12;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jewisco(Start caller, int oldLocation) {
        super(caller, 125);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 419;
        mainFrame.krabat.zoomf = 0.5f;
        mainFrame.krabat.defScale = -100;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 125, 20, 125, 458, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(126, 1170, 126, 1170, 420, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(1171, 1260, 1171, 1260, 443, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(805, 1080, 725, 1165, 376, 419));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(870, 990, 870, 1010, 361, 375));

        mainFrame.wegSucher.ClearMatrix(5);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(1, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(16, true);
                break;
            case 123: // von Hala aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(126, 430));
                mainFrame.krabat.SetFacing(3);
                scrollwert = 0;
                setScroll = true;
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx-dd/jewi/jew-l.gif");
        backr = getPicture("gfx-dd/jewi/jew-r.gif");
        buch = getPicture("gfx-dd/jewi/jkniha.gif");

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

        //  Buch zeichnen, solange noch da
        if (!mainFrame.Actions[952]) {
            g.setClip(213, 346, 45, 17);
            g.drawImage(buch, 213, 346, null);
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
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
	/*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
	  {
	  g.drawImage (weiden2, 84, 221, null);
	  }*/

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
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

                // Ausreden fuer Buch
                if ((brBuch.IsPointInRect(pTemp)) && (!mainFrame.Actions[952])) {
                    nextActionID = 150;
                    pTemp = pBuch;
                }

                // Ausreden fuer Zuschauer
                if (zuschauer.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pZuschauer;
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

                // zu Spaniska gehen ?
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = pExitLinks;
                    } else {
                        pTemp = new GenericPoint(pExitLinks.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Buch anschauen
                if ((brBuch.IsPointInRect(pTemp)) && (!mainFrame.Actions[952])) {
                    nextActionID = 1;
                    pTemp = pBuch;
                }

                if (zuschauer.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pZuschauer;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Buch mitnehmen
                if ((brBuch.IsPointInRect(pTemp)) && (!mainFrame.Actions[952])) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBuch);
                    mainFrame.repaint();
                    return;
                }

                // Zuschauer mitnehmen
                if (zuschauer.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(pZuschauer);
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
            mainFrame.invHighCursor = (tmp.IsPointInRect(pTemp)) ||
                    (zuschauer.IsPointInRect(pTemp)) ||
                    ((brBuch.IsPointInRect(pTemp)) && (!mainFrame.Actions[952]));

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
            if (((brBuch.IsPointInRect(pTemp)) && (!mainFrame.Actions[952])) ||
                    (zuschauer.IsPointInRect(pTemp))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
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
                // Buch anschauen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Jewisco_00000"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00001"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00002"),
                        fBuch, 3, 2, 2);
                break;

            case 2:
                // immer noch Buch anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Jewisco_00003"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00004"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00005"),
                        fBuch, 3, 0, 3);
                break;

            case 3:
                // Ende Buchanguck
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 5:
                // look Zuschauer
                int zfza = (int) (Math.random() * 1.99);
                switch (zfza) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Jewisco_00006"),
                                Start.stringManager.getTranslation("Loc3_Jewisco_00007"),
                                Start.stringManager.getTranslation("Loc3_Jewisco_00008"),
                                fZuschauer, 3, 0, 0);
                        break;
                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Jewisco_00009"),
                                Start.stringManager.getTranslation("Loc3_Jewisco_00010"),
                                Start.stringManager.getTranslation("Loc3_Jewisco_00011"),
                                fZuschauer, 3, 0, 0);
                        break;
                }
                break;

            case 50:
                // take book
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.nAnimation = 121;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fBuch);
                nextActionID = 55;
                Counter = 5;
                break;

            case 55:
                // Ende take book
                if ((--Counter) == 1) {
                    mainFrame.Actions[952] = true;
                    mainFrame.inventory.vInventory.addElement(55);
                    mainFrame.Clipset = false;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Jewisco_00012"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00013"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00014"),
                        fBuch, 3, 0, 60);
                break;

            case 60:
                // Ende nehmen
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 70:
                // Use Zuschauer
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Jewisco_00015"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00016"),
                        Start.stringManager.getTranslation("Loc3_Jewisco_00017"),
                        fZuschauer, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Hala
                NeuesBild(123, locationID);
                break;

            case 150:
                // Ausreden Buch
                DingAusrede(fBuch);
                break;

            case 155:
                // Ausreden Zuschauer
                DingAusrede(fZuschauer);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}