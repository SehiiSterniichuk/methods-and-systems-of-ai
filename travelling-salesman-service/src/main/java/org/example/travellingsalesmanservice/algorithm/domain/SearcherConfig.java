package org.example.travellingsalesmanservice.algorithm.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record SearcherConfig(@NotEmpty
                             Distance distance,
                             @NotEmpty
                             BreedingType breedingType,

                             @Min(10)
                             @Max(90)
                             int diffPercent) {
}
