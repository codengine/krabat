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

abstract public class Mainloc2 extends Mainloc {
    // Variablen deklarieren, die in der Location dann nicht mehr erscheinen duerfen

    private static final String[] HMMe = {Start.stringManager.getTranslation("Main_Mainloc2_00000"), Start.stringManager.getTranslation("Main_Mainloc2_00001"), Start.stringManager.getTranslation("Main_Mainloc2_00002")};
    private static final String[] DMMe = {Start.stringManager.getTranslation("Main_Mainloc2_00003"), Start.stringManager.getTranslation("Main_Mainloc2_00004"), Start.stringManager.getTranslation("Main_Mainloc2_00005")};
    private static final String[] NMMe = {Start.stringManager.getTranslation("Main_Mainloc2_00006"), Start.stringManager.getTranslation("Main_Mainloc2_00007"), Start.stringManager.getTranslation("Main_Mainloc2_00008")};
    private static final int M_KONSTANTE = 2;

    // Methodendefinitionen

    // Konstruktor
    public Mainloc2(Start caller) {
        super(caller);
    }

    // Mueller - Anmeckersprueche  				
    public void MuellerMecker(GenericPoint posit) {
        if (mainFrame.sprache == 1) {
            outputText = mainFrame.ifont.TeileText(HMMe[(int) Math.round(Math.random() * M_KONSTANTE)]);
        }
        if (mainFrame.sprache == 2) {
            outputText = mainFrame.ifont.TeileText(DMMe[(int) Math.round(Math.random() * M_KONSTANTE)]);
        }
        if (mainFrame.sprache == 3) {
            outputText = mainFrame.ifont.TeileText(NMMe[(int) Math.round(Math.random() * M_KONSTANTE)]);
        }
        outputTextPos = mainFrame.ifont.CenterText(outputText, posit);
    }

    // Diese Methoden werden erst in den einzelnen Labyrinths implementiert.
    @Override
    abstract public void paintLocation(GenericDrawingContext g);

    @Override
    abstract public void evalMouseEvent(GenericMouseEvent e);

    @Override
    abstract public void evalMouseExitEvent(GenericMouseEvent e);

    @Override
    abstract public void evalMouseMoveEvent(GenericPoint mousePoint);

    @Override
    abstract public void evalKeyEvent(GenericKeyEvent e);
}      	