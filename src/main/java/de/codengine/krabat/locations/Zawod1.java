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
import de.codengine.krabat.anims.Husa;
import de.codengine.krabat.anims.IntroDomaPtack;
import de.codengine.krabat.anims.LogoPtack;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericDrawingContext2D;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Zawod1 extends Mainloc {
    private GenericImage domal, domar, sky, kij, rapaki, intro1, intro2, budysin, budysky;
    private GenericImage sitz1, sitz2;
    private final GenericImage[] floete;
    private int IntroStep = 1;
    private int nextActionID = 0;
    private IntroDomaPtack ptack1, ptack2;
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

    private LogoPtack vogel1, vogel2, vogel3;

    private int skipActionID = 0;

    private int Farb = 1;

    private int BildIndex = 1;

    private int floetenIndex = 0;
    private int zwinkerIndex = 0;

    private int Verhinderfloete = 3;
    private static final int MAX_VERHINDERFLOETE = 3;

    private int CDtrack = 22;

    private long FloetenSpielZeit = 0; // zaehlt die Zeit, wielange Krabat Floete spielen soll

    private static final String HText1 = Start.stringManager.getTranslation("Loc1_Zawod1_00000");
    private static final String DText1 = Start.stringManager.getTranslation("Loc1_Zawod1_00001");
    private static final String NText1 = Start.stringManager.getTranslation("Loc1_Zawod1_00002");


    private static final String HText2 = Start.stringManager.getTranslation("Loc1_Zawod1_00003");
    private static final String DText2 = Start.stringManager.getTranslation("Loc1_Zawod1_00004");
    private static final String NText2 = Start.stringManager.getTranslation("Loc1_Zawod1_00005");


    private static final String HText3 = Start.stringManager.getTranslation("Loc1_Zawod1_00006");
    private static final String DText3 = Start.stringManager.getTranslation("Loc1_Zawod1_00007");
    private static final String NText3 = Start.stringManager.getTranslation("Loc1_Zawod1_00008");


    private int Zeile4 = 0;
    private static final int ENDZEILE_VIER = 9;
    private static final String[][] Speicher4 = {{Start.stringManager.getTranslation("Loc1_Zawod1_00009"), Start.stringManager.getTranslation("Loc1_Zawod1_00010"), Start.stringManager.getTranslation("Loc1_Zawod1_00011"), Start.stringManager.getTranslation("Loc1_Zawod1_00012"), Start.stringManager.getTranslation("Loc1_Zawod1_00013"), Start.stringManager.getTranslation("Loc1_Zawod1_00014"), Start.stringManager.getTranslation("Loc1_Zawod1_00015"), Start.stringManager.getTranslation("Loc1_Zawod1_00016"), Start.stringManager.getTranslation("Loc1_Zawod1_00017")},
            {Start.stringManager.getTranslation("Loc1_Zawod1_00018"), Start.stringManager.getTranslation("Loc1_Zawod1_00019"), Start.stringManager.getTranslation("Loc1_Zawod1_00020"), Start.stringManager.getTranslation("Loc1_Zawod1_00021"), Start.stringManager.getTranslation("Loc1_Zawod1_00022"), Start.stringManager.getTranslation("Loc1_Zawod1_00023"), Start.stringManager.getTranslation("Loc1_Zawod1_00024"), Start.stringManager.getTranslation("Loc1_Zawod1_00025"), Start.stringManager.getTranslation("Loc1_Zawod1_00026")},
            {Start.stringManager.getTranslation("Loc1_Zawod1_00027"), Start.stringManager.getTranslation("Loc1_Zawod1_00028"), Start.stringManager.getTranslation("Loc1_Zawod1_00029"), Start.stringManager.getTranslation("Loc1_Zawod1_00030"), Start.stringManager.getTranslation("Loc1_Zawod1_00031"), Start.stringManager.getTranslation("Loc1_Zawod1_00032"), Start.stringManager.getTranslation("Loc1_Zawod1_00033"), Start.stringManager.getTranslation("Loc1_Zawod1_00034"), Start.stringManager.getTranslation("Loc1_Zawod1_00035")}};


    private static final GenericPoint MittelPunkt = new GenericPoint(320, 240);
    private int TalkPause = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von diesem Intro erzeugen
    public Zawod1(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

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

        mainFrame.Clipset = false;

        mainFrame.krabat.SetKrabatPos(new GenericPoint(128, 352));
        mainFrame.krabat.SetFacing(3);

        mainFrame.krabat.maxx = 402;
        mainFrame.krabat.zoomf = 2.32f;
        mainFrame.krabat.defScale = 0;

        offImage = GenericToolkit.getDefaultToolkit().createImage(250, 200);
        offGraphics = offImage.getGraphics();

        Counter = 0; // nur zur Sicherheit

        mainFrame.Freeze(false);
    }

    // Bilder vorbereiten
    private void InitImages() {
        rapaki = getPicture("gfx/intro/rap100.gif");
        domal = getPicture("gfx/doma/dom-l.gif");
        domar = getPicture("gfx/doma/dom-r.gif");
        sky = getPicture("gfx/doma/domsky.gif");
        kij = getPicture("gfx/doma/doma2.gif");

        intro1 = getPicture("gfx/intro/intro1.gif");
        intro2 = getPicture("gfx/intro/intro2.gif");
        budysin = getPicture("gfx/intro/bzpali.gif");
        budysky = getPicture("gfx/intro/bzsky.gif");

        sitz1 = getPicture("gfx/anims/ksedzo1.gif");
        sitz2 = getPicture("gfx/anims/ksedzo1a.gif");

        floete[0] = getPicture("gfx/anims/ks-f1.gif");
        floete[1] = getPicture("gfx/anims/ks-f2.gif");
        floete[2] = getPicture("gfx/anims/ks-f3.gif");
        floete[3] = getPicture("gfx/anims/ks-f4.gif");

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
            if (!mainFrame.Clipset) {
                mainFrame.Clipset = true;
                Cursorform = 200;
                mainFrame.fPlayAnim = true;
                mainFrame.scrollx = 0;
                mainFrame.scrolly = 0;
                g.setClip(0, 0, 644, 484);
                g.clearRect(0, 0, 640, 480);
                evalMouseMoveEvent(mainFrame.Mousepoint);
                if (!playing) {
                    playing = true;
                    mainFrame.wave.PlayFile("gamesound.wav"); // passiert nicht mehr,nur Gag
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
                g2.drawImage(rapaki, 0, 0, null);
                if (Help < 40) {
                    Help++;
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                } else {
                    nextActionID = 1;
                }
                g.drawImage(offImage, 205, 150, null);
            }

            // Voegel durch das Bild fliegen lassen
            if (IntroStep == 2) {
                g.setClip(0, 0, 644, 484);
                g.clearRect(0, 170, 640, 180);
                g.drawImage(rapaki, 205, 150, null);
                vogel1.Flieg(g);
                vogel2.Flieg(g);
                boolean ende = vogel3.Flieg(g);
                if (!ende) {
                    nextActionID = 1;
                }

                // hier den Sound eval.
                if (++Counter == 30) {
                    mainFrame.wave.PlayFile("sfx/rapak1.wav");
                }
                if (Counter == 60) {
                    mainFrame.wave.PlayFile("sfx/rapak2.wav");
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
                g3.drawImage(rapaki, 0, 0, null);
                if (Help > 0) {
                    Help--;
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                } else {
                    nextActionID = 3;
                }
                g.drawImage(offImage, 205, 150, null);
            }
        }

        // Textausgabe auf leeren Screen (noch keine Bilder da), aber getrennt nach vorgesehenen Bildern
        if (IntroStep > 9 && IntroStep < 20) {
            // Wiederherstellen ist zwar Quatsch, aber wer weiss
            if (!mainFrame.Clipset) {
                g.setClip(0, 0, 644, 484);
                mainFrame.Clipset = true;
                Cursorform = 200;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }

            if (BildIndex == 1) {
                rapaki = null;
                g.drawImage(budysky, 0, 0, null);
                g.drawImage(budysin, 0, 0, null);
            }
            if (BildIndex == 2) {
                budysky = null;
                budysin = null;
                g.drawImage(intro1, 0, 0, null);
            }

            if (BildIndex == 3) {
                intro1 = null;
                g.drawImage(intro2, 0, 0, null);
            }

            // Textausgabe
            if (outputText != "") {
                g.setClip(0, 0, 644, 484);
                mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[13]);
            }

            // Textausgabezeit mit talkCount realisieren
            if (mainFrame.talkCount > 1) {
                mainFrame.talkCount--;
                if (mainFrame.talkCount == 1) {
                    mainFrame.Clipset = false;
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
            if (!mainFrame.Clipset) {
                mainFrame.Clipset = true;
                Cursorform = 200;
                mainFrame.fPlayAnim = true;
                if (setScroll) {
                    setScroll = false;
                    mainFrame.scrollx = Scrollwert;
                }
                g.setClip(0, 0, 1284, 484);
                g.drawImage(sky, mainFrame.scrollx / 10, 0);
                g.drawImage(domal, 0, 0);
                g.drawImage(domar, 640, 0);
                g.drawImage(kij, 80, 325);
                evalKrabat(g);
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }

            // Voegel fliegen mit Scrolling bis letzter Vogel weg und Scroller zu Ende
            if (IntroStep == 100) {
                intro2 = null;
                int xx = mainFrame.scrollx - 10;
                if (xx < 0) {
                    xx = 0;
                }
                g.setClip(xx, 0, xx + 650, 285);
                g.drawImage(sky, mainFrame.scrollx / 10, 0);
                g.drawImage(domal, 0, 0);
                g.drawImage(domar, 640, 0);
                g.drawImage(kij, 80, 325);
                evalKrabat(g);
                g.setClip(0, 0, 900, 250);
                ptack1.Flieg(g);
                mainFrame.scrollx -= 1;
                if (mainFrame.scrollx < 0) {
                    mainFrame.scrollx = 0;
                }
                if (!ptack2.Flieg(g) && mainFrame.scrollx == 0) {
                    IntroStep++;
                }
                evalSound();
            }

            // Gaense animieren
            if (true) {
                g.setClip(120, 255, 230, 110);
                g.drawImage(sky, mainFrame.scrollx / 10, 0);
                g.drawImage(domal, 0, 0);
                g.drawImage(domar, 640, 0);
                gans1.BewegeGans(g);
                gans2.BewegeGans(g);
                gans3.BewegeGans(g);
            }

// 		if (FloetenSpielZeit < 300) FloetenSpielZeit++; // Floete nur 1x waehrend repaint erhoehen
// 		System.out.println (System.currentTimeMillis());

            evalKrabat(g);

            // Textausgabe
            if (outputText != "") {
                g.setClip(0, 0, 644, 484);
                mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, Farb);
            }

            // Textausgabezeit mit talkCount realisieren
            if (mainFrame.talkCount > 1) {
                mainFrame.talkCount--;
                if (mainFrame.talkCount == 1) {
                    mainFrame.Clipset = false;
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
                    g.drawImage(floete[floetenIndex], 103, 316, null);
                    // System.out.print (FloetenSpielZeit + " ");
                    break;
                }  // ansonsten gehts jetzt mit diesen cases weiter (denken)

            case 103: // Nachdenken
            case 104:
                // Zwinker - Routine
                g.setClip(103, 316, 27, 37);
                g.drawImage(domal, 0, 0);
                int zuffi = (int) Math.random() * 50;
                if (zwinkerIndex == 0 && zuffi > 45) {
                    zwinkerIndex = 1;
                } else {
                    zwinkerIndex = 0;
                }
                g.drawImage(zwinkerIndex == 0 ? sitz1 : sitz2, 103, 316, null);
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
                System.out.println("Falscher Krabat gewuenscht !");
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
            mainFrame.Clipset = false;
            outputText = "";
        }

        // linke Maustaste
        if (e.isLeftClick()) {
        }

        // rechte Maustaste
        else {
            // bei Intro - Bildern Mainmenu erlauben
            // > 0 = alles, > 9 = nur nach Rapaki-Intro
            if (IntroStep > 9 && IntroStep < 20) {
                // Hauptmenue aufrufen
                mainFrame.mainmenu.introcall = true;
                skipActionID = 100;
                mainFrame.repaint();
            }
        }
    }

    // MouseMove - Auswertung + Cursorformen

    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (mainFrame.fPlayAnim) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
        } else {
            if (Cursorform != 0) {
                Cursorform = 0;
                mainFrame.setCursor(mainFrame.Normal);
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
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
                mainFrame.mainmenu.introcall = true;
                skipActionID = 100;
                mainFrame.repaint();
                return;
            }

            // Load - Screen aktivieren
            if (Taste == GenericKeyEvent.VK_F3) {
                System.out.println("----- Enable open screen in intro! -----");
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

            mainFrame.wave.PlayFile("sfx/rapak" + (char) zweiteZahl + ".wav");
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
                    mainFrame.Clipset = false;
                    mainFrame.fPlayAnim = true;
                    nextActionID = 0;
                    skipActionID = 0;
                    break;

                case 20:
                    // Skip Bilder - Voegel
                    IntroStep = 15;
                    Scrollwert = 200;
                    outputText = "";
                    setScroll = true;
                    mainFrame.Clipset = false;
                    mainFrame.fPlayAnim = true;
                    nextActionID = 0;
                    skipActionID = 0;
                    break;

                case 100:
                    // Mainmenu aktivieren
                    mainFrame.fPlayAnim = false;
                    mainFrame.whatScreen = 2;
                    skipActionID = 0;
                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                    Cursorform = 200;
                    break;

                case 120:
                    // Load - Screen aktivieren
                    mainFrame.fPlayAnim = false;
                    skipActionID = 0;
                    mainFrame.ConstructLocation(102);
                    mainFrame.whatScreen = 3;
                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                    Cursorform = 200;
                    break;

                case 200:
                    // Spiel starten
                    mainFrame.fPlayAnim = false;
                    mainFrame.Clipset = false;
                    mainFrame.mainmenu.introcall = false;
                    // Einmalige Ausnahme, wegen Gaense uebergeben
                    mainFrame.ConstructLocation(6, gans1, gans2, gans3);
                    mainFrame.DestructLocation(100);
                    mainFrame.repaint();
                    skipActionID = 0;
                    break;

                default:
                    System.out.println("Wrong SkipAction !!");
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
                    mainFrame.Clipset = false;
                    mainFrame.fPlayAnim = true;
                    nextActionID = 0;
                    break;

                case 10:
                    // Einfuehrungstext
                    // mainFrame.player.Play ("2", -133600);
                    if (mainFrame.sprache == 1) {
                        outputText = Start.stringManager.getTranslation("Loc1_Zawod1_00036");
                    }
                    if (mainFrame.sprache == 2) {
                        outputText = Start.stringManager.getTranslation("Loc1_Zawod1_00037");
                    }
                    if (mainFrame.sprache == 3) {
                        outputText = Start.stringManager.getTranslation("Loc1_Zawod1_00038");
                    }
                    outputTextPos = mainFrame.ifont.CenterText(outputText, MittelPunkt);
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
                    if (mainFrame.sprache == 1) {
                        outputText = mainFrame.ifont.TeileText(HText1);
                    }
                    if (mainFrame.sprache == 2) {
                        outputText = mainFrame.ifont.TeileText(DText1);
                    }
                    if (mainFrame.sprache == 3) {
                        outputText = mainFrame.ifont.TeileText(NText1);
                    }
                    outputTextPos = mainFrame.ifont.CenterText(outputText, new GenericPoint(320, 450));
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
                    if (mainFrame.sprache == 1) {
                        outputText = mainFrame.ifont.TeileText(HText2);
                    }
                    if (mainFrame.sprache == 2) {
                        outputText = mainFrame.ifont.TeileText(DText2);
                    }
                    if (mainFrame.sprache == 3) {
                        outputText = mainFrame.ifont.TeileText(NText2);
                    }
                    outputTextPos = mainFrame.ifont.CenterText(outputText, new GenericPoint(320, 450));
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
                    mainFrame.Clipset = false;
                    mainFrame.fPlayAnim = true;
                    nextActionID = 0;
                    break;

                case 100: // nix zu tun...
                    break;

                case 101:
                    // Text ueber Krabat (Erzaehler)
                    if (mainFrame.sprache == 1) {
                        outputText = mainFrame.ifont.TeileText(HText3);
                    }
                    if (mainFrame.sprache == 2) {
                        outputText = mainFrame.ifont.TeileText(DText3);
                    }
                    if (mainFrame.sprache == 3) {
                        outputText = mainFrame.ifont.TeileText(NText3);
                    }
                    outputTextPos = mainFrame.ifont.CenterText(outputText, MittelPunkt);
                    IntroStep++;
                    TalkPause = 5;
                    break;

                case 102: // Kleine Pause, wg. Introstep - Variablenwert
                    IntroStep++;
                    break;

                case 103:
                    // Text von Krabat denkend
                    outputText = mainFrame.ifont.TeileText(Speicher4[mainFrame.sprache - 1][Zeile4]);
                    outputTextPos = mainFrame.ifont.KrabatText(outputText);
                    Zeile4++;
                    if (Zeile4 == ENDZEILE_VIER) {
                        IntroStep++;
                    } else {
                        nextActionID = 0;
                    }
                    TalkPause = 5;
                    break;

                case 104:
                    // Text von Krabat redend
                    mainFrame.Clipset = false;
                    if (mainFrame.sprache == 1) {
                        outputText = Start.stringManager.getTranslation("Loc1_Zawod1_00039");
                    }
                    if (mainFrame.sprache == 2) {
                        outputText = Start.stringManager.getTranslation("Loc1_Zawod1_00040");
                    }
                    if (mainFrame.sprache == 3) {
                        outputText = Start.stringManager.getTranslation("Loc1_Zawod1_00041");
                    }
                    outputTextPos = mainFrame.ifont.KrabatText(outputText);
                    IntroStep++;
                    TalkPause = 3;
                    break;

                case 105:
                    // alles loeschen wg. Redendem Krabat
                    mainFrame.Clipset = false;
                    IntroStep++;
                    break;

                default:
                    System.out.println("Wrong Action !!");
            }
        }
    }
}  