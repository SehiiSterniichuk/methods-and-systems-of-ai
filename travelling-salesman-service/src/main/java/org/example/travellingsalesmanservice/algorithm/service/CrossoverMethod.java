package org.example.travellingsalesmanservice.algorithm.service;

import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;

public interface CrossoverMethod {
    void createTwoChildren(Chromosome parent1, Chromosome parent2, Chromosome child1, Chromosome child2);
}
