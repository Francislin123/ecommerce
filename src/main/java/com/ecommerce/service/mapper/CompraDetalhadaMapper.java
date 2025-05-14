package com.ecommerce.service.mapper;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.controller.compras.request.CompraDetalhadaResponseDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompraDetalhadaMapper {

    public static CompraDetalhadaResponseDTO mapearCompraDetalhada(ClientesResponseDTO cliente,
                                                                   CompraResponseDTO compra,
                                                                   VinhoResponseDTO vinho) {

        if (vinho == null) {
            log.debug("Vinho não encontrado para o código: {}", compra.getCodigo());
            return null;
        }

        double valorTotal = compra.getQuantidade() * vinho.getPreco();

        log.debug("Mapeando compra: Cliente: {} | Produto: {} | Quantidade: {} | Valor Unitário: {} | Valor Total: {}",
                cliente.getNome(),
                vinho.getTipoVinho(),
                compra.getQuantidade(),
                vinho.getPreco(),
                valorTotal
        );

        return new CompraDetalhadaResponseDTO(
                cliente.getNome(),
                cliente.getCpf(),
                String.valueOf(vinho.getCodigo()),
                vinho.getTipoVinho(),
                vinho.getTipoVinho(),
                compra.getQuantidade(),
                vinho.getPreco(),
                valorTotal
        );
    }
}
