package de.gitlab.kaisoenmalex.api.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * This class holds information about application sound system and offers methods to control them.
 *
 * @author Alexander Ley
 * @version 1.0
 */
public class SoundUtil {

    public static double baseVolume = 0.5;
    public static MediaPlayer player;
    public static boolean lobbyTime = true;

    /**
     * Plays Lobby music.
     */
    public static void playLobby(){
        if (!lobbyTime) {
            fadeIntoSound("/sounds/lobby.mp3", 5);
            lobbyTime = true;
        }
    }

    /**
     * Plays Hitman music.
     */
    public static void playHitman(){
        if (lobbyTime) {
            fadeIntoSound("/sounds/hitman.mp3", 5);
            lobbyTime = false;
        }
    }

    /**
     * Cross-fade between two sounds. Old sound will be faded out. Creates a new MusicPlayer using path.
     * @param path new sound to fade into (loaded via classloader).
     * @param time how long in seconds it should need to be complete.
     */
    public static void fadeIntoSound(String path, int time){
        fadeOutSound(player, time);
        fadeInSound(path, time);
    }

    /**
     * Creates a new player using path, which is faded out and closed and the end.
     * @param path new sound to fade out (loaded via classloader).
     * @param time how long in seconds it should need to be complete.
     */
    public static void fadeOutSound(String path, int time){
        fadeSound(path, time, baseVolume, 0);
    }

    /**
     * Fades player out and close it at the end.
     * @param time how long in seconds it should need to be complete.
     */
    public static void fadeOutSound(MediaPlayer player, int time){
        fadeSound(player, time, baseVolume, 0);
    }

    /**
     * Creates a new player using path, which is faded in.
     * @param path new sound to fade in (loaded via classloader).
     * @param time how long in seconds it should need to be complete.
     */
    public static void fadeInSound(String path, int time){
        fadeSound(path, time, 0, baseVolume);
    }

    /**
     * Fades player in.
     * @param time how long in seconds it should need to be complete.
     */
    public static void fadeInSound(MediaPlayer player, int time){
        fadeSound(player, time, 0, baseVolume);
    }

    /**
     * Fades a sound to any volume.
     * Creates a new player using path.
     * If start and end are equal path will only play without fade with end volume.
     * @param path path new sound to fade (loaded via classloader).
     * @param time how long in seconds it needs to reach end.
     * @param start start/initial volume (between 0.0 and 1.0).
     * @param end end volume (between 0.0 and 1.0).
     */
    public static void fadeSound(String path, int time, double start, double end) {
        player = new MediaPlayer(new Media(Objects.requireNonNull(FxUtils.class.getResource(path)).toExternalForm()));
        if (NumberUtil.equals(start, end)) {
            if (player.getStatus() != MediaPlayer.Status.PLAYING) player.play();
            player.setVolume(end);
            return;
        }

        new FadeSound(player, time, start, end);
    }

    /**
     * Fades a sound to any volume.
     * If start and end are equal path will only play without fade with end volume.
     * @param time how long in seconds it needs to reach end.
     * @param start start/initial volume (between 0.0 and 1.0).
     * @param end end volume (between 0.0 and 1.0).
     */
    public static void fadeSound(MediaPlayer player, int time, double start, double end) {
        if (NumberUtil.equals(start, end)) {
            if (player.getStatus() != MediaPlayer.Status.PLAYING) player.play();
            return;
        }
        new FadeSound(player, time, start, end);
    }
}
