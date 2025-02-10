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

package de.codengine.locations3;

import de.codengine.Start;
import de.codengine.anims.HlownyStraznik;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Casnik extends Mainloc {
    private GenericImage background, kozuch, umdrei, umvier, kluc, zeitumstell;
    private GenericImage[] pendel;
    private HlownyStraznik hlStraznik;
    private Multiple2 Dialog;

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

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 365;
        mainFrame.krabat.zoomf = 1.2f;
        mainFrame.krabat.defScale = -40;

        pendel = new GenericImage[3];

        hlStraznik = new HlownyStraznik(mainFrame, true, mainFrame.Actions[705]);
        //                                           ist casnik
        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);

        Verhinderpendel = MAX_VERHINDERPENDEL[Pendelpos];

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // wenn kein Load, dann alle Versuche zuruecksetzen
        if (oldLocation != 0) {
            // Actions fuer Versuche zuruecksetzen
            for (int i = 615; i <= 620; i++) {
                mainFrame.Actions[i] = false;
            }
        }

        // Grenzen setzen (abhaengig vom Tigerfell)
        if (mainFrame.Actions[600] == false) {
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
                mainFrame.krabat.SetKrabatPos(new GenericPoint(190, 470));
                mainFrame.krabat.SetFacing(12);
                break;
            case 144: // von Couch aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(450, 300));
                mainFrame.krabat.SetFacing(6);
                break;
        }

        // beim Init festlegen, ob geschnarcht wird oder nicht
        straznikSchnarcht = mainFrame.Actions[705];
    }

    private void initBorders() {
        // Grenzen setzen, wenn Tigerfell liegt
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 355, 20, 355, 404, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(505, 620, 505, 620, 404, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 620, 20, 620, 364, 403));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(262, 455, 198, 550, 320, 363));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(415, 470, 415, 455, 300, 319));

        mainFrame.wegSucher.ClearMatrix(5);

        mainFrame.wegSucher.PosVerbinden(0, 2);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
    }

    private void initBordersWithoutTiger() {
        // Grenzen setzen, wenn kein Tigerfell liegt
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(20, 620, 20, 620, 364, 479));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(262, 455, 198, 550, 320, 363));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(415, 470, 415, 455, 300, 319));

        mainFrame.wegSucher.ClearMatrix(3);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/casnik/casnik.gif");
        kozuch = getPicture("gfx-dd/casnik/kozuch.gif");
        umdrei = getPicture("gfx-dd/casnik/cas3.gif");
        umvier = getPicture("gfx-dd/casnik/cas4.gif");
        kluc = getPicture("gfx-dd/casnik/pkluc.gif");
        zeitumstell = getPicture("gfx-dd/casnik/s-o-hlebija.gif");

        pendel[0] = getPicture("gfx-dd/casnik/pendel2.gif");
        pendel[1] = getPicture("gfx-dd/casnik/pendel1.gif");
        pendel[2] = getPicture("gfx-dd/casnik/pendel3.gif");

        loadPicture();
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
        if (mainFrame.Actions[608] == false) {
            mainFrame.Actions[608] = true;
            mainFrame.wave.PlayFile("sfx-dd/drei.wav");
        }

        // Clipping -Region initialisieren
        if (mainFrame.Clipset == false) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // liegt Tigerfell noch da ?
        if (mainFrame.Actions[600] == false) {
            g.setClip(355, 412, 145, 75);
            g.drawImage(background, 0, 0, null);
            g.drawImage(kozuch, 355, 412, null);
        }

        // Uhr richtig zeichnen
        g.setClip(311, 154, 25, 24);
        g.drawImage(background, 0, 0, null);
        g.drawImage((mainFrame.Actions[606] == false) ? umdrei : umvier, 311, 154, null);

        // Hier das Uhrpendel rein
        g.setClip(309, 190, 32, 59);
        g.drawImage(pendel[Pendelpos], 309, 190, null);
        if ((--Verhinderpendel) < 1) {
            if (pendelForward == true) {
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
        g.setClip(temp.lo_point.x, temp.lo_point.y, (temp.ru_point.x - temp.lo_point.x),
                (temp.ru_point.y - temp.lo_point.y));
        g.drawImage(background, 0, 0, null);


        // Schluessel zeichnen, wenn noch da
        if (mainFrame.Actions[951] == false) {
            g.setClip(129, 235, 6, 16);
            g.drawImage(background, 0, 0, null);
            g.drawImage(kluc, 129, 235, null);
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Straznik weiterbewegen
        readyFlag = hlStraznik.evalStraznik(TalkPerson, trink, schlafein, mainFrame.Actions[706]);
        // Cliprect nun setzen
        temp = hlStraznik.straznikRect(TalkPerson);
        g.setClip(temp.lo_point.x, temp.lo_point.y, (temp.ru_point.x - temp.lo_point.x),
                (temp.ru_point.y - temp.lo_point.y));
        // Hl. Straznik zeichnen
        hlStraznik.drawStraznik(g, TalkPerson);
        if (trink == true) {
            trink = false;
        }
        if (schlafein == true) {
            schlafein = false;
        }

        // Krabat einen Schritt laufen lassen
        mainFrame.wegGeher.GeheWeg();

        // Krabat zeichnen

        if (krabatStelltUhrUm == true) {
            // Uhr umstellen, erstmal Pos und Groesse berechnen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.GetKrabatPos().x, mainFrame.krabat.GetKrabatPos().y);

            // Groesse
            int scale = mainFrame.krabat.defScale;
            scale += (int) (((float) mainFrame.krabat.maxx - (float) hier.y) / mainFrame.krabat.zoomf);

            // System.out.println ("Scale ist " + scale + " gross.");

            // Umrechnen des Scalings auf dieses Image
            float scaleUmrechnung = 163f / 100f;
            scale = (int) ((float) scale * scaleUmrechnung);

            // Hoehe: nur offset
            int hoch = (int) (163 - scale);

            // Breite abhaengig von Hoehe...
            float Scaleverhaeltnis = 163f / 50f;
            int weit = (int) (50 - ((float) scale / Scaleverhaeltnis));

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            // Krabat zeichnen
            g.drawImage(zeitumstell, hier.x, hier.y, weit, hoch, null);
        } else {
            // Animation??
            if (mainFrame.krabat.nAnimation != 0) {
                mainFrame.krabat.DoAnimation(g);

                // Cursorruecksetzung nach Animationsende
                if (mainFrame.krabat.nAnimation == 0) {
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                }
            } else {
                if ((mainFrame.talkCount > 0) && (TalkPerson != 0)) {
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
        if (AnimOutputText != "") {
            // Textausgabe
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip((int) may.getX(), (int) may.getY(), (int) may.getWidth(), (int) may.getHeight());
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple == true) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Anims bedienen
        if (straznikSchnarcht == true) {
            DoAnims();
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // bei Multiple Choice extra Mouseroutine
        if (mainFrame.isMultiple == true) {
            Dialog.evalMouseEvent(e);
            return;
        }

        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim == true) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // linker Maustaste
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp) == true) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Kozuch, wenn noch da
                if ((rectKozuch.IsPointInRect(pTemp) == true) && (mainFrame.Actions[600] == false)) {
                    // Standard - Sinnloszeug
                    nextActionID = 150;
                    pTemp = pKozuch;
                }

                // Ausreden fuer HlStraznik
                // wenn drin und:
                // Sonnenuhr weg ODER
                // nicht in der Naehe Sonnenuhr und schon schlafend ODER
                // noch stehend
                if ((hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) == true) &&
                        ((mainFrame.Actions[706] == true) ||
                                ((umsonnenuhr.IsPointInRect(pTemp) == false) && (mainFrame.Actions[705] == true)) ||
                                (mainFrame.Actions[705] == false))) {
                    // Wein geben, wenn es geht
                    if ((mainFrame.whatItem == 44) && (mainFrame.Actions[606] == true)) {
                        nextActionID = 205;
                    } else {
                        // Wein geben, wenn zu zeitig
                        if ((mainFrame.whatItem == 44) && (mainFrame.Actions[606] == false)) {
                            nextActionID = 185;
                        } else {
                            // Extra - Sinnloszeug fuer andere Sachen
                            nextActionID = 155;
                        }
                    }
                    pTemp = pStraznik;
                }

                // Ausreden fuer Schluessel
                if ((rectSchluessel.IsPointInRect(pTemp) == true) && (mainFrame.Actions[951] == false)) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTemp = pSchluessel;
                }

                // Ausreden fuer Morgenstern
                if (morgenstern.IsPointInRect(pTemp) == true) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = pMorgenstern;
                }

                // Ausreden fuer Standuhr oder Vorstellen !!
                if (zeiger.IsPointInRect(pTemp) == true) {
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
                if ((standuhr.IsPointInRect(pTemp) == true) && (zeiger.IsPointInRect(pTemp) == false)) {
                    nextActionID = 190;
                    pTemp = pStanduhr;
                }

                // Ausreden fuer Postawa
                if (statue.IsPointInRect(pTemp) == true) {
                    // Extra - Sinnloszeug
                    nextActionID = 200;
                    pTemp = pStatue;
                }

                // Ausreden fuer Sonnenuhr
                // wenn drin, noch nicht weg aber schon da
                if ((sonnenuhr.IsPointInRect(pTemp) == true) && (mainFrame.Actions[706] == false) &&
                        (mainFrame.Actions[705] == true)) {
                    // Extra - Sinnloszeug
                    nextActionID = 250;
                    pTemp = pSonnenuhr;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.invCursor = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
                return;
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Wonka gehen ?
                if (untererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (untererAusgang.IsPointInRect(kt) == false) {
                        pTemp = pExitDown;
                    } else {
                        pTemp = new GenericPoint(pExitDown.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Couch gehen ?
                if (obererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (obererAusgang.IsPointInRect(kt) == false) {
                        pTemp = pExitUp;
                    } else {
                        pTemp = new GenericPoint(pExitUp.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // HlStraznik ansehen
                if ((hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) == true) &&
                        ((mainFrame.Actions[706] == true) ||
                                ((umsonnenuhr.IsPointInRect(pTemp) == false) && (mainFrame.Actions[705] == true)) ||
                                (mainFrame.Actions[705] == false))) {
                    nextActionID = 1;
                    pTemp = pStraznik;
                }

                // Schluessel ansehen
                if ((rectSchluessel.IsPointInRect(pTemp) == true) && (mainFrame.Actions[951] == false)) {
                    nextActionID = 2;
                    pTemp = pSchluessel;
                }

                // Tigerfell ansehen
                if ((mainFrame.Actions[600] == false) && (rectKozuch.IsPointInRect(pTemp) == true)) {
                    nextActionID = 3;
                    pTemp = pKozuch;
                }

                // Morgenstern ansehen
                if (morgenstern.IsPointInRect(pTemp) == true) {
                    nextActionID = 4;
                    pTemp = pMorgenstern;
                }

                // Standuhr ansehen
                if ((standuhr.IsPointInRect(pTemp) == true) && (zeiger.IsPointInRect(pTemp) == false)) {
                    nextActionID = 5;
                    pTemp = pStanduhr;
                }

                // Zeiger ansehen
                if (zeiger.IsPointInRect(pTemp) == true) {
                    nextActionID = 6;
                    pTemp = pZeiger;
                }

                // Postawa ansehen
                if (statue.IsPointInRect(pTemp) == true) {
                    nextActionID = 7;
                    pTemp = pStatue;
                }

                // Sonnenuhr ansehen
                if ((sonnenuhr.IsPointInRect(pTemp) == true) && (mainFrame.Actions[706] == false) &&
                        (mainFrame.Actions[705] == true)) {
                    nextActionID = 8;
                    pTemp = pSonnenuhr;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Hl.Straznik reden
                if ((hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) == true) &&
                        ((mainFrame.Actions[706] == true) ||
                                ((umsonnenuhr.IsPointInRect(pTemp) == false) && (mainFrame.Actions[705] == true)) ||
                                (mainFrame.Actions[705] == false))) {
                    if (mainFrame.Actions[605] == true) {
                        // er ist schon eingeschlafen
                        nextActionID = 70;
                    } else {
                        // er kann noch reden
                        nextActionID = 50;
                    }
                    mainFrame.wegGeher.SetzeNeuenWeg(pStraznik);
                    mainFrame.repaint();
                    return;
                }

                // Kozuch mitnehmen
                if ((rectKozuch.IsPointInRect(pTemp) == true) &&
                        (mainFrame.Actions[600] == false)) {
                    nextActionID = 40;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKozuch);
                    mainFrame.repaint();
                    return;
                }

                // Schluessel mitnehmen
                if ((rectSchluessel.IsPointInRect(pTemp) == true) && (mainFrame.Actions[951] == false)) {
                    nextActionID = 45;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSchluessel);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((obererAusgang.IsPointInRect(pTemp) == true) ||
                        (untererAusgang.IsPointInRect(pTemp))) {
                    return;
                }

                // Morgenstern mitnehmen
                if (morgenstern.IsPointInRect(pTemp) == true) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pMorgenstern);
                    mainFrame.repaint();
                    return;
                }

                // Standuhr mitnehmen
                if ((standuhr.IsPointInRect(pTemp) == true) && (zeiger.IsPointInRect(pTemp) == false)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(pStanduhr);
                    mainFrame.repaint();
                    return;
                }

                // Zeiger mitnehmen
                if (zeiger.IsPointInRect(pTemp) == true) {
                    nextActionID = 75;
                    mainFrame.wegGeher.SetzeNeuenWeg(pZeiger);
                    mainFrame.repaint();
                    return;
                }

                // Statua mitnehmen
                if (statue.IsPointInRect(pTemp) == true) {
                    nextActionID = 80;
                    mainFrame.wegGeher.SetzeNeuenWeg(pStatue);
                    mainFrame.repaint();
                    return;
                }

                // Sonnenuhr mitnehmen
                if ((sonnenuhr.IsPointInRect(pTemp) == true) && (mainFrame.Actions[706] == false) &&
                        (mainFrame.Actions[705] == true)) {
                    nextActionID = 90;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSonnenuhr);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.Clipset = false;
                mainFrame.repaint();
                ResetAnims();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple == true) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if ((mainFrame.fPlayAnim == true) || (mainFrame.krabat.nAnimation != 0)) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            if ((tmp.IsPointInRect(pTemp) == true) ||
                    ((rectKozuch.IsPointInRect(pTemp) == true) && (mainFrame.Actions[600] == false)) ||
                    ((sonnenuhr.IsPointInRect(pTemp) == true) && (mainFrame.Actions[706] == false) &&
                            (mainFrame.Actions[705] == true)) ||
                    ((hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) == true) &&
                            ((mainFrame.Actions[706] == true) ||
                                    ((umsonnenuhr.IsPointInRect(pTemp) == false) && (mainFrame.Actions[705] == true)) ||
                                    (mainFrame.Actions[705] == false))) ||
                    ((rectSchluessel.IsPointInRect(pTemp) == true) && (mainFrame.Actions[951] == false)) ||
                    (morgenstern.IsPointInRect(pTemp) == true) || (standuhr.IsPointInRect(pTemp) == true) ||
                    (statue.IsPointInRect(pTemp) == true)) {
                mainFrame.invHighCursor = true;
            } else {
                mainFrame.invHighCursor = false;
            }

            if ((Cursorform != 10) && (mainFrame.invHighCursor == false)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor == true)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (((rectKozuch.IsPointInRect(pTemp) == true) && (mainFrame.Actions[600] == false)) ||
                    ((hlStraznik.straznikRect(TalkPerson).IsPointInRect(pTemp) == true) &&
                            ((mainFrame.Actions[706] == true) ||
                                    ((umsonnenuhr.IsPointInRect(pTemp) == false) && (mainFrame.Actions[705] == true)) ||
                                    (mainFrame.Actions[705] == false))) ||
                    ((sonnenuhr.IsPointInRect(pTemp) == true) && (mainFrame.Actions[706] == false) &&
                            (mainFrame.Actions[705] == true)) ||
                    ((rectSchluessel.IsPointInRect(pTemp) == true) && (mainFrame.Actions[951] == false)) ||
                    (morgenstern.IsPointInRect(pTemp) == true) || (standuhr.IsPointInRect(pTemp) == true) ||
                    (statue.IsPointInRect(pTemp) == true)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 6;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
        if (mainFrame.isMultiple == true) {
            Dialog.evalMouseExitEvent(e);
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultiple == true) {
            Dialog.evalKeyEvent(e);
            return;
        }

        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor == true) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim == true) {
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
            return;
        }
    }

    // Vor Key - Events alles deaktivieren
    private void Keyclear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
        mainFrame.krabat.StopWalking();
        ResetAnims();
    }

    private void evalSound(boolean tick) {
        // zufaellig wavs fuer Geschnatter abspielen...
        // wenn gefordert, dann auch nicht spielen
        if (noSoundActive == true) {
            return;
        }

        if ((--SoundCountdown) > 1) {
            return; // wenn noetig, dann bisschen warten, ehe was gespielt wird
        }

        // nur kurz vor Ausschlagende abspielen
        if ((Pendelpos == 2) && (tick == true)) {
            mainFrame.wave.PlayFile("sfx-dd/tick.wav");
        }
        if ((Pendelpos == 0) && (tick == false)) {
            mainFrame.wave.PlayFile("sfx-dd/tack.wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering == true) ||
                (mainFrame.krabat.isWalking == true)) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if ((nextActionID > 499) && (nextActionID < 600)) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.Mousepoint);

            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if ((nextActionID > 119) && (nextActionID < 129)) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Hlowny straznik anschauen
                // Testen, ob er schon schlaeft
                if (mainFrame.Actions[605] == false) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00000"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00001"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00002"),
                            fStraznik, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00003"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00004"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00005"),
                            fStraznik, 3, 0, 0);
                }
                break;

            case 2:
                // Schluessel anschauen
                // Test, ob Krabat schon weiss, wozu der Schluessel ist
                if (mainFrame.Actions[602] == false) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00006"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00007"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00008"),
                            fKluc, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00009"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00010"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00011"),
                            fKluc, 3, 0, 0);
                }
                break;

            case 3:
                // Tigerfell anschauen
                // zuffi 0...1
                int zffZahl = (int) (Math.random() * 1.9);
                switch (zffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00012"),
                                Start.stringManager.getTranslation("Loc3_Casnik_00013"),
                                Start.stringManager.getTranslation("Loc3_Casnik_00014"),
                                fTiger, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00015"),
                                Start.stringManager.getTranslation("Loc3_Casnik_00016"),
                                Start.stringManager.getTranslation("Loc3_Casnik_00017"),
                                fTiger, 3, 0, 0);
                        break;
                }
                break;

            case 4:
                // Morgenstern anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00018"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00019"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00020"),
                        fMorgenstern, 3, 0, 0);
                break;

            case 5:
                // Standuhr anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00021"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00022"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00023"),
                        fCasnik, 3, 0, 0);
                break;

            case 6:
                // Zeiger anschauen
                // Entscheidung, ob schon vorgestellt
                if (mainFrame.Actions[606] == false) {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00024"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00025"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00026"),
                            fZeiger, 3, 0, 0);
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00027"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00028"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00029"),
                            fZeiger, 3, 0, 0);
                }
                break;

            case 7:
                // Engel anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00030"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00031"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00032"),
                        fEngel, 3, 0, 0);
                break;

            case 8:
                // Sonnenuhr anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00033"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00034"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00035"),
                        fSonnenuhr, 3, 0, 0);
                break;

            case 40:
                // Tigerfell mitnehmen und Bewegungsgrenzen neu setzen
                mainFrame.fPlayAnim = true;
                initBordersWithoutTiger();
                mainFrame.krabat.SetFacing(fTiger);
                mainFrame.krabat.nAnimation = 32;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 43;
                Counter = 5;
                break;

            case 43:
                // Ende take Kozuch
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(new Integer(36));
                    mainFrame.Clipset = false;
                    mainFrame.Actions[600] = true;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 45:
                // Schluessel mitnehmen
                if (mainFrame.Actions[605] == true) {
                    // darf mitnehmen (Wache hat Wein bekommen -> besoffen)
                    mainFrame.fPlayAnim = true;
                    mainFrame.krabat.SetFacing(fKluc);
                    mainFrame.krabat.nAnimation = 120;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    nextActionID = 48;
                    Counter = 5;
                } else {
                    // 4 mal darf Kr. versuchen wegzunehmen
                    if ((mainFrame.Actions[615] == false) || (mainFrame.Actions[616] == false) || (mainFrame.Actions[617] == false)) {
                        mainFrame.fPlayAnim = true;
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        nextActionID = 301;
                        if (mainFrame.Actions[616] == true) {
                            mainFrame.Actions[617] = true;
                        }
                        if (mainFrame.Actions[615] == true) {
                            mainFrame.Actions[616] = true;
                        }
                        if (mainFrame.Actions[615] == false) {
                            mainFrame.Actions[615] = true;
                        }
                    } else {
                        // zur Strafe in die Kueche zurueck
                        NeuesBild(120, locationID);
                    }
                }
                break;

            case 48:
                // Ende take Schluessel
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(new Integer(47));
                    mainFrame.Clipset = false;
                    mainFrame.Actions[951] = true;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 50:
                // Krabat beginnt MC (Straznik benutzen)
                mainFrame.krabat.SetFacing(9);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                // Sequenz vor MC ? (beim ersten Ansprechen)
                if (mainFrame.Actions[601] == true) {
                    nextActionID = 600;
                } else {
                    nextActionID = 608;
                }
                break;

            case 55:
                // Morgenstern mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00036"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00037"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00038"),
                        fMorgenstern, 3, 0, 0);
                break;

            case 60:
                // Uhr mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00039"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00040"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00041"),
                        fCasnik, 3, 0, 0);
                break;

            case 70:
                // Straznik benutzen, wenn schon eingeschlafen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00042"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00043"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00044"),
                        fStraznik, 3, 0, 0);
                break;

            case 75:
                // Zeiger mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00045"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00046"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00047"),
                        fZeiger, 3, 0, 0);
                break;

            case 80:
                // Postawa mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00048"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00049"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00050"),
                        fEngel, 3, 0, 0);
                break;

            case 90:
                // Sonnenuhr mitnehmen
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.SetFacing(fSonnenuhr);
                mainFrame.krabat.nAnimation = 121;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 93;
                Counter = 5;
                break;

            case 93:
                // Ende take Sonnenuhr
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(new Integer(45));
                    mainFrame.Clipset = false;
                    mainFrame.Actions[706] = true;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Wonka
                NeuesBild(146, locationID);
                break;

            case 101:
                // Gehe zu Chodba
                if (mainFrame.Actions[605] == true) {
                    // darf zur couch gehen (Wache hat Wein bekommen)
                    NeuesBild(144, locationID);
                } else {
                    // 4 mal darf Kr. versuchen rauszugehen
                    if ((mainFrame.Actions[618] == false) || (mainFrame.Actions[619] == false) || (mainFrame.Actions[620] == false)) {
                        mainFrame.fPlayAnim = true;
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        nextActionID = 300;
                        if (mainFrame.Actions[619] == true) {
                            mainFrame.Actions[620] = true;
                        }
                        if (mainFrame.Actions[618] == true) {
                            mainFrame.Actions[619] = true;
                        }
                        if (mainFrame.Actions[618] == false) {
                            mainFrame.Actions[618] = true;
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
                if (mainFrame.Actions[606] == false) {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    SoundCountdown = 30;
                    mainFrame.invCursor = false;
                    mainFrame.krabat.SetFacing(fZeiger);
                    nextActionID = 171;
                    krabatStelltUhrUm = true;
                    umstellCounter = 7;
                } else {
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Casnik_00051"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00052"),
                            Start.stringManager.getTranslation("Loc3_Casnik_00053"),
                            fZeiger, 3, 0, 0);
                }
                break;

            case 171:
                // warten auf Ende Zeigerumstell und schluss damit
                if ((--umstellCounter) > 1) {
                    break;
                }
                mainFrame.Actions[606] = true;
                mainFrame.Clipset = false;
                krabatStelltUhrUm = false;
                mainFrame.wave.PlayFile("sfx-dd/vier.wav");
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;


            case 175:
                // Uhr - Ausreden
                DingAusrede(fCasnik);
                break;

            case 185:
                // Reaktion Hl.Straznik, wenn Wein zu frueh angeboten
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.nAnimation = 142;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00054"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00055"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00056"),
                        fStraznik, 48, 2, 186, hlStraznik.evalTalkPoint());
                break;

            case 186:
                // Reaktion Hl.Straznik auf zu frueh verabreichten Wein
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00057"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00058"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00059"),
                        0, 48, 2, 187, hlStraznik.evalTalkPoint());
                mainFrame.krabat.StopAnim();
                break;

            case 187:
                // Ende dieser Anim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                noSoundActive = true;
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 142;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00060"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00061"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00062"),
                        fStraznik, 48, 2, 220, hlStraznik.evalTalkPoint());
                mainFrame.Actions[605] = true;
                mainFrame.Actions[705] = true;
                break;

            case 220:
                // Straznik trinkt wein
                trink = true;
                Counter = 5;
                nextActionID = 225;
                break;

            case 225:
                // warten aufs Ende von K, dann Sound
                if ((--Counter) > 0) {
                    break;
                }
                mainFrame.krabat.StopAnim();
                mainFrame.wave.PlayFile("sfx-dd/pic.wav");
                mainFrame.inventory.vInventory.removeElement(new Integer(44)); // Wein rausnehmen
                nextActionID = 230;
                break;

            case 230:
                // Reaktion Hl.Straznik auf verabreichten Wein
                if (readyFlag == true) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00063"), Start.stringManager.getTranslation("Loc3_Casnik_00064"), Start.stringManager.getTranslation("Loc3_Casnik_00065"),
                        0, 48, 2, 235, hlStraznik.evalTalkPoint());
                break;

            case 235:
                // Straznik schlaeft ein
                schlafein = true;
                nextActionID = 240;
                break;

            case 240:
                // warten auf Ende Einschlafen
                if (readyFlag == false) {
                    nextActionID = 245;
                }
                break;

            case 245:
                // Ende Weintrinkanim  
                straznikSchnarcht = true;
                noSoundActive = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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
                        if (mainFrame.sprache == 1) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00066");
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00067");
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00068");
                        }
                        break;

                    case 1:
                        if (mainFrame.sprache == 1) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00069");
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00070");
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00071");
                        }
                        break;

                    case 2:
                        if (mainFrame.sprache == 1) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00072");
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00073");
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00074");
                        }
                        break;
                }

                outputTextPos = mainFrame.ifont.CenterText(outputText, hlStraznik.evalTalkPoint());
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
                        if (mainFrame.sprache == 1) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00075");
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00076");
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00077");
                        }
                        break;

                    case 1:
                        if (mainFrame.sprache == 1) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00078");
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00079");
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00080");
                        }
                        break;

                    case 2:
                        if (mainFrame.sprache == 1) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00081");
                        }
                        if (mainFrame.sprache == 2) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00082");
                        }
                        if (mainFrame.sprache == 3) {
                            outputText = Start.stringManager.getTranslation("Loc3_Casnik_00083");
                        }
                        break;
                }

                outputTextPos = mainFrame.ifont.CenterText(outputText, hlStraznik.evalTalkPoint());
                TalkPerson = 48;
                TalkPause = 2;
                nextActionID = 800;
                break;


            // Dialog mit Kuchar ////////////////////////////////////////////

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // Obersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00084"), 1000, 602, new int[]{602}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00085"), 602, 1000, null, 611);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00086"), 1000, 604, new int[]{604}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00087"), 604, 603, new int[]{603}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00088"), 603, 1000, null, 622);

                    // 3. Frage (607)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00089"), 1000, 607, new int[]{607}, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00090"), 607, 1000, null, 800);
                }
                // Niedersorbische Fragen /////////////////////////////////
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00091"), 1000, 602, new int[]{602}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00092"), 602, 1000, null, 611);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00093"), 1000, 604, new int[]{604}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00094"), 604, 603, new int[]{603}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00095"), 603, 1000, null, 622);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00096"), 1000, 607, new int[]{607}, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00097"), 607, 1000, null, 800);
                }
                // Deutsche Fragen /////////////////////////////////
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00098"), 1000, 602, new int[]{602}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00099"), 602, 1000, null, 611);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00100"), 1000, 604, new int[]{604}, 620);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00101"), 604, 603, new int[]{603}, 621);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00102"), 603, 1000, null, 622);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00103"), 1000, 607, new int[]{607}, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc3_Casnik_00104"), 607, 1000, null, 800);
                }

                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            // Sequenz vor MC (erstes Anreden)
            case 608:
                // Reaktion Hl.Straznik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00105"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00106"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00107"),
                        0, 48, 2, 600, hlStraznik.evalTalkPoint());
                mainFrame.Actions[601] = true; // Flag setzen, Diese Zeile nicht wiederholen
                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Hl.Straznik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00108"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00109"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00110"),
                        0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 611:
                // Reaktion Hl.Straznik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00111"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00112"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00113"),
                        0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Hl.Straznik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00114"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00115"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00116"),
                        0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 621:
                // Reaktion Hl.Straznik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00117"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00118"),
                        Start.stringManager.getTranslation("Loc3_Casnik_00119"),
                        0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 622:
                // Reaktion Hl.Straznik
                PersonSagt(Start.stringManager.getTranslation("Loc3_Casnik_00120"), Start.stringManager.getTranslation("Loc3_Casnik_00121"), Start.stringManager.getTranslation("Loc3_Casnik_00122"),
                        0, 48, 2, 600, hlStraznik.evalTalkPoint());
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.Actions[607] = false;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
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
                if ((--AnimCounter) < 1) {
                    AnimID = 30;
                }
                break;

            case 30:
                // Text ueber Hl. Straznik ausgeben
                if (mainFrame.sprache == 1) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc3_Casnik_00123");
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc3_Casnik_00124");
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc3_Casnik_00125");
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, hlStraznik.evalTalkPoint());
                AnimCounter = 50;
                AnimTalkPerson = 48;
                AnimID = 40;
                break;

            case 40:
                // warten, bis zu Ende geschnarcht
                if ((--AnimCounter) < 1) {
                    AnimID = 50;
                }
                break;

            case 50:
                // variable Pause dazwischen
                AnimOutputText = "";
                mainFrame.Clipset = false;
                AnimCounter = (int) ((Math.random() * 70) + 50);
                AnimID = 60;
                break;

            case 60:
                // Pause abwarten und von vorn...
                if ((--AnimCounter) < 1) {
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