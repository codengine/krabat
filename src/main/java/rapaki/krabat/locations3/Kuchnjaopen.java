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

public class Kuchnjaopen extends Mainloc {
    private GenericImage background, herd, schwein/* , herd2, herd3 */;

    // private HlownyStraznik hlStraznik;

    // private GenericPoint talkPoint;

    // Konstanten - Rects
    private static final Borderrect rechterAusgang
            = new Borderrect(467, 120, 505, 320);
    private static final Borderrect glocke
            = new Borderrect(104, 230, 125, 252);
    private static final Borderrect drjewo
            = new Borderrect(237, 342, 305, 392);
    private static final Borderrect durje
            = new Borderrect(9, 87, 100, 380);
    private static final Borderrect wokno
            = new Borderrect(560, 0, 640, 118);
    private static final Borderrect swinjo
            = new Borderrect(454, 387, 639, 479);
    private static final Borderrect herdRect     // zum drueberzeichnen
            = new Borderrect(0, 386, 347, 479);
    private static final Borderrect schweinRect  // zum drueberzeichnen
            = new Borderrect(388, 344, 639, 479);

    // Konstante Points
    private static final GenericPoint pRight = new GenericPoint(449, 320);
    private static final GenericPoint pSwinjo = new GenericPoint(490, 430);
    private static final GenericPoint pDrjewo = new GenericPoint(260, 400);
    private static final GenericPoint pWokno = new GenericPoint(575, 355);
    // private static final GenericPoint pKachle = new GenericPoint (195, 479);
    // private static final GenericPoint pTopf   = new GenericPoint ( 66, 470);
    private static final GenericPoint pGlocke = new GenericPoint(121, 381);
    private static final GenericPoint pDurje = new GenericPoint(79, 398);

    // private static final GenericPoint pStraza   = new GenericPoint (300, 300);

    // Konstante ints
    private static final int fSwinjo = 6;
    private static final int fDrjewo = 12;
    private static final int fDurje = 9;
    private static final int fGlocke = 9;
    private static final int fWokno = 12;
    private static final int fHerd = 6;
    private static final int fHornc = 6;

    // private static final int fStraza = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Kuchnjaopen(Start caller, int oldLocation) {
        super(caller, 132);

        mainFrame.Freeze(true);

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = -80;

        // hlStraznik  = new HlownyStraznik (mainFrame, false, mainFrame.Actions[705]);
        //                                           nicht Casnik !!!!
        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {

        // Feuer-Variablen immer auf niedrigste Groesse setzen
        mainFrame.Actions[625] = false;
        mainFrame.Actions[626] = false;
        mainFrame.Actions[627] = false;

        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        // mainFrame.wegGeher.vBorders.addElement
        //	(new bordertrapez (405, 445, 400, 475, 305, 354));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(400, 311, 449, 354));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(370, 575, 360, 610, 355, 399));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(125, 610, 75, 620, 400, 430));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(75, 380, 75, 380, 431, 479));

        mainFrame.wegSucher.ClearMatrix(4);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 121:
                // von Hintergasse aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(422, 320));
                mainFrame.krabat.SetFacing(9);
                break;
            case 145:
                // von August reingesteckt worden - STRAFE !
                // Dienstkleidung zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(new Integer(41));
                // Eigene Kleidung entfernen
                mainFrame.inventory.vInventory.removeElement(new Integer(53));
                // Flag setzen -> normale Kleidung
                mainFrame.Actions[511] = false;
                mainFrame.Actions[850] = false;
                mainFrame.krabat.SetKrabatPos(new GenericPoint(370, 410));
                mainFrame.krabat.SetFacing(3);
                break;
        }

        mainFrame.CheckKrabat();

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/kuchnja/kuchnja.gif");
        herd = getPicture("gfx-dd/kuchnja/herd.gif");
        schwein = getPicture("gfx-dd/kuchnja/schwein.gif");
        // herd2      = getPicture ("gfx-dd/kuchnja/herd2.gif");
        // herd3      = getPicture ("gfx-dd/kuchnja/herd3.gif");

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

        // HlStraznik zeichnen
	/*borderrect temp = hlStraznik.straznikRect (TalkPerson);
	  // Hintergrund loeschen
	  g.setClip (temp.lo_point.x, temp.lo_point.y, (temp.ru_point.x - temp.lo_point.x), 
	  (temp.ru_point.y - temp.lo_point.y));
	  g.drawImage (background, 0, 0, null);
	  // Straznik weiterbewegen
	  hlStraznik.evalStraznik (TalkPerson, false, false, mainFrame.Actions[706]);
	  // Cliprect nun setzen
	  temp = hlStraznik.straznikRect (TalkPerson);
	  g.setClip (temp.lo_point.x, temp.lo_point.y, (temp.ru_point.x - temp.lo_point.x), 
	  (temp.ru_point.y - temp.lo_point.y));
	  // Hl. Straznik zeichnen
	  hlStraznik.drawStraznik (g, TalkPerson);*/

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

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

        // hinter Herd oder Schwein ? (nur Clipping - Region wird neugezeichnet)
        if (herdRect.IsPointInRect(pKrTemp) == true) {
            g.drawImage(herd, 25, 387, null);
        }
        if (schweinRect.IsPointInRect(pKrTemp) == true) {
            g.drawImage(schwein, 427, 337, null);
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

                // Schwein Ausreden
                if (swinjo.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 37: // kotwica
                            nextActionID = 195;
                            break;
                        default:
                            nextActionID = 160;
                            break;
                    }
                    pTemp = pSwinjo;
                }

                // Holz Ausreden
                if (drjewo.IsPointInRect(pTemp) == true) {
                    nextActionID = 165;
                    pTemp = pDrjewo;
                }

                // Tuer Ausreden
                if (durje.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        default:
                            nextActionID = 170;
                            break;
                    }
                    pTemp = pDurje;
                }

                // Glocke Ausreden
                if (glocke.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        default:
                            nextActionID = 175;
                            break;
                    }
                    pTemp = pGlocke;
                }

                // Fenster Ausreden
                if (wokno.IsPointInRect(pTemp) == true) {
                    nextActionID = 180;
                    pTemp = pWokno;
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

                // zu Hintergasse gehen ? geht hier...
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (rechterAusgang.IsPointInRect(kt) == false) {
                        pTemp = pRight;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pRight.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schwein ansehen
                if (swinjo.IsPointInRect(pTemp) == true) {
                    nextActionID = 2;
                    pTemp = pSwinjo;
                }
                // Holz ansehen
                if (drjewo.IsPointInRect(pTemp) == true) {
                    nextActionID = 3;
                    pTemp = pDrjewo;
                }
                // Tuer ansehen
                if (durje.IsPointInRect(pTemp) == true) {
                    nextActionID = 4;
                    pTemp = pDurje;
                }
                // Glocke ansehen
                if (glocke.IsPointInRect(pTemp) == true) {
                    nextActionID = 5;
                    pTemp = pGlocke;
                }
                // Fenster ansehen
                if (wokno.IsPointInRect(pTemp) == true) {
                    nextActionID = 6;
                    pTemp = pWokno;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Holz mitnehmen
                if (drjewo.IsPointInRect(pTemp) == true) {
                    nextActionID = 40;
                    mainFrame.wegGeher.SetzeNeuenWeg(pDrjewo);
                    mainFrame.repaint();
                    return;
                }

                // Schwein mitnehmen
                if (swinjo.IsPointInRect(pTemp) == true) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSwinjo);
                    mainFrame.repaint();
                    return;
                }

                // Tuer mitnehmen
                if (durje.IsPointInRect(pTemp) == true) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Glocke mitnehmen
                if (glocke.IsPointInRect(pTemp) == true) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(pGlocke);
                    mainFrame.repaint();
                    return;
                }

                // Fenster mitnehmen
                if (wokno.IsPointInRect(pTemp) == true) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWokno);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
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
                    (glocke.IsPointInRect(pTemp) == true) ||
                    (drjewo.IsPointInRect(pTemp) == true) ||
                    (durje.IsPointInRect(pTemp) == true) ||
                    (wokno.IsPointInRect(pTemp) == true) ||
                    (swinjo.IsPointInRect(pTemp) == true)) {
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
            if (rechterAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if ((glocke.IsPointInRect(pTemp) == true) ||
                    (drjewo.IsPointInRect(pTemp) == true) ||
                    (durje.IsPointInRect(pTemp) == true) ||
                    (wokno.IsPointInRect(pTemp) == true) ||
                    (swinjo.IsPointInRect(pTemp) == true)) {
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
        // if (mainFrame.isMultiple == true) Dialog.evalMouseExitEvent (e);
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
  
    /*private GenericPoint evalKucharTalkPoint ()
      {
      // Hier Position des Textes berechnen
      borderrect temp = kuchar.KucharRect ();
      GenericPoint tTalk = new GenericPoint ((temp.ru_point.x + temp.lo_point.x) / 2, temp.lo_point.y - 50);
      return tTalk;
      }*/

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
            case 2:
                // Schwein anschauen
                int zuffZahl = (int) (Math.random() * 2.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00000"),
                                Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00001"),
                                Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00002"),
                                fSwinjo, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00003"),
                                Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00004"),
                                Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00005"),
                                fSwinjo, 3, 0, 0);
                        break;

                    case 2:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00006"),
                                Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00007"),
                                Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00008"),
                                fSwinjo, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Holz anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00009"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00010"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00011"),
                        fDrjewo, 3, 0, 0);
                break;

            case 4:
                // Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00012"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00013"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00014"),
                        fDurje, 3, 0, 0);
                break;

            case 5:
                // Glocke anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00015"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00016"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00017"),
                        fGlocke, 3, 0, 0);
                break;

            case 6:
                // Fenster anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00018"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00019"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00020"),
                        fWokno, 3, 0, 0);
                break;

            case 7:
                // Herd anschauen
                if ((mainFrame.Actions[625] == false) && (mainFrame.Actions[626] == false) && (mainFrame.Actions[627] == false)) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00021"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00022"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00023"),
                            fHerd, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00024"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00025"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00026"),
                            fHerd, 3, 0, 0);
                }
                break;

            case 8:
                // Kochtopf anschauen
                if ((mainFrame.Actions[625] == false) && (mainFrame.Actions[626] == false) && (mainFrame.Actions[627] == false)) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00027"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00028"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00029"),
                            fHornc, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00030"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00031"),
                            Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00032"),
                            fHornc, 3, 0, 0);
                }
                break;


            case 40:
                // Holzscheitel mitnehmen ist hier nicht moeglich
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00033"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00034"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00035"),
                        fDrjewo, 3, 0, 0);
                break;

            case 55:
                // Schwein mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00036"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00037"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00038"),
                        fSwinjo, 3, 0, 0);
                break;

            case 60:
                // Tuer mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00039"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00040"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00041"),
                        fDurje, 3, 0, 0);
                break;

            case 65:
                // Glocke mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00042"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00043"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00044"),
                        fGlocke, 3, 0, 0);
                break;

            case 70:
                // Fenster mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00045"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00046"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00047"),
                        fWokno, 3, 0, 0);
                break;

            case 75:
                // Kochtopf mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00048"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00049"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00050"),
                        fHornc, 3, 0, 0);
                break;

            case 80:
                // Herd mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00051"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00052"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00053"),
                        fHornc, 3, 0, 0);
                break;

            case 100:
                // Animationssequenz beenden
                NeuesBild(121, locationID);
                break;

            case 155:
                // Dinge ins Feuer schmeissen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00054"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00055"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00056"),
                        fHerd, 3, 0, 0);
                break;

            case 160:
                // Dinge dem Schwein geben
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00057"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00058"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00059"),
                        fSwinjo, 3, 0, 0);
                break;

            case 165:
                // Drjewo - Ausreden
                DingAusrede(fDrjewo);
                break;

            case 170:
                // durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 175:
                // klinkac  Ausreden
                DingAusrede(fGlocke);
                break;

            case 180:
                // wokno - Ausreden
                DingAusrede(fWokno);
                break;

            case 185:
                // Dinge in den Topf werfen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00060"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00061"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00062"),
                        fHornc, 3, 0, 0);
                break;

            case 195:
                // kotwica auf swino
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00063"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00064"),
                        Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00065"),
                        fSwinjo, 3, 0, 0);
                break;

            // Sequenzen mit Kuchar  /////////////////////////////////

		/*      case 300:
			// Reaktion Krabat, wenn Holz in Ofen
			KrabatSagt (Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00066"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00067"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00068"),
			0, 3, 2, 800);
			break;

			case 301:
			// Reaktion Krabat, wenn Holz in Ofen 2 mal
			KrabatSagt (Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00069"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00070"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00071"),
			0, 3, 2, 800);
			break;

			case 302:
			// Reaktion Krabat, wenn Holz in Ofen 3 mal -> dann zur Hintergasse
			KrabatSagt (Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00072"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00073"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00074"),
			0, 3, 2, 303);
			break;

			case 303:
			// Reaktion Koch, wenn Brei ueberkocht
			PersonSagt (Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00075"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00076"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00077"),
			0, 42, 2, 304, evalKucharTalkPoint ());
			break;

			case 304:
			// Koch zum Herd bewegen
			kuchar.MoveTo (kuPointHerd);
			walkReady = false;
			nextActionID = 305;
			break;  

			case 305:
			// Warten, bis sie ausgelaufen ist
			if (walkReady == true) nextActionID = 309;
			break;
      	
      
			case 309:
			// Reaktion Koch, wenn Brei ueberkocht
			PersonSagt (Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00078"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00079"),
			Start.stringManager.getTranslation("Loc3_Kuchnjaopen_00080"),
			0, 42, 2, 310, evalKucharTalkPoint ());
			break;

			case 310:
			// Krabat geht zum Ausgang Hintergasse
			nextActionID = 100;
			mainFrame.wegGeher.SetzeNeuenWeg (pRight);
			break;*/


            case 800:
                // MC beenden, wenn zuende gelabert...
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