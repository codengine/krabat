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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Kapala extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Kapala.class);
    private GenericImage background;
    private GenericImage rolle1;
    private GenericImage rolle2;
    private final GenericImage[] Feuer;

    // Konstanten - Rects
    private static final Borderrect ausgangGang
            = new Borderrect(180, 365, 285, 432);
    private static final Borderrect papierRollen
            = new Borderrect(425, 415, 505, 448);
    private static final Borderrect papierRolle1
            = new Borderrect(434, 363, 456, 380);
    private static final Borderrect papierRolle2
            = new Borderrect(458, 368, 475, 385);

    private static final int Feuerwidth = 50;
    private int Feuercount = 0;
    private int Verhinderfeuer;
    private static final int MAX_VERHINDERFEUER = 2;

    // private boolean zeigeSkizze = false;

    // Konstante Points
    private static final GenericPoint pExitGang = new GenericPoint(295, 425);
    private static final GenericPoint pRollen = new GenericPoint(421, 457);
    private static final GenericPoint pRolle1 = new GenericPoint(431, 465);
    private static final GenericPoint pRolle2 = new GenericPoint(455, 475);
    private static final GenericPoint FeuerMitte = new GenericPoint(540, 278);

    // Konstante ints
    private static final int fRolle1 = 12;
    private static final int fRolle2 = 12;
    private static final int fRolleUnten = 12;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Kapala(Start caller, int oldLocation) {
        super(caller, 153);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(15, true);

        Feuer = new GenericImage[11];

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 2.5f;
        mainFrame.krabat.defScale = -50;

        Verhinderfeuer = MAX_VERHINDERFEUER;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(304, 372, 354, 372, 426, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(373, 467, 432, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(433, 434, 433, 469, 467, 479));

        mainFrame.wegSucher.ClearMatrix(3);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 152: // von Gang
                mainFrame.krabat.setPos(new GenericPoint(317, 438));
                mainFrame.krabat.SetFacing(3);
                break;
        }

        // Merken, dass Krabat hier schon einal drin war -> Frage bei Dinglinger raus
        mainFrame.Actions[631] = true;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/kapala/kapala.gif");
        rolle1 = getPicture("gfx-dd/kapala/rolla1.gif");
        rolle2 = getPicture("gfx-dd/kapala/rolla2.gif");

        Feuer[0] = getPicture("gfx/wjes/wn0.gif");
        Feuer[1] = getPicture("gfx/wjes/wn1.gif");
        Feuer[2] = getPicture("gfx/wjes/wn2.gif");
        Feuer[3] = getPicture("gfx/wjes/wn3.gif");
        Feuer[4] = getPicture("gfx/wjes/wn4.gif");
        Feuer[5] = getPicture("gfx/wjes/wn5.gif");
        Feuer[6] = getPicture("gfx/wjes/wn6.gif");
        Feuer[7] = getPicture("gfx/wjes/wn7.gif");
        Feuer[8] = getPicture("gfx/wjes/wn8.gif");
        Feuer[9] = getPicture("gfx/wjes/wn9.gif");
        Feuer[10] = getPicture("gfx/wjes/wn10.gif");

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

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // 1. Papierrolle immer zeichnen, 2. nur, wenn noch da
        g.setClip(434, 363, 42, 23);
        g.drawImage(background, 0, 0);
        if (!mainFrame.Actions[632]) {
            g.drawImage(rolle1, 434, 363);
        }
        if (!mainFrame.Actions[630]) {
            g.drawImage(rolle2, 458, 368);
        }

        // Feuer animieren
        if (--Verhinderfeuer < 1) {
            Verhinderfeuer = MAX_VERHINDERFEUER;
            Feuercount++;
            if (Feuercount == 11) {
                Feuercount = 0;
            }

        }

        g.setClip(520, 230, 50, 50);
        g.drawImage(background, 0, 0);
        g.drawImage(Feuer[Feuercount], FeuerMitte.x - Feuerwidth / 2, FeuerMitte.y - Feuerwidth, Feuerwidth, Feuerwidth);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        mainFrame.wegGeher.GeheWeg();

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
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
	/*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
	  {
	  g.drawImage (weiden2, 84, 221, null);
	  }*/

        // grosses Skizzenbild zeigen, wenn noetig
// 	if (zeigeSkizze == true)
// 	    {
// 		GenericRectangle mx = new Rectangle ();
// 		mx = g.getClipBounds ();
// 		g.setClip (0, 0, 644, 484);
// 		g.drawImage (skizze, 0, 0, null);
// 		g.setClip (mx);
// 	    }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Papierrollen unten
                if (papierRollen.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 150;
                    pTemp = pRollen;
                }

                // Ausreden fuer Papierrolle 1
                if (papierRolle1.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTemp = pRolle1;
                }

                // Ausreden fuer Papierrolle 2
                if (papierRolle2.IsPointInRect(pTemp) && !mainFrame.Actions[630]) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTemp = pRolle2;
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

                // zu Gang gehen ?
                if (ausgangGang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangGang.IsPointInRect(kt)) {
                        pTemp = pExitGang;
                    } else {
                        pTemp = new GenericPoint(pExitGang.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Rollen unten
                if (papierRollen.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pRollen;
                }

                // Rolle1 ansehen
                if (papierRolle1.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = pRolle1;
                }

                // Rolle2 ansehen
                if (papierRolle2.IsPointInRect(pTemp) && !mainFrame.Actions[630]) {
                    nextActionID = 3;
                    pTemp = pRolle2;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Papierrollen unten mitnehmen
                if (papierRollen.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pRollen);
                    mainFrame.repaint();
                    return;
                }

                // Papierrolle1 mitnehmen
                if (papierRolle1.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pRolle1);
                    mainFrame.repaint();
                    return;
                }

                // Papierrolle2 mitnehmen
                if (papierRolle2.IsPointInRect(pTemp) && !mainFrame.Actions[630]) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(pRolle2);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangGang.IsPointInRect(pTemp)) {
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    papierRollen.IsPointInRect(pTemp) ||
                    papierRolle1.IsPointInRect(pTemp) ||
                    papierRolle2.IsPointInRect(pTemp) && !mainFrame.Actions[630];

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
            if (papierRollen.IsPointInRect(pTemp) ||
                    papierRolle1.IsPointInRect(pTemp) ||
                    papierRolle2.IsPointInRect(pTemp) && !mainFrame.Actions[630]) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangGang.IsPointInRect(pTemp)) {
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
    @Override
    public void evalMouseExitEvent() {
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
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)
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
                // Rollen unten anschauen
                KrabatSagt("Kapala_1", fRolleUnten, 3, 0, 0);
                break;

            case 2:
                // Rolle1 oben anschauen
                KrabatSagt("Kapala_2", fRolle1, 3, 0, 0);
                break;

            case 3:
                // Rolle2 anschauen
                KrabatSagt("Kapala_3", fRolle2, 3, 0, 0);
                break;

            case 50:
                // Rollen unten take
                KrabatSagt("Kapala_4", fRolleUnten, 3, 0, 0);
                break;

            case 55:
                // Rolle1 oben take
                KrabatSagt("Kapala_5", fRolle1, 3, 0, 0);
                break;

            case 60:
                // Rolle2 take
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fRolle2);
                mainFrame.krabat.nAnimation = 121;
                Counter = 5;
                nextActionID = 65;
                break;

            case 65:
                // wenn genommen, dann Spruch und behalten
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(50);
                    mainFrame.Clipset = false;
                    mainFrame.Actions[630] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.ConstructLocation(108);
                mainFrame.invCursor = false;
                mainFrame.fPlayAnim = false;
                mainFrame.whatScreen = 8;
                nextActionID = 0;
                mainFrame.Clipset = false;
                mainFrame.repaint();
// 		zeigeSkizze = true;
// 		Counter = 20;
// 		nextActionID = 70;
                break;

// 	    case 70:
            // Bild weg und Spruch
// 		if ((--Counter) > 1) break;
// 		KrabatSagt ("Aha, to wupada ka#z skica woporneje #skl#e. Tak, ka#z na rysowance Kulowskeho fararja. To bud#de so Dinglinger wjeseli#c.",
// 			    "Aha, to wugl#edajo ako skica woporneje #skl#e. Tak, ako na kreslance Kulojskego fararja. To bu#do se Dinglinger wjaseli#y.",
// 			    "Heh, jetzt wei#t ich es wieder! Das ist die Skizze, die der Pfarrer in Wittichenau mir gezeigt hatte.",
// 			    fRolle2, 3, 2, 75);
// 		break;

// 	    case 75:
            // Ende nehmen
// 		zeigeSkizze = false;
// 		mainFrame.Clipset = false;
// 		mainFrame.fPlayAnim = false;
// 		evalMouseMoveEvent (mainFrame.Mousepoint);
// 		nextActionID = 0;
// 		mainFrame.repaint();
// 		break;

            case 100:
                // Gehe zu Gang
                NeuesBild(152, locationID);
                break;

            case 150:
                // Rollen unten - Ausreden
                DingAusrede(fRolleUnten);
                break;

            case 155:
                // Rolle1 - Ausreden
                DingAusrede(fRolle1);
                break;

            case 160:
                // Rolle2 - Ausreden
                DingAusrede(fRolle2);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}