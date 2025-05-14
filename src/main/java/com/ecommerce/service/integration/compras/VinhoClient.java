package com.ecommerce.service.integration.compras;

import com.ecommerce.controller.clientes.response.VinhoResponseDTO;

import java.util.List;

@FunctionalInterface
public interface VinhoClient {
    List<VinhoResponseDTO> recomendacaoDeVinho();
}
