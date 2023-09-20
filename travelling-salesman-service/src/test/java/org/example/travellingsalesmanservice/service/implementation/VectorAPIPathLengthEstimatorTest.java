package org.example.travellingsalesmanservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.domain.Chromosome;
import org.example.travellingsalesmanservice.service.PathLengthEstimator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@Slf4j
class VectorAPIPathLengthEstimatorTest {
    private final PathLengthEstimator estimator = new VectorAPIPathLengthEstimator();
    private final PathLengthEstimator simpleEstimator = new SimplePathLengthEstimator();
    @Test
    public void testExactNumberOfGenesAsSpeciesSupport() {
        int length = VectorAPIPathLengthEstimator.species.length();
        int[] xData = IntStream.range(0, length + 1).toArray();
        xData[length] = 0;
        int[] yData = new int[length + 1];
        System.arraycopy(xData, 0, yData, 0, xData.length);
        int[] actual = new int[1];
        Chromosome chromosomes = new Chromosome(xData, yData);
        estimator.calculateSquaredPathLength(chromosomes, actual);
        int[] expected = new int[1];
        simpleEstimator.calculateSquaredPathLength(chromosomes, expected);
        assertArrayEquals(expected, actual);
        log.info(Arrays.toString(actual));
    }

    @Test
    public void testLessNumberOfGenesAsSpeciesSupport() {
        int length = VectorAPIPathLengthEstimator.species.length() / 2;
        int[] xData = IntStream.range(0, length + 1).toArray();
        xData[length] = 0;
        int[] yData = new int[length + 1];
        System.arraycopy(xData, 0, yData, 0, xData.length);
        int[] actual = new int[1];
        Chromosome chromosomes = new Chromosome(xData, yData);
        estimator.calculateSquaredPathLength(chromosomes, actual);
        int[] expected = new int[1];
        simpleEstimator.calculateSquaredPathLength(chromosomes, expected);
        assertArrayEquals(expected, actual);
        log.info(Arrays.toString(actual));
    }

    @Test
    public void testGreaterNumberOfGenesAsSpeciesSupport() {
        int length = VectorAPIPathLengthEstimator.species.length() + 10;
        int[] xData = IntStream.range(0, length + 1).toArray();
        xData[length] = 0;
        int[] yData = new int[length + 1];
        System.arraycopy(xData, 0, yData, 0, xData.length);
        int[] actual = new int[1];
        Chromosome chromosomes = new Chromosome(xData, yData);
        estimator.calculateSquaredPathLength(chromosomes, actual);
        int[] expected = new int[1];
        simpleEstimator.calculateSquaredPathLength(chromosomes, expected);
        assertArrayEquals(expected, actual);
        log.info(Arrays.toString(actual));
    }


    @Test
    public void testLessNumberOfGenesAsSpeciesSupportMultiplePaths() {
        int numberOfPaths = 5;
        int length = VectorAPIPathLengthEstimator.species.length() / 2;
        int maxValueExclusive = length + 1;
        int[] xData = IntStream.range(0, (maxValueExclusive * numberOfPaths) + 1).map(x -> x % maxValueExclusive).toArray();
        xData[xData.length - 1] = 0;
        int[] yData = new int[xData.length];
        System.arraycopy(xData, 0, yData, 0, xData.length);
        int[] actual = new int[numberOfPaths];
        Chromosome chromosomes = new Chromosome(xData, yData);
        estimator.calculateSquaredPathLength(chromosomes, actual);
        int[] expected = new int[numberOfPaths];
        simpleEstimator.calculateSquaredPathLength(chromosomes, expected);
        assertArrayEquals(expected, actual);
        log.info(Arrays.toString(actual));
    }

    @Test
    public void testGreaterNumberOfGenesAsSpeciesSupportMultiplePaths() {
        int numberOfPaths = 5;
        int length = (int)(VectorAPIPathLengthEstimator.species.length() * 1.5);
        int maxValueExclusive = length + 1;
        int[] xData = IntStream.range(0, (maxValueExclusive * numberOfPaths) + 1).map(x -> x % maxValueExclusive).toArray();
        xData[xData.length - 1] = 0;
        int[] yData = new int[xData.length];
        System.arraycopy(xData, 0, yData, 0, xData.length);
        int[] actual = new int[numberOfPaths];
        Chromosome chromosomes = new Chromosome(xData, yData);
        estimator.calculateSquaredPathLength(chromosomes, actual);
        int[] expected = new int[numberOfPaths];
        simpleEstimator.calculateSquaredPathLength(chromosomes, expected);
        assertArrayEquals(expected, actual);
        log.info(Arrays.toString(actual));
    }

    @Test
    public void testExactNumberOfGenesAsSpeciesSupportMultiplePaths() {
        int numberOfPaths = 5;
        int length = VectorAPIPathLengthEstimator.species.length();
        int maxValueExclusive = length + 1;
        int[] xData = IntStream.range(0, (maxValueExclusive * numberOfPaths) + 1).map(x -> x % maxValueExclusive).toArray();
        xData[xData.length - 1] = 0;
        int[] yData = new int[xData.length];
        System.arraycopy(xData, 0, yData, 0, xData.length);
        int[] actual = new int[numberOfPaths];
        Chromosome chromosomes = new Chromosome(xData, yData);
        estimator.calculateSquaredPathLength(chromosomes, actual);
        int[] expected = new int[numberOfPaths];
        simpleEstimator.calculateSquaredPathLength(chromosomes, expected);
        assertArrayEquals(expected, actual);
        log.info(Arrays.toString(actual));
    }
}