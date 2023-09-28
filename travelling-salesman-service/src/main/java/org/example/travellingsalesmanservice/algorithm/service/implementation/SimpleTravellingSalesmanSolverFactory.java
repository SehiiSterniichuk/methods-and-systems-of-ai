package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.service.*;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleTravellingSalesmanSolverFactory {
    private final PathLengthEstimator estimator;
    private final XYPopulationGenerator generator;
    private final CrossoverFactory crossoverFactory;

    public TravellingSalesmanSolver getGeneticSolver(TaskConfig config, Dataset dataset, TrackingEntity entity) {
        if (GeneticAlgorithm.isSimple(dataset)) {
            return GeneticAlgorithm.builder().entity(entity).dataset(dataset).build();
        }
        int[] pathLengths = new int[config.populationSize()];
        return GeneticAlgorithm.builder()
                .entity(entity)
                .pathLengths(pathLengths)
                .estimator(estimator)
                .crossoverFactory(crossoverFactory)
                .generator(generator)
                .dataset(dataset)
                .taskConfig(config)
                .build();
    }


}
