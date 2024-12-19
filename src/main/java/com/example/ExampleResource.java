package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;

@Path("/api")
@RequiredArgsConstructor
public class ExampleResource {

    private final ExampleService exampleService;

    @GET
    @Path("/test-timeout")
    @Produces(MediaType.TEXT_PLAIN)
    public String timeoutCall() {
        return exampleService.callHttpWithTimeout();
    }
}
