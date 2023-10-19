package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ActionDTO(long id,

                        @NotBlank
                        String name,
                        List<RuleDTO> gotoAction) {
    public List<ActionDTO> toUnmodifiableList(){
        return List.of(this);
    }
}
