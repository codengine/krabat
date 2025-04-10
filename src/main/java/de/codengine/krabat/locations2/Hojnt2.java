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
import de.codengine.krabat.anims.Boom;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Hojnt2 extends MainLocation2 {
    private static final Logger log = LoggerFactory.getLogger(Hojnt2.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage skyl;
    private GenericImage skyr;
    private GenericImage hojnt2;
    private GenericImage hojnt3; /* hojnt4, */
    private GenericImage seil;
    private boolean setScroll = false;
    private int scrollwert;
    private boolean muellerda = false;
    private boolean setAnim = false;

    private Boom muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // private boolean AnimActive = false;
    private Miller mueller;

    private GenericImage leftschatten;
    private GenericImage rightschatten;
    private GenericImage vorder;
    private boolean isLeft;
    private int xpos;
    private static final int MIN_SCHATTENX = 340;
    private static final int MAX_SCHATTENX = 620;
    private static final int MOVE = 4;
    private int Verhinderwandern;
    private static final int MAX_VERHINDERWANDERN = 5;

    // Konstante rects
    private static final BorderRect obererAusgang = new BorderRect(199, 277, 271, 379);
    private static final BorderRect rechterAusgang = new BorderRect(1206, 409, 1279, 479);
    private static final BorderRect strauchRect = new BorderRect(160, 249, 305, 394);
    private static final BorderRect jamaRect = new BorderRect(1081, 378, 1188, 424);
    private static final BorderRect hoelzerRect = new BorderRect(613, 250, 640, 290);
    private static final BorderRect leineRect = new BorderRect(1100, 245, 1155, 362);
    private static final BorderRect kurotwy1Rect = new BorderRect(438, 278, 454, 311);
    private static final BorderRect kurotwy2Rect = new BorderRect(480, 278, 507, 311);
    private static final BorderRect wokno1Rect = new BorderRect(378, 267, 420, 307);
    private static final BorderRect wokno2Rect = new BorderRect(523, 267, 565, 309);
    private static final BorderRect sekeraRect = new BorderRect(145, 188, 237, 220);
    private static final BorderRect durjeRect = new BorderRect(273, 266, 303, 339);
    private static final BorderRect drjewoRect = new BorderRect(0, 150, 84, 428);

    // Konstante Punkte
    // Konstante Punkte
    private static final GenericPoint Pup = new GenericPoint(246, 350);
    private static final GenericPoint Pright = new GenericPoint(1279, 452);
    private static final GenericPoint Pjama = new GenericPoint(1134, 421);
    private static final GenericPoint Phoelzer = new GenericPoint(587, 357);
    private static final GenericPoint Pkurotwy1 = new GenericPoint(447, 351);
    private static final GenericPoint Pkurotwy2 = new GenericPoint(494, 343);
    private static final GenericPoint Pwokno1 = new GenericPoint(399, 350);
    private static final GenericPoint Pwokno2 = new GenericPoint(543, 347);
    private static final GenericPoint Psekera = new GenericPoint(412, 396);
    private static final GenericPoint Pdurje = new GenericPoint(277, 351);
    private static final GenericPoint Pdrjewo = new GenericPoint(284, 365);
    private static final GenericPoint mlynkFeet = new GenericPoint(1016, 438);

    // Konstante Ints
    private static final int fJama = 12;
    private static final int fLeine = 12;
    private static final int fHoelzer = 3;
    private static final int fKurotwy = 12;
    private static final int fWokno = 12;
    private static final int fSekera = 9;
    private static final int fDurje = 3;
    private static final int fDrjewo = 9;

    public Hojnt2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        mainFrame.krabat.maxx = 420;
        mainFrame.krabat.zoomf = 4.4f;
        mainFrame.krabat.defScale = -10;

        mueller = new Miller(mainFrame);

        muellermorph = new Boom(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = 0;

        mueller.setPos(mlynkFeet);
        mueller.SetFacing(3);

        xpos = (int) (Math.random() * (MAX_SCHATTENX - MIN_SCHATTENX - 10) + MIN_SCHATTENX + 5);
        isLeft = (int) (Math.random() * 50) > 25;
        Verhinderwandern = MAX_VERHINDERWANDERN;

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                BackgroundMusicPlayer.getInstance().playTrack(20, true);
                break;
            case 87: // Aus Wjes kommend
                BackgroundMusicPlayer.getInstance().stop();
                mainFrame.krabat.setPos(new GenericPoint(1243, 458));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                setAnim = true;
                TalkPause = 10;
                break;

            case 80: // Von Njedz aus
                mainFrame.krabat.setPos(new GenericPoint(256, 354));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 0;
                setScroll = true;
                break;
        }

        InitLocation();

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.pathWalker.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(229, 265, 267, 340, 350, 364));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(285, 606, 361, 642, 365, 383));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(605, 663, 715, 927, 384, 410));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(716, 996, 857, 1138, 411, 441));
        mainFrame.pathWalker.vBorders.addElement(new BorderTrapezoid(985, 1279, 1117, 1279, 442, 463));

        // Matrix loeschen
        mainFrame.pathFinder.ClearMatrix(5);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.pathFinder.PosVerbinden(0, 1);
        mainFrame.pathFinder.PosVerbinden(1, 2);
        mainFrame.pathFinder.PosVerbinden(2, 3);
        mainFrame.pathFinder.PosVerbinden(3, 4);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx/hojnt/hojnt-l.png");
        backr = getPicture("gfx/hojnt/hojnt-r.png");
        skyl = getPicture("gfx/hojnt/hsky-l.png");
        skyr = getPicture("gfx/hojnt/hsky-r.png");
        hojnt2 = getPicture("gfx/hojnt/hojnt2.png");
        hojnt3 = getPicture("gfx/hojnt/hojnt3.png");
        seil = getPicture("gfx/hojnt/seil.png");

        leftschatten = getPicture("gfx/hojnt/ho-lschatten.png");
        rightschatten = getPicture("gfx/hojnt/ho-rschatten.png");
        vorder = getPicture("gfx/hojnt/jwokna.png");

    }

    @Override
    public void cleanup() {
        backl = null;
        backr = null;
        skyl = null;
        skyr = null;
        hojnt2 = null;
        hojnt3 = null;
        seil = null;

        leftschatten = null;
        rightschatten = null;
        vorder = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollX = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.mousePoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isBackgroundAnimRunning = true;
            if (setAnim) {
                mainFrame.isAnimRunning = true;
            }
        }

        // Hintergrund zeichnen
        g.drawImage(skyl, mainFrame.scrollX / 10, 0);
        g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Parallaxer ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollX - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 325);
            g.drawImage(skyl, mainFrame.scrollX / 10, 0);
            g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }


        // wenn Jaeger in Huette, dann Schatten wandern lassen
        if (--Verhinderwandern < 1) {
            Verhinderwandern = MAX_VERHINDERWANDERN;
            if (!isLeft) {
                xpos += MOVE;
                if (xpos > MAX_SCHATTENX) {
                    isLeft = true;
                }
            } else {
                xpos -= MOVE;
                if (xpos < MIN_SCHATTENX) {
                    isLeft = false;
                }
            }
        }
        g.setClip(340, 246, 273, 83);
        g.drawImage(backl, 0, 0);
        if (isLeft) {
            g.drawImage(leftschatten, xpos, 280);
        } else {
            g.drawImage(rightschatten, xpos, 280);
        }
        g.drawImage(vorder, 340, 246);

        // kaputte Grube zeichnen, wenn noetig, sonst Strick einfuegen
        g.setClip(1122, 191, 6, 106);
        g.drawImage(skyr, mainFrame.scrollX / 10 + 540, 0);
        g.drawImage(backr, 640, 0);
        g.drawImage(seil, 1122, 191);

        // leeren Haken zeichnen, wenn noetig
        g.setClip(426, 258, 20, 19);
        g.drawImage(hojnt3, 426, 258);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // Mueller zeichnen
        if (muellerda) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            BorderRect temp = mueller.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(backr, 640, 0);

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

        // hinterm Brunnen (nur Clipping - Region wird neugezeichnet)
        if (strauchRect.IsPointInRect(pKrTemp)) {
            g.drawImage(hojnt2, 143, 262);
        }

        // Ab hier muss Cliprect wieder gerettet werden
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

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 1000;
        }

        // Gibt es was zu tun ? Achtung! Scrolling - Verriegelung in DoActions extra !!!
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Auszugebenden Text abbrechen
        outputText = "";
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }

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

                BorderRect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Hier kommt Routine hin, die Standard - Ausredeantworten auswaehlt, je nach Gegenstand
                // in Location, damit Textposition bekannt ist!!!

                // Ausreden fuer Grube
                if (jamaRect.IsPointInRect(pTemp)) {
                    // nur Ausreden
                    nextActionID = 155;
                    pTemp = Pjama;
                }

                // an Leine ziehen mit Stock oder sowas
                if (leineRect.IsPointInRect(pTemp)) {
                    nextActionID = 660;
                    pTemp = Pjama;
                }

                // an Hoelzern mit Gegenstand, immer Ausrede
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    nextActionID = 166;
                    pTemp = Phoelzer;
                }

                // Ausrede fuer Rebhuehner1
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 420;
                    pTemp = Pkurotwy1;
                }

                // Ausrede fuer Rebhuehner2
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 420;
                    pTemp = Pkurotwy2;
                }

                // Ausrede fuer Schatten (Fenster1)
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                        case 18: // bron
                            nextActionID = 670;
                            break;
                        default:
                            nextActionID = 430;
                            break;
                    }
                    pTemp = Pwokno1;
                }

                // Ausrede fuer Schatten (Fenster2)
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                        case 18: // bron
                            nextActionID = 670;
                            break;
                        default:
                            nextActionID = 430;
                            break;
                    }
                    pTemp = Pwokno2;
                }

                // Ausrede fuer Axt
                if (sekeraRect.IsPointInRect(pTemp)) {
                    nextActionID = 440;
                    pTemp = Psekera;
                }

                // Ausrede fuer Tuer
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 450;
                    pTemp = Pdurje;
                }

                // Ausrede fuer Drjewo
                if (drjewoRect.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 680 : 460;
                    pTemp = Pdrjewo;
                }

                // wenn nix ausgewaehlt, dann einfach nur hinlaufen
                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
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

                // nach Njedz gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Wjes gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.isDoubleClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Jama ansehen
                if (jamaRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pjama;
                }

                // Leine ansehen
                if (leineRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Pjama;
                }

                // Hoelzer ansehen
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Phoelzer;
                }

                // Rebhuehner1 ansehen
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pkurotwy1;
                }

                // Rebhuehner2 ansehen
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pkurotwy2;
                }

                // Schatten (Fenster1) ansehen
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Pwokno1;
                }

                // Schatten (Fenster2) ansehen
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Pwokno2;
                }

                // Axt ansehen
                if (sekeraRect.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = Psekera;
                }

                // Tuer ansehen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 8;
                    pTemp = Pdurje;
                }

                // Drjewo ansehen
                if (drjewoRect.IsPointInRect(pTemp)) {
                    nextActionID = 9;
                    pTemp = Pdrjewo;
                }

                mainFrame.pathWalker.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weg nach Wjes anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Njedz anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Jama mitnehmen ?
                if (jamaRect.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pjama);
                    mainFrame.repaint();
                    return;
                }

                // Leine benutzen geht nicht !
                if (leineRect.IsPointInRect(pTemp)) {
                    nextActionID = 53;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pjama);
                    mainFrame.repaint();
                    return;
                }

                // Hoelzer benutzen endet in Ausrede
                if (hoelzerRect.IsPointInRect(pTemp)) {
                    nextActionID = 190;
                    mainFrame.pathWalker.SetzeNeuenWeg(Phoelzer);
                    mainFrame.repaint();
                    return;
                }

                // Kurotwy1 mitnehmen
                if (kurotwy1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 600;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pkurotwy1);
                    mainFrame.repaint();
                    return;
                }

                // Kurotwy2 mitnehmen
                if (kurotwy2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 600;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pkurotwy2);
                    mainFrame.repaint();
                    return;
                }

                // Wokno1 mitnehmen
                if (wokno1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 610;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwokno1);
                    mainFrame.repaint();
                    return;
                }

                // Wokno2 mitnehmen
                if (wokno2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 610;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pwokno2);
                    mainFrame.repaint();
                    return;
                }

                // Sekera mitnehmen
                if (sekeraRect.IsPointInRect(pTemp)) {
                    nextActionID = 620;
                    mainFrame.pathWalker.SetzeNeuenWeg(Psekera);
                    mainFrame.repaint();
                    return;
                }

                // Durje mitnehmen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 630;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pdurje);
                    mainFrame.repaint();
                    return;
                }

                // Drjewo mitnehmen
                if (drjewoRect.IsPointInRect(pTemp)) {
                    nextActionID = 640;
                    mainFrame.pathWalker.SetzeNeuenWeg(Pdrjewo);
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
        // Wenn Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollX, pTxxx.y + mainFrame.scrollY);

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.isInventoryCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            BorderRect tmp = mainFrame.krabat.getRect();
            mainFrame.isInventoryHighlightCursor = tmp.IsPointInRect(pTemp) ||
                    jamaRect.IsPointInRect(pTemp) || leineRect.IsPointInRect(pTemp) ||
                    hoelzerRect.IsPointInRect(pTemp) ||
                    kurotwy1Rect.IsPointInRect(pTemp) || kurotwy2Rect.IsPointInRect(pTemp) ||
                    wokno1Rect.IsPointInRect(pTemp) || wokno2Rect.IsPointInRect(pTemp) ||
                    sekeraRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp) ||
                    drjewoRect.IsPointInRect(pTemp);

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

            if (jamaRect.IsPointInRect(pTemp) || leineRect.IsPointInRect(pTemp) ||
                    hoelzerRect.IsPointInRect(pTemp) ||
                    kurotwy1Rect.IsPointInRect(pTemp) || kurotwy2Rect.IsPointInRect(pTemp) ||
                    wokno1Rect.IsPointInRect(pTemp) || wokno2Rect.IsPointInRect(pTemp) ||
                    sekeraRect.IsPointInRect(pTemp) || durjeRect.IsPointInRect(pTemp) ||
                    drjewoRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.cursorCross);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.cursorUp);
                    Cursorform = 4;
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

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {

        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
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
            case 2:
                // Jama anschauen
                KrabatSagt("Hojnt2_1", fJama, 3, 0, 0);
                break;

            case 3:
                // Leine anschauen
                KrabatSagt("Hojnt2_2", fLeine, 3, 0, 0);
                break;

            case 4:
                // Hoelzer anschauen
                KrabatSagt("Hojnt2_3", fHoelzer, 3, 0, 0);
                break;

            case 5:
                // Kurotwy anschauen
                KrabatSagt("Hojnt2_4", fKurotwy, 3, 0, 0);
                break;

            case 6:
                // Wokno anschauen
                KrabatSagt("Hojnt2_5", fWokno, 3, 0, 0);
                break;

            case 7:
                // Sekera anschauen
                KrabatSagt("Hojnt2_6", fSekera, 3, 0, 0);
                break;

            case 8:
                // Durje anschauen
                KrabatSagt("Hojnt2_7", fDurje, 3, 0, 0);
                break;

            case 9:
                // Drjewo anschauen
                KrabatSagt("Hojnt2_8", fDrjewo, 3, 0, 0);
                break;

            case 53:
                // Leine mitnehmen
                KrabatSagt("Hojnt2_9", fLeine, 3, 0, 0);
                break;

            case 55:
                // Jama mitnehmen
                KrabatSagt("Hojnt2_10", fJama, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Njedz
                NeuesBild(80, 73);
                break;

            case 101:
                // Gehe zu Wjes
                NeuesBild(87, 73);
                break;

            case 150:
                // Standard - Ausreden fuer Jama
                DingAusrede(fJama);
                break;

            case 165:
                // Leine - Ausreden
                DingAusrede(fLeine);
                break;

            case 166:
                // Hoelzer - Ausreden
                DingAusrede(fHoelzer);
                break;

            case 190:
                // Ausrede, wenn vor dem Jaegerhaus die Hoelzer benutzt werden sollen
                KrabatSagt("Hojnt2_11", fHoelzer, 3, 0, 0);
                break;

            case 420:
                // Rebhuehner - Ausreden
                DingAusrede(fKurotwy);
                break;

            case 430:
                // Wokno - Ausreden
                DingAusrede(fWokno);
                break;

            case 440:
                // Sekera - Ausreden
                DingAusrede(fSekera);
                break;

            case 450:
                // Durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 460:
                // Drjewo - Ausreden
                DingAusrede(fDrjewo);
                break;

            case 600:
                // Kurotwy mitnehmen
                KrabatSagt("Hojnt2_12", fKurotwy, 3, 0, 0);
                break;

            case 610:
                // Wokno mitnehmen
                KrabatSagt("Hojnt2_13", fWokno, 3, 0, 0);
                break;

            case 620:
                // Sekera mitnehmen
                KrabatSagt("Hojnt2_14", fSekera, 3, 0, 0);
                break;

            case 630:
                // Durje mitnehmen
                KrabatSagt("Hojnt2_15", fDurje, 3, 0, 0);
                break;

            case 640:
                // Drjewo mitnehmen
                KrabatSagt("Hojnt2_16", fDrjewo, 3, 0, 0);
                break;

            case 660:
                // kij auf lajna, wenn haken schon genommen
                KrabatSagt("Hojnt2_17", fLeine, 3, 0, 0);
                break;

            case 670:
                // kamuski und bron auf wokno
                KrabatSagt("Hojnt2_18", fWokno, 3, 0, 0);
                break;

            case 680:
                // kamuski auf drjewo
                KrabatSagt("Hojnt2_19", fDrjewo, 3, 0, 0);
                break;

            case 1000:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 110);  // 68 - 100 - scaleMueller
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
                NeuesBild(90, 73);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}