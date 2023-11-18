package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class HopfieldService implements TaskService {
    private final HopfieldCalculator calculator;
    private final ImageProcessingService imgService;
    private Weight weight;

    public Long createNetwork(@Valid @NotNull PostRequest postRequest) {
        validate(postRequest);
        weight = calculator.calculateWeightMatrix(postRequest.patterns());
        return 1L;
    }

    private static void validate(PostRequest postRequest) {
        int length = postRequest.patterns().get(0).p().length;
        postRequest.patterns().forEach(pattern -> {
            assert length == pattern.p().length;
            IntStream.range(0, length).map(i -> pattern.p()[i].length).forEach(l -> {
                assert l == length;
            });
        });
    }

    @Override
    public Pattern createTask(@Valid PostTaskRequest postRequest) {
        return calculator.recallPattern(postRequest.pattern(), weight, weight.w().length);
    }

    @Override
    public Resource createTask(MultipartFile image, String name) {
        var pattern = imgService.resizeAndConvertImage(image, (int) Math.sqrt(weight.w().length));
        log.info(STR. "pattern:\{ pattern.length } \{ weight.w().length }" ); //pattern:25 25
        Pattern response = calculator.recallPattern(pattern, weight, Math.min(weight.w().length, 25));
        return imgService.convertPatternToImageBytes(response, response.p().length);
    }

    public Long createNetwork(MultipartFile[] images, @SuppressWarnings("unused") String name) {
        weight = new Weight(calculator.calculateWeightMatrixFromFlat(imgService.processImages(images)));
        return 2L;
    }
}
