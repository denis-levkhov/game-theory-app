package ru.pearacle.systemmodeling.dto;

import java.util.List;

public record NloResponseDto(List<Integer> missing_rows_matrix1, List<Integer> missing_cols_matrix2) {
}
