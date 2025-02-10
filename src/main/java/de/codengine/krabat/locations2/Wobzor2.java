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
import de.codengine.krabat.anims.Bumm;
import de.codengine.krabat.anims.Mlynk2;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Wobzor2 extends Mainloc2 {
    private GenericImage background, horiz3, horiz4;
    private final GenericImage[] Wasser;
    private boolean switchanim = false;
    private int wassercount = 1;
    private boolean forward = true;
    private Mlynk2 mueller;

    private boolean muellerVisible = false;

    private Bumm muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // Vordergrund - Rects
    private static final Borderrect horiz3Rect = new Borderrect(179, 175, 270, 248);
    private static final Borderrect horiz4Rect = new Borderrect(528, 177, 639, 219);

    // Konstanten - Points
    private static final GenericPoint Pkrabat = new GenericPoint(153, 231);
    private static final GenericPoint mlynkFeet = new GenericPoint(180, 230);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wobzor2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 4f;
        mainFrame.krabat.defScale = 80;

        mainFrame.krabat.SetKrabatPos(Pkrabat);
        mainFrame.krabat.SetFacing(3);

        Wasser = new GenericImage[9];
        mueller = new Mlynk2(mainFrame);

        muellermorph = new Bumm(mainFrame);

        mueller.maxx = 0;
        mueller.zoomf = 4f;
        mueller.defScale = 80;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(9);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);

        nextActionID = 10;
        TalkPause = 10;
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/horiz/horiz2.gif");
        horiz3 = getPicture("gfx/horiz/horiz3.gif");
        horiz4 = getPicture("gfx/horiz/horiz4.gif");

        Wasser[1] = getPicture("gfx/horiz/hww5.gif");
        Wasser[2] = getPicture("gfx/horiz/hww4.gif");
        Wasser[3] = getPicture("gfx/horiz/hww3.gif");
        Wasser[4] = getPicture("gfx/horiz/hww2.gif");
        Wasser[5] = getPicture("gfx/horiz/hw3.gif");
        Wasser[6] = getPicture("gfx/horiz/hw4.gif");
        Wasser[7] = getPicture("gfx/horiz/hw5.gif");
        Wasser[8] = getPicture("gfx/horiz/hw6.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        horiz3 = null;
        horiz4 = null;

        Wasser[1] = null;
        Wasser[2] = null;
        Wasser[3] = null;
        Wasser[4] = null;
        Wasser[5] = null;
        Wasser[6] = null;
        Wasser[7] = null;
        Wasser[8] = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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
        g.drawImage(background, 0, 0, null);

        if (mainFrame.isAnim) {
            switchanim = !(switchanim);
            if (switchanim) {
                if (forward) {
                    wassercount++;
                    if (wassercount == 9) {
                        wassercount = 7;
                        forward = false;
                    }
                } else {
                    wassercount--;
                    if (wassercount == 0) {
                        wassercount = 2;
                        forward = true;
                    }
                }
            }
            g.setClip(303, 356, 338, 125);
            g.drawImage(Wasser[wassercount], 303, 356, null);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0, null);
        }

        if (muellerVisible) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.MlynkRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0, null);

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
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (horiz3Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(horiz3, 197, 186, null);
        }

        // hinterm horiz4 (nur Clipping - Region wird neugezeichnet)
        if (horiz4Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(horiz4, 543, 186, null);
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
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 25);  // 68 - 100 - scaleMueller
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
                NeuesBild(90, 88);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}