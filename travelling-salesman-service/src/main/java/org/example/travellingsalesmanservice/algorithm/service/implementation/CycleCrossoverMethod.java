package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.IntStream;

@Component
class CycleCrossoverMethod implements CrossoverMethod {
    @Data
    @AllArgsConstructor
    static class MutablePoint {
        int x;
        int y;
    }

    @Override
    public void createTwoChildren(Chromosome parent1, Chromosome parent2, Chromosome child1, Chromosome child2) {
        int length = parent1.x().length;
        int startA = IntStream.range(1, parent1.size())
                .filter(x -> !parent1.equals(x, parent2))
                .findAny().orElse(-1);
        if (startA == -1) {
            throw new IllegalStateException("Parents are the same!");
        }
        var cycle1 = new int[length];
        var cycle2 = new int[length];
        Arrays.fill(cycle1, -1);
        Arrays.fill(cycle2, -1);
        child1.fillWith(0, parent1, 0, length);
        child2.fillWith(0, parent2, 0, length);
        cycle(parent1, parent2, cycle1, startA);
        cycle(parent2, parent1, cycle2, startA);
        buildChild(parent2, child1, length, cycle1);
        buildChild(parent1, child2, length, cycle2);
    }

    private static void buildChild(Chromosome parent2, Chromosome child1, int length, int[] cycle1) {
        for (int i = 1; i < length; i++) {
            int value = cycle1[i];
            if (value != -1) {
                child1.setFrom(i, parent2);
            }
        }
    }

    private void cycle(Chromosome parent1, Chromosome parent2, int[] cycle, int startA) {
        int a = cycle[startA] = parent1.indexOf(parent2.x()[startA], parent2.y()[startA]);
        for (int i = 0; a >= 1 && a != startA && i < parent1.size(); i++) {
            a = cycle[a] = parent1.indexOf(parent2.x()[a], parent2.y()[a]);
        }
    }
}
