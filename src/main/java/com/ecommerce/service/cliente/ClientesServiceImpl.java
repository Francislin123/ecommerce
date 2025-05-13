package com.ecommerce.service.cliente;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.controller.response.CompraResponseDTO;
import com.ecommerce.service.integration.client.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientesServiceImpl implements ClientesService {

    @Autowired
    private Client client;

    @Override
    public List<ClientesResponseDTO> clientesFieis() {
        return client.clientClientesFieis().stream()
                .sorted((p1, p2) -> {
                    int total1 = p1.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
                    int total2 = p2.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
                    return Integer.compare(total2, total1); // decrescente
                })
                .limit(3)
                .collect(Collectors.toList());
    }
}
