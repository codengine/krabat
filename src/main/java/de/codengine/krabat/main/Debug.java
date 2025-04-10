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

package de.codengine.krabat.main;

import de.codengine.krabat.platform.GenericDrawingContext;

import java.time.Clock;
import java.time.Instant;

@SuppressWarnings("unused")
public class Debug {
    private static final Clock CLOCK = Clock.systemDefaultZone();
    public static final boolean enabled = false;

    private Debug() {
    }

    // private Start mainFrame;
    public static void DrawRect(GenericDrawingContext g, Iterable<BorderTrapezoid> rectangles) {
        if (!enabled) {
            return;
        }

        g.setColor(GenericColor.white);
        GenericRectangle my;
        my = g.getClipBounds();
        g.setClip(0, 0, 1280, 480);

        for (BorderTrapezoid di : rectangles) {
            g.drawLine(di.x1, di.y1, di.x3, di.y2);
            g.drawLine(di.x3, di.y2, di.x4, di.y2);
            g.drawLine(di.x4, di.y2, di.x2, di.y1);
            g.drawLine(di.x2, di.y1, di.x1, di.y1);
        }

        g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
    }

    public static void DrawRect(GenericDrawingContext g, BorderRect rectangle, int scrollx, int scrolly)
    {
        g.setColor(GenericColor.white);
        g.drawRect(rectangle.lo_point.x + scrollx, rectangle.lo_point.y + scrolly,
                rectangle.ru_point.x - rectangle.lo_point.x,
                rectangle.ru_point.y - rectangle.lo_point.y);
    }

    public static Instant getTimeInstant() {
        return CLOCK.instant();
    }
}  