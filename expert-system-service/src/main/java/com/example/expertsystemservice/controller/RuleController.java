package com.example.expertsystemservice.controller;

import com.example.expertsystemservice.domain.GetRuleRequest;
import com.example.expertsystemservice.domain.PostRuleRequest;
import com.example.expertsystemservice.domain.RuleDTO;
import com.example.expertsystemservice.service.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expert-system/rule")
@CrossOrigin(origins = {"http://localhost:3000"})
public class RuleController {
    private final RuleService service;

    //method to retrieve a rule and its hierarchy at a given depth
    @GetMapping
    public RuleDTO getRule(@RequestBody @Valid GetRuleRequest request) {
        return service.getRule(request);
    }

    //create a new rule. If the rule uses new rules as branches
    // they should be provided in the list(field of request body) too.
    // if a rule uses existing rules as branches, then the branches must contain the ID OR the name of the existing rule
    // So, it means that each element of the list has 2 as maximum height of its hierarchy
    @PostMapping
    public List<Long> postRules(@RequestBody @Valid PostRuleRequest request) {
        return service.createNewRule(request);
    }

    @DeleteMapping("/{id}")
    public long deleteRule(@PathVariable long id) {
        return service.delete(id);
    }

    @DeleteMapping("/all-hierarchy/{id}")
    public long deleteAllRuleHierarchy(@PathVariable long id) {
        return service.deleteAll(id);
    }

    @DeleteMapping("/all")
    public List<Long> deleteAll() {
        return service.deleteAll();
    }
}
