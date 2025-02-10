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

package de.codengine.anims;

import de.codengine.Start;
import de.codengine.platform.GenericImage;
import de.codengine.platform.GenericImageFetcher;


public class Mainanim {
    // Objekt auf "start" - sollte das hier rein (Geschwindigkeit ???)
    public Start mainFrame;

    private GenericImageFetcher imageFetcher;

    // Konstruktor
    public Mainanim(Start caller) {
        mainFrame = caller;
        imageFetcher = mainFrame.imageFetcher;
    }

    // Methode zum Laden (registrieren) eines Bildes
    public GenericImage getPicture(String Filename) {
        return imageFetcher.fetchImage(Filename);
    }

    public void cleanup() {
        // override for cleanup actions
    }
}