package com.ecommerce.auxiliar;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.controller.compras.response.CompraDetalhadaResponseDTO;
import com.ecommerce.service.auxiliar.ProcessarComprasAnual;
import com.ecommerce.service.mapper.CompraDetalhadaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProcessarComprasAnualTest {

    @InjectMocks
    private ProcessarComprasAnual processarComprasAnual;

    @InjectMocks
    private CompraDetalhadaMapper compraDetalhadaMapper;

    @Test
    void processarComprasParaRelatorioAnual_deveAdicionarComprasDetalhadasParaOAnoCorreto() {
        // Arrange
        int ano = 2023;
        List<ClientesResponseDTO> clientes = List.of(
                new ClientesResponseDTO("Cliente 1", "20623850567",  List.of(new CompraResponseDTO("1", 2), new CompraResponseDTO("2", 1))),
                new ClientesResponseDTO("Cliente 2", "04372012950", Collections.singletonList(new CompraResponseDTO("1", 1)))
        );
        Map<String, VinhoResponseDTO> vinhosPorCodigo = Map.of(
                "1", new VinhoResponseDTO("1", "Tinto Reserva", 50.0, "2023-01-15", "2023"),
                "2", new VinhoResponseDTO("2", "Branco", 30.0, "2024-02-20", "2024")
        );
        List<CompraDetalhadaResponseDTO> comprasDetalhadas = new ArrayList<>();

        // Act
        processarComprasAnual.processarComprasParaRelatorioAnual(ano, clientes, vinhosPorCodigo, comprasDetalhadas);

        // Assert
        assertEquals(2, comprasDetalhadas.size());

        CompraDetalhadaResponseDTO compra1 = comprasDetalhadas.stream()
                .filter(c -> c.getCodigoProduto().equals("1") && c.getNomeCliente().equals("Cliente 1"))
                .findFirst()
                .orElse(null); // <-- Linha 58
        assertNotNull(compra1);
        assertEquals(100.0, compra1.getValorTotal());

    }

    @Test
    void processarComprasParaRelatorioAnual_naoDeveAdicionarComprasParaAnoDiferente() {
        // Arrange
        int ano = 2024;
        List<ClientesResponseDTO> clientes = List.of(
                new ClientesResponseDTO("1", "Cliente 1", Collections.singletonList(new CompraResponseDTO("1", 2)))
        );
        Map<String, VinhoResponseDTO> vinhosPorCodigo = Map.of(
                "1", new VinhoResponseDTO("1", "Tinto", 50.0, "2023-01-15", "2023")
        );
        List<CompraDetalhadaResponseDTO> comprasDetalhadas = new ArrayList<>();

        // Act
        processarComprasAnual.processarComprasParaRelatorioAnual(ano, clientes, vinhosPorCodigo, comprasDetalhadas);

        // Assert
        assertTrue(comprasDetalhadas.isEmpty());
    }

    @Test
    void processarComprasParaRelatorioAnual_deveIgnorarComprasComVinhoNulo() {
        // Arrange
        int ano = 2023;
        List<ClientesResponseDTO> clientes = List.of(
                new ClientesResponseDTO("1", "Cliente 1", Collections.singletonList(new CompraResponseDTO("1", 2)))
        );
        Map<String, VinhoResponseDTO> vinhosPorCodigo = Collections.emptyMap(); // Vinho com código "1" não existe
        List<CompraDetalhadaResponseDTO> comprasDetalhadas = new ArrayList<>();

        // Act
        processarComprasAnual.processarComprasParaRelatorioAnual(ano, clientes, vinhosPorCodigo, comprasDetalhadas);

        // Assert
        assertTrue(comprasDetalhadas.isEmpty());
    }

    @Test
    void processarComprasParaRelatorioAnual_deveIgnorarComprasComAnoCompraNulo() {
        // Arrange
        int ano = 2023;
        List<ClientesResponseDTO> clientes = List.of(
                new ClientesResponseDTO("1", "Cliente 1", Collections.singletonList(new CompraResponseDTO("1", 2)))
        );
        Map<String, VinhoResponseDTO> vinhosPorCodigo = Map.of(
                "1", new VinhoResponseDTO("1", "Tinto", 50.0, null, null)
        );
        List<CompraDetalhadaResponseDTO> comprasDetalhadas = new ArrayList<>();

        // Act
        processarComprasAnual.processarComprasParaRelatorioAnual(ano, clientes, vinhosPorCodigo, comprasDetalhadas);

        // Assert
        assertTrue(comprasDetalhadas.isEmpty());
    }
}
