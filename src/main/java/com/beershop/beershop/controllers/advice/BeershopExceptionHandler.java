package com.beershop.beershop.controllers.advice;

import com.beershop.beershop.exptions.NoAvailableSectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class BeershopExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound (EntityNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .timestemp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleNoSuchElementException (NoSuchElementException ex) {
        ApiError apiError = ApiError.builder()
                .timestemp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .message("None value was found for this request")
                .build();

        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NoAvailableSectionException.class)
    protected ResponseEntity<Object> handleNoAvailableSectionException (NoAvailableSectionException ex) {
        ApiError apiError = ApiError.builder()
                .timestemp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN)
                .message(ex.getMessage())
                .build();

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        log.error("[ControllerAdvice] Throwing error with status {}", apiError.getStatus());

        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
