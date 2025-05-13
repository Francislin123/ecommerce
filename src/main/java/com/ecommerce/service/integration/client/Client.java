package com.ecommerce.service.integration.client;

import com.transfer.api.service.integration.client.response.ClientResponse;

@FunctionalInterface
public interface Client {
    ClientResponse validateIfTheClientExists(final String idClient);
}
