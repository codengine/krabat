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

package de.codengine.krabat.platform.java;

import de.codengine.krabat.platform.GenericStorageManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;


public class JavaStorageManager extends GenericStorageManager {

    private static final Logger log = LoggerFactory.getLogger(JavaStorageManager.class);
    private final boolean loadSaveSupported;

    private final Path loadSaveRootPath;

    private final String loadSavePrefix;

    private final String loadSaveSuffix;

    private final boolean slownikSupported;

    private final Path slownikRootPath;

    private final boolean propertyStorageSupported;

    private final Path propertyRootPath;

    private final Path translationsRootPath;

    public JavaStorageManager(boolean loadSaveSupported, Path loadSaveRootPath, String loadSavePrefix, String loadSaveSuffix,
                              boolean slownikSupported, Path slownikRootPath, boolean propertyStorageSupported, Path propertyRootPath,
                              Path translationsRootPath) {
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

    @Override
    public boolean isLoadSaveSupported() {
        return loadSaveSupported;
    }

    @Override
    public byte[] loadFromFile(int gameIndex) {
        File file = loadSaveRootPath.resolve(loadSavePrefix + gameIndex + loadSaveSuffix).toFile();
        byte[] ret;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
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
        }

        return ret;
    }

    @Override
    public boolean saveToFile(byte[] data, int gameIndex) {
        File file = loadSaveRootPath.resolve(loadSavePrefix + gameIndex + loadSaveSuffix).toFile();
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            stream.write(data);
            stream.flush();
            stream.close();
            return true;
        } catch (IOException e) {
            log.error("Could not save data to {}", file.getAbsolutePath(), e);
            return false;
        }
    }

    @Override
    public boolean isSlownikSupported() {
        return slownikSupported;
    }

    @Override
    public byte[] loadSlownik(String relativeFileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        File file = slownikRootPath.resolve(relativeFileName).toFile();
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
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
            log.error("Could not load slownik from {}", file.getAbsolutePath(), e);
            return new byte[]{};
        }

        return baos.toByteArray();
    }

    @Override
    public boolean isPropertyStorageSupported() {
        return propertyStorageSupported;
    }

    private Path getPropertyPath() {
        return propertyRootPath.resolve("game.properties");
    }

    @Override
    public void getGameProperties(Properties props) {
        File file = getPropertyPath().toFile();
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
            props.load(stream);
        } catch (IOException e) {
            System.out.println("---- Warning: Game properties could not be loaded ----");
            // intentionally empty
        }
    }

    @Override
    public void saveGameProperties(Properties props) {
        File file = getPropertyPath().toFile();
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
            props.store(stream, "Game properties");
            stream.flush();
            stream.close();
        } catch (IOException e) {
            // intentionally empty
            System.out.println("---- ERROR: Game properties could not be saved ----");
        }
    }

    private Path getTranslationPath(String filename) {
        return translationsRootPath.resolve(filename);
    }

    @Override
    public HashMap<String, String> loadTranslationsFile(String filename) {
        HashMap<String, String> translations = new HashMap<String, String>();

        File file = getTranslationPath(filename).toFile();

        try {
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
            String line = "";
            String key;
            String value;
            int separatorPos;
            while ((line = reader.readLine()) != null) {
                separatorPos = line.indexOf('\t');
                key = line.substring(0, separatorPos).trim();
                value = line.substring(separatorPos + 1); // do not trim, the spaces may be wanted!!!
                translations.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            log.error("Could not load translations from {}", file.getAbsolutePath(), e);
            throw new Error("Translations file not found!");
        }

        return translations;
    }

    @Override
    public void mergeTranslationsFile(String filename, HashMap<String, String> translations, boolean isFake, String fakePrefix) {
        File file = getTranslationPath(filename).toFile();
        try {
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new FileReader(file)));
            String line = "";
            String key;
            String value;
            int separatorPos;
            while ((line = reader.readLine()) != null) {
                separatorPos = line.indexOf('\t');
                key = line.substring(0, separatorPos).trim();
                value = line.substring(separatorPos + 1); // do not trim, the spaces may be wanted!!!
                if (isFake) {
                    value = fakePrefix + " " + value;
                }
                translations.put(key, value);
            }
            reader.close();
        } catch (IOException e) {
            log.error("Could not load translations from {}", file.getAbsolutePath(), e);
            throw new Error("Translations file not found!");
        }
    }
}
