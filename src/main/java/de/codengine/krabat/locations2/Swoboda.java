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
import de.codengine.krabat.anims.Boom;
import de.codengine.krabat.anims.Miller;
import de.codengine.krabat.anims.Mother;
import de.codengine.krabat.anims.RavenFreedom;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Swoboda extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Swoboda.class);
    private GenericImage background;
    private Mother mutter;
    private Miller mueller;
    private RavenFreedom[] raben;
    private GenericPoint[] rapakpos;

    private boolean setAnim = false;
    private boolean krabatHere = false;
    private boolean rapakiAchtung = false;
    private boolean mlynkHatStock = false;

    private static final int KRABATRABE = 3;

    private static final int[] Rarray = {0, 275, 278, 281, 283, 286, 288, 289, 291, 292, 293, 294, 294};

    private static final GenericPoint Pmutter = new GenericPoint(154, 479);
    private static final GenericPoint Pmueller = new GenericPoint(453, 467);

    private GenericPoint mutterPoint;
    private static final GenericPoint krabatPoint = new GenericPoint(300, 471);

    private GenericPoint mutterTalk;

    private Boom krabatmorph;
    private int krabatmorphcount = 0;
    private boolean iskrabatmorphing = false;

    private Boom rabemorph;
    private int rabemorphcount = 0;
    private boolean israbemorphing = false;

    private boolean rabeVisible = true;

    private int Counter = 0;

    public Swoboda(Start caller, int oldLocation) {
        super(caller);
        mainFrame.freeze(true);

        mainFrame.checkKrabat();

        // mainFrame.player.Play ("12", 174000);  // kommt schon durch Mlyn2 rein

        mainFrame.krabat.maxx = 200;
        mainFrame.krabat.zoomf = 4.93f;
        mainFrame.krabat.defScale = -150;

        krabatmorph = new Boom(mainFrame);
        rabemorph = new Boom(mainFrame);

        InitLocation();
        InitImages();
        Cursorform = 200;  // Sinnloser Wert, damit garantiert neuer Cursor gesetzt wird
        if (oldLocation == 71) { // Aus Doma kommend
            mainFrame.krabat.setPos(krabatPoint);
            mainFrame.krabat.SetFacing(9);
        }

        setAnim = true;

        mainFrame.freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation() {
        mutter = new Mother(mainFrame, false);
        mueller = new Miller(mainFrame);

        mueller.maxx = 467;
        mueller.zoomf = 4f;
        mueller.defScale = -120;

        mueller.setPos(Pmueller);
        mueller.SetFacing(9);

        mutterPoint = new GenericPoint();
        mutterPoint.x = Pmutter.x - mutter.Breites / 2;
        mutterPoint.y = Pmutter.y - mutter.Hoehes;

        mutterTalk = new GenericPoint();
        mutterTalk.x = Pmutter.x;
        mutterTalk.y = mutterPoint.y - 50;

        raben = new RavenFreedom[13];
        raben[1] = new RavenFreedom(mainFrame);
        raben[2] = new RavenFreedom(mainFrame);
        raben[3] = new RavenFreedom(mainFrame);
        raben[4] = new RavenFreedom(mainFrame);
        raben[5] = new RavenFreedom(mainFrame);
        raben[6] = new RavenFreedom(mainFrame);
        raben[7] = new RavenFreedom(mainFrame);
        raben[8] = new RavenFreedom(mainFrame);
        raben[9] = new RavenFreedom(mainFrame);
        raben[10] = new RavenFreedom(mainFrame);
        raben[11] = new RavenFreedom(mainFrame);
        raben[12] = new RavenFreedom(mainFrame);

        rapakpos = new GenericPoint[13];
        for (int i = 1; i <= 12; i++) {
            // rapakpos[i] = new GenericPoint (((i - 1) * 23) + 142, ((i - 1) * 2) + 231);
            rapakpos[i] = new GenericPoint((i - 1) * 23 + 142, Rarray[i] - RavenFreedom.Hoehe + 3);
        }

        // Hier bekommt Krabat seine Inventargegenstaende wieder
        mainFrame.inventory.vInventory.addElement(12);
        mainFrame.inventory.vInventory.addElement(20);

        // Hier Zeichen an Karte, dass Teil 2 begonnen hat
        mainFrame.actions[305] = true;
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mlyn/mlynn-r.png");

    }

    @Override
    public void cleanup() {
        background = null;

        mutter.cleanup();
        mutter = null;
        mueller.cleanup();
        mueller = null;

        for (int i = 1; i <= 12; i++) {
            raben[i].cleanup();
            raben[i] = null;
        }

        krabatmorph.cleanup();
        krabatmorph = null;
        rabemorph.cleanup();
        rabemorph = null;
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // System.out.print("g");

        // Clipping - Region initialisieren und Rauchthread aktivieren
        if (!mainFrame.isClipSet) {
            mainFrame.scrollX = 0;
            mainFrame.scrollY = 0;
            mainFrame.isClipSet = true;
            Cursorform = 200;
            g.setClip(0, 0, 1284, 964);
            mainFrame.isBackgroundAnimRunning = true;
            mainFrame.isAnimRunning = true;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // Hintergrund zeichnen (Krabat loeschen bzw. voellig neu zeichnen)
        g.drawImage(background, 0, 0);

        // Debugging - Zeichnen der Laufrechtecke
        if (Debug.enabled) {
            Debug.DrawRect(g, mainFrame.pathWalker.vBorders);
        }

        // wenn Krabat morpht, dann diesen Hintergrund loeschen
        if (iskrabatmorphing) {
            g.setClip(krabatmorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        // wenn Rabe morpht, dann diesen Hintergrund loeschen
        if (israbemorphing) {
            g.setClip(rabemorph.bummRect());
            g.drawImage(background, 0, 0);
        }

        // alle Hintergruende loeschen wegen Ueberdecken
        // Mutter
        g.setClip(mutterPoint.x, mutterPoint.y, mutter.Breites, mutter.Hoehes);
        g.drawImage(background, 0, 0);

        // Mueller Hintergrund loeschen
        // Clipping - Rectangle feststellen und setzen
        BorderRect temp;
        if (!mlynkHatStock) {
            temp = mueller.getRect();
        } else {
            temp = mueller.MlynkRectMitStock();
        }
        g.setClip(temp.lo_point.x - 10, temp.lo_point.y - 10, temp.ru_point.x - temp.lo_point.x + 20,
                temp.ru_point.y - temp.lo_point.y + 20);

        // Zeichne Hintergrund neu
        g.drawImage(background, 0, 0);

        // Raben
        for (int i = 1; i <= 12; i++) {
            g.setClip(rapakpos[i].x, rapakpos[i].y, RavenFreedom.Breite, RavenFreedom.Hoehe);
            g.drawImage(background, 0, 0);
        }

        // Raben zeichnen
        if (!rapakiAchtung) {
            for (int i = 1; i <= 12; i++) {
                if (i == KRABATRABE && !rabeVisible) {
                    continue;
                }
                g.setClip(rapakpos[i].x, rapakpos[i].y, RavenFreedom.Breite, RavenFreedom.Hoehe);
                raben[i].drawRapak(g, rapakpos[i]);
            }
        } else {
            for (int i = 1; i <= 12; i++) {
                if (i == KRABATRABE && !rabeVisible) {
                    continue;
                }
                g.setClip(rapakpos[i].x, rapakpos[i].y, RavenFreedom.Breite, RavenFreedom.Hoehe);
                if (i != KRABATRABE) {
                    raben[i].KratzeLinks(g, rapakpos[i]);
                } else {
                    raben[i].KratzeRechts(g, rapakpos[i]);
                }
            }
        }

        // morphenden Raben zeichnen
        if (israbemorphing) {
            g.setClip(rabemorph.bummRect());
            rabemorphcount = rabemorph.drawBumm(g);
        }

        // Mac zeichnen bei Reden und Herumstehen,
        g.setClip(mutterPoint.x, mutterPoint.y, mutter.Breites, mutter.Hoehes);
        mutter.drawMac(g, mutterPoint, TalkPerson);

        // Mueller zeichnen
        // Clipping - Rectangle feststellen und setzen
        BorderRect tmp;
        if (!mlynkHatStock) {
            tmp = mueller.getRect();
        } else {
            tmp = mueller.MlynkRectMitStock();
        }
        g.setClip(tmp.lo_point.x - 10, tmp.lo_point.y - 10, tmp.ru_point.x - tmp.lo_point.x + 20,
                tmp.ru_point.y - tmp.lo_point.y + 20);

        // Zeichne ihn jetzt

        // Redet er etwa gerade ??
        if (TalkPerson == 36 && mainFrame.talkCount > 0) {
            if (!mlynkHatStock) {
                mueller.talkMlynk(g);
            } else {
                mueller.talkMlynkWithKij(g);
            }
        }

        // nur rumstehen oder laufen
        else {
            if (!mlynkHatStock) {
                mueller.drawMlynk(g);
            } else {
                mueller.drawMlynkWithKij(g);
            }
        }

        // Krabats neue Position festlegen wenn noetig
        mainFrame.pathWalker.GeheWeg();

        // Krabat zeichnen

        // Animation??
        if (krabatHere) {
            if (mainFrame.krabat.nAnimation != 0) {
                mainFrame.krabat.DoAnimation(g);

                // Cursorruecksetzung nach Animationsende
                if (mainFrame.krabat.nAnimation == 0) {
                    evalMouseMoveEvent(mainFrame.mousePoint);
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

        if (iskrabatmorphing) {
            g.setClip(krabatmorph.bummRect());
            krabatmorphcount = krabatmorph.drawBumm(g);
        }

        // Ab hier muss Cliprect wieder gerettet werden
        // Steht Krabat hinter einem Gegenstand ? Koordinaten noch mal checken !!!
        // GenericPoint pKrTemp = mainFrame.krabat.GetKrabatPos ();

        // Textausgabe, falls noetig
        if (!Objects.equals(outputText, "")) {
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 1284, 964);
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Gibt es was zu tun , Achtung: Scrolling wird in jeder DoAction einzeln kontrolliert!!!

        if (setAnim) {
            setAnim = false;
            nextActionID = 100;
        }

        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        // Auszugebenden Text abbrechen
        outputText = "";
        if (mainFrame.talkCount != 0) {
            mainFrame.isClipSet = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
    }


    @Override
    public void evalMouseMoveEvent(GenericPoint pTxxx) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (mainFrame.isAnimRunning) {
            if (Cursorform != 20) {
                Cursorform = 20;
                mainFrame.setCursor(mainFrame.cursorNone);
            }
        }
    }

    @Override
    public void evalMouseExitEvent() {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // Was soll Krabat machen ?
        switch (nextActionID) {

            case 100:
                // Mueller spricht
                PersonSagt("Swoboda_1", 0, 36, 2, 120, mueller.evalMlynkTalkPoint());
                break;

            case 120:
                // Mueller spricht
                PersonSagt("Swoboda_2", 0, 36, 2, 130, mueller.evalMlynkTalkPoint());
                break;

            case 130:
                // Mutter spricht 1. Satz, Raben beginnen mit gezieltem Kratzen
                PersonSagt("Swoboda_3", 0, 20, 2, 135, mutterTalk);
                rapakiAchtung = true;
                break;

            case 135:
                // Mutter spricht und zeigt auf K
                PersonSagt("Swoboda_4", 0, 60, 2, 140, mutterTalk);
                break;

            case 140:
                // Mueller spricht, Raben hoeren auf sich zu kratzen
                PersonSagt("Swoboda_5", 0, 36, 2, 150, mueller.evalMlynkTalkPoint());
                mlynkHatStock = true;
                rapakiAchtung = false;
                break;

            case 150:
                // Raben rausmorphen
                GenericPoint pTe = new GenericPoint(rapakpos[KRABATRABE].x + RavenFreedom.Breite / 2,
                        rapakpos[KRABATRABE].y + RavenFreedom.Hoehe);
                rabemorph.Init(pTe, 50);
                israbemorphing = true;
                nextActionID = 153;
                break;

            case 153:
                // Rabe weg
                if (rabemorphcount < 3) {
                    break;
                }
                rabeVisible = false;
                nextActionID = 160;
                break;

            case 160:
                // Morphing beginnen
                if (rabemorphcount < 8) {
                    break;
                }
                israbemorphing = false;
                mainFrame.isClipSet = false;
                Counter = 22;
                nextActionID = 165;
                break;

            case 165:
                // Krabats Morph beginnt verzoegert...
                if (--Counter > 1) {
                    break;
                }
                krabatmorph.Init(mainFrame.krabat.getPos(), 240);
                iskrabatmorphing = true;
                nextActionID = 170;
                break;

            case 170:
                // jetzt erscheint K
                if (krabatmorphcount < 3) {
                    break;
                }
                krabatHere = true;
                nextActionID = 180;
                break;

            case 180:
                // Krabat zeigen
                if (krabatmorphcount < 8) {
                    break;
                }
                mlynkHatStock = false;
                iskrabatmorphing = false;
                mainFrame.isClipSet = false;
                TalkPause = 10;
                nextActionID = 185;
                break;

            case 185:
                // Mueller spricht
                PersonSagt("Swoboda_6", 0, 36, 2, 210, mueller.evalMlynkTalkPoint());
                break;

            case 210:
                // Skip zu Mlyn2
                NeuesBild(90, 92);
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }
    }
}