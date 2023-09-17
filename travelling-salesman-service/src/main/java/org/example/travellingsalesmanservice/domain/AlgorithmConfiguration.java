package org.example.travellingsalesmanservice.domain;

import org.example.travellingsalesmanservice.service.CrossoverMethod;

public record AlgorithmConfiguration(int iterationNumber,
                                     int showEachIterationStep,
                                     int populationSize,
                                     CrossoverMethod crossover) {
}
