package org.example.travellingsalesmanservice.app.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.AlgorithmConfiguration;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.TaskId;
import org.example.travellingsalesmanservice.algorithm.service.CrossoverAlgorithm;
import org.example.travellingsalesmanservice.algorithm.service.SecondParentSearcher;
import org.example.travellingsalesmanservice.algorithm.service.TrackingEntity;
import org.example.travellingsalesmanservice.algorithm.service.TravellingSalesmanSolver;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.app.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.StringTemplate.STR;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskExecutor executor;
    private final CrossoverAlgorithm crossoverAlgorithm;
    private final SecondParentSearcher searcher;
    private final TravellingSalesmanSolver solver;
    private final Map<Long, TrackingEntityWrapper> map = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    record TrackingEntityWrapper(TrackingEntity e, Future<TrackingEntity> f) {
    }

    @Override
    public TaskId createTask(TaskConfig config, Dataset dataset) {
        var entity = new TrackingEntity(config, dataset.data().length);
        var algoConfig = new AlgorithmConfiguration(config, entity, crossoverAlgorithm, searcher);
        var future = executor.submit(() -> solver.start(dataset, algoConfig));
        long id = counter.incrementAndGet();
        map.put(id, new TrackingEntityWrapper(entity, future));
        log.info("Submitted task with id: {}", id);
        return new TaskId(id);
    }

    @Override
    @SuppressWarnings("preview")
    public ResultResponse getTask(Long id) {
        var entity = map.get(id);
        if (entity == null) {
            throw new IllegalStateException(STR. "task with id: \{ id } not found" );
        }
        ResultResponse resultResponse = entity.e.get();
        if (!resultResponse.isHasNext()) {
            map.remove(id);
            if (!entity.f.isDone()) {
                entity.f.cancel(true);
                log.info(STR. "task with id: \{ id } was canceled" );
            } else {
                log.info(STR. "Task with id: \{ id } is finished" );
            }
        }
        if (entity.f.state() == Future.State.FAILED) {
            var throwable = entity.f.exceptionNow();
            log.error(throwable.getMessage());
            resultResponse = resultResponse.toBuilder()
                    .message(resultResponse.getMessage() + " " + throwable.getMessage())
                    .build();
        }
        return resultResponse;
    }
}
