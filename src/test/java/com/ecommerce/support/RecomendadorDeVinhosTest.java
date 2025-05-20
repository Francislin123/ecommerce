package com.ecommerce.support;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.exception.ClienteNaoEncontradoException;
import com.ecommerce.service.clientes.ClientesServiceImpl;
import com.ecommerce.service.integracao.client.Client;
import com.ecommerce.service.integracao.compras.VinhoClient;
import com.ecommerce.service.support.RecomendadorDeVinhos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class RecomendadorDeVinhosTest {

    @InjectMocks
    private ClientesServiceImpl clientesService;

    @Mock
    private Client clienteClient;

    @Mock
    private VinhoClient vinhoClient;

    @Mock
    private RecomendadorDeVinhos recomendadorDeVinhos;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void recomendarVinhoPorTipo_DeveRetornarVinhoQuandoEncontrado() {
        String cpf = "12345678900";
        CompraResponseDTO compra = new CompraResponseDTO("001", 3);
        ClientesResponseDTO cliente = new ClientesResponseDTO("João", cpf, List.of(compra));
        VinhoResponseDTO vinho = new VinhoResponseDTO("001", "Cabernet");

        when(clienteClient.clientClientesFieis()).thenReturn(List.of(cliente));
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(List.of(vinho));
        when(recomendadorDeVinhos.recomendar(any(), any())).thenReturn(Optional.of(vinho));

        Optional<VinhoResponseDTO> resultado = clientesService.recomendarVinhoPorTipo(cpf);

        assertTrue(resultado.isPresent());
        assertEquals("Cabernet", resultado.get().getTipoVinho());
    }

    @Test
    void recomendarVinhoPorTipo_DeveRetornarVazioQuandoSemRecomendacao() {
        String cpf = "12345678900";
        CompraResponseDTO compra = new CompraResponseDTO("002", 2);
        ClientesResponseDTO cliente = new ClientesResponseDTO("Maria", cpf, List.of(compra));
        VinhoResponseDTO vinho = new VinhoResponseDTO("003", "Merlot");

        when(clienteClient.clientClientesFieis()).thenReturn(List.of(cliente));
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(List.of(vinho));
        when(recomendadorDeVinhos.recomendar(any(), any())).thenReturn(Optional.empty());

        Optional<VinhoResponseDTO> resultado = clientesService.recomendarVinhoPorTipo(cpf);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void recomendarVinhoPorTipo_DeveLancarExcecaoQuandoClienteNaoExiste() {
        String cpf = "00000000000";

        when(clienteClient.clientClientesFieis()).thenReturn(List.of());

        assertThrows(ClienteNaoEncontradoException.class, () ->
                clientesService.recomendarVinhoPorTipo(cpf));
    }
}