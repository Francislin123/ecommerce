package com.ecommerce.service.clientes;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.exception.ClienteNaoEncontradoException;
import com.ecommerce.service.integracao.client.Client;
import com.ecommerce.service.integracao.compras.VinhoClient;
import com.ecommerce.service.support.RecomendadorDeVinhos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientesServiceImpl implements ClientesService {

    private Client clienteClient;
    private VinhoClient vinhoClient;
    private RecomendadorDeVinhos recomendadorDeVinhos;

    @Autowired
    public ClientesServiceImpl(Client clienteClient, VinhoClient vinhoClient, RecomendadorDeVinhos recomendadorDeVinhos) {
        this.clienteClient = clienteClient;
        this.vinhoClient = vinhoClient;
        this.recomendadorDeVinhos = recomendadorDeVinhos;
    }

    @Override
    public List<ClientesResponseDTO> clientesFieis() {
        log.debug("Iniciando busca dos clientes mais fiéis.");

        List<ClientesResponseDTO> clientes = clienteClient.clientClientesFieis();
        log.debug("Total de clientes recuperados: {}", clientes.size());

        List<ClientesResponseDTO> top3Clientes = clientes.stream()
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

        log.debug("Top 3 clientes mais fiéis identificados: {}", top3Clientes);

        return top3Clientes;
    }

    @Override
    public Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final String cpf) {
        log.debug("Iniciando recomendação de vinho para o CPF: {}", cpf);

        ClientesResponseDTO cliente = clienteClient.clientClientesFieis().stream()
                .filter(c -> cpf.equals(c.getCpf()))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("Cliente não encontrado para CPF: {}", cpf);
                    throw new ClienteNaoEncontradoException(cpf);
                });

        log.debug("Cliente encontrado: {}", cliente.getNome());

        List<CompraResponseDTO> compras = cliente.getCompras();
        List<VinhoResponseDTO> vinhosDisponiveis = vinhoClient.recomendacaoDeVinho();

        log.debug("Total de vinhos disponíveis para recomendação: {}", vinhosDisponiveis.size());

        VinhoResponseDTO recomendacao = recomendadorDeVinhos.recomendar(compras, vinhosDisponiveis);

        if (recomendacao != null) {
            log.debug("Recomendação de vinho gerada com sucesso: código {}", recomendacao.getCodigo());
        } else {
            log.debug("Nenhuma recomendação encontrada para o cliente.");
        }

        return Optional.ofNullable(recomendacao);
    }
}