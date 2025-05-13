package com.ecommerce.service;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.integration.account.Account;
import com.transfer.api.service.integration.account.response.AccountOriginResponse;
import com.transfer.api.service.integration.balance.NotificationBacen;
import com.transfer.api.service.integration.balance.request.NotificationBacenRequest;
import com.transfer.api.service.integration.client.Client;
import com.transfer.api.service.integration.client.response.ClientResponse;
import com.transfer.api.service.integration.transfer.TransferClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.transfer.api.util.CheckTransfer.checkTransfer;
import static com.transfer.api.util.CheckTransfer.isaBoolean;

@Service
public class RolesForValidateTransferImpl implements RolesForValidateTransfer {

    @Autowired
    private Client client;

    @Autowired
    private Account account;

    @Autowired
    private TransferClient transferClient;

    @Autowired
    private NotificationBacen notificationBacen;

    public TransferResponseDTO rolesForValidateTransfer(TransferRequestDTO transferRequestDTO) {

        AccountOriginResponse accountOrigin;

        final ClientResponse clientResponse;

        String idTransferFinal;

        try {

            clientResponse = CompletableFuture.completedFuture(
                    this.client.validateIfTheClientExists(
                            transferRequestDTO.getIdCliente())).get(5, TimeUnit.SECONDS);

            if (clientResponse.getId() == null) {
                return TransferResponseDTO.builder().build();
            }

            accountOrigin = CompletableFuture.completedFuture(
                    this.account.searchSourceAccountData(
                            transferRequestDTO.getConta().getIdOrigem())).get(5, TimeUnit.SECONDS);

            final boolean checkTransfer = checkTransfer(accountOrigin.getLimiteDiario(), transferRequestDTO.getValor());

            if (checkTransfer) {
                return TransferResponseDTO.builder().limiteDiario(accountOrigin.getLimiteDiario()).build();
            }

            idTransferFinal = CompletableFuture.completedFuture(
                    this.transferClient.transferSend(transferRequestDTO)).get(5, TimeUnit.SECONDS);

            if (isaBoolean(transferRequestDTO, accountOrigin)) {

                this.notificationBacen.notificationBacen(NotificationBacenRequest.builder()
                        .valor(transferRequestDTO.getValor())
                        .conta(transferRequestDTO.getConta())
                        .build());
            }

        } catch (RuntimeException | TimeoutException | ExecutionException | InterruptedException e) {
            return TransferResponseDTO.builder()
                    .msg("Service unavailable we are working to stabilize")
                    .build();
        }

        return TransferResponseDTO.builder()
                .limiteDiario(accountOrigin.getLimiteDiario())
                .idTransferencia(idTransferFinal)
                .build();
    }
}
