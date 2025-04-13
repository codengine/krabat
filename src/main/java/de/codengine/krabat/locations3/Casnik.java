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
import de.codengine.krabat.anims.MainGuard;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Casnik extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Casnik.class);
    private GenericImage background;
    private GenericImage kozuch;
    private GenericImage umdrei;
    private GenericImage umvier;
    private GenericImage kluc;
    private GenericImage zeitumstell;
    private final GenericImage[] pendel;
    private final MainGuard hlStraznik;
    private final MultipleChoice Dialog;

    // Fuer Aktionen des Hl. Straznik
    private boolean trink = false;
    private boolean schlafein = false;

    // Anzeige, ob eine Anim fertig
    private boolean readyFlag = true;

    // Anzeige, ob Krabat gerade Uhr umstellt
    private boolean krabatStelltUhrUm = false;
    private int umstellCounter;

    private int Pendelpos = 1;
    private boolean pendelForward = true;
    private int Verhinderpendel;
    private static final int[] MAX_VERHINDERPENDEL = {15, 1, 15};

    private int AnimID = 10;
    private int AnimCounter = 0;
    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos = new GenericPoint(0, 0);
    private int AnimTalkPerson = 0;

    private boolean straznikSchnarcht = false;

    private boolean noSoundActive = false;
    private int SoundCountdown = 10;

    // Konstanten - Rects
    private static final BorderRect obererAusgang
            = new BorderRect(414, 158, 496, 293);
    private static final BorderRect untererAusgang
            = new BorderRect(50, 439, 300, 479);
    private static final BorderRect standuhr
            = new BorderRect(300, 145, 345, 315);
    private static final BorderRect morgenstern
            = new BorderRect(22, 310, 54, 360);
    private static final BorderRect rectKozuch
            = new BorderRect(345, 410, 500, 470);
    private static final BorderRect rectSchluessel
            = new BorderRect(125, 230, 143, 253);
    private static final BorderRect zeiger
            = new BorderRect(303, 146, 345, 187);
    private static final BorderRect statue
            = new BorderRect(168, 142, 200, 237);
    private static final BorderRect sonnenuhr
            = new BorderRect(139, 284, 150, 296);
    private static final BorderRect umsonnenuhr
            = new BorderRect(133, 278, 156, 302);

    // Konstante Points
    private static final GenericPoint pExitUp = new GenericPoint(450, 295);
    private static final GenericPoint pExitDown = new GenericPoint(190, 479);
    private static final GenericPoint pKozuch = new GenericPoint(355, 472);
    private static final GenericPoint pStraznik = new GenericPoint(205, 359);
    private static final GenericPoint pSchluessel = new GenericPoint(108, 375);
    private static final GenericPoint pMorgenstern = new GenericPoint(60, 380);
    private static final GenericPoint pStanduhr = new GenericPoint(321, 341);
    private static final GenericPoint pZeiger = new GenericPoint(326, 324);
    private static final GenericPoint pStatue = new GenericPoint(213, 295);
    private static final GenericPoint pSonnenuhr = new GenericPoint(144, 379);

    // Konstante ints
    private static final int fStraznik = 9;
    private static final int fKluc = 12;
    private static final int fTiger = 3;
    private static final int fMorgenstern = 9;
    private static final int fCasnik = 12;
    private static final int fZeiger = 12;
    private static final int fEngel = 9;
    private static final int fSonnenuhr = 12;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Casnik(Start caller, int oldLocation) {
        super(caller, 143);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 365;
        mainFrame.krabat.zoomFactor = 1.2f;
        mainFrame.krabat.defaultScale = -40;

        pendel = new GenericImage[3];

        hlStraznik = new MainGuard(mainFrame, true, mainFrame.actions[705]);
        //                                           ist casnik
        Dialog = new MultipleChoice(mainFrame);

        initLocation(oldLocation);

        Verhinderpendel = MAX_VERHINDERPENDEL[Pendelpos];

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // wenn kein Load, dann alle Versuche zuruecksetzen
        if (oldLocation != 0) {
            // Actions fuer Versuche zuruecksetzen
            for (int i = 615; i <= 620; i++) {
                mainFrame.actions[i] = false;
            }
        }

        // Grenzen setzen (abhaengig vom Tigerfell)
        if (!mainFrame.actions[600]) {
            initBorders();
        } else {
            initBordersWithoutTiger();
        }

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 146: // von Wonka aus
                mainFrame.krabat.setPos(new GenericPoint(190, 470));
                mainFrame.krabat.setFacing(12);
                break;
            case 144: // von Couch aus
                mainFrame.krabat.setPos(new GenericPoint(450, 300));
                mainFrame.krabat.setFacing(6);
                break;
        }

        // beim Init festlegen, ob geschnarcht wird oder nicht
        straznikSchnarcht = mainFrame.actions[705];
    }

    private void initBorders() {
        // Grenzen setzen, wenn Tigerfell liegt
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 355, 20, 355, 404, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(505, 620, 505, 620, 404, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 620, 20, 620, 364, 403));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(262, 455, 198, 550, 320, 363));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(415, 470, 415, 455, 300, 319));

        mainFrame.pathFinder.clearMatrix(5);

        mainFrame.pathFinder.connectPos(0, 2);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);
    }

    private void initBordersWithoutTiger() {
        // Grenzen setzen, wenn kein Tigerfell liegt
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(20, 620, 20, 620, 364, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(262, 455, 198, 550, 320, 363));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(415, 470, 415, 455, 300, 319));

        mainFrame.pathFinder.clearMatrix(3);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/casnik/casnik.png");
        kozuch = getPicture("gfx-dd/casnik/kozuch.png");
        umdrei = getPicture("gfx-dd/casnik/cas3.png");
        umvier = getPicture("gfx-dd/casnik/cas4.png");
        kluc = getPicture("gfx-dd/casnik/pkluc.png");
        zeitumstell = getPicture("gfx-dd/casnik/s-o-hlebija.png");

        pendel[0] = getPicture("gfx-dd/casnik/pendel2.png");
        pendel[1] = getPicture("gfx-dd/casnik/pendel1.png");
        pendel[2] = getPicture("gfx-dd/casnik/pendel3.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // wenn noch ie dringewesen, dann Sound von Uhr "um drei"
        if (!mainFrame.actions[608]) {
            mainFrame.actions[608] = true;
            mainFrame.soundPlayer.playFile("sfx-dd/drei.wav");
        }

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

        // liegt Tigerfell noch da ?
        if (!mainFrame.actions[600]) {
            g.setClip(355, 412, 145, 75);
            g.drawImage(background, 0, 0);
            g.drawImage(kozuch, 355, 412);
        }

        // Uhr richtig zeichnen
        g.setClip(311, 154, 25, 24);
        g.drawImage(background, 0, 0);
        g.drawImage(!mainFrame.actions[606] ? umdrei : umvier, 311, 154);

        // Hier das Uhrpendel rein
        g.setClip(309, 190, 32, 59);
        g.drawImage(pendel[Pendelpos], 309, 190);
        if (--Verhinderpendel < 1) {
            if (pendelForward) {
                evalSound(true); // tick
                Pendelpos++;
                if (Pendelpos == 3) {
                    Pendelpos = 1;
                    pendelForward = false;
                }
            } else {
                evalSound(false); // tack
                Pendelpos--;
                if (Pendelpos == -1) {
                    Pendelpos = 1;
                    pendelForward = true;
                }
            }
            Verhinderpendel = MAX_VERHINDERPENDEL[Pendelpos];
        }

        // Hl. Straznik Hintergrund loeschen (wg. Schluessel)
        BorderRect temp = hlStraznik.straznikRect(talkPerson);
        // Hintergrund loeschen
        g.setClip(temp.topLeftPoint.x, temp.topLeftPoint.y, temp.bottomRightPoint.x - temp.topLeftPoint.x,
                temp.bottomRightPoint.y - temp.topLeftPoint.y);
        g.drawImage(background, 0, 0);


        // Schluessel zeichnen, wenn noch da
        if (!mainFrame.actions[951]) {
            g.setClip(129, 235, 6, 16);
            g.drawImage(background, 0, 0);
            g.drawImage(kluc, 129, 235);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Straznik weiterbewegen
        readyFlag = hlStraznik.evalStraznik(talkPerson, trink, schlafein, mainFrame.actions[706]);
        // Cliprect nun setzen
        temp = hlStraznik.straznikRect(talkPerson);
        g.setClip(temp.topLeftPoint.x, temp.topLeftPoint.y, temp.bottomRightPoint.x - temp.topLeftPoint.x,
                temp.bottomRightPoint.y - temp.topLeftPoint.y);
        // Hl. Straznik zeichnen
        hlStraznik.drawStraznik(g, talkPerson);
        if (trink) {
            trink = false;
        }
        if (schlafein) {
            schlafein = false;
        }

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.doWalk();

        // Krabat zeichnen

        if (krabatStelltUhrUm) {
            // Uhr umstellen, erstmal Pos und Groesse berechnen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.getPos().x, mainFrame.krabat.getPos().y);

            // Groesse
            int scale = mainFrame.krabat.defaultScale;
            scale += (int) (((float) mainFrame.krabat.maxX - (float) hier.y) / mainFrame.krabat.zoomFactor);

            // System.out.println ("Scale ist " + scale + " gross.");

            // Umrechnen des Scalings auf dieses Image
            float scaleUmrechnung = 163f / 100f;
            scale = (int) ((float) scale * scaleUmrechnung);

            // Hoehe: nur offset
            int hoch = 163 - scale;

            // Breite abhaengig von Hoehe...
            float Scaleverhaeltnis = 163f / 50f;
            int weit = (int) (50 - (float) scale / Scaleverhaeltnis);

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            // Krabat zeichnen
            g.drawImage(zeitumstell, hier.x, hier.y, weit, hoch);
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

        // Ausgabe von Animoutputtext
        if (!Objects.equals(AnimOutputText, "")) {
            // Textausgabe
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, COLORS[AnimTalkPerson]);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Anims bedienen
        if (straznikSchnarcht) {
            doAnims();
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
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

                // Ausreden fuer Kozuch, wenn noch da
                if (rectKozuch.isPointInRect(pTemp) && !mainFrame.actions[600]) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
                    pTemp = pKozuch;
                }

                // Ausreden fuer HlStraznik
                // wenn drin und:
                // Sonnenuhr weg ODER
                // nicht in der Naehe Sonnenuhr und schon schlafend ODER
                // noch stehend
                if (hlStraznik.straznikRect(talkPerson).isPointInRect(pTemp) &&
                        (mainFrame.actions[706] ||
                                !umsonnenuhr.isPointInRect(pTemp) && mainFrame.actions[705] ||
                                !mainFrame.actions[705])) {
                    // Wein geben, wenn es geht
                    if (mainFrame.whatItem == 44 && mainFrame.actions[606]) {
                        nextActionID = 205;
                    } else {
                        // Wein geben, wenn zu zeitig
                        // Extra - Sinnloszeug fuer andere Sachen
                        nextActionID = mainFrame.whatItem == 44 ? 185 : 155;
                    }
                    pTemp = pStraznik;
                }

                // Ausreden fuer Schluessel
                if (rectSchluessel.isPointInRect(pTemp) && !mainFrame.actions[951]) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTemp = pSchluessel;
                }

                // Ausreden fuer Morgenstern
                if (morgenstern.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = pMorgenstern;
                }

                // Ausreden fuer Standuhr oder Vorstellen !!
                if (zeiger.isPointInRect(pTemp)) {
                    if (mainFrame.whatItem == 42) {
                        // Uhr vorstellen und weiter
                        nextActionID = 170;
                    } else {
                        // Extra - Sinnloszeug
                        nextActionID = 175;
                    }
                    pTemp = pZeiger;
                }

                // Ausreden fuer Uhr
                if (standuhr.isPointInRect(pTemp) && !zeiger.isPointInRect(pTemp)) {
                    nextActionID = 190;
                    pTemp = pStanduhr;
                }

                // Ausreden fuer Postawa
                if (statue.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 200;
                    pTemp = pStatue;
                }

                // Ausreden fuer Sonnenuhr
                // wenn drin, noch nicht weg aber schon da
                if (sonnenuhr.isPointInRect(pTemp) && !mainFrame.actions[706] &&
                        mainFrame.actions[705]) {
                    // Extra - Sinnloszeug
                    nextActionID = 250;
                    pTemp = pSonnenuhr;
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

                // zu Wonka gehen ?
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTemp = pExitDown;
                    } else {
                        pTemp = new GenericPoint(pExitDown.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Couch gehen ?
                if (obererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.isPointInRect(kt)) {
                        pTemp = pExitUp;
                    } else {
                        pTemp = new GenericPoint(pExitUp.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // HlStraznik ansehen
                if (hlStraznik.straznikRect(talkPerson).isPointInRect(pTemp) &&
                        (mainFrame.actions[706] ||
                                !umsonnenuhr.isPointInRect(pTemp) && mainFrame.actions[705] ||
                                !mainFrame.actions[705])) {
                    nextActionID = 1;
                    pTemp = pStraznik;
                }

                // Schluessel ansehen
                if (rectSchluessel.isPointInRect(pTemp) && !mainFrame.actions[951]) {
                    nextActionID = 2;
                    pTemp = pSchluessel;
                }

                // Tigerfell ansehen
                if (!mainFrame.actions[600] && rectKozuch.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pKozuch;
                }

                // Morgenstern ansehen
                if (morgenstern.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pMorgenstern;
                }

                // Standuhr ansehen
                if (standuhr.isPointInRect(pTemp) && !zeiger.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pStanduhr;
                }

                // Zeiger ansehen
                if (zeiger.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pZeiger;
                }

                // Postawa ansehen
                if (statue.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pStatue;
                }

                // Sonnenuhr ansehen
                if (sonnenuhr.isPointInRect(pTemp) && !mainFrame.actions[706] &&
                        mainFrame.actions[705]) {
                    nextActionID = 8;
                    pTemp = pSonnenuhr;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Hl.Straznik reden
                if (hlStraznik.straznikRect(talkPerson).isPointInRect(pTemp) &&
                        (mainFrame.actions[706] ||
                                !umsonnenuhr.isPointInRect(pTemp) && mainFrame.actions[705] ||
                                !mainFrame.actions[705])) {
                    if (mainFrame.actions[605]) {
                        // er ist schon eingeschlafen
                        nextActionID = 70;
                    } else {
                        // er kann noch reden
                        nextActionID = 50;
                    }
                    mainFrame.pathWalker.setNewWay(pStraznik);
                    mainFrame.repaint();
                    return;
                }

                // Kozuch mitnehmen
                if (rectKozuch.isPointInRect(pTemp) &&
                        !mainFrame.actions[600]) {
                    nextActionID = 40;
                    mainFrame.pathWalker.setNewWay(pKozuch);
                    mainFrame.repaint();
                    return;
                }

                // Schluessel mitnehmen
                if (rectSchluessel.isPointInRect(pTemp) && !mainFrame.actions[951]) {
                    nextActionID = 45;
                    mainFrame.pathWalker.setNewWay(pSchluessel);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (obererAusgang.isPointInRect(pTemp) ||
                        untererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Morgenstern mitnehmen
                if (morgenstern.isPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(pMorgenstern);
                    mainFrame.repaint();
                    return;
                }

                // Standuhr mitnehmen
                if (standuhr.isPointInRect(pTemp) && !zeiger.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(pStanduhr);
                    mainFrame.repaint();
                    return;
                }

                // Zeiger mitnehmen
                if (zeiger.isPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.pathWalker.setNewWay(pZeiger);
                    mainFrame.repaint();
                    return;
                }

                // Statua mitnehmen
                if (statue.isPointInRect(pTemp)) {
                    nextActionID = 80;
                    mainFrame.pathWalker.setNewWay(pStatue);
                    mainFrame.repaint();
                    return;
                }

                // Sonnenuhr mitnehmen
                if (sonnenuhr.isPointInRect(pTemp) && !mainFrame.actions[706] &&
                        mainFrame.actions[705]) {
                    nextActionID = 90;
                    mainFrame.pathWalker.setNewWay(pSonnenuhr);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.stopWalking();
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                resetAnims();
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
                    rectKozuch.isPointInRect(pTemp) && !mainFrame.actions[600] ||
                    sonnenuhr.isPointInRect(pTemp) && !mainFrame.actions[706] &&
                            mainFrame.actions[705] ||
                    hlStraznik.straznikRect(talkPerson).isPointInRect(pTemp) &&
                            (mainFrame.actions[706] ||
                                    !umsonnenuhr.isPointInRect(pTemp) && mainFrame.actions[705] ||
                                    !mainFrame.actions[705]) ||
                    rectSchluessel.isPointInRect(pTemp) && !mainFrame.actions[951] ||
                    morgenstern.isPointInRect(pTemp) || standuhr.isPointInRect(pTemp) ||
                    statue.isPointInRect(pTemp);

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
            if (rectKozuch.isPointInRect(pTemp) && !mainFrame.actions[600] ||
                    hlStraznik.straznikRect(talkPerson).isPointInRect(pTemp) &&
                            (mainFrame.actions[706] ||
                                    !umsonnenuhr.isPointInRect(pTemp) && mainFrame.actions[705] ||
                                    !mainFrame.actions[705]) ||
                    sonnenuhr.isPointInRect(pTemp) && !mainFrame.actions[706] &&
                            mainFrame.actions[705] ||
                    rectSchluessel.isPointInRect(pTemp) && !mainFrame.actions[951] ||
                    morgenstern.isPointInRect(pTemp) || standuhr.isPointInRect(pTemp) ||
                    statue.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (obererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
                }
                return;
            }

            if (untererAusgang.isPointInRect(pTemp)) {
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
        resetAnims();
    }

    private void evalSound(boolean tick) {
        // zufaellig wavs fuer Geschnatter abspielen...
        // wenn gefordert, dann auch nicht spielen
        if (noSoundActive) {
            return;
        }

        if (--SoundCountdown > 1) {
            return; // wenn noetig, dann bisschen warten, ehe was gespielt wird
        }

        // nur kurz vor Ausschlagende abspielen
        if (Pendelpos == 2 && tick) {
            mainFrame.soundPlayer.playFile("sfx-dd/tick.wav");
        }
        if (Pendelpos == 0 && !tick) {
            mainFrame.soundPlayer.playFile("sfx-dd/tack.wav");
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
                // Hlowny straznik anschauen
                // Testen, ob er schon schlaeft
                if (!mainFrame.actions[605]) {
                    krabatSays("Casnik_1", fStraznik, 3, 0, 0);
                } else {
                    krabatSays("Casnik_2", fStraznik, 3, 0, 0);
                }
                break;

            case 2:
                // Schluessel anschauen
                // Test, ob Krabat schon weiss, wozu der Schluessel ist
                if (!mainFrame.actions[602]) {
                    krabatSays("Casnik_3", fKluc, 3, 0, 0);
                } else {
                    krabatSays("Casnik_4", fKluc, 3, 0, 0);
                }
                break;

            case 3:
                // Tigerfell anschauen
                // zuffi 0...1
                int zffZahl = (int) (Math.random() * 1.9);
                switch (zffZahl) {
                    case 0:
                        krabatSays("Casnik_5", fTiger, 3, 0, 0);
                        break;

                    case 1:
                        krabatSays("Casnik_6", fTiger, 3, 0, 0);
                        break;
                }
                break;

            case 4:
                // Morgenstern anschauen
                krabatSays("Casnik_7", fMorgenstern, 3, 0, 0);
                break;

            case 5:
                // Standuhr anschauen
                krabatSays("Casnik_8", fCasnik, 3, 0, 0);
                break;

            case 6:
                // Zeiger anschauen
                // Entscheidung, ob schon vorgestellt
                if (!mainFrame.actions[606]) {
                    krabatSays("Casnik_9", fZeiger, 3, 0, 0);
                } else {
                    krabatSays("Casnik_10", fZeiger, 3, 0, 0);
                }
                break;

            case 7:
                // Engel anschauen
                krabatSays("Casnik_11", fEngel, 3, 0, 0);
                break;

            case 8:
                // Sonnenuhr anschauen
                krabatSays("Casnik_12", fSonnenuhr, 3, 0, 0);
                break;

            case 40:
                // Tigerfell mitnehmen und Bewegungsgrenzen neu setzen
                mainFrame.isAnimRunning = true;
                initBordersWithoutTiger();
                mainFrame.krabat.setFacing(fTiger);
                mainFrame.krabat.nAnimation = 32;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 43;
                Counter = 5;
                break;

            case 43:
                // Ende take Kozuch
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(36);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[600] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 45:
                // Schluessel mitnehmen
                if (mainFrame.actions[605]) {
                    // darf mitnehmen (Wache hat Wein bekommen -> besoffen)
                    mainFrame.isAnimRunning = true;
                    mainFrame.krabat.setFacing(fKluc);
                    mainFrame.krabat.nAnimation = 120;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    nextActionID = 48;
                    Counter = 5;
                } else {
                    // 4 mal darf Kr. versuchen wegzunehmen
                    if (!mainFrame.actions[615] || !mainFrame.actions[616] || !mainFrame.actions[617]) {
                        mainFrame.isAnimRunning = true;
                        evalMouseMoveEvent(mainFrame.mousePoint);
                        nextActionID = 301;
                        if (mainFrame.actions[616]) {
                            mainFrame.actions[617] = true;
                        }
                        if (mainFrame.actions[615]) {
                            mainFrame.actions[616] = true;
                        }
                        if (!mainFrame.actions[615]) {
                            mainFrame.actions[615] = true;
                        }
                    } else {
                        // zur Strafe in die Kueche zurueck
                        createNewLocation(120, locationID);
                    }
                }
                break;

            case 48:
                // Ende take Schluessel
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(47);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[951] = true;
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
                // Krabat beginnt MC (Straznik benutzen)
                mainFrame.krabat.setFacing(9);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.actions[601]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 55:
                // Morgenstern mitnehmen
                krabatSays("Casnik_13", fMorgenstern, 3, 0, 0);
                break;

            case 60:
                // Uhr mitnehmen
                krabatSays("Casnik_14", fCasnik, 3, 0, 0);
                break;

            case 70:
                // Straznik benutzen, wenn schon eingeschlafen
                krabatSays("Casnik_15", fStraznik, 3, 0, 0);
                break;

            case 75:
                // Zeiger mitnehmen
                krabatSays("Casnik_16", fZeiger, 3, 0, 0);
                break;

            case 80:
                // Postawa mitnehmen
                krabatSays("Casnik_17", fEngel, 3, 0, 0);
                break;

            case 90:
                // Sonnenuhr mitnehmen
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.setFacing(fSonnenuhr);
                mainFrame.krabat.nAnimation = 121;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 93;
                Counter = 5;
                break;

            case 93:
                // Ende take Sonnenuhr
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(45);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[706] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Wonka
                createNewLocation(146, locationID);
                break;

            case 101:
                // Gehe zu Chodba
                if (mainFrame.actions[605]) {
                    // darf zur couch gehen (Wache hat Wein bekommen)
                    createNewLocation(144, locationID);
                } else {
                    // 4 mal darf Kr. versuchen rauszugehen
                    if (!mainFrame.actions[618] || !mainFrame.actions[619] || !mainFrame.actions[620]) {
                        mainFrame.isAnimRunning = true;
                        evalMouseMoveEvent(mainFrame.mousePoint);
                        nextActionID = 300;
                        if (mainFrame.actions[619]) {
                            mainFrame.actions[620] = true;
                        }
                        if (mainFrame.actions[618]) {
                            mainFrame.actions[619] = true;
                        }
                        if (!mainFrame.actions[618]) {
                            mainFrame.actions[618] = true;
                        }
                    } else {
                        // zur Strafe in die Kueche zurueck
                        createNewLocation(120, locationID);
                    }
                }
                break;

            case 150:
                // Kozuch - Ausreden
                thingExcuse(fTiger);
                break;

            case 155:
                // Straznik - Ausreden
                maleExcuse(fStraznik);
                break;

            case 160:
                // Schluessel - Ausreden
                thingExcuse(fKluc);
                break;

            case 165:
                // Morgenstern - Ausreden
                thingExcuse(fMorgenstern);
                break;

            case 170:
                // Uhr vorstellen
                if (!mainFrame.actions[606]) {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    SoundCountdown = 30;
                    mainFrame.isInventoryCursor = false;
                    mainFrame.krabat.setFacing(fZeiger);
                    nextActionID = 171;
                    krabatStelltUhrUm = true;
                    umstellCounter = 7;
                } else {
                    krabatSays("Casnik_18", fZeiger, 3, 0, 0);
                }
                break;

            case 171:
                // warten auf Ende Zeigerumstell und schluss damit
                if (--umstellCounter > 1) {
                    break;
                }
                mainFrame.actions[606] = true;
                mainFrame.isClipSet = false;
                krabatStelltUhrUm = false;
                mainFrame.soundPlayer.playFile("sfx-dd/vier.wav");
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;


            case 175:
                // Uhr - Ausreden
                thingExcuse(fCasnik);
                break;

            case 185:
                // Reaktion Hl.Straznik, wenn Wein zu frueh angeboten
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.nAnimation = 142;
                personSays("Casnik_19", fStraznik, 48, 2, 186, hlStraznik.evalTalkPoint());
                break;

            case 186:
                // Reaktion Hl.Straznik auf zu frueh verabreichten Wein
                personSays("Casnik_20", 0, 48, 2, 187, hlStraznik.evalTalkPoint());
                mainFrame.krabat.stopAnim();
                break;

            case 187:
                // Ende dieser Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                nextActionID = 0;
                break;

            case 190:
                // Zeiger - Ausreden
                thingExcuse(fZeiger);
                break;

            case 200:
                // statue - Ausreden
                thingExcuse(fEngel);
                break;


            case 205:
                // Gib Wein an Straznik
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                noSoundActive = true;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 142;
                personSays("Casnik_21", fStraznik, 48, 2, 220, hlStraznik.evalTalkPoint());
                mainFrame.actions[605] = true;
                mainFrame.actions[705] = true;
                break;

            case 220:
                // Straznik trinkt wein
                trink = true;
                Counter = 5;
                nextActionID = 225;
                break;

            case 225:
                // warten aufs Ende von K, dann Sound
                if (--Counter > 0) {
                    break;
                }
                mainFrame.krabat.stopAnim();
                mainFrame.soundPlayer.playFile("sfx-dd/pic.wav");
                mainFrame.inventory.vInventory.removeElement(44); // Wein rausnehmen
                nextActionID = 230;
                break;

            case 230:
                // Reaktion Hl.Straznik auf verabreichten Wein
                if (readyFlag) {
                    break;
                }
                personSays("Casnik_22", 0, 48, 2, 235, hlStraznik.evalTalkPoint());
                break;

            case 235:
                // Straznik schlaeft ein
                schlafein = true;
                nextActionID = 240;
                break;

            case 240:
                // warten auf Ende Einschlafen
                if (!readyFlag) {
                    nextActionID = 245;
                }
                break;

            case 245:
                // Ende Weintrinkanim  
                straznikSchnarcht = true;
                noSoundActive = false;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 250:
                // Ausreden fuer Sonnenuhr
                thingExcuse(fSonnenuhr);
                break;

            // Sequenzen mit Hauptwaechter  /////////////////////////////////

            case 300:
                // Versuch ins Arbeitszimmer zu gehen
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                int zuffZahl = (int) (Math.random() * 2.9);
                switch (zuffZahl) {
                    case 0:
                        outputText = Start.STRING_MANAGER.getTranslation("Casnik_36");
                        break;

                    case 1:
                        outputText = Start.STRING_MANAGER.getTranslation("Casnik_37");
                        break;

                    case 2:
                        outputText = Start.STRING_MANAGER.getTranslation("Casnik_38");
                        break;
                }

                outputTextPos = mainFrame.imageFont.centerText(outputText, hlStraznik.evalTalkPoint());
                talkPerson = 48;
                talkPause = 2;
                nextActionID = 800;
                break;

            case 301:
                // Versuch Schluessel wegzunehmen
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        outputText = Start.STRING_MANAGER.getTranslation("Casnik_39");
                        break;

                    case 1:
                        outputText = Start.STRING_MANAGER.getTranslation("Casnik_40");
                        break;

                    case 2:
                        outputText = Start.STRING_MANAGER.getTranslation("Casnik_41");
                        break;
                }

                outputTextPos = mainFrame.imageFont.centerText(outputText, hlStraznik.evalTalkPoint());
                talkPerson = 48;
                talkPause = 2;
                nextActionID = 800;
                break;


            // Dialog mit Kuchar ////////////////////////////////////////////

            case 600:
                // Multiple - Choice - Routine
                Dialog.initMC(20);

                // 1. Frage
                Dialog.extend("Casnik_29", 1000, 602, new int[]{602}, 610);
                Dialog.extend("Casnik_30", 602, 1000, null, 611);

                // 2. Frage
                Dialog.extend("Casnik_31", 1000, 604, new int[]{604}, 620);
                Dialog.extend("Casnik_32", 604, 603, new int[]{603}, 621);
                Dialog.extend("Casnik_33", 603, 1000, null, 622);

                // 3. Frage (607)
                Dialog.extend("Casnik_34", 1000, 607, new int[]{607}, 800);
                Dialog.extend("Casnik_35", 607, 1000, null, 800);

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
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                break;

            // Sequenz vor MC (erstes Anreden)
            case 608:
                // Reaktion Hl.Straznik
                personSays("Casnik_23", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                mainFrame.actions[601] = true; // Flag setzen, Diese Zeile nicht wiederholen
                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Hl.Straznik
                personSays("Casnik_24", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 611:
                // Reaktion Hl.Straznik
                personSays("Casnik_25", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Hl.Straznik
                personSays("Casnik_26", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 621:
                // Reaktion Hl.Straznik
                personSays("Casnik_27", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 622:
                // Reaktion Hl.Straznik
                personSays("Casnik_28", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[607] = false;
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    // Schnarchanim des Hl. Straznik ausfuehren
    private void doAnims() {
        switch (AnimID) {

            case 10:
                // Warteschleife, damit Menu problemlos...
                AnimCounter = 10;
                AnimID = 20;
                break;

            case 20:
                // warten...
                if (--AnimCounter < 1) {
                    AnimID = 30;
                }
                break;

            case 30:
                // Text ueber Hl. Straznik ausgeben
                AnimOutputText = Start.STRING_MANAGER.getTranslation("Casnik_42");
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, hlStraznik.evalTalkPoint());
                AnimCounter = 50;
                AnimTalkPerson = 48;
                AnimID = 40;
                break;

            case 40:
                // warten, bis zu Ende geschnarcht
                if (--AnimCounter < 1) {
                    AnimID = 50;
                }
                break;

            case 50:
                // variable Pause dazwischen
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimCounter = (int) (Math.random() * 70 + 50);
                AnimID = 60;
                break;

            case 60:
                // Pause abwarten und von vorn...
                if (--AnimCounter < 1) {
                    AnimID = 10;
                }
                break;
        }
    }

    // Anims zuruecksetzen, damit leerer Screen bei Menu usw...
    private void resetAnims() {
        AnimOutputText = "";
        AnimCounter = 10;
        AnimID = 10;
    }

}


/* TEXTE

   W s#lu#zbje njepiju - hiks - N#etko je hakle we t#roch - tamle ma#s #kasnik. Za hod#dinu mam swjatok. Hdy#z #kasnik to pokaza, m#o#zemoj jedyn pi#c !

   Ow, je hi#zo w #styrjoch... Tak m#o#zu sej jedyn pop#re#c... Oj, sylny tobak...
   Sym naraz n#ekak mu#kny... brrrrrr, hrrrrrr...

*/