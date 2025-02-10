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

public class Usermultiple {
    private final Start mainFrame;

    // fuer Anzeige
    private int selected = -1;
    private int oldsel = -1;
    private int Cursorform;
    private boolean Paintcall = false;
    private int yoff;

    // Variablen fuer Fragen
    public int Anzahl;
    public String[] Fragen = new String[10];
    public int[] Ident = new int[10];
    public GenericRectangle[] Positionen = new GenericRectangle[10];
    public int Antwort = 0;

    public boolean user = false;

    // Im Konstruktor Variablen bereitstellen
    public Usermultiple(Start caller) {
        mainFrame = caller;
    }

    // Hier wird neue MC-Routine initialisiert
    public void InitMC(int yoff) {
        // hier wird Init des Cursors beim Aufrufen erzwungen
        Cursorform = 200;
        Anzahl = -1;
        this.yoff = yoff;
    }

    // Hier wird ein MC - Element hinzugefuegt
    public void ExtendMC(String text, GenericRectangle posit, int index) {
        Anzahl++;
        Fragen[Anzahl] = text;
        if (Anzahl == 0) {
            Positionen[Anzahl] = new GenericRectangle(posit.getX(), posit.getY() + yoff,
                    posit.getWidth(), posit.getHeight());
        } else {
            int temp = Positionen[Anzahl - 1].getY() + Positionen[Anzahl - 1].getHeight();
            Positionen[Anzahl] = new GenericRectangle(posit.getX(), temp, posit.getWidth(),
                    posit.getHeight());
        }
        Ident[Anzahl] = index;
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
                            Positionen[i].getX() + mainFrame.scrollx,
                            Positionen[i].getY() + mainFrame.scrolly + 10, 0xffff0000);
                } else {
                    mainFrame.ifont.drawString(g, ("$" + Fragen[i]),
                            Positionen[i].getX() + mainFrame.scrollx,
                            Positionen[i].getY() + mainFrame.scrolly + 10, 0xffb00000);
                }
            }
            oldsel = selected;
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
            return;
        }

        if (oldsel != -1) {
            mainFrame.ifont.drawString(g, ("$" + Fragen[oldsel]),
                    Positionen[oldsel].getX() + mainFrame.scrollx,
                    Positionen[oldsel].getY() + mainFrame.scrolly + 10, 0xffb00000);
        }
        if (oldsel != -1) {
            oldsel = -1;
        }

        if (selected != -1) {
            mainFrame.ifont.drawString(g, ("$" + Fragen[selected]),
                    Positionen[selected].getX() + mainFrame.scrollx,
                    Positionen[selected].getY() + mainFrame.scrolly + 10, 0xffff0000);
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
                    Antwort = i;
                    selected = -1;
                    oldsel = -1;
                    mainFrame.fPlayAnim = true;
                    user = false;
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