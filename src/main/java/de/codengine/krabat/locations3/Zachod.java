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

public class Zachod extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Zachod.class);
    private GenericImage background;
    private GenericImage seil;
    private GenericImage deska;
    private GenericImage kista;
    private GenericImage kista2;
    private final GenericImage[] krabat_schieb;

    // Konstanten - Rects
    private static final BorderRect ausgangCychi
            = new BorderRect(468, 190, 532, 294);
    private static final BorderRect ausgangGang
            = new BorderRect(132, 376, 230, 421);
    private static final BorderRect rectSeil
            = new BorderRect(574, 271, 598, 298);
    private static final BorderRect staraDeska
            = new BorderRect(542, 432, 584, 457);
    private static final BorderRect blido
            = new BorderRect(300, 324, 377, 350);
    private static final BorderRect korbik
            = new BorderRect(162, 138, 194, 180);
    private static final BorderRect kisty
            = new BorderRect(99, 345, 200, 400);
    private static final BorderRect kistyUnten
            = new BorderRect(48, 394, 150, 479);
    private static final BorderRect nowaDeska
            = new BorderRect(156, 371, 204, 429);
    private static final BorderRect kistenRect
            = new BorderRect(0, 330, 300, 420);

    private static final GenericPoint AnfangsPunkt = new GenericPoint(99, 345);
    private static final GenericPoint AnfangsPunkt2 = new GenericPoint(157, 347);
    private static final GenericPoint EndPunkt = new GenericPoint(48, 394);

    private static final float Xoffset = 3f;
    private final float Yoffset;

    private float xpos;
    private float ypos;
    private GenericPoint festnagelPoint = new GenericPoint(0, 0); // Krabat darf sich beim schieben nicht relativ zu den Kisten bewegen
    private GenericPoint bezugsPunkt = new GenericPoint(0, 0);

    private boolean verschiebeKiste = false;

    private boolean kistenSound = false;

    private int AnimPosition = 0;
    private int AnimCounter = 0;
    private int Anfangsscale = 0;

    // Konstante Points
    private static final GenericPoint pExitCychi = new GenericPoint(500, 299);
    private static final GenericPoint pExitGang = new GenericPoint(180, 400);
    private static final GenericPoint pSeil = new GenericPoint(559, 341);
    private static final GenericPoint pStaraDeska = new GenericPoint(542, 434);
    private static final GenericPoint pBlido = new GenericPoint(314, 420);
    private static final GenericPoint pKorbik = new GenericPoint(175, 324);
    private static final GenericPoint pKistyOben = new GenericPoint(179, 385);
    private static final GenericPoint pKistyUnten = new GenericPoint(227, 465);
    private static final GenericPoint pNowaDeska = new GenericPoint(207, 395);
    private static final GenericPoint pEntnagel1 = new GenericPoint(185, 377);
    private static final GenericPoint pEntnagel2 = new GenericPoint(207, 380);
    private static final GenericPoint pEntnagel3 = new GenericPoint(140, 406);
    private static final GenericPoint pEntnagel4 = new GenericPoint(158, 418);

    // Konstante ints
    private static final int fKisteOben = 6;
    private static final int fKisteUnten = 9;
    private static final int fSeil = 3;
    private static final int fBrettNeu = 9;
    private static final int fBrettAlt = 3;
    private static final int fBlido = 12;
    private static final int fKorbik = 12;

    private int Counter = 0;

    private int SonderAnim = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zachod(Start caller, int oldLocation) {
        super(caller, 151);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        // Berechnungen fuer Kistenverschieben

        // Anfangskoordinaten
        xpos = AnfangsPunkt.x;
        ypos = AnfangsPunkt.y;

        // Y - Offset beim Verschieben
        float xe = EndPunkt.x;
        float ye = EndPunkt.y;

        Yoffset = (ye - ypos) / (xe - xpos) * Xoffset;

        mainFrame.krabat.maxX = 403;
        mainFrame.krabat.zoomFactor = 2.29f;
        mainFrame.krabat.defaultScale = -50;

        krabat_schieb = new GenericImage[2];

        initLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        initMatrix();

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 150: // von Cychi
                mainFrame.krabat.setPos(new GenericPoint(500, 310));
                mainFrame.krabat.setFacing(6);
                break;
            case 152: // von Gang
                mainFrame.krabat.setPos(new GenericPoint(214, 395));
                mainFrame.krabat.setFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/zachod/zachod.png");

        seil = getPicture("gfx-dd/zachod/seil.png");
        deska = getPicture("gfx-dd/zachod/deska.png");
        kista = getPicture("gfx-dd/zachod/kista.png");
        kista2 = getPicture("gfx-dd/zachod/kista2.png");

        krabat_schieb[0] = getPicture("gfx-dd/zachod/k-u-kiste1.png");
        krabat_schieb[1] = getPicture("gfx-dd/zachod/k-u-kiste2.png");

    }

    // Laufrectangles aendern, je nachdem, wo Kisten sind
    private void initMatrix() {
        if (!mainFrame.actions[516]) {
            // Kisten sind noch oben
            mainFrame.pathWalker.vBorders.removeAllElements();
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(480, 299, 523, 407));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(524, 340, 560, 342));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(467, 541, 467, 470, 408, 479));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(75, 466, 20, 466, 444, 479));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(242, 244, 298, 300, 381, 443));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(155, 375, 244, 380));

            mainFrame.pathFinder.clearMatrix(6);

            mainFrame.pathFinder.connectPos(0, 1);
            mainFrame.pathFinder.connectPos(0, 2);
            mainFrame.pathFinder.connectPos(2, 3);
            mainFrame.pathFinder.connectPos(3, 4);
            mainFrame.pathFinder.connectPos(4, 5);
        } else {
            // Kisten sind unten
            // Brett ist noch drauf
            if (!mainFrame.actions[517]) {
                mainFrame.pathWalker.vBorders.removeAllElements();
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(480, 299, 523, 407));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(524, 340, 560, 342));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(467, 541, 467, 470, 408, 479));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(233, 466, 220, 466, 444, 479));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(150, 245, 150, 300, 387, 443));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(120, 393, 149, 443));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(159, 370, 210, 386));

                mainFrame.pathFinder.clearMatrix(7);

                mainFrame.pathFinder.connectPos(0, 1);
                mainFrame.pathFinder.connectPos(0, 2);
                mainFrame.pathFinder.connectPos(2, 3);
                mainFrame.pathFinder.connectPos(3, 4);
                mainFrame.pathFinder.connectPos(4, 5);
                mainFrame.pathFinder.connectPos(4, 6);
            } else {
                // Brett ist weg
                mainFrame.pathWalker.vBorders.removeAllElements();
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(480, 299, 523, 407));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(524, 340, 560, 342));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(467, 541, 467, 470, 408, 479));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(233, 466, 220, 466, 444, 479));
                mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(201, 245, 220, 300, 387, 443));

                mainFrame.pathFinder.clearMatrix(5);

                mainFrame.pathFinder.connectPos(0, 1);
                mainFrame.pathFinder.connectPos(0, 2);
                mainFrame.pathFinder.connectPos(2, 3);
                mainFrame.pathFinder.connectPos(3, 4);
            }
        }
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

        // Hintergrund fuer Kiste und Brett loeschen (nur 1 Mal, dafuer richtig !)
        g.setClip(55, 343, 220, 137);
        g.drawImage(background, 0, 0);

        // Seil zeichnen, solange noch da
        if (!mainFrame.actions[518]) {
            g.setClip(574, 268, 29, 51);
            g.drawImage(background, 0, 0);
            g.drawImage(seil, 574, 268);
        }

        // Kiste bzw. Deska zeichnen
        // Deska zuerst, da im Hintergrund
        if (!mainFrame.actions[517]) {
            g.setClip(115, 367, 95, 56);
            g.drawImage(deska, 115, 367);
        } else {
            g.setClip(67, 358, 95, 56);
            g.drawImage(deska, 67, 358);
        }

        // Krabats neue Position hier bestimmen, damit beide Kistenzeichenroutinen
        // mit dem gleichen Punkt arbeiten...
        mainFrame.pathWalker.doWalk();

        // Kiste oben zeichnen, wenn keine Verschieberoutine und wenn Krabat davor ist
        if (!verschiebeKiste) {
            if (!mainFrame.actions[516] &&
                    !kistenRect.isPointInRect(mainFrame.krabat.getPos())) {
                // Kisten sind nicht verschoben und Krabat ist davor
                g.setClip(AnfangsPunkt.x, AnfangsPunkt.y, 178, 183);
                g.drawImage(kista, AnfangsPunkt.x, AnfangsPunkt.y);
                g.drawImage(kista2, AnfangsPunkt2.x, AnfangsPunkt2.y);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        if (SonderAnim != 0) {
            // Sonderanims ausfuehren
            if (SonderAnim == 1) {
                // Kisten schieben

                // Groesse
                int scale = mainFrame.krabat.defaultScale;
                scale += (int) (((float) mainFrame.krabat.maxX - (float) (festnagelPoint.y - 50)) / mainFrame.krabat.zoomFactor);

                // zum 1. Mal merken, wie gross Scale war
                if (Anfangsscale == 0) {
                    Anfangsscale = scale;
                }

                // Hoehe: nur offset
                int hoch = 100 - scale;

                // Breite
                int weit = 50 - scale / 2;

                // Punkt fuer LO-Evaluierung bereitstellen
                GenericPoint hier = new GenericPoint(festnagelPoint.x, festnagelPoint.y);

                hier.x -= weit / 2;
                hier.y -= hoch;

                hier.y += Math.abs((Anfangsscale - scale) / 2);

                // Cliprect setzen
                g.setClip(hier.x, hier.y, weit + 1, (hoch + 1) / 2);

                // GenericImage weiterschalten
                if (++AnimCounter > 4) {
                    AnimCounter = 0;
                    if (AnimPosition == 1) {
                        AnimPosition = 0;
                    } else {
                        AnimPosition = 1;
                    }
                }

                // Krabat zeichnen
                g.drawImage(krabat_schieb[AnimPosition], hier.x, hier.y, weit, hoch);

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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        GenericRectangle myx;
        myx = g.getClipBounds();

        // Kiste oben zeichnen, aber nur, wenn nicht gerade die Verschieberoutine aktiv ist
        // und nur, wenn Krabat dahinter ist
        if (!verschiebeKiste && kistenRect.isPointInRect(pKrTemp) &&
                !mainFrame.actions[516]) {
            // Kisten sind nicht verschoben
            g.setClip(AnfangsPunkt.x, AnfangsPunkt.y, 178, 183);
            g.drawImage(kista, AnfangsPunkt.x, AnfangsPunkt.y);
            g.drawImage(kista2, AnfangsPunkt2.x, AnfangsPunkt2.y);
        }

        // Hier Routine fuer Kiste verschieben aufrufen, Krabat ist immer dahinter
        if (verschiebeKiste) {
            g.setClip(50, 340, 220, 140);
            verschiebeKiste = moveIt(g);
        }

        // verschobene Kisten sind auch immer vor Krabat
        if (mainFrame.actions[516] && !verschiebeKiste) {
            // Kisten sind verschoben immer hier zeichnen
            g.setClip(EndPunkt.x, EndPunkt.y, 178, 183);
            g.drawImage(kista, EndPunkt.x, EndPunkt.y);
            g.drawImage(kista2, EndPunkt.x + AnfangsPunkt2.x - AnfangsPunkt.x, EndPunkt.y + AnfangsPunkt2.y - AnfangsPunkt.y);
        }

        g.setClip(myx.getX(), myx.getY(), myx.getWidth(), myx.getHeight());

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

                // Ausreden fuer Kiste, je nachdem, wo, hier oben
                if (kisty.isPointInRect(pTemp) && !mainFrame.actions[516]) {
                    nextActionID = 150;
                    pTemp = pKistyOben;
                }

                // Kiste unten
                if (kistyUnten.isPointInRect(pTemp) && mainFrame.actions[516]) {
                    nextActionID = 155;
                    pTemp = pKistyUnten;
                }

                // Seil Ausreden, falls noch da
                if (rectSeil.isPointInRect(pTemp) && !mainFrame.actions[518]) {
                    nextActionID = 160;
                    pTemp = pSeil;
                }

                // Brett Ausreden, falls noch da und Kiste verschoben
                if (nowaDeska.isPointInRect(pTemp) &&
                        !mainFrame.actions[517] && mainFrame.actions[516]) {
                    switch (mainFrame.whatItem) {
                        case 46: // hamor
                            nextActionID = 180;
                            break;
                        case 42: // hlebija
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 165;
                            break;
                    }
                    pTemp = pNowaDeska;
                }

                // deska alt Ausreden
                if (staraDeska.isPointInRect(pTemp)) {
                    nextActionID = 210;
                    pTemp = pStaraDeska;
                }

                // blido Ausreden
                if (blido.isPointInRect(pTemp)) {
                    nextActionID = 220;
                    pTemp = pBlido;
                }

                // korbik Ausreden
                if (korbik.isPointInRect(pTemp)) {
                    nextActionID = 230;
                    pTemp = pKorbik;
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

                // zu Cychi gehen ?
                if (ausgangCychi.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangCychi.isPointInRect(kt)) {
                        pTemp = pExitCychi;
                    } else {
                        pTemp = new GenericPoint(pExitCychi.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Gang gehen ?
                if (ausgangGang.isPointInRect(pTemp) && mainFrame.actions[517]) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangGang.isPointInRect(kt)) {
                        pTemp = pExitGang;
                    } else {
                        pTemp = new GenericPoint(pExitGang.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Seil ansehen, falls noch da
                if (rectSeil.isPointInRect(pTemp) && !mainFrame.actions[518]) {
                    nextActionID = 1;
                    pTemp = pSeil;
                }

                // Kiste unverschoben ansehen
                if (kisty.isPointInRect(pTemp) && !mainFrame.actions[516]) {
                    nextActionID = 3;
                    pTemp = pKistyOben;
                }

                // Kiste verschoben ansehen
                if (kistyUnten.isPointInRect(pTemp) && mainFrame.actions[516]) {
                    nextActionID = 8;
                    pTemp = pKistyUnten;
                }

                // Deska ansehen, wenn noch da, nur bei verschobener Kiste
                if (nowaDeska.isPointInRect(pTemp) &&
                        !mainFrame.actions[517] && mainFrame.actions[516]) {
                    nextActionID = 4;
                    pTemp = pNowaDeska;
                }

                // stara Deska ansehen
                if (staraDeska.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pStaraDeska;
                }

                // Blido ansehen
                if (blido.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pBlido;
                }

                // Korbik ansehen
                if (korbik.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pKorbik;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Seil mitnehmen
                if (rectSeil.isPointInRect(pTemp) && !mainFrame.actions[518]) {
                    nextActionID = 20;
                    mainFrame.pathWalker.setNewWay(pSeil);
                    mainFrame.repaint();
                    return;
                }

                // Kiste verschieben
                if (kisty.isPointInRect(pTemp) && !mainFrame.actions[516]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pKistyOben);
                    mainFrame.repaint();
                    return;
                }

                // verschobene Kiste nicht nochmal verschieben
                if (kistyUnten.isPointInRect(pTemp) && mainFrame.actions[516]) {
                    nextActionID = 80;
                    mainFrame.pathWalker.setNewWay(pKistyUnten);
                    mainFrame.repaint();
                    return;
                }

                // Brett mitnehmen, nur wenn da und sichtbar
                if (nowaDeska.isPointInRect(pTemp) &&
                        !mainFrame.actions[517] && mainFrame.actions[516]) {
                    nextActionID = 85;
                    mainFrame.pathWalker.setNewWay(pNowaDeska);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen, 2. Ausgang beachten, da nicht immer zu sehen !
                if (ausgangCychi.isPointInRect(pTemp) ||
                        ausgangGang.isPointInRect(pTemp) && mainFrame.actions[517]) {
                    return;
                }

                // stara Deska mitnehmen
                if (staraDeska.isPointInRect(pTemp)) {
                    nextActionID = 240;
                    mainFrame.pathWalker.setNewWay(pStaraDeska);
                    mainFrame.repaint();
                    return;
                }

                // blido mitnehmen
                if (blido.isPointInRect(pTemp)) {
                    nextActionID = 250;
                    mainFrame.pathWalker.setNewWay(pBlido);
                    mainFrame.repaint();
                    return;
                }

                // korbik mitnehmen
                if (korbik.isPointInRect(pTemp)) {
                    nextActionID = 260;
                    mainFrame.pathWalker.setNewWay(pKorbik);
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
                    rectSeil.isPointInRect(pTemp) && !mainFrame.actions[518] ||
                    kisty.isPointInRect(pTemp) && !mainFrame.actions[516] ||
                    kistyUnten.isPointInRect(pTemp) && mainFrame.actions[516] ||
                    nowaDeska.isPointInRect(pTemp) && !mainFrame.actions[517] ||
                    staraDeska.isPointInRect(pTemp) || blido.isPointInRect(pTemp) ||
                    korbik.isPointInRect(pTemp);

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
            // Fenster und Seil, falls noch da und den ganzen Rest
            if (rectSeil.isPointInRect(pTemp) && !mainFrame.actions[518] ||
                    kisty.isPointInRect(pTemp) && !mainFrame.actions[516] ||
                    kistyUnten.isPointInRect(pTemp) && mainFrame.actions[516] ||
                    nowaDeska.isPointInRect(pTemp) && !mainFrame.actions[517] ||
                    staraDeska.isPointInRect(pTemp) || blido.isPointInRect(pTemp) ||
                    korbik.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (ausgangCychi.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (ausgangGang.isPointInRect(pTemp) && mainFrame.actions[517]) {
                if (cursorShape != 6) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 6;
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

    // Verschiebe die Kiste ("Wo hat Kollege Kiste hingestellt ??")
    private boolean moveIt(GenericDrawingContext g) {
        // Position der Kisten veraendern
        xpos -= Xoffset;
        ypos -= Yoffset;

        // bisherigen Offset berechnen
        int xdiff = (int) (AnfangsPunkt.x - xpos);
        int ydiff = (int) (AnfangsPunkt.y - ypos);

        // System.out.println ("Offsets Feet : " + xdiff + " : " + ydiff);

        // eval. fuer Krabats Position berechnen
        festnagelPoint.x = bezugsPunkt.x - xdiff;
        festnagelPoint.y = bezugsPunkt.y - ydiff;

        // und auch setzen, fuer spaeter
        mainFrame.krabat.setPos(festnagelPoint);

        g.drawImage(kista, (int) xpos, (int) ypos);
        g.drawImage(kista2, (int) xpos + AnfangsPunkt2.x - AnfangsPunkt.x, (int) ypos + AnfangsPunkt2.y - AnfangsPunkt.y);

        return !(Math.abs((int) xpos - EndPunkt.x) < Xoffset);
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {
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
            switchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Seil ansehen
                krabatSays("Zachod_1", fSeil, 3, 0, 0);
                break;

            case 3:
                // Kiste oben ansehen
                krabatSays("Zachod_2", fKisteOben, 3, 0, 0);
                break;

            case 4:
                // neues Brett ansehen
                krabatSays("Zachod_3", fBrettNeu, 3, 0, 0);
                break;

            case 5:
                // stara Deska ansehen
                krabatSays("Zachod_4", fBrettAlt, 3, 0, 0);
                break;

            case 6:
                // Blido ansehen
                krabatSays("Zachod_5", fBlido, 3, 0, 0);
                break;

            case 7:
                // korbik ansehen
                krabatSays("Zachod_6", fKorbik, 3, 0, 0);
                break;

            case 8:
                // Kiste unten ansehen
                krabatSays("Zachod_7", fKisteUnten, 3, 0, 0);
                break;

            case 20:
                // Seil mitnehmen (wenn noch da)
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(fSeil);
                nextActionID = 21;
                // zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(38);
                mainFrame.krabat.nAnimation = 31;
                Counter = 5;
                break;

            case 21:
                // auf Ende Anim warten und Seil verschwinden lassen
                if (--Counter == 1) {
                    mainFrame.actions[518] = true;        // Flag setzen
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

            case 50:
                // Kiste verschieben, wenn Stollen gegessen, sonst Ausrede
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                festnagelPoint = new GenericPoint(mainFrame.krabat.getPos().x, mainFrame.krabat.getPos().y + 60);
                bezugsPunkt = new GenericPoint(festnagelPoint.x, festnagelPoint.y);
                SonderAnim = 1; // extra-Images fuer Kistenschieben
                if (mainFrame.actions[680])  // Kisten tatsaechlich verschieben
                {
                    mainFrame.actions[516] = true; // Kiste als verschoben markieren
                    verschiebeKiste = true;
                    nextActionID = 55;
                } else {
                    Counter = 20;
                    nextActionID = 65;
                }
                break;

            case 55:
                // Auf fertige Kiste warten
                if (!kistenSound) {
                    kistenSound = true;
                    mainFrame.soundPlayer.playFile("sfx-dd/kisty.wav");
                }
                if (!verschiebeKiste) {
                    nextActionID = 60;
                }
                break;

            case 60:
                // Anim beenden
                SonderAnim = 0;
                initMatrix();
                mainFrame.krabat.setPos(new GenericPoint(mainFrame.krabat.getPos().x, mainFrame.krabat.getPos().y - 40));
                mainFrame.krabat.setFacing(6);
                mainFrame.isClipSet = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 65:
                // erfolgloser Versuch, die Kisten zu verschieben
                if (--Counter > 1) {
                    break;
                }
                SonderAnim = 0;
                mainFrame.isClipSet = false;
                if (!mainFrame.actions[559] || !mainFrame.actions[681]) {
                    // Noch keinen 1. Teil Stollen gegessen oder noch nie Kisten verschoben
                    krabatSays("Zachod_8", fKisteOben, 3, 2, 70);
                    mainFrame.actions[681] = true;
                } else {
                    // Noch keinen 2. Teil Stollen gegessen
                    krabatSays("Zachod_9", fKisteOben, 3, 2, 70);
                }
                break;

            case 70:
                // Ende dieser Anim
                mainFrame.krabat.setFacing(6);
                mainFrame.isClipSet = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 80:
                // verschobene Kiste nochmal verschieben
                krabatSays("Zachod_10", fKisteUnten, 3, 0, 0);
                break;

            case 85:
                // Brett mitnehmen
                krabatSays("Zachod_11", fBrettNeu, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Cychi
                createNewLocation(150, locationID);
                break;

            case 101:
                // Gehe zu Gang (unterirdisch)
                createNewLocation(152, locationID);
                break;

            case 150:
                // Kiste - Ausreden
                thingExcuse(fKisteOben);
                break;

            case 155:
                // Kisteu - Ausreden
                thingExcuse(fKisteUnten);
                break;

            case 160:
                // Seil
                thingExcuse(fSeil);
                break;

            case 165:
                // Brett - Ausreden
                thingExcuse(fBrettNeu);
                break;

            case 180:
                // Brett entfernen ansagen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                krabatSays("Zachod_12", fBrettNeu, 3, 0, 185);
                break;

            case 185:
                // zu erstem Nagel laufen
                mainFrame.pathWalker.setNewWay(pEntnagel1);
                nextActionID = 187;
                break;

            case 187:
                // und raus damit
                mainFrame.krabat.nAnimation = 153;
                mainFrame.isInventoryCursor = false;
                nextActionID = 189;
                break;

            case 189:
                // und zum naechsten
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.pathWalker.setNewWay(pEntnagel2);
                nextActionID = 191;
                break;

            case 191:
                // und raus damit (Nr. 2)
                mainFrame.krabat.nAnimation = 153;
                nextActionID = 193;
                break;

            case 193:
                // und zum naechsten (Nr. 3)
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.pathWalker.setNewWay(pEntnagel3);
                nextActionID = 194;
                break;

            case 194:
                // und raus damit (Nr. 3)
                mainFrame.krabat.nAnimation = 153;
                nextActionID = 195;
                break;

            case 195:
                // und zum letzten (Nr. 4)
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.pathWalker.setNewWay(pEntnagel4);
                nextActionID = 196;
                break;

            case 196:
                // und raus damit (Nr. 4)
                mainFrame.krabat.nAnimation = 153;
                nextActionID = 197;
                break;

            case 197:
                // Brett reinschmeissen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.pathWalker.setNewWay(pNowaDeska);
                nextActionID = 198;
                break;

            case 198:
                // Brett entfernen
                mainFrame.actions[517] = true;
                initMatrix();
                mainFrame.krabat.setFacing(fBrettNeu);
                mainFrame.krabat.nAnimation = 92;
                mainFrame.soundPlayer.playFile("sfx-dd/deska.wav");
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                break;

            case 200:
                // Hlebija auf Deska
                // ZuffZahl 0...1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        krabatSays("Zachod_13", fBrettNeu, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Zachod_14", fBrettNeu, 3, 0, 0);
                        break;
                }
                break;

            case 210:
                // stara Deska - Ausreden
                thingExcuse(fBrettAlt);
                break;

            case 220:
                // blido - Ausreden
                thingExcuse(fBlido);
                break;

            case 230:
                // korbik - Ausreden
                thingExcuse(fKorbik);
                break;

            case 240:
                // stara Deska mitnehmen
                krabatSays("Zachod_15", fBrettAlt, 3, 0, 0);
                break;

            case 250:
                // Blido mitnehmen
                krabatSays("Zachod_16", fBlido, 3, 0, 0);
                break;

            case 260:
                // korbik mitnehmen
                krabatSays("Zachod_17", fKorbik, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}