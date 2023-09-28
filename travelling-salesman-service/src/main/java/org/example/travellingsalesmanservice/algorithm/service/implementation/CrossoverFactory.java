package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.springframework.stereotype.Component;

import java.util.Random;
@Component
@RequiredArgsConstructor
public class CrossoverFactory {
    private final SearcherFactory searcherFactory;
    public CrossoverAlgorithm getCrossover(TaskConfig config, int[] pathLengths, Chromosome chromosome) {
        return new SingleThreadCrossoverAlgorithm(new CycleCrossoverMethod(),
                new RandomGeneMutation(new Random()),
                searcherFactory.getSearcher(config.searcherConfig(), pathLengths,chromosome));
    }
}
