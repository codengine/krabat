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

package de.codengine;

import de.codengine.anims.*;
import de.codengine.locations.*;
import de.codengine.locations2.*;
import de.codengine.locations3.*;
import de.codengine.locations4.*;
import de.codengine.main.*;
import de.codengine.platform.*;
import de.codengine.sound.AbstractPlayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class Start implements Runnable {
    // private static final boolean have_png = true;

    public Thread animator;

    // Instanz des z.Z. aktiven Location-Objekts (SS)
    public Mainloc currentLocation;

    // Cursorvariablen
    public GenericCursor Cup, Cdown, Cleft, Cright, Cinventar, CHinventar, Normal, Kreuz, Warten, Nix;
    public boolean invCursor;         // Zeigt an, ob Mauscursor = Inventarstueck
    public boolean invHighCursor;     // Zeigt an, ob Mauscursor gehighlighted werden muss
    public int whatItem = 0;          // Welches Inventarstueck haengt an der Angel?
    // Ist nur im Zusammenhang mit invCursor gueltig!!!

    // Klasseninitialisierungen
    public Inventar inventory;
    public Hauptmenu mainmenu;
    public Wocinic laden;
    public Skladzic speichern;
    public Info credits;
    public Krabat krabat;
    public Imagefont ifont;
    public Wegsucher wegSucher;
    public Weggeher wegGeher;
    public Konc exit;
    public GenericSoundEffectPlayer wave;
    public Karta karte;
    public Slownik woerterbuch;
    public Skica skica;
    // public Slownikcreate woerterbuch;

    // public showrect1 showrect;  // nur fuer Debugging

    // wichtige Sachen fuer Hauptklasse
    private GenericImage offImage;
    public GenericImage saveImage;
    public GenericDrawingContext offGraphics;

    public int currLocation;                  // aktuelle Location (Index)
    public int talkCount;                     // Zaehler, wielange jemand noch spricht und ob ueberhaupt
    public boolean fPlayAnim;                 // Flag, ob Animation gespielt wird
    public boolean isWindowactive;            // Flag, ob Spiel aktiv ist
    public boolean isMousevalid;              // Flag, ob Mausposition im Fenster ist
    public boolean isAnim;                    // Flag, ob Hintergrundanimationen laufen
    public boolean isMultiple;                // Flag, ob Multiple Choice gerade aktiv ist
    private boolean isListenerActive;         // Flag, ob Listener ausgefuehrt werden oder nicht
    private boolean StopPaint = false;        // Paint - Schleife waehrend Init anhalten

    // Flags, die Override fuer Menue festlegen
    public int whatScreen;

    public int sprache; // Sprache
    private int thirdLangIndex;

    // Variablen fuer Mousemove und Doppelklick
    public GenericPoint Mousepoint = new GenericPoint(0, 0);
    public boolean dClick = false;
    // private long paintzeit = 0;  // fuer Debugging Benchmark - Anzeige

    // Scrolling - Variablen
    public int scrollx, scrolly;
    public boolean isScrolling;

    public boolean[] Actions = new boolean[1002];
    public boolean Clipset;

    // Fuer Debugging manuelles Skalieren
    public int override = 0;

    // Konstante fuer Labyrinth - Schwierigkeit
    public static final int LabyHelp = 15;


    // Redefarben, die sich ueber mehrere Locations erstrecken
    public static final int Muellerfarbe = 21;
    public static final int ErzaehlerFarbe = 13;

    private int KrabatForm = 0; // gibt an, welche KrabatKlasse aktiv ist (default ist von der Seite)

    public boolean komme_von_karte = false; // hier ein "Achtung" an den CD-Player jeder Kartenlocation,
    // dass auf jeden Fall die CD neu gestartet werden muss

    // this flag is for make sure that the paint method does nothing until it makes sense
    // otherwise there will be NPExceptions, when using FullScreenMode -- SS 2001/11/10
    boolean fPaintAllowed = false;

    public GenericImageFetcher imageFetcher;

    // public static String urlBase;

    public static AbstractPlayer player;

    private GenericImageObserver observer;

    private GenericContainer container;

    private AtomicInteger repaintCounter;

    public GenericStorageManager storageManager;

    private ArrayList<GenericPoint> hotspots;

    private GameProperties gameProperties;

    public static StringManager stringManager;

    public String thirdGameLanguage;

    protected void runGamePt1(
            int currentLanguageIndex,
            GenericImageFetcher imageFetcher,
            GenericImageObserver observer,
            GenericContainer container,
            GenericSoundEffectPlayer player,
            AbstractPlayer musicPlayer,
            GenericStorageManager storageManager) {
        Start.player = musicPlayer;
        // Start.urlBase = urlBase + "/";

        this.imageFetcher = imageFetcher;
        this.observer = observer;
        this.container = container;
        this.storageManager = storageManager;
        // Alle wichtigen Variablen zuruecksetzen
        InitGame();

        stringManager = new StringManager(storageManager);

        // Speicheranzeige laden fuer Debugging
        // new memory();

        // Variablen fuer Mausdoppelklick festlegen
        Mousepoint = new GenericPoint(0, 0);

        Hashtable<String, String> defaults = new Hashtable<String, String>();
        defaults.put(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, Integer.toString(currentLanguageIndex));
        defaults.put(GameProperties.THIRD_GAME_LANGUAGE_SELECTION, "none");
        gameProperties = new GameProperties(storageManager, defaults);
        gameProperties.loadProperties();

        // which language index is active in the game?
        int tmpLangIndex = 0;
        try {
            tmpLangIndex = Integer.parseInt(gameProperties.getProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX));
        } catch (NumberFormatException e) {
            System.out.println("Unrecognized game language index string:'"
                    + gameProperties.getProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX) + "'.");
            tmpLangIndex = 0;
        }
        if ((tmpLangIndex >= 1) && (tmpLangIndex <= 3)) {
            sprache = tmpLangIndex;
        } else {
            sprache = 1; // fallback is HS
        }

        // what is the selected "third language" (if any)
        thirdGameLanguage = gameProperties.getProperty(GameProperties.THIRD_GAME_LANGUAGE_SELECTION);

        // if no language selected yet, this will load German
        stringManager.defineThirdLanguage(LanguageSupportMapper.getLanguageFilename(thirdGameLanguage), false, "");

        System.out.println("Sprache " + sprache);
        System.out.println("Third language: " + thirdGameLanguage);

        // feststellen, ob Sound abgespielt werden darf und wie
        wave = player;

        // Stets vorhandene Klassen laden
        ifont = new Imagefont(this, observer); // Schrift
        wegSucher = new Wegsucher();      // Laufroutinen
        wegGeher = new Weggeher(this);   // Laufroutinen
        // inventory = new inventar (this, InitImages());   // Inventar

        hotspots = new ArrayList<GenericPoint>();

    }

    protected void runGamePt2(GenericPoint hotSpot, GenericCursor[] cursors) {
        inventory = new Inventar(this, hotSpot);   // Inventar

        Cup = cursors[0];
        Cdown = cursors[1];
        Cleft = cursors[2];
        Cright = cursors[3];
        Cinventar = cursors[4];
        CHinventar = cursors[5];
        Normal = cursors[6];
        Kreuz = cursors[7];
        Warten = cursors[8];
        Nix = cursors[9];

        mainmenu = new Hauptmenu(this, gameProperties);  // Hauptmenue
        exit = new Konc(this, gameProperties);       // Sicherheitsabfragen
        // showrect  = new showrect1 (this);  // Debugging - Anzeige der Laufrectangles
        krabat = (Krabat) new KrabatNormal(this);

        CheckKrabat();

        // getrenntes Laden vom Imagebild und Ausschneiden
        // ifont.init();
        System.out.println("Font is cut.");

        //unsichtbares Bild fuer Double-Buffering erzeugen

        // Java documentation says not to do this
        // super.addNotify ();

        // ConstructLocation(79);  // Zum ueberspringen von Locations

        //         System.out.println (System.getProperty ("java.home"));
        //         System.out.println (System.getProperty ("java.class.path"));
        //         System.out.println (System.getProperty ("os.name"));
        //         System.out.println (System.getProperty ("os.arch"));
        //         System.out.println (System.getProperty ("user.name"));
        //         System.out.println (System.getProperty ("user.home"));
        //         System.out.println (System.getProperty ("user.dir")); 

        repaintCounter = new AtomicInteger();

        // finally paint and run everything 
        fPaintAllowed = true;
    }

    protected void runGamePt3() {
        offImage = GenericToolkit.getDefaultToolkit().createImage(1280, 480);
        offGraphics = offImage.getGraphics();
        System.out.println("Got DB Offscreen-Image");

        if (LanguageSupportMapper.getInternalCode(thirdGameLanguage) == 0) {
            // show language chooser
            ConstructLocation(109);
        } else {
            // language set in properties, start game
            ConstructLocation(100);
        }

        start_thread();
        repaint();
        System.out.println("Well, here we go...");
    }

    // Hier wird alles fuer Neustart initialisiert
    private void InitGame() {
        // Aktionen loeschen (Krabat hat noch nichts gemacht!)
        for (int i = 0; i <= 1000; ++i) {
            Actions[i] = false;
        }
        invCursor = false;
        invHighCursor = false;
        talkCount = 0;
        fPlayAnim = false;
        isWindowactive = true;
        isMousevalid = true;
        isAnim = false;
        isMultiple = false;
        isListenerActive = false;
        whatScreen = 0;
        scrollx = 0;
        scrolly = 0;
        isScrolling = false;
        Clipset = false;
    }

    public void repaint() {
        repaintCounter.incrementAndGet();
    }

    public void setCursor(GenericCursor cursor) {
        container.setCursor(cursor);
    }

    public final synchronized GenericImage paint(GenericDrawingContext g) {
        // Interpolation aktivieren, somit werden gezoomte/verkleinerte Sprites
        // wesentlich besser dargestellt (besonders der Krabat)
        /*
    	Graphics2D g2 = (Graphics2D) offGraphics;
        g2.setRenderingHint (RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		*/

        // no paint, until it make sense (othwerwise exceptions in full screen mode)
        if (fPaintAllowed == false) {
            return null;
        }

        // System.out.println("Normalpaint !");

        // System.out.println("Time : " + (System.currentTimeMillis() - paintzeit));
        // paintzeit = System.currentTimeMillis();

        // bei aktivem Exit (Sicherheitsabfragen) Paint umleiten - Prioritaet 1 - kein Scrolling
        if (exit.active == true) {
            exit.paintExit(offGraphics);
            // g.drawImage(offImage, -scrollx, -scrolly, observer);
            return offImage;
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2 - kein Scrolling
        if (whatScreen != 0) {
            switch (whatScreen) {
                case 1:
                    inventory.paintInventory(offGraphics);
                    break;
                case 2:
                    mainmenu.paintMainmenu(offGraphics);
                    break;
                case 3:
                    laden.paintLaden(offGraphics);
                    break;
                case 4:
                    speichern.paintSpeichern(offGraphics);
                    break;
                case 5:
                    credits.paintCredits(offGraphics);
                    break;
                case 6:
                    karte.paintKarte(offGraphics);
                    break;
                case 7:
                    woerterbuch.paintSlownik(offGraphics);
                    break;
                case 8:
                    skica.paintSkizze(offGraphics);
                    break;
                default:
                    System.out.println("Wrong Paint Prio 2 !");
            }
            // g.drawImage(offImage, -scrollx, -scrolly, observer);
            return offImage;
        }

        // Scrolling berechnen und ausfuehren
        ScrollIt();

        // paint in der jeweiligen Location aufrufen - Prioritaet 3 (SS)
        if (currentLocation != null) {
            currentLocation.paintLocation(offGraphics);
        } else {
            System.out.println("Null-Painting !");
        }

        // Hier Anzeige von Mausposition und Scaling - Variable fuer manuelles Zooming

        // if (isAnim == true) offGraphics.drawString ("Animationsmodus ein !", 200, 100);


        if (offImage != null) {

            // g.drawImage (offImage, -scrollx, -scrolly, observer);
            return offImage;
        }
        // if (offImage != null) g.drawImage (offImage, -scrollx, -scrolly, 2048, 1536, this);  


        // if (isAnim == true) g.drawString ("Animationsmodus ein !", 200, 100);

        return null;
    }

    // Scrolling - Routine
    private void ScrollIt() {
        int scrollOffsetx = 0;

        // erst mal schauen, wer ueberhaupt alles scrollt...
        switch (currLocation) {
            case 6:
            case 71:
                // Doma in Teil 1 und 2
                scrollOffsetx = 5;
                break;

            case 13:
            case 87:
                // Wjes in Teil 1 und 2
                scrollOffsetx = 4;
                break;

            case 14:
            case 73:
                // Hojnt in Teil 1 und 2
                scrollOffsetx = 6;
                break;

            case 18:
                // Dubring Teil 1
                scrollOffsetx = 6;
                break;

            case 21:
            case 76:
                // Kulow in Teil 1 und 2
                scrollOffsetx = 4;
                break;

            case 91:
                // Inmlyn Teil 2
                scrollOffsetx = 9;
                break;

            case 123:
                // Hala Teil 3
                scrollOffsetx = 10;
                break;

            case 125:
                // Jewisco Teil 3
                scrollOffsetx = 10;
                break;

            case 126:
                // Murja Teil 3
                scrollOffsetx = 3;
                break;

            case 152:
                // Gang Teil 3
                scrollOffsetx = 6;
                break;

            default:
                // alle anderen Locations muessen Routine verlassen !!
                isScrolling = false;
                return;
        }

        // Hier allgemeine Scrollingroutine
        int temp = krabat.GetKrabatPos().x - scrollx;

        isScrolling = false;
        if (temp < 315) {
            scrollx -= scrollOffsetx;
            isScrolling = true;
        }
        if (temp > 325) {
            scrollx += scrollOffsetx;
            isScrolling = true;
        }
        if (scrollx < 0) {
            scrollx = 0;
            isScrolling = false;
        }
        if (scrollx > 640) {
            scrollx = 640;
            isScrolling = false;
        }

        // Cursor neu berechnen, weil Hintergrund verschoben wurde
        // aus case-bloecken entfernt (SS)
        if (currentLocation != null) {
            currentLocation.evalMouseMoveEvent(Mousepoint);
        }
    }

    // Thread starten und immer laufen lassen
    private void start_thread() {
        animator = new Thread(this);
        animator.setPriority(Thread.NORM_PRIORITY);
        animator.setDaemon(true); // thread shall not block VM termination
        animator.start();
    }

    @Override
    public void run() {
        boolean tmpTrigger;

        while (animator != null) {
            synchronized (animator) {
                tmpTrigger = repaintCounter.get() > 0;
            }

            // um Krabat kuemmern, solange er was zu tun hat
            if (((krabat.isWalking == true) || (krabat.isWandering == true) ||
                    (krabat.nAnimation != 0) || (talkCount != 0) || (fPlayAnim == true)
                    || (isScrolling == true) || (isAnim == true) || (tmpTrigger == true))
                    && (isWindowactive == true) && (StopPaint == false))
                /*if (true)*/ {

                // System.out.println("Paint counter: " + repaintCounter.get());

                // the repaint() logic might try to schedule
                // another repaint
                // therefore, make sure we do not decrement
                // a repaint request immediately again
                // and so check for decrement right here
                if (repaintCounter.get() > 0) {
                    repaintCounter.decrementAndGet();
                }

                // System.out.println("Repaint!");
                container.repaint();

                // repaint();
            } else {
                /*
                System.out.println("No repaint: ");
                System.out.print("isWindowactive=" + isWindowactive + "; ");
                System.out.print("StopPaint=" + StopPaint + "; ");
                System.out.print("fPlayAnim=" + fPlayAnim + "; ");
                System.out.print("isAnim=" + isAnim + "; ");
                
                System.out.println();
                */
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }

        }
    }

    public void mouseEntered() {
        isMousevalid = true;
    }

    public void mouseExited(GenericMouseEvent e) {
        isMousevalid = false;

        //Signal an Menuelocations, dass HiLight abgeschaltet werden soll
        if (whatScreen != 0) {
            switch (whatScreen) {
                case 1:
                    inventory.evalMouseExitEvent(e);
                    break;
                case 2:
                    mainmenu.evalMouseExitEvent(e);
                    break;
                case 3:
                    laden.evalMouseExitEvent(e);
                    break;
                case 4:
                    speichern.evalMouseExitEvent(e);
                    break;
                case 5:
                    credits.evalMouseExitEvent(e);
                    break;
                case 6:
                    karte.evalMouseExitEvent(e);
                    break;
                case 7:
                    woerterbuch.evalMouseExitEvent(e);
                    break;
                case 8:
                    skica.evalMouseExitEvent(e);
                    break;
                default:
                    System.out.println("Wrong Exitevent !");
            }
            return;
        }

        // hier wird Erigenis an die ggw. Location weitergegeben
        if (currentLocation != null) {
            currentLocation.evalMouseExitEvent(e);
        }
    }

    public synchronized final void mousePressed(GenericMouseEvent e) {
        if ((isMousevalid == false) || /*(isWindowactive == false) || */
                (isListenerActive == false)) {
            // Versuch, Event zu loeschen, wenn nicht benoetigt
            e.consume();
            return;
        }

        // apply the doubleclick detection
        dClick = e.getDoubleClick();

        // Wenn Exit aktiv, dorthin - Prio 1
        if (exit.active == true) {
            exit.evalMouseEvent(e);
            return;
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2
        if (whatScreen != 0) {
            switch (whatScreen) {
                case 1:
                    inventory.evalMouseEvent(e);
                    break;
                case 2:
                    mainmenu.evalMouseEvent(e);
                    break;
                case 3:
                    laden.evalMouseEvent(e);
                    break;
                case 4:
                    speichern.evalMouseEvent(e);
                    break;
                case 5:
                    credits.evalMouseEvent(e);
                    break;
                case 6:
                    karte.evalMouseEvent(e);
                    break;
                case 7:
                    woerterbuch.evalMouseEvent(e);
                    break;
                case 8:
                    skica.evalMouseEvent(e);
                    break;
                default:
                    System.out.println("Wrong Pressevent Prio 2 !");
            }
            return;
        }

        // ansonsten an Location - Prio 3
        if (currentLocation != null) {
            currentLocation.evalMouseEvent(e);
        }

    }

    // Mouse-MotionEvents abfangen
    public synchronized void mouseDragged(GenericMouseEvent e) {
        mouseMoved(e);
    }

    public synchronized final void mouseMoved(GenericMouseEvent e) {
        // System.out.print("M");
        if (/*(isWindowactive == false) ||*/ (isListenerActive == false)) {
            return;
        }
        Mousepoint = e.getPoint();

        // Bei aktivem Exit dorthin - Prio 1
        if (exit.active == true) {
            exit.evalMouseMoveEvent(Mousepoint);
            return;
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2
        if (whatScreen != 0) {
            switch (whatScreen) {
                case 1:
                    inventory.evalMouseMoveEvent(Mousepoint);
                    break;
                case 2:
                    mainmenu.evalMouseMoveEvent(Mousepoint);
                    break;
                case 3:
                    laden.evalMouseMoveEvent(Mousepoint);
                    break;
                case 4:
                    speichern.evalMouseMoveEvent(Mousepoint);
                    break;
                case 5:
                    credits.evalMouseMoveEvent(Mousepoint);
                    break;
                case 6:
                    karte.evalMouseMoveEvent(Mousepoint);
                    break;
                case 7:
                    woerterbuch.evalMouseMoveEvent(Mousepoint);
                    break;
                case 8:
                    skica.evalMouseMoveEvent(Mousepoint);
                    break;
                default:
                    System.out.println("Wrong Moveevent Prio 2");
            }
            return;
        }

        // ansonsten an Location - Prio 3
        if (currentLocation != null) {
            currentLocation.evalMouseMoveEvent(Mousepoint);
        }
    }

    public void windowClosing() {
        if (isListenerActive == false) {
            return;
        }
        Clipset = false;
        exit.Activate(1);
    }

    // verlassene Location wird geloescht
    public synchronized void DestructLocation(int oldLocation) {
        // normale Locations werden nicht zerstoert, nur ueberschrieben (Mainloc - Objekt)

        // Laden, Speichern, Credits, Karte muss jedoch geloescht werden 
        switch (oldLocation) {
            case 102:
                laden = null;
                break;
            case 103:
                speichern = null;
                break;
            case 104:
                credits = null;
                break;
            case 106:
                karte = null;
                break;
            case 107:
                woerterbuch = null;
                break;
            case 108:
                skica = null;
                break;
        }
    }

    // neue Location wird erzeugt
    public synchronized void ConstructLocation(int newLocation) {
        // Paint - Schleife anhalten
        NoPaint(true);

        // to save memory a "cleanup" method is called here
        // the default implementation, however, is just an empty method
        if ((newLocation <= 50) ||
                ((newLocation >= 63) && (newLocation < 102)) ||
                (newLocation > 110)) {
            if (currentLocation != null) {
                currentLocation.cleanup();

                // if the system is under constant load, the garbage
                // collector will seldom/never be called
                // observed on slow android systems
                System.gc();
            }
        }

        // Labyrinth - Load - Einspruenge umleiten und als Load kennzeichnen
        if ((newLocation > 50) && (newLocation < 63)) {
            ConstructLocation(newLocation, 0);
            return;
        }

        switch (newLocation) {
            case 0:
                break;
            case 1:
                currentLocation = (Mainloc) new Ralbicy1(this, currLocation);
                break;
            case 2:
                currentLocation = (Mainloc) new Most1(this, currLocation);
                break;
            case 3:
                currentLocation = (Mainloc) new Jitk1(this, currLocation);
                break;
            case 4:
                currentLocation = (Mainloc) new Haty1(this, currLocation);
                break;
            case 5:
                currentLocation = (Mainloc) new Les1(this, currLocation);
                break;
            case 6:
                currentLocation = (Mainloc) new Doma1(this, currLocation);
                break;
            case 7:
                currentLocation = (Mainloc) new Sunow1(this, currLocation);
                break;
            case 8:
                currentLocation = (Mainloc) new Rapak1(this, currLocation);
                break;
            case 9:
                currentLocation = (Mainloc) new Wobzor1(this, currLocation);
                break;
            case 10:
                currentLocation = (Mainloc) new Wjerby1(this, currLocation);
                break;
            case 11:
                currentLocation = (Mainloc) new Polo1(this, currLocation);
                break;
            case 12:
                currentLocation = (Mainloc) new Kupa1(this, currLocation);
                break;
            case 13:
                currentLocation = (Mainloc) new Wjes1(this, currLocation);
                break;
            case 14:
                currentLocation = (Mainloc) new Hojnt1(this, currLocation);
                break;
            case 15:
                currentLocation = (Mainloc) new Njedz1(this, currLocation);
                break;
            case 16:
                currentLocation = (Mainloc) new Wila1(this, currLocation);
                break;
            case 17:
                currentLocation = (Mainloc) new CornyCholmc1(this, currLocation);
                break;
            case 18:
                currentLocation = (Mainloc) new Dubring1(this, currLocation);
                break;
            case 19:
                currentLocation = (Mainloc) new Zdzary1(this, currLocation);
                break;
            case 20:
                currentLocation = (Mainloc) new Mertens1(this, currLocation);
                break;
            case 21:
                currentLocation = (Mainloc) new Kulow1(this, currLocation);
                break;
            case 22:
                currentLocation = (Mainloc) new Cyrkej1(this, currLocation);
                break;
            //    case 23: currentLocation = (Mainloc) new pinca1 (this, currLocation);
            //             break;
            case 24:
                currentLocation = (Mainloc) new Hoscenc1(this, currLocation);
                break;
            case 25:
                currentLocation = (Mainloc) new Mlyn1(this, currLocation);
                break;
            case 26:
                currentLocation = (Mainloc) new MlynkCornyCholmc1(this, currLocation);
                break;
            case 27:
                currentLocation = (Mainloc) new Jama1(this, currLocation);
                break;
            case 28:
                currentLocation = (Mainloc) new Dzera(this, currLocation);
                break;
            case 29:
                currentLocation = (Mainloc) new HojntAuto(this, currLocation);
                break;
            case 70:
                currentLocation = (Mainloc) new Cyrkej2(this, currLocation);
                break;
            case 71:
                currentLocation = (Mainloc) new Doma2(this, currLocation);
                break;
            case 72:
                currentLocation = (Mainloc) new Dubring2(this, currLocation);
                break;
            case 73:
                currentLocation = (Mainloc) new Hojnt2(this, currLocation);
                break;
            case 74:
                currentLocation = (Mainloc) new Jitk2(this, currLocation);
                break;
            case 75:
                currentLocation = (Mainloc) new CornyCholmc2(this, currLocation);
                break;
            case 76:
                currentLocation = (Mainloc) new Kulow2(this, currLocation);
                break;
            case 77:
                currentLocation = (Mainloc) new Labyr122(this, currLocation);
                break;
            case 78:
                currentLocation = (Mainloc) new Les2(this, currLocation);
                break;
            case 79:
                currentLocation = (Mainloc) new Mertens2(this, currLocation);
                break;
            case 80:
                currentLocation = (Mainloc) new Njedz2(this, currLocation);
                break;
            case 81:
                currentLocation = (Mainloc) new Pinca2(this, currLocation);
                break;
            case 82:
                currentLocation = (Mainloc) new Ralbicy2(this, currLocation);
                break;
            case 83:
                currentLocation = (Mainloc) new Rapak2(this, currLocation);
                break;
            case 84:
                currentLocation = (Mainloc) new Sunow2(this, currLocation);
                break;
            case 85:
                currentLocation = (Mainloc) new Wila2(this, currLocation);
                break;
            case 86:
                currentLocation = (Mainloc) new Wjerby2(this, currLocation);
                break;
            case 87:
                currentLocation = (Mainloc) new Wjes2(this, currLocation);
                break;
            case 88:
                currentLocation = (Mainloc) new Wobzor2(this, currLocation);
                break;
            case 89:
                currentLocation = (Mainloc) new Most2(this, currLocation);
                break;
            case 90:
                currentLocation = (Mainloc) new Mlyn2(this, currLocation);
                break;
            case 91:
                currentLocation = (Mainloc) new Inmlyn(this, currLocation);
                break;
            case 92:
                currentLocation = (Mainloc) new Swoboda(this, currLocation);
                break;
            case 93:
                currentLocation = (Mainloc) new Zdzary2(this, currLocation);
                break;
            case 94:
                currentLocation = (Mainloc) new Jezba(this, currLocation);
                break;
            case 100:
                currentLocation = (Mainloc) new Zawod1(this);
                break;
            case 101:
                currentLocation = (Mainloc) new Extro(this);
                break;
            case 102:
                laden = new Wocinic(this);
                newLocation = currLocation; // bei Laden, Speichern, Credits
                break;
            case 103:
                speichern = new Skladzic(this);
                newLocation = currLocation; // bleibt alte Location bestehen
                break;
            case 104:
                credits = new Info(this);
                newLocation = currLocation;
                break;
            case 105:
                currentLocation = (Mainloc) new Install(this);
                break;
            case 106:
                karte = new Karta(this);
                newLocation = currLocation; // alte Location bleibt bestehen
                break;
            case 107:
                woerterbuch = new Slownik(this);
                // woerterbuch = new Slownikcreate (this);
                newLocation = currLocation;
                break;
            case 108:
                skica = new Skica(this);
                newLocation = currLocation; // alte Location bleibt bestehen
                break;
            case 109:
                currentLocation = (Mainloc) new LanguageChooser(this, gameProperties);
                break;
            // Dresdener Locations
            case 120:
                currentLocation = (Mainloc) new Kuchnja(this, currLocation);
                break;
            case 121:
                currentLocation = (Mainloc) new Haska(this, currLocation);
                break;
            case 122:
                currentLocation = (Mainloc) new Spaniska(this, currLocation);
                break;
            case 123:
                currentLocation = (Mainloc) new Hala(this, currLocation);
                break;
            case 124:
                currentLocation = (Mainloc) new Komedij(this, currLocation);
                break;
            case 125:
                currentLocation = (Mainloc) new Jewisco(this, currLocation);
                break;
            case 126:
                currentLocation = (Mainloc) new Murja(this, currLocation);
                break;
            case 127:
                currentLocation = (Mainloc) new Terassa(this, currLocation);
                break;
            case 128:
                currentLocation = (Mainloc) new Straze(this, currLocation);
                break;
            case 129:
                currentLocation = (Mainloc) new Mlynkmurja(this, currLocation);
                break;
            case 130:
                currentLocation = (Mainloc) new Hdwor(this, currLocation);
                break;
            case 131:
                currentLocation = (Mainloc) new Trepjena(this, currLocation);
                break;
            case 132:
                currentLocation = (Mainloc) new Kuchnjaopen(this, currLocation);
                break;
            case 140:
                currentLocation = (Mainloc) new Saal(this, currLocation);
                break;
            case 141:
                currentLocation = (Mainloc) new Dingl(this, currLocation);
                break;
            case 142:
                currentLocation = (Mainloc) new Chodba(this, currLocation);
                break;
            case 143:
                currentLocation = (Mainloc) new Casnik(this, currLocation);
                break;
            case 144:
                currentLocation = (Mainloc) new Couch(this, currLocation);
                break;
            case 145:
                currentLocation = (Mainloc) new Zelen(this, currLocation);
                break;
            case 146:
                currentLocation = (Mainloc) new Wonka(this, currLocation);
                break;
            case 150:
                currentLocation = (Mainloc) new Cychi(this, currLocation);
                break;
            case 151:
                currentLocation = (Mainloc) new Zachod(this, currLocation);
                break;
            case 152:
                currentLocation = (Mainloc) new Gang(this, currLocation);
                break;
            case 153:
                currentLocation = (Mainloc) new Kapala(this, currLocation);
                break;
            case 160:
                currentLocation = (Mainloc) new Panorama(this, currLocation);
                break;
            case 161:
                currentLocation = (Mainloc) new Stwa(this, currLocation);
                break;
            case 162:
                currentLocation = (Mainloc) new Zahrodnik(this, currLocation);
                break;
            case 163:
                currentLocation = (Mainloc) new Habor(this, currLocation);
                break;
            case 164:
                currentLocation = (Mainloc) new Lodz(this, currLocation);
                break;
            case 170:
                currentLocation = (Mainloc) new Zastup(this, currLocation);
                break;
            case 171:
                currentLocation = (Mainloc) new Manega(this, currLocation);
                break;
            case 175:
                currentLocation = (Mainloc) new StareWiki(this, currLocation);
                break;
            case 180:
                currentLocation = (Mainloc) new DDKarta(this, currLocation);
                break;
            case 181:
                currentLocation = (Mainloc) new Poklad(this, currLocation);
                break;
            case 200:
                currentLocation = (Mainloc) new Wotrow(this, currLocation);
                break;
            case 201:
                currentLocation = (Mainloc) new Hrodz(this, currLocation);
                break;
            case 202:
                currentLocation = (Mainloc) new Rowy(this, currLocation);
                break;
            case 203:
                currentLocation = (Mainloc) new Doma4(this, currLocation);
                break;
            default:
                System.out.println("Falsche Location-ID fuer Konstruktor !");
        }
        currLocation = newLocation;

        // Paint - Schleife wieder aktivieren
        NoPaint(false);
    }

    // neue Location wird erzeugt fuer Labyrinth extra, da Richtung unbekannt !!!
    public synchronized void ConstructLocation(int newLocation, int Richtung) {
        // Richtungsvariable wieder nach Uhrzeit 3, 6, 9, 12

        // Paint - Schleife anhalten
        NoPaint(true);

        switch (newLocation) {

            case 51:
                currentLocation = (Mainloc) new Labyr1(this, Richtung);
                break;
            case 52:
                currentLocation = (Mainloc) new Labyr2(this, Richtung);
                break;
            case 53:
                currentLocation = (Mainloc) new Labyr3(this, Richtung);
                break;
            case 54:
                currentLocation = (Mainloc) new Labyr4(this, Richtung);
                break;
            case 55:
                currentLocation = (Mainloc) new Labyr5(this, Richtung);
                break;
            case 56:
                currentLocation = (Mainloc) new Labyr6(this, Richtung);
                break;
            case 57:
                currentLocation = (Mainloc) new Labyr7(this, Richtung);
                break;
            case 58:
                currentLocation = (Mainloc) new Labyr8(this, Richtung);
                break;
            case 59:
                currentLocation = (Mainloc) new Labyr9(this, Richtung);
                break;
            case 60:
                currentLocation = (Mainloc) new Labyr10(this, Richtung);
                break;
            case 61:
                currentLocation = (Mainloc) new Labyr11(this, Richtung);
                break;
            case 62:
                currentLocation = (Mainloc) new Labyr12(this, Richtung);
                break;
            default:
                System.out.println("Nott awajlebbl!!");
        }
        currLocation = newLocation;

        // Paint - Schleife wieder aktivieren
        NoPaint(false);
    }

    public synchronized void ConstructLocation(int newLocation, Husa gans1, Husa gans2, Husa gans3) {
        NoPaint(true);

        if (currentLocation != null) {
            currentLocation.cleanup();

            // if the system is under constant load, the garbage
            // collector will seldom/never be called
            // observed on slow android systems
            System.gc();
        }

        currentLocation = (Mainloc) new Doma1(this, currLocation, gans1, gans2, gans3);

        currLocation = newLocation;

        NoPaint(false);
    }

    public synchronized final void keyPressed(GenericKeyEvent e) {
        if ((isListenerActive == false) || (isWindowactive == false)) {
            return;
        }

        // Zum Debuggen hier Krabat-Groesse einstellbar
        // Nur auf Funktionstasten reagieren
        int Taste = e.getKeyCode();

        // Verkleinerung
        /*    if (Taste == KeyEvent.VK_F7)
        {
        override++;
        repaint();
        return;
        }    

        // Vegroesserung
        if (Taste == KeyEvent.VK_F8)
        {
        override--;
        repaint();
        return;
        }*/

        // Bei aktivem Exit dorthin - Prio 1
        if (exit.active == true) {
            exit.evalKeyEvent(e);
            return;
        }

        // Feststellen, ob das Woerterbuch aktiviert werden soll
        if (whatScreen == 0) {
            if (Taste == GenericKeyEvent.VK_F5) {
                ConstructLocation(107);
                whatScreen = 7;
                Clipset = false;
                repaint();
            }
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2
        if (whatScreen != 0) {
            switch (whatScreen) {
                case 1:
                    inventory.evalKeyEvent(e);
                    break;
                case 2:
                    mainmenu.evalKeyEvent(e);
                    break;
                case 3:
                    laden.evalKeyEvent(e);
                    break;
                case 4:
                    speichern.evalKeyEvent(e);
                    break;
                case 5:
                    credits.evalKeyEvent(e);
                    break;
                case 6:
                    karte.evalKeyEvent(e);
                    break;
                case 7:
                    woerterbuch.evalKeyEvent(e);
                    break;
                case 8:
                    skica.evalKeyEvent(e);
                    break;
                default:
                    System.out.println("Wrong Keyevent Prio 2 !");
            }
            return;
        }

        // ansonsten an Location - Prio 3
        if (currentLocation != null) {
            currentLocation.evalKeyEvent(e);
        }
    }

    // erzeugt kleines GenericImage vom Gamescreen, falls Speichern aufgerufen wird
    public void StoreImage() {
        GenericImage tempImage = GenericToolkit.getDefaultToolkit().createImage(640, 480);
        GenericDrawingContext tempGraphics = tempImage.getGraphics();
        tempGraphics.drawImage(offImage, -scrollx, -scrolly, observer);
        saveImage = tempImage.getScaledInstance(119, 89, GenericImage.SCALE_DEFAULT);
    }

    // Programmneustart
    public void restart() {
        DestructLocation(currLocation);
        InitGame();
        inventory.ResetInventory();
        ConstructLocation(100);
        repaint();
    }

    // Alle relevanten Listener fuer Mousepress und Mousemove deaktivieren
    public void Freeze(boolean cold) {
        if (cold == true) {
            setCursor(Warten);
            isListenerActive = false;
        } else {
            isListenerActive = true;
        }
    }

    // Repaint - Schleife anhalten, verhindert Paint-Anhaeufungen
    public void NoPaint(boolean haltan) {
        StopPaint = haltan; // true = stop, false = weiter
    }

    // diese Methode castet den richtigen Spieler, je nach Action - Array
    public void CheckKrabat() {
        // 850 und 851 = false -> Krabat ist ganz normal
        // 850 = true          -> Krabat von Seite mit Drasta
        // 851 = true          -> Krabat von Oben
        // Hilfsvariable verhindert staendiges Laden...

        // alte Krabatvariablen merken, die beim Umladen weg waeren
        int merkFacing = krabat.GetFacing();
        GenericPoint merkPos = krabat.GetKrabatPos();
        int merkMaxx = krabat.maxx;
        float merkZoom = krabat.zoomf;
        int merkDef = krabat.defScale;

        // normales Aussehen
        if ((Actions[850] == false) && (Actions[851] == false)) {
            if (KrabatForm != 1) {
                krabat = (Krabat) new KrabatNormal(this);
                KrabatForm = 1;
            }
        }

        // hat sich Drasta angezogen
        if (Actions[850] == true) {
            if (KrabatForm != 2) {
                krabat = (Krabat) new KrabatDrasta(this);
                KrabatForm = 2;
            }
        }

        // ist von oben zu sehen
        if (Actions[851] == true) {
            if (KrabatForm != 3) {
                krabat = (Krabat) new KrabatOben(this);
                KrabatForm = 3;
            }
        }

        // Krabatvariablen wiederherstellen
        krabat.SetFacing(merkFacing);
        krabat.SetKrabatPos(merkPos);
        krabat.maxx = merkMaxx;
        krabat.zoomf = merkZoom;
        krabat.defScale = merkDef;

    }

    public GenericImage constructCursorImage(String Pathname) {
        return imageFetcher.fetchImage(Pathname);
    }

    public void clearHotspots() {
        hotspots.clear();
    }

    public void addHotspot(GenericPoint pt) {
        hotspots.add(pt);
    }

    public void addHotspot(Borderrect rect) {
        hotspots.add(rect.getMiddlePoint());
    }

    public void removeHotspot(Borderrect rect) {
        hotspots.remove(rect.getMiddlePoint());
    }

    public ArrayList<GenericPoint> getHotspots() {
        return hotspots;
    }
}
