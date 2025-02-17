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
import de.codengine.krabat.anims.Bumm;
import de.codengine.krabat.anims.Mlynk2;
import de.codengine.krabat.anims.Mlynkptack;
import de.codengine.krabat.anims.PtackZaRapaka;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericDrawingContext2D;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MlynkCornyCholmc1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(MlynkCornyCholmc1.class);
    private GenericImage background; /* background2, */
    private GenericImage himmel1;
    private GenericImage himmel2;
    private GenericImage vorder;

    private Mlynk2 mueller;
    private boolean muellerda = false;
    private boolean krabatda = true;
    private boolean voegelda = false;
    private boolean voegelfertig = false;
    private boolean setAnim = false;
    private boolean muellerFliegtAllein = true;

    private boolean isFading = true;
    private int Fadecount = 0;
    private boolean darker = true;
    private GenericImage offImage;
    private GenericDrawingContext offGraphics;

    private PtackZaRapaka krabatvogel;
    private Mlynkptack muellervogel;

    private int Warten = 0;
    private boolean mlynkGibtKarte = false;

    private Bumm muellermorph;
    private Bumm krabatmorph;

    private int muellermorphcount;
    // private int krabatmorphcount;

    private boolean ismuellermorphing = false;
    private boolean iskrabatmorphing = false;

    // Konstanten - Rects deklarieren
    //   private static final borderrect obererAusgang  = new borderrect (120, 189, 177, 230);
    //   private static final borderrect untererAusgang = new borderrect (376, 451, 590, 479);
    //   private static final borderrect waldRect       = new borderrect (  0,   0, 366, 194);

    // Konstante Points
//    private static final GenericPoint Pup          = new GenericPoint (151, 268);
    //  private static final GenericPoint Pdown        = new GenericPoint (497, 479);
    //  private static final GenericPoint Pwald        = new GenericPoint (213, 376);
    private static final GenericPoint mlynkFeet = new GenericPoint(171, 305);

    // konstantes Rectangle fuer den Waldvordergrund
    private static final GenericRectangle vorderWaldRect = new GenericRectangle(0, 190, 44, 209);

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public MlynkCornyCholmc1(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        // CD aus, wenn Mueller da...
        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.CheckKrabat();

        // es werden die alten Werte fuer Krabat erhalten, wie sie aus Kolmc stammen
  	/*mainFrame.krabat.maxx = 458;
	  mainFrame.krabat.zoomf = 2.79f;
	  mainFrame.krabat.defScale = 0;*/

        mueller = new Mlynk2(mainFrame);
        mueller.maxx = 0;
        mueller.zoomf = 4f;
        mueller.defScale = 60;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(6);

        krabatvogel = new PtackZaRapaka(mainFrame, 172, 290, 30, -50, true);  // start, x, y, zoomfaktor, ende, gleitet nie?
        muellervogel = new Mlynkptack(mainFrame, -50, 270, 25, 155, false); // start, x, y, zoomfaktor, ende, isLeft?

        krabatmorph = new Bumm(mainFrame);
        muellermorph = new Bumm(mainFrame);

        offImage = GenericToolkit.getDefaultToolkit().createImage(640, 90);
        offGraphics = offImage.getGraphics();

        InitLocation();
        mainFrame.Freeze(false);
    }

    // damit die Rueckflugssequenz immer gleich ist...
    private void InitMuellerRueckflug() {
        muellervogel = new Mlynkptack(mainFrame, 155, 270, 30, -50, true);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        InitImages();
        setAnim = true;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/kolmc/kolmc2.gif");
        himmel1 = getPicture("gfx/kolmc/kcsky1.gif");
        himmel2 = getPicture("gfx/kolmc/kcsky2.gif");
        vorder = getPicture("gfx/kolmc/kwald.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        himmel1 = null;
        himmel2 = null;
        vorder = null;

        mueller.cleanup();
        mueller = null;

        offImage = null;
        offGraphics = null;

        krabatvogel.cleanup();
        krabatvogel = null;
        muellervogel.cleanup();
        muellervogel = null;
        muellermorph.cleanup();
        muellermorph = null;
        krabatmorph.cleanup();
        krabatmorph = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
            mainFrame.fPlayAnim = true;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
        }

        FadeBackground();

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);
        g.setClip(vorderWaldRect);
        g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());


        // Himmel - Fading ermoeglichen
        g.setClip(0, 0, 640, 80);
        g.drawImage(offImage, 0, 0);
        g.drawImage(background, 0, 0);

        // Mlynk Hintergrund loeschen
        if (muellerda) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        // wenn Krabat morpht, dann diesen Hintergrund loeschen
        if (iskrabatmorphing) {
            g.setClip(krabatmorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        // Voegel Hintergrund loeschen
        if (voegelda) {
            g.setClip(muellervogel.mlynkPtackRect());
            g.drawImage(background, 0, 0);

            if (!muellerFliegtAllein) {
                g.setClip(krabatvogel.ptack2Rect());
                g.drawImage(background, 0, 0);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Mueller zeichnen
        if (muellerda) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne sie jetzt

            // Extrawurst Karte geben
            if (mlynkGibtKarte) {
                mueller.drawMlynkWithKarte(g);
            } else {
                // Redet sie etwa gerade ??
                if (TalkPerson == 36 && mainFrame.talkCount > 0) {
                    mueller.talkMlynk(g);
                }

                // nur rumstehen oder laufen
                else {
                    mueller.drawMlynk(g);
                }
            }
        }

        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }

        if (krabatda) {
            // Animation??
            if (mainFrame.krabat.nAnimation != 0) {
                mainFrame.krabat.DoAnimation(g);

                // Cursorruecksetzung nach Animationsende
                if (mainFrame.krabat.nAnimation == 0) {
                    evalMouseMoveEvent(mainFrame.Mousepoint);
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
                            // Krabat steht nur da
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

        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (iskrabatmorphing) {
            g.setClip(krabatmorph.bummRect());
            /* krabatmorphcount = */
            krabatmorph.drawBumm(g);
        }

        // Voegel aus Bild rausfliegen lassen
        if (voegelda) {
            if (muellerFliegtAllein) {
                g.setClip(muellervogel.mlynkPtackRect());
                voegelfertig = muellervogel.Flieg(g);
                g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());
            } else {
                g.setClip(muellervogel.mlynkPtackRect());
                muellervogel.Flieg(g);
                g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());
                g.setClip(krabatvogel.ptack2Rect());
                voegelfertig = krabatvogel.Flieg(g);
                g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());
            }
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        if (setAnim) {
            setAnim = false;

            // Hier Art der Animation festlegen
            if (!mainFrame.Actions[226]) {
                nextActionID = 1000;
            } else {
                nextActionID = 1100;
            }
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1 && !isFading) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // GenericPoint pTemp = e.getPoint ();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.Nix);
        }
    }

    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    private void FadeBackground() {
        if (!isFading) {
            return;
        }

        if (darker) {
            // nach Richtung dunkel faden
            GenericDrawingContext2D g2 = offGraphics.get2DContext();

            GenericAlphaComposite ac = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, 1);
            g2.setComposite(ac);
            g2.drawImage(himmel1, 0, 0);

            float helper = Fadecount;
            ac = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, helper / 40);
            g2.setComposite(ac);
            g2.drawImage(himmel2, 0, 0);

            if (Fadecount < 40) {
                if (Fadecount == 1) {
                    mainFrame.wave.PlayFile("sfx/mlynk-les.wav");
                }
                Fadecount++;
            } else {
                isFading = false;
            }
        } else {
            // nach Richtung hell faden
            GenericDrawingContext2D g2 = offGraphics.get2DContext();

            GenericAlphaComposite ac = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, 1);
            g2.setComposite(ac);
            g2.drawImage(himmel1, 0, 0);

            float helper = Fadecount;
            ac = GenericAlphaComposite.getInstance(GenericAlphaComposite.SRC_OVER, helper / 40);
            g2.setComposite(ac);
            g2.drawImage(himmel2, 0, 0);

            if (Fadecount > 0) {
                Fadecount--;
            } else {
                isFading = false;
            }
        }

    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.Mousepoint);

            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1000:
                // Muellervogel soll reinfliegen, wird geskippt
                // mainFrame.wave.PlayFile ("sfx/mlynk-les.wav");
                // voegelda = true;
                Counter = 30;
                nextActionID = 1004;
                break;

            case 1003:
                // Warten ,bis da
                if (!voegelfertig) {
                    nextActionID = 1004;
                }
                Counter = 20;
                break;

            case 1004:
                // Morphsequenz zeichnen
                if (--Counter > 1) {
                    break;
                }
                muellermorph.Init(mlynkFeet, 35);
                ismuellermorphing = true;
                nextActionID = 1005;
                break;

            case 1005:
                // Mueller erscheinen lassen
                if (muellermorphcount < 3) {
                    break;
                }
                muellerda = true;
                voegelda = false;
                nextActionID = 1010;
                break;

            case 1010:
                // Mueller redet
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                mainFrame.Clipset = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00000"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00001"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00002"),
                        12, 36, 2, 1020, mueller.evalMlynkTalkPoint());
                break;

            case 1020:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00003"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00004"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00005"),
                        0, 36, 2, 1030, mueller.evalMlynkTalkPoint());
                break;

            case 1030:
                // Krabat spricht
                KrabatSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00006"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00007"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00008"),
                        0, 1, 2, 1040);
                break;

            case 1040:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00009"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00010"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00011"),
                        0, 36, 2, 1042, mueller.evalMlynkTalkPoint());
                break;

            case 1042:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00012"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00013"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00014"),
                        0, 36, 2, 1050, mueller.evalMlynkTalkPoint());
                break;

            case 1050:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00015"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00016"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00017"),
                        0, 36, 2, 1055, mueller.evalMlynkTalkPoint());
                break;

            case 1055:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00018"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00019"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00020"),
                        0, 36, 2, 1060, mueller.evalMlynkTalkPoint());
                break;

            case 1060:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00021"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00022"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00023"),
                        0, 36, 2, 1061, mueller.evalMlynkTalkPoint());
                break;

            case 1061:
                // Mueller gibt Karte
                mlynkGibtKarte = true;
                Warten = 10;
                nextActionID = 1062;
                break;

            case 1062:
                // warten bis Ende
                if (Warten == 4) {
                    mainFrame.krabat.nAnimation = 121;
                }
                if (--Warten < 1) {
                    nextActionID = 1063;
                    mlynkGibtKarte = false;
                }
                break;

            case 1063:
                // Mueller redet
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00024"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00025"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00026"),
                        0, 36, 2, 1064, mueller.evalMlynkTalkPoint());
                break;

            case 1064:
                // hier Unterscheidung,. ob er Feder schon hat oder noch nicht
                InitMuellerRueckflug();
                if (!mainFrame.Actions[919]) {
                    nextActionID = 1065;
                } else {
                    nextActionID = 1290;
                }
                break;

            case 1065:
                // Mueller zurueckfaden lassen
                // Hier Karte uebergeben
                mainFrame.inventory.vInventory.addElement(20);
                muellermorph.Init(mlynkFeet, 35);
                ismuellermorphing = true;
                nextActionID = 1066;
                break;


            case 1066:
                // Mueller verschwindet und Bild zurueckfaden
                if (muellermorphcount < 3) {
                    break;
                }
                mainFrame.Clipset = false;
                muellerda = false;
                // voegelda = true;
                darker = false;
                nextActionID = 1067;
                break;

            case 1067:
                // warten, bis Mueller aus dem Bild ist
                if (muellermorphcount < 8) {
                    break;
                }
                mainFrame.Clipset = false;
                ismuellermorphing = false;
                nextActionID = 1069;
                break;

            case 1069:
                // Zurueckfaden
                // if (voegelfertig == true) break;
                // voegelda = false;
                isFading = true;
                nextActionID = 1070;
                break;

            case 1070:
                // back to Kolmc
                mainFrame.Actions[226] = true;
                NeuesBild(17, 26);
                break;

            case 1100:
                // Muellervogel soll reinfliegen, wird geskippt
                // mainFrame.wave.PlayFile ("sfx/mlynk-les.wav");
                // voegelda = true;
                Counter = 30;
                nextActionID = 1120;
                break;

            case 1110:
                // Warten ,bis da
                if (!voegelfertig) {
                    nextActionID = 1120;
                }
                Counter = 20;
                break;

            case 1120:
                // Morphsequenz zeichnen
                if (--Counter > 1) {
                    break;
                }
                muellermorph.Init(mlynkFeet, 35);
                ismuellermorphing = true;
                nextActionID = 1130;
                break;

            case 1130:
                // Mueller erscheinen lassen
                if (muellermorphcount < 3) {
                    break;
                }
                muellerda = true;
                voegelda = false;
                nextActionID = 1140;
                break;

            case 1140:
                // Mueller redet
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                mainFrame.Clipset = false;
                nextActionID = 1300;
                break;

            case 1290:
                // Krabat sagt, dass er Feder hat und gibt sie
                KrabatSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00027"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00028"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00029"),
                        0, 1, 2, 1291);
                break;

            case 1291:
                // Feder geben
                mainFrame.krabat.nAnimation = 121;
                nextActionID = 1292;
                break;

            case 1292:
                // warten auf Ende geben
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 1300;
                }
                break;

            case 1300:
                // Mueller redet
                muellerda = true;
                InitMuellerRueckflug();
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00030"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00031"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00032"),
                        0, 36, 7, 1310, mueller.evalMlynkTalkPoint());
                break;

            case 1310:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00033"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00034"),
                        Start.stringManager.getTranslation("Loc1_MlynkCornyCholmc1_00035"),
                        0, 36, 2, 1320, mueller.evalMlynkTalkPoint());
                break;

            case 1320:
                // Verzauberanim beider Leute
                muellermorph.Init(mlynkFeet, 35);
                krabatmorph.Init(mainFrame.krabat.getPos(), -40);  // Krabat macht hier keinen Krach
                ismuellermorphing = true;
                iskrabatmorphing = true;
                nextActionID = 1342;
                break;

            case 1342:
                // Leute verschwinden und Voegel erscheinen
                if (muellermorphcount < 3) {
                    break;
                }
                muellerda = false;
                krabatda = false;
                voegelda = true;
                muellerFliegtAllein = false;
                mainFrame.Clipset = false;
                nextActionID = 1344;
                break;

            case 1344:
                // warten auf Ende fliegen
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                iskrabatmorphing = false;
                mainFrame.Clipset = false;
                Counter = 15;
                nextActionID = 1346;
                break;

            case 1346:
                // Sound nach gewisser Zeit abspielen
                if (--Counter > 1) {
                    break;
                }
                mainFrame.wave.PlayFile("sfx/rapak2.wav");
                nextActionID = 1348;
                break;

            case 1348:
                // warten auf Ende fliegen
                if (!voegelfertig) {
                    nextActionID = 1350;
                }
                break;

            case 1350:
                // Skip zur Muehle
                NeuesBild(25, 26);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}