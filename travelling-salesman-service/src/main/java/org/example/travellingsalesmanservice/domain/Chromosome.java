package org.example.travellingsalesmanservice.domain;

public record Chromosome(int[] x, int[] y) {
    public static Chromosome ofLength(int length) {
        return new Chromosome(new int[length], new int[length]);
    }

    public void fillWith(int start, Chromosome source, int sourceStart, int length) {
        System.arraycopy(source.x, sourceStart, x, start, length);
        System.arraycopy(source.y, sourceStart, y, start, length);
    }

    @SuppressWarnings("unused")
    public boolean equals(int i, Chromosome p){
        return p.x[i] == x[i] && p.y[i] == y[i];
    }
}
