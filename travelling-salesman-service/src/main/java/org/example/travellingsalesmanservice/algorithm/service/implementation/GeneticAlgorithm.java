package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.algorithm.domain.Result;
import org.example.travellingsalesmanservice.algorithm.service.*;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;

import static org.example.travellingsalesmanservice.app.domain.ResultResponse.*;

@RequiredArgsConstructor
@Slf4j
@Builder
class GeneticAlgorithm implements TravellingSalesmanSolver {
    private final PathLengthEstimator estimator;
    private final XYPopulationGenerator generator;
    private final CrossoverFactory crossoverFactory;
    private final TrackingEntity entity;
    private final int[] pathLengths;
    private final Dataset dataset;
    private final TaskConfig taskConfig;

    @Override
    public TrackingEntity start() {
        if (isSimple(dataset)) {
            handleSimpleTask();
            return entity;
        }
        var chromosomes = generator.generateChromosomes(dataset, taskConfig.populationSize());
        estimator.calculateSquaredPathLength(chromosomes, pathLengths);
        try {
            start(chromosomes, crossoverFactory.getCrossover(taskConfig, pathLengths, chromosomes));
        } catch (RuntimeException e) {
            entity.put(getErrorResult(e.getMessage(), -1));
            throw e;
        }
        return entity;
    }



    public static boolean isSimple(Dataset dataset) {
        return dataset.data().length < 4;
    }

    protected void start(Chromosome chromosomes, CrossoverAlgorithm crossover) {
        int counterOfSameResults = 0;
        Result bestResult = findBestPath(chromosomes, pathLengths);
        int i = 0;
        for (; i < taskConfig.iterationNumber(); i++) {
            crossover.performCrossover(chromosomes, pathLengths, taskConfig.mutationProbability());
            estimator.calculateSquaredPathLength(chromosomes, pathLengths);
            var current = findBestPath(chromosomes, pathLengths);
            if (current.isBetterThan(bestResult)) {
                bestResult = current;
                counterOfSameResults = 0;
                entity.put(getNewBestResult(bestResult, i));
            } else if (counterOfSameResults < taskConfig.allowedNumberOfGenerationsWithTheSameResult()) {
                counterOfSameResults++;
            } else {
                entity.put(getResultCounter(bestResult, i, counterOfSameResults));
                break;
            }
            boolean show = i % taskConfig.showEachIterationStep() == 0;
            boolean finish = i + 1 == taskConfig.iterationNumber();
            if (show && !finish) {
                String chromosomesString = chromosomes.toString(0, chromosomes.size() / pathLengths.length);
                log.debug(chromosomesString);
                entity.put(getShowResult(bestResult, i));
            } else if (finish) {
                entity.put(getFinishResult(bestResult, i));
            }
        }
    }

    private void handleSimpleTask() {
        Result result = Result.builder()
                .path(dataset.data())
                .pathLength(calculatePath(dataset.data()))
                .build();
        entity.put(getFinishResult(result, 0));
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
