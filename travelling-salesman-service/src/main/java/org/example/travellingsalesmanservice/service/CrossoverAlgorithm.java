package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Chromosome;

public interface CrossoverAlgorithm {
    void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher, float mutationProbability);
    void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher);
}
