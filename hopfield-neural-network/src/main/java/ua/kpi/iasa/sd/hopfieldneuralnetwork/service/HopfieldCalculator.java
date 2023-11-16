package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;
import java.util.Arrays;
import java.util.List;

@Component
public class HopfieldCalculator {
    public Weight calculateWeightMatrix(List<Pattern> patterns) {
        var p = patterns.stream().map(Pattern::p).map(this::flattenPattern).toArray(int[][]::new);
        return new Weight(calculateWeightMatrixFromFlat(p));
    }

    public Weight calculateWeightMatrix(int[][][] p) {
        var flattenPatterns = Arrays.stream(p).map(this::flattenPattern).toArray(int[][]::new);
        float[][] weight = calculateWeightMatrixFromFlat(flattenPatterns);
        return new Weight(weight);
    }

    public float[][] calculateWeightMatrixFromFlat(int[][] patterns) {
        int patternSize = patterns[0].length;
        float[][] weightMatrix = new float[patternSize][patternSize];
        for (int[] pattern : patterns) {
            for (int i = 0; i < patternSize; i++) {
                for (int j = 0; j < patternSize; j++) {
                    if(i == j) continue;
                    weightMatrix[i][j] += pattern[i] * pattern[j];
                }
            }
        }
        // Normalize the weight matrix
        for (int i = 0; i < patternSize; i++) {
            for (int j = 0; j < patternSize; j++) {
                weightMatrix[i][j] /= patternSize;
            }
        }
        return weightMatrix;
    }

    public Pattern recallPattern(Pattern pattern, Weight weight, int iterationNumber) {
        var y = flattenPattern(pattern.p());
        var recalledPattern = reshapePattern(
                recallPattern(y, weight.w(), iterationNumber),
                pattern.p().length,
                pattern.p()[0].length);
        return new Pattern(recalledPattern);
    }

    public static int[] recallPattern(int[] input, float[][] weightMatrix, int iterationNumber) {
        int patternSize = input.length;
        int[] currentPattern = input.clone(); // Initialize the current pattern as the input

        boolean stable = false;
        float[] activationBuf = new float[patternSize];
        for (int it = 0; !stable && it < iterationNumber; it++) {
            int[] newPattern = new int[patternSize];
            for (int i = 0; i < patternSize; i++) {
                float activation = 0;
                for (int j = 0; j < patternSize; j++) {
                    activation += weightMatrix[i][j] * currentPattern[j];
                }
                activationBuf[i] = activation;
            }
            float min, max;
            min = max = activationBuf[0];
            for (int i = 1; i < activationBuf.length; i++) {
                if (min > activationBuf[i]) {
                    min = activationBuf[i];
                } else if (max < activationBuf[i]) {
                    max = activationBuf[i];
                }
            }
            float threshold = (min + max) / 2;
            for (int i = 0; i < newPattern.length; i++) {
                newPattern[i] = (activationBuf[i] >= threshold) ? 1 : 0;
            }
            stable = java.util.Arrays.equals(currentPattern, newPattern);
            currentPattern = newPattern.clone();
        }
        return currentPattern;
    }

    private int[] flattenPattern(int[][] pattern) {
        int rows = pattern.length;
        int cols = pattern[0].length;
        int[] flattenedPattern = new int[rows * cols];
        int index = 0;
        for (int[] ints : pattern) {
            for (int j = 0; j < cols; j++) {
                flattenedPattern[index++] = ints[j];
            }
        }
        return flattenedPattern;
    }

    public static int[][] reshapePattern(int[] pattern, int rows, int cols) {
        int[][] reshapedPattern = new int[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                reshapedPattern[i][j] = pattern[index++];
            }
        }
        return reshapedPattern;
    }
}
