package org.example.travellingsalesmanservice.algorithm.service.implementation;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.PathLengthEstimator;
import org.springframework.stereotype.Component;

@Component
public class VectorAPIPathLengthEstimator implements PathLengthEstimator {//клас обрахунку довжин шляхів популяції
    public static final VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;

    @Override
    public void calculateSquaredPathLength(Chromosome chromosomes, int[] pathLengths) {//передаємо хромосому та буфер для запису довжин шляхів
        calculateSquaredPathLength(chromosomes.x(), chromosomes.y(), pathLengths);
    }

    private void calculateSquaredPathLength(int[] xData, int[] yData, int[] pathLengths) {
        int length = xData.length - 1;//because last is starting point
        int[] squaredDistances = new int[length];//буфер для довжин шляхи між містами
        for (int i = 0; i < length; i += species.length()) {//даний цикл обраховує лише шляхи між містами
            var mask = species.indexInRange(i, length);
            IntVector x1Vec = IntVector.fromArray(species, xData, i, mask);//x1
            IntVector y1Vec = IntVector.fromArray(species, yData, i, mask);//y1
            IntVector x2Vec = IntVector.fromArray(species, xData, (i + 1), mask);//x2
            IntVector y2Vec = IntVector.fromArray(species, yData, (i + 1), mask);//y2
            var xDifference = x2Vec.sub(x1Vec);//dx
            var xSquared = xDifference.mul(xDifference);//dx^2
            var yDifference = y2Vec.sub(y1Vec);//dy
            var ySquared = yDifference.mul(yDifference);//dy^2
            var distance = ySquared.add(xSquared);//dx^2+dy^2
            distance.intoArray(squaredDistances, i, mask);//save dx^2+dy^2
        }
        //тепер обраховуємо довжину шляху кожної хромосоми та записуємо у масив pathLengths
        reduceDistancesToPaths(pathLengths, squaredDistances);
    }

    private void reduceDistancesToPaths(int[] pathLengths, int[] squaredDistances) {
        int geneLength = squaredDistances.length / pathLengths.length;
        for (int i = 0; i < squaredDistances.length; i += geneLength) {
            int endOfGene = i + geneLength;
            for (int j = i; j < endOfGene; ) {
                var mask = species.indexInRange(j, endOfGene);
                IntVector gene = IntVector.fromArray(species, squaredDistances, j, mask);
                int pathLength = gene.reduceLanes(VectorOperators.ADD, mask);
                pathLengths[i / geneLength] += pathLength;
                if (species.length() >= geneLength) {
                    break;
                }else {
                    j += species.length();
                }
            }
        }
    }
}
