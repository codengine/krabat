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

public class UserMultipleChoice {
    private final Start mainFrame;

    // fuer Anzeige
    private int selected = -1;
    private int oldsel = -1;
    private int Cursorform;
    private boolean Paintcall = false;
    private int yoff;

    // Variablen fuer Fragen
    public int Anzahl;
    public final String[] Fragen = new String[10];
    public final int[] Ident = new int[10];
    public final GenericRectangle[] Positionen = new GenericRectangle[10];
    public int Antwort = 0;

    public boolean user = false;

    // Im Konstruktor Variablen bereitstellen
    public UserMultipleChoice(Start caller) {
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
    public void ExtendMC(String langKey, GenericRectangle posit, int index) {
        String text = mainFrame.imageFont.TeileTextKey(langKey);
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
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            for (int i = 0; i <= Anzahl; ++i) {
                if (selected == i) {
                    mainFrame.imageFont.drawString(g, "$" + Fragen[i],
                            Positionen[i].getX() + mainFrame.scrollX,
                            Positionen[i].getY() + mainFrame.scrollY + 10, 0xffff0000);
                } else {
                    mainFrame.imageFont.drawString(g, "$" + Fragen[i],
                            Positionen[i].getX() + mainFrame.scrollX,
                            Positionen[i].getY() + mainFrame.scrollY + 10, 0xffb00000);
                }
            }
            oldsel = selected;
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
            return;
        }

        if (oldsel != -1) {
            mainFrame.imageFont.drawString(g, "$" + Fragen[oldsel],
                    Positionen[oldsel].getX() + mainFrame.scrollX,
                    Positionen[oldsel].getY() + mainFrame.scrollY + 10, 0xffb00000);
        }
        if (oldsel != -1) {
            oldsel = -1;
        }

        if (selected != -1) {
            mainFrame.imageFont.drawString(g, "$" + Fragen[selected],
                    Positionen[selected].getX() + mainFrame.scrollX,
                    Positionen[selected].getY() + mainFrame.scrollY + 10, 0xffff0000);
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
        if (e.isLeftClick()) {
            // Linke Maustaste
            for (int i = 0; i <= Anzahl; ++i) {
                if (Positionen[i].contains(pTemp)) {
                    Antwort = i;
                    selected = -1;
                    oldsel = -1;
                    mainFrame.isAnimRunning = true;
                    user = false;
                    mainFrame.isClipSet = false;
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
            mainFrame.setCursor(mainFrame.cursorNormal);
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

    public void evalMouseExitEvent() {
        // System.out.println("ExitEvent erhalten !");
        selected = -1;
        mainFrame.repaint();
    }
}