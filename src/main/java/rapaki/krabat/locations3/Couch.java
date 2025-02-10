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

package rapaki.krabat.locations3;

import rapaki.krabat.Start;
import rapaki.krabat.main.*;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.sound.BackgroundMusicPlayer;

public class Couch extends Mainloc {
    private GenericImage background, offeneTuer;
    private GenericImage vordertuer, fallen;

    private boolean istTuerOffen = false;

    // Konstanten - Rects
    private static final Borderrect rechterAusgang
            = new Borderrect(490, 165, 590, 418);
    private static final Borderrect untererAusgang
            = new Borderrect(120, 445, 500, 479);
    private static final Borderrect durje
            = new Borderrect(257, 212, 347, 375);
    private static final Borderrect couch
            = new Borderrect(53, 115, 203, 386);

    // Konstante Points
    private static final GenericPoint pExitRight = new GenericPoint(510, 426);
    private static final GenericPoint pExitDown = new GenericPoint(315, 479);
    private static final GenericPoint pDurje = new GenericPoint(280, 383);
    private static final GenericPoint pCouch = new GenericPoint(145, 406);

    // Konstante ints
    private static final int fDurje = 12;
    private static final int fCouch = 9;

    private int SonderAnim = 0;

    private int Fallgeschwindigkeit = 1;

    private static final int MAX_FALLGESCHWINDIGKEIT = 18;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Couch(Start caller, int oldLocation) {
        super(caller, 144);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 4.8f;
        mainFrame.krabat.defScale = -60;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(265, 370, 85, 370, 383, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(371, 505, 371, 500, 426, 479));

        mainFrame.wegSucher.ClearMatrix(2);

        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 143: // von Casnik aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(510, 426));
                mainFrame.krabat.SetFacing(9);
                break;
            case 145: // von Zelen aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(315, 465));
                mainFrame.krabat.SetFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/couch/couch.gif");
        offeneTuer = getPicture("gfx-dd/couch/couch3.gif");

        vordertuer = getPicture("gfx-dd/couch/couch2.gif");
        fallen = getPicture("gfx-dd/couch/s-o-fallen.gif");

        loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation(GenericDrawingContext g) {

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

        // Tuer zeichnen, wenn geoeffnet
        if (istTuerOffen == true) {
            g.setClip(253, 211, 54, 171);
            g.drawImage(offeneTuer, 253, 211, null);
            g.drawImage(vordertuer, 219, 233, null);
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        mainFrame.wegGeher.GeheWeg();

        if (SonderAnim != 0) {
            // hier erstmal alles berechnen, dann je nachdem die Bilder switchen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.GetKrabatPos().x, mainFrame.krabat.GetKrabatPos().y);

            // Groesse
            int scale = mainFrame.krabat.defScale;
            scale += (int) (((float) mainFrame.krabat.maxx - (float) hier.y) / mainFrame.krabat.zoomf);

            // System.out.println ("Scale ist " + scale + " gross.");

            // Hoehe: nur offset
            int hoch = (int) (100 - scale);

            // Breite abhaengig von Hoehe...
            int weit = (int) (50 - (scale / 2));

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            g.drawImage(fallen, hier.x, hier.y, weit, hoch, null);

            Fallgeschwindigkeit++;
            if (Fallgeschwindigkeit == MAX_FALLGESCHWINDIGKEIT) {
                SonderAnim = 0;
            }

            GenericPoint tmp = mainFrame.krabat.GetKrabatPos();
            tmp.y += Fallgeschwindigkeit * 2;
            mainFrame.krabat.SetKrabatPos(tmp);
        } else {
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
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
	/*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
	  {
	  g.drawImage (weiden2, 84, 221, null);
	  }*/

        if (istTuerOffen == true) {
            g.drawImage(vordertuer, 219, 233, null);
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

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

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

                // Ausreden fuer Tuer
                if (durje.IsPointInRect(pTemp) == true) {
                    nextActionID = 150;
                    pTemp = pDurje;
                }

                // Ausreden fuer couch
                if (couch.IsPointInRect(pTemp) == true) {
                    nextActionID = 155;
                    pTemp = pCouch;
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

                // zu Casnik gehen ?
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (rechterAusgang.IsPointInRect(kt) == false) {
                        pTemp = pExitRight;
                    } else {
                        pTemp = new GenericPoint(pExitRight.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Zelen gehen ?
                if (untererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (untererAusgang.IsPointInRect(kt) == false) {
                        pTemp = pExitDown;
                    } else {
                        pTemp = new GenericPoint(pExitDown.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Tuer anschauen
                if (durje.IsPointInRect(pTemp) == true) {
                    nextActionID = 1;
                    pTemp = pDurje;
                }

                // Couch anschauen
                if (couch.IsPointInRect(pTemp) == true) {
                    nextActionID = 2;
                    pTemp = pCouch;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((untererAusgang.IsPointInRect(pTemp) == true) ||
                        (rechterAusgang.IsPointInRect(pTemp))) {
                    return;
                }

                // durje benutzen
                if (durje.IsPointInRect(pTemp) == true) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Couch benutzen
                if (couch.IsPointInRect(pTemp) == true) {
                    nextActionID = 90;
                    mainFrame.wegGeher.SetzeNeuenWeg(pCouch);
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
    public void evalMouseMoveEvent(GenericPoint pTemp) {
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
                    (durje.IsPointInRect(pTemp) == true) ||
                    (couch.IsPointInRect(pTemp) == true)) {
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
            if ((durje.IsPointInRect(pTemp) == true) ||
                    (couch.IsPointInRect(pTemp) == true)) {
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

            if (untererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 6;
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
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
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
                // durje ansehen
                if (mainFrame.Actions[670] == false) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00000"),
                            Start.stringManager.getTranslation("Loc3_Couch_00001"),
                            Start.stringManager.getTranslation("Loc3_Couch_00002"),
                            fDurje, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00003"),
                            Start.stringManager.getTranslation("Loc3_Couch_00004"),
                            Start.stringManager.getTranslation("Loc3_Couch_00005"),
                            fDurje, 3, 0, 0);
                }
                break;

            case 2:
                // Couch ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00006"),
                        Start.stringManager.getTranslation("Loc3_Couch_00007"),
                        Start.stringManager.getTranslation("Loc3_Couch_00008"),
                        fCouch, 3, 0, 0);
                break;

            case 50:
                // Durje oeffnen
                // keine 2 Mal durchfuehren
                if (mainFrame.Actions[670] == true) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00009"),
                            Start.stringManager.getTranslation("Loc3_Couch_00010"),
                            Start.stringManager.getTranslation("Loc3_Couch_00011"),
                            fDurje, 3, 0, 0);
                } else {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    BackgroundMusicPlayer.getInstance().stop();
                    nextActionID = 55;
                    mainFrame.Actions[670] = true;
                    istTuerOffen = true;
                    mainFrame.wave.PlayFile("sfx/kdurjeauf.wav");
                    mainFrame.krabat.SetFacing(fDurje);
                }
                break;

            case 55:
                // Animszene Tuer ist offen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00012"),
                        Start.stringManager.getTranslation("Loc3_Couch_00013"),
                        Start.stringManager.getTranslation("Loc3_Couch_00014"),
                        0, 3, 2, 60);
                break;

            case 60:
                // Animszene Tuer ist offen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00015"),
                        Start.stringManager.getTranslation("Loc3_Couch_00016"),
                        Start.stringManager.getTranslation("Loc3_Couch_00017"),
                        0, 3, 2, 70);
                break;

		/*case 65:
		// Animszene Tuer ist offen
		KrabatSagt (Start.stringManager.getTranslation("Loc3_Couch_00018"),
			    Start.stringManager.getTranslation("Loc3_Couch_00019"),
			    Start.stringManager.getTranslation("Loc3_Couch_00020"),
			    0, 3, 2, 70);
			    break;*/

            case 70:
                // runterfallen lassen
                SonderAnim = 1;
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00021"),
                        Start.stringManager.getTranslation("Loc3_Couch_00022"),
                        Start.stringManager.getTranslation("Loc3_Couch_00023"),
                        0, 3, 2, 75);
                break;

            case 75:
                // Gehe zu Murja draussen
                if (SonderAnim != 0) {
                    break;
                }
                Counter = 20;
                nextActionID = 76;
                break;

            case 76:
                // bisschen warten, bevor Sound
                if ((--Counter) > 0) {
                    break;
                }
                mainFrame.wave.PlayFile("sfx-dd/gebuesch.wav");
                Counter = 15;
                nextActionID = 77;
                break;

            case 77:
                // warten und Locationswitch
                if ((--Counter) > 1) {
                    break;
                }
                NeuesBild(126, locationID);
                break;

            case 90:
                // Couch mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Couch_00024"),
                        Start.stringManager.getTranslation("Loc3_Couch_00025"),
                        Start.stringManager.getTranslation("Loc3_Couch_00026"),
                        fCouch, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Casnik
                NeuesBild(143, locationID);
                break;

            case 101:
                // Gehe zu Zelen
                NeuesBild(145, locationID);
                break;

            case 150:
                // durje-Ausreden
                DingAusrede(fDurje);
                break;

            case 155:
                // couch-Ausreden
                DingAusrede(fCouch);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}