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
import de.codengine.krabat.anims.Boom;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.anims.MillerBird;
import de.codengine.krabat.anims.RapakiRaven;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericDrawingContext2D;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MlynkCornyCholmc1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(MlynkCornyCholmc1.class);
    private GenericImage background; /* background2, */
    private GenericImage himmel1;
    private GenericImage himmel2;
    private GenericImage vorder;

    private Miller mueller;
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

    private RapakiRaven krabatvogel;
    private MillerBird muellervogel;

    private int Warten = 0;
    private boolean mlynkGibtKarte = false;

    private Boom muellermorph;
    private Boom krabatmorph;

    private int muellermorphcount;

    private boolean ismuellermorphing = false;
    private boolean iskrabatmorphing = false;

    // Konstante Points
    private static final GenericPoint mlynkFeet = new GenericPoint(171, 305);

    // konstantes Rectangle fuer den Waldvordergrund
    private static final GenericRectangle vorderWaldRect = new GenericRectangle(0, 190, 44, 209);

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public MlynkCornyCholmc1(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        // CD aus, wenn Mueller da...
        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.checkKrabat();

        mueller = new Miller(mainFrame);
        mueller.maxX = 0;
        mueller.zoomFactor = 4f;
        mueller.defaultScale = 60;

        mueller.setPos(mlynkFeet);
        mueller.setFacing(6);

        krabatvogel = new RapakiRaven(mainFrame, 172, 290, 30, -50, true);  // start, x, y, zoomfaktor, ende, gleitet nie?
        muellervogel = new MillerBird(mainFrame, -50, 270, 25, 155, false); // start, x, y, zoomfaktor, ende, isLeft?

        krabatmorph = new Boom(mainFrame);
        muellermorph = new Boom(mainFrame);

        offImage = GenericToolkit.getDefaultToolkit().createImage(640, 90);
        offGraphics = offImage.getGraphics();

        initLocation();
        mainFrame.freeze(false);
    }

    // damit die Rueckflugssequenz immer gleich ist...
    private void initMillerFlightBack() {
        muellervogel = new MillerBird(mainFrame, 155, 270, 30, -50, true);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation() {
        initImages();
        setAnim = true;
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/kolmc/kolmc2.png");
        himmel1 = getPicture("gfx/kolmc/kcsky1.png");
        himmel2 = getPicture("gfx/kolmc/kcsky2.png");
        vorder = getPicture("gfx/kolmc/kwald.png");

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
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
            mainFrame.isAnimRunning = true;
            cursorShape = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        fadeBackground();

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
            BorderRect temp = mueller.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

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
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Mueller zeichnen
        if (muellerda) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = mueller.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne sie jetzt

            // Extrawurst Karte geben
            if (mlynkGibtKarte) {
                mueller.drawMlynkWithKarte(g);
            } else {
                // Redet sie etwa gerade ??
                if (talkPerson == 36 && mainFrame.talkCount > 0) {
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
                voegelfertig = muellervogel.doFly(g);
                g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());
            } else {
                g.setClip(muellervogel.mlynkPtackRect());
                muellervogel.doFly(g);
                g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());
                g.setClip(krabatvogel.ptack2Rect());
                voegelfertig = krabatvogel.doFly(g);
                g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());
            }
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

        if (setAnim) {
            setAnim = false;

            // Hier Art der Animation festlegen
            if (!mainFrame.actions[226]) {
                nextActionID = 1000;
            } else {
                nextActionID = 1100;
            }
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1 && !isFading) {
            doAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            talkPerson = 0;
        }
        outputText = "";
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (cursorShape != 20) {
            cursorShape = 20;
            mainFrame.setCursor(mainFrame.cursorNone);
        }
    }

    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    private void fadeBackground() {
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
                    mainFrame.soundPlayer.playFile("sfx/mlynk-les.wav");
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

    private void doAction() {
        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.mousePoint);

            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1000:
                // Muellervogel soll reinfliegen, wird geskippt
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
                muellermorph.init(mlynkFeet, 35);
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
                mainFrame.isClipSet = false;
                personSays("MlynkCornyCholmc1_1", 12, 36, 2, 1020, mueller.evalMlynkTalkPoint());
                break;

            case 1020:
                // Mueller redet
                personSays("MlynkCornyCholmc1_2", 0, 36, 2, 1030, mueller.evalMlynkTalkPoint());
                break;

            case 1030:
                // Krabat spricht
                krabatSays("MlynkCornyCholmc1_3", 0, 1, 2, 1040);
                break;

            case 1040:
                // Mueller redet
                personSays("MlynkCornyCholmc1_4", 0, 36, 2, 1042, mueller.evalMlynkTalkPoint());
                break;

            case 1042:
                // Mueller redet
                personSays("MlynkCornyCholmc1_5", 0, 36, 2, 1050, mueller.evalMlynkTalkPoint());
                break;

            case 1050:
                // Mueller redet
                personSays("MlynkCornyCholmc1_6", 0, 36, 2, 1055, mueller.evalMlynkTalkPoint());
                break;

            case 1055:
                // Mueller redet
                personSays("MlynkCornyCholmc1_7", 0, 36, 2, 1060, mueller.evalMlynkTalkPoint());
                break;

            case 1060:
                // Mueller redet
                personSays("MlynkCornyCholmc1_8", 0, 36, 2, 1061, mueller.evalMlynkTalkPoint());
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
                personSays("MlynkCornyCholmc1_9", 0, 36, 2, 1064, mueller.evalMlynkTalkPoint());
                break;

            case 1064:
                // hier Unterscheidung,. ob er Feder schon hat oder noch nicht
                initMillerFlightBack();
                if (!mainFrame.actions[919]) {
                    nextActionID = 1065;
                } else {
                    nextActionID = 1290;
                }
                break;

            case 1065:
                // Mueller zurueckfaden lassen
                // Hier Karte uebergeben
                mainFrame.inventory.vInventory.addElement(20);
                muellermorph.init(mlynkFeet, 35);
                ismuellermorphing = true;
                nextActionID = 1066;
                break;


            case 1066:
                // Mueller verschwindet und Bild zurueckfaden
                if (muellermorphcount < 3) {
                    break;
                }
                mainFrame.isClipSet = false;
                muellerda = false;
                darker = false;
                nextActionID = 1067;
                break;

            case 1067:
                // warten, bis Mueller aus dem Bild ist
                if (muellermorphcount < 8) {
                    break;
                }
                mainFrame.isClipSet = false;
                ismuellermorphing = false;
                nextActionID = 1069;
                break;

            case 1069:
                // Zurueckfaden
                isFading = true;
                nextActionID = 1070;
                break;

            case 1070:
                // back to Kolmc
                mainFrame.actions[226] = true;
                createNewLocation(17, 26);
                break;

            case 1100:
                // Muellervogel soll reinfliegen, wird geskippt
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
                muellermorph.init(mlynkFeet, 35);
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
                mainFrame.isClipSet = false;
                nextActionID = 1300;
                break;

            case 1290:
                // Krabat sagt, dass er Feder hat und gibt sie
                krabatSays("MlynkCornyCholmc1_10", 0, 1, 2, 1291);
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
                initMillerFlightBack();
                personSays("MlynkCornyCholmc1_11", 0, 36, 7, 1310, mueller.evalMlynkTalkPoint());
                break;

            case 1310:
                // Mueller redet
                personSays("MlynkCornyCholmc1_12", 0, 36, 2, 1320, mueller.evalMlynkTalkPoint());
                break;

            case 1320:
                // Verzauberanim beider Leute
                muellermorph.init(mlynkFeet, 35);
                krabatmorph.init(mainFrame.krabat.getPos(), -40);  // Krabat macht hier keinen Krach
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
                mainFrame.isClipSet = false;
                nextActionID = 1344;
                break;

            case 1344:
                // warten auf Ende fliegen
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                iskrabatmorphing = false;
                mainFrame.isClipSet = false;
                Counter = 15;
                nextActionID = 1346;
                break;

            case 1346:
                // Sound nach gewisser Zeit abspielen
                if (--Counter > 1) {
                    break;
                }
                mainFrame.soundPlayer.playFile("sfx/rapak2.wav");
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
                createNewLocation(25, 26);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}