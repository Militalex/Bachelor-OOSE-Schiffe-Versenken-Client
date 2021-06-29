package de.gitlab.kaisoenmalex.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This interface give classes the functionality to use a logger.
 */
public interface ILogger {

    default Logger getLogger(){
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Shows a Error window with String msg.
     */
    default void showError(String msg){
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
