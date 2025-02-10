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

package de.codengine.locations2;

import de.codengine.Start;
import de.codengine.anims.Bumm;
import de.codengine.anims.Mlynk2;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Wila2 extends Mainloc2 {
    private GenericImage background;
    private Mlynk2 mueller;
    private boolean setAnim = false;
    private boolean muellerda = false;

    private Bumm muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(393, 101, 464, 144);
    private static final Borderrect untererAusgang = new Borderrect(102, 437, 340, 479);
    private static final Borderrect rechterAusgang = new Borderrect(610, 306, 639, 479);
    private static final Borderrect durjeRect = new Borderrect(300, 347, 328, 411);

    // Konstante Points
    private static final GenericPoint Pdown = new GenericPoint(208, 479);
    private static final GenericPoint Pright = new GenericPoint(639, 411);
    private static final GenericPoint Pup = new GenericPoint(428, 145);
    private static final GenericPoint mlynkFeet = new GenericPoint(260, 452);
    private static final GenericPoint Pdurje = new GenericPoint(312, 433);

    private static final int fDurje = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wila2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 400;
        mainFrame.krabat.zoomf = 4f;
        mainFrame.krabat.defScale = 5;

        mueller = new Mlynk2(mainFrame);

        muellermorph = new Bumm(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = 10;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(6);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(166, 458, 639, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(352, 639, 166, 639, 428, 457));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(536, 639, 464, 639, 373, 427));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(550, 639, 536, 639, 333, 372));
        // mainFrame.wegGeher.vBorders.addElement (new bordertrapez (488, 494, 550, 625, 249, 332));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(488, 494, 519, 560, 249, 297));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(465, 298, 567, 307));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(523, 567, 550, 625, 308, 332));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(390, 298, 464, 307));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(460, 464, 488, 494, 178, 248));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(435, 437, 460, 464, 145, 177));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(10);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 6);
        mainFrame.wegSucher.PosVerbinden(6, 5);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(5, 7);
        mainFrame.wegSucher.PosVerbinden(4, 8);
        mainFrame.wegSucher.PosVerbinden(8, 9);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                if (mainFrame.Actions[300]) {
                    BackgroundMusicPlayer.getInstance().playTrack(20, true);
                } else {
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                break;
            case 80:
                // von Njedz aus (Mueller oder Karte)
                mainFrame.krabat.SetKrabatPos(new GenericPoint(293, 475));
                mainFrame.krabat.SetFacing(12);
                if (mainFrame.Actions[300]) {
                    BackgroundMusicPlayer.getInstance().stop(); // wenn Mueller, dann CD aus
                    setAnim = true;
                    TalkPause = 10;
                } else {
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                break;
            case 75:
                // von Kolmc aus
                // nix Musik, kann nur Heimgehszene sein
                mainFrame.krabat.SetKrabatPos(new GenericPoint(436, 147));
                mainFrame.krabat.SetFacing(6);
                break;

            case 72:
                // von Dubring aus (ueber die Karte)
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(620, 404));
                mainFrame.krabat.SetFacing(9);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/villa/villa.gif");

    }

    @Override
    public void cleanup() {
        background = null;

        mueller.cleanup();
        mueller = null;
        muellermorph.cleanup();
        muellermorph = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
            if (setAnim) {
                mainFrame.fPlayAnim = true;
            }
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0, null);

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0, null);
        }

        // Mueller zeichnen
        if (muellerda) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.MlynkRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0, null);

            // Redet er etwa gerade ??
            if ((TalkPerson == 36) && (mainFrame.talkCount > 0)) {
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

        // sonst noch was zu tun ?
        if (outputText != "") {
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

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 1000;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
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
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Tuer
                if (durjeRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                            nextActionID = 250;
                            break;
                        default:
                            nextActionID = 270;
                            break;
                    }
                    pTemp = Pdurje;
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
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Njedz gehen ?
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

                // zu Dubring gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
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

                // zu Kolmc gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Tuer ansehen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Pdurje;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Njedz Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Kolmc anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Dubring anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Tuer benutzen ?
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 280;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurje);
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
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
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
                    (durjeRect.IsPointInRect(pTemp));

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
            if (durjeRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
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

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
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

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    // dieses Event nicht beachten
    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
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

            case 3:
                // Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Wila2_00000"),
                        Start.stringManager.getTranslation("Loc2_Wila2_00001"),
                        Start.stringManager.getTranslation("Loc2_Wila2_00002"),
                        fDurje, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Njedz oder Karte
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                if (mainFrame.Actions[300]) {
                    mainFrame.ConstructLocation(80);
                    mainFrame.DestructLocation(85);
                } else {
                    mainFrame.ConstructLocation(106);
                    mainFrame.whatScreen = 6;
                }
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu Kolmc
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                if (mainFrame.Actions[300]) {
                    mainFrame.ConstructLocation(75);
                    mainFrame.DestructLocation(85);
                } else {
                    mainFrame.ConstructLocation(106);
                    mainFrame.whatScreen = 6;
                }
                mainFrame.repaint();
                break;

            case 102:
                // nach Dubring gehen
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                if (mainFrame.Actions[300]) {
                    mainFrame.ConstructLocation(72);
                    mainFrame.DestructLocation(85);
                } else {
                    mainFrame.ConstructLocation(106);
                    mainFrame.whatScreen = 6;
                }
                mainFrame.repaint();
                break;

            case 250:
                // Tuer anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Wila2_00003"),
                        Start.stringManager.getTranslation("Loc2_Wila2_00004"),
                        Start.stringManager.getTranslation("Loc2_Wila2_00005"),
                        fDurje, 3, 0, 0);
                break;

            case 270:
                // Durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 280:
                // Tuer mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_Wila2_00006"),
                        Start.stringManager.getTranslation("Loc2_Wila2_00007"),
                        Start.stringManager.getTranslation("Loc2_Wila2_00008"),
                        fDurje, 3, 0, 0);
                break;

            case 1000:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 100);  // 68 - 100 - scaleMueller
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
                mainFrame.Clipset = false;
                MuellerMecker(mueller.evalMlynkTalkPoint());
                TalkPerson = 36;
                TalkPause = 5;
                nextActionID = 1010;
                break;

            case 1010:
                // Gehe zu Muehle zurueck
                NeuesBild(90, 85);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}