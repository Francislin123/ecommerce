package com.ecommerce.service.compras;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.controller.compras.response.CompraDetalhadaResponseDTO;
import com.ecommerce.exception.CompraNaoEncontradaException;
import com.ecommerce.service.auxiliar.GetStringVinhoResponse;
import com.ecommerce.service.auxiliar.ProcessarComprasAnual;
import com.ecommerce.service.integracao.client.Client;
import com.ecommerce.service.integracao.compras.VinhoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ComprasServiceImpl implements ComprasService {

    private Client clienteClient;

    private VinhoClient vinhoClient;

    private ProcessarComprasAnual relatorioAnual;

    private GetStringVinhoResponse vinhoMapper;

    @Autowired
    public ComprasServiceImpl(Client clienteClient, VinhoClient vinhoClient, ProcessarComprasAnual relatorioAnual,
                              GetStringVinhoResponse vinhoMapper) {
        this.clienteClient = clienteClient;
        this.vinhoClient = vinhoClient;
        this.relatorioAnual = relatorioAnual;
        this.vinhoMapper = vinhoMapper;
    }

    @Override
    public List<CompraDetalhadaResponseDTO> listarComprasOrdenadas() {

        log.debug("Iniciando a listagem de compras ordenadas.");

        final List<ClientesResponseDTO> clientes = this.clienteClient.clientClientesFieis();
        final List<VinhoResponseDTO> vinhos = this.vinhoClient.recomendacaoDeVinho();

        log.debug("Clientes obtidos: {}", clientes.size());
        log.debug("Vinhedos obtidos: {}", vinhos.size());

        final Map<String, VinhoResponseDTO> vinhosPorCodigo =
                this.vinhoMapper.getStringVinhoResponseDTOMap(vinhos);

        log.debug("Mapa de vinhos por código gerado com sucesso.");

        List<CompraDetalhadaResponseDTO> comprasDetalhadas =
                this.relatorioAnual.getCompraDetalhadaResponseDTOS(clientes, vinhosPorCodigo);

        log.debug("Total de compras detalhadas processadas: {}", comprasDetalhadas.size());

        return comprasDetalhadas;
    }

    @Override
    public CompraDetalhadaResponseDTO maiorCompraDoAno(final int ano) {
        log.debug("Iniciando busca pela maior compra do ano: {}", ano);

        List<ClientesResponseDTO> clientes = this.clienteClient.clientClientesFieis();
        log.debug("Total de clientes recuperados: {}", clientes.size());

        List<VinhoResponseDTO> vinhos = this.vinhoClient.recomendacaoDeVinho();
        log.debug("Total de vinhos recuperados: {}", vinhos.size());

        Map<String, VinhoResponseDTO> vinhosPorCodigo = vinhos.stream()
                .collect(Collectors.toMap(
                        v -> String.valueOf(v.getCodigo()),
                        v -> v
                ));
        log.debug("Mapeamento de vinhos por código concluído. Total mapeado: {}", vinhosPorCodigo.size());

        List<CompraDetalhadaResponseDTO> comprasDetalhadas = new ArrayList<>();

        log.debug("Processando compras detalhadas para o ano: {}", ano);
        relatorioAnual.processarComprasParaRelatorioAnual(ano, clientes, vinhosPorCodigo, comprasDetalhadas);
        log.debug("Total de compras detalhadas processadas: {}", comprasDetalhadas.size());

        return comprasDetalhadas.stream()
                .max(Comparator.comparingDouble(CompraDetalhadaResponseDTO::getValorTotal))
                .orElseThrow(() -> {
                    log.warn("Nenhuma compra encontrada para o ano: {}", ano);
                    throw new CompraNaoEncontradaException(ano);
                });
    }
}