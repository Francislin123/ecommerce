package com.ecommerce.service.integracao.client;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
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
public class ClientImpl implements Client {

    @Value("${url.clientes}")
    private String uriClientes;

    private final Gson gson = new Gson();

    private final HttpClient httpClient;

    public ClientImpl() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public ClientImpl(String uriClientes, HttpClient httpClient) {
        this.uriClientes = uriClientes;
        this.httpClient = httpClient;
    }

    public ClientImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    @Cacheable("clientesFieis")
    public List<ClientesResponseDTO> clientClientesFieis() {

        log.info("Inicio da requisição");

        final String bodyResultFinal;

        try {

            HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(uriClientes)).build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            final int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Clientes não encontrados");
                return new ArrayList<>();
            }

            // print response body
            bodyResultFinal = response.body();

        } catch (IOException | InterruptedException e) {
            log.trace("Erro com integração");
            throw new RuntimeException(e);
        }

        Type listType = new TypeToken<List<ClientesResponseDTO>>() {
        }.getType();
        List<ClientesResponseDTO> lista = this.gson.fromJson(bodyResultFinal, listType);
        log.info("Clientes retornados com sucesso");
        return lista;
    }
}
