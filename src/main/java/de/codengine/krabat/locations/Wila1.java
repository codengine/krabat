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
import de.codengine.krabat.anims.Plokarka;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Wila1 extends Mainloc {
    private static final Logger log = LoggerFactory.getLogger(Wila1.class);
    private GenericImage background;
    private GenericImage foreground;
    private GenericImage leineleer;
    private GenericImage clean;
    private GenericImage dirty;
    private GenericImage stom;
    private GenericImage dooropen;
    private GenericImage vdoor;
    private GenericImage stange;
    private GenericImage onelost;
    private GenericImage twolost;
    private GenericImage threelost;
    private final GenericImage[] krabat_waesche;

    private Plokarka waschfrau;

    private boolean showPlokarka = false; // Ist sie ueberhaupt zu sehen ???
    private boolean walkReady = true; // Flag, ob sie denn schon fertiggelaufen ist

    private boolean hide = false; // versteckt sich Krabat hinter Baum ?
    private boolean waschClip = false; // muss die Waschfrau ueberdeckt werden ?
    private boolean isAbnehming = false; // nimmt sie gerade waesche ab ??

    private int waescheFehlt;

    private int SonderAnim = 0;
    private int AnimCounter = 0;
    private int AnimPosition = 0;

    // Konstanten - Rects
    private static final Borderrect obererAusgang = new Borderrect(378, 90, 464, 144);
    private static final Borderrect untererAusgang = new Borderrect(102, 437, 400, 479);
    private static final Borderrect rechterAusgang = new Borderrect(600, 306, 639, 479);
    private static final Borderrect kleiderRect = new Borderrect(375, 235, 441, 280);
    private static final Borderrect leineRect = new Borderrect(375, 240, 441, 265);
    private static final Borderrect durjeRect = new Borderrect(300, 347, 328, 411);
    private static final Borderrect dachRect = new Borderrect(355, 282, 421, 351); // fuer Vordergrund
    private static final Borderrect stangeRect = new Borderrect(423, 261, 476, 372); // fuer Vordergrund

    // Konstante Points
    private static final GenericPoint Pdown = new GenericPoint(208, 479);
    private static final GenericPoint Pright = new GenericPoint(639, 411);
    private static final GenericPoint Pup = new GenericPoint(428, 145);
    private static final GenericPoint Pleine = new GenericPoint(418, 298);
    private static final GenericPoint Pkleider = new GenericPoint(409, 293);
    private static final GenericPoint pVorBaum = new GenericPoint(461, 177);
    private static final GenericPoint pBaum = new GenericPoint(495, 176);
    private static final GenericPoint Pdurje = new GenericPoint(312, 433);
    private static final GenericPoint pVollspritz = new GenericPoint(454, 299);

    private static final GenericPoint waschLook = new GenericPoint(384, 423);
    private static final GenericPoint waschInDoor = new GenericPoint(313, 418);
    private static final GenericPoint waschVorDoor = new GenericPoint(325, 423);

    // Konstante ints
    private static final int fSaty = 12;
    private static final int fLajna = 12;
    private static final int fDurje = 12;
    private static final int fVollspritz = 9;

    private int Counter = 0;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Wila1(Start caller, int oldLocation) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        mainFrame.krabat.maxx = 463;
        mainFrame.krabat.zoomf = 5.3f;
        mainFrame.krabat.defScale = 10;

        waschfrau = new Plokarka(mainFrame);
        waschfrau.maxx = 400;
        waschfrau.zoomf = 4f;
        waschfrau.defScale = 0;

        if (!mainFrame.Actions[175]) {
            waescheFehlt = 0;
        }

        krabat_waesche = new GenericImage[2];

        InitLocation(oldLocation);
        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        // Grenzen setzen
        mainFrame.wegGeher.vBorders.removeAllElements();
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(166, 458, 639, 479));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(352, 639, 166, 639, 428, 457));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(536, 639, 464, 639, 373, 427));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(550, 639, 536, 639, 333, 372));
        // mainFrame.wegGeher.vBorders.addElement (new bordertrapez (488, 494, 550, 625, 249, 332));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(488, 494, 519, 560, 249, 297));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(465, 298, 567, 307));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(523, 567, 550, 625, 308, 332));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(390, 298, 464, 307));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(460, 464, 488, 494, 178, 248));
        mainFrame.wegGeher.vBorders.addElement(new Bordertrapez(435, 437, 460, 464, 145, 177));

        // Matrix loeschen
        mainFrame.wegSucher.ClearMatrix(10);

        // moegliche Wege eintragen (Positionen (= Rechtecke) verbinden)
        mainFrame.wegSucher.PosVerbinden(0, 1);
        mainFrame.wegSucher.PosVerbinden(1, 2);
        mainFrame.wegSucher.PosVerbinden(2, 3);
        mainFrame.wegSucher.PosVerbinden(3, 6);
        mainFrame.wegSucher.PosVerbinden(6, 5);
        mainFrame.wegSucher.PosVerbinden(4, 5);
        mainFrame.wegSucher.PosVerbinden(5, 7);
        mainFrame.wegSucher.PosVerbinden(4, 8);
        mainFrame.wegSucher.PosVerbinden(8, 9);

        InitImages();
        switch (oldLocation) {
            case 0:
                // Einsprung fuer Load
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                break;
            case 15:
                // von Njedz aus
                if (mainFrame.komme_von_karte) {
                    mainFrame.komme_von_karte = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(293, 465));
                mainFrame.krabat.SetFacing(12);
                break;
            case 17:
                // von Kolmc aus
                if (mainFrame.komme_von_karte) {
                    mainFrame.komme_von_karte = false;
                    BackgroundMusicPlayer.getInstance().playTrack(26, true);
                }
                mainFrame.krabat.setPos(new GenericPoint(436, 147));
                mainFrame.krabat.SetFacing(6);
                break;
            case 18:
                // von Dubring aus
                BackgroundMusicPlayer.getInstance().playTrack(26, true);
                mainFrame.krabat.setPos(new GenericPoint(620, 404));
                mainFrame.krabat.SetFacing(9);
                break;
        }
        mainFrame.komme_von_karte = false;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/villa/villa.gif");
        foreground = getPicture("gfx/villa/villa2.gif");
        leineleer = getPicture("gfx/villa/villa3.gif");
        clean = getPicture("gfx/villa/villa4.gif");
        dirty = getPicture("gfx/villa/villa5.gif");

        stom = getPicture("gfx/villa/stom.gif");
        vdoor = getPicture("gfx/villa/durje-1.gif");
        dooropen = getPicture("gfx/villa/v-durje.gif");
        stange = getPicture("gfx/villa/villa6.gif");

        onelost = getPicture("gfx/villa/vw2.gif");
        twolost = getPicture("gfx/villa/vw1.gif");
        threelost = getPicture("gfx/villa/vw3.gif");

        krabat_waesche[0] = getPicture("gfx/villa/k-l-bloto1.gif");
        krabat_waesche[1] = getPicture("gfx/villa/k-l-bloto2.gif");

    }

    @Override
    public void cleanup() {
        background = null;
        foreground = null;
        leineleer = null;
        clean = null;
        dirty = null;

        stom = null;
        vdoor = null;
        dooropen = null;
        stange = null;

        onelost = null;
        twolost = null;
        threelost = null;

        krabat_waesche[0] = null;
        krabat_waesche[1] = null;

        waschfrau.cleanup();
        waschfrau = null;
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

        // Waschfrau Hintergrund loeschen
        if (showPlokarka) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = waschfrau.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne Hintergrund neu
            g.drawImage(background, 0, 0);
        }

        // Hier Entscheidung, was wann gezeichnet wird

        // Offene Tuer  zeichnen
        if (showPlokarka) {
            g.setClip(294, 340, 52, 82);
            g.drawImage(dooropen, 294, 340);
        }

        // nur, wenn Leine nicht aufgehoben, muss gemalt werden
        if (!mainFrame.Actions[906]) {
            if (!mainFrame.Actions[177]) {
                // Waesche haengt normal da
                g.setClip(370, 228, 83, 78);
                g.drawImage(clean, 370, 228);
            } else {
                if (!mainFrame.Actions[175]) {
                    g.setClip(370, 227, 84, 80);
                    switch (waescheFehlt) {
                        case 0:
                            // schmutzige Waesche zeichnen, alle Stuecke dran
                            g.drawImage(dirty, 370, 228);
                            break;

                        case 1:
                            // 1. Stueck weg
                            g.drawImage(onelost, 370, 227);
                            break;

                        case 2:
                            // 2. Stueck weg
                            g.drawImage(twolost, 370, 227);
                            break;

                        case 3:
                            // 3. Stueck weg
                            g.drawImage(threelost, 370, 227);
                            break;

                        default:
                            log.error("Fehler im LeineChooser! waescheFehlt = {}", waescheFehlt);
                            break;
                    }
                } else {
                    // leere Leine zeichnen
                    g.setClip(370, 227, 83, 78);
                    g.drawImage(leineleer, 370, 227);
                }
            }
        }

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.wegGeher.vBorders);
        }

        // Waschfrau bewegen
        if (showPlokarka && !walkReady) {
            // Waschfrau um 1 Schritt weiterbewegen (nur virtuell)
            walkReady = waschfrau.Move();
        }

        // Waschfrau zeichnen
        if (showPlokarka) {
            // Clipping - Rectangle feststellen und setzen
            Borderrect temp = waschfrau.getRect();
            g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                    temp.ru_point.y - temp.lo_point.y + 20);

            // Zeichne sie jetzt

            // Redet sie etwa gerade ??
            if ((TalkPerson == 27 || TalkPerson == 55) && mainFrame.talkCount > 0) {
                if (TalkPerson == 27) {
                    waschfrau.talkPlokarka(g);
                }
                if (TalkPerson == 55) {
                    waschfrau.haendePlokarka(g);
                }
            }

            // nur rumstehen oder laufen
            else {
                if (isAbnehming) {
                    isAbnehming = waschfrau.nimmWaescheAb(g);
                    if (!isAbnehming) {
                        waescheFehlt++;
                    }
                } else {
                    waschfrau.drawPlokarka(g);
                }
            }

            // Tuer - Vordergrund ins Clipping - Rect zeichnen
            g.drawImage(vdoor, 285, 330);
        }

        // Hier Vordergrund fuer Waschfrau zeichnen
        if (waschClip) {
            g.drawImage(foreground, 340, 292);
        }

        mainFrame.wegGeher.GeheWeg();

        // Krabat zeichnen

        if (SonderAnim != 0) {
            // Sonderanims ausfuehren
            if (SonderAnim == 1) {
                // Waesche beschmutzen

                // Groesse
                int scale = mainFrame.krabat.defScale;
                scale += (int) (((float) mainFrame.krabat.maxx - (float) mainFrame.krabat.getPos().y) / mainFrame.krabat.zoomf);

                // Hoehe: nur offset
                int hoch = 100 - scale;

                // Breite
                int weit = 50 - scale / 2;

                // Punkt fuer LO-Evaluierung bereitstellen
                GenericPoint hier = mainFrame.krabat.getPos();

                hier.x -= (int) ((float) weit * 0.7f);
                hier.y -= hoch;

                // Cliprect setzen
                g.setClip(hier.x, hier.y, weit + 1, hoch + 1);

                // GenericImage weiterschalten
                if (++AnimCounter % 4 == 0) {
                    if (AnimPosition == 1) {
                        AnimPosition = 0;
                    } else {
                        AnimPosition = 1;
                    }
                }

                // Krabat zeichnen
                g.drawImage(krabat_waesche[AnimPosition], hier.x, hier.y, weit, hoch);

                // eal. Ob die Anim zu Ende ist
                if (AnimCounter > 8) {
                    SonderAnim = 0;
                }
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

        // Ab hier muss Cliprect wieder gerettet werden
        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        GenericPoint pKrTemp = mainFrame.krabat.getPos();

        // Hier Vordergruende waehrend der Anim zeichnen
        if (hide) {
            g.drawImage(stom, 485, 143);
        }

        // Hier Krabat - Vordergruende zeichnen
        if (dachRect.IsPointInRect(pKrTemp)) {
            g.drawImage(vdoor, 285, 330);
        }

        // Hier Krabat - Vordergruende zeichnen
        if (stangeRect.IsPointInRect(pKrTemp)) {
            g.drawImage(stange, 444, 273);
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

                // Ausreden fuer Leine
                if (leineRect.IsPointInRect(pTemp) && !mainFrame.Actions[906] &&
                        mainFrame.Actions[175]) {
                    nextActionID = 260;
                    pTemp = Pleine;
                }

                // Ausreden fuer Kleider
                if (kleiderRect.IsPointInRect(pTemp) && !mainFrame.Actions[175]) {
                    if (mainFrame.whatItem == 16) { // Honck z blotom
                        nextActionID = 155;
                        pTemp = pVollspritz;
                    } else {
                        nextActionID = 150;
                        pTemp = Pkleider;
                    }
                }

                // Ausreden fuer Tuer
                if (durjeRect.IsPointInRect(pTemp)) {
                    // kamuski
                    nextActionID = mainFrame.whatItem == 12 ? 250 : 270;
                    pTemp = Pdurje;
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

                // zu Njedz gehen ?
                if (untererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 100;
                    GenericPoint kt = mainFrame.krabat.getPos();

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

                // zu Dubring gehen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 102;
                    GenericPoint kt = mainFrame.krabat.getPos();

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

                // zu Kolmc gehen
                if (obererAusgang.IsPointInRect(pTemp)) {
                    nextActionID = 101;
                    GenericPoint kt = mainFrame.krabat.getPos();

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

                // Leine ansehen
                if (leineRect.IsPointInRect(pTemp) && !mainFrame.Actions[906] &&
                        mainFrame.Actions[175]) {
                    nextActionID = 2;
                    pTemp = Pleine;
                }

                // Kleider ansehen
                if (kleiderRect.IsPointInRect(pTemp) && !mainFrame.Actions[175]) {
                    nextActionID = 1;
                    pTemp = Pkleider;
                }

                // Tuer ansehen
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 3;
                    pTemp = Pdurje;
                }

                mainFrame.wegGeher.SetzeNeuenWeg(pTemp);
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

                // Dubring anschauen
                if (rechterAusgang.IsPointInRect(pTemp)) {
                    return;
                }

                // Leine benutzen ?
                if (leineRect.IsPointInRect(pTemp) && !mainFrame.Actions[906] &&
                        mainFrame.Actions[175]) {
                    nextActionID = 50;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pleine);
                    mainFrame.repaint();
                    return;
                }

                // Kleider benutzen ?
                if (kleiderRect.IsPointInRect(pTemp) && !mainFrame.Actions[175]) {
                    nextActionID = 53;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pkleider);
                    mainFrame.repaint();
                    return;
                }

                // Tuer benutzen ?
                if (durjeRect.IsPointInRect(pTemp)) {
                    nextActionID = 280;
                    mainFrame.wegGeher.SetzeNeuenWeg(Pdurje);
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
            mainFrame.invHighCursor = tmp.IsPointInRect(pTemp) ||
                    leineRect.IsPointInRect(pTemp) && !mainFrame.Actions[906] && mainFrame.Actions[175] ||
                    kleiderRect.IsPointInRect(pTemp) && !mainFrame.Actions[175] ||
                    durjeRect.IsPointInRect(pTemp);

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
            if (leineRect.IsPointInRect(pTemp) && !mainFrame.Actions[906] && mainFrame.Actions[175] ||
                    kleiderRect.IsPointInRect(pTemp) && !mainFrame.Actions[175] ||
                    durjeRect.IsPointInRect(pTemp)) {
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
                // Kleider anschauen
                KrabatSagt("Wila1_1", fSaty, 3, 0, 0);
                break;

            case 2:
                // Leine anschauen
                // Zufallszahl zwischen 0 und 1
                int zuffZahl = (int) (Math.random() * 1.9);
                switch (zuffZahl) {
                    case 0:
                        KrabatSagt("Wila1_2", fLajna, 3, 0, 0);
                        break;

                    case 1:
                        KrabatSagt("Wila1_3", fLajna, 3, 0, 0);
                        break;
                }
                break;

            case 3:
                // Tuer anschauen
                KrabatSagt("Wila1_4", fDurje, 3, 0, 0);
                break;

            case 50:
                // Leine mitnehmen ??
                mainFrame.fPlayAnim = true;
                mainFrame.krabat.SetFacing(fLajna);
                mainFrame.krabat.nAnimation = 120;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 51;
                Counter = 5;
                break;

            case 51:
                // Ende Leine nehmen
                if (--Counter == 1) {
                    mainFrame.inventory.vInventory.addElement(6);
                    mainFrame.Clipset = false;
                    mainFrame.Actions[906] = true;
                }
                if (mainFrame.krabat.nAnimation != 0 || Counter > 0) {
                    break;
                }
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                nextActionID = 0;
                mainFrame.repaint();
                break;

            case 53:
                // Kleider mitnehmen ??
                // Waschfrau muss erscheinen, vorher Versuch mitnehmen
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fLajna); // Krabat schaut auf Leine
                mainFrame.krabat.nAnimation = 121;
                Counter = 3;
                nextActionID = 54;
                break;

            case 54:
                // jetzt die Frau her
                if (--Counter > 0) {
                    break;
                }
                mainFrame.wave.PlayFile("sfx/vdurjeauf.wav");
                showPlokarka = true;
                waschfrau.setPos(waschInDoor);
                waschfrau.SetFacing(3);
                nextActionID = 55;
                break;

            case 55:
                // Waschfrau loslaufen lassen nach unten
                waschfrau.MoveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 58;
                break;

            case 58:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 60;
                }
                break;

            case 60:
                // Waschfrau loslaufen lassen zur Anguckposition
                waschfrau.MoveTo(waschLook);
                walkReady = false;
                nextActionID = 62;
                break;

            case 62:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 65;
                }
                break;

            case 65:
                // Sie sagt ihren Spruch
                mainFrame.krabat.SetFacing(6);
                if (!mainFrame.Actions[176]) {
                    outputText = mainFrame.ifont.TeileTextKey("Wila1_8");
                    mainFrame.Actions[176] = true;
                } else {
                    outputText = mainFrame.ifont.TeileTextKey("Wila1_9");
                }
                waschfrau.SetFacing(12);
                // Hier Position des Textes berechnen
                Borderrect temp = waschfrau.getRect();
                GenericPoint tTalk = new GenericPoint((temp.ru_point.x + temp.lo_point.x) / 2, temp.lo_point.y - 50);
                outputTextPos = mainFrame.ifont.CenterText(outputText, tTalk);
                TalkPerson = 27;
                nextActionID = 70;
                break;

            case 70:
                // Waschfrau loslaufen lassen nach links
                waschfrau.MoveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 72;
                break;

            case 72:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 75;
                }
                break;

            case 75:
                // Waschfrau loslaufen lassen nach Oben
                waschfrau.MoveTo(waschInDoor);
                walkReady = false;
                nextActionID = 78;
                break;

            case 78:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 80;
                }
                break;

            case 80:
                // Waschfrau wieder verschwinden lassen
                showPlokarka = false;
                mainFrame.wave.PlayFile("sfx/vdurjezu.wav");
                mainFrame.Clipset = false;
                nextActionID = 0;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 100:
                // Gehe zu Njedz
                NeuesBild(15, 16);
                break;

            case 101:
                // gehe zu Kolmc 
                NeuesBild(17, 16);
                break;

            case 102:
                // nach Dubring gehen
                NeuesBild(18, 16);
                break;

            case 150:
                // Kleider - Ausreden
                DingAusrede(fLajna);
                break;

            // Anim : Waesche vollspritzen und verstecken ////////////////////////////

            case 155:
                // Waesche mit Schlamm vollspritzen
                mainFrame.invCursor = false;
                mainFrame.fPlayAnim = true;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.krabat.SetFacing(fVollspritz);
                SonderAnim = 1;
                nextActionID = 160;
                break;

            case 160:
                // Krabat sagt ich verdufte
                if (AnimCounter == 6) {
                    // hier die Waesche schmutzig machen
                    mainFrame.Actions[177] = true;
                    mainFrame.Clipset = false;
                    mainFrame.wave.PlayFile("sfx/bloto2.wav");
                }
                if (mainFrame.krabat.nAnimation != 0 || SonderAnim != 0) {
                    break;
                }
                KrabatSagt("Wila1_5", 0, 3, 0, 165);
                break;

            case 165:
                // Krabat versteckt sich
                hide = true;
                mainFrame.inventory.vInventory.addElement(4);
                mainFrame.inventory.vInventory.removeElement(16); // honck wieder leer machen
                mainFrame.wegGeher.SetzeNeuenWeg(pVorBaum);
                nextActionID = 168;
                break;

            case 168:
                // hinter Baum gehen
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pBaum);
                nextActionID = 170;
                break;

            case 170:
                // Noch richtigrumdrehen
                mainFrame.krabat.SetFacing(6);
                nextActionID = 175;
                break;

            case 175:
                // Waschfrau muss erscheinen
                showPlokarka = true;
                mainFrame.wave.PlayFile("sfx/vdurjeauf.wav");
                waschfrau.setPos(waschInDoor);
                waschfrau.SetFacing(6);
                nextActionID = 180;
                break;

            case 180:
                // Waschfrau loslaufen lassen nach unten
                waschfrau.MoveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 185;
                break;

            case 185:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 186;
                }
                break;

            case 186:
                // Waschfrau loslaufen lassen zur Anguckposition
                waschfrau.MoveTo(waschLook);
                walkReady = false;
                nextActionID = 187;
                break;

            case 187:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 190;
                }
                break;

            case 190:
                // Sie sagt ihren Spruch
                outputText = mainFrame.ifont.TeileTextKey("Wila1_10");
                // Hier Position des Textes berechnen
                Borderrect tmp = waschfrau.getRect();
                GenericPoint tTlk = new GenericPoint((tmp.ru_point.x + tmp.lo_point.x) / 2, tmp.lo_point.y - 50);
                outputTextPos = mainFrame.ifont.CenterText(outputText, tTlk);
                waschfrau.SetFacing(12);
                TalkPerson = 55;
                nextActionID = 191;
                break;

            case 191:
                // Waschfrau loslaufen lassen, soll 1. Stueck abnehmen
                waschClip = true;
                waschfrau.MoveTo(new GenericPoint(426, 304));
                walkReady = false;
                nextActionID = 192;
                break;

            case 192:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 193;
                }
                break;

            case 193:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 194;
                break;

            case 194:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    nextActionID = 195;
                }
                break;

            case 195:
                // Waschfrau loslaufen lassen, soll 2. Stueck abnehmen
                waschfrau.hasWaesche = true;
                waschfrau.MoveTo(new GenericPoint(410, 313));
                walkReady = false;
                nextActionID = 196;
                break;

            case 196:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 197;
                }
                break;

            case 197:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 198;
                break;

            case 198:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    nextActionID = 199;
                }
                break;

            case 199:
                // Waschfrau loslaufen lassen, soll 3. Stueck abnehmen
                waschfrau.MoveTo(new GenericPoint(389, 315));
                walkReady = false;
                nextActionID = 200;
                break;

            case 200:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 201;
                }
                break;

            case 201:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 202;
                break;

            case 202:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    nextActionID = 203;
                }
                break;

            case 203:
                // Waschfrau loslaufen lassen, soll 4. Stueck abnehmen
                waschfrau.MoveTo(new GenericPoint(371, 315));
                walkReady = false;
                nextActionID = 204;
                break;

            case 204:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 205;
                }
                break;

            case 205:
                // richtigrum hinstellen (oder besser: nimmWaescheAbAnim)
                isAbnehming = true;
                nextActionID = 206;
                break;

            case 206:
                // warten auf Ende abnehmen
                if (!isAbnehming) {
                    mainFrame.Actions[175] = true;
                    nextActionID = 207;
                }
                break;

            case 207:
                // Waesche ist abgenommen, kennzeichnen
                mainFrame.Clipset = false;
                nextActionID = 210;
                break;

            case 210:
                // Waschfrau loslaufen lassen zurueck zur Anguckposition
                waschClip = true;
                waschfrau.MoveTo(waschLook);
                walkReady = false;
                nextActionID = 215;
                break;

            case 215:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 220;
                }
                break;

            case 220:
                // Waschfrau loslaufen lassen anch links
                waschClip = false;
                waschfrau.MoveTo(waschVorDoor);
                walkReady = false;
                nextActionID = 225;
                break;

            case 225:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 226;
                }
                break;

            case 226:
                // Waschfrau loslaufen lassen nach oben
                waschfrau.MoveTo(waschInDoor);
                walkReady = false;
                nextActionID = 227;
                break;

            case 227:
                // Warten, bis sie ausgelaufen ist
                if (walkReady) {
                    nextActionID = 230;
                }
                break;

            case 230:
                // Waschfrau wieder verschwinden lassen
                showPlokarka = false;
                mainFrame.wave.PlayFile("sfx/vdurjezu.wav");
                mainFrame.Clipset = false;
                nextActionID = 235;
                break;

            case 235:
                // Krabat kommt hinter Versteck wieder vor
                mainFrame.wegGeher.SetzeGarantiertNeuenWeg(pVorBaum);
                nextActionID = 240;
                break;

            case 240:
                // Anim beenden
                hide = false;
                mainFrame.Clipset = false;
                nextActionID = 0;
                mainFrame.fPlayAnim = false;
                evalMouseMoveEvent(mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            case 250:
                // Tuer anschauen
                KrabatSagt("Wila1_6", fDurje, 3, 0, 0);
                break;

            case 260:
                // lajna - Ausreden
                DingAusrede(fLajna);
                break;

            case 270:
                // Durje - Ausreden
                DingAusrede(fDurje);
                break;

            case 280:
                // Tuer mitnehmen
                KrabatSagt("Wila1_7", fDurje, 3, 0, 0);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }
}