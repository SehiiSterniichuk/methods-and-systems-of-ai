package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.StringTemplate.STR;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CrossoverMethodTest {
    void testWhenOneShift(CrossoverMethod crossoverMethod) {
        int[] xData = {1, 2, 34, 5, 7};
        int[] yData = {1, 2, 34, 5, 7};
        int[] xData2 = {1, 34, 5,7, 2};
        int[] yData2 = {1, 34, 5,7, 2};
        testDuplicate(xData, yData, xData2, yData2, crossoverMethod);
    }

    @Test
    void testWhenOneShiftCyclic() {
        testWhenOneShift(new CycleCrossoverMethod());
    }

    @Test
    void testWhenOneShiftOnePoint() {
        testWhenOneShift(new OnePointCrossoverMethod());
    }



    @Test
    void testAcademicSolution() {
        int[] xData = IntStream.range(0, 10).toArray();
        int[] yData = Arrays.copyOf(xData, xData.length);
        int[] xDataCheck = Arrays.copyOf(xData, xData.length);
        int[] xData2 = {0, 9, 3, 7, 8, 2, 6, 5, 1, 4};
        int[] xData2Check = Arrays.copyOf(xData2, xData2.length);
        int[] yData2 = Arrays.copyOf(xData2, xData2.length);
        assertEquals(yData2.length, yData.length);
        // Create two parents with the same city at the same index
        Chromosome parent1 = new Chromosome(xData, yData);
        Chromosome parent2 = new Chromosome(xData2, yData2);
        // Create empty child chromosomes
        Chromosome child1 = Chromosome.ofLength(xData.length);
        Chromosome child2 = Chromosome.ofLength(xData.length);
        // Create a CrossoverMethod implementation
        CrossoverMethod crossoverMethod = new CycleCrossoverMethod(); // Replace with your actual implementation
        log.info("parent1: " + parent1);
        log.info("parent2: " + parent2);
        // Perform crossover
        crossoverMethod.createTwoChildren(parent1, parent2, child1, child2);
        // Verify that the children have unique cities (no duplicates)
        log.info("child1: " + child1);
        log.info("child2: " + child2);
        var samePositions = List.of(1, 4, 8, 6, 9);
        for (var pos : samePositions) {
            assertEquals(xDataCheck[pos], xData[pos]);
            assertEquals(xData2Check[pos], xData2[pos]);
        }
        int[] newPositions = IntStream.range(1, 10)
                .filter(x -> !samePositions.contains(x))
                .toArray();
        for (var newPos : newPositions) {
            assertNotEquals(xData2Check[newPos], xData[newPos]);
            assertNotEquals(xDataCheck[newPos], xData2[newPos]);
        }
    }
    @Test
    void testWhenTwoShiftCyclicMethod() {
        testWhenTwoShift(new CycleCrossoverMethod());
    }

    @Test
    void testWhenTwoShiftOnePointMethod() {
        testWhenTwoShift(new OnePointCrossoverMethod());
    }


    void testWhenTwoShift(CrossoverMethod crossoverMethod) {
        int[] xData = {0, 1, 2, 34, 5};
        int[] yData = {0, 1, 2, 34, 5};
        int[] xData2 = {0, 34, 5, 1, 2};
        int[] yData2 = {0, 34, 5, 1, 2};
        testDuplicate(xData, yData, xData2, yData2, crossoverMethod);
    }

    private void testDuplicate(int[] xData, int[] yData, int[] xData2, int[] yData2, CrossoverMethod crossoverMethod) {
        // Create two parents with the same city at the same index
        Chromosome parent1 = new Chromosome(xData, yData);
        Chromosome parent2 = new Chromosome(xData2, yData2);
        // Create empty child chromosomes
        Chromosome child1 = Chromosome.ofLength(xData.length);
        Chromosome child2 = Chromosome.ofLength(xData.length);
        // Create a CrossoverMethod implementation
        log.info(parent1.toString());
        log.info(parent2.toString());
        // Perform crossover
        crossoverMethod.createTwoChildren(parent1, parent2, child1, child2);
        // Verify that the children have unique cities (no duplicates)
        log.info(child1.toString());
        assertTrue(hasUniqueCities(child1));
        log.info(child2.toString());
        assertTrue(hasUniqueCities(child2));
    }

    // Helper method to check if a chromosome has unique cities
    @SuppressWarnings({"preview", "unused"})
    private boolean hasUniqueCities(Chromosome chromosome) {
        for (int i = 0; i < chromosome.size(); i++) {
            for (int j = i + 1; j < chromosome.size(); j++) {
                if (chromosome.equalsPoints(i, j)) {
                    var x = chromosome.x();
                    log.error(STR."i=\{i}, j=\{j}, x: \{x[i]}");
                    return false; // Duplicate city found
                }
            }
        }
        return true; // No duplicates found
    }
}