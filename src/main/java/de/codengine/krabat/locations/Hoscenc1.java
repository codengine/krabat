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

package de.codengine.krabat.locations;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.DrunkGuest;
import de.codengine.krabat.anims.Dundak;
import de.codengine.krabat.anims.Innkeeper;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Hoscenc1 extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Hoscenc1.class);
    private GenericImage background;
    private GenericImage hosc6;
    private GenericImage honck;
    private GenericImage durje;
    private GenericImage vorderdurje;
    private Innkeeper wirt;
    private Dundak strolch;
    private DrunkGuest saeufer;
    private final MultipleChoice Dialog;

    private final BorderRect brStrolch;
    private final BorderRect brSaeufer;
    private final GenericPoint strolchPoint;
    private final GenericPoint saeuferPoint;
    private final GenericPoint StrolchTalk;
    private final GenericPoint SaeuferTalk;

    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos;
    private int AnimTalkPerson = 0;
    private int AnimID = 1;
    private int AnimCounter = 60;
    private boolean AnimMCLocked = false;

    private boolean showKorcmar = false;
    private boolean walkReady = true;
    private boolean doorOpen = false;

    private int Counter = 0;

    private boolean initSound = false;
    private int SoundCountdown = 20;

    // Konstante Strings
    private static final String[] AP = {"Hoscenc1_79", "Hoscenc1_80", "Hoscenc1_81"};

    // Konstanten - Rects
    private static final BorderRect linkerAusgang = new BorderRect(0, 380, 74, 479);
    private static final BorderRect hosc6Rect = new BorderRect(0, 85, 360, 479);
    private static final BorderRect honckRect = new BorderRect(235, 271, 267, 314);
    private static final BorderRect wobraz1Rect = new BorderRect(158, 166, 236, 215);
    private static final BorderRect wobraz2Rect = new BorderRect(271, 118, 343, 166);
    private static final BorderRect stolcRect = new BorderRect(553, 398, 625, 444);
    private static final BorderRect durjeRect = new BorderRect(394, 108, 460, 259);

    // Konstante Punkte
    private static final GenericPoint Psaeufer = new GenericPoint(436, 380);
    private static final GenericPoint Pstrolch = new GenericPoint(444, 434);
    private static final GenericPoint Phonck = new GenericPoint(221, 369);
    private static final GenericPoint Pleft = new GenericPoint(0, 464);
    private static final GenericPoint Pwobraz1 = new GenericPoint(187, 367);
    private static final GenericPoint Pwobraz2 = new GenericPoint(424, 313);
    private static final GenericPoint Pstolc = new GenericPoint(523, 398);
    private static final GenericPoint Pdurje = new GenericPoint(442, 281);
    private static final GenericPoint WirtOOben = new GenericPoint(367, 236);
    private static final GenericPoint WirtOben = new GenericPoint(433, 263);
    private static final GenericPoint WirtUnten = new GenericPoint(449, 327);

    // Konstante ints
    private static final int fSaeufer = 9;
    private static final int fStrolch = 3;
    private static final int fHonck = 3;
    private static final int fWobraz1 = 9;
    private static final int fWobraz2 = 12;
    private static final int fStolc = 3;
    private static final int fDurje = 12;
    private static final int fWirt = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Hoscenc1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        // hier zufaellig Lied auswaehlen
        int zf = (int) (Math.random() * 50);
        if (zf > 25) {
            BackgroundMusicPlayer.getInstance().playTrack(5, true);
        } else {
            BackgroundMusicPlayer.getInstance().playTrack(19, true);
        }

        mainFrame.krabat.maxX = 479;
        mainFrame.krabat.zoomFactor = 10.95f;
        mainFrame.krabat.defaultScale = -70;

        wirt = new Innkeeper(mainFrame);
        wirt.maxX = 0;
        wirt.zoomFactor = 10.95f;
        wirt.defaultScale = 0;
        wirt.setPos(new GenericPoint(300, 300));
        wirt.setFacing(6);

        strolch = new Dundak(mainFrame);

        strolchPoint = new GenericPoint(529, 350);

        StrolchTalk = new GenericPoint();
        StrolchTalk.x = strolchPoint.x + Dundak.Breite / 2;
        StrolchTalk.y = strolchPoint.y - 50;

        brStrolch = new BorderRect(strolchPoint.x, strolchPoint.y, strolchPoint.x + Dundak.Breite, strolchPoint.y + Dundak.Hoehe);

        saeufer = new DrunkGuest(mainFrame);

        saeuferPoint = new GenericPoint(265, 262);

        SaeuferTalk = new GenericPoint();
        SaeuferTalk.x = saeuferPoint.x + DrunkGuest.Breite / 2;
        SaeuferTalk.y = saeuferPoint.y - 78;  // war 50, hat Strolchtext gestoert

        brSaeufer = new BorderRect(saeuferPoint.x, saeuferPoint.y, saeuferPoint.x + DrunkGuest.Breite, saeuferPoint.y + DrunkGuest.Hoehe);

        Dialog = new MultipleChoice(mainFrame);

        initLocation(oldLocation);

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(447, 448, 441, 495, 283, 399));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(441, 495, 370, 450, 400, 425));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(0, 460, 0, 362, 426, 479));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(52, 220, 15, 260, 384, 425));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(185, 185, 52, 221, 340, 383));

        // Matrix loeschen
        mainFrame.pathFinder.clearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.connectPos(0, 1);
        mainFrame.pathFinder.connectPos(1, 2);
        mainFrame.pathFinder.connectPos(2, 3);
        mainFrame.pathFinder.connectPos(3, 4);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 13:
                // von Wjes aus
                initSound = true; // nur hier auch wirklich abspielen, wenn man reinkommt...
                mainFrame.krabat.setPos(new GenericPoint(35, 454));
                mainFrame.krabat.setFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx/hoscenc/hosc.png");
        hosc6 = getPicture("gfx/hoscenc/hosc6.png");
        honck = getPicture("gfx/hoscenc/hosc7.png");
        durje = getPicture("gfx/hoscenc/hdurje.png");
        vorderdurje = getPicture("gfx/hoscenc/hdurje2.png");

    }

    @Override
    public void cleanup() {
        background = null;
        hosc6 = null;
        honck = null;
        durje = null;
        vorderdurje = null;

        wirt.cleanup();
        wirt = null;
        strolch.cleanup();
        strolch = null;
        saeufer.cleanup();
        saeufer = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        if (initSound) {
            initSound = false;
            mainFrame.soundPlayer.playFile("sfx/wdurjezu.wav");
        }

        // Clipping -Region initialisieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            cursorShape = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isBackgroundAnimRunning = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // offene Tuer zeichnen, wenn noetig
        if (doorOpen) {
            g.setClip(390, 105, 77, 169);
            g.drawImage(durje, 390, 105);
        }

        // Wirt Hintergrund loeschen
        if (showKorcmar) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = wirt.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
            if (doorOpen) {
                g.drawImage(durje, 390, 105);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Honck zeichnen, da im Hintergrund !!!
        if (!mainFrame.actions[902]) {
            g.setClip(219, 271, 49, 44);
            g.drawImage(honck, 219, 271);
        }

        // Andere Personen zeichnen
        // Pjany
        g.setClip(saeuferPoint.x, saeuferPoint.y, DrunkGuest.Breite, DrunkGuest.Hoehe);
        g.drawImage(background, 0, 0);
        // Saeufer darf nur reden, wenn die Anims nicht gesperrt sind
        saeufer.drawPjany(g, saeuferPoint);

        // Dundak
        g.setClip(strolchPoint.x, strolchPoint.y, Dundak.Breite, Dundak.Hoehe);
        g.drawImage(background, 0, 0);
        strolch.drawDundak(g, talkPerson, strolchPoint, SoundCountdown);
        if (SoundCountdown > 0) {
            SoundCountdown--;
        }

        // Korcmar bewegen
        if (showKorcmar && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = wirt.move();
        }

        // Wirt zeichnen
        if (showKorcmar) {
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = wirt.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Wirt neu
            if (talkPerson == 25 && mainFrame.talkCount > 1) {
                wirt.talkKorcmar(g);
            } else {
                wirt.drawKorcmar(g);
            }

            // Vordergrund draufzeichnen
            g.drawImage(vorderdurje, 294, 63);
        }

        mainFrame.pathWalker.doWalk();

        // Krabat zeichnen

        // Animation??
        if (mainFrame.krabat.nAnimation != 0) {
            mainFrame.krabat.doAnimation(g);

            // Cursorruecksetzung nach Animationsende
            if (mainFrame.krabat.nAnimation == 0) {
                evalMouseMoveEvent(mainFrame.mousePoint);
            }
        } else {
            if (mainFrame.talkCount > 0 && talkPerson != 0) {
                // beim Reden
                switch (talkPerson) {
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

        // hinterm Balken (nur Clipping - Region wird neugezeichnet)
        if (hosc6Rect.isPointInRect(pKrTemp)) {
            g.drawImage(hosc6, 51, 185);
        }

        // Ausgabe von AnimText, falls noetig
        if (!Objects.equals(AnimOutputText, "") && !AnimMCLocked) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, COLORS[AnimTalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, COLORS[talkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                talkPerson = 0;
            }
        }

        if (talkPause > 0 && mainFrame.talkCount < 1) {
            talkPause--;
        }

        // Multiple Choice ausfuehren
        if (mainFrame.isMultipleChoiceActive) {
            mainFrame.isClipSet = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Die Anims muessen bedient werden
        if (AnimID != 0 && !AnimMCLocked) {
            doAnims();
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
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
            talkPerson = 0;
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

                BorderRect tmp = mainFrame.krabat.getBoundingBox();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Saeufer
                if (brSaeufer.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 19: // pjero
                            nextActionID = 210;
                            break;
                        case 2: // kij
                            nextActionID = 220;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Psaeufer;
                }

                // Ausreden fuer Strolch
                if (brStrolch.isPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = Pstrolch;
                }

                // Ausreden fuer Wobraz1
                if (wobraz1Rect.isPointInRect(pTemp)) {
                    // honck z blotom
                    nextActionID = mainFrame.whatItem == 16 ? 202 : 172;
                    pTemp = Pwobraz1;
                }

                // Ausreden fuer Wobraz2
                if (wobraz2Rect.isPointInRect(pTemp)) {
                    // honck z blotom
                    nextActionID = mainFrame.whatItem == 16 ? 200 : 170;
                    pTemp = Pwobraz2;
                }

                // Ausreden fuer Stolc
                if (stolcRect.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 175;
                    pTemp = Pstolc;
                }

                // Ausreden fuer Durje
                if (durjeRect.isPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 180;
                    pTemp = Pdurje;
                }

                // Ausreden fuer Honck
                if (honckRect.isPointInRect(pTemp) && !mainFrame.actions[902]) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = Phonck;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.isInventoryCursor = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // Saeufer ansehen
                if (brSaeufer.isPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Psaeufer;
                }

                // Strolch ansehen
                if (brStrolch.isPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Pstrolch;
                }

                // Honck ansehen
                if (honckRect.isPointInRect(pTemp) && !mainFrame.actions[902]) {
                    nextActionID = 4;
                    pTemp = Phonck;
                }

                // zu Wjes gehen ?
                if (linkerAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.isPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // wobraz1 ansehen
                if (wobraz1Rect.isPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pwobraz1;
                }

                // wobraz2 ansehen
                if (wobraz2Rect.isPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Pwobraz2;
                }

                // stolc ansehen
                if (stolcRect.isPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = Pstolc;
                }

                // durje ansehen
                if (durjeRect.isPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTemp = Pdurje;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Ausgang zu Wjes abfangen
                if (linkerAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Saeufer reden
                if (brSaeufer.isPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(Psaeufer);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Strolch reden
                if (brStrolch.isPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.pathWalker.setNewWay(Pstrolch);
                    mainFrame.repaint();
                    return;
                }

                // Honck nehmen
                if (honckRect.isPointInRect(pTemp) && !mainFrame.actions[902]) {
                    nextActionID = 55;
                    mainFrame.pathWalker.setNewWay(Phonck);
                    mainFrame.repaint();
                    return;
                }

                // Wobraz1 nehmen
                if (wobraz1Rect.isPointInRect(pTemp)) {
                    nextActionID = 62;
                    mainFrame.pathWalker.setNewWay(Pwobraz1);
                    mainFrame.repaint();
                    return;
                }

                // Wobraz2 nehmen
                if (wobraz2Rect.isPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.pathWalker.setNewWay(Pwobraz2);
                    mainFrame.repaint();
                    return;
                }

                // Stolc nehmen
                if (stolcRect.isPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.pathWalker.setNewWay(Pstolc);
                    mainFrame.repaint();
                    return;
                }

                // Durje nehmen
                if (durjeRect.isPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.pathWalker.setNewWay(Pdurje);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                resetAnims();
                mainFrame.isClipSet = false;
                mainFrame.isBackgroundAnimRunning = false;
                mainFrame.krabat.stopWalking();
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
            if (cursorShape != 20) {
                cursorShape = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getBoundingBox();
            mainFrame.isInventoryHighlightCursor = tmp.isPointInRect(pTemp) ||
                    brSaeufer.isPointInRect(pTemp) || brStrolch.isPointInRect(pTemp) ||
                    honckRect.isPointInRect(pTemp) && !mainFrame.actions[902] ||
                    wobraz1Rect.isPointInRect(pTemp) || wobraz2Rect.isPointInRect(pTemp) ||
                    stolcRect.isPointInRect(pTemp) || durjeRect.isPointInRect(pTemp);

            if (cursorShape != 10 && !mainFrame.isInventoryHighlightCursor) {
                cursorShape = 10;
                mainFrame.setCursor(mainFrame.cursorInventory);
            }

            if (cursorShape != 11 && mainFrame.isInventoryHighlightCursor) {
                cursorShape = 11;
                mainFrame.setCursor(mainFrame.cursorHighlightInventory);
            }
        }


        // normaler Cursor, normale Reaktion
        else {
            if (brSaeufer.isPointInRect(pTemp) ||
                    brStrolch.isPointInRect(pTemp) || honckRect.isPointInRect(pTemp) &&
                    !mainFrame.actions[902] ||
                    wobraz1Rect.isPointInRect(pTemp) || wobraz2Rect.isPointInRect(pTemp) ||
                    stolcRect.isPointInRect(pTemp) || durjeRect.isPointInRect(pTemp)) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (linkerAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 4) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    cursorShape = 4;
                }
                return;
            }

            // sonst normal-Cursor
            if (cursorShape != 0) {
                mainFrame.setCursor(mainFrame.cursorNormal);
                cursorShape = 0;
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
            keyClear();
            nextActionID = 122;
            mainFrame.repaint();
            return;
        }

        // Save - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F2) {
            keyClear();
            nextActionID = 121;
            mainFrame.repaint();
            return;
        }

        // Load - Screen aktivieren
        if (Taste == GenericKeyEvent.VK_F3) {
            keyClear();
            nextActionID = 120;
            mainFrame.repaint();
        }
    }

    // Vor Key - Events alles deaktivieren
    private void keyClear() {
        outputText = "";
        resetAnims();
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.isClipSet = false;
        mainFrame.isBackgroundAnimRunning = false;
        mainFrame.krabat.stopWalking();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void doAction() {
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
            switchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 2:
                // Saeufer anschauen
                krabatSays("Hoscenc1_1", fSaeufer, 3, 0, 0);
                break;

            case 3:
                // Strolch anschauen
                krabatSays("Hoscenc1_2", fStrolch, 3, 0, 0);
                break;

            case 4:
                // Honck anschauen
                krabatSays("Hoscenc1_3", fHonck, 3, 0, 0);
                break;

            case 5:
                // Wobraz1 anschauen
                krabatSays("Hoscenc1_4", fWobraz2, 3, 0, 0);
                break;

            case 6:
                // Wobraz2 anschauen
                krabatSays("Hoscenc1_5", fWobraz1, 3, 0, 0);
                break;

            case 7:
                // Stolc anschauen
                krabatSays("Hoscenc1_6", fStolc, 3, 0, 0);
                break;

            case 8:
                // Durje anschauen
                krabatSays("Hoscenc1_7", fDurje, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Saeufer benutzen)
                mainFrame.krabat.setFacing(fSaeufer);
                mainFrame.isAnimRunning = true;
                AnimMCLocked = true;
                resetAnims();
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 600;
                break;

            case 51:
                // Krabat beginnt MC (Strolch benutzen)
                mainFrame.krabat.setFacing(fStrolch);
                mainFrame.isAnimRunning = true;
                AnimMCLocked = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 800;
                break;

            case 55:
                // Honck benutzen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 58;
                mainFrame.inventory.vInventory.addElement(4);
                mainFrame.krabat.setFacing(fHonck);
                mainFrame.krabat.nAnimation = 31;
                Counter = 5;
                break;

            case 58:
                // Ende Honcktakeanim
                if (--Counter == 1) {
                    // auf Gleichzeitigkeit Handwegnehmen und Gegenstand weg trimmen
                    mainFrame.actions[902] = true;
                    mainFrame.isClipSet = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                nextActionID = 0;
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 60:
                // Wobraz 1 mitnehmen
                krabatSays("Hoscenc1_8", fWobraz1, 3, 0, 0);
                break;

            case 62:
                // Wobraz 2 mitnehmen
                krabatSays("Hoscenc1_9", fWobraz2, 3, 0, 0);
                break;

            case 65:
                // Stolc mitnehmen
                krabatSays("Hoscenc1_10", fStolc, 3, 0, 0);
                break;

            case 70:
                // Durje mitnehmen
                krabatSays("Hoscenc1_11", fDurje, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Wjes
                createNewLocation(13, 24);
                break;

            case 150:
                // Wirt - Ausreden
                maleExcuse(fWirt);
                break;

            case 155:
                // Saeufer - Ausreden
                maleExcuse(fSaeufer);
                break;

            case 160:
                // Strolch - Ausreden
                maleExcuse(fStrolch);
                break;

            case 165:
                // Honck - Ausreden
                thingExcuse(fHonck);
                break;

            case 170:
                // Wobrazy 1 Ausreden
                thingExcuse(fWobraz1);
                break;

            case 172:
                // Wobrazy 2 Ausreden
                thingExcuse(fWobraz2);
                break;

            case 175:
                // Stolc - Ausreden
                thingExcuse(fStolc);
                break;

            case 180:
                // Durje - Ausreden
                thingExcuse(fDurje);
                break;

            case 200:
                // Honck z Blotom auf Wobraz 1
                krabatSays("Hoscenc1_12", fWobraz1, 3, 0, 0);
                break;

            case 202:
                // Honck z Blotom auf Wobraz 2
                krabatSays("Hoscenc1_13", fWobraz2, 3, 0, 0);
                break;

            case 210:
                // Pjero auf Pjany
                krabatSays("Hoscenc1_14", fSaeufer, 3, 0, 0);
                break;

            case 220:
                // Kij auf Pjany
                krabatSays("Hoscenc1_15", fSaeufer, 3, 0, 0);
                break;

            case 230:
                // Kij auf Dundak
                krabatSays("Hoscenc1_16", fStrolch, 3, 0, 0);
                break;

            case 240:
                // bron auf Dundak
                krabatSays("Hoscenc1_17", fStrolch, 3, 0, 0);
                break;

            // Dialog mit Saeufer (spaeter Wirt)

            case 600:
                // Multiple - Choice - Routine
                Dialog.initMC(20);
                // Test, ob Saeufer oder Wirt gefragt wird
                if (!mainFrame.actions[30]) {
                    // Dialog mit Saeufer...
                    Dialog.extend("Hoscenc1_54", 1000, 1000, null, 0);
                    Dialog.extend("Hoscenc1_55", 1000, 1000, null, 0);
                    Dialog.extend("Hoscenc1_56", 1000, 1000, null, 0);
                } else {
                    // Dialog mit Wirt
                    // 1. Frage
                    Dialog.extend("Hoscenc1_57", 1000, 1000, null, 610);

                    // 2. Frage
                    Dialog.extend("Hoscenc1_58", 1000, 32, new int[]{32}, 620);
                    Dialog.extend("Hoscenc1_59", 32, 33, new int[]{33}, 630);
                    Dialog.extend("Hoscenc1_60", 33, 31, new int[]{31, 39, 44}, 640);

                    // 3. Frage
                    Dialog.extend("Hoscenc1_61", 1000, 36, new int[]{36}, 650);
                    Dialog.extend("Hoscenc1_62", 36, 37, new int[]{37}, 660);
                    Dialog.extend("Hoscenc1_63", 37, 35, new int[]{35}, 670);

                    // 5. Frage (4. bedeutet Ende...)
                    Dialog.extend("Hoscenc1_64", 39, 40, new int[]{40}, 680);
                    Dialog.extend("Hoscenc1_65", 40, 41, new int[]{41}, 690);
                    Dialog.extend("Hoscenc1_66", 41, 42, new int[]{42}, 700);
                    Dialog.extend("Hoscenc1_67", 42, 43, new int[]{43}, 710);
                    Dialog.extend("Hoscenc1_68", 43, 1000, null, 720);

                    // 6. Frage
                    Dialog.extend("Hoscenc1_69", 44, 45, new int[]{45}, 730);
                    Dialog.extend("Hoscenc1_70", 45, 1000, null, 740);

                    // 4. Frage
                    Dialog.extend("Hoscenc1_71", 1000, 1000, null, 785);
                }

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
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                // Je nach ausgewaehlter Frage Action ausfuehren
                if (!mainFrame.actions[30]) {
                    // Saeufer aergern, bis Wirt kommt...
                    if (!mainFrame.actions[46]) {
                        mainFrame.actions[46] = true;
                        nextActionID = 750;
                    } else {
                        SoundCountdown = 100; // Strolch-Sound deaktivieren

                        mainFrame.actions[30] = true;
                        nextActionID = 760;
                    }
                } else {
                    nextActionID = Dialog.actionId;
                }
                break;

            case 610:
                // Reaktion Wirt auf 1. Frage
                personSays("Hoscenc1_18", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 620:
                // Reaktion Wirt auf 1. Teil 2. Frage
                personSays("Hoscenc1_19", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 630:
                // Reaktion Wirt auf 2. Teil 2. Frage
                personSays("Hoscenc1_20", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 640:
                // Reaktion Wirt auf 3. Teil 2. Frage
                personSays("Hoscenc1_21", 0, 25, 2, 641, wirt.evalTalkPoint());
                break;

            case 641:
                // Reaktion Wirt auf 3. Teil 2. Frage
                personSays("Hoscenc1_22", 0, 25, 2, 642, wirt.evalTalkPoint());
                break;

            case 642:
                // Reaktion Wirt auf 3. Teil 2. Frage
                personSays("Hoscenc1_23", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 650:
                // Reaktion Wirt auf 1. Teil 3. Frage
                personSays("Hoscenc1_24", 0, 25, 2, 651, wirt.evalTalkPoint());
                break;

            case 651:
                // Reaktion Wirt auf 1. Teil 3. Frage
                personSays("Hoscenc1_25", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 660:
                // Reaktion Wirt auf 2. Teil 3. Frage
                personSays("Hoscenc1_26", 0, 25, 2, 661, wirt.evalTalkPoint());
                break;

            case 661:
                // Reaktion Wirt auf 2. Teil 3. Frage
                personSays("Hoscenc1_27", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 670:
                // Reaktion Wirt auf 3. Teil 3. Frage
                personSays("Hoscenc1_28", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 680:
                // Reaktion Wirt auf 1. Teil 5. Frage
                personSays("Hoscenc1_29", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 690:
                // Reaktion Wirt auf 2. Teil 5. Frage
                personSays("Hoscenc1_30", 0, 25, 2, 691, wirt.evalTalkPoint());
                break;

            case 691:
                // Reaktion Wirt auf 2. Teil 5. Frage
                personSays("Hoscenc1_31", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 700:
                // Reaktion Wirt auf 3. Teil 5. Frage
                personSays("Hoscenc1_32", 0, 25, 2, 701, wirt.evalTalkPoint());
                break;

            case 701:
                // Reaktion Wirt auf 3. Teil 5. Frage
                personSays("Hoscenc1_33", 0, 25, 2, 702, wirt.evalTalkPoint());
                break;

            case 702:
                // Reaktion Wirt auf 3. Teil 5. Frage
                personSays("Hoscenc1_34", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 710:
                // Reaktion Wirt auf 4. Teil 5. Frage
                personSays("Hoscenc1_35", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 720:
                // Reaktion Wirt auf 5. Teil 5. Frage
                personSays("Hoscenc1_36", 0, 25, 2, 721, wirt.evalTalkPoint());
                break;

            case 721:
                // Reaktion Wirt auf 5. Teil 5. Frage
                personSays("Hoscenc1_37", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 730:
                // Reaktion Wirt auf 1. Teil 6. Frage
                personSays("Hoscenc1_38", 0, 25, 2, 731, wirt.evalTalkPoint());
                break;

            case 731:
                // Reaktion Wirt auf 1. Teil 6. Frage
                personSays("Hoscenc1_39", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 740:
                // Reaktion Wirt auf 2. Teil 6. Frage
                personSays("Hoscenc1_40", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 750:
                // Reaktion Saeufer auf 1. Krabat - Frage
                int random = (int) Math.round(Math.random() * (AP.length - 1));
                outputText = Start.STRING_MANAGER.getTranslation(AP[random]);
                outputTextPos = mainFrame.imageFont.centerText(outputText, SaeuferTalk);
                talkPerson = 23;
                talkPause = 2;
                nextActionID = 910;
                break;

            // Wirt-Anim

            case 760:
                // Wirt kommt gelaufen
                SoundCountdown = 30; // Strolch-Sound deaktivieren
                doorOpen = true;
                mainFrame.soundPlayer.playFile("sfx/hdurjeauf.wav");
                wirt.setPos(WirtOOben);
                wirt.setFacing(6);
                wirt.moveTo(WirtOben);
                walkReady = false;
                showKorcmar = true;
                nextActionID = 770;
                break;

            case 770:
                // warten bis ausgelaufen
                if (walkReady) {
                    nextActionID = 773;
                }
                break;

            case 773:
                // wirt kommt weitergelaufen
                wirt.moveTo(WirtUnten);
                walkReady = false;
                nextActionID = 777;
                break;

            case 777:
                // warten bis ausgelaufen
                if (walkReady) {
                    nextActionID = 780;
                }
                break;

            case 780:
                // Reaktion Wirt wenn Saeufer von Krabat 2. Mal gefragt
                personSays("Hoscenc1_41", fWirt, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 785:
                // Wirt geht zurueck
                wirt.moveTo(WirtOben);
                walkReady = false;
                nextActionID = 790;
                break;

            case 790:
                // warten bis ausgelaufen
                if (walkReady) {
                    nextActionID = 791;
                }
                break;

            case 791:
                // wirt geht weiter weg
                SoundCountdown = 100; // Strolch-Sound deaktivieren
                wirt.moveTo(WirtOOben);
                walkReady = false;
                nextActionID = 793;
                break;

            case 793:
                // warten bis ausgelaufen
                if (walkReady) {
                    nextActionID = 795;
                }
                break;

            case 795:
                // Wirt weg
                doorOpen = false;
                mainFrame.soundPlayer.playFile("sfx/hdurjezu.wav");
                showKorcmar = false;
                mainFrame.isClipSet = false;
                nextActionID = 900;
                break;

            // Gespraech mit Strolch

            case 800:
                // Multiple - Choice - Routine Dundak
                Dialog.initMC(20);
                // 1. Frage
                Dialog.extend("Hoscenc1_72", 1000, 50, new int[]{50, 51, 55}, 810);

                // 2. Frage
                Dialog.extend("Hoscenc1_73", 1000, 1000, null, 820);

                // 4. Frage
                Dialog.extend("Hoscenc1_74", 51, 52, new int[]{52}, 830);
                Dialog.extend("Hoscenc1_75", 52, 53, new int[]{53}, 840);

                // 5. Frage
                Dialog.extend("Hoscenc1_76", 55, 56, new int[]{56}, 860);
                Dialog.extend("Hoscenc1_77", 56, 57, new int[]{57}, 870);

                // 3. Frage (bedeutet Ende)
                Dialog.extend("Hoscenc1_78", 1000, 1000, null, 900);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 801;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 801:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                outputText = Dialog.questions[Dialog.answer];
                outputTextPos = mainFrame.imageFont.krabatText(outputText);
                talkPerson = 1;
                talkPause = 2;

                nextActionID = Dialog.actionId;

                break;

            case 810:
                // Reaktion Strolch auf 1. Frage
                personSays("Hoscenc1_42", 0, 24, 2, 800, StrolchTalk);
                break;

            case 820:
                // Reaktion Strolch auf 2. Frage
                personSays("Hoscenc1_43", 0, 24, 2, 800, StrolchTalk);
                break;

            case 830:
                // Reaktion Strolch auf 1. Teil 4. Frage
                personSays("Hoscenc1_44", 0, 24, 2, 831, StrolchTalk);
                break;

            case 831:
                // Reaktion Strolch auf 1. Teil 4. Frage
                personSays("Hoscenc1_45", 0, 24, 2, 800, StrolchTalk);
                break;

            case 840:
                // Reaktion Strolch auf 2. Teil 4. Frage
                personSays("Hoscenc1_46", 0, 24, 2, 841, StrolchTalk);
                break;

            case 841:
                // Reaktion Strolch auf 2. Teil 4. Frage
                personSays("Hoscenc1_47", 0, 24, 2, 842, StrolchTalk);
                break;

            case 842:
                // Reaktion Strolch auf 2. Teil 4. Frage
                personSays("Hoscenc1_48", 0, 24, 2, 845, StrolchTalk);
                break;

            case 845:
                // Krabat sagt Spruch
                krabatSays("Hoscenc1_49", 0, 1, 2, 850);
                break;

            case 850:
                // Reaktion Strolch auf 3. Teil 4. Frage
                personSays("Hoscenc1_50", 0, 24, 2, 800, StrolchTalk);
                break;

            case 860:
                // Reaktion Strolch auf 1. Teil 5. Frage
                personSays("Hoscenc1_51", 0, 24, 2, 861, StrolchTalk);
                break;

            case 861:
                // Reaktion Strolch auf 1. Teil 5. Frage
                personSays("Hoscenc1_52", 0, 24, 2, 800, StrolchTalk);
                break;

            case 870:
                // Reaktion Strolch auf 2. Teil 5. Frage
                personSays("Hoscenc1_53", 0, 24, 2, 800, StrolchTalk);
                break;

            case 900:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[30] = false; // Saeufercount zuruecksetzen
                mainFrame.actions[46] = false; // Saeufercount zuruecksetzen
                mainFrame.isAnimRunning = false;
                AnimMCLocked = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            case 910:
                // MC beenden, hier ohne Reset des Saeufercounts, wenn noch kein Erfolg beim Aergern
                mainFrame.isAnimRunning = false;
                AnimMCLocked = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    private void doAnims() {
        switch (AnimID) {
            case 1:
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 2;
                }
                break;

            case 2:
                AnimOutputText = "";
                mainFrame.isClipSet = false;
                AnimTalkPerson = 0;
                AnimCounter = 50 + (int) Math.round(Math.random() * 40);
                AnimID = 3;
                break;

            case 3:
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 4;
                }
                break;

            case 4:
                // Saeufer - Schnarchen
                int random = (int) Math.round(Math.random() * (AP.length - 1));
                AnimOutputText = Start.STRING_MANAGER.getTranslation(AP[random]);
                AnimOutputTextPos = mainFrame.imageFont.centerAnimText(AnimOutputText, SaeuferTalk);
                AnimCounter = 30;
                AnimTalkPerson = 23;
                AnimID = 1;
                break;

        }

    }

    // setzt Anim so zurueck, dass beim ersten Aufruf nix auf dem Bildschirm steht
    private void resetAnims() {
        AnimOutputText = "";
        AnimCounter = 300;
        AnimID = 1;
        AnimTalkPerson = 0;
    }

}