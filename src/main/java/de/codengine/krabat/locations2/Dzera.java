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
import de.codengine.krabat.anims.Cat;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Dzera extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Dzera.class);
    private GenericImage background;

    private Miller mueller;
    private Cat katze;

    private boolean setAnim = false;

    // Points
    private final GenericPoint kockaTalk = new GenericPoint(0, 0);
    private final GenericPoint Pkocka = new GenericPoint(0, 0);

    // Konstante Points
    private static final GenericPoint mlynkFeet = new GenericPoint(195, 363);
    private static final GenericPoint kockaFeet = new GenericPoint(363, 363);
    // private static final GenericPoint krabatTalk = new GenericPoint (320, 100);
    // private static final GenericPoint erzTalk    = new GenericPoint (200, 320);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Dzera(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mueller = new Miller(mainFrame);
        katze = new Cat(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = -20;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(3);

        Pkocka.x = kockaFeet.x - Cat.Breite / 2;
        Pkocka.y = kockaFeet.y - Cat.Hoehe;
        kockaTalk.x = kockaFeet.x;
        kockaTalk.y = Pkocka.y - 50;

        InitLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mlyn/guck.png");
    }

    @Override
    public void cleanup() {
        background = null;

        mueller.cleanup();
        mueller = null;
        katze.cleanup();
        katze = null;
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
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

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

        // Katze zeichnen
        g.setClip(Pkocka.x, Pkocka.y, Cat.Breite, Cat.Hoehe);
        g.drawImage(background, 0, 0);
        katze.drawKocka(g, TalkPerson, Pkocka);

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
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

        if (!setAnim) {
            setAnim = true;
            nextActionID = 100;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
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
            TalkPerson = 0;
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
            case 100:
                // Mueller redet
                PersonSagt("Dzera_1", 0, 36, 0, 110, mueller.evalMlynkTalkPoint());
                break;

            case 110:
                // Katze redet
                PersonSagt("Dzera_2", 0, 59, 2, 120, kockaTalk);
                break;

            case 120:
                // Mueller redet
                PersonSagt("Dzera_3", 0, 36, 2, 130, mueller.evalMlynkTalkPoint());
                break;

            case 130:
                // Katze redet
                PersonSagt("Dzera_4", 0, 59, 2, 135, kockaTalk);
                break;

            case 135:
                // Katze redet
                PersonSagt("Dzera_5", 0, 59, 2, 140, kockaTalk);
                break;

            case 140:
                // Mueller redet
                PersonSagt("Dzera_6", 0, 36, 2, 150, mueller.evalMlynkTalkPoint());
                break;

            case 150:
                // Krabat redet
                KrabatSagt("Dzera_7", 0, 3, 2, 160);
                break;

            case 160:
                // Katze redet
                PersonSagt("Dzera_8", 0, 59, 2, 170, kockaTalk);
                break;

            case 170:
                // Mueller redet
                PersonSagt("Dzera_9", 0, 36, 2, 180, mueller.evalMlynkTalkPoint());
                break;

            case 180:
                // Katze redet
                PersonSagt("Dzera_10", 0, 59, 2, 190, kockaTalk);
                break;

            case 190:
                // Mueller redet
                PersonSagt("Dzera_11", 0, 36, 2, 200, mueller.evalMlynkTalkPoint());
                break;

            case 200:
                // Katze redet
                PersonSagt("Dzera_12", 0, 59, 2, 205, kockaTalk);
                break;

            case 205:
                // Katze redet
                PersonSagt("Dzera_13", 0, 59, 2, 210, kockaTalk);
                break;

            case 210:
                // Mueller redet
                PersonSagt("Dzera_14", 0, 36, 2, 220, mueller.evalMlynkTalkPoint());
                break;

            case 220:
                // Mueller redet
                PersonSagt("Dzera_15", 0, 36, 2, 250, mueller.evalMlynkTalkPoint());
                break;

            case 250:
                // Skip zur Muehle Innen
                NeuesBild(91, 28);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}