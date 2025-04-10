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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Doma4 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Doma4.class);
    private GenericImage background1;
    private GenericImage background2;
    private GenericImage back;
    private GenericImage foreground;
    private GenericImage foreground2;

    private final GenericImage[] Rauchanim;
    private final GenericImage[] LeuteRechts;
    private final GenericImage[] LeuteMitte;
    private final GenericImage[] LeuteLinks;

    private int LeutelinksCount = 1;
    private int LeuterechtsCount = 1;

    private int Verhinderleutelinks;
    private int Verhinderleuterechts;

    private static final int MAX_VERHINDERLEUTELINKS = 2;
    private static final int MAX_VERHINDERLEUTERECHTS = 2;

    private int Rauchcount = 1;

    private boolean switchanim = true;
    private boolean setScroll = false;
    private final int scrollwert;

    // Konstante Points
    private static final GenericPoint mittelPunkt = new GenericPoint(960, 250);

    // Variablen fuer Fenster links (d.h. Leute Mitte)
    private int leuteMitteActionID = 0;
    private int leuteMitteCounter = 0;

    public Doma4(Start caller) {
        super(caller);

        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(24, false);

        Rauchanim = new GenericImage[13];
        LeuteRechts = new GenericImage[6];
        LeuteLinks = new GenericImage[7];
        LeuteMitte = new GenericImage[3];

        InitImages();

        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        scrollwert = 640;
        setScroll = true;

        nextActionID = 10;

        Verhinderleutelinks = MAX_VERHINDERLEUTELINKS;
        Verhinderleuterechts = MAX_VERHINDERLEUTERECHTS;

        mainFrame.freeze(false);
    }

    // Bilder vorbereiten
    private void InitImages() {
        background1 = getPicture("gfx/doma/dom-l.png");
        background2 = getPicture("gfx/doma/dom-r.png");
        back = getPicture("gfx/doma/domsky.png");
        foreground = getPicture("gfx/doma/dwokna.png");
        foreground2 = getPicture("gfx/doma/dwokna2.png");

        Rauchanim[1] = getPicture("gfx/doma/ra1.png");
        Rauchanim[2] = getPicture("gfx/doma/ra2.png");
        Rauchanim[3] = getPicture("gfx/doma/ra3.png");
        Rauchanim[4] = getPicture("gfx/doma/ra4.png");
        Rauchanim[5] = getPicture("gfx/doma/ra5.png");
        Rauchanim[6] = getPicture("gfx/doma/ra6.png");
        Rauchanim[7] = getPicture("gfx/doma/ra7.png");
        Rauchanim[8] = getPicture("gfx/doma/ra8.png");
        Rauchanim[9] = getPicture("gfx/doma/ra9.png");
        Rauchanim[10] = getPicture("gfx/doma/ra10.png");
        Rauchanim[11] = getPicture("gfx/doma/ra11.png");
        Rauchanim[12] = getPicture("gfx/doma/ra12.png");

        LeuteLinks[1] = getPicture("gfx/doma/ll1.png");
        LeuteLinks[2] = getPicture("gfx/doma/ll2.png");
        LeuteLinks[3] = getPicture("gfx/doma/ll3.png");
        LeuteLinks[4] = getPicture("gfx/doma/ll4.png");
        LeuteLinks[5] = getPicture("gfx/doma/ll6.png");
        LeuteLinks[6] = getPicture("gfx/doma/ll7.png");

        LeuteRechts[1] = getPicture("gfx/doma/lr1.png");
        LeuteRechts[2] = getPicture("gfx/doma/lr2.png");
        LeuteRechts[3] = getPicture("gfx/doma/lr3.png");
        LeuteRechts[4] = getPicture("gfx/doma/lr4.png");
        LeuteRechts[5] = getPicture("gfx/doma/lr5.png");

        LeuteMitte[1] = getPicture("gfx/doma/lm1.png");
        LeuteMitte[2] = getPicture("gfx/doma/lm2.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isAnimRunning = true;
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
        g.drawImage(back, mainFrame.scrollX / 10, 0);
        g.drawImage(background1, 0, 0);
        g.drawImage(background2, 640, 0);

        // Ab hier ist Retten des ClipRect sinnlos!!!
        // Rauch animieren
        switchanim = !switchanim;
        if (switchanim) {
            Rauchcount++;
            if (Rauchcount == 13) {
                Rauchcount = 1;
            }
        }
        g.setClip(985, 15, 30, 120);
        g.drawImage(back, mainFrame.scrollX / 10, 0);
        g.drawImage(Rauchanim[Rauchcount], 985, 15);
        g.drawImage(background2, 640, 0);

        // Leute animieren
        g.setClip(923, 271, 127, 74);
        g.drawImage(back, mainFrame.scrollX / 10, 0);
        g.drawImage(background1, 0, 0);
        g.drawImage(background2, 640, 0);
        g.drawImage(foreground2, 923, 271);

        if (--Verhinderleutelinks < 1) {
            Verhinderleutelinks = MAX_VERHINDERLEUTELINKS;
            LeutelinksCount = (int) (Math.random() * 5.9);
            LeutelinksCount++;
        }

        g.drawImage(LeuteLinks[LeutelinksCount], 997, 285);


        if (--Verhinderleuterechts < 1) {
            Verhinderleuterechts = MAX_VERHINDERLEUTERECHTS;
            LeuterechtsCount = (int) (Math.random() * 4.9);
            LeuterechtsCount++;
        }

        g.drawImage(LeuteRechts[LeuterechtsCount], 1007, 299);

        // wenn derzeitge Animation vor dem linken Fenster abgelaufen ist (40 Frames)
        // -> per Zufall eine neue Animation waehlen
        leuteMitteCounter += 1; // Geschwindigkeit halbiert
        if (leuteMitteCounter > 40) {
            leuteMitteActionID = (int) Math.round(Math.random() * 4.9);
            leuteMitteCounter = 0;
        }

        int yOffset = (int) Math.round(Math.random() * 2.9);

        // was passiert zur Zeit vor dem linken Fenster ?
        // (bei ID 2 - 4 ist niemand vor dem Fenster zu sehen)
        switch (leuteMitteActionID) {
            // es laeft jemand von links nach rechts durchs Fenster
            case 0:
                g.drawImage(LeuteMitte[2], 904 + leuteMitteCounter, 296 + yOffset);
                break;

            // es laeft jemand von rechts nach links durchs Fenster
            case 1:
                g.drawImage(LeuteMitte[1], 944 /*988*/ - leuteMitteCounter, 296 + yOffset);
                break;
        }

        // Vordergrund draufzeichnen
        g.drawImage(foreground, 923, 271);

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        // Textausgabe, falls noetig
        if (!Objects.equals(outputText, "")) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 964);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        if (TalkPause > 0 && mainFrame.talkCount == 0) {
            TalkPause--;
        }

        // Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!

        if (nextActionID != 0 && TalkPause == 0 && mainFrame.talkCount == 0) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Auszugebenden Text abbrechen
        outputText = "";
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
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

            case 10:
                // Text Erzaehler
                PersonSagt("Doma4_1", 0, 54, 2, 15, mittelPunkt);
                break;

            case 15:
                // Text Erzaehler
                PersonSagt("Doma4_2", 0, 54, 2, 20, mittelPunkt);
                break;

            case 20:
                // Skip zu Wotrow
                NeuesBild(200, 203);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}