package ru.pearacle.systemmodeling.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record StrategyDominatedDto(@Schema(example = "[[1, 2, 3], [4, 5, 6], [7, 8, 9]]")
                                   List<List<Integer>> matrix,
                                   @Schema(example = "true")
                                   Boolean checkRows,
                                   @Schema(example = "true")
                                   Boolean strict) {

}
