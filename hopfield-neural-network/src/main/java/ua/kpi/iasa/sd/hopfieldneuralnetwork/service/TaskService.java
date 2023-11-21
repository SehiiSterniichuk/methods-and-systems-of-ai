package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories.NetworkRepository;

@Service
@RequiredArgsConstructor
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
            Pattern response = calculator.recallPattern(pattern, weight, Math.min(weight.w().length, 25));
            return imgService.convertPatternToImageBytes(response, response.p().length);
        }).orElseThrow(() -> new IllegalArgumentException(STR. "Network with name \{ name } doesn't exist" ));
    }
}
