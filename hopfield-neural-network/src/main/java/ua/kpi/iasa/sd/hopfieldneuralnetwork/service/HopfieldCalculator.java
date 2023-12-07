package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.springframework.stereotype.Component;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class HopfieldCalculator {
    public Weight calculateWeightMatrix(List<Pattern> patterns) {
        var p = patterns.stream().map(Pattern::p).map(HopfieldCalculator::flattenPattern).toArray(byte[][]::new);
        return new Weight(calculateWeightMatrixFromFlat(p));
    }

    public Weight calculateWeightMatrix(byte[][][] p) {
        var flattenPatterns = Arrays.stream(p).map(HopfieldCalculator::flattenPattern).toArray(byte[][]::new);
        var weight = calculateWeightMatrixFromFlat(flattenPatterns);
        return new Weight(weight);
    }

    public byte[][] calculateWeightMatrixFromFlat(byte[][] patterns) {
        int patternSize = patterns[0].length;
        var weightMatrix = new byte[patternSize][patternSize];
        for (var pattern : patterns) {
            for (int i = 0; i < patternSize; i++) {
                for (int j = 0; j < patternSize; j++) {
                    if (i == j) continue;
                    weightMatrix[i][j] += (byte) (pattern[i] * pattern[j]);
                }
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

    public Pattern recallPattern(byte[] input, Weight weight, int iterationNumber) {
        int length = (int) Math.sqrt(weight.w().length);
        var recalledPattern = reshapePattern(
                recallPattern(input, weight.w(), iterationNumber),
                length,
                length);
        return new Pattern(recalledPattern);
    }

    public byte[] recallPattern(byte[] input, byte[][] weightMatrix, int iterationNumber) {
        int patternSize = input.length;
        var currentPattern = copyByteToIntPattern(input);
        var newPattern = new int[patternSize];
        var activationBuf = new int[patternSize];
        for (int it = 0; it < iterationNumber; it++) {
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
            var temp = currentPattern;
            currentPattern = newPattern;
            newPattern = temp;
        }
        return copyIntToBytePattern(currentPattern);
    }

    private static int[] copyByteToIntPattern(byte[] input) {
        var receiver = new int[input.length];
        IntStream.range(0, input.length).parallel().forEach(i -> receiver[i] = input[i]);
        return receiver;
    }

    private static byte[] copyIntToBytePattern(int[] input) {
        var receiver = new byte[input.length];
        IntStream.range(0, input.length).parallel().forEach(i -> receiver[i] = (byte) input[i]);
        return receiver;
    }

    public static byte[] flattenPattern(byte[][] pattern) {
        int rows = pattern.length;
        int cols = pattern[0].length;
        var flattenedPattern = new byte[rows * cols];
        int index = 0;
        for (var ints : pattern) {
            for (int j = 0; j < cols; j++) {
                flattenedPattern[index++] = ints[j];
            }
        }
        return flattenedPattern;
    }

    public static byte[][] reshapePattern(byte[] pattern, int rows, int cols) {
        var reshapedPattern = new byte[rows][cols];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                reshapedPattern[i][j] = pattern[index++];
            }
        }
        return reshapedPattern;
    }
}
