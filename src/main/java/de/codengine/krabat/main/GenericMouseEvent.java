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


// ok

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

public class GenericMouseEvent {
    private final int button;
    private final int modifiersEx;

    private final GenericPoint point;

    private final boolean isDoubleClick;

    public GenericMouseEvent(int button, int modifiersEx, GenericPoint point, boolean isDoubleClick) {
        this.button = button;
        this.modifiersEx = modifiersEx;
        this.point = point;
        this.isDoubleClick = isDoubleClick;
    }

    public GenericPoint getPoint() {
        return point;
    }

    public boolean getDoubleClick() {
        return isDoubleClick;
    }

    public boolean isLeftClick() {
        return button == MouseEvent.BUTTON1 && (modifiersEx & InputEvent.BUTTON1_DOWN_MASK) != 0;
    }

    public boolean isRightClick() {
        return button == MouseEvent.BUTTON3 && (modifiersEx & InputEvent.BUTTON3_DOWN_MASK) != 0;
    }
}
