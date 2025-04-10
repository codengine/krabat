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

import de.codengine.krabat.main.GenericKeyEvent;
import de.codengine.krabat.main.GenericMouseEvent;
import de.codengine.krabat.main.GenericPoint;
import de.codengine.krabat.main.GenericToolkit;
import de.codengine.krabat.platform.*;
import de.codengine.krabat.platform.java.*;
import de.codengine.krabat.sound.AbstractPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationStart extends Frame implements WindowListener, MouseListener, MouseMotionListener, KeyListener {
    private static final Logger log = LoggerFactory.getLogger(ApplicationStart.class);
    private final Start appInstance;

    private GenericCursor cursorUp;
    private GenericCursor cursorDown;
    private GenericCursor cursorLeft;
    private GenericCursor cursorRight;
    private GenericCursor cursorNormal;
    private GenericCursor cursorCross;
    private GenericCursor cursorWait;
    private GenericCursor cursorNone;

    private Image iconImage;

    private Point mouseTemp;

    private boolean isDoubleClick;

    private long timeskip;

    private static final int doubleClickPointLimit = 5;

    private static final long doubleClickTimeLimit = 500;

    public ApplicationStart(int defaultLanguageIndex, boolean fullscreen) {
        super("Krabat");
        appInstance = new Start();
        startGame(fullscreen);

        Path workingDir = Paths.get(System.getProperty("user.dir"));

        Path langPath = workingDir.resolve("lang");
        GenericImageFetcher imageFetcher = new JavaImageFetcher(workingDir, langPath,this);
        GenericContainer container = new JavaContainer(this);
        GenericSoundEffectPlayer player = new JavaSoundEffectPlayer(workingDir);
        GenericToolkit.impl = new JavaToolkitImpl(this);
        AbstractPlayer musicPlayer = new OGGPlayer(workingDir);
        Path resourcePath = workingDir.resolve("resource");
        GenericStorageManager storageManager = new JavaStorageManager(
                true, workingDir.resolve("hry"), "krabat", ".hra",
                true, resourcePath,
                true, resourcePath,
                langPath);

        appInstance.runGamePt1(defaultLanguageIndex, imageFetcher, container, player, musicPlayer, storageManager);
        GenericPoint pt = initImages(imageFetcher);
        setIconImage(iconImage);
        appInstance.runGamePt2(pt, new GenericCursor[]{cursorUp, cursorDown, cursorLeft, cursorRight, cursorNormal, cursorCross, cursorWait, cursorNone});

        mouseTemp = new Point(0, 0);
        isDoubleClick = false;
        timeskip = System.currentTimeMillis();

        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        addWindowListener(this);
        setVisible(true);
        appInstance.runGamePt3();
    }

    // Programmstart
    public static void main(String[] args) {
        int rec = 1;        // Default ist Obersorbisch (hs, ds oder de)
        boolean fullScreen = false;  // Default is Vollbild (im Fenster, wenn 2. Parameter = "win")

        if (args.length >= 1) {
            int tmp = 0;
            try {
                tmp = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                log.error("Language index string :'{}' not recognized!", args[0]);
            }
            if (tmp > 0) {
                rec = tmp;
            }
        }
        if (args.length >= 2) {
            fullScreen = !args[1].equals("win");
        }

        new ApplicationStart(rec, fullScreen);
    }

    private void startGame(boolean fullscreen) {
        setResizable(false);
        setBackground(Color.black);
        setSize(640, 480);

        // Default-Display und akuellen Modus ermitteln
        GraphicsEnvironment gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gfxDevice = gfxEnv.getDefaultScreenDevice();
        final DisplayMode dmCurrent = gfxDevice.getDisplayMode();

        // Vollbild aktivieren wenn erwünscht und unterstützt
        if (fullscreen && gfxDevice.isFullScreenSupported()) {

            // Window-Dekorationen entfernen
            this.setLocation(0, 0);
            this.setUndecorated(true);

            // Fenster im Vollbild anzeigen
            gfxDevice.setFullScreenWindow(this);

            // Modus 640x480 setzen, aktuelle Farbtiefe beibehalten
            gfxDevice.setDisplayMode(new DisplayMode(640, 480,
                    dmCurrent.getBitDepth(), DisplayMode.REFRESH_RATE_UNKNOWN));
        }
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public final synchronized void paint(Graphics g) {
        GenericImage img = appInstance.paint(null);
        if (img != null) {
            Image javaImg = ((JavaImage) img).getImage();
            int scrollx = appInstance.scrollX;
            int scrolly = appInstance.scrollY;
            g.drawImage(javaImg, -scrollx, -scrolly, null);
        }
    }

    // Cursorbilder vorbereiten
    private GenericPoint initImages(GenericImageFetcher fetcher) {
        GenericImage imgCursorUp;
        GenericImage imgCursorDown;
        GenericImage imgCursorLeft;
        GenericImage imgCursorRight;
        GenericImage imgCursorNormal;
        GenericImage imgCursorCross;
        GenericImage imgCursorWait;
        GenericImage imgCursorNone;

        imgCursorUp = fetcher.fetchImage("gfx/cursors/horje.png", false);
        imgCursorDown = fetcher.fetchImage("gfx/cursors/dele.png", false);
        imgCursorLeft = fetcher.fetchImage("gfx/cursors/nalewo.png", false);
        imgCursorRight = fetcher.fetchImage("gfx/cursors/naprawo.png", false);
        imgCursorNormal = fetcher.fetchImage("gfx/cursors/bezec4.png", false);
        imgCursorCross = fetcher.fetchImage("gfx/cursors/bezec10.png", false);
        imgCursorWait = fetcher.fetchImage("gfx/cursors/cakac.png", false);
        imgCursorNone = fetcher.fetchImage("gfx/cursors/trans.png", false);
        iconImage = ((JavaImage) fetcher.fetchImage("gfx/k-icon.png", false)).getImage();

        // Cursorgroesse fuer jeweiliges System bestimmen
        Dimension cursor = getToolkit().getBestCursorSize(32, 32);
        double x = cursor.getWidth();
        double y = cursor.getHeight();

        int xxx = (int) x / 2;
        int yyy = (int) y / 2;

        // Mauscursor initialisieren
        cursorUp = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorUp, new GenericPoint(xxx, yyy), "Up");
        cursorDown = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorDown, new GenericPoint(xxx, yyy), "Down");
        cursorLeft = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorLeft, new GenericPoint(xxx, yyy), "Left");
        cursorRight = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorRight, new GenericPoint(xxx, yyy), "Right");
        cursorNormal = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorNormal, new GenericPoint(xxx, yyy), "Normal");
        cursorCross = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorCross, new GenericPoint(xxx, yyy), "Kreuz");
        cursorWait = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorWait, new GenericPoint(xxx, yyy), "Warten");
        cursorNone = GenericToolkit.getDefaultToolkit().createCustomCursor(imgCursorNone, new GenericPoint(xxx, yyy), "Nix");

        return new GenericPoint(xxx, yyy);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        appInstance.mouseEntered();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        appInstance.mouseExited();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Doppelclick (zeitlich begrenzt) erkennen
        isDoubleClick = Math.abs(mouseTemp.x - e.getPoint().x) < doubleClickPointLimit &&
                Math.abs(mouseTemp.y - e.getPoint().y) < doubleClickPointLimit &&
                !isDoubleClick && System.currentTimeMillis() - timeskip < doubleClickTimeLimit;
        timeskip = System.currentTimeMillis();
        mouseTemp = e.getPoint();

        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getButton(), e.getModifiersEx(), point, isDoubleClick);
        appInstance.mousePressed(ge);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getButton(), e.getModifiersEx(), point, false);
        appInstance.mouseDragged(ge);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getButton(), e.getModifiersEx(), point, false);
        appInstance.mouseMoved(ge);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        appInstance.keyPressed(new GenericKeyEvent(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // Window-Events abfangen
    @Override
    public void windowClosed(WindowEvent event) {
    }

    @Override
    public void windowDeiconified(WindowEvent event) {
        appInstance.isWindowActive = true;
    }

    @Override
    public void windowIconified(WindowEvent event) {
        appInstance.isWindowActive = false;
    }

    @Override
    public void windowActivated(WindowEvent event) {
        appInstance.isWindowActive = true;
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
        appInstance.isWindowActive = false;
    }

    @Override
    public void windowOpened(WindowEvent event) {
    }

    @Override
    public void windowClosing(WindowEvent event) {
        appInstance.windowClosing();
    }
}
