package com.example.expertsystemservice.domain.decision;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record DecisionRequest(
        @Min(0)
        long ruleId,
        @NotEmpty
        List<Variable> variables
) {
}
