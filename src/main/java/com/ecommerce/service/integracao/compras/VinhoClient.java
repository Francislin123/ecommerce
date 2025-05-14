package com.ecommerce.service.integracao.compras;

import com.ecommerce.controller.clientes.response.VinhoResponseDTO;

import java.util.List;

@FunctionalInterface
public interface VinhoClient {
    List<VinhoResponseDTO> recomendacaoDeVinho();
}
