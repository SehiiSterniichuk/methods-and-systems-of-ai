package org.example.travellingsalesmanservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultResponse {
    private Result result;
    private int currentIteration;
}
