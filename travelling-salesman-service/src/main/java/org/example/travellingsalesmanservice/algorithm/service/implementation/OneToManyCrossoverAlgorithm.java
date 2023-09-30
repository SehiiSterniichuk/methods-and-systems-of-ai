package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.example.travellingsalesmanservice.algorithm.service.Mutation;

import java.util.Random;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Builder
public class OneToManyCrossoverAlgorithm implements CrossoverAlgorithm {
    private final CrossoverMethod crossoverMethod;
    private final Mutation mutation;
    private final Random rand = new Random();

    @Override
    public void performCrossover(Chromosome p, int[] pathLengths, float mutationProbability) {
        int chromosomeLength = (p.x().length - 1) / pathLengths.length;
        int max = 0;
        int bestRes = pathLengths[0];
        for (int i = 1; i < pathLengths.length; i++) {
            int current = pathLengths[i];
            if (current > bestRes) {
                bestRes = current;
                max = i;
            }
        }
        int best = max;
        Chromosome parentBest = Chromosome.ofLength(chromosomeLength);
        parentBest.fillWith(0, p, best * chromosomeLength, chromosomeLength);
        IntStream.range(0, pathLengths.length)
                .parallel()
                .filter(i -> i != best)
                .forEach(i -> {
                    if (isMutation(mutationProbability)) {
                        mutation.mutate(p, i * chromosomeLength, chromosomeLength);
                        return;
                    }
                    int p2 = i * chromosomeLength;
                    if (p.equalsSubChromosomes(best * chromosomeLength, p2, chromosomeLength)) {
                        mutation.mutate(p, p2, chromosomeLength);
                        return;
                    }
                    Chromosome child1 = Chromosome.ofLength(chromosomeLength);
                    Chromosome child2 = Chromosome.ofLength(chromosomeLength);
                    Chromosome parent2 = Chromosome.ofLength(chromosomeLength);
                    parent2.fillWith(0, p, p2, chromosomeLength);
                    crossoverMethod.createTwoChildren(parentBest, parent2, child1, child2);
                    p.fillWith(p2, child2, 0, chromosomeLength);
                });
    }

    private boolean isMutation(float probability) {
        return rand.nextFloat(1) < probability;
    }
}
