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

import de.codengine.krabat.ScreenType;
import de.codengine.krabat.Start;
import de.codengine.krabat.anims.MainAnim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Map extends MainAnim {
    private static final Logger log = LoggerFactory.getLogger(Map.class);
    private GenericImage map;
    private String outputText = "";
    private GenericPoint outputTextPos;
    private int newLocation = 0;
    private int oldLocation = 0;
    private int cursorShape = 200;
    private boolean paintCall = false;

    private static final GenericPoint POINT_LOCATION = new GenericPoint(87, 18);

    private static final int OFFSET = 25;

    private final GenericPoint kulowPoint;
    private final GenericPoint njedzPoint;
    private final GenericPoint sunowPoint;
    private final GenericPoint ralbicyPoint;
    private final GenericPoint jitkPoint;
    private final GenericPoint zdzaryPoint;
    private final GenericPoint kolmcPoint;

    private final BorderRect kulowRect;
    private final BorderRect njedzRect;
    private final BorderRect sunowRect;
    private final BorderRect ralbicyRect;
    private final BorderRect jitkRect;
    private final BorderRect zdzaryRect;
    private final BorderRect kolmcRect;

    private final BorderRect brTotal;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Map(Start caller) {
        super(caller);
        mainFrame.freeze(true);

        initImages();

        kulowRect = new BorderRect(231 + POINT_LOCATION.x, 55 + POINT_LOCATION.y, 290 + POINT_LOCATION.x, 106 + POINT_LOCATION.y);
        njedzRect = new BorderRect(109 + POINT_LOCATION.x, 96 + POINT_LOCATION.y, 150 + POINT_LOCATION.x, 124 + POINT_LOCATION.y);
        sunowRect = new BorderRect(213 + POINT_LOCATION.x, 237 + POINT_LOCATION.y, 264 + POINT_LOCATION.x, 274 + POINT_LOCATION.y);
        ralbicyRect = new BorderRect(166 + POINT_LOCATION.x, 315 + POINT_LOCATION.y, 223 + POINT_LOCATION.x, 361 + POINT_LOCATION.y);
        jitkRect = new BorderRect(242 + POINT_LOCATION.x, 348 + POINT_LOCATION.y, 284 + POINT_LOCATION.x, 414 + POINT_LOCATION.y);
        zdzaryRect = new BorderRect(372 + POINT_LOCATION.x, 147 + POINT_LOCATION.y, 397 + POINT_LOCATION.x, 168 + POINT_LOCATION.y);
        kolmcRect = new BorderRect(140 + POINT_LOCATION.x, 35 + POINT_LOCATION.y, 177 + POINT_LOCATION.x, 61 + POINT_LOCATION.y);

        brTotal = new BorderRect(57 + POINT_LOCATION.x, 0, 436 + POINT_LOCATION.x, 479);

        kulowPoint = new GenericPoint(265 + POINT_LOCATION.x, 49 + POINT_LOCATION.y - OFFSET);
        njedzPoint = new GenericPoint(91 + POINT_LOCATION.x, 90 + POINT_LOCATION.y - OFFSET);
        sunowPoint = new GenericPoint(237 + POINT_LOCATION.x, 233 + POINT_LOCATION.y - OFFSET);
        ralbicyPoint = new GenericPoint(192 + POINT_LOCATION.x, 316 + POINT_LOCATION.y - OFFSET);
        jitkPoint = new GenericPoint(260 + POINT_LOCATION.x, 342 + POINT_LOCATION.y - OFFSET);
        zdzaryPoint = new GenericPoint(352 + POINT_LOCATION.x, 142 + POINT_LOCATION.y - OFFSET);
        kolmcPoint = new GenericPoint(186 + POINT_LOCATION.x, 61 + POINT_LOCATION.y - OFFSET);

        mainFrame.freeze(false);
        paintCall = true;
        evalMouseMoveEvent(mainFrame.mousePoint);
    }

    // Bilder vorbereiten
    public void initImages() {
        map = getPicture("gfx/karta/karta.png");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintMap(GenericDrawingContext g) {

        // Karte - Background zeichnen
        if (!mainFrame.isClipSet) {
            g.setClip(0, 0, 1280, 480);
            g.drawImage(map, mainFrame.scrollX + POINT_LOCATION.x, mainFrame.scrollY + POINT_LOCATION.y);
            cursorShape = 200;
            paintCall = true;
            evalMouseMoveEvent(mainFrame.mousePoint);
        }

        // sonst noch was zu tun ?
        if (!Objects.equals(outputText, "")) {
            // Textausgabe
            mainFrame.imageFont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, 0xffff0000);
        }
        oldLocation = newLocation;
    }

    public void evalMouseExitEvent() {
        outputText = "";
        mainFrame.repaint();
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        if (!e.isLeftClick()) {
            return;
        }

        GenericPoint pTemp = e.getPoint();

        // Hier Entscheidung nach Teilen

        // wenn ausserhalb der Karte, dann weg
        if (!brTotal.isPointInRect(pTemp)) {
            deactivate();
            return;
        }

        // Teil 1
        if (!mainFrame.actions[305]) {

            int tloc;

            // Hier Index der Orte:
            // 1 = kulow
            // 2 = njedz
            // 3 = sunow
            // 4 = ralbicy
            // 5 = jitk
            // 6 = zdzary
            // 7 = kolmc
            // 8 = doma

            if (kulowRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(21);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(21);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
                return;
            }

            if (njedzRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(16);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(16);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
                return;
            }

            if (sunowRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(13);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(13);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
                return;
            }

            if (ralbicyRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(1);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(1);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
                return;
            }

            if (jitkRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(3);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(3);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
                return;
            }

            if (zdzaryRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(19);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(19);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
                return;
            }

            if (kolmcRect.isPointInRect(pTemp)) {
                tloc = evalCurrLocation(17);
                if (tloc != 0) {
                    mainFrame.enteringFromMap = true;
                    mainFrame.currentLocationIdx = tloc;
                    mainFrame.actions[851] = false;
                    mainFrame.constructLocation(17);
                }
                mainFrame.isClipSet = false;
                deactivate();
                mainFrame.repaint();
            }
        } else {
            // Hier moeglicherweise noch dritter Teil rein...
            if (!mainFrame.actions[499]) {
                int tloc;

                if (kulowRect.isPointInRect(pTemp)) {
                    tloc = evalCurrLocation(76);
                    if (tloc != 0) {
                        mainFrame.currentLocationIdx = tloc;
                        mainFrame.actions[851] = false;
                        mainFrame.constructLocation(76);
                    }
                    mainFrame.isClipSet = false;
                    deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (sunowRect.isPointInRect(pTemp)) {
                    tloc = evalCurrLocation(87);
                    if (tloc != 0) {
                        mainFrame.currentLocationIdx = tloc;
                        mainFrame.actions[851] = false;
                        mainFrame.constructLocation(87);
                    }
                    mainFrame.isClipSet = false;
                    deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (jitkRect.isPointInRect(pTemp)) {
                    tloc = evalCurrLocation(71);
                    if (tloc != 0) {
                        mainFrame.currentLocationIdx = tloc;
                        mainFrame.actions[851] = false;
                        mainFrame.constructLocation(71);
                    }
                    mainFrame.isClipSet = false;
                    deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (zdzaryRect.isPointInRect(pTemp)) {
                    tloc = evalCurrLocation(93);
                    if (tloc != 0) {
                        mainFrame.currentLocationIdx = tloc;
                        mainFrame.actions[851] = false;
                        mainFrame.constructLocation(93);
                    }
                    mainFrame.isClipSet = false;
                    deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (njedzRect.isPointInRect(pTemp)) {
                    tloc = evalCurrLocation(85);
                    if (tloc != 0) {
                        mainFrame.currentLocationIdx = tloc;
                        mainFrame.actions[851] = false;
                        mainFrame.constructLocation(85);
                    }
                    mainFrame.isClipSet = false;
                    deactivate();
                    mainFrame.repaint();
                }
            }
        }
    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Hier Index der Orte:
        // 1 = kulow
        // 2 = njedz
        // 3 = sunow
        // 4 = ralbicy
        // 5 = jitk
        // 6 = zdzary
        // 7 = kolmc
        // 8 = doma

        newLocation = 0;

        // Hier Aufteilung nach Teil
        if (!mainFrame.actions[305]) {
            if (kulowRect.isPointInRect(pTemp)) {
                newLocation = 1;
            }
            if (njedzRect.isPointInRect(pTemp)) {
                newLocation = 2;
            }
            if (sunowRect.isPointInRect(pTemp)) {
                newLocation = 3;
            }
            if (ralbicyRect.isPointInRect(pTemp)) {
                newLocation = 4;
            }
            if (jitkRect.isPointInRect(pTemp)) {
                newLocation = 5;
            }
            if (zdzaryRect.isPointInRect(pTemp)) {
                newLocation = 6;
            }
            if (kolmcRect.isPointInRect(pTemp)) {
                newLocation = 7;
            }
        } else {
            // Hier zur Not 3. Teil noch drin
            if (!mainFrame.actions[499]) {
                if (kulowRect.isPointInRect(pTemp)) {
                    newLocation = 1;
                }
                if (sunowRect.isPointInRect(pTemp)) {
                    newLocation = 3;
                }
                if (jitkRect.isPointInRect(pTemp)) {
                    newLocation = 5;
                }
                if (zdzaryRect.isPointInRect(pTemp)) {
                    newLocation = 6;
                }
                if (njedzRect.isPointInRect(pTemp)) {
                    newLocation = 2;
                }
            }
        }

        if (cursorShape != 0) {
            cursorShape = 0;
            mainFrame.setCursor(mainFrame.cursorNormal);
        }

        evalString(newLocation);

        // wenn noetig , dann Neuzeichnen!
        if (paintCall) {
            paintCall = false;
            return;
        }

        if (newLocation != oldLocation) {
            mainFrame.repaint();
        }
    }

    private void evalString(int newort) {
        switch (newort) {
            case 0:
                outputText = "";
                break;

            case 1:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_1");
                outputTextPos = mainFrame.imageFont.centerAnimText(outputText, new GenericPoint(kulowPoint.x + mainFrame.scrollX, kulowPoint.y + mainFrame.scrollY));
                break;

            case 2:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_2");
                outputTextPos = new GenericPoint(njedzPoint.x + mainFrame.scrollX, njedzPoint.y + mainFrame.scrollY);
                break;

            case 3:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_3");
                outputTextPos = mainFrame.imageFont.centerAnimText(outputText, new GenericPoint(sunowPoint.x + mainFrame.scrollX, sunowPoint.y + mainFrame.scrollY));
                break;

            case 4:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_4");
                outputTextPos = mainFrame.imageFont.centerAnimText(outputText, new GenericPoint(ralbicyPoint.x + mainFrame.scrollX, ralbicyPoint.y + mainFrame.scrollY));
                break;

            case 5:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_5");
                outputTextPos = mainFrame.imageFont.centerAnimText(outputText, new GenericPoint(jitkPoint.x + mainFrame.scrollX, jitkPoint.y + mainFrame.scrollY));
                break;

            case 6:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_6");
                outputTextPos = new GenericPoint(zdzaryPoint.x + mainFrame.scrollX, zdzaryPoint.y + mainFrame.scrollY);
                break;

            case 7:
                outputText = Start.STRING_MANAGER.getTranslation("Karta_7");
                outputTextPos = new GenericPoint(kolmcPoint.x + mainFrame.scrollX, kolmcPoint.y + mainFrame.scrollY);
                break;

            default:
                log.error("Fehler im Chooser der Strings / Positionen. newort = {}", newort);
                break;
        }
    }

    private int evalCurrLocation(int target) {
        int rueck = 0;
        int t = mainFrame.currentLocationIdx;

        if (t == target) {
            return rueck;
        }

        switch (target) {
            case 1:
                // Ralbicy
                if (t > 2 && t < 7) {
                    rueck = 3;
                } else {
                    rueck = 2;
                }
                break;

            case 3:
                // Jitk
                if (t == 6) {
                    rueck = 6;
                } else {
                    if (t == 4 || t == 5 || t == 8 || t == 19) {
                        rueck = 5;
                    } else {
                        rueck = 1;
                    }
                }
                break;

            case 6:
                // Doma
                rueck = 3;
                break;

            case 13:
                // Wjes
                if (t > 13 && t < 19 || t == 27 || t > 50 && t < 63) {
                    rueck = 14;
                } else {
                    if (t > 9 && t < 13 || t > 19 && t < 24) {
                        rueck = 10;
                    } else {
                        if (t > 0 && t < 8 && t != 4) {
                            rueck = 7;
                        } else {
                            if (t == 24) {
                                rueck = 24;
                            } else {
                                rueck = 9;
                            }
                        }
                    }
                }
                break;

            case 16:
                // Villa
                if (t == 11 || t == 18 || t > 19 && t < 23) {
                    rueck = 18;
                } else {
                    if (t == 17 || t > 50 && t < 63) {
                        rueck = 17;
                    } else {
                        rueck = 15;
                    }
                }
                break;

            case 17:
                // Kolmc
                if (t > 50 && t < 63) {
                    rueck = 62;
                } else {
                    rueck = 16;
                }
                break;

            case 19:
                // Zdzary
                rueck = 8;
                break;

            case 21:
                // Kulow
                if (t == 20) {
                    rueck = 20;
                } else {
                    if (t > 14 && t < 19 || t == 22 || t > 50 && t < 63) {
                        rueck = 22;
                    } else {
                        rueck = 11;
                    }
                }
                break;

            // Teil 2 evaluieren

            case 70:
                // Cyrkej
                if (t == 85) {
                    rueck = 72; // von Wila2 aus Dubring2 vorgaukeln
                } else {
                    rueck = 76;         // sonst Kulow vorgaukeln
                }
                break;

            case 71:
                // Doma
                rueck = 87;  // immer Wjes2 vorgaukeln
                break;

            case 76:
                // Kulow
                if (t == 70) {
                    rueck = 70;  // aus Cyrkej kommend das auch vorgaukeln
                } else {
                    if (t == 79) {
                        rueck = 79; // aus Mertens kommend dies vorgaukeln
                    } else {
                        rueck = 87; // sonst immer Wjes2 vorgaukeln
                    }
                }
                break;

            case 79:
                // Mertens
                rueck = 76;  // immer Kulow2 vorgaukeln
                break;

            case 83:
                rueck = 87; // ansonsten Wjes2 vorgaukeln
                break;

            case 85:
                // Villa
                if (t == 70 || t == 76 || t == 79 || t == 93) {
                    rueck = 72; // Cyrkej2, Kulow2, Mertens2, Zdzary2 -> Dubring2
                } else {
                    rueck = 80;  // ansonsten -> Njedz2
                }
                break;

            case 87:
                // Wjes
                if (t == 70 || t == 76 || t == 79) {
                    rueck = 76; // Cyrkej2, Kulow2, Mertens2 -> Kulow2
                } else {
                    if (t == 83 || t == 93) {
                        rueck = 83;  // Rapak2, Zdzary2 -> Rapak2
                    } else {
                        if (t == 85) {
                            rueck = 73; // Wila2 -> Hojnt2
                        } else {
                            rueck = 71;  // Rest -> Doma2
                        }
                    }
                }
                break;

            case 93:
                // Zdzary
                rueck = 83;  // immer -> Rapak2
                break;

            default:
                log.error("Fehler beim Locationaussuchen aufgetreten! Ziel = {}", target);
                break;
        }

        return rueck;
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();
        if (key == GenericKeyEvent.VK_ESCAPE) {
            deactivate();
        }
    }

    // Deaktivieren //////////
    private void deactivate() {
        mainFrame.destructLocation(106);
        newLocation = 0;
        mainFrame.isClipSet = false;
        mainFrame.whatScreen = ScreenType.NONE;
        cursorShape = 200;
        mainFrame.repaint();
    }
}