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
import de.codengine.krabat.anims.WikowarRybow;
import de.codengine.krabat.anims.WikowarZita;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Kulow2 extends Mainloc {
    private GenericImage backleft, backright, himmel, saeule, kulow2;
    private boolean setScroll = false;
    private int scrollwert;

    private WikowarZita haendler;
    private WikowarRybow fischer;
    private final GenericPoint rybowarTalk;
    // private borderrect rybowarRect;
    private boolean rybowarhoertzu = false;
    // private boolean rybowarsuchtzrawc = false;
    // private boolean octopussyerscheint = false;

    private final Multiple2 Dialog;

    private boolean wikowarhoertzu = false;
    private final Borderrect wikowarRect;
    private final GenericPoint wikowarTalk;

    // Konstanten - Rects
    private static final Borderrect untererAusgang = new Borderrect(0, 444, 330, 479);
    private static final Borderrect linkerAusgang = new Borderrect(343, 318, 395, 347);
    private static final Borderrect rechterAusgang = new Borderrect(1240, 407, 1279, 479);
    private static final Borderrect kulow2Rect = new Borderrect(343, 305, 558, 479);
    // private static final borderrect drybaRect      = new borderrect ( 721, 338,  783, 372);
    private static final Borderrect durjelRect = new Borderrect(194, 341, 215, 390);
    private static final Borderrect durjerRect = new Borderrect(865, 347, 898, 429);
    private static final Borderrect synoRect = new Borderrect(998, 407, 1043, 431);
    private static final Borderrect sudobjoRect = new Borderrect(1061, 411, 1090, 443);

    private static final Borderrect rybowarLookRect = new Borderrect(714, 343, 741, 384);

    // Konstante Points
    private static final GenericPoint Pwikowar = new GenericPoint(532, 467);
    private static final GenericPoint Prybowar = new GenericPoint(691, 453);
    private static final GenericPoint rybowarUp = new GenericPoint(701, 339);
    private static final GenericPoint Pleft = new GenericPoint(362, 371);
    private static final GenericPoint Pright = new GenericPoint(1279, 470);
    private static final GenericPoint Pdown = new GenericPoint(144, 479);
    // private static final GenericPoint Pdryba      = new GenericPoint ( 758, 458);
    private static final GenericPoint Pdurjel = new GenericPoint(204, 388);
    private static final GenericPoint Pdurjer = new GenericPoint(885, 421);
    private static final GenericPoint Psyno = new GenericPoint(1028, 443);
    private static final GenericPoint Psudobjo = new GenericPoint(1075, 442);
    private static final GenericPoint PwikZita = new GenericPoint(421, 366);
    private static final GenericPoint doorTalk = new GenericPoint(883, 200);

    // Konstante ints
    private static final int fWikowar = 9;
    private static final int fRybowar = 3;
    private static final int fDryba = 3;
    private static final int fGTuer = 12;
    private static final int fOTuer = 12;
    private static final int fSyno = 12;
    private static final int fSudobjo = 12;

    public Kulow2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(6, true);

        mainFrame.krabat.maxx = 475;
        mainFrame.krabat.zoomf = 1.54f;
        mainFrame.krabat.defScale = 0;

        haendler = new WikowarZita(mainFrame);
        fischer = new WikowarRybow(mainFrame);

        rybowarTalk = new GenericPoint();
        rybowarTalk.x = rybowarUp.x + (WikowarRybow.Breite / 2);
        rybowarTalk.y = rybowarUp.y - 100;

        // rybowarRect = new borderrect (rybowarUp.x, rybowarUp.y, rybowarUp.x + WikowarRybow.Breite, rybowarUp.y + WikowarRybow.Hoehe);

        wikowarTalk = new GenericPoint(PwikZita.x + (WikowarZita.Breite / 2), PwikZita.y - 50);

        wikowarRect = new Borderrect(PwikZita.x, PwikZita.y, PwikZita.x + WikowarZita.Breite, PwikZita.y + WikowarZita.Hoehe);

        Dialog = new Multiple2(mainFrame);

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                break;
            case 70: // Aus Cyrkej kommend
                mainFrame.krabat.SetKrabatPos(new GenericPoint(371, 371));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 51;
                setScroll = true;
                break;
            case 87: // Von Wjes aus ueber Skip (Karte)
                mainFrame.krabat.SetKrabatPos(new GenericPoint(144, 452));
                mainFrame.krabat.SetFacing(12);
                scrollwert = 0;
                setScroll = true;
                break;
            case 79: // Von Mertens aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(1258, 445));
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
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 455, 310, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(134, 355, 0, 310, 395, 454));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(370, 424, 318, 481, 371, 394));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(479, 623, 540, 623, 395, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(624, 655, 624, 761, 430, 461));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(624, 462, 813, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(814, 468, 959, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(881, 881, 840, 933, 443, 467));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(960, 468, 1279, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(1267, 1279, 985, 1279, 434, 467));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(1262, 1271, 1267, 1279, 421, 433));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(11);

        // Wege eintragen
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(3, 5);
        mainFrame.wegSucher.PosVerbinden(5, 6);
        mainFrame.wegSucher.PosVerbinden(6, 7);
        mainFrame.wegSucher.PosVerbinden(6, 8);
        mainFrame.wegSucher.PosVerbinden(8, 9);
        mainFrame.wegSucher.PosVerbinden(9, 10);
    }

    // Bilder vorbereiten
    private void InitImages() {
        backleft = getPicture("gfx/kulow/kulow-l.gif");
        backright = getPicture("gfx/kulow/kulow-r.gif");
        himmel = getPicture("gfx/kulow/kulsky.gif");
        saeule = getPicture("gfx/kulow/postsaeule.gif");
        kulow2 = getPicture("gfx/kulow/kulow2.gif");

    }

    @Override
    public void cleanup() {
        backleft = null;
        backright = null;
        himmel = null;
        saeule = null;
        kulow2 = null;

        haendler.cleanup();
        haendler = null;
        fischer.cleanup();
        fischer = null;
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

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            if (setScroll) {
                setScroll = false;
                mainFrame.scrollx = scrollwert;
            }
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.isAnim = true;
            g.setClip(0, 0, 1284, 964);
        }

        // Ryba wurde aufgehoben!!!!!!!!!
	/*if (mainFrame.krabat.fAnimHelper == true)
	  {
	  mainFrame.inventory.vInventory.addElement (new Integer (13));
	  mainFrame.Clipset = false; 
	  mainFrame.krabat.fAnimHelper = false;
	  mainFrame.Actions [913] = true;
	  }*/

        // Hintergrund zeichnen
        g.drawImage(himmel, (mainFrame.scrollx / 6), 0, null);
        g.drawImage(backleft, 0, 0, null);
        g.drawImage(backright, 640, 0, null);

        // Parallax - Scrolling ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 285);
            g.drawImage(himmel, (mainFrame.scrollx / 6), 0, null);
            g.drawImage(backleft, 0, 0, null);
            g.drawImage(backright, 640, 0, null);
        }

        // Parallaxer fuer Saeule, muss immer Hintergrund loeschen ?????
        float xtf = mainFrame.scrollx;
        xtf = 1180 - ((mainFrame.scrollx * 3) / 8);
        int xt = (int) xtf;
        g.setClip(xt - 2, 208, xt + 90, 479);
        g.drawImage(backright, 640, 0, null);

        // Hintergrund fuer Wikowar loeschen -> sonst loescht der Krabat
        g.setClip(PwikZita.x, PwikZita.y, WikowarZita.Breite, WikowarZita.Hoehe);
        g.drawImage(backleft, 0, 0, null);

        // Ab hier ist Retten des ClipRect sinnlos!!!

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Andere Personen zeichnen

        // Rybowar
        g.setClip(rybowarUp.x, rybowarUp.y, WikowarRybow.Breite, WikowarRybow.Hoehe);
        g.drawImage(backright, 640, 0, null);
        fischer.drawRybowar(g, TalkPerson, 0, rybowarUp, rybowarhoertzu, false, false, false, false);

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
            if ((mainFrame.talkCount > 0) && (TalkPerson != 0)) {
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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (kulow2Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(kulow2, 338, 321, null);
        }

        // Ab hier muss Cliprect wieder gerettet werden

        // Wikowar
        GenericRectangle mx;
        mx = g.getClipBounds();
        g.setClip(PwikZita.x, PwikZita.y, WikowarZita.Breite, WikowarZita.Hoehe);
        haendler.drawWikowar(g, PwikZita, TalkPerson, wikowarhoertzu);
        g.setClip(mx.getX(), mx.getY(), mx.getWidth(), mx.getHeight());

        // Postsaeule zeichnen, wenn im Bild
        // System.out.println (mainFrame.scrollx);
        if (mainFrame.scrollx > 320) // Diesen Wert bitte exakt !!
        {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(xt - 2, 208, xt + 90, 479);
            g.drawImage(saeule, xt, 208, null);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
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

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
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

        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

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
            if (e.isRightClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Wikowar
                if (wikowarRect.IsPointInRect(pTemp)) {
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
                    pTemp = Pwikowar;
                }

                // Ausreden fuer Rybowar
                if (rybowarLookRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 205;
                            break;
                        case 14: // ryba
                            nextActionID = 165;
                            break;
                        case 18: // bron
                            nextActionID = 215;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Prybowar;
                }

                // Ausreden fuer tuer links
                if (durjelRect.IsPointInRect(pTemp)) {
                    nextActionID = 170;
                    pTemp = Pdurjel;
                }

                // Ausreden fuer tuer rechts
                if (durjerRect.IsPointInRect(pTemp)) {
                    nextActionID = 175;
                    pTemp = Pdurjer;
                }

                // Ausreden fuer Stroh
                if (synoRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                            nextActionID = 230;
                            break;
                        default:
                            nextActionID = 180;
                            break;
                    }
                    pTemp = Psyno;
                }

                // Ausreden fuer Gefaess
                if (sudobjoRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 14: // ryba
                            nextActionID = 240;
                            break;
                        default:
                            nextActionID = 185;
                            break;
                    }
                    pTemp = Psudobjo;
                }

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
            if (e.isRightClick()) {
                // linke Maustaste
                nextActionID = 0;

                // nach Polo gehen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // nach Mertens gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

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

                // nach Cyrkej gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!linkerAusgang.IsPointInRect(kt)) {
                        pTemp = Pleft;
                    } else {
                        // Hier "up"
                        pTemp = new GenericPoint(kt.x, Pleft.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Wikowar ansehen
                if (wikowarRect.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pwikowar;
                }

                // Rybowar ansehen
                if (rybowarLookRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Prybowar;
                }

                // tuer links ansehen
                if (durjelRect.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = Pdurjel;
                }

                // tuer rechts ansehen
                if (durjerRect.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = Pdurjer;
                }

                // Stroh ansehen
                if (synoRect.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = Psyno;
                }

                // Gefaess ansehen
                if (sudobjoRect.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = Psudobjo;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weg nach Polo anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Mertens anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Weg nach Cyrkej anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Wikowar reden, nicht bei Backgroundanim
                if (wikowarRect.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwikowar);
                    mainFrame.repaint();
                    return;
                }

                // Mit dem Rybowar reden, nicht bei Backgroundanim
                if (rybowarLookRect.IsPointInRect(pTemp)) {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(Prybowar);
                    mainFrame.repaint();
                    return;
                }

                // Tuer links nehmen
                if (durjelRect.IsPointInRect(pTemp)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurjel);
                    mainFrame.repaint();
                    return;
                }

                // Tuer rechts nehmen
                if (durjerRect.IsPointInRect(pTemp)) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurjer);
                    mainFrame.repaint();
                    return;
                }

                // Stroh nehmen
                if (synoRect.IsPointInRect(pTemp)) {
                    nextActionID = 70;
                    mainFrame.wegGeher.SetzeNeuenWeg(Psyno);
                    mainFrame.repaint();
                    return;
                }

                // Gefaess nehmen
                if (sudobjoRect.IsPointInRect(pTemp)) {
                    nextActionID = 75;
                    mainFrame.wegGeher.SetzeNeuenWeg(Psudobjo);
                    mainFrame.repaint();
                    return;
                }

                // Inventarroutine aktivieren, wenn nichts anderes angeklickt ist
                nextActionID = 123;
                mainFrame.isAnim = false;
                mainFrame.krabat.StopWalking();
                mainFrame.repaint();

            }
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple) {
            Dialog.evalMouseMoveEvent(pTxxx);
            return;
        }

        // neuen Punkt erzeugen wg. Referenzgleichheit
        GenericPoint pTemp = new GenericPoint(pTxxx.x + mainFrame.scrollx, pTxxx.y + mainFrame.scrolly);

        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if ((mainFrame.fPlayAnim) || (mainFrame.krabat.nAnimation != 0)) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            mainFrame.invHighCursor = (tmp.IsPointInRect(pTemp)) ||
                    (wikowarRect.IsPointInRect(pTemp)) ||
                    (rybowarLookRect.IsPointInRect(pTemp)) ||
                    (durjelRect.IsPointInRect(pTemp)) || (durjerRect.IsPointInRect(pTemp)) ||
                    (synoRect.IsPointInRect(pTemp)) || (sudobjoRect.IsPointInRect(pTemp));

            if ((Cursorform != 10) && (!mainFrame.invHighCursor)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if ((rybowarLookRect.IsPointInRect(pTemp)) ||
                    (wikowarRect.IsPointInRect(pTemp)) ||
                    (durjelRect.IsPointInRect(pTemp)) || (durjerRect.IsPointInRect(pTemp)) ||
                    (synoRect.IsPointInRect(pTemp)) || (sudobjoRect.IsPointInRect(pTemp))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 5;
                }
                return;
            }

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
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
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        mainFrame.Clipset = false;
        mainFrame.isAnim = false;
        mainFrame.krabat.StopWalking();
    }

    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent(e);
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {

        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering) ||
                (mainFrame.krabat.isWalking)) {
            return;
        }


        // hier wird zu den Standardausreden von Krabat verzweigt, wenn noetig (in Superklasse)
        if ((nextActionID > 499) && (nextActionID < 600)) {
            setKrabatAusrede();

            // manche Ausreden erfordern neuen Cursor !!!

            evalMouseMoveEvent(mainFrame.Mousepoint);

            return;
        }

        // Hier Evaluation der Screenaufrufe, in Superklasse
        if ((nextActionID > 119) && (nextActionID < 129)) {
            SwitchScreen();
            return;
        }

        // Was soll Krabat machen ?
        switch (nextActionID) {
            case 1:
                // Wikowar anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00000"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00001"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00002"),
                        fWikowar, 3, 0, 0);
                break;

            case 2:
                // Rybowar anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00003"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00004"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00005"),
                        fRybowar, 3, 0, 0);
                break;

            case 4:
                // geschlossene Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00006"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00007"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00008"),
                        fGTuer, 3, 0, 0);
                break;

            case 5:
                // offene Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00009"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00010"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00011"),
                        fOTuer, 3, 0, 0);
                break;

            case 6:
                // Stroh anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00012"), Start.stringManager.getTranslation("Loc2_Kulow2_00013"), Start.stringManager.getTranslation("Loc2_Kulow2_00014"),
                        fSyno, 3, 0, 0);
                break;

            case 7:
                // Gefaess anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00015"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00016"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00017"),
                        fSudobjo, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Wikowar benutzen)
                mainFrame.krabat.SetFacing(fWikowar);
                if (mainFrame.isScrolling) {
                    return;
                }
                wikowarhoertzu = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 600;
                break;

            case 51:
                // Krabat beginnt MC (Rybowar benutzen)
                mainFrame.krabat.SetFacing(fRybowar);
                if (mainFrame.isScrolling) {
                    return;
                }
                rybowarhoertzu = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 900;
                break;

            case 60:
                // geschl. Tuer benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00018"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00019"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00020"),
                        fGTuer, 3, 0, 0);
                break;

            case 65:
                // offene Tuer benutzen
                // Zuffzahl von 0 bis 2
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00021"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00022"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00023"),
                                fOTuer, 52, 0, 0, doorTalk);
                        break;

                    case 1:
                        PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00024"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00025"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00026"),
                                fOTuer, 52, 0, 0, doorTalk);
                        break;

                    case 2:
                        PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00027"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00028"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00029"),
                                fOTuer, 52, 0, 0, doorTalk);
                        break;
                }
                break;

            case 70:
                // Stroh benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00030"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00031"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00032"),
                        fSyno, 3, 0, 0);
                break;

            case 75:
                // Gefaess benutzen
                int zuffZahl = (int) Math.round(Math.random() * 2);
                if (zuffZahl == 2) {
                    zuffZahl = 0;
                }
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00033"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00034"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00035"),
                                fSudobjo, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00036"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00037"),
                                Start.stringManager.getTranslation("Loc2_Kulow2_00038"),
                                fSudobjo, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // Karte einblenden
                mainFrame.ConstructLocation(106);
                mainFrame.isAnim = false;
                mainFrame.whatScreen = 6;
                nextActionID = 0;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 101:
                // Gehe zu Mertens
                NeuesBild(79, 76);
                break;

            case 102:
                // Gehe zu Cyrkej
                NeuesBild(70, 76);
                break;

            case 150:
                // Wikowar - Ausreden
                MPersonAusrede(fWikowar);
                break;

            case 155:
                // Rybowar - Ausreden
                MPersonAusrede(fRybowar);
                break;

            case 170:
                // Durjeleft - Ausreden
                DingAusrede(fGTuer);
                break;

            case 175:
                // Durjeright - Ausreden
                DingAusrede(fOTuer);
                break;

            case 180:
                // Stroh - Ausreden
                DingAusrede(fSyno);
                break;

            case 185:
                // Gefaess - Ausreden
                DingAusrede(fSudobjo);
                break;

            case 200:
                // kij auf wikowar
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00039"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00040"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00041"),
                        fWikowar, 3, 0, 0);
                break;

            case 205:
                // kij auf rybowar
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00042"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00043"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00044"),
                        fRybowar, 3, 0, 0);
                break;

            case 210:
                // bron auf wikowar
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00045"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00046"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00047"),
                        fWikowar, 3, 0, 0);
                break;

            case 215:
                // bron auf rybowar
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00048"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00049"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00050"),
                        fRybowar, 3, 0, 0);
                break;

            case 220:
                // kamuski auf dryba
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00051"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00052"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00053"),
                        fDryba, 3, 0, 0);
                break;

            case 230:
                // kamuski auf syno
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00054"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00055"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00056"),
                        fSyno, 3, 0, 0);
                break;

            case 240:
                // ryba auf sudobjo
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00057"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00058"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00059"),
                        fSudobjo, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine mit Wikowar
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00060"), 1000, 120, new int[]{120}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00061"), 120, 121, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00062"), 1000, 122, new int[]{122}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00063"), 122, 123, new int[]{123}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00064"), 123, 1000, null, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00065"), 1000, 125, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00066"), 125, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00067"), 1000, 120, new int[]{120}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00068"), 120, 121, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00069"), 1000, 122, new int[]{122}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00070"), 122, 123, new int[]{123}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00071"), 123, 1000, null, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00072"), 1000, 125, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00073"), 125, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00074"), 1000, 120, new int[]{120}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00075"), 120, 121, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00076"), 1000, 122, new int[]{122}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00077"), 122, 123, new int[]{123}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00078"), 123, 1000, null, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00079"), 1000, 125, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00080"), 125, 1000, null, 800);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[125] = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 610:
                // Reaktion Wikowar auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00081"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00082"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00083"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 620:
                // Reaktion Wikowar auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00084"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00085"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00086"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 630:
                // Reaktion Wikowar auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00087"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00088"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00089"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 640:
                // Reaktion Wikowar auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00090"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00091"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00092"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 650:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00093"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00094"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00095"),
                        0, 32, 2, 651, wikowarTalk);
                break;

            case 651:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00096"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00097"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00098"),
                        0, 32, 2, 652, wikowarTalk);
                break;

            case 652:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00099"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00100"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00101"),
                        0, 32, 2, 653, wikowarTalk);
                break;

            case 653:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00102"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00103"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00104"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.Actions[125] = false;
                mainFrame.fPlayAnim = false;
                wikowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 900:
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00105"), 1000, 130, new int[]{130}, 910);
                    if (!mainFrame.Actions[150]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00106"), 130, 1000, null, 920);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00107"), 130, 1000, null, 930);
                    }

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00108"), 1000, 134, null, 1100);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00109"), 134, 1000, null, 1100);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00110"), 1000, 130, new int[]{130}, 910);
                    if (!mainFrame.Actions[150]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00111"), 130, 1000, null, 920);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00112"), 130, 1000, null, 930);
                    }

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00113"), 1000, 134, null, 1100);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00114"), 134, 1000, null, 1100);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00115"), 1000, 130, new int[]{130}, 910);
                    if (!mainFrame.Actions[150]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00116"), 130, 1000, null, 920);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00117"), 130, 1000, null, 930);
                    }

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00118"), 1000, 134, null, 1100);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Kulow2_00119"), 134, 1000, null, 1100);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 901;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 901:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[134] = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 910:
                // Reaktion Rybowar auf 1. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00120"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00121"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00122"),
                        0, 33, 2, 900, rybowarTalk);
                break;

            case 920:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K noch nicht in Haty)
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00123"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00124"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00125"),
                        0, 33, 2, 900, rybowarTalk);
                break;

            case 930:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K in Haty gewesen)
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00126"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00127"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00128"),
                        0, 33, 2, 931, rybowarTalk);
                break;

            case 931:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K in Haty gewesen)
                PersonSagt(Start.stringManager.getTranslation("Loc2_Kulow2_00129"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00130"),
                        Start.stringManager.getTranslation("Loc2_Kulow2_00131"),
                        0, 33, 2, 900, rybowarTalk);
                break;

            case 1100:
                // MC beenden, wenn zuende gelabert...
                mainFrame.Actions[134] = false;
                mainFrame.fPlayAnim = false;
                rybowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }
}  				