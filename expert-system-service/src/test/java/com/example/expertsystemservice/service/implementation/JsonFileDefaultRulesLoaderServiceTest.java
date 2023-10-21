package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.config.Config;
import com.example.expertsystemservice.service.RuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
class JsonFileDefaultRulesLoaderServiceTest {

    @Mock
    private Config config;

    @Mock
    private RuleService ruleService;

    private JsonFileDefaultRulesLoaderService defaultRulesLoaderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        defaultRulesLoaderService = new JsonFileDefaultRulesLoaderService(config, ruleService);
    }

    @Test
    public void testLoadRulesFromDefaultSource() {
        // Mock the behavior of Config to return a file path
        doReturn("default_rules.json").when(config).getPathToDefaultRules();
        doReturn(List.of(1)).when(ruleService).createNewRule(any());
        defaultRulesLoaderService.loadRulesFromDefaultSource();
    }
}