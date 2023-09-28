package org.example.travellingsalesmanservice.data.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.data.domain.Statistic;
import org.example.travellingsalesmanservice.data.service.TaskStorageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lab1/task-storage/")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "**", "localhost:3000", "http://localhost:3000"})
public class TaskStorageController {
    private final TaskStorageService service;

    @GetMapping("/statistic/{id}")
    public List<Statistic> getStatistic(@PathVariable @Valid @NotBlank String id) {
        return service.findStatisticByTaskId(id);
    }
}
