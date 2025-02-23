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

abstract public class MainLocation2 extends MainLocation {
    // Variablen deklarieren, die in der Location dann nicht mehr erscheinen duerfen
    private static final String[] MMe_TEXTS = {"Mainloc2_1", "Mainloc2_2", "Mainloc2_3"};

    // Methodendefinitionen

    // Konstruktor
    public MainLocation2(Start caller) {
        super(caller);
    }

    // Mueller - Anmeckersprueche  				
    public void MuellerMecker(GenericPoint posit) {
        int random = (int) Math.round(Math.random() * (MMe_TEXTS.length - 1));
        outputText = mainFrame.imageFont.TeileTextKey(MMe_TEXTS[random]);
        outputTextPos = mainFrame.imageFont.CenterText(outputText, posit);
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