package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class CrossoverAlgorithmFactory {

    private final CycleCrossoverMethod cycleCrossoverMethod;
    private final OnePointCrossoverMethod onePointCrossoverMethod;
    private final SearcherFactory searcherFactory;

    public CrossoverAlgorithm getCrossover(TaskConfig config, int[] pathLengths, Chromosome chromosome) {
        CrossoverMethod crossoverMethod = switch (config.crossoverType()) {
            case CYCLIC -> cycleCrossoverMethod;
            case ONE_POINT -> onePointCrossoverMethod;
        };
        SecondParentSearcher searcher = searcherFactory.getSearcher(config.searcherConfig(), pathLengths, chromosome);
        RandomGeneMutation mutation = new RandomGeneMutation(new Random());
        return new ManyToManyCrossoverAlgorithm(crossoverMethod, mutation, searcher);
//        return OneToManyCrossoverAlgorithm.builder()
//                .crossoverMethod(crossoverMethod)
//                .mutation(mutation)
//                .build();
    }
}
