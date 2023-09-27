package org.example.travellingsalesmanservice.data.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.data.domain.DatasetDTO;
import org.example.travellingsalesmanservice.data.service.StorageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lab1/data")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "**", "localhost:3000", "http://localhost:3000"})
public class StorageController {
    private final StorageService service;

    @PostMapping
    public void saveDataset(@RequestBody @Valid DatasetDTO request) {
        service.saveDataset(request);
    }

    @GetMapping("/{name}")
    public DatasetDTO getDataset(@PathVariable String name) {
        return service.findDatasetByName(name);
    }

    @GetMapping("/all")
    public List<String> getAllDatasets() {
        return service.getAll();
    }
}
