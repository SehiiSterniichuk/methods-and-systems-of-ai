package org.example.travellingsalesmanservice.algorithm.domain;

import jakarta.validation.constraints.Size;

public record Dataset(
        @Size(min = 3)
        Point[] data) {
}
