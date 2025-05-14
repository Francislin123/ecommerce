package com.ecommerce.service.auxiliar;

import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GetStringVinhoResponse {

    public Map<String, VinhoResponseDTO> getStringVinhoResponseDTOMap(List<VinhoResponseDTO> vinhos) {
        final Map<String, VinhoResponseDTO> vinhosPorCodigo = vinhos.stream()
                .collect(Collectors.toMap(
                        v -> String.valueOf(v.getCodigo()),
                        v -> v,
                        (existing, replacement) -> existing
                        // (existing, replacement) -> replacement
                ));
        return vinhosPorCodigo;
    }
}
