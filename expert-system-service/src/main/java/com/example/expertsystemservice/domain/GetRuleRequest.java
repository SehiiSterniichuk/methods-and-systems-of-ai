package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GetRuleRequest(

        @Min(1)
        long id,


        @Min(2)
        @Max(100)
        Long depth
) {
}
