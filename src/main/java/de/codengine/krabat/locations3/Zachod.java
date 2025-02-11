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
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Zachod extends Mainloc {
    private GenericImage background;
    private GenericImage seil;
    private GenericImage deska;
    private GenericImage kista;
    private GenericImage kista2;
    private final GenericImage[] krabat_schieb;

    // Konstanten - Rects
    private static final Borderrect ausgangCychi
            = new Borderrect(468, 190, 532, 294);
    private static final Borderrect ausgangGang
            = new Borderrect(132, 376, 230, 421);
    private static final Borderrect rectSeil
            = new Borderrect(574, 271, 598, 298);
    private static final Borderrect staraDeska
            = new Borderrect(542, 432, 584, 457);
    private static final Borderrect blido
            = new Borderrect(300, 324, 377, 350);
    private static final Borderrect korbik
            = new Borderrect(162, 138, 194, 180);
    private static final Borderrect kisty
            = new Borderrect(99, 345, 200, 400);
    private static final Borderrect kistyUnten
            = new Borderrect(48, 394, 150, 479);
    private static final Borderrect nowaDeska
            = new Borderrect(156, 371, 204, 429);
    private static final Borderrect kistenRect
            = new Borderrect(0, 330, 300, 420);

    private static final GenericPoint AnfangsPunkt = new GenericPoint(99, 345);
    private static final GenericPoint AnfangsPunkt2 = new GenericPoint(157, 347);
    private static final GenericPoint EndPunkt = new GenericPoint(48, 394);

    private static final float Xoffset = 3f;
    private final float Yoffset;

    private float xpos;
    private float ypos;
    private GenericPoint festnagelPoint = new GenericPoint(0, 0); // Krabat darf sich beim schieben nicht relativ zu den Kisten bewegen
    private GenericPoint bezugsPunkt = new GenericPoint(0, 0);

    private boolean verschiebeKiste = false;

    private boolean kistenSound = false;

    private int AnimPosition = 0;
    private int AnimCounter = 0;
    private int Anfangsscale = 0;

    // Konstante Points
    private static final GenericPoint pExitCychi = new GenericPoint(500, 299);
    private static final GenericPoint pExitGang = new GenericPoint(180, 400);
    private static final GenericPoint pSeil = new GenericPoint(559, 341);
    private static final GenericPoint pStaraDeska = new GenericPoint(542, 434);
    private static final GenericPoint pBlido = new GenericPoint(314, 420);
    private static final GenericPoint pKorbik = new GenericPoint(175, 324);
    private static final GenericPoint pKistyOben = new GenericPoint(179, 385);
    private static final GenericPoint pKistyUnten = new GenericPoint(227, 465);
    private static final GenericPoint pNowaDeska = new GenericPoint(207, 395);
    private static final GenericPoint pEntnagel1 = new GenericPoint(185, 377);
    private static final GenericPoint pEntnagel2 = new GenericPoint(207, 380);
    private static final GenericPoint pEntnagel3 = new GenericPoint(140, 406);
    private static final GenericPoint pEntnagel4 = new GenericPoint(158, 418);

    // Konstante ints
    private static final int fKisteOben = 6;
    private static final int fKisteUnten = 9;
    private static final int fSeil = 3;
    private static final int fBrettNeu = 9;
    private static final int fBrettAlt = 3;
    private static final int fBlido = 12;
    private static final int fKorbik = 12;

    private int Counter = 0;

    private int SonderAnim = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Zachod(Start caller, int oldLocation) {
        super(caller, 151);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().stop();

        // Berechnungen fuer Kistenverschieben

        // Anfangskoordinaten
        xpos = AnfangsPunkt.x;
        ypos = AnfangsPunkt.y;

        // Y - Offset beim Verschieben
        float xe = EndPunkt.x;
        float ye = EndPunkt.y;

        Yoffset = (ye - ypos) / (xe - xpos) * Xoffset;

        // System.out.println ("Y - Offset = " + Yoffset);

        mainFrame.krabat.maxx = 403;
        mainFrame.krabat.zoomf = 2.29f;
        mainFrame.krabat.defScale = -50;

        krabat_schieb = new GenericImage[2];

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        InitMatrix();

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                break;
            case 150: // von Cychi
                mainFrame.krabat.SetKrabatPos(new GenericPoint(500, 310));
                mainFrame.krabat.SetFacing(6);
                break;
            case 152: // von Gang
                mainFrame.krabat.SetKrabatPos(new GenericPoint(214, 395));
                mainFrame.krabat.SetFacing(6);
                break;
        }
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx-dd/zachod/zachod.gif");

        seil = getPicture("gfx-dd/zachod/seil.gif");
        deska = getPicture("gfx-dd/zachod/deska.gif");
        kista = getPicture("gfx-dd/zachod/kista.gif");
        kista2 = getPicture("gfx-dd/zachod/kista2.gif");

        krabat_schieb[0] = getPicture("gfx-dd/zachod/k-u-kiste1.gif");
        krabat_schieb[1] = getPicture("gfx-dd/zachod/k-u-kiste2.gif");

    }

    // Laufrectangles aendern, je nachdem, wo Kisten sind
    private void InitMatrix() {
        if (!mainFrame.Actions[516]) {
            // Kisten sind noch oben
            mainFrame.wegGeher.vBorders.removeAllElements();
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(480, 299, 523, 407));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(524, 340, 560, 342));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(467, 541, 467, 470, 408, 479));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(75, 466, 20, 466, 444, 479));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(242, 244, 298, 300, 381, 443));
            mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(155, 375, 244, 380));

            mainFrame.wegSucher.ClearMatrix(6);

            mainFrame.wegSucher.PosVerbinden(0, 1);
            mainFrame.wegSucher.PosVerbinden(0, 2);
            mainFrame.wegSucher.PosVerbinden(2, 3);
            mainFrame.wegSucher.PosVerbinden(3, 4);
            mainFrame.wegSucher.PosVerbinden(4, 5);
        } else {
            // Kisten sind unten
            // Brett ist noch drauf
            if (!mainFrame.Actions[517]) {
                mainFrame.wegGeher.vBorders.removeAllElements();
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(480, 299, 523, 407));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(524, 340, 560, 342));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(467, 541, 467, 470, 408, 479));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(233, 466, 220, 466, 444, 479));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(150, 245, 150, 300, 387, 443));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(120, 393, 149, 443));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(159, 370, 210, 386));

                mainFrame.wegSucher.ClearMatrix(7);

                mainFrame.wegSucher.PosVerbinden(0, 1);
                mainFrame.wegSucher.PosVerbinden(0, 2);
                mainFrame.wegSucher.PosVerbinden(2, 3);
                mainFrame.wegSucher.PosVerbinden(3, 4);
                mainFrame.wegSucher.PosVerbinden(4, 5);
                mainFrame.wegSucher.PosVerbinden(4, 6);
            } else {
                // Brett ist weg
                mainFrame.wegGeher.vBorders.removeAllElements();
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(480, 299, 523, 407));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(524, 340, 560, 342));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(467, 541, 467, 470, 408, 479));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(233, 466, 220, 466, 444, 479));
                mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(201, 245, 220, 300, 387, 443));

                mainFrame.wegSucher.ClearMatrix(5);

                mainFrame.wegSucher.PosVerbinden(0, 1);
                mainFrame.wegSucher.PosVerbinden(0, 2);
                mainFrame.wegSucher.PosVerbinden(2, 3);
                mainFrame.wegSucher.PosVerbinden(3, 4);
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

        // Hintergrund fuer Kiste und Brett loeschen (nur 1 Mal, dafuer richtig !)
        g.setClip(55, 343, 220, 137);
        g.drawImage(background, 0, 0);

        // Seil zeichnen, solange noch da
        if (!mainFrame.Actions[518]) {
            g.setClip(574, 268, 29, 51);
            g.drawImage(background, 0, 0);
            g.drawImage(seil, 574, 268);
        }

        // Kiste bzw. Deska zeichnen
        // Deska zuerst, da im Hintergrund
        if (!mainFrame.Actions[517]) {
            g.setClip(115, 367, 95, 56);
            g.drawImage(deska, 115, 367);
        } else {
            g.setClip(67, 358, 95, 56);
            g.drawImage(deska, 67, 358);
        }

        // Krabats neue Position hier bestimmen, damit beide Kistenzeichenroutinen
        // mit dem gleichen Punkt arbeiten...
        mainFrame.wegGeher.GeheWeg();

        // Kiste oben zeichnen, wenn keine Verschieberoutine und wenn Krabat davor ist
        if (!verschiebeKiste) {
            if (!mainFrame.Actions[516] &&
                    !kistenRect.IsPointInRect(mainFrame.krabat.GetKrabatPos())) {
                // Kisten sind nicht verschoben und Krabat ist davor
                g.setClip(AnfangsPunkt.x, AnfangsPunkt.y, 178, 183);
                g.drawImage(kista, AnfangsPunkt.x, AnfangsPunkt.y);
                g.drawImage(kista2, AnfangsPunkt2.x, AnfangsPunkt2.y);
            }
      
                /*if (mainFrame.Actions[516] == true)
                    {
                        // Kisten sind verschoben, dann Krabat nicht im Rect, also immer hier zeichnen
                        g.setClip (EndPunkt.x, EndPunkt.y, 178, 183);
                        g.drawImage (kista, EndPunkt.x, EndPunkt.y, null);
                        g.drawImage (kista2, EndPunkt.x + (AnfangsPunkt2.x - AnfangsPunkt.x), EndPunkt.y + (AnfangsPunkt2.y - AnfangsPunkt.y), null);
			}*/
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        if (SonderAnim != 0) {
            // Sonderanims ausfuehren
            if (SonderAnim == 1) {
                // Kisten schieben

                // Groesse
                int scale = mainFrame.krabat.defScale;
                scale += (int) (((float) mainFrame.krabat.maxx - (float) (festnagelPoint.y - 50)) / mainFrame.krabat.zoomf);

                // zum 1. Mal merken, wie gross Scale war
                if (Anfangsscale == 0) {
                    Anfangsscale = scale;
                }

                // Hoehe: nur offset
                int hoch = 100 - scale;

                // Breite
                int weit = 50 - scale / 2;

                // Punkt fuer LO-Evaluierung bereitstellen
                GenericPoint hier = new GenericPoint(festnagelPoint.x, festnagelPoint.y);

                hier.x -= weit / 2;
                hier.y -= hoch;

                hier.y += Math.abs((Anfangsscale - scale) / 2);

                // Cliprect setzen
                g.setClip(hier.x, hier.y, weit + 1, (hoch + 1) / 2);

                // GenericImage weiterschalten
                if (++AnimCounter > 4) {
                    AnimCounter = 0;
                    if (AnimPosition == 1) {
                        AnimPosition = 0;
                    } else {
                        AnimPosition = 1;
                    }
                }

                // Krabat zeichnen
                g.drawImage(krabat_schieb[AnimPosition], hier.x, hier.y, weit, hoch);

                // System.out.println ("Krabats Pos ist: " + festnagelPoint.x + " " + festnagelPoint.y);
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
        GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos();

        // hinter weiden2 (nur Clipping - Region wird neugezeichnet)
        /*if (weiden2Rect.IsPointInRect (pKrTemp) == true)
          {
          g.drawImage (weiden2, 84, 221, null);
          }*/

        GenericRectangle myx;
        myx = g.getClipBounds();

        // Kiste oben zeichnen, aber nur, wenn nicht gerade die Verschieberoutine aktiv ist
        // und nur, wenn Krabat dahinter ist
        if (!verschiebeKiste && kistenRect.IsPointInRect(pKrTemp) &&
                !mainFrame.Actions[516]) {
            // Kisten sind nicht verschoben
            g.setClip(AnfangsPunkt.x, AnfangsPunkt.y, 178, 183);
            g.drawImage(kista, AnfangsPunkt.x, AnfangsPunkt.y);
            g.drawImage(kista2, AnfangsPunkt2.x, AnfangsPunkt2.y);
        }

        // Hier Routine fuer Kiste verschieben aufrufen, Krabat ist immer dahinter
        if (verschiebeKiste) {
            g.setClip(50, 340, 220, 140);
            verschiebeKiste = MoveIt(g);
        }

        // verschobene Kisten sind auch immer vor Krabat
        if (mainFrame.Actions[516] && !verschiebeKiste) {
            // Kisten sind verschoben immer hier zeichnen
            g.setClip(EndPunkt.x, EndPunkt.y, 178, 183);
            g.drawImage(kista, EndPunkt.x, EndPunkt.y);
            g.drawImage(kista2, EndPunkt.x + AnfangsPunkt2.x - AnfangsPunkt.x, EndPunkt.y + AnfangsPunkt2.y - AnfangsPunkt.y);
        }

        g.setClip(myx.getX(), myx.getY(), myx.getWidth(), myx.getHeight());

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

                // Ausreden fuer Kiste, je nachdem, wo, hier oben
                if (kisty.IsPointInRect(pTemp) && !mainFrame.Actions[516]) {
                    nextActionID = 150;
                    pTemp = pKistyOben;
                }

                // Kiste unten
                if (kistyUnten.IsPointInRect(pTemp) && mainFrame.Actions[516]) {
                    nextActionID = 155;
                    pTemp = pKistyUnten;
                }

                // Seil Ausreden, falls noch da
                if (rectSeil.IsPointInRect(pTemp) && !mainFrame.Actions[518]) {
                    nextActionID = 160;
                    pTemp = pSeil;
                }

                // Brett Ausreden, falls noch da und Kiste verschoben
                if (nowaDeska.IsPointInRect(pTemp) &&
                        !mainFrame.Actions[517] && mainFrame.Actions[516]) {
                    switch (mainFrame.whatItem) {
                        case 46: // hamor
                            nextActionID = 180;
                            break;
                        case 42: // hlebija
                            nextActionID = 200;
                            break;
                        default:
                            nextActionID = 165;
                            break;
                    }
                    pTemp = pNowaDeska;
                }

                // deska alt Ausreden
                if (staraDeska.IsPointInRect(pTemp)) {
                    nextActionID = 210;
                    pTemp = pStaraDeska;
                }

                // blido Ausreden
                if (blido.IsPointInRect(pTemp)) {
                    nextActionID = 220;
                    pTemp = pBlido;
                }

                // korbik Ausreden
                if (korbik.IsPointInRect(pTemp)) {
                    nextActionID = 230;
                    pTemp = pKorbik;
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

                // zu Cychi gehen ?
                if (ausgangCychi.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangCychi.IsPointInRect(kt)) {
                        pTemp = pExitCychi;
                    } else {
                        pTemp = new GenericPoint(pExitCychi.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // zu Gang gehen ?
                if (ausgangGang.IsPointInRect(pTemp) && mainFrame.Actions[517]) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.GetKrabatPos();

                    // Wenn nahe am Ausgang, dann "gerade" verlassen
                    if (!ausgangGang.IsPointInRect(kt)) {
                        pTemp = pExitGang;
                    } else {
                        pTemp = new GenericPoint(pExitGang.x, kt.y);
                    }

                    if (mainFrame.dClick) {
                        mainFrame.krabat.StopWalking();
                        mainFrame.repaint();
                        return;
                    }
                }

                // Seil ansehen, falls noch da
                if (rectSeil.IsPointInRect(pTemp) && !mainFrame.Actions[518]) {
                    nextActionID = 1;
                    pTemp = pSeil;
                }

                // Kiste unverschoben ansehen
                if (kisty.IsPointInRect(pTemp) && !mainFrame.Actions[516]) {
                    nextActionID = 3;
                    pTemp = pKistyOben;
                }

                // Kiste verschoben ansehen
                if (kistyUnten.IsPointInRect(pTemp) && mainFrame.Actions[516]) {
                    nextActionID = 8;
                    pTemp = pKistyUnten;
                }

                // Deska ansehen, wenn noch da, nur bei verschobener Kiste
                if (nowaDeska.IsPointInRect(pTemp) &&
                        !mainFrame.Actions[517] && mainFrame.Actions[516]) {
                    nextActionID = 4;
                    pTemp = pNowaDeska;
                }

                // stara Deska ansehen
                if (staraDeska.IsPointInRect(pTemp)) {
                    nextActionID = 5;
                    pTemp = pStaraDeska;
                }

                // Blido ansehen
                if (blido.IsPointInRect(pTemp)) {
                    nextActionID = 6;
                    pTemp = pBlido;
                }

                // Korbik ansehen
                if (korbik.IsPointInRect(pTemp)) {
                    nextActionID = 7;
                    pTemp = pKorbik;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
                mainFrame.repaint();
            } else {
                // rechte Maustaste

                // Seil mitnehmen
                if (rectSeil.IsPointInRect(pTemp) && !mainFrame.Actions[518]) {
                    nextActionID = 20;
                    mainFrame.wegGeher.SetzeNeuenWeg(pSeil);
                    mainFrame.repaint();
                    return;
                }

                // Kiste verschieben
                if (kisty.IsPointInRect(pTemp) && !mainFrame.Actions[516]) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKistyOben);
                    mainFrame.repaint();
                    return;
                }

                // verschobene Kiste nicht nochmal verschieben
                if (kistyUnten.IsPointInRect(pTemp) && mainFrame.Actions[516]) {
                    nextActionID = 80;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKistyUnten);
                    mainFrame.repaint();
                    return;
                }

                // Brett mitnehmen, nur wenn da und sichtbar
                if (nowaDeska.IsPointInRect(pTemp) &&
                        !mainFrame.Actions[517] && mainFrame.Actions[516]) {
                    nextActionID = 85;
                    mainFrame.wegGeher.SetzeNeuenWeg(pNowaDeska);
                    mainFrame.repaint();
                    return;
                }

                // Wenn Ausgang -> kein Inventar anzeigen, 2. Ausgang beachten, da nicht immer zu sehen !
                if (ausgangCychi.IsPointInRect(pTemp) ||
                        ausgangGang.IsPointInRect(pTemp) && mainFrame.Actions[517]) {
                    return;
                }

                // stara Deska mitnehmen
                if (staraDeska.IsPointInRect(pTemp)) {
                    nextActionID = 240;
                    mainFrame.wegGeher.SetzeNeuenWeg(pStaraDeska);
                    mainFrame.repaint();
                    return;
                }

                // blido mitnehmen
                if (blido.IsPointInRect(pTemp)) {
                    nextActionID = 250;
                    mainFrame.wegGeher.SetzeNeuenWeg(pBlido);
                    mainFrame.repaint();
                    return;
                }

                // korbik mitnehmen
                if (korbik.IsPointInRect(pTemp)) {
                    nextActionID = 260;
                    mainFrame.wegGeher.SetzeNeuenWeg(pKorbik);
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    rectSeil.IsPointInRect(pTemp) && !mainFrame.Actions[518] ||
                    kisty.IsPointInRect(pTemp) && !mainFrame.Actions[516] ||
                    kistyUnten.IsPointInRect(pTemp) && mainFrame.Actions[516] ||
                    nowaDeska.IsPointInRect(pTemp) && !mainFrame.Actions[517] ||
                    staraDeska.IsPointInRect(pTemp) || blido.IsPointInRect(pTemp) ||
                    korbik.IsPointInRect(pTemp);

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
            // Fenster und Seil, falls noch da und den ganzen Rest
            if (rectSeil.IsPointInRect(pTemp) && !mainFrame.Actions[518] ||
                    kisty.IsPointInRect(pTemp) && !mainFrame.Actions[516] ||
                    kistyUnten.IsPointInRect(pTemp) && mainFrame.Actions[516] ||
                    nowaDeska.IsPointInRect(pTemp) && !mainFrame.Actions[517] ||
                    staraDeska.IsPointInRect(pTemp) || blido.IsPointInRect(pTemp) ||
                    korbik.IsPointInRect(pTemp)) {
                if (Cursorform != 1) {
                    mainFrame.setCursor(mainFrame.Kreuz);
                    Cursorform = 1;
                }
                return;
            }

            if (ausgangCychi.IsPointInRect(pTemp)) {
                if (Cursorform != 12) {
                    mainFrame.setCursor(mainFrame.Cup);
                    Cursorform = 12;
                }
                return;
            }

            if (ausgangGang.IsPointInRect(pTemp) && mainFrame.Actions[517]) {
                if (Cursorform != 6) {
                    mainFrame.setCursor(mainFrame.Cdown);
                    Cursorform = 6;
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

    // Verschiebe die Kiste ("Wo hat Kollege Kiste hingestellt ??")
    private boolean MoveIt(GenericDrawingContext g) {
        // Position der Kisten veraendern
        xpos -= Xoffset;
        ypos -= Yoffset;

        // bisherigen Offset berechnen
        int xdiff = (int) (AnfangsPunkt.x - xpos);
        int ydiff = (int) (AnfangsPunkt.y - ypos);

        // System.out.println ("Offsets Feet : " + xdiff + " : " + ydiff);

        // eval. fuer Krabats Position berechnen
        festnagelPoint.x = bezugsPunkt.x - xdiff;
        festnagelPoint.y = bezugsPunkt.y - ydiff;

        // und auch setzen, fuer spaeter
        mainFrame.krabat.SetKrabatPos(festnagelPoint);

        g.drawImage(kista, (int) xpos, (int) ypos);
        g.drawImage(kista2, (int) xpos + AnfangsPunkt2.x - AnfangsPunkt.x, (int) ypos + AnfangsPunkt2.y - AnfangsPunkt.y);

        return !(Math.abs((int) xpos - EndPunkt.x) < Xoffset);
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
                // Seil ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00000"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00001"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00002"),
                        fSeil, 3, 0, 0);
                break;

            case 3:
                // Kiste oben ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00003"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00004"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00005"),
                        fKisteOben, 3, 0, 0);
                break;

            case 4:
                // neues Brett ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00006"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00007"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00008"),
                        fBrettNeu, 3, 0, 0);
                break;

            case 5:
                // stara Deska ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00009"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00010"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00011"),
                        fBrettAlt, 3, 0, 0);
                break;

            case 6:
                // Blido ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00012"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00013"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00014"),
                        fBlido, 3, 0, 0);
                break;

            case 7:
                // korbik ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00015"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00016"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00017"),
                        fKorbik, 3, 0, 0);
                break;

            case 8:
                // Kiste unten ansehen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00018"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00019"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00020"),
                        fKisteUnten, 3, 0, 0);
                break;

            case 20:
                // Seil mitnehmen (wenn noch da)
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fSeil);
                nextActionID = 21;
                // zu Inventar hinzufuegen
                mainFrame.inventory.vInventory.addElement(38);
                mainFrame.krabat.nAnimation = 31;
                Counter = 5;
                break;

            case 21:
                // auf Ende Anim warten und Seil verschwinden lassen
                if (--Counter == 1) {
                    mainFrame.Actions[518] = true;        // Flag setzen
                    mainFrame.Clipset = false;  // alles neu zeichnen
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 50:
                // Kiste verschieben, wenn Stollen gegessen, sonst Ausrede
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                festnagelPoint = new GenericPoint(mainFrame.krabat.GetKrabatPos().x, mainFrame.krabat.GetKrabatPos().y + 60);
                bezugsPunkt = new GenericPoint(festnagelPoint.x, festnagelPoint.y);
                SonderAnim = 1; // extra-Images fuer Kistenschieben
                if (mainFrame.Actions[680])  // Kisten tatsaechlich verschieben
                {
                    mainFrame.Actions[516] = true; // Kiste als verschoben markieren
                    verschiebeKiste = true;
                    nextActionID = 55;
                } else {
                    Counter = 20;
                    nextActionID = 65;
                }
                break;

            case 55:
                // Auf fertige Kiste warten
                if (!kistenSound) {
                    kistenSound = true;
                    mainFrame.wave.PlayFile("sfx-dd/kisty.wav");
                }
                if (!verschiebeKiste) {
                    nextActionID = 60;
                }
                break;

            case 60:
                // Anim beenden
                SonderAnim = 0;
                InitMatrix();
                mainFrame.krabat.SetKrabatPos(new GenericPoint(mainFrame.krabat.GetKrabatPos().x, mainFrame.krabat.GetKrabatPos().y - 40));
                mainFrame.krabat.SetFacing(6);
                mainFrame.Clipset = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 65:
                // erfolgloser Versuch, die Kisten zu verschieben
                if (--Counter > 1) {
                    break;
                }
                SonderAnim = 0;
                mainFrame.Clipset = false;
                if (!mainFrame.Actions[559] || !mainFrame.Actions[681]) {
                    // Noch keinen 1. Teil Stollen gegessen oder noch nie Kisten verschoben
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00021"),
                            Start.stringManager.getTranslation("Loc3_Zachod_00022"),
                            Start.stringManager.getTranslation("Loc3_Zachod_00023"),
                            fKisteOben, 3, 2, 70);
                    mainFrame.Actions[681] = true;
                } else {
                    // Noch keinen 2. Teil Stollen gegessen
                    KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00024"),
                            Start.stringManager.getTranslation("Loc3_Zachod_00025"),
                            Start.stringManager.getTranslation("Loc3_Zachod_00026"),
                            fKisteOben, 3, 2, 70);
                }
                break;

            case 70:
                // Ende dieser Anim
                // mainFrame.krabat.SetKrabatPos (new GenericPoint (mainFrame.krabat.GetKrabatPos().x, mainFrame.krabat.GetKrabatPos().y - 60));
                mainFrame.krabat.SetFacing(6);
                mainFrame.Clipset = false;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 80:
                // verschobene Kiste nochmal verschieben
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00027"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00028"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00029"),
                        fKisteUnten, 3, 0, 0);
                break;

            case 85:
                // Brett mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00030"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00031"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00032"),
                        fBrettNeu, 3, 0, 0);
                break;

            case 100:
                // Gehe zu Cychi
                NeuesBild(150, locationID);
                break;

            case 101:
                // Gehe zu Gang (unterirdisch)
                NeuesBild(152, locationID);
                break;

            case 150:
                // Kiste - Ausreden
                DingAusrede(fKisteOben);
                break;

            case 155:
                // Kisteu - Ausreden
                DingAusrede(fKisteUnten);
                break;

            case 160:
                // Seil
                DingAusrede(fSeil);
                break;

            case 165:
                // Brett - Ausreden
                DingAusrede(fBrettNeu);
                break;

            case 180:
                // Brett entfernen ansagen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00033"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00034"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00035"),
                        fBrettNeu, 3, 0, 185);
                break;

            case 185:
                // zu erstem Nagel laufen
                mainFrame.wegGeher.SetzeNeuenWeg(pEntnagel1);
                nextActionID = 187;
                break;

            case 187:
                // und raus damit
                mainFrame.krabat.nAnimation = 153;
                mainFrame.invCursor = false;
                nextActionID = 189;
                break;

            case 189:
                // und zum naechsten
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.wegGeher.SetzeNeuenWeg(pEntnagel2);
                nextActionID = 191;
                break;

            case 191:
                // und raus damit (Nr. 2)
                mainFrame.krabat.nAnimation = 153;
                nextActionID = 193;
                break;

            case 193:
                // und zum naechsten (Nr. 3)
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.wegGeher.SetzeNeuenWeg(pEntnagel3);
                nextActionID = 194;
                break;

            case 194:
                // und raus damit (Nr. 3)
                mainFrame.krabat.nAnimation = 153;
                nextActionID = 195;
                break;

            case 195:
                // und zum letzten (Nr. 4)
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.wegGeher.SetzeNeuenWeg(pEntnagel4);
                nextActionID = 196;
                break;

            case 196:
                // und raus damit (Nr. 4)
                mainFrame.krabat.nAnimation = 153;
                nextActionID = 197;
                break;

            case 197:
                // Brett reinschmeissen
                if (mainFrame.krabat.nAnimation != 0) {
                    break;
                }
                mainFrame.wegGeher.SetzeNeuenWeg(pNowaDeska);
                nextActionID = 198;
                break;

            case 198:
                // Brett entfernen
                mainFrame.Actions[517] = true;
                InitMatrix();
                mainFrame.krabat.SetFacing(fBrettNeu);
                mainFrame.krabat.nAnimation = 92;
                mainFrame.wave.PlayFile("sfx-dd/deska.wav");
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                break;

            case 200:
                // Hlebija auf Deska
                // ZuffZahl 0...1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00036"),
                                Start.stringManager.getTranslation("Loc3_Zachod_00037"),
                                Start.stringManager.getTranslation("Loc3_Zachod_00038"),
                                fBrettNeu, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00039"),
                                Start.stringManager.getTranslation("Loc3_Zachod_00040"),
                                Start.stringManager.getTranslation("Loc3_Zachod_00041"),
                                fBrettNeu, 3, 0, 0);
                        break;
                }
                break;

            case 210:
                // stara Deska - Ausreden
                DingAusrede(fBrettAlt);
                break;

            case 220:
                // blido - Ausreden
                DingAusrede(fBlido);
                break;

            case 230:
                // korbik - Ausreden
                DingAusrede(fKorbik);
                break;

            case 240:
                // stara Deska mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00042"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00043"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00044"),
                        fBrettAlt, 3, 0, 0);
                break;

            case 250:
                // Blido mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00045"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00046"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00047"),
                        fBlido, 3, 0, 0);
                break;

            case 260:
                // korbik mitnehmen
                KrabatSagt(Start.stringManager.getTranslation("Loc3_Zachod_00048"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00049"),
                        Start.stringManager.getTranslation("Loc3_Zachod_00050"),
                        fKorbik, 3, 0, 0);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}