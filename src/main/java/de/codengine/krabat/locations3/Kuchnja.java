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

package de.codengine.krabat.locations3;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.KitchenChef;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Kuchnja extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Kuchnja.class);
    private GenericImage background;
    private GenericImage herd;
    private GenericImage schwein;
    private GenericImage herd2;
    private GenericImage herd3;
    private GenericImage holz;
    private final GenericImage[] Feuer;
    private final GenericImage[] Wusmuz;
    private final GenericImage[] Ueberkoch;
    private final KitchenChef kitchenChef;
    private final MultipleChoice Dialog;

    private boolean kucharHoertZu = false;
    private boolean kucharIstAufgestanden = false;

    // private boolean walkReady = true;

    private int Feuerwidth = 25;
    private int Feuercount = 0;
    private int Verhinderfeuer;
    private static final int MAX_VERHINDERFEUER = 2;

    private int Wusmuzcount = 1;
    private int Wusmuzy = 399;
    private int Verhinderwusmuz;
    private static final int MAX_VERHINDERWUSMUZ = 2;

    private int Verhindersteigen;
    private static final int MAX_VERHINDERSTEIGEN = 5;

    private int Ueberkochcount = 0;

    private boolean isUeberkoching = false;

    private boolean ueberkochSound = true;

    private boolean schnauzeKuchar = false; // Sound sperren

    // fuer Holzreinschmeissen
    private boolean holzFliegt = false;
    /*
    private int flugCounter = 0;
    private int Verhinderflieg;
    */
    // private static final int MAX_VERHINDERFLIEG = 2;
    private static final GenericPoint flugStart = new GenericPoint(195, 393);
    private static final GenericPoint flugEnde = new GenericPoint(140, 479);
    private static final float XOFFSET = 8;
    private final float Yoffset;
    private float Xpos;
    private float Ypos;

    // Konstanten - Rects
    private static final BorderRect rechterAusgang
            = new BorderRect(467, 120, 505, 320);
    private static final BorderRect glocke
            = new BorderRect(104, 230, 125, 252);
    private static final BorderRect kochtopf
            = new BorderRect(40, 390, 110, 455);
    private static final BorderRect drjewo
            = new BorderRect(237, 342, 305, 392);
    private static final BorderRect durje
            = new BorderRect(9, 87, 100, 380);
    private static final BorderRect kachle
            = new BorderRect(141, 427, 218, 479);
    private static final BorderRect wokno
            = new BorderRect(560, 0, 640, 118);
    private static final BorderRect swinjo
            = new BorderRect(454, 387, 639, 479);
    private static final BorderRect herdRect     // zum drueberzeichnen
            = new BorderRect(0, 386, 347, 520);
    private static final BorderRect schweinRect  // zum drueberzeichnen
            = new BorderRect(388, 344, 639, 479);

    // Konstante Points
    private static final GenericPoint pRight = new GenericPoint(470, 343);
    private static final GenericPoint pKuchar = new GenericPoint(410, 379);
    private static final GenericPoint pSwinjo = new GenericPoint(490, 430);
    private static final GenericPoint pDrjewo = new GenericPoint(260, 400);
    private static final GenericPoint pWokno = new GenericPoint(575, 355);
    private static final GenericPoint pKachle = new GenericPoint(250, 500);
    private static final GenericPoint pTopf = new GenericPoint(66, 470);
    private static final GenericPoint pGlocke = new GenericPoint(121, 381);
    private static final GenericPoint pDurje = new GenericPoint(79, 398);

    // private static final GenericPoint kuPoint     = new GenericPoint (403, 240);
    // private static final GenericPoint kuPointHerd = new GenericPoint ( 66, 341);

    private static final GenericPoint FeuerMitte = new GenericPoint(171, 469);

    // Konstnate ints
    private static final int fKuchar = 12;
    private static final int fSwinjo = 6;
    private static final int fDrjewo = 12;
    private static final int fDurje = 9;
    private static final int fGlocke = 9;
    private static final int fWokno = 12;
    private static final int fHerd = 6;
    private static final int fHornc = 6;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Kuchnja(Start caller, int oldLocation) {
        super(caller, 120);

        mainFrame.freeze(true);

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = -80;

        kitchenChef = new KitchenChef(mainFrame);
        Dialog = new MultipleChoice(mainFrame);
    
        /*kuchar.maxx     = mainFrame.krabat.maxx;
          kuchar.zoomf    = mainFrame.krabat.zoomf;
          kuchar.defScale = mainFrame.krabat.defScale;
    
          kuchar.SetKucharPos (kuPoint);
          kuchar.SetFacing (6); */

        Feuer = new GenericImage[11];
        Wusmuz = new GenericImage[6];
        Ueberkoch = new GenericImage[4];

        InitLocation(oldLocation);

        Verhinderfeuer = MAX_VERHINDERFEUER;
        Verhinderwusmuz = MAX_VERHINDERWUSMUZ;
        Verhindersteigen = MAX_VERHINDERSTEIGEN;

        // Verhinderflieg = MAX_VERHINDERFLIEG;

        Xpos = flugStart.x;
        float xe = flugEnde.x;
        Ypos = flugStart.y;
        float ye = flugEnde.y;

        Yoffset = (ye - Ypos) / (Xpos - xe) * XOFFSET;

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Feuergroesse zuruecksetzen, wenn kein Load erfolgte
        if (oldLocation != 0) {
            mainFrame.actions[625] = false;
            mainFrame.actions[626] = false;
            mainFrame.actions[627] = false;
        } else {
            // wenn Load erfolgte, dann muss die wusmuzgroesse festgestellt werden
            if (mainFrame.actions[625]) {
                Wusmuzy = 395;
            }
            if (mainFrame.actions[626]) {
                Wusmuzy = 393;
            }
            if (mainFrame.actions[627]) {
                Wusmuzy = 391;
            }
        }

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        // mainFrame.wegGeher.vBorders.addElement 
        //	(new bordertrapez (405, 445, 400, 475, 305, 354));
        // mainFrame.wegGeher.vBorders.addElement
        //     (new bordertrapez (478, 353, 482, 372));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(370, 575, 360, 610, 373, 399));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(125, 610, 75, 620, 400, 430));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(75, 380, 75, 380, 431, 510));

        mainFrame.pathFinder.ClearMatrix(3);

        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        // mainFrame.wegSucher.PosVerbinden (2, 3);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(25, true);
                break;
            case 94:
                // von Jezba aus - Skip aus Teil 2
                mainFrame.krabat.setPos(new GenericPoint(284, 417));
                mainFrame.krabat.SetFacing(3);
                break;
            case 121:
                // von Hintergasse aus, geht nicht !!!!!!!!! (?)
                mainFrame.krabat.setPos(new GenericPoint(455, 325));
                mainFrame.krabat.SetFacing(9);
                break;
            case 141:
                // von Dinglinger reingesteckt worden - STRAFE !
                BackgroundMusicPlayer.getInstance().playTrack(25, true);
                // Diesntkleidung zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(41);
                // Eigene Kleidung entfernen
                mainFrame.inventory.vInventory.removeElement(53);
                // Flag setzen -> normale Kleidung
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.krabat.setPos(new GenericPoint(284, 417));
                mainFrame.krabat.SetFacing(3);
                break;
            case 143:
                // von Hauptwaechter reingesteckt worden - STRAFE !
                BackgroundMusicPlayer.getInstance().playTrack(25, true);
                // Diesntkleidung zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(41);
                // Eigene Kleidung entfernen
                mainFrame.inventory.vInventory.removeElement(53);
                // Flag setzen -> normale Kleidung
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.krabat.setPos(new GenericPoint(284, 417));
                mainFrame.krabat.SetFacing(3);
                break;
            case 145:
                // von August reingesteckt worden - STRAFE !  geht nicht !!! (?)
                // Diesntkleidung zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(41);
                // Eigene Kleidung entfernen
                mainFrame.inventory.vInventory.removeElement(53);
                // Flag setzen -> normale Kleidung
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.krabat.setPos(new GenericPoint(284, 417));
                mainFrame.krabat.SetFacing(3);
                break;
        }

        mainFrame.checkKrabat();

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/kuchnja/kuchnja.png");
        herd = getPicture("gfx-dd/kuchnja/herd.png");
        schwein = getPicture("gfx-dd/kuchnja/schwein.png");
        herd2 = getPicture("gfx-dd/kuchnja/herd2.png");
        herd3 = getPicture("gfx-dd/kuchnja/herd3.png");
        holz = getPicture("gfx-dd/kuchnja/kdrjewo.png");

        Feuer[0] = getPicture("gfx/wjes/wn0.png");
        Feuer[1] = getPicture("gfx/wjes/wn1.png");
        Feuer[2] = getPicture("gfx/wjes/wn2.png");
        Feuer[3] = getPicture("gfx/wjes/wn3.png");
        Feuer[4] = getPicture("gfx/wjes/wn4.png");
        Feuer[5] = getPicture("gfx/wjes/wn5.png");
        Feuer[6] = getPicture("gfx/wjes/wn6.png");
        Feuer[7] = getPicture("gfx/wjes/wn7.png");
        Feuer[8] = getPicture("gfx/wjes/wn8.png");
        Feuer[9] = getPicture("gfx/wjes/wn9.png");
        Feuer[10] = getPicture("gfx/wjes/wn10.png");

        Wusmuz[1] = getPicture("gfx-dd/kuchnja/sp1.png");
        Wusmuz[2] = getPicture("gfx-dd/kuchnja/sp2.png");
        Wusmuz[3] = getPicture("gfx-dd/kuchnja/sp3.png");
        Wusmuz[4] = getPicture("gfx-dd/kuchnja/sp4.png");
        Wusmuz[5] = getPicture("gfx-dd/kuchnja/sp5.png");

        Ueberkoch[0] = getPicture("gfx-dd/kuchnja/herd7.png");
        Ueberkoch[1] = getPicture("gfx-dd/kuchnja/herd6.png");
        Ueberkoch[2] = getPicture("gfx-dd/kuchnja/herd5.png");
        Ueberkoch[3] = getPicture("gfx-dd/kuchnja/herd4.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        /*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
          {
          Dialog.paintMultiple (g);
          return;
          } */

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Kuchar Hintergrund loeschen
        // Clipping - Rectangle feststellen und setzen
        BorderRect temp = kitchenChef.KucharRect();
        g.setClip(temp.lo_point.x, temp.lo_point.y, temp.ru_point.x - temp.lo_point.x,
                temp.ru_point.y - temp.lo_point.y);

        // Zeichne Hintergrund neu
        g.drawImage(background, 0, 0);

        // Hintergrund fuer fliegendes Holz wiederherstellen
        if (holzFliegt) {
            g.setClip(154, 410, 90, 70);
            g.drawImage(background, 0, 0);
        }

        // Kuchar bewegen
        /*if (walkReady == false)
          {
          // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)    	
          walkReady = kuchar.Move();
          }
    
          // Kuchar zeichnen
          // Clipping - Rectangle feststellen und setzen
          borderrect temp2 = kuchar.KucharRect();
          g.setClip (temp2.lo_point.x - 10, temp2.lo_point.y - 10, temp2.ru_point.x - temp2.lo_point.x + 20,
          temp2.ru_point.y - temp2.lo_point.y + 20);
                 
          // Zeichne sie jetzt
      
          // Redet sie etwa gerade ??
          if ((TalkPerson == 42) && (mainFrame.talkCount > 0))
          {
          kuchar.talkKuchar (g);	
          }
      
          // nur rumstehen oder laufen
          else
          {
          kuchar.drawKuchar (g);
          }	*/

        // Kuchar-Cliprect wieder neu holen
        temp = kitchenChef.KucharRect();
        g.setClip(temp.lo_point.x, temp.lo_point.y, temp.ru_point.x - temp.lo_point.x,
                temp.ru_point.y - temp.lo_point.y);

        kitchenChef.drawKuchar(g, TalkPerson, kucharHoertZu, kucharIstAufgestanden, schnauzeKuchar);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Krabat einen Schritt laufen lassen
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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinter Herd oder Schwein ? (nur Clipping - Region wird neugezeichnet)
        if (herdRect.IsPointInRect(pKrTemp)) {
            g.drawImage(herd, 25, 387);
        }
        if (schweinRect.IsPointInRect(pKrTemp)) {
            g.drawImage(schwein, 427, 337);
        }

        // Herd - Anims sind im Vordergrund, deshalb hier

        // Clipbounds retten, da spaeter wiederhergestellt werden muessen
        GenericRectangle miy;
        miy = g.getClipBounds();
        g.setClip(0, 335, 335, 479);

        // Feuer je nach Groesse animieren
        if (--Verhinderfeuer < 1) {
            Verhinderfeuer = MAX_VERHINDERFEUER;
            Feuercount++;
            if (Feuercount == 11) {
                Feuercount = 0;
            }

            if (mainFrame.actions[625] && Feuerwidth < 32) {
                Feuerwidth++;
            }
            if (mainFrame.actions[626] && Feuerwidth < 39) {
                Feuerwidth++;
            }
            if (mainFrame.actions[627] && Feuerwidth < 46) {
                Feuerwidth++;
            }
        }

        float feuerTemp = Feuerwidth;
        float Feueroffset = feuerTemp / 1.1f;

        g.drawImage(Feuer[Feuercount], FeuerMitte.x - Feuerwidth / 2, 481 - (int) Feueroffset, Feuerwidth, Feuerwidth);

        // hier das fliegende Holzscheit animieren
        if (holzFliegt) {
            Xpos -= XOFFSET;
            Ypos += Yoffset;

            if (Xpos < flugEnde.x) {
                holzFliegt = false;
                Xpos = flugStart.x;
                Ypos = flugStart.y;
            } else {
                g.drawImage(holz, (int) Xpos, (int) Ypos);
            }
        }

        // Herd davorzeichnen
        g.drawImage(herd2, 149, 461);

        // Wusmuz je nach Groesse animieren
        if (!isUeberkoching) {
            // nur Kochen, bis 3. Mal Holz angelegt
            if (--Verhinderwusmuz < 1) {
                Verhinderwusmuz = MAX_VERHINDERWUSMUZ;
                Wusmuzcount++;
                if (Wusmuzcount == 6) {
                    Wusmuzcount = 1;
                }

                if (--Verhindersteigen < 1) {
                    Verhindersteigen = MAX_VERHINDERSTEIGEN;
                    if (mainFrame.actions[625] && Wusmuzy > 395) {
                        Wusmuzy--;
                    }
                    if (mainFrame.actions[626] && Wusmuzy > 393) {
                        Wusmuzy--;
                    }
                    if (mainFrame.actions[627] && Wusmuzy > 391) {
                        Wusmuzy--;
                    }
                }
            }

            evalSound(mainFrame.actions[625], false);  // einzelne Blubbs

            // hier Skip auf richtige Ueberkochanims
            if (Wusmuzy <= 391) {
                isUeberkoching = true;
            }

            g.drawImage(Wusmuz[Wusmuzcount], 52, Wusmuzy);
            g.drawImage(herd3, 48, 406);
        } else {
            // hier geht es dann mit dem richtigen Ueberkochen los...

            evalSound(true, true); // einmaliges langes Blubb

            if (--Verhinderwusmuz < 1) {
                Verhinderwusmuz = MAX_VERHINDERWUSMUZ;
                Wusmuzcount++;
                if (Wusmuzcount == 6) {
                    Wusmuzcount = 1;
                }

                if (--Verhindersteigen < 1) {
                    Verhindersteigen = MAX_VERHINDERSTEIGEN;
                    if (Ueberkochcount < 3) {
                        Ueberkochcount++;
                    }
                }
            }
            g.drawImage(Ueberkoch[Ueberkochcount], 41, 387);
            g.drawImage(Wusmuz[Wusmuzcount], 52, Wusmuzy);
        }

        // Clipping - Region wiederherstellen
        g.setClip(miy.getX(), miy.getY(), miy.getWidth(), miy.getHeight());

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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // bei Multiple Choice extra Mouseroutine
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseEvent(e);
            return;
        }

        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.isAnimRunning) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // linker Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Koch
                if (kitchenChef.KucharRect().IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 150;
                    pTemp = pKuchar;
                }

                // Holz in Ofen schmeissen oder Ausreden fuer Ofen
                if (kachle.IsPointInRect(pTemp)) {
                    // drjewo
                    nextActionID = mainFrame.whatItem == 35 ? 45 : 155;
                    pTemp = pKachle;
                }

                // Schwein Ausreden
                if (swinjo.IsPointInRect(pTemp)) {
                    // kotwica
                    nextActionID = mainFrame.whatItem == 37 ? 195 : 160;
                    pTemp = pSwinjo;
                }

                // Holz Ausreden
                if (drjewo.IsPointInRect(pTemp)) {
                    nextActionID = 165;
                    pTemp = pDrjewo;
                }

                // Tuer Ausreden
                if (durje.IsPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTemp = pDurje;
                }

                // Glocke Ausreden
                if (glocke.IsPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = pGlocke;
                }

                // Fenster Ausreden
                if (wokno.IsPointInRect(pTemp)) {
                    nextActionID = 180;
                    pTemp = pWokno;
                }

                // Kochtopf ausreden
                if (kochtopf.IsPointInRect(pTemp)) {
                    nextActionID = 185;
                    pTemp = pTopf;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                GenericPoint pTt = new GenericPoint(pTemp.x, pTemp.y);

                // zu Hintergasse gehen ? -> Anschiss
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 620;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTt = pRight;
                    } else {
                        // es wird nach unten verlassen
                        pTt = new GenericPoint(kt.x, pRight.y);
                    }

                    // if (mainFrame.dClick == true)
                    // {
                    //   mainFrame.krabat.StopWalking();
                    //   mainFrame.repaint();
                    //   return;
                    // }
                }

                // Schwein ansehen
                if (swinjo.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTt = pSwinjo;
                }
                // Holz ansehen
                if (drjewo.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTt = pDrjewo;
                }
                // Tuer ansehen
                if (durje.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTt = pDurje;
                }
                // Glocke ansehen
                if (glocke.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTt = pGlocke;
                }
                // Fenster ansehen
                if (wokno.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTt = pWokno;
                }
                // Herd (Feuer) ansehen
                if (kachle.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTt = pKachle;
                }

                // Kochtopf ansehen
                if (kochtopf.IsPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTt = pTopf;
                }

                // Kuchar ansehen
                if (kitchenChef.KucharRect().IsPointInRect(pTemp) && !rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTt = pKuchar;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTt);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Mit dem Kuchar reden
                if (kitchenChef.KucharRect().IsPointInRect(pTemp) && !rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(pKuchar);
                    mainFrame.repaint();
                    return;
                }

                // Holz mitnehmen
                if (drjewo.IsPointInRect(pTemp)) {
                    nextActionID = 40;
                    mainFrame.pathWalker.SetzeNeuenWeg(pDrjewo);
                    mainFrame.repaint();
                    return;
                }

                // Ofen mitnehmen
                if (kachle.IsPointInRect(pTemp)) {
                    nextActionID = 80;
                    mainFrame.pathWalker.SetzeNeuenWeg(pKachle);
                    mainFrame.repaint();
                    return;
                }

                // Schwein mitnehmen
                if (swinjo.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(pSwinjo);
                    mainFrame.repaint();
                    return;
                }

                // Tuer mitnehmen
                if (durje.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.SetzeNeuenWeg(pDurje);
                    mainFrame.repaint();
                    return;
                }

                // Glocke mitnehmen
                if (glocke.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(pGlocke);
                    mainFrame.repaint();
                    return;
                }

                // Fenster mitnehmen
                if (wokno.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.SetzeNeuenWeg(pWokno);
                    mainFrame.repaint();
                    return;
                }

                // Kochtopf mitnehmen
                if (kochtopf.IsPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.pathWalker.SetzeNeuenWeg(pTopf);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    kitchenChef.KucharRect().IsPointInRect(pTemp) ||
                    kachle.IsPointInRect(pTemp) ||
                    glocke.IsPointInRect(pTemp) ||
                    drjewo.IsPointInRect(pTemp) ||
                    durje.IsPointInRect(pTemp) ||
                    wokno.IsPointInRect(pTemp) ||
                    swinjo.IsPointInRect(pTemp) ||
                    kochtopf.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.isInventoryHighlightCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (Cursorform != 11 && mainFrame.isInventoryHighlightCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
                }
                return;
            }

            if (glocke.IsPointInRect(pTemp) ||
                    drjewo.IsPointInRect(pTemp) ||
                    durje.IsPointInRect(pTemp) ||
                    wokno.IsPointInRect(pTemp) ||
                    kachle.IsPointInRect(pTemp) ||
                    swinjo.IsPointInRect(pTemp) ||
                    kochtopf.IsPointInRect(pTemp) ||
                    kitchenChef.KucharRect().IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                Cursorform = 0;
            }
        }
    }

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultipleChoiceActive) {
            return;
        }

        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.isInventoryCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.isAnimRunning) {
            return;
        }

        // Bei Krabat - Animation keine Keys
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();

        // Hauptmenue aktivieren
        if (Taste == GenericKeyEvent.VK_F1) {
            Keyclear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            Keyclear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            Keyclear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void Keyclear() {
        outputText = "";
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.StopWalking();
    }

    private GenericPoint evalKucharTalkPoint() {
        // Hier Position des Textes berechnen
        BorderRect temp = kitchenChef.KucharRect();
        return new GenericPoint((temp.ru_point.x + temp.lo_point.x) / 2, temp.lo_point.y - 50);
    }

    private void evalSound(boolean erster, boolean zweiter) {
        // zufaellig wavs fuer Geschnatter abspielen...
        if (!zweiter)  // nur einzelne blubbs spielen
        {
            if (schnauzeKuchar) {
                return; // grosses Blubb hat Prio vor allem anderen
            }

            int zfz = (int) (Math.random() * 100);
            if (zfz > 97 && !erster || zfz > 95 && erster) {
                int zwzfz = (int) (Math.random() * 2.99);
                zwzfz += 49;
                mainFrame.soundPlayer.PlayFile("sfx-dd/blubb" + (char) zwzfz + ".wav");
            }
        } else {
            if (ueberkochSound) // nur 1x abspielen
            {
                ueberkochSound = false;
                mainFrame.soundPlayer.PlayFile("sfx-dd/blubblub.wav");
            }
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if (nextActionID > 499 && nextActionID < 600) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.mousePoint);

            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Kuchar anschauen
                KrabatSagt("Kuchnja_1", fKuchar, 3, 0, 0);
                break;

            case 2:
                // Schwein anschauen
                int zuffZahl = (int) (Math.random() * 2.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Kuchnja_2", fSwinjo, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Kuchnja_3", fSwinjo, 3, 0, 0);
                        break;

                    case 2:
                        KrabatSagt("Kuchnja_4", fSwinjo, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Holz anschauen
                int zuffZahl3 = (int) (Math.random() * 1.9);
                switch (zuffZahl3) {
                    case 0:
                        KrabatSagt("Kuchnja_5", fDrjewo, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Kuchnja_6", fDrjewo, 3, 0, 0);
                        break;
                }
                break;

            case 4:
                // Tuer anschauen
                KrabatSagt("Kuchnja_7", fDurje, 3, 0, 0);
                break;

            case 5:
                // Glocke anschauen
                KrabatSagt("Kuchnja_8", fGlocke, 3, 0, 0);
                break;

            case 6:
                // Fenster anschauen
                KrabatSagt("Kuchnja_9", fWokno, 3, 0, 0);
                break;

            case 7:
                // Herd anschauen
                if (!mainFrame.actions[625] && !mainFrame.actions[626] && !mainFrame.actions[627]) {
                    KrabatSagt("Kuchnja_10", fHerd, 3, 0, 0);
                } else {
                    KrabatSagt("Kuchnja_11", fHerd, 3, 0, 0);
                }
                break;

            case 8:
                // Kochtopf anschauen
                if (!mainFrame.actions[625] && !mainFrame.actions[626] && !mainFrame.actions[627]) {
                    KrabatSagt("Kuchnja_12", fHornc, 3, 0, 0);
                } else {
                    KrabatSagt("Kuchnja_13", fHornc, 3, 0, 0);
                }
                break;


            case 40:
                // Holzscheitel mitnehmen (wenn noch keins im Inventar)
                if (!mainFrame.actions[950]) {
                    nextActionID = 0;
                    // zu Inventar hinzufuegen
                    mainFrame.inventory.vInventory.addElement(35);
                    mainFrame.actions[950] = true;
                    mainFrame.krabat.nAnimation = 122;
                    mainFrame.krabat.SetFacing(fDrjewo);
                    mainFrame.repaint();
                } else {
                    KrabatSagt("Kuchnja_14", fDrjewo, 3, 0, 0);
                }
                break;

            case 45:
                // Holz in Ofen schmeissen -> Reaktion je nach Feuergroesse
                mainFrame.krabat.SetFacing(fHerd);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                // Holz aus Inventory entfernen
                mainFrame.krabat.nAnimation = 149;
                mainFrame.inventory.vInventory.removeElement(35);
                // bei 3. Stufe die Kuchar-Sound abschalten
                if (mainFrame.actions[626]) {
                    schnauzeKuchar = true;
                }
                nextActionID = 46;
                break;

            case 46:
                // Ende K-Anim und Start Holz-Anim
                if (mainFrame.krabat.nAnimation == 149) {
                    break;
                }
                holzFliegt = true;
                nextActionID = 47;
                break;

            case 47:
                // Ende Holzfliegen und weiter
                if (holzFliegt) {
                    break;
                }
                mainFrame.actions[950] = false;

                if (mainFrame.actions[626]) {
                    mainFrame.actions[627] = true;
                }
                if (mainFrame.actions[625]) {
                    mainFrame.actions[626] = true;
                }
                if (!mainFrame.actions[625]) {
                    mainFrame.actions[625] = true;
                }

                // Feuer erste Stufe
                if (!mainFrame.actions[626]) {
                    nextActionID = 300;
                    break;
                }

                // Feuer 2. Stufe
                if (!mainFrame.actions[627]) {
                    nextActionID = 301;
                    break;
                }

                // Feuer groesste Stufe
                nextActionID = 302;
                break;


            case 50:
                // Krabat beginnt MC (Kuchar benutzen)
                mainFrame.krabat.SetFacing(fKuchar);
                mainFrame.isAnimRunning = true;
                kucharHoertZu = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 55:
                // Schwein mitnehmen
                KrabatSagt("Kuchnja_15", fSwinjo, 3, 0, 0);
                break;

            case 60:
                // Tuer mitnehmen
                KrabatSagt("Kuchnja_16", fDurje, 3, 0, 0);
                break;

            case 65:
                // Glocke mitnehmen
                KrabatSagt("Kuchnja_17", fGlocke, 3, 0, 0);
                break;

            case 70:
                // Fenster mitnehmen
                KrabatSagt("Kuchnja_18", fWokno, 3, 0, 0);
                break;

            case 75:
                // Kochtopf mitnehmen
                KrabatSagt("Kuchnja_19", fHornc, 3, 0, 0);
                break;

            case 80:
                // Herd mitnehmen
                KrabatSagt("Kuchnja_20", fHornc, 3, 0, 0);
                break;

            case 100:
                // Animationssequenz beenden
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // Gehe zu Hintergasse
                NeuesBild(121, locationID);
                break;

            case 150:
                // Kuchar - Ausreden
                MPersonAusrede(fKuchar);
                break;

            case 155:
                // Dinge ins Feuer schmeissen
                KrabatSagt("Kuchnja_21", fHerd, 3, 0, 0);
                break;

            case 160:
                // Dinge dem Schwein geben
                KrabatSagt("Kuchnja_22", fSwinjo, 3, 0, 0);
                break;

            case 165:
                // Drjewo - Ausreden
                DingAusrede(fDrjewo);
                break;

            case 170:
                // durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 175:
                // klinkac  Ausreden
                DingAusrede(fGlocke);
                break;

            case 180:
                // wokno - Ausreden
                DingAusrede(fWokno);
                break;

            case 185:
                // Dinge in den Topf werfen
                KrabatSagt("Kuchnja_23", fHornc, 3, 0, 0);
                break;

            case 195:
                // kotwica auf swino
                KrabatSagt("Kuchnja_24", fSwinjo, 3, 0, 0);
                break;


            // Sequenzen mit Kuchar  /////////////////////////////////

            case 300:
                // Reaktion Krabat, wenn Holz in Ofen
                // if (mainFrame.krabat.nAnimation != 0) break;
                KrabatSagt("Kuchnja_25", 0, 3, 2, 800);
                break;

            case 301:
                // Reaktion Krabat, wenn Holz in Ofen 2 mal
                // if (mainFrame.krabat.nAnimation != 0) break;
                KrabatSagt("Kuchnja_26", 0, 3, 2, 800);
                break;

            case 302:
                // Reaktion Krabat, wenn Holz in Ofen 3 mal -> dann zur Hintergasse
                // if (mainFrame.krabat.nAnimation != 0) break;
                KrabatSagt("Kuchnja_27", 0, 3, 2, 303);
                break;

            case 303:
                // Reaktion Koch, wenn Brei ueberkocht
                kucharHoertZu = true;
                PersonSagt("Kuchnja_28", fKuchar, 42, 2, 304, evalKucharTalkPoint());
                break;

            case 304:
                // Koch aufstehen lassen
                kucharIstAufgestanden = true;
                kucharHoertZu = false;
                nextActionID = 305;
                break;

            case 305:
                // kleine Verzoegerung...
                nextActionID = 309;
                break;

            case 309:
                // Reaktion Koch, wenn Brei ueberkocht
                PersonSagt("Kuchnja_29", 0, 42, 2, 310, evalKucharTalkPoint());
                break;

            case 310:
                // Krabat geht zum Ausgang Hintergasse
                nextActionID = 320;
                mainFrame.pathWalker.SetzeNeuenWeg(pRight);
                break;

            case 320:
                // nun auch wirklich hinten raus laufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(pRight);
                nextActionID = 100;
                break;

            // Dialog mit Kuchar ///////////////////////////////////

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);

                // 1. Frage
                Dialog.ExtendMC("Kuchnja_49", 1000, 504, new int[]{504}, 610);
                Dialog.ExtendMC("Kuchnja_50", 504, 503, new int[]{503}, 611);
                Dialog.ExtendMC("Kuchnja_51", 503, 502, new int[]{502}, 612);
                Dialog.ExtendMC("Kuchnja_52", 502, 501, new int[]{501}, 613);
                Dialog.ExtendMC("Kuchnja_53", 501, 500, new int[]{500}, 615);
                Dialog.ExtendMC("Kuchnja_54", 500, 1000, null, 617);

                // 2. Frage
                Dialog.ExtendMC("Kuchnja_55", 1000, 507, new int[]{507}, 630);
                Dialog.ExtendMC("Kuchnja_56", 507, 506, new int[]{506}, 631);
                Dialog.ExtendMC("Kuchnja_57", 506, 505, new int[]{505}, 632);
                Dialog.ExtendMC("Kuchnja_58", 505, 1000, null, 634);

                // 3. Frage
                Dialog.ExtendMC("Kuchnja_59", 1000, 509, new int[]{509}, 640);
                Dialog.ExtendMC("Kuchnja_60", 509, 508, new int[]{508}, 641);
                Dialog.ExtendMC("Kuchnja_61", 508, 1000, null, 642);

                // 4. Frage
                Dialog.ExtendMC("Kuchnja_62", 1000, 1000, null, 620);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            // Antworten zu Frage 1 ////////////////////////////
            case 610:
                // Reaktion Koch
                PersonSagt("Kuchnja_30", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 611:
                // Reaktion Koch
                PersonSagt("Kuchnja_31", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 612:
                // Reaktion Koch
                PersonSagt("Kuchnja_32", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 613:
                // Reaktion Koch
                PersonSagt("Kuchnja_33", 0, 42, 2, 614, evalKucharTalkPoint());
                break;

            case 614:
                // Reaktion Koch
                PersonSagt("Kuchnja_34", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 615:
                // Reaktion Koch
                PersonSagt("Kuchnja_35", 0, 42, 2, 616, evalKucharTalkPoint());
                break;

            case 616:
                // Reaktion Koch
                PersonSagt("Kuchnja_36", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 617:
                // Reaktion Koch
                PersonSagt("Kuchnja_37", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            // Antworten zu Frage 2 ////////////////////////////
            case 620:
                // Reaktion Koch (zufaellige Antwort)
                // Zafallszahl von 0 bis 5 generieren
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.krabat.SetFacing(12);
                nextActionID = 800;  // Dialog danach beenden (ausser letzte Antwort)
                int zuffZahl2 = (int) (Math.random() * 5.9);
                switch (zuffZahl2) {
                    case 0:
                        outputText = mainFrame.imageFont.TeileTextKey("Kuchnja_63");
                        break;
                    case 1:
                        outputText = mainFrame.imageFont.TeileTextKey("Kuchnja_64");
                        break;
                    case 2:
                        outputText = mainFrame.imageFont.TeileTextKey("Kuchnja_65");
                        break;
                    case 3:
                        outputText = mainFrame.imageFont.TeileTextKey("Kuchnja_66");
                        break;
                    case 4:
                        outputText = mainFrame.imageFont.TeileTextKey("Kuchnja_67");
                        break;
                    case 5:
                        outputText = mainFrame.imageFont.TeileTextKey("Kuchnja_68");
                        nextActionID = 621;
                        break;
                }

                outputTextPos = mainFrame.imageFont.CenterText(outputText, evalKucharTalkPoint());
                TalkPerson = 42;
                TalkPause = 2;
                break;

            case 621:
                // Reaktion Krabat
                KrabatSagt("Kuchnja_38", 0, 1, 2, 800);
                break;

            // Antworten zu Frage 3 ////////////////////////////
            case 630:
                // Reaktion Koch
                PersonSagt("Kuchnja_39", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 631:
                // Reaktion Koch
                PersonSagt("Kuchnja_40", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 632:
                // Reaktion Koch
                PersonSagt("Kuchnja_41", 0, 42, 2, 633, evalKucharTalkPoint());
                break;

            case 633:
                // Reaktion Koch
                PersonSagt("Kuchnja_42", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 634:
                // Reaktion Koch
                PersonSagt("Kuchnja_43", 0, 42, 2, 635, evalKucharTalkPoint());
                break;

            case 635:
                // Reaktion Koch
                PersonSagt("Kuchnja_44", 0, 42, 2, 600, evalKucharTalkPoint());

                break;

            // Antworten zu Frage 4 ////////////////////////////
            case 640:
                // Reaktion Koch
                PersonSagt("Kuchnja_45", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 641:
                // Reaktion Koch
                PersonSagt("Kuchnja_46", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 642:
                // Reaktion Koch
                PersonSagt("Kuchnja_47", 0, 42, 2, 643, evalKucharTalkPoint());
                break;

            case 643:
                // Reaktion Koch
                PersonSagt("Kuchnja_48", 0, 42, 2, 600, evalKucharTalkPoint());
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                kucharHoertZu = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}