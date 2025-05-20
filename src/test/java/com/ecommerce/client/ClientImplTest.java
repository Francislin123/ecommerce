package com.ecommerce.client;

import com.ecommerce.controller.clientes.response.ClientesResponseDTO;
import com.ecommerce.service.integracao.client.ClientImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
class ClientImplTest {

    private ClientImpl clientImpl;
    private HttpClient httpClientMock;
    private HttpResponse<String> httpResponseMock;
    private String uriClientes;

    private final String mockJson = "[{\"nome\":\"João\",\"email\":\"joao@email.com\"}]";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        httpClientMock = mock(HttpClient.class);
        httpResponseMock = mock(HttpResponse.class);
        clientImpl = new ClientImpl(httpClientMock);

        // Simula a URL externa
        Field field = ClientImpl.class.getDeclaredField("uriClientes");
        field.setAccessible(true);
        field.set(clientImpl, "http://fakeurl.com/clientes");
    }

    @Test
    void deveRetornarListaDeClientes_quandoRespostaForValida() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(mockJson);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        List<ClientesResponseDTO> clientes = clientImpl.clientClientesFieis();

        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        assertEquals("João", clientes.get(0).getNome());
    }

    @Test
    void deveRetornarListaVazia_quandoStatus404() throws IOException, InterruptedException {
        when(httpResponseMock.statusCode()).thenReturn(404);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        List<ClientesResponseDTO> clientes = clientImpl.clientClientesFieis();

        assertNotNull(clientes);
        assertTrue(clientes.isEmpty());
    }

    @Test
    void deveLancarRuntimeException_quandoIOExceptionOuInterrupted() throws IOException, InterruptedException {
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Erro de IO"));

        assertThrows(RuntimeException.class, () -> clientImpl.clientClientesFieis());
    }
}
