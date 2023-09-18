package org.example.travellingsalesmanservice.service;

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class VectorAPIPathLengthEstimator implements PathLengthEstimator {
    private final VectorSpecies<Integer> species = IntVector.SPECIES_PREFERRED;

    @Override
    public void calculateSquaredPathLength(XYPopulationGenerator.Points chromosomes, int[] pathLengths) {
//        int length = chromosomes.x().length;
//        int batchSize = species.loopBound(length);
//        for (int i = 0; i < length; i += batchSize) {
//            IntVector x1Vec = IntVector.fromArray(species, chromosomes.x(), i);
//            IntVector y1Vec = IntVector.fromArray(species, chromosomes.y(), i);
//            IntVector x2Vec = IntVector.fromArray(species, chromosomes.x(), (i + 1), VectorMask.fromArray());
//            IntVector y2Vec = IntVector.fromArray(species, chromosomes.y(), (i + 1));
//        }
//        // Handle remaining elements outside the vectorized processing
//        for (int i = (length / batchSize) * batchSize; i < length; i++) {
//            int x = chromosomes.x()[i];
//            int y = chromosomes.y()[i];
//            int squaredLength = x * x + y * y;
//            pathLengths[i] = squaredLength;
//        }
    }
}
