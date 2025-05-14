package com.ecommerce.service.integration.compras;

import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
@Slf4j
public class VinhoClientImpl implements VinhoClient {

    @Value("${url.produtos}")
    private String uriVinhos;

    private final Gson gson = new Gson();

    private static final HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();

    @Override
    @Cacheable("recomendacaoDeVinho")
    public List<VinhoResponseDTO> recomendacaoDeVinho() {

        log.info("Inicio da requisição");

        final String bodyResultFinal;

        try {

            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uriVinhos)).build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            final int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Cardapio de vinhos não encontrados");
                return new ArrayList<>();
            }

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
