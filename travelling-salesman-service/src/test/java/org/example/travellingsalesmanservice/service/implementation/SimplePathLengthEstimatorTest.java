package org.example.travellingsalesmanservice.service.implementation;

import org.example.travellingsalesmanservice.domain.Chromosome;
import org.example.travellingsalesmanservice.service.PathLengthEstimator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SimplePathLengthEstimatorTest {
    private final PathLengthEstimator estimator = new SimplePathLengthEstimator();

    @Test
    void calculateSquaredPathLength() {
        var path = new int[2];
        estimator.calculateSquaredPathLength(new Chromosome(new int[]{0, 1, 0}, new int[]{1, 2, 1}), path);
        assertArrayEquals(new int[]{2, 2}, path);
    }
}