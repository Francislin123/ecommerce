package com.ecommerce.controller.compras;

import com.ecommerce.controller.compras.request.CompraDetalhadaResponseDTO;
import com.ecommerce.service.compras.ComprasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ComprasController {

    private ComprasService comprasService;

    @Autowired
    public ComprasController(ComprasService comprasService) {
        this.comprasService = comprasService;
    }

    @GetMapping(value = "/compras", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CompraDetalhadaResponseDTO>> listarComprasOrdenadas() {
        List<CompraDetalhadaResponseDTO> response = comprasService.listarComprasOrdenadas();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/maior_compra/{ano}")
    public CompraDetalhadaResponseDTO maiorCompra(@PathVariable int ano) {
        return comprasService.maiorCompraDoAno(ano);
    }
}
