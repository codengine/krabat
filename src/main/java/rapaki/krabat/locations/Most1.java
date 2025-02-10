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

package rapaki.krabat.locations;

import rapaki.krabat.Start;
import rapaki.krabat.anims.Reh;
import rapaki.krabat.main.*;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.sound.BackgroundMusicPlayer;

public class Most1 extends Mainloc {
    private GenericImage background, gelaend, wegstueck;
    private GenericImage[] flussu;
    private boolean switchanim = false;
    private int flusscount = 1;
    private int oldActionID = 0;
    private GenericPoint Endpunkt, Wendepunkt, Merkpunkt;
    private boolean Berglauf = false;
    private boolean isTal;

    private Reh reh;

    // Konstanten - Rects
    private static final Borderrect rechterAusgang = new Borderrect(609, 353, 639, 415);
    private static final Borderrect obererAusgang = new Borderrect(412, 176, 496, 221);
    private static final Borderrect untererAusgang = new Borderrect(0, 436, 246, 479);
    // private static final borderrect gelRect        = new borderrect (351, 284, 430, 343);
    private static final Borderrect ralbitzSchild = new Borderrect(254, 208, 301, 228);
    private static final Borderrect dresdenSchild = new Borderrect(255, 233, 296, 246);
    private static final Borderrect rekaRect = new Borderrect(427, 383, 513, 476);

    private static final Bordertrapez BergTrapez = new Bordertrapez(376, 396, 272, 342, 278, 349);
    private static final Bordertrapez TalTrapez = new Bordertrapez(457, 459, 407, 419, 225, 284);

    // Points in Location
    private static final GenericPoint Pschild = new GenericPoint(305, 333);
    // private static final GenericPoint RalbicyPoint = new GenericPoint (632, 246);
    private static final GenericPoint Pdown = new GenericPoint(95, 479);
    private static final GenericPoint Pup = new GenericPoint(458, 226);
    private static final GenericPoint Pright = new GenericPoint(639, 390);
    private static final GenericPoint Preka = new GenericPoint(476, 388);

    // Zooming - Variablen
    private static final int TAL_MAXX = 278;
    private static final int TAL_MINX = 278;
    private static final float TAL_ZOOMF = 5.2f;
    private static final int TAL_DEFSCALE = 70;

    private static final int BERG_MAXX = 422;
    private static final int BERG_MINX = 134;
    private static final float BERG_ZOOMF = 3.6f;
    private static final int BERG_DEFSCALE = -20;

    // Konstante ints
    private static final int fPokazRal = 9;
    private static final int fPokazDrj = 9;
    private static final int fReka = 6;
    private static final int fDrjezdzany = 6;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Most1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        Merkpunkt = new GenericPoint(0, 0);

        flussu = new GenericImage[8];

        reh = new Reh(mainFrame, false, new GenericRectangle(586, 247, 53, 25), 3);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        switch (oldLocation) {
            case 0: // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                GenericPoint tp = mainFrame.krabat.GetKrabatPos();
                Borderrect TalRect = new Borderrect(400, 220, 460, 290);
                if (TalRect.IsPointInRect(tp) == true) {
                    isTal = true;
                } else {
                    isTal = false;
                }
                break;
            case 1:
                // von Ralbicy aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(624, 384));
                mainFrame.krabat.SetFacing(9);
                isTal = false;
                break;
            case 7:
                // von Sunow aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(458, 226));
                mainFrame.krabat.SetFacing(6);
                isTal = true;
                break;
        }

        // Matrix - Init
        InitMatrix();

    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/most/most2.gif");
        gelaend = getPicture("gfx/most/most-2.gif");
        wegstueck = getPicture("gfx/most/most-3.gif");

        flussu[1] = getPicture("gfx/most/flu-1.gif");
        flussu[2] = getPicture("gfx/most/flu-2.gif");
        flussu[3] = getPicture("gfx/most/flu-3.gif");
        flussu[4] = getPicture("gfx/most/flu-4.gif");
        flussu[5] = getPicture("gfx/most/flu-5.gif");
        flussu[6] = getPicture("gfx/most/flu-6.gif");
        flussu[7] = getPicture("gfx/most/flu-7.gif");

        loadPicture();
    }

    private void InitMatrix() {
        mainFrame.wegGeher.vBorders.removeAllElements();

        if (isTal == true) {
            // Grenzen setzen im Tal
            // Taltrapez
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(457, 459, 407, 419, 225, 284));

            // Laufmatrix anpassen
            mainFrame.wegSucher.ClearMatrix(1);

            // Zooming anpassen
            mainFrame.krabat.maxx = TAL_MAXX;
            mainFrame.krabat.minx = TAL_MINX;
            mainFrame.krabat.zoomf = TAL_ZOOMF;
            mainFrame.krabat.defScale = TAL_DEFSCALE;
        } else {
            // Grenzen setzen auf dem Berg
            // Bergtrapez
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(376, 396, 272, 342, 278, 349));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(272, 515, 218, 515, 350, 367));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(516, 358, 556, 373));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(557, 368, 609, 378));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(610, 368, 639, 402));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(218, 286, 43, 182, 368, 479));

            // Laufmatrix anpassen
            mainFrame.wegSucher.ClearMatrix(6);

            // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 2);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(1, 5);

            // Zooming anpassen
            mainFrame.krabat.maxx = BERG_MAXX;
            mainFrame.krabat.minx = BERG_MINX;
            mainFrame.krabat.zoomf = BERG_ZOOMF;
            mainFrame.krabat.defScale = BERG_DEFSCALE;
        }
    }

    public void cleanup() {
        background = null;
        gelaend = null;
        wegstueck = null;

        flussu[1] = null;
        flussu[2] = null;
        flussu[3] = null;
        flussu[4] = null;
        flussu[5] = null;
        flussu[6] = null;
        flussu[7] = null;

        reh.cleanup();
        reh = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (mainFrame.Clipset == false) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            mainFrame.isAnim = true;
            g.setClip(0, 0, 644, 484);
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // Rehe Hintergrund loeschen
        g.setClip(586, 200, 100, 100);
        g.drawImage(background, 0, 0, null);

        // Rehe zeichnen
        reh.drawReh(g);

        // Animation abspielen
        switchanim = !(switchanim);
        if (switchanim == true) {
            g.setClip(0, 0, 644, 484);
            g.drawImage(flussu[flusscount], 387, 380, null);
            flusscount++;
            if (flusscount == 8) {
                flusscount = 1;
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // hier ist der Sound...
        evalSound();

        mainFrame.wegGeher.GeheWeg();

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
        if (Berglauf == true) {
            g.drawImage(wegstueck, 342, 270, null);
        }

        GenericPoint tem = mainFrame.krabat.GetKrabatPos();

        if (BergTrapez.PointInside(tem) == true) {
            g.drawImage(gelaend, 379, 273, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip((int) my.getX(), (int) my.getY(), (int) my.getWidth(), (int) my.getHeight());
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

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
        }
        outputText = "";

        // Wenn in Animation, dann normales Gameplay aussetzen
        if (mainFrame.fPlayAnim == true) {
            return;
        }

        // Wenn Krabat - Animation, dann normales Gameplay aussetzen
        if (mainFrame.krabat.nAnimation != 0) {
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // linker Maustaste
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp) == true) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Schild Ralbitz
                if (ralbitzSchild.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 16: // kamuski
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = Pschild;
                }

                // Ausreden fuer Schild Dresden
                if (dresdenSchild.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 16: // kamuski
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 155;
                            break;
                    }
                    pTemp = Pschild;
                }

                // Ausreden fuer Reka
                if (rekaRect.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 10: // wuda + wacki
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 160;
                            break;
                    }
                    pTemp = Preka;
                }

                boolean tp = TesteLauf(pTemp, nextActionID);

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (tp == false) {
                    mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                }
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
                return;
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Dresden gehen ?
                if (untererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 60;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (untererAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                }

                // nach Sunow gehen?
                if (obererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (obererAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // rechter Ausgang zu Ralbicy
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (rechterAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schild Ralbitz ansehen
                if (ralbitzSchild.IsPointInRect(pTemp) == true) {
                    nextActionID = 1;
                    pTemp = Pschild;
                }

                // Schild Dresden ansehen
                if (dresdenSchild.IsPointInRect(pTemp) == true) {
                    nextActionID = 2;
                    pTemp = Pschild;
                }

                // Reka ansehen
                if (rekaRect.IsPointInRect(pTemp) == true) {
                    nextActionID = 3;
                    pTemp = Preka;
                }

                boolean tz = TesteLauf(pTemp, nextActionID);

                System.out.println("Lauftest ergab : " + tz);

                if (tz == false) {
                    mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                }
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                nextActionID = 0;

                // ??? Anschauen
                if (untererAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Sunow anschauen
                if (obererAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Ralbitz anschauen
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Schild Ralbitz mitnehmen
                if (ralbitzSchild.IsPointInRect(pTemp) == true) {
                    nextActionID = 50;
                    pTemp = Pschild;
                    boolean tu = TesteLauf(pTemp, nextActionID);
                    if (tu == false) {
                        mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Schild Ralbitz mitnehmen
                if (dresdenSchild.IsPointInRect(pTemp) == true) {
                    nextActionID = 55;
                    pTemp = Pschild;
                    boolean tu = TesteLauf(pTemp, nextActionID);
                    if (tu == false) {
                        mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                    }
                    mainFrame.repaint();
                    return;
                }

                // Reka mitnehmen
                if (rekaRect.IsPointInRect(pTemp) == true) {
                    nextActionID = 70;
                    pTemp = Preka;
                    boolean tu = TesteLauf(pTemp, nextActionID);
                    if (tu == false) {
                        mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                    }
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

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Wenn Animation, dann transparenter Cursor
        if ((mainFrame.fPlayAnim == true) || (mainFrame.krabat.nAnimation != 0)) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.Nix);
            }
            return;
        }

        // wenn InventarCursor, dann anders reagieren
        if (mainFrame.invCursor == true) {
            // hier kommt Routine hin, die Highlight berechnet
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            if ((ralbitzSchild.IsPointInRect(pTemp) == true) || (tmp.IsPointInRect(pTemp) == true) ||
                    (dresdenSchild.IsPointInRect(pTemp) == true) || (rekaRect.IsPointInRect(pTemp) == true)) {
                mainFrame.invHighCursor = true;
            } else {
                mainFrame.invHighCursor = false;
            }

            if ((Cursorform != 10) && (mainFrame.invHighCursor == false)) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            if ((Cursorform != 11) && (mainFrame.invHighCursor == true)) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }
        }


        // normaler Cursor, normale Reaktion
        else {
            if (rechterAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 3) {
                    mainFrame.setCursor(mainFrame.Cright);
                    Cursorform = 3;
                }
                return;
            }

            if ((ralbitzSchild.IsPointInRect(pTemp) == true) || (dresdenSchild.IsPointInRect(pTemp) == true) ||
                    (rekaRect.IsPointInRect(pTemp) == true)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
                }
                return;
            }

            if (untererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 5) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 5;
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

    // Erkennungsroutine, ob Animationsmodus eingeschaltet werden muss
    private boolean TesteLauf(GenericPoint pTxxx, int Action) {
        GenericPoint kpos = mainFrame.krabat.GetKrabatPos();

        // Hier Punkt klonen, damit alter Punkt erhalten bleibt
        GenericPoint pTemp = new GenericPoint(pTxxx.x, pTxxx.y);

        System.out.println("Es wird getestet.");

        // vom Tal auf den Berg???
        if ((isTal == true) && (pTemp.y > 277)) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 600;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor dem Verschwinden berechnen
            GenericPoint rand = new GenericPoint(0, 0);
            rand = TalTrapez.Punkte(kpos.y);
            System.out.println(" links aktuell rechts " + rand.x + " " + kpos.x + " " + rand.y);
            pTemp.y = TalTrapez.y2;
            float t1 = kpos.x - rand.x;
            float t2 = rand.y - rand.x;
            float teil = t1 / t2;
            pTemp.x = (int) TalTrapez.x3 + (int) ((TalTrapez.x4 - TalTrapez.x3) * teil);

            System.out.println("Mittenfaktor " + teil);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (BergTrapez.x1 + ((BergTrapez.x2 - BergTrapez.x1) * teil)), BergTrapez.y1);
            Wendepunkt = new GenericPoint(((pTemp.x + Endpunkt.x) / 2), 380);

            mainFrame.wegGeher.SetzeWegOhneStand(pTemp);

            System.out.println(" Startpunkt " + pTemp.x + " " + pTemp.y);

            mainFrame.repaint();

            System.out.println(" Wendepunkt Endpunkt " + Wendepunkt.x + " " + Wendepunkt.y + " " + Endpunkt.x + " " + Endpunkt.y);

            return true;
        }

        // vom Berg ins Tal ??
        if ((isTal == false) && (pTemp.y < 278)) {
            // Alte Position retten
            oldActionID = Action;
            nextActionID = 610;
            Merkpunkt.x = pTemp.x;
            Merkpunkt.y = pTemp.y;

            // Punkt vor Verschwinden berechnen
            GenericPoint raud = new GenericPoint(0, 0);
            raud = BergTrapez.Punkte(kpos.y);

            System.out.println(" links aktuell rechts " + raud.x + " " + kpos.x + " " + raud.y);

            pTemp.y = BergTrapez.y1;
            float t3 = kpos.x - raud.x;
            float t4 = raud.y - raud.x;
            float teal = t3 / t4;

            System.out.println(" Mittenfaktor " + teal);

            if (BergTrapez.PointInside(mainFrame.krabat.GetKrabatPos()) == true) {
                pTemp.x = (int) BergTrapez.x1 + (int) ((BergTrapez.x2 - BergTrapez.x1) * teal);
            } else {
                // Default - Werte fuer Tallauf, wenn noch zu weit weg
                pTemp = new GenericPoint(((BergTrapez.x1 + BergTrapez.x2) / 2), BergTrapez.y1);
                teal = 0.5f;
            }

            System.out.println(" Mittenfaktor neu " + teal);

            // Punkte waehrend Berglauf berechnen
            Endpunkt = new GenericPoint((int) (TalTrapez.x3 + ((TalTrapez.x4 - TalTrapez.x3) * teal)), TalTrapez.y2);
            Wendepunkt = new GenericPoint(((pTemp.x + Endpunkt.x) / 2), 380);

            mainFrame.wegGeher.SetzeWegOhneStand(pTemp);

            System.out.println(" Startpunkt " + pTemp.x + " " + pTemp.y);

            mainFrame.repaint();

            System.out.println(" Wendepunkt Endpunkt " + Wendepunkt.x + " " + Wendepunkt.y + " " + Endpunkt.x + " " + Endpunkt.y);

            return true;
        }
        return false;
    }

    // dieses Event nicht beachten
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Wenn Inventarcursor, dann keine Keys
        if (mainFrame.invCursor == true) {
            return;
        }

        // Bei Animationen keine Keys
        if (mainFrame.fPlayAnim == true) {
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
            return;
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

    private void evalSound() {
        int zfz = (int) (Math.random() * 100);

        if (zfz > 97) {
            int zwzfz = (int) (Math.random() * 4.99);
            zwzfz += 49;

            mainFrame.wave.PlayFile("sfx/recka" + (char) zwzfz + ".wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering == true) ||
                (mainFrame.krabat.isWalking == true)) {
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
                // Schild Ralbitz anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00000"),
                        Start.stringManager.getTranslation("Loc1_Most1_00001"),
                        Start.stringManager.getTranslation("Loc1_Most1_00002"),
                        fPokazRal, 3, 0, 0);
                break;

            case 2:
                // Schild Dresden anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00003"),
                        Start.stringManager.getTranslation("Loc1_Most1_00004"),
                        Start.stringManager.getTranslation("Loc1_Most1_00005"),
                        fPokazDrj, 3, 0, 0);
                break;

            case 3:
                // Reka anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00006"),
                        Start.stringManager.getTranslation("Loc1_Most1_00007"),
                        Start.stringManager.getTranslation("Loc1_Most1_00008"),
                        fReka, 3, 0, 0);
                break;

            case 50:
                // Schild Ralbitz mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00009"),
                        Start.stringManager.getTranslation("Loc1_Most1_00010"),
                        Start.stringManager.getTranslation("Loc1_Most1_00011"),
                        fPokazRal, 3, 0, 0);
                break;

            case 55:
                // Schild Dresden mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00012"),
                        Start.stringManager.getTranslation("Loc1_Most1_00013"),
                        Start.stringManager.getTranslation("Loc1_Most1_00014"),
                        fPokazDrj, 3, 0, 0);
                break;

            case 60:
                // Nach Dresden gehen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00015"),
                        Start.stringManager.getTranslation("Loc1_Most1_00016"),
                        Start.stringManager.getTranslation("Loc1_Most1_00017"),
                        fDrjezdzany, 3, 0, 0);
                break;

            case 70:
                // Reka mitnehmen
                // Zufallszahl 0 bis 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00018"),
                                Start.stringManager.getTranslation("Loc1_Most1_00019"),
                                Start.stringManager.getTranslation("Loc1_Most1_00020"),
                                fReka, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00021"),
                                Start.stringManager.getTranslation("Loc1_Most1_00022"),
                                Start.stringManager.getTranslation("Loc1_Most1_00023"),
                                fReka, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // Gehe zu Ralbicy
                NeuesBild(1, 2);
                break;

            case 102:
                // nach Sunow gehen
                NeuesBild(7, 2);
                break;

            case 150:
                // Schild - Ausreden Ralbitz
                DingAusrede(fPokazRal);
                break;

            case 155:
                // Schild - Ausreden Ralbitz
                DingAusrede(fPokazDrj);
                break;

            case 160:
                // Ausrede Wasser
                DingAusrede(fReka);
                break;

            case 200:
                // kamuski auf schild
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00024"),
                        Start.stringManager.getTranslation("Loc1_Most1_00025"),
                        Start.stringManager.getTranslation("Loc1_Most1_00026"),
                        fPokazRal, 3, 0, 0);
                break;

            case 210:
                // Wuda + wacki auf Wasser
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Most1_00027"),
                        Start.stringManager.getTranslation("Loc1_Most1_00028"),
                        Start.stringManager.getTranslation("Loc1_Most1_00029"),
                        fReka, 3, 0, 0);
                break;

            case 600:
                // vom Tal auf den Berg laufen
                Berglauf = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(Wendepunkt);
                nextActionID = 601;
                break;

            case 601:
                // beim Lauf Tal auf Berg wieder zum Vorschein kommen
                mainFrame.krabat.maxx = BERG_MAXX;
                mainFrame.krabat.minx = BERG_MINX;
                mainFrame.krabat.defScale = BERG_DEFSCALE;
                mainFrame.krabat.zoomf = BERG_ZOOMF;
                mainFrame.wegGeher.SetzeGarantiertWegFalsch(Endpunkt);
                nextActionID = 620;
                break;

            case 610:
                // vom Berg ins Tal laufen invertiert
                Berglauf = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeGarantiertWegFalsch(Wendepunkt);
                nextActionID = 611;
                break;

            case 611:
                // beim Lauf Berg ins Tal wieder zum Vorschein kommen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(Endpunkt);
                mainFrame.krabat.maxx = TAL_MAXX;
                mainFrame.krabat.defScale = TAL_DEFSCALE;
                mainFrame.krabat.zoomf = TAL_ZOOMF;
                mainFrame.krabat.minx = TAL_MINX;
                nextActionID = 620;
                break;

            case 620:
                // Laufen beenden und alles wieder auf Normal zuruecksetzen
                mainFrame.fPlayAnim = false;
                Berglauf = false;
                Cursorform = 200;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                isTal = !(isTal);
                InitMatrix();
                nextActionID = oldActionID;
                mainFrame.wegGeher.SetzeNeuenWeg(Merkpunkt);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}