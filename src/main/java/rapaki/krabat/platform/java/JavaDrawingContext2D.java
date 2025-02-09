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

import rapaki.krabat.main.GenericAlphaComposite;
import rapaki.krabat.main.GenericColor;
import rapaki.krabat.platform.GenericDrawingContext2D;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.platform.GenericImageObserver;

import java.awt.*;

public class JavaDrawingContext2D extends GenericDrawingContext2D {

	private final Graphics2D g;
	
	public JavaDrawingContext2D(Graphics g) {
		this.g = (Graphics2D) g;
	}

	public void setComposite(GenericAlphaComposite ad) {
		AlphaComposite ac = AlphaComposite.getInstance(ad.getRule(), ad.getAplha());
		g.setComposite(ac);
	}

	public void drawImage(GenericImage genericImage, int x, int y,
			GenericImageObserver observer) {
		Image image = ((JavaImage) genericImage).getImage();
		g.drawImage(image, x, y, null);
	}

	public void fillRect(int x1, int y1, int width, int height) {
		g.fillRect(x1, y1, width, height);
	}

	public void setColor(GenericColor inakt) {
		g.setColor(new Color(inakt.getR(), inakt.getG(), inakt.getB()));
	}

}
