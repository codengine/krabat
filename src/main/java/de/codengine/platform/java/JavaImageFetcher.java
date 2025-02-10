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

package de.codengine.platform.java;

import de.codengine.platform.GenericImage;
import de.codengine.platform.GenericImageFetcher;

import java.awt.*;
import java.nio.file.Path;

public class JavaImageFetcher extends GenericImageFetcher {

    private final Path workingDir;

    private final Component comp;

    public JavaImageFetcher(Path workingDir, Component comp) {
        this.workingDir = workingDir;
        this.comp = comp;
    }

    @Override
    public GenericImage fetchImage(String relativePath) {

        MediaTracker tracker = new MediaTracker(comp);

        Image img = null;

        String filePath = workingDir.resolve(relativePath.substring(0, relativePath.length() - 3) + "png").toFile().toString();

        // For the application, the URL will typically start with "file:///" and the user.dir
        // files from a .jar might look like this
        // ReturnImage = getToolkit().getImage ("jar:file:///" + System.getProperty("user.dir") + "!" + Filename);
        try {
            img = comp.getToolkit().getImage(filePath);
            tracker.addImage(img, 0);
            tracker.waitForAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        JavaImage retImage = new JavaImage(img);

        return retImage;
    }
}
