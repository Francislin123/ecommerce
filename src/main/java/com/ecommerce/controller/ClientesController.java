package com.ecommerce.controller;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.controller.response.VinhoResponseDTO;
import com.ecommerce.service.cliente.ClientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    private final ClientesService clientesService;

    @Autowired
    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping(value = "/fieis", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientesResponseDTO>> listarClientesFieis() {
        List<ClientesResponseDTO> response = clientesService.clientesFieis();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tipo/{cpf}")
    public ResponseEntity<VinhoResponseDTO> recomendarVinhoPorTipo(@PathVariable Long cpf) {
        Optional<VinhoResponseDTO> recomendacaoOptional = clientesService.recomendarVinhoPorTipo(cpf);
        return recomendacaoOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
