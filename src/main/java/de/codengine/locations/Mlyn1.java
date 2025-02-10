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

package de.codengine.locations;

import de.codengine.Start;
import de.codengine.anims.Bumm;
import de.codengine.anims.Mlynk2;
import de.codengine.anims.Mlynkptack;
import de.codengine.anims.PtackZaRapaka;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Mlyn1 extends Mainloc {
    private GenericImage background, offeneTuer, foreground;

    private final GenericImage[] Rad;
    private int Radcount = 1;
    private int Verhinderrad;
    private static final int MAX_VERHINDERRAD = 3;

    private Mlynk2 mueller;
    private boolean showPersonen = false;
    private boolean setClipForText = false;
    private boolean allowTextClipset = false;

    private boolean walkReady = true;
    private boolean openDoor = false;
    private boolean krabatBehindDoor = false;

    private boolean showVoegel = true;
    private boolean voegelFliegen = true;
    private PtackZaRapaka krabatvogel;
    private Mlynkptack muellervogel;

    private Bumm muellermorph;
    private Bumm krabatmorph;

    private int muellermorphcount;
    // private int krabatmorphcount;

    private boolean ismuellermorphing = false;
    private boolean iskrabatmorphing = false;

    private boolean schnauzeRad = true;

    // Text - Variablen
    private static final String HText = Start.stringManager.getTranslation("Loc1_Mlyn1_00000");
    private static final String DText = Start.stringManager.getTranslation("Loc1_Mlyn1_00001");
    private static final String NText = Start.stringManager.getTranslation("Loc1_Mlyn1_00002");

    // Konstanten - Rects
    // private static final borderrect rechterAusgang = new borderrect (560, 402, 639, 479);

    // Konstante Points
    // private static final GenericPoint Pright       = new GenericPoint (639, 467);
    private static final GenericPoint Pkrabat = new GenericPoint(300, 452);
    private static final GenericPoint mlynkFeet = new GenericPoint(155, 440);
    private static final GenericPoint doorPoint = new GenericPoint(65, 395);
    private static final GenericPoint vorDoorPoint = new GenericPoint(78, 412);
    private static final GenericPoint muehleRein = new GenericPoint(110, 390);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mlyn1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(12, true);

        mainFrame.krabat.maxx = 452;
        mainFrame.krabat.zoomf = 0.9f;
        mainFrame.krabat.defScale = -10;

        Rad = new GenericImage[21];
        mueller = new Mlynk2(mainFrame);

        mueller.maxx = 440;
        mueller.zoomf = 0.9f;
        mueller.defScale = 0;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(3);

        krabatvogel = new PtackZaRapaka(mainFrame, 845, 408, 10, 300);
        muellervogel = new Mlynkptack(mainFrame, 700, 370, 10, 161, true);

        muellermorph = new Bumm(mainFrame);
        krabatmorph = new Bumm(mainFrame);

        InitLocation(oldLocation);
        mainFrame.Freeze(false);

        nextActionID = 10;
        TalkPause = 5;

        Verhinderrad = MAX_VERHINDERRAD;
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(154, 639, 242, 639, 442, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(90, 100, 154, 164, 435, 441));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(90, 100, 90, 100, 420, 434));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(60, 70, 77, 100, 395, 419));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(4);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);

        InitImages();

        // Hier Inventar komplett loeschen und nur noch das Wichtige drinlassen
        mainFrame.inventory.vInventory.removeAllElements();
        mainFrame.inventory.vInventory.addElement(Integer.valueOf(1));

        mainFrame.krabat.SetKrabatPos(Pkrabat);
        mainFrame.krabat.SetFacing(9);
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mlyn/mlyn2.gif");
        offeneTuer = getPicture("gfx/mlyn/mldurje.gif");
        foreground = getPicture("gfx/mlyn/mlmauer.gif");

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

        loadPicture();
    }

    @Override
    public void cleanup() {
        background = null;
        offeneTuer = null;
        foreground = null;

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
        krabatvogel.cleanup();
        krabatvogel = null;
        muellervogel.cleanup();
        muellervogel = null;
        muellermorph.cleanup();
        muellermorph = null;
        krabatmorph.cleanup();
        krabatmorph = null;
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
            setClipForText = false;
            mainFrame.isAnim = true;
            mainFrame.fPlayAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // Mueller Hintergrund loeschen
        if (showPersonen) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.MlynkRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0, null);
        }

        // Hintergrund fuer Tuer loeschen, wenn auf
        if (openDoor) {
            g.setClip(72, 325, 14, 70);
            g.drawImage(offeneTuer, 72, 325, null);
        }

        // Hintergrund Loeschen, wenn Voegel da (muss vor erster Anim erfolgen)
        if (showVoegel) {
            g.setClip(muellervogel.mlynkPtackRect());
            g.drawImage(background, 0, 0, null);
            g.setClip(krabatvogel.ptack2Rect());
            g.drawImage(background, 0, 0, null);
        }

        // Hintergrund fuer "Bumm" loeschen beim Mueller
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0, null);
        }

        // Hier Hintergrund fuer Krabat"bumm"
        if (iskrabatmorphing) {
            g.setClip(krabatmorph.bummRect());
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

        // Mueller bewegen
        if ((showPersonen) && (!walkReady)) {
            // Mueller um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = mueller.Move();
        }

        mainFrame.wegGeher.GeheWeg();

        // Mueller zeichnen
        if (showPersonen) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.MlynkRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne ihn jetzt

            // Redet er etwa gerade ??
            if ((TalkPerson == 36) && (mainFrame.talkCount > 0)) {
                mueller.talkMlynk(g);
            }

            // nur rumstehen oder laufen
            else {
                mueller.drawMlynk(g);
            }

            if (openDoor) {
                g.drawImage(foreground, 77, 306, null);
            }

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

            if (krabatBehindDoor) {
                g.drawImage(foreground, 77, 306, null);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Voegel in Bild reinfliegen lassen
        if (showVoegel) {
            g.setClip(muellervogel.mlynkPtackRect());
            voegelFliegen = muellervogel.Flieg(g);
            g.setClip(krabatvogel.ptack2Rect());
            krabatvogel.Flieg(g);
        }

        // hier die Morphanim zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }

        // dsgl. fuer Krabat
        if (iskrabatmorphing) {
            g.setClip(krabatmorph.bummRect());
            /* krabatmorphcount = */
            krabatmorph.drawBumm(g);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            if (!setClipForText) {
                g.setClip(0, 0, 644, 484);
            } else {
                g.setClip(120, 260, 135, 145);
            }
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            if (allowTextClipset) {
                if (!setClipForText) {
                    setClipForText = true;
                }
                g.setClip(120, 260, 135, 145);
            } else {
                g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
            }
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
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering) ||
                (mainFrame.krabat.isWalking)) {
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 10:
                // Auf Ende des Fliegens warten
                if (!voegelFliegen) {
                    nextActionID = 11;
                }
                break;

            case 11:
                // morphing beginnen
                muellermorph.Init(mlynkFeet, 100);
                krabatmorph.Init(Pkrabat, -100);  // Krabats Morph macht keinen Krach
                ismuellermorphing = true;
                iskrabatmorphing = true;
                nextActionID = 12;
                break;

            case 12:
                // Personen einschalten und los gehts
                if (muellermorphcount < 3) {
                    break;
                }
                showPersonen = true;
                showVoegel = false;
                mainFrame.Clipset = false;
                nextActionID = 15;
                TalkPause = 2;
                break;

            case 15:
                // Mueller spricht
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                iskrabatmorphing = false;
                schnauzeRad = false;
                mainFrame.Clipset = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mlyn1_00003"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00004"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00005"),
                        9, 36, 2, 20, mueller.evalMlynkTalkPoint());
                break;

            case 20:
                // Krabat spricht
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Mlyn1_00006"), Start.stringManager.getTranslation("Loc1_Mlyn1_00007"), Start.stringManager.getTranslation("Loc1_Mlyn1_00008"),
                        0, 3, 2, 30);
                break;

            case 30:
                // Krabat spricht
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Mlyn1_00009"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00010"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00011"),
                        0, 1, 2, 40);
                break;

            case 40:
                // Mueller spricht
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mlyn1_00012"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00013"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00014"),
                        0, 36, 2, 50, mueller.evalMlynkTalkPoint());
                break;

            case 50:
                // Krabat spricht
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Mlyn1_00015"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00016"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00017"),
                        0, 1, 2, 60);
                break;

            case 60:
                // Mueller spricht
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mlyn1_00018"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00019"),
                        Start.stringManager.getTranslation("Loc1_Mlyn1_00020"),
                        0, 36, 2, 65, mueller.evalMlynkTalkPoint());
                break;

            case 65: // lasse beide reinlaufen
                schnauzeRad = true;
                mueller.MoveTo(doorPoint);
                walkReady = false;
                mainFrame.wegGeher.SetzeNeuenWeg(vorDoorPoint);
                nextActionID = 70;
                break;

            case 70:
                // warten auf ich habe fertig
                mainFrame.krabat.SetFacing(12);
                if (walkReady) {
                    nextActionID = 71;
                }
                break;

            case 71:
                // Mueller oeffnet Tuer und geht rein
                openDoor = true;
                mainFrame.wave.PlayFile("sfx/cdurjeauf.wav");
                mueller.MoveTo(muehleRein);
                walkReady = false;
                mainFrame.wegGeher.SetzeNeuenWeg(doorPoint);
                nextActionID = 72;
                break;

            case 72:
                // warten auf ich habe fertig
                if (walkReady) {
                    nextActionID = 73;
                }
                break;

            case 73:
                // jetzt geht auch Krabat rein
                krabatBehindDoor = true;
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(muehleRein);
                nextActionID = 79;
                break;

            case 79:
                // Figuren ausblenden
                mainFrame.Clipset = false;
                showPersonen = false;
                openDoor = false;
                mainFrame.wave.PlayFile("sfx-dd/gdurjezu.wav");
                krabatBehindDoor = false;
                nextActionID = 80;
                break;

            case 80:
                // Textausgabe
                mainFrame.Clipset = false;
                allowTextClipset = true;
                PersonSagt(HText, DText, NText, 0, 54, 2, 90, new GenericPoint(320, 400));
                break;

            case 90:
                // Weiter in den Locations
                allowTextClipset = false;
                NeuesBild(91, 25);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }

    private void evalSound() {
        if (schnauzeRad) {
            return;
        }

        int zf = (int) (Math.random() * 100);

        if (zf > 96) {

            int zwzf = (int) (Math.random() * 2.99);
            zwzf += 49;

            mainFrame.wave.PlayFile("sfx/mlyn" + (char) zwzf + ".wav");
        }
    }
}