package org.example.travellingsalesmanservice.algorithm.service.implementation;

import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.example.travellingsalesmanservice.algorithm.domain.SearcherConfig;
import org.springframework.stereotype.Component;

@Component
public class SearcherFactory {
    public SecondParentSearcher getSearcher(SearcherConfig config, int[] pathLengths, Chromosome chromosome) {
        return switch (config.distance()) {
            case HAMMING -> getHamming(config, pathLengths, chromosome);
            case SCALAR -> getScalar(config, pathLengths);
        };
    }

    private SecondParentSearcher getScalar(SearcherConfig config, int[] pathLengths) {
        float diff = (float) config.diffPercent() / 100;
        return switch (config.breedingType()) {
            case INBREEDING -> new ScalarParentSearcher(pathLengths, true, diff);
            case OUTBREEDING -> new ScalarParentSearcher(pathLengths, false, diff);
        };
    }

    private SecondParentSearcher getHamming(SearcherConfig config, int[] pathLengths, Chromosome chromosome) {
        float diff = (float) config.diffPercent() / 100;
        int chromosomeLength = chromosome.size() / pathLengths.length;
        return switch (config.breedingType()) {
            case INBREEDING -> new HammingParentSearcher(pathLengths, chromosome,chromosomeLength, HammingParentSearcher.getMinInbreedingCounter(diff, chromosomeLength), chromosomeLength);
            case OUTBREEDING -> new HammingParentSearcher(pathLengths, chromosome,chromosomeLength, 1, HammingParentSearcher.getMaxOutbreedingCounter(diff, chromosomeLength) + 1);
        };
    }
}
