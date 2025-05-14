package com.ecommerce.service;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.controller.compras.request.CompraDetalhadaResponseDTO;
import com.ecommerce.service.auxiliar.GetStringVinhoResponse;
import com.ecommerce.service.auxiliar.ProcessarComprasAnual;
import com.ecommerce.service.compras.ComprasService;
import com.ecommerce.service.compras.ComprasServiceImpl;
import com.ecommerce.service.integracao.client.Client;
import com.ecommerce.service.integracao.compras.VinhoClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ComprasServiceImplTest {

    @Mock
    private Client clienteClient;

    @Mock
    private VinhoClient vinhoClient;

    @Mock
    private ProcessarComprasAnual relatorioAnual;

    @Mock
    private GetStringVinhoResponse getVinhoResponse;

    @InjectMocks
    private ComprasService comprasService = new ComprasServiceImpl();

    @Test
    void listarComprasOrdenadas_deveRetornarListaDeComprasDetalhadas() {
        // Arrange
        List<ClientesResponseDTO> clientes = List.of(
                new ClientesResponseDTO( "Cliente 1", "12345678901", Collections.singletonList(new CompraResponseDTO("1", 2))),
                new ClientesResponseDTO( "Cliente 2", "98765432109", Collections.singletonList(new CompraResponseDTO("2", 1)))
        );
        List<VinhoResponseDTO> vinhos = List.of(
                new VinhoResponseDTO("1", "Tinto", 50.0, "2023-01-01", "2023"),
                new VinhoResponseDTO("2", "Branco", 30.0, "2024-02-15", "2024")
        );
        Map<String, VinhoResponseDTO> vinhosPorCodigo = Map.of(
                "1", vinhos.get(0),
                "2", vinhos.get(1)
        );
        List<CompraDetalhadaResponseDTO> comprasDetalhadasMock = List.of(
                new CompraDetalhadaResponseDTO("Cliente 1", "12345678901", "1", "Tinto", "Tinto", 2, 50.0, 100.0),
                new CompraDetalhadaResponseDTO("Cliente 2", "98765432109", "2", "Branco", "Branco", 1, 30.0, 30.0)
        );

        when(clienteClient.clientClientesFieis()).thenReturn(clientes);
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(vinhos);
        when(getVinhoResponse.getStringVinhoResponseDTOMap(vinhos)).thenReturn(vinhosPorCodigo);
        when(relatorioAnual.getCompraDetalhadaResponseDTOS(clientes, vinhosPorCodigo)).thenReturn(comprasDetalhadasMock);

        // Act
        List<CompraDetalhadaResponseDTO> result = comprasService.listarComprasOrdenadas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cliente 1", result.get(0).getNomeCliente());
        assertEquals("Branco", result.get(1).getTipoVinho());

        verify(clienteClient, times(1)).clientClientesFieis();
        verify(vinhoClient, times(1)).recomendacaoDeVinho();
        verify(getVinhoResponse, times(1)).getStringVinhoResponseDTOMap(vinhos);
        verify(relatorioAnual, times(1)).getCompraDetalhadaResponseDTOS(clientes, vinhosPorCodigo);
    }

    @Test
    void maiorCompraDoAno_deveRetornarAMaiorCompraDetalhadaDoAno() {
        // Arrange
        int ano = 2023;
        List<ClientesResponseDTO> clientes = List.of(
                new ClientesResponseDTO( "Cliente 1", "123", Collections.singletonList(new CompraResponseDTO("1", 2))),
                new ClientesResponseDTO( "Cliente 2", "456", Collections.singletonList(new CompraResponseDTO("2", 1)))
        );
        List<VinhoResponseDTO> vinhos = List.of(
                new VinhoResponseDTO("1", "Tinto", 50.0, "2023-01-01", "2023"),
                new VinhoResponseDTO("2", "Branco", 30.0, "2024-02-15", "2024")
        );
        Map<String, VinhoResponseDTO> vinhosPorCodigo = Map.of(
                "1", vinhos.get(0),
                "2", vinhos.get(1)
        );
        List<CompraDetalhadaResponseDTO> comprasDetalhadasMock = List.of(
                new CompraDetalhadaResponseDTO("Cliente 1", "123", "1", "Tinto", "Tinto", 2, 50.0, 100.0),
                new CompraDetalhadaResponseDTO("Cliente 2", "456", "1", "Tinto", "Tinto", 1, 50.0, 50.0)
        );

        when(clienteClient.clientClientesFieis()).thenReturn(clientes);
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(vinhos);
        doAnswer(invocation -> {
            int inputAno = invocation.getArgument(0);
            List<ClientesResponseDTO> inputClientes = invocation.getArgument(1);
            Map<String, VinhoResponseDTO> inputVinhosPorCodigo = invocation.getArgument(2);
            List<CompraDetalhadaResponseDTO> outputList = invocation.getArgument(3);
            for (ClientesResponseDTO cliente : inputClientes) {
                for (CompraResponseDTO compra : cliente.getCompras()) {
                    VinhoResponseDTO vinho = inputVinhosPorCodigo.get(compra.getCodigo());
                    if (vinho != null && vinho.getAnoCompra() != null && vinho.getAnoCompra().startsWith(String.valueOf(inputAno))) {
                        double valorTotal = compra.getQuantidade() * vinho.getPreco();
                        outputList.add(new CompraDetalhadaResponseDTO(cliente.getNome(), cliente.getCpf(), String.valueOf(vinho.getCodigo()), vinho.getTipoVinho(), vinho.getTipoVinho(), compra.getQuantidade(), vinho.getPreco(), valorTotal));
                    }
                }
            }
            return null;
        }).when(relatorioAnual).processarComprasParaRelatorioAnual(eq(ano), anyList(), anyMap(), anyList());

        // Act
        CompraDetalhadaResponseDTO result = comprasService.maiorCompraDoAno(ano);

        // Assert
        assertNotNull(result);
        assertEquals(100.0, result.getValorTotal());
        assertEquals("Cliente 1", result.getNomeCliente());

        verify(clienteClient, times(1)).clientClientesFieis();
        verify(vinhoClient, times(1)).recomendacaoDeVinho();
        verify(relatorioAnual, times(1)).processarComprasParaRelatorioAnual(eq(ano), anyList(), anyMap(), anyList());
    }

    @Test
    void maiorCompraDoAno_deveLancarRuntimeException_quandoNenhumaCompraEncontradaParaOAno() {
        // Arrange
        int ano = 2025;
        when(clienteClient.clientClientesFieis()).thenReturn(Collections.emptyList());
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(Collections.emptyList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> comprasService.maiorCompraDoAno(ano));
        assertEquals("Nenhuma compra encontrada para o ano 2025", exception.getMessage());

        verify(clienteClient, times(1)).clientClientesFieis();
        verify(vinhoClient, times(1)).recomendacaoDeVinho();
        verify(relatorioAnual, times(1)).processarComprasParaRelatorioAnual(eq(ano), anyList(), anyMap(), anyList());
    }
}
