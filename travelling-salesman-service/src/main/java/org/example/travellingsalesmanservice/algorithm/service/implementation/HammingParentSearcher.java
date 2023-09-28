package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;

import java.util.stream.IntStream;

@RequiredArgsConstructor
public class HammingParentSearcher implements SecondParentSearcher {
    private final int[] pathLengths;
    private final Chromosome chromosome;
    private final int chromosomeLength;
    private final int minInclusive;
    private final int maxExclusive;

    @Override
    public int findSecond(int firstParentIndex) {
        int start = firstParentIndex + 1;
        return IntStream.range(start, pathLengths.length)
                .parallel()
                .filter(i -> pathLengths[i] > 0)
                .filter(i -> isRange(firstParentIndex, i))
                .findAny()
                .orElse(IntStream.range(start, pathLengths.length)
                        .parallel()
                        .filter(i -> pathLengths[i] > 0)
                        .findAny().orElse(PARENT_NOT_FOUND));
    }

    private boolean isRange(int firstParentIndex, int i) {
        float length = getHammingLength(i, firstParentIndex);
        return minInclusive <= length && length < maxExclusive;
    }

    private float getHammingLength(int p1, int p2) {
        int counter = 1;
        int pointer1 = p1 * chromosomeLength;
        int pointer2 = p2 * chromosomeLength;
        for (int i = 1; i < chromosomeLength; i++) {
            if (chromosome.equalsPoints(pointer1 + i, pointer2 + i)) {
                counter++;
            }
        }
        return counter;
    }

    public static int getMinInbreedingCounter(float diff, int chromosomeLength){
        return (int) (chromosomeLength * (1 - diff));
    }

    public static int getMaxOutbreedingCounter(float diff, int chromosomeLength){
        return (int) (chromosomeLength * diff);
    }
}
