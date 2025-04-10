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

package de.codengine.krabat.locations;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Deer;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Sunow1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Sunow1.class);
    private GenericImage background;
    private GenericImage wegstueck;
    private int oldActionID = 0;
    private boolean Berglauf = false;
    private GenericPoint Endpunkt;
    private GenericPoint Wendepunkt;
    private final GenericPoint Merkpunkt;
    private boolean isTal;

    private Deer deer;

    // Konstanten - Rects
    private static final BorderRect obererAusgang = new BorderRect(343, 137, 455, 186);
    private static final BorderRect untererAusgang = new BorderRect(18, 408, 252, 479);
    private static final BorderRect sunowRect = new BorderRect(260, 138, 589, 184);

    private static final BorderTrapezoid BergTrapez = new BorderTrapezoid(295, 322, 154, 229, 278, 394);
    private static final BorderTrapezoid TalTrapez = new BorderTrapezoid(391, 397, 340, 356, 202, 276);

    // Konstanten - Points
    private static final GenericPoint Pdown = new GenericPoint(105, 479);
    private static final GenericPoint Pup = new GenericPoint(393, 201);

    // Zooming - Variablen
    private static final int TAL_MAXX = 276;
    private static final int TAL_MINX = 276;
    private static final float TAL_ZOOMF = 2.92f;
    private static final int TAL_DEFSCALE = 55;

    private static final int BERG_MAXX = 479;
    private static final int BERG_MINX = 77;
    private static final float BERG_ZOOMF = 13.4f;
    private static final int BERG_DEFSCALE = -30;

    // Konstante ints
    private static final int fSunow = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Sunow1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        Merkpunkt = new GenericPoint(0, 0);
        InitLocation(oldLocation);

        deer = new Deer(mainFrame, false, new GenericRectangle(262, 204, 53, 25), 103);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        switch (oldLocation) {
            case 0: // Einsprung fuer Load
                // Berechnung, ob K im Tal steht oder nicht
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                GenericPoint tp = mainFrame.krabat.getPos();
                BorderRect TalRect = new BorderRect(330, 200, 400, 285);
                isTal = TalRect.IsPointInRect(tp);
                break;
            case 2: // aus Most kommend
                mainFrame.krabat.setPos(new GenericPoint(124, 467));
                mainFrame.krabat.SetFacing(12);
                isTal = false;
                break;
            case 13: // aus Wjes kommend
                mainFrame.krabat.setPos(new GenericPoint(393, 201));
                mainFrame.krabat.SetFacing(6);
                isTal = true;
                break;
        }

        // Matrix je nach Standort initialisieren
        InitMatrix();

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/sunow/sunow.png");
        wegstueck = getPicture("gfx/sunow/sunow-2.png");

    }

    private void InitMatrix() {
        mainFrame.pathWalker.vBorders.removeAllElements();

        if (isTal) {
            // Grenzen setzen im Tal
            // Taltrapez
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(391, 397, 340, 356, 202, 276));

            // Laufmatrix anpassen
            // Matrix loeschen
            mainFrame.pathFinder.ClearMatrix(1);

            // Zooming anpassen
            mainFrame.krabat.maxx = TAL_MAXX;
            mainFrame.krabat.minx = TAL_MINX;
            mainFrame.krabat.zoomf = TAL_ZOOMF;
            mainFrame.krabat.defScale = TAL_DEFSCALE;
        } else {
            // Grenzen setzen auf dem Berg
            // Bergtrapez
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(295, 322, 154, 229, 278, 394));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(154, 229, 54, 162, 395, 479));

            // Laufmatrix anpassen
            // Matrix loeschen
            mainFrame.pathFinder.ClearMatrix(2);

            // moegliche Wege eintragen
            mainFrame.pathFinder.PosVerbinden(0, 1);

            // Zooming anpassen
            mainFrame.krabat.maxx = BERG_MAXX;
            mainFrame.krabat.minx = BERG_MINX;
            mainFrame.krabat.zoomf = BERG_ZOOMF;
            mainFrame.krabat.defScale = BERG_DEFSCALE;
        }
    }

    @Override
    public void cleanup() {
        background = null;
        wegstueck = null;

        deer.cleanup();
        deer = null;
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

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Rehe Hintergrund loeschen
        g.setClip(260, 150, 100, 100);
        g.drawImage(background, 0, 0);

        // Rehe zeichnen
        deer.drawReh(g);

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
        if (Berglauf) {
            g.drawImage(wegstueck, 253, 268);
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
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

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // ausreden fuer Schoenau
                if (sunowRect.IsPointInRect(pTemp) && !obererAusgang.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 200 : 150;
                    mainFrame.repaint();
                    return;
                }

                boolean tg = TesteLauf(pTemp, nextActionID);

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (!tg) {
                    mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                }
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

                // zu Most gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Wjes gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                if (sunowRect.IsPointInRect(pTemp) && !obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    mainFrame.repaint();
                    return;
                }

                boolean gh = TesteLauf(pTemp, nextActionID);

                if (!gh) {
                    mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                }
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                nextActionID = 0;

                // Most Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Wjes anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                if (sunowRect.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.repaint();
                    return;
                }

                // boolean fuck = TesteLauf (pTemp, nextActionID);

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
        // Wenn Animation, dann transparenter Cursor
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
            mainFrame.isInventoryHighlightCursor = sunowRect.IsPointInRect(pTemp) || tmp.IsPointInRect(pTemp);

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
            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
                }
                return;
            }

            if (sunowRect.IsPointInRect(pTemp) && !obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 5;
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

    // Erkennungsroutine, ob Animationsmodus eingeschaltet werden muss
    private boolean TesteLauf(GenericPoint pTxxx, int Action) {
        GenericPoint kpos = mainFrame.krabat.getPos();

        // Hier Punkt klonen, damit alter Punkt erhalten bleibt
        GenericPoint pTemp = new GenericPoint(pTxxx.x, pTxxx.y);

        // vom Tal auf den Berg???
        if (isTal && pTemp.y > 277) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 600;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor dem Verschwinden berechnen
            GenericPoint rand = TalTrapez.Punkte(kpos.y);
            log.debug("randx: {} kposx: {} randy: {}", rand.x, kpos.x, rand.y);
            pTemp.y = TalTrapez.y2;
            float t1 = kpos.x - rand.x;
            float t2 = rand.y - rand.x;
            float teil = t1 / t2;
            pTemp.x = TalTrapez.x3 + (int) ((TalTrapez.x4 - TalTrapez.x3) * teil);
            log.debug("teil: {}", teil);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (BergTrapez.x1 + (BergTrapez.x2 - BergTrapez.x1) * teil), BergTrapez.y1);
            Wendepunkt = new GenericPoint((pTemp.x + Endpunkt.x) / 2, 380);

            mainFrame.pathWalker.SetzeWegOhneStand(pTemp);
            mainFrame.repaint();
            log.debug("Wendepunkt.x: {} Wendepunkt.y: {} Endpunkt.x: {} Endpunkt.y: {}", Wendepunkt.x, Wendepunkt.y, Endpunkt.x, Endpunkt.y);
            return true;
        }

        // vom Berg ins Tal ??
        if (!isTal && pTemp.y < 278) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 610;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor Verschwinden berechnen
            GenericPoint raud = BergTrapez.Punkte(kpos.y);
            log.debug("raud.x: {} kpos.x: {} raud.y: {}", raud.x, kpos.x, raud.y);
            pTemp.y = BergTrapez.y1;
            float t3 = kpos.x - raud.x;
            float t4 = raud.y - raud.x;
            float teal = t3 / t4;
            log.debug("teal: {}", teal);

            if (BergTrapez.PointInside(mainFrame.krabat.getPos())) {
                pTemp.x = BergTrapez.x1 + (int) ((BergTrapez.x2 - BergTrapez.x1) * teal);
            } else {
                // Default - Werte fuer Tallauf, wenn noch zu weit weg
                pTemp = new GenericPoint((BergTrapez.x1 + BergTrapez.x2) / 2, BergTrapez.y1);
                teal = 0.5f;
            }

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (TalTrapez.x3 + (TalTrapez.x4 - TalTrapez.x3) * teal), TalTrapez.y2);
            Wendepunkt = new GenericPoint((pTemp.x + Endpunkt.x) / 2, 380);

            mainFrame.pathWalker.SetzeWegOhneStand(pTemp);
            mainFrame.repaint();
            log.debug("Wendepunkt.x: {} Wendepunkt.y: {} Endpunkt.x: {} Endpunkt.y: {}", Wendepunkt.x, Wendepunkt.y, Endpunkt.x, Endpunkt.y);
            return true;
        }
        return false;
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

        // System.out.println("NextAction : " + nextActionID);

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
                // Sunow anschauen
                KrabatSagt("Sunow1_1", fSunow, 3, 0, 0);
                break;

            case 50:
                // Sunow mitnehmen
                KrabatSagt("Sunow1_2", fSunow, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Most
                NeuesBild(2, 7);
                break;

            case 101:
                // nach Wjes gehen
                NeuesBild(13, 7);
                break;

            case 150:
                // Dorf - Ausreden
                DingAusrede(fSunow);
                break;

            case 200:
                // kamuski auf dorf
                KrabatSagt("Sunow1_3", fSunow, 3, 0, 0);
                break;

            case 600:
                // vom Tal auf den Berg laufen
                Berglauf = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Wendepunkt);
                nextActionID = 601;
                break;

            case 601:
                // beim Lauf Tal auf Berg wieder zum Vorschein kommen
                mainFrame.krabat.maxx = BERG_MAXX;
                mainFrame.krabat.zoomf = BERG_ZOOMF;
                mainFrame.krabat.defScale = BERG_DEFSCALE;
                mainFrame.krabat.minx = BERG_MINX;
                mainFrame.pathWalker.SetzeGarantiertWegFalsch(Endpunkt);
                nextActionID = 620;
                break;

            case 610:
                // vom Berg ins Tal laufen invertiert
                Berglauf = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.SetzeGarantiertWegFalsch(Wendepunkt);
                nextActionID = 611;
                break;

            case 611:
                // beim Lauf Berg ins Tal wieder zum Vorschein kommen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Endpunkt);
                mainFrame.krabat.maxx = TAL_MAXX;
                mainFrame.krabat.defScale = TAL_DEFSCALE;
                mainFrame.krabat.zoomf = TAL_ZOOMF;
                mainFrame.krabat.minx = TAL_MINX;
                nextActionID = 620;
                break;

            case 620:
                // Laufen beenden und alles wieder auf Normal zuruecksetzen
                mainFrame.isAnimRunning = false;
                Berglauf = false;
                Cursorform = 200;
                evalMouseMoveEvent(mainFrame.mousePoint);
                isTal = !isTal;
                InitMatrix();
                nextActionID = oldActionID;
                mainFrame.pathWalker.SetzeNeuenWeg(Merkpunkt);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}