package com.example.expertsystemservice.service.implementation.converter;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.Rule;
import com.example.expertsystemservice.domain.RuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleConverter {
    private ActionConverter converter;

    @Autowired
    public void setConverter(ActionConverter converter) {
        this.converter = converter;
        converter.setConverter(this);
    }

    public RuleDTO toDTO(Rule rule) {
        return new RuleDTO(rule.getId(), rule.getName(), rule.getCondition(),
                rule.getDecisionInfo(),
                converter.toUnmodifiableListDTO(rule.getThenAction()),
                converter.toUnmodifiableListDTO(rule.getElseAction()));
    }

    public Rule toEntity(RuleDTO rule, List<Action> thenEntity, List<Action> elseEntity) {
        return new Rule(rule.id(), rule.name(), rule.condition(), rule.decisionInfo(), thenEntity, elseEntity);
    }

    public RuleDTO toLeafDTO(Rule rule) {
        return new RuleDTO(rule.getId(), rule.getName(), rule.getCondition(),
                rule.getDecisionInfo(),
                null,
                null);
    }

    public List<RuleDTO> toUnmodifiableListDTO(List<Rule> action) {
        return action.stream().map(this::toDTO).toList();
    }

    public List<Rule> toUnmodifiableListEntity(List<RuleDTO> action) {
        return action.stream().map(this::toEntity).toList();
    }

    public Rule toEntity(RuleDTO ruleDTO) {
        return new Rule(ruleDTO.id(), ruleDTO.name(), ruleDTO.condition(),
                ruleDTO.decisionInfo(),
                converter.toUnmodifiableListEntity(ruleDTO.thenAction()),
                converter.toUnmodifiableListEntity(ruleDTO.elseAction()));
    }

    public RuleDTO toDTO(Rule rule, List<ActionDTO> thenActions, List<ActionDTO> elseActions) {
        return new RuleDTO(rule.getId(), rule.getName(), rule.getCondition(),
                rule.getDecisionInfo(), thenActions, elseActions);
    }
}

