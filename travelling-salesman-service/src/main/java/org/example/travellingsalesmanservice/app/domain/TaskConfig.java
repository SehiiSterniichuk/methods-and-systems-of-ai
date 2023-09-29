package org.example.travellingsalesmanservice.app.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.example.travellingsalesmanservice.algorithm.domain.CrossoverType;
import org.example.travellingsalesmanservice.algorithm.domain.SearcherConfig;

@Builder
public record TaskConfig(
        @Min(1)
        int iterationNumber,
        @Min(1)
        int allowedNumberOfGenerationsWithTheSameResult,
        @Min(1)
        int showEachIterationStep,
        @Min(2)
        int populationSize,
        @Min(0)
        @Max(1)
        float mutationProbability,
        @NotNull
        SearcherConfig searcherConfig,
        @NotNull
        CrossoverType crossoverType) {
}
