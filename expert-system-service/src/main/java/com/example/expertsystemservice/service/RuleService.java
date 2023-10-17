package com.example.expertsystemservice.service;

import com.example.expertsystemservice.domain.GetRuleRequest;
import com.example.expertsystemservice.domain.PostRuleRequest;
import com.example.expertsystemservice.domain.RuleDTO;

public interface RuleService {
    RuleDTO getRule(GetRuleRequest request);

    long createNewRule(PostRuleRequest request);

    long delete(long id);

    long deleteAll(long id);
}
