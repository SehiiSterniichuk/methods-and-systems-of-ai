package org.example.travellingsalesmanservice.algorithm.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.service.Mutation;
import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;
import org.springframework.stereotype.Component;

import java.util.random.RandomGenerator;

@RequiredArgsConstructor
@Component
public class RandomGeneMutation implements Mutation {
    private final RandomGenerator rand;

    @Override
    public void mutate(Chromosome p, int start, int length) {
        assert length >= 2;
        int swap1 = rand.nextInt(length);
        int swap2 = rand.ints().map(Math::abs).map(x -> x % length)
                .filter(x -> swap1 != x).findAny().orElse(Integer.MIN_VALUE);
        p.swapByIndex(swap1 + start, swap2 + start);
    }
}
