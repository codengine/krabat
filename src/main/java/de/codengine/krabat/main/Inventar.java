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
import de.codengine.krabat.platform.GenericImageObserver;

import java.util.Vector;

public class Inventar extends Mainanim {
    private GenericImage iInventar, inactiveMenu, activeMenu;

    private final GenericImage[] InventarStuecke;
    private final int[] InventarID;

    private static final int INVENTAR_CACHE = 10;
    private int CachePosition = 1;

    public GenericImage Pfeill, DPfeill, Pfeilr, DPfeilr;  // damit man das auch woanders nutzen kann
    public Vector<Integer> vInventory;                       // Vektor , der alle Inventarstuecke beinhaltet
    private int nextActionID;                       // zur Textausgabe der Inventarstuecke
    private final GenericPoint pLO;                              // Offset der Linken oberen Ecke
    private final Borderrect brGesamt;
    private final Borderrect brMenu;            // Rectangles fuer mousemove
    public Borderrect brPfeill, brPfeilr;           // fuer andere Klassen nutzbar
    private int nFeldAktiv = -1;                    // Flags fuer roten Rahmen
    private int oFeldAktiv = -1;
    private String outputText = "";                 // fuer Textausgabe
    private GenericPoint outputTextPos;
    private int menuitem = 0;                       // Flags fuer Menuitems
    private int olditem = 0;
    private boolean Paintcall = false;              // soll mousemoved auch neu zeichnen??
    private final GenericColor inakt = new GenericColor(156, 132, 107); // Farbe zum loeschen roter Rahmen
    private boolean secScreenAvail = false;        // ist 2. Screen moeglich??
    private boolean secScreenActive = false;        // ist 2. Screen aktiv??
    private int Cursorform = 200;
    private int ytemp;                              // TempVariable fuer y - Position Text

    private final GenericPoint HotSpot;

    public boolean noBackgroundSound = false;       // Anzeige, ob Backgroundwavs deaktiviert werden sollen

    private final GenericImageObserver observer = null;

    // Texte fuer Standardausreden
    private static final String[] HAusreden = {"Main_Inventar_00000", "Main_Inventar_00001", "Main_Inventar_00002", "Main_Inventar_00003"};
    private static final String[] DAusreden = {"Main_Inventar_00004", "Main_Inventar_00005", "Main_Inventar_00006", "Main_Inventar_00007"};
    private static final String[] NAusreden = {"Main_Inventar_00008", "Main_Inventar_00009", "Main_Inventar_00010", "Main_Inventar_00011"};

    // ----PARSER_DISABLE----

    // Texte fuer das Laden der einzelnen Images
    private static final String ImageDirectory = "gfx/inventar/i-";
    private static final String CursorDirectory = "gfx/cursors/";
    private static final String Suffix = ".gif";

    // Imagefilenamen
    private static final String[] IconImages =
            {"leer", "floete2", "stock", "schild", "honck", "hocka", "lajna", "wuda1", "hadz", "wuda2",
                    "wuda3", "wuda4", "kamj", "dryba", "ryba", "krosk", "bloto", "rohodz", "bron", "pjero2",
                    "karta", "leer", "leer", "leer", "leer", "leer", "leer", "leer", "leer", "leer",
                    "list", "wop1", "wop2", "wop3", "wop4", "drjewo", "tiger", "kotwi", "lajna", "pkotwi",
                    "tolery", "drasta", "hlebija", "helmut", "wilhelm", "casnik", "hammer", "kluc", "metal", "prikaz",
                    "skica", "wosusk2", "wosusk", "drasta2", "karta", "kniha", "leer", "leer", "leer", "leer",
                    "skla", "koraktor", "kamjen", "stroh"};

    private static final String[] CursorImages =
            {"leer", "flej2", "kij2", "schild2", "honck", "hocka", "lajna", "kijw", "hadz", "kijwu",
                    "kijwuw", "kijwur", "kamj", "dryba", "ryba", "krosk", "bloto", "roh", "bron", "pjero",
                    "kkarta", "leer", "leer", "leer", "leer", "leer", "leer", "leer", "leer", "leer",
                    "list", "wop1", "wop2", "wop3", "wop4", "drjewo", "tiger", "kotwi", "lajna", "pkotwi",
                    "tolery", "drasta", "hlebija", "helmut", "wilhelm", "casnik", "hammer", "kluc", "metal", "prikaz",
                    "skica", "wosusk2", "wosusk", "drasta2", "kkarta", "kniha", "leer", "leer", "leer", "leer",
                    "skla", "koraktor", "kamjen", "stroh"};

    private static final String[] CursorHighImages =
            {"leer", "flej2-u", "kij-u3", "schild-u3", "honck-u", "hocka-u", "lajna-u", "kijw-u", "hadz-u", "kijwu-u",
                    "kijwuw-u", "kijwur-u", "kamj-u", "dryba-u", "ryba-u", "krosk-u", "bloto-u", "roh-u", "bron-u", "pjero-u",
                    "kkarta-u", "leer", "leer", "leer", "leer", "leer", "leer", "leer", "leer", "leer",
                    "list-u", "wop1-u", "wop2-u", "wop3-u", "wop4-u", "drjewo-u", "tiger-u", "kotwi-u", "lajna-u", "pkotwi-u",
                    "tolery-u", "drasta-u", "hlebija-u", "helmut-u", "wilhelm-u", "casnik-u", "hammer-u", "kluc-u", "metal-u", "prikaz-u",
                    "skica-u", "wosusk2-u", "wosusk-u", "drasta2-u", "kkarta-u", "kniha-u", "leer", "leer", "leer", "leer",
                    "skla-u", "koraktor-u", "kamjen-u", "stroh-u"};

    // ----PARSER_ENABLE----

    // Initialisierung ////////////////////////////////////////////////////////

    // Instanz von dieser Location erzeugen
    public Inventar(Start caller, GenericPoint HotSpot) {
        super(caller);

        InventarStuecke = new GenericImage[INVENTAR_CACHE + 1];
        InventarID = new int[INVENTAR_CACHE + 1];

        for (int i = 0; i <= INVENTAR_CACHE; i++) {
            InventarStuecke[i] = null;
            InventarID[i] = 0;
        }

        InitImages();

        this.HotSpot = HotSpot;

        // Rechtecke im Inventar-Fenster festlegen
        pLO = new GenericPoint(31, 31);
        brGesamt = new Borderrect(pLO.x + 65, pLO.y + 46,
                pLO.x + 513, pLO.y + 380);
        brMenu = new Borderrect(pLO.x + 415, pLO.y + 331,
                pLO.x + 493, pLO.y + 360);
        brPfeill = new Borderrect(pLO.x + 90, pLO.y + 319,
                pLO.x + 180, pLO.y + 358);
        brPfeilr = new Borderrect(pLO.x + 249, pLO.y + 319,
                pLO.x + 339, pLO.y + 358);
        vInventory = new Vector<Integer>();
        ResetInventory();
    }

    public void ResetInventory() {
        vInventory.removeAllElements();
        vInventory.addElement(1); // hier wird Floete addiert
    }

    // Bilder vorbereiten
    private void InitImages() {
        iInventar = getPicture("gfx/inventar/inventar.gif");
        activeMenu = getPicture("gfx/inventar/m-meni.gif");
        inactiveMenu = getPicture("gfx/inventar/meni.gif");
        Pfeill = getPicture("gfx/inventar/r-p-l.gif");
        DPfeill = getPicture("gfx/inventar/d-p-l.gif");
        Pfeilr = getPicture("gfx/inventar/r-p-r.gif");
        DPfeilr = getPicture("gfx/inventar/d-p-r.gif");
    }

    // Laderoutine fuer Inventarimages
    private GenericImage IconImages(int nGegID) {
        GenericImage InventarStueck = null;

        InventarStueck = getPicture(ImageDirectory + IconImages[nGegID] + Suffix);

        return InventarStueck;
    }

    // Laderoutine fuer Cursors
    private GenericImage GetCursorImage(int nGegID) {
        GenericImage Cursor = null;

        // Cursor = getPicture (CursorDirectory + CursorImages[nGegID] + Suffix);

        // loadPicture();

        // so long, obiges war einmal vor langer Zeit :-(

        Cursor = mainFrame.constructCursorImage(CursorDirectory + CursorImages[nGegID] + Suffix);

        return Cursor;
    }

    // Laderoutine fuer HighlightCursors
    private GenericImage GetCursorHighImage(int nGegID) {
        GenericImage CursorHigh = null;

        // CursorHigh = getPicture (CursorDirectory + CursorHighImages[nGegID] + Suffix);

        // loadPicture();

        // so long, obiges war einmal vor langer Zeit :-(

        CursorHigh = mainFrame.constructCursorImage(CursorDirectory + CursorHighImages[nGegID] + Suffix);

        return CursorHigh;
    }


    // Paint-Routine dieser Location //////////////////////////////////////////

    public void paintInventory(GenericDrawingContext g) {

        // Inventar-Background und Menuitems zeichnen
        if (!mainFrame.Clipset) {
            mainFrame.Clipset = true;

            // Inventarhintergrund zeichnen und Clipping - Region einengen
            g.setClip(0, 0, 1284, 964);
            g.drawImage(iInventar, pLO.x + mainFrame.scrollx, pLO.y + mainFrame.scrolly, null);
            g.setClip(90 + mainFrame.scrollx, 70 + mainFrame.scrolly, 550, 390);

            // Menuitems zeichnen
            if (!secScreenActive) {
                g.drawImage
                        (inactiveMenu, pLO.x + 415 + mainFrame.scrollx, pLO.y + 331 + mainFrame.scrolly, null);
            }
            g.drawImage(DPfeill, 119 + mainFrame.scrollx, 349 + mainFrame.scrolly, null);

            // Gegenstand-Icons zeichnen
            int nAnzahl = vInventory.size();
            if (nAnzahl > 9) {
                secScreenAvail = true;
                if (!secScreenActive) {
                    g.drawImage(DPfeilr, 279 + mainFrame.scrollx, 348 + mainFrame.scrolly, null);
                }
            } else {
                secScreenAvail = false;
            }
            int f = 0;
            if (secScreenActive) {
                f = 9;
            } else if (nAnzahl > 9) {
                nAnzahl = 9;
            }
            // System.out.println("Inventarstuecke : " + nAnzahl);
            for (int i = f; i < nAnzahl; i++) {
                int iTemp = vInventory.elementAt(i);
                GenericPoint pTemp = new GenericPoint(GetCurrentXY(i));
                if (mainFrame.whatItem != iTemp || !mainFrame.invCursor) {
                    g.drawImage(GetIconImage(iTemp), pTemp.x + 1 + mainFrame.scrollx, pTemp.y + 1 + mainFrame.scrolly, observer);
                }
            }

            // original Mauspoint bei Init beachten, erst zuletzt, da es die Variablen "secScreenAvail" gueltig benoetigt
            Cursorform = 200;
            Paintcall = true;
            evalMouseMoveEvent(mainFrame.Mousepoint);
        }

        // Alten roten Rahmen loeschen
        if (oFeldAktiv >= 0) {
            g.setColor(inakt);
            GenericPoint pTemp = GetCurrentXY(oFeldAktiv);
            g.drawRect(pTemp.x + mainFrame.scrollx, pTemp.y + mainFrame.scrolly, 145, 75);
            oFeldAktiv = -1;
        }

        // Ist ein Feld aktiv ? Dann roten Rahmen drum
        if (nFeldAktiv >= 0) {
            g.setColor(GenericColor.red);
            GenericPoint pTemp = GetCurrentXY(nFeldAktiv);
            g.drawRect(pTemp.x + mainFrame.scrollx, pTemp.y + mainFrame.scrolly, 145, 75);
        }

        if (nFeldAktiv >= 0) {
            oFeldAktiv = nFeldAktiv;
        }

        // Menuitem abdunkeln, wenn Maus weg
        switch (olditem) {
            case 0:
                break;
            case 1:
                g.drawImage(inactiveMenu, pLO.x + 415 + mainFrame.scrollx, pLO.y + 331 + mainFrame.scrolly, null);
                break;
            case 2:
                g.drawImage(DPfeill, 119 + mainFrame.scrollx, 349 + mainFrame.scrolly, null);
                break;
            case 3:
                g.drawImage(DPfeilr, 279 + mainFrame.scrollx, 348 + mainFrame.scrolly, null);
                break;
            default:
                System.out.println("Wrong inv - menuitem to clear!");
        }

        olditem = 0;  // anders loesen! - too many repaints

        // Menuitem highlighten , wenn maus darueber
        switch (menuitem) {
            case 0:
                break;
            case 1:
                g.drawImage(activeMenu, pLO.x + 416 + mainFrame.scrollx, pLO.y + 333 + mainFrame.scrolly, observer);
                break;
            case 2:
                g.drawImage(Pfeill, 121 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                break;
            case 3:
                g.drawImage(Pfeilr, 280 + mainFrame.scrollx, 350 + mainFrame.scrolly, observer);
                break;
            default:
                System.out.println("Wrong inventar-menuitem!!!");
        }

        if (menuitem != 0) {
            olditem = menuitem;
        }

        // Textausgabe, falls noetig
        if (outputText != "") {
            mainFrame.ifont.drawString(g, outputText, outputTextPos.x, outputTextPos.y, 1);
        }

        // Talkcount herunterzaehlen fuer Anzeige
        if (mainFrame.talkCount > 0) {
            mainFrame.talkCount--;
            // System.out.println(mainFrame.talkCount);
            if (mainFrame.talkCount != 0) {
                return;
            } else {
                mainFrame.Clipset = false;
                outputText = "";
                mainFrame.repaint();
                return;
            }
        }

        // Gibt es was zu tun ?
        if (nextActionID != 0) {
            DoAction();
        }
    }

    private GenericImage GetIconImage(int nGegID) {
        // hier Caching - Routine

        // Suchen, ob Inventarstueck in Cache gespeichert
        for (int i = 1; i <= INVENTAR_CACHE; i++) {
            if (nGegID == InventarID[i]) {
                return InventarStuecke[i];
            }
        }

        // Gegenstand ist nicht im Cache, also neu holen
        GenericImage Temp = IconImages(nGegID);
        InventarStuecke[CachePosition] = Temp;
        CachePosition++;
        if (CachePosition == 11) {
            CachePosition = 1;
        }

        return Temp;
    }

    // Mouse-Auswertung dieser Location ///////////////////////////////////////

    public void evalMouseEvent(GenericMouseEvent e) {
        // Talkcount - Schleife beenden und Text loeschen
        mainFrame.talkCount = 0;
        mainFrame.Clipset = false;
        outputText = "";
        mainFrame.repaint();
        GenericPoint pTemp = e.getPoint();

        // Kursor = Sprite -> anders auf MouseEvents reagieren
        if (mainFrame.invCursor) {
            if (e.isLeftClick()) {
                // linke Maustaste gedrueckt
                if (brPfeill.IsPointInRect(pTemp)) {
                    // bei Pfeil links verlassen bzw auf 1. Screen zurueck
                    if (!secScreenActive) {
                        Deactivate();
                    } else {
                        secScreenActive = false;
                        mainFrame.Clipset = false;
                        menuitem = 0;
                        olditem = 0;
                        nFeldAktiv = -1;
                        oFeldAktiv = -1;
                    }
                    mainFrame.repaint();
                    return;
                }

                if (brPfeilr.IsPointInRect(pTemp) && !secScreenActive && secScreenAvail) {
                    // bei Pfeil nach rechts 2. Screen anzeigen
                    secScreenActive = true;
                    mainFrame.Clipset = false;
                    menuitem = 0;
                    olditem = 0;
                    nFeldAktiv = -1;
                    oFeldAktiv = -1;
                    mainFrame.repaint();
                    return;
                }

                // Kombinationen abfragen und erledigen
                int x = GetMouseRect(pTemp);
                if (x != -1) {
                    int xx = vInventory.elementAt(x);

                    // Kombination Kij + Lajna = Wuda
                    if (evalKombination(2, 6, 7, xx)) {
                        return;
                    }

                    // Kombination Wuda + Hocka = W + H
                    if (evalKombination(5, 7, 9, xx)) {
                        return;
                    }

                    // Kombination W + H + Wacki = W + H + W
                    if (evalKombination(8, 9, 10, xx)) {
                        return;
                    }

                    // Kombination W + H + W + Dryba = W + H + D + Wacki extra !!!!
                    if (evalKombination(10, 13, 11, xx)) {
                        vInventory.addElement(8);
                        return;
                    }

                    // Kombination W + H + Dryba = W + H + D
                    if (evalKombination(9, 13, 11, xx)) {
                        return;
                    }

                    // Kombination W + H + D + Wacki = W + H + W + Dryba extra
                    if (evalKombination(11, 8, 10, xx)) {
                        vInventory.addElement(13);
                        return;
                    }

                    // Kombination Kamusk + Rohodz = Bron + Kamuski -> Extrawurst in evalKombination
                    if (evalKombination(12, 17, 18, xx)) {
                        return;
                    }

                    // Kombination Enterhaken + Seil = ultimatives Klettertool
                    if (evalKombination(37, 38, 39, xx)) {
                        return;
                    }

                    // Wenn auf die jeweilige leere Zelle geklickt, dann ablegen
                    if (mainFrame.whatItem == xx) {
                        mainFrame.invCursor = false;
                        mainFrame.Clipset = false;
                        mainFrame.repaint();
                        evalMouseMoveEvent(mainFrame.Mousepoint);
                        return;
                    }

                    // nix von Alledem, also muessen die Ausreden ran

                    // wacki + hornck bloto
                    if (evalAusrede(8, 16, 110, xx, x)) {
                        return;
                    }

                    // ryba + hornck bloto
                    if (evalAusrede(14, 16, 115, xx, x)) {
                        return;
                    }

                    // karta + kamuski
                    if (evalAusrede(12, 20, 120, xx, x)) {
                        return;
                    }

                    // deska + kamuski
                    if (evalAusrede(12, 3, 125, xx, x)) {
                        return;
                    }

                    // bron + kamuski
                    if (evalAusrede(12, 18, 130, xx, x)) {
                        return;
                    }

                    // pjerjo + ryba
                    if (evalAusrede(14, 19, 135, xx, x)) {
                        return;
                    }

                    // pjerjo + hornck bloto
                    if (evalAusrede(16, 19, 140, xx, x)) {
                        return;
                    }

                    // flejta + kamuski
                    if (evalAusrede(1, 12, 145, xx, x)) {
                        return;
                    }

                    // kij + kamuski
                    if (evalAusrede(2, 12, 150, xx, x)) {
                        return;
                    }

                    // flejta + dryba
                    if (evalAusrede(1, 13, 155, xx, x)) {
                        return;
                    }

                    // hocka + dryba
                    if (evalAusrede(5, 13, 160, xx, x)) {
                        return;
                    }

                    // wuda + dryba
                    if (evalAusrede(7, 13, 165, xx, x)) {
                        return;
                    }

                    // wacki + dryba
                    if (evalAusrede(8, 13, 170, xx, x)) {
                        return;
                    }

                    // kamuski + dryba
                    if (evalAusrede(12, 13, 175, xx, x)) {
                        return;
                    }

                    // wacki + ryba
                    if (evalAusrede(8, 14, 180, xx, x)) {
                        return;
                    }

                    // wuda hocka + ryba
                    if (evalAusrede(9, 14, 185, xx, x)) {
                        return;
                    }

                    // wuda wacki + ryba
                    if (evalAusrede(10, 14, 190, xx, x)) {
                        return;
                    }

                    // wuda dryba + ryba
                    if (evalAusrede(11, 14, 195, xx, x)) {
                        return;
                    }

                    // kij + wacki
                    if (evalAusrede(2, 8, 200, xx, x)) {
                        return;
                    }

                    // hornck + wacki
                    if (evalAusrede(4, 8, 205, xx, x)) {
                        return;
                    }

                    // hocka + wacki
                    if (evalAusrede(5, 8, 210, xx, x)) {
                        return;
                    }

                    // wuda + wacki
                    if (evalAusrede(7, 8, 215, xx, x)) {
                        return;
                    }

                    // hocka + lajna
                    if (evalAusrede(5, 6, 220, xx, x)) {
                        return;
                    }

                    // Teil 3

                    // list + lajna, kotwica a lajna, sluz drasta, kotwica -> lassen wir so, sieht schlimm genug aus
                    if (mainFrame.whatItem == 30 && (xx == 38 || xx == 39 || xx == 41 || xx == 37) ||
                            (mainFrame.whatItem == 38 || mainFrame.whatItem == 39 || mainFrame.whatItem == 41 || mainFrame.whatItem == 37) && xx == 30) {
                        evalYPos(x);
                        nextActionID = 225;
                        mainFrame.repaint();
                        return;
                    }

                    // list + 5 tolerow
                    if (evalAusrede(30, 40, 230, xx, x)) {
                        return;
                    }

                    // 5 tolerow + drasta / sluz drasta -> lassen
                    if (mainFrame.whatItem == 40 && (xx == 53 || xx == 41) || (mainFrame.whatItem == 53 || mainFrame.whatItem == 41) && xx == 40) {
                        evalYPos(x);
                        nextActionID = 240;
                        mainFrame.repaint();
                        return;
                    }

                    // hamor + metall
                    if (evalAusrede(46, 48, 255, xx, x)) {
                        return;
                    }

                    // Teil 4

                    // Syno auf Lichtschaale
                    if (evalAusrede(63, 60, 270, xx, x)) {
                        return;
                    }

                    // Feuersteine auf Syno
                    if (evalAusrede(12, 63, 271, xx, x)) {
                        return;
                    }

                    // Feuersteine auf Koraktor
                    if (evalAusrede(12, 61, 272, xx, x)) {
                        return;
                    }

                    // hier ist die Standardausrede, wenn alles andere versagt hat

                    // TexthoehenAnfang berechnen
                    evalYPos(x);

                    // NextActionID der Standardausrede
                    nextActionID = 100;
                    mainFrame.repaint();
                }
            } else {
                // rechte Maustaste gedrueckt
                // Gegenstand wird wieder abgelegt (grundsaetzlich!)
                mainFrame.invCursor = false;
                mainFrame.Clipset = false;
                mainFrame.repaint();
                evalMouseMoveEvent(mainFrame.Mousepoint);
            }
        }

        // Kursor ist normal, default - Reaktion
        else {

            // ActionID initialisieren
            nextActionID = 0;

            if (e.isLeftClick()) {
                // linke Maustaste

                // Click ausserhalb Inventarfenster beendet Inventar
                if (!brGesamt.IsPointInRect(pTemp)) {
                    Deactivate();
                    mainFrame.repaint();
                    return;
                }

                // ins Hauptmenue verzweigen
                if (brMenu.IsPointInRect(pTemp) && !secScreenActive) {
                    Deactivate();
                    mainFrame.whatScreen = 2;
                    mainFrame.repaint();
                    return;
                }

                // bei Pfeil links beenden oder auf 1. Screen zurueck
                if (brPfeill.IsPointInRect(pTemp)) {
                    if (!secScreenActive) {
                        Deactivate();
                    } else {
                        secScreenActive = false;
                        mainFrame.Clipset = false;
                        menuitem = 0;
                        olditem = 0;
                        nFeldAktiv = -1;
                        oFeldAktiv = -1;
                    }
                    mainFrame.repaint();
                    return;
                }

                // 2. Screen bei rechtem Pfeil anzeigen
                if (brPfeilr.IsPointInRect(pTemp) && !secScreenActive && secScreenAvail) {
                    secScreenActive = true;
                    mainFrame.Clipset = false;
                    menuitem = 0;
                    olditem = 0;
                    nFeldAktiv = -1;
                    oFeldAktiv = -1;
                    mainFrame.repaint();
                    return;
                }

                // jeweilige Beschreibung zum Inventar
                int x = GetMouseRect(pTemp);
                if (x > -1) {
                    // TexthoehenAnfang berechnen
                    evalYPos(x);

                    // NextActionID des jeweiligen Gegenstandes initialisieren
                    nextActionID = vInventory.elementAt(x);
                    mainFrame.repaint();
                }
            } else {

                // ----PARSER_DISABLE----

                // rechte Maustaste
                int x = GetMouseRect(pTemp);
                if (x > -1) {
                    mainFrame.Cinventar = GenericToolkit.getDefaultToolkit().createCustomCursor
                            (GetCursorImage(vInventory.elementAt(x)),
                                    HotSpot, "Inv");
                    mainFrame.CHinventar = GenericToolkit.getDefaultToolkit().createCustomCursor
                            (GetCursorHighImage(vInventory.elementAt(x)),
                                    HotSpot, "HInv");
                    mainFrame.invCursor = true;
                    mainFrame.whatItem = vInventory.elementAt(x);

                    // ----PARSER_ENABLE----

                    // hier fuer den Sound eine Unterscheidung, ob bei nativem Player
                    // der Soundbackground wegfallen soll oder nicht
                    noBackgroundSound = mainFrame.whatItem == 1 || mainFrame.whatItem == 12 ||
                            mainFrame.whatItem == 17 || mainFrame.whatItem == 51; // Floete, kamuski, Rohodz, halber Wosusk

                    mainFrame.Clipset = false;
                    mainFrame.repaint();
                    evalMouseMoveEvent(mainFrame.Mousepoint);
                }
            }

            // Neuzeichnen, falls etwas zu tun ist
            if (nextActionID != 0) {
                mainFrame.repaint();
            }
        }
    }

    // Routinen, die das MouseEvent entlasten

    // Kombinationen erledigen
    private boolean evalKombination(int erstesItem, int zweitesItem, int neuesItem, int xx) {
        if (mainFrame.whatItem == erstesItem && xx == zweitesItem || mainFrame.whatItem == zweitesItem && xx == erstesItem) {
            vInventory.insertElementAt(neuesItem, GetVektorPos(erstesItem, zweitesItem));

            // Extrawurst fuer die Steine, werden bei Kombination mit dem Schilf nicht geloescht
            if (erstesItem != 12) {
                vInventory.removeElement(erstesItem);
            }
            if (zweitesItem != 12) {
                vInventory.removeElement(zweitesItem);
            }

            mainFrame.invCursor = false;
            mainFrame.invHighCursor = false;
            mainFrame.Clipset = false;
            mainFrame.repaint();
            return true;
        } else {
            return false;
        }
    }

    // Ausreden anzeigen
    private boolean evalAusrede(int erstesItem, int zweitesItem, int AusredenID, int xx, int x) {
        if (mainFrame.whatItem == erstesItem && xx == zweitesItem || mainFrame.whatItem == zweitesItem && xx == erstesItem) {
            evalYPos(x);
            nextActionID = AusredenID;
            mainFrame.repaint();
            return true;
        } else {
            return false;
        }
    }

    // Berechnet zum uebergebenen Rectangle die y - Position des Textes, damit diese nicht unter Cursor
    private void evalYPos(int x) {
        // Textposition festlegen, damit Text nicht ueber Inventarstueck erscheint
        int offsett = 20;

        int dies = x;
        if (dies > 8) {
            dies -= 9;
        }

        // Inventarstueck in oberer Reihe, also Text in 2. Reihe
        if (dies < 3) {
            ytemp = 169 + offsett;
        }

        // Inventarstueck in mittlerer Reihe, also Text in 1. Reihe
        if (dies > 2 && dies < 6) {
            ytemp = 88 + offsett;
        }

        // Inventarstueck in unterer Reihe, also Text in 2. Reihe
        if (dies > 5) {
            ytemp = 169 + offsett;
        }

        // 1. Reihe waere 88
        // 3. Reihe waere 250
    }

    /// ///////////////////////////////Mousemove ///////////////////////////////////////
    public void evalMouseMoveEvent(GenericPoint pTemp) {
        // Kursor = Sprite -> andere Reaktion!!
        if (mainFrame.invCursor) {
            // hier kommt Routine hin, die erkennt, ob gehighlighted wird

            int invTemp = GetMouseRect(pTemp);

            if (invTemp > -1) {
                if (vInventory.elementAt(invTemp) != mainFrame.whatItem) {
                    mainFrame.invHighCursor = true;
                }
            } else {
                mainFrame.invHighCursor = false;
            }

            // bei Inventarcursor diesen setzen
            if (Cursorform != 10 && !mainFrame.invHighCursor) {
                Cursorform = 10;
                mainFrame.setCursor(mainFrame.Cinventar);
            }

            // bei Inventarcursor mit Highlight diesen setzen
            if (Cursorform != 11 && mainFrame.invHighCursor) {
                Cursorform = 11;
                mainFrame.setCursor(mainFrame.CHinventar);
            }

            // Beenden ,wenn mit Inventarstueck Screen verlassen
            if (!brGesamt.IsPointInRect(pTemp)) {
                Deactivate();
                mainFrame.repaint();
                return;
            }

            // Roten Rand um Inventarstuecke berechnen
            nFeldAktiv = GetMouseRect(pTemp);

            // Menu - Hilight berechnen
            menuitem = 0;
            if (brPfeill.IsPointInRect(pTemp)) {
                menuitem = 2;
            }
            if (brPfeilr.IsPointInRect(pTemp) &&
                    secScreenAvail && !secScreenActive) {
                menuitem = 3;
            }

            // Nur Neuzeichnen, wenn sich etwas geaendert hat
            if (Paintcall) {
                Paintcall = false;
                return;
            }
            if (olditem != menuitem || oFeldAktiv != nFeldAktiv) {
                // Talkcount - Schleife beenden
                mainFrame.talkCount = 0;

                mainFrame.Clipset = false; // ist wegen Text loeschen n�tig !!!!!
                outputText = "";
                mainFrame.repaint();
            }
        }

        // Kursor normal, default - Reaktion
        else {
            // Standard - Cursor setzen
            if (Cursorform != 0) {
                Cursorform = 0;
                mainFrame.setCursor(mainFrame.Normal);
            }

            // Roten rand berechnen
            nFeldAktiv = GetMouseRect(pTemp);

            menuitem = 0;
            if (brMenu.IsPointInRect(pTemp) && !secScreenActive) {
                menuitem = 1;
            }
            if (brPfeill.IsPointInRect(pTemp)) {
                menuitem = 2;
            }
            if (brPfeilr.IsPointInRect(pTemp) &&
                    secScreenAvail && !secScreenActive) {
                menuitem = 3;
            }

            // wenn noetig, dann repaint
            if (Paintcall) {
                Paintcall = false;
                return;
            }
            if (olditem != menuitem || oFeldAktiv != nFeldAktiv) {
                // Talkcount - Schleife beenden
                mainFrame.talkCount = 0;

                mainFrame.Clipset = false; // ist wegen Text loeschen noetig !!!!!
                outputText = "";
                // System.out.println(olditem + " " + menuitem + " " + oFeldAktiv + " " + nFeldAktiv);
                mainFrame.repaint();
            }
        }
    }

    public void evalMouseExitEvent(GenericMouseEvent e) {
        menuitem = 0;
        nFeldAktiv = -1;
        mainFrame.repaint();
    }

    // Key - Auswertung dieser Location /////////////////////////////////

    public void evalKeyEvent(GenericKeyEvent e) {
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();
        if (Taste == GenericKeyEvent.VK_ESCAPE) {
            Deactivate();
            mainFrame.repaint();
        }
    }


    // Inventar - deaktivieren - Routine//////////////////////////////////////
    public void Deactivate() {
        mainFrame.Clipset = false;
        menuitem = 0;
        nFeldAktiv = -1;
        mainFrame.whatScreen = 0;
        secScreenActive = false;
        mainFrame.invHighCursor = false;
    }

    // Berechnungsroutine Inventarfensternummer - X/Y-Koordinaten//////////////
    private Borderrect GetCurrentRect(int Number) {
        GenericPoint Pleftup = new GenericPoint(GetCurrentXY(Number));
        return new Borderrect(Pleftup.x, Pleftup.y, Pleftup.x + 145, Pleftup.y + 75);
    }

    // 2.Screen wird beachtet - X/Y Koordinaten stimmen
    private GenericPoint GetCurrentXY(int Numb) {
        GenericPoint Pleftup = new GenericPoint();
        int Number = Numb;
        if (secScreenActive) {
            Number = Numb - 9;
        }
        Pleftup.x = 95 + Number % 3 * 151;
        Pleftup.y = 76 + Number / 3 * 81;
        return Pleftup;
    }  

    /*  public boolean isInventory(int which)
	{
	boolean ret = false;
	int nAnzahl = vInventory.size ();
	for (int i = 0; i < nAnzahl; i++)
	{
	int iTemp = ((Integer) vInventory.elementAt (i)).intValue();
	if (iTemp == which) ret = true;
	}
	return ret;
	}*/

    // gibt Inventarposition zurueck, wo Maus drueber ( = Vectorposition im Inventarvektor)
    private int GetMouseRect(GenericPoint Mp) {
        int nAnzahl = vInventory.size();
        int giveback = -1;
        if (!secScreenActive) {
            if (nAnzahl > 9) {
                nAnzahl = 9;
            }
            for (int i = 0; i < nAnzahl; i++) {
                if (GetCurrentRect(i).IsPointInRect(Mp)) {
                    giveback = i;
                }
            }
        } else {
            for (int i = 9; i < nAnzahl; i++) {
                if (GetCurrentRect(i).IsPointInRect(Mp)) {
                    giveback = i;
                }
            }
        }
        return giveback;
    }

    // Routine, die feststellt, wo die beiden Objekte im Vektor liegen und kleineren zurueckgibt
    private int GetVektorPos(int first, int second) {
        for (int i = 0; i < vInventory.size(); i++) {
            if (vInventory.elementAt(i) == first || vInventory.elementAt(i) == second) {
                return i;
            }
        }
        return -1;
    }

    // Routine, die die allgemeinen Sachen der Textausgabe regelt
    private void ShowText(String HText, String DText, String NText) {
        if (mainFrame.sprache == 1) {
            outputText = HText;
        }
        if (mainFrame.sprache == 2) {
            outputText = DText;
        }
        if (mainFrame.sprache == 3) {
            outputText = NText;
        }
        outputTextPos = mainFrame.ifont.CenterText(outputText, new GenericPoint(320 + mainFrame.scrollx, ytemp));
        mainFrame.repaint();
    }

    // Aktionen dieser Location ////////////////////////////////////////

    private void DoAction() {
        // Was soll getan werden ?
        switch (nextActionID) {
            case 1:
                // Pfeife anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00012"),
                        Start.stringManager.getTranslation("Main_Inventar_00013"),
                        Start.stringManager.getTranslation("Main_Inventar_00014"));
                break;

            case 2:
                // Stock anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00015"),
                        Start.stringManager.getTranslation("Main_Inventar_00016"),
                        Start.stringManager.getTranslation("Main_Inventar_00017"));
                break;

            case 3:
                // Schild (deska) anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00018"),
                        Start.stringManager.getTranslation("Main_Inventar_00019"),
                        Start.stringManager.getTranslation("Main_Inventar_00020"));
                break;

            case 4:
                // Honck anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00021"),
                        Start.stringManager.getTranslation("Main_Inventar_00022"),
                        Start.stringManager.getTranslation("Main_Inventar_00023"));
                break;

            case 5:
                // Hocka  anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00024"),
                        Start.stringManager.getTranslation("Main_Inventar_00025"),
                        Start.stringManager.getTranslation("Main_Inventar_00026"));
                break;

            case 6:
                // Lajna anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00027"),
                        Start.stringManager.getTranslation("Main_Inventar_00028"),
                        Start.stringManager.getTranslation("Main_Inventar_00029"));
                break;

            case 7:
                // Wuda anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00030"),
                        Start.stringManager.getTranslation("Main_Inventar_00031"),
                        Start.stringManager.getTranslation("Main_Inventar_00032"));
                break;

            case 8:
                // Wacki anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00033"),
                        Start.stringManager.getTranslation("Main_Inventar_00034"),
                        Start.stringManager.getTranslation("Main_Inventar_00035"));
                break;

            case 9:
                // Wuda + Hocka anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00036"),
                        Start.stringManager.getTranslation("Main_Inventar_00037"),
                        Start.stringManager.getTranslation("Main_Inventar_00038"));
                break;

            case 10:
                // Wuda + Hocka + Wacka anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00039"),
                        Start.stringManager.getTranslation("Main_Inventar_00040"),
                        Start.stringManager.getTranslation("Main_Inventar_00041"));
                break;

            case 11:
                // Wuda + Hocka + Drjewjana Ryba anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00042"),
                        Start.stringManager.getTranslation("Main_Inventar_00043"),
                        Start.stringManager.getTranslation("Main_Inventar_00044"));
                break;

            case 12:
                // Wohnjowe kamuski anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00045"),
                        Start.stringManager.getTranslation("Main_Inventar_00046"),
                        Start.stringManager.getTranslation("Main_Inventar_00047"));
                break;

            case 13:
                // Drjewjana Ryba anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00048"),
                        Start.stringManager.getTranslation("Main_Inventar_00049"),
                        Start.stringManager.getTranslation("Main_Inventar_00050"));
                break;

            case 14:
                // Ryba anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00051"),
                        Start.stringManager.getTranslation("Main_Inventar_00052"),
                        Start.stringManager.getTranslation("Main_Inventar_00053"));
                break;

            case 15:
                // Krosik anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00054"),
                        Start.stringManager.getTranslation("Main_Inventar_00055"),
                        Start.stringManager.getTranslation("Main_Inventar_00056"));
                break;

            case 16:
                // Honck z blotom anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00057"),
                        Start.stringManager.getTranslation("Main_Inventar_00058"),
                        Start.stringManager.getTranslation("Main_Inventar_00059"));
                break;

            case 17:
                // Rohodz anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00060"),
                        Start.stringManager.getTranslation("Main_Inventar_00061"),
                        Start.stringManager.getTranslation("Main_Inventar_00062"));
                break;

            case 18:
                // Rohodz + Kamusk anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00063"),
                        Start.stringManager.getTranslation("Main_Inventar_00064"),
                        Start.stringManager.getTranslation("Main_Inventar_00065"));
                break;

            case 19:
                // Pjero anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00066"),
                        Start.stringManager.getTranslation("Main_Inventar_00067"),
                        Start.stringManager.getTranslation("Main_Inventar_00068"));
                break;

            case 20:
                // Karta anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00069"),
                        Start.stringManager.getTranslation("Main_Inventar_00070"),
                        Start.stringManager.getTranslation("Main_Inventar_00071"));
                break;


            // Dresdner INVENTAR //////////////////////////////////////////

            case 30:
                // List Zahrodnika anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00072"),
                        Start.stringManager.getTranslation("Main_Inventar_00073"),
                        Start.stringManager.getTranslation("Main_Inventar_00074"));
                break;

            case 31:
                // dowolnosc1 anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00075"),
                        Start.stringManager.getTranslation("Main_Inventar_00076"),
                        Start.stringManager.getTranslation("Main_Inventar_00077"));
                break;

            case 32:
                // dowolnosc3 anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00078"),
                        Start.stringManager.getTranslation("Main_Inventar_00079"),
                        Start.stringManager.getTranslation("Main_Inventar_00080"));
                break;

            case 33:
                // dowolnosc2 anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00081"),
                        Start.stringManager.getTranslation("Main_Inventar_00082"),
                        Start.stringManager.getTranslation("Main_Inventar_00083"));
                break;

            case 34:
                // dowolnosc4 anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00084"),
                        Start.stringManager.getTranslation("Main_Inventar_00085"),
                        Start.stringManager.getTranslation("Main_Inventar_00086"));
                break;

            case 35:
                // Drjewo anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00087"),
                        Start.stringManager.getTranslation("Main_Inventar_00088"),
                        Start.stringManager.getTranslation("Main_Inventar_00089"));
                break;

            case 36:
                // Tigerowy kozuch anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00090"),
                        Start.stringManager.getTranslation("Main_Inventar_00091"),
                        Start.stringManager.getTranslation("Main_Inventar_00092"));
                break;

            case 37:
                // Kotwica anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00093"),
                        Start.stringManager.getTranslation("Main_Inventar_00094"),
                        Start.stringManager.getTranslation("Main_Inventar_00095"));
                break;

            case 38:
                // Powjaz anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00096"),
                        Start.stringManager.getTranslation("Main_Inventar_00097"),
                        Start.stringManager.getTranslation("Main_Inventar_00098"));
                break;

            case 39:
                // Kotwica + Powjaz anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00099"),
                        Start.stringManager.getTranslation("Main_Inventar_00100"),
                        Start.stringManager.getTranslation("Main_Inventar_00101"));
                break;

            case 40:
                // Tolery anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00102"),
                        Start.stringManager.getTranslation("Main_Inventar_00103"),
                        Start.stringManager.getTranslation("Main_Inventar_00104"));
                break;

            case 41:
                // Sluzowna Drasta anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00105"),
                        Start.stringManager.getTranslation("Main_Inventar_00106"),
                        Start.stringManager.getTranslation("Main_Inventar_00107"));
                break;

            case 42:
                // Helbija anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00108"),
                        Start.stringManager.getTranslation("Main_Inventar_00109"),
                        Start.stringManager.getTranslation("Main_Inventar_00110"));
                break;

            case 43:
                // Helm anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00111"),
                        Start.stringManager.getTranslation("Main_Inventar_00112"),
                        Start.stringManager.getTranslation("Main_Inventar_00113"));
                break;

            case 44:
                // Helm mit Wein anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00114"),
                        Start.stringManager.getTranslation("Main_Inventar_00115"),
                        Start.stringManager.getTranslation("Main_Inventar_00116"));
                break;

            case 45:
                // Sonnenuhr anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00117"),
                        Start.stringManager.getTranslation("Main_Inventar_00118"),
                        Start.stringManager.getTranslation("Main_Inventar_00119"));
                break;

            case 46:
                // Hammer anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00120"),
                        Start.stringManager.getTranslation("Main_Inventar_00121"),
                        Start.stringManager.getTranslation("Main_Inventar_00122"));
                break;

            case 47:
                // Schluessel anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00123"),
                        Start.stringManager.getTranslation("Main_Inventar_00124"),
                        Start.stringManager.getTranslation("Main_Inventar_00125"));
                break;

            case 48:
                // Metall anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00126"),
                        Start.stringManager.getTranslation("Main_Inventar_00127"),
                        Start.stringManager.getTranslation("Main_Inventar_00128"));
                break;

            case 49:
                // Befehl anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00129"),
                        Start.stringManager.getTranslation("Main_Inventar_00130"),
                        Start.stringManager.getTranslation("Main_Inventar_00131"));
                break;

            case 50:
                // Skizze anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00132"),
                        Start.stringManager.getTranslation("Main_Inventar_00133"),
                        Start.stringManager.getTranslation("Main_Inventar_00134"));
                break;

            case 51:
                // Halben Stollen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00135"),
                        Start.stringManager.getTranslation("Main_Inventar_00136"),
                        Start.stringManager.getTranslation("Main_Inventar_00137"));
                break;

            case 52:
                // Stollen anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00138"),
                        Start.stringManager.getTranslation("Main_Inventar_00139"),
                        Start.stringManager.getTranslation("Main_Inventar_00140"));
                break;

            case 53:
                // Drasta anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00141"),
                        Start.stringManager.getTranslation("Main_Inventar_00142"),
                        Start.stringManager.getTranslation("Main_Inventar_00143"));
                break;

            case 54:
                // Karte in Dresden anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00144"),
                        Start.stringManager.getTranslation("Main_Inventar_00145"),
                        Start.stringManager.getTranslation("Main_Inventar_00146"));
                break;

            case 55:
                // schweres Buch anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00147"),
                        Start.stringManager.getTranslation("Main_Inventar_00148"),
                        Start.stringManager.getTranslation("Main_Inventar_00149"));
                break;

            case 60:
                // Schuessel anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00150"),
                        Start.stringManager.getTranslation("Main_Inventar_00151"),
                        Start.stringManager.getTranslation("Main_Inventar_00152"));
                break;

            case 61:
                // Koraktor anschauen
                // ShowText ("Koraktor - sk#on#knje sym knihu$mojich prjedownikow namaka#l!",
                //	  "Koraktor - sko#ncnje som knig#ly$mojich pr#edownikow namaka#l!!",
                //	  "Der Koraktor -$endlich habe ich ihn gefunden!");
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00153"),
                        Start.stringManager.getTranslation("Main_Inventar_00154"),
                        Start.stringManager.getTranslation("Main_Inventar_00155"));
                break;

            case 62:
                // Grossen Stein anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00156"),
                        Start.stringManager.getTranslation("Main_Inventar_00157"),
                        Start.stringManager.getTranslation("Main_Inventar_00158"));
                break;

            case 63:
                // Syno anschauen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00159"),
                        Start.stringManager.getTranslation("Main_Inventar_00160"),
                        Start.stringManager.getTranslation("Main_Inventar_00161"));
                break;

            case 100:
                // Standardausreden
                int zuffZahl = (int) Math.round(Math.random() * HAusreden.length);
                if (zuffZahl == HAusreden.length) {
                    zuffZahl = 0;
                }
                ShowText(Start.stringManager.getTranslation(HAusreden[zuffZahl]),
                        Start.stringManager.getTranslation(DAusreden[zuffZahl]),
                        Start.stringManager.getTranslation(NAusreden[zuffZahl]));
                break;

            case 110:
                // wacki auf hornck bloto
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00162"),
                        Start.stringManager.getTranslation("Main_Inventar_00163"),
                        Start.stringManager.getTranslation("Main_Inventar_00164"));
                break;

            case 115:
                // ryba auf hornck bloto
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00165"),
                        Start.stringManager.getTranslation("Main_Inventar_00166"),
                        Start.stringManager.getTranslation("Main_Inventar_00167"));
                break;

            case 120:
                // karta auf kamuski
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00168"),
                        Start.stringManager.getTranslation("Main_Inventar_00169"),
                        Start.stringManager.getTranslation("Main_Inventar_00170"));
                break;

            case 125:
                // deska auf kamuski
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00171"),
                        Start.stringManager.getTranslation("Main_Inventar_00172"),
                        Start.stringManager.getTranslation("Main_Inventar_00173"));
                break;

            case 130:
                // bron auf kamuski
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00174"),
                        Start.stringManager.getTranslation("Main_Inventar_00175"),
                        Start.stringManager.getTranslation("Main_Inventar_00176"));
                break;

            case 135:
                // pjerjo auf ryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00177"),
                        Start.stringManager.getTranslation("Main_Inventar_00178"),
                        Start.stringManager.getTranslation("Main_Inventar_00179"));
                break;

            case 140:
                // pjerjo auf hornck bloto
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00180"),
                        Start.stringManager.getTranslation("Main_Inventar_00181"),
                        Start.stringManager.getTranslation("Main_Inventar_00182"));
                break;

            case 145:
                // flejta auf kamuski
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00183"),
                        Start.stringManager.getTranslation("Main_Inventar_00184"),
                        Start.stringManager.getTranslation("Main_Inventar_00185"));
                break;

            case 150:
                // kij auf kamuski
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00186"),
                        Start.stringManager.getTranslation("Main_Inventar_00187"),
                        Start.stringManager.getTranslation("Main_Inventar_00188"));
                break;

            case 155:
                // flejta auf dryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00189"),
                        Start.stringManager.getTranslation("Main_Inventar_00190"),
                        Start.stringManager.getTranslation("Main_Inventar_00191"));
                break;

            case 160:
                // hocka auf dryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00192"),
                        Start.stringManager.getTranslation("Main_Inventar_00193"),
                        Start.stringManager.getTranslation("Main_Inventar_00194"));
                break;

            case 165:
                // wuda auf dryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00195"),
                        Start.stringManager.getTranslation("Main_Inventar_00196"),
                        Start.stringManager.getTranslation("Main_Inventar_00197"));
                break;

            case 170:
                // wacki auf dryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00198"),
                        Start.stringManager.getTranslation("Main_Inventar_00199"),
                        Start.stringManager.getTranslation("Main_Inventar_00200"));
                break;

            case 175:
                // kamuski auf dryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00201"),
                        Start.stringManager.getTranslation("Main_Inventar_00202"),
                        Start.stringManager.getTranslation("Main_Inventar_00203"));
                break;

            case 180:
                // wacki auf ryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00204"),
                        Start.stringManager.getTranslation("Main_Inventar_00205"),
                        Start.stringManager.getTranslation("Main_Inventar_00206"));
                break;

            case 185:
                // wuda + hocka auf ryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00207"),
                        Start.stringManager.getTranslation("Main_Inventar_00208"),
                        Start.stringManager.getTranslation("Main_Inventar_00209"));
                break;

            case 190:
                // wuda + wacki auf ryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00210"),
                        Start.stringManager.getTranslation("Main_Inventar_00211"),
                        Start.stringManager.getTranslation("Main_Inventar_00212"));
                break;

            case 195:
                // wuda + dryba auf ryba
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00213"),
                        Start.stringManager.getTranslation("Main_Inventar_00214"),
                        Start.stringManager.getTranslation("Main_Inventar_00215"));
                break;

            case 200:
                // wacki auf kij
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00216"),
                        Start.stringManager.getTranslation("Main_Inventar_00217"),
                        Start.stringManager.getTranslation("Main_Inventar_00218"));
                break;

            case 205:
                // wacki auf hornck
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00219"),
                        Start.stringManager.getTranslation("Main_Inventar_00220"),
                        Start.stringManager.getTranslation("Main_Inventar_00221"));
                break;

            case 210:
                // wacki auf  hocka
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00222"),
                        Start.stringManager.getTranslation("Main_Inventar_00223"),
                        Start.stringManager.getTranslation("Main_Inventar_00224"));
                break;

            case 215:
                // wacki auf wuda
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00225"),
                        Start.stringManager.getTranslation("Main_Inventar_00226"),
                        Start.stringManager.getTranslation("Main_Inventar_00227"));
                break;

            case 220:
                // lajna auf hocka
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00228"),
                        Start.stringManager.getTranslation("Main_Inventar_00229"),
                        Start.stringManager.getTranslation("Main_Inventar_00230"));
                break;

            //////// Dresdner Sonderausreden ///////////////////////////////////

            case 225:
                // list auf verschienden sachen
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00231"),
                        Start.stringManager.getTranslation("Main_Inventar_00232"),
                        Start.stringManager.getTranslation("Main_Inventar_00233"));
                break;

            case 230:
                // List auf 5 tolerow
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00234"),
                        Start.stringManager.getTranslation("Main_Inventar_00235"),
                        Start.stringManager.getTranslation("Main_Inventar_00236"));
                break;

            case 240:
                // 5 tolerow auf drasta / sluz drasta
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00237"),
                        Start.stringManager.getTranslation("Main_Inventar_00238"),
                        Start.stringManager.getTranslation("Main_Inventar_00239"));
                break;

            case 255:
                // hamor auf metall
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00240"),
                        Start.stringManager.getTranslation("Main_Inventar_00241"),
                        Start.stringManager.getTranslation("Main_Inventar_00242"));
                break;

            // Teil 4

            case 270:
                // Syno auf Lichtschaale
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00243"),
                        Start.stringManager.getTranslation("Main_Inventar_00244"),
                        Start.stringManager.getTranslation("Main_Inventar_00245"));
                break;

            case 271:
                // Feuersteine auf Syno
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00246"),
                        Start.stringManager.getTranslation("Main_Inventar_00247"),
                        Start.stringManager.getTranslation("Main_Inventar_00248"));
                break;

            case 272:
                // deska auf kamuski
                ShowText(Start.stringManager.getTranslation("Main_Inventar_00249"),
                        Start.stringManager.getTranslation("Main_Inventar_00250"),
                        Start.stringManager.getTranslation("Main_Inventar_00251"));
                break;

            default:
                System.out.println("Falsche Action-ID !");
        }

        nextActionID = 0;  // ID zuruecksetzen
    }
}