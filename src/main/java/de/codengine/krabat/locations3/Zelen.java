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
import de.codengine.krabat.anims.Awgust;
import de.codengine.krabat.anims.Fellowship;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Zelen extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Zelen.class);
    private GenericImage background;
    private GenericImage iprikaz;
    private GenericImage vorder;
    private final GenericImage[] kerze;
    private GenericImage siegel;
    private final GenericImage[] krabat_siegeln;

    private final Awgust awgust;
    private boolean hatHandErhoben = false;

    private final Fellowship fellowship;

    private boolean awgustVisible = false;  // Awgust erscheint nur zusammen mit Druzina !!!

    private boolean walkReadyAwgust = true;
    private boolean walkReadyDruzina = true;

    private int VerhinderwalkAwgust;
    private int VerhinderwalkDruzina;

    private static final int MAX_VERHINDERWALKAWGUST = 2;
    private static final int MAX_VERHINDERWALKDRUZINA = 2;

    private int Flacker = 0;
    private int Tropf = 0;

    private int Verhinderflacker;
    private int Verhindertropf;

    private static final int MAX_VERHINDERFLACKER = 2;
    private static final int MAX_VERHINDERTROPF = 2;

    // Konstanten - Rects
    private static final BorderRect obererAusgang
            = new BorderRect(177, 234, 237, 410);
    private static final BorderRect prikaz
            = new BorderRect(412, 404, 435, 412);
    private static final BorderRect kerzeRect
            = new BorderRect(378, 382, 393, 402);

    // Konstante Points
    private static final GenericPoint pExitUp = new GenericPoint(215, 424);
    private static final GenericPoint pTisch = new GenericPoint(392, 500);
    private static final GenericPoint pKerze = new GenericPoint(392, 500);
    private static final GenericPoint pVorTisch = new GenericPoint(305, 472);

    private static final GenericPoint awgustStart = new GenericPoint(145, 374);
    private static final GenericPoint awgustStop = new GenericPoint(211, 443);
    private static final GenericPoint druzinaStart = new GenericPoint(148, 347);
    private static final GenericPoint druzinaStop = new GenericPoint(218, 406);

    // Konstante ints
    private static final int fPrikaz = 3;
    private static final int fKlavier = 9;
    private static final int fStuhl = 9;
    private static final int fAwgust = 9;
    private static final int fKerze = 12;

    private int SonderAnim = 0;

    private int Siegeln = 0;

    private int Siegelzeit = 40;

    private int Verhindersiegeln;

    private static final int MAX_VERHINDERSIEGELN = 7;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zelen(Start caller, int oldLocation) {
        super(caller, 145);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxX = 0;
        mainFrame.krabat.zoomFactor = 2f;
        mainFrame.krabat.defaultScale = -60;

        awgust = new Awgust(mainFrame);
        fellowship = new Fellowship(mainFrame);

        awgust.maxX = 420;
        awgust.zoomFactor = 3f;
        awgust.defaultScale = 0;

        fellowship.maxX = 420;
        fellowship.zoomFactor = 3f;

        awgust.setPos(awgustStart);
        fellowship.setPos(druzinaStart);

        kerze = new GenericImage[8];
        krabat_siegeln = new GenericImage[2];

        initLocation(oldLocation);

        Verhinderflacker = MAX_VERHINDERFLACKER;
        Verhindertropf = MAX_VERHINDERTROPF;
        Verhindersiegeln = MAX_VERHINDERSIEGELN;

        VerhinderwalkAwgust = MAX_VERHINDERWALKAWGUST;
        VerhinderwalkDruzina = MAX_VERHINDERWALKDRUZINA - 1;  // asynchron Laufen !!

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void initLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.pathWalker.vBorders.removeAllElements();
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(215, 305, 215, 305, 424, 464));
        mainFrame.pathWalker.vBorders.addElement
                (new BorderTrapezoid(135, 305, 135, 305, 465, 479));

        mainFrame.pathFinder.clearMatrix(2);

        mainFrame.pathFinder.connectPos(0, 1);

        initImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 144: // von Couch aus
                mainFrame.krabat.setPos(new GenericPoint(215, 430));
                mainFrame.krabat.setFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void initImages() {
        background = getPicture("gfx-dd/zelen/zelen.png");
        iprikaz = getPicture("gfx-dd/zelen/zprikaz.png");
        vorder = getPicture("gfx-dd/zelen/zdurje.png");

        kerze[0] = getPicture("gfx-dd/zelen/kerze1.png");
        kerze[1] = getPicture("gfx-dd/zelen/kerze2.png");
        kerze[2] = getPicture("gfx-dd/zelen/kerze3.png");
        kerze[3] = getPicture("gfx-dd/zelen/kerze4.png");
        kerze[4] = getPicture("gfx-dd/zelen/kerze5.png");
        kerze[5] = getPicture("gfx-dd/zelen/kerze6.png");
        kerze[6] = getPicture("gfx-dd/zelen/kerze7.png");
        kerze[7] = getPicture("gfx-dd/zelen/kerze8.png");

        siegel = getPicture("gfx-dd/zelen/stempel.png");

        krabat_siegeln[0] = getPicture("gfx-dd/zelen/s-o-siegeln1.png");
        krabat_siegeln[1] = getPicture("gfx-dd/zelen/s-o-siegeln2.png");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
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

        // Awgust zeichnen, wenn da...
        if (awgustVisible) {
            // Hier wird ersteinmal der Hintergrund beider Figuren geloescht

            // Clipping - Rectangle feststellen und setzen fuer Druzina
            BorderRect temp = fellowship.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);

            // Hier dasselbe fuer den August
            temp = awgust.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);

            // beide Figuren bewegen, wenn dies noetig ist
            if (--VerhinderwalkAwgust < 1) {
                VerhinderwalkAwgust = MAX_VERHINDERWALKAWGUST;
                walkReadyAwgust = awgust.move();
            }

            if (--VerhinderwalkDruzina < 1) {
                VerhinderwalkDruzina = MAX_VERHINDERWALKDRUZINA;
                walkReadyDruzina = fellowship.move();
            }

            // nun beide zeichnen, Awgust als zweiten (ist immer davor)
            // Clipping - Rectangle feststellen und setzen fuer Druzina
            temp = fellowship.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);
            fellowship.drawDruzina(g);
            g.drawImage(vorder, 82, 210);

            // fuer Awgust ein paar Unterscheidungen
            temp = awgust.getBoundingBox();
            g.setClip(temp.topLeftPoint.x - 10, temp.topLeftPoint.y - 10, temp.bottomRightPoint.x - temp.topLeftPoint.x + 20,
                    temp.bottomRightPoint.y - temp.topLeftPoint.y + 20);
            if (talkPerson == 40 && mainFrame.talkCount > 1) {
                awgust.talkAwgust(g, hatHandErhoben);
            } else {
                awgust.drawAwgust(g);
            }
            g.drawImage(vorder, 82, 210);
        }

        // prikaz zeichnen, solange noch da
        if (!mainFrame.actions[640]) {
            g.setClip(412, 404, 24, 10);
            g.drawImage(background, 0, 0);
            g.drawImage(iprikaz, 412, 404);
        }

        // Kerze zeichnen
        if (--Verhinderflacker < 1) {
            Verhinderflacker = MAX_VERHINDERFLACKER;
            Flacker = (int) (Math.random() * 4.9);
        }
        if (--Verhindertropf < 1) {
            Verhindertropf = MAX_VERHINDERTROPF;
            Tropf++;
            if (Tropf == 3) {
                Tropf = 0;
            }
        }
        g.setClip(378, 382, 15, 20);
        g.drawImage(background, 0, 0);
        g.drawImage(kerze[Flacker], 378, 382);
        g.drawImage(kerze[Tropf + 5], 378, 382);

        // Stempel zeichnen
        g.setClip(370, 397, 9, 10);
        g.drawImage(siegel, 370, 397);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.ENABLED) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        mainFrame.pathWalker.doWalk();

        if (SonderAnim != 0) {
            // hier erstmal alles berechnen, dann je nachdem die Bilder switchen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.getPos().x, mainFrame.krabat.getPos().y);

            // Groesse
            int scale = mainFrame.krabat.defaultScale;
            scale += (int) (((float) mainFrame.krabat.maxX - (float) hier.y) / mainFrame.krabat.zoomFactor);
            if (scale < mainFrame.krabat.defaultScale) {
                scale = mainFrame.krabat.defaultScale;
            }

            // System.out.println ("Scale ist " + scale + " gross.");

            // Hoehe: nur offset
            int hoch = 100 - scale;

            // Breite abhaengig von Hoehe...
            int weit = 50 - scale / 2;

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            // Hier ab und zu umschalten...
            if (--Verhindersiegeln < 1) {
                Verhindersiegeln = MAX_VERHINDERSIEGELN;
                if (Siegeln == 1) {
                    Siegeln = 0;
                } else {
                    Siegeln = 1;
                }
            }

            // selbstbeendende Anim
            if (--Siegelzeit < 0) {
                SonderAnim = 0;
            }

            // zeichnen und gut
            g.drawImage(krabat_siegeln[Siegeln], hier.x, hier.y, weit, hoch);
        } else {
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

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
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

                BorderRect tmp = mainFrame.krabat.getBoundingBox();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.isPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden Stuhl
//                         if (stuhl.IsPointInRect (pTemp) == true)
//                             { 
//                                 nextActionID = 165;
//                                 pTemp = pStuhl;
//                             }

                // Ausreden Prikaz
                if (prikaz.isPointInRect(pTemp) && !mainFrame.actions[640]) {
                    nextActionID = 155;
                    pTemp = pTisch;
                }

                // Ausreden Kerze
                if (kerzeRect.isPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 31:
                        case 33:// ununterschriebene und unterschriebene ungesiegelte Erlaubnis
                            nextActionID = 80;
                            break;
                        case 32:
                        case 34:// bereits gesiegelte Erlaubnisse
                            nextActionID = 95;
                            break;
                        default:
                            nextActionID = 170;
                    }
                    pTemp = pKerze;
                }

                // Ausreden Klavier
//                         if (klavier.IsPointInRect (pTemp) == true)
//                             { 
//                                 nextActionID = 160;
//                                 pTemp = pKlavier;
//                             }

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

                // zu Couch gehen ?
                if (obererAusgang.isPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.isPointInRect(kt)) {
                        pTemp = pExitUp;
                    } else {
                        pTemp = new GenericPoint(pExitUp.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.stopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Prikaz ansehen
                if (prikaz.isPointInRect(pTemp) && !mainFrame.actions[640]) {
                    nextActionID = 1;
                    pTemp = pTisch;
                }

                // Kerze ansehen
                if (kerzeRect.isPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pKerze;
                }

                mainFrame.pathWalker.setNewWay(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Prikaz mitnehmen
                if (prikaz.isPointInRect(pTemp) && !mainFrame.actions[640]) {
                    nextActionID = 50;
                    mainFrame.pathWalker.setNewWay(pTisch);
                    mainFrame.repaint();
                    return;
                }

                // Kerze benutzen
                if (kerzeRect.isPointInRect(pTemp)) {
                    nextActionID = 90;
                    mainFrame.pathWalker.setNewWay(pKerze);
                    mainFrame.repaint();
                    return;
                }

                // Stuhl mitnehmen
//                         if (stuhl.IsPointInRect (pTemp) == true)
//                             {
//                                 nextActionID = 60;
//                                 mainFrame.wegGeher.SetzeNeuenWeg (pStuhl);
// 	   			mainFrame.repaint();
//                                 return;
//                             }	

                // Klavier mitnehmen
//                         if (klavier.IsPointInRect (pTemp) == true)
//                             {
//                                 nextActionID = 55;
//                                 mainFrame.wegGeher.SetzeNeuenWeg (pKlavier);
// 	   			mainFrame.repaint();
//                                 return;
//                             }	

                // Wenn Ausgang -> kein Inventar anzeigen
                if (obererAusgang.isPointInRect(pTemp)) {
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.stopWalking();
                mainFrame.repaint();
            }
        }
    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
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
// 		    (stuhl.IsPointInRect (pTemp) == true) ||
//                     (klavier.IsPointInRect (pTemp) == true) ||
                    prikaz.isPointInRect(pTemp) && !mainFrame.actions[640] ||
                    kerzeRect.isPointInRect(pTemp);

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
            if (kerzeRect.isPointInRect(pTemp) ||
// 		    (klavier.IsPointInRect (pTemp) == true) ||
//                     (stuhl.IsPointInRect (pTemp) == true) ||
                    prikaz.isPointInRect(pTemp) && !mainFrame.actions[640]) {
                if (cursorShape != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    cursorShape = 1;
                }
                return;
            }

            if (obererAusgang.isPointInRect(pTemp)) {
                if (cursorShape != 12) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    cursorShape = 12;
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

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
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

        // hier wird zu den Standardausreden von Krabat verzweigt, 
        // wenn noetig (in Superklasse)
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
            case 1:
                // Prikaz ansehen
                krabatSays("Zelen_1", fPrikaz, 3, 0, 0);
                break;

            case 2:
                // Klavier ansehen
                krabatSays("Zelen_2", fKlavier, 3, 0, 0);
                break;

            case 3:
                // Stuhl ansehen
                krabatSays("Zelen_3", fStuhl, 3, 0, 0);
                break;

            case 4:
                // Kerze ansehen
                krabatSays("Zelen_4", fPrikaz, 3, 0, 0);
                break;

            case 50:
                // Prikaz mitnehmen, zuerstmal hinlaufen
                mainFrame.isAnimRunning = true;
                evalMouseMoveEvent(mainFrame.mousePoint);
                mainFrame.pathWalker.setNewWayGuaranteed(pKerze);
                nextActionID = 52;
                break;

            case 52:
                // so, nun darf genommen werden
                mainFrame.krabat.setFacing(fPrikaz);
                mainFrame.krabat.nAnimation = 31;
                Counter = 5;
                nextActionID = 53;
                break;

            case 53:
                // auf Ende nehmen warten
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(49);
                    mainFrame.actions[640] = true;
                    mainFrame.isClipSet = false;
                }
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 65;
                break;

            case 55:
                // Klavier mitnehmen
                krabatSays("Zelen_5", fKlavier, 3, 0, 0);
                break;

            case 60:
                // Stuhl mitnehmen
                krabatSays("Zelen_6", fStuhl, 3, 0, 0);
                break;

            case 65:
                // Ende nehmen
                if (mainFrame.actions[640] && mainFrame.actions[641]) {
                    nextActionID = 220;
                } else {
                    nextActionID = 70;
                }
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 70:
                // einfaches Anim-Ende, K muss zuruecklaufen 
                if (SonderAnim != 0) {
                    break;
                }
                mainFrame.pathWalker.setNewWay(pVorTisch);
                nextActionID = 75;
                break;

            case 75:
                // Ende dieser Anim
                mainFrame.isAnimRunning = false;
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 80:
                // nur Siegeln
                if (!mainFrame.actions[641]) {
                    mainFrame.isAnimRunning = true;
                    evalMouseMoveEvent(mainFrame.mousePoint);
                    mainFrame.isInventoryCursor = false;

                    // Dowol noch nicht gesiegelt, also testen, ob da, dann kann gesiegelt werden
                    if (!mainFrame.actions[645]) {
                        // Dowol noch nicht an PredMali gegeben, also ununterschriebene austauschen
                        mainFrame.inventory.vInventory.removeElement(31);
                        mainFrame.inventory.vInventory.addElement(32);
                    }

                    if (mainFrame.actions[645] && mainFrame.actions[647] &&
                            mainFrame.actions[648]) {
                        // Dowol von PredMali zurueck, also unterschriebene austauschen
                        mainFrame.inventory.vInventory.removeElement(33);
                        mainFrame.inventory.vInventory.addElement(34);
                    }

                    mainFrame.pathWalker.setNewWayGuaranteed(pKerze);
                    nextActionID = 83;
                } else { // schon gesiegelt, nicht 2x
                    krabatSays("Zelen_7", fPrikaz, 3, 0, 0);
                }
                break;

            case 83:
                // steht vor der Kerze, also gut
                mainFrame.actions[641] = true;
                SonderAnim = 1;
                mainFrame.krabat.setFacing(fKerze);
                Counter = 20;
                nextActionID = 86;
                break;

            case 86:
                // Krabat spricht
                if (--Counter == 1) {
                    mainFrame.soundPlayer.playFile("sfx/schildlegen.wav");
                }
                if (SonderAnim != 0) {
                    break;
                }
                krabatSays("Zelen_8", 0, 3, 2, 87);
                break;

            case 87:
                // jetzt schauen, obs schon zu Ende ist oder noch nicht...
                if (mainFrame.actions[640] && mainFrame.actions[641]) {
                    nextActionID = 220;
                } else {
                    nextActionID = 70;
                }
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 90:
                // Kerze allein benutzen geht nicht
                krabatSays("Zelen_9", fPrikaz, 3, 0, 0);
                break;

            case 95:
                // fertige Erlaubnis auf kerze -> geht nicht
                krabatSays("Zelen_10", fPrikaz, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Couch
                createNewLocation(144, locationID);
                break;

            case 155:
                // Prikaz - Ausreden
                thingExcuse(fPrikaz);
                break;

            case 160:
                // Klavier - Ausreden
                thingExcuse(fKlavier);
                break;

            case 165:
                // Stuhl - Ausreden
                thingExcuse(fStuhl);
                break;

            case 170:
                // Kerze-Ausreden
                thingExcuse(fKerze);
                break;

            case 220:
                // Awgust und Druzina erscheinen und laufen her
                BackgroundMusicPlayer.getInstance().playTrack(23, false);
                awgustVisible = true;
                awgust.moveTo(awgustStop);
                fellowship.moveTo(druzinaStop);
                walkReadyAwgust = false;
                walkReadyDruzina = false;
                nextActionID = 225;
                break;

            case 225:
                // warten, bis beide angekommen sind
                if (!walkReadyAwgust || !walkReadyDruzina) {
                    break;
                }
                nextActionID = 230;
                break;

            case 230:
                // Reaktion August
                awgustVisible = true;
                personSays("Zelen_11", fAwgust, 40, 2, 240, awgust.evalAwgustTalkPoint());
                break;

            case 240:
                // Krabat spricht
                krabatSays("Zelen_12", 0, 1, 2, 250);
                break;

            case 250:
                // Reaktion August
                hatHandErhoben = true;
                personSays("Zelen_13", 0, 40, 2, 260, awgust.evalAwgustTalkPoint());
                break;

            case 260:
                // Gehe zu Kuche
                mainFrame.actions[655] = true; // Hier Action - Variable fuer Muellererscheinen !!!!
                createNewLocation(132, locationID);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}