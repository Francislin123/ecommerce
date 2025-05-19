package com.ecommerce.service.compras;

import com.ecommerce.controller.compras.response.CompraDetalhadaResponseDTO;

import java.util.List;

public interface ComprasService {
    List<CompraDetalhadaResponseDTO> listarComprasOrdenadas();
    CompraDetalhadaResponseDTO maiorCompraDoAno(int ano);
}
