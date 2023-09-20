package org.example.travellingsalesmanservice.domain;

import org.example.travellingsalesmanservice.service.CrossoverMethod;
import org.example.travellingsalesmanservice.service.SecondParentSearcher;

public record AlgorithmConfiguration(int iterationNumber,
                                     int allowedNumberOfGenerationsWithTheSameResult,
                                     int showEachIterationStep,
                                     int populationSize,
                                     float mutationProbability,
                                     CrossoverMethod crossoverMethod,
                                     SecondParentSearcher searcher) {
}
