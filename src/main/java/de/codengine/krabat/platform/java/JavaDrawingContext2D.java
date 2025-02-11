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

import de.codengine.krabat.main.GenericAlphaComposite;
import de.codengine.krabat.main.GenericColor;
import de.codengine.krabat.platform.GenericDrawingContext2D;
import de.codengine.krabat.platform.GenericImage;

import java.awt.*;

public class JavaDrawingContext2D extends GenericDrawingContext2D {

    private final Graphics2D g;

    public JavaDrawingContext2D(Graphics g) {
        this.g = (Graphics2D) g;
    }

    @Override
    public void setComposite(GenericAlphaComposite ad) {
        AlphaComposite ac = AlphaComposite.getInstance(ad.getRule(), ad.getAplha());
        g.setComposite(ac);
    }

    @Override
    public void drawImage(GenericImage genericImage, int x, int y) {
        Image image = ((JavaImage) genericImage).getImage();
        g.drawImage(image, x, y, null);
    }

    @Override
    public void fillRect(int x1, int y1, int width, int height) {
        g.fillRect(x1, y1, width, height);
    }

    @Override
    public void setColor(GenericColor inakt) {
        g.setColor(new Color(inakt.getR(), inakt.getG(), inakt.getB()));
    }

}
