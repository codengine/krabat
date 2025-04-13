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

public class MultipleChoice  // Turrican II laesst gruessen!!!!!!
{
    private final Start mainFrame;

    // fuer Anzeige
    private int selected = -1;
    private int oldSelected = -1;
    private int cursorShape;
    private boolean paintCall = false;
    private int yOff;

    // Variable fuer maximale MC-Groesse
    private static final int MC_SIZE = 10;

    // Variablen fuer Fragen
    private int count;
    public final String[] questions = new String[MC_SIZE];
    private final GenericRectangle[] positions = new GenericRectangle[MC_SIZE];
    private final int[] nextActionIds = new int[MC_SIZE];
    private final int[][] actionVariables = new int[MC_SIZE][10];

    public int answer = 0;
    public int actionId = 0;

    // Im Konstruktor Variablen bereitstellen
    public MultipleChoice(Start caller) {
        mainFrame = caller;
    }

    // Hier wird neue MC-Routine initialisiert
    public void initMC(int yOff) {
        // hier wird Init des Cursors beim Aufrufen erzwungen
        cursorShape = 200;
        count = -1;
        this.yOff = yOff;
    }

    // Hier wird ein MC - Element hinzugefuegt mit automatischer Breite
    public void extend(String langKey, int active, int asked, int[] successors, int nextActionId) {
        // hier testen, ob diese Frage schon interessant ist, sonst zurueckspringen
        if (active < 1000 && !mainFrame.actions[active]) {
            return;
        }

        // hier noch testen, ob die Frage schon gefrat wurde und deshalb rausfaellt
        if (asked < 1000 && mainFrame.actions[asked]) {
            return;
        }

        // Anzahl Fragen um 1 erhoehen
        count++;

        // String merken
        String text = Start.STRING_MANAGER.getTranslation(langKey);
        questions[count] = mainFrame.imageFont.splitText(text);

        // Rectangle je nach Position des Textes festlegen
        // 1. Rectangle extra
        if (count == 0) {
            positions[count] = new GenericRectangle(0, yOff, 639, 40 + (mainFrame.imageFont.getLineCount(questions[count]) - 1) * 27);
        } else {
            // folgende Rects immer anschliessend
            int temp = positions[count - 1].getY() + positions[count - 1].getHeight();
            positions[count] = new GenericRectangle(0, temp, 639, 40 + (mainFrame.imageFont.getLineCount(questions[count]) - 1) * 27);
        }

        // nextActionID merken (wird zurueckgegeben bei Erfolg)
        nextActionIds[count] = nextActionId;

        // hier das Array fuer alle Nachfolger initialisieren (NULL = kein Array)
        for (int i = 0; i < 10; i++) {
            if (successors != null) {
                if (i < successors.length) {
                    actionVariables[count][i] = successors[i];
                } else {
                    actionVariables[count][i] = 1000; // 1000 -> ungueltig !
                }
            } else {
                actionVariables[count][i] = 1000; // 1000 -> ungueltig !
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
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);

            for (int i = 0; i <= count; ++i) {
                if (selected == i) {
                    mainFrame.imageFont.drawString(g, "$" + questions[i],
                            positions[i].getX() + mainFrame.scrollX + 30,
                            positions[i].getY() + mainFrame.scrollY + 10, 1);
                } else {
                    mainFrame.imageFont.drawString(g, "$" + questions[i],
                            positions[i].getX() + mainFrame.scrollX + 30,
                            positions[i].getY() + mainFrame.scrollY + 10, 0xff00b000);
                }
            }
            oldSelected = selected;
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
            return;
        }

        if (oldSelected != -1) {
            mainFrame.imageFont.drawString(g, "$" + questions[oldSelected],
                    positions[oldSelected].getX() + mainFrame.scrollX + 30,
                    positions[oldSelected].getY() + mainFrame.scrollY + 10, 0xff00b000);
        }
        if (oldSelected != -1) {
            oldSelected = -1;
        }

        if (selected != -1) {
            mainFrame.imageFont.drawString(g, "$" + questions[selected],
                    positions[selected].getX() + mainFrame.scrollX + 30,
                    positions[selected].getY() + mainFrame.scrollY + 10, 1);
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
                    // Antwort angeben fuer die Location
                    answer = i;
                    actionId = nextActionIds[i];

                    // Actionarray bearbeiten
                    for (int f = 0; f < 10; f++) {
                        if (actionVariables[i][f] < 1000) {
                            mainFrame.actions[actionVariables[i][f]] = true;
                        }
                    }

                    // MC-Klasse deaktivieren und alles zuruecksetzen
                    selected = -1;
                    oldSelected = -1;
                    mainFrame.isAnimRunning = true;
                    mainFrame.isMultipleChoiceActive = false;
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