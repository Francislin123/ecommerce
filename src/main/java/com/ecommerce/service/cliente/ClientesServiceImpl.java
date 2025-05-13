package com.ecommerce.service.cliente;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.controller.response.CompraResponseDTO;
import com.ecommerce.controller.response.VinhoResponseDTO;
import com.ecommerce.service.integration.client.Client;
import com.ecommerce.service.integration.compras.VinhoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientesServiceImpl implements ClientesService {

    @Autowired
    private Client clienteClient;

    @Autowired
    private VinhoClient vinhoClient;

    @Override
    public List<ClientesResponseDTO> clientesFieis() {
        return clienteClient.clientClientesFieis().stream().sorted((p1, p2) -> {
            int total1 = p1.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
            int total2 = p2.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
            return Integer.compare(total2, total1); // decrescente
        }).limit(3).collect(Collectors.toList());
    }

    @Override
    public Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final Long cpf) {

        List<ClientesResponseDTO> clientesResponseDTOS = clienteClient.clientClientesFieis();

        Optional<ClientesResponseDTO> clienteOptional = clientesResponseDTOS.stream()
                .filter(cliente -> cliente.getCpf().equals(cpf))
                .findFirst();

        List<VinhoResponseDTO> historicoCompras = vinhoClient.recomendacaoDeVinho();

        if (historicoCompras == null || historicoCompras.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Long> frequenciaTipos = historicoCompras.stream()
                .map(VinhoResponseDTO::getTipoVinho)
                .collect(Collectors.groupingBy(tipo -> tipo, Collectors.counting()));

        Optional<Map.Entry<String, Long>> tipoMaisCompradoOptional = frequenciaTipos.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        if (tipoMaisCompradoOptional.isEmpty()) {
            return Optional.empty();
        }

        String tipoPreferido = tipoMaisCompradoOptional.get().getKey();

        List<VinhoResponseDTO> recomendacoesPotenciais = vinhoClient.recomendacaoDeVinho();

        if (recomendacoesPotenciais != null && !recomendacoesPotenciais.isEmpty()) {
            return Optional.of(recomendacoesPotenciais.get(0));
        }

        return Optional.empty();
    }
}
