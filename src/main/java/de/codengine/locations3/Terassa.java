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

package de.codengine.locations3;

import de.codengine.Start;
import de.codengine.anims.DDKowar;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Terassa extends Mainloc {
    private GenericImage background, gelaender, busch, hammer;
    private GenericImage gelaender2, delle;
    private DDKowar schmied;

    // private borderrect schmiedRect;
    private Borderrect schmiedClickRect;
    private GenericPoint schmiedPoint;
    private GenericPoint schmiedTalk;
    private boolean schmiedVisible = false;
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
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        schmied = new DDKowar(mainFrame);

        schmiedPoint = new GenericPoint();
        schmiedPoint.x = schmiedFeet.x - (DDKowar.Breite / 2);
        schmiedPoint.y = schmiedFeet.y - DDKowar.Hoehe;

        schmiedTalk = new GenericPoint();
        schmiedTalk.x = schmiedFeet.x;
        schmiedTalk.y = schmiedPoint.y - 50;

        // schmiedRect = new borderrect (schmiedPoint.x, schmiedPoint.y, schmiedPoint.x + DDKowar.Breite, schmiedPoint.y + DDKowar.Hoehe);
        schmiedClickRect = new Borderrect(schmiedPoint.x + 7, schmiedPoint.y, schmiedPoint.x + DDKowar.Breite - 14, schmiedPoint.y + DDKowar.Hoehe);

        // hier evaluieren, ob Schmied ueberhaupt da ist
        if ((mainFrame.Actions[529] == true) && (mainFrame.Actions[701] == false)) {
            schmiedVisible = true;
        } else {
            schmiedVisible = false;
        }

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // wenn im Vordergrundrect, welches fuer "ist hinten" gilt, dann ist er vorn
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                if (vorWennHinten[0].PointInside(mainFrame.krabat.GetKrabatPos()) == true) {
                    isVordergrund = true;
                } else {
                    isVordergrund = false;
                }
                break;
            case 126: // von Murja aus
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(500, 339));
                mainFrame.krabat.SetFacing(6);
                isVordergrund = false;
                break;
            case 128: // von Straza aus
                BackgroundMusicPlayer.getInstance().playTrack(21, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(193, 368));
                mainFrame.krabat.SetFacing(6);
                isVordergrund = false;
                break;
            case 150: // von Cychi aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(22, 375));
                mainFrame.krabat.SetFacing(3);
                isVordergrund = false;
                break;
            case 180: // von Karta aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(597, 420));
                mainFrame.krabat.SetFacing(9);
                isVordergrund = false;
                break;
        }

        // es ist bekannt, ob er vorn oder hinten steht, also init
        InitBorders();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/terassa/terassa.gif");
        gelaender = getPicture("gfx-dd/terassa/gelaender.gif");
        busch = getPicture("gfx-dd/terassa/busch.gif");
        hammer = getPicture("gfx-dd/terassa/thammer.gif");
        gelaender2 = getPicture("gfx-dd/terassa/gelaender2.gif");
        delle = getPicture("gfx-dd/terassa/delle.gif");

        loadPicture();
    }

    private void InitBorders() {
        // Grenzen loeschen
        mainFrame.wegGeher.vBorders.removeAllElements();

        // Testen, welche Grenzen gesetzt werden muessen
        if (isVordergrund == false) {
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(515, 550, 300, 500, 337, 362));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(10, 450, 10, 560, 363, 389));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(10, 560, 250, 630, 390, 440));

            mainFrame.wegSucher.ClearMatrix(3);

            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 2);

            mainFrame.krabat.maxx = UNTEN_MAXX;
            mainFrame.krabat.minx = UNTEN_MINX;
            mainFrame.krabat.defScale = UNTEN_DEFSCALE;
            mainFrame.krabat.zoomf = UNTEN_ZOOMF;
        } else {
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(15, 446, 34, 479));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(35, 73, 35, 191, 446, 479));

            mainFrame.wegSucher.ClearMatrix(2);

            mainFrame.wegSucher.PosVerbinden(0, 1);

            mainFrame.krabat.maxx = OBEN_MAXX;
            mainFrame.krabat.minx = OBEN_MINX;
            mainFrame.krabat.defScale = OBEN_DEFSCALE;
            mainFrame.krabat.zoomf = OBEN_ZOOMF;
        }
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
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // Kowar Hintergrund loeschen (sonst vielleicht K geloescht)
        if (schmiedVisible == true) {
            g.setClip(schmiedPoint.x, schmiedPoint.y, DDKowar.Breite, DDKowar.Hoehe);
            g.drawImage(background, 0, 0, null);
            g.drawImage(gelaender, 0, 284, null);

            evalSound(); // Schmied macht Geraeusche
        }

        // Hammer zeichnen, solange noch da
        if ((mainFrame.Actions[953] == false) && (schmiedVisible == true)) {
            g.setClip(221, 463, 27, 20);
            g.drawImage(hammer, 221, 463, null);
        }

        // Hier die Delle reinzeichnen, sobald sie drin ist (muss dann immer gezeichnet werden !!!)
        if (mainFrame.Actions[700] == true) {
            g.setClip(203, 358, 15, 10);
            g.drawImage(delle, 203, 358, null);
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

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
                        // steht Krabat nur da
                        mainFrame.krabat.drawKrabat(g);
                        break;
                }
            }
            // Rumstehen oder Laufen
            else {
                mainFrame.krabat.drawKrabat(g);
                if (mainFrame.invCursor == true) {
                    evalMouseMoveEvent(mainFrame.Mousepoint);  // wenn Krabat unter den Umzieh-Cursor laeuft, muss das Highlight automatisch kommen
                }
            }
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // Wenn Krabat im Vordergrund steht, dann braucht nicht gecheckt zu werden
        if (isVordergrund == false) {
            // steht hinter Gelander
            if (gelaenderRect.IsPointInRect(pKrTemp) == true) {
                g.drawImage((mainFrame.Actions[700] == false) ? gelaender : gelaender2, 0, 284, null);
            }

            // steht hinter Busch
            if (buschRect.IsPointInRect(pKrTemp) == true) {
                g.drawImage(busch, 512, 284, null);
            }
        }

        // Kowar zeichnen
        if (schmiedVisible == true) {
            GenericRectangle may;
            may = g.getClipBounds();
            g.setClip(schmiedPoint.x, schmiedPoint.y, DDKowar.Breite, DDKowar.Hoehe);
            schmied.drawDDkowar(g, TalkPerson, schmiedPoint, schmiedhoertzu);
            g.setClip((int) may.getX(), (int) may.getY(), (int) may.getWidth(), (int) may.getHeight());
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp) == true) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden Schmied
                if ((schmiedClickRect.IsPointInRect(pTemp) == true) && (schmiedVisible == true)) {
                    nextActionID = 150;
                    pTxxx = pSchmied;
                }

                // Ausreden Hammer
                if ((hammerRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[953] == false) &&
                        (schmiedVisible == true)) {
                    nextActionID = 155;
                    pTxxx = pHammer;
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                if (mussKrabatEbeneWechseln(pTxxx) == false) {
                    mainFrame.wegGeher.SetzeNeuenWeg(pTxxx);
                    mainFrame.repaint();
                }
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

                GenericPoint pTxxx = new GenericPoint(pTemp.x, pTemp.y);

                // zu Murja gehen ?
                if (ausgangMurja.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (ausgangMurja.IsPointInRect(kt) == false) {
                        pTxxx = pExitMurja;
                    } else {
                        pTxxx = new GenericPoint(pExitMurja.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Straza gehen ?
                if (ausgangStraza.IsPointInRect(pTemp) == true) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (ausgangStraza.IsPointInRect(kt) == false) {
                        pTxxx = pExitStraza;
                    } else {
                        pTxxx = new GenericPoint(pExitStraza.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Cychi gehen ?
                if (ausgangCychi.IsPointInRect(pTemp) == true) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (ausgangCychi.IsPointInRect(kt) == false) {
                        pTxxx = pExitCychi;
                    } else {
                        pTxxx = new GenericPoint(pExitCychi.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Karta gehen ?
                if (ausgangKarta.IsPointInRect(pTemp) == true) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (ausgangKarta.IsPointInRect(kt) == false) {
                        pTxxx = pExitKarta;
                    } else {
                        pTxxx = new GenericPoint(pExitKarta.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Schmied ansehen
                if ((schmiedClickRect.IsPointInRect(pTemp) == true) && (schmiedVisible == true)) {
                    nextActionID = 1;
                    pTxxx = pSchmied;
                }

                // Hammer ansehen
                if ((hammerRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[953] == false) &&
                        (schmiedVisible == true)) {
                    nextActionID = 2;
                    pTxxx = pHammer;
                }

                if (mussKrabatEbeneWechseln(pTxxx) == false) {
                    mainFrame.wegGeher.SetzeNeuenWeg(pTxxx);
                    mainFrame.repaint();
                }
            } else {
                // rechte Maustaste

                // Wenn Ausgang -> kein Inventar anzeigen
                if ((ausgangMurja.IsPointInRect(pTemp)) ||
                        (ausgangStraza.IsPointInRect(pTemp)) ||
                        (ausgangCychi.IsPointInRect(pTemp)) ||
                        (ausgangKarta.IsPointInRect(pTemp))) {
                    return;
                }

                // Schmied anreden
                if ((schmiedClickRect.IsPointInRect(pTemp) == true) && (schmiedVisible == true)) {
                    nextActionID = 50;
                    if (mussKrabatEbeneWechseln(pSchmied) == false) {
                        mainFrame.wegGeher.SetzeNeuenWeg(pSchmied);
                        mainFrame.repaint();
                    }
                    return;
                }

                // Hammer nehmen
                if ((hammerRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[953] == false) &&
                        (schmiedVisible == true)) {
                    nextActionID = 55;
                    if (mussKrabatEbeneWechseln(pHammer) == false) {
                        mainFrame.wegGeher.SetzeNeuenWeg(pHammer);
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
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
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
            if ((tmp.IsPointInRect(pTemp) == true) ||
                    ((schmiedClickRect.IsPointInRect(pTemp) == true) && (schmiedVisible == true)) ||
                    ((hammerRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[953] == false) &&
                            (schmiedVisible == true))) {
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
            if (((schmiedClickRect.IsPointInRect(pTemp) == true) && (schmiedVisible == true)) ||
                    ((hammerRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[953] == false) &&
                            (schmiedVisible == true))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if ((ausgangMurja.IsPointInRect(pTemp) == true) ||
                    (ausgangStraza.IsPointInRect(pTemp) == true)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangCychi.IsPointInRect(pTemp) == true) {
                if (Cursorform != 9) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 9;
                }
                return;
            }

            if (ausgangKarta.IsPointInRect(pTemp) == true) {
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
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // hier wird festgestellt, ob Krabat den Vorder/Hintergrundwechsel durchfuehren muss
    private boolean mussKrabatEbeneWechseln(GenericPoint Zielpunkt) {
        // rects sind verschieden, je nach dem, ob er oben ist
        if (isVordergrund == true) {
            if ((vorWennVor[0].PointInside(Zielpunkt) == true) ||
                    (vorWennVor[1].PointInside(Zielpunkt) == true) ||
                    (vorWennVor[2].PointInside(Zielpunkt) == true)) {
                // der WegGeher kann normal wie immer angesprochen werden, nix wird veraendert
                return false;
            } else {
                // Laufpunkt wird extra gesetzt und Anim bei Erreichen eingeschaltet

                // DoAction und ZielPunkt merken
                MerkPunkt = Zielpunkt;
                MerkActionID = nextActionID;

                mainFrame.wegGeher.SetzeWegOhneStand(walktoOben);
                mainFrame.repaint();
                nextActionID = 800;
                return true;
            }
        } else {
            if ((vorWennHinten[0].PointInside(Zielpunkt) == false) &&
                    (vorWennHinten[1].PointInside(Zielpunkt) == false)) {
                // der WegGeher kann normal wie immer angesprochen werden, nix wird veraendert
                return false;
            } else {
                // Laufpunkt wird extra gesetzt und Anim bei Erreichen eingeschaltet

                // DoAction und ZielPunkt merken
                MerkPunkt = Zielpunkt;
                MerkActionID = nextActionID;

                mainFrame.wegGeher.SetzeWegOhneStand(walktoUnten);
                mainFrame.repaint();
                nextActionID = 900;
                return true;
            }
        }
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
        // zufaellig wavs fuer Geschnatter abspielen...

        // 1. Inventar zeigt an, dass ein Gegenstand genommen wurde, der Sounddevice braucht
        // 2. wave gibt an, dass die nativen Soundroutinen benutzt werden
        // 3. invCursor -> soll nur dann abschalten

        if (schnauzeSchmied == true) {
            return;
        }

        int zf = (int) (Math.random() * 100);
        if (zf > 96) {
            int zwzf = (int) (Math.random() * 2.99);
            zwzf += 49;

            mainFrame.wave.PlayFile("sfx-dd/schmied" + (char) zwzf + ".wav");
        }
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if ((mainFrame.krabat.isWandering == true) ||
                (mainFrame.krabat.isWalking == true)) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt,
        // wenn noetig (in Superklasse)

        // Achtung, hier sind 2 Ausnahmen, weil diese Sachen in dieser Location behandelt werden muessen
        if ((nextActionID > 499) && (nextActionID < 600) && (nextActionID != 541) && (nextActionID != 553)) {
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
                // Schmied anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00000"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00001"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00002"),
                        fKowar, 3, 0, 0);
                break;

            case 2:
                // Hammer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00003"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00004"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00005"),
                        fHammer, 3, 0, 0);
                break;

            case 50:
                // Schmied anreden hat wenig Erfolg
                // zufaellige Antwort -> Zahl von 0 bis 3 generieren
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeSchmied = true;
                Counter = 10;
                nextActionID = 52;
                break;

            case 52:
                // nach bisschen Warten die Antwort bringen (Sound stoert hoffentlich nicht mehr)
                if ((--Counter) > 1) {
                    break;
                }
                int zuffZahl = (int) (Math.random() * 3.9);
                switch (zuffZahl) {
                    case 0:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Terassa_00006"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00007"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00008"),
                                fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 1:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Terassa_00009"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00010"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00011"),
                                fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 2:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Terassa_00012"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00013"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00014"),
                                fKowar, 45, 0, 53, schmiedTalk);
                        break;

                    case 3:
                        PersonSagt(Start.stringManager.getTranslation("Loc3_Terassa_00015"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00016"),
                                Start.stringManager.getTranslation("Loc3_Terassa_00017"),
                                fKowar, 45, 0, 53, schmiedTalk);
                        break;
                }
                break;

            case 53:
                // Ende Talk Schmied
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeSchmied = false;
                nextActionID = 0;
                break;

            case 55:
                // Hammer mitnehmen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeSchmied = true;
                mainFrame.krabat.SetFacing(fHammer);
                nextActionID = 60;
                mainFrame.krabat.nAnimation = 34;
                Counter = 5;
                break;

            case 60:
                // Krabat sagt Spruch
                if ((--Counter) == 1) {
                    mainFrame.Actions[953] = true;        // Flag setzen
                    mainFrame.Clipset = false;  // alles neu zeichnen
                    // Inventar hinzufuegen
                    mainFrame.inventory.vInventory.addElement(new Integer(46));
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00018"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00019"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00020"),
                        0, 3, 2, 63);
                break;

            case 63:
                // zum haemmern hinlaufen
                mainFrame.wegGeher.SetzeNeuenWeg(pToHammer);
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
                if ((--Counter) == 1) {
                    mainFrame.Actions[700] = true; // ab jetzt die Delle drin
                    mainFrame.Clipset = false;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_Terassa_00021"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00022"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00023"),
                        fKowar, 45, 2, 75, schmiedTalk);
                schmiedhoertzu = true;
                break;

            case 75:
                // Krabat antwortet
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00024"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00025"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00026"),
                        fKowar, 1, 2, 80);
                break;

            case 80:
                // Schmied sagt letzten Spruch
                PersonSagt(Start.stringManager.getTranslation("Loc3_Terassa_00027"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00028"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00029"),
                        0, 45, 2, 85, schmiedTalk);
                schmiedhoertzu = false;
                break;

            case 85:
                // Ende dieser Anim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                schnauzeSchmied = false;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Murja
                // Hat Krabat noch Dienstkleidung an -> zur Murja darf er so gehen
                if (mainFrame.Actions[511] == true) {
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
                if (mainFrame.Actions[511] == true) {
                    krabatUmziehen();
                    break;
                }
                NeuesBild(150, locationID);
                break;

            case 103:
                // Gehe zu Karta
                // Hat Krabat noch Dienstkleidung an -> erst umziehen, aber nicht automatisch
                if (mainFrame.Actions[511] == true) {
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
                if (mussKrabatEbeneWechseln(vorUmziehPoint) == false) {
                    mainFrame.wegGeher.SetzeNeuenWeg(vorUmziehPoint);
                }
                break;

            case 553:
                // Krabat zieht sich wieder normale Klamotten an
                nextActionID = 750;
                if (mussKrabatEbeneWechseln(vorUmziehPoint) == false) {
                    mainFrame.wegGeher.SetzeNeuenWeg(vorUmziehPoint);
                }
                break;

            // Anim "Sluz. Drasta anziehen"

            case 700:
                // Krabat geht hintern Busch
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(UmziehPoint);
                nextActionID = 710;
                break;

            case 710:
                // Kleidung wechseln, sluz Drasta anziehen
                mainFrame.Actions[511] = true;
                mainFrame.Actions[850] = true;
                mainFrame.CheckKrabat();
                mainFrame.inventory.vInventory.addElement(new Integer(53));
                mainFrame.inventory.vInventory.removeElement(new Integer(41));
                nextActionID = 720;
                break;

            case 720:
                // wieder erscheinen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(nachUmziehPoint);
                if (mainFrame.Actions[702] == false) {
                    nextActionID = 730;  // Spruch nur 1x reissen
                } else {
                    nextActionID = 740;
                }
                break;

            case 730:
                // Kommentar
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00030"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00031"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00032"),
                        0, 3, 0, 740);
                break;

            case 740:
                // Ende
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            // Anim "Normale Kleidung anziehen"

            case 750:
                // Krabat geht hintern Busch
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(UmziehPoint);
                nextActionID = 760;
                break;

            case 760:
                // Kleidung wechseln, wieder normale Kleidung anziehen
                mainFrame.Actions[511] = false;
                mainFrame.Actions[850] = false;
                mainFrame.CheckKrabat();
                mainFrame.inventory.vInventory.addElement(new Integer(41));
                mainFrame.inventory.vInventory.removeElement(new Integer(53));
                nextActionID = 770;
                break;

            case 770:
                // wieder erscheinen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(nachUmziehPoint);
                if (mainFrame.Actions[702] == false) {
                    nextActionID = 775;
                } else {
                    nextActionID = 780;
                }
                break;

            case 775:
                // Spruch reissen, dass wieder Normalkleidung
                mainFrame.Actions[702] = true;
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00033"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00034"),
                        Start.stringManager.getTranslation("Loc3_Terassa_00035"),
                        0, 3, 0, 780);
                break;

            case 780:
                // Ende
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            // Hier Routinen fuer "die Treppe runter"

            case 800:
                // von vorn nach hinten laufen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                // auf die Treppe zu laufen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(walktoTreppeOben);
                Counter = 20;
                nextActionID = 810;
                break;

            case 810:
                // die Treppe runterlaufen, nur runterbeamen
                if ((--Counter) > 1) {
                    break;
                }
                // mainFrame.wegGeher.SetzeGarantiertWegFalsch (walktoTreppe);
                mainFrame.krabat.SetKrabatPos(walktoTreppe);
                // Borders neu initialisieren
                isVordergrund = false;
                InitBorders();
                nextActionID = 820;
                break;

            case 820:
                // wieder in den richtigen Bereich laufen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(walktoUnten);
                nextActionID = 830;
                break;

            case 830:
                // wir sind da, nur noch die alten Werte restaurieren
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = MerkActionID;
                mainFrame.wegGeher.SetzeNeuenWeg(MerkPunkt);
                mainFrame.repaint();
                break;

            // Hier Routinen fuer "die Treppe rauf" (Hallo Kelly Bundy !!!)

            case 900:
                // von hinten nach vorn laufen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                // auf die Treppe zu laufen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(walktoTreppe);
                Counter = 20;
                nextActionID = 910;
                break;

            case 910:
                // die Treppe hochlaufen, nur beamen
                if ((--Counter) > 1) {
                    break;
                }
                // mainFrame.wegGeher.SetzeGarantiertWegFalsch (walktoTreppeOben);
                mainFrame.krabat.SetKrabatPos(walktoTreppeOben);
                nextActionID = 920;
                break;

            case 920:
                // wieder in den richtigen Bereich laufen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(walktoOben);
                // Borders neu initialisieren
                isVordergrund = true;
                InitBorders();
                nextActionID = 930;
                break;

            case 930:
                // wir sind da, nur noch die alten Werte restaurieren
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = MerkActionID;
                mainFrame.wegGeher.SetzeNeuenWeg(MerkPunkt);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }
    }

    private void krabatUmziehen() {
        // Krabat zieht sich Dienstkleidung aus
        KrabatSagt(Start.stringManager.getTranslation("Loc3_Terassa_00036"),
                Start.stringManager.getTranslation("Loc3_Terassa_00037"),
                Start.stringManager.getTranslation("Loc3_Terassa_00038"),
                0, 3, 0, 0);
    }
}