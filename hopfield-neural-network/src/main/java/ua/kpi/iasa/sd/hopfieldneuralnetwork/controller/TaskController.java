package ua.kpi.iasa.sd.hopfieldneuralnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.service.TaskService;

@RestController
@RequestMapping("api/v1/hopfield/task")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class TaskController {

    private final TaskService service;
    @PostMapping
    public Pattern postTask(@RequestBody PostTaskRequest postRequest){
        return service.createTask(postRequest);
    }
}
