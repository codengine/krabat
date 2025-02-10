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

package de.codengine.locations;

import de.codengine.Start;
import de.codengine.anims.Bludnicki;
import de.codengine.anims.Plomja;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Labyr9 extends Mainlaby {
    private GenericImage background, lab92, lab93;

    private Bludnicki irrlicht;
    private Plomja feuer;

    private boolean isBlinker = false;
    private boolean bludVisible = false;
    private int Ausgang = 0;
    private static final GenericPoint Aunten = new GenericPoint(185, 430);
    private static final GenericPoint Alinks = new GenericPoint(10, 240);
    private static final GenericPoint Arechts = new GenericPoint(610, 312);
    private GenericPoint AusPoint = new GenericPoint(0, 0);

    // Konstanten - Rects
    private static final Borderrect linkerAusgang = new Borderrect(0, 269, 34, 383);
    private static final Borderrect rechterAusgang = new Borderrect(609, 224, 639, 311);
    private static final Borderrect untererAusgang = new Borderrect(236, 424, 460, 479);
    private static final Borderrect lab92Rect = new Borderrect(330, 166, 639, 361);
    private static final Borderrect lab93Rect = new Borderrect(0, 281, 107, 395);

    // Exitlocations - Array
    private static final int[] Exitright = {51, 52, /*53,*/ 54, 56, 57, 58, 61};
    private static final int[] Exitleft = {51, 52, /*53,*/ 55, 57, 58, 60, 62};
    private static final int[] Exitdown = {52, 54, 55, 58, 60, 61};

    // Konstante Points
    private static final GenericPoint Pleft = new GenericPoint(0, 323);
    private static final GenericPoint Pright = new GenericPoint(639, 261);
    private static final GenericPoint Pdown = new GenericPoint(353, 479);

    // Nach diesem Fusspunkt richtet sich die komplette Location !!!
    private static final GenericPoint bludFeet = new GenericPoint(218, 292);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Labyr9(Start caller, int Richtung) {
        super(caller);
        System.out.println("Laby 9");

        mainFrame = caller;
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 5.22f;
        mainFrame.krabat.defScale = -30;

        irrlicht = new Bludnicki(mainFrame);
        feuer = new Plomja(mainFrame);

        // Definitionen fuer Hauptklasse
        int th = bludFeet.x - (irrlicht.breite / 2);
        bludRect = new Borderrect(th, bludFeet.y - irrlicht.hoehe, th + irrlicht.breite, bludFeet.y);
        bludTalk = new GenericPoint(bludFeet.x, bludFeet.y - irrlicht.hoehe - 50);

        Pblud = new GenericPoint(177, 323);  // Krabats Anguck/Benutz-Punkt fuer Blud
        bludFacing = 3;
        locIndex = 59;

        InitLocation(Richtung);

        // hier Entscheidung, ob Blud da ist oder nicht
        if (mainFrame.Actions[235]) {
            bludVisible = true;
        }

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int Richtung) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(568, 243, 639, 280));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(458, 260, 567, 295));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(405, 282, 457, 309));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(357, 294, 404, 318));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(212, 301, 356, 318));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 299, 211, 336));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(309, 365, 309, 377, 319, 479));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(7);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(4, 6);
        mainFrame.wegSucher.PosVerbinden(3, 6);

        InitImages();
        switch (Richtung) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(10, true);
                Ausgang = BerechneAusgang(false, true, false, true);
                break;
            case 9:
                // von rechts aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(535, 283));
                mainFrame.krabat.SetFacing(9);
                Ausgang = BerechneAusgang(false, true, false, false);
                break;
            case 3:
                // von links aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(46, 321));
                mainFrame.krabat.SetFacing(3);
                Ausgang = BerechneAusgang(false, true, false, true);
                break;
            case 12:
                // von unten aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(353, 463));
                mainFrame.krabat.SetFacing(12);
                Ausgang = BerechneAusgang(false, false, false, true);
                break;
        }
        // Hier richtigen Punkt fuers Geblinker eintragen
        if (Ausgang == 3) {
            AusPoint = Arechts;
        }
        if (Ausgang == 9) {
            AusPoint = Alinks;
        }
        if (Ausgang == 6) {
            AusPoint = Aunten;
        }

        if (mainFrame.Actions[236]) {
            isBlinker = true;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/labyrinth/laby9.gif");
        lab92 = getPicture("gfx/labyrinth/lab9-2.gif");
        lab93 = getPicture("gfx/labyrinth/lab9-3.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        lab92 = null;
        lab93 = null;

        irrlicht.cleanup();
        irrlicht = null;
        feuer.cleanup();
        feuer = null;
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

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
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

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // Irrlichter zeichnen
        if (bludVisible) {
            g.setClip(bludRect.lo_point.x, bludRect.lo_point.y, irrlicht.breite, irrlicht.hoehe);
            g.drawImage(background, 0, 0, null);
            irrlicht.drawBludnicki(g, TalkPerson, bludRect.lo_point, hoerterzu, bludNimmt);
        }

        // Blinkern zeichnen
        if (isBlinker) {
            g.setClip(AusPoint.x, AusPoint.y, Plomja.Breite, Plomja.Hoehe);
            g.drawImage(background, 0, 0, null);
            feuer.drawPlomja(g, AusPoint);
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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab92Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab92, 343, 84, null);
        }

        // hinterm horiz3 (nur Clipping - Region wird neugezeichnet)
        if (lab93Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab93, 16, 320, null);
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

                // Ausreden fuer Irrlichter
                if ((bludRect.IsPointInRect(pTemp)) && (bludVisible)) {
                    if (mainFrame.whatItem == 15) {
                        nextActionID = 155;
                    } else {
                        nextActionID = 150;
                    }
                    pTemp = Pblud;
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

                // zu naechstem Laby gehen links
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

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

                // zu naechstem Laby gehen rechts
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

                // zu naechstem Laby gehen unten
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
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

                // Irrlichter ansehen
                if ((bludRect.IsPointInRect(pTemp)) && (bludVisible)) {
                    nextActionID = 1;
                    pTemp = Pblud;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Naechstes Laby anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Naechstes Laby anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Naechstes Laby anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Mit dem Irrlicht reden
                if ((bludRect.IsPointInRect(pTemp)) && (bludVisible)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pblud);
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
        // bei Multiple Choice eigene Routine aufrufen
        if (mainFrame.isMultiple) {
            Dialog.evalMouseMoveEvent(pTemp);
            return;
        }

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
            mainFrame.invHighCursor = (tmp.IsPointInRect(pTemp)) || ((bludRect.IsPointInRect(pTemp)) && (bludVisible));

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
            if ((bludRect.IsPointInRect(pTemp)) && (bludVisible)) {
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

            if (linkerAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 2;
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
    public void evalMouseExitEvent(GenericMouseEvent e) {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent(e);
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

    private void evalNewLocationLeft() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * Start.LabyHelp) + 50;
            while (zuffi > 62) {
                zuffi--;
            }
            for (int i = 0; i < Exitleft.length; i++) {
                if (zuffi == Exitleft[i]) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    private void evalNewLocationRight() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int i = 0; i < Exitright.length; i++) {
                if (zuffi == Exitright[i]) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    private void evalNewLocationDown() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int i = 0; i < Exitdown.length; i++) {
                if (zuffi == Exitdown[i]) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
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

        // Hier Verzweigung zu Blud - Nextactions, wenn noetig
        if ((nextActionID < 100) || (nextActionID > 130)) {
            BludAction(nextActionID);

            // Manchmal neuer Cursor wegen MC
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
            case 100:
                // Gehe zu naechstem Laby links
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                Erscheinen(Ausgang == 9);
                if (mainFrame.Actions[235]) {
                    BludLocationLeft();
                } else {
                    evalNewLocationLeft();
                }
                mainFrame.ConstructLocation(newloc, 9);
                mainFrame.DestructLocation(59);
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu naechstem Laby rechts
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                Erscheinen(Ausgang == 3);
                if (mainFrame.Actions[235]) {
                    BludLocationRight();
                } else {
                    evalNewLocationRight();
                }
                mainFrame.ConstructLocation(newloc, 3);
                mainFrame.DestructLocation(59);
                mainFrame.repaint();
                break;

            case 102:
                // gehe zu naechstem Laby unten
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                Erscheinen(Ausgang == 6);
                if (mainFrame.Actions[235]) {
                    BludLocationDown();
                } else {
                    evalNewLocationDown();
                }
                mainFrame.ConstructLocation(newloc, 6);
                mainFrame.DestructLocation(59);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}