package ru.pearacle.systemmodeling.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.pearacle.systemmodeling.dto.NashResponseDto;
import ru.pearacle.systemmodeling.dto.NloResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedResultDto;
import ru.pearacle.systemmodeling.dto.StrategyDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class PettyStringUtil {

    public String toPrettyString(StrategyDominatedResultDto strategyDominatedDto, StrategyDominatedDto strategyDominatedDtoInput) {
        StringBuilder sb = new StringBuilder();

        sb.append("Исходная матрица:\n");
        strategyDominatedDtoInput.matrix().forEach(row ->
                sb.append(row.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", ")))
                        .append("\n")
        );

        sb.append("\nТип доминации: ").append(strategyDominatedDto.getDominationType()).append("\n");
        sb.append("Цель: ").append(strategyDominatedDto.getTarget()).append("\n");

        sb.append("\nУдалённые стратегии:\n");
        for (StrategyDominatedResultDto.DominatedStrategy strategy : strategyDominatedDto.getDominatedStrategies()) {
            sb.append("- Индекс: ").append(strategy.getIndex())
                    .append(", Значения: ")
                    .append(strategy.getValues().stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(", ")))
                    .append("\n");
        }

        sb.append("\nФинальная матрица после удаления доминированных стратегий:\n");

        List<List<Integer>> modifiedMatrix = removeDominatedStrategies(strategyDominatedDtoInput.matrix(), strategyDominatedDto.getDominatedStrategies(), strategyDominatedDtoInput.checkRows());

        modifiedMatrix.forEach(row ->
                sb.append(row.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", ")))
                        .append("\n")
        );

        return sb.toString();
    }

    private List<List<Integer>> removeDominatedStrategies(List<List<Integer>> matrix, List<StrategyDominatedResultDto.DominatedStrategy> dominatedStrategies, Boolean checkRows) {
        List<List<Integer>> modifiedMatrix = new ArrayList<>(matrix);
        List<Integer> indicesToRemove = new ArrayList<>();

        for (StrategyDominatedResultDto.DominatedStrategy dominatedStrategy : dominatedStrategies) {
            indicesToRemove.add(dominatedStrategy.getIndex());
        }

        if (checkRows) {
            indicesToRemove.sort((a, b) -> Integer.compare(b, a));
            for (int index : indicesToRemove) {
                modifiedMatrix.remove(index);
            }
        } else {
            for (List<Integer> row : modifiedMatrix) {
                for (int i = indicesToRemove.size() - 1; i >= 0; i--) {
                    row.remove((int) indicesToRemove.get(i));
                }
            }
        }

        if (modifiedMatrix.isEmpty()) {
            log.warn("После удаления доминированных стратегий, матрица пустая.");
        }
        return modifiedMatrix;
    }

    public String toPrettyString(NashResponseDto nashResponseDto) {
        if (nashResponseDto.common_positions() == null || nashResponseDto.common_positions().isEmpty()) {
            return "Общие позиции отсутствуют.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Общие позиции:\n");

        for (List<Integer> position : nashResponseDto.common_positions()) {
            sb.append("- (")
                    .append(position.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(", ")))
                    .append(")\n");
        }

        return sb.toString();
    }

    public String toPrettyString(NloResponseDto nloResponseDto, StrategyDto strategyDto) {
        StringBuilder sb = new StringBuilder();

        sb.append("Исходная матрица 1 (первый игрок):\n");
        strategyDto.matrix1().forEach(row -> sb.append(row.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                + "\n"));

        sb.append("\nИсходная матрица 2 (второй игрок):\n");
        strategyDto.matrix2().forEach(row -> sb.append(row.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                + "\n"));

        if (nloResponseDto.missing_rows_matrix1() != null && !nloResponseDto.missing_rows_matrix1().isEmpty()) {
            sb.append("\nСтроки первой матрицы, где максимум не был найден: ")
                    .append(nloResponseDto.missing_rows_matrix1())
                    .append("\n");
        } else {
            sb.append("\nВ первой матрице все строки содержат максимум в каком-то столбце.\n");
        }

        if (nloResponseDto.missing_cols_matrix2() != null && !nloResponseDto.missing_cols_matrix2().isEmpty()) {
            sb.append("\nСтолбцы второй матрицы, где максимум не был найден: ")
                    .append(nloResponseDto.missing_cols_matrix2())
                    .append("\n");
        } else {
            sb.append("\nВо второй матрице все столбцы содержат максимум в какой-то строке.\n");
        }

        List<Integer> missingRows = nloResponseDto.missing_rows_matrix1();
        List<List<Integer>> modifiedMatrix1 = strategyDto.matrix1().stream()
                .filter(row -> !missingRows.contains(strategyDto.matrix1().indexOf(row)))
                .toList();

        List<List<Integer>> modifiedMatrix2 = getLists(nloResponseDto, strategyDto);

        sb.append("\nФинальная матрица 1 (первый игрок) после удаления строк с отсутствием максимума:\n");
        modifiedMatrix1.forEach(row -> sb.append(row.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                + "\n"));

        sb.append("\nФинальная матрица 2 (второй игрок) после удаления столбцов с отсутствием максимума:\n");
        modifiedMatrix2.forEach(row -> sb.append(row.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))
                + "\n"));

        return sb.toString();
    }

    private static List<List<Integer>> getLists(NloResponseDto nloResponseDto, StrategyDto strategyDto) {
        List<Integer> missingCols = nloResponseDto.missing_cols_matrix2();
        List<List<Integer>> modifiedMatrix2 = new ArrayList<>();

        for (List<Integer> row : strategyDto.matrix2()) {
            List<Integer> modifiedRow = new ArrayList<>();
            for (int colIndex = 0; colIndex < row.size(); colIndex++) {
                if (!missingCols.contains(colIndex)) {
                    modifiedRow.add(row.get(colIndex));
                }
            }
            modifiedMatrix2.add(modifiedRow);
        }
        return modifiedMatrix2;
    }
}
