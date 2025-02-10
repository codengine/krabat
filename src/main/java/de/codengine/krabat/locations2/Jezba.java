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

public class Jezba extends Mainloc2 {
    private GenericImage background, zweitesBild;

    private static final GenericPoint MittelPunkt = new GenericPoint(320, 170);
    private static final GenericPoint UntererPunkt = new GenericPoint(320, 380);

    private boolean showSecondPicture = false;

    private boolean playFanfare = false;

    private static final String H1Text = Start.stringManager.getTranslation("Loc2_Jezba_00000");
    private static final String D1Text = Start.stringManager.getTranslation("Loc2_Jezba_00001");
    private static final String N1Text = Start.stringManager.getTranslation("Loc2_Jezba_00002");

    private static final String H2Text = Start.stringManager.getTranslation("Loc2_Jezba_00003");
    private static final String D2Text = Start.stringManager.getTranslation("Loc2_Jezba_00004");
    private static final String N2Text = Start.stringManager.getTranslation("Loc2_Jezba_00005");

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Jezba(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        // Hier Inventarvektor reduzieren (Karte austauschen)
        mainFrame.inventory.ResetInventory();
        mainFrame.inventory.vInventory.addElement(12); // Feuersteine behaelt K
        mainFrame.inventory.vInventory.addElement(54); // "Dresdener Karte" der Lausitz

        InitLocation(oldLocation);
        mainFrame.Freeze(false);

        nextActionID = 10;
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/most/trip.gif");
        zweitesBild = getPicture("gfx-dd/terassa/terassa.gif");

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
            mainFrame.wave.PlayFile("sfx/fanfara.wav");
        }


        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
            mainFrame.fPlayAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        if (!showSecondPicture) {
            g.drawImage(background, 0, 0, null);
        } else {
            g.drawImage(zweitesBild, 0, 0, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        if (mainFrame.talkCount > 0) {
            mainFrame.talkCount--;
            if (mainFrame.talkCount < 1) {
                mainFrame.Clipset = false;
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
            mainFrame.Clipset = false;
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
            mainFrame.setCursor(mainFrame.Nix);
        }

    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
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
                PersonSagt(H1Text, D1Text, N1Text, 0, 54, 2, 20, MittelPunkt);
                break;

            case 20:
                // Hammertext ausgeben, 2. Teil auf Tesassa-Bild
                showSecondPicture = true;
                mainFrame.Clipset = false;
                BackgroundMusicPlayer.getInstance().playTrack(25, true);
                PersonSagt(H2Text, D2Text, N2Text, 0, 54, 2, 30, UntererPunkt);
                break;

            case 30:
                // Jump nach Dresden in die Kueche
                NeuesBild(120, 94);
                // temporaeres Ende hier
                // NeuesBild (101, 94);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}