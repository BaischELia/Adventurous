package com.adventurous.game.exceptions;

/**
 * Exception thrown when the TMX map has an incompatible format.
 */
public class TmxParseError extends Error {

    public TmxParseError(String errorMessage) {
        super(errorMessage);
    }

}
