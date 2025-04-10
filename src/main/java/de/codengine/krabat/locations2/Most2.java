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

package de.codengine.krabat.locations2;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.*;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Most2 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Most2.class);
    private GenericImage background;
    private GenericImage gelaend;
    private GenericImage wegstueck;
    private GenericImage gras;
    private final GenericImage[] flussu;
    private int oldActionID = 0;
    private boolean switchanim = false;
    private int flusscount = 1;
    private GenericPoint Endpunkt;
    private GenericPoint Wendepunkt;
    private final GenericPoint Merkpunkt;
    private boolean Berglauf = false;
    private boolean isTal;

    private Miller mueller;
    private boolean setAnim = false;
    private boolean muellerda = false;
    private boolean setAusnahme = false; // Anzeige, ob Schweinehueteszene

    private Boom muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    private boolean showPohonc = false;
    private final GenericPoint pohoncPoint;
    private final GenericPoint pohoncTalk;
    private Coachman kutscher;
    private boolean pohoncHoertZu = false;

    private Coach coach;
    private boolean kutscheOpen = false;
    // private int Kutschenstatus;
    private int kutscheArbeitet = 0;
    private boolean kutscheda = false;

    private Pig1 schwein1;
    private Pig2 schwein2;
    private Pig3 schwein3;
    private boolean schweineDa = false;
    private boolean schwein1Tanzt = false;
    private boolean schwein2Tanzt = false;
    private boolean schwein3Tanzt = false;

    private boolean zeigeGras = false;

    private int FadeToBlack = 0;

    private int Counter = 0;

    private boolean backgroundSoundAus = false; // gilt nur fuer Schweine
    private boolean wasserAus = false;          // gilt nur fuer Wasser

    // Konstanten - Rects
    private static final BorderRect rechterAusgang = new BorderRect(609, 353, 639, 415);
    private static final BorderRect obererAusgang = new BorderRect(412, 176, 496, 221);
    private static final BorderRect untererAusgang = new BorderRect(0, 436, 246, 479);
    // private static final borderrect gelRect        = new borderrect (351, 284, 430, 343); 
    private static final BorderRect ralbitzSchild = new BorderRect(254, 208, 301, 228);
    private static final BorderRect dresdenSchild = new BorderRect(255, 233, 296, 246);
    private static final BorderRect rekaRect = new BorderRect(427, 383, 513, 476);

    private static final BorderTrapezoid BergTrapez = new BorderTrapezoid(376, 396, 272, 342, 278, 349);
    private static final BorderTrapezoid TalTrapez = new BorderTrapezoid(457, 459, 407, 419, 225, 284);

    // Points in Location
    private static final GenericPoint Pschild = new GenericPoint(305, 333);
    private static final GenericPoint PrightExtra = new GenericPoint(606, 385);
    private static final GenericPoint Pdown = new GenericPoint(95, 479);
    private static final GenericPoint Pup = new GenericPoint(458, 226);
    private static final GenericPoint Pright = new GenericPoint(639, 390);
    private static final GenericPoint mlynkFeet = new GenericPoint(487, 358);
    private static final GenericPoint kralTalk = new GenericPoint(320, 220);
    private static final GenericPoint Preka = new GenericPoint(476, 388);
    private static final GenericPoint kVorKutsche = new GenericPoint(100, 450);
    private static final GenericPoint pohoncBrrr = new GenericPoint(200, 220);

    // Punkte fuer Floetengang
    private static final GenericPoint PvorGras = new GenericPoint(254, 323);
    private static final GenericPoint PnachGras = new GenericPoint(180, 300);
    private static final GenericPoint PvorFloete = new GenericPoint(30, 300);
    private static final GenericPoint Pfloete = new GenericPoint(30, 340);

    // Zooming - Variablen
    private static final int TAL_MAXX = 278;
    private static final int TAL_MINX = 190;
    private static final float TAL_ZOOMF = 5.78f;
    private static final int TAL_DEFSCALE = 69;

    private static final int BERG_MAXX = 370;
    private static final int BERG_MINX = 190;
    private static final float BERG_ZOOMF = 1.28f;
    private static final int BERG_DEFSCALE = -40;

    // Ausreden, wenn Gegend verlassen werden soll
    private static final String[] TEXTS = {"Most2_25", "Most2_26", "Most2_27", "Most2_28"};

    // Konstante ints
    private static final int fPokazRal = 9;
    private static final int fPokazDrj = 9;
    private static final int fReka = 6;
    private static final int fDrjezdzany = 6;
    private static final int fPohonc = 3;

    private boolean ersteAusredeGesagt = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Most2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        Merkpunkt = new GenericPoint(0, 0);

        flussu = new GenericImage[8];
        mueller = new Miller(mainFrame);

        muellermorph = new Boom(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = -30;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(3);

        kutscher = new Coachman(mainFrame);

        pohoncPoint = new GenericPoint(80, 267);

        pohoncTalk = new GenericPoint();
        pohoncTalk.x = pohoncPoint.x + Coachman.Breite / 2;
        pohoncTalk.y = pohoncPoint.y - 50;

        coach = new Coach(mainFrame);

        schwein1 = new Pig1(mainFrame, new GenericPoint(40, 270));
        schwein2 = new Pig2(mainFrame, new GenericPoint(180, 250));
        schwein3 = new Pig3(mainFrame, new GenericPoint(110, 280));

        InitLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        switch (oldLocation) {
            case 0: // Einsprung fuer Load
                if (mainFrame.actions[300]) {
                    BackgroundMusicPlayer.getInstance().playTrack(20, true);
                } else {
                    BackgroundMusicPlayer.getInstance().stop();
                }
                GenericPoint tp = mainFrame.krabat.getPos();
                BorderRect TalRect = new BorderRect(400, 220, 460, 290);
                isTal = TalRect.IsPointInRect(tp);
                break;
            case 71:
                // von Doma aus - Sonderstellung, soll nix spielen
                BackgroundMusicPlayer.getInstance().stop();
                mainFrame.krabat.setPos(new GenericPoint(293, 351));
                mainFrame.krabat.SetFacing(12);
                isTal = false;
                break;
            case 82:
                // von Ralbicy aus
                BackgroundMusicPlayer.getInstance().stop();
                mainFrame.krabat.setPos(new GenericPoint(624, 384));
                mainFrame.krabat.SetFacing(9);
                isTal = false;
                backgroundSoundAus = true; // Hintergrundgeraeusche hier abschalten
                wasserAus = true;
                setAnim = true;
                TalkPause = 10;
                break;
            case 84:
                // von Sunow aus, heimgehszene, nix tun
                mainFrame.krabat.setPos(new GenericPoint(458, 226));
                mainFrame.krabat.SetFacing(6);
                isTal = true;
                break;
        }

        // Schweinehueteszene
        if (mainFrame.actions[303]) {
            setAusnahme = true;
            schweineDa = true;
        }

        // Matrix - Init
        InitMatrix();

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/most/most2.png");
        gelaend = getPicture("gfx/most/most-2.png");
        wegstueck = getPicture("gfx/most/most-3.png");
        gras = getPicture("gfx/most/mtrawa.png");

        flussu[1] = getPicture("gfx/most/flu-1.png");
        flussu[2] = getPicture("gfx/most/flu-2.png");
        flussu[3] = getPicture("gfx/most/flu-3.png");
        flussu[4] = getPicture("gfx/most/flu-4.png");
        flussu[5] = getPicture("gfx/most/flu-5.png");
        flussu[6] = getPicture("gfx/most/flu-6.png");
        flussu[7] = getPicture("gfx/most/flu-7.png");

    }

    @Override
    public void cleanup() {
        background = null;
        gelaend = null;
        wegstueck = null;
        gras = null;

        flussu[1] = null;
        flussu[2] = null;
        flussu[3] = null;
        flussu[4] = null;
        flussu[5] = null;
        flussu[6] = null;
        flussu[7] = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;

        kutscher.cleanup();
        kutscher = null;
        coach.cleanup();
        coach = null;
        schwein1.cleanup();
        schwein1 = null;
        schwein2.cleanup();
        schwein2 = null;
        schwein3.cleanup();
        schwein3 = null;
    }

    private void InitMatrix() {
        mainFrame.pathWalker.vBorders.removeAllElements();

        if (isTal) {
            // Grenzen setzen im Tal
            // Taltrapez
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(457, 459, 407, 419, 225, 284));

            // Laufmatrix anpassen
            mainFrame.pathFinder.ClearMatrix(1);

            // Zooming anpassen
            mainFrame.krabat.maxx = TAL_MAXX;
            mainFrame.krabat.minx = TAL_MINX;
            mainFrame.krabat.zoomf = TAL_ZOOMF;
            mainFrame.krabat.defScale = TAL_DEFSCALE;
        } else {
            // Grenzen setzen auf dem Berg
            // Bergtrapez
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(376, 396, 272, 342, 278, 349));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(272, 515, 218, 515, 350, 367));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(516, 358, 556, 373));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(557, 368, 609, 378));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(610, 368, 639, 402));
            mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(218, 286, 43, 182, 368, 479));

            // Laufmatrix anpassen
            mainFrame.pathFinder.ClearMatrix(6);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.pathFinder.PosVerbinden(0, 1);
            mainFrame.pathFinder.PosVerbinden(1, 2);
            mainFrame.pathFinder.PosVerbinden(2, 3);
            mainFrame.pathFinder.PosVerbinden(3, 4);
            mainFrame.pathFinder.PosVerbinden(1, 5);

            // Zooming anpassen
            mainFrame.krabat.maxx = BERG_MAXX;
            mainFrame.krabat.minx = BERG_MINX;
            mainFrame.krabat.zoomf = BERG_ZOOMF;
            mainFrame.krabat.defScale = BERG_DEFSCALE;
        }
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            mainFrame.isClipSet = true;
            mainFrame.isBackgroundAnimRunning = true;
            g.setClip(0, 0, 644, 484);
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        // Pohonc Hintergrund loeschen
        if (showPohonc) {
            g.setClip(pohoncPoint.x, pohoncPoint.y, Coachman.Breite, Coachman.Hoehe);
            g.drawImage(background, 0, 0);
        }

        // Kutsche Hintergrund loeschen
        if (kutscheda) {
            g.setClip(coach.kutscheRect());
            g.drawImage(background, 0, 0);
        }

        // Schweine Hintergrund loeschen
        if (schweineDa) {
            g.setClip(schwein1.swinoRect());
            g.drawImage(background, 0, 0);
            g.setClip(schwein2.swinoRect());
            g.drawImage(background, 0, 0);
            g.setClip(schwein3.swinoRect());
            g.drawImage(background, 0, 0);
        }

        // Animation abspielen
        switchanim = !switchanim;
        if (switchanim) {
            g.setClip(0, 0, 644, 484);
            g.drawImage(flussu[flusscount], 387, 380);
            flusscount++;
            if (flusscount == 8) {
                flusscount = 1;
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // hier ist der Sound...
        evalSound();

        // Mueller zeichnen
        if (muellerda) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);

            // Redet er etwa gerade ??
            if (TalkPerson == 36 && mainFrame.talkCount > 0) {
                mueller.talkMlynk(g);
            }

            // nur rumstehen oder laufen
            else {
                mueller.drawMlynk(g);
            }
        }

        // Schweine zeichnen, wenn da
        if (schweineDa) {
            g.setClip(schwein1.swinoRect());
            schwein1.drawSwino(g, schwein1Tanzt, backgroundSoundAus);
            g.setClip(schwein2.swinoRect());
            schwein2.drawSwino(g, schwein2Tanzt, backgroundSoundAus);
            g.setClip(schwein3.swinoRect());
            schwein3.drawSwino(g, schwein3Tanzt, backgroundSoundAus);
        }


        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
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
        if (Berglauf) {
            g.drawImage(wegstueck, 342, 270);
        }

        GenericPoint tem = mainFrame.krabat.getPos();

        if (BergTrapez.PointInside(tem)) {
            g.drawImage(gelaend, 379, 273);
        }

        // Gras zeichnen, wenn K auf die Wiese geht
        if (zeigeGras) {
            g.drawImage(gras, 188, 290);
        }

        // altes Rect retten, wird nachfolgend veraendert
        GenericRectangle rct = g.getClipBounds();

        // Kutsche zeichnen, hat Vorrang vor K
        if (kutscheda) {
            g.setClip(coach.kutscheRect());
            kutscheArbeitet = coach.drawKutsche(g, kutscheOpen);
        }

        // Pohonc zeichnen, auch Vorrang, weil auf Kutsche, aber nicht, wenn sie aus dem Rauch aufsteigt.
        if (showPohonc) {
            g.setClip(pohoncPoint.x, pohoncPoint.y, Coachman.Breite, Coachman.Hoehe);
            kutscher.drawKutser(g, TalkPerson, pohoncPoint, pohoncHoertZu);
        }

        // altes rect wiederherstellen
        g.setClip(rct);

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            if (mainFrame.actions[300]) {
                nextActionID = 1000;
            }
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

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    if (!setAusnahme) {
                        nextActionID = 500 + mainFrame.whatItem;
                    } else {
                        if (mainFrame.whatItem == 1) {
                            nextActionID = 2010;
                        } else {
                            nextActionID = 500 + mainFrame.whatItem;
                        }
                    }
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Schild Ralbitz
                if (ralbitzSchild.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 16 ? 200 : 150;
                    pTemp = Pschild;
                }

                // Ausreden fuer Schild Dresden
                if (dresdenSchild.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 16 ? 200 : 155;
                    pTemp = Pschild;
                }

                // Ausreden fuer Reka
                if (rekaRect.IsPointInRect(pTemp)) {
                    // wuda + wacki
                    nextActionID = mainFrame.whatItem == 10 ? 210 : 160;
                    pTemp = Preka;
                }

                boolean tp = TesteLauf(pTemp, nextActionID);

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (!tp) {
                    mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                }
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

                // zu Dresden gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    if (!setAusnahme) {
                        nextActionID = 60;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!untererAusgang.IsPointInRect(kt)) {
                            pTemp = Pdown;
                        } else {
                            pTemp = new GenericPoint(kt.x, Pdown.y);
                        }
                    } else {
                        nextActionID = 2000;
                        pTemp = Pdown;
                    }
                }

                // nach Sunow gehen?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    if (!setAusnahme) {
                        nextActionID = 102;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!obererAusgang.IsPointInRect(kt)) {
                            pTemp = Pup;
                        } else {
                            pTemp = new GenericPoint(kt.x, Pup.y);
                        }

                        if (mainFrame.isDoubleClick) {
                            mainFrame.krabat.StopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    } else {
                        nextActionID = 2000;
                        pTemp = Pup;
                    }
                }

                // rechter Ausgang zu Ralbicy
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    if (!setAusnahme) {
                        nextActionID = 100;
                        GenericPoint kt = mainFrame.krabat.getPos();

                        // Wenn nahe am Ausgang, dann "gerade" verlassen
                        if (!rechterAusgang.IsPointInRect(kt)) {
                            pTemp = Pright;
                        } else {
                            pTemp = new GenericPoint(Pright.x, kt.y);
                        }

                        if (mainFrame.isDoubleClick) {
                            mainFrame.krabat.StopWalking();
                            mainFrame.repaint();
                            return;
                        }
                    } else {
                        nextActionID = 2000;
                        pTemp = PrightExtra;
                    }
                }

                // Schild Ralbitz ansehen
                if (ralbitzSchild.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pschild;
                }

                // Schild Dresden ansehen
                if (dresdenSchild.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschild;
                }

                // Reka ansehen
                if (rekaRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Preka;
                }

                boolean tz = TesteLauf(pTemp, nextActionID);

                log.debug("Lauftest ergab : {}", tz);

                if (!tz) {
                    mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                }
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                nextActionID = 0;

                // ??? Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Sunow anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Ralbitz anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Schild Ralbitz mitnehmen
                if (ralbitzSchild.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    pTemp = Pschild;
                    boolean tu = TesteLauf(pTemp, nextActionID);
                    if (!tu) {
                        mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Schild Ralbitz mitnehmen
                if (dresdenSchild.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    pTemp = Pschild;
                    boolean tu = TesteLauf(pTemp, nextActionID);
                    if (!tu) {
                        mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Reka mitnehmen
                if (rekaRect.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    pTemp = Preka;
                    boolean tu = TesteLauf(pTemp, nextActionID);
                    if (!tu) {
                        mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                    }
                    mainFrame.repaint();
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
        // Wenn Animation, dann transparenter Cursor
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
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = ralbitzSchild.IsPointInRect(pTemp) || tmp.IsPointInRect(pTemp) ||
                    dresdenSchild.IsPointInRect(pTemp) || rekaRect.IsPointInRect(pTemp);

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
            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
                }
                return;
            }

            if (ralbitzSchild.IsPointInRect(pTemp) || dresdenSchild.IsPointInRect(pTemp) ||
                    rekaRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 5;
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

    // Erkennungsroutine, ob Animationsmodus eingeschaltet werden muss
    private boolean TesteLauf(GenericPoint pTxxx, int Action) {
        // hier mit "false" zurueckspringen, wenn die Ausnahmeszene ist, da soll Krabat nicht runtergehen koennen
        if (setAusnahme) {
            return false;
        }

        GenericPoint kpos = mainFrame.krabat.getPos();

        // Hier Punkt klonen, damit alter Punkt erhalten bleibt
        GenericPoint pTemp = new GenericPoint(pTxxx.x, pTxxx.y);

        log.debug("Es wird getestet.");

        // vom Tal auf den Berg???
        if (isTal && pTemp.y > 277) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 600;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor dem Verschwinden berechnen
            GenericPoint rand = TalTrapez.Punkte(kpos.y);
            log.debug(" links aktuell rechts {} {} {}", rand.x, kpos.x, rand.y);
            pTemp.y = TalTrapez.y2;
            float t1 = kpos.x - rand.x;
            float t2 = rand.y - rand.x;
            float teil = t1 / t2;
            pTemp.x = TalTrapez.x3 + (int) ((TalTrapez.x4 - TalTrapez.x3) * teil);

            log.debug("Mittenfaktor {}", teil);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (BergTrapez.x1 + (BergTrapez.x2 - BergTrapez.x1) * teil), BergTrapez.y1);
            Wendepunkt = new GenericPoint((pTemp.x + Endpunkt.x) / 2, 380);

            mainFrame.pathWalker.SetzeWegOhneStand(pTemp);

            log.debug(" Startpunkt {} {}", pTemp.x, pTemp.y);

            mainFrame.repaint();

            log.debug(" Wendepunkt Endpunkt {} {} {} {}", Wendepunkt.x, Wendepunkt.y, Endpunkt.x, Endpunkt.y);

            return true;
        }

        // vom Berg ins Tal ??
        if (!isTal && pTemp.y < 278) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 610;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor Verschwinden berechnen
            GenericPoint raud = BergTrapez.Punkte(kpos.y);

            log.debug(" links aktuell rechts {} {} {}", raud.x, kpos.x, raud.y);

            pTemp.y = BergTrapez.y1;
            float t3 = kpos.x - raud.x;
            float t4 = raud.y - raud.x;
            float teal = t3 / t4;

            log.debug(" Mittenfaktor {}", teal);

            if (BergTrapez.PointInside(mainFrame.krabat.getPos())) {
                pTemp.x = BergTrapez.x1 + (int) ((BergTrapez.x2 - BergTrapez.x1) * teal);
            } else {
                // Default - Werte fuer Tallauf, wenn noch zu weit weg
                pTemp = new GenericPoint((BergTrapez.x1 + BergTrapez.x2) / 2, BergTrapez.y1);
                teal = 0.5f;
            }

            log.debug(" Mittenfaktor neu {}", teal);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (TalTrapez.x3 + (TalTrapez.x4 - TalTrapez.x3) * teal), TalTrapez.y2);
            Wendepunkt = new GenericPoint((pTemp.x + Endpunkt.x) / 2, 380);

            mainFrame.pathWalker.SetzeWegOhneStand(pTemp);

            log.debug(" Startpunkt {} {}", pTemp.x, pTemp.y);

            mainFrame.repaint();

            log.debug(" Wendepunkt Endpunkt {} {} {} {}", Wendepunkt.x, Wendepunkt.y, Endpunkt.x, Endpunkt.y);

            return true;
        }
        return false;
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

    private void evalSound() {
        if (wasserAus) {
            return; // wenn nix mehr gespielt werden soll, dann exit
        }

        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 4.99);
            zwzfz += 49;

            mainFrame.soundPlayer.PlayFile("sfx/recka" + (char) zwzfz + ".wav");
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
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 520) {
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
                // Schild Ralbitz anschauen
                KrabatSagt("Most2_1", fPokazRal, 3, 0, 0);
                break;

            case 2:
                // Schild Dresden anschauen
                KrabatSagt("Most2_2", fPokazDrj, 3, 0, 0);
                break;

            case 3:
                // Reka anschauen
                KrabatSagt("Most2_3", fReka, 3, 0, 0);
                break;

            case 50:
                // Schild Ralbitz mitnehmen
                KrabatSagt("Most2_4", fPokazRal, 3, 0, 0);
                break;

            case 55:
                // Schild Dresden mitnehmen
                KrabatSagt("Most2_5", fPokazDrj, 3, 0, 0);
                break;

            case 60:
                // Nach Dresden gehen
                KrabatSagt("Most2_6", fDrjezdzany, 3, 0, 0);
                break;

            case 70:
                // Reka mitnehmen
                // Zufallszahl 0 bis 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Most2_7", fReka, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Most2_8", fReka, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // Gehe zu Ralbicy
                NeuesBild(82, 89);
                break;

            case 102:
                // nach Sunow gehen 
                NeuesBild(84, 89);
                break;

            case 150:
                // Schild - Ausreden Ralbitz
                DingAusrede(fPokazRal);
                break;

            case 155:
                // Schild - Ausreden Ralbitz
                DingAusrede(fPokazDrj);
                break;

            case 160:
                // Ausrede Wasser
                DingAusrede(fReka);
                break;

            case 200:
                // kamuski auf schild
                KrabatSagt("Most2_9", fPokazRal, 3, 0, 0);
                break;

            case 210:
                // Wuda + wacki auf Wasser
                KrabatSagt("Most2_10", fReka, 3, 0, 0);
                break;

            case 520:
                // Karten - Extraausrede
                int zd = (int) Math.round(Math.random() * (TEXTS.length - 1));
                if (!ersteAusredeGesagt) {
                    ersteAusredeGesagt = true;
                    zd = 0;
                }
                KrabatSagt(TEXTS[zd], 0, 3, 0, 0);
                break;

            case 600:
                // vom Tal auf den Berg laufen
                Berglauf = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Wendepunkt);
                nextActionID = 601;
                break;

            case 601:
                // beim Lauf Tal auf Berg wieder zum Vorschein kommen
                mainFrame.krabat.maxx = BERG_MAXX;
                mainFrame.krabat.minx = BERG_MINX;
                mainFrame.krabat.defScale = BERG_DEFSCALE;
                mainFrame.krabat.zoomf = BERG_ZOOMF;
                mainFrame.pathWalker.SetzeGarantiertWegFalsch(Endpunkt);
                nextActionID = 620;
                break;

            case 610:
                // vom Berg ins Tal laufen invertiert
                Berglauf = true;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.SetzeGarantiertWegFalsch(Wendepunkt);
                nextActionID = 611;
                break;

            case 611:
                // beim Lauf Berg ins Tal wieder zum Vorschein kommen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Endpunkt);
                mainFrame.krabat.maxx = TAL_MAXX;
                mainFrame.krabat.defScale = TAL_DEFSCALE;
                mainFrame.krabat.zoomf = TAL_ZOOMF;
                mainFrame.krabat.minx = TAL_MINX;
                nextActionID = 620;
                break;

            case 620:
                // Laufen beenden und alles wieder auf Normal zuruecksetzen
                mainFrame.isAnimRunning = false;
                Berglauf = false;
                Cursorform = 200;
                evalMouseMoveEvent(mainFrame.mousePoint);
                isTal = !isTal;
                InitMatrix();
                nextActionID = oldActionID;
                mainFrame.pathWalker.SetzeNeuenWeg(Merkpunkt);
                mainFrame.repaint();
                break;

            case 1000:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 120);  // 68 - 100 - scaleMueller
                ismuellermorphing = true;
                nextActionID = 1003;
                break;

            case 1003:
                // Mueller erscheint
                if (muellermorphcount < 3) {
                    break;
                }
                muellerda = true;
                nextActionID = 1008;
                break;

            case 1008:
                // Mueller sagt seinen Spruch
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                mainFrame.isClipSet = false;
                MuellerMecker(mueller.evalMlynkTalkPoint());
                TalkPerson = 36;
                TalkPause = 5;
                nextActionID = 1010;
                break;

            case 1010:
                // Gehe zu Muehle zurueck
                NeuesBild(90, 89);
                break;

            case 2000:
                // Ausreden, wenn Verlassen versucht
                int zr = (int) Math.round(Math.random() * (TEXTS.length - 1));
                if (!ersteAusredeGesagt) {
                    ersteAusredeGesagt = true;
                    zr = 0;
                }
                KrabatSagt(TEXTS[zr], 0, 3, 0, 0);
                break;

            case 2010:
                // ab ins Gebuesch gehen -> Beginn Anim DD
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                backgroundSoundAus = true;
                mainFrame.pathWalker.SetzeNeuenWeg(PvorGras);
                nextActionID = 2011;
                break;

            case 2011:
                // uebers Gras laufen
                zeigeGras = true;
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(PnachGras);
                nextActionID = 2012;
                break;

            case 2012:
                // und hin zur Floetenpos
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(PvorFloete);
                nextActionID = 2013;
                break;

            case 2013:
                // und genau hin
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(Pfloete);
                nextActionID = 2015;
                break;

            case 2015:
                // Ab nach DD
                BackgroundMusicPlayer.getInstance().playTrack(11, false);
                mainFrame.krabat.nAnimation = 157;
                mainFrame.isInventoryCursor = false;
                Counter = 100;
                nextActionID = 2017;
                break;

            case 2017:
                // Schweine fangen an zu tanzen
                if (Counter < 60) {
                    schwein1Tanzt = true;
                }
                if (Counter < 40) {
                    schwein2Tanzt = true;
                }
                if (Counter < 20) {
                    schwein3Tanzt = true;
                }
                if (--Counter > 1) {
                    break;
                }
                Counter = 190;
                nextActionID = 2020;
                break;

            case 2020:
                // Kutsche hinten Init und los gehts...
                if (Counter == 100) {
                    coach.InitKutsche(0);
                    kutscheda = true;
                    kutscheArbeitet = 1;
                }
                if (--Counter > 1) {
                    break;
                }
                // System.out.println ("Floete gestoppt...");
                mainFrame.krabat.StopAnim();
                nextActionID = 2030;
                break;

            case 2030:
                // warten bis Ende 1. Anim
                if (kutscheArbeitet != 0) {
                    break;
                }
                kutscheda = false;
                wasserAus = true; // ab hier auch Recka aus
                Counter = 30;
                nextActionID = 2035;
                break;

            case 2035:
                // Warteschleife runterzaehlen und dann oben Kutsche zeigen
                if (--Counter > 1) {
                    break;
                }
                kutscheda = true;
                coach.InitKutsche(1);
                kutscheArbeitet = 1;
                nextActionID = 2040;
                break;

            case 2040:
                // warten bis Ende 2. Anim
                if (kutscheArbeitet != 10) {
                    break;
                }
                PersonSagt("Most2_11", fPohonc, 39, 2, 2500, pohoncBrrr);
                break;

            case 2500:
                // Pohonc zeigen
                if (kutscheArbeitet != 0) {
                    break;
                }
                schwein1Tanzt = false;
                schwein2Tanzt = false;
                schwein3Tanzt = false;
                coach.InitKutsche(2);
                mainFrame.isClipSet = false;
                nextActionID = 2520;
                break;

            case 2520:
                // Krabat spricht
                if (kutscheArbeitet != 0) {
                    break;
                }
                showPohonc = true;
                pohoncHoertZu = true;
                KrabatSagt("Most2_12", 0, 1, 0, 2530);
                break;

            case 2530:
                // Pohonc spricht
                PersonSagt("Most2_13", 0, 39, 2, 2540, pohoncTalk);
                break;

            case 2540:
                // Krabat spricht
                KrabatSagt("Most2_14", 0, 1, 2, 2550);
                break;

            case 2550:
                // Pohonc spricht
                PersonSagt("Most2_15", 0, 39, 2, 2560, pohoncTalk);
                break;

            case 2560:
                // Krabat spricht
                KrabatSagt("Most2_16", 0, 1, 2, 2570);
                break;

            case 2570:
                // Pohonc spricht
                PersonSagt("Most2_17", 0, 39, 2, 2580, pohoncTalk);
                break;

            case 2580:
                // Krabat spricht
                KrabatSagt("Most2_18", 0, 1, 2, 2590);
                break;

            case 2590:
                // Pohonc spricht
                PersonSagt("Most2_19", 0, 39, 2, 2600, pohoncTalk);
                break;

            case 2600:
                // Krabat geht vor die Kutsche
                kutscheOpen = true;
                mainFrame.soundPlayer.PlayFile("sfx/kdurjeauf.wav");
                pohoncHoertZu = false;
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(kVorKutsche);
                nextActionID = 2605;
                break;

            case 2605:
                // K spricht
                KrabatSagt("Most2_20", 0, 1, 2, 2610);
                break;

            case 2610:
                // Kral spricht
                PersonSagt("Most2_21", 0, 40, 2, 2620, kralTalk);
                break;

            case 2620:
                // Krabat spricht
                KrabatSagt("Most2_22", 0, 1, 2, 2630);
                break;

            case 2630:
                // Kral spricht
                PersonSagt("Most2_23", 0, 40, 2, 2640, kralTalk);
                break;

            case 2640:
                // Kral spricht
                mainFrame.soundPlayer.PlayFile("sfx/kdurjezu.wav");
                kutscheOpen = false;
                PersonSagt("Most2_24", 0, 40, 2, 2650, kralTalk);
                break;

            case 2650:
                // Kutsche faehrt weg...ausfaden
                FadeToBlack = 1;
                nextActionID = 2655;
                break;

            case 2655:
                // bis zum Ende warten
                FadeToBlack += 3;
                if (FadeToBlack >= 240) {
                    nextActionID = 2658;
                }
                break;

            case 2658:
                // ein Frame repaint schinden, damit Screen vor Load leer
                nextActionID = 2660;
                break;

            case 2660:
                // Skip zum Bild Kutsche
                NeuesBild(94, 89);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}