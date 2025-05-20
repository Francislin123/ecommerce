package com.ecommerce.client;

import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.service.integracao.compras.VinhoClientImpl;
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
class VinhoClientImplTest {

    private VinhoClientImpl vinhoClientImpl;
    private HttpClient httpClientMock;
    private HttpResponse<String> httpResponseMock;

    private final String mockJson = "[\n" +
            "  {\n" +
            "    \"codigo\": 1,\n" +
            "    \"tipo_vinho\": \"Tinto\",\n" +
            "    \"preco\": 229.99,\n" +
            "    \"safra\": \"2017\",\n" +
            "    \"ano_compra\": 2018\n" +
            "  },\n" +
            "  {\n" +
            "    \"codigo\": 2,\n" +
            "    \"tipo_vinho\": \"Branco\",\n" +
            "    \"preco\": 126.50,\n" +
            "    \"safra\": \"2018\",\n" +
            "    \"ano_compra\": 2019\n" +
            "  },\n" +
            "  {\n" +
            "    \"codigo\": 3,\n" +
            "    \"tipo_vinho\": \"Rosé\",\n" +
            "    \"preco\": 121.75,\n" +
            "    \"safra\": \"2019\",\n" +
            "    \"ano_compra\": 2020\n" +
            "  }\n" +
            "]";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        httpClientMock = mock(HttpClient.class);
        httpResponseMock = mock(HttpResponse.class);
        vinhoClientImpl = new VinhoClientImpl(httpClientMock);

        // Simula a URL externa
        Field field = VinhoClientImpl.class.getDeclaredField("uriVinhos");
        field.setAccessible(true);
        field.set(vinhoClientImpl, "http://fakeurl.com/vinhos");
    }

    @Test
    void deveRetornarListaDeVinhos_quandoRespostaForValida() throws Exception {
        when(httpResponseMock.statusCode()).thenReturn(200);
        when(httpResponseMock.body()).thenReturn(mockJson);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        List<VinhoResponseDTO> vinhos = vinhoClientImpl.recomendacaoDeVinho();

        assertNotNull(vinhos);
        assertEquals(3, vinhos.size());
        assertEquals("Tinto", vinhos.get(0).getTipoVinho());
    }

    @Test
    void deveRetornarListaVazia_quandoStatus404() throws Exception {
        when(httpResponseMock.statusCode()).thenReturn(404);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponseMock);

        List<VinhoResponseDTO> vinhos = vinhoClientImpl.recomendacaoDeVinho();

        assertNotNull(vinhos);
        assertTrue(vinhos.isEmpty());
    }

    @Test
    void deveLancarRuntimeException_quandoIOExceptionOuInterrupted() throws Exception {
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Erro"));

        assertThrows(RuntimeException.class, () -> vinhoClientImpl.recomendacaoDeVinho());
    }
}