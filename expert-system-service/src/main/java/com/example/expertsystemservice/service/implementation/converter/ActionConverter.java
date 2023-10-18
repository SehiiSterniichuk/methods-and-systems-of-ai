package com.example.expertsystemservice.service.implementation.converter;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.ActionDTO;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
public class ActionConverter {
    private RuleConverter converter;

    public ActionDTO toDTO(Action action) {
        return new ActionDTO(action.getId(), action.getName(),
                converter.toUnmodifiableListDTO(action.getGotoAction()));
    }

    public List<ActionDTO> toUnmodifiableListDTO(List<Action> action){
        return action.stream().map(this::toDTO).toList();
    }

    public List<Action> toUnmodifiableListEntity(List<ActionDTO> action){
        return action.stream().map(this::toEntity).toList();
    }

    public Action toEntity(ActionDTO actionDTO) {
        return new Action(actionDTO.id(), actionDTO.name(),
                converter.toUnmodifiableListEntity(actionDTO.gotoAction()));
    }
}

