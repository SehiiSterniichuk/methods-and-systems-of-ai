package org.example.travellingsalesmanservice.app.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.algorithm.domain.Point;
import org.example.travellingsalesmanservice.algorithm.domain.TaskId;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.app.service.TaskService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class TaskServiceImplTest {
    private final TaskService service;

    @Autowired
    TaskServiceImplTest(TaskService service) {
        this.service = service;
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    void checkStart(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .iterationNumber(3)
                .mutationProbability(0.2f)
                .populationSize(30)
                .showEachIterationStep(1)
                .allowedNumberOfGenerationsWithTheSameResult(3)
                .build();
        Point[] p = Stream.generate(() -> Point.getRandom(1000)).distinct().limit(limit).toArray(Point[]::new);
        TaskId task = service.createTask(config, new Dataset(p));
        long id = task.id();
        assertTrue(id > 0);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 7, 10})
    void getTaskSmallPopulation(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .iterationNumber(limit * 2)
                .mutationProbability((float) limit / 100)
                .populationSize(limit * 10)
                .showEachIterationStep(limit)
                .allowedNumberOfGenerationsWithTheSameResult(limit * 2)
                .build();
        getTask(limit, config);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20, 30})
    void getTaskBigPopulation(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .iterationNumber(limit * 40)
                .mutationProbability((float) limit / 50)
                .populationSize(limit * 1000)
                .showEachIterationStep(10)
                .allowedNumberOfGenerationsWithTheSameResult(limit * 2)
                .build();
        getTask(limit, config);
    }

    private void getTask(int limit, TaskConfig config) {
        log.info(config.toString());
        Point[] p = Stream.generate(() -> Point.getRandom(1000)).distinct().limit(limit).toArray(Point[]::new);
        TaskId task = service.createTask(config, new Dataset(p));
        log.info("input: "  + Arrays.toString(p));
        long id = task.id();
        assertTrue(id > 0);
        ResultResponse response = service.getTask(id);
        assertNotNull(response);
        log.info(response.toString());
        while (response.isHasNext()) {
            response = service.getTask(id);
            assertNotNull(response);
            log.info(response.toString());
            assertEquals(p.length, Arrays.stream(response.getResult().path()).distinct().count());
        }
        log.info(response.toString());
        assertNotNull(response.getResult());
        assertEquals(p.length, Arrays.stream(response.getResult().path()).distinct().count());
    }
}