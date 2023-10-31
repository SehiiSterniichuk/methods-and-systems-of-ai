package com.example.expertsystemservice.service;

import com.example.expertsystemservice.domain.decision.DecisionRequest;
import com.example.expertsystemservice.domain.decision.DecisionResponse;

public interface DecisionService {
    DecisionResponse makeDecision(DecisionRequest request);
}
