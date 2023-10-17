package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ActionDTO(long id,

                        @NotBlank
                        String name,
                        List<RuleDTO> gotoAction) {
}
