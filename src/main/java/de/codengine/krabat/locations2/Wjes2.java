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

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.anims.BlacksmithSunow;
import de.codengine.krabat.anims.Boom;
import de.codengine.krabat.anims.Messenger;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Wjes2 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Wjes2.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage sky;
    private GenericImage wjes2;
    private GenericImage wjes3;
    private GenericImage wjes4;
    private GenericImage wjes5;
    private final GenericImage[] Schild;
    private final GenericImage[] Feuer;
    private boolean setScroll = false;
    private int scrollwert;
    private BlacksmithSunow schmied;
    private final MultipleChoice Dialog;
    private Messenger bote;
    private Miller mueller;

    private Boom muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    private int animcount = 1;
    private boolean animfor = true;
    private int wartezeit = 10;
    private static final int[] animzeit = {0, 7, 4, 3, 2, 7, 7, 2, 3, 4, 7};

    private int Feuercount = 0;
    private boolean switchanim = false;

    private boolean setAnim = false;
    private boolean muellerda = false;

    private final GenericPoint boteText;
    private final GenericPoint Pbote;

    private boolean ListenSchmied = false;
    private boolean schnauzeSchmied = false;

    // Konstanten - Rects
    private static final BorderRect obererAusgang = new BorderRect(925, 207, 984, 248);
    private static final BorderRect untererAusgang = new BorderRect(532, 450, 760, 479);
    private static final BorderRect linkerAusgang = new BorderRect(0, 242, 48, 338);
    private static final BorderRect rechterAusgang = new BorderRect(1231, 240, 1280, 301);
    private static final BorderRect brTuer = new BorderRect(825, 287, 884, 357);
    private static final BorderRect wjes2Rect = new BorderRect(191, 326, 396, 446);
    private static final BorderRect wjes3Rect = new BorderRect(912, 236, 1028, 313);
    private static final BorderRect wjes4Rect = new BorderRect(694, 357, 1150, 477);
    private static final BorderRect wjes5Rect = new BorderRect(1162, 235, 1279, 355);
    private static final BorderRect sudobjaRect = new BorderRect(405, 320, 464, 369);
    private static final BorderRect woknoRect = new BorderRect(584, 344, 610, 369);
    private static final BorderRect schildRect = new BorderRect(921, 259, 958, 303);
    private static final BorderRect wohenRect = new BorderRect(1072, 227, 1086, 242);

    // Konstante Punkte
    private static final GenericPoint Pschmied = new GenericPoint(1026, 279);
    private static final GenericPoint SchmiedTalk = new GenericPoint(1069, 100);
    private static final GenericPoint boteFeet = new GenericPoint(1067, 375);
    private static final GenericPoint Pkbote = new GenericPoint(989, 380);
    private static final GenericPoint Pup = new GenericPoint(946, 261);
    private static final GenericPoint Pdown = new GenericPoint(640, 479);
    private static final GenericPoint Pright = new GenericPoint(1279, 262);
    private static final GenericPoint Pleft = new GenericPoint(0, 288);
    private static final GenericPoint mlynkFeet = new GenericPoint(615, 443);
    private static final GenericPoint Psudobja = new GenericPoint(419, 369);
    private static final GenericPoint Pwokno = new GenericPoint(601, 419);
    private static final GenericPoint Pschild = new GenericPoint(899, 371);
    private static final GenericPoint Pwohen = new GenericPoint(1051, 262);

    // Konstante ints
    private static final int fSchmied = 3;
    private static final int fSudobja = 3;
    private static final int fWokno = 12;
    private static final int fSchild = 3;
    private static final int fWohen = 3;
    private static final int fTuer = 12;

    // Konstruktor ////////////////////////////////////////////////////////////////////////////

    public Wjes2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        schmied = new BlacksmithSunow(mainFrame);
        bote = new Messenger(mainFrame);
        mueller = new Miller(mainFrame);
        Dialog = new MultipleChoice(mainFrame);

        muellermorph = new Boom(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = 35;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(6);

        mainFrame.krabat.maxx = 368;
        mainFrame.krabat.zoomf = 18.4f;
        mainFrame.krabat.defScale = 25;

        Pbote = new GenericPoint(boteFeet.x - Messenger.Breite / 2, boteFeet.y - Messenger.Hoehe);
        boteText = new GenericPoint(boteFeet.x, Pbote.y - 50);

        Schild = new GenericImage[6];
        Feuer = new GenericImage[11];

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                if (mainFrame.actions[300]) {
                    BackgroundMusicPlayer.getInstance().playTrack(20, true);
                } else {
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                break;
            case 71: // Aus Doma kommend ueber Karte
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(640, 460));
                mainFrame.krabat.SetFacing(12);
                scrollwert = 320;
                setScroll = true;
                break;
            case 84: // Aus Sunow kommend, geht nur bei Heimgehszene
                BackgroundMusicPlayer.getInstance().stop(); // Mueller, also nix CD
                mainFrame.krabat.setPos(new GenericPoint(640, 460));
                mainFrame.krabat.SetFacing(12);
                scrollwert = 320;
                setScroll = true;
                schnauzeSchmied = true;
                setAnim = true;
                TalkPause = 10;
                break;
            case 83: // Von Rapak aus ueber Karte
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(1226, 299));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
            case 76: // Von Kulow aus (Karte)
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(946, 265));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 636;
                setScroll = true;
                break;
            case 73: // Von Jaeger aus
                if (!mainFrame.actions[300]) {
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(16, 292));
                mainFrame.krabat.SetFacing(3);
                scrollwert = 0;
                setScroll = true;
                break;
        }

        InitLocation();

        // hier Achtung, ob Animationsmodus eingeschaltet werden muss !!!
        if (mainFrame.actions[302]) {
            setAnim = true;
        }

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(452, 815, 593, 742, 435, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 129, 440, 570, 307, 434));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 18, 0, 129, 273, 306));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(870, 1010, 639, 807, 363, 434));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1011, 1070, 1061, 1120, 363, 415));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1061, 1171, 1075, 1171, 416, 433));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1075, 1156, 1123, 1211, 434, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1036, 1090, 887, 1090, 318, 362));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(930, 960, 1038, 1103, 261, 317));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1091, 318, 1218, 335));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(1261, 1279, 1105, 1218, 266, 317));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(11);

        // Wege eintragen
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(0, 3);
        mainFrame.pathFinder.PosVerbinden(3, 7);
        mainFrame.pathFinder.PosVerbinden(4, 7);
        mainFrame.pathFinder.PosVerbinden(4, 5);
        mainFrame.pathFinder.PosVerbinden(5, 6);
        mainFrame.pathFinder.PosVerbinden(9, 10);
        mainFrame.pathFinder.PosVerbinden(9, 7);
        mainFrame.pathFinder.PosVerbinden(7, 8); // bei Problemen mit dem Gartenzaun weg !!!!!!!!!!!!!!
        mainFrame.pathFinder.PosVerbinden(9, 8);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx/wjes/wjes-l.png");
        backr = getPicture("gfx/wjes/wjes-r.png");
        sky = getPicture("gfx/wjes/sunsky.png");
        wjes2 = getPicture("gfx/wjes/wjes2.png");
        wjes3 = getPicture("gfx/wjes/wjes3.png");
        wjes4 = getPicture("gfx/wjes/wjes4.png");
        wjes5 = getPicture("gfx/wjes/wjes5.png");
        Schild[1] = getPicture("gfx/wjes/gs1.png");
        Schild[2] = getPicture("gfx/wjes/gs2.png");
        Schild[3] = getPicture("gfx/wjes/gs3.png");
        Schild[4] = getPicture("gfx/wjes/gs4.png");
        Schild[5] = getPicture("gfx/wjes/gs5.png");
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

    }

    @Override
    public void cleanup() {
        backl = null;
        backr = null;
        sky = null;
        wjes2 = null;
        wjes3 = null;
        wjes4 = null;
        wjes5 = null;
        Schild[1] = null;
        Schild[2] = null;
        Schild[3] = null;
        Schild[4] = null;
        Schild[5] = null;
        Feuer[0] = null;
        Feuer[1] = null;
        Feuer[2] = null;
        Feuer[3] = null;
        Feuer[4] = null;
        Feuer[5] = null;
        Feuer[6] = null;
        Feuer[7] = null;
        Feuer[8] = null;
        Feuer[9] = null;
        Feuer[10] = null;

        schmied.cleanup();
        schmied = null;
        bote.cleanup();
        bote = null;
        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Hier faellt das MC-Abkuerzen raus, weil Anims noch im Hintergrund laufen muessen !!!!!!!!!!!!!

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 964);

            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }

            mainFrame.isBackgroundAnimRunning = true;

            // fuer garantierten Beginn einer Anim !!!
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Hintergrund zeichnen
        g.drawImage(sky, mainFrame.scrollX / 10, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Parallaxer ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollX - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 91);
            g.drawImage(sky, mainFrame.scrollX / 10, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // Ab hier ist Retten des ClipRect sinnlos!!!

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // Hintergrund fuer Schild loeschen, wenn noetig
        if (mainFrame.scrollX > 255) {
            g.setClip(903, 259, 71, 51);
            g.drawImage(backr, 640, 0);
        }

        // Hintergrund fuer Schmied und Feuer loeschen
        g.setClip(1047, 177, 100, 103);
        g.drawImage(backr, 640, 0);

        // Boten Hintergrund loeschen
        if (mainFrame.actions[302]) {
            g.setClip(Pbote.x, Pbote.y, Messenger.Breite, Messenger.Hoehe);
            g.drawImage(backr, 640, 0);
        }

        // Feuer animieren
        if (mainFrame.scrollX > 320) {
            switchanim = !switchanim;
            if (switchanim) {
                Feuercount++;
                if (Feuercount == 11) {
                    Feuercount = 0;
                }
            }
            g.setClip(1068, 219, 26, 26);
            g.drawImage(Feuer[Feuercount], 1068, 219);
        }

        // Schmied zeichnen
        g.setClip(1047, 177, 100, 103);
        schmied.drawKowar(g, TalkPerson, ListenSchmied, schnauzeSchmied); // Sound je nach Flag, bei Mueller weg

        // Mueller zeichnen
        if (muellerda) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(backl, 0, 0);

            // Redet er etwa gerade ??
            if (TalkPerson == 36 && mainFrame.talkCount > 0) {
                mueller.talkMlynk(g);
            }

            // nur rumstehen oder laufen
            else {
                mueller.drawMlynk(g);
            }
        }

        // bei gemorphtem Mueller nun das Bumm zeichnen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            muellermorphcount = muellermorph.drawBumm(g);
        }

        // Krabats neue Position festlegen wenn noetig
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

        // Ab hier muss Cliprect wieder gerettet werden

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm wjes2 (nur Clipping - Region wird neugezeichnet)
        if (wjes2Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(wjes2, 215, 339);
        }

        // hinterm wjes3 (nur Clipping - Region wird neugezeichnet)
        if (wjes3Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(wjes3, 911, 205);
        }

        // hinterm wjes4 (nur Clipping - Region wird neugezeichnet)
        if (wjes4Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(wjes4, 736, 367);
        }

        // hinterm wjes5 (nur Clipping - Region wird neugezeichnet)
        if (wjes5Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(wjes5, 1191, 247);
        }

        // Boten zeichnen, wenn noetig, hat Vorrang vor Krabat
        if (mainFrame.actions[302]) {
            GenericRectangle rt = g.getClipBounds();
            g.setClip(Pbote.x, Pbote.y, Messenger.Breite, Messenger.Hoehe);
            bote.drawPosol(g, TalkPerson, Pbote);
            g.setClip(rt);
        }

        // wackelndes Schild - Animation
        if (mainFrame.scrollX > 255) {
            wartezeit--;
            // System.out.print (wartezeit + " ");
            if (wartezeit == 0) {
                if (animfor) {
                    animcount++;
                    if (animcount == 6) {
                        animcount = 4;
                        animfor = false;
                    }
                    wartezeit = animzeit[animcount + 5];
                } else {
                    animcount--;
                    if (animcount == 0) {
                        animcount = 2;
                        animfor = true;
                    }
                    wartezeit = animzeit[animcount];
                }
            }
            GenericRectangle mi;
            mi = g.getClipBounds();
            g.setClip(903, 259, 71, 51);
            g.drawImage(Schild[animcount], 903, 259);
            g.setClip(mi.getX(), mi.getY(), mi.getWidth(), mi.getHeight());
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
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
        // Achtung ! Hier SetAnim - Variable fuer garantierte Anims !!!
        // darf erst hier ausgewertet werden, da durch "fPlayAnim" vorher mouseEvents verriegelt werden muessen
        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            if (mainFrame.actions[302]) {
                nextActionID = 1050;
            }
            if (mainFrame.actions[300]) {
                nextActionID = 1000;
            }
        }

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

        // Auszugebenden Text abbrechen
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollX;

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
            // linke Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Sudobja
                if (sudobjaRect.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 155;
                    pTxxx = Psudobja;
                }

                // Ausreden fuer Wokno
                if (woknoRect.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 160;
                    pTxxx = Pwokno;
                }

                // Ausreden fuer Schild
                if (schildRect.IsPointInRect(pTemp)) {
                    // kij
                    nextActionID = mainFrame.whatItem == 2 ? 220 : 165;
                    pTxxx = Pschild;
                }

                // Ausreden fuer Wohen
                if (wohenRect.IsPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTxxx = Pwohen;
                }

                // Ausreden fuer Schmied
                // Schmied nach Feuer, da er vor dem feuer sein kann !!!!!
                if (schmied.schmiedRect().IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 200;
                            break;
                        case 18: // bron
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTxxx = Pschmied;
                }

                // wenn nix ausgewaehlt, dann einfach nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // Gegenstand grundsaetzlich wieder ablegen
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // nach Weiden gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTxxx = Pup;
                    } else {
                        pTxxx = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Sunow gehen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTxxx = Pdown;
                    } else {
                        pTxxx = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Horiz gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTxxx = Pright;
                    } else {
                        pTxxx = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Jaeger gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTxxx = Pleft;
                    } else {
                        pTxxx = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Hoscenc gehen
                if (brTuer.IsPointInRect(pTemp)) {
                    nextActionID = 80;
                    pTxxx = new GenericPoint(854, 362);
                }

                // Sudobja ansehen
                if (sudobjaRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTxxx = Psudobja;
                }

                // Wokno ansehen
                if (woknoRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTxxx = Pwokno;
                }

                // Schild ansehen
                if (schildRect.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTxxx = Pschild;
                }

                // Wohen ansehen
                if (wohenRect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTxxx = Pwohen;
                }

                // Schmied ansehen
                // Schmied nach Feuer, da er davor laufen kann !!!!!!!!!
                if (schmied.schmiedRect().IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTxxx = Pschmied;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weg nach Weiden anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Sunow anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Horiz anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Jaeger anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Hoscenc anschauen
                if (brTuer.IsPointInRect(pTemp)) {
				/*nextActionID = 80;
				  mainFrame.wegGeher.SetzeNeuenWeg (new GenericPoint (854, 362));
				  mainFrame.repaint();*/
                    return;
                }

                // mit dem Schmied reden
                // hier Schmied als erstes, da Routine terminiert !
                if (schmied.schmiedRect().IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pschmied);
                    mainFrame.repaint();
                    return;
                }

                // Sudobja mitnehmen
                if (sudobjaRect.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(Psudobja);
                    mainFrame.repaint();
                    return;
                }

                // Wokno mitnehmen
                if (woknoRect.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwokno);
                    mainFrame.repaint();
                    return;
                }

                // Schild mitnehmen
                if (schildRect.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Wohen mitnehmen
                if (wohenRect.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwohen);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();

            }
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultipleChoiceActive) {
            Dialog.evalMouseMoveEvent(pTxxx);
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollX, pTxxx.y + mainFrame.scrollY);

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
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) || schmied.schmiedRect().IsPointInRect(pTemp) ||
                    sudobjaRect.IsPointInRect(pTemp) || woknoRect.IsPointInRect(pTemp) ||
                    schildRect.IsPointInRect(pTemp) || wohenRect.IsPointInRect(pTemp);

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
            if (schmied.schmiedRect().IsPointInRect(pTemp) ||
                    sudobjaRect.IsPointInRect(pTemp) || woknoRect.IsPointInRect(pTemp) ||
                    schildRect.IsPointInRect(pTemp) || wohenRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp) || brTuer.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.cursorDown);
                    Cursorform = 5;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 2;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.cursorRight);
                    Cursorform = 3;
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

        // System.out.println("Nextaction " + nextActionID);

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Schmied anschauen
                KrabatSagt("Wjes2_1", fSchmied, 3, 0, 0);
                break;

            case 2:
                // Sudobja anschauen
                KrabatSagt("Wjes2_2", fSudobja, 3, 0, 0);
                break;

            case 3:
                // Wokno anschauen
                KrabatSagt("Wjes2_3", fWokno, 3, 0, 0);
                break;

            case 4:
                // Schild anschauen
                KrabatSagt("Wjes2_4", fSchild, 3, 0, 0);
                break;

            case 5:
                // Wohen anschauen
                KrabatSagt("Wjes2_5", fWohen, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Schmied benutzen)
                mainFrame.krabat.SetFacing(fSchmied);
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                ListenSchmied = true;
                nextActionID = 600;
                break;

            case 55:
                // Sudobja mitnehmen
                KrabatSagt("Wjes2_6", fSudobja, 3, 0, 0);
                break;

            case 60:
                // Wokno mitnehmen
                KrabatSagt("Wjes2_7", fWokno, 3, 0, 0);
                break;

            case 65:
                // Schild mitnehmen
                KrabatSagt("Wjes2_8", fSchild, 3, 0, 0);
                break;

            case 70:
                // wohen mitnehmen
                KrabatSagt("Wjes2_9", fWohen, 3, 0, 0);
                break;

            case 80:
                // Hoscenc mitnehmen
                KrabatSagt("Wjes2_10", fTuer, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Sunow oder Karte
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                if (mainFrame.actions[300]) {
                    mainFrame.constructLocation(84);
                    mainFrame.destructLocation(87);
                } else {
                    // Karte einblenden
                    mainFrame.constructLocation(106);
                    mainFrame.whatScreen = ScreenType.MAP;
                }
                mainFrame.repaint();
                break;

            case 101:
                // Gehe zu Horiz oder Karte
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                if (mainFrame.actions[300]) {
                    mainFrame.constructLocation(88);
                    mainFrame.destructLocation(87);
                } else {
                    // Karte einblenden
                    mainFrame.constructLocation(106);
                    mainFrame.whatScreen = ScreenType.MAP;
                }
                mainFrame.repaint();
                break;

            case 102:
                // Gehe zu Jaeger oder Karte
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                if (mainFrame.actions[300]) {
                    mainFrame.constructLocation(73);
                    mainFrame.destructLocation(87);
                } else {
                    // Karte einblenden
                    mainFrame.constructLocation(106);
                    mainFrame.whatScreen = ScreenType.MAP;
                }
                mainFrame.repaint();
                break;

            case 103:
                // Gehe zu Weiden oder Kulow
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                nextActionID = 0;
                if (mainFrame.actions[300]) {
                    mainFrame.constructLocation(86);
                    mainFrame.destructLocation(87);
                } else {
                    // Karte einblenden
                    mainFrame.constructLocation(106);
                    mainFrame.whatScreen = ScreenType.MAP;
                }
                mainFrame.repaint();
                break;

            case 150:
                // Schmied - Ausreden
                MPersonAusrede(fSchmied);
                break;

            case 155:
                // sudobjo - Ausreden
                DingAusrede(fSudobja);
                break;

            case 160:
                // wokno - Ausreden
                DingAusrede(fWokno);
                break;

            case 165:
                // znamjo - Ausreden
                DingAusrede(fSchild);
                break;

            case 170:
                // Ausreden wohen
                KrabatSagt("Wjes2_11", fWohen, 3, 0, 0);
                break;

            case 200:
                // kij auf kowar
                KrabatSagt("Wjes2_12", fSchmied, 3, 0, 0);
                break;

            case 210:
                // bron auf kowar
                KrabatSagt("Wjes2_13", fSchmied, 3, 0, 0);
                break;

            case 220:
                // kij auf znamjo
                KrabatSagt("Wjes2_14", fSchild, 3, 0, 0);
                break;

            case 230:
                // kamuski auf wohen
                KrabatSagt("Wjes2_15", fWohen, 3, 0, 0);
                break;

            // Dialog mit Bauer

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // 2. Frage
                Dialog.ExtendMC("Wjes2_30", 1000, 66, new int[]{66}, 660);
                Dialog.ExtendMC("Wjes2_31", 66, 67, new int[]{67}, 670);
                Dialog.ExtendMC("Wjes2_32", 67, 68, new int[]{68}, 680);
                Dialog.ExtendMC("Wjes2_33", 68, 69, new int[]{69}, 690);
                Dialog.ExtendMC("Wjes2_34", 69, 70, new int[]{70}, 700);

                // 3. Frage (bedeutet Ende)
                Dialog.ExtendMC("Wjes2_35", 1000, 1000, null, 800);

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

            case 660:
                // Reaktion Kowar auf 1. Teil 2. Frage
                PersonSagt("Wjes2_16", 0, 22, 2, 600, SchmiedTalk);
                break;

            case 670:
                // Reaktion Kowar auf 2. Teil 2. Frage
                PersonSagt("Wjes2_17", 0, 22, 2, 671, SchmiedTalk);
                break;

            case 671:
                // Reaktion Kowar auf 2. Teil 2. Frage
                PersonSagt("Wjes2_18", 0, 22, 2, 600, SchmiedTalk);
                break;

            case 680:
                // Reaktion Kowar auf 3. Teil 2. Frage
                PersonSagt("Wjes2_19", 0, 22, 2, 681, SchmiedTalk);
                break;

            case 681:
                // Reaktion Kowar auf 3. Teil 2. Frage
                PersonSagt("Wjes2_20", 0, 22, 2, 600, SchmiedTalk);
                break;

            case 690:
                // Reaktion Kowar auf 4. Teil 2. Frage
                PersonSagt("Wjes2_21", 0, 22, 2, 691, SchmiedTalk);
                break;

            case 691:
                // Reaktion Kowar auf 4. Teil 2. Frage
                PersonSagt("Wjes2_22", 0, 22, 2, 692, SchmiedTalk);
                break;

            case 692:
                // Reaktion Kowar auf 4. Teil 2. Frage
                PersonSagt("Wjes2_23", 0, 22, 2, 600, SchmiedTalk);
                break;

            case 700:
                // Reaktion Kowar auf 5. Teil 2. Frage
                PersonSagt("Wjes2_24", 0, 22, 2, 701, SchmiedTalk);
                break;

            case 701:
                // Reaktion Kowar auf 5. Teil 2. Frage
                PersonSagt("Wjes2_25", 0, 22, 2, 600, SchmiedTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                ListenSchmied = false;
                mainFrame.repaint();
                break;

            // Mueller is boese...
            case 1000:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 70);  // 68 - 100 - scaleMueller
                ismuellermorphing = true;
                nextActionID = 1003;
                break;

            case 1003:
                // Mueller erscheint
                if (muellermorphcount < 3) {
                    break;
                }
                muellerda = true;
                nextActionID = 1008;
                break;

            case 1008:
                // Mueller sagt seinen Spruch
                if (muellermorphcount < 8) {
                    break;
                }
                ismuellermorphing = false;
                mainFrame.isClipSet = false;
                MuellerMecker(mueller.evalMlynkTalkPoint());
                TalkPerson = 36;
                TalkPause = 5;
                nextActionID = 1010;
                break;

            case 1010:
                // Gehe zu Muehle zurueck
                NeuesBild(90, 87);
                break;

            // Anim: Bote redet
            case 1050:
                PersonSagt("Wjes2_26", 0, 38, 2, 1053, boteText);
                break;

            case 1053:
                // Krabat geht zum Boten
                mainFrame.pathWalker.SetzeNeuenWeg(Pkbote);
                nextActionID = 1055;
                break;

            case 1055:
                // Krabat steht vor Boten
                mainFrame.krabat.SetFacing(3);
                if (mainFrame.isScrolling) {
                    break;
                }
                mainFrame.isClipSet = false;
                nextActionID = 1058;
                break;

            case 1058:
                // Rede des Boten
                PersonSagt("Wjes2_27", 0, 38, 2, 1060, boteText);
                break;

            case 1060:
                // Rede des Boten
                PersonSagt("Wjes2_28", 0, 38, 2, 1070, boteText);
                break;

            case 1070:
                // Krabat spricht
                KrabatSagt("Wjes2_29", 0, 3, 2, 1080);
                break;

            case 1080:
                // Sprung nach Doma
                mainFrame.actions[303] = true;
                NeuesBild(71, 87);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}