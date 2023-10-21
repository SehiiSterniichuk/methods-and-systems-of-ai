package com.example.expertsystemservice.service.implementation.converter;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.Rule;
import com.example.expertsystemservice.domain.RuleDTO;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Setter
public class ActionConverter {
    private RuleConverter converter;

    public ActionDTO toDTO(Action action) {
        return new ActionDTO(action.getId(), action.getName(), action.getFormula(),
                converter.toUnmodifiableListDTO(action.getGotoAction()));
    }

    public List<ActionDTO> toUnmodifiableListDTO(List<Action> action){
        if(action == null) return List.of();
        return action.stream().map(this::toDTO).toList();
    }

    public List<Action> toUnmodifiableListEntity(List<ActionDTO> action){
        if(action == null) return List.of();
        return action.stream().map(this::toEntity).toList();
    }

    public Action toEntity(ActionDTO actionDTO) {
        return new Action(actionDTO.id(), actionDTO.name(), actionDTO.formula(),
                converter.toUnmodifiableListEntity(actionDTO.gotoAction()));
    }

    public Action toEntity(ActionDTO a, List<Rule> gotoRuleEntity) {
        return Action.builder()
                .formula(a.formula())
                .name(a.name())
                .gotoAction(gotoRuleEntity)
                .build();
    }

    public ActionDTO toDTO(Action action, List<RuleDTO> list) {
        return new ActionDTO(action.getId(), action.getName(), action.getFormula(), list);
    }
}

