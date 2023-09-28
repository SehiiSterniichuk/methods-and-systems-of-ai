package org.example.travellingsalesmanservice.app.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.app.domain.PostTaskRequest;
import org.example.travellingsalesmanservice.algorithm.domain.TaskId;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/lab1/tasks/")
@CrossOrigin(origins = {"*", "**", "localhost:3000", "http://localhost:3000"})
@RequiredArgsConstructor
public class TaskController {
    private final TaskService service;
    @PostMapping
    public TaskId createTask(@RequestBody @Valid PostTaskRequest request) {
        return service.createTask(request.config(), request.dataset());
    }

    @GetMapping("{id}")
    public ResultResponse getTask(@PathVariable @Valid @NotBlank String id){
        return service.getTask(id);
    }
}
