package com.example.expertsystemservice.service.implementation.converter;

import com.example.expertsystemservice.domain.Rule;
import com.example.expertsystemservice.domain.RuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RuleConverter {
    private ActionConverter converter;

    @Autowired
    public void setConverter(ActionConverter converter) {
        this.converter = converter;
        converter.setConverter(this);
    }

    public RuleDTO toDTO(Rule rule) {
        return new RuleDTO(rule.getId(), rule.getName(), rule.getCondition(),
                converter.toUnmodifiableListDTO(rule.getThenAction()),
                converter.toUnmodifiableListDTO(rule.getElseAction()));
    }

    public List<RuleDTO> toUnmodifiableListDTO(List<Rule> action) {
        return action.stream().map(this::toDTO).toList();
    }

    public List<Rule> toUnmodifiableListEntity(List<RuleDTO> action) {
        return action.stream().map(this::toEntity).toList();
    }

    public Rule toEntity(RuleDTO ruleDTO) {
        return new Rule(ruleDTO.id(), ruleDTO.name(), ruleDTO.condition(),
                converter.toUnmodifiableListEntity(ruleDTO.thenAction()),
                converter.toUnmodifiableListEntity(ruleDTO.elseAction()));
    }
}

