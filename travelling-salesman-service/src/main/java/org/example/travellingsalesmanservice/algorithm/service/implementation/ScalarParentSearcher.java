package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;

import java.util.OptionalInt;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
public class ScalarParentSearcher implements SecondParentSearcher {
    private final int[] pathLengths;
    private final boolean isInbreeding;
    private final float diff;

    @Override
    public int findSecond(int firstParentIndex) {
        int first = pathLengths[firstParentIndex];
        int min = (int) (first - first * diff);
        int max = (int) (first + first * diff);
        int start = firstParentIndex + 1;
        OptionalInt inRangeValue = IntStream.range(start, pathLengths.length)
                .parallel()
                .filter(i -> pathLengths[i] > 0)
                .filter(i -> isRange(i, min, max))
                .findAny();
        return inRangeValue
                .orElse(IntStream.range(start, pathLengths.length)
                        .parallel()
                        .filter(i -> pathLengths[i] > 0)
                        .findAny().orElse(PARENT_NOT_FOUND));
    }

    private boolean isRange(int i, int min, int max) {
        if (isInbreeding) {
            return pathLengths[i] >= min && pathLengths[i] < max;
        }
        return pathLengths[i] < min || pathLengths[i] >= max;
    }
}
