package com.ecommerce.controller.clientes.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ClientesResponseDTO {

    private String nome;
    private String cpf;
    private List<CompraResponseDTO> compras;

    @Builder
    public ClientesResponseDTO(String nome, String cpf, List<CompraResponseDTO> compras) {
        this.nome = nome;
        this.cpf = cpf;
        this.compras = compras;
    }
}
