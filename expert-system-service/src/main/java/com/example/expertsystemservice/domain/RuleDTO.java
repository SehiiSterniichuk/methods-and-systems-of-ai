package com.example.expertsystemservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
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

    public RuleDTO withId(Long id){
        return new RuleDTO(id, this.name, this.condition, this.thenAction, this.elseAction);
    }
}
