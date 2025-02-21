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
import de.codengine.krabat.anims.Dundak;
import de.codengine.krabat.anims.Korcmar;
import de.codengine.krabat.anims.Pjany;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Hoscenc1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Hoscenc1.class);
    private GenericImage background;
    private GenericImage hosc6;
    private GenericImage honck;
    private GenericImage durje;
    private GenericImage vorderdurje;
    private Korcmar wirt;
    private Dundak strolch;
    private Pjany saeufer;
    private final Multiple2 Dialog;

    private final Borderrect brStrolch;
    private final Borderrect brSaeufer;
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
    private static final String[] APH = {Start.stringManager.getTranslation("Loc1_Hoscenc1_00000"), Start.stringManager.getTranslation("Loc1_Hoscenc1_00001"), Start.stringManager.getTranslation("Loc1_Hoscenc1_00002")};
    private static final String[] APD = {Start.stringManager.getTranslation("Loc1_Hoscenc1_00003"), Start.stringManager.getTranslation("Loc1_Hoscenc1_00004"), Start.stringManager.getTranslation("Loc1_Hoscenc1_00005")};
    private static final String[] APN = {Start.stringManager.getTranslation("Loc1_Hoscenc1_00006"), Start.stringManager.getTranslation("Loc1_Hoscenc1_00007"), Start.stringManager.getTranslation("Loc1_Hoscenc1_00008")};

    // Konstanten - Rects
    private static final Borderrect linkerAusgang = new Borderrect(0, 380, 74, 479);
    private static final Borderrect hosc6Rect = new Borderrect(0, 85, 360, 479);
    private static final Borderrect honckRect = new Borderrect(235, 271, 267, 314);
    private static final Borderrect wobraz1Rect = new Borderrect(158, 166, 236, 215);
    private static final Borderrect wobraz2Rect = new Borderrect(271, 118, 343, 166);
    private static final Borderrect stolcRect = new Borderrect(553, 398, 625, 444);
    private static final Borderrect durjeRect = new Borderrect(394, 108, 460, 259);

    // Konstante Punkte
    private static final GenericPoint Psaeufer = new GenericPoint(436, 380);
    private static final GenericPoint Pstrolch = new GenericPoint(444, 434);
    // private static final GenericPoint wirtFeet    = new GenericPoint (320, 464);
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
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        // hier zufaellig Lied auswaehlen
        int zf = (int) (Math.random() * 50);
        if (zf > 25) {
            BackgroundMusicPlayer.getInstance().playTrack(5, true);
        } else {
            BackgroundMusicPlayer.getInstance().playTrack(19, true);
        }

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 10.95f;
        mainFrame.krabat.defScale = -70;

        wirt = new Korcmar(mainFrame);
        wirt.maxx = 0;
        wirt.zoomf = 10.95f;
        wirt.defScale = 0;
        wirt.setPos(new GenericPoint(300, 300));
        wirt.SetFacing(6);

        strolch = new Dundak(mainFrame);

        strolchPoint = new GenericPoint(529, 350);

        StrolchTalk = new GenericPoint();
        StrolchTalk.x = strolchPoint.x + Dundak.Breite / 2;
        StrolchTalk.y = strolchPoint.y - 50;

        brStrolch = new Borderrect(strolchPoint.x, strolchPoint.y, strolchPoint.x + Dundak.Breite, strolchPoint.y + Dundak.Hoehe);

        saeufer = new Pjany(mainFrame);

        saeuferPoint = new GenericPoint(265, 262);

        SaeuferTalk = new GenericPoint();
        SaeuferTalk.x = saeuferPoint.x + Pjany.Breite / 2;
        SaeuferTalk.y = saeuferPoint.y - 78;  // war 50, hat Strolchtext gestoert

        brSaeufer = new Borderrect(saeuferPoint.x, saeuferPoint.y, saeuferPoint.x + Pjany.Breite, saeuferPoint.y + Pjany.Hoehe);

        Dialog = new Multiple2(mainFrame);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(447, 448, 441, 495, 283, 399));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(441, 495, 370, 450, 400, 425));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 460, 0, 362, 426, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(52, 220, 15, 260, 384, 425));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(185, 185, 52, 221, 340, 383));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 13:
                // von Wjes aus
                initSound = true; // nur hier auch wirklich abspielen, wenn man reinkommt...
                mainFrame.krabat.setPos(new GenericPoint(35, 454));
                mainFrame.krabat.SetFacing(3);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/hoscenc/hosc.gif");
        hosc6 = getPicture("gfx/hoscenc/hosc6.gif");
        honck = getPicture("gfx/hoscenc/hosc7.gif");
        durje = getPicture("gfx/hoscenc/hdurje.gif");
        vorderdurje = getPicture("gfx/hoscenc/hdurje2.gif");

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

        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen
        /*if ((mainFrame.isMultiple == true) && (mainFrame.Clipset == true))
          {
          Dialog.paintMultiple (g);
          return;
          } */

        // Honck wurde aufgehoben!!!!!!!!!
        /*if (mainFrame.krabat.fAnimHelper == true)
          {
          mainFrame.inventory.vInventory.addElement (new Integer (4));
          mainFrame.Clipset = false; 
          mainFrame.krabat.fAnimHelper = false;
          mainFrame.Actions [902] = true;
          }*/

        if (initSound) {
            initSound = false;
            mainFrame.wave.PlayFile("sfx/wdurjezu.wav");
        }

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
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
            Borderrect temp = wirt.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
            if (doorOpen) {
                g.drawImage(durje, 390, 105);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Honck zeichnen, da im Hintergrund !!!
        if (!mainFrame.Actions[902]) {
            g.setClip(219, 271, 49, 44);
            g.drawImage(honck, 219, 271);
        }

        // Andere Personen zeichnen
        // Pjany
        g.setClip(saeuferPoint.x, saeuferPoint.y, Pjany.Breite, Pjany.Hoehe);
        g.drawImage(background, 0, 0);
        // Saeufer darf nur reden, wenn die Anims nicht gesperrt sind
        saeufer.drawPjany(g, saeuferPoint);

        // Dundak
        g.setClip(strolchPoint.x, strolchPoint.y, Dundak.Breite, Dundak.Hoehe);
        g.drawImage(background, 0, 0);
        strolch.drawDundak(g, TalkPerson, strolchPoint, SoundCountdown);
        if (SoundCountdown > 0) {
            SoundCountdown--;
        }

        // Korcmar bewegen
        if (showKorcmar && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = wirt.Move();
        }

        // Wirt zeichnen
        if (showKorcmar) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = wirt.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Wirt neu
            if (TalkPerson == 25 && mainFrame.talkCount > 1) {
                wirt.talkKorcmar(g);
            } else {
                wirt.drawKorcmar(g);
            }

            // Vordergrund draufzeichnen
            g.drawImage(vorderdurje, 294, 63);
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

        // hinterm Balken (nur Clipping - Region wird neugezeichnet)
        if (hosc6Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(hosc6, 51, 185);
        }

        // Ausgabe von AnimText, falls noetig
        if (!Objects.equals(AnimOutputText, "") && !AnimMCLocked) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
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

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Die Anims muessen bedient werden
        if (AnimID != 0 && !AnimMCLocked) {
            DoAnims();
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
        if (mainFrame.isMultiple) {
            Dialog.evalMouseEvent(e);
            return;
        }

        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // linker Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Saeufer
                if (brSaeufer.IsPointInRect(pTemp)) {
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
                if (brStrolch.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = Pstrolch;
                }

                // Ausreden fuer Wobraz1
                if (wobraz1Rect.IsPointInRect(pTemp)) {
                    // honck z blotom
                    nextActionID = mainFrame.whatItem == 16 ? 202 : 172;
                    pTemp = Pwobraz1;
                }

                // Ausreden fuer Wobraz2
                if (wobraz2Rect.IsPointInRect(pTemp)) {
                    // honck z blotom
                    nextActionID = mainFrame.whatItem == 16 ? 200 : 170;
                    pTemp = Pwobraz2;
                }

                // Ausreden fuer Stolc
                if (stolcRect.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 175;
                    pTemp = Pstolc;
                }

                // Ausreden fuer Durje
                if (durjeRect.IsPointInRect(pTemp)) {
                    // Extra - Sinnloszeug
                    nextActionID = 180;
                    pTemp = Pdurje;
                }

                // Ausreden fuer Honck
                if (honckRect.IsPointInRect(pTemp) && !mainFrame.Actions[902]) {
                    // Extra - Sinnloszeug
                    nextActionID = 165;
                    pTemp = Phonck;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // grundsaetzlich Gegenstand wieder ablegen
                mainFrame.invCursor = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
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

                // Saeufer ansehen
                if (brSaeufer.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Psaeufer;
                }

                // Strolch ansehen
                if (brStrolch.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Pstrolch;
                }

                // Honck ansehen
                if (honckRect.IsPointInRect(pTemp) && !mainFrame.Actions[902]) {
                    nextActionID = 4;
                    pTemp = Phonck;
                }

                // zu Wjes gehen ?
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // wobraz1 ansehen
                if (wobraz1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pwobraz1;
                }

                // wobraz2 ansehen
                if (wobraz2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Pwobraz2;
                }

                // stolc ansehen
                if (stolcRect.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = Pstolc;
                }

                // durje ansehen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTemp = Pdurje;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Ausgang zu Wjes abfangen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Saeufer reden
                if (brSaeufer.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Psaeufer);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Strolch reden
                if (brStrolch.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pstrolch);
                    mainFrame.repaint();
                    return;
                }

                // Honck nehmen
                if (honckRect.IsPointInRect(pTemp) && !mainFrame.Actions[902]) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Phonck);
                    mainFrame.repaint();
                    return;
                }

                // Wobraz1 nehmen
                if (wobraz1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 62;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwobraz1);
                    mainFrame.repaint();
                    return;
                }

                // Wobraz2 nehmen
                if (wobraz2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwobraz2);
                    mainFrame.repaint();
                    return;
                }

                // Stolc nehmen
                if (stolcRect.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pstolc);
                    mainFrame.repaint();
                    return;
                }

                // Durje nehmen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurje);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                ResetAnims();
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.fPlayAnim || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    brSaeufer.IsPointInRect(pTemp) || brStrolch.IsPointInRect(pTemp) ||
                    honckRect.IsPointInRect(pTemp) && !mainFrame.Actions[902] ||
                    wobraz1Rect.IsPointInRect(pTemp) || wobraz2Rect.IsPointInRect(pTemp) ||
                    stolcRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp);

            if (Cursorform != 10 && !mainFrame.invHighCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if (Cursorform != 11 && mainFrame.invHighCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }


        // normaler Cursor, normale Reaktion
        else {
            if (brSaeufer.IsPointInRect(pTemp) ||
                    brStrolch.IsPointInRect(pTemp) || honckRect.IsPointInRect(pTemp) &&
                    !mainFrame.Actions[902] ||
                    wobraz1Rect.IsPointInRect(pTemp) || wobraz2Rect.IsPointInRect(pTemp) ||
                    stolcRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 4;
                }
                return;
            }

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
        // Bei Multiple Choice eigene Keyroutine
        if (mainFrame.isMultiple) {
            return;
        }

        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim) {
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
        ResetAnims();
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
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

            evalMouseMoveEvent(mainFrame.Mousepoint);

            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if (nextActionID > 119 && nextActionID < 129) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 2:
                // Saeufer anschauen
                KrabatSagt("Hoscenc1_1", fSaeufer, 3, 0, 0);
                break;

            case 3:
                // Strolch anschauen
                KrabatSagt("Hoscenc1_2", fStrolch, 3, 0, 0);
                break;

            case 4:
                // Honck anschauen
                KrabatSagt("Hoscenc1_3", fHonck, 3, 0, 0);
                break;

            case 5:
                // Wobraz1 anschauen
                KrabatSagt("Hoscenc1_4", fWobraz2, 3, 0, 0);
                break;

            case 6:
                // Wobraz2 anschauen
                KrabatSagt("Hoscenc1_5", fWobraz1, 3, 0, 0);
                break;

            case 7:
                // Stolc anschauen
                KrabatSagt("Hoscenc1_6", fStolc, 3, 0, 0);
                break;

            case 8:
                // Durje anschauen
                KrabatSagt("Hoscenc1_7", fDurje, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Saeufer benutzen)
                mainFrame.krabat.SetFacing(fSaeufer);
                mainFrame.fPlayAnim = true;
                AnimMCLocked = true;
                ResetAnims();
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 600;
                break;

            case 51:
                // Krabat beginnt MC (Strolch benutzen)
                mainFrame.krabat.SetFacing(fStrolch);
                mainFrame.fPlayAnim = true;
                AnimMCLocked = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 800;
                break;

            case 55:
                // Honck benutzen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 58;
                mainFrame.inventory.vInventory.addElement(4);
                mainFrame.krabat.SetFacing(fHonck);
                mainFrame.krabat.nAnimation = 31;
                Counter = 5;
                break;

            case 58:
                // Ende Honcktakeanim
                if (--Counter == 1) {
                    // auf Gleichzeitigkeit Handwegnehmen und Gegenstand weg trimmen
                    mainFrame.Actions[902] = true;
                    mainFrame.Clipset = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                nextActionID = 0;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 60:
                // Wobraz 1 mitnehmen
                KrabatSagt("Hoscenc1_8", fWobraz1, 3, 0, 0);
                break;

            case 62:
                // Wobraz 2 mitnehmen
                KrabatSagt("Hoscenc1_9", fWobraz2, 3, 0, 0);
                break;

            case 65:
                // Stolc mitnehmen
                KrabatSagt("Hoscenc1_10", fStolc, 3, 0, 0);
                break;

            case 70:
                // Durje mitnehmen
                KrabatSagt("Hoscenc1_11", fDurje, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Wjes
                NeuesBild(13, 24);
                break;

            case 150:
                // Wirt - Ausreden
                MPersonAusrede(fWirt);
                break;

            case 155:
                // Saeufer - Ausreden
                MPersonAusrede(fSaeufer);
                break;

            case 160:
                // Strolch - Ausreden
                MPersonAusrede(fStrolch);
                break;

            case 165:
                // Honck - Ausreden
                DingAusrede(fHonck);
                break;

            case 170:
                // Wobrazy 1 Ausreden
                DingAusrede(fWobraz1);
                break;

            case 172:
                // Wobrazy 2 Ausreden
                DingAusrede(fWobraz2);
                break;

            case 175:
                // Stolc - Ausreden
                DingAusrede(fStolc);
                break;

            case 180:
                // Durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 200:
                // Honck z Blotom auf Wobraz 1
                KrabatSagt("Hoscenc1_12", fWobraz1, 3, 0, 0);
                break;

            case 202:
                // Honck z Blotom auf Wobraz 2
                KrabatSagt("Hoscenc1_13", fWobraz2, 3, 0, 0);
                break;

            case 210:
                // Pjero auf Pjany
                KrabatSagt("Hoscenc1_14", fSaeufer, 3, 0, 0);
                break;

            case 220:
                // Kij auf Pjany
                KrabatSagt("Hoscenc1_15", fSaeufer, 3, 0, 0);
                break;

            case 230:
                // Kij auf Dundak
                KrabatSagt("Hoscenc1_16", fStrolch, 3, 0, 0);
                break;

            case 240:
                // bron auf Dundak
                KrabatSagt("Hoscenc1_17", fStrolch, 3, 0, 0);
                break;

            // Dialog mit Saeufer (spaeter Wirt)

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                // Test, ob Saeufer oder Wirt gefragt wird
                if (!mainFrame.Actions[30]) {
                    // Dialog mit Saeufer...
                    Dialog.ExtendMCO("Hoscenc1_54", 1000, 1000, null, 0);
                    Dialog.ExtendMCO("Hoscenc1_55", 1000, 1000, null, 0);
                    Dialog.ExtendMCO("Hoscenc1_56", 1000, 1000, null, 0);
                } else {
                    // Dialog mit Wirt
                    // 1. Frage
                    Dialog.ExtendMCO("Hoscenc1_57", 1000, 1000, null, 610);

                    // 2. Frage
                    Dialog.ExtendMCO("Hoscenc1_58", 1000, 32, new int[]{32}, 620);
                    Dialog.ExtendMCO("Hoscenc1_59", 32, 33, new int[]{33}, 630);
                    Dialog.ExtendMCO("Hoscenc1_60", 33, 31, new int[]{31, 39, 44}, 640);

                    // 3. Frage
                    Dialog.ExtendMCO("Hoscenc1_61", 1000, 36, new int[]{36}, 650);
                    Dialog.ExtendMCO("Hoscenc1_62", 36, 37, new int[]{37}, 660);
                    Dialog.ExtendMCO("Hoscenc1_63", 37, 35, new int[]{35}, 670);

                    // 5. Frage (4. bedeutet Ende...)
                    Dialog.ExtendMCO("Hoscenc1_64", 39, 40, new int[]{40}, 680);
                    Dialog.ExtendMCO("Hoscenc1_65", 40, 41, new int[]{41}, 690);
                    Dialog.ExtendMCO("Hoscenc1_66", 41, 42, new int[]{42}, 700);
                    Dialog.ExtendMCO("Hoscenc1_67", 42, 43, new int[]{43}, 710);
                    Dialog.ExtendMCO("Hoscenc1_68", 43, 1000, null, 720);

                    // 6. Frage
                    Dialog.ExtendMCO("Hoscenc1_69", 44, 45, new int[]{45}, 730);
                    Dialog.ExtendMCO("Hoscenc1_70", 45, 1000, null, 740);

                    // 4. Frage
                    Dialog.ExtendMCO("Hoscenc1_71", 1000, 1000, null, 785);
                }

                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                // Je nach ausgewaehlter Frage Action ausfuehren
                if (!mainFrame.Actions[30]) {
                    // Saeufer aergern, bis Wirt kommt...
                    if (!mainFrame.Actions[46]) {
                        mainFrame.Actions[46] = true;
                        nextActionID = 750;
                    } else {
                        SoundCountdown = 100; // Strolch-Sound deaktivieren

                        mainFrame.Actions[30] = true;
                        nextActionID = 760;
                    }
                } else {
                    nextActionID = Dialog.ActionID;
                }
                break;

            case 610:
                // Reaktion Wirt auf 1. Frage
                PersonSagt("Hoscenc1_18", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 620:
                // Reaktion Wirt auf 1. Teil 2. Frage
                PersonSagt("Hoscenc1_19", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 630:
                // Reaktion Wirt auf 2. Teil 2. Frage
                PersonSagt("Hoscenc1_20", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 640:
                // Reaktion Wirt auf 3. Teil 2. Frage
                PersonSagt("Hoscenc1_21", 0, 25, 2, 641, wirt.evalTalkPoint());
                break;

            case 641:
                // Reaktion Wirt auf 3. Teil 2. Frage
                PersonSagt("Hoscenc1_22", 0, 25, 2, 642, wirt.evalTalkPoint());
                break;

            case 642:
                // Reaktion Wirt auf 3. Teil 2. Frage
                PersonSagt("Hoscenc1_23", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 650:
                // Reaktion Wirt auf 1. Teil 3. Frage
                PersonSagt("Hoscenc1_24", 0, 25, 2, 651, wirt.evalTalkPoint());
                break;

            case 651:
                // Reaktion Wirt auf 1. Teil 3. Frage
                PersonSagt("Hoscenc1_25", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 660:
                // Reaktion Wirt auf 2. Teil 3. Frage
                PersonSagt("Hoscenc1_26", 0, 25, 2, 661, wirt.evalTalkPoint());
                break;

            case 661:
                // Reaktion Wirt auf 2. Teil 3. Frage
                PersonSagt("Hoscenc1_27", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 670:
                // Reaktion Wirt auf 3. Teil 3. Frage
                PersonSagt("Hoscenc1_28", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 680:
                // Reaktion Wirt auf 1. Teil 5. Frage
                PersonSagt("Hoscenc1_29", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 690:
                // Reaktion Wirt auf 2. Teil 5. Frage
                PersonSagt("Hoscenc1_30", 0, 25, 2, 691, wirt.evalTalkPoint());
                break;

            case 691:
                // Reaktion Wirt auf 2. Teil 5. Frage
                PersonSagt("Hoscenc1_31", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 700:
                // Reaktion Wirt auf 3. Teil 5. Frage
                PersonSagt("Hoscenc1_32", 0, 25, 2, 701, wirt.evalTalkPoint());
                break;

            case 701:
                // Reaktion Wirt auf 3. Teil 5. Frage
                PersonSagt("Hoscenc1_33", 0, 25, 2, 702, wirt.evalTalkPoint());
                break;

            case 702:
                // Reaktion Wirt auf 3. Teil 5. Frage
                PersonSagt("Hoscenc1_34", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 710:
                // Reaktion Wirt auf 4. Teil 5. Frage
                PersonSagt("Hoscenc1_35", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 720:
                // Reaktion Wirt auf 5. Teil 5. Frage
                PersonSagt("Hoscenc1_36", 0, 25, 2, 721, wirt.evalTalkPoint());
                break;

            case 721:
                // Reaktion Wirt auf 5. Teil 5. Frage
                PersonSagt("Hoscenc1_37", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 730:
                // Reaktion Wirt auf 1. Teil 6. Frage
                PersonSagt("Hoscenc1_38", 0, 25, 2, 731, wirt.evalTalkPoint());
                break;

            case 731:
                // Reaktion Wirt auf 1. Teil 6. Frage
                PersonSagt("Hoscenc1_39", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 740:
                // Reaktion Wirt auf 2. Teil 6. Frage
                PersonSagt("Hoscenc1_40", 0, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 750:
                // Reaktion Saeufer auf 1. Krabat - Frage
                if (mainFrame.sprache == 1) {
                    outputText = APH[(int) Math.round(Math.random() * 2)];
                }
                if (mainFrame.sprache == 2) {
                    outputText = APD[(int) Math.round(Math.random() * 2)];
                }
                if (mainFrame.sprache == 3) {
                    outputText = APN[(int) Math.round(Math.random() * 2)];
                }
                outputTextPos = mainFrame.ifont.CenterText(outputText, SaeuferTalk);
                TalkPerson = 23;
                TalkPause = 2;
                nextActionID = 910;
                break;

            // Wirt-Anim

            case 760:
                // Wirt kommt gelaufen
                SoundCountdown = 30; // Strolch-Sound deaktivieren
                doorOpen = true;
                mainFrame.wave.PlayFile("sfx/hdurjeauf.wav");
                wirt.setPos(WirtOOben);
                wirt.SetFacing(6);
                wirt.MoveTo(WirtOben);
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
                wirt.MoveTo(WirtUnten);
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
                PersonSagt("Hoscenc1_41", fWirt, 25, 2, 600, wirt.evalTalkPoint());
                break;

            case 785:
                // Wirt geht zurueck
                wirt.MoveTo(WirtOben);
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
                wirt.MoveTo(WirtOOben);
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
                mainFrame.wave.PlayFile("sfx/hdurjezu.wav");
                showKorcmar = false;
                mainFrame.Clipset = false;
                nextActionID = 900;
                break;

            // Gespraech mit Strolch

            case 800:
                // Multiple - Choice - Routine Dundak
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMCO("Hoscenc1_72", 1000, 50, new int[]{50, 51, 55}, 810);

                // 2. Frage
                Dialog.ExtendMCO("Hoscenc1_73", 1000, 1000, null, 820);

                // 4. Frage
                Dialog.ExtendMCO("Hoscenc1_74", 51, 52, new int[]{52}, 830);
                Dialog.ExtendMCO("Hoscenc1_75", 52, 53, new int[]{53}, 840);

                // 5. Frage
                Dialog.ExtendMCO("Hoscenc1_76", 55, 56, new int[]{56}, 860);
                Dialog.ExtendMCO("Hoscenc1_77", 56, 57, new int[]{57}, 870);

                // 3. Frage (bedeutet Ende)
                Dialog.ExtendMCO("Hoscenc1_78", 1000, 1000, null, 900);

                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 801;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 801:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 810:
                // Reaktion Strolch auf 1. Frage
                PersonSagt("Hoscenc1_42", 0, 24, 2, 800, StrolchTalk);
                break;

            case 820:
                // Reaktion Strolch auf 2. Frage
                PersonSagt("Hoscenc1_43", 0, 24, 2, 800, StrolchTalk);
                break;

            case 830:
                // Reaktion Strolch auf 1. Teil 4. Frage
                PersonSagt("Hoscenc1_44", 0, 24, 2, 831, StrolchTalk);
                break;

            case 831:
                // Reaktion Strolch auf 1. Teil 4. Frage
                PersonSagt("Hoscenc1_45", 0, 24, 2, 800, StrolchTalk);
                break;

            case 840:
                // Reaktion Strolch auf 2. Teil 4. Frage
                PersonSagt("Hoscenc1_46", 0, 24, 2, 841, StrolchTalk);
                break;

            case 841:
                // Reaktion Strolch auf 2. Teil 4. Frage
                PersonSagt("Hoscenc1_47", 0, 24, 2, 842, StrolchTalk);
                break;

            case 842:
                // Reaktion Strolch auf 2. Teil 4. Frage
                PersonSagt("Hoscenc1_48", 0, 24, 2, 845, StrolchTalk);
                break;

            case 845:
                // Krabat sagt Spruch
                KrabatSagt("Hoscenc1_49", 0, 1, 2, 850);
                break;

            case 850:
                // Reaktion Strolch auf 3. Teil 4. Frage
                PersonSagt("Hoscenc1_50", 0, 24, 2, 800, StrolchTalk);
                break;

            case 860:
                // Reaktion Strolch auf 1. Teil 5. Frage
                PersonSagt("Hoscenc1_51", 0, 24, 2, 861, StrolchTalk);
                break;

            case 861:
                // Reaktion Strolch auf 1. Teil 5. Frage
                PersonSagt("Hoscenc1_52", 0, 24, 2, 800, StrolchTalk);
                break;

            case 870:
                // Reaktion Strolch auf 2. Teil 5. Frage
                PersonSagt("Hoscenc1_53", 0, 24, 2, 800, StrolchTalk);
                break;

            case 900:
                // MC beenden, wenn zuende gelabert...
                mainFrame.Actions[30] = false; // Saeufercount zuruecksetzen
                mainFrame.Actions[46] = false; // Saeufercount zuruecksetzen
                mainFrame.fPlayAnim = false;
                AnimMCLocked = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 910:
                // MC beenden, hier ohne Reset des Saeufercounts, wenn noch kein Erfolg beim Aergern
                mainFrame.fPlayAnim = false;
                AnimMCLocked = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    private void DoAnims() {
        switch (AnimID) {
            case 1:
                AnimCounter--;
                if (AnimCounter < 1) {
                    AnimID = 2;
                }
                break;

            case 2:
                AnimOutputText = "";
                mainFrame.Clipset = false;
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
                if (mainFrame.sprache == 1) {
                    AnimOutputText = APH[(int) Math.round(Math.random() * 2)];
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = APD[(int) Math.round(Math.random() * 2)];
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = APN[(int) Math.round(Math.random() * 2)];
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, SaeuferTalk);
                AnimCounter = 30;
                AnimTalkPerson = 23;
                AnimID = 1;
                break;

        }

    }

    // setzt Anim so zurueck, dass beim ersten Aufruf nix auf dem Bildschirm steht
    private void ResetAnims() {
        AnimOutputText = "";
        AnimCounter = 300;
        AnimID = 1;
        AnimTalkPerson = 0;
    }

}