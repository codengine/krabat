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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Jezba extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Jezba.class);
    private GenericImage background;
    private GenericImage zweitesBild;

    private static final GenericPoint MittelPunkt = new GenericPoint(320, 170);
    private static final GenericPoint UntererPunkt = new GenericPoint(320, 380);

    private boolean showSecondPicture = false;

    private boolean playFanfare = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jezba(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        // Hier Inventarvektor reduzieren (Karte austauschen)
        mainFrame.inventory.ResetInventory();
        mainFrame.inventory.vInventory.addElement(12); // Feuersteine behaelt K
        mainFrame.inventory.vInventory.addElement(54); // "Dresdener Karte" der Lausitz

        InitLocation();
        mainFrame.freeze(false);

        nextActionID = 10;
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/most/trip.png");
        zweitesBild = getPicture("gfx-dd/terassa/terassa.png");

    }

    @Override
    public void cleanup() {
        background = null;
        zweitesBild = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Fanfare 1x abspielen
        if (!playFanfare) {
            playFanfare = true;
            mainFrame.soundPlayer.PlayFile("sfx/fanfara.wav");
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
            mainFrame.isAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        if (!showSecondPicture) {
            g.drawImage(background, 0, 0);
        } else {
            g.drawImage(zweitesBild, 0, 0);
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

        if (mainFrame.talkCount > 0) {
            mainFrame.talkCount--;
            if (mainFrame.talkCount < 1) {
                mainFrame.isClipSet = false;
                outputText = "";
            }
        }

        if (mainFrame.talkCount < 1 && TalkPause > 0) {
            TalkPause--;
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
                // Hammertext ausgeben
                PersonSagt("Jezba_1", 0, 54, 2, 20, MittelPunkt);
                break;

            case 20:
                // Hammertext ausgeben, 2. Teil auf Tesassa-Bild
                showSecondPicture = true;
                mainFrame.isClipSet = false;
                BackgroundMusicPlayer.getInstance().playTrack(25, true);
                PersonSagt("Jezba_2", 0, 54, 2, 30, UntererPunkt);
                break;

            case 30:
                // Jump nach Dresden in die Kueche
                NeuesBild(120, 94);
                // temporaeres Ende hier
                // NeuesBild (101, 94);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}