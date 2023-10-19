package com.example.expertsystemservice.domain;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record ActionDTO(long id,

                        String name,
                        List<RuleDTO> gotoAction) {
    public List<ActionDTO> toUnmodifiableList(){
        return List.of(this);
    }
}
