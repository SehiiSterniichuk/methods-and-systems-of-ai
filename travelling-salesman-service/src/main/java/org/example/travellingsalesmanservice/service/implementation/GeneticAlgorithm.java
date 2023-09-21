package org.example.travellingsalesmanservice.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.domain.*;
import org.example.travellingsalesmanservice.service.PathLengthEstimator;
import org.example.travellingsalesmanservice.service.TravellingSalesmanSolver;
import org.example.travellingsalesmanservice.service.XYPopulationGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
class GeneticAlgorithm implements TravellingSalesmanSolver {
    private final PathLengthEstimator estimator;

    private final XYPopulationGenerator generator;

    @Override
    public TrackingEntity start(Dataset dataset, AlgorithmConfiguration config) {
        Result bestResult;
        TrackingEntity entity = new TrackingEntity(new ArrayList<>(1));
        var chromosomes = generator.generateChromosomes(dataset, config.populationSize());
        var pathLengths = new int[config.populationSize()];
        estimator.calculateSquaredPathLength(chromosomes, pathLengths);
        int counterOfSameResults = 0;
        bestResult = findBestPath(chromosomes, pathLengths);
        for (int i = 0; i < config.iterationNumber(); i++) {
            config.crossoverAlgorithm().crossover(chromosomes, pathLengths, config.searcher());
            estimator.calculateSquaredPathLength(chromosomes, pathLengths);
            var currentResult = findBestPath(chromosomes, pathLengths);
            if (currentResult.isBetterThan(bestResult)) {
                bestResult = currentResult;
            } else if (counterOfSameResults < config.allowedNumberOfGenerationsWithTheSameResult()) {
                counterOfSameResults++;
            } else {
                entity.update(bestResult, i);
                break;
            }
            if(i % config.showEachIterationStep() == 0){
                entity.update(bestResult, i);
            }
        }
        return entity;
    }

    private Result findBestPath(Chromosome chromosomes, int[] pathLengths) {
        int numberOfCities = (chromosomes.x().length - 1) / pathLengths.length;
        int min = pathLengths[0];
        int minIndex = 0;
        for (int i = 1; i < pathLengths.length; i++) {
            int path = pathLengths[i];
            if (path < min) {
                min = path;
                minIndex = i;
            }
        }
        Point[] bestPath = new Point[numberOfCities];
        for (int i = 0; i < numberOfCities; i++) {
            int index = i + minIndex;
            bestPath[i] = new Point(chromosomes.x()[index], chromosomes.y()[index]);
        }
        return new Result(bestPath, Math.sqrt(min));
    }


}
