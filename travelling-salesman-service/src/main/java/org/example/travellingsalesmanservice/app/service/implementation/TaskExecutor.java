package org.example.travellingsalesmanservice.app.service.implementation;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public record TaskExecutor(ExecutorService executorService) implements Executor {
    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }

    public <T> Future<T> submit(Callable<T> callable){
        return executorService.submit(callable);
    }
}
