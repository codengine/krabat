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

public class Les2 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Les2.class);
    private GenericImage background;
    private GenericImage strauch;
    private Miller mueller;

    private boolean muellerVisible = false;

    private Boom muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // Vordergrund - Rects
    private static final BorderRect strauchRect = new BorderRect(0, 380, 255, 419);

    // Konstante Points
    private static final GenericPoint Pkrabat = new GenericPoint(50, 361);
    private static final GenericPoint mlynkFeet = new GenericPoint(264, 339);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Les2(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxX = 428;
        mainFrame.krabat.zoomFactor = 2.88f;
        mainFrame.krabat.defaultScale = -20;

        mueller = new Miller(mainFrame);

        muellermorph = new Boom(mainFrame);

        mueller.maxX = 300;
        mueller.zoomFactor = 4f;
        mueller.defaultScale = 10;

        mueller.setPos(mlynkFeet);
        mueller.setFacing(9);

        initLocation();

        mainFrame.freeze(false);

        nextActionID = 10;
        talkPause = 10;
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation() {
        initImages();

        mainFrame.krabat.setPos(Pkrabat);
        mainFrame.krabat.setFacing(3);
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/les1/wald.png");
        strauch = getPicture("gfx/les1/grass2.png");

    }

    @Override
    public void cleanup() {
        background = null;
        strauch = null;

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
            cursorShape = 200;
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
            BorderRect temp = mueller.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);

            // Redet er etwa gerade ??
            if (talkPerson == 36 && mainFrame.talkCount > 0) {
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

        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Krabat hinterm Gras ??
        if (strauchRect.isPointInRect(pKrTemp)) {
            g.drawImage(strauch, 0, 381);
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

        if (mainFrame.talkCount < 1 && talkPause > 0) {
            talkPause--;
        }

        if (mainFrame.talkCount > 0) {
            mainFrame.talkCount--;
            if (mainFrame.talkCount < 1) {
                mainFrame.isClipSet = false;
                outputText = "";
            }
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && mainFrame.talkCount < 1 && talkPause < 1) {
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

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {
        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 10:
                // Morphing beginnt
                muellermorph.init(mlynkFeet, 80);  // 68 - 100 - scaleMueller
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
                millerComplain(mueller.evalMlynkTalkPoint());
                talkPerson = 36;
                talkPause = 5;
                nextActionID = 20;
                break;

            case 20:
                // Gehe zu Muehle zurueck
                createNewLocation(90, 78);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}