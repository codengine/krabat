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
import de.codengine.krabat.anims.WodnyMuz;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Mertens1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Mertens1.class);
    private GenericImage background;

    private WodnyMuz wmann;
    private final Multiple2 Dialog;
    private boolean isShowing = false;
    private boolean isTauching = false;

    private int Counter;
    private boolean setAnim = false;

    private final GenericPoint wmannTalk;
    private final Borderrect wmannRect;

    // Punkte in Location
    private static final GenericPoint Pleft = new GenericPoint(0, 305);
    private static final GenericPoint wmannFeet = new GenericPoint(365, 373);
    private static final GenericPoint wmannfrauTalk = new GenericPoint(240, 60);
    private static final GenericPoint Pwmann = new GenericPoint(59, 305);

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

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mertens1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(26, true);

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 3.05f;
        mainFrame.krabat.defScale = 0;

        Counter = (int) Math.round(Math.random() * 60);
        Counter += 40;

        InitLocation(oldLocation); // schon hier oben, da wmannposit festgelegt werden muss

        wmann = new WodnyMuz(mainFrame, mainFrame.Actions[207]);
        Dialog = new Multiple2(mainFrame);

        wmannTalk = new GenericPoint();
        wmannTalk.x = wmannFeet.x;
        wmannTalk.y = wmannFeet.y - WodnyMuz.Tauchhoehe - 50;

        int wmannxk = wmannFeet.x - WodnyMuz.Breite / 2;
        // die verschiedenen Hoehenangaben sind richtig, da "Fusspunkt" nicht Ende des Images !
        wmannRect = new Borderrect(wmannxk, wmannFeet.y - WodnyMuz.Tauchhoehe, wmannxk + WodnyMuz.Breite, wmannFeet.y - WodnyMuz.Tauchhoehe + WodnyMuz.Hoehe);

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
                // keine Veraenderung von "istOben"
                break;
            case 21:
                // von Kulow aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(38, 305));
                mainFrame.krabat.SetFacing(3);
                mainFrame.Actions[207] = false;
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
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {
        // bei Multiple Choice und keinem Grund zum Neuzeichnen hier abkuerzen, geht nicht wergen Anim !!!
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
            mainFrame.isAnim = true;
            g.setClip(0, 0, 644, 484);
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

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Counter runterzaehlen fuer Wassermannerscheinen
        if (Counter > 0 && !mainFrame.Actions[206]) {
            Counter--;
            if (Counter < 1) {
                setAnim = true;
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
            nextActionID = 2000;
        }

        // System.out.println (isTauching + " " + isShowing);

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

                // Ausreden fuer Wassermann
                        /*if ((wmannRect.IsPointInRect (pTemp) == true) && (isShowing == true))
                          {
                          // Standard - Sinnloszeug
                          nextActionID = 150;
                          pTemp = Pwmann;
                          }	*/

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

                // Wmann ansehen
                        /*if ((wmannRect.IsPointInRect (pTemp) == true) && (isShowing == true))
                          {
                          nextActionID = 1;
                          pTemp = Pwmann;
                          }*/

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

                // Mit dem Wmann reden
                        /*if ((wmannRect.IsPointInRect (pTemp) == true) && (isShowing == true))
                          {
                          nextActionID = 50;
                          mainFrame.wegGeher.SetzeNeuenWeg (Pwmann);
                          mainFrame.repaint();
                          return;
                          }*/

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
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00000"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00001"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00002"),
                        fWoda, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Wmann benutzen)
                mainFrame.krabat.SetFacing(fWmuz);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);

                // Hier Unterscheidung, welcher Dialog gemeint..
                mainFrame.Actions[276] = true; // Signal, dass im 2.Teil Wassermann bekannt
                if (!mainFrame.Actions[160]) {
                    nextActionID = 600;
                } else {
                    nextActionID = 900;
                }
                break;

            case 55:
                // Wasser mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00003"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00004"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00005"),
                        fWoda, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Kulow
                NeuesBild(21, 20);
                break;

            case 160:
                // Wasser - Ausreden
                DingAusrede(fWoda);
                break;

            case 170:
                // wuda auf Wasser ausreden
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00006"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00007"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00008"),
                        fWoda, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine
                Dialog.InitMC(20);
                if (mainFrame.sprache == 1) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00009"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00010"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00011"), 1000, 163, new int[]{163}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00012"), 163, 164, new int[]{164}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00013"), 164, 165, new int[]{165}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00014"), 165, 166, new int[]{166, 184}, 660);
                    if (!mainFrame.Actions[168]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00015"), 166, 167, new int[]{167}, 670);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00016"), 166, 167, new int[]{167}, 670);
                    }
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00017"), 167, 169, new int[]{169}, 680);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00018"), 1000, 170, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00019"), 170, 1000, null, 800);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00020"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00021"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00022"), 1000, 163, new int[]{163}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00023"), 163, 164, new int[]{164}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00024"), 164, 165, new int[]{165}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00025"), 165, 166, new int[]{166, 184}, 660);
                    if (!mainFrame.Actions[168]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00026"), 166, 167, new int[]{167}, 670);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00027"), 166, 167, new int[]{167}, 670);
                    }
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00028"), 167, 169, new int[]{169}, 680);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00029"), 1000, 170, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00030"), 170, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00031"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00032"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00033"), 1000, 163, new int[]{163}, 630);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00034"), 163, 164, new int[]{164}, 640);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00035"), 164, 165, new int[]{165}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00036"), 165, 166, new int[]{166, 184}, 660);
                    if (!mainFrame.Actions[168]) {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00037"), 166, 167, new int[]{167}, 670);
                    } else {
                        Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00038"), 166, 167, new int[]{167}, 670);
                    }
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00039"), 167, 169, new int[]{169}, 680);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00040"), 1000, 170, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00041"), 170, 1000, null, 800);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 601;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[170] = true;
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
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00042"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00043"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00044"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 620:
                // Reaktion Wmuz auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00045"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00046"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00047"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 630:
                // Reaktion Wmuz auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00048"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00049"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00050"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 640:
                // Reaktion Wmuz auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00051"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00052"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00053"),
                        0, 34, 2, 641, wmannTalk);
                break;

            case 641:
                // Reaktion Wmuz auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00054"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00055"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00056"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 650:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00057"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00058"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00059"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 660:
                // Reaktion Wmuz auf 4. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00060"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00061"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00062"),
                        0, 34, 2, 661, wmannTalk);
                break;

            case 661:
                // Reaktion Wmuz auf 4. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00063"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00064"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00065"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 670:
                // Reaktion Wmuz auf 5. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00066"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00067"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00068"),
                        0, 34, 2, 671, wmannTalk);
                break;

            case 671:
                // Reaktion Wmuz auf 5. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00069"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00070"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00071"),
                        0, 34, 2, 672, wmannTalk);
                break;

            case 672:
                // Reaktion Wmuz auf 5. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00072"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00073"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00074"),
                        0, 34, 2, 600, wmannTalk);
                break;

            case 680:
                // Reaktion Wmuz auf 6. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00075"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00076"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00077"),
                        0, 34, 2, 681, wmannTalk);
                break;

            case 681:
                // Reaktion Wmuz auf 6. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00078"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00079"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00080"),
                        0, 34, 2, 682, wmannTalk);
                break;

            case 682:
                // Reaktion WmuzFrau auf 6. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00081"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00082"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00083"),
                        0, 53, 2, 683, wmannfrauTalk);
                break;

            case 683:
                // Reaktion Wmuz auf 6. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00084"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00085"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00086"),
                        0, 34, 2, 684, wmannTalk);
                break;

            case 684:
                // Reaktion Wmuz auf 6. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00087"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00088"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00089"),
                        0, 34, 2, 690, wmannTalk);
                break;

            case 690:
                // Wassermann taucht wieder ab
                isShowing = false;
                isTauching = true;
                nextActionID = 695;
                break;

            case 695:
                // warten, bis habe fertig
                if (!isTauching) {
                    nextActionID = 700;
                }
                break;

            case 700:
                // alles wieder abschalten
                mainFrame.Actions[206] = true; // hier anzeigen, dass Wassermann kein 2. Mal erscheint
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
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
                mainFrame.Actions[170] = false; // es kommt wieder huch ...
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
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00090"), 1000, 161, new int[]{161}, 910);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00091"), 161, 1000, null, 920);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00092"), 1000, 200, new int[]{200}, 930);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00093"), 200, 201, new int[]{201}, 940);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00094"), 201, 202, new int[]{202}, 950);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00095"), 202, 203, new int[]{203}, 680);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00096"), 1000, 204, null, 1000);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00097"), 204, 1000, null, 1000);
                }
                if (mainFrame.sprache == 2) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00098"), 1000, 161, new int[]{161}, 610);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00099"), 161, 1000, null, 620);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00100"), 1000, 200, new int[]{200}, 930);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00101"), 200, 201, new int[]{201}, 940);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00102"), 164, 165, new int[]{165}, 650);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00103"), 167, 169, new int[]{169}, 680);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00104"), 1000, 170, null, 800);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00105"), 170, 1000, null, 800);
                }
                if (mainFrame.sprache == 3) {
                    // 1. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00106"), 1000, 161, new int[]{161}, 910);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00107"), 161, 1000, null, 920);

                    // 2. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00108"), 1000, 200, new int[]{200}, 930);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00109"), 200, 201, new int[]{201}, 940);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00110"), 201, 202, new int[]{202}, 950);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00111"), 202, 203, new int[]{203}, 680);

                    // 3. Frage
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00112"), 1000, 204, null, 1000);
                    Dialog.ExtendMC(Start.stringManager.getTranslation("Loc1_Mertens1_00113"), 204, 1000, null, 1000);
                }
                mainFrame.isMultiple = true;
                mainFrame.fPlayAnim = false;
                nextActionID = 901;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 901:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.Actions[204] = true;
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
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00114"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00115"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00116"),
                        0, 34, 2, 900, wmannTalk);
                break;

            case 920:
                // Reaktion Wmuz auf 2. Teil 1. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00117"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00118"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00119"),
                        0, 34, 2, 900, wmannTalk);
                break;

            case 930:
                // Reaktion Wmuz auf 1. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00120"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00121"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00122"),
                        0, 34, 2, 900, wmannTalk);
                break;

            case 940:
                // Reaktion Wmuz auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00123"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00124"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00125"),
                        0, 34, 2, 941, wmannTalk);
                break;

            case 941:
                // Reaktion Wmuz auf 2. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00126"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00127"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00128"),
                        0, 34, 2, 900, wmannTalk);
                break;

            case 950:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00129"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00130"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00131"),
                        0, 34, 2, 951, wmannTalk);
                break;

            case 951:
                // Reaktion Wmuz auf 3. Teil 2. Frage
                PersonSagt(Start.stringManager.getTranslation("Loc1_Mertens1_00132"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00133"),
                        Start.stringManager.getTranslation("Loc1_Mertens1_00134"),
                        0, 34, 2, 900, wmannTalk);
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
                mainFrame.Actions[204] = false;
                mainFrame.fPlayAnim = false;
                nextActionID = 0;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
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