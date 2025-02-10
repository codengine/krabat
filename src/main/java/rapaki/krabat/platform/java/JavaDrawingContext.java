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

import rapaki.krabat.main.GenericColor;
import rapaki.krabat.main.GenericRectangle;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericDrawingContext2D;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.platform.GenericImageObserver;

import java.awt.*;

public class JavaDrawingContext extends GenericDrawingContext {

    private final Graphics g;

    public JavaDrawingContext(Graphics g) {
        this.g = g;
    }

    public void clearRect(int x, int y, int width, int height) {
        g.clearRect(x, y, width, height);
    }

    public void drawImage(GenericImage genericImage, int x, int y,
                          int width, int height, GenericImageObserver observer) {
        Image image = ((JavaImage) genericImage).getImage();
        g.drawImage(image, x, y, width, height, null);
    }

    public void drawImage(GenericImage genericImage, int x, int y,
                          GenericImageObserver observer) {
        Image image = ((JavaImage) genericImage).getImage();
        g.drawImage(image, x, y, null);
    }

    public void drawImage(GenericImage genericImage, int x, int y) {
        drawImage(genericImage, x, y, null);
    }

    public void drawImage(GenericImage ktemp, int x, int y,
                          int width, int height) {
        drawImage(ktemp, x, y, width, height, null);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawRect(int x1, int y1, int width, int height) {
        g.drawRect(x1, y1, width, height);
    }

    public void fillRect(int x1, int y1, int width, int height) {
        g.fillRect(x1, y1, width, height);
    }

    public GenericRectangle getClipBounds() {
        Rectangle rect = g.getClipBounds();
        GenericRectangle gRect = new GenericRectangle(
                rect.x, rect.y, rect.width, rect.height);
        return gRect;
    }

    public void setClip(int x, int y, int width, int height) {
        g.setClip(x, y, width, height);
    }

    public void setClip(GenericRectangle txx) {
        setClip(txx.getX(), txx.getY(), txx.getWidth(), txx.getHeight());
    }

    public void setColor(GenericColor inakt) {
        g.setColor(new Color(inakt.getR(), inakt.getG(), inakt.getB()));
    }

    public GenericDrawingContext2D get2DContext() {
        return new JavaDrawingContext2D(g);
    }

}
