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
import de.codengine.krabat.anims.Plomja;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Labyr4 extends Mainlaby {
    private GenericImage background;
    private GenericImage lab42;

    private Plomja feuer;

    private boolean isBlinker = false;
    private int Ausgang = 0;
    private static final GenericPoint Aoben = new GenericPoint(260, 143);
    private static final GenericPoint Alinks = new GenericPoint(10, 210);
    private static final GenericPoint Aunten = new GenericPoint(130, 460);
    private GenericPoint AusPoint = new GenericPoint(0, 0);

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(279, 163, 373, 203);
    private static final Borderrect linkerAusgang = new Borderrect(0, 227, 30, 333);
    private static final Borderrect untererAusgang = new Borderrect(152, 438, 357, 479);
    private static final Borderrect lab42Rect = new Borderrect(74, 164, 243, 329);

    // Exitlocation - Array
    private static final int[] Exitup = {53, 56, 59/*,60*/, /*62*/};
    private static final int[] Exitleft = {51, 52, 53, 55, 57, 58, 59, 60, 62};
    private static final int[] Exitdown = {52, 55, 58/*, 60*/, 61};

    // Konstante Points
    private static final GenericPoint Pleft = new GenericPoint(0, 280);
    private static final GenericPoint Pup = new GenericPoint(341, 192);
    private static final GenericPoint Pdown = new GenericPoint(252, 479);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Labyr4(Start caller, int Richtung) {
        super(caller);
        System.out.println("Laby 4");

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 300;
        mainFrame.krabat.zoomf = 5.07f;
        mainFrame.krabat.defScale = -30;

        feuer = new Plomja(mainFrame);

        InitLocation(Richtung);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int Richtung) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 312, 0, 287, 262, 294));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(237, 287, 220, 294, 295, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(301, 313, 270, 312, 224, 261));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(3);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(0, 2);

        InitImages();
        switch (Richtung) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(10, true);
                Ausgang = BerechneAusgang(true, true, false, false);
                break;
            case 3:
                // von links aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(27, 283));
                mainFrame.krabat.SetFacing(3);
                Ausgang = BerechneAusgang(true, true, false, false);
                break;
            case 6:
                // von oben aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(304, 237));
                mainFrame.krabat.SetFacing(6);
                Ausgang = BerechneAusgang(false, true, false, false);
                break;
            case 12:
                // von unten aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(252, 458));
                mainFrame.krabat.SetFacing(12);
                Ausgang = BerechneAusgang(true, false, false, false);
                break;
        }
        // Hier richtigen Punkt fuers Geblinker eintragen
        if (Ausgang == 6) {
            AusPoint = Aunten;
        }
        if (Ausgang == 9) {
            AusPoint = Alinks;
        }
        if (Ausgang == 12) {
            AusPoint = Aoben;
        }

        if (mainFrame.Actions[236]) {
            isBlinker = true;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/labyrinth/laby4.gif");
        lab42 = getPicture("gfx/labyrinth/lab4-2.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        lab42 = null;

        feuer.cleanup();
        feuer = null;
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
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Blinkern zeichnen
        if (isBlinker) {
            g.setClip(AusPoint.x, AusPoint.y, Plomja.Breite, Plomja.Hoehe);
            g.drawImage(background, 0, 0);
            feuer.drawPlomja(g, AusPoint);
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
        if (lab42Rect.IsPointInRect(pKrTemp)) {
            g.drawImage(lab42, 77, 135);
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

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
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

                // zu naechstem Laby gehen oben
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

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Naechstes Laby anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Naechstes Laby anschauen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Naechstes Laby anschauen
                if (untererAusgang.IsPointInRect(pTemp)) {
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp);

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
      /*if ((brEingang.IsPointInRect (pTemp) == true) ||
          (brWagen.IsPointInRect (pTemp) == true))
      {
        if (Cursorform != 1)
        {
          mainFrame.setCursor (mainFrame.Kreuz);
          Cursorform = 1;
        }
        return;
      }*/

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
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

    private void evalNewLocationUp() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int i = 0; i < Exitup.length; i++) {
                if (zuffi == Exitup[i]) {
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
                mainFrame.DestructLocation(54);
                mainFrame.repaint();
                break;

            case 101:
                // gehe zu naechstem Laby oben
                mainFrame.Clipset = false;
                mainFrame.isAnim = false;
                nextActionID = 0;
                Erscheinen(Ausgang == 12);
                if (mainFrame.Actions[235]) {
                    BludLocationUp();
                } else {
                    evalNewLocationUp();
                }
                mainFrame.ConstructLocation(newloc, 12);
                mainFrame.DestructLocation(54);
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
                mainFrame.DestructLocation(54);
                mainFrame.repaint();
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}