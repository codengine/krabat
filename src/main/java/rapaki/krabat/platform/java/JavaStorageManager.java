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

package rapaki.krabat.platform.java;

import rapaki.krabat.platform.GenericStorageManager;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;


public class JavaStorageManager extends GenericStorageManager {

    private final boolean loadSaveSupported;

    private final String loadSaveRootPath;

    private final String loadSavePrefix;

    private final String loadSaveSuffix;

    private final boolean slownikSupported;

    private final String slownikRootPath;

    private final boolean propertyStorageSupported;

    private final String propertyRootPath;

    private final String translationsRootPath;

    public JavaStorageManager(boolean loadSaveSupported, String loadSaveRootPath, String loadSavePrefix, String loadSaveSuffix,
                              boolean slownikSupported, String slownikRootPath, boolean propertyStorageSupported, String propertyRootPath,
                              String translationsRootPath) {
        this.loadSaveSupported = loadSaveSupported;
        this.loadSaveRootPath = loadSaveRootPath;
        this.loadSavePrefix = loadSavePrefix;
        this.loadSaveSuffix = loadSaveSuffix;
        this.slownikSupported = slownikSupported;
        this.slownikRootPath = slownikRootPath;
        this.propertyStorageSupported = propertyStorageSupported;
        this.propertyRootPath = propertyRootPath;
        this.translationsRootPath = translationsRootPath;
    }

    public boolean isLoadSaveSupported() {
        return loadSaveSupported;
    }

    public byte[] loadFromFile(int gameIndex) {
        String filename = loadSaveRootPath + File.separator + loadSavePrefix + Integer.toString(gameIndex) + loadSaveSuffix;
        byte[] ret;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(new URI(filename))));
            ret = new byte[fileSize];
            int ptr = 0;
            int status;
            while (ptr < ret.length) {
                status = stream.read(ret, ptr, ret.length - ptr);

                if (status < 0) {
                    throw new IOException("EOF while still expecting data!");
                } else {
                    ptr += status;
                }
            }
            stream.close();

        } catch (IOException e) {
            return new byte[]{};
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new byte[]{};
        }

        return ret;
    }

    public boolean saveToFile(byte[] data, int gameIndex) {
        String filename = loadSaveRootPath + File.separator + loadSavePrefix + Integer.toString(gameIndex) + loadSaveSuffix;
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(new URI(filename))));
            stream.write(data);
            stream.flush();
            stream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSlownikSupported() {
        return slownikSupported;
    }

    public byte[] loadSlownik(String relativeFileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            BufferedInputStream stream = new BufferedInputStream(
                    new FileInputStream(new File(new URI(slownikRootPath + File.separator + relativeFileName))));
            byte[] tmp = new byte[1024];
            int status = 0;
            while (status != -1) {
                status = stream.read(tmp);

                if (status > 0) {
                    baos.write(tmp, 0, status);
                }
            }
            stream.close();


        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new byte[]{};
        }

        return baos.toByteArray();
    }

    public boolean isPropertyStorageSupported() {
        return propertyStorageSupported;
    }

    public void getGameProperties(Properties props) {
        try {
            BufferedInputStream stream = new BufferedInputStream(
                    new FileInputStream(new File(new URI(propertyRootPath + File.separator + "game.properties"))));
            props.load(stream);
        } catch (IOException e) {
            System.out.println("---- Warning: Game properties could not be loaded ----");
            // intentionally empty
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void saveGameProperties(Properties props) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(new URI(propertyRootPath + File.separator + "game.properties"))));
            props.store(stream, "Game properties");
            stream.flush();
            stream.close();
        } catch (IOException e) {
            // intentionally empty
            System.out.println("---- ERROR: Game properties could not be saved ----");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> loadTranslationsFile(String filename) {
        HashMap<String, String> translations = new HashMap<String, String>();

        try {
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(new File(new URI(translationsRootPath + File.separator + filename)))));
            String line = "";
            String key;
            String value;
            int separatorPos;
            while ((line = reader.readLine()) != null) {
                separatorPos = line.indexOf("\t");
                key = line.substring(0, separatorPos).trim();
                value = line.substring(separatorPos + 1); // do not trim, the spaces may be wanted!!!
                translations.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Translations file not found!");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new Error("Translations file not found!");
        }

        return translations;
    }

    public void mergeTranslationsFile(String filename, HashMap<String, String> translations, boolean isFake, String fakePrefix) {

        try {
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(new File(new URI(translationsRootPath + File.separator + filename)))));
            String line = "";
            String key;
            String value;
            int separatorPos;
            while ((line = reader.readLine()) != null) {
                separatorPos = line.indexOf("\t");
                key = line.substring(0, separatorPos).trim();
                value = line.substring(separatorPos + 1); // do not trim, the spaces may be wanted!!!
                if (isFake == true) {
                    value = fakePrefix + " " + value;
                }
                translations.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Translations file not found!");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new Error("Translations file not found!");
        }
    }
}
