package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Chromosome;

public interface PathLengthEstimator {
    void calculateSquaredPathLength(Chromosome chromosomes, int[] pathLengths);
}
