package org.example.travellingsalesmanservice.data.domain;

import lombok.Builder;
import org.example.travellingsalesmanservice.algorithm.domain.Point;

@Builder
public record Statistic(int iteration, double path, Point[] points) {
}
