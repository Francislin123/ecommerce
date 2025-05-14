package com.ecommerce.service.compras;

import com.ecommerce.controller.compras.request.CompraDetalhadaResponseDTO;

import java.util.List;

public interface ComprasService {
    List<CompraDetalhadaResponseDTO> listarComprasOrdenadas();
}
