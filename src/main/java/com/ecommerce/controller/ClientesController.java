package com.ecommerce.controller;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.service.cliente.ClientesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
