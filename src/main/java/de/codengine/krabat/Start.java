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

package de.codengine.krabat;

import de.codengine.krabat.anims.*;
import de.codengine.krabat.locations.*;
import de.codengine.krabat.locations2.*;
import de.codengine.krabat.locations3.*;
import de.codengine.krabat.locations4.*;
import de.codengine.krabat.main.*;
import de.codengine.krabat.platform.*;
import de.codengine.krabat.sound.AbstractPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

public class Start implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Start.class);
    // private static final boolean have_png = true;

    public Thread animator;

    // Instanz des z.Z. aktiven Location-Objekts (SS)
    public MainLocation currentLocation;

    public GenericCursor cursorUp;
    public GenericCursor cursorDown;
    public GenericCursor cursorLeft;
    public GenericCursor cursorRight;
    public GenericCursor cursorInventory;
    public GenericCursor cursorHighlightInventory;
    public GenericCursor cursorNormal;
    public GenericCursor cursorCross;
    public GenericCursor cursorWait;
    public GenericCursor cursorNone;
    public boolean isInventoryCursor;         // Zeigt an, ob Mauscursor = Inventarstueck
    public boolean isInventoryHighlightCursor;     // Zeigt an, ob Mauscursor gehighlighted werden muss
    public int whatItem = 0;          // Welches Inventarstueck haengt an der Angel?
    // Ist nur im Zusammenhang mit invCursor gueltig!!!

    // Klasseninitialisierungen
    public Inventar inventory;
    public MainMenu mainMenu;
    public LoadGame loadGame;
    public SaveGame saveGame;
    public Info credits;
    public Krabat krabat;
    public Imagefont imageFont;
    public PathFinder pathFinder;
    public PathWalker pathWalker;
    public ExitGame exitGame;
    public GenericSoundEffectPlayer soundPlayer;
    public Karta map;
    public Slownik dictionary;
    public Skica sketch;
    // public Slownikcreate woerterbuch;

    // wichtige Sachen fuer Hauptklasse
    private GenericImage offImage;
    public GenericImage saveImage;
    public GenericDrawingContext offGraphics;

    public int currentLocationIdx;                  // aktuelle Location (Index)
    public int talkCount;                     // Zaehler, wielange jemand noch spricht und ob ueberhaupt
    public boolean isAnimRunning;                 // Flag, ob Animation gespielt wird
    public boolean isWindowActive;            // Flag, ob Spiel aktiv ist
    public boolean isMouseValid;              // Flag, ob Mausposition im Fenster ist
    public boolean isBackgroundAnimRunning;                    // Flag, ob Hintergrundanimationen laufen
    public boolean isMultipleChoiceActive;                // Flag, ob Multiple Choice gerade aktiv ist
    private boolean isListenerActive;         // Flag, ob Listener ausgefuehrt werden oder nicht
    private boolean stopPaint = false;        // Paint - Schleife waehrend Init anhalten

    public ScreenType whatScreen = ScreenType.NONE;

    public static int language; // Sprache

    // Variablen fuer Mousemove und Doppelklick
    public GenericPoint mousePoint = new GenericPoint(0, 0);
    public boolean isDoubleClick = false;

    // Scrolling - Variablen
    public int scrollX;
    public int scrollY;
    public boolean isScrolling;

    public boolean[] actions = new boolean[1002];
    public boolean isClipSet;

    // Konstante fuer Labyrinth - Schwierigkeit
    public static final int labyrinthHelp = 15;

    private int krabatShape = 0; // gibt an, welche KrabatKlasse aktiv ist (default ist von der Seite)

    public boolean enteringFromMap = false; // hier ein "Achtung" an den CD-Player jeder Kartenlocation,
    // dass auf jeden Fall die CD neu gestartet werden muss

    // this flag is for make sure that the paint method does nothing until it makes sense
    // otherwise there will be NPExceptions, when using FullScreenMode -- SS 2001/11/10
    boolean isPaintAllowed = false;

    public GenericImageFetcher imageFetcher;

    // public static String urlBase;

    public static AbstractPlayer player;

    private GenericContainer container;

    private AtomicInteger repaintCounter;

    public GenericStorageManager storageManager;

    private GameProperties gameProperties;

    public static StringManager stringManager;

    public static String thirdGameLanguage;

    protected void runGamePt1(
            int currentLanguageIndex,
            GenericImageFetcher imageFetcher,
            GenericContainer container,
            GenericSoundEffectPlayer player,
            AbstractPlayer musicPlayer,
            GenericStorageManager storageManager) {
        Start.player = musicPlayer;

        this.imageFetcher = imageFetcher;
        this.container = container;
        this.storageManager = storageManager;
        // Alle wichtigen Variablen zuruecksetzen
        initGame();

        stringManager = new StringManager(storageManager);

        // Variablen fuer Mausdoppelklick festlegen
        mousePoint = new GenericPoint(0, 0);

        Hashtable<String, String> defaults = new Hashtable<>();
        defaults.put(GameProperties.CURRENT_GAME_LANGUAGE_INDEX, Integer.toString(currentLanguageIndex));
        defaults.put(GameProperties.THIRD_GAME_LANGUAGE_SELECTION, "de");
        gameProperties = new GameProperties(storageManager, defaults);
        gameProperties.loadProperties();

        // which language index is active in the game?
        int tmpLangIndex = 0;
        try {
            tmpLangIndex = Integer.parseInt(gameProperties.getProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX));
        } catch (NumberFormatException e) {
            log.warn("Unrecognized game language index string:'{}'.", gameProperties.getProperty(GameProperties.CURRENT_GAME_LANGUAGE_INDEX));
        }

        if (tmpLangIndex >= 1 && tmpLangIndex <= 3) {
            language = tmpLangIndex;
        } else {
            language = 1; // fallback is HS
        }

        // what is the selected "third language" (if any)
        thirdGameLanguage = gameProperties.getProperty(GameProperties.THIRD_GAME_LANGUAGE_SELECTION);
        // if no language selected yet, this will load German
        stringManager.defineThirdLanguage(thirdGameLanguage);

        log.info("Sprache {}", language);
        log.info("Third language: {}", thirdGameLanguage);

        // feststellen, ob Sound abgespielt werden darf und wie
        soundPlayer = player;

        // Stets vorhandene Klassen laden
        imageFont = new Imagefont(this); // Schrift
        pathFinder = new PathFinder();      // Laufroutinen
        pathWalker = new PathWalker(this);   // Laufroutinen
    }

    protected void runGamePt2(GenericPoint hotSpot, GenericCursor[] cursors) {
        inventory = new Inventar(this, hotSpot);   // Inventar

        cursorUp = cursors[0];
        cursorDown = cursors[1];
        cursorLeft = cursors[2];
        cursorRight = cursors[3];
        cursorNormal = cursors[4];
        cursorCross = cursors[5];
        cursorWait = cursors[6];
        cursorNone = cursors[7];

        mainMenu = new MainMenu(this, gameProperties);  // Hauptmenue
        exitGame = new ExitGame(this, gameProperties);       // Sicherheitsabfragen
        krabat = new KrabatNormal(this);

        CheckKrabat();

        log.trace("Font is cut.");

        repaintCounter = new AtomicInteger();

        // finally paint and run everything 
        isPaintAllowed = true;
    }

    protected void runGamePt3() {
        offImage = GenericToolkit.getDefaultToolkit().createImage(1280, 480);
        offGraphics = offImage.getGraphics();
        log.debug("Got DB Offscreen-Image");

        // FIXME
        /*
        if (LanguageSupportMapper.getInternalCode(thirdGameLanguage) == 0) {
            // show language chooser
            ConstructLocation(109);
        } else {
            // language set in properties, start game
            ConstructLocation(100);
        }
         */

        ConstructLocation(100);

        startThread();
        repaint();
        log.debug("Well, here we go...");
    }

    // Hier wird alles fuer Neustart initialisiert
    private void initGame() {
        // Aktionen loeschen (Krabat hat noch nichts gemacht!)
        for (int i = 0; i <= 1000; ++i) {
            actions[i] = false;
        }
        isInventoryCursor = false;
        isInventoryHighlightCursor = false;
        talkCount = 0;
        isAnimRunning = false;
        isWindowActive = true;
        isMouseValid = true;
        isBackgroundAnimRunning = false;
        isMultipleChoiceActive = false;
        isListenerActive = false;
        whatScreen = ScreenType.NONE;
        scrollX = 0;
        scrollY = 0;
        isScrolling = false;
        isClipSet = false;
    }

    public void repaint() {
        repaintCounter.incrementAndGet();
    }

    public void setCursor(GenericCursor cursor) {
        container.setCursor(cursor);
    }

    public final synchronized GenericImage paint(@SuppressWarnings("unused") GenericDrawingContext g) {
        // Interpolation aktivieren, somit werden gezoomte/verkleinerte Sprites
        // wesentlich besser dargestellt (besonders der Krabat)

        //TODO: Add support for antialiasing and filtering

        /*
    	Graphics2D g2 = (Graphics2D) offGraphics;
        g2.setRenderingHint (RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		*/

        // no paint, until it make sense (othwerwise exceptions in full screen mode)
        if (!isPaintAllowed) {
            return null;
        }

        // bei aktivem Exit (Sicherheitsabfragen) Paint umleiten - Prioritaet 1 - kein Scrolling
        if (exitGame.active) {
            exitGame.paintExit(offGraphics);
            return offImage;
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2 - kein Scrolling
        if (whatScreen != ScreenType.NONE) {
            switch (whatScreen) {
                case INVENTORY:
                    inventory.paintInventory(offGraphics);
                    break;
                case MAIN_MENU:
                    mainMenu.paintMainmenu(offGraphics);
                    break;
                case LOAD_GAME:
                    loadGame.paintLaden(offGraphics);
                    break;
                case SAVE_GAME:
                    saveGame.paintSpeichern(offGraphics);
                    break;
                case CREDITS:
                    credits.paintCredits(offGraphics);
                    break;
                case MAP:
                    map.paintKarte(offGraphics);
                    break;
                case DICTIONARY:
                    dictionary.paintSlownik(offGraphics);
                    break;
                case SKETCH:
                    sketch.paintSkizze(offGraphics);
                    break;
                default:
                    log.error("Wrong Paint Prio 2! whatScreen = {}", whatScreen);
            }
            return offImage;
        }

        // Scrolling berechnen und ausfuehren
        scrollIt();

        // paint in der jeweiligen Location aufrufen - Prioritaet 3 (SS)
        if (currentLocation != null) {
            currentLocation.paintLocation(offGraphics);
        } else {
            log.error("Null-Painting !");
        }

        if (offImage != null) {
            return offImage;
        }

        return null;
    }

    // Scrolling - Routine
    private void scrollIt() {
        int scrollOffsetx;

        // erst mal schauen, wer ueberhaupt alles scrollt...
        switch (currentLocationIdx) {
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
        int temp = krabat.getPos().x - scrollX;

        isScrolling = false;
        if (temp < 315) {
            scrollX -= scrollOffsetx;
            isScrolling = true;
        }
        if (temp > 325) {
            scrollX += scrollOffsetx;
            isScrolling = true;
        }
        if (scrollX < 0) {
            scrollX = 0;
            isScrolling = false;
        }
        if (scrollX > 640) {
            scrollX = 640;
            isScrolling = false;
        }

        // Cursor neu berechnen, weil Hintergrund verschoben wurde
        // aus case-bloecken entfernt (SS)
        if (currentLocation != null) {
            currentLocation.evalMouseMoveEvent(mousePoint);
        }
    }

    // Thread starten und immer laufen lassen
    private void startThread() {
        animator = new Thread(this);
        animator.setPriority(Thread.NORM_PRIORITY);
        animator.setDaemon(true); // thread shall not block VM termination
        animator.start();
    }

    @Override
    public void run() {
        while (animator != null) {
            final boolean repaintTriggered = repaintCounter.get() > 0;
            final boolean needsRepaint = needsRepaint(repaintTriggered);

            // um Krabat kuemmern, solange er was zu tun hat
            if (needsRepaint) {
                // the repaint() logic might try to schedule
                // another repaint
                // therefore, make sure we do not decrement
                // a repaint request immediately again
                // and so check for decrement right here
                repaintCounter.updateAndGet(val -> val > 0 ? val - 1 : val);
                container.repaint();
            }

            sleep(100);
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis); //TODO: Make configurable
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean needsRepaint(boolean repaintTriggered) {
        return (krabat.isWalking || krabat.isWandering ||
                krabat.nAnimation != 0 || talkCount != 0 || isAnimRunning
                || isScrolling || isBackgroundAnimRunning || repaintTriggered)
                && !stopPaint;
    }

    public void mouseEntered() {
        isMouseValid = true;
    }

    public void mouseExited() {
        isMouseValid = false;

        //Signal an Menuelocations, dass HiLight abgeschaltet werden soll
        if (whatScreen != ScreenType.NONE) {
            switch (whatScreen) {
                case INVENTORY:
                    inventory.evalMouseExitEvent();
                    break;
                case MAIN_MENU:
                    mainMenu.evalMouseExitEvent();
                    break;
                case LOAD_GAME:
                    loadGame.evalMouseExitEvent();
                    break;
                case SAVE_GAME:
                    saveGame.evalMouseExitEvent();
                    break;
                case CREDITS:
                    credits.evalMouseExitEvent();
                    break;
                case MAP:
                    map.evalMouseExitEvent();
                    break;
                case DICTIONARY:
                    dictionary.evalMouseExitEvent();
                    break;
                case SKETCH:
                    sketch.evalMouseExitEvent();
                    break;
                default:
                    log.error("Wrong Exitevent! whatScreen = {}", whatScreen);
            }
            return;
        }

        // hier wird Erigenis an die ggw. Location weitergegeben
        if (currentLocation != null) {
            currentLocation.evalMouseExitEvent();
        }
    }

    public synchronized final void mousePressed(GenericMouseEvent e) {
        if (!isMouseValid || /*(isWindowactive == false) || */
                !isListenerActive) {
            // Versuch, Event zu loeschen, wenn nicht benoetigt
            return;
        }

        // apply the doubleclick detection
        isDoubleClick = e.getDoubleClick();

        // Wenn Exit aktiv, dorthin - Prio 1
        if (exitGame.active) {
            exitGame.evalMouseEvent(e);
            return;
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2
        if (whatScreen != ScreenType.NONE) {
            switch (whatScreen) {
                case INVENTORY:
                    inventory.evalMouseEvent(e);
                    break;
                case MAIN_MENU:
                    mainMenu.evalMouseEvent(e);
                    break;
                case LOAD_GAME:
                    loadGame.evalMouseEvent(e);
                    break;
                case SAVE_GAME:
                    saveGame.evalMouseEvent(e);
                    break;
                case CREDITS:
                    credits.evalMouseEvent(e);
                    break;
                case MAP:
                    map.evalMouseEvent(e);
                    break;
                case DICTIONARY:
                    dictionary.evalMouseEvent(e);
                    break;
                case SKETCH:
                    sketch.evalMouseEvent(e);
                    break;
                default:
                    log.error("Wrong Pressevent Prio 2! whatScreen = {}", whatScreen);
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
        if (/*(isWindowactive == false) ||*/ !isListenerActive) {
            return;
        }
        mousePoint = e.getPoint();

        // Bei aktivem Exit dorthin - Prio 1
        if (exitGame.active) {
            exitGame.evalMouseMoveEvent(mousePoint);
            return;
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2
        if (whatScreen != ScreenType.NONE) {
            switch (whatScreen) {
                case INVENTORY:
                    inventory.evalMouseMoveEvent(mousePoint);
                    break;
                case MAIN_MENU:
                    mainMenu.evalMouseMoveEvent(mousePoint);
                    break;
                case LOAD_GAME:
                    loadGame.evalMouseMoveEvent(mousePoint);
                    break;
                case SAVE_GAME:
                    saveGame.evalMouseMoveEvent(mousePoint);
                    break;
                case CREDITS:
                    credits.evalMouseMoveEvent(mousePoint);
                    break;
                case MAP:
                    map.evalMouseMoveEvent(mousePoint);
                    break;
                case DICTIONARY:
                    dictionary.evalMouseMoveEvent(mousePoint);
                    break;
                case SKETCH:
                    sketch.evalMouseMoveEvent();
                    break;
                default:
                    log.error("Wrong Moveevent Prio 2! whatScreen = {}", whatScreen);
            }
            return;
        }

        // ansonsten an Location - Prio 3
        if (currentLocation != null) {
            currentLocation.evalMouseMoveEvent(mousePoint);
        }
    }

    public void windowClosing() {
        if (!isListenerActive) {
            return;
        }
        isClipSet = false;
        exitGame.Activate(1);
    }

    // verlassene Location wird geloescht
    public synchronized void DestructLocation(int oldLocation) {
        // normale Locations werden nicht zerstoert, nur ueberschrieben (Mainloc - Objekt)

        // Laden, Speichern, Credits, Karte muss jedoch geloescht werden 
        switch (oldLocation) {
            case 102:
                loadGame = null;
                break;
            case 103:
                saveGame = null;
                break;
            case 104:
                credits = null;
                break;
            case 106:
                map = null;
                break;
            case 107:
                dictionary = null;
                break;
            case 108:
                sketch = null;
                break;
        }
    }

    // neue Location wird erzeugt
    public synchronized void ConstructLocation(int newLocation) {
        // Paint - Schleife anhalten
        NoPaint(true);

        // to save memory a "cleanup" method is called here
        // the default implementation, however, is just an empty method
        if (newLocation <= 50 ||
                newLocation >= 63 && newLocation < 102 ||
                newLocation > 110) {
            if (currentLocation != null) {
                currentLocation.cleanup();

                // if the system is under constant load, the garbage
                // collector will seldom/never be called
                // observed on slow android systems
                System.gc();
            }
        }

        // Labyrinth - Load - Einspruenge umleiten und als Load kennzeichnen
        if (newLocation > 50 && newLocation < 63) {
            ConstructLocation(newLocation, 0);
            return;
        }

        switch (newLocation) {
            case 0:
                break;
            case 1:
                currentLocation = new Ralbicy1(this, currentLocationIdx);
                break;
            case 2:
                currentLocation = new Most1(this, currentLocationIdx);
                break;
            case 3:
                currentLocation = new Jitk1(this, currentLocationIdx);
                break;
            case 4:
                currentLocation = new Haty1(this, currentLocationIdx);
                break;
            case 5:
                currentLocation = new Les1(this, currentLocationIdx);
                break;
            case 6:
                currentLocation = new Doma1(this, currentLocationIdx);
                break;
            case 7:
                currentLocation = new Sunow1(this, currentLocationIdx);
                break;
            case 8:
                currentLocation = new Rapak1(this, currentLocationIdx);
                break;
            case 9:
                currentLocation = new Wobzor1(this, currentLocationIdx);
                break;
            case 10:
                currentLocation = new Wjerby1(this, currentLocationIdx);
                break;
            case 11:
                currentLocation = new Polo1(this, currentLocationIdx);
                break;
            case 12:
                currentLocation = new Kupa1(this, currentLocationIdx);
                break;
            case 13:
                currentLocation = new Wjes1(this, currentLocationIdx);
                break;
            case 14:
                currentLocation = new Hojnt1(this, currentLocationIdx);
                break;
            case 15:
                currentLocation = new Njedz1(this, currentLocationIdx);
                break;
            case 16:
                currentLocation = new Wila1(this, currentLocationIdx);
                break;
            case 17:
                currentLocation = new CornyCholmc1(this, currentLocationIdx);
                break;
            case 18:
                currentLocation = new Dubring1(this, currentLocationIdx);
                break;
            case 19:
                currentLocation = new Zdzary1(this, currentLocationIdx);
                break;
            case 20:
                currentLocation = new Mertens1(this, currentLocationIdx);
                break;
            case 21:
                currentLocation = new Kulow1(this, currentLocationIdx);
                break;
            case 22:
                currentLocation = new Cyrkej1(this, currentLocationIdx);
                break;
            //    case 23: currentLocation = (Mainloc) new pinca1 (this, currLocation);
            //             break;
            case 24:
                currentLocation = new Hoscenc1(this, currentLocationIdx);
                break;
            case 25:
                currentLocation = new Mlyn1(this);
                break;
            case 26:
                currentLocation = new MlynkCornyCholmc1(this);
                break;
            case 27:
                currentLocation = new Jama1(this);
                break;
            case 28:
                currentLocation = new Dzera(this);
                break;
            case 29:
                currentLocation = new HojntAuto(this);
                break;
            case 70:
                currentLocation = new Cyrkej2(this, currentLocationIdx);
                break;
            case 71:
                currentLocation = new Doma2(this, currentLocationIdx);
                break;
            case 72:
                currentLocation = new Dubring2(this);
                break;
            case 73:
                currentLocation = new Hojnt2(this, currentLocationIdx);
                break;
            case 74:
                currentLocation = new Jitk2(this, currentLocationIdx);
                break;
            case 75:
                currentLocation = new CornyCholmc2(this, currentLocationIdx);
                break;
            case 76:
                currentLocation = new Kulow2(this, currentLocationIdx);
                break;
            case 77:
                currentLocation = new Labyr122(this);
                break;
            case 78:
                currentLocation = new Les2(this);
                break;
            case 79:
                currentLocation = new Mertens2(this, currentLocationIdx);
                break;
            case 80:
                currentLocation = new Njedz2(this, currentLocationIdx);
                break;
            case 81:
                currentLocation = new Pinca2(this);
                break;
            case 82:
                currentLocation = new Ralbicy2(this, currentLocationIdx);
                break;
            case 83:
                currentLocation = new Rapak2(this, currentLocationIdx);
                break;
            case 84:
                currentLocation = new Sunow2(this, currentLocationIdx);
                break;
            case 85:
                currentLocation = new Wila2(this, currentLocationIdx);
                break;
            case 86:
                currentLocation = new Wjerby2(this);
                break;
            case 87:
                currentLocation = new Wjes2(this, currentLocationIdx);
                break;
            case 88:
                currentLocation = new Wobzor2(this);
                break;
            case 89:
                currentLocation = new Most2(this, currentLocationIdx);
                break;
            case 90:
                currentLocation = new Mlyn2(this, currentLocationIdx);
                break;
            case 91:
                currentLocation = new Inmlyn(this, currentLocationIdx);
                break;
            case 92:
                currentLocation = new Swoboda(this, currentLocationIdx);
                break;
            case 93:
                currentLocation = new Zdzary2(this, currentLocationIdx);
                break;
            case 94:
                currentLocation = new Jezba(this);
                break;
            case 100:
                currentLocation = new Zawod1(this);
                break;
            case 101:
                currentLocation = new Extro(this);
                break;
            case 102:
                loadGame = new LoadGame(this);
                newLocation = currentLocationIdx; // bei Laden, Speichern, Credits
                break;
            case 103:
                saveGame = new SaveGame(this);
                newLocation = currentLocationIdx; // bleibt alte Location bestehen
                break;
            case 104:
                credits = new Info(this);
                newLocation = currentLocationIdx;
                break;
            case 105:
                currentLocation = new Install(this);
                break;
            case 106:
                map = new Karta(this);
                newLocation = currentLocationIdx; // alte Location bleibt bestehen
                break;
            case 107:
                dictionary = new Slownik(this);
                // woerterbuch = new Slownikcreate (this);
                newLocation = currentLocationIdx;
                break;
            case 108:
                sketch = new Skica(this);
                newLocation = currentLocationIdx; // alte Location bleibt bestehen
                break;
            case 109:
                currentLocation = new LanguageChooser(this, gameProperties);
                break;
            // Dresdener Locations
            case 120:
                currentLocation = new Kuchnja(this, currentLocationIdx);
                break;
            case 121:
                currentLocation = new Haska(this, currentLocationIdx);
                break;
            case 122:
                currentLocation = new Spaniska(this, currentLocationIdx);
                break;
            case 123:
                currentLocation = new Hala(this, currentLocationIdx);
                break;
            case 124:
                currentLocation = new Komedij(this, currentLocationIdx);
                break;
            case 125:
                currentLocation = new Jewisco(this, currentLocationIdx);
                break;
            case 126:
                currentLocation = new Murja(this, currentLocationIdx);
                break;
            case 127:
                currentLocation = new Terassa(this, currentLocationIdx);
                break;
            case 128:
                currentLocation = new Straze(this, currentLocationIdx);
                break;
            case 129:
                currentLocation = new Mlynkmurja(this);
                break;
            case 130:
                currentLocation = new Hdwor(this, currentLocationIdx);
                break;
            case 131:
                currentLocation = new Trepjena(this, currentLocationIdx);
                break;
            case 132:
                currentLocation = new Kuchnjaopen(this, currentLocationIdx);
                break;
            case 140:
                currentLocation = new Saal(this, currentLocationIdx);
                break;
            case 141:
                currentLocation = new Dingl(this, currentLocationIdx);
                break;
            case 142:
                currentLocation = new Chodba(this, currentLocationIdx);
                break;
            case 143:
                currentLocation = new Casnik(this, currentLocationIdx);
                break;
            case 144:
                currentLocation = new Couch(this, currentLocationIdx);
                break;
            case 145:
                currentLocation = new Zelen(this, currentLocationIdx);
                break;
            case 146:
                currentLocation = new Wonka(this, currentLocationIdx);
                break;
            case 150:
                currentLocation = new Cychi(this, currentLocationIdx);
                break;
            case 151:
                currentLocation = new Zachod(this, currentLocationIdx);
                break;
            case 152:
                currentLocation = new Gang(this, currentLocationIdx);
                break;
            case 153:
                currentLocation = new Kapala(this, currentLocationIdx);
                break;
            case 160:
                currentLocation = new Panorama(this, currentLocationIdx);
                break;
            case 161:
                currentLocation = new Stwa(this, currentLocationIdx);
                break;
            case 162:
                currentLocation = new Zahrodnik(this, currentLocationIdx);
                break;
            case 163:
                currentLocation = new Habor(this, currentLocationIdx);
                break;
            case 164:
                currentLocation = new Lodz(this, currentLocationIdx);
                break;
            case 170:
                currentLocation = new Zastup(this, currentLocationIdx);
                break;
            case 171:
                currentLocation = new Manega(this, currentLocationIdx);
                break;
            case 175:
                currentLocation = new StareWiki(this, currentLocationIdx);
                break;
            case 180:
                currentLocation = new DDKarta(this, currentLocationIdx);
                break;
            case 181:
                currentLocation = new Poklad(this);
                break;
            case 200:
                currentLocation = new Wotrow(this, currentLocationIdx);
                break;
            case 201:
                currentLocation = new Hrodz(this, currentLocationIdx);
                break;
            case 202:
                currentLocation = new Rowy(this, currentLocationIdx);
                break;
            case 203:
                currentLocation = new Doma4(this);
                break;
            default:
                log.error("Falsche Location-ID fuer Konstruktor! newLocation: {}", newLocation);
        }
        currentLocationIdx = newLocation;

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
                currentLocation = new Labyr1(this, Richtung);
                break;
            case 52:
                currentLocation = new Labyr2(this, Richtung);
                break;
            case 53:
                currentLocation = new Labyr3(this, Richtung);
                break;
            case 54:
                currentLocation = new Labyr4(this, Richtung);
                break;
            case 55:
                currentLocation = new Labyr5(this, Richtung);
                break;
            case 56:
                currentLocation = new Labyr6(this, Richtung);
                break;
            case 57:
                currentLocation = new Labyr7(this, Richtung);
                break;
            case 58:
                currentLocation = new Labyr8(this, Richtung);
                break;
            case 59:
                currentLocation = new Labyr9(this, Richtung);
                break;
            case 60:
                currentLocation = new Labyr10(this, Richtung);
                break;
            case 61:
                currentLocation = new Labyr11(this, Richtung);
                break;
            case 62:
                currentLocation = new Labyr12(this, Richtung);
                break;
            default:
                log.error("Not available! newLocation: {}", newLocation);
        }
        currentLocationIdx = newLocation;

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

        currentLocation = new Doma1(this, currentLocationIdx, gans1, gans2, gans3);

        currentLocationIdx = newLocation;

        NoPaint(false);
    }

    public synchronized final void keyPressed(GenericKeyEvent e) {
        if (!isListenerActive || !isWindowActive) {
            return;
        }

        // Zum Debuggen hier Krabat-Groesse einstellbar
        // Nur auf Funktionstasten reagieren
        int key = e.getKeyCode();

        // Bei aktivem Exit dorthin - Prio 1
        if (exitGame.active) {
            exitGame.evalKeyEvent(e);
            return;
        }

        // Feststellen, ob das Woerterbuch aktiviert werden soll
        if (whatScreen == ScreenType.NONE) {
            if (key == GenericKeyEvent.VK_F5) {
                ConstructLocation(107);
                whatScreen = ScreenType.DICTIONARY;
                isClipSet = false;
                repaint();
            }
        }

        // bei aktivem Inventar,Load,Save,Credits,Hauptmenu hierher umleiten - Prioritaet 2
        if (whatScreen != ScreenType.NONE) {
            switch (whatScreen) {
                case INVENTORY:
                    inventory.evalKeyEvent(e);
                    break;
                case MAIN_MENU:
                    mainMenu.evalKeyEvent(e);
                    break;
                case LOAD_GAME:
                    loadGame.evalKeyEvent(e);
                    break;
                case SAVE_GAME:
                    saveGame.evalKeyEvent(e);
                    break;
                case CREDITS:
                    credits.evalKeyEvent(e);
                    break;
                case MAP:
                    map.evalKeyEvent(e);
                    break;
                case DICTIONARY:
                    dictionary.evalKeyEvent(e);
                    break;
                case SKETCH:
                    sketch.evalKeyEvent(e);
                    break;
                default:
                    log.error("Wrong Keyevent Prio 2! whatScreen: {}", whatScreen);
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
        tempGraphics.drawImage(offImage, -scrollX, -scrollY);
        saveImage = tempImage.getScaledInstance(119, 89, GenericImage.SCALE_DEFAULT);
    }

    // Programmneustart
    public void restart() {
        DestructLocation(currentLocationIdx);
        initGame();
        inventory.ResetInventory();
        ConstructLocation(100);
        repaint();
    }

    // Alle relevanten Listener fuer Mousepress und Mousemove deaktivieren
    public void Freeze(boolean cold) {
        if (cold) {
            setCursor(cursorWait);
            isListenerActive = false;
        } else {
            isListenerActive = true;
        }
    }

    // Repaint - Schleife anhalten, verhindert Paint-Anhaeufungen
    public void NoPaint(boolean haltan) {
        stopPaint = haltan; // true = stop, false = weiter
    }

    // diese Methode castet den richtigen Spieler, je nach Action - Array
    public void CheckKrabat() {
        // 850 und 851 = false -> Krabat ist ganz normal
        // 850 = true          -> Krabat von Seite mit Drasta
        // 851 = true          -> Krabat von Oben
        // Hilfsvariable verhindert staendiges Laden...

        // alte Krabatvariablen merken, die beim Umladen weg waeren
        int merkFacing = krabat.GetFacing();
        GenericPoint merkPos = krabat.getPos();
        int merkMaxx = krabat.maxx;
        float merkZoom = krabat.zoomf;
        int merkDef = krabat.defScale;

        // normales Aussehen
        if (!actions[850] && !actions[851]) {
            if (krabatShape != 1) {
                krabat = new KrabatNormal(this);
                krabatShape = 1;
            }
        }

        // hat sich Drasta angezogen
        if (actions[850]) {
            if (krabatShape != 2) {
                krabat = new KrabatDrasta(this);
                krabatShape = 2;
            }
        }

        // ist von oben zu sehen
        if (actions[851]) {
            if (krabatShape != 3) {
                krabat = new KrabatOben(this);
                krabatShape = 3;
            }
        }

        // Krabatvariablen wiederherstellen
        krabat.SetFacing(merkFacing);
        krabat.setPos(merkPos);
        krabat.maxx = merkMaxx;
        krabat.zoomf = merkZoom;
        krabat.defScale = merkDef;

    }

    public GenericImage constructCursorImage(String Pathname) {
        return imageFetcher.fetchImage(Pathname, false);
    }
}
