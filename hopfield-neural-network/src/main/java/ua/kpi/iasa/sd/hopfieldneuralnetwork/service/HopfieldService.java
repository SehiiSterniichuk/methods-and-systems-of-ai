package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.PostTaskRequest;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HopfieldService implements TaskService {
    private final HopfieldCalculator calculator;
    private Weight weight;

    public Long createNetwork(@Valid PostRequest postRequest) {
        validate(postRequest);
        weight = calculator.calc(postRequest.patterns());
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
    public Long createTask(@Valid PostTaskRequest postRequest) {
        Pattern pattern = calculator.findPattern(postRequest.pattern(), weight, 10, 0.5f);
        System.out.println(pattern);
        return 1L;
    }
}
