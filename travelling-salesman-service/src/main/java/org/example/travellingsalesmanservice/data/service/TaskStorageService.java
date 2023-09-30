package org.example.travellingsalesmanservice.data.service;

import org.example.travellingsalesmanservice.algorithm.domain.Dataset;
import org.example.travellingsalesmanservice.app.domain.ResultResponse;
import org.example.travellingsalesmanservice.app.domain.TaskConfig;
import org.example.travellingsalesmanservice.data.domain.Statistic;

import java.util.List;

public interface TaskStorageService {
    String createTask(TaskConfig config, Dataset dataset);

    void addMessage(String id, String s);

    void updateTask(String id, ResultResponse resultResponse);

    List<Statistic> findStatisticByTaskId(String id);
}
