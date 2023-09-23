package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.example.travellingsalesmanservice.algorithm.service.Mutation;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.springframework.stereotype.Component;

import java.util.Random;

import static org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher.PARENT_NOT_FOUND;

@RequiredArgsConstructor
@Component
public class SingleThreadCrossoverAlgorithm implements CrossoverAlgorithm {
    private final CrossoverMethod crossoverMethod;
    private final Mutation mutation;
    private final Random rand = new Random();

    @Override
    public void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher, float mutationProbability) {
        int chromosomeLength = (p.x().length - 1) / pathLengths.length;
        Chromosome child1 = Chromosome.ofLength(chromosomeLength);
        Chromosome child2 = Chromosome.ofLength(chromosomeLength);
        Chromosome parent1 = Chromosome.ofLength(chromosomeLength);
        Chromosome parent2 = Chromosome.ofLength(chromosomeLength);
        for (int i = 0; i < pathLengths.length; i++) {
            int j = searcher.findSecond(i, pathLengths);
            int p1 = i * chromosomeLength;
            int p2 = j * chromosomeLength;
            if (j == PARENT_NOT_FOUND || p.equalsSubChromosomes(p1, p2, chromosomeLength) || isMutation(mutationProbability)) {
                mutation.mutate(p, p1, chromosomeLength);
                pathLengths[i] = -1;
                continue;
            }
            parent1.fillWith(0, p, p1, chromosomeLength);
            parent2.fillWith(0, p, p2, chromosomeLength);
            pathLengths[i] = -1;
            //todo check whether I need to mark second parent or not
            crossoverMethod.createTwoChildren(parent1, parent2, child1, child2);
            p.fillWith(p1, child1, 0, chromosomeLength);
            p.fillWith(p2, child2, 0, chromosomeLength);
        }
    }

    private boolean isMutation(float probability) {
        return rand.nextFloat(1) < probability;
    }

    @Override
    public void crossover(Chromosome p, int[] pathLengths, SecondParentSearcher searcher) {
        crossover(p, pathLengths, searcher, 0.1f);
    }
}
