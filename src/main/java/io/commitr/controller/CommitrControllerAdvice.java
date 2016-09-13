package io.commitr.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by peter on 9/13/16.
 */
@ControllerAdvice
public class CommitrControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(CommitrControllerAdvice.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "{uuid.format.invalid}")
    @ExceptionHandler(com.fasterxml.jackson.databind.exc.InvalidFormatException.class)
    public void handleInvalidUUIDFormat() {
        logger.info("Bad UUID was posted to /goal");
    }
}
