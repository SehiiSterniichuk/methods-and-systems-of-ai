package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Chromosome;

public interface CrossoverMethod {
    void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher);
}
