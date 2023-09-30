package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.BreedingType;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.domain.Distance;
import org.example.travellingsalesmanservice.algorithm.domain.SearcherConfig;
import org.example.travellingsalesmanservice.algorithm.service.XYPopulationGenerator;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class ManyToManyCrossoverAlgorithmTest {

    @Test
    public void simpleTest() {
        int amountOfGenes = 5;
        int datasetSize = 3;
        Chromosome chromosome = XYPopulationGenerator.randomPopulation(new SimpleXYPopulationGenerator(), datasetSize, amountOfGenes);
        var estimator = new SimplePathLengthEstimator();
        int[] paths = new int[datasetSize];
        SearcherConfig config = SearcherConfig.builder()
                .breedingType(BreedingType.INBREEDING)
                .diffPercent(33)
                .distance(Distance.SCALAR)
                .build();
        var searcher = (new SearcherFactory()).getSearcher(config, paths, chromosome);
        var crossoverMethod = ManyToManyCrossoverAlgorithm.builder()
                .crossoverMethod(new CycleCrossoverMethod())
                .searcher(searcher)
                .mutation(new RandomGeneMutation(new Random()))
                .build();
        estimator.calculateSquaredPathLength(chromosome, paths);
        Chromosome old = Chromosome.ofLength(chromosome.size());
        old.fillWith(0, chromosome, 0, chromosome.size());
        crossoverMethod.performCrossover(chromosome, paths, 0.0f);
        for (var path : paths) {
            assertEquals(-1, path);
        }
    }
}