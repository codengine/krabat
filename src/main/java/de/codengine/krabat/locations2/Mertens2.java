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
import de.codengine.krabat.anims.Kacka1;
import de.codengine.krabat.anims.Kacka2;
import de.codengine.krabat.anims.WodnyMuz;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Mertens2 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Mertens2.class);
    private GenericImage background;

    private WodnyMuz wmann;
    private final Multiple2 Dialog;
    private boolean isShowing = false;
    private boolean isTauching = false;

    private int Counter;
    private boolean noCounter = false;

    private boolean setAnim = false;
    private int whichAnim = 0;

    private final GenericPoint wmannTalk;
    private final Borderrect wmannRect;

    private Kacka1 kacka1;
    private Kacka2 kacka2;
    private boolean kacka1Visible = false;
    private boolean kacka2Visible = false;
    private boolean kacka1IsLeft = false;
    private boolean kacka1IsMoving = true;
    private boolean kacka2FliegtNoch = true;

    private GenericPoint kacka1TalkPoint;
    private GenericPoint kacka2TalkPoint;

    // Punkte in Location
    private static final GenericPoint Pleft = new GenericPoint(0, 305);
    private static final GenericPoint wmannFeet = new GenericPoint(365, 373);
    // private static final GenericPoint wmannfrauTalk = new GenericPoint (240,  60);
    private static final GenericPoint Pwmann = new GenericPoint(59, 305);
    private static final GenericPoint Pverlassen = new GenericPoint(-50, 305);

    // Konstanten - Rects deklarieren
    private static final Borderrect linkerAusgang = new Borderrect(0, 261, 29, 341);
    private static final Borderrect wasserobenRect = new Borderrect(0, 181, 452, 240);
    private static final Borderrect wasseruntenRect = new Borderrect(122, 338, 428, 400);

    // fuers Blinkern
    private static final Bordertrapez[] Blink =
            {new Bordertrapez(597, 195, 639, 413),
                    new Bordertrapez(543, 231, 554, 285),
                    new Bordertrapez(552, 303, 556, 339),
                    new Bordertrapez(553, 183, 561, 203),
                    new Bordertrapez(504, 517, 505, 506, 178, 212),
                    new Bordertrapez(500, 501, 485, 511, 290, 419),
                    new Bordertrapez(388, 456, 388, 435, 173, 381),
                    new Bordertrapez(388, 435, 388, 415, 382, 401),
                    new Bordertrapez(358, 387, 83, 387, 289, 354),
                    new Bordertrapez(83, 387, 178, 387, 355, 401),
                    new Bordertrapez(0, 193, 387, 226),
                    new Bordertrapez(155, 387, 0, 387, 175, 192),
                    new Bordertrapez(34, 152, 152, 153, 227, 249),
                    new Bordertrapez(153, 227, 180, 246),
                    new Bordertrapez(181, 227, 330, 252)};

    private int[][][] MerkArray;
    private static final int HAEUFIGKEITSKONSTANTE = 1000;
    private static final int ANIMZUSTAENDE = 4;

    // Konstante ints
    private static final int fWoda = 0;
    private static final int fWmuz = 3;

    private int Counter2 = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mertens2(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(26, true);

        mainFrame.krabat.maxx = 323;
        mainFrame.krabat.zoomf = 3.05f;
        mainFrame.krabat.defScale = -20;

        InitLocation(oldLocation);

        wmann = new WodnyMuz(mainFrame, mainFrame.Actions[207]);
        Dialog = new Multiple2(mainFrame);

        wmannTalk = new GenericPoint();
        wmannTalk.x = wmannFeet.x;
        wmannTalk.y = wmannFeet.y - WodnyMuz.Tauchhoehe - 50;

        int wmannxk = wmannFeet.x - WodnyMuz.Breite / 2;
        // die verschiedenen Hoehenangaben sind richtig, da "Fusspunkt" nicht Ende des Images !
        wmannRect = new Borderrect(wmannxk, wmannFeet.y - WodnyMuz.Tauchhoehe, wmannxk + WodnyMuz.Breite, wmannFeet.y - WodnyMuz.Tauchhoehe + WodnyMuz.Hoehe);

        Counter = (int) Math.round(Math.random() * 60);
        Counter += 40;

        kacka1 = new Kacka1(mainFrame);
        kacka2 = new Kacka2(mainFrame);

        // fuer Blinkern rein
        InitBlinker();

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 150, 0, 13, 288, 323));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(284, 307, 65, 185, 266, 287));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(2);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                // keine Veraenderung von "istoben"
                break;
            case 81:
                // von Pinca aus (Anim)
                mainFrame.krabat.setPos(new GenericPoint(59, 305));
                mainFrame.krabat.SetFacing(3);
                setAnim = true;
                noCounter = true;
                isShowing = true;
                whichAnim = 1100;
                mainFrame.Actions[207] = true; // bei Anim ist Wassermann schon oben
                kacka1Visible = true; // erste Ente schon da (Wassermann)
                break;
            case 76:
                // von Kulow aus
                mainFrame.krabat.setPos(new GenericPoint(38, 305));
                mainFrame.krabat.SetFacing(3);
                mainFrame.Actions[207] = false; // Wassermann taucht erst auf
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mertens/mertens.gif");

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

        for (Bordertrapez bordertrapez : Blink) {
            if (bordertrapez.Flaeche() / HAEUFIGKEITSKONSTANTE > AnzahlStriche) {
                AnzahlStriche = bordertrapez.Flaeche() / HAEUFIGKEITSKONSTANTE;
            }
        }

        // Groesse des Arrays steht fest, also Init
        MerkArray = new int[AnzahlRects][AnzahlStriche][AnzahlMerksachen];

        // Jetzt das Array initialisieren und beschraenken, da nicht alle borderrects so viele Striche haben wie das Groesste
        for (int i = 0; i < MerkArray.length; i++) {
            for (int j = 0; j < MerkArray[i].length; j++) {
                // mit -1 kennzeichnen, das dieser Eintrag nicht beachtet werden soll
                if (Blink[i].Flaeche() / HAEUFIGKEITSKONSTANTE < j && j > 0) {
                    MerkArray[i][j][2] = -1;
                } else {
                    // gewisse Anfangszufaelligkeit zuweisen, damit nicht alle im selben Status
                    int zuffZahl;

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

    @Override
    public void cleanup() {
        background = null;

        wmann.cleanup();
        wmann = null;
        kacka1.cleanup();
        kacka1 = null;
        kacka2.cleanup();
        kacka2 = null;
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
            if (setAnim) {
                mainFrame.fPlayAnim = true;
            }
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        g.drawImage(background, 0, 0);

        /*if (mainFrame.isAnim == true)
          {
          switchanim = ! (switchanim);
          if (switchanim == true)
          {
          if (forward == true)
          {
          Wellencount++;
          if (Wellencount == 10)
          {
          Wellencount = 8;
          forward = false;
          }
          }
          else
          {
          Wellencount--;
          if (Wellencount == 0)
          {
          Wellencount = 2;
          forward = true;
          }
          }
          }	
          g.setClip (  0, 169, 640, 241);
          g.drawImage (Wellen[Wellencount], 0, 169, this);
          }	*/

        // fuers Blinkern rein
        g.setClip(0, 157, 639, 276);
        g.drawImage(background, 0, 0);
        Blink(g);

        // erste Ente zeichnen, wenn da
        if (kacka1Visible) {
            g.setClip(kacka1.kackaRect());
            g.drawImage(background, 0, 0);
            kacka1.drawKacka(g, TalkPerson, kacka1IsLeft, kacka1IsMoving);
        }

        // zweite Ente zeichnen, wenn da
        if (kacka2Visible) {
            g.setClip(kacka2.kackaRect());
            g.drawImage(background, 0, 0);
            kacka2FliegtNoch = kacka2.drawKacka(g, TalkPerson);
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Counter runterzaehlen fuer Wassermannerscheinen, nur so lange, bis Zeichnung gezeigt
        if (Counter > 0 && !mainFrame.Actions[302] && !noCounter) {
            Counter--;
            if (Counter < 1) {
                setAnim = true;
                whichAnim = 2000;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }
        }

        // Wassermann zeichnen beim Schwimmen und Reden
        if (isShowing) {
            g.setClip(wmannRect.lo_point.x, wmannRect.lo_point.y, WodnyMuz.Breite, WodnyMuz.Tauchhoehe);
            g.drawImage(background, 0, 0);
            wmann.drawWmuz(g, TalkPerson, wmannRect.lo_point);
        }

        // Wassermann zeichnen beim Auf / Abtauchen
        if (isTauching) {
            g.setClip(wmannRect.lo_point.x, wmannRect.lo_point.y, WodnyMuz.Breite, WodnyMuz.Tauchhoehe);
            g.drawImage(background, 0, 0);
            isTauching = wmann.Tauche(g, wmannRect.lo_point);
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
            if (mainFrame.talkCount > 0 && TalkPerson != 0) {
                // beim Reden
                switch (TalkPerson) {
                    case 1:
                        // Krabat spricht gestikulierend
                        mainFrame.krabat.talkKrabat(g);
                        break;
                    case 3:
                        // Krabat beim beschreibenden Reden
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

        // Multiple Choice ausfuehren
        if (mainFrame.isMultiple) {
            mainFrame.Clipset = false;
            Dialog.paintMultiple(g);
            return;
        }

        if (setAnim) {
            setAnim = false;
            mainFrame.krabat.StopWalking();
            nextActionID = whichAnim;
            whichAnim = 0;
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer Wasser, kein Punkt
                if (wasserobenRect.IsPointInRect(pTemp) || wasseruntenRect.IsPointInRect(pTemp)) {
                    // wuda + wacka
                    nextActionID = mainFrame.whatItem == 10 ? 170 : 160;
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

                // zu Kulow gehen ?
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

                // Wasser ansehen
                if (wasserobenRect.IsPointInRect(pTemp) || wasseruntenRect.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Kulow anschauen
                if (linkerAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Wasser mitnehmen
                if (wasserobenRect.IsPointInRect(pTemp) || wasseruntenRect.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || wasserobenRect.IsPointInRect(pTemp) ||
                    wasseruntenRect.IsPointInRect(pTemp);

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
            if (wasserobenRect.IsPointInRect(pTemp) || wasseruntenRect.IsPointInRect(pTemp)) {
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

            // sonst normal-Cursor
            if (Cursorform != 0) {
                mainFrame.setCursor(mainFrame.Normal);
                Cursorform = 0;
            }
        }
    }

    @Override
    public void evalMouseExitEvent() {
        if (mainFrame.isMultiple) {
            Dialog.evalMouseExitEvent();
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

    private void Blink(GenericDrawingContext g) {
        g.setColor(GenericColor.white);

        // Das Array Stueck fuer Stueck abarbeiten
        for (int i = 0; i < MerkArray.length; i++) {
            for (int j = 0; j < MerkArray[i].length; j++) {
                // nur bearbeiten, falls der Eintrag nicht gesperrt ist
                if (MerkArray[i][j][2] > -1) {
                    // ein leeres Feld bekommt einen neuen Eintrag zugewiesen
                    if (MerkArray[i][j][0] == 0 && MerkArray[i][j][1] == 0) {
                        // gewisse Haeufigkeit fuer Neubelegung festlegen
                        int zuffZahl = (int) Math.round(Math.random() * 50);
                        if (zuffZahl > 25) {
                            // Werte fuer Zufallsgenerator berechnen
                            int xlaenge = Math.max(Blink[i].x2, Blink[i].x4)
                                    - Math.min(Blink[i].x1, Blink[i].x3);
                            int ylaenge = Blink[i].y2 - Blink[i].y1;
                            int xoffset = Math.min(Blink[i].x1, Blink[i].x3);

                            // ungueltige Werte nicht beachten
                            do {
                                MerkArray[i][j][0] = (int) Math.round(Math.random() * xlaenge) + xoffset;
                                MerkArray[i][j][1] = (int) Math.round(Math.random() * ylaenge) + Blink[i].y1;
                            }
                            while (!Blink[i].PointInside(new GenericPoint(MerkArray[i][j][0], MerkArray[i][j][1])));
                        }
                    }

                    // wenn der Eintrag gueltig ist, dann auch zeichnen
                    if (MerkArray[i][j][0] != 0 && MerkArray[i][j][1] != 0) {
                        // den Eintrag zeichnen
                        switch (MerkArray[i][j][2]) {
                            case 0: // Ein Punkt
                            case 4:
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0], MerkArray[i][j][1]);
                                break;

                            case 1: // Zwei Punkte
                            case 3:
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 1, MerkArray[i][j][1]);
                                break;

                            case 2: // Drei Punkte
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 2, MerkArray[i][j][1]);
                                break;

                            default:
                                log.error("Fehler in Blinkerroutine !!! {}", MerkArray[i][j][2]);

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
            case 2:
                // Wasser anschauen
                KrabatSagt("Mertens2_1", fWoda, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Wmann benutzen)
                mainFrame.krabat.SetFacing(fWmuz);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);

                // Abfrage, ob schon begruesst
                if (!mainFrame.Actions[290]) {
                    nextActionID = 60;
                } else {
                    nextActionID = 70;
                }
                break;

            case 55:
                // Wasser mitnehmen
                KrabatSagt("Mertens2_2", fWoda, 3, 0, 0);
                break;

            case 60:
                // Wassermann begruesst Krabat
                PersonSagt("Mertens2_47", 0, 34, 2, 70, wmannTalk);
                mainFrame.Actions[290] = true;
                break;

            case 70:
                // Hier Unterscheidung, welcher Dialog gemeint..
                if (!mainFrame.Actions[276]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 900;
                }
                break;

            case 100:
                // Gehe zu Kulow
                NeuesBild(76, 79);
                break;

            case 160:
                // Wasser - Ausreden
                DingAusrede(fWoda);
                break;

            case 170:
                // wuda auf Wasser ausreden
                KrabatSagt("Mertens2_3", fWoda, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00042"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00043"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00044"), 1000, 280, new int[]{280}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00045"), 280, 281, new int[]{281}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00046"), 281, 282, new int[]{282, 283, 267}, 650);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00047"), 283, 1000, null, 660);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00048"), 1000, 284, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00049"), 284, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00050"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00051"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00052"), 1000, 280, new int[]{280}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00053"), 280, 281, new int[]{281}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00054"), 281, 282, new int[]{282, 283, 267}, 650);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00055"), 283, 1000, null, 660);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00056"), 1000, 284, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00057"), 284, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00058"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00059"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00060"), 1000, 280, new int[]{280}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00061"), 280, 281, new int[]{281}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00062"), 281, 282, new int[]{282, 283, 267}, 650);

                    // 4. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00063"), 283, 1000, null, 660);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00064"), 1000, 284, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00065"), 284, 1000, null, 800);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[284] = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 610:
                // Reaktion Wmuz auf 1. Teil 1. Frage
                PersonSagt("Mertens2_4", 0, 34, 2, 600, wmannTalk);
                break;

            case 620:
                // Reaktion Wmuz auf 2. Teil 1. Frage
                PersonSagt("Mertens2_5", 0, 34, 2, 600, wmannTalk);
                break;

            case 630:
                // Reaktion Wmuz auf 1. Teil 2. Frage
                PersonSagt("Mertens2_6", 0, 34, 2, 600, wmannTalk);
                break;

            case 640:
                // Reaktion Wmuz auf 2. Teil 2. Frage
                PersonSagt("Mertens2_7", 0, 34, 2, 641, wmannTalk);
                break;

            case 641:
                // Reaktion Wmuz auf 2. Teil 2. Frage
                PersonSagt("Mertens2_8", 0, 34, 2, 600, wmannTalk);
                break;

            case 650:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_9", 0, 34, 2, 651, wmannTalk);
                break;

            case 651:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_37", 0, 34, 2, 652, wmannTalk);
                break;

            case 652:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_38", 0, 34, 2, 653, wmannTalk);
                break;

            case 653:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_39", 0, 34, 2, 654, wmannTalk);
                break;

            case 654:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_40", 0, 34, 2, 655, wmannTalk);
                break;

            case 655:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_41", 0, 34, 2, 656, wmannTalk);
                break;

            case 656:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_42", 0, 34, 2, 657, wmannTalk);
                break;

            case 657:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_43", 0, 34, 2, 658, wmannTalk);
                break;

            case 658:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_44", 0, 34, 2, 659, wmannTalk);
                break;

            case 659:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_45", 0, 34, 2, 661, wmannTalk);
                break;

            case 660:
                // Reaktion Wmuz auf 4. Teil 2. Frage
                PersonSagt("Mertens2_10", 0, 34, 2, 651, wmannTalk);
                break;

            case 661:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_46", 0, 34, 2, 600, wmannTalk);
                break;

            case 800:
                // Warten auf Ende abtauchen wmann
                isShowing = false;
                isTauching = true;
                nextActionID = 810;
                break;

            case 810:
                // MC beenden, wenn zuende gelabert...
                if (isTauching) {
                    break;
                }
                mainFrame.Actions[284] = false;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 900:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00087"), 1000, 285, new int[]{285}, 910);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00088"), 285, 286, new int[]{286}, 920);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00089"), 1000, 288, new int[]{267, 288}, 930);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00090"), 288, 1000, null, 950);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00091"), 1000, 287, null, 1000);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00092"), 287, 1000, null, 1000);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00093"), 1000, 285, new int[]{285}, 910);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00094"), 285, 286, new int[]{286}, 920);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00095"), 1000, 288, new int[]{267, 288}, 930);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00096"), 288, 1000, null, 950);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00097"), 1000, 287, null, 1000);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00098"), 287, 1000, null, 1000);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00099"), 1000, 285, new int[]{285}, 910);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00100"), 285, 286, new int[]{286}, 920);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00101"), 1000, 288, new int[]{267, 288}, 930);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00102"), 288, 1000, null, 950);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00103"), 1000, 287, null, 1000);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc2_Mertens2_00104"), 287, 1000, null, 1000);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 901;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 901:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[287] = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.ifont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                break;

            case 910:
                // Reaktion Wmuz auf 1. Teil 1. Frage
                PersonSagt("Mertens2_11", 0, 34, 2, 900, wmannTalk);
                break;

            case 920:
                // Reaktion Wmuz auf 2. Teil 1. Frage
                PersonSagt("Mertens2_12", 0, 34, 2, 900, wmannTalk);
                break;

            case 930:
                // Reaktion Wmuz auf 1. Teil 2. Frage
                PersonSagt("Mertens2_13", 0, 34, 2, 931, wmannTalk);
                break;

            case 931:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_37", 0, 34, 2, 932, wmannTalk);
                break;

            case 932:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_38", 0, 34, 2, 933, wmannTalk);
                break;

            case 933:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_39", 0, 34, 2, 934, wmannTalk);
                break;

            case 934:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_40", 0, 34, 2, 935, wmannTalk);
                break;

            case 935:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_41", 0, 34, 2, 936, wmannTalk);
                break;

            case 936:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_42", 0, 34, 2, 937, wmannTalk);
                break;

            case 937:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_43", 0, 34, 2, 938, wmannTalk);
                break;

            case 938:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_44", 0, 34, 2, 939, wmannTalk);
                break;

            case 939:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_45", 0, 34, 2, 940, wmannTalk);
                break;

            case 940:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt("Mertens2_46", 0, 34, 2, 900, wmannTalk);
                break;

            case 950:
                // Reaktion Wmuz auf 4. Teil 2. Frage
                PersonSagt("Mertens2_14", 0, 34, 2, 933, wmannTalk);
                break;

            case 1000:
                // warten, bis Wmann zuende getaucht ist
                isShowing = false;
                isTauching = true;
                nextActionID = 1010;
                break;

            case 1010:
                // MC beenden, wenn zuende gelabert...
                if (isTauching) {
                    break;
                }
                mainFrame.Actions[287] = false;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            // Action, wenn Karte gegeben
            case 1100:
                // Wmuz spricht
                PersonSagt("Mertens2_15", fWmuz, 34, 2, 1110, wmannTalk);
                break;

            case 1110:
                // Krabat spricht
                KrabatSagt("Mertens2_16", 0, 1, 2, 1120);
                break;

            case 1120:
                // Wmuz spricht
                PersonSagt("Mertens2_17", 0, 34, 2, 1130, wmannTalk);
                break;

            case 1130:
                // Krabat spricht
                KrabatSagt("Mertens2_18", 0, 1, 2, 1140);
                break;

            case 1140:
                // Wmuz spricht
                PersonSagt("Mertens2_19", 0, 34, 2, 1150, wmannTalk);
                break;

            case 1150:
                // Krabat spricht
                KrabatSagt("Mertens2_20", 0, 1, 2, 1160);
                break;

            case 1160:
                // Wmuz spricht
                PersonSagt("Mertens2_21", 0, 34, 2, 1165, wmannTalk);
                break;

            case 1165:
                // Wmuz spricht
                PersonSagt("Mertens2_22", 0, 34, 2, 1170, wmannTalk);
                break;

            case 1170:
                // Wmuz spricht
                PersonSagt("Mertens2_23", 0, 34, 2, 1180, wmannTalk);
                break;

            case 1180:
                // Krabat spricht
                KrabatSagt("Mertens2_24", 0, 1, 2, 1190);
                break;

            case 1190:
                // Wmuz spricht
                PersonSagt("Mertens2_25", 0, 34, 2, 1193, wmannTalk);
                break;

            case 1193:
                // Wmuz spricht
                PersonSagt("Mertens2_26", 0, 34, 2, 1195, wmannTalk);
                break;

            case 1195:
                // Wmuz spricht
                PersonSagt("Mertens2_27", 0, 34, 2, 1200, wmannTalk);
                break;

            case 1200:
                // Krabat spricht
                KrabatSagt("Mertens2_28", 0, 1, 2, 1205);
                // Hier beginnt Wassermann abzutauchen
                isShowing = false;
                isTauching = true;
                break;

            case 1205:
                // Krabat spricht
                if (isTauching) {
                    break;
                }
                KrabatSagt("Mertens2_29", 0, 1, 2, 1210);
                break;

            case 1210:
                // Krabat verlaesst das Bild
                // Hier wird der Bote in Wjes aktiviert,,,und Pfarrer ausgeschaltet
                mainFrame.Actions[302] = true;
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(Pverlassen);
                nextActionID = 1220;
                // hier werden die talkPoints fuer die Enten zugewiesen
                GenericRectangle k1Temp = kacka1.kackaRect();
                kacka1TalkPoint = new GenericPoint(k1Temp.getX() + k1Temp.getWidth() / 2, k1Temp.getY() - 50);
                break;

            case 1220:
                // Animszene mit Enten
                kacka1IsMoving = false;
                mainFrame.wave.PlayFile("sfx/mlynk-kacka.wav");
                PersonSagt("Mertens2_30", 0, 71, 2, 1225, kacka1TalkPoint);
                break;

            case 1225:
                // erste Ente spricht noch
                PersonSagt("Mertens2_31", 0, 71, 2, 1230, kacka1TalkPoint);
                break;

            case 1230:
                // andere Ente erscheinen lassen
                kacka2.initPos(kacka1.Posit);
                kacka2Visible = true;
                kacka1IsLeft = true;
                nextActionID = 1240;
                break;

            case 1240:
                // warten, bis 2. Ente hergeflogen
                if (kacka2FliegtNoch) {
                    break;
                }
                GenericRectangle k2Temp = kacka2.kackaRect();
                kacka2TalkPoint = new GenericPoint(k2Temp.getX() + k2Temp.getWidth() / 2, k2Temp.getY() - 50);
                // System.out.println ("Talkpoint Ente 2: " + kacka2TalkPoint.x + " " + kacka2TalkPoint.y);
                nextActionID = 1250;
                Counter2 = 20;
                break;

            case 1250:
                // erste Ente spricht
                if (--Counter2 > 1) {
                    break;
                }
                PersonSagt("Mertens2_32", 0, 71, 2, 1260, kacka1TalkPoint);
                break;

            case 1260:
                // zweite Ente spricht
                PersonSagt("Mertens2_33", 0, 72, 2, 1270, kacka2TalkPoint);
                break;

            case 1270:
                // erste Ente
                PersonSagt("Mertens2_34", 0, 71, 2, 1280, kacka1TalkPoint);
                break;

            case 1280:
                // zweite Ente
                PersonSagt("Mertens2_35", 0, 72, 2, 1290, kacka2TalkPoint);
                break;

            case 1290:
                // erste Ente
                PersonSagt("Mertens2_36", 0, 71, 2, 1900, kacka1TalkPoint);
                break;


            case 1900:
                // Ende dieser Anim
                NeuesBild(76, 79);
                break;

            case 2000:
                // Wassermann zeigen (Auftauchen lassen)
                isTauching = true;
                nextActionID = 2010;
                break;

            case 2010:
                // warten auf Auftauchen
                if (!isTauching) {
                    nextActionID = 2020;
                    isShowing = true;
                }
                break;

            case 2020:
                // Krabat stellt sich richtig hin
                mainFrame.wegGeher.SetzeNeuenWeg(Pwmann);
                nextActionID = 50;
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

}