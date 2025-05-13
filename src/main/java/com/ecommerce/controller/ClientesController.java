package com.ecommerce.controller;

import com.transfer.api.controller.request.TransferRequestDTO;
import com.transfer.api.controller.response.TransferResponseAPIResponseEntity;
import com.transfer.api.controller.response.TransferResponseDTO;
import com.transfer.api.service.facade.TransferFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ClientesController.URI_TRANSFER)
public class ClientesController {

    public static final String URI_TRANSFER = "/transfer";

    @Autowired
    private TransferFacade transfer;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransferResponseDTO> makeTransfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        return TransferResponseAPIResponseEntity.getTransferResponseDTOResponseEntity(
                transfer.makeTransfer(transferRequestDTO));
    }
}
