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
import de.codengine.krabat.anims.Mainanim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.platform.GenericImageObserver;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Konc extends Mainanim {
    private int selected = -1;
    private int Anzahl;
    private final String[] Fragen = new String[4];
    private final GenericRectangle[] Positionen = new GenericRectangle[4];
    public boolean active = false;
    private boolean Paintcall = false;
    private GenericImage backgr;
    private int WhatAction;

    private final GenericImageObserver observer = null;

    private final GameProperties gameProperties;

    // Im Konstruktor Variablen bereitstellen
    public Konc(Start caller, GameProperties gameProperties) {
        super(caller);
        this.gameProperties = gameProperties;
    }

    // Hier wird der Screen aktiviert
    public void Activate(int WhatAction) {
        this.WhatAction = WhatAction;
        InitImages();
        mainFrame.setCursor(mainFrame.Normal);
        Anzahl = 2;

        if (mainFrame.sprache == 1) {
            Fragen[0] = Start.stringManager.getTranslation("Main_Konc_00000");
            Fragen[1] = Start.stringManager.getTranslation("Main_Konc_00001");
            Fragen[2] = Start.stringManager.getTranslation("Main_Konc_00002");
            Positionen[0] = new GenericRectangle(205, 202, 150, 50);
            Positionen[1] = new GenericRectangle(210, 235, 56, 26);
            Positionen[2] = new GenericRectangle(280, 235, 56, 26);
        } else if (mainFrame.sprache == 2) {
            Fragen[0] = Start.stringManager.getTranslation("Main_Konc_00003");
            Fragen[1] = Start.stringManager.getTranslation("Main_Konc_00004");
            Fragen[2] = Start.stringManager.getTranslation("Main_Konc_00005");
            Positionen[0] = new GenericRectangle(200, 202, 150, 50);
            Positionen[1] = new GenericRectangle(217, 235, 36, 26);
            Positionen[2] = new GenericRectangle(280, 235, 56, 26);
        } else {
            Fragen[0] = Start.stringManager.getTranslation("Main_Konc_00006");
            Fragen[1] = Start.stringManager.getTranslation("Main_Konc_00007");
            Fragen[2] = Start.stringManager.getTranslation("Main_Konc_00008");
            Positionen[0] = new GenericRectangle(200, 202, 150, 50);
            Positionen[1] = new GenericRectangle(210, 235, 56, 26);
            Positionen[2] = new GenericRectangle(280, 235, 56, 26);
        }
        active = true;
        mainFrame.repaint();
    }

    // Hintergrund laden
    private void InitImages() {
        backgr = getPicture("gfx/abfrage3.gif");
    }

    // Paint - Routine fuer Multiple Choice
    public void paintExit(GenericDrawingContext g) {
        GenericRectangle my;
        my = g.getClipBounds();
        g.setClip(0, 0, 1280, 960);
        Paintcall = true;
        evalMouseMoveEvent(mainFrame.Mousepoint);
        g.drawImage(backgr, 190 + mainFrame.scrollx, 190 + mainFrame.scrolly, observer);
        for (int i = 0; i <= Anzahl; ++i) {
            if (selected == i || i == 0) {
                mainFrame.ifont.drawString(g, Fragen[i],
                        Positionen[i].getX() + mainFrame.scrollx,
                        Positionen[i].getY() + mainFrame.scrolly, 0xffff0000);
            } else {
                mainFrame.ifont.drawString(g, Fragen[i],
                        Positionen[i].getX() + mainFrame.scrollx,
                        Positionen[i].getY() + mainFrame.scrolly, 0xffb00000);
            }
        }
        g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
    }

    // Mouse - Listener fuer Multiple Choice
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (e.isLeftClick()) {
            // Linke Maustaste
            if (Positionen[1].contains(pTemp)) {
                // Es wurde HAJ gewaehlt
                switch (WhatAction) {
                    case 1: // Beenden
                        BackgroundMusicPlayer.getInstance().stop();
                        // mainFrame.setVisible(false);
                        // mainFrame.dispose();
                        System.out.println("Store properties!");
                        gameProperties.saveProperties();
                        System.exit(0);
                        break;
                    case 2: // Neustarten
                        BackgroundMusicPlayer.getInstance().stop();
                        mainFrame.Clipset = false;
                        backgr = null;
                        active = false;
                        mainFrame.restart();
                        break;
                    case 3: // Speichern ueber existierend
                        mainFrame.speichern.saveIsValid = true;
                        active = false;
                        backgr = null;
                        mainFrame.Clipset = false;
                        mainFrame.repaint();
                        break;
                }
            }
            if (Positionen[2].contains(pTemp)) {
                // Es wurde NE gewaehlt
                mainFrame.Clipset = false;
                backgr = null;
                active = false;
                mainFrame.repaint();
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        int oldsel = selected;
        selected = -1;
        for (int i = 1; i <= Anzahl; ++i) {
            if (Positionen[i].contains(pTemp)) {
                selected = i;
                break;
            }
        }
        if (Paintcall) {
            Paintcall = false;
            return;
        }
        if (oldsel != selected) {
            mainFrame.repaint();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            // Esc hat dieselben Auswirkungen wie Ne!!!
            mainFrame.Clipset = false;
            backgr = null;
            active = false;
            mainFrame.repaint();
        }
    }
}
      
    
    