package com.example;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.ResteasyReactiveClientProblem;

public class CustomWebClientApplicationException extends WebApplicationException implements ResteasyReactiveClientProblem {

    public CustomWebClientApplicationException() {
        super();
    }

    public CustomWebClientApplicationException(final String message) {
        super(message, null, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public CustomWebClientApplicationException(final Response response) {
        super((Throwable) null, response);
    }

    public CustomWebClientApplicationException(final String message, final Response response) {
        super(message, null, response);
    }

    public CustomWebClientApplicationException(final int status) {
        super((Throwable) null, status);
    }

    public CustomWebClientApplicationException(final String message, final int status) {
        super(message, null, status);
    }

    public CustomWebClientApplicationException(final Response.Status status) {
        super((Throwable) null, status);
    }

    public CustomWebClientApplicationException(final String message, final Response.Status status) {
        super(message, null, status);
    }

    public CustomWebClientApplicationException(final Throwable cause) {
        super(cause, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public CustomWebClientApplicationException(final String message, final Throwable cause) {
        super(message, cause, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public CustomWebClientApplicationException(final Throwable cause, final Response response) {
        super(cause, response);
    }

    public CustomWebClientApplicationException(final String message, final Throwable cause, final Response response) {
        super(message, cause, response);
    }

    public CustomWebClientApplicationException(final Throwable cause, final int status) {
        super(cause, Response.status(status).build());
    }

    public CustomWebClientApplicationException(final String message, final Throwable cause, final int status) {
        super(message, cause, Response.status(status).build());
    }

    public CustomWebClientApplicationException(final Throwable cause, final Response.Status status) {
        super(cause, Response.status(status).build());
    }

    public CustomWebClientApplicationException(final String message, final Throwable cause,
            final Response.Status status) {
        super(message, cause, Response.status(status).build());
    }
}
