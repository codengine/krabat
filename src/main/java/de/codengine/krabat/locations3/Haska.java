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

package de.codengine.krabat.locations3;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.KrabatWerfen;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Haska extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Haska.class);
    private GenericImage background;
    private GenericImage ohneSeil;
    private final GenericImage[] tropfen;
    private boolean switchanim = false;
    private int weinCount = 0;

    private final KrabatWerfen krabatWerfen;

    private boolean krabatWirft = false;

    private boolean setAnim = false;

    private boolean schnauzeWein = false;

    // Konstanten - Rects
    private static final Borderrect ausgangKuchnja
            = new Borderrect(421, 275, 463, 391);
    private static final Borderrect ausgangStadt
            = new Borderrect(330, 254, 370, 363);
    private static final Borderrect fenster
            = new Borderrect(33, 11, 110, 127);
    private static final Borderrect wino
            = new Borderrect(452, 396, 509, 452);
    // private static final borderrect komedHaus
    //     = new borderrect (115, 135, 204, 365);
    private static final Borderrect seilHaken
            = new Borderrect(60, 132, 90, 425);

    // Konstante Points
    private static final GenericPoint pExitKuchnja = new GenericPoint(400, 420);
    private static final GenericPoint pFenster = new GenericPoint(170, 450);
    private static final GenericPoint pExitStadt = new GenericPoint(320, 367);
    private static final GenericPoint pWino = new GenericPoint(438, 460);
    private static final GenericPoint pAmHaken = new GenericPoint(116, 464);

    // Konstante ints
    private static final int fKuchnja = 3;
    private static final int fFenster = 9;
    private static final int fWino = 3;
    private static final int fEnterhaken = 9;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Haska(Start caller, int oldLocation) {
        super(caller, 121);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();
        tropfen = new GenericImage[9];

        krabatWerfen = new KrabatWerfen(mainFrame);

        mainFrame.krabat.maxx = 424;
        mainFrame.krabat.zoomf = 1.48f;
        mainFrame.krabat.defScale = -50;

        krabatWerfen.maxx = mainFrame.krabat.maxx;
        krabatWerfen.zoomf = mainFrame.krabat.zoomf;
        krabatWerfen.defScale = mainFrame.krabat.defScale;

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(234, 320, 193, 370, 367, 419));
        mainFrame.wegGeher.vBorders.addElement
                (new Bordertrapez(193, 400, 145, 443, 420, 479));

        mainFrame.wegSucher.ClearMatrix(2);

        mainFrame.wegSucher.PosVerbinden(0, 1);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 120: // von Kuchnja aus
            case 132: // von Kuchnjaopen dasselbe
                mainFrame.krabat.setPos(new GenericPoint(400, 420));
                mainFrame.krabat.SetFacing(9);
                break;
            case 122: // von Spaniska aus
                mainFrame.krabat.setPos(new GenericPoint(180, 460));
                mainFrame.krabat.SetFacing(3);
                if (!mainFrame.Actions[519]) {
                    setAnim = true;
                    mainFrame.Actions[519] = true;
                }
                break;
            case 126: // von Murja aus
                mainFrame.krabat.setPos(new GenericPoint(322, 370));
                mainFrame.krabat.SetFacing(9);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/haska/haska.gif");
        ohneSeil = getPicture("gfx-dd/haska/haska2.gif");

        tropfen[0] = getPicture("gfx-dd/haska/wino0.gif");
        tropfen[1] = getPicture("gfx-dd/haska/wino1.gif");
        tropfen[2] = getPicture("gfx-dd/haska/wino2.gif");
        tropfen[3] = getPicture("gfx-dd/haska/wino3.gif");
        tropfen[4] = getPicture("gfx-dd/haska/wino4.gif");
        tropfen[5] = getPicture("gfx-dd/haska/wino5.gif");
        tropfen[6] = getPicture("gfx-dd/haska/wino6.gif");
        tropfen[7] = getPicture("gfx-dd/haska/wino7.gif");
        tropfen[8] = getPicture("gfx-dd/haska/wino8.gif");

    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

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

        // Seil uebermalen, falls noch keins da
        if (!mainFrame.Actions[519]) {
            g.drawImage(ohneSeil, 53, 111);
        }

        // Anim (Weintropfen) zeichnen, da stets im Hintergrund
        if (mainFrame.isAnim) {
            switchanim = !switchanim;
            if (switchanim) {
                weinCount++;
                evalSound(weinCount);
                if (weinCount == 13) {
                    weinCount = 0;
                }
            }
            g.setClip(447, 436, 11, 15);
            if (weinCount < 9) {
                g.drawImage(tropfen[weinCount], 448, 437);
            } else {
                g.drawImage(tropfen[0], 448, 437);
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        mainFrame.wegGeher.GeheWeg();

        // hier die Extrawurst fuer den Hakenwurf
        if (krabatWirft) {
            krabatWirft = krabatWerfen.drawKrabat(g, mainFrame.krabat.getPos());
        } else {
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
        }

        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
        /*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
          {
          g.drawImage (weiden2, 84, 221, null);
          }*/

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

        // wenn automatische Anim, dann jetzt beginnen
        if (setAnim) {
            mainFrame.krabat.StopWalking();
            setAnim = false;
            nextActionID = 27;
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

                Borderrect tmp = mainFrame.krabat.getRect();

                // Aktion, wenn Krabat angeclickt wurde
                if (tmp.IsPointInRect(pTemp)) {
                    nextActionID = 500 + mainFrame.whatItem;
                    mainFrame.repaint();
                    return;
                }

                // Enterhaken mit Fenster benutzen
                if (fenster.IsPointInRect(pTemp)) {
                    // kotwica + lajna
                    nextActionID = mainFrame.whatItem == 39 ? 20 : 155;
                    pTemp = pFenster;
                }

                // Helm mit wein fuellen
                if (wino.IsPointInRect(pTemp)) {
                    switch (mainFrame.whatItem) {
                        case 43: // Friedhelm
                            nextActionID = 30;
                            break;
                        case 53: // drasta
                            nextActionID = 200;
                            break;
                        case 52: // wosusk
                            nextActionID = 210;
                            break;
                        default:
                            nextActionID = 150;
                            break;
                    }
                    pTemp = pWino;
                }

                // Enterhaken am Haus Ausreden
                if (seilHaken.IsPointInRect(pTemp) && mainFrame.Actions[519]) {
                    pTemp = pFenster;
                    nextActionID = 160;
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

                // zu Ostansicht gehen ?
                if (ausgangStadt.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangStadt.IsPointInRect(kt)) {
                        pTemp = pExitStadt;
                    } else {
                        pTemp = new GenericPoint(pExitStadt.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Kuchnja gehen ? - nicht reingehen -> Ausreden
                if (ausgangKuchnja.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

                    // gerade verlassen, wenn nahe
                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangKuchnja.IsPointInRect(kt)) {
                        pTemp = pExitKuchnja;
                    } else {
                        pTemp = new GenericPoint(pExitKuchnja.x, kt.y);
                    }

                    // nur dann Doppelklick, wenn man in die Kueche gehen kann
                    if (mainFrame.dClick && mainFrame.Actions[655]) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Fenster ansehen
                if (fenster.IsPointInRect(pTemp)) {
                    nextActionID = 4;
                    pTemp = pFenster;
                }


                // Wino ansehen
                if (wino.IsPointInRect(pTemp)) {
                    nextActionID = 1;
                    pTemp = pWino;
                }

                // KomedHaus ansehen
                        /*if (komedHaus.IsPointInRect (pTemp) == true) 
                          {
                          nextActionID = 3;
                          }*/

                // Enterhaken am Haus ansehen
                if (seilHaken.IsPointInRect(pTemp) && mainFrame.Actions[519]) {
                    pTemp = pFenster;
                    nextActionID = 6;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Wino trinken
                if (wino.IsPointInRect(pTemp)) {
                    nextActionID = 2;
                    mainFrame.wegGeher.SetzeNeuenWeg(pWino);
                    mainFrame.repaint();
                    return;
                }

                // Fenster benutzen
                if (fenster.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    mainFrame.wegGeher.SetzeNeuenWeg(pFenster);
                    mainFrame.repaint();
                    return;
                }

                // Am Enterhaken hochklettern
                if (seilHaken.IsPointInRect(pTemp) && mainFrame.Actions[519]) {
                    nextActionID = 101;
                    mainFrame.wegGeher.SetzeNeuenWeg(pFenster);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen
                if (ausgangKuchnja.IsPointInRect(pTemp) ||
                        ausgangStadt.IsPointInRect(pTemp)) {
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    fenster.IsPointInRect(pTemp) ||
                    wino.IsPointInRect(pTemp) ||
                    mainFrame.Actions[519] &&
                            seilHaken.IsPointInRect(pTemp);

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
            if (wino.IsPointInRect(pTemp) ||
                    // (komedHaus.IsPointInRect (pTemp) == true) ||
                    fenster.IsPointInRect(pTemp) ||
                    mainFrame.Actions[519] &&
                            seilHaken.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangKuchnja.IsPointInRect(pTemp) ||
                    ausgangStadt.IsPointInRect(pTemp)) {
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

    private void evalSound(int wein) {
        // zufaellig wavs fuer Geschnatter abspielen...
        if (schnauzeWein) {
            return;
        }

        if (wein == 5) {
            mainFrame.wave.PlayFile("sfx-dd/tropf.wav");
        }

    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // nichts zu tun, oder Krabat laeuft noch
        if (mainFrame.krabat.isWandering ||
                mainFrame.krabat.isWalking) {
            return;
        }

        // hier wird zu den Standardausreden von Krabat verzweigt, 
        // wenn noetig (in Superklasse)
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
                // Wino anschauen
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Haska_1", fWino, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Haska_2", fWino, 3, 0, 0);
                        break;
                }
                break;

            case 2:
                // Wino benutzen
                KrabatSagt("Haska_3", fWino, 3, 0, 0);
                break;

            case 4:
                // Fenster ansehen
                KrabatSagt("Haska_4", fFenster, 3, 0, 0);
                break;

            case 5:
                // versuch ins Fenster reinzukommen (benutzen)
                KrabatSagt("Haska_5", fFenster, 3, 0, 0);
                break;

            case 6:
                // Enterhaken am Fenster ansehen
                KrabatSagt("Haska_6", fEnterhaken, 3, 0, 0);
                break;

            case 20:
                // Enterhaken an Fenster schmeissen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fEnterhaken);
                schnauzeWein = true;
                Counter = 20;
                nextActionID = 21;
                break;

            case 21:
                // Weile warten (Sound-Wein zu Ende) und dann werfen
                if (--Counter > 1) {
                    break;
                }
                mainFrame.invCursor = false;
                // Enterhaken-Kombi aus Inventory entfernen
                mainFrame.inventory.vInventory.removeElement(39);
                nextActionID = 22;
                krabatWirft = true;
                break;

            case 22:
                // warten auf Ende werfe Haken
                if (krabatWirft) {
                    break;
                }
                NeuesBild(122, locationID);
                break;

            case 27:
                // Bemerkung nach Hakenwurf
                KrabatSagt("Haska_7", fEnterhaken, 3, 0, 28);
                break;

            case 28:
                // Ende Anim
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                break;

            case 30:
                // Helm mit wein fuellen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(12);
                mainFrame.invCursor = false;
                mainFrame.inventory.vInventory.removeElement(43);
                mainFrame.inventory.vInventory.addElement(44);
                nextActionID = 35;
                Counter = 20;
                schnauzeWein = true;
                break;

            case 35:
                // Helm durch HelmWein in Inventory erstetzen
                // Buecken
                if (--Counter > 1) {
                    break;
                }
                mainFrame.krabat.nAnimation = 152;
                nextActionID = 37;
                mainFrame.wave.PlayFile("sfx-dd/tropftropf.wav");
                Counter = 15;
                break;

            case 37:
                if (mainFrame.krabat.nAnimation != 0 || --Counter > 0) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                schnauzeWein = false;
                mainFrame.repaint();
                break;

            case 100:
                // In Kueche gehen -> Krabat will nicht
                if (!mainFrame.Actions[655]) {
                    int zuffZahl2 = (int) (Math.random() * 2.9);
                    switch (zuffZahl2) {
                        case 0:
                            KrabatSagt("Haska_8", fKuchnja, 3, 0, 0);
                            break;

                        case 1:
                            KrabatSagt("Haska_9", fKuchnja, 3, 0, 0);
                            break;

                        case 2:
                            KrabatSagt("Haska_10", fKuchnja, 3, 0, 0);
                            break;
                    }
                } else {
                    // ab jetzt will er...
                    NeuesBild(132, locationID);
                }
                break;

            case 101:
                // Gehe zu Spaniska, zuerst richtig rangehen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pAmHaken);
                nextActionID = 110;
                break;

            case 102:
                // Gehe zu Murja
                // hier unterscheiden, welche Location konstruhiert wird
                if (mainFrame.Actions[655] && !mainFrame.Actions[656]) {
                    NeuesBild(129, locationID);
                } else {
                    NeuesBild(126, locationID);
                }
                break;

            case 110:
                // Spruch vorm hochklettern
                KrabatSagt("Haska_11", fEnterhaken, 3, 0, 112);
                break;

            case 112:
                // wir sind am Haken, also jetzt "nehmen"-Anim
                mainFrame.krabat.nAnimation = 90;
                nextActionID = 114;
                Counter = 4;
                break;

            case 114:
                // kurz vor Ende Nehmen umschalten
                if (--Counter > 1) {
                    break;
                }
                NeuesBild(122, locationID);
                break;

            case 200:
                // drasta auf wino
                KrabatSagt("Haska_12", fWino, 3, 0, 0);
                break;

            case 210:
                // wosusk auf wino
                KrabatSagt("Haska_13", fWino, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}