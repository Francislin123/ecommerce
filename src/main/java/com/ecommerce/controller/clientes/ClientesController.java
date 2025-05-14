package com.ecommerce.controller.clientes;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.service.clientes.ClientesService;
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
@RequestMapping(ClientesController.URI_CLIENTES)
public class ClientesController {

    public static final String URI_CLIENTES = "/clientes";

    private final ClientesService clientesService;

    @Autowired
    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping(value = "/clientes-fieis", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientesResponseDTO>> listarClientesFieis() {
        List<ClientesResponseDTO> response = clientesService.clientesFieis();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recomendacao/cliente/tipo/{cpf}")
    public ResponseEntity<VinhoResponseDTO> recomendarVinhoPorTipo(@PathVariable String cpf) {
        Optional<VinhoResponseDTO> recomendacaoOptional = clientesService.recomendarVinhoPorTipo(cpf);
        return recomendacaoOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
