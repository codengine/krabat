package de.codengine.krabat.anims;

import de.codengine.krabat.Start;
import de.codengine.krabat.main.BorderRect;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericDrawingContext;

import static de.codengine.krabat.anims.DirectionX.LEFT;
import static de.codengine.krabat.anims.DirectionX.RIGHT;
import static de.codengine.krabat.anims.DirectionY.DOWN;
import static de.codengine.krabat.anims.DirectionY.UP;

public abstract class MovableMainAnim extends MainAnim {
    protected final int width;
    protected final int height;
    protected final float scaleFactor;
    // Grundlegende Variablen
    protected float posX;
    protected float posY;               // genaue Position der Fuesse fuer Offsetberechnung
    protected float tempPosX;
    protected float tempPosY;             // temporaere Variablen fuer genaue Position
    protected int animPos = 0;             // Animationsbild
    public boolean resetAnimPos = true;  // Bewirkt Standsprite nach Laufen

    // Variablen fuer Bewegung und Richtung
    protected GenericPoint walkTo = new GenericPoint(0, 0);                 // Zielpunkt fuer move()
    protected GenericPoint tmpWalkTo = new GenericPoint(0, 0);                // Zielpunkt, der in moveTo() gesetzt und von Move uebernommen wird
    // hier ist das Problem der Threadsynchronisierung !!!!!!!
    protected DirectionX directionX = RIGHT;          // Laufrichtung x
    protected DirectionX tmpDirectionX = RIGHT;

    protected DirectionY directionY = DOWN;          // Laufrichtung y
    protected DirectionY tmpDirectionY = DOWN;

    protected boolean isAnimHorizontal = true;    // Animationen in x oder y Richtung
    protected boolean tmpIsAnimHorizontal = true; // Animationen in x oder y Richtung

    public boolean upsideDown = false;   // Beim Berg - und Tallauf GenericImage wenden
    public int minX;                      // "Falschherum" - X - Koordinate, damit Scaling wieder stimmt...
    // Variablen fuer Zooming
    public int maxX;                      // X - Koordinate, bis zu der nicht gezoomt wird
    // (Vordergrund) bildabhaengig
    public float zoomFactor;                   // gibt an, wie stark gezoomt wird, wenn Figur in
    // den Hintergrund geht (bildabhaengig)

    public int defaultScale;                  // definiert maximale Groesse von Krabat bei x > maxx

    public MovableMainAnim(Start caller, int width, int height) {
        super(caller);
        this.width = width;
        this.height = height;
        this.scaleFactor = (float) width / height;
    }

    protected abstract int getLeftPos(int x, int y);

    protected abstract int getUpPos(int y);

    protected abstract int getScale(int y);

    // Routine, die BorderRect zurueckgibt, wo sich Krabat gerade befindet
    public BorderRect getBoundingBox() {
        int x = getLeftPos((int) posX, (int) posY);
        int y = getUpPos((int) posY);
        int xd = 2 * ((int) posX - x) + x;
        int yd = (int) posY;
        return new BorderRect(x, y, xd, yd);
    }

    protected int calcLeftPosDefault(int x, int y) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        int scaleY = getScale(y);
        return x - (width - scaleY / 2) / 2;
    }

    protected int calcLeftPosDefault(int x, int y, float scaleFactor) {
        // Linke x-Koordinate = Fusspunkt - halbe Breite
        // + halbe Hoehendifferenz
        float scaleY = getScale(y) * scaleFactor;
        return x - (width - (int) scaleY) / 2;
    }

    protected int calcUpPosDefault(int y) {
        // obere y-Koordinate = untere y-Koordinate - konstante Hoehe
        // + Hoehendifferenz
        int scaleY = getScale(y);
        return y - height + scaleY;
    }

    protected int calcScaleDefault(int y) {
        return calcScaleDefault(y, 0);
    }

    protected int calcScaleDefault(int y, int defaultScale) {
        // Ermittlung der Hoehendifferenz beim Zooming
        if (!upsideDown) {
            // normale Berechnung
            float helper = (maxX - y) / zoomFactor;
            if (helper < 0) {
                helper = 0;
            }
            helper += defaultScale;
            return (int) helper;
        } else {
            // Berechnung bei "upsidedown" - Berg/Tallauf
            float help2 = (y - minX) / zoomFactor;
            if (help2 < 0) {
                help2 = 0;
            }
            help2 += defaultScale;
            return (int) help2;
        }
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    protected void krabatClipDefault(GenericDrawingContext drawingContext, int x, int y) {
        krabatClipDefault(drawingContext, x, y, 1);
    }

    // Clipping - Region vor Zeichnen von Krabat setzen
    protected void krabatClipDefault(GenericDrawingContext drawingContext, int xx, int yy, int verticalDisplacementFactor) {
        // Links - oben - Korrdinaten ermitteln
        int x = getLeftPos(xx, yy);
        int y = getUpPos(yy);

        // Breite und Hoehe ermitteln
        int xd = 2 * (xx - x);
        int yd = verticalDisplacementFactor * (yy - y);
        drawingContext.setClip(x, y, xd, yd);
    }

    protected boolean calcHorizontal(GenericPoint targetPoint, int maxAngle) {
        // Horizontal oder verikal laufen ?
        if (targetPoint.x != (int) posX) {
            // Winkel berechnen, den Krabat laufen soll
            double yAngle = Math.abs(targetPoint.y - (int) posY);
            double xAngle = Math.abs(targetPoint.x - (int) posX);
            double angle = Math.atan(yAngle / xAngle);
            return !(angle > maxAngle * Math.PI / 180);
        }

        return false;
    }

    protected void moveXdefault(int distance, int slowdownFactor) {
        // Skalierungsfaktor holen
        int scale = getScale((int) posY);

        // Zooming - Faktor beruecksichtigen in x - Richtung
        float horizontalDistance = distance - (float) scale / slowdownFactor;
        if (horizontalDistance < 1) {
            horizontalDistance = 1;
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(posX - walkTo.x) / horizontalDistance;

        tempPosY = posY;
        if (z != 0) {
            tempPosY += directionY.getVal() * (Math.abs(posY - walkTo.y) / z);
        }

        tempPosX = posX + directionX.getVal() * horizontalDistance;
    }

    // Horizontal - Positions - Verschieberoutine
    protected void moveXdefault(float horizontalDistance) {
        // Zooming - Faktor beruecksichtigen in x - Richtung
        if (horizontalDistance < 1) {
            horizontalDistance = 1;
        }

        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(posX - walkTo.x) / horizontalDistance;

        tempPosY = posY;
        if (z != 0) {
            tempPosY += directionY.getVal() * (Math.abs(posY - walkTo.y) / z);
        }

        tempPosX = posX + directionX.getVal() * horizontalDistance;
    }

    // Vertikal - Positions - Verschieberoutine
    protected void moveYdefault(int distance, int slowdownFactor) {
        // Skalierungsfaktor holen
        int scale = getScale((int) posY);

        // Zooming - Faktor beruecksichtigen in y-Richtung
        float verticalDistance = distance - (float) scale / slowdownFactor;
        if (verticalDistance < 1) {
            verticalDistance = 1;
            // hier kann noch eine Entscheidungsroutine hin, die je nach Animationsphase
            // und vert_distance ein Pixel erlaubt oder nicht
        }

        moveY(verticalDistance);
    }

    protected void moveY(float verticalDistance) {
        // Verschiebungsoffset berechnen (fuer schraege Bewegung)
        float z = Math.abs(posY - walkTo.y) / verticalDistance;

        tempPosX = posX;
        if (z != 0) {
            tempPosX += directionX.getVal() * (Math.abs(posX - walkTo.x) / z);
        }

        tempPosY = posY + directionY.getVal() * verticalDistance;
    }

    // Krabat an bestimmte Position setzen incl richtigem Zoomfaktor (Fuss-Koordinaten angegeben)
    public void setPos(GenericPoint targetPoint) {
        posX = targetPoint.x;        // Float - Variablen initialisieren
        posY = targetPoint.y;
    }

    // Krabats Position ermitteln incl richtigem Zoomfaktor (Ausgabe der Fuss-Koordinaten)
    public GenericPoint getPos() {
        return new GenericPoint((int) posX, (int) posY);
    }

    protected void moveToDefault(GenericPoint targetPoint) {
        // Variablen an Move uebergeben
        tmpWalkTo = targetPoint;

        // Laufrichtung ermitteln
        tmpDirectionX = targetPoint.x > (int) posX ? RIGHT : LEFT;
        tmpDirectionY = targetPoint.y > (int) posY ? DOWN : UP;
    }
}
