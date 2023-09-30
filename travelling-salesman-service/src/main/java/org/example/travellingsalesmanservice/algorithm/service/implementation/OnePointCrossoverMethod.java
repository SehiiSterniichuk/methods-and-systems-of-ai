package org.example.travellingsalesmanservice.algorithm.service.implementation;

import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverMethod;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class OnePointCrossoverMethod implements CrossoverMethod {
    @Override//одноточковий кросовер
    public void createTwoChildren(Chromosome parent1, Chromosome parent2, Chromosome child1, Chromosome child2) {
        int length = parent1.size();
        // Create sets to keep track of unique points in child1 and child2
        Set<Point> uniquePointsChild1 = new HashSet<>(length);
        Set<Point> uniquePointsChild2 = new HashSet<>(length);
        // Choose a random crossoverType point (excluding the first two points)
        int crossoverPoint = (int) (Math.random() * (length - 1)) + 1;
        // Copy the first part from parent1 to child1 and parent2 to child2
        copyFirstPart(parent1, child1, crossoverPoint, uniquePointsChild1);
        copyFirstPart(parent2, child2, crossoverPoint, uniquePointsChild2);
        // Copy the remaining unique points from parent2 to child1 and parent1 to child2
        insertSecondPart(parent1, parent2, child1, crossoverPoint, length, uniquePointsChild1);
        insertSecondPart(parent2, parent1, child2, crossoverPoint, length, uniquePointsChild2);
    }

    private void insertSecondPart(Chromosome parent1, Chromosome parent2, Chromosome child1, int crossoverPoint, int length, Set<Point> uniquePointsChild1) {
        for (int c = crossoverPoint, p = crossoverPoint; c < length; c++, p++) {
            p %= length;
            Point p2 = parent2.getPoint(p);
            if (!uniquePointsChild1.contains(p2)) {
                child1.setPoint(c, p2);
                uniquePointsChild1.add(p2);
                continue;
            }
            Point p1 = parent1.getPoint(p);
            if (!uniquePointsChild1.contains(p1)) {
                child1.setPoint(c, p1);
                uniquePointsChild1.add(p1);
            } else {
                c--;
            }
        }
    }

    private void copyFirstPart(Chromosome parent1, Chromosome child, int crossoverPoint, Set<Point> uniquePointsChild) {
        for (int i = 0; i < crossoverPoint; i++) {
            int x = parent1.x()[i];
            int y = parent1.y()[i];
            Point p = new Point(x, y);
            child.setFrom(i, parent1);
            uniquePointsChild.add(p);
        }
    }
}
