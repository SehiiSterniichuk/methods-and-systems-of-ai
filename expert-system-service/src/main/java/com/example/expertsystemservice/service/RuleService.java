package com.example.expertsystemservice.service;

import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.GetRuleRequest;
import com.example.expertsystemservice.domain.PostRuleRequest;
import com.example.expertsystemservice.domain.RuleDTO;

import java.util.List;

public interface RuleService {
    RuleDTO getRule(GetRuleRequest request);

    List<Long> createNewRule(PostRuleRequest request);

    long delete(long id);

    long deleteAll(long id);

    long deleteAll();

    RuleDTO updateFormula(long id, String newFormula);

    ActionDTO updateActionFormula(long id, String newFormula);
}
