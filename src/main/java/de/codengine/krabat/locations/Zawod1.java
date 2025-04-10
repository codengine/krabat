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

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Husa;
import de.codengine.krabat.anims.IntroDomaPtack;
import de.codengine.krabat.anims.LogoPtack;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericDrawingContext2D;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Zawod1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Zawod1.class);
    private GenericImage domal;
    private GenericImage domar;
    private GenericImage sky;
    private GenericImage kij;
    private GenericImage rapaki;
    private GenericImage intro1;
    private GenericImage intro2;
    private GenericImage budysin;
    private GenericImage budysky;
    private GenericImage sitz1;
    private GenericImage sitz2;
    private final GenericImage[] floete;
    private int IntroStep = 1;
    private int nextActionID = 0;
    private IntroDomaPtack ptack1;
    private IntroDomaPtack ptack2;
    private final Husa gans1;
    private final Husa gans2;
    private final Husa gans3;

    private GenericImage offImage;
    private GenericDrawingContext offGraphics;

    private int Counter = 0;
    private int Help = 1;

    private boolean setScroll = false;
    private int Scrollwert;

    private boolean playing = true;  // so lassen, ist nur ein Gag...

    private LogoPtack vogel1;
    private LogoPtack vogel2;
    private LogoPtack vogel3;

    private int skipActionID = 0;

    private int Farb = 1;

    private int BildIndex = 1;

    private int floetenIndex = 0;
    private int zwinkerIndex = 0;

    private int Verhinderfloete = 3;
    private static final int MAX_VERHINDERFLOETE = 3;

    private int CDtrack = 22;

    private long FloetenSpielZeit = 0; // zaehlt die Zeit, wielange Krabat Floete spielen soll

    private int line4 = 0;
    private static final String[] TEXTS4 = {
            "Zawod1_4", "Zawod1_5", "Zawod1_6", "Zawod1_7", "Zawod1_8", "Zawod1_9", "Zawod1_10", "Zawod1_11", "Zawod1_12"
    };


    private static final GenericPoint MittelPunkt = new GenericPoint(320, 240);
    private int TalkPause = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von diesem Intro erzeugen
    public Zawod1(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        floete = new GenericImage[4];

        InitImages();

        ptack1 = new IntroDomaPtack(mainFrame, 1150, 10);
        ptack2 = new IntroDomaPtack(mainFrame, 1250, 15);

        vogel1 = new LogoPtack(mainFrame, 640, 185);
        vogel2 = new LogoPtack(mainFrame, 690, 200);
        vogel3 = new LogoPtack(mainFrame, 750, 225);

        gans1 = new Husa(mainFrame, new Borderrect(150, 260, 197, 270));
        gans2 = new Husa(mainFrame, new Borderrect(238, 270, 285, 280));
        gans3 = new Husa(mainFrame, new Borderrect(182, 300, 276, 310));

        mainFrame.isClipSet = false;

        mainFrame.krabat.setPos(new GenericPoint(128, 352));
        mainFrame.krabat.SetFacing(3);

        mainFrame.krabat.maxx = 402;
        mainFrame.krabat.zoomf = 2.32f;
        mainFrame.krabat.defScale = 0;

        offImage = GenericToolkit.getDefaultToolkit().createImage(250, 200);
        offGraphics = offImage.getGraphics();

        Counter = 0; // nur zur Sicherheit

        mainFrame.freeze(false);
    }

    // Bilder vorbereiten
    private void InitImages() {
        rapaki = getPicture("gfx/intro/rap100.png");
        domal = getPicture("gfx/doma/dom-l.png");
        domar = getPicture("gfx/doma/dom-r.png");
        sky = getPicture("gfx/doma/domsky.png");
        kij = getPicture("gfx/doma/doma2.png");

        intro1 = getPicture("gfx/intro/intro1.png");
        intro2 = getPicture("gfx/intro/intro2.png");
        budysin = getPicture("gfx/intro/bzpali.png");
        budysky = getPicture("gfx/intro/bzsky.png");

        sitz1 = getPicture("gfx/anims/ksedzo1.png");
        sitz2 = getPicture("gfx/anims/ksedzo1a.png");

        floete[0] = getPicture("gfx/anims/ks-f1.png");
        floete[1] = getPicture("gfx/anims/ks-f2.png");
        floete[2] = getPicture("gfx/anims/ks-f3.png");
        floete[3] = getPicture("gfx/anims/ks-f4.png");

    }

    @Override
    public void cleanup() {
        rapaki = null;
        domal = null;
        domar = null;
        sky = null;
        kij = null;

        intro1 = null;
        intro2 = null;
        budysin = null;
        budysky = null;

        sitz1 = null;
        sitz2 = null;

        floete[0] = null;
        floete[1] = null;
        floete[2] = null;
        floete[3] = null;

        ptack1.cleanup();
        ptack1 = null;
        ptack2.cleanup();
        ptack2 = null;

        vogel1.cleanup();
        vogel1 = null;
        vogel2.cleanup();
        vogel2 = null;
        vogel3.cleanup();
        vogel3 = null;

        offGraphics = null;
        offImage = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // System.out.println("repaint!");

        // mit erstem CD-Stueck hier beginnen
        if (CDtrack == 22) {
            CDtrack = 0;
            BackgroundMusicPlayer.getInstance().playTrack(2, true);
        }

        // Logo ein- und ausfaden ///////////////////////////////////////////////
        if (IntroStep < 10) {
            // Wiederherstellen, wenn Mainmenu aufgerufen
            if (!mainFrame.isClipSet) {
                mainFrame.isClipSet = true;
                Cursorform = 200;
                mainFrame.isAnimRunning = true;
                mainFrame.scrollX = 0;
                mainFrame.scrollY = 0;
                g.setClip(0, 0, 644, 484);
                g.clearRect(0, 0, 640, 480);
                evalMouseMoveEvent(mainFrame.mousePoint);
                if (!playing) {
                    playing = true;
                    mainFrame.soundPlayer.PlayFile("gamesound.wav"); // passiert nicht mehr,nur Gag
                }
            }

            // Rapaki einfaden
            if (IntroStep == 1) {
                GenericDrawingContext2D g2 = offGraphics.get2DContext();

                GenericAlphaComposite ac = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, 1);
                g2.setComposite(ac);
                g2.setColor(GenericColor.black);
                g2.fillRect(0, 0, 250, 200);

                float fuckhelp = Help;
                ac = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, fuckhelp / 40);
                g2.setComposite(ac);
                g2.drawImage(rapaki, 0, 0);
                if (Help < 40) {
                    Help++;
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                } else {
                    nextActionID = 1;
                }
                g.drawImage(offImage, 205, 150);
            }

            // Voegel durch das Bild fliegen lassen
            if (IntroStep == 2) {
                g.setClip(0, 0, 644, 484);
                g.clearRect(0, 170, 640, 180);
                g.drawImage(rapaki, 205, 150);
                vogel1.Flieg(g);
                vogel2.Flieg(g);
                boolean ende = vogel3.Flieg(g);
                if (!ende) {
                    nextActionID = 1;
                }

                // hier den Sound eval.
                if (++Counter == 30) {
                    mainFrame.soundPlayer.PlayFile("sfx/rapak1.wav");
                }
                if (Counter == 60) {
                    mainFrame.soundPlayer.PlayFile("sfx/rapak2.wav");
                }
            }

            // Rapaki ausfaden
            if (IntroStep == 3) {
                GenericDrawingContext2D g3 = offGraphics.get2DContext();
                GenericAlphaComposite ad = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, 1);
                g3.setComposite(ad);
                g3.setColor(GenericColor.black);
                g3.fillRect(0, 0, 250, 200);

                float fhelp = Help;
                ad = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, fhelp / 40);
                g3.setComposite(ad);
                g3.drawImage(rapaki, 0, 0);
                if (Help > 0) {
                    Help--;
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                } else {
                    nextActionID = 3;
                }
                g.drawImage(offImage, 205, 150);
            }
        }

        // Textausgabe auf leeren Screen (noch keine Bilder da), aber getrennt nach vorgesehenen Bildern
        if (IntroStep > 9 && IntroStep < 20) {
            // Wiederherstellen ist zwar Quatsch, aber wer weiss
            if (!mainFrame.isClipSet) {
                g.setClip(0, 0, 644, 484);
                mainFrame.isClipSet = true;
                Cursorform = 200;
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
            }

            if (BildIndex == 1) {
                rapaki = null;
                g.drawImage(budysky, 0, 0);
                g.drawImage(budysin, 0, 0);
            }
            if (BildIndex == 2) {
                budysky = null;
                budysin = null;
                g.drawImage(intro1, 0, 0);
            }

            if (BildIndex == 3) {
                intro1 = null;
                g.drawImage(intro2, 0, 0);
            }

            // Textausgabe
            if (!Objects.equals(outputText, "")) {
                g.setClip(0, 0, 644, 484);
                mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[13]);
            }

            // Textausgabezeit mit talkCount realisieren
            if (mainFrame.talkCount > 1) {
                mainFrame.talkCount--;
                if (mainFrame.talkCount == 1) {
                    mainFrame.isClipSet = false;
                    outputText = "";
                }
                return;
            }

            if (TalkPause > 0) {
                TalkPause--;
                return;
            }

            nextActionID = IntroStep;
        }

        // Vogelflug und Scrolling in Doma ////////////////////////////////////////////////////
        if (IntroStep > 99) {
            // Wiederherstellen, wenn Exit aufgerufen
            if (!mainFrame.isClipSet) {
                mainFrame.isClipSet = true;
                Cursorform = 200;
                mainFrame.isAnimRunning = true;
                if (setScroll) {
                    setScroll = false;
                    mainFrame.scrollX = Scrollwert;
                }
                g.setClip(0, 0, 1284, 484);
                g.drawImage(sky, mainFrame.scrollX / 10, 0);
                g.drawImage(domal, 0, 0);
                g.drawImage(domar, 640, 0);
                g.drawImage(kij, 80, 325);
                evalKrabat(g);
                evalMouseMoveEvent(mainFrame.mousePoint);
            }

            // Voegel fliegen mit Scrolling bis letzter Vogel weg und Scroller zu Ende
            if (IntroStep == 100) {
                intro2 = null;
                int xx = mainFrame.scrollX - 10;
                if (xx < 0) {
                    xx = 0;
                }
                g.setClip(xx, 0, xx + 650, 285);
                g.drawImage(sky, mainFrame.scrollX / 10, 0);
                g.drawImage(domal, 0, 0);
                g.drawImage(domar, 640, 0);
                g.drawImage(kij, 80, 325);
                evalKrabat(g);
                g.setClip(0, 0, 900, 250);
                ptack1.Flieg(g);
                mainFrame.scrollX -= 1;
                if (mainFrame.scrollX < 0) {
                    mainFrame.scrollX = 0;
                }
                if (!ptack2.Flieg(g) && mainFrame.scrollX == 0) {
                    IntroStep++;
                }
                evalSound();
            }

            // Gaense animieren
            g.setClip(120, 255, 230, 110);
            g.drawImage(sky, mainFrame.scrollX / 10, 0);
            g.drawImage(domal, 0, 0);
            g.drawImage(domar, 640, 0);
            gans1.BewegeGans(g);
            gans2.BewegeGans(g);
            gans3.BewegeGans(g);

// 		if (FloetenSpielZeit < 300) FloetenSpielZeit++; // Floete nur 1x waehrend repaint erhoehen
// 		System.out.println (System.currentTimeMillis());

            evalKrabat(g);

            // Textausgabe
            if (!Objects.equals(outputText, "")) {
                g.setClip(0, 0, 644, 484);
                mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, Farb);
            }

            // Textausgabezeit mit talkCount realisieren
            if (mainFrame.talkCount > 1) {
                mainFrame.talkCount--;
                if (mainFrame.talkCount == 1) {
                    mainFrame.isClipSet = false;
                    outputText = "";
                }
                return;
            }

            if (TalkPause > 0) {
                TalkPause--;
                return;
            }

            nextActionID = IntroStep;

            if (IntroStep < 102) {
                Farb = FarbenArray[13];
            } else {
                Farb = FarbenArray[1];
            }

            if (IntroStep == 106) {
                skipActionID = 200;
            }
        }

        if (skipActionID != 0 || nextActionID != 0) {
            DoAction();
        }
    }

    // Hier Extrawurst fuer Krabat - da jedesmal anders...
    private void evalKrabat(GenericDrawingContext g) {
        switch (IntroStep) {
            case 100: // Krabat spielt Floete
            case 101:
            case 102:
                // Floeten - Routine, bis die Zeit um ist
                if (System.currentTimeMillis() - FloetenSpielZeit <= 21000) {

                    g.setClip(103, 316, 27, 37);
                    g.drawImage(domal, 0, 0);
                    if (--Verhinderfloete < 1) {
                        Verhinderfloete = MAX_VERHINDERFLOETE;
                        floetenIndex++;
                        if (floetenIndex == 4) {
                            floetenIndex = 0;
                        }
                    }
                    g.drawImage(floete[floetenIndex], 103, 316);
                    // System.out.print (FloetenSpielZeit + " ");
                    break;
                }  // ansonsten gehts jetzt mit diesen cases weiter (denken)

            case 103: // Nachdenken
            case 104:
                // Zwinker - Routine
                g.setClip(103, 316, 27, 37);
                g.drawImage(domal, 0, 0);
                int zuffi = (int) (Math.random() * 50); //FIXME: This might break something, the previous calculation was always 0
                zwinkerIndex = zwinkerIndex == 0 && zuffi > 45 ? 1 : 0;
                g.drawImage(zwinkerIndex == 0 ? sitz1 : sitz2, 103, 316);
                break;

            case 105: // Reden und aufstehen
                // normale Krabat - Routine
                mainFrame.krabat.describeKrabat(g);
                break;

            case 106: // nur Dastehen
                // normale Krabat - Routine
                mainFrame.krabat.drawKrabat(g);
                break;

            default: // Fehler
                log.error("Falscher Krabat gewuenscht! IntroStep = {}", IntroStep);
                break;
        }
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // GenericPoint pTemp = e.getPoint ();

        // Grundsaetzlicher Textabbruch      
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPause = 0;
            mainFrame.isClipSet = false;
            outputText = "";
        }

        if (e.isRightClick()) {
            // bei Intro - Bildern Mainmenu erlauben
            // > 0 = alles, > 9 = nur nach Rapaki-Intro
            if (IntroStep > 9 && IntroStep < 20) {
                // Hauptmenue aufrufen
                mainFrame.mainMenu.introcall = true;
                skipActionID = 100;
                mainFrame.repaint();
            }
        }
    }

    // MouseMove - Auswertung + Cursorformen

    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (mainFrame.isAnimRunning) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
        } else {
            if (Cursorform != 0) {
                Cursorform = 0;
                mainFrame.setCursor(mainFrame.cursorNormal);
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Tasten - Auswertung

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Escape soll immer gehen...

        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            // Disabled - kein Escape im Intro
                /*if (IntroStep < 10)
                  {
                  mainFrame.talkCount = 1;
                  TalkPause = 0;
                  skipActionID = 10;
                  mainFrame.repaint();
                  return;
                  }	*/

            if (IntroStep > 9 && IntroStep < 100) {
                mainFrame.talkCount = 1;
                TalkPause = 0;
                skipActionID = 20;
                mainFrame.repaint();
                return;
            }

            if (IntroStep > 99) {
                mainFrame.talkCount = 1;
                TalkPause = 0;
                skipActionID = 200;
                mainFrame.repaint();
                return;
            }

            return;
        }

        // Der Rest geht nur noch im Intro

        if (IntroStep > 9 && IntroStep < 20) {
            // Hauptmenue aktivieren
            if (Taste == GenericKeyEvent.VK_F1) {
                mainFrame.mainMenu.introcall = true;
                skipActionID = 100;
                mainFrame.repaint();
                return;
            }

            // Load - Screen aktivieren
            if (Taste == GenericKeyEvent.VK_F3) {
                log.debug("----- Enable open screen in intro! -----");
                skipActionID = 120;
                mainFrame.repaint();
            }
        }
    }

    // Ausgabe des Rabensounds, aber selten...
    private void evalSound() {
        int zuffZahl = (int) (Math.random() * 100);

        if (zuffZahl > 96) {
            int zweiteZahl = (int) (Math.random() * 1.9);
            zweiteZahl += 49;

            mainFrame.soundPlayer.PlayFile("sfx/rapak" + (char) zweiteZahl + ".wav");
        }
    }

    // Aktionen, die ausgefuehrt werden muessen /////////////////////////////
    private void DoAction() {
        // SkipActionID hat Vorrang
        if (skipActionID != 0) {
            switch (skipActionID) {
                case 10:
                    // Skip Intro - Bilder, ist momentan disabled
                    IntroStep = 10;
                    mainFrame.isClipSet = false;
                    mainFrame.isAnimRunning = true;
                    nextActionID = 0;
                    skipActionID = 0;
                    break;

                case 20:
                    // Skip Bilder - Voegel
                    IntroStep = 15;
                    Scrollwert = 200;
                    outputText = "";
                    setScroll = true;
                    mainFrame.isClipSet = false;
                    mainFrame.isAnimRunning = true;
                    nextActionID = 0;
                    skipActionID = 0;
                    break;

                case 100:
                    // Mainmenu aktivieren
                    mainFrame.isAnimRunning = false;
                    mainFrame.whatScreen = ScreenType.MAIN_MENU;
                    skipActionID = 0;
                    mainFrame.isClipSet = false;
                    mainFrame.repaint();
                    Cursorform = 200;
                    break;

                case 120:
                    // Load - Screen aktivieren
                    mainFrame.isAnimRunning = false;
                    skipActionID = 0;
                    mainFrame.constructLocation(102);
                    mainFrame.whatScreen = ScreenType.LOAD_GAME;
                    mainFrame.isClipSet = false;
                    mainFrame.repaint();
                    Cursorform = 200;
                    break;

                case 200:
                    // Spiel starten
                    mainFrame.isAnimRunning = false;
                    mainFrame.isClipSet = false;
                    mainFrame.mainMenu.introcall = false;
                    // Einmalige Ausnahme, wegen Gaense uebergeben
                    mainFrame.constructLocation(6, gans1, gans2, gans3);
                    mainFrame.destructLocation(100);
                    mainFrame.repaint();
                    skipActionID = 0;
                    break;

                default:
                    log.error("Wrong SkipAction !! skipActionID = {}", skipActionID);
            }
        } else {
            // System.out.println ("A-ID" + nextActionID);

            switch (nextActionID) {
                case 1:
                    // Next Introstep please
                    IntroStep++;
                    nextActionID = 0;
                    break;

// 		    case 3:
// 			// 1 Repaint dazwischen platzlassen, damit "Rapaki" verschwindet
// 			nextActionID = 4;
// 			break;

                case 3:
                    // Von Rapaki auf Bautzen umschalten
                    IntroStep = 10;
                    mainFrame.isClipSet = false;
                    mainFrame.isAnimRunning = true;
                    nextActionID = 0;
                    break;

                case 10:
                    // Einfuehrungstext
                    // mainFrame.player.Play ("2", -133600);
                    outputText = Start.stringManager.getTranslation("Zawod1_13");
                    outputTextPos = mainFrame.imageFont.CenterText(outputText, MittelPunkt);
                    mainFrame.talkCount += 50; // Zeit fuer Bautzen-Bild kuenstlich verlaengern
                    TalkPause = 5;
                    IntroStep++;
                    break;

                case 11:
                    // 1 Sekunde warten
                    BildIndex = 2;
                    TalkPause = 10;
                    IntroStep++;
                    break;

                case 12:
                    // Text ueber Situation
                    outputText = mainFrame.imageFont.TeileTextKey("Zawod1_1");
                    outputTextPos = mainFrame.imageFont.CenterText(outputText, new GenericPoint(320, 450));
                    IntroStep++;
                    TalkPause = 5;
                    break;

                case 13:
                    // 1 Sekunde warten
                    BildIndex = 3;
                    TalkPause = 10;
                    IntroStep++;
                    break;

                case 14:
                    // Text ueber die dicken Gutsherren
                    outputText = mainFrame.imageFont.TeileTextKey("Zawod1_2");
                    outputTextPos = mainFrame.imageFont.CenterText(outputText, new GenericPoint(320, 450));
                    IntroStep++;
                    TalkPause = 5;
                    break;

                case 15:
                    // Von Rapaki auf Vogelflug umschalten
                    BackgroundMusicPlayer.getInstance().playTrack(3, false);
                    FloetenSpielZeit = System.currentTimeMillis();
                    IntroStep = 100;
                    Scrollwert = 200;
                    setScroll = true;
                    mainFrame.isClipSet = false;
                    mainFrame.isAnimRunning = true;
                    nextActionID = 0;
                    break;

                case 100: // nix zu tun...
                    break;

                case 101:
                    // Text ueber Krabat (Erzaehler)
                    outputText = mainFrame.imageFont.TeileTextKey("Zawod1_3");
                    outputTextPos = mainFrame.imageFont.CenterText(outputText, MittelPunkt);
                    IntroStep++;
                    TalkPause = 5;
                    break;

                case 102: // Kleine Pause, wg. Introstep - Variablenwert
                    IntroStep++;
                    break;

                case 103:
                    // Text von Krabat denkend
                    outputText = mainFrame.imageFont.TeileTextKey(TEXTS4[line4]);
                    outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                    line4++;
                    if (line4 == TEXTS4.length) {
                        IntroStep++;
                    } else {
                        nextActionID = 0;
                    }
                    TalkPause = 5;
                    break;

                case 104:
                    // Text von Krabat redend
                    mainFrame.isClipSet = false;
                    outputText = Start.stringManager.getTranslation("Zawod1_14");
                    outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                    IntroStep++;
                    TalkPause = 3;
                    break;

                case 105:
                    // alles loeschen wg. Redendem Krabat
                    mainFrame.isClipSet = false;
                    IntroStep++;
                    break;

                default:
                    log.error("Wrong Action!! nextActionID = {}", nextActionID);
            }
        }
    }
}  