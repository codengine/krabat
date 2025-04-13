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
import de.codengine.krabat.anims.KrabatShoot;
import de.codengine.krabat.anims.Rapak;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Rapak1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Rapak1.class);
    private GenericImage background;
    private GenericImage blumen1;
    private GenericImage blumen2;
    private GenericImage schild;

    private final GenericImage[] Feder;
    private boolean animit = false;
    private boolean segeln = false;
    private int ykoord = 186;
    private int xkoord = 437;
    private static final int NORM_XKOORD = 437;

    private int Zaehl;
    private boolean forward = true;
    private double angle = 0;
    private static final double Offset = 2 * Math.PI / 15;

    private Rapak rabe;
    private KrabatShoot schiesser;
    private int Sonderstatus = 0;

    private int Counter = 0;

    // Konstanten - Rects
    private static final BorderRect obererAusgang = new BorderRect(162, 150, 240, 193);
    private static final BorderRect linkerAusgang = new BorderRect(0, 308, 27, 425);
    private static final BorderRect untererAusgang = new BorderRect(26, 430, 286, 479);
    private static final BorderRect rechterAusgang = new BorderRect(594, 319, 639, 447);
    private static final BorderRect brSchildOben = new BorderRect(393, 204, 466, 228);
    private static final BorderRect brSchild = new BorderRect(399, 230, 467, 309);
    private static final BorderRect blRect1 = new BorderRect(0, 367, 100, 479);
    private static final BorderRect blRect2 = new BorderRect(200, 414, 485, 479);
    private static final BorderRect rapakRect = new BorderRect(396, 147, 465, 179);
    private static final BorderRect pjeroRect = new BorderRect(408, 391, 479, 417);

    // Punkte in Location
    private static final GenericPoint Pschild = new GenericPoint(430, 370);
    private static final GenericPoint Pdown = new GenericPoint(144, 479);
    private static final GenericPoint Pleft = new GenericPoint(15, 369);
    private static final GenericPoint Pup = new GenericPoint(200, 187);
    private static final GenericPoint Pright = new GenericPoint(621, 384);
    private static final GenericPoint Prapak = new GenericPoint(430, 370);
    private static final GenericPoint Pcylic = new GenericPoint(282, 402);
    private static final GenericPoint Ppjero = new GenericPoint(390, 408);
    private static final GenericPoint PpjeroAufh = new GenericPoint(421, 417);

    // Konstante ints
    private static final int fSchild = 12;
    private static final int fRapak = 12;
    private static final int fPjero = 3;
    private static final int fSchildOben = 12;
    private static final int fExitRight = 3;
    private static final int fSchiessen = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Rapak1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 377;
        mainFrame.krabat.zoomFactor = 1.67f;
        mainFrame.krabat.defaultScale = -50;

        Feder = new GenericImage[4];
        rabe = new Rapak(mainFrame);
        schiesser = new KrabatShoot(mainFrame);

        schiesser.maxx = mainFrame.krabat.maxX;
        schiesser.zoomf = mainFrame.krabat.zoomFactor;
        schiesser.defScale = mainFrame.krabat.defaultScale;

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(196, 205, 172, 194, 185, 229));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(172, 230, 194, 261));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(172, 194, 190, 237, 262, 332));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(190, 237, 152, 251, 333, 365));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 369, 85, 416));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(86, 366, 277, 441));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(86, 442, 233, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(278, 376, 459, 441));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(460, 374, 639, 428));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(9);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 5);
        mainFrame.pathFinder.connectPos(4, 5);
        mainFrame.pathFinder.connectPos(5, 6);
        mainFrame.pathFinder.connectPos(5, 7);
        mainFrame.pathFinder.connectPos(7, 8);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 4:
                // von Haty aus
                mainFrame.krabat.setPos(new GenericPoint(147, 470));
                mainFrame.krabat.setFacing(12);
                break;
            case 9:
                // von Horiz aus
                mainFrame.krabat.setPos(new GenericPoint(34, 376));
                mainFrame.krabat.setFacing(3);
                break;
            case 19:
                // von Zdzary aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(195, 200));
                mainFrame.krabat.setFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/rapak/rapak.png");
        blumen1 = getPicture("gfx/rapak/rap1.png");
        blumen2 = getPicture("gfx/rapak/rap3.png");
        schild = getPicture("gfx/rapak/rap2.png");
        Feder[1] = getPicture("gfx/rapak/pkp1.png");
        Feder[2] = getPicture("gfx/rapak/pkp2.png");
        Feder[3] = getPicture("gfx/rapak/pkp3.png");

    }

    @Override
    public void cleanup() {
        background = null;
        blumen1 = null;
        blumen2 = null;
        schild = null;
        Feder[1] = null;
        Feder[2] = null;
        Feder[3] = null;

        schiesser.cleanup();
        schiesser = null;
        rabe.cleanup();
        rabe = null;
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

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Raben Hintergrund loeschen
        GenericRectangle raRect = rabe.rapakRect();
        g.setClip(raRect.getX(), raRect.getY(), raRect.getWidth(), raRect.getHeight());
        g.drawImage(background, 0, 0);

        // Schild zeichnen, da im Hintergrund !!!
        if (!mainFrame.actions[901]) {
            g.setClip(399, 230, 70, 81);
            g.drawImage(schild, 399, 230);
        }

        // Raben zeichnen
        raRect = rabe.rapakRect();
        g.setClip(raRect.getX(), raRect.getY(), raRect.getWidth(), raRect.getHeight());

        // beim Treffer
        if (animit) {
            animit = !rabe.fliegRapak(g);
        }
        // normale Backgroundanims
        else {
            rabe.drawRapak(g, talkPerson);
        }

        // zeichne Feder, wie sie runterfliegt
        if (segeln) {
            // GenericImage weiterschalten
            if (forward) {
                Zaehl++;
                if (Zaehl == 4) {
                    Zaehl = 2;
                    forward = false;
                }
            } else {
                Zaehl--;
                if (Zaehl == 0) {
                    Zaehl = 2;
                    forward = true;
                }
            }

            // Feder zeichnen
            g.setClip(xkoord - 5, ykoord - 4, 21, 16);
            g.drawImage(background, 0, 0);
            if (!mainFrame.actions[901]) {
                g.drawImage(schild, 399, 230);
            }
            g.drawImage(Feder[Zaehl], xkoord, ykoord);

            // Y - Koordinate weiterschalten
            ykoord += 3;

            // X - Koordinate weiterschalten
            angle += Offset;
            if (angle > 2 * Math.PI) {
                angle -= 2 * Math.PI;
            }
            xkoord = (int) Math.round(Math.sin(angle) * 5.1);
            xkoord += NORM_XKOORD;

            // auf Ende pruefen
            if (ykoord >= 394) {
                segeln = false;
            }
        }

        // wenn geschossen, dann Feder zeichnen
        if (mainFrame.actions[210] && !mainFrame.actions[919]) {
            g.setClip(437, 390, 16, 16);
            g.drawImage(background, 0, 0);
            g.drawImage(Feder[1], 437, 394);
        }

        mainFrame.pathWalker.doWalk();

        if (Sonderstatus != 0) {
            // Sonderanims
            g.setClip(160, 160, 325, 320);
            if (!schiesser.drawKrabat(g, mainFrame.krabat.getPos())) {
                Sonderstatus = 0;
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
        }

        // Krabat hinter Gestruepp
        if (blRect1.isPointInRect(pKrTemp)) {
            g.drawImage(blumen1, 0, 374);
        }

        if (blRect2.isPointInRect(pKrTemp)) {
            g.drawImage(blumen2, 241, 417);
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, COLORS[talkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
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

                // Ausreden fuer Schild oben
                if (brSchildOben.isPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 200 : 170;
                    pTemp = Pschild;
                }

                // Ausreden fuer Schild unten, wenn noch da
                if (brSchild.isPointInRect(pTemp) && !mainFrame.actions[901]) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 210;
                            break;
                        case 12: // kamuski
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = Pschild;
                }

                // Ausreden fuer Feder
                if (pjeroRect.isPointInRect(pTemp) && !mainFrame.actions[919] &&
                        mainFrame.actions[210]) {
                    // Standard - Sinnloszeug
                    nextActionID = 165;
                    pTemp = Ppjero;
                }

                // Ausreden fuer Raben
                if (rapakRect.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 18: // bron
                            if (!mainFrame.actions[210]) {
                                nextActionID = 160;
                                pTemp = Pcylic;
                            } else {
                                nextActionID = 220;
                                pTemp = Pschild;
                            }
                            break;

                        case 2: // kij
                        case 7: // wuda
                        case 9: // wuda+hocka
                        case 10: // wuda+hocka+dryba
                        case 11: // wuda+hocka+wacki
                            nextActionID = 230;
                            pTemp = Pschild;
                            break;

                        default:
                            nextActionID = 155;
                            pTemp = Pschild;
                    }
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

                // zu Haty gehen ?
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Horiz gehen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.isPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // oberer Ausgang zu Zdzary
                if (obererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.isPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // rechter Ausgang
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }
                }

                // Schild ansehen
                if (brSchild.isPointInRect(pTemp) && !mainFrame.actions[901]) {
                    nextActionID = 1;
                    pTemp = Pschild;
                }

                // Schild oben ansehen
                if (brSchildOben.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Pschild;
                }

                // Rapak ansehen
                if (rapakRect.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Prapak;
                }

                // Pjero ansehen
                if (pjeroRect.isPointInRect(pTemp) && !mainFrame.actions[919] &&
                        mainFrame.actions[210]) {
                    nextActionID = 3;
                    pTemp = Ppjero;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Haty Anschauen
                if (untererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Horiz anschauen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Zdzary anschauen
                if (obererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // rechten Ausgang anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Schild mitnehmen
                if (brSchild.isPointInRect(pTemp) && !mainFrame.actions[901]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Schild oben mitnehmen
                if (brSchildOben.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Raben fangen
                if (rapakRect.isPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(Prapak);
                    mainFrame.repaint();
                    return;
                }

                // Pjero fangen
                if (pjeroRect.isPointInRect(pTemp) && !mainFrame.actions[919] &&
                        mainFrame.actions[210]) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(PpjeroAufh);
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
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
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
            mainFrame.isInventoryHighlightCursor = brSchild.isPointInRect(pTemp) && !mainFrame.actions[901]
                    || tmp.isPointInRect(pTemp) || rapakRect.isPointInRect(pTemp) ||
                    !mainFrame.actions[919] && !mainFrame.actions[210] && pjeroRect.isPointInRect(pTemp) ||
                    brSchildOben.isPointInRect(pTemp);

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
            if (obererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 4;
                }
                return;
            }

            if (brSchild.isPointInRect(pTemp) && !mainFrame.actions[901] ||
                    rapakRect.isPointInRect(pTemp) || brSchildOben.isPointInRect(pTemp) ||
                    !mainFrame.actions[919] && mainFrame.actions[210] && pjeroRect.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }


            if (linkerAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    cursorShape = 2;
                }
                return;
            }

            if (rechterAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    cursorShape = 3;
                }
                return;
            }

            if (untererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 5;
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
                // Schild anschauen
                krabatSays("Rapak1_1", fSchild, 3, 0, 0);
                break;

            case 2:
                // Raben anschauen, Zufallszahl zwischen 0 und 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Rapak1_2", fRapak, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Rapak1_3", fRapak, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Pjero anschauen
                krabatSays("Rapak1_4", fPjero, 3, 0, 0);
                break;

            case 4:
                // Schild oben anschauen
                krabatSays("Rapak1_5", fSchildOben, 3, 0, 0);
                break;

            case 50:
                // Schild mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.soundPlayer.playFile("sfx/schildnehmen.wav");
                nextActionID = 53;
                mainFrame.krabat.setFacing(fSchild);
                mainFrame.krabat.nAnimation = 121;
                Counter = 5;
                break;

            case 53:
                // Ende Schild aufheben
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(3);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[901] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                nextActionID = 0;
                break;

            case 55:
                // Raben mitnehmen
                animit = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.soundPlayer.playFile("sfx/rapak1.wav");
                mainFrame.krabat.setFacing(fRapak);
                mainFrame.krabat.nAnimation = 150;
                nextActionID = 57;
                break;

            case 57:
                // Krabat sagt Spruch
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                if (animit) {
                    break;
                }
                krabatSays("Rapak1_6", 0, 3, 0, 58);
                break;

            case 58:
                // Ende Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 60:
                // Feder mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 63;
                mainFrame.krabat.setFacing(fPjero);
                mainFrame.krabat.nAnimation = 122;
                Counter = 5;
                break;

            case 63:
                // Ende Feder aufheben
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(19);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[919] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 65:
                // Schild oben mitnehmen
                krabatSays("Rapak1_7", fSchildOben, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Haty
                createNewLocation(4, 8);
                break;

            case 101:
                // nach Horiz gehen
                createNewLocation(9, 8);
                break;

            case 102:
                // gehe zu Zdzary
                createNewLocation(19, 8);
                break;

            case 103:
                // nach rechts gehen will ich nicht !
                krabatSays("Rapak1_8", fExitRight, 3, 0, 0);
                break;

            case 150:
                // Schild - Ausreden
                thingExcuse(fSchild);
                break;

            case 155:
                // Rapak - Ausreden
                thingExcuse(fRapak);
                break;

            case 160:
                // Schiessen
                mainFrame.krabat.setFacing(fSchiessen);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.inventory.vInventory.addElement(17);
                mainFrame.inventory.vInventory.removeElement(18); // Waffe wieder entladen
                mainFrame.isInventoryCursor = false;
                Sonderstatus = 1;
                nextActionID = 161;
                break;

            case 161:
                // warten auf Ende Schiessen
                if (Sonderstatus == 0) {
                    nextActionID = 163;
                }
                break;

            case 163:
                // Rabe laesst Feder fallen
                nextActionID = 164;
                animit = true;
                if (!mainFrame.actions[210]) {
                    segeln = true;
                }
                break;

            case 164:
                // Auf Ende beider Anims warten und dann beenden
                if (animit || segeln) {
                    break;
                }
                mainFrame.isClipSet = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.actions[210] = true;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 165:
                // Feder - Ausreden
                thingExcuse(fPjero);
                break;

            case 170:
                // Schild oben - Ausreden
                thingExcuse(fSchildOben);
                break;

            case 200:
                // Kamuski auf schildern
                krabatSays("Rapak1_9", fSchild, 3, 0, 0);
                break;

            case 210:
                // Kij auf schild unten
                krabatSays("Rapak1_10", fSchild, 3, 0, 0);
                break;

            case 220:
                // bron auf rapak 2. Mal
                krabatSays("Rapak1_11", fRapak, 3, 0, 0);
                break;

            case 230:
                // Stock+Kombinationen auf Rapak
                krabatSays("Rapak1_12", fRapak, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}