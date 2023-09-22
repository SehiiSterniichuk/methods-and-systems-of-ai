package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.XYPopulationGenerator;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class SingleThreadCrossoverAlgorithmTest {

    @Test
    public void simpleTest() {
        int amountOfGenes = 5;
        int datasetSize = 3;
        Chromosome chromosome = XYPopulationGenerator.randomPopulation(new SimpleXYPopulationGenerator(), datasetSize, amountOfGenes);
        var crossoverMethod = new SingleThreadCrossoverAlgorithm(new CycleCrossoverMethod(), new RandomGeneMutation(new Random()));
        var estimator = new SimplePathLengthEstimator();
        int[] paths = new int[datasetSize];
        estimator.calculateSquaredPathLength(chromosome, paths);
        Chromosome old = Chromosome.ofLength(chromosome.size());
        old.fillWith(0, chromosome, 0, chromosome.size());
        crossoverMethod.crossover(chromosome, paths, new InbreedingParentSearcher());
        for (var path : paths) {
            assertEquals(-1, path);
        }
    }
}