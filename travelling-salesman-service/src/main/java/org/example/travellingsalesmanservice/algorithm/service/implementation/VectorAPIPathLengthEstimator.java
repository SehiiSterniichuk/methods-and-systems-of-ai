package org.example.travellingsalesmanservice.algorithm.service.implementation;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.PathLengthEstimator;
import org.springframework.stereotype.Component;

@Component
public class VectorAPIPathLengthEstimator implements PathLengthEstimator {
    public static final VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;

    @Override
    public void calculateSquaredPathLength(Chromosome chromosomes, int[] pathLengths) {
        calculateSquaredPathLength(chromosomes.x(), chromosomes.y(), pathLengths);
    }

    private void calculateSquaredPathLength(int[] xData, int[] yData, int[] pathLengths) {
        int length = xData.length - 1;//because last is starting point
        int[] squaredDistances = new int[length];
        for (int i = 0; i < length; i += species.length()) {
            var mask = species.indexInRange(i, length);
            IntVector x1Vec = IntVector.fromArray(species, xData, i, mask);
            IntVector y1Vec = IntVector.fromArray(species, yData, i, mask);
            IntVector x2Vec = IntVector.fromArray(species, xData, (i + 1), mask);
            IntVector y2Vec = IntVector.fromArray(species, yData, (i + 1), mask);
            var xDifference = x2Vec.sub(x1Vec);
            var xSquared = xDifference.mul(xDifference);
            var yDifference = y2Vec.sub(y1Vec);
            var ySquared = yDifference.mul(yDifference);
            var distance = ySquared.add(xSquared);
            distance.intoArray(squaredDistances, i, mask);
        }
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
