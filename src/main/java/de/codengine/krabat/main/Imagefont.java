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
import de.codengine.krabat.platform.GenericDrawingContext;
import de.codengine.krabat.platform.GenericImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Imagefont {
    private static final int SPACE = 8;  // Breite eines Spaces in Pixeln
    private static final int ABSTAND = 25; // Abstand von 2 Zeilen
    public static final int MAX_WIDTH = 600;
    private static final Logger log = LoggerFactory.getLogger(Imagefont.class);
    public final GenericImage[] redFont;
    private final Start mainFrame;
    // private imagehelpercutandsave im2;
    private static final int ZEIT = 3;  // entspricht 0.3 Sekunden

    // Variablen fuer das Cacheing
    private static final int GROESSE = 150;
    private final GenericImage[] cache;
    private final int[][] Inhalt;
    private int Counter = 1; // Rotationsprinzip

    // Hier Vars fuer Background-Verdunkelung
    /*
    
    private static final boolean clear_background = false; // Einschalten
    private boolean override_clear = false; // temporaere Variable, nicht aendern
    private static final boolean raster_background = false; // nicht alles loeschen, sondern "Geistermuster"

	*/

    // TODO image observer handling might not be correct!!!!

    // Konstruktor
    public Imagefont(Start caller) {
        mainFrame = caller;

        redFont = new GenericImage[240];
        cache = new GenericImage[GROESSE];
        Inhalt = new int[GROESSE][3];

        //    im2 = new imagehelpercutandsave ("gfx/sfont-cerw.gif");
        //  im2.CutFont (redFont);
        //im2 = null;

        Imagehelperstatic im = new Imagehelperstatic();
        im.CutFont(redFont);

        // Cache saeubern
        for (int i = 1; i < GROESSE; i++) {
            Inhalt[i][1] = 0;
            Inhalt[i][2] = 0;
        }
    }

    // Schreiben eines Strings an best. Pos. in Grafik-Kontext g mit jeweiliger Farbe
    public void drawString(GenericDrawingContext g, String str, int xpos, int ypos, int Farbe) {
        // bei Nullstring tschau
        if (Objects.equals(str, "")) {
            return;
        }

        int offset;                         // Offset der Y - Position des Zeichens
        int laenge = str.length();         // Laenge des Strings insgesamt in Zeichen
        int new_xpos = xpos;                // Aktuelle X - Position
        int textl = LineLength(str);       // Laenge des Strings im Bild in Pixeln (Zeilenumbruch usw...)
        boolean zentriert = true;           // Zentrierung oder linksbuendig

        // Fontfilter initialisieren, wenn noetig
        GenericImageFilter change = null;
        if (Farbe != 1) {
            change = new FontFilter(Farbe);
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
                    new_xpos = textl / 2 - tlaeng / 2 + xpos;
                }
            }

            // Zeilenumbruch realisieren
            if (ch == 36) {
                // Hier verzweigen fuer zentriert oder linksbuendig
                if (zentriert) {
                    String teil = "";

                    // String bis zum Ende bzw. zum naechsten Dollarzeichen umkopieren
                    for (int j = i + 1; j < laenge; j++) {
                        if (str.charAt(j) != 36) {
                            teil += str.charAt(j);
                        } else {
                            break;
                        }
                    }

                    // Laenge des Teilstrings ermitteln
                    int tlaeng = LineLength(teil);

                    // Teilstring zentrieren innerhalb gesamter Textbreite
                    new_xpos = textl / 2 - tlaeng / 2 + xpos;
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
                case 81:
                case 103:
                case 106:
                case 112:
                case 113:
                case 121:
                    offset = 4;
                    break;
                case 74:
                    offset = 2;
                    break;
                case 44:
                case 228:
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
                    if (Inhalt[u][1] == ch && Inhalt[u][2] == Farbe) {
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

                    g.drawImage(cache[tmp], new_xpos, ypos + offset);
                } else {
                    // Bild ist nicht im Cache, also neu erzeugen und im Cache ablegen
                    GenericImage TempIm = GenericToolkit.getDefaultToolkit().createImage(new GenericFilteredImageSource(redFont[ch].getSource(), change));

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

                    g.drawImage(TempIm, new_xpos, ypos + offset);
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

                g.drawImage(redFont[ch], new_xpos, ypos + offset);
            }
            new_xpos += redFont[ch].getWidth();
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
                log.error("Achtung !!!!!!!!!! Falsches Sonderzeichen '{}' - Space auf dem Screen !!!!!!!!!!!!!", s_char);
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
            if (ch == '$') {
                if (tLaenge > laenge) {
                    laenge = tLaenge;
                }
                tLaenge = 0;
            } else {
                if (ch == ' ') {
                    tLaenge += SPACE;
                } else {
                    if (ch == '#') {
                        i++;
                        ch = Text.charAt(i);
                        tLaenge += redFont[evalSpecialChar(ch)].getWidth();
                    } else {
                        tLaenge += redFont[ch].getWidth();
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
        int x = posit.x - laenge / 2;

        // x - Position dem Viewport anpassen
        if (x < mainFrame.scrollx + 15) {
            x = mainFrame.scrollx + 15;
        }

        if (x + laenge > mainFrame.scrollx + 625) {
            x = mainFrame.scrollx + 625 - laenge;
        }

        // y - Position ueber der Figur anpassen
        int y = posit.y - (ZeilenAnzahl(Text) - 1) * ABSTAND;

        // Redelaenge festlegen
        RedeLaenge(Text);

        return new GenericPoint(x, y);
    }

    // Routine zum Festlegen des TalkCount - Zaehlers
    private void RedeLaenge(String Text) {
        int zaehle = 0;
        int laenge = Text.length();

        for (int i = 0; i < laenge; i++) {
            int ch = Text.charAt(i);
            if (ch != 36 && ch != 32 && ch != 35) {
                zaehle++;
            }
        }
        int zwiwert = zaehle * ZEIT;
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
        int ypos = tmp.lo_point.y - 2 * ABSTAND;
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
        int x = posit.x - laenge / 2;

        // x - Position dem Viewport anpassen
        if (x < mainFrame.scrollx + 15) {
            x = mainFrame.scrollx + 15;
        }

        if (x + laenge > mainFrame.scrollx + 625) {
            x = mainFrame.scrollx + 625 - laenge;
        }

        // y - Position ueber der Figur anpassen
        int y = posit.y - (ZeilenAnzahl(Text) - 1) * ABSTAND;

        // Redelaenge festlegen
        // RedeLaenge (Text);

        return new GenericPoint(x, y);
    }
    // Optimierte Methode, die die Zeilenbreite direkt aufsummiert
    public String TeileText(String input) {
        // Eingabetext trimmen
        String inputTrimmed = input.trim();

        // Falls bereits das Formatzeichen '$' vorkommt, den Originaltext zurückgeben.
        if (inputTrimmed.indexOf('$') != -1) {
            return input;
        }

        final StringBuilder output = new StringBuilder();
        final StringBuilder line = new StringBuilder();  // aktuelle Zeile
        final StringBuilder word = new StringBuilder();    // aktuelles Wort (analog zu "teil")
        int lineWidth = 0;  // akkumulierte Breite der aktuellen Zeile
        int wordWidth = 0;    // akkumulierte Breite des aktuellen Wortes

        // Den Eingabetext Zeichen für Zeichen durchgehen
        for (int i = 0; i < inputTrimmed.length(); i++) {
            char ch = inputTrimmed.charAt(i);

            if (ch == '$') {
                // Sollte das Formatzeichen '$' vorkommen, geben wir den Originaltext zurück.
                return input;
            } else if (ch == '%') {
                // Prozentzeichen: erzwungener Umbruch
                // Zuerst das aktuelle Wort (falls vorhanden) in die Zeile einfügen
                flushWord(output, line, word, lineWidth, wordWidth);
                // Dann den erzwungenen Zeilenumbruch: aktuelle Zeile abschließen und zurücksetzen
                output.append(line).append("$");
                line.setLength(0);
                lineWidth = 0;
                // Wortpuffer wird ohnehin geleert
                word.setLength(0);
                wordWidth = 0;
            } else if (ch == ' ') {
                // Leerzeichen: Prüfen, ob unmittelbar ein Satzzeichen folgt
                if (i + 1 < inputTrimmed.length()) {
                    char next = inputTrimmed.charAt(i + 1);
                    if (next == '!' || next == '?' || next == '.' || next == '-') {
                        // Falls ja, wird das Leerzeichen als Bestandteil des Wortes übernommen
                        word.append(ch);
                        wordWidth += SPACE;
                        continue;
                    }
                }
                // Andernfalls wird das aktuelle Wort abgeschlossen und in die Zeile eingefügt.
                int[] res = flushWord(output, line, word, lineWidth, wordWidth);
                lineWidth = res[0];
                wordWidth = res[1]; // sollte 0 sein
            } else {
                // Normaler Zeichenfall
                if (ch == '#') {
                    // Sonderfall: '#' plus das folgende Zeichen als spezielles Symbol
                    word.append(ch);
                    if (i + 1 < inputTrimmed.length()) {
                        i++;
                        char next = inputTrimmed.charAt(i);
                        word.append(next);
                        // Die Breite des Spezialzeichens wird ermittelt über evalSpecialChar(...)
                        wordWidth += redFont[evalSpecialChar(next)].getWidth();
                    }
                } else {
                    word.append(ch);
                    wordWidth += redFont[ch].getWidth();
                }
            }
        }

        // Nach der Schleife: Falls noch ein Wort im Puffer ist, in die Zeile einfügen.
        flushWord(output, line, word, lineWidth, wordWidth);

        // Restliche Zeile anhängen
        output.append(line);

        return output.toString();
    }

    /**
     * Hilfsmethode zum "Flushen" des aktuellen Wortes in die laufende Zeile.
     * Wird aufgerufen, wenn ein Leerzeichen (ohne direkt folgendes Satzzeichen) oder
     * ein Umbruch (durch '%') erfolgt.
     * <p>
     * Die Methode prüft, ob das aktuelle Wort (ggf. mit einem dazwischen zu setzenden Leerzeichen,
     * falls die Zeile nicht leer ist) in die laufende Zeile passt. Falls nicht, wird die Zeile
     * abgeschlossen (mit '$') und eine neue Zeile begonnen.
     *
     * @param output    Der bisherige Ausgabepuffer.
     * @param line      Die laufende Zeile.
     * @param word       Der Wortpuffer.
     * @param lineWidth Aktuelle Breite der Zeile.
     * @param wordWidth   Breite des aktuellen Wortes.
     * @return Ein int-Array mit zwei Werten: [neue Zeilenbreite, 0 (Wortbreite nach Flush)].
     */
    private int[] flushWord(StringBuilder output, StringBuilder line, StringBuilder word, int lineWidth, int wordWidth) {
        // Falls kein Wort vorhanden, nichts zu tun
        if (wordWidth == 0) {
            return new int[]{lineWidth, 0};
        }
        // Bestimmen, ob ein Leerzeichen (zwischen den Wörtern) benötigt wird
        boolean needSpace = line.length() > 0;
        int spaceWidth = needSpace ? SPACE : 0;

        // Passt das Wort (ggf. inkl. Leerzeichen) in die aktuelle Zeile?
        if (lineWidth + spaceWidth + wordWidth <= MAX_WIDTH) {
            // Falls ja, ggf. Leerzeichen anhängen
            if (needSpace) {
                line.append(' ');
                lineWidth += SPACE;
            }
            line.append(word);
            lineWidth += wordWidth;
        } else {
            // Passt nicht mehr: die aktuelle Zeile abschließen und in den Ausgabepuffer schreiben
            output.append(line).append('$');
            // Neue Zeile beginnen: Hier entfällt das führende Leerzeichen, daher:
            line.setLength(0);
            line.append(word);
            lineWidth = wordWidth;
        }
        // Wortpuffer leeren
        word.setLength(0);
        wordWidth = 0;
        return new int[]{lineWidth, wordWidth};
    }
}