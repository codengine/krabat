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
import de.codengine.krabat.anims.Mlynk2;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Mlyn2 extends Mainloc {
    private GenericImage background;

    private final GenericImage[] Rad;
    private int Radcount = 1;
    private int Verhinderrad;
    private static final int MAX_VERHINDERRAD = 3;

    private Mlynk2 mueller;
    private boolean showPersonen = true;
    private boolean showKrabat = true;

    private boolean muellerSprichtMitStock = false;

    // Texte
    private static final String H1Text = Start.stringManager.getTranslation("Loc2_Mlyn2_00000");
    private static final String D1Text = Start.stringManager.getTranslation("Loc2_Mlyn2_00001");
    private static final String N1Text = Start.stringManager.getTranslation("Loc2_Mlyn2_00002");

    private static final String HText = Start.stringManager.getTranslation("Loc2_Mlyn2_00003");
    private static final String DText = Start.stringManager.getTranslation("Loc2_Mlyn2_00004");
    private static final String NText = Start.stringManager.getTranslation("Loc2_Mlyn2_00005");

    private static final String[] HAMecker = {Start.stringManager.getTranslation("Loc2_Mlyn2_00006"), Start.stringManager.getTranslation("Loc2_Mlyn2_00007")};
    private static final String[] DAMecker = {Start.stringManager.getTranslation("Loc2_Mlyn2_00008"), Start.stringManager.getTranslation("Loc2_Mlyn2_00009")};
    private static final String[] NAMecker = {Start.stringManager.getTranslation("Loc2_Mlyn2_00010"), Start.stringManager.getTranslation("Loc2_Mlyn2_00011")};
    private static final int KONSTANTE = 1;

    // Konstanten - Rects
    // private static final borderrect rechterAusgang = new borderrect (560, 402, 639, 479);

    // Konstante Points
    // private static final GenericPoint Pright    = new GenericPoint (639, 467);
    private static final GenericPoint Pkrabat = new GenericPoint(243, 452);
    private static final GenericPoint mlynkFeet = new GenericPoint(161, 440);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mlyn2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 313;
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = 0;

        Rad = new GenericImage[21];
        mueller = new Mlynk2(mainFrame);

        mueller.maxx = 440;
        mueller.zoomf = 1f;
        mueller.defScale = 0;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(3);

        Verhinderrad = MAX_VERHINDERRAD;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        mainFrame.krabat.SetKrabatPos(Pkrabat);
        mainFrame.krabat.SetFacing(9);

        // Einsprung, wenn von Inmlyn kommend (Anim: darfst nach Hause gehen...)
        if (oldLocation == 91) // Inmlyn
        {
            // von Inmlyn aus
            BackgroundMusicPlayer.getInstance().stop();
            nextActionID = 10;
        } else {
            if (oldLocation == 92) // Swoboda
            {
                // von Swoboda aus
                nextActionID = 1000;
                showKrabat = false;
            } else {
                if (oldLocation == 71) // Doma2
                {
                    // Uebergang Doma2 -> Swoboda
                    BackgroundMusicPlayer.getInstance().playTrack(12, true);
                    nextActionID = 2000;
                    showKrabat = false;
                    showPersonen = false;
                } else {
                    // Anmecker, da er nicht den kuerzesten Weg gewaehlt hat
                    BackgroundMusicPlayer.getInstance().stop();
                    nextActionID = 500;
                }
            }
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mlyn/mlyn2.gif");
        Rad[1] = getPicture("gfx/mlyn/r1.gif");
        Rad[2] = getPicture("gfx/mlyn/r2.gif");
        Rad[3] = getPicture("gfx/mlyn/r3.gif");
        Rad[4] = getPicture("gfx/mlyn/r4.gif");
        Rad[5] = getPicture("gfx/mlyn/r5.gif");
        Rad[6] = getPicture("gfx/mlyn/r6.gif");
        Rad[7] = getPicture("gfx/mlyn/r7.gif");
        Rad[8] = getPicture("gfx/mlyn/r8.gif");
        Rad[9] = getPicture("gfx/mlyn/r9.gif");
        Rad[10] = getPicture("gfx/mlyn/r10.gif");
        Rad[11] = getPicture("gfx/mlyn/r11.gif");
        Rad[12] = getPicture("gfx/mlyn/r12.gif");
        Rad[13] = getPicture("gfx/mlyn/r13.gif");
        Rad[14] = getPicture("gfx/mlyn/r14.gif");
        Rad[15] = getPicture("gfx/mlyn/r15.gif");
        Rad[16] = getPicture("gfx/mlyn/r16.gif");
        Rad[17] = getPicture("gfx/mlyn/r17.gif");
        Rad[18] = getPicture("gfx/mlyn/r18.gif");
        Rad[19] = getPicture("gfx/mlyn/r19.gif");
        Rad[20] = getPicture("gfx/mlyn/r20.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        Rad[1] = null;
        Rad[2] = null;
        Rad[3] = null;
        Rad[4] = null;
        Rad[5] = null;
        Rad[6] = null;
        Rad[7] = null;
        Rad[8] = null;
        Rad[9] = null;
        Rad[10] = null;
        Rad[11] = null;
        Rad[12] = null;
        Rad[13] = null;
        Rad[14] = null;
        Rad[15] = null;
        Rad[16] = null;
        Rad[17] = null;
        Rad[18] = null;
        Rad[19] = null;
        Rad[20] = null;

        mueller.cleanup();
        mueller = null;
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

        // Mueller Hintergrund loeschen
        if (showPersonen) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp;
            if (!muellerSprichtMitStock) {
                temp = mueller.MlynkRect();
            } else {
                temp = mueller.MlynkRectMitStockAndersrum();
            }

            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0, null);
        }

        // Anim zeichnen, da stets im Hintergrund
        if (mainFrame.isAnim) {
            if ((--Verhinderrad) < 1) {
                Verhinderrad = MAX_VERHINDERRAD;
                Radcount++;
                if (Radcount == 21) {
                    Radcount = 1;
                }
            }
            g.setClip(120, 260, 135, 145);
            g.drawImage(Rad[Radcount], 120, 260, null);

            evalSound();
        }

        // Mueller zeichnen
        if (showPersonen) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp;
            if (!muellerSprichtMitStock) {
                temp = mueller.MlynkRect();
            } else {
                temp = mueller.MlynkRectMitStockAndersrum();
            }

            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne ihn jetzt

            // Redet er etwa gerade ??
            if ((TalkPerson == 36) && (mainFrame.talkCount > 0)) {
                if (!muellerSprichtMitStock) {
                    mueller.talkMlynk(g);
                } else {
                    mueller.talkMlynkWithKijAndersrum(g, false);  // Stock nur ab und zu heben
                }
            }

            // nur rumstehen oder laufen
            else {
                mueller.drawMlynk(g);
            }

            if (showKrabat) {
                // redender Krabat
                if ((mainFrame.talkCount > 0) && (TalkPerson == 1)) {
                    mainFrame.krabat.talkKrabat(g);
                } else {
                    // beschreibender Krabat
                    if ((mainFrame.talkCount > 0) && (TalkPerson == 3)) {
                        mainFrame.krabat.describeKrabat(g);
                    }
                    // rumstehender Krabat
                    else {
                        mainFrame.krabat.drawKrabat(g);
                    }
                }
            }
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
                // Textausgabe
                PersonSagt(HText, DText, NText, 0, 54, 2, 20, new GenericPoint(320, 200));
                break;

            case 20:
                // Mueller spricht
                PersonSagt(Start.stringManager.getTranslation("Loc2_Mlyn2_00012"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00013"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00014"),
                        0, 36, 2, 30, mueller.evalMlynkTalkPoint());
                break;

            case 30:
                // Mueller spricht
                PersonSagt(Start.stringManager.getTranslation("Loc2_Mlyn2_00015"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00016"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00017"),
                        0, 36, 2, 35, mueller.evalMlynkTalkPoint());
                break;

            case 35:
                // Mueller spricht
                PersonSagt(Start.stringManager.getTranslation("Loc2_Mlyn2_00018"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00019"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00020"),
                        0, 36, 2, 40, mueller.evalMlynkTalkPoint());
                break;

            case 40:
                // Gehe nach Kolmc Teil 2
                NeuesBild(75, 90);
                break;

            case 500:
                // Muellertext fuer Anmecker...
                int zf = (int) (Math.round(Math.random() * KONSTANTE));
                PersonSagt(HAMecker[zf], DAMecker[zf], NAMecker[zf], 0, 36, 2, 40, mueller.evalMlynkTalkPoint());
                break;

            case 1000:
                // Text vom Erzaehler
                PersonSagt(Start.stringManager.getTranslation("Loc2_Mlyn2_00021"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00022"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00023"),
                        0, 54, 2, 1010, new GenericPoint(320, 200));
                break;

            case 1010:
                // Mueller spricht
                muellerSprichtMitStock = true;
                PersonSagt(Start.stringManager.getTranslation("Loc2_Mlyn2_00024"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00025"),
                        Start.stringManager.getTranslation("Loc2_Mlyn2_00026"),
                        0, 36, 2, 1020, mueller.evalMlynkTalkPoint());
                break;

            case 1020:
                // Gehe nach Doma Teil 2
                muellerSprichtMitStock = false;
                NeuesBild(71, 90);
                break;

            case 2000:
                // Ersten Teil Text fuer Uebergang Swoboda zeichnen
                PersonSagt(H1Text, D1Text, N1Text, 0, 54, 2, 2010, new GenericPoint(320, 200));
                break;

            case 2010:
                // Skip zu Swoboda
                NeuesBild(92, 90);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }

    private void evalSound() {
        int zf = (int) (Math.random() * 100);

        if (zf > 96) {

            int zwzf = (int) (Math.random() * 2.99);
            zwzf += 49;

            mainFrame.wave.PlayFile("sfx/mlyn" + (char) zwzf + ".wav");
        }
    }
}