package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record RuleDTO(Long id,
                      @NotBlank
                      String name,
                      @NotBlank
                      String condition,
                      @NotNull
                      List<ActionDTO> thenAction,
                      List<ActionDTO> elseAction) {

    public List<RuleDTO> toUnmodifiableList(){
        return List.of(this);
    }
}
