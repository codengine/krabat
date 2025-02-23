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

public class Komedij extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Komedij.class);
    private GenericImage background;
    private GenericImage jacke;
    private GenericImage schwert;
    private GenericImage sluzDrasta;
    private GenericImage gorilla;
    private final GenericImage[] kerzen;

    private static final GenericPoint[] kerzenPunkte =
            {new GenericPoint(177, 37),
                    new GenericPoint(192, 41),
                    new GenericPoint(204, 44)};

    private final int[] kerzenBild;
    private final int[] Verhinderkerze;
    private static final int[] MAX_VERHINDERKERZE = {3, 2, 4};

    // Konstanten - Rects
    private static final Borderrect linkerAusgang
            = new Borderrect(0, 0, 163, 150);
    private static final Borderrect sekera
            = new Borderrect(144, 210, 185, 298);
    private static final Borderrect rectJacke // fuer vordergrund
            = new Borderrect(0, 370, 260, 480);
    private static final Borderrect rectGorilla
            = new Borderrect(424, 190, 490, 345);
    private static final Borderrect rectSluzDrasta
            = new Borderrect(502, 209, 570, 359);
    private static final Borderrect rectSchwert // fuer Vordergrund
            = new Borderrect(210, 370, 445, 480);
    private static final Borderrect postawa
            = new Borderrect(55, 151, 128, 479);
    private static final Borderrect mjec
            = new Borderrect(312, 227, 336, 479);
    private static final Borderrect tesaki
            = new Borderrect(491, 251, 578, 311);

    // Konstante Points
    private static final GenericPoint pExitLeft = new GenericPoint(135, 372);
    private static final GenericPoint pSekera = new GenericPoint(165, 380);
    private static final GenericPoint pGorilla = new GenericPoint(452, 395);
    private static final GenericPoint pSluzDrasta = new GenericPoint(520, 400);
    private static final GenericPoint pPostawa = new GenericPoint(161, 389);
    private static final GenericPoint pMjec = new GenericPoint(371, 444);
    private static final GenericPoint pTesaki = new GenericPoint(521, 400);

    // Konstante ints
    private static final int fSekera = 9;
    private static final int fPostawa = 6;
    private static final int fMjec = 6;
    private static final int fTesaki = 12;
    private static final int fSluzDrasta = 12;
    private static final int fGorilla = 12;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Komedij(Start caller, int oldLocation) {
        super(caller, 124);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomf = 1f;
        mainFrame.krabat.defScale = -100;

        kerzen = new GenericImage[5];

        kerzenBild = new int[]{0, 0, 0};
        Verhinderkerze = new int[]{MAX_VERHINDERKERZE[0], MAX_VERHINDERKERZE[1], MAX_VERHINDERKERZE[2]};

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(105, 390, 105, 390, 372, 475));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(391, 530, 391, 530, 392, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(531, 620, 531, 620, 415, 479));

        mainFrame.pathFinder.ClearMatrix(3);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(16, true);
                break;
            case 123: // von Hala
                mainFrame.krabat.setPos(new GenericPoint(160, 395));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/komedij/komedij.png");
        jacke = getPicture("gfx-dd/komedij/jacke.png");
        schwert = getPicture("gfx-dd/komedij/schwert.png");
        gorilla = getPicture("gfx-dd/komedij/gorilla.png");
        sluzDrasta = getPicture("gfx-dd/komedij/sluz.png");

        kerzen[0] = getPicture("gfx-dd/komedij/kerze1.png");
        kerzen[1] = getPicture("gfx-dd/komedij/kerze2.png");
        kerzen[2] = getPicture("gfx-dd/komedij/kerze3.png");
        kerzen[3] = getPicture("gfx-dd/komedij/kerze4.png");
        kerzen[4] = getPicture("gfx-dd/komedij/kerze5.png");

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
        g.drawImage(gorilla, 424, 190);

        // flackernde Kerzen zeichnen
        g.setClip(170, 30, 60, 60);
        for (int i = 0; i <= 2; i++) {
            if (--Verhinderkerze[i] < 1) {
                Verhinderkerze[i] = MAX_VERHINDERKERZE[i];
                kerzenBild[i] = (int) (Math.random() * 4.99);
            }

            g.drawImage(kerzen[kerzenBild[i]], kerzenPunkte[i].x, kerzenPunkte[i].y);
        }

        // Ist Dienstkleidung noch da
        if (!mainFrame.actions[512]) {
            g.setClip(502, 209, 70, 152);
            g.drawImage(sluzDrasta, 502, 209);
        }

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
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinter Jacke (nur Clipping - Region wird neugezeichnet)
        if (rectJacke.IsPointInRect(pKrTemp)) {
            g.drawImage(jacke, 63, 163);
        }
        // hinter Schwert (nur Clipping - Region wird neugezeichnet)
        if (rectSchwert.IsPointInRect(pKrTemp)) {
            g.drawImage(schwert, 249, 216);
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Sekera
                if (sekera.IsPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTemp = pSekera;
                }

                // Ausreden fuer postawa, wenn geht ???
                if (postawa.IsPointInRect(pTemp)) {
                    nextActionID = 155;
                    pTemp = pPostawa;
                }

                // Ausreden fuer mjec
                if (mjec.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = pMjec;
                }

                // Ausreden fuer tesaki, wenn sluz dr. weg
                if (tesaki.IsPointInRect(pTemp) && mainFrame.actions[512]) {
                    nextActionID = 165;
                    pTemp = pTesaki;
                }

                // Ausreden fuer sl. dr, wenn noch da
                if (rectSluzDrasta.IsPointInRect(pTemp) && !mainFrame.actions[512]) {
                    nextActionID = 170;
                    pTemp = pSluzDrasta;
                }

                // Ausreden fuer Gorilla
                if (rectGorilla.IsPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = pGorilla;
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

                // zu Halle gehen ?
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = pExitLeft;
                    } else {
                        pTemp = new GenericPoint(pExitLeft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Gorilla ansehen
                if (rectGorilla.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pGorilla;
                }

                // Beil ansehen
                if (sekera.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pSekera;
                }

                // Dienstkleidung ansehen (falls noch da)
                if (rectSluzDrasta.IsPointInRect(pTemp) && !mainFrame.actions[512]) {
                    nextActionID = 5;
                    pTemp = pSluzDrasta;
                }

                // Postawa ansehen, wenn mgl.
                if (postawa.IsPointInRect(pTemp)) {
                    nextActionID = 10;
                    pTemp = pPostawa;
                }

                // mjec ansehen
                if (mjec.IsPointInRect(pTemp)) {
                    nextActionID = 11;
                    pTemp = pMjec;
                }

                // tesaki ansehen
                if (tesaki.IsPointInRect(pTemp) && mainFrame.actions[512]) {
                    nextActionID = 12;
                    pTemp = pTesaki;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Gorilla mitnehmen
                if (rectGorilla.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    mainFrame.pathWalker.SetzeNeuenWeg(pGorilla);
                    mainFrame.repaint();
                    return;
                }

                // Beil mitnehmen
                if (sekera.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSekera);
                    mainFrame.repaint();
                    return;
                }

                // SluzDrasta mitnehmen (falls noch da)
                if (rectSluzDrasta.IsPointInRect(pTemp) && !mainFrame.actions[512]) {
                    nextActionID = 6;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSluzDrasta);
                    mainFrame.repaint();
                    return;
                }

                // postawa benutzen, wenn mgl.
                if (postawa.IsPointInRect(pTemp)) {
                    nextActionID = 20;
                    mainFrame.pathWalker.SetzeNeuenWeg(pPostawa);
                    mainFrame.repaint();
                    return;
                }

                // mjec benutzen
                if (mjec.IsPointInRect(pTemp)) {
                    nextActionID = 25;
                    mainFrame.pathWalker.SetzeNeuenWeg(pMjec);
                    mainFrame.repaint();
                    return;
                }

                // tesaki benutzen
                if (tesaki.IsPointInRect(pTemp) && mainFrame.actions[512]) {
                    nextActionID = 30;
                    mainFrame.pathWalker.SetzeNeuenWeg(pTesaki);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (linkerAusgang.IsPointInRect(pTemp)) {
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    sekera.IsPointInRect(pTemp) ||
                    rectGorilla.IsPointInRect(pTemp) ||
                    rectSluzDrasta.IsPointInRect(pTemp) && !mainFrame.actions[512] ||
                    postawa.IsPointInRect(pTemp) ||
                    mjec.IsPointInRect(pTemp) ||
                    tesaki.IsPointInRect(pTemp) && mainFrame.actions[512];

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
            if (sekera.IsPointInRect(pTemp) ||
                    rectGorilla.IsPointInRect(pTemp) ||
                    rectSluzDrasta.IsPointInRect(pTemp) && !mainFrame.actions[512] ||
                    postawa.IsPointInRect(pTemp) ||
                    mjec.IsPointInRect(pTemp) ||
                    tesaki.IsPointInRect(pTemp) && mainFrame.actions[512]) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
                }
                return;
            }

            // if (obererAusgang.IsPointInRect (pTemp) == true)
            // {
            //  if (Cursorform != 12)
            //  {
            //    mainFrame.setCursor (mainFrame.Cup);
            //    Cursorform = 12;
            //  }
            //  return;
            // }

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
                // Gorilla ansehen
                KrabatSagt("Komedij_1", fGorilla, 3, 0, 0);
                break;

            case 2:
                // Gorilla mitnehmen
                KrabatSagt("Komedij_2", fGorilla, 3, 0, 0);
                break;

            case 3:
                // Beil ansehen
                KrabatSagt("Komedij_3", fSekera, 3, 0, 0);
                break;

            case 4:
                // Beil mitnehmen
                KrabatSagt("Komedij_4", fSekera, 3, 0, 0);
                break;

            case 5:
                // Bedienstetenkleidung ansehen
                KrabatSagt("Komedij_5", fSluzDrasta, 3, 0, 0);
                break;

            case 6:
                // Bedienstetenkleidung mitnehmen (wenn noch da)
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // mainFrame.wave.PlayFile ("sfx-dd/wusch2.wav");
                mainFrame.krabat.SetFacing(fSluzDrasta);
                nextActionID = 7;
                mainFrame.krabat.nAnimation = 121;
                Counter = 5;
                break;

            case 7:
                // Ende take sluz. drasta
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(41);
                    mainFrame.actions[512] = true;        // Flag setzen
                    mainFrame.isClipSet = false;  // alles neu zeichnen
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 10:
                // Postawa ansehen
                // zuffzahl 0...1
                int zZahl = (int) (Math.random() * 1.9);
                switch (zZahl) {
                    case 0:
                        KrabatSagt("Komedij_6", fPostawa, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Komedij_7", fPostawa, 3, 0, 0);
                        break;
                }
                break;

            case 11:
                // mjec ansehen
                KrabatSagt("Komedij_8", fMjec, 3, 0, 0);
                break;

            case 12:
                // tesaki ansehen
                KrabatSagt("Komedij_9", fTesaki, 3, 0, 0);
                break;

            case 20:
                // postawa mitnehmen
                KrabatSagt("Komedij_10", fPostawa, 3, 0, 0);
                break;

            case 25:
                // mjec mitnehmen
                KrabatSagt("Komedij_11", fMjec, 3, 0, 0);
                break;

            case 30:
                // tesaki mitnehmen
                KrabatSagt("Komedij_12", fTesaki, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Hala-Doppelbild
                NeuesBild(123, locationID);
                break;

            case 150:
                // Ausreden sekera
                DingAusrede(fSekera);
                break;

            case 155:
                // Ausreden postawa
                DingAusrede(fPostawa);
                break;

            case 160:
                // Ausreden mjec
                DingAusrede(fMjec);
                break;

            case 165:
                // Ausreden tesaki
                DingAusrede(fTesaki);
                break;

            case 170:
                // Ausreden sluz Drasta
                DingAusrede(fSluzDrasta);
                break;

            case 175:
                // Ausreden gorilla
                DingAusrede(fGorilla);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}