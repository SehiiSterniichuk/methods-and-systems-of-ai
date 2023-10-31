package com.example.expertsystemservice.domain;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ActionDTO(long id,

                        String name,
                        String formula,
                        List<RuleDTO> gotoAction) {

    public boolean isFormulaNullOrBlank() {
        return formula == null || formula.isBlank();
    }

    public List<ActionDTO> toUnmodifiableList() {
        return List.of(this);
    }
}
