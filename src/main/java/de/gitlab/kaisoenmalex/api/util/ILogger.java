package de.gitlab.kaisoenmalex.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JOptionPane;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This interface give classes the functionality to use a logger.
 */
public interface ILogger{

    /**
     * Offers a Logger for static classes.
     */
    Logger STATIC_LOGGER = LoggerFactory.getLogger("Static Logger");

    default Logger getLogger(){
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Shows a Error window with String msg.
     */
    static void showError(String msg){
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
