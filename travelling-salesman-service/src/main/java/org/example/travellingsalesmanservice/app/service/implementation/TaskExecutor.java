package org.example.travellingsalesmanservice.app.service.implementation;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public record TaskExecutor(ExecutorService executorService) implements Executor {
    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }
}
