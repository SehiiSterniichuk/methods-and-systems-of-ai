package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.*;
import com.example.expertsystemservice.repository.RuleRepository;
import com.example.expertsystemservice.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GraphRuleService implements RuleService {
    private final RuleRepository repository;

    @Override
    public RuleDTO getRule(GetRuleRequest request) {
        return null;
    }

    @Override
    public List<Long> createNewRule(PostRuleRequest request) {
        List<RuleDTO> rules = request.rules();
        int start = 0;
        List<Rule> newRulesToSave = new ArrayList<>(rules.size());
        Map<String, Rule> processedNewRules = new HashMap<>(rules.size());
        for (int i = start; i < rules.size(); i++) {
            var rule = rules.get(i);
            Rule entity = toEntity(rule, rules, processedNewRules);
            newRulesToSave.add(entity);
        }
        return saveAllTreeRules(newRulesToSave);
    }

    private List<Long> saveAllTreeRules(List<Rule> newRulesToSave) {
        List<Long> ids = new ArrayList<>(newRulesToSave.size());
        newRulesToSave.forEach(rule -> saveRule(rule, ids));
        return ids;
    }

    private void saveRule(Rule rule, List<Long> ids) {
        if (repository.findRuleByIdOrName(rule.getId(), rule.getName()).isPresent()) {
            return;
        }
        var saved = repository.save(rule);
        ids.add(saved.getId());
//                    connectRuleToActions(ids, saved, ActionRelationship.THEN);
//                    connectRuleToActions(ids, saved, ActionRelationship.ELSE);
    }

//    private void connectRuleToActions(List<Long> ids, Rule rule, ActionRelationship relationship) {
//        var actions = switch (relationship) {
//            case THEN -> rule.getThenAction();
//            case ELSE -> rule.getElseAction();
//        };
//        for (var action : actions) {
//            Action savedAction = saveAction(action, ids);
//            switch (relationship){
//                case THEN -> repository.connectThenById(rule, savedAction);
//                case ELSE -> repository.connectElseById(rule, savedAction);
//            }
//        }
//    }

//    private Action saveAction(Action action, List<Long> ids) {
//        return actionRepository.findActionByIdOrName(action.getId(), action.getName())
//                .orElseGet(() -> {
//                    Action saved = actionRepository.save(action);
//                    for (var gotoRule : action.getGotoAction()) {
//                        Rule savedRule = saveRule(gotoRule, ids);
//                        repository.connectGoto(action, savedRule, GotoRelationship.GOTO);
//                    }
//                    return saved;
//                });
//    }


    private Rule toEntity(RuleDTO rule, List<RuleDTO> rules, Map<String, Rule> processedNewRules) {
        List<ActionDTO> thenAction = rule.thenAction();
        List<ActionDTO> elseAction = rule.elseAction();
        List<Action> thenEntity = List.of();
        List<Action> elseEntity = List.of();
        if (thenAction != null) {
            thenEntity = toActionEntity(rules, processedNewRules, thenAction);
        }
        if (elseAction != null) {
            elseEntity = toActionEntity(rules, processedNewRules, elseAction);
        }
        return Rule.builder()
                .name(rule.name())
                .condition(rule.condition())
                .thenAction(thenEntity)
                .elseAction(elseEntity)
                .build();
    }

    private List<Action> toActionEntity(List<RuleDTO> rules, Map<String, Rule> processedNewRules, List<ActionDTO> then) {
        return then.stream()
                .map(a -> {
                    List<RuleDTO> gotoAction = a.gotoAction();
                    List<Rule> gotoRuleEntity = List.of();
                    if (gotoAction != null && !gotoAction.isEmpty()) {
                        gotoRuleEntity = new ArrayList<>(gotoAction.size());
                        for (RuleDTO actionRule : gotoAction) {
                            Rule subRule = processedNewRules.get(actionRule.name());
                            if (subRule != null) {
                                gotoRuleEntity.add(subRule);
                                continue;
                            }
                            if (actionRule.id() == null) {
                                var subRuleDTO = getSubRule(rules, actionRule);
                                subRule = toEntity(subRuleDTO, rules, processedNewRules);
                            } else {
                                subRule = findRuleById(actionRule.id());
                            }
                            processedNewRules.put(subRule.getName(), subRule);
                            gotoRuleEntity.add(subRule);
                        }
                    }
                    return Action.builder().name(a.name())
                            .gotoAction(gotoRuleEntity).build();
                }).toList();
    }

    private Rule findRuleById(long id) {
        return repository
                .findRuleById(id)
                .orElseThrow(() -> new IllegalArgumentException(STR. "Not found rule with id: \{ id }" ));
    }

    private static RuleDTO getSubRule(List<RuleDTO> rules, RuleDTO actionRule) {
        return rules.stream().parallel().filter(r -> r.name().equals(actionRule.name()))
                .findAny()
                .orElseThrow(() -> {
                    String m = STR. """
                            Not found rule with name: \{ actionRule.name() }.
                            Provide a new rule or ID for old one
                            """ ;
                    return new IllegalArgumentException(m);
                });
    }

    @Override
    public long delete(long id) {
        return 0;
    }

    @Override
    public long deleteAll(long id) {
        return 0;
    }

    @Override
    public List<Long> deleteAll() {
        return null;
    }
}
