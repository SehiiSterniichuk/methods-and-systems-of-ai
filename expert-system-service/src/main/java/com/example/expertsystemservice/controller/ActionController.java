package com.example.expertsystemservice.controller;

import com.example.expertsystemservice.service.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/expert-system/action")
@CrossOrigin(origins = {"http://localhost:3000"})
public class ActionController {
    private final ActionService service;

    @DeleteMapping("/{id}")
    public List<Long> delete(@PathVariable long id) {
        return List.of(service.delete(id));
    }
}
