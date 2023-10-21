package com.example.expertsystemservice.domain.decision;

import com.example.expertsystemservice.domain.RuleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record DecisionInfo(
        @NotNull
        RuleType type,

        String formula,

        List<String> variables) {

    public static DecisionInfo of(RuleType type, String formula, String... variables) {
        return new DecisionInfo(type, formula, List.of(variables));
    }

    public static DecisionInfo ofFormula(String formula, String... variables) {
        return new DecisionInfo(RuleType.FORMULA, formula, List.of(variables));
    }

    public static DecisionInfo ofBinaryFormula(String formula, String... variables) {
        return new DecisionInfo(RuleType.BINARY_FORMULA, formula, List.of(variables));
    }

    public static DecisionInfo ofValueFormula(String formula, String... variables) {
        return new DecisionInfo(RuleType.VALUE_FORMULA, formula, List.of(variables));
    }

    public static final DecisionInfo BINARY = new DecisionInfo(RuleType.BINARY, null, null);
}
