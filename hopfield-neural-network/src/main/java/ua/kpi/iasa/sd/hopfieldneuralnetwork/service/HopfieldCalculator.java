package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.Arrays;
import java.util.List;

@Component
public class HopfieldCalculator {
    public Weight calc(List<Pattern> patterns) {
        var p = patterns.stream().map(Pattern::p).toArray(int[][][]::new);
        return calc(p);
    }

    public Weight calc(int[][][] p) {
        int dimension = p[0].length;
        float[][] weight = new float[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i != j) {
                    for (int[][] neighbors : p) {
                        if(neighbors[i][j] != 1){
                            continue;
                        }
                        weight[i][j] += neighbors[i][j];
                    }
                    weight[i][j] /= dimension;
                }
            }
        }
        return new Weight(weight);
    }

    public Pattern findPattern(Pattern pattern, Weight weight, int iterationNumber, float threshold) {
        int[][] y = Arrays.stream(pattern.p()).map(a -> {
            var newA = new int[a.length];
            System.arraycopy(a, 0, newA, 0, a.length);
            return newA;
        }).toArray(int[][]::new);
        float[][] w = weight.w();
        int dimension = weight.w().length;
        var y1 = new int[dimension][dimension];
        for (int iteration = 0; iteration < iterationNumber; iteration++) {
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    float weightedSum = 0;
                    for (int k = 0; k < dimension; k++) {
                        weightedSum += w[i][k] * y[k][j];
                    }
                    y1[i][j] = (weightedSum >= threshold) ? 1 : -1;
                }
            }
            var temp = y;
            y = y1;
            y1 = temp;
        }
        return new Pattern(y);
    }
}
