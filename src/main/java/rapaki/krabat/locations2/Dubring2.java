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

package rapaki.krabat.locations2;

import rapaki.krabat.Start;
import rapaki.krabat.anims.Bumm;
import rapaki.krabat.anims.Mlynk2;
import rapaki.krabat.main.*;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.sound.BackgroundMusicPlayer;

public class Dubring2 extends Mainloc2 {
    private GenericImage backl, backr, sky;
    private boolean setScroll = false;
    private int scrollwert;
    private Mlynk2 mueller;

    private boolean muellerVisible = false;

    private Bumm muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // Punkte fuer Krabat und den Mueller deklarieren
    private static final GenericPoint Pkrabat = new GenericPoint(28, 372);
    private static final GenericPoint mlynkFeet = new GenericPoint(104, 374);

    public Dubring2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 7f;
        mainFrame.krabat.defScale = -30;

        mainFrame.krabat.SetKrabatPos(Pkrabat);
        mainFrame.krabat.SetFacing(3);

        mueller = new Mlynk2(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = -30;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(9);

        muellermorph = new Bumm(mainFrame);

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        mainFrame.Freeze(false);

        nextActionID = 10;
        TalkPause = 10;
    }

    // Bilder vorbereiten
    public void InitImages() {
        backl = getPicture("gfx/dubring/dubr-l3.gif");
        backr = getPicture("gfx/dubring/dubr-r3.gif");
        sky = getPicture("gfx/dubring/dubrsky.gif");

        loadPicture();
    }

    public void cleanup() {
        backl = null;
        backr = null;
        sky = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
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
            mainFrame.isAnim = true;
            mainFrame.fPlayAnim = true;
        }

        // Hintergrund zeichnen
        g.drawImage(sky, (mainFrame.scrollx / 2), 0, null);
        g.drawImage(backl, 0, 0, null);
        g.drawImage(backr, 640, 0, null);

        // Parallaxer ausfuehren
        if (mainFrame.isScrolling == true) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 241);
            g.drawImage(sky, (mainFrame.scrollx / 2), 0, null);
            g.drawImage(backl, 0, 0, null);
            g.drawImage(backr, 640, 0, null);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing == true) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(backl, 0, 0, null);
            g.drawImage(backr, 640, 0, null);
        }

        if (muellerVisible == true) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.MlynkRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(backl, 0, 0, null);

            // Redet er etwa gerade ??
            if ((TalkPerson == 36) && (mainFrame.talkCount > 0)) {
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
        if (ismuellermorphing == true) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }

        // Ab hier muss Cliprect wieder gerettet werden
        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
        }

        if ((mainFrame.talkCount < 1) && (TalkPause > 0)) {
            TalkPause--;
        }

        if (mainFrame.talkCount > 0) {
            mainFrame.talkCount--;
            if (mainFrame.talkCount < 1) {
                mainFrame.Clipset = false;
                outputText = "";
            }
        }


        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (mainFrame.talkCount < 1) && (TalkPause < 1)) {
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

    // dieses Event nicht beachten
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        return;
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        switch (nextActionID) {
            case 10:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 120);  // 68 - 100 - scaleMueller
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
                mainFrame.Clipset = false;
                MuellerMecker(mueller.evalMlynkTalkPoint());
                TalkPerson = 36;
                TalkPause = 5;
                nextActionID = 20;
                break;

            case 20:
                // Gehe zu Muehle zurueck
                NeuesBild(90, 72);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }
}