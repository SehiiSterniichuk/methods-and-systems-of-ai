package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.*;
import com.example.expertsystemservice.repository.ActionRepository;
import com.example.expertsystemservice.repository.RuleRepository;
import com.example.expertsystemservice.service.RuleService;
import com.example.expertsystemservice.service.implementation.converter.ActionConverter;
import com.example.expertsystemservice.service.implementation.converter.RuleConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class GraphRuleService implements RuleService {
    private final RuleRepository repository;
    private final ActionRepository actionRepository;
    private final RuleDTOValidator validator;
    private final RuleConverter converter;
    private final ActionConverter actionConverter;

    //depth 2 is equal to root node + all goto rules
    // leaf rules has condition, name, id and DecisionInfo properties, but Actions are missed
    @Override
    public RuleDTO getRule(@Valid GetRuleRequest request) {
        Rule rule = repository.findRuleById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Not found rule with id: " + request.id()));
        return ruleToDTO(rule, request.depth());
    }

    private RuleDTO ruleToDTO(Rule rule, Long depth) {
        if (depth <= 1) {
            return converter.toLeafDTO(rule);
        }
        --depth;
        List<ActionDTO> elseActions = actionToDTO(rule.getElseAction(), depth);
        List<ActionDTO> thenActions = actionToDTO(rule.getThenAction(), depth);
        return converter.toDTO(rule, thenActions, elseActions);
    }

    private List<ActionDTO> actionToDTO(List<Action> actions, Long depth) {
        return actions.stream()
                .map(action -> actionToDTO(depth, action))
                .toList();
    }

    private ActionDTO actionToDTO(Long depth, Action action) {
        return actionConverter.toDTO(action, action.getGotoAction()
                .stream()
                .map(r -> this.ruleToDTO(r, depth))
                .toList());
    }

    @Override
    public List<Long> createNewRule(PostRuleRequest request) {
        List<RuleDTO> rules = request.rules();
        int start = 0;
        rules.stream().parallel().forEach(r -> {
            String message = validator.isValid(r);
            if (!message.isBlank()) {
                throw new IllegalArgumentException(message + r);
            }
        });
        List<Rule> newRulesToSave = new ArrayList<>(rules.size());
        Map<String, Rule> processedNewRules = new HashMap<>(rules.size());
        for (int i = start; i < rules.size(); i++) {
            var rule = rules.get(i);
            if (processedNewRules.get(rule.name()) != null) {
                continue;
            }
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
        if (rule.getId() != null) {
            return;
        }
        if (repository.existsRuleByName(rule.getName())) {
            return;
        }
        var saved = repository.save(rule);
        ids.add(saved.getId());
    }

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
        return converter.toEntity(rule, thenEntity, elseEntity);
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
                    return actionConverter.toEntity(a, gotoRuleEntity);
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
        long found = repository.findRuleById(id).map(Rule::getId).orElse(-1L);
        if (found >= 0) {
            repository.findRuleById(id);
            repository.deleteById(id);
        }
        return found;
    }

    @Override
    public List<Long> deleteAll(long id, long depth) {
        if (depth < 1) {
            return List.of();
        }
        Rule ruleById = findRuleById(id);
        if (ruleById == null) {
            return List.of();
        }
        Set<Long> ids = new HashSet<>();
        deleteRule(ruleById, depth, ids);
        return ids.stream().toList();
    }

    private void deleteRule(Rule rule, long depth, Set<Long> ids) {
        if(depth <= 0 || ids.contains(rule.getId())){
            return;
        }
        depth--;
        ids.add(rule.getId());
        if(rule.getThenAction() != null){
            deleteActions(rule.getThenAction(), depth, ids);
        }
        if(rule.getElseAction() != null){
            deleteActions(rule.getElseAction(), depth, ids);
        }
        repository.delete(rule);
    }

    private void deleteActions(List<Action> thenAction, long depth, Set<Long> ids) {
        for (var action: thenAction) {
            if(action.getGotoAction() != null){
                action.getGotoAction().forEach(x->deleteRule(x, depth, ids));
            }
            actionRepository.delete(action);
        }
    }

    @Override
    public long deleteAll() {
        long count = repository.count();
        repository.deleteAll();
        actionRepository.deleteAll();
        return count;
    }

    @Override
    public RuleDTO updateFormula(long id, String newFormula) {
        Optional<Rule> ruleById = repository.findRuleById(id);
        if (ruleById.isPresent()) {
            ruleById.get().setFormula(newFormula);
            repository.save(ruleById.get());
            return converter.toLeafDTO(ruleById.get());
        }
        return null;
    }

    @Override
    public ActionDTO updateActionFormula(long id, String newFormula) {
        var action = actionRepository.findById(id);
        if (action.isPresent()) {
            action.get().setFormula(newFormula);
            actionRepository.save(action.get());
            return actionConverter.toDTO(action.get(), List.of());
        }
        return null;
    }
}
