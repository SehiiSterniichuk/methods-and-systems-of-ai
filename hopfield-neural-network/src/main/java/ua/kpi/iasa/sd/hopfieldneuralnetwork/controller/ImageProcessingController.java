package ua.kpi.iasa.sd.hopfieldneuralnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.service.ImageProcessingService;

@RestController
@RequestMapping("api/v1/hopfield/img")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class ImageProcessingController {

    private final ImageProcessingService service;

    @PostMapping("/convert-to-pattern")
    public int[][] processImages(@RequestParam("images") MultipartFile[] images) {
        return service.processImages(images);
    }
}

