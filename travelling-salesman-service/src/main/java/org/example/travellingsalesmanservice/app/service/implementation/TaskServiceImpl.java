package org.example.travellingsalesmanservice.app.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.TaskId;
import org.example.travellingsalesmanservice.algorithm.service.*;
import org.example.travellingsalesmanservice.algorithm.service.implementation.SimpleTravellingSalesmanSolverFactory;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.app.service.TaskService;
import org.example.travellingsalesmanservice.data.service.TaskStorageService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import static java.lang.StringTemplate.STR;


@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskExecutor executor;
    private final TaskStorageService taskService;
    private final Map<String, TrackingEntityWrapper> map = new ConcurrentHashMap<>();
    private final SimpleTravellingSalesmanSolverFactory factory;

    record TrackingEntityWrapper(TrackingEntity e, Future<TrackingEntity> f) {
    }

    @Override
    public TaskId createTask(TaskConfig config, Dataset dataset) {
        var entity = new TrackingEntity(config, dataset.data().length);
        TravellingSalesmanSolver solver = factory.getGeneticSolver(config, dataset, entity);
        var future = executor.submit(solver::start);
        String id = taskService.createTask(config, dataset);
        log.info(STR. "new task with id: \{ id }" );
        map.put(id, new TrackingEntityWrapper(entity, future));
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new TaskId(id);
    }

    @Override
    @SuppressWarnings("preview")
    public ResultResponse getTask(String id) {
        var entity = map.get(id);
        log.debug(STR. "get id: \{ id }" );
        if (entity == null) {
            throw new IllegalStateException(STR. "task with id: \{ id } not found" );
        } else if (entity.e.isLastResultHasTaken() && !entity.e.isNotifiedAboutEnd()) {
            entity.e.setNotifiedAboutEnd(true);
            throw new IllegalStateException(STR. "task with id: \{ id } has received its last result" );
        }
        if (entity.e.isNotifiedAboutEnd()) {
            return null;
        }
        ResultResponse resultResponse = entity.e.get();
        log.debug(STR. "get resultresponse id: \{ id }" );
        if (!resultResponse.hasNext()) {
            lastResult(id, entity);
        }
        if (entity.f.state() == Future.State.FAILED) {
            resultResponse = failedTask(id, entity, resultResponse);
        }
        if (resultResponse.isNewBestResult()) {
            taskService.updateTask(id, resultResponse);
        }
        return resultResponse;
    }

    private ResultResponse failedTask(String id, TrackingEntityWrapper entity, ResultResponse resultResponse) {
        var throwable = entity.f.exceptionNow();
        String message = throwable.getMessage();
        log.error(message);
        resultResponse = resultResponse.toBuilder()
                .message(resultResponse.message() + " " + message)
                .build();
        taskService.addMessage(id, message);
        removeAfterTime(id);
        return resultResponse;
    }

    private void lastResult(String id, TrackingEntityWrapper entity) {
        String message;
        if (!entity.f.isDone()) {
            entity.f.cancel(true);
            message = STR. "task with id: \{ id } was canceled" ;
        } else {
            message = STR. "Task with id: \{ id } is finished" ;
        }
        log.info(message);
        taskService.addMessage(id, message);
    }

    private void removeAfterTime(String id) {
        executor.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            map.remove(id);
        });
    }
}
