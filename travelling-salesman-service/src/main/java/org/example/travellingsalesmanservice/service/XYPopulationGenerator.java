package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Dataset;
import org.example.travellingsalesmanservice.domain.Chromosome;

public interface XYPopulationGenerator {
    Chromosome generateChromosomes(Dataset dataset, int amountOfGenes);
}
