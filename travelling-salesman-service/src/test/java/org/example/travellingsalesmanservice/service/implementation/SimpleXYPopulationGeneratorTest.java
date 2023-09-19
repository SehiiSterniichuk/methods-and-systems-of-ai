package org.example.travellingsalesmanservice.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.domain.Dataset;
import org.example.travellingsalesmanservice.domain.Point;
import org.example.travellingsalesmanservice.service.XYPopulationGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class SimpleXYPopulationGeneratorTest {
    private final XYPopulationGenerator generator = new SimpleXYPopulationGenerator();
    final int N = 5;
    final int amountOfGenes = 3;
    Point[] array = IntStream.range(0, N).mapToObj(x -> new Point(x, x + 1)).toArray(Point[]::new);

    @Test
    void checkLength() {
        XYPopulationGenerator.Points points = generator.generateChromosomes(new Dataset(array), amountOfGenes);
        assertEquals(points.x().length, N * amountOfGenes + 1);
        assertEquals(points.y().length, N * amountOfGenes + 1);
    }

    @Test
    void checkContains() {
        XYPopulationGenerator.Points points = generator.generateChromosomes(new Dataset(array), amountOfGenes);
        for (int geneIndex = 0; geneIndex < amountOfGenes; geneIndex++) {
            int startIndex = geneIndex * N;
            int endIndex = startIndex + N;
            long xCount = Arrays.stream(points.x(), startIndex, endIndex).distinct().count();
            long yCount = Arrays.stream(points.y(), startIndex, endIndex).distinct().count();
            assertEquals(xCount, N);
            assertEquals(yCount, N);
        }
    }
}