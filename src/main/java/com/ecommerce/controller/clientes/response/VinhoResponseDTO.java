package com.ecommerce.controller.clientes.response;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VinhoResponseDTO {

    private String codigo;

    @SerializedName("tipo_vinho")
    private String tipoVinho;

    private double preco;

    private String safra;

    @SerializedName("ano_compra")
    private String anoCompra;

    @Builder
    public VinhoResponseDTO(String codigo, String tipoVinho, double preco, String safra, String anoCompra) {
        this.codigo = codigo;
        this.tipoVinho = tipoVinho;
        this.preco = preco;
        this.safra = safra;
        this.anoCompra = anoCompra;
    }

    public VinhoResponseDTO(String codigo, String tipoVinho) {
        this.codigo = codigo;
        this.tipoVinho = tipoVinho;
    }

    @Override
    public String toString() {
        return "VinhoResponseDTO{" +
                "codigo='" + codigo + '\'' +
                ", tipoVinho='" + tipoVinho + '\'' +
                ", preco=" + preco +
                ", safra='" + safra + '\'' +
                ", anoCompra='" + anoCompra + '\'' +
                '}';
    }
}
