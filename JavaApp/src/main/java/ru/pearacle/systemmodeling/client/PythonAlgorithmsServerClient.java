package ru.pearacle.systemmodeling.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.pearacle.systemmodeling.dto.NashResponseDto;
import ru.pearacle.systemmodeling.dto.StrategyDto;
import ru.pearacle.systemmodeling.dto.NloResponseDto;

@FeignClient(name = "matrixService", url = "http://localhost:8000")
public interface PythonAlgorithmsServerClient {

    @PostMapping("/nash-clear")
    NashResponseDto findNashClearStrategy(@RequestBody StrategyDto strategyDto);

    @PostMapping("/nlo")
    NloResponseDto deleteNloStrategy(@RequestBody StrategyDto strategyDto);
}
