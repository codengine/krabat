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

package de.codengine.main;

import de.codengine.Start;
import de.codengine.anims.Mainanim;
import de.codengine.platform.GenericDrawingContext;
import de.codengine.platform.GenericImage;
import de.codengine.platform.GenericImageObserver;

public class Slownik extends Mainanim {
    private GenericImage background, pfeiloben, pfeilunten, dpfeiloben, dpfeilunten;

    private static final GenericPoint pPfeilOben = new GenericPoint(210, 80);  // allgemeine Pfeildeklarationen
    private static final GenericPoint pPfeilUnten = new GenericPoint(210, 277);

    private static final int BREITE = 35; // dto.
    private static final int HOEHE = 53;

    private final Borderrect brPfeilOben;
    private final Borderrect brPfeilUnten;

    private int Cursorform = 200;

    // private String outputText = "";
    // private GenericPoint outputTextPos;

    private final String[][] Eintrag;

    private int Index = 0;

    // private static final int OFFSET = 256;

    /*private static final String[] Auswahl = {"b", "c", "d", "e", "f", "g", "h", "j", "k", "l","#l", "m", "n",
      "o", "p", "r", "s","#s", "t", "w", "z","#d","#z"};*/
    private static final String[] DAuswahl = {"B", "C", "D", "E", "F", "G", "H", "Ch", "J", "K", "#L", "L", "M", "N",
            "O", "Pa", "Po", "P#y", "R", "S", "#S", "T", "Wa", "Wo", "Wu", "Z", "#Z", "#D"};
    private static final int[] DSprung = {0, 25, 37, 80, 81, 83, 114, 118, 122, 126, 180, 188, 197, 218, 260,
            262, 284, 324, 360, 378, 408, 419, 430, 448, 491, 530, 576, 586};

    private static final String[] HAuswahl = {"B", "#K", "D", "D#d", "H", "Ch", "J", "K", "L", "M", "N", "O",
            "Pa", "Po", "P#r", "R", "S", "#S", "T", "#C", "W#e", "Wo", "Wu", "Z", "#Z"};
    private static final int[] HSprung = {0, 6, 11, 21, 23, 30, 33, 35, 52, 56, 61, 79, 80, 89, 106,
            117, 126, 149, 155, 162, 163, 173, 194, 207, 226};

    private int Skip = -1;
    private static final GenericPoint LinksOben = new GenericPoint(30, 400);  // fuer Beginn Auswahlleiste
    private static final int XDIFF = 40;    // allegemeine festlegungen fuer Auswahlleiste
    private static final int YDIFF = 30;
    private static final int DXANZA = 14;
    private static final int HXANZA = 13;
    private static final int YANZA = 2;

    private final Borderrect brGesamt;   // Gesamtrect fuer Auswahlleiste

    private int menuitem = 0;
    private int olditem = 0;

    private int Nummer = 0;

    private boolean Paintcall = false;

    private static final int X_SORB = 30;  // allgemeine Deklarationen fuer Woerterbuchanfang
    private static final int X_DEUT = 260;
    private static final int Y_SORB = 80;
    private static final int Y_DEUT = 80;

    // Initialisierung ////////////////////////////////////////////////////////

    private final GenericImageObserver observer = null;

    // Instanz von dieser Location erzeugen
    public Slownik(Start caller) {
        super(caller);
        mainFrame.Freeze(true);

        mainFrame.NoPaint(true);

        Eintrag = new String[600][2];

        brPfeilOben = new Borderrect(pPfeilOben.x, pPfeilOben.y, pPfeilOben.x + BREITE, pPfeilOben.y + HOEHE);
        brPfeilUnten = new Borderrect(pPfeilUnten.x, pPfeilUnten.y, pPfeilUnten.x + BREITE, pPfeilUnten.y + HOEHE);

        brGesamt = new Borderrect(LinksOben.x, LinksOben.y, LinksOben.x + (((mainFrame.sprache == 2) ? DXANZA : HXANZA) * XDIFF) - 1, LinksOben.y + (YANZA * YDIFF) - 1);

        InitImages();

        LoadSlownik();

        mainFrame.Freeze(false);
    }

    // Bilder vorbereiten
    private void InitImages() {
        background = getPicture("gfx/mainmenu/background2.gif");
        pfeiloben = getPicture("gfx/mainmenu/pfeil-hoch.gif");
        dpfeiloben = getPicture("gfx/mainmenu/pfeil-hoch-leer.gif");
        pfeilunten = getPicture("gfx/mainmenu/pfeil-runter.gif");
        dpfeilunten = getPicture("gfx/mainmenu/pfeil-runter-leer.gif");
    }

    // Woerterbuch laden
    private void LoadSlownik() {
        String File = "";

        if (mainFrame.sprache == 2) {
            File = "slowds.kra";
        } else {
            File = "slowhs.kra";
        }

        byte[] Feld = new byte[]{};

        if (mainFrame.storageManager.isSlownikSupported()) {
            Feld = mainFrame.storageManager.loadSlownik(File);
        }

        int Pos = Feld.length;


        // auf Strings konvertieren und Eintraege generieren
        int i = 0;
        Nummer = 0;

        if (Pos == 0) {
            return;
        }

        do {
            // solange String reinkopieren, bis $07 erreicht ( = Trennzeichen Sorbisch -> Deutsch)
            Eintrag[Nummer][0] = "";
            for (int j = i; Feld[j] != 13; j++) {
                // System.out.print (Feld[j] + " ");
                Eintrag[Nummer][0] += (char) Feld[j];
                i++;
            }

            i += 2;

            Eintrag[Nummer][1] = "";
            for (int j = i; Feld[j] != 13; j++) {
                // System.out.print (Feld[j] + " ");
                Eintrag[Nummer][1] += (char) Feld[j];
                i++;
            }

            i += 2;
            Nummer++;

            // System.out.println (Nummer);
        }
        while (i < Pos);
    
	/*for (int f = 0; f < Nummer; f++)
	  {
	  System.out.println (Eintrag[f][0] + " " + Eintrag[f][1]);
	  }	*/

        // alle Eintraege sortieren
        // Sort (Nummer);
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintSlownik(GenericDrawingContext g) {

        // Credits-Background zeichnen
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;
            g.setClip(0, 0, 1280, 480);
            Cursorform = 200;
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);

            // alles loeschen und neuzeichnen - hier die texte, die sich nur bei "Clipset = false" aendern (Mouseclick)
            g.drawImage(background, mainFrame.scrollx, 0, null);
            GenericPoint ps = mainFrame.ifont.CenterAnimText("S#lownik", new GenericPoint(320, 35));
            mainFrame.ifont.drawString(g, "S#lownik", ps.x, ps.y, 0xffff0000);

            for (int i = Index; i < (Index + 10); i++) {
                // System.out.println (Eintrag[i][0]);
                mainFrame.ifont.drawString(g, Eintrag[i][0], X_SORB + mainFrame.scrollx, mainFrame.scrolly + Y_SORB + (i - Index) * 25, 0xffff0000);
                mainFrame.ifont.drawString(g, Eintrag[i][1], X_DEUT + mainFrame.scrollx, mainFrame.scrolly + Y_DEUT + (i - Index) * 25, 0xffff0000);
            }

            // Pfeile dazu-sind ja sonst geloescht !
            g.drawImage(dpfeiloben, pPfeilOben.x + mainFrame.scrollx, pPfeilOben.y + mainFrame.scrolly, observer);
            g.drawImage(dpfeilunten, pPfeilUnten.x + mainFrame.scrollx, pPfeilUnten.y + mainFrame.scrolly, observer);
        }

        // Hier Unterscheidung HS-DS
        int TempXANZA = (mainFrame.sprache == 2) ? DXANZA : HXANZA;

        // Auswahl anzeigen bzw. highlighten (wenn ihr dabeiseid, dann gibts 'n Highlight)
        for (int i = 0; i < TempXANZA; i++) {
            GenericPoint px = mainFrame.ifont.CenterAnimText((mainFrame.sprache == 2) ? DAuswahl[i] : HAuswahl[i], new GenericPoint((mainFrame.scrollx + LinksOben.x + (i * XDIFF) + (XDIFF / 2)), (mainFrame.scrolly + LinksOben.y)));
            mainFrame.ifont.drawString(g, (mainFrame.sprache == 2) ? DAuswahl[i] : HAuswahl[i], px.x, px.y, (i == Skip) ? 0xffff0000 : 0xff800000);
        }

        for (int i = TempXANZA; i < ((TempXANZA * 2) - ((mainFrame.sprache == 2) ? 0 : 1)); i++)  // HS hat ungerade Anzahl
        {
            GenericPoint py = mainFrame.ifont.CenterAnimText((mainFrame.sprache == 2) ? DAuswahl[i] : HAuswahl[i], new GenericPoint((mainFrame.scrollx + LinksOben.x + ((i - TempXANZA) * XDIFF) + (XDIFF / 2)), (mainFrame.scrolly + LinksOben.y + YDIFF)));
            mainFrame.ifont.drawString(g, (mainFrame.sprache == 2) ? DAuswahl[i] : HAuswahl[i], py.x, py.y, (i == Skip) ? 0xffff0000 : 0xff800000);
        }


        // Wenn noetig, dann highlight aufheben!!!
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(dpfeiloben, pPfeilOben.x + mainFrame.scrollx, pPfeilOben.y + mainFrame.scrolly, observer);
                break;
            case 2:
                g.drawImage(dpfeilunten, pPfeilUnten.x + mainFrame.scrollx, pPfeilUnten.y + mainFrame.scrolly, observer);
                break;
            default:
                System.out.println("Falsches Menu-Item zum abdunkeln!!!");
        }

        if (olditem != 0) {
            olditem = 0;
        }

        // Wenn noetig, dann highlighten!!!
        switch (menuitem) {
            case 0:
                break;
            case 1:
                g.drawImage(pfeiloben, pPfeilOben.x + mainFrame.scrollx, pPfeilOben.y + mainFrame.scrolly, observer);
                break;
            case 2:
                g.drawImage(pfeilunten, pPfeilUnten.x + mainFrame.scrollx, pPfeilUnten.y + mainFrame.scrolly, observer);
                break;
            default:
                System.out.println("Falsches Menu-Item!!!");
        }

        if (menuitem != 0) {
            olditem = menuitem;
        }

    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        GenericPoint pTemp = e.getPoint();

        if (e.isRightClick()) {
            // linke Maustaste
            // Pfeil-Oben gedrueckt
            if (brPfeilOben.IsPointInRect(pTemp)) {
                Index -= 10;
                mainFrame.Clipset = false;
                if (Index < 0) {
                    Index = 0;
                }
                mainFrame.repaint();
            }

            // Pfeil-Unten gedrueckt
            if (brPfeilUnten.IsPointInRect(pTemp)) {
                Index += 10;
                mainFrame.Clipset = false;
                if (Index > (Nummer - 10)) {
                    Index = Nummer - 10;
                }
                mainFrame.repaint();
            }

            // Buckstabe wurde ausgewaehlt
            if (brGesamt.IsPointInRect(pTemp)) {
                int adresse = 0;

                // Y-Offset berechnen
                adresse += ((pTemp.y - LinksOben.y) / YDIFF) * ((mainFrame.sprache == 2) ? DXANZA : HXANZA);

                // X-Offset dazu
                adresse += ((pTemp.x - LinksOben.x) / XDIFF);

                // Index neu festlegen, nicht, wenn auf Leerfeld in HS gedrueckt
                if ((mainFrame.sprache != 1) || (adresse < 25)) {
                    Index = (mainFrame.sprache == 2) ? DSprung[adresse] : HSprung[adresse];
                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                }
            }
        } else {
            // rechte Maustaste, sofort raus (immer)
            Deactivate();
        }

    }

    public void evalMouseMoveEvent(GenericPoint pTemp) {

        // if ((NachOben.IsPointInRect (pTemp) == false) && (NachUnten.IsPointInRect (pTemp) == false))
        // {
        if (Cursorform != 0) {
            Cursorform = 0;
            mainFrame.setCursor(mainFrame.Normal);
        }
        // }

        // Highlight im Menue festlegen
        menuitem = 0;
        if (brPfeilOben.IsPointInRect(pTemp)) {
            menuitem = 1;
        }
        if (brPfeilUnten.IsPointInRect(pTemp)) {
            menuitem = 2;
        }

        int oldskip = Skip;
        if (!brGesamt.IsPointInRect(pTemp)) {
            Skip = -1;
        } else {
            Skip = 0;

            // Y-Offset berechnen
            Skip += ((pTemp.y - LinksOben.y) / YDIFF) * ((mainFrame.sprache == 2) ? DXANZA : HXANZA);

            // X-Offset dazu
            Skip += ((pTemp.x - LinksOben.x) / XDIFF);
        }

        // wenn noetig , dann Neuzeichnen!
        if (Paintcall) {
            Paintcall = false;
            return;
        }
        if ((menuitem != olditem) || (Skip != oldskip)) {
            mainFrame.repaint();
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
        menuitem = 0;
        Skip = -1;
        mainFrame.repaint();
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
        mainFrame.Clipset = false;
        mainFrame.DestructLocation(107);
        mainFrame.whatScreen = 0;

        mainFrame.NoPaint(false);

        mainFrame.repaint();
    }

    // alle Zeichen in ASCII umwandeln, dabei Sonderzeichen erzeugen
    /*private String ConvertEntry (int Nummer)
      {
      // entweder normales ASCII-Zeichen zurueckgeben oder Umwandlung bei Sonderzeichen
      if (Nummer > 0) return (new String (new char[] {(char) Nummer}));
    
      Nummer += OFFSET;
    
      switch (Nummer)
      {
      case 0xb3:
      return ("#l");
      case 0x9c:
      return ("#y");
      case 0x9e:
      return ("#z");
      case 0x9f:
      return ("#d");
      case 0xe6:
      return ("#c");
      case 0x9a:
      return ("#s");
      case 0xec:
      return ("#e");
      case 0xe0:
      return ("#x");
      case 0xfc:
      return ("#u");
      case 0xe4:
      return ("#a");
      case 0xf6:
      return ("#p");			
      case 0xdf:
      return ("#t");
      case 0xc8:
      return ("#K");
      case 0xf1:
      return ("#n");
      case 0xe8:
      return ("#k");
      case 0xd6:
      return ("#P");
      case 0xdc:
      return ("#U");
      case 0xc4:
      return ("#A");		    
      default:
      System.out.println ("Fehler bei ASCIIWANDLUNG !! " + Nummer);
      return (" ");
      }		
      }
  
      // true, wenn erster String groesser ist (spaeter erscheinen muss)
      private boolean istGroesserAls (String Erster, String Zweiter)
      {
      // Strings beide konvertieren, so dass aus Sonderzeichen normale Zeichen werden
      String Er = Convert (Erster);
      String Zw = Convert (Zweiter);
    
      if (Er.compareToIgnoreCase (Zw) >= 0) return true;
      else return false;
      }
  
      // true, wenn erster String kleiner ist (eher erscheinen muss)
      private boolean istKleinerAls (String Erster, String Zweiter)
      {
      return !(istGroesserAls (Erster, Zweiter));
      }		
  
      private String Convert (String Source2)
      {
      String Target = "";
      String Source = Source2.toLowerCase ();
  	
      for (int i = 0; i < Source.length(); i++)
      {
      if (Source.charAt (i) != 36) Target += new String (new char[] {Source.charAt (i)});
      else
      {
      i++;
      switch (Source.charAt (i))
      {
      case 99:
      Target += "c";
      break;
      case 107:
      Target += "c";	
      break;
      case 100:
      Target += "z";
      break;
      case 122:
      Target += "z";
      break;
      case 101:
      Target += "e";
      break;
      case 108:
      Target += "l";
      break;
      case 110:
      Target += "n";
      break;
      case 114:
      Target += "r";
      break;
      case 115:
      Target += "s";
      break;
      case 111:
      Target += "o";
      break;
      case 120:
      Target += "x";
      break;
      case 121:
      Target += "y";
      break;
      default: 
      System.out.println ("Falscher Char in Convert !");
      break;	
      }	  
      }		
      }
    
      return Target;
      }		
  
      // alle Eintraege sortieren
      private void Sort (int wieviele)
      {
      boolean binfertig;
    
      String serbtemp, deuttemp;
    
      // solange, bis keine Aenderung mehr gemacht werden musste
      do
      {
      binfertig = true;
    	
      for (int i = 0; i < (wieviele - 1); i++)
      {
      if (istGroesserAls (Eintrag[i][0], Eintrag[i+1][0]) == true)
      {
      binfertig = false;
      serbtemp = Eintrag[i][0];
      deuttemp = Eintrag[i][1];
      Eintrag[i][0] = Eintrag[i+1][0];
      Eintrag[i][1] = Eintrag[i+1][1];
      Eintrag[i+1][0] = serbtemp;
      Eintrag[i+1][1] = deuttemp;
      }
      }
      }
      while (binfertig == false);				
      }*/
}
