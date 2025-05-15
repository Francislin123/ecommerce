package com.ecommerce.service;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.controller.clientes.response.CompraResponseDTO;
import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.service.clientes.ClientesService;
import com.ecommerce.service.clientes.ClientesServiceImpl;
import com.ecommerce.service.integracao.client.Client;
import com.ecommerce.service.integracao.compras.VinhoClient;
import com.ecommerce.service.support.RecomendadorDeVinhos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClientesServiceImplTest {

    @Mock
    private Client clienteClient;

    @Mock
    private VinhoClient vinhoClient;

    @Mock
    private RecomendadorDeVinhos recomendadorDeVinhos;

    @InjectMocks
    private ClientesService clientesService = new ClientesServiceImpl();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTop3ClientesFieis() {
        ClientesResponseDTO cliente1 = new ClientesResponseDTO("João", "111", Arrays.asList(
                new CompraResponseDTO("1", 10),
                new CompraResponseDTO("2", 5)
        ));
        ClientesResponseDTO cliente2 = new ClientesResponseDTO("Maria", "222", List.of(
                new CompraResponseDTO("3", 8)
        ));
        ClientesResponseDTO cliente3 = new ClientesResponseDTO("Pedro", "333", List.of(
                new CompraResponseDTO("4", 15)
        ));
        ClientesResponseDTO cliente4 = new ClientesResponseDTO("Ana", "444", List.of(
                new CompraResponseDTO("5", 2)
        ));

        when(clienteClient.clientClientesFieis()).thenReturn(List.of(cliente1, cliente2, cliente3, cliente4));

        List<ClientesResponseDTO> top3 = clientesService.clientesFieis();

        assertEquals(3, top3.size());
        assertEquals("João", top3.get(0).getNome());
        assertEquals("Pedro", top3.get(1).getNome());
        assertEquals("Maria", top3.get(2).getNome());
    }

    @Test
    void deveRecomendarVinhoBaseadoNoTipoMaisComprado() {
        String cpf = "12345678900";

        List<CompraResponseDTO> compras = List.of(
                new CompraResponseDTO("1", 5),
                new CompraResponseDTO("2", 3)
        );

        ClientesResponseDTO cliente = new ClientesResponseDTO("Cliente Teste", cpf, compras);
        List<ClientesResponseDTO> clientes = List.of(cliente);

        List<VinhoResponseDTO> vinhos = List.of(
                new VinhoResponseDTO("1", "Tinto"),
                new VinhoResponseDTO("2", "Branco")
        );

        VinhoResponseDTO vinhoRecomendado = new VinhoResponseDTO("1", "Tinto");

        when(clienteClient.clientClientesFieis()).thenReturn(clientes);
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(vinhos);
        when(recomendadorDeVinhos.recomendar(compras, vinhos)).thenReturn(vinhoRecomendado);

        Optional<VinhoResponseDTO> resultado = clientesService.recomendarVinhoPorTipo(cpf);

        assertTrue(resultado.isPresent());
        assertEquals("Tinto", resultado.get().getTipoVinho());
    }

    @Test
    void deveLancarExcecaoSeClienteNaoEncontrado() {
        when(clienteClient.clientClientesFieis()).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clientesService.recomendarVinhoPorTipo("000");
        });

        assertEquals("Cliente não encontrado para o CPF: " + "000" , exception.getMessage());
    }

    @Test
    void deveRetornarEmptySeClienteNaoTemComprasRelacionadasAoTipo() {
        String cpf = "999";
        ClientesResponseDTO cliente = new ClientesResponseDTO("Sem compras", cpf, List.of(
                new CompraResponseDTO("99", 1) // código que não existe em vinhos disponíveis
        ));

        VinhoResponseDTO vinho1 = new VinhoResponseDTO("10", "Merlot");

        when(clienteClient.clientClientesFieis()).thenReturn(List.of(cliente));
        when(vinhoClient.recomendacaoDeVinho()).thenReturn(List.of(vinho1));

        Optional<VinhoResponseDTO> recomendacao = clientesService.recomendarVinhoPorTipo(cpf);

        assertTrue(recomendacao.isEmpty());
    }
}
