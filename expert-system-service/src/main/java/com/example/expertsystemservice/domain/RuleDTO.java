package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record RuleDTO(long id,
                      @NotBlank
                      String name,
                      @NotBlank
                      String condition,
                      List<ActionDTO> thenAction,
                      List<ActionDTO> elseAction) {
}
