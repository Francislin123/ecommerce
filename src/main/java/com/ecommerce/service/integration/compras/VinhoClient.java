package com.ecommerce.service.integration.compras;

import com.ecommerce.controller.response.VinhoResponseDTO;

import java.util.List;

@FunctionalInterface
public interface VinhoClient {
    List<VinhoResponseDTO> recomendacaoDeVinho();
}
