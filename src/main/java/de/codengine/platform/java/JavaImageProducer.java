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

import de.codengine.platform.GenericImageProducer;

import java.awt.image.ImageProducer;

public class JavaImageProducer extends GenericImageProducer {

    private final ImageProducer producer;

    public JavaImageProducer(ImageProducer producer) {
        this.producer = producer;
    }

    public ImageProducer getProducer() {
        return producer;
    }
}
