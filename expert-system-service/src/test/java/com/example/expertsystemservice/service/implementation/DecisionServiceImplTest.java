package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.decision.DecisionRequest;
import com.example.expertsystemservice.domain.decision.DecisionResponse;
import com.example.expertsystemservice.domain.decision.DecisionStatus;
import com.example.expertsystemservice.domain.decision.Variable;
import com.example.expertsystemservice.service.DecisionService;
import com.example.expertsystemservice.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.example.expertsystemservice.service.implementation.DTOConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class DecisionServiceImplTest {
    @Mock
    private RuleService ruleService;

    private DecisionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DecisionServiceImpl(ruleService, new JSExpressionCalculator());
    }

    @Test
    void makeDecisionBinarySimpleTest() {
        doReturn(DTOConstants.binaryRule).when(ruleService).getRule(any());
        DecisionResponse response = service.makeDecision(DecisionRequest.builder()
                .ruleId(DTOConstants.binaryRule.id())
                        .variables(Variable.outputListFalse)
                .build());
        assertEquals(DTOConstants.elseAction, response.decisions().getFirst().action());
        assertEquals(DecisionStatus.SUCCESS, response.decisions().getFirst().status());
    }

    @Test
    void makeDecisionFormulaPositiveTest() {
        doReturn(DTOConstants.parabolaFormulaRule).when(ruleService).getRule(any());
        DecisionResponse response = service.makeDecision(DTOConstants.parabolaPositiveRequest);
        assertEquals(DTOConstants.thenActionParabola, response.decisions().getFirst().action());
        assertEquals(DecisionStatus.SUCCESS, response.decisions().getFirst().status());
    }
    @Test
    void makeDecisionFormulaNegativeTest() {
        doReturn(DTOConstants.parabolaFormulaRule).when(ruleService).getRule(any());
        DecisionResponse response = service.makeDecision(DTOConstants.parabolaNegativeRequest);
        assertEquals(DTOConstants.elseActionParabola, response.decisions().getFirst().action());
        assertEquals(DecisionStatus.SUCCESS, response.decisions().getFirst().status());
    }

    @Test
    void makeDecisionBinaryFormulaPositiveTest() {
        doReturn(DTOConstants.parabolaBinaryFormulaRule).when(ruleService).getRule(any());
        DecisionResponse response = service.makeDecision(DTOConstants.parabolaPositiveRequest);
        assertEquals(DTOConstants.thenActionParabola, response.decisions().getFirst().action());
        assertEquals(DecisionStatus.SUCCESS, response.decisions().getFirst().status());
    }
    @Test
    void makeDecisionBinaryFormulaNegativeTest() {
        doReturn(DTOConstants.parabolaBinaryFormulaRule).when(ruleService).getRule(any());
        DecisionResponse response = service.makeDecision(DTOConstants.parabolaNegativeRequest);
        assertEquals(DTOConstants.elseActionParabola, response.decisions().getFirst().action());
        assertEquals(DecisionStatus.SUCCESS, response.decisions().getFirst().status());
    }
    @Test
    void makeDecisionValueFormulaPositiveTest() {
        doReturn(DTOConstants.parabolaValueFormulaRule).when(ruleService).getRule(any());
        DecisionResponse response = service.makeDecision(DTOConstants.parabolaPositiveRequest);
        assertEquals(response.decisions().size(), 3);
        int counter = 0;
        for(var d : response.decisions()){
            if(valueActionParabola10.name().equals(d.action().name())){
                counter++;
                assertEquals("1000", d.value());
            } else if(valueActionParabola20.name().equals(d.action().name())){
                counter++;
                assertEquals("2000", d.value());
            } else if(valueActionParabola30.name().equals(d.action().name())){
                counter++;
                assertEquals("3000", d.value());
            }
        }
        assertEquals( 3, counter);
    }
}