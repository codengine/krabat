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

package rapaki.krabat.main;

import rapaki.krabat.platform.GenericStorageManager;

import java.util.Hashtable;
import java.util.Properties;

public class GameProperties {
	
	public static final String CURRENT_GAME_LANGUAGE_INDEX = "CurrentGameLanguageIndex";
	
	public static final String THIRD_GAME_LANGUAGE_SELECTION = "ThirdGameLanguage";
	
	private final GenericStorageManager storageManager;
	
	private final Hashtable<String, String> defaults;
	
	private final Properties props;
	
	public GameProperties(GenericStorageManager storageManager, Hashtable<String, String> defaults) {
		this.storageManager = storageManager;
		this.defaults = defaults;
		props = new Properties();
	}
	
	public void loadProperties() {
		if (storageManager.isPropertyStorageSupported()) {
			storageManager.getGameProperties(props);
		}
	}
	
	public void saveProperties() {
		if (storageManager.isPropertyStorageSupported()) {
			storageManager.saveGameProperties(props);
		}
	}
	
	public String getProperty(String key) {
		return props.getProperty(key, defaults.get(key));
	}
	
	public void setProperty(String key, String value) {
		props.setProperty(key, value);
	}
}
