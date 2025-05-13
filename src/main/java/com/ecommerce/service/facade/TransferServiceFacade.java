package com.ecommerce.service.facade;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.RolesForValidateTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransferServiceFacade implements TransferFacade {

    @Autowired
    private RolesForValidateTransfer rolesForValidateTransfer;

    @Override
    public TransferResponseDTO makeTransfer(final TransferRequestDTO transferRequestDTO){
        return rolesForValidateTransfer.rolesForValidateTransfer(transferRequestDTO);
    }
}
