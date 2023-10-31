package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.RuleDTO;
import com.example.expertsystemservice.domain.decision.DecisionInfo;
import com.example.expertsystemservice.domain.decision.DecisionRequest;
import com.example.expertsystemservice.domain.decision.Variable;

import java.util.List;

public class DTOConstants {
    private static long id = 0;

    private static long getId() {
        return id++;
    }

    public static final ActionDTO thenAction = ActionDTO.builder()
            .name("new simple then action")
            .build();
    public static final ActionDTO elseAction = ActionDTO.builder()
            .name("simple else action")
            .build();

    public static final RuleDTO binaryRule = RuleDTO.builder()
            .id(getId())
            .decisionInfo(DecisionInfo.BINARY)
            .condition("OK?")
            .name("binary rule")
            .thenAction(thenAction.toUnmodifiableList())
            .elseAction(elseAction.toUnmodifiableList())
            .build();

    public static final ActionDTO thenActionParabola = ActionDTO.builder()
            .name("point is higher than X")
            .formula(STR."\{Variable.OUTPUT_VARIABLE} > 0")
            .build();

    public static final ActionDTO valueActionParabola10 = ActionDTO.builder()
            .name("mult 10")
            .formula(STR."\{Variable.OUTPUT_VARIABLE} * 10")
            .build();
    public static final ActionDTO valueActionParabola20 = ActionDTO.builder()
            .name("mult 20")
            .formula(STR."\{Variable.OUTPUT_VARIABLE} * 20")
            .build();
    public static final ActionDTO valueActionParabola30 = ActionDTO.builder()
            .name("mult 30")
            .formula(STR."\{Variable.OUTPUT_VARIABLE} * 30")
            .build();

    public static final ActionDTO elseActionParabola = ActionDTO.builder()
            .name("point is lower than X or lay on X")
            .build();
    public static final RuleDTO parabolaFormulaRule = RuleDTO.builder()
            .id(getId())
            .condition("""
                    To determine where your point is answer to the next questions:
                    What are coefficients of your parabola?
                    if y = a*(x - h)^2 + k,
                    where:
                    (h, k) = vertex of the parabola
                    x = your point coordinate
                    """)
            .name("parabola point is located above the X")
            .decisionInfo(DecisionInfo.ofFormula("a_v*Math.pow(x_v - h_v, 2) + k_v", "a","h","k","x"))
            .thenAction(thenActionParabola.toUnmodifiableList())
            .elseAction(elseActionParabola.toUnmodifiableList())
            .build();

    public static final RuleDTO parabolaBinaryFormulaRule = RuleDTO.builder()
            .id(getId())
            .condition("""
                    To determine where your point is answer to the next questions:
                    What are coefficients of your parabola?
                    if y = a*(x - h)^2 + k,
                    where:
                    (h, k) = vertex of the parabola
                    x = your point coordinate
                    """)
            .name("parabola point is located above the X")
            .decisionInfo(DecisionInfo.ofBinaryFormula("(a_v*Math.pow(x_v - h_v, 2) + k_v) > 0", "a","h","k","x"))
            .thenAction(thenActionParabola.toUnmodifiableList())
            .elseAction(elseActionParabola.toUnmodifiableList())
            .build();

    public static final DecisionRequest parabolaPositiveRequest = DecisionRequest.builder()
            .variables(List.of(new Variable("a", "1"),
                    new Variable("h", "0"),
                    new Variable("k","0"),
                    new Variable("x", "10")))
            .ruleId(parabolaFormulaRule.id())
            .build();

    public static final DecisionRequest parabolaNegativeRequest = DecisionRequest.builder()
            .variables(List.of(new Variable("a", "1"),
                    new Variable("h", "0"),
                    new Variable("k","0"),
                    new Variable("x", "0")))
            .ruleId(parabolaFormulaRule.id())
            .build();

    public static final RuleDTO parabolaValueFormulaRule = RuleDTO.builder()
            .id(getId())
            .condition("""
                    To determine where your point is answer to the next questions:
                    What are coefficients of your parabola?
                    if y = a*(x - h)^2 + k,
                    where:
                    (h, k) = vertex of the parabola
                    x = your point coordinate
                    """)
            .name("parabola point is located above the X")
            .decisionInfo(DecisionInfo.ofValueFormula("(a_v*Math.pow(x_v - h_v, 2) + k_v)", "a","h","k","x"))
            .thenAction(List.of(valueActionParabola10, valueActionParabola20))
            .elseAction(valueActionParabola30.toUnmodifiableList())
            .build();
}
