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
import de.codengine.krabat.anims.DDKowar;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Terassa extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Terassa.class);
    private GenericImage background;
    private GenericImage gelaender;
    private GenericImage busch;
    private GenericImage hammer;
    private GenericImage gelaender2;
    private GenericImage delle;
    private final DDKowar schmied;

    // private borderrect schmiedRect;
    private final Borderrect schmiedClickRect;
    private final GenericPoint schmiedPoint;
    private final GenericPoint schmiedTalk;
    private final boolean schmiedVisible;
    private boolean schmiedhoertzu = false;
    private boolean schnauzeSchmied = false;
    // Schmied ist da, wenn 529 = true und solange 701 = false, aber nur
    // bei Neueintritt in Location testen

    private boolean isVordergrund;

    // Eingrenzung, ob K nach vorn will, wenn er hinten steht
    private static final Bordertrapez[] vorWennHinten =
            {new Bordertrapez(0, 1, 0, 330, 408, 479),
                    new Bordertrapez(43, 334, 132, 430)};

    // Eingrenzung, ob K vorn bleiben will, wenn er vorn steht
    private static final Bordertrapez[] vorWennVor =
            {new Bordertrapez(0, 408, 41, 479),
                    new Bordertrapez(42, 129, 42, 639, 335, 446),
                    new Bordertrapez(42, 447, 639, 479)};

    // Merkvariablen fuer Zielpunkt und ZielActionID, wenn Ebene wechselt
    private int MerkActionID;
    private GenericPoint MerkPunkt;

    // Konstanten - Rects
    private static final Borderrect ausgangMurja
            = new Borderrect(491, 252, 560, 330);
    private static final Borderrect ausgangStraza
            = new Borderrect(171, 313, 215, 360);
    private static final Borderrect ausgangCychi
            = new Borderrect(0, 300, 40, 380);
    private static final Borderrect ausgangKarta
            = new Borderrect(600, 385, 639, 460);
    private static final Borderrect gelaenderRect  // fuer Vordergrund
            = new Borderrect(-400, 326, 639, 800);
    private static final Borderrect buschRect      // fuer Vordergrund
            = new Borderrect(498, 283, 639, 374);
    private static final Borderrect hammerRect
            = new Borderrect(221, 463, 248, 479);

    // Konstante Points
    private static final GenericPoint pExitMurja = new GenericPoint(515, 337);
    private static final GenericPoint pExitStraza = new GenericPoint(193, 311);  // damit K von vorn immer nach hinten geht, bevor exit
    private static final GenericPoint pExitCychi = new GenericPoint(10, 375);
    private static final GenericPoint pExitKarta = new GenericPoint(630, 440);
    private static final GenericPoint pSchmied = new GenericPoint(199, 479);
    private static final GenericPoint schmiedFeet = new GenericPoint(340, 491);
    private static final GenericPoint pHammer = new GenericPoint(188, 479);
    private static final GenericPoint pToHammer = new GenericPoint(199, 479);

    private static final GenericPoint vorUmziehPoint = new GenericPoint(549, 339);
    private static final GenericPoint UmziehPoint = new GenericPoint(620, 339);
    private static final GenericPoint nachUmziehPoint = new GenericPoint(500, 339);

    private static final GenericPoint walktoUnten = new GenericPoint(20, 393);
    private static final GenericPoint walktoTreppe = new GenericPoint(-30, 430);
    private static final GenericPoint walktoTreppeOben = new GenericPoint(-60, 393);
    private static final GenericPoint walktoOben = new GenericPoint(27, 450);

    private static final int UNTEN_MAXX = 440;
    private static final int UNTEN_MINX = 440;
    private static final int UNTEN_DEFSCALE = 50;
    private static final float UNTEN_ZOOMF = 3.0f;

    private static final int OBEN_MAXX = 0;
    private static final int OBEN_MINX = 0;
    private static final int OBEN_DEFSCALE = -90;
    private static final float OBEN_ZOOMF = 6.0f;

    // Konstante ints
    private static final int fKowar = 3;
    private static final int fHammer = 3;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Terassa(Start caller, int oldLocation) {
        super(caller, 127);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        schmied = new DDKowar(mainFrame);

        schmiedPoint = new GenericPoint();
        schmiedPoint.x = schmiedFeet.x - DDKowar.Breite / 2;
        schmiedPoint.y = schmiedFeet.y - DDKowar.Hoehe;

        schmiedTalk = new GenericPoint();
        schmiedTalk.x = schmiedFeet.x;
        schmiedTalk.y = schmiedPoint.y - 50;

        // schmiedRect = new borderrect (schmiedPoint.x, schmiedPoint.y, schmiedPoint.x + DDKowar.Breite, schmiedPoint.y + DDKowar.Hoehe);
        schmiedClickRect = new Borderrect(schmiedPoint.x + 7, schmiedPoint.y, schmiedPoint.x + DDKowar.Breite - 14, schmiedPoint.y + DDKowar.Hoehe);

        // hier evaluieren, ob Schmied ueberhaupt da ist
        schmiedVisible = mainFrame.actions[529] && !mainFrame.actions[701];

        InitLocation(oldLocation);
        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // wenn im Vordergrundrect, welches fuer "ist hinten" gilt, dann ist er vorn
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                isVordergrund = vorWennHinten[0].PointInside(mainFrame.krabat.getPos());
                break;
            case 126: // von Murja aus
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(new GenericPoint(500, 339));
                mainFrame.krabat.SetFacing(6);
                isVordergrund = false;
                break;
            case 128: // von Straza aus
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.setPos(new GenericPoint(193, 368));
                mainFrame.krabat.SetFacing(6);
                isVordergrund = false;
                break;
            case 150: // von Cychi aus
                mainFrame.krabat.setPos(new GenericPoint(22, 375));
                mainFrame.krabat.SetFacing(3);
                isVordergrund = false;
                break;
            case 180: // von Karta aus
                mainFrame.krabat.setPos(new GenericPoint(597, 420));
                mainFrame.krabat.SetFacing(9);
                isVordergrund = false;
                break;
        }

        // es ist bekannt, ob er vorn oder hinten steht, also init
        InitBorders();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/terassa/terassa.png");
        gelaender = getPicture("gfx-dd/terassa/gelaender.png");
        busch = getPicture("gfx-dd/terassa/busch.png");
        hammer = getPicture("gfx-dd/terassa/thammer.png");
        gelaender2 = getPicture("gfx-dd/terassa/gelaender2.png");
        delle = getPicture("gfx-dd/terassa/delle.png");

    }

    private void InitBorders() {
        // Grenzen loeschen
        mainFrame.pathWalker.vBorders.removeAllElements();

        // Testen, welche Grenzen gesetzt werden muessen
        if (!isVordergrund) {
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(515, 550, 300, 500, 337, 362));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(10, 450, 10, 560, 363, 389));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(10, 560, 250, 630, 390, 440));

            mainFrame.pathFinder.ClearMatrix(3);

            mainFrame.pathFinder.PosVerbinden(0, 1);
            mainFrame.pathFinder.PosVerbinden(1, 2);

            mainFrame.krabat.maxx = UNTEN_MAXX;
            mainFrame.krabat.minx = UNTEN_MINX;
            mainFrame.krabat.defScale = UNTEN_DEFSCALE;
            mainFrame.krabat.zoomf = UNTEN_ZOOMF;
        } else {
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(15, 446, 34, 479));
            mainFrame.pathWalker.vBorders.addElement(new Bordertrapez(35, 73, 35, 191, 446, 479));

            mainFrame.pathFinder.ClearMatrix(2);

            mainFrame.pathFinder.PosVerbinden(0, 1);

            mainFrame.krabat.maxx = OBEN_MAXX;
            mainFrame.krabat.minx = OBEN_MINX;
            mainFrame.krabat.defScale = OBEN_DEFSCALE;
            mainFrame.krabat.zoomf = OBEN_ZOOMF;
        }
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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

        // Kowar Hintergrund loeschen (sonst vielleicht K geloescht)
        if (schmiedVisible) {
            g.setClip(schmiedPoint.x, schmiedPoint.y, DDKowar.Breite, DDKowar.Hoehe);
            g.drawImage(background, 0, 0);
            g.drawImage(gelaender, 0, 284);

            evalSound(); // Schmied macht Geraeusche
        }

        // Hammer zeichnen, solange noch da
        if (!mainFrame.actions[953] && schmiedVisible) {
            g.setClip(221, 463, 27, 20);
            g.drawImage(hammer, 221, 463);
        }

        // Hier die Delle reinzeichnen, sobald sie drin ist (muss dann immer gezeichnet werden !!!)
        if (mainFrame.actions[700]) {
            g.setClip(203, 358, 15, 10);
            g.drawImage(delle, 203, 358);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        mainFrame.pathWalker.GeheWeg();

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
                if (mainFrame.isInventoryCursor) {
                    evalMouseMoveEvent(mainFrame.mousePoint);  // wenn Krabat unter den Umzieh-Cursor laeuft, muss das Highlight automatisch kommen
                }
            }
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Wenn Krabat im Vordergrund steht, dann braucht nicht gecheckt zu werden
        if (!isVordergrund) {
            // steht hinter Gelander
            if (gelaenderRect.IsPointInRect(pKrTemp)) {
                g.drawImage(!mainFrame.actions[700] ? gelaender : gelaender2, 0, 284);
            }

            // steht hinter Busch
            if (buschRect.IsPointInRect(pKrTemp)) {
                g.drawImage(busch, 512, 284);
            }
        }

        // Kowar zeichnen
        if (schmiedVisible) {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(schmiedPoint.x, schmiedPoint.y, DDKowar.Breite, DDKowar.Hoehe);
            schmied.drawDDkowar(g, TalkPerson, schmiedPoint, schmiedhoertzu);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
        }

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

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
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

                Borderrect tmp = mainFrame.krabat.getRect();

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden Schmied
                if (schmiedClickRect.IsPointInRect(pTemp) && schmiedVisible) {
                    nextActionID = 150;
                    pTxxx = pSchmied;
                }

                // Ausreden Hammer
                if (hammerRect.IsPointInRect(pTemp) && !mainFrame.actions[953] &&
                        schmiedVisible) {
                    nextActionID = 155;
                    pTxxx = pHammer;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (canKrabatStayOnLayer(pTxxx)) {
                    mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
                    mainFrame.repaint();
                }
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Murja gehen ?
                if (ausgangMurja.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangMurja.IsPointInRect(kt)) {
                        pTxxx = pExitMurja;
                    } else {
                        pTxxx = new GenericPoint(pExitMurja.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Straza gehen ?
                if (ausgangStraza.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangStraza.IsPointInRect(kt)) {
                        pTxxx = pExitStraza;
                    } else {
                        pTxxx = new GenericPoint(pExitStraza.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Cychi gehen ?
                if (ausgangCychi.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangCychi.IsPointInRect(kt)) {
                        pTxxx = pExitCychi;
                    } else {
                        pTxxx = new GenericPoint(pExitCychi.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Karta gehen ?
                if (ausgangKarta.IsPointInRect(pTemp)) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKarta.IsPointInRect(kt)) {
                        pTxxx = pExitKarta;
                    } else {
                        pTxxx = new GenericPoint(pExitKarta.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schmied ansehen
                if (schmiedClickRect.IsPointInRect(pTemp) && schmiedVisible) {
                    nextActionID = 1;
                    pTxxx = pSchmied;
                }

                // Hammer ansehen
                if (hammerRect.IsPointInRect(pTemp) && !mainFrame.actions[953] &&
                        schmiedVisible) {
                    nextActionID = 2;
                    pTxxx = pHammer;
                }

                if (canKrabatStayOnLayer(pTxxx)) {
                    mainFrame.pathWalker.SetzeNeuenWeg(pTxxx);
                    mainFrame.repaint();
                }
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangMurja.IsPointInRect(pTemp) ||
                        ausgangStraza.IsPointInRect(pTemp) ||
                        ausgangCychi.IsPointInRect(pTemp) ||
                        ausgangKarta.IsPointInRect(pTemp)) {
                    return;
                }

                // Schmied anreden
                if (schmiedClickRect.IsPointInRect(pTemp) && schmiedVisible) {
                    nextActionID = 50;
                    if (canKrabatStayOnLayer(pSchmied)) {
                        mainFrame.pathWalker.SetzeNeuenWeg(pSchmied);
                        mainFrame.repaint();
                    }
                    return;
                }

                // Hammer nehmen
                if (hammerRect.IsPointInRect(pTemp) && !mainFrame.actions[953] &&
                        schmiedVisible) {
                    nextActionID = 55;
                    if (canKrabatStayOnLayer(pHammer)) {
                        mainFrame.pathWalker.SetzeNeuenWeg(pHammer);
                        mainFrame.repaint();
                    }
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    schmiedClickRect.IsPointInRect(pTemp) && schmiedVisible ||
                    hammerRect.IsPointInRect(pTemp) && !mainFrame.actions[953] &&
                            schmiedVisible;

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
            if (schmiedClickRect.IsPointInRect(pTemp) && schmiedVisible ||
                    hammerRect.IsPointInRect(pTemp) && !mainFrame.actions[953] &&
                            schmiedVisible) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangMurja.IsPointInRect(pTemp) ||
                    ausgangStraza.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangCychi.IsPointInRect(pTemp)) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.cursorLeft);
                    Cursorform = 9;
                }
                return;
            }

            if (ausgangKarta.IsPointInRect(pTemp)) {
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

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // hier wird festgestellt, ob Krabat den Vorder/Hintergrundwechsel durchfuehren muss
    private boolean canKrabatStayOnLayer(GenericPoint Zielpunkt) {
        // rects sind verschieden, je nach dem, ob er oben ist
        if (isVordergrund) {
            if (vorWennVor[0].PointInside(Zielpunkt) ||
                    vorWennVor[1].PointInside(Zielpunkt) ||
                    vorWennVor[2].PointInside(Zielpunkt)) {
                // der WegGeher kann normal wie immer angesprochen werden, nix wird veraendert
                return true;
            } else {
                // Laufpunkt wird extra gesetzt und Anim bei Erreichen eingeschaltet

                // DoAction und ZielPunkt merken
                MerkPunkt = Zielpunkt;
                MerkActionID = nextActionID;

                mainFrame.pathWalker.SetzeWegOhneStand(walktoOben);
                mainFrame.repaint();
                nextActionID = 800;
                return false;
            }
        } else {
            if (!vorWennHinten[0].PointInside(Zielpunkt) &&
                    !vorWennHinten[1].PointInside(Zielpunkt)) {
                // der WegGeher kann normal wie immer angesprochen werden, nix wird veraendert
                return true;
            } else {
                // Laufpunkt wird extra gesetzt und Anim bei Erreichen eingeschaltet

                // DoAction und ZielPunkt merken
                MerkPunkt = Zielpunkt;
                MerkActionID = nextActionID;

                mainFrame.pathWalker.SetzeWegOhneStand(walktoUnten);
                mainFrame.repaint();
                nextActionID = 900;
                return false;
            }
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
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

    private void evalSound() {
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        if (schnauzeSchmied) {
            return;
        }

        int zf = (int) (Math.random() * 100);
        if (zf > 96) {
            int zwzf = (int) (Math.random() * 2.99);
            zwzf += 49;

            mainFrame.soundPlayer.PlayFile("sfx-dd/schmied" + (char) zwzf + ".wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)

        // Achtung, hier sind 2 Ausnahmen, weil diese Sachen in dieser Location behandelt werden muessen
        if (nextActionID > 499 && nextActionID < 600 && nextActionID != 541 && nextActionID != 553) {
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
                // Schmied anschauen
                KrabatSagt("Terassa_1", fKowar, 3, 0, 0);
                break;

            case 2:
                // Hammer anschauen
                KrabatSagt("Terassa_2", fHammer, 3, 0, 0);
                break;

            case 50:
                // Schmied anreden hat wenig Erfolg
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = true;
                Counter = 10;
                nextActionID = 52;
                break;

            case 52:
                // nach bisschen Warten die Antwort bringen (Sound stoert hoffentlich nicht mehr)
                if (--Counter > 1) {
                    break;
                }
                int zuffZahl = (int) (Math.random() * 3.9);
                switch (zuffZahl) {
                    case 0:
                        PersonSagt("Terassa_3", fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 1:
                        PersonSagt("Terassa_4", fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 2:
                        PersonSagt("Terassa_5", fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 3:
                        PersonSagt("Terassa_6", fKowar, 45, 0, 53, schmiedTalk);
                        break;
                }
                break;

            case 53:
                // Ende Talk Schmied
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = false;
                nextActionID = 0;
                break;

            case 55:
                // Hammer mitnehmen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = true;
                mainFrame.krabat.SetFacing(fHammer);
                nextActionID = 60;
                mainFrame.krabat.nAnimation = 34;
                Counter = 5;
                break;

            case 60:
                // Krabat sagt Spruch
                if (--Counter == 1) {
                    mainFrame.actions[953] = true;        // Flag setzen
                    mainFrame.isClipSet = false;  // alles neu zeichnen
                    // Inventar hinzufuegen
                    mainFrame.inventory.vInventory.addElement(46);
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                KrabatSagt("Terassa_7", 0, 3, 2, 63);
                break;

            case 63:
                // zum haemmern hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pToHammer);
                nextActionID = 65;
                break;

            case 65:
                // Krabat schlaegt zu
                mainFrame.krabat.SetFacing(12);
                mainFrame.krabat.nAnimation = 147;
                nextActionID = 70;
                Counter = 10;
                break;

            case 70:
                // Schmied regt sich auf
                if (--Counter == 1) {
                    mainFrame.actions[700] = true; // ab jetzt die Delle drin
                    mainFrame.isClipSet = false;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                PersonSagt("Terassa_8", fKowar, 45, 2, 75, schmiedTalk);
                schmiedhoertzu = true;
                break;

            case 75:
                // Krabat antwortet
                KrabatSagt("Terassa_9", fKowar, 1, 2, 80);
                break;

            case 80:
                // Schmied sagt letzten Spruch
                PersonSagt("Terassa_10", 0, 45, 2, 85, schmiedTalk);
                schmiedhoertzu = false;
                break;

            case 85:
                // Ende dieser Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                schnauzeSchmied = false;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Murja
                // Hat Krabat noch Dienstkleidung an -> zur Murja darf er so gehen
                if (mainFrame.actions[511]) {
                    krabatUmziehen();
                    break;
                }
                NeuesBild(126, locationID);
                break;

            case 101:
                // Gehe zu Straza
                NeuesBild(128, locationID);
                break;

            case 102:
                // Gehe zu Cychi
                // Hat Krabat noch Dienstkleidung an -> erst umziehen, aber nicht automatisch !!!
                if (mainFrame.actions[511]) {
                    krabatUmziehen();
                    break;
                }
                NeuesBild(150, locationID);
                break;

            case 103:
                // Gehe zu Karta
                // Hat Krabat noch Dienstkleidung an -> erst umziehen, aber nicht automatisch
                if (mainFrame.actions[511]) {
                    krabatUmziehen();
                    break;
                }
                NeuesBild(180, locationID);
                break;

            case 150:
                // Kowar - Ausreden
                MPersonAusrede(fKowar);
                break;

            case 155:
                // Hammer - Ausreden
                DingAusrede(fHammer);
                break;

            case 541:
                // Krabat zieht sich Bedienstetenkleidung an
                nextActionID = 700;
                if (canKrabatStayOnLayer(vorUmziehPoint)) {
                    mainFrame.pathWalker.SetzeNeuenWeg(vorUmziehPoint);
                }
                break;

            case 553:
                // Krabat zieht sich wieder normale Klamotten an
                nextActionID = 750;
                if (canKrabatStayOnLayer(vorUmziehPoint)) {
                    mainFrame.pathWalker.SetzeNeuenWeg(vorUmziehPoint);
                }
                break;

            // Anim "Sluz. Drasta anziehen"

            case 700:
                // Krabat geht hintern Busch
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(UmziehPoint);
                nextActionID = 710;
                break;

            case 710:
                // Kleidung wechseln, sluz Drasta anziehen
                mainFrame.actions[511] = true;
                mainFrame.actions[850] = true;
                mainFrame.checkKrabat();
                mainFrame.inventory.vInventory.addElement(53);
                mainFrame.inventory.vInventory.removeElement(41);
                nextActionID = 720;
                break;

            case 720:
                // wieder erscheinen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(nachUmziehPoint);
                if (!mainFrame.actions[702]) {
                    nextActionID = 730;  // Spruch nur 1x reissen
                } else {
                    nextActionID = 740;
                }
                break;

            case 730:
                // Kommentar
                KrabatSagt("Terassa_11", 0, 3, 0, 740);
                break;

            case 740:
            case 780:
                // Ende
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            // Anim "Normale Kleidung anziehen"

            case 750:
                // Krabat geht hintern Busch
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.isInventoryCursor = false;
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(UmziehPoint);
                nextActionID = 760;
                break;

            case 760:
                // Kleidung wechseln, wieder normale Kleidung anziehen
                mainFrame.actions[511] = false;
                mainFrame.actions[850] = false;
                mainFrame.checkKrabat();
                mainFrame.inventory.vInventory.addElement(41);
                mainFrame.inventory.vInventory.removeElement(53);
                nextActionID = 770;
                break;

            case 770:
                // wieder erscheinen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(nachUmziehPoint);
                if (!mainFrame.actions[702]) {
                    nextActionID = 775;
                } else {
                    nextActionID = 780;
                }
                break;

            case 775:
                // Spruch reissen, dass wieder Normalkleidung
                mainFrame.actions[702] = true;
                KrabatSagt("Terassa_12", 0, 3, 0, 780);
                break;

            // Hier Routinen fuer "die Treppe runter"

            case 800:
                // von vorn nach hinten laufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // auf die Treppe zu laufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(walktoTreppeOben);
                Counter = 20;
                nextActionID = 810;
                break;

            case 810:
                // die Treppe runterlaufen, nur runterbeamen
                if (--Counter > 1) {
                    break;
                }
                // mainFrame.wegGeher.SetzeGarantiertWegFalsch (walktoTreppe);
                mainFrame.krabat.setPos(walktoTreppe);
                // Borders neu initialisieren
                isVordergrund = false;
                InitBorders();
                nextActionID = 820;
                break;

            case 820:
                // wieder in den richtigen Bereich laufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(walktoUnten);
                nextActionID = 830;
                break;

            case 830:
            case 930:
                // wir sind da, nur noch die alten Werte restaurieren
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = MerkActionID;
                mainFrame.pathWalker.SetzeNeuenWeg(MerkPunkt);
                mainFrame.repaint();
                break;

            // Hier Routinen fuer "die Treppe rauf" (Hallo Kelly Bundy !!!)

            case 900:
                // von hinten nach vorn laufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                // auf die Treppe zu laufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(walktoTreppe);
                Counter = 20;
                nextActionID = 910;
                break;

            case 910:
                // die Treppe hochlaufen, nur beamen
                if (--Counter > 1) {
                    break;
                }
                // mainFrame.wegGeher.SetzeGarantiertWegFalsch (walktoTreppeOben);
                mainFrame.krabat.setPos(walktoTreppeOben);
                nextActionID = 920;
                break;

            case 920:
                // wieder in den richtigen Bereich laufen
                mainFrame.pathWalker.SetzeGarantiertNeuenWeg(walktoOben);
                // Borders neu initialisieren
                isVordergrund = true;
                InitBorders();
                nextActionID = 930;
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }

    private void krabatUmziehen() {
        // Krabat zieht sich Dienstkleidung aus
        KrabatSagt("Terassa_13", 0, 3, 0, 0);
    }
}