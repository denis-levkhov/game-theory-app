package ru.pearacle.systemmodeling.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.pearacle.systemmodeling.client.PythonAlgorithmsServerClient;
import ru.pearacle.systemmodeling.dto.NashResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDto;
import ru.pearacle.systemmodeling.dto.NloResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedResultDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlgorithmsService {

    private final PythonAlgorithmsServerClient pythonAlgorithmsServerClient;

    public StrategyDominatedResultDto deleteDominatedStrategies(StrategyDominatedDto strategyDto) {
        List<List<Integer>> matrix = strategyDto.matrix();
        int rows = matrix.size();
        int cols = matrix.get(0).size();
        boolean checkRows = strategyDto.checkRows();
        boolean strict = strategyDto.strict();

        List<StrategyDominatedResultDto.DominatedStrategy> dominated = new ArrayList<>();

        int outer = checkRows ? rows : cols;
        int inner = checkRows ? cols : rows;

        log.info("Начинаем поиск доминированных стратегий. Проверяем по {}. Тип доминирования: {}.",
                checkRows ? "строкам (первый игрок)" : "столбцам (второй игрок)",
                strict ? "строгое" : "слабое");

        for (int i = 0; i < outer; i++) {
            for (int j = 0; j < outer; j++) {
                if (i == j) continue;

                log.info("Сравниваем стратегию {} с стратегией {}.", i, j);

                boolean allBetter = true;
                boolean atLeastOneStrictlyBetter = false;

                for (int k = 0; k < inner; k++) {
                    int a = checkRows ? matrix.get(j).get(k) : matrix.get(k).get(j);
                    int b = checkRows ? matrix.get(i).get(k) : matrix.get(k).get(i);

                    if (a < b) {
                        log.info("На позиции {} стратегия {} не лучше стратегии {} ({} < {}). Переходим к следующей паре.",
                                k, j, i, a, b);
                        allBetter = false;
                        break;
                    }
                    if (a > b) {
                        atLeastOneStrictlyBetter = true;
                    }
                }

                if (allBetter && (!strict || atLeastOneStrictlyBetter)) {
                    log.info("Стратегия {} доминируется стратегией {}.", i, j);

                    List<Integer> values;
                    if (checkRows) {
                        values = matrix.get(i);
                    } else {
                        values = new ArrayList<>();
                        for (int row = 0; row < rows; row++) {
                            values.add(matrix.get(row).get(i));
                        }
                    }

                    dominated.add(StrategyDominatedResultDto.DominatedStrategy.builder()
                            .index(i)
                            .values(values)
                            .build());

                    break;
                }
            }
        }

        String target = checkRows ? "СТРОКИ (первый игрок)" : "СТОЛБЦЫ (второй игрок)";
        String dominationType = strict ? "строго" : "слабо";

        log.info("Поиск завершён. Найдено {} доминированных стратегий.", dominated.size());

        return StrategyDominatedResultDto.builder()
                .dominationType(dominationType)
                .target(target)
                .dominatedStrategies(dominated)
                .build();
    }

    public NashResponseDto findNashCleanStrategy(StrategyDto request) {
        return pythonAlgorithmsServerClient.findNashClearStrategy(request);
    }

    public NloResponseDto deleteNloStrategy(StrategyDto request) {
        return pythonAlgorithmsServerClient.deleteNloStrategy(request);
    }
}
