package org.example.travellingsalesmanservice.algorithm.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.travellingsalesmanservice.algorithm.domain.Result;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class TrackingEntity {
    private final BlockingQueue<ResultResponse> queue = new LinkedBlockingQueue<>();

//    @SneakyThrows
//    public void put(Result result, int currentIteration) {
//        put(result, currentIteration, null);
//    }

    @SneakyThrows
    public void put(Result result, int currentIteration, String message) {
        put(result, currentIteration, message, true);
    }

    @SneakyThrows
    public void putFinish(Result result, int currentIteration) {
        putFinish(result, currentIteration, null);
    }

    @SneakyThrows
    public void putFinish(Result result, int currentIteration, String message) {
        put(result, currentIteration, message, false);
    }

    @SneakyThrows
    public void put(Result result, int currentIteration, String message, boolean hasNext) {
        var r = ResultResponse.builder()
                .result(result)
                .currentIteration(currentIteration)
                .message(message)
                .hasNext(hasNext)
                .build();
        queue.put(r);
    }


    @SneakyThrows
    public ResultResponse get() {
        return queue.take();
    }
}
