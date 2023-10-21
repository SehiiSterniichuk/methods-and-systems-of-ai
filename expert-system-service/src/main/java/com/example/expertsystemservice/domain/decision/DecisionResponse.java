package com.example.expertsystemservice.domain.decision;

import java.util.List;

public record DecisionResponse(
        List<Decision> decisions
) {
}
