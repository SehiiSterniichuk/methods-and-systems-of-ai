package org.example.travellingsalesmanservice.algorithm.service.implementation;

import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.algorithm.service.XYPopulationGenerator;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.IntStream;

@Component
class SimpleXYPopulationGenerator implements XYPopulationGenerator {
    private final Random rand = new Random();

    @Override
    public Chromosome generateChromosomes(Dataset dataset, int amountOfGenes) {
        Point[] data = dataset.data();
        int setLength = data.length;
        assert setLength >= 2;
        int allGenesNumber = setLength * amountOfGenes;
        int[] x = new int[allGenesNumber + 1];//+1 because last gene is always starting point
        int[] y = new int[allGenesNumber + 1];
        int[] genes = IntStream.range(1, setLength).toArray();//indexes of 'Point's
        int[] xData = Arrays.stream(data).mapToInt(Point::x).toArray();
        int[] yData = Arrays.stream(data).mapToInt(Point::y).toArray();
        int xStart = xData[0];
        int yStart = yData[0];
        for (int i = 0; i < allGenesNumber; i += setLength) {
            x[i] = xStart;
            y[i] = yStart;
            pasteGenes(x, xData, genes, (i + 1));
            pasteGenes(y, yData, genes, (i + 1));
            shuffle(genes);
        }
        x[allGenesNumber] = xStart;
        y[allGenesNumber] = yStart;
        return new Chromosome(x, y);
    }

    private void pasteGenes(int[] target, int[] source, int[] genes, int startIndex) {
        for (int i = 0, targetIndex = startIndex; i < genes.length; i++, targetIndex++) {
            target[targetIndex] = source[genes[i]];
        }
    }

    private void shuffle(int[] array) {
        int length = array.length;
        for (int i = 1; i < length; i++) {
            int randomIndexToSwap = rand.nextInt(length - 1) + 1;
            int temp = array[randomIndexToSwap];
            array[randomIndexToSwap] = array[i];
            array[i] = temp;
        }
    }
}
