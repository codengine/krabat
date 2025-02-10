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

package de.codengine.locations4;

import de.codengine.Start;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Doma4 extends Mainloc {
    private GenericImage background1, background2, back, foreground, foreground2;

    private GenericImage[] Rauchanim;
    private GenericImage[] LeuteRechts;
    private GenericImage[] LeuteMitte;
    private GenericImage[] LeuteLinks;

    private int LeutelinksCount = 1;
    private int LeuterechtsCount = 1;

    private int Verhinderleutelinks;
    private int Verhinderleuterechts;

    private static final int MAX_VERHINDERLEUTELINKS = 2;
    private static final int MAX_VERHINDERLEUTERECHTS = 2;

    private int Rauchcount = 1;

    private boolean switchanim = true;
    private boolean setScroll = false;
    private int scrollwert;

    // Konstante Points
    private static final GenericPoint mittelPunkt = new GenericPoint(960, 250);

    // Variablen fuer Fenster links (d.h. Leute Mitte)
    private int leuteMitteActionID = 0;
    private int leuteMitteCounter = 0;

    public Doma4(Start caller, int oldLocation) {
        super(caller);

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(24, false);

        Rauchanim = new GenericImage[13];
        LeuteRechts = new GenericImage[6];
        LeuteLinks = new GenericImage[7];
        LeuteMitte = new GenericImage[3];

        InitImages();

        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        scrollwert = 640;
        setScroll = true;

        InitLocation();

        nextActionID = 10;

        Verhinderleutelinks = MAX_VERHINDERLEUTELINKS;
        Verhinderleuterechts = MAX_VERHINDERLEUTERECHTS;

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
    }

    // Bilder vorbereiten
    private void InitImages() {
        background1 = getPicture("gfx/doma/dom-l.gif");
        background2 = getPicture("gfx/doma/dom-r.gif");
        back = getPicture("gfx/doma/domsky.gif");
        foreground = getPicture("gfx/doma/dwokna.gif");
        foreground2 = getPicture("gfx/doma/dwokna2.gif");

        Rauchanim[1] = getPicture("gfx/doma/ra1.gif");
        Rauchanim[2] = getPicture("gfx/doma/ra2.gif");
        Rauchanim[3] = getPicture("gfx/doma/ra3.gif");
        Rauchanim[4] = getPicture("gfx/doma/ra4.gif");
        Rauchanim[5] = getPicture("gfx/doma/ra5.gif");
        Rauchanim[6] = getPicture("gfx/doma/ra6.gif");
        Rauchanim[7] = getPicture("gfx/doma/ra7.gif");
        Rauchanim[8] = getPicture("gfx/doma/ra8.gif");
        Rauchanim[9] = getPicture("gfx/doma/ra9.gif");
        Rauchanim[10] = getPicture("gfx/doma/ra10.gif");
        Rauchanim[11] = getPicture("gfx/doma/ra11.gif");
        Rauchanim[12] = getPicture("gfx/doma/ra12.gif");

        LeuteLinks[1] = getPicture("gfx/doma/ll1.gif");
        LeuteLinks[2] = getPicture("gfx/doma/ll2.gif");
        LeuteLinks[3] = getPicture("gfx/doma/ll3.gif");
        LeuteLinks[4] = getPicture("gfx/doma/ll4.gif");
        LeuteLinks[5] = getPicture("gfx/doma/ll6.gif");
        LeuteLinks[6] = getPicture("gfx/doma/ll7.gif");

        LeuteRechts[1] = getPicture("gfx/doma/lr1.gif");
        LeuteRechts[2] = getPicture("gfx/doma/lr2.gif");
        LeuteRechts[3] = getPicture("gfx/doma/lr3.gif");
        LeuteRechts[4] = getPicture("gfx/doma/lr4.gif");
        LeuteRechts[5] = getPicture("gfx/doma/lr5.gif");

        LeuteMitte[1] = getPicture("gfx/doma/lm1.gif");
        LeuteMitte[2] = getPicture("gfx/doma/lm2.gif");

        loadPicture();
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation(GenericDrawingContext g) {

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (mainFrame.Clipset == false) {
            mainFrame.Clipset = true;
            if (setScroll == true) {
                setScroll = false;
                mainFrame.scrollx = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.fPlayAnim = true;
            mainFrame.isAnim = true;
        }

        // Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
        g.drawImage(back, (mainFrame.scrollx / 10), 0, null);
        g.drawImage(background1, 0, 0, null);
        g.drawImage(background2, 640, 0, null);

        // Ab hier ist Retten des ClipRect sinnlos!!!
        // Rauch animieren
        switchanim = !(switchanim);
        if (switchanim == true) {
            Rauchcount++;
            if (Rauchcount == 13) {
                Rauchcount = 1;
            }
        }
        g.setClip(985, 15, 30, 120);
        g.drawImage(back, (mainFrame.scrollx / 10), 0, null);
        g.drawImage(Rauchanim[Rauchcount], 985, 15, null);
        g.drawImage(background2, 640, 0, null);

        // Leute animieren
        g.setClip(923, 271, 127, 74);
        g.drawImage(back, (mainFrame.scrollx / 10), 0, null);
        g.drawImage(background1, 0, 0, null);
        g.drawImage(background2, 640, 0, null);
        g.drawImage(foreground2, 923, 271, null);

        if ((--Verhinderleutelinks) < 1) {
            Verhinderleutelinks = MAX_VERHINDERLEUTELINKS;
            LeutelinksCount = (int) (Math.random() * 5.9);
            LeutelinksCount++;
        }

        g.drawImage(LeuteLinks[LeutelinksCount], 997, 285, null);


        if ((--Verhinderleuterechts) < 1) {
            Verhinderleuterechts = MAX_VERHINDERLEUTERECHTS;
            LeuterechtsCount = (int) (Math.random() * 4.9);
            LeuterechtsCount++;
        }

        g.drawImage(LeuteRechts[LeuterechtsCount], 1007, 299, null);

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
                g.drawImage(LeuteMitte[2], 904 + leuteMitteCounter, 296 + yOffset, null);
                break;

            // es laeft jemand von rechts nach links durchs Fenster
            case 1:
                g.drawImage(LeuteMitte[1], 944 /*988*/ - leuteMitteCounter, 296 + yOffset, null);
                break;
        }

        // Vordergrund draufzeichnen
        g.drawImage(foreground, 923, 271, null);

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        // Textausgabe, falls noetig
        if (outputText != "") {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 964);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
        }

        if ((TalkPause > 0) && (mainFrame.talkCount == 0)) {
            TalkPause--;
        }

        // Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!

        if ((nextActionID != 0) && (TalkPause == 0) && (mainFrame.talkCount == 0)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        // Auszugebenden Text abbrechen
        outputText = "";
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        return;
    }


    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.Nix);
        }
        return;
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {

        // Was soll Krabat machen ?
        switch (nextActionID) {

            case 10:
                // Text Erzaehler
                PersonSagt(Start.stringManager.getTranslation("Loc4_Doma4_00000"),
                        Start.stringManager.getTranslation("Loc4_Doma4_00001"),
                        Start.stringManager.getTranslation("Loc4_Doma4_00002"),
                        0, 54, 2, 15, mittelPunkt);
                break;

            case 15:
                // Text Erzaehler
                PersonSagt(Start.stringManager.getTranslation("Loc4_Doma4_00003"),
                        Start.stringManager.getTranslation("Loc4_Doma4_00004"),
                        Start.stringManager.getTranslation("Loc4_Doma4_00005"),
                        0, 54, 2, 20, mittelPunkt);
                break;

            case 20:
                // Skip zu Wotrow
                NeuesBild(200, 203);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }
}