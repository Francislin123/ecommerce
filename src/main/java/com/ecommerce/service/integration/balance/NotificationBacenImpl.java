package com.ecommerce.service.integration.balance;

import com.google.gson.Gson;
import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@Slf4j
public class NotificationBacenImpl implements NotificationBacen {

    @Override
    public void notificationBacen(NotificationBacenRequest notification) {
        Gson gson = new Gson();
        try {

            String json = gson.toJson(notification);

            HttpClient client = HttpClient.newHttpClient();

            String uriNotification = "http://localhost:9090/notificacoes";

            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .uri(URI.create(uriNotification))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            final int statusCode = response.statusCode();

            if (statusCode == 429) {
                log.info("Call limit has been exceeded");
            }

        } catch (IOException | InterruptedException e) {
            log.trace("Error with integration", e);
            throw new RuntimeException(e);
        }
    }
}
