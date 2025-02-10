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
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Kupa1 extends Mainloc {
    private GenericImage background, kupa3, kupa4, kupa5, kupa2a, rohodz;

    private int WhichItem = 0;

    // Konstanten - Rects
    private static final Borderrect rechterAusgang = new Borderrect(610, 336, 639, 405);
    private static final Borderrect kupa3Rect = new Borderrect(114, 293, 271, 396);
    private static final Borderrect rohodzRect = new Borderrect(0, 341, 81, 479);
    private static final Borderrect schildRect = new Borderrect(324, 361, 360, 390);

    private static final int[] Carray = {363, 363, 363, 364, 364, 365, 365, 365, 366, 366,
            366, 366, 366, 366, 366, 367, 367, 367, 367, 367,
            367, 367, 367, 367, 368, 368, 368, 368, 368, 368,
            368, 368, 368, 368, 368, 368, 368, 368, 369, 369,
            369, 369, 369, 369, 369, 369, 369, 369, 369, 369,
            369, 369, 369, 369, 369, 369, 369, 369, 369, 369,
            369, 369, 369, 369, 369, 369, 369, 367, 367, 367,
            367, 367, 367, 367, 367, 367, 367, 367, 367, 367,
            367, 367, 368, 368, 368, 368, 367, 367, 367, 367,
            367, 367, 367, 367, 367, 367, 367, 367, 367, 367,

            367, 367, 367, 367, 367, 367, 366, 366, 366, 366,
            366, 366, 366, 366, 366, 366, 366, 366, 366, 365,
            365, 365, 364, 364, 364, 363, 363, 363, 363, 363,
            362, 361, 361, 361, 361, 360, 359, 359, 359, 358,
            358, 357, 356, 356, 356, 356, 356, 356, 356, 356};

    // Konstante Punkte
    private static final GenericPoint Pright = new GenericPoint(639, 378);
    private static final GenericPoint Prohodz = new GenericPoint(117, 450);
    private static final GenericPoint Pschild = new GenericPoint(356, 368);

    // fuers Blinkern
    private static final Bordertrapez Blink[] =
            {new Bordertrapez(0, 331, 150, 349),
                    new Bordertrapez(74, 150, 74, 87, 350, 404),
                    new Bordertrapez(77, 405, 84, 452),
                    new Bordertrapez(77, 92, 77, 128, 453, 479),
                    new Bordertrapez(238, 324, 402, 351),
                    new Bordertrapez(254, 400, 283, 373, 352, 360),
                    new Bordertrapez(297, 376, 297, 343, 385, 435),
                    new Bordertrapez(297, 343, 250, 379, 436, 479),
                    new Bordertrapez(403, 324, 430, 328),
                    new Bordertrapez(431, 325, 501, 326),
                    new Bordertrapez(523, 330, 639, 334),
                    new Bordertrapez(553, 639, 626, 639, 335, 353)};

    private int[][][] MerkArray;
    private static final int HAEUFIGKEITSKONSTANTE = 1000;
    private static final int ANIMZUSTAENDE = 4;

    // Konstante ints
    private static final int fRohodz = 9;
    private static final int fSchild = 9;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Kupa1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 480;
        mainFrame.krabat.zoomf = 2.0f;
        mainFrame.krabat.defScale = -20;

        InitLocation(oldLocation);

        // fuer Blinkern rein
        InitBlinker();

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 10:
                // von Weiden aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(612, 376));
                mainFrame.krabat.SetFacing(9);
                break;
        }
        InitMatrix();
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/kupa/kupa2.gif");
        kupa3 = getPicture("gfx/kupa/kupa3.gif");
        kupa4 = getPicture("gfx/kupa/kupa4.gif");
        kupa5 = getPicture("gfx/kupa/kupa5.gif");
        kupa2a = getPicture("gfx/kupa/kupa2a.gif");
        rohodz = getPicture("gfx/kupa/krohodz.gif");

        loadPicture();
    }

    private void InitMatrix() {
        // Entscheidung, ob Schild schon reingelegt oder noch nicht
        if (mainFrame.Actions[224] == true) {
            // Schild ist drin
            // Grenzen setzen
            mainFrame.wegGeher.vBorders.removeAllElements();
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(543, 570, 615, 639, 358, 382));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(500, 350, 550, 357));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(446, 345, 499, 354));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(425, 348, 445, 356));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(412, 355, 424, 362));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(262, 356, 411, 371));

            // Borders auf der Insel
            // mainFrame.wegGeher.vBorders.addElement (new bordertrapez (243, 361, 261, 404));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(210, 261, 210, 261, 356, 371));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(166, 209, 156, 209, 368, 371));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(156, 158, 123, 125, 372, 404));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(117, 252, 117, 181, 405, 450));

            // Matrix loeschen
            mainFrame.wegSucher.ClearMatrix(10);

            // Wege eintragen
            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 2);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(4, 5);
            mainFrame.wegSucher.PosVerbinden(5, 6);
            mainFrame.wegSucher.PosVerbinden(6, 7);
            mainFrame.wegSucher.PosVerbinden(7, 8);
            mainFrame.wegSucher.PosVerbinden(8, 9);
        } else {
            // noch kein Schild drin, nix mit auf die Insel geh...
            // Grenzen setzen
            mainFrame.wegGeher.vBorders.removeAllElements();
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(543, 570, 615, 639, 358, 382));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(500, 350, 550, 357));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(446, 345, 499, 354));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(425, 348, 445, 356));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(412, 355, 424, 362));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(356, 356, 411, 371));

            // Matrix loeschen
            mainFrame.wegSucher.ClearMatrix(6);

            // Wege eintragen
            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(1, 2);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(4, 5);
        }
        return;
    }

    private void InitBlinker() {
        // hier wird das Blinkern festgelegt, indem das Array initialisiert wird, wo der
        // Blinkstatus gespeichert wird

        // So viele Borderrects
        int AnzahlRects = Blink.length;

        // So viele Sachen sind zu merken
        int AnzahlMerksachen = 3;

        // So viele Striche sollen in den borderrects erscheinen
        int AnzahlStriche = 1;

        for (int i = 0; i < Blink.length; i++) {
            if ((Blink[i].Flaeche() / HAEUFIGKEITSKONSTANTE) > AnzahlStriche) {
                AnzahlStriche = Blink[i].Flaeche() / HAEUFIGKEITSKONSTANTE;
            }
        }

        // Groesse des Arrays steht fest, also Init
        MerkArray = new int[AnzahlRects][AnzahlStriche][AnzahlMerksachen];

        // Jetzt das Array initialisieren und beschraenken, da nicht alle borderrects so viele Striche haben wie das Groesste
        for (int i = 0; i < MerkArray.length; i++) {
            for (int j = 0; j < MerkArray[i].length; j++) {
                // mit -1 kennzeichnen, das dieser Eintrag nicht beachtet werden soll
                if (((Blink[i].Flaeche() / HAEUFIGKEITSKONSTANTE) < j) && (j > 0)) {
                    MerkArray[i][j][2] = -1;
                } else {
                    // gewisse Anfangszufaelligkeit zuweisen, damit nicht alle im selben Status
                    int zuffZahl = 0;

                    do {
                        zuffZahl = (int) Math.round(Math.random() * 7);
                    }
                    while (zuffZahl > ANIMZUSTAENDE);

                    MerkArray[i][j][2] = zuffZahl;
                }

                // x - und y - Koordinate = 0 -> Platz ist frei
                MerkArray[i][j][0] = 0;
                MerkArray[i][j][1] = 0;
            }
        }
    }

    public void cleanup() {
        background = null;
        kupa3 = null;
        kupa4 = null;
        kupa5 = null;
        kupa2a = null;
        rohodz = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation(GenericDrawingContext g) {
        // Gegenstaende in Location veraendert
        if (mainFrame.krabat.fAnimHelper == true) {
            if (WhichItem == 17) {
                mainFrame.inventory.vInventory.addElement(new Integer(17));
                mainFrame.Clipset = false;
                mainFrame.krabat.fAnimHelper = false;
                mainFrame.Actions[917] = true;
                WhichItem = 0;
            }
            if (WhichItem == 500) {
                mainFrame.inventory.vInventory.removeElement(new Integer(3));
                mainFrame.Clipset = false;
                mainFrame.krabat.fAnimHelper = false;
                mainFrame.Actions[224] = true;
                InitMatrix();
                WhichItem = 0;
            }
        }

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

        // Blinkern ermoeglichen
        g.setClip(0, 323, 639, 479);
        g.drawImage(background, 0, 0, null);
        Blink(g);

        // fehlendes Stueck Bruecke zeichnen
        if (mainFrame.Actions[224] == false) {
            g.setClip(324, 361, 37, 28);
            g.drawImage(kupa5, 324, 361, null);
        } else {
            g.setClip(324, 361, 37, 28);
            g.drawImage(kupa2a, 324, 361, null);
        }

        // Hier Rohodz zeichnen, wenn noch nicht aufgehoben
        g.setClip(48, 388, 44, 61);
        g.drawImage(kupa4, 48, 388, null); // dieses Stueck immer, da jetzt der andere kommt
        if (mainFrame.Actions[917] == false) {
            g.setClip(50, 415, 49, 37);
            g.drawImage(rohodz, 50, 415, null);
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        mainFrame.wegGeher.GeheWeg();

        mainFrame.krabat.SetKrabatPos(CorrectY(mainFrame.krabat.GetKrabatPos()));

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

        // hinter kupa3 (nur Clipping - Region wird neugezeichnet)
        if (kupa3Rect.IsPointInRect(pKrTemp) == true) {
            g.drawImage(kupa3, 138, 281, null);
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

                // Ausreden fuer Rohodz
                if ((rohodzRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[917] == false) &&
                        (mainFrame.Actions[224] == true)) {
                    // Extra - Sinnloszeug
                    nextActionID = 150;
                    pTemp = Prohodz;
                }

                // Ausreden fuer Schild
                if ((schildRect.IsPointInRect(pTemp) == true) &&
                        (mainFrame.Actions[224] == false)) {
                    if (mainFrame.whatItem == 3) {
                        nextActionID = 160;
                        pTemp = Pschild;
                    } else {
                        // Extra - Sinnloszeug
                        nextActionID = 155;
                        pTemp = Pschild;
                    }
                }

                // wenn nichts anderes gewaehlt, dann nur hinlaufen
                SetzeNeuenWeg(pTemp);
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

                // Rohodz ansehen
                if ((rohodzRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[917] == false) &&
                        (mainFrame.Actions[224] == true)) {
                    nextActionID = 1;
                    pTemp = Prohodz;
                }

                // Schild ansehen
                if ((schildRect.IsPointInRect(pTemp) == true) &&
                        (mainFrame.Actions[224] == false)) {
                    nextActionID = 2;
                    pTemp = Pschild;
                }

                // zu Weiden gehen
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

                SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Weiden anschauen
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Rohodz nehmen
                if ((rohodzRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[917] == false) &&
                        (mainFrame.Actions[224] == true)) {
                    nextActionID = 50;
                    SetzeNeuenWeg(Prohodz);
                    mainFrame.repaint();
                    return;
                }

                // Schild nehmen
                if ((schildRect.IsPointInRect(pTemp) == true) &&
                        (mainFrame.Actions[224] == false)) {
                    nextActionID = 55;
                    SetzeNeuenWeg(Pschild);
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
            if ((tmp.IsPointInRect(pTemp) == true) || ((rohodzRect.IsPointInRect(pTemp) == true) &&
                    (mainFrame.Actions[917] == false) && (mainFrame.Actions[224] == true)) ||
                    ((mainFrame.Actions[224] == false) && (schildRect.IsPointInRect(pTemp) == true))) {
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
            if (((rohodzRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[917] == false) &&
                    (mainFrame.Actions[224] == true)) || ((schildRect.IsPointInRect(pTemp) == true) &&
                    (mainFrame.Actions[224] == false))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (rechterAusgang.IsPointInRect(pTemp) == true) {
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

    // Routinen fuer veraendertes Laufen auf definierter Linie

    private void SetzeNeuenWeg(GenericPoint dest) {
        GenericPoint right = CorrectSetY(dest);
        mainFrame.wegGeher.SetzeNeuenWeg(right);
    }

    private GenericPoint CorrectY(GenericPoint dst) {
        // Y - Koordinate beim Laufen nur auf definiertem Stueck beachten
        if (dst.x > 411) {
            return dst;
        }
        if (dst.x < 262) {
            return dst;
        } else {
            return (new GenericPoint(dst.x, Carray[dst.x - 262] + 2));
        }
    }

    private GenericPoint CorrectSetY(GenericPoint dst) {
        // Hier wird Y-Koordinate auch in anderen Rects beeinflusst -> "Ausschalten" der BestRect - Routine
        if (dst.x > 499) {
            return dst;
        }
        if (dst.x > 445) {
            return (new GenericPoint(dst.x, 350));
        }
        if (dst.x > 424) {
            return (new GenericPoint(dst.x, 352));
        }
        if (dst.x > 411) {
            return (new GenericPoint(dst.x, 359));
        }

        // Hier Korrektur, wenn Schild noch nicht in Location
        if (mainFrame.Actions[224] == false) {
            if (dst.x < 362) {
                return (CorrectY(new GenericPoint(362, 300)));
            }
        }
        if (dst.x < 262) {
            return dst; //(new GenericPoint (262, 363));
        }
        return (new GenericPoint(dst.x, Carray[dst.x - 262] + 2));
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

    // Hier ist die Blinkerroutine //////////////////////////////////////////

    private void Blink(GenericDrawingContext g) {
        g.setColor(GenericColor.white);

        // Das Array Stueck fuer Stueck abarbeiten
        for (int i = 0; i < MerkArray.length; i++) {
            for (int j = 0; j < MerkArray[i].length; j++) {
                // nur bearbeiten, falls der Eintrag nicht gesperrt ist
                if (MerkArray[i][j][2] > -1) {
                    // ein leeres Feld bekommt einen neuen Eintrag zugewiesen
                    if ((MerkArray[i][j][0] == 0) && (MerkArray[i][j][1] == 0)) {
                        // gewisse Haeufigkeit fuer Neubelegung festlegen
                        int zuffZahl = (int) Math.round(Math.random() * 50);
                        if (zuffZahl > 25) {
                            // Werte fuer Zufallsgenerator berechnen
                            int xlaenge = ((Blink[i].x2 > Blink[i].x4) ? Blink[i].x2 : Blink[i].x4)
                                    - ((Blink[i].x1 < Blink[i].x3) ? Blink[i].x1 : Blink[i].x3);
                            int ylaenge = Blink[i].y2 - Blink[i].y1;
                            int xoffset = ((Blink[i].x1 < Blink[i].x3) ? Blink[i].x1 : Blink[i].x3);

                            // ungueltige Werte nicht beachten
                            do {
                                MerkArray[i][j][0] = (int) Math.round(Math.random() * xlaenge) + xoffset;
                                MerkArray[i][j][1] = (int) Math.round(Math.random() * ylaenge) + Blink[i].y1;
                            }
                            while (Blink[i].PointInside(new GenericPoint(MerkArray[i][j][0], MerkArray[i][j][1])) == false);
                        }
                    }

                    // wenn der Eintrag gueltig ist, dann auch zeichnen
                    if ((MerkArray[i][j][0] != 0) && (MerkArray[i][j][1] != 0)) {
                        // den Eintrag zeichnen
                        switch (MerkArray[i][j][2]) {
                            case 0: // Ein Punkt
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0], MerkArray[i][j][1]);
                                break;

                            case 1: // Zwei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 1, MerkArray[i][j][1]);
                                break;

                            case 2: // Drei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 2, MerkArray[i][j][1]);
                                break;

                            case 3: // Zwei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 1, MerkArray[i][j][1]);
                                break;

                            case 4: // Ein Punkt
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0], MerkArray[i][j][1]);
                                break;

                            default:
                                System.out.println("Fehler in Blinkerroutine !!!");

                        }

                        // Status weiterzaehlen
                        MerkArray[i][j][2]++;

                        // wenn zuende, dann loeschen
                        if (MerkArray[i][j][2] > ANIMZUSTAENDE) {
                            MerkArray[i][j][0] = 0;
                            MerkArray[i][j][1] = 0;
                            MerkArray[i][j][2] = 0;
                        }
                    }
                }
            }
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
                // Rohodz anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kupa1_00000"),
                        Start.stringManager.getTranslation("Loc1_Kupa1_00001"),
                        Start.stringManager.getTranslation("Loc1_Kupa1_00002"),
                        fRohodz, 3, 0, 0);
                break;

            case 2:
                // Schild anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Kupa1_00003"),
                        Start.stringManager.getTranslation("Loc1_Kupa1_00004"),
                        Start.stringManager.getTranslation("Loc1_Kupa1_00005"),
                        fSchild, 3, 0, 0);
                break;

            case 50:
                // Rohodz benutzen
                nextActionID = 0;
                mainFrame.wave.PlayFile("sfx/rohodz.wav");
                mainFrame.krabat.SetFacing(9);
                WhichItem = 17;
                mainFrame.krabat.nAnimation = 92;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                break;

            case 55:
                // Schild benutzen
                int zuffZahl = (int) Math.round(Math.random() * 2);
                if (zuffZahl == 2) {
                    zuffZahl = 1;
                }
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Kupa1_00006"),
                                Start.stringManager.getTranslation("Loc1_Kupa1_00007"),
                                Start.stringManager.getTranslation("Loc1_Kupa1_00008"),
                                fSchild, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Kupa1_00009"),
                                Start.stringManager.getTranslation("Loc1_Kupa1_00010"),
                                Start.stringManager.getTranslation("Loc1_Kupa1_00011"),
                                fSchild, 3, 0, 0);
                        break;
                }
                break;

            case 100:
                // gehe zu Wjerby
                NeuesBild(10, 12);
                break;

            case 150:
                // Rohodz - Ausreden
                DingAusrede(fRohodz);
                break;

            case 155:
                // Schild - Ausreden
                DingAusrede(fSchild);
                break;

            case 160:
                // Schild hinlegen
                nextActionID = 0;
                mainFrame.wave.PlayFile("sfx/schildlegen.wav");
                mainFrame.krabat.SetFacing(9);
                WhichItem = 500;
                mainFrame.krabat.nAnimation = 130;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.invCursor = false;
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}