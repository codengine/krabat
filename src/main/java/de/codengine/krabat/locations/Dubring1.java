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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Dubring1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Dubring1.class);
    private GenericImage backl;
    private GenericImage backr;
    private GenericImage sky;
    private GenericImage vorder;
    private boolean setScroll = false;
    private int scrollwert;

    // Konstanten - Rects deklarieren
    private static final Borderrect linkerAusgang = new Borderrect(0, 316, 35, 398);
    private static final Borderrect rechterAusgang = new Borderrect(1246, 305, 1279, 393);
    private static final Borderrect halza1Rect = new Borderrect(490, 204, 540, 290);
    private static final Borderrect halza2Rect = new Borderrect(849, 259, 892, 320);
    // private static final borderrect stomyRect      = new borderrect (   0,   0, 1279, 200);
    private static final Borderrect trawaRect = new Borderrect(1000, 350, 1279, 479);

    // alle Schlamm-Moeglichkeiten registrieren
    private static final Borderrect schlamm1 = new Borderrect(40, 302, 143, 332);
    private static final GenericPoint Pschlamm1 = new GenericPoint(76, 358);
    private static final Borderrect schlamm2 = new Borderrect(0, 406, 145, 479);
    private static final GenericPoint Pschlamm2 = new GenericPoint(75, 381);
    private static final Borderrect schlamm3 = new Borderrect(251, 302, 660, 333);
    private static final GenericPoint Pschlamm3 = new GenericPoint(431, 350);
    private static final Borderrect schlamm4 = new Borderrect(338, 395, 457, 435);
    private static final GenericPoint Pschlamm4 = new GenericPoint(399, 373);
    private static final Bordertrapez schlamm5 = new Bordertrapez(611, 731, 500, 620, 400, 479);
    private static final GenericPoint Pschlamm5 = new GenericPoint(685, 374);
    private static final Borderrect schlamm6 = new Borderrect(845, 292, 1279, 314);
    private static final GenericPoint Pschlamm6 = new GenericPoint(1154, 365);
    private static final Borderrect schlamm7 = new Borderrect(837, 396, 985, 455);
    private static final GenericPoint Pschlamm7 = new GenericPoint(923, 379);
    private static final Borderrect schlamm8 = new Borderrect(1083, 423, 1279, 479);
    private static final GenericPoint Pschlamm8 = new GenericPoint(1196, 397);

    private static final GenericPoint Pleft = new GenericPoint(0, 361);
    private static final GenericPoint Pright = new GenericPoint(1279, 340);
    private static final GenericPoint Phalza1 = new GenericPoint(490, 349);
    private static final GenericPoint Phalza2 = new GenericPoint(862, 336);

    // Konstante ints
    private static final int fSchlammVorn = 6;
    private static final int fSchlammHinten = 12;
    private static final int fHalza = 12;
    private static final int fStomy = 12;


    public Dubring1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(9, true);

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 7f;
        mainFrame.krabat.defScale = -0;

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                break;
            case 16: // Aus Villa kommend
                mainFrame.krabat.setPos(new GenericPoint(20, 365));
                mainFrame.krabat.SetFacing(3);
                scrollwert = 0;
                setScroll = true;
                break;
            case 22: // Von Cyrkej aus
                mainFrame.krabat.setPos(new GenericPoint(1262, 344));
                mainFrame.krabat.SetFacing(9);
                scrollwert = 640;
                setScroll = true;
                break;
        }

        InitLocation();
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {

        mainFrame.wegGeher.vBorders.removeAllElements();

        // Grenzen setzen
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 48, 0, 144, 352, 373));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(72, 321, 167, 272, 374, 390));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(300, 506, 213, 506, 360, 373));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(507, 363, 571, 381));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(572, 956, 572, 1113, 357, 373));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(740, 810, 652, 957, 339, 356));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(900, 1251, 1082, 1206, 374, 403));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(1278, 1279, 1148, 1279, 326, 373));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(8);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(4, 6);
        mainFrame.wegSucher.PosVerbinden(6, 7);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backl = getPicture("gfx/dubring/dubr-l3.gif");
        backr = getPicture("gfx/dubring/dubr-r3.gif");
        sky = getPicture("gfx/dubring/dubrsky.gif");
        vorder = getPicture("gfx/dubring/dtrawa.gif");

    }

    @Override
    public void cleanup() {
        backl = null;
        backr = null;
        sky = null;
        vorder = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollx = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            g.setClip(0, 0, 1284, 964);
            mainFrame.isAnim = true;
        }

        // Hintergrund zeichnen
        g.drawImage(sky, mainFrame.scrollx / 2, 0);
        g.drawImage(backl, 0, 0);
        g.drawImage(backr, 640, 0);

        // Parallaxer ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 241);
            g.drawImage(sky, mainFrame.scrollx / 2, 0);
            g.drawImage(backl, 0, 0);
            g.drawImage(backr, 640, 0);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Krabats neue Position festlegen wenn noetig
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
                        // Krabat steht nur da
                        mainFrame.krabat.drawKrabat(g);
                        break;
                }
            }
            // Rumstehen oder Laufen
            else {
                mainFrame.krabat.drawKrabat(g);
            }
        }

        // Steht Krabat hinter dem Gras rechts unten ????
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (trawaRect.IsPointInRect(pKrTemp)) {
            g.drawImage(vorder, 1061, 377);
        }

        // Ab hier muss Cliprect wieder gerettet werden
        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
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

        // Gibt es was zu tun ? Achtung! Scrolling fuer DoAction jeweils extra abfangen!
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
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }

        // Cursorpunkt mit Scrolloffset berechnen
        GenericPoint pTemp = e.getPoint();
        pTemp.x += mainFrame.scrollx;

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
            // linke Maustaste
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Bloto1
                if (schlamm1.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 150;
                            break;
                        case 14: // ryba
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 200;
                            break;
                    }
                    pTemp = Pschlamm1;
                }

                // Ausreden fuer Bloto2
                if (schlamm2.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 155;
                            break;
                        case 14: // ryba
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 205;
                            break;
                    }
                    pTemp = Pschlamm2;
                }

                // Ausreden fuer Bloto3
                if (schlamm3.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 150;
                            break;
                        case 14: // ryba
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 200;
                            break;
                    }
                    pTemp = Pschlamm3;
                }

                // Ausreden fuer Bloto4
                if (schlamm4.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 155;
                            break;
                        case 14: // ryba
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 205;
                            break;
                    }
                    pTemp = Pschlamm4;
                }

                // Ausreden fuer Bloto5
                if (schlamm5.PointInside(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 155;
                            break;
                        case 14: // ryba
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 205;
                            break;
                    }
                    pTemp = Pschlamm5;
                }

                // Ausreden fuer Bloto6
                if (schlamm6.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 150;
                            break;
                        case 14: // ryba
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 200;
                            break;
                    }
                    pTemp = Pschlamm6;
                }

                // Ausreden fuer Bloto7
                if (schlamm7.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 155;
                            break;
                        case 14: // ryba
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 205;
                            break;
                    }
                    pTemp = Pschlamm7;
                }

                // Ausreden fuer Bloto8
                if (schlamm8.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 4: // honck
                            nextActionID = 155;
                            break;
                        case 14: // ryba
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 205;
                            break;
                    }
                    pTemp = Pschlamm8;
                }

                // Ausreden fuer Halza1
                if (halza1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = Phalza1;
                }

                // Ausreden fuer Halza2
                if (halza2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 160;
                    pTemp = Phalza2;
                }

                // Ausreden fuer Stomy, kein spezieller Punkt, aber hinlaufen
        /*if (stomyRect.IsPointInRect (pTemp) == true)
        {
        	nextActionID = 165;
        }	*/

                // wenn nix ausgewaehlt, dann einfach nur hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            }

            // rechte Maustaste
            else {
                // Gegenstand grundsaetzlich wieder ablegen
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

                // nach Villa gehen
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

                // nach Cyrkej gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!rechterAusgang.IsPointInRect(kt)) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schlamm1 ansehen
                if (schlamm1.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pschlamm1;
                }

                // Schlamm2 ansehen
                if (schlamm2.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschlamm2;
                }

                // Schlamm3 ansehen
                if (schlamm3.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pschlamm3;
                }

                // Schlamm4 ansehen
                if (schlamm4.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschlamm4;
                }

                // Schlamm5 ansehen
                if (schlamm5.PointInside(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschlamm5;
                }

                // Schlamm6 ansehen
                if (schlamm6.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pschlamm6;
                }

                // Schlamm7 ansehen
                if (schlamm7.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschlamm7;
                }

                // Schlamm8 ansehen
                if (schlamm8.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pschlamm8;
                }

                // Halza1 ansehen
                if (halza1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Phalza1;
                }

                // Halza2 ansehen
                if (halza2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Phalza2;
                }

                // Stomy ansehen, kein spezieller Punkt
        /*if (halza2Rect.IsPointInRect (pTemp) == true)
        {
          nextActionID = 4;
        }*/

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weg nach Cyrkej anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Villa anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Schlamm1 mitnehmen
                if (schlamm1.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm1);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm2 mitnehmen
                if (schlamm2.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm2);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm3 mitnehmen
                if (schlamm3.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm3);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm4 mitnehmen
                if (schlamm4.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm4);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm5 mitnehmen
                if (schlamm5.PointInside(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm5);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm6 mitnehmen
                if (schlamm6.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm6);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm7 mitnehmen
                if (schlamm7.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm7);
                    mainFrame.repaint();
                    return;
                }

                // Schlamm8 mitnehmen
                if (schlamm8.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschlamm8);
                    mainFrame.repaint();
                    return;
                }

                // Halza 1 mitnehmen
                if (halza1Rect.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(Phalza1);
                    mainFrame.repaint();
                    return;
                }

                // Halza 2 mitnehmen
                if (halza2Rect.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(Phalza2);
                    mainFrame.repaint();
                    return;
                }

                // Stomy mitnehmen
        /*if (stomyRect.IsPointInRect (pTemp) == true)
        {
          nextActionID = 65;
          mainFrame.repaint();
          return;
        }*/

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();

            }
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.fPlayAnim || mainFrame.krabat.nAnimation != 0) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.getRect();
            // (stomyRect.IsPointInRect (pTemp) == true))
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || schlamm1.IsPointInRect(pTemp) ||
                    schlamm2.IsPointInRect(pTemp) || schlamm3.IsPointInRect(pTemp) ||
                    schlamm4.IsPointInRect(pTemp) || schlamm5.PointInside(pTemp) ||
                    schlamm6.IsPointInRect(pTemp) || schlamm7.IsPointInRect(pTemp) ||
                    schlamm8.IsPointInRect(pTemp) ||
                    halza1Rect.IsPointInRect(pTemp) || halza2Rect.IsPointInRect(pTemp);

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
            if (schlamm1.IsPointInRect(pTemp) ||
                    schlamm2.IsPointInRect(pTemp) || schlamm3.IsPointInRect(pTemp) ||
                    schlamm4.IsPointInRect(pTemp) || schlamm5.PointInside(pTemp) ||
                    schlamm6.IsPointInRect(pTemp) || schlamm7.IsPointInRect(pTemp) ||
                    schlamm8.IsPointInRect(pTemp) ||
                    halza1Rect.IsPointInRect(pTemp) || halza2Rect.IsPointInRect(pTemp))
            // (stomyRect.IsPointInRect (pTemp) == true))
            {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 2;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
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

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
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
            case 1:
                // Schlamm anschauen nach hinten
                KrabatSagt("Dubring1_1", fSchlammHinten, 3, 0, 0);
                break;

            case 2:
                // Schlamm anschauen nach vorn
                KrabatSagt("Dubring1_2", fSchlammVorn, 3, 0, 0);
                break;

            case 3:
                // Halzy 1 und 2 anschauen
                KrabatSagt("Dubring1_3", fHalza, 3, 0, 0);
                break;

            case 4:
                // Stomy anschauen
                KrabatSagt("Dubring1_4", fStomy, 3, 0, 0);
                break;

            case 50:
                // Schlamm mitnehmen nach hinten
                KrabatSagt("Dubring1_5", fSchlammHinten, 3, 0, 0);
                break;

            case 55:
                // Schlamm mitnehmen nach vorn
                KrabatSagt("Dubring1_6", fSchlammVorn, 3, 0, 0);
                break;

            case 60:
                // Halzy 1 und 2 mitnehmen
                KrabatSagt("Dubring1_7", fHalza, 3, 0, 0);
                break;

	/*      case 65:
        // Stomy mitnehmen
        KrabatSagt (Start.stringManager.getTranslation("Loc1_Dubring1_00021"), Start.stringManager.getTranslation("Loc1_Dubring1_00022"), Start.stringManager.getTranslation("Loc1_Dubring1_00023"),
        						fStomy, 3, 0, 0);
        break;*/

            case 100:
                // Gehe zu Villa
                NeuesBild(16, 18);
                break;

            case 101:
                // gehe zu Cyrkej
                NeuesBild(22, 18);
                break;

            case 150:
                // Bloto schoepfen nach hinten schauen
                nextActionID = 0;
                mainFrame.wave.PlayFile("sfx/bloto1.wav");
                mainFrame.invCursor = false;
                mainFrame.krabat.SetFacing(fSchlammHinten);
                mainFrame.krabat.nAnimation = 143;
                mainFrame.inventory.vInventory.addElement(16);
                mainFrame.inventory.vInventory.removeElement(4);
                mainFrame.Actions[916] = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                break;

            case 155:
                // Bloto schoepfen nach vorne schauen
                nextActionID = 0;
                mainFrame.wave.PlayFile("sfx/bloto1.wav");
                mainFrame.invCursor = false;
                mainFrame.krabat.SetFacing(fSchlammVorn);
                mainFrame.krabat.nAnimation = 144;
                mainFrame.inventory.vInventory.addElement(16);
                mainFrame.inventory.vInventory.removeElement(4);
                mainFrame.Actions[916] = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                break;

            case 160:
                // Ausreden fuer halzy 1 und 2
                DingAusrede(fHalza);
                break;

            case 165:
                // Ausreden fuer Stomy
                DingAusrede(fStomy);
                break;

            case 200:
                // Schlamm - Ausreden nach hinten schauen
                KrabatSagt("Dubring1_8", fSchlammHinten, 3, 0, 0);
                break;

            case 205:
                // Schlamm - Ausreden nach vorne schauen
                KrabatSagt("Dubring1_9", fSchlammVorn, 3, 0, 0);
                break;

            case 210:
                // fisch auf Schlamm - Ausreden nach hinten schauen
                KrabatSagt("Dubring1_10", fSchlammHinten, 3, 0, 0);
                break;

            case 215:
                // fisch auf Schlamm - Ausreden nach vorne schauen
                KrabatSagt("Dubring1_11", fSchlammVorn, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}