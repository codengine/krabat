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

import de.codengine.main.GenericKeyEvent;
import de.codengine.main.GenericMouseEvent;
import de.codengine.main.GenericPoint;
import de.codengine.main.GenericToolkit;
import de.codengine.platform.*;
import de.codengine.platform.java.*;
import de.codengine.sound.AbstractPlayer;

import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationStart extends Frame implements WindowListener, MouseListener, MouseMotionListener, KeyListener {
    private final Start appInstance;

    private GenericCursor Cup, Cdown, Cleft, Cright, Cinventar, CHinventar, Normal, Kreuz, Warten, Nix;

    private Image iconImage;

    private Point Mousetemp;

    private boolean dClick;

    private long timeskip;

    private static final int doubleClickPointLimit = 5;

    private static final long doubleClickTimeLimit = 500;

    public ApplicationStart(int defaultLanguageIndex, boolean fullScreen) {
        super("Krabat");
        appInstance = new Start();
        startGame(fullScreen);

        Path workingDir = Paths.get(System.getProperty("user.dir"));

        GenericImageFetcher imageFetcher = new JavaImageFetcher(workingDir, this);
        GenericImageObserver observer = new JavaImageObserver(this);
        GenericContainer container = new JavaContainer(this);
        GenericSoundEffectPlayer player = new JavaSoundEffectPlayer(workingDir.resolve("sound"));
        GenericToolkit.impl = new JavaToolkitImpl(this);
        AbstractPlayer musicPlayer = new OGGPlayer(workingDir);
        Path resourcePath = workingDir.resolve("resource");
        GenericStorageManager storageManager = new JavaStorageManager(
                true, workingDir.resolve("hry"), "krabat", ".hra",
                true, resourcePath,
                true, resourcePath,
                resourcePath);

        appInstance.runGamePt1(defaultLanguageIndex, imageFetcher, observer, container, player, musicPlayer, storageManager);
        GenericPoint pt = InitImages(imageFetcher);
        setIconImage(iconImage);
        appInstance.runGamePt2(pt, new GenericCursor[]{Cup, Cdown, Cleft, Cright, Cinventar, CHinventar, Normal, Kreuz, Warten, Nix});

        Mousetemp = new Point(0, 0);
        dClick = false;
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
                System.err.println("Language index string :'" + args[0] + "' not recognized!");
                tmp = 0;
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

    private void startGame(boolean fullScreen) {
        setResizable(false);
        setBackground(Color.black);
        setSize(640, 480);

        // Default-Display und akuellen Modus ermitteln
        GraphicsEnvironment gfxEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice gfxDevice = gfxEnv.getDefaultScreenDevice();
        final DisplayMode dmCurrent = gfxDevice.getDisplayMode();

        // Vollbild aktivieren wenn erwünscht und unterstützt
        if (fullScreen && gfxDevice.isFullScreenSupported()) {

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
            int scrollx = appInstance.scrollx;
            int scrolly = appInstance.scrolly;
            g.drawImage(javaImg, -scrollx, -scrolly, null);
        }
    }

    // Cursorbilder vorbereiten
    private GenericPoint InitImages(GenericImageFetcher fetcher) {
        GenericImage Ccup = null;
        GenericImage Ccdown = null;
        GenericImage Ccleft = null;
        GenericImage Ccright = null;
        GenericImage NNormal = null;
        GenericImage KKreuz = null;
        GenericImage WWarten = null;
        GenericImage NNix = null;

        Ccup = fetcher.fetchImage("gfx/cursors/horje.gif");
        Ccdown = fetcher.fetchImage("gfx/cursors/dele.gif");
        Ccleft = fetcher.fetchImage("gfx/cursors/nalewo.gif");
        Ccright = fetcher.fetchImage("gfx/cursors/naprawo.gif");
        NNormal = fetcher.fetchImage("gfx/cursors/bezec4.gif");
        KKreuz = fetcher.fetchImage("gfx/cursors/bezec10.gif");
        WWarten = fetcher.fetchImage("gfx/cursors/cakac.gif");
        NNix = fetcher.fetchImage("gfx/cursors/trans.gif");
        iconImage = ((JavaImage) fetcher.fetchImage("gfx/k-icon.gif")).getImage();

        // Cursorgroesse fuer jeweiliges System bestimmen
        Dimension cursor = getToolkit().getBestCursorSize(32, 32);
        double x = cursor.getWidth();
        double y = cursor.getHeight();
        // System.out.println("Cursorgroesse : " + x + " " + y);

        int xxx = (int) x / 2;
        int yyy = (int) y / 2;
        // System.out.println ("Anpassung auf " + xxx + " " + yyy); 

        // Mauscursor initialisieren
        Cup = GenericToolkit.getDefaultToolkit().createCustomCursor(Ccup, new GenericPoint(xxx, yyy), "Up");
        Cdown = GenericToolkit.getDefaultToolkit().createCustomCursor(Ccdown, new GenericPoint(xxx, yyy), "Down");
        Cleft = GenericToolkit.getDefaultToolkit().createCustomCursor(Ccleft, new GenericPoint(xxx, yyy), "Left");
        Cright = GenericToolkit.getDefaultToolkit().createCustomCursor(Ccright, new GenericPoint(xxx, yyy), "Right");
        Normal = GenericToolkit.getDefaultToolkit().createCustomCursor(NNormal, new GenericPoint(xxx, yyy), "Normal");
        Kreuz = GenericToolkit.getDefaultToolkit().createCustomCursor(KKreuz, new GenericPoint(xxx, yyy), "Kreuz");
        Warten = GenericToolkit.getDefaultToolkit().createCustomCursor(WWarten, new GenericPoint(xxx, yyy), "Warten");
        Nix = GenericToolkit.getDefaultToolkit().createCustomCursor(NNix, new GenericPoint(xxx, yyy), "Nix");

        return (new GenericPoint(xxx, yyy));
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
        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getModifiers(), point, false);
        appInstance.mouseExited(ge);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Doppelclick (zeitlich begrenzt) erkennen
        dClick = (Math.abs(Mousetemp.x - e.getPoint().x) < doubleClickPointLimit) &&
                (Math.abs(Mousetemp.y - e.getPoint().y) < doubleClickPointLimit) &&
                (!dClick) && ((System.currentTimeMillis() - timeskip) < doubleClickTimeLimit);
        timeskip = System.currentTimeMillis();
        Mousetemp = e.getPoint();

        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getModifiers(), point, dClick);
        appInstance.mousePressed(ge);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getModifiers(), point, false);
        appInstance.mouseDragged(ge);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        GenericPoint point = new GenericPoint(e.getPoint().x, e.getPoint().y);
        GenericMouseEvent ge = new GenericMouseEvent(
                e.getModifiers(), point, false);
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
        appInstance.isWindowactive = true;
    }

    @Override
    public void windowIconified(WindowEvent event) {
        appInstance.isWindowactive = false;
    }

    @Override
    public void windowActivated(WindowEvent event) {
        appInstance.isWindowactive = true;
    }

    @Override
    public void windowDeactivated(WindowEvent event) {
        appInstance.isWindowactive = false;
    }

    @Override
    public void windowOpened(WindowEvent event) {
    }

    @Override
    public void windowClosing(WindowEvent event) {
        appInstance.windowClosing();
    }
}
