package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record GetRuleRequest(

        @Min(0)
        long id,


        @Min(2)
        @Max(100)
        long depth
) {
}
