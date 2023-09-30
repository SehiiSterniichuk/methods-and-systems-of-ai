package org.example.travellingsalesmanservice.algorithm.domain;

import lombok.Builder;

import java.util.Arrays;
import java.util.stream.IntStream;

@Builder
public record Result(Point[] path, double pathLength) {
    public boolean isBetterThan(Result r, double eps) {
        return r.pathLength - this.pathLength > eps;
    }

    public boolean isBetterThan(Result r) {
        return this.isBetterThan(r, 0.01);
    }

    @Override
    public String toString() {
        return "Result{" +
                "path=" + Arrays.toString(path) +
                ", pathLength=" + pathLength +
                '}';
    }

    public Chromosome toChromosome() {
        Chromosome c = Chromosome.ofLength(path.length);
        IntStream.range(0, path.length).forEach(i -> c.setPoint(i, path[i]));
        return c;
    }
}
