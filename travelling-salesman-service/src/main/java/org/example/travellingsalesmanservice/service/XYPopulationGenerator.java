package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Chromosome;
import org.example.travellingsalesmanservice.domain.Dataset;
import org.example.travellingsalesmanservice.domain.Point;

import java.util.Random;

public interface XYPopulationGenerator {
    Chromosome generateChromosomes(Dataset dataset, int amountOfGenes);

    @SuppressWarnings("SuspiciousNameCombination")
    static Chromosome randomPopulation(XYPopulationGenerator generator, int datasetSize, int amountOfGenes) {
        Point[] array = (new Random()).ints(1, 100 * datasetSize)
                .distinct()
                .limit(datasetSize)
                .mapToObj(x -> new Point(x, x)).toArray(Point[]::new);
        return generator.generateChromosomes(new Dataset(array), amountOfGenes);
    }
}
