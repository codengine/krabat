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

package de.codengine.krabat.platform;

import java.util.HashMap;
import java.util.Properties;

public abstract class GenericStorageManager {

    protected static final int fileSize = 44100;

    public int getFileSize() {
        return fileSize;
    }

    public abstract boolean isLoadSaveSupported();

    public abstract boolean isSlownikSupported();

    public abstract boolean isPropertyStorageSupported();

    public abstract byte[] loadFromFile(int gameIndex);

    public abstract boolean saveToFile(byte[] data, int gameIndex);

    public abstract byte[] loadSlownik(String relativeFileName);

    public abstract void getGameProperties(Properties props);

    public abstract void saveGameProperties(Properties props);

    public abstract HashMap<String, String> loadTranslationsFile(String filename);

    public abstract void mergeTranslationsFile(String filename, HashMap<String, String> translations, boolean isFake, String fakePrefix);

}
