package com.ecommerce.service.integration.account.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AccountOriginResponse {
    private String id;
    private double saldo;
    private double limiteDiario;
    private boolean ativo;

    public AccountOriginResponse() {
    }

    @Builder
    public AccountOriginResponse(String id, double saldo, double limiteDiario, boolean ativo) {
        this.id = id;
        this.saldo = saldo;
        this.limiteDiario = limiteDiario;
        this.ativo = ativo;
    }
}
