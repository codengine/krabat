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

import java.util.Vector;

public class Wegsucher {
    private boolean[][] matrix;
    private int numRects;
    private int endPos;
    private Vector<Integer> vBesterWeg;

    public Wegsucher() {
    }

    /// // Hier werden die Eintraege in den Wegsucher initialisiert ///////////////////

    public void ClearMatrix(int wieviel) {
        numRects = wieviel;

        matrix = new boolean[numRects][numRects];

        for (int i = 0; i < wieviel; i++) {
            for (int j = 0; j < wieviel; j++) {
                matrix[i][j] = false;
            }
        }
    }

    // Zwei Positionen in Matrix verbinden (in jede Richtung)
    public void PosVerbinden(int p1, int p2) {
        matrix[p1][p2] = true;
        matrix[p2][p1] = true;
    }

    // Rekursive Funktion zum Zusammensetzen des Weges
    private void RekursivSuche(int aktPos, Vector<Integer> vWeg) {
        // nicht an gleiche Instanz anhaengen, sonst Probleme beim Backtracking
        Vector<Integer> vTemp = (Vector<Integer>) vWeg.clone();

        // aktuelle Position an Weg anhaengen
        vTemp.addElement(aktPos);

        if (aktPos == endPos) {
            // Ende gefunden
            EvalResult(vTemp);
            return;
        }

        // alle moeglichen Wege weitervefolgen
        for (int i = 0; i < numRects; i++) {
            if ((matrix[aktPos][i]) &&    // dieser Weg moeglich ?
                    (!vTemp.contains(i))) {
                // noch nicht im Weg enthalten ?
                RekursivSuche(i, vTemp);
            }
        }
    }

    // Alle Loesungsvorschlaege entgegenehmen und den besten ermitteln
    private void EvalResult(Vector<Integer> result) {
        if (vBesterWeg.isEmpty()) {
            // der erste Weg
            vBesterWeg = result;
        } else {
            // nur uebernehmen, wenn Weg kuerzer
            if (result.size() < vBesterWeg.size()) {
                vBesterWeg = result;
            }
        }
    }

    // Suche von Start nach Ziel starten
    public Vector<Integer> StartSuche(int st_pos, int end_pos) {
        endPos = end_pos;
        vBesterWeg = new Vector<Integer>();
        RekursivSuche(st_pos, new Vector<Integer>());
        // System.out.println ("Bester Weg: ");
        // TraceWeg (vBesterWeg);
        return vBesterWeg;
    }

    // zum Debuggen: Ausgabe des Weges
  /*
  private void TraceWeg (Vector<Integer> tWeg)
  {
    int nAnzahl = tWeg.size ();
    
    for (int i = 0; i < nAnzahl; i++)
    {
      Integer tInt = (Integer) tWeg.elementAt (i);
      System.out.print (tInt.intValue ());
    }
    System.out.println ("");
  }
  */
}