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

import de.codengine.Start;
import de.codengine.anims.Mainanim;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;

public class Skica extends Mainanim {
    private GenericImage skizze;

    private int Counter = 0;

    // Sachen, dies hier nicht gibt, weil nicht von Mainloc abgeleitet
    private int nextActionID;
    private String outputText = "";
    private int Cursorform;
    private GenericPoint outputTextPos;
    private int TalkPause;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Skica(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        InitImages();

        nextActionID = 10;

        mainFrame.Freeze(false);
    }

    // Bilder vorbereiten
    private void InitImages() {
        skizze = getPicture("gfx-dd/kapala/skica.gif");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintSkizze(GenericDrawingContext g) {

        // Karte - Background zeichnen
        if (mainFrame.Clipset == false) {
            mainFrame.Clipset = true;
            g.setClip(0, 0, 1284, 484);
            Cursorform = 200;
            evalMouseMoveEvent(mainFrame.Mousepoint);
        }

        g.drawImage(skizze, mainFrame.scrollx, mainFrame.scrolly, null);

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            // System.out.println ("Skica: Trying to draw text.");
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, 0xff00ff00); // Krabats Frabe
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
            }
        }

        if ((TalkPause > 0) && (mainFrame.talkCount < 1)) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if ((nextActionID != 0) && (TalkPause < 1) && (mainFrame.talkCount < 1)) {
            DoAction();
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        // GenericPoint pTemp = e.getPoint ();

        if (e.getModifiers() != GenericInputEvent.BUTTON3_MASK) {
            Deactivate();
            return;
        } else {
            Deactivate();
            return;
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.Nix);
        }
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
            return;
        }
    }

    // Deaktivieren //////////
    private void Deactivate() {
        mainFrame.DestructLocation(108);
        mainFrame.Clipset = false;
        mainFrame.whatScreen = 0;
        Cursorform = 200;
        mainFrame.repaint();
    }

    private void DoAction() {

        switch (nextActionID) {
            case 10:
                // bisschen warten
                Counter = 10;
                nextActionID = 20;
                break;

            case 20:
                // jetzt Text
                if ((--Counter) > 1) {
                    break;
                }

                if (mainFrame.Actions[633] == false) // Text nur 1x sagen, wenn gefunden
                {
                    switch (mainFrame.sprache) {
                        case 1: // Obersorbisch
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Main_Skica_00000"));
                            break;

                        case 2: // Niedersorbisch
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Main_Skica_00001"));
                            break;

                        case 3: // Deutsch
                            outputText = mainFrame.ifont.TeileText(Start.stringManager.getTranslation("Main_Skica_00002"));
                            break;
                    }
                    outputTextPos = mainFrame.ifont.CenterText(outputText, new GenericPoint(320, 200));

                    mainFrame.Actions[633] = true;
                    Counter = 0;
                } else {
                    Counter = 50;
                }
                nextActionID = 30;
                break;

            case 30:
                // und schluss, wenn bis hierhin gekommen
                if ((--Counter) > 1) {
                    break; // warten, falls Counter gesetzt wurde
                }
                Deactivate();
                break;

            default:
                System.out.println("Wrong NextActionID in main/Skica!");
                break;
        }
    }
}