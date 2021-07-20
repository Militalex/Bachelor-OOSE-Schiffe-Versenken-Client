package de.gitlab.kaisoenmalex.api.util;

import java.io.IOException;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This Exception is thrown if code send to server is invalid.
 */
public class CodeInvalidException extends IOException {
    public CodeInvalidException() {
    }

    public CodeInvalidException(String message) {
        super(message);
    }

    public CodeInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeInvalidException(Throwable cause) {
        super(cause);
    }
}
