package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Chromosome;

public interface CrossoverMethod {
    void createTwoChildren(Chromosome parent1, Chromosome parent2, Chromosome child1, Chromosome child2);
}
