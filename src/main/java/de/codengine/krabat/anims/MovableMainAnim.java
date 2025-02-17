package de.codengine.krabat.anims;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.Borderrect;
import de.codengine.krabat.main.GenericPoint;

import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;

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
}
