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
import de.codengine.krabat.platform.GenericDrawingContext;

public class Multiple2  // Turrican II laesst gruessen!!!!!!
{
    private final Start mainFrame;

    // fuer Anzeige
    private int selected = -1;
    private int oldsel = -1;
    private int Cursorform;
    private boolean Paintcall = false;
    private int yoff;

    // Variable fuer maximale MC-Groesse
    private static final int MCGROESSE = 10;

    // Variablen fuer Fragen
    private int Anzahl;
    public String[] Fragen = new String[MCGROESSE];
    private final GenericRectangle[] Positionen = new GenericRectangle[MCGROESSE];
    private final int[] Nextactionids = new int[MCGROESSE];
    private final int[][] Actionvariablen = new int[MCGROESSE][10];

    public int Antwort = 0;
    public int ActionID = 0;

    // Im Konstruktor Variablen bereitstellen
    public Multiple2(Start caller) {
        mainFrame = caller;
    }

    // Hier wird neue MC-Routine initialisiert
    public void InitMC(int yoff) {
        // hier wird Init des Cursors beim Aufrufen erzwungen
        Cursorform = 200;
        Anzahl = -1;
        this.yoff = yoff;
    }

    // Hier wird ein MC - Element hinzugefuegt mit automatischer Breite
    public void ExtendMC(String text, int Aktiv, int Gefragt, int[] Nachfolger, int nextActionID) {
        // hier testen, ob diese Frage schon interessant ist, sonst zurueckspringen
        if ((Aktiv < 1000) && (!mainFrame.Actions[Aktiv])) {
            return;
        }

        // hier noch testen, ob die Frage schon gefrat wurde und deshalb rausfaellt
        if ((Gefragt < 1000) && (mainFrame.Actions[Gefragt])) {
            return;
        }

        // Anzahl Fragen um 1 erhoehen
        Anzahl++;

        // String merken
        Fragen[Anzahl] = mainFrame.ifont.TeileText(text);

        // Rectangle je nach Position des Textes festlegen
        // 1. Rectangle extra
        if (Anzahl == 0) {
            Positionen[Anzahl] = new GenericRectangle(0, yoff, 639, 40 + ((mainFrame.ifont.ZeilenAnzahl(Fragen[Anzahl]) - 1) * 27));
        } else {
            // folgende Rects immer anschliessend
            int temp = Positionen[Anzahl - 1].getY() + Positionen[Anzahl - 1].getHeight();
            Positionen[Anzahl] = new GenericRectangle(0, temp, 639, 40 + ((mainFrame.ifont.ZeilenAnzahl(Fragen[Anzahl]) - 1) * 27));
        }

        // nextActionID merken (wird zurueckgegeben bei Erfolg)
        Nextactionids[Anzahl] = nextActionID;

        // hier das Array fuer alle Nachfolger initialisieren (NULL = kein Array)
        for (int i = 0; i < 10; i++) {
            if (Nachfolger != null) {
                if (i < Nachfolger.length) {
                    Actionvariablen[Anzahl][i] = Nachfolger[i];
                    // System.out.println ("Variable " + Actionvariablen [Anzahl][i] + " wurde uebernommen.");
                } else {
                    Actionvariablen[Anzahl][i] = 1000; // 1000 -> ungueltig !
                }
            } else {
                Actionvariablen[Anzahl][i] = 1000; // 1000 -> ungueltig !
            }
        }

        // alles gemerkt, ok
    }

    // Paint - Routine fuer Multiple Choice
    public void paintMultiple(GenericDrawingContext g) {
        GenericRectangle my;
        my = g.getClipBounds();
        g.setClip(0, 0, 1284, 964);

        // 1.Aufruf, zuerst alles Zeichnen
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);

            for (int i = 0; i <= Anzahl; ++i) {
                if (selected == i) {
                    mainFrame.ifont.drawString(g, ("$" + Fragen[i]),
                            Positionen[i].getX() + mainFrame.scrollx + 30,
                            Positionen[i].getY() + mainFrame.scrolly + 10, 1);
                } else {
                    mainFrame.ifont.drawString(g, ("$" + Fragen[i]),
                            Positionen[i].getX() + mainFrame.scrollx + 30,
                            Positionen[i].getY() + mainFrame.scrolly + 10, 0xff00b000);
                }
            }
            oldsel = selected;
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
            return;
        }

        if (oldsel != -1) {
            mainFrame.ifont.drawString(g, ("$" + Fragen[oldsel]),
                    Positionen[oldsel].getX() + mainFrame.scrollx + 30,
                    Positionen[oldsel].getY() + mainFrame.scrolly + 10, 0xff00b000);
        }
        if (oldsel != -1) {
            oldsel = -1;
        }

        if (selected != -1) {
            mainFrame.ifont.drawString(g, ("$" + Fragen[selected]),
                    Positionen[selected].getX() + mainFrame.scrollx + 30,
                    Positionen[selected].getY() + mainFrame.scrolly + 10, 1);
        }

        if (selected != -1) {
            oldsel = selected;
        }
        // System.out.println ("Paint : " + selected);
        g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
    }

    // Mouse - Listener fuer Multiple Choice
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (e.isRightClick()) {
            // Linke Maustaste
            for (int i = 0; i <= Anzahl; ++i) {
                if (Positionen[i].contains(pTemp)) {
                    // Antwort angeben fuer die Location
                    Antwort = i;
                    ActionID = Nextactionids[i];

                    // Actionarray bearbeiten
                    for (int f = 0; f < 10; f++) {
                        if (Actionvariablen[i][f] < 1000) {
                            mainFrame.Actions[Actionvariablen[i][f]] = true;
                            // System.out.println ("Actionvariable " + Actionvariablen[i][f] + " wurde true gesetzt.");
                        }
                    }

                    // MC-Klasse deaktivieren und alles zuruecksetzen
                    selected = -1;
                    oldsel = -1;
                    mainFrame.fPlayAnim = true;
                    mainFrame.isMultiple = false;
                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                    break;
                }
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Cursor auf Normal setzen je nach Bedarf
        if (Cursorform != 0) {
            Cursorform = 0;
            mainFrame.setCursor(mainFrame.Normal);
        }

        // System.out.println("Move Thrown !");
        selected = -1;
        for (int i = 0; i <= Anzahl; ++i) {
            if (Positionen[i].contains(pTemp)) {
                selected = i;
                // System.out.println("Over an Item!");
                break;
            }
        }

        // System.out.println("Move : " + selected);

        if (Paintcall) {
            Paintcall = false;
            return;
        }
        if (oldsel != selected) {
            // System.out.println("Repainting for move!");
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
        // System.out.println("ExitEvent erhalten !");
        selected = -1;
        mainFrame.repaint();
    }
}