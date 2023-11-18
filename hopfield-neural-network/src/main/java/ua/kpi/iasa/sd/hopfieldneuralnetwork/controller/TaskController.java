package ua.kpi.iasa.sd.hopfieldneuralnetwork.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping("/img")
    public ResponseEntity<Resource> postNetwork(@RequestParam MultipartFile image, @RequestParam @Valid @NotBlank String name){
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + "response.png" + "\"").body(service.createTask(image, name));
    }
}
