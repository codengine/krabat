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
import de.codengine.krabat.anims.Bumm;
import de.codengine.krabat.anims.Mlynk2;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

import java.util.Objects;

public class CornyCholmc2 extends Mainloc2 {
    private GenericImage background;
    private GenericImage himmel;
    private GenericImage vorder;
    private boolean setAnim = false;
    private boolean muellerda = false;
    private Mlynk2 mueller;

    private Bumm muellermorph;
    private int muellermorphcount = 0;
    private boolean ismuellermorphing = false;

    // Konstanten - Rects deklarieren
    private static final Borderrect obererAusgang = new Borderrect(123, 228, 187, 276);
    private static final Borderrect untererAusgang = new Borderrect(376, 451, 590, 479);
    private static final Borderrect waldRect = new Borderrect(0, 44, 424, 164);
    private static final Borderrect kolmcRect = new Borderrect(511, 125, 639, 241);

    // Konstante Points
    private static final GenericPoint Pup = new GenericPoint(161, 268);
    private static final GenericPoint Pdown = new GenericPoint(463, 479);
    private static final GenericPoint Pwald = new GenericPoint(174, 318);
    private static final GenericPoint mlynkFeet = new GenericPoint(418, 443);
    private static final GenericPoint Pkolmc = new GenericPoint(234, 372);

    // Konstante ints
    private static final int fWald = 12;
    private static final int fWjes = 3;

    // konstantes Rectangle fuer den Waldvordergrund
    private static final GenericRectangle vorderWaldRect = new GenericRectangle(0, 190, 44, 209);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public CornyCholmc2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        // Oberwichtig !!! Hier wird "Hachhauselauf" eingeschaltet !!!
        mainFrame.Actions[300] = true;

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 479;
        mainFrame.krabat.zoomf = 4.2f;
        mainFrame.krabat.defScale = 20;

        mueller = new Mlynk2(mainFrame);

        muellermorph = new Bumm(mainFrame);

        mueller.maxx = 300;
        mueller.zoomf = 4f;
        mueller.defScale = 20;

        mueller.SetMlynkPos(mlynkFeet);
        mueller.SetFacing(6);

        InitLocation(oldLocation);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(437, 490, 472, 497, 466, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(211, 263, 437, 490, 389, 465));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(196, 222, 211, 263, 367, 388));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(169, 184, 196, 222, 325, 366));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(153, 163, 169, 184, 276, 324));

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
                BackgroundMusicPlayer.getInstance().playTrack(20, true);
                break;
            case 85:
                // von Villa aus
                BackgroundMusicPlayer.getInstance().stop();
                mainFrame.krabat.SetKrabatPos(new GenericPoint(463, 468));
                mainFrame.krabat.SetFacing(12);
                setAnim = true;
                TalkPause = 10;
                break;
            case 90:
                // von Mlyn2 aus (richtig)
                BackgroundMusicPlayer.getInstance().playTrack(20, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(161, 286));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/kolmc/kolmc2.gif");
        himmel = getPicture("gfx/kolmc/kcsky1.gif");
        vorder = getPicture("gfx/kolmc/kwald.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        himmel = null;
        vorder = null;

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
        g.drawImage(himmel, 0, 0);
        g.drawImage(background, 0, 0);
        g.setClip(vorderWaldRect);
        g.drawImage(vorder, vorderWaldRect.getX(), vorderWaldRect.getY());

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // wenn der Mueller morpht, dann diesen Hintergrund loeschen
        if (ismuellermorphing) {
            g.setClip(muellermorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        // Mueller zeichnen
        if (muellerda) {
            // Hintergrund fuer Mueller loeschen
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = mueller.MlynkRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);

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

        mainFrame.wegGeher.GeheWeg();

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

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = 1000;
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

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Wald
                if (waldRect.IsPointInRect(pTemp)) {
                    // Kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 200 : 150;
                    pTemp = Pwald;
                }

                // Ausreden fuer Kolmc
                if (kolmcRect.IsPointInRect(pTemp)) {
                    // Standard - Sinnloszeug
                    nextActionID = 155;
                    pTemp = Pkolmc;
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

                // zu Villa gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!untererAusgang.IsPointInRect(kt)) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Labyrinth gehen
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

                // Wald ansehen
                if (waldRect.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = Pwald;
                }

                // Kolmc ansehen
                if (kolmcRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    pTemp = Pkolmc;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Hojnt Anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Villa anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Wald reden
                if (waldRect.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwald);
                    mainFrame.repaint();
                    return;
                }

                // Kolmc mitnehmen
                if (kolmcRect.IsPointInRect(pTemp)) {
                    nextActionID = 85;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pkolmc);
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
            Borderrect tmp = mainFrame.krabat.KrabatRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || waldRect.IsPointInRect(pTemp) ||
                    kolmcRect.IsPointInRect(pTemp);

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
            if (waldRect.IsPointInRect(pTemp) || kolmcRect.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
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
                // Wald anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_CornyCholmc2_00000"), Start.stringManager.getTranslation("Loc2_CornyCholmc2_00001"), Start.stringManager.getTranslation("Loc2_CornyCholmc2_00002"),
                        fWald, 3, 0, 0);
                break;

            case 2:
                // Kolmc anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_CornyCholmc2_00003"),
                        Start.stringManager.getTranslation("Loc2_CornyCholmc2_00004"),
                        Start.stringManager.getTranslation("Loc2_CornyCholmc2_00005"),
                        fWjes, 3, 0, 0);
                break;

            case 50:
                // Krabat will Wald nicht benutzen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_CornyCholmc2_00006"),
                        Start.stringManager.getTranslation("Loc2_CornyCholmc2_00007"),
                        Start.stringManager.getTranslation("Loc2_CornyCholmc2_00008"),
                        fWald, 3, 0, 0);
                break;

            case 85:
                // Kolmc mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc2_CornyCholmc2_00009"),
                        Start.stringManager.getTranslation("Loc2_CornyCholmc2_00010"),
                        Start.stringManager.getTranslation("Loc2_CornyCholmc2_00011"),
                        fWjes, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Villa
                NeuesBild(85, 75);
                break;

            case 101:
                // gehe zu Labyrinth
                NeuesBild(77, 75);
                break;

            case 150:
                // Wald - Ausreden
                DingAusrede(fWald);
                break;

            case 155:
                // Kolmc - Ausreden
                DingAusrede(fWjes);
                break;

            case 1000:
                // Morphing beginnt
                muellermorph.Init(mlynkFeet, 80);  // 68 - 100 - scaleMueller
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
                NeuesBild(90, 75);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}