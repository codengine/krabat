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

package rapaki.krabat.locations;

import rapaki.krabat.Start;
import rapaki.krabat.anims.KrabatShoot;
import rapaki.krabat.anims.Rapak;
import rapaki.krabat.main.*;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.sound.BackgroundMusicPlayer;

public class Rapak1 extends Mainloc {
    private GenericImage background, blumen1, blumen2, schild;

    private GenericImage[] Feder;
    private boolean animit = false;
    private boolean segeln = false;
    private int ykoord = 186;
    private int xkoord = 437;
    private static final int NORM_XKOORD = 437;

    private int Zaehl;
    private boolean forward = true;
    private double angle = 0;
    private static final double Offset = (2 * Math.PI) / 15;

    private Rapak rabe;
    private KrabatShoot schiesser;
    private int Sonderstatus = 0;

    private int Counter = 0;

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(162, 150, 240, 193);
    private static final Borderrect linkerAusgang = new Borderrect(0, 308, 27, 425);
    private static final Borderrect untererAusgang = new Borderrect(26, 430, 286, 479);
    private static final Borderrect rechterAusgang = new Borderrect(594, 319, 639, 447);
    private static final Borderrect brSchildOben = new Borderrect(393, 204, 466, 228);
    private static final Borderrect brSchild = new Borderrect(399, 230, 467, 309);
    private static final Borderrect blRect1 = new Borderrect(0, 367, 100, 479);
    private static final Borderrect blRect2 = new Borderrect(200, 414, 485, 479);
    private static final Borderrect rapakRect = new Borderrect(396, 147, 465, 179);
    private static final Borderrect pjeroRect = new Borderrect(408, 391, 479, 417);

    // Punkte in Location
    private static final GenericPoint Pschild = new GenericPoint(430, 370);
    private static final GenericPoint Pdown = new GenericPoint(144, 479);
    private static final GenericPoint Pleft = new GenericPoint(15, 369);
    private static final GenericPoint Pup = new GenericPoint(200, 187);
    private static final GenericPoint Pright = new GenericPoint(621, 384);
    private static final GenericPoint Prapak = new GenericPoint(430, 370);
    private static final GenericPoint Pcylic = new GenericPoint(282, 402);
    private static final GenericPoint Ppjero = new GenericPoint(390, 408);
    private static final GenericPoint PpjeroAufh = new GenericPoint(421, 417);

    // Konstante ints
    private static final int fSchild = 12;
    private static final int fRapak = 12;
    private static final int fPjero = 3;
    private static final int fSchildOben = 12;
    private static final int fExitRight = 3;
    private static final int fSchiessen = 12;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Rapak1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 377;
        mainFrame.krabat.zoomf = 1.67f;
        mainFrame.krabat.defScale = -50;

        Feder = new GenericImage[4];
        rabe = new Rapak(mainFrame);
        schiesser = new KrabatShoot(mainFrame);

        schiesser.maxx = mainFrame.krabat.maxx;
        schiesser.zoomf = mainFrame.krabat.zoomf;
        schiesser.defScale = mainFrame.krabat.defScale;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(196, 205, 172, 194, 185, 229));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(172, 230, 194, 261));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(172, 194, 190, 237, 262, 332));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(190, 237, 152, 251, 333, 365));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(0, 369, 85, 416));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(86, 366, 277, 441));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(86, 442, 233, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(278, 376, 459, 441));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(460, 374, 639, 428));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(9);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 5);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(5, 6);
        mainFrame.wegSucher.PosVerbinden(5, 7);
        mainFrame.wegSucher.PosVerbinden(7, 8);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 4:
                // von Haty aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(147, 470));
                mainFrame.krabat.SetFacing(12);
                break;
            case 9:
                // von Horiz aus
                mainFrame.krabat.SetKrabatPos(new GenericPoint(34, 376));
                mainFrame.krabat.SetFacing(3);
                break;
            case 19:
                // von Zdzary aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.SetKrabatPos(new GenericPoint(195, 200));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/rapak/rapak.gif");
        blumen1 = getPicture("gfx/rapak/rap1.gif");
        blumen2 = getPicture("gfx/rapak/rap3.gif");
        schild = getPicture("gfx/rapak/rap2.gif");
        Feder[1] = getPicture("gfx/rapak/pkp1.gif");
        Feder[2] = getPicture("gfx/rapak/pkp2.gif");
        Feder[3] = getPicture("gfx/rapak/pkp3.gif");

        loadPicture();
    }

    public void cleanup() {
        background = null;
        blumen1 = null;
        blumen2 = null;
        schild = null;
        Feder[1] = null;
        Feder[2] = null;
        Feder[3] = null;

        schiesser.cleanup();
        schiesser = null;
        rabe.cleanup();
        rabe = null;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintLocation(GenericDrawingContext g) {
        // Schild wurde aufgehoben!!!!!!!!!
	/*if (mainFrame.krabat.fAnimHelper == true)
	  {
	  // Schild aufheben
	  if (Aufheben == 3)
	  {
	  mainFrame.inventory.vInventory.addElement (new Integer (3));
	  mainFrame.Clipset = false; 
	  mainFrame.krabat.fAnimHelper = false;
	  mainFrame.Actions [901] = true;
	  Aufheben = 0;
	  }
	  else
	  {
	  // Feder aufheben
	  if (Aufheben == 19)
	  {
	  mainFrame.inventory.vInventory.addElement (new Integer (19));
	  mainFrame.Clipset = false;
	  mainFrame.krabat.fAnimHelper = false;
	  mainFrame.Actions[919] = true;
	  Aufheben = 0;
	  }
	  }				  
	  }*/

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

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // bei Notwendigkeit Hintergrund fuer Schiessanim loeschen
	/*if (Sonderstatus != 0)
	  {
	  g.setClip (160, 190, 325, 210);
	  g.drawImage (background, 0, 0, null);
	  }*/

        // Raben Hintergrund loeschen
        GenericRectangle raRect = rabe.rapakRect();
        g.setClip((int) raRect.getX(), (int) raRect.getY(), (int) raRect.getWidth(), (int) raRect.getHeight());
        g.drawImage(background, 0, 0, null);

        // Schild zeichnen, da im Hintergrund !!!
        if (mainFrame.Actions[901] == false) {
            g.setClip(399, 230, 70, 81);
            g.drawImage(schild, 399, 230, null);
        }

        // Raben zeichnen
        raRect = rabe.rapakRect();
        g.setClip((int) raRect.getX(), (int) raRect.getY(), (int) raRect.getWidth(), (int) raRect.getHeight());

        // beim Treffer
        if (animit == true) {
            animit = !(rabe.fliegRapak(g));
        }
        // normale Backgroundanims
        else {
            rabe.drawRapak(g, TalkPerson);
        }

        // zeichne Feder, wie sie runterfliegt
        if (segeln == true) {
            // GenericImage weiterschalten
            if (forward == true) {
                Zaehl++;
                if (Zaehl == 4) {
                    Zaehl = 2;
                    forward = false;
                }
            } else {
                Zaehl--;
                if (Zaehl == 0) {
                    Zaehl = 2;
                    forward = true;
                }
            }

            // Feder zeichnen
            g.setClip(xkoord - 5, ykoord - 4, 21, 16);
            g.drawImage(background, 0, 0, null);
            if (mainFrame.Actions[901] == false) {
                g.drawImage(schild, 399, 230, null);
            }
            g.drawImage(Feder[Zaehl], xkoord, ykoord, null);

            // Y - Koordinate weiterschalten
            ykoord += 3;

            // X - Koordinate weiterschalten
            angle += Offset;
            if (angle > (2 * Math.PI)) {
                angle -= (2 * Math.PI);
            }
            xkoord = (int) Math.round(Math.sin(angle) * 5.1);
            xkoord += NORM_XKOORD;

            // auf Ende pruefen
            if (ykoord >= 394) {
                segeln = false;
            }
        }

        // wenn geschossen, dann Feder zeichnen
        if ((mainFrame.Actions[210] == true) && (mainFrame.Actions[919] == false)) {
            g.setClip(437, 390, 16, 16);
            g.drawImage(background, 0, 0, null);
            g.drawImage(Feder[1], 437, 394, null);
        }

        mainFrame.wegGeher.GeheWeg();

        if (Sonderstatus != 0) {
            // Sonderanims
            g.setClip(160, 160, 325, 320);
            if (schiesser.drawKrabat(g, mainFrame.krabat.GetKrabatPos()) == false) {
                Sonderstatus = 0;
            }
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
        }

        // Krabat hinter Gestruepp
        if (blRect1.IsPointInRect(pKrTemp) == true) {
            g.drawImage(blumen1, 0, 374, null);
        }

        if (blRect2.IsPointInRect(pKrTemp) == true) {
            g.drawImage(blumen2, 241, 417, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            // System.out.println ("TalkPerson ist : " + TalkPerson);
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

                // Ausreden fuer Schild oben
                if (brSchildOben.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 12: // kamuski
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 170;
                            break;
                    }
                    pTemp = Pschild;
                }

                // Ausreden fuer Schild unten, wenn noch da
                if ((brSchild.IsPointInRect(pTemp) == true) && (mainFrame.Actions[901] == false)) {
                    switch (mainFrame.whatItem) {
                        case 2: // kij
                            nextActionID = 210;
                            break;
                        case 12: // kamuski
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = Pschild;
                }

                // Ausreden fuer Feder
                if ((pjeroRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[919] == false) &&
                        (mainFrame.Actions[210] == true)) {
                    // Standard - Sinnloszeug
                    nextActionID = 165;
                    pTemp = Ppjero;
                }

                // Ausreden fuer Raben
                if (rapakRect.IsPointInRect(pTemp) == true) {
                    switch (mainFrame.whatItem) {
                        case 18: // bron
                            if (mainFrame.Actions[210] == false) {
                                nextActionID = 160;
                                pTemp = Pcylic;
                            } else {
                                nextActionID = 220;
                                pTemp = Pschild;
                            }
                            break;

                        case 2: // kij
                        case 7: // wuda
                        case 9: // wuda+hocka
                        case 10: // wuda+hocka+dryba
                        case 11: // wuda+hocka+wacki
                            nextActionID = 230;
                            pTemp = Pschild;
                            break;

                        default:
                            nextActionID = 155;
                            pTemp = Pschild;
                    }
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
                return;
            }
        }

        // normaler Cursor, normale Reaktion
        else {
            if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
                // linke Maustaste
                nextActionID = 0;

                // zu Haty gehen ?
                if (untererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (untererAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pdown;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pdown.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Horiz gehen
                if (linkerAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (linkerAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pleft;
                    } else {
                        pTemp = new GenericPoint(Pleft.x, kt.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // oberer Ausgang zu Zdzary
                if (obererAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (obererAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pup;
                    } else {
                        pTemp = new GenericPoint(kt.x, Pup.y);
                    }

                    if (mainFrame.dClick == true) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // rechter Ausgang
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    nextActionID = 103;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (rechterAusgang.IsPointInRect(kt) == false) {
                        pTemp = Pright;
                    } else {
                        pTemp = new GenericPoint(Pright.x, kt.y);
                    }

                    // Dopppelclick waere nix gutt
				/*if (mainFrame.dClick == true)
				  {
				  mainFrame.krabat.StopWalking();
				  mainFrame.repaint();
				  return;
				  } */
                }

                // Schild ansehen
                if ((brSchild.IsPointInRect(pTemp) == true) && (mainFrame.Actions[901] == false)) {
                    nextActionID = 1;
                    pTemp = Pschild;
                }

                // Schild oben ansehen
                if (brSchildOben.IsPointInRect(pTemp) == true) {
                    nextActionID = 4;
                    pTemp = Pschild;
                }

                // Rapak ansehen
                if (rapakRect.IsPointInRect(pTemp) == true) {
                    nextActionID = 2;
                    pTemp = Prapak;
                }

                // Pjero ansehen
                if ((pjeroRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[919] == false) &&
                        (mainFrame.Actions[210] == true)) {
                    nextActionID = 3;
                    pTemp = Ppjero;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Haty Anschauen
                if (untererAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Horiz anschauen
                if (linkerAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Zdzary anschauen
                if (obererAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // rechten Ausgang anschauen
                if (rechterAusgang.IsPointInRect(pTemp) == true) {
                    return;
                }

                // Schild mitnehmen
                if ((brSchild.IsPointInRect(pTemp) == true) && (mainFrame.Actions[901] == false)) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Schild oben mitnehmen
                if (brSchildOben.IsPointInRect(pTemp) == true) {
                    nextActionID = 65;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pschild);
                    mainFrame.repaint();
                    return;
                }

                // Raben fangen
                if (rapakRect.IsPointInRect(pTemp) == true) {
                    nextActionID = 55;
                    mainFrame.wegGeher.SetzeNeuenWeg(Prapak);
                    mainFrame.repaint();
                    return;
                }

                // Pjero fangen
                if ((pjeroRect.IsPointInRect(pTemp) == true) && (mainFrame.Actions[919] == false) &&
                        (mainFrame.Actions[210] == true)) {
                    nextActionID = 60;
                    mainFrame.wegGeher.SetzeNeuenWeg(PpjeroAufh);
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
            if (((brSchild.IsPointInRect(pTemp) == true) && (mainFrame.Actions[901] == false))
                    || (tmp.IsPointInRect(pTemp) == true) || (rapakRect.IsPointInRect(pTemp) == true) ||
                    ((mainFrame.Actions[919] == false) && (mainFrame.Actions[210] == false) && (pjeroRect.IsPointInRect(pTemp) == true)) ||
                    (brSchildOben.IsPointInRect(pTemp) == true)) {
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
            if (obererAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 4) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 4;
                }
                return;
            }

            if (((brSchild.IsPointInRect(pTemp) == true) && (mainFrame.Actions[901] == false)) ||
                    (rapakRect.IsPointInRect(pTemp) == true) || (brSchildOben.IsPointInRect(pTemp) == true) ||
                    ((mainFrame.Actions[919] == false) && (mainFrame.Actions[210] == true) && (pjeroRect.IsPointInRect(pTemp) == true))) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }


            if (linkerAusgang.IsPointInRect(pTemp) == true) {
                if (Cursorform != 2) {
                    mainFrame.setCursor(mainFrame.Cleft);
                    Cursorform = 2;
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

            if (untererAusgang.IsPointInRect(pTemp) == true) {
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
                // Schild anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00000"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00001"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00002"),
                        fSchild, 3, 0, 0);
                break;

            case 2:
                // Raben anschauen, Zufallszahl zwischen 0 und 1 generieren
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00003"),
                                Start.stringManager.getTranslation("Loc1_Rapak1_00004"),
                                Start.stringManager.getTranslation("Loc1_Rapak1_00005"),
                                fRapak, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00006"),
                                Start.stringManager.getTranslation("Loc1_Rapak1_00007"),
                                Start.stringManager.getTranslation("Loc1_Rapak1_00008"),
                                fRapak, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Pjero anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00009"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00010"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00011"),
                        fPjero, 3, 0, 0);
                break;

            case 4:
                // Schild oben anschauen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00012"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00013"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00014"),
                        fSchildOben, 3, 0, 0);
                break;

            case 50:
                // Schild mitnehmen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wave.PlayFile("sfx/schildnehmen.wav");
                nextActionID = 53;
                mainFrame.krabat.SetFacing(fSchild);
                mainFrame.krabat.nAnimation = 121;
                Counter = 5;
                break;

            case 53:
                // Ende Schild aufheben
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(new Integer(3));
                    mainFrame.Clipset = false;
                    mainFrame.Actions[901] = true;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                nextActionID = 0;
                break;

            case 55:
                // Raben mitnehmen
                animit = true;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wave.PlayFile("sfx/rapak1.wav");
                mainFrame.krabat.SetFacing(fRapak);
                mainFrame.krabat.nAnimation = 150;
                nextActionID = 57;
                break;

            case 57:
                // Krabat sagt Spruch
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                if (animit == true) {
                    break;
                }
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00015"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00016"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00017"),
                        0, 3, 0, 58);
                break;

            case 58:
                // Ende Anim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 60:
                // Feder mitnehmen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 63;
                mainFrame.krabat.SetFacing(fPjero);
                mainFrame.krabat.nAnimation = 122;
                Counter = 5;
                break;

            case 63:
                // Ende Feder aufheben
                if ((--Counter) == 1) {
                    mainFrame.inventory.vInventory.addElement(new Integer(19));
                    mainFrame.Clipset = false;
                    mainFrame.Actions[919] = true;
                }
                if ((mainFrame.krabat.nAnimation != 0) || (Counter > 0)) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 65:
                // Schild oben mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00018"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00019"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00020"),
                        fSchildOben, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Haty
                NeuesBild(4, 8);
                break;

            case 101:
                // nach Horiz gehen
                NeuesBild(9, 8);
                break;

            case 102:
                // gehe zu Zdzary
                NeuesBild(19, 8);
                break;

            case 103:
                // nach rechts gehen will ich nicht !
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00021"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00022"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00023"),
                        fExitRight, 3, 0, 0);
                break;

            case 150:
                // Schild - Ausreden
                DingAusrede(fSchild);
                break;

            case 155:
                // Rapak - Ausreden
                DingAusrede(fRapak);
                break;

            case 160:
                // Schiessen
                mainFrame.krabat.SetFacing(fSchiessen);
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.inventory.vInventory.addElement(new Integer(17));
                mainFrame.inventory.vInventory.removeElement(new Integer(18)); // Waffe wieder entladen
                mainFrame.invCursor = false;
                Sonderstatus = 1;
                nextActionID = 161;
                break;

            case 161:
                // warten auf Ende Schiessen
                if (Sonderstatus == 0) {
                    nextActionID = 163;
                }
                break;

            case 163:
                // Rabe laesst Feder fallen
                nextActionID = 164;
                animit = true;
                if (mainFrame.Actions[210] == false) {
                    segeln = true;
                }
                break;

            case 164:
                // Auf Ende beider Anims warten und dann beenden
                if ((animit == true) || (segeln == true)) {
                    break;
                }
                mainFrame.Clipset = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.Actions[210] = true;
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 165:
                // Feder - Ausreden
                DingAusrede(fPjero);
                break;

            case 170:
                // Schild oben - Ausreden
                DingAusrede(fSchildOben);
                break;

            case 200:
                // Kamuski auf schildern
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00024"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00025"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00026"),
                        fSchild, 3, 0, 0);
                break;

            case 210:
                // Kij auf schild unten
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00027"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00028"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00029"),
                        fSchild, 3, 0, 0);
                break;

            case 220:
                // bron auf rapak 2. Mal
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00030"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00031"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00032"),
                        fRapak, 3, 0, 0);
                break;

            case 230:
                // Stock+Kombinationen auf Rapak
                KrabatSagt(Start.stringManager.getTranslation("Loc1_Rapak1_00033"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00034"),
                        Start.stringManager.getTranslation("Loc1_Rapak1_00035"),
                        fRapak, 3, 0, 0);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}