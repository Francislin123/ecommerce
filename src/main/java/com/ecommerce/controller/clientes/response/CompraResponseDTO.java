package com.ecommerce.controller.clientes.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CompraResponseDTO {

    private String codigo;
    private int quantidade;

    @Builder
    public CompraResponseDTO(String codigo, int quantidade) {
        this.codigo = codigo;
        this.quantidade = quantidade;
    }
}
