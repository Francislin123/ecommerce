package com.ecommerce.service.support;

import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RecomendadorDeVinhos {

    public VinhoResponseDTO recomendar(List<CompraResponseDTO> compras, List<VinhoResponseDTO> vinhosDisponiveis) {
        if (compras == null || vinhosDisponiveis == null) return null;

        // Cria um mapa de codigo -> tipoVinho
        Map<String, String> tipoPorCodigo = vinhosDisponiveis.stream()
                .collect(Collectors.toMap(
                        v -> String.valueOf(v.getCodigo()),
                        VinhoResponseDTO::getTipoVinho,
                        (existing, replacement) -> existing // em caso de conflito
                ));

        // Agrupa quantidades por tipo de vinho
        Map<String, Integer> tipoContagem = new HashMap<>();
        for (CompraResponseDTO compra : compras) {
            String tipo = tipoPorCodigo.get(compra.getCodigo());
            if (tipo != null) {
                tipoContagem.merge(tipo, compra.getQuantidade(), Integer::sum);
            }
        }

        // Encontra o tipo mais comprado
        Optional<String> tipoMaisComprado = tipoContagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        // Retorna um vinho desse tipo
        if (tipoMaisComprado.isPresent()) {
            for (VinhoResponseDTO vinho : vinhosDisponiveis) {
                if (tipoMaisComprado.get().equalsIgnoreCase(vinho.getTipoVinho())) {
                    return vinho;
                }
            }
        }
        return null;
    }
}