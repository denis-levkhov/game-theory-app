package ru.pearacle.systemmodeling.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pearacle.systemmodeling.dto.NashResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDto;
import ru.pearacle.systemmodeling.dto.NloResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedDto;
import ru.pearacle.systemmodeling.dto.StrategyDominatedResultDto;
import ru.pearacle.systemmodeling.service.AlgorithmsService;
import ru.pearacle.systemmodeling.util.PettyStringUtil;

@Slf4j
@RestController
@RequestMapping("/strategies")
@RequiredArgsConstructor
@Tag(name = "Modeling-System API", description = "Endpoints for operations with Modeling-System-Util")
public class AlgorithmsController {

    private final AlgorithmsService algorithmsService;
    private final PettyStringUtil pettyStringUtil;

    @PostMapping("/dominated/delete")
    @Operation(summary = "Delete strict/weakly dominated strategies in game")
    public ResponseEntity<StrategyDominatedResultDto> deleteDominatedStrategies(@RequestBody
                                                                                StrategyDominatedDto strategyDto) {
        log.info("Request to delete dominated strategies: {}", strategyDto);
        return ResponseEntity.ok(algorithmsService.deleteDominatedStrategies(strategyDto));
    }

    @PostMapping("/nash-clean/find")
    @Operation(summary = "Find Nash Clean strategy in game")
    public ResponseEntity<NashResponseDto> findNashCleanStrategy(@RequestBody
                                                                 StrategyDto strategyDto) {
        log.info("Request to find Nash Clean strategy: {}", strategyDto);
        return ResponseEntity.ok(algorithmsService.findNashCleanStrategy(strategyDto));
    }

    @PostMapping("/nlo/delete")
    @Operation(summary = "Delete NLO strategy in game")
    public ResponseEntity<NloResponseDto> deleteNloStrategy(@RequestBody
                                                            StrategyDto strategyDto) {
        log.info("Request to delete NLO strategy: {}", strategyDto);
        return ResponseEntity.ok(algorithmsService.deleteNloStrategy(strategyDto));
    }

    @PostMapping("/dominated/delete/string")
    @Operation(summary = "Delete strict/weakly dominated strategies in game")
    public ResponseEntity<String> deleteDominatedStrategiesPettyString(@RequestBody
                                                                       StrategyDominatedDto strategyDto) {
        log.info("Request to delete dominated strategies with petty string exit: {}", strategyDto);
        return ResponseEntity.ok(pettyStringUtil.toPrettyString(algorithmsService.deleteDominatedStrategies(strategyDto), strategyDto));
    }

    @PostMapping("/nash-clean/find/string")
    @Operation(summary = "Find Nash Clean strategy in game")
    public ResponseEntity<String> findNashCleanStrategyPettyString(@RequestBody
                                                                   StrategyDto strategyDto) {
        log.info("Request to find Nash Clean strategy with petty string exit: {}", strategyDto);
        return ResponseEntity.ok(pettyStringUtil.toPrettyString(algorithmsService.findNashCleanStrategy(strategyDto)));
    }

    @PostMapping("/nlo/delete/string")
    @Operation(summary = "Delete NLO strategy in game")
    public ResponseEntity<String> deleteNloStrategyPettyString(@RequestBody
                                                               StrategyDto strategyDto) {
        log.info("Request to delete NLO strategy with petty string exit: {}", strategyDto);
        return ResponseEntity.ok(pettyStringUtil.toPrettyString(algorithmsService.deleteNloStrategy(strategyDto), strategyDto));
    }
}
