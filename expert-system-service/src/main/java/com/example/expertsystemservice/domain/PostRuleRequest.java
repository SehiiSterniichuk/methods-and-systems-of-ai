package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PostRuleRequest(
        @NotEmpty
        List<RuleDTO> rules
) {
}
