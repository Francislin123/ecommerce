package com.ecommerce.service.compras;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.controller.compras.request.CompraDetalhadaResponseDTO;
import com.ecommerce.service.integration.client.Client;
import com.ecommerce.service.integration.compras.VinhoClient;
import com.ecommerce.service.mapper.CompraDetalhadaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ComprasServiceImpl implements ComprasService {

    @Autowired
    private Client clienteClient;

    @Autowired
    private VinhoClient vinhoClient;

    @Override
    public List<CompraDetalhadaResponseDTO> listarComprasOrdenadas() {

        log.debug("Iniciando a listagem de compras ordenadas.");

        final List<ClientesResponseDTO> clientes = clienteClient.clientClientesFieis();
        final List<VinhoResponseDTO> vinhos = vinhoClient.recomendacaoDeVinho();

        log.debug("Clientes obtidos: {}", clientes.size());
        log.debug("Vinhedos obtidos: {}", vinhos.size());

        final Map<String, VinhoResponseDTO> vinhosPorCodigo = vinhos.stream()
                .collect(Collectors.toMap(
                        v -> String.valueOf(v.getCodigo()),
                        v -> v
                ));

        log.debug("Mapa de vinhos por código gerado com sucesso.");

        List<CompraDetalhadaResponseDTO> comprasDetalhadas = clientes.stream()
                .flatMap(cliente -> cliente.getCompras().stream()
                        .map(compra ->
                                CompraDetalhadaMapper.mapearCompraDetalhada(
                                        cliente, compra, vinhosPorCodigo.get(compra.getCodigo())))
                )
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(CompraDetalhadaResponseDTO::valorTotal))
                .collect(Collectors.toList());

        log.debug("Total de compras detalhadas processadas: {}", comprasDetalhadas.size());

        return comprasDetalhadas;
    }
}