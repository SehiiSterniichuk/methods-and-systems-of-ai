package com.example.expertsystemservice.controller;

import com.example.expertsystemservice.service.DefaultRulesLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expert-system/rule-loader")
@CrossOrigin(origins = {"http://localhost:3000"})
public class DefaultRulesLoaderController {
    private final DefaultRulesLoaderService service;
    @PostMapping
    public List<Long> loadRulesFromDefaultSource() {
        return service.loadRulesFromDefaultSource();
    }
}
