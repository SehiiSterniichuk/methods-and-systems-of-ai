package ua.kpi.iasa.sd.hopfieldneuralnetwork.service;

import org.junit.jupiter.api.Test;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Pattern;
import ua.kpi.iasa.sd.hopfieldneuralnetwork.domain.Weight;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class HopfieldCalculatorTest {

    @Test
    void findPattern() {
        var calculator = new HopfieldCalculator();
        var pattern1 = new byte[][]{
                {1, 1, 1, 1, 1, 1},
                {0, 0, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0},
                {0, 0, 1, 1, 0, 0}
        };
        var pattern2 = new byte[][]{
                {1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1}
        };
        var pattern1Modified = new byte[][]{
                {1, 1, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 0},
                {0, 0, 1, 1, 1, 0},
                {0, 0, 1, 1, 0, 1},
                {0, 0, 1, 1, 0, 0}
        };
        byte[][][] pattern11 = {pattern1, pattern2};
        Weight weight = calculator.calculateWeightMatrix(pattern11);
        System.out.println(Arrays.deepToString(weight.w()).replaceAll("], ", "],\n"));
        Pattern response = calculator.recallPattern(new Pattern(pattern1Modified), weight, 3);
        System.out.println(Arrays.deepToString(response.p()).replaceAll("], ", "],\n"));
        assertArrayEquals(response.p(), pattern1);
    }
}