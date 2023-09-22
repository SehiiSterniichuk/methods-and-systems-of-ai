package org.example.travellingsalesmanservice.app.config;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.app.service.implementation.TaskExecutor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExecutorShutdownHook implements DisposableBean {
    private final TaskExecutor executor;
    @Override
    public void destroy() {
        executor.executorService().shutdown();
    }
}
