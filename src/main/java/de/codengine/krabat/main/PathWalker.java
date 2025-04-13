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

import de.codengine.krabat.Start;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Vector;

public class PathWalker {
    private static final Logger log = LoggerFactory.getLogger(PathWalker.class);
    private final Start mainFrame;
    public final Vector<BorderTrapezoid> vBorders;
    public GenericPoint destinationPlace;
    private Vector<Integer> vBestWeg;
    private int wegPosition = 0;

    public PathWalker(Start caller) {
        mainFrame = caller;
        vBorders = new Vector<>();
    }

    // normale Laufroutine ohne irgendwelche Extras
    public void setNewWay(GenericPoint destination) {
        // Extras alle wieder ausschalten
        mainFrame.krabat.resetAnimPos = true;
        mainFrame.krabat.upsideDown = false;

        // Krabats Animation stoppen, falls aktiv
        mainFrame.krabat.stopAnim();

        destinationPlace = destination;
        int nAnzahlRect = vBorders.size();
        BorderTrapezoid tBestRect;
        int currBorder = getCurrentBorder();
        int bestRect = -1;
        float minDistance = 1000;

        // Wurde ggw. Rechteck korrekt ermittelt ?
        if (currBorder < 0) {
            currBorder = rightRect();
            log.debug("Rectangle wurde neubestimmt : {}", currBorder);
        }

        // Mitte-Koordinaten auf Fuss-Koordinaten umrechnen
        // entfuellt hier
        GenericPoint feetPoint = new GenericPoint(destinationPlace.x, destinationPlace.y);

        // alle Grenzrechtecke dursuchen, welches der Beste als Ziel
        for (int i = 0; i < nAnzahlRect; i++) {
            BorderTrapezoid tBRect = vBorders.elementAt(i);
            float temporaryDist = tBRect.centerDistance(feetPoint);
            if (temporaryDist < minDistance) {
                // dieses Rechteck ist besser
                bestRect = i;
                minDistance = temporaryDist;
            }
        }

        // liegt Ziel innerhalb des optimalen GrenzRectangle ?
        tBestRect = vBorders.elementAt(bestRect);

        log.debug("BestRect : {}", bestRect);

        if (!tBestRect.pointInside(feetPoint)) {
            // optimales Ziel im Rectangle suchen
            destinationPlace = tBestRect.edgePoint(feetPoint);
        }

        //   System.out.print ("Destination: ");
        //   System.out.print (destinationPlace.x+" ");
        //   System.out.println (destinationPlace.y);

        // Ziel und Start im selben Grenzrechteck ?
        if (bestRect == currBorder) {
            // System.out.println ("Das selbe  Border-Rectangle !");
            // keine grosse Wanderung noetig
            mainFrame.krabat.isWandering = false;
            mainFrame.krabat.moveTo(destinationPlace);
            return;
        }

        //    System.out.print ("Ggw. Rect: ");
        //    System.out.println (currBorder);

        //    System.out.print ("Best Rect: ");
        //    System.out.println (bestRect);

        // besten Weg zum Ziel ermitteln
        vBestWeg = mainFrame.pathFinder.startSearch(currBorder, bestRect);

        //    System.out.print ("Weglaenge: ");
        //    System.out.println (vBestWeg.size ());

        // neuen Kurs sofort setzen
        mainFrame.krabat.isWalking = false;
        mainFrame.krabat.isWandering = true;
        wegPosition = 0;
    }

    // Hier wird von der aktuellen Position genau auf den Punkt gelaufen, Rectangles sind egal
    public void setNewWayGuaranteed(GenericPoint destPoint) {
        mainFrame.krabat.upsideDown = false;
        mainFrame.krabat.resetAnimPos = false;
        mainFrame.krabat.isWandering = false;
        mainFrame.krabat.moveTo(destPoint);
    }

    public void setWayWithoutStanding(GenericPoint dpoint) {
        setNewWay(dpoint);
        mainFrame.krabat.resetAnimPos = false;
    }

    public void setWayGuaranteedWrong(GenericPoint pt) {
        setNewWayGuaranteed(pt);
        mainFrame.krabat.upsideDown = true;
    }

    // Krabat auf seinem Weg weiterbewegen
    public void doWalk() {
        if (mainFrame.krabat.isWalking) {
            // erst zur naechten Station gehen lassen
            mainFrame.krabat.move();
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
            int thisRect = tInteger;
            tInteger = vBestWeg.elementAt(wegPosition + 1);
            int nextRect = tInteger;

            // System.out.println("Start : " + thisRect + "Ziel : " + nextRect);

            // Optimalen Uebergang zwischen den beiden Grenzen finden
            GenericPoint pKrFeetPos = mainFrame.krabat.getPos();
            BorderTrapezoid thisBRect = vBorders.elementAt(thisRect);
            BorderTrapezoid nextBRect = vBorders.elementAt(nextRect);
            GenericPoint pUeber = optimalTransition(thisBRect, nextBRect, pKrFeetPos);

            //      System.out.print ("Schritt: ");
            //      System.out.println (wegPosition);

            // bis zur naechsten Station marschieren
            mainFrame.krabat.moveTo(pUeber);
        } else {
            // die letzte Station; Wanderung beenden und direkt zum Ziel gehen
            vBestWeg.removeAllElements();
            mainFrame.krabat.isWandering = false;
            mainFrame.krabat.moveTo(destinationPlace);
        }
        wegPosition++;
        // schliesslich einen Schritt setzen
        mainFrame.krabat.move();
    }

    // Ermitteln des Grenzrechtecks, in welchem sich Krabat ggw. befindet
    private int getCurrentBorder() {
        int nTemp = -1;
        int nBRAnzahl = vBorders.size();
        GenericPoint pKrabat = mainFrame.krabat.getPos();
        for (int i = 0; i < nBRAnzahl; i++) {
            BorderTrapezoid tBRect = vBorders.elementAt(i);
            if (tBRect.pointInside(pKrabat)) {
                nTemp = i;
                break;
            }
        }
        log.debug("Aktuelles RE : {}", nTemp);
        return nTemp;
    }


    // Ermittelt den Punkt des besten Ubergangs zwischen zwei Grenzrechtecken,
    // wobei die aktuelle Position der Figur beachtet wird (Fuesse)
    private GenericPoint optimalTransition(BorderTrapezoid quell, BorderTrapezoid ziel,
                                           GenericPoint pKrabatFeets) {
        // Testen, ob Mittenlauf sinnvoll!
        boolean mittenlauf = false;
        if (vBestWeg.size() - wegPosition > 2) {
            mittenlauf = true;
            log.debug("Mittenlauf initiiert!");
        }

        GenericPoint pBest = new GenericPoint(0, 0);

        // Lage von Quelle zu Ziel feststellen

        int left;
        int right;

        // ist Quelle unter Ziel ?
        if (quell.y1 > ziel.y2) {
            // erlaubtes Geradenstueck berechnen
            left = Math.max(ziel.x3, quell.x1);
            right = Math.min(ziel.x4, quell.x2);

            // kuerzesten Weg berechnen
            pBest.y = ziel.y2;
            if (pKrabatFeets.x < left) {
                pBest.x = left;
            } else {
                pBest.x = Math.min(pKrabatFeets.x, right);
            }
            if (mittenlauf && Math.abs(pKrabatFeets.y - quell.y1) > Math.abs(pKrabatFeets.x - (left + right) / 2)) {
                pBest.x = (left + right) / 2;
            }
            return pBest;
        }

        // ist Quelle ueber Ziel ?
        if (quell.y2 < ziel.y1) {
            // erlaubtes Geradenstueck berechnen
            left = Math.max(ziel.x1, quell.x3);
            right = Math.min(ziel.x2, quell.x4);

            // kuerzesten Weg berechnen
            pBest.y = ziel.y1;
            if (pKrabatFeets.x < left) {
                pBest.x = left;
            } else {
                pBest.x = Math.min(pKrabatFeets.x, right);
            }
            if (mittenlauf && Math.abs(pKrabatFeets.y - quell.y2) > Math.abs(pKrabatFeets.x - (left + right) / 2)) {
                pBest.x = (left + right) / 2;
            }
            return pBest;
        }

        int up;
        int down;

        // ist Quelle links vom Ziel ?
        if (quell.x2 < ziel.x1) {
            // erlaubtes Geradenstueck berechnen
            up = Math.max(ziel.y1, quell.y1);
            down = Math.min(ziel.y2, quell.y2);

            // kuerzesten Weg berechnen
            pBest.x = ziel.x1;
            if (pKrabatFeets.y < up) {
                pBest.y = up;
            } else {
                pBest.y = Math.min(pKrabatFeets.y, down);
            }
            if (mittenlauf && Math.abs(pKrabatFeets.x - quell.x2) > Math.abs(pKrabatFeets.y - (up + down) / 2)) {
                pBest.y = (up + down) / 2;
            }
            return pBest;
        }

        // ist Quelle rechts vom Ziel ?
        if (quell.x1 > ziel.x2) {
            // erlaubtes Geradenstueck berechnen
            up = Math.max(ziel.y1, quell.y1);
            down = Math.min(ziel.y2, quell.y2);

            // kuerzesten Weg berechnen
            pBest.x = ziel.x2;
            if (pKrabatFeets.y < up) {
                pBest.y = up;
            } else {
                pBest.y = Math.min(pKrabatFeets.y, down);
            }
            if (mittenlauf && Math.abs(pKrabatFeets.x - quell.x1) > Math.abs(pKrabatFeets.y - (up + down) / 2)) {
                pBest.y = (up + down) / 2;
            }
            return pBest;
        }

        log.error("Fehler im Optimaluebergang !!! Rects falsch!!!");

        return pBest;
    }

    private int rightRect() {
        int rgbe = -1;
        int wieviele = vBorders.size();
        GenericPoint footPosition = mainFrame.krabat.getPos();
        int tAbstand = 20000;
        for (int fuck = 0; fuck < wieviele; fuck++) {
            BorderTrapezoid temprect = vBorders.elementAt(fuck);
            if (temprect.centerDistance(footPosition) < tAbstand) {
                tAbstand = temprect.centerDistance(footPosition);
                rgbe = fuck;
            }
        }
        return rgbe;
    }
}