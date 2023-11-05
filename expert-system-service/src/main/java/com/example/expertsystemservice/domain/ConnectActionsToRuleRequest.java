package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ConnectActionsToRuleRequest(

        @NotEmpty
        List<Long> actionIds,
        @Min(0)
        long ruleId
) {
}
