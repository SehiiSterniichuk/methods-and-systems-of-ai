package org.example.travellingsalesmanservice.app.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

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
        float mutationProbability) {
}
