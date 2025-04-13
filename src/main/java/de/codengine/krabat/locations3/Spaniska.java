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
import de.codengine.krabat.anims.Dziwadzelnica;
import de.codengine.krabat.anims.KrabatFalling;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Spaniska extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Spaniska.class);
    private GenericImage background;
    private GenericImage stolc;
    private GenericImage roze;
    private GenericImage haken;
    private GenericImage pjenjezy;
    private GenericImage haken2;
    private GenericImage krabat_steigen;
    private final Dziwadzelnica dziwadzelnica;
    private final GenericPoint talkPoint;
    private final BorderRect rectDziwadzelnica;
    private final BorderRect rectLeftDziwadzelnica;
    private final BorderRect rectLookDziwadzelnica;
    private final GenericPoint dziwPoint;
    private final GenericPoint dziwLeftPoint;

    private int FadeToBlack = 0;

    private boolean schlaegtZu = false;
    private boolean krabatFaellt = false;
    private boolean fallRueckgabe = true;
    private boolean beatRueckgabe = true;
    private boolean dziwSagtNurKurzeSaetze = false; // wenn Hakenanim, dann keinen Monstersatz von sich geben -> lesbar in kurzer Zeit

    private final KrabatFalling krabatFalling;

    private boolean setAnim = false;
    private int setAnimID = 0;

    // Variablen fuer Backgroundanim
    private int AnimID = 10;
    private int AnimCounter = 0;
    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos = new GenericPoint(0, 0);
    private int AnimTalkPerson = 0;

    private boolean dziwSchreit = false;

    private boolean ersterHakenDa = true;
    private boolean zweiterHakenDa = true;

    private boolean krabatVisible = true;

    private boolean klettertRein = false;

    // Konstanten - Rects
    private static final BorderRect untererAusgang
            = new BorderRect(275, 445, 500, 479);
    private static final BorderRect obererAusgang
            = new BorderRect(456, 126, 554, 287);
    private static final BorderRect papier
            = new BorderRect(67, 352, 115, 378);
    private static final BorderRect spiegel
            = new BorderRect(35, 146, 110, 306);
    private static final BorderRect blumen
            = new BorderRect(558, 317, 639, 440);
    private static final BorderRect faltWand
            = new BorderRect(235, 167, 413, 373);
    private static final BorderRect rectHaken
            = new BorderRect(460, 288, 524, 333);
    private static final BorderRect geld
            = new BorderRect(144, 355, 171, 374);

    // Konstante Points
    private static final GenericPoint pExitDown = new GenericPoint(415, 479);
    private static final GenericPoint pExitUp = new GenericPoint(454, 369);
    private static final GenericPoint pHaken = new GenericPoint(455, 370);
    private static final GenericPoint pDziwadzelnica = new GenericPoint(323, 450);
    private static final GenericPoint pPapier = new GenericPoint(192, 470);
    private static final GenericPoint pGeld = new GenericPoint(192, 470);
    private static final GenericPoint pBlumen = new GenericPoint(620, 430);
    private static final GenericPoint pSpiegel = new GenericPoint(195, 470);
    private static final GenericPoint talkPointCenter = new GenericPoint(320, 220);
    private static final GenericPoint dziwFeetLeft = new GenericPoint(255, 460);
    private static final GenericPoint dziwFeetRight = new GenericPoint(292, 460);
    private static final GenericPoint pKletterFeet = new GenericPoint(494, 335);
    private static final GenericPoint pFaltWand = new GenericPoint(455, 373);

    // Konstante ints
    private static final int fDziw = 9;
    private static final int fKotwica = 12;
    private static final int fSpiegel = 9;
    private static final int fBlumen = 6;
    private static final int fToler = 9;
    private static final int fPapier = 9;
    private static final int fFaltWand = 9;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Spaniska(Start caller, int oldLocation) {
        super(caller, 122);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomFactor = 1f;
        mainFrame.krabat.defaultScale = -90;

        dziwadzelnica = new Dziwadzelnica(mainFrame);

        dziwPoint = new GenericPoint(dziwFeetRight.x - Dziwadzelnica.Breite / 2, dziwFeetRight.y - Dziwadzelnica.Hoehe);
        dziwLeftPoint = new GenericPoint(dziwFeetLeft.x - Dziwadzelnica.Breite / 2, dziwFeetLeft.y - Dziwadzelnica.Hoehe);
        talkPoint = new GenericPoint(dziwFeetLeft.x, dziwLeftPoint.y - 50);

        rectDziwadzelnica = new BorderRect(dziwPoint.x, dziwPoint.y, dziwPoint.x + Dziwadzelnica.Breite,
                dziwPoint.y + Dziwadzelnica.Hoehe);
        rectLeftDziwadzelnica = new BorderRect(dziwLeftPoint.x, dziwLeftPoint.y, dziwLeftPoint.x + Dziwadzelnica.Breite,
                dziwLeftPoint.y + Dziwadzelnica.Hoehe);
        rectLookDziwadzelnica = new BorderRect(dziwLeftPoint.x, dziwLeftPoint.y, 294, /* Achtung, wird nicht berechnet!!*/ dziwPoint.y + Dziwadzelnica.Hoehe);


        krabatFalling = new KrabatFalling(mainFrame, -90);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(325, 490, 318, 490, 430, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(245, 317, 185, 317, 465, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(450, 455, 435, 600, 370, 429));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(590, 430, 620, 432));

        mainFrame.pathFinder.clearMatrix(4);

        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(0, 2);
        mainFrame.pathFinder.connectPos(2, 3);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                if (mainFrame.actions[514]) {
                    BackgroundMusicPlayer.getInstance().playTrack(16, true);
                }
                break;
            case 121: // von Hintergasse
                if (mainFrame.actions[514]) {
                    BackgroundMusicPlayer.getInstance().playTrack(16, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(480, 400));
                mainFrame.krabat.setFacing(6);
                setAnim = true;
                if (!mainFrame.actions[519]) {
                    ersterHakenDa = false;
                    zweiterHakenDa = false;
                    krabatVisible = false;
                    dziwSagtNurKurzeSaetze = true;
                    setAnimID = 1000;
                } else {
                    setAnimID = 2000;
                    klettertRein = true;
                }
                break;
            case 123: // von Hala 
                mainFrame.krabat.setPos(new GenericPoint(400, 470));
                mainFrame.krabat.setFacing(12);
                break;
        }

        // wenn Dziwadzelnica noch da, dann die Backgroundanims einschalten...
        if (!mainFrame.actions[514]) {
            dziwSchreit = true;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/spaniska/spaniska.png");
        stolc = getPicture("gfx-dd/spaniska/stolc.png");
        roze = getPicture("gfx-dd/spaniska/roze.png");
        haken = getPicture("gfx-dd/spaniska/haken.png");
        pjenjezy = getPicture("gfx-dd/spaniska/pjenjezy.png");
        haken2 = getPicture("gfx-dd/spaniska/haken2.png");

        krabat_steigen = getPicture("gfx-dd/spaniska/k-u-steigen.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            cursorShape = 200;
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);
        if (ersterHakenDa) {
            if (zweiterHakenDa) {
                g.drawImage(haken, 460, 288);
            } else {
                g.drawImage(haken2, 452, 278);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // ist Schauspielerin da ?
        if (!mainFrame.actions[514]) {
            // Dziwadzelnica zeichnen
            if (!schlaegtZu) {
                // Cliprect fuer nach rechts schauen
                g.setClip(rectDziwadzelnica.topLeftPoint.x, rectDziwadzelnica.topLeftPoint.y,
                        rectDziwadzelnica.bottomRightPoint.x - rectDziwadzelnica.topLeftPoint.x,
                        rectDziwadzelnica.bottomRightPoint.y - rectDziwadzelnica.topLeftPoint.y);
                g.drawImage(background, 0, 0);
                beatRueckgabe = dziwadzelnica.drawDziwadzelnica(g, talkPerson, schlaegtZu, AnimTalkPerson, dziwPoint);
            } else {
                // Cliprect fuer nach links
                g.setClip(rectLeftDziwadzelnica.topLeftPoint.x, rectLeftDziwadzelnica.topLeftPoint.y,
                        rectLeftDziwadzelnica.bottomRightPoint.x - rectLeftDziwadzelnica.topLeftPoint.x,
                        rectLeftDziwadzelnica.bottomRightPoint.y - rectLeftDziwadzelnica.topLeftPoint.y);
                g.drawImage(background, 0, 0);
                beatRueckgabe = dziwadzelnica.drawDziwadzelnica(g, talkPerson, schlaegtZu, AnimTalkPerson, dziwLeftPoint);
            }
        }

        if (!mainFrame.actions[515]) {
            // geld zeichnen, da es noch aufm Tisch liegt
            g.setClip(144, 355, 28, 21);
            g.drawImage(background, 0, 0);
            g.drawImage(pjenjezy, 144, 355);
        }

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.doWalk();

        // Krabat zeichnen
        if (krabatVisible) {
            if (krabatFaellt || klettertRein) {
                // hier das Hinfallen, Cliprect besorgt diese Routine selbst
                if (krabatFaellt) {
                    fallRueckgabe = krabatFalling.drawKrabat(g, mainFrame.krabat.getPos());
                }

                // hier das reinklettern, nur 1 Image
                if (klettertRein) {
                    // Groesse
                    int scale = mainFrame.krabat.defaultScale;
                    scale += (int) (((float) mainFrame.krabat.maxX - (float) pKletterFeet.y) / mainFrame.krabat.zoomFactor);

                    // hier Test auf "nicht zu gross"
                    if (scale < mainFrame.krabat.defaultScale) {
                        scale = mainFrame.krabat.defaultScale;
                    }

                    GenericPoint pLeftUp = new GenericPoint(pKletterFeet.x - (50 - scale / 2) / 2, pKletterFeet.y - (100 - scale));

                    g.setClip(pLeftUp.x, pLeftUp.y, 50 - scale / 2, 100 - scale);
                    g.drawImage(krabat_steigen, pLeftUp.x, pLeftUp.y, 50 - scale / 2, 100 - scale);
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
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinter Stuhl oder Rosen ? (nur Clipping - Region wird neugezeichnet)
        if (pKrTemp.x < 310) {
            g.drawImage(stolc, 147, 419);
        }
        if (pKrTemp.x > 490) {
            g.drawImage(roze, 543, 309);
        }

        // Hier das FadeToBlack, wenn noetig
        if (FadeToBlack > 0) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);

            g.clearRect(0, 0, FadeToBlack, 479);
            g.clearRect(639 - FadeToBlack, 0, 639, 479);
            g.clearRect(0, 0, 639, FadeToBlack);
            g.clearRect(0, 479 - FadeToBlack, 639, 479);

            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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

        // Anims bedienen
        if (dziwSchreit) {
            doAnims();
        }

        // selbstaendige Anim beginnen
        if (setAnim) {
            mainFrame.krabat.stopWalking();
            setAnim = false;
            nextActionID = setAnimID;
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
                // Extra-Punkt fuer Walkto wegen Ueberschneidungen
                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                nextActionID = 0;

                BorderRect tmp = mainFrame.krabat.getBoundingBox();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // ist Schauspielerin da ?
                if (!mainFrame.actions[515]) {
                    // Ausreden fuer Dziwadzelnica
                    if (rectLookDziwadzelnica.isPointInRect(pTemp)) {
                        // Extra - Sinnloszeug
                        nextActionID = 155;
                        pTxxx = pDziwadzelnica;
                    }
                }

                // Ausreden fuer kotwica - fenster
                if (rectHaken.isPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTxxx = pHaken;
                }

                // Ausreden fuer roze
                if (blumen.isPointInRect(pTemp)) {
                    // hlebija
                    nextActionID = mainFrame.whatItem == 42 ? 200 : 160;
                    pTxxx = pBlumen;
                }

                // Ausreden fuer spaniska
                if (faltWand.isPointInRect(pTemp)) {
                    nextActionID = 165;
                    pTxxx = pFaltWand;
                }

                // Ausreden fuer spihel
                if (spiegel.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 42: // hlebija
                        case 46: // hammer
                        case 48: // Metall
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 170;
                            break;
                    }
                    pTxxx = pSpiegel;
                }

                // Ausreden fuer 5 tolerow
                if (geld.isPointInRect(pTemp) && !mainFrame.actions[515]) {
                    nextActionID = 175;
                    pTxxx = pGeld;
                }

                // Ausreden fuer dokument
                if (papier.isPointInRect(pTemp)) {
                    nextActionID = 180;
                    pTxxx = pPapier;
                }

                log.debug("Point = {} {}", pTxxx.x, pTxxx.y);

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.setNewWay(pTxxx);
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
                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // linke Maustaste
                nextActionID = 0;

                // zu Halle gehen ?
                if (untererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.isPointInRect(kt)) {
                        pTxxx = pExitDown;
                    } else {
                        pTxxx = new GenericPoint(kt.x, pExitDown.y);  // X-Pos bleibt, Y vom Exitpunkt
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach draussen gehen ?
                if (obererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.isPointInRect(kt)) {
                        pTxxx = pExitUp;
                    } else {
                        pTxxx = new GenericPoint(kt.x, pExitUp.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Enterhaken ansehen
                if (rectHaken.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTxxx = pHaken;
                }

                // Spiegel ansehen
                if (spiegel.isPointInRect(pTemp)) {
                    pTxxx = pSpiegel;
                    nextActionID = 4;
                }

                // Blumen ansehen
                if (blumen.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTxxx = pBlumen;
                }

                // Faltwand ansehen
                if (faltWand.isPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTxxx = pFaltWand;
                }

                // 5 Tolerow ansehen
                if (geld.isPointInRect(pTemp) && !mainFrame.actions[515]) {
                    nextActionID = 9;
                    pTxxx = pGeld;
                }

                // dokument ansehen
                if (papier.isPointInRect(pTemp)) {
                    nextActionID = 10;
                    pTxxx = pPapier;
                }

                // ist Schauspielerin da ?
                if (!mainFrame.actions[515]) {
                    // Dziwadzelnica ansehen
                    if (rectLookDziwadzelnica.isPointInRect(pTemp)) {
                        nextActionID = 1;
                        pTxxx = pDziwadzelnica;
                    }
                }

                log.debug("Point = {} {}", pTxxx.x, pTxxx.y);

                mainFrame.pathWalker.setNewWay(pTxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // ist Schauspielerin da ?
                if (!mainFrame.actions[515]) {
                    // Mit Dziwadzelnica reden
                    if (rectLookDziwadzelnica.isPointInRect(pTemp)) {
                        nextActionID = 2;
                        pTemp = pDziwadzelnica;
                        mainFrame.pathWalker.setNewWay(pTemp);
                        mainFrame.repaint();
                        return;
                    }
                }

                // ist Schauspielerin noch da ?
                if (!mainFrame.actions[515]) {
                    // Geld nehmen -> paar auf die Ruebe
                    if (geld.isPointInRect(pTemp)) {
                        nextActionID = 20;
                        pTemp = pPapier;
                        mainFrame.pathWalker.setNewWay(pTemp);
                        mainFrame.repaint();
                        return;
                    }
                }

                // Papier nehmen geht irgendwie nicht
                if (papier.isPointInRect(pTemp)) {
                    nextActionID = 30;
                    pTemp = pPapier;
                    mainFrame.pathWalker.setNewWay(pTemp);
                    mainFrame.repaint();
                    return;
                }

                // Am Enterhaken runterklettern
                if (rectHaken.isPointInRect(pTemp)) {
                    nextActionID = 40;
                    mainFrame.pathWalker.setNewWay(pHaken);
                    mainFrame.repaint();
                    return;
                }

                // in Spiegel sehen
                if (spiegel.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.pathWalker.setNewWay(pSpiegel);
                    mainFrame.repaint();
                    return;
                }

                // an Blumen riechen
                if (blumen.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    mainFrame.pathWalker.setNewWay(pBlumen);
                    mainFrame.repaint();
                    return;
                }

                // Spaniska mitnehmen
                if (faltWand.isPointInRect(pTemp)) {
                    nextActionID = 35;
                    mainFrame.pathWalker.setNewWay(pFaltWand);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (untererAusgang.isPointInRect(pTemp) ||
                        obererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.stopWalking();
                mainFrame.isClipSet = false;
                resetAnims();
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
                    rectLookDziwadzelnica.isPointInRect(pTemp) && !mainFrame.actions[515] ||
                    papier.isPointInRect(pTemp) ||
                    spiegel.isPointInRect(pTemp) ||
                    blumen.isPointInRect(pTemp) ||
                    faltWand.isPointInRect(pTemp) ||
                    rectHaken.isPointInRect(pTemp) ||
                    geld.isPointInRect(pTemp) && !mainFrame.actions[515];

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
            if (rectLookDziwadzelnica.isPointInRect(pTemp) && !mainFrame.actions[515] ||
                    papier.isPointInRect(pTemp) ||
                    spiegel.isPointInRect(pTemp) ||
                    blumen.isPointInRect(pTemp) ||
                    faltWand.isPointInRect(pTemp) ||
                    rectHaken.isPointInRect(pTemp) ||
                    geld.isPointInRect(pTemp) && !mainFrame.actions[515]) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (untererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 3) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    cursorShape = 3;
                }
                return;
            }

            if (obererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 6) {
                    mainFrame.setCursor(mainFrame.cursorRight);
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
        resetAnims();
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
                // Dziwad#delnica anschauen
                krabatSays("Spaniska_1", fDziw, 3, 0, 0);
                break;

            case 2:
                // Mit Dziwadzelnica reden (versuchen)
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 301;
                mainFrame.krabat.setFacing(fDziw);
                break;

            case 3:
                // Enterhaken anschauen
                krabatSays("Spaniska_2", fKotwica, 3, 0, 0);
                break;

            case 4:
                // Spiegel anschauen
                krabatSays("Spaniska_3", fSpiegel, 3, 0, 0);
                break;

            case 5:
                // Spiegel benutzen
                krabatSays("Spaniska_4", fSpiegel, 3, 0, 0);
                break;

            case 6:
                // Blumen anschauen
                krabatSays("Spaniska_5", fBlumen, 3, 0, 0);
                break;

            case 7:
                // Blumen benutzen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.setFacing(fBlumen);
                nextActionID = 15;
                Counter = 3;
                break;

            case 8:
                // Faltwand ansehen
                krabatSays("Spaniska_6", fFaltWand, 3, 0, 0);
                break;

            case 9:
                // 5 Taler ansehen
                krabatSays("Spaniska_7", fToler, 3, 0, 0);
                break;

            case 10:
                // Papier ansehen
                krabatSays("Spaniska_8", fPapier, 3, 0, 0);
                break;

            case 15:
                // wirklich riechen
                if (--Counter > 1) {
                    break;
                }
                mainFrame.krabat.nAnimation = 62;
                nextActionID = 16;
                break;

            case 16:
                // Kommentar
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                krabatSays("Spaniska_9", fBlumen, 3, 2, 17);
                break;

            case 17:
                // Ende Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 20:
                // Geld nehmen und niederschlagen lassen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                dziwSchreit = false;
                resetAnims();
                mainFrame.krabat.setFacing(fToler);
                mainFrame.krabat.nAnimation = 91;
                nextActionID = 21;
                Counter = 5;
                break;

            case 21:
                // Auf Ende des Aufhebens warten
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(40);
                    mainFrame.isClipSet = false;
                    mainFrame.actions[515] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                nextActionID = 22;
                break;

            case 22:
                // Dziw haut drauf
                schlaegtZu = true;
                nextActionID = 23;
                break;

            case 23:
                // Faden einleiten
                if (beatRueckgabe) {
                    break;
                }
                krabatFaellt = true;
                nextActionID = 24;
                break;

            case 24:
                // bis zum Ende warten
                if (fallRueckgabe) {
                    break;
                }
                FadeToBlack = 1;
                nextActionID = 25;
                break;

            case 25:
                // bis zum Ende warten
                FadeToBlack += 3;
                if (FadeToBlack >= 240) {
                    nextActionID = 310;
                }
                break;

            case 30:
                // Papier mitnehmen
                krabatSays("Spaniska_10", fPapier, 3, 0, 0);
                break;

            case 35:
                // Spaniska mitnehmen
                krabatSays("Spaniska_11", fFaltWand, 3, 0, 0);
                break;

            case 40:
                // Haken wieder wegnehmen
                krabatSays("Spaniska_12", fKotwica, 3, 0, 0);
                break;

            case 100:
                // Frau schreit rum (Hilfe) oder gehe zu Hala-Doppelbild
                if (!mainFrame.actions[515]) {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    nextActionID = 301;
                    mainFrame.repaint();
                } else {
                    createNewLocation(123, locationID);
                }
                break;

            case 101:
                // Frau schreit rum (Hilfe) oder gehe zu Hintergasse zurueck
                createNewLocation(121, locationID);
                break;

            case 150:
                // Wokno - Ausreden
                thingExcuse(fKotwica);
                break;

            case 155:
                // Dziwad#delnica - Ausreden
                maleExcuse(fDziw);
                break;

            case 160:
                // roze - Ausreden
                thingExcuse(fBlumen);
                break;

            case 165:
                // spaniska - Ausreden
                thingExcuse(fFaltWand);
                break;

            case 170:
                // spihel - Ausreden
                thingExcuse(fSpiegel);
                break;

            case 175:
                // 5 tolerow - Ausreden
                thingExcuse(fToler);
                break;

            case 180:
                // dokument - Ausreden
                thingExcuse(fPapier);
                break;

            case 200:
                // hlebija auf rosen
                krabatSays("Spaniska_13", fBlumen, 3, 0, 0);
                break;

            case 210:
                // schwere ggst auf Spiegel
                krabatSays("Spaniska_14", fSpiegel, 3, 0, 0);
                break;

            // Krabat schmeisst Bemerkungen
            case 301:
                // zufaellige Antwort -> Zahl von 0 bis 2 generieren
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        krabatSays("Spaniska_15", 0, 3, 2, 800);
                        break;

                    case 1:
                        krabatSays("Spaniska_16", 0, 3, 2, 800);
                        break;

                    case 2:
                        krabatSays("Spaniska_17", 0, 3, 2, 800);
                        break;
                }
                break;

            // KRABAT WIRD NIEDERGESCHLAGEN
            case 310:
                // Text einblenden
                mainFrame.actions[514] = true;  // Dziw ist weg
                personSays("Spaniska_18", 0, 54, 2, 311, talkPointCenter);
                break;

            case 311:
                // Text einblenden
                personSays("Spaniska_19", 0, 54, 2, 312, talkPointCenter);
                break;

            case 312:
                // Text einblenden
                krabatFaellt = false;
                FadeToBlack = 0;
                mainFrame.isClipSet = false;
                personSays("Spaniska_20", 0, 54, 2, 313, talkPointCenter);
                break;

            case 313:
                // Krabat brummt der Schaedel
                BackgroundMusicPlayer.getInstance().playTrack(16, true);
                krabatSays("Spaniska_21", 0, 3, 2, 800);
                break;

            case 800:
                // Dialog beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            // Anim, wie sich Haken festbeisst im Fenster ///////////////////////////////////////

            case 1000:
                // Hakenwerfanim beginnt
                Counter = 1;
                nextActionID = 1010;
                break;

            case 1010:
                // warten, bis 1. Haken gezeigt wird
                if (--Counter > 0) {
                    break;
                }
                ersterHakenDa = true;
                mainFrame.isClipSet = false;
                nextActionID = 1020;
                Counter = 6;
                break;

            case 1020:
                // abwarten, bis 1. Haken time out
                if (--Counter > 0) {
                    break;
                }
                zweiterHakenDa = true;
                mainFrame.soundPlayer.playFile("sfx-dd/haken.wav");
                mainFrame.isClipSet = false;
                nextActionID = 1030;
                Counter = 60;
                break;

            case 1030:
                // warten, bis 2. Haken Timeout und dann back
                if (--Counter > 0) {
                    break;
                }
                createNewLocation(121, locationID);
                break;

            // Anim, wie Krabat reingestiegen kommt ////////////////////////////////////////
            case 2000:
                // warteschleife init.
                Counter = 7;
                nextActionID = 2010;
                break;

            case 2010:
                // warten bis Ende Counter
                if (--Counter > 1) {
                    break;
                }
                klettertRein = false;
                mainFrame.pathWalker.setNewWay(new GenericPoint(480, 420));
                nextActionID = 2020;
                break;

            case 2020:
                // warten bis Ende Counter
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }

    // Schnarchanim des Loewen ausfuehren
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

            // Dziwadzelnica schreit rum
            case 30:
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                int zuffZahl = (int) (Math.random() * 3.9);
                if (dziwSagtNurKurzeSaetze && zuffZahl > 1) {
                    zuffZahl = (int) (Math.random() * 1.9);
                }
                switch (zuffZahl) {
                    case 0:
                        AnimOutputText = Start.STRING_MANAGER.getTranslation("Spaniska_22");
                        AnimCounter = 50;
                        break;

                    case 1:
                        AnimOutputText = Start.STRING_MANAGER.getTranslation("Spaniska_23");
                        AnimCounter = 60;
                        break;

                    case 2:
                        AnimOutputText = Start.STRING_MANAGER.getTranslation("Spaniska_24");
                        AnimCounter = 120;
                        break;

                    case 3:
                        AnimOutputText = Start.STRING_MANAGER.getTranslation("Spaniska_25");
                        AnimCounter = 70;
                        break;
                }
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, talkPoint);
                AnimTalkPerson = 58;
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
                AnimTalkPerson = 0;
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