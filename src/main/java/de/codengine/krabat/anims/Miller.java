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
import de.codengine.krabat.main.BorderRect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public class Miller extends MovableMainAnim {
    private static final Logger log = LoggerFactory.getLogger(Miller.class);
    // Alle GenericImage - Objekte
    private final GenericImage[] krabat_back;

    private final GenericImage[] krabat_stand_front_head;
    private final GenericImage[] krabat_stand_front_body;
    private final GenericImage[] krabat_stand_left_head;
    private final GenericImage[] krabat_stand_left_body;
    private final GenericImage[] krabat_stand_right_head;
    private final GenericImage[] krabat_stand_right_body;

    private final GenericImage[] krabat_talk_front_head;
    private final GenericImage[] krabat_talk_front_body;
    private final GenericImage[] krabat_talk_left_head;
    private final GenericImage[] krabat_talk_left_body;
    private final GenericImage[] krabat_talk_right_head;
    private final GenericImage[] krabat_talk_right_body;

    private GenericImage krabat_hat_stock;
    private GenericImage krabat_gib_karte;
    private GenericImage krabat_hat_stock_andersrum;

    private int Zwinker = 0;
    private static final int BODYOFFSET = 29;

    private int Verhinderkopf;
    private static final int MAX_VERHINDERKOPF = 2;

    private int Verhinderbody;
    private static final int MAX_VERHINDERBODY = 8;

    private int Head = 0;
    private int Body = 0;

    // Spritevariablen
    private static final int CWIDTH = 70;// Default - Werte Hoehe,Breite
    private static final int CHEIGHT = 100;
    private static final int CWIDTH_STOCK = 100;  // so breit ist er, wenn er den Stock hat
    private static final int COFFSET_STOCK = 132;  // so weit ist das ungezoomte GenericImage nach links verschoben (* 2) = virtuelle Breite
    // wenn man beachtet, dass die Fussposition so bleiben soll

    private final float scaleVerhaeltnisStock = (float) COFFSET_STOCK / CHEIGHT;

    // Abstaende default
    private static final int CVERT_DIST = 1;

    // beim Zoomen veraendern
    private static final int SLOWY = 10;  // dsgl. fuer y - Richtung

    private boolean istMuellerSchonTot = false;

    // Initialisierung ////////////////////////////////////////////////////////////////

    public Miller(Start caller) {
        super(caller, CWIDTH, CHEIGHT);

        krabat_back = new GenericImage[9];

        krabat_stand_front_head = new GenericImage[2];
        krabat_stand_front_body = new GenericImage[1];
        krabat_stand_left_head = new GenericImage[2];
        krabat_stand_left_body = new GenericImage[1];
        krabat_stand_right_head = new GenericImage[2];
        krabat_stand_right_body = new GenericImage[1];

        krabat_talk_front_head = new GenericImage[5];
        krabat_talk_front_body = new GenericImage[3];
        krabat_talk_left_head = new GenericImage[4];
        krabat_talk_left_body = new GenericImage[3];
        krabat_talk_right_head = new GenericImage[4];
        krabat_talk_right_body = new GenericImage[3];

        initImages();

        Verhinderkopf = MAX_VERHINDERKOPF;
        Verhinderbody = MAX_VERHINDERBODY;
    }

    // Bilder vorbereiten
    private void initImages() {
        krabat_back[0] = getPicture("gfx/anims/ml3.png");
        krabat_back[1] = getPicture("gfx/anims/ml3-1.png");
        krabat_back[2] = getPicture("gfx/anims/ml3-1.png");
        krabat_back[3] = getPicture("gfx/anims/ml3-2.png");
        krabat_back[4] = getPicture("gfx/anims/ml3-2.png");
        krabat_back[5] = getPicture("gfx/anims/ml3-3.png");
        krabat_back[6] = getPicture("gfx/anims/ml3-3.png");
        krabat_back[7] = getPicture("gfx/anims/ml3-4.png");
        krabat_back[8] = getPicture("gfx/anims/ml3-4.png");

        krabat_stand_front_head[0] = getPicture("gfx/anims/ml1-h.png");
        krabat_stand_front_head[1] = getPicture("gfx/anims/ml1-ha.png");
        krabat_stand_front_body[0] = getPicture("gfx/anims/ml1-b.png");

        krabat_stand_left_head[0] = getPicture("gfx/anims/ml2-h.png");
        krabat_stand_left_head[1] = getPicture("gfx/anims/ml2-ha.png");
        krabat_stand_left_body[0] = getPicture("gfx/anims/ml2-b.png");

        krabat_stand_right_head[0] = getPicture("gfx/anims/ml4-h.png");
        krabat_stand_right_head[1] = getPicture("gfx/anims/ml4-ha.png");
        krabat_stand_right_body[0] = getPicture("gfx/anims/ml4-b.png");

        krabat_talk_front_head[0] = getPicture("gfx/anims/ml1-h1.png");
        krabat_talk_front_head[1] = getPicture("gfx/anims/ml1-h2.png");
        krabat_talk_front_head[2] = getPicture("gfx/anims/ml1-h3.png");
        krabat_talk_front_head[3] = getPicture("gfx/anims/ml1-h4.png");
        krabat_talk_front_head[4] = getPicture("gfx/anims/ml1-h5.png");

        krabat_talk_front_body[0] = getPicture("gfx/anims/ml1-b1.png");
        krabat_talk_front_body[1] = getPicture("gfx/anims/ml1-b2.png");
        krabat_talk_front_body[2] = getPicture("gfx/anims/ml1-b3.png");

        krabat_talk_left_head[0] = getPicture("gfx/anims/ml2-h1.png");
        krabat_talk_left_head[1] = getPicture("gfx/anims/ml2-h2.png");
        krabat_talk_left_head[2] = getPicture("gfx/anims/ml2-h3.png");
        krabat_talk_left_head[3] = getPicture("gfx/anims/ml2-h5.png");

        krabat_talk_left_body[0] = getPicture("gfx/anims/ml2-b1.png");
        krabat_talk_left_body[1] = getPicture("gfx/anims/ml2-b2.png");
        krabat_talk_left_body[2] = getPicture("gfx/anims/ml2-b3.png");

        krabat_talk_right_head[0] = getPicture("gfx/anims/ml4-h2.png");
        krabat_talk_right_head[1] = getPicture("gfx/anims/ml4-h3.png");
        krabat_talk_right_head[2] = getPicture("gfx/anims/ml4-h4.png");
        krabat_talk_right_head[3] = getPicture("gfx/anims/ml4-h5.png");

        krabat_talk_right_body[0] = getPicture("gfx/anims/ml4-b1.png");
        krabat_talk_right_body[1] = getPicture("gfx/anims/ml4-b2.png");
        krabat_talk_right_body[2] = getPicture("gfx/anims/ml4-b3.png");

        krabat_hat_stock = getPicture("gfx/anims/ml5-b.png");
        krabat_gib_karte = getPicture("gfx/anims/ml1-gibkarte.png");

        krabat_hat_stock_andersrum = getPicture("gfx/anims/ml5-c.png");
    }


    // Laufen mit Mlynk ////////////////////////////////////////////////////////////////

    // Plokarka um einen Schritt weitersetzen
    // false = weiterlaufen, true = stehengebleibt
    public synchronized boolean move() {
        // Variablen uebernehmen (Threadsynchronisierung)
        isAnimHorizontal = tmpIsAnimHorizontal;
        walkTo = tmpWalkTo;
        directionX = tmpDirectionX;
        directionY = tmpDirectionY;

        if (!isAnimHorizontal)
        // Vertikal laufen
        {
            // neuen Punkt ermitteln und setzen
            moveY();
            posX = tempPosX;
            posY = tempPosY;

            // Animationsphase weiterschalten
            animPos++;
            if (animPos == 9) {
                animPos = 1;
            }

            // Naechsten Schritt auf Gueltigkeit ueberpruefen
            moveY();

            // Ueberschreitung feststellen in Y - Richtung
            if ((walkTo.y - (int) tempPosY) * directionY.getVal() <= 0) {
                setPos(walkTo);
                animPos = 0;
                return true;
            }
        }

        return false;
    }

    // Vertikal - Positions - Verschieberoutine
    private void moveY() {
        moveYdefault(CVERT_DIST, SLOWY);
    }

    // Vorbereitungen fuer das Laufen treffen und starten
    // Diese Routine wird nur im "MousePressed" - Event angesprungen
    public synchronized void moveTo(GenericPoint aim) {
        moveToDefault(aim);
        tmpIsAnimHorizontal = false;

        if (animPos == 0) {
            animPos = 1;       // Animationsimage bei Neubeginn initialis.
        }
    }

    // Krabat - Animationen /////////////////////////////////////////////////////////////

    // je nach Laufrichtung Krabat zeichnen
    public void drawMlynk(GenericDrawingContext offGraph) {
        // je nach Richtung Sprite auswaehlen und zeichnen (hier aber nur Stehen , ausser back = laufen)

        // Zufallscounter fuer Zwinkern
        int zuffZahl = (int) Math.round(Math.random() * 50);

        if (Zwinker == 1) {
            Zwinker = 0;
        } else {
            if (zuffZahl > 45) {
                Zwinker = 1;
            }
        }

        // Override, wenn der Mueller gestorben ist
        if (istMuellerSchonTot) {
            Zwinker = 0;
        }

        // fuer MaleIhn nur Richtung angeben, restlichen Variablen koennen dort ausgewertet werden
        if (isAnimHorizontal) {
            // nach links laufen
            if (directionX == LEFT) {
                drawHim(offGraph, 9);
            }

            // nach rechts laufen
            if (directionX == RIGHT) {
                drawHim(offGraph, 3);
            }
        } else {
            // Bei normaler Darstellung
            if (!upsideDown) {
                // nach oben laufen
                if (directionY == UP) {
                    drawHim(offGraph, 12);
                }

                // nach unten laufen
                if (directionY == DOWN) {
                    drawHim(offGraph, 6);
                }
            }
        }
    }

    // Zwinkern verhindern 
    public void drawMlynkWithoutZwinkern(GenericDrawingContext g) {
        istMuellerSchonTot = true;  // gehackt, aber egal, brauchen den Mueller nicht wieder ab hier...
        drawMlynk(g);
    }

    // Hier hat der Mueller den Stock erhoben
    public void drawMlynkWithKij(GenericDrawingContext offGraph) {
        // Zufallscounter fuer Zwinkern
        int zuffZahl = (int) Math.round(Math.random() * 50);

        if (Zwinker == 1) {
            Zwinker = 0;
        } else {
            if (zuffZahl > 45) {
                Zwinker = 1;
            }
        }

        drawHimWithStick(offGraph, 150);
    }

    // hier gibt er die Karte an Krabat
    public void drawMlynkWithKarte(GenericDrawingContext offGraph) {
        drawHim(offGraph, 20);
    }

    // Lasse Krabat in eine bestimmte Richtung schauen (nach Uhrzeit!)
    public void setFacing(int direction) {
        switch (direction) {
            case 3:
                isAnimHorizontal = true;
                directionX = RIGHT;
                break;
            case 6:
                isAnimHorizontal = false;
                directionY = DOWN;
                break;
            case 9:
                isAnimHorizontal = true;
                directionX = LEFT;
                break;
            case 12:
                isAnimHorizontal = false;
                directionY = UP;
                break;
            default:
                log.debug("Falsche Uhrzeit zum Witzereissen!");
        }
    }

    // Richtung, in die Krabat schaut, ermitteln (wieder nach Uhrzeit)
    public int getFacing() {
        if (isAnimHorizontal) {
            return directionX == RIGHT ? 3 : 9;
        } else {
            return directionY == DOWN ? 6 : 12;
        }
    }


    // Zeichne Mlynk beim Sprechen mit anderen Personen (normal)
    public void talkMlynk(GenericDrawingContext offGraph) {
        if (--Verhinderkopf < 1) {
            Verhinderkopf = MAX_VERHINDERKOPF;

            Head = (int) Math.round(Math.random() * 5);
            if (Head == 5) {
                Head = 0;
            }
        }

        if (Body == 3) {
            Body = 0;
        }

        if (--Verhinderbody < 1) {
            Verhinderbody = MAX_VERHINDERBODY;
            Body = (int) Math.round(Math.random() * 3);
            if (Body == 3) {
                Body = 0;
            }
        }

        int tFacing = getFacing();
        if (tFacing == 12) {
            log.debug("Nach hinten kann er nicht reden !!!");
            tFacing = 9;
        }

        // einen Kopf einsparen bei zur seite reden
        if (tFacing != 6 && Head == 4) {
            Head = 0;
        }

        tFacing *= 10;

        drawHim(offGraph, tFacing);
    }

    // hat Stock immer nach links erhoben fuers Verzaubern
    public void talkMlynkWithKij(GenericDrawingContext offGraph) {
        // Hier muss nur der Kopf bewegt werden, sonst ni#ko
        if (--Verhinderkopf < 1) {
            Verhinderkopf = MAX_VERHINDERKOPF;

            Head = (int) Math.round(Math.random() * 4);
            if (Head == 4) {
                Head = 0;
            }
        }

        drawHimWithStick(offGraph, 180);
    }

    public void talkMlynkWithKijAndersrum(GenericDrawingContext offGraph, boolean stockIstImmerOben) {
        // Kopf verschieben
        if (--Verhinderkopf < 1) {
            Verhinderkopf = MAX_VERHINDERKOPF;
            Head = (int) (Math.random() * 3.9);
        }

        // Koerper verschieben
        if (--Verhinderbody < 1) {
            Verhinderbody = MAX_VERHINDERBODY;
            Body = (int) (Math.random() * 3.9);
        }

        if (stockIstImmerOben) {
            Body = 3;
        }

        // Malen und gut
        drawHimWithStickInverse(offGraph);

    }

    public GenericPoint evalMlynkTalkPoint() {
        // Hier Position des Textes berechnen
        BorderRect temp = getBoundingBox();
        return new GenericPoint((temp.bottomRightPoint.x + temp.topLeftPoint.x) / 2, temp.topLeftPoint.y - 50);
    }

    // Zooming-Variablen berechnen
    @Override
    protected int getLeftPos(int x, int y) {
        return calcLeftPosDefault(x, y, scaleFactor);
    }

    // hier beachten, dass er einen Stock in der Hand haelt !!!!!!
    private int getLeftPosWithKij(int pox, int poy) {
        // Skalierungsfaktor holen
        float fScaleY = getScale(poy) * scaleVerhaeltnisStock;
        return pox - (COFFSET_STOCK - (int) fScaleY) / 2;
    }

    @Override
    protected int getUpPos(int y) {
        return calcUpPosDefault(y);
    }

    @Override
    protected int getScale(int y) {
        return calcScaleDefault(y, defaultScale);
    }

    // Clipping - Region vor Zeichnen von Krabat setzen, hier mit Stock in der Hand
    private void krabatClipWithKij(GenericDrawingContext g, int xx, int yy) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPosWithKij(xx, yy);
        int y = getUpPos(yy);
        // System.out.println(xx +  " " + x);

        // Breite und Hoehe ermitteln
        int xd = 2 * (xx - x);
        int yd = yy - y;
        g.setClip(x, y, xd, yd);
    }

    // Clipping-Region wird gross gesetzt, denn man weiss ja nie, ob er den Stock in der Hand haelt oder nicht
    private void krabatClipWithKijInverse(GenericDrawingContext g, int xx, int yy) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos(xx, yy);
        int y = getUpPos(yy);
        // System.out.println(xx +  " " + x);

        // Skalierungsfaktor holen

        float fScaleY = getScale(yy) * scaleVerhaeltnisStock;

        // Breite und Hoehe ermitteln
        int xd = x + CWIDTH_STOCK - (int) fScaleY;
        int yd = yy - y;

        g.setClip(x, y, xd, yd);
    }

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet, wenn er den Stock hat !!!
    public BorderRect mlynkRectWithStick() {
        int x = getLeftPosWithKij((int) posX, (int) posY);
        int y = getUpPos((int) posY);
        int xd = 2 * ((int) posX - x) + x;
        int yd = (int) posY;
        return new BorderRect(x, y, xd, yd);
    }

    public BorderRect mlynkRectWithStickInverse() {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos((int) posX, (int) posY);
        int y = getUpPos((int) posY);
        // System.out.println(xx +  " " + x);

        // Skalierungsfaktor holen

        float fScaleY = getScale((int) posY) * scaleVerhaeltnisStock;

        // Breite und Hoehe ermitteln
        int xd = x + CWIDTH_STOCK - (int) fScaleY;
        int yd = (int) posY;

        return new BorderRect(x, y, xd, yd);
    }

    // ganz normales Darstellen
    private void drawHim(GenericDrawingContext g, int Richtung) {
        // Clipping - Region setzen
        krabatClipDefault(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // Figur zeichnen
        switch (Richtung) {
            case 3: // nach rechts gehen
                g.drawImage(krabat_stand_right_head[Zwinker], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_stand_right_body[0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            case 6: // nach vorn gehen
                g.drawImage(krabat_stand_front_head[Zwinker], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_stand_front_body[0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            case 9: // nach links gehen
                g.drawImage(krabat_stand_left_head[Zwinker], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_stand_left_body[0], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            case 12: // nach hinten gehen
                g.drawImage(krabat_back[animPos], left, up, Koerperbreite, Kopfhoehe + Koerperhoehe);
                break;

            case 20: // Karte geben, nach vorn schauen
                g.drawImage(krabat_stand_front_head[Zwinker], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_gib_karte, left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            case 30: // nach rechts reden
                g.drawImage(krabat_talk_right_head[Head], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_talk_right_body[Body], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            case 60: // nach vorn reden
                g.drawImage(krabat_talk_front_head[Head], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_talk_front_body[Body], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            case 90: // nach links reden
                g.drawImage(krabat_talk_left_head[Head], left, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_talk_left_body[Body], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
                break;

            default:
                log.error("Falsche Richtung fuer Mueller angegeben! Richtung = {}", Richtung);
                break;
        }
    }

    // Stock nach links und immer oben
    private void drawHimWithStick(GenericDrawingContext g, int Richtung) {
        // Clipping - Region setzen
        krabatClipWithKij(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPosWithKij((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        // hier die Extrawurst, wenn er den Stock hat
        int KoerperbreiteMitStock = CWIDTH_STOCK - scale;
        int OffsetMitStock = getLeftPos((int) posX, (int) posY) - getLeftPosWithKij((int) posX, (int) posY);

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // System.out.println ("Zoomfaktor : " + scale + " Offset : " + OffsetMitStock);

        // Figur zeichnen
        switch (Richtung) {
            case 150: // nach links schauen mit erhobenem Stock
                g.drawImage(krabat_stand_left_head[Zwinker], left + OffsetMitStock, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_hat_stock, left, up + Kopfhoehe, KoerperbreiteMitStock, Koerperhoehe);
                break;

            case 180: // nach links reden mit erhobenem Stock
                g.drawImage(krabat_talk_left_head[Head], left + OffsetMitStock, up, Koerperbreite, Kopfhoehe);
                g.drawImage(krabat_hat_stock, left, up + Kopfhoehe, KoerperbreiteMitStock, Koerperhoehe);
                break;

            default:
                log.error("Falsche Richtung fuer Mueller angegeben! Richtung = {}", Richtung);
                break;
        }
    }

    // hier soll er den Stock nur ab und zu hochhalten
    private void drawHimWithStickInverse(GenericDrawingContext g) {
        // Clipping - Region setzen
        krabatClipWithKijInverse(g, (int) posX, (int) posY);

        // Groesse und Position der Figur berechnen
        int left = getLeftPos((int) posX, (int) posY);
        int up = getUpPos((int) posY);
        int scale = getScale((int) posY);

        // hier die Breiten und Hoehenscalings fuer Kopf und Body berechnen
        float fBodyoffset = BODYOFFSET;
        float fHoehe = CHEIGHT;

        float fScaleY = (float) scale * scaleFactor;
        int Koerperbreite = CWIDTH - (int) fScaleY;
        int Kopfhoehe = (int) (fBodyoffset - (float) scale * (fBodyoffset / fHoehe));
        int Koerperhoehe = (int) (fHoehe - scale - Kopfhoehe);

        // hier die Extrawurst, wenn er den Stock hat
        int KoerperbreiteMitStock = CWIDTH_STOCK - scale;
        // int OffsetMitStock        = getLeftPos ( ((int) xps), ((int) yps) ) - getLeftPosWithKij ( ((int) xps), ((int) yps) );

        // System.out.println ("Mueller ist " + Koerperbreite + " breit und Kopf " + Kopfhoehe + " und Body " + Koerperhoehe + " hoch.");

        // System.out.println ("Zoomfaktor : " + scale + " Offset : " + OffsetMitStock);

        // Figur zeichnen
        g.drawImage(krabat_talk_right_head[Head], left, up, Koerperbreite, Kopfhoehe);

        if (Body == 3) {
            g.drawImage(krabat_hat_stock_andersrum, left, up + Kopfhoehe, KoerperbreiteMitStock, Koerperhoehe);
        } else {
            g.drawImage(krabat_talk_right_body[Body], left, up + Kopfhoehe, Koerperbreite, Koerperhoehe);
        }

    }
}