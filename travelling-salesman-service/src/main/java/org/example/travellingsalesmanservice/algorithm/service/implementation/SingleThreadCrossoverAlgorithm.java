package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.example.travellingsalesmanservice.algorithm.service.Mutation;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;

import java.util.Random;

import static org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher.PARENT_NOT_FOUND;

@RequiredArgsConstructor
@Builder
public class SingleThreadCrossoverAlgorithm implements CrossoverAlgorithm {
    private final CrossoverMethod crossoverMethod;
    private final Mutation mutation;
    private final SecondParentSearcher searcher;
    private final Random rand = new Random();

    @Override
    public void performCrossover(Chromosome p, int[] pathLengths, float mutationProbability) {
        int chromosomeLength = (p.x().length - 1) / pathLengths.length;
        Chromosome child1 = Chromosome.ofLength(chromosomeLength);
        Chromosome child2 = Chromosome.ofLength(chromosomeLength);
        Chromosome parent1 = Chromosome.ofLength(chromosomeLength);
        Chromosome parent2 = Chromosome.ofLength(chromosomeLength);
        int processedParentsCounter = 0;
        for (int i = 0; i < pathLengths.length && processedParentsCounter != pathLengths.length; i++) {
            if (pathLengths[i] <= 0) {
                continue;
            } else if (isMutation(mutationProbability)) {
                mutation.mutate(p, i * chromosomeLength, chromosomeLength);
                pathLengths[i] = -1;
                processedParentsCounter++;
                continue;
            }
            int j = searcher.findSecond(i);
            int p1 = i * chromosomeLength;
            int p2 = j * chromosomeLength;
            if (j == PARENT_NOT_FOUND || p.equalsSubChromosomes(p1, p2, chromosomeLength)) {
                mutation.mutate(p, p1, chromosomeLength);
                pathLengths[i] = -1;
                processedParentsCounter++;
                continue;
            }
            parent1.fillWith(0, p, p1, chromosomeLength);
            parent2.fillWith(0, p, p2, chromosomeLength);
            pathLengths[i] = pathLengths[j] = -1;
            crossoverMethod.createTwoChildren(parent1, parent2, child1, child2);
            p.fillWith(p1, child1, 0, chromosomeLength);
            p.fillWith(p2, child2, 0, chromosomeLength);
            processedParentsCounter += 2;
        }
    }

    private boolean isMutation(float probability) {
        return rand.nextFloat(1) < probability;
    }
}
