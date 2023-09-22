package org.example.travellingsalesmanservice.algorithm.service;

import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;

public interface PathLengthEstimator {
    void calculateSquaredPathLength(Chromosome chromosomes, int[] pathLengths);
}
