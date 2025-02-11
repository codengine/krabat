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

import de.codengine.krabat.main.GenericFilteredImageSource;
import de.codengine.krabat.main.GenericImageFilter;
import de.codengine.krabat.main.GenericMemoryImageSource;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.platform.GenericCursor;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.platform.GenericToolkitImpl;

import java.awt.*;
import java.awt.image.*;

public class JavaToolkitImpl extends GenericToolkitImpl {

    private final Component comp;

    public JavaToolkitImpl(Component comp) {
        this.comp = comp;
    }

    @Override
    public void prepareImage(GenericImage genericImage) {
        Image img = ((JavaImage) genericImage).getImage();
        comp.prepareImage(img, null);
    }

    @Override
    public int checkImage(GenericImage genericImage) {
        Image img = ((JavaImage) genericImage).getImage();
        return comp.checkImage(img, null);
    }

    @Override
    public GenericCursor createCustomCursor(GenericImage genericImage,
                                            GenericPoint hotSpot, String name) {
        Image img = ((JavaImage) genericImage).getImage();
        Point point = new Point(hotSpot.x, hotSpot.y);
        Cursor cursor = comp.getToolkit().createCustomCursor(img, point, name);
        return new JavaCursor(cursor);
    }

    @Override
    public GenericImage createImage(
            GenericMemoryImageSource gen) {
        MemoryImageSource src = new MemoryImageSource(
                gen.getW(),
                gen.getH(),
                gen.getPix(),
                gen.getOff(),
                gen.getScan());
        Image img = comp.getToolkit().createImage(src);
        return new JavaImage(img);
    }

    @Override
    public GenericImage createImage(
            GenericFilteredImageSource gen) {
        ImageProducer producer = ((JavaImageProducer) gen.getProducer()).getProducer();
        FilteredImageSource src = new FilteredImageSource(producer, new ImageFilterImpl(gen.getFilter()));
        Image img = comp.getToolkit().createImage(src);
        return new JavaImage(img);
    }

    @Override
    public GenericImage createImage(int i, int j) {
        Image img = comp.createImage(i, j);
        return new JavaImage(img);
    }

    @Override
    public void grabPixelsFromImage(GenericImage actualImage, int x, int y,
                                    int w, int h, int[] pix, int off, int scansize) {
        Image img = ((JavaImage) actualImage).getImage();
        PixelGrabber pg = new PixelGrabber(img, x, y, w, h, pix, off, scansize);
        boolean success = false;
        try {
            success = pg.grabPixels();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (!success) {
            System.err.println("Error grabbing pixels!");
        }
    }

    static class ImageFilterImpl extends RGBImageFilter {

        private final GenericImageFilter impl;

        public ImageFilterImpl(GenericImageFilter impl) {
            this.impl = impl;
        }

        @Override
        public int filterRGB(int x, int y, int rgb) {
            return impl.filterRGB(rgb);
        }
    }
}
