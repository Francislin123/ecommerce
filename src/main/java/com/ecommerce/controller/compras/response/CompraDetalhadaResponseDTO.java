package com.ecommerce.controller.compras.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompraDetalhadaResponseDTO {
    private String nomeCliente;
    private String cpfCliente;
    private String codigoProduto;
    private String nomeProduto;
    private String tipoVinho;
    private int quantidade;
    private double precoUnitario;
    private double valorTotal;

    // Construtores, getters e setters
    public CompraDetalhadaResponseDTO(String nomeCliente, String cpfCliente, String codigoProduto,
                                      String nomeProduto, String tipoVinho, int quantidade,
                                      double precoUnitario, double valorTotal) {
        this.nomeCliente = nomeCliente;
        this.cpfCliente = cpfCliente;
        this.codigoProduto = codigoProduto;
        this.nomeProduto = nomeProduto;
        this.tipoVinho = tipoVinho;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
    }
}
