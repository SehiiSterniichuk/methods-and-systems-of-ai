package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.Arrays;
import java.util.List;

@Component
@Log4j2
public class HopfieldCalculator {
    public Weight calculateWeightMatrix(List<Pattern> patterns) {
        var p = patterns.stream().map(Pattern::p).map(HopfieldCalculator::flattenPattern).toArray(int[][]::new);
        return new Weight(calculateWeightMatrixFromFlat(p));
    }

    public Weight calculateWeightMatrix(int[][][] p) {
        var flattenPatterns = Arrays.stream(p).map(HopfieldCalculator::flattenPattern).toArray(int[][]::new);
        int[][] weight = calculateWeightMatrixFromFlat(flattenPatterns);
        return new Weight(weight);
    }

    public int[][] calculateWeightMatrixFromFlat(int[][] patterns) {
        int patternSize = patterns[0].length;
        int[][] weightMatrix = new int[patternSize][patternSize];
        for (int[] pattern : patterns) {
            for (int i = 0; i < patternSize; i++) {
                for (int j = 0; j < patternSize; j++) {
                    if (i == j) continue;
                    weightMatrix[i][j] += pattern[i] * pattern[j];
                }
            }
        }
        return weightMatrix;
    }

    public Pattern recallPattern(Pattern pattern, Weight weight, int iterationNumber) {
        var y = flattenPattern(pattern.p());
        log.info(STR. "pattern:\{ y.length } \{ weight.w().length }" );
        var recalledPattern = reshapePattern(
                recallPattern(y, weight.w(), iterationNumber),
                pattern.p().length,
                pattern.p()[0].length);
        return new Pattern(recalledPattern);
    }

    public Pattern recallPattern(int[] input, Weight weight, int iterationNumber) {
        int length = (int) Math.sqrt(weight.w().length);
        var recalledPattern = reshapePattern(
                recallPattern(input, weight.w(), iterationNumber),
                length,
                length);
        return new Pattern(recalledPattern);
    }

    public int[] recallPattern(int[] input, int[][] weightMatrix, int iterationNumber) {
        int patternSize = input.length;
        int[] currentPattern = input.clone(); // Initialize the current pattern as the input
        int[] activationBuf = new int[patternSize];
        for (int it = 0; it < iterationNumber; ) {
            int[] newPattern = new int[patternSize];
            for (int i = 0; i < patternSize; i++) {
                int activation = 0;
                for (int j = 0; j < patternSize; j++) {
                    activation += weightMatrix[i][j] * currentPattern[j];
                }
                activationBuf[i] = activation;
            }
            int min, max;
            min = max = activationBuf[0];
            for (int i = 1; i < activationBuf.length; i++) {
                if (min > activationBuf[i]) {
                    min = activationBuf[i];
                } else if (max < activationBuf[i]) {
                    max = activationBuf[i];
                }
            }
            int threshold = (min + max) / 2;
            for (int i = 0; i < newPattern.length; i++) {
                newPattern[i] = (activationBuf[i] >= threshold) ? 1 : 0;
            }
            if (Arrays.equals(currentPattern, newPattern)) {
                it++;
            } else {
                it = 0;
            }
            currentPattern = newPattern.clone();
        }
        return currentPattern;
    }

    public static int[] flattenPattern(int[][] pattern) {
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
