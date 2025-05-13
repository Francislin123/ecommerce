package com.ecommerce.service.integration.client;

import com.google.gson.Gson;
import com.transfer.api.service.integration.client.response.ClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
@Slf4j
public class ClientImpl implements Client {

    private final Gson gson = new Gson();

    private static final HttpClient httpClient =
            HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

    @Override
    @Cacheable(cacheNames = "validateIfTheClientExists")
    public ClientResponse validateIfTheClientExists(final String idClient) {
        final String bodyResultFinal;
        try {

            String uriClient = "http://localhost:9090/clientes/";

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .GET().uri(URI.create(uriClient + idClient))
                    .build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            final int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Client not found for id: " + idClient);
                return ClientResponse.builder().build();
            }

            // print response body
            bodyResultFinal = response.body();

        } catch (IOException | InterruptedException e) {
            log.trace("Error with integration for idClient: " + idClient, e);
            throw new RuntimeException(e);
        }
        ClientResponse clientResponse = this.gson.fromJson(bodyResultFinal, ClientResponse.class);
        log.info("Client successfully found by id: " + clientResponse.getId());
        return clientResponse;
    }
}
