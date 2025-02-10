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

package de.codengine.sound;

import de.codengine.Start;
// import rapaki.krabat.platform.java.OGGPlayer;
// import rapaki.krabat.platform.android.DummyPlayer;

/**
 * Klasse zum Abspielen der Krabat-Hintergrundmusik (Wiedergabe von MP3-Dateien). Die Benutzung
 * erfolgt als Singleton, das Abspielen wird stets im Hintergrund-Thread gestartet.
 *
 * @author Stefan Saring
 */
public class BackgroundMusicPlayer {

    private static final String FILE_PREFIX = "titel";

    private static BackgroundMusicPlayer instance;
    private final AbstractPlayer player;

    private BackgroundMusicPlayer(AbstractPlayer player) {
        // player = new MP3Player (urlBase);
        this.player = player;
        // player = new DummyPlayer(urlBase);
    }

    /**
     * Liefert die einzige MusicPlayer-Instanz.
     *
     * @return MusicPlayer
     */
    public static BackgroundMusicPlayer getInstance() {
        if (instance == null) {
            // TODO yes this is dirty!!!!
            instance = new BackgroundMusicPlayer(Start.player);
        }
        return instance;
    }

    /**
     * Spielt den Track mit der angegebenen Nummer in einem Hintergrund-Thread ab. Eine
     * evtl. bereits laufende Wiedergabe wird zunächst gestoppt.
     *
     * @param trackNumber Nr des abzuspielenden Tracks
     * @param repeat      Flag, ob der Track im Endlos-Modus wiederholt werden soll
     */
    public void playTrack(int trackNumber, boolean repeat) {
        // 1 abziehen, weil die ursprünglichen CD-Tracks mit 2 anfingen (Nr. 1 waren Daten)
        // => die MP3-Tracks fangen aber mit 1 an
        player.play(getFilename(trackNumber - 1), repeat);
    }

    public void pause() {
        player.pause();
    }

    public void resume() {
        player.resume();
    }

    /**
     * Beendet eine evtl. laufende Wiedergabe.
     */
    public void stop() {
        player.stop();
    }

    private String getFilename(int trackNumber) {
        StringBuilder sb = new StringBuilder(FILE_PREFIX);
        if (trackNumber < 10) {
            sb.append("0");
        }
        sb.append(trackNumber).append(player.getMusicSuffix());
        return sb.toString();
    }
}
