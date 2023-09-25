package org.example.travellingsalesmanservice.algorithm.service;

import lombok.SneakyThrows;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.algorithm.domain.Result;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static java.lang.StringTemplate.STR;

public class TrackingEntity {
    private final BlockingQueue<ResultResponse> queue = new LinkedBlockingQueue<>();
    private final long timeout;
    private static final long timeoutCoefficient = 10;
    Result timeoutResult = Result.builder().path(new Point[0]).pathLength(-1).build();
    private final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
    @SuppressWarnings("unused")
    private final String timeUnitString = timeUnit.toString();


    public TrackingEntity(TaskConfig config, int datasetLength) {
        this.timeout = ((long) config.populationSize() *
                config.iterationNumber() *
                datasetLength *
                config.showEachIterationStep() * timeoutCoefficient);
    }

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
        ResultResponse poll = queue.poll(timeout, timeUnit);
        return poll != null ? poll : getTimeoutResponse();
    }

    @SuppressWarnings("preview")
    private ResultResponse getTimeoutResponse() {
        return ResultResponse.builder()
                .result(timeoutResult)
                .message(STR."TIMEOUT: \{this.timeout} of \{timeUnitString}")
                .currentIteration(-1)
                .hasNext(false)
                .build();
    }

}
