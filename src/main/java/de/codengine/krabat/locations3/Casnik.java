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
import de.codengine.krabat.anims.HlownyStraznik;
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
    private final HlownyStraznik hlStraznik;
    private final Multiple2 Dialog;

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
    private static final Borderrect obererAusgang
            = new Borderrect(414, 158, 496, 293);
    private static final Borderrect untererAusgang
            = new Borderrect(50, 439, 300, 479);
    private static final Borderrect standuhr
            = new Borderrect(300, 145, 345, 315);
    private static final Borderrect morgenstern
            = new Borderrect(22, 310, 54, 360);
    private static final Borderrect rectKozuch
            = new Borderrect(345, 410, 500, 470);
    private static final Borderrect rectSchluessel
            = new Borderrect(125, 230, 143, 253);
    private static final Borderrect zeiger
            = new Borderrect(303, 146, 345, 187);
    private static final Borderrect statue
            = new Borderrect(168, 142, 200, 237);
    private static final Borderrect sonnenuhr
            = new Borderrect(139, 284, 150, 296);
    private static final Borderrect umsonnenuhr
            = new Borderrect(133, 278, 156, 302);

    // Konstante Points
    private static final GenericPoint pExitUp = new GenericPoint(450, 295);
    private static final GenericPoint pExitDown = new GenericPoint(190, 479);
    private static final GenericPoint pKozuch = new GenericPoint(355, 472);
    private static final GenericPoint pStraznik = new GenericPoint(205, 359);
    private static final GenericPoint pSchluessel = new GenericPoint(108, 375);
    private static final GenericPoint pMorgenstern = new GenericPoint(60, 380);
    private static final GenericPoint pStanduhr = new GenericPoint(321, 341);
    // private static final GenericPoint strazFeet    = new GenericPoint (120, 338);
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

        mainFrame.krabat.maxx = 365;
        mainFrame.krabat.zoomf = 1.2f;
        mainFrame.krabat.defScale = -40;

        pendel = new GenericImage[3];

        hlStraznik = new HlownyStraznik(mainFrame, true, mainFrame.actions[705]);
        //                                           ist casnik
        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);

        Verhinderpendel = MAX_VERHINDERPENDEL[Pendelpos];

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
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

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 146: // von Wonka aus
                mainFrame.krabat.setPos(new GenericPoint(190, 470));
                mainFrame.krabat.SetFacing(12);
                break;
            case 144: // von Couch aus
                mainFrame.krabat.setPos(new GenericPoint(450, 300));
                mainFrame.krabat.SetFacing(6);
                break;
        }

        // beim Init festlegen, ob geschnarcht wird oder nicht
        straznikSchnarcht = mainFrame.actions[705];
    }

    private void initBorders() {
        // Grenzen setzen, wenn Tigerfell liegt
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(20, 355, 20, 355, 404, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(505, 620, 505, 620, 404, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(20, 620, 20, 620, 364, 403));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(262, 455, 198, 550, 320, 363));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(415, 470, 415, 455, 300, 319));

        mainFrame.pathFinder.ClearMatrix(5);

        mainFrame.pathFinder.PosVerbinden(0, 2);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);
    }

    private void initBordersWithoutTiger() {
        // Grenzen setzen, wenn kein Tigerfell liegt
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(20, 620, 20, 620, 364, 479));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(262, 455, 198, 550, 320, 363));
        mainFrame.pathWalker.vBorders.addElement
                (new Bordertrapez(415, 470, 415, 455, 300, 319));

        mainFrame.pathFinder.ClearMatrix(3);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
    }

    // Bilder vorbereiten
    private void InitImages() {
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

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        /*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
          {
          Dialog.paintMultiple (g);
          return;
          }*/

        // wenn noch ie dringewesen, dann Sound von Uhr "um drei"
        if (!mainFrame.actions[608]) {
            mainFrame.actions[608] = true;
            mainFrame.soundPlayer.PlayFile("sfx-dd/drei.wav");
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
        Borderrect temp = hlStraznik.straznikRect(TalkPerson);
        // Hintergrund loeschen
        g.setClip(temp.lo_point.x, temp.lo_point.y, temp.ru_point.x - temp.lo_point.x,
                temp.ru_point.y - temp.lo_point.y);
        g.drawImage(background, 0, 0);


        // Schluessel zeichnen, wenn noch da
        if (!mainFrame.actions[951]) {
            g.setClip(129, 235, 6, 16);
            g.drawImage(background, 0, 0);
            g.drawImage(kluc, 129, 235);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Straznik weiterbewegen
        readyFlag = hlStraznik.evalStraznik(TalkPerson, trink, schlafein, mainFrame.actions[706]);
        // Cliprect nun setzen
        temp = hlStraznik.straznikRect(TalkPerson);
        g.setClip(temp.lo_point.x, temp.lo_point.y, temp.ru_point.x - temp.lo_point.x,
                temp.ru_point.y - temp.lo_point.y);
        // Hl. Straznik zeichnen
        hlStraznik.drawStraznik(g, TalkPerson);
        if (trink) {
            trink = false;
        }
        if (schlafein) {
            schlafein = false;
        }

        // Krabat einen Schritt laufen lassen
        mainFrame.pathWalker.GeheWeg();

        // Krabat zeichnen

        if (krabatStelltUhrUm) {
            // Uhr umstellen, erstmal Pos und Groesse berechnen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.getPos().x, mainFrame.krabat.getPos().y);

            // Groesse
            int scale = mainFrame.krabat.defScale;
            scale += (int) (((float) mainFrame.krabat.maxx - (float) hier.y) / mainFrame.krabat.zoomf);

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

        // hinter Schloss ? (nur Clipping - Region wird neugezeichnet)
        // if (rectHrod.IsPointInRect (pKrTemp) == true) {
        //    g.drawImage (hrod, 0, 354, null);
        // }

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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Anims bedienen
        if (straznikSchnarcht) {
            DoAnims();
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Kozuch, wenn noch da
                if (rectKozuch.IsPointInRect(pTemp) && !mainFrame.actions[600]) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
                    pTemp = pKozuch;
                }

                // Ausreden fuer HlStraznik
                // wenn drin und:
                // Sonnenuhr weg ODER
                // nicht in der Naehe Sonnenuhr und schon schlafend ODER
                // noch stehend
                if (hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) &&
                        (mainFrame.actions[706] ||
                                !umsonnenuhr.IsPointInRect(pTemp) && mainFrame.actions[705] ||
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
                if (rectSchluessel.IsPointInRect(pTemp) && !mainFrame.actions[951]) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTemp = pSchluessel;
                }

                // Ausreden fuer Morgenstern
                if (morgenstern.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = pMorgenstern;
                }

                // Ausreden fuer Standuhr oder Vorstellen !!
                if (zeiger.IsPointInRect(pTemp)) {
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
                if (standuhr.IsPointInRect(pTemp) && !zeiger.IsPointInRect(pTemp)) {
                    nextActionID = 190;
                    pTemp = pStanduhr;
                }

                // Ausreden fuer Postawa
                if (statue.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 200;
                    pTemp = pStatue;
                }

                // Ausreden fuer Sonnenuhr
                // wenn drin, noch nicht weg aber schon da
                if (sonnenuhr.IsPointInRect(pTemp) && !mainFrame.actions[706] &&
                        mainFrame.actions[705]) {
                    // Extra - Sinnloszeug
                    nextActionID = 250;
                    pTemp = pSonnenuhr;
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

                // zu Wonka gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = pExitDown;
                    } else {
                        pTemp = new GenericPoint(pExitDown.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Couch gehen ?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = pExitUp;
                    } else {
                        pTemp = new GenericPoint(pExitUp.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // HlStraznik ansehen
                if (hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) &&
                        (mainFrame.actions[706] ||
                                !umsonnenuhr.IsPointInRect(pTemp) && mainFrame.actions[705] ||
                                !mainFrame.actions[705])) {
                    nextActionID = 1;
                    pTemp = pStraznik;
                }

                // Schluessel ansehen
                if (rectSchluessel.IsPointInRect(pTemp) && !mainFrame.actions[951]) {
                    nextActionID = 2;
                    pTemp = pSchluessel;
                }

                // Tigerfell ansehen
                if (!mainFrame.actions[600] && rectKozuch.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = pKozuch;
                }

                // Morgenstern ansehen
                if (morgenstern.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pMorgenstern;
                }

                // Standuhr ansehen
                if (standuhr.IsPointInRect(pTemp) && !zeiger.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pStanduhr;
                }

                // Zeiger ansehen
                if (zeiger.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pZeiger;
                }

                // Postawa ansehen
                if (statue.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pStatue;
                }

                // Sonnenuhr ansehen
                if (sonnenuhr.IsPointInRect(pTemp) && !mainFrame.actions[706] &&
                        mainFrame.actions[705]) {
                    nextActionID = 8;
                    pTemp = pSonnenuhr;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Hl.Straznik reden
                if (hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) &&
                        (mainFrame.actions[706] ||
                                !umsonnenuhr.IsPointInRect(pTemp) && mainFrame.actions[705] ||
                                !mainFrame.actions[705])) {
                    if (mainFrame.actions[605]) {
                        // er ist schon eingeschlafen
                        nextActionID = 70;
                    } else {
                        // er kann noch reden
                        nextActionID = 50;
                    }
                    mainFrame.pathWalker.SetzeNeuenWeg(pStraznik);
                    mainFrame.repaint();
                    return;
                }

                // Kozuch mitnehmen
                if (rectKozuch.IsPointInRect(pTemp) &&
                        !mainFrame.actions[600]) {
                    nextActionID = 40;
                    mainFrame.pathWalker.SetzeNeuenWeg(pKozuch);
                    mainFrame.repaint();
                    return;
                }

                // Schluessel mitnehmen
                if (rectSchluessel.IsPointInRect(pTemp) && !mainFrame.actions[951]) {
                    nextActionID = 45;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSchluessel);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (obererAusgang.IsPointInRect(pTemp) ||
                        untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Morgenstern mitnehmen
                if (morgenstern.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(pMorgenstern);
                    mainFrame.repaint();
                    return;
                }

                // Standuhr mitnehmen
                if (standuhr.IsPointInRect(pTemp) && !zeiger.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.SetzeNeuenWeg(pStanduhr);
                    mainFrame.repaint();
                    return;
                }

                // Zeiger mitnehmen
                if (zeiger.IsPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.pathWalker.SetzeNeuenWeg(pZeiger);
                    mainFrame.repaint();
                    return;
                }

                // Statua mitnehmen
                if (statue.IsPointInRect(pTemp)) {
                    nextActionID = 80;
                    mainFrame.pathWalker.SetzeNeuenWeg(pStatue);
                    mainFrame.repaint();
                    return;
                }

                // Sonnenuhr mitnehmen
                if (sonnenuhr.IsPointInRect(pTemp) && !mainFrame.actions[706] &&
                        mainFrame.actions[705]) {
                    nextActionID = 90;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSonnenuhr);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                ResetAnims();
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    rectKozuch.IsPointInRect(pTemp) && !mainFrame.actions[600] ||
                    sonnenuhr.IsPointInRect(pTemp) && !mainFrame.actions[706] &&
                            mainFrame.actions[705] ||
                    hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) &&
                            (mainFrame.actions[706] ||
                                    !umsonnenuhr.IsPointInRect(pTemp) && mainFrame.actions[705] ||
                                    !mainFrame.actions[705]) ||
                    rectSchluessel.IsPointInRect(pTemp) && !mainFrame.actions[951] ||
                    morgenstern.IsPointInRect(pTemp) || standuhr.IsPointInRect(pTemp) ||
                    statue.IsPointInRect(pTemp);

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
            if (rectKozuch.IsPointInRect(pTemp) && !mainFrame.actions[600] ||
                    hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) &&
                            (mainFrame.actions[706] ||
                                    !umsonnenuhr.IsPointInRect(pTemp) && mainFrame.actions[705] ||
                                    !mainFrame.actions[705]) ||
                    sonnenuhr.IsPointInRect(pTemp) && !mainFrame.actions[706] &&
                            mainFrame.actions[705] ||
                    rectSchluessel.IsPointInRect(pTemp) && !mainFrame.actions[951] ||
                    morgenstern.IsPointInRect(pTemp) || standuhr.IsPointInRect(pTemp) ||
                    statue.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.cursorDown);
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
        ResetAnims();
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
            mainFrame.soundPlayer.PlayFile("sfx-dd/tick.wav");
        }
        if (Pendelpos == 0 && !tick) {
            mainFrame.soundPlayer.PlayFile("sfx-dd/tack.wav");
        }
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
                // Hlowny straznik anschauen
                // Testen, ob er schon schlaeft
                if (!mainFrame.actions[605]) {
                    KrabatSagt("Casnik_1", fStraznik, 3, 0, 0);
                } else {
                    KrabatSagt("Casnik_2", fStraznik, 3, 0, 0);
                }
                break;

            case 2:
                // Schluessel anschauen
                // Test, ob Krabat schon weiss, wozu der Schluessel ist
                if (!mainFrame.actions[602]) {
                    KrabatSagt("Casnik_3", fKluc, 3, 0, 0);
                } else {
                    KrabatSagt("Casnik_4", fKluc, 3, 0, 0);
                }
                break;

            case 3:
                // Tigerfell anschauen
                // zuffi 0...1
                int zffZahl = (int) (Math.random() * 1.9);
                switch (zffZahl) {
                    case 0:
                        KrabatSagt("Casnik_5", fTiger, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Casnik_6", fTiger, 3, 0, 0);
                        break;
                }
                break;

            case 4:
                // Morgenstern anschauen
                KrabatSagt("Casnik_7", fMorgenstern, 3, 0, 0);
                break;

            case 5:
                // Standuhr anschauen
                KrabatSagt("Casnik_8", fCasnik, 3, 0, 0);
                break;

            case 6:
                // Zeiger anschauen
                // Entscheidung, ob schon vorgestellt
                if (!mainFrame.actions[606]) {
                    KrabatSagt("Casnik_9", fZeiger, 3, 0, 0);
                } else {
                    KrabatSagt("Casnik_10", fZeiger, 3, 0, 0);
                }
                break;

            case 7:
                // Engel anschauen
                KrabatSagt("Casnik_11", fEngel, 3, 0, 0);
                break;

            case 8:
                // Sonnenuhr anschauen
                KrabatSagt("Casnik_12", fSonnenuhr, 3, 0, 0);
                break;

            case 40:
                // Tigerfell mitnehmen und Bewegungsgrenzen neu setzen
                mainFrame.isAnimRunning = true;
                initBordersWithoutTiger();
                mainFrame.krabat.SetFacing(fTiger);
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
                    mainFrame.krabat.SetFacing(fKluc);
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
                        NeuesBild(120, locationID);
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
                mainFrame.krabat.SetFacing(9);
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
                KrabatSagt("Casnik_13", fMorgenstern, 3, 0, 0);
                break;

            case 60:
                // Uhr mitnehmen
                KrabatSagt("Casnik_14", fCasnik, 3, 0, 0);
                break;

            case 70:
                // Straznik benutzen, wenn schon eingeschlafen
                KrabatSagt("Casnik_15", fStraznik, 3, 0, 0);
                break;

            case 75:
                // Zeiger mitnehmen
                KrabatSagt("Casnik_16", fZeiger, 3, 0, 0);
                break;

            case 80:
                // Postawa mitnehmen
                KrabatSagt("Casnik_17", fEngel, 3, 0, 0);
                break;

            case 90:
                // Sonnenuhr mitnehmen
                mainFrame.isAnimRunning = true;
                mainFrame.krabat.SetFacing(fSonnenuhr);
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
                NeuesBild(146, locationID);
                break;

            case 101:
                // Gehe zu Chodba
                if (mainFrame.actions[605]) {
                    // darf zur couch gehen (Wache hat Wein bekommen)
                    NeuesBild(144, locationID);
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
                        NeuesBild(120, locationID);
                    }
                }
                break;

            case 150:
                // Kozuch - Ausreden
                DingAusrede(fTiger);
                break;

            case 155:
                // Straznik - Ausreden
                MPersonAusrede(fStraznik);
                break;

            case 160:
                // Schluessel - Ausreden
                DingAusrede(fKluc);
                break;

            case 165:
                // Morgenstern - Ausreden
                DingAusrede(fMorgenstern);
                break;

            case 170:
                // Uhr vorstellen
                if (!mainFrame.actions[606]) {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    SoundCountdown = 30;
                    mainFrame.isInventoryCursor = false;
                    mainFrame.krabat.SetFacing(fZeiger);
                    nextActionID = 171;
                    krabatStelltUhrUm = true;
                    umstellCounter = 7;
                } else {
                    KrabatSagt("Casnik_18", fZeiger, 3, 0, 0);
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
                mainFrame.soundPlayer.PlayFile("sfx-dd/vier.wav");
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;


            case 175:
                // Uhr - Ausreden
                DingAusrede(fCasnik);
                break;

            case 185:
                // Reaktion Hl.Straznik, wenn Wein zu frueh angeboten
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.nAnimation = 142;
                PersonSagt("Casnik_19", fStraznik, 48, 2, 186, hlStraznik.evalTalkPoint());
                break;

            case 186:
                // Reaktion Hl.Straznik auf zu frueh verabreichten Wein
                PersonSagt("Casnik_20", 0, 48, 2, 187, hlStraznik.evalTalkPoint());
                mainFrame.krabat.StopAnim();
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
                DingAusrede(fZeiger);
                break;

            case 200:
                // statue - Ausreden
                DingAusrede(fEngel);
                break;


            case 205:
                // Gib Wein an Straznik
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                noSoundActive = true;
                mainFrame.isInventoryCursor = false;
                mainFrame.krabat.nAnimation = 142;
                PersonSagt("Casnik_21", fStraznik, 48, 2, 220, hlStraznik.evalTalkPoint());
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
                mainFrame.krabat.StopAnim();
                mainFrame.soundPlayer.PlayFile("sfx-dd/pic.wav");
                mainFrame.inventory.vInventory.removeElement(44); // Wein rausnehmen
                nextActionID = 230;
                break;

            case 230:
                // Reaktion Hl.Straznik auf verabreichten Wein
                if (readyFlag) {
                    break;
                }
                PersonSagt("Casnik_22", 0, 48, 2, 235, hlStraznik.evalTalkPoint());
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
                DingAusrede(fSonnenuhr);
                break;

            // Sequenzen mit Hauptwaechter  /////////////////////////////////

            case 300:
                // Versuch ins Arbeitszimmer zu gehen
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                int zuffZahl = (int) (Math.random() * 2.9);
                switch (zuffZahl) {
                    case 0:
                        outputText = Start.stringManager.getTranslation("Casnik_36");
                        break;

                    case 1:
                        outputText = Start.stringManager.getTranslation("Casnik_37");
                        break;

                    case 2:
                        outputText = Start.stringManager.getTranslation("Casnik_38");
                        break;
                }

                outputTextPos = mainFrame.imageFont.CenterText(outputText, hlStraznik.evalTalkPoint());
                TalkPerson = 48;
                TalkPause = 2;
                nextActionID = 800;
                break;

            case 301:
                // Versuch Schluessel wegzunehmen
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        outputText = Start.stringManager.getTranslation("Casnik_39");
                        break;

                    case 1:
                        outputText = Start.stringManager.getTranslation("Casnik_40");
                        break;

                    case 2:
                        outputText = Start.stringManager.getTranslation("Casnik_41");
                        break;
                }

                outputTextPos = mainFrame.imageFont.CenterText(outputText, hlStraznik.evalTalkPoint());
                TalkPerson = 48;
                TalkPause = 2;
                nextActionID = 800;
                break;


            // Dialog mit Kuchar ////////////////////////////////////////////

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // 1. Frage
                Dialog.ExtendMC("Casnik_29", 1000, 602, new int[]{602}, 610);
                Dialog.ExtendMC("Casnik_30", 602, 1000, null, 611);

                // 2. Frage
                Dialog.ExtendMC("Casnik_31", 1000, 604, new int[]{604}, 620);
                Dialog.ExtendMC("Casnik_32", 604, 603, new int[]{603}, 621);
                Dialog.ExtendMC("Casnik_33", 603, 1000, null, 622);

                // 3. Frage (607)
                Dialog.ExtendMC("Casnik_34", 1000, 607, new int[]{607}, 800);
                Dialog.ExtendMC("Casnik_35", 607, 1000, null, 800);

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

            // Sequenz vor MC (erstes Anreden)
            case 608:
                // Reaktion Hl.Straznik
                PersonSagt("Casnik_23", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                mainFrame.actions[601] = true; // Flag setzen, Diese Zeile nicht wiederholen
                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Hl.Straznik
                PersonSagt("Casnik_24", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 611:
                // Reaktion Hl.Straznik
                PersonSagt("Casnik_25", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Hl.Straznik
                PersonSagt("Casnik_26", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 621:
                // Reaktion Hl.Straznik
                PersonSagt("Casnik_27", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 622:
                // Reaktion Hl.Straznik
                PersonSagt("Casnik_28", 0, 48, 2, 600, hlStraznik.evalTalkPoint());
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

            case 30:
                // Text ueber Hl. Straznik ausgeben
                AnimOutputText = Start.stringManager.getTranslation("Casnik_42");
                AnimOutputTextPos = mainFrame.imageFont.CenterAnimText(AnimOutputText, hlStraznik.evalTalkPoint());
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
    private void ResetAnims() {
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