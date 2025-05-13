package com.ecommerce.service.integration.client.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientResponse {
    private String id;
    private String nome;
    private String telefone;
    private String tipoPessoa;

    @Builder
    public ClientResponse(String id, String nome, String telefone, String tipoPessoa) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.tipoPessoa = tipoPessoa;
    }
}
