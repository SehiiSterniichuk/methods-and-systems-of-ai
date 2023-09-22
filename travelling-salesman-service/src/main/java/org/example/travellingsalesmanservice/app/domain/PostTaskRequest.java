package org.example.travellingsalesmanservice.app.domain;

import org.example.travellingsalesmanservice.algorithm.domain.Dataset;

public record PostTaskRequest(TaskConfig config, Dataset dataset) {
}
