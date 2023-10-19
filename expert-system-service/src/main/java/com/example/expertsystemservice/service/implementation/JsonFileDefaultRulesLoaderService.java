package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.config.Config;
import com.example.expertsystemservice.domain.PostRuleRequest;
import com.example.expertsystemservice.domain.RuleDTO;
import com.example.expertsystemservice.service.DefaultRulesLoaderService;
import com.example.expertsystemservice.service.RuleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonFileDefaultRulesLoaderService implements DefaultRulesLoaderService {
    private final Config config;
    private final RuleService ruleService;

    @Override
    public List<Long> loadRulesFromDefaultSource() {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonFilePath = config.getPathToDefaultRules();
        RuleDTO[] rules;
        try (FileInputStream fileInputStream = new FileInputStream(jsonFilePath)) {
            rules = objectMapper.readValue(fileInputStream, RuleDTO[].class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ruleService.createNewRule(PostRuleRequest.builder()
                .rules(Arrays.stream(rules).toList())
                .build());
    }
}
