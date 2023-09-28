package org.example.travellingsalesmanservice.app.domain;

import lombok.*;
import org.example.travellingsalesmanservice.algorithm.domain.Result;

@Builder(toBuilder = true)
public record ResultResponse(Result result, int currentIteration,
                             boolean hasNext, String message,
                             boolean isNewBestResult) {

    public static ResultResponse getNewBestResult(Result bestResult, int i) {
        return ResultResponse.builder().result(bestResult)
                .currentIteration(i)
                .hasNext(true)
                .message("New best result")
                .isNewBestResult(true).build();
    }

    public static ResultResponse getErrorResult(String message, int i) {
        return ResultResponse.builder().result(null)
                .currentIteration(i)
                .hasNext(false)
                .message(message)
                .isNewBestResult(false).build();
    }

    public static ResultResponse getShowResult(Result bestResult, int i) {
        return ResultResponse.builder().result(bestResult)
                .currentIteration(i)
                .hasNext(true)
                .message("Show iteration")
                .isNewBestResult(false).build();
    }

    public static ResultResponse getFinishResult(Result bestResult, int i) {
        return ResultResponse.builder().result(bestResult)
                .currentIteration(i)
                .hasNext(false)
                .message(STR."Finished all iterations.")
                .isNewBestResult(false).build();
    }

    public static ResultResponse getResultCounter(Result result, int i, int counterOfSameResults) {
        return ResultResponse.builder().result(result)
                .currentIteration(i)
                .hasNext(false)
                .message(STR. "Finished. Counter of the same result: \{ counterOfSameResults }" )
                .isNewBestResult(false).build();
    }
}
