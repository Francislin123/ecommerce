package com.ecommerce.service.support;

import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RecomendadorDeVinhos {

    private static final Logger logger = LoggerFactory.getLogger(RecomendadorDeVinhos.class);

    public Optional<VinhoResponseDTO> recomendar(List<CompraResponseDTO> compras, List<VinhoResponseDTO> vinhosDisponiveis) {
        if (compras == null || vinhosDisponiveis == null) {
            logger.warn("Lista de compras ou vinhos disponíveis está nula.");
            return Optional.empty();
        }

        logger.info("Iniciando recomendação com {} compras e {} vinhos disponíveis.", compras.size(), vinhosDisponiveis.size());

        Map<String, String> tipoPorCodigo = vinhosDisponiveis.stream()
                .collect(Collectors.toMap(
                        v -> String.valueOf(v.getCodigo()),
                        VinhoResponseDTO::getTipoVinho,
                        (existing, replacement) -> existing
                ));

        Map<String, Integer> tipoContagem = new HashMap<>();
        for (CompraResponseDTO compra : compras) {
            String tipo = tipoPorCodigo.get(compra.getCodigo());
            if (tipo != null) {
                tipoContagem.merge(tipo, compra.getQuantidade(), Integer::sum);
            }
        }

        logger.debug("Contagem por tipo de vinho: {}", tipoContagem);

        return tipoContagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .flatMap(tipoMaisComprado ->
                        vinhosDisponiveis.stream()
                                .filter(v -> tipoMaisComprado.equalsIgnoreCase(v.getTipoVinho()))
                                .findFirst()
                )
                .map(vinho -> {
                    logger.info("Vinho recomendado: {}", vinho);
                    return vinho;
                });
    }
}