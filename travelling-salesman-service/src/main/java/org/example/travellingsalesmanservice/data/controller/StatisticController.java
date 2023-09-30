package org.example.travellingsalesmanservice.data.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.data.service.implementation.ChartService;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/lab1/chart")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*", "**", "localhost:3000", "http://localhost:3000"})
public class StatisticController {
    private final ChartService service;

    @GetMapping("/generate/{id}")
    public void generateChart(@PathVariable @Valid @NotBlank String id, HttpServletResponse response) throws IOException {
        // Create a dataset
        ByteArrayOutputStream chartImage = service.makeChart(id);
        // Set response headers
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=chart.png");
        // Send the PNG image as a response
        response.getOutputStream().write(chartImage.toByteArray());
        response.getOutputStream().flush();
    }
}

