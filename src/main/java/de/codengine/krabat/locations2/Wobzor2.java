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
import de.codengine.krabat.anims.Bumm;
import de.codengine.krabat.anims.Mlynk2;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Wobzor2 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Wobzor2.class);
    private GenericImage background;
    private GenericImage horiz3;
    private GenericImage horiz4;
    private final GenericImage[] Wasser;
    private boolean switchanim = false;
    private int wassercount = 1;
    private boolean forward = true;
    private Mlynk2 mueller;

    private boolean muellerVisible = false;

    private Bumm muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // Vordergrund - Rects
    private static final Borderrect horiz3Rect = new Borderrect(179, 175, 270, 248);
    private static final Borderrect horiz4Rect = new Borderrect(528, 177, 639, 219);

    // Konstanten - Points
    private static final GenericPoint Pkrabat = new GenericPoint(153, 231);
    private static final GenericPoint mlynkFeet = new GenericPoint(180, 230);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wobzor2(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 4f;
        mainFrame.krabat.defScale = 80;

        mainFrame.krabat.setPos(Pkrabat);
        mainFrame.krabat.SetFacing(3);

        Wasser = new GenericImage[9];
        mueller = new Mlynk2(mainFrame);

        muellermorph = new Bumm(mainFrame);

        mueller.maxx = 0;
        mueller.zoomf = 4f;
        mueller.defScale = 80;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(9);

        InitLocation();

        mainFrame.freeze(false);

        nextActionID = 10;
        TalkPause = 10;
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/horiz/horiz2.png");
        horiz3 = getPicture("gfx/horiz/horiz3.png");
        horiz4 = getPicture("gfx/horiz/horiz4.png");

        Wasser[1] = getPicture("gfx/horiz/hww5.png");
        Wasser[2] = getPicture("gfx/horiz/hww4.png");
        Wasser[3] = getPicture("gfx/horiz/hww3.png");
        Wasser[4] = getPicture("gfx/horiz/hww2.png");
        Wasser[5] = getPicture("gfx/horiz/hw3.png");
        Wasser[6] = getPicture("gfx/horiz/hw4.png");
        Wasser[7] = getPicture("gfx/horiz/hw5.png");
        Wasser[8] = getPicture("gfx/horiz/hw6.png");

    }

    @Override
    public void cleanup() {
        background = null;
        horiz3 = null;
        horiz4 = null;

        Wasser[1] = null;
        Wasser[2] = null;
        Wasser[3] = null;
        Wasser[4] = null;
        Wasser[5] = null;
        Wasser[6] = null;
        Wasser[7] = null;
        Wasser[8] = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
            mainFrame.isAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        if (mainFrame.isBackgroundAnimRunning) {
            switchanim = !switchanim;
            if (switchanim) {
                if (forward) {
                    wassercount++;
                    if (wassercount == 9) {
                        wassercount = 7;
                        forward = false;
                    }
                } else {
                    wassercount--;
                    if (wassercount == 0) {
                        wassercount = 2;
                        forward = true;
                    }
                }
            }
            g.setClip(303, 356, 338, 125);
            g.drawImage(Wasser[wassercount], 303, 356);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        if (muellerVisible) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.getRect();
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

        // Krabat zeichnen
        mainFrame.krabat.drawKrabat(g);

        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (horiz3Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(horiz3, 197, 186);
        }

        // hinterm horiz4 (nur Clipping - Region wird neugezeichnet)
        if (horiz4Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(horiz4, 543, 186);
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

        if (mainFrame.talkCount < 1 && TalkPause > 0) {
            TalkPause--;
        }

        if (mainFrame.talkCount > 0) {
            mainFrame.talkCount--;
            if (mainFrame.talkCount < 1) {
                mainFrame.isClipSet = false;
                outputText = "";
            }
        }


        // Gibt es was zu tun ?
        if (nextActionID != 0 && mainFrame.talkCount < 1 && TalkPause < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // GenericPoint pTemp = e.getPoint ();
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.cursorNone);
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 10:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 25);  // 68 - 100 - scaleMueller
                ismuellermorphing = true;
                nextActionID = 13;
                break;

            case 13:
                // Mueller erscheint
                if (muellermorphcount < 3) {
                    break;
                }
                muellerVisible = true;
                nextActionID = 18;
                break;

            case 18:
                // Mueller sagt seinen Spruch
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                mainFrame.isClipSet = false;
                MuellerMecker(mueller.evalMlynkTalkPoint());
                TalkPerson = 36;
                TalkPause = 5;
                nextActionID = 20;
                break;

            case 20:
                // Gehe zu Muehle zurueck
                NeuesBild(90, 88);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}