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
import de.codengine.krabat.anims.Boom;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Labyr122 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Labyr122.class);
    private GenericImage background;
    private GenericImage lab122;
    private GenericImage lab123;
    private Miller mueller;
    private boolean setAnim = true;

    private boolean muellerVisible = false;

    private Boom muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;


    // Vordergrund - Rects
    private static final BorderRect lab122Rect = new BorderRect(296, 162, 610, 355);
    private static final BorderRect lab123Rect = new BorderRect(189, 329, 252, 377);

    // Konstante Points
    // private static final GenericPoint Pright    = new GenericPoint (639, 282);
    // private static final GenericPoint Pdown     = new GenericPoint (254, 479);
    private static final GenericPoint mlynkFeet = new GenericPoint(288, 344);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Labyr122(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 6.97f;
        mainFrame.krabat.defScale = -30;

        mueller = new Miller(mainFrame);

        muellermorph = new Boom(mainFrame);

        mueller.maxx = 0;
        mueller.zoomf = 4f;
        mueller.defScale = 0;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(6);

        InitLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        // von unten aus
        mainFrame.krabat.setPos(new GenericPoint(254, 452));
        mainFrame.krabat.SetFacing(12);
        TalkPause = 10;
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/labyrinth/laby12.png");
        lab122 = getPicture("gfx/labyrinth/lab12-2.png");
        lab123 = getPicture("gfx/labyrinth/lab12-3.png");

    }

    @Override
    public void cleanup() {
        background = null;
        lab122 = null;
        lab123 = null;

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

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        if (muellerVisible) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu

            // Redet er etwa gerade ??
            if (TalkPerson == 36 && mainFrame.talkCount > 0) {
                mueller.talkMlynk(g);
            }

            // nur rumstehen oder laufen
            else {
                mueller.drawMlynk(g);
            }
        }

        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }


        // Krabat zeichnen
        mainFrame.krabat.drawKrabat(g);

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab122Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab122, 338, 192);
        }

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab123Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab123, 211, 354);
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

        if (setAnim) {
            setAnim = false;
            nextActionID = 10;
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
                muellermorph.Init(mlynkFeet, 100);  // 68 - 100 - scaleMueller
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
                NeuesBild(90, 77);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}