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
import de.codengine.krabat.sound.BackgroundMusicPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class Mainlaby extends MainLocation {
    private static final Logger log = LoggerFactory.getLogger(Mainlaby.class);
    public GenericPoint bludTalk = new GenericPoint(0, 0);
    public GenericPoint Pblud = new GenericPoint(0, 0);

    public Borderrect bludRect = new Borderrect(0, 0, 1, 1);

    public int bludFacing = 0;
    public int locIndex = 0;
    public int newloc = 0;

    public boolean hoerterzu = false;
    public boolean bludNimmt = false;

    public final Multiple2 Dialog;

    private static final int ERSCH_KONSTANTE = 10;

    private static final int[] BludExitUp = {53, 59, 60};
    private static final int[] BludExitDown = {52, 58, 60};
    private static final int[] BludExitLeft = {52, 53, 57, 58, 59};
    private static final int[] BludExitRight = {52, 53, 57, 58, 59};

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Mainlaby(Start caller) {
        super(caller);

        Dialog = new Multiple2(mainFrame);
    }

    // Neue Location berechnen, die Blud enthaelt (auuser derselben)
    public void BludLocationLeft() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : BludExitLeft) {
                if (zuffi == j && locIndex != zuffi) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    public void BludLocationRight() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : BludExitRight) {
                if (zuffi == j && locIndex != zuffi) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    public void BludLocationUp() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : BludExitUp) {
                if (zuffi == j && locIndex != zuffi) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    public void BludLocationDown() {
        boolean exit = false;
        int zuffi = 0;
        while (!exit) {
            zuffi = (int) Math.round(Math.random() * 12) + 50;
            for (int j : BludExitDown) {
                if (zuffi == j && locIndex != zuffi) {
                    exit = true;
                    break;
                }
            }
        }
        newloc = zuffi;
    }

    // Definitionen des Action - Arrays
    // 235 - 246 koennen verwendet werden

    // 235 : naechste Location muss Bludnicki zeigen (Signal an die 5 Locations, dass der Kunde erscheint)
    // 236 : im Bild soll ein Blinkern erscheinen, Richtung ist Sache der Location
    // 237 - 239 : Zaehler, wie viele Bilder noch dem Blinkern gefolgt werden muss
    // 240 - 245 : Zaehler, wie schnell ein Blinkern erscheinen soll, wird immer zurueckgesetzt,
    //             wenn Weg verlassen oder bereits mit dem Kunden gesprochen 

    public int BerechneAusgang(boolean oben, boolean unten, boolean links, boolean rechts) {
        do {
            int zuffi = (int) Math.round(Math.random() * 120);
            if (zuffi < 30 && oben) {
                return 12;
            }
            if (zuffi >= 30 && zuffi < 60 && unten) {
                return 6;
            }
            if (zuffi >= 60 && zuffi < 90 && links) {
                return 9;
            }
            if (zuffi >= 90 && rechts) {
                return 3;
            }
        }
        while (true);
    }

    public void Erscheinen(boolean blink) {
        if (mainFrame.actions[235]) {
            // System.out.println ("Jetzt wieder alles deaktivieren...");
            ClearErscheinen();
            return;
        }

        if (!mainFrame.actions[236]) {
            // System.out.println ("Noch kein Blinkern da");

            int i = 240;

            // es wurde normal gelaufen, kein Blinkern erscheinen, also kann keinem gefolgt werden
            while (mainFrame.actions[i]) {
                i++;
            }

            if (i == 245) {
                // wenn es der Zufall so will, genug rumgeirrt, es kann das Blinkern erscheinen
                int zuffi = (int) Math.round(Math.random() * ERSCH_KONSTANTE);
                if (zuffi < 5) {
                    // Herumirrzaehler wieder loeschen
                    for (int f = 240; f <= 245; f++) {
                        mainFrame.actions[f] = false;
                    }
                    mainFrame.actions[236] = true;

                    log.debug("Aber jetzt blinkert es !!");

                }
            } else {
                // Zaehler eins hochsetzen
                mainFrame.actions[i] = true;
            }

            // Blud aus (Sicherheit)
            mainFrame.actions[235] = false;
            return;
        }

        if (!blink) {
            log.debug("Es wurde der richtige Weg verlassen");


            // Tja, Pech gehabt, wer dem Weg nicht folgt

            // Zaehler fuer Erscheinen loeschen
            for (int f = 240; f <= 245; f++) {
                mainFrame.actions[f] = false;
            }

            // Zaehler fuer wielange folgen loeschen
            for (int g = 237; g <= 239; g++) {
                mainFrame.actions[g] = false;
            }

            // Blinkern aus
            mainFrame.actions[236] = false;

            // Blud aus
            mainFrame.actions[235] = false;
            return;
        }

        log.debug("Der richtige Weg wurde befolgt");

        // wir sind ja jetzt auf dem richtigen Weg...
        int x = 237;

        while (mainFrame.actions[x]) {
            x++;
        }

        if (x == 239) {
            // dann sind wir ja jetzt da....

            log.debug("Jetzt kann ein Blud erscheinen");

            // Blud ein
            mainFrame.actions[235] = true;

            // Blinkern aus
            mainFrame.actions[236] = false;

            // Zaehler wielange folgen wieder loeschen
            for (int j = 237; j <= 239; j++) {
                mainFrame.actions[j] = false;
            }
        } else {
            mainFrame.actions[x] = true;
        }

    }

    public void ClearErscheinen() {
        for (int i = 235; i <= 246; i++) {
            mainFrame.actions[i] = false;
        }
    }

    public void BludAction(int AID) {

        // Was soll Krabat machen ?
        switch (AID) {
            case 1:
                // Irrlichter anschauen
                KrabatSagt("Mainlaby_1", bludFacing, 3, 0, 0);
                break;

            case 50:
                // Krabat beginnt MC (Bludnicki benutzen)
                mainFrame.krabat.SetFacing(bludFacing);
                hoerterzu = true;
                mainFrame.isAnimRunning = true;
                // evalMouseMoveEvent (mainFrame.Mousepoint);
                nextActionID = 600;
                break;

            case 150:
                // Bludnicki - Ausreden
                MPersonAusrede(bludFacing);
                break;

            case 155:
                // Gib Krosik an Bludnickis
                mainFrame.isAnimRunning = true;
                hoerterzu = true;
                mainFrame.krabat.nAnimation = 131;
                mainFrame.krabat.SetFacing(bludFacing);
                evalMouseMoveEvent(mainFrame.mousePoint);
                nextActionID = 156;
                break;

            case 156:
                // auf Ende Anim warten
                if (mainFrame.krabat.nAnimation == 0) {
                    nextActionID = 157;
                }
                bludNimmt = true;
                break;

            case 157:
                // Entscheidung, ob nur raus oder auch Spruch
                bludNimmt = false;
                if (!mainFrame.actions[197]) {
                    // nur raus, kein Spruch
                    PersonSagt("Mainlaby_2", bludFacing, 35, 2, 158, bludTalk);
                } else {
                    // Spruch sagen und rausbeamen
                    BackgroundMusicPlayer.getInstance().stop();
                    mainFrame.soundPlayer.PlayFile("sfx/spruch.wav");
                    PersonSagt("Mainlaby_3", bludFacing, 35, 2, 158, bludTalk);
                    mainFrame.actions[215] = true;

                    // hier in den Dialogen die Muehlenfragen rausnehmen
                    mainFrame.actions[195] = true;
                    mainFrame.actions[196] = true;
                    mainFrame.actions[182] = true;
                    mainFrame.actions[183] = true;
                }
                // Krosik - Lockvariable entfernen, jetzt faengt er wieder was bei Haty
                mainFrame.actions[915] = false;
                break;

            case 158:
                // Krabat wird rausgebeamt
                mainFrame.isInventoryCursor = false;
                mainFrame.inventory.vInventory.removeElement(15);
                NeuesBild(17, locIndex);
                break;

            case 160:
                // Irrlichter mit kamuski benutzen
                KrabatSagt("Mainlaby_4", bludFacing, 3, 0, 0);
                break;

            case 600:
                // Multiple - Choice - Routine mit Bludnicki
                Dialog.InitMC(20);
                // 1. Frage
                Dialog.ExtendMC("Mainlaby_12", 1000, 180, new int[]{180, 168}, 610);
                Dialog.ExtendMC("Mainlaby_13", 180, 181, new int[]{181}, 620);
                Dialog.ExtendMC("Mainlaby_14", 181, 182, new int[]{182, 197}, 630);
                Dialog.ExtendMC("Mainlaby_15", 195, 196, new int[]{182, 196}, 630);
                if (!mainFrame.actions[184]) {
                    Dialog.ExtendMC("Mainlaby_16", 182, 183, new int[]{183, 195}, 640);
                } else {
                    Dialog.ExtendMC("Mainlaby_17", 182, 183, new int[]{183, 195}, 640);
                }

                // 2. Frage
                Dialog.ExtendMC("Mainlaby_18", 1000, 185, new int[]{185, 168}, 650);
                Dialog.ExtendMC("Mainlaby_19", 185, 1000, null, 660);

                // 3. Frage (Ende)
                Dialog.ExtendMC("Mainlaby_20", 1000, 186, null, 800);
                Dialog.ExtendMC("Mainlaby_21", 186, 1000, null, 800);

                mainFrame.isMultipleChoiceActive = true;
                mainFrame.isAnimRunning = false;
                nextActionID = 601;
                mainFrame.isClipSet = false;
                mainFrame.repaint();
                break;

            case 601:
                // Ausgewaehltes Multiple-Choice-Ding wird angezeigt
                mainFrame.isAnimRunning = true;
                // evalMouseMoveEvent (mainFrame.Mousepoint);
                outputText = Dialog.Fragen[Dialog.Antwort];
                outputTextPos = mainFrame.imageFont.KrabatText(outputText);
                TalkPerson = 1;
                TalkPause = 2;

                nextActionID = Dialog.ActionID;

                // Fragen zurueckschalten, wegen loop "Pytam #Kertowski mlyn."
                if (mainFrame.actions[183] && nextActionID == 630) {
                    mainFrame.actions[183] = false;
                }
                if (mainFrame.actions[196] && nextActionID == 640) {
                    mainFrame.actions[196] = false;
                }

                break;

            case 610:
                // Reaktion Irrlicht auf 1. Teil 1. Frage
                PersonSagt("Mainlaby_5", 0, 35, 2, 600, bludTalk);
                break;

            case 620:
                // Reaktion Irrlicht auf 2. Teil 1. Frage
                PersonSagt("Mainlaby_6", 0, 35, 2, 600, bludTalk);
                break;

            case 630:
                // Reaktion Irrlicht auf 3. Teil 1. Frage
                PersonSagt("Mainlaby_7", 0, 35, 2, 600, bludTalk);
                break;

            case 640:
                // Reaktion Irrlicht auf 4. Teil 1. Frage
                PersonSagt("Mainlaby_8", 0, 35, 2, 641, bludTalk);
                break;

            case 641:
                // Reaktion Irrlicht auf 4. Teil 1. Frage
                PersonSagt("Mainlaby_9", 0, 35, 2, 600, bludTalk);
                break;

            case 650:
                // Reaktion Irrlicht auf 1. Teil 2. Frage
                PersonSagt("Mainlaby_10", 0, 35, 2, 651, bludTalk);
                break;

            case 651:
                // Skip zu Kolmc
                NeuesBild(17, locIndex);
                hoerterzu = false;
                break;

            case 660:
                // Reaktion Irrlicht auf 2. Teil 3. Frage
                PersonSagt("Mainlaby_11", 0, 35, 2, 600, bludTalk);
                break;

            case 800:
                // MC beenden, wenn zuende gelabert...
                mainFrame.actions[186] = false;

                // hier testen, ob Fragen zurueckgestellt werden sollen
                if (!mainFrame.actions[195] || !mainFrame.actions[196] ||
                        !mainFrame.actions[182] || !mainFrame.actions[183]) {
                    mainFrame.actions[196] = false;
                    mainFrame.actions[182] = false;
                    mainFrame.actions[183] = false;
                }
                mainFrame.isAnimRunning = false;
                nextActionID = 0;
                hoerterzu = false;
                // evalMouseMoveEvent (mainFrame.Mousepoint);
                mainFrame.repaint();
                break;

            default:
                log.error("Falsche Action-ID: {} !", nextActionID);
        }

    }

    // Diese Methoden werden erst in den einzelnen Labyrinths implementiert.
    @Override
    abstract public void paintLocation(GenericDrawingContext g);

    @Override
    abstract public void evalMouseEvent(GenericMouseEvent e);

    @Override
    abstract public void evalMouseExitEvent();

    @Override
    abstract public void evalMouseMoveEvent(GenericPoint mousePoint);

    @Override
    abstract public void evalKeyEvent(GenericKeyEvent e);
}