package org.example.travellingsalesmanservice.app.domain;

import lombok.*;
import org.example.travellingsalesmanservice.algorithm.domain.Result;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
public class ResultResponse {
    private Result result;
    private int currentIteration;
    private boolean hasNext;
    private String message;

    @Override
    public String toString() {
        return "ResultResponse{" +
                "result=" + result +
                ", currentIteration=" + currentIteration +
                ", hasNext=" + hasNext +
                ", message='" + message + '\'' +
                '}';
    }
}
