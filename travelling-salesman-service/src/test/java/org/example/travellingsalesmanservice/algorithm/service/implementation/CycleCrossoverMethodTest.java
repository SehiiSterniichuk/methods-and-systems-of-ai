package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.junit.jupiter.api.Test;

import static java.lang.StringTemplate.STR;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class CycleCrossoverMethodTest {
    @Test
    void testWhenOneShift() {
        int[] xData = {1,2,34,5};
        int[] yData = {1,2,34,5};
        int[] xData2 = {2,34,5,1};
        int[] yData2 = {2,34,5,1};
        testDuplicate(xData, yData, xData2, yData2);
    }

    @Test
    void testWhenTwoShift() {
        int[] xData = {0, 1,2,34,5};
        int[] yData = {0, 1,2,34,5};
        int[] xData2 = {2,34,5,1, 0};
        int[] yData2 = {2,34,5,1, 0};
        testDuplicate(xData, yData, xData2, yData2);
    }

    private void testDuplicate(int[] xData, int[] yData, int[] xData2, int[] yData2) {
        // Create two parents with the same city at the same index
        Chromosome parent1 = new Chromosome(xData, yData);
        Chromosome parent2 = new Chromosome(xData2, yData2);
        // Create empty child chromosomes
        Chromosome child1 = Chromosome.ofLength(xData.length);
        Chromosome child2 = Chromosome.ofLength(xData.length);
        // Create a CrossoverMethod implementation
        CrossoverMethod crossoverMethod = new CycleCrossoverMethod(); // Replace with your actual implementation
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