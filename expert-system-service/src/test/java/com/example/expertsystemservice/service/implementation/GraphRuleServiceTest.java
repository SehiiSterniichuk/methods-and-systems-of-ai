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
            .build();

    private final ActionDTO thenAction = ActionDTO.builder()
            .name("new simple then action")
            .build();

    private final RuleDTO branchRule = RuleDTO.builder()
            .condition("Unexpected 2")
            .name("With new branches!")
            .thenAction(thenAction.toUnmodifiableList())
            .build();

    @BeforeEach
    void setUp() {
        service = new GraphRuleService(repository);
    }

    @AfterEach
    void tearDown() {
        repository.deleteByName(simpleRule.name());
        actionRepository.deleteActionByName(thenAction.name());
    }

    @Test
    void createNewRuleSimple() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        long id = service.createNewRule(PostRuleRequest.builder()
                .rules(simpleRule.toUnmodifiableList())
                .build());
        log.info(STR."id: \{id}");
        var saved = repository.findRuleById(id).orElse(null);
        assertNotNull(saved);
        assertEquals(converter.toDTO(saved), simpleRule);
    }

    @Test
    void createNewRuleWithNewActions() {
        boolean exist = repository.existsRuleByName(simpleRule.name());
        assertFalse(exist);
        long id = service.createNewRule(PostRuleRequest.builder()
                .rules(branchRule.toUnmodifiableList())
                .build());
        log.info(STR."id: \{id}");
        var saved = repository.findRuleById(id).orElse(null);
        assertNotNull(saved);
        assertEquals(saved.getThenAction().size(), 1);
        assertEquals(converter.toDTO(saved), simpleRule);
    }
}