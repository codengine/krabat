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

package de.codengine.krabat.main;


public class BorderTrapezoid {
    public final int x1;
    public final int x2;
    public final int x3;
    public final int x4;
    public final int y1;
    public final int y2;

    // x1 = lo, x2 = ro, x3 = lu, x4 = ru, y1 = o, y2 = u
    public BorderTrapezoid(int x1, int x2, int x3, int x4, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y1 = y1;
        this.y2 = y2;
    }

    public BorderTrapezoid(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x1;
        this.x4 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    // Hier wird ermittelt, ob Punkt innerhalb des Trapezes ist
    public boolean pointInside(GenericPoint point) {
        // Zuerst wird Rechteck um Viereck berechnet
        int lox = Math.min(x3, x1);

        int hix = Math.max(x2, x4);

        // Hier erfolgt Abfrage, ob Punkt innerhalb des Vierecks oder nicht
        if (point.x < lox || point.x > hix || point.y < y1 || point.y > y2) {
            return false;
        }

        GenericPoint temp = points(point.y);

        // Abfrage, ob Punkt innerhalb
        return temp.x <= point.x && temp.y >= point.x;
    }

    // Hier wird der kuerzeste Abstand des Punktes zum Trapez ueberhaupt ermittelt
    public int centerDistance(GenericPoint point) {
        GenericPoint pTemp = edgePoint(point);
        int xdist = Math.abs(point.x - pTemp.x);
        int ydist = Math.abs(point.y - pTemp.y);
        return (int) Math.sqrt(xdist * xdist + ydist * ydist);
    }

    // Routine, die zu gegebener Y-Koordinate nach Anstiegsgleichung
    // die linke und rechte X-Koordinate des Trapezes ermittelt
    public GenericPoint points(int y) {
        float yy1 = y1;
        float yy2 = y2;

        // hier werden die Anstiegsoffsets berechnet
        float leftoff = ((float) x1 - (float) x3) / (yy2 - yy1);
        float rightoff = ((float) x2 - (float) x4) / (yy2 - yy1);
        // ein positiver Offset gibt positiven Anstieg an

        // hier wird die X-Koordinate der schraegen Trapezlinien an der Y-Position des Punktes berechnet
        int leftx = (int) (x3 + leftoff * (y2 - y));
        int rightx = (int) (x4 + rightoff * (y2 - y));

        return new GenericPoint(leftx, rightx);
    }

    public synchronized GenericPoint edgePoint(GenericPoint point) {
        // Zuerst testen, ob nicht doch innerhalb gelegen
        if (pointInside(point)) {
            return new GenericPoint(point.x, point.y);
        }

        // Zur Anstiegsberechnung muessen Koordinaten in float umgewandelt werden
        float xx1 = x1;
        float xx2 = x2;
        float xx3 = x3;
        float xx4 = x4;
        float yy1 = y1;
        float yy2 = y2;

        // Berechnung, ob Punkt rechts, links, oben, unten des Trapezes ist
        // -> welche Seite ist ueberhaupt relevant...

        // wenn Punkt genau ueber Gerade des Trapezes, dann keine Berechnung notwendig
        if (point.y < yy1 && point.x > xx1 && point.x < xx2) {
            return new GenericPoint(point.x, y1);
        }

        // Wenn Punkt genau unter Gerade des Trapezes, dann keine Berechnung notwendig
        if (point.y > yy2 && point.x > xx3 && point.x < xx4) {
            return new GenericPoint(point.x, y2);
        }


        boolean isLeft = false;
        // Feststellen, ob Punkt eindeutig links oder rechts vom Trapez ueber Viereckvergleich
        if (point.x < xx1 && point.x < xx3) {
            isLeft = true;
        } else {
            if (point.x <= xx2 || point.x <= xx4) {
                // Gerade mitten durch Trapez berechnen fuer rechts/links - Auswahl

                // Fixpunkte in der Mitte der Waagerechten
                float upx = (xx1 + xx2) / 2;
                float dox = (xx3 + xx4) / 2;

                // Anstieg
                float ans = (upx - dox) / (yy2 - yy1);
                // System.out.println("Anstieg : " + ans);

                // Geradenpunkt bei aktueller Y - Koordinate
                float aktx = dox + (yy2 - point.y) * ans;
                // System.out.println("X1 : " + xx1 + "X3 : " + xx3 + "Geradenpunkt : " + aktx);

                // Punkt links von Trapez
                isLeft = aktx > point.x;
            }
        }

        if (isLeft) {
            // System.out.print("Punkt ist links vom Trapez!");

            // Anstiege der linken Schraege berechnen
            float ml;
            if (xx1 - xx3 != 0) {
                ml = (yy2 - yy1) / (xx1 - xx3);
            } else {
                // es handelt sich um senkrechte Seite - Koordinaten koennen sofort bestimmt werden

                // System.out.print("Linke Seite ist senkrecht!");

                // Punkt ist oberhalb, oberen Eckpunkt zurueckgeben
                if (point.y < yy1) {
                    return new GenericPoint(x1, y1);
                }

                // Punkt ist unterhalb, unteren Eckpunkt zurueckgeben
                if (point.y > yy2) {
                    return new GenericPoint(x3, y2);
                }

                // Punkt muss auf Hoehe Rechteck sein, angepasste Koordinaten zurueckgeben
                return new GenericPoint(x1, point.y);
            }
            // System.out.print("Anstieg " + ml);

            // Jetzt muss noch der Y-Offset berechnet werden fuer die Geradengleichung
            float nl = yy1 + xx1 * ml;
            // System.out.println("Y-Offset Trapez " + nl);

            // Hier folgt der Offset fuer den Punkt (reziproker negativer Anstieg)
            float npl = point.y + point.x * (-1 / ml);
            // System.out.print("Y-Offset Punkt " + npl);

            // nun alles ins geloeste Gleichungssystem fuer Schnittpunkt 2-er Geraden einsetzen

            // Schnittpunkt mit linker Gerade
            float xl = (npl - nl) / (ml + 1 / ml);
            float yl = nl + ml * xl;
            xl = -xl;
            // System.out.print("Aktuell " + xl + " " + yl);

            // testen, ob Schnittpunkt innerhalb erlaubter Menge des Trapezes
            if (yl > yy1 && yl < yy2) {
                // Schnittpunkt ist innerhalb - Trapezpunkt ermitteln (Rundungsfehler!!!)
                GenericPoint tmp = new GenericPoint((int) xl, (int) yl);

                // Solange testen, bis sich Punkt innerhalb ergibt
                while (!pointInside(tmp) && tmp.x < 1300) {
                    tmp.x++;
                }
                return tmp;
            } else {
                // Schnittpunkt ist ausserhalb, Eckpunkt zurueckgeben

                // oberer Punkt
                if (yl < yy1) {
                    return new GenericPoint(x1, y1);
                }

                // unterer Punkt
                else {
                    return new GenericPoint(x3, y2);
                }
            }
        }

        // Punkt rechts vom Trapez
        else {
            // System.out.print("Punkt ist rechts vom Trapez!");

            // Anstieg der rechten Schraege berechnen
            float mr;
            if (xx2 - xx4 != 0) {
                mr = (yy2 - yy1) / (xx2 - xx4);
            } else {
                // es handelt sich um senkrechte Seite - Koordinaten koennen sofort bestimmt werden

                // System.out.print("Rechte Seite ist senkrecht!");

                // Punkt ist oberhalb, oberen Eckpunkt zurueckgeben
                if (point.y < yy1) {
                    return new GenericPoint(x2, y1);
                }

                // Punkt ist unterhalb, unteren Eckpunkt zurueckgeben
                if (point.y > yy2) {
                    return new GenericPoint(x4, y2);
                }

                // Punkt muss auf Hoehe Rechteck sein, angepasste Koordinaten zurueckgeben
                return new GenericPoint(x2, point.y);
            }
            // System.out.print("Anstieg " + mr);

            // Jetzt muss noch der Y-Offset berechnet werden fuer die Geradengleichung
            float nr = yy1 + xx2 * mr;
            // System.out.println("Y-Offset Trapez " + nr);

            // Hier folgen die Offsets fuer den Punkt (reziproker negativer Anstieg)
            float npr = point.y + point.x * (-1 / mr);
            // System.out.println("Y-Offset Punkt " + npr);

            // nun alles ins geloeste Gleichungssystem fuer Schnittpunkt 2-er Geraden einsetzen

            // Schnittpunkt mit rechter Gerade
            float xr = (npr - nr) / (mr + 1 / mr);
            float yr = nr + mr * xr;
            xr = -xr;
            // System.out.print("Aktuell " + xr + " " + yr);

            // testen, ob Schnittpunkt innerhalb erlaubter Menge des Trapezes
            if (yr > yy1 && yr < yy2) {
                // Schnittpunkt ist innerhalb - Trapezpunkt ermitteln (Rundungsfehler!!!)
                GenericPoint tmp = new GenericPoint((int) xr, (int) yr);

                // Solange testen, bis sich Punkt innerhalb ergibt
                while (!pointInside(tmp) && tmp.x > -1) {
                    tmp.x--;
                }
                return tmp;
            } else {
                // Schnittpunkt ist ausserhalb, Eckpunkt zurueckgeben

                // oberer Punkt
                if (yr < yy1) {
                    return new GenericPoint(x2, y1);
                }

                // unterer Punkt
                else {
                    return new GenericPoint(x4, y2);
                }
            }
        }
    }

    // Hier wird der Flaecheninhalt des Trapezes in Pixeln wiedergegeben
    public int surfaceArea() {
        int xoffset = (x3 + x4) / 2 - (x1 + x3) / 2;
        int yoffset = y2 - y1;
        return xoffset * yoffset;
    }
}		     