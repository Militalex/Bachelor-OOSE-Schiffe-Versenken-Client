package de.gitlab.kaisoenmalex.api.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Alexander Ley
 * @version 1.0
 * Represents an Object with Timer
 */
public abstract class Tickable {
    protected Timer timer = new Timer();
    protected TimerTask task = new TimerTask() {
        @Override
        public void run() {
            tick();
        }
    };

    /**
     * Tick Loop
     */
    public abstract void tick();

    /**
     * Starts the Timer
     * @param delay Start delay.
     * @param period Timer period.
     */
    public void start(long delay, long period){
        timer.schedule(task, delay, period);
    }

    /**
     * Stops the Timer and makes the timer ready for next start.
     */
    public void stop() {
        timer.cancel();
    }

    /**
     * Refreshes timer object, so that it can start again. Note Refreshing timer lets application open.
     */
    public void refresh(){
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        };
    }
}
