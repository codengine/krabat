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
import de.codengine.krabat.anims.WikowarRybow;
import de.codengine.krabat.anims.WikowarZita;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Kulow1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Kulow1.class);
    private GenericImage backleft;
    private GenericImage backright;
    private GenericImage himmel;
    private GenericImage saeule;
    private GenericImage kulow2;
    private GenericImage ryba;
    private boolean setScroll = false;
    private int scrollwert;

    private WikowarZita haendler;
    private WikowarRybow fischer;
    private final Multiple2 Dialog;

    private final GenericPoint rybowarTalk;
    // private borderrect rybowarRect;
    private boolean rybowarhoertzu = false;
    private boolean rybowargibtkrosik = false;
    private boolean rybowarsuchtzrawc = false;
    private boolean octopussyerscheint = false;
    private boolean rybowarnimmtfisch = false;

    private boolean wikowarhoertzu = false;
    private final Borderrect wikowarRect;
    private final GenericPoint wikowarTalk;

    private String AnimOutputText = "";
    private GenericPoint AnimOutputTextPos;
    private int AnimTalkPerson = 0;
    private int AnimID = 0;
    private int AnimCounter = 0;
    private boolean AnimActive = false;
    private boolean AnimLocked = false;

    private int Counter = 0;

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
    private static final Borderrect drybaLookRect = new Borderrect(723, 344, 777, 366);

    // Konstante Points
    private static final GenericPoint Pwikowar = new GenericPoint(532, 467);
    private static final GenericPoint Prybowar = new GenericPoint(691, 453);
    private static final GenericPoint rybowarUp = new GenericPoint(701, 339);
    private static final GenericPoint Pleft = new GenericPoint(362, 371);
    private static final GenericPoint Pright = new GenericPoint(1279, 470);
    private static final GenericPoint Pdown = new GenericPoint(144, 479);
    private static final GenericPoint Pdryba = new GenericPoint(758, 458);
    private static final GenericPoint Pdurjel = new GenericPoint(204, 388);
    private static final GenericPoint Pdurjer = new GenericPoint(885, 421);
    private static final GenericPoint Psyno = new GenericPoint(1028, 443);
    private static final GenericPoint Psudobjo = new GenericPoint(1075, 442);
    private static final GenericPoint PwikZita = new GenericPoint(421, 366);
    private static final GenericPoint doorTalk = new GenericPoint(883, 200);

    // Konstante ints
    private static final int fWikowar = 9;
    private static final int fRybowar = 3;
    private static final int fDryba = 12;
    private static final int fGTuer = 12;
    private static final int fOTuer = 12;
    private static final int fSyno = 12;
    private static final int fSudobjo = 12;

    public Kulow1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.komme_von_karte = false;  // hier ohne Bedeutung, Kulow stimmt immer neu an
        BackgroundMusicPlayer.getInstance().playTrack(6, true);

        mainFrame.krabat.maxx = 470;
        mainFrame.krabat.zoomf = 1.67f;
        mainFrame.krabat.defScale = 0;

        haendler = new WikowarZita(mainFrame);
        fischer = new WikowarRybow(mainFrame);

        rybowarTalk = new GenericPoint();
        rybowarTalk.x = rybowarUp.x + WikowarRybow.Breite / 2;
        rybowarTalk.y = rybowarUp.y - 100;

        // rybowarRect = new borderrect (rybowarUp.x, rybowarUp.y, rybowarUp.x + fischer.Breite, rybowarUp.y + fischer.Hoehe);

        wikowarTalk = new GenericPoint(PwikZita.x + WikowarZita.Breite / 2, PwikZita.y - 50);

        wikowarRect = new Borderrect(PwikZita.x, PwikZita.y, PwikZita.x + WikowarZita.Breite, PwikZita.y + WikowarZita.Hoehe);

        Dialog = new Multiple2(mainFrame);

        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird

        switch (oldLocation) {
            case 0: // Einsprung von Load
                break;
            case 22: // Aus Cyrkej kommend
                mainFrame.krabat.setPos(new GenericPoint(371, 371));
                mainFrame.krabat.SetFacing(6);
                scrollwert = 51;
                setScroll = true;
                break;
            case 11: // Von Polo aus
                mainFrame.krabat.setPos(new GenericPoint(144, 452));
                mainFrame.krabat.SetFacing(12);
                scrollwert = 0;
                setScroll = true;
                break;
            case 20: // Von Mertens aus
                mainFrame.krabat.setPos(new GenericPoint(1258, 445));
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
        ryba = getPicture("gfx/kulow/ddryba2a.gif");

    }

    @Override
    public void cleanup() {
        backleft = null;
        backright = null;
        himmel = null;
        saeule = null;
        kulow2 = null;
        ryba = null;

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

        // Clipping - Region initialisieren
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
            AnimLocked = false;
        }

        // Hintergrund zeichnen
        g.drawImage(himmel, mainFrame.scrollx / 6, 0);
        g.drawImage(backleft, 0, 0);
        g.drawImage(backright, 640, 0);

        // Parallax - Scrolling ausfuehren
        if (mainFrame.isScrolling) {
            int xtemp = mainFrame.scrollx - 5;
            if (xtemp < 0) {
                xtemp = 0;
            }
            g.setClip(xtemp, 0, 650, 285);
            g.drawImage(himmel, mainFrame.scrollx / 6, 0);
            g.drawImage(backleft, 0, 0);
            g.drawImage(backright, 640, 0);
        }

        // Parallaxer fuer Saeule, muss immer Hintergrund loeschen ?????
        float xtf = 1180 - (float) (mainFrame.scrollx * 3) / 8;
        int xt = (int) xtf;
        g.setClip(xt - 2, 208, xt + 90, 479);
        g.drawImage(backright, 640, 0);

        // Hintergrund fuer Wikowar loeschen -> sonst loescht der Krabat
        g.setClip(PwikZita.x, PwikZita.y, WikowarZita.Breite, WikowarZita.Hoehe);
        g.drawImage(backleft, 0, 0);

        // Ab hier ist Retten des ClipRect sinnlos!!!

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Andere Personen zeichnen

        // Rybowar
        g.setClip(rybowarUp.x, rybowarUp.y, WikowarRybow.Breite + 50, WikowarRybow.Hoehe);
        g.drawImage(backright, 640, 0);
        if (!AnimLocked) {
            fischer.drawRybowar(g, TalkPerson, AnimTalkPerson, rybowarUp,
                    rybowarhoertzu, rybowargibtkrosik, rybowarsuchtzrawc,
                    octopussyerscheint, rybowarnimmtfisch);
        } else {
            fischer.drawRybowar(g, TalkPerson, 0, rybowarUp,
                    rybowarhoertzu, rybowargibtkrosik, rybowarsuchtzrawc,
                    octopussyerscheint, rybowarnimmtfisch);
        }

        if (octopussyerscheint) {
            octopussyerscheint = false;
        }

        // Wenn Holzfisch noch da, dann auch zeichnen
        if (!mainFrame.Actions[913]) {
            g.setClip(721, 338, 62, 34);
            g.drawImage(ryba, 721, 338);
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

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (kulow2Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(kulow2, 338, 321);
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
            g.drawImage(saeule, xt, 208);
            g.setClip(may.getX(), may.getY(), may.getWidth(), may.getHeight());
        }

        // Ausgabe von AnimText, falls noetig
        if (!Objects.equals(AnimOutputText, "") && AnimActive && !AnimLocked) {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 484);
            mainFrame.ifont.drawString(g, AnimOutputText, AnimOutputTextPos.x, AnimOutputTextPos.y, FarbenArray[AnimTalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        // Die Anims muessen bedient werden
        if (AnimID != 0 && AnimActive && !AnimLocked) {
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
            if (e.isLeftClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.getRect();

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
                if (rybowarLookRect.IsPointInRect(pTemp) && !AnimActive)
                    /*((drybaRect.IsPointInRect (pTemp) == false) || (mainFrame.Actions[913] == true)))*/ {
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

                // Ausreden fuer Dryba
                if (drybaLookRect.IsPointInRect(pTemp) && !mainFrame.Actions[913] &&
                        (!rybowarLookRect.IsPointInRect(pTemp) || AnimActive)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 220 : 160;
                    pTemp = Pdryba;
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
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 230 : 180;
                    pTemp = Psyno;
                }

                // Ausreden fuer Gefaess
                if (sudobjoRect.IsPointInRect(pTemp)) {
                    // ryba
                    nextActionID = mainFrame.whatItem == 14 ? 240 : 185;
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
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // nach Polo gehen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

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

                // nach Cyrkej gehen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

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
                if (rybowarLookRect.IsPointInRect(pTemp) && !AnimActive)
                    /*((drybaRect.IsPointInRect (pTemp) == false) || (mainFrame.Actions[913] == true)))*/ {
                    nextActionID = 2;
                    pTemp = Prybowar;
                }

                // Dryba ansehen
                if (drybaLookRect.IsPointInRect(pTemp) && !mainFrame.Actions[913] &&
                        (!rybowarLookRect.IsPointInRect(pTemp) || AnimActive)) {
                    nextActionID = 3;
                    pTemp = Pdryba;
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
                if (rybowarLookRect.IsPointInRect(pTemp) && !AnimActive)
                    /* drybaRect.IsPointInRect (pTemp) == false) || (mainFrame.Actions[913] == true)))*/ {
                    nextActionID = 51;
                    mainFrame.wegGeher.SetzeNeuenWeg(Prybowar);
                    mainFrame.repaint();
                    return;
                }

                // Dryba nehmen
                if (drybaLookRect.IsPointInRect(pTemp) && !mainFrame.Actions[913] &&
                        (!rybowarLookRect.IsPointInRect(pTemp) || AnimActive)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdryba);
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
                AnimLocked = true;
                mainFrame.Clipset = false;
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
                    wikowarRect.IsPointInRect(pTemp) ||
                    rybowarLookRect.IsPointInRect(pTemp) && !AnimActive ||
                    drybaLookRect.IsPointInRect(pTemp) && !mainFrame.Actions[913] ||
                    durjelRect.IsPointInRect(pTemp) || durjerRect.IsPointInRect(pTemp) ||
                    synoRect.IsPointInRect(pTemp) || sudobjoRect.IsPointInRect(pTemp);

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
            if (rybowarLookRect.IsPointInRect(pTemp) && !AnimActive ||
                    wikowarRect.IsPointInRect(pTemp) ||
                    drybaLookRect.IsPointInRect(pTemp) && !mainFrame.Actions[913] ||
                    durjelRect.IsPointInRect(pTemp) || durjerRect.IsPointInRect(pTemp) ||
                    synoRect.IsPointInRect(pTemp) || sudobjoRect.IsPointInRect(pTemp)) {
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
        AnimLocked = true;
        mainFrame.krabat.StopWalking();
    }

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent();
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {

        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // System.out.println("Nextaction " + nextActionID);

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
                // Wikowar anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00000"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00001"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00002"),
                        fWikowar, 3, 0, 0);
                break;

            case 2:
                // Rybowar anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00003"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00004"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00005"),
                        fRybowar, 3, 0, 0);
                break;

            case 3:
                // Dryba anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00006"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00007"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00008"),
                        fDryba, 3, 0, 0);
                break;

            case 4:
                // geschlossene Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00009"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00010"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00011"),
                        fGTuer, 3, 0, 0);
                break;

            case 5:
                // offene Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00012"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00013"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00014"),
                        fOTuer, 3, 0, 0);
                break;

            case 6:
                // Stroh anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00015"), Start.stringManager.getTranslation("Loc1_Kulow1_00016"), Start.stringManager.getTranslation("Loc1_Kulow1_00017"),
                        fSyno, 3, 0, 0);
                break;

            case 7:
                // Gefaess anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00018"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00019"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00020"),
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

            case 55:
                // Dryba benutzen
                if (AnimActive) {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    nextActionID = 56;
                    mainFrame.krabat.SetFacing(fDryba);
                    mainFrame.krabat.nAnimation = 120;
                    Counter = 5;
                    // mainFrame.wave.PlayFile ("sfx-dd/wusch2.wav");
                } else {
                    PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00021"),
                            Start.stringManager.getTranslation("Loc1_Kulow1_00022"),
                            Start.stringManager.getTranslation("Loc1_Kulow1_00023"),
                            0, 33, 0, 0, rybowarTalk);
                }
                break;

            case 56:
                // Ende nimmFischAttrappe
                if (--Counter == 1) {
                    // Auf Gleichzeitigkeit timen !!!
                    mainFrame.Clipset = false;
                    mainFrame.Actions[913] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.inventory.vInventory.addElement(13);
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                nextActionID = 0;
                break;

            case 60:
                // geschl. Tuer benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00024"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00025"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00026"),
                        fGTuer, 3, 0, 0);
                break;

            case 65:
                // offene Tuer benutzen
                // Zuffzahl von 0 bis 2
                int zuffZahl2 = (int) (Math.random() * 2.9);
                switch (zuffZahl2) {
                    case 0:
                        PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00027"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00028"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00029"),
                                fOTuer, 52, 0, 0, doorTalk);
                        break;

                    case 1:
                        PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00030"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00031"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00032"),
                                fOTuer, 52, 0, 0, doorTalk);
                        break;

                    case 2:
                        PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00033"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00034"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00035"),
                                fOTuer, 52, 0, 0, doorTalk);
                        break;
                }
                break;

            case 70:
                // Stroh benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00036"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00037"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00038"),
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
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00039"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00040"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00041"),
                                fSudobjo, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00042"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00043"),
                                Start.stringManager.getTranslation("Loc1_Kulow1_00044"),
                                fSudobjo, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // Gehe zu Polo
                NeuesBild(11, 21);
                break;

            case 101:
                // Gehe zu Mertens
                NeuesBild(20, 21);
                break;

            case 102:
                // Gehe zu Cyrkej
                NeuesBild(22, 21);
                break;

            case 150:
                // Wikowar - Ausreden
                MPersonAusrede(fWikowar);
                break;

            case 155:
                // Rybowar - Ausreden
                MPersonAusrede(fRybowar);
                break;

            case 160:
                // Dryba - Ausreden
                DingAusrede(fDryba);
                break;

            case 165:
                // Fisch geben
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fRybowar);
                mainFrame.invCursor = false;
                mainFrame.krabat.nAnimation = 148;
                nextActionID = 166;
                break;

            case 166:
                // Fisch wurde ueberreicht
                rybowarnimmtfisch = true;
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                rybowarnimmtfisch = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00045"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00046"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00047"),
                        fRybowar, 33, 2, 167, rybowarTalk);
                break;

            case 167:
                // Fisch wurde ueberreicht
                rybowargibtkrosik = true;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00048"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00049"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00050"),
                        fRybowar, 33, 0, 168, rybowarTalk);
                mainFrame.inventory.vInventory.removeElement(14);
                mainFrame.inventory.vInventory.addElement(15);
                // Zeichen fuer habe keinen Fisch, aber Groschen -> faengt noch nichts in Haty
                mainFrame.Actions[914] = false;
                mainFrame.Actions[915] = true;
                break;

            case 168:
                // Krabat nimmt Krosik
                mainFrame.krabat.nAnimation = 30;
                nextActionID = 169;
                break;

            case 169:
                // Ende Krosikueberreichen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                rybowargibtkrosik = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
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
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00051"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00052"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00053"),
                        fWikowar, 3, 0, 0);
                break;

            case 205:
                // kij auf rybowar
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00054"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00055"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00056"),
                        fRybowar, 3, 0, 0);
                break;

            case 210:
                // bron auf wikowar
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00057"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00058"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00059"),
                        fWikowar, 3, 0, 0);
                break;

            case 215:
                // bron auf rybowar
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00060"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00061"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00062"),
                        fRybowar, 3, 0, 0);
                break;

            case 220:
                // kamuski auf dryba
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00063"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00064"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00065"),
                        fDryba, 3, 0, 0);
                break;

            case 230:
                // kamuski auf syno
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00066"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00067"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00068"),
                        fSyno, 3, 0, 0);
                break;

            case 240:
                // ryba auf sudobjo
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00069"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00070"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00071"),
                        fSudobjo, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine mit Wikowar
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00072"), 1000, 120, new int[]{120}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00073"), 120, 121, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00074"), 1000, 122, new int[]{122}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00075"), 122, 123, new int[]{123}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00076"), 123, 1000, null, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00077"), 1000, 125, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00078"), 125, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00079"), 1000, 120, new int[]{120}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00080"), 120, 121, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00081"), 1000, 122, new int[]{122}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00082"), 122, 123, new int[]{123}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00083"), 123, 1000, null, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00084"), 1000, 125, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00085"), 125, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00086"), 1000, 120, new int[]{120}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00087"), 120, 121, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00088"), 1000, 122, new int[]{122}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00089"), 122, 123, new int[]{123}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00090"), 123, 1000, null, 650);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00091"), 1000, 125, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00092"), 125, 1000, null, 800);
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
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00093"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00094"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00095"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 620:
                // Reaktion Wikowar auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00096"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00097"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00098"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 630:
                // Reaktion Wikowar auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00099"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00100"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00101"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 640:
                // Reaktion Wikowar auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00102"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00103"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00104"),
                        0, 32, 2, 600, wikowarTalk);
                break;

            case 650:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00105"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00106"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00107"),
                        0, 32, 2, 651, wikowarTalk);
                break;

            case 651:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00108"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00109"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00110"),
                        0, 32, 2, 652, wikowarTalk);
                break;

            case 652:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00111"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00112"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00113"),
                        0, 32, 2, 653, wikowarTalk);
                break;

            case 653:
                // Reaktion Wikowar auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00114"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00115"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00116"),
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
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00117"), 1000, 130, new int[]{130}, 910);
                    if (!mainFrame.Actions[150]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00118"), 130, 1000, null, 920);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00119"), 130, 1000, null, 930);
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00120"), 1000, 132, new int[]{132, 133}, 940);

                    // 4. Frage (wenn 2. weg)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00121"), 133, 1000, null, 950);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00122"), 1000, 134, null, 1100);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00123"), 134, 1000, null, 1100);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00124"), 1000, 130, new int[]{130}, 910);
                    if (!mainFrame.Actions[150]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00125"), 130, 1000, null, 920);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00126"), 130, 1000, null, 930);
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00127"), 1000, 132, new int[]{132, 133}, 940);

                    // 4. Frage (wenn 2. weg)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00128"), 133, 1000, null, 950);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00129"), 1000, 134, null, 1100);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00130"), 134, 1000, null, 1100);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00131"), 1000, 130, new int[]{130}, 910);
                    if (!mainFrame.Actions[150]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00132"), 130, 1000, null, 920);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00133"), 130, 1000, null, 930);
                    }

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00134"), 1000, 132, new int[]{132, 133}, 940);

                    // 4. Frage (wenn 2. weg)
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00135"), 133, 1000, null, 950);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00136"), 1000, 134, null, 1100);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Kulow1_00137"), 134, 1000, null, 1100);
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
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00138"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00139"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00140"),
                        0, 33, 2, 900, rybowarTalk);
                break;

            case 920:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K noch nicht in Haty)
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00141"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00142"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00143"),
                        0, 33, 2, 900, rybowarTalk);
                break;

            case 930:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K in Haty gewesen)
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00144"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00145"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00146"),
                        0, 33, 2, 931, rybowarTalk);
                break;

            case 931:
                // Reaktion Rybowar auf 2. Teil 1. Frage (K in Haty gewesen)
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00147"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00148"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00149"),
                        0, 33, 2, 900, rybowarTalk);
                break;

            case 940:
                // Fischer quatscht erstes Mal beim Suchen (2. Frage)
                mainFrame.Actions[134] = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00150"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00151"),
                        Start.stringManager.getTranslation("Loc1_Kulow1_00152"),
                        0, 33, 1, 1110, rybowarTalk);
                break;

            case 950:
                // Fischer quatscht naechste Male beim Suchen (4. Frage)
                mainFrame.Actions[134] = false;
                PersonSagt(Start.stringManager.getTranslation("Loc1_Kulow1_00153"), Start.stringManager.getTranslation("Loc1_Kulow1_00154"), Start.stringManager.getTranslation("Loc1_Kulow1_00155"),
                        0, 33, 1, 1120, rybowarTalk);
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

            case 1110:
                // MC beenden und 1. Animation starten
                AnimActive = true;
                mainFrame.fPlayAnim = false;
                rybowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                AnimID = 1;
                mainFrame.repaint();
                break;

            case 1120:
                // MC beenden und 2. Animation starten
                AnimActive = true;
                mainFrame.fPlayAnim = false;
                rybowarhoertzu = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                AnimID = 500;
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }

    private void DoAnims() {
        switch (AnimID) {
            case 1:
                // Figur verschwinden lassen und bisschen warten
                rybowarsuchtzrawc = true;
                AnimCounter = 20;
                AnimID = 2;
                break;

            case 2:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 3;
                }
                break;

            case 3:
                // 1. Textausgabe
                if (mainFrame.sprache == 1) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00156");
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00157");
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00158");
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 10;
                AnimTalkPerson = 33;
                AnimID = 4;
                break;

            case 4:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 5;
                }
                break;

            case 5:
                AnimOutputText = "";
                mainFrame.Clipset = false;
                AnimCounter = 10;
                AnimTalkPerson = 0;
                AnimID = 6;
                break;

            case 6:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 7;
                }
                break;

            case 7:
                // 2. Textausgabe
                if (mainFrame.sprache == 1) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00159");
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00160");
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00161");
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 10;
                AnimTalkPerson = 33;
                AnimID = 8;
                break;

            case 8:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 9;
                }
                break;

            case 9:
                AnimOutputText = "";
                mainFrame.Clipset = false;
                AnimCounter = 10;
                AnimTalkPerson = 0;
                AnimID = 10;
                break;

            case 10:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 23;
                }
                break;
  	  	
		/*  	  case 11:
			  // 3. Textausgabe
			  if (mainFrame.sprache == 1) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00162");
			  if (mainFrame.sprache == 2) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00163");
			  if (mainFrame.sprache == 3) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00164");
			  AnimOutputTextPos = mainFrame.ifont.CenterAnimText (AnimOutputText, rybowarTalk);
			  AnimCounter = 10;
			  AnimTalkPerson = 33;
			  AnimID = 12;
			  break;
  	  	
			  case 12:
			  AnimCounter--;
			  if (AnimCounter == 0) AnimID = 13;
			  break;
  	  	
			  case 13:
			  AnimOutputText = "";
			  mainFrame.Clipset = false;
			  AnimCounter = 10;
			  AnimTalkPerson = 0;
			  AnimID = 14;
			  break;
  	  	
			  case 14:
			  AnimCounter--;
			  if (AnimCounter == 0) AnimID = 15;
			  break;
  	  	
			  case 15:
			  // 4. Textausgabe
			  if (mainFrame.sprache == 1) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00165");
			  if (mainFrame.sprache == 2) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00166");
			  if (mainFrame.sprache == 3) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00167");
			  AnimOutputTextPos = mainFrame.ifont.CenterAnimText (AnimOutputText, rybowarTalk);
			  AnimCounter = 20;
			  AnimTalkPerson = 33;
			  AnimID = 16;
			  break;
  	  	
			  case 16:
			  AnimCounter--;
			  if (AnimCounter == 0) AnimID = 17;
			  break;
  	  	
			  case 17:
			  AnimOutputText = "";
			  mainFrame.Clipset = false;
			  AnimCounter = 10;
			  AnimTalkPerson = 0;
			  AnimID = 18;
			  break;
  	  	
			  case 18:
			  AnimCounter--;
			  if (AnimCounter == 0) AnimID = 19;
			  break;
  	  	
			  case 19:
			  // 5. Textausgabe
			  if (mainFrame.sprache == 1) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00168");
			  if (mainFrame.sprache == 2) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00169");
			  if (mainFrame.sprache == 3) AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00170");
			  AnimOutputTextPos = mainFrame.ifont.CenterAnimText (AnimOutputText, rybowarTalk);
			  AnimCounter = 10;
			  AnimTalkPerson = 33;
			  AnimID = 20;
			  break;
  	  	
			  case 20:
			  AnimCounter--;
			  if (AnimCounter == 0) AnimID = 21;
			  break;
  	  	
			  case 21:
			  AnimOutputText = "";
			  mainFrame.Clipset = false;
			  AnimCounter = 10;
			  AnimTalkPerson = 0;
			  AnimID = 22;
			  break;
  	  	
			  case 22:
			  AnimCounter--;
			  if (AnimCounter == 0) AnimID = 23;
			  break; */

            case 23:
                // 6. Textausgabe (Person wieder aufrichten...
                rybowarsuchtzrawc = false;
                if (mainFrame.sprache == 1) {
                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Kulow1_00171"));
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Kulow1_00172"));
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Kulow1_00173"));
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 20;
                AnimTalkPerson = 33;
                AnimID = 24;
                break;

            case 24:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 25;
                }
                break;

            case 25:
            case 508:
                AnimOutputText = "";
                mainFrame.Clipset = false;
                AnimCounter = 0;
                AnimTalkPerson = 0;
                AnimID = 0;
                AnimActive = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 500:
                // Figur verschwinden lassen und bisschen warten
                AnimCounter = 90;
                rybowarsuchtzrawc = true;
                AnimID = 501;
                break;

            case 501:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 502;
                }
                break;

            case 502:
                // 1. Textausgabe
                int zuffi = 1;
                if (!mainFrame.Actions[138]) {
                    mainFrame.Actions[138] = true;
                    zuffi = 0;
                }
                switch (zuffi) {
                    case 0:
                        octopussyerscheint = true;
                        mainFrame.wave.PlayFile("sfx/oktopus.wav");
                        if (mainFrame.sprache == 1) {
                            AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00174");
                        }
                        if (mainFrame.sprache == 2) {
                            AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00175");
                        }
                        if (mainFrame.sprache == 3) {
                            AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00176");
                        }
                        break;

                    case 1:
                        if (mainFrame.sprache == 1) {
                            AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00177");
                        }
                        if (mainFrame.sprache == 2) {
                            AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00178");
                        }
                        if (mainFrame.sprache == 3) {
                            AnimOutputText = Start.stringManager.getTranslation("Loc1_Kulow1_00179");
                        }
                        break;
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 25;
                AnimTalkPerson = 33;
                AnimID = 503;
                break;

            case 503:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 504;
                }
                break;

            case 504:
                AnimOutputText = "";
                mainFrame.Clipset = false;
                AnimCounter = 50;
                AnimTalkPerson = 0;
                AnimID = 505;
                break;

            case 505:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 506;
                }
                break;

            case 506:
                // 2. Textausgabe (Person wieder aufrichten...
                rybowarsuchtzrawc = false;
                if (mainFrame.sprache == 1) {
                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Kulow1_00180"));
                }
                if (mainFrame.sprache == 2) {
                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Kulow1_00181"));
                }
                if (mainFrame.sprache == 3) {
                    AnimOutputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Loc1_Kulow1_00182"));
                }
                AnimOutputTextPos = mainFrame.ifont.CenterAnimText(AnimOutputText, rybowarTalk);
                AnimCounter = 30;
                AnimTalkPerson = 33;
                AnimID = 507;
                break;

            case 507:
                AnimCounter--;
                if (AnimCounter == 0) {
                    AnimID = 508;
                }
                break;

        }
    }
}