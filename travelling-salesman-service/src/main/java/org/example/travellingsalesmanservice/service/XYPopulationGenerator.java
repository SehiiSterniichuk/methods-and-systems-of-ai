package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Dataset;

public interface XYPopulationGenerator {
    Points generateChromosomes(Dataset dataset, int amountOfGenes);

    record Points(int[] x, int[] y){
    }
}
