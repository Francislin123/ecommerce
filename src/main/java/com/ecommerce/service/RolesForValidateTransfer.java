package com.ecommerce.service;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;

@FunctionalInterface
public interface RolesForValidateTransfer {
    TransferResponseDTO rolesForValidateTransfer(TransferRequestDTO transferRequestDTO);
}
