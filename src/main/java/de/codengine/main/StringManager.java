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

import de.codengine.platform.GenericStorageManager;

import java.util.HashMap;

public class StringManager {

    private final HashMap<String, String> translations;

    private final GenericStorageManager storageManager;

    public StringManager(GenericStorageManager storageManager) {
        this.storageManager = storageManager;
        translations = storageManager.loadTranslationsFile("stringtable_common.txt");
    }

    public void defineThirdLanguage(String filename, boolean isFake, String fakePrefix) {
        storageManager.mergeTranslationsFile(filename, translations, isFake, fakePrefix);
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }
}
