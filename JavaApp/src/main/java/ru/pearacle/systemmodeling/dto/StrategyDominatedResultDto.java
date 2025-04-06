package ru.pearacle.systemmodeling.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDominatedResultDto {
    private String dominationType;
    private String target;
    private List<DominatedStrategy> dominatedStrategies;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DominatedStrategy {
        private int index;
        private List<Integer> values;
    }
}
