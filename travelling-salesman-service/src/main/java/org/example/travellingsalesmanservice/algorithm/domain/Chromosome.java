package org.example.travellingsalesmanservice.algorithm.domain;

import java.util.Arrays;
import java.util.stream.IntStream;

public record Chromosome(int[] x, int[] y) {
    public static Chromosome ofLength(int length) {
        return new Chromosome(new int[length], new int[length]);
    }

    public void fillWith(int start, Chromosome source, int sourceStart, int length) {
        System.arraycopy(source.x, sourceStart, x, start, length);
        System.arraycopy(source.y, sourceStart, y, start, length);
    }

    public int size(){
        return x.length;
    }

    public boolean equals(int i, Chromosome p) {
        return p.x[i] == x[i] && p.y[i] == y[i];
    }

    public boolean equalsPoints(int i, int j) {
        return x[j] == x[i] && y[j] == y[i];
    }

    public int indexOf(int xP, int yP) {
        return IntStream.range(0, x.length)
                .filter(i -> x[i] == xP && y[i] == yP)
                .findAny().orElse(-1);
    }

//    public void swapPoints(int i, Chromosome c2) {
//        swap(x, c2.x, i);
//        swap(y, c2.y, i);
//    }

//    private void swap(int[] array, int[] array2, int i) {
//        int old = array[i];
//        array[i] = array2[i];
//        array2[i] = old;
//    }

    @Override
    public String toString() {
        return "Chromosome{" +
                "x=" + Arrays.toString(x) +
                ", y=" + Arrays.toString(y) +
                '}';
    }

    public void setFrom(int i, Chromosome parent) {
        x[i] = parent.x[i];
        y[i] = parent.y[i];
    }

    public void swapByIndex(int i, int j) {
        swapByIndex(i, j, x);
        swapByIndex(i, j, y);
    }

    private static void swapByIndex(int i, int j, int[] array) {
        var temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
