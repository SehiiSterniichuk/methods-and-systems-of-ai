package org.example.travellingsalesmanservice.algorithm.service.implementation;

import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
class InbreedingParentSearcher implements SecondParentSearcher {
    @Override
    public int findSecond(int firstParentIndex, int[] pathLengths) {
        int first = pathLengths[firstParentIndex];
        float diff = 0.3f;
        int min = (int) (first - first * diff);
        int max = (int) (first + first * diff);
        return IntStream.range(firstParentIndex, pathLengths.length)
                .parallel()
                .filter(i -> pathLengths[i] > 0)
                .filter(i -> pathLengths[i] > min && pathLengths[i] < max)
                .findAny()
                .orElse(IntStream.range(firstParentIndex, pathLengths.length)
                        .parallel()
                        .filter(i -> pathLengths[i] > 0)
                        .findAny().orElse(PARENT_NOT_FOUND));

    }
}
