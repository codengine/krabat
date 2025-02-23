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
import de.codengine.krabat.anims.Farar;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Pinca2 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Pinca2.class);
    private GenericImage background;
    private GenericImage kniha;
    private Farar pfarrer;

    private final GenericPoint Pfarar;
    private final GenericPoint fararTalk;

    private boolean showBuch = false;

    // Konstante Points
    private static final GenericPoint Pkrabat = new GenericPoint(394, 465);
    private static final GenericPoint Pfar = new GenericPoint(302, 453);
    private static final GenericPoint buchTalk = new GenericPoint(320, 440);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Pinca2(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(8, true);

        mainFrame.krabat.maxx = 260;
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = -127;

        pfarrer = new Farar(mainFrame);
        Pfarar = new GenericPoint();
        Pfarar.x = Pfar.x - Farar.Breite / 2;
        Pfarar.y = Pfar.y - Farar.Hoehe;

        fararTalk = new GenericPoint();
        fararTalk.x = Pfar.x;
        fararTalk.y = Pfarar.y - 50;

        InitLocation();
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        InitImages();
        mainFrame.krabat.setPos(Pkrabat);
        mainFrame.krabat.SetFacing(9);
        nextActionID = 600;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/pinca/pinca.png");
        kniha = getPicture("gfx/pinca/kniha.png");

    }

    @Override
    public void cleanup() {
        background = null;
        kniha = null;

        pfarrer.cleanup();
        pfarrer = null;
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
        if (showBuch) {
            g.drawImage(kniha, 0, 0);
        }

        // Andere Personen zeichnen, nur wenn das Buch nicht gezeigt wird

        // Pfarrer
        g.setClip(Pfarar.x, Pfarar.y, Farar.Breite, Farar.Hoehe);
        g.drawImage(background, 0, 0);
        pfarrer.drawFarar(g, TalkPerson, Pfarar);
        if (showBuch) {
            g.drawImage(kniha, 0, 0);
        }

        mainFrame.pathWalker.GeheWeg();

        // Krabat zeichnen

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.mousePoint);
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
                        // steht Krabat nur da
                        mainFrame.krabat.drawKrabat(g);
                        break;
                }
            }
            // Rumstehen oder Laufen
            else {
                mainFrame.krabat.drawKrabat(g);
            }
        }

        if (showBuch) {
            g.drawImage(kniha, 0, 0);
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

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
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
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {

            case 600:
                // Farar redet
                PersonSagt("Pinca2_3", 0, 37, 2, 610, fararTalk);
                break;

            case 610:
                // Farar redet
                PersonSagt("Pinca2_1", 0, 37, 2, 620, fararTalk);
                break;

            case 620:
                // Farar redet
                mainFrame.isClipSet = false;
                showBuch = true;
                PersonSagt("Pinca2_4", 0, 37, 2, 625, buchTalk);
                break;

            case 625:
                // farar redet
                PersonSagt("Pinca2_5", 0, 37, 2, 630, buchTalk);
                break;

            case 630:
                // Krabat spricht
                mainFrame.isClipSet = false;
                showBuch = false;
                KrabatSagt("Pinca2_2", 0, 1, 2, 640);
                break;

            case 640:
                // Farar redet
                PersonSagt("Pinca2_6", 0, 37, 2, 643, fararTalk);
                break;

            case 643:
                // Farar redet
                PersonSagt("Pinca2_7", 0, 37, 2, 645, fararTalk);
                break;

            case 645:
                // 1 Repaint abwarten dass beide in Ruhestellung
                nextActionID = 650;
                break;

            case 650:
                // Ab nach Mertens
                NeuesBild(79, 81);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}