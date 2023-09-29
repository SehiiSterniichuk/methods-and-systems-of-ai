package org.example.travellingsalesmanservice.app.service.implementation;

import lombok.extern.slf4j.Slf4j;
import org.example.travellingsalesmanservice.algorithm.domain.*;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.app.service.TaskService;
import org.example.travellingsalesmanservice.data.repository.TaskRepository;
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
    private final TaskRepository repository;

    private final SearcherConfig defaultSearcherConfig = SearcherConfig.builder()
            .breedingType(BreedingType.INBREEDING)
            .diffPercent(33)
            .distance(Distance.SCALAR)
            .build();
    private final SearcherConfig oppositeSearcherConfig = SearcherConfig.builder()
            .breedingType(BreedingType.OUTBREEDING)
            .diffPercent(33)
            .distance(Distance.HAMMING)
            .build();

    @Autowired
    TaskServiceImplTest(TaskService service, TaskRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    void checkStart(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .crossoverType(CrossoverType.CYCLIC)
                .searcherConfig(defaultSearcherConfig)
                .iterationNumber(3)
                .mutationProbability(0.2f)
                .populationSize(30)
                .showEachIterationStep(1)
                .allowedNumberOfGenerationsWithTheSameResult(3)
                .build();
        Point[] p = Stream.generate(() -> Point.getRandom(1000)).distinct().limit(limit).toArray(Point[]::new);
        TaskId task = service.createTask(config, new Dataset(p));
        var id = task.id();
        assertFalse(id.isBlank());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 7, 10})
    void getTaskSmallPopulation(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .crossoverType(CrossoverType.CYCLIC)
                .searcherConfig(defaultSearcherConfig)
                .iterationNumber(limit * 2)
                .mutationProbability((float) limit / 100)
                .populationSize(limit * 10)
                .showEachIterationStep(limit)
                .allowedNumberOfGenerationsWithTheSameResult(limit * 2)
                .build();
        getTask(limit, config);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 7, 10})
    void getTaskSmallPopulationOnePointCrossover(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .crossoverType(CrossoverType.ONE_POINT)
                .searcherConfig(defaultSearcherConfig)
                .iterationNumber(limit * 2)
                .mutationProbability((float) limit / 100)
                .populationSize(limit * 10)
                .showEachIterationStep(limit)
                .allowedNumberOfGenerationsWithTheSameResult(limit * 2)
                .build();
        getTask(limit, config);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 7, 10})
    void getTaskSmallPopulationOppositeScenario(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .crossoverType(CrossoverType.CYCLIC)
                .searcherConfig(oppositeSearcherConfig)
                .iterationNumber(limit * 2)
                .mutationProbability((float) limit / 100)
                .populationSize(limit * 10)
                .showEachIterationStep(limit)
                .allowedNumberOfGenerationsWithTheSameResult(limit * 2)
                .build();
        getTask(limit, config);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20})
    void getTaskBigPopulation(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .crossoverType(CrossoverType.CYCLIC)
                .searcherConfig(defaultSearcherConfig)
                .iterationNumber(limit * 40)
                .mutationProbability((float) limit / 50)
                .populationSize(limit * 1000)
                .showEachIterationStep(10)
                .allowedNumberOfGenerationsWithTheSameResult(limit * 2)
                .build();
        getTask(limit, config);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15, 20})
    void getTaskBigPopulationOppositeScenario(int limit) {
        TaskConfig config = TaskConfig
                .builder()
                .crossoverType(CrossoverType.CYCLIC)
                .searcherConfig(oppositeSearcherConfig)
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
        var id = task.id();
        assertFalse(id.isBlank());
        ResultResponse response = service.getTask(id);
        assertNotNull(response);
        log.info(response.toString());
        while (response.hasNext()) {
            response = service.getTask(id);
            assertNotNull(response);
            log.info(response.toString());
            assertEquals(p.length, Arrays.stream(response.result().path()).distinct().count());
        }
        log.info(response.toString());
        assertNotNull(response.result());
        assertEquals(p.length, Arrays.stream(response.result().path()).distinct().count());
        repository.deleteById(id).block();
    }
}