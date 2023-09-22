package org.example.travellingsalesmanservice.algorithm.domain;

import lombok.Builder;

import java.util.Arrays;

@Builder
public record Result(Point[] path, double pathLength) {
    public boolean isBetterThan(Result r, double eps) {
        return r.pathLength - this.pathLength > eps;
    }

    public boolean isBetterThan(Result r) {
        return this.isBetterThan(r, 0.01);
    }

//    private boolean isEqualLength(Result r, double eps) {
//        return (this.pathLength - r.pathLength) < eps;
//    }

    @Override
    public String toString() {
        return "Result{" +
                "path=" + Arrays.toString(path) +
                ", pathLength=" + pathLength +
                '}';
    }
}
