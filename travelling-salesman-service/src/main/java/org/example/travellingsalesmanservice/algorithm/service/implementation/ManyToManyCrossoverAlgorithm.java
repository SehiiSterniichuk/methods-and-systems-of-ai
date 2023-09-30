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
public class ManyToManyCrossoverAlgorithm implements CrossoverAlgorithm {
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
        //поки всі особини не взяли участь в кросовері
        for (int i = 0; i < pathLengths.length && processedParentsCounter != pathLengths.length; i++) {
            if (pathLengths[i] <= 0) {//якщо особина вже брала участь у кросовері
                continue;//йдемо на наступну ітерацію
            } else if (isMutation(mutationProbability)) {//випадкова мутація
                mutation.mutate(p, i * chromosomeLength, chromosomeLength);
                pathLengths[i] = -1;//позначаємо особину як опрацьовану
                processedParentsCounter++;//інкрементуємо лічильник оброблених особин
                continue;
            }
            int j = searcher.findSecond(i);//шукаємо пару для кросоверу
            int p1 = i * chromosomeLength;
            int p2 = j * chromosomeLength;
            if (j == PARENT_NOT_FOUND) {//Кейс останньої особини
                mutation.mutate(p, p1, chromosomeLength);//робимо її мутацію
                pathLengths[i] = -1;
                processedParentsCounter++;
                continue;
            } else if (p.equalsSubChromosomes(p1, p2, chromosomeLength)) {//якщо особини однакові
                mutation.mutate(p, p1, chromosomeLength);//першу мутуємо, іншу лишаємо без змін
                pathLengths[i] = pathLengths[j] = -1;//позначаємо обидві як опрацьовані
                processedParentsCounter += 2;//інкрементуємо лічильник на 2
                continue;
            }
            //якщо знайдені особини різні проводимо кросовер
            parent1.fillWith(0, p, p1, chromosomeLength);
            parent2.fillWith(0, p, p2, chromosomeLength);
            pathLengths[i] = pathLengths[j] = -1;
            //створюємо два нащадки
            crossoverMethod.createTwoChildren(parent1, parent2, child1, child2);
            p.fillWith(p1, child1, 0, chromosomeLength);//записуємо нащадок 1
            p.fillWith(p2, child2, 0, chromosomeLength);//записуємо нащадок 2
            processedParentsCounter += 2;//інкрементуємо лічильник на 2
        }
    }

    private boolean isMutation(float probability) {
        return rand.nextFloat(1) < probability;
    }
}
