package de.gitlab.kaisoenmalex.api.util;

import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This class can fade sounds to any volumes.
 */
public class FadeSound extends Tickable{

    /**
     * List of all running fading actions.
     */
    public static List<FadeSound> runningFades = new ArrayList<>();

    private final MediaPlayer player;
    private final double end;

    private final double step;
    /**
     * Actual volume which count up continuously.
     */
    private double volume;

    /**
     * @param time how long in seconds it needs to reach end.
     * @param start start/initial volume (between 0.0 and 1.0).
     * @param end end volume (between 0.0 and 1.0).
     * @throws IllegalArgumentException if start and end are equal.
     */
    public FadeSound(MediaPlayer player, int time, double start, double end) {
        if (NumberUtil.equals(start, end)) {
            throw new IllegalArgumentException("Start and end are too equal.");
        }

        this.player = player;
        this.end = end;
        this.volume = start;

        //Calculate steps
        step = ((end - start) / time) /4;

        start(0, 250);

        player.setVolume(start);
        player.setCycleCount(Integer.MAX_VALUE);

        if (player.getStatus() != MediaPlayer.Status.PLAYING) player.play();

        runningFades.add(this);
    }

    /**
     * Add step to volume until end is reached.
     */
    @Override
    public void tick() {
        if ((step < 0 && volume <= end) || (step > 0 && volume >= end)){
            player.setVolume(end);
            forceStop();
            return;
        }

        player.setVolume(volume += step);
    }

    /**
     * Lightweight stop by setting volume to stop.
     */
    @Override
    public void stop() {
        volume = end;
    }

    /**
     * Stops timer without removing it from runningFades.
     */
    private void stopWithoutClear(){
        super.forceStop();
    }

    /**
     * Stops timer and removes it from runningFades.
     */
    @Override
    public void forceStop() {
        super.forceStop();
        runningFades.remove(this);
    }

    /**
     * Stops all runningFades and clears it.
     */
    public static void clearFades(){
        runningFades.forEach(fadeSound -> {
            fadeSound.player.stop();
            fadeSound.stopWithoutClear();
        });
        runningFades.clear();
    }
}
