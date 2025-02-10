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

package de.codengine.krabat.locations3;

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Dinglingerwalk;
import de.codengine.krabat.anims.StrazaPoklad;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import de.codengine.krabat.sound.BackgroundMusicPlayer;

public class Poklad extends Mainloc {
    private GenericImage schody, komora, vorderschody, skla;
    private final Dinglingerwalk dinglingerwalk;
    private final StrazaPoklad straza;

    private GenericPoint talkPointStraza;
    private GenericPoint strazaPoint;
    // private borderrect rectStraza;

    private boolean showDingl = false;
    private boolean showStraza = false;

    private boolean ausgewechselt = false;

    private int whatPicture = 0;

    private int welcheAnim = 0;
    private int animRueckgabe = 0;

    private static final GenericPoint strazaFeet = new GenericPoint(177, 287);
    private static final GenericPoint dinglPoint1 = new GenericPoint(241, 279);
    private static final GenericPoint dinglPoint2 = new GenericPoint(700, 550);
    private static final GenericPoint dinglPoint3 = new GenericPoint(370, 440);
    private static final GenericPoint dinglPoint4 = new GenericPoint(574, 418);
    private static final GenericPoint dinglPoint9 = new GenericPoint(173, 279);

    private boolean walkReady = true;

    private int Counter = 0;

    private boolean schalenSound = false;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Poklad(Start caller, int oldLocation) {
        super(caller, 181);

        mainFrame.Freeze(true);

        mainFrame.CheckKrabat();

        BackgroundMusicPlayer.getInstance().playTrack(25, true);

        dinglingerwalk = new Dinglingerwalk(mainFrame);

        dinglingerwalk.maxx = 0;
        dinglingerwalk.zoomf = 1f;
        dinglingerwalk.defScale = 125;

        dinglingerwalk.SetDinglingerPos(dinglPoint1);
        dinglingerwalk.SetFacing(9);

        straza = new StrazaPoklad(mainFrame);

        InitLocation(oldLocation);

        whatPicture = 1;
        nextActionID = 10;
        showDingl = true;
        showStraza = true;

        evalPersonPoints(strazaFeet);

        mainFrame.Freeze(false);
    }

    // Gegend intialisieren (Grenzen u.s.w.)
    private void InitLocation(int oldLocation) {
        InitImages();
    }

    // Bilder vorbereiten
    private void InitImages() {
        schody = getPicture("gfx-dd/trepj/trepj.gif");
        komora = getPicture("gfx-dd/poklad/poklad.gif");
        skla = getPicture("gfx-dd/poklad/pskla.gif");

        vorderschody = getPicture("gfx-dd/trepj/trepj-vorn.gif");

    }

    private void evalPersonPoints(GenericPoint strazap) {
        strazaPoint = new GenericPoint();
        strazaPoint.x = strazap.x - StrazaPoklad.Breite / 2;
        strazaPoint.y = strazap.y - StrazaPoklad.Hoehe;

        talkPointStraza = new GenericPoint();
        talkPointStraza.x = strazap.x;
        talkPointStraza.y = strazaPoint.y - 50;

        // rectStraza = new borderrect (strazaPoint.x, strazaPoint.y, strazaPoint.x + StrazaPoklad.Breite, strazaPoint.y + StrazaPoklad.Hoehe);
    }

    // Paint-Routine dieser Location //////////////////////////////////////////

    @Override
    public void paintLocation(GenericDrawingContext g) {

        // Clipping -Region initialisieren
        if (!mainFrame.Clipset) {
            mainFrame.scrollx = 0;
            mainFrame.scrolly = 0;
            Cursorform = 200;
            mainFrame.fPlayAnim = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);
            mainFrame.Clipset = true;
            g.setClip(0, 0, 644, 484);
            mainFrame.isAnim = true;
        }

        // Hintergrund und Krabat zeichnen
        if (whatPicture == 1) {
            g.drawImage(schody, 0, 0, null);
        }
        if (whatPicture == 2) {
            g.drawImage(komora, 0, 0, null);
        }

        // Debugging - Zeichnen der Laufrechtecke
        // mainFrame.showrect.Zeichne(g, mainFrame.wegGeher.vBorders);

        // straza und Dinglinger Hintergrund loeschen
        if (showStraza) {
            g.setClip(strazaPoint.x, strazaPoint.y, StrazaPoklad.Breite, StrazaPoklad.Hoehe);
            if (whatPicture == 1) {
                g.drawImage(schody, 0, 0, null);
            }
            if (whatPicture == 2) {
                g.drawImage(komora, 0, 0, null);
            }
        }

        if (showDingl) {
            // Hintergrund loeschen
            Borderrect temp = dinglingerwalk.DinglingerRect();
            g.setClip(temp.lo_point.x, temp.lo_point.y,
                    temp.ru_point.x - temp.lo_point.x, temp.ru_point.y - temp.lo_point.y);
            if (whatPicture == 1) {
                g.drawImage(schody, 0, 0, null);
            }
            if (whatPicture == 2) {
                g.drawImage(komora, 0, 0, null);
            }
        }

        // Skla auf komora zeichnen, wenn noetig
        if (whatPicture == 2 && ausgewechselt) {
            g.setClip(577, 269, 24, 15);
            g.drawImage(skla, 577, 269, null);
        }

        // Dinglinger zeichnen
        if (showDingl) {
            // Hintergrund loeschen
            Borderrect temp = dinglingerwalk.DinglingerRect();
            g.setClip(temp.lo_point.x, temp.lo_point.y,
                    temp.ru_point.x - temp.lo_point.x, temp.ru_point.y - temp.lo_point.y);

            // Dinglinger weiterbewegen, wenn noetig
            if (!walkReady) {
                walkReady = dinglingerwalk.Move();
            }

            // Dinglinger zeichnen
            if (TalkPerson == 47 && mainFrame.talkCount > 1) {
                dinglingerwalk.talkDinglinger(g, welcheAnim);
            } else {
                // Sonderanims oder normal zeichnen
                if (welcheAnim == 3 || welcheAnim == 2) {
                    animRueckgabe = dinglingerwalk.DoAnimation(g, welcheAnim);
                } else {
                    dinglingerwalk.drawDinglinger(g, welcheAnim);
                }
            }

            // Vordergrund zeichnen
            if (whatPicture == 1) {
                g.drawImage(vorderschody, 147, 0, null);
            }
        }

        // Straza zeichnen
        if (showStraza) {
            g.setClip(strazaPoint.x, strazaPoint.y, StrazaPoklad.Breite, StrazaPoklad.Hoehe);
            straza.drawStraza(g, TalkPerson, strazaPoint, false);
            g.drawImage(vorderschody, 147, 0, null);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            GenericRectangle my;
            my = g.getClipBounds();
            g.setClip(0, 0, 644, 484);
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, FarbenArray[TalkPerson]);
            g.setClip(my.getX(), my.getY(), my.getWidth(), my.getHeight());
        }

        // Redeschleife herunterzaehlen und Neuzeichnen ermoeglichen
        if (mainFrame.talkCount > 0) {
            --mainFrame.talkCount;
            if (mainFrame.talkCount <= 1) {
                mainFrame.Clipset = false;
                outputText = "";
                TalkPerson = 0;
            }
        }

        if (TalkPause > 0 && mainFrame.talkCount < 1) {
            TalkPause--;
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0 && TalkPause < 1 && mainFrame.talkCount < 1) {
            DoAction();
        }
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    @Override
    public void evalMouseEvent(GenericMouseEvent e) {
        if (mainFrame.talkCount != 0) {
            mainFrame.Clipset = false;
        }
        if (mainFrame.talkCount > 1) {
            mainFrame.talkCount = 1;
            TalkPerson = 0;
        }
        outputText = "";

    }

    // befindet sich Cursor ueber Gegenstand, dann Kreuz-Cursor
    @Override
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Wenn Animation oder Krabat - Animation, dann transparenter Cursor
        if (Cursorform != 20) {
            Cursorform = 20;
            mainFrame.setCursor(mainFrame.Nix);
        }
    }

    @Override
    public void evalMouseExitEvent(GenericMouseEvent e) {
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    @Override
    public void evalKeyEvent(GenericKeyEvent e) {
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // Was soll Krabat machen ?
        switch (nextActionID) {

            case 10:
                // Erzaehler spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00000"), Start.stringManager.getTranslation("Loc3_Poklad_00001"), Start.stringManager.getTranslation("Loc3_Poklad_00002"), 0, 54, 2, 20, new GenericPoint(320, 200));
                break;

            case 20:
                // Straza spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00003"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00004"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00005"),
                        0, 46, 2, 30, talkPointStraza);
                break;

            case 30:
                // Dingl spricht
                welcheAnim = 1;
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00006"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00007"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00008"),
                        0, 47, 2, 40, dinglingerwalk.evalTalkPoint());
                break;

            case 40:
                // Straza spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00009"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00010"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00011"),
                        0, 46, 2, 45, talkPointStraza);
                break;

            case 45:
                // Dinglinger vorlaufen lassen
                welcheAnim = 0;
                dinglingerwalk.MoveTo(dinglPoint9);
                walkReady = false;
                nextActionID = 50;
                break;

            case 50:
                // Bild umschalten und Personen weg
                if (!walkReady) {
                    break;
                }
                mainFrame.Clipset = false;
                dinglingerwalk.defScale = 0;
                dinglingerwalk.SetDinglingerPos(dinglPoint2);
                dinglingerwalk.SetFacing(9);
                showStraza = false;
                whatPicture = 2;
                nextActionID = 55;
                break;

            case 55:
                // Aufschliessound bringen und bisschen warten
                Counter = 50;
                mainFrame.wave.PlayFile("sfx-dd/kluc.wav");
                nextActionID = 60;
                break;

            case 60:
                // Dingl spricht im Verborgenen
                if (--Counter > 1) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00012"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00013"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00014"),
                        0, 47, 2, 70, new GenericPoint(640, 200));
                break;

            case 70:
                // Dinglinger erscheinen lassen und hinstellen
                evalPersonPoints(strazaFeet);
                dinglingerwalk.MoveTo(dinglPoint3);
                walkReady = false;
                nextActionID = 80;
                break;

            case 80:
                // Dingl spricht
                if (!walkReady) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00015"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00016"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00017"),
                        0, 47, 2, 81, dinglingerwalk.evalTalkPoint());
                break;

            case 81:
                // umschauen
                welcheAnim = 2;
                nextActionID = 83;
                break;

            case 83:
                // laufe zur Schuessel
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 0;
                dinglingerwalk.MoveTo(dinglPoint4);
                walkReady = false;
                nextActionID = 85;
                break;

            case 85:
                // Dinglinger spricht
                if (!walkReady) {
                    break;
                }
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00018"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00019"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00020"),
                        0, 47, 2, 90, dinglingerwalk.evalTalkPoint());
                break;

            case 90:
                // Dingl spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00021"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00022"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00023"),
                        0, 47, 2, 95, dinglingerwalk.evalTalkPoint());
                break;

            case 95:
                // Schuessel austauschen
                welcheAnim = 3;
                nextActionID = 100;
                break;

            case 100:
                // Dinglinger laeuft raus
                if (animRueckgabe == 6) {
                    ausgewechselt = true;
                    if (!schalenSound) {
                        schalenSound = true;
                        mainFrame.wave.PlayFile("sfx/becher.wav");
                    }
                }
                if (animRueckgabe != 100) {
                    break;
                }
                welcheAnim = 0;
                dinglingerwalk.MoveTo(dinglPoint2);
                walkReady = false;
                nextActionID = 105;
                break;

            case 105:
                // Auf 1. Bild zurueckschalten und Personen zeigen
                if (!walkReady) {
                    break;
                }
                mainFrame.Clipset = false;
                showStraza = true;
                whatPicture = 1;
                evalPersonPoints(strazaFeet);
                dinglingerwalk.defScale = 125;
                dinglingerwalk.SetDinglingerPos(dinglPoint9);
                dinglingerwalk.SetFacing(3);
                dinglingerwalk.MoveTo(dinglPoint1);
                walkReady = false;
                nextActionID = 110;
                break;

            case 110:
                // Straza spricht
                if (!walkReady) {
                    break;
                }
                dinglingerwalk.SetFacing(9);
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00024"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00025"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00026"),
                        0, 46, 2, 120, talkPointStraza);
                break;

            case 120:
                // Dingl spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00027"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00028"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00029"),
                        0, 47, 2, 130, dinglingerwalk.evalTalkPoint());
                break;

            case 130:
                // Straza spricht
                PersonSagt(Start.stringManager.getTranslation("Loc3_Poklad_00030"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00031"),
                        Start.stringManager.getTranslation("Loc3_Poklad_00032"),
                        0, 46, 2, 140, talkPointStraza);
                break;

            case 140:
                // Gehe zu Dinglinger (Anim)
                NeuesBild(141, locationID);
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

    }
}