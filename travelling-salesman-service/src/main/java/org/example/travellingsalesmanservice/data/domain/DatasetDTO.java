package org.example.travellingsalesmanservice.data.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.example.travellingsalesmanservice.algorithm.domain.Point;

@Builder
public record DatasetDTO(@NotEmpty String name, @Size(min = 5) Point[] points) {
}
