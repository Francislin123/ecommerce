package com.ecommerce.controller.compras.request;

public record CompraDetalhadaResponseDTO(
        String nomeCliente,
        String cpfCliente,
        String codigoProduto,
        String nomeProduto,
        String tipoVinho,
        int quantidade,
        double precoUnitario,
        double valorTotal
) {}
