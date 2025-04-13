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
import de.codengine.krabat.anims.MainAnim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExitGame extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(ExitGame.class);
    private int selected = -1;
    private int count;
    private final String[] questions = new String[4];
    private final GenericRectangle[] positions = new GenericRectangle[4];
    public boolean active = false;
    private boolean paintCall = false;
    private GenericImage background;
    private int whatAction;

    private final GameProperties gameProperties;

    // Im Konstruktor Variablen bereitstellen
    public ExitGame(Start caller, GameProperties gameProperties) {
        super(caller);
        this.gameProperties = gameProperties;
    }

    // Hier wird der Screen aktiviert
    public void activate(int whatAction) {
        this.whatAction = whatAction;
        initImages();
        mainFrame.setCursor(mainFrame.cursorNormal);
        count = 2;

        questions[0] = Start.STRING_MANAGER.getTranslation("Konc_1");
        questions[1] = Start.STRING_MANAGER.getTranslation("Konc_2");
        questions[2] = Start.STRING_MANAGER.getTranslation("Konc_3");
        positions[0] = new GenericRectangle(205, 202, 150, 50);
        positions[1] = new GenericRectangle(210, 235, 56, 26);
        positions[2] = new GenericRectangle(280, 235, 56, 26);

        active = true;
        mainFrame.repaint();
    }

    // Hintergrund laden
    private void initImages() {
        background = getPicture("gfx/abfrage3.png");
    }

    // Paint - Routine fuer Multiple Choice
    public void paintExit(GenericDrawingContext g) {
        GenericRectangle my;
        my = g.getClipBounds();
        g.setClip(0, 0, 1280, 960);
        paintCall = true;
        evalMouseMoveEvent(mainFrame.mousePoint);
        g.drawImage(background, 190 + mainFrame.scrollX, 190 + mainFrame.scrollY);
        for (int i = 0; i <= count; ++i) {
            if (selected == i || i == 0) {
                mainFrame.imageFont.drawString(g, questions[i],
                        positions[i].getX() + mainFrame.scrollX,
                        positions[i].getY() + mainFrame.scrollY, 0xffff0000);
            } else {
                mainFrame.imageFont.drawString(g, questions[i],
                        positions[i].getX() + mainFrame.scrollX,
                        positions[i].getY() + mainFrame.scrollY, 0xffb00000);
            }
        }
        g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
    }

    // Mouse - Listener fuer Multiple Choice
    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();
        if (e.isLeftClick()) {
            // Linke Maustaste
            if (positions[1].contains(pTemp)) {
                // Es wurde HAJ gewaehlt
                switch (whatAction) {
                    case 1: // Beenden
                        BackgroundMusicPlayer.getInstance().stop();
                        log.debug("Store properties!");
                        gameProperties.saveProperties();
                        System.exit(0);
                        break;
                    case 2: // Neustarten
                        BackgroundMusicPlayer.getInstance().stop();
                        mainFrame.isClipSet = false;
                        background = null;
                        active = false;
                        mainFrame.restart();
                        break;
                    case 3: // Speichern ueber existierend
                        mainFrame.saveGame.saveIsValid = true;
                        active = false;
                        background = null;
                        mainFrame.isClipSet = false;
                        mainFrame.repaint();
                        break;
                }
            }
            if (positions[2].contains(pTemp)) {
                // Es wurde NE gewaehlt
                mainFrame.isClipSet = false;
                background = null;
                active = false;
                mainFrame.repaint();
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        int oldsel = selected;
        selected = -1;
        for (int i = 1; i <= count; ++i) {
            if (positions[i].contains(pTemp)) {
                selected = i;
                break;
            }
        }
        if (paintCall) {
            paintCall = false;
            return;
        }
        if (oldsel != selected) {
            mainFrame.repaint();
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();
        if (key == GenericKeyEvent.VK_ESCAPE) {
            // Esc hat dieselben Auswirkungen wie Ne!!!
            mainFrame.isClipSet = false;
            background = null;
            active = false;
            mainFrame.repaint();
        }
    }
}
      
    
    