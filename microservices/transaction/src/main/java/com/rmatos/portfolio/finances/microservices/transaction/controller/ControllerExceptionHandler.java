package com.rmatos.portfolio.finances.microservices.transaction.controller;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.rmatos.portfolio.finances.microservices.transaction.exception.MicroserviceGenericException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MicroserviceGenericException.class)
    public ResponseEntity handleResourceNotFound(MicroserviceGenericException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingParameters(MissingServletRequestParameterException ex) {
        String errorMsg = ex.getParameterName() + " parameter is missing";
        return ResponseEntity.unprocessableEntity().body(errorMsg);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity handleMissingParameters(InvalidFormatException ex) {
        String errorMsg = "Invalid JSON: " + ex.getMessage();
        return ResponseEntity.unprocessableEntity().body(errorMsg);
    }
}
