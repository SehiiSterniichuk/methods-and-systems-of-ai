package org.example.travellingsalesmanservice.algorithm.domain;

import java.util.Random;

public record Point(int x, int y) {
    public static Point getRandom(int bound) {
        Random rand = new Random();
        int x = rand.nextInt() % bound;
        int y = rand.nextInt() % bound;
        return new Point(x, y);
    }

    public double distance(Point p) {
        int dx = p.x - x;
        int dy = p.y - y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
