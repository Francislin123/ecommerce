package com.ecommerce.service.integration.account;

import com.google.gson.Gson;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
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
public class AccountImpl implements Account {

    private final Gson gson = new Gson();

    private static final HttpClient httpClient =
            HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();

    @Override
    @Cacheable(cacheNames = "searchSourceAccountData")
    public AccountOriginResponse searchSourceAccountData(final String idAccountOrigin) {

        final String bodyResultFinal;

        try {

            String uriTransferAccountOrigin = "http://localhost:9090/contas/";

            HttpRequest request = HttpRequest.newBuilder()
                    .GET().uri(URI.create(uriTransferAccountOrigin + idAccountOrigin)).build();

            final HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            final int statusCode = response.statusCode();

            if (statusCode == 404) {
                log.info("Account origin not found for id: " + idAccountOrigin);
                return AccountOriginResponse.builder().build();
            }

            // print response body
            bodyResultFinal = response.body();

        } catch (IOException | InterruptedException e) {
            log.trace("Error with integration " + "idAccountOrigin: " + idAccountOrigin, e);
            throw new RuntimeException(e);
        }

        AccountOriginResponse accountOriginResponse = this.gson.fromJson(bodyResultFinal, AccountOriginResponse.class);
        log.info("Account origin successfully found by id: " + accountOriginResponse.getId());
        return accountOriginResponse;
    }
}
