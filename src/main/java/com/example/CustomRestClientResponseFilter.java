package com.example;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientResponseContext;
import jakarta.ws.rs.client.ClientResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.StatusType;
import java.io.IOException;
import org.jboss.resteasy.reactive.client.handlers.ClientResponseCompleteRestHandler;
import org.jboss.resteasy.reactive.client.impl.ClientRequestContextImpl;
import org.jboss.resteasy.reactive.client.impl.ClientResponseImpl;
import org.jboss.resteasy.reactive.client.impl.RestClientRequestContext;
import org.jboss.resteasy.reactive.common.core.UnwrappableException;
import org.jboss.resteasy.reactive.common.jaxrs.ResponseImpl;
import org.jboss.resteasy.reactive.common.jaxrs.StatusTypeImpl;

public class CustomRestClientResponseFilter implements ClientResponseFilter {

    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        RestClientRequestContext restClientReqContext = ((ClientRequestContextImpl) requestContext)
                .getRestClientRequestContext();

        if (responseContext.getStatus() >= 400 && restClientReqContext.isCheckSuccessfulFamily()) {
            ResponseImpl response = ClientResponseCompleteRestHandler.mapToResponse(restClientReqContext, false);

            try {
                response.bufferEntity();
            } catch (Exception ignored) {
                // Ignore this error.
            }

            StatusType effectiveResponseStatus = determineEffectiveResponseStatus(restClientReqContext,
                    restClientReqContext.getOrCreateClientRequestContext());
            if (Response.Status.Family.familyOf(effectiveResponseStatus.getStatusCode())
                    != Response.Status.Family.SUCCESSFUL) {
                Exception ex = mapToException(response);
                throw new UnwrappableException(ex);
            }
        }
    }

    private StatusType determineEffectiveResponseStatus(RestClientRequestContext context,
            ClientRequestContextImpl requestContext) {
        StatusType effectiveResponseStatus = new StatusTypeImpl(context.getResponseStatus(), context.getResponseReasonPhrase());
        if (effectiveResponseStatus.getStatusCode() == 0 && isAbortedWith(requestContext)) {
            Response abortedWith = requestContext.getAbortedWith();
            if (abortedWith.getStatusInfo() != null) {
                effectiveResponseStatus = abortedWith.getStatusInfo();
            }
        }

        return effectiveResponseStatus;
    }

    private boolean isAbortedWith(ClientRequestContextImpl requestContext) {
        return requestContext != null && requestContext.getAbortedWith() != null;
    }

    private Exception mapToException(Response response) {
        CustomWebClientApplicationException exception = new CustomWebClientApplicationException(
                String.format("%s, status code %d", response.getStatusInfo().getReasonPhrase(), response.getStatus()),
                response);

        if (response instanceof ClientResponseImpl clientResponse) {
            StackTraceElement[] callerStackTrace = clientResponse.getCallerStackTrace();
            if (callerStackTrace != null) {
                exception.setStackTrace(callerStackTrace);
            }
        }

        return exception;
    }
}
