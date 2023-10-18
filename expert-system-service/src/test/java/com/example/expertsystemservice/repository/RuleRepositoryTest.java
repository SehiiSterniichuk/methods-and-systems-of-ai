package com.example.expertsystemservice.repository;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RuleRepositoryTest {

    @Autowired
    private RuleRepository repository;
    private final Rule rule = Rule.builder()
            .condition("Unexpected condition")
            .name("Without branched actions :(")
            .thenAction(Action.builder().name("OK action").build().toUnmodifiableList())
            .build();

    @Test
    void deleteByName() {
        boolean exist = repository.existsRuleByName(rule.getName());
        assertFalse(exist);
        repository.save(rule);
        exist = repository.existsRuleByName(rule.getName());
        assertTrue(exist);
        repository.deleteByName(rule.getName());
    }
}