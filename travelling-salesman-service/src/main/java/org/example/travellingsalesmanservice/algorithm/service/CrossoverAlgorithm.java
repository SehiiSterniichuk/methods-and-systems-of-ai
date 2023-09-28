package org.example.travellingsalesmanservice.algorithm.service;

import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;

public interface CrossoverAlgorithm {
    void performCrossover(Chromosome p, int[] pathLengths, float mutationProbability);
}
