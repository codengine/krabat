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
import de.codengine.anims.Kocka;
import de.codengine.anims.Mlynk2;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Dzera extends Mainloc {
    private GenericImage background;

    private Mlynk2 mueller;
    private Kocka katze;

    private boolean setAnim = false;

    // Points
    private final GenericPoint kockaTalk = new GenericPoint(0, 0);
    private final GenericPoint Pkocka = new GenericPoint(0, 0);

    // Konstante Points
    private static final GenericPoint mlynkFeet = new GenericPoint(195, 363);
    private static final GenericPoint kockaFeet = new GenericPoint(363, 363);
    // private static final GenericPoint krabatTalk = new GenericPoint (320, 100);
    // private static final GenericPoint erzTalk    = new GenericPoint (200, 320);

    // Text
  /*private static final String HText = Start.stringManager.getTranslation("Loc2_Dzera_00000");
  private static final String DText = Start.stringManager.getTranslation("Loc2_Dzera_00001");
  private static final String NText = Start.stringManager.getTranslation("Loc2_Dzera_00002");
  */

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Dzera(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mueller = new Mlynk2(mainFrame);
        katze = new Kocka(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = -20;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(3);

        Pkocka.x = kockaFeet.x - (Kocka.Breite / 2);
        Pkocka.y = kockaFeet.y - Kocka.Hoehe;
        kockaTalk.x = kockaFeet.x;
        kockaTalk.y = Pkocka.y - 50;

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mlyn/guck.gif");

        loadPicture();
    }

    @Override
    public void cleanup() {
        background = null;

        mueller.cleanup();
        mueller = null;
        katze.cleanup();
        katze = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
            mainFrame.fPlayAnim = true;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

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

        // Katze zeichnen
        g.setClip(Pkocka.x, Pkocka.y, Kocka.Breite, Kocka.Hoehe);
        g.drawImage(background, 0, 0, null);
        katze.drawKocka(g, TalkPerson, Pkocka);

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
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

        if (!setAnim) {
            setAnim = true;
            nextActionID = 100;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
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
            TalkPerson = 0;
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
            case 100:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00003"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00004"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00005"),
                        0, 36, 0, 110, mueller.evalMlynkTalkPoint());
                break;

            case 110:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00006"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00007"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00008"),
                        0, 59, 2, 120, kockaTalk);
                break;

            case 120:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00009"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00010"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00011"),
                        0, 36, 2, 130, mueller.evalMlynkTalkPoint());
                break;

            case 130:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00012"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00013"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00014"),
                        0, 59, 2, 135, kockaTalk);
                break;

            case 135:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00015"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00016"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00017"),
                        0, 59, 2, 140, kockaTalk);
                break;

            case 140:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00018"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00019"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00020"),
                        0, 36, 2, 150, mueller.evalMlynkTalkPoint());
                break;

            case 150:
                // Krabat redet
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Dzera_00021"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00022"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00023"),
                        0, 3, 2, 160);
                break;

            case 160:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00024"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00025"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00026"),
                        0, 59, 2, 170, kockaTalk);
                break;

            case 170:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00027"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00028"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00029"),
                        0, 36, 2, 180, mueller.evalMlynkTalkPoint());
                break;

            case 180:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00030"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00031"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00032"),
                        0, 59, 2, 190, kockaTalk);
                break;

            case 190:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00033"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00034"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00035"),
                        0, 36, 2, 200, mueller.evalMlynkTalkPoint());
                break;

            case 200:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00036"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00037"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00038"),
                        0, 59, 2, 205, kockaTalk);
                break;

            case 205:
                // Katze redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00039"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00040"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00041"),
                        0, 59, 2, 210, kockaTalk);
                break;

            case 210:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00042"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00043"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00044"),
                        0, 36, 2, 220, mueller.evalMlynkTalkPoint());
                break;

            case 220:
                // Mueller redet
                PersonSagt(Start.stringManager.getTranslation("Loc2_Dzera_00045"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00046"),
                        Start.stringManager.getTranslation("Loc2_Dzera_00047"),
                        0, 36, 2, 250, mueller.evalMlynkTalkPoint());
                break;
        
      /*case 230:
        // Krabat redet
        if (mainFrame.sprache == 1) outputText = mainFrame.ifont.TeileText (Start.stringManager.getTranslation("Loc2_Dzera_00048"));
        if (mainFrame.sprache == 2) outputText = mainFrame.ifont.TeileText (Start.stringManager.getTranslation("Loc2_Dzera_00049"));
        if (mainFrame.sprache == 3) outputText = mainFrame.ifont.TeileText (Start.stringManager.getTranslation("Loc2_Dzera_00050"));
        outputTextPos = mainFrame.ifont.CenterText (outputText, krabatTalk);
        TalkPerson = 3;
        TalkPause = 2;
        nextActionID = 250;
        break;
        
      case 240:
      	// ErzaehlerText - wird geskippt !
        if (mainFrame.sprache == 1) outputText = mainFrame.ifont.TeileText (HText);
        if (mainFrame.sprache == 2) outputText = mainFrame.ifont.TeileText (DText);
        if (mainFrame.sprache == 3) outputText = mainFrame.ifont.TeileText (NText);
        outputTextPos = mainFrame.ifont.CenterText (outputText, erzTalk);
        TalkPerson = 5;
        TalkPause = 2;
        nextActionID = 250;
        break;  */

            case 250:
                // Skip zur Muehle Innen
                NeuesBild(91, 28);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}