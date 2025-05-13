package com.ecommerce.service.facade;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseDTO;

@FunctionalInterface
public interface TransferFacade {
    TransferResponseDTO makeTransfer(TransferRequestDTO transferRequestDTO);
}
