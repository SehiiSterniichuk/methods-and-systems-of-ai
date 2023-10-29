package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.GetRuleRequest;
import com.example.expertsystemservice.domain.RuleDTO;
import com.example.expertsystemservice.domain.RuleType;
import com.example.expertsystemservice.domain.decision.*;
import com.example.expertsystemservice.service.DecisionService;
import com.example.expertsystemservice.service.ExpressionCalculator;
import com.example.expertsystemservice.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.expertsystemservice.domain.decision.Variable.OUTPUT_VARIABLE;
import static com.example.expertsystemservice.domain.decision.Variable.VARIABLE_POSTFIX;

@Service
@RequiredArgsConstructor
public class DecisionServiceImpl implements DecisionService {
    private final RuleService ruleService;
    private final ExpressionCalculator calculator;

    @Override
    public DecisionResponse makeDecision(DecisionRequest request) {
        RuleDTO rule = ruleService.getRule(GetRuleRequest.builder()
                .id(request.ruleId())
                .depth(2)
                .build());
        RuleType type = rule.decisionInfo().type();
        return switch (type) {
            case BINARY -> processBinary(rule, request);
            case FORMULA -> processFormula(rule, request);
            case BINARY_FORMULA -> processBinaryFormula(rule, request);
            case VALUE_FORMULA -> processValueFormula(rule, request);
        };
    }

    private DecisionResponse processValueFormula(RuleDTO rule, DecisionRequest request) {
        String output = getOutput(rule, request);
        List<Decision> decisions = calculateAllActions(rule, output);
        return new DecisionResponse(decisions);
    }

    private String getOutput(RuleDTO rule, DecisionRequest request) {
        String formula = rule.decisionInfo().formula();
        String output;
        if (formula == null || formula.isBlank()) {
            output = request.variables().get(0).value();
        } else {
            String expression = getExpression(request, formula);
            output = calculator.evaluate(expression);
        }
        return output;
    }

    private List<Decision> calculateAllActions(RuleDTO rule, String output) {
        return Stream.concat(rule.thenAction().stream(), rule.elseAction().stream())
                .map(a -> {
                    if (a.isFormulaNullOrBlank()) {
                        return new Decision("empty formula", DecisionStatus.FAILED, a);
                    }
                    String expression = a.formula().replaceAll(OUTPUT_VARIABLE, output);
                    String actionValue = calculator.evaluate(expression);
                    return new Decision(actionValue, DecisionStatus.SUCCESS, a);
                }).toList();
    }

    private DecisionResponse processBinaryFormula(RuleDTO rule, DecisionRequest request) {
        String formula = rule.decisionInfo().formula();
        boolean output;
        if (formula == null || formula.isBlank()) {
            return new DecisionResponse(List.of(new Decision("has no formula", DecisionStatus.FAILED, null)));
        } else {
            String expression = getExpression(request, formula);
            output = calculator.evaluateToBoolean(expression);
        }
        List<ActionDTO> actionDTO = output ? rule.thenAction() : rule.elseAction();
        List<Decision> decisions = actionDTO.stream()
                .map(a -> new Decision(null, DecisionStatus.SUCCESS, a)).toList();
        return new DecisionResponse(decisions);
    }

    private static String getExpression(DecisionRequest request, String formula) {
        String[] variables = request.variables().stream().map(Variable::name).map(x -> x + VARIABLE_POSTFIX).toArray(String[]::new);
        String[] values = request.variables().stream().map(Variable::value).toArray(String[]::new);
        return StringUtils.replaceEach(formula, variables, values);
    }

    private DecisionResponse processFormula(RuleDTO rule, DecisionRequest request) {
        String output = getOutput(rule, request);
        List<Decision> decisions = calculateActions(rule.thenAction(), output);
        decisions = getDecisions(rule, decisions, output);
        return new DecisionResponse(decisions);
    }

    private List<Decision> getDecisions(RuleDTO rule, List<Decision> decisions, String output) {
        if (hasNoSuccess(decisions)) {
            if (rule.elseAction().size() == 1 && rule.elseAction().getFirst().isFormulaNullOrBlank()) {
                decisions = List.of(new Decision("else", DecisionStatus.SUCCESS, rule.elseAction().getFirst()));
                return decisions;
            }
            List<Decision> elseDecisions = calculateActions(rule.elseAction(), output);
            if (hasNoSuccess(elseDecisions)) {
                decisions = Stream.concat(decisions.stream(), elseDecisions.stream())
                        .collect(Collectors.toList());
            } else if (!elseDecisions.isEmpty()) {
                decisions = elseDecisions;
            }
        }
        return decisions;
    }

    private static boolean hasNoSuccess(List<Decision> decisions) {
        return decisions.stream().map(Decision::status).noneMatch(s -> s == DecisionStatus.SUCCESS);
    }

    private List<Decision> calculateActions(List<ActionDTO> actions, String output) {
        return actions.stream().map(a -> {
            if (a.isFormulaNullOrBlank()) {
                return new Decision("has no formula", DecisionStatus.FAILED, a);
            } else {
                boolean result = calculator.evaluateToBoolean(a.formula().replaceAll(OUTPUT_VARIABLE, output));
                DecisionStatus status = !result ? DecisionStatus.FAILED : DecisionStatus.SUCCESS;
                return new Decision(result + "", status, a);
            }
        }).toList();
    }

    private DecisionResponse processBinary(RuleDTO rule, DecisionRequest request) {
        var value = request.variables().get(0).value();
        List<ActionDTO> actionDTO;
        if (value.matches("(?i).*(?:1|\\+|ok|yes|true|так|yeah|sure).*")) {
            actionDTO = rule.thenAction();
        } else {
            actionDTO = rule.elseAction();
        }
        List<Decision> decisions = actionDTO.stream()
                .map(a -> new Decision(null, DecisionStatus.SUCCESS, a)).toList();
        return new DecisionResponse(decisions);
    }
}
