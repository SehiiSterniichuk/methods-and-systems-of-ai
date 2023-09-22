package org.example.travellingsalesmanservice.algorithm.service;

import org.example.travellingsalesmanservice.algorithm.domain.AlgorithmConfiguration;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;

@SuppressWarnings("unused")
public interface TravellingSalesmanSolver {
    void start(Dataset dataset, AlgorithmConfiguration algorithmConfiguration);
}
