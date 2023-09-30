package org.example.travellingsalesmanservice.app.config;

import org.example.travellingsalesmanservice.app.service.implementation.TaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {
    @Bean
    public TaskExecutor taskExecutor() {
        ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutor(service);
    }
}
