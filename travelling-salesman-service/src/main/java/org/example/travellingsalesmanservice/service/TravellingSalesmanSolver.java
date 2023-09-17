package org.example.travellingsalesmanservice.service;

import org.example.travellingsalesmanservice.domain.AlgorithmConfiguration;
import org.example.travellingsalesmanservice.domain.Dataset;
import org.example.travellingsalesmanservice.domain.TrackingEntity;

@SuppressWarnings("unused")
public interface TravellingSalesmanSolver {
    TrackingEntity start(Dataset dataset, AlgorithmConfiguration config);
}
