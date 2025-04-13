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
import de.codengine.krabat.anims.WasherWoman;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Wila1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Wila1.class);
    private GenericImage background;
    private GenericImage foreground;
    private GenericImage leineleer;
    private GenericImage clean;
    private GenericImage dirty;
    private GenericImage stom;
    private GenericImage dooropen;
    private GenericImage vdoor;
    private GenericImage stange;
    private GenericImage onelost;
    private GenericImage twolost;
    private GenericImage threelost;
    private final GenericImage[] krabat_waesche;

    private WasherWoman waschfrau;

    private boolean showPlokarka = false; // Ist sie ueberhaupt zu sehen ???
    private boolean walkReady = true; // Flag, ob sie denn schon fertiggelaufen ist

    private boolean hide = false; // versteckt sich Krabat hinter Baum ?
    private boolean waschClip = false; // muss die Waschfrau ueberdeckt werden ?
    private boolean isAbnehming = false; // nimmt sie gerade waesche ab ??

    private int waescheFehlt;

    private int SonderAnim = 0;
    private int AnimCounter = 0;
    private int AnimPosition = 0;

    // Konstanten - Rects
    private static final BorderRect obererAusgang = new BorderRect(378, 90, 464, 144);
    private static final BorderRect untererAusgang = new BorderRect(102, 437, 400, 479);
    private static final BorderRect rechterAusgang = new BorderRect(600, 306, 639, 479);
    private static final BorderRect kleiderRect = new BorderRect(375, 235, 441, 280);
    private static final BorderRect leineRect = new BorderRect(375, 240, 441, 265);
    private static final BorderRect durjeRect = new BorderRect(300, 347, 328, 411);
    private static final BorderRect dachRect = new BorderRect(355, 282, 421, 351); // fuer Vordergrund
    private static final BorderRect stangeRect = new BorderRect(423, 261, 476, 372); // fuer Vordergrund

    // Konstante Points
    private static final GenericPoint Pdown = new GenericPoint(208, 479);
    private static final GenericPoint Pright = new GenericPoint(639, 411);
    private static final GenericPoint Pup = new GenericPoint(428, 145);
    private static final GenericPoint Pleine = new GenericPoint(418, 298);
    private static final GenericPoint Pkleider = new GenericPoint(409, 293);
    private static final GenericPoint pVorBaum = new GenericPoint(461, 177);
    private static final GenericPoint pBaum = new GenericPoint(495, 176);
    private static final GenericPoint Pdurje = new GenericPoint(312, 433);
    private static final GenericPoint pVollspritz = new GenericPoint(454, 299);

    private static final GenericPoint waschLook = new GenericPoint(384, 423);
    private static final GenericPoint waschInDoor = new GenericPoint(313, 418);
    private static final GenericPoint waschVorDoor = new GenericPoint(325, 423);

    // Konstante ints
    private static final int fSaty = 12;
    private static final int fLajna = 12;
    private static final int fDurje = 12;
    private static final int fVollspritz = 9;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wila1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 463;
        mainFrame.krabat.zoomFactor = 5.3f;
        mainFrame.krabat.defaultScale = 10;

        waschfrau = new WasherWoman(mainFrame);
        waschfrau.maxX = 400;
        waschfrau.zoomFactor = 4f;
        waschfrau.defaultScale = 0;

        if (!mainFrame.actions[175]) {
            waescheFehlt = 0;
        }

        krabat_waesche = new GenericImage[2];

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(166, 458, 639, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(352, 639, 166, 639, 428, 457));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(536, 639, 464, 639, 373, 427));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(550, 639, 536, 639, 333, 372));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(488, 494, 519, 560, 249, 297));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(465, 298, 567, 307));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(523, 567, 550, 625, 308, 332));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(390, 298, 464, 307));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(460, 464, 488, 494, 178, 248));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(435, 437, 460, 464, 145, 177));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(10);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 6);
        mainFrame.pathFinder.connectPos(6, 5);
        mainFrame.pathFinder.connectPos(4, 5);
        mainFrame.pathFinder.connectPos(5, 7);
        mainFrame.pathFinder.connectPos(4, 8);
        mainFrame.pathFinder.connectPos(8, 9);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 15:
                // von Njedz aus
                if (mainFrame.enteringFromMap) {
                    mainFrame.enteringFromMap = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(293, 465));
                mainFrame.krabat.setFacing(12);
                break;
            case 17:
                // von Kolmc aus
                if (mainFrame.enteringFromMap) {
                    mainFrame.enteringFromMap = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(436, 147));
                mainFrame.krabat.setFacing(6);
                break;
            case 18:
                // von Dubring aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(620, 404));
                mainFrame.krabat.setFacing(9);
                break;
        }
        mainFrame.enteringFromMap = false;
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/villa/villa.png");
        foreground = getPicture("gfx/villa/villa2.png");
        leineleer = getPicture("gfx/villa/villa3.png");
        clean = getPicture("gfx/villa/villa4.png");
        dirty = getPicture("gfx/villa/villa5.png");

        stom = getPicture("gfx/villa/stom.png");
        vdoor = getPicture("gfx/villa/durje-1.png");
        dooropen = getPicture("gfx/villa/v-durje.png");
        stange = getPicture("gfx/villa/villa6.png");

        onelost = getPicture("gfx/villa/vw2.png");
        twolost = getPicture("gfx/villa/vw1.png");
        threelost = getPicture("gfx/villa/vw3.png");

        krabat_waesche[0] = getPicture("gfx/villa/k-l-bloto1.png");
        krabat_waesche[1] = getPicture("gfx/villa/k-l-bloto2.png");

    }

    @Override
    public void cleanup() {
        background = null;
        foreground = null;
        leineleer = null;
        clean = null;
        dirty = null;

        stom = null;
        vdoor = null;
        dooropen = null;
        stange = null;

        onelost = null;
        twolost = null;
        threelost = null;

        krabat_waesche[0] = null;
        krabat_waesche[1] = null;

        waschfrau.cleanup();
        waschfrau = null;
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

        // Waschfrau Hintergrund loeschen
        if (showPlokarka) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = waschfrau.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
        }

        // Hier Entscheidung, was wann gezeichnet wird

        // Offene Tuer  zeichnen
        if (showPlokarka) {
            g.setClip(294, 340, 52, 82);
            g.drawImage(dooropen, 294, 340);
        }

        // nur, wenn Leine nicht aufgehoben, muss gemalt werden
        if (!mainFrame.actions[906]) {
            if (!mainFrame.actions[177]) {
                // Waesche haengt normal da
                g.setClip(370, 228, 83, 78);
                g.drawImage(clean, 370, 228);
            } else {
                if (!mainFrame.actions[175]) {
                    g.setClip(370, 227, 84, 80);
                    switch (waescheFehlt) {
                        case 0:
                            // schmutzige Waesche zeichnen, alle Stuecke dran
                            g.drawImage(dirty, 370, 228);
                            break;

                        case 1:
                            // 1. Stueck weg
                            g.drawImage(onelost, 370, 227);
                            break;

                        case 2:
                            // 2. Stueck weg
                            g.drawImage(twolost, 370, 227);
                            break;

                        case 3:
                            // 3. Stueck weg
                            g.drawImage(threelost, 370, 227);
                            break;

                        default:
                            log.error("Fehler im LeineChooser! waescheFehlt = {}", waescheFehlt);
                            break;
                    }
                } else {
                    // leere Leine zeichnen
                    g.setClip(370, 227, 83, 78);
                    g.drawImage(leineleer, 370, 227);
                }
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Waschfrau bewegen
        if (showPlokarka && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = waschfrau.move();
        }

        // Waschfrau zeichnen
        if (showPlokarka) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = waschfrau.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne sie jetzt

            // Redet sie etwa gerade ??
            if ((talkPerson == 27 || talkPerson == 55) && mainFrame.talkCount > 0) {
                if (talkPerson == 27) {
                    waschfrau.talkPlokarka(g);
                }
                if (talkPerson == 55) {
                    waschfrau.haendePlokarka(g);
                }
            }

            // nur rumstehen oder laufen
            else {
                if (isAbnehming) {
                    isAbnehming = waschfrau.nimmWaescheAb(g);
                    if (!isAbnehming) {
                        waescheFehlt++;
                    }
                } else {
                    waschfrau.drawPlokarka(g);
                }
            }

            // Tuer - Vordergrund ins Clipping - Rect zeichnen
            g.drawImage(vdoor, 285, 330);
        }

        // Hier Vordergrund fuer Waschfrau zeichnen
        if (waschClip) {
            g.drawImage(foreground, 340, 292);
        }

        mainFrame.pathWalker.doWalk();

        // Krabat zeichnen

        if (SonderAnim != 0) {
            // Sonderanims ausfuehren
            if (SonderAnim == 1) {
                // Waesche beschmutzen

                // Groesse
                int scale = mainFrame.krabat.defaultScale;
                scale += (int) (((float) mainFrame.krabat.maxX - (float) mainFrame.krabat.getPos().y) / mainFrame.krabat.zoomFactor);

                // Hoehe: nur offset
                int hoch = 100 - scale;

                // Breite
                int weit = 50 - scale / 2;

                // Punkt fuer LO-Evaluierung bereitstellen
                GenericPoint hier = mainFrame.krabat.getPos();

                hier.x -= (int) ((float) weit * 0.7f);
                hier.y -= hoch;

                // Cliprect setzen
                g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

                // GenericImage weiterschalten
                if (++AnimCounter % 4 == 0) {
                    if (AnimPosition == 1) {
                        AnimPosition = 0;
                    } else {
                        AnimPosition = 1;
                    }
                }

                // Krabat zeichnen
                g.drawImage(krabat_waesche[AnimPosition], hier.x, hier.y, weit, hoch);

                // eal. Ob die Anim zu Ende ist
                if (AnimCounter > 8) {
                    SonderAnim = 0;
                }
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

        // Ab hier muss Cliprect wieder gerettet werden
        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Hier Vordergruende waehrend der Anim zeichnen
        if (hide) {
            g.drawImage(stom, 485, 143);
        }

        // Hier Krabat - Vordergruende zeichnen
        if (dachRect.isPointInRect(pKrTemp)) {
            g.drawImage(vdoor, 285, 330);
        }

        // Hier Krabat - Vordergruende zeichnen
        if (stangeRect.isPointInRect(pKrTemp)) {
            g.drawImage(stange, 444, 273);
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
            talkPerson = 0;
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

                // Ausreden fuer Leine
                if (leineRect.isPointInRect(pTemp) && !mainFrame.actions[906] &&
                        mainFrame.actions[175]) {
                    nextActionID = 260;
                    pTemp = Pleine;
                }

                // Ausreden fuer Kleider
                if (kleiderRect.isPointInRect(pTemp) && !mainFrame.actions[175]) {
                    if (mainFrame.whatItem == 16) { // Honck z blotom
                        nextActionID = 155;
                        pTemp = pVollspritz;
                    } else {
                        nextActionID = 150;
                        pTemp = Pkleider;
                    }
                }

                // Ausreden fuer Tuer
                if (durjeRect.isPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 250 : 270;
                    pTemp = Pdurje;
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

                // zu Njedz gehen ?
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

                // zu Dubring gehen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.isPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Kolmc gehen
                if (obererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
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

                // Leine ansehen
                if (leineRect.isPointInRect(pTemp) && !mainFrame.actions[906] &&
                        mainFrame.actions[175]) {
                    nextActionID = 2;
                    pTemp = Pleine;
                }

                // Kleider ansehen
                if (kleiderRect.isPointInRect(pTemp) && !mainFrame.actions[175]) {
                    nextActionID = 1;
                    pTemp = Pkleider;
                }

                // Tuer ansehen
                if (durjeRect.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Pdurje;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Njedz Anschauen
                if (untererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Kolmc anschauen
                if (obererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Dubring anschauen
                if (rechterAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Leine benutzen ?
                if (leineRect.isPointInRect(pTemp) && !mainFrame.actions[906] &&
                        mainFrame.actions[175]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Pleine);
                    mainFrame.repaint();
                    return;
                }

                // Kleider benutzen ?
                if (kleiderRect.isPointInRect(pTemp) && !mainFrame.actions[175]) {
                    nextActionID = 53;
                    mainFrame.pathWalker.setNewWay(Pkleider);
                    mainFrame.repaint();
                    return;
                }

                // Tuer benutzen ?
                if (durjeRect.isPointInRect(pTemp)) {
                    nextActionID = 280;
                    mainFrame.pathWalker.setNewWay(Pdurje);
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
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) ||
                    leineRect.isPointInRect(pTemp) && !mainFrame.actions[906] && mainFrame.actions[175] ||
                    kleiderRect.isPointInRect(pTemp) && !mainFrame.actions[175] ||
                    durjeRect.isPointInRect(pTemp);

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
            if (leineRect.isPointInRect(pTemp) && !mainFrame.actions[906] && mainFrame.actions[175] ||
                    kleiderRect.isPointInRect(pTemp) && !mainFrame.actions[175] ||
                    durjeRect.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
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

            if (obererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 4;
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
                // Kleider anschauen
                krabatSays("Wila1_1", fSaty, 3, 0, 0);
                break;

            case 2:
                // Leine anschauen
                // Zufallszahl zwischen 0 und 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Wila1_2", fLajna, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Wila1_3", fLajna, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Tuer anschauen
                krabatSays("Wila1_4", fDurje, 3, 0, 0);
                break;

            case 50:
                // Leine mitnehmen ??
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.setFacing(fLajna);
                mainFrame.krabat.nAnimation = 120;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 51;
                Counter = 5;
                break;

            case 51:
                // Ende Leine nehmen
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(6);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[906] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 53:
                // Kleider mitnehmen ??
                // Waschfrau muss erscheinen, vorher Versuch mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(fLajna); // Krabat schaut auf Leine
                mainFrame.krabat.nAnimation = 121;
                Counter = 3;
                nextActionID = 54;
                break;

            case 54:
                // jetzt die Frau her
                if (--Counter > 0) {
                    break;
                }
                mainFrame.soundPlayer.playFile("sfx/vdurjeauf.wav");
                showPlokarka = true;
                waschfrau.setPos(waschInDoor);
                waschfrau.setFacing(3);
                nextActionID = 55;
                break;

            case 55:
                // Waschfrau loslaufen lassen nach unten
                waschfrau.moveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 58;
                break;

            case 58:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 60;
                }
                break;

            case 60:
                // Waschfrau loslaufen lassen zur Anguckposition
                waschfrau.moveTo(waschLook);
                walkReady = false;
                nextActionID = 62;
                break;

            case 62:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 65;
                }
                break;

            case 65:
                // Sie sagt ihren Spruch
                mainFrame.krabat.setFacing(6);
                if (!mainFrame.actions[176]) {
                    outputText = mainFrame.imageFont.splitTextKey("Wila1_8");
                    mainFrame.actions[176] = true;
                } else {
                    outputText = mainFrame.imageFont.splitTextKey("Wila1_9");
                }
                waschfrau.setFacing(12);
                // Hier Position des Textes berechnen
                BorderRect temp = waschfrau.getBoundingBox();
                GenericPoint tTalk = new GenericPoint((temp.bottomRightPoint.x + temp.topLeftPoint.x) / 2, temp.topLeftPoint.y - 50);
                outputTextPos = mainFrame.imageFont.centerText(outputText, tTalk);
                talkPerson = 27;
                nextActionID = 70;
                break;

            case 70:
                // Waschfrau loslaufen lassen nach links
                waschfrau.moveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 72;
                break;

            case 72:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 75;
                }
                break;

            case 75:
                // Waschfrau loslaufen lassen nach Oben
                waschfrau.moveTo(waschInDoor);
                walkReady = false;
                nextActionID = 78;
                break;

            case 78:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 80;
                }
                break;

            case 80:
                // Waschfrau wieder verschwinden lassen
                showPlokarka = false;
                mainFrame.soundPlayer.playFile("sfx/vdurjezu.wav");
                mainFrame.isClipSet = false;
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Njedz
                createNewLocation(15, 16);
                break;

            case 101:
                // gehe zu Kolmc 
                createNewLocation(17, 16);
                break;

            case 102:
                // nach Dubring gehen
                createNewLocation(18, 16);
                break;

            case 150:
                // Kleider - Ausreden
                thingExcuse(fLajna);
                break;

            // Anim : Waesche vollspritzen und verstecken ////////////////////////////

            case 155:
                // Waesche mit Schlamm vollspritzen
                mainFrame.isInventoryCursor = false;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(fVollspritz);
                SonderAnim = 1;
                nextActionID = 160;
                break;

            case 160:
                // Krabat sagt ich verdufte
                if (AnimCounter == 6) {
                    // hier die Waesche schmutzig machen
                    mainFrame.actions[177] = true;
                    mainFrame.isClipSet = false;
                    mainFrame.soundPlayer.playFile("sfx/bloto2.wav");
                }
                if (mainFrame.krabat.nAnimation != 0 || SonderAnim != 0) {
                    break;
                }
                krabatSays("Wila1_5", 0, 3, 0, 165);
                break;

            case 165:
                // Krabat versteckt sich
                hide = true;
                mainFrame.inventory.vInventory.addElement(4);
                mainFrame.inventory.vInventory.removeElement(16); // honck wieder leer machen
                mainFrame.pathWalker.setNewWay(pVorBaum);
                nextActionID = 168;
                break;

            case 168:
                // hinter Baum gehen
                mainFrame.pathWalker.setNewWayGuaranteed(pBaum);
                nextActionID = 170;
                break;

            case 170:
                // Noch richtigrumdrehen
                mainFrame.krabat.setFacing(6);
                nextActionID = 175;
                break;

            case 175:
                // Waschfrau muss erscheinen
                showPlokarka = true;
                mainFrame.soundPlayer.playFile("sfx/vdurjeauf.wav");
                waschfrau.setPos(waschInDoor);
                waschfrau.setFacing(6);
                nextActionID = 180;
                break;

            case 180:
                // Waschfrau loslaufen lassen nach unten
                waschfrau.moveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 185;
                break;

            case 185:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 186;
                }
                break;

            case 186:
                // Waschfrau loslaufen lassen zur Anguckposition
                waschfrau.moveTo(waschLook);
                walkReady = false;
                nextActionID = 187;
                break;

            case 187:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 190;
                }
                break;

            case 190:
                // Sie sagt ihren Spruch
                outputText = mainFrame.imageFont.splitTextKey("Wila1_10");
                // Hier Position des Textes berechnen
                BorderRect tmp = waschfrau.getBoundingBox();
                GenericPoint tTlk = new GenericPoint((tmp.bottomRightPoint.x + tmp.topLeftPoint.x) / 2, tmp.topLeftPoint.y - 50);
                outputTextPos = mainFrame.imageFont.centerText(outputText, tTlk);
                waschfrau.setFacing(12);
                talkPerson = 55;
                nextActionID = 191;
                break;

            case 191:
                // Waschfrau loslaufen lassen, soll 1. Stueck abnehmen
                waschClip = true;
                waschfrau.moveTo(new GenericPoint(426, 304));
                walkReady = false;
                nextActionID = 192;
                break;

            case 192:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 193;
                }
                break;

            case 193:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 194;
                break;

            case 194:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    nextActionID = 195;
                }
                break;

            case 195:
                // Waschfrau loslaufen lassen, soll 2. Stueck abnehmen
                waschfrau.hasWaesche = true;
                waschfrau.moveTo(new GenericPoint(410, 313));
                walkReady = false;
                nextActionID = 196;
                break;

            case 196:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 197;
                }
                break;

            case 197:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 198;
                break;

            case 198:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    nextActionID = 199;
                }
                break;

            case 199:
                // Waschfrau loslaufen lassen, soll 3. Stueck abnehmen
                waschfrau.moveTo(new GenericPoint(389, 315));
                walkReady = false;
                nextActionID = 200;
                break;

            case 200:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 201;
                }
                break;

            case 201:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 202;
                break;

            case 202:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    nextActionID = 203;
                }
                break;

            case 203:
                // Waschfrau loslaufen lassen, soll 4. Stueck abnehmen
                waschfrau.moveTo(new GenericPoint(371, 315));
                walkReady = false;
                nextActionID = 204;
                break;

            case 204:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 205;
                }
                break;

            case 205:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 206;
                break;

            case 206:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    mainFrame.actions[175] = true;
                    nextActionID = 207;
                }
                break;

            case 207:
                // Waesche ist abgenommen, kennzeichnen
                mainFrame.isClipSet = false;
                nextActionID = 210;
                break;

            case 210:
                // Waschfrau loslaufen lassen zurueck zur Anguckposition
                waschClip = true;
                waschfrau.moveTo(waschLook);
                walkReady = false;
                nextActionID = 215;
                break;

            case 215:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 220;
                }
                break;

            case 220:
                // Waschfrau loslaufen lassen anch links
                waschClip = false;
                waschfrau.moveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 225;
                break;

            case 225:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 226;
                }
                break;

            case 226:
                // Waschfrau loslaufen lassen nach oben
                waschfrau.moveTo(waschInDoor);
                walkReady = false;
                nextActionID = 227;
                break;

            case 227:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 230;
                }
                break;

            case 230:
                // Waschfrau wieder verschwinden lassen
                showPlokarka = false;
                mainFrame.soundPlayer.playFile("sfx/vdurjezu.wav");
                mainFrame.isClipSet = false;
                nextActionID = 235;
                break;

            case 235:
                // Krabat kommt hinter Versteck wieder vor
                mainFrame.pathWalker.setNewWayGuaranteed(pVorBaum);
                nextActionID = 240;
                break;

            case 240:
                // Anim beenden
                hide = false;
                mainFrame.isClipSet = false;
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 250:
                // Tuer anschauen
                krabatSays("Wila1_6", fDurje, 3, 0, 0);
                break;

            case 260:
                // lajna - Ausreden
                thingExcuse(fLajna);
                break;

            case 270:
                // Durje - Ausreden
                thingExcuse(fDurje);
                break;

            case 280:
                // Tuer mitnehmen
                krabatSays("Wila1_7", fDurje, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}