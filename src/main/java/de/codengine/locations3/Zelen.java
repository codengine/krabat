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
import de.codengine.anims.Awgust;
import de.codengine.anims.Druzina;
import de.codengine.main.*;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.sound.BackgroundMusicPlayer;

public class Zelen extends Mainloc {
    private GenericImage background, iprikaz, vorder;
    private final GenericImage[] kerze;
    private GenericImage siegel;
    private final GenericImage[] krabat_siegeln;

    // private int whatInventory = 0;

    private final Awgust awgust;
    private boolean hatHandErhoben = false;

    private final Druzina druzina;

    private boolean awgustVisible = false;  // Awgust erscheint nur zusammen mit Druzina !!!

    private boolean walkReadyAwgust = true;
    private boolean walkReadyDruzina = true;

    private int VerhinderwalkAwgust;
    private int VerhinderwalkDruzina;

    private static final int MAX_VERHINDERWALKAWGUST = 2;
    private static final int MAX_VERHINDERWALKDRUZINA = 2;

    private int Flacker = 0;
    private int Tropf = 0;

    private int Verhinderflacker;
    private int Verhindertropf;

    private static final int MAX_VERHINDERFLACKER = 2;
    private static final int MAX_VERHINDERTROPF = 2;

    // Konstanten - Rects
    private static final Borderrect obererAusgang
            = new Borderrect(177, 234, 237, 410);
    /*
    private static final borderrect klavier
        = new borderrect (0, 375, 100, 455);
    private static final borderrect stuhl
        = new borderrect (111, 352, 167, 453);
    */
    private static final Borderrect prikaz
            = new Borderrect(412, 404, 435, 412);
    private static final Borderrect kerzeRect
            = new Borderrect(378, 382, 393, 402);

    // Konstante Points
    private static final GenericPoint pExitUp = new GenericPoint(215, 424);
    private static final GenericPoint pTisch = new GenericPoint(392, 500);
    //   private static final GenericPoint pStuhl     = new GenericPoint (196, 465);
    // private static final GenericPoint pKlavier   = new GenericPoint (100, 465);
    private static final GenericPoint pKerze = new GenericPoint(392, 500);
    private static final GenericPoint pVorTisch = new GenericPoint(305, 472);

    // private static final GenericPoint erzaehlerPoint = new GenericPoint (320, 200);

    private static final GenericPoint awgustStart = new GenericPoint(145, 374);
    private static final GenericPoint awgustStop = new GenericPoint(211, 443);
    private static final GenericPoint druzinaStart = new GenericPoint(148, 347);
    private static final GenericPoint druzinaStop = new GenericPoint(218, 406);

    // Konstante ints
    private static final int fPrikaz = 3;
    private static final int fKlavier = 9;
    private static final int fStuhl = 9;
    private static final int fAwgust = 9;
    private static final int fKerze = 12;

    private int SonderAnim = 0;

    private int Siegeln = 0;

    private int Siegelzeit = 40;

    private int Verhindersiegeln;

    private static final int MAX_VERHINDERSIEGELN = 7;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zelen(Start caller, int oldLocation) {
        super(caller, 145);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 0;
        mainFrame.krabat.zoomf = 2f;
        mainFrame.krabat.defScale = -60;

        awgust = new Awgust(mainFrame);
        druzina = new Druzina(mainFrame);

        awgust.maxx = 420;
        awgust.zoomf = 3f;
        awgust.defScale = 0;

        druzina.maxx = 420;
        druzina.zoomf = 3f;
        awgust.defScale = 0;

        awgust.SetAwgustPos(awgustStart);
        druzina.SetDruzinaPos(druzinaStart);

        kerze = new GenericImage[8];
        krabat_siegeln = new GenericImage[2];

        InitLocation(oldLocation);

        Verhinderflacker = MAX_VERHINDERFLACKER;
        Verhindertropf = MAX_VERHINDERTROPF;
        Verhindersiegeln = MAX_VERHINDERSIEGELN;

        VerhinderwalkAwgust = MAX_VERHINDERWALKAWGUST;
        VerhinderwalkDruzina = MAX_VERHINDERWALKDRUZINA - 1;  // asynchron Laufen !!

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(215, 305, 215, 305, 424, 464));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(135, 305, 135, 305, 465, 479));

        mainFrame.wegSucher.ClearMatrix(2);

        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(13, true);
                break;
            case 144: // von Couch aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(215, 430));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/zelen/zelen.gif");
        iprikaz = getPicture("gfx-dd/zelen/zprikaz.gif");
        vorder = getPicture("gfx-dd/zelen/zdurje.gif");

        kerze[0] = getPicture("gfx-dd/zelen/kerze1.gif");
        kerze[1] = getPicture("gfx-dd/zelen/kerze2.gif");
        kerze[2] = getPicture("gfx-dd/zelen/kerze3.gif");
        kerze[3] = getPicture("gfx-dd/zelen/kerze4.gif");
        kerze[4] = getPicture("gfx-dd/zelen/kerze5.gif");
        kerze[5] = getPicture("gfx-dd/zelen/kerze6.gif");
        kerze[6] = getPicture("gfx-dd/zelen/kerze7.gif");
        kerze[7] = getPicture("gfx-dd/zelen/kerze8.gif");

        siegel = getPicture("gfx-dd/zelen/stempel.gif");

        krabat_siegeln[0] = getPicture("gfx-dd/zelen/s-o-siegeln1.gif");
        krabat_siegeln[1] = getPicture("gfx-dd/zelen/s-o-siegeln2.gif");

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
        g.drawImage(background, 0, 0, null);

        // Awgust zeichnen, wenn da...
        if (awgustVisible) {
            // Hier wird ersteinmal der Hintergrund beider Figuren geloescht

            // Clipping - Rectangle feststellen und setzen fuer Druzina
            Borderrect temp = druzina.DruzinaRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0, null);

            // Hier dasselbe fuer den August
            temp = awgust.AwgustRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0, null);

            // beide Figuren bewegen, wenn dies noetig ist
            if ((--VerhinderwalkAwgust) < 1) {
                VerhinderwalkAwgust = MAX_VERHINDERWALKAWGUST;
                walkReadyAwgust = awgust.Move();
            }

            if ((--VerhinderwalkDruzina) < 1) {
                VerhinderwalkDruzina = MAX_VERHINDERWALKDRUZINA;
                walkReadyDruzina = druzina.Move();
            }

            // nun beide zeichnen, Awgust als zweiten (ist immer davor)
            // Clipping - Rectangle feststellen und setzen fuer Druzina
            temp = druzina.DruzinaRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);
            druzina.drawDruzina(g);
            g.drawImage(vorder, 82, 210, null);

            // fuer Awgust ein paar Unterscheidungen
            temp = awgust.AwgustRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);
            if ((TalkPerson == 40) && (mainFrame.talkCount > 1)) {
                awgust.talkAwgust(g, hatHandErhoben);
            } else {
                awgust.drawAwgust(g);
            }
            g.drawImage(vorder, 82, 210, null);
        }

        // prikaz zeichnen, solange noch da
        if (!mainFrame.Actions[640]) {
            g.setClip(412, 404, 24, 10);
            g.drawImage(background, 0, 0, null);
            g.drawImage(iprikaz, 412, 404, null);
        }

        // Kerze zeichnen
        if ((--Verhinderflacker) < 1) {
            Verhinderflacker = MAX_VERHINDERFLACKER;
            Flacker = (int) (Math.random() * 4.9);
        }
        if ((--Verhindertropf) < 1) {
            Verhindertropf = MAX_VERHINDERTROPF;
            Tropf++;
            if (Tropf == 3) {
                Tropf = 0;
            }
        }
        g.setClip(378, 382, 15, 20);
        g.drawImage(background, 0, 0, null);
        g.drawImage(kerze[Flacker], 378, 382, null);
        g.drawImage(kerze[Tropf + 5], 378, 382, null);

        // Stempel zeichnen
        g.setClip(370, 397, 9, 10);
        g.drawImage(siegel, 370, 397, null);

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        mainFrame.wegGeher.GeheWeg();

        if (SonderAnim != 0) {
            // hier erstmal alles berechnen, dann je nachdem die Bilder switchen
            GenericPoint hier = new GenericPoint(mainFrame.krabat.GetKrabatPos().x, mainFrame.krabat.GetKrabatPos().y);

            // Groesse
            int scale = mainFrame.krabat.defScale;
            scale += (int) (((float) mainFrame.krabat.maxx - (float) hier.y) / mainFrame.krabat.zoomf);
            if (scale < mainFrame.krabat.defScale) {
                scale = mainFrame.krabat.defScale;
            }

            // System.out.println ("Scale ist " + scale + " gross.");

            // Hoehe: nur offset
            int hoch = 100 - scale;

            // Breite abhaengig von Hoehe...
            int weit = 50 - (scale / 2);

            hier.x -= weit / 2;
            hier.y -= hoch;

            // Cliprect setzen
            g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

            // Hier ab und zu umschalten...
            if ((--Verhindersiegeln) < 1) {
                Verhindersiegeln = MAX_VERHINDERSIEGELN;
                if (Siegeln == 1) {
                    Siegeln = 0;
                } else {
                    Siegeln = 1;
                }
            }

            // selbstbeendende Anim
            if ((--Siegelzeit) < 0) {
                SonderAnim = 0;
            }

            // zeichnen und gut
            g.drawImage(krabat_siegeln[Siegeln], hier.x, hier.y, weit, hoch, null);
        } else {
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
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
        /*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
          {
          g.drawImage (weiden2, 84, 221, null);
          }*/

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
            if (e.isRightClick()) {
                nextActionID = 0;

                Borderrect tmp = mainFrame.krabat.KrabatRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Ausreden Stuhl
//                         if (stuhl.IsPointInRect (pTemp) == true)
//                             { 
//                                 nextActionID = 165;
//                                 pTemp = pStuhl;
//                             }

                // Ausreden Prikaz
                if ((prikaz.IsPointInRect(pTemp)) && (!mainFrame.Actions[640])) {
                    nextActionID = 155;
                    pTemp = pTisch;
                }

                // Ausreden Kerze
                if (kerzeRect.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 31:
                        case 33:// ununterschriebene und unterschriebene ungesiegelte Erlaubnis
                            nextActionID = 80;
                            break;
                        case 32:
                        case 34:// bereits gesiegelte Erlaubnisse
                            nextActionID = 95;
                            break;
                        default:
                            nextActionID = 170;
                    }
                    pTemp = pKerze;
                }

                // Ausreden Klavier
//                         if (klavier.IsPointInRect (pTemp) == true)
//                             { 
//                                 nextActionID = 160;
//                                 pTemp = pKlavier;
//                             }

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
            if (e.isRightClick()) {
                // linke Maustaste
                nextActionID = 0;

                // zu Couch gehen ?
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!obererAusgang.IsPointInRect(kt)) {
                        pTemp = pExitUp;
                    } else {
                        pTemp = new GenericPoint(pExitUp.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Prikaz ansehen
                if ((prikaz.IsPointInRect(pTemp)) && (!mainFrame.Actions[640])) {
                    nextActionID = 1;
                    pTemp = pTisch;
                }

                // Kerze ansehen
                if (kerzeRect.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pKerze;
                }

                // Stuhl ansehen
//                         if (stuhl.IsPointInRect (pTemp) == true)
//                             {
//                                 nextActionID = 3;
//                                 pTemp = pStuhl;
//                             }	

                // Klavier ansehen
//                         if (klavier.IsPointInRect (pTemp) == true)
//                             {
//                                 nextActionID = 2;
//                                 pTemp = pKlavier;
//                             }	

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Prikaz mitnehmen
                if ((prikaz.IsPointInRect(pTemp)) && (!mainFrame.Actions[640])) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pTisch);
                    mainFrame.repaint();
                    return;
                }

                // Kerze benutzen
                if (kerzeRect.IsPointInRect(pTemp)) {
                    nextActionID = 90;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKerze);
                    mainFrame.repaint();
                    return;
                }

                // Stuhl mitnehmen
//                         if (stuhl.IsPointInRect (pTemp) == true)
//                             {
//                                 nextActionID = 60;
//                                 mainFrame.wegGeher.SetzeNeuenWeg (pStuhl);
// 	   			mainFrame.repaint();
//                                 return;
//                             }	

                // Klavier mitnehmen
//                         if (klavier.IsPointInRect (pTemp) == true)
//                             {
//                                 nextActionID = 55;
//                                 mainFrame.wegGeher.SetzeNeuenWeg (pKlavier);
// 	   			mainFrame.repaint();
//                                 return;
//                             }	

                // Wenn Ausgang -> kein Inventar anzeigen
                if (obererAusgang.IsPointInRect(pTemp)) {
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
// 		    (stuhl.IsPointInRect (pTemp) == true) ||
//                     (klavier.IsPointInRect (pTemp) == true) ||
                    ((prikaz.IsPointInRect(pTemp)) && (!mainFrame.Actions[640])) ||
                    (kerzeRect.IsPointInRect(pTemp));

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
            if ((kerzeRect.IsPointInRect(pTemp)) ||
// 		    (klavier.IsPointInRect (pTemp) == true) ||
//                     (stuhl.IsPointInRect (pTemp) == true) ||
                    ((prikaz.IsPointInRect(pTemp)) && (!mainFrame.Actions[640]))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (obererAusgang.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
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

        // hier wird zu den Standardausreden von Krabat verzweigt, 
        // wenn noetig (in Superklasse)
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
                // Prikaz ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00000"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00001"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00002"),
                        fPrikaz, 3, 0, 0);
                break;

            case 2:
                // Klavier ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00003"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00004"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00005"),
                        fKlavier, 3, 0, 0);
                break;

            case 3:
                // Stuhl ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00006"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00007"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00008"),
                        fStuhl, 3, 0, 0);
                break;

            case 4:
                // Kerze ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00009"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00010"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00011"),
                        fPrikaz, 3, 0, 0);
                break;

            case 50:
                // Prikaz mitnehmen, zuerstmal hinlaufen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pKerze);
                nextActionID = 52;
                break;

            case 52:
                // so, nun darf genommen werden
                mainFrame.krabat.SetFacing(fPrikaz);
                mainFrame.krabat.nAnimation = 31;
                // whatInventory = 49;
                Counter = 5;
                nextActionID = 53;
                break;

            case 53:
                // auf Ende nehmen warten
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(49);
                    mainFrame.Actions[640] = true;
                    mainFrame.Clipset = false;
                }
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                nextActionID = 65;
                break;

            case 55:
                // Klavier mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00012"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00013"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00014"),
                        fKlavier, 3, 0, 0);
                break;

            case 60:
                // Stuhl mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00015"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00016"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00017"),
                        fStuhl, 3, 0, 0);
                break;

            case 65:
                // Ende nehmen
                if ((mainFrame.Actions[640]) && (mainFrame.Actions[641])) {
                    nextActionID = 220;
                } else {
                    nextActionID = 70;
                }
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 70:
                // einfaches Anim-Ende, K muss zuruecklaufen 
                if (SonderAnim != 0) {
                    break;
                }
                mainFrame.wegGeher.SetzeNeuenWeg(pVorTisch);
                nextActionID = 75;
                break;

            case 75:
                // Ende dieser Anim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 80:
                // nur Siegeln
                if (!mainFrame.Actions[641]) {
                    mainFrame.fPlayAnim = true;
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                    mainFrame.invCursor = false;

                    // Dowol noch nicht gesiegelt, also testen, ob da, dann kann gesiegelt werden
                    if (!mainFrame.Actions[645]) {
                        // Dowol noch nicht an PredMali gegeben, also ununterschriebene austauschen
                        mainFrame.inventory.vInventory.removeElement(31);
                        mainFrame.inventory.vInventory.addElement(32);
                    }

                    if ((mainFrame.Actions[645]) && (mainFrame.Actions[647]) &&
                            (mainFrame.Actions[648])) {
                        // Dowol von PredMali zurueck, also unterschriebene austauschen
                        mainFrame.inventory.vInventory.removeElement(33);
                        mainFrame.inventory.vInventory.addElement(34);
                    }

                    mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pKerze);
                    nextActionID = 83;
                } else { // schon gesiegelt, nicht 2x
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00018"),
                            Start.stringManager.getTranslation("Loc3_Zelen_00019"),
                            Start.stringManager.getTranslation("Loc3_Zelen_00020"),
                            fPrikaz, 3, 0, 0);
                }
                break;

            case 83:
                // steht vor der Kerze, also gut
                mainFrame.Actions[641] = true;
                SonderAnim = 1;
                mainFrame.krabat.SetFacing(fKerze);
                Counter = 20;
                nextActionID = 86;
                break;


		/*           case 85:
                // Ende siegeln
		if (SonderAnim != 0) break;
                PersonSagt (Start.stringManager.getTranslation("Loc3_Zelen_00021"),
                            Start.stringManager.getTranslation("Loc3_Zelen_00022"),
                            Start.stringManager.getTranslation("Loc3_Zelen_00023"),
                            0, 54, 2, 86, erzaehlerPoint);
			    break;*/


            case 86:
                // Krabat spricht
                if ((--Counter) == 1) {
                    mainFrame.wave.PlayFile("sfx/schildlegen.wav");
                }
                if (SonderAnim != 0) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00024"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00025"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00026"),
                        0, 3, 2, 87);
                break;

            case 87:
                // jetzt schauen, obs schon zu Ende ist oder noch nicht...
                if ((mainFrame.Actions[640]) && (mainFrame.Actions[641])) {
                    nextActionID = 220;
                } else {
                    nextActionID = 70;
                }
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 90:
                // Kerze allein benutzen geht nicht
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00027"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00028"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00029"),
                        fPrikaz, 3, 0, 0);
                break;

            case 95:
                // fertige Erlaubnis auf kerze -> geht nicht
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00030"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00031"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00032"),
                        fPrikaz, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Couch
                NeuesBild(144, locationID);
                break;

            case 155:
                // Prikaz - Ausreden
                DingAusrede(fPrikaz);
                break;

            case 160:
                // Klavier - Ausreden
                DingAusrede(fKlavier);
                break;

            case 165:
                // Stuhl - Ausreden
                DingAusrede(fStuhl);
                break;

            case 170:
                // Kerze-Ausreden
                DingAusrede(fKerze);
                break;

            case 220:
                // Awgust und Druzina erscheinen und laufen her
                BackgroundMusicPlayer.getInstance().playTrack(23, false);
                awgustVisible = true;
                awgust.MoveTo(awgustStop);
                druzina.MoveTo(druzinaStop);
                walkReadyAwgust = false;
                walkReadyDruzina = false;
                nextActionID = 225;
                break;

            case 225:
                // warten, bis beide angekommen sind
                if ((!walkReadyAwgust) || (!walkReadyDruzina)) {
                    break;
                }
                nextActionID = 230;
                break;

            case 230:
                // Reaktion August
                awgustVisible = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zelen_00033"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00034"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00035"),
                        fAwgust, 40, 2, 240, awgust.evalAwgustTalkPoint());
                break;

            case 240:
                // Krabat spricht
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zelen_00036"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00037"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00038"),
                        0, 1, 2, 250);
                break;

            case 250:
                // Reaktion August
                hatHandErhoben = true;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Zelen_00039"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00040"),
                        Start.stringManager.getTranslation("Loc3_Zelen_00041"),
                        0, 40, 2, 260, awgust.evalAwgustTalkPoint());
                break;

            case 260:
                // Gehe zu Kuche
                mainFrame.Actions[655] = true; // Hier Action - Variable fuer Muellererscheinen !!!!
                NeuesBild(132, locationID);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}