package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories.NetworkRepository;

import static ua.kpi.iasa.sd.hopfieldneuralnetwork.service.ImageProcessingService.DIMENSION;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskService {

    private final HopfieldCalculator calculator;
    private final ImageProcessingService imgService;
    private final NetworkRepository repository;
    private final WeightConverter converter;

    public Pattern createTask(@Valid PostTaskRequest postRequest) {
        return repository.findByNameIgnoreCase(postRequest.networkName()).map(n -> {
            var weight = converter.convertToDTO(n.getWeight());
            return calculator.recallPattern(postRequest.pattern(), weight, weight.w().length);
        }).orElseThrow(() -> new IllegalArgumentException(STR. "Network with name \{ postRequest.networkName() } doesn't exist" ));
    }

    public Resource createTask(MultipartFile image, String name) {
        return repository.findByNameIgnoreCase(name).map(n -> {
            var weight = converter.convertToDTO(n.getWeight());
            var pattern = imgService.resizeAndConvertImage(image, (int) Math.sqrt(weight.w().length));
            Pattern response = calculator.recallPattern(pattern, weight, Math.max(DIMENSION / 10, 5));
            return imgService.convertPatternToImageBytes(response, response.p().length);
        }).orElseThrow(() -> new IllegalArgumentException(STR. "Network with name \{ name } doesn't exist" ));
    }
}
