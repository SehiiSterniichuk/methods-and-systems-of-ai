package org.example.travellingsalesmanservice.algorithm.domain;

import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.example.travellingsalesmanservice.algorithm.service.TrackingEntity;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;

public record AlgorithmConfiguration(TaskConfig taskConfig,
                                     TrackingEntity trackingEntity,
                                     CrossoverAlgorithm crossoverAlgorithm,
                                     SecondParentSearcher searcher) {
}
