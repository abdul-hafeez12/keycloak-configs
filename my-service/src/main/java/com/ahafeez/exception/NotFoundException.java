package com.ahafeez.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {
    public NotFoundException(String msg) {
        super(msg, Response.Status.NOT_FOUND);
    }
}
