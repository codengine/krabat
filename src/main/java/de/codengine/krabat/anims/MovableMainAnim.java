package de.codengine.krabat.anims;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public abstract class MovableMainAnim extends Mainanim {
    protected final int width;
    protected final int height;
    protected final float scaleFactor;
    // Grundlegende Variablen
    protected float xps;
    protected float yps;               // genaue Position der Fuesse fuer Offsetberechnung
    protected float txps;
    protected float typs;             // temporaere Variablen fuer genaue Position
    protected int anim_pos = 0;             // Animationsbild
    public boolean clearanimpos = true;  // Bewirkt Standsprite nach Laufen

    // Variablen fuer Bewegung und Richtung
    protected GenericPoint walkto = new GenericPoint(0, 0);                 // Zielpunkt fuer Move()
    protected GenericPoint Twalkto = new GenericPoint(0, 0);                // Zielpunkt, der in MoveTo() gesetzt und von Move uebernommen wird
    // hier ist das Problem der Threadsynchronisierung !!!!!!!
    protected DirectionX directionX = RIGHT;          // Laufrichtung x
    protected DirectionX tDirectionX = RIGHT;

    protected DirectionY directionY = DOWN;          // Laufrichtung y
    protected DirectionY tDirectionY = DOWN;

    protected boolean horizontal = true;    // Animationen in x oder y Richtung
    protected boolean Thorizontal = true; // Animationen in x oder y Richtung

    public boolean upsidedown = false;   // Beim Berg - und Tallauf GenericImage wenden
    public int minx;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...
    // Variablen fuer Zooming
    public int maxx;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomf;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)

    public int defScale;                  // definiert maximale Groesse von Krabat bei x > maxx

    public MovableMainAnim(Start caller, int width, int height) {
        super(caller);
        this.width = width;
        this.height = height;
        this.scaleFactor = (float) width / height;
    }

    protected abstract int getLeftPos(int pox, int poy);
    protected abstract int getUpPos(int poy);
    protected abstract int getScale(int poy);

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    public Borderrect getRect() {
        int x = getLeftPos((int) xps, (int) yps);
        int y = getUpPos((int) yps);
        int xd = 2 * ((int) xps - x) + x;
        int yd = (int) yps;
        return new Borderrect(x, y, xd, yd);
    }

    protected int calcLeftPosDefault(int pox, int poy) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int scaleY = getScale(poy);
        return pox - (width - scaleY / 2) / 2;
    }

    protected int calcLeftPosDefault(int pox, int poy, float scaleFactor) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        float fScaleY = getScale(poy) * scaleFactor;
        return pox - (width - (int) fScaleY) / 2;
    }

    protected int calcUpPosDefault(int poy) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        int fScaleY = getScale(poy);
        return poy - height + fScaleY;
    }

    protected int calcScaleDefault(int poy) {
        return calcScaleDefault(poy, 0);
    }

    protected int calcScaleDefault(int poy, int defScale) {
        // Ermittlung der Hoehendifferenz beim Zooming
        if (!upsidedown) {
            // normale Berechnung
            float helper = (maxx - poy) / zoomf;
            if (helper < 0) {
                helper = 0;
            }
            helper += defScale;
            return (int) helper;
        } else {
            // Berechnung bei "upsidedown" - Berg/Tallauf
            float help2 = (poy - minx) / zoomf;
            if (help2 < 0) {
                help2 = 0;
            }
            help2 += defScale;
            return (int) help2;
        }
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    protected void krabatClipDefault(GenericDrawingContext g, int xx, int yy) {
        krabatClipDefault(g, xx, yy, 1);
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    protected void krabatClipDefault(GenericDrawingContext g, int xx, int yy, int ydFactor) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos(xx, yy);
        int y = getUpPos(yy);

        // Breite und Hoehe ermitteln
        int xd = 2 * (xx - x);
        int yd = ydFactor * (yy - y);
        g.setClip(x, y, xd, yd);

        // Fuer Debugging ClipRectangle zeichnen
        // g.setColor(Color.white);
        // g.drawRect(x, y, xd - 1, yd - 1);
        // System.out.println(x + " " + y + " " + xd + " " + yd);
    }

    protected boolean calcHorizontal(GenericPoint aim, int maxAngle) {
        // Horizontal oder verikal laufen ?
        if (aim.x != (int) xps) {
            // Winkel berechnen, den Krabat laufen soll
            double yangle = Math.abs(aim.y - (int) yps);
            double xangle = Math.abs(aim.x - (int) xps);
            double angle = Math.atan(yangle / xangle);
            return !(angle > maxAngle * Math.PI / 180);
        }

        return false;
    }

    protected void verschiebeXdefault(int dist, int slowX) {
        // Skalierungsfaktor holen
        int scale = getScale((int) yps);

        // Zooming - Faktor beruecksichtigen in x - Richtung
        float horizDist = dist - (float) scale / slowX;
        if (horizDist < 1) {
            horizDist = 1;
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(xps - walkto.x) / horizDist;

        typs = yps;
        if (z != 0) {
            typs += directionY.getVal() * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + directionX.getVal() * horizDist;
    }

    // Horizontal - Positions - Verschieberoutine
    protected void verschiebeXdefault(float horizDist) {
        // Zooming - Faktor beruecksichtigen in x - Richtung
        if (horizDist < 1) {
            horizDist = 1;
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(xps - walkto.x) / horizDist;

        typs = yps;
        if (z != 0) {
            typs += directionY.getVal() * (Math.abs(yps - walkto.y) / z);
        }

        txps = xps + directionX.getVal() * horizDist;
    }

    // Vertikal - Positions - Verschieberoutine
    protected void verschiebeYdefault(int dist, int slowY) {
        // Skalierungsfaktor holen
        int scale = getScale((int) yps);

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float vertDist = dist - (float) scale / slowY;
        if (vertDist < 1) {
            vertDist = 1;
            // hier kann noch eine Entscheidungsroutine hin, die je nach Animationsphase
            // und vert_distance ein Pixel erlaubt oder nicht
        }

        verschiebeY(vertDist);
    }

    protected void verschiebeY(float vertDist) {
        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(yps - walkto.y) / vertDist;

        txps = xps;
        if (z != 0) {
            txps += directionX.getVal() * (Math.abs(xps - walkto.x) / z);
        }

        typs = yps + directionY.getVal() * vertDist;
        // System.out.println(xps + " " + txps + " " + yps + " " + typs);
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void setPos(GenericPoint aim) {
        xps = aim.x;        // Float - Variablen initialisieren
        yps = aim.y;
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    public GenericPoint getPos() {
        //System.out.println(" Aktuelle Pos : "+pos_x+" "+pos_y);
        return new GenericPoint((int) xps, (int) yps);
    }

    protected void moveToDefault(GenericPoint aim) {
        // Variablen an Move uebergeben
        Twalkto = aim;

        // Laufrichtung ermitteln
        tDirectionX = aim.x > (int) xps ? RIGHT : LEFT;
        tDirectionY = aim.y > (int) yps ? DOWN : UP;
    }
}
