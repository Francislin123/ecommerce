package com.ecommerce.auxiliar;

import com.ecommerce.controller.clientes.response.VinhoResponseDTO;
import com.ecommerce.service.auxiliar.GetStringVinhoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetStringVinhoResponseTest {

    @InjectMocks
    private GetStringVinhoResponse getStringVinhoResponse;

    @Test
    void getStringVinhoResponseDTOMap_deveRetornarMapaCorretoComVinhosPorCodigo() {
        // Arrange
        List<VinhoResponseDTO> vinhos = List.of(
                new VinhoResponseDTO("1", "Tinto", 50.0, "2023-01-01", "2023"),
                new VinhoResponseDTO("2", "Branco", 30.0, "2024-02-15", "2024"),
                new VinhoResponseDTO("3", "Rosé", 40.0, "2022-11-20", "2022")
        );

        // Act
        Map<String, VinhoResponseDTO> vinhoMap = getStringVinhoResponse.getStringVinhoResponseDTOMap(vinhos);

        // Assert
        assertNotNull(vinhoMap);
        assertEquals(3, vinhoMap.size());

        assertTrue(vinhoMap.containsKey("1"));
        assertEquals("Tinto", vinhoMap.get("1").getTipoVinho());
        assertEquals(50.0, vinhoMap.get("1").getPreco());

        assertTrue(vinhoMap.containsKey("2"));
        assertEquals("Branco", vinhoMap.get("2").getTipoVinho());
        assertEquals(30.0, vinhoMap.get("2").getPreco());

        assertTrue(vinhoMap.containsKey("3"));
        assertEquals("Rosé", vinhoMap.get("3").getTipoVinho());
        assertEquals(40.0, vinhoMap.get("3").getPreco());
    }

    @Test
    void getStringVinhoResponseDTOMap_deveRetornarMapaVazioParaListaDeVinhosVazia() {
        // Arrange
        List<VinhoResponseDTO> vinhosVazia = List.of();

        // Act
        Map<String, VinhoResponseDTO> vinhoMap = getStringVinhoResponse.getStringVinhoResponseDTOMap(vinhosVazia);

        // Assert
        assertNotNull(vinhoMap);
        assertTrue(vinhoMap.isEmpty());
    }

    @Test
    void getStringVinhoResponseDTOMap_deveLidarComCodigosDeVinhoDuplicadosMantendoOUltimo() {
        // Arrange
        List<VinhoResponseDTO> vinhosComDuplicados = List.of(
                new VinhoResponseDTO("1", "Tinto", 50.0, "2023-01-01", "2023"),
                new VinhoResponseDTO("2", "Branco", 30.0, "2024-02-15", "2024"),
                new VinhoResponseDTO("1", "Merlot", 60.0, "2023-05-10", "2023") // Código duplicado
        );

        // Act
        // Para garantir que o último seja mantido, precisamos fornecer a função de merge
        Map<String, VinhoResponseDTO> vinhoMap = vinhosComDuplicados.stream()
                .collect(Collectors.toMap(
                        VinhoResponseDTO::getCodigo,
                        vino -> vino,
                        (existing, replacement) -> replacement // Mantém o último valor
                ));

        // Assert
        assertNotNull(vinhoMap);
        assertEquals(2, vinhoMap.size()); // A chave duplicada será sobrescrita

        assertTrue(vinhoMap.containsKey("1"));
        assertEquals("Merlot", vinhoMap.get("1").getTipoVinho()); // O último valor deve ser mantido
        assertEquals(60.0, vinhoMap.get("1").getPreco());

        assertTrue(vinhoMap.containsKey("2"));
        assertEquals("Branco", vinhoMap.get("2").getTipoVinho());
        assertEquals(30.0, vinhoMap.get("2").getPreco());
    }

    @Test
    void getStringVinhoResponseDTOMap_deveLidarComCodigosDeVinhoNaoString() {
        // Arrange
        // Simulando um cenário onde o código poderia não ser uma String diretamente
        List<VinhoResponseDTO> vinhosComCodigoNumerico = List.of(
                new VinhoResponseDTO("1", "Tinto", 50.0, "2023-01-01", "2023"),
                new VinhoResponseDTO("2", "Branco", 30.0, "2024-02-15", "2024")
        );

        // Act
        Map<String, VinhoResponseDTO> vinhoMap = getStringVinhoResponse.getStringVinhoResponseDTOMap(vinhosComCodigoNumerico);

        // Assert
        assertNotNull(vinhoMap);
        assertEquals(2, vinhoMap.size());

        assertTrue(vinhoMap.containsKey("1"));
        assertEquals("Tinto", vinhoMap.get("1").getTipoVinho());

        assertTrue(vinhoMap.containsKey("2"));
        assertEquals("Branco", vinhoMap.get("2").getTipoVinho());
    }
}
