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

import de.codengine.krabat.Start;
import de.codengine.krabat.anims.Mainanim;
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;

public class Karta extends Mainanim {
    private GenericImage karta;
    private String outputText = "";
    private GenericPoint outputTextPos;
    private int newort = 0;
    private int oldort = 0;
    private int Cursorform = 200;
    private boolean Paintcall = false;

    private static final GenericPoint Plo = new GenericPoint(87, 18);

    private static final int Offset = 25;

    private final GenericPoint Pkulow;
    private final GenericPoint Pnjedz;
    private final GenericPoint Psunow;
    private final GenericPoint Pralbicy;
    private final GenericPoint Pjitk;
    private final GenericPoint Pzdzary;
    private final GenericPoint Pkolmc;

    private final Borderrect kulowRect;
    private final Borderrect njedzRect;
    private final Borderrect sunowRect;
    private final Borderrect ralbicyRect;
    private final Borderrect jitkRect;
    private final Borderrect zdzaryRect;
    private final Borderrect kolmcRect;

    private final Borderrect brGesamt;

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Karta(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        InitImages();

        kulowRect = new Borderrect(231 + Plo.x, 55 + Plo.y, 290 + Plo.x, 106 + Plo.y);
        njedzRect = new Borderrect(109 + Plo.x, 96 + Plo.y, 150 + Plo.x, 124 + Plo.y);
        sunowRect = new Borderrect(213 + Plo.x, 237 + Plo.y, 264 + Plo.x, 274 + Plo.y);
        ralbicyRect = new Borderrect(166 + Plo.x, 315 + Plo.y, 223 + Plo.x, 361 + Plo.y);
        jitkRect = new Borderrect(242 + Plo.x, 348 + Plo.y, 284 + Plo.x, 414 + Plo.y);
        zdzaryRect = new Borderrect(372 + Plo.x, 147 + Plo.y, 397 + Plo.x, 168 + Plo.y);
        kolmcRect = new Borderrect(140 + Plo.x, 35 + Plo.y, 177 + Plo.x, 61 + Plo.y);

        brGesamt = new Borderrect(57 + Plo.x, 0, 436 + Plo.x, 479);

        Pkulow = new GenericPoint(265 + Plo.x, 49 + Plo.y - Offset);
        Pnjedz = new GenericPoint(91 + Plo.x, 90 + Plo.y - Offset);
        Psunow = new GenericPoint(237 + Plo.x, 233 + Plo.y - Offset);
        Pralbicy = new GenericPoint(192 + Plo.x, 316 + Plo.y - Offset);
        Pjitk = new GenericPoint(260 + Plo.x, 342 + Plo.y - Offset);
        Pzdzary = new GenericPoint(352 + Plo.x, 142 + Plo.y - Offset);
        Pkolmc = new GenericPoint(186 + Plo.x, 61 + Plo.y - Offset);

        mainFrame.Freeze(false);
        Paintcall = true;
        evalMouseMoveEvent(mainFrame.Mousepoint);
    }

    // Bilder vorbereiten
    public void InitImages() {
        karta = getPicture("gfx/karta/karta.gif");
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintKarte(GenericDrawingContext g) {

        // Karte - Background zeichnen
        if (!mainFrame.Clipset) {
            g.setClip(0, 0, 1280, 480);
            g.drawImage(karta, mainFrame.scrollx + Plo.x, mainFrame.scrolly + Plo.y, null);
            Cursorform = 200;
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);
        }

        // sonst noch was zu tun ?
        if (outputText != "") {
            // Textausgabe
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, 0xffff0000);
        }
        oldort = newort;
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
        outputText = "";
        mainFrame.repaint();
    }


    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();

        if (e.isLeftClick()) {
            // Hier Entscheidung nach Teilen

            // wenn ausserhalb der Karte, dann weg
            if (!brGesamt.IsPointInRect(pTemp)) {
                Deactivate();
                return;
            }

            // Teil 1
            if (!mainFrame.Actions[305]) {

                int tloc = 0;

                // Hier Index der Orte:
                // 1 = kulow
                // 2 = njedz
                // 3 = sunow
                // 4 = ralbicy
                // 5 = jitk
                // 6 = zdzary
                // 7 = kolmc
                // 8 = doma

                if (kulowRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(21);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(21);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (njedzRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(16);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(16);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (sunowRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(13);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(13);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (ralbicyRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(1);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(1);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (jitkRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(3);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(3);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (zdzaryRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(19);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(19);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                if (kolmcRect.IsPointInRect(pTemp)) {
                    tloc = evalCurrLocation(17);
                    if (tloc != 0) {
                        mainFrame.komme_von_karte = true;
                        mainFrame.currLocation = tloc;
                        mainFrame.Actions[851] = false;
                        mainFrame.ConstructLocation(17);
                    }
                    mainFrame.Clipset = false;
                    Deactivate();
                    mainFrame.repaint();
                }
            } else {
                // Hier moeglicherweise noch dritter Teil rein...
                if (!mainFrame.Actions[499]) {
                    int tloc = 0;

                    if (kulowRect.IsPointInRect(pTemp)) {
                        tloc = evalCurrLocation(76);
                        if (tloc != 0) {
                            mainFrame.currLocation = tloc;
                            mainFrame.Actions[851] = false;
                            mainFrame.ConstructLocation(76);
                        }
                        mainFrame.Clipset = false;
                        Deactivate();
                        mainFrame.repaint();
                        return;
                    }

                    if (sunowRect.IsPointInRect(pTemp)) {
                        tloc = evalCurrLocation(87);
                        if (tloc != 0) {
                            mainFrame.currLocation = tloc;
                            mainFrame.Actions[851] = false;
                            mainFrame.ConstructLocation(87);
                        }
                        mainFrame.Clipset = false;
                        Deactivate();
                        mainFrame.repaint();
                        return;
                    }

                    if (jitkRect.IsPointInRect(pTemp)) {
                        tloc = evalCurrLocation(71);
                        if (tloc != 0) {
                            mainFrame.currLocation = tloc;
                            mainFrame.Actions[851] = false;
                            mainFrame.ConstructLocation(71);
                        }
                        mainFrame.Clipset = false;
                        Deactivate();
                        mainFrame.repaint();
                        return;
                    }

                    if (zdzaryRect.IsPointInRect(pTemp)) {
                        tloc = evalCurrLocation(93);
                        if (tloc != 0) {
                            mainFrame.currLocation = tloc;
                            mainFrame.Actions[851] = false;
                            mainFrame.ConstructLocation(93);
                        }
                        mainFrame.Clipset = false;
                        Deactivate();
                        mainFrame.repaint();
                        return;
                    }

                    if (njedzRect.IsPointInRect(pTemp)) {
                        tloc = evalCurrLocation(85);
                        if (tloc != 0) {
                            mainFrame.currLocation = tloc;
                            mainFrame.Actions[851] = false;
                            mainFrame.ConstructLocation(85);
                        }
                        mainFrame.Clipset = false;
                        Deactivate();
                        mainFrame.repaint();
                    }
                }
			/*else
			  {
			  // Hier 3. Teil...
			  // Locationevaluation erst, wenn alle Locations drin
        
			  if (kutsaRect.IsPointInRect (pTemp) == true)
			  {
			  mainFrame.ConstructLocation (120);
			  mainFrame.Clipset = false;
			  Deactivate();
			  mainFrame.repaint();
			  return;
			  }		
        
			  if (hrodRect.IsPointInRect (pTemp) == true)
			  {
			  mainFrame.ConstructLocation (130);
			  mainFrame.Clipset = false;
			  Deactivate();
			  mainFrame.repaint();
			  return;
			  }		
        
			  if (panorRect.IsPointInRect (pTemp) == true)
			  {
			  mainFrame.ConstructLocation (140);
			  mainFrame.Clipset = false;
			  Deactivate();
			  mainFrame.repaint();
			  return;
			  }		
          
			  if (zastupRect.IsPointInRect (pTemp) == true)
			  {
			  mainFrame.ConstructLocation (150);
			  mainFrame.Clipset = false;
			  Deactivate();
			  mainFrame.repaint();
			  return;
			  }		
			  }	*/
            }
        } else {
            // rechte Maustaste
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

        newort = 0;

        // Hier Aufteilung nach Teil
        if (!mainFrame.Actions[305]) {
            if (kulowRect.IsPointInRect(pTemp)) {
                newort = 1;
            }
            if (njedzRect.IsPointInRect(pTemp)) {
                newort = 2;
            }
            if (sunowRect.IsPointInRect(pTemp)) {
                newort = 3;
            }
            if (ralbicyRect.IsPointInRect(pTemp)) {
                newort = 4;
            }
            if (jitkRect.IsPointInRect(pTemp)) {
                newort = 5;
            }
            if (zdzaryRect.IsPointInRect(pTemp)) {
                newort = 6;
            }
            if (kolmcRect.IsPointInRect(pTemp)) {
                newort = 7;
            }
        } else {
            // Hier zur Not 3. Teil noch drin
            if (!mainFrame.Actions[499]) {
                if (kulowRect.IsPointInRect(pTemp)) {
                    newort = 1;
                }
                if (sunowRect.IsPointInRect(pTemp)) {
                    newort = 3;
                }
                if (jitkRect.IsPointInRect(pTemp)) {
                    newort = 5;
                }
                if (zdzaryRect.IsPointInRect(pTemp)) {
                    newort = 6;
                }
                if (njedzRect.IsPointInRect(pTemp)) {
                    newort = 2;
                }
            }
		/*else
		  {
		  if (kutsaRect.IsPointInRect   (pTemp) == true) newort = 9;
		  if (hrodRect.IsPointInRect    (pTemp) == true) newort = 10;
		  if (panorRect.IsPointInRect   (pTemp) == true) newort = 11;
		  if (zastupRect.IsPointInRect  (pTemp) == true) newort = 12;
		  }	*/
        }
      
	/*if ((newort != 0) && (Cursorform != 1))
	  {
	  Cursorform = 1;
	  mainFrame.setCursor (mainFrame.Kreuz);
	  }
    
	  if ((newort == 0) && (Cursorform != 0))
	  {
	  Cursorform = 0;
	  mainFrame.setCursor (mainFrame.Normal);
	  }	*/

        if (Cursorform != 0) {
            Cursorform = 0;
            mainFrame.setCursor(mainFrame.Normal);
        }

        evalString(newort);

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
            return;
        }

        if (newort != oldort) {
            mainFrame.repaint();
        }
    }

    private void evalString(int newort) {
        if (mainFrame.sprache == 1) {
            switch (newort) {
                case 0:
                    outputText = "";
                    break;

                case 1:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00000");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pkulow.x + mainFrame.scrollx, Pkulow.y + mainFrame.scrolly));
                    break;

                case 2:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00001");
                    outputTextPos = new GenericPoint(Pnjedz.x + mainFrame.scrollx, Pnjedz.y + mainFrame.scrolly);
                    break;

                case 3:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00002");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Psunow.x + mainFrame.scrollx, Psunow.y + mainFrame.scrolly));
                    break;

                case 4:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00003");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pralbicy.x + mainFrame.scrollx, Pralbicy.y + mainFrame.scrolly));
                    break;

                case 5:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00004");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pjitk.x + mainFrame.scrollx, Pjitk.y + mainFrame.scrolly));
                    break;

                case 6:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00005");
                    outputTextPos = new GenericPoint(Pzdzary.x + mainFrame.scrollx, Pzdzary.y + mainFrame.scrolly);
                    break;

                case 7:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00006");
                    outputTextPos = new GenericPoint(Pkolmc.x + mainFrame.scrollx, Pkolmc.y + mainFrame.scrolly);
                    break;

                default:
                    System.out.println("Fehler im Chooser der Strings / Positionen");
                    break;
            }
        }

        if (mainFrame.sprache == 2) {
            switch (newort) {
                case 0:
                    outputText = "";
                    break;

                case 1:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00007");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pkulow.x + mainFrame.scrollx, Pkulow.y + mainFrame.scrolly));
                    break;

                case 2:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00008");
                    outputTextPos = new GenericPoint(Pnjedz.x + mainFrame.scrollx, Pnjedz.y + mainFrame.scrolly);
                    break;

                case 3:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00009");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Psunow.x + mainFrame.scrollx, Psunow.y + mainFrame.scrolly));
                    break;

                case 4:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00010");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pralbicy.x + mainFrame.scrollx, Pralbicy.y + mainFrame.scrolly));
                    break;

                case 5:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00011");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pjitk.x + mainFrame.scrollx, Pjitk.y + mainFrame.scrolly));
                    break;

                case 6:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00012");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pzdzary.x + mainFrame.scrollx, Pzdzary.y + mainFrame.scrolly));
                    break;

                case 7:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00013");
                    outputTextPos = new GenericPoint(Pkolmc.x + mainFrame.scrollx, Pkolmc.y + mainFrame.scrolly);
                    break;

                default:
                    System.out.println("Fehler im Chooser der Strings / Positionen");
                    break;
            }
        }

        if (mainFrame.sprache == 3) {
            switch (newort) {
                case 0:
                    outputText = "";
                    break;

                case 1:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00014");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pkulow.x + mainFrame.scrollx, Pkulow.y + mainFrame.scrolly));
                    break;

                case 2:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00015");
                    outputTextPos = new GenericPoint(Pnjedz.x + mainFrame.scrollx, Pnjedz.y + mainFrame.scrolly);
                    break;

                case 3:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00016");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Psunow.x + mainFrame.scrollx, Psunow.y + mainFrame.scrolly));
                    break;

                case 4:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00017");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pralbicy.x + mainFrame.scrollx, Pralbicy.y + mainFrame.scrolly));
                    break;

                case 5:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00018");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pjitk.x + mainFrame.scrollx, Pjitk.y + mainFrame.scrolly));
                    break;

                case 6:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00019");
                    outputTextPos = mainFrame.ifont.CenterAnimText(outputText, new GenericPoint(Pzdzary.x + mainFrame.scrollx, Pzdzary.y + mainFrame.scrolly));
                    break;

                case 7:
                    outputText = Start.stringManager.getTranslation("Main_Karta_00020");
                    outputTextPos = new GenericPoint(Pkolmc.x + mainFrame.scrollx, Pkolmc.y + mainFrame.scrolly);
                    break;

                default:
                    System.out.println("Fehler im Chooser der Strings / Positionen");
                    break;
            }
        }
    }

    private int evalCurrLocation(int Ziel) {
        int rueck = 0;
        int t = mainFrame.currLocation;

        if (t == Ziel) {
            return rueck;
        }

        switch (Ziel) {
            case 1:
                // Ralbicy
                if ((t > 2) && (t < 7)) {
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
                    if ((t == 4) || (t == 5) || (t == 8) || (t == 19)) {
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
                if (((t > 13) && (t < 19)) || (t == 27) || ((t > 50) && (t < 63))) {
                    rueck = 14;
                } else {
                    if (((t > 9) && (t < 13)) || ((t > 19) && (t < 24))) {
                        rueck = 10;
                    } else {
                        if ((t > 0) && (t < 8) && (t != 4)) {
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
                if ((t == 11) || (t == 18) || ((t > 19) && (t < 23))) {
                    rueck = 18;
                } else {
                    if ((t == 17) || ((t > 50) && (t < 63))) {
                        rueck = 17;
                    } else {
                        rueck = 15;
                    }
                }
                break;

            case 17:
                // Kolmc
                if ((t > 50) && (t < 63)) {
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
                    if (((t > 14) && (t < 19)) || (t == 22) || ((t > 50) && (t < 63))) {
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
                // Rapak
                if (t == 93) {
                    rueck = 93; // bei Zdzary auch dies vorgaukeln
                }
                rueck = 87; // ansonsten Wjes2 vorgaukeln
                break;

            case 85:
                // Villa
                if ((t == 70) || (t == 76) || (t == 79) || (t == 93)) {
                    rueck = 72; // Cyrkej2, Kulow2, Mertens2, Zdzary2 -> Dubring2
                } else {
                    rueck = 80;  // ansonsten -> Njedz2
                }
                break;

            case 87:
                // Wjes
                if ((t == 70) || (t == 76) || (t == 79)) {
                    rueck = 76; // Cyrkej2, Kulow2, Mertens2 -> Kulow2
                } else {
                    if ((t == 83) || (t == 93)) {
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
                System.out.println("Fehler beim Locationaussuchen aufgetreten !");
                break;
        }

        return rueck;
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
        }
    }

    // Deaktivieren //////////
    private void Deactivate() {
        mainFrame.DestructLocation(106);
        newort = 0;
        mainFrame.Clipset = false;
        mainFrame.whatScreen = 0;
        Cursorform = 200;
        mainFrame.repaint();
    }
}