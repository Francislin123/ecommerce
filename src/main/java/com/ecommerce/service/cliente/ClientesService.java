package com.ecommerce.service.cliente;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.controller.response.VinhoResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ClientesService {
    List<ClientesResponseDTO> clientesFieis();
    Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final String cpf);
}
