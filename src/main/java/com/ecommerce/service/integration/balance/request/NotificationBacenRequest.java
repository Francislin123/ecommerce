package com.ecommerce.service.integration.balance.request;

import com.transfer.api.controller.request.ContaRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationBacenRequest {
    private double valor;
    private ContaRequestDto conta;

    @Builder
    public NotificationBacenRequest(double valor, ContaRequestDto conta) {
        this.valor = valor;
        this.conta = conta;
    }
}
