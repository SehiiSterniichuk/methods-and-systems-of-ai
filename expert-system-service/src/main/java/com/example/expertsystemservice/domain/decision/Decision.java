package com.example.expertsystemservice.domain.decision;

import com.example.expertsystemservice.domain.ActionDTO;

public record Decision(
        String value,
        DecisionStatus status,
        ActionDTO action) {
}
