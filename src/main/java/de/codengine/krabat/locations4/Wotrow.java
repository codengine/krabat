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

package de.codengine.krabat.locations4;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Wotrow extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Wotrow.class);
    private GenericImage background;
    private GenericImage foreground;
    private GenericImage mond;

    // Strings - Texte
    /*private static final String H1Text = Start.stringManager.getTranslation("Loc4_Wotrow_00000");
      private static final String D1Text = Start.stringManager.getTranslation("Loc4_Wotrow_00001");
      private static final String N1Text = Start.stringManager.getTranslation("Loc4_Wotrow_00002");
    */

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(325, 119, 387, 149);
    private static final Borderrect untererAusgang = new Borderrect(309, 445, 423, 479);
    private static final Borderrect brWoda = new Borderrect(420, 270, 525, 280);
    private static final Borderrect brWokno = new Borderrect(331, 398, 360, 411);

    // Konstante Points
    private static final GenericPoint Pdown = new GenericPoint(357, 479);
    private static final GenericPoint Pup = new GenericPoint(332, 175);
    private static final GenericPoint Pwoda = new GenericPoint(406, 278);
    private static final GenericPoint Pwokno = new GenericPoint(388, 443);

    // wir beginnen bei Y - Koordinate 148
    /*    private static final int[] Carray = {360, 358, 356, 355, 353, 352, 351, 349, 347, 346, 
	  345, 344, 343, 342, 341, 340, 339, 339, 338, 337,
	  336, 335, 335, 334, 333, 333, 332, 332, 331, 331,
	  330, 330, 330, 330, 330, 330, 330, 330, 330, 330,
	  330, 330, 330, 330, 330, 330, 331, 331, 332, 332,
	  
	  333, 333, 334, 335, 337, 338, 340, 341, 343, 344,
	  346, 348, 349, 351, 352, 355, 356, 357, 360, 361,
	  363, 364, 366, 367, 368, 368, 370, 370, 370, 371,
	  371, 371, 372, 372, 372, 373, 374, 374, 375, 375,
	  375, 376, 377, 377, 378, 378, 378, 378, 379, 379,
	  
	  380, 380, 380, 381, 381, 382, 382, 382, 383, 384,
	  385, 385, 386, 386, 388, 389, 390, 392, 394, 396,
	  398, 399, 400, 401, 402, 404, 404, 405, 406, 406,
	  407, 407, 407, 408, 409, 411, 412, 414, 415, 415,
	  416, 417, 419, 420, 421, 423, 424, 425, 426, 426,
	  
	  427, 428, 430, 431, 432, 433, 434, 435, 437, 438,
	  439, 440, 441, 442, 442, 442, 442, 442, 442, 442,
	  442, 442, 442, 441, 440, 440, 439, 438, 437, 436,
	  434, 432, 430, 427, 424, 420, 418, 415, 415, 413,
	  411, 407, 404, 402, 400, 398, 395, 393, 391, 387,
	  
	  386, 384, 382, 381, 380, 378, 377, 376, 375, 375,
	  375, 375, 375, 375, 375, 375, 375, 375, 375, 375,
	  375, 375, 376, 378, 379, 380, 381, 382, 383, 385,
	  387, 389, 390, 392, 396, 400, 404, 408, 412, 416};*/

    // letzte Y - Koordinate = 387

    // Konstante ints
    private static final int fExit = 6;
    private static final int fWoda = 3;
    private static final int fWokno = 9;

    // fuers Blinkern
    private static final Bordertrapez[] Blink =
            {new Bordertrapez(430, 461, 421, 460, 273, 277),
                    new Bordertrapez(469, 491, 470, 491, 274, 277)};

    private int[][][] MerkArray;
    private static final int HAEUFIGKEITSKONSTANTE = 1000;
    private static final int ANIMZUSTAENDE = 9;

    private static final GenericColor blinkFarbe = new GenericColor(222, 214, 148);

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wotrow(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        mainFrame.krabat.maxx = 400;
        mainFrame.krabat.zoomf = 8f;
        mainFrame.krabat.defScale = 60;

        // fuer Blinkern rein
        InitBlinker();

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(441, 463, 340, 368, 403, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(410, 414, 441, 463, 390, 402));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(373, 376, 409, 412, 371, 389));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(373, 356, 374, 370));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(437, 439, 373, 376, 326, 355));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(440, 442, 437, 439, 319, 325));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(440, 312, 441, 318));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(407, 408, 440, 441, 282, 311));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(401, 402, 407, 408, 272, 281));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(390, 391, 401, 402, 266, 271));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(382, 383, 390, 391, 255, 265));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(367, 368, 382, 383, 222, 254));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(332, 333, 367, 368, 199, 221));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(330, 331, 332, 333, 190, 198));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(332, 333, 330, 331, 175, 189));

        mainFrame.wegSucher.ClearMatrix(15);

        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 4);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(5, 6);
        mainFrame.wegSucher.PosVerbinden(6, 7);
        mainFrame.wegSucher.PosVerbinden(7, 8);
        mainFrame.wegSucher.PosVerbinden(8, 9);
        mainFrame.wegSucher.PosVerbinden(9, 10);
        mainFrame.wegSucher.PosVerbinden(10, 11);
        mainFrame.wegSucher.PosVerbinden(11, 12);
        mainFrame.wegSucher.PosVerbinden(12, 13);
        mainFrame.wegSucher.PosVerbinden(13, 14);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 201:
                // von Oben aus 
                mainFrame.krabat.SetKrabatPos(new GenericPoint(332, 185));
                mainFrame.krabat.SetFacing(6);
                break;
            case 203:
                // von Unten aus (Doma4)
                mainFrame.krabat.SetKrabatPos(new GenericPoint(372, 462));
                mainFrame.krabat.SetFacing(12);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/wotrow/wotrow.gif");
        foreground = getPicture("gfx/wotrow/wotrow-fg.gif");

        mond = getPicture("gfx/wotrow/mesack.gif");

    }


    private void InitBlinker() {
        // hier wird das Blinkern festgelegt, indem das Array initialisiert wird, wo der
        // Blinkstatus gespeichert wird

        // So viele Borderrects
        int AnzahlRects = Blink.length;

        // So viele Sachen sind zu merken
        int AnzahlMerksachen = 3;

        // So viele Striche sollen in den borderrects erscheinen
        int AnzahlStriche = 1; // 1 Mindestens !

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

                    // System.out.println ("Array " + i + " " + j + "bekommt einen Blinker...");

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

        // Mond zeichnen
        g.drawImage(mond, 399, 26);

        // Blinkern ermoeglichen
        // g.setClip (392, 246, 248, 107);
        g.setClip(0, 0, 644, 484);
        g.drawImage(background, 0, 0);
        Blink(g);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // hier ist der Sound...
        evalSound();

        mainFrame.wegGeher.GeheWeg();

        //         mainFrame.krabat.SetKrabatPos (CorrectX (mainFrame.krabat.GetKrabatPos()));

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

        // Vordergrund vor Krabat zeichnen
        g.drawImage(foreground, 335, 64);

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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden fuer woda
                if (brWoda.IsPointInRect(pTemp)) {
                    pTemp = Pwoda;
                    nextActionID = 150;
                }

                // Ausreden fuer wokno
                if (brWokno.IsPointInRect(pTemp)) {
                    pTemp = Pwokno;
                    nextActionID = 155;
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
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.isLeftClick()) {
                // linke Maustaste
                nextActionID = 0;

                // nach Unten gehen ?
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

                // nach Oben gehen
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

                // Anschauen woda
                if (brWoda.IsPointInRect(pTemp)) {
                    pTemp = Pwoda;
                    nextActionID = 1;
                }

                // Anschauen wokno
                if (brWokno.IsPointInRect(pTemp)) {
                    pTemp = Pwokno;
                    nextActionID = 5;
                }

                SetzeNeuenWeg(pTemp);
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

                // Woda mitnehmen
                if (brWoda.IsPointInRect(pTemp)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwoda);
                    mainFrame.repaint();
                    return;
                }

                // Wokno mitnehmen
                if (brWokno.IsPointInRect(pTemp)) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pwokno);
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
            Borderrect tmp = mainFrame.krabat.getRect();
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) || brWoda.IsPointInRect(pTemp) ||
                    brWokno.IsPointInRect(pTemp);

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
            if (brWoda.IsPointInRect(pTemp) || brWokno.IsPointInRect(pTemp)) {
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

    // Routinen fuer veraendertes Laufen auf definierter Linie, deaktiviert!!!

    private void SetzeNeuenWeg(GenericPoint dest) {
        // GenericPoint right = CorrectSetX (dest);
        // mainFrame.wegGeher.SetzeNeuenWeg (right);

        // Routine wird nicht mehr genutzt
        mainFrame.wegGeher.SetzeNeuenWeg(dest);
    }		
  
    /*
    
    private GenericPoint CorrectX (GenericPoint dst)
    {
	// nix tun
	return (dst);

	*/

    // X - Koordinate beim Laufen nur auf definiertem Stueck beachten
  	/*if (dst.y < 148) return dst;
	  if (dst.y > 387) return dst;
	  else return (new GenericPoint ((Carray [dst.y - 148]), dst.y));*/
    // }		

    /*
    private GenericPoint CorrectSetX (GenericPoint dst)
    {
	// ebenso nix tun
	return (dst);

	*/

    // Hier wird Y-Koordinate auch in anderen Rects beeinflusst -> "Ausschalten" der BestRect - Routine
  	/*if (dst.y < 148) return (new GenericPoint (CorrectX (new GenericPoint (dst.x, 148))));
	  if (dst.y > 387) return dst;

	  return (new GenericPoint (Carray [dst.y - 148], dst.y));*/
    // }

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

    // Umgebungs-Sounds abspielen
    private void evalSound() {
        int zfz = (int) (Math.random() * 100);

        if (zfz > 92) {
            mainFrame.wave.PlayFile("sfx/grillen.wav");
        }

        if (zfz > 98) {
            int zfz2 = (int) (Math.random() * 1.99f);

            if (zfz2 < 1) {
                mainFrame.wave.PlayFile("sfx/uhu1.wav");
            } else {
                mainFrame.wave.PlayFile("sfx/uhu2.wav");
            }
        }
    }

    private void Blink(GenericDrawingContext g) {
        g.setColor(blinkFarbe);

        // System.out.println ("Blinkern...");

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
                            case 1:
                            case 8:
                            case 9:
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0], MerkArray[i][j][1]);
                                break;

                            case 2: // Zwei Punkte
                            case 3:
                            case 6:
                            case 7:
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 1, MerkArray[i][j][1]);
                                break;

                            case 4: // Drei Punkte
                            case 5:
                                g.drawLine(MerkArray[i][j][0], MerkArray[i][j][1], MerkArray[i][j][0] + 2, MerkArray[i][j][1]);
                                break;

                            default:
                                log.error("Fehler in Blinkerroutine !!! {}", MerkArray[i][j][2]);

                        }

                        // Status weiterzaehlen
                        MerkArray[i][j][2]++;

                        // System.out.println ("Ein Blinker wurde gezeichnet.");

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

            case 1:
                // Look woda
                KrabatSagt(Start.stringManager.getTranslation("Loc4_Wotrow_00003"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00004"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00005"),
                        fWoda, 3, 0, 0);
                break;

            case 5:
                // Look Wokno
                KrabatSagt(Start.stringManager.getTranslation("Loc4_Wotrow_00006"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00007"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00008"),
                        fWokno, 3, 0, 0);
                break;

            case 50:
                // Use woda
                KrabatSagt(Start.stringManager.getTranslation("Loc4_Wotrow_00009"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00010"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00011"),
                        fWoda, 3, 0, 0);
                break;

            case 55:
                // Use wokno
                KrabatSagt(Start.stringManager.getTranslation("Loc4_Wotrow_00012"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00013"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00014"),
                        fWokno, 3, 0, 0);
                break;

            case 150:
                // Ausrede woda
                DingAusrede(fWoda);
                break;

            case 155:
                // Ausrede wokno
                DingAusrede(fWokno);
                break;

            case 100:
                // Nicht zurueckgehen
                KrabatSagt(Start.stringManager.getTranslation("Loc4_Wotrow_00015"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00016"),
                        Start.stringManager.getTranslation("Loc4_Wotrow_00017"),
                        fExit, 3, 0, 0);
                break;

            case 101:
                // Gehe zu Hrod
                NeuesBild(201, 200);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}