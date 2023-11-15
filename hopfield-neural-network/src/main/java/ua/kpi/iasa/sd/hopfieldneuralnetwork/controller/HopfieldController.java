package ua.kpi.iasa.sd.hopfieldneuralnetwork.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.service.HopfieldService;

@RestController
@RequestMapping("api/v1/hopfield/network")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000"})
public class HopfieldController {

    private final HopfieldService service;
    @PostMapping
    public Long postNetwork(@RequestBody PostRequest postRequest){
        return service.createNetwork(postRequest);
    }
}
