package org.example.travellingsalesmanservice.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.domain.Chromosome;
import org.example.travellingsalesmanservice.service.CrossoverMethod;
import org.example.travellingsalesmanservice.service.SecondParentSearcher;

import java.util.random.RandomGenerator;

@RequiredArgsConstructor
public class OnePointCrossover implements CrossoverMethod {
    private final RandomGenerator rand;

    @Override
    public void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher) {
        int chromosomeLength = (p.x().length - 1) / pathLengths.length;
        Chromosome child1 = Chromosome.ofLength(chromosomeLength);
        Chromosome child2 = Chromosome.ofLength(chromosomeLength);
        Chromosome parent1 = Chromosome.ofLength(chromosomeLength);
        Chromosome parent2 = Chromosome.ofLength(chromosomeLength);
        for (int i = 0; i < pathLengths.length; i++) {
            int j = searcher.findSecond(i, pathLengths);
            parent1.fillWith(0, p, i, chromosomeLength);
            parent2.fillWith(0, p, j, chromosomeLength);
            createTwoChildren(parent1, parent2, child1, child2);
        }
    }

    private void createTwoChildren(Chromosome parent1, Chromosome parent2, Chromosome child1, Chromosome child2) {
        int length = parent1.x().length;
        int split = rand.nextInt(length);
        // Create child1 using genetic material from parent1 up to the split point
        child1.fillWith(0, parent1, 0, split);
        // Fill the remaining part of child1 with genetic material from parent2
        child1.fillWith(split, parent2, split, length - split);
        // Create child2 using genetic material from parent2 up to the split point
        child2.fillWith(0, parent2, 0, split);
        // Fill the remaining part of child2 with genetic material from parent1
        child2.fillWith(split, parent1, split, length - split);
    }
}
