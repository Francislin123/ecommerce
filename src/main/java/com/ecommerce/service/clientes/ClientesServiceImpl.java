package com.ecommerce.service.clientes;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.service.integracao.client.Client;
import com.ecommerce.service.integracao.compras.VinhoClient;
import com.ecommerce.service.support.RecomendadorDeVinhos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientesServiceImpl implements ClientesService {

    @Autowired
    private Client clienteClient;

    @Autowired
    private VinhoClient vinhoClient;

    @Autowired
    private RecomendadorDeVinhos recomendadorDeVinhos;

    String clienteNãoEncontrado = "Cliente não encontrado";

    @Override
    public List<ClientesResponseDTO> clientesFieis() {
        return clienteClient.clientClientesFieis().stream()
                .sorted((p1, p2) -> {
                    int total1 = p1.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
                    int total2 = p2.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
                    int recorrencia1 = (int) p1.getCompras().stream().map(CompraResponseDTO::getCodigo).distinct().count();
                    int recorrencia2 = (int) p2.getCompras().stream().map(CompraResponseDTO::getCodigo).distinct().count();
                    int comparacaoValor = Integer.compare(total2, total1);
                    return comparacaoValor != 0 ? comparacaoValor : Integer.compare(recorrencia2, recorrencia1);
                })
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final String cpf) {
        ClientesResponseDTO cliente = clienteClient.clientClientesFieis().stream()
                .filter(c -> cpf.equals(c.getCpf()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(clienteNãoEncontrado));
        List<CompraResponseDTO> compras = cliente.getCompras();
        List<VinhoResponseDTO> vinhosDisponiveis = vinhoClient.recomendacaoDeVinho();
        VinhoResponseDTO recomendacao = recomendadorDeVinhos.recomendar(compras, vinhosDisponiveis);
        return Optional.ofNullable(recomendacao);
    }
}
