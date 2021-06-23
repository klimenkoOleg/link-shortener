package com.oklimenko.shorteninglink.exceptions.handling;

import com.oklimenko.shorteninglink.exceptions.LongUrlInvalidException;
import com.oklimenko.shorteninglink.exceptions.ShortUrlNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

/**
 * REST exception handler to map business exceptions with the proper HTTP Response.
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Thrown if long URL has incorrect format.
     *
     * @param request   standard ServerHttpRequest.
     * @param exception incoming Exception.
     * @return DTO with error description for REST response.
     */
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(LongUrlInvalidException.class)
    @ResponseBody
    public HttpErrorDto handleInvalidInputException(ServerHttpRequest request, Exception exception) {
        return response(UNPROCESSABLE_ENTITY, request, exception);
    }

    /**
     * Thrown if no long URL found in storage by Key.
     *
     * @param request   standard ServerHttpRequest.
     * @param exception incoming Exception.
     * @return DTO with error description for REST response.
     */
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ShortUrlNotFound.class)
    @ResponseBody
    public HttpErrorDto handlePostgreDatabaseException(ServerHttpRequest request, Exception exception) {
        return response(NOT_FOUND, request, exception);
    }

    private HttpErrorDto response(HttpStatus httpStatus, ServerHttpRequest request, Exception exception) {
        return response(httpStatus, null, request, exception);
    }

    private HttpErrorDto response(HttpStatus httpStatus, String code, ServerHttpRequest request, Exception exception) {
        final String path = request.getPath().pathWithinApplication().value();
        final String message = exception.getMessage();
        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
        return new HttpErrorDto(message, code, httpStatus, path);
    }
}
