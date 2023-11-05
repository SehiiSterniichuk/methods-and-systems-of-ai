package com.example.expertsystemservice.service;

import com.example.expertsystemservice.domain.*;

import java.util.List;

public interface RuleService {
    RuleDTO getRule(GetRuleRequest request);

    List<Long> createNewRule(PostRuleRequest request);

    long delete(long id);

    List<Long> deleteAll(long id, long depth);

    long deleteAll();

    RuleDTO updateFormula(long id, String newFormula);

    ActionDTO updateActionFormula(long id, String newFormula);

    List<Long> connectActionsToRule(ConnectActionsToRuleRequest request);
}
