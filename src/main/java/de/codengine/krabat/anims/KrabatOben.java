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

package de.codengine.krabat.anims;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public class KrabatOben extends Krabat {
    private static final Logger log = LoggerFactory.getLogger(KrabatOben.class);
    // Alle "von - oben" Images
    private final GenericImage[] krabato_left;
    private final GenericImage[] krabato_right;
    private final GenericImage[] krabato_front;
    private final GenericImage[] krabato_back;

    private final GenericImage[] krabato_talkl;
    private final GenericImage[] krabato_talkr;
    private final GenericImage[] krabato_talkm;
    private final GenericImage[] krabato_klopf;

    private final GenericImage[] krabato_floetel;
    private final GenericImage[] krabato_floeter;
    private final GenericImage[] krabato_floetef;
    private final GenericImage[] krabato_floeteb;

    private final GenericImage[] krabato_rfloetel;
    private final GenericImage[] krabato_rfloeter;
    private final GenericImage[] krabato_rfloetef;
    private final GenericImage[] krabato_rfloeteb;

    private final GenericImage[] krabato_wosuskl;
    private final GenericImage[] krabato_wosuskr;
    private final GenericImage[] krabato_wosuskf;
    private final GenericImage[] krabato_wosuskb;

    private final GenericImage[] krabato_feuerl;
    private final GenericImage[] krabato_feuerr;
    private final GenericImage[] krabato_feuerf;
    private final GenericImage[] krabato_feuerb;

    private GenericImage krabato_liesl;
    private GenericImage krabato_liesr;
    private GenericImage krabato_liesf;
    private GenericImage krabato_liesb;

    // Spritevariablen
    private static final int CWIDTHO = 41; // Default fuer von Oben
    private static final int CHEIGHTO = 41;

    // Abstaende default von Oben
    private static final int CHORIZO_DIST = 4; // Hier Default fuer von Oben
    private static final int CVERTO_DIST = 4;

    // Variablen fuer Laufberechnung
    private static final int CLOHNENXO = 18;  // fuer von oben
    private static final int CLOHNENYO = 18;

    private static final int SLOWXO = 10;  // Hier fuer von oben
    private static final int SLOWYO = 10;  // nach dem Subtraktionsschema
    // wie gross muss Zoomf sein, dass alle Abstaende -1
    // gerechnet werden...

    private int TalkMitte = 1;
    private int TalkLeft = 1;
    private int TalkRight = 1;

    private int Verhindertalk = 2;
    private static final int MAX_VERHINDERTALK = 4;

    private int Verhinderbody = 6;
    private static final int MAX_VERHINDERBODY = 10;

    private int describeMitte = 1;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public KrabatOben(Start caller) {
        super(caller, CWIDTHO, CHEIGHTO);

        krabato_left = new GenericImage[10];
        krabato_right = new GenericImage[10];
        krabato_front = new GenericImage[10];
        krabato_back = new GenericImage[10];

        krabato_talkl = new GenericImage[4];
        krabato_talkr = new GenericImage[4];
        krabato_talkm = new GenericImage[5];
        krabato_klopf = new GenericImage[3];

        krabato_floetel = new GenericImage[5];
        krabato_floeter = new GenericImage[5];
        krabato_floeteb = new GenericImage[5];
        krabato_floetef = new GenericImage[5];

        krabato_rfloetel = new GenericImage[5];
        krabato_rfloeter = new GenericImage[5];
        krabato_rfloeteb = new GenericImage[5];
        krabato_rfloetef = new GenericImage[5];

        krabato_wosuskl = new GenericImage[2];
        krabato_wosuskr = new GenericImage[2];
        krabato_wosuskf = new GenericImage[2];
        krabato_wosuskb = new GenericImage[2];

        krabato_feuerl = new GenericImage[2];
        krabato_feuerr = new GenericImage[2];
        krabato_feuerf = new GenericImage[2];
        krabato_feuerb = new GenericImage[2];

        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        // Images von oben (alle)
        krabato_left[1] = getPicture("gfx/anims/h-l-1.gif");
        krabato_left[2] = getPicture("gfx/anims/h-l-2.gif");
        krabato_left[3] = getPicture("gfx/anims/h-l-3.gif");
        krabato_left[4] = getPicture("gfx/anims/h-l-4.gif");
        krabato_left[5] = getPicture("gfx/anims/h-l-5.gif");
        krabato_left[6] = getPicture("gfx/anims/h-l-6.gif");
        krabato_left[7] = getPicture("gfx/anims/h-l-7.gif");
        krabato_left[8] = getPicture("gfx/anims/h-l-8.gif");
        krabato_left[9] = getPicture("gfx/anims/h-l-9.gif");
        krabato_left[0] = getPicture("gfx/anims/h-l-10.gif");

        krabato_right[1] = getPicture("gfx/anims/h-r-1.gif");
        krabato_right[2] = getPicture("gfx/anims/h-r-2.gif");
        krabato_right[3] = getPicture("gfx/anims/h-r-3.gif");
        krabato_right[4] = getPicture("gfx/anims/h-r-4.gif");
        krabato_right[5] = getPicture("gfx/anims/h-r-5.gif");
        krabato_right[6] = getPicture("gfx/anims/h-r-6.gif");
        krabato_right[7] = getPicture("gfx/anims/h-r-7.gif");
        krabato_right[8] = getPicture("gfx/anims/h-r-8.gif");
        krabato_right[9] = getPicture("gfx/anims/h-r-9.gif");
        krabato_right[0] = getPicture("gfx/anims/h-r-10.gif");

        krabato_front[1] = getPicture("gfx/anims/h-u-1.gif");
        krabato_front[2] = getPicture("gfx/anims/h-u-2.gif");
        krabato_front[3] = getPicture("gfx/anims/h-u-3.gif");
        krabato_front[4] = getPicture("gfx/anims/h-u-4.gif");
        krabato_front[5] = getPicture("gfx/anims/h-u-5.gif");
        krabato_front[6] = getPicture("gfx/anims/h-u-6.gif");
        krabato_front[7] = getPicture("gfx/anims/h-u-7.gif");
        krabato_front[8] = getPicture("gfx/anims/h-u-8.gif");
        krabato_front[9] = getPicture("gfx/anims/h-u-9.gif");
        krabato_front[0] = getPicture("gfx/anims/h-u-10.gif");

        krabato_back[1] = getPicture("gfx/anims/h-o-1.gif");
        krabato_back[2] = getPicture("gfx/anims/h-o-2.gif");
        krabato_back[3] = getPicture("gfx/anims/h-o-3.gif");
        krabato_back[4] = getPicture("gfx/anims/h-o-4.gif");
        krabato_back[5] = getPicture("gfx/anims/h-o-5.gif");
        krabato_back[6] = getPicture("gfx/anims/h-o-6.gif");
        krabato_back[7] = getPicture("gfx/anims/h-o-7.gif");
        krabato_back[8] = getPicture("gfx/anims/h-o-8.gif");
        krabato_back[9] = getPicture("gfx/anims/h-o-9.gif");
        krabato_back[0] = getPicture("gfx/anims/h-o-10.gif");

        krabato_talkl[1] = getPicture("gfx/anims/h-tl-1.gif");
        krabato_talkl[2] = getPicture("gfx/anims/h-tl-2.gif");
        krabato_talkl[3] = getPicture("gfx/anims/h-tl-3.gif");

        krabato_talkr[1] = getPicture("gfx/anims/h-tr-1.gif");
        krabato_talkr[2] = getPicture("gfx/anims/h-tr-2.gif");
        krabato_talkr[3] = getPicture("gfx/anims/h-tr-3.gif");

        krabato_talkm[1] = getPicture("gfx/anims/h-tk-1.gif");
        krabato_talkm[2] = getPicture("gfx/anims/h-tk-2.gif");
        krabato_talkm[3] = getPicture("gfx/anims/h-tk-3.gif");
        krabato_talkm[4] = getPicture("gfx/anims/h-tk-4.gif");

        krabato_klopf[1] = getPicture("gfx/anims/h-o-20.gif");
        krabato_klopf[2] = getPicture("gfx/anims/h-o-21.gif");

        krabato_floetel[0] = getPicture("gfx/anims/h-l-f1.gif");
        krabato_floetel[1] = getPicture("gfx/anims/h-l-f2.gif");
        krabato_floetel[2] = getPicture("gfx/anims/h-l-f3.gif");
        krabato_floetel[3] = getPicture("gfx/anims/h-l-f4.gif");
        krabato_floetel[4] = getPicture("gfx/anims/h-l-f5.gif");

        krabato_floeter[0] = getPicture("gfx/anims/h-r-f1.gif");
        krabato_floeter[1] = getPicture("gfx/anims/h-r-f2.gif");
        krabato_floeter[2] = getPicture("gfx/anims/h-r-f3.gif");
        krabato_floeter[3] = getPicture("gfx/anims/h-r-f4.gif");
        krabato_floeter[4] = getPicture("gfx/anims/h-r-f5.gif");

        krabato_floetef[0] = getPicture("gfx/anims/h-u-f1.gif");
        krabato_floetef[1] = getPicture("gfx/anims/h-u-f2.gif");
        krabato_floetef[2] = getPicture("gfx/anims/h-u-f3.gif");
        krabato_floetef[3] = getPicture("gfx/anims/h-u-f4.gif");
        krabato_floetef[4] = getPicture("gfx/anims/h-u-f5.gif");

        krabato_floeteb[0] = getPicture("gfx/anims/h-o-f1.gif");
        krabato_floeteb[1] = getPicture("gfx/anims/h-o-f2.gif");
        krabato_floeteb[2] = getPicture("gfx/anims/h-o-f3.gif");
        krabato_floeteb[3] = getPicture("gfx/anims/h-o-f4.gif");
        krabato_floeteb[4] = getPicture("gfx/anims/h-o-f5.gif");

        krabato_rfloetel[0] = getPicture("gfx/anims/h-l-fr1.gif");
        krabato_rfloetel[1] = getPicture("gfx/anims/h-l-fr2.gif");
        krabato_rfloetel[2] = getPicture("gfx/anims/h-l-fr3.gif");
        krabato_rfloetel[3] = getPicture("gfx/anims/h-l-fr4.gif");
        krabato_rfloetel[4] = getPicture("gfx/anims/h-l-fr5.gif");

        krabato_rfloeter[0] = getPicture("gfx/anims/h-r-fr1.gif");
        krabato_rfloeter[1] = getPicture("gfx/anims/h-r-fr2.gif");
        krabato_rfloeter[2] = getPicture("gfx/anims/h-r-fr3.gif");
        krabato_rfloeter[3] = getPicture("gfx/anims/h-r-fr4.gif");
        krabato_rfloeter[4] = getPicture("gfx/anims/h-r-fr5.gif");

        krabato_rfloetef[0] = getPicture("gfx/anims/h-u-fr1.gif");
        krabato_rfloetef[1] = getPicture("gfx/anims/h-u-fr2.gif");
        krabato_rfloetef[2] = getPicture("gfx/anims/h-u-fr3.gif");
        krabato_rfloetef[3] = getPicture("gfx/anims/h-u-fr4.gif");
        krabato_rfloetef[4] = getPicture("gfx/anims/h-u-fr5.gif");

        krabato_rfloeteb[0] = getPicture("gfx/anims/h-o-fr1.gif");
        krabato_rfloeteb[1] = getPicture("gfx/anims/h-o-fr2.gif");
        krabato_rfloeteb[2] = getPicture("gfx/anims/h-o-fr3.gif");
        krabato_rfloeteb[3] = getPicture("gfx/anims/h-o-fr4.gif");
        krabato_rfloeteb[4] = getPicture("gfx/anims/h-o-fr5.gif");

        krabato_wosuskl[0] = getPicture("gfx/anims/h-l-st1.gif");
        krabato_wosuskl[1] = getPicture("gfx/anims/h-l-st2.gif");

        krabato_wosuskr[0] = getPicture("gfx/anims/h-r-st1.gif");
        krabato_wosuskr[1] = getPicture("gfx/anims/h-r-st2.gif");

        krabato_wosuskf[0] = getPicture("gfx/anims/h-u-st1.gif");
        krabato_wosuskf[1] = getPicture("gfx/anims/h-u-st2.gif");

        krabato_wosuskb[0] = getPicture("gfx/anims/h-o-st1.gif");
        krabato_wosuskb[1] = getPicture("gfx/anims/h-o-st2.gif");

        krabato_feuerl[0] = getPicture("gfx/anims/h-l-k1.gif");
        krabato_feuerl[1] = getPicture("gfx/anims/h-l-k2.gif");

        krabato_feuerr[0] = getPicture("gfx/anims/h-r-k1.gif");
        krabato_feuerr[1] = getPicture("gfx/anims/h-r-k2.gif");

        krabato_feuerf[0] = getPicture("gfx/anims/h-u-k1.gif");
        krabato_feuerf[1] = getPicture("gfx/anims/h-u-k2.gif");

        krabato_feuerb[0] = getPicture("gfx/anims/h-o-k1.gif");
        krabato_feuerb[1] = getPicture("gfx/anims/h-o-k2.gif");

        krabato_liesl = getPicture("gfx/anims/h-l-k.gif");
        krabato_liesr = getPicture("gfx/anims/h-r-k.gif");
        krabato_liesf = getPicture("gfx/anims/h-u-k.gif");
        krabato_liesb = getPicture("gfx/anims/h-o-k.gif");
    }


    // Laufen mit Krabat ////////////////////////////////////////////////////////////////

    // Krabat um einen Schritt weitersetzen, Achtung ! Diese Routine wird sowohl im "MousePressed" - Event
    // als auch im "Paint" - Event angesprungen...

    // Diese Routine bleibt unveraendert, unabhaengig davon, wie Krabat gerade aussieht
    @Override
    public synchronized void Move() {
        // Wenn kein Laufen gewuenscht, dann auch nicht laufen!
        if (!isWalking && !isWandering) {
            return;
        }

        // Variablen uebernehmen (Threadsynchronisierung)
        horizontal = Thorizontal;
        walkto = new GenericPoint(Twalkto.x, Twalkto.y);
        directionX = tDirectionX;
        directionY = tDirectionY;

        if (horizontal)
        // Horizontal laufen, es gelten die Images left und right, gehen von 2 bis 11, von oben 1 bis 9
        {
            // Animationsphase weiterschalten,
            anim_pos++;

            // Laufen von Oben, Ueberschreitung pruefen
            if (anim_pos > 9) {
                anim_pos = 1;
            }

            // neuen Punkt ermitteln und setzen
            VerschiebeX();
            xps = txps;
            yps = typs;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeX();

            // Ueberschreitung feststellen in X - Richtung
            if ((walkto.x - (int) txps) * directionX.getVal() <= 0) {
                // System.out.println("Ueberschreitung x! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                isWalking = false;
                if (!isWandering && clearanimpos) {
                    anim_pos = 0;
                }
            }
        } else
        // Vertikal laufen, Images front und back von 2 bis 9, von oben 1 bis 9
        {
            // Animationsphase weiterschalten
            anim_pos++;

            // Laufen von Oben, Ueberschreitung pruefen
            if (anim_pos > 9) {
                anim_pos = 1;
            }

            // neuen Punkt ermitteln und setzen
            VerschiebeY();
            xps = txps;
            yps = typs;

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            VerschiebeY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkto.y - (int) typs) * directionY.getVal() <= 0) {
                // System.out.println("Ueberschreitung y! " + walkto.x + " " + walkto.y + " " + txps + " " + typs);
                isWalking = false;
                if (!isWandering && clearanimpos) {
                    anim_pos = 0;
                }
            }
        }

        if (!isWalking && !isWandering) {
            // System.out.println("Krabatpos korrigiert!");
            setPos(walkto);
            if (clearanimpos) {
                anim_pos = 0;
            }
        }
        // if (anim_pos == 0) 
        //{
        //System.out.println("Animpos reset!");
        //}
        // pos_x = (int) xps;
        // pos_y = (int) yps;
    }

    // Horizontal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void VerschiebeX() {
        // Verschieberoutine fuer Laufen von oben
        // Skalierungsfaktor ist gleich zoomf bei von Oben

        // Zooming - Faktor beruecksichtigen in x - Richtung
        float horizDist = CHORIZO_DIST - zoomf / SLOWXO;
        if (horizDist < 1) {
            horizDist = 1;
        }

        verschiebeXkrabat(horizDist);
    }

    // Vertikal - Positions - Verschieberoutine, je nach Krabat - Aussehen
    private void VerschiebeY() {
        // Verschieberoutine fuer Laufen von oben
        // Skalierungsfaktor ist zoomf fuer von Oben

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vertDist = CVERTO_DIST - zoomf / SLOWYO;
        if (vertDist < 1) {
            vertDist = 1;
            // hier kann noch eine Entscheidungsroutine hin, die je nach Animationsphase
            // und vert_distance ein Pixel erlaubt oder nicht
        }

        verschiebeYkrabat(vertDist);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    @Override
    public synchronized void MoveTo(GenericPoint aim) {
        // Lohnt es sich zu laufen ?
        int lohnx = CLOHNENXO - (int) zoomf;
        int lohny = CLOHNENYO - (int) zoomf;
        moveToKrabat(aim, lohnx, lohny, 1, 45);
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    @Override
    public void drawKrabat(GenericDrawingContext offGraph) {
        // Malen von oben
        // je nach Richtung Sprite auswaehlen und zeichnen
        if (horizontal) {
            // nach links laufen
            if (directionX == LEFT) {
                MaleIhn(offGraph, krabato_left[anim_pos]);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                MaleIhn(offGraph, krabato_right[anim_pos]);
            }
        } else {
            // nach oben laufen
            if (directionY == UP) {
                MaleIhn(offGraph, krabato_back[anim_pos]);
            }

            // nach unten laufen
            if (directionY == DOWN) {
                MaleIhn(offGraph, krabato_front[anim_pos]);
            }
        }
    }


    // Abspielen einer Animation
    @Override
    public void DoAnimation(GenericDrawingContext g) {
        switch (nAnimation) {
            case 2:    // Floete spielen
                SpieleFloete(g, nAnimStep++);
                break;

            case 4:    // An Tuer klopfen
                if (nAnimStep < 11) {
                    Klopfe(g, nAnimStep++);
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 5:  // Rohodz spielen
                if (nAnimStep == 0) {
                    mainFrame.wave.PlayFile("sfx/frohodz.wav");
                } ///////////////// Sound !!!!!!!!!!!!!!
                if (nAnimStep < rohodzWartezeit) {
                    SpieleRohodz(g, nAnimStep++);
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 146: // Wosusk essen (speziell)
                if (nAnimStep < 20) {
                    if (nAnimStep == 0) {
                        mainFrame.wave.PlayFile("sfx-dd/wosusk.wav");
                    } ////////////Sound!!!!!!!!!!!!!!!!!!!!!!
                    IssWosusk(g);
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 154: // Feuer machen
                if (nAnimStep < 8) {
                    if (nAnimStep == 0) {
                        mainFrame.wave.PlayFile("sfx/kamjeny.wav");
                    } /////////////////// Sound !!!!!!!!!!!
                    MacheFeuer(g);
                    nAnimStep++;
                } else {
                    StopAnim();
                    drawKrabat(g);
                }
                break;

            case 155: // aus dem Buch lesen
                LiesBuch(g);
                break;

        }
    }

    // Krabat spielt Floete incl Zoominginformationen
    private void SpieleFloete(GenericDrawingContext g, int tCount) {
        // Sound abspielen
        if (tCount == 0) {
            int zuffi = (int) (Math.random() * 4.99);
            mainFrame.wave.PlayFile("sfx/flejta" + (char) (zuffi + 49) + ".wav");
            Floetenwartezeit = Floetenwartezeitarray[zuffi];
        }

        if (--Floetenwartezeit < 1) {
            // als letztes normal nach vorn sehend hinstellen
            StopAnim();
            drawKrabat(g);
            return;
        }

        // von Oben
        int nFrame = tCount % 10 / 2;

        if (GetFacing() == 3) {
            MaleIhn(g, krabato_floeter[nFrame]);
        }
        if (GetFacing() == 6) {
            MaleIhn(g, krabato_floetef[nFrame]);
        }
        if (GetFacing() == 9) {
            MaleIhn(g, krabato_floetel[nFrame]);
        }
        if (GetFacing() == 12) {
            MaleIhn(g, krabato_floeteb[nFrame]);
        }
    }

    // Krabat spielt Rohodz incl Zoominginformationen
    private void SpieleRohodz(GenericDrawingContext g, int tCount) {
        // von Oben
        int nFrame = tCount % 10 / 2;

        if (GetFacing() == 3) {
            MaleIhn(g, krabato_rfloeter[nFrame]);
        }
        if (GetFacing() == 6) {
            MaleIhn(g, krabato_rfloetef[nFrame]);
        }
        if (GetFacing() == 9) {
            MaleIhn(g, krabato_rfloetel[nFrame]);
        }
        if (GetFacing() == 12) {
            MaleIhn(g, krabato_rfloeteb[nFrame]);
        }
    }

    // Krabat klopft an Tuer
    private void Klopfe(GenericDrawingContext g, int tCount) {
        int welche = (tCount & 1) + 1;

        MaleIhn(g, krabato_klopf[welche]);
    }


    // Lasse K wosusk essen
    private void IssWosusk(GenericDrawingContext g) {
        int tempvar = nAnimStep % 4 < 2 ? 0 : 1;

        if (GetFacing() == 3) {
            MaleIhn(g, krabato_wosuskr[tempvar]);
        }
        if (GetFacing() == 6) {
            MaleIhn(g, krabato_wosuskf[tempvar]);
        }
        if (GetFacing() == 9) {
            MaleIhn(g, krabato_wosuskl[tempvar]);
        }
        if (GetFacing() == 12) {
            MaleIhn(g, krabato_wosuskb[tempvar]);
        }

    }

    // Lasse K mit Feuersteinen spielen
    private void MacheFeuer(GenericDrawingContext g) {
        int tempvar = nAnimStep % 4 < 2 ? 0 : 1;

        if (GetFacing() == 3) {
            MaleIhn(g, krabato_feuerr[tempvar]);
        }
        if (GetFacing() == 6) {
            MaleIhn(g, krabato_feuerf[tempvar]);
        }
        if (GetFacing() == 9) {
            MaleIhn(g, krabato_feuerl[tempvar]);
        }
        if (GetFacing() == 12) {
            MaleIhn(g, krabato_feuerb[tempvar]);
        }
    }

    // Lasse K das Buch lesen
    private void LiesBuch(GenericDrawingContext g) {
        if (GetFacing() == 3) {
            MaleIhn(g, krabato_liesr);
        }
        if (GetFacing() == 6) {
            MaleIhn(g, krabato_liesf);
        }
        if (GetFacing() == 9) {
            MaleIhn(g, krabato_liesl);
        }
        if (GetFacing() == 12) {
            MaleIhn(g, krabato_liesb);
        }
    }

    // Zeichne Krabat beim Sprechen mit anderen Personen
    @Override
    public void talkKrabat(GenericDrawingContext offGraph) {
        if (mainFrame.talkCount > 1) {
            // Reden von Oben
            if (--Verhinderbody < 1) {
                Verhinderbody = MAX_VERHINDERBODY;

                TalkLeft = (int) Math.round(Math.random() * 3);
                TalkLeft++;
                if (TalkLeft == 4) {
                    TalkLeft = 2;
                }

                TalkRight = (int) Math.round(Math.random() * 3);
                TalkRight++;
                if (TalkRight == 4) {
                    TalkRight = 2;
                }
            }

            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                TalkMitte = (int) Math.round(Math.random() * 4);
                TalkMitte++;
                if (TalkMitte == 5) {
                    TalkMitte = 1;
                }
            }

            Rede(offGraph, TalkLeft, TalkMitte, TalkRight);
        } else {
            drawKrabat(offGraph);
        }
    }

    // Krabat beim Monolog (ohne Gestikulieren)
    @Override
    public void describeKrabat(GenericDrawingContext offGraph) {
        // reden von Oben
        if (mainFrame.talkCount > 1) {
            int links = 2;
            int rechts = 2;

            if (--Verhindertalk < 1) {
                Verhindertalk = MAX_VERHINDERTALK;
                describeMitte = (int) Math.round(Math.random() * 4);
                describeMitte++;
                if (describeMitte == 5) {
                    describeMitte = 1;
                }
            }

            Rede(offGraph, links, describeMitte, rechts);
        } else {
            drawKrabat(offGraph);
        }
    }

    // Gilt nur fuer von Oben, Images wurden bereits getestet
    private void Rede(GenericDrawingContext g, int links, int mitte, int rechts) {
        // Clipping - Region setzen
        krabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps);
        int up = getUpPos((int) yps);
        int scale = (int) zoomf;

        // linken Teil der Figur zeichnen
        g.drawImage(krabato_talkl[links], left, up, 9 - scale * 9 / 41, CHEIGHTO - scale);

        // Mitte zeichnen, dabei Scaling - Offset beachten
        int off = 9 - scale * 10 / 41;
        g.drawImage(krabato_talkm[mitte], left + off, up, 22 - scale * 22 / 41, CHEIGHTO - scale);

        // rechten Teil zeichnen, 2 Offsets beachten
        int ooff = off + 22 - scale * 22 / 41;
        g.drawImage(krabato_talkr[rechts], left + ooff, up, 9 - scale * 10 / 41, CHEIGHTO - scale);
    }

    // Zooming-Variablen berechnen
    private int getLeftPos(int pox) {
        return getLeftPos(pox, 1);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int pox, int ignored) {
        return pox - (CWIDTHO - (int) zoomf) / 2;
    }

    @Override
    protected int getUpPos(int poy) {
        return poy - (CHEIGHTO - (int) zoomf) / 2;
    }

    @Override
    protected int getScale(int poy) {
        return calcScaleDefault(poy);
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    private void krabatClip(GenericDrawingContext g, int xx, int yy) {
        krabatClipDefault(g, xx, yy, 2);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    @Override
    public Borderrect getRect() {
        int x = getLeftPos((int) xps);
        int y = getUpPos((int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = 2 * ((int) yps - y) + y;
        return new Borderrect(x, y, xd, yd);
    }

    private void MaleIhn(GenericDrawingContext g, GenericImage ktemp) {
        // Clipping - Region setzen
        krabatClip(g, (int) xps, (int) yps);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) xps);
        int up = getUpPos((int) yps);
        int scale = (int) zoomf;

        // Figur zeichnen
        g.drawImage(ktemp, left, up, CWIDTHO - scale, CHEIGHTO - scale);
    }
}