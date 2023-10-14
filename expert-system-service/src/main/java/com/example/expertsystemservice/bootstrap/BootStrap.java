package com.example.expertsystemservice.bootstrap;

import com.example.expertsystemservice.domain.Action;
import com.example.expertsystemservice.domain.Rule;
import com.example.expertsystemservice.repository.ActionRepository;
import com.example.expertsystemservice.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BootStrap implements CommandLineRunner {
    private final RuleRepository ruleRepository;
    private final ActionRepository actionRepository;

    @Override
    public void run(String... args) {
        log.info("java.version: " + System.getProperty("java.version"));
        Action doAction = Action.builder()
                .name("Do action")
                .build();
        Rule firstTestRule = Rule.builder()
                .name("first run")
                .condition("is first run?")
                .build();
        firstTestRule = ruleRepository.save(firstTestRule);
        doAction = actionRepository.save(doAction);
        ruleRepository.connectThen(firstTestRule, doAction);
//        Action gotoAction = Action.builder()
//                .name("GOTO action")
//                .build();
//        gotoAction = actionRepository.save(gotoAction);
//
//        Rule relationTestRule = Rule.builder()
//                .name("relation run")
//                .condition("is relation run?")
//                .build();
//        relationTestRule = ruleRepository.save(relationTestRule);
//
//// Create the relationship between 'relationTestRule' and 'gotoAction'
//        relationTestRule.setThenAction(List.of(gotoAction));
//        ruleRepository.save(relationTestRule);
//
//// Create the relationship between 'gotoAction' and 'firstTestRule'
//        gotoAction.setGotoAction(List.of(firstTestRule));
//        actionRepository.save(gotoAction);
        log.info("Entities and relationships saved successfully.");
    }
}

