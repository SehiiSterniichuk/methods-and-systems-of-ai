package com.example.expertsystemservice.controller;

import com.example.expertsystemservice.domain.decision.DecisionRequest;
import com.example.expertsystemservice.domain.decision.DecisionResponse;
import com.example.expertsystemservice.domain.decision.Variable;
import com.example.expertsystemservice.service.DecisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expert-system/decision")
@CrossOrigin(origins = {"http://localhost:3000"})
public class DecisionController {
    private final DecisionService service;

    @PostMapping
    public DecisionResponse makeDecision(@Valid DecisionRequest request) {
        return service.makeDecision(request);
    }

    @GetMapping("/output-variable-name")
    public String getOutputVariable(){
        return Variable.OUTPUT_VARIABLE;
    }

    @GetMapping("/variable-postfix")
    public String getVariablePostfix(){
        return Variable.VARIABLE_POSTFIX;
    }
}
