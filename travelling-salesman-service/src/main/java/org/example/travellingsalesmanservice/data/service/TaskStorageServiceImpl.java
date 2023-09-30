package org.example.travellingsalesmanservice.data.service;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.Result;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.data.domain.Statistic;
import org.example.travellingsalesmanservice.data.domain.Task;
import org.example.travellingsalesmanservice.data.repository.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStorageServiceImpl implements TaskStorageService {
    private final TaskRepository repository;

    @Override
    public String createTask(TaskConfig config, Dataset dataset) {
        Task task = Task.builder().statisticList(List.of()).dataset(dataset).config(config).build();
        return repository.save(task).map(Task::getId).block();
    }

    @Override
    public void addMessage(String id, String s) {
        repository.findById(id)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(entity -> {
                    entity.setMessage(entity.getMessage() + " " + s);
                    return repository.save(entity);
                })
                .subscribe(); // Trigger the findById operation
    }


    @Override
    public void updateTask(String id, ResultResponse resultResponse) {
        repository.findById(id)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(x -> {
                    x.getStatisticList().add(toStatistic(resultResponse));
                    return repository.save(x);
                })
                .subscribe(); // Trigger the findById operation
    }

    @Override
    public List<Statistic> findStatisticByTaskId(String id) {
        return repository.findById(id).map(Task::getStatisticList).block();
    }

    private Statistic toStatistic(ResultResponse resultResponse) {
        Result result = resultResponse.result();
        return Statistic.builder()
                .iteration(resultResponse.currentIteration())
                .points(result.path())
                .path(result.pathLength())
                .build();
    }
}
