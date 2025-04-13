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

package de.codengine.krabat.locations4;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Hrodz extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Hrodz.class);
    private GenericImage background;
    private GenericImage steinpic;
    private GenericImage gruftzu;
    private GenericImage vordergruft;
    private GenericImage krabat_left;
    private GenericImage krabat_right;
    private GenericImage krabat_wippen_left;
    private GenericImage krabat_wippen_right;
    private GenericImage krabat_fallen;

    private int Wippen = 0;

    private int Verhinderwippen;

    private static final int MAX_VERHINDERWIPPEN = 2;

    private int Fallgeschwindigkeit = 1;

    private static final int MAX_FALLGESCHWINDIGKEIT = 15;

    // Punkte in Location
    private static final GenericPoint Pright = new GenericPoint(639, 445);
    private static final GenericPoint Pschaukeln = new GenericPoint(491, 435);
    private static final GenericPoint Pstein = new GenericPoint(224, 460);

    // Konstanten - Rects deklarieren
    private static final BorderRect rechterAusgang =
            new BorderRect(593, 373, 639, 479);
    private static final BorderRect megaLinks =
            new BorderRect(0, 0, 525, 479);
    private static final BorderRect megaRechts =
            new BorderRect(469, 0, 639, 479);
    private static final BorderRect stein =
            new BorderRect(158, 425, 201, 479);

    // Konstante ints
    private static final int fStein = 9;

    // Flag, ob man rechts oder links vom "Loch" ist
    private boolean isRight;
    private boolean krabatFaelltRunter = false; // dto.

    private int SonderAnim = 0;

    private int Counter = 0;

    private boolean schnauzeHintergrund = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Hrodz(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 4.5f;
        mainFrame.krabat.defaultScale = -50;

        initLocation(oldLocation);

        Verhinderwippen = MAX_VERHINDERWIPPEN;

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(
                new BorderTrapezoid(224, 450, 328, 479));
        mainFrame.pathWalker.vBorders.addElement(
                new BorderTrapezoid(329, 437, 417, 479));
        mainFrame.pathWalker.vBorders.addElement(
                new BorderTrapezoid(418, 413, 639, 479));

        mainFrame.pathFinder.clearMatrix(3);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // Berechnen, wo K steht, wg. rumschaukeln...
                isRight = mainFrame.krabat.getPos().x > 500;
                break;
            case 200:
                // von Wotrow aus
                isRight = true;
                mainFrame.krabat.setPos(new GenericPoint(612, 445));
                mainFrame.krabat.setFacing(9);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/wotrow/hrodzi.png");
        steinpic = getPicture("gfx/wotrow/hkamjen.png");
        gruftzu = getPicture("gfx/wotrow/gruftzu.png");
        vordergruft = getPicture("gfx/wotrow/hgruft.png");

        krabat_wippen_left = getPicture("gfx/wotrow/k-l-wippen.png");
        krabat_wippen_right = getPicture("gfx/wotrow/k-r-wippen.png");

        krabat_fallen = getPicture("gfx/wotrow/k-r-fallen.png");
        krabat_left = getPicture("gfx/anims/k-l-10.png");
        krabat_right = getPicture("gfx/anims/k-r-10.png");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            cursorShape = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // offene oder geschlossene Gruft zeichnen
        // Gruft geschlossen, muss so sein, man kann bei offener nicht mehr Speichern
        g.setClip(462, 416, 100, 49);
        g.drawImage(gruftzu, 462, 416);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // hier ist der Sound...
        evalSound();

        // Krabat einen Schritt gehen lassen
        mainFrame.pathWalker.doWalk();

        if (SonderAnim != 0) {
            // hier erstmal alles berechnen, dann je nachdem die Bilder switchen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.getPos().x,
                    mainFrame.krabat.getPos().y);

            // Groesse
            int scale = mainFrame.krabat.defaultScale;
            scale += (int) (((float) mainFrame.krabat.maxX -
                    (float) hier.y) / mainFrame.krabat.zoomFactor);

            // hier Test auf "nicht zu gross"
            if (scale < mainFrame.krabat.defaultScale) {
                scale = mainFrame.krabat.defaultScale;
            }

            // System.out.println ("Scale ist " + scale + " gross.");

            // Hoehe: nur offset
            int hoch = 100 - scale;

            // Breite abhaengig von Hoehe...
            int weit = 50 - scale / 2;

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            switch (SonderAnim) {
                case 1:
                    // Lauf ohne Stein nach links
                    if (--Verhinderwippen < 1) {
                        Verhinderwippen = MAX_VERHINDERWIPPEN;
                        Wippen++;
                        if (Wippen == 4) {
                            SonderAnim = 0;
                            Wippen = 0;
                        }
                    }
                    g.drawImage(Wippen % 2 != 0 ?
                                    krabat_wippen_left : krabat_left, hier.x,
                            hier.y, weit, hoch);
                    break;

                case 2:
                    // Lauf ohne Stein nach rechts
                    if (--Verhinderwippen < 1) {
                        Verhinderwippen = MAX_VERHINDERWIPPEN;
                        Wippen++;
                        if (Wippen == 4) {
                            SonderAnim = 0;
                            Wippen = 0;
                        }
                    }
                    g.drawImage(Wippen % 2 != 0 ?
                                    krabat_wippen_right : krabat_right,
                            hier.x, hier.y, weit, hoch);
                    break;

                case 3:
                    // Lauf mit Stein nach rechts -> Falle rein
                    g.drawImage(krabat_fallen, hier.x, hier.y, weit,
                            hoch);
                    Fallgeschwindigkeit++;
                    if (Fallgeschwindigkeit == MAX_FALLGESCHWINDIGKEIT) {
                        mainFrame.talkCount = 0; // schreien aufhoeren
                        SonderAnim = 0;
                    }
                    GenericPoint tmp = mainFrame.krabat.getPos();
                    tmp.y += Fallgeschwindigkeit * 2;
                    mainFrame.krabat.setPos(tmp);
                    break;
            }
        } else {
            // Animation??
            if (mainFrame.krabat.nAnimation != 0) {
                mainFrame.krabat.doAnimation(g);

                // Cursorruecksetzung nach Animationsende
                if (mainFrame.krabat.nAnimation == 0) {
                    evalMouseMoveEvent(mainFrame.mousePoint);
                }
            } else {
                if (mainFrame.talkCount > 0 && talkPerson != 0) {
                    // beim Reden
                    switch (talkPerson) {
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

        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // Vordergrund vor Gruft zeichnen, wenn K reinfaellt
        if (krabatFaelltRunter) {
            g.drawImage(vordergruft, 461, 432);
        }

        // Stein zeichnen, solange noch da
        if (!mainFrame.actions[980]) {
            GenericRectangle my;
            my = g.getClipBounds();

            g.setClip(152, 415, 52, 66);
            g.drawImage(steinpic, 153, 416);

            g.setClip(my.getX(), my.getY(),
                    my.getWidth(), my.getHeight());
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x,
                    outputTextPos.y, COLORS[talkPerson]);
            g.setClip(my.getX(), my.getY(),
                    my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                talkPerson = 0;
            }
        }

        if (talkPause > 0 && mainFrame.talkCount < 1) {
            talkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 &&
                mainFrame.talkCount < 1) {
            doAction();
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

                BorderRect tmp = mainFrame.krabat.getBoundingBox();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Standardausreden fuer Stein
                if (stein.isPointInRect(pTemp) &&
                        !mainFrame.actions[980]) {
                    nextActionID = 150;
                    pTemp = Pstein;
                }

                // Testen, ob die Schunkelanim erfolgen muss oder nicht
                if (isRight &&
                        megaLinks.isPointInRect(pTemp)) {
                    // will ohne Stein von rechts nach links
                    pTemp = Pschaukeln;
                    nextActionID = 600;
                }
                if (!isRight &&
                        megaRechts.isPointInRect(pTemp)) {
                    if (mainFrame.actions[980]) {
                        // will mit Stein zurueck
                        nextActionID = 800;
                    } else {
                        // will ohne Stein zurueck
                        nextActionID = 700;
                    }
                    pTemp = Pschaukeln;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Wotrow gehen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                }

                // Stein ansehen
                if (stein.isPointInRect(pTemp) &&
                        !mainFrame.actions[980]) {
                    nextActionID = 1;
                    pTemp = Pstein;
                }

                // Testen, ob die Schunkelanim erfolgen muss oder nicht
                if (isRight &&
                        megaLinks.isPointInRect(pTemp)) {
                    // will ohne Stein von rechts nach links
                    pTemp = Pschaukeln;
                    nextActionID = 600;
                }
                if (!isRight &&
                        megaRechts.isPointInRect(pTemp)) {
                    if (mainFrame.actions[980]) {
                        // will mit Stein zurueck
                        nextActionID = 800;
                    } else {
                        // will ohne Stein zurueck
                        nextActionID = 700;
                    }
                    pTemp = Pschaukeln;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wotrow anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Stein aufnehmen
                if (stein.isPointInRect(pTemp) &&
                        !mainFrame.actions[980]) {
                    // Testen, ob die Schunkelanim erfolgen muss oder nicht
                    if (isRight &&
                            megaLinks.isPointInRect(pTemp)) {
                        // will ohne Stein von rechts nach links
                        mainFrame.pathWalker.setNewWay(Pschaukeln);
                        nextActionID = 600;
                    } else {
                        // normales Steinmitnehmen
                        nextActionID = 50;
                        mainFrame.pathWalker.setNewWay(Pstein);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning ||
                mainFrame.krabat.nAnimation != 0) {
            if (cursorShape != 20) {
                cursorShape = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getBoundingBox();
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) ||
                    stein.isPointInRect(pTemp) &&
                            !mainFrame.actions[980];

            if (cursorShape != 10 && !mainFrame.isInventoryHighlightCursor) {
                cursorShape = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (cursorShape != 11 && mainFrame.isInventoryHighlightCursor) {
                cursorShape = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (rechterAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 2) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    cursorShape = 2;
                }
                return;
            }

            if (stein.isPointInRect(pTemp) &&
                    !mainFrame.actions[980]) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            // sonst normal-Cursor
            if (cursorShape != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                cursorShape = 0;
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
            keyClear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            keyClear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            keyClear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void keyClear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.stopWalking();
    }

    // Umgebungs-Sounds abspielen
    private void evalSound() {
        if (schnauzeHintergrund) {
            return; // nix tun bei Abschalten
        }

        int zfz = (int) (Math.random() * 100);

        if (zfz > 98) {
            mainFrame.soundPlayer.playFile("sfx/uhu3.wav");
        }
        if (zfz > 92) {
            mainFrame.soundPlayer.playFile("sfx/grillen2.wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {
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
            switchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Stein anschauen
                krabatSays("Hrodz_1", fStein, 3, 0, 0);
                break;

            case 50:
                // Stein mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(fStein);
                mainFrame.krabat.nAnimation = 94;
                mainFrame.inventory.vInventory.addElement(62);
                Counter = 5;
                nextActionID = 55;
                break;

            case 55:
                // Text sagen
                if (--Counter == 1) {
                    mainFrame.actions[980] = true; // Flag setzen, es gibt keine 2 Steine !!!
                    mainFrame.isClipSet = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                krabatSays("Hrodz_2", 0, 3, 2, 60);
                break;

            case 60:
                // Ende Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu Wotrow
                createNewLocation(200, 201);
                break;

            case 150:
                // Stein - Ausreden
                thingExcuse(fStein);
                break;

            // Anim : gehe von rechts nach links (immer ohne Stein) ////////////////////

            case 600:
                // von rechts nach links ohne Stein
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(9);
                Counter = 20;
                schnauzeHintergrund = true;
                nextActionID = 610;
                break;

            case 610:
                // warten Ende anim
                if (--Counter > 1) {
                    break;
                }
                SonderAnim = 1;
                nextActionID = 620;
                mainFrame.soundPlayer.playFile("sfx/knack.wav");
                break;

            case 620:
                // weiterlaufen
                if (SonderAnim != 0) {
                    break;
                }
                krabatSays("Hrodz_3", 9, 3, 0, 0);
                nextActionID = 630;
                break;

            case 630:
                // Ende
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                isRight = false;
                schnauzeHintergrund = false;
                mainFrame.repaint();
                break;

            // Anim : gehe von links nach rechts ohne Stein ////////////////////////////////

            case 700:
                // von links nach rechts ohne Stein
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(3);
                schnauzeHintergrund = true;
                Counter = 20;
                nextActionID = 710;
                break;

            case 710:
                // warten Ende anim
                if (--Counter > 1) {
                    break;
                }
                SonderAnim = 2;
                nextActionID = 720;
                mainFrame.soundPlayer.playFile("sfx/knack.wav");
                break;

            case 720:
                // weiterlaufen
                if (SonderAnim != 0) {
                    break;
                }
                krabatSays("Hrodz_4", 3, 3, 0, 0);
                nextActionID = 730;
                break;

            case 730:
                // Ende
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                isRight = true;
                schnauzeHintergrund = false;
                mainFrame.repaint();
                break;

            // Anim : gehe von links nach rechts mit Stein (falling down, ein ganz normaler Tag) /////////////

            case 800:
                // von links nach rechst mit Stein
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                Counter = 20;
                nextActionID = 810;
                schnauzeHintergrund = true;
                mainFrame.krabat.setFacing(3);
                break;


            case 810:
                // warten Ende anim
                if (--Counter > 0) {
                    break;
                }
                mainFrame.soundPlayer.playFile("sfx/pasle1.wav");
                krabatFaelltRunter = true;
                SonderAnim = 3;
                krabatSays("Hrodz_5", 3, 3, 0, 820);
                break;

            case 820:
                // reinfallen -> Rowy
                if (SonderAnim != 0) {
                    break;
                }
                createNewLocation(202, 201);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}