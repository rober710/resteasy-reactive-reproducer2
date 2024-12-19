package com.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.quarkus.rest.client.reactive.runtime.context.HttpClientOptionsContextResolver;
import io.vertx.core.http.HttpClientOptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.UriBuilder;
import java.util.concurrent.TimeUnit;
import org.jboss.resteasy.reactive.client.api.QuarkusRestClientProperties;
import org.jboss.resteasy.reactive.client.impl.ClientBuilderImpl;
import org.jboss.resteasy.reactive.common.jaxrs.ConfigurationImpl;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.Options.DYNAMIC_PORT;

@ApplicationScoped
public class ExampleService {

    private final Client httpClient = createHttpClient(1000);

    private final WireMockServer wireMockServer = createMockService();

    public String callHttpWithTimeout() {
        String url = UriBuilder.fromUri(wireMockServer.baseUrl()).path("delay").build().toString();

        try {
            String payload = httpClient.target(url).request().get(String.class);
            System.out.println("payload read in normal execution: " + payload);
            return payload;
        } catch (WebApplicationException e) {
            e.printStackTrace();
            String payload = e.getResponse().readEntity(String.class);
            return "Error response: " + payload;
        }
    }

    private static Client createHttpClient(long timeoutMillis) {
        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        ConfigurationImpl configuration = new ConfigurationImpl(RuntimeType.CLIENT);

        HttpClientOptions httpOpts = new HttpClientOptions();
        httpOpts.setKeepAlive(true);
        httpOpts.setTcpKeepAlive(true);
        httpOpts.setIdleTimeoutUnit(TimeUnit.MILLISECONDS);
        httpOpts.setIdleTimeout((int) timeoutMillis);

        configuration.property(QuarkusRestClientProperties.KEEP_ALIVE_ENABLED, true);
        configuration.property(QuarkusRestClientProperties.READ_TIMEOUT, timeoutMillis);
        configuration.property(QuarkusRestClientProperties.CONNECTION_TTL, 1);
        configuration.property(QuarkusRestClientProperties.CONNECTION_POOL_SIZE, 2);
        configuration.property(QuarkusRestClientProperties.MAX_REDIRECTS, 3);
        clientBuilder.connectTimeout(timeoutMillis, TimeUnit.MILLISECONDS);

        ((ClientBuilderImpl) clientBuilder).followRedirects(true);
        configuration.register(new HttpClientOptionsContextResolver(httpOpts));

        return clientBuilder.withConfig(configuration)
                .register(new CustomRestClientResponseFilter())
                .build();
    }

    private static WireMockServer createMockService() {
        WireMockServer wireMockServer = new WireMockServer(DYNAMIC_PORT);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlEqualTo("/delay"))
                .willReturn(ok("Delayed content").withFixedDelay(10_000)));

        return wireMockServer;
    }
}
