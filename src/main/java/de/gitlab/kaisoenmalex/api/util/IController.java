package de.gitlab.kaisoenmalex.api.util;

/**
 * @author Alexander Ley
 * @version 1.0
 *
 * This Interface wraps all Controller into one Reference Type and Returns the actual object.
 */
public interface IController<T> {
    T getInstance();
}
