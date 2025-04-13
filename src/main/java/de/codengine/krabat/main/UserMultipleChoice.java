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
    private int oldSelected = -1;
    private int cursorShape;
    private boolean paintCall = false;
    private int yOff;

    // Variablen fuer Fragen
    public int count;
    public final String[] questions = new String[10];
    public final int[] ident = new int[10];
    public final GenericRectangle[] positions = new GenericRectangle[10];
    public int answer = 0;

    public boolean user = false;

    // Im Konstruktor Variablen bereitstellen
    public UserMultipleChoice(Start caller) {
        mainFrame = caller;
    }

    // Hier wird neue MC-Routine initialisiert
    public void initMC(int yOff) {
        // hier wird Init des Cursors beim Aufrufen erzwungen
        cursorShape = 200;
        count = -1;
        this.yOff = yOff;
    }

    // Hier wird ein MC - Element hinzugefuegt
    public void extend(String langKey, GenericRectangle position, int index) {
        String text = mainFrame.imageFont.splitTextKey(langKey);
        count++;
        questions[count] = text;
        if (count == 0) {
            positions[count] = new GenericRectangle(position.getX(), position.getY() + yOff,
                    position.getWidth(), position.getHeight());
        } else {
            int temp = positions[count - 1].getY() + positions[count - 1].getHeight();
            positions[count] = new GenericRectangle(position.getX(), temp, position.getWidth(),
                    position.getHeight());
        }
        ident[count] = index;
    }

    // Paint - Routine fuer Multiple Choice
    public void paintMultiple(GenericDrawingContext g) {
        GenericRectangle my;
        my = g.getClipBounds();
        g.setClip(0, 0, 1284, 964);

        // 1.Aufruf, zuerst alles Zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            for (int i = 0; i <= count; ++i) {
                if (selected == i) {
                    mainFrame.imageFont.drawString(g, "$" + questions[i],
                            positions[i].getX() + mainFrame.scrollX,
                            positions[i].getY() + mainFrame.scrollY + 10, 0xffff0000);
                } else {
                    mainFrame.imageFont.drawString(g, "$" + questions[i],
                            positions[i].getX() + mainFrame.scrollX,
                            positions[i].getY() + mainFrame.scrollY + 10, 0xffb00000);
                }
            }
            oldSelected = selected;
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
            return;
        }

        if (oldSelected != -1) {
            mainFrame.imageFont.drawString(g, "$" + questions[oldSelected],
                    positions[oldSelected].getX() + mainFrame.scrollX,
                    positions[oldSelected].getY() + mainFrame.scrollY + 10, 0xffb00000);
        }
        if (oldSelected != -1) {
            oldSelected = -1;
        }

        if (selected != -1) {
            mainFrame.imageFont.drawString(g, "$" + questions[selected],
                    positions[selected].getX() + mainFrame.scrollX,
                    positions[selected].getY() + mainFrame.scrollY + 10, 0xffff0000);
        }

        if (selected != -1) {
            oldSelected = selected;
        }
        g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
    }

    // Mouse - Listener fuer Multiple Choice
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (e.isLeftClick()) {
            // Linke Maustaste
            for (int i = 0; i <= count; ++i) {
                if (positions[i].contains(pTemp)) {
                    answer = i;
                    selected = -1;
                    oldSelected = -1;
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
        if (cursorShape != 0) {
            cursorShape = 0;
            mainFrame.setCursor(mainFrame.cursorNormal);
        }

        selected = -1;
        for (int i = 0; i <= count; ++i) {
            if (positions[i].contains(pTemp)) {
                selected = i;
                break;
            }
        }

        if (paintCall) {
            paintCall = false;
            return;
        }
        if (oldSelected != selected) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent() {
        selected = -1;
        mainFrame.repaint();
    }
}