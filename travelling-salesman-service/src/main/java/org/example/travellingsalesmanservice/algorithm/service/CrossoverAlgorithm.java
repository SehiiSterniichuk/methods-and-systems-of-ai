package org.example.travellingsalesmanservice.algorithm.service;

import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;

public interface CrossoverAlgorithm {
    void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher, float mutationProbability);
    void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher);
}
