package com.ecommerce.service.cliente;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.controller.response.CompraResponseDTO;
import com.ecommerce.controller.response.VinhoResponseDTO;
import com.ecommerce.service.integration.client.Client;
import com.ecommerce.service.integration.compras.VinhoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
            return Integer.compare(total2, total1);
        }).limit(3).collect(Collectors.toList());
    }

    @Override
    public Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final String cpf) {

        ClientesResponseDTO clientesResponseDTO = clienteClient.clientClientesFieis()
                .stream().filter(c -> cpf.equals(c.getCpf()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<VinhoResponseDTO> vinhosDisponiveis = vinhoClient.recomendacaoDeVinho();

        List<CompraResponseDTO> compras = clientesResponseDTO.getCompras();

        Map<String, String> tipoPorCodigo = criarMockVinhoMap(vinhosDisponiveis);

        Map<String, Integer> tipoContagem = new HashMap<>();
        for (CompraResponseDTO compra : compras) {
            String tipo = tipoPorCodigo.get(compra.getCodigo());
            if (tipo != null) {
                tipoContagem.put(tipo, tipoContagem.getOrDefault(tipo, 0) + compra.getQuantidade());
            }
        }

        Optional<String> tipoMaisComprado = tipoContagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        if (tipoMaisComprado.isEmpty()) {
            return Optional.empty();
        }

        String tipoPreferido = tipoMaisComprado.get();

        return vinhosDisponiveis.stream()
                .filter(v -> tipoPreferido.equalsIgnoreCase(v.getTipoVinho()))
                .findFirst();
    }

    private Map<String, String> criarMockVinhoMap(List<VinhoResponseDTO> vinhosDisponiveis) {
        return vinhosDisponiveis.stream()
                .collect(Collectors.toMap(
                        v -> String.valueOf(v.getCodigo()),
                        VinhoResponseDTO::getTipoVinho,
                        (existingValue, newValue) -> existingValue
                ));
    }
}
