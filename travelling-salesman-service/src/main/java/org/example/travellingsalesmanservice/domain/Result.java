package org.example.travellingsalesmanservice.domain;

public record Result(Point[] path, double pathLength) {
    public boolean isBetterThan(Result r, double eps){
        return this.pathLength - r.pathLength > eps;
    }

    public boolean isBetterThan(Result r){
        return this.isBetterThan(r, 0.01);
    }
}
