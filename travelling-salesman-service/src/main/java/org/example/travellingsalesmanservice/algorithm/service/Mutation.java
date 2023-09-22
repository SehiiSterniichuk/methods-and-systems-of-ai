package org.example.travellingsalesmanservice.algorithm.service;

import org.example.travellingsalesmanservice.algorithm.domain.Chromosome;

public interface Mutation {
    void mutate(Chromosome p, int start, int length);
}
