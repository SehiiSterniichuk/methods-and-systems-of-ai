package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.util.List;

@Builder
public record PostRuleRequest(
        @NotEmpty
        List<RuleDTO> rules
) {
}
