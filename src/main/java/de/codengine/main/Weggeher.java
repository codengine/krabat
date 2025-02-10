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

package de.codengine.main;

import de.codengine.Start;

import java.util.Vector;

public class Weggeher {
    private final Start mainFrame;
    public Vector<Bordertrapez> vBorders;
    public GenericPoint destinationPlace;
    private Vector<Integer> vBestWeg;
    private int wegPosition = 0;

    public Weggeher(Start caller) {
        mainFrame = caller;
        vBorders = new Vector<Bordertrapez>();
    }

    // normale Laufroutine ohne irgendwelche Extras
    public void SetzeNeuenWeg(GenericPoint destPoint) {
        // Extras alle wieder ausschalten
        mainFrame.krabat.clearanimpos = true;
        mainFrame.krabat.upsidedown = false;

        //System.out.println("Neuer Weg "+destPoint.x+" "+destPoint.y);
        // Krabats Animation stoppen, falls aktiv
        mainFrame.krabat.StopAnim();

        destinationPlace = destPoint;
        int nAnzahlRect = vBorders.size();
        Bordertrapez tBestRect;
        int currBorder = GetCurrentBorder();
        int bestRect = -1;
        float minDistance = 1000;

        // Wurde ggw. Rechteck korrekt ermittelt ?
        if (currBorder < 0) {
            currBorder = RightRect();
            System.out.println("Rectangle wurde neubestimmt : " + currBorder);
        }

        // Mitte-Koordinaten auf Fuss-Koordinaten umrechnen
        // entfuellt hier
        GenericPoint feetPoint = new GenericPoint(destinationPlace.x, destinationPlace.y);

        // alle Grenzrechtecke dursuchen, welches der Beste als Ziel
        for (int i = 0; i < nAnzahlRect; i++) {
            Bordertrapez tBRect = vBorders.elementAt(i);
            float t_dist = tBRect.CenterDistance(feetPoint);
            if (t_dist < minDistance) {
                // dieses Rechteck ist besser
                bestRect = i;
                minDistance = t_dist;
            }
        }

        // liegt Ziel innerhalb des optimalen GrenzRectangle ?
        tBestRect = vBorders.elementAt(bestRect);

        System.out.println("BestRect : " + bestRect);

        if (!tBestRect.PointInside(feetPoint)) {
            // optimales Ziel im Rectangle suchen
            destinationPlace = tBestRect.RandPunkt(feetPoint);
            // System.out.println("Best GenericPoint " + destinationPlace.x + " " + destinationPlace.y + " of " + feetPoint.x + " " + feetPoint.y);
        }

        //   System.out.print ("Destination: ");
        //   System.out.print (destinationPlace.x+" ");
        //   System.out.println (destinationPlace.y);

        // Ziel und Start im selben Grenzrechteck ?
        if (bestRect == currBorder) {
            // System.out.println ("Das selbe  Border-Rectangle !");
            // keine grosse Wanderung noetig
            mainFrame.krabat.isWandering = false;
            mainFrame.krabat.MoveTo(destinationPlace);
            return;
        }

        //    System.out.print ("Ggw. Rect: ");
        //    System.out.println (currBorder);

        //    System.out.print ("Best Rect: ");
        //    System.out.println (bestRect);

        // besten Weg zum Ziel ermitteln
        vBestWeg = mainFrame.wegSucher.StartSuche(currBorder, bestRect);

        //    System.out.print ("Weglaenge: ");
        //    System.out.println (vBestWeg.size ());

        // neuen Kurs sofort setzen
        mainFrame.krabat.isWalking = false;
        mainFrame.krabat.isWandering = true;
        wegPosition = 0;
    }

    // Hier wird von der aktuellen Position genau auf den Punkt gelaufen, Rectangles sind egal
    public void SetzeGarantiertNeuenWeg(GenericPoint destPoint) {
        mainFrame.krabat.upsidedown = false;
        mainFrame.krabat.clearanimpos = false;
        mainFrame.krabat.isWandering = false;
        mainFrame.krabat.MoveTo(destPoint);
    }

    public void SetzeWegOhneStand(GenericPoint dpoint) {
        SetzeNeuenWeg(dpoint);
        mainFrame.krabat.clearanimpos = false;
    }

    public void SetzeGarantiertWegFalsch(GenericPoint pt) {
        SetzeGarantiertNeuenWeg(pt);
        mainFrame.krabat.upsidedown = true;
    }

    // Krabat auf seinem Weg weiterbewegen
    public void GeheWeg() {
        if (mainFrame.krabat.isWalking) {
            // erst zur naechten Station gehen lassen
            mainFrame.krabat.Move();
            return;
        }

        if (!mainFrame.krabat.isWandering) {
            // bereits am Ziel
            return;
        }

        // noch nicht die letzte Station ?
        if (wegPosition < vBestWeg.size() - 1) {
            // System.out.println("Neuer Kurs !!");

            // diese und naechste Station (Borders) ermitteln

            Integer tInteger = vBestWeg.elementAt(wegPosition);
            int thisRect = tInteger.intValue();
            tInteger = vBestWeg.elementAt(wegPosition + 1);
            int nextRect = tInteger.intValue();

            // System.out.println("Start : " + thisRect + "Ziel : " + nextRect);

            // Optimalen Uebergang zwischen den beiden Grenzen finden
            GenericPoint pKrFeetPos = mainFrame.krabat.GetKrabatPos();
            Bordertrapez thisBRect = vBorders.elementAt(thisRect);
            Bordertrapez nextBRect = vBorders.elementAt(nextRect);
            GenericPoint pUeber = OptimalUebergang(thisBRect, nextBRect, pKrFeetPos);

            //      System.out.print ("Schritt: ");
            //      System.out.println (wegPosition);

            // bis zur naechsten Station marschieren
            mainFrame.krabat.MoveTo(pUeber);
            wegPosition++;
        } else {
            // die letzte Station; Wanderung beenden und direkt zum Ziel gehen
            vBestWeg.removeAllElements();
            //      bestWeg = "";
            mainFrame.krabat.isWandering = false;
            mainFrame.krabat.MoveTo(destinationPlace);
            //      System.out.print ("Letzte Station !");
            wegPosition++;
        }
        // schliesslich einen Schritt setzen
        mainFrame.krabat.Move();
    }

    // Ermitteln des Grenzrechtecks, in welchem sich Krabat ggw. befindet
    private int GetCurrentBorder() {
        int nTemp = -1;
        int nBRAnzahl = vBorders.size();
        GenericPoint pKrabat = mainFrame.krabat.GetKrabatPos();
        for (int i = 0; i < nBRAnzahl; i++) {
            Bordertrapez tBRect = vBorders.elementAt(i);
            if (tBRect.PointInside(pKrabat)) {
                nTemp = i;
                break;
            }
        }
        System.out.println("Aktuelles RE : " + nTemp);
        return nTemp;
    }


    // Ermittelt den Punkt des besten Ubergangs zwischen zwei Grenzrechtecken,
    // wobei die aktuelle Position der Figur beachtet wird (Fuesse)
    private GenericPoint OptimalUebergang(Bordertrapez quell, Bordertrapez ziel,
                                          GenericPoint pKrabatFeets) {
        // Testen, ob Mittenlauf sinnvoll!
        boolean mittenlauf = false;
        if ((vBestWeg.size() - wegPosition) > 2) {
            mittenlauf = true;
            System.out.println("Mittenlauf initiiert!");
        }

        GenericPoint pBest = new GenericPoint(0, 0);

        // Lage von Quelle zu Ziel feststellen

        int left = 0;
        int right = 0;

        // ist Quelle unter Ziel ?
        if (quell.y1 > ziel.y2) {
            // erlaubtes Geradenstueck berechnen
            if (ziel.x3 > quell.x1) {
                left = ziel.x3;
            } else {
                left = quell.x1;
            }
            if (ziel.x4 < quell.x2) {
                right = ziel.x4;
            } else {
                right = quell.x2;
            }

            // kuerzesten Weg berechnen
            pBest.y = ziel.y2;
            if (pKrabatFeets.x < left) {
                pBest.x = left;
            } else {
                if (pKrabatFeets.x > right) {
                    pBest.x = right;
                } else {
                    pBest.x = pKrabatFeets.x;
                }
            }
            if ((mittenlauf) && (Math.abs(pKrabatFeets.y - quell.y1) > Math.abs(pKrabatFeets.x - ((left + right) / 2)))) {
                pBest.x = (left + right) / 2;
            }
            return pBest;
        }

        // ist Quelle ueber Ziel ?
        if (quell.y2 < ziel.y1) {
            // erlaubtes Geradenstueck berechnen
            if (ziel.x1 > quell.x3) {
                left = ziel.x1;
            } else {
                left = quell.x3;
            }
            if (ziel.x2 < quell.x4) {
                right = ziel.x2;
            } else {
                right = quell.x4;
            }

            // kuerzesten Weg berechnen
            pBest.y = ziel.y1;
            if (pKrabatFeets.x < left) {
                pBest.x = left;
            } else {
                if (pKrabatFeets.x > right) {
                    pBest.x = right;
                } else {
                    pBest.x = pKrabatFeets.x;
                }
            }
            if ((mittenlauf) && (Math.abs(pKrabatFeets.y - quell.y2) > Math.abs(pKrabatFeets.x - ((left + right) / 2)))) {
                pBest.x = (left + right) / 2;
            }
            return pBest;
        }

        int up = 0;
        int down = 0;

        // ist Quelle links vom Ziel ?
        if (quell.x2 < ziel.x1) {
            // erlaubtes Geradenstueck berechnen
            if (ziel.y1 > quell.y1) {
                up = ziel.y1;
            } else {
                up = quell.y1;
            }
            if (ziel.y2 < quell.y2) {
                down = ziel.y2;
            } else {
                down = quell.y2;
            }

            // kuerzesten Weg berechnen
            pBest.x = ziel.x1;
            if (pKrabatFeets.y < up) {
                pBest.y = up;
            } else {
                if (pKrabatFeets.y > down) {
                    pBest.y = down;
                } else {
                    pBest.y = pKrabatFeets.y;
                }
            }
            if ((mittenlauf) && (Math.abs(pKrabatFeets.x - quell.x2) > Math.abs(pKrabatFeets.y - ((up + down) / 2)))) {
                pBest.y = (up + down) / 2;
            }
            return pBest;
        }

        // ist Quelle rechts vom Ziel ?
        if (quell.x1 > ziel.x2) {
            // erlaubtes Geradenstueck berechnen
            if (ziel.y1 > quell.y1) {
                up = ziel.y1;
            } else {
                up = quell.y1;
            }
            if (ziel.y2 < quell.y2) {
                down = ziel.y2;
            } else {
                down = quell.y2;
            }

            // kuerzesten Weg berechnen
            pBest.x = ziel.x2;
            if (pKrabatFeets.y < up) {
                pBest.y = up;
            } else {
                if (pKrabatFeets.y > down) {
                    pBest.y = down;
                } else {
                    pBest.y = pKrabatFeets.y;
                }
            }
            if ((mittenlauf) && (Math.abs(pKrabatFeets.x - quell.x1) > Math.abs(pKrabatFeets.y - ((up + down) / 2)))) {
                pBest.y = (up + down) / 2;
            }
            return pBest;
        }

        System.out.println("Fehler im Optimaluebergang !!! Rects falsch!!!");

        return pBest;
    }

    private int RightRect() {
        int rgbe = -1;
        int wieviele = vBorders.size();
        GenericPoint Fussp = mainFrame.krabat.GetKrabatPos();
        int tAbstand = 20000;
        for (int fuck = 0; fuck < wieviele; fuck++) {
            Bordertrapez temprect = vBorders.elementAt(fuck);
            if (temprect.CenterDistance(Fussp) < tAbstand) {
                tAbstand = temprect.CenterDistance(Fussp);
                rgbe = fuck;
            }
        }
        return rgbe;
    }
}