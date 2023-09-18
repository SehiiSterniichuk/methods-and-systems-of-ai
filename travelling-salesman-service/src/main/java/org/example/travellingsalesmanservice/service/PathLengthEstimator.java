package org.example.travellingsalesmanservice.service;

public interface PathLengthEstimator {
    void calculateSquaredPathLength(XYPopulationGenerator.Points chromosomes, int[] pathLengths);
}
