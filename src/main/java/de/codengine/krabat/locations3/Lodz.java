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
import de.codengine.krabat.anims.MerchantIronOre;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Lodz extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Lodz.class);
    private GenericImage background;
    private GenericImage vor1;
    private GenericImage vor2;
    private final MerchantIronOre wikowarka;
    private final MultipleChoice Dialog;

    private int GiveCounter = 0;

    private boolean krabatVisible = true;
    private boolean zonaVisible = true;
    private boolean walkReady = true;

    private boolean Stollen = false;
    private boolean Metall = false;
    private boolean Kiss = false;

    // Konstanten - Rects
    private static final BorderRect ausgangHabor
            = new BorderRect(0, 70, 40, 240);

    // Konstante Points
    private static final GenericPoint pExitHabor = new GenericPoint(15, 156);
    private static final GenericPoint pZona = new GenericPoint(427, 247);
    private static final GenericPoint zonaPoint = new GenericPoint(427, 206);

    private static final GenericPoint innenPointKrabat = new GenericPoint(458, 150);
    private static final GenericPoint innenPointZona = new GenericPoint(478, 110);

    // Konstante ints
    private static final int fZona = 12;

    private boolean isListening = false;

    private boolean schnauzeWasser = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Lodz(Start caller, int oldLocation) {
        super(caller, 164);

        // Krabat von Oben hinstellen
        mainFrame.actions[851] = true;
        mainFrame.checkKrabat();

        mainFrame.freeze(true);

        mainFrame.krabat.maxx = 50;      // no zooming
        mainFrame.krabat.zoomf = 0f;
        mainFrame.krabat.defScale = 50;

        wikowarka = new MerchantIronOre(mainFrame);
        Dialog = new MultipleChoice(mainFrame);

        wikowarka.setPos(zonaPoint);
        wikowarka.SetFacing(6);

        if (mainFrame.actions[559]) {
            zonaVisible = false;  // nachdem Anim gelaufen, ist sie nicht mehr da
        }

        InitLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(10, 30, 180, 200, 155, 246));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(127, 460, 127, 460, 247, 257));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(242, 300, 242, 300, 258, 330));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(144, 164, 80, 100, 258, 370));

        mainFrame.pathFinder.ClearMatrix(4);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(1, 3);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(14, true);
                break;
            case 163:
                // von Habor aus
                mainFrame.krabat.setPos(new GenericPoint(39, 164));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/lodz/lodz.png");
        vor1 = getPicture("gfx-dd/lodz/woc1.png");
        vor2 = getPicture("gfx-dd/lodz/woc2.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        if (mainFrame.isMultipleChoiceActive && mainFrame.isClipSet) {
            Dialog.paintMultiple(g);
            return;
        }

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

        // Zona Hintergrund loeschen
        // Clipping - Rectangle feststellen und setzen
        BorderRect temp = wikowarka.getRect();
        g.setClip(temp.lo_point.x - 5, temp.lo_point.y - 5, temp.ru_point.x - temp.lo_point.x + 10,
                temp.ru_point.y - temp.lo_point.y + 10);

        // Zeichne Hintergrund neu
        g.drawImage(background, 0, 0);

        // Kuchar bewegen
        if (!walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = wikowarka.Move();
        }

        // Zona zeichnen
        // Clipping - Rectangle feststellen und setzen
        BorderRect temp2 = wikowarka.getRect();
        g.setClip(temp2.lo_point.x - 5, temp2.lo_point.y - 5, temp2.ru_point.x - temp2.lo_point.x + 10,
                temp2.ru_point.y - temp2.lo_point.y + 10);

        // Zeichne sie jetzt

        // Redet sie etwa gerade ??
        if (zonaVisible) {
            if (TalkPerson == 65 && mainFrame.talkCount > 0) {
                wikowarka.talkZona(g);
            }

            // nur rumstehen oder laufen
            else {
                if (Stollen || Metall || Kiss) {
                    BorderRect tp = wikowarka.getRect();
                    if (Stollen) {
                        wikowarka.giveWosusk(g, tp.lo_point);
                    }
                    if (Metall) {
                        wikowarka.giveMetal(g, tp.lo_point);
                    }
                    if (Kiss) {
                        wikowarka.Kiss(g, new GenericPoint(tp.lo_point.x, tp.lo_point.y + 21));
                    }
                } else {
                    wikowarka.drawZona(g, isListening);
                }
            }
            // Vordergrund zeichnen wg. reingehen
            g.drawImage(vor2, 419, 100);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.GeheWeg();

        // Sounds abspielen
        evalSound();

        // Krabat zeichnen

        // Animation??
        if (krabatVisible) {
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
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // da es hier nicht wehtut, wird der Vordergrund immer neu gezeichnet
        g.drawImage(vor1, 291, 204);
        g.drawImage(vor2, 419, 100);

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
                // if (brKirche.IsPointInRect (pTemp) == true)
                // {
                // 	// Standard - Sinnloszeug
                // 	nextActionID = 150;
                //	pTemp = Pkirche;
                // }

                // Ausreden fuer Frau oder Stollen geben
                if (wikowarka.getRect().IsPointInRect(pTemp) && zonaVisible) {
                    if (mainFrame.whatItem == 52) {
                        nextActionID = 160;
                    } else {
                        // Extra - Sinnloszeug
                        nextActionID = 155;
                    }
                    pTemp = pZona;
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

                // zu Habor gehen ?
                if (ausgangHabor.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangHabor.IsPointInRect(kt)) {
                        pTemp = pExitHabor;
                    } else {
                        // es wird nach unten verlassen
                        pTemp = new GenericPoint(kt.x, pExitHabor.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Kirche ansehen
                // if (brKirche.IsPointInRect (pTemp) == true)
                // {
                //   nextActionID = 1;
                //  pTemp = Pkirche;
                // }

                // Frau ansehen
                if (wikowarka.getRect().IsPointInRect(pTemp) && zonaVisible) {
                    nextActionID = 1;
                    pTemp = pZona;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit der Frau reden
                if (wikowarka.getRect().IsPointInRect(pTemp) && zonaVisible) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(pZona);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangHabor.IsPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    wikowarka.getRect().IsPointInRect(pTemp) && zonaVisible;

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
            if (ausgangHabor.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
                }
                return;
            }

            if (wikowarka.getRect().IsPointInRect(pTemp) && zonaVisible) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
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

    private GenericPoint evalZonaTalkPoint() {
        // Hier Position des Textes berechnen
        BorderRect temp = wikowarka.getRect();
        return new GenericPoint((temp.ru_point.x + temp.lo_point.x) / 2, temp.lo_point.y - 50);
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
                // Wikowarka anschauen
                KrabatSagt("Lodz_1", fZona, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Wikowarka benutzen)
                isListening = true;
                mainFrame.krabat.SetFacing(fZona);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 100:
                // Gehe zu Habor
                mainFrame.actions[851] = false;
                NeuesBild(163, locationID);
                break;

            case 155:
                // Zona - Ausreden
                WPersonAusrede(fZona);
                break;

            case 160:
                // grosse Animszene, weil Stollen gegeben - aber nur, wenn er es auch weiss !!!
                if (!mainFrame.actions[552]) {
                    PersonSagt("Lodz_2", fZona, 65, 0, 0, evalZonaTalkPoint());
                } else {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    mainFrame.isInventoryCursor = false;
                    mainFrame.krabat.setPos(pZona);
                    mainFrame.krabat.SetFacing(fZona);
                    GiveCounter = 0;
                    nextActionID = 165;
                }
                break;

            case 165:
                // Stollen geben und aus Inventar entfernen
                mainFrame.inventory.vInventory.removeElement(52);
                // Halben Stollen rein, ist nachher uebrig
                mainFrame.inventory.vInventory.addElement(51);
                Stollen = true;
                krabatVisible = false;
                nextActionID = 170;
                break;

            case 170:
                // kleine Weile geben
                if (GiveCounter++ < 5) {
                    break;
                }
                nextActionID = 175;
                break;

            case 175:
                // Geben wieder aufheben
                Stollen = false;
                krabatVisible = true;
                mainFrame.isClipSet = false;
                nextActionID = 180;
                break;

            case 180:
                // Zona sagt Spruch
                isListening = true;
                if (!mainFrame.actions[710]) // Krabat ist vor der Uebergabe nochmal weggegangen
                {
                    PersonSagt("Lodz_3", 0, 65, 2, 181, evalZonaTalkPoint());
                } else {
                    PersonSagt("Lodz_4", 0, 65, 2, 181, evalZonaTalkPoint());
                }
                break;

            case 181:
                // Laufe nach oben (beide)
                wikowarka.MoveTo(innenPointZona);
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(innenPointKrabat);
                walkReady = false;
                nextActionID = 182;
                break;

            case 182:
                // Warten, bis beide ausgelaufen sind (das ist, wenn DoAction erreicht und Frau - Variable = false)
                if (walkReady) {
                    nextActionID = 183;
                }
                break;

            case 183:
                // beide verschwinden
                krabatVisible = false;
                zonaVisible = false;
                GiveCounter = 50;
                nextActionID = 185;
                break;

            case 185:
                // ErzaehlerText
                if (--GiveCounter > 0) {
                    break;
                }
                PersonSagt("Lodz_5", 0, 54, 2, 200, new GenericPoint(320, 200));
                break;

            case 200:
                // beide tauchen wieder auf
                krabatVisible = true;
                zonaVisible = true;
                nextActionID = 201;
                break;

            case 201:
                // Laufe nach unten (beide)
                wikowarka.MoveTo(zonaPoint);
                mainFrame.pathWalker.SetzeNeuenWeg(pZona);
                walkReady = false;
                nextActionID = 202;
                break;

            case 202:
                // Warten, bis beide ausgelaufen sind (das ist, wenn DoAction erreicht und Frau - Variable = false)
                if (walkReady) {
                    nextActionID = 205;
                }
                break;

            case 205:
                // Zona sagt Spruch
                PersonSagt("Lodz_6", fZona, 65, 2, 210, evalZonaTalkPoint());
                break;

            case 210:
                // Eisen geben und dem Inventar hinzufuegen
                schnauzeWasser = true;
                mainFrame.inventory.vInventory.addElement(48);
                Metall = true;
                krabatVisible = false;
                GiveCounter = 0;
                nextActionID = 215;
                break;

            case 215:
                // kleine Weile geben
                if (GiveCounter++ < 5) {
                    break;
                }
                nextActionID = 220;
                break;

            case 220:
                // wieder auf Normal
                Metall = false;
                krabatVisible = true;
                mainFrame.isClipSet = false;
                nextActionID = 225;
                break;

            case 225:
                // Krabat sagt Spruch	
                KrabatSagt("Lodz_7", fZona, 1, 2, 230);
                break;

            case 230:
                // Zona sagt Spruch
                PersonSagt("Lodz_8", fZona, 65, 2, 235, evalZonaTalkPoint());
                break;

            case 235:
                // Krabat verschwinden lassen und Kuss - szene
                mainFrame.soundPlayer.PlayFile("sfx-dd/hubka.wav");
                Kiss = true;
                krabatVisible = false;
                GiveCounter = 0;
                nextActionID = 240;
                break;

            case 240:
                // wir lassen Ihnen ein bisschen Zeit
                if (GiveCounter++ < 5) {
                    break;
                }
                nextActionID = 245;
                break;

            case 245:
                // Na, wir wollen es ja nicht uebertreiben !!
                Kiss = false;
                krabatVisible = true;
                mainFrame.isClipSet = false;
                nextActionID = 250;
                break;

            case 250:
                // Krabat sagt Spruch	
                KrabatSagt("Lodz_9", fZona, 1, 2, 255);
                break;

            case 255:
                // Zona sagt Spruch
                schnauzeWasser = false;
                PersonSagt("Lodz_10", fZona, 65, 2, 300, evalZonaTalkPoint());
                break;

            case 300:
                // Laufe nach oben, gute Frau
                wikowarka.MoveTo(innenPointZona);
                walkReady = false;
                nextActionID = 305;
                break;

            case 305:
                // Warten, bis sie ausgelaufen ist
                isListening = false;
                if (walkReady) {
                    nextActionID = 310;
                }
                break;

            case 310:
                // Ende der Fahnenstange
                // Schiff als "ich fahre bald weg" kennzeichnen
                mainFrame.actions[559] = true;
                zonaVisible = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            // Dialog mit Kuchar

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Lodz_22", 1000, 550, new int[]{550}, 610);
                Dialog.ExtendMC("Lodz_23", 550, 1000, null, 611);

                // 2. Frage
                Dialog.ExtendMC("Lodz_24", 550, 553, new int[]{553}, 620);
                Dialog.ExtendMC("Lodz_25", 553, 552, new int[]{552}, 622);
                if (!mainFrame.actions[956]) {
                    Dialog.ExtendMC("Lodz_26", 552, 1000, null, 623);
                }

                // 3. Frage
                Dialog.ExtendMC("Lodz_27", 1000, 558, new int[]{558}, 630);
                Dialog.ExtendMC("Lodz_28", 558, 557, new int[]{557}, 631);
                Dialog.ExtendMC("Lodz_29", 557, 556, new int[]{556}, 632);
                Dialog.ExtendMC("Lodz_30", 556, 555, new int[]{555}, 633);
                Dialog.ExtendMC("Lodz_31", 555, 1000, null, 634);

                // 4. Frage
                Dialog.ExtendMC("Lodz_32", 1000, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Wikowarka
                PersonSagt("Lodz_11", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 611:
                // Reaktion Wikowarka
                PersonSagt("Lodz_12", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Wikowarka
                PersonSagt("Lodz_13", 0, 65, 2, 621, evalZonaTalkPoint());
                break;

            case 621:
                // Reaktion Wikowarka
                PersonSagt("Lodz_14", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 622:
                // Reaktion Wikowarka
                mainFrame.actions[710] = true;
                PersonSagt("Lodz_15", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 623:
                // Reaktion Wikowarka
                PersonSagt("Lodz_16", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion Wikowarka
                PersonSagt("Lodz_17", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 631:
                // Reaktion Wikowarka
                PersonSagt("Lodz_18", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 632:
                // Reaktion Wikowarka
                PersonSagt("Lodz_19", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 633:
                // Reaktion Wikowarka
                PersonSagt("Lodz_20", 0, 65, 2, 600, evalZonaTalkPoint());
                break;

            case 634:
                // Reaktion Wikowarka
                PersonSagt("Lodz_21", 0, 65, 2, 600, evalZonaTalkPoint());
                break;


            case 800:
                // MC beenden, wenn zuende gelabert...
                isListening = false;
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        // wenn anderes wichtiger ist, dann hier verhindern
        if (schnauzeWasser) {
            return;
        }

        int zf = (int) (Math.random() * 100);

        if (zf > 96) {
            int zwzf = (int) (Math.random() * 4.99);
            zwzf += 49;

            mainFrame.soundPlayer.PlayFile("sfx-dd/lodz" + (char) zwzf + ".wav");
        }

    }
}

/* Texte

   Ow, sy wosu#sk d#osta#l. Tak p#oj, kofej je hi#zo hotowy... a potom m#o#ze#s mi wo sebi pow#eda#c...

   Hod#dinu poszd#di#so

   Ja so #ci d#dakuju, Krabato - za wosu#sk, za wopyt, za #zortne pow#edki... tule ma#s k#on#kk p#loneho #zeleza, hdy#s j#on tak nuznje trjeba#s ?

   Ow, z tym sy mi woprawd#de jara pomha#la.

   Je n#etko #kas, zo #ci bo#zemje praju.

   M#o#ze#s to hi#s#ce raz wospjetowa#c ?

   N#e. To p#rejara boli - we wutrobje. N#etko d#di...

*/