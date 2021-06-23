package com.oklimenko.shorteninglink.exceptions.handling;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * DTO for error description, used in REST responses.
 */
@Data
public class HttpErrorDto {

    private String message;

    private String code;

    private HttpStatus httpStatus;

    private String path;

    private ZonedDateTime timestamp;

    /**
     * Stores NOW time in DTO.
     * @param message
     * @param code
     * @param httpStatus
     * @param path
     */
    public HttpErrorDto(String message, String code, HttpStatus httpStatus, String path) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
        this.path = path;
        timestamp = ZonedDateTime.now();
    }
}
