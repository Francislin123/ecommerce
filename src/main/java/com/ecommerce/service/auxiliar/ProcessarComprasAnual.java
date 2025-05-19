package com.ecommerce.service.auxiliar;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.controller.compras.response.CompraDetalhadaResponseDTO;
import com.ecommerce.service.mapper.CompraDetalhadaMapper;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProcessarComprasAnual {

    public void processarComprasParaRelatorioAnual(int ano, List<ClientesResponseDTO> clientes,
                                                   Map<String, VinhoResponseDTO> vinhosPorCodigo,
                                                   List<CompraDetalhadaResponseDTO> comprasDetalhadas) {
        for (ClientesResponseDTO cliente : clientes) {
            for (CompraResponseDTO compra : cliente.getCompras()) {
                VinhoResponseDTO vinho = vinhosPorCodigo.get(compra.getCodigo());

                if (vinho != null && vinho.getAnoCompra() != null) {
                    String[] partesData = vinho.getAnoCompra().split("-");
                    if (partesData.length >= 1 && Integer.parseInt(partesData[0]) == ano) {
                        double valorTotal = compra.getQuantidade() * vinho.getPreco();

                        comprasDetalhadas.add(new CompraDetalhadaResponseDTO(
                                cliente.getNome(),
                                cliente.getCpf(),
                                String.valueOf(vinho.getCodigo()),
                                vinho.getTipoVinho(), // Assumindo que VinhoResponseDTO tem um nomeProduto
                                vinho.getTipoVinho(),
                                compra.getQuantidade(),
                                vinho.getPreco(),
                                valorTotal
                        ));
                    }
                }
            }
        }
    }

    public List<CompraDetalhadaResponseDTO> getCompraDetalhadaResponseDTOS(
            List<ClientesResponseDTO> clientes, Map<String, VinhoResponseDTO> vinhosPorCodigo) {
        List<CompraDetalhadaResponseDTO> comprasDetalhadas = clientes.stream()
                .flatMap(cliente -> cliente.getCompras().stream()
                        .map(compra ->  // 'compra' pode ser null
                                CompraDetalhadaMapper.mapearCompraDetalhada(
                                        cliente, compra, vinhosPorCodigo.get(compra.getCodigo())))
                )
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingDouble(CompraDetalhadaResponseDTO::getValorTotal))
                .collect(Collectors.toList());
        return comprasDetalhadas;
    }
}
