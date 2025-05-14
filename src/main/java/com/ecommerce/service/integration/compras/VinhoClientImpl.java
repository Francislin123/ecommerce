package com.ecommerce.service.integration.compras;

import com.ecommerce.controller.response.VinhoResponseDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class VinhoClientImpl implements VinhoClient {

    private static final Logger log = LoggerFactory.getLogger(VinhoClientImpl.class);

    @Value("${url.produtos}")
    private String uriVinhos;

    private final Gson gson = new Gson();

    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();

    @Override
    public List<VinhoResponseDTO> recomendacaoDeVinho() {

        final String bodyResultFinal;

        try {

            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uriVinhos)).build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            final int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Cardapio de vinhos não encontrados");
                return new ArrayList<>();
            }

            // print response body
            bodyResultFinal = response.body();

        } catch (IOException | InterruptedException e) {
            log.trace("Erro com integração");
            throw new RuntimeException(e);
        }

        Type listType = new TypeToken<List<VinhoResponseDTO>>() {
        }.getType();
        List<VinhoResponseDTO> lista = this.gson.fromJson(bodyResultFinal, listType);
        log.info("Cardapio de vinhos retornado.");
        return lista;
    }
}
