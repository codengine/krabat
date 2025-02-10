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

package rapaki.krabat.main;

import rapaki.krabat.Start;
import rapaki.krabat.platform.GenericDrawingContext;
import rapaki.krabat.platform.GenericImage;
import rapaki.krabat.platform.GenericImageObserver;

public class Imagefont {
    private static final int SPACE = 8;  // Breite eines Spaces in Pixeln
    private static final int ABSTAND = 25; // Abstand von 2 Zeilen
    public GenericImage redFont[];
    private Start mainFrame;
    private Imagehelperstatic im;
    // private imagehelpercutandsave im2;
    private static final int ZEIT = 3;  // entspricht 0.3 Sekunden
    private GenericImageFilter Change;

    // Variablen fuer das Cacheing
    private static final int GROESSE = 150;
    private GenericImage[] cache;
    private int Inhalt[][];
    private int Counter = 1; // Rotationsprinzip

    // Hier Vars fuer Background-Verdunkelung
    /*
    
    private static final boolean clear_background = false; // Einschalten
    private boolean override_clear = false; // temporaere Variable, nicht aendern
    private static final boolean raster_background = false; // nicht alles loeschen, sondern "Geistermuster"

	*/

    private final GenericImageObserver observer;

    // TODO image observer handling might not be correct!!!!

    // Konstruktor
    public Imagefont(Start caller, GenericImageObserver observer) {
        mainFrame = caller;

        this.observer = observer;
        redFont = new GenericImage[240];
        cache = new GenericImage[GROESSE];
        Inhalt = new int[GROESSE][3];

        //    im2 = new imagehelpercutandsave ("gfx/sfont-cerw.gif");
        //  im2.CutFont (redFont);
        //im2 = null;

        im = new Imagehelperstatic(observer);
        im.CutFont(redFont);
        im = null;

        // Cache saeubern
        for (int i = 1; i < GROESSE; i++) {
            Inhalt[i][1] = 0;
            Inhalt[i][2] = 0;
        }
    }

    // Schreiben eines Strings an best. Pos. in Grafik-Kontext g mit jeweiliger Farbe
    public void drawString(GenericDrawingContext g, String str, int xpos, int ypos, int Farbe) {
        // bei Nullstring tschau
        if (str == "") {
            return;
        }

        int offset;                         // Offset der Y - Position des Zeichens
        int laenge = str.length();         // Laenge des Strings insgesamt in Zeichen
        int new_xpos = xpos;                // Aktuelle X - Position
        int textl = LineLength(str);       // Laenge des Strings im Bild in Pixeln (Zeilenumbruch usw...)
        boolean zentriert = true;           // Zentrierung oder linksbuendig

        // Fontfilter initialisieren, wenn noetig
        Change = null;
        if (Farbe != 1) {
            Change = new FontFilter(Farbe);
        }

        // feststellen, ob Rot oder Gruen oder Dunkelgruen oder Dunkelrot -> Keinen Hintergrund schwaerzen
//	if ((Farbe == 1) || (Farbe == 0xffff0000) || (Farbe == 0xffb00000) || (Farbe == 0xff00b000) || (Farbe == 0xff00ff00)) override_clear = true;
//	else override_clear = false;


        // jedes Zeichen einzeln kopieren
        for (int i = 0; i < laenge; i++) {
            // Aktuelles Zeichen herholen
            int ch = str.charAt(i);

            // Entscheidung, ob zentriert wird oder linksbuendig geschrieben wird
            if (i == 0) {
                // 1. Zeichen im String = "$" bedeutet linksbuendig
                if (ch == 36) {
                    zentriert = false;
                    continue;
                } else {
                    zentriert = true;

                    // hier folgt die komplette Routine nochmal, da anders reagiert werden muss (kein $ da)
                    String teil = "";

                    // String bis zum Ende bzw. zum naechsten Dollarzeichen umkopieren
                    for (int j = i; j < laenge; j++) {
                        if (str.charAt(j) != 36) {
                            teil += str.charAt(j);
                        } else {
                            break;
                        }
                    }

                    // Laenge des Teilstrings ermitteln
                    int tlaeng = LineLength(teil);

                    // Teilstring zentrieren innerhalb gesamter Textbreite
                    new_xpos = (textl / 2) - (tlaeng / 2) + xpos;
                }
            }

            // Zeilenumbruch realisieren
            if (ch == 36) {
                // Hier verzweigen fuer zentriert oder linksbuendig
                if (zentriert == true) {
                    String teil = "";

                    // String bis zum Ende bzw. zum naechsten Dollarzeichen umkopieren
                    for (int j = (i + 1); j < laenge; j++) {
                        if (str.charAt(j) != 36) {
                            teil += str.charAt(j);
                        } else {
                            break;
                        }
                    }

                    // Laenge des Teilstrings ermitteln
                    int tlaeng = LineLength(teil);

                    // Teilstring zentrieren innerhalb gesamter Textbreite
                    new_xpos = (textl / 2) - (tlaeng / 2) + xpos;
                } else {
                    // Linksbuendig, also sehr einfach :-)
                    new_xpos = xpos;
                }

                // Y - Position "eine Zeile weiter"
                ypos += ABSTAND;
                continue;
            }

            // wenn Space, dann ueberspringen
            if (ch == 32) {
                // hier auch das Space loeschen, wenn eingeschaltet
//			if ((clear_background == true) && (override_clear == false))
//			    {
//				if (raster_background == true)
//				    {
//					for (int k = new_xpos; k <= (new_xpos + SPACE); k++) {
//					    for (int j = ypos; j <= (ypos + ABSTAND); j++) {
//						if (((k+j) % 2) == 0)
//						    g.clearRect (k, j, 1, 1);
//					    }
//					}
//				    }
//				else
//				    {
//					g.clearRect (new_xpos, ypos, SPACE, ABSTAND);
//				    }
//			    }

                new_xpos += SPACE;
                continue;
            }
            // falls Raute, Sonderzeichen ermitteln
            if (ch == 35) {
                ch = evalSpecialChar(str.charAt(++i));
            }

            // Zeichen ausschneiden und in Kontext zeichnen
            switch (ch) {
                case 103:
                    offset = 4;
                    break;
                case 106:
                    offset = 4;
                    break;
                case 112:
                    offset = 4;
                    break;
                case 113:
                    offset = 4;
                    break;
                case 121:
                    offset = 4;
                    break;
                case 74:
                    offset = 2;
                    break;
                case 81:
                    offset = 4;
                    break;
                case 44:
                    offset = 3;
                    break;
                case 228:
                    offset = 3;
                    break;
                case 231:
                    offset = 3;
                    break;
                //case 41:  offset=3;
                //          break;
                default:
                    offset = 0;
            }
            if (Farbe != 1) {
                int tmp = 0;

                // Bild im Cache suchen
                for (int u = 1; u < GROESSE; u++) {
                    if ((Inhalt[u][1] == ch) && (Inhalt[u][2] == Farbe)) {
                        tmp = u;
                        break;
                    }
                }

                // wenn gefunden, dann zeichnen
                if (tmp != 0) {
//				if ((clear_background == true) && (override_clear == false))
//				    {
//					if (raster_background == true)
//					    {
//						for (int k = new_xpos; k <= (new_xpos + redFont[ch].getWidth(this)); k++) {
//						    for (int j = ypos; j <= (ypos + ABSTAND); j++) {
//							if (((k+j) % 2) == 0)
//							    g.clearRect (k, j, 1, 1);
//						    }
//						}
//					    }
//					else
//					    {
//						g.clearRect (new_xpos, ypos, redFont[ch].getWidth(this), ABSTAND);
//					    }
//				    }

                    g.drawImage(cache[tmp], new_xpos, ypos + offset, null);
                } else {
                    // Bild ist nicht im Cache, also neu erzeugen und im Cache ablegen
                    GenericImage TempIm = GenericToolkit.getDefaultToolkit().createImage(new GenericFilteredImageSource(redFont[ch].getSource(), Change));

//				if ((clear_background == true) && (override_clear == false))
//				    {
//					if (raster_background == true)
//					    {
//						for (int k = new_xpos; k <= (new_xpos + redFont[ch].getWidth(this)); k++) {
//						    for (int j = ypos; j <= (ypos + ABSTAND); j++) {
//							if (((k+j) % 2) == 0)
//							    g.clearRect (k, j, 1, 1);
//						    }
//						}
//					    }
//					else
//					    {
//						g.clearRect (new_xpos, ypos, redFont[ch].getWidth(this), ABSTAND);
//					    }
//				    }

                    g.drawImage(TempIm, new_xpos, ypos + offset, null);
                    cache[Counter] = TempIm;
                    Inhalt[Counter][1] = ch;
                    Inhalt[Counter][2] = Farbe;
                    Counter++;
                    // System.out.print (Counter + "  ");
                    if (Counter == GROESSE) {
                        Counter = 1;
                    }
                }
            } else {
//			if ((clear_background == true) && (override_clear == false))
//			    {
//				if (raster_background == true)
//				    {
//					for (int k = new_xpos; k <= (new_xpos + redFont[ch].getWidth(this)); k++) {
//					    for (int j = ypos; j <= (ypos + ABSTAND); j++) {
//						if (((k+j) % 2) == 0)
//						    g.clearRect (k, j, 1, 1);
//					    }
//					}
//				    }
//				else
//				    {
//					g.clearRect (new_xpos, ypos, redFont[ch].getWidth(this), ABSTAND);
//				    }
//			    }

                g.drawImage(redFont[ch], new_xpos, ypos + offset, null);
            }
            new_xpos += redFont[ch].getWidth(observer);
        }
    }

    // Gibt den Code des Sonderzeichens zurueck
    private int evalSpecialChar(int s_char) {
        int temp = 1;

        switch (s_char) {
            // kleine sorb. Sonderzeichen
            case 99:
                temp = 200;
                break;         // 'c  (c)
            case 107:
                temp = 201;
                break;        // "c  (k)
            case 100:
                temp = 202;
                break;        // 'z  (d)
            case 122:
                temp = 203;
                break;        // "z  (z)
            case 101:
                temp = 204;
                break;        // "e  (e)
            case 108:
                temp = 205;
                break;        // -l  (l)
            case 110:
                temp = 206;
                break;        // 'n  (n)
            case 114:
                temp = 207;
                break;        // "r  (r)
            case 115:
                temp = 208;
                break;        // "s  (s)
            case 111:
                temp = 209;
                break;        // 'o  (o)

            // grosse sorb. Sonderzeichen
            case 67:
                temp = 210;
                break;        // 'C  (C)
            case 75:
                temp = 211;
                break;        // "C  (K)
            case 68:
                temp = 212;
                break;        // 'Z  (D)
            case 90:
                temp = 213;
                break;        // "Z  (Z)
            case 69:
                temp = 214;
                break;        // "E  (E)
            case 76:
                temp = 215;
                break;        // -L  (L)
            case 78:
                temp = 216;
                break;        // 'N  (N)
            case 82:
                temp = 217;
                break;        // "R  (R)
            case 83:
                temp = 218;
                break;        // "S  (S)
            case 79:
                temp = 227;
                break;        // 'O  (O)

            // kleine deutsche Sonderzeichen
            case 97:
                temp = 219;
                break;       // ? (a)
            case 117:
                temp = 220;
                break;       // ? (u)
            case 112:
                temp = 221;
                break;       // ? (p)
            case 116:
                temp = 222;
                break;       // ? (t)

            // grosse deutsche Sonderzeichen
            case 65:
                temp = 223;
                break;       // ? (A)
            case 85:
                temp = 224;
                break;       // ? (U)
            case 80:
                temp = 225;
                break;       // ? (P)
            case 84:
                temp = 226;
                break;       // gross ? (T)

            // Anfuehrungszeichen
            case 102:
                temp = 228;
                break;       // Anfueh. unten (f)
            case 70:
                temp = 229;
                break;       // Anfueh. oben  (F)
            case 103:
                temp = 231;
                break;       // Gedankenstrich unten (g)
            case 71:
                temp = 230;
                break;       // Gedankenstrich oben (G)

            // niedersorbische Sonderzeichen
            case 89:
                temp = 235;
                break;             // Gross S-Strich (Y)
            case 121:
                temp = 236;
                break;       // Klein s-Strich (y)
            case 88:
                temp = 237;
                break;       // Gross R-Strich (X)
            case 120:
                temp = 238;
                break;             // klein r-Strich (x)
            default:
                System.out.println("Achtung !!!!!!!!!! Falsches Sonderzeichen - Space auf dem Screen !!!!!!!!!!!!!");
                break;
        }

        return temp;
    }

    // Ermittelt die X - Breite eines vorgegebenen Textes (wurst, wie lang und wieviele Zeichen)
    public int LineLength(String Text) {
        int bis = Text.length();
        int laenge = 0;
        int tLaenge = 0;
        for (int i = 0; i < bis; i++) {
            int ch = Text.charAt(i);
            if (ch == 36) {
                if (tLaenge > laenge) {
                    laenge = tLaenge;
                }
                tLaenge = 0;
            } else {
                if (ch == 32) {
                    tLaenge += SPACE;
                } else {
                    if (ch == 35) {
                        i++;
                        ch = Text.charAt(i);
                        tLaenge += redFont[evalSpecialChar(ch)].getWidth(observer);
                    } else {
                        tLaenge += redFont[ch].getWidth(observer);
                    }
                }
            }
        }
        if (tLaenge > laenge) {
            laenge = tLaenge;
        }
        return laenge;
    }

    // Routine, die Text ueber gegebene x - Koordinate im Viewport zentriert
    public GenericPoint CenterText(String Text, GenericPoint posit) {
        // Laenge des Textes in Pixeln ermitteln
        int laenge = LineLength(Text);

        // zentrierte x - Position ermitteln
        int x = posit.x - (laenge / 2);

        // x - Position dem Viewport anpassen
        if (x < (mainFrame.scrollx + 15)) {
            x = mainFrame.scrollx + 15;
        }

        if ((x + laenge) > (mainFrame.scrollx + 625)) {
            x = mainFrame.scrollx + 625 - laenge;
        }

        // y - Position ueber der Figur anpassen
        int y = posit.y - (ZeilenAnzahl(Text) - 1) * ABSTAND;

        // Redelaenge festlegen
        RedeLaenge(Text);

        return (new GenericPoint(x, y));
    }

    // Routine zum Festlegen des TalkCount - Zaehlers
    private void RedeLaenge(String Text) {
        int zaehle = 0;
        int laenge = Text.length();

        for (int i = 0; i < laenge; i++) {
            int ch = Text.charAt(i);
            if ((ch != 36) && (ch != 32) && (ch != 35)) {
                zaehle++;
            }
        }
        int zwiwert = (int) zaehle * ZEIT;
        if (zwiwert < 30) {
            zwiwert = 30;
        }

        mainFrame.talkCount = zwiwert;
    }

    // Routine, die Zeilen des Textes zaehlt und damit die Y - Breite bekanntgibt
    public int ZeilenAnzahl(String Text) {
        int zaehle = 1;
        int laenge = Text.length();

        for (int i = 1; i < laenge; i++) {
            int ch = Text.charAt(i);
            if (ch == 36) {
                zaehle++;
            }
        }
        return zaehle;
    }

    // Routine, die Text genau ueber Krabat zentriert
    public GenericPoint KrabatText(String Text) {
        Borderrect tmp = mainFrame.krabat.KrabatRect();

        // Default : Abstand der letzten Zeile ist 25 Pixel von Krabat
        int ypos = tmp.lo_point.y - (2 * ABSTAND);
        int xpos = (tmp.lo_point.x + tmp.ru_point.x) / 2;

        GenericPoint KraPoint = CenterText(Text, new GenericPoint(xpos, ypos));

        // Text nicht oben verschwinden lassen, lieber unter Krabat setzen
        if (KraPoint.y < 20) {
            KraPoint.y = tmp.ru_point.y + ABSTAND;
        }

        return KraPoint;
    }

    // Routine, die Text ueber gegebene x - Koordinate im Viewport zentriert
    public GenericPoint CenterAnimText(String Text, GenericPoint posit) {
        // Laenge des Textes in Pixeln ermitteln
        int laenge = LineLength(Text);

        // zentrierte x - Position ermitteln
        int x = posit.x - (laenge / 2);

        // x - Position dem Viewport anpassen
        if (x < (mainFrame.scrollx + 15)) {
            x = mainFrame.scrollx + 15;
        }

        if ((x + laenge) > (mainFrame.scrollx + 625)) {
            x = mainFrame.scrollx + 625 - laenge;
        }

        // y - Position ueber der Figur anpassen
        int y = posit.y - (ZeilenAnzahl(Text) - 1) * ABSTAND;

        // Redelaenge festlegen
        // RedeLaenge (Text);

        return (new GenericPoint(x, y));
    }

    // Teile String in so viele Teile, dass die auf Bildschirm passen - moeglichst obere Zeile laenger...
    public String TeileText(String EingabeTwo) {
        String Eingabe = EingabeTwo.trim();
        String Ausgabe = "";
        String Zeile = "";
        String Teil = "";

        int bis = Eingabe.length();
        int laenge = 0;

        for (int i = 0; i < bis; i++) {
            int ch = Eingabe.charAt(i);
            if (ch == 36) {
                // Formatzeichen entdeckt, also nix veraendern
                return EingabeTwo;
            } else {
                if (ch == 37) {
                    // Prozentzeichen entdeckt, also Umbruch einfuegen

                    // zuerst testen, ob Zeile nicht sowieso schon zu lang ist
                    if (LineLength(Zeile + Teil) < 600) {
                        Zeile = Zeile + Teil;
                        Teil = " ";
                        laenge += SPACE;
                    } else {
                        // Laenger darfs nicht werden...
                        Ausgabe = Ausgabe + Zeile + "$";
                        Zeile = "";
                        if (Teil != "") {
                            Teil = Teil.substring(1) + " ";
                        }
                        Zeile = Zeile + Teil;
                        Teil = "";
                        laenge = 0;
                    }

                    // Hier den Umbruch einfuegen, der sein muss
                    if (Teil != "") {
                        Teil = Teil + " ";
                    }
                    Zeile = Zeile + Teil + "$";
                    Ausgabe = Ausgabe + Zeile;
                    Zeile = "";
                    Teil = "";
                    laenge = 0;
                } else {
                    if (ch == 32) {
                        // Space entdeckt, jetzt kalkulieren

                        // nix veraendern, wenn Ausrufezeichen, Fragezeichen, Punkt oder so an naechster Stelle
                        int chtemp = Eingabe.charAt(i + 1);
                        if ((chtemp == 33) || (chtemp == 63) || (chtemp == 46) || (chtemp == 45)) {
                            laenge += SPACE;
                            Teil = Teil + (char) ch;
                        } else {
                            // Zeile darf noch laenger werden

                            // System.out.println (LineLength (Zeile + Teil));

                            if (LineLength(Zeile + Teil) < 600) {
                                Zeile = Zeile + Teil;
                                Teil = " ";
                                laenge += SPACE;
                            } else {
                                // Laenger darfs nicht werden...
                                Ausgabe = Ausgabe + Zeile + "$";
                                Zeile = "";
                                if (Teil != "") {
                                    Teil = Teil.substring(1) + " ";
                                }
                                Zeile = Zeile + Teil;
                                Teil = "";
                                laenge = 0;
                            }
                        }
                    } else {
                        // Teilstring hinzufuegen, wenn nicht Space
                        Teil = Teil + (char) ch;

                        if (ch == 35) {
                            // Bei Sonderzeichen richtige Laenge ermitteln
                            i++;
                            ch = Eingabe.charAt(i);
                            Teil = Teil + (char) ch;
                            laenge += redFont[evalSpecialChar(ch)].getWidth(observer);
                        } else {
                            laenge += redFont[ch].getWidth(observer);
                        }
                    }
                }
            }
        }
        if (LineLength(Zeile + Teil) < 600) {
            Zeile = Zeile + Teil;
            Teil = " ";
            laenge += SPACE;
            Ausgabe = Ausgabe + Zeile;
        } else {
            // Laenger darfs nicht werden...
            Ausgabe = Ausgabe + Zeile + "$";
            Zeile = "";
            if (Teil != "") {
                Teil = Teil.substring(1) + " ";
            }
            Zeile = Zeile + Teil;
            Teil = "";
            laenge = 0;
            Ausgabe = Ausgabe + Zeile;
        }
        return Ausgabe;
    }
}