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

package de.codengine.locations2;

import de.codengine.Start;
import de.codengine.anims.Farar;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Pinca2 extends Mainloc {
    private GenericImage background, kniha;
    private Farar pfarrer;

    private GenericPoint Pfarar;
    private GenericPoint fararTalk;

    private boolean showBuch = false;

    // Konstante Points
    private static final GenericPoint Pkrabat = new GenericPoint(394, 465);
    private static final GenericPoint Pfar = new GenericPoint(302, 453);
    private static final GenericPoint buchTalk = new GenericPoint(320, 440);

    // Strings
    private static final String H1Text = Start.stringManager.getTranslation("Loc2_Pinca2_00000");
    private static final String D1Text = Start.stringManager.getTranslation("Loc2_Pinca2_00001");
    private static final String N1Text = Start.stringManager.getTranslation("Loc2_Pinca2_00002");

    private static final String H2Text = Start.stringManager.getTranslation("Loc2_Pinca2_00003");
    private static final String D2Text = Start.stringManager.getTranslation("Loc2_Pinca2_00004");
    private static final String N2Text = Start.stringManager.getTranslation("Loc2_Pinca2_00005");

    private static final String H21Text = Start.stringManager.getTranslation("Loc2_Pinca2_00006");
    private static final String D21Text = Start.stringManager.getTranslation("Loc2_Pinca2_00007");
    private static final String N21Text = Start.stringManager.getTranslation("Loc2_Pinca2_00008");

    private static final String H3Text = Start.stringManager.getTranslation("Loc2_Pinca2_00009");
    private static final String D3Text = Start.stringManager.getTranslation("Loc2_Pinca2_00010");
    private static final String N3Text = Start.stringManager.getTranslation("Loc2_Pinca2_00011");

    private static final String H31Text = Start.stringManager.getTranslation("Loc2_Pinca2_00012");
    private static final String D31Text = Start.stringManager.getTranslation("Loc2_Pinca2_00013");
    private static final String N31Text = Start.stringManager.getTranslation("Loc2_Pinca2_00014");

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Pinca2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(8, true);

        mainFrame.krabat.maxx = 260;
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = -127;

        pfarrer = new Farar(mainFrame);
        Pfarar = new GenericPoint();
        Pfarar.x = Pfar.x - (Farar.Breite / 2);
        Pfarar.y = Pfar.y - (Farar.Hoehe);

        fararTalk = new GenericPoint();
        fararTalk.x = Pfar.x;
        fararTalk.y = Pfarar.y - 50;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        mainFrame.krabat.SetKrabatPos(Pkrabat);
        mainFrame.krabat.SetFacing(9);
        nextActionID = 600;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/pinca/pinca.gif");
        kniha = getPicture("gfx/pinca/kniha.gif");

        loadPicture();
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
        if (mainFrame.Clipset == false) {
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
        g.drawImage(background, 0, 0, null);
        if (showBuch == true) {
            g.drawImage(kniha, 0, 0, null);
        }

        // Andere Personen zeichnen, nur wenn das Buch nicht gezeigt wird

        // Pfarrer
        g.setClip(Pfarar.x, Pfarar.y, Farar.Breite, Farar.Hoehe);
        g.drawImage(background, 0, 0, null);
        pfarrer.drawFarar(g, TalkPerson, Pfarar);
        if (showBuch == true) {
            g.drawImage(kniha, 0, 0, null);
        }

        mainFrame.wegGeher.GeheWeg();

        // Krabat zeichnen

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.DoAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }
        } else {
            if ((mainFrame.talkCount > 0) && (TalkPerson != 0)) {
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

        if (showBuch == true) {
            g.drawImage(kniha, 0, 0, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
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
        return;
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        return;
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering == true) ||
                (mainFrame.krabat.isWalking == true)) {
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {

            case 600:
                // Farar redet
                PersonSagt(H1Text, D1Text, N1Text, 0, 37, 2, 610, fararTalk);
                break;

            case 610:
                // Farar redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Pinca2_00015"),
                        Start.stringManager.getTranslation("Loc2_Pinca2_00016"),
                        Start.stringManager.getTranslation("Loc2_Pinca2_00017"),
                        0, 37, 2, 620, fararTalk);
                break;

            case 620:
                // Farar redet
                mainFrame.Clipset = false;
                showBuch = true;
                PersonSagt(H2Text, D2Text, N2Text, 0, 37, 2, 625, buchTalk);
                break;

            case 625:
                // farar redet
                PersonSagt(H21Text, D21Text, N21Text, 0, 37, 2, 630, buchTalk);
                break;

            case 630:
                // Krabat spricht
                mainFrame.Clipset = false;
                showBuch = false;
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Pinca2_00018"),
                        Start.stringManager.getTranslation("Loc2_Pinca2_00019"),
                        Start.stringManager.getTranslation("Loc2_Pinca2_00020"),
                        0, 1, 2, 640);
                break;

            case 640:
                // Farar redet
                PersonSagt(H3Text, D3Text, N3Text, 0, 37, 2, 643, fararTalk);
                break;

            case 643:
                // Farar redet
                PersonSagt(H31Text, D31Text, N31Text, 0, 37, 2, 645, fararTalk);
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
                System.out.println("Falsche Action-ID !");
        }

    }
}