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

import de.codengine.krabat.main.GenericColor;
import de.codengine.krabat.main.GenericRectangle;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericDrawingContext2D;
import de.codengine.krabat.platform.GenericImage;

import java.awt.*;

public class JavaDrawingContext extends GenericDrawingContext {

    private final Graphics g;

    public JavaDrawingContext(Graphics g) {
        this.g = g;
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        g.clearRect(x, y, width, height);
    }

    @Override
    public void drawImage(GenericImage genericImage, int x, int y,
                          int width, int height) {
        Image image = ((JavaImage) genericImage).getImage();
        g.drawImage(image, x, y, width, height, null);
    }

    @Override
    public void drawImage(GenericImage genericImage, int x, int y) {
        Image image = ((JavaImage) genericImage).getImage();
        g.drawImage(image, x, y, null);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawRect(int x1, int y1, int width, int height) {
        g.drawRect(x1, y1, width, height);
    }

    @Override
    public GenericRectangle getClipBounds() {
        Rectangle rect = g.getClipBounds();
        return new GenericRectangle(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public void setClip(int x, int y, int width, int height) {
        g.setClip(x, y, width, height);
    }

    @Override
    public void setClip(GenericRectangle txx) {
        setClip(txx.getX(), txx.getY(), txx.getWidth(), txx.getHeight());
    }

    @Override
    public void setColor(GenericColor inakt) {
        g.setColor(new Color(inakt.getR(), inakt.getG(), inakt.getB()));
    }

    @Override
    public GenericDrawingContext2D get2DContext() {
        return new JavaDrawingContext2D(g);
    }

}
