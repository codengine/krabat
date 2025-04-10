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
import de.codengine.krabat.anims.Bumm;
import de.codengine.krabat.anims.Mlynk2;
import de.codengine.krabat.anims.Oldmlynk;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Rowy extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Rowy.class);
    private GenericImage background;
    private GenericImage backgroundBright;
    private GenericImage grab;
    private GenericImage imSargOffen;
    private final GenericImage[] krabat_aufstehen = new GenericImage[4];
    private final GenericImage[] krabat_anzuenden = new GenericImage[3];
    private GenericImage krabat_grab_oeffnen;
    private GenericImage krabat_links_zucken;
    private GenericImage krabat_links_stehen;
    private final GenericImage[] krabat_links_back = new GenericImage[4];
    private final GenericImage[] fledermaus = new GenericImage[4];
    private final GenericImage[] fledermausSitzen = new GenericImage[3];
    private final GenericImage[] schaf = new GenericImage[3];
    private GenericImage imSchaale;
    private GenericImage imStroh;
    private GenericImage imKorraktor;
    private final GenericImage[] imFeuer = new GenericImage[11];
    private final GenericImage[] imSwaixtixKippen = new GenericImage[6];
    private GenericImage imOhneSwaixtix;
    private final GenericImage[] muellerExplosion = new GenericImage[11];

    private final Oldmlynk alterMueller;
    private boolean alterMuellerda = false;
    private final Mlynk2 mueller;
    private boolean muellerTalkWithStock = false;
    private int muellerTalkCounter = 0;
    private boolean muellerda = false; // Achtung !!!!!!!!!! Muss wg. Load noch berechnet werden !!!
    private boolean walkReady = true;

    // Vars fuer die Backgroundanim
    private int AnimID = 10;
    private int AnimCounter = 0;
    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos = new GenericPoint(0, 0);
    private int AnimTalkPerson = 0;
    private boolean animActive = false;

    private final GenericPoint muellerPoint;

    // Variablen fuer Morphing-Animationen
    private final Bumm muellerMorph;
    private int muellerMorphCount = 0;
    private boolean isMuellerMorphing = false;

    // konstante Rects
    private static final Borderrect horncy =
            new Borderrect(551, 391, 639, 479);
    private static final Borderrect swaixtix =
            new Borderrect(144, 254, 192, 396);
    private static final Borderrect swaixtixArm =
            new Borderrect(124, 255, 141, 290);
    private static final Borderrect swaixtixGefallen =
            new Borderrect(16, 320, 160, 380);
    private static final Borderrect syno =
            new Borderrect(32, 434, 115, 479);
    private static final Borderrect brMueller =
            new Borderrect(268, 323, 298, 413);

    // alle Graeber (von links oben nach rechts unten)
    private static final Borderrect brGrab1 =
            new Borderrect(228, 368, 253, 396);
    private static final Borderrect brGrab2 =
            new Borderrect(280, 376, 325, 400);
    private static final Borderrect brGrab3 = // (dieses Grab wird geoeffnet !)
            new Borderrect(335, 391, 398, 416);
    private static final Borderrect brGrab4 =
            new Borderrect(356, 425, 470, 442);

    // konstante Points
    private static final GenericPoint Phorncy = new GenericPoint(520, 420);
    private static final GenericPoint Pswaixtix = new GenericPoint(175, 430);
    private static final GenericPoint PswaixtixArm = new GenericPoint(130, 395);
    private static final GenericPoint Psyno = new GenericPoint(152, 455);
    private static final GenericPoint muellerFeet = new GenericPoint(291, 420);
    private static final GenericPoint pTalkToMueller = new GenericPoint(195, 430);
    private static final GenericPoint pTouchMueller = new GenericPoint(242, 435);
    private static final GenericPoint pGrab1 = new GenericPoint(236, 430);
    private static final GenericPoint pGrab2 = new GenericPoint(295, 430);
    private static final GenericPoint pGrab3 = new GenericPoint(320, 430);
    private static final GenericPoint pGrab4 = new GenericPoint(330, 438);

    // konstante ints
    private static final int fHorncy = 3;
    private static final int fSwaixtix = 12;
    private static final int fSyno = 9;
    private static final int Muellerzooming = 0;
    private static final int fMueller = 3;
    private static final int fAufstehen = 6;
    // private static final int fGrab1 = 12;
    // private static final int fGrab2 = 12;
    // private static final int fGrab3 = 3;
    // private static final int fGrab4 = 3;

    // String fuer Muellerdrohen
    private static final String[] MILLER_THREATENS = {
            "Rowy_54", "Rowy_55", "Rowy_56", "Rowy_57", "Rowy_58", "Rowy_59"
    };

    private int SonderAnim = 0;
    private int Counter = 0;
    private int sonderAnimCounter = 0;
    private boolean switchAnim = false;
    private int feuerAnimCount = 0;
    private int facingOnGraves = 12;
    private boolean zeigeGrab = false;

    private int fledermausActionID = 0;
    private final static GenericPoint fledermausStartPos = new GenericPoint(265, 325);
    private final static GenericPoint fledermausSitzPos = new GenericPoint(532, 195);
    private final static GenericPoint fledermausEndPos = new GenericPoint(265, 350);
    private final GenericPoint fledermausCurrentPos = new GenericPoint(265, 325);
    private int fledermausCounter = 0;
    private int fledermausQuasselFrame = 0;

    private boolean isSchafDa = false;
    private final static GenericPoint schafPosLO = new GenericPoint(426, 395);
    private int schafCounter = 1;
    private int schafMeckerFrame = 0;
    private boolean schnauzeSchaf = false;

    private boolean muellerExplodiert = false;
    private int muellerExplosionCounter = 0;

    private boolean initSound = true;
    private boolean swaixtixSound = false;

    private boolean istMuellerSchonTot = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Rowy(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(17, true);

        mainFrame.krabat.maxx = 430;
        mainFrame.krabat.zoomf = 4.5f;
        mainFrame.krabat.defScale = -25;  // -45 war zu gross (Jan)

        mueller = new Mlynk2(mainFrame);
        mueller.maxx = 0;
        mueller.zoomf = 1f;
        mueller.defScale = 0;

        mueller.setPos(muellerFeet);
        mueller.SetFacing(9);

        alterMueller = new Oldmlynk(mainFrame, Muellerzooming, true);

        muellerMorph = new Bumm(mainFrame);

        muellerPoint = new GenericPoint();
        muellerPoint.x = muellerFeet.x - Oldmlynk.Breite / 2;
        muellerPoint.y = muellerFeet.y - Oldmlynk.Hoehe;

        InitLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        initLocationWalkingRects();
        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // Berechnen, ob Mueller da ist
                muellerda = mainFrame.actions[985];
                if (muellerda) {
                    animActive = true;
                }
                break;
            case 201:
                // von Hrodz aus
                mainFrame.krabat.setPos(new GenericPoint(475, 455));
                mainFrame.krabat.SetFacing(6);
                // hier sofort Aufstehsequenz abspielen
                mainFrame.isAnimRunning = true;
                initSound = false; // nur hier darf der Sound gespielt werden
                nextActionID = 100;
                break;
        }
    }

    // begehbare Breiche initialisieren
    public void initLocationWalkingRects() {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(
                new Bordertrapez(112, 330, 175, 330, 430, 479));
        mainFrame.pathWalker.vBorders.addElement(
                new Bordertrapez(331, 454, 494, 456));
        mainFrame.pathWalker.vBorders.addElement(
                new Bordertrapez(495, 410, 525, 479));
        mainFrame.pathWalker.vBorders.addElement(
                new Bordertrapez(432, 505, 432, 525, 383, 409));

        // wenn Swaixtix noch nicht umgefallen -> Treppe (5. Trapez) noch da
        if (!mainFrame.actions[986]) {
            mainFrame.pathFinder.ClearMatrix(5);
        } else {
            mainFrame.pathFinder.ClearMatrix(4);
        }

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);

        // wenn Swaixtix noch nicht umgefallen -> Treppe (5. Trapez) hinzufuegen
        if (!mainFrame.actions[986]) {
            mainFrame.pathWalker.vBorders.addElement(
                    new Bordertrapez(130, 131, 112, 114, 395, 429));

            mainFrame.pathFinder.PosVerbinden(0, 4);
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/wotrow/rowy1.png");
        backgroundBright = getPicture("gfx/wotrow/rowy2.png");
        grab = getPicture("gfx/wotrow/sarg.png");
        imSargOffen = getPicture("gfx/wotrow/rsarg.png");
        imSchaale = getPicture("gfx/wotrow/rskla.png");
        imStroh = getPicture("gfx/wotrow/rstroh.png");
        imKorraktor = getPicture("gfx/wotrow/rkniha.png");

        krabat_aufstehen[0] = getPicture("gfx/anims/k-u-takeunten.png");
        krabat_aufstehen[1] = getPicture("gfx/anims/k-u-10.png");
        krabat_aufstehen[2] = getPicture("gfx/wotrow/k-u-lookleft.png");
        krabat_aufstehen[3] = getPicture("gfx/wotrow/k-u-lookright.png");
        krabat_anzuenden[0] = getPicture("gfx/wotrow/k-o-kamj1.png");
        krabat_anzuenden[1] = getPicture("gfx/wotrow/k-o-kamj2.png");
        krabat_anzuenden[2] = getPicture("gfx/wotrow/k-o-kamj3.png");
        krabat_grab_oeffnen = getPicture("gfx/anims/k-r-takeunten.png");
        krabat_links_back[0] = getPicture("gfx/wotrow/k-l-lb1.png");
        krabat_links_back[1] = getPicture("gfx/wotrow/k-l-lb2.png");
        krabat_links_back[2] = getPicture("gfx/wotrow/k-l-lb3.png");
        krabat_links_back[3] = getPicture("gfx/wotrow/k-l-lb4.png");
        krabat_links_zucken = getPicture("gfx/wotrow/k-l-zucken.png");
        krabat_links_stehen = getPicture("gfx/anims/k-l-10.png");

        fledermaus[0] = getPicture("gfx/wotrow/fmaus1a.png");
        fledermaus[1] = getPicture("gfx/wotrow/fmaus2a.png");
        fledermaus[2] = getPicture("gfx/wotrow/fmaus1.png");
        fledermaus[3] = getPicture("gfx/wotrow/fmaus2.png");

        fledermausSitzen[0] = getPicture("gfx/wotrow/fmaus3.png");
        fledermausSitzen[1] = getPicture("gfx/wotrow/fmaus3a.png");
        fledermausSitzen[2] = getPicture("gfx/wotrow/fmaus3b.png");

        schaf[0] = getPicture("gfx/wotrow/rschaf.png");
        schaf[1] = getPicture("gfx/wotrow/rschaf2.png");
        schaf[2] = getPicture("gfx/wotrow/rschaf3.png");

        imFeuer[0] = getPicture("gfx/wjes/wn0.png");
        imFeuer[1] = getPicture("gfx/wjes/wn1.png");
        imFeuer[2] = getPicture("gfx/wjes/wn2.png");
        imFeuer[3] = getPicture("gfx/wjes/wn3.png");
        imFeuer[4] = getPicture("gfx/wjes/wn4.png");
        imFeuer[5] = getPicture("gfx/wjes/wn5.png");
        imFeuer[6] = getPicture("gfx/wjes/wn6.png");
        imFeuer[7] = getPicture("gfx/wjes/wn7.png");
        imFeuer[8] = getPicture("gfx/wjes/wn8.png");
        imFeuer[9] = getPicture("gfx/wjes/wn9.png");
        imFeuer[10] = getPicture("gfx/wjes/wn10.png");

        imSwaixtixKippen[0] = getPicture("gfx/wotrow/sw1.png");
        imSwaixtixKippen[1] = getPicture("gfx/wotrow/sw2.png");
        imSwaixtixKippen[2] = getPicture("gfx/wotrow/sw3.png");
        imSwaixtixKippen[3] = getPicture("gfx/wotrow/sw4.png");
        imSwaixtixKippen[4] = getPicture("gfx/wotrow/sw6.png");
        imSwaixtixKippen[5] = getPicture("gfx/wotrow/sw5.png");
        imOhneSwaixtix = getPicture("gfx/wotrow/rback.png");

        muellerExplosion[0] = getPicture("gfx/wotrow/rml1.png");
        muellerExplosion[1] = getPicture("gfx/wotrow/rml2.png");
        muellerExplosion[2] = getPicture("gfx/wotrow/rml3.png");
        muellerExplosion[3] = getPicture("gfx/wotrow/rml4.png");
        muellerExplosion[4] = getPicture("gfx/wotrow/rml5.png");
        muellerExplosion[5] = getPicture("gfx/wotrow/rml6.png");
        muellerExplosion[6] = getPicture("gfx/wotrow/rml7.png");
        muellerExplosion[7] = getPicture("gfx/wotrow/rml8.png");
        muellerExplosion[8] = getPicture("gfx/wotrow/rml9.png");
        muellerExplosion[9] = getPicture("gfx/wotrow/rml10.png");
        muellerExplosion[10] = getPicture("gfx/wotrow/rml11.png");

    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Sound bei init
        if (!initSound) {
            initSound = true;
            mainFrame.soundPlayer.PlayFile("sfx/schildlegen.wav");
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

        // Sonderbild "Grab" oder normale Location malen ?
        if (zeigeGrab) {
            g.drawImage(grab, 0, 0);

            // Schrei des Schreckens zeigen ?
            if (sonderAnimCounter < 24) {
                mainFrame.imageFont.drawString(g, Start.stringManager.getTranslation("Rowy_60"), 210, 230, FarbenArray[1]);
            }
        } else {
            // normaler Ablauf -> Hintergrund und Krabat zeichnen

            // wenn Swaixtix brennt -> hellen Hintergrund zeichnen
            if (!mainFrame.actions[984]) {
                g.drawImage(background, 0, 0);
            } else {
                g.drawImage(backgroundBright, 0, 0);
            }

            // hat Kabat schon Schaale (982) und Stroh (983) hingelegt ?
            if (mainFrame.actions[982] || mainFrame.actions[983]) {
                g.setClip(122, 260, 22, 20);
                if (mainFrame.actions[982]) {
                    g.drawImage(imSchaale, 123, 261);
                }
                if (mainFrame.actions[983]) {
                    g.drawImage(imStroh, 123, 261);
                }
            }

            // ist 3.Grab bereits offen ?
            if (mainFrame.actions[987]) {
                g.setClip(312, 376, 117, 54);
                g.drawImage(imSargOffen, 313, 377);
            }

            // ist Swaixtix umgefallen -> mit Hintergrund ueberpinseln !
            if (mainFrame.actions[986]) {
                g.setClip(0, 244, 251, 177);
                g.drawImage(imOhneSwaixtix, 0, 244);

                // liegt der Korraktor noch auf dem Swaixtix-Sockel ? 
                if (!mainFrame.actions[985]) {
                    g.drawImage(imKorraktor, 145, 387);
                }

                // Umfall-Animation abspielen ???
                if (SonderAnim == 5) {
                    if (sonderAnimCounter > 10) {
                        g.drawImage(imSwaixtixKippen[0], 103, 248);
                        g.drawImage(imFeuer[0], 101, 252);
                    } else {
                        if (sonderAnimCounter > 8) {
                            g.drawImage(imSwaixtixKippen[1], 81, 250);
                            g.drawImage(imFeuer[0], 78, 257);
                        } else {
                            if (sonderAnimCounter > 6) {
                                g.drawImage(imSwaixtixKippen[2], 62, 260);
                                g.drawImage(imFeuer[0], 60, 267);
                            } else {
                                if (sonderAnimCounter > 4) {
                                    g.drawImage(imSwaixtixKippen[3], 41, 270);
                                    g.drawImage(imFeuer[0], 38, 283);
                                } else {
                                    if (sonderAnimCounter > 2) {
                                        g.drawImage(imSwaixtixKippen[4], 22, 282);
                                        g.drawImage(imFeuer[0], 23, 300);
                                    } else {
                                        g.drawImage(imSwaixtixKippen[5], 7, 286);
                                        g.drawImage(imFeuer[0], 8, 331);
                                        if (!swaixtixSound) {
                                            swaixtixSound = true;
                                            mainFrame.soundPlayer.PlayFile("sfx-dd/schlag.wav");
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    g.drawImage(imSwaixtixKippen[5], 7, 286);
                }
            }

            // brennt Swajxtix's Fote ? -> Feueranimation abspielen (nicht bei Swaixtix-Umkippen)
            if (mainFrame.actions[984] && SonderAnim != 5) {
                switchAnim = !switchAnim;
                if (switchAnim) {
                    feuerAnimCount++;
                    if (feuerAnimCount == 11) {
                        feuerAnimCount = 0;
                    }
                }
                // Feuer an welcher Position (steht/liegt Swaixtix ?) ?
                if (!mainFrame.actions[986]) {
                    g.setClip(120, 248, 26, 26);
                    g.drawImage(imFeuer[feuerAnimCount], 120, 248);
                } else {
                    g.setClip(8, 331, 26, 26);
                    g.drawImage(imFeuer[feuerAnimCount], 8, 331);
                }
            }

            // Morphing-Rauchanimationen abspielen ?
            if (isMuellerMorphing) {
                // -> dann zeichne jetzt Hintergrund neu, bevor Figuren draufkommen
                g.setClip(muellerMorph.bummRect());
                drawSpecialBackgrounds(g);
            }

            // Mueller Hintergrund loeschen
            if (muellerda) {
                // Clipping - Rectangle feststellen und setzen
                Borderrect temp;
                if (mueller.GetFacing() == 3) {
                    temp = mueller.MlynkRectMitStockAndersrum();
                } else {
                    temp = mueller.MlynkRectMitStock();
                }
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 20,
                        temp.ru_point.y - temp.lo_point.y + 20);
                drawSpecialBackgrounds(g);
                // System.out.println ("Loesch-Koordinaten: " + temp.lo_point.x + " " + temp.lo_point.y + " bis " + temp.ru_point.x + " " + temp.ru_point.y);
            }

            // Mueller bewegen
            if (muellerda && !walkReady) {
                // Mueller um 1 Schritt weiterbewegen (nur virtuell)
                walkReady = mueller.Move();
            }

            // Mueller zeichnen
            if (muellerda) {
                // Facing checken (nach links oder rechts glotzen, je nach Krabat)
                Borderrect temp;
                if (mainFrame.krabat.getPos().x > muellerFeet.x) {
                    mueller.SetFacing(3);
                    temp = mueller.MlynkRectMitStockAndersrum();
                } else {
                    mueller.SetFacing(9);
                    temp = mueller.MlynkRectMitStock();
                }

                // Clipping - Rectangle feststellen und setzen (30 Pixel wegen Kij dazu (SS))
                g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10,
                        temp.ru_point.x - temp.lo_point.x + 20,
                        temp.ru_point.y - temp.lo_point.y + 20);
                // System.out.println ("Zeichen-Koordinaten: " + temp.lo_point.x + " " + temp.lo_point.y + " bis " + temp.ru_point.x + " " + temp.ru_point.y);

                // Zeichne ihn jetzt

                // Redet er etwa gerade ??
                if (TalkPerson == 36 && mainFrame.talkCount > 0 ||
                        !Objects.equals(AnimOutputText, "")) {
                    // Nach 4 Frames erneut entscheiden, ob Mueller mit oder ohne Stock redet
                    if (muellerTalkCounter++ % 4 == 0) {
                        muellerTalkWithStock = (int) (Math.random() * 2) != 0;
                    }

                    // Mueller redet (mit oder ohne Stock ?)
                    if (!muellerTalkWithStock) {
                        mueller.talkMlynk(g);
                    } else {
                        // bei "mit Stock reden" Richtung beachten !
                        if (mueller.GetFacing() == 9) {
                            mueller.talkMlynkWithKij(g);
                        } else {
                            mueller.talkMlynkWithKijAndersrum(g, true);  // STock ist immer oben
                        }
                    }
                }
                // nur rumstehen oder laufen
                else {
                    if (!istMuellerSchonTot) {
                        mueller.drawMlynk(g);
                    } else // mit Zwinkern aufhoeren
                    {
                        mueller.drawMlynkWithoutZwinkern(g);
                    }
                }
            }

            // alten Mueller zeichnen, wenn er da ist
            if (alterMuellerda) {
                g.setClip(muellerPoint.x, muellerPoint.y,
                        Oldmlynk.Breite, Oldmlynk.Hoehe);
                drawSpecialBackgrounds(g);

                alterMueller.drawOldmlynk(g, TalkPerson, muellerPoint);
            }

            // explodiert der Mueller zur Zeit ?
            if (muellerExplodiert) {
                g.setClip(muellerPoint.x, muellerPoint.y,
                        Oldmlynk.Breite, Oldmlynk.Hoehe);
                drawSpecialBackgrounds(g);

                if (muellerExplosionCounter < muellerExplosion.length * 2) {
                    g.drawImage(muellerExplosion[muellerExplosionCounter++ / 2],
                            muellerPoint.x, muellerPoint.y,
                            Oldmlynk.Breite, Oldmlynk.Hoehe);
                } else {
                    muellerExplodiert = false;
                }
            }

            // ist Fledermaus da ?
            if (fledermausActionID > 0) {
                // clip setzen und hintergrund ueberpinseln
                g.setClip(fledermausCurrentPos.x - 2, fledermausCurrentPos.y - 2, 44, 34);
                g.drawImage(backgroundBright, 0, 0, 640, 480);

                switch (fledermausActionID) {
                    case 1:
                        // auf Kopfehoehe des Muellers flattern (vor Hochfliegen)
                        g.drawImage(fledermaus[fledermausCounter++ % 2],
                                fledermausCurrentPos.x, fledermausCurrentPos.y);
                        break;

                    case 2: // zur Decke hinauffliegen
                        // X-Verschiebung fuer Anim-Step berechnen
                        float factorHoch = (fledermausSitzPos.x - fledermausStartPos.x)
                                / (float) (fledermausStartPos.y - fledermausSitzPos.y);
                        int tempXDistHoch = (int) ((fledermausStartPos.y - fledermausCurrentPos.y) * factorHoch);
                        fledermausCurrentPos.x = fledermausStartPos.x + tempXDistHoch;

                        g.drawImage(fledermaus[fledermausCounter++ % 2],
                                fledermausCurrentPos.x, fledermausCurrentPos.y);

                        if (fledermausCurrentPos.y >= fledermausSitzPos.y) {
                            fledermausCurrentPos.y -= 2;
                        } else {
                            fledermausActionID = 3;
                        }
                        break;

                    case 3: // an Decke haengen
                        g.drawImage(fledermausSitzen[0], fledermausSitzPos.x, fledermausSitzPos.y);
                        break;

                    case 4: // an Decke haengen und reden (1 von den 3 QuasselFrames zeigen)
                        g.drawImage(fledermausSitzen[fledermausQuasselFrame], fledermausSitzPos.x, fledermausSitzPos.y);
                        if (fledermausCounter++ % 3 == 0) {
                            fledermausQuasselFrame = (int) (Math.random() * 3);
                        }
                        break;

                    case 5: // von Decke auf Kopfehoehe runterfliegen
                        // X-Verschiebung fuer Anim-Step berechnen
                        float factorRunter = (fledermausSitzPos.x - fledermausEndPos.x)
                                / (float) (fledermausEndPos.y - fledermausSitzPos.y);
                        int tempXDistRunter = (int) ((fledermausEndPos.y - fledermausCurrentPos.y) * factorRunter);
                        fledermausCurrentPos.x = fledermausEndPos.x + tempXDistRunter;

                        g.drawImage(fledermaus[fledermausCounter++ % 2 + 2],
                                fledermausCurrentPos.x, fledermausCurrentPos.y);

                        if (fledermausCurrentPos.y <= fledermausEndPos.y) {
                            fledermausCurrentPos.y += 2;
                        } else {
                            fledermausActionID = 6;
                        }
                        break;

                    case 6:
                        // auf Kopfehoehe des Muellers flattern (nach Hochfliegen)
                        g.drawImage(fledermaus[fledermausCounter++ % 2 + 2],
                                fledermausCurrentPos.x, fledermausCurrentPos.y);
                        break;
                }
            }

            // ist Schaf da ?
            if (isSchafDa) {
                // clip setzen und hintergrund ueberpinseln
                g.setClip(schafPosLO.x - 1, schafPosLO.y - 1, 120 + 2, 75 + 2);
                g.drawImage(backgroundBright, 0, 0, 640, 480);
                // Schaf malen
                g.drawImage(schaf[schafMeckerFrame], schafPosLO.x, schafPosLO.y);
                // aller 4 Frames soll Schaf was anderes machen
                if (schafCounter++ % 30 == 0) {
                    schafCounter = 1;

                    schafMeckerFrame = (int) (Math.random() * 2.99);

                    // Schafsbloeken synchron zum offenen Maul
                    if (schafMeckerFrame == 2 && !schnauzeSchaf) {
                        mainFrame.soundPlayer.PlayFile("sfx/wowca.wav");
                    }
                }
            }

            // Morphing-Rauchanimationen abspielen ?
            if (isMuellerMorphing) {
                // dann jetzt Rauchanimation zeichnen ueber Figuren zeichnen
                g.setClip(muellerMorph.bummRect());
                muellerMorphCount = muellerMorph.drawBumm(g);
            }

            // Debugging - Zeichnen der Laufrechtecke
            if (Debug.enabled) {
                Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
            }

            // hier ist der Sound...
            // evalSound ();    

            // Krabat einen Schritt gehen lassen
            mainFrame.pathWalker.GeheWeg();

            // Sonderanimationen fuer Krabat ?
            if (SonderAnim != 0) {
                // hier erstmal alles berechnen, dann je nachdem die Bilder switchen
                GenericPoint hier = new GenericPoint(mainFrame.krabat.getPos().x,
                        mainFrame.krabat.getPos().y);

                // Groesse
                int scale = mainFrame.krabat.defScale;
                scale += (int) (((float) mainFrame.krabat.maxx -
                        (float) hier.y) / mainFrame.krabat.zoomf);

                // hier Test auf "nicht zu gross"
                if (scale < mainFrame.krabat.defScale) {
                    scale = mainFrame.krabat.defScale;
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
                        // Krabat sthet nach runterfallen wieder auf
                        if (sonderAnimCounter > 20) {
                            g.drawImage(krabat_aufstehen[0],
                                    hier.x, hier.y, weit, hoch);
                        } else {
                            if (sonderAnimCounter > 14) {
                                g.drawImage(krabat_aufstehen[1],
                                        hier.x, hier.y, weit, hoch);
                            } else {
                                if (sonderAnimCounter > 7) {
                                    g.drawImage(krabat_aufstehen[2],
                                            hier.x, hier.y, weit, hoch);
                                } else {
                                    if (sonderAnimCounter > 0) {
                                        g.drawImage(krabat_aufstehen[3],
                                                hier.x, hier.y, weit, hoch);
                                    }
                                }
                            }
                        }

                        // Aufstehsequenz beendet ?
                        sonderAnimCounter--;
                        if (sonderAnimCounter == 0) {
                            SonderAnim = 0;
                            nextActionID = 101;
                        }
                        break;

                    case 2:
                        // Krabat zeundet Stroh bei Swaixtix an
                        if (sonderAnimCounter > 14) {
                            g.drawImage(krabat_anzuenden[0],
                                    hier.x, hier.y, weit, hoch);
                        } else {
                            if (sonderAnimCounter > 7) {
                                g.drawImage(krabat_anzuenden[1],
                                        hier.x, hier.y, weit, hoch);
                            } else {
                                if (sonderAnimCounter > 0) {
                                    g.drawImage(krabat_anzuenden[2],
                                            hier.x, hier.y, weit, hoch);
                                }
                            }
                        }

                        // Aufstehsequenz beendet ?
                        sonderAnimCounter--;
                        if (sonderAnimCounter == 0) {
                            SonderAnim = 0;
                            nextActionID = 1041;
                        }
                        break;

                    case 3:
                        // Krabat oeffnet Grab
                        // Cliprect setzen
                        g.setClip(hier.x, hier.y, weit * 2 + 1, hoch + 1);
                        g.drawImage(krabat_grab_oeffnen,
                                hier.x, hier.y, weit * 2, hoch);

                        if (sonderAnimCounter == 3) {
                            mainFrame.actions[987] = true; // Flag fuer Grab offen
                        }

                        // Grab-Oeffnen-Sequenz beenden ?
                        if (--sonderAnimCounter == 0) {
                            SonderAnim = 0;
                            nextActionID = 1102;
                        }
                        break;

                    case 4:
                        // Krabat laeuft rueckwaets 
                        // --> Krabat im aktuellen Schritt zeichnen
                        int nrStep = sonderAnimCounter++ % 4;
                        g.drawImage(krabat_links_back[nrStep],
                                hier.x, hier.y, weit, hoch);

                        // X-Position von Krabat um einen Schritt vermindern
                        GenericPoint pCurrent = mainFrame.krabat.getPos();
                        pCurrent.x -= 14;
                        mainFrame.krabat.setPos(pCurrent);

                        // wenn Krabat kurz vor Swaixtix -> Laufen beenden
                        if (pCurrent.x < PswaixtixArm.x + 65) {
                            pCurrent.x = PswaixtixArm.x + 65;
                            mainFrame.krabat.setPos(pCurrent);
                            // System.out.println ("Koordinaten fuer K vor Sw: " + pCurrent.x + " " + pCurrent.y);
                            SonderAnim = 0;
                            nextActionID = 1104;
                        }
                        break;

                    case 5:
                        // Krabat schmeisst Swaixtix um
                        // --> Krabat in Auffang-Position zeichnen

                        if (sonderAnimCounter > 9) {
                            g.drawImage(krabat_links_zucken,
                                    hier.x, hier.y, weit, hoch);
                        } else {
                            g.drawImage(krabat_links_stehen,
                                    hier.x, hier.y, weit, hoch);
                        }

                        // Umschmeiss-Aktion beenden ?
                        if (--sonderAnimCounter <= 0) {
                            SonderAnim = 0;
                            nextActionID = 1105;
                        }
                        break;
                }
            } else {
                // keine spezielle Sonderanimationen !

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

            // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        }

        // Ausgabe von Animoutputtext
        if (!Objects.equals(AnimOutputText, "")) {
            // Textausgabe
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            // um 45 Pixel hoeher zeichnen, dass sich Muelle-Mecker-Text nicht mit  Kr-Texten ueberscheiden !
            mainFrame.imageFont.drawString(g, AnimOutputText,
                    AnimOutputTextPos.x, AnimOutputTextPos.y - 45,
                    FarbenArray[AnimTalkPerson]);
            g.setClip(may.getX(), may.getY(),
                    may.getWidth(), may.getHeight());
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x,
                    outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(),
                    my.getWidth(), my.getHeight());
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
        if (animActive) {
            DoAnims();
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 &&
                mainFrame.talkCount < 1) {
            DoAction();
        }
    }

    // Zeichnen spezieller Background-Grafiken, die vom normalen Hintergrundbild
    // abweichen (z.B. offenes Grab bei Rauchanimation).
    public void drawSpecialBackgrounds(GenericDrawingContext g) {
        // Zeichne Hintergrund neu
        g.drawImage(backgroundBright, 0, 0);

        // gekippte Saeule (+ sauberen Hintergrund) zeichnen, falls umgefallen
        if (mainFrame.actions[986]) {
            g.drawImage(imOhneSwaixtix, 0, 244);
            g.drawImage(imSwaixtixKippen[5], 7, 286);
        }

        // wenn 3.Grab bereits offen ist, auch neu zeichnen
        if (mainFrame.actions[987]) {
            g.drawImage(imSargOffen, 313, 377);
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer horncy
                if (horncy.IsPointInRect(pTemp)) {
                    pTemp = Phorncy;
                    nextActionID = 150;
                }

                // Ausreden fuer swaixtix
                if (swaixtix.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                        swaixtixGefallen.IsPointInRect(pTemp) && mainFrame.actions[986]) {
                    pTemp = Pswaixtix;
                    nextActionID = 155;
                }

                // Ausreden fuer swaixtix's Arm
                if (swaixtixArm.IsPointInRect(pTemp) && !mainFrame.actions[986]) {
                    switch (mainFrame.whatItem) {
                        case 60: // skla
                            nextActionID = 1000;
                            break;
                        case 63: // syno
                            if (!mainFrame.actions[982]) {
                                // Schale noch nicht drin
                                nextActionID = 1010;
                            } else {
                                // Schale schon drin, also reinlegen
                                nextActionID = 1020;
                            }
                            break;
                        case 12: // kamuski
                            if (!mainFrame.actions[983] || mainFrame.actions[984]) {
                                // entweder Stroh noch nicht drin oder brennt schon
                                nextActionID = 1030;
                            } else {
                                // Stroh drin in Swaixtix
                                nextActionID = 1040;
                            }
                            break;
                        default:
                            nextActionID = 167;
                            break;
                    }
                    pTemp = PswaixtixArm;
                }

                // Ausreden fuer Syno, solange noch da
                if (syno.IsPointInRect(pTemp)) {
                    pTemp = Psyno;

                    if (mainFrame.whatItem == 12) {
                        // bei Feuersteinen extra Reaktion
                        nextActionID = 170;
                    } else {
                        nextActionID = 160;
                    }
                }

                // Ausreden fuer alle Graeber
                if (brGrab1.IsPointInRect(pTemp) || brGrab2.IsPointInRect(pTemp) ||
                        brGrab3.IsPointInRect(pTemp) || brGrab4.IsPointInRect(pTemp)) {
                    // zu welchem Grab gehen und wie drauf gucken ?
                    if (brGrab1.IsPointInRect(pTemp)) {
                        pTemp = pGrab1;
                        facingOnGraves = 12;
                    }
                    if (brGrab2.IsPointInRect(pTemp)) {
                        pTemp = pGrab2;
                        facingOnGraves = 12;
                    }
                    if (brGrab3.IsPointInRect(pTemp)) {
                        pTemp = pGrab3;
                        facingOnGraves = 3;
                    }
                    if (brGrab4.IsPointInRect(pTemp)) {
                        pTemp = pGrab4;
                        facingOnGraves = 3;
                    }
                    nextActionID = 165;
                }

                // Ausreden fuer Mueller, solange er da ist
                if (brMueller.IsPointInRect(pTemp) &&
                        muellerda) {
                    pTemp = pTouchMueller;

                    switch (mainFrame.whatItem) {
                        case 61: // Korraktor
                            nextActionID = 176;
                            break;
                        case 62: // grosser Stein
                            nextActionID = 175;
                            break;
                        default: // standardspruch
                            nextActionID = 166;
                            break;
                    }
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

                // Anschauen horncy
                if (horncy.IsPointInRect(pTemp)) {
                    pTemp = Phorncy;
                    nextActionID = 1;
                }

                // Anschauen swaixtix
                if (swaixtix.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                        swaixtixGefallen.IsPointInRect(pTemp) && mainFrame.actions[986]) {
                    pTemp = Pswaixtix;
                    nextActionID = 2;
                }

                // Anschauen Swaixtix's Arm
                if (swaixtixArm.IsPointInRect(pTemp) && !mainFrame.actions[986]) {
                    pTemp = PswaixtixArm;
                    nextActionID = 7;        // Standardspruch

                    if (mainFrame.actions[983]) {
                        nextActionID = 9;    // Spruch, wenn Stroh und Schaale draufliegen
                    }
                    if (mainFrame.actions[984]) {
                        nextActionID = 10;   // Spruch, wenn der Kunde brennt
                    }
                }

                // anschauen Syno, solange noch da
                if (syno.IsPointInRect(pTemp)) {
                    pTemp = Psyno;
                    nextActionID = 3;
                }

                // anschauen aller Graeber
                if (brGrab1.IsPointInRect(pTemp) || brGrab2.IsPointInRect(pTemp) ||
                        brGrab3.IsPointInRect(pTemp) || brGrab4.IsPointInRect(pTemp)) {
                    // Swaixtix brennt noch nicht -> Standardspruch
                    nextActionID = 4;

                    // handelt es sich um das helle Grab (Swaixtix muss brennen) ?
                    if (brGrab3.IsPointInRect(pTemp) && mainFrame.actions[984]) {
                        // spruch ablassen, dass das Grab irgendwie heller ist
                        nextActionID = 5;
                        mainFrame.actions[988] = true;
                    }

                    // Spruch, wenn das geoeffnete Grab angeschaut wird
                    if (brGrab3.IsPointInRect(pTemp) && mainFrame.actions[985]) {
                        nextActionID = 8;
                    }
                }

                // zu welchem Grab gehen und wie drauf gucken ?
                if (brGrab1.IsPointInRect(pTemp)) {
                    pTemp = pGrab1;
                    facingOnGraves = 12;
                }
                if (brGrab2.IsPointInRect(pTemp)) {
                    pTemp = pGrab2;
                    facingOnGraves = 12;
                }
                if (brGrab3.IsPointInRect(pTemp)) {
                    pTemp = pGrab3;
                    facingOnGraves = 3;
                }
                if (brGrab4.IsPointInRect(pTemp)) {
                    pTemp = pGrab4;
                    facingOnGraves = 3;
                }

                // anschauen Mueller, solange er da ist
                if (brMueller.IsPointInRect(pTemp) &&
                        muellerda) {
                    pTemp = pTouchMueller;
                    nextActionID = 6;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Horncy mitnehmen
                if (horncy.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Phorncy);
                    mainFrame.repaint();
                    return;
                }

                // swaixtix mitnehmen
                if (swaixtix.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                        swaixtixGefallen.IsPointInRect(pTemp) && mainFrame.actions[986]) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pswaixtix);
                    mainFrame.repaint();
                    return;
                }

                // Swaixtix's Arm mitnehmen
                if (swaixtixArm.IsPointInRect(pTemp) && !mainFrame.actions[986]) {
                    nextActionID = 67;
                    mainFrame.pathWalker.SetzeNeuenWeg(PswaixtixArm);
                    mainFrame.repaint();
                    return;
                }

                // Syno mitnehmen
                if (syno.IsPointInRect(pTemp)) {
                    // Syno nur einmal mitnehmen -> ansonsten dummer Sprucher
                    if (!mainFrame.actions[981]) {
                        nextActionID = 60;
                    } else {
                        nextActionID = 61;
                    }

                    mainFrame.pathWalker.SetzeNeuenWeg(Psyno);
                    mainFrame.repaint();
                    return;
                }

                // Graeber (alle) mitnehmen
                if (brGrab1.IsPointInRect(pTemp) || brGrab2.IsPointInRect(pTemp) ||
                        brGrab3.IsPointInRect(pTemp) || brGrab4.IsPointInRect(pTemp)) {
                    // Standardspruch
                    nextActionID = 65;

                    // handelt es sich um das helle Grab ? (Swaixtix muss brennen)
                    // -> Grab oeffnen -> Endanimation ankurbeln
                    if (brGrab3.IsPointInRect(pTemp) && mainFrame.actions[984]) {
                        nextActionID = 1100;
                    }

                    // zu welchem Grab laufen und wie aufs Grab gucken ?
                    GenericPoint pNewDest = new GenericPoint(0, 0);
                    if (brGrab1.IsPointInRect(pTemp)) {
                        pNewDest = pGrab1;
                        facingOnGraves = 12;
                    }
                    if (brGrab2.IsPointInRect(pTemp)) {
                        pNewDest = pGrab2;
                        facingOnGraves = 12;
                    }
                    if (brGrab3.IsPointInRect(pTemp)) {
                        pNewDest = pGrab3;
                        facingOnGraves = 3;
                    }
                    if (brGrab4.IsPointInRect(pTemp)) {
                        pNewDest = pGrab4;
                        facingOnGraves = 3;
                    }
                    mainFrame.pathWalker.SetzeNeuenWeg(pNewDest);
                    mainFrame.repaint();
                    return;
                }

                // Mueller mitnehmen (falls es da ist)
                if (brMueller.IsPointInRect(pTemp) &&
                        muellerda) {
                    nextActionID = 66;
                    mainFrame.pathWalker.SetzeNeuenWeg(pTouchMueller);
                    mainFrame.repaint();
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
        if (mainFrame.isAnimRunning ||
                mainFrame.krabat.nAnimation != 0) {
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
                    horncy.IsPointInRect(pTemp) ||
                    swaixtix.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                    swaixtixGefallen.IsPointInRect(pTemp) && mainFrame.actions[986] ||
                    swaixtixArm.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                    syno.IsPointInRect(pTemp) ||
                    brGrab1.IsPointInRect(pTemp) ||
                    brGrab2.IsPointInRect(pTemp) ||
                    brGrab3.IsPointInRect(pTemp) ||
                    brGrab4.IsPointInRect(pTemp) ||
                    brMueller.IsPointInRect(pTemp) &&
                            muellerda;

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
            if (horncy.IsPointInRect(pTemp) ||
                    swaixtix.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                    swaixtixGefallen.IsPointInRect(pTemp) && mainFrame.actions[986] ||
                    swaixtixArm.IsPointInRect(pTemp) && !mainFrame.actions[986] ||
                    syno.IsPointInRect(pTemp) ||
                    brGrab1.IsPointInRect(pTemp) ||
                    brGrab2.IsPointInRect(pTemp) ||
                    brGrab3.IsPointInRect(pTemp) ||
                    brGrab4.IsPointInRect(pTemp) ||
                    brMueller.IsPointInRect(pTemp) &&
                            muellerda) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
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
        ResetAnims();
        mainFrame.krabat.StopWalking();
    }

    // Umgebungs-Sounds abspielen
    /*private void evalSound ()
    {
        if ((mainFrame.inventory.noBackgroundSound == true) && (mainFrame.wave.noBackgroundSound == true) && (mainFrame.invCursor == true)) return; // bei Problemen mit dem Soundsystem zurueckspringen

        // Schaafbloecken (wenn da)
        if (isSchafDa == true)
        {
            int zfz = (int) (Math.random () * 100);
            if (zfz > 97) {
                mainFrame.wave.PlayFile ("sfx/wowca.wav");
            }
        }
	}*/

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 561) // Koraktor raussieben !!!
        {
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
                // horncy anschauen
                KrabatSagt("Rowy_1", fHorncy, 3, 0, 0);
                break;

            case 2:
                // swaixtix anschauen
                KrabatSagt("Rowy_2", fSwaixtix, 3, 0, 0);
                break;

            case 3:
                // syno anschauen
                KrabatSagt("Rowy_3", fSyno, 3, 0, 0);
                break;

            case 4:
                // rowy anschauen
                KrabatSagt("Rowy_4", facingOnGraves, 3, 0, 0);
                break;

            case 5:
                // Grab mit Skellet nach swajxtix anbrennen anschauen
                KrabatSagt("Rowy_5", facingOnGraves, 3, 0, 0);
                break;

            case 6:
                // Mueller anschauen
                KrabatSagt("Rowy_6", fMueller, 3, 0, 0);
                break;

            case 7:
                // Swaixtix's Arm anschauen
                KrabatSagt("Rowy_7", fSwaixtix, 3, 0, 0);
                break;

            case 8:
                // das geoeffnete Grab anschauen
                KrabatSagt("Rowy_8", facingOnGraves, 3, 0, 0);
                break;

            case 9:
                // Swaixtix's Arm (mit Lichtschaale und Stroh) anschauen
                KrabatSagt("Rowy_9", fSwaixtix, 3, 0, 0);
                break;

            case 10:
                // Swaixtix's Arm (brennt) anschauen
                KrabatSagt("Rowy_10", fSwaixtix, 3, 0, 0);
                break;

            case 50:
                // horncy mitnehmen
                KrabatSagt("Rowy_11", fHorncy, 3, 0, 0);
                break;

            case 55:
                // swaixtix mitnehmen
                KrabatSagt("Rowy_12", fSwaixtix, 3, 0, 0);
                break;

            case 60:
                // syno mitnehmen
                mainFrame.krabat.SetFacing(fSyno);
                mainFrame.krabat.nAnimation = 94;
                nextActionID = 0;
                mainFrame.inventory.vInventory.addElement(63);
                mainFrame.actions[981] = true; // nur einmal Aufheben
                break;

            case 61:
                // syno nicht mehr mitnehmen
                KrabatSagt("Rowy_13", fSyno, 3, 0, 0);
                break;

            case 65:
                // rowy mitnehmen
                KrabatSagt("Rowy_14", facingOnGraves, 3, 0, 0);
                break;

            case 66:
                // Mueller mitnehmen
                KrabatSagt("Rowy_15", fMueller, 3, 0, 0);
                break;

            case 67:
                // swaixtix's Arm mitnehmen
                KrabatSagt("Rowy_16", fSwaixtix, 3, 0, 0);
                break;

            case 100:
                // Krabat faellt in Grube rein (am Anfang)
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                SonderAnim = 1; // extra-Images fuer Aufstehen und Rumgucken
                sonderAnimCounter = 30;
                nextActionID = 0;
                break;

            case 101:
                // Spruch sagen
                SonderAnim = 0;
                // Kommentar nach Aufstehen schmeissen
                KrabatSagt("Rowy_17", fAufstehen, 3, 2, 102);
                break;

            case 102:
                // Anim beenden
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                KrabatSagt("Rowy_18", fAufstehen, 3, 0, 0);
                break;


            case 150:
                // horncy - Ausreden
                DingAusrede(fHorncy);
                break;

            case 155:
                // swaixtix - Ausreden
                DingAusrede(fSwaixtix);
                break;

            case 160:
                // syno - ausreden
                DingAusrede(fSyno);
                break;

            case 165:
                // rowy - Ausreden
                DingAusrede(facingOnGraves);
                break;

            case 166:
                // Mueller - Ausreden
                KrabatSagt("Rowy_19", fMueller, 3, 0, 0);
                break;

            case 167:
                // swaixtix's Arm - Ausreden
                DingAusrede(fSwaixtix);
                break;

            case 170:
                // Feuersteine mit Stroh benutzen
                KrabatSagt("Rowy_20", fSyno, 3, 0, 0);
                break;

            case 175:
                // grossen Stein mit Mueller benutzen
                KrabatSagt("Rowy_21", fMueller, 3, 0, 0);
                break;

            case 176:
                // Koraktor mit Mueller benutzen
                KrabatSagt("Rowy_22", fMueller, 3, 0, 0);
                break;

            //////////////DoActions fuer 1. Anim /////////////////////

            case 200:
                // Mueller wird per Morphing hergezaubert
                Borderrect tmp = mueller.getRect();
                GenericPoint tt = new GenericPoint((tmp.lo_point.x + tmp.ru_point.x) / 2, tmp.ru_point.y);
                muellerMorph.Init(tt, 100);  // 2. Argument ist die Groesse in Pixeln !!!
                isMuellerMorphing = true;
                nextActionID = 205;
                break;

            case 205:
                // Mueller spricht
                if (muellerMorphCount < 8) {
                    // nach Morphing-Phase 3 Mueller zeichnen
                    if (muellerMorphCount == 3) {
                        muellerda = true;
                    }
                    break;
                }
                isMuellerMorphing = false;
                mainFrame.isClipSet = false;
                PersonSagt("Rowy_23", 0, 36,
                        2, 210, mueller.evalMlynkTalkPoint());
                break;

            case 210:
                // Krabat spricht
                KrabatSagt("Rowy_24", fMueller, 1, 2, 220);
                break;

            case 220:
                // Mueller spricht
                PersonSagt("Rowy_25", 0, 36, 2, 230, mueller.evalMlynkTalkPoint());
                break;

            case 230:
                // Krabat spricht
                KrabatSagt("Rowy_26", 0, 1, 2, 240);
                break;

            case 240:
                // Mueller spricht
                PersonSagt("Rowy_27", 0, 36, 2, 250, mueller.evalMlynkTalkPoint());
                break;

            case 250:
                // Krabat spricht
                KrabatSagt("Rowy_28", 0, 1, 2, 260);
                break;

            case 260:
                // Mueller spricht
                PersonSagt("Rowy_29", 0, 36, 2,
                        261, mueller.evalMlynkTalkPoint());
                break;

            case 261:
                // Start: Mueller wird per Morphing auf alten Mueller geswitcht
                Borderrect tmp2 = mueller.getRect();
                GenericPoint tt2 = new GenericPoint((tmp2.lo_point.x + tmp2.ru_point.x) / 2, tmp2.ru_point.y);
                muellerMorph.Init(tt2, 100);  // 2. Argument ist die Groesse in Pixeln
                isMuellerMorphing = true;
                nextActionID = 263;
                break;

            case 263:
                // Mueller wird per Morphing auf alten Mueller geswitcht
                if (muellerMorphCount < 8) {
                    // nach Morphing-Phase 3 Mueller zeichnen
                    if (muellerMorphCount == 3) {
                        // Switch auf alten Mueller
                        muellerda = false;
                        alterMuellerda = true;
                    }
                    break;
                }
                isMuellerMorphing = false;
                mainFrame.isClipSet = false;
                nextActionID = 266;
                Counter = 30;
                break;

            case 266:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 268;
                break;

            case 268:
                // Start: Alter Mueller wird per Morphing auf Fledermaus geswitcht
                Borderrect tmp3 = mueller.getRect();
                GenericPoint tt3 = new GenericPoint((tmp3.lo_point.x + tmp3.ru_point.x) / 2, tmp3.ru_point.y);
                muellerMorph.Init(tt3, 100);  // 2. Argument ist die Groesse in Pixeln
                isMuellerMorphing = true;
                nextActionID = 270;
                break;

            case 270:
                // Alter Mueller wird per Morphing auf Fledermaus geswitcht
                if (muellerMorphCount < 8) {
                    // nach Morphing-Phase 3 Fledermaus zeichnen
                    if (muellerMorphCount == 3) {
                        // Switch auf Fledermaus (fliegt zur Decke und haengt sich hin)
                        muellerda = false;
                        alterMuellerda = false;
                        fledermausActionID = 1;
                    }
                    break;
                }
                isMuellerMorphing = false;
                mainFrame.isClipSet = false;
                nextActionID = 271;
                Counter = 10;
                break;

            case 271:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                // zur decke losfliegen
                fledermausActionID = 2;
                nextActionID = 272;
                break;

            case 272:
                // ist fledermaus an der decke angekommen ? (3 => sitzen)
                if (fledermausActionID != 3) {
                    break;
                }
                nextActionID = 274;
                Counter = 10;
                break;

            case 274:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 276;
                break;

            case 276:
                // Krabat spricht
                KrabatSagt("Rowy_30", 0, 1, 2, 280);
                break;

            case 280:
                // Mueller spricht
                fledermausActionID = 4; // Fledermaus haengt und redet
                PersonSagt("Rowy_31", 0, 36, 2, 290, mueller.evalMlynkTalkPoint());
                break;

            case 290:
                // Krabat spricht
                fledermausActionID = 3; // Fledermaus haengt und haelt Schnauze
                KrabatSagt("Rowy_32", 0, 1,
                        2, 300);
                break;

            case 300:
                // Mueller spricht
                fledermausActionID = 4; // Fledermaus haengt und redet
                PersonSagt("Rowy_33", 0, 36, 2, 310, mueller.evalMlynkTalkPoint());
                break;

            case 310:
                // Krabat spricht
                fledermausActionID = 3; // Fledermaus haengt und haelt Schnauze
                KrabatSagt("Rowy_34", 0, 1, 2, 320);
                break;

            case 320:
                // Mueller spricht
                fledermausActionID = 4; // Fledermaus haengt und redet
                PersonSagt("Rowy_35", 0, 36, 2, 330, mueller.evalMlynkTalkPoint());
                break;

            case 330:
                // Krabat spricht
                fledermausActionID = 3; // Fledermaus haengt und haelt Schnauze
                KrabatSagt("Rowy_36", 0, 1, 2, 340);
                break;

            case 340:
                // Krabat spricht
                KrabatSagt("Rowy_37", 0,
                        1, 2, 342);
                break;

            case 342:
                fledermausActionID = 5;
                nextActionID = 344;
                break;

            case 344:
                // ist fledermaus wieder unten angekommen ?
                if (fledermausActionID != 6) {
                    break;
                }
                nextActionID = 346;
                break;

            case 346:
                // Start: Fledermaus wird per Morphing auf Mueller geswitcht
                Borderrect tmp4 = mueller.getRect();
                GenericPoint tt4 = new GenericPoint((tmp4.lo_point.x + tmp4.ru_point.x) / 2, tmp4.ru_point.y);
                muellerMorph.Init(tt4, 100);  // 2. Argument ist die Groesse in Pixeln
                isMuellerMorphing = true;
                nextActionID = 347;
                break;

            case 347:
                // Fledermaus wird per Morphing auf Mueller geswitcht
                if (muellerMorphCount < 8) {
                    // nach Morphing-Phase 3 Mueller statt Fledermaus zeichnen
                    if (muellerMorphCount == 3) {
                        muellerda = true;
                        alterMuellerda = false;
                        fledermausActionID = 0;
                    }
                    break;
                }
                isMuellerMorphing = false;
                mainFrame.isClipSet = false;
                nextActionID = 350;
                break;

            case 350:
                // Mueller spricht
                PersonSagt("Rowy_38", 0, 36, 2, 360,
                        mueller.evalMlynkTalkPoint());
                break;

            case 360:
                // Krabat spricht
                KrabatSagt("Rowy_39", 0, 1, 2, 370);
                break;

            case 370:
                // Ende 1. Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                animActive = true;
                mainFrame.repaint();
                break;

            // Ende 1. Anims /////////////////////////////////////////

            // DoActions 2. Anim ////////////////////////////////////

            case 400:
                // Krabat spricht
                mainFrame.krabat.SetFacing(3);
                mainFrame.krabat.nAnimation = 156; // aus Korraktor lesen
                KrabatSagt("Rowy_40", 0, 1, 2, 410);  // Next ID 410 ist richtig (695 ist Ende) !!!!!!
                break;

            case 410:
                // Mueller spricht
                mainFrame.krabat.nAnimation = 158; // Koraktor halten
                PersonSagt("Rowy_41", 0, 36, 2, 420,
                        mueller.evalMlynkTalkPoint());
                break;

            case 420:
                // Krabat spricht
                mainFrame.krabat.nAnimation = 156; // aus Korraktor lesen
                KrabatSagt("Rowy_42", 0, 1, 2, 430);
                break;

            case 430:
                // Krabat spricht
                KrabatSagt("Rowy_43", 0, 1, 2, 432);
                break;

            case 432:
                // mit Lesen aufhoeren
                mainFrame.krabat.StopAnim();
                Counter = 30;
                nextActionID = 435;
                break;

            case 435:
                // Start: Schaf wird reingezaubert
                GenericPoint ttS = new GenericPoint(schafPosLO.x + 60, schafPosLO.y + 75);
                muellerMorph.Init(ttS, 120);  // 2. Argument ist die Groesse in Pixeln
                isMuellerMorphing = true;
                nextActionID = 437;
                break;

            case 437:
                // Morphing: Schaf wird reingezaubert
                if (muellerMorphCount < 8) {
                    // nach Morphing-Phase 3 Schaf zeichnen
                    if (muellerMorphCount == 3) {
                        isSchafDa = true;
                    }
                    break;
                }
                isMuellerMorphing = false;
                mainFrame.isClipSet = false;
                nextActionID = 438;
                Counter = 20;
                break;

            case 438:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 470;
                break;

            case 470:
                // Mueller spricht
                PersonSagt("Rowy_44", 0, 36, 2, 480, mueller.evalMlynkTalkPoint());
                break;

            case 480:
                // Krabat spricht
                mainFrame.krabat.nAnimation = 156;
                KrabatSagt("Rowy_45", 0, 1, 2, 490);
                break;

            case 490:
                // Krabat spricht
                KrabatSagt("Rowy_46", 0, 1, 2, 492);
                break;

            case 492:
                // Krabat spricht
                KrabatSagt("Rowy_47", 0, 1, 2, 495);
                break;

            case 495:
                // mit Lesen aufhoeren
                mainFrame.krabat.StopAnim();
                istMuellerSchonTot = true;  // mit Zwinkern aufhoeren
                schnauzeSchaf = true; // Schafbloeken abschalten, ist eh bald weg
                Counter = 30;
                nextActionID = 497;
                break;

            case 497:
                // Start: Schaf wird weggezaubert
                if (--Counter > 1) {
                    break;
                }
                GenericPoint ttS2 = new GenericPoint(schafPosLO.x + 60, schafPosLO.y + 75);
                muellerMorph.Init(ttS2, 100);  // 2. Argument ist die Groesse in Pixeln
                isMuellerMorphing = true;
                nextActionID = 498;
                break;

            case 498:
                // Morphing: Schaf wird weggezaubert
                if (muellerMorphCount < 8) {
                    // nach Morphing-Phase 3 Schaf nicht mehr zeichnen
                    if (muellerMorphCount == 3) {
                        isSchafDa = false;
                    }
                    break;
                }
                isMuellerMorphing = false;
                mainFrame.isClipSet = false;
                nextActionID = 499;
                Counter = 20;
                break;

            case 499:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 670;
                break;

            //////////////////////////////////////////////////////////

            case 561:
                // Krabat benutzt Koraktor -> erstmal zum Ausgangspunkt laufen
                // und dann 2. Teil der Endanimation starten
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                animActive = false;
                ResetAnims();
                mainFrame.isClipSet = false;
                nextActionID = 400;
                mainFrame.pathWalker.SetzeNeuenWeg(pTalkToMueller);
                mainFrame.repaint();
                return;

            ////////////////////////////////////////////////////////////

            case 670:
                // Krabat spricht
                KrabatSagt("Rowy_48", 0, 1, 2, 680);
                break;

            case 680:
                // Krabat spricht
                KrabatSagt("Rowy_49", 0, 1, 2, 681);
                Counter = 10;
                break;

            case 681:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 682;
                break;

            case 682:
                // Krabat geht nah an Mueller ran
                mainFrame.pathWalker.SetzeNeuenWeg(pTouchMueller);
                // mainFrame.repaint();
                nextActionID = 684;
                Counter = 5;
                break;

            case 684:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                BackgroundMusicPlayer.getInstance().stop();
                nextActionID = 688;
                break;

            case 688:
                // Krabat beruehrt Mueller
                mainFrame.krabat.nAnimation = 31;
                nextActionID = 689;
                Counter = 4;
                mainFrame.repaint();
                break;

            case 689:
                // bisschen warten
                if (Counter == 3) {
                    mainFrame.soundPlayer.PlayFile("sfx/explosion.wav");
                }
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 690;
                break;

            case 690:
                // Start: Mueller explodiert
                muellerExplodiert = true;
                muellerda = false;
                nextActionID = 692;
                break;

            case 692:
                // Mueller explodiert
                if (muellerExplodiert) {
                    break;
                }
                nextActionID = 693;
                Counter = 15;
                break;

            case 693:
                // bisschen warten
                if (Counter == 8) {
                    mainFrame.krabat.SetFacing(6);
                }
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 695;
                break;

            case 695:
                // Krabat spricht
                mainFrame.soundPlayer.PlayFile("sfx/mlynk-konc.wav");
                KrabatSagt("Rowy_50", 0, 1, 2, 698);
                Counter = 8;
                break;

            case 698:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 700;
                break;

            case 700:
                // Extro laden
                NeuesBild(101, 202);
                break;

            case 1000:
                // put Schuessel in Swaixtix
                mainFrame.krabat.nAnimation = 123;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 0;
                mainFrame.actions[982] = true;
                mainFrame.inventory.vInventory.removeElement(60);
                mainFrame.repaint();
                break;

            case 1010:
                // Stroh zu zeitig -> geht nicht
                DingAusrede(fSwaixtix);
                break;

            case 1020:
                // put Stroh in Schuessel
                mainFrame.krabat.nAnimation = 123;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                nextActionID = 0;
                mainFrame.actions[983] = true;
                mainFrame.inventory.vInventory.removeElement(63);
                mainFrame.repaint();
                break;

            case 1030:
                // Steine zu zeitig -> geht nicht
                DingAusrede(fSwaixtix);
                break;

            case 1040:
                // Swaixtix anbrennen
                // Animation fuer Anzuenden abspielen
                mainFrame.isAnimRunning = true;
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                SonderAnim = 2; // extra-Images fuer Swaixtix anbrennen
                sonderAnimCounter = 20;
                nextActionID = 0;
                mainFrame.soundPlayer.PlayFile("sfx/kamjeny.wav");
                mainFrame.repaint();
                break;

            case 1041:
                // Anzuenden-Anim beenden
                mainFrame.actions[984] = true;
                SonderAnim = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                // komplette Szene neuzeichnen (helles Hintergrundbild) !
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 1100:
                // der Beginn vom Ende
                if (!mainFrame.actions[985]) {
                    // erstes Mal Grab sehen
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);

                    // Spruch bloss loslassen, wenn Krabat nach Anzuenden noch nicht Grab angesehen hat !
                    if (!mainFrame.actions[988]) {
                        KrabatSagt("Rowy_51", 0, 1, 2, 1101);
                    } else {
                        nextActionID = 1101;
                    }
                } else {
                    KrabatSagt("Rowy_52", 0, 3, 0, 0);
                }
                break;

            case 1101:
                // Grabdeckel zur Seite schieben
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // Animation fuer Grab oeffnen abspielen
                SonderAnim = 3;
                sonderAnimCounter = 10;
                nextActionID = 0;
                mainFrame.soundPlayer.PlayFile("sfx/grab.wav");
                mainFrame.repaint();
                break;

            case 1102:
                // Grab zeigen und eine Weile warten
                zeigeGrab = true;
                mainFrame.isClipSet = false;
                sonderAnimCounter = 30;
                nextActionID = 1103;
                break;

            case 1103:
                // Grabanzeige beenden und Rueckwaerts-Laufanimation starten
                if (--sonderAnimCounter > 1) {
                    break;
                }
                zeigeGrab = false;
                mainFrame.isClipSet = false;

                // Rueckwaerts laufen und Swaixtix umwerfen
                SonderAnim = 4;
                sonderAnimCounter = 0;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 1104:
                // Swaixtix-Umschmeissen-Anim beginnen
                mainFrame.krabat.SetFacing(9);
                SonderAnim = 5;
                sonderAnimCounter = 13;
                nextActionID = 0;
                mainFrame.actions[986] = true;
                // WalkingRects neu initialisieren -> Treppe nicht mehr begehbar
                this.initLocationWalkingRects();
                mainFrame.repaint();
                break;

            case 1105:
                // Swaixtix-Umschmeissen-Anim beeenden
                SonderAnim = 0;
                nextActionID = 1106;
                mainFrame.repaint();
                Counter = 10;
                break;

            case 1106:
                // bisschen warten
                if (Counter == 5) {
                    mainFrame.krabat.SetFacing(9);
                }
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 1110;
                break;

            case 1110:
                // K findet Koraktor auf Swaixtix's Sockel
                KrabatSagt("Rowy_53", 9, 3, 2, 1111);
                break;

            case 1111:
                // Korraktor-aufheben-Anim beginnen
                mainFrame.krabat.nAnimation = 92;
                nextActionID = 1112;
                Counter = 5;
                break;

            case 1112:
                // Korraktor-aufheben-Anim beenden (& ins Inventar aufnehmen)
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(61);
                    mainFrame.actions[985] = true;
                    mainFrame.isClipSet = false;
                }
                if (Counter > 0 || mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 1113;
                Counter = 10;
                break;

            case 1113:
                // bisschen warten
                if (--Counter > 1) {
                    break;
                }
                nextActionID = 200;
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    // Anim des Muellers ausfuehren
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
                // Text ueber Mueller ausgeben
                int random = (int) (Math.random() * 5.9);
                AnimOutputText = Start.stringManager.getTranslation(MILLER_THREATENS[random]);
                AnimOutputTextPos = mainFrame.imageFont.CenterAnimText(
                        AnimOutputText, mueller.evalMlynkTalkPoint());
                AnimCounter = 50;
                AnimTalkPerson = 36;
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
        AnimTalkPerson = 0;
    }

}