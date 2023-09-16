package org.example.travellingsalesmanservice.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"*", "**", "localhost:3000", "http://localhost:3000"})
@RequestMapping("api/v1/math")
public class MathController {
    @PostMapping("/sum/{v1}/{v2}")
    public int getSum(@PathVariable int v1,@PathVariable int v2) {
        return v1 + v2;
    }
}
