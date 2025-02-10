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

package rapaki.krabat.main;


public class Bordertrapez {
    public int x1, x2, x3, x4, y1, y2;

    // x1 = lo, x2 = ro, x3 = lu, x4 = ru, y1 = o, y2 = u
    public Bordertrapez(int x1, int x2, int x3, int x4, int y1, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Bordertrapez(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x1;
        this.x4 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    // Hier wird ermittelt, ob Punkt innerhalb des Trapezes ist
    public boolean PointInside(GenericPoint der) {
        // Zuerst wird Rechteck um Viereck berechnet
        int lox;
        if (x3 > x1) {
            lox = x1;
        } else {
            lox = x3;
        }

        int hix;
        if (x2 > x4) {
            hix = x2;
        } else {
            hix = x4;
        }

        // Hier erfolgt Abfrage, ob Punkt innerhalb des Vierecks oder nicht
        if ((der.x < lox) || (der.x > hix) || (der.y < y1) || (der.y > y2)) {
            // System.out.println(der.x + " " + der.y + " outside " + lox + " " + hix + " " + y1 + " " + y2);
            return false;
        }

        GenericPoint temp = Punkte(der.y);

        // Abfrage, ob Punkt innerhalb
        if ((temp.x > der.x) || (temp.y < der.x)) {
            // System.out.println("No Trapez !");
            return false;
        } else {
            // System.out.println("Inside !");
            return true;
        }
    }

    // Hier wird der kuerzeste Abstand des Punktes zum Trapez ueberhaupt ermittelt
    public int CenterDistance(GenericPoint dieser) {
        GenericPoint Ptemp = RandPunkt(dieser);
        int xdist = Math.abs(dieser.x - Ptemp.x);
        int ydist = Math.abs(dieser.y - Ptemp.y);
        int dist = (int) Math.sqrt((xdist * xdist) + (ydist * ydist));
        return dist;
    }

    // Hier wird der beste Punkt innerhalb eines Trapezes ermittelt,
    // falls der Punkt ausserhalb ist
	/* public synchronized GenericPoint RandPunkt (GenericPoint dieser)
	{
	  // System.out.println("This GenericPoint : " + dieser.x + " " + dieser.y);
	  
	  // Test, ob Punkt innerhalb und tschuess
	  if (PointInside (dieser) == true) 
	  {
	    // System.out.println("Falscher BestPoint Aufruf!");
	  	return (new GenericPoint (dieser.x, dieser.y));
	  }
	  
	  // falls Punkt oberhalb Trapez
	  if (dieser.y < y1)
	  {
	  	// oberhalb und links
	  	if (dieser.x < x1) return (new GenericPoint (x1, y1));
	  	// oberhalb und rechts
	  	if (dieser.x > x2) return (new GenericPoint (x2, y1));
	  	// oberhalb und dazwischen
	  	return (new GenericPoint (dieser.x, y1));
	  }
	  // falls Punkt unterhalb Trapez
	  if (dieser.y > y2)
	  {
	  	// unterhalb und links
	  	if (dieser.x < x3) return (new GenericPoint (x3, y2));
	  	// unterhalb und rechts
	  	if (dieser.x > x4) return (new GenericPoint (x4, y2));
	  	// unterhalb und dazwischen
	  	return (new GenericPoint (dieser.x, y2));
	  }
	  // X-Koordinaten des Trapezes zur Y-Koordinate holen
	  GenericPoint temp = Punkte (dieser.y);
	  // wenn Punkt links vom Trapez
	  if (dieser.x <= temp.x) return (new GenericPoint (temp.x, dieser.y));			 
	  // wenn Punkt rechts vom Trapez
	  if (dieser.x >= temp.y) return (new GenericPoint (temp.y, dieser.y));
	  
	  System.out.println("Fehler bei BestPointBerechnung !!");
	  return (new GenericPoint (0, 0)); 
	}	*/

    // Routine, die zu gegebener Y-Koordinate nach Anstiegsgleichung
    // die linke und rechte X-Koordinate des Trapezes ermittelt
    public GenericPoint Punkte(int ykoord) {
        float xx1 = x1;
        float xx2 = x2;
        float xx3 = x3;
        float xx4 = x4;
        float yy1 = y1;
        float yy2 = y2;

        // hier werden die Anstiegsoffsets berechnet
        float leftoff = (xx1 - xx3) / (yy2 - yy1);
        float rightoff = (xx2 - xx4) / (yy2 - yy1);
        // ein positiver Offset gibt positiven Anstieg an

        // hier wird die X-Koordinate der schraegen Trapezlinien an der Y-Position des Punktes berechnet
        int leftx = (int) (x3 + (leftoff * (y2 - ykoord)));
        int rightx = (int) (x4 + (rightoff * (y2 - ykoord)));

        return (new GenericPoint(leftx, rightx));
    }

    public synchronized GenericPoint RandPunkt(GenericPoint dieser) {
        // Zuerst testen, ob nicht doch innerhalb gelegen
        if (PointInside(dieser) == true) {
            // System.out.println("Dieser Punkt ist innerhalb!!!");
            return (new GenericPoint(dieser.x, dieser.y));
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
        if ((dieser.y < yy1) && (dieser.x > xx1) && (dieser.x < xx2)) {
            // System.out.println("Punkt ist genau ueber Trapez !!");
            return (new GenericPoint(dieser.x, y1));
        }

        // Wenn Punkt genau unter Gerade des Trapezes, dann keine Berechnung notwendig
        if ((dieser.y > yy2) && (dieser.x > xx3) && (dieser.x < xx4)) {
            // System.out.println("Punkt ist genau unter Trapez !!");
            return (new GenericPoint(dieser.x, y2));
        }


        boolean isLeft = false;
        // Feststellen, ob Punkt eindeutig links oder rechts vom Trapez ueber Viereckvergleich
        if ((dieser.x < xx1) && (dieser.x < xx3)) {
            isLeft = true;
            // System.out.println("Eindeutig Links !");
        } else {
            if ((dieser.x > xx2) && (dieser.x > xx4)) {
                isLeft = false;
                // System.out.println("Eindeutig Rechts !");
            } else {
                // Gerade mitten durch Trapez berechnen fuer rechts/links - Auswahl

                // Fixpunkte in der Mitte der Waagerechten
                float upx = (xx1 + xx2) / 2;
                float dox = (xx3 + xx4) / 2;

                // Anstieg
                float ans = (upx - dox) / (yy2 - yy1);
                // System.out.println("Anstieg : " + ans);

                // Geradenpunkt bei aktueller Y - Koordinate
                float aktx = dox + ((yy2 - dieser.y) * ans);
                // System.out.println("X1 : " + xx1 + "X3 : " + xx3 + "Geradenpunkt : " + aktx);

                // Punkt links von Trapez
                if (aktx > dieser.x) {
                    isLeft = true;
                } else {
                    isLeft = false;
                }
                // System.out.println("Berechnung notwendig !");
            }
        }

        if (isLeft == true) {
            // System.out.print("Punkt ist links vom Trapez!");

            // Anstiege der linken Schraege berechnen
            float ml = 0;
            if ((xx1 - xx3) != 0) {
                ml = (yy2 - yy1) / (xx1 - xx3);
            } else {
                // es handelt sich um senkrechte Seite - Koordinaten koennen sofort bestimmt werden

                // System.out.print("Linke Seite ist senkrecht!");

                // Punkt ist oberhalb, oberen Eckpunkt zurueckgeben
                if (dieser.y < yy1) {
                    // System.out.println("Obere Ecke!");
                    return (new GenericPoint(x1, y1));
                }

                // Punkt ist unterhalb, unteren Eckpunkt zurueckgeben
                if (dieser.y > yy2) {
                    // System.out.println("Untere Ecke!");
                    return (new GenericPoint(x3, y2));
                }

                // Punkt muss auf Hoehe Rechteck sein, angepasste Koordinaten zurueckgeben
                // System.out.println("Innerhalb erlaubt!");
                return (new GenericPoint(x1, dieser.y));
            }
            // System.out.print("Anstieg " + ml);

            // Jetzt muss noch der Y-Offset berechnet werden fuer die Geradengleichung
            float nl = yy1 + (xx1 * ml);
            // System.out.println("Y-Offset Trapez " + nl);

            // Hier folgt der Offset fuer den Punkt (reziproker negativer Anstieg)
            float npl = dieser.y + (dieser.x * (-1 / ml));
            // System.out.print("Y-Offset Punkt " + npl);

            // nun alles ins geloeste Gleichungssystem fuer Schnittpunkt 2-er Geraden einsetzen

            // Schnittpunkt mit linker Gerade
            float xl = (npl - nl) / (ml + (1 / ml));
            float yl = nl + (ml * xl);
            xl = -xl;
            // System.out.print("Aktuell " + xl + " " + yl);

            // testen, ob Schnittpunkt innerhalb erlaubter Menge des Trapezes
            if ((yl > yy1) && (yl < yy2)) {
                // Schnittpunkt ist innerhalb - Trapezpunkt ermitteln (Rundungsfehler!!!)
                GenericPoint tmp = new GenericPoint((int) xl, (int) yl);

                // Solange testen, bis sich Punkt innerhalb ergibt
                while ((PointInside(tmp) == false) && (tmp.x < 1300)) {
                    // System.out.println("Searching...");
                    tmp.x++;
                }
                // System.out.println("Routine erfolgreich beendet!" + tmp.x + " " + tmp.y);
                return tmp;
            } else {
                // Schnittpunkt ist ausserhalb, Eckpunkt zurueckgeben

                // oberer Punkt
                if (yl < yy1) {
                    // System.out.println("Punkt zu hoch, oberer Eckpunkt zurueckgegeben!" + (int) xl + " " + (int) yl);
                    return (new GenericPoint(x1, y1));
                }

                // unterer Punkt
                else {
                    // System.out.println("Punkt zu tief, unterer Eckpunkt zurueckgegeben!" + (int) xl + " " + (int) yl);
                    return (new GenericPoint(x3, y2));
                }
            }
        }

        // Punkt rechts vom Trapez
        else {
            // System.out.print("Punkt ist rechts vom Trapez!");

            // Anstieg der rechten Schraege berechnen
            float mr = 0;
            if ((xx2 - xx4) != 0) {
                mr = (yy2 - yy1) / (xx2 - xx4);
            } else {
                // es handelt sich um senkrechte Seite - Koordinaten koennen sofort bestimmt werden

                // System.out.print("Rechte Seite ist senkrecht!");

                // Punkt ist oberhalb, oberen Eckpunkt zurueckgeben
                if (dieser.y < yy1) {
                    // System.out.println("Obere Ecke!");
                    return (new GenericPoint(x2, y1));
                }

                // Punkt ist unterhalb, unteren Eckpunkt zurueckgeben
                if (dieser.y > yy2) {
                    // System.out.println("Untere Ecke!");
                    return (new GenericPoint(x4, y2));
                }

                // Punkt muss auf Hoehe Rechteck sein, angepasste Koordinaten zurueckgeben
                // System.out.println("Innerhalb erlaubt!");
                return (new GenericPoint(x2, dieser.y));
            }
            // System.out.print("Anstieg " + mr);

            // Jetzt muss noch der Y-Offset berechnet werden fuer die Geradengleichung
            float nr = yy1 + (xx2 * mr);
            // System.out.println("Y-Offset Trapez " + nr);

            // Hier folgen die Offsets fuer den Punkt (reziproker negativer Anstieg)
            float npr = dieser.y + (dieser.x * (-1 / mr));
            // System.out.println("Y-Offset Punkt " + npr);

            // nun alles ins geloeste Gleichungssystem fuer Schnittpunkt 2-er Geraden einsetzen

            // Schnittpunkt mit rechter Gerade
            float xr = (npr - nr) / (mr + (1 / mr));
            float yr = nr + (mr * xr);
            xr = -xr;
            // System.out.print("Aktuell " + xr + " " + yr);

            // testen, ob Schnittpunkt innerhalb erlaubter Menge des Trapezes
            if ((yr > yy1) && (yr < yy2)) {
                // Schnittpunkt ist innerhalb - Trapezpunkt ermitteln (Rundungsfehler!!!)
                GenericPoint tmp = new GenericPoint((int) xr, (int) yr);

                // Solange testen, bis sich Punkt innerhalb ergibt
                while ((PointInside(tmp) == false) && (tmp.x > -1)) {
                    // System.out.println("Searching!");
                    tmp.x--;
                }
                // System.out.println("Routine erfolgreich beendet!" + tmp.x + " " + tmp.y);
                return tmp;
            } else {
                // Schnittpunkt ist ausserhalb, Eckpunkt zurueckgeben

                // oberer Punkt
                if (yr < yy1) {
                    // System.out.println("Punkt zu hoch, oberer Eckpunkt zurueckgegeben!" + (int) xr + " " + (int) yr);
                    return (new GenericPoint(x2, y1));
                }

                // unterer Punkt
                else {
                    // System.out.println("Punkt zu tief, unterer Eckpunkt zurueckgegeben!" + (int) xr + " " + (int) yr);
                    return (new GenericPoint(x4, y2));
                }
            }
        }
    }

    // Hier wird der Flaecheninhalt des Trapezes in Pixeln wiedergegeben
    public int Flaeche() {
        int xoffset = ((x3 + x4) / 2) - ((x1 + x3) / 2);
        int yoffset = y2 - y1;
        return (xoffset * yoffset);
    }
}		     