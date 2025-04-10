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
import de.codengine.krabat.anims.Fire;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Labyr5 extends MainLabyrinth {
    private static final Logger log = LoggerFactory.getLogger(Labyr5.class);
    private GenericImage background;
    private GenericImage lab52;
    private GenericImage lab53;
    private GenericImage lab54;

    private Fire feuer;

    private boolean isBlinker = false;
    private int Ausgang = 0;
    private static final GenericPoint Aoben = new GenericPoint(415, 205);
    private static final GenericPoint Arechts = new GenericPoint(610, 451);
    private GenericPoint AusPoint = new GenericPoint(0, 0);

    // Konstanten - Rects
    private static final BorderRect rechterAusgang = new BorderRect(608, 362, 639, 458);
    private static final BorderRect obererAusgang = new BorderRect(241, 176, 347, 301);
    private static final BorderRect lab52Rect = new BorderRect(296, 276, 372, 334);
    private static final BorderRect lab53Rect = new BorderRect(195, 357, 380, 479);
    private static final BorderRect lab54Rect = new BorderRect(226, 170, 453, 446);

    // Exitlocation - Array
    private static final int[] Exitright = {51, 52, 53, 54, 56, 57, 58, 59, /*61*/};
    private static final int[] Exitup = {53, 54, 56, 59, 60, /*62*/};

    // Konstante Punkte
    private static final GenericPoint Pup = new GenericPoint(291, 225);
    private static final GenericPoint Pright = new GenericPoint(639, 413);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Labyr5(Start caller, int Richtung) {
        super(caller);
        log.debug("Laby 5");

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 426;
        mainFrame.krabat.zoomf = 4.77f;
        mainFrame.krabat.defScale = -30;

        feuer = new Fire(mainFrame);

        InitLocation(Richtung);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int Richtung) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(414, 381, 639, 417));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(282, 365, 413, 414));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(254, 272, 282, 316, 318, 364));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(266, 290, 254, 272, 290, 317));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(4);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);

        InitImages();
        switch (Richtung) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(10, true);
                Ausgang = BerechneAusgang(true, false, false, true);
                break;
            case 6:
                // von oben aus
                mainFrame.krabat.setPos(new GenericPoint(280, 334));
                mainFrame.krabat.SetFacing(6);
                Ausgang = BerechneAusgang(false, false, false, true);
                break;
            case 9:
                // von rechts aus
                mainFrame.krabat.setPos(new GenericPoint(590, 403));
                mainFrame.krabat.SetFacing(9);
                Ausgang = BerechneAusgang(true, false, false, false);
                break;
        }
        // Hier richtigen Punkt fuers Geblinker eintragen
        if (Ausgang == 3) {
            AusPoint = Arechts;
        }
        if (Ausgang == 12) {
            AusPoint = Aoben;
        }

        if (mainFrame.actions[236]) {
            isBlinker = true;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/labyrinth/laby5.png");
        lab52 = getPicture("gfx/labyrinth/lab5-2.png");
        lab53 = getPicture("gfx/labyrinth/lab5-3.png");
        lab54 = getPicture("gfx/labyrinth/lab5-4.png");

    }

    @Override
    public void cleanup() {
        background = null;
        lab52 = null;
        lab53 = null;
        lab54 = null;

        feuer.cleanup();
        feuer = null;
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

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Blinkern zeichnen
        if (isBlinker) {
            g.setClip(AusPoint.x, AusPoint.y, Fire.Breite, Fire.Hoehe);
            g.drawImage(background, 0, 0);
            feuer.drawPlomja(g, AusPoint);
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
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab52Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab52, 309, 295);
        }

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab53Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab53, 243, 383);
        }

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab54Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab54, 204, 145);
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

                // zu naechstem Laby gehen oben
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
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

                // zu naechstem Laby gehen rechts
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Naechstes Laby anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Naechstes Laby anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp);

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
      /*if ((brEingang.IsPointInRect (pTemp) == true) ||
          (brWagen.IsPointInRect (pTemp) == true))
      {
        if (Cursorform != 1)
        {
          mainFrame.setCursor (mainFrame.Kreuz);
          Cursorform = 1;
        }
        return;
      }*/

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
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
    }

    private void evalNewLocationUp() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : Exitup) {
                if (zuffi == j) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    private void evalNewLocationRight() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : Exitright) {
                if (zuffi == j) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
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
            case 100:
                // Gehe zu naechstem Laby oben
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                Erscheinen(Ausgang == 12);
                if (mainFrame.actions[235]) {
                    BludLocationUp();
                } else {
                    evalNewLocationUp();
                }
                mainFrame.constructLocation(newloc, 12);
                mainFrame.destructLocation(55);
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu naechstem Laby rechts
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                Erscheinen(Ausgang == 3);
                if (mainFrame.actions[235]) {
                    BludLocationRight();
                } else {
                    evalNewLocationRight();
                }
                mainFrame.constructLocation(newloc, 3);
                mainFrame.destructLocation(55);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}