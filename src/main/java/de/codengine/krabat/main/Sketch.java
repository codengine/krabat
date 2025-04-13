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

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.anims.MainAnim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Sketch extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(Sketch.class);
    private GenericImage sketch;

    private int counter = 0;

    // Sachen, dies hier nicht gibt, weil nicht von Mainloc abgeleitet
    private int nextActionID;
    private String outputText = "";
    private int cusorShape;
    private GenericPoint outputTextPos;
    private int talkPause;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Sketch(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        initImages();

        nextActionID = 10;

        mainFrame.freeze(false);
    }

    // Bilder vorbereiten
    private void initImages() {
        sketch = getPicture("gfx-dd/kapala/skica.png");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintSketch(GenericDrawingContext g) {

        // Karte - Background zeichnen
        if (!mainFrame.isClipSet) {
            mainFrame.isClipSet = true;
            g.setClip(0, 0, 1284, 484);
            cusorShape = 200;
            evalMouseMoveEvent();
        }

        g.drawImage(sketch, mainFrame.scrollX, mainFrame.scrollY);

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, 0xff00ff00); // Krabats Frabe
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.isClipSet = false;
                outputText = "";
            }
        }

        if (talkPause > 0 && mainFrame.talkCount < 1) {
            talkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && talkPause < 1 && mainFrame.talkCount < 1) {
            doAction();
        }
    }

    @SuppressWarnings("EmptyMethod")
    public void evalMouseExitEvent() {
        //FIXME: Why no handling here?
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent ignored) {
        deactivate();
    }

    public void evalMouseMoveEvent() {
        if (cusorShape == 20) {
            return;
        }
        cusorShape = 20;
        mainFrame.setCursor(mainFrame.cursorNone);
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();
        if (key == GenericKeyEvent.VK_ESCAPE) {
            deactivate();
        }
    }

    // Deaktivieren //////////
    private void deactivate() {
        mainFrame.destructLocation(108);
        mainFrame.isClipSet = false;
        mainFrame.whatScreen = ScreenType.NONE;
        cusorShape = 200;
        mainFrame.repaint();
    }

    private void doAction() {

        switch (nextActionID) {
            case 10:
                // bisschen warten
                counter = 10;
                nextActionID = 20;
                break;

            case 20:
                // jetzt Text
                if (--counter > 1) {
                    break;
                }

                if (!mainFrame.actions[633]) // Text nur 1x sagen, wenn gefunden
                {
                    outputText = mainFrame.imageFont.splitTextKey("Skica_1");
                    outputTextPos = mainFrame.imageFont.centerText(outputText, new GenericPoint(320, 200));

                    mainFrame.actions[633] = true;
                    counter = 0;
                } else {
                    counter = 50;
                }
                nextActionID = 30;
                break;

            case 30:
                // und schluss, wenn bis hierhin gekommen
                if (--counter > 1) {
                    break; // warten, falls Counter gesetzt wurde
                }
                deactivate();
                break;

            default:
                log.error("Wrong NextActionID in main/Skica! nextActionID = {}", nextActionID);
                break;
        }
    }
}