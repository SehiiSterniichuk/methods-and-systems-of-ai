package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.domain.ActionDTO;
import com.example.expertsystemservice.domain.PostRuleRequest;
import com.example.expertsystemservice.domain.RuleDTO;
import com.example.expertsystemservice.repository.ActionRepository;
import com.example.expertsystemservice.repository.RuleRepository;
import com.example.expertsystemservice.service.RuleService;
import com.example.expertsystemservice.service.implementation.converter.RuleConverter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class GraphRuleServiceTest {
    private RuleService service;

    @Autowired
    private RuleRepository repository;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private RuleConverter converter;

    private final RuleDTO simpleRule = RuleDTO.builder()
            .condition("Unexpected condition")
            .name("Without branched actions :(")
            .thenAction(List.of())
            .elseAction(List.of())
            .build();

    private final RuleDTO simpleRulePointer = RuleDTO.builder()
            .condition("Unexpected condition")
            .name("Without branched actions :(")
            .thenAction(List.of())
            .elseAction(List.of())
            .build();

    private final ActionDTO thenAction = ActionDTO.builder()
            .name("new simple then action")
            .build();

    private final ActionDTO gotoAction = ActionDTO.builder()
            .name("new simple then action")
            .gotoAction(simpleRulePointer.toUnmodifiableList())
            .build();

    private final RuleDTO branchRule = RuleDTO.builder()
            .condition("Unexpected 2")
            .name("With new branches!")
            .thenAction(thenAction.toUnmodifiableList())
            .elseAction(List.of())
            .build();

    private final RuleDTO branchRuleWithGoTo = RuleDTO.builder()
            .condition("condition with goto")
            .name("rule with goto")
            .thenAction(thenAction.toUnmodifiableList())
            .elseAction(gotoAction.toUnmodifiableList())
            .build();
    RuleDTO secondComplexRule = RuleDTO.builder()
            .name("second")
            .thenAction(thenAction.toUnmodifiableList())
            .condition("second condition")
            .build();
    private final List<RuleDTO> allRules = List.of(simpleRule,
            branchRule,
            secondComplexRule,
            branchRuleWithGoTo,
            simpleRulePointer);
    private final List<ActionDTO> allActions = List.of(gotoAction, thenAction);

    @BeforeEach
    void setUp() {
        service = new GraphRuleService(repository);
    }

    @AfterEach
    void tearDown() {
        allRules.forEach(r -> repository.deleteByName(r.name()));
        allActions.forEach(a -> actionRepository.deleteActionByName(a.name()));
    }

    @Test
    void createNewRuleSimple() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        var id = service.createNewRule(PostRuleRequest.builder()
                .rules(simpleRule.toUnmodifiableList())
                .build());
        log.info(STR. "id: \{ id }" );
        var saved = repository.findRuleById(id.get(0)).orElse(null);
        assertNotNull(saved);
        assertEquals(converter.toDTO(saved), simpleRule.withId(saved.getId()));
    }

    @Test
    void createNewRuleWithNewActions() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        var id = service.createNewRule(PostRuleRequest.builder()
                .rules(branchRule.toUnmodifiableList())
                .build());
        log.info(STR. "id: \{ id }" );
        var saved = repository.findRuleById(id.get(0)).orElse(null);
        assertNotNull(saved);
        assertEquals(saved.getThenAction().size(), 1);
        assertEquals(branchRule.name(), saved.getName());
        assertEquals(branchRule.thenAction().get(0).name(), saved.getThenAction().get(0).getName());
    }

    @Test
    void createNewRuleWithNewGoToAction() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        createRules(List.of(branchRuleWithGoTo, simpleRule));
    }

    @Test
    void createNewRuleWithOldGoToAction() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        List<Long> old = service.createNewRule(PostRuleRequest.builder()
                .rules(List.of(simpleRule))
                .build());
        assertEquals(old.size(), 1);
        var simpleRuleWithId = gotoAction.toBuilder().gotoAction(simpleRule.withId(old.get(0)).toUnmodifiableList()).build();
        RuleDTO branchRuleWithOldGoTo = branchRuleWithGoTo.toBuilder()
                .elseAction(simpleRuleWithId.toUnmodifiableList())
                .build();
        createRules(List.of(branchRuleWithOldGoTo));
    }

    @Test
    void createTwoRulesPointingToTheSameGoToRule() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        List<Long> old = service.createNewRule(PostRuleRequest.builder()
                .rules(List.of(simpleRule))
                .build());
        assertEquals(old.size(), 1);
        var simpleRuleWithId = gotoAction.toBuilder().gotoAction(simpleRule.withId(old.get(0)).toUnmodifiableList()).build();
        RuleDTO branchRuleWithOldGoTo = branchRuleWithGoTo.toBuilder()
                .elseAction(simpleRuleWithId.toUnmodifiableList())
                .build();
        createRules(List.of(branchRuleWithOldGoTo));
        //we created Rule that points to simpleRule.
        //then create a new rule that points to simpleRule too
        RuleDTO complexRuleWithPointerToOldRule = secondComplexRule
                .toBuilder()
                .elseAction(simpleRuleWithId.toUnmodifiableList()).build();
        createRules(complexRuleWithPointerToOldRule.toUnmodifiableList(),
                complexRuleWithPointerToOldRule, simpleRule);
    }

    private void createRules(List<RuleDTO> branchRuleWithOldGoTo) {
        createRules(branchRuleWithOldGoTo, branchRuleWithGoTo, simpleRule);
    }

    private void createRules(List<RuleDTO> saveSource, RuleDTO main, RuleDTO elseRule) {
        var id = service.createNewRule(PostRuleRequest.builder()
                .rules(saveSource)
                .build());
        log.info(STR. "id: \{ id }" );
        var saved = repository.findRuleById(id.get(0)).orElse(null);
        assertNotNull(saved);
        assertEquals(saved.getThenAction().size(), 1);
        assertEquals(main.name(), saved.getName());
        assertEquals(main.thenAction().get(0).name(), saved.getThenAction().get(0).getName());
        assertEquals(main.elseAction().get(0).name(), saved.getElseAction().get(0).getName());
        assertEquals(main.elseAction().get(0).gotoAction().get(0).name(),
                saved.getElseAction().get(0).getGotoAction().get(0).getName());
        assertEquals(elseRule.condition(), saved.getElseAction().get(0).getGotoAction().get(0).getCondition());
    }
}