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

package de.codengine.krabat.locations2;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Boom;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Ralbicy2 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Ralbicy2.class);
    private GenericImage background;
    private GenericImage holz;
    private GenericImage kreuz;
    // private bur1 bauer;
    private Miller mueller;
    private final MultipleChoice Dialog;
    private boolean setAnim = false;
    private boolean muellerda = false;
    // private boolean isListening = false;

    private Boom muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // private boolean schnauzeBauer = false;

    private static final boolean bauerda = false;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang = new BorderRect(450, 423, 639, 479);
    private static final BorderRect linkerAusgang = new BorderRect(0, 290, 42, 361);
    private static final BorderRect brKirche = new BorderRect(172, 136, 544, 277);
    private static final BorderRect brBauer = new BorderRect(62, 217, 120, 307);

    // Punkte in Location
    private static final GenericPoint Pkirche = new GenericPoint(77, 316);
    // private static final GenericPoint Pbauer    = new GenericPoint ( 44, 307);
    private static final GenericPoint BurTalk = new GenericPoint(90, 140);
    private static final GenericPoint Pright = new GenericPoint(581, 479);
    private static final GenericPoint Pleft = new GenericPoint(0, 327);
    private static final GenericPoint mlynkFeet = new GenericPoint(468, 457);

    // Konstante ints
    private static final int fKirche = 3;
    private static final int fBauer = 3;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Ralbicy2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 424;
        mainFrame.krabat.zoomf = 1.63f;
        mainFrame.krabat.defScale = -40;

        // bauer  = new bur1 (mainFrame);
        Dialog = new MultipleChoice(mainFrame);
        mueller = new Miller(mainFrame);

        muellermorph = new Boom(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = -30;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(3);

        InitLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 16, 0, 221, 296, 362));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 223, 97, 223, 363, 420));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(224, 372, 300, 435));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(301, 390, 408, 454));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(409, 390, 639, 479));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(20, true);
                break;

            case 74:
                // von Jitk aus, Mueller kommt
                BackgroundMusicPlayer.getInstance().stop();
                mainFrame.krabat.setPos(new GenericPoint(581, 463));
                mainFrame.krabat.SetFacing(9);
                setAnim = true;
                // schnauzeBauer = true;
                TalkPause = 10;
                break;

            case 89:
                // von Most aus
                mainFrame.krabat.setPos(new GenericPoint(20, 332));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/ralbicy/ralbicy4.png");
        holz = getPicture("gfx/ralbicy/holz2.png");
        kreuz = getPicture("gfx/ralbicy/kreuz.png");

    }

    @Override
    public void cleanup() {
        background = null;
        holz = null;
        kreuz = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
	/*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
	    {
		Dialog.paintMultiple (g);
		return;
		}  */

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);
        g.drawImage(holz, 0, 365);
        g.drawImage(kreuz, 118, 194);


        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0);
            g.drawImage(holz, 0, 365);
            g.drawImage(kreuz, 118, 194);
        }

        // Bauern zeichnen, faellt wohl raus
//	if (bauerda == true) 
//	    {
//		g.setClip ( 62, 217, 58, 90);
//		g.drawImage (background, 0, 0, null);
//		bauer.drawBur (g, TalkPerson, isListening, schnauzeBauer);
//	    }

        // Mueller zeichnen
        if (muellerda) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
            g.drawImage(holz, 0, 365);
            g.drawImage(kreuz, 118, 194);

            // Redet er etwa gerade ??
            if (TalkPerson == 36 && mainFrame.talkCount > 0) {
                mueller.talkMlynk(g);
            }

            // nur rumstehen oder laufen
            else {
                mueller.drawMlynk(g);
            }
        }

        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
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

        // muss noch mit Abfrage versehen werden (sonst unnuetz!)
        g.drawImage(holz, 0, 365);
        g.drawImage(kreuz, 118, 194);

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

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 1000;
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

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Kirche
                if (brKirche.IsPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
                    pTemp = Pkirche;
                }

                // Ausreden fuer Bauer
//			if ((brBauer.IsPointInRect (pTemp) == true) && (bauerda == true))
//			    {
//				// Extra - Sinnloszeug
//				nextActionID = 155;
//				pTemp = Pbauer;
//			    }				        

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

                // zu Jitk gehen ?
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, Pright.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Most gehen?
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Kirche ansehen
                if (brKirche.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pkirche;
                }

                // Bauer ansehen
//			if ((brBauer.IsPointInRect (pTemp) == true) && (bauerda == true))
//			    {
//				nextActionID = 2;
//				pTemp = Pbauer;
//			    }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Jitk anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Ausgang zu Most abfangen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Kirche mitnehmen
                if (brKirche.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pkirche);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Bauern reden
//			if ((brBauer.IsPointInRect (pTemp) == true) && (bauerda == true))
//			    {
//				nextActionID = 51;
//				mainFrame.wegGeher.SetzeNeuenWeg (Pbauer);
//				mainFrame.repaint();
//				return;
//			    }

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
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = brKirche.IsPointInRect(pTemp) ||
                    tmp.IsPointInRect(pTemp) ||
                    brBauer.IsPointInRect(pTemp) && bauerda;

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
            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 3;
                }
                return;
            }

            if (brKirche.IsPointInRect(pTemp) ||
                    brBauer.IsPointInRect(pTemp) && bauerda) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 2;
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
                // Kirche anschauen
                KrabatSagt("Ralbicy2_1", fKirche, 3, 0, 0);
                break;

            case 2:
                // Bauer anschauen
                KrabatSagt("Ralbicy2_2", fBauer, 3, 0, 0);
                break;

            case 50:
                // Kirche mitnehmen
                KrabatSagt("Ralbicy2_3", fKirche, 3, 0, 0);
                break;

            case 51:
                // Krabat beginnt MC (Bauer benutzen)
                mainFrame.krabat.SetFacing(3);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // isListening = true;  // Bauer hoert auf zu arbeiten
                nextActionID = 600;
                break;

            case 100:
                // Gehe zu Jitk
                NeuesBild(74, 82);
                break;

            case 101:
                // nach Most gehen
                NeuesBild(89, 82);
                break;

            case 150:
                // Kirche - Ausreden
                DingAusrede(fKirche);
                break;

            case 155:
                // Bauer - Ausreden
                MPersonAusrede(fBauer);
                break;

            case 200:
                // Stock oder Angel auf Bauern
                KrabatSagt("Ralbicy2_4", fBauer, 3, 0, 0);
                break;

            case 220:
                // Krosik auf Bauern
                KrabatSagt("Ralbicy2_5", fBauer, 3, 0, 0);
                break;

            case 230:
                // Bron auf Bauern
                KrabatSagt("Ralbicy2_6", fBauer, 3, 0, 0);
                break;

            // Dialog mit Bauer

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Ralbicy2_18", 1000, 22, new int[]{22}, 640);
                Dialog.ExtendMC("Ralbicy2_19", 22, 21, new int[]{21}, 630);
                Dialog.ExtendMC("Ralbicy2_20", 21, 20, new int[]{20}, 620);
                Dialog.ExtendMC("Ralbicy2_21", 20, 1000, null, 610);

                // 2. Frage
                Dialog.ExtendMC("Ralbicy2_22", 1000, 23, new int[]{23}, 660);
                Dialog.ExtendMC("Ralbicy2_23", 23, 24, new int[]{24}, 650);

                // 4. Frage
                Dialog.ExtendMC("Ralbicy2_24", 25, 1000, null, 800);
                Dialog.ExtendMC("Ralbicy2_25", 1000, 25, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.actions[25] = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;
                nextActionID = Dialog.ActionID;
                break;

            case 610:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_7", 0, 21, 2, 611, BurTalk);
                break;

            case 611:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_8", 0, 21, 2, 612, BurTalk);
                break;

            case 612:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_9", 0, 21, 2, 600, BurTalk);
                break;

            case 620:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_10", 0, 21, 2, 621, BurTalk);
                break;

            case 621:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_11", 0, 21, 2, 622, BurTalk);
                break;

            case 622:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_12", 0, 21, 2, 600, BurTalk);
                break;

            case 630:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_13", 0, 21, 2, 631, BurTalk);
                break;

            case 631:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_14", 0, 21, 2, 600, BurTalk);
                break;

            case 640:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_15", 0, 21, 2, 600, BurTalk);
                break;

            case 650:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_16", 0, 21, 2, 600, BurTalk);
                break;

            case 660:
                // Reaktion Bauer
                PersonSagt("Ralbicy2_17", 0, 21, 2, 600, BurTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[25] = false; // wieder nico dale beim 1. MC
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                // isListening = false; // Bauer hoert wieder auf zuzuhoeren
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 1000:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 120);  // 68 - 100 - scaleMueller
                ismuellermorphing = true;
                nextActionID = 1003;
                break;

            case 1003:
                // Mueller erscheint
                if (muellermorphcount < 3) {
                    break;
                }
                muellerda = true;
                nextActionID = 1008;
                break;

            case 1008:
                // Mueller sagt seinen Spruch
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                mainFrame.isClipSet = false;
                MuellerMecker(mueller.evalMlynkTalkPoint());
                TalkPerson = 36;
                TalkPause = 5;
                nextActionID = 1010;
                break;

            case 1010:
                // Gehe zu Muehle zurueck
                NeuesBild(90, 89);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}