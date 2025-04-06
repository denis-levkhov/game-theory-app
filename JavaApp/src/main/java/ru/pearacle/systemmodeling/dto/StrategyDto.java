package ru.pearacle.systemmodeling.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StrategyDto(//@Schema(example = "[[2, 0], [0, 1]]")
                          @Schema(example = "[[6, 9, 9], [8, 5, 8], [3, 6, 8]]")
                          List<List<Integer>> matrix1,
                          //@Schema(example = "[[2, 1], [1, 1]]")
                          @Schema(example = "[[9, 8, 3], [6, 8, 6], [9, 8, 6]]")
                          List<List<Integer>> matrix2) {}
