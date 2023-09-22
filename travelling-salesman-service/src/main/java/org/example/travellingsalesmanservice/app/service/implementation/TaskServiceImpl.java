package org.example.travellingsalesmanservice.app.service.implementation;

import lombok.RequiredArgsConstructor;
import org.example.travellingsalesmanservice.algorithm.domain.*;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.example.travellingsalesmanservice.algorithm.service.TrackingEntity;
import org.example.travellingsalesmanservice.algorithm.service.TravellingSalesmanSolver;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.app.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.StringTemplate.STR;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskExecutor executor;
    private final CrossoverAlgorithm crossoverAlgorithm;
    private final SecondParentSearcher searcher;
    private final TravellingSalesmanSolver solver;
    private final Map<Long, TrackingEntity> map = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public TaskId createTask(TaskConfig config, Dataset dataset) {
        var entity = new TrackingEntity();
        var algoConfig = new AlgorithmConfiguration(config, entity, crossoverAlgorithm, searcher);
        executor.execute(() -> solver.start(dataset, algoConfig));
        long id = counter.incrementAndGet();
        map.put(id, entity);
        return new TaskId(id);
    }

    @Override
    @SuppressWarnings("preview")
    public ResultResponse getTask(Long id) {
        TrackingEntity entity = map.get(id);
        assert entity != null : STR."task with id: \{id} not found";
        return entity.get();
    }
}
