package com.ecommerce.service.cliente;

import com.ecommerce.controller.response.ClientesResponseDTO;
import com.ecommerce.controller.response.CompraResponseDTO;
import com.ecommerce.controller.response.VinhoResponseDTO;
import com.ecommerce.service.integration.client.Client;
import com.ecommerce.service.integration.compras.VinhoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ClientesServiceImpl implements ClientesService {

    @Autowired
    private Client clienteClient;

    @Autowired
    private VinhoClient vinhoClient;

    @Override
    public List<ClientesResponseDTO> clientesFieis() {
        return clienteClient.clientClientesFieis().stream().sorted((p1, p2) -> {
            int total1 = p1.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
            int total2 = p2.getCompras().stream().mapToInt(CompraResponseDTO::getQuantidade).sum();
            return Integer.compare(total2, total1);
        }).limit(3).collect(Collectors.toList());
    }

    @Override
    public Optional<VinhoResponseDTO> recomendarVinhoPorTipo(final String cpf) {

        ClientesResponseDTO clientesResponseDTO = clienteClient.clientClientesFieis()
                .stream().filter(c -> cpf.equals(c.getCpf()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        List<CompraResponseDTO> compras = clientesResponseDTO.getCompras();

        List<Map<String, Object>> listaDeVinhos = List.of(
                Map.of("codigo", 1, "tipo_vinho", "Tinto", "preco", 229.99, "safra", "2017", "ano_compra", 2018),
                Map.of("codigo", 2, "tipo_vinho", "Branco", "preco", 126.50, "safra", "2018", "ano_compra", 2019),
                Map.of("codigo", 3, "tipo_vinho", "Rosé", "preco", 121.75, "safra", "2019", "ano_compra", 2020),
                Map.of("codigo", 4, "tipo_vinho", "Espumante", "preco", 134.25, "safra", "2020", "ano_compra", 2021),
                Map.of("codigo", 5, "tipo_vinho", "Chardonnay", "preco", 128.99, "safra", "2021", "ano_compra", 2022),
                Map.of("codigo", 6, "tipo_vinho", "Tinto", "preco", 327.50, "safra", "2016", "ano_compra", 2017),
                Map.of("codigo", 7, "tipo_vinho", "Branco", "preco", 125.25, "safra", "2017", "ano_compra", 2018),
                Map.of("codigo", 8, "tipo_vinho", "Rosé", "preco", 120.99, "safra", "2018", "ano_compra", 2019),
                Map.of("codigo", 9, "tipo_vinho", "Espumante", "preco", 135.50, "safra", "2019", "ano_compra", 2020),
                Map.of("codigo", 10, "tipo_vinho", "Chardonnay", "preco", 130.75, "safra", "2020", "ano_compra", 2021),
                Map.of("codigo", 11, "tipo_vinho", "Tinto", "preco", 128.99, "safra", "2017", "ano_compra", 2018),
                Map.of("codigo", 12, "tipo_vinho", "Branco", "preco", 106.50, "safra", "2018", "ano_compra", 2019),
                Map.of("codigo", 13, "tipo_vinho", "Rosé", "preco", 121.75, "safra", "2019", "ano_compra", 2020),
                Map.of("codigo", 14, "tipo_vinho", "Espumante", "preco", 134.25, "safra", "2020", "ano_compra", 2021),
                Map.of("codigo", 15, "tipo_vinho", "Chardonnay", "preco", 188.99, "safra", "2021", "ano_compra", 2022),
                Map.of("codigo", 16, "tipo_vinho", "Tinto", "preco", 127.50, "safra", "2016", "ano_compra", 2017),
                Map.of("codigo", 17, "tipo_vinho", "Branco", "preco", 125.25, "safra", "2017", "ano_compra", 2018),
                Map.of("codigo", 18, "tipo_vinho", "Rosé", "preco", 120.99, "safra", "2018", "ano_compra", 2019),
                Map.of("codigo", 19, "tipo_vinho", "Espumante", "preco", 135.50, "safra", "2019", "ano_compra", 2020),
                Map.of("codigo", 20, "tipo_vinho", "Chardonnay", "preco", 130.75, "safra", "2020", "ano_compra", 2021));

        Map<String, String> tipoPorCodigo  = criarMockVinhoMapSemRepetidos(listaDeVinhos);

        // 4. Contar quantidades por tipo
        Map<String, Integer> tipoContagem = new HashMap<>();
        for (CompraResponseDTO compra : compras) {
            String tipo = tipoPorCodigo.get(compra.getCodigo());
            if (tipo != null) {
                tipoContagem.put(tipo, tipoContagem.getOrDefault(tipo, 0) + compra.getQuantidade());
            }
        }

        // 5. Determinar o tipo mais comprado
        Optional<String> tipoMaisComprado = tipoContagem.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);

        if (tipoMaisComprado.isEmpty()) {
            return Optional.empty();
        }

        String tipoPreferido = tipoMaisComprado.get();

        // 6. Buscar um vinho recomendado do mesmo tipo
        List<VinhoResponseDTO> vinhosDisponiveis = vinhoClient.recomendacaoDeVinho();

        return vinhosDisponiveis.stream()
                .filter(v -> tipoPreferido.equalsIgnoreCase(v.getTipoVinho()))
                .findFirst();
    }

    public static Map<String, String> criarMockVinhoMapSemRepetidos(List<Map<String, Object>> listaDeVinhos) {
        return listaDeVinhos.stream()
                .collect(Collectors.toMap(
                        vinho -> String.valueOf(vinho.get("codigo")),
                        vinho -> (String) vinho.get("tipo_vinho"),
                        (existingValue, newValue) -> existingValue
                ));
    }
}
