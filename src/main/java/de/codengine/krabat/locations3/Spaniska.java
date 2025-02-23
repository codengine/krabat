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
import de.codengine.krabat.anims.KrabatFall;
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
    private final Borderrect rectDziwadzelnica;
    private final Borderrect rectLeftDziwadzelnica;
    private final Borderrect rectLookDziwadzelnica;
    private final GenericPoint dziwPoint;
    private final GenericPoint dziwLeftPoint;

    private int FadeToBlack = 0;

    private boolean schlaegtZu = false;
    private boolean krabatFaellt = false;
    private boolean fallRueckgabe = true;
    private boolean beatRueckgabe = true;
    private boolean dziwSagtNurKurzeSaetze = false; // wenn Hakenanim, dann keinen Monstersatz von sich geben -> lesbar in kurzer Zeit

    private final KrabatFall krabatFall;

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
    private static final Borderrect untererAusgang
            = new Borderrect(275, 445, 500, 479);
    private static final Borderrect obererAusgang
            = new Borderrect(456, 126, 554, 287);
    private static final Borderrect papier
            = new Borderrect(67, 352, 115, 378);
    private static final Borderrect spiegel
            = new Borderrect(35, 146, 110, 306);
    private static final Borderrect blumen
            = new Borderrect(558, 317, 639, 440);
    private static final Borderrect faltWand
            = new Borderrect(235, 167, 413, 373);
    private static final Borderrect rectHaken
            = new Borderrect(460, 288, 524, 333);
    private static final Borderrect geld
            = new Borderrect(144, 355, 171, 374);

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
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 50;   // nicht zoomen !!!
        mainFrame.krabat.zoomf = 1f;
        mainFrame.krabat.defScale = -90;

        dziwadzelnica = new Dziwadzelnica(mainFrame);

        dziwPoint = new GenericPoint(dziwFeetRight.x - Dziwadzelnica.Breite / 2, dziwFeetRight.y - Dziwadzelnica.Hoehe);
        dziwLeftPoint = new GenericPoint(dziwFeetLeft.x - Dziwadzelnica.Breite / 2, dziwFeetLeft.y - Dziwadzelnica.Hoehe);
        talkPoint = new GenericPoint(dziwFeetLeft.x, dziwLeftPoint.y - 50);

        rectDziwadzelnica = new Borderrect(dziwPoint.x, dziwPoint.y, dziwPoint.x + Dziwadzelnica.Breite,
                dziwPoint.y + Dziwadzelnica.Hoehe);
        rectLeftDziwadzelnica = new Borderrect(dziwLeftPoint.x, dziwLeftPoint.y, dziwLeftPoint.x + Dziwadzelnica.Breite,
                dziwLeftPoint.y + Dziwadzelnica.Hoehe);
        rectLookDziwadzelnica = new Borderrect(dziwLeftPoint.x, dziwLeftPoint.y, 294, /* Achtung, wird nicht berechnet!!*/ dziwPoint.y + Dziwadzelnica.Hoehe);


        krabatFall = new KrabatFall(mainFrame, -90);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        // mainFrame.wegGeher.vBorders.addElement 
        //	(new bordertrapez (230, 245, 230, 290, 395, 429));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(325, 490, 318, 490, 430, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(245, 317, 185, 317, 465, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(450, 455, 435, 600, 370, 429));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(590, 430, 620, 432));

        mainFrame.pathFinder.ClearMatrix(4);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(0, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);

        InitImages();
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
                mainFrame.krabat.SetFacing(6);
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
                mainFrame.krabat.SetFacing(12);
                break;
        }

        // wenn Dziwadzelnica noch da, dann die Backgroundanims einschalten...
        if (!mainFrame.actions[514]) {
            dziwSchreit = true;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
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
            Cursorform = 200;
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
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // ist Schauspielerin da ?
        if (!mainFrame.actions[514]) {
            // Dziwadzelnica zeichnen
            if (!schlaegtZu) {
                // Cliprect fuer nach rechts schauen
                g.setClip(rectDziwadzelnica.lo_point.x, rectDziwadzelnica.lo_point.y,
                        rectDziwadzelnica.ru_point.x - rectDziwadzelnica.lo_point.x,
                        rectDziwadzelnica.ru_point.y - rectDziwadzelnica.lo_point.y);
                g.drawImage(background, 0, 0);
                beatRueckgabe = dziwadzelnica.drawDziwadzelnica(g, TalkPerson, schlaegtZu, AnimTalkPerson, dziwPoint);
            } else {
                // Cliprect fuer nach links
                g.setClip(rectLeftDziwadzelnica.lo_point.x, rectLeftDziwadzelnica.lo_point.y,
                        rectLeftDziwadzelnica.ru_point.x - rectLeftDziwadzelnica.lo_point.x,
                        rectLeftDziwadzelnica.ru_point.y - rectLeftDziwadzelnica.lo_point.y);
                g.drawImage(background, 0, 0);
                beatRueckgabe = dziwadzelnica.drawDziwadzelnica(g, TalkPerson, schlaegtZu, AnimTalkPerson, dziwLeftPoint);
            }
        }

        if (!mainFrame.actions[515]) {
            // geld zeichnen, da es noch aufm Tisch liegt
            g.setClip(144, 355, 28, 21);
            g.drawImage(background, 0, 0);
            g.drawImage(pjenjezy, 144, 355);
        }

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.GeheWeg();

        // Krabat zeichnen
        if (krabatVisible) {
            if (krabatFaellt || klettertRein) {
                // hier das Hinfallen, Cliprect besorgt diese Routine selbst
                if (krabatFaellt) {
                    fallRueckgabe = krabatFall.drawKrabat(g, mainFrame.krabat.getPos());
                }

                // hier das reinklettern, nur 1 Image
                if (klettertRein) {
                    // Groesse
                    int scale = mainFrame.krabat.defScale;
                    scale += (int) (((float) mainFrame.krabat.maxx - (float) pKletterFeet.y) / mainFrame.krabat.zoomf);

                    // hier Test auf "nicht zu gross"
                    if (scale < mainFrame.krabat.defScale) {
                        scale = mainFrame.krabat.defScale;
                    }

                    GenericPoint pLeftUp = new GenericPoint(pKletterFeet.x - (50 - scale / 2) / 2, pKletterFeet.y - (100 - scale));

                    g.setClip(pLeftUp.x, pLeftUp.y, 50 - scale / 2, 100 - scale);
                    g.drawImage(krabat_steigen, pLeftUp.x, pLeftUp.y, 50 - scale / 2, 100 - scale);
                }
            } else {
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
            mainFrame.imageFont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
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

        // Anims bedienen
        if (dziwSchreit) {
            DoAnims();
        }

        // selbstaendige Anim beginnen
        if (setAnim) {
            mainFrame.krabat.StopWalking();
            setAnim = false;
            nextActionID = setAnimID;
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
                // Extra-Punkt fuer Walkto wegen Ueberschneidungen
                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // ist Schauspielerin da ?
                if (!mainFrame.actions[515]) {
                    // Ausreden fuer Dziwadzelnica
                    if (rectLookDziwadzelnica.IsPointInRect(pTemp)) {
                        // Extra - Sinnloszeug
                        nextActionID = 155;
                        pTxxx = pDziwadzelnica;
                    }
                }

                // Ausreden fuer kotwica - fenster
                if (rectHaken.IsPointInRect(pTemp)) {
                    nextActionID = 150;
                    pTxxx = pHaken;
                }

                // Ausreden fuer roze
                if (blumen.IsPointInRect(pTemp)) {
                    // hlebija
                    nextActionID = mainFrame.whatItem == 42 ? 200 : 160;
                    pTxxx = pBlumen;
                }

                // Ausreden fuer spaniska
                if (faltWand.IsPointInRect(pTemp)) {
                    nextActionID = 165;
                    pTxxx = pFaltWand;
                }

                // Ausreden fuer spihel
                if (spiegel.IsPointInRect(pTemp)) {
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
                if (geld.IsPointInRect(pTemp) && !mainFrame.actions[515]) {
                    nextActionID = 175;
                    pTxxx = pGeld;
                }

                // Ausreden fuer dokument
                if (papier.IsPointInRect(pTemp)) {
                    nextActionID = 180;
                    pTxxx = pPapier;
                }

                log.debug("Point = {} {}", pTxxx.x, pTxxx.y);

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
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
                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // linke Maustaste
                nextActionID = 0;

                // zu Halle gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTxxx = pExitDown;
                    } else {
                        pTxxx = new GenericPoint(kt.x, pExitDown.y);  // X-Pos bleibt, Y vom Exitpunkt
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach draussen gehen ?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTxxx = pExitUp;
                    } else {
                        pTxxx = new GenericPoint(kt.x, pExitUp.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Enterhaken ansehen
                if (rectHaken.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTxxx = pHaken;
                }

                // Spiegel ansehen
                if (spiegel.IsPointInRect(pTemp)) {
                    pTxxx = pSpiegel;
                    nextActionID = 4;
                }

                // Blumen ansehen
                if (blumen.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTxxx = pBlumen;
                }

                // Faltwand ansehen
                if (faltWand.IsPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTxxx = pFaltWand;
                }

                // 5 Tolerow ansehen
                if (geld.IsPointInRect(pTemp) && !mainFrame.actions[515]) {
                    nextActionID = 9;
                    pTxxx = pGeld;
                }

                // dokument ansehen
                if (papier.IsPointInRect(pTemp)) {
                    nextActionID = 10;
                    pTxxx = pPapier;
                }

                // ist Schauspielerin da ?
                if (!mainFrame.actions[515]) {
                    // Dziwadzelnica ansehen
                    if (rectLookDziwadzelnica.IsPointInRect(pTemp)) {
                        nextActionID = 1;
                        pTxxx = pDziwadzelnica;
                    }
                }

                log.debug("Point = {} {}", pTxxx.x, pTxxx.y);

                mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // ist Schauspielerin da ?
                if (!mainFrame.actions[515]) {
                    // Mit Dziwadzelnica reden
                    if (rectLookDziwadzelnica.IsPointInRect(pTemp)) {
                        nextActionID = 2;
                        pTemp = pDziwadzelnica;
                        mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                        mainFrame.repaint();
                        return;
                    }
                }

                // ist Schauspielerin noch da ?
                if (!mainFrame.actions[515]) {
                    // Geld nehmen -> paar auf die Ruebe
                    if (geld.IsPointInRect(pTemp)) {
                        nextActionID = 20;
                        pTemp = pPapier;
                        mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                        mainFrame.repaint();
                        return;
                    }
                }

                // Papier nehmen geht irgendwie nicht
                if (papier.IsPointInRect(pTemp)) {
                    nextActionID = 30;
                    pTemp = pPapier;
                    mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                    mainFrame.repaint();
                    return;
                }

                // Am Enterhaken runterklettern
                if (rectHaken.IsPointInRect(pTemp)) {
                    nextActionID = 40;
                    mainFrame.pathWalker.SetzeNeuenWeg(pHaken);
                    mainFrame.repaint();
                    return;
                }

                // in Spiegel sehen
                if (spiegel.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSpiegel);
                    mainFrame.repaint();
                    return;
                }

                // an Blumen riechen
                if (blumen.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    mainFrame.pathWalker.SetzeNeuenWeg(pBlumen);
                    mainFrame.repaint();
                    return;
                }

                // Spaniska mitnehmen
                if (faltWand.IsPointInRect(pTemp)) {
                    nextActionID = 35;
                    mainFrame.pathWalker.SetzeNeuenWeg(pFaltWand);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (untererAusgang.IsPointInRect(pTemp) ||
                        obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.isClipSet = false;
                ResetAnims();
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
                    rectLookDziwadzelnica.IsPointInRect(pTemp) && !mainFrame.actions[515] ||
                    papier.IsPointInRect(pTemp) ||
                    spiegel.IsPointInRect(pTemp) ||
                    blumen.IsPointInRect(pTemp) ||
                    faltWand.IsPointInRect(pTemp) ||
                    rectHaken.IsPointInRect(pTemp) ||
                    geld.IsPointInRect(pTemp) && !mainFrame.actions[515];

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
            if (rectLookDziwadzelnica.IsPointInRect(pTemp) && !mainFrame.actions[515] ||
                    papier.IsPointInRect(pTemp) ||
                    spiegel.IsPointInRect(pTemp) ||
                    blumen.IsPointInRect(pTemp) ||
                    faltWand.IsPointInRect(pTemp) ||
                    rectHaken.IsPointInRect(pTemp) ||
                    geld.IsPointInRect(pTemp) && !mainFrame.actions[515]) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 3;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 6;
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
        ResetAnims();
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
                // Dziwad#delnica anschauen
                KrabatSagt("Spaniska_1", fDziw, 3, 0, 0);
                break;

            case 2:
                // Mit Dziwadzelnica reden (versuchen)
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 301;
                mainFrame.krabat.SetFacing(fDziw);
                break;

            case 3:
                // Enterhaken anschauen
                KrabatSagt("Spaniska_2", fKotwica, 3, 0, 0);
                break;

            case 4:
                // Spiegel anschauen
                KrabatSagt("Spaniska_3", fSpiegel, 3, 0, 0);
                break;

            case 5:
                // Spiegel benutzen
                KrabatSagt("Spaniska_4", fSpiegel, 3, 0, 0);
                break;

            case 6:
                // Blumen anschauen
                KrabatSagt("Spaniska_5", fBlumen, 3, 0, 0);
                break;

            case 7:
                // Blumen benutzen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.SetFacing(fBlumen);
                nextActionID = 15;
                Counter = 3;
                break;

            case 8:
                // Faltwand ansehen
                KrabatSagt("Spaniska_6", fFaltWand, 3, 0, 0);
                break;

            case 9:
                // 5 Taler ansehen
                KrabatSagt("Spaniska_7", fToler, 3, 0, 0);
                break;

            case 10:
                // Papier ansehen
                KrabatSagt("Spaniska_8", fPapier, 3, 0, 0);
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
                KrabatSagt("Spaniska_9", fBlumen, 3, 2, 17);
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
                ResetAnims();
                mainFrame.krabat.SetFacing(fToler);
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
                KrabatSagt("Spaniska_10", fPapier, 3, 0, 0);
                break;

            case 35:
                // Spaniska mitnehmen
                KrabatSagt("Spaniska_11", fFaltWand, 3, 0, 0);
                break;

            case 40:
                // Haken wieder wegnehmen
                KrabatSagt("Spaniska_12", fKotwica, 3, 0, 0);
                break;

            case 100:
                // Frau schreit rum (Hilfe) oder gehe zu Hala-Doppelbild
                if (!mainFrame.actions[515]) {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    nextActionID = 301;
                    mainFrame.repaint();
                } else {
                    NeuesBild(123, locationID);
                }
                break;

            case 101:
                // Frau schreit rum (Hilfe) oder gehe zu Hintergasse zurueck
                NeuesBild(121, locationID);
                break;

            case 150:
                // Wokno - Ausreden
                DingAusrede(fKotwica);
                break;

            case 155:
                // Dziwad#delnica - Ausreden
                MPersonAusrede(fDziw);
                break;

            case 160:
                // roze - Ausreden
                DingAusrede(fBlumen);
                break;

            case 165:
                // spaniska - Ausreden
                DingAusrede(fFaltWand);
                break;

            case 170:
                // spihel - Ausreden
                DingAusrede(fSpiegel);
                break;

            case 175:
                // 5 tolerow - Ausreden
                DingAusrede(fToler);
                break;

            case 180:
                // dokument - Ausreden
                DingAusrede(fPapier);
                break;

            case 200:
                // hlebija auf rosen
                KrabatSagt("Spaniska_13", fBlumen, 3, 0, 0);
                break;

            case 210:
                // schwere ggst auf Spiegel
                KrabatSagt("Spaniska_14", fSpiegel, 3, 0, 0);
                break;

            // Krabat schmeisst Bemerkungen
            case 301:
                // zufaellige Antwort -> Zahl von 0 bis 2 generieren
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        KrabatSagt("Spaniska_15", 0, 3, 2, 800);
                        break;

                    case 1:
                        KrabatSagt("Spaniska_16", 0, 3, 2, 800);
                        break;

                    case 2:
                        KrabatSagt("Spaniska_17", 0, 3, 2, 800);
                        break;
                }
                break;

            // KRABAT WIRD NIEDERGESCHLAGEN
            case 310:
                // Text einblenden
                mainFrame.actions[514] = true;  // Dziw ist weg
                PersonSagt("Spaniska_18", 0, 54, 2, 311, talkPointCenter);
                break;

            case 311:
                // Text einblenden
                PersonSagt("Spaniska_19", 0, 54, 2, 312, talkPointCenter);
                break;

            case 312:
                // Text einblenden
                krabatFaellt = false;
                FadeToBlack = 0;
                mainFrame.isClipSet = false;
                PersonSagt("Spaniska_20", 0, 54, 2, 313, talkPointCenter);
                break;

            case 313:
                // Krabat brummt der Schaedel
                BackgroundMusicPlayer.getInstance().playTrack(16, true);
                KrabatSagt("Spaniska_21", 0, 3, 2, 800);
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
                mainFrame.soundPlayer.PlayFile("sfx-dd/haken.wav");
                mainFrame.isClipSet = false;
                nextActionID = 1030;
                Counter = 60;
                break;

            case 1030:
                // warten, bis 2. Haken Timeout und dann back
                if (--Counter > 0) {
                    break;
                }
                NeuesBild(121, locationID);
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
                mainFrame.pathWalker.SetzeNeuenWeg(new GenericPoint(480, 420));
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
    private void DoAnims() {
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
                        AnimOutputText = Start.stringManager.getTranslation("Spaniska_22");
                        AnimCounter = 50;
                        break;

                    case 1:
                        AnimOutputText = Start.stringManager.getTranslation("Spaniska_23");
                        AnimCounter = 60;
                        break;

                    case 2:
                        AnimOutputText = Start.stringManager.getTranslation("Spaniska_24");
                        AnimCounter = 120;
                        break;

                    case 3:
                        AnimOutputText = Start.stringManager.getTranslation("Spaniska_25");
                        AnimCounter = 70;
                        break;
                }
                AnimOutputTextPos = mainFrame.imageFont.CenterAnimText(AnimOutputText, talkPoint);
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
    private void ResetAnims() {
        AnimOutputText = "";
        AnimCounter = 10;
        AnimID = 10;
    }

}