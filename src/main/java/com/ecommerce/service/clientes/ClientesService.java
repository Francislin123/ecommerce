package com.ecommerce.service.clientes;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ClientesService {
    List<ClientesResponseDTO> clientesFieis();
    Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final String cpf);
}
