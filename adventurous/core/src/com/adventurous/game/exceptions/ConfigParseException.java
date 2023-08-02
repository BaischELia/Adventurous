package com.adventurous.game.exceptions;

/**
 * Exception thrown when there is an error parsing a configuration file.
 */
public class ConfigParseException extends Exception{
    public ConfigParseException(String errorMessage) {
        super(errorMessage);
    }
}
