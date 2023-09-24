package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.*;
import org.example.travellingsalesmanservice.algorithm.service.PathLengthEstimator;
import org.example.travellingsalesmanservice.algorithm.service.TrackingEntity;
import org.example.travellingsalesmanservice.algorithm.service.TravellingSalesmanSolver;
import org.example.travellingsalesmanservice.algorithm.service.XYPopulationGenerator;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.springframework.stereotype.Component;

import static java.lang.StringTemplate.STR;

@RequiredArgsConstructor
@Component
@Slf4j
class GeneticAlgorithm implements TravellingSalesmanSolver {
    private final PathLengthEstimator estimator;
    private final XYPopulationGenerator generator;

    @Override
    public void start(Dataset dataset, AlgorithmConfiguration configuration) {
        if (dataset.data().length < 4) {
            handleSimpleTask(dataset, configuration);
            return;
        }
        var taskConfig = configuration.taskConfig();
        var entity = configuration.trackingEntity();
        var chromosomes = generator.generateChromosomes(dataset, taskConfig.populationSize());
        var pathLengths = new int[taskConfig.populationSize()];
        estimator.calculateSquaredPathLength(chromosomes, pathLengths);
        try {
            start(configuration, chromosomes, pathLengths, taskConfig, entity);
        } catch (RuntimeException e) {
            entity.putFinish(null, -1, e.getMessage());
            throw e;
        }
    }

    private void start(AlgorithmConfiguration configuration, Chromosome chromosomes,
                       int[] pathLengths, TaskConfig taskConfig, TrackingEntity entity) {
        int counterOfSameResults = 0;
        Result bestResult = findBestPath(chromosomes, pathLengths);
        int i = 0;
        for (; i < taskConfig.iterationNumber(); i++) {
            configuration.crossoverAlgorithm().crossover(chromosomes, pathLengths, configuration.searcher(),
                    configuration.taskConfig().mutationProbability());
            estimator.calculateSquaredPathLength(chromosomes, pathLengths);
            var currentResult = findBestPath(chromosomes, pathLengths);
            if (currentResult.isBetterThan(bestResult)) {
                bestResult = currentResult;
                counterOfSameResults = 0;
                entity.put(bestResult, i, "New best result");
            } else if (counterOfSameResults < taskConfig.allowedNumberOfGenerationsWithTheSameResult()) {
                counterOfSameResults++;
            } else {
                entity.putFinish(bestResult, i, STR."Finished. Counter of the same result: \{counterOfSameResults}");
                break;
            }
            boolean show = i % taskConfig.showEachIterationStep() == 0;
            boolean finish = i + 1 == taskConfig.iterationNumber();
            if (show && !finish) {
                entity.put(bestResult, i, "Show iteration");
            } else if (finish) {
                entity.putFinish(bestResult, i, STR."Finished all iterations.");
            }
        }
    }

    private void handleSimpleTask(Dataset dataset, AlgorithmConfiguration configuration) {
        Result result = Result.builder()
                .path(dataset.data())
                .pathLength(calculatePath(dataset.data()))
                .build();
        configuration.trackingEntity().putFinish(result, 0);
    }

    private double calculatePath(Point[] data) {
        if (data.length <= 1) {
            return 0;
        } else if (data.length == 2) {
            return data[0].distance(data[1]);
        } else if (data.length == 3) {
            return data[0].distance(data[1]) + data[1].distance(data[2]) + data[2].distance(data[0]);
        } else {
            throw new UnsupportedOperationException("It's for small sizes");
        }
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
        int index = minIndex * numberOfCities;
        for (int i = 0; i < numberOfCities; i++, index++) {
            bestPath[i] = new Point(chromosomes.x()[index], chromosomes.y()[index]);
        }
        return new Result(bestPath, Math.sqrt(min));
    }
}
