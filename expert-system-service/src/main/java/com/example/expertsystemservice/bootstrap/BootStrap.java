package com.example.expertsystemservice.bootstrap;

import com.example.expertsystemservice.repository.RuleRepository;
import com.example.expertsystemservice.service.DefaultRulesLoaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BootStrap implements CommandLineRunner {
    private final RuleRepository repository;
    private final DefaultRulesLoaderService loaderService;
    @Override
    public void run(String... args) {
        log.info("java.version: " + System.getProperty("java.version"));
        if(repository.findAnyRule().isEmpty()){
            loaderService.loadRulesFromDefaultSource();
            log.info(STR."Loaded \{repository.count()} rules");
        }
    }
}

