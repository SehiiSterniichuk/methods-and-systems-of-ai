package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.Chromosome;

public interface Mutation {
    void mutate(Chromosome p, int start, int length);
}
