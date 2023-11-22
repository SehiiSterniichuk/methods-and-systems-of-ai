package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.*;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories.ArrayRepository;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories.NetworkRepository;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.repositories.WeightRepository;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class HopfieldService {
    private final HopfieldCalculator calculator;
    private final ImageProcessingService imgService;
    private final NetworkRepository repository;
    private final WeightRepository weightRepository;
    private final ArrayRepository arrayRepository;
    private final WeightConverter converter;


    public Long createNetwork(@Valid @NotNull PostRequest postRequest) {
        validate(postRequest);
        var name = postRequest.name();
        if (repository.existsByNameIgnoreCase(postRequest.name())) {
            String message = STR. "Network with name \{ postRequest.name() } already exists" ;
            log.error(message);
            return -1L;
        }
        var weight = calculator.calculateWeightMatrix(postRequest.patterns());
        return createNetwork(name, weight);
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

    public Long createNetwork(MultipartFile[] images, String name) {
        if (repository.existsByNameIgnoreCase(name)) {
            String message = STR. "Network with name \{ name } already exists" ;
            log.error(message);
            return -1L;
        }
        var weight = new Weight(calculator.calculateWeightMatrixFromFlat(imgService.processImages(images)));
        return createNetwork(name, weight);
    }

    private Long createNetwork(String name, Weight weight) {
        Network n = new Network();
        n.setName(name);
        WeightEntity weightEntity = new WeightEntity();
        List<ArrayRow> rows = converter.toArrayRows(weight);
        arrayRepository.saveAll(rows);
        weightEntity.setW(rows);
        weightRepository.save(weightEntity);
        n.setWeight(weightEntity);
        return repository.save(n).getId();
    }
}
