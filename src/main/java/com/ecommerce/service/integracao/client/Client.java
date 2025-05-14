package com.ecommerce.service.integracao.client;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;

import java.util.List;

@FunctionalInterface
public interface Client {
    List<ClientesResponseDTO> clientClientesFieis();
}
