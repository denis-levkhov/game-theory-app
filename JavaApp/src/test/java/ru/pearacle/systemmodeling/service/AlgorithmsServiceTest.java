package ru.pearacle.systemmodeling.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.pearacle.systemmodeling.client.PythonAlgorithmsServerClient;
import ru.pearacle.systemmodeling.dto.NashResponseDto;
import ru.pearacle.systemmodeling.dto.NloResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedResultDto;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlgorithmsServiceTest {

    @Mock
    private PythonAlgorithmsServerClient pythonAlgorithmsServerClient;

    @InjectMocks
    private AlgorithmsService algorithmsService;

    @Test
    void deleteDominatedStrategies_shouldReturnDominatedStrategies_whenStrictDominationByRows() {
        List<List<Integer>> matrix = Arrays.asList(
                Arrays.asList(3, 3),
                Arrays.asList(1, 1)
        );

        StrategyDominatedDto dto = new StrategyDominatedDto(matrix, true, true);

        StrategyDominatedResultDto result = algorithmsService.deleteDominatedStrategies(dto);

        assertThat(result.getDominationType()).isEqualTo("строго");
        assertThat(result.getTarget()).isEqualTo("СТРОКИ (первый игрок)");
        assertThat(result.getDominatedStrategies()).hasSize(1);

        StrategyDominatedResultDto.DominatedStrategy dominated = result.getDominatedStrategies().get(0);
        assertThat(dominated.getIndex()).isEqualTo(1);
        assertThat(dominated.getValues()).containsExactly(1, 1);
    }

    @Test
    void deleteDominatedStrategies_shouldReturnEmptyList_whenNoDomination() {
        List<List<Integer>> matrix = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(2, 1)
        );
        StrategyDominatedDto dto = new StrategyDominatedDto(matrix, true, true);

        StrategyDominatedResultDto result = algorithmsService.deleteDominatedStrategies(dto);

        assertThat(result.getDominatedStrategies()).isEmpty();
    }

    @Test
    void deleteDominatedStrategies_shouldReturnDominatedStrategies_whenWeakDominationByColumns() {
        List<List<Integer>> matrix = Arrays.asList(
                Arrays.asList(2, 2),
                Arrays.asList(1, 1)
        );

        StrategyDominatedDto dto = new StrategyDominatedDto(matrix, false, false);

        StrategyDominatedResultDto result = algorithmsService.deleteDominatedStrategies(dto);

        assertThat(result.getDominationType()).isEqualTo("слабо");
        assertThat(result.getTarget()).isEqualTo("СТОЛБЦЫ (второй игрок)");
        assertThat(result.getDominatedStrategies()).hasSize(2);
    }

    @Test
    void findNashCleanStrategy_shouldDelegateToPythonClient() {
        StrategyDto request = new StrategyDto(List.of(), List.of());
        NashResponseDto expectedResponse = new NashResponseDto(List.of());
        when(pythonAlgorithmsServerClient.findNashClearStrategy(request)).thenReturn(expectedResponse);

        NashResponseDto response = algorithmsService.findNashCleanStrategy(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(pythonAlgorithmsServerClient).findNashClearStrategy(request);
    }

    @Test
    void deleteNloStrategy_shouldDelegateToPythonClient() {
        StrategyDto request = new StrategyDto(List.of(), List.of());
        NloResponseDto expectedResponse = new NloResponseDto(List.of(), List.of());
        when(pythonAlgorithmsServerClient.deleteNloStrategy(request)).thenReturn(expectedResponse);

        NloResponseDto response = algorithmsService.deleteNloStrategy(request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(pythonAlgorithmsServerClient).deleteNloStrategy(request);
    }
}