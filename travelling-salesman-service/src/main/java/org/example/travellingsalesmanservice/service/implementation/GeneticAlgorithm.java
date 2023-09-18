package org.example.travellingsalesmanservice.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.domain.AlgorithmConfiguration;
import org.example.travellingsalesmanservice.domain.Dataset;
import org.example.travellingsalesmanservice.domain.Result;
import org.example.travellingsalesmanservice.domain.TrackingEntity;
import org.example.travellingsalesmanservice.service.PathLengthEstimator;
import org.example.travellingsalesmanservice.service.TravellingSalesmanSolver;
import org.example.travellingsalesmanservice.service.XYPopulationGenerator;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
@RequiredArgsConstructor
class GeneticAlgorithm implements TravellingSalesmanSolver {
    private final PathLengthEstimator estimator;

    private final XYPopulationGenerator generator;

    @Override
    public TrackingEntity start(Dataset dataset, AlgorithmConfiguration config) {
        Result currentBestResult;
        var chromosomes = generator.generateChromosomes(dataset, config.populationSize());
        var pathLengths = new int[config.populationSize()];
        estimator.calculateSquaredPathLength(chromosomes, pathLengths);
        return null;
    }


}
