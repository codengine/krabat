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

import de.codengine.krabat.Start;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.platform.GenericImageFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JavaImageFetcher extends GenericImageFetcher {

    private static final Logger log = LoggerFactory.getLogger(JavaImageFetcher.class);
    private final Path workingDir;
    private final Path langPath;
    private final Component comp;

    public JavaImageFetcher(Path workingDir, Path langPath, Component comp) {
        this.workingDir = workingDir;
        this.langPath = langPath;
        this.comp = comp;
    }

    @Override
    public GenericImage fetchImage(String relativePath, boolean useLang) {

        MediaTracker tracker = new MediaTracker(comp);

        Image img = null;

        Path filePath = null;

        if (useLang) {
            filePath = langPath.resolve(getLangAbbreviation()).resolve(relativePath);
            if(!Files.exists(filePath)) {
                log.warn("Translated image {} not found", filePath);
                filePath = null;
            }
        }

        if(filePath == null) {
            filePath = workingDir.resolve(relativePath);
        }

        // For the application, the URL will typically start with "file:///" and the user.dir
        // files from a .jar might look like this
        // ReturnImage = getToolkit().getImage ("jar:file:///" + System.getProperty("user.dir") + "!" + Filename);
        try {
            img = comp.getToolkit().getImage(filePath.toFile().toString());
            tracker.addImage(img, 0);
            tracker.waitForAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new JavaImage(img);
    }

    private String getLangAbbreviation() {
        switch (Start.language) {
            case 1:
                return "hs";
            case 2:
                return "ds";
            default:
                return Start.thirdGameLanguage;
        }
    }
}
