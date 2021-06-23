package com.oklimenko.shorteninglink.exceptions;

/**
 * Thrown if long URL has incorrect format.
 */
public class LongUrlInvalidException extends RuntimeException {

    /**
     * Thrown if long URL has incorrect format.
     * @param message description.
     */
    public LongUrlInvalidException(String message) {
        super(message);
    }

}
