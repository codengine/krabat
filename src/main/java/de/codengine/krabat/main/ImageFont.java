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

public class ImageFont {
    private static final int SPACE = 8;  // Breite eines Spaces in Pixeln
    private static final int LINE_HEIGHT = 25; // Abstand von 2 Zeilen
    public static final int MAX_WIDTH = 600;
    private static final Logger log = LoggerFactory.getLogger(ImageFont.class);
    public final GenericImage[] redFont;
    private final Start mainFrame;
    private static final int TIME = 3;  // entspricht 0.3 Sekunden

    // Variablen fuer das Cacheing
    private static final int CACHE_SIZE = 150;
    private final GenericImage[] cache;
    private final int[][] cacheContent;
    private int counter = 1; // Rotationsprinzip

    // TODO image observer handling might not be correct!!!!

    // Konstruktor
    public ImageFont(Start caller) {
        mainFrame = caller;

        redFont = new GenericImage[240];
        cache = new GenericImage[CACHE_SIZE];
        cacheContent = new int[CACHE_SIZE][3];

        ImageHelperStatic im = new ImageHelperStatic();
        im.cutFont(redFont);

        // Cache saeubern
        for (int i = 1; i < CACHE_SIZE; i++) {
            cacheContent[i][1] = 0;
            cacheContent[i][2] = 0;
        }
    }

    // Schreiben eines Strings an best. Pos. in Grafik-Kontext g mit jeweiliger Farbe
    public void drawString(GenericDrawingContext g, String str, int xPos, int yPos, int color) {
        // bei Nullstring tschau
        if (Objects.equals(str, "")) {
            return;
        }

        int offset;                         // Offset der Y - Position des Zeichens
        int laenge = str.length();         // Laenge des Strings insgesamt in Zeichen
        int newXpos = xPos;                // Aktuelle X - Position
        int textl = lineLength(str);       // Laenge des Strings im Bild in Pixeln (Zeilenumbruch usw...)
        boolean zentriert = true;           // Zentrierung oder linksbuendig

        // Fontfilter initialisieren, wenn noetig
        GenericImageFilter change = null;
        if (color != 1) {
            change = new FontFilter(color);
        }

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
                    int tlaeng = lineLength(teil);

                    // Teilstring zentrieren innerhalb gesamter Textbreite
                    newXpos = textl / 2 - tlaeng / 2 + xPos;
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
                    int tlaeng = lineLength(teil);

                    // Teilstring zentrieren innerhalb gesamter Textbreite
                    newXpos = textl / 2 - tlaeng / 2 + xPos;
                } else {
                    // Linksbuendig, also sehr einfach :-)
                    newXpos = xPos;
                }

                // Y - Position "eine Zeile weiter"
                yPos += LINE_HEIGHT;
                continue;
            }

            // wenn Space, dann ueberspringen
            if (ch == 32) {
                newXpos += SPACE;
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
                default:
                    offset = 0;
            }
            if (color != 1) {
                int tmp = 0;

                // Bild im Cache suchen
                for (int u = 1; u < CACHE_SIZE; u++) {
                    if (cacheContent[u][1] == ch && cacheContent[u][2] == color) {
                        tmp = u;
                        break;
                    }
                }

                // wenn gefunden, dann zeichnen
                if (tmp != 0) {
                    g.drawImage(cache[tmp], newXpos, yPos + offset);
                } else {
                    // Bild ist nicht im Cache, also neu erzeugen und im Cache ablegen
                    GenericImage tempIm = GenericToolkit.getDefaultToolkit().createImage(new GenericFilteredImageSource(redFont[ch].getSource(), change));

                    g.drawImage(tempIm, newXpos, yPos + offset);
                    cache[counter] = tempIm;
                    cacheContent[counter][1] = ch;
                    cacheContent[counter][2] = color;
                    counter++;
                    if (counter == CACHE_SIZE) {
                        counter = 1;
                    }
                }
            } else {
                g.drawImage(redFont[ch], newXpos, yPos + offset);
            }
            newXpos += redFont[ch].getWidth();
        }
    }

    // Gibt den Code des Sonderzeichens zurueck
    private int evalSpecialChar(int sChar) {
        int temp = 1;

        switch (sChar) {
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
                log.error("Achtung !!!!!!!!!! Falsches Sonderzeichen '{}' - Space auf dem Screen !!!!!!!!!!!!!", sChar);
                break;
        }

        return temp;
    }

    // Ermittelt die X - Breite eines vorgegebenen Textes (wurst, wie lang und wieviele Zeichen)
    public int lineLength(String text) {
        int bis = text.length();
        int laenge = 0;
        int tLaenge = 0;
        for (int i = 0; i < bis; i++) {
            int ch = text.charAt(i);
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
                        ch = text.charAt(i);
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
    public GenericPoint centerText(String text, GenericPoint position) {
        // Laenge des Textes in Pixeln ermitteln
        int laenge = lineLength(text);

        // zentrierte x - Position ermitteln
        int x = position.x - laenge / 2;

        // x - Position dem Viewport anpassen
        if (x < mainFrame.scrollX + 15) {
            x = mainFrame.scrollX + 15;
        }

        if (x + laenge > mainFrame.scrollX + 625) {
            x = mainFrame.scrollX + 625 - laenge;
        }

        // y - Position ueber der Figur anpassen
        int y = position.y - (getLineCount(text) - 1) * LINE_HEIGHT;

        // Redelaenge festlegen
        initTalkLength(text);

        return new GenericPoint(x, y);
    }

    // Routine zum Festlegen des TalkCount - Zaehlers
    private void initTalkLength(String text) {
        int zaehle = 0;
        int laenge = text.length();

        for (int i = 0; i < laenge; i++) {
            int ch = text.charAt(i);
            if (ch != 36 && ch != 32 && ch != 35) {
                zaehle++;
            }
        }
        int zwiwert = zaehle * TIME;
        if (zwiwert < 30) {
            zwiwert = 30;
        }

        mainFrame.talkCount = zwiwert;
    }

    // Routine, die Zeilen des Textes zaehlt und damit die Y - Breite bekanntgibt
    public int getLineCount(String text) {
        int zaehle = 1;
        int laenge = text.length();

        for (int i = 1; i < laenge; i++) {
            int ch = text.charAt(i);
            if (ch == 36) {
                zaehle++;
            }
        }
        return zaehle;
    }

    // Routine, die Text genau ueber Krabat zentriert
    public GenericPoint krabatText(String text) {
        BorderRect tmp = mainFrame.krabat.getBoundingBox();

        // Default : Abstand der letzten Zeile ist 25 Pixel von Krabat
        int ypos = tmp.topLeftPoint.y - 2 * LINE_HEIGHT;
        int xpos = (tmp.topLeftPoint.x + tmp.bottomRightPoint.x) / 2;

        GenericPoint krabatPoint = centerText(text, new GenericPoint(xpos, ypos));

        // Text nicht oben verschwinden lassen, lieber unter Krabat setzen
        if (krabatPoint.y < 20) {
            krabatPoint.y = tmp.bottomRightPoint.y + LINE_HEIGHT;
        }

        return krabatPoint;
    }

    // Routine, die Text ueber gegebene x - Koordinate im Viewport zentriert
    public GenericPoint centerAnimText(String text, GenericPoint position) {
        // Laenge des Textes in Pixeln ermitteln
        int laenge = lineLength(text);

        // zentrierte x - Position ermitteln
        int x = position.x - laenge / 2;

        // x - Position dem Viewport anpassen
        if (x < mainFrame.scrollX + 15) {
            x = mainFrame.scrollX + 15;
        }

        if (x + laenge > mainFrame.scrollX + 625) {
            x = mainFrame.scrollX + 625 - laenge;
        }

        // y - Position ueber der Figur anpassen
        int y = position.y - (getLineCount(text) - 1) * LINE_HEIGHT;

        return new GenericPoint(x, y);
    }

    public String splitTextKey(String langKey) {
        String input = Start.STRING_MANAGER.getTranslation(langKey);
        return splitText(input);
    }

    // Optimierte Methode, die die Zeilenbreite direkt aufsummiert
    public String splitText(String input) {
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
     * @param word      Der Wortpuffer.
     * @param lineWidth Aktuelle Breite der Zeile.
     * @param wordWidth Breite des aktuellen Wortes.
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