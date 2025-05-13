package com.ecommerce.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class VinhoResponseDTO {

    private int codigo;

    @JsonProperty("tipo_vinho")
    private String tipoVinho;

    private double preco;

    private String safra;

    @JsonProperty("ano_compra")
    private int anoCompra;

    @Builder
    public VinhoResponseDTO(int codigo, String tipoVinho, double preco, String safra, int anoCompra) {
        this.codigo = codigo;
        this.tipoVinho = tipoVinho;
        this.preco = preco;
        this.safra = safra;
        this.anoCompra = anoCompra;
    }
}
