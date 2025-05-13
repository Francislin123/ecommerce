package com.ecommerce.service.integration.client;

import com.ecommerce.controller.response.ClientesResponseDTO;

import java.util.List;

@FunctionalInterface
public interface Client {
    List<ClientesResponseDTO> clientClientesFieis();
}
