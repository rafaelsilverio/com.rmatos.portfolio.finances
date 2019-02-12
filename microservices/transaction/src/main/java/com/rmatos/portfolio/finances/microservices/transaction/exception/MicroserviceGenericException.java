package com.rmatos.portfolio.finances.microservices.transaction.exception;

import org.springframework.http.HttpStatus;

public class MicroserviceGenericException extends RuntimeException {
    private HttpStatus statusCode;

    public MicroserviceGenericException(String msg, HttpStatus statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
