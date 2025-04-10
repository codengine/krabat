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
import de.codengine.krabat.platform.GenericStorageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StringManager {
    private final Map<Integer, Map<String, String>> translations = new HashMap<>();

    private final GenericStorageManager storageManager;

    public StringManager(GenericStorageManager storageManager) {
        this.storageManager = storageManager;
        translations.put(1, storageManager.loadTranslations("hs.txt"));
        translations.put(2, storageManager.loadTranslations("ds.txt"));
    }

    public void defineThirdLanguage(String abbreviation) {
        translations.put(3, storageManager.loadTranslations(abbreviation + ".txt"));
    }

    public String getTranslation(String key) {
        return Optional.ofNullable(translations.get(Start.language))
                .map(map -> map.get(key))
                .orElseGet(() -> "Missing translation: " + key);
    }
}
