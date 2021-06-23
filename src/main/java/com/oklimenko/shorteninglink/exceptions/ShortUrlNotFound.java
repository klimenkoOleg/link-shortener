package com.oklimenko.shorteninglink.exceptions;

/**
 * Thrown if no long URL found in storage by Key.
 */
public class ShortUrlNotFound extends RuntimeException {

    /**
     * Thrown if no long URL found in storage by Key.
     * @param message description.
     */
    public ShortUrlNotFound(String message) {
        super(message);
    }

}
